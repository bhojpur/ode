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
 * FitsReader is the file format reader for
 * Flexible Image Transport System (FITS) images.
 *
 * Much of this code was adapted from ImageJ (http://rsb.info.nih.gov/ij).
 */
public class FitsReader extends FormatReader {

  // -- Constants --

  private static final int LINE_LENGTH = 80;

  // -- Fields --

  private long pixelOffset;

  // -- Constructor --

  /** Constructs a new FitsReader. */
  public FitsReader() {
    super("Flexible Image Transport System", new String[] {"fits", "fts"});
    domains =
      new String[] {FormatTools.ASTRONOMY_DOMAIN, FormatTools.UNKNOWN_DOMAIN};
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

    in.seek(pixelOffset + no * FormatTools.getPlaneSize(this));
    readPlane(in, x, y, w, h, buf);
    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) pixelOffset = 0;
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);
    CoreMetadata m = core.get(0);

    String line = in.readString(LINE_LENGTH);
    if (!line.startsWith("SIMPLE")) {
      throw new FormatException("Unsupported FITS file.");
    }

    String key = "", value = "";
    while (true) {
      line = in.readString(LINE_LENGTH);

      // parse key/value pair
      int ndx = line.indexOf('=');
      int comment = line.indexOf("/", ndx);
      if (comment < 0) comment = line.length();

      if (ndx >= 0) {
        key = line.substring(0, ndx).trim();
        value = line.substring(ndx + 1, comment).trim();
      }
      else key = line.trim();

      // if the file has an extended header, "END" will appear twice
      // the first time marks the end of the extended header
      // the second time marks the end of the standard header
      // image dimensions are only populated by the standard header
      if (key.equals("END") && getSizeX() > 0) break;

      if (key.equals("BITPIX")) {
        int bits = Integer.parseInt(value);
        boolean fp = bits < 0;
        boolean signed = bits != 8;
        bits = Math.abs(bits) / 8;
        m.pixelType = FormatTools.pixelTypeFromBytes(bits, signed, fp);
      }
      else if (key.equals("NAXIS1")) m.sizeX = Integer.parseInt(value);
      else if (key.equals("NAXIS2")) m.sizeY = Integer.parseInt(value);
      else if (key.equals("NAXIS3")) m.sizeZ = Integer.parseInt(value);

      addGlobalMeta(key, value);
    }
    while (in.read() == 0x20);
    pixelOffset = in.getFilePointer() - 1;

    m.sizeC = 1;
    m.sizeT = 1;
    if (getSizeZ() == 0) m.sizeZ = 1;

    // correct for truncated files
    int planeSize =
      getSizeX() * getSizeY() * FormatTools.getBytesPerPixel(getPixelType());
    if (DataTools.safeMultiply64(planeSize, getSizeZ()) >
      (in.length() - pixelOffset))
    {
      m.sizeZ = (int) ((in.length() - pixelOffset) / planeSize);
    }

    m.imageCount = m.sizeZ;
    m.rgb = false;
    m.littleEndian = false;
    m.interleaved = false;
    m.dimensionOrder = "XYZCT";
    m.indexed = false;
    m.falseColor = false;
    m.metadataComplete = true;

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
