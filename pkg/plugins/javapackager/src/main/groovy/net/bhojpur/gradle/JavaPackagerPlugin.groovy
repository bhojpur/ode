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

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ProjectLayout
import org.gradle.api.internal.CollectionCallbackActionDecorator
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Sync
import org.gradle.internal.reflect.Instantiator
import org.gradle.jvm.tasks.Jar
import net.bhojpur.gradle.extensions.InstallOptions
import net.bhojpur.gradle.extensions.InstallOptionsContainer
import net.bhojpur.gradle.extensions.implementation.DefaultInstallOptions
import net.bhojpur.gradle.extensions.implementation.DefaultInstallOptionsContainer

import javax.inject.Inject

@CompileStatic
class JavaPackagerPlugin implements Plugin<Project> {

    public static final String MAIN_DEPLOY_NAME = "main"

    public static final String PACKAGE_APPLICATION_TASK_NAME = "packageApplication"

    public static final String DISTRIBUTION_GROUP = "distribution"

    private Project project

    private final ProjectLayout layout
    private final Instantiator instantiator
    private final CollectionCallbackActionDecorator callbackActionDecorator

    @Inject
    JavaPackagerPlugin(ProjectLayout layout, Instantiator instantiator, CollectionCallbackActionDecorator callbackActionDecorator) {
        this.layout = layout
        this.instantiator = instantiator
        this.callbackActionDecorator = callbackActionDecorator
    }

    @Override
    void apply(Project project) {
        this.project = project

        project.pluginManager.apply(ApplicationPlugin)

        InstallOptionsContainer deployContainer =
                project.extensions.create(InstallOptionsContainer, "deploy", DefaultInstallOptionsContainer,
                        InstallOptions, instantiator, callbackActionDecorator, project)

        deployContainer.configureEach { InstallOptions options ->
            if (options instanceof DefaultInstallOptions) {
                createJavaPackagerTask(options)
            }

            if (options.name == MAIN_DEPLOY_NAME) {
                configureMain(options)
            }
        }

        deployContainer.create(MAIN_DEPLOY_NAME)
    }

    void configureMain(InstallOptions deploy) {
        project.afterEvaluate {
            JavaApplication javaApplication = project.extensions.getByType(JavaApplication)
            deploy.mainClassName.convention(javaApplication.mainClassName)
            deploy.javaOptions.convention(javaApplication.applicationDefaultJvmArgs)

            // The mainJar is the archive created by the 'jar' task
            Jar jar = project.tasks.getByName(JavaPlugin.JAR_TASK_NAME) as Jar
            deploy.mainJar.convention(jar.archiveFileName)
            deploy.applicationVersion.convention(jar.archiveVersion)

            // Use the files from the 'installDist' task
            Sync installDistTask = project.tasks.getByName("installDist") as Sync
            deploy.applicationName.convention(installDistTask.destinationDir.name)
            deploy.outputFile.convention(layout.buildDirectory.file("packaged/${deploy.name}/${installDistTask.destinationDir.name}"))
            deploy.sourceDir.convention(layout.projectDirectory.dir(installDistTask.destinationDir.toString()))
            deploy.sourceFiles.from(project.fileTree(installDistTask.destinationDir).include("**/*.*"))
        }
    }

    void createJavaPackagerTask(DefaultInstallOptions deploy) {
        // Wait for evaluation as we require the values of outputTypes
        List<String> outputTypes = Platform.installerTypesAsString

        outputTypes.each { String outputType ->
            String taskName = makeTaskName(deploy.name, outputType)

            project.tasks.register(taskName, Exec, new Action<Exec>() {
                @Override
                void execute(Exec jp) {
                    if (deploy.name == MAIN_DEPLOY_NAME) {
                        jp.dependsOn(project.tasks.named("installDist"))
                    }

                    jp.setGroup(DISTRIBUTION_GROUP)
                    jp.setDescription("Creates a $outputType bundle")
                    jp.commandLine("javapackager", "-deploy")
                    jp.argumentProviders.addAll(deploy.createCmdArgProviders(outputType))
                }
            })
        }
    }

    /**
     * Create a task name e.g. 'packageImporterApplicationDmg' or 'packageImporterApplicationExe'
     *
     * Default name when using 'main' as an install option is 'packageApplication(DmgInstallOptions, Exe, etc.)'
     * @param configName deploy configuration name
     * @param outputType possible output types [exe, msi, dmg, pkg]
     * @return
     */
    static String makeTaskName(String configName, String outputType) {
        String taskName = PACKAGE_APPLICATION_TASK_NAME + outputType.capitalize()
        if (configName != MAIN_DEPLOY_NAME) {
            taskName = "package" + configName.capitalize() + "Application" + outputType.capitalize()
        }
        return taskName
    }

}