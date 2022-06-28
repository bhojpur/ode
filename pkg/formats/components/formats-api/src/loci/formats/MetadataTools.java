package loci.formats;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

import loci.common.DateTools;
import loci.common.Location;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.meta.IMetadata;
import loci.formats.meta.IPyramidStore;
import loci.formats.meta.MetadataRetrieve;
import loci.formats.meta.MetadataStore;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;
import ode.xml.model.enums.*;
import ode.xml.model.enums.handlers.*;
import ode.xml.model.primitives.NonNegativeInteger;
import ode.xml.model.primitives.PositiveInteger;
import ode.xml.model.primitives.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class for working with metadata objects,
 * including {@link MetadataStore}, {@link MetadataRetrieve},
 * and ODE-XML strings.
 * Most of the methods require the optional {@link loci.formats.ode}
 * package, and optional ode-xml.jar library, to be present at runtime.
 */
public final class MetadataTools {

  // -- Constants --

  private static final Logger LOGGER =
    LoggerFactory.getLogger(MetadataTools.class);

  // -- Static fields --

  private static boolean defaultDateEnabled = false;

  // -- Constructor --

  private MetadataTools() { }

  // -- Utility methods - ODE-XML --

  /**
   * Populates the Pixels element of the given metadata store, using core
   * metadata from the given reader.
   *
   * Delegate to
   * {@link #populatePixels(MetadataStore, IFormatReader, boolean, boolean)}
   * with {@code doPlane} set to {@code false} and {@code doImageName} set to
   * {@code true}.
   *
   * @param store The metadata store whose Pixels should be populated
   * @param r     The format reader whose core metadata should be used
   */
  public static void populatePixels(MetadataStore store, IFormatReader r) {
    populatePixels(store, r, false, true);
  }

  /**
   * Populates the Pixels element of the given metadata store, using core
   * metadata from the given reader.  If the {@code doPlane} flag is set,
   * then the Plane elements will be populated as well.
   *
   * Delegates to
   * {@link #populatePixels(MetadataStore, IFormatReader, boolean, boolean)}
   * with {@code doImageName} set to {@code true}.
   *
   * @param store   The metadata store whose Pixels should be populated
   * @param r       The format reader whose core metadata should be used
   * @param doPlane Specifies whether Plane elements should be populated
   */
  public static void populatePixels(MetadataStore store, IFormatReader r,
    boolean doPlane)
  {
    populatePixels(store, r, doPlane, true);
  }

