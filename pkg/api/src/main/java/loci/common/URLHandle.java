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
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Provides random access to URLs using the IRandomAccess interface.
 * Instances of URLHandle are read-only.
 *
 * @see IRandomAccess
 * @see StreamHandle
 * @see java.net.URLConnection
 */
public class URLHandle extends StreamHandle {

  // -- Fields --

  /** URL of open socket */
  private String url;

  /** Socket underlying this stream */
  private URLConnection conn;

  // -- Constructors --

  /**
   * Constructs a new URLHandle using the given URL.
   *
   * @param url the fully qualified URL path
   * @throws IOException if the URL is invalid or unreadable
   */
  public URLHandle(String url) throws IOException {
    if (!url.startsWith("http") && !url.startsWith("file:")) {
      url = "http://" + url;
    }
    this.url = url;
    resetStream();
  }

  // -- IRandomAccess API methods --

  /* @see IRandomAccess#seek(long) */
  @Override
  public void seek(long pos) throws IOException {
    if (pos < fp && pos >= mark) {
      stream.reset();
      fp = mark;
      skip(pos - fp);
    }
    else super.seek(pos);
  }

  // -- StreamHandle API methods --

  /* @see StreamHandle#resetStream() */
  @Override
  protected void resetStream() throws IOException {
    conn = (new URL(url)).openConnection();
    stream = new DataInputStream(new BufferedInputStream(
      conn.getInputStream(), RandomAccessInputStream.MAX_OVERHEAD));
    fp = 0;
    mark = 0;
    length = conn.getContentLength();
    if (stream != null) stream.mark(RandomAccessInputStream.MAX_OVERHEAD);
  }

  // -- Helper methods --

  /** Skip over the given number of bytes. */
  private void skip(long bytes) throws IOException {
    long skipped = 0;
    while (skipped < bytes) {
      final long n = skipBytes(bytes - skipped);
      if (n == 0) break;
      skipped += n;
    }
  }

}