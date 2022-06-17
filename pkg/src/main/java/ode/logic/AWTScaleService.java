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

// Java imports
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Third-party libraries

// Application-internal dependencies
import ode.api.IScale;

/**
 * Provides methods for scaling buffered images.
 */
public class AWTScaleService implements IScale {
    /** The logger for this class. */
    private static Logger log = LoggerFactory.getLogger(AWTScaleService.class);

    /*
     * (non-Javadoc)
     * 
     * @see ode.api.IScale#scaleBufferedImage(java.awt.image.BufferedImage,
     *      float, float)
     */
    public BufferedImage scaleBufferedImage(BufferedImage image, float xScale,
            float yScale) {
        int thumbHeight = (int) (image.getHeight() * yScale);
        int thumbWidth = (int) (image.getWidth() * xScale);
        log.info("Scaling to: " + thumbHeight + "x" + thumbWidth);

        // Create the required compatible (thumbnail) buffered image to avoid
        // potential errors from Java's ImagingLib.
        ColorModel cm = image.getColorModel();
        WritableRaster r = cm.createCompatibleWritableRaster(thumbWidth,
                thumbHeight);
        BufferedImage thumbImage = new BufferedImage(cm, r, false, null);

        // Do the actual scaling and return the result
        Graphics2D graphics2D = thumbImage.createGraphics();
        // graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        // RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
        graphics2D.dispose();
        return thumbImage;
    }
}