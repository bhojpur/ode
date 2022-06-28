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
import loci.formats.FormatTools;
import loci.formats.IFormatWriter;
import loci.formats.WriterWrapper;
import loci.formats.meta.MetadataRetrieve;

/**
 * A writer wrapper for writing image planes from BufferedImage objects.
 */
public class BufferedImageWriter extends WriterWrapper {

  // -- Utility methods --

  /**
   * Converts the given writer into a BufferedImageWriter, wrapping if needed.
   */
  public static BufferedImageWriter makeBufferedImageWriter(IFormatWriter w) {
    if (w instanceof BufferedImageWriter) return (BufferedImageWriter) w;
    return new BufferedImageWriter(w);
  }

  // -- Constructors --

  /** Constructs a BufferedImageWriter around a new image writer. */
  public BufferedImageWriter() { super(); }

  /** Constructs a BufferedImageWriter with the given writer. */
  public BufferedImageWriter(IFormatWriter r) { super(r); }

  // -- BufferedImageWriter methods --

  /**
   * Saves the given BufferedImage to the current file.
   *
   * @param no the plane index within the series.
   * @param image the BufferedImage to save.
   */
  public void saveImage(int no, BufferedImage image)
    throws FormatException, IOException
  {
    saveImage(no, image, 0, 0, image.getWidth(), image.getHeight());
  }

  /**
   * Saves the given BufferedImage to the current file.  The BufferedImage
   * may represent a subsection of the full image to be saved.
   *
   * @param no the plane index within the series.
   * @param image the BufferedImage to save.
   * @param x the X coordinate of the upper-left corner of the image.
   * @param y the Y coordinate of the upper-left corner of the image.
   * @param w the width (in pixels) of the image.
   * @param h the height (in pixels) of the image.
   */
  public void saveImage(int no, BufferedImage image, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    Class dataType = getNativeDataType();
    if (BufferedImage.class.isAssignableFrom(dataType)) {
      // native data type is compatible with BufferedImage
      savePlane(no, image, x, y, w, h);
    }
    else {
      // must convert BufferedImage to byte array
      byte[] buf = toBytes(image, this);

      saveBytes(no, buf, x, y, w, h);
    }
  }

  // -- Utility methods --

  public static byte[] toBytes(BufferedImage image, IFormatWriter writer) {
    boolean littleEndian = false;
    int bpp = FormatTools.getBytesPerPixel(AWTImageTools.getPixelType(image));

    MetadataRetrieve r = writer.getMetadataRetrieve();
    if (r != null) {
      Boolean bigEndian = false;
      if (r.getPixelsBigEndian(writer.getSeries()) != null)
      {
        bigEndian = r.getPixelsBigEndian(writer.getSeries()).booleanValue();
      }
      else if (r.getPixelsBinDataCount(writer.getSeries()) == 0) {
        bigEndian = r.getPixelsBinDataBigEndian(writer.getSeries(), 0).booleanValue();
      }
      if (bigEndian != null) littleEndian = !bigEndian.booleanValue();
    }

    byte[][] pixelBytes = AWTImageTools.getPixelBytes(image, littleEndian);
    byte[] buf = new byte[pixelBytes.length * pixelBytes[0].length];
    if (writer.isInterleaved()) {
      for (int i=0; i<pixelBytes[0].length; i+=bpp) {
        for (int j=0; j<pixelBytes.length; j++) {
          System.arraycopy(pixelBytes[j], i, buf,
            i * pixelBytes.length + j * bpp, bpp);
        }
      }
    }
    else {
      for (int i=0; i<pixelBytes.length; i++) {
        System.arraycopy(pixelBytes[i], 0, buf,
          i * pixelBytes[0].length, pixelBytes[i].length);
      }
    }
    pixelBytes = null;
    return buf;
  }

}
