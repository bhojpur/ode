package net.bhojpur.gradle.dsl.utils

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

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.util.GradleVersion

class ResourceLoader {

    static def loadFiles(Project project, List<String> fileList) {
        return fileList.collect {
            loadFile(project, it)
        }
    }

    static def loadFiles(Project project, String[] fileList) {
        return fileList.collect {
            loadFile(project, it)
        }
    }

    static def loadFile(Project project, String resFile) {
        if (GradleVersion.current() >= GradleVersion.version('4.8')) {
            def url = IOUtils.getResource("/" + resFile) // getResource(project, resFile)
            if (!url) {
                throw new GradleException("can't find file file ${resFile}")
            }
            return project.resources.text.fromUri(url.toURI()).asFile()
        } else {
            return loadFileOrExtract(project, resFile)
        }
    }

    static def loadFileOrExtract(Project project, String resourceFile) {
        def result = new File("${project.buildDir}/${resourceFile}")
        // Check if file exists in build directory
        if (!result.exists()) {
            // Copy it to the projects build directory
            def inputStream = IOUtils.getResourceAsStream("/${resourceFile}")
            FileUtils.copyInputStreamToFile(inputStream, result)
        }
        return result
    }

}