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
   pytest fixtures used as defined in conftest.py:
   - gatewaywrapper
"""

import ode
import ode.scripts
from ode.rtypes import rstring, rtime, wrap
from ode.gateway.scripts import dbhelpers
import time
import pytest

PRIVATE = 'rw----'
READONLY = 'rwr---'
READANN = 'rwra--'
READWRITE = 'rwrw--'

class TestHistory (object):
    @pytest.fixture(autouse=True)
    def setUp(self):
        """ Create a group with User """
        dbhelpers.USERS['history_test_user'] = dbhelpers.UserEntry(
            'history_test_user', 'ome',
            firstname='history', lastname='user',
            groupname="rw_history", groupperms=READWRITE)
        dbhelpers.bootstrap(onlyUsers=True)

    def searchHistory(self, gateway, start, end, dtype="Dataset"):

        tm = gateway.getTimelineService()
        count = tm.countByPeriod([dtype], rtime(long(start)),
                                 rtime(long(end)), None, gateway.SERVICE_OPTS)
        data = tm.getByPeriod([dtype], rtime(long(start)), rtime(long(end)),
                              None, True, gateway.SERVICE_OPTS)

        logs = tm.getEventLogsByPeriod(rtime(start), rtime(end), None,
                                       gateway.SERVICE_OPTS)
        entityType = 'ome.model.containers.%s' % dtype
        filteredLogs = [{'id': i.entityId.val, 'action': i.action.val}
                        for i in logs if i.entityType.val == entityType]

        typeCount = count[dtype]
        dataCount = len(data[dtype])
        logCount = len(filteredLogs)

        assert typeCount == dataCount, \
            "Period count should match number of objects"
        assert logCount == dataCount, \
            "Logs count should match number of objects"

    @pytest.mark.broken(ticket="11494")
    def testCreateHistory(self, gatewaywrapper):

        # Login as user...
        gatewaywrapper.doLogin(dbhelpers.USERS['history_test_user'])
        userId = gatewaywrapper.gateway.getEventContext().userId
        uuid = gatewaywrapper.gateway.getEventContext().sessionUuid
        default_groupId = gatewaywrapper.gateway.getEventContext().groupId

        start = int(round(time.time() * 1000)) - 1000

        # Create Dataset in 'default' group
        update = gatewaywrapper.gateway.getUpdateService()
        new_ds = ode.model.DatasetI()
        dataset_name = "history_test_%s" % uuid
        new_ds.name = rstring(dataset_name)
        new_ds = update.saveAndReturnObject(new_ds)
        new_ds_Id = new_ds.id.val

        # As Admin, create a second group with this user & upload script
        gatewaywrapper.loginAsAdmin()
        gid = gatewaywrapper.gateway.createGroup(
            "history-test-%s" % uuid, member_Ids=[userId], perms=READWRITE)

        # login as User
        gatewaywrapper.doLogin(dbhelpers.USERS['history_test_user'])

        end = int(round(time.time() * 1000)) + 1000
        self.searchHistory(gatewaywrapper.gateway, start, end)

        # switch user into new group
        switched = gatewaywrapper.gateway.c.sf.setSecurityContext(
            ode.model.ExperimenterGroupI(gid, False))
        assert switched, "Failed to switch into new group"
        # Shouldn't be able to access Dataset...
        self.searchHistory(gatewaywrapper.gateway, start, end)
        assert None == gatewaywrapper.gateway.getObject("Dataset", new_ds_Id)
        gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup(str(default_groupId))
        assert None != gatewaywrapper.gateway.getObject("Dataset", new_ds_Id)

        self.searchHistory(gatewaywrapper.gateway, start, end)

        # Create Another Dataset in new group
        update = gatewaywrapper.gateway.getUpdateService()
        new_ds = ode.model.DatasetI()
        dataset_name = "history_test_%s" % uuid
        new_ds.name = rstring(dataset_name)
        new_ds = update.saveAndReturnObject(new_ds)
        new_ds_Id = new_ds.id.val

        end = int(round(time.time() * 1000)) + 1000
        self.searchHistory(gatewaywrapper.gateway, start, end)


class TestScript (object):
    @pytest.fixture(autouse=True)
    def setUp(self):
        """ Create a group with User """
        dbhelpers.USERS['script_test_user'] = dbhelpers.UserEntry(
            'script_test_user', 'ome',
            firstname='script',
            lastname='user',
            groupname="rw_script",
            groupperms=READWRITE)
        dbhelpers.bootstrap(onlyUsers=True)

    @pytest.mark.broken(ticket="11610")
    def testRunScript(self, gatewaywrapper):
        # Login as user...
        gatewaywrapper.doLogin(dbhelpers.USERS['script_test_user'])
        userId = gatewaywrapper.gateway.getEventContext().userId
        uuid = gatewaywrapper.gateway.getEventContext().sessionUuid
        default_groupId = gatewaywrapper.gateway.getEventContext().groupId
        # Create Dataset in 'default' group
        update = gatewaywrapper.gateway.getUpdateService()
        new_ds = ode.model.DatasetI()
        dataset_name = "script_test_%s" % uuid
        new_ds.name = rstring(dataset_name)
        new_ds = update.saveAndReturnObject(new_ds)
        new_ds_Id = new_ds.id.val

        # As Admin, create a second group with this user & upload script
        gatewaywrapper.loginAsAdmin()
        gid = gatewaywrapper.gateway.createGroup(
            "script-test-%s" % uuid, member_Ids=[userId], perms=READWRITE)

        SCRIPT = """if True:
        import ode.scripts
        import ode.rtypes
        client = ode.scripts.client("ticket8573", \
                ode.scripts.Long("datasetId"), \
                ode.scripts.String("datasetName", out=True))
        ec = client.sf.getAdminService().getEventContext()
        gid = ec.groupId
        qs = client.sf.getQueryService()
        ds_Id = client.getInput("datasetId").getValue()
        print "Running test..."     # generate stdout
        try:
            dataset = qs.find("Dataset", ds_Id)
            ds_Name = dataset.name.val
            print ds_Name
        except:
            ds_Name = "Not Found"
        client.setOutput("gid", ode.rtypes.rlong(gid))
        client.setOutput("datasetName", ode.rtypes.rstring(ds_Name))
        """
        svc = gatewaywrapper.gateway.getScriptService()
        scriptID = svc.uploadOfficialScript(
            "/test/ticket8573/%s" % uuid, SCRIPT)

        # switch user into new group
        gatewaywrapper.doLogin(dbhelpers.USERS['script_test_user'])
        switched = gatewaywrapper.gateway.c.sf.setSecurityContext(
            ode.model.ExperimenterGroupI(gid, False))
        assert switched, "Failed to switch into new group"
        # Shouldn't be able to access Dataset...
        assert None == gatewaywrapper.gateway.getObject("Dataset", new_ds_Id)
        gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup(
            str(default_groupId))
        assert None != gatewaywrapper.gateway.getObject("Dataset", new_ds_Id)

        # run script
        svc = gatewaywrapper.gateway.getScriptService()
        process = svc.runScript(scriptID, wrap({"datasetId": new_ds_Id}).val,
                                None, gatewaywrapper.gateway.SERVICE_OPTS)
        cb = ode.scripts.ProcessCallbackI(gatewaywrapper.gateway.c, process)
        while cb.block(500) is None:
            pass
        results = process.getResults(0, gatewaywrapper.gateway.SERVICE_OPTS)
        assert 'stdout' in results, \
            "Failed to return stdout Original File. #8614"
        assert results["gid"].val == default_groupId, \
            "We want script to have eventContext of group:%s not %s" % \
            (default_groupId, results["gid"].val)
        assert results["datasetName"].val == dataset_name, \
            "Script should be able to access Dataset"
