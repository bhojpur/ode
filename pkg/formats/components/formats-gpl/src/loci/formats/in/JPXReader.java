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
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.codec.JPEG2000BoxType;
import loci.formats.codec.JPEG2000Codec;
import loci.formats.codec.JPEG2000CodecOptions;
import loci.formats.meta.MetadataStore;

/**
 * JPXReader is the file format reader for JPX (3D JPEG-2000) images.
 */
public class JPXReader extends FormatReader {

  // -- Constants --

  /** Logger for this class. */
  private static final Logger LOGGER = LoggerFactory.getLogger(JPXReader.class);

  // -- Fields --

  /** The number of JPEG 2000 resolution levels the file has. */
  private Integer resolutionLevels;

  /** The color lookup table associated with this file. */
  private int[][] lut;

  private ArrayList<Long> pixelOffsets = new ArrayList<Long>();

  private int lastSeries = -1;
  private int lastPlane = -1;
  private byte[] lastSeriesPlane;

  // -- Constructor --

  /** Constructs a new JPX Reader. */
  public JPXReader() {
    super("JPX", "jpx");
    suffixSufficient = false;
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 8;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    boolean validStart = (stream.readShort() & 0xffff) == 0xff4f;
    if (!validStart) {
      stream.skipBytes(2);
      validStart = stream.readInt() == JPEG2000BoxType.SIGNATURE.getCode();
    }
    stream.seek(stream.length() - 2);
    boolean validEnd = (stream.readShort() & 0xffff) == 0xffd9;
    return validStart && validEnd;
  }

  /* @see loci.formats.IFormatReader#get8BitLookupTable() */
  @Override
  public byte[][] get8BitLookupTable() {
    FormatTools.assertId(currentId, true, 1);
    if (lut == null || FormatTools.getBytesPerPixel(getPixelType()) != 1) {
      return null;
    }

    byte[][] byteLut = new byte[lut.length][lut[0].length];
    for (int i=0; i<lut.length; i++) {
      for (int j=0; j<lut[i].length; j++) {
        byteLut[i][j] = (byte) (lut[i][j] & 0xff);
      }
    }
    return byteLut;
  }

  /* @see loci.formats.IFormatReader#get16BitLookupTable() */
  @Override
  public short[][] get16BitLookupTable() {
    FormatTools.assertId(currentId, true, 1);
    if (lut == null || FormatTools.getBytesPerPixel(getPixelType()) != 2) {
      return null;
    }

    short[][] shortLut = new short[lut.length][lut[0].length];
    for (int i=0; i<lut.length; i++) {
      for (int j=0; j<lut[i].length; j++) {
        shortLut[i][j] = (short) (lut[i][j] & 0xffff);
      }
    }
    return shortLut;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      resolutionLevels = null;
      lut = null;
      pixelOffsets.clear();
      lastSeries = -1;
      lastPlane = -1;
      lastSeriesPlane = null;
    }
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    if (lastSeries == getSeries() && lastPlane == no && lastSeriesPlane != null)
    {
      try (RandomAccessInputStream s = new RandomAccessInputStream(lastSeriesPlane)) {
        readPlane(s, x, y, w, h, buf);
      }
      return buf;
    }

    JPEG2000CodecOptions options = JPEG2000CodecOptions.getDefaultOptions();
    options.interleaved = isInterleaved();
    options.littleEndian = isLittleEndian();
    if (resolutionLevels != null) {
      options.resolution = Math.abs(getSeries() - resolutionLevels);
    }
    else if (getSeriesCount() > 1) {
      options.resolution = getSeries();
    }

    in.seek(pixelOffsets.get(no));
    lastSeriesPlane = new JPEG2000Codec().decompress(in, options);
    try (RandomAccessInputStream s = new RandomAccessInputStream(lastSeriesPlane)) {
      readPlane(s, x, y, w, h, buf);
    }
    lastSeries = getSeries();
    lastPlane = no;
    return buf;
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);

    in = new RandomAccessInputStream(id);
    CoreMetadata ms0 = core.get(0);

    JPEG2000MetadataParser metadataParser = new JPEG2000MetadataParser(in);
    if (metadataParser.isRawCodestream()) {
      LOGGER.info("Codestream is raw, using codestream dimensions.");
      ms0.sizeX = metadataParser.getCodestreamSizeX();
      ms0.sizeY = metadataParser.getCodestreamSizeY();
      ms0.sizeC = metadataParser.getCodestreamSizeC();
      ms0.pixelType = metadataParser.getCodestreamPixelType();
    }
    else {
      LOGGER.info("Codestream is JP2 boxed, using header dimensions.");
      ms0.sizeX = metadataParser.getHeaderSizeX();
      ms0.sizeY = metadataParser.getHeaderSizeY();
      ms0.sizeC = metadataParser.getHeaderSizeC();
      ms0.pixelType = metadataParser.getHeaderPixelType();
    }
    lut = metadataParser.getLookupTable();

    findPixelOffsets();

    ms0.sizeZ = 1;
    ms0.sizeT = pixelOffsets.size();
    ms0.imageCount = getSizeZ() * getSizeT();
    ms0.dimensionOrder = "XYCZT";
    ms0.rgb = getSizeC() > 1;
    ms0.interleaved = true;
    ms0.littleEndian = false;
    ms0.indexed = !isRGB() && lut != null;

    // New core metadata now that we know how many sub-resolutions we have.
    if (resolutionLevels != null) {
      int seriesCount = resolutionLevels + 1;

      for (int i = 1; i < seriesCount; i++) {
        CoreMetadata ms = new CoreMetadata(this, 0);
        core.add(ms);
        ms.sizeX = core.get(i - 1).sizeX / 2;
        ms.sizeY = core.get(i - 1).sizeY / 2;
        ms.thumbnail = true;
      }
    }

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this, true);
  }

  // -- Helper methods --

  private void findPixelOffsets() throws IOException {
    in.seek(0);

    byte[] buf = new byte[8192];
    int overlap = 3;
    in.read(buf, 0, overlap);

    while (in.getFilePointer() < in.length()) {
      int n = in.read(buf, overlap, buf.length - overlap);

      for (int i=0; i<n - overlap; i++) {
        if (buf[i] == (byte) 0xff) {
          if (buf[i + 1] == (byte) 0x4f && buf[i + 2] == (byte) 0xff &&
            buf[i + 3] == (byte) 0x51)
          {
            pixelOffsets.add(in.getFilePointer() - n + i - overlap);
          }
        }
      }

      System.arraycopy(buf, n - overlap, buf, 0, overlap);
    }
  }

}
