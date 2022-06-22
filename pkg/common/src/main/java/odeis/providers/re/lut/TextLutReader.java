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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

/**
 * Read the text Lut.
 */
class TextLutReader
    extends LutReader
{

    /** Number of rows and columns composing the file.*/
    private int rows, columns;

    private String firstTok;

    /**
     * Counts the lines.
     * @param r The file reader.
     * @throws IOException
     */
    private void countLines(Reader r) throws IOException {
        StreamTokenizer tok = new StreamTokenizer(r);
        int wordsPerRow = 0, wordsInPreviousRow = 0;
        tok.resetSyntax();
        tok.wordChars(43, 43);
        tok.wordChars(45, 127);
        tok.whitespaceChars(0, 42);
        tok.whitespaceChars(44, 44);
        tok.whitespaceChars(128, 255);
        tok.eolIsSignificant(true);

        while (tok.nextToken() != StreamTokenizer.TT_EOF) {
            switch (tok.ttype) {
                case StreamTokenizer.TT_EOL:
                    rows++;
                    if (wordsPerRow == 0)
                        rows--;  // ignore empty rows
                    if (rows == 1 && wordsPerRow > 0)
                        columns = wordsPerRow;
                    if (rows > 1 && wordsPerRow != 0 &&
                            wordsPerRow != wordsInPreviousRow)
                        throw new IOException("Row "+rows+ " is not "
                                + "the same length as the first row.");
                    if (wordsPerRow != 0)
                        wordsInPreviousRow = wordsPerRow;
                    wordsPerRow = 0;
                    break;
                case StreamTokenizer.TT_WORD:
                    wordsPerRow++;
            }
        }
        if (wordsPerRow == columns) 
            rows++; // last row does not end with EOL
    }

    /**
     * Parses the value.
     *
     * @param value The value to pass.
     * @param defaultValue The default value.
     * @return See above
     */
    private double parseDouble(String value, double defaultValue)
    {
        if (value == null) return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {}
        return defaultValue;
    }

    /**
     * Reader the file and populates the specified array.
     *
     * @param r The reader.
     * @param pixels The array storing the values.
     * @throws IOException
     */
    private void read(Reader r, float[] pixels) throws IOException {
        int size = pixels.length;
        StreamTokenizer tok = new StreamTokenizer(r);
        tok.resetSyntax();
        tok.wordChars(43, 43);
        tok.wordChars(45, 127);
        tok.whitespaceChars(0, 42);
        tok.whitespaceChars(44, 44);
        tok.whitespaceChars(128, 255);
        int i = 0;
        int inc = size/20;
        if (inc < 1) {
            inc = 1;
        }
        while (tok.nextToken() != StreamTokenizer.TT_EOF) {
            if (tok.ttype == StreamTokenizer.TT_WORD) {
                if (i == 0) {
                    firstTok = tok.sval;
                }
                pixels[i++] = (float) parseDouble(tok.sval, Double.NaN);
                if (i == size)
                    break;
            }
        }
    }

    /** Creates a new instance.*/
    TextLutReader(File f)
    {
        super(f);
        rows = 0;
        columns = 1;
    }

    /**
     * Crops. Returns the extracted values as an array.
     *
     * @param x The x-value of the area to crop.
     * @param y The y-value of the area to crop.
     * @param w The width of the area to crop.
     * @param h The height of the area to crop.
     * @param pixels The array to crop.
     * @return See above.
     */
    private float[] crop(int x, int y, int w, int h, float[] pixels) {
        float[] pixels2 = new float[w*h];
        for (int ys = y; ys < y+h; ys++) {
            int offset1 = (ys-y)*w;
            int offset2 = ys*columns+x;
            for (int xs = 0; xs < w; xs++)
                pixels2[offset1++] = pixels[offset2++];
        }
        return pixels2;
    }

    /**
     * Reads the file. The <code>raw</code> flag is not used.
     */
    @Override
    int read() throws Exception {
        String path = file.getAbsolutePath();
        Reader r = new BufferedReader(new FileReader(path));
        countLines(r);
        r.close();
        r = new BufferedReader(new FileReader(path));
        if (columns*rows == 0)
            return 0;
        int height = rows;
        float[] pixels = new float[columns*height];
        read(r, pixels);
        r.close();
        int firstRowNaNCount = 0;
        for (int i = 0; i < columns; i++) {
            if (i < pixels.length && Float.isNaN(pixels[i]))
                firstRowNaNCount++;
        }

        float[] values = pixels;
        if (firstRowNaNCount == columns && !("NaN".equals(firstTok)||
                "nan".equals(firstTok))) { // assume first row is header
            height = rows-1;
            values = crop(0, 1, columns, height, pixels);
        }

        if (columns < 3 || columns > 4 || height < SIZE|| height > SIZE+2)
            return 0;
        int x = columns == 4 ? 1 : 0;
        int y = height > SIZE ? 1: 0;
        //crop again
        float[] result = crop(x, y, 3, SIZE, values);
        columns = 3;
        for (int i = 0; i< SIZE; i++) {
            reds[i] = (byte) result[i*columns];
            greens[i] = (byte) result[i*columns+1];
            blues[i] = (byte) result[i*columns+2];
        }
        return SIZE;
    }
}