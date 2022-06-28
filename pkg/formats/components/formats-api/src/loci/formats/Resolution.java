package loci.formats;

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
 * Represents a single subresolution in a pyramid.
 */
public class Resolution {

  // -- Fields --

  public PositiveInteger sizeX;
  public PositiveInteger sizeY;
  public int index;

  // -- Constructors --

  public Resolution(int index, int x, int y) {
    this.index = index;
    this.sizeX = new PositiveInteger(x);
    this.sizeY = new PositiveInteger(y);
  }

  public Resolution(int index, int fullX, int fullY, int scale) {
    this.index = index;
    int div = (int) Math.pow(scale, index);
    this.sizeX = new PositiveInteger(fullX / div);
    this.sizeY = new PositiveInteger(fullY / div);
  }

}
