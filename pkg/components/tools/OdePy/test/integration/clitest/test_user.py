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

from ode.cli import NonZeroReturnCode
from ode.rtypes import rstring
from ode.plugins.user import UserControl
from ode.testlib.cli import CLITest, RootCLITest
from ode.testlib.cli import get_user_ids, get_group_ids
from ode.testlib.cli import UserIdNameFixtures
from ode.testlib.cli import GroupFixtures
from ode.testlib.cli import UserFixtures
from Glacier2 import PermissionDeniedException
import getpass
import pytest

GroupNames = [str(x) for x in GroupFixtures]
UserNames = [str(x) for x in UserFixtures]
UserIdNameNames = [str(x) for x in UserIdNameFixtures]
sort_keys = [None, "id", "login", "first-name", "last-name", "email"]
middlename_prefixes = [None, '-m', '--middlename']
email_prefixes = [None, '-e', '--email']
institution_prefixes = [None, '-i', '--institution']
admin_prefixes = [None, '-a', '--admin']
password_prefixes = [None, '-P', '--userpassword']


class TestUser(CLITest):

    @classmethod
    def setup_class(self):
        super(TestUser, self).setup_class()
        self.cli.register("user", UserControl, "TEST")
        self.users = self.sf.getAdminService().lookupExperimenters()

    def setup_method(self, method):
        super(TestUser, self).setup_method(method)
        self.args += ["user"]

    # List subcommand
    # ========================================================================
    @pytest.mark.parametrize("sort_key", sort_keys)
    @pytest.mark.parametrize("group_format", [None, "count", "long"])
    def testList(self, capsys, sort_key, group_format):
        self.args += ["list"]
        if sort_key:
            self.args += ["--sort-by-%s" % sort_key]
        if group_format:
            self.args += ["--%s" % group_format]
        self.cli.invoke(self.args, strict=True)

        # Read from the stdout
        out, err = capsys.readouterr()
        ids = get_user_ids(out, sort_key=sort_key)

        # Check all users are listed
        if sort_key == 'login':
            sorted_list = sorted(self.users, key=lambda x: x.odeName.val)
        elif sort_key == 'first-name':
            sorted_list = sorted(self.users, key=lambda x: x.firstName.val)
        elif sort_key == 'last-name':
            sorted_list = sorted(self.users, key=lambda x: x.lastName.val)
        elif sort_key == 'email':
            sorted_list = sorted(self.users, key=lambda x: (
                x.email and x.email.val or ""))
        else:
            sorted_list = sorted(self.users, key=lambda x: x.id.val)
        assert ids == [user.id.val for user in sorted_list]

    @pytest.mark.parametrize("style", [None, "sql", "csv", "plain", "json"])
    def testListWithStyles(self, capsys, style):
        self.args += ["list"]
        if style:
            self.args += ["--style=%s" % style]
        self.cli.invoke(self.args, strict=True)

    # Info subcomand
    # ========================================================================
    def testInfoNoArgument(self, capsys):
        self.args += ["info"]
        self.cli.invoke(self.args, strict=True)

        # Read from the stdout
        out, err = capsys.readouterr()
        ids = get_user_ids(out)
        assert ids == [self.user.id.val]

    @pytest.mark.parametrize("userfixture", UserFixtures, ids=UserNames)
    def testInfoArgument(self, capsys, userfixture):
        self.args += ["info"]
        self.args += userfixture.get_arguments(self.user)
        self.cli.invoke(self.args, strict=True)

        # Read from the stdout
        out, err = capsys.readouterr()
        ids = get_user_ids(out)
        assert ids == [self.user.id.val]

    def testInfoInvalidUser(self, capsys):
        self.args += ["info"]
        self.args += ["-1"]
        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke(self.args, strict=True)

    # Listgroups subcomand
    # ========================================================================
    def testListGroupsNoArgument(self, capsys):
        self.args += ["listgroups"]
        self.cli.invoke(self.args, strict=True)

        out, err = capsys.readouterr()
        ids = get_group_ids(out)
        roles = self.sf.getAdminService().getSecurityRoles()
        assert ids == [roles.userGroupId, self.group.id.val]

    @pytest.mark.parametrize("userfixture", UserFixtures, ids=UserNames)
    def testListGroupsArgument(self, capsys, userfixture):
        self.args += ["listgroups"]
        self.args += userfixture.get_arguments(self.user)
        self.cli.invoke(self.args, strict=True)

        out, err = capsys.readouterr()
        ids = get_group_ids(out)
        roles = self.sf.getAdminService().getSecurityRoles()
        assert ids == [roles.userGroupId, self.group.id.val]

    def testListGroupsInvalidArgument(self, capsys):
        self.args += ["listgroups"]
        self.args += ["-1"]

        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke(self.args, strict=True)

    # Email subcommand
    # ========================================================================
    @pytest.mark.parametrize("oneperline_arg", [None, "-1", "--one"])
    def testEmail(self, capsys, oneperline_arg):
        self.args += ["email", "-i"]
        if oneperline_arg:
            self.args += [oneperline_arg]
        self.cli.invoke(self.args, strict=True)

        # Read from the stdout
        out, err = capsys.readouterr()

        # Check all users are listed
        emails = [x.email.val for x in self.users if x.email and x.email.val]
        if oneperline_arg:
            assert out.strip() == "\n".join(emails)
        else:
            assert out.strip() == ", ".join(emails)

    # Password subcommand
    # ========================================================================
    @pytest.mark.parametrize("is_unicode", [True, False])
    def testPassword(self, is_unicode):
        self.args += ["password"]
        login = self.ctx.userName
        if is_unicode:
            password = "ążćę"
        else:
            password = self.uuid()

        self.setup_mock()
        self.mox.StubOutWithMock(getpass, 'getpass')
        i1 = 'Please enter password for your user (%s): ' % login
        i2 = 'Please enter password to be set: '
        i3 = 'Please re-enter password to be set: '
        getpass.getpass(i1).AndReturn(login)
        getpass.getpass(i2).AndReturn(password)
        getpass.getpass(i3).AndReturn(password)
        self.mox.ReplayAll()

        try:
            self.cli.invoke(self.args, strict=True)
            self.teardown_mock()

            # Check session creation using new password
            self.new_client(user=login, password=password)

            # Check session creation fails with a random password
            with pytest.raises(PermissionDeniedException):
                self.new_client(user=login, password=self.uuid)

            if is_unicode:
                # Check session creation fails with a combination of unicode
                # characters
                with pytest.raises(PermissionDeniedException):
                    self.new_client(user=login, password="żąćę")
                # Check session creation fails with question marks
                with pytest.raises(PermissionDeniedException):
                    self.new_client(user=login, password="????")
        finally:
            # Restore default password
            self.sf.getAdminService().changePasswordWithOldPassword(
                rstring(password), rstring(login))

    def testAddAdminOnly(self, capsys):
        group = self.new_group()
        login = self.uuid()
        firstname = self.uuid()
        lastname = self.uuid()

        self.args += ["add", login, firstname, lastname]
        self.args += ["%s" % group.id.val]
        self.args += ["--userpassword", "%s" % self.uuid()]
        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke(self.args, strict=True)
        out, err = capsys.readouterr()
        assert err.endswith("SecurityViolation: Admins only!\n")


