/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
*/

package loci.formats.out;

import java.io.IOException;

import ode.units.UNITS;
import ode.units.quantity.Length;

import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;

import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.FormatWriter;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataRetrieve;
import loci.formats.services.WlzService;

/**
 * WlzWriter is the file format writer for Woolz files.
 */
public class WlzWriter extends FormatWriter {

  // -- Fields --

  private WlzService wlz = null;

  public static final String NO_WLZ_MSG =
    "\n" +
    "Woolz is required to read and write Woolz objects.\n" +
    "Please obtain the necessary JAR and native library files from:\n" +
    "http://www.emouseatlas.org/emap/analysis_tools_resources/software/woolz.html.\n" +
    "The source code for these is also available from:\n" +
    "https://github.com/ma-tech/Woolz.";

  private String outputOrder = "XYZCT";

  // -- Constructor --

  public WlzWriter() {
    super("Woolz", new String[] {"wlz"});
  }

  // -- ICSWriter API methods --

  /**
   * Set the order in which dimensions should be written to the file.
   * Valid values are specified in the documentation for
   * {@link loci.formats.IFormatReader#getDimensionOrder()}
   */
  public void setOutputOrder(String outputOrder) {
    this.outputOrder = outputOrder;
  }

  // -- IFormatWriter API methods --

  /**
   * @see loci.formats.IFormatWriter#saveBytes(int, byte[], int, int, int, int)
   */
  @Override
  public void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    if(wlz != null) {
      checkParams(no, buf, x, y, w, h);
      wlz.saveBytes(no, buf, x, y, w, h);
    }
  }

  /* @see loci.formats.IFormatWriter#canDoStacks() */
  @Override
  public boolean canDoStacks() {
    return true;
  }

  /* @see loci.formats.IFormatWriter#getPixelTypes(String) */
  @Override
  public int[] getPixelTypes(String codec) {
    int[] spt;
    if(wlz != null) {
      spt = wlz.getSupPixelTypes();
    }
    else {
      spt  = new int[] {};
    }
    return(spt);
  }

  // -- FormatWriter API methods --

  /* @see loci.formats.FormatWriter#setId(String) */
  @Override
  public void setId(String id) throws FormatException, IOException {
    super.setId(id);
    try {
      ServiceFactory factory = new ServiceFactory();
      wlz = factory.getInstance(WlzService.class);
    }
    catch (DependencyException e) {
      throw new FormatException(NO_WLZ_MSG, e);
    }
    if(wlz != null) {
      MetadataRetrieve meta = getMetadataRetrieve();
      MetadataTools.verifyMinimumPopulated(meta, series);
      wlz.open(id, "w");

      String stageLabelName = null;
      try {
        stageLabelName = meta.getStageLabelName(0);
      }
      catch (NullPointerException e) { }
      int oX = 0;
      int oY = 0;
      int oZ = 0;
      if((stageLabelName != null) &&
         stageLabelName.equals(wlz.getWlzOrgLabelName())) {
        final Length stageX = meta.getStageLabelX(0);
        final Length stageY = meta.getStageLabelY(0);
        final Length stageZ = meta.getStageLabelZ(0);
        oX = (int) Math.rint(stageX.value(UNITS.REFERENCEFRAME).doubleValue());
        oY = (int) Math.rint(stageY.value(UNITS.REFERENCEFRAME).doubleValue());
        oZ = (int) Math.rint(stageZ.value(UNITS.REFERENCEFRAME).doubleValue());
      }
      int nX = meta.getPixelsSizeX(series).getValue().intValue();
      int nY = meta.getPixelsSizeY(series).getValue().intValue();
      int nZ = meta.getPixelsSizeZ(series).getValue().intValue();
      int nC = meta.getPixelsSizeC(series).getValue().intValue();
      int nT = meta.getPixelsSizeT(series).getValue().intValue();
      double vX = 1.0;
      double vY = 1.0;
      double vZ = 1.0;
      if(meta.getPixelsPhysicalSizeX(0) != null) {
        vX = meta.getPixelsPhysicalSizeX(0).value(UNITS.MICROMETER).doubleValue();
      }
      if(meta.getPixelsPhysicalSizeY(0) != null) {
        vY = meta.getPixelsPhysicalSizeY(0).value(UNITS.MICROMETER).doubleValue();
      }
      if(meta.getPixelsPhysicalSizeZ(0) != null) {
        vZ = meta.getPixelsPhysicalSizeZ(0).value(UNITS.MICROMETER).doubleValue();
      }
      int gType = FormatTools.pixelTypeFromString(
                  meta.getPixelsType(series).toString());
      wlz.setupWrite(oX, oY, oZ, nX, nY, nZ, nC, nT, vX, vY, vZ, gType);
    }
  }

  /* @see loci.formats.FormatWriter#close() */
  @Override
  public void close() throws IOException {
    super.close();
    if(wlz != null) {
      wlz.close();
    }
  }

  // -- Helper methods --

}
