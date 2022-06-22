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
import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.common.base.Optional;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.io.Files;

/**
 * Abstract skeleton class implementing {@link ChecksumProvider} and providing
 * implementations of the interface methods using a universal checksum class
 * object. Classes extending this class shall pass in a concrete checksum
 * algorithm implementation (a {@link HashFunction} instance) as the constructor
 * parameter.
 */
public class AbstractChecksumProvider implements ChecksumProvider {

    private final HashFunction hashFunction;

    private Hasher hasher;

    private Optional<HashCode> hashCode = Optional.absent();

    private Optional<byte[]> hashBytes = Optional.absent();

    private Optional<String> hashString = Optional.absent();

    /**
     * Protected constructor. There should not be an instance of this class.
     * @param hashFunction
     */
    protected AbstractChecksumProvider(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
        this.hasher = this.hashFunction.newHasher();
    }

    /**
     * @see ChecksumProvider#putBytes(byte[])
     */
    public ChecksumProvider putBytes(byte[] byteArray) {
        return this.putBytes(byteArray, 0, byteArray.length);
    }

    /**
     * @see ChecksumProvider#putBytes(byte[], int, int)
     */
    public ChecksumProvider putBytes(byte[] byteArray, int offset, int length) {
        this.verifyState(this.hashBytes, this.hashString);
        this.hasher.putBytes(byteArray, offset, length);
        return this;
    }

    /**
     * @see ChecksumProvider#putBytes(ByteBuffer)
     */
    public ChecksumProvider putBytes(ByteBuffer byteBuffer) {
        this.verifyState(this.hashBytes, this.hashString);
        if (byteBuffer.hasArray()) {
            this.hasher.putBytes(byteBuffer.array(), 0, byteBuffer.limit());
            return this;
        } else {
            throw new IllegalArgumentException("Supplied ByteBuffer has " +
                    "inaccessible array.");
        }
    }

    /**
     * @see ChecksumProvider#putFile(String)
     */
    public ChecksumProvider putFile(String filePath) {
        this.verifyState(this.hashBytes, this.hashString);
        try {
            this.hashCode = Optional.of(
                    Files.hash(new File(filePath), this.hashFunction));
            return this;
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    /**
     * @see ChecksumProvider#checksumAsBytes()
     */
    public byte[] checksumAsBytes() {
        this.hashBytes = Optional.of(this.pickChecksum().asBytes());
        return this.hashBytes.get();
    }

    /**
     * @see ChecksumProvider#checksumAsString()
     */
    public String checksumAsString() {
        this.hashString = Optional.of(this.pickChecksum().toString());
        return this.hashString.get();
    }

    private HashCode pickChecksum() {
        return this.hashCode.isPresent() ?
                this.hashCode.get() : this.hasher.hash();
    }

    private void verifyState(Optional... optionalObjects) {
        for (Optional optionalObject : optionalObjects) {
            if (optionalObject.isPresent()) {
                throw new IllegalStateException("Checksum state already set. " +
                        "Mutation illegal.");
            }
        }
    }
}