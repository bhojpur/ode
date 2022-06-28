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

import loci.common.Location;
import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

/**
 * JEOLReader is the file format reader for JEOL files.
 */
public class JEOLReader extends FormatReader {

  // -- Fields --

  private long pixelOffset;
  private String parameterFile;

  // -- Constructor --

  /** Constructs a new JEOL reader. */
  public JEOLReader() {
    super("JEOL", new String[] {"dat", "img", "par"});
    domains = new String[] {FormatTools.SEM_DOMAIN};
    hasCompanionFiles = true;
    suffixSufficient = false;
    datasetDescription = "A single .dat file or an .img file with a " +
      "similarly-named .par file";
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(String) */
  @Override
  public boolean isThisType(String name, boolean open) {
    if (checkSuffix(name, "par") && open) {
      String base = new Location(name).getAbsoluteFile().getAbsolutePath();
      base = base.substring(0, base.lastIndexOf("."));
      String id = base + ".IMG";
      if (!new Location(id).exists()) {
        id = base + ".DAT";
      }
      if (!new Location(id).exists()) {
        return false;
      }
      return true;
    }
    if (checkSuffix(name, "dat") && open) {
      try (RandomAccessInputStream stream = new RandomAccessInputStream(name)) {
        if (stream.length() == (1024 * 1024)) return true;
      }
      catch (IOException e) { }
      
      return false;
    }
    return super.isThisType(name, open);
  }

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 2;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    String magic = stream.readString(blockLen);
    return magic.equals("MG") || magic.equals("IM");
  }

  /* @see loci.formats.IFormatReader#getSeriesUsedFiles(boolean) */
  @Override
  public String[] getSeriesUsedFiles(boolean noPixels) {
    if (noPixels) {
      return parameterFile == null ? null : new String[] {parameterFile};
    }
    String id = new Location(currentId).getAbsolutePath();
    return parameterFile == null ? new String[] {id} :
      new String[] {id, parameterFile};
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
      parameterFile = null;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    if (checkSuffix(id, "par")) {
      String base = new Location(id).getAbsoluteFile().getAbsolutePath();
      base = base.substring(0, base.lastIndexOf("."));
      id = base + ".IMG";
      if (!new Location(id).exists()) {
        id = base + ".DAT";
      }
      if (!new Location(id).exists()) {
        throw new FormatException("Could not find image file.");
      }
    }

    super.initFile(id);
    in = new RandomAccessInputStream(id);
    CoreMetadata m = core.get(0);
    m.littleEndian = true;
    in.order(isLittleEndian());

    parameterFile = id.substring(0, id.lastIndexOf(".")) + ".PAR";
    parameterFile = new Location(parameterFile).getAbsolutePath();
    if (!new Location(parameterFile).exists()) parameterFile = null;

    String magic = in.readString(2);

    if (magic.equals("MG")) {
      in.seek(0x63c);
      m.sizeX = in.readInt();
      m.sizeY = in.readInt();
      pixelOffset = in.getFilePointer() + 540;
    }
    else if (magic.equals("IM")) {
      int commentLength = in.readShort();
      m.sizeX = 1024;
      pixelOffset = in.getFilePointer() + commentLength + 56;
      m.sizeY = (int) ((in.length() - pixelOffset) / getSizeX());
    }
    else {
      m.sizeX = 1024;
      m.sizeY = 1024;
      pixelOffset = 0;
    }

    addGlobalMeta("Pixel data offset", pixelOffset);

    m.pixelType = FormatTools.UINT8;
    m.sizeZ = 1;
    m.sizeC = 1;
    m.sizeT = 1;
    m.imageCount = 1;
    m.dimensionOrder = "XYZCT";

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
