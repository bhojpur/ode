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
   Tests of the admin service
"""

from ode.testlib import ITest
import pytest
import ode
from ode.rtypes import rstring

class TestAdmin(ITest):

    def testGetGroup(self):
        a = self.sf.getAdminService()
        l = a.lookupGroups()
        g = a.getGroup(l[0].getId().val)
        assert 0 != g.sizeOfGroupExperimenterMap()

    def testSetGroup(self):
        # Add user to new group to test setting default
        uid = self.user.id.val
        e = self.sf.getAdminService().getExperimenter(uid)
        admin = self.root.sf.getAdminService()
        grp = self.new_group()
        admin.addGroups(e, [grp])

        admin.setDefaultGroup(e, grp)

        dg = self.sf.getAdminService().getDefaultGroup(uid)
        assert dg.id.val == grp.id.val

    def testChangePassword(self):
        """
        See ticket:3201
        """

        client = self.new_client()

        admin = client.sf.getAdminService()
        admin.changePassword(rstring("ode"))

        query = client.sf.getQueryService()
        getOnlyOne = ode.sys.ParametersI()
        getOnlyOne.limit = 1

        myId = admin.getEventContext().userId
        hql = ("SELECT event.time FROM EventLog "
               "WHERE event.experimenter.id = %s "
               "AND action = 'PASSWORD' ORDER BY id DESC") % myId

        # time of first password change
        whenOde = query.projection(hql, getOnlyOne)[0][0].val

        # Now login without a password
        client2 = client.createClient(True)
        try:
            admin = client2.sf.getAdminService()

            with pytest.raises(ode.SecurityViolation):
                admin.changePassword(rstring("foo"))
            admin.changePasswordWithOldPassword(rstring("ode"), rstring("foo"))
        finally:
            client2.closeSession()

        # Now try to change password without a secure session
        if False:  # Waiting on ticket:3232
            client3 = client.createClient(False)
            try:
                admin = client3.sf.getAdminService()
                with pytest.raises(ode.SecurityViolation):
                    admin.changePasswordWithOldPassword(
                        rstring("foo"), rstring("foo"))
            finally:
                client3.closeSession()

        # time of last password change
        whenFoo = query.projection(hql, getOnlyOne)[0][0].val
        assert whenFoo > whenOde

    @pytest.mark.broken(reason="Empty password disabled by config",
                        ticket="3201")
    def testChangePasswordWhenUnset(self):
        """
        Shows that it's possible to use the
        changePasswordWithOldPassword when
        previously no password was set.

        See ticket:3201
        """
        client = self.new_client()
        admin = client.sf.getAdminService()

        # By setting the user's password to the empty string
        # any password will be allowed as the old password
        admin.changePassword(rstring(""))
        admin.changePasswordWithOldPassword(rstring("IGNORED"), rstring("ode"))
        with pytest.raises(ode.SecurityViolation):
            admin.changePasswordWithOldPassword(
                rstring("BADPW"), rstring("foo"))
        admin.changePasswordWithOldPassword(rstring("ode"), rstring("foo"))

        # None disables user. No further password checks will pass.
        # Only the current session or an admin will be able to
        # reset the password
        admin.changePassword(None)
        with pytest.raises(ode.SecurityViolation):
            admin.changePasswordWithOldPassword(rstring(""), rstring("foo"))
        with pytest.raises(ode.SecurityViolation):
            admin.changePasswordWithOldPassword(rstring("ode"), rstring("foo"))
        with pytest.raises(ode.ApiUsageException):
            admin.changePasswordWithOldPassword(None, rstring("foo"))
        joined_client = client.createClient(True)
        try:
            with pytest.raises(ode.SecurityViolation):
                joined_client.sf.getAdminService()\
                    .changePasswordWithOldPassword(rstring(""), rstring("ode"))
        finally:
            joined_client.__del__()
        admin.changePassword(rstring("ode"))  # could be an admin

    def testGetEventContext4011(self):
        """
        Tests the "freshness" of the iAdmin.getEventContext() call.
        """
        group = self.new_group()
        admin = self.sf.getAdminService()
        root_admin = self.root.sf.getAdminService()

        ec1 = admin.getEventContext()
        exp = ode.model.ExperimenterI(ec1.userId, False)
        grps1 = root_admin.getMemberOfGroupIds(exp)

        # Now add the user to a group and see if the
        # event context is updated.
        root_admin.addGroups(exp, [group])
        ec2 = admin.getEventContext()
        grps2 = root_admin.getMemberOfGroupIds(exp)

        # Check via the groups
        assert len(grps1)+1 == len(grps2)
        assert group.id.val in grps2

        # Check again via the contexts
        assert len(ec1.memberOfGroups)+1 == len(ec2.memberOfGroups)
        assert group.id.val in ec2.memberOfGroups

    def testUserRoles4056(self):
        """
        Tests for optimistic lock exception when modifying roles.
        """
        client = self.new_client()
        admin = client.sf.getAdminService()
        ec = admin.getEventContext()
        roles = admin.getSecurityRoles()

        exp = ode.model.ExperimenterI(ec.userId, False)
        grp = ode.model.ExperimenterGroupI(roles.userGroupId, False)

        root_admin = self.root.sf.getAdminService()
        root_admin.removeGroups(exp, [grp])
        root_admin.addGroups(exp, [grp])
        root_admin.removeGroups(exp, [grp])
        root_admin.addGroups(exp, [grp])

    def testSetSecurityPassword(self):
        """
        Several methods require the user to have authenticated with a password.
        In 4.3, a method was added to the ServiceFactoryPrx to allow late
        password-based authentication.

        See #3202
        See @RolesAllow("HasPassword")
        """
        experimenter = self.new_user()  # To have password changed

        password = self.root.getProperty("ode.rootpass")
        # Secure, but not password-auth'd
        new_client = self.root.createClient(True)

        admin = new_client.sf.getAdminService()
        new_password = ode.rtypes.rstring("FOO")

        # Initially, the test should fail.
        with pytest.raises(ode.SecurityViolation):
            admin.changeUserPassword(experimenter.odeName.val, new_password)

        # Now set the password
        new_client.sf.setSecurityPassword(password)

        # And then it should succeed
        admin.changeUserPassword(experimenter.odeName.val, new_password)

    def new_client_FAILS(self, user):
        import Glacier2
        with pytest.raises(Glacier2.CannotCreateSessionException):
            self.new_client(user=user)
            pass

    def new_client_RESTRICTED(self, user):
        c = self.new_client(user=user)
        with pytest.raises(ode.SecurityViolation):
            c.sf.getQueryService().find("Image", -1)  # Should be disallowed

    # This test is no longer valid as it shpuld not be possible to remove
    # users from their only remaining group. It would be easy to may the
    # test pass by adding extra groups but that would defeat the purpose
    # of this test. Marking as broken until the test has been reviewed.
    @pytest.mark.broken(reason="Is this test still valid?", ticket="11465")
    def test9193(self):
        # Test the removal of removing users
        # from a group when the group in question
        # may be their last (i.e. default) group

        g = self.new_group()
        u = self.new_user(group=g)

        # Test removing the default group
        self.remove_experimenters(g, [u])
        self.new_client_FAILS(user=u)

        self.add_experimenters(g, [u])
        c = self.new_client(user=u)

        # Now we'll try removing and re-adding user
        UG = c.sf.getAdminService().lookupGroup("user")
        self.remove_experimenters(UG, [u])
        self.new_client_RESTRICTED(user=u)

        self.add_experimenters(UG, [u])
        c = self.new_client(user=u)

        # Now we'll try with both
        admin = self.root.sf.getAdminService()
        admin.removeGroups(u, [g, UG])
        self.new_client_FAILS(user=u)

        admin.addGroups(u, [g, UG])
        c = self.new_client(user=u)

        # And now in the other order
        admin.removeGroups(u, [UG, g])
        self.new_client_FAILS(user=u)

        admin.addGroups(u, [UG, g])
        c = self.new_client(user=u)
