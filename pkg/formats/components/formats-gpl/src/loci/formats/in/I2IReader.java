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

import loci.common.DataTools;
import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

/**
 * I2IReader is the file format reader for I2I files.
 */
public class I2IReader extends FormatReader {

  // -- Constants --

  private static final int HEADER_SIZE = 1024;

  // -- Constructor --

  /** Constructs a new I2I reader. */
  public I2IReader() {
    super("I2I", new String[] {"i2i"});
    domains = new String[] {FormatTools.LM_DOMAIN};
  }

  // -- IFormatReader API methods --

  /** @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    if (!FormatTools.validStream(stream, HEADER_SIZE, false)) {
      return false;
    }
    char pixelType = (char) stream.readByte();
    if (pixelType != 'I' && pixelType != 'R' && pixelType != 'C') {
      return false;
    }
    char check = (char) stream.readByte();
    if (check != ' ') {
      return false;
    }
    long pixelCount = getDimension(stream);
    pixelCount *= getDimension(stream);
    pixelCount *= getDimension(stream);
    return pixelCount > 0;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    long planeSize = FormatTools.getPlaneSize(this);
    long offset = HEADER_SIZE + no * planeSize;

    if (offset + planeSize <= in.length() && offset >= 0) {
      in.seek(offset);
      readPlane(in, x, y, w, h, buf);
    }

    return buf;
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  public void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);

    CoreMetadata m = core.get(0);

    char pixelType = (char) in.readByte();

    switch (pixelType) {
      case 'I':
        m.pixelType = FormatTools.INT16;
        break;
      case 'R':
        m.pixelType = FormatTools.FLOAT;
        break;
      case 'C':
        throw new FormatException("Complex pixel data not yet supported");
      default:
        throw new FormatException("Invalid pixel type: " + pixelType);
    }

    if ((char) in.readByte() != ' ') {
      throw new FormatException("Expected space after pixel type character");
    }

    m.sizeX = getDimension(in);
    m.sizeY = getDimension(in);
    m.sizeZ = getDimension(in);

    m.littleEndian = (char) in.readByte() != 'B';

    in.order(isLittleEndian());

    short minPixelValue = in.readShort();
    short maxPixelValue = in.readShort();
    short xCoordinate = in.readShort();
    short yCoordinate = in.readShort();
    int n = in.readShort();

    in.skipBytes(33); // reserved for future use

    for (int i=0; i<15; i++) {
      String history = in.readString(64);
      addGlobalMetaList("Image history", history.trim());
    }

    addGlobalMeta("Minimum intensity value", minPixelValue);
    addGlobalMeta("Maximum intensity value", maxPixelValue);
    addGlobalMeta("Image position X", xCoordinate);
    addGlobalMeta("Image position Y", yCoordinate);

    // the stored Z value is always the total number of planes
    // when an additional dimension is defined, the Z value
    // needs to be adjusted to reflect the true Z count
    if (n > 0) {
      m.sizeZ /= n;
    }

    // the user defines what the N dimension means
    // in practice, it could be timepoints, channels, etc. but we have no
    // way of knowing based on the file metadata
    m.sizeT = n;

    m.imageCount = getSizeZ() * getSizeT();
    m.sizeC = 1;
    m.rgb = false;
    m.dimensionOrder = "XYZTC";

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

  private int getDimension(RandomAccessInputStream stream) throws IOException {
    String dim = stream.readString(6).trim();
    Integer dimension = DataTools.parseInteger(dim);
    return dimension == null ? 0 : dimension;
  }

}
