package loci.formats.out;

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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.FormatWriter;
import loci.formats.gui.AWTImageTools;
import loci.formats.meta.MetadataRetrieve;

/**
 * ImageIOWriter is the superclass for file format writers that use the
 * javax.imageio library.
 */
public abstract class ImageIOWriter extends FormatWriter {

  // -- Fields --

  protected String kind;

  // -- Constructors --

  /**
   * Constructs an ImageIO-based writer with the given name, default suffix
   * and output type (e.g., png, jpeg).
   */
  public ImageIOWriter(String format, String suffix, String kind) {
    super(format, suffix);
    this.kind = kind;
  }

  /**
   * Constructs an ImageIO-based writer with the given name, default suffixes
   * and output type (e.g., png, jpeg). */
  public ImageIOWriter(String format, String[] suffixes, String kind) {
    super(format, suffixes);
    this.kind = kind;
  }

  // -- IFormatWriter API methods --

  /**
   * @see loci.formats.IFormatWriter#saveBytes(int, byte[], int, int, int, int)
   */
  @Override
  public void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    checkParams(no, buf, x, y, w, h);
    MetadataRetrieve meta = getMetadataRetrieve();
    BufferedImage image = AWTImageTools.makeImage(buf,
      interleaved, meta, series);
    savePlane(no, image, x, y, w, h);
  }

  /**
   * @see loci.formats.IFormatWriter#savePlane(int, Object, int, int, int, int)
   */
  @Override
  public void savePlane(int no, Object plane, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    if (!(plane instanceof Image)) {
      throw new IllegalArgumentException(
        "Object to save must be a java.awt.Image");
    }
    if (!isFullPlane(x, y, w, h)) {
      throw new FormatException("ImageIOWriter does not support writing tiles");
    }

    BufferedImage img = AWTImageTools.makeBuffered((Image) plane, cm);
    ImageIO.write(img, kind, out);
  }

  /* @see loci.formats.IFormatWriter#getPixelTypes(String) */
  @Override
  public int[] getPixelTypes(String codec) {
    return new int[] {FormatTools.UINT8, FormatTools.UINT16};
  }

  // -- IFormatHandler API methods --

  /* @see loci.formats.IFormatHandler#getNativeDataType() */
  @Override
  public Class<?> getNativeDataType() {
    return Image.class;
  }

}
