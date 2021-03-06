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

import ode.units.quantity.Length;
import ode.xml.model.primitives.Timestamp;

/**
 * UnisokuReader is the file format reader for Unisoku STM files.
 */
public class UnisokuReader extends FormatReader {

  // -- Constants --

  protected static final String UNISOKU_MAGIC_STRING = ":STM data";

  // -- Fields --

  private String datFile;

  // -- Constructor --

  /** Constructs a new Unisoku reader. */
  public UnisokuReader() {
    super("Unisoku STM", new String[] {"hdr", "dat"});
    domains = new String[] {FormatTools.SPM_DOMAIN};
    suffixSufficient = false;
    hasCompanionFiles = true;
    datasetDescription = "One .HDR file plus one similarly-named .DAT file";
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(String, boolean) */
  @Override
  public boolean isThisType(String name, boolean open) {
    if (checkSuffix(name, "hdr")) {
      return super.isThisType(name, open);
    }

    if (name.indexOf('.') < 0) {
      return false;
    }

    String baseName = name.substring(0, name.lastIndexOf("."));

    Location file = new Location(baseName + ".HDR");
    if (!file.exists()) {
      file = new Location(baseName + ".hdr");
    }

    if (!file.exists()) {
      return false;
    }
    return super.isThisType(file.getAbsolutePath(), open);
  }

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 9;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    return (stream.readString(blockLen)).indexOf(UNISOKU_MAGIC_STRING) >= 0;
  }

  /* @see loci.formats.IFormatReader#getUsedFiles(boolean) */
  @Override
  public String[] getUsedFiles(boolean noPixels) {
    FormatTools.assertId(currentId, true, 1);
    if (noPixels) return new String[] {currentId};
    return new String[] {currentId, datFile};
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    try (RandomAccessInputStream dat = new RandomAccessInputStream(datFile)) {
      dat.order(isLittleEndian());
      readPlane(dat, x, y, w, h, buf);
    }
    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      datFile = null;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    id = new Location(id).getAbsolutePath();

    if (checkSuffix(id, "dat")) {
      id = id.substring(0, id.lastIndexOf(".")) + ".HDR";
    }

    super.initFile(id);

    CoreMetadata m = core.get(0);

    datFile = id.substring(0, id.lastIndexOf(".")) + ".DAT";

    String header = DataTools.readFile(id);
    String[] lines = header.split("\r");
    Length sizeX = null;
    Length sizeY = null;

    String imageName = null, remark = null, date = null;
    double pixelSizeX = 0d, pixelSizeY = 0d;

    for (int i=0; i<lines.length; ) {
      lines[i] = lines[i].trim();
      if (lines[i].startsWith(":")) {
        String key = lines[i++];
        final StringBuilder data = new StringBuilder();
        while (i < lines.length && !lines[i].trim().startsWith(":")) {
          data.append(" ");
          data.append(lines[i++].trim());
        }
        String value = data.toString().trim();
        addGlobalMeta(key, value);
        String[] v = value.split(" ");

        if (key.equals(":data volume(x*y)")) {
          m.sizeX = Integer.parseInt(v[0]);
          m.sizeY = Integer.parseInt(v[1]);
        }
        else if (key.equals(":date; time")) {
          date = DateTools.formatDate(value, "MM/dd/yy HH:mm:ss");
        }
        else if (key.startsWith(":ascii flag; data type")) {
          value = value.substring(value.indexOf(' ') + 1);
          int type = Integer.parseInt(value);
          boolean signed = type % 2 == 1;
          int bytes = type / 2;
          m.pixelType =
            FormatTools.pixelTypeFromBytes(bytes, signed, bytes == 4);
        }
        else if (getMetadataOptions().getMetadataLevel() !=
          MetadataLevel.MINIMUM)
        {
          if (key.equals(":sample name")) {
            imageName = value;
          }
          else if (key.equals(":remark")) {
            remark = value;
          }
          else if (key.startsWith(":x_data ->")) {
            String unit = v[0];
            pixelSizeX = Double.parseDouble(v[2]) - Double.parseDouble(v[1]);
            pixelSizeX /= getSizeX();
            sizeX = FormatTools.getPhysicalSizeX(pixelSizeX, unit);
          }
          else if (key.startsWith(":y_data ->")) {
            String unit = v[0];
            pixelSizeY = Double.parseDouble(v[2]) - Double.parseDouble(v[1]);
            pixelSizeY /= getSizeY();
            sizeY = FormatTools.getPhysicalSizeY(pixelSizeY, unit);
          }
        }
      }
    }

    m.sizeZ = 1;
    m.sizeC = 1;
    m.sizeT = 1;
    m.imageCount = 1;
    m.rgb = false;
    m.interleaved = false;
    m.indexed = false;
    m.dimensionOrder = "XYZCT";
    m.littleEndian = true;

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);

    store.setImageName(imageName, 0);
    if (date != null) {
      store.setImageAcquisitionDate(new Timestamp(date), 0);
    }

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      store.setImageDescription(remark, 0);

      if (sizeX != null) {
        store.setPixelsPhysicalSizeX(sizeX, 0);
      }
      if (sizeY != null) {
        store.setPixelsPhysicalSizeY(sizeY, 0);
      }
    }
  }

}
