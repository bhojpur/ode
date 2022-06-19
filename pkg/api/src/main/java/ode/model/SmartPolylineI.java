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

import static ode.rtypes.rstring;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SmartPolylineI extends ode.model.PolylineI implements SmartShape {

    public void areaPoints(PointCallback cb) {
        Shape s = asAwtShape();
        if (s == null) {
            return;
        }
        final PathIterator it = s.getPathIterator(Util.getAwtTransform(transform));
        double [] vals = new double[] {0, 0, 0, 0, 0, 0};
        double [] last_point = null;
        while (!it.isDone()) {
            it.currentSegment(vals);
            double [] new_point = new double[] {vals[0], vals[1]};
            if (last_point != null) {
                final Set<Point2D> points = Util.getQuantizedLinePoints(
                    new Line2D.Double(last_point[0], last_point[1], new_point[0], new_point[1]), null);
                for (Point2D p : points) cb.handle((int) p.getX(), (int) p.getY());
            }
            last_point = new_point;
            it.next();
        }
    }
    
    public Shape asAwtShape() {
        String str = this.points.getValue();
        if (str == null) {
            return null;
        }
        String path = Util.parsePointsToPath(str, false);
        return Util.parseAwtPath(path);
    }

    public List<Point> asPoints() {
        String str = this.points.getValue();
        if (str == null) {
            return null;
        }
        List<Point> points = Util.parsePoints(str);
        assert Util.checkNonNull(points) : "Null points in " + this;
        return points;
    }

    public void randomize(Random random) {
        if (roi == null) {
            StringBuilder sb = new StringBuilder();
            int sz = random.nextInt(20) + 1;
            for (int i = 0; i < sz; i++) {
                int x = random.nextInt(100);
                int y = random.nextInt(100);
                if (i > 0) {
                    sb.append(",");
                }
                Util.appendSvgPoint(sb, x, y);
            }
            this.points = rstring(sb.toString());
        } else {
            throw new UnsupportedOperationException(
                    "Roi-based values unsupported");
        }
    }

}