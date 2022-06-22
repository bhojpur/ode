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

from ode.testlib.cli import CLITest, RootCLITest
from ode.cli import NonZeroReturnCode
from ode.plugins.fs import FsControl

import pytest
import ode

transfers = ['ln_s', 'ln', 'ln_rm', 'cp', 'cp_rm']
repos = ['Managed', 'Public', 'Script']


class TestFS(CLITest):

    def setup_method(self, method):
        super(TestFS, self).setup_method(method)
        self.cli.register("fs", FsControl, "TEST")
        self.args += ["fs"]

    def set_conn_args(self):
        passwd = self.root.getProperty("ode.rootpass")
        host = self.root.getProperty("ode.host")
        port = self.root.getProperty("ode.port")
        self.args = ["fs", "-w", passwd]
        self.args += ["-s", host, "-p",  port]

    def parse_ids(self, output):
        ids = []
        for line in output.split('\n')[:-1]:
            ids.append(int(line.split(',')[1]))
        return ids

    def parse_repos(self, output):
        ids = []
        for line in output.split('\n')[:-1]:
            ids.append(line.split(',')[3])
        return ids

    def testRepos(self, capsys):
        """Test fs repos subcommand"""

        self.args += ["repos", "--style=plain"]
        self.cli.invoke(self.args, strict=True)
        o, e = capsys.readouterr()

        errs = [line
                for line in e.split("\n")
                if line.strip() and "Joined session" not in line]
        if errs:
            raise Exception(errs)

        assert set(self.parse_repos(o)) == set(repos)

    def testSetsWithTransfer(self, capsys):
        """Test --with-transfer option of fs sets subcommand"""

        f = {}
        i0 = self.import_fake_file()
        f[None] = self.get_fileset(i0)

        for transfer in transfers:
            i = self.import_fake_file(extra_args=['--transfer=%s' % transfer])
            f[transfer] = self.get_fileset(i)

        self.args += ["sets", "--style=plain"]
        self.cli.invoke(self.args, strict=True)
        o, e = capsys.readouterr()
        printed_ids = set(self.parse_ids(o))
        expected_ids = set([x.id.val for x in f.values()])
        assert expected_ids.issubset(printed_ids)

        for transfer in transfers:
            self.cli.invoke(self.args + ['--with-transfer', '%s' % transfer],
                            strict=True)
            o, e = capsys.readouterr()
            printed_ids = set(self.parse_ids(o))
            expected_ids = set([x.id.val for (k, x) in f.iteritems()
                                if k == transfer])
            assert expected_ids.issubset(printed_ids)

    def testSetsAdminOnly(self, capsys):
        """Test fs sets --check is admin-only"""

        self.args += ["sets", "--check"]
        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke(self.args, strict=True)
        out, err = capsys.readouterr()
        assert err.endswith("SecurityViolation: Admins only!\n")

    @pytest.mark.broken(reason="fs rename is temporarily disabled")
    def testRenameAdminOnly(self, capsys):
        """Test fs rename is admin-only"""

        self.args += ["rename", "Fileset:1"]
        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke(self.args, strict=True)
        out, err = capsys.readouterr()
        assert err.endswith("SecurityViolation: Admins only!\n")

    def testMkdirAdminOnly(self, capsys):
        """Test fs mkdir is admin-only"""

        directory_name = self.uuid()
        self.args += ["mkdir", directory_name]
        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke(self.args, strict=True)
        out, err = capsys.readouterr()
        assert err.endswith("SecurityViolation: Admins only!\n")


class TestFsRoot(RootCLITest):

    def setup_method(self, method):
        super(TestFsRoot, self).setup_method(method)
        self.cli.register("fs", FsControl, "TEST")
        self.args += ["fs", "mkdir"]

    def testMkdirAsAdminSimpleDirCreation(self, capsys):
        """Test fs mkdir simple directory creation workflows"""

        top_directory_name = self.uuid()
        args_simple = self.args + [top_directory_name]
        """mkdir of simple non-existing directory succeeds"""
        self.cli.invoke(args_simple, strict=True)
        """mkdir of pre-existing simple directory fails"""
        with pytest.raises(ode.ResourceError) as exc_info:
            self.cli.invoke(args_simple, strict=True)
        assert "Path exists on disk" in exc_info.value.message
        """mkdir passes with --parents flag even when directory exists"""
        args_simple_parents = args_simple + ["--parents"]
        self.cli.invoke(args_simple_parents, strict=True)
        """mkdir of directory within preexisting directory succeeds"""
        subdirectory_name = self.uuid()
        args_hierarchy = self.args + [top_directory_name + "/" +
                                      subdirectory_name]
        self.cli.invoke(args_hierarchy, strict=True)
        """Second mkdir of a subdirectory fails as the subdirectory exists"""
        with pytest.raises(ode.ResourceError) as exc_info:
            self.cli.invoke(args_hierarchy, strict=True)
        assert "Path exists on disk" in exc_info.value.message

    def testMkdirAsAdminHierarchyOnlyPreexisting(self, capsys):
        """Test fs mkdir hierarchy is pre-existing only"""

        top_directory_name = self.uuid()
        subdirectory_name = self.uuid()
        args_hierarchy = self.args + [top_directory_name + "/" +
                                      subdirectory_name]
        """mkdir of non-existing top_directory/subdirectory hierarchy fails"""
        with pytest.raises(ode.SecurityViolation) as exc_info:
            self.cli.invoke(args_hierarchy, strict=True)
        assert "Cannot find parent directory" in exc_info.value.message

    def testMkdirAsAdminHierarchyParents(self, capsys):
        """Test fs mkdir hierarchy with --parents workflows"""

        top_directory_name = self.uuid()
        subdirectory_name = self.uuid()
        """mkdir of a non-existing top_directory/subdirectory
        hierarchy passes with --parents"""
        args_hierarchy_parents = self.args + [top_directory_name + "/" +
                                              subdirectory_name, "--parents"]
        self.cli.invoke(args_hierarchy_parents, strict=True)
        """mkdir of pre-existing (top) directory fails"""
        args_simple = self.args + [top_directory_name]
        with pytest.raises(ode.ResourceError) as exc_info:
            self.cli.invoke(args_simple, strict=True)
        assert "Path exists on disk" in exc_info.value.message
        """mkdir of pre-existing top_directory/subdirectory hierarchy fails"""
        args_hierarchy = self.args + [top_directory_name + "/" +
                                      subdirectory_name]
        with pytest.raises(ode.ResourceError) as exc_info:
            self.cli.invoke(args_hierarchy, strict=True)
        assert "Path exists on disk" in exc_info.value.message
        """mkdir of pre-existing top directory with --parents succeeds"""
        args_simple_parents = args_simple + ["--parents"]
        self.cli.invoke(args_simple_parents, strict=True)
        """mkdir of pre-existing hierarchy with --parents succeeds"""
        self.cli.invoke(args_hierarchy_parents, strict=True)
