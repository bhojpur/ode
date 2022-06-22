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
   Integration test focused on the ode.api.ISession interface.
"""

import os

from ode.testlib import ITest
import pytest
import traceback
import ode

from ode.rtypes import rint
from ode.rtypes import rlong
from ode.rtypes import rstring
from ode.rtypes import unwrap

from ode.cmd import UpdateSessionTimeoutRequest

class TestISession(ITest):

    def testBasicUsage(self):
        self.client.sf.getSessionService()
        admin = self.client.sf.getAdminService()
        admin.getEventContext().sessionUuid

    def testManuallyClosingOwnSession(self):
        client = self.new_client()
        client.killSession()

    def testCreateSessionForUser(self):
        p = ode.sys.Parameters()
        p.theFilter = ode.sys.Filter()
        p.theFilter.limit = rint(1)
        user = self.root.sf.getQueryService().findByQuery(
            """
            select e from Experimenter e
            where e.id > 0 and e.odeName != 'guest'
            """, p)
        p = ode.sys.Principal()
        p.name = user.odeName.val
        p.group = "user"
        p.eventType = "Test"
        sess = self.root.sf.getSessionService().createSessionWithTimeout(
            p, 10000)  # 10 secs

        client = ode.client()  # ok rather than new_client since has __del__
        try:
            user_sess = client.createSession(sess.uuid, sess.uuid)
            uuid = user_sess.getAdminService().getEventContext().sessionUuid
            assert sess.uuid.val == uuid
            client.closeSession()
        finally:
            client.__del__()

    def testJoinSession_Helper(self):
        test_user = self.new_user()

        client = ode.client()  # ok rather than new_client since has __del__
        try:
            sf = client.createSession(test_user.odeName.val,
                                      test_user.odeName.val)
            a = sf.getAdminService()
            suuid = a.getEventContext().sessionUuid
            sf.detachOnDestroy()
            return suuid
        finally:
            client.__del__()

    def testJoinSession(self):
        suuid = self.testJoinSession_Helper()
        c1 = ode.client()  # ok rather than new_client since has __del__
        try:
            sf1 = c1.joinSession(suuid)
            a1 = sf1.getAdminService()
            s1uuid = a1.getEventContext().sessionUuid
            assert s1uuid == suuid
        finally:
            c1.__del__()

    @pytest.mark.parametrize("who", (
        ("root", -1, None),
        ("root", 1, None),
        ("user", -1, None),
        ("baduser", 1, None)))
    def testUpdateSessions(self, who):
        who, idlediff, livediff = who
        if who.startswith("root"):
            client = self.root
        else:
            client = self.client

        uuid = client.getSessionId()
        service = client.sf.getSessionService()
        obj_before = service.getSession(uuid)
        live = unwrap(obj_before.timeToLive)
        idle = unwrap(obj_before.timeToIdle)

        req = UpdateSessionTimeoutRequest()
        req.session = uuid
        if livediff is not None:
            req.timeToLive = rlong(live+livediff)
        if idlediff is not None:
            req.timeToIdle = rlong(idle+idlediff)
        try:
            cb = client.submit(req)
            cb.getResponse()
            cb.close(True)
            assert not who.startswith("bad")  # must throw
        except ode.CmdError as ce:
            if who.startswith("bad"):
                assert ce.err.name == "non-admin-increase"
                return
            else:
                print(ce.err.parameters.get("stacktrace"))
                raise Exception(ce.err.category,
                                ce.err.name)

        obj_after = client.sf.getQueryService().findByQuery(
            ("select s from Session s "
             "where s.id = %s") % obj_before.id.val, None)
        assert obj_before.id == obj_after.id
        assert obj_before.uuid == obj_after.uuid
        assert req.timeToLive is None \
            or req.timeToLive == obj_after.timeToLive
        assert req.timeToIdle is None \
            or req.timeToIdle == obj_after.timeToIdle

        # Now try again! (required SessionManager fix)
        obj_after = service.getSession(uuid)
        assert req.timeToIdle.val == obj_after.timeToIdle.val

    def testCreateSessionForGuest(self):
        p = ode.sys.Principal()
        p.name = "guest"
        p.group = "guest"
        p.eventType = "User"
        sess = self.root.sf.getSessionService().createSessionWithTimeout(
            p, 10000)  # 10 secs
        guest_client = ode.client()
        guest_client.joinSession(sess.uuid.val)
        guest_client.closeSession()

    def test1018CreationDestructionClosing(self):
        c1, c2, c3, c4 = None, None, None, None
        try:
            c1 = ode.client()  # ok rather than new_client since has __del__
            user = self.new_user()
            s1 = c1.createSession(user.odeName.val, user.odeName.val)
            s1.detachOnDestroy()
            uuid = s1.ice_getIdentity().name

            # Intermediate "disrupter"
            c2 = ode.client()  # ok rather than new_client since has __del__
            s2 = c2.createSession(uuid, uuid)
            s2.closeOnDestroy()
            s2.getAdminService().getEventContext()
            c2.closeSession()

            # 1 should still be able to continue
            s1.getAdminService().getEventContext()

            # Now if s1 exists another session should be able to connect
            c1.closeSession()
            c3 = ode.client()  # ok rather than new_client since has __del__
            s3 = c3.createSession(uuid, uuid)
            s3.closeOnDestroy()
            s3.getAdminService().getEventContext()

            # Guarantee that the session is closed.
            c3.killSession()

            # Now a connection should not be possible
            c4 = ode.client()  # ok rather than new_client since has __del__
            import Glacier2
            with pytest.raises(Glacier2.PermissionDeniedException):
                c4.createSession(uuid, uuid)
        finally:
            for c in (c1, c2, c3, c4):
                if c:
                    c.__del__()

    def testSimpleDestruction(self):
        c = ode.client()  # ok rather than new_client since has __del__
        try:
            c.ic.getImplicitContext().put(
                ode.constants.CLIENTUUID, "SimpleDestruction")
            user = self.new_user()
            s = c.createSession(user.odeName.val, user.odeName.val)
            s.closeOnDestroy()
            c.closeSession()
        finally:
            c.__del__()

    def testGetMySessionsTicket1975(self):
        svc = self.client.sf.getSessionService()
        svc.getMyOpenSessions()
        svc.getMyOpenAgentSessions("ode.web")
        svc.getMyOpenClientSessions()

    def testTicket2196SetSecurityContext(self):
        ec = self.client.sf.getAdminService().getEventContext()
        exp0 = ode.model.ExperimenterI(ec.userId, False)
        grp0 = ode.model.ExperimenterGroupI(ec.groupId, False)
        grp1 = self.new_group([exp0])

        # Change: should pass
        # Force reload #4011
        self.client.sf.getAdminService().getEventContext()
        self.client.sf.setSecurityContext(grp1)

        # Make a stateful service, and change again
        rfs = self.client.sf.createRawFileStore()
        with pytest.raises(ode.SecurityViolation):
            self.client.sf.setSecurityContext(grp0)
        rfs.close()

        # Service is now closed, should be ok
        self.client.sf.setSecurityContext(grp1)

    def testManageMySessions(self):
        adminCtx = self.client.sf.getAdminService().getEventContext()
        username = adminCtx.userName
        group = adminCtx.groupName

        self.root.sf.getAdminService().lookupExperimenter(username)
        p = ode.sys.Principal()
        p.name = username
        p.group = group
        p.eventType = "User"
        newConnId = self.root.sf.getSessionService().createSessionWithTimeout(
            p, 60000)

        # ok rather than new_client since has __del__
        c1 = ode.client(
            pmap=['--Ice.Config=' + (os.environ.get("ICE_CONFIG"))])
        try:
            host = c1.ic.getProperties().getProperty('ode.host')
            port = int(c1.ic.getProperties().getProperty('ode.port'))
            c1.__del__()  # Just used for parsing

            # ok rather than new_client since has __del__
            c1 = ode.client(host=host, port=port)
            s = c1.joinSession(newConnId.getUuid().val)
            s.detachOnDestroy()

            svc = self.client.sf.getSessionService()

            for s in svc.getMyOpenSessions():
                if (adminCtx.sessionUuid != s.uuid.val
                        and s.defaultEventType.val
                        not in ('Internal', 'Sessions')):
                    cc = None
                    try:
                        try:
                            # ok rather than new_client since has __del__
                            cc = ode.client(host, port)
                            cc.joinSession(s.uuid.val)
                            cc.killSession()
                        except:
                            self.assertRaises(traceback.format_exc())
                    finally:
                        cc.__del__()

            for s in svc.getMyOpenSessions():
                assert s.uuid.val != newConnId.getUuid().val
        finally:
            c1.__del__()

    @pytest.mark.parametrize("client_ip", [
        "127.0.0.1",
        "2400:cb00:2048:1::6814:55",
        "1234:5678:1234:5678:1234:5678:121.212.121.212"])
    def testSessionWithIP(self, client_ip):
        c1 = ode.client(
            pmap=['--Ice.Config='+(os.environ.get("ICE_CONFIG"))])
        try:
            host = c1.ic.getProperties().getProperty('ode.host')
            port = int(c1.ic.getProperties().getProperty('ode.port'))
            rootpass = c1.ic.getProperties().getProperty('ode.rootpass')
        finally:
            c1.__del__()

        c = ode.client(host=host, port=port)
        try:
            c.setAgent("ODE.py.root_test")
            c.setIP(client_ip)
            s = c.createSession("root", rootpass)

            p = ode.sys.ParametersI()
            p.map = {}
            p.map["uuid"] = rstring(
                s.getAdminService().getEventContext().sessionUuid)
            res = s.getQueryService().findByQuery(
                "from Session where uuid=:uuid", p)

            assert client_ip == res.getUserIP().val

            s.closeOnDestroy()
            c.closeSession()
        finally:
            c.__del__()

    # Test that ISession.createUserSession can be used from a normal session.
    def testCreateUserSession(self):
        iSession = self.client.sf.getSessionService()
        newSession = iSession.createUserSession(
            10 * 1000, 10 * 1000, self.ctx.groupName)
        c = ode.client(self.client.getPropertyMap())
        c.joinSession(newSession.uuid.val)

    # Test that ISession.createUserSession cannot be used from a session that
    # is a sudo from somebody else.
    @pytest.mark.parametrize("to_root", [True, False])
    def testCreateUserSessionFromSudo(self, to_root):
        principal = ode.sys.Principal()
        principal.eventType = "Test"

        if to_root:
            iAdmin_root = self.root.sf.getAdminService()
            ctx_root = iAdmin_root.getEventContext()
            principal.name = ctx_root.userName
            principal.group = ctx_root.groupName
        else:
            principal.name = self.ctx.userName
            principal.group = self.ctx.groupName

        iSession_root = self.root.sf.getSessionService()
        sudo_session = iSession_root.createSessionWithTimeout(
            principal, 10 * 1000)

        c = ode.client(self.client.getPropertyMap())
        c.joinSession(sudo_session.uuid.val)

        iSession = c.sf.getSessionService()
        with pytest.raises(ode.SecurityViolation):
            iSession.createUserSession(
                10 * 1000, 10 * 1000, self.ctx.groupName)
