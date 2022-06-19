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
import java.util.zip.GZIPInputStream;

/**
 * StreamHandle implementation for reading from gzip-compressed files
 * or byte arrays.  Instances of GZipHandle are read-only.
 *
 * @see StreamHandle
 */
public class GZipHandle extends StreamHandle {

  // -- Constructor --

  /**
   * Construct a new GZipHandle for the given file.
   *
   * @param file the path to the GZip file
   * @throws HandleException if the given file name is not a GZip file.
   */
  public GZipHandle(String file) throws IOException {
    super();
    this.file = file;
    if (!isGZipFile(file)) {
      throw new HandleException(file + " is not a gzip file.");
    }

    resetStream();

    length = 0;
    while (true) {
      int skip = stream.skipBytes(1024);
      if (skip <= 0) break;
      length += skip;
    }

    resetStream();
  }

  // -- GZipHandle API methods --

  /**
   * @param file the path to the GZip file
   * @return true if the given filename is a gzip file
   * @throws IOException if the file cannot be read
   */
  public static boolean isGZipFile(String file) throws IOException {
    if (!file.toLowerCase().endsWith(".gz")) return false;

    FileInputStream s = new FileInputStream(file);
    byte[] b = new byte[2];
    s.read(b);
    s.close();
    return DataTools.bytesToInt(b, true) == GZIPInputStream.GZIP_MAGIC;
  }

  // -- StreamHandle API methods --

  /* @see StreamHandle#resetStream() */
  @Override
  protected void resetStream() throws IOException {
    if (stream != null) stream.close();
    BufferedInputStream bis = new BufferedInputStream(
      new FileInputStream(file), RandomAccessInputStream.MAX_OVERHEAD);
    stream = new DataInputStream(new GZIPInputStream(bis));
  }

}