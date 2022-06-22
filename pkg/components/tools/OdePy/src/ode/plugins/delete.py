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
   Startup plugin for command-line deletes
"""

import sys

from ode.cli import CLI, GraphControl

HELP = """Delete Bhojpur ODE data.

Remove entire graphs of data based on the ID of the top-node.

By default linked tag, file and term annotations are not deleted.
To delete linked annotations they must be explicitly included.

Examples:

    # Delete an image but not its linked tag, file and term annotations
    ode delete Image:50
    # Delete an image including linked tag, file and term annotations
    ode delete Image:51 --include TagAnnotation,FileAnnotation,TermAnnotation
    # Delete an image including all linked annotations
    ode delete Image:52 --include Annotation

    # Delete three images and two datasets including their contents
    ode delete Image:101,102,103 Dataset:201,202
    # Delete five images and four datasets including their contents
    # Note that --force flag is required when deleting a range, if not
    # passed, a dry run is performed
    ode delete Image:106-110 Dataset:203-205,207 --force
    # Delete a project excluding contained datasets and linked annotations
    ode delete Project:101 --exclude Dataset,Annotation

    # Delete all images contained under a project
    ode delete Project/Dataset/Image:53
    # Delete all images contained under two projects
    ode delete Project/Image:201,202

    # Do a dry run of a delete reporting the outcome if the delete had been run
    ode delete Dataset:53 --dry-run
    # Do a dry run of a delete, reporting all the objects
    # that would have been deleted
    ode delete Dataset:53 --dry-run --report
"""

class DeleteControl(GraphControl):

    def cmd_type(self):
        import ode
        import ode.all
        return ode.cmd.Delete2

    def print_detailed_report(self, req, rsp, status):
        import ode
        if isinstance(rsp, ode.cmd.DoAllRsp):
            for response in rsp.responses:
                if isinstance(response, ode.cmd.Delete2Response):
                    self.print_delete_response(response)
        elif isinstance(rsp, ode.cmd.Delete2Response):
            self.print_delete_response(rsp)

    def print_delete_response(self, rsp):
        if rsp.deletedObjects:
            self.ctx.out("Deleted objects")
            objIds = self._get_object_ids(rsp.deletedObjects)
            for k in objIds:
                self.ctx.out("  %s:%s" % (k, objIds[k]))

    def default_exclude(self):
        """
        Don't delete these three types of Annotation by default
        """
        return ["TagAnnotation", "TermAnnotation", "FileAnnotation"]

try:
    register("delete", DeleteControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("delete", DeleteControl, HELP)
        cli.invoke(sys.argv[1:])