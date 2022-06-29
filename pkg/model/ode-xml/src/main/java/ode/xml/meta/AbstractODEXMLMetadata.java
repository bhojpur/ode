package ode.xml.meta;

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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import loci.common.Constants;
import loci.common.xml.XMLTools;

import ode.xml.model.ODE;
import ode.xml.model.ODEModelObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class for constructing and manipulating ODE-XML DOMs.
 * It is the superclass for all versions of ODE-XML. It requires the
 * ode.xml package to compile (part of ode-xml.jar).
 */
public abstract class AbstractODEXMLMetadata implements ODEXMLMetadata {

  // -- Constants --

  /** XSI namespace. */
  public static final String XSI_NS =
    "http://www.w3.org/2001/XMLSchema-instance";

  /** ODE-XML schema location. */
  public static final String SCHEMA =
    "http://www.bhojpur.net/Schemas/ODE/2018-03/ode.xsd";

  protected static final Logger LOGGER =
    LoggerFactory.getLogger(AbstractODEXMLMetadata.class);

  // -- Fields --

  /** The root element of ODE-XML. */
  protected ODEModelObject root;

  /** DOM element that backs the first Image's CustomAttributes node. */
  private Element imageCA;

  private DocumentBuilder builder;

  // -- Constructors --

  /** Creates a new ODE-XML metadata object. */
  public AbstractODEXMLMetadata() {
  }

  // -- ODEXMLMetadata API methods --

  /**
   * Dumps the given ODE-XML DOM tree to a string.
   * @return ODE-XML as a string.
   */
  @Override
  public String dumpXML() {
    if (root == null) {
      root = (ODEModelObject) getRoot();
      if (root == null) return null;
    }
    Document doc = createNewDocument();
    Element r = root.asXMLElement(doc);
    String schemaLocation = ODE.NAMESPACE + " " + SCHEMA;
    return XMLTools.dumpXML(schemaLocation, doc, r);
  }


  // -- Helper methods --

  public Document createNewDocument() {
    if (builder == null) {
      builder = XMLTools.createBuilder();
    }
    return builder.newDocument();
  }

}