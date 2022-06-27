package ode.util;

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

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

/**
 * Provides helper methods for performing various things on image data.
 */
public class ImageUtil
{
    /**
     * Creates a buffered image from a rendering engine RGB buffer without data
     * copying.
     * 
     * @param buf
     *            the rendering engine packed integer buffer.
     * @param sizeX
     *            the X-width of the image rendered.
     * @param sizeY
     *            the Y-width of the image rendered.
     * @return a buffered image wrapping <i>buf</i> with the X-Y dimensions
     *         provided.
     */
    public static BufferedImage createBufferedImage(int[] buf, int sizeX, int sizeY)
    {
        // First wrap the packed integer array with a Java2D buffer
        DataBufferInt j2DBuf = new DataBufferInt(buf, sizeX * sizeY, 0);

        // Create a sample model which supplies the bit masks for each colour
        // component.
        SinglePixelPackedSampleModel sampleModel = new SinglePixelPackedSampleModel(
                DataBuffer.TYPE_INT, sizeX, sizeY, sizeX, new int[] {
                        0x00ff0000, // Red
                        0x0000ff00, // Green
                        0x000000ff, // Blue
                // 0xff000000 // Alpha
                });

        // Now create a compatible raster which wraps the Java2D buffer and is
        // told how to get to the pixel data by the sample model.
        WritableRaster raster = Raster.createWritableRaster(sampleModel,
                j2DBuf, new Point(0, 0));

        // Finally create a screen accessible colour model and wrap the raster
        // in a buffered image.
        ColorModel colorModel = new DirectColorModel(24, 0x00ff0000, // Red
                0x0000ff00, // Green
                0x000000ff // Blue
        // 0xff000000 // Alpha
        );
        BufferedImage image = new BufferedImage(colorModel, raster, false, null);

        return image;
    }
}