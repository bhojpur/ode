package net.bhojpur.gradle

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

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Groovydoc
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.kotlin.dsl.*

class AdditionalArtifactsPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        plugins.withType<JavaPlugin> {
            val sourceSets = the<SourceSetContainer>()

            val javadoc by tasks.named<Javadoc>("javadoc") {
                // Configure java doc options
                val stdOpts = options as StandardJavadocDocletOptions
                stdOpts.addStringOption("Xdoclint:none", "-quiet")
                if (JavaVersion.current().isJava9Compatible) {
                    stdOpts.addBooleanOption("html5", true)
                }
            }

            tasks.register<Jar>("sourcesJar") {
                description = "Creates a jar of java sources, classified -sources"
                archiveClassifier.set("sources")
                from(sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].allSource)
            }

            tasks.register<Jar>("javadocJar") {
                description = "Creates a jar of java docs, classified -javadoc"
                archiveClassifier.set("javadoc")
                from(javadoc)
            }
        }

        plugins.withType<GroovyPlugin> {
            val groovydoc by tasks.existing(Groovydoc::class)

            tasks.register<Jar>("groovydocJar") {
                description = "Creates a jar of groovy docs, classified -groovydoc"
                archiveClassifier.set("groovydoc")
                from(groovydoc)
            }
        }
    }
}