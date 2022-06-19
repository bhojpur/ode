package ode.formats.utests;

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

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import loci.common.Location;
import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportCandidates;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportFixture;
import ode.formats.importer.ImportLibrary;
import ode.formats.importer.ODEWrapper;
import ode.formats.importer.util.IniFileLoader;
import ode.util.TempFileManager;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.ini4j.IniFile;
import org.ini4j.IniFile.Mode;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Various configuration workflows
 *
 * @see ImportFixture
 * @see ImportReader
 * @see ImportConfig
 * @see ImportLibrary
 * @see ImportCandidates
 * @see ODEWrapper
 * @see ODEMetadataStoreClient
 */
public class IniFileLoaderTest {

    private IniFileLoader ini;
    private Preferences maps;
    private File temporaryFile;

    private static final String[] data = new String[] {
            "\\\\hostname1\\path1;  \\\\hostname2\\path2",
            "\\\\hostname1\\path1;  \\\\hostname2\\path2 ",
            " \\\\hostname1\\path1;  \\\\hostname2\\path2",
            " \\\\hostname1\\path1;  \\\\hostname2\\path2 ",
            " \\\\hostname1\\path1;\\\\hostname2\\path2 ",
            " /hostname1/path1;\\\\hostname2\\path2 ",
            " /hostname1/path1;\\\\hostname2\\path2 ",
            " /hostname1/path1 ; \\\\hostname2\\path2 ",
            "/hostname1/path1 ; \\\\hostname2\\path2"
    };

    @BeforeMethod
    public void setUp() throws Exception {
        temporaryFile = TempFileManager.create_path("initest");
        ini = new IniFileLoader(null);
        maps = Preferences.userNodeForPackage(getClass());
    }

    @AfterMethod
    public void tearDown() throws Exception {
        temporaryFile.delete();
    }

    protected void assertMaps(Map<String, List<String>> rv) {
        Assert.assertEquals(1, rv.size());
        List<String> list = rv.get("A");
        Assert.assertEquals(list.size(), 2);
        String first = list.get(0);
        if (!"\\\\hostname1\\path1".equals(first)
            && !"/hostname1/path1".equals(first)) {
            Assert.fail(String.format("%s does not equal %s or %s",
                    first, "\\\\hostname1\\path1", "/hostname1/path1"));
        }
        Assert.assertEquals(list.get(1), "\\\\hostname2\\path2");
    }

    @Test
    public void testFlexReaderServerMapUNC() throws Exception {
        FileUtils.writeLines(temporaryFile, Arrays.asList(
                "[FlexReaderServerMaps]",
                "CIA-1=\\\\\\\\hostname1\\\\path1;\\\\\\\\hostname1\\\\path2"));
        Preferences test = new IniFile(temporaryFile, Mode.RW);
        Preferences maps = test.node("FlexReaderServerMaps");
        Map<String, List<String>> parsedMaps = ini.parseFlexMaps(maps);
        Assert.assertEquals(parsedMaps.size(), 1);
        List<String> serverPaths = parsedMaps.get("CIA-1");
        Assert.assertEquals(serverPaths.size(), 2);
        Assert.assertEquals(serverPaths.get(0), "\\\\hostname1\\path1");
        Assert.assertEquals(serverPaths.get(1), "\\\\hostname1\\path2");
    }

    @Test
    public void testFlexReaderServerMapUnix() throws Exception {
        FileUtils.writeLines(temporaryFile, Arrays.asList(
                "[FlexReaderServerMaps]",
                "CIA-1=/mnt/path1;/mnt/path2"));
        Preferences test = new IniFile(temporaryFile, Mode.RW);
        Preferences maps = test.node("FlexReaderServerMaps");
        Map<String, List<String>> parsedMaps = ini.parseFlexMaps(maps);
        Assert.assertEquals(parsedMaps.size(), 1);
        List<String> serverPaths = parsedMaps.get("CIA-1");
        Assert.assertEquals(serverPaths.size(), 2);
        Assert.assertEquals(serverPaths.get(0), "/mnt/path1");
        Assert.assertEquals(serverPaths.get(1), "/mnt/path2");
    }

    @Test
    public void testAllData() throws Exception {
        for (String item : data) {
            maps.put("A",item);
            assertMaps(ini.parseFlexMaps(maps));
        }
    }

    @Test
    public void testDuplicatedSections() throws Exception {
        FileUtils.writeLines(temporaryFile, Arrays.asList(
                "[Section]",
                "A=1",
                "[Section]",
                "A=2"));
        Preferences test = new IniFile(temporaryFile, Mode.RW);
        String a1 = test.node("Section").get("A", "missing");
        Assert.assertEquals(a1, "2");
    }

    @Test
    public void testDuplicatedSections2() throws Exception {
        FileUtils.writeLines(temporaryFile, Arrays.asList(
                "[Section]",
                "[Section]",
                "A=1"));
        Preferences test = new IniFile(temporaryFile, Mode.RW);
        String a1 = test.node("Section").get("A", "missing");
        Assert.assertEquals(a1, "1");
    }

    @Test
    public void testLocation() throws Exception {
        ch.qos.logback.classic.Logger lociLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("loci");
        lociLogger.setLevel(ch.qos.logback.classic.Level.DEBUG);
        Location loc = new Location("/");
        Assert.assertTrue(loc.exists());
    }

}