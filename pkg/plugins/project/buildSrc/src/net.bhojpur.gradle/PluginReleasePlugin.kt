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

import org.ajoberstar.grgit.Grgit
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import net.bhojpur.gradle.reckon.gradle.ReckonExtension
import net.bhojpur.gradle.reckon.gradle.ReckonPlugin

class PluginReleasePlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        applyReckonPlugin()
        configureReleasePluginExtension()
    }

    private
    fun Project.applyReckonPlugin() {
        apply<ReckonPlugin>()
    }

    private
    fun Project.configureReleasePluginExtension() {
        configure<ReckonExtension> {
            // set default to patch, saying as that's what we tend to
            // work towards first
            scopeOptions(mapOf("scope" to "patch"))
        }

        // safety checks before releasing
        tasks.named(ReckonPlugin.TAG_TASK).configure {
            doFirst {
                val grgit = (project.findProperty("grgit")
                        ?: throw GradleException("Can't find grgit")) as Grgit
                val version = version.toString()

                if (grgit.branch.current().name != "master") {
                    throw IllegalStateException("Can only release from master.")
                }

                if (!version.contains('-')) {
                    val head = grgit.head()
                    val tagsOnHead = grgit.tag.list().filter { it.commit == head }
                    if (tagsOnHead.find { it.name.startsWith("$version-rc.") } == null) {
                        throw IllegalStateException("Must release an rc of this commit before making a final.")
                    }
                }
            }
        }

        tasks.matching { it.name == "check" }.configureEach {
            tasks.getByName(ReckonPlugin.TAG_TASK).dependsOn(this)
        }
    }
}