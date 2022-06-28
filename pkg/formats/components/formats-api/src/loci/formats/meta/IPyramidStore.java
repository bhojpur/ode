package loci.formats.meta;

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
