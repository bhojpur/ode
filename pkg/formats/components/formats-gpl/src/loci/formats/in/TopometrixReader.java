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

/**
 * TopometrixReader is the file format reader for TopoMetrix .tfr, .ffr,
 * .zfr, .zfp, and .2fl files.
 */
public class TopometrixReader extends FormatReader {

  // -- Fields --

  private long pixelOffset;

  // -- Constructor --

  /** Constructs a new TopoMetrix reader. */
  public TopometrixReader() {
    super("TopoMetrix", new String[] {"tfr", "ffr", "zfr", "zfp", "2fl"});
    domains = new String[] {FormatTools.SEM_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 6;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    String check = stream.readString(blockLen);
    if (!check.startsWith("#R")) return false;
    try {
      Double.parseDouble(check.substring(2, 5));
    }
    catch (NumberFormatException e) { }
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
    m.littleEndian = true;
    in.order(isLittleEndian());

    in.skipBytes(2);
    int version = (int) Double.parseDouble(in.readString(4));
    in.skipBytes(2);
    pixelOffset = Long.parseLong(in.readString(4));
    in.skipBytes(2);

    long fp = in.getFilePointer();
    String date = in.readLine().trim();
    int commentLength = (int) (240 - in.getFilePointer() + fp);
    String comment = in.readString(commentLength).trim();

    if (version == 5) {
      in.seek(452);
    }

    in.skipBytes(152);

    m.sizeX = in.readShort();
    in.skipBytes(2);
    m.sizeY = in.readShort();

    double xSize = 0d, ySize = 0d;
    double adc = 0d, dacToWorldZero = 0d;

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      in.skipBytes(10);
      if (version == 5) {
        in.skipBytes(4);
        xSize = in.readDouble();
        in.skipBytes(8);
        ySize = in.readDouble();
        adc = in.readDouble();
        dacToWorldZero = in.readDouble();

        in.skipBytes(1176);

        double sampleVolts = in.readDouble();
        double tunnelCurrent = in.readDouble();
        in.skipBytes(16);
        double timePerPixel = in.readDouble();
        in.skipBytes(40);
        double scanAngle = in.readDouble();

        addGlobalMeta("Sample volts", sampleVolts);
        addGlobalMeta("Tunnel current", tunnelCurrent);
        addGlobalMeta("Scan rate", timePerPixel);
        addGlobalMeta("Scan angle", scanAngle);
      }
      else {
        xSize = in.readFloat();
        in.skipBytes(4);
        ySize = in.readFloat();
        adc = in.readFloat();
        in.skipBytes(764);
        dacToWorldZero = in.readFloat();
      }

      addGlobalMeta("Version", version);
      addGlobalMeta("X size (in um)", xSize);
      addGlobalMeta("Y size (in um)", ySize);
      addGlobalMeta("ADC", adc);
      addGlobalMeta("DAC to world zero", dacToWorldZero);
      addGlobalMeta("Comment", comment);
      addGlobalMeta("Acquisition date", date);
    }

    m.pixelType = FormatTools.UINT16;
    m.sizeZ = 1;
    m.sizeC = 1;
    m.sizeT = 1;
    m.imageCount = 1;
    m.dimensionOrder = "XYZCT";

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);

    date = DateTools.formatDate(date,
      new String[] {"MM/dd/yy HH:mm:ss", "MM/dd/yyyy HH:mm:ss"});
    if (date != null) {
      store.setImageAcquisitionDate(new Timestamp(date), 0);
    }

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      Length sizeX =
        FormatTools.getPhysicalSizeX((double) xSize / getSizeX());
      Length sizeY =
        FormatTools.getPhysicalSizeY((double) ySize / getSizeY());

      if (sizeX != null) {
        store.setPixelsPhysicalSizeX(sizeX, 0);
      }
      if (sizeY != null) {
        store.setPixelsPhysicalSizeY(sizeY, 0);
      }
      store.setImageDescription(comment, 0);
    }
  }

}
