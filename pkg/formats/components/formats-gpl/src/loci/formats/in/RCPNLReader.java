/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in;

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
