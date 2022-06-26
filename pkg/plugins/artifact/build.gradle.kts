/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

plugins {
    `maven-publish`
    `kotlin-dsl`
    `java-gradle-plugin`
    // net.bhojpur.gradle.`plugin-project`
}

group = "net.bhojpur.gradle"
version = "1.0.0"

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("org.jfrog.buildinfo:build-info-extractor-gradle:4.9.3")
    implementation("org.ajoberstar:grgit:1.9.1") {
        setForce(true)
    }
    implementation("org.ajoberstar:gradle-git:1.7.1")
    implementation("org.ajoberstar:gradle-git-publish:0.3.3")
}

gradlePlugin {
    plugins {
        register("project-plugin") {
            id = "net.bhojpur.gradle.project"
            implementationClass = "net.bhojpur.gradle.ProjectPlugin"
        }
        register("additional-repositories-plugin") {
            id = "net.bhojpur.gradle.additional-repositories"
            implementationClass = "net.bhojpur.gradle.AdditionalRepositoriesPlugin"
        }
        register("additional-artifacts-plugin") {
            id = "net.bhojpur.gradle.additional-artifacts"
            implementationClass = "net.bhojpur.gradle.AdditionalArtifactsPlugin"
        }
        register("publishing-plugin") {
            id = "net.bhojpur.gradle.publishing"
            implementationClass = "net.bhojpur.gradle.PublishingPlugin"
        }
        register("plugin-project-plugin") {
            id = "net.bhojpur.gradle.plugin-project"
            implementationClass = "net.bhojpur.gradle.PluginProjectPlugin"
        }
        register("plugin-publishing-plugin") {
            id = "net.bhojpur.gradle.plugin-publishing"
            implementationClass = "net.bhojpur.gradle.PluginPublishingPlugin"
        }
        register("release-plugin") {
            id = "net.bhojpur.gradle.release"
            implementationClass = "net.bhojpur.gradle.ReleasePlugin"
        }
    }
}