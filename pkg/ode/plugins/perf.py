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
   Plugin for measuring the performance of a Bhojpur  ODE
   installation.

   Plugin read by ode.cli.Cli during initialization. The method(s)
   defined here will be added to the Cli class for later use.
"""
from __future__ import print_function

import sys
from ode.cli import BaseControl, CLI
from extras.argparse import FileType
import ode.install.perf_test as perf_test

HELP = """Run perf_test files

%s

""" % perf_test.FILE_FORMAT

class PerfControl(BaseControl):

    def _configure(self, parser):
        parser.add_argument(
            "-l", "--list", action="store_true",
            help="List available commands")
        parser.add_argument(
            "file", nargs="*", type=FileType('r'), default=None,
            help="Read from files or standard in")
        parser.set_defaults(func=self.__call__)
        parser.add_login_arguments()

    def __call__(self, args):
        if args.list:
            ops = [x[4:] for x in dir(perf_test.Item) if x.startswith("_op_")]
            ops.sort()
            for op in ops:
                print(op)
        else:
            if not args.file:
                self.ctx.die(167, "No files given. Use '-' for stdin.")
            client = self.ctx.conn(args)
            ctx = perf_test.Context(None, client=client)
            self.ctx.out("Saving performance results to %s" % ctx.dir)
            ctx.add_reporter(perf_test.CsvReporter(ctx.dir))
            # ctx.add_reporter(perf_test.HdfReporter(ctx.dir))
            # ctx.add_reporter(perf_test.PlotReporter())
            handler = perf_test.PerfHandler(ctx)
            perf_test.handle(handler, args.file)

try:
    register("perf", PerfControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("perf", PerfControl, HELP)
        cli.invoke(sys.argv[1:])