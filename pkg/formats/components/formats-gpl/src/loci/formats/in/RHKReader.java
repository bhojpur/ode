package loci.formats.in;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

import ode.units.quantity.Length;

/**
 * RHKReader is the file format reader for RHK Technologies files.
 */
public class RHKReader extends FormatReader {

  // -- Constants --

  private static final int HEADER_SIZE = 512;

  // -- Fields --

  private boolean invertX = false, invertY = false;
  private long pixelOffset = 0;

  // -- Constructor --

  /** Constructs a new RHK reader. */
  public RHKReader() {
    super("RHK Technologies", new String[] {"sm2", "sm3"});
    domains = new String[] {FormatTools.SPM_DOMAIN};
  }

  // -- IFormatReader API methods --

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);
    in.seek(pixelOffset);

    int realX = invertX ? getSizeX() - x - w : x;
    int realY = invertY ? getSizeY() - y - h : y;

    readPlane(in, realX, realY, w, h, buf);

    int bpp = FormatTools.getBytesPerPixel(getPixelType());
    if (invertY) {
      byte[] rowBuf = new byte[w * bpp];
      for (int row=0; row<h/2; row++) {
        int top = row * rowBuf.length;
        int bottom = (h - row - 1) * rowBuf.length;
        System.arraycopy(buf, top, rowBuf, 0, rowBuf.length);
        System.arraycopy(buf, bottom, buf, top, rowBuf.length);
        System.arraycopy(rowBuf, 0, buf, bottom, rowBuf.length);
      }
    }
    if (invertX) {
      byte[] pixel = new byte[bpp];
      for (int row=0; row<h; row++) {
        for (int col=0; col<w/2; col++) {
          int left = row * w * bpp + col * bpp;
          int right = row * w * bpp + (w - col - 1) * bpp;
          System.arraycopy(buf, left, pixel, 0, bpp);
          System.arraycopy(buf, right, buf, left, bpp);
          System.arraycopy(pixel, 0, buf, right, bpp);
        }
      }
    }

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      invertX = false;
      invertY = false;
      pixelOffset = 0;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);
    CoreMetadata m = core.get(0);

    m.littleEndian = true;
    in.order(isLittleEndian());

    boolean xpm = in.readShort() == 0xaa;

    int dataType = -1;

    if (xpm) {
      in.seek(40);
      int imageType = in.readInt();
      int pageType = in.readInt();
      dataType = in.readInt();
      int lineType = in.readInt();
      in.skipBytes(8);
      m.sizeX = in.readInt();
      m.sizeY = in.readInt();
      in.skipBytes(16);
      pixelOffset = in.readInt();
    }
    else {
      in.seek(32);
      String[] typeData = in.readString(32).trim().split(" ");
      int imageType = Integer.parseInt(typeData[0]);
      dataType = Integer.parseInt(typeData[1]);
      int lineType = Integer.parseInt(typeData[2]);
      m.sizeX = Integer.parseInt(typeData[3]);
      m.sizeY = Integer.parseInt(typeData[4]);
      int pageType = Integer.parseInt(typeData[6]);
      pixelOffset = HEADER_SIZE;
    }

    switch (dataType) {
      case 0:
        m.pixelType = FormatTools.FLOAT;
        break;
      case 1:
        m.pixelType = FormatTools.INT16;
        break;
      case 2:
        m.pixelType = FormatTools.INT32;
        break;
      case 3:
        m.pixelType = FormatTools.UINT8;
        break;
      default:
        throw new FormatException("Unsupported data type: " + dataType);
    }

    double xScale = 0d, yScale = 0d;

    if (xpm) {
      in.skipBytes(8);
      xScale = in.readFloat() * 1000000;
      yScale = in.readFloat() * 1000000;
    }
    else {
      String[] xAxis = in.readString(32).trim().split(" ");
      String[] yAxis = in.readString(32).trim().split(" ");

      xScale = Double.parseDouble(xAxis[1]) * 1000000;
      yScale = Double.parseDouble(yAxis[1]) * 1000000;
      invertX = xScale < 0;
      invertY = yScale > 0;
    }

    in.seek(352);
    String description = in.readString(32).trim();
    addGlobalMeta("Description", description);

    m.rgb = false;
    m.sizeZ = 1;
    m.sizeC = 1;
    m.sizeT = 1;
    m.imageCount = 1;
    m.dimensionOrder = "XYZCT";

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      Length sizeX = FormatTools.getPhysicalSizeX(xScale);
      Length sizeY = FormatTools.getPhysicalSizeY(yScale);

      if (sizeX != null) {
        store.setPixelsPhysicalSizeX(sizeX, 0);
      }
      if (sizeY != null) {
        store.setPixelsPhysicalSizeY(sizeY, 0);
      }
      store.setImageDescription(description, 0);
    }
  }

}
