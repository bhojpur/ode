package ode.services.server.test;

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

import ode.api.AMD_IAdmin_getEventContext;
import ode.api.AMD_IAdmin_getGroup;
import ode.model.ExperimenterGroup;
import ode.sys.EventContext;

import org.testng.annotations.Test;

@Test(groups = "integration")
public class AdminITest extends AbstractServantTest {

    @Test
    public void testgetEventContext() throws Exception {
        final RV rv = new RV();
        user.admin.getEventContext_async(new AMD_IAdmin_getEventContext() {
            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }

            public void ice_response(EventContext __ret) {
                rv.rv = __ret;
            }
        }, current("getEventContext"));
        rv.assertPassed();
    }

    @Test(groups = "ticket:1204")
    public void testGetGroup() throws Exception {
        final RV rv = new RV();
        user.admin.getGroup_async(new AMD_IAdmin_getGroup() {

            public void ice_exception(Exception exc) {
                rv.ex = exc;
            }

            public void ice_response(ExperimenterGroup __ret) {
                rv.rv = __ret;
            }
        }, 0L, current("getGroup"));
        rv.assertPassed();
    }
}