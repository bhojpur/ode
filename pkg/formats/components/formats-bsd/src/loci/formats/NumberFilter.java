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
import java.io.FileFilter;
import java.math.BigInteger;

/**
 * NumberFilter is a helper filter for FilePattern.findPattern().
 */
public class NumberFilter implements FileFilter {

  // -- Fields --

  /** String appearing before the numerical block. */
  private String pre;

  /** String appearing after the numerical block. */
  private String post;

  // -- Constructor --

  /**
   * Creates a filter for files containing a numerical block,
   * sandwiched between the given strings.
   */
  public NumberFilter(String pre, String post) {
    this.pre = pre;
    this.post = post;
  }

  // -- NumberFilter API methods --

  /** Gets numbers filling the asterisk positions. */
  public BigInteger getNumber(String name) {
    if (!name.startsWith(pre) || !name.endsWith(post)) return null;
    int ndx = pre.length();
    int end = name.length() - post.length();
    try { return new BigInteger(name.substring(ndx, end)); }
    catch (NumberFormatException exc) { return null; }
    catch (IndexOutOfBoundsException exc) { return null; }
  }

  /** Tests if a specified file should be included in a file list. */
  public boolean accept(String name) {
    return getNumber(name) != null;
  }

  // -- FileFilter API methods --

  /** Tests if a specified file should be included in a file list. */
  @Override
  public boolean accept(File pathname) {
    return accept(pathname.getName());
  }

}
