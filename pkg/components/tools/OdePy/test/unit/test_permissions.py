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
   Simple unit test which stipulates what the default permissions
   values should be.
"""

import ode.model

class TestPermissions(object):

    def setup_method(self, method):
        self.p = ode.model.PermissionsI()

    def testperm1(self):
        # The default
        assert self.p.isUserRead()
        assert self.p.isUserAnnotate()
        assert self.p.isUserWrite()
        assert self.p.isGroupRead()
        assert self.p.isGroupAnnotate()
        assert self.p.isGroupWrite()
        assert self.p.isWorldRead()
        assert self.p.isWorldAnnotate()
        assert self.p.isWorldWrite()

        # All off
        self.p._perm1 = 0
        assert not self.p.isUserRead()
        assert not self.p.isUserAnnotate()
        assert not self.p.isUserWrite()
        assert not self.p.isGroupRead()
        assert not self.p.isGroupAnnotate()
        assert not self.p.isGroupWrite()
        assert not self.p.isWorldRead()
        assert not self.p.isWorldAnnotate()
        assert not self.p.isWorldWrite()

        # All on
        self.p._perm1 = -1
        assert self.p.isUserRead()
        assert self.p.isUserAnnotate()
        assert self.p.isUserWrite()
        assert self.p.isGroupRead()
        assert self.p.isGroupAnnotate()
        assert self.p.isGroupWrite()
        assert self.p.isWorldRead()
        assert self.p.isWorldAnnotate()
        assert self.p.isWorldWrite()

        # Various swaps
        self.p.setUserRead(False)
        assert not self.p.isUserRead()
        self.p.setGroupWrite(True)
        assert self.p.isGroupWrite()

        # Now reverse each of the above
        self.p.setUserRead(True)
        assert self.p.isUserRead()
        self.p.setGroupWrite(False)
        assert not self.p.isGroupWrite()

    def testPermissionsSetters(self):
        # start with everythin false
        p = ode.model.PermissionsI('------')

        # read flags are easy, straight binary
        # user flags
        p.setUserRead(True)
        assert p.isUserRead()
        assert 'r' == str(p)[0]
        p.setUserRead(False)
        assert not p.isUserRead()
        assert '-' == str(p)[0]

        # group flags
        p.setGroupRead(True)
        assert p.isGroupRead()
        assert 'r' == str(p)[2]
        p.setGroupRead(False)
        assert not p.isGroupRead()
        assert '-' == str(p)[2]

        # world flags
        p.setWorldRead(True)
        assert p.isWorldRead()
        assert 'r' == str(p)[4]
        p.setWorldRead(False)
        assert not p.isWorldRead()
        assert '-' == str(p)[4]

        # write flags are trickier as the string
        # representation is ternary
        # user flags
        p.setUserAnnotate(True)
        assert p.isUserAnnotate()
        assert not p.isUserWrite()
        assert 'a' == str(p)[1]
        p.setUserWrite(True)
        assert p.isUserAnnotate()
        assert p.isUserWrite()
        assert 'w' == str(p)[1]
        p.setUserWrite(False)
        assert p.isUserAnnotate()
        assert not p.isUserWrite()
        assert 'a' == str(p)[1]
        p.setUserAnnotate(False)
        assert not p.isUserAnnotate()
        assert not p.isUserWrite()
        assert '-' == str(p)[1]

        # group flags
        p.setGroupAnnotate(True)
        assert p.isGroupAnnotate()
        assert not p.isGroupWrite()
        assert 'a' == str(p)[3]
        p.setGroupWrite(True)
        assert p.isGroupAnnotate()
        assert p.isGroupWrite()
        assert 'w' == str(p)[3]
        p.setGroupWrite(False)
        assert p.isGroupAnnotate()
        assert not p.isGroupWrite()
        assert 'a' == str(p)[3]
        p.setGroupAnnotate(False)
        assert not p.isGroupAnnotate()
        assert not p.isGroupWrite()
        assert '-' == str(p)[3]

        # world flags
        p.setWorldAnnotate(True)
        assert p.isWorldAnnotate()
        assert not p.isWorldWrite()
        assert 'a' == str(p)[5]
        p.setWorldWrite(True)
        assert p.isWorldAnnotate()
        assert p.isWorldWrite()
        assert 'w' == str(p)[5]
        p.setWorldWrite(False)
        assert p.isWorldAnnotate()
        assert not p.isWorldWrite()
        assert 'a' == str(p)[5]
        p.setWorldAnnotate(False)
        assert not p.isWorldAnnotate()
        assert not p.isWorldWrite()
        assert '-' == str(p)[5]

    def test8564(self):

        p = ode.model.PermissionsI("rwrwrw")
        self.assertRW(p, "User", "Group", "World")
        assert "rwrwrw" == str(p)

        p = ode.model.PermissionsI("rarara")
        self.assertRA(p, "User", "Group", "World")
        assert "rarara" == str(p)

        p = ode.model.PermissionsI("rwrar-")
        self.assertRW(p, "User")
        self.assertRA(p, "Group")
        self.assertRO(p, "World")
        assert "rwrar-" == str(p)

    # Helpers

    def assertRO(self, p, *roles):
        for role in roles:
            self.assertRAE(p, role, True, False, False)

    def assertRA(self, p, *roles):
        for role in roles:
            self.assertRAE(p, role, True, True, False)

    def assertRW(self, p, *roles):
        for role in roles:
            self.assertRAE(p, role, True, True, True)

    def assertRAE(self, p, role, read, annotate, edit):
        isRead = getattr(p, "is%sRead" % role)()
        isAnno = getattr(p, "is%sAnnotate" % role)()
        isEdit = getattr(p, "is%sWrite" % role)()

        msg = """
Permissions: %s Role: %s
Expected READ: %s \t Found: %s
Expected ANNO: %s \t Found: %s
Expected EDIT: %s \t Found: %s""" % \
            (p, role, read, isRead, annotate, isAnno, edit, isEdit)

        assert read == isRead, msg
        assert annotate == isAnno, msg
        assert edit == isEdit, msg
