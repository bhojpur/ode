package ode.system.utests;

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

import java.util.Properties;

import ode.system.PreferenceContext;

import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.Ignore;

@Test(groups = "ticket:800")
public class PrefsTest {

    public final static String testDefault = "test_default";

    PreferenceContext ctx;
    String oldDefault;

    @Test
    public void testSimple() {
        ctx = new PreferenceContext();
        System.setProperty("test", "ok"); //
        Assert.assertEquals(ctx.getProperty("test"), "ok");
    }

    // Locals

    @Test
    public void testSystemOverridesLocals() {

        String key = "localsOverrideSystem";

        System.setProperty(key, "false");

        Properties p = new Properties();
        p.setProperty(key, "true");

        ctx = new PreferenceContext();
        ctx.setProperties(p);

        Assert.assertEquals(ctx.getProperty(key), "false");
    }

    @Test
    public void testLocalsOverrideFiles() {

        String key = "localsOverridesFiles";

        Properties p = new Properties();
        p.setProperty(key, "true");

        ctx = new PreferenceContext();
        ctx.setProperties(p);
        ctx.setIgnoreResourceNotFound(false);
        ctx.setLocations(new Resource[] { new ClassPathResource(
                "ode/system/utests/Prefs.properties") });

        Assert.assertEquals(ctx.getProperty(key), "true");

    }

    // System

    /**
     * Currently the {@link PreferencesPlaceholderConfigurer} does not use the
     * {@link System#getProperties()} to as
     * {@link PropertyPlaceholderConfigurer} does. We may need to modify
     * {@link PreferenceContext} to do so.
     */
    @Test(groups = "broken")
    public void testSystemOverridesFiles() {

        String key = "systemOverridesFiles";

        System.setProperty(key, "true");

        ctx = new PreferenceContext();
        ctx.setIgnoreResourceNotFound(false);
        ctx.setLocations(new Resource[] { new ClassPathResource(
                "ode/system/utests/Prefs.properties") });

        Assert.assertEquals(ctx.getProperty(key), "true");

    }

    @Test
    public void testMissingFilesOk() {
        ctx = new PreferenceContext();
        ctx.setLocations(new Resource[] { new ClassPathResource(
                "DOES_NOT_EXIST") });
        ctx.getProperty("test");
    }

    @Ignore("To re-enable when we sort out version handling")
    @Test
    public void testOdeVersion() {
        ctx = new PreferenceContext();
        ctx.setLocation(new ClassPathResource("ode.properties"));
        String v = ctx.getProperty("ode.version");
        Assert.assertNotNull(v);
        Assert.assertTrue(v.length() > 0);
    }

}