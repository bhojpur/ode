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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ode.formats.MockedODEImportFixture;
import ode.services.server.impl.AdminI;
import ode.services.server.impl.QueryI;
import ode.services.server.impl.UpdateI;
import ode.system.OdeContext;
import ode.system.ServiceFactory;
import ode.testing.InterceptingServiceFactory;
import ode.RType;
import ode.api.AMD_IAdmin_getEventContext;
import ode.api.AMD_IQuery_findAllByQuery;
import ode.api.AMD_IQuery_projection;
import ode.api.AMD_IUpdate_saveAndReturnObject;
import ode.model.IObject;
import ode.model.Pixels;
import ode.sys.EventContext;
import ode.util.TempFileManager;

import org.apache.commons.io.IOUtils;
import org.jmock.MockObjectTestCase;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

@Test(groups = "integration")
public abstract class AbstractServantTest extends MockObjectTestCase {

    private final static AtomicReference<File> tinyHolder =
        new AtomicReference<File>();

    protected ManagedContextFixture user, root;
    protected OdeContext ctx;
    protected File odeDataDir;

    public class RV {
        public Exception ex;
        public Object rv;

        public Object assertPassed() throws Exception {
            if (ex != null) {
                throw ex;
            }
            return rv;
        }
    }

    @Override
    protected void setUp() throws Exception {

        // ticket:#6417
        odeDataDir = TempFileManager.create_path(".odeDataDir", "test", true);
        System.setProperty("ode.data.dir", odeDataDir.getAbsolutePath());

        // Shared
        OdeContext inner = OdeContext.getManagedServerContext();
        ctx = new OdeContext(new String[] { "classpath:ode/test2.xml",
                "classpath:ode/services/messaging.xml",
                "classpath:ode/services/spec.xml", // for DeleteI
                "classpath:ode/config.xml", // for ${} in servantDefs.
                "classpath:ode/services/throttling/throttling.xml"
        }, false);
        ctx.setParent(inner);
        ctx.afterPropertiesSet();

        user = new ManagedContextFixture(ctx);
        root = new ManagedContextFixture(ctx);
    }

    @Override
    @AfterClass
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SuppressWarnings("unchecked")
    protected List<IObject> assertFindByQuery(String q, ode.sys.Parameters p)
            throws Exception {
        return assertFindByQuery(user.query, q, p);
    }

    @SuppressWarnings("unchecked")
    protected List<List<RType>> assertProjection(String q, ode.sys.Parameters p)
            throws Exception {
        return assertProjection(user.query, q, p);
    }

    @SuppressWarnings("unchecked")
    protected List<IObject> assertFindByQuery(QueryI query, String q,
            ode.sys.Parameters p) throws Exception {

        final Exception[] ex = new Exception[1];
        final boolean[] status = new boolean[1];
        final List[] rv = new List[1];
        query.findAllByQuery_async(new AMD_IQuery_findAllByQuery() {

            public void ice_exception(Exception exc) {
                ex[0] = exc;
            }

            public void ice_response(List<IObject> __ret) {
                rv[0] = __ret;
                status[0] = true;
            }
        }, q, p, current("findAllByQuery"));
        if (ex[0] != null) {
            throw ex[0];
        } else {
            assertTrue(status[0]);
        }
        return rv[0];
    }

    @SuppressWarnings("unchecked")
    protected List<List<RType>> assertProjection(QueryI query, String q,
            ode.sys.Parameters p) throws Exception {

        final RV rv = new RV();
        query.projection_async(new AMD_IQuery_projection() {

            public void ice_exception(Exception exc) {
                rv.ex = exc;
            }

            @SuppressWarnings("rawtypes")
            public void ice_response(List __ret) {
                rv.rv = __ret;
            }
        }, q, p, current("projection"));
        rv.assertPassed();
        return (List<List<RType>>) rv.rv;
    }

    protected <T extends IObject> T assertSaveAndReturn(T t) throws Exception {
        return assertSaveAndReturn(user.update, t);
    }

    protected <T extends IObject> T assertSaveAndReturn(UpdateI up, T t)
            throws Exception {
        final RV rv = new RV();
        up.saveAndReturnObject_async(new AMD_IUpdate_saveAndReturnObject() {

            public void ice_exception(Exception exc) {
                rv.ex = exc;
            }

            public void ice_response(IObject __ret) {
                rv.rv = __ret;
            }
        }, t, current("saveAndReturnObject"));
        rv.assertPassed();
        return (T) rv.rv;
    }

    protected EventContext assertEventContext(AdminI admin)
            throws Exception {
        final RV rv = new RV();
        admin.getEventContext_async(new AMD_IAdmin_getEventContext() {

            public void ice_exception(Exception exc) {
                rv.ex = exc;
            }

            public void ice_response(EventContext __ret) {
                rv.rv = __ret;
            }
        }, current("getEventContext"));
        rv.assertPassed();
        return (EventContext) rv.rv;
    }

    protected Ice.Current current(String method) {
        Ice.Current curr = new Ice.Current();
        curr.operation = method;
        return curr;
    }

    protected long makePixels() throws Exception, FileNotFoundException {
        if (false) {
            throw new RuntimeException(
                    "Unforunately MockedODEImportFixture is not supported here \n"
                            + "Instead, the service factory must be registered with a communicator \n"
                            + "and that proxy given to the ODEImportFixture");
        } else {
            long pixels = -1;

            MockedODEImportFixture fixture = new MockedODEImportFixture(
                    user.managedSf, "");
            File tinyTest = getTinyFileName();
            List<Pixels> list = fixture.fullImport(tinyTest, "tinyTest");
            pixels = list.get(0).getId().getValue();
            return pixels;
        }
    }

    protected long makeImage() throws Exception, FileNotFoundException {
        long pixels = makePixels();
        //ServiceFactory sf = new InterceptingServiceFactory(this.sf, user.login);
        //return sf.getQueryService().findByQuery("select i from Image i join i.pixels p " +
		//	"where p.id = " + pixels, null).getId();
        return pixels;
    }

    /**
     * Since in some cases the tinyTest.d3d.dv file is in a jar and
     * not a regular file, we may need to copy it to a temporary file
     * which gets destroyed
     * @return
     */
    protected File getTinyFileName() throws IOException {
        File f = tinyHolder.get();
        if (f == null) {
            String tt = "classpath:tinyTest.d3d.dv";
            try {
                f = ResourceUtils.getFile(tt);
                tinyHolder.compareAndSet(null, f);
            } catch (FileNotFoundException fnfe) {
                URL url = ResourceUtils.getURL(tt);
                InputStream is = url.openStream();
                f = File.createTempFile("tinyTest", ".dv");
                FileOutputStream fos = new FileOutputStream(f);
                IOUtils.copy(is, fos);
                fos.close();
                if (tinyHolder.compareAndSet(null, f)) {
                    f.deleteOnExit();
                } else {
                    // Value was updated in another thread.
                    f.delete();
                    f = tinyHolder.get();
                }
            }
        }
        return f;
    }
}