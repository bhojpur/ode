package net.bhojpur.gradle.api.tasks

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
import org.gradle.api.GradleException
import org.gradle.api.file.CopySpec
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileTree
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import net.bhojpur.gradle.api.types.Language
import net.bhojpur.gradle.api.types.Prefix
import net.bhojpur.gradle.api.utils.ApiNamer

@CompileStatic
class SplitTask extends SourceTask {

    /**
     * Directory to spit out source files
     */
    private final DirectoryProperty outputDir = project.objects.directoryProperty()

    /**
     * Optional rename params (from, to) that support regex
     */
    private final Property<ApiNamer> namer = project.objects.property(ApiNamer)

    /**
     * List of the languages we want to split from .combinedFiles files
     */
    private final Property<Language> language = project.objects.property(Language)


    private static final Logger Log = Logging.getLogger(SplitTask)

    @TaskAction
    void action() {
        language.get().prefixes.each { Prefix prefix ->
            // Transform prefix enum to lower case for naming
            String prefixName = prefix.name().toLowerCase()

            // Assign default to rename
            ApiNamer apiNamer = namer.getOrElse(new ApiNamer())

            project.sync { CopySpec c ->
                c.into outputDir
                c.from getSource()
                c.rename apiNamer.getRenamer(prefix)
                c.filter { String line -> filerLine(line, prefixName) }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PathSensitive(PathSensitivity.RELATIVE)
    FileTree getSource() {
        return super.getSource()
    }

    @OutputDirectory
    DirectoryProperty getOutputDir() {
        return this.outputDir
    }

    @Input
    Property<Language> getLanguage() {
        return this.language
    }

    @Input
    @Optional
    Property<ApiNamer> getNamer() {
        return this.namer
    }

    void setOutputDir(File dir) {
        this.outputDir.set(dir)
    }

    void setOutputDir(Directory dir) {
        this.outputDir.set(dir)
    }

    void setOutputDir(Provider<Directory> provider) {
        this.outputDir.set(provider)
    }

    void setLanguage(String language) {
        Language lang = Language.find(language)
        if (lang == null) {
            throw new GradleException("Unsupported language : ${language}")
        }
        setLanguage(lang)
    }

    void setLanguage(Language lang) {
        this.language.set(lang)
    }

    void setLanguage(Provider<? extends Language> lang) {
        this.language.set(lang)
    }

    void setNamer(Provider<? extends ApiNamer> provider) {
        this.namer.set(provider)
    }

    void setNamer(ApiNamer apiNamer) {
        this.namer.set(apiNamer)
    }

    private static def filerLine(String line, String prefix) {
        return line.matches("^\\[all](.*)|^\\[${prefix}](.*)") ?
                line.replaceAll("^\\[all]\\s?|^\\[${prefix}]\\s?", "") :
                null
    }

}