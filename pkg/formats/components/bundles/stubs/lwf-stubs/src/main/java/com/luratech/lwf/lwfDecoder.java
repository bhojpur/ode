package com.luratech.lwf;

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

/*
 * Luratech LWF library stub classes.
 */

import java.io.IOException;
import java.io.InputStream;

/**
 * Stub of the Luratech LuraWave&reg; Java decoder class.
 * NOTE: This class contains <b>NO</b> real implementation.
 */
public class lwfDecoder {

  // NB: This field is used to distinguish between this stub class and the
  // actual lwfDecoder implementation.
  public static final boolean IS_STUB = true;

  /**
	 * @param stream
   * @param password
   * @param licenseCode
   * @throws IOException
	 */
  public lwfDecoder(InputStream stream, String password, String licenseCode)
    throws IOException, SecurityException
  {
  }

  public int getWidth() {
    return -1;
  }

  public int getHeight() {
    return -1;
  }

  /**
	 * @param image
   * @param limit
   * @param quality
   * @param scale
	 */
  public void decodeToMemoryGray8(byte[] image, int limit,
    int quality, int scale) throws SecurityException
  {
  }

  /**
   * @param image
   * @param imageoffset
   * @param limit
   * @param quality
   * @param scale
   * @param pdx
   * @param pdy
   * @param clip_x
   * @param clip_y
   * @param clip_w
   * @param clip_h
   * @throws SecurityException
   */
  public void decodeToMemoryGray16(short[] image, int imageoffset, int limit,
    int quality, int scale, int pdx, int pdy, int clip_x, int clip_y,
    int clip_w, int clip_h) throws SecurityException
  {
  }

}