package utests.gateway.model;

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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import ode.rtypes;
import ode.gateway.model.PolygonData;
import ode.gateway.model.PolylineData;
import ode.model.Polygon;
import ode.model.PolygonI;
import ode.model.Polyline;
import ode.model.PolylineI;

import org.testng.annotations.Test;

import junit.framework.TestCase;

/**
 * Tests the storage of points for polyline and polygon according to the
 * schema.
 */
public class PolylineAndPolygonTest extends TestCase {

    /** Returns a double array as a number attribute value. */
    private String toNumber(double number)
    {
        String str = Double.toString(number);
        if (str.endsWith(".0"))
            str = str.substring(0, str.length()-2);
        return str;
    }

    /**
     * Converts the specified points.
     *
     * @param points The points to handle.
     * @return See above.
     */
    private String toPoints(Point2D.Double[] points)
    {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < points.length; i++)
        {
            if (i != 0)
            {
                buf.append(" ");
            }
            buf.append(toNumber(points[i].x));
            buf.append(',');
            buf.append(toNumber(points[i].y));
        }
        return buf.toString();
    }

    @Test
    public void testSavePointsForPolyline() {
        PolylineData data = new PolylineData();
        List<Point2D.Double> points = new ArrayList<Point2D.Double>();
        int n = 5;
        for (int i = 0; i < n; i++) {
            Point2D.Double p = new Point2D.Double(i, n);
            points.add(p);
        }
        data.setPoints(points);
        Polyline shape = (Polyline) data.asIObject();
        String pointsAsString = shape.getPoints().getValue();
        //Check that the string no longer contains "points""
        assertFalse(pointsAsString.contains("points"));
    }

    @Test
    public void testSavePointsForPolygon() {
        PolygonData data = new PolygonData();
        List<Point2D.Double> points = new ArrayList<Point2D.Double>();
        int n = 5;
        for (int i = 0; i < n; i++) {
            Point2D.Double p = new Point2D.Double(i, n);
            points.add(p);
        }
        data.setPoints(points);
        Polygon shape = (Polygon) data.asIObject();
        String pointsAsString = shape.getPoints().getValue();
        //Check that the string no longer contains
        //"points", "points1", "points2" or "masks"
        assertFalse(pointsAsString.contains("points"));
        assertFalse(pointsAsString.contains("points1"));
        assertFalse(pointsAsString.contains("points2"));
        assertFalse(pointsAsString.contains("masks"));
    }

    @Test
    public void testSetPointsForPolyline() {
        PolylineData data = new PolylineData();
        List<Point2D.Double> points = new ArrayList<Point2D.Double>();
        int n = 5;
        for (int i = 0; i < n; i++) {
            Point2D.Double p = new Point2D.Double(i, n);
            points.add(p);
        }
        data.setPoints(points);
        Polyline shape = (Polyline) data.asIObject();
        String pointsAsString = shape.getPoints().getValue();
        //Check that the string no longer contains
        //"points"
        assertFalse(pointsAsString.contains("points"));
    }

    @Test
    public void testSetPointsForPolygon() {
        PolygonData data = new PolygonData();
        List<Point2D.Double> points = new ArrayList<Point2D.Double>();
        int n = 5;
        for (int i = 0; i < n; i++) {
            Point2D.Double p = new Point2D.Double(i, n);
            points.add(p);
        }
        data.setPoints(points);
        Polygon shape = (Polygon) data.asIObject();
        String pointsAsString = shape.getPoints().getValue();
        //Check that the string no longer contains
        //"points"
        assertFalse(pointsAsString.contains("points"));
    }

    @Test
    public void testNewInstanceForPolyline() {
        
        List<Point2D.Double> points = new ArrayList<Point2D.Double>();
        int n = 5;
        for (int i = 0; i < n; i++) {
            Point2D.Double p = new Point2D.Double(i, n);
            points.add(p);
        }
        PolylineData data = new PolylineData(points);
        Polyline shape = (Polyline) data.asIObject();
        String pointsAsString = shape.getPoints().getValue();
        //Check that the string no longer contains
        //"points"
        assertFalse(pointsAsString.contains("points"));
        List<Point2D.Double> list = data.getPoints();
        Point2D.Double p, p1;
        for (int i = 0; i < n; i++) {
            p = points.get(i);
            p1 = list.get(i);
            assertEquals(p.getX(), p1.getX());
            assertEquals(p.getY(), p1.getY());
        }
    }

    @Test
    public void testNewInstanceForPolygon() {
        
        List<Point2D.Double> points = new ArrayList<Point2D.Double>();
        int n = 5;
        for (int i = 0; i < n; i++) {
            Point2D.Double p = new Point2D.Double(i, n);
            points.add(p);
        }
        PolygonData data = new PolygonData(points);
        Polygon shape = (Polygon) data.asIObject();
        String pointsAsString = shape.getPoints().getValue();
        //Check that the string no longer contains
        //"points"
        assertFalse(pointsAsString.contains("points"));
        List<Point2D.Double> list = data.getPoints();
        Point2D.Double p, p1;
        for (int i = 0; i < n; i++) {
            p = points.get(i);
            p1 = list.get(i);
            assertEquals(p.getX(), p1.getX());
            assertEquals(p.getY(), p1.getY());
        }
    }

    @Test
    public void testSavePointsLegacyModeForPolygon() {
        Polygon shape = new PolygonI();
        int n = 5;
        Point2D.Double[] points = new Point2D.Double[n];
        List<Integer> masks = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            Point2D.Double p = new Point2D.Double(i, n);
            points[i] = p;
            masks.add(i);
        }
        String maskValues = "";
        for (int i = 0 ; i < masks.size()-1; i++)
            maskValues = maskValues + masks.get(i)+",";
        maskValues = maskValues+masks.get(masks.size()-1)+"";

        String pointsValues = toPoints(points);
        String pts = "points["+pointsValues+"] ";
        pts = pts + "points1["+pointsValues+"] ";
        pts = pts + "points2["+pointsValues+"] ";
        pts = pts + "mask["+maskValues+"] ";
        shape.setPoints(rtypes.rstring(pts));
        PolygonData data = new PolygonData(shape);
        List<Point2D.Double> list = data.getPoints();
        Point2D.Double p, p1;
        for (int i = 0; i < n; i++) {
            p = points[i];
            p1 = list.get(i);
            assertEquals(p.getX(), p1.getX());
            assertEquals(p.getY(), p1.getY());
        }
        list = data.getPoints();
        for (int i = 0; i < n; i++) {
            p = points[i];
            p1 = list.get(i);
            assertEquals(p.getX(), p1.getX());
            assertEquals(p.getY(), p1.getY());
        }
        List<Integer> ml = data.getMaskPoints();
        for (int i = 0; i < n; i++) {
            assertEquals(masks.get(i), ml.get(i));
        }
    }

    @Test
    public void testSavePointsLegacyModeForPolyline() {
        Polyline shape = new PolylineI();
        int n = 5;
        Point2D.Double[] points = new Point2D.Double[n];
        List<Integer> masks = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            Point2D.Double p = new Point2D.Double(i, n);
            points[i] = p;
            masks.add(i);
        }
        String maskValues = "";
        for (int i = 0 ; i < masks.size()-1; i++)
            maskValues = maskValues + masks.get(i)+",";
        maskValues = maskValues+masks.get(masks.size()-1)+"";

        String pointsValues = toPoints(points);
        String pts = "points["+pointsValues+"] ";
        pts = pts + "points1["+pointsValues+"] ";
        pts = pts + "points2["+pointsValues+"] ";
        pts = pts + "mask["+maskValues+"] ";
        shape.setPoints(rtypes.rstring(pts));
        PolygonData data = new PolygonData(shape);
        List<Point2D.Double> list = data.getPoints();
        Point2D.Double p, p1;
        for (int i = 0; i < n; i++) {
            p = points[i];
            p1 = list.get(i);
            assertEquals(p.getX(), p1.getX());
            assertEquals(p.getY(), p1.getY());
        }
        list = data.getPoints();
        for (int i = 0; i < n; i++) {
            p = points[i];
            p1 = list.get(i);
            assertEquals(p.getX(), p1.getX());
            assertEquals(p.getY(), p1.getY());
        }
        List<Integer> ml = data.getMaskPoints();
        for (int i = 0; i < n; i++) {
            assertEquals(masks.get(i), ml.get(i));
        }
    }
    
    @Test
    public void testGetPointsForPolygon() {
        PolygonData data = new PolygonData();
        List<Point2D.Double> points = new ArrayList<Point2D.Double>();
        int n = 5;
        Point2D.Double p;
        for (int i = 0; i < n; i++) {
            p = new Point2D.Double(i, n);
            points.add(p);
        }
        data.setPoints(points);
        List<Point2D.Double> list = data.getPoints();
        Point2D.Double p1;
        for (int i = 0; i < n; i++) {
            p = points.get(i);
            p1 = list.get(i);
            assertEquals(p.getX(), p1.getX());
            assertEquals(p.getY(), p1.getY());
        }
    }

    @Test
    public void testGetPointsForPolyline() {
        PolylineData data = new PolylineData();
        List<Point2D.Double> points = new ArrayList<Point2D.Double>();
        int n = 5;
        Point2D.Double p;
        List<Integer> masks = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            p = new Point2D.Double(i, n);
            points.add(p);
            masks.add(i);
        }
        data.setPoints(points);
        List<Point2D.Double> list = data.getPoints();
        Point2D.Double p1;
        for (int i = 0; i < n; i++) {
            p = points.get(i);
            p1 = list.get(i);
            assertEquals(p.getX(), p1.getX());
            assertEquals(p.getY(), p1.getY());
        }
    }
}