  /**
   * Populates the Pixels element of the given metadata store, using core
   * metadata from the given reader.  If the {@code doPlane} flag is set,
   * then the Plane elements will be populated as well. If the
   * {@code doImageName} flag is set, then the image name will be populated as
   * well.
   *
   * @param store       The metadata store whose Pixels should be populated
   * @param r           The format reader whose core metadata should be used
   * @param doPlane     Specifies whether Plane elements should be populated
   * @param doImageName Specifies whether the Image name should be populated
   */
  public static void populatePixels(MetadataStore store, IFormatReader r,
    boolean doPlane, boolean doImageName)
  {
    if (store == null || r == null) return;
    int oldSeries = r.getSeries();

    for (int i=0; i<r.getSeriesCount(); i++) {
      r.setSeries(i);

      String imageName = null;
      if (doImageName) {
        Location f = new Location(r.getCurrentFile());
        imageName = f.getName();

        if (r.getSeriesCount() > 1) {
          imageName += " #" + (i + 1);
        }
      }
      String pixelType = FormatTools.getPixelTypeString(r.getPixelType());

      populateMetadata(store, r.getCurrentFile(), i, imageName,
        r.isLittleEndian(), r.getDimensionOrder(), pixelType, r.getSizeX(),
        r.getSizeY(), r.getSizeZ(), r.getSizeC(), r.getSizeT(),
        r.getRGBChannelCount());

      store.setPixelsInterleaved(r.isInterleaved(), i);
      store.setPixelsSignificantBits(
        new PositiveInteger(r.getBitsPerPixel()), i);

      if (store instanceof IPyramidStore) {
        for (int res=1; res<r.getResolutionCount(); res++) {
          r.setResolution(res);
          ((IPyramidStore) store).setResolutionSizeX(
            new PositiveInteger(r.getSizeX()), i, res);
          ((IPyramidStore) store).setResolutionSizeY(
            new PositiveInteger(r.getSizeY()), i, res);
        }
        r.setResolution(0);
      }

      try {
        ODEXMLService service =
          new ServiceFactory().getInstance(ODEXMLService.class);
        if (service.isODEXMLRoot(store.getRoot())) {
          MetadataStore baseStore = r.getMetadataStore();
          if (service.isODEXMLMetadata(baseStore)) {
            ODEXMLMetadata odeMeta;
            try {
              odeMeta = service.getODEMetadata(service.asRetrieve(baseStore));
              if (odeMeta.getTiffDataCount(i) == 0 && odeMeta.getPixelsBinDataCount(i) == 0) {
                service.addMetadataOnly(odeMeta, i, i == 0);
              }
            }
            catch (ServiceException e) {
              LOGGER.warn("Failed to add MetadataOnly", e);
            }
          }
        }
      }
      catch (DependencyException exc) {
        LOGGER.warn("Failed to add MetadataOnly", exc);
      }

      if (doPlane) {
        for (int q=0; q<r.getImageCount(); q++) {
          int[] coords = r.getZCTCoords(q);
          store.setPlaneTheZ(new NonNegativeInteger(coords[0]), i, q);
          store.setPlaneTheC(new NonNegativeInteger(coords[1]), i, q);
          store.setPlaneTheT(new NonNegativeInteger(coords[2]), i, q);
        }
      }
    }
    r.setSeries(oldSeries);
  }

  /**
   * Populates the given {@link MetadataStore}, for the specified series, using
   * the values from the provided {@link CoreMetadata}.
   * <p>
   * After calling this method, the metadata store will be sufficiently
   * populated for use with an {@link IFormatWriter} (assuming it is also a
   * {@link MetadataRetrieve}).
   * </p>
   */
  public static void populateMetadata(MetadataStore store, int series,
    String imageName, CoreMetadata coreMeta)
  {
    final String pixelType = FormatTools.getPixelTypeString(coreMeta.pixelType);
    final int effSizeC = coreMeta.imageCount / coreMeta.sizeZ / coreMeta.sizeT;
    final int samplesPerPixel = coreMeta.sizeC / effSizeC;
    populateMetadata(store, null, series, imageName, coreMeta.littleEndian,
      coreMeta.dimensionOrder, pixelType, coreMeta.sizeX, coreMeta.sizeY,
      coreMeta.sizeZ, coreMeta.sizeC, coreMeta.sizeT, samplesPerPixel);
  }

  /**
   * Populates the given {@link MetadataStore}, for the specified series, using
   * the provided values.
   * <p>
   * After calling this method, the metadata store will be sufficiently
   * populated for use with an {@link IFormatWriter} (assuming it is also a
   * {@link MetadataRetrieve}).
   * </p>
   */
  public static void populateMetadata(MetadataStore store, int series,
    String imageName, boolean littleEndian, String dimensionOrder,
    String pixelType, int sizeX, int sizeY, int sizeZ, int sizeC, int sizeT,
    int samplesPerPixel)
  {
    populateMetadata(store, null, series, imageName, littleEndian,
      dimensionOrder, pixelType, sizeX, sizeY, sizeZ, sizeC, sizeT,
      samplesPerPixel);
  }

