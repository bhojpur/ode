package ode.services.roi;

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

import java.util.HashMap;
import java.util.Map;

import ode.model.Ellipse;
import ode.model.Label;
import ode.model.Line;
import ode.model.Mask;
import ode.model.Path;
import ode.model.Point;
import ode.model.Polygon;
import ode.model.Polyline;
import ode.model.Rectangle;
import ode.model.Roi;
import ode.model.Shape;
import ode.model.SmartEllipseI;
import ode.model.SmartLineI;
import ode.model.SmartMaskI;
import ode.model.SmartPathI;
import ode.model.SmartPointI;
import ode.model.SmartPolygonI;
import ode.model.SmartPolylineI;
import ode.model.SmartRectI;
import ode.model.SmartTextI;
import ode.util.ObjectFactoryRegistry;
import ode.util.ObjectFactoryRegistry.ObjectFactory;

/**
 * Intelligent server-side representations of the {@link Roi} and {@link Shape}
 * types. Registered manually in server-config.xml. This class manages the
 * ObjectFactories for the various types. Similar to java.util.Arrays.
 */
public abstract class RoiTypes {

    // Object factories
    // =========================================================================

    public static class RoiTypesObjectFactoryRegistry extends ObjectFactoryRegistry {

        @Override
        public Map<String, ObjectFactory> createFactories(Ice.Communicator ic) {
            Map<String, ObjectFactory> factories = new HashMap<String, ObjectFactory>();

            factories.put(SmartEllipseI.ice_staticId(), new ObjectFactory(Ellipse.ice_staticId()) {

                @Override
                public Ellipse create(String name) {
                    return new SmartEllipseI();
                }

            });

            factories.put(SmartLineI.ice_staticId(), new ObjectFactory(Line.ice_staticId()) {

                @Override
                public Line create(String name) {
                    return new SmartLineI();
                }

            });

            factories.put(SmartMaskI.ice_staticId(), new ObjectFactory(Mask.ice_staticId()) {

                @Override
                public Mask create(String name) {
                    return new SmartMaskI();
                }

            });

            factories.put(SmartPathI.ice_staticId(), new ObjectFactory(Path.ice_staticId()) {

                @Override
                public Path create(String name) {
                    return new SmartPathI();
                }

            });

            factories.put(SmartPointI.ice_staticId(), new ObjectFactory(Point.ice_staticId()) {

                @Override
                public Point create(String name) {
                    return new SmartPointI();
                }

            });

            factories.put(SmartPolygonI.ice_staticId(), new ObjectFactory(Polygon.ice_staticId()) {

                @Override
                public Polygon create(String name) {
                    return new SmartPolygonI();
                }

            });

            factories.put(SmartPolylineI.ice_staticId(),
                    new ObjectFactory(Polyline.ice_staticId()) {

                        @Override
                        public Polyline create(String name){
                            return new SmartPolylineI();
                        }

                    });

            factories.put(SmartRectI.ice_staticId(), new ObjectFactory(Rectangle.ice_staticId()) {

                @Override
                public Rectangle create(String name) {
                    return new SmartRectI();
                }

            });

            factories.put(SmartTextI.ice_staticId(), new ObjectFactory(Label.ice_staticId()) {

                @Override
                public Label create(String name) {
                    return new SmartTextI();
                }

            });

            return factories;
        }

    }

}