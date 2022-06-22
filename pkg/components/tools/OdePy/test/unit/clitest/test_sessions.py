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

from ode.cli import CLI
from ode.plugins.sessions import SessionsControl
import pytest

class TestSessions(object):

    def setup_method(self, method):
        self.cli = CLI()
        self.cli.register("sessions", SessionsControl, "TEST")
        self.args = ["sessions"]

    def testHelp(self):
        self.args += ["-h"]
        self.cli.invoke(self.args, strict=True)

    @pytest.mark.parametrize(
        "subcommand", SessionsControl().get_subcommands())
    def testSubcommandHelp(self, subcommand):
        self.args += [subcommand, "-h"]
        self.cli.invoke(self.args, strict=True)

    def testDefaultSessionsDir(self):
        from ode.util import get_user_dir
        from path import path

        # Default store sessions dir is under user dir
        store = self.cli.controls['sessions'].store(None)
        assert store.dir == path(get_user_dir()) / 'ode' / 'sessions'

    @pytest.mark.parametrize('environment', (
        {'ODE_USERDIR': None,
         'ODE_SESSION_DIR': None,
         'ODE_SESSIONDIR': None},
        {'ODE_USERDIR': None,
         'ODE_SESSION_DIR': 'session_dir',
         'ODE_SESSIONDIR': None},
        {'ODE_USERDIR': None,
         'ODE_SESSION_DIR': None,
         'ODE_SESSIONDIR': 'sessiondir'},
        {'ODE_USERDIR': 'userdir',
         'ODE_SESSION_DIR': None,
         'ODE_SESSIONDIR': None},
        {'ODE_USERDIR': None,
         'ODE_SESSION_DIR': 'session_dir',
         'ODE_SESSIONDIR': 'sessiondir'},
        {'ODE_USERDIR': 'userdir',
         'ODE_SESSION_DIR': 'session_dir',
         'ODE_SESSIONDIR': None},
        {'ODE_USERDIR': 'userdir',
         'ODE_SESSION_DIR': None,
         'ODE_SESSIONDIR': 'sessiondir'},
        {'ODE_USERDIR': 'userdir',
         'ODE_SESSION_DIR': 'session_dir',
         'ODE_SESSIONDIR': 'sessiondir'}))
    @pytest.mark.parametrize('session_args', [None, 'session_dir'])
    def testCustomSessionsDir(
            self, tmpdir, monkeypatch, environment,
            session_args):
        from argparse import Namespace
        from ode.util import get_user_dir
        from path import path

        for var in environment.keys():
            if environment[var]:
                monkeypatch.setenv(var, tmpdir / environment.get(var))
            else:
                monkeypatch.delenv(var, raising=False)

        # args.session_dir sets the sessions dir
        args = Namespace()
        if session_args:
            setattr(args, session_args, tmpdir / session_args)

        if environment.get('ODE_SESSION_DIR') or session_args:
            pytest.deprecated_call(self.cli.controls['sessions'].store, args)

        store = self.cli.controls['sessions'].store(args)
        # By order of precedence
        if environment.get('ODE_SESSIONDIR'):
            sdir = path(tmpdir) / environment.get('ODE_SESSIONDIR')
        elif environment.get('ODE_SESSION_DIR'):
            sdir = (path(tmpdir) / environment.get('ODE_SESSION_DIR') /
                    'ode' / 'sessions')
        elif session_args:
            sdir = path(getattr(args, session_args)) / 'ode' / 'sessions'
        elif environment.get('ODE_USERDIR'):
            sdir = path(tmpdir) / environment.get('ODE_USERDIR') / 'sessions'
        else:
            sdir = path(get_user_dir()) / 'ode' / 'sessions'
        assert store.dir == sdir
