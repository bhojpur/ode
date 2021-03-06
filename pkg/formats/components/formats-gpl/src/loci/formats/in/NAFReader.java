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
import loci.formats.UnsupportedCompressionException;
import loci.formats.meta.MetadataStore;

/**
 * NAFReader is the file format reader for Hamamatsu Aquacosmos NAF files.
 */
public class NAFReader extends FormatReader {

  // -- Constants --

  private static final int LUT_SIZE = 263168;

  // -- Fields --

  private long[] offsets;
  private boolean compressed;

  // -- Constructor --

  /** Constructs a new NAF reader. */
  public NAFReader() {
    super("Hamamatsu Aquacosmos", "naf");
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

    if (compressed) {
      throw new UnsupportedCompressionException(
        "Sorry, compressed data is not supported.");
    }

    in.seek(offsets[getSeries()] + no * FormatTools.getPlaneSize(this));
    readPlane(in, x, y, w, h, buf);
    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      offsets = null;
      compressed = false;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);

    String endian = in.readString(2);
    boolean little = endian.equals("II");
    in.order(little);

    in.seek(98);
    int seriesCount = in.readInt();

    in.seek(192);
    while (in.read() == 0);
    String description = in.readCString();
    addGlobalMeta("Description", description);

    while (in.readInt() == 0);

    long fp = in.getFilePointer();
    if ((fp % 2) == 0) fp -= 4;
    else fp--;

    offsets = new long[seriesCount];
    core.clear();
    for (int i=0; i<seriesCount; i++) {
      in.seek(fp + i*256);
      CoreMetadata ms = new CoreMetadata();
      core.add(ms);
      ms.littleEndian = little;
      ms.sizeX = in.readInt();
      ms.sizeY = in.readInt();
      int numBits = in.readInt();
      ms.sizeC = in.readInt();
      ms.sizeZ = in.readInt();
      ms.sizeT = in.readInt();

      ms.imageCount = ms.sizeZ * ms.sizeC * ms.sizeT;
      int nBytes = numBits / 8;
      ms.pixelType =
        FormatTools.pixelTypeFromBytes(nBytes, false, nBytes == 8);

      ms.dimensionOrder = "XYCZT";
      ms.rgb = false;

      in.skipBytes(4);

      long pointer = in.getFilePointer();
      String name = in.readCString();

      if (i == 0) {
        in.skipBytes(92 - in.getFilePointer() + pointer);
        while (true) {
          int check = in.readInt();
          if (check > in.getFilePointer()) {
            offsets[i] = (long) check + LUT_SIZE;
            break;
          }
          in.skipBytes(92);
        }
      }
      else {
          CoreMetadata mp = core.get(i - 1);
          offsets[i] = offsets[i - 1] + mp.sizeX * mp.sizeY *
            mp.imageCount *
          FormatTools.getBytesPerPixel(mp.pixelType);
      }
      offsets[i] += 352;
      in.seek(offsets[i]);
      while (in.getFilePointer() + 116 < in.length() && in.read() == 3 &&
        in.read() == 37)
      {
        in.skipBytes(114);
        offsets[i] = in.getFilePointer();
      }
      in.seek(in.getFilePointer() - 1);
      byte[] buf = new byte[3 * 1024 * 1024];
      int n = in.read(buf, 0, 1);
      boolean found = false;
      while (!found && in.getFilePointer() < in.length()) {
        n += in.read(buf, 1, buf.length - 1);
        for (int q=0; q<buf.length - 1; q++) {
          if ((buf[q] & 0xff) == 192 && (buf[q + 1] & 0xff) == 46) {
            offsets[i] = in.getFilePointer() - n + q;
            found = true;
            break;
          }
        }
        buf[0] = buf[buf.length - 1];
        n = 1;
      }
      if (found) offsets[i] += 16063;
      if (i == offsets.length - 1 && !compressed && i > 0) {
        offsets[i] = (int) (in.length() -
          (ms.sizeX * ms.sizeY * ms.imageCount * (numBits / 8)));
      }
    }

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
