package ode.codecs;

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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.util.Hashtable;

import loci.common.RandomAccessInputStream;
import loci.common.Region;
import ode.codecs.CodecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class JPEGTileDecoder implements AutoCloseable {

  // -- Constants --

  protected static final Logger LOGGER =
    LoggerFactory.getLogger(JPEGTileDecoder.class);

  // -- Fields --

  private TileConsumer consumer;
  private TileCache tiles;
  private RandomAccessInputStream in;

  // -- JPEGTileDecoder API methods --

  public void initialize(String id, int imageWidth) {
    try {
      initialize(new RandomAccessInputStream(id), imageWidth);
    }
    catch (IOException e) {
      LOGGER.debug("", e);
    }
  }

  public void initialize(RandomAccessInputStream in, int imageWidth) {
    initialize(in, 0, 0, imageWidth);
  }

  public void initialize(RandomAccessInputStream in, int y, int h,
    int imageWidth)
  {
    this.in = in;
    tiles = new TileCache(y, h);

    preprocess(this.in);

    try {
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      byte[] data = new byte[this.in.available()];
      this.in.readFully(data);
      Image image = toolkit.createImage(data);
      ImageProducer producer = image.getSource();

      consumer = new TileConsumer(producer, y, h);
      producer.startProduction(consumer);
      while (producer.isConsumer(consumer));
    }
    catch (IOException e) { }
  }

  /**
    * Pre-process the stream to make sure that the
    * image width and height are non-zero.  Returns an array containing
    * the image width and height.
    */
  public int[] preprocess(RandomAccessInputStream in) {
    int[] dims = new int[2];
    try {
      long fp = in.getFilePointer();
      boolean littleEndian = in.isLittleEndian();
      in.order(false);

      while (in.getFilePointer() < in.length() - 1) {
        int code = in.readShort() & 0xffff;
        int length = in.readShort() & 0xffff;
        long pointer = in.getFilePointer();
        if (length > 0xff00 || code < 0xff00) {
          in.seek(pointer - 3);
          continue;
        }
        if (code == 0xffc0) {
          in.skipBytes(1);
          int height = in.readShort() & 0xffff;
          int width = in.readShort() & 0xffff;

          if (height == 0 || width == 0) {
            throw new RuntimeException(
              "Width or height > 65500 is not supported.");
          }

          dims[0] = width;
          dims[1] = height;

          break;
        }
        else if (pointer + length - 2 < in.length()) {
          in.seek(pointer + length - 2);
        }
        else {
          break;
        }
      }

      in.seek(fp);
      in.order(littleEndian);
    }
    catch (IOException e) { }
    return dims;
  }

  public byte[] getScanline(int y) {
    try {
      return tiles.get(0, y, consumer.getWidth(), 1);
    }
    catch (CodecException e) {
      LOGGER.debug("", e);
    }
    catch (IOException e) {
      LOGGER.debug("", e);
    }
    return null;
  }

  public int getWidth() {
    return consumer.getWidth();
  }

  public int getHeight() {
    return consumer.getHeight();
  }

  public void close() {
    try {
      if (in != null) {
        in.close();
      }
    }
    catch (IOException e) {
      LOGGER.debug("", e);
    }
    tiles = null;
    consumer = null;
  }

  // -- Helper classes --

  class TileConsumer implements ImageConsumer {
    private int width, height;
    private ImageProducer producer;
    private int yy = 0, hh = 0;

    public TileConsumer(ImageProducer producer) {
      this.producer = producer;
    }

    public TileConsumer(ImageProducer producer, int y, int h) {
      this(producer);
      this.yy = y;
      this.hh = h;
    }

    // -- TileConsumer API methods --

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }

    // -- ImageConsumer API methods --

    @Override
    public void imageComplete(int status) {
      producer.removeConsumer(this);
    }

    @Override
    public void setDimensions(int width, int height) {
      this.width = width;
      this.height = height;
      if (hh <= 0) hh = height;
    }

    @Override
    public void setPixels(int x, int y, int w, int h, ColorModel model,
      byte[] pixels, int off, int scanSize)
    {
      LOGGER.debug("Storing row {} of {} ({}%)", new Object[] {y, height,
        ((double) y / height) * 100.0});
      if (y >= (yy + hh)) {
        imageComplete(0);
        return;
      }
      else if (y < yy) return;
      try {
        tiles.add(pixels, x, y, w, h);
      }
      catch (CodecException e) {
        LOGGER.debug("", e);
      }
      catch (IOException e) {
        LOGGER.debug("", e);
      }
    }

    @Override
    public void setPixels(int x, int y, int w, int h, ColorModel model,
      int[] pixels, int off, int scanSize)
    {
      LOGGER.debug("Storing row {} of {} ({}%)", new Object[] {y, (yy + hh),
        ((double) y / (yy + hh)) * 100.0});
      if (y >= (yy + hh)) {
        imageComplete(0);
        return;
      }
      else if (y < yy) return;
      try {
        tiles.add(pixels, x, y, w, h);
      }
      catch (CodecException e) {
        LOGGER.debug("", e);
      }
      catch (IOException e) {
        LOGGER.debug("", e);
      }
    }

    @Override
    public void setProperties(Hashtable props) { }
    @Override
    public void setColorModel(ColorModel model) { }
    @Override
    public void setHints(int hintFlags) { }
  }

  class TileCache {
    private static final int ROW_COUNT = 128;

    private Hashtable<Region, byte[]> compressedTiles =
      new Hashtable<Region, byte[]>();
    private JPEGCodec codec = new JPEGCodec();
    private CodecOptions options = new CodecOptions();

    private ByteVector toCompress = new ByteVector();
    private int row = 0;

    private Region lastRegion = null;
    private byte[] lastTile = null;

    private int yy = 0, hh = 0;

    public TileCache(int yy, int hh) {
      options.interleaved = true;
      options.littleEndian = false;
      this.yy = yy;
      this.hh = hh;
    }

    public void add(byte[] pixels, int x, int y, int w, int h)
      throws CodecException, IOException
    {
      toCompress.add(pixels);
      row++;

      if ((y % ROW_COUNT) == ROW_COUNT - 1 || y == getHeight() - 1 ||
        y == yy + hh - 1)
      {
        Region r = new Region(x, y - row + 1, w, row);
        options.width = w;
        options.height = row;
        options.channels = 1;
        options.bitsPerSample = 8;
        options.signed = false;

        byte[] compressed = codec.compress(toCompress.toByteArray(), options);
        compressedTiles.put(r, compressed);
        toCompress.clear();
      }
    }

    public void add(int[] pixels, int x, int y, int w, int h)
      throws CodecException, IOException
    {
      byte[] buf = new byte[pixels.length * 3];
      for (int i=0; i<pixels.length; i++) {
        buf[i * 3] = (byte) ((pixels[i] & 0xff0000) >> 16);
        buf[i * 3 + 1] = (byte) ((pixels[i] & 0xff00) >> 8);
        buf[i * 3 + 2] = (byte) (pixels[i] & 0xff);
      }

      toCompress.add(buf);
      row++;

      if ((y % ROW_COUNT) == ROW_COUNT - 1 || y == getHeight() - 1 ||
        y == yy + hh - 1)
      {
        Region r = new Region(x, y - row + 1, w, row);
        options.width = w;
        options.height = row;
        options.channels = 3;
        options.bitsPerSample = 8;
        options.signed = false;

        byte[] compressed = codec.compress(toCompress.toByteArray(), options);
        compressedTiles.put(r, compressed);
        toCompress.clear();
        row = 0;
      }
    }

    public byte[] get(int x, int y, int w, int h)
      throws CodecException, IOException
    {
      Region[] keys = compressedTiles.keySet().toArray(new Region[0]);
      Region r = new Region(x, y, w, h);
      for (Region key : keys) {
        if (key.intersects(r)) {
          r = key;
        }
      }
      if (!r.equals(lastRegion)) {
        lastRegion = r;
        byte[] compressed = null;
        compressed = compressedTiles.get(r);
        if (compressed == null) return null;
        lastTile = codec.decompress(compressed, options);
      }

      int pixel = options.channels * (options.bitsPerSample / 8);
      byte[] buf = new byte[w * h * pixel];

      for (int i=0; i<h; i++) {
        System.arraycopy(lastTile, r.width * pixel * (i + y - r.y) + (x - r.x),
          buf, i * w * pixel, pixel * w);
      }

      return buf;
    }
  }

}