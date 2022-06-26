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
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

import static net.bhojpur.gradle.dsl.FileTypes.PATTERN_DB_TYPE
import static net.bhojpur.gradle.dsl.FileTypes.PATTERN_ODE_XML
import static net.bhojpur.gradle.dsl.FileTypes.PATTERN_TEMPLATE

@CompileStatic
class DslExtension {

    private final Project project

    final VelocityConfig velocity = new VelocityConfig()

    final NamedDomainObjectContainer<MultiFileConfig> multiFile

    final NamedDomainObjectContainer<SingleFileConfig> singleFile

    final ConfigurableFileCollection odeXmlFiles

    final ConfigurableFileCollection databaseTypes

    final ConfigurableFileCollection templates

    final Property<String> database

    final DirectoryProperty outputDir

    DslExtension(Project project,
                 NamedDomainObjectContainer<MultiFileConfig> multiFile,
                 NamedDomainObjectContainer<SingleFileConfig> singleFile) {
        this.project = project
        this.multiFile = multiFile
        this.singleFile = singleFile
        this.odeXmlFiles = project.files()
        this.databaseTypes = project.files()
        this.templates = project.files()
        this.database = project.objects.property(String)
        this.outputDir = project.objects.directoryProperty()

        // Set some conventions
        this.database.convention("psql")
        this.outputDir.convention(project.layout.projectDirectory.dir("build/psql"))
        this.odeXmlFiles.setFrom(project.fileTree(dir: "src/main/resources/mappings", include: PATTERN_ODE_XML))
        this.databaseTypes.setFrom(project.fileTree(dir: "src/main/resources/properties", include: PATTERN_DB_TYPE))
        this.templates.setFrom(project.fileTree(dir: "src/main/resources/templates", include: PATTERN_TEMPLATE))
    }

    void multiFile(Action<? super NamedDomainObjectContainer<MultiFileConfig>> action) {
        action.execute(this.multiFile)
    }

    void singleFile(Action<? super NamedDomainObjectContainer<SingleFileConfig>> action) {
        action.execute(this.singleFile)
    }

    void odeXmlFiles(FileCollection files) {
        this.odeXmlFiles.from files
    }

    void setOdeXmlFiles(FileCollection files) {
        this.odeXmlFiles.setFrom(files)
    }

    void databaseTypes(FileCollection files) {
        this.databaseTypes.from files
    }

    void setDatabaseTypes(FileCollection files) {
        this.databaseTypes.setFrom files
    }

    void templates(FileCollection files) {
        this.templates.from files
    }

    void setTemplates(FileCollection files) {
        this.templates.setFrom files
    }

    void velocityConfig(Action<? super VelocityConfig> action) {
        setTemplates(action)
    }

    void setTemplates(Action<? super VelocityConfig> action) {
        action.execute(velocity)
    }

    void setOutputDir(Provider<? extends Directory> dir) {
        this.outputDir.set(dir)
    }

    void setOutputDir(Directory dir) {
        this.outputDir.set(dir)
    }

    void setOutputDir(File dir) {
        this.outputDir.set(dir)
    }

    void database(String db) {
        this.database.set(db)
    }

}