package ode.services.server.test.mock;

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

import ode.model.core.OriginalFile;
import ode.model.internal.Permissions;
import ode.services.server.fire.Registry;
import ode.services.server.repo.PublicRepositoryI;
import ode.services.server.repo.RepositoryDaoImpl;
import ode.services.server.repo.TemporaryRepositoryI;
import ode.services.server.repo.path.FilePathRestrictionInstance;
import ode.services.util.Executor;
import ode.system.Principal;
import ode.testing.MockServiceFactory;
import ode.util.checksum.ChecksumProviderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Ice.ObjectAdapter;

/**
 *
 */
public class TemporaryRepositoryTest extends MockObjectTestCase {

    MockFixture fixture;
    ObjectAdapter oa;
    Registry reg;
    ChecksumProviderFactory cpf;

    @BeforeClass(groups = "integration")
    public void setup() throws Exception {
        fixture = new MockFixture(this, "ODE.mock");
        oa = fixture.server.getServerAdapter();
        // reg = fixture.server.getRegistry();
        Mock mockReg = mock(Registry.class);
        reg = (Registry) mockReg.proxy();
        mockReg.expects(atLeastOnce()).method("addObject");
        Mock mockCpf = mock(ChecksumProviderFactory.class);
        cpf = (ChecksumProviderFactory) mockCpf.proxy();
        mockCpf.expects(atLeastOnce()).method("getProvider()");
    }

    @Test(groups = "integration")
    public void testSimple() throws Exception {
        final OriginalFile repo = new OriginalFile();
        repo.setId(1L);
        repo.getDetails().setPermissions(Permissions.WORLD_IMMUTABLE);
        final MockServiceFactory sf = new MockServiceFactory();
        sf.mockConfig = fixture.mock("mock-ode.api.IConfig");
        sf.mockConfig.expects(once()).method("getDatabaseUuid").will(
                returnValue("mock-db-uuid"));
        sf.mockUpdate = fixture.mock("mock-ode.api.IUpdate");
        sf.mockUpdate.expects(once()).method("saveAndReturnObject").will(
                returnValue(repo));
        sf.mockQuery = fixture.mock("mock-ode.api.IQuery");
        sf.mockQuery.expects(once()).method("findByString").will(
                returnValue(repo));
        sf.mockQuery.expects(once()).method("findByQuery").will(
                returnValue(repo));

        Principal p = new Principal("session");
        TemporaryRepositoryI tr = new TemporaryRepositoryI(oa, reg, fixture.ex,
                p, new PublicRepositoryI(new RepositoryDaoImpl(p, fixture.ex),
                        cpf, null, FilePathRestrictionInstance.UNIX_REQUIRED.name));
        fixture.mock("executorMock").expects(atLeastOnce()).method("execute")
                .will(new Stub() {

                    public Object invoke(Invocation arg0) throws Throwable {
                        return ((Executor.Work) arg0.parameterValues.get(1))
                                .doWork(null, sf);
                    }

                    public StringBuffer describeTo(StringBuffer arg0) {
                        arg0.append(" calls doWork");
                        return arg0;
                    }
                });
        assertTrue(tr.takeover());
        assertNotNull(tr.getDescription());
    }

    @Test(groups = "integration")
    public void testFileUtils() throws Exception {
        String tmpPath = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmpPath).getAbsoluteFile();
        tmpDir.list();
        System.out.println(FileUtils.listFiles(tmpDir, FileFilterUtils
                .fileFileFilter(), FileFilterUtils.falseFileFilter()));

    }
}