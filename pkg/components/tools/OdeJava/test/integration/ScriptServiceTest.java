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

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import ode.services.scripts.ScriptRepoHelper;

import ode.SecurityViolation;
import ode.ValidationException;
import ode.api.IScriptPrx;
import ode.api.RawFileStorePrx;
import ode.gateway.util.Requests;
import ode.grid.JobParams;
import ode.grid.RepositoryMap;
import ode.grid.RepositoryPrx;
import ode.model.IObject;
import ode.model.OriginalFile;
import ode.model.OriginalFileI;
import ode.sys.EventContext;
import ode.sys.ParametersI;

import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Collections of tests for the <code>Script</code> service.
 */
public class ScriptServiceTest extends AbstractServerTest {

    /** The mimetype of the lookup table files.*/
    static final String LUT_MIMETYPE = "text/x-lut";

    /** The mimetype of Python scripts. */
    static final String PYTHON_MIMETYPE = "text/x-python";

    /**
     * Tests to make sure that a new entry for the same file is not added
     * to the originalFile table.
     * @throws Exception Thrown if an error occurred.
     */
    @Test
    public void testDuplicateEntries() throws Exception {
        IScriptPrx svc = factory.getScriptService();
        List<OriginalFile> scripts = svc.getScriptsByMimetype(LUT_MIMETYPE);
        Assert.assertNotNull(scripts);
        int n = scripts.size();
        ParametersI param = new ParametersI();
        param.add("m", ode.rtypes.rstring(LUT_MIMETYPE));
        String sql = "select f from OriginalFile as f "
                + "where f.mimetype = :m";
        List<IObject> values = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(values.size(), n);
    }

    /**
     * Tests the retrieval of the scripts using the <code>getScripts</code>
     * method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetScripts() throws Exception {
        IScriptPrx svc = factory.getScriptService();
        List<OriginalFile> scripts = svc.getScripts();
        Assert.assertNotNull(scripts);
        Assert.assertTrue(CollectionUtils.isNotEmpty(scripts));
        Iterator<OriginalFile> i = scripts.iterator();
        OriginalFile f;
        while (i.hasNext()) {
            f = i.next();
            Assert.assertNotNull(f);
            if (LUT_MIMETYPE.equals(f.getMimetype().getValue())) {
                Assert.fail("Lut should not be returned.");
            }
        }
        //do it twice since we had initially a bug in loading
        scripts = svc.getScripts();
        Assert.assertNotNull(scripts);
        Assert.assertTrue(CollectionUtils.isNotEmpty(scripts));
        i = scripts.iterator();
        while (i.hasNext()) {
            f = i.next();
            Assert.assertNotNull(f);
            if (LUT_MIMETYPE.equals(f.getMimetype().getValue())) {
                Assert.fail("Lut should not be returned.");
            }
        }
    }

    /**
     * Tests the retrieval of the scripts using the <code>getScriptsByMimetype</code>
     * method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testGetScriptsByMimetype() throws Exception {
        IScriptPrx svc = factory.getScriptService();
        List<OriginalFile> scripts = svc.getScriptsByMimetype(LUT_MIMETYPE);
        Assert.assertNotNull(scripts);
        Assert.assertTrue(CollectionUtils.isNotEmpty(scripts));
        Iterator<OriginalFile> i = scripts.iterator();
        OriginalFile f;
        while (i.hasNext()) {
            f = i.next();
            Assert.assertNotNull(f);
            String mimetype = f.getMimetype().getValue();
            Assert.assertEquals(mimetype, LUT_MIMETYPE);
        }
        scripts = svc.getScriptsByMimetype(LUT_MIMETYPE);
        i = scripts.iterator();
        while (i.hasNext()) {
            f = i.next();
            Assert.assertNotNull(f);
            String mimetype = f.getMimetype().getValue();
            Assert.assertEquals(mimetype, LUT_MIMETYPE);
        }
    }
    
    /**
     * Tests the retrieval of the parameters associated to a script using the
     * <code>getParams</code> method.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     * @see #testGetScripts()
     */
    @Test
    public void testGetParams() throws Exception {
        IScriptPrx svc = factory.getScriptService();
        final List<OriginalFile> scripts = svc.getScriptsByMimetype(PYTHON_MIMETYPE);
        for (final OriginalFile f : scripts) {
            final long id = f.getId().getValue();
            final JobParams params;
            try {
                params = svc.getParams(id);
            } catch (ValidationException ve) {
                /* try another, some scripts may be bad */
                continue;
            }
            Assert.assertNotNull(params, "no parameters for script #" + id);
            /* test passed */
            return;
        }
        Assert.fail("no script parameters could be fetched");
    }