class TestUserRoot(RootCLITest):

    @classmethod
    def setup_class(self):
        super(TestUserRoot, self).setup_class()
        self.cli.register("user", UserControl, "TEST")
        self.users = self.sf.getAdminService().lookupExperimenters()

    def setup_method(self, method):
        super(TestUserRoot, self).setup_method(method)
        self.args += ["user"]

    def getuserids(self, gid):
        group = self.sf.getAdminService().getGroup(gid)
        return [x.child.id.val for x in group.copyGroupExperimenterMap()]

    def getmemberids(self, gid):
        group = self.sf.getAdminService().getGroup(gid)
        return [x.child.id.val for x in group.copyGroupExperimenterMap()
                if not x.owner.val]

    def getownerids(self, gid):
        group = self.sf.getAdminService().getGroup(gid)
        return [x.child.id.val for x in group.copyGroupExperimenterMap()
                if x.owner.val]

    # User joingroup subcommand
    # ========================================================================
    @pytest.mark.parametrize(
        "idnamefixture", UserIdNameFixtures, ids=UserIdNameNames)
    @pytest.mark.parametrize("groupfixture", GroupFixtures, ids=GroupNames)
    @pytest.mark.parametrize("owner_arg", [None, '--as-owner'])
    def testJoinGroup(self, idnamefixture, groupfixture, owner_arg):
        user = self.new_user()
        group = self.new_group()
        assert user.id.val not in self.getuserids(group.id.val)

        self.args += ["joingroup"]
        self.args += idnamefixture.get_arguments(user)
        self.args += groupfixture.get_arguments(group)
        if owner_arg:
            self.args += [owner_arg]
        self.cli.invoke(self.args, strict=True)

        # Check user has been added to the list of member/owners
        if owner_arg:
            assert user.id.val in self.getownerids(group.id.val)
        else:
            assert user.id.val in self.getmemberids(group.id.val)

    # User leavegroup subcommand
    # ========================================================================
    @pytest.mark.parametrize(
        "idnamefixture", UserIdNameFixtures, ids=UserIdNameNames)
    @pytest.mark.parametrize("groupfixture", GroupFixtures, ids=GroupNames)
    @pytest.mark.parametrize("is_owner", [True, False])
    @pytest.mark.parametrize("owner_arg", [None, '--as-owner'])
    def testLeaveGroup(self, idnamefixture, groupfixture, is_owner,
                       owner_arg):
        user = self.new_user()
        group = self.new_group([user])
        if is_owner:
            self.root.sf.getAdminService().setGroupOwner(group, user)
            assert user.id.val in self.getownerids(group.id.val)
        else:
            assert user.id.val in self.getmemberids(group.id.val)

        self.args += ["leavegroup"]
        self.args += idnamefixture.get_arguments(user)
        self.args += groupfixture.get_arguments(group)
        if owner_arg:
            self.args += [owner_arg]
        self.cli.invoke(self.args, strict=True)

        # Check user has been added to the list of member/owners
        if owner_arg:
            assert user.id.val not in self.getownerids(group.id.val)
        else:
            assert user.id.val not in self.getuserids(group.id.val)

    # User add subcommand
    # ========================================================================
    @pytest.mark.parametrize("middlename_prefix", middlename_prefixes)
    @pytest.mark.parametrize("email_prefix", email_prefixes)
    @pytest.mark.parametrize("institution_prefix", institution_prefixes)
    @pytest.mark.parametrize("admin_prefix", admin_prefixes)
    def testAdd(self, middlename_prefix, email_prefix, institution_prefix,
                admin_prefix):
        group = self.new_group()
        login = self.uuid()
        firstname = self.uuid()
        lastname = self.uuid()

        self.args += ["add", login, firstname, lastname]
        kwargs = {
            'odeName': login,
            'firstName': firstname,
            'lastName': lastname}
        self.args += ["%s" % group.id.val]
        if middlename_prefix:
            middlename = self.uuid()
            self.args += [middlename_prefix, middlename]
            kwargs['middleName'] = middlename
        if email_prefix:
            email = "%s.%s@%s.org" % (firstname[:6], lastname[:6],
                                      self.uuid()[:6])
            self.args += [email_prefix, email]
            kwargs['email'] = email
        if institution_prefix:
            institution = self.uuid()
            self.args += [institution_prefix, institution]
            kwargs['institution'] = institution
        if admin_prefix:
            self.args += [admin_prefix]
        self.args += ['-P', login]
        self.cli.invoke(self.args, strict=True)

        # Check user has been added to the list of member/owners
        user = self.sf.getAdminService().lookupExperimenter(login)
        for key, value in kwargs.iteritems():
            assert getattr(user, key).val == kwargs[key]

        assert user.id.val in self.getuserids(group.id.val)
        if admin_prefix:
            roles = self.sf.getAdminService().getSecurityRoles()
            assert user.id.val in self.getuserids(roles.systemGroupId)

    @pytest.mark.parametrize("groupfixture", GroupFixtures, ids=GroupNames)
    def testAddGroup(self, groupfixture):
        group = self.new_group()
        login = self.uuid()
        firstname = self.uuid()
        lastname = self.uuid()

        self.args += ["add", login, firstname, lastname]
        self.args += groupfixture.get_arguments(group)
        self.args += ['-P', login]
        self.cli.invoke(self.args, strict=True)

        # Check user has been added to the list of member/owners
        user = self.sf.getAdminService().lookupExperimenter(login)
        assert user.odeName.val == login
        assert user.firstName.val == firstname
        assert user.lastName.val == lastname
        assert user.id.val in self.getuserids(group.id.val)

    @pytest.mark.parametrize("password_prefix", password_prefixes)
    @pytest.mark.parametrize("is_unicode", [True, False])
    def testAddPassword(self, password_prefix, is_unicode):
        group = self.new_group()
        login = self.uuid()
        firstname = self.uuid()
        lastname = self.uuid()
        if is_unicode:
            password = "ążćę"
        else:
            password = self.uuid()

        self.args += ["add", login, firstname, lastname]
        self.args += ["%s" % group.id.val]
        if password_prefix:
            self.args += [password_prefix, "%s" % password]
        else:
            self.setup_mock()
            self.mox.StubOutWithMock(getpass, 'getpass')
            i1 = 'Please enter password for your new user (%s): ' % login
            i2 = 'Please re-enter password for your new user (%s): ' % login
            getpass.getpass(i1).AndReturn(password)
            getpass.getpass(i2).AndReturn(password)
            self.mox.ReplayAll()

        self.cli.invoke(self.args, strict=True)
        if not password_prefix:
            self.teardown_mock()

        # Check user has been added to the list of member/owners
        user = self.sf.getAdminService().lookupExperimenter(login)
        assert user.odeName.val == login
        assert user.firstName.val == firstname
        assert user.lastName.val == lastname
        assert user.id.val in self.getuserids(group.id.val)

        # Check session creation using password
        self.new_client(user=login, password=password)
        # Check session creation fails with a random password
        with pytest.raises(PermissionDeniedException):
            self.new_client(user=login, password=self.uuid)

    def testAddNoPassword(self):
        group = self.new_group()
        login = self.uuid()
        firstname = self.uuid()
        lastname = self.uuid()

        self.args += ["add", login, firstname, lastname]
        self.args += ["%s" % group.id.val]
        self.args += ["--no-password"]

        # Assumes the server has the default configuration, i.e.
        # password_required=true
        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke(self.args, strict=True)

    # Password subcommand
    # ========================================================================
    @pytest.mark.parametrize("is_unicode", [True, False])
    def testPassword(self, is_unicode):
        user = self.new_user()
        login = user.odeName.val
        self.args += ["password", "%s" % login]
        if is_unicode:
            password = "ążćę"
        else:
            password = self.uuid()

        self.setup_mock()
        self.mox.StubOutWithMock(getpass, 'getpass')
        i1 = 'Please enter password for your user (root): '
        i2 = 'Please enter password to be set: '
        i3 = 'Please re-enter password to be set: '
        getpass.getpass(i1).AndReturn(self.root.getProperty("ode.rootpass"))
        getpass.getpass(i2).AndReturn(password)
        getpass.getpass(i3).AndReturn(password)
        self.mox.ReplayAll()

        self.cli.invoke(self.args, strict=True)
        self.teardown_mock()

        # Check session creation using new password
        self.new_client(user=login, password=password)

        # Check session creation fails with a random password
        with pytest.raises(PermissionDeniedException):
            self.new_client(user=login, password=self.uuid)

        if is_unicode:
            # Check session creation fails with a combination of unicode
            # characters
            with pytest.raises(PermissionDeniedException):
                self.new_client(user=login, password="żąćę")
            # Check session creation fails with question marks
            with pytest.raises(PermissionDeniedException):
                self.new_client(user=login, password="????")
