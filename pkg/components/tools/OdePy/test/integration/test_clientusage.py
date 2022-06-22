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
   Tests for the demonstrating client usage
"""

import ode
from ode.testlib import ITest
from ode.rtypes import rstring

class TestClientUsage(ITest):

    """
    Note: this is the only test which should use 'ode.client()'
    All others should use the new_client(user=) syntax from ITest
    """

    def testClientClosedAutomatically(self):
        client = ode.client()
        user = self.new_user()
        client.createSession(user.odeName.val, user.odeName.val)
        client.getSession().closeOnDestroy()

    def testClientClosedManually(self):
        client = ode.client()
        user = self.new_user()
        client.createSession(user.odeName.val, user.odeName.val)
        client.getSession().closeOnDestroy()
        client.closeSession()

    def testUseSharedMemory(self):
        client = ode.client()
        user = self.new_user()
        client.createSession(user.odeName.val, user.odeName.val)

        assert 0 == len(client.getInputKeys())
        client.setInput("a", rstring("b"))
        assert 1 == len(client.getInputKeys())
        assert "a" in client.getInputKeys()
        assert "b" == client.getInput("a").getValue()

        client.closeSession()

    def testCreateInsecureClientTicket2099(self):
        secure = ode.client()
        assert secure.isSecure()
        try:
            user = self.new_user()
            s = secure.createSession(
                user.odeName.val, user.odeName.val)
            s.getAdminService().getEventContext()
            insecure = secure.createClient(False)
            try:
                insecure.getSession().getAdminService().getEventContext()
                assert not insecure.isSecure()
            finally:
                insecure.closeSession()
        finally:
            secure.closeSession()

    def testGetStatefulServices(self):
        root = self.root
        sf = root.sf
        sf.setSecurityContext(ode.model.ExperimenterGroupI(0, False))
        sf.createRenderingEngine()
        srvs = root.getStatefulServices()
        assert 1 == len(srvs)
        try:
            sf.setSecurityContext(ode.model.ExperimenterGroupI(1, False))
            assert False, "Should not be allowed"
        except:
            pass  # good
        srvs[0].close()
        srvs = root.getStatefulServices()
        assert 0 == len(srvs)
        sf.setSecurityContext(ode.model.ExperimenterGroupI(1, False))
