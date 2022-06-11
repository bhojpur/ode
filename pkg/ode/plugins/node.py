#!/usr/bin/env python
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
 Bhojpur ODE Grid node controller

 This is a python wrapper around icegridnode.
"""

from __future__ import division
from __future__ import print_function

from builtins import str
from past.utils import old_div
from ode.cli import BaseControl, CLI, NonZeroReturnCode
from ode.util import tail_lines
import os
import sys
import signal
import platform
from extras.path import path

from ode.install.windows_warning import windows_warning, WINDOWS_WARNING

HELP = """Control icegridnode.

           start       -- Start the node via icegridnode. With sync doesn't \
return until reachable.
           stop        -- Stop the node via icegridadmin. With sync doesn't \
return until stopped.
           status      -- Prints a status message. Return code is non-zero if\
 there is a problem.
           restart     -- Calls "sync start" then "stop" ("sync stop" if sync\
 is specified)

        node-name cannot be "start", "stop", "restart", "status", or "sync".
"""

if platform.system() == 'Windows':
    HELP += ("\n\n%s" % WINDOWS_WARNING)

class NodeControl(BaseControl):

    def _configure(self, parser):
        parser.add_argument(
            "name", nargs="?",
            help="Optional name of this node.", default=self._node())
        parser.add_argument(
            "sync", nargs="?", choices=("sync",),
            help="Whether or not to call wait on results")
        parser.add_argument(
            "command", nargs="+",
            choices=("start", "stop", "status", "restart"))
        parser.add_argument(
            "--foreground", action="store_true",
            help="Start in foreground mode (no daemon/service)")
        parser.set_defaults(func=self.__call__)

    def __call__(self, args):
        self._node(args.name)  # Set environment value
        for act in args.command:
            c = getattr(self, act)
            c(args)

    def _handleNZRC(self, nzrc):
        """
        Set the return value from nzrc on the context, and print
        out the last two lines of any error messages if present.
        """
        props = self._properties()
        self.ctx.rv = nzrc.rv
        myoutput = old_div(self.dir, path(props["Ice.StdErr"]))
        if not myoutput.exists():
            pass
        else:
            print("from %s:" % str(myoutput))
            print(tail_lines(str(myoutput), 2))

    @windows_warning
    def start(self, args):

        self.ctx.invoke(["admin", "rewrite"])
        self._initDir()

        try:
            command = ["icegridnode", self._icecfg()]
            if self._isWindows():
                self.ctx.die(128, "Not implemented")
                # The following code clearly hasn't been tested.
                # TODO: Fix this or remove it completely
                command = command + ["--install", "ODE."+args.node]
                self.ctx.call(command)
                self.ctx.call(["icegridnode", "--start", "ODE."+args.node])
            else:
                if args.foreground:
                    command = command + ["--nochdir"]
                else:
                    command = command + ["--daemon", "--pidfile",
                                         str(self._pid()), "--nochdir"]
                self.ctx.call(command)
        except OSError as o:
                msg = """%s\nPossibly an error finding "icegridnode". Try \
"icegridnode -h" from the command line.""" % o
                raise Exception(msg)
        except NonZeroReturnCode as nzrc:
                self._handleNZRC(nzrc)

    def status(self, args):
        self.ctx.invoke(["admin", "status", args.name])

    @windows_warning
    def stop(self, args):
        if self._isWindows():
            try:
                command = ["icegridnode", "--stop", "ODE."+args.name]
                self.ctx.call(command)
                command = ["icegridnode", "--uninstall", "ODE."+args.name]
                self.ctx.call(command)
            except NonZeroReturnCode as nzrc:
                self._handleNZRC(nzrc)
        else:
                pid = open(self._pid(), "r").readline()
                os.kill(int(pid), signal.SIGTERM)
                # command = ["icegridadmin"] + [self._intcfg()] + ["-c", "node
                # shutdown %s" % args.name]
                # self.ctx.call(command)

    @windows_warning
    def kill(self, args):
        pid = open(self._pid(), "r").readline()
        os.kill(int(pid), signal.SIGKILL)

try:
    register("node", NodeControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.loadplugins()
        cli.invoke(sys.argv[1:])