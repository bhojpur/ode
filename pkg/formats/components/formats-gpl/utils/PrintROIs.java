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

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

import ode.xml.model.Ellipse;
import ode.xml.model.Label;
import ode.xml.model.Line;
import ode.xml.model.Mask;
import ode.xml.model.ODE;
import ode.xml.model.Point;
import ode.xml.model.Polygon;
import ode.xml.model.Polyline;
import ode.xml.model.Rectangle;
import ode.xml.model.Shape;
import ode.xml.model.Union;

/**
 * A simple example of how to retrieve ROI data parsed from a file.
 */
public class PrintROIs {

  /** Print all of the ROIs that were parsed from the file. */
  public static void printAllROIs(IMetadata odexml) {
    int roiCount = odexml.getROICount();
    for (int roi=0; roi<roiCount; roi++) {
      printROI(odexml, roi);
    }
  }

  /** Print only the ROIs that are associated with the given series. */
  public static void printSeriesROIs(IMetadata odexml, int series) {
    int roiCount = odexml.getImageROIRefCount(series);

    ArrayList<String> roiIDs = new ArrayList<String>();
    int totalROICount = odexml.getROICount();
    for (int roi=0; roi<totalROICount; roi++) {
      String roiID = odexml.getROIID(roi);
      roiIDs.add(roiID);
    }

    for (int roi=0; roi<roiCount; roi++) {
      String roiRef = odexml.getImageROIRef(series, roi);
      int roiIndex = roiIDs.indexOf(roiRef);

      System.out.println("ROIs associated with series #" + series);
      printROI(odexml, roiIndex);
    }
  }

  /** Print all of the shapes associated with the specified ROI. */
  public static void printROI(IMetadata odexml, int roi) {
    String roiID = odexml.getROIID(roi);
    System.out.println("ROI #" + roi + " (ID = " + roiID + ")");
    System.out.println("  Name = " + odexml.getROIName(roi));

    int shapeCount = odexml.getShapeCount(roi);

    System.out.println("  Number of shapes = " + shapeCount);

    // Note that it is not possible to retrieve the shape's type
    // from the IMetadata object; you must use the underlying model
    // objects to determine the shape type.
    ODE root = (ODE) odexml.getRoot();
    Union allShapes = root.getROI(roi).getUnion();

    for (int shape=0; shape<shapeCount; shape++) {
      Shape shapeObject = allShapes.getShape(shape);
      printShape(shapeObject);
    }
  }

  /** Print the given shape. */
  public static void printShape(Shape shapeObject) {
    if (shapeObject instanceof Ellipse) {
      Ellipse ellipse = (Ellipse) shapeObject;
      System.out.println("    Ellipse:");
      System.out.println("      Text = " + ellipse.getText());
      System.out.println("      X = " + ellipse.getX());
      System.out.println("      Y = " + ellipse.getY());
      System.out.println("      Radius (X) = " + ellipse.getRadiusX());
      System.out.println("      Radius (Y) = " + ellipse.getRadiusY());
    }
    else if (shapeObject instanceof Line) {
      Line line = (Line) shapeObject;
      System.out.println("    Line:");
      System.out.println("      Text = " + line.getText());
      System.out.println("      X1 = " + line.getX1());
      System.out.println("      Y1 = " + line.getY1());
      System.out.println("      X2 = " + line.getX2());
      System.out.println("      Y2 = " + line.getY2());
    }
    else if (shapeObject instanceof Point) {
      Point point = (Point) shapeObject;
      System.out.println("    Point:");
      System.out.println("      Text = " + point.getText());
      System.out.println("      X = " + point.getX());
      System.out.println("      Y = " + point.getY());
    }
    else if (shapeObject instanceof Polyline) {
      Polyline polyline = (Polyline) shapeObject;
      System.out.println("    Polyline:");
      System.out.println("      Text = " + polyline.getText());
      System.out.println("      Points = " + polyline.getPoints());
    }
    else if (shapeObject instanceof Polygon) {
      Polygon polygon = (Polygon) shapeObject;
      System.out.println("    Polygon:");
      System.out.println("      Text = " + polygon.getText());
      System.out.println("      Points = " + polygon.getPoints());
    }
    else if (shapeObject instanceof Rectangle) {
      Rectangle rectangle = (Rectangle) shapeObject;
      System.out.println("    Rectangle:");
      System.out.println("      Text = " + rectangle.getText());
    }
    else if (shapeObject instanceof Mask) {
      Mask mask = (Mask) shapeObject;
      System.out.println("    Mask:");
      System.out.println("      Text = " + mask.getText());
      System.out.println("      X = " + mask.getX());
      System.out.println("      Y = " + mask.getY());
      System.out.println("      Width = " + mask.getWidth());
      System.out.println("      Height = " + mask.getHeight());
    }
    else if (shapeObject instanceof Label) {
      Label text = (Label) shapeObject;
      System.out.println("    Label:");
      System.out.println("      Text = " + text.getText());
      System.out.println("      X = " + text.getX());
      System.out.println("      Y = " + text.getY());
    }
  }

  public static void main(String[] args) throws Exception {
    ImageReader reader = new ImageReader();
    IMetadata odexml;

    try {
      ServiceFactory factory = new ServiceFactory();
      ODEXMLService service = factory.getInstance(ODEXMLService.class);
      odexml = service.createODEXMLMetadata();
    }
    catch (DependencyException exc) {
      throw new FormatException("Could not create ODE-XML store.", exc);
    }
    catch (ServiceException exc) {
      throw new FormatException("Could not create ODE-XML store.", exc);
    }

    reader.setMetadataStore(odexml);
    reader.setId(args[0]);

    printAllROIs(odexml);
    System.out.println();
    for (int series=0; series<reader.getSeriesCount(); series++) {
      printSeriesROIs(odexml, series);
    }

    reader.close();
  }

}
