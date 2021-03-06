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
import loci.common.DateTools;
import loci.common.Location;
import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;
import ode.xml.model.primitives.Timestamp;
import ode.units.quantity.Length;

/**
 * FujiReader is the file format reader for Fuji LAS 3000 datasets.
 */
public class FujiReader extends FormatReader {

  // -- Constants --

  private static final String DATE_FORMAT = "ddd MMM dd HH:mm:ss yyyy";

  // -- Fields --

  private String infFile;
  private String pixelsFile;

  // -- Constructor --

  /** Constructs a new Fuji reader. */
  public FujiReader() {
    super("Fuji LAS 3000", new String[] {"img", "inf"});
    domains = new String[] {FormatTools.GEL_DOMAIN};
    hasCompanionFiles = true;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(String, boolean) */
  @Override
  public boolean isThisType(String name, boolean open) {
    if (!super.isThisType(name, open)) return false;
    if (!open) return false;

    String baseName = name.substring(0, name.lastIndexOf("."));

    if (checkSuffix(name, "inf")) {
      return new Location(baseName + ".img").exists();
    }
    else if (checkSuffix(name, "img")) {
      return new Location(baseName + ".inf").exists();
    }
    return false;
  }

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    return false;
  }

  /* @see loci.formats.IFormatReader#isSingleFile(String) */
  @Override
  public boolean isSingleFile(String id) throws FormatException, IOException {
    return false;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    try (RandomAccessInputStream s = new RandomAccessInputStream(pixelsFile)) {
      readPlane(s, x, y, w, h, buf);
    }
    return buf;
  }

  /* @see loci.formats.IFormatReader#getSeriesUsedFiles(boolean) */
  @Override
  public String[] getSeriesUsedFiles(boolean noPixels) {
    FormatTools.assertId(currentId, true, 1);
    if (noPixels) {
      return new String[] {infFile};
    }
    return new String[] {infFile, pixelsFile};
  }

  /* @see loci.formats.IFormatReader#fileGroupOption(String) */
  @Override
  public int fileGroupOption(String id) throws FormatException, IOException {
    return FormatTools.MUST_GROUP;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      infFile = null;
      pixelsFile = null;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);

    if (checkSuffix(id, "inf")) {
      infFile = new Location(id).getAbsolutePath();
      pixelsFile = infFile.substring(0, infFile.lastIndexOf(".")) + ".img";
    }
    else {
      pixelsFile = new Location(id).getAbsolutePath();
      infFile = pixelsFile.substring(0, pixelsFile.lastIndexOf(".")) + ".inf";
    }

    String[] lines = DataTools.readFile(infFile).split("\r{0,1}\n");

    int bits = Integer.parseInt(lines[5]);

    CoreMetadata m = core.get(0);
    m.pixelType = FormatTools.pixelTypeFromBytes(bits / 8, false, false);

    m.sizeX = Integer.parseInt(lines[6]);
    m.sizeY = Integer.parseInt(lines[7]);

    m.sizeC = 1;
    m.sizeT = 1;
    m.sizeZ = 1;
    m.imageCount = getSizeZ() * getSizeC() * getSizeT();
    m.dimensionOrder = "XYCZT";

    for (String line : lines) {
      addGlobalMetaList("Line", line);
    }

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);

    String imageName = lines[1];
    String timestamp = lines[10];
    timestamp = DateTools.formatDate(timestamp, DATE_FORMAT);

    store.setImageName(imageName, 0);
    if (timestamp != null) {
      store.setImageAcquisitionDate(new Timestamp(timestamp), 0);
    }

    double physicalWidth = Double.parseDouble(lines[3]);
    double physicalHeight = Double.parseDouble(lines[4]);

    Length sizeX = FormatTools.getPhysicalSizeX(physicalWidth);
    Length sizeY = FormatTools.getPhysicalSizeY(physicalHeight);

    if (sizeX != null) {
      store.setPixelsPhysicalSizeX(sizeX, 0);
    }
    if (sizeY != null) {
      store.setPixelsPhysicalSizeY(sizeY, 0);
    }

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      String instrument = lines[13];

      String instrumentID = MetadataTools.createLSID("Instrument", 0);
      store.setInstrumentID(instrumentID, 0);
      store.setMicroscopeModel(instrument, 0);
    }
  }

}
