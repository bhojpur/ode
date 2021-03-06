package org.libjpegturbo.turbojpeg;

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

import java.awt.*;

/**
 * Lossless transform parameters
 */
public class TJTransform extends Rectangle {

  private static final long serialVersionUID = -127367705761430371L;

  /**
   * The number of lossless transform operations
   */
  final public static int NUMOP         = 8;
  /**
   * Do not transform the position of the image pixels.
   */
  final public static int OP_NONE       = 0;
  /**
   * Flip (mirror) image horizontally.  This transform is imperfect if there
   * are any partial MCU blocks on the right edge.
   * @see #OPT_PERFECT
   */
  final public static int OP_HFLIP      = 1;
  /**
   * Flip (mirror) image vertically.  This transform is imperfect if there are
   * any partial MCU blocks on the bottom edge.
   * @see #OPT_PERFECT
   */
  final public static int OP_VFLIP      = 2;
  /**
   * Transpose image (flip/mirror along upper left to lower right axis).  This
   * transform is always perfect.
   * @see #OPT_PERFECT
   */
  final public static int OP_TRANSPOSE  = 3;
  /**
   * Transverse transpose image (flip/mirror along upper right to lower left
   * axis).  This transform is imperfect if there are any partial MCU blocks in
   * the image.
   * @see #OPT_PERFECT
   */
  final public static int OP_TRANSVERSE = 4;
  /**
   * Rotate image clockwise by 90 degrees.  This transform is imperfect if
   * there are any partial MCU blocks on the bottom edge.
   * @see #OPT_PERFECT
   */
  final public static int OP_ROT90      = 5;
  /**
   * Rotate image 180 degrees.  This transform is imperfect if there are any
   * partial MCU blocks in the image.
   * @see #OPT_PERFECT
   */
  final public static int OP_ROT180     = 6;
  /**
   * Rotate image counter-clockwise by 90 degrees.  This transform is imperfect
   * if there are any partial MCU blocks on the right edge.
   * @see #OPT_PERFECT
   */
  final public static int OP_ROT270     = 7;


  /**
   * This option will cause {@link TJTransformer#transform
   * TJTransformer.transform()} to throw an exception if the transform is not
   * perfect.  Lossless transforms operate on MCU blocks, whose size depends on
   * the level of chrominance subsampling used.  If the image's width or height
   * is not evenly divisible by the MCU block size (see {@link TJ#getMCUWidth}
   * and {@link TJ#getMCUHeight}), then there will be partial MCU blocks on the
   * right and/or bottom edges.   It is not possible to move these partial MCU
   * blocks to the top or left of the image, so any transform that would
   * require that is "imperfect."  If this option is not specified, then any
   * partial MCU blocks that cannot be transformed will be left in place, which
   * will create odd-looking strips on the right or bottom edge of the image.
   */
  final public static int OPT_PERFECT  = 1;
  /**
   * This option will discard any partial MCU blocks that cannot be
   * transformed.
   */
  final public static int OPT_TRIM     = 2;
  /**
   * This option will enable lossless cropping.
   */
  final public static int OPT_CROP     = 4;
  /**
   * This option will discard the color data in the input image and produce
   * a grayscale output image.
   */
  final public static int OPT_GRAY     = 8;
  /**
   * This option will prevent {@link TJTransformer#transform
   * TJTransformer.transform()} from outputting a JPEG image for this
   * particular transform.  This can be used in conjunction with a custom
   * filter to capture the transformed DCT coefficients without transcoding
   * them.
   */
  final public static int OPT_NOOUTPUT = 16;

  
  /**
   * Create a new lossless transform instance.
   */
  public TJTransform() {
  }

  /**
   * Create a new lossless transform instance with the given parameters.
   *
   * @param x the left boundary of the cropping region.  This must be evenly
   * divisible by the MCU block width (see {@link TJ#getMCUWidth})
   *
   * @param y the upper boundary of the cropping region.  This must be evenly
   * divisible by the MCU block height (see {@link TJ#getMCUHeight})
   *
   * @param w the width of the cropping region.  Setting this to 0 is the
   * equivalent of setting it to the width of the source JPEG image - x.
   *
   * @param h the height of the cropping region.  Setting this to 0 is the
   * equivalent of setting it to the height of the source JPEG image - y.
   *
   * @param op one of the transform operations (<code>OP_*</code>)
   *
   * @param options the bitwise OR of one or more of the transform options
   * (<code>OPT_*</code>)
   *
   * @param cf an instance of an object that implements the {@link
   * TJCustomFilter} interface, or null if no custom filter is needed
   */
  public TJTransform(int x, int y, int w, int h, int op, int options,
    TJCustomFilter cf) throws Exception {
    super(x, y, w, h);
    this.op = op;  this.options = options;  this.cf = cf;
  }

  /**
   * Create a new lossless transform instance with the given parameters.
   *
   * @param r a <code>Rectangle</code> instance that specifies the cropping
   * region.  See {@link
   * #TJTransform(int, int, int, int, int, int, TJCustomFilter)} for more
   * detail.
   *
   * @param op one of the transform operations (<code>OP_*</code>)
   *
   * @param options the bitwise OR of one or more of the transform options
   * (<code>OPT_*</code>)
   *
   * @param cf an instance of an object that implements the {@link
   * TJCustomFilter} interface, or null if no custom filter is needed
   */
  public TJTransform(Rectangle r, int op, int options,
    TJCustomFilter cf) throws Exception {
    super(r);
    this.op = op;  this.options = options;  this.cf = cf;
  }

  /**
   * Transform operation (one of <code>OP_*</code>)
   */
  public int op = 0;

  /**
   * Transform options (bitwise OR of one or more of <code>OPT_*</code>)
   */
  public int options = 0;

  /**
   * Custom filter instance
   */
  public TJCustomFilter cf = null;
}
