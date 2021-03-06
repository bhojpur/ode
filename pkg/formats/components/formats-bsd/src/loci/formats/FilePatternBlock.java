package loci.formats;

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

import java.math.BigInteger;

/**
 * FilePatternBlock represents a single block in a {@link FilePattern}.
 */
public class FilePatternBlock {

  // -- Constants --

  /** Identifies the start of a block. */
  public static final String BLOCK_START = "<";

  /** Identifies the end of a block. */
  public static final String BLOCK_END = ">";

  // -- Fields --

  /** Elements within this block, e.g. ["R", "G", "B"] or ["1", "2", "3"]. */
  private String[] elements;

  /** Whether or not this is a fixed-width block. */
  private boolean fixed;

  /** Whether or not this is a numeric block. */
  private boolean numeric;

  /** The number of leading zeroes. */
  private int zeroes;

  /** String representation of this block. */
  private String block;

  private BigInteger begin = null, end = null, step = null;

  // -- Constructor --

  /**
   * Builds a FilePatternBlock from a block string.
   *
   * See {@link FilePattern} for block string syntax.
   *
   * @param block the block string.
   */
  public FilePatternBlock(String block) {
    this.block = block;
    explode();
  }

  // -- FilePatternBlock API methods --

  /**
   * Gets all block elements.
   *
   * @return an array containing all block elements.
   */
  public String[] getElements() {
    return elements;
  }

  /**
   * Gets the block string.
   *
   * @return the block string.
   */
  public String getBlock() {
    return block;
  }

  /**
   * Whether or not this is a fixed-width block.
   *
   * All elements in a fixed-width block have the same length (numbers
   * are zero-padded as needed).
   *
   * @return true if this is a fixed-width block, false otherwise.
   */
  public boolean isFixed() {
    return fixed;
  }

  /**
   * Whether or not this is a numeric block.
   *
   * All elements in a numeric block consist of digit characters.  For
   * instance, <code>&lt;10-15:2&gt;</code> expands to <code>10, 13,
   * 15</code>, all of which consist only of digits.
   *
   * @return true if this is a numeric block, false otherwise.
   */
  public boolean isNumeric() {
    return numeric;
  }

  /**
   * Gets the first element in a range block.
   *
   * @return the first block element
   */
  public BigInteger getFirst() {
    return begin;
  }

  /**
   * Gets the last element in a range block.
   *
   * @return the last block element
   */
  public BigInteger getLast() {
    return end;
  }

  /**
   * Gets the difference between consecutive elements in a range block.
   *
   * @return the step element as defined above.
   */
  public BigInteger getStep() {
    return step;
  }

  // -- Helper methods --

  private void throwBadBlock(String msgTemplate) {
    throw new IllegalBlockException(String.format(msgTemplate, block));
  }

  private void throwBadBlock(String msgTemplate, Throwable cause) {
    throw new IllegalBlockException(
        String.format(msgTemplate, block), cause
    );
  }

  private void setNumeric() {
    numeric = true;
    for (String s: elements) {
      try {
        new BigInteger(s);
      } catch (NumberFormatException e) {
        numeric = false;
        break;
      }
    }
  }

  private void setFixed() {
    fixed = true;
    int L = elements[0].length();
    for (int i = 1; i < elements.length; i++) {
      if (elements[i].length() != L) {
        fixed = false;
        break;
      }
    }
  }

  private void explode() {
    if (!block.startsWith(BLOCK_START) || !block.endsWith(BLOCK_END)) {
      throwBadBlock("\"%s\": missing block delimiter(s)");
    }
    String trimmed = block.substring(
        BLOCK_START.length(), block.length() - BLOCK_END.length()
    );
    elements = trimmed.split(",", -1);
    if (elements.length > 1) {
      setNumeric();
      setFixed();
      return;
    }
    elements = elements[0].split("-");
    if (elements.length < 2) {
      setNumeric();
      fixed = true;
      return;
    }
    String b = elements[0];
    elements = elements[1].split(":", -1);
    String e = elements[0];
    String s = (elements.length < 2) ? "1" : elements[1];

    numeric = true;

    try {
      begin = new BigInteger(b);
      end = new BigInteger(e);
      step = new BigInteger(s);
    } catch (NumberFormatException badN) {
      numeric = false;
      try {
        begin = new BigInteger(b, Character.MAX_RADIX);
        end = new BigInteger(e, Character.MAX_RADIX);
        step = new BigInteger(s, Character.MAX_RADIX);
      } catch (NumberFormatException badL) {
        throwBadBlock("invalid range delimiter(s)", badL);
      }
    }

    fixed = b.length() == e.length();
    zeroes = 0;
    for (zeroes=0; zeroes<e.length(); zeroes++) {
      if (e.charAt(zeroes) != '0') break;
    }

    int count = end.subtract(begin).divide(step).intValue() + 1;
    elements = new String[count];

    for (int i=0; i<count; i++) {
      BigInteger v = begin.add(step.multiply(BigInteger.valueOf(i)));
      String value = numeric ? v.toString() : v.toString(Character.MAX_RADIX);
      if (!numeric) {
        if (Character.isLowerCase(b.charAt(0))) value = value.toLowerCase();
        else value = value.toUpperCase();
      }
      int padChars = fixed ? e.length() - value.length() : 0;
      elements[i] = value;
      for (int j=0; j<padChars; j++) {
        elements[i] = "0" + elements[i];
      }
    }
  }

}
