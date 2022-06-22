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
   Test of the ode db control.
"""

import pytest
import os
from path import path
from ode.plugins.db import DatabaseControl
from ode.util.temp_files import create_path
from ode.cli import NonZeroReturnCode
from ode.cli import CLI
from extras.mox import Mox
import getpass
import __builtin__

hash_map = {
    ('0', ''): 'PJueOtwuTPHB8Nq/1rFVxg==',
    ('0', '--no-salt'): 'vvFwuczAmpyoRC0Nsv8FCw==',
    ('1', ''): 'pvL5Tyr9tCD2esF938sHEQ==',
    ('1', '--no-salt'): 'vvFwuczAmpyoRC0Nsv8FCw==',
}

class TestDatabase(object):

    def setup_method(self, method):
        self.cli = CLI()
        self.cli.register("db", DatabaseControl, "TEST")
        self.args = ["db"]

        dir = path(__file__) / ".." / ".." / ".." / ".." / ".." / ".." /\
            ".." / "dist"  # FIXME: should not be hard-coded
        dir = dir.abspath()
        cfg = dir / "etc" / "ode.properties"
        cfg = cfg.abspath()
        self.cli.dir = dir

        self.data = {}
        for line in cfg.text().split("\n"):
            line = line.strip()
            for x in ("version", "patch"):
                key = "ode.db." + x
                if line.startswith(key):
                    self.data[x] = line[len(key)+1:]

        self.file = create_path()
        self.script_file = "%(version)s__%(patch)s.sql" % self.data
        if os.path.isfile(self.script_file):
            os.rename(self.script_file, self.script_file + '.bak')
        assert not os.path.isfile(self.script_file)

        self.mox = Mox()
        self.mox.StubOutWithMock(getpass, 'getpass')
        self.mox.StubOutWithMock(__builtin__, "raw_input")

    def teardown_method(self, method):
        self.file.remove()
        if os.path.isfile(self.script_file):
            os.remove(self.script_file)
        if os.path.isfile(self.script_file + '.bak'):
            os.rename(self.script_file + '.bak', self.script_file)

        self.mox.UnsetStubs()
        self.mox.VerifyAll()

    def password(self, string, strict=True):
        self.cli.invoke("db password " + string % self.data, strict=strict)

    def testHelp(self):
        self.args += ["-h"]
        self.cli.invoke(self.args, strict=True)

    @pytest.mark.parametrize(
        'subcommand', DatabaseControl().get_subcommands())
    def testSubcommandHelp(self, subcommand):
        self.args += [subcommand, "-h"]
        self.cli.invoke(self.args, strict=True)

    def testBadVersionDies(self):
        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke("db script NONE NONE pw", strict=True)

    def testPasswordIsAskedForAgainIfDiffer(self):
        self.expectPassword("ode")
        self.expectConfirmation("bad")
        self.expectPassword("ode")
        self.expectConfirmation("ode")
        self.mox.ReplayAll()
        self.password("")

    def testPasswordIsAskedForAgainIfEmpty(self):
        self.expectPassword("")
        self.expectPassword("ode")
        self.expectConfirmation("ode")
        self.mox.ReplayAll()
        self.password("")

    @pytest.mark.parametrize('no_salt', ['', '--no-salt'])
    @pytest.mark.parametrize('user_id', ['', '0', '1'])
    @pytest.mark.parametrize('password', ['', 'ode'])
    def testPassword(self, user_id, password, no_salt, capsys):
        args = ""
        if user_id:
            args += "--user-id=%s " % user_id
        if no_salt:
            args += "%s " % no_salt
        if password:
            args += "%s" % password
        else:
            self.expectPassword("ode", id=user_id)
            self.expectConfirmation("ode", id=user_id)
            self.mox.ReplayAll()
        self.password(args)
        out, err = capsys.readouterr()
        assert out.strip() == self.password_output(user_id, no_salt)

    @pytest.mark.parametrize('file_arg', ['', '-f', '--file'])
    @pytest.mark.parametrize('no_salt', ['', '--no-salt'])
    @pytest.mark.parametrize('password', ['', '--password ode'])
    def testScript(self, no_salt, file_arg, password, capsys):
        """
        Recommended usage of db script
        """
        args = "db script " + password
        if no_salt:
            args += " %s" % no_salt
        if file_arg:
            args += " %s %s" % (file_arg, str(self.file))
            output = self.file
        else:
            output = self.script_file

        if not password:
            self.expectPassword("ode")
            self.expectConfirmation("ode")
        self.mox.ReplayAll()

        self.cli.invoke(args, strict=True)

        out, err = capsys.readouterr()
        assert 'Using %s for version' % self.data['version'] in err
        assert 'Using %s for patch' % self.data['patch'] in err
        if password:
            assert 'Using password from commandline' in err

        with open(output) as f:
            lines = f.readlines()
            for line in lines:
                if line.startswith('insert into password values (0'):
                    assert line.strip() == self.script_output(no_salt)

    @pytest.mark.parametrize('file_arg', ['', '-f', '--file'])
    @pytest.mark.parametrize('no_salt', ['', '--no-salt'])
    @pytest.mark.parametrize('pos_args', [
        '%s %s %s', '--version %s --patch %s --password %s'])
    def testScriptDeveloperArgs(self, pos_args, no_salt, file_arg, capsys):
        """
        Deprecated and developer usage of db script
        """
        arg_values = ('VERSION', 'PATCH', 'PASSWORD')
        args = "db script " + pos_args % arg_values
        if no_salt:
            args += " %s" % no_salt
        if file_arg:
            args += " %s %s" % (file_arg, str(self.file))
            self.file
        else:
            self.script_file
        self.mox.ReplayAll()

        with pytest.raises(NonZeroReturnCode):
            self.cli.invoke(args, strict=True)

        out, err = capsys.readouterr()

        assert 'Using %s for version' % (arg_values[0]) in err
        assert 'Using %s for patch' % (arg_values[1]) in err
        assert 'Using password from commandline' in err
        assert 'Invalid Database version/patch' in err

    def password_ending(self, user, id):
        if id and id != '0':
            rv = "user %s: " % id
        else:
            rv = "%s user: " % user
        return "password for Bhojpur ODE " + rv

    def expectPassword(self, pw, user="root", id=None):
        getpass.getpass("Please enter %s" %
                        self.password_ending(user, id)).AndReturn(pw)

    def expectConfirmation(self, pw, user="root", id=None):
        getpass.getpass("Please re-enter %s" %
                        self.password_ending(user, id)).AndReturn(pw)

    def password_output(self, user_id, no_salt):
        update_msg = "UPDATE password SET hash = \'%s\'" \
            " WHERE experimenter_id = %s;"
        if not user_id:
            user_id = "0"
        return update_msg % (hash_map[(user_id, no_salt)], user_id)

    def script_output(self, no_salt):
        root_password_msg = "insert into password values (0,\'%s\');"
        return root_password_msg % (hash_map[("0", no_salt)])
