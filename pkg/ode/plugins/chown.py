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
   chown plugin

   Plugin read by ode.cli.Cli during initialization. The method(s)
   defined here will be added to the Cli class for later use.
"""

from ode.cli import CLI, GraphControl, ExperimenterArg, GraphArg
import sys

HELP = """Transfer ownership of data between users. Entire graphs of data,
based on the ID of the top-node, are transferred.

There are two ways to use this command, either specify the data to be
transferred or specify users from whom the ownership of all their data
will be transferred. These two usage ways can be combined.

The usage with specified users has to be considered as advanced usage
and might potentially be slow.

This command can only be used by Bhojpur ODE administrators and group owners.

Group owners can only transfer ownership between members of the owned group.
Administrators can transfer ownership between any users regardless of their
respective groups.

Note that after transferring ownership the data will remain in their original
group. Thus it is possible for an administrator to make the data unavailable
to the new user. In such cases the data must be moved to one of the user's
groups or the user must be added to the group that the data is in.

Examples:

    # In each case transfer the ownership of an image to user 101
    ode chown 101 Image:1
    ode chown User:101 Image:2
    ode chown Experimenter:101 Image:3
    # In both cases transfer five images to the user named jane
    ode chown jane Image:51,52,53,54,56
    ode chown jane Image:51-54,56

    # Transfer a plate but leave all images with the original owner
    ode chown 201 Plate:1 --exclude Image

    # Transfer all images contained under a project
    ode chown 101 Project/Dataset/Image:53
    # Transfer all images contained under two projects
    ode chown 101 Project/Image:201,202

    # Advanced usage & potentially slow:
    # Transfer all data owned by user 111 and one image to user 4
    ode chown 4 Experimenter:111 Image:17
    # Advanced usage & potentially slow:
    # Transfer all data of users 1, 3 and 7 to user 10
    ode chown 10 Experimenter:1,3,7

    # Do a dry run of a transfer reporting the outcome
    # if the transfer had been run
    ode chown 101 Dataset:53 --dry-run
    # Do a dry run of a transfer, reporting all the objects
    # that would have been transferred
    ode chown 101 Dataset:53 --dry-run --report
"""

class ChownControl(GraphControl):

    def cmd_type(self):
        import ode
        import ode.all
        return ode.cmd.Chown2

    def _pre_objects(self, parser):
        parser.add_argument(
            "usr", nargs="?", type=ExperimenterArg,
            help="user to transfer ownership of specified objects and/or all"
                 " objects owned by specified user(s) to")

    def _objects(self, parser):
        parser.add_argument(
            "obj", nargs="*", type=GraphArg(self.cmd_type()),
            help="objects to be processed in the form <Class>:<Id>"
                 " and/or user(s) to transfer all data from in the"
                 " form Experimenter:<Id>")

    def populate_target_users(self, command_check):
        """
        Move the Experimenters whose data are to be
        transferred from targetObjects
        to targetUsers. The rewritten _check_command
        method checked these Experimenters when they were
        in targetObjects, but Chown2 needs them in targetUsers.
        """
        try:
            command_check.targetUsers = command_check.targetObjects.pop(
                "Experimenter")
        except KeyError:
            pass

    def _check_command(self, command_check):
        """
        Rewrite higher class method to have command check
        as well as the possibility to adjust the command.
        """
        super(ChownControl, self)._check_command(command_check)
        self.populate_target_users(command_check)

    def _process_request(self, req, args, client):
        # Retrieve user id
        uid = args.usr.lookup(client)
        if uid is None:
            self.ctx.die(196, "Failed to find user: %s" % args.usr.orig)

        # Set requests user
        import ode
        if isinstance(req, ode.cmd.DoAll):
            for request in req.requests:
                if isinstance(request, ode.cmd.SkipHead):
                    request.request.userId = uid
                else:
                    request.userId = uid
        else:
            if isinstance(req, ode.cmd.SkipHead):
                req.request.userId = uid
            else:
                req.userId = uid

        super(ChownControl, self)._process_request(req, args, client)

    def print_detailed_report(self, req, rsp, status):
        import ode
        if isinstance(rsp, ode.cmd.DoAllRsp):
            for response in rsp.responses:
                if isinstance(response, ode.cmd.Chown2Response):
                    self.print_chown_response(response)
        elif isinstance(rsp, ode.cmd.Chown2Response):
            self.print_chown_response(rsp)

    def print_chown_response(self, rsp):
        if rsp.includedObjects:
            self.ctx.out("Included objects")
            objIds = self._get_object_ids(rsp.includedObjects)
            for k in objIds:
                self.ctx.out("  %s:%s" % (k, objIds[k]))
        if rsp.deletedObjects:
            self.ctx.out("Deleted objects")
            objIds = self._get_object_ids(rsp.deletedObjects)
            for k in objIds:
                self.ctx.out("  %s:%s" % (k, objIds[k]))

try:
    register("chown", ChownControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("chown", ChownControl, HELP)
        cli.invoke(sys.argv[1:])