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

import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

/**
 * Zeiss LMS reader for data from Zeiss CSM 700 systems.  Not to be confused
 * with the Zeiss LSM reader, which reads the much more common .lsm format
 */
public class ZeissLMSReader extends FormatReader {

  // -- Constants --

  private static final String CHECK = "LMSFLE";
  private static final String MARKER = "BM6";

  // -- Fields --

  private ArrayList<Long> offsets = new ArrayList<Long>();
  private byte[][] lut;

  // -- Constructor --

  /** Constructs a new LMS reader. */
  public ZeissLMSReader() {
    super("Zeiss LMS", "lms");
    domains = new String[] {FormatTools.LM_DOMAIN};
  }

  // -- IFormatReader API methods --

  /** @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    int checkLen = 16;
    if (!FormatTools.validStream(stream, checkLen, false)) {
      return false;
    }
    return stream.readString(checkLen).indexOf(CHECK) >= 0;
  }

  /* @see loci.formats.IFormatReader#get8BitLookupTable() */
  public byte[][] get8BitLookupTable() {
    FormatTools.assertId(currentId, true, 1);
    if (isIndexed()) {
      return lut;
    }
    return null;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    in.seek(offsets.get(getSeriesCount() - getSeries() - 1));
    in.skipBytes((long) no * FormatTools.getPlaneSize(this));
    readPlane(in, x, y, w, h, buf);

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      offsets.clear();
      lut = null;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  public void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);

    CoreMetadata m = core.get(0);
    CoreMetadata thumb = new CoreMetadata();

    m.littleEndian = true;
    thumb.littleEndian = true;

    in.order(m.littleEndian);

    in.seek(18);
    double magnification = in.readInt();

    // all assumed to be constant based upon the CSM 700 data sheet
    thumb.sizeX = 1280;
    thumb.sizeY = 1024;
    thumb.pixelType = FormatTools.UINT8;
    thumb.sizeC = 3;
    thumb.rgb = true;
    thumb.interleaved = true;
    thumb.dimensionOrder = "XYCZT";

    m.sizeX = 1280;
    m.sizeY = 1024;
    m.pixelType = FormatTools.UINT16;
    m.sizeC = 1;
    m.rgb = false;
    m.dimensionOrder = "XYCZT";
    m.indexed = true;

    // each image can be found using the "BM6" marker

    seekToNextMarker();
    in.skipBytes(50);
    offsets.add(in.getFilePointer());
    in.skipBytes((long) thumb.sizeX * thumb.sizeY * thumb.sizeC);

    seekToNextMarker();
    in.skipBytes(50);

    lut = new byte[3][256];
    for (int i=0; i<lut[0].length; i++) {
      for (int j=0; j<lut.length; j++) {
        lut[j][i] = in.readByte();
      }
      in.skipBytes(1);  // skip alpha channel
    }

    offsets.add(in.getFilePointer());

    // again, Z stack is assumed based upon the CSM 700 data sheet

    thumb.sizeZ = 1;
    thumb.sizeT = 1;
    thumb.imageCount = thumb.sizeZ * thumb.sizeT;

    long availableBytes = in.length() - offsets.get(1);
    int planeSize =
      m.sizeX * m.sizeY * FormatTools.getBytesPerPixel(m.pixelType);
    m.sizeZ = (int) (availableBytes / planeSize);
    m.sizeT = 1;
    m.imageCount = m.sizeZ * m.sizeT;

    core.add(thumb);

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);

    store.setInstrumentID(MetadataTools.createLSID("Instrument", 0), 0);
    String objective = MetadataTools.createLSID("Objective", 0, 0);
    store.setObjectiveID(objective, 0, 0);
    store.setObjectiveNominalMagnification(magnification, 0, 0);
    store.setObjectiveSettingsID(objective, 0);
    store.setObjectiveSettingsID(objective, 1);
  }

  private void seekToNextMarker() throws IOException {
    while (in.getFilePointer() < in.length()) {
      String check = in.readString(3);
      if (check.equals(MARKER)) {
        in.skipBytes(1);
        return;
      }
      in.seek(in.getFilePointer() - 2);
    }
  }

}
