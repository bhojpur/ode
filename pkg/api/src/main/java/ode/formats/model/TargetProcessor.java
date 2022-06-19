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

import ode.util.LSID;
import ode.metadatastore.IObjectContainer;
import ode.model.Dataset;
import ode.model.IObject;
import ode.model.Image;
import ode.model.Plate;
import ode.model.Screen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes the IObjectContainerStore and populates references for the
 * linkage target object of choice.
 */
public class TargetProcessor implements ModelProcessor {

  /** Logger for this class */
  private Logger log = LoggerFactory.getLogger(TargetProcessor.class);

  /**
   * Processes the Bhojpur ODE client side metadata store.
   * @param store Bhojpur ODE metadata store to process.
   * @throws ModelException If there is an error during processing.
   */
  public void process(IObjectContainerStore store) throws ModelException {
    IObject target = store.getUserSpecifiedTarget();
    if (target == null) {
      return;
    }

    List<IObjectContainer> containers = null;

    if (target instanceof Dataset) {
      containers = store.getIObjectContainers(Image.class);
    } else if (target instanceof Screen) {
      containers = store.getIObjectContainers(Plate.class);
    } else {
      throw new ModelException("Unable to handle target: " + target);
    }

    for (IObjectContainer container : containers) {
      LSID targetLSID = new LSID(container.LSID);
      LSID referenceLSID =
          new LSID(String.format("%s:%d", target.getClass().getName(),
                                 target.getId().getValue()));
      store.addReference(targetLSID, referenceLSID);
    }
  }
}