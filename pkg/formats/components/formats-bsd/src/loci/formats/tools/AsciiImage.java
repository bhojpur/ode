package loci.formats.tools;

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

import java.awt.image.BufferedImage;

/**
 * A utility class for outputting a BufferedImage as ASCII text.
 */
public class AsciiImage {

  // -- Constants --

  private static final String NL = System.getProperty("line.separator");
  private static final String CHARS = " .,-+o*O#";

  // -- Fields --

  private BufferedImage img;

  // -- Constructor --

  public AsciiImage(BufferedImage img) {
    this.img = img;
  }

  // -- Object methods --

  @Override
  public String toString() {
    final int width = img.getWidth();
    final int height = img.getHeight();
    final StringBuilder sb = new StringBuilder();
    for (int y=0; y<height; y++) {
      for (int x=0; x<width; x++) {
        final int pix = img.getRGB(x, y);
        final int a = 0xff & (pix >> 24);
        final int r = 0xff & (pix >> 16);
        final int g = 0xff & (pix >> 8);
        final int b = 0xff & pix;
        final int avg = (r + g + b) / 3;
        sb.append(getChar(avg));
      }
      sb.append(NL);
    }
    return sb.toString();
  }

  // -- Helper methods --

  private char getChar(int value) {
    int index = CHARS.length() * value / 256;
    return CHARS.charAt(index);
  }

}
