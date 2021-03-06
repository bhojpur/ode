package loci.formats.out;

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
import java.awt.image.BufferedImage;
import java.io.IOException;

import loci.common.DataTools;
import loci.common.ReflectException;
import loci.common.ReflectedUniverse;
import loci.formats.FormatException;
import loci.formats.FormatWriter;
import loci.formats.gui.AWTImageTools;
import loci.formats.gui.LegacyQTTools;
import loci.formats.meta.MetadataRetrieve;

/**
 * LegacyQTWriter is a file format writer for QuickTime movies. It uses the
 * QuickTime for Java library, and allows the user to choose between a variety
 * of common video codecs.
 *
 * Much of this code was based on the QuickTime Movie Writer for ImageJ
 * (available at http://rsb.info.nih.gov/ij/plugins/movie-writer.html).
 */
public class LegacyQTWriter extends FormatWriter {

  // -- Constants --

  /** Time scale. */
  private static final int TIME_SCALE = 600;

  // -- Fields --

  /** Instance of LegacyQTTools to handle QuickTime for Java detection. */
  protected LegacyQTTools tools;

  /** Reflection tool for QuickTime for Java calls. */
  protected ReflectedUniverse r;

  /** The codec to use. */
  protected int codec = QTWriter.CODEC_RAW;

  /** The quality to use. */
  protected int quality = QTWriter.QUALITY_NORMAL;

  /** Number of frames written. */
  private int numWritten = 0;

  /** Frame width. */
  private int width;

  /** Frame height. */
  private int height;

  private int[] pixels2 = null;

  // -- Constructor --

  public LegacyQTWriter() {
    super("Legacy QuickTime", "mov");
  }

  // -- LegacyQTWriter API methods --

  /**
   * Sets the encoded movie's codec.
   * @param codec Codec value:<ul>
   *   <li>QTWriter.CODEC_CINEPAK</li>
   *   <li>QTWriter.CODEC_ANIMATION</li>
   *   <li>QTWriter.CODEC_H_263</li>
   *   <li>QTWriter.CODEC_SORENSON</li>
   *   <li>QTWriter.CODEC_SORENSON_3</li>
   *   <li>QTWriter.CODEC_MPEG_4</li>
   *   <li>QTWriter.CODEC_RAW</li>
   * </ul>
   */
  public void setCodec(int codec) { this.codec = codec; }

  /**
   * Sets the quality of the encoded movie.
   * @param quality Quality value:<ul>
   *   <li>QTWriter.QUALITY_LOW</li>
   *   <li>QTWriter.QUALITY_MEDIUM</li>
   *   <li>QTWriter.QUALITY_HIGH</li>
   *   <li>QTWriter.QUALITY_MAXIMUM</li>
   * </ul>
   */
  public void setQuality(int quality) { this.quality = quality; }

  // -- IFormatWriter API methods --

