package ode.api.local;

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
import java.io.IOException;
import java.io.OutputStream;

/**
 * Provides methods for performing scaling (change of the image size through
 * interpolation or other means) on BufferedImages.
 */
public interface LocalCompress {

    /**
     * Compresses a buffered image to an output stream.
     * 
     * @param image
     *            the thumbnail's buffered image.
     * @param outputStream
     *            the stream to write to.
     * @throws IOException
     *             if there is a problem when writing to <i>stream</i>.
     */
	void compressToStream(BufferedImage image, OutputStream outputStream)
		throws IOException;

	/**
	 * Sets the current compression level for the service. (The default is 85%)
	 * 
	 * @param percentage A percentage compression level from 1.00 (100%) to 
	 * 0.01 (1%).
	 * @throws ode.conditions.ValidationException if the {@code percentage} is out of
	 * range. (actually doesn't but might should)
	 */
	void setCompressionLevel(float percentage);
	
	/**
	 * Returns the current compression level for the service.
	 * @return the current compression level
	 */
	float getCompressionLevel();
}