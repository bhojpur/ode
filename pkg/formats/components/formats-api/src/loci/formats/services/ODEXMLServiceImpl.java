/*
 * Top-level reader and writer APIs
 */

package loci.formats.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import loci.common.services.AbstractService;
import loci.common.services.ServiceException;
import loci.common.xml.XMLTools;
import loci.formats.CoreMetadata;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.Modulo;
import loci.formats.meta.IMetadata;
import loci.formats.meta.MetadataRetrieve;
import loci.formats.meta.MetadataStore;
import loci.formats.meta.ModuloAnnotation;
import loci.formats.meta.OriginalMetadataAnnotation;
import loci.formats.ode.ODEPyramidStore;
import loci.formats.ode.ODEXMLMetadata;

import ode.units.quantity.Length;

import ode.xml.meta.MetadataConverter;
import ode.xml.meta.ODEXMLMetadataRoot;
import ode.xml.model.BinData;
import ode.xml.model.Channel;
import ode.xml.model.Image;
import ode.xml.model.MetadataOnly;
import ode.xml.model.ODE;
import ode.xml.model.ODEModel;
import ode.xml.model.ODEModelImpl;
import ode.xml.model.ODEModelObject;
import ode.xml.model.Pixels;
import ode.xml.model.TiffData;
import ode.xml.model.Annotation;
import ode.xml.model.StructuredAnnotations;
import ode.xml.model.XMLAnnotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ODEXMLServiceImpl extends AbstractService implements ODEXMLService
{

  /** Latest ODE-XML version namespace. */
  public static final String LATEST_VERSION = "2016-06";

  public static final String NO_ODE_XML_MSG =
    "ode-xml.jar is required to read ODE-TIFF files.  " +
    "Please download it from " + FormatTools.URL_BIO_FORMATS_LIBRARIES;

  /** Logger for this class. */
  private static final Logger LOGGER =
    LoggerFactory.getLogger(ODEXMLService.class);

  // -- Stylesheet names --

  private static final String XSLT_PATH = "/transforms/";
  private static final String XSLT_2003FC =
    XSLT_PATH + "2003-FC-to-2008-09.xsl";
  private static final String XSLT_200706 =
    XSLT_PATH + "2007-06-to-2008-09.xsl";
  private static final String XSLT_200802 =
    XSLT_PATH + "2008-02-to-2008-09.xsl";
  private static final String XSLT_200809 =
    XSLT_PATH + "2008-09-to-2009-09.xsl";
  private static final String XSLT_200909 =
    XSLT_PATH + "2009-09-to-2010-04.xsl";
  private static final String XSLT_201004 =
    XSLT_PATH + "2010-04-to-2010-06.xsl";
  private static final String XSLT_201006 =
    XSLT_PATH + "2010-06-to-2011-06.xsl";
  private static final String XSLT_201106 =
    XSLT_PATH + "2011-06-to-2012-06.xsl";
  private static final String XSLT_201206 =
    XSLT_PATH + "2012-06-to-2013-06.xsl";
  private static final String XSLT_201306 =
    XSLT_PATH + "2013-06-to-2015-01.xsl";
  private static final String XSLT_201501 =
    XSLT_PATH + "2015-01-to-2016-06.xsl";

  // -- Cached stylesheets --

  /** Reordering stylesheet. */
  private static Templates reorderXSLT;

  /** Stylesheets for updating from previous schema releases. */
  private static Templates update2003FC;
  private static Templates update200706;
  private static Templates update200802;
  private static Templates update200809;
  private static Templates update200909;
  private static Templates update201004;
  private static Templates update201006;
  private static Templates update201106;
  private static Templates update201206;
  private static Templates update201306;
  private static Templates update201501;

  private static final String SCHEMA_PATH =
    "http://www.bhojpur.net/Schemas/ODE/";

  /**
   * The pattern of system ID URLs for ODE-XML schema definitions.
   */
  private static final Pattern SCHEMA_URL_PATTERN = Pattern.compile(
      "http://www.bhojpur.net/Schemas/" +
      "\\p{Alpha}+/(\\w+-\\w+)/(\\p{Alpha}+)\\.xsd");

  /**
   * Finds ODE-XML schema definitions in specifications.jar.
   */
  private static final XMLTools.SchemaReader SCHEMA_CLASSPATH_READER =
      new XMLTools.SchemaReader() {
        @Override
        public InputStream getSchemaAsStream(String url) {
          final Matcher matcher = SCHEMA_URL_PATTERN.matcher(url);
          if (matcher.matches()) {
            /* from specification.jar */
            return getClass().getResourceAsStream("/released-schema/" +
                 matcher.group(1) + "/" + matcher.group(2) + ".xsd");
          } else if(url.equals("http://www.w3.org/2001/xml.xsd")) {
            return getClass().getResourceAsStream(
              "/released-schema/external/xml.xsd");
          } else {
            return null;
          }
        }
      };

  /**
   * Default constructor.
   */
  public ODEXMLServiceImpl() {
    checkClassDependency(ode.xml.model.ODEModelObject.class);
  }

  /** @see ODEXMLService#getLatestVersion() */
  @Override
  public String getLatestVersion() {
    return LATEST_VERSION;
  }

  /** @see ODEXMLService#transformToLatestVersion(String) */
  @Override
  public String transformToLatestVersion(String xml) throws ServiceException {
    String version = getODEXMLVersion(xml);
    if (null == version) {
      throw new ServiceException("Could not get ODE-XML version");
    }
    if (version.equals(getLatestVersion())) return xml;
    LOGGER.debug("Attempting to update XML with version: {}", version);
    LOGGER.trace("Initial dump: {}", xml);

    String transformed = null;
    try {
      if (version.equals("2003-FC")) {
        xml = verifyODENamespace(xml);
        LOGGER.debug("Running UPDATE_2003FC stylesheet.");
        if (update2003FC == null) {
          update2003FC =
            XMLTools.getStylesheet(XSLT_2003FC, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(xml, update2003FC);
      }
      else if (version.equals("2007-06")) {
        xml = verifyODENamespace(xml);
        LOGGER.debug("Running UPDATE_200706 stylesheet.");
        if (update200706 == null) {
          update200706 =
            XMLTools.getStylesheet(XSLT_200706, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(xml, update200706);
      }
      else if (version.equals("2008-02")) {
        xml = verifyODENamespace(xml);
        LOGGER.debug("Running UPDATE_200802 stylesheet.");
        if (update200802 == null) {
          update200802 =
            XMLTools.getStylesheet(XSLT_200802, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(xml, update200802);
      }
      else transformed = xml;
      LOGGER.debug("XML updated to at least 2008-09");
      LOGGER.trace("At least 2008-09 dump: {}", transformed);

      if (version.compareTo("2009-09") < 0) {
        transformed = verifyODENamespace(transformed);
        LOGGER.debug("Running UPDATE_200809 stylesheet.");
        if (update200809 == null) {
          update200809 =
            XMLTools.getStylesheet(XSLT_200809, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(transformed, update200809);
      }
      LOGGER.debug("XML updated to at least 2009-09");
      LOGGER.trace("At least 2009-09 dump: {}", transformed);

      if (version.compareTo("2010-04") < 0) {
        transformed = verifyODENamespace(transformed);
        LOGGER.debug("Running UPDATE_200909 stylesheet.");
        if (update200909 == null) {
          update200909 =
            XMLTools.getStylesheet(XSLT_200909, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(transformed, update200909);
      }
      else transformed = xml;
      LOGGER.debug("XML updated to at least 2010-04");
      LOGGER.trace("At least 2010-04 dump: {}", transformed);

      if (version.compareTo("2010-06") < 0) {
        transformed = verifyODENamespace(transformed);
        LOGGER.debug("Running UPDATE_201004 stylesheet.");
        if (update201004 == null) {
          update201004 =
            XMLTools.getStylesheet(XSLT_201004, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(transformed, update201004);
      }
      else transformed = xml;
      LOGGER.debug("XML updated to at least 2010-06");

      if (version.compareTo("2011-06") < 0) {
        transformed = verifyODENamespace(transformed);
        LOGGER.debug("Running UPDATE_201006 stylesheet.");
        if (update201006 == null) {
          update201006 =
            XMLTools.getStylesheet(XSLT_201006, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(transformed, update201006);
      }
      else transformed = xml;
      LOGGER.debug("XML updated to at least 2011-06");

      if (version.compareTo("2012-06") < 0) {
        transformed = verifyODENamespace(transformed);
        LOGGER.debug("Running UPDATE_201106 stylesheet.");
        if (update201106 == null) {
          update201106 =
            XMLTools.getStylesheet(XSLT_201106, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(transformed, update201106);
      }
      else transformed = xml;
      LOGGER.debug("XML updated to at least 2012-06");

      if (version.compareTo("2013-06") < 0) {
        transformed = verifyODENamespace(transformed);
        LOGGER.debug("Running UPDATE_201206 stylesheet.");
        if (update201206 == null) {
          update201206 =
            XMLTools.getStylesheet(XSLT_201206, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(transformed, update201206);
      }
      else transformed = xml;
      LOGGER.debug("XML updated to at least 2013-06");

      if (version.compareTo("2015-01") < 0) {
        transformed = verifyODENamespace(transformed);
        LOGGER.debug("Running UPDATE_201306 stylesheet.");
        if (update201306 == null) {
          update201306 =
            XMLTools.getStylesheet(XSLT_201306, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(transformed, update201306);
      }
      else transformed = xml;
      LOGGER.debug("XML updated to at least 2015-01");

      if (version.compareTo("2016-06") < 0) {
        transformed = verifyODENamespace(transformed);
        LOGGER.debug("Running UPDATE_201501 stylesheet.");
        if (update201501 == null) {
          update201501 =
            XMLTools.getStylesheet(XSLT_201501, ODEXMLServiceImpl.class);
        }
        transformed = XMLTools.transformXML(transformed, update201501);
      }
      else transformed = xml;
      LOGGER.debug("XML updated to at least 2016-06");

      // fix namespaces
      transformed = transformed.replaceAll("<ns.*?:", "<");
      transformed = transformed.replaceAll("xmlns:ns.*?=", "xmlns:ODE=");
      transformed = transformed.replaceAll("</ns.*?:", "</");
      LOGGER.trace("Transformed XML dump: {}", transformed);
      return transformed;
    }
    catch (IOException e) {
      LOGGER.warn("Could not transform version " + version + " ODE-XML.");
    }
    return null;
  }

  /** @see ODEXMLService#createODEXMLMetadata() */
  @Override
  public ODEXMLMetadata createODEXMLMetadata() throws ServiceException {
    return createODEXMLMetadata(null);
  }

  /** @see ODEXMLService#createODEXMLMetadata(java.lang.String) */
  @Override
  public ODEXMLMetadata createODEXMLMetadata(String xml)
    throws ServiceException {
    return createODEXMLMetadata(xml, null);
  }

  /**
   * @see ODEXMLService#createODEXMLMetadata(java.lang.String, java.lang.String)
   */
  @Override
  public ODEXMLMetadata createODEXMLMetadata(String xml, String version)
    throws ServiceException {
    if (xml != null) {
      xml = XMLTools.sanitizeXML(xml);
    }
    ODEXMLMetadataRoot ode =
      xml == null ? null : createRoot(transformToLatestVersion(xml));

    ODEXMLMetadata meta = new ODEPyramidStore();
    if (ode != null) meta.setRoot(ode);
    return meta;
  }

  /** @see ODEXMLService#createODEXMLRoot(java.lang.String) */
  @Override
  public ODEModelObject createODEXMLRoot(String xml) throws ServiceException {
    return createRoot(transformToLatestVersion(xml));
  }

  /** @see ODEXMLService#isODEXMLMetadata(java.lang.Object) */
  @Override
  public boolean isODEXMLMetadata(Object o) {
    return o instanceof ODEXMLMetadata;
  }

  /** @see ODEXMLService#isODEXMLRoot(java.lang.Object) */
  @Override
  public boolean isODEXMLRoot(Object o) {
    return o instanceof ODEModelObject;
  }

  /**
   * Constructs an ODE root node. <b>NOTE:</b> This method is mostly here to
   * ensure type safety of return values as instances of service dependency
   * classes should not leak out of the interface.
   * @param xml String of XML to create the root node from.
   * @return An ode.xml.model.ODEModelObject subclass root node.
   * @throws IOException If there is an error reading from the string.
   * @throws SAXException If there is an error parsing the XML.
   * @throws ParserConfigurationException If there is an error preparing the
   * parsing infrastructure.
   */
  private ODEXMLMetadataRoot createRoot(String xml) throws ServiceException {
    try {
      ODEModel model = new ODEModelImpl();
      ODEXMLMetadataRoot ode = new ODEXMLMetadataRoot(XMLTools.parseDOM(xml).getDocumentElement(), model);
      model.resolveReferences();
      return ode;
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  /** @see ODEXMLService#getODEXMLVersion(java.lang.Object) */
  @Override
  public String getODEXMLVersion(Object o) {
    if (o == null) return null;
    if (o instanceof ODEXMLMetadata || o instanceof ODEModelObject) {
      return LATEST_VERSION;
    }
    else if (o instanceof String) {
      String xml = (String) o;
      try {
        Element e = XMLTools.parseDOM(xml).getDocumentElement();
        String namespace = e.getAttribute("xmlns");
        if (namespace == null || namespace.equals("")) {
          namespace = e.getAttribute("xmlns:ode");
        }
        if (namespace == null || namespace.equals("")) {
          namespace = e.getAttribute("xmlns:ODE");
        }

        return namespace.endsWith("ode.xsd") ? "2003-FC" :
          namespace.substring(namespace.lastIndexOf("/") + 1);
      }
      catch (ParserConfigurationException pce) { }
      catch (SAXException se) { }
      catch (IOException ioe) { }
    }
    return null;
  }

  /** @see ODEXMLService#getODEMetadata(loci.formats.meta.MetadataRetrieve) */
  @Override
  public ODEXMLMetadata getODEMetadata(MetadataRetrieve src)
    throws ServiceException {
    // check if the metadata is already an ODE-XML metadata object
    if (src instanceof ODEXMLMetadata) return (ODEXMLMetadata) src;

    // populate a new ODE-XML metadata object with metadata
    // converted from the non-ODE-XML metadata object
    ODEXMLMetadata odexmlMeta = createODEXMLMetadata();
    convertMetadata(src, odexmlMeta);
    return odexmlMeta;
  }

  /** @see ODEXMLService#getODEXML(loci.formats.meta.MetadataRetrieve) */
  @Override
  public String getODEXML(MetadataRetrieve src) throws ServiceException {
    ODEXMLMetadata odexmlMeta = getODEMetadata(src);
    String xml = odexmlMeta.dumpXML();

    // make sure that the namespace has been set correctly

    // convert XML string to DOM
    Document doc = null;
    Exception exception = null;
    try {
      doc = XMLTools.parseDOM(xml);
    }
    catch (ParserConfigurationException exc) { exception = exc; }
    catch (SAXException exc) { exception = exc; }
    catch (IOException exc) { exception = exc; }
    if (exception != null) {
      LOGGER.info("Malformed ODE-XML", exception);
      return null;
    }

    Element root = doc.getDocumentElement();
    root.setAttribute("xmlns", SCHEMA_PATH + getLatestVersion());

    // convert tweaked DOM back to XML string
    try {
      xml = XMLTools.getXML(doc);
    }
    catch (TransformerConfigurationException exc) { exception = exc; }
    catch (TransformerException exc) { exception = exc; }
    if (exception != null) {
      LOGGER.info("Internal XML conversion error", exception);
      return null;
    }

    return xml;
  }

  /** @see ODEXMLService#validateODEXML(java.lang.String) */
  @Override
  public boolean validateODEXML(String xml) {
    return validateODEXML(xml, false);
  }

  /** @see ODEXMLService#validateODEXML(java.lang.String, boolean) */
  @Override
  public boolean validateODEXML(String xml, boolean pixelsHack) {
    // HACK: Inject a TiffData element beneath any childless Pixels elements.
    if (pixelsHack) {
      // convert XML string to DOM
      Document doc = null;
      Exception exception = null;
      try {
        doc = XMLTools.parseDOM(xml);
      }
      catch (ParserConfigurationException exc) { exception = exc; }
      catch (SAXException exc) { exception = exc; }
      catch (IOException exc) { exception = exc; }
      if (exception != null) {
        LOGGER.info("Malformed ODE-XML", exception);
        return false;
      }

      // inject TiffData elements as needed
      NodeList list = doc.getElementsByTagName("Pixels");
      for (int i=0; i<list.getLength(); i++) {
        Node node = list.item(i);
        NodeList children = node.getChildNodes();
        boolean needsTiffData = true;
        for (int j=0; j<children.getLength(); j++) {
          Node child = children.item(j);
          String name = child.getLocalName();
          if ("TiffData".equals(name) || "BinData".equals(name)) {
            needsTiffData = false;
            break;
          }
        }
        if (needsTiffData) {
          // inject TiffData element
          Node tiffData = doc.createElement("TiffData");
          node.insertBefore(tiffData, node.getFirstChild());
        }
      }

      // convert tweaked DOM back to XML string
      try {
        xml = XMLTools.getXML(doc);
      }
      catch (TransformerConfigurationException exc) { exception = exc; }
      catch (TransformerException exc) { exception = exc; }
      if (exception != null) {
        LOGGER.info("Internal XML conversion error", exception);
        return false;
      }
    }

    // remove any Modulo annotations for validation
    // the "Label" attribute on Modulo conflicts with the "Label" shape
    // and will cause validation errors

    try {
      ODEXMLMetadata odexml = createODEXMLMetadata(xml);
      ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexml.getRoot();
      for (int image=0; image<root.sizeOfImageList(); image++) {
        Image img = root.getImage(image);

        for (int i=0; i<img.sizeOfLinkedAnnotationList(); i++) {
          Annotation annotation = img.getLinkedAnnotation(i);
          if (!(annotation instanceof XMLAnnotation)) {
            continue;
          }

          String annotationXML = ((XMLAnnotation) annotation).getValue();

          Document annotationRoot = XMLTools.parseDOM(annotationXML);
          NodeList nodes = annotationRoot.getElementsByTagName("ModuloAlongZ");

          if (nodes.getLength() > 0) {
            ((XMLAnnotation) annotation).setValue("");
          }
          nodes = annotationRoot.getElementsByTagName("ModuloAlongC");
          if (nodes.getLength() > 0) {
            ((XMLAnnotation) annotation).setValue("");
          }
          nodes = annotationRoot.getElementsByTagName("ModuloAlongT");
          if (nodes.getLength() > 0) {
            ((XMLAnnotation) annotation).setValue("");
          }
        }
      }
      odexml.setRoot(root);
      xml = getODEXML(odexml);
    }
    catch (ServiceException|ParserConfigurationException|SAXException|IOException e) {
      LOGGER.warn("Could not remove Modulo annotations", e);
    }

    return XMLTools.validateXML(xml, "ODE-XML", SCHEMA_CLASSPATH_READER);
  }

  /**
   * @see ODEXMLService#getModuloAlongZ(ODEXMLMetadata, int)
   */
  @Override
  public Modulo getModuloAlongZ(ODEXMLMetadata odexml, int image) {
    return getModuloAlong(odexml, "ModuloAlongZ", image);
  }

  /**
   * @see ODEXMLService#getModuloAlongC(ODEXMLMetadata, int)
   */
  @Override
  public Modulo getModuloAlongC(ODEXMLMetadata odexml, int image) {
    return getModuloAlong(odexml, "ModuloAlongC", image);
  }

  /**
   * @see ODEXMLService#getModuloAlongT(ODEXMLMetadata, int)
   */
  @Override
  public Modulo getModuloAlongT(ODEXMLMetadata odexml, int image) {
    return getModuloAlong(odexml, "ModuloAlongT", image);
  }

  /**
   * Create a {@link loci.formats.Modulo} corresponding to the given ModuloAlong* tag.
   * @param odexml the ODEXMLMetadata from which to retrieve the ModuloAlong* tag
   * @param tag the tag name (e.g. "ModuloAlongC")
   * @param image the Image index within the ODEXMLMetadata
   * @return the corresponding Modulo object
   */
  private Modulo getModuloAlong(ODEXMLMetadata odexml, String tag, int image) {
    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexml.getRoot();
    Image img = root.getImage(image);
    if (img == null) {
      return null;
    }

    for (int i=0; i<img.sizeOfLinkedAnnotationList(); i++) {
      Annotation annotation = img.getLinkedAnnotation(i);
      if (!(annotation instanceof XMLAnnotation)) {
        continue;
      }

      String xml = ((XMLAnnotation) annotation).getValue();

      try {
        Document annotationRoot = XMLTools.parseDOM(xml);
        NodeList nodes = annotationRoot.getElementsByTagName(tag);

        if (nodes.getLength() > 0) {
          Element modulo = (Element) nodes.item(0);
          NamedNodeMap attrs = modulo.getAttributes();

          Modulo m = new Modulo(tag.substring(tag.length() - 1));

          Node start = attrs.getNamedItem("Start");
          Node end = attrs.getNamedItem("End");
          Node step = attrs.getNamedItem("Step");
          Node type = attrs.getNamedItem("Type");
          Node typeDescription = attrs.getNamedItem("TypeDescription");
          Node unit = attrs.getNamedItem("Unit");

          if (start != null) {
            m.start = Double.parseDouble(start.getNodeValue());
          }
          if (end != null) {
            m.end = Double.parseDouble(end.getNodeValue());
          }
          if (step != null) {
            m.step = Double.parseDouble(step.getNodeValue());
          }
          if (type != null) {
            m.type = type.getNodeValue();
          }
          if (typeDescription != null) {
            m.typeDescription = typeDescription.getNodeValue();
          }
          if (unit != null) {
            m.unit = unit.getNodeValue();
          }

          NodeList labels = modulo.getElementsByTagName("Label");
          if (labels != null && labels.getLength() > 0) {
            m.labels = new String[labels.getLength()];
            for (int q=0; q<labels.getLength(); q++) {
              m.labels[q] = labels.item(q).getTextContent();
            }
          }

          return m;
        }
      }
      catch (ParserConfigurationException e) {
        LOGGER.debug("Failed to parse ModuloAlong", e);
      }
      catch (SAXException e) {
        LOGGER.debug("Failed to parse ModuloAlong", e);
      }
      catch (IOException e) {
        LOGGER.debug("Failed to parse ModuloAlong", e);
      }
    }
    return null;
  }

  /**
   * @see ODEXMLService#getOriginalMetadata(loci.formats.ode.ODEXMLMetadata)
   */
  @Override
  public Hashtable getOriginalMetadata(ODEXMLMetadata odexmlMeta) {
    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexmlMeta.getRoot();
    StructuredAnnotations annotations = root.getStructuredAnnotations();
    if (annotations == null) {
      return null;
    }

    Hashtable metadata = new Hashtable();

    for (int i=0; i<annotations.sizeOfXMLAnnotationList(); i++) {
      XMLAnnotation annotation = annotations.getXMLAnnotation(i);

      if (annotation instanceof OriginalMetadataAnnotation) {
        OriginalMetadataAnnotation original =
          (OriginalMetadataAnnotation) annotation;
        metadata.put(original.getKey(), original.getValueForKey());
        continue;
      }

      String xml = annotation.getValue();

      try {
        Document annotationRoot = XMLTools.parseDOM(xml);
        NodeList metadataNodes =
          annotationRoot.getElementsByTagName("OriginalMetadata");

        for (int meta=0; meta<metadataNodes.getLength(); meta++) {
          Element metadataNode = (Element) metadataNodes.item(meta);
          NodeList keys =
            metadataNode.getElementsByTagName("Key");
          NodeList values =
            metadataNode.getElementsByTagName("Value");

          for (int q=0; q<keys.getLength(); q++) {
            Node key = keys.item(q);
            Node value = values.item(q);

            metadata.put(key.getTextContent(), value.getTextContent());
          }
        }

        if (metadataNodes.getLength() == 0) {
          metadataNodes = annotationRoot.getDocumentElement().getChildNodes();

          for (int meta=0; meta<metadataNodes.getLength(); meta++) {
            // Not all nodes will be instances of Element, particularly if
            // the XML parsing was done by Xerces.  In particular, if a
            // comment is found, it will be an instance of
            // com.sun.org.apache.xerces.internal.dom.DeferredCommentImpl.
            if (metadataNodes.item(meta) instanceof Element) {
              Element node = (Element) metadataNodes.item(meta);
              String name = node.getNodeName();

              NamedNodeMap attrs = node.getAttributes();
              Node value = attrs.getNamedItem("Value");
              if (value != null) {
                metadata.put(name, value.getNodeValue());
              }
            }
          }
        }
      }
      catch (ParserConfigurationException e) {
        LOGGER.debug("Failed to parse OriginalMetadata", e);
      }
      catch (SAXException e) {
        LOGGER.debug("Failed to parse OriginalMetadata", e);
      }
      catch (IOException e) {
        LOGGER.debug("Failed to parse OriginalMetadata", e);
      }
    }

    return metadata;
  }

  /**
   * @see ODEXMLService#populateOriginalMetadata(loci.formats.ode.ODEXMLMetadata, Hashtable)
   */
  @Override
  public void populateOriginalMetadata(ODEXMLMetadata odexmlMeta,
    Hashtable<String, Object> metadata)
  {
    odexmlMeta.resolveReferences();

    if (metadata.size() == 0) {
      return;
    }

    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexmlMeta.getRoot();
    StructuredAnnotations annotations = root.getStructuredAnnotations();
    if (annotations == null) annotations = new StructuredAnnotations();
    int annotationIndex = annotations.sizeOfXMLAnnotationList();

    if (annotationIndex > 0) {
      String lastAnnotationID =
        odexmlMeta.getXMLAnnotationID(annotationIndex - 1);
      String lastIndex =
        lastAnnotationID.substring(lastAnnotationID.lastIndexOf(":") + 1);
      try {
        int index = Integer.parseInt(lastIndex);
        while (index >= annotationIndex) {
          annotationIndex++;
        }
      }
      catch (NumberFormatException e) { }
    }

    for (String key : metadata.keySet()) {
      OriginalMetadataAnnotation annotation = new OriginalMetadataAnnotation();
      annotation.setID(MetadataTools.createLSID("Annotation", annotationIndex));
      annotation.setKeyValue(key, metadata.get(key).toString());
      annotations.addXMLAnnotation(annotation);
      annotationIndex++;
    }

    root.setStructuredAnnotations(annotations);
    odexmlMeta.setRoot(root);
  }

  /**
   * @see ODEXMLService#populateOriginalMetadata(loci.formats.ode.ODEXMLMetadata, java.lang.String, java.lang.String)
   */
  @Override
  public void populateOriginalMetadata(ODEXMLMetadata odexmlMeta,
    String key, String value)
  {
    odexmlMeta.resolveReferences();
    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexmlMeta.getRoot();
    StructuredAnnotations annotations = root.getStructuredAnnotations();
    if (annotations == null) annotations = new StructuredAnnotations();
    int annotationIndex = annotations.sizeOfXMLAnnotationList();

    if (annotationIndex > 0) {
     String lastAnnotationID =
        odexmlMeta.getXMLAnnotationID(annotationIndex - 1);
      String lastIndex =
        lastAnnotationID.substring(lastAnnotationID.lastIndexOf(":") + 1);
      try {
        int index = Integer.parseInt(lastIndex);
        while (index >= annotationIndex) {
          annotationIndex++;
        }
      }
      catch (NumberFormatException e) { }
    }

    OriginalMetadataAnnotation annotation = new OriginalMetadataAnnotation();
    annotation.setID(MetadataTools.createLSID("Annotation", annotationIndex));
    annotation.setKeyValue(key, value);
    annotations.addXMLAnnotation(annotation);

    root.setStructuredAnnotations(annotations);
    odexmlMeta.setRoot(root);
  }

  /**
   * @see ODEXMLService#convertMetadata(java.lang.String, loci.formats.meta.MetadataStore)
   */
  @Override
  public void convertMetadata(String xml, MetadataStore dest)
    throws ServiceException {
    ODEXMLMetadataRoot ode = createRoot(transformToLatestVersion(xml));
    String rootVersion = getODEXMLVersion(ode);
    String storeVersion = getODEXMLVersion(dest);
    if (rootVersion.equals(storeVersion)) {
      // metadata store is already an ODE-XML metadata object of the
      // correct schema version; populate ODE-XML string directly
      if (!(dest instanceof ODEXMLMetadata)) {
        throw new IllegalArgumentException(
            "Expecting ODEXMLMetadata instance.");
      }
      dest.setRoot(ode);
    }
    else {
      // metadata store is incompatible; create an ODE-XML
      // metadata object and copy it into the destination
      IMetadata src = createODEXMLMetadata(xml);
      convertMetadata(src, dest);

      // make sure that physical sizes are corrected
      for (int image=0; image<src.getImageCount(); image++) {
        Length physicalSizeX = src.getPixelsPhysicalSizeX(image);
        if (physicalSizeX != null && physicalSizeX.value() != null) {
          physicalSizeX = FormatTools.getPhysicalSize(physicalSizeX.value().doubleValue(), physicalSizeX.unit().getSymbol());
          dest.setPixelsPhysicalSizeX(physicalSizeX, image);
        }
        Length physicalSizeY = src.getPixelsPhysicalSizeY(image);
        if (physicalSizeY != null && physicalSizeY.value() != null) {
          physicalSizeY = FormatTools.getPhysicalSize(physicalSizeY.value().doubleValue(), physicalSizeY.unit().getSymbol());
          dest.setPixelsPhysicalSizeY(physicalSizeY, image);
        }
        Length physicalSizeZ = src.getPixelsPhysicalSizeZ(image);
        if (physicalSizeZ != null && physicalSizeZ.value() != null) {
          physicalSizeZ = FormatTools.getPhysicalSize(physicalSizeZ.value().doubleValue(), physicalSizeZ.unit().getSymbol());
          dest.setPixelsPhysicalSizeZ(physicalSizeZ, image);
        }
      }
    }
  }

  /**
   * @see ODEXMLService#convertMetadata(loci.formats.meta.MetadataRetrieve, loci.formats.meta.MetadataStore)
   */
  @Override
  public void convertMetadata(MetadataRetrieve src, MetadataStore dest) {
    MetadataConverter.convertMetadata(src, dest);
  }

  /** @see ODEXMLService#removeBinData(ODEXMLMetadata) */
  @Override
  public void removeBinData(ODEXMLMetadata odexmlMeta) {
    odexmlMeta.resolveReferences();
    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexmlMeta.getRoot();
    List<Image> images = root.copyImageList();
    for (Image img : images) {
      Pixels pix = img.getPixels();
      List<BinData> binData = pix.copyBinDataList();
      for (BinData bin : binData) {
        pix.removeBinData(bin);
      }
      pix.setMetadataOnly(null);
    }
    odexmlMeta.setRoot(root);
  }

  /** @see ODEXMLService#removeTiffData(ODEXMLMetadata) */
  @Override
  public void removeTiffData(ODEXMLMetadata odexmlMeta) {
    odexmlMeta.resolveReferences();
    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexmlMeta.getRoot();
    List<Image> images = root.copyImageList();
    for (Image img : images) {
      Pixels pix = img.getPixels();
      List<TiffData> tiffData = pix.copyTiffDataList();
      for (TiffData tiff : tiffData) {
        pix.removeTiffData(tiff);
      }
      pix.setMetadataOnly(null);
    }
    odexmlMeta.setRoot(root);
  }

  /** @see ODEXMLService#removeChannels(ODEXMLMetadata, int, int) */
  @Override
  public void removeChannels(ODEXMLMetadata odexmlMeta, int image, int sizeC) {
    odexmlMeta.resolveReferences();
    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexmlMeta.getRoot();
    Pixels img = root.getImage(image).getPixels();
    List<Channel> channels = img.copyChannelList();

    for (int c=0; c<channels.size(); c++) {
      Channel channel = channels.get(c);
      if (channel.getID() == null || c >= sizeC) {
        img.removeChannel(channel);
      }
    }
    odexmlMeta.setRoot(root);
  }

  /** @see ODEXMLService#addMetadataOnly(ODEXMLMetadata, int) */
  @Override
  public void addMetadataOnly(ODEXMLMetadata odexmlMeta, int image) {
    addMetadataOnly(odexmlMeta, image, true);
  }

  /** @see ODEXMLService#addMetadataOnly(ODEXMLMetadata, int, boolean) */
  public void addMetadataOnly(ODEXMLMetadata odexmlMeta, int image,
    boolean resolve)
  {
    if (resolve) {
      odexmlMeta.resolveReferences();
    }
    MetadataOnly meta = new MetadataOnly();
    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexmlMeta.getRoot();
    Pixels pix = root.getImage(image).getPixels();
    pix.setMetadataOnly(meta);
  }

  /** @see ODEXMLService#isEqual(ODEXMLMetadata, ODEXMLMetadata) */
  @Override
  public boolean isEqual(ODEXMLMetadata src1, ODEXMLMetadata src2) {
    src1.resolveReferences();
    src2.resolveReferences();
 
    ODEXMLMetadataRoot odeRoot1 = (ODEXMLMetadataRoot) src1.getRoot();
    ODEXMLMetadataRoot odeRoot2 = (ODEXMLMetadataRoot) src2.getRoot();

    DocumentBuilder builder = null;
    try {
      builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
    catch (ParserConfigurationException e) {
      return false;
    }

    Document doc1 = builder.newDocument();
    Document doc2 = builder.newDocument();

    Element root1 = odeRoot1.asXMLElement(doc1);
    Element root2 = odeRoot2.asXMLElement(doc2);

    return equals(root1, root2);
  }

  @Override
  public void addModuloAlong(ODEXMLMetadata meta, CoreMetadata core, int imageIdx)
  {
    meta.resolveReferences();

    if (core.moduloZ.length() == 1 && core.moduloC.length() == 1 &&
      core.moduloT.length() == 1)
    {
      // nothing to populate
      return;
    }

    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) meta.getRoot();
    Image image;
    try {
      image = root.getImage(imageIdx);
    } catch (IndexOutOfBoundsException ieeb) {
      // If there is no image of the given index,
      // then we are considering the metadata corrupt,
      // and exiting without doing anything.
      return;
    }
    StructuredAnnotations annotations = root.getStructuredAnnotations();
    if (annotations == null) annotations = new StructuredAnnotations();
    int annotationIndex = annotations.sizeOfXMLAnnotationList();

    final Set<String> knownModulos = new HashSet<String>();
    if (annotationIndex > 0) {
     // Check which modulo annotations are already present.
     for (int idx = 0; idx < annotationIndex; idx++) {
       if (ModuloAnnotation.MODULO_NS.equals(
         meta.getXMLAnnotationNamespace(idx))) {

         // ignore this annotation if it is not linked to the current Image
         boolean ignore = true;
         String xmlID = meta.getXMLAnnotationID(idx);
         for (int link=0; link<image.sizeOfLinkedAnnotationList(); link++) {
           if (xmlID.equals(image.getLinkedAnnotation(link).getID()))
           {
             ignore = false;
             break;
           }
         }
         if (ignore) {
           continue;
         }

         String value = meta.getXMLAnnotationValue(idx);
         try {
           Document doc = XMLTools.parseDOM(value);
           NodeList modulos = doc.getElementsByTagName("Modulo");
           for (int m = 0; m < modulos.getLength(); m++) {
             Node modulo = modulos.item(m);
             NodeList children = modulo.getChildNodes();
             for (int c = 0; c < children.getLength(); c++) {
               Node child = children.item(c);
               String name = child.getNodeName();
               knownModulos.add(name);
             }
           }
         } catch (Exception e) {
           LOGGER.warn("Could not parse XML from annotation: {}", value, e);
         }
       }
     }

     // Calculate the next annotation ID that should be used.
     String lastAnnotationID = meta.getXMLAnnotationID(annotationIndex - 1);
      String lastIndex =
        lastAnnotationID.substring(lastAnnotationID.lastIndexOf(":") + 1);
      try {
        int index = Integer.parseInt(lastIndex);
        while (index >= annotationIndex) {
          annotationIndex++;
        }
      }
      catch (NumberFormatException e) { }
    }

    int imageAnnotation = 0;

    if (core.moduloZ.length() > 1 && !knownModulos.contains("ModuloAlongZ")) {
      createModulo(meta, core.moduloZ,
        annotations, image, imageIdx, annotationIndex, imageAnnotation);
      annotationIndex++;
      imageAnnotation++;
    }

    if (core.moduloC.length() > 1 && !knownModulos.contains("ModuloAlongC")) {
        createModulo(meta, core.moduloC,
          annotations, image, imageIdx, annotationIndex, imageAnnotation);
        annotationIndex++;
        imageAnnotation++;
    }

    if (core.moduloT.length() > 1 && !knownModulos.contains("ModuloAlongT")) {
      createModulo(meta, core.moduloT,
        annotations, image, imageIdx, annotationIndex, imageAnnotation);
      annotationIndex++;
      imageAnnotation++;
    }

    root.setStructuredAnnotations(annotations);
    meta.setRoot(root);
  }

  /**
   * Create a ModuloAlong* annotation corresponding to the given
   * {@link loci.formats.Modulo}.
   * @param meta the ODEXMLMetadata in which to create the annotation
   * @param modulo the Modulo object that contains the annotation data
   * @param annotations the list of existing annotations
   * @param image the Image to which the new annotation should be linked
   * @param imageIdx the index of the Image
   * @param annotationIndex the index to be assigned to the new annotation
   * @param imageAnnotation the index to be assigned to the new annotation link
   */
  private void createModulo(
          final ODEXMLMetadata meta,
          final Modulo modulo,
          final StructuredAnnotations annotations,
          final Image image,
          final int imageIdx,
          final int annotationIndex,
          final int imageAnnotation) {
    ModuloAnnotation annotation = new ModuloAnnotation();
    annotation.setModulo(meta, modulo);
    String id = MetadataTools.createLSID("Annotation", annotationIndex);
    annotation.setID(id);
    annotations.addXMLAnnotation(annotation);
    meta.setImageAnnotationRef(id, imageIdx, imageAnnotation);
    image.linkAnnotation(annotation);
  }

  // -- Utility methods - casting --

  /** @see ODEXMLService#asStore(loci.formats.meta.MetadataRetrieve) */
  @Override
  public MetadataStore asStore(MetadataRetrieve meta) {
    return meta instanceof MetadataStore ? (MetadataStore) meta : null;
  }

  /** @see ODEXMLService#asRetrieve(loci.formats.meta.MetadataStore) */
  @Override
  public MetadataRetrieve asRetrieve(MetadataStore meta) {
    return meta instanceof MetadataRetrieve ? (MetadataRetrieve) meta : null;
  }

  // -- Helper methods --

  /** Ensures that an xmlns:ode element exists. */
  private String verifyODENamespace(String xml) {
    try {
      Document doc = XMLTools.parseDOM(xml);
      Element e = doc.getDocumentElement();
      String odeNamespace = e.getAttribute("xmlns:ode");
      if (odeNamespace == null || odeNamespace.equals("")) {
        e.setAttribute("xmlns:ode", e.getAttribute("xmlns"));
      }
      return XMLTools.getXML(doc);
    }
    catch (ParserConfigurationException pce) { }
    catch (TransformerConfigurationException tce) { }
    catch (TransformerException te) { }
    catch (SAXException se) { }
    catch (IOException ioe) { }
    return null;
  }

  /** Compares two Elements for equality. */
  public boolean equals(Node e1, Node e2) {
    NodeList children1 = e1.getChildNodes();
    NodeList children2 = e2.getChildNodes();

    String localName1 = e1.getLocalName();
    if (localName1 == null) {
      localName1 = "";
    }
    String localName2 = e2.getLocalName();
    if (localName2 == null) {
      localName2 = "";
    }
    if (!localName1.equals(localName2)) {
      return false;
    }

    if (localName1.equals("StructuredAnnotations")) {
      // we don't care about StructuredAnnotations at all
      return true;
    }

    NamedNodeMap attributes1 = e1.getAttributes();
    NamedNodeMap attributes2 = e2.getAttributes();

    if (attributes1 == null || attributes2 == null) {
      if ((attributes1 == null && attributes2 != null) ||
        (attributes1 != null && attributes2 == null))
      {
        return false;
      }
    }
    else if (attributes1.getLength() != attributes2.getLength()) {
      return false;
    }
    else {
      // make sure that all of the attributes are equal, except for IDs

      int nAttributes = attributes1.getLength();

      for (int i=0; i<nAttributes; i++) {
        Node n1 = attributes1.item(i);
        String localName = n1.getNodeName();

        if (localName != null && !localName.equals("ID")) {
          Node n2 = attributes2.getNamedItem(localName);
          if (n2 == null) {
            return false;
          }

          if (!equals(n1, n2)) {
            return false;
          }
        }
        else if ("ID".equals(localName)) {
          if (localName1.endsWith("Settings")) {
            // this is a reference to a different node
            // the references are equal if the two referenced nodes are equal

            Node n2 = attributes2.getNamedItem(localName);

            Node realRoot1 = findRootNode(e1);
            Node realRoot2 = findRootNode(e2);

            String refName = localName1.replaceAll("Settings", "");

            Node ref1 = findChildWithID(realRoot1, refName, n1.getNodeValue());
            Node ref2 = findChildWithID(realRoot2, refName, n2.getNodeValue());

            if (ref1 == null && ref2 == null) {
              return true;
            }
            else if ((ref1 == null && ref2 != null) ||
              (ref1 != null && ref2 == null) || !equals(ref1, ref2))
            {
              return false;
            }
          }
        }
      }
    }

    if (children1.getLength() != children2.getLength()) {
      return false;
    }

    Object node1 = e1.getNodeValue();
    Object node2 = e2.getNodeValue();

    if (node1 == null && node2 != null) {
      return false;
    }
    if (node1 != null && !node1.equals(node2) && !localName1.equals("")) {
      return false;
    }

    for (int i=0; i<children1.getLength(); i++) {
      if (!equals(children1.item(i), children2.item(i))) {
        return false;
      }
    }
    return true;
  }

  /** Return the absolute root node for the specified child node. */
  private Node findRootNode(Node child) {
    if (child.getParentNode() != null) {
      return findRootNode(child.getParentNode());
    }
    return child;
  }

  /** Return the child node with specified value for the "ID" attribute. */
  private Node findChildWithID(Node root, String name, String id) {
    NamedNodeMap attributes = root.getAttributes();
    if (attributes != null) {
      Node idNode = attributes.getNamedItem("ID");

      if (idNode != null && id.equals(idNode.getNodeValue()) &&
        name.equals(root.getNodeName()))
      {
        return root;
      }
    }

    NodeList children = root.getChildNodes();

    for (int i=0; i<children.getLength(); i++) {
      Node result = findChildWithID(children.item(i), name, id);
      if (result != null) {
        return result;
      }
    }

    return null;
  }

}
