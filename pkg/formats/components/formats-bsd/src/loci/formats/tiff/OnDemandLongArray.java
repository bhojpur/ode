/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.tiff;

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
