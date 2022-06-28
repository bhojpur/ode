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
 * MolecularImagingReader is the file format reader for Molecular Imaging files.
 */
public class MolecularImagingReader extends FormatReader {

  // -- Constants --

  private static final String MAGIC_STRING = "UK SOFT";
  private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";

  // -- Fields --

  private long pixelOffset = 0;

  // -- Constructor --

  /** Constructs a new Molecular Imaging reader. */
  public MolecularImagingReader() {
    super("Molecular Imaging", "stp");
    domains = new String[] {FormatTools.SEM_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 16;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    return stream.readString(blockLen).indexOf(MAGIC_STRING) > 0;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);
    in.seek(pixelOffset + no * FormatTools.getPlaneSize(this));
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

    m.sizeZ = 0;
    String date = null;
    double pixelSizeX = 0d, pixelSizeY = 0d;

    String data = in.findString("Data_section  \r\n");
    String[] lines = data.split("\n");
    for (String line : lines) {
      line = line.trim();

      int space = line.indexOf(' ');
      if (space != -1) {
        String key = line.substring(0, space).trim();
        String value = line.substring(space + 1).trim();
        addGlobalMeta(key, value);

        if (key.equals("samples_x")) {
          m.sizeX = Integer.parseInt(value);
        }
        else if (key.equals("samples_y")) {
          m.sizeY = Integer.parseInt(value);
        }
        else if (key.equals("buffer_id")) {
          m.sizeZ++;
        }
        else if (key.equals("Date")) {
          date = value;
        }
        else if (key.equals("time")) {
          date += " " + value;
        }
        else if (key.equals("length_x")) {
          pixelSizeX = Double.parseDouble(value) / getSizeX();
        }
        else if (key.equals("length_y")) {
          pixelSizeY = Double.parseDouble(value) / getSizeY();
        }
      }
    }
    pixelOffset = in.getFilePointer();

    m.sizeC = 1;
    m.sizeT = 1;
    m.imageCount = getSizeZ();
    m.rgb = false;
    m.pixelType = FormatTools.UINT16;
    m.littleEndian = true;
    m.dimensionOrder = "XYZCT";

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
    if (date != null) {
      date = DateTools.formatDate(date, DATE_FORMAT);
      if (date != null) {
        store.setImageAcquisitionDate(new Timestamp(date), 0);
      }
    }

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      Length sizeX = FormatTools.getPhysicalSizeX(pixelSizeX);
      Length sizeY = FormatTools.getPhysicalSizeY(pixelSizeY);
      if (sizeX != null) {
        store.setPixelsPhysicalSizeX(sizeX, 0);
      }
      if (sizeY != null) {
        store.setPixelsPhysicalSizeY(sizeY, 0);
      }
    }
  }

}
