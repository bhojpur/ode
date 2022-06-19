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

import java.io.File;

import ode.services.server.impl.MetadataStoreI;
import ode.services.roi.PopulateRoiJob;
import ode.services.util.Executor;
import ode.sys.ParametersI;

import org.testng.annotations.Test;

@Test(groups = "integration")
public class MetadataStoreITest extends AbstractServantTest {

    File file() {
        File toplevel = new File(
                "components/tools/OdePy/src/ode/util/populate_roi.py");
        File relative = new File(
                "../tools/OdePy/src/ode/util/populate_roi.py");
        if (toplevel.exists()) {
            return toplevel;
        } else if (relative.exists()) {
            return relative;
        } else {
            throw new RuntimeException("huh? where are you?");
        }
    }

    /*
    BROKEN BY r5316
    @Test(groups = "ticket:1193")
    public void testPostProcess() throws Exception {
        setUp();
        Executor ex = (Executor) ctx.getBean("executor");
        PopulateRoiJob popRoi = new PopulateRoiJob(root_sf.getPrincipal(), ex,
                file());
        popRoi.init();
        popRoi.createJob();
        MetadataStoreI ms = new MetadataStoreI(be, popRoi);
        configure(ms, user_initializer);
        ms.setServiceFactory(user_sf);
        ParametersI p = new ParametersI().add("pixels", ode.rtypes.rlist(ode.rtypes.rlong(1)));
        assertFindByQuery(MetadataStoreI.plate_query, p);
    }
    */

}