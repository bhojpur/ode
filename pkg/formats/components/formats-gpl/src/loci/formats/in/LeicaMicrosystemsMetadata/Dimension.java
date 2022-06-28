package loci.formats.in.LeicaMicrosystemsMetadata;

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
 * This class represents image dimensions extracted from LMS image xmls.
 */
public class Dimension {

  // -- Fields --
  public DimensionKey key;
  public int size;
  public long bytesInc;
  public String unit = null;
  private Double length = 0d;
  private Double offByOneLength = 0d;
  public boolean oldPhysicalSize = false;
  public int frameIndex = 0;

  public enum DimensionKey {
    X(1, 'X'),
    Y(2, 'Y'),
    Z(3, 'Z'),
    T(4, 'T'),
    C(5, 'C'),
    S(10, 'S');

    public int id;
    public char token;

    DimensionKey(int id, char token) {
      this.id = id;
      this.token = token;
    }

    public static DimensionKey with(int id) {
      for (DimensionKey key : DimensionKey.values()) {
        if (key.id == id) {
          return key;
        }
      }
      return null;
    }
  }

  private static final long METER_MULTIPLY = 1000000;

  // -- Constructors --

  public Dimension(DimensionKey key, int size, long bytesInc, String unit, Double length, boolean oldPhysicalSize) {
    this.key = key;
    this.size = size;
    this.bytesInc = bytesInc;
    this.unit = unit;
    this.oldPhysicalSize = oldPhysicalSize;
    setLength(length);
  }

  private Dimension() {
  }

  public static Dimension createChannelDimension(int channelNumber, long bytesInc) {
    Dimension dimension = new Dimension();
    dimension.bytesInc = bytesInc;
    dimension.size = channelNumber;
    dimension.key = DimensionKey.C;
    return dimension;
  }

  // -- Methods --
  public void setLength(Double length) {
    this.length = length;
    offByOneLength = 0d;
    if (size > 1) {
      offByOneLength = this.length / size;
      this.length /= (size - 1); // length per pixel
    } else {
      this.length = 0d;
    }

    if (unit.equals("Ks")) {
      this.length /= 1000;
      offByOneLength /= 1000;
    } else if (unit.equals("m")) {
      this.length *= METER_MULTIPLY;
      offByOneLength *= METER_MULTIPLY;
    }
  }

  public Double getLength() {
    if (key == DimensionKey.X || key == DimensionKey.Y) {
      return oldPhysicalSize ? offByOneLength : length;
    } else {
      return length;
    }
  }
}
