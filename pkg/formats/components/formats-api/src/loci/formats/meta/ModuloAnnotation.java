package loci.formats.meta;

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

import loci.common.xml.XMLTools;
import loci.formats.Modulo;
import loci.formats.ode.ODEXMLMetadata;
import ode.xml.model.XMLAnnotation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ModuloAnnotation extends XMLAnnotation {

  public static final String MODULO_NS;
  static {
    MODULO_NS = "bhojpur.net/ode/dimension/modulo";
  }

  private Modulo modulo;

  /**
   * Set the value of this annotation using the given Modulo object
   * @param meta the associated ODEXMLMetadata
   * @param m the Modulo object from which to retrieve dimension information
   */
  public void setModulo(ODEXMLMetadata meta, Modulo m) {
    modulo = m;
    setNamespace(MODULO_NS);
    Document doc = XMLTools.createDocument();
    Element r = makeModuloElement(doc);
    setValue(XMLTools.dumpXML(null, doc, r, false));
  }

  /**
   * Construct a DOM element for this annotation using
   * the given Document as the root.
   * @param document the root document node
   */
  protected Element makeModuloElement(Document document) {
    Element mtop = document.createElement("Modulo");
    mtop.setAttribute("namespace", "http://www.bhojpur.net/Schemas/Additions/2011-09");
    // TODO: the above should likely NOT be hard-coded
    Element m = document.createElement("ModuloAlong" + modulo.parentDimension);
    mtop.appendChild(m);

    String type = modulo.type;
    String typeDescription = modulo.typeDescription;
    if (type != null) {
      type = type.toLowerCase();
    }
    // Handle CZI files for the moment.
    if (type.equals("rotation")) {
      type = "angle";
    }
    if (type == null || (!type.equals("angle") && !type.equals("phase") &&
      !type.equals("tile") && !type.equals("lifetime") &&
      !type.equals("lambda")))
    {
      if (typeDescription == null) {
        typeDescription = type;
      }
      type = "other";
    }

    m.setAttribute("Type", type);
    m.setAttribute("TypeDescription", typeDescription);
    if (modulo.unit != null) {
      m.setAttribute("Unit", modulo.unit);
    }
    if (modulo.end > modulo.start) {
      m.setAttribute("Start", String.valueOf(modulo.start));
      m.setAttribute("Step", String.valueOf(modulo.step));
      m.setAttribute("End", String.valueOf(modulo.end));
    }
    if (modulo.labels != null) {
      for (String label : modulo.labels) {
        Element labelNode = document.createElement("Label");
        labelNode.setTextContent(label);
        m.appendChild(labelNode);
      }
    }
    return mtop;
  }
}