  /**
   * Populates the given {@link MetadataStore}, for the specified series, using
   * the provided values.
   * <p>
   * After calling this method, the metadata store will be sufficiently
   * populated for use with an {@link IFormatWriter} (assuming it is also a
   * {@link MetadataRetrieve}).
   * </p>
   */
  public static void populateMetadata(MetadataStore store, String file,
    int series, String imageName, boolean littleEndian, String dimensionOrder,
    String pixelType, int sizeX, int sizeY, int sizeZ, int sizeC, int sizeT,
    int samplesPerPixel)
  {
    store.setImageID(createLSID("Image", series), series);
    setDefaultCreationDate(store, file, series);
    if (imageName != null) store.setImageName(imageName, series);
    populatePixelsOnly(store, series, littleEndian, dimensionOrder, pixelType,
      sizeX, sizeY, sizeZ, sizeC, sizeT, samplesPerPixel);
  }

  public static void populatePixelsOnly(MetadataStore store, IFormatReader r) {
    int oldSeries = r.getSeries();
    for (int i=0; i<r.getSeriesCount(); i++) {
      r.setSeries(i);

      String pixelType = FormatTools.getPixelTypeString(r.getPixelType());

      populatePixelsOnly(store, i, r.isLittleEndian(), r.getDimensionOrder(),
        pixelType, r.getSizeX(), r.getSizeY(), r.getSizeZ(), r.getSizeC(),
        r.getSizeT(), r.getRGBChannelCount());

      if (store instanceof IPyramidStore) {
        for (int res=1; res<r.getResolutionCount(); res++) {
          r.setResolution(res);
          ((IPyramidStore) store).setResolutionSizeX(
            new PositiveInteger(r.getSizeX()), i, res);
          ((IPyramidStore) store).setResolutionSizeY(
            new PositiveInteger(r.getSizeY()), i, res);
        }
        r.setResolution(0);
      }
    }
    r.setSeries(oldSeries);
  }

  public static void populatePixelsOnly(MetadataStore store, int series,
    boolean littleEndian, String dimensionOrder, String pixelType, int sizeX,
    int sizeY, int sizeZ, int sizeC, int sizeT, int samplesPerPixel)
  {
    store.setPixelsID(createLSID("Pixels", series), series);
    store.setPixelsBigEndian(!littleEndian, series);
    try {
      store.setPixelsDimensionOrder(
        DimensionOrder.fromString(dimensionOrder), series);
    }
    catch (EnumerationException e) {
      LOGGER.warn("Invalid dimension order: " + dimensionOrder, e);
    }
    try {
      store.setPixelsType(PixelType.fromString(pixelType), series);
    }
    catch (EnumerationException e) {
      LOGGER.warn("Invalid pixel type: " + pixelType, e);
    }
    store.setPixelsSizeX(new PositiveInteger(sizeX), series);
    store.setPixelsSizeY(new PositiveInteger(sizeY), series);
    store.setPixelsSizeZ(new PositiveInteger(sizeZ), series);
    store.setPixelsSizeC(new PositiveInteger(sizeC), series);
    store.setPixelsSizeT(new PositiveInteger(sizeT), series);
    int effSizeC = sizeC / samplesPerPixel;
    for (int i=0; i<effSizeC; i++) {
      store.setChannelID(createLSID("Channel", series, i),
        series, i);
      store.setChannelSamplesPerPixel(new PositiveInteger(samplesPerPixel),
        series, i);
    }
  }

  /**
   * Constructs an LSID, given the object type and indices.
   * For example, if the arguments are "Detector", 1, and 0, the LSID will
   * be "Detector:1:0".
   */
  public static String createLSID(String type, int... indices) {
    StringBuffer lsid = new StringBuffer(type);
    for (int index : indices) {
      lsid.append(":");
      lsid.append(index);
    }
    return lsid.toString();
  }

  /**
   * Checks whether the given metadata object has the minimum metadata
   * populated to successfully describe an Image.
   *
   * @throws FormatException if there is a missing metadata field,
   *   or the metadata object is uninitialized
   */
  public static void verifyMinimumPopulated(MetadataRetrieve src)
    throws FormatException
  {
    verifyMinimumPopulated(src, 0);
  }

