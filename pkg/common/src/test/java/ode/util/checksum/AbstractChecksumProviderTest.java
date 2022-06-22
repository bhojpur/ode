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

import java.nio.ByteBuffer;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;

/**
 * A basic set of pure unit tests for {@link AbstractChecksumProvider}. Makes
 * heavy use of old jMock (1.x).
 */
public class AbstractChecksumProviderTest extends MockObjectTestCase {

    private Mock mockHashFunction, mockHasher;

    private AbstractChecksumProvider abstractChecksumProvider;

    @BeforeClass
    public void setUp() throws Exception {
        this.mockHashFunction = mock(HashFunction.class);
        this.mockHasher = mock(Hasher.class);
    }

    @Test
    public void testAbstractChecksumProviderCtor() {
        this.mockHashFunction.expects(once()).method("newHasher")
            .withNoArguments()
            .will(returnValue(this.mockHasher.proxy()));
        this.abstractChecksumProvider = new AbstractChecksumProvider(
                (HashFunction) mockHashFunction.proxy());
    }

    @Test
    public void testPutBytesWithByteArray() {
        this.mockHasher.expects(once()).method("putBytes");
        Object actual = this.abstractChecksumProvider.putBytes("abc".getBytes());
        Assert.assertTrue(actual instanceof AbstractChecksumProvider);
    }

    @Test
    public void testPutBytesWithByteBuffer() {
        this.mockHasher.expects(once()).method("putBytes");
        Object actual = this.abstractChecksumProvider.putBytes(
                ByteBuffer.wrap("abc".getBytes()));
        Assert.assertTrue(actual instanceof AbstractChecksumProvider);
    }

}