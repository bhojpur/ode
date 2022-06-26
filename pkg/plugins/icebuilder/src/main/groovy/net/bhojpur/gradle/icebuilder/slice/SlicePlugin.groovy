package net.bhojpur.gradle.icebuilder.slice;

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

import org.gradle.api.logging.Logging
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException

class SlicePlugin implements Plugin<Project> {
    private static final def LOGGER = Logging.getLogger(SliceTask)

    void apply(Project project) {
        project.tasks.create('compileSlice', SliceTask) {
            group = "Slice"
        }

        // Create and install the extension object.
        def slice = project.extensions.create("slice", SliceExtension,
                project.container(Java),
                project.container(Python, { name -> new Python(name, project) })
        )

        // slice.extensions.add("python", project.container(Python))

        slice.extensions.create("freezej", Freezej,
                project.container(Dict), project.container(Index))

        slice.output = project.file("${project.buildDir}/generated-src")

        if (isAndroidProject(project)) {
            project.afterEvaluate {
                // Android projects do not define a 'compileJava' task. We wait until the project is evaluated
                // and add our dependency to the variant's javaCompiler task.
                getAndroidVariants(project).all { variant ->
                    variant.registerJavaGeneratingTask(project.tasks.getByName('compileSlice'), slice.output)
                }
            }
        } else {
//            project.sourceSets.main.java.srcDir slice.output
            project.afterEvaluate {
                def compileJava = project.tasks.getByName("compileJava")
                if (compileJava) {
                    compileJava.dependsOn('compileSlice')
                }
            }
        }
    }

    def isAndroidProject(Project project) {
        return project.hasProperty('android') && project.android.sourceSets
    }

    def getAndroidVariants(Project project) {
        return project.android.hasProperty('libraryVariants') ?
                project.android.libraryVariants : project.android.applicationVariants
    }
}