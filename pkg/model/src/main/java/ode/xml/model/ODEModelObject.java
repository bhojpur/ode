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

import ode.xml.model.enums.EnumerationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface ODEModelObject {

  /**
   * Takes the entire object hierarchy and produces an XML DOM tree.
   * @param document Destination document for element creation, etc.
   * @return XML DOM tree root element for this model object.
   */
  Element asXMLElement(Document document);

  /** 
   * Updates the object hierarchy recursively from an XML DOM tree.
   * <b>NOTE:</b> No properties are removed, only added or updated.
   * @param element Root of the XML DOM tree to construct a model object
   * graph from.
   * @param model Handler for the ODE model which keeps track of instances
   * and references seen during object population.
   * @throws EnumerationException If there is an error instantiating an
   * enumeration during model object creation.
   */
  void update(Element element, ODEModel model) throws EnumerationException;

  /**
   * Link a given ODE model object to this model object.
   * @param reference The <i>type</i> qualifier for the reference. This should
   * be the corresponding reference type for <code>o</code>. If, for example,
   * <code>o</code> is of type <code>Image</code>, <code>reference</code>
   * <b>MUST</b> be of type <code>ImageRef</code>.
   * @param o Model object to link to.
   * @return <code>true</code> if this model object was able to handle the
   * reference, <code>false</code> otherwise.
   */
  boolean link(Reference reference, ODEModelObject o);
}