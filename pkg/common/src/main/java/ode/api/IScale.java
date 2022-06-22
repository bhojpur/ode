package ode.api;

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

import java.awt.image.BufferedImage;

/**
 * Provides methods for performing scaling (change of the image size through
 * interpolation or other means) on BufferedImages.
 */
public interface IScale extends ServiceInterface {

    /**
     * Scales a buffered image using defined X and Y axis scale factors. For
     * example:
     * <p>
     * If you wanted to take a 512x512 image and scale it to 256x256 you would
     * use an X and Y scale factor of 0.5.
     * </p>
     * NOTE: The X and Y scale factors <b>do not</b> have to be equal.
     * 
     * @param image
     *            the buffered image to scale.
     * @param xScale
     *            X-axis scale factor.
     * @param yScale
     *            Y-axis scale factor.
     * @return a scaled buffered image.
     */
    public BufferedImage scaleBufferedImage(BufferedImage image, float xScale,
            float yScale);
}