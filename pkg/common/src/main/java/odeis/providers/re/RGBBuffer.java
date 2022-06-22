package odeis.providers.re;

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

import java.io.Serializable;
import java.util.Arrays;

/**
 * Holds the data of an <i>RGB</i> image. The image data is stored in three
 * byte arrays, one for each color band.
 */
public class RGBBuffer implements Serializable {

    /** The serial number. */
    private static final long serialVersionUID = 5319594152389817323L;

    /** Index of the red band in the image's data buffer. */
    public static final int R_BAND = 0;

    /** Index of the green band in the image's data buffer. */
    public static final int G_BAND = 1;

    /** Index of the blue band in the image's data buffer. */
    public static final int B_BAND = 2;

    /**
     * Holds the image's data. The byte array of index {@link #R_BAND} holds the
     * data for the red band, index {@link #G_BAND} holds the data for the green
     * band, and index {@link #B_BAND} holds the data for the blue band. The
     * size of each array is the one specified to the constructor.
     */
    private byte[][] bands;

    /**
     * Number of pixels on the <i>X1</i>-axis. This is the <i>X</i>-axis in
     * the case of an <i>XY</i> or <i>XZ</i> plane. Otherwise it is the <i>Z</i>-axis
     * &#151; <i>ZY</i> plane.
     */
    private int sizeX1;

    /**
     * Number of pixels on the X2-axis. This is the <i>Y</i>-axis in the case
     * of an <i>XY</i> or <i>ZY</i> plane. Otherwise it is the <i>Z</i>-axis
     * &#151; <i>XZ</i> plane.
     */
    private int sizeX2;

    /**
     * Simple constructor to avoid memory allocations.
     * 
     */
    protected RGBBuffer() {
    }

    /**
     * Creates a new 3-band buffer.
     * 
     * @param sizeX1
     *            The number of pixels on the <i>X1</i>-axis. This is the <i>X</i>-axis
     *            in the case of an <i>XY</i>-plane or <i>XZ</i>-plane.
     *            Otherwise it is the <i>Z</i>-axis &#151; <i>ZY</i>-plane.
     * @param sizeX2
     *            The number of pixels on the <i>X2</i>-axis. This is the <i>Y</i>-axis
     *            in the case of an <i>XY</i>-plane or <i>ZY</i>-plane.
     *            Otherwise it is the <i>Z</i>-axis &#151; <i>XZ</i>-plane.
     * @see #bands
     */
    public RGBBuffer(int sizeX1, int sizeX2) {
        this.sizeX1 = sizeX1;
        this.sizeX2 = sizeX2;
        bands = new byte[3][];
        for (int i = 0; i < 3; ++i) {
            bands[i] = new byte[sizeX1 * sizeX2];
        }
    }

    /**
     * Returns the data buffer for the red band.
     * 
     * @return See above.
     */
    public byte[] getRedBand() {
        return bands[R_BAND];
    }

    /**
     * Returns the data buffer for the green band.
     * 
     * @return See above.
     */
    public byte[] getGreenBand() {
        return bands[G_BAND];
    }

    /**
     * Returns the data buffer for the blue band.
     * 
     * @return See above.
     */
    public byte[] getBlueBand() {
        return bands[B_BAND];
    }

    /**
     * Returns the number of pixels on the <i>X1</i>-axis. This is the <i>X</i>-axis
     * in the case of an <i>XY</i>-plane or <i>XZ</i>-plane. Otherwise it is
     * the <i>Z</i>-axis &#151; <i>ZY</i>-plane.
     * 
     * @return The number of pixels on the <i>X1</i>-axis.
     */
    public int getSizeX1() {
        return sizeX1;
    }

    /**
     * Returns the number of pixels on the <i>X2</i>-axis. This is the <i>Y</i>-axis
     * in the case of an <i>XY</i>-plane or <i>ZY</i>-plane. Otherwise it is
     * the <i>Z</i>-axis &#151; <i>XZ</i>-plane.
     * 
     * @return The number of pixels on the <i>X2</i>-axis.
     */
    public int getSizeX2() {
        return sizeX2;
    }

    /**
     * Sets the Red value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @param value
     *            The pixel value to set.
     */
    public synchronized void setRedValue(int index, int value) {
        bands[R_BAND][index] = (byte) value;
    }

    /**
     * Sets the Green value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @param value
     *            The pixel value to set.
     */
    public synchronized void setGreenValue(int index, int value) {
        bands[G_BAND][index] = (byte) value;
    }

    /**
     * Sets the Blue value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @param value
     *            The pixel value to set.
     */
    public synchronized void setBlueValue(int index, int value) {
        bands[B_BAND][index] = (byte) value;
    }

    /**
     * Retrieves the Red value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @return The pixel value at the index.
     */
    public byte getRedValue(int index) {
        return bands[R_BAND][index];
    }

    /**
     * Retrieves the Green value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @return The pixel value at the index.
     */
    public byte getGreenValue(int index) {
        return bands[G_BAND][index];
    }

    /**
     * Retrieves the Blue value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @return The pixel value at the index.
     */
    public byte getBlueValue(int index) {
        return bands[B_BAND][index];
    }
    
    /**
     * Zeros out (sets every pixel offset to zero) each band.
     */
    public void zero()
    {
    	for (int i = 0; i < bands.length; i++)
    		Arrays.fill(bands[i], (byte) 0);
    }
}