/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.tiff;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import loci.common.enumeration.CodedEnum;
import loci.common.enumeration.EnumException;

/**
 * Utility class for working with TIFF photometric interpretations.
 */
public enum PhotoInterp implements CodedEnum {

  WHITE_IS_ZERO(0, "WhiteIsZero", "Monochrome"),
  BLACK_IS_ZERO(1, "BlackIsZero", "Monochrome"),
  RGB(2, "RGB", "RGB"),
  RGB_PALETTE(3, "Palette", "Monochrome"),
  TRANSPARENCY_MASK(4, "Transparency Mask", "RGB"),
  CMYK(5, "CMYK", "CMYK"),
  Y_CB_CR(6, "YCbCr", "RGB"),
  CIE_LAB(8, "CIELAB", "RGB"),
  CFA_ARRAY(32803, "Color Filter Array", "RGB");

  /** Default luminance values for YCbCr data. */
  public static final float LUMA_RED = 0.299f;
  public static final float LUMA_GREEN = 0.587f;
  public static final float LUMA_BLUE = 0.114f;

  /** Code for the IFD type in the actual TIFF file. */
  private int code;

  /** Given name of the photometric interpretation. */
  private String name;

  /** Metadata type of the photometric interpretation. */
  private String metadataType;

  private static final Map<Integer,PhotoInterp> lookup =
    new HashMap<Integer,PhotoInterp>();

  /** Reverse lookup of code to IFD type enumerate value. */
  static {
    for(PhotoInterp v : EnumSet.allOf(PhotoInterp.class)) {
      lookup.put(v.getCode(), v);
    }
  }

  // -- Constructor --

  /**
   * Default constructor.
   * @param code Integer "code" for the photometric interpretation.
   * @param name Given name of the photometric interpretation.
   * @param metadataType Metadata type of the photometric interpretation.
   */
  private PhotoInterp(int code, String name, String metadataType) {
    this.code = code;
    this.name = name;
    this.metadataType = metadataType;
  }

  // -- PhotoInterp methods --

  /**
   * Retrieves a photometric interpretation by reverse lookup of its "code".
   * @param code The code to look up.
   * @return The <code>PhotoInterp</code> instance for the
   * <code>code</code> or <code>null</code> if it does not exist.
   */
  public static PhotoInterp get(int code) {
    PhotoInterp toReturn = lookup.get(code);
    if (toReturn == null) {
      throw new EnumException("Unable to find PhotoInterp with code: " + code);
    }
    return toReturn;
  }

  /* (non-Javadoc)
   * @see loci.common.CodedEnum#getCode()
   */
  @Override
  public int getCode() {
    return code;
  }

  /**
   * Retrieves the given name of the photometric interpretation. 
   * @return See above.
   */
  public String getName() {
    return name;
  }

  /**
   * Retrieves the metadata type of the photometric interpretation.
   * @return See above.
   */
  public String getMetadataType() {
    return metadataType;
  }

}
