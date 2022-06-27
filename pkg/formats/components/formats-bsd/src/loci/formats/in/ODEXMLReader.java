/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.in;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import loci.common.CBZip2InputStream;
import loci.common.RandomAccessInputStream;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.common.xml.BaseHandler;
import loci.common.xml.XMLTools;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.MissingLibraryException;
import loci.formats.codec.CodecOptions;
import loci.formats.codec.JPEG2000Codec;
import loci.formats.codec.JPEGCodec;
import loci.formats.codec.ZlibCodec;
import loci.formats.meta.MetadataStore;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;
import loci.formats.services.ODEXMLServiceImpl;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;
import com.google.common.io.BaseEncoding;


/**
 * ODEXMLReader is the file format reader for ODE-XML files.
 */
public class ODEXMLReader extends FormatReader {

  // -- Fields --

  // compression value and offset for each BinData element
  private List<BinData> binData;
  private List<Long> binDataOffsets;
  private List<String> compression;

  private String odexml;
  private boolean hasSPW = false;

  // -- Constructor --

  /** Constructs a new ODE-XML reader. */
  public ODEXMLReader() {
    super("ODE-XML", new String[] {"ode", "ode.xml"});
    domains = FormatTools.NON_GRAPHICS_DOMAINS;
    suffixNecessary = false;
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    final int blockLen = 64;
    String xml = stream.readString(blockLen);
    return xml.startsWith("<?xml") && xml.indexOf("<ODE") >= 0;
  }

  /* @see loci.formats.IFormatReader#isThisType(String, boolean) */
  @Override
  public boolean isThisType(String name, boolean open) {
    if (checkSuffix(name, "companion.ode")) {
      // pass binary-only files along to the ODE-TIFF reader
      return false;
    }
    return super.isThisType(name, open);
  }

  /* @see loci.formats.IFormatReader#getDomains() */
  @Override
  public String[] getDomains() {
    FormatTools.assertId(currentId, true, 1);
    return hasSPW ? new String[] {FormatTools.HCS_DOMAIN} :
      FormatTools.NON_SPECIAL_DOMAINS;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    if (binDataOffsets.size() == 0) return buf;
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    int index = no;
    int series = getSeries();
    for (int i=0; i<series; i++) {
      index += core.get(i).imageCount;
    }
    if (index >= binDataOffsets.size()) {
      index = binDataOffsets.size() - 1;
    }

    long offset = binDataOffsets.get(index).longValue();
    String compress = compression.get(index);

    in.seek(offset);

    int depth = FormatTools.getBytesPerPixel(getPixelType());
    int planeSize = getSizeX() * getSizeY() * depth;

    CodecOptions options = new CodecOptions();
    options.width = getSizeX();
    options.height = getSizeY();
    options.bitsPerSample = depth * 8;
    options.channels = getRGBChannelCount();
    options.maxBytes = planeSize;
    options.littleEndian = isLittleEndian();
    options.interleaved = isInterleaved();

    String encoded = in.readString("<");
    encoded = encoded.trim();
    if (encoded.length() == 0 || encoded.equals("<")) {
      LOGGER.debug("No pixel data for plane #{}", no);
      return buf;
    }
    encoded = encoded.substring(0, encoded.length() - 1);
    byte[] pixels =  BaseEncoding.base64().decode(encoded);
    // return a blank plane if no pixel data was stored
    if (pixels.length == 0) {
      LOGGER.debug("No pixel data for plane #{}", no);
      return buf;
    }

    // TODO: Create a method uncompress to handle all compression methods
    if (compress.equals("bzip2")) {
      byte[] tempPixels = pixels;
      pixels = new byte[tempPixels.length - 2];
      System.arraycopy(tempPixels, 2, pixels, 0, pixels.length);

      ByteArrayInputStream bais = new ByteArrayInputStream(pixels);
      CBZip2InputStream bzip = new CBZip2InputStream(bais);
      pixels = new byte[planeSize];
      bzip.read(pixels, 0, pixels.length);
      tempPixels = null;
      bais.close();
      bzip.close();
      bais = null;
      bzip = null;
    }
    else if (compress.equals("zlib")) {
      pixels = new ZlibCodec().decompress(pixels, options);
    }
    else if (compress.equals("J2K")) {
      pixels = new JPEG2000Codec().decompress(pixels, options);
    }
    else if (compress.equals("JPEG")) {
      pixels = new JPEGCodec().decompress(pixels, options);
    }

    for (int row=0; row<h; row++) {
      int off = (row + y) * getSizeX() * depth + x * depth;
      System.arraycopy(pixels, off, buf, row * w * depth, w * depth);
    }

    pixels = null;

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      compression = null;
      binDataOffsets = null;
      binData = null;
      odexml = null;
      hasSPW = false;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);

