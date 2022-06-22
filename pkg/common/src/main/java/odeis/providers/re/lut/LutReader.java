package odeis.providers.re.lut;

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

import java.io.File;

/**
 * Class to be extended by LUT reader.
 */
public abstract class LutReader {

    /** The size of the color interval.*/
    static final int SIZE = 256;

    /** Holds the red values.*/
    protected byte[] reds = new byte[SIZE];

    /** Holds the green values.*/
    protected byte[] greens = new byte[SIZE];

    /** Holds the blues values.*/
    protected byte[] blues = new byte[SIZE];

    /** The file to read.*/
    protected File file;

    /** Flag indicating to read 32 byte NIH Image LUT header.*/
    protected boolean raw;

    /**
     * Creates a new instance.
     *
     * @param file The file to read.
     */
    LutReader(File file)
    {
        this.file = file;
    }

    /**
     * Reads the lookup table.
     * @return See above.
     * @throws Exception Throw if the file cannot be read.
     */
    abstract int read()
            throws Exception;

    /**
     * Returns the red value.
     *
     * @param value The value to handle.
     * @return See above
     */
    public byte getRed(int value)
    {
        return reds[value];
    }

    /**
     * Returns the green value.
     *
     * @param value The value to handle.
     * @return See above
     */
    public byte getGreen(int value)
    {
        return greens[value];
    }

    /**
     * Returns the blue value.
     *
     * @param value The value to handle.
     * @return See above
     */
    public byte getBlue(int value)
    {
        return blues[value];
    }

}