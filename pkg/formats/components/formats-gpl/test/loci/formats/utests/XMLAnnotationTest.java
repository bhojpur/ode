package loci.formats.utests;

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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ode.xml.model.Annotation;
import ode.xml.model.Channel;
import ode.xml.model.Image;
import ode.xml.model.ODE;
import ode.xml.model.ODEModel;
import ode.xml.model.ODEModelImpl;
import ode.xml.model.Pixels;
import ode.xml.model.XMLAnnotation;
import ode.xml.model.enums.EnumerationException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 * Test case which outlines the problems.
 */
public class XMLAnnotationTest {

  private ODE ode;

  @BeforeClass
  public void setUp() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    Document document = parser.parse(
        this.getClass().getResourceAsStream("XMLAnnotationTest.ode"));
    ODEModel model = new ODEModelImpl();
    // Read string XML in as a DOM tree and parse into the object hierarchy
    ode = new ODE(document.getDocumentElement(), model);
    model.resolveReferences();
  }

  @Test
  public void testValidXMLAnnotation() throws EnumerationException {
    assertNotNull(ode);
    assertEquals(1, ode.sizeOfImageList());
    Image image = ode.getImage(0);
    Pixels pixels = image.getPixels();
    assertNotNull(pixels);
    assertEquals(3, pixels.sizeOfChannelList());
    Channel channel = pixels.getChannel(0);
    assertEquals(1, channel.sizeOfLinkedAnnotationList());
    Annotation annotation = channel.getLinkedAnnotation(0);
    assertEquals(XMLAnnotation.class, annotation.getClass());
    String annotationValue = ((XMLAnnotation) annotation).getValue();

    // normalize line endings if the test is run on Windows
    annotationValue = annotationValue.replaceAll("\r\n", "\n");
    assertEquals("<TestData>\n                    <key>foo</key>\n\t\t\t\t\t<value>bar</value>\n                </TestData>", annotationValue);
  }

}
