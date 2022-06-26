package net.bhojpur.gradle.dsl.tasks

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
import groovy.transform.Internal
import ode.dsl.velocity.Generator
import ode.dsl.velocity.MultiFileGenerator
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory

@SuppressWarnings("UnstableApiUsage")
@CompileStatic
class FilesGeneratorTask extends GeneratorBaseTask {

    /**
     * Set this when you want to generate multiple files
     * Note: also requires setting {@link this.formatOutput}
     */
    private final DirectoryProperty outputDir = project.objects.directoryProperty()

    /**
     * Default callback returns SemanticType.shortName
     */
    private final Property<MultiFileGenerator.FileNameFormatter> formatOutput =
            project.objects.property(MultiFileGenerator.FileNameFormatter)

    @Override
    protected Generator.Builder createGenerator() {
        return new MultiFileGenerator.Builder()
                .setOutputDir(outputDir.get().asFile)
                .setFileNameFormatter(formatOutput.get())
    }

    @OutputDirectory
    DirectoryProperty getOutputDir() {
        return this.outputDir
    }

    @Nested
    @Internal
    Property<MultiFileGenerator.FileNameFormatter> getFormatOutput() {
        return this.formatOutput
    }

}