    /**
     * Tests to upload an official script by a user who is not an administrator,
     * this method uses the <code>uploadOfficialScript</code>.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testUploadOfficialScript() throws Exception {
        newUserAndGroup("rwr---");
        IScriptPrx svc = factory.getScriptService();
        int n = svc.getScripts().size();
        final String testScriptName = "Test_" + getClass().getName() + '_' + UUID.randomUUID() + ".py";
        try {
            svc.uploadOfficialScript(testScriptName, getPythonScript());
            Assert.fail("Only administrators can upload official script.");
        } catch (Exception e) {
        }
        Assert.assertEquals(svc.getScripts().size(), n);
    }

    /**
     * Tests to upload an official script by a user who is an administrator,
     * this method uses the <code>uploadOfficialScript</code>.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testUploadOfficialScriptAsRoot() throws Exception {
        logRootIntoGroup();
        IScriptPrx svc = factory.getScriptService();
        List<OriginalFile> scripts = svc.getScripts();
        final String testScriptName = "Test_" + getClass().getName() + '_' + UUID.randomUUID() + ".py";
        final long id = svc.uploadOfficialScript(testScriptName, getPythonScript());
        Assert.assertTrue(id > 0);
        Assert.assertEquals(svc.getScripts().size(), scripts.size()+1);
        deleteScript(id);
    }

    /**
     * Tests to upload an official script by a user who is an administrator,
     * this method uses the <code>deleteScript</code>.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDeleteScriptAsRoot() throws Exception {
        logRootIntoGroup();
        IScriptPrx svc = factory.getScriptService();
        List<OriginalFile> scripts = svc.getScripts();
        final String testScriptName = "Test_" + getClass().getName() + '_' + UUID.randomUUID() + ".py";
        final long id = svc.uploadOfficialScript(testScriptName, getPythonScript());
        deleteScript(id);
        Assert.assertEquals(svc.getScripts().size(), scripts.size());
        //Check that the entry has been removed from DB
        assertDoesNotExist(new OriginalFileI(id, false));
    }

    /**
     * Tests to upload a script, this method uses the <code>uploadScript</code>.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testUploadScript() throws Exception {
        newUserAndGroup("rwr---");
        IScriptPrx svc = factory.getScriptService();
        final String testScriptName = "Test_" + getClass().getName() + '_' + UUID.randomUUID() + ".py";
        final long id = svc.uploadScript(testScriptName, getPythonScript());
        Assert.assertTrue(id > 0);
    }

    /**
     * Tests to upload an official lut by a user who is an administrator,
     * this method uses the <code>uploadOfficialScript</code>.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testUploadOfficialLUTAsRoot() throws Exception {
        logRootIntoGroup();
        IScriptPrx svc = factory.getScriptService();
        List<OriginalFile> scripts = svc.getScriptsByMimetype(LUT_MIMETYPE);
        int n = scripts.size();
        OriginalFile f = scripts.get(0);
        String str = readScript(f);
        String folder = f.getName().getValue();
        long id = svc.uploadOfficialScript(folder, str);
        Assert.assertTrue(id > 0);
        Assert.assertEquals(svc.getScriptsByMimetype(LUT_MIMETYPE).size(),
                n+1);
        deleteScript(id);
    }

    /**
     * Test that writing to the script repository does not break the listing of scripts.
     * @throws Exception unexpected
     */
    @Test(groups = "broken")
    public void testGetScriptsFiltersUnreadable() throws Exception {
        final EventContext scriptOwner = newUserAndGroup("rwr---");
        /* find the script repository */
        final RepositoryMap repositories = factory.sharedResources().repositories();
        int index;
        for (index = 0; !ScriptRepoHelper.SCRIPT_REPO.equals(repositories.descriptions.get(index).getHash().getValue()); index++);
        final RepositoryPrx repo = repositories.proxies.get(index);
        /* write a script directly via the repository */
        final String scriptName = "Test_" + getClass().getName() + "_" + UUID.randomUUID() + ".py";
        final OriginalFile scriptFile = repo.register(scriptName, ode.rtypes.rstring(PYTHON_MIMETYPE));
        final byte[] scriptContent = getPythonScript().getBytes(StandardCharsets.UTF_8);
        final RawFileStorePrx rfs = repo.file(scriptName, "rw");
        rfs.write(scriptContent, 0, scriptContent.length);
        rfs.close();
        /* switch to a fresh user */
        newUserAndGroup("rwr---");
        /* try again to get a list of the Python scripts */
        try {
            factory.getScriptService().getScriptsByMimetype(PYTHON_MIMETYPE);
        } catch (SecurityViolation sv) {
            Assert.fail("should be able to get the list of accessible Python scripts", sv);
        } finally {
            /* clean up the troublesome upload */
            loginUser(scriptOwner);
            doChange(Requests.delete().target(scriptFile).build());
        }
    }

    /**
     * Delete the uploaded script.
     *
     * @param id The identifier of the script.
     * @throws Exception Thrown if an error occurred.
     */
    private void deleteScript(long id) throws Exception {
        IScriptPrx svc = factory.getScriptService();
        svc.deleteScript(id);
    }

    /**
     * Reads the specified script as a string.
     *
     * @param f The script to read.
     * @return See above.
     * @throws Exception Thrown if an error occurred.
     */
    String readScript(OriginalFile f) throws Exception {
        RawFileStorePrx store;
        byte[] values;
        store = factory.createRawFileStore();
        try {
            store.setFileId(f.getId().getValue());
            values = store.read(0, (int) f.getSize().getValue());
        } finally {
            store.close();
        }
        return new String(values, StandardCharsets.UTF_8);
    }
}
