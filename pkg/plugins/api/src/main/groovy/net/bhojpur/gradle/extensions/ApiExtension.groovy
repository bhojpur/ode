package net.bhojpur.gradle.api.extensions

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

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Provider

class ApiExtension {

    private final Project project

    final NamedDomainObjectContainer<SplitExtension> language

    final ConfigurableFileCollection combinedFiles

    final DirectoryProperty outputDir

    ApiExtension(Project project, NamedDomainObjectContainer<SplitExtension> language) {
        this.project = project
        this.language = language
        this.combinedFiles = project.files()
        this.outputDir = project.objects.directoryProperty()

        // Set conventions
        this.outputDir.convention(project.layout.projectDirectory.dir("src/api"))
    }

    void language(Action<? super NamedDomainObjectContainer<SplitExtension>> action) {
        action.execute(language)
    }

    void setOutputDir(String dir) {
        setOutputDir(new File(dir))
    }

    void setOutputDir(Directory dir) {
        this.outputDir.set(dir)
    }

    void setOutputDir(File dir) {
        this.outputDir.set(dir)
    }

    void setOutputDir(Provider<? extends Directory> dir) {
        this.outputDir.set(dir)
    }

}