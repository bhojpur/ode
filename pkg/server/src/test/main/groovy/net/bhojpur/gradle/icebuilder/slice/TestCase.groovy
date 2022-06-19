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

import org.gradle.testfixtures.ProjectBuilder
import org.junit.After
import org.junit.Before

import static org.junit.Assume.assumeNoException
import static org.junit.Assume.assumeNotNull

class TestCase {
    def project = null

    @Before
    void createProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'java'
        project.pluginManager.apply 'slice'
    }

    @Before
    void checkIceInstalled() {
        assumeNotNull(project.slice.iceHome)
        assumeNotNull(project.slice.slice2java)
    }

    @After
    public void cleanupProject() {
        project.delete()
        project = null
    }

    def newProjectWithProjectDir() {
        def p = ProjectBuilder.builder().withProjectDir(project.rootDir).build()
        p.pluginManager.apply 'java'
        p.pluginManager.apply 'slice'
        return p
    }

    void forceReinitialization() {
        // Setting any property will trigger the plug-in initialization in the next property read.
        //
        // Set cppConfiguration and cppPlatform to null is required to force reading CPP_PLATFORM and
        // CPP_CONFIGURATION enviroment variables during re-intialization
        project.slice.cppConfiguration = null
        project.slice.cppPlatform = null
    }

    def pathToFile(pathList) {
        return new File(pathList.join(File.separator))
    }
}