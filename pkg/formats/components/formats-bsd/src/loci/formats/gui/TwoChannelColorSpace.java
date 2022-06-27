/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.gui;

import java.awt.color.ColorSpace;

/**
 * ColorSpace for 2-channel images.
 */
public class TwoChannelColorSpace extends ColorSpace {

  // -- Constants --

  public static final int CS_2C = -1;

  private static final int NUM_COMPONENTS = 2;

  // -- Constructor --

  protected TwoChannelColorSpace(int type, int components) {
    super(type, components);
  }

  // -- ColorSpace API methods --

  @Override
  public float[] fromCIEXYZ(float[] color) {
    ColorSpace rgb = ColorSpace.getInstance(ColorSpace.CS_sRGB);
    return rgb.fromCIEXYZ(toRGB(color));
  }

  @Override
  public float[] fromRGB(float[] rgb) {
    return new float[] {rgb[0], rgb[1]};
  }

  public static ColorSpace getInstance(int colorSpace) {
    if (colorSpace == CS_2C) {
      return new TwoChannelColorSpace(ColorSpace.TYPE_2CLR, NUM_COMPONENTS);
    }
    return ColorSpace.getInstance(colorSpace);
  }

  @Override
  public String getName(int idx) {
    return idx == 0 ? "Red" : "Green";
  }

  @Override
  public int getNumComponents() {
    return NUM_COMPONENTS;
  }

  @Override
  public int getType() {
    return ColorSpace.TYPE_2CLR;
  }

  @Override
  public boolean isCS_sRGB() { return false; }

  @Override
  public float[] toCIEXYZ(float[] color) {
    ColorSpace rgb = ColorSpace.getInstance(ColorSpace.CS_sRGB);
    return rgb.toCIEXYZ(toRGB(color));
  }

  @Override
  public float[] toRGB(float[] color) {
    return new float[] {color[0], color[1], 0};
  }

}
