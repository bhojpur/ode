package loci.formats.codec;

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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import loci.common.enumeration.CodedEnum;
import loci.common.enumeration.EnumException;

/**
 * An enumeration of compression types.
 */
public enum CompressionType implements CodedEnum {

  UNCOMPRESSED(1, "Uncompressed"),
  ZLIB(2, "zlib"),
  CINEPAK(3, "Cinepak"),
  ANIMATION(4, "Animation"),
  H_263(5, "H.263"),
  SORENSON(6, "Sorenson"),
  SORENSON_3(7, "Sorenson 3"),
  MPEG_4(8, "MPEG 4"),
  LZW(9, "LZW"),
  J2K(10, "JPEG-2000"),
  J2K_LOSSY(11, "JPEG-2000 Lossy"),
  JPEG(12, "JPEG");
  
  /** Code for the compression. */
  private int code;
  
  /** The compression used. */
  private String compression;
  
  /** Map used to retrieve the compression type corresponding to the code. */
  private static final Map<Integer, CompressionType> lookup =
    new HashMap<Integer, CompressionType>();
  
  /** Reverse lookup of code to compression type enumerate value. */
  static {
    for(CompressionType v : EnumSet.allOf(CompressionType.class)) {
      lookup.put(v.getCode(), v);
    }
  }
  
  /**
   * Retrieves the compression by reverse lookup of its "code".
   * @param code The code to look up.
   * @return The <code>CompressionType</code> instance for the
   * <code>code</code> or <code>null</code> if it does not exist.
   */
  public static CompressionType get(int code) {
    CompressionType toReturn = lookup.get(code);
    if (toReturn == null) {
      throw new EnumException("Unable to find CompressionType with code: " +
        ""+code);
    }
    return toReturn;
  }
  
  /**
   * Default constructor.
   * @param code Integer "code" for the IFD type.
   * @param compression The type of compression.
   */
  private CompressionType(int code, String compression) {
    this.code = code;
    this.compression = compression;
  }
  
  /**
   * Implemented as specified by the {@link CodedEnum} I/F.
   * @see CodedEnum#getCode()
   */
  @Override
  public int getCode() {
    return code;
  }

  /**
   * Returns the compression.
   * @return See above.
   */
  public String getCompression() {
    return compression;
  }
  
}
