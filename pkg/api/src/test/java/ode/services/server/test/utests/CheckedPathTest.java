package ode.services.server.test.utests;

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

import ode.services.server.repo.CheckedPath;
import ode.services.server.repo.path.FilePathRestrictionInstance;
import ode.services.server.repo.path.FilePathRestrictions;
import ode.services.server.repo.path.ServerFilePathTransformer;
import ode.services.server.repo.path.MakePathComponentSafe;
import ode.ValidationException;
import ode.util.TempFileManager;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = { "fs" })
public class CheckedPathTest {
    private final FilePathRestrictions conservativeRules =
            FilePathRestrictionInstance.getFilePathRestrictions(FilePathRestrictionInstance.values());

    File dir;
    CheckedPath root;
    ServerFilePathTransformer serverPaths;

    @BeforeClass
    public void setup() throws Exception {
        this.dir = TempFileManager.create_path("repo", "test", true);
        this.serverPaths = new ServerFilePathTransformer();
        this.serverPaths.setBaseDirFile(this.dir);
        this.serverPaths.setPathSanitizer(new MakePathComponentSafe(this.conservativeRules));
    }

    @Test
    public void testCtorWithRootPathPasses() throws Exception {
        CheckedPath cp = new CheckedPath(this.serverPaths, "", null, null);
        Assert.assertTrue(cp.isRoot);
    }

    @Test(expectedExceptions=ValidationException.class)
    public void testCtorWithPathAboveRootThrows() throws ValidationException {
        new CheckedPath(this.serverPaths, "..", null, null);
    }

    @Test
    public void testCtorWithPathBelowRootPasses() throws Exception {
        CheckedPath cp = new CheckedPath(this.serverPaths, "foo", null, null);
        Assert.assertFalse(cp.isRoot);
    }

    @Test
    public void testMustExistPassesWithExistingFile() throws Exception {
        File f = new File(this.dir, "foo");
        FileUtils.touch(f);
        CheckedPath cp = new CheckedPath(this.serverPaths, f.getName(), null, null);
        Assert.assertEquals(cp.mustExist(), cp);
    }

    @Test(expectedExceptions=ValidationException.class)
    public void testMustExistThrowsWithNonexistingFile()
            throws ValidationException {
        new CheckedPath(this.serverPaths, "bar", null, null).mustExist();
    }
}