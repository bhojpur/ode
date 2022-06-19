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
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * StreamHandle implementation for reading from Zip-compressed files
 * or byte arrays.  Instances of ZipHandle are read-only.
 *
 * @see StreamHandle
 */
public class ZipHandle extends StreamHandle {

  // -- Fields --

  private RandomAccessInputStream in;
  private ZipInputStream zip;
  private String entryName;
  private int entryCount;

  // -- Constructor --

  public ZipHandle(String file) throws IOException {
    super();
    this.file = file;

    in = openStream(file);
    zip = new ZipInputStream(in);
    entryName = null;
    entryCount = 0;

    // strip off .zip extension and directory prefix
    String innerFile = file.substring(0, file.length() - 4);
    int slash = innerFile.lastIndexOf(File.separator);
    if (slash < 0) slash = innerFile.lastIndexOf("/");
    if (slash >= 0) innerFile = innerFile.substring(slash + 1);

    // look for Zip entry with same prefix as the Zip file itself
    boolean matchFound = false;
    length = 0;
    while (true) {
      ZipEntry ze = zip.getNextEntry();
      if (ze == null) break;
      if (entryName == null) entryName = ze.getName();
      if (!matchFound && ze.getName().startsWith(innerFile)) {
        // found entry with matching name
        entryName = ze.getName();
        matchFound = true;
      }
      entryCount++;
      length += ze.getSize();
    }
    resetStream();

    if (length <= 0) {
      populateLength();
    }
  }

  /**
   * Constructs a new ZipHandle corresponding to the given entry of the
   * specified Zip file.
   *
   * @param file a name that can be passed to
   *        {@link Location#getHandle(String, boolean, boolean)}
   * @param entry the specific entry in the Zip file to be opened
   * @throws HandleException if the given file is not a Zip file.
   * @see ZipEntry
   */
  public ZipHandle(String file, ZipEntry entry) throws IOException {
    super();
    this.file = file;

    in = openStream(file);
    zip = new ZipInputStream(in);
    entryName = entry.getName();
    entryCount = 1;

    seekToEntry();
    resetStream();
    length = entry.getSize();
    if (length <= 0) {
      populateLength();
    }
  }

  // -- ZipHandle API methods --

  /**
   * @param file a name that can be passed to
   *        {@link Location#getHandle(String, boolean, boolean)}
   * @return true if the given filename is a Zip file.
   * @throws IOException if the file cannot be read
   */
  public static boolean isZipFile(String file) throws IOException {
    if (!file.toLowerCase().endsWith(".zip")) return false;

    IRandomAccess handle = getHandle(file);
    byte[] b = new byte[2];
    if (handle.length() >= 2) {
      handle.read(b);
    }
    handle.close();
    return new String(b, Constants.ENCODING).equals("PK");
  }

  /**
   * @return the name of the backing Zip entry.
   */
  public String getEntryName() {
    return entryName;
  }

  /**
   * @return the DataInputStream corresponding to the backing Zip entry.
   */
  public DataInputStream getInputStream() {
    return stream;
  }

  /**
   * @return the number of entries.
   */
  public int getEntryCount() {
    return entryCount;
  }

  // -- IRandomAccess API methods --

  /* @see IRandomAccess#close() */
  @Override
  public void close() throws IOException {
    super.close();
    zip = null;
    entryName = null;
    if (in != null) in.close();
    in = null;
    entryCount = 0;
  }

  // -- StreamHandle API methods --

  /* @see StreamHandle#resetStream() */
  @Override
  protected void resetStream() throws IOException {
    if (stream != null) stream.close();
    if (in != null) {
      in.close();
      in = openStream(file);
    }
    if (zip != null) zip.close();
    zip = new ZipInputStream(in);
    if (entryName != null) seekToEntry();
    stream = new DataInputStream(new BufferedInputStream(
      zip, RandomAccessInputStream.MAX_OVERHEAD));
    stream.mark(RandomAccessInputStream.MAX_OVERHEAD);
  }

  // -- Helper methods --

  private void seekToEntry() throws IOException {
    while (!entryName.equals(zip.getNextEntry().getName()));
  }

  private void populateLength() throws IOException {
    length = -1;
    while (stream.available() > 0) {
      stream.skip(1);
      length++;
    }
    resetStream();
  }

  private static IRandomAccess getHandle(String file) throws IOException {
    return Location.getHandle(file, false, false);
  }

  private static RandomAccessInputStream openStream(String file)
    throws IOException
  {
    return new RandomAccessInputStream(getHandle(file), file);
  }

}