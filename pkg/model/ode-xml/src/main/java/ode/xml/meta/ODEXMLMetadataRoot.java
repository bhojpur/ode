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

import org.w3c.dom.Element;

import ode.xml.model.ODE;
import ode.xml.model.ODEModel;
import ode.xml.model.enums.EnumerationException;

/**
 * A utility class for constructing and manipulating ODE-XML DOMs.
 */
public class ODEXMLMetadataRoot extends ODE implements MetadataRoot {

  /** Default constructor. */
  public ODEXMLMetadataRoot()
  {
    super();
  }

  /**
   * Constructs ODE recursively from an XML DOM tree.
   * @param element Root of the XML DOM tree to construct a model object
   * graph from.
   * @param model Handler for the ODE model which keeps track of instances
   * and references seen during object population.
   * @throws EnumerationException If there is an error instantiating an
   * enumeration during model object creation.
   */
  public ODEXMLMetadataRoot(Element element, ODEModel model)
    throws EnumerationException
  {
    super(element, model);
  }

  /**
   * Construct from existing Bhojpur ODE instance.
   *
   * @param ode the Bhojpur ODE instance to copy.
   */
  public ODEXMLMetadataRoot(ODE ode)
  {
    super(ode);
  }

}