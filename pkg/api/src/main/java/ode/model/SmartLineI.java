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

import static ode.rtypes.rdouble;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.Random;

public class SmartLineI extends ode.model.LineI implements SmartShape {

    public void areaPoints(PointCallback cb) {
        Shape s = asAwtShape();
        if (s == null) {
            return;
        }
        if (transform != null) s = Util.transformAwtShape(s, transform);
        final Set<Point2D> points = Util.getQuantizedLinePoints((Line2D) s, null);
        for (Point2D p : points)
            cb.handle((int) p.getX(), (int) p.getY());
    }
    
    public Shape asAwtShape() {
        try {
            double x1 = getX1().getValue();
            double x2 = getX2().getValue();
            double y1 = getY1().getValue();
            double y2 = getY2().getValue();
            Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
            return line;
        } catch (NullPointerException npe) {
            return null;
        }
    }

    public List<Point> asPoints() {
        try {
            List<Point> points = new ArrayList<Point>();
            SmartPointI start = new SmartPointI();
            start.setX(getX1());
            start.setY(getY1());
            SmartPointI end = new SmartPointI();
            end.setX(getX2());
            end.setY(getY2());
            points.addAll(start.asPoints());
            points.addAll(end.asPoints());
            assert Util.checkNonNull(points) : "Null points in " + this;
            return points;
        } catch (NullPointerException npe) {
            return null;
        }
    }

    public void randomize(Random random) {
        if (roi == null) {
            x1 = rdouble(random.nextInt(100));
            x2 = rdouble(random.nextInt(100));
            y1 = rdouble(random.nextInt(100));
            y2 = rdouble(random.nextInt(100));
        } else {
            throw new UnsupportedOperationException(
                    "Roi-based values unsupported");
        }
    }

}