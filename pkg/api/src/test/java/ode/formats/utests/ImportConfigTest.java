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

import java.util.Map;
import java.util.Properties;

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportCandidates;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportFixture;
import ode.formats.importer.ImportLibrary;
import ode.formats.importer.ODEWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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
public class ImportConfigTest {

    Logger log = LoggerFactory.getLogger(ImportConfigTest.class);
    ImportConfig config;
    Properties p;

    void basic() {
        p = new Properties();
        config = new ImportConfig(null, null, p);
    }

    @Test
    public void testSimple() throws Exception {
        ImportConfig config = new ImportConfig();
        config.hostname.set("foo");
        Map<String, String> dump = config.map();
        Assert.assertEquals(dump.get("hostname"), "foo");
    }

    @Test
    public void testDefaultsDontGetStored() {
        basic();
        ImportConfig.StrValue str = new ImportConfig.StrValue("foo", config, "default");
        str.store();
        Assert.assertNull(p.getProperty("foo"));
    }

    @Test
    public void testDoesntLoadOverExistingValues() {
        basic();
        p.put("src", "prop");
        ImportConfig.StrValue str = new ImportConfig.StrValue("src", config);
        str.set("set");
        str.load();
        Assert.assertEquals(str.get(), "set");
    }

    @Test
    public void testDisableStorage() {
        basic();
        ImportConfig.PassValue pass = new ImportConfig.PassValue("pass", config);
        pass.store();
        Assert.assertEquals(p.getProperty("pass",""), "");
    }

    @Test
    public void testDefaultGetsLoaded() {
        basic();
        ImportConfig.IntValue port = new ImportConfig.IntValue("port", config, 1111);
        Assert.assertEquals(port.get().intValue(), 1111);
    }

}