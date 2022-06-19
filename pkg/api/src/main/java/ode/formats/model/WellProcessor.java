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

import static ode.rtypes.rint;
import static ode.rtypes.rstring;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import ode.formats.Index;
import ode.util.LSID;
import ode.metadatastore.IObjectContainer;
import ode.model.Plate;
import ode.model.PlateAcquisition;
import ode.model.Well;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes the Wells of an IObjectContainerStore and ensures that the Plate
 * has been populated and that it is validated and that if any PlateAcquisition
 * objects are in the hierarchy that they have a name.
 */
public class WellProcessor implements ModelProcessor {

  /** Logger for this class */
  private Logger log = LoggerFactory.getLogger(WellProcessor.class);

  /** Object container store to process. */
  private IObjectContainerStore store;

  /**
   * Processes the Bhojpur ODE client side metadata store.
   * @param store Bhojpur ODE metadata store to process.
   * @throws ModelException If there is an error during processing.
   */
  public void process(IObjectContainerStore store) throws ModelException {
    this.store = store;
    List<IObjectContainer> containers =
        store.getIObjectContainers(Well.class);
    for (IObjectContainer container : containers) {
      Integer plateIndex = container.indexes.get(Index.PLATE_INDEX.getValue());
      // Validate Plate
      Plate plate = validatePlate(plateIndex);
      Well well = (Well) container.sourceObject;
      if (well.getColumn() != null &&
          well.getColumn().getValue() >= plate.getColumns().getValue()) {
        plate.setColumns(rint(well.getColumn().getValue() + 1));
      }
      if (well.getRow() != null &&
          well.getRow().getValue() >= plate.getRows().getValue()) {
        plate.setRows(rint(well.getRow().getValue() + 1));
      }
    }
  }

  /**
   * Validates that a Plate object container exists and that the Plate source
   * object has a name and that its row and column count are initialized.
   * @param plateIndex Index of the plate within the data model.
   * @return The existing or created plate.
   */
  private Plate validatePlate(int plateIndex) {
    LinkedHashMap<Index, Integer> indexes =
        new LinkedHashMap<Index, Integer>();
    indexes.put(Index.PLATE_INDEX, plateIndex);
    IObjectContainer container =
        store.getIObjectContainer(Plate.class, indexes);
    Plate plate = (Plate) container.sourceObject;
    String userSpecifiedPlateName = store.getUserSpecifiedName();
    String userSpecifiedPlateDescription = store.getUserSpecifiedDescription();

    if (userSpecifiedPlateName != null) {
      plate.setName(rstring(userSpecifiedPlateName));
    }
    if (plate.getName() == null || plate.getName().getValue() == null ||
      plate.getName().getValue().isEmpty())
    {
      log.warn("Missing plate name for: " + container.LSID);
      String filename = store.getReader().getCurrentFile();
      filename = new File(filename).getName();
      plate.setName(rstring(filename));
    }

    if (userSpecifiedPlateDescription != null) {
      plate.setDescription(rstring(userSpecifiedPlateDescription));
    }
    if (plate.getRows() == null) {
      plate.setRows(rint(1));
    }
    if (plate.getColumns() == null) {
      plate.setColumns(rint(1));
    }
    return plate;
  }
}