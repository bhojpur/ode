/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in;

import java.io.IOException;

import loci.common.DateTools;
import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffParser;

import ode.units.quantity.Length;
import ode.units.UNITS;

import ode.xml.model.primitives.Timestamp;

/**
 * LEOReader is the file format reader for LEO EM files.
 */
public class LEOReader extends BaseTiffReader {

  // -- Constants --

  public static final int LEO_TAG = 34118;
  private static final String[] DATE_FORMATS = new String[] {
      "HH:mm dd-MMM-yyyy", "HH:mm:ss dd MMM yyyy"};

  // -- Fields --

  private Length xSize;
  private String date;
  private String time;
  private Length workingDistance;

  // -- Constructor --

  /** Constructs a new LEO reader. */
  public LEOReader() {
    super("LEO", new String[] {"sxm", "tif", "tiff"});
    domains = new String[] {FormatTools.EM_DOMAIN};
    suffixSufficient = false;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    TiffParser parser = new TiffParser(stream);
    parser.setDoCaching(false);
    IFD ifd = parser.getFirstIFD();
    if (ifd == null) return false;
    return ifd.containsKey(LEO_TAG);
  }

  // -- Internal BaseTiffReader API methods --

  /* @see BaseTiffReader#initStandardMetadata() */
  @Override
  protected void initStandardMetadata() throws FormatException, IOException {
    super.initStandardMetadata();

    String tag = ifds.get(0).getIFDTextValue(LEO_TAG);
    String[] lines = tag.split("\n");

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      for (int line=36; line<lines.length; line++) {
        String t = lines[line];
        if (t.startsWith("AP_") || t.startsWith("DP_") || t.startsWith("SV_")) {
          // Parse metadata as key/value pair and add to the global metadata
          String separator;
          if (t.equals("AP_TIME") || t.equals("AP_DATE")) {
            separator = "\\s+:";
          } else {
            separator = "\\s+=\\s+";
          }
          String value = parseKeyValue(lines[++line], separator);

          // Handle metadata that can be mapped to the ODE model
          if (t.equals("AP_TIME")) {
            time = value;
          } else if (t.equals("AP_DATE")) {
            date = value;
          } else if (t.equals("AP_IMAGE_PIXEL_SIZE")) {
            xSize = FormatTools.parseLength(value);
          } else if (t.equals("AP_WD")) {
            workingDistance = FormatTools.parseLength(value);
          }
        }
      }

      if (xSize == null) {
        // Legacy physical size parsing if API_IMAGE_PIXEL_SIZE is not found
        xSize = FormatTools.getPhysicalSizeY(
          Double.parseDouble(lines[3]) * 1000000);
      }
    }
  }

  /* @see BaseTiffReader#initMetadataStore() */
  @Override
  protected void initMetadataStore() throws FormatException {
    super.initMetadataStore();

    MetadataStore store = makeFilterMetadata();

    String acquisitionDate = DateTools.formatDate(time + " " + date, DATE_FORMATS);
    if (acquisitionDate != null) {
      store.setImageAcquisitionDate(new Timestamp(acquisitionDate), 0);
    }

    if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
      if (xSize != null) {
        store.setPixelsPhysicalSizeX(xSize, 0);
        store.setPixelsPhysicalSizeY(xSize, 0);
      }

      String instrument = MetadataTools.createLSID("Instrument", 0);
      store.setInstrumentID(instrument, 0);
      store.setImageInstrumentRef(instrument, 0);

      store.setObjectiveID(MetadataTools.createLSID("Objective", 0, 0), 0, 0);
      if (workingDistance != null) {
        store.setObjectiveWorkingDistance(workingDistance, 0, 0);
      }
      store.setObjectiveImmersion(MetadataTools.getImmersion("Other"), 0, 0);
      store.setObjectiveCorrection(MetadataTools.getCorrection("Other"), 0, 0);
    }
  }

  private String parseKeyValue(String string, String separator) {
    String[] values = string.split(separator);
    if (values.length == 2) {
      addGlobalMeta(values[0], values[1]);
      return values[1];
    } else {
      return null;
    }
  }
}
