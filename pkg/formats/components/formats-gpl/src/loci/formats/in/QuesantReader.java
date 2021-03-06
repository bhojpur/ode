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
 * QuesantReader is the file format reader for Quesant .afm files.
 */
public class QuesantReader extends FormatReader {

  // -- Constants --

  public static final int MAX_HEADER_SIZE = 1024;

  // -- Fields --

  private int pixelsOffset;
  private double xSize = 0d;
  private String date = null, comment = null;

  // -- Constructor --

  /** Constructs a new Quesant reader. */
  public QuesantReader() {
    super("Quesant AFM", "afm");
    domains = new String[] {FormatTools.SEM_DOMAIN};
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

    in.seek(pixelsOffset);
    readPlane(in, x, y, w, h, buf);
    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      pixelsOffset = 0;
      xSize = 0d;
      date = comment = null;
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

    while (in.getFilePointer() < MAX_HEADER_SIZE) {
      readVariable();
    }

    in.seek(pixelsOffset);
    m.sizeX = in.readShort();
    pixelsOffset += 2;

    m.sizeY = getSizeX();
    m.pixelType = FormatTools.UINT16;

    m.sizeZ = 1;
    m.sizeC = 1;
    m.sizeT = 1;
    m.imageCount = 1;
    m.dimensionOrder = "XYZCT";

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
    if (date != null) {
      // Insert fake separator between seconds and milliseconds to use
      // DateTools.formatDate()
      int separator = date.lastIndexOf(":");
      if (separator > 0 && date.length() > (separator + 5)) {
        date = date.substring(0, separator + 3) + "." +
          date.substring(separator + 3);
      }
      date = DateTools.formatDate(date, "MMM dd yyyy HH:mm:ss", ".");
      if (date != null) {
        store.setImageAcquisitionDate(new Timestamp(date), 0);
      }
    }

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      store.setImageDescription(comment, 0);

      Length sizeX =
        FormatTools.getPhysicalSizeX((double) xSize / getSizeX());
      Length sizeY =
        FormatTools.getPhysicalSizeY((double) xSize / getSizeY());

      if (sizeX != null) {
        store.setPixelsPhysicalSizeX(sizeX, 0);
      }
      if (sizeY != null) {
        store.setPixelsPhysicalSizeY(sizeY, 0);
      }
    }
  }

  // -- Helper methods --

  private void readVariable() throws IOException {
    String code = in.readString(4);
    if (getMetadataOptions().getMetadataLevel() == MetadataLevel.MINIMUM &&
      !code.equals("IMAG"))
    {
      in.skipBytes(4);
      return;
    }

    int offset = in.readInt();
    long fp = in.getFilePointer();
    if (offset <= 0 || offset > in.length()) return;

    in.seek(offset);

    if (code.equals("SDES")) {
      String sdes = in.readCString().trim();
      if (comment == null) comment = sdes;
      else comment += " " + sdes;
    }
    else if (code.equals("DESC")) {
      int length = in.readShort();
      String desc = in.readString(length);
      if (comment == null) comment = desc;
      else comment += " " + desc;
    }
    else if (code.equals("DATE")) {
      date = in.readCString();
    }
    else if (code.equals("IMAG")) {
      pixelsOffset = offset;
    }
    else if (code.equals("HARD")) {
      xSize = in.readFloat();

      float scanRate = in.readFloat();
      float tunnelCurrent = (in.readFloat() * 10) / 32768;

      in.skipBytes(12);

      float integralGain = in.readFloat();
      float proportGain = in.readFloat();
      boolean isSTM = in.readShort() == 10;
      float dynamicRange = in.readFloat();

      addGlobalMeta("Scan rate (Hz)", scanRate);
      addGlobalMeta("Tunnel current", tunnelCurrent);
      addGlobalMeta("Is STM image", isSTM);
      addGlobalMeta("Integral gain", integralGain);
      addGlobalMeta("Proportional gain", proportGain);
      addGlobalMeta("Z dynamic range", dynamicRange);
    }
    in.seek(fp);
  }

}
