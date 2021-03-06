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

import java.io.StreamTokenizer;
import java.io.IOException;
import java.util.StringTokenizer;

import loci.common.ByteArrayHandle;
import loci.common.RandomAccessInputStream;
import loci.common.RandomAccessOutputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

/**
 * PGMReader is the file format reader for Portable Gray Map (PGM) images.
 *
 * Much of this code was adapted from ImageJ (http://rsb.info.nih.gov/ij).
 */
public class PGMReader extends FormatReader {

  // -- Constants --

  public static final char PGM_MAGIC_CHAR = 'P';

  // -- Fields --

  private boolean rawBits;

  /** Offset to pixel data. */
  private long offset;

  // -- Constructor --

  /** Constructs a new PGMReader. */
  public PGMReader() {
    super("Portable Any Map",
      new String[] {"pbm", "pgm", "ppm"});

    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
    suffixNecessary = false;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 2;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    return stream.read() == PGM_MAGIC_CHAR &&
      Character.isDigit((char) stream.read());
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    in.seek(offset);
    if (rawBits) {
      readPlane(in, x, y, w, h, buf);
    }
    else {
      ByteArrayHandle handle = new ByteArrayHandle();
      RandomAccessOutputStream out = new RandomAccessOutputStream(handle);
      out.order(isLittleEndian());

      while (in.getFilePointer() < in.length()) {
        String line = in.readLine().trim();
        line = line.replaceAll("[^0-9]", " ");
        StringTokenizer t = new StringTokenizer(line, " ");
        while (t.hasMoreTokens()) {
          int q = Integer.parseInt(t.nextToken().trim());
          if (getPixelType() == FormatTools.UINT16) {
            out.writeShort(q);
          }
          else out.writeByte(q);
        }
      }

      out.close();
      try (RandomAccessInputStream s = new RandomAccessInputStream(handle)) {
        s.seek(0);
        readPlane(s, x, y, w, h, buf);
      }
    }

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      rawBits = false;
      offset = 0;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);
    CoreMetadata m = core.get(0);

    //              Plain     Raw
    // B&W            P1      P4
    // Grayscale      P2      P5
    // RGB            P3      P6
    final String magic = in.readString(2);
    rawBits = magic.equals("P4") || magic.equals("P5") || magic.equals("P6");
    m.sizeC = (magic.equals("P3") || magic.equals("P6")) ? 3 : 1;
    final boolean isBlackAndWhite = magic.equals("P1") || magic.equals("P4");

    StreamTokenizer st = new StreamTokenizer(in);
    st.commentChar('#');

    int token;

    token = st.nextToken();
    m.sizeX = (int) st.nval;
    token = st.nextToken();
    m.sizeY = (int) st.nval;
    if (!isBlackAndWhite) {
      token = st.nextToken();
      final int max = (int) st.nval;
      if (max > 255)
        m.pixelType = FormatTools.UINT16;
      else
        m.pixelType = FormatTools.UINT8;
    }

    // After reading the 2 or 3 header entries, it is possible that there are
    // multiple comments before the actual pixel data.
    Byte c;
    do {
      c = in.readByte();
      if (c == 35) {
        // Found a # for comment
        do {
          c = in.readByte();
        } while (c != 13 && c != 10); // comment only ends after CR or LF
      }
    } while (c == 32 || c == 13 || c == 10 || c == 9 || c == 11 || c == 12);
    //            space      CR         LF         TAB       VT         FF
    offset = in.getFilePointer() -1;

    addGlobalMeta("Black and white", isBlackAndWhite);

    m.rgb = getSizeC() == 3;
    m.dimensionOrder = "XYCZT";
    m.littleEndian = true;
    m.interleaved = true;
    m.sizeZ = 1;
    m.sizeT = 1;
    m.indexed = false;
    m.falseColor = false;

    // This may not be true. Multiple images were added to the netpbm
    // formats in July 2000.
    m.imageCount = 1;

    // 1. the comments are being ignored;
    // 2. we may be missing some images (allowed in raw)
    m.metadataComplete = false;

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
