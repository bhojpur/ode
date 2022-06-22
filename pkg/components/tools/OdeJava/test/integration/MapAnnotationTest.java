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

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import ode.ServerError;
import ode.api.IQueryPrx;
import ode.api.IUpdatePrx;
import ode.model.ExperimenterGroup;
import ode.model.ExperimenterGroupI;
import ode.model.MapAnnotation;
import ode.model.NamedValue;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Testing of the new {@link MapAnnotation} feature including
 * the underlying support for {@link Map} fields.
 */
public class MapAnnotationTest extends AbstractServerTest {

    /**
     * Test persistence of a foo &rarr; bar map.
     * @throws ServerError unexpected
     */
    @Test
    public void testStringMapField() throws ServerError {
        String uuid = UUID.randomUUID().toString();
        IQueryPrx queryService = root.getSession().getQueryService();
        IUpdatePrx updateService = root.getSession().getUpdateService();
        ExperimenterGroup group = new ExperimenterGroupI();
        group.setName(ode.rtypes.rstring(uuid));
        group.setLdap(ode.rtypes.rbool(false));
        group.setConfig(new ArrayList<NamedValue>());
        group.getConfig().add(new NamedValue("foo", "bar"));
        group = (ExperimenterGroup) updateService.saveAndReturnObject(group);
        group = (ExperimenterGroup) queryService.findByQuery(
                "select g from ExperimenterGroup g join fetch g.config " +
                "where g.id = " + group.getId().getValue(), null);
        Assert.assertEquals("foo", group.getConfig().get(0).name);
        Assert.assertEquals("bar", group.getConfig().get(0).value);
    }

    /**
     * Test persistence of a foo &rarr; empty string map.
     * @throws ServerError unexpected
     */
    @Test
    public void testStringMapEmptyField() throws Exception {
        String uuid = UUID.randomUUID().toString();
        IQueryPrx queryService = root.getSession().getQueryService();
        IUpdatePrx updateService = root.getSession().getUpdateService();
        ExperimenterGroup group = new ExperimenterGroupI();
        group.setName(ode.rtypes.rstring(uuid));
        group.setLdap(ode.rtypes.rbool(false));
        group.setConfig(new ArrayList<NamedValue>());
        group.getConfig().add(new NamedValue("foo", ""));
        group = (ExperimenterGroup) updateService.saveAndReturnObject(group);
        group = (ExperimenterGroup) queryService.findByQuery(
                "select g from ExperimenterGroup g join fetch g.config " +
                "where g.id = " + group.getId().getValue(), null);
        Assert.assertEquals("foo", group.getConfig().get(0).name);
        Assert.assertEquals("", group.getConfig().get(0).value);
    }

    /**
     * Test persistence of a bar &rarr; <code>null</code> map.
     * These null fields get converted to the empty string by Ice.
     */
    @Test
    public void testNulledMapValue() throws Exception {
        String uuid = UUID.randomUUID().toString();
        IUpdatePrx updateService = root.getSession().getUpdateService();
        ExperimenterGroup group = new ExperimenterGroupI();
        group.setName(ode.rtypes.rstring(uuid));
        group.setLdap(ode.rtypes.rbool(false));
        group.setConfig(new ArrayList<NamedValue>());
        group.getConfig().add(new NamedValue("foo", ""));
        group.getConfig().add(new NamedValue("bar", null));
        group = (ExperimenterGroup) updateService.saveAndReturnObject(group);
        Assert.assertEquals("", group.getConfig().get(0).value);
    }

    @Test
    public void testMapGetters() throws Exception {
        ExperimenterGroup group = new ExperimenterGroupI();
        group.setConfig(new ArrayList<NamedValue>());
        group.getConfig().add(new NamedValue("foo", ""));
        group.getConfig().add(new NamedValue("bar", null));
        group.getConfigAsMap();
    }
}
