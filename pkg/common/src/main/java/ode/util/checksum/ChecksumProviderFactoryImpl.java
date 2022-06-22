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

import java.util.Arrays;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * An implementation of the {@link ChecksumProviderFactory} interface.
 */
public class ChecksumProviderFactoryImpl implements ChecksumProviderFactory {

    private static final ImmutableSet<ChecksumType> availableChecksumTypes =
            Sets.immutableEnumSet(Arrays.asList(ChecksumType.values()));

    /**
     * @see ChecksumProviderFactory#getProvider(ChecksumType)
     */
    public ChecksumProvider getProvider(ChecksumType checksumType) {
        // Dumb implementation for now
        // TODO: remove the switch statement
        switch (checksumType) {
            case FILE_SIZE:
                return new FileSizeChecksumProviderImpl();
            case ADLER32:
                return new Adler32ChecksumProviderImpl();
            case CRC32:
                return new CRC32ChecksumProviderImpl();
            case MD5:
                return new MD5ChecksumProviderImpl();
            case MURMUR32:
                return new Murmur32ChecksumProviderImpl();
            case MURMUR128:
                return new Murmur128ChecksumProviderImpl();
            case SHA1:
            default:
                return new SHA1ChecksumProviderImpl();
        }
    }

    @Override
    public Set<ChecksumType> getAvailableTypes() {
        return availableChecksumTypes;
    }
}