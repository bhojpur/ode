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

import java.io.File;

import loci.common.Location;

/**
 * Utility class helping with generating human-readable resource names
 * based on alphanumeric indexing.
 */
public class ResourceNamer {

  public static final String PLATE = "Plate";

  public static final String RUN = "Run";

  public static final String WELL = "Well";

  public static final String FIELD = "Field";

  public static final String FAKE_EXT = "fake";

  public static final String DOT = ".";

  private static final int ALPHABET_LENGTH = 26;

  private int resourceCount = 0;

  private char letter, tmpLetter;

  public ResourceNamer(int resourceCount) {
    if (resourceCount < 0) {
      throw new IllegalArgumentException();
    }
    this.resourceCount = resourceCount;
    restartAlphabet();
  }

  /**
   * Creates a new {@link Location} instance using the provided parent path and
   * child node name. Concatenates the child name with a numerical index that
   * acts as an incrementing counter of node elements.
   * 
   * @param resourceParentPath
   *          Path to the parent element.
   * @param resourceName
   *          Template string used for naming the child resource.
   * @param nameIndex
   *          Numerical value used for naming the child resource.
   * @param resourceExtension
   *          Optional extension (if the child resource is a file) or path
   *          separator (if folder).
   * @return {@link Location} New instance representing the parent and child
   *          resources.
   */
  public Location getLocationFromResourceName(Location resourceParentPath,
      String resourceName, int nameIndex, String resourceExtension) {
    StringBuilder sb = new StringBuilder();
    sb.append(resourceName + String.format("%03d", nameIndex));
    if (resourceExtension != null) {
      if (!resourceExtension.startsWith(File.separator)) {
        sb.append(DOT);
      }
      sb.append(resourceExtension);
    }
    return new Location(resourceParentPath, sb.toString());
  }

  /**
   * Resets the internal state of the class so that it starts alphabet
   * generation from the first letter (ASCII A).
   */
  public void restartAlphabet() {
    letter = tmpLetter = 'A';
  }

  /**
   * Increments the internal alphabet pointer to the next element of the
   * alphabet.
   */
  public void nextLetter() {
    tmpLetter++;
    if (tmpLetter > 'Z') {
      tmpLetter = letter;
      letter++;
    }
  }

  /**
   * Returns a <code>String</code> representing a single letter (in the case
   * where the class instance has been initialized with a resource count lower
   * than 26) or a concatenation of two letters (if resource count is higher
   * than 26).
   * @return See above.
   */
  public String getLetter() {
    if (resourceCount > ALPHABET_LENGTH) {
      return Character.toString(letter) + Character.toString(tmpLetter);
    } else {
      return Character.toString(tmpLetter);
    }
  }

  public static int alphabeticIndexCount(String index) {
    int count = 0;
    char[] letters = index.toCharArray();
    for (int i = 0; i < letters.length; i++) {
      count += (letters[i] - 64) * Math.pow(ALPHABET_LENGTH, i);
    }
    return count;
  }

}
