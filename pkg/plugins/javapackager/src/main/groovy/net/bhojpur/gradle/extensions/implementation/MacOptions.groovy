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
import net.bhojpur.gradle.InstallerType
import net.bhojpur.gradle.extensions.InstallOsOptions

@SuppressWarnings("UnstableApiUsage")
@CompileStatic
abstract class MacOptions implements InstallOsOptions {

    final RegularFileProperty icon

    protected final Project project

    protected final String installerType

    MacOptions(InstallerType type, Project project) {
        this.installerType = type
        this.project = project

        this.icon = project.objects.fileProperty()
    }

    void setIcon(File icon) {
        String fileExtension = FilenameUtils.getExtension(icon.getName())
        if (fileExtension != "icns") {
            throw new GradleException("Only .icn extension supported, supplied: $fileExtension")
        }
        this.icon.set(icon)
    }

}