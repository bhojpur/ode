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
 * FEIReader is the file format reader for FEI and Philips .img files.
 */
public class FEIReader extends FormatReader {

  // -- Constants --

  public static final String FEI_MAGIC_STRING = "XL";
  private static final int INVALID_PIXELS = 112;

  // -- Fields --

  private int headerSize;

  // -- Constructor --

  /** Constructs a new FEI reader. */
  public FEIReader() {
    super("FEI/Philips", "img");
    suffixSufficient = false;
    domains = new String[] {FormatTools.SEM_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 2;
    if (!FormatTools.validStream(stream, blockLen, false)) return false;
    return stream.readString(blockLen).startsWith(FEI_MAGIC_STRING);
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    in.seek(headerSize);

    byte[] segment = new byte[getSizeX() / 2];
    byte[] plane = new byte[FormatTools.getPlaneSize(this)];
    // interlace frames - there are four rows of two columns
    for (int q=0; q<4; q++) {
      for (int row=q; row<getSizeY(); row+=4) {
        for (int s=0; s<2; s++) {
          in.read(segment);
          in.skipBytes(INVALID_PIXELS / 2);
          for (int col=s; col<getSizeX(); col+=2) {
            plane[row*getSizeX() + col] = segment[col / 2];
          }
        }
      }
    }

    try (RandomAccessInputStream pixels = new RandomAccessInputStream(plane)) {
      readPlane(pixels, x, y, w, h, buf);
    }

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      headerSize = 0;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);
    in.order(true);

    CoreMetadata m = core.get(0);

    LOGGER.info("Reading file header");

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      in.skipBytes(44);

      float magnification = in.readFloat();
      float kV = in.readFloat() / 1000;
      float wd = in.readFloat();
      in.skipBytes(12);
      float spot = in.readFloat();

      addGlobalMeta("Magnification", magnification);
      addGlobalMeta("kV", kV);
      addGlobalMeta("Working distance", wd);
      addGlobalMeta("Spot", spot);
    }

    in.seek(514);
    m.sizeX = in.readShort() - INVALID_PIXELS;
    m.sizeY = in.readShort();
    in.skipBytes(4);
    headerSize = in.readShort();

    // always one grayscale plane per file

    m.sizeZ = 1;
    m.sizeC = 1;
    m.sizeT = 1;
    m.imageCount = 1;
    m.littleEndian = true;
    m.pixelType = FormatTools.UINT8;
    m.rgb = false;
    m.indexed = false;
    m.interleaved = false;
    m.dimensionOrder = "XYCZT";

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
