package loci.plugins.util;

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

import ij.process.ImageProcessor;

import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.cache.CacheException;
import loci.formats.cache.ICacheSource;

/**
 * Retrieves ImageJ image processors from an image reader.
 */
public class ImageProcessorSource implements ICacheSource {

  // -- Fields --

  /** Image reader from which to draw ImageProcessors. */
  protected ImageProcessorReader reader;

  // -- Constructors --

  public ImageProcessorSource(IFormatReader reader) {
    if (reader instanceof ImageProcessorReader) {
      this.reader = (ImageProcessorReader) reader;
    }
    else {
      this.reader = new ImageProcessorReader(reader);
    }
  }

  // -- ICacheSource API methods --

  /* @see loci.formats.cache.ICacheSource#getObject(int) */
  @Override
  public int getObjectCount() { return reader.getImageCount(); }

  /* @see loci.formats.cache.ICacheSource#getObject(int) */
  @Override
  public Object getObject(int index) throws CacheException {
    // assumes that channels are separated
    ImageProcessor ip = null;
    try {
      ip = reader.openProcessors(index)[0];
    }
    catch (FormatException exc) {
      throw new CacheException(exc);
    }
    catch (IOException exc) {
      throw new CacheException(exc);
    }
    return ip;
  }

}
