package ode.xml.model;

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

import java.util.ArrayList;
import java.util.List;

import ode.xml.model.enums.EnumerationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractODEModelObject implements ODEModelObject {

  /* (non-Javadoc)
   * @see ode.xml.r201004.ODEModelObject#update(org.w3c.dom.Element, ode.xml.r201004.ODEModel)
   */
  @Override
  public void update(Element element, ODEModel model)
  throws EnumerationException {
    // Nothing to update.
  }

  /* (non-Javadoc)
   * @see ode.xml.r201004.ODEModelObject#asXMLElement(org.w3c.dom.Document)
   */
  @Override
  public abstract Element asXMLElement(Document document);
  
  /**
   * Takes the entire object hierarchy and produced an XML DOM tree taking
   * into account class hierarchy.
   * @param document Destination document for element creation, etc.
   * @param element Element from the subclass. If <code>null</code> a new
   * element will be created of this class.
   * @return <code>element</code> populated with properties from this class.
   */
  protected Element asXMLElement(Document document, Element element) {
    return element;
  }

  /* (non-Javadoc)
   * @see ode.xml.r201004.ODEModelObject#link(ode.xml.r201004.Reference, ode.xml.r201004.ODEModelObject)
   */
  @Override
  public boolean link(Reference reference, ODEModelObject o) {
    return false;
  }

  /**
   * Retrieves all the children of an element that have a given tag name. If a
   * tag has a namespace prefix it will be stripped prior to attempting a
   * name match.
   * @param parent DOM element to retrieve tags based upon.
   * @param name Name of the tags to retrieve.
   * @return List of elements which have the tag <code>name</code>.
   */
  public static List<Element> getChildrenByTagName(
      Element parent, String name) {
    List<Element> toReturn = new ArrayList<Element>();
    NodeList children = parent.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE
          && name.equals(stripNamespacePrefix(child.getNodeName()))) {
        toReturn.add((Element) child);
      }
    }
    return toReturn;
  }

  /**
   * Strips the namespace prefix off of a given tag name.
   * @param v Tag name to strip the prefix from if it has one.
   * @return <code>v</code> with the namespace prefix stripped or <code>v</code>
   * if it has none.
   */
  public static String stripNamespacePrefix(String v) {
    int beginIndex = v.lastIndexOf(':');
    if (beginIndex != -1) {
      v = v.substring(beginIndex + 1);
    }
    return v;
  }
}