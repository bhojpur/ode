package ode.system.utests;

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

import ode.system.UpgradeCheck;

import org.testng.Assert;
import org.testng.annotations.Test;

public class UpgradeCheckTest {

    String version =  "1.2.3";
    ode.system.UpgradeCheck check;

    @Test
    public void testNoActionOnNull() {
        check = new UpgradeCheck(null, version, "test");
        check.run();
        Assert.assertFalse(check.isUpgradeNeeded());
        Assert.assertFalse(check.isExceptionThrown());
    }

    @Test
    public void testNoActionOnEmpty() {
        check = new UpgradeCheck("", version, "test");
        check.run();
        Assert.assertFalse(check.isUpgradeNeeded());
        Assert.assertFalse(check.isExceptionThrown());
    }

    @Test
    public void testSlowResponse() {
        check = new UpgradeCheck("http://127.0.0.1:8000", version, "test");
        check.run();
        Assert.assertFalse(check.isUpgradeNeeded());
        Assert.assertTrue(check.isExceptionThrown());

    }

    @Test
    public void testSlowResponse2() {
        check = new UpgradeCheck("http://127.0.0.1:9998", version, "test");
        check.run();
        Assert.assertFalse(check.isUpgradeNeeded());
        Assert.assertTrue(check.isExceptionThrown());
    }

    @Test
    public void testBadIp() {
        check = new UpgradeCheck("200.200.200.200", version, "test");
        check.run();
        Assert.assertFalse(check.isUpgradeNeeded());
        Assert.assertTrue(check.isExceptionThrown());

    }

    @Test
    public void testWrongVersion() {
        check = new UpgradeCheck("200.200.200.200", "XYZ" + version, "test");
        check.run();
        Assert.assertFalse(check.isUpgradeNeeded());
        Assert.assertTrue(check.isExceptionThrown());

    }

    @Test(enabled = false)
    public void testBadUrl1() {
        check = new UpgradeCheck("http://foo", "XYZ" + version, "test");
        check.run();
        Assert.assertFalse(check.isUpgradeNeeded());
        Assert.assertTrue(check.isExceptionThrown());

    }

    @Test
    public void testBadUrl2() {
        check = new UpgradeCheck("file://dev/null", "XYZ" + version, "test");
        check.run();
        Assert.assertFalse(check.isUpgradeNeeded());
        Assert.assertTrue(check.isExceptionThrown());

    }

    @Test
    public void testBadUrl3() {
        check = new UpgradeCheck("abcp", "XYZ" + version, "test");
        check.run();
        Assert.assertFalse(check.isUpgradeNeeded());
        Assert.assertTrue(check.isExceptionThrown());

    }

    @Test
    public void testBadUrl4() {
        check = new UpgradeCheck("abc://bar", "XYZ" + version, "test");
        check.run();
        Assert.assertFalse(check.isUpgradeNeeded());
        Assert.assertTrue(check.isExceptionThrown());

    }

}