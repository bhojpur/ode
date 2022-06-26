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
import groovy.util.Node
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.XmlProvider
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import net.bhojpur.gradle.PluginHelper.Companion.camelCaseName
import net.bhojpur.gradle.PluginHelper.Companion.createArtifactoryMavenRepo
import net.bhojpur.gradle.PluginHelper.Companion.createGitlabMavenRepo
import net.bhojpur.gradle.PluginHelper.Companion.createStandardMavenRepo
import net.bhojpur.gradle.PluginHelper.Companion.getRuntimeClasspathConfiguration
import net.bhojpur.gradle.PluginHelper.Companion.licenseGnu2
import net.bhojpur.gradle.PluginHelper.Companion.resolveProperty
import net.bhojpur.gradle.PluginHelper.Companion.safeAdd
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.set

class PublishingPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        applyPublishingPlugin()
        configureManifest()
        configurePublishingExtension()
        configureArtifactoryExtension()
    }

    private
    fun Project.applyPublishingPlugin() {
        apply<MavenPublishPlugin>()
        apply<ArtifactoryPlugin>()
    }
    
    private
    fun Project.configureManifest() {
        plugins.withType<JavaPlugin> {
            tasks.named<Jar>("jar") {
                manifest {
                    attributes["Implementation-Title"] = name.replace(Regex("[^A-Za-z0-9]"), "")
                    attributes["Implementation-Version"] = project.version
                    attributes["Built-By"] = System.getProperty("user.name")
                    attributes["Built-Date"] = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    attributes["Built-JDK"] = System.getProperty("java.version")
                    attributes["Built-Gradle"] = gradle.gradleVersion
                    attributes["Class-Path"] = getRuntimeClasspathConfiguration(project)
                            ?.joinToString(separator = " ") { it.name }
                }
            }
        }
    }

    private
    fun Project.configurePublishingExtension() {
        configure<PublishingExtension> {
            repositories {
                safeAdd(createArtifactoryMavenRepo())
                safeAdd(createGitlabMavenRepo())
                safeAdd(createStandardMavenRepo())
            }

            publications {
                create<MavenPublication>(camelCaseName()) {
                    plugins.withType<JavaPlugin> {
                        from(components["java"])
                        artifact(tasks.getByName("sourcesJar"))
                        artifact(tasks.getByName("javadocJar"))
                    }
                    plugins.withType<GroovyPlugin> {
                        artifact(tasks.getByName("groovydocJar"))
                    }
                    pom(standardPom())
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


    private
    fun Project.standardPom(): Action<in MavenPom>? {
        return Action {
            licenseGnu2()
        }
    }
}