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
 * Initializes the reader corresponding to the specified lookup table.
 */
public class LutReaderFactory {

    /**
     * Reads the specified file.
     *
     * @param filePath The path to the directory.
     * @param fileName The name of the file.
     */
    public static LutReader read(String filePath, String fileName)
        throws Exception
    {
        return LutReaderFactory.read(new File(filePath, fileName));
    }

    /**
     * Reads the specified file.
     *
     * @param file The file to read.
     */
    public static LutReader read(File file)
        throws Exception
    {
        LutReader reader = null;
        long length = file.length();
        int size = 0;
        if (length > 768) { // attempt to read NIH Image LUT
            reader = new BinaryLutReader(file);
            size = reader.read();
        }
        //read raw lut
        if (size == 0 && (length == 0 || length == 768 || length == 970)) {
            reader = new BinaryLutReader(file, true);
            size = reader.read();
        }
        if (size == 0 && length > 768) {
            reader = new TextLutReader(file);
            size = reader.read();
        }
        return reader;
    }

}