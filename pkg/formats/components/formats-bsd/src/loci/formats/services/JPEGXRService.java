/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.services;

import loci.common.services.Service;
import loci.formats.FormatException;

/**
 * Interface defining methods for working with JPEG-XR data
 */
public interface JPEGXRService extends Service {

  /**
   * Decompress the given JPEG-XR compressed byte array and return as a byte array.
   * Opening and closing of decoders and streams is handled internally.
   *
   * @param compressed the complete JPEG-XR compressed data
   * @return raw decompressed bytes
   * @throws FormatException if an error occurs during decompression
   */
  byte[] decompress(byte[] compressed) throws FormatException;

}
