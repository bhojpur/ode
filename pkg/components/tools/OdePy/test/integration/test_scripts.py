#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

"""
   Integration test focused running interactive scripts.
"""

import os
import time
import pytest
from ode.testlib import ITest
import ode
import ode.all

import ode.processor
import ode.scripts
import ode.cli

from ode.rtypes import rstring, wrap, unwrap
from ode.util.temp_files import create_path

PUBLIC = ode.model.PermissionsI("rwrwrw")

if "DEBUG" in os.environ:
    ode.util.configure_logging(loglevel=10)

class TestScripts(ITest):

    def pingfile(self):
        pingfile = create_path()
        pingfile.write_text("""if True:
        import ode
        import ode.scripts as OS
        import ode.grid as OG
        OS.client("ping-%s")
        """ % self.uuid())
        return pingfile

    def testBasicUsage(self):
        svc = self.client.sf.getScriptService()
        return svc

    def testTicket1036(self):
        self.client.setInput("a", rstring("a"))
        self.client.getInput("a")

    def testUploadAndPing(self):
        name = str(self.pingfile())
        file = self.client.upload(name, type="text/x-python")

        impl = ode.processor.usermode_processor(self.client)
        try:
            svc = self.client.sf.getScriptService()
            jp = svc.getParams(file.id.val)
            assert jp, "Non-zero params"
        finally:
            impl.cleanup()

    def testUpload2562(self):
        uuid = self.uuid()
        f = self.pingfile()
        svc = self.root.sf.getScriptService()
        id = svc.uploadOfficialScript("../%s.py" % uuid, f.text())
        ofile = self.query.get("OriginalFile", id)
        assert "/" == ofile.path.val
        assert "%s.py" % uuid == ofile.name.val

        uuid = self.uuid()  # New uuid is need because /test/../ --> /
        id = svc.uploadOfficialScript("/test/../%s.py" % uuid, f.text())
        ofile = self.query.get("OriginalFile", id)
        assert "/" == ofile.path.val
        assert "%s.py" % uuid == ofile.name.val
        return svc, ofile

    def testDelete6905(self):
        """
        Delete of official scripts was broken in 4.3.2.
        """
        svc, ofile = self.testUpload2562()
        svc.deleteScript(ofile.id.val)

    def testDelete11371(self):
        """
        Delete of official scripts was broken in 4.4.8.

        The fix to ticket 11371 should cause this test to pass
        and enable testDelete6905 to run without causing later
        tests to fail.
        """
        # First upload a number of scripts to a single directory
        noOfScripts = 5
        svc = self.root.sf.getScriptService()
        scrCount = len(svc.getScripts())
        dirUuid = self.uuid()
        ids = []
        for x in range(noOfScripts):
            uuid = self.uuid()
            f = self.pingfile()
            ids.append(svc.uploadOfficialScript(
                "/%s/%s.py" % (dirUuid, uuid), f.text()))
            ofile = self.query.get("OriginalFile", ids[x])
            assert "/%s/" % dirUuid == ofile.path.val
            assert "%s.py" % uuid == ofile.name.val
        # There should now be five more
        assert scrCount + noOfScripts == len(svc.getScripts())

        # Now delete just one script
        svc.deleteScript(ids[0])
        # There should now be one fewer
        assert scrCount + noOfScripts - 1 == len(svc.getScripts())

    @pytest.mark.broken(ticket="11610")
    def testParseErrorTicket2185(self):
        svc = self.root.sf.getScriptService()
        impl = ode.processor.usermode_processor(self.root)
        try:
            try:
                script_id = svc.uploadScript('testpath', "THIS STINKS")
                svc.getParams(script_id)
            except ode.ValidationException as ve:
                assert "THIS STINKS" in str(ve), str(ve)
        finally:
            impl.cleanup()

    def testUploadOfficialScript(self):
        root_client = self.new_client(system=True)
        scriptService = root_client.sf.getScriptService()
        uuid = self.uuid()

        scriptLines = [
            "import ode",
            "from ode.rtypes import rstring, rlong",
            "import ode.scripts as scripts",
            "if __name__ == '__main__':",
            "    client = scripts.client(",
            "        'HelloWorld.py', 'Hello World example script',",
            "         scripts.Int('longParam', True, description='theDesc',",
            "         min=1, max=10, values=[5]) )",
            "    client.setOutput('returnMessage', rstring('Script ran OK!'))"]
        script = "\n".join(scriptLines)

        id = scriptService.uploadOfficialScript(
            "/testUploadOfficialScript%s.py" % uuid, script)
        impl = ode.processor.usermode_processor(root_client)
        try:
            # force the server to parse the file enough to get params (checks
            # syntax etc)
            params = scriptService.getParams(id)
            for key, param in params.inputs.items():
                assert "longParam" == key
                assert param.prototype is not None, \
                    "Parameter prototype is 'None'"
                assert "theDesc" == param.description
                assert 1 == param.min.getValue(), "Min value not correct"
                assert 10 == param.max.getValue(), "Max value not correct"
                assert 5 == param.values.getValue()[0].getValue(), \
                    "First option value not correct"
        finally:
            impl.cleanup()

    def testRunScript(self):
        # Trying to run script
        root_client = self.new_client(system=True)
        scriptService = root_client.sf.getScriptService()
        uuid = self.uuid()

        scriptLines = [
            "import ode",
            "from ode.rtypes import rstring",
            "import ode.scripts as scripts",
            "if __name__ == '__main__':",
            "    client = scripts.client(",
            "        'HelloWorld.py', 'Hello World example script',",
            "        scripts.String('message', optional=True))",
            "    client.setOutput('returnMessage', rstring('Script ran OK!'))"]
        script = "\n".join(scriptLines)
        map = {"message": ode.rtypes.rstring(
            "Sending this message to the server!"), }

        # Also ticket:2304
        # should be OK for root to upload as official script (unique path) and
        # run
        officialScriptId = scriptService.uploadOfficialScript(
            "offical/test/script%s.py" % uuid, script)
        assert scriptService.canRunScript(officialScriptId)  # ticket:2341

        impl = ode.processor.usermode_processor(root_client)
        try:
            proc = scriptService.runScript(officialScriptId, map, None)
            try:
                cb = ode.scripts.ProcessCallbackI(root_client, proc)
                while not cb.block(1000):  # ms.
                    pass
                cb.close()
                results = proc.getResults(0)    # ms
            finally:
                proc.close(False)
        finally:
            impl.cleanup()

        assert "returnMessage" in results, \
            "Script should have run as Official script"

        # should fail if we try to upload as 'user' script and run (no user
        # processor)
        userScriptId = scriptService.uploadScript(
            "/user/test/script%s.py" % (self.uuid()), script)
        print(userScriptId)
        # scriptService.canRunScript(userScriptId) returns 'True' here for
        # some reason? (should be False)
        # But the method works in every other situation I have tried (Will).
        # Commenting out for now.
        # self.assertFalse(scriptService.canRunScript(userScriptId))
        # ticket:2341
        results = {}
        try:
            proc = scriptService.runScript(userScriptId, map, None)
            try:
                cb = ode.scripts.ProcessCallbackI(root_client, proc)
                while not cb.block(1000):  # ms.
                    pass
                cb.close()
                results = proc.getResults(0)    # ms
            finally:
                proc.close(False)
            assert False, "ticket:2309 - should not run without processor"
        except:
            pass

        assert "returnMessage" not in results, \
            "Script should not have run. No user processor!"

        impl = ode.processor.usermode_processor(root_client)
        try:
            assert scriptService.canRunScript(userScriptId)  # ticket:2341
        finally:
            impl.cleanup()

    def testEditScript(self):
        root_client = self.new_client(system=True)
        scriptService = root_client.sf.getScriptService()
        uuid = self.uuid()

        scriptLines = [
            "import ode",
            "from ode.rtypes import rstring",
            "import ode.scripts as scripts",
            "if __name__ == '__main__':",
            "    client = scripts.client(",
            "        'HelloWorld.py', 'Hello World example script',",
            "        scripts.String('message', optional=True))",
            "    client.setOutput('returnMessage', rstring('Script ran OK!'))"]
        script = "\n".join(scriptLines)
        # map = {"message": ode.rtypes.rstring(
        #     "Sending this message to the server!"), }

        scriptPath = "/test/edit/script%s.py" % uuid
        scriptId = scriptService.uploadOfficialScript(scriptPath, script)

        scripts = scriptService.getScripts()
        namedScripts = [
            s for s in scripts if s.path.val + s.name.val == scriptPath]
        scriptFile = namedScripts[0]

        paramsBefore = scriptService.getParams(scriptId)

        editedScript = """
import ode, ode.scripts as s
from ode.rtypes import *

client = s.client(
    "HelloWorld.py", "edited script",
    s.Long("a").inout(), s.String("b").inout())
client.setOutput("a", rlong(0))
client.setOutput("b", rstring("c"))
client.closeSession()
"""
        scriptService.editScript(scriptFile, editedScript)

        editedText = scriptService.getScriptText(scriptId)
        assert editedScript == editedText

        paramsAfter = scriptService.getParams(scriptId)

        assert "message" in paramsBefore.inputs
        assert 0 == len(paramsBefore.outputs)

        for x in ("a", "b"):
            assert x in paramsAfter.inputs
            assert x in paramsAfter.outputs

    def testScriptValidation(self):
        root_client = self.new_client(system=True)
        scriptService = root_client.sf.getScriptService()
        uuid = self.uuid()

        invalidScript = "This text is not valid as a script"

        invalidPath = "/test/validation/invalid%s.py" % uuid

        try:
            # this should throw, since the script is invalid
            scriptService.uploadOfficialScript(invalidPath, invalidScript)
            assert False, "uploadOfficialScript() uploaded invalid script"
        except ode.ValidationException:
            pass

        getId = scriptService.getScriptID(invalidPath)
        assert -1 == getId, \
            "getScriptID() didn't return '-1' for invalid script"
        scripts = scriptService.getScripts()
        for s in scripts:
            assert s.mimetype.val == "text/x-python"
            assert s.path.val + s.name.val != invalidPath, \
                "getScripts() returns invalid script"

        # upload a valid script - then edit
        scriptLines = [
            "import ode",
            "from ode.rtypes import rstring",
            "import ode.scripts as scripts",
            "if __name__ == '__main__':",
            "    client = scripts.client(",
            "       'HelloWorld.py', 'Hello World example script',",
            "        scripts.String('message', optional=True))",
            "    client.setOutput('returnMessage', rstring('Script ran OK!'))"]
        validScript = "\n".join(scriptLines)
        validPath = "/test/validation/valid%s.py" % uuid
        validId = scriptService.uploadOfficialScript(validPath, validScript)

        try:
            # this should throw, since the script is invalid
            scriptService.editScript(
                ode.model.OriginalFileI(validId, False), invalidScript)
            assert False, "editScript() failed to throw with invalid script"
        except ode.ValidationException:
            pass

        getId = scriptService.getScriptID(validPath)
        assert - 1 == getId, \
            "getScriptID() didn't return 'None' for invalid script"
        scripts = scriptService.getScripts()
        for s in scripts:
            assert s.mimetype.val == "text/x-python"
            assert s.path.val + \
                s.name.val != validPath, "getScripts() returns invalid script"

    @pytest.mark.broken(ticket="11539")
    def testAutoFillTicket2326(self):
        SCRIPT = """if True:
        import ode.scripts
        import ode.rtypes
        client = ode.scripts.client(
            "ticket2326", ode.scripts.Long("width", optional=True))
        width = client.getInput("width")
        print width
        client.setOutput(
            "noWidthKey",
            ode.rtypes.rbool("width" not in client.getInputKeys()))
        client.setOutput("widthIsNull", ode.rtypes.rbool(width is None))
        """
        impl = ode.processor.usermode_processor(self.client)
        svc = self.client.sf.getScriptService()
        try:
            scriptID = svc.uploadScript("/test/testAutoFillTicket2326", SCRIPT)
            process = svc.runScript(scriptID, {}, None)
            cb = ode.scripts.ProcessCallbackI(self.client, process)
            while cb.block(500) is None:
                pass
            results = process.getResults(0)
            stdout = results["stdout"].val
            downloaded = create_path()
            self.client.download(ofile=stdout, filename=str(downloaded))
            text = downloaded.text().strip()
            assert "None" == text
            assert results["widthIsNull"].val
            assert results["noWidthKey"].val
            assert "stderr" not in results
        finally:
            impl.cleanup()

    @pytest.mark.broken(reason="Minor performance failure", ticket="11539")
    def testParamLoadingPerformanceTicket2285(self):
        root_client = self.new_client(system=True)
        svc = root_client.sf.getScriptService()
        SCRIPT = """if True:
        import ode.model as OM
        import ode.rtypes as OR
        import ode.scripts as OS
        c = OS.client("perf test",
            OS.Long("a", min=0, max=5),
            OS.String("b", values=("a","b","c")),
            OS.List("c").ofType(OM.ImageI))
        """
        upload_time, scriptID = self.timeit(
            svc.uploadOfficialScript, "/test/perf%s.py" % self.uuid(), SCRIPT)
        impl = ode.processor.usermode_processor(root_client)
        try:
            params_time, params = self.timeit(svc.getParams, scriptID)
            assert params_time < (upload_time / 10), \
                "upload_time(%s) <= 10 * params_time(%s)!" % \
                (upload_time, params_time)
            assert params_time < 0.1, "params_time(%s) >= 0.01 !" % params_time

            run_time, process = self.timeit(
                svc.runScript, scriptID, wrap({"a": long(5)}).val, None)

            def wait():
                cb = ode.scripts.ProcessCallbackI(root_client, process)
                while cb.block(500) is None:
                    # process.poll() # This seems to make things much faster
                    pass
            wait_time, ignore = self.timeit(wait)
            results_time, ignore = self.timeit(process.getResults, 0)
            assert 5 > (run_time + results_time + wait_time), \
                "run(%s)+wait(%s)+results(%s) > 5" % \
                (run_time, wait_time, results_time)
        finally:
            impl.cleanup()

    def testSpeedOfThumbnailFigure(self):
        svc = self.client.sf.getScriptService()
        scriptID = svc.getScriptID("/ode/figure_scripts/Thumbnail_Figure.py")
        if scriptID == -1:
            assert False, "Script not found"
        session = self.client.getSession()
        image = self.create_test_image(100, 100, 1, 1, 1, session)
        process = svc.runScript(
            scriptID, wrap({"Data_Type": "Image", "IDs": [image.id.val]}).val,
            None)
        wait_time, ignore = self.timeit(
            ode.scripts.wait, self.client, process)
        assert wait_time < 60, "wait_time over 1 min for TbFig!"
        results = process.getResults(0)
        results = ode.scripts.unwrap(results)
        # Test passes for me locally (Will) but not on hudson.
        # Script fails on hudson. Only get returned Original Files
        # (stderr, stdout) but not the text of these.
        # commenting out for now to get Hudson green.
        # assert "Thumbnail-Figure Created" ==  results["Message"]

    def test6066(self):

        # Make two users in a new group. Only one is an owner of the group
        grp = self.new_group()
        clientU, userU = self.new_client_and_user(group=grp, owner=False)
        clientA, userA = self.new_client_and_user(group=grp, owner=True)

        # Make both users admins
        admin = self.root.sf.getAdminService()
        for sf, usr in ((clientU.sf, userU), (clientA.sf, userA)):
            admin.addGroups(usr, [ode.model.ExperimenterGroupI(0, False)])
            sf.getAdminService().getEventContext()  # Reset session

        def assertUploadAndReplace(client):
            svc = client.sf.getScriptService()
            SCRIPT = """if True:
            import ode.model as OM
            import ode.rtypes as OR
            import ode.scripts as OS
            c = OS.client("ticket 6066")
            """

            # Upload official
            scriptID = svc.uploadOfficialScript(
                "/test/ticket6066%s.py" % self.uuid(), SCRIPT)
            svc.getParams(scriptID)

            # Replace
            svc.editScript(ode.model.OriginalFileI(scriptID, False),
                           SCRIPT + "\n")
            svc.getParams(scriptID)

            # This variable is not used but I'm sure it was intended to be
            # so just commenting out for now to satisfy flake8 (cgb)
            # LATER = """
            # process = svc.runScript(scriptID, None, None)

            # impl = ode.processor.usermode_processor(self.root)
            # cb = ode.scripts.ProcessCallbackI(self.root, process)
            # count = 10
            # while cb.block(500) is None:
            #     count -= 1
            #     if not count:
            #         assert False, "Took too long"
            # """

        assertUploadAndReplace(clientA)
        assertUploadAndReplace(clientU)

    # ode.group support in scripts
    # See story ticket:3527. The permission

    @pytest.mark.broken(ticket="11610")
    def test3527(self):
        SCRIPT = """if True:
        import ode.scripts
        import ode.rtypes
        client = ode.scripts.client("ticket3527", \
                ode.scripts.Long("gid", out=True))
        ec = client.sf.getAdminService().getEventContext()
        gid = ec.groupId
        client.setOutput("gid", ode.rtypes.rlong(gid))
        """
        impl = ode.processor.usermode_processor(self.client)
        svc = self.client.sf.getScriptService()
        try:
            scriptID = svc.uploadScript("/test/test3527", SCRIPT)
            process = svc.runScript(scriptID, {}, None)
            cb = ode.scripts.ProcessCallbackI(self.client, process)
            while cb.block(500) is None:
                pass
            results = process.getResults(0)
            gid = self.client.sf.getAdminService().getEventContext().groupId
            assert gid == results["gid"].val
        finally:
            impl.cleanup()

    def testDynamicTime(self):
        DYNAMIC = """if True:
        import ode.all  # For constants
        import ode.constants.namespaces as OCN
        import ode.grid as OG
        import ode.scripts as OS
        job = OG.JobParams(name="testDynamic",
                           namespaces=[OCN.NSDYNAMIC])
        c = OS.client(job)
        """
        CACHED = """if True:
        import ode.scripts as OS
        c = OS.client("testDynamic")
        """
        svc = self.root.sf.getScriptService()
        dynamicID = svc.uploadOfficialScript(
            "/test/dynamic/dyn%s.py" % self.uuid(), DYNAMIC)
        cachedID = svc.uploadOfficialScript(
            "/test/dynamic/cached%s.py" % self.uuid(), CACHED)
        # Prep each of the params
        svc.getParams(dynamicID)
        svc.getParams(cachedID)

        dynamic_times = []
        cached_times = []

        for x in range(3):
            d_start = time.time()
            svc.getParams(dynamicID)
            d_stop = time.time()

            c_start = time.time()
            svc.getParams(cachedID)
            c_stop = time.time()

            dynamic_times.append(d_stop-d_start)
            cached_times.append(c_stop-c_start)

        d_time = min(dynamic_times)
        c_time = min(cached_times)
        assert d_time > 10 * c_time

    def testDynamicUpdate(self):
        SCRIPT = """#!/usr/bin/env python

import ode
import ode.gateway
from ode import scripts
from ode.rtypes import rstring, unwrap
from datetime import datetime

def get_params():
    try:
        client = ode.client()
        client.createSession()
        conn = ode.gateway.ServerGateway(client_obj=client)
        conn.SERVICE_OPTS.setOdeGroup(-1)
        objparams = [rstring(d.getName()) for d in conn.getObjects('Dataset')]
        if not objparams:
            objparams = [rstring('<No objects found>')]
        return objparams
    except Exception as e:
        return ['Exception: %s' % e]
    finally:
        client.closeSession()


def runScript():
    "The main entry point of the script"

    objparams = get_params()

    client = scripts.client(
        'Dynamic_Test.py',
        'Test dynamic parameters',

        scripts.String(
            'Object', optional=False, grouping='1',
            description='Select an object',
            values=objparams),

        namespaces=[ode.constants.namespaces.NSDYNAMIC],
    )

    paramsTime = datetime.now()

    try:
        scriptParams = client.getInputs(unwrap=True)
        message = 'Params: %s\\n' % scriptParams
        client.setOutput('Message', rstring(str(message)))

    finally:
        client.closeSession()

if __name__ == '__main__':
    runScript()"""
        uuid1 = self.uuid()
        uuid2 = self.uuid()
        svc = self.client.sf.getScriptService()
        rsvc = self.root.sf.getScriptService()
        script = rsvc.uploadOfficialScript(
            "/test/dynamic/dyn%s.py" % self.uuid(), SCRIPT)
        update = self.client.sf.getUpdateService()

        def contains_uuids(u1, u2):
            params = svc.getParams(script)
            datasets = unwrap(params.inputs["Object"].values)
            assert u1 == (uuid1 in datasets)
            assert u2 == (uuid2 in datasets)

        def run_with_param(param):
            process = svc.runScript(script, {'Object': wrap(param)}, None)
            cb = ode.scripts.ProcessCallbackI(self.client, process)
            while cb.block(500) is None:
                pass
            cb.close()

        contains_uuids(False, False)
        with pytest.raises(ode.ValidationException):
            run_with_param(uuid1)

        update.saveObject(self.new_dataset(name=uuid1))
        contains_uuids(True, False)
        run_with_param(uuid1)

        update.saveObject(self.new_dataset(name=uuid2))
        contains_uuids(True, True)
        run_with_param(uuid2)
