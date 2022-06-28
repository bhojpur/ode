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
import loci.formats.tiff.IFD;
import loci.formats.tiff.IFDList;
import loci.formats.tiff.TiffParser;

/**
 * MRWReader is the file format reader for Minolta MRW files.
 */
public class MRWReader extends FormatReader {

  // -- Constants --

  public static final String MRW_MAGIC_STRING = "MRM";

  private static final int[] COLOR_MAP_1 = {0, 1, 1, 2};
  private static final int[] COLOR_MAP_2 = {1, 2, 0, 1};

  // -- Fields --

  /** Offset to image data. */
  private int offset;

  private int sensorWidth, sensorHeight;
  private int bayerPattern;
  private int storageMethod;
  private int dataSize;

  private float[] wbg;

  private byte[] fullImage;

  // -- Constructor --

  /** Constructs a new MRW reader. */
  public MRWReader() {
    super("Minolta MRW", "mrw");
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 4;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    return stream.readString(4).endsWith(MRW_MAGIC_STRING);
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    int nBytes = sensorWidth * sensorHeight;
    if (dataSize == 12) nBytes *= 3;
    else if (dataSize == 16) nBytes *= 4;

    in.seek(offset);

    short[] s = new short[getSizeX() * getSizeY() * 3];

    for (int row=0; row<getSizeY(); row++) {
      boolean evenRow = (row % 2) == 0;
      for (int col=0; col<getSizeX(); col++) {
        boolean evenCol = (col % 2) == 0;
        short val = (short) (in.readBits(dataSize) & 0xffff);

        int redOffset = row * getSizeX() + col;
        int greenOffset = (getSizeY() + row) * getSizeX() + col;
        int blueOffset = (2 * getSizeY() + row) * getSizeX() + col;

        if (evenRow) {
          if (evenCol) {
            val = (short) (val * wbg[0]);
            if (bayerPattern == 1) s[redOffset] = val;
            else s[greenOffset] = val;
          }
          else {
            val = (short) (val * wbg[1]);
            if (bayerPattern == 1) s[greenOffset] = val;
            else s[blueOffset] = val;
          }
        }
        else {
          if (evenCol) {
            val = (short) (val * wbg[2]);
            if (bayerPattern == 1) s[greenOffset] = val;
            else s[redOffset] = val;
          }
          else {
            val = (short) (val * wbg[3]);
            if (bayerPattern == 1) s[blueOffset] = val;
            else s[greenOffset] = val;
          }
        }
      }
      in.skipBits(dataSize * (sensorWidth - getSizeX()));
    }

    int[] colorMap = bayerPattern == 1 ? COLOR_MAP_1 : COLOR_MAP_2;

    if (fullImage == null) {
      fullImage = new byte[FormatTools.getPlaneSize(this)];
      fullImage = ImageTools.interpolate(s, fullImage, colorMap,
        getSizeX(), getSizeY(), isLittleEndian());
    }
    try (RandomAccessInputStream stream = new RandomAccessInputStream(fullImage)) {
      readPlane(stream, x, y, w, h, buf);
    }
    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      offset = 0;
      sensorWidth = sensorHeight = 0;
      bayerPattern = 0;
      storageMethod = 0;
      dataSize = 0;
      wbg = null;
      fullImage = null;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);
    CoreMetadata m = core.get(0);

    in.skipBytes(4); // magic number
    offset = in.readInt() + 8;

    while (in.getFilePointer() < offset) {
      String blockName = in.readString(4);
      int len = in.readInt();
      long fp = in.getFilePointer();

      if (blockName.endsWith("PRD")) {
        in.skipBytes(8);
        sensorHeight = in.readShort();
        sensorWidth = in.readShort();
        m.sizeY = in.readShort();
        m.sizeX = in.readShort();
        dataSize = in.read();
        in.skipBytes(1);
        storageMethod = in.read();
        in.skipBytes(4);
        bayerPattern = in.read();
      }
      else if (blockName.endsWith("WBG")) {
        wbg = new float[4];
        byte[] wbScale = new byte[4];
        in.read(wbScale);
        for (int i=0; i<wbg.length; i++) {
          float coeff = in.readShort();
          wbg[i] = coeff / (64 << wbScale[i]);
        }
      }
      else if (blockName.endsWith("TTW") &&
        getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM)
      {
        byte[] b = new byte[len];
        in.read(b);
        try (RandomAccessInputStream ras = new RandomAccessInputStream(b)) {
          TiffParser tp = new TiffParser(ras);
          IFDList ifds = tp.getMainIFDs();

          for (IFD ifd : ifds) {
            Integer[] keys = (Integer[]) ifd.keySet().toArray(new Integer[0]);
            // CTR FIXME - getIFDTagName is for debugging only!
            for (int q=0; q<keys.length; q++) {
              addGlobalMeta(IFD.getIFDTagName(keys[q].intValue()),
                ifd.get(keys[q]));
            }
          }

          IFDList exifIFDs = tp.getExifIFDs();
          for (IFD exif : exifIFDs) {
            for (Integer key : exif.keySet()) {
              addGlobalMeta(IFD.getIFDTagName(key.intValue()), exif.get(key));
            }
          }
        }
      }

      in.seek(fp + len);
    }

    m.pixelType = FormatTools.UINT16;
    m.rgb = true;
    m.littleEndian = false;
    m.dimensionOrder = "XYCZT";
    m.imageCount = 1;
    m.sizeC = 3;
    m.sizeZ = 1;
    m.sizeT = 1;
    m.interleaved = true;
    m.bitsPerPixel = dataSize;

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
