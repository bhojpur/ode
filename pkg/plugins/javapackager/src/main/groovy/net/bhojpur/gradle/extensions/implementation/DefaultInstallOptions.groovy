package net.bhojpur.gradle.extensions.implementation

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
import org.apache.commons.io.FilenameUtils
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.process.CommandLineArgumentProvider
import net.bhojpur.gradle.Platform
import net.bhojpur.gradle.extensions.InstallOsOptions
import net.bhojpur.gradle.extensions.InstallOptions
import net.bhojpur.gradle.tasks.JavaPackagerDeploy

@SuppressWarnings("UnstableApiUsage")
@CompileStatic
class DefaultInstallOptions implements InstallOptions {

    final Property<String> description

    final Property<String> applicationName

    final Property<String> applicationVersion

    final Property<String> mainClassName

    final Property<String> mainJar

    final ListProperty<String> outputTypes

    final ListProperty<String> arguments

    final ListProperty<String> javaOptions

    final RegularFileProperty icon

    final RegularFileProperty licenseFile

    final RegularFileProperty outputFile

    final DirectoryProperty sourceDir

    final ConfigurableFileCollection sourceFiles

    private final String name

    private final Project project

    DefaultInstallOptions(String name, Project project) {
        this.name = name
        this.project = project
        this.description = project.objects.property(String)
        this.applicationName = project.objects.property(String)
        this.applicationVersion = project.objects.property(String)
        this.mainClassName = project.objects.property(String)
        this.mainJar = project.objects.property(String)
        this.outputTypes = project.objects.listProperty(String)
        this.arguments = project.objects.listProperty(String)
        this.javaOptions = project.objects.listProperty(String)
        this.icon = project.objects.fileProperty()
        this.licenseFile = project.objects.fileProperty()
        this.outputFile = project.objects.fileProperty()
        this.sourceDir = project.objects.directoryProperty()
        this.sourceFiles = project.files()
    }

    Iterable<CommandLineArgumentProvider> createCmdArgProviders(String outputType) {
        List<CommandLineArgumentProvider> cmdArgProviders = []

        InstallOsOptions osOptions = extensionContainer.getByName(outputType) as InstallOsOptions
        osOptions.icon.convention(icon)

        JavaPackagerDeploy deployCmdProps = new JavaPackagerDeploy(project)
        deployCmdProps.nativeType.set(outputType)
        deployCmdProps.icon.set(osOptions.icon)
        deployCmdProps.mainClass.set(mainClassName)
        deployCmdProps.mainJar.set(mainJar)
        deployCmdProps.arguments.set(arguments)
        deployCmdProps.jvmOptions.set(javaOptions)
        deployCmdProps.applicationName.set(applicationName)
        deployCmdProps.applicationVersion.set(applicationVersion)
        deployCmdProps.srcDir.set(sourceDir)
        deployCmdProps.srcFiles.from(sourceFiles)
        deployCmdProps.outputDir.set(getOutputDir())
        deployCmdProps.outputFileName.set(getOutputFileName())

        // Add general command line arguments
        cmdArgProviders.add(osOptions.createCmdArgsProvider())
        cmdArgProviders.add(deployCmdProps)
        cmdArgProviders
    }

    @Override
    String getName() {
        return name
    }

    @Override
    void setApplicationDescription(String description) {
        this.description.set(description)
    }

    @Override
    void setApplicationName(String name) {
        this.applicationName.set(name)
    }

    @Override
    void setApplicationName(Provider<? extends String> name) {
        this.applicationName.set(name)
    }

    @Override
    void setApplicationVersion(String version) {
        this.applicationVersion.set(version)
    }

    @Override
    void setApplicationVersion(Provider<? extends String> version) {
        this.applicationVersion.set(version)
    }

    @Override
    void setMainClassName(String mainClass) {
        this.mainClassName.set(mainClass)
    }

    @Override
    void setMainClassName(Provider<? extends String> mainClass) {
        this.mainClassName.set(mainClass)
    }

    @Override
    void setMainJar(String name) {
        this.mainJar.set(name)
    }

    @Override
    void setMainJar(Provider<? extends String> name) {
        this.mainJar.set(name)
    }

    @Override
    void setArguments(Iterable<? extends String> args) {
        this.arguments.set(args)
    }

    @Override
    void setJavaOptions(Iterable<? extends String> options) {
        this.javaOptions.set(options)
    }

    @Override
    void setIcon(RegularFileProperty icon) {
        this.icon.set(icon.flatMap {
            RegularFileProperty normalisedIcon = project.objects.fileProperty()
            normalisedIcon.set(icon)
            normalisedIcon
        })
    }

    @Override
    void setIcon(File icon) {
        this.icon.set(normaliseIcon(icon))
    }

    @Override
    void setIcon(String icon) {
        this.setIcon(new File(icon))
    }

    @Override
    void setOutputFile(RegularFile file) {
        this.outputFile.set(file)
    }

    @Override
    void setOutputFile(File file) {
        this.outputFile.set(file)
    }

    @Override
    void setOutputFile(String file) {
        this.setOutputFile(project.file(file))
    }

    @Override
    void setSourceDir(File dir) {
        this.sourceDir.set(dir)
    }

    @Override
    void setSourceDir(Directory dir) {
        this.sourceDir.set(dir)
    }

    @Override
    void setSourceFiles(Object... files) {
        this.sourceFiles.setFrom(files)
    }

    @Override
    void setSourceFiles(Iterable<?> files) {
        this.sourceFiles.setFrom(files)
    }

    @Override
    void exe(Action<? super WinOptions> action) {
        executeOsOptionsAction("exe", action)
    }

    @Override
    void msi(Action<? super WinOptions> action) {
        executeOsOptionsAction("msi", action)
    }

    @Override
    void dmg(Action<? super MacOptions> action) {
        executeOsOptionsAction("dmg", action)
    }

    @Override
    void pkg(Action<? super MacOptions> action) {
        executeOsOptionsAction("pkg", action)
    }

    Provider<Directory> getOutputDir() {
        this.outputFile.flatMap { RegularFile regularFile ->
            DirectoryProperty property = project.objects.directoryProperty()
            property.set(regularFile.asFile.getParentFile())
            property
        }
    }

    Provider<String> getOutputFileName() {
        this.outputFile.map { RegularFile regularFile ->
            regularFile.asFile.name
        }
    }

    private File normaliseIcon(File icon) {
        return new File(icon.parent, FilenameUtils.getBaseName(icon.name) + "." + Platform.iconExtension)
    }

    private <T> void executeOsOptionsAction(String extensionName, Action<T> action) {
        if (!(this instanceof ExtensionAware)) {
            throw new GradleException("This class instance is not extensions aware")
        }

        T options = extensionContainer.findByName(extensionName) as T
        if (!options) {
            addExtension(extensionName)

            options = extensionContainer.findByName(extensionName) as T
            if (!options) {
                throw new GradleException("Unable to add extension '${extensionName}'")
            }
        }
        action.execute(options)
    }

    void addExtension(String type) {
        if (!(this instanceof ExtensionAware)) {
            throw new GradleException("This class instance is not extensions aware")
        }

        def extensions = extensionContainer
        switch (type) {
            case "exe":
                extensions.create(type, ExeInstallOptions, project)
                break
            case "msi":
                extensions.create(type, MsiInstallOptions, project)
                break
            case "dmg":
                extensions.create(type, DmgInstallOptions, project)
                break
            case "pkg":
                extensions.create(type, PkgInstallOptions, project)
                break
        }
    }

}