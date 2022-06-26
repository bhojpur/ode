package net.bhojpur.gradle.tasks

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
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.process.CommandLineArgumentProvider

@SuppressWarnings("UnstableApiUsage")
@CompileStatic
class JavaPackagerDeployWin implements CommandLineArgumentProvider {

    @Input
    @Optional
    final Property<Boolean> installDirChooser

    @Input
    @Optional
    final Property<Boolean> installUserLevel

    @Input
    @Optional
    final Property<Boolean> addShortcut

    @Input
    @Optional
    final Property<String> copyright

    @Input
    @Optional
    final Property<String> registryVendor

    @Input
    @Optional
    final Property<String> startMenuGroup

    @InputFile
    @Optional
    final RegularFileProperty icon

    JavaPackagerDeployWin(Project project) {
        this.installDirChooser = project.objects.property(Boolean)
        this.installUserLevel = project.objects.property(Boolean)
        this.addShortcut = project.objects.property(Boolean)
        this.copyright = project.objects.property(String)
        this.registryVendor = project.objects.property(String)
        this.startMenuGroup = project.objects.property(String)
        this.icon = project.objects.fileProperty()
    }

    @Override
    Iterable<String> asArguments() {
        List<String> cmd = []

        if (installDirChooser.isPresent()) {
            cmd.add("-BinstalldirChooser=" + installDirChooser.get())
        }

        if (addShortcut.isPresent()) {
            cmd.add("-BshortcutHint=" + addShortcut.get())
        }

        if (copyright.isPresent()) {
            cmd.add("-Bcopyright=" + copyright.get())
        }

        if (startMenuGroup.isPresent()) {
            cmd.add("-Bwin.menuGroup=" + startMenuGroup.get())
        }

        if (registryVendor.isPresent()) {
            cmd.add("-Bvendor=" + registryVendor.get())
        }

        return cmd
    }

}