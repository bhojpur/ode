/*
 * Top-level reader and writer APIs
 */

package loci.formats;

import ode.xml.model.primitives.PositiveInteger;

/**
 * Represents a single subresolution in a pyramid.
 */
public class Resolution {

  // -- Fields --

  public PositiveInteger sizeX;
  public PositiveInteger sizeY;
  public int index;

  // -- Constructors --

  public Resolution(int index, int x, int y) {
    this.index = index;
    this.sizeX = new PositiveInteger(x);
    this.sizeY = new PositiveInteger(y);
  }

  public Resolution(int index, int fullX, int fullY, int scale) {
    this.index = index;
    int div = (int) Math.pow(scale, index);
    this.sizeX = new PositiveInteger(fullX / div);
    this.sizeY = new PositiveInteger(fullY / div);
  }

}