    in = new RandomAccessInputStream(id);
    in.setEncoding("ASCII");
    binData = new ArrayList<BinData>();
    binDataOffsets = new ArrayList<Long>();
    compression = new ArrayList<String>();

    DefaultHandler handler = new ODEXMLHandler();
    try (RandomAccessInputStream s = new RandomAccessInputStream(id)) {
      XMLTools.parseXML(s, handler);
    }
    catch (IOException e) {
      throw new FormatException("Malformed ODE-XML", e);
    }

    int lineNumber = 1;
    for (BinData bin : binData) {
      int line = bin.getRow();
      int col = bin.getColumn();

      while (lineNumber < line) {
        in.readLine();
        lineNumber++;
      }
      binDataOffsets.add(in.getFilePointer() + col - 1);
    }

    LOGGER.info("Populating metadata");

    ODEXMLMetadata odexmlMeta;
    ODEXMLService service;
    try {
      ServiceFactory factory = new ServiceFactory();
      service = factory.getInstance(ODEXMLService.class);
      odexmlMeta = service.createODEXMLMetadata(odexml);
    }
    catch (DependencyException de) {
      throw new MissingLibraryException(ODEXMLServiceImpl.NO_ODE_XML_MSG, de);
    }
    catch (ServiceException se) {
      throw new FormatException(se);
    }

    hasSPW = odexmlMeta.getPlateCount() > 0;

    // TODO
    //Hashtable originalMetadata = odexmlMeta.getOriginalMetadata();
    //if (originalMetadata != null) metadata = originalMetadata;

    int numDatasets = odexmlMeta.getImageCount();

    int oldSeries = getSeries();
    core.clear();
    for (int i=0; i<numDatasets; i++) {
      CoreMetadata ms = new CoreMetadata();
      core.add(ms);

      setSeries(i);

      Integer w = odexmlMeta.getPixelsSizeX(i).getValue();
      Integer h = odexmlMeta.getPixelsSizeY(i).getValue();
      Integer t = odexmlMeta.getPixelsSizeT(i).getValue();
      Integer z = odexmlMeta.getPixelsSizeZ(i).getValue();
      Integer c = odexmlMeta.getPixelsSizeC(i).getValue();
      if (w == null || h == null || t == null || z == null | c == null) {
        throw new FormatException("Image dimensions not found");
      }

      Boolean endian = null;
      if (binData.size() > 0) {
        endian = false;
        if (odexmlMeta.getPixelsBigEndian(i) != null) {
          endian = odexmlMeta.getPixelsBigEndian(i).booleanValue();
        }
        else if (odexmlMeta.getPixelsBinDataCount(i) != 0) {
          endian = odexmlMeta.getPixelsBinDataBigEndian(i, 0).booleanValue();
        }
      }
      String pixType = odexmlMeta.getPixelsType(i).toString();
      ms.dimensionOrder = odexmlMeta.getPixelsDimensionOrder(i).toString();
      ms.sizeX = w.intValue();
      ms.sizeY = h.intValue();
      ms.sizeT = t.intValue();
      ms.sizeZ = z.intValue();
      ms.sizeC = c.intValue();
      ms.imageCount = getSizeZ() * getSizeC() * getSizeT();
      ms.littleEndian = endian == null ? false : !endian.booleanValue();
      ms.rgb = false;
      ms.interleaved = false;
      ms.indexed = false;
      ms.falseColor = true;
      ms.pixelType = FormatTools.pixelTypeFromString(pixType);
      ms.orderCertain = true;
      if (odexmlMeta.getPixelsSignificantBits(i) != null) {
        ms.bitsPerPixel = odexmlMeta.getPixelsSignificantBits(i).getValue();
      }
    }
    setSeries(oldSeries);

