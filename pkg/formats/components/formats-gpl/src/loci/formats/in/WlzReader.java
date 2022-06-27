/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
*/

package loci.formats.in;

import java.io.IOException;

import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;

import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.services.WlzService;
import loci.formats.meta.MetadataStore;

import ode.xml.model.primitives.PositiveFloat;

import ode.units.quantity.Length;
import ode.units.UNITS;

/**
 * WlzReader is a file format reader for Woolz files.
 * Woolz is available from: https://github.com/ma-tech/Woolz
 */
public class WlzReader extends FormatReader {

  // -- Constants --

  // -- Static initializers --

  static {
  }

  // -- Fields --

  private transient WlzService wlz = null;

  public static final String NO_WLZ_MSG =
    "\n" +
    "Woolz is required to read and write Woolz objects.\n" +
    "Please obtain the necessary JAR and native library files from:\n" +
    "http://www.emouseatlas.org/emap/analysis_tools_resources/software/woolz.html.\n" +
    "The source code for these is also available from:\n" +
    "https://github.com/ma-tech/Woolz.";

  // -- Constructor --

  public WlzReader() {
    super("Woolz", new String[] {"wlz"});
    domains = new String[] {FormatTools.UNKNOWN_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see IFormatReader#isThisType(String, boolean) */
  @Override
  public boolean isThisType(String file, boolean open) {
    return super.isThisType(file, open);
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);
    if (wlz != null) {
      buf = wlz.readBytes(no, buf, x, y, w, h);
    }
    else {
      try {
        ServiceFactory factory = new ServiceFactory();
        wlz = factory.getInstance(WlzService.class);
      }
      catch (DependencyException e) {
        throw new FormatException(NO_WLZ_MSG, e);
      }
      if (wlz != null) {
        wlz.open(currentId, "r");
        buf = wlz.readBytes(no, buf, x, y, w, h);
      }
    }
    return buf;
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    try {
      ServiceFactory factory = new ServiceFactory();
      wlz = factory.getInstance(WlzService.class);
    }
    catch (DependencyException e) {
      throw new FormatException(NO_WLZ_MSG, e);
    }
    if (wlz != null) {
      wlz.open(id, "r");
      CoreMetadata md = core.get(0);
      MetadataStore store = makeFilterMetadata();
      md.rgb = wlz.isRGB();
      md.interleaved = false;
      md.indexed = false;
      md.sizeX = wlz.getSizeX();
      md.sizeY = wlz.getSizeY();
      md.sizeZ = wlz.getSizeZ();
      md.sizeC = wlz.getSizeC();
      md.sizeT = wlz.getSizeT();
      md.dimensionOrder = "XYZCT";
      md.imageCount = wlz.getSizeZ();
      md.pixelType = wlz.getPixelType();
      PositiveFloat x = new PositiveFloat(Math.abs(wlz.getVoxSzX()));
      PositiveFloat y = new PositiveFloat(Math.abs(wlz.getVoxSzY()));
      PositiveFloat z = new PositiveFloat(Math.abs(wlz.getVoxSzZ()));
      store.setPixelsPhysicalSizeX(FormatTools.createLength(x, UNITS.MICROMETER), 0);
      store.setPixelsPhysicalSizeY(FormatTools.createLength(y, UNITS.MICROMETER), 0);
      store.setPixelsPhysicalSizeZ(FormatTools.createLength(z, UNITS.MICROMETER), 0);
      store.setStageLabelName(wlz.getWlzOrgLabelName(), 0);
      store.setStageLabelX(new Length(wlz.getOrgX(), UNITS.REFERENCEFRAME), 0);
      store.setStageLabelY(new Length(wlz.getOrgY(), UNITS.REFERENCEFRAME), 0);
      store.setStageLabelZ(new Length(wlz.getOrgZ(), UNITS.REFERENCEFRAME), 0);
      MetadataTools.populatePixels(store, this);
    }
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      if (wlz != null) {
        wlz.close();
        wlz = null;
      }
    }
  }

  // -- Helper methods --

}
