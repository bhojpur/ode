/*
 * Java bindings and pre-built binaries for libjpeg-turbo.
 */

package org.libjpegturbo.turbojpeg;

/**
 * Fractional scaling factor
 */
public class TJScalingFactor {

  public TJScalingFactor(int num, int denom) throws Exception {
    if(num < 1 || denom < 1)
      throw new Exception("Numerator and denominator must be >= 1");
    this.num = num;
    this.denom = denom;
  }

  /**
   * Returns numerator
   * @return numerator
   */
  public int getNum() {
    return num;
  }

  /**
   * Returns denominator
   * @return denominator
   */
  public int getDenom() {
    return denom;
  }

  /**
   * Returns the scaled value of <code>dimension</code>.  This function
   * performs the integer equivalent of
   * <code>ceil(dimension * scalingFactor)</code>.
   * @return the scaled value of <code>dimension</code>
   */
  public int getScaled(int dimension) {
    return (dimension * num + denom - 1) / denom;
  }

  /**
   * Returns true or false, depending on whether this instance and
   * <code>other</code> have the same numerator and denominator.
   * @return true or false, depending on whether this instance and
   * <code>other</code> have the same numerator and denominator
   */
  public boolean equals(TJScalingFactor other) {
    return (this.num == other.num && this.denom == other.denom);
  }

  /**
   * Returns true or false, depending on whether this instance is equal to
   * 1/1.
   * @return true or false, depending on whether this instance is equal to
   * 1/1
   */
  public boolean isOne() {
    return (num == 1 && denom == 1);
  }

  /**
   * Numerator
   */
  private int num = 1;

  /**
   * Denominator
   */
  private int denom = 1;
};
