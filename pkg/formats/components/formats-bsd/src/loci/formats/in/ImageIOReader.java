/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.in;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.gui.AWTImageTools;
import loci.formats.meta.MetadataStore;

/**
 * ImageIOReader is the superclass for file format readers
 * that use the javax.imageio package.
 */
public abstract class ImageIOReader extends BIFormatReader {

  // -- Fields --

  /**
   * Transient reference to a {@link BufferedImage} for this
   * {@link #currentId}. May be null after de-serialization,
   * in which case {@link #initImage} must be called.
   */
  private transient BufferedImage img;

  // -- Constructors --

  /** Constructs a new ImageIOReader. */
  public ImageIOReader(String name, String suffix) {
    super(name, suffix);
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
  }

  /** Constructs a new ImageIOReader. */
  public ImageIOReader(String name, String[] suffixes) {
    super(name, suffixes);
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      img = null;
    }
  }

  /* @see loci.formats.IFormatReader#getOptimalTileHeight() */
  @Override
  public int getOptimalTileHeight() {
    FormatTools.assertId(currentId, true, 1);
    return getSizeY();
  }

  /* @see loci.formats.IFormatReader#openPlane(int, int, int, int, int int) */
  @Override
  public Object openPlane(int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, -1, x, y, w, h);

    if (img == null) {
      initImage();
    }
    return AWTImageTools.getSubimage(img, isLittleEndian(), x, y, w, h);
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    CoreMetadata m = core.get(0);

    LOGGER.info("Populating metadata");
    m.imageCount = 1;
    initImage();

    m.sizeX = img.getWidth();
    m.sizeY = img.getHeight();

    m.rgb = img.getRaster().getNumBands() > 1;

    m.sizeZ = 1;
    m.sizeC = isRGB() ? 3 : 1;
    m.sizeT = 1;
    m.dimensionOrder = "XYCZT";
    m.pixelType = AWTImageTools.getPixelType(img);
    m.interleaved = false;
    m.littleEndian = false;
    m.metadataComplete = true;
    m.indexed = false;
    m.falseColor = false;

    // populate the metadata store
    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

  protected void initImage() throws IOException, FormatException {
      ;
      try (RandomAccessInputStream ras = new RandomAccessInputStream(currentId);
            DataInputStream dis = new DataInputStream(ras)) {
        img = ImageIO.read(dis);
      }
      if (img == null) throw new FormatException("Invalid image stream");

  }

}
