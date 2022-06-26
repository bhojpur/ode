package net.bhojpur.gradle.extensions

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
import org.gradle.api.Named
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import net.bhojpur.gradle.extensions.implementation.MacOptions
import net.bhojpur.gradle.extensions.implementation.WinOptions

/**
 * Valid installer types are: {"exe", "msi", "rpm", "deb", "pkg", "dmg"}.
 */
@CompileStatic
interface InstallOptions extends Named, Extensible {

    /**
     * Description of the application
     * @return
     */
    Property<String> getDescription()

    /**
     *
     * @param description
     */
    void setApplicationDescription(String description)

    /**
     * Name of the application and/or installer
     * @return
     */
    Property<String> getApplicationName()

    /**
     *
     * @param name
     */
    void setApplicationName(String name)

    /**
     *
     * @param name
     */
    void setApplicationName(Provider<? extends String> name)

    /**
     * Sets the version string on the installer file name
     * @return
     */
    Property<String> getApplicationVersion()

    /**
     *
     * @param version
     */
    void setApplicationVersion(String version)

    /**
     *
     * @param version
     */
    void setApplicationVersion(Provider<? extends String> version)

    /**
     * Qualified name of the application main class to execute.
     * @return
     */
    Property<String> getMainClassName()

    /**
     *
     * @param mainClass
     */
    void setMainClassName(String mainClass)

    /**
     *
     * @param mainClass
     */
    void setMainClassName(Provider<? extends String> mainClass)

    /**
     * The name of the main JAR of the application.
     * containing the main class (specified as a path relative to {@code getSourceDir).
     * @return
     */
    Property<String> getMainJar()

    /**
     *
     * @param name
     */
    void setMainJar(String name)

    /**
     *
     * @param name
     */
    void setMainJar(Provider<? extends String> name)

    /**
     * List of types of the installer to create.
     * @return
     */
    Provider<List<String>> getOutputTypes()

    /**
     * Command line arguments to pass to the main class if no command line arguments are given to the launcher
     * @return
     */
    ListProperty<String> getArguments()

    /**
     * Set unnamed java command line arguments
     * @param args
     */
    void setArguments(Iterable<? extends String> args)

    /**
     * Options to pass to the Java runtime
     * @return
     */
    ListProperty<String> getJavaOptions()

    /**
     *
     * @param options
     */
    void setJavaOptions(Iterable<? extends String> options)


    /**
     *
     * @return
     */
    Provider<RegularFile> getIcon()

    /**
     *
     * @param icon
     */
    void setIcon(RegularFileProperty icon)

    /**
     *
     * @param icon
     */
    void setIcon(File icon)

    /**
     *
     * @param icon
     */
    void setIcon(String icon)

    /**
     * Location of the End User License Agreement (EULA) to be presented or recorded by the bundler.
     * @return RegularFile object
     */
    RegularFileProperty getLicenseFile()

    /**
     * File to be created by packager tooll
     * @return RegularFile object
     */
    RegularFileProperty getOutputFile()

    /**
     *
     * @param file
     */
    void setOutputFile(RegularFile file)

    /**
     *
     * @param file
     */
    void setOutputFile(File file)

    /**
     *
     * @param file
     */
    void setOutputFile(String file)

    /**
     * Base directory of the files to package.
     * @return DirectoryProperty
     */
    DirectoryProperty getSourceDir()

    /**
     *
     * @param dir
     */
    void setSourceDir(File dir)

    /**
     *
     * @param dir
     */
    void setSourceDir(Directory dir)

    /**
     * List of files in the directory specified by the -srcdir option.
     * If omitted, all files in the directory (which is a mandatory argument in this case) will be used.
     * @return ConfigurableFileCollection
     */
    ConfigurableFileCollection getSourceFiles()

    /**
     *
     * @param files
     */
    void setSourceFiles(Object... files)

    /**
     *
     * @param files
     */
    void setSourceFiles(Iterable<?> files)

    /**
     * Configure execuable specific options for javapackager (Windows only)
     * @param action
     */
    void exe(Action<? super WinOptions> action)

    /**
     * Configure msi specific options for javapackager (Windows only)
     * @param action
     */
    void msi(Action<? super WinOptions> action)

    /**
     * Configure dmg specific options for javapackager (Mac only)
     * @param action
     */
    void dmg(Action<? super MacOptions> action)

    /**
     * Configure pkg specific options for javapackager (Mac only)
     * @param action
     */
    void pkg(Action<? super MacOptions> action)

}