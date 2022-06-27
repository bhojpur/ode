/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

/**
 * A growable array of bytes.
 */
public class ByteVector {
  private ode.codecs.ByteVector vector;

  public ByteVector() {
    vector = new ode.codecs.ByteVector();
  }

  public ByteVector(int initialSize) {
    vector = new ode.codecs.ByteVector(initialSize);
  }

  public ByteVector(byte[] byteBuffer) {
    vector = new ode.codecs.ByteVector(byteBuffer);
  }

  public void add(byte x) {
    this.vector.add(x);
  }

  public int size() {
    return this.vector.size();
  }

  public byte get(int index) {
    return this.vector.get(index);
  }

  public void add(byte[] array) {
    this.vector.add(array);
  }

  public void add(byte[] array, int off, int len) {
    this.vector.add(array, off, len);
  }

  void doubleCapacity() {
  }

  public void clear() {
    this.vector.clear();
  }

  public byte[] toByteArray() {
    return this.vector.toByteArray();
  }

  ode.codecs.ByteVector getWrapped() {
    return this.vector;
  }

}
