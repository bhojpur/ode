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

/**
 * TurboJPEG lossless transformer
 */
public class TJTransformer extends TJDecompressor {

  /**
   * Create a TurboJPEG lossless transformer instance.
   */
  public TJTransformer() throws Exception {
    init();
  }

  /**
   * Create a TurboJPEG lossless transformer instance and associate the JPEG
   * image stored in <code>jpegImage</code> with the newly-created instance.
   *
   * @param jpegImage JPEG image buffer (size of the JPEG image is assumed to
   * be the length of the array)
   */
  public TJTransformer(byte[] jpegImage) throws Exception {
    init();
    setJPEGImage(jpegImage, jpegImage.length);
  }

  /**
   * Create a TurboJPEG lossless transformer instance and associate the JPEG
   * image of length <code>imageSize</code> bytes stored in
   * <code>jpegImage</code> with the newly-created instance.
   *
   * @param jpegImage JPEG image buffer
   *
   * @param imageSize size of the JPEG image (in bytes)
   */
  public TJTransformer(byte[] jpegImage, int imageSize) throws Exception {
    init();
    setJPEGImage(jpegImage, imageSize);
  }

  /**
   * Losslessly transform the JPEG image associated with this transformer
   * instance into one or more JPEG images stored in the given destination
   * buffers.  Lossless transforms work by moving the raw coefficients from one
   * JPEG image structure to another without altering the values of the
   * coefficients.  While this is typically faster than decompressing the
   * image, transforming it, and re-compressing it, lossless transforms are not
   * free.  Each lossless transform requires reading and Huffman decoding all
   * of the coefficients in the source image, regardless of the size of the
   * destination image.  Thus, this method provides a means of generating
   * multiple transformed images from the same source or of applying multiple
   * transformations simultaneously, in order to eliminate the need to read the
   * source coefficients multiple times.
   *
   * @param dstBufs an array of image buffers.  <code>dstbufs[i]</code> will
   * receive a JPEG image that has been transformed using the parameters in
   * <code>transforms[i]</code>.  Use {@link TJ#bufSize} to determine the
   * maximum size for each buffer based on the cropped width and height.
   *
   * @param transforms an array of {@link TJTransform} instances, each of
   * which specifies the transform parameters and/or cropping region for the
   * corresponding transformed output image
   *
   * @param flags the bitwise OR of one or more of {@link TJ TJ.FLAG_*}
   */
  public void transform(byte[][] dstBufs, TJTransform[] transforms,
    int flags) throws Exception {
    if(jpegBuf == null) throw new Exception("JPEG buffer not initialized");
    transformedSizes = transform(jpegBuf, jpegBufSize, dstBufs, transforms,
      flags);
  }
  
  /**
   * Losslessly transform the JPEG image associated with this transformer
   * instance and return an array of {@link TJDecompressor} instances, each of
   * which has a transformed JPEG image associated with it.
   *
   * @param transforms an array of {@link TJTransform} instances, each of
   * which specifies the transform parameters and/or cropping region for the
   * corresponding transformed output image
   *
   * @return an array of {@link TJDecompressor} instances, each of
   * which has a transformed JPEG image associated with it
   *
   * @param flags the bitwise OR of one or more of {@link TJ TJ.FLAG_*}
   */
  public TJDecompressor[] transform(TJTransform[] transforms, int flags)
    throws Exception {
    byte[][] dstBufs = new byte[transforms.length][];
    if(jpegWidth < 1 || jpegHeight < 1)
      throw new Exception("JPEG buffer not initialized");
    for(int i = 0; i < transforms.length; i++) {
      int w = jpegWidth, h = jpegHeight;
      if((transforms[i].options & TJTransform.OPT_CROP) != 0) {
        if(transforms[i].width != 0) w = transforms[i].width;
        if(transforms[i].height != 0) h = transforms[i].height;
      }
      dstBufs[i] = new byte[TJ.bufSize(w, h, jpegSubsamp)];
    }
    TJDecompressor[] tjd = new TJDecompressor[transforms.length];
    transform(dstBufs, transforms, flags);
    for(int i = 0; i < transforms.length; i++)
      tjd[i] = new TJDecompressor(dstBufs[i], transformedSizes[i]);
    return tjd;
  }
  
  /**
   * Returns an array containing the sizes of the transformed JPEG images from
   * the most recent call to {@link #transform transform()}.
   *
   * @return an array containing the sizes of the transformed JPEG images from
   * the most recent call to {@link #transform transform()}
   */
  public int[] getTransformedSizes() throws Exception {
    if(transformedSizes == null)
      throw new Exception("No image has been transformed yet");
    return transformedSizes;
  }

  private native void init() throws Exception;

  private native int[] transform(byte[] srcBuf, int srcSize, byte[][] dstBufs,
    TJTransform[] transforms, int flags) throws Exception;

  static {
    TJLoader.load();
  }

  private int[] transformedSizes = null;
};
