/*
 * Top-level reader and writer APIs
 */

package loci.formats.codec;

import java.awt.image.ColorModel;

/**
 * Options for compressing and decompressing data.
 */
public class CodecOptions {

  /** Width, in pixels, of the image. (READ/WRITE) */
  public int width;

  /** Height, in pixels, of the image. (READ/WRITE) */
  public int height;

  /** Number of channels. (READ/WRITE) */
  public int channels;

  /** Number of bits per channel. (READ/WRITE) */
  public int bitsPerSample;

  /** Indicates endianness of pixel data. (READ/WRITE) */
  public boolean littleEndian;

  /** Indicates whether or not channels are interleaved. (READ/WRITE) */
  public boolean interleaved;

  /** Indicates whether or not the pixel data is signed. (READ/WRITE) */
  public boolean signed;

  /**
   * Tile width as it would be provided to:
   * {@link javax.imageio.ImageWriteParam#setTiling(int, int, int, int)}
   * (WRITE).
   */
  public int tileWidth;

  /**
   * Tile height as it would be provided to:
   * {@link javax.imageio.ImageWriteParam#setTiling(int, int, int, int)}
   * (WRITE).
   */
  public int tileHeight;

  /**
   * Horizontal offset of the tile grid as it would be provided to:
   * {@link javax.imageio.ImageWriteParam#setTiling(int, int, int, int)}
   * (WRITE).
   */
  public int tileGridXOffset;

  /**
   * Vertical offset of the tile grid as it would be provided to:
   * {@link javax.imageio.ImageWriteParam#setTiling(int, int, int, int)}
   * (WRITE).
   */
  public int tileGridYOffset;

  /**
   * If compressing, this is the maximum number of raw bytes to compress.
   * If decompressing, this is the maximum number of raw bytes to return.
   * (READ/WRITE).
   */
  public int maxBytes;

  /** Pixels for preceding image (READ/WRITE). */
  public byte[] previousImage;

  /**
   * Used with codecs allowing lossy and lossless compression.
   * Default is set to true (WRITE).
   */
  public boolean lossless;

  /** Color model to use when constructing an image (WRITE).*/
  public ColorModel colorModel;

  /** Compression quality level as it would be provided to:
   * {@link javax.imageio.ImageWriteParam#compressionQuality} (WRITE).
   */
  public double quality;

  /**
   * Whether or not the decompressed data will be stored as YCbCr.
   */
  public boolean ycbcr;

  // -- Constructors --

  /** Construct a new CodecOptions. */
  public CodecOptions() {}

  /** Construct a new CodecOptions using the given CodecOptions. */
  public CodecOptions(CodecOptions options) {
    if (options != null) {
      this.width = options.width;
      this.height = options.height;
      this.channels = options.channels;
      this.bitsPerSample = options.bitsPerSample;
      this.littleEndian = options.littleEndian;
      this.interleaved = options.interleaved;
      this.signed = options.signed;
      this.maxBytes = options.maxBytes;
      this.previousImage = options.previousImage;
      this.lossless = options.lossless;
      this.colorModel = options.colorModel;
      this.quality = options.quality;
      this.tileWidth = options.tileWidth;
      this.tileHeight = options.tileHeight;
      this.tileGridXOffset = options.tileGridXOffset;
      this.tileGridYOffset = options.tileGridYOffset;
      this.ycbcr = options.ycbcr;
    }
  }

  // -- Static methods --

  /** Return CodecOptions with reasonable default values. */
  public static CodecOptions getDefaultOptions() {
    CodecOptions options = new CodecOptions();
    options.littleEndian = false;
    options.interleaved = false;
    options.lossless = true;
    options.ycbcr = false;
    return options;
  }

}
