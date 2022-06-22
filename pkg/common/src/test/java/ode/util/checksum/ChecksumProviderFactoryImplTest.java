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

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for the {@link ChecksumProviderFactoryImpl} class.
 */
public class ChecksumProviderFactoryImplTest {

    private ChecksumProviderFactoryImpl cpf;
    
    @BeforeClass
    protected void setUp() throws Exception {
        this.cpf = new ChecksumProviderFactoryImpl();
    }

    @Test
    public void testGetProviderWithSHA1ChecksumType() {
        ChecksumProvider cp = this.cpf.getProvider(ChecksumType.SHA1);
        Assert.assertTrue(cp instanceof SHA1ChecksumProviderImpl);
    }

    @Test
    public void testGetProviderWithMD5ChecksumType() {
        ChecksumProvider cp = this.cpf.getProvider(ChecksumType.MD5);
        Assert.assertTrue(cp instanceof MD5ChecksumProviderImpl);
    }

    @Test
    public void testGetProviderWithAdler32ChecksumType() {
        ChecksumProvider cp = this.cpf.getProvider(ChecksumType.ADLER32);
        Assert.assertTrue(cp instanceof Adler32ChecksumProviderImpl);
    }

    @Test
    public void testGetProviderWithCRC32ChecksumType() {
        ChecksumProvider cp = this.cpf.getProvider(ChecksumType.CRC32);
        Assert.assertTrue(cp instanceof CRC32ChecksumProviderImpl);
    }

    @Test
    public void testGetProviderWithMurmur32ChecksumType() {
        ChecksumProvider cp = this.cpf.getProvider(ChecksumType.MURMUR32);
        Assert.assertTrue(cp instanceof Murmur32ChecksumProviderImpl);
    }

    @Test
    public void testGetProviderWithMurmur128ChecksumType() {
        ChecksumProvider cp = this.cpf.getProvider(ChecksumType.MURMUR128);
        Assert.assertTrue(cp instanceof Murmur128ChecksumProviderImpl);
    }

}