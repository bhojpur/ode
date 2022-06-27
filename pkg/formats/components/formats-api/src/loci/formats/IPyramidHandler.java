/*
 * Top-level reader and writer APIs
 */

package loci.formats;

/**
 * Interface for defining image pyramids.
 */
public interface IPyramidHandler {

  /**
   * Return the number of resolutions for the current series.
   *
   * Resolutions are stored in descending order, so the largest resolution is
   * first and the smallest resolution is last.
   */
  int getResolutionCount();

  /**
   * Set the resolution level.
   *
   * @see #getResolutionCount()
   */
  void setResolution(int resolution);

  /**
   * Get the current resolution level.
   *
   * @see #getResolutionCount()
   */
  int getResolution();

}
