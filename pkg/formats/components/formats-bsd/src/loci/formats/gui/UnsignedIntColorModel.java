package loci.formats.gui;

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

import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

/**
 * ColorModel that handles unsigned 32 bit data.
 */
public class UnsignedIntColorModel extends ColorModel {

  // -- Fields --

  private int nChannels;
  private ComponentColorModel helper;

  // -- Constructors --

  public UnsignedIntColorModel(int pixelBits, int dataType, int nChannels)
    throws IOException
  {
    super(pixelBits, makeBitArray(nChannels, pixelBits),
      AWTImageTools.makeColorSpace(nChannels), nChannels == 4, false,
      ColorModel.TRANSLUCENT, dataType);

    helper = new ComponentColorModel(AWTImageTools.makeColorSpace(nChannels),
      nChannels == 4, false, ColorModel.TRANSLUCENT, dataType);

    this.nChannels = nChannels;
  }

  // -- ColorModel API methods --

  /* @see java.awt.image.ColorModel#getDataElements(int, Object) */
  @Override
  public synchronized Object getDataElements(int rgb, Object pixel) {
    return helper.getDataElements(rgb, pixel);
  }

  /* @see java.awt.image.ColorModel#isCompatibleRaster(Raster) */
  @Override
  public boolean isCompatibleRaster(Raster raster) {
    return raster.getNumBands() == getNumComponents() &&
      raster.getTransferType() == getTransferType();
  }

  /* @see java.awt.image.ColorModel#createCompatibleWritableRaster(int, int) */
  @Override
  public WritableRaster createCompatibleWritableRaster(int w, int h) {
    int[] bandOffsets = new int[nChannels];
    for (int i=0; i<nChannels; i++) bandOffsets[i] = i;

    SampleModel m = new ComponentSampleModel(DataBuffer.TYPE_INT, w, h,
      nChannels, w * nChannels, bandOffsets);
    DataBuffer db = new DataBufferInt(w * h, nChannels);
    return Raster.createWritableRaster(m, db, null);
  }

  /* @see java.awt.image.ColorModel#getAlpha(int) */
  @Override
  public int getAlpha(int pixel) {
    return (int) (Math.pow(2, 32) - 1);
  }

  /* @see java.awt.image.ColorModel#getBlue(int) */
  @Override
  public int getBlue(int pixel) {
    return getComponent(pixel, 3);
  }

  /* @see java.awt.image.ColorModel#getGreen(int) */
  @Override
  public int getGreen(int pixel) {
    return getComponent(pixel, 2);
  }

  /* @see java.awt.image.ColorModel#getRed(int) */
  @Override
  public int getRed(int pixel) {
    return getComponent(pixel, 1);
  }

  /* @see java.awt.image.ColorModel#getAlpha(Object) */
  @Override
  public int getAlpha(Object data) {
    int max = (int) Math.pow(2, 32) - 1;
    if (data instanceof int[]) {
      int[] i = (int[]) data;
      if (i.length == 1) return getAlpha(i[0]);
      return getAlpha(i.length == 4 ? i[0] : max);
    }
    return max;
  }

  /* @see java.awt.image.ColorModel#getRed(Object) */
  @Override
  public int getRed(Object data) {
    int max = (int) Math.pow(2, 32) - 1;
    if (data instanceof int[]) {
      int[] i = (int[]) data;
      if (i.length == 1) return getRed(i[0]);
      return getRed(i.length != 4 ? i[0] : i[1]);
    }
    return max;
  }

  /* @see java.awt.image.ColorModel#getGreen(Object) */
  @Override
  public int getGreen(Object data) {
    int max = (int) Math.pow(2, 32) - 1;
    if (data instanceof int[]) {
      int[] i = (int[]) data;
      if (i.length == 1) return getGreen(i[0]);
      return getGreen(i.length != 4 ? i[1] : i[2]);
    }
    return max;
  }

  /* @see java.awt.image.ColorModel#getBlue(Object) */
  @Override
  public int getBlue(Object data) {
    int max = (int) Math.pow(2, 32) - 1;
    if (data instanceof int[]) {
      int[] i = (int[]) data;
      if (i.length == 1) return getBlue(i[0]);
      return getBlue(i[i.length - 1]);
    }
    return max;
  }

  // -- Helper methods --

  private int getComponent(int pixel, int index) {
    long v = pixel & 0xffffffffL;
    double f = (double) v / (Math.pow(2, 32) - 1);
    f *= 255;
    return (int) f;
  }

  private static int[] makeBitArray(int nChannels, int nBits) {
    int[] bits = new int[nChannels];
    for (int i=0; i<bits.length; i++) {
      bits[i] = nBits;
    }
    return bits;
  }

}
