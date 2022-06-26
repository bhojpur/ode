package net.bhojpur.gradle.server.tasks

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

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.file.FileTree
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.util.PatternSet

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@CompileStatic
class ImportResourcesTask extends DefaultTask {

    private static final Logger Log = Logging.getLogger(ImportResourcesTask)

    @InputFiles
    Configuration config

    @OutputDirectory
    File extractDir

    @Input
    String pattern

    private PatternSet patternSet = new PatternSet()

    @TaskAction
    void apply() {
        ResolvedArtifact artifact = config.resolvedConfiguration.resolvedArtifacts.find {
            it.name.contains("ode-model")
        }
        if (!artifact) {
            throw new GradleException("ode-model artifact not found")
        }

        // Set our pattern set
        patternSet.include(pattern)

        // obtain file tree for jar file
        FileTree fileTree = project.zipTree(artifact.file).matching(patternSet)

        // Copy each file matching pattern to our extract directory
        fileTree.files.each { File src ->
            Path file = src.toPath()
            Path to = extractDir.toPath()

            // Copy each file to output location
            Files.copy(src.toPath(), to.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING)
        }
    }

    void extractDir(Object dir) {
        setExtractDir(dir)
    }

    void setExtractDir(Object dir) {
        this.extractDir = project.file(dir)
    }

}