  /**
   * @see loci.formats.IFormatWriter#saveBytes(int, byte[], int, int, int, int)
   */
  @Override
  public void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    checkParams(no, buf, x, y, w, h);
    MetadataRetrieve meta = getMetadataRetrieve();
    BufferedImage image = AWTImageTools.makeImage(buf,
      interleaved, meta, series);
    savePlane(no, image, x, y, w, h);
  }

  /**
   * @see loci.formats.IFormatWriter#savePlane(int, Object, int, int, int, int)
   */
  @Override
  public void savePlane(int no, Object plane, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    if (!(plane instanceof Image)) {
      throw new IllegalArgumentException(
        "Object to save must be a java.awt.Image");
    }

    if (tools == null || r == null) {
      tools = new LegacyQTTools();
      r = tools.getUniverse();
    }
    tools.checkQTLibrary();

    BufferedImage img = AWTImageTools.makeBuffered((Image) plane);

    if (!initialized[series][no]) {
      initialized[series][no] = true;

      try {
        r.exec("QTSession.open()");
        width = img.getWidth();
        height = img.getHeight();
        r.setVar("path", currentId);
        r.setVar("width", (float) width);
        r.setVar("height", (float) height);

        r.exec("movFile = new QTFile(path)");
        r.exec("kMoviePlayer = StdQTConstants.kMoviePlayer");
        int resFlag = ((Integer)
          r.exec("StdQTConstants.createMovieFileDontCreateResFile")).intValue();
        r.setVar("flags", resFlag);
        r.exec("movie = Movie.createMovieFile(movFile, kMoviePlayer, flags)");
        r.setVar("timeScale", TIME_SCALE);
        r.setVar("zero", 0);
        r.setVar("zeroFloat", (float) 0);
        r.exec("videoTrack = movie.addTrack(width, height, zeroFloat)");
        r.exec("videoMedia = new VideoMedia(videoTrack, timeScale)");
        r.exec("videoMedia.beginEdits()");

        r.setVar("width", width);
        r.setVar("height", height);
        r.exec("bounds = new QDRect(zero, zero, width, height)");
        r.exec("gw = new QDGraphics(bounds)");

        r.exec("pixMap = gw.getPixMap()");
        r.exec("pixSize = pixMap.getPixelSize()");
        r.setVar("codec", codec);
        r.setVar("quality", quality);

        int rawImageSize = width * height * 4;
        r.setVar("rawImageSize", rawImageSize);

        r.setVar("boolTrue", true);
        r.exec("imageHandle = new QTHandle(rawImageSize, boolTrue)");
        r.exec("imageHandle.lock()");
        r.exec("compressedImage = RawEncodedImage.fromQTHandle(imageHandle)");

        r.setVar("rate", 30);

        r.exec("seq = new CSequence(gw, bounds, pixSize, codec, " +
          "CodecComponent.bestFidelityCodec, quality, quality, rate, null, " +
          "zero)");

        r.exec("imgDesc = seq.getDescription()");
      }
      catch (ReflectException e) {
        LOGGER.debug("", e);
        throw new FormatException("Legacy QuickTime writer failed", e);
      }
    }

    numWritten++;

    try {
      r.exec("pixelData = pixMap.getPixelData()");

      r.exec("intsPerRow = pixelData.getRowBytes()");
      int intsPerRow = ((Integer) r.getVar("intsPerRow")).intValue() / 4;

      byte[][] px = AWTImageTools.getBytes(img);

      int[] pixels = new int[px[0].length];
      for (int i=0; i<pixels.length; i++) {
        byte[] b = new byte[4];
        for (int j=0; j<px.length; j++) {
          b[j] = px[j][i];
        }
        for (int j=px.length; j<4; j++) {
          b[j] = px[j % px.length][i];
        }
        pixels[i] = DataTools.bytesToInt(b, true);
      }

      if (pixels2 == null) pixels2 = new int[intsPerRow * height];
      r.exec("nativeLittle = EndianOrder.isNativeLittleEndian()");
      boolean nativeLittle =
        ((Boolean) r.getVar("nativeLittle")).booleanValue();
      if (nativeLittle) {
        int offset1, offset2;
        for (int row=0; row<height; row++) {
          offset1 = row * width;
          offset2 = row * intsPerRow;
          for (int col=0; col<width; col++) {
            r.setVar("thisByte", pixels[offset1++]);
            r.exec("b = EndianOrder.flipBigEndianToNative32(thisByte)");
            pixels2[offset2++] = ((Integer) r.getVar("b")).intValue();
          }
        }
      }
      else {
        for (int i=0; i<height; i++) {
          System.arraycopy(pixels, i*width, pixels2, i*intsPerRow, width);
        }
      }

      r.setVar("pixels2", pixels2);
      r.setVar("len", intsPerRow * height);

      r.exec("pixelData.copyFromArray(zero, pixels2, zero, len)");
      r.exec("flags = StdQTConstants.codecFlagUpdatePrevious");
      r.exec("cfInfo = seq.compressFrame(gw, bounds, flags, compressedImage)");

      // see developer.apple.com/qa/qtmcc/qtmcc20.html
      r.exec("similarity = cfInfo.getSimilarity()");
      int sim = ((Integer) r.getVar("similarity")).intValue();
      boolean sync = sim == 0;
      r.exec("dataSize = cfInfo.getDataSize()");
      r.setVar("fps", fps);
      r.setVar("frameRate", TIME_SCALE);
      r.setVar("rate", TIME_SCALE / fps);

      if (sync) {
        r.setVar("sync", 0);
      }
      else r.exec("sync = StdQTConstants.mediaSampleNotSync");
      r.setVar("one", 1);
      r.exec("videoMedia.addSample(imageHandle, zero, dataSize, " +
        "rate, imgDesc, one, sync)");
    }
    catch (ReflectException e) {
      LOGGER.debug("", e);
      throw new FormatException("Legacy QuickTime writer failed", e);
    }

    if (no == getPlaneCount() - 1) {
      try {
        r.exec("videoMedia.endEdits()");
        r.exec("duration = videoMedia.getDuration()");
        r.setVar("floatOne", (float) 1.0);
        r.exec("videoTrack.insertMedia(zero, zero, duration, floatOne)");
        r.exec("omf = OpenMovieFile.asWrite(movFile)");
        r.exec("name = movFile.getName()");
        r.exec("flags = StdQTConstants.movieInDataForkResID");
        r.exec("movie.addResource(omf, flags, name)");
        r.exec("QTSession.close()");
      }
      catch (ReflectException e) {
        LOGGER.debug("", e);
        throw new FormatException("Legacy QuickTime writer failed", e);
      }
      close();
    }
  }

  /* @see loci.formats.IFormatWriter#canDoStacks() */
  @Override
  public boolean canDoStacks() { return true; }

  // -- IFormatHandler API methods --

  /* @see loci.formats.IFormatHandler#getNativeDataType() */
  @Override
  public Class<?> getNativeDataType() {
    return Image.class;
  }

  /* @see loci.formats.IFormatHandler#close() */
  @Override
  public void close() throws IOException {
    super.close();
    r = null;
    numWritten = 0;
    width = 0;
    height = 0;
    pixels2 = null;
  }

}
