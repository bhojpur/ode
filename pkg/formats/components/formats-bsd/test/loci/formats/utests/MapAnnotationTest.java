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
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ode.xml.model.Image;
import ode.xml.model.MapAnnotation;
import ode.xml.model.MapPair;
import ode.xml.model.ODE;
import ode.xml.model.ODEModel;
import ode.xml.model.ODEModelImpl;
import ode.xml.model.Pixels;
import ode.xml.model.StructuredAnnotations;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Test case for MapAnnotation values
 */
public class MapAnnotationTest {

  private ODE ode = new ODE();

  @BeforeClass
  public void setUp() throws Exception {
    // Add an Image/Pixels
    Image image = new Image();
    image.setID("Image:0");
    Pixels pixels = new Pixels();
    pixels.setID("Pixels:0");
    image.setPixels(pixels);

    // Add a Map Annotation
    List<MapPair> map = new ArrayList<MapPair>();
    map.add(new MapPair("a", "1"));
    map.add(new MapPair("d", "2"));
    map.add(new MapPair("c", "3"));
    map.add(new MapPair("b", "4"));
    map.add(new MapPair("e", "5"));
    map.add(new MapPair("c", "6"));

    MapAnnotation mapAnnotation = new MapAnnotation();
    mapAnnotation.setID("Annotation:0");
    mapAnnotation.setValue(map);

    StructuredAnnotations structuredAnnotations = new StructuredAnnotations();
    structuredAnnotations.addMapAnnotation(mapAnnotation);
    ode.setStructuredAnnotations(structuredAnnotations );
    image.linkAnnotation(mapAnnotation);
    ode.addImage(image);
  }

  @Test
  public void testMapAnnotationValid() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    Document document = parser.newDocument();
    // Produce a valid ODE DOM element hierarchy
    Element root = ode.asXMLElement(document);
    SPWModelMock.postProcess(root, document, false);
    ODEModel model = new ODEModelImpl();
    ode = new ODE(document.getDocumentElement(), model);
    model.resolveReferences();
  }

  @Test
  public void testMapAnnotationValueContent() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    Document document = parser.newDocument();
    // Produce a valid ODE DOM element hierarchy
    Element root = ode.asXMLElement(document);
    SPWModelMock.postProcess(root, document, false);
    ODEModel model = new ODEModelImpl();
    ode = new ODE(document.getDocumentElement(), model);
    model.resolveReferences();

    assertNotNull(ode); 
    assertEquals(ode.getImage(0).getPixels().getID(), "Pixels:0"); 
    assertNotNull(ode.getImage(0).getLinkedAnnotation(0)); 

    MapAnnotation mapAnnotation = (MapAnnotation) ode.getImage(0).getLinkedAnnotation(0); 
    List<MapPair> dataMap = mapAnnotation.getValue();

    assertEquals(6, dataMap.size());
    assertEquals("a", dataMap.get(0).getName());
    assertEquals("1", dataMap.get(0).getValue());
    assertEquals("d", dataMap.get(1).getName());
    assertEquals("2", dataMap.get(1).getValue());
    assertEquals("c", dataMap.get(2).getName());
    assertEquals("3", dataMap.get(2).getValue());
    assertEquals("b", dataMap.get(3).getName());
    assertEquals("4", dataMap.get(3).getValue());
    assertEquals("e", dataMap.get(4).getName());
    assertEquals("5", dataMap.get(4).getValue());
    assertEquals("c", dataMap.get(5).getName());
    assertEquals("6", dataMap.get(5).getValue());
  }

}
