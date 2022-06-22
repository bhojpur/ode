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

import java.util.Set;

/**
 * A factory producing throw-away objects of the {@link ChecksumProvider} type.
 */
public interface ChecksumProviderFactory {

    /**
     * Returns an implementation of {@link ChecksumProvider} depending on the
     * specified {@link ChecksumType}.
     *
     * @param checksumType The type of requested {@link ChecksumProvider}.
     * @return The {@link ChecksumProvider} implementation.
     */
    ChecksumProvider getProvider(ChecksumType checksumType);

    /**
     * @return the values for which this instance creates a corresponding provider from {@link #getProvider(ChecksumType)}
     */
    Set<ChecksumType> getAvailableTypes();
}