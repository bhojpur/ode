package loci.formats.gui;

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

import java.awt.image.BufferedImage;
import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.cache.CacheException;
import loci.formats.cache.ICacheSource;

/**
 * Retrieves BufferedImages from a data source using Bhojpur ODE-Formats.
 */
public class BufferedImageSource implements ICacheSource {

  // -- Fields --

  /** Image reader from which to draw BufferedImages. */
  protected BufferedImageReader reader;

  // -- Constructors --

  public BufferedImageSource(IFormatReader reader) throws CacheException {
    if (reader instanceof BufferedImageReader) {
      this.reader = (BufferedImageReader) reader;
    }
    else {
      this.reader = new BufferedImageReader(reader);
    }
  }

  // -- ICacheSource API methods --

  /* @see loci.formats.cache.ICacheSource#getObject(int) */
  @Override
  public int getObjectCount() { return reader.getImageCount(); }

  /* @see loci.formats.cache.ICacheSource#getObject(int) */
  @Override
  public Object getObject(int index) throws CacheException {
    BufferedImage bi = null;
    try {
      bi = reader.openImage(index);
    }
    catch (FormatException exc) {
      throw new CacheException(exc);
    }
    catch (IOException exc) {
      throw new CacheException(exc);
    }
    return bi;
  }

}