    // populate assigned metadata store with the
    // contents of the internal ODE-XML metadata object
    MetadataStore store = getMetadataStore();
    service.convertMetadata(odexmlMeta, store);
    MetadataTools.populatePixels(store, this, false, false);
  }

  // -- Helper class --

  class ODEXMLHandler extends BaseHandler {
    private final StringBuilder xmlBuffer;
    private String currentQName;
    private Locator locator;
    private boolean inPixels;

    public ODEXMLHandler() {
      xmlBuffer = new StringBuilder();
      inPixels = false;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
      if (!inPixels || currentQName.indexOf("BinData") < 0) {
        xmlBuffer.append(new String(ch, start, length));
      }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
      if (qName.indexOf("Pixels") != -1) {
        inPixels = false;
      }

      xmlBuffer.append("</");
      xmlBuffer.append(qName);
      xmlBuffer.append(">");
    }

    @Override
    public void startElement(String uri, String localName, String qName,
      Attributes attributes)
    {
      currentQName = qName;

      if (qName.indexOf("Pixels") != -1) {
        inPixels = true;
      }

      if (inPixels && qName.indexOf("BinData") != -1) {
        binData.add(
          new BinData(locator.getLineNumber(), locator.getColumnNumber()));
        String compress = attributes.getValue("Compression");
        compression.add(compress == null ? "" : compress);

        xmlBuffer.append("<");
        xmlBuffer.append(qName);
        for (int i=0; i<attributes.getLength(); i++) {
          String key = XMLTools.escapeXML(attributes.getQName(i));
          String value = XMLTools.escapeXML(attributes.getValue(i));
          if (key.equals("Length")) value = "0";
          xmlBuffer.append(" ");
          xmlBuffer.append(key);
          xmlBuffer.append("=\"");
          xmlBuffer.append(value);
          xmlBuffer.append("\"");
        }
        xmlBuffer.append(">");
      } else {
        xmlBuffer.append("<");
        xmlBuffer.append(qName);
        for (int i=0; i<attributes.getLength(); i++) {
          String key = XMLTools.escapeXML(attributes.getQName(i));
          String value = XMLTools.escapeXML(attributes.getValue(i));
          if (key.equals("BigEndian")) {
            String endian = value.toLowerCase();
            if (!endian.equals("true") && !endian.equals("false")) {
              // hack for files that specify 't' or 'f' instead of
              // 'true' or 'false'
              if (endian.startsWith("t")) endian = "true";
              else if (endian.startsWith("f")) endian = "false";
            }
            value = endian;
          }
          xmlBuffer.append(" ");
          xmlBuffer.append(key);
          xmlBuffer.append("=\"");
          xmlBuffer.append(value);
          xmlBuffer.append("\"");
        }
        xmlBuffer.append(">");
      }
    }

    @Override
    public void endDocument() {
      odexml = xmlBuffer.toString();
    }

    @Override
    public void setDocumentLocator(Locator locator) {
      this.locator = locator;
    }
  }

  class BinData {
    private int row;
    private int column;

    public BinData(int row, int column) {
      this.row = row;
      this.column = column;
    }

    public int getRow() { return row; }
    public int getColumn() { return column; }
  }

}
