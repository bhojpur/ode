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

/**
 * PovrayReader is the file format reader for POV-Ray .df3 files.
 *
 * @see <a href=http://www.povray.org/documentation/view/3.6.1/374/>http://www.povray.org/documentation/view/3.6.1/374/<a>
 */
public class PovrayReader extends FormatReader {

  // -- Constants --

  private static final int HEADER_SIZE = 6;

  // -- Constructor --

  /** Constructs a new POV-Ray reader. */
  public PovrayReader() {
    super("POV-Ray", "df3");
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
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

    in.seek(HEADER_SIZE + FormatTools.getPlaneSize(this) * no);
    readPlane(in, x, y, w, h, buf);
    return buf;
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);

    in = new RandomAccessInputStream(id);

    CoreMetadata m = core.get(0);

    m.littleEndian = false;

    in.order(isLittleEndian());

    m.sizeX = in.readShort();
    m.sizeY = in.readShort();
    m.sizeZ = in.readShort();

    long fileLength = in.length() - HEADER_SIZE;
    int nBytes = (int) (fileLength / (getSizeX() * getSizeY() * getSizeZ()));

    m.pixelType = FormatTools.pixelTypeFromBytes(nBytes, false, false);
    m.sizeC = 1;
    m.sizeT = 1;
    m.rgb = false;
    m.dimensionOrder = "XYZCT";
    m.imageCount = getSizeZ() * getSizeC() * getSizeT();

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
