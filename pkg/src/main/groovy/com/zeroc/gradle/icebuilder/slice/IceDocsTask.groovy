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

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class IceDocsTask extends DefaultTask {

    private static final def Log = Logging.getLogger(IceDocsTask)

    @Input
    @Optional
    final Property<Boolean> underscore = project.objects.property(Boolean)

    @Input
    @Optional
    final Property<Boolean> debug = project.objects.property(Boolean)

    @Input
    @Optional
    final Property<Integer> index = project.objects.property(Integer)

    @Input
    @Optional
    final Property<Integer> summary = project.objects.property(Integer)

    @InputFile
    @Optional
    final RegularFileProperty header = project.objects.fileProperty()

    @InputFile
    @Optional
    final RegularFileProperty footer = project.objects.fileProperty()

    @InputFile
    @Optional
    final RegularFileProperty indexHeader = project.objects.fileProperty()

    @InputFile
    @Optional
    final RegularFileProperty indexFooter = project.objects.fileProperty()

    @OutputDirectory
    final DirectoryProperty outputDir = project.objects.directoryProperty()

    @InputFiles
    @Optional
    FileCollection includeDirs

    @InputFiles
    @Optional
    FileCollection sourceDirs

    @InputFile
    @Optional
    final RegularFileProperty src = project.objects.fileProperty()

    // Change this to a configuration
    @Input
    SliceExtension sliceExt = project.slice

    @TaskAction
    void apply() {
        List<String> cmd = ["slice2html"]

        cmd.addAll(["-I", sliceExt.sliceDir])

        if (includeDirs) {
            // Add any additional includes
            includeDirs.each { dir -> cmd.addAll(["-I", "${dir}"]) }
        }

        cmd.addAll(["--output-dir", String.valueOf(outputDir.asFile.get())])

        if (header.isPresent()) {
            cmd.addAll(["--hdr", String.valueOf(header.asFile.get())])
        }

        if (footer.isPresent()) {
            cmd.addAll(["--ftr", String.valueOf(footer.asFile.get())])
        }

        if (indexHeader.isPresent()) {
            cmd.addAll(["--indexhdr", String.valueOf(indexHeader.asFile.get())])
        }

        if (indexFooter.isPresent()) {
            cmd.addAll(["--indexftr", String.valueOf(indexFooter.asFile.get())])
        }

        if (debug.getOrElse(false)) {
            cmd.add("-d")
        }

        // Add the source files
        if (sourceDirs) {
            sourceDirs.each { dir ->
                new File(dir.absolutePath).traverse(type: groovy.io.FileType.FILES) { it ->
                    if (it.name.endsWith('.ice')) {
                        cmd.add(String.valueOf(it))
                    }
                }
            }
        }

        executeCommand(cmd)
    }

    void executeCommand(List cmd) {
        def p = cmd.execute()
        p.waitForProcessOutput(new StringBuffer(), System.err)
        if (p.exitValue() != 0) {
            throw new GradleException("${cmd[0]} failed with exit code: ${p.exitValue()}")
        }
    }

}