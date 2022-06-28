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

import java.awt.image.BufferedImage;
import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ReaderWrapper;

/**
 * A reader wrapper for reading image planes as BufferedImage objects.
 */
public class BufferedImageReader extends ReaderWrapper {

  // -- Utility methods --

  /**
   * Converts the given reader into a BufferedImageReader, wrapping if needed.
   */
  public static BufferedImageReader makeBufferedImageReader(IFormatReader r) {
    if (r instanceof BufferedImageReader) return (BufferedImageReader) r;
    return new BufferedImageReader(r);
  }

  // -- Constructors --

  /** Constructs a BufferedImageReader around a new image reader. */
  public BufferedImageReader() { super(); }

  /** Constructs a BufferedImageReader with the given reader. */
  public BufferedImageReader(IFormatReader r) { super(r); }

  // -- BufferedImageReader methods --

  /** Obtains the specified image from the current file. */
  public BufferedImage openImage(int no) throws FormatException, IOException {
    return openImage(no, 0, 0, getSizeX(), getSizeY());
  }

  /**
   * Obtains a sub-image of the specified image, whose upper-left corner is
   * given by (x, y).
   */
  public BufferedImage openImage(int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    Class dataType = getNativeDataType();
    if (BufferedImage.class.isAssignableFrom(dataType)) {
      // native data type is compatible with BufferedImage
      return (BufferedImage) openPlane(no, x, y, w, h);
    }
    else {
      // must construct BufferedImage from byte array
      return AWTImageTools.openImage(openBytes(no, x, y, w, h), this, w, h);
    }
  }

  /** Obtains a thumbnail for the specified image from the current file. */
  public BufferedImage openThumbImage(int no)
    throws FormatException, IOException
  {
    Class dataType = getNativeDataType();
    if (BufferedImage.class.isAssignableFrom(dataType)) {
      BufferedImage img = AWTImageTools.makeUnsigned(openImage(no));
      return AWTImageTools.scale(img, getThumbSizeX(), getThumbSizeY(), false);
    }

    byte[] thumbBytes = openThumbBytes(no);
    return AWTImageTools.openImage(thumbBytes, this,
      getThumbSizeX(), getThumbSizeY());
  }

}
