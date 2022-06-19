package ode.logic;

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

import java.awt.image.DataBuffer;

import odeis.providers.re.RGBBuffer;

/**
 * Creates a data buffer with three banks of type byte.
 */
class RGBByteBuffer extends DataBuffer {
    private byte[][] banks;

    RGBByteBuffer(RGBBuffer buf, int sizeX, int sizeY) {
        super(TYPE_BYTE, sizeX * sizeY, 3);
        banks = new byte[3][];
        banks[0] = buf.getRedBand();
        banks[1] = buf.getGreenBand();
        banks[2] = buf.getBlueBand();
    }

    /**
     * Returns the requested data array element from the specified bank as an
     * integer.
     * 
     * @param bank
     *            The specified bank.
     * @param i
     *            The index of the requested data array element.
     * @see java.awt.image.DataBuffer#getElem(int, int)
     */
    @Override
    public int getElem(int bank, int i) {
        return banks[bank][i] & 0xFF;
    }

    /**
     * Sets the requested data array element in the specified bank from the
     * given integer.
     * 
     * @param bank
     *            The specified bank.
     * @param i
     *            The specified index into the data array.
     * @param val
     *            The data to set the element in the specified bank.
     * @see java.awt.image.DataBuffer#setElem(int, int, int)
     */
    @Override
    public void setElem(int bank, int i, int val) {
        banks[bank][i] = (byte) val;
    }

}