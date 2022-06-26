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
import org.gradle.api.provider.Property
import org.gradle.process.CommandLineArgumentProvider

@CompileStatic
class JavaPackagerDeployPkg implements CommandLineArgumentProvider {

    final Property<String> signingKeyDeveloperIdInstaller

    JavaPackagerDeployPkg(Project project) {
        signingKeyDeveloperIdInstaller = project.objects.property(String)
    }

    @Override
    Iterable<String> asArguments() {
        List cmd = []
        if (signingKeyDeveloperIdInstaller.isPresent()) {
            cmd.add("-Bmac.signing-key-developer-id-installer=" + signingKeyDeveloperIdInstaller.get())
        } else {
            cmd.add("-nosign")
        }
        return cmd
    }

}