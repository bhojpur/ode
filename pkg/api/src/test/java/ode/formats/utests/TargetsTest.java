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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportContainer;
import ode.formats.importer.targets.ImportTarget;
import ode.formats.importer.targets.ModelImportTarget;
import ode.formats.importer.targets.TargetBuilder;
import ode.formats.importer.targets.TemplateImportTarget;
import ode.model.units.BigResult;
import ode.model.IObject;
import ode.model.Screen;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TargetsTest {

    public static class TestTarget implements ImportTarget {

        @Override
        public void init(String target) {
        }

        @Override
        public IObject load(ODEMetadataStoreClient client, ImportContainer ic) {
            return null;
        }

    }

    TargetBuilder tb() {
        return new TargetBuilder();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testResistReuse() {
        tb().parse("Screen:1").parse("Dataset:1");
    }

    @Test
    public void testBuilder() throws BigResult {
        ImportTarget t;
        t = tb().parse("Screen:1").build();
        Assert.assertTrue(t instanceof ModelImportTarget);
        t = tb().parse("Dataset:1").build();
        Assert.assertTrue(t instanceof ModelImportTarget);
        t = tb().parse("/a/b/c").build();
        Assert.assertTrue(t instanceof TemplateImportTarget);
        t = tb().parse("unknown").build();
        Assert.assertTrue(t instanceof TemplateImportTarget);
        t = tb().parse(TestTarget.class.getName()+":stuff").build();
        Assert.assertTrue(t instanceof TestTarget);
    }

    @Test
    public void testModelImportTarget() throws Exception {
        TargetBuilder b = new TargetBuilder();
        ModelImportTarget t = (ModelImportTarget) b.parse("Screen:1").build();
        Assert.assertEquals(t.getObjectType(), Screen.class);
        IObject obj = t.load(null, null);
        Assert.assertEquals(obj.getId().getValue(), 1L);
    }

    @Test
    public void testTemplateRegexes() {
        Pattern p;
        Matcher m;

        p = Pattern.compile("(?<Container1>.*)");
        m = p.matcher("everything");
        Assert.assertTrue(m.matches());
        Assert.assertEquals(m.group("Container1"), "everything");

        p = Pattern.compile("(?<Ignore>/home)/(?<Container1>.*)");
        m = p.matcher("/home/user/MyLab/2015-01-01/");
        Assert.assertTrue(m.matches());
        Assert.assertEquals(m.group("Ignore"), "/home");
        Assert.assertEquals(m.group("Container1"), "user/MyLab/2015-01-01/");

        // Explicit "ignore"
        p = Pattern.compile("(?<Ignore>(/[^/]+){2})/(?<Container1>.*?)");
        m = p.matcher("/home/user/MyLab/2015-01-01/");
        Assert.assertTrue(m.matches());
        Assert.assertEquals(m.group("Ignore"), "/home/user");
        Assert.assertEquals(m.group("Container1"), "MyLab/2015-01-01/");

        // Implicit "ignore"
        p = Pattern.compile("^.*user/(?<Container1>.*?)");
        m = p.matcher("/home/user/MyLab/2015-01-01/");
        Assert.assertTrue(m.matches());
        try {
            m.group("Ignore"); // Not included
        } catch (IllegalArgumentException iae) {
            // good
        }
        Assert.assertEquals(m.group("Container1"), "MyLab/2015-01-01/");

        // Group
        p = Pattern.compile("^.*user/(?<Group>[^/]+)/(?<Container1>.*?)");
        m = p.matcher("/home/user/MyLab/2015-01-01/");
        Assert.assertTrue(m.matches());
        Assert.assertEquals(m.group("Group"), "MyLab");
        Assert.assertEquals(m.group("Container1"), "2015-01-01/");

        // Container2 takes all extra paths
        p = Pattern.compile("^.*user/(?<Container2>([^/]+/)*)(?<Container1>([^/]+/))");
        m = p.matcher("/home/user/MyLab/2015-01-01/foo/");
        Assert.assertTrue(m.matches());
        Assert.assertEquals(m.group("Container2"), "MyLab/2015-01-01/");
        Assert.assertEquals(m.group("Container1"), "foo/");
        m = p.matcher("/home/user/MyLab/");
        Assert.assertTrue(m.matches());
        Assert.assertEquals(m.group("Container1"), "MyLab/");

        // TODO:
        // Guarantee whether all paths will end in / or not
        // Add a helper for: ([^/]+/)
        // Add a name for IDs CID1, CID2
    }

    // Absolute:
    // --------
    // /tmp/my-data/JRSLab/2015-05-foo/some-dir/a.fake
    // /{-2}/{Group}/{Container}/
    // /{-2}/{Group}/{Container*}/
    // /{-2}/{Group}/{Container+}/
    // /{-2}/{Group}/{Container}/{Container*}
    // /{-2}/{Group}/{Container*}/{Container}

    // Relative:

    String[][] data = new String[][] {
        new String[] {"/{-3}/{Group}/" },
        new String[] {"{/" },
    };

    @Test
    public void testTemplateBuilding() {
        TargetBuilder b = new TargetBuilder();
        b.parse("");
    }
}