package ode.codecs.services;

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
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.ServiceLoader;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import loci.common.services.AbstractService;
import loci.common.services.ServiceException;
import ode.codecs.JPEG2000CodecOptions;

import com.sun.media.imageio.plugins.jpeg2000.J2KImageReadParam;
import com.sun.media.imageio.plugins.jpeg2000.J2KImageWriteParam;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReader;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriter;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterSpi;

/**
 * Implementation of JAIIIOService for reading and writing JPEG-2000 data.
 */
public class JAIIIOServiceImpl extends AbstractService
  implements JAIIIOService
{

  // -- Constants --

  public static final String NO_J2K_MSG =
    "The JAI Image I/O Tools are required to read JPEG-2000 files. " +
    "Please obtain jai_imageio.jar from " +
    "http://www.bhojpur.net/site/support/ode-formats/developers/java-library.html";

  // -- Fields --

  private IIORegistry serviceRegistry;

  // -- JAIIIOService API methods --

  /**
   * Default constructor.
   */
  public JAIIIOServiceImpl() {
    // Thorough class checking
    checkClassDependency(J2KImageWriteParam.class);
    checkClassDependency(J2KImageWriter.class);
    checkClassDependency(J2KImageWriterSpi.class);
    checkClassDependency(J2KImageReadParam.class);
    checkClassDependency(J2KImageReader.class);
    checkClassDependency(J2KImageReaderSpi.class);

    serviceRegistry = registerServiceProviders();
  }

  /* @see JAIIIOService#writeImage(OutputStream, BufferedImage, JPEG2000CodecOptions) */
  @Override
  public void writeImage(OutputStream out, BufferedImage img,
      JPEG2000CodecOptions options) throws IOException, ServiceException
  {
    ImageOutputStream ios = ImageIO.createImageOutputStream(out);

    IIORegistry registry = IIORegistry.getDefaultInstance();
    Iterator<J2KImageWriterSpi> iter = 
      ServiceLoader.load(J2KImageWriterSpi.class).iterator();
    registry.registerServiceProviders(iter);
    J2KImageWriterSpi spi =
      registry.getServiceProviderByClass(J2KImageWriterSpi.class);
    J2KImageWriter writer = new J2KImageWriter(spi);
    writer.setOutput(ios);

    String filter = options.lossless ? J2KImageWriteParam.FILTER_53 :
      J2KImageWriteParam.FILTER_97;

    IIOImage iioImage = new IIOImage(img, null, null);
    J2KImageWriteParam param =
      (J2KImageWriteParam) writer.getDefaultWriteParam();
    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionType("JPEG2000");
    if (!options.lossless) {
      param.setEncodingRate(options.quality);
    }
    param.setLossless(options.lossless);
    param.setFilter(filter);
    param.setCodeBlockSize(options.codeBlockSize);
    param.setComponentTransformation(false);
    if (options.tileWidth > 0 && options.tileHeight > 0) {
      param.setTiling(options.tileWidth, options.tileHeight,
                      options.tileGridXOffset, options.tileGridYOffset);
    }
    if (options.numDecompositionLevels != null) {
      param.setNumDecompositionLevels(
          options.numDecompositionLevels.intValue());
    }
    writer.write(null, iioImage, param);
    ios.close();
  }

  /* @see JAIIIOService#readImage(InputStream, JPEG2000CodecOptions) */
  @Override
  public BufferedImage readImage(InputStream in, JPEG2000CodecOptions options)
    throws IOException, ServiceException
  {
    J2KImageReader reader = getReader();
    MemoryCacheImageInputStream mciis = new MemoryCacheImageInputStream(in);
    reader.setInput(mciis, false, true);
    J2KImageReadParam param = (J2KImageReadParam) reader.getDefaultReadParam();
    if (options.resolution != null) {
      param.setResolution(options.resolution.intValue());
    }
    BufferedImage image = reader.read(0, param);
    mciis.close();
    reader.dispose();
    return image;
  }

  /* @see JAIIIOService#readImage(InputStream) */
  @Override
  public BufferedImage readImage(InputStream in)
    throws IOException, ServiceException
  {
    return readImage(in, JPEG2000CodecOptions.getDefaultOptions());
  }

  /* @see JAIIIOService#readRaster(InputStream, JPEG2000CodecOptions) */
  @Override
  public Raster readRaster(InputStream in, JPEG2000CodecOptions options)
    throws IOException, ServiceException
  {
    J2KImageReader reader = getReader();
    MemoryCacheImageInputStream mciis = new MemoryCacheImageInputStream(in);
    reader.setInput(mciis, false, true);
    J2KImageReadParam param = (J2KImageReadParam) reader.getDefaultReadParam();
    if (options.resolution != null) {
      param.setResolution(options.resolution.intValue());
    }
    Raster raster = reader.readRaster(0, param);
    mciis.close();
    reader.dispose();
    return raster;
  }

  /* @see JAIIIOService#readRaster(InputStream) */
  @Override
  public Raster readRaster(InputStream in) throws IOException, ServiceException
  {
    return readRaster(in, JPEG2000CodecOptions.getDefaultOptions());
  }

  /** Set up the JPEG-2000 image reader. */
  private J2KImageReader getReader() {
    J2KImageReaderSpi spi =
      serviceRegistry.getServiceProviderByClass(J2KImageReaderSpi.class);
    return new J2KImageReader(spi);
  }

  /** Register the JPEG-2000 readers with the reader service. */
  private static IIORegistry registerServiceProviders() {
    IIORegistry registry = IIORegistry.getDefaultInstance();
    Iterator<J2KImageReaderSpi> iter =
            ServiceLoader.load(J2KImageReaderSpi.class).iterator();
    registry.registerServiceProviders(iter);
    return registry;
  }

}