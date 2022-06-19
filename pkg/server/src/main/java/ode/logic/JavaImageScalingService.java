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
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

//Third-party libraries
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import com.mortennobel.imagescaling.ResampleOp;

// Application-internal dependencies
import ode.api.IScale;

/**
 * Provides methods for scaling buffered images using
 * <a href="http://code.google.com/p/java-image-scaling/">Java Image Scaling</a>.
 * 
 */
public class JavaImageScalingService implements IScale {
    /** The logger for this class. */
    private static Logger log = LoggerFactory.getLogger(JavaImageScalingService.class);

    /*
     * (non-Javadoc)
     * 
     * @see ode.api.IScale#scaleBufferedImage(java.awt.image.BufferedImage,
     * float, float)
     */
    public BufferedImage scaleBufferedImage(BufferedImage image, float xScale,
            float yScale) {
        int thumbHeight = (int) (image.getHeight() * yScale);
        int thumbWidth = (int) (image.getWidth() * xScale);
        if (thumbHeight < 3)
            thumbHeight = 3;
        if (thumbWidth < 3)
            thumbWidth = 3;
        
        log.info("Scaling to: " + thumbHeight + "x" + thumbWidth);
        
        StopWatch s1 = new Slf4JStopWatch("java-image-scaling.resampleOp");
        BufferedImage toReturn;
        if (image.getHeight() >= 3 && image.getWidth() >= 3) {
            ResampleOp resampleOp = new ResampleOp(thumbWidth, thumbHeight);
            toReturn = resampleOp.filter(image, null);
        } else {
            toReturn = new BufferedImage(thumbWidth, thumbHeight,
                    image.getType());
            Graphics2D g = toReturn.createGraphics();
            g.getRenderingHints().add(
                    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_OFF));
            g.drawImage(image, 0, 0, thumbWidth, thumbHeight, 0, 0,
                    image.getWidth(), image.getHeight(), null);
            g.dispose();
        }
        s1.stop();
        return toReturn;
    }
}