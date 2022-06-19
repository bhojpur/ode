package loci.common.xml;

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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import loci.common.Constants;
import loci.common.RandomAccessInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A utility class for working with XML.
 */
public final class XMLTools {

  // -- Constants --

  static final Logger LOGGER = LoggerFactory.getLogger(XMLTools.class);

  private static final String XSI_NS =
    "http://www.w3.org/2001/XMLSchema-instance";

  private static final String XML_SCHEMA_PATH =
    "http://www.w3.org/2001/XMLSchema";

  private static final TransformerFactory transformFactory = createTransformFactory();

  private static TransformerFactory createTransformFactory() {
    TransformerFactory factory = TransformerFactory.newInstance();
    factory.setErrorListener(new XMLListener());
    return factory;
  };

  // -- Interfaces --

  /**
   * A schema reader can provide {@link InputStream}s for certain XML schemas.
   */
  public interface SchemaReader {

    /**
     * Get the given XML schema definition as an {@link InputStream}.
     * @param systemId the system ID of the sought XML schema definition
     * @return a stream from which to read the schema,
     * or {@code null} if one is not available
     */
    InputStream getSchemaAsStream(String systemId);
  }

  // -- Fields --

  private static ThreadLocal<HashMap<URI, Schema>> schemas =
    new ThreadLocal<HashMap<URI, Schema>>()
  {
    @Override
    protected HashMap<URI, Schema> initialValue() {
      return new HashMap<URI, Schema>();
    }
  };

  // -- Constructor --

  private XMLTools() { }

  // -- XML to/from DOM --

  /**
   * Creates a new {@link DocumentBuilder} via {@link DocumentBuilderFactory}
   * or logs and throws a {@link RuntimeException}.
   *
   * @return the default {@link DocumentBuilder} instance
   */
  public static DocumentBuilder createBuilder() {
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
    catch (ParserConfigurationException e) {
      LOGGER.error("Cannot create DocumentBuilder", e);
      throw new RuntimeException(e);
    }
  }

  /**
   * Calls {@link DocumentBuilder#newDocument()} on a
   * {@link #createBuilder() new builder}.
   *
   * @return an empty {@link Document}
   */
  public static Document createDocument() {
    return createBuilder().newDocument();
  }

  /**
   * Parses a DOM from the given XML file on disk.
   *
   * @param file a file on disk that contains XML
   * @return a {@link Document} reflecting the contents of the file
   * @throws ParserConfigurationException if the XML parser cannot be created
   * @throws SAXException if there is an error parsing the XML
   * @throws IOException if there is an error reading from the file
   */
  public static Document parseDOM(File file)
    throws ParserConfigurationException, SAXException, IOException
  {
    InputStream is = new FileInputStream(file);
    try {
      Document doc = parseDOM(is);
      return doc;
    } finally {
      is.close();
    }
  }

  /**
   * Parses a DOM from the given XML string.
   *
   * @param xml XML data
   * @return a {@link Document} reflecting the XML string
   * @throws ParserConfigurationException if the XML parser cannot be created
   * @throws SAXException if there is an error parsing the XML
   * @throws IOException if there is an error reading from the file
   */
  public static Document parseDOM(String xml)
    throws ParserConfigurationException, SAXException, IOException
  {
    byte[] bytes = xml.getBytes(Constants.ENCODING);
    InputStream is = new ByteArrayInputStream(bytes);
    try {
      Document doc = parseDOM(is);
      return doc;
    } finally {
      is.close();
    }
  }

  /**
   * Parses a DOM from the given XML input stream.
   *
   * @param is the {@link InputStream} containing XML
   * @return a {@link Document} representing the XML
   * @throws ParserConfigurationException if the XML parser cannot be created
   * @throws SAXException if there is an error parsing the XML
   * @throws IOException if there is an error reading from the stream
   */
  public static Document parseDOM(InputStream is)
    throws ParserConfigurationException, SAXException, IOException
  {
    final InputStream in =
      is.markSupported() ? is : new BufferedInputStream(is);
    checkUTF8(in);

    // Java XML factories are not declared to be thread safe
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = factory.newDocumentBuilder();
    db.setErrorHandler(new ParserErrorHandler());
    return db.parse(in);
  }

