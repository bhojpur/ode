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
import java.awt.image.DataBufferShort;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

/**
 * ColorModel that handles 8, 16 and 32 bits per channel signed data.
 */
public class SignedColorModel extends ColorModel {

  // -- Fields --

  private int pixelBits;
  private int nChannels;
  private ComponentColorModel helper;

  private int max;

  // -- Constructors --

  public SignedColorModel(int pixelBits, int dataType, int nChannels)
    throws IOException
  {
    super(pixelBits, makeBitArray(nChannels, pixelBits),
      AWTImageTools.makeColorSpace(nChannels), nChannels == 4, false,
      ColorModel.TRANSLUCENT, dataType);

    int type = dataType;
    if (type == DataBuffer.TYPE_SHORT) {
      type = DataBuffer.TYPE_USHORT;
    }

    helper = new ComponentColorModel(AWTImageTools.makeColorSpace(nChannels),
      nChannels == 4, false, ColorModel.TRANSLUCENT, type);

    this.pixelBits = pixelBits;
    this.nChannels = nChannels;

    max = (int) Math.pow(2, pixelBits) - 1;
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
    if (pixelBits == 16) {
      return raster.getTransferType() == DataBuffer.TYPE_SHORT;
    }
    return helper.isCompatibleRaster(raster);
  }

  /* @see java.awt.image.ColorModel#createCompatibleWritableRaster(int, int) */
  @Override
  public WritableRaster createCompatibleWritableRaster(int w, int h) {
    if (pixelBits == 16) {
      int[] bandOffsets = new int[nChannels];
      for (int i=0; i<nChannels; i++) bandOffsets[i] = i;

      SampleModel m = new ComponentSampleModel(DataBuffer.TYPE_SHORT, w, h,
        nChannels, w * nChannels, bandOffsets);
      DataBuffer db = new DataBufferShort(w * h, nChannels);
      return Raster.createWritableRaster(m, db, null);
    }
    return helper.createCompatibleWritableRaster(w, h);
  }

  /* @see java.awt.image.ColorModel#getAlpha(int) */
  @Override
  public int getAlpha(int pixel) {
    if (nChannels < 4) return 255;
    return rescale(pixel, max);
  }

  /* @see java.awt.image.ColorModel#getBlue(int) */
  @Override
  public int getBlue(int pixel) {
    if (nChannels == 1) return getRed(pixel);
    return rescale(pixel, max);
  }

  /* @see java.awt.image.ColorModel#getGreen(int) */
  @Override
  public int getGreen(int pixel) {
    if (nChannels == 1) return getRed(pixel);
    return rescale(pixel, max);
  }

  /* @see java.awt.image.ColorModel#getRed(int) */
  @Override
  public int getRed(int pixel) {
    return rescale(pixel, max);
  }

  /* @see java.awt.image.ColorModel#getAlpha(Object) */
  @Override
  public int getAlpha(Object data) {
    if (data instanceof byte[]) {
      byte[] b = (byte[]) data;
      if (b.length == 1) return getAlpha(b[0]);
      return rescale(b.length == 4 ? b[0] : max, max);
    }
    else if (data instanceof short[]) {
      short[] s = (short[]) data;
      if (s.length == 1) return getAlpha(s[0]);
      return rescale(s.length == 4 ? s[0] : max, max);
    }
    else if (data instanceof int[]) {
      int[] i = (int[]) data;
      if (i.length == 1) return getAlpha(i[0]);
      return rescale(i.length == 4 ? i[0] : max, max);
    }
    return 0;
  }

  /* @see java.awt.image.ColorModel#getRed(Object) */
  @Override
  public int getRed(Object data) {
    if (data instanceof byte[]) {
      byte[] b = (byte[]) data;
      if (b.length == 1) return getRed(b[0]);
      return rescale(b.length != 4 ? b[0] : b[1]);
    }
    else if (data instanceof short[]) {
      short[] s = (short[]) data;
      if (s.length == 1) return getRed(s[0]);
      return rescale(s.length != 4 ? s[0] : s[1], max);
    }
    else if (data instanceof int[]) {
      int[] i = (int[]) data;
      if (i.length == 1) return getRed(i[0]);
      return rescale(i.length != 4 ? i[0] : i[1], max);
    }
    return 0;
  }

  /* @see java.awt.image.ColorModel#getGreen(Object) */
  @Override
  public int getGreen(Object data) {
    if (data instanceof byte[]) {
      byte[] b = (byte[]) data;
      if (b.length == 1) return getGreen(b[0]);
      return rescale(b.length != 4 ? b[1] : b[2]);
    }
    else if (data instanceof short[]) {
      short[] s = (short[]) data;
      if (s.length == 1) return getGreen(s[0]);
      return rescale(s.length != 4 ? s[1] : s[2], max);
    }
    else if (data instanceof int[]) {
      int[] i = (int[]) data;
      if (i.length == 1) return getGreen(i[0]);
      return rescale(i.length != 4 ? i[1] : i[2], max);
    }
    return 0;
  }

  /* @see java.awt.image.ColorModel#getBlue(Object) */
  @Override
  public int getBlue(Object data) {
    if (data instanceof byte[]) {
      byte[] b = (byte[]) data;
      if (b.length == 1) return getBlue(b[0]);
      return rescale(b.length > 2 ? b[b.length - 1] : 0);
    }
    else if (data instanceof short[]) {
      short[] s = (short[]) data;
      if (s.length == 1) return getBlue(s[0]);
      return rescale(s.length > 2 ? s[s.length - 1] : 0, max);
    }
    else if (data instanceof int[]) {
      int[] i = (int[]) data;
      if (i.length == 1) return getBlue(i[0]);
      return rescale(i.length > 2 ? i[i.length - 1] : 0, max);
    }
    return 0;
  }

  // -- Helper methods --

  private int rescale(int value, int max) {
    float v = (float) value / (float) max;
    v *= 255;
    return rescale((int) v);
  }

  private int rescale(int value) {
    if (value < 128) {
      value += 128; // [0, 127] -> [128, 255]
    }
    else {
      value -= 128; // [128, 255] -> [0, 127]
    }
    return value;
  }

  private static int[] makeBitArray(int nChannels, int nBits) {
    int[] bits = new int[nChannels];
    for (int i=0; i<bits.length; i++) {
      bits[i] = nBits;
    }
    return bits;
  }

}
