package ode.services.server.test.geom;

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

import static ode.rtypes.rfloat;
import static ode.rtypes.rint;
import static ode.rtypes.rstring;
import static ode.rtypes.rtime;

import java.util.List;


import ode.api.IPixels;
import ode.io.nio.PixelsService;
import ode.services.roi.GeomTool;
import ode.services.roi.PixelData;
import ode.services.util.Executor;
import ode.system.OdeContext;
import ode.tools.hibernate.SessionFactory;
import ode.util.SqlAction;
import ode.model.Shape;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test(groups = "integration")
public class GeomToolTest{

    protected OdeContext ctx;

    protected PixelData data;

    protected GeomTool geomTool;

    protected SessionFactory factory;

    protected SqlAction sql;

    protected Executor ex;

    protected String uuid;

    @BeforeTest
    public void setup() {
        ctx = OdeContext.getManagedServerContext();
        sql = (SqlAction) ctx.getBean("simpleSqlAction");
        factory = (SessionFactory) ctx.getBean("odeSessionFactory");
        data = new PixelData((PixelsService) ctx.getBean("/ODE/Pixels"),
                (IPixels) ctx.getBean("internal-ode.api.IPixels"));

        ex = (Executor) ctx.getBean("executor");
        uuid = (String) ctx.getBean("uuid");
        geomTool = new GeomTool(data, sql, factory, ex, uuid);

    }

    public void testTicket2045() throws Exception {
        // Synchronization no longer performed!
    }

    public void testShapeConversion() throws Exception {
        List<Shape> shapes = geomTool.random(50000);
        for (Shape shape : shapes) {
            String path = geomTool.dbPath(shape);
            Assert.assertNotNull(path);
        }
    }

}