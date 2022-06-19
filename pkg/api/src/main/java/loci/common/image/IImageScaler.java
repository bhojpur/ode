package loci.common.image;

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

/**
 * Interface defining image scaling operations.
 */
public interface IImageScaler {

  /**
   * Downsamples the given image.
   *
   * @param srcImage a byte array representing the image to be downsampled
   * @param width the width in pixels of the source image
   * @param height the height in pixels of the source image
   * @param scaleFactor the value used to calculate the downsampled width and height; expected to be greater than 1
   * @param bytesPerPixel the number of bytes in one pixel (usually 1, 2, 4, or 8)
   * @param littleEndian true if bytes in a pixel are stored in little endian order
   * @param floatingPoint true if the pixels should be interpreted as float or double instead of uint32/uint64
   * @param channels the number of RGB channels included in srcImage
   * @param interleaved true if the RGB channels are stored in interleaved order (RGBRGBRGB... and not RRR...GGG...BBB)
   * @return the downsampled image
   */
  byte[] downsample(byte[] srcImage, int width, int height, double scaleFactor,
    int bytesPerPixel, boolean littleEndian, boolean floatingPoint,
    int channels, boolean interleaved);

}