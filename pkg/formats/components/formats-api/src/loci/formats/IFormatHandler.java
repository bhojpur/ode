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

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface for all biological file format readers and writers.
 */
public interface IFormatHandler extends Closeable, IMetadataConfigurable {

  /** Checks if the given string is a valid filename for this file format. */
  boolean isThisType(String name);

  /** Gets the name of this file format. */
  String getFormat();

  /** Gets the default file suffixes for this file format. */
  String[] getSuffixes();

  /**
   * Returns the native data type of image planes for this reader, as returned
   * by {@link IFormatReader#openPlane} or {@link IFormatWriter#savePlane}.
   * For most readers this type will be a byte array; however, some readers
   * call external APIs that work with other types such as
   * {@link java.awt.image.BufferedImage}.
   */
  Class<?> getNativeDataType();

  /** Sets the current file name. */
  void setId(String id) throws FormatException, IOException;

}
