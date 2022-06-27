/*
 * Top-level reader and writer APIs
 */

package loci.formats.meta;

import ode.xml.model.primitives.PositiveInteger;

/**
 * Interface for defining pyramid resolutions.
 */
public interface IPyramidStore {

  /**
   * Define the image width in pixels of the given pyramid resolution.
   * Both width and height must be defined for each desired resolution.
   *
   * @param sizeX the image width in pixels
   * @param image the Image (series) index
   * @param resolution the resolution index; expected to be greater than 0
   *    as the largest resolution is defined by the Image itself.
   *
   * @see #setResolutionSizeY(PositiveInteger, int, int)
   */
  void setResolutionSizeX(PositiveInteger sizeX, int image, int resolution);

  /**
   * Define the image height in pixels of the given pyramid resolution.
   * Both width and height must be defined for each desired resolution.
   *
   * @param sizeY the image height in pixels
   * @param image the Image (series) index
   * @param resolution the resolution index; expected to be greater than 0
   *    as the largest resolution is defined by the Image itself.
   *
   * @see #setResolutionSizeX(PositiveInteger, int, int)
   */
  void setResolutionSizeY(PositiveInteger sizeY, int image, int resolution);

  /**
   * Retrieve the number of pyramid resolutions defined for the given Image.
   *
   * @param image the Image (series) index
   * @return the number of valid pyramid resolutions, including the largest
   *    resolution; expected to be positive if the Image index is valid.
   */
  int getResolutionCount(int image);

  /**
   * Retrieve the image width in pixels of the given pyramid resolution.
   *
   * @param image the Image (series) index
   * @param resolution the resolution index; expected to be greater than 0
   *    as the largest resolution is defined by the Image itself.
   * @return the width in pixels, or null if either index is invalid
   */
  PositiveInteger getResolutionSizeX(int image, int resolution);

  /**
   * Retrieve the image height in pixels of the given pyramid resolution.
   *
   * @param image the Image (series) index
   * @param resolution the resolution index; expected to be greater than 0
   *    as the largest resolution is defined by the Image itself.
   * @return the height in pixels, or null if either index is invalid
   */
  PositiveInteger getResolutionSizeY(int image, int resolution);

}
