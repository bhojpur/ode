package net.bhojpur.gradle.dsl.extensions

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
import ode.dsl.SemanticType
import ode.dsl.velocity.MultiFileGenerator
import org.gradle.api.Project
import org.gradle.api.Transformer
import org.gradle.api.provider.Property
import net.bhojpur.gradle.dsl.utils.SemanticTypeClosure
import net.bhojpur.gradle.dsl.utils.SemanticTypeTransformer

@CompileStatic
class MultiFileConfig extends BaseFileConfig {

    final Property<File> outputDir

    final Property<MultiFileGenerator.FileNameFormatter> formatOutput

    MultiFileConfig(String name, Project project) {
        super(name, project)
        this.outputDir = project.objects.property(File)
        this.formatOutput = project.objects.property(MultiFileGenerator.FileNameFormatter)
    }

    void outputDir(File dir) {
        setOutputDir(dir)
    }

    void outputDir(String dir) {
        setOutputDir(dir)
    }

    void setOutputDir(String dir) {
        setOutputDir(new File(dir))
    }

    void setOutputDir(File dir) {
        this.outputDir.set(dir)
    }

    void formatOutput(final Transformer<? extends String, ? super SemanticType> transformer) {
        setFormatOutput(transformer)
    }

    void formatOutput(Closure closure) {
        setFormatOutput(closure)
    }

    void setFormatOutput(final Transformer<? extends String, ? super SemanticType> transformer) {
        formatOutput.set(new SemanticTypeTransformer(transformer))
    }

    void setFormatOutput(Closure closure) {
        formatOutput.set(new SemanticTypeClosure(closure))
    }

}