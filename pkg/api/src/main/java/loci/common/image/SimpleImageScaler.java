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
 * Basic implementation of {@link IImageScaler}.
 * A n-by-n source region is transformed to 1 output pixel by
 * picking the upper-left-most of the n-by-n pixels.
 */
public class SimpleImageScaler implements IImageScaler {

  /**
   * @see loci.common.image.IImageScaler#downsample(byte[], int, int, double,
   *  int, boolean, boolean, int, boolean)
   */
  @Override
  public byte[] downsample(byte[] srcImage, int width, int height,
    double scaleFactor, int bytesPerPixel, boolean littleEndian,
    boolean floatingPoint, int channels, boolean interleaved)
  {
    if (scaleFactor < 1) {
      throw new IllegalArgumentException("Scale factor cannot be less than 1");
    }
    int newW = (int) (width / scaleFactor);
    int newH = (int) (height / scaleFactor);
    if (newW == 0 || newH == 0) {
      throw new IllegalArgumentException(
        "Scale factor too large; new width = " + newW +
        ", new height = " + newH);
    }
    if (newW == width && newH == height) {
      return srcImage;
    }

    int yd = (height / newH) * width - width;
    int yr = height % newH;
    int xd = width / newW;
    int xr = width % newW;

    byte[] outBuf = new byte[newW * newH * bytesPerPixel * channels];
    int count = interleaved ? 1 : channels;
    int pixelChannels = interleaved ? channels : 1;

    for (int c=0; c<count; c++) {
      int srcOffset = c * width * height;
      int destOffset = c * newW * newH;
      for (int yyy=newH, ye=0; yyy>0; yyy--) {
        for (int xxx=newW, xe=0; xxx>0; xxx--) {
          // for every pixel in the output image, pick the upper-left-most pixel
          // in the corresponding area of the source image, e.g. for a scale
          // factor of 2.0:
          //
          // ---------      -----
          // |a|b|c|d|      |a|c|
          // ---------      -----
          // |e|f|g|h|      |i|k|
          // --------- ==>  -----
          // |i|j|k|l|
          // ---------
          // |m|n|o|p|
          // ---------
          for (int rgb=0; rgb<pixelChannels; rgb++) {
            for (int b=0; b<bytesPerPixel; b++) {
              outBuf[bytesPerPixel * (destOffset * pixelChannels + rgb) + b] =
                srcImage[bytesPerPixel * (srcOffset * pixelChannels + rgb) + b];
            }
          }
          destOffset++;
          srcOffset += xd;
          xe += xr;
          if (xe >= newW) {
            xe -= newW;
            srcOffset++;
          }
        }
        srcOffset += yd;
        ye += yr;
        if (ye >= newH) {
          ye -= newH;
          srcOffset += width;
        }
      }
    }
    return outBuf;
  }

}