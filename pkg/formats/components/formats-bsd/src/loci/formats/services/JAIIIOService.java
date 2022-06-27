/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.services;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import loci.common.services.Service;
import loci.common.services.ServiceException;
import loci.formats.codec.JPEG2000CodecOptions;

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