  /**
   * Converts the given DOM back to a string.
   *
   * @param doc the {@link Document} representing the XML
   * @return a string representation of the XML
   * @throws TransformerConfigurationException if there is an error
   *         initializing the conversion
   * @throws TransformerException if there is an error during writing
   */
  public static String getXML(Document doc)
    throws TransformerConfigurationException, TransformerException
  {
    StringWriter stringWriter = new StringWriter();
    Result result = new StreamResult(stringWriter);
    writeXML(result, doc, true);
    return stringWriter.getBuffer().toString();
  }

  /**
   * Dumps the given ODE-XML DOM tree to a string.
   * @param schemaLocation if null, no xmlns attribute will be added.
   * @param doc the {@link Document} representing the XML to be written
   * @param r the {@link Element} to use for adding the schema location
   * @return ODE-XML as a string.
   */
  public static String dumpXML(String schemaLocation, Document doc, Element r) {
    return dumpXML(schemaLocation, doc, r, true);
  }

  /**
   * Dumps the given ODE-XML DOM tree to a string.
   * @param schemaLocation if null, no xmlns attribute will be added.
   * @param doc the {@link Document} representing the XML to be written
   * @param r the {@link Element} to use for adding the schema location
   * @param includeXMLDeclaration false if the XML declaration should
   *        be omitted, see {@link OutputKeys#OMIT_XML_DECLARATION}
   * @return ODE-XML as a string.
   */
  public static String dumpXML(String schemaLocation, Document doc, Element r,
    boolean includeXMLDeclaration) {
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      if (schemaLocation != null) {
        r.setAttribute("xmlns:xsi", XSI_NS);
        r.setAttribute("xsi:schemaLocation", schemaLocation);
      }
      doc.appendChild(r);
      writeXML(os, doc, includeXMLDeclaration);
      return os.toString(Constants.ENCODING);
    }
    catch (TransformerException exc) {
      LOGGER.warn("Failed to create XML", exc);
      throw new RuntimeException(exc);
    }
    catch (UnsupportedEncodingException exc) {
      LOGGER.warn("Failed to create XML", exc);
      throw new RuntimeException(exc);
    }
  }

  // -- Filtering --

  /**
   * Escape special characters.
   * Replaces quotes, ampersands, and angle brackets
   * with the escaped equivalent.
   *
   * @param s input string containing characters to escape
   * @return copy of the input string with characters escaped
   *         as described above
   */
  public static String escapeXML(String s) {
    StringBuffer sb = new StringBuffer();

    for (int i=0; i<s.length(); i++) {
      char c = s.charAt(i);

      if (c == '<') {
        sb.append("&lt;");
      }
      else if (c == '>') {
        sb.append("&gt;");
      }
      else if (c == '&') {
        sb.append("&amp;");
      }
      else if (c == '\"') {
        sb.append("&quot;");
      }
      else if (c == '\'') {
        sb.append("&apos;");
      }
      else {
        sb.append(c);
      }
    }

    return sb.toString();
  }

  /**
   * Remove invalid characters from an XML string.
   *
   * @param s the input string from which to remove invalid characters
   * @return a copy of the input string with CR, LF, tab, and control
   *         characters replaced by a single space
   */
  public static String sanitizeXML(String s) {
    final char[] c = s.toCharArray();
    for (int i=0; i<s.length(); i++) {
      if ((Character.isISOControl(c[i]) && c[i] != '\n' && c[i] != '\t' &&
        c[i] != '\r') || !Character.isDefined(c[i]))
      {
        c[i] = ' ';
      }
      // eliminate invalid &# sequences
      if (i > 0 && c[i - 1] == '&' && c[i] == '#') c[i - 1] = ' ';
    }
    return new String(c);
  }

  /**
   * Indents XML to be more readable.
   * Uses 3 spaces to indent, and may indent CDATA.
   *
   * @param xml the XML string to format
   * @return the formatted XML string
   * @see #indentXML(String, int, boolean)
   */
  public static String indentXML(String xml) {
    return indentXML(xml, 3, false);
  }

  /**
   * Indents XML by the given spacing to be more readable.
   * CDATA may be indented.
   *
   * @param xml the XML string to format
   * @param spacing the number of spaces by which to indent
   * @return the formatted XML string
   * @see #indentXML(String, int, boolean)
   */
  public static String indentXML(String xml, int spacing) {
    return indentXML(xml, spacing, false);
  }

  /**
   * Indents XML to be more readable, avoiding any whitespace
   * injection into CDATA if the preserveCData flag is set.
   * Uses 3 spaces to indent.
   *
   * @param xml the XML string to format
   * @param preserveCData true if CDATA nodes should be preserved
   *                      with no indenting
   * @return the formatted XML string
   * @see #indentXML(String, int, boolean)
   */
  public static String indentXML(String xml, boolean preserveCData) {
    return indentXML(xml, 3, preserveCData);
  }

  /**
   * Indents XML by the given spacing to be more readable, avoiding any
   * whitespace injection into CDATA if the preserveCData flag is set.
   *
   * @param xml the XML string to format
   * @param spacing the number of spaces by which to indent
   * @param preserveCData true if CDATA nodes should be preserved
   *                      with no indenting
   * @return the formatted XML string
   */
  public static String indentXML(String xml, int spacing,
    boolean preserveCData)
  {
    if (xml == null) return null; // garbage in, garbage out
    StringBuffer sb = new StringBuffer();
    StringTokenizer st = new StringTokenizer(xml, "<>", true);
    int indent = 0, noSpace = 0;
    boolean first = true, element = false;
    while (st.hasMoreTokens()) {
      String token = st.nextToken().trim();
      if (token.equals("")) continue;
      if (token.equals("<")) {
        element = true;
        continue;
      }
      if (element && token.equals(">")) {
        element = false;
        continue;
      }

      if (!element && preserveCData) noSpace = 2;

      if (noSpace == 0) {
        // advance to next line
        if (first) first = false;
        else sb.append("\n");
      }

      // adjust indent backwards
      if (element && token.startsWith("/")) indent -= spacing;

      if (noSpace == 0) {
        // apply indent
        for (int j=0; j<indent; j++) sb.append(" ");
      }

      // output element contents
      if (element) sb.append("<");
      sb.append(token);
      if (element) sb.append(">");

      if (noSpace == 0) {
        // adjust indent forwards
        if (element &&
          !token.startsWith("?") && // ?xml tag, probably
          !token.startsWith("/") && // end element
          !token.endsWith("/") && // standalone element
          !token.startsWith("!")) // comment
        {
          indent += spacing;
        }
      }

      if (noSpace > 0) noSpace--;
    }
    sb.append("\n");
    return sb.toString();
  }

  // -- Parsing --

  /**
   * Parses the given XML string into a list of key/value pairs.
   *
   * @param xml the {@link InputStream} representing the XML
   * @return a hashtable of key/value pairs representing the XML
   * @throws IOException if there is an error during parsing
   * @see MetadataHandler
   */
  public static Hashtable<String, String> parseXML(String xml)
    throws IOException
  {
    MetadataHandler handler = new MetadataHandler();
    parseXML(xml, handler);
    return handler.getMetadata();
  }

  /**
   * Parses the given XML string using the specified XML handler.
   *
   * @param xml the string representing the XML
   * @param handler the {@link DefaultHandler} to use for parsing
   * @throws IOException if there is an error during parsing
   */
  public static void parseXML(String xml, DefaultHandler handler)
    throws IOException
  {
    parseXML(xml.getBytes(Constants.ENCODING), handler);
  }

  /**
   * Parses the XML contained in the given input stream into
   * using the specified XML handler.
   * Be very careful, as 'stream' <b>will</b> be closed by the SAX parser.
   *
   * @param stream the {@link RandomAccessInputStream} representing the XML
   * @param handler the {@link DefaultHandler} to use for parsing
   * @throws IOException if there is an error during parsing
   */
  public static void parseXML(RandomAccessInputStream stream,
    DefaultHandler handler) throws IOException
  {
    parseXML((InputStream) stream, handler);
  }

  /**
   * Parses the XML contained in the given byte array into
   * using the specified XML handler.
   *
   * @param xml the byte array representing the XML
   * @param handler the {@link DefaultHandler} to use for parsing
   * @throws IOException if there is an error during parsing
   */
  public static void parseXML(byte[] xml, DefaultHandler handler)
    throws IOException
  {
    parseXML(new ByteArrayInputStream(xml), handler);
  }

  /**
   * Parses the XML contained in the given InputStream using the
   * specified XML handler.
   *
   * @param xml the {@link InputStream} representing the XML
   * @param handler the {@link DefaultHandler} to use for parsing
   * @throws IOException if there is an error during parsing
   */
  public static void parseXML(InputStream xml, DefaultHandler handler)
    throws IOException
  {
    try {
      // Java XML factories are not declared to be thread safe
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser parser = factory.newSAXParser();
      parser.parse(xml, handler);
    }
    catch (ParserConfigurationException exc) {
      IOException e = new IOException();
      e.initCause(exc);
      throw e;
    }
    catch (SAXException exc) {
      IOException e = new IOException();
      e.initCause(exc);
      throw e;
    }
  }

  // -- I/O --

  /**
   * Writes the specified DOM to the given output stream.
   *
   * @param os the {@link OutputStream} to which XML should be written
   * @param doc the {@link Document} representing the XML to write
   * @throws TransformerException if there is an error during writing
   */
  public static void writeXML(OutputStream os, Document doc)
    throws TransformerException
  {
    writeXML(os, doc, true);
  }

  /**
   * Writes the specified DOM to the given output stream.
   *
   * @param os the {@link OutputStream} to which XML should be written
   * @param doc the {@link Document} representing the XML to write
   * @param includeXMLDeclaration false if the XML declaration should
   *        be omitted, see {@link OutputKeys#OMIT_XML_DECLARATION}
   * @throws TransformerException if there is an error during writing
   */
  public static void writeXML(OutputStream os, Document doc,
    boolean includeXMLDeclaration)
    throws TransformerException
  {
    writeXML(new StreamResult(os), doc, includeXMLDeclaration);
  }

  /**
   * Writes the specified DOM to the given stream.
   *
   * @param output the {@link Result} to which XML should be written
   * @param doc the {@link Document} representing the XML to write
   * @param includeXMLDeclaration false if the XML declaration should
   *        be omitted, see {@link OutputKeys#OMIT_XML_DECLARATION}
   * @throws TransformerException if there is an error during writing
   */
  public static void writeXML(Result output, Document doc,
    boolean includeXMLDeclaration)
    throws TransformerException
  {
    Transformer idTransform = transformFactory.newTransformer();
    if (!includeXMLDeclaration) {
      idTransform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    }
    Source input = new DOMSource(doc);
    idTransform.transform(input, output);
  }

  // -- XSLT --

  /**
   * Gets an XSLT template from the given resource location.
   *
   * @param resourcePath the name of the stylesheet resource
   * @param sourceClass the class to use when searching for the resource
   * @return a {@link Templates} object representing the stylesheet
   */
  public static Templates getStylesheet(String resourcePath,
    Class<?> sourceClass)
  {
    InputStream xsltStream;
    if (sourceClass == null) {
      try {
        xsltStream = new FileInputStream(resourcePath);
      }
      catch (IOException exc) {
        LOGGER.debug("Could not open file", exc);
        return null;
      }
    }
    else {
      xsltStream = sourceClass.getResourceAsStream(resourcePath);
    }

    try {
      StreamSource xsltSource = new StreamSource(xsltStream);
      // Java XML factories are not declared to be thread safe
      return transformFactory.newTemplates(xsltSource);
    }
    catch (TransformerConfigurationException exc) {
      LOGGER.debug("Could not construct template", exc);
    }
    finally {
      try {
        if (xsltStream != null) xsltStream.close();
      }
      catch (IOException e) {
        LOGGER.debug("Could not close file", e);
      }
    }
    return null;
  }

  /**
   * Replaces NS:tag with NS_tag for undeclared namespaces
   *
   * @param xml the XML string whose namespaces need to be replaced
   * @return a copy of the input string with NS:tag replaced by NS_tag
   *         for any undeclared namespaces
   */
  public static String avoidUndeclaredNamespaces(String xml) {
    int gt = xml.indexOf('>');
    if (gt > 0 && xml.startsWith("<?xml ")) {
      gt = xml.indexOf('>', gt + 1);
    }
    if (gt > 0) {
      String firstTag = xml.substring(0, gt + 1).toLowerCase();

      // the first tag is a comment; we need to find the first "real" tag
      while (firstTag.endsWith("-->")) {
        gt = xml.indexOf('>', gt + 1);
        firstTag = xml.substring(0, gt + 1).toLowerCase();
      }

      Set namespaces = new HashSet();
      Pattern pattern = Pattern.compile(" xmlns:(\\w+)");
      Matcher matcher = pattern.matcher(firstTag);
      while (matcher.find()) {
        namespaces.add(matcher.group(1));
      }

      pattern = Pattern.compile("</?(\\w+):");
      matcher = pattern.matcher(xml);
      while (matcher.find()) {
        String namespace = matcher.group(1);
        if (!namespace.equalsIgnoreCase("OME") && !namespace.startsWith("ns") &&
          !namespaces.contains(namespace.toLowerCase()))
        {
          int end = matcher.end();
          xml = xml.substring(0, end - 1) + "_" + xml.substring(end);
        }
      }

      Pattern emptyNamespaces = Pattern.compile(" xmlns:(\\w+)=\"\"");
      matcher = emptyNamespaces.matcher(firstTag);
      while (matcher.find()) {
        int start = matcher.start();
        int end = matcher.end();
        xml = xml.substring(0, start + 1) + xml.substring(end);
      }
    }
    return xml;
  }

  /**
   * Transforms the given XML string using the specified XSLT stylesheet.
   *
   * @param xml the XML string to be transformed
   * @param xslt the {@link Templates} object representing an XSLT stylesheet
   * @return the XML string that results from applying the stylesheet
   * @throws IOException if there is an error parsing the XML
   * @see #getStylesheet(String, Class)
   */
  public static String transformXML(String xml, Templates xslt)
    throws IOException
  {
    xml = avoidUndeclaredNamespaces(xml);
    return transformXML(new StreamSource(new StringReader(xml)), xslt);
  }

  /**
   * Transforms the given XML data using the specified XSLT stylesheet.
   *
   * @param xmlSource the {@link Source} object representing
   *                  the XML to be transformed
   * @param xslt the {@link Templates} object representing an XSLT stylesheet
   * @return the XML string that results from applying the stylesheet
   * @throws IOException if there is an error parsing the XML
   * @see #getStylesheet(String, Class)
   */
  public static String transformXML(Source xmlSource, Templates xslt)
    throws IOException
  {
    Transformer trans;
    try {
      trans = xslt.newTransformer();
      trans.setErrorListener(new XMLListener());
    }
    catch (TransformerConfigurationException exc) {
      IOException e = new IOException();
      e.initCause(exc);
      throw e;
    }
    StringWriter xmlWriter = new StringWriter();
    StreamResult xmlResult = new StreamResult(xmlWriter);
    try {
      trans.transform(xmlSource, xmlResult);
    }
    catch (TransformerException exc) {
      IOException e = new IOException();
      e.initCause(exc);
      throw e;
    }
    return xmlWriter.toString();
  }

  // -- Validation --

  /**
   * Attempts to validate the given XML string using
   * Java's XML validation facility.
   * @param xml The XML string to validate.
   * @return whether or not validation was successful.
   */
  public static boolean validateXML(String xml) {
    return validateXML(xml, null, null);
  }

  /**
   * Attempts to validate the given XML string using
   * Java's XML validation facility.
   * @param xml The XML string to validate.
   * @param label String describing the type of XML being validated.
   * @return whether or not validation was successful.
   */
  public static boolean validateXML(String xml, String label) {
      return validateXML(xml, label, null);
  }

  /**
   * Attempts to validate the given XML string using
   * Java's XML validation facility.
   * @param xml The XML string to validate.
   * @param label String describing the type of XML being validated.
   * @param schemaReader turns schema system IDs into input streams,
   * may be {@code null}
   * @return whether or not validation was successful.
   */
  public static boolean validateXML(String xml, String label,
      final SchemaReader schemaReader) {
    if (label == null) label = "XML";
    Exception exception = null;

    // get path to schema from root element using SAX
    LOGGER.info("Parsing schema path");
    ValidationSAXHandler saxHandler = new ValidationSAXHandler();
    try {
      // Java XML factories are not declared to be thread safe
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      InputStream is =
        new ByteArrayInputStream(xml.getBytes(Constants.ENCODING));
      saxParser.parse(is, saxHandler);
    }
    catch (ParserConfigurationException exc) { exception = exc; }
    catch (SAXException exc) { exception = exc; }
    catch (IOException exc) { exception = exc; }
    if (exception != null) {
      LOGGER.warn("Error parsing schema path from {}", label, exception);
      return false;
    }
    String schemaPath = saxHandler.getSchemaPath();
    if (schemaPath == null) {
      LOGGER.error("No schema path found. Validation cannot continue.");
      return false;
    }
    LOGGER.info(schemaPath);

    LOGGER.info("Validating {}", label);

    // compile the schema
    URI schemaLocation = null;
    try {
      schemaLocation = new URI(schemaPath);
    }
    catch (URISyntaxException exc) {
      LOGGER.info("Error accessing schema at {}", schemaPath, exc);
      return false;
    }
    final SchemaFactory schemaFactory =
        SchemaFactory.newInstance(XML_SCHEMA_PATH);
    if (schemaReader != null) {
      final LSResourceResolver resolver = new LSResourceResolver() {
        @Override
        public LSInput resolveResource(String type, String namespaceURI,
            String publicId, String systemId, String baseURI) {
          InputStream stream = schemaReader.getSchemaAsStream(systemId);
          if (stream == null) {
            return null;
          }

          final LSInput input = new LSInputI();
          input.setPublicId(publicId);
          input.setSystemId(systemId);
          input.setBaseURI(baseURI);
          try {
            input.setCharacterStream(new InputStreamReader(stream, "UTF-8"));
          } catch (UnsupportedEncodingException e) {
            LOGGER.warn("no UTF-8 character encoding available");
            return null;
          }
          return input;
        }
      };
      schemaFactory.setResourceResolver(resolver);
    }
    Schema schema = schemas.get().get(schemaLocation);
    if (schema == null) {
      try {
        final InputStream schemaIn = schemaReader == null ? null :
            schemaReader.getSchemaAsStream(schemaPath);
        if (schemaIn == null) {
          schema = schemaFactory.newSchema(schemaLocation.toURL());
        } else {
          final Source schemaSource = new StreamSource(schemaIn, schemaPath);
          schema = schemaFactory.newSchema(schemaSource);
          schemaIn.close();
        }
        schemas.get().put(schemaLocation, schema);
      }
      catch (IOException exc) {
        LOGGER.info("Error parsing schema at {}", schemaPath, exc);
        return false;
      }
      catch (SAXException exc) {
        LOGGER.info("Error parsing schema at {}", schemaPath, exc);
        return false;
      }
    }

    // get a validator from the schema
    Validator validator = schema.newValidator();

    // prepare the XML source
    StringReader reader = new StringReader(xml);
    InputSource is = new InputSource(reader);
    SAXSource source = new SAXSource(is);

    // validate the XML
    ValidationErrorHandler errorHandler = new ValidationErrorHandler();
    validator.setErrorHandler(errorHandler);
    try {
      validator.validate(source);
    }
    catch (IOException exc) { exception = exc; }
    catch (SAXException exc) { exception = exc; }
    final int errors = errorHandler.getErrorCount();
    if (errors > 0) {
      LOGGER.info("Error validating document: {} errors found", errors);
      return false;
    }
    else LOGGER.info("No validation errors found.");
    return errorHandler.ok();
  }

  // -- Helper methods --

  /**
   * Checks the given stream for a UTF-8 BOM header, skipping it if present. If
   * no UTF-8 BOM is present, the position of the stream is unchanged.
   * <p>
   * We must discard this character because <a href=
   * "http://www.rgagnon.com/javadetails/java-handle-utf8-file-with-bom.html"
   * >Java does not handle it correctly</a>.
   * </p>
   */
  private static void checkUTF8(InputStream is) throws IOException {
    // check first 3 bytes of the stream
    is.mark(3);
    if (is.read() != 0xef || is.read() != 0xbb || is.read() != 0xbf) {
      // NB: Data stream does not start with the UTF-8 BOM; reset it.
      is.reset();
    }
  }

  // -- Helper class --

  /** ErrorListener implementation that logs errors and warnings using SLF4J. */
  static class XMLListener implements ErrorListener {
    @Override
    public void error(TransformerException e) {
      LOGGER.debug("", e);
    }

    @Override
    public void fatalError(TransformerException e) {
      LOGGER.debug("", e);
    }

    @Override
    public void warning(TransformerException e) {
      LOGGER.debug("", e);
    }
  }

}