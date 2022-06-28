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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.meta.MetadataStore;
import loci.formats.tiff.IFD;
import loci.formats.tiff.PhotoInterp;
import loci.formats.tiff.TiffParser;

/**
 * PyramidTiffReader is the file format reader for pyramid TIFFs.
 */
public class PyramidTiffReader extends BaseTiffReader {

  // -- Constants --

  /** Logger for this class. */
  private static final Logger LOGGER =
    LoggerFactory.getLogger(PyramidTiffReader.class);

  // -- Fields --

  // -- Constructor --

  /** Constructs a new pyramid TIFF reader. */
  public PyramidTiffReader() {
    super("Pyramid TIFF", new String[] {"tif", "tiff"});
    domains = new String[] {FormatTools.EM_DOMAIN};
    suffixSufficient = false;
    suffixNecessary = false;
    equalStrips = true;
    noSubresolutions = true;
    canSeparateSeries = false;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    TiffParser parser = new TiffParser(stream);
    parser.setAssumeEqualStrips(equalStrips);
    IFD ifd = parser.getFirstIFD();
    if (ifd == null) return false;
    String software = ifd.getIFDTextValue(IFD.SOFTWARE);
    if (software == null) return false;
    return software.indexOf("Faas") >= 0;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);
    int index = getCoreIndex();
    tiffParser.setAssumeEqualStrips(equalStrips);
    tiffParser.getSamples(ifds.get(index), buf, x, y, w, h);
    return buf;
  }

  /* @see loci.formats.IFormatReader#getOptimalTileWidth() */
  @Override
  public int getOptimalTileWidth() {
    FormatTools.assertId(currentId, true, 1);
    try {
      return (int) ifds.get(getCoreIndex()).getTileWidth();
    }
    catch (FormatException e) {
      LOGGER.debug("", e);
    }
    return super.getOptimalTileWidth();
  }

  /* @see loci.formats.IFormatReader#getOptimalTileHeight() */
  @Override
  public int getOptimalTileHeight() {
    FormatTools.assertId(currentId, true, 1);
    try {
      return (int) ifds.get(getCoreIndex()).getTileLength();
    }
    catch (FormatException e) {
      LOGGER.debug("", e);
    }
    return super.getOptimalTileHeight();
  }

  // -- Internal BaseTiffReader API methods --

  /* @see loci.formats.in.BaseTiffReader#initStandardMetadata() */
  @Override
  protected void initStandardMetadata() throws FormatException, IOException {
    int seriesCount = ifds.size();

    // repopulate core metadata
    core.clear();
    core.add();
    for (int s=0; s<seriesCount; s++) {
      CoreMetadata ms = new CoreMetadata();
      core.add(0, ms);

      if (s == 0) {
        ms.resolutionCount = seriesCount;
      }

      IFD ifd = ifds.get(s);

      PhotoInterp p = ifd.getPhotometricInterpretation();
      int samples = ifd.getSamplesPerPixel();
      ms.rgb = samples > 1 || p == PhotoInterp.RGB;

      long numTileRows = ifd.getTilesPerColumn() - 1;
      long numTileCols = ifd.getTilesPerRow() - 1;

      ms.sizeX = (int) ifd.getImageWidth();
      ms.sizeY = (int) ifd.getImageLength();
      ms.sizeZ = 1;
      ms.sizeT = 1;
      ms.sizeC = ms.rgb ? samples : 1;
      ms.littleEndian = ifd.isLittleEndian();
      ms.indexed = p == PhotoInterp.RGB_PALETTE &&
        (get8BitLookupTable() != null || get16BitLookupTable() != null);
      ms.imageCount = 1;
      ms.pixelType = ifd.getPixelType();
      ms.metadataComplete = true;
      ms.interleaved = false;
      ms.falseColor = false;
      ms.dimensionOrder = "XYCZT";
      ms.thumbnail = s > 0;
    }
  }

  /* @see loci.formats.BaseTiffReader#initMetadataStore() */
  @Override
  protected void initMetadataStore() throws FormatException {
    super.initMetadataStore();

    MetadataStore store = makeFilterMetadata();

    for (int i=0; i<getSeriesCount(); i++) {
      store.setImageName("Series " + (i + 1), i);
    }
  }

}
