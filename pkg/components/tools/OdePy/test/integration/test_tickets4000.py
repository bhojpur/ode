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
   Integration tests for tickets between 3000 and 3999
"""

import ode
from ode.testlib import ITest
import pytest

from ode.rtypes import rstring

class TestTickets4000(ITest):

    @pytest.mark.broken(ticket="11539")
    def test3138(self):
        """
        Try multiple logins to see if they slow down
        """
        user = self.new_user()
        name = user.odeName.val

        self.root.sf.getAdminService().changeUserPassword(
            name, rstring("GOOD"))

        self.login_attempt(name, 0.15, less=True)
        self.login_attempt(name, 3.0)
        self.login_attempt(name, 0.15, "GOOD", less=True)
        self.login_attempt(name, 0.15, less=True)
        self.login_attempt(name, 3.0)

    def testChangeActiveGroup(self):
        client = self.new_client()
        admin = client.sf.getAdminService()

        assert 2 == len(admin.getEventContext().memberOfGroups)

        # AS ROOT: adding user to extra group
        admin_root = self.root.sf.getAdminService()
        exp = admin_root.getExperimenter(admin.getEventContext().userId)
        grp = self.new_group()
        admin_root.addGroups(exp, [grp])

        assert 3 == len(admin.getEventContext().memberOfGroups)

        proxies = dict()
        # creating stateful services
        proxies['search'] = client.sf.createSearchService()
        proxies['thumbnail'] = client.sf.createThumbnailStore()
        proxies['admin'] = client.sf.getAdminService()

        # changing group
        for k in proxies.keys():
            try:
                proxies[k].close()
            except AttributeError:
                pass

        client.sf.setSecurityContext(
            ode.model.ExperimenterGroupI(grp.id.val, False))
        admin.setDefaultGroup(admin.getExperimenter(
            admin.getEventContext().userId),
            ode.model.ExperimenterGroupI(grp.id.val, False))
        assert grp.id.val == \
            client.sf.getAdminService().getEventContext().groupId

    def testChangeActiveGroupWhenConnectionLost(self):
        import os
        client = self.new_client()
        admin = client.sf.getAdminService()
        uuid = client.sf.getAdminService().getEventContext().sessionUuid
        assert 2 == len(admin.getEventContext().memberOfGroups)

        # AS ROOT: adding user to extra group
        admin_root = self.root.sf.getAdminService()
        exp = admin_root.getExperimenter(admin.getEventContext().userId)
        grp = self.new_group()
        admin_root.addGroups(exp, [grp])

        assert 3 == len(admin.getEventContext().memberOfGroups)

        proxies = dict()
        # creating stateful services
        proxies['search'] = client.sf.createSearchService()  # 1A
        proxies['thumbnail'] = client.sf.createThumbnailStore()  # 1B
        proxies['admin'] = client.sf.getAdminService()
        copy = dict(proxies)

        # loosing the connection
        # ...

        # joining session

        c = ode.client(
            pmap=['--Ice.Config=' + (os.environ.get("ICE_CONFIG"))])
        host = c.ic.getProperties().getProperty('ode.host')
        port = int(c.ic.getProperties().getProperty('ode.port'))
        c = ode.client(host=host, port=port)
        sf = c.joinSession(uuid)

        # retriving stateful services
        proxies['search'] = sf.createSearchService()  # 2A
        proxies['thumbnail'] = sf.createThumbnailStore()  # 2B
        proxies['admin'] = sf.getAdminService()

        # changing group
        for k in proxies.keys():
            prx = proxies[k]
            if isinstance(prx, ode.api.StatefulServiceInterfacePrx):
                prx.close()

        """
        A security violation must be thrown here because the first instances
        which are stored in proxies (#1A and #1B) are never closed since #2A
        and #2B overwrite them. Using the copy instance, we can close them.
        """
        with pytest.raises(ode.SecurityViolation):
            sf.setSecurityContext(
                ode.model.ExperimenterGroupI(grp.id.val, False))

        for k in copy.keys():
            prx = copy[k]
            if isinstance(prx, ode.api.StatefulServiceInterfacePrx):
                prx.close()

        sf.setSecurityContext(
            ode.model.ExperimenterGroupI(grp.id.val, False))

        ec = admin.getEventContext()
        sf.getAdminService().setDefaultGroup(
            sf.getAdminService().getExperimenter(ec.userId),
            ode.model.ExperimenterGroupI(grp.id.val, False))
        assert grp.id.val == ec.groupId

    def test3201(self):
        import Glacier2

        def testLogin(username, password):
            import os
            c = ode.client(
                pmap=['--Ice.Config=' + (os.environ.get("ICE_CONFIG"))])
            host = c.ic.getProperties().getProperty('ode.host')
            port = int(c.ic.getProperties().getProperty('ode.port'))
            ode.client(host=host, port=port)
            sf = c.createSession(username, password)
            sf.getAdminService().getEventContext()
            c.closeSession()

        admin_root = self.root.sf.getAdminService()

        client = self.new_client()
        admin = client.sf.getAdminService()
        odeName = admin.getEventContext().userName

        # change password as user
        admin.changePassword(rstring("aaa"))

        testLogin(odeName, "aaa")

        # change password as root
        admin_root.changeUserPassword(odeName, rstring("ode"))

        with pytest.raises(Glacier2.PermissionDeniedException):
            testLogin(odeName, "aaa")

        testLogin(odeName, "ode")

        admin.changePasswordWithOldPassword(rstring("ode"), rstring("ccc"))

        testLogin(odeName, "ccc")

    def test3131(self):
        _ = ode.rtypes.rstring
        la = ode.model.LongAnnotationI()
        la.ns = _(self.uuid())
        la = self.update.saveAndReturnObject(la)
        la.ns = _(self.uuid())
        la = self.update.saveAndReturnObject(la)
        la.ns = _(self.uuid())
        la = self.update.saveAndReturnObject(la)
        assert -1 == la.details.updateEvent.session.sizeOfEvents()
