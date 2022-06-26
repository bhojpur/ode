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

import org.ajoberstar.gradle.git.base.GrgitPlugin
import org.ajoberstar.gradle.git.release.opinion.OpinionReleasePlugin
import org.ajoberstar.gradle.git.release.base.ReleasePluginExtension
import org.ajoberstar.gradle.git.release.base.ReleaseVersion
import org.ajoberstar.gradle.git.release.base.TagStrategy
import org.ajoberstar.gradle.git.release.opinion.Strategies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.delegateClosureOf

@Deprecated(message="Deprecated since 1.0.0")
class ReleasePlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        applyGrgitPlugin()
        configureReleasePluginExtension()
    }

    private
    fun Project.applyGrgitPlugin() {
        apply<OpinionReleasePlugin>()
        apply<GrgitPlugin>()
    }

    private
    fun Project.configureReleasePluginExtension() {
        configure<ReleasePluginExtension> {
            versionStrategy(Strategies.getFINAL())
            defaultVersionStrategy = Strategies.getSNAPSHOT()
            tagStrategy(delegateClosureOf<TagStrategy> {
                generateMessage = closureOf<ReleaseVersion> {
                    "Version ${project.version}"
                }
            })
        }
    }
}