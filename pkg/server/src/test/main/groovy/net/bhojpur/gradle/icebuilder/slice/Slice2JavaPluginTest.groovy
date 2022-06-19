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

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder

import static org.junit.Assert.assertTrue

class Slice2JavaPluginTest extends TestCase {

    @Test
    public void testSlice2JavaWithDefaults() {
        // Where builder checks for slice files by default
        pathToFile([project.rootDir, 'src', 'main', 'slice']).mkdirs()

        writeTestSliceToFile(pathToFile([project.rootDir, 'src', 'main', 'slice', 'Test.ice']))

        project.tasks.compileSlice.action()

        assertTrue(pathToFile([project.rootDir, 'build', 'generated-src', 'Test']).exists())
        assertTrue(pathToFile([project.rootDir, 'build', 'generated-src', 'Test', 'Hello.java']).exists())
    }

    @Test
    public void testSlice2JavaSliceSrcDir() {
        def sliceDir = pathToFile([project.rootDir, 'src', 'other', 'slice'])
        sliceDir.mkdirs()

        project.slice.java {
            srcDir = sliceDir.toString()
        }

        writeTestSliceToFile(pathToFile([project.rootDir, 'src', 'other', 'slice', 'Test.ice']))

        project.tasks.compileSlice.action()

        assertTrue(pathToFile([project.rootDir, 'build', 'generated-src', 'Test']).exists())
        assertTrue(pathToFile([project.rootDir, 'build', 'generated-src', 'Test', 'Hello.java']).exists())
    }

    @Test
    public void testRemovingGeneratedFiles() {
        // Where builder checks for slice files by default
        pathToFile([project.rootDir, 'src', 'main', 'slice']).mkdirs()

        writeTestSliceToFile(pathToFile([project.rootDir, 'src', 'main', 'slice', 'Test.ice']))

        project.tasks.compileSlice.action()

        def geneatedHello = pathToFile([project.rootDir, 'build', 'generated-src', 'Test', 'Hello.java'])

        assertTrue(geneatedHello.exists())
        geneatedHello.delete()
        assertTrue(!geneatedHello.exists())

        // Project tasks should not be re-executed and we are not using a tool like
        // GradleConnector/GradleRunner. So instead we make a new project and run it.
        def p = newProjectWithProjectDir()
        p.pluginManager.apply 'java'
        p.pluginManager.apply 'slice'
        p.tasks.compileSlice.action()
        assertTrue(geneatedHello.exists())
    }

    private void writeTestSliceToFile(file) {
        file << """
            |module Test
            |{
            |
            |interface Hello
            |{
            |    idempotent void sayHello(int delay);
            |    void shutdown();
            |};
            |
            |};
        """.stripMargin()
    }
}