package ode.services.roi;

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

import java.io.IOException;

import ode.api.IPixels;
import ode.conditions.ApiUsageException;
import ode.conditions.ResourceError;
import ode.conditions.ValidationException;
import ode.io.nio.DimensionsOutOfBoundsException;
import ode.io.nio.PixelBuffer;
import ode.io.nio.PixelsService;
import ode.model.core.Pixels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Strategy for loading and optionally caching pixel data.
 */
public class PixelData {

    protected Logger log = LoggerFactory.getLogger(PixelData.class);

    protected final PixelsService data;

    protected final IPixels meta;

    public PixelData(PixelsService data, IPixels meta) {
        this.data = data;
        this.meta = meta;
    }

    public PixelBuffer getBuffer(long pix) {
        return data.getPixelBuffer(meta.retrievePixDescription(pix), false);
    }

    public double get(PixelBuffer buf, int x, int y, int z, int c, int t) {
        ode.util.PixelData pd = null;
        try {
            pd = buf.getRow(y, z, c, t);
            return pd.getPixelValue(x);
        } catch (IOException e) {
            throw new ResourceError("IOException: " + e);
        } catch (DimensionsOutOfBoundsException e) {
            throw new ApiUsageException("DimensionsOutOfBounds: " + e);
        } catch (IndexOutOfBoundsException iobe) {
            throw new ValidationException("IndexOutOfBounds: " + iobe);
        } finally {
            if (pd != null) {
                pd.dispose();
            }
        }
    }

    /**
     * Returns the {@link ode.util.PixelData} for plane given its z, c and t
     * as well as a {@link PixelBuffer}
     *
     * @param buf the {@link PixelBuffer}
     * @param z the Z
     * @param c the C
     * @param t the T
     * @return the ode.util.PixelData for the plane
     */
    public ode.util.PixelData getPlane(PixelBuffer buf, int z, int c, int t) {
        try {
            return buf.getPlane(z, c, t);
        } catch (IOException e) {
            throw new ResourceError("IOException: " + e);
        } catch (DimensionsOutOfBoundsException e) {
            throw new ApiUsageException("DimensionsOutOfBounds: " + e);
        } catch (IndexOutOfBoundsException iobe) {
            throw new ValidationException("IndexOutOfBounds: " + iobe);
        }
    }

    /**
     * Returns whether a pyramid should be used for the given {@link Pixels}.
     * This usually implies that this is a "Big image" and therefore will
     * need tiling.
     *
     * @see PixelsService#requiresPixelsPyramid(Pixels)
     * @param pix the pixels
     * @return {@code true} if a pyramid should be used, {@code false}
     *         otherwise
     */
    public boolean requiresPixelsPyramid(Pixels pix) {
        return data.requiresPixelsPyramid(pix);
    }

}