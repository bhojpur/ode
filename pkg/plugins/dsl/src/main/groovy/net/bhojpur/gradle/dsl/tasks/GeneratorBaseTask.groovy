package net.bhojpur.gradle.dsl.tasks

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
import ode.dsl.velocity.Generator
import org.apache.velocity.app.VelocityEngine
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.util.PatternFilterable
import org.gradle.api.tasks.util.PatternSet
import org.gradle.internal.Factory
import net.bhojpur.gradle.dsl.FileTypes

import javax.inject.Inject

@SuppressWarnings("UnstableApiUsage")
@CompileStatic
abstract class GeneratorBaseTask extends DefaultTask {

    private static final Logger Log = Logging.getLogger(GeneratorBaseTask)

    private final RegularFileProperty template = project.objects.fileProperty()

    private final RegularFileProperty databaseType = project.objects.fileProperty()

    private final MapProperty velocityConfig = project.objects.mapProperty(String.class, String.class)

    private final ConfigurableFileCollection mappingFiles = project.files()

    private final PatternFilterable odeXmlPatternSet

    GeneratorBaseTask() {
        odeXmlPatternSet = getPatternSetFactory().create()
                .include(FileTypes.PATTERN_ODE_XML)
    }

    @Inject
    protected Factory<PatternSet> getPatternSetFactory() {
        throw new UnsupportedOperationException()
    }

    @TaskAction
    void apply() {
        // Create velocity engine with config
        Properties p = new Properties()
        p.putAll(velocityConfig.get())
        VelocityEngine ve = new VelocityEngine(p)

        // Build our file generator
        def builder = createGenerator()
        builder.velocityEngine = ve
        builder.template = template.get().asFile
        builder.odeXmlFiles = getOdeXmlFiles()
        builder.databaseTypes = getDatabaseTypes().get()
        builder.profile = getProfile().get()
        builder.build().call()
    }

    abstract protected Generator.Builder createGenerator()

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    Set<File> getOdeXmlFiles() {
        FileTree src = this.mappingFiles.asFileTree
        return src.matching(odeXmlPatternSet).files
    }

    @Internal
    Provider<Properties> getDatabaseTypes() {
        databaseType.map { RegularFile file ->
            Properties databaseTypeProps = new Properties()
            file.asFile.withInputStream { databaseTypeProps.load(it) }
            databaseTypeProps
        }
    }

    @Internal
    Provider<String> getProfile() {
        // Determine database type
        databaseType.map { RegularFile file ->
            String fileName = file.asFile.name
            fileName.substring(0, fileName.lastIndexOf("-"))
        }
    }

    @InputFile
    RegularFileProperty getTemplate() {
        return template
    }

    @InputFile
    RegularFileProperty getDatabaseType() {
        return databaseType
    }

    @Input
    @Optional
    MapProperty getVelocityConfig() {
        return velocityConfig
    }

    @Internal
    ConfigurableFileCollection getMappingFiles() {
        return mappingFiles
    }

}