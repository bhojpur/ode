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
Code-generated tests for which run through all the
sensible usages of ImageWrapper.getThumbnail()
"""

from ode.testlib import ITest

def generate_parameters():
    """
    Generate tuples of parameters for possible
    methods. Some may be skipped later.
    """
    for perms in ("rw", "rwr"):
        for testertype in ("root", "member"):
            for direct in (True, False):
                for grpctx in (True, False):
                    for size in (16, 96):
                        yield (perms, testertype, direct, grpctx, size)

class Test10618(ITest):
    """
    Holder for all of the generated methods.
    """
    pass

#
# Primary method-generation loop
#
for perms, testertype, direct, grpctx, size in generate_parameters():

    if perms == "rw" and testertype == "member":
        # A member in a private group will never be able
        # to load the ImageWrapper and therefore need not
        # be tested.
        continue

    def dynamic_test(self, perms=perms, testertype=testertype,
                     direct=direct, size=size):

        group = self.new_group(perms=perms.ljust(6, "-"))
        owner = self.new_client(group=group)
        image = self.create_test_image(session=owner.sf)

        if testertype == "root":
            tester = self.root
        elif testertype == "member":
            tester = self.new_client(group=group)

        import ode.gateway
        conn = ode.gateway.ServerGateway(client_obj=tester)

        if grpctx:
            conn.SERVICE_OPTS.setOdeGroup(str(group.id.val))
        else:
            conn.SERVICE_OPTS.setOdeGroup(str(-1))

        img = conn.getObject("Image", image.id)
        assert img.getThumbnail(size=size, direct=direct)

    test_name = "test_%s_%s_dir%s_grp%s_%s" % \
                (perms, testertype, direct, grpctx, size)
    setattr(Test10618, test_name, dynamic_test)
