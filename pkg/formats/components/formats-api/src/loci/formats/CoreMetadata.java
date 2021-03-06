package loci.formats;

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

import java.util.Hashtable;

/**
 * Encompasses core metadata values.
 */
public class CoreMetadata implements Cloneable {

  // -- Fields --

  // TODO: We may want to consider refactoring the FormatReader getter
  // methods that populate missing CoreMetadata fields on the fly
  // (getThumbSizeX, getThumbSizeY) to avoid doing so -- one alternate
  // approach would be to have this class use getter methods instead
  // of public fields.

  /** Width (in pixels) of images in this series. */
  public int sizeX;

  /** Height (in pixels) of images in this series. */
  public int sizeY;

  /** Number of Z sections. */
  public int sizeZ;

  /** Number of channels. */
  public int sizeC;

  /** Number of timepoints. */
  public int sizeT;

  /** Width (in pixels) of thumbnail images in this series. */
  public int thumbSizeX;

  /** Height (in pixels) of thumbnail images in this series. */
  public int thumbSizeY;

  /**
   * Describes the number of bytes per pixel.  Must be one of the <i>static</i>
   * pixel types (e.g. <code>INT8</code>) in {@link loci.formats.FormatTools}.
   */
  public int pixelType;

  /** Number of valid bits per pixel. */
  public int bitsPerPixel;

  /** Total number of images. */
  public int imageCount;

  public Modulo moduloZ = new Modulo("Z");
  public Modulo moduloC = new Modulo("C");
  public Modulo moduloT = new Modulo("T");

  /**
   * Order in which dimensions are stored.  Must be one of the following:<ul>
   *  <li>XYCZT</li>
   *  <li>XYCTZ</li>
   *  <li>XYZCT</li>
   *  <li>XYZTC</li>
   *  <li>XYTCZ</li>
   *  <li>XYTZC</li>
   * </ul>
   */
  public String dimensionOrder;

  /**
   * Indicates whether or not we are confident that the
   * dimension order is correct.
   */
  public boolean orderCertain;

  /**
   * Indicates whether or not the images are stored as RGB
   * (multiple channels per plane).
   */
  public boolean rgb;

  /** Indicates whether or not each pixel's bytes are in little endian order. */
  public boolean littleEndian;

  /**
   * True if channels are stored RGBRGBRGB...; false if channels are stored
   * RRR...GGG...BBB...
   */
  public boolean interleaved;

  /** Indicates whether or not the images are stored as indexed color. */
  public boolean indexed;

  /** Indicates whether or not we can ignore the color map (if present). */
  public boolean falseColor = true;

  /**
   * Indicates whether or not we are confident that all of the metadata stored
   * within the file has been parsed.
   */
  public boolean metadataComplete;

  /** Non-core metadata associated with this series. */
  public Hashtable<String, Object> seriesMetadata;

  /**
   * Indicates whether or not this series is a lower-resolution copy of
   * another series.
   */
  public boolean thumbnail;

  public int resolutionCount = 1;

  // -- Constructors --

  public CoreMetadata() {
    seriesMetadata = new Hashtable<String, Object>();
  }

  public CoreMetadata(IFormatReader r, int coreIndex) {
    int currentIndex = r.getCoreIndex();
    r.setCoreIndex(coreIndex);

    sizeX = r.getSizeX();
    sizeY = r.getSizeY();
    sizeZ = r.getSizeZ();
    sizeC = r.getSizeC();
    sizeT = r.getSizeT();
    thumbSizeX = r.getThumbSizeX();
    thumbSizeY = r.getThumbSizeY();
    pixelType = r.getPixelType();
    bitsPerPixel = r.getBitsPerPixel();
    imageCount = r.getImageCount();
    dimensionOrder = r.getDimensionOrder();
    orderCertain = r.isOrderCertain();
    rgb = r.isRGB();
    littleEndian = r.isLittleEndian();
    interleaved = r.isInterleaved();
    indexed = r.isIndexed();
    falseColor = r.isFalseColor();
    metadataComplete = r.isMetadataComplete();
    seriesMetadata = new Hashtable<String, Object>();
    seriesMetadata.putAll(r.getSeriesMetadata());
    thumbnail = r.isThumbnailSeries();
    resolutionCount = r.getResolutionCount();
    moduloZ = r.getModuloZ();
    moduloC = r.getModuloC();
    moduloT = r.getModuloT();

    r.setCoreIndex(currentIndex);
  }

  public CoreMetadata(CoreMetadata c) {
    sizeX = c.sizeX;
    sizeY = c.sizeY;
    sizeZ = c.sizeZ;
    sizeC = c.sizeC;
    sizeT = c.sizeT;
    thumbSizeX = c.thumbSizeX;
    thumbSizeY = c.thumbSizeY;
    pixelType = c.pixelType;
    bitsPerPixel = c.bitsPerPixel;
    imageCount = c.imageCount;
    dimensionOrder = c.dimensionOrder;
    orderCertain = c.orderCertain;
    rgb = c.rgb;
    littleEndian = c.littleEndian;
    interleaved = c.interleaved;
    indexed = c.indexed;
    falseColor = c.falseColor;
    metadataComplete = c.metadataComplete;
    seriesMetadata = new Hashtable<String, Object>();
    seriesMetadata.putAll(c.seriesMetadata);
    thumbnail = c.thumbnail;
    resolutionCount = c.resolutionCount;
    moduloZ = new Modulo(c.moduloZ);
    moduloC = new Modulo(c.moduloC);
    moduloT = new Modulo(c.moduloT);
  }

  // -- Object methods --

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString() + ":");
    sb.append("\n\tsizeX = " + sizeX);
    sb.append("\n\tsizeY = " + sizeY);
    sb.append("\n\tsizeZ = " + sizeZ);
    sb.append("\n\tsizeC = " + sizeC);
    sb.append("\n\tsizeT = " + sizeT);
    sb.append("\n\tthumbSizeX = " + thumbSizeX);
    sb.append("\n\tthumbSizeY = " + thumbSizeY);
    sb.append("\n\tpixelType = " + FormatTools.getPixelTypeString(pixelType));
    sb.append("\n\tbitsPerPixel = " + bitsPerPixel);
    sb.append("\n\timageCount = " + imageCount);
    sb.append("\n\tdimensionOrder = " + dimensionOrder);
    sb.append("\n\torderCertain = " + orderCertain);
    sb.append("\n\trgb = " + rgb);
    sb.append("\n\tlittleEndian = " + littleEndian);
    sb.append("\n\tinterleaved = " + interleaved);
    sb.append("\n\tindexed = " + indexed);
    sb.append("\n\tfalseColor = " + falseColor);
    sb.append("\n\tmetadataComplete = " + metadataComplete);
    sb.append("\n\tseriesMetadata = " + seriesMetadata.size() + " keys");
    sb.append("\n\tthumbnail = " + thumbnail);
    return sb.toString();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public CoreMetadata clone(IFormatReader r, int coreIndex) {
      return new CoreMetadata(r, coreIndex);
  }

}
