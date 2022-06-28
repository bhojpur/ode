package loci.formats.utests.tiff;

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
import java.nio.ByteBuffer;

import loci.common.ByteArrayHandle;
import loci.common.RandomAccessInputStream;
import loci.common.RandomAccessOutputStream;
import loci.formats.out.TiffWriter;
import loci.formats.tiff.TiffSaver;

public class TiffWriterMock extends TiffWriter {
  
  private static final int INITIAL_CAPACITY = 1024 * 1024;
  private ByteArrayHandle handle;
  
  public TiffWriterMock() {
    super();
    handle = new ByteArrayHandle(INITIAL_CAPACITY);
  }
  
  public TiffWriterMock(String format, String[] exts) {
    super(format, exts);
    handle = new ByteArrayHandle(INITIAL_CAPACITY);
  }
  
  @Override
  protected RandomAccessOutputStream createOutputStream() throws IOException {
    return new RandomAccessOutputStream(handle);
  }
  
  @Override
  protected RandomAccessInputStream createInputStream() throws IOException {
    return new RandomAccessInputStream(handle);
  }
  
  @Override
  protected TiffSaver createTiffSaver() {
    return new TiffSaver(out, handle);
  }
  
  public void createOutputBuffer(boolean mock) throws IOException {
    if (mock) {
      handle = new ByteArrayHandleMock(INITIAL_CAPACITY);
    }
    else {
      handle = new ByteArrayHandle(INITIAL_CAPACITY);
    }
  }
  
  public void setBufferLength(long length) throws IOException {
    handle.setLength(length);
  }

}
