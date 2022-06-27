/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.in;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import loci.common.DataTools;
import loci.common.Location;
import loci.common.RandomAccessInputStream;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.CoreMetadata;
import loci.formats.CoreMetadataList;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.MetadataTools;
import loci.formats.MissingLibraryException;
import loci.formats.Modulo;
import loci.formats.SubResolutionFormatReader;
import loci.formats.meta.MetadataStore;
import loci.formats.ode.ODEPyramidStore;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;
import loci.formats.services.ODEXMLServiceImpl;
import loci.formats.tiff.IFD;
import loci.formats.tiff.IFDList;
import loci.formats.tiff.PhotoInterp;
import loci.formats.tiff.TiffIFDEntry;
import loci.formats.tiff.TiffParser;

import ode.xml.meta.ODEXMLMetadataRoot;
import ode.xml.model.Channel;
import ode.xml.model.Image;
import ode.xml.model.MapPair;
import ode.xml.model.Pixels;
import ode.xml.model.Plane;
import ode.xml.model.primitives.NonNegativeInteger;
import ode.xml.model.primitives.PositiveInteger;
import ode.xml.model.primitives.Timestamp;

/**
 * ODETiffReader is the file format reader for
 * <a href="https://docs.bhojpur.net/latest/ode-model/ode-tiff/">ODE-TIFF</a>
 * files.
 */
public class ODETiffReader extends SubResolutionFormatReader {

  public static final String[] ODE_TIFF_SUFFIXES =
    {"ode.tiff", "ode.tif", "ode.tf2", "ode.tf8", "ode.btf", "companion.ode"};

  public static final String FAIL_ON_MISSING_KEY = "odetiff.fail_on_missing_tiff";
  public static final boolean FAIL_ON_MISSING_DEFAULT = true;

  // -- Fields --

  /** Mapping from series and plane numbers to files and IFD entries. */
  protected ODETiffPlane[][] info; // dimensioned [numSeries][numPlanes]

  /** List of used files. */
  protected String[] used;

  private int lastPlane = 0;
  private boolean hasSPW;

  private ODEXMLService service;
  private transient ODEXMLMetadata meta;
  private String metaFile;

  private String metadataFile;

  // -- Constructor --

