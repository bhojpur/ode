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
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Random;

public class SmartMaskI extends ode.model.MaskI implements SmartShape {

    public void areaPoints(PointCallback cb) {
        Shape s = asAwtShape();
        if (s == null) {
            return;
        }
        if (transform != null) s = Util.transformAwtShape(s, transform);
        Rectangle2D r = s.getBounds2D();
        Util.pointsByBoundingBox(s, r, cb);
    }

    public Shape asAwtShape() {
        double[] d = data();
        if (d == null || d.length != 4) {
            return null;
        }
        Rectangle2D.Double rect = new Rectangle2D.Double(d[0], d[1], d[2], d[3]);
        return rect;
    }

    public List<Point> asPoints() {
        double[] d = data();
        if (d == null || d.length != 4) {
            return null;
        }
        List<Point> points = Util.points(d[0], d[1], d[2], d[3]);
        assert Util.checkNonNull(points) : "Null points in " + this;
        return points;
    }

    public void randomize(Random random) {
        if (roi == null) {
            x = rdouble(random.nextInt(100));
            y = rdouble(random.nextInt(100));
            width = rdouble(random.nextInt(100));
            height = rdouble(random.nextInt(100));
        } else {
            throw new UnsupportedOperationException(
                    "Roi-based values unsupported");
        }
    }

    private double[] data() {
        try {
            double x = getX().getValue();
            double y = getY().getValue();
            double w = getWidth().getValue();
            double h = getHeight().getValue();
            return new double[] { x, y, w, h };
        } catch (NullPointerException npe) {
            return null;
        }
    }
}