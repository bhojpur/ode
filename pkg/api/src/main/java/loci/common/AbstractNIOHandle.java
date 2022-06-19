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

import java.io.IOException;

/**
 * A wrapper for buffered NIO logic that implements the IRandomAccess interface.
 *
 * @see IRandomAccess
 * @see java.io.RandomAccessFile
 */
public abstract class AbstractNIOHandle implements IRandomAccess {

  /** Error message to be used when instantiating an EOFException. */
  protected static final String EOF_ERROR_MSG =
    "Attempting to read beyond end of file.";

  //-- Constants --

  // -- Fields --

  // -- Constructors --

  // -- AbstractNIOHandle methods --

  /* @see IRandomAccess#exists() */
  @Override
  public boolean exists() throws IOException {
    return length() >= 0;
  }

  /**
   * Ensures that the file mode is either "r" or "rw".
   * @param mode Mode to validate.
   * @throws IllegalArgumentException If an illegal mode is passed.
   */
  protected void validateMode(String mode) {
    if (!(mode.equals("r") || mode.equals("rw"))) {
      throw new IllegalArgumentException(
        String.format("%s mode not in supported modes ('r', 'rw')", mode));
    }
  }

  /**
   * Ensures that the handle has the correct length to be written to and
   * extends it as required.
   * @param writeLength Number of bytes to write.
   * @return <code>true</code> if the buffer has not required an extension.
   * <code>false</code> otherwise.
   * @throws IOException If there is an error changing the handle's length.
   */
  protected boolean validateLength(int writeLength) throws IOException {
    if (getFilePointer() + writeLength > length()) {
      setLength(getFilePointer() + writeLength);
      return false;
    }
    return true;
  }

  /**
   * Sets the new length of the handle.
   * @param length New length.
   * @throws IOException If there is an error changing the handle's length.
   */
  protected abstract void setLength(long length) throws IOException;

}