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

import ode.services.server.fire.Registry;
import ode.services.server.repo.InternalRepositoryI;
import ode.services.server.repo.PublicRepositoryI;
import ode.services.server.repo.RepositoryDaoImpl;
import ode.services.server.repo.path.FilePathRestrictionInstance;
import ode.services.util.Executor;
import ode.services.util.ReadOnlyStatus;
import ode.system.OdeContext;
import ode.system.Principal;
import ode.util.checksum.ChecksumProviderFactory;
import ode.util.TempFileManager;

import org.jmock.MockObjectTestCase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Ice.ObjectAdapter;

/**
 *
 */
public class StandaloneRepositoryTest extends MockObjectTestCase {

    OdeContext ctx;
    MockFixture fixture;
    ObjectAdapter oa;
    Registry reg;
    Executor ex;
    ChecksumProviderFactory cpf;
    File dir;

    @BeforeClass(groups = "integration")
    public void setup() throws Exception {
        dir = TempFileManager.create_path("repo", "test", true);
        System.setProperty("ode.repo.dir", dir.getAbsolutePath());
        ctx = OdeContext.getInstance("ODE.repository");
        ex = (Executor) ctx.getBean("executor");
        cpf = (ChecksumProviderFactory) ctx.getBean("checksumProviderFactory");
        fixture = new MockFixture(this, ctx);
        oa = fixture.server.getServerAdapter();
        reg = fixture.server.getRegistry();
    }

    @Test(groups = "integration")
    public void testSimple() throws Exception {
        Principal p = new Principal("mock-uuid");
        InternalRepositoryI repo = new InternalRepositoryI(oa, reg, ex,
                p, dir.getAbsolutePath(), new ReadOnlyStatus(false, false),
                new PublicRepositoryI(new RepositoryDaoImpl(p, ex), cpf, null,
                        FilePathRestrictionInstance.UNIX_REQUIRED.name));
    }

}