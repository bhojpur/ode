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
import java.nio.ByteBuffer;

import com.google.common.hash.HashCode;

/**
 * Trivial checksum provider whose hash is simply the number of bytes that were put into the calculation.
 */
public class FileSizeChecksumProviderImpl implements ChecksumProvider {
    private long size = 0;
    private boolean isChecksumCalculated = false;

    @Override
    public ChecksumProvider putBytes(byte[] byteArray) {
        verifyState();

        size += byteArray.length;
        return this;
    }

    @Override
    public ChecksumProvider putBytes(byte[] byteArray, int offset, int length) {
        verifyState();

        /* provide bounds exception specified by method documentation */
        @SuppressWarnings("unused")
        final byte start = byteArray[offset];
        @SuppressWarnings("unused")
        final byte end = byteArray[offset + length - 1];

        size += length;
        return this;
    }

    @Override
    public ChecksumProvider putBytes(ByteBuffer byteBuffer) {
        verifyState();

        if (!byteBuffer.hasArray()) {
            throw new IllegalArgumentException("Supplied ByteBuffer has " +
                    "inaccessible array.");
        }

        size += byteBuffer.limit() - byteBuffer.position();
        return this;
    }

    @Override
    public ChecksumProvider putFile(String filePath) {
        verifyState();

        size = new File(filePath).length();
        return this;
    }

    @Override
    public byte[] checksumAsBytes() {
        isChecksumCalculated = true;
        /* use Guava hash code to match the other algorithms */
        return HashCode.fromLong(size).asBytes();
    }

    @Override
    public String checksumAsString() {
        isChecksumCalculated = true;
        return HashCode.fromLong(size).toString();
    }

    private void verifyState() {
        if (isChecksumCalculated) {
            throw new IllegalStateException("Checksum state already set. " +
                    "Mutation illegal.");
        }
    }
}