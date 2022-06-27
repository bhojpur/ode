/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

/**
 * VGSAMReader is the file format reader for VG SAM .dti files.
 */
public class VGSAMReader extends FormatReader {

  // -- Constants --

  public static final String VG_MAGIC_STRING = "VGS";
  public static final int PIXEL_OFFSET = 368;

  // -- Fields --

  // -- Constructor --

  /** Constructs a new VG SAM reader. */
  public VGSAMReader() {
    super("VG SAM", "dti");
    domains = new String[] {FormatTools.SPM_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 3;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    return (stream.readString(blockLen)).indexOf(VG_MAGIC_STRING) >= 0;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    in.seek(PIXEL_OFFSET);
    readPlane(in, x, y, w, h, buf);
    return buf;
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);
    CoreMetadata m = core.get(0);

    in.seek(348);
    m.sizeX = in.readInt();
    m.sizeY = in.readInt();
    in.skipBytes(4);

    int bpp = in.readInt();
    addGlobalMeta("Bytes per pixel", bpp);
    m.pixelType = FormatTools.pixelTypeFromBytes(bpp, false, bpp == 4);
    m.littleEndian = false;
    m.sizeZ = 1;
    m.sizeC = 1;
    m.sizeT = 1;
    m.imageCount = 1;
    m.rgb = false;
    m.interleaved = false;
    m.dimensionOrder = "XYZCT";

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
