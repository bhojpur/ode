package loci.common;

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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * StreamHandle implementation for reading from BZip2-compressed files
 * or byte arrays.  Instances of BZip2Handle are read-only.
 *
 * @see StreamHandle
 */
public class BZip2Handle extends StreamHandle {

  // -- Constructor --

  /**
   * Construct a new BZip2Handle corresponding to the given file.
   *
   * @param file the path to a file on disk
   * @throws HandleException if the given file is not a BZip2 file.
   */
  public BZip2Handle(String file) throws IOException {
    super();
    this.file = file;
    if (!isBZip2File(file)) {
      throw new HandleException(file + " is not a BZip2 file.");
    }

    resetStream();

    length = 0;
    while (true) {
      int skip = stream.skipBytes(1024);
      if (skip <= 0) {
        break;
      }
      length += skip;
    }

    resetStream();
  }

  // -- BZip2Handle API methods --

  /**
   * Returns true if the given filename is a BZip2 file.
   *
   * @param file the path to a file on disk
   * @return true if file's extension is .bz2 and the
   *         first 2 bytes are the BZip2 magic marker
   * @throws IOException if the file is not readable
   */
  public static boolean isBZip2File(String file) throws IOException {
    if (!file.toLowerCase().endsWith(".bz2")) {
      return false;
    }

    FileInputStream s = new FileInputStream(file);
    byte[] b = new byte[2];
    s.read(b);
    s.close();
    return new String(b, Constants.ENCODING).equals("BZ");
  }

  // -- StreamHandle API methods --

  /* @see StreamHandle#resetStream() */
  @Override
  protected void resetStream() throws IOException {
    BufferedInputStream bis = new BufferedInputStream(
      new FileInputStream(file), RandomAccessInputStream.MAX_OVERHEAD);
    int skipped = 0;
    while (skipped < 2) {
      skipped += bis.skip(2 - skipped);
    }
    stream = new DataInputStream(new CBZip2InputStream(bis));
  }


}