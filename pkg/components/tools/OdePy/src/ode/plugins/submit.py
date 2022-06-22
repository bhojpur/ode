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
   submit plugin

   Plugin read by ode.cli.Cli during initialization. The method(s)
   defined here will be added to the Cli class for later use.
"""

from __future__ import print_function

from builtins import str
from core.cli import BaseControl, CLI
import sys

prompt = "ode submit [%s]> "

class Save(Exception):
    pass

class Cancel(Exception):
    pass

class SubmitCLI(CLI):

    def __init__(self):
        CLI.__init__(self)
        self.queue = []
        self.prompt = prompt % str(0)

    def postcmd(self, stop, line):
        self.queue.append(line)
        self.prompt = prompt % str(len(self.queue))
        return CLI.postcmd(self, stop, line)

    def do_save(self, arg):
        raise Save()

    def do_cancel(self, arg):
        raise Cancel()

    def post_process(self):
        print("Uploading")
        print(self.queue)

HELP = """When run without arguments, submit shell is opened
which takes commands without executing them. On save,
the file is trasferred to the server, and executed."""

class SubmitControl(BaseControl):

    def _configure(self, parser):
        parser.add_argument("arg", nargs="*", help="single command with args")
        parser.set_defaults(func=self.__call__)

    def __call__(self, args):
        submit = SubmitCLI()
        arg = args.arg
        if arg and len(arg) > 0:
            submit.invoke(arg)
            submit.post_process()
        else:
            try:
                submit.invokeloop()
            except Save:
                submit.execute()
            except Cancel:
                l = len(submit.queue)
                if l > 0:
                    print(l, " items queued. Really cancel? [Yn]")

try:
    # register("submit", SubmitControl, HELP)
    pass
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("submit", SubmitControl, HELP)
        cli.invoke(sys.argv[1:])