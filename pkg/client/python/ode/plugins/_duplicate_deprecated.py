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
   Plugin for duplicating object graphs
"""

import sys
import os
import warnings

from ode.cli import CLI, GraphControl

HELP = """Duplicate Bhojpur ODE data.

Duplicate entire graphs of data based on the ID of the top-node.

Note that no object that corresponds to a file on disk will be duplicated.
In some circumstances a duplicate object may reference an original object that
does have associated binary data but a duplicated image should not be expected
to include any pixel data.

Examples:

    # Duplicate a dataset
    ode duplicate Dataset:50
    # Do the same reporting all the new duplicate objects
    ode duplicate Dataset:50 --report

    # Do a dry run of a duplicate reporting the outcome
    # if the duplicate had been run
    ode duplicate Dataset:53 --dry-run
    # Do a dry run of a duplicate, reporting all the objects
    # that would have been duplicated
    ode duplicate Dataset:53 --dry-run --report
"""

DEPRECATION_MESSAGE = (
    "This plugin is deprecated as of Bhojpur ODE.py. Use the plugin"
    " available from https://pypi.org/project/ode-cli-duplicate/"
    " instead.")

class DuplicateControl(GraphControl):

    def main_method(self, args):
        self.ctx.err(DEPRECATION_MESSAGE, DeprecationWarning)
        super(DuplicateControl, self).main_method(args)

    def cmd_type(self):
        import ode
        import ode.all
        return ode.cmd.Duplicate

    def print_detailed_report(self, req, rsp, status):
        import ode
        if isinstance(rsp, ode.cmd.DoAllRsp):
            for response in rsp.responses:
                if isinstance(response, ode.cmd.DuplicateResponse):
                    self.print_duplicate_response(response)
        elif isinstance(rsp, ode.cmd.DuplicateResponse):
            self.print_duplicate_response(rsp)

    def print_duplicate_response(self, rsp):
        if rsp.duplicates:
            self.ctx.out("Duplicates")
            objIds = self._get_object_ids(rsp.duplicates)
            for k in objIds:
                self.ctx.out("  %s:%s" % (k, objIds[k]))

try:
    if "ODE_DEV_PLUGINS" in os.environ:
        warnings.warn(DEPRECATION_MESSAGE, DeprecationWarning)
        register("duplicate", DuplicateControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("duplicate", DuplicateControl, HELP)
        cli.invoke(sys.argv[1:])