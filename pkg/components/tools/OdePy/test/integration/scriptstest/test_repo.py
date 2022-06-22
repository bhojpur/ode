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
   Integration test focused on the registering of official
   scripts in the ScriptRepo.
"""

from ode.testlib import ITest
import pytest
import ode
import ode.all

class TestScriptRepo(ITest):

    def testScriptRepo(self):
        sr = self.client.sf.sharedResources()
        repo = sr.getScriptRepository()
        assert repo

    def testGetOfficialScripts(self):
        scriptService = self.sf.getScriptService()
        officialScripts = scriptService.getScripts()
        count = len(officialScripts)
        assert count > 0

    def testGetUserScripts(self):
        scriptService = self.sf.getScriptService()
        myUserScripts = scriptService.getUserScripts([])
        sid = scriptService.uploadScript(
            "/test/foo.py",
            """if True:
            import ode, ode.scripts as OS
            OS.client("name")
            """)

        myUserScripts = scriptService.getUserScripts([])
        assert sid in [x.id.val for x in myUserScripts]

        myUserScripts = scriptService.getUserScripts(
            [ode.model.ExperimenterI(self.user.id.val, False)])
        assert sid in [x.id.val for x in myUserScripts]

    @pytest.mark.broken(ticket="11494")
    def testGetGroupScripts(self):
        scriptService = self.sf.getScriptService()
        client = self.new_client(self.group)

        sid = client.sf.getScriptService().uploadScript(
            "/test/otheruser.py",
            """if True:
            import ode, ode.scripts as OS
            OS.client("testGetGroupScripts")
            """)

        myGroupScripts = scriptService.getUserScripts(
            [ode.model.ExperimenterGroupI(self.group.id.val, False)])
        assert sid in [x.id.val for x in myGroupScripts]

    def testCantUndulyLoadScriptRepoFromUuid(self):
        pass

    def testMultipleScriptPathsNotSupported(self):
        pass

    def testUploadingViaOfficialScriptShowsUpInRepo(self):
        pass

    def testUploadingViaNonOfficialScriptDoesntShowUpInRepo(self):
        pass
