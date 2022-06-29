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

import loci.common.services.Service;
import loci.common.services.ServiceException;
import ode.codecs.JPEG2000CodecOptions;

/**
 * Interface defining methods for reading data using JAI Image I/O.
 */
public interface JAIIIOService extends Service {

  /**
   * Writes an image using JAI Image I/O using the JPEG 2000 codec.
   * @param out Target output stream.
   * @param img Source buffered image.
   * @param options Options for the JPEG 2000 codec.
   * @throws IOException Thrown if there is an error reading from or writing
   * to one of the target streams / buffers.
   * @throws ServiceException Thrown if there is an error initializing or
   * interacting with the dependencies of the service.
   */
  public void writeImage(OutputStream out, BufferedImage img,
      JPEG2000CodecOptions options) throws IOException, ServiceException;

  /**
   * Reads an image using JAI Image I/O using the JPEG 2000 codec.
   * @param in Target input stream.
   * @param options Options for the JPEG 2000 codec.
   * @return An AWT buffered image.
   * @throws IOException Thrown if there is an error reading from or writing
   * to one of the target streams / buffers.
   * @throws ServiceException Thrown if there is an error initializing or
   * interacting with the dependencies of the service.
   */
  public BufferedImage readImage(InputStream in, JPEG2000CodecOptions options)
    throws IOException, ServiceException;

  /**
   * Reads an image using JAI Image I/O using the JPEG 2000 codec.
   * @param in Target input stream.
   * @return An AWT buffered image.
   * @throws IOException Thrown if there is an error reading from or writing
   * to one of the target streams / buffers.
   * @throws ServiceException Thrown if there is an error initializing or
   * interacting with the dependencies of the service.
   * @see #readImage(InputStream, JPEG2000CodecOptions)
   */
  public BufferedImage readImage(InputStream in)
    throws IOException, ServiceException;

  /**
   * Reads an image into a raster using JAI Image I/O using the JPEG 2000 codec.
   * @param in Target input stream.
   * @param options Options for the JPEG 2000 codec.
   * @return An AWT image raster.
   * @throws IOException Thrown if there is an error reading from or writing
   * to one of the target streams / buffers.
   * @throws ServiceException Thrown if there is an error initializing or
   * interacting with the dependencies of the service.
   */
  public Raster readRaster(InputStream in, JPEG2000CodecOptions options)
    throws IOException, ServiceException;

  /**
   * Reads an image into a raster using JAI Image I/O using the JPEG 2000 codec.
   * @param in Target input stream.
   * @return An AWT image raster.
   * @throws IOException Thrown if there is an error reading from or writing
   * to one of the target streams / buffers.
   * @throws ServiceException Thrown if there is an error initializing or
   * interacting with the dependencies of the service.
   * @see #readRaster(InputStream, JPEG2000CodecOptions)
   */
  public Raster readRaster(InputStream in) throws IOException, ServiceException;

}