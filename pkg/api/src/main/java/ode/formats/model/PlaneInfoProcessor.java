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

import java.util.List;

import ode.formats.Index;
import ode.util.LSID;
import ode.metadatastore.IObjectContainer;
import ode.model.PlaneInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes the plane info sets of an IObjectContainerStore and removes
 * entities of this rapidly exploding object that have no metadata populated.
 */
public class PlaneInfoProcessor implements ModelProcessor {

  /** Logger for this class */
  private Logger log = LoggerFactory.getLogger(PlaneInfoProcessor.class);

  /**
   * Processes the Bhojpur ODE client side metadata store.
   * @param store Bhojpur ODE metadata store to process.
   * @throws ModelException If there is an error during processing.
   */
  public void process(IObjectContainerStore store) throws ModelException {
    List<IObjectContainer> containers =
        store.getIObjectContainers(PlaneInfo.class);
    for (IObjectContainer container : containers) {
      PlaneInfo pi = (PlaneInfo) container.sourceObject;
      if (pi.getDeltaT() == null &&
          pi.getExposureTime() == null &&
          pi.getPositionX() == null &&
          pi.getPositionY() == null &&
          pi.getPositionZ() == null) {
        LSID lsid = new LSID(
            PlaneInfo.class,
            container.indexes.get(Index.IMAGE_INDEX.getValue()),
            container.indexes.get(Index.PLANE_INDEX.getValue()));
        log.debug("Removing empty PlaneInfo: " + lsid);
        store.removeIObjectContainer(lsid);
      }
    }
  }
}