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

import pytest

from ode.testlib import AbstractRepoTest
from ode.constants.namespaces import NSFSRENAME
from ode.plugins.fs import contents
from ode.plugins.fs import prep_directory
from ode.plugins.fs import rename_fileset
from ode.sys import ParametersI
from ode import SecurityViolation

# Module level marker
pytestmark = pytest.mark.fs_suite

class TestRename(AbstractRepoTest):

    def setup_method(self, method):
        self.pixels = self.client.sf.getPixelsService()
        self.query = self.client.sf.getQueryService()
        self.update = self.client.sf.getUpdateService()
        self.mrepo = self.client.getManagedRepository()

    def assert_rename(self, fileset, new_dir,
                      client=None, mrepo=None, ctx=None):
        """
        Change the path entry for the files contained
        in the orig_dir and then verify that they will
        only be listed as belonging to the new_dir. The
        files on disk ARE NOT MOVED.
        """
        if client is None:
            client = self.client
        if mrepo is None:
            mrepo = self.mrepo

        orig_dir = fileset.templatePrefix.val

        # Before the move the new location should be empty
        assert 3 == len(list(contents(mrepo, orig_dir, ctx)))
        assert 0 == len(list(contents(mrepo, new_dir, ctx)))

        rv = rename_fileset(client, mrepo, fileset, new_dir, ctx=ctx)

        # After the move, the old location should be empty
        assert 0 == len(list(contents(mrepo, orig_dir, ctx)))
        assert 3 == len(list(contents(mrepo, new_dir, ctx)))

        return rv

    def fake_move(self, tomove):
        """
        This methods uses an admin-only backdoor in order to
        perform the desired move. Sysadmins would move likely
        just perform the move manually via OS commands:

            mv old_dir/* new_dir/

        """
        for source, target in tomove:
            cb = self.raw("mv", [source, target], client=self.root)
            self.assert_passes(cb)

    @pytest.mark.parametrize("data", (
        ("user1", "user1", "rw----", True),
        ("user1", "user2", "rwra--", False),
        ("user1", "user2", "rwrw--", True),
        ("user1", "root", "rwra--", True),
        ("root", "root", "rwra--", True),
    ))
    def test_rename_permissions(self, data):
        owner, renamer, perms, allowed = data
        group = self.new_group(perms=perms)
        clients = {
            "user1": self.new_client(group=group),
            "user2": self.new_client(group=group),
            "root": self.root,
        }
        orig_img = self.import_fake_file(name="rename",
                                         sizeX=16, sizeY=16,
                                         with_companion=True,
                                         client=clients[owner])[0]
        orig_fs = self.get_fileset([orig_img], clients[owner])

        uid = orig_fs.details.owner.id.val
        gid = orig_fs.details.group.id.val

        client = clients[renamer]
        mrepo = client.getManagedRepository()
        ctx = client.getContext(group=gid)
        if renamer == "root":
            ctx["ode.user"] = str(uid)

        new_dir = prep_directory(client, mrepo)
        try:
            tomove = self.assert_rename(
                orig_fs, new_dir, client=client, mrepo=mrepo, ctx=ctx)
            assert allowed
        except SecurityViolation:
            assert not allowed

        if renamer == "root":
            self.fake_move(tomove)

    def test_rename_annotation(self):
        ns = NSFSRENAME
        mrepo = self.client.getManagedRepository()
        orig_img = self.import_fake_file(with_companion=True)
        orig_fs = self.get_fileset(orig_img)
        new_dir = prep_directory(self.client, mrepo)
        self.assert_rename(orig_fs, new_dir)
        ann = self.query.projection((
            "select a.id from FilesetAnnotationLink l "
            "join l.child as a where l.parent.id = :id "
            "and a.ns = :ns"),
            ParametersI().addId(orig_fs.id).addString("ns", ns))
        assert ann

    def test_prep_and_delete(self):
        mrepo = self.client.getManagedRepository()
        new_dir = prep_directory(self.client, mrepo)
        tree = list(contents(mrepo, new_dir))
        assert 0 == len(tree)
