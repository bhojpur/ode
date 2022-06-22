package integration;

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

import ode.model.core.Image;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.cmd.Chgrp2;
import ode.cmd.Chmod2;
import ode.cmd.Chown2;
import ode.cmd.Delete2;
import ode.cmd.LegalGraphTargets;
import ode.cmd.LegalGraphTargetsResponse;
import ode.cmd.SkipHead;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test which model object classes are reported as being legal as targets for a request.
 */
public class LegalGraphTargetsTest extends AbstractServerTest {
    /**
     * Test a model object class that is expected to be legal for {@link Chgrp2}.
     * @throws Exception unexpected
     */
    @Test
    public void testInclusionChgrp() throws Exception {
        final LegalGraphTargets query = new LegalGraphTargets();
        query.request = new Chgrp2();
        final LegalGraphTargetsResponse response = (LegalGraphTargetsResponse) doChange(query);
        Assert.assertTrue(response.targets.contains(Image.class.getName()));
    }

    /**
     * Test a model object class that is not expected to be legal for {@link Chgrp2}.
     * @throws Exception unexpected
     */
    @Test
    public void testExclusionChgrp() throws Exception {
        final LegalGraphTargets query = new LegalGraphTargets();
        query.request = new Chgrp2();
        final LegalGraphTargetsResponse response = (LegalGraphTargetsResponse) doChange(query);
        Assert.assertFalse(response.targets.contains(Experimenter.class.getName()));
    }

    /**
     * Test a model object class that is expected to be legal for {@link Chmod2}.
     * @throws Exception unexpected
     */
    @Test
    public void testInclusionChmod() throws Exception {
        final LegalGraphTargets query = new LegalGraphTargets();
        query.request = new Chmod2();
        final LegalGraphTargetsResponse response = (LegalGraphTargetsResponse) doChange(query);
        Assert.assertTrue(response.targets.contains(ExperimenterGroup.class.getName()));
    }

    /**
     * Test a model object class that is not expected to be legal for {@link Chmod2}.
     * @throws Exception unexpected
     */
    @Test
    public void testExclusionChmod() throws Exception {
        final LegalGraphTargets query = new LegalGraphTargets();
        query.request = new Chmod2();
        final LegalGraphTargetsResponse response = (LegalGraphTargetsResponse) doChange(query);
        Assert.assertFalse(response.targets.contains(Image.class.getName()));
    }

    /**
     * Test a model object class that is expected to be legal for {@link Chown2}.
     * @throws Exception unexpected
     */
    @Test
    public void testInclusionChown() throws Exception {
        final LegalGraphTargets query = new LegalGraphTargets();
        query.request = new Chown2();
        final LegalGraphTargetsResponse response = (LegalGraphTargetsResponse) doChange(query);
        Assert.assertTrue(response.targets.contains(Image.class.getName()));
    }

    /**
     * Test a model object class that is not expected to be legal for {@link Chown2}.
     * @throws Exception unexpected
     */
    @Test
    public void testExclusionChown() throws Exception {
        final LegalGraphTargets query = new LegalGraphTargets();
        query.request = new Chown2();
        final LegalGraphTargetsResponse response = (LegalGraphTargetsResponse) doChange(query);
        Assert.assertFalse(response.targets.contains(Experimenter.class.getName()));
    }

    /**
     * Test a model object class that is expected to be legal for {@link Delete2}.
     * @throws Exception unexpected
     */
    @Test
    public void testInclusionDelete() throws Exception {
        final LegalGraphTargets query = new LegalGraphTargets();
        query.request = new Delete2();
        final LegalGraphTargetsResponse response = (LegalGraphTargetsResponse) doChange(query);
        Assert.assertTrue(response.targets.contains(Image.class.getName()));
    }

    /**
     * Test a model object class that is not expected to be legal for {@link Delete2}.
     * @throws Exception unexpected
     */
    @Test
    public void testExclusionDelete() throws Exception {
        final LegalGraphTargets query = new LegalGraphTargets();
        query.request = new Delete2();
        final LegalGraphTargetsResponse response = (LegalGraphTargetsResponse) doChange(query);
        Assert.assertFalse(response.targets.contains(Experimenter.class.getName()));
    }

    /**
     * Test that {@link SkipHead} cannot be queried for legal targets.
     * @throws Exception unexpected
     */
    @Test
    public void testSkipHeadFails() throws Exception {
        final LegalGraphTargets query = new LegalGraphTargets();
        query.request = new SkipHead();
        doChange(client, factory, query, false);
    }
}