  /**
   * Checks whether the given metadata object has the minimum metadata
   * populated to successfully describe the nth Image.
   *
   * @throws FormatException if there is a missing metadata field,
   *   or the metadata object is uninitialized
   */
  public static void verifyMinimumPopulated(MetadataRetrieve src, int n)
    throws FormatException
  {
    if (src == null) {
      throw new FormatException("Metadata object is null; " +
          "call IFormatWriter.setMetadataRetrieve() first");
    }
    if (src instanceof MetadataStore
        && ((MetadataStore) src).getRoot() == null) {
      throw new FormatException("Metadata object has null root; " +
        "call IMetadata.createRoot() first");
    }
    if (src.getImageID(n) == null) {
      throw new FormatException("Image ID #" + n + " is null");
    }
    if (src.getPixelsID(n) == null) {
      throw new FormatException("Pixels ID #" + n + " is null");
    }
    for (int i=0; i<src.getChannelCount(n); i++) {
      if (src.getChannelID(n, i) == null) {
        throw new FormatException("Channel ID #" + i + " in Image #" + n +
          " is null");
      }
    }
    if (src.getPixelsBigEndian(n) == null)
    {
      if (src.getPixelsBinDataCount(n) == 0 || src.getPixelsBinDataBigEndian(n, 0) == null) {
        throw new FormatException("BigEndian #" + n + " is null");
      }
    }
    if (src.getPixelsDimensionOrder(n) == null) {
      throw new FormatException("DimensionOrder #" + n + " is null");
    }
    if (src.getPixelsType(n) == null) {
      throw new FormatException("PixelType #" + n + " is null");
    }
    if (src.getPixelsSizeC(n) == null) {
      throw new FormatException("SizeC #" + n + " is null");
    }
    if (src.getPixelsSizeT(n) == null) {
      throw new FormatException("SizeT #" + n + " is null");
    }
    if (src.getPixelsSizeX(n) == null) {
      throw new FormatException("SizeX #" + n + " is null");
    }
    if (src.getPixelsSizeY(n) == null) {
      throw new FormatException("SizeY #" + n + " is null");
    }
    if (src.getPixelsSizeZ(n) == null) {
      throw new FormatException("SizeZ #" + n + " is null");
    }
  }

  /**
   * Disables the setting of a default creation date.
   *
   * By default, missing creation dates will be replaced with the corresponding
   * file's last modification date, or the current time if the modification
   * date is not available.
   *
   * Calling this method with the 'enabled' parameter set to 'false' causes
   * missing creation dates to be left as null.
   *
   * @param enabled See above.
   * @see #setDefaultCreationDate(MetadataStore, String, int)
   */
  public static void setDefaultDateEnabled(boolean enabled) {
    defaultDateEnabled = enabled;
  }

  /**
   * Sets a default creation date.  If the named file exists, then the creation
   * date is set to the file's last modification date.  Otherwise, it is set
   * to the current date.
   *
   * @see #setDefaultDateEnabled(boolean)
   */
  public static void setDefaultCreationDate(MetadataStore store, String id,
    int series)
  {
    if (!defaultDateEnabled) {
      return;
    }
    Location file = id == null ? null : new Location(id).getAbsoluteFile();
    long time = System.currentTimeMillis();
    if (file != null && file.exists()) time = file.lastModified();
    store.setImageAcquisitionDate(new Timestamp(DateTools.convertDate(
        time, DateTools.UNIX)), series);
  }

  /**
   * Adjusts the given dimension order as needed so that it contains exactly
   * one of each of the following characters: 'X', 'Y', 'Z', 'C', 'T'.
   */
  public static String makeSaneDimensionOrder(String dimensionOrder) {
    String order = dimensionOrder.toUpperCase();
    order = order.replaceAll("[^XYZCT]", "");
    String[] axes = new String[] {"X", "Y", "C", "Z", "T"};
    for (String axis : axes) {
      if (order.indexOf(axis) == -1) order += axis;
      while (order.indexOf(axis) != order.lastIndexOf(axis)) {
        order = order.replaceFirst(axis, "");
      }
    }
    return order;
  }

