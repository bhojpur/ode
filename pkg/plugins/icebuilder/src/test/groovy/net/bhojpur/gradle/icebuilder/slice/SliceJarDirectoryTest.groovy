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

import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

class SliceJarDirectoryTest extends TestCase {

    def iceHome = null

    @Before
    public void createIceHome() {
        def isWindows = System.getProperty('os.name').toLowerCase().contains('windows')

        iceHome = File.createTempDir()
        createIceHomePath(["bin"])

        def copyFileBytes = { src, dst ->
           def srcFile = new File(src)
           def dstFile = new File(dst)
           dstFile << srcFile.bytes
           return dstFile
        }

        def dst =  [iceHome.toString(), "bin", new File(project.slice.slice2java).getName()].join(File.separator)
        copyFileBytes(project.slice.slice2java, dst).setExecutable(true)

        // For Ice 3.6 we also copy slice2java dependencies. This is unnecessary in
        // Ice 3.7 as slice2java is statically linked.
        if(isWindows && project.slice.compareIceVersion("3.7") == -1) {
           ['slice36.dll', 'iceutil36.dll'].each {
                def src = [new File(project.slice.slice2java).getParent(), it].join(File.separator)
                copyFileBytes(src, [iceHome.toString(), "bin", it].join(File.separator)).setExecutable(true)
           }
        }
    }

    @After
    public void cleanupIceHome() {
        // if(iceHome) {
        //     iceHome.deleteDir()
        //     iceHome.deleteOnExit()
        // }
    }

    def createIceHomePath(path) {
        def newPath = new File([iceHome.toString(), path.join(File.separator)].join(File.separator))
        newPath.mkdirs()
        newPath.toString()
    }

    @Test
    public void testSliceCommon() {
        def tmpSliceDir = createIceHomePath(["share", "slice"])
        project.slice.iceHome = iceHome.toString()
        assertNotNull(project.slice.sliceDir)
        assertTrue(project.slice.sliceDir == tmpSliceDir)
    }

    @Test
    public void testIce37SliceDir() {
        def tmpSliceDir = createIceHomePath(["share", "ice", "slice"])
        project.slice.iceHome = iceHome.toString()
        assertNotNull(project.slice.sliceDir)
        assertTrue(project.slice.sliceDir == tmpSliceDir)
    }

    @Test
    public void testIce36SliceDir() {
        def tmpSliceDir = createIceHomePath(["share", "Ice-${project.slice.iceVersion}", "slice"])
        project.slice.iceHome = iceHome.toString()
        assertNotNull(project.slice.sliceDir)
        assertTrue(project.slice.sliceDir == tmpSliceDir)
    }

    @Test
    public void testOptSourceSliceDir() {
        def tmpSliceDir = createIceHomePath(["slice"])
        project.slice.iceHome = iceHome.toString()
        assertNotNull(project.slice.sliceDir)
        assertTrue(project.slice.sliceDir == tmpSliceDir)
    }

}