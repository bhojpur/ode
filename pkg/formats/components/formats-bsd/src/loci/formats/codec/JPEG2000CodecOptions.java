/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.codec;

/**
 * Options for compressing and decompressing JPEG-2000 data.
 */
public class JPEG2000CodecOptions extends CodecOptions {

  // -- Fields --

  /**
   * The maximum code-block size to use per tile-component as it would be
   * provided to:
   * {@link com.sun.media.imageio.plugins.jpeg2000.J2KImageWriteParam#setCodeBlockSize(int[])}
   * (WRITE).
   */
  public int[] codeBlockSize;

  /**
   * The number of decomposition levels as would be provided to:
   * {@link com.sun.media.imageio.plugins.jpeg2000.J2KImageWriteParam#setNumDecompositionLevels(int)}
   * (WRITE). Leaving this value <code>null</code> signifies that when a JPEG
   * 2000 parameter set is created for the purposes of compression the number
   * of decomposition levels will be left as the default.
   */
  public Integer numDecompositionLevels;

  /**
   * The resolution level as would be provided to:
   * {@link com.sun.media.imageio.plugins.jpeg2000.J2KImageReadParam#setResolution(int)}
   * (READ). Leaving this value <code>null</code> signifies that when a JPEG
   * 2000 parameter set is created for the purposes of compression the number
   * of decomposition levels will be left as the default.
   */
  public Integer resolution;

  /**
   * Whether or not to write a boxed stream, i.e. with SOC and SIZ markers.
   * By default, a raw code stream is written.
   */
  public boolean writeBox = true;

  // -- Constructors --

  /** Creates a new instance. */
  public JPEG2000CodecOptions() {
    super();
  }

  /**
   * Creates a new instance with options.
   * @param options The option to set.
   */
  public JPEG2000CodecOptions(CodecOptions options) {
    super(options);
    if (options instanceof JPEG2000CodecOptions) {
      JPEG2000CodecOptions j2kOptions = (JPEG2000CodecOptions) options;
      if (j2kOptions.codeBlockSize != null) {
        codeBlockSize = j2kOptions.codeBlockSize;
      }
      numDecompositionLevels = j2kOptions.numDecompositionLevels;
      resolution = j2kOptions.resolution;
    }
  }

  // -- Static methods --

  /** Return JPEG2000CodecOptions with reasonable default values. */
  public static JPEG2000CodecOptions getDefaultOptions() {
    CodecOptions options = CodecOptions.getDefaultOptions();
    return getDefaultOptions(options);
  }

  /**
   * Return JPEG2000CodecOptions using the given CodecOptions as the default.
   * @param options The specified options.
   */
  public static JPEG2000CodecOptions getDefaultOptions(CodecOptions options) {
    JPEG2000CodecOptions j2kOptions = new JPEG2000CodecOptions(options);

    j2kOptions.quality = j2kOptions.lossless ? Double.MAX_VALUE : 10;
    j2kOptions.codeBlockSize = new int[] {64, 64};
    j2kOptions.writeBox = true;

    return j2kOptions;
  }

}