  // -- Utility methods - original metadata --

  /** Gets a sorted list of keys from the given hashtable. */
  public static String[] keys(Hashtable<String, Object> meta) {
    String[] keys = new String[meta.size()];
    meta.keySet().toArray(keys);
    Arrays.sort(keys);
    return keys;
  }

  /**
   * Merges the given lists of metadata, prepending the
   * specified prefix for the destination keys.
   */
  public static void merge(Map<String, Object> src, Map<String, Object> dest,
    String prefix)
  {
    for (String key : src.keySet()) {
      dest.put(prefix + key, src.get(key));
    }
  }

  /**
   * Creates an ODE-XML metadata object using reflection, to avoid
   * direct dependencies on the optional {@link loci.formats.ode} package.
   * @return A new instance of {@link loci.formats.ode.AbstractODEXMLMetadata},
   *   or null if one cannot be created.
   */
  public static IMetadata createODEXMLMetadata() {
    try {
      final ODEXMLService service =
        new ServiceFactory().getInstance(ODEXMLService.class);
      if (service == null) return null;
      return service.createODEXMLMetadata();
    }
    catch (DependencyException exc) {
      return null;
    }
    catch (ServiceException exc) {
      return null;
    }
  }

  // -- Metadata enumeration convenience methods --

  /**
   * Retrieves an {@link ode.xml.model.enums.AcquisitionMode} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static AcquisitionMode getAcquisitionMode(String value)
    throws FormatException
  {
    AcquisitionModeEnumHandler handler = new AcquisitionModeEnumHandler();
    try {
      return (AcquisitionMode) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("AcquisitionMode creation failed", e);
    }
  }

  /**
   * Retrieves an {@link ode.xml.model.enums.ArcType} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static ArcType getArcType(String value) throws FormatException {
    ArcTypeEnumHandler handler = new ArcTypeEnumHandler();
    try {
      return (ArcType) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("ArcType creation failed", e);
    }
  }

  /**
   * Retrieves an {@link ode.xml.model.enums.Binning} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static Binning getBinning(String value) throws FormatException {
    BinningEnumHandler handler = new BinningEnumHandler();
    try {
      return (Binning) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("Binning creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.Compression} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static Compression getCompression(String value) throws FormatException {
    CompressionEnumHandler handler = new CompressionEnumHandler();
    try {
      return (Compression) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("Compression creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.ContrastMethod} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static ContrastMethod getContrastMethod(String value)
    throws FormatException
  {
    ContrastMethodEnumHandler handler = new ContrastMethodEnumHandler();
    try {
      return (ContrastMethod) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("ContrastMethod creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.Correction} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static Correction getCorrection(String value) throws FormatException {
    CorrectionEnumHandler handler = new CorrectionEnumHandler();
    try {
      return (Correction) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("Correction creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.DetectorType} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static DetectorType getDetectorType(String value) throws FormatException {
    DetectorTypeEnumHandler handler = new DetectorTypeEnumHandler();
    try {
      return (DetectorType) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("DetectorType creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.DimensionOrder} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static DimensionOrder getDimensionOrder(String value)
    throws FormatException
  {
    DimensionOrderEnumHandler handler = new DimensionOrderEnumHandler();
    try {
      return (DimensionOrder) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("DimensionOrder creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.ExperimentType} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static ExperimentType getExperimentType(String value)
    throws FormatException
  {
    ExperimentTypeEnumHandler handler = new ExperimentTypeEnumHandler();
    try {
      return (ExperimentType) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("ExperimentType creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.FilamentType} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static FilamentType getFilamentType(String value) throws FormatException {
    FilamentTypeEnumHandler handler = new FilamentTypeEnumHandler();
    try {
      return (FilamentType) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("FilamentType creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.FillRule} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static FillRule getFillRule(String value) throws FormatException {
    FillRuleEnumHandler handler = new FillRuleEnumHandler();
    try {
      return (FillRule) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("FillRule creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.FilterType} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static FilterType getFilterType(String value) throws FormatException {
    FilterTypeEnumHandler handler = new FilterTypeEnumHandler();
    try {
      return (FilterType) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("FilterType creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.FontFamily} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static FontFamily getFontFamily(String value) throws FormatException {
    FontFamilyEnumHandler handler = new FontFamilyEnumHandler();
    try {
      return (FontFamily) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("FontFamily creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.FontStyle} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static FontStyle getFontStyle(String value) throws FormatException {
    FontStyleEnumHandler handler = new FontStyleEnumHandler();
    try {
      return (FontStyle) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("FontStyle creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.IlluminationType} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static IlluminationType getIlluminationType(String value)
    throws FormatException
  {
    IlluminationTypeEnumHandler handler = new IlluminationTypeEnumHandler();
    try {
      return (IlluminationType) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("IlluminationType creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.Immersion} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static Immersion getImmersion(String value) throws FormatException {
    ImmersionEnumHandler handler = new ImmersionEnumHandler();
    try {
      return (Immersion) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("Immersion creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.LaserMedium} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static LaserMedium getLaserMedium(String value) throws FormatException {
    LaserMediumEnumHandler handler = new LaserMediumEnumHandler();
    try {
      return (LaserMedium) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("LaserMedium creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.LaserType} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static LaserType getLaserType(String value) throws FormatException {
    LaserTypeEnumHandler handler = new LaserTypeEnumHandler();
    try {
      return (LaserType) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("LaserType creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.Marker} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static Marker getMarker(String value) throws FormatException {
    MarkerEnumHandler handler = new MarkerEnumHandler();
    try {
      return (Marker) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("Marker creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.Medium} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static Medium getMedium(String value) throws FormatException {
    MediumEnumHandler handler = new MediumEnumHandler();
    try {
      return (Medium) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("Medium creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.MicrobeamManipulationType}
   * enumeration value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static MicrobeamManipulationType getMicrobeamManipulationType(String value)
    throws FormatException
  {
    MicrobeamManipulationTypeEnumHandler handler =
      new MicrobeamManipulationTypeEnumHandler();
    try {
      return (MicrobeamManipulationType) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("MicrobeamManipulationType creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.MicroscopeType} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static MicroscopeType getMicroscopeType(String value)
    throws FormatException
  {
    MicroscopeTypeEnumHandler handler = new MicroscopeTypeEnumHandler();
    try {
      return (MicroscopeType) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("MicroscopeType creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.NamingConvention} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static NamingConvention getNamingConvention(String value)
    throws FormatException
  {
    NamingConventionEnumHandler handler = new NamingConventionEnumHandler();
    try {
      return (NamingConvention) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("NamingConvention creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.PixelType} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static PixelType getPixelType(String value) throws FormatException {
    PixelTypeEnumHandler handler = new PixelTypeEnumHandler();
    try {
      return (PixelType) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("PixelType creation failed", e);
    }
  }
  /**
   * Retrieves an {@link ode.xml.model.enums.Pulse} enumeration
   * value for the given String.
   *
   * @throws ode.xml.model.enums.EnumerationException if an appropriate
   *  enumeration value is not found.
   */
  public static Pulse getPulse(String value) throws FormatException {
    PulseEnumHandler handler = new PulseEnumHandler();
    try {
      return (Pulse) handler.getEnumeration(value);
    }
    catch (EnumerationException e) {
      throw new FormatException("Pulse creation failed", e);
    }
  }

}
