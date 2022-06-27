/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import loci.common.RandomAccessInputStream;
import loci.common.xml.XMLTools;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffParser;

import ode.xml.model.primitives.Timestamp;

import ode.units.quantity.ElectricPotential;
import ode.units.quantity.Frequency;
import ode.units.quantity.Length;
import ode.units.quantity.Temperature;
import ode.units.quantity.Time;
import ode.units.UNITS;

/**
 * NikonElementsTiffReader is the file format reader for TIFF files produced
 * by Nikon Elements.
 */
public class NikonElementsTiffReader extends BaseTiffReader {

  // -- Constants --

  private static final int NIKON_XML_TAG = 65332;
  private static final int NIKON_XML_TAG_2 = 65333;

  // -- Fields --

  private ND2Handler handler;

  // -- Constructor --

  public NikonElementsTiffReader() {
    super("Nikon Elements TIFF", new String[] {"tif", "tiff"});
    suffixSufficient = false;
    domains = new String[] {FormatTools.LM_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    TiffParser tp = new TiffParser(stream);
    IFD ifd = tp.getFirstIFD();
    if (ifd == null) return false;
    return ifd.containsKey(NIKON_XML_TAG);
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      handler = null;
    }
  }

  // -- Internal BaseTiffReader API methods --

  /* @see BaseTiffReader#initStandardMetadata() */
  @Override
  protected void initStandardMetadata() throws FormatException, IOException {
    super.initStandardMetadata();

    String xml = ifds.get(0).getIFDTextValue(NIKON_XML_TAG).trim();
    if (xml.length() == 0) {
      xml = ifds.get(0).getIFDTextValue(NIKON_XML_TAG_2).trim();
    }
    int open = xml.indexOf('<');
    if (open >= 0) {
      xml = xml.substring(open);
    }
    xml = "<NIKON>" + xml + "</NIKON>";
    xml = XMLTools.sanitizeXML(xml);

    handler = new ND2Handler(core, false, getImageCount());
    try {
      XMLTools.parseXML(xml, handler);

      final Map<String, Object> globalMetadata = handler.getMetadata();
      for (final Map.Entry<String, Object> entry : globalMetadata.entrySet()) {
        addGlobalMeta(entry.getKey(), entry.getValue());
      }
    }
    catch (IOException e) { }
  }

  /* @see BaseTiffReader#initMetadataStore() */
  @Override
  protected void initMetadataStore() throws FormatException {
    super.initMetadataStore();
    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this, true);

    String date = handler.getDate();
    if (date != null) {
      store.setImageAcquisitionDate(new Timestamp(date), 0);
    }

    if (getMetadataOptions().getMetadataLevel() == MetadataLevel.MINIMUM) {
      return;
    }

    Length sizeX = FormatTools.getPhysicalSizeX(handler.getPixelSizeX());
    Length sizeY = FormatTools.getPhysicalSizeY(handler.getPixelSizeY());
    Length sizeZ = FormatTools.getPhysicalSizeZ(handler.getPixelSizeZ());
    if (sizeX != null) {
      store.setPixelsPhysicalSizeX(sizeX, 0);
    }
    if (sizeY != null) {
      store.setPixelsPhysicalSizeY(sizeY, 0);
    }
    if (sizeZ != null) {
      store.setPixelsPhysicalSizeZ(sizeZ, 0);
    }

    String instrument = MetadataTools.createLSID("Instrument", 0);
    store.setInstrumentID(instrument, 0);
    store.setImageInstrumentRef(instrument, 0);

    ArrayList<Double> exposureTimes = handler.getExposureTimes();
    ArrayList<Length> posX = handler.getXPositions();
    ArrayList<Length> posY = handler.getYPositions();
    ArrayList<Length> posZ = handler.getZPositions();

    for (int i=0; i<getImageCount(); i++) {
      int c = getZCTCoords(i)[1];
      if (c < exposureTimes.size() && exposureTimes.get(c) != null) {
        store.setPlaneExposureTime(new Time(exposureTimes.get(c), UNITS.SECOND), 0, i);
      }

      if (i < posX.size()) {
        store.setPlanePositionX(posX.get(i), 0, i);
      }
      if (i < posY.size()) {
        store.setPlanePositionY(posY.get(i), 0, i);
      }
      if (i < posZ.size()) {
        store.setPlanePositionZ(posZ.get(i), 0, i);
      }
    }

