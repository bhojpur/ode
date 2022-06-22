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

import os
import sys

import logging

logging.basicConfig(level=0)

import ode
import ode.util

import Ice

import ode.all
import ode.grid.monitors as monitors
from drivers import MockMonitor

class MockDropBox(Ice.Application):

    def run(self, args):
        retries = 5
        interval = 3
        dropBoxDir = "DropBox"
        dirImportWait = 60
        pathMode = "Follow"

        sf = ode.util.internal_service_factory(
            self.communicator(), "root", "system",
            retries=retries, interval=interval)
        try:
            configService = sf.getConfigService()
            dropBoxBase = configService.getConfigValue("ode.data.dir")
            dropBoxBase = os.path.join(dropBoxBase, dropBoxDir)
        finally:
            sf.destroy()

        config = None  # Satisfies flake8 but needs fixing

        fsServer = self.communicator().stringToProxy(config.serverIdString)
        fsServer = monitors.MonitorServerPrx.checkedCast(fsServer.ice_twoway())

        identity = self.communicator().stringToIdentity(config.clientIdString)

        mClient = MockMonitor(dropBoxBase)
        adapter = self.communicator().createObjectAdapter(
            config.clientAdapterName)
        adapter.add(mClient, identity)
        adapter.activate()

        mClientProxy = monitors.MonitorClientPrx.checkedCast(
            adapter.createProxy(identity))
        monitorType = monitors.MonitorType.__dict__["Persistent"]
        eventTypes = [monitors.EventType.__dict__["Create"],
                      monitors.EventType.__dict__["Modify"]]
        pathMode = monitors.PathMode.__dict__[pathMode]
        serverId = fsServer.createMonitor(
            monitorType, eventTypes, pathMode, dropBoxBase,
            list(config.fileTypes),  [], mClientProxy, 0.0, True)

        mClient.setId(serverId)
        mClient.setServerProxy(fsServer)
        mClient.setSelfProxy(mClientProxy)
        mClient.setDirImportWait(dirImportWait)
        mClient.setMaster(self)
        fsServer.startMonitor(serverId)

        self.communicator().waitForShutdown()

        if mClient is not None:
            mClient.stop()
        fsServer.stopMonitor(id)
        fsServer.destroyMonitor(id)


class TestDropBoxClient(object):

    def test1(self):
        app = MockDropBox()
        app.main(sys.argv)

    def teardown_method(self, method):
        MockMonitor.static_stop()
