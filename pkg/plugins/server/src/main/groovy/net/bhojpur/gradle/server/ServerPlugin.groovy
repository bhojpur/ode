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
import ode.dsl.SemanticType
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.TaskProvider
import net.bhojpur.gradle.api.ApiPlugin
import net.bhojpur.gradle.api.extensions.ApiExtension
import net.bhojpur.gradle.api.tasks.SplitTask
import net.bhojpur.gradle.server.extensions.ServerExtension
import net.bhojpur.gradle.server.tasks.ImportResourcesTask
import net.bhojpur.gradle.dsl.DslPlugin
import net.bhojpur.gradle.dsl.extensions.BaseFileConfig
import net.bhojpur.gradle.dsl.extensions.DslExtension
import net.bhojpur.gradle.dsl.extensions.MultiFileConfig
import net.bhojpur.gradle.dsl.tasks.FilesGeneratorTask
import net.bhojpur.gradle.dsl.tasks.GeneratorBaseTask

import javax.inject.Inject
import java.util.concurrent.Callable

import static net.bhojpur.gradle.dsl.FileTypes.PATTERN_DB_TYPE
import static net.bhojpur.gradle.dsl.FileTypes.PATTERN_ODE_XML

@CompileStatic
class ServerPlugin implements Plugin<Project> {

    private static final Logger Log = Logging.getLogger(ServerPlugin)

    public static final String TASK_IMPORT_MAPPINGS = "importMappings"

    public static final String TASK_IMPORT_DATABASE_TYPES = "importDatabaseTypes"

    Map<String, BaseFileConfig> fileGeneratorConfigMap = [:]

    final ObjectFactory objectFactory

    final ProviderFactory providerFactory

    private Project project

    @Inject
    ServerPlugin(ObjectFactory objectFactory, ProviderFactory providerFactory) {
        this.objectFactory = objectFactory
        this.providerFactory = providerFactory
    }

    @Override
    void apply(Project project) {
        this.project = project

        ServerExtension server =
                project.extensions.create("server", ServerExtension, project)

        TaskProvider<ImportResourcesTask> importMappings = registerImportMappings()

        TaskProvider<ImportResourcesTask> importDbTypesTask = registerImportDbTypes()

        project.plugins.withType(DslPlugin) {
            // Get the [ task.name | extension ] map
            fileGeneratorConfigMap =
                    project.properties.get("fileGeneratorConfigMap") as Map<String, BaseFileConfig>

            DslExtension dsl = project.extensions.getByType(DslExtension)

            // Configure extensions of ode.dsl plugin
            dsl.outputDir.set(server.outputDir)
            dsl.odeXmlFiles.from(importMappings)
            dsl.databaseTypes.from(importDbTypesTask)

            // Add generateCombinedFilesTask
            dsl.multiFile.addLater(createGenerateCombinedFilesConfig(server))

            // Set each generator task to depend on
            project.tasks.withType(GeneratorBaseTask).configureEach(new Action<GeneratorBaseTask>() {
                @Override
                void execute(GeneratorBaseTask gbt) {
                    gbt.dependsOn importMappings, importDbTypesTask
                }
            })
        }

        project.plugins.withType(ApiPlugin) {
            TaskProvider<FilesGeneratorTask> generateCombinedTask =
                    getGenerateCombinedTask(project)

            ApiExtension api = project.extensions.getByType(ApiExtension)
            api.outputDir.set(server.outputDir)
            api.combinedFiles.from(generateCombinedTask)

            project.tasks.withType(SplitTask).configureEach(new Action<SplitTask>() {
                @Override
                void execute(SplitTask st) {
                    st.dependsOn generateCombinedTask
                }
            })
        }
    }

    Provider<MultiFileConfig> createGenerateCombinedFilesConfig(ServerExtension server) {
        providerFactory.provider(new Callable<MultiFileConfig>() {
            @Override
            MultiFileConfig call() throws Exception {
                def extension = new MultiFileConfig("combined", project)
                extension.template = "combined.vm"
                extension.outputDir = "${project.buildDir}/${server.database.get()}/combined"
                extension.formatOutput = { SemanticType st -> "${st.getShortname()}I.combined" }
                return extension
            }
        })
    }

    TaskProvider<FilesGeneratorTask> getGenerateCombinedTask(Project project) {
        def combinedFilesExt = fileGeneratorConfigMap.find {
            it.key.toLowerCase().contains("combined")
        }
        project.tasks.named(combinedFilesExt.key, FilesGeneratorTask)
    }

    TaskProvider<ImportResourcesTask> registerImportMappings() {
        project.tasks.register(TASK_IMPORT_MAPPINGS, ImportResourcesTask, new Action<ImportResourcesTask>() {
            @Override
            void execute(ImportResourcesTask t) {
                t.with {
                    config = ImportHelper.getConfigurationForOdeModel(project)
                    extractDir = "$project.buildDir/mappings"
                    pattern = PATTERN_ODE_XML
                }
            }
        })
    }

    TaskProvider<ImportResourcesTask> registerImportDbTypes() {
        project.tasks.register(TASK_IMPORT_DATABASE_TYPES, ImportResourcesTask, new Action<ImportResourcesTask>() {
            @Override
            void execute(ImportResourcesTask t) {
                t.with {
                    config = ImportHelper.getConfigurationForOdeModel(project)
                    extractDir = "$project.buildDir/databaseTypes"
                    pattern = PATTERN_DB_TYPE
                }
            }
        })
    }

}