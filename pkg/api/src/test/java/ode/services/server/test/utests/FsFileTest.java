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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ode.services.server.repo.path.FsFile;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"fs"})
public class FsFileTest {
    /**
     * Test that {@link FsFile} instances are the same regardless of how they were constructed.
     */
    @Test
    public void testFsFileConstructorEquivalence() {
        final Set<FsFile> files = new HashSet<FsFile>();
        files.add(new FsFile("a", "b", "c"));
        files.add(new FsFile(Arrays.asList("a", "b", "c")));
        files.add(new FsFile(new FsFile("a", "b", "c"), 6));
        files.add(new FsFile(new FsFile("p", "q", "a", "b", "c"), 3));
        files.add(new FsFile("a/b/c"));
        files.add(new FsFile("//a//b//c//"));
        files.add(FsFile.concatenate(new FsFile(),
                                     new FsFile("a"),
                                     new FsFile(),
                                     new FsFile("b"),
                                     new FsFile("c")));
        Assert.assertEquals(files.size(), 1,
                "different means of constructing the same FsFile should be equivalent");
        Assert.assertTrue(files.add(new FsFile("c/b/a")),
                "different FsFiles should not be equivalent");
        
    }
    
    /**
     * Test relative file path query where the child is within the parent.
     */
    @Test
    public void testChildPathLegal() {
        final FsFile parent = new FsFile("a/b/c");
        final FsFile child = new FsFile("a/b/c/d/e");
        Assert.assertEquals(child.getPathFrom(parent).toString(), "d/e",
                "unexpected result for relative path");
    }
    
    /**
     * Test relative file path query where the child is the parent.
     */
    @Test
    public void testChildPathSame() {
        final FsFile path = new FsFile("a/b/c");
        Assert.assertEquals(path.getPathFrom(path).toString(), "",
                "relative path to same directory should be empty");
    }
    
    /**
     * Test relative file path query where the child is not within the parent.
     */
    @Test
    public void testChildPathIllegal() {
        final FsFile parent = new FsFile("a/c/c");
        final FsFile child = new FsFile("a/b/c/d/e");
        Assert.assertNull(child.getPathFrom(parent),
                "relative path may only be within parent directory");
    }
    
    /**
     * Test that empty paths have empty properties.
     */
    @Test
    public void testEmptyPathEmptiness() {
        Assert.assertTrue(FsFile.emptyPath.getComponents().isEmpty(),
                "the empty path should be empty");
        Assert.assertEquals(FsFile.emptyPath.toString(), "",
                "the empty path should be empty");
    }
}