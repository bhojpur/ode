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
import loci.formats.FormatException;
import loci.formats.CoreMetadata;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.UnsupportedCompressionException;
import loci.formats.meta.MetadataStore;

/**
 * LIMReader is the file format reader for Laboratory Imaging/Nikon LIM files.
 */
public class LIMReader extends FormatReader {

  // -- Constants --

  private static final int PIXELS_OFFSET = 0x94b;

  // -- Fields --

  private boolean isCompressed;

  // -- Constructor --

  /** Constructs a new LIM reader. */
  public LIMReader() {
    super("Laboratory Imaging", "lim");
    domains = new String[] {FormatTools.LM_DOMAIN};
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

    in.seek(PIXELS_OFFSET);
    readPlane(in, x, y, w, h, buf);

    // swap red and blue channels
    if (isRGB()) {
      for (int i=0; i<buf.length/3; i++) {
        byte tmp = buf[i*3];
        buf[i*3] = buf[i*3 + 2];
        buf[i*3 + 2] = tmp;
      }
    }

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) isCompressed = false;
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

    m.sizeX = in.readShort() & 0x7fff;
    m.sizeY = in.readShort();
    int bits = in.readShort();

    while (bits % 8 != 0) bits++;
    if ((bits % 3) == 0) {
      m.sizeC = 3;
      bits /= 3;
    }
    m.pixelType = FormatTools.pixelTypeFromBytes(bits / 8, false, false);

    isCompressed = in.readShort() != 0;
    addGlobalMeta("Is compressed", isCompressed);
    if (isCompressed) {
      throw new UnsupportedCompressionException(
        "Compressed LIM files not supported.");
    }

    m.imageCount = 1;
    m.sizeZ = 1;
    m.sizeT = 1;
    if (getSizeC() == 0) m.sizeC = 1;
    m.rgb = getSizeC() > 1;
    m.dimensionOrder = "XYZCT";
    m.indexed = false;
    m.falseColor = false;
    m.interleaved = true;
    m.metadataComplete = true;

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
