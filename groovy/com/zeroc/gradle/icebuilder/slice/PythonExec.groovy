package com.zeroc.gradle.icebuilder.slice

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

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.AbstractExecTask

class PythonExec extends AbstractExecTask {

    PythonExec() {
        super(PythonExec.class)
        super.executable(project.slice.slice2py)
        super.args("-I${project.slice.sliceDir}")
    }

    def include(Object... arguments) {
        List modified = arguments.collect { "-I${it}" }
        return super.args(modified)
    }

    def prefix(String prefix) {
        return super.args("--prefix=${prefix}")
    }

    def outputDir(File dir) {
        return super.args("--output-dir=${dir}")
    }

    def iceFiles(FileCollection files) {
        return super.args(files.getFiles())
    }

}