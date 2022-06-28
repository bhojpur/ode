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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import loci.common.Constants;
import loci.common.DateTools;
import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;
import ode.xml.model.primitives.Timestamp;

import ode.units.quantity.Length;
import ode.units.quantity.Temperature;
import ode.units.quantity.Time;
import ode.units.UNITS;

/**
 * KodakReader is the file format reader for Kodak Molecular Imaging .bip files.
 */
public class KodakReader extends FormatReader {

  // -- Constants --

  private static final String MAGIC_STRING = "DTag";
  private static final String PIXELS_STRING = "BSfD";
  private static final String DIMENSIONS_STRING = "GBiH";
  private static final String FILEINFO_STRING = "DLFi";

  private static final String DATE_FORMAT = "HH:mm:ss 'on' MM/dd/yyyy";

  // -- Fields --

  private long pixelOffset;

  // -- Constructor --

  /** Constructs a new Kodak reader. */
  public KodakReader() {
    super("Kodak Molecular Imaging", "bip");
    domains = new String[] {FormatTools.GEL_DOMAIN};
    suffixSufficient = false;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 16;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    return (stream.readString(blockLen)).indexOf(MAGIC_STRING) >= 0;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    in.seek(pixelOffset);
    readPlane(in, x, y, w, h, buf);

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      pixelOffset = 0;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);

    CoreMetadata m = core.get(0);

    m.littleEndian = false;

    findString(DIMENSIONS_STRING);
    in.skipBytes(DIMENSIONS_STRING.length() + 20);
    m.sizeX = in.readInt();
    m.sizeY = in.readInt();

    findString(PIXELS_STRING);
    pixelOffset = in.getFilePointer() + PIXELS_STRING.length() + 20;

    m.sizeZ = 1;
    m.sizeC = 1;
    m.sizeT = 1;
    m.imageCount = 1;
    m.dimensionOrder = "XYCZT";
    m.pixelType = FormatTools.FLOAT;

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this, true);

    readExtraMetadata(store);
    readFileInfoMetadata(store);
  }

  // -- Helper methods --

  private void findString(String marker) throws IOException {
    byte[] buf = new byte[8192];
    int overlap = marker.length();

    in.read(buf, 0, overlap);

    while (in.getFilePointer() < in.length()) {
      int length = in.read(buf, overlap, buf.length - overlap);

      for (int i=0; i<length; i++) {
        if (marker.equals(
          new String(buf, i, marker.length(), Constants.ENCODING)))
        {
          in.seek(in.getFilePointer() - (length + overlap) + i);
          return;
        }
      }

      System.arraycopy(buf, length, buf, 0, overlap);
    }
  }

  private void readExtraMetadata(MetadataStore store) throws IOException {
    if (getMetadataOptions().getMetadataLevel() == MetadataLevel.MINIMUM) {
      return;
    }

    in.seek(0);

    findString("Image Capture Source");
    String metadata = in.readCString();

    if (metadata == null) {
      return;
    }

    String[] lines = metadata.split("\n");

    for (String line : lines) {
      int index = line.indexOf(':');
      if (index < 0 || line.startsWith("#") || line.startsWith("-")) {
        continue;
      }

      String key = line.substring(0, index).trim();
      String value = line.substring(index + 1).trim();

      addGlobalMeta(key, value);

      if (key.equals("Image Capture Source")) {
        String instrument = MetadataTools.createLSID("Instrument", 0);
        store.setInstrumentID(instrument, 0);
        store.setImageInstrumentRef(instrument, 0);
        store.setMicroscopeModel(value, 0);
      }
      else if (key.equals("Capture Time/Date")) {
        String date = DateTools.formatDate(value, DATE_FORMAT);
        if (date != null) {
          store.setImageAcquisitionDate(new Timestamp(date), 0);
        }
      }
      else if (key.equals("Exposure Time")) {
        Double exposureTime = new Double(value.substring(0, value.indexOf(' ')));
        if (exposureTime != null) {
          store.setPlaneExposureTime(new Time(exposureTime, UNITS.SECOND), 0, 0);
        }
      }
      else if (key.equals("Vertical Resolution")) {
        // resolution stored in pixels per inch
        if (value.indexOf(' ') > 0) {
          value = value.substring(0, value.indexOf(' '));
        }
        Double size = new Double(value);
        size = 1.0 / (size * (1.0 / 25400));

        Length sizeY = FormatTools.getPhysicalSizeY(size);
        if (sizeY != null) {
          store.setPixelsPhysicalSizeY(sizeY, 0);
        }
      }
      else if (key.equals("Horizontal Resolution")) {
        // resolution stored in pixels per inch
        if (value.indexOf(' ') > 0) {
          value = value.substring(0, value.indexOf(' '));
        }
        Double size = new Double(value);
        size = 1.0 / (size * (1.0 / 25400));

        Length sizeX = FormatTools.getPhysicalSizeX(size);
        if (sizeX != null) {
          store.setPixelsPhysicalSizeX(sizeX, 0);
        }
      }
      else if (key.equals("CCD Temperature")) {
        Double temp;
        Matcher hexMatcher = Pattern.compile("0x([0-9A-F]+)").matcher(value);
        if (hexMatcher.matches()) {
          // CCD temperature stored as a hexadecimal string such as "0xEB".
          temp = new Double(Integer.parseInt(hexMatcher.group(1), 16));
          LOGGER.debug("CCD temperature detected as {}; assumed to be invalid", temp);
        }
        else {
          temp = new Double(value.substring(0, value.indexOf(' ')));
          store.setImagingEnvironmentTemperature(
                new Temperature(temp, UNITS.CELSIUS), 0);
        }
      }
    }
  }

  private void readFileInfoMetadata(MetadataStore store) throws IOException {
    in.seek(0);
    findString(FILEINFO_STRING);

    int tagLength = FILEINFO_STRING.length();

    if (in.length() - in.getFilePointer() < tagLength + 20) {
      return;
    }

    in.skipBytes(tagLength + 16);
    int dataLength = in.readInt() - tagLength - 20;

    if (in.length() - in.getFilePointer() < dataLength) {
      return;
    }

    byte[] data = new byte[dataLength];
    if (in.read(data) != dataLength) {
      return;
    }

    String info = new String(data, Constants.ENCODING);
    info = info.trim().replaceAll("(\\r|\\n)+", " | ");

    if (info.isEmpty()) {
      return;
    }

    addGlobalMeta("FileInfo", info);
  }
}
