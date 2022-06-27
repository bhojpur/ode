/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import loci.common.RandomAccessInputStream;

/**
 */
public class JPEGTileDecoder implements AutoCloseable {
  private ode.codecs.JPEGTileDecoder decoder;

  public JPEGTileDecoder() {
    decoder = new ode.codecs.JPEGTileDecoder();
  }

  // -- JPEGTileDecoder API methods --

  public void initialize(String id, int imageWidth) {
    this.decoder.initialize(id, imageWidth);
  }

  public void initialize(RandomAccessInputStream in, int imageWidth) {
    this.decoder.initialize(in, imageWidth);
  }

  public void initialize(RandomAccessInputStream in, int y, int h,
    int imageWidth)
  {
    this.decoder.initialize(in, y, h, imageWidth);
  }

  /**
    * Pre-process the stream to make sure that the
    * image width and height are non-zero.  Returns an array containing
    * the image width and height.
    */
  public int[] preprocess(RandomAccessInputStream in) {
    return this.decoder.preprocess(in);
  }

  public byte[] getScanline(int y) {
    return this.decoder.getScanline(y);
  }

  public int getWidth() {
    return this.decoder.getWidth();
  }

  public int getHeight() {
    return this.decoder.getHeight();
  }

  public void close() {
    this.decoder.close();
  }

  ode.codecs.JPEGTileDecoder getWrapped() {
    return this.decoder;
  }

}
