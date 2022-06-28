package loci.formats.codec;

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

import loci.common.RandomAccessInputStream;

/**
 */
public class JPEGTileDecoder implements AutoCloseable {
  private ode.codecs.JPEGTileDecoder decoder;

  public JPEGTileDecoder() {
    decoder = new ode.codecs.JPEGTileDecoder();
  }

  // -- JPEGTileDecoder API methods --

  public void initialize(String id, int imageWidth) {
    this.decoder.initialize(id, imageWidth);
  }

  public void initialize(RandomAccessInputStream in, int imageWidth) {
    this.decoder.initialize(in, imageWidth);
  }

  public void initialize(RandomAccessInputStream in, int y, int h,
    int imageWidth)
  {
    this.decoder.initialize(in, y, h, imageWidth);
  }

  /**
    * Pre-process the stream to make sure that the
    * image width and height are non-zero.  Returns an array containing
    * the image width and height.
    */
  public int[] preprocess(RandomAccessInputStream in) {
    return this.decoder.preprocess(in);
  }

  public byte[] getScanline(int y) {
    return this.decoder.getScanline(y);
  }

  public int getWidth() {
    return this.decoder.getWidth();
  }

  public int getHeight() {
    return this.decoder.getHeight();
  }

  public void close() {
    this.decoder.close();
  }

  ode.codecs.JPEGTileDecoder getWrapped() {
    return this.decoder;
  }

}
