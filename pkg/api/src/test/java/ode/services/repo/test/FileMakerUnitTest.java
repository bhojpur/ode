package ode.services.repo.test;

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

import ode.conditions.InternalException;
import ode.services.server.repo.FileMaker;

import org.testng.annotations.Test;

/**
 * Tests the simple class responsible for creating, reading, and locking the
 * repository uuid file.
 */
@Test(groups = { "repo" })
public class FileMakerUnitTest extends AbstractRepoUnitTest {

    @Test(expectedExceptions = InternalException.class)
    public void testGetNotInitializedThrows() throws Exception {
        FileMaker fm = new FileMaker(tmpRepo.getAbsolutePath());
        fm.getLine();
    }

    @Test(expectedExceptions = InternalException.class)
    public void testWriteNotInitializedThrows() throws Exception {
        FileMaker fm = new FileMaker(tmpRepo.getAbsolutePath());
        fm.writeLine(null);
    }

    public void testGetDir() throws Exception {
        FileMaker fm = new FileMaker(tmpRepo.getAbsolutePath());
        assertEquals(tmpRepo.getAbsolutePath(), fm.getDir());
    }

    public void testBlank() throws Exception {
        FileMaker fm = new FileMaker(tmpRepo.getAbsolutePath());
        fm.init("blank");
        String line = fm.getLine();
        assertEquals(null, line);
    }

    public void testInitialized() throws Exception {
        FileMaker fm = new FileMaker(tmpRepo.getAbsolutePath());
        fm.init("settofoo");
        fm.writeLine("foo");
        String line = fm.getLine();
        assertEquals("foo", line);
    }

    public void testClose() throws Exception {
        String lockFilename = "uuid";
        StringBuilder sb = new StringBuilder();
        sb.append(File.separator);
        sb.append(".ode");
        sb.append(File.separator);
        sb.append("repository");
        sb.append(File.separator);
        sb.append(lockFilename);
        sb.append(File.separator);
        sb.append(".lock");
        FileMaker fm = new FileMaker(tmpRepo.getAbsolutePath());
        fm.init(lockFilename);
        fm.close();
        assertFalse(new File(fm.getDir() + sb.toString()).exists());
    }

}