  /** Constructs a new ODE-TIFF reader. */
  public ODETiffReader() {
    super("ODE-TIFF", ODE_TIFF_SUFFIXES);
    suffixNecessary = false;
    suffixSufficient = false;
    domains = FormatTools.NON_GRAPHICS_DOMAINS;
    hasCompanionFiles = true;
    datasetDescription = "One or more .ode.tiff files";
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.FormatReader#getAvailableOptions() */
  @Override
  protected ArrayList<String> getAvailableOptions() {
    ArrayList<String> optionsList = super.getAvailableOptions();
    optionsList.add(FAIL_ON_MISSING_KEY);
    return optionsList;
  }

  /* @see loci.formats.SubResolutionFormatReader#isSingleFile(String) */
  @Override
  public boolean isSingleFile(String id) throws FormatException, IOException {
    // companion files in a binary-only dataset should always have additional files
    if (checkSuffix(id, "companion.ode")) {
      return false;
    }

    // parse and populate ODE-XML metadata
    String fileName = new Location(id).getAbsoluteFile().getAbsolutePath();
    IFD ifd = null;
    long[] ifdOffsets = null;
    try (RandomAccessInputStream ras = new RandomAccessInputStream(fileName, 16)) {
        TiffParser tp = new TiffParser(ras);
        ifd = tp.getFirstIFD();
        ifdOffsets = tp.getIFDOffsets();
    }
    String xml = ifd.getComment();

    if (service == null) setupService();
    ODEXMLMetadata meta;
    try {
      meta = service.createODEXMLMetadata(xml);
      metaFile = new Location(id).getAbsolutePath();
    }
    catch (ServiceException se) {
      throw new FormatException(se);
    }

    if (meta.getRoot() == null) {
      throw new FormatException("Could not parse ODE-XML from TIFF comment");
    }

    int nImages = 0;
    for (int i=0; i<meta.getImageCount(); i++) {
      int nChannels = meta.getChannelCount(i);
      if (nChannels == 0) nChannels = 1;
      int z = meta.getPixelsSizeZ(i).getValue();
      int t = meta.getPixelsSizeT(i).getValue();
      nImages += z * t * nChannels;
    }
    return nImages > 0 && nImages <= ifdOffsets.length;
  }

  /* @see loci.formats.SubResolutionFormatReader#isThisType(String, boolean) */
  @Override
  public boolean isThisType(String name, boolean open) {
    if (checkSuffix(name, "companion.ode")) {
      // force the reader to pick up binary-only companion files
      return true;
    }
    metaFile = new Location(name).getAbsolutePath();
    boolean valid = super.isThisType(name, open);
    if (metadataFile != null) {
      // this is a binary-only file
      // overwrite XML with what is in the companion ODE-XML file
      String dir = new File(metaFile).getParent();
      Location path = new Location(dir, metadataFile);
      LOGGER.debug("Checking metadata file {}", path);
      if (!path.exists()) return false;
      metadataFile = path.getAbsolutePath();

      try {
        String xml = readMetadataFile();
        service.createODEXMLMetadata(xml);
      } catch (ServiceException se) {
        LOGGER.debug("ODE-XML parsing failed", se);
        return false;
      } catch (IOException | NullPointerException e) {
        return false;
      }
    }
    return valid;
  }

  /* @see loci.formats.SubResolutionFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    TiffParser tp = new TiffParser(stream);
    tp.setDoCaching(false);
    boolean validHeader = tp.isValidHeader();
    if (!validHeader) return false;
    // look for ODE-XML in first IFD's comment
    IFD ifd = tp.getFirstIFD();
    if (ifd == null) return false;
    Object description = ifd.get(IFD.IMAGE_DESCRIPTION);
    if (description == null) {
      return false;
    }
    String comment = null;
    if (description instanceof TiffIFDEntry) {
      Object value = tp.getIFDValue((TiffIFDEntry) description);
      if (value != null) {
        comment = value.toString();
      }
    }
    else if (description instanceof String) {
      comment = (String) description;
    }
    if (comment == null || comment.trim().length() == 0) return false;

    comment = comment.trim();

    // do a basic sanity check before attempting to parse the comment as XML
    // the parsing step is a bit slow, so there is no sense in trying unless
    // we are reasonably sure that the comment contains XML
    if (!comment.startsWith("<") || !comment.endsWith(">")) {
      return false;
    }

    try {
      if (service == null) setupService();
      meta = service.createODEXMLMetadata(comment);

      try {
        metadataFile = meta.getBinaryOnlyMetadataFile();
        // check the suffix to make sure that the MetadataFile is not
        // referencing the current ODE-TIFF
        if (metadataFile != null)
        {
          return true;
        }
      }
      catch (NullPointerException e) {
      }

      for (int i=0; i<meta.getImageCount(); i++) {
        meta.setPixelsBigEndian(Boolean.TRUE, i);
        if (meta.getPixelsBinDataCount(i) > 0) {
          for (int j=0; j<meta.getPixelsBinDataCount(i); j++) {
            meta.setPixelsBinDataBigEndian(Boolean.TRUE, i, j);
          }
        }
        MetadataTools.verifyMinimumPopulated(meta, i);
      }
      return meta.getImageCount() > 0;
    }
    catch (ServiceException | NullPointerException | FormatException | IndexOutOfBoundsException se) {
      LOGGER.debug("ODE-XML parsing failed", se);
    }
    return false;
  }

  /* @see loci.formats.SubResolutionFormatReader#getDomains() */
  @Override
  public String[] getDomains() {
    FormatTools.assertId(currentId, true, 1);
    return hasSPW ? new String[] {FormatTools.HCS_DOMAIN} :
      FormatTools.NON_SPECIAL_DOMAINS;
  }

  @Override
  public void setSeries(int series) {
    super.setSeries(series);

    // don't keep inactive series' files open
    for (int i=0; i<info.length; i++) {
      if (i != series && info[i] != null) {
        for (ODETiffPlane p : info[i]) {
          if (p != null && p.reader != null) {
            try {
              p.reader.close();
            }
            catch (IOException e) {
              LOGGER.warn("Could not close " + p.id, e);
            }
          }
        }
      }
    }
  }

  /* @see loci.formats.SubResolutionFormatReader#get8BitLookupTable() */
  @Override
  public byte[][] get8BitLookupTable() throws FormatException, IOException {
    if (info[series][lastPlane] == null ||
      info[series][lastPlane].reader == null ||
      info[series][lastPlane].id == null)
    {
      return null;
    }
    initializeReader(info[series][lastPlane].reader, info[series][lastPlane].id);
    return info[series][lastPlane].reader.get8BitLookupTable();
  }

  /* @see loci.formats.SubResolutionFormatReader#get16BitLookupTable() */
  @Override
  public short[][] get16BitLookupTable() throws FormatException, IOException {
    if (info[series][lastPlane] == null ||
      info[series][lastPlane].reader == null ||
      info[series][lastPlane].id == null)
    {
      return null;
    }
    initializeReader(info[series][lastPlane].reader, info[series][lastPlane].id);
    return info[series][lastPlane].reader.get16BitLookupTable();
  }

  /* @see loci.formats.SubResolutionFormatReader#reopenFile() */
  @Override
  public void reopenFile() throws IOException {
    super.reopenFile();
    for (int s=0; s<info.length; s++) {
      for (int q=0; q<info[s].length; q++) {
        // only reopen readers that had previously been initialized
        if (info[s][q] != null && info[s][q].reader != null &&
          info[s][q].reader.getCurrentFile() != null)
        {
          info[s][q].reader.reopenFile();
        }
      }
    }
  }

  /*
   * @see loci.formats.SubResolutionFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);
    lastPlane = no;
    int i = info[series][no].ifd;

    if (!info[series][no].exists ||
      info[series][no].reader == null ||
      info[series][no].id == null)
    {
      Arrays.fill(buf, (byte) 0);
      return buf;
    }

    MinimalTiffReader r = (MinimalTiffReader) info[series][no].reader;
    if (r.getCurrentFile() == null) {
      initializeReader(r, info[series][no].id);
    }
    r.lastPlane = i;
    IFDList ifdList = r.getIFDs();
    if (i >= ifdList.size()) {
      LOGGER.warn("Error untangling IFDs; the ODE-TIFF file may be malformed (IFD #{} missing).", i);
      return buf;
    }
    IFD ifd = ifdList.get(i);
    try (RandomAccessInputStream s = new RandomAccessInputStream(info[series][no].id, 16)) {
      TiffParser p = new TiffParser(s);
      if (resolution > 0) {
        // read the required SubIFD, but don't attempt to read the ImageDescription
        // the ImageDescription will be completely ignored anyway
        // it may be quite large (> 10 MB) in which case this has a significant
        // impact on read time and memory usage
        p.setDoCaching(false);
        long offset = ifd.getIFDLongArray(IFD.SUB_IFD)[((ODETiffCoreMetadata)core.get(series, resolution)).subresolutionOffset];
        ifd = p.getIFD(offset);
        ifd.remove(IFD.IMAGE_DESCRIPTION);
        p.fillInIFD(ifd);
      }
      p.getSamples(ifd, buf, x, y, w, h);
    }

    // reasonably safe to close the reader if the entire plane or
    // lower-right-most tile from a single plane file has been read
    if (r.getImageCount() == 1 && w + x == getSizeX() && h + y == getSizeY()) {
      r.close();
    }
    return buf;
  }

  /* @see loci.formats.SubResolutionFormatReader#getSeriesUsedFiles(boolean) */
  @Override
  public String[] getSeriesUsedFiles(boolean noPixels) {
    FormatTools.assertId(currentId, true, 1);
    if (noPixels) return null;
    final List<String> usedFiles = new ArrayList<>();
    if (metadataFile != null) {
      usedFiles.add(metadataFile);
    }
    if (info != null && info[series] != null) {
      for (int i=0; i<info[series].length; i++) {
        if (info[series] != null && info[series][i] != null &&
          info[series][i].id != null &&
          !usedFiles.contains(info[series][i].id))
        {
          usedFiles.add(info[series][i].id);
        }
      }
    }
    return usedFiles.toArray(new String[usedFiles.size()]);
  }

  /* @see loci.formats.SubResolutionFormatReader#fileGroupOption() */
  @Override
  public int fileGroupOption(String id) {
    try {
      boolean single = isSingleFile(id);
      return single ? FormatTools.CAN_GROUP : FormatTools.MUST_GROUP;
    }
    catch (FormatException | IOException e) {
      LOGGER.debug("", e);
    }
    return FormatTools.CAN_GROUP;
  }

  /* @see loci.formats.SubResolutionFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (info != null) {
      for (ODETiffPlane[] dimension : info) {
        if (dimension == null) continue;
        for (ODETiffPlane plane : dimension) {
          if (plane != null && plane.reader != null) {
            try {
              plane.reader.close();
            }
            catch (Exception e) {
              LOGGER.error("Plane closure failure!", e);
            }
          }
        }
      }
    }
    if (!fileOnly) {
      info = null;
      used = null;
      lastPlane = 0;
      metadataFile = null;
    }
  }

  /* @see loci.formats.SubResolutionFormatReader#getOptimalTileWidth() */
  @Override
  public int getOptimalTileWidth() {
    FormatTools.assertId(currentId, true, 1);
    return ((ODETiffCoreMetadata) getCurrentCore()).tileWidth;
  }

  /* @see loci.formats.SubResolutionFormatReader#getOptimalTileHeight() */
  @Override
  public int getOptimalTileHeight() {
    FormatTools.assertId(currentId, true, 1);
    return ((ODETiffCoreMetadata) getCurrentCore()).tileHeight;
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.SubResolutionFormatReader#getRequiredDirectories(String[]) */
  @Override
  public int getRequiredDirectories(String[] files)
          throws FormatException, IOException
  {
    return FormatTools.getRequiredDirectories(files);
  }

  /* @see loci.formats.SubResolutionFormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    // normalize file name
    super.initFile(normalizeFilename(null, id));
    id = currentId;
    String dir = new File(id).getParent();

    // parse and populate ODE-XML metadata
    String fileName = new Location(id).getAbsoluteFile().getAbsolutePath();
    if (!new File(fileName).exists()) {
      fileName = currentId;
    }
    String xml;
    IFD firstIFD = null;

    boolean companion = false;
    if (checkSuffix(fileName, "companion.ode")) {
      xml = DataTools.readFile(fileName);
      companion = true;
    }
    else {
      firstIFD = getFirstIFD(fileName);
      xml = firstIFD.getComment();
    }

    if (service == null) setupService();
    try {
      if (meta == null || !metaFile.equals(currentId)) {
        meta = service.createODEXMLMetadata(xml);
        metaFile = currentId;
      }
      if (companion) {
        String firstTIFF = meta.getUUIDFileName(0, 0);
        firstIFD = getFirstIFD(new Location(dir, firstTIFF).getAbsolutePath());
        metadataFile = fileName;
      }
    }
    catch (ServiceException se) {
      throw new FormatException(se);
    }

    String metadataPath = null;
    if (metadataFile == null) {
      try {
        metadataPath = meta.getBinaryOnlyMetadataFile();
      }
      catch (NullPointerException e) {
      }
    }

    if (metadataPath != null) {
      // this is a binary-only file
      // overwrite XML with what is in the companion ODE-XML file
      Location path = new Location(dir, metadataPath);
      if (path.exists()) {
        // Since metatadataPath can be relative, use getCanonicalPath()
        metadataFile = path.getCanonicalPath();
        xml = readMetadataFile();

        try {
          meta = service.createODEXMLMetadata(xml);
          // Compute all paths relative to the directory of the metadata file
          dir = path.getParentFile().getCanonicalPath();
          // Set the current ID to the metadata file
          currentId = metadataFile;
        }
        catch (ServiceException se) {
          throw new FormatException(se);
        }
        catch (NullPointerException e) {
          metadataFile = null;
          metadataPath = null;
        }
      }
    }

    hasSPW = meta.getPlateCount() > 0;

    for (int i=0; i<meta.getImageCount(); i++) {
      int sizeC = meta.getPixelsSizeC(i).getValue();
      service.removeChannels(meta, i, sizeC);
    }

    // if flattened resolutions are requested, remove resolution annotations
    // otherwise, MetadataStore and reader metadata will be inconsistent
    if (hasFlattenedResolutions()) {
      try {
        for (int i=0; i<meta.getMapAnnotationCount(); i++) {
          if (meta.getMapAnnotationNamespace(i).equals(ODEPyramidStore.NAMESPACE)) {
            meta.setMapAnnotationValue(new ArrayList<MapPair>(), i);
          }
        }
      }
      catch (NullPointerException e) {
        // not unexpected if there are no map annotations
        LOGGER.trace("Could not remove resolution annotations", e);
      }
    }

    Hashtable originalMetadata = service.getOriginalMetadata(meta);
    if (originalMetadata != null) metadata = originalMetadata;

    LOGGER.trace(xml);

    if (meta.getRoot() == null) {
      throw new FormatException("Could not parse ODE-XML from TIFF comment");
    }

    String[] acquiredDates = new String[meta.getImageCount()];
    for (int i=0; i<acquiredDates.length; i++) {
      Timestamp acquisitionDate = meta.getImageAcquisitionDate(i);
      if (acquisitionDate != null) {
        acquiredDates[i] = acquisitionDate.getValue();
      }
    }

    String currentUUID = meta.getUUID();

    // special case for ungrouped files
    // attempts to correct the Z, C, and T dimensions
    // by checking the indexes for each TiffData associated
    // with the current file

    if (!isGroupFiles() && !isSingleFile(currentId)) {
      IFormatReader reader = new MinimalTiffReader();
      initializeReader(reader, currentId);
      core.set(0, 0, new ODETiffCoreMetadata(reader.getCoreMetadataList().get(0)));
      int ifdCount = reader.getImageCount();
      reader.close();
      int maxSeries = 0;
      info = new ODETiffPlane[meta.getImageCount()][];
      ArrayList<Integer> imagesToRemove = new ArrayList<>();
      ArrayList<int[]> cBounds = new ArrayList<>();
      for (int i=0; i<meta.getImageCount(); i++) {
        int maxZ = 0;
        int maxC = 0;
        int maxT = 0;
        int minZ = Integer.MAX_VALUE;
        int minC = Integer.MAX_VALUE;
        int minT = Integer.MAX_VALUE;

        int sizeZ = meta.getPixelsSizeZ(i).getValue();
        int sizeC = meta.getChannelCount(i);
        int sizeT = meta.getPixelsSizeT(i).getValue();
        String order = meta.getPixelsDimensionOrder(i).getValue();
        int num = sizeZ * sizeC * sizeT;
        ODETiffCoreMetadata m = (ODETiffCoreMetadata) (i < core.size() ? core.get(i, 0) : new ODETiffCoreMetadata(core.get(0, 0)));
        m.dimensionOrder = order;

        info[i] = new ODETiffPlane[meta.getTiffDataCount(i)];
        int next = 0;
        for (int td=0; td<meta.getTiffDataCount(i); td++) {
          String uuid = null;
          try {
            uuid = meta.getUUIDValue(i, td);
          }
          catch (NullPointerException e) { }
          String filename = null;
          try {
            filename = meta.getUUIDFileName(i, td);
          }
          catch (NullPointerException e) { }
          if ((uuid == null || !uuid.equals(currentUUID)) &&
            (filename == null || !currentId.endsWith(filename)))
          {
            // this plane doesn't appear to be in the current file
            continue;
          }

          if (i > maxSeries) {
            maxSeries = i;
          }
          NonNegativeInteger ifd = meta.getTiffDataIFD(i, td);
          NonNegativeInteger count = meta.getTiffDataPlaneCount(i, td);
          NonNegativeInteger firstZ = meta.getTiffDataFirstZ(i, td);
          NonNegativeInteger firstC = meta.getTiffDataFirstC(i, td);
          NonNegativeInteger firstT = meta.getTiffDataFirstT(i, td);

          int realCount = count == null ? 1 : count.getValue();
          if (ifd == null && count == null) {
            realCount = ifdCount;
          }
          for (int q=0; q<realCount; q++) {
            ODETiffPlane p = new ODETiffPlane();
            p.id = currentId;
            p.ifd = q;
            if (ifd != null) {
              p.ifd += ifd.getValue();
            }
            p.reader = reader;
            info[i][next++] = p;
            int z = firstZ == null ? 0 : firstZ.getValue();
            int c = firstC == null ? 0 : firstC.getValue();
            int t = firstT == null ? 0 : firstT.getValue();

            if (q > 0) {
              int index = FormatTools.getIndex(order,
                sizeZ, sizeC, sizeT, num, z, c, t);
              int[] add = FormatTools.getZCTCoords(order, sizeZ, sizeC, sizeT, num, q);
              z += add[0];
              c += add[1];
              t += add[2];
            }

            if (z > maxZ) {
              maxZ = z;
            }
            if (c > maxC) {
              maxC = c;
            }
            if (t > maxT) {
              maxT = t;
            }
            if (z < minZ) {
              minZ = z;
            }
            if (c < minC) {
              minC = c;
            }
            if (t < minT) {
              minT = t;
            }
          }
        }
        if (i <= maxSeries) {
          m.sizeZ = (maxZ - minZ) + 1;
          m.sizeC = (maxC - minC) + 1;
          m.sizeT = (maxT - minT) + 1;
          m.imageCount = m.sizeZ * m.sizeC * m.sizeT;
          m.sizeC *= meta.getChannelSamplesPerPixel(i, 0).getValue();
          if (i >= core.size()) {
            core.add(m);
          }
          cBounds.add(new int[] {minC, maxC});
        }
        else {
          imagesToRemove.add(i);
        }
      }

      // remove extra Images, Channels, and Planes

      meta.resolveReferences();
      ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) meta.getRoot();
      List<Image> images = root.copyImageList();
      for (int i=imagesToRemove.size()-1; i>=0; i--) {
        images.remove(imagesToRemove.get(i));
      }
      for (int i=0; i<images.size(); i++) {
        Image img = images.get(i);
        Pixels pix = img.getPixels();
        List<Plane> planes = pix.copyPlaneList();
        for (int p=0; p<planes.size(); p++) {
          Plane plane = planes.get(p);
          if (plane.getTheZ().getValue() >= core.get(i, 0).sizeZ ||
            plane.getTheC().getValue() >= core.get(i, 0).sizeC ||
            plane.getTheT().getValue() >= core.get(i, 0).sizeT)
          {
            pix.removePlane(planes.get(p));
          }
        }
        pix.setMetadataOnly(null);

        List<Channel> channels = pix.copyChannelList();
        for (int c=0; c<channels.size(); c++) {
          if (c < cBounds.get(i)[0] || c > cBounds.get(i)[1]) {
            pix.removeChannel(channels.get(c));
          }
        }
      }
      meta.setRoot(root);

      service.convertMetadata(meta, metadataStore);
      MetadataTools.populatePixels(metadataStore, this);

      addSubResolutions();

      return;
    }

    service.convertMetadata(meta, metadataStore);

    // determine series count from Image and Pixels elements
    int seriesCount = meta.getImageCount();
    core.clear();
    for (int i=0; i<seriesCount; i++) {
      core.add(new ODETiffCoreMetadata());
    }
    info = new ODETiffPlane[seriesCount][];

    // compile list of file/UUID mappings
    Hashtable<String, String> files = new Hashtable<>();
    for (int i=0; i<seriesCount; i++) {
      int tiffDataCount = meta.getTiffDataCount(i);
      for (int td=0; td<tiffDataCount; td++) {
        String uuid = null;
        try {
          uuid = meta.getUUIDValue(i, td);
        }
        catch (NullPointerException e) { }
        String filename = null;
        if (uuid == null) {
          // no UUID means that TiffData element refers to this file
          uuid = "";
          filename = id;
        }
        else {
          filename = meta.getUUIDFileName(i, td);
          if (!new Location(dir, filename).exists()) filename = null;
          if (filename == null) {
            if (uuid.equals(currentUUID) || currentUUID == null) {
              // UUID references this file
              filename = id;
            } else {
              filename = "";
            }
          }
          else filename = normalizeFilename(dir, filename);
          // If ODE.UUID was not defined, set currentUUID for future searches
          if (filename.equals(id) && currentUUID == null) {
            currentUUID = uuid;
          }
        }

        // If no valid file has been identified for the TiffData element,
        // either throw an exception or register the none-existing path with
        // an ERROR statement
        if (filename.isEmpty()) {
          String msg = "Missing file " + meta.getUUIDFileName(i, td) +
            " associated with UUID " + uuid + ".";
          if (failOnMissingTIFF()) {
            throw new FormatException(msg);
          } else {
            LOGGER.error(msg + " Corresponding planes will be black.");
            filename = normalizeFilename(dir, meta.getUUIDFileName(i, td));
          }
        }

        // Run some sanity check on the UUID/filename mapping
        String existing = files.get(uuid);
        if (existing == null) {
          files.put(uuid, filename);
        } else if (!existing.equals(filename)) {
          throw new FormatException("Inconsistent filenames for UUID " + uuid +
            ": " + meta.getUUIDFileName(i, td) + " does not match " +
            existing + ".");
        }
      }
    }

    // build list of used files
    Enumeration en = files.keys();
    int numUUIDs = files.size();
    HashSet fileSet = new HashSet(); // ensure no duplicate filenames
    for (int i=0; i<numUUIDs; i++) {
      String uuid = (String) en.nextElement();
      String filename = files.get(uuid);
      fileSet.add(filename);
    }
    used = new String[fileSet.size()];
    Iterator iter = fileSet.iterator();
    for (int i=0; i<used.length; i++) used[i] = (String) iter.next();

    // process TiffData elements
    Hashtable<String, IFormatReader> readers = new Hashtable<>();
    boolean adjustedSamples = false;
    for (int i=0; i<seriesCount; i++) {
      int s = i;
      LOGGER.debug("Image[{}] {", i);
      LOGGER.debug("  id = {}", meta.getImageID(i));

      String order = meta.getPixelsDimensionOrder(i).toString();

      PositiveInteger samplesPerPixel = null;
      if (meta.getChannelCount(i) > 0) {
        samplesPerPixel = meta.getChannelSamplesPerPixel(i, 0);
      }
      int samples = samplesPerPixel == null ?  -1 : samplesPerPixel.getValue();
      int tiffSamples = firstIFD.getSamplesPerPixel();

      if (adjustedSamples ||
        (samples != tiffSamples && (i == 0 || samples < 0)))
      {
        LOGGER.warn("SamplesPerPixel mismatch: ODE={}, TIFF={}",
          samples, tiffSamples);
        samples = tiffSamples;
        adjustedSamples = true;
      }
      else {
        adjustedSamples = false;
      }

      if (adjustedSamples && meta.getChannelCount(i) <= 1) {
        adjustedSamples = false;
      }

      int effSizeC = meta.getPixelsSizeC(i).getValue();
      if (!adjustedSamples) {
        effSizeC /= samples;
      }
      if (effSizeC == 0) effSizeC = 1;
      if (effSizeC * samples != meta.getPixelsSizeC(i).getValue()) {
        effSizeC = meta.getPixelsSizeC(i).getValue();
      }
      int sizeT = meta.getPixelsSizeT(i).getValue();
      int sizeZ = meta.getPixelsSizeZ(i).getValue();
      int num = effSizeC * sizeT * sizeZ;

      ODETiffPlane[] planes = new ODETiffPlane[num];
      for (int no=0; no<num; no++) planes[no] = new ODETiffPlane();

      int tiffDataCount = meta.getTiffDataCount(i);
      Boolean zOneIndexed = null;
      Boolean cOneIndexed = null;
      Boolean tOneIndexed = null;

      // pre-scan TiffData indices to see if any of them are indexed from 1

      for (int td=0; td<tiffDataCount; td++) {
        NonNegativeInteger firstC = meta.getTiffDataFirstC(i, td);
        NonNegativeInteger firstT = meta.getTiffDataFirstT(i, td);
        NonNegativeInteger firstZ = meta.getTiffDataFirstZ(i, td);
        int c = firstC == null ? 0 : firstC.getValue();
        int t = firstT == null ? 0 : firstT.getValue();
        int z = firstZ == null ? 0 : firstZ.getValue();

        if (c >= effSizeC && cOneIndexed == null) {
          cOneIndexed = true;
        }
        else if (c == 0) {
          cOneIndexed = false;
        }
        if (z >= sizeZ && zOneIndexed == null) {
          zOneIndexed = true;
        }
        else if (z == 0) {
          zOneIndexed = false;
        }
        if (t >= sizeT && tOneIndexed == null) {
          tOneIndexed = true;
        }
        else if (t == 0) {
          tOneIndexed = false;
        }
      }

      for (int td=0; td<tiffDataCount; td++) {
        LOGGER.debug("    TiffData[{}] {", td);
        // extract TiffData parameters
        String filename = null;
        String uuid = null;
        try {
          filename = meta.getUUIDFileName(i, td);
        } catch (NullPointerException e) {
          LOGGER.debug("Ignoring null UUID object when retrieving filename.");
        }
        try {
          uuid = meta.getUUIDValue(i, td);
        } catch (NullPointerException e) {
          LOGGER.debug("Ignoring null UUID object when retrieving value.");
        }
        NonNegativeInteger tdIFD = meta.getTiffDataIFD(i, td);
        int ifd = tdIFD == null ? 0 : tdIFD.getValue();
        NonNegativeInteger numPlanes = meta.getTiffDataPlaneCount(i, td);
        NonNegativeInteger firstC = meta.getTiffDataFirstC(i, td);
        NonNegativeInteger firstT = meta.getTiffDataFirstT(i, td);
        NonNegativeInteger firstZ = meta.getTiffDataFirstZ(i, td);
        int c = firstC == null ? 0 : firstC.getValue();
        int t = firstT == null ? 0 : firstT.getValue();
        int z = firstZ == null ? 0 : firstZ.getValue();

        // NB: some writers index FirstC, FirstZ and FirstT from 1
        if (cOneIndexed != null && cOneIndexed) c--;
        if (zOneIndexed != null && zOneIndexed) z--;
        if (tOneIndexed != null && tOneIndexed) t--;

        if (z >= sizeZ || c >= effSizeC || t >= sizeT) {
          LOGGER.warn("Found invalid TiffData: Z={}, C={}, T={}",
            new Object[] {z, c, t});
          break;
        }

        int index = FormatTools.getIndex(order,
          sizeZ, effSizeC, sizeT, num, z, c, t);
        int count = numPlanes == null ? 1 : numPlanes.getValue();
        if (count == 0) {
          core.set(s, 0, null);
          break;
        }

        // get reader object for this filename
        if (filename == null) {
          if (uuid == null) filename = id;
          else filename = files.get(uuid);
        }
        else filename = normalizeFilename(dir, filename);
        IFormatReader r = readers.get(filename);
        if (r == null) {
          r = new MinimalTiffReader();
          readers.put(filename, r);
        }

        Location file = new Location(filename);
        boolean exists = true;
        if (!file.exists()) {
          // if this is an absolute file name, try using a relative name
          // old versions of ODETiffWriter wrote an absolute path to
          // UUID.FileName, which causes problems if the file is moved to
          // a different directory
          filename =
            filename.substring(filename.lastIndexOf(File.separator) + 1);
          filename = dir + File.separator + filename;

          if (!new Location(filename).exists()) {
            filename = currentId;
            // if only one file is defined, we have to assume that it
            // corresponds to the current file
            exists = fileSet.size() == 1;
          }
        }

        // populate plane index -> IFD mapping
        for (int q=0; q<count; q++) {
          int no = index + q;
          planes[no].reader = r;
          planes[no].id = filename;
          planes[no].ifd = ifd + q;
          planes[no].certain = true;
          planes[no].exists = exists;
          LOGGER.trace("      Plane[{}]: file={}, IFD={}",
            new Object[] {no, planes[no].id, planes[no].ifd});
        }
        if (numPlanes == null) {
          // unknown number of planes; fill down
          for (int no=index+1; no<num; no++) {
            if (planes[no].certain) break;
            planes[no].reader = r;
            planes[no].id = filename;
            planes[no].ifd = planes[no - 1].ifd + 1;
            planes[no].exists = exists;
            LOGGER.trace("      Plane[{}]: FILLED", no);
          }
        }
        else {
          // known number of planes; clear anything subsequently filled
          for (int no=index+count; no<num; no++) {
            if (planes[no].certain) break;
            planes[no].reader = null;
            planes[no].id = null;
            planes[no].ifd = -1;
            LOGGER.trace("      Plane[{}]: CLEARED", no);
          }
        }
        LOGGER.debug("    }");
      }

      if (core.get(s, 0) == null) continue;

      // verify that all planes are available
      LOGGER.debug("    --------------------------------");
      int validPlanes = num;
      for (int no=0; no<num; no++) {
        LOGGER.debug("    Plane[{}]: file={}, IFD={}",
          new Object[] {no, planes[no].id, planes[no].ifd});
        if (planes[no].reader == null) {
          validPlanes--;
          LOGGER.warn("Image ID '{}': missing plane #{}",
            meta.getImageID(i), no);
        }
      }
      // only use the current file's IFD count if this is a single file set
      // otherwise, we risk pulling in IFDs from a different Image
      if (validPlanes < num && files.size() <= 1) {
        LOGGER.warn("Using TiffReader to determine the number of planes.");
        TiffReader r = new TiffReader();
        initializeReader(r, currentId);
        try {
          planes = new ODETiffPlane[r.getImageCount()];
          for (int plane=0; plane<planes.length; plane++) {
            planes[plane] = new ODETiffPlane();
            planes[plane].id = currentId;
            planes[plane].reader = r;
            planes[plane].ifd = plane;
          }
          num = planes.length;
        }
        finally {
          r.close();
        }
      }
      LOGGER.debug("  }");

      // populate core metadata
      ODETiffCoreMetadata m = (ODETiffCoreMetadata) core.get(s, 0);
      info[s] = planes;
      RandomAccessInputStream testFile = null;
      try {
        if (info[s][0].id != null) {
          testFile = new RandomAccessInputStream(info[s][0].id, 16);
        }
        if (info[s][0].reader == null) {
          info[s][0].reader = new MinimalTiffReader();
        }
        String firstFile = info[s][0].id;
        if (firstFile == null ||
          (testFile != null && !info[s][0].reader.isThisType(testFile)))
        {
          LOGGER.warn("{} is not a valid ODE-TIFF", info[s][0].id);
          info[s][0].id = currentId;
          info[s][0].exists = false;
          if (info[s][0].ifd < 0) {
            info[s][0].ifd = 0;
          }
        }
        if (testFile != null) {
          testFile.close();
        }
        for (int plane=1; plane<info[s].length; plane++) {
          if (info[s][plane].id == null || info[s][plane].reader == null) {
            continue;
          }
          if (info[s][plane].id.equals(firstFile)) {
            // don't repeat slow type checking if the files are the same
            if (!info[s][0].exists) {
              info[s][plane].id = info[s][0].id;
              info[s][plane].exists = false;
            }

            continue;
          }
          try (RandomAccessInputStream test = new RandomAccessInputStream(info[s][plane].id, 16)) {
            if (!info[s][plane].reader.isThisType(test)) {
              LOGGER.warn("{} is not a valid ODE-TIFF", info[s][plane].id);
              info[s][plane].id = info[s][0].id;
              info[s][plane].exists = false;
            }
          }
        }

        initializeReader(info[s][0].reader, info[s][0].id);
        m.tileWidth = info[s][0].reader.getOptimalTileWidth();
        m.tileHeight = info[s][0].reader.getOptimalTileHeight();

        m.sizeX = meta.getPixelsSizeX(i).getValue();
        int tiffWidth = (int) firstIFD.getImageWidth();
        if (m.sizeX != tiffWidth && s == 0) {
          LOGGER.warn("SizeX mismatch: ODE={}, TIFF={}",
            m.sizeX, tiffWidth);
        }
        m.sizeY = meta.getPixelsSizeY(i).getValue();
        int tiffHeight = (int) firstIFD.getImageLength();
        if (m.sizeY != tiffHeight && s ==  0) {
          LOGGER.warn("SizeY mismatch: ODE={}, TIFF={}",
            m.sizeY, tiffHeight);
        }
        m.sizeZ = meta.getPixelsSizeZ(i).getValue();
        m.sizeC = meta.getPixelsSizeC(i).getValue();
        m.sizeT = meta.getPixelsSizeT(i).getValue();
        m.pixelType = FormatTools.pixelTypeFromString(
          meta.getPixelsType(i).toString());
        int tiffPixelType = firstIFD.getPixelType();
        if (m.pixelType != tiffPixelType && (s == 0 || adjustedSamples)) {
          LOGGER.warn("PixelType mismatch: ODE={}, TIFF={}",
            m.pixelType, tiffPixelType);
          m.pixelType = tiffPixelType;
        }
        m.imageCount = num;
        m.dimensionOrder = meta.getPixelsDimensionOrder(i).toString();

        // hackish workaround for files exported by Bhojpur ODE that have an
        // incorrect dimension order
        String uuidFileName = "";
        try {
          if (meta.getTiffDataCount(i) > 0) {
            uuidFileName = meta.getUUIDFileName(i, 0);
          }
        }
        catch (NullPointerException e) { }
        if (meta.getChannelCount(i) > 0 && meta.getChannelName(i, 0) == null &&
          meta.getTiffDataCount(i) > 0 &&
          uuidFileName.indexOf("__ode_export") != -1)
        {
          m.dimensionOrder = "XYZCT";
        }

        m.orderCertain = true;
        PhotoInterp photo = firstIFD.getPhotometricInterpretation();
        m.rgb = samples > 1 || photo == PhotoInterp.RGB;
        if ((samples != m.sizeC && (samples % m.sizeC) != 0 &&
          (m.sizeC % samples) != 0) || m.sizeC == 1 ||
          adjustedSamples)
        {
          m.sizeC *= samples;
        }

        if (m.sizeZ * m.sizeT * m.sizeC >
          m.imageCount && !m.rgb)
        {
          if (m.sizeZ == m.imageCount) {
            m.sizeT = 1;
            m.sizeC = 1;
          }
          else if (m.sizeT == m.imageCount) {
            m.sizeZ = 1;
            m.sizeC = 1;
          }
          else if (m.sizeC == m.imageCount) {
            m.sizeT = 1;
            m.sizeZ = 1;
          }
        }

        if (meta.getPixelsBinDataCount(i) > 1) {
          LOGGER.warn("ODE-TIFF Pixels element contains BinData elements! " +
                      "Ignoring.");
        }
        m.littleEndian = firstIFD.isLittleEndian();
        m.interleaved = false;
        m.indexed = photo == PhotoInterp.RGB_PALETTE &&
          firstIFD.getIFDValue(IFD.COLOR_MAP) != null;
        if (m.indexed) {
          m.rgb = false;
        }
        m.falseColor = true;
        m.metadataComplete = true;
        if (meta.getPixelsSignificantBits(i) != null) {
          m.bitsPerPixel = meta.getPixelsSignificantBits(i).getValue();
        }
      }
      catch (NullPointerException exc) {
        throw new FormatException("Incomplete Pixels metadata", exc);
      }
      finally {
        if (testFile != null) {
          testFile.close();
        }
        // close the file handle, but keep all other metadata
        if (info[s][0].reader != null) {
          info[s][0].reader.close(true);
        }
      }
    }

    // remove null CoreMetadata entries

    CoreMetadataList series = new CoreMetadataList();
    final List<ODETiffPlane[]> planeInfo = new ArrayList<>();
    int currentSeries = 0;
    for (int i=0; i<core.size(); i++) {
      if (core.get(i, 0) == null) {
        continue;
      }
      series.add();
      planeInfo.add(info[i]);
      for (int j=0; j<core.size(i); j++) {
        series.add(currentSeries, core.get(i, j));
      }
      currentSeries++;
    }
    core = series;
    info = planeInfo.toArray(new ODETiffPlane[0][0]);

    if (getImageCount() == 1) {
      ODETiffCoreMetadata ms0 = (ODETiffCoreMetadata) core.get(0, 0);
      ms0.sizeZ = 1;
      if (!ms0.rgb) {
        ms0.sizeC = 1;
      }
      ms0.sizeT = 1;
    }

    for (int i=0; i<core.size(); i++) {
      ODETiffCoreMetadata m = (ODETiffCoreMetadata) core.get(i, 0);
      Modulo z = service.getModuloAlongZ(meta, i);
      if (z != null) {
        m.moduloZ = z;
      }
      Modulo c = service.getModuloAlongC(meta, i);
      if (c != null) {
        m.moduloC = c;
      }
      Modulo t = service.getModuloAlongT(meta, i);
      if (t != null) {
        m.moduloT = t;
      }
    }

    // remove any values we no longer need from the
    // helper readers' IFDs
    for (ODETiffPlane[] s : info) {
      for (ODETiffPlane p : s) {
        if (p != null && p.reader != null) {
          removeIFDComments(p.reader);
          p.reader.close();
        }
      }
    }

    MetadataTools.populatePixels(metadataStore, this, false, false);
    for (int i=0; i<meta.getImageCount(); i++) {
      // make sure that TheZ, TheC, and TheT are all set on any
      // existing Planes
      // missing Planes are not added, and exising TheZ, TheC, and
      // TheT values are not changed
      for (int p=0; p<meta.getPlaneCount(i); p++) {
        NonNegativeInteger z = meta.getPlaneTheZ(i, p);
        NonNegativeInteger c = meta.getPlaneTheC(i, p);
        NonNegativeInteger t = meta.getPlaneTheT(i, p);

        if (z == null) {
          z = new NonNegativeInteger(0);
          metadataStore.setPlaneTheZ(z, i, p);
        }
        if (c == null) {
          c = new NonNegativeInteger(0);
          metadataStore.setPlaneTheC(c, i, p);
        }
        if (t == null) {
          t = new NonNegativeInteger(0);
          metadataStore.setPlaneTheT(t, i, p);
        }
      }
    }
    for (int i=0; i<acquiredDates.length; i++) {
      if (acquiredDates[i] != null) {
        metadataStore.setImageAcquisitionDate(
            new Timestamp(acquiredDates[i]), i);
      }
    }

    addSubResolutions();
  }

  // -- ODETiffReader API methods --

  /**
   * Get a MetadataStore suitable for display.
   *
   * Note: Historically, this method removed certain elements
   * for display purposes and was not be suitable for use with
   * FormatWriter due to not containing required BinData
   * BigEndian attributes. This is no longer the case; the general
   * {@link SubResolutionFormatReader#getMetadataStore()} method will always create
   * valid metadata which is suitable for both display and use
   * with FormatWriter, and so should be used instead.
   *
   * @return the metadata store.
   * @deprecated Use the general {@link SubResolutionFormatReader#getMetadataStore()} method.
   */
  public MetadataStore getMetadataStoreForDisplay() {
    return getMetadataStore();
  }

  /**
   * Get a MetadataStore suitable for writing.
   *
   * Note: Historically, this method created metadata suitable
   * for use with FormatWriter, but would possibly not generate
   * valid ODE-XML if both BinData and TiffData elements were
   * present.  This is no longer the case; the general
   * {@link SubResolutionFormatReader#getMetadataStore()} method will always create
   * valid metadata which is suitable for use with FormatWriter,
   * and so should be used instead.
   *
   * @return the metadata store.
   * @deprecated Use the general {@link SubResolutionFormatReader#getMetadataStore()} method.
   */
  public MetadataStore getMetadataStoreForConversion() {
    return getMetadataStore();
  }

  // -- Helper methods --

  private String normalizeFilename(String dir, String name) {
     Location file = new Location(dir, name);
     if (file.exists()) return file.getAbsolutePath();
     return name;
  }

  private void setupService() throws FormatException {
    try {
      ServiceFactory factory = new ServiceFactory();
      service = factory.getInstance(ODEXMLService.class);
    }
    catch (DependencyException de) {
      throw new MissingLibraryException(ODEXMLServiceImpl.NO_ODE_XML_MSG, de);
    }
  }

  private void addSubResolutions() throws IOException, FormatException {
    boolean repopulateMetadata = false;
    for(int s = 0; s < core.size(); s++) {
      ODETiffCoreMetadata c0 = (ODETiffCoreMetadata) core.get(s, 0);
      int i = info[s][0].ifd;
      MinimalTiffReader r = (MinimalTiffReader) info[s][0].reader;
      if (r.getCurrentFile() == null) {
        r.setId(info[s][0].id);
      }
      r.lastPlane = i;
      IFDList ifdList = r.getIFDs();
      if (i >= ifdList.size()) {
        LOGGER.warn("Error untangling IFDs; the ODE-TIFF file may be malformed (IFD #{} missing).", i);
        continue;
      }
      IFD ifd = ifdList.get(i);
      IFDList subifds = null;
      try (RandomAccessInputStream rs = new RandomAccessInputStream(info[s][0].id, 16)) {
        TiffParser p = new TiffParser(rs);
        subifds = p.getSubIFDs(ifd);
      }
      c0.resolutionCount = subifds.size() + 1;

      if (c0.resolutionCount > 1 && hasFlattenedResolutions()) {
        repopulateMetadata = true;
      }

      for (int si = 0; si < subifds.size(); si++) {
        IFD subifd = subifds.get(si);
        ODETiffCoreMetadata c = new ODETiffCoreMetadata(c0);
        c.subresolutionOffset = si;
        c.sizeX = (int) subifd.getImageWidth();
        c.sizeY = (int) subifd.getImageLength();
        try {
          c.tileWidth = (int) subifd.getTileWidth();
        }
        catch (FormatException e) {
          c.tileWidth = c.sizeX;
        }
        try {
          c.tileHeight = (int) subifd.getTileLength();
          if (c.tileHeight <= 0) {
            c.tileHeight = c.sizeY;
          }
        }
        catch (FormatException e) {
          c.tileHeight = c.sizeY;
        }
        // Duplicate MinimalTiffReader getOptimalTileHeight logic
        if (DataTools.safeMultiply64(c.tileHeight, c.tileWidth) >
          10 * 1024 * 1024) {
          int bpp = FormatTools.getBytesPerPixel(c.pixelType);
          int effC = c.sizeC / (c.imageCount / (c.sizeZ * c.sizeT));
          int maxHeight = (1024 * 1024) / (c.sizeX * effC * bpp);
          c.tileHeight = (int) Math.min(maxHeight, c.sizeY);
        }
        core.add(s, c);
      }
      r.close();
    }
    core.reorder();

    // make sure the Image count matches the series count when
    // the resolutions are flattened
    if (repopulateMetadata) {
      MetadataTools.populatePixels(metadataStore, this);
    }
  }

  /** Extracts the ODE-XML from the current {@link #metadataFile}. */
  private String readMetadataFile() throws IOException {
    LOGGER.debug("Reading metadata from {}", metadataFile);
    if (checkSuffix(metadataFile, "ode.tiff") ||
        checkSuffix(metadataFile, "ode.tif") ||
        checkSuffix(metadataFile, "ode.tf2") ||
        checkSuffix(metadataFile, "ode.tf8") ||
        checkSuffix(metadataFile, "ode.btf")) {
      // metadata file is an ODE-TIFF file; extract ODE-XML comment
      try (RandomAccessInputStream in = new RandomAccessInputStream(metadataFile)) {
        TiffParser parser = new TiffParser(in);
        return parser.getComment();
      }
    }
    // assume metadata file is an XML file
    return DataTools.readFile(metadataFile);
  }

  private static IFD getFirstIFD(String fname) throws IOException {
    IFD firstIFD = null;
    try (RandomAccessInputStream ras = new RandomAccessInputStream(fname, 16)) {
      TiffParser tp = new TiffParser(ras);
      firstIFD = tp.getFirstIFD();
    }
    return firstIFD;
  }

  private void initializeReader(IFormatReader r, String file)
    throws FormatException, IOException
  {
    r.setId(file);
    removeIFDComments(r);
  }

  private void removeIFDComments(IFormatReader r) {
    if (r != null && r instanceof MinimalTiffReader) {
      if (((MinimalTiffReader) r).ifds != null) {
        for (IFD ifd : ((MinimalTiffReader) r).ifds) {
          ifd.remove(IFD.IMAGE_DESCRIPTION);
        }
      }
    }
  }

  public boolean failOnMissingTIFF() {
      MetadataOptions options = getMetadataOptions();
      if (options instanceof DynamicMetadataOptions) {
        return ((DynamicMetadataOptions) options).getBoolean(
         FAIL_ON_MISSING_KEY, FAIL_ON_MISSING_DEFAULT);
      }
      return FAIL_ON_MISSING_DEFAULT;
    }

  // -- Helper classes --

  /** Structure containing details on where to find a particular image plane. */
  private class ODETiffPlane {
    /** Reader to use for accessing this plane. */
    public IFormatReader reader;
    /** File containing this plane. */
    public String id;
    /** IFD number of this plane. */
    public int ifd = -1;
    /** Certainty flag, for dealing with unspecified NumPlanes. */
    public boolean certain = false;
    /**
     * Whether or not the file meant to contain this plane exists.
     * The value of 'id' may be changed to allow the tile and image dimensions
     * to be populated; this flag indicates whether the originally recorded file
     * for this plane exists.
     */
    public boolean exists = true;
  }

  private class ODETiffCoreMetadata extends CoreMetadata {
    /** SubIFD offset for sub-resolution level; -1 if the main IFD. */
    int subresolutionOffset = -1;
    /** Tile size X. */
    int tileWidth;
    /** Tile size Y. */
    int tileHeight;

    ODETiffCoreMetadata() {
      super();
    }

    ODETiffCoreMetadata(ODETiffCoreMetadata copy) {
      super(copy);
      subresolutionOffset = copy.subresolutionOffset;
      tileWidth = copy.tileWidth;
      tileHeight = copy.tileHeight;
    }

    ODETiffCoreMetadata(CoreMetadata copy) {
      super(copy);
    }
  }
}
