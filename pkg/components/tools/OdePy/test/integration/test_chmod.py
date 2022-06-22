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
   Tests of the changing permissions on groups
"""

import time
from ode.testlib import ITest
import ode

class BaseChmodTest(ITest):

    def init(self, from_perms, to_perms):
        self.group = self.new_group(perms=from_perms)
        self.owner = self.new_client(group=self.group, owner=True)
        self.member = self.new_client(group=self.group, owner=False)
        self.from_perms = from_perms
        self.to_perms = to_perms

    def refresh(self, client):
        client.sf.getAdminService().getEventContext()  # Refresh

    def assertEqPerms(self, a, b):
        assert a.__class__ in (ode.model.PermissionsI, str)
        assert b.__class__ in (ode.model.PermissionsI, str)
        assert str(a) == str(b)

    def addData(self):
        c = ode.model.CommentAnnotationI()
        up = self.owner.sf.getUpdateService()
        self.comment = up.saveAndReturnObject(c)

    def load(self, client):
        query = client.sf.getQueryService()
        return query.get("CommentAnnotation", self.comment.id.val)

    def chmod(self, client):
        self.start = time.time()
        try:
            admin = client.sf.getAdminService()
            old_ctx = admin.getEventContext()
            old_grp = admin.getGroup(self.group.id.val)
            self.change_permissions(self.group.id.val, self.to_perms, client)
            new_ctx = admin.getEventContext()  # Refresh
            new_grp = admin.getGroup(self.group.id.val)
        finally:
            self.stop = time.time()
            self.elapsed = (self.stop - self.start)

        # Check old
        old_perms = old_grp.details.permissions
        self.assertEqPerms(old_ctx.groupPermissions, self.from_perms)
        self.assertEqPerms(old_ctx.groupPermissions, old_perms)

        # Check new
        new_perms = new_grp.details.permissions
        self.assertEqPerms(new_ctx.groupPermissions, self.to_perms)
        self.assertEqPerms(new_ctx.groupPermissions, new_perms)

    def assertChmod(self):
        old_comment = self.comment
        new_comment = self.load(self.owner)
        old_obj_perms = old_comment.details.permissions
        new_obj_perms = new_comment.details.permissions

        self.assertEqPerms(self.from_perms, old_obj_perms)
        self.assertEqPerms(self.to_perms, new_obj_perms)

    def assertState(self, client, canAnnotate, canEdit):
        obj = self.load(client)
        details = obj.details
        perms = details.permissions

        # Check the new perms state
        assert canAnnotate == perms.canAnnotate()
        assert canEdit == perms.canEdit()
        assert details.getCallContext() is not None
        assert details.getEventContext() is not None

class TestChmodEasy(BaseChmodTest):

    """
    Tests all the transitions which are known to be trivial.
    These mostly center around *adding* read permissions
    since there is nothing new to check.
    """

    def assertChmod(self):
        assert self.elapsed < 0.5
        BaseChmodTest.assertChmod(self)

    def test_chmod_rw_rwr(self):
        self.init("rw----", "rwr---")
        self.addData()
        self.chmod(self.owner)
        self.assertChmod()
        self.assertState(self.owner, True, True)

        self.refresh(self.member)
        self.assertState(self.member, False, False)


class TestChmodHard(BaseChmodTest):

    """
    Tests all the transitions which require runtime checks.
    These mostly center around *removing* read permissions
    since there it must be shown that this won't lead to
    confusing SecurityViolations.
    """

    pass
    # What to do about non-group chmod
