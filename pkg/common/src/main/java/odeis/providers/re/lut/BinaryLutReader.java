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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Binary lookup table reader. After code from ImageJ source.
 */
class BinaryLutReader
    extends LutReader
{

    private void interpolate(byte[] reds, byte[] greens, byte[] blues, int nColors) {
        byte[] r = new byte[nColors]; 
        byte[] g = new byte[nColors]; 
        byte[] b = new byte[nColors];
        System.arraycopy(reds, 0, r, 0, nColors);
        System.arraycopy(greens, 0, g, 0, nColors);
        System.arraycopy(blues, 0, b, 0, nColors);
        double scale = nColors/new Double(SIZE);
        int i1, i2;
        double fraction;
        int v = SIZE-1;
        for (int i = 0; i< SIZE; i++) {
            i1 = (int)(i*scale);
            i2 = i1+1;
            if (i2 == nColors) {
                i2 = nColors-1;
            }
            fraction = i*scale - i1;
            reds[i] = (byte)((1.0-fraction)*(r[i1]&v) + fraction*(r[i2]&v));
            greens[i] = (byte)((1.0-fraction)*(g[i1]&v) + fraction*(g[i2]&v));
            blues[i] = (byte)((1.0-fraction)*(b[i1]&v) + fraction*(b[i2]&v));
        }
    }

    /**
     * Creates a new instance.
     *
     * @param file The file to read.
     */
    BinaryLutReader(File file)
    {
        super(file);
    }

    /**
     * Creates a new instance.
     *
     * @param file The file to read.
     * @param raw Set the flag indicating to read or not NIH lut.
     */
    BinaryLutReader(File file, boolean raw)
    {
        super(file);
        this.raw = raw;
    }

    /**
     * Reads the binary lut.
     */
    @Override
    int read() throws Exception {
        InputStream is = new FileInputStream(file.getAbsolutePath());
        DataInputStream f = new DataInputStream(is);
        int nColors = SIZE;
        if (!raw) {
            // attempt to read 32 byte NIH Image LUT header
            int id = f.readInt();
            if (id != 1229147980) { // 'ICOL'
                f.close();
                return 0;
            }
            f.readShort(); //version
            nColors = f.readShort();
            f.readShort(); //start
            f.readShort(); //end
            f.readLong(); //fill 1
            f.readLong(); //fill 2
            f.readInt(); // filler
        }
        f.read(reds, 0, nColors);
        f.read(greens, 0, nColors);
        f.read(blues, 0, nColors);
        if (nColors < SIZE) {
            interpolate(reds, greens, blues, nColors);
        }
        f.close();
        return SIZE;
    }

}