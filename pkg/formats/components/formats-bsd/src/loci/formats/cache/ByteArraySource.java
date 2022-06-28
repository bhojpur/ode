package loci.formats.cache;

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

import loci.formats.FormatException;
import loci.formats.IFormatReader;

/**
 * Retrieves byte arrays from a data source
 * (e.g., a file) using Bhojpur ODE-Formats.
 */
public class ByteArraySource extends CacheSource {

  // -- Constructors --

  /** Constructs a byte array source from the given Bhojpur ODE-Formats reader. */
  public ByteArraySource(IFormatReader r) { super(r); }

  /** Constructs a byte array source that draws from the given file. */
  public ByteArraySource(String id) throws CacheException { super(id); }

  // -- ICacheSource API methods --

  /* @see loci.formats.cache.ICacheSource#getObject(int) */
  @Override
  public Object getObject(int index) throws CacheException {
    try { return reader.openBytes(index); }
    catch (FormatException exc) { throw new CacheException(exc); }
    catch (IOException exc) { throw new CacheException(exc); }
  }

}
