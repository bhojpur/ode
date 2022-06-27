/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in;

import java.io.IOException;

import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.meta.MetadataStore;
import loci.formats.tiff.IFD;
import loci.formats.tiff.PhotoInterp;

/**
 * JPKReader is the file format reader for JPK Instruments files.
 */
public class JPKReader extends BaseTiffReader {

  // -- Constructor --

  /** Constructs a new JPK reader. */
  public JPKReader() {
    super("JPK Instruments", "jpk");
    domains = new String[] {FormatTools.SEM_DOMAIN};
    canSeparateSeries = false;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(String, boolean) */
  @Override
  public boolean isThisType(String name, boolean open) {
    return checkSuffix(name, "jpk");
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    if (getSeriesCount() == 1) {
      return super.openBytes(no, buf, x, y, w, h);
    }
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);
    int ifd = getSeries() == 0 ? 0 : no + 1;

    if (tiffParser == null) {
      initTiffParser();
    }

    tiffParser.fillInIFD(ifds.get(ifd));
    tiffParser.getSamples(ifds.get(ifd), buf, x, y, w, h);
    return buf;
  }

  // -- Internal BaseTiffReader API methods --

  /* @see loci.formats.BaseTiffReader#initStandardMetadata() */
  @Override
  protected void initStandardMetadata() throws FormatException, IOException {
    super.initStandardMetadata();

    ifds = tiffParser.getMainIFDs();

    // repopulate core metadata

    int seriesCount = ifds.size() > 1 ? 2 : 1;
    core.clear();
    for (int s=0; s<seriesCount; s++) {
      CoreMetadata ms = new CoreMetadata();
      core.add(ms);
      IFD ifd = ifds.get(s);
      tiffParser.fillInIFD(ifd);
      PhotoInterp p = ifd.getPhotometricInterpretation();
      int samples = ifd.getSamplesPerPixel();
      ms.rgb = samples > 1 || p == PhotoInterp.RGB;

      ms.sizeX = (int) ifd.getImageWidth();
      ms.sizeY = (int) ifd.getImageLength();
      ms.sizeZ = 1;
      ms.sizeT = s == 0 ? 1 : ifds.size() - 1;
      ms.sizeC = ms.rgb ? samples : 1;
      ms.littleEndian = ifd.isLittleEndian();
      ms.indexed = p == PhotoInterp.RGB_PALETTE &&
        (get8BitLookupTable() != null || get16BitLookupTable() != null);
      ms.imageCount = s == 0 ? 1 : ifds.size() - 1;
      ms.pixelType = ifd.getPixelType();
      ms.metadataComplete = true;
      ms.interleaved = false;
      ms.falseColor = false;
      ms.dimensionOrder = "XYCZT";

      if (getMetadataOptions().getMetadataLevel() != MetadataLevel.MINIMUM) {
        setSeries(s);
        for (Integer key : ifds.get(s).keySet()) {
          if (key >= 32848) {
            addSeriesMeta("Tag " + key, ifds.get(s).get(key));
          }
        }
      }
    }
    setSeries(0);
  }

  /* @see loci.formats.BaseTiffReader#initMetadataStore() */
  @Override
  protected void initMetadataStore() throws FormatException {
    super.initMetadataStore();

    MetadataStore store = makeFilterMetadata();

    for (int i=0; i<getSeriesCount(); i++) {
      store.setImageName("Series " + (i + 1), i);
    }
  }

}
