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
import java.util.HashMap;

import loci.formats.FormatTools;

import org.apache.commons.io.FileUtils;
import org.jmock.Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Ice.Current;

import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportContainer;
import ode.services.server.fire.Registry;
import ode.services.server.repo.LegacyRepositoryI;
import ode.services.server.repo.ManagedRepositoryI;
import ode.services.server.repo.RepositoryDaoImpl;
import ode.services.server.repo.path.ClientFilePathTransformer;
import ode.services.server.repo.path.FilePathRestrictionInstance;
import ode.services.server.repo.path.FilePathRestrictions;
import ode.services.server.repo.path.MakePathComponentSafe;
import ode.services.util.ReadOnlyStatus;
import ode.system.Principal;

import ode.api.AMD_RawFileStore_write;
import ode.api.AMD_StatefulServiceInterface_close;
import ode.api.RawFileStorePrx;
import ode.api.ServiceFactoryPrx;
import ode.api._RawFileStoreTie;
import ode.grid.ImportProcessPrx;
import ode.grid.ImportSettings;
import ode.model.Fileset;
import ode.model.FilesetI;
import ode.util.TempFileManager;

@Test(groups = { "integration", "repo", "fs" })
public class ManagedRepositoryITest extends AbstractServantTest {
    private final FilePathRestrictions conservativeRules =
            FilePathRestrictionInstance.getFilePathRestrictions(FilePathRestrictionInstance.values());

    FakeAdapter adapter;
    Mock adapterMock, regMock, sfMock;
    ManagedRepositoryI repo;
    LegacyRepositoryI internal;

    @Override
    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();
        adapter = new FakeAdapter();
        adapterMock = (Mock) user.ctx.getBean("adapterMock");
        adapterMock.setDefaultStub(adapter);

        sfMock = mock(ServiceFactoryPrx.class);
        sfMock.setDefaultStub(new FakeProxy(null, user.sf));

        regMock = mock(Registry.class);
        regMock.expects(atLeastOnce()).method("getInternalServiceFactory")
            .will(returnValue(sfMock.proxy()));
        Registry reg = (Registry) regMock.proxy();

        final Principal rootPrincipal = root.getPrincipal();
        final Principal userPrincipal = user.getPrincipal();
        final File targetDir = new File(odeDataDir, "ManagedRepo-"+userPrincipal.getName());
        targetDir.mkdirs();

        repo = new ManagedRepositoryI("template",
                new RepositoryDaoImpl(rootPrincipal, user.ex));
        repo.setApplicationContext(user.ctx);

        internal = new LegacyRepositoryI(user.adapter, reg, user.ex, rootPrincipal,
                targetDir.getAbsolutePath(), new ReadOnlyStatus(false, false), repo);
        internal.takeover();
    }

    @AfterClass
    protected void tearDown() throws Exception {
        if (internal != null) {
            internal.close();
        }
        super.tearDown();
    }

    protected Ice.Current curr() {
        Principal p = user.getPrincipal();
        Ice.Current ic = new Ice.Current();
        ic.ctx = new HashMap<String, String>();
        ic.ctx.put(ode.constants.SESSIONUUID.value, p.getName());
        ic.id = new Ice.Identity();
        ic.id.category = "fake";
        ic.id.name = p.getName();
        return ic;
    }

    /**
     * Generate a multi-file fake data set by touching "test.fake" and then
     * converting that into a multi-file format (here, ics). Thanks, Melissa.
     *
     * @param dir Directory in which the fakes are created.
     * @return {@link File} object for one of the two files that can be imported.
     * @throws Exception
     */
    protected ImportContainer makeFake(File dir) throws Exception {
        File fake = new File(dir, "test.fake");
        File ids = new File(dir, "test.ids");
        File ics = new File(dir, "test.ics");
        FileUtils.touch(fake);
        FormatTools.convert(fake.getAbsolutePath(), ids.getAbsolutePath());
        ImportContainer ic = new ImportContainer(ids, null /*target*/,
                null /*user pixels */,
                null /*reader*/, new String[]{ids.getAbsolutePath(), ics.getAbsolutePath()},
                Boolean.FALSE /*spw*/);
        return ic;
    }

    public void testBasicImportExample() throws Exception {
        final ClientFilePathTransformer clientPaths = 
                new ClientFilePathTransformer(new MakePathComponentSafe(this.conservativeRules));
        
        File tmpDir = TempFileManager.create_path("mydata.", ".dir", true);
        ImportContainer ic = makeFake(tmpDir);
        ImportSettings settings = new ImportSettings();
        Fileset fs = new FilesetI();
        ic.fillData(new ImportConfig(), settings, fs, clientPaths);
        final Current curr = curr();
        settings.checksumAlgorithm = repo.suggestChecksumAlgorithm(repo.listChecksumAlgorithms(curr), curr);

        ImportProcessPrx i = repo.importFileset(fs, settings, curr);
        assertNotNull(i);

        upload(i.getUploader(0));
        upload(i.getUploader(1));

        // FIXME: TBD

    }

    void upload(RawFileStorePrx prx) throws Exception {
        final Exception[] ex = new Exception[1];
        final _RawFileStoreTie file = (_RawFileStoreTie) adapter.findByProxy(prx);
        try {
            file.write_async(new AMD_RawFileStore_write(){
                public void ice_response() {
                    // no-op
                }

                public void ice_exception(Exception ex2) {
                    ex[0] = ex2;
                }}, new byte[]{0}, 0, 1, curr());
        } finally {
            file.close_async(new AMD_StatefulServiceInterface_close(){

                public void ice_response() {
                    // no-op
                }

                public void ice_exception(Exception ex2) {
                    if (ex[0] == null) {
                        ex[0] = ex2;
                    } else {
                        ex2.printStackTrace();
                    }
                }}, curr());
        }
        if (ex[0] != null) {
            throw ex[0];
        }

    }
}