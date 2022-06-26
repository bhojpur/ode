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

import groovy.lang.GroovyObject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.*
import org.gradle.plugin.devel.plugins.JavaGradlePluginPlugin
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import net.bhojpur.gradle.PluginHelper.Companion.createArtifactoryMavenRepo
import net.bhojpur.gradle.PluginHelper.Companion.createGitlabMavenRepo
import net.bhojpur.gradle.PluginHelper.Companion.createStandardMavenRepo
import net.bhojpur.gradle.PluginHelper.Companion.licenseGnu2
import net.bhojpur.gradle.PluginHelper.Companion.resolveProperty
import net.bhojpur.gradle.PluginHelper.Companion.safeAdd

class PluginPublishingPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        applyJavaGradlePlugin()
        configurePluginMaven()
        configureArtifactoryExtension()
    }

    private
    fun Project.applyJavaGradlePlugin() {
        apply<MavenPublishPlugin>()
        apply<ArtifactoryPlugin>()
        apply<JavaGradlePluginPlugin>()
    }

    private
    fun Project.configurePluginMaven() {
        afterEvaluate {
            configure<PublishingExtension> {
                repositories {
                    safeAdd(createArtifactoryMavenRepo())
                    safeAdd(createGitlabMavenRepo())
                    safeAdd(createStandardMavenRepo())
                }

                // pluginMaven is task created by MavenPluginPublishPlugin
                publications.getByName<MavenPublication>("pluginMaven") {
                    artifact(tasks.getByName("sourcesJar"))
                    artifact(tasks.getByName("javadocJar"))

                    plugins.withType<GroovyPlugin> {
                        artifact(tasks.getByName("groovydocJar"))
                    }

                    pom {
                        licenseGnu2()
                    }
                }
            }
        }
    }

    private
    fun Project.configureArtifactoryExtension() {
        plugins.withType<ArtifactoryPlugin> {
            configure<ArtifactoryPluginConvention> {
                publish(delegateClosureOf<PublisherConfig> {
                    setContextUrl(resolveProperty("ARTIFACTORY_URL", "artifactoryUrl"))
                    repository(delegateClosureOf<GroovyObject> {
                        setProperty("repoKey", resolveProperty("ARTIFACTORY_REPOKEY", "artifactoryRepokey"))
                        setProperty("username", resolveProperty("ARTIFACTORY_USER", "artifactoryUser"))
                        setProperty("password", resolveProperty("ARTIFACTORY_PASSWORD", "artifactoryPassword"))
                    })
                })
            }
        }
    }
}