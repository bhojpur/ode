package net.bhojpur.gradle.dsl

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
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.TaskProvider
import net.bhojpur.gradle.dsl.extensions.BaseFileConfig
import net.bhojpur.gradle.dsl.extensions.DslExtension
import net.bhojpur.gradle.dsl.extensions.MultiFileConfig
import net.bhojpur.gradle.dsl.extensions.SingleFileConfig
import net.bhojpur.gradle.dsl.factories.MultiFileGeneratorFactory
import net.bhojpur.gradle.dsl.factories.SingleFileGeneratorFactory
import net.bhojpur.gradle.dsl.tasks.FileGeneratorTask
import net.bhojpur.gradle.dsl.tasks.FilesGeneratorTask

import javax.inject.Inject

@SuppressWarnings("UnstableApiUsage")
@CompileStatic
class DslPluginBase extends DslBase implements Plugin<Project> {

    public static final String GROUP = "ode-dsl"

    public static final String EXTENSION_NAME_DSL = "dsl"

    public static final String TASK_PREFIX_GENERATE = "generate"

    final Map<String, BaseFileConfig> fileGeneratorConfigMap = [:]

    private final ObjectFactory objectFactory

    private final ProviderFactory providerFactory

    private static final Logger Log = Logging.getLogger(DslPluginBase)

    @Inject
    DslPluginBase(ObjectFactory objectFactory, ProviderFactory providerFactory) {
        this.objectFactory = objectFactory
        this.providerFactory = providerFactory
    }

    @Override
    void apply(Project project) {
        def dsl = createDslExtension(project)

        // Add the map to extra properties
        // Access via project.fileGeneratorConfigMap
        project.extensions.extraProperties
                .set("fileGeneratorConfigMap", fileGeneratorConfigMap)

        dsl.multiFile.whenObjectAdded { MultiFileConfig mfg ->
            def task = addMultiFileGenTask(project, dsl, mfg)
            fileGeneratorConfigMap.put(task.name, mfg)
        }

        dsl.singleFile.whenObjectAdded { SingleFileConfig sfg ->
            def task = addSingleFileGenTask(project, dsl, sfg)
            fileGeneratorConfigMap.put(task.name, sfg)
        }
    }

    DslExtension createDslExtension(Project project) {
        def multiFileContainer =
                project.container(MultiFileConfig, new MultiFileGeneratorFactory(project))
        def singleFileContainer =
                project.container(SingleFileConfig, new SingleFileGeneratorFactory(project))

        project.extensions.create(EXTENSION_NAME_DSL, DslExtension, project,
                multiFileContainer, singleFileContainer)
    }

    TaskProvider<FilesGeneratorTask> addMultiFileGenTask(Project project, DslExtension dsl, MultiFileConfig ext) {
        String taskName = TASK_PREFIX_GENERATE + ext.name.capitalize() + dsl.database.get().capitalize()

        project.tasks.register(taskName, FilesGeneratorTask, new Action<FilesGeneratorTask>() {
            @Override
            void execute(FilesGeneratorTask t) {
                t.with {
                    group = GROUP
                    formatOutput.set(ext.formatOutput)
                    velocityConfig.set(dsl.velocity.getProperties())
                    outputDir.set(getOutputDirProvider(dsl.outputDir, ext.outputDir))
                    template.set(findTemplateProvider(dsl.templates, ext.template))
                    databaseType.set(findDatabaseTypeProvider(dsl.databaseTypes, dsl.database))
                    mappingFiles.from(dsl.odeXmlFiles + ext.odeXmlFiles)
                }
            }
        })
    }

    TaskProvider<FileGeneratorTask> addSingleFileGenTask(Project project, DslExtension dsl, SingleFileConfig ext) {
        String taskName = TASK_PREFIX_GENERATE + ext.name.capitalize() + dsl.database.get().capitalize()

        project.tasks.register(taskName, FileGeneratorTask, new Action<FileGeneratorTask>() {
            @Override
            void execute(FileGeneratorTask t) {
                t.with {
                    group = GROUP
                    velocityConfig.set(dsl.velocity.getProperties())
                    outputFile.set(getOutputFileProvider(dsl.outputDir, ext.outputFile))
                    template.set(findTemplateProvider(dsl.templates, ext.template))
                    databaseType.set(findDatabaseTypeProvider(dsl.databaseTypes, dsl.database))
                    mappingFiles.from(dsl.odeXmlFiles + ext.odeXmlFiles)
                }
            }
        })
    }

    Provider<Directory> getOutputDirProvider(DirectoryProperty baseDir, Property<File> childDir) {
        childDir.flatMap { File f -> baseDir.dir(f.toString()) }
    }

    Provider<RegularFile> getOutputFileProvider(DirectoryProperty baseDir, Property<File> childFile) {
        childFile.flatMap { File f -> baseDir.file(f.toString()) }
    }

    Provider<RegularFile> findDatabaseTypeProvider(FileCollection collection, Property<String> type) {
        type.map { String t ->
            RegularFileProperty result = objectFactory.fileProperty()
            result.set(findDatabaseType(collection, type.get()))
            result.get()
        }
    }

    Provider<RegularFile> findTemplateProvider(FileCollection collection, Property<File> file) {
        file.map { File f ->
            RegularFileProperty result = objectFactory.fileProperty()
            result.set(findTemplate(collection, f))
            result.get()
        }
    }

}