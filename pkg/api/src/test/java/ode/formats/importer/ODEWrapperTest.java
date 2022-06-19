package ode.formats.importer;

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

import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.Memoizer;
import loci.formats.MinMaxCalculator;
import loci.formats.in.FakeReader;
import loci.formats.meta.DummyMetadata;
import loci.formats.ode.ODEXMLMetadataImpl;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ode.util.TempFileManager;

public class ODEWrapperTest {

    class Bad {
        class Store extends DummyMetadata {
            public Store(boolean ThisCantBeDeserialized) {
                /*
                 * no action
                 */
            }
        }
    }

    File fake;

    File png;

    ODEWrapper wrapper;

    ImportConfig config;

    @BeforeMethod
    public void createPNG() throws Exception {
        fake = TempFileManager.create_path("odewrappertest.", ".fake");
        png = TempFileManager.create_path("odewrappertest.", ".png");
        FileUtils.touch(fake);
        FormatTools.convert(fake.getAbsolutePath(), png.getAbsolutePath());
    }

    @AfterMethod
    public void deleteFiles() throws Exception {
        Memoizer m = new Memoizer();
        FileUtils.deleteQuietly(fake);
        FileUtils.deleteQuietly(png);
        FileUtils.deleteQuietly(m.getMemoFile(png.toString()));
        FileUtils.deleteQuietly(m.getMemoFile(fake.toString()));
        m.close();
    }

    @BeforeMethod
    public void setUp() {
        config = new ImportConfig();
        wrapper = new ODEWrapper(config, 0, null);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        wrapper.close();
    }

    @Test
    public void testFake() throws Exception {
        wrapper.setId(fake.getAbsolutePath());
        wrapper.close();
    }

    @Test
    public void testPNG() throws Exception {
        wrapper.setId(png.getAbsolutePath());
        wrapper.close();
    }

    @Test
    public void testReuse() throws Exception {
        wrapper.setId(fake.getAbsolutePath());
        wrapper.close();
        wrapper.setId(png.getAbsolutePath());
    }

    @Test(timeOut=3000)
    public void testMatchedWrappers() throws Exception {
        try (FakeReader r = new FakeReader();
             Memoizer m = new Memoizer(r, 0)) {
            m.setId(fake.getAbsolutePath());
            m.close();
            Assert.assertTrue(m.isSavedToMemo());
            m.setId(fake.getAbsolutePath());
            Assert.assertTrue(m.isLoadedFromMemo());
            m.close();
        }
    }

    @Test(invocationCount = 10, timeOut=3000)
    public void testMismatchedWrappers() throws Exception {
        try (IFormatReader r = new MinMaxCalculator();
                Memoizer m = new Memoizer(0L);
                Memoizer m1 = new Memoizer(r, 0L)) {
            m.setId(fake.getAbsolutePath());
            m.close();
            Assert.assertTrue(m.isSavedToMemo());
            m1.setId(fake.getAbsolutePath());
            Assert.assertFalse(m1.isLoadedFromMemo());
            m1.close();
        }
    }

    @Test
    public void testODEXMLMetadataStore() throws Exception {
        wrapper.setMetadataStore(new ODEXMLMetadataImpl());
        wrapper.setId(fake.getAbsolutePath());
        wrapper.close();
        Memoizer m = new Memoizer(wrapper.getImageReader(), 0L);
        m.setId(fake.getAbsolutePath());
        Assert.assertFalse(m.isLoadedFromMemo());
        Assert.assertTrue(m.isSavedToMemo());
        m.close();
        wrapper.setId(fake.getAbsolutePath());
        wrapper.close();
        m = new Memoizer(wrapper.getImageReader(), 0L);
        m.setId(fake.getAbsolutePath());
        Assert.assertTrue(m.isLoadedFromMemo());
        Assert.assertFalse(m.isSavedToMemo());
        Assert.assertEquals(wrapper.getMetadataStore().getClass(),
                ODEXMLMetadataImpl.class);
        m.close();
    }

    @Test
    public void testUnserializable() throws Exception {
        wrapper.setMetadataStore(new Bad().new Store(false));
        wrapper.setId(png.getAbsolutePath());
        wrapper.close();
        wrapper.setId(png.getAbsolutePath());
    }

}