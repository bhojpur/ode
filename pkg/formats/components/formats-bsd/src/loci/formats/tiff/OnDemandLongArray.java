package loci.formats.tiff;

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
import loci.common.DataTools;
import loci.common.RandomAccessInputStream;

public class OnDemandLongArray {

  private transient RandomAccessInputStream stream;
  private int size;
  private long start;

  public OnDemandLongArray(RandomAccessInputStream in) throws IOException {
    stream = in;
    start = stream.getFilePointer();
  }

  public void setSize(int size) {
    this.size = size;
  }

  public RandomAccessInputStream getStream() {
    return stream;
  }

  public void setStream(RandomAccessInputStream in) {
    stream = in;
  }

  public long get(int index) throws IOException {
    long fp = stream.getFilePointer();
    stream.seek(start + index * 8);
    long value = stream.readLong();
    stream.seek(fp);
    return value;
  }

  public long size() {
    return size;
  }

  public long[] toArray() throws IOException {
    long fp = stream.getFilePointer();
    stream.seek(start);
    byte[] rawBytes = new byte[size * 8];
    stream.readFully(rawBytes);
    stream.seek(fp);
    return (long[]) DataTools.makeDataArray(rawBytes, 8, false, stream.isLittleEndian());
  }

  public void close() throws IOException {
    if (stream != null) {
      stream.close();
    }
    stream = null;
    size = 0;
    start = 0;
  }

}
