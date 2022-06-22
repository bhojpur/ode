package integration;

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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * Base test for unit tests.
 *
 */
public class AbstractTest
{

    /** Path the schema language. */
    public static final String JAXP_SCHEMA_LANGUAGE =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /** W3C. */
    public static final String W3C_XML_SCHEMA =
        "http://www.w3.org/2001/XMLSchema";

    /** The source. */
    public static final String JAXP_SCHEMA_SOURCE =
        "http://java.sun.com/xml/jaxp/properties/schemaSource";

    /** The ODE-XML format. */
    public static final String ODE_FORMAT = "ode";

    /** The ODE-XML format. */
    public static final String ODE_XML_FORMAT = "ode.xml";

    /** Holds the error, info, warning. */
    protected Logger log = LoggerFactory.getLogger(getClass());

    protected String rootpass;

    /**
     * Calculates a SHA-1 digest.
     * @param data Data to calcluate a SHA-1 digest for.
     * @param offset Offset within the byte buffer to calculate from.
     * @param len Number of bytes from <code>offset</code> to calculate with.
     * @return Hex string of the SHA-1 digest.
     */
    protected String sha1(byte[] data, int offset, int len)
    {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(
                    "Required SHA-1 message digest algorithm unavailable.");
        }
        md.update(data, offset, len);
        return byteArrayToHexString(md.digest());
    }

    /**
     * Calculates a SHA-1 digest.
     * @param data Data to calcluate a SHA-1 digest for.
     * @return Hex string of the SHA-1 digest.
     */
    protected String sha1(byte[] data)
    {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(
                    "Required SHA-1 message digest algorithm unavailable.");
        }
        md.update(data);
        return byteArrayToHexString(md.digest());
    }

    /**
     * Converts a byte array to a hex string.
     * @param in Byte array to convert.
     * @return Hex string representing the byte array.
     */
    public static String byteArrayToHexString(byte in[]) {

        byte ch = 0x00;
        int i = 0;

        if (in == null || in.length <= 0) {
            return null;
        }

        String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "a", "b", "c", "d", "e", "f" };

        StringBuffer out = new StringBuffer(in.length * 2);

        while (i < in.length) {
            ch = (byte) (in[i] & 0xF0);
            ch = (byte) (ch >>> 4);
            ch = (byte) (ch & 0x0F);
            out.append(pseudo[ch]);
            ch = (byte) (in[i] & 0x0F);
            out.append(pseudo[ch]);
            i++;
        }

        String rslt = new String(out);
        return rslt;
    }

    /**
     * Initializes the various services.
     * @throws Exception Thrown if an error occurred.
     */
    @BeforeClass
    protected void setUp()
        throws Exception
    {
    }

    /**
     * Closes the session.
     * @throws Exception Thrown if an error occurred.
     */
    @AfterClass
    public void tearDown()
        throws Exception
    {
    }

    /**
     */
    protected void clean() throws Exception {
    }

    /**
     * Transforms the input file using the specified stylesheet file.
     *
     * @param input  The file to transform.
     * @param output The destination file.
     * @param xslt   The stylesheet file to use.
     * @throws Exception Thrown if an error occurred while encoding the image.
     */
    protected void transformFile(File input, File output, File xslt)
        throws Exception
    {
        if (input == null)
            throw new IllegalArgumentException("No file to transform.");
        if (output == null)
            throw new IllegalArgumentException("No destination file.");
        if (xslt == null)
            throw new IllegalArgumentException("No stylesheet provided.");
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(
                new StreamSource(xslt));
        StreamResult result = new StreamResult(new FileOutputStream(output));
        transformer.transform(new StreamSource(input), result);
    }

    /**
     * Transforms the input file using the specified stylesheet stream.
     *
     * @param input  The file to transform.
     * @param output The destination file.
     * @param xslt   The stylesheet InputStream to use.
     * @throws Exception Thrown if an error occurred.
     */
    protected void transformFileWithStream(File input, File output, InputStream xslt)
        throws Exception
    {
        if (input == null)
            throw new IllegalArgumentException("No file to transform.");
        if (output == null)
            throw new IllegalArgumentException("No destination file.");
        if (xslt == null)
            throw new IllegalArgumentException("No stylesheet provided.");

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(
                new StreamSource(xslt));
        StreamResult result = new StreamResult(new FileOutputStream(output));
        transformer.transform(new StreamSource(input), result);
    }

}
