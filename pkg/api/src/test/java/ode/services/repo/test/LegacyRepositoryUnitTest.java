package ode.services.repo.test;

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
import java.io.RandomAccessFile;
import java.util.UUID;

import ode.model.core.OriginalFile;
import ode.model.internal.Permissions;
import ode.services.server.fire.Registry;
import ode.services.server.repo.LegacyRepositoryI;
import ode.services.server.repo.PublicRepositoryI;
import ode.services.server.repo.RepositoryDaoImpl;
import ode.services.server.repo.path.FilePathRestrictionInstance;
import ode.services.util.Executor;
import ode.services.util.ReadOnlyStatus;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.testing.MockServiceFactory;
import ode.util.checksum.ChecksumProviderFactory;
import ode.grid.InternalRepositoryPrx;
import ode.model.enums.ChecksumAlgorithmAdler32;

import org.jmock.Mock;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 */
@Test(groups = { "repo" })
public class LegacyRepositoryUnitTest extends AbstractRepoUnitTest {

    Mock exMock;
    Executor ex;
    Mock oaMock;
    Ice.ObjectAdapter oa;
    Mock regMock;
    Registry reg;
    Mock mockCpf;
    ChecksumProviderFactory cpf;

    MockServiceFactory sf;

    @BeforeMethod
    protected void beforeMethod() throws Exception {
        newTmpDir();
        exMock = mock(Executor.class);
        ex = (Executor) exMock.proxy();
        oaMock = mock(Ice.ObjectAdapter.class);
        oa = (Ice.ObjectAdapter) oaMock.proxy();
        regMock = mock(Registry.class);
        reg = (Registry) regMock.proxy();
        mockCpf = mock(ChecksumProviderFactory.class);
        cpf = (ChecksumProviderFactory) mockCpf.proxy();

        sf = new MockServiceFactory();
        sf.mockConfig.expects(atLeastOnce()).method("getDatabaseUuid").will(
                returnValue("mockuuid"));
        sf.mockQuery.expects(atLeastOnce()).method("findByString").will(
                returnValue(file()));
        sf.mockQuery.expects(atLeastOnce()).method("findByQuery");
        sf.mockAdmin.expects(atLeastOnce()).method("moveToCommonSpace");

        exMock.expects(atLeastOnce()).method("execute").will(
                new ExecutorStub(sf));
        exMock.expects(atLeastOnce()).method("getContext")
                .will(returnValue(OdeContext.getManagedServerContext()));
        oaMock.expects(atLeastOnce()).method("find");
        oaMock.expects(atLeastOnce()).method("createDirectProxy");
    }

    private LegacyRepositoryI mk() throws Exception {
        return mk(reg);
    }

    private LegacyRepositoryI mk(Registry registry) throws Exception {
        Principal p = new Principal("sessionUuid", "system", "Internal");
        return new LegacyRepositoryI(oa, registry, ex, p, tmpRepo.getAbsolutePath(), new ReadOnlyStatus(false, false),
                new PublicRepositoryI(new RepositoryDaoImpl(p, ex), cpf,
                        ChecksumAlgorithmAdler32.value,
                        FilePathRestrictionInstance.UNIX_REQUIRED.name));
    }

    private OriginalFile file() {
        OriginalFile f = new OriginalFile(1L, true);
        f.getDetails().setPermissions(Permissions.WORLD_IMMUTABLE);
        f.setPath("foo");
        f.setName("bar");
        return f;
    }

    private void newRepoObject() {
        sf.mockUpdate.expects(once()).method("saveAndReturnObject").will(
                returnValue(file()));
    }

    private void addsRepoServices() {
        oaMock.expects(atLeastOnce()).method("add").will(returnValue(null));
        regMock.expects(atLeastOnce()).method("addObject");
        oaMock.expects(once()).method("activate");
    }

    public void testRepoAlredyInitialized() throws Exception {

        newRepoObject();
        addsRepoServices();
        final LegacyRepositoryI r1 = mk();
        assertTrue(r1.takeover());
        r1.close();

        addsRepoServices();
        final LegacyRepositoryI r2 = mk();
        assertTrue(r2.takeover());
        r2.close();
    }

    public void testNoTakeoverAfterClose() throws Exception {

        newRepoObject();
        addsRepoServices();
        final LegacyRepositoryI r1 = mk();
        assertTrue(r1.takeover());
        r1.close();
        assertFalse(r1.takeover());
        assertFalse(r1.takeover());
        assertFalse(r1.takeover());

    }

    public void testTakeoverOnlyOnce() throws Exception {
        newRepoObject();
        addsRepoServices();
        LegacyRepositoryI r1 = mk();
        assertTrue(r1.takeover());
        assertFalse(r1.takeover());
    }

    public void testLocking() throws Exception {
        newRepoObject();
        addsRepoServices();

        final LegacyRepositoryI r1 = mk();
        final LegacyRepositoryI r2 = mk();

        assertTrue(r1.takeover());

        class ShouldHang extends Thread {
            boolean tookOver = false;

            @Override
            public void run() {
                r2.takeover();
                tookOver = true;
            }
        }

        ShouldHang t = new ShouldHang();
        t.join(2000);
        assertFalse(t.tookOver);

    }

    public void testRaf() throws Exception {

        RandomAccessFile raf;
        String test;
        String uuid = UUID.randomUUID().toString();
        File blank = new File(tmpRepo, "blank.raf");
        File existing = new File(tmpRepo, "existing.raf");

        // Test blank RAF
        raf = new RandomAccessFile(blank, "rw");
        try {
            test = raf.readUTF();
            fail("EOF");
        } catch (java.io.EOFException eof) {
            // ok
        }

        // Test existing RAF
        raf = new RandomAccessFile(existing, "rw");
        raf.writeUTF(uuid);
        raf.close();
        raf = new RandomAccessFile(existing, "rw");
        test = raf.readUTF();
        assertEquals(uuid, test);
    }

    public void testTakeoverNoRepositoryPrx() throws Exception {
        newRepoObject();
        addsRepoServices();

        InternalRepositoryPrx[] repos = {};
        regMock.expects(atLeastOnce()).method("lookupRepositories")
                .will(returnValue(repos));

        final LegacyRepositoryI r1 = mk(reg);

        assertTrue(r1.takeover());
    }

    class ExecutorStub implements Stub {

        ServiceFactory sf;

        public ExecutorStub(ServiceFactory sf) {
            this.sf = sf;
        }

        public Object invoke(Invocation arg0) throws Throwable {
            Principal p = (Principal) arg0.parameterValues.get(0);
            Executor.SimpleWork work = (Executor.SimpleWork) arg0.parameterValues
                    .get(1);
            return work.doWork(null, this.sf);
        }

        public StringBuffer describeTo(StringBuffer arg0) {
            arg0.append("calls execute");
            return arg0;
        }
    }
}