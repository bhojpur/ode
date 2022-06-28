package loci.formats.in;

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

import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;
import ode.units.UNITS;
import ode.units.quantity.Length;

public class RCPNLReader extends DeltavisionReader {

  public RCPNLReader() {
    super();
    format = "RCPNL";
    suffixes = new String[] {"rcpnl"};
    suffixNecessary = true;
    hasCompanionFiles = false;

    // if an *.rcpnl file is encountered, assume all timepoints are positions
    // the stage position values may not represent a uniform grid,
    // but should still be separate positions

    positionInT = true;
  }

  @Override
  public boolean isThisType(String name, boolean open) {
    return checkSuffix(name, "rcpnl") && super.isThisType(name, open);
  }

  @Override
  protected void populateObjective(MetadataStore store, int lensID)
    throws FormatException
  {
    super.populateObjective(store, lensID);

    switch (lensID) {
      case 18107:
        store.setObjectiveNominalMagnification(10.0, 0, 0);
        store.setObjectiveLensNA(0.30, 0, 0);
        store.setObjectiveWorkingDistance(
          new Length(15.0, UNITS.MILLIMETER), 0, 0);
        store.setObjectiveImmersion(MetadataTools.getImmersion("Air"), 0, 0);
        store.setObjectiveCorrection(
          MetadataTools.getCorrection("PlanFluor"), 0, 0);
        store.setObjectiveManufacturer("Nikon", 0, 0);
        break;
      case 18108:
        store.setObjectiveNominalMagnification(20.0, 0, 0);
        store.setObjectiveLensNA(0.75, 0, 0);
        store.setObjectiveCorrection(
          MetadataTools.getCorrection("PlanApo"), 0, 0);
        store.setObjectiveManufacturer("Nikon", 0, 0);
        break;
      case 18109:
        store.setObjectiveNominalMagnification(40.0, 0, 0);
        store.setObjectiveLensNA(0.95, 0, 0);
        store.setObjectiveCorrection(
          MetadataTools.getCorrection("PlanApo"), 0, 0);
        store.setObjectiveManufacturer("Nikon", 0, 0);
        break;
      case 18110:
        store.setObjectiveNominalMagnification(40.0, 0, 0);
        store.setObjectiveLensNA(0.60, 0, 0);
        store.setObjectiveCorrection(
          MetadataTools.getCorrection("PlanFluor"), 0, 0);
        store.setObjectiveManufacturer("Nikon", 0, 0);
        break;
      case 18111:
        store.setObjectiveNominalMagnification(4.0, 0, 0);
        store.setObjectiveLensNA(0.20, 0, 0);
        store.setObjectiveCorrection(
          MetadataTools.getCorrection("PlanApo"), 0, 0);
        store.setObjectiveManufacturer("Nikon", 0, 0);
        break;
    }
  }

}
