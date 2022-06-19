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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import ode.api.local.LocalCompress;

public class CompressImpl implements LocalCompress {

	/** The default compression quality in fractional percent. */
    private float quality = 0.85F;
	
    /* (non-Javadoc)
     * @see ode.api.ICompress#compressToStream(java.awt.image.BufferedImage, java.io.OutputStream)
     */
    public void compressToStream(BufferedImage image, OutputStream outputStream)
    	throws IOException
    {
        // Get a JPEG image writer
        ImageWriter jpegWriter =
        	ImageIO.getImageWritersByFormatName("jpeg").next();

        // Setup the compression value from (0.05, 0.75 and 0.95)
        ImageWriteParam iwp = jpegWriter.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(quality);

        // Write the JPEG to our ByteArray stream
    	ImageOutputStream imageOutputStream = null;
        try {
        	imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        	jpegWriter.setOutput(imageOutputStream);
        	jpegWriter.write(null, new IIOImage(image, null, null), iwp);
        } finally {
        	if (imageOutputStream != null)
        		imageOutputStream.close();
        }
    }

	/* (non-Javadoc)
	 * @see ode.api.ICompress#setCompressionLevel(float)
	 */
	public void setCompressionLevel(float percentage)
	{
		quality = percentage;
	}
	
	/* (non-Javadoc)
	 * @see ode.api.ICompress#getCompressionLevel()
	 */
	public float getCompressionLevel()
	{
		return quality;
	}
}