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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import loci.common.services.AbstractService;
import loci.common.services.DependencyException;
import loci.common.services.ServiceException;

import com.luratech.lwf.lwfDecoder;

public class LuraWaveServiceImpl extends AbstractService
  implements LuraWaveService {

  /** System property to check for the LuraWave license code. */
  public static final String LICENSE_PROPERTY = "lurawave.license";

  /** Message displayed if the LuraWave LWF decoder library is not found. */
  public static final String NO_LURAWAVE_MSG =
    "The LuraWave decoding library, lwf_jsdk2.6.jar, is required to decode " +
    "this file.\r\nPlease make sure it is present in your classpath.";

  /** Message to display if no LuraWave license code is given. */
  public static final String NO_LICENSE_MSG =
    "No LuraWave license code was specified.\r\nPlease set one in the " +
    LICENSE_PROPERTY + " system property (e.g., with -D" + LICENSE_PROPERTY +
    "=XXXX from the command line).";

  /** Message to display if an invalid LuraWave license code is given. */
  public static final String INVALID_LICENSE_MSG = "Invalid license code: ";

  /** Identifying field in stub class. */
  public static final String STUB_FIELD = "IS_STUB";

  /** LuraWave decoder delegate. */
  private transient Object delegate;

  /** License code. */
  private String license;

  /**
   * Default constructor.
   */
  public LuraWaveServiceImpl() throws DependencyException {
    checkClassDependency(com.luratech.lwf.lwfDecoder.class);
    try {
      Field isStub = com.luratech.lwf.lwfDecoder.class.getField(STUB_FIELD);
      if (isStub != null) {
        throw new DependencyException(NO_LURAWAVE_MSG);
      }
    }
    catch (NoSuchFieldException e) { }
  }

  /* (non-Javadoc)
   * @see ode.codecs.services.LuraWaveService#setLicenseCode(java.lang.String)
   */
  @Override
  public void setLicenseCode(String license) {
    this.license = license;
  }

  /* (non-Javadoc)
   * @see ode.codecs.services.LuraWaveService#getLicenseCode()
   */
  @Override
  public String getLicenseCode() {
    return license;
  }

  /* (non-Javadoc)
   * @see ode.codecs.services.LuraWaveService#initialize(java.io.InputStream)
   */
  @Override
  public void initialize(InputStream stream)
    throws IOException, DependencyException, ServiceException {
    initLicense();
    try {
      delegate = new lwfDecoder(stream, null, license);
    }
    catch (SecurityException e) {
      throw new ServiceException(e);
    }
  }

  /* (non-Javadoc)
   * @see ode.codecs.services.LuraWaveService#getWidth()
   */
  @Override
  public int getWidth() {
    return ((lwfDecoder) delegate).getWidth();
  }

  /* (non-Javadoc)
   * @see ode.codecs.services.LuraWaveService#getHeight()
   */
  @Override
  public int getHeight() {
    return ((lwfDecoder) delegate).getHeight();
  }

  /* (non-Javadoc)
   * @see ode.codecs.services.LuraWaveService#decodeToMemoryGray8(byte[], int, int, int)
   */
  @Override
  public void decodeToMemoryGray8(byte[] image, int limit,
                                  int quality, int scale)
    throws ServiceException {
    try {
      ((lwfDecoder) delegate).decodeToMemoryGray8(image, limit, quality, scale);
    }
    catch (SecurityException e) {
      throw new ServiceException(e);
    }
  }

  /* (non-Javadoc)
   * @see ode.codecs.services.LuraWaveService#decodeToMemoryGray16(short[], int, int, int, int, int, int, int, int, int, int)
   */
  @Override
  public void decodeToMemoryGray16(
      short[] image, int imageoffset, int limit, int quality, int scale,
      int pdx, int pdy, int clip_x, int clip_y, int clip_w, int clip_h)
    throws ServiceException {
    try {
      ((lwfDecoder) delegate).decodeToMemoryGray16(image, imageoffset, limit, quality, scale,
                                    pdx, pdy, clip_x, clip_y, clip_w, clip_h);
    }
    catch (SecurityException e) {
      throw new ServiceException(e);
    }
  }

  private void initLicense() throws DependencyException {
    if (license != null) return; // license already initialized
    license = System.getProperty(LICENSE_PROPERTY);
    if (license == null) throw new DependencyException(NO_LICENSE_MSG);
  }

}