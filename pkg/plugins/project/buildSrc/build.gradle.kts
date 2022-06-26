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
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "net.bhojpur.gradle"

repositories {
    mavenLocal()
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("net.bhojpur.gradle:artifact-plugin:1.0.0")
    implementation("net.bhojpur.gradle:reckon-plugin:latest.release")
}

gradlePlugin {
    plugins {
        register("plugin-release-plugin") {
            id = "plugin-release"
            implementationClass = "net.bhojpur.gradle.PluginReleasePlugin"
        }
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    description = "Creates a jar of java sources, classified -sources"
    archiveClassifier.set("sources")
    from(sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].allSource)
}

tasks.named("assemble") {
    dependsOn(sourcesJar)
}