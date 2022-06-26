package net.bhojpur.gradle.server

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
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.ResolvableDependencies
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.JavaPlugin

import static net.bhojpur.gradle.server.ConventionPluginHelper.getRuntimeClasspathConfiguration

@CompileStatic
class ImportHelper {

    public static final String CONFIGURATION_NAME = "dataFiles"

    private static final Logger Log = Logging.getLogger(ImportHelper)

    static File findOdeModel(Configuration config) {
        return findOdeModel(config.incoming)
    }

    static File findOdeModel(ResolvableDependencies dependencies) {
        File file = dependencies.artifacts.artifactFiles.files.find {
            it.name.contains("ode-model")
        }
        if (file == null) {
            throw new GradleException("Unable to find ode-model artifact")
        }
        Log.info("Found ode-model: ${file.toString()}")
        return file
    }

    static Configuration getConfigurationForOdeModel(Project project) {
        def javaPlugin = project.plugins.withType(JavaPlugin)
        javaPlugin ? getRuntimeClasspathConfiguration(project) : getDataFilesConfig(project)
    }

    static Configuration getDataFilesConfig(Project project) {
        Configuration config = project.configurations.findByName(CONFIGURATION_NAME)
        if (!config) {
            config = createDataFilesConfig(project)
        }
        return config
    }

    static Configuration createDataFilesConfig(Project project) {
        project.buildscript.repositories.addAll(
                project.repositories.mavenLocal(),
                project.repositories.mavenCentral()
        )

        Configuration config = project.configurations.create(CONFIGURATION_NAME)
                .setVisible(false)
                .setTransitive(false)
                .setDescription("The data artifacts to be processed for this plugin.")

        config.defaultDependencies(new Action<DependencySet>() {
            void execute(DependencySet dependencies) {
                dependencies.add(project.dependencies.create("net.bhojpur.ode:ode-model:latest"))
            }
        })

        return config
    }
}