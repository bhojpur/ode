package ode.formats.model;

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
import java.util.LinkedHashMap;
import java.util.List;

import ode.formats.Index;
import ode.metadatastore.IObjectContainer;
import ode.model.Ellipse;
import ode.model.IObject;
import ode.model.Line;
import ode.model.Mask;
import ode.model.Path;
import ode.model.Point;
import ode.model.Polygon;
import ode.model.Polyline;
import ode.model.Rectangle;
import ode.model.Roi;
import ode.model.Label;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes the shapes of a IObjectContainerStore and ensures
 * that the ROI containers are present.
 */
public class ShapeProcessor implements ModelProcessor {

  /** Logger for this class */
  private Logger log = LoggerFactory.getLogger(ShapeProcessor.class);

  /** Exhaustive list of ROI types. */
  private static final List<Class<? extends IObject>> SHAPE_TYPES =
      new ArrayList<Class<? extends IObject>>();

  static {
    SHAPE_TYPES.add(Line.class);
    SHAPE_TYPES.add(Rectangle.class);
    SHAPE_TYPES.add(Mask.class);
    SHAPE_TYPES.add(Ellipse.class);
    SHAPE_TYPES.add(Point.class);
    SHAPE_TYPES.add(Polyline.class);
    SHAPE_TYPES.add(Path.class);
    SHAPE_TYPES.add(Label.class);
    // XXX: Unused ODE-XML type
    SHAPE_TYPES.add(Polygon.class);
  }

  /**
   * Processes the Bhojpur ODE client side metadata store.
   * @param store Bhojpur ODE metadata store to process.
   * @throws ModelException If there is an error during processing.
   */
  public void process(IObjectContainerStore store) throws ModelException {
    for (Class<? extends IObject> klass : SHAPE_TYPES) {
      List<IObjectContainer> containers =
        store.getIObjectContainers(klass);
      for (IObjectContainer container : containers) {
        Integer roiIndex = container.indexes.get(Index.ROI_INDEX.getValue());
        LinkedHashMap<Index, Integer> indexes =
            new LinkedHashMap<Index, Integer>();
        indexes.put(Index.ROI_INDEX, roiIndex);
        // Creates an ROI if one doesn't exist
        store.getIObjectContainer(Roi.class, indexes);
      }
    }
  }
}