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
   chgrp plugin

   Plugin read by ode.cli.Cli during initialization. The method(s)
   defined here will be added to the Cli class for later use.
"""

from ode.cli import CLI, GraphControl, ExperimenterGroupArg
import sys

HELP = """Move data between groups

Move entire graphs of data based on the ID of the top-node.

Examples:

    # In each case move an image to group 101
    ode chgrp 101 Image:1
    ode chgrp Group:101 Image:2
    ode chgrp ExperimenterGroup:101 Image:3
    # In both cases move five images to the group named "My Lab"
    ode chgrp "My Lab" Image:51,52,53,54,56
    ode chgrp "My Lab" Image:51-54,56

    # Move a plate but leave all images in the original group
    ode chgrp 201 Plate:1 --exclude Image

    # Move all images contained under a project
    ode chgrp 101 Project/Dataset/Image:53
    # Move all images contained under two projects
    ode chgrp 101 Project/Image:201,202

    # Do a dry run of a move reporting the outcome if the move had been run
    ode chgrp 101 Dataset:53 --dry-run
    # Do a dry run of a move, reporting all the objects
    # that would have been moved
    ode chgrp 101 Dataset:53 --dry-run --report
"""

class ChgrpControl(GraphControl):

    def cmd_type(self):
        import ode
        import ode.all
        return ode.cmd.Chgrp2

    def _pre_objects(self, parser):
        parser.add_argument(
            "grp", nargs="?", type=ExperimenterGroupArg,
            help="""Group to move objects to""")

    def is_admin(self, client):
        # check if the user currently logged is an admin
        from ode.model.enums import AdminPrivilegeChgrp
        ec = self.ctx.get_event_context()
        return AdminPrivilegeChgrp in ec.adminPrivileges

    def _process_request(self, req, args, client):
        # Retrieve group id
        gid = args.grp.lookup(client)
        if gid is None:
            self.ctx.die(196, "Failed to find group: %s" % args.grp.orig)

        # Retrieve group
        import ode
        try:
            group = client.sf.getAdminService().getGroup(gid)
        except ode.ApiUsageException:
            self.ctx.die(196, "Failed to find group: %s" % args.grp.orig)

        # Check session owner is member of the target group
        uid = self.ctx.get_event_context().userId
        admin = self.is_admin(client)
        ids = [x.child.id.val for x in group.copyGroupExperimenterMap()]
        # check if the user is an admin
        if uid not in ids and not admin:
            self.ctx.die(197, "Current user is not member of group: %s" %
                         group.id.val)

        # Set requests group
        if isinstance(req, ode.cmd.DoAll):
            for request in req.requests:
                if isinstance(request, ode.cmd.SkipHead):
                    request.request.groupId = gid
                else:
                    request.groupId = gid
        else:
            if isinstance(req, ode.cmd.SkipHead):
                req.request.groupId = gid
            else:
                req.groupId = gid

        super(ChgrpControl, self)._process_request(req, args, client)

    def print_detailed_report(self, req, rsp, status):
        import ode
        if isinstance(rsp, ode.cmd.DoAllRsp):
            for response in rsp.responses:
                if isinstance(response, ode.cmd.Chgrp2Response):
                    self.print_chgrp_response(response)
        elif isinstance(rsp, ode.cmd.Chgrp2Response):
            self.print_chgrp_response(rsp)

    def print_chgrp_response(self, rsp):
        if rsp.includedObjects:
            self.ctx.out("Included objects")
            obj_ids = self._get_object_ids(rsp.includedObjects)
            for k in obj_ids:
                self.ctx.out("  %s:%s" % (k, obj_ids[k]))
        if rsp.deletedObjects:
            self.ctx.out("Deleted objects")
            obj_ids = self._get_object_ids(rsp.deletedObjects)
            for k in obj_ids:
                self.ctx.out("  %s:%s" % (k, obj_ids[k]))

try:
    register("chgrp", ChgrpControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("chgrp", ChgrpControl, HELP)
        cli.invoke(sys.argv[1:])