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

import ode.units.quantity.Length;

/**
 * SeikoReader is the file format reader for Seiko .xqd/.xqf files.
 */
public class SeikoReader extends FormatReader {

  // -- Constants --

  private static final int HEADER_SIZE = 2944;

  // -- Fields --

  // -- Constructor --

  /** Constructs a new Seiko reader. */
  public SeikoReader() {
    super("Seiko", new String[] {"xqd", "xqf"});
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

    in.seek(HEADER_SIZE);
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
    m.littleEndian = true;
    in.order(isLittleEndian());

    String comment = null;
    double xSize = 0d, ySize = 0d;

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      in.seek(40);
      comment = in.readCString();

      in.seek(156);

      xSize = in.readFloat();
      in.skipBytes(4);
      ySize = in.readFloat();

      addGlobalMeta("Comment", comment);
    }

    in.seek(1402);

    m.sizeX = in.readShort();
    m.sizeY = in.readShort();

    m.sizeZ = 1;
    m.sizeC = 1;
    m.sizeT = 1;
    m.imageCount = 1;
    m.dimensionOrder = "XYZCT";
    m.pixelType = FormatTools.UINT16;

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      store.setImageDescription(comment, 0);

      Length sizeX = FormatTools.getPhysicalSizeX(xSize);
      Length sizeY = FormatTools.getPhysicalSizeY(ySize);
      if (sizeX != null) {
        store.setPixelsPhysicalSizeX(sizeX, 0);
      }
      if (sizeY != null) {
        store.setPixelsPhysicalSizeY(sizeY, 0);
      }
    }
  }

}
