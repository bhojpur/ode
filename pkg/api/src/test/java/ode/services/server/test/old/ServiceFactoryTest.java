package ode.services.server.test.old;

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

import ode.api.IConfigPrx;
import ode.api.IConfigPrxHelper;
import ode.api.IUpdatePrx;
import ode.api.IUpdatePrxHelper;
import ode.api.RenderingEnginePrx;
import ode.api.ServiceFactoryPrx;
import ode.api.ServiceInterfacePrx;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ServiceFactoryTest extends IceTest {

    ode.client ice;

    @Test
    public void testProvidesIConfig() throws Exception {
        ice = new ode.client();
        try {
            ice.createSession(null, null);
            Ice.ObjectPrx base = ice.getSession().getConfigService();
            IConfigPrx prx = IConfigPrxHelper.checkedCast(base);
            Assert.assertNotNull(prx);
        } finally {
            ice.closeSession();
        }
    }

    @Test
    public void testProvidesIUpdate() throws Exception {
        ice = new ode.client();
        try {
            ice.createSession(null, null);
            Ice.ObjectPrx base = ice.getSession().getUpdateService();
            IUpdatePrx prx = IUpdatePrxHelper.checkedCast(base);
            Assert.assertNotNull(prx);
        } finally {
            ice.closeSession();
        }
    }

    @Test
    public void testProvidesRenderingEngine() throws Exception {
        ice = new ode.client();
        try {
            ice.createSession(null, null);
            RenderingEnginePrx prx = ice.getSession()
                    .createRenderingEngine();
            Assert.assertNotNull(prx);
        } finally {
            ice.closeSession();
        }
    }

    @Test
    public void testKeepAliveAndIsAliveWorkOnNewProxy() throws Exception {
        ice = new ode.client();
        try {
            ice.createSession(null, null);
            ServiceFactoryPrx session = ice.getSession();
            RenderingEnginePrx prx = session.createRenderingEngine();
            Assert.assertNotNull(prx);
            Assert.assertTrue(session.keepAlive(prx));
            Assert.assertTrue(0 == session.keepAllAlive(new ServiceInterfacePrx[] { prx }));
        } finally {
            ice.closeSession();
        }
    }

    @Test
    public void testGetByNameFailsOnStatefulService() throws Exception {
        Assert.fail("NYI");
    }
}