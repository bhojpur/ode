package ode.util.checksum;

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
import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.EnumMap;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Abstract base class for unit test classes extending
 * {@link AbstractChecksumProvider}. This class and {@link ChecksumTestVector}
 * should be updated with relevant test vectors and tests whenever a new
 * checksum algorithm implementation is added. This class should be ignored by
 * TestNG.
 */
public abstract class AbstractChecksumProviderAlgorithmTest {

    ChecksumProvider checksumProvider;

    private File smallFile, mediumFile, bigFile;

    private EnumMap<ChecksumTestVector, String> checksumValues;

    public AbstractChecksumProviderAlgorithmTest(ChecksumProvider cp,
            EnumMap<ChecksumTestVector, String> checksumValues) {
        try {
            String name = "smallFile.fake";
            this.smallFile = new File(System.getProperty("java.io.tmpdir"), name);
            this.smallFile.deleteOnExit();
            try (final Writer out = new FileWriter(this.smallFile)) {
                for (int index = 0; index < 10; index ++) {
                    out.append("fake" + index);
                }
            }
            name = "mediumFile.fake";
            this.mediumFile = new File(System.getProperty("java.io.tmpdir"), name);
            this.mediumFile.deleteOnExit();
            try (final Writer out = new FileWriter(this.mediumFile)) {
                for (int index = 0; index < 100; index ++) {
                    out.append("fake" + index);
                }
            }
            name = "bigFile.fake";
            this.bigFile = new File(System.getProperty("java.io.tmpdir"), name);
            this.bigFile.deleteOnExit();
            try (final Writer out = new FileWriter(this.bigFile)) {
                for (int index = 0; index < 1000; index ++) {
                    out.append("fake" + index);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException during test set up.");
        }
        this.checksumProvider = cp;
        this.checksumValues = checksumValues;
    }

    @Test
    public void testChecksumAsStringWithByteArray() {
        String actual = this.checksumProvider
                .putBytes("abc".getBytes())
                .checksumAsString();
        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.ABC));
    }

    @Test
    public void testChecksumAsStringWithPartOfByteArrayZeroOffset() {
        String actual = this.checksumProvider
                .putBytes("abcdef".getBytes(), 0, 3)
                .checksumAsString();
        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.ABC));
    }

    @Test
    public void testChecksumAsStringWithPartOfByteArrayNonZeroOffset() {
        String actual = this.checksumProvider
                .putBytes("defabc".getBytes(), 3, 3)
                .checksumAsString();
        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.ABC));
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testChecksumAsStringWithPartOfByteArrayShouldThrowIOOB() {
        this.checksumProvider.putBytes("abcdef".getBytes(), -1, -1);
    }


    @Test(expectedExceptions = NullPointerException.class)
    public void testPutBytesWithNullByteArrayShouldThrowNPE() {
        byte nullArray[] = null;
        this.checksumProvider.putBytes(nullArray);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testPutBytesWithChunkOfNullByteArrayShouldThrowNPE() {
        byte nullArray[] = null;
        this.checksumProvider.putBytes(nullArray, 123, 456);
    }

    @Test
    public void testChecksumAsStringWithEmptyByteArray() {
        String actual = this.checksumProvider
                .putBytes("".getBytes())
                .checksumAsString();
        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.EMPTYARRAY));
    }

    @Test
    public void testChecksumAsStringWithByteBuffer() {
        String actual = this.checksumProvider
                .putBytes(ByteBuffer.wrap("abc".getBytes()))
                .checksumAsString();
        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.ABC));
    }

    @Test
    public void testChecksumAsStringWithTruncatedByteBuffer() {
        ByteBuffer buffer = ByteBuffer.wrap("abcdef".getBytes());
        buffer.position(0).limit(3);
        String actual = this.checksumProvider
                .putBytes(buffer)
                .checksumAsString();
        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.ABC));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testChecksumAsBytesWithEmptyByteBufferShouldThrowIAE() {
        byte[] actual = this.checksumProvider
                .putBytes(ByteBuffer.allocateDirect(0))
                .checksumAsBytes();
        Assert.assertNull(actual);
    }

    @Test
    public void testChecksumAsStringWithSmallFilePathString() {
        String actual = this.checksumProvider
                .putFile(this.smallFile.getAbsolutePath())
                .checksumAsString();

        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.SMALLFILE));
    }

    @Test
    public void testChecksumAsStringWithMediumFilePathString() {
        String actual = this.checksumProvider
                .putFile(this.mediumFile.getAbsolutePath())
                .checksumAsString();

        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.MEDIUMFILE));
    }

    @Test
    public void testChecksumAsStringWithBigFilePathString() {
        String actual = this.checksumProvider
                .putFile(this.bigFile.getAbsolutePath())
                .checksumAsString();

        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.BIGFILE));
    }

    @Test
    public void testChecksumAsStringWithEmptyObject() {
        String actual = this.checksumProvider.checksumAsString();
        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.EMPTYARRAY));
    }

    @Test
    public void testChecksumAsStringWithSequentialPutBytes() {
        String actual = this.checksumProvider
                .putBytes("a".getBytes())
                .putBytes("bc".getBytes())
                .checksumAsString();
        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.ABC));
    }

    @Test
    public void testChecksumAsStringWithMixedPutBytes() {
        String actual = this.checksumProvider
                .putBytes("abc".getBytes())
                .putFile(this.smallFile.getAbsolutePath())
                .checksumAsString();
        Assert.assertEquals(actual, this.checksumValues
                .get(ChecksumTestVector.SMALLFILE));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testPutBytesAfterChecksumAsString() {
        this.checksumProvider
            .putBytes("abc".getBytes())
            .checksumAsString();
        this.checksumProvider.putBytes("abc".getBytes());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testPutBytesAfterChecksumAsBytes() {
        this.checksumProvider
            .putBytes("abc".getBytes())
            .checksumAsBytes();
        this.checksumProvider.putBytes("abc".getBytes());
    }
}