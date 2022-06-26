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

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Transformer
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import net.bhojpur.gradle.api.types.Language
import net.bhojpur.gradle.api.utils.ApiNamer

class SplitExtension {

    final String name

    final Project project

    final ConfigurableFileCollection combinedFiles

    final Property<Language> language

    final Property<File> outputDir

    final Property<ApiNamer> renamer

    SplitExtension(String name, Project project) {
        this.name = name
        this.project = project
        this.combinedFiles = project.files()
        this.language = project.objects.property(Language)
        this.outputDir = project.objects.property(File)
        this.renamer = project.objects.property(ApiNamer)

        // Optionally set language based on name of extension
        Language lang = Language.values().find { lang ->
            name.toUpperCase().contains(lang.name())
        }
        if (lang) {
            this.language.convention(lang)
        }
    }

    void combinedFiles(Iterable<?> files) {
        this.combinedFiles.from files
    }

    void combinedFiles(Object... files) {
        this.combinedFiles.from files
    }

    void setCombinedFiles(Iterable<?> files) {
        this.combinedFiles.setFrom(files)
    }

    void setCombinedFiles(Object... files) {
        this.combinedFiles.setFrom(files)
    }

    void language(String language) {
        setLanguage(language)
    }

    void language(Language lang) {
        setLanguage(lang)
    }

    void setLanguage(Language lang) {
        this.language.set(lang)
    }

    void setLanguage(String languageString) {
        Language lang = Language.find(languageString)
        if (lang == null) {
            throw new GradleException("Unsupported language: ${languageString}")
        }
        this.language.set(lang)
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

    void rename(Transformer<? extends String, ? extends String> renamer) {
        this.renamer.set(new ApiNamer(renamer))
    }

    void rename(String sourceRegEx, String replaceWith) {
        this.renamer.set(new ApiNamer(sourceRegEx, replaceWith))
    }

    void rename(String replaceWith) {
        this.renamer.set(new ApiNamer(null, replaceWith))
    }

}