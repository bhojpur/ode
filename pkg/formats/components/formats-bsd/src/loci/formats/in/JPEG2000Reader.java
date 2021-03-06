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
 * JPEG2000Reader is the file format reader for JPEG-2000 images.
 */
public class JPEG2000Reader extends FormatReader {

  // -- Constants --

  /** Logger for this class. */
  private static final Logger LOGGER =
    LoggerFactory.getLogger(JPEG2000Reader.class);

  // -- Fields --

  /** The number of JPEG 2000 resolution levels the file has. */
  private Integer resolutionLevels;

  /** The color lookup table associated with this file. */
  private int[][] lut;

  private long pixelsOffset;

  private int lastSeries = -1;
  private byte[] lastSeriesPlane;

  // -- Constructor --

  /** Constructs a new JPEG2000Reader. */
  public JPEG2000Reader() {
    super("JPEG-2000", new String[] {"jp2", "j2k", "jpf"});
    suffixSufficient = false;
    suffixNecessary = false;
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 40;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    boolean validStart = (stream.readShort() & 0xffff) == 0xff4f;
    if (!validStart) {
      stream.skipBytes(2);
      validStart = stream.readInt() == JPEG2000BoxType.SIGNATURE.getCode();

      if (validStart) {
        stream.skipBytes(12);
        validStart = !stream.readString(4).equals("jpx ");
      }
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
      pixelsOffset = 0;
      lastSeries = -1;
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

    if (lastSeries == getCoreIndex() && lastSeriesPlane != null) {
      try (RandomAccessInputStream s = new RandomAccessInputStream(lastSeriesPlane)) {
        readPlane(s, x, y, w, h, buf);
      }
      return buf;
    }

    JPEG2000CodecOptions options = JPEG2000CodecOptions.getDefaultOptions();
    options.interleaved = isInterleaved();
    options.littleEndian = isLittleEndian();
    if (resolutionLevels != null) {
      options.resolution = Math.abs(getCoreIndex() - resolutionLevels);
    }
    else if (core.size() > 1) {
      options.resolution = getCoreIndex();
    }

    in.seek(pixelsOffset);
    lastSeriesPlane = new JPEG2000Codec().decompress(in, options);
    try (RandomAccessInputStream s = new RandomAccessInputStream(lastSeriesPlane)) {
      readPlane(s, x, y, w, h, buf);
    }
    lastSeries = getCoreIndex();
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
    resolutionLevels = metadataParser.getResolutionLevels();

    pixelsOffset = metadataParser.getCodestreamOffset();

    ms0.sizeZ = 1;
    ms0.sizeT = 1;
    ms0.imageCount = 1;
    ms0.dimensionOrder = "XYCZT";
    ms0.rgb = getSizeC() > 1;
    ms0.interleaved = true;
    ms0.littleEndian = false;
    ms0.indexed = !isRGB() && lut != null;

    // New core metadata now that we know how many sub-resolutions we have.
    if (resolutionLevels != null) {
      int seriesCount = resolutionLevels + 1;
      core.get(0).resolutionCount = seriesCount;

      for (int i = 1; i < seriesCount; i++) {
        CoreMetadata ms = new CoreMetadata(this, 0);
        core.add(ms);
        ms.sizeX = Math.max(core.get(i - 1).sizeX / 2, 1);
        ms.sizeY = Math.max(core.get(i - 1).sizeY / 2, 1);
        ms.thumbnail = true;
      }
    }

    ArrayList<String> comments = metadataParser.getComments();
    LOGGER.debug("Found {} comments", comments.size());
    for (int i=0; i<comments.size(); i++) {
      String comment = comments.get(i);
      int equal = comment.indexOf('=');
      if (equal >= 0) {
        String key = comment.substring(0, equal);
        String value = comment.substring(equal + 1);

        addGlobalMeta(key, value);
      }
      else {
        addGlobalMetaList("Comment", comment);
      }
    }

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this, true);
  }

  // -- Helper methods --

}
