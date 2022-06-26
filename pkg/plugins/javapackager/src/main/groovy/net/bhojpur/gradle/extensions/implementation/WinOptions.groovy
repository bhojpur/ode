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
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.process.CommandLineArgumentProvider
import net.bhojpur.gradle.InstallerType
import net.bhojpur.gradle.extensions.InstallOsOptions
import net.bhojpur.gradle.tasks.JavaPackagerDeployWin

@SuppressWarnings("UnstableApiUsage")
@CompileStatic
abstract class WinOptions implements InstallOsOptions {

    final Property<Boolean> installDirChooser

    final Property<Boolean> installUserLevel

    final Property<Boolean> addShortcut

    final Property<String> copyright

    final Property<String> registryVendor

    final Property<String> startMenuGroup

    final RegularFileProperty icon

    private final Project project

    private final InstallerType type

    WinOptions(InstallerType type, Project project) {
        this.type = type
        this.project = project
        this.installDirChooser = project.objects.property(Boolean)
        this.installUserLevel = project.objects.property(Boolean)
        this.addShortcut = project.objects.property(Boolean)
        this.copyright = project.objects.property(String)
        this.registryVendor = project.objects.property(String)
        this.startMenuGroup = project.objects.property(String)
        this.icon = project.objects.fileProperty()

        this.addShortcut.convention(true)
        this.installUserLevel.convention(true)
        this.installDirChooser.convention(true)
    }

    @Override
    CommandLineArgumentProvider createCmdArgsProvider() {
        def provider = new JavaPackagerDeployWin(project)
        provider.installDirChooser.set(this.installDirChooser)
        provider.installUserLevel.set(this.installUserLevel)
        provider.addShortcut.set(this.addShortcut)
        provider.copyright.set(this.copyright)
        provider.registryVendor.set(this.registryVendor)
        provider.startMenuGroup.set(this.startMenuGroup)
        return provider
    }

    void setInstallDirChooser(boolean installDirChooser) {
        this.installDirChooser.set(installDirChooser)
    }

    void setInstallUserLevel(boolean installUserLevel) {
        this.installUserLevel.set(installUserLevel)
    }

    void setAddShortcut(boolean addShortcut) {
        this.addShortcut.set(addShortcut)
    }

    void setCopyright(String copyright) {
        this.copyright.set(copyright)
    }

    void setRegistryVendor(String registryVendor) {
        this.registryVendor.set(registryVendor)
    }

    void setStartMenuGroup(String startMenuGroup) {
        this.startMenuGroup.set(startMenuGroup)
    }

    void setIcon(File icon) {
        String fileExtension = FilenameUtils.getExtension(icon.getName())
        if (fileExtension != "ico") {
            throw new GradleException("Only .ico extension supported, supplied: $fileExtension")
        }
        this.icon.set(icon)
    }

}
