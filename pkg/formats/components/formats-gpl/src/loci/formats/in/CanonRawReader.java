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
import loci.formats.ImageTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

/**
 * CanonRawReader is the file format reader for Canon RAW files.
 */
public class CanonRawReader extends FormatReader {

  // -- Constants --

  private static final int FILE_LENGTH = 18653760;
  private static final int[] COLOR_MAP = {1, 0, 2, 1};

  // -- Fields --

  private short[] pix;
  private byte[] plane;

  // -- Constructor --

  /** Constructs a new Canon RAW reader. */
  public CanonRawReader() {
    super("Canon RAW", new String[] {"cr2", "crw", "jpg", "thm", "wav"});
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
    suffixNecessary = false;
    suffixSufficient = false;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    return stream.length() == FILE_LENGTH;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    if (plane == null) {
      plane = new byte[FormatTools.getPlaneSize(this)];
      ImageTools.interpolate(
        pix, plane, COLOR_MAP, getSizeX(), getSizeY(), isLittleEndian());
    }

    try (RandomAccessInputStream s = new RandomAccessInputStream(plane)) {
      readPlane(s, x, y, w, h, buf);
    }

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      plane = null;
      pix = null;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);

    byte[] pixBuffer = new byte[FILE_LENGTH];
    in.read(pixBuffer);

    // reverse bytes in pairs

    for (int i=0; i<pixBuffer.length; i+=2) {
      byte v = pixBuffer[i];
      pixBuffer[i] = pixBuffer[i + 1];
      pixBuffer[i + 1] = v;
    }

    CoreMetadata m = core.get(0);
    m.sizeX = 4080;
    m.sizeY = 3048;
    m.sizeC = 3;
    m.sizeZ = 1;
    m.sizeT = 1;
    m.imageCount = getSizeZ() * getSizeT();
    m.indexed = false;
    m.littleEndian = true;
    m.dimensionOrder = "XYCZT";
    m.pixelType = FormatTools.UINT16;
    m.bitsPerPixel = 12;
    m.rgb = true;
    m.interleaved = true;

    pix = new short[getSizeX() * getSizeY() * 3];

    int nextByte = 0;
    boolean even = true;

    int plane = getSizeX() * getSizeY();

    for (int row=0; row<getSizeY(); row++) {
      int rowOffset = row * getSizeX();
      for (int col=0; col<getSizeX(); col++) {
        int v = 0;
        if (even) {
          v = (pixBuffer[nextByte++] & 0xff) << 4 |
            ((pixBuffer[nextByte] & 0xf0) >> 4);
        }
        else {
          v = ((pixBuffer[nextByte++] & 0xf) << 8) |
            (pixBuffer[nextByte++] & 0xff);
        }
        short val = (short) (v & 0xffff);
        even = !even;

        int mapIndex = (row % 2) * 2 + (col % 2);

        switch (COLOR_MAP[mapIndex]) {
          case 0:
            pix[rowOffset + col] = val;
            break;
          case 1:
            pix[plane + rowOffset + col] = val;
            break;
          case 2:
            pix[2 * plane + rowOffset + col] = val;
            break;
        }
      }
    }

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
