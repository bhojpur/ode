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

import java.util.Arrays;

/**
 * Holds the data of an <i>RGBA</i> image. The image data is stored as a 
 * packed integer in the format RGBA. Note that Java color objects are 
 * stored in ARGB.
 */
public class RGBAIntBuffer extends RGBBuffer {

    /** The serial number. */
    private static final long serialVersionUID = 5319594152383817324L;

    /** The data buffer storing pixel values. */
    private int[] dataBuf;

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
     * Creates a new 4-band packed integer buffer.
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
    public RGBAIntBuffer(int sizeX1, int sizeX2) {
        this.sizeX1 = sizeX1;
        this.sizeX2 = sizeX2;
        dataBuf = new int[sizeX1 * sizeX2];
    }

    /**
     * Sets the Red value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @param value
     *            The pixel value to set.
     */
    @Override
    public void setRedValue(int index, int value) {
        dataBuf[index] = dataBuf[index] | value << 24;
    }

    /**
     * Sets the Green value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @param value
     *            The pixel value to set.
     */
    @Override
    public void setGreenValue(int index, int value) {
        dataBuf[index] = dataBuf[index] | value << 16;
    }

    /**
     * Sets the Blue value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @param value
     *            The pixel value to set.
     */
    @Override
    public void setBlueValue(int index, int value) {
        dataBuf[index] = dataBuf[index] | value << 8;
    }

    /**
     * Retrieves the Red value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @return The pixel value at the index.
     */
    @Override
    public byte getRedValue(int index) {
        return (byte) ((dataBuf[index] & 0xFF000000) >> 24);
    }

    /**
     * Retrieves the Green value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @return The pixel value at the index.
     */
    @Override
    public byte getGreenValue(int index) {
        return (byte) ((dataBuf[index] & 0x00FF0000) >> 16);
    }

    /**
     * Retrieves the Blue value for a particular pixel index.
     * 
     * @param index
     *            The index in the band array.
     * @return The pixel value at the index.
     */
    @Override
    public byte getBlueValue(int index) {
        return (byte) ((dataBuf[index] & 0x0000FF00)>>8);
    }

    /**
     * Retrieves the data buffer that contains the pixel values.
     * 
     * @return an integer array containing pixel values.
     */
    public int[] getDataBuffer() {
        return dataBuf;
    }
    
    @Override
    public void zero() {
    	Arrays.fill(dataBuf, 0);
    }
}