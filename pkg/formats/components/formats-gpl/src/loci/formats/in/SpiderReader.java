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
import ode.units.UNITS;

/**
 * SpiderReader is the file format reader for SPIDER files.
 */
public class SpiderReader extends FormatReader {

  // -- Constants --

  private static final String DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss";

  // -- Fields --

  private long headerSize = 0;
  private boolean oneHeaderPerSlice = false;

  // -- Constructor --

  /** Constructs a new SPIDER reader. */
  public SpiderReader() {
    super("SPIDER", "spi");
    domains = new String[] {FormatTools.EM_DOMAIN};
    suffixSufficient = true;
    suffixNecessary = false;
  }

  // -- IFormatReader API methods --

  /**
   * @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream)
   */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 104;
    if (!FormatTools.validStream(stream, blockLen, true)) return false;
    int size = (int) stream.readFloat() * 4;
    if (size == 0) {
      stream.seek(0);
      stream.order(false);
      size = (int) stream.readFloat() * 4;
    }
    stream.skipBytes(4);
    size *= (int) stream.readFloat();
    stream.seek(44);
    int nsam = (int) stream.readFloat();
    size *= nsam;
    int headerSize = nsam * (int) stream.readFloat() * 4;
    stream.skipBytes(48);
    int slices = (int) stream.readFloat();
    if (slices > 0) {
      size *= slices;
    }
    return size + headerSize == stream.length() || size == stream.length();
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    long header = headerSize;
    if (oneHeaderPerSlice) {
      header += (no + 1) * headerSize;
    }

    in.seek(header + no * FormatTools.getPlaneSize(this));
    readPlane(in, x, y, w, h, buf);

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      headerSize = 0;
      oneHeaderPerSlice = false;
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

    int nSlice = (int) in.readFloat();
    m.littleEndian = nSlice > 0;
    if (!isLittleEndian()) {
      in.order(isLittleEndian());
      in.seek(0);
      nSlice = (int) in.readFloat();
    }

    int nRow = (int) in.readFloat();
    int irec = (int) in.readFloat();
    in.skipBytes(4);

    int iform = (int) in.readFloat();
    int imami = (int) in.readFloat();
    float fmax = in.readFloat();
    float fmin = in.readFloat();
    float average = in.readFloat();
    float sig = in.readFloat();
    in.skipBytes(4);
    int nsam = (int) in.readFloat();
    int labrec = (int) in.readFloat();
    headerSize = (long) labrec * nsam * 4;

    int iAngle = (int) in.readFloat();
    float phi = in.readFloat();
    float theta = in.readFloat();
    float gamma = in.readFloat();

    float xOff = in.readFloat();
    float yOff = in.readFloat();
    float zOff = in.readFloat();

    float scale = in.readFloat();
    int labbyte = (int) in.readFloat();
    int lenbyte = (int) in.readFloat();
    int istack = (int) in.readFloat();
    in.skipBytes(4);

    float maxim = in.readFloat();
    float imgnum = in.readFloat();
    float lastIndx = in.readFloat();
    in.skipBytes(8);

    float kAngle = in.readFloat();
    float phi1 = in.readFloat();
    float theta1 = in.readFloat();
    float psi1 = in.readFloat();
    float phi2 = in.readFloat();
    float theta2 = in.readFloat();
    float psi2 = in.readFloat();

    float pixelSize = in.readFloat(); // in angstroms
    float ev = in.readFloat();
    in.skipBytes(4 * 61);

    float psi3 = in.readFloat();
    float theta3 = in.readFloat();
    float phi3 = in.readFloat();

    float lAngle = in.readFloat();
    in.skipBytes(4 * 107);

    String creationDate = in.readString(12).trim();
    String creationTime = in.readString(8);
    String title = in.readString(160);

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      addGlobalMeta("NSLICE", nSlice);
      addGlobalMeta("NROW", nRow);
      addGlobalMeta("IREC", irec);
      addGlobalMeta("IFORM", iform);
      addGlobalMeta("IMAMI", imami);
      addGlobalMeta("FMAX", fmax);
      addGlobalMeta("FMIN", fmin);
      addGlobalMeta("AV", average);
      addGlobalMeta("SIG", sig);
      addGlobalMeta("NSAM", nsam);
      addGlobalMeta("LABREC", labrec);
      addGlobalMeta("IANGLE", iAngle);
      addGlobalMeta("PHI", phi);
      addGlobalMeta("THETA", theta);
      addGlobalMeta("GAMMA", gamma);
      addGlobalMeta("XOFF", xOff);
      addGlobalMeta("YOFF", yOff);
      addGlobalMeta("ZOFF", zOff);
      addGlobalMeta("SCALE", scale);
      addGlobalMeta("LABBYT", labbyte);
      addGlobalMeta("LENBYT", lenbyte);
      addGlobalMeta("ISTACK/MAXINDX", istack);
      addGlobalMeta("MAXIM", maxim);
      addGlobalMeta("IMGNUM", imgnum);
      addGlobalMeta("LASTINDX", lastIndx);
      addGlobalMeta("KANGLE", kAngle);
      addGlobalMeta("PHI1", phi1);
      addGlobalMeta("THETA1", theta1);
      addGlobalMeta("PSI1", psi1);
      addGlobalMeta("PHI2", phi2);
      addGlobalMeta("THETA2", theta2);
      addGlobalMeta("PSI2", psi2);
      addGlobalMeta("PIXSIZ", pixelSize);
      addGlobalMeta("EV", ev);
      addGlobalMeta("PHI3", phi3);
      addGlobalMeta("THETA3", theta3);
      addGlobalMeta("PSI3", psi3);
      addGlobalMeta("LANGLE", lAngle);
      addGlobalMeta("CDAT", creationDate);
      addGlobalMeta("CTIM", creationTime);
      addGlobalMeta("CTIT", title);
    }

    m.imageCount = (int) Math.max(nSlice, 1);
    if (maxim > 0) {
      m.imageCount *= maxim;
    }
    m.sizeZ = getImageCount();
    m.sizeC = 1;
    m.sizeT = 1;

    m.sizeY = nRow;
    m.sizeX = nsam;

    m.pixelType = FormatTools.FLOAT;
    m.dimensionOrder = "XYZCT";
    m.rgb = false;

    long planeSize = FormatTools.getPlaneSize(this);
    oneHeaderPerSlice =
      (irec * nsam * 4) != planeSize && ((irec - 1) * 4) != planeSize;

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);

    store.setImageName(title, 0);
    String date = creationDate + " " + creationTime;
    date = DateTools.formatDate(date, DATE_FORMAT);
    if (date != null) {
      store.setImageAcquisitionDate(new Timestamp(date), 0);
    }

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      Double size = new Double(pixelSize);
      Length sizeX = FormatTools.getPhysicalSizeX(size, UNITS.ANGSTROM);
      Length sizeY = FormatTools.getPhysicalSizeY(size, UNITS.ANGSTROM);
      if (sizeX != null) {
        store.setPixelsPhysicalSizeX(sizeX, 0);
      }
      if (sizeY != null) {
        store.setPixelsPhysicalSizeY(sizeY, 0);
      }
    }
  }

}
