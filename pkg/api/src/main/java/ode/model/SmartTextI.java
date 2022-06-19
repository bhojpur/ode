package ode.model;

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

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SmartTextI extends ode.model.LabelI implements SmartShape {

    public void areaPoints(PointCallback cb) {
        try {
            double point_x = x.getValue();
            double point_y = y.getValue();
            if (transform != null) {
                final AffineTransform t = Util.getAwtTransform(transform);
                
                final Point2D p = 
                    t.transform(new Point2D.Double(point_x, point_y), null);
                point_x = p.getX();
                point_y = p.getY();
        	 }
            cb.handle((int) point_x, (int) point_y);
        } catch (NullPointerException npe) {
            return;
        }
    }

    public Shape asAwtShape() {
        List<Point> points = asPoints();
        if (points == null) {
            return null;
        }
        String path = Util.pointsToPath(points, true);
        return Util.parseAwtPath(path);
    }

    public List<Point> asPoints() {
        if (x == null || y == null) {
            return null; // As in SmartPoint
        }
        Point pt = new PointI();
        pt.x = x;
        pt.y = y;
        List<Point> points = Arrays.<Point> asList(pt);
        assert Util.checkNonNull(points) : "Null points in " + this;
        return points;
    }

    public void randomize(Random random) {
        if (roi == null) {
            // DO NOTHING FOR NOW
        } else {
            throw new UnsupportedOperationException(
                    "Roi-based values unsupported");
        }
    }
}