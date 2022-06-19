package ode.util.test;

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
import java.util.Arrays;

import ode.util.TempFileManager;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

// Upping timeout since travis can fail with the following:
// [nomemorytestng] FAILED: testBasicUsage on null(ode.util.test.TempFileManagerTest)
// [nomemorytestng] org.testng.internal.thread.ThreadTimeoutException: Method org.testng.internal.TestNGMethod.testBasicUsage() didn't finish within the time-out 1000
// [nomemorytestng]    at java.net.Inet6AddressImpl.lookupAllHostAddr(Native Method)
// [nomemorytestng]    at java.net.InetAddress$1.lookupAllHostAddr(InetAddress.java:901)
// [nomemorytestng]    at java.net.InetAddress.getAddressesFromNameService(InetAddress.java:1293)
// [nomemorytestng]    at java.net.InetAddress.getLocalHost(InetAddress.java:1469)
// [nomemorytestng]    at sun.management.VMManagementImpl.getVmId(VMManagementImpl.java:135)
// [nomemorytestng]    at sun.management.RuntimeImpl.getName(RuntimeImpl.java:59)
// [nomemorytestng]    at ode.util.TempFileManager.pid(TempFileManager.java:279)
// [nomemorytestng]    at ode.util.TempFileManager.<init>(TempFileManager.java:118)
//
@Test(groups = "unit", timeOut = 30000)
public class TempFileManagerTest {

    @Test
    public void testBasicUsage() throws IOException {
        File p = TempFileManager.create_path("foo", ".bar");
        Assert.assertTrue(p.exists());
        TempFileManager.remove_path(p);
        Assert.assertFalse(p.exists());
    };

    @Test
    public void testNoCleanUp() throws IOException {
        File p = TempFileManager.create_path("foo", ".bar");
        Assert.assertTrue(p.exists());
    }

    @Test
    public void testDeleteOnExit() throws IOException {
        File p = TempFileManager.create_path("foo", ".bar");
        p.deleteOnExit();
    }

    @Test
    public void testUsingThePath() throws IOException {
        File p = TempFileManager.create_path("write", ".txt");
        FileUtils.writeLines(p, Arrays.asList("hi"));
        String hi = FileUtils.readFileToString(p).trim();
        Assert.assertEquals(hi, "hi");
    }

    @Test
    public void testUsingThePathAndAFile() throws IOException {
        File p = TempFileManager.create_path("write", ".txt");
        FileUtils.writeLines(p, Arrays.asList("hi"));
        File f = new File(p.getAbsolutePath());
        String hi = FileUtils.readFileToString(f).trim();
        Assert.assertEquals(hi, "hi");
    }

    @Test
    public void testFolderSimple() throws IOException {
        File p = TempFileManager.create_path("close", ".dir", true);
        Assert.assertTrue(p.exists());
        Assert.assertTrue(p.isDirectory());
    }

    @Test
    public void testFolderWrite() throws IOException {
        File p = TempFileManager.create_path("close", ".dir", true);
        Assert.assertTrue(p.exists());
        Assert.assertTrue(p.isDirectory());
        File f = new File(p, "file");
        FileUtils.writeStringToFile(f, "hi");
    }

    @Test
    public void testFolderDelete() throws IOException {
        File p = TempFileManager.create_path("close", ".dir", true);
        Assert.assertTrue(p.exists());
        Assert.assertTrue(p.isDirectory());
        File f = new File(p, "file");
        FileUtils.writeStringToFile(f, "hi");
        FileUtils.deleteDirectory(p);
    }

}