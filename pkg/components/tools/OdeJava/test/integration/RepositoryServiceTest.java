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

import ode.InternalException;
import ode.api.IRepositoryInfoPrx;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Collections of tests for the <code>Repository</code> service.
 */
public class RepositoryServiceTest extends AbstractServerTest {

    /**
     * Tests the <code>getFreeSpaceInKilobytes</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testFreeSpace() throws Exception {
        IRepositoryInfoPrx svc = root.getSession().getRepositoryInfoService();
        Assert.assertTrue(svc.getFreeSpaceInKilobytes() > 0);
    }

    /**
     * Tests the <code>getUsedSpaceInKilobytes</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(expectedExceptions = { InternalException.class })
    public void testUsedSpace() throws Exception {
        IRepositoryInfoPrx svc = root.getSession().getRepositoryInfoService();
        svc.getUsedSpaceInKilobytes();
    }

    /**
     * Tests the <code>getUsageFraction</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test(expectedExceptions = { InternalException.class })
    public void testUsageFraction() throws Exception {
        IRepositoryInfoPrx svc = root.getSession().getRepositoryInfoService();
        svc.getUsageFraction();
    }

}
