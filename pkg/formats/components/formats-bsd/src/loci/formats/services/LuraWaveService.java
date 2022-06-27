/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.services;

import java.io.IOException;
import java.io.InputStream;

import loci.common.services.DependencyException;
import loci.common.services.Service;
import loci.common.services.ServiceException;

public interface LuraWaveService extends Service {

  /**
   * Overrides the license code to use when initializing the LuraWave decoder.
   * By default the license code is loaded from the "lurawave.license" system
   * property.
   * @param license String license code.
   */
  public void setLicenseCode(String license);

  /**
   * Retrieves the current license code as a string.
   * @return See above.
   */
  public String getLicenseCode();

  /**
   * Wraps {@link com.luratech.lwf.lwfDecoder#lwfDecoder(InputStream, String, String)}.
   * @throws IOException If parsing of the image header fails.
   * @throws DependencyException If no license code was specified.
   * @throws ServiceException If the license code is invalid.
   */
  public void initialize(InputStream stream)
    throws IOException, DependencyException, ServiceException;

  /** Wraps {@link com.luratech.lwf.lwfDecoder#getWidth()} */
  public int getWidth();

  /** Wraps {@link com.luratech.lwf.lwfDecoder#getHeight()} */
  public int getHeight();

  /** 
   * Wraps {@link com.luratech.lwf.lwfDecoder#decodeToMemoryGray8(byte[], int, int, int)}.
   * @throws ServiceException If the license code is invalid.
   */
  public void decodeToMemoryGray8(byte[] image, int limit,
                                  int quality, int scale)
    throws ServiceException;

  /** 
   * Wraps {@link com.luratech.lwf.lwfDecoder#decodeToMemoryGray16(short[], int, int, int, int, int, int, int, int, int, int)}.
   * @throws ServiceException If the license code is invalid.
   */
  public void decodeToMemoryGray16(
      short[] image, int imageoffset, int limit, int quality, int scale,
      int pdx, int pdy, int clip_x, int clip_y, int clip_w, int clip_h)
    throws ServiceException;

}
