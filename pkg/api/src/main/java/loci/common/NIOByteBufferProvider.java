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
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a facade to byte buffer allocation that enables
 * <code>FileChannel.map()</code> usage on platforms where it's unlikely to
 * give us problems and heap allocation where it is.
 */
public class NIOByteBufferProvider {

  // -- Constants --

  /** The minimum Java version we know is safe for memory mapped I/O. */
  public static final int MINIMUM_JAVA_VERSION = 6;

  /** Logger for this class. */
  private static final Logger LOGGER =
    LoggerFactory.getLogger(NIOByteBufferProvider.class);

  // -- Fields --

  /** Whether or not we are to use memory mapped I/O. */
  private static boolean useMappedByteBuffer = false;

  /** File channel to allocate or map data from. */
  private FileChannel channel;

  /** If we are to use memory mapped I/O, the map mode. */
  private MapMode mapMode;

  static {
    String mapping = System.getProperty("mappedBuffers");
    useMappedByteBuffer = Boolean.parseBoolean(mapping);
    LOGGER.debug("Using mapped byte buffer? {}", useMappedByteBuffer);
  }

  // -- Constructors --

  /**
   * Default constructor.
   * @param channel File channel to allocate or map byte buffers from.
   * @param mapMode The map mode. Required but only used if memory mapped I/O
   * is to occur.
   */
  public NIOByteBufferProvider(FileChannel channel, MapMode mapMode) {
    this.channel = channel;
    this.mapMode = mapMode;
  }

  // -- NIOByteBufferProvider API Methods --

  /**
   * Allocates or maps the desired file data into memory.
   * @param bufferStartPosition The absolute position of the start of the
   * buffer.
   * @param newSize The buffer size.
   * @return A newly allocated or mapped NIO byte buffer.
   * @throws IOException If there is an issue mapping, aligning or allocating
   * the buffer.
   */
  public ByteBuffer allocate(long bufferStartPosition, int newSize)
    throws IOException {
    if (useMappedByteBuffer) {
      return allocateMappedByteBuffer(bufferStartPosition, newSize);
    }
    return allocateDirect(bufferStartPosition, newSize);
  }

  /**
   * Allocates memory and copies the desired file data into it.
   * @param bufferStartPosition The absolute position of the start of the
   * buffer.
   * @param newSize The buffer size.
   * @return A newly allocated NIO byte buffer.
   * @throws IOException If there is an issue aligning or allocating
   * the buffer.
   */
  protected ByteBuffer allocateDirect(long bufferStartPosition, int newSize)
    throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(newSize);
    channel.read(buffer, bufferStartPosition);
    return buffer;
  }

  /**
   * Memory maps the desired file data into memory.
   * @param bufferStartPosition The absolute position of the start of the
   * buffer.
   * @param newSize The buffer size.
   * @return A newly mapped NIO byte buffer.
   * @throws IOException If there is an issue mapping, aligning or allocating
   * the buffer.
   */
  protected ByteBuffer allocateMappedByteBuffer(
    long bufferStartPosition, int newSize) throws IOException
  {
    return channel.map(mapMode, bufferStartPosition, newSize);
  }
}