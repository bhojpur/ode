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

import java.util.LinkedHashMap;
import java.util.List;

import ode.formats.Index;
import ode.util.LSID;
import ode.metadatastore.IObjectContainer;
import ode.model.Arc;
import ode.model.Detector;
import ode.model.Filament;
import ode.model.Instrument;
import ode.model.Laser;
import ode.model.OTF;
import ode.model.Objective;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes the members of an Instrument (Objective, OTF, Arc Laser and
 * Filament) and ensures that the Instrument containers are present in the
 * container cache, adding them if they are missing.
 */
public class InstrumentProcessor implements ModelProcessor {
  /** Logger for this class */
  private Logger log = LoggerFactory.getLogger(InstrumentProcessor.class);

  /**
   * Processes the Bhojpur ODE client side metadata store.
   * @param store Bhojpur ODE metadata store to process.
   * @throws ModelException If there is an error during processing.
   */
  public void process(IObjectContainerStore store) throws ModelException {
    List<IObjectContainer> containers =
      store.getIObjectContainers(Detector.class);
    containers.addAll(store.getIObjectContainers(Objective.class));
    containers.addAll(store.getIObjectContainers(OTF.class));
    containers.addAll(store.getIObjectContainers(Arc.class));
    containers.addAll(store.getIObjectContainers(Laser.class));
    containers.addAll(store.getIObjectContainers(Filament.class));

    for (IObjectContainer container : containers) {
      Integer instrumentIndex = container.indexes.get(
          Index.INSTRUMENT_INDEX.getValue());
      Instrument instrument = (Instrument) store.getSourceObject(
          new LSID(Instrument.class, instrumentIndex));

      // If instrument is missing
      if (instrument == null) {
        LinkedHashMap<Index, Integer> indexes =
          new LinkedHashMap<Index, Integer>();
        indexes.put(Index.INSTRUMENT_INDEX, instrumentIndex);
        container = store.getIObjectContainer(Instrument.class, indexes);
        instrument = (Instrument) container.sourceObject;
      }
    }
  }

}