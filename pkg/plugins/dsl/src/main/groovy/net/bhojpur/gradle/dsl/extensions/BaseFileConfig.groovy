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
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property

@CompileStatic
class BaseFileConfig {

    final String name

    final Project project

    final ConfigurableFileCollection odeXmlFiles

    final Property<File> template

    BaseFileConfig(String name, Project project) {
        this.name = name
        this.project = project
        this.odeXmlFiles = project.files()
        this.template = project.objects.property(File)
    }

    void odeXmlFiles(Object... files) {
        this.odeXmlFiles.from(files)
    }

    void setOdeXmlFiles(Object... files) {
        this.odeXmlFiles.setFrom(files)
    }

    void template(String template) {
        setTemplate(template)
    }

    void template(File template) {
        setTemplate(template)
    }

    void setTemplate(String t) {
        setTemplate(new File(t))
    }

    void setTemplate(File t) {
        this.template.set(t)
    }

}


