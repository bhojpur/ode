/*
 * Java bindings and pre-built binaries for libjpeg-turbo.
 */

package org.libjpegturbo.turbojpeg;

import java.awt.*;
import java.nio.*;

/**
 * Custom filter callback interface
 */
public interface TJCustomFilter {

  /**
   * A callback function that can be used to modify the DCT coefficients after
   * they are losslessly transformed but before they are transcoded to a new
   * JPEG file.  This allows for custom filters or other transformations to be
   * applied in the frequency domain.
   *
   * @param coeffBuffer a buffer containing transformed DCT coefficients.
   * (NOTE: this buffer is not guaranteed to be valid once the callback
   * returns, so applications wishing to hand off the DCT coefficients to
   * another function or library should make a copy of them within the body of
   * the callback.)
   *
   * @param bufferRegion rectangle containing the width and height of
   * <code>coeffBuffer</code> as well as its offset relative to the component
   * plane.  TurboJPEG implementations may choose to split each component plane
   * into multiple DCT coefficient buffers and call the callback function once
   * for each buffer.
   *
   * @param planeRegion rectangle containing the width and height of the
   * component plane to which <code>coeffBuffer</code> belongs
   *
   * @param componentID ID number of the component plane to which
   * <code>coeffBuffer</code>belongs (Y, Cb, and Cr have, respectively, ID's of
   * 0, 1, and 2 in typical JPEG images.)
   *
   * @param transformID ID number of the transformed image to which
   * <code>coeffBuffer</code> belongs.  This is the same as the index of the
   * transform in the transforms array that was passed to {@link
   * TJTransformer#transform TJTransformer.transform()}.
   *
   * @param transform a {@link TJTransform} instance that specifies the
   * parameters and/or cropping region for this transform
   */
  public void customFilter(ShortBuffer coeffBuffer, Rectangle bufferRegion,
    Rectangle planeRegion, int componentID, int transformID,
    TJTransform transform)
    throws Exception;
}
