package net.bhojpur.gradle.dsl

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
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.compile.JavaCompile
import net.bhojpur.gradle.dsl.extensions.DslExtension
import net.bhojpur.gradle.dsl.tasks.GeneratorBaseTask

@CompileStatic
class DslPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply(DslPluginBase)

        DslExtension dsl = project.extensions.getByType(DslExtension)

        configureForJavaPlugin(project, dsl)
    }

    void configureForJavaPlugin(Project project, DslExtension dsl) {
        project.plugins.withType(JavaPlugin) { JavaPlugin java ->
            // Configure default outputDir
            JavaPluginConvention javaConvention =
                    project.convention.getPlugin(JavaPluginConvention)

            SourceSet main =
                    javaConvention.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)

            main.java.srcDirs dsl.outputDir.dir("java")
            main.resources.srcDirs dsl.outputDir.dir("resources")

            // Configure compileJava task to depend on our tasks
            project.tasks.named("compileJava").configure { JavaCompile jc ->
                jc.dependsOn project.tasks.withType(GeneratorBaseTask)
            }
        }
    }

    // ToDo: fill this functionality in to handle jar naming
    static void configureForMavenPublish(Project project) {
        project.plugins.withType(MavenPublishPlugin) { MavenPublishPlugin plugin ->

        }
    }

}