/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers%
 */

package loci.formats.out;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ode.xml.meta.ODEXMLMetadataRoot;
import ode.xml.model.primitives.NonNegativeInteger;
import ode.xml.model.primitives.PositiveInteger;

import loci.common.Location;
import loci.common.Constants;
import loci.common.RandomAccessInputStream;
import loci.common.RandomAccessOutputStream;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.common.xml.XMLTools;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.meta.MetadataRetrieve;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;
import loci.formats.tiff.IFD;
import loci.formats.tiff.TiffSaver;
import loci.formats.in.MetadataOptions;
import loci.formats.in.DynamicMetadataOptions;

/**
 * ODETiffWriter is the file format writer for ODE-TIFF files.
 */
public class ODETiffWriter extends TiffWriter {

  // -- Constants --

  private static final String WARNING_COMMENT =
    "<!-- Warning: this comment is an ODE-XML metadata block, which " +
    "contains crucial dimensional parameters and other important metadata. " +
    "Please edit cautiously (if at all), and back up the original data " +
    "before doing so. For more information, see the ODE-TIFF web site: " +
    FormatTools.URL_ODE_TIFF + ". -->";

  public static final String COMPANION_KEY = "odetiff.companion";

  // -- Fields --

  private String[][] imageLocations;
  private ODEXMLMetadata odeMeta;
  private ODEXMLService service;
  private Map<String, Integer> ifdCounts = new HashMap<String, Integer>();

  private Map<String, String> uuids = new HashMap<String, String>();

  // -- Constructor --

  public ODETiffWriter() {
    super("ODE-TIFF",
      new String[] {"ode.tif", "ode.tiff", "ode.tf2", "ode.tf8", "ode.btf"});
  }

  // -- IFormatHandler API methods --

  /* @see loci.formats.IFormatHandler#close() */
  @Override
  public void close() throws IOException {
    try {
      if (currentId != null) {
        setupServiceAndMetadata();

        // remove any BinData and old TiffData elements from the ODE-XML
        service.removeBinData(odeMeta);
        service.removeTiffData(odeMeta);

        for (int series=0; series<odeMeta.getImageCount(); series++) {
          setSeries(series);
          populateImage(odeMeta, series);
        }

        String companion = getCompanion();
        String companionUUID = null;
        if (null != companion) {
          String companionXML = getODEXML(companion);
          PrintWriter out = new PrintWriter(companion, Constants.ENCODING);
          out.println(XMLTools.indentXML(companionXML, true));
          out.close();
          companionUUID = "urn:uuid:" + getUUID(
              new Location(companion).getName());
        }

        List<String> files = new ArrayList<String>();
        for (String[] s : imageLocations) {
          for (String f : s) {
            if (!files.contains(f) && f != null) {
              files.add(f);

              String xml = null;
              if (null != companion) {
                xml = getBinaryOnlyODEXML(f, companion, companionUUID);
              } else {
                xml = getODEXML(f);
              }
              xml = insertWarningComment(xml);
              if (getMetadataOptions().isValidate()) {
                service.validateODEXML(xml);
              }

              // write ODE-XML to the first IFD's comment
              saveComment(f, xml);
            }
          }
        }
      }
    }
    catch (DependencyException de) {
      throw new RuntimeException(de);
    }
    catch (ServiceException se) {
      throw new RuntimeException(se);
    }
    catch (FormatException fe) {
      throw new RuntimeException(fe);
    }
    catch (IllegalArgumentException iae) {
      throw new RuntimeException(iae);
    }
    finally {
      super.close();

      boolean canReallyClose =
        odeMeta == null || ifdCounts.size() == odeMeta.getImageCount();

      if (odeMeta != null && canReallyClose) {
        int odePlaneCount = 0;
        for (int i=0; i<odeMeta.getImageCount(); i++) {
          int sizeZ = odeMeta.getPixelsSizeZ(i).getValue();
          int sizeC = odeMeta.getPixelsSizeC(i).getValue();
          int sizeT = odeMeta.getPixelsSizeT(i).getValue();

          odePlaneCount += sizeZ * sizeC * sizeT;
        }

        int ifdCount = 0;
        for (String key : ifdCounts.keySet()) {
          ifdCount += ifdCounts.get(key);
        }

        canReallyClose = odePlaneCount == ifdCount;
      }

      if (canReallyClose) {
        imageLocations = null;
        odeMeta = null;
        service = null;
        ifdCounts.clear();
      }
      else {
        for(String k : ifdCounts.keySet())
        ifdCounts.put(k, 0);
      }
    }
  }

  // -- IFormatWriter API methods --

  /**
   * @see loci.formats.IFormatWriter#saveBytes(int, byte[], int, int, int, int)
   */
  @Override
  public void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    saveBytes(no, buf, null, x, y, w, h);
  }

  /**
   * @see loci.formats.IFormatWriter#saveBytes(int, byte[], int, int, int, int)
   */
  @Override
  public void saveBytes(int no, byte[] buf, IFD ifd, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    super.saveBytes(no, buf, ifd, x, y, w, h);

    int index = no;
    while (imageLocations[series][index] != null) {
      if (index < imageLocations[series].length - 1) {
        index++;
      }
      else {
        break;
      }
    }
    imageLocations[series][index] = currentId;
  }

  // -- FormatWriter API methods --

  /* @see FormatWriter#setId(String) */
  @Override
  public void setId(String id) throws FormatException, IOException {
    if (id.equals(currentId)) return;
    super.setId(id);
    if (imageLocations == null) {
      MetadataRetrieve r = getMetadataRetrieve();
      imageLocations = new String[r.getImageCount()][];
      for (int i=0; i<imageLocations.length; i++) {
        setSeries(i);
        imageLocations[i] = new String[planeCount()];
      }
      setSeries(0);
    }
  }

  // -- ODETiff-specific methods --
  public String getCompanion() {
    MetadataOptions options = getMetadataOptions();
    if (options instanceof DynamicMetadataOptions) {
      return ((DynamicMetadataOptions) options).get(COMPANION_KEY);
    }
    return null;
  }

  // -- Helper methods --

  /** Gets the UUID corresponding to the given filename. */
  private String getUUID(String filename) {
    String uuid = uuids.get(filename);
    if (uuid == null) {
      uuid = UUID.randomUUID().toString();
      uuids.put(filename, uuid);
    }
    return uuid;
  }

  private void setupServiceAndMetadata()
    throws DependencyException, ServiceException
  {
    // extract ODE-XML string from metadata object
    MetadataRetrieve retrieve = getMetadataRetrieve();

    ServiceFactory factory = new ServiceFactory();
    service = factory.getInstance(ODEXMLService.class);
    ODEXMLMetadata originalODEMeta = service.getODEMetadata(retrieve);
    originalODEMeta.resolveReferences();

    String odexml = service.getODEXML(originalODEMeta);
    odeMeta = service.createODEXMLMetadata(odexml);
  }

  private String insertWarningComment(String xml) {
    String prefix = xml.substring(0, xml.indexOf('>') + 1);
    String suffix = xml.substring(xml.indexOf('>') + 1);
    return prefix + WARNING_COMMENT + suffix;
  }

  private String getODEXML(String file) throws FormatException, IOException {
    // generate UUID and add to ODE element
    String uuid = "urn:uuid:" + getUUID(new Location(file).getName());
    odeMeta.setUUID(uuid);

    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odeMeta.getRoot();
    root.setCreator(FormatTools.CREATOR);

    String xml;
    try {
      xml = service.getODEXML(odeMeta);
    }
    catch (ServiceException se) {
      throw new FormatException(se);
    }
    return xml;
  }

  private String getBinaryOnlyODEXML(
      String file, String companion, String companionUUID) throws
        FormatException, IOException, DependencyException, ServiceException {
    ServiceFactory factory = new ServiceFactory();
    ODEXMLService service = factory.getInstance(ODEXMLService.class);
    ODEXMLMetadata meta = service.createODEXMLMetadata();
    String uuid = "urn:uuid:" + getUUID(new Location(file).getName());
    meta.setUUID(uuid);
    meta.setBinaryOnlyMetadataFile(new Location(companion).getName());
    meta.setBinaryOnlyUUID(companionUUID);
    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) meta.getRoot();
    root.setCreator(FormatTools.CREATOR);
    return service.getODEXML(meta);
  }

  private void saveComment(String file, String xml) throws IOException {
    if (out != null) out.close();
    out = new RandomAccessOutputStream(file);
    RandomAccessInputStream in = null;
    try {
      TiffSaver saver = new TiffSaver(out, file);
      saver.setBigTiff(isBigTiff);
      in = new RandomAccessInputStream(file);
      saver.overwriteLastIFDOffset(in);
      saver.overwriteComment(in, xml);
    }
    catch (FormatException exc) {
      IOException io = new IOException("Unable to append ODE-XML comment");
      io.initCause(exc);
      throw io;
    }
    finally {
      if (out != null) out.close();
      if (in != null) in.close();
    }
  }

  private void populateTiffData(ODEXMLMetadata odeMeta, int[] zct,
    int ifd, int series, int plane)
  {
    odeMeta.setTiffDataFirstZ(new NonNegativeInteger(zct[0]), series, plane);
    odeMeta.setTiffDataFirstC(new NonNegativeInteger(zct[1]), series, plane);
    odeMeta.setTiffDataFirstT(new NonNegativeInteger(zct[2]), series, plane);
    odeMeta.setTiffDataIFD(new NonNegativeInteger(ifd), series, plane);
    odeMeta.setTiffDataPlaneCount(new NonNegativeInteger(1), series, plane);
  }

  private void populateImage(ODEXMLMetadata odeMeta, int series) {
    String dimensionOrder = odeMeta.getPixelsDimensionOrder(series).toString();
    int sizeZ = odeMeta.getPixelsSizeZ(series).getValue().intValue();
    int sizeC = odeMeta.getPixelsSizeC(series).getValue().intValue();
    int sizeT = odeMeta.getPixelsSizeT(series).getValue().intValue();

    int imageCount = getPlaneCount();

    if (imageCount == 0) {
      odeMeta.setTiffDataPlaneCount(new NonNegativeInteger(0), series, 0);
      return;
    }

    PositiveInteger samplesPerPixel =
      new PositiveInteger((sizeZ * sizeC * sizeT) / imageCount);
    for (int c=0; c<odeMeta.getChannelCount(series); c++) {
      odeMeta.setChannelSamplesPerPixel(samplesPerPixel, series, c);
    }
    sizeC /= samplesPerPixel.getValue();

    int nextPlane = 0;
    for (int plane=0; plane<imageCount; plane++) {
      int[] zct = FormatTools.getZCTCoords(dimensionOrder,
        sizeZ, sizeC, sizeT, imageCount, plane);

      int planeIndex = plane;
      if (imageLocations[series].length < imageCount) {
        planeIndex /= (imageCount / imageLocations[series].length);
      }

      String filename = imageLocations[series][planeIndex];
      if (filename != null) {
        filename = new Location(filename).getName();

        Integer ifdIndex = ifdCounts.get(filename);
        int ifd = ifdIndex == null ? 0 : ifdIndex.intValue();

        odeMeta.setUUIDFileName(filename, series, nextPlane);
        String uuid = "urn:uuid:" + getUUID(filename);
        odeMeta.setUUIDValue(uuid, series, nextPlane);

        // fill in any non-default TiffData attributes
        populateTiffData(odeMeta, zct, ifd, series, nextPlane);
        ifdCounts.put(filename, ifd + 1);
        nextPlane++;
      }
    }
  }

  private int planeCount() {
    MetadataRetrieve r = getMetadataRetrieve();
    int z = r.getPixelsSizeZ(series).getValue().intValue();
    int t = r.getPixelsSizeT(series).getValue().intValue();
    int c = r.getChannelCount(series);
    String pixelType = r.getPixelsType(series).getValue();
    int bytes = FormatTools.getBytesPerPixel(pixelType);

    if (bytes > 1 && c == 1) {
      c = r.getChannelSamplesPerPixel(series, 0).getValue();
    }

    return z * c * t;
  }

}