    String detector = MetadataTools.createLSID("Detector", 0, 0);
    store.setDetectorID(detector, 0, 0);
    store.setDetectorModel(handler.getCameraModel(), 0, 0);
    store.setDetectorType(MetadataTools.getDetectorType("Other"), 0, 0);

    ArrayList<String> channelNames = handler.getChannelNames();
    ArrayList<String> modality = handler.getModalities();
    ArrayList<String> binning = handler.getBinnings();
    ArrayList<Double> speed = handler.getSpeeds();
    ArrayList<Double> gain = handler.getGains();
    ArrayList<Double> temperature = handler.getTemperatures();
    ArrayList<Double> exWave = handler.getExcitationWavelengths();
    ArrayList<Double> emWave = handler.getEmissionWavelengths();
    ArrayList<Integer> power = handler.getPowers();
    ArrayList<Hashtable<String, String>> rois = handler.getROIs();
    Double pinholeSize = handler.getPinholeSize();

    for (int c=0; c<getEffectiveSizeC(); c++) {
      if (pinholeSize != null) {
        store.setChannelPinholeSize(new Length(pinholeSize, UNITS.MICROMETER), 0, c);
      }
      if (c < channelNames.size()) {
        store.setChannelName(channelNames.get(c), 0, c);
      }
      if (c < modality.size()) {
        store.setChannelAcquisitionMode(
          MetadataTools.getAcquisitionMode(modality.get(c)), 0, c);
      }
      if (c < emWave.size()) {
        Length em = FormatTools.getEmissionWavelength(emWave.get(c));
        if (em != null) {
          store.setChannelEmissionWavelength(em, 0, c);
        }
      }
      if (c < exWave.size()) {
        Length ex = FormatTools.getExcitationWavelength(exWave.get(c));
        if (ex != null) {
          store.setChannelExcitationWavelength(ex, 0, c);
        }
      }
      if (c < binning.size()) {
        store.setDetectorSettingsBinning(MetadataTools.getBinning(binning.get(c)), 0, c);
      }
      if (c < gain.size()) {
        store.setDetectorSettingsGain(gain.get(c), 0, c);
      }
      if (c < speed.size()) {
        store.setDetectorSettingsReadOutRate(
                new Frequency(speed.get(c), UNITS.HERTZ), 0, c);
      }
      store.setDetectorSettingsID(detector, 0, c);
    }

    if (temperature.size() > 0) {
      store.setImagingEnvironmentTemperature(new Temperature(
              temperature.get(0), UNITS.CELSIUS), 0);
    }

    Double voltage = handler.getVoltage();
    if (voltage != null) {
      store.setDetectorSettingsVoltage(
              new ElectricPotential(voltage, UNITS.VOLT), 0, 0);
    }

    Double na = handler.getNumericalAperture();
    if (na != null) store.setObjectiveLensNA(na, 0, 0);

    Double mag = handler.getMagnification();
    if (mag != null) store.setObjectiveCalibratedMagnification(mag, 0, 0);

    store.setObjectiveModel(handler.getObjectiveModel(), 0, 0);

    String immersion = handler.getImmersion();
    if (immersion == null) immersion = "Other";
    store.setObjectiveImmersion(MetadataTools.getImmersion(immersion), 0, 0);

    String correction = handler.getCorrection();
    if (correction == null || correction.length() == 0) correction = "Other";
    store.setObjectiveCorrection(MetadataTools.getCorrection(correction), 0, 0);

    String objective = MetadataTools.createLSID("Objective", 0, 0);
    store.setObjectiveID(objective, 0, 0);
    store.setObjectiveSettingsID(objective, 0);

    Double refractiveIndex = handler.getRefractiveIndex();
    if (refractiveIndex != null) {
      store.setObjectiveSettingsRefractiveIndex(refractiveIndex, 0);
    }

    if (getMetadataOptions().getMetadataLevel() == MetadataLevel.NO_OVERLAYS) {
      return;
    }

    handler.populateROIs(store);
  }

}
