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

import ode.xml.model.primitives.Timestamp;

import loci.common.DateTools;
import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

/**
 * OpenlabRawReader is the file format reader for Openlab RAW files.
 * Specifications available at
 * http://www.improvision.com/support/tech_notes/detail.php?id=344
 */
public class OpenlabRawReader extends FormatReader {

  // -- Constants --

  public static final String OPENLAB_RAW_MAGIC_STRING = "OLRW";

  private static final int HEADER_SIZE = 288;

  // -- Fields --

  /** Offset to each image's pixel data. */
  protected int[] offsets;

  /** Number of bytes per pixel. */
  private int bytesPerPixel;

  // -- Constructor --

  /** Constructs a new RAW reader. */
  public OpenlabRawReader() {
    super("Openlab RAW", "raw");
    suffixSufficient = false;
    domains = new String[] {FormatTools.UNKNOWN_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = OPENLAB_RAW_MAGIC_STRING.length();
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    return stream.readString(blockLen).startsWith(OPENLAB_RAW_MAGIC_STRING);
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    in.seek(offsets[no / getSizeC()] + HEADER_SIZE);
    readPlane(in, x, y, w, h, buf);

    if (FormatTools.getBytesPerPixel(getPixelType()) == 1) {
      // need to invert the pixels
      for (int i=0; i<buf.length; i++) {
        buf[i] = (byte) (255 - buf[i]);
      }
    }
    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      offsets = null;
      bytesPerPixel = 0;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);

    // read the 12 byte file header

    LOGGER.info("Verifying Openlab RAW format");

    if (!in.readString(4).equals("OLRW")) {
      throw new FormatException("Openlab RAW magic string not found.");
    }

    LOGGER.info("Populating metadata");

    CoreMetadata m = core.get(0);

    int version = in.readInt();

    m.imageCount = in.readInt();
    offsets = new int[getImageCount()];
    offsets[0] = 12;

    in.skipBytes(8);
    m.sizeX = in.readInt();
    m.sizeY = in.readInt();
    in.skipBytes(1);
    m.sizeC = in.read();
    bytesPerPixel = in.read();
    in.skipBytes(1);

    long stampMs = in.readLong();
    String stamp = null;
    if (stampMs > 0) {
      stampMs /= 1000000;
      stampMs -= (67 * 365.25 * 24 * 60 * 60);
      stamp = DateTools.convertDate(stampMs, DateTools.UNIX);
    }

    in.skipBytes(4);
    int len = in.read() & 0xff;
    String imageName = in.readString(len - 1).trim();

    if (getSizeC() <= 1) m.sizeC = 1;
    else m.sizeC = 3;

    int plane = getSizeX() * getSizeY() * bytesPerPixel;
    for (int i=1; i<getImageCount(); i++) {
      offsets[i] = offsets[i - 1] + HEADER_SIZE + plane;
    }

    m.sizeZ = getImageCount();
    m.sizeT = 1;
    m.rgb = getSizeC() > 1;
    m.dimensionOrder = isRGB() ? "XYCZT" : "XYZTC";
    m.interleaved = false;
    m.littleEndian = false;
    m.metadataComplete = true;
    m.indexed = false;
    m.falseColor = false;

    switch (bytesPerPixel) {
      case 1:
      case 3:
        m.pixelType = FormatTools.UINT8;
        break;
      case 2:
        m.pixelType = FormatTools.UINT16;
        break;
      default:
        m.pixelType = FormatTools.FLOAT;
    }

    addGlobalMeta("Width", getSizeX());
    addGlobalMeta("Height", getSizeY());
    addGlobalMeta("Bytes per pixel", bytesPerPixel);
    addGlobalMeta("Image name", imageName);
    addGlobalMeta("Timestamp", stamp);
    addGlobalMeta("Version", version);

    // The metadata store we're working with.
    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
    if (stamp != null) {
      store.setImageAcquisitionDate(new Timestamp(stamp), 0);
    }
    store.setImageName(imageName, 0);
  }

}
