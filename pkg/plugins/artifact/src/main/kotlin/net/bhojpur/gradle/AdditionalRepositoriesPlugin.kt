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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.repositories
import net.bhojpur.gradle.PluginHelper.Companion.createArtifactoryMavenRepo
import net.bhojpur.gradle.PluginHelper.Companion.createGitlabMavenRepo
import net.bhojpur.gradle.PluginHelper.Companion.createStandardMavenRepo
import net.bhojpur.gradle.PluginHelper.Companion.safeAdd
import java.net.URI

class AdditionalRepositoriesPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        repositories {
            safeAdd(createArtifactoryMavenRepo())
            safeAdd(createGitlabMavenRepo())
            safeAdd(createStandardMavenRepo())

            maven {
                name = "bhojpur.maven"
                url = URI.create("https://artifacts.bhojpur.net/artifactory/maven/")
            }
            maven {
                name = "unidata"
                url = URI.create("https://artifacts.unidata.ucar.edu/repository/unidata-all/")
            }
        }
    }
}