package net.bhojpur.gradle.icebuilder.slice

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

import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.EnvironmentVariables

import static org.junit.Assert.*
import static org.junit.Assume.*

class SlicePluginPropertyTest extends TestCase {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Test
    public void pluginAddsCompileSliceTaskToProject() {
        assertTrue(project.tasks.compileSlice instanceof SliceTask)
    }

    @Test
    public void testAutoDetectIceHome() {
        // This test only works if ICE_HOME is not set
        assumeTrue(System.getenv()['ICE_HOME'] == null)
        assertTrue(project.slice.iceHome != "")
        assertTrue(project.slice.iceVersion != "" && project.slice.iceVersion != null)
        assertTrue(new File(project.slice.slice2java).exists())
        assertTrue(new File(project.slice.sliceDir).exists())
    }

    @Test
    public void testManualBinDistIceHome() {
        // This test only works if ICE_HOME is not set
        assumeTrue(System.getenv()['ICE_HOME'] == null)
        def iceHome = project.slice.iceHome
        project.slice.iceHome = iceHome
        assertTrue(project.slice.iceHome != "")
        assertNotNull(project.slice.iceHome)
        assertTrue(project.slice.iceVersion != "")
        assertNotNull(project.slice.iceVersion)
        assertTrue(new File(project.slice.slice2java).exists())
        assertTrue(new File(project.slice.sliceDir).exists())
    }

    @Test
    public void testInvalidIceHome() {
        // Test a bogus iceHome
        def tmpIceHome = File.createTempDir()
        tmpIceHome.deleteOnExit()
        project.slice.iceHome = tmpIceHome.toString()
        try {
            def iceHome = project.slice.iceHome
            fail()
        } catch (e) {
            // This should throw an exception since /dev/null is not a valid iceHome location. slice2java is missing
        }
    }

    @Test
    public void testIceHomeWithNoSlice2Java() {
        // Test that if iceHome is a srcDist and slice2java is missing that we can still
        // initialize (at least partially) the configuration without failure

        // Create temporary iceHome with fake structure that slice extension requires
        def tmpIceHome = File.createTempDir()
        tmpIceHome.deleteOnExit()
        def tmpBuildGralde = new File([tmpIceHome.toString(), "java", "build.gradle"].join(File.separator))
        tmpBuildGralde.mkdirs()
        tmpBuildGralde.deleteOnExit()
        assertTrue(tmpBuildGralde.exists())

        project.slice.iceHome = tmpIceHome.toString()

        assertTrue(project.slice.iceHome == tmpIceHome.toString())
        assertTrue(project.slice.iceVersion == null)
        assertTrue(project.slice.sliceDir == null)
    }

    @Test
    public void testCppPlatformAndConfigurationFromEnvironment() {
        environmentVariables.set("CPP_CONFIGURATION", "Release");
        environmentVariables.set("CPP_PLATFORM", "Win32");
        forceReinitialization()

        assertTrue(project.slice.cppConfiguration == "Release")
        assertTrue(project.slice.cppPlatform == "Win32")
    }

    @Test
    public void testCppPlatformAndConfiguration() {
        project.slice {
            cppConfiguration = "Debug"
            cppPlatform = "x64"
        }
        assertTrue(project.slice.cppConfiguration == "Debug")
        assertTrue(project.slice.cppPlatform == "x64")
    }

    @Test
    public void testCppPlatformAndConfigurationOverrideEnvironment() {
        environmentVariables.set("CPP_CONFIGURATION", "Release");
        environmentVariables.set("CPP_PLATFORM", "Win32");
        forceReinitialization()

        project.slice {
            cppConfiguration = "Debug"
            cppPlatform = "x64"
        }

        assertTrue(project.slice.cppConfiguration == "Debug")
        assertTrue(project.slice.cppPlatform == "x64")
    }
}