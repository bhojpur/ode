package net.bhojpur.gradle.icebuilder.slice

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

import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue
import static org.junit.Assume.assumeTrue

class Slice2JavaNuGetIceHome extends TestCase {
    @Before
    public void checkIsWindows() {
        // For what we are testing our ice version must be >= 3.7 on Windows os

        assumeTrue(project.slice.compareIceVersion('3.7') >= 0)
        assumeTrue(System.getProperty('os.name').toLowerCase().contains('windows'))
    }

    @Test void testCppNugetIceHome() {
        def nuget = File.createTempDir()
        def tools = new File([nuget.toString(), "tools"].join(File.separator))
        tools.mkdirs()
        def copyFileBytes = { src, dst ->
           def srcFile = new File(src)
           def dstFile = new File(dst)
           dstFile << srcFile.bytes
           return dstFile
        }

        def dst =  [nuget.toString(), "tools", new File(project.slice.slice2java).getName()].join(File.separator)
        copyFileBytes(project.slice.slice2java, dst).setExecutable(true)

        project.slice.iceHome = nuget.toString()
        assertTrue(project.slice.slice2java == dst)
    }
}