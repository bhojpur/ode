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
   Integration test for mail testing. Requires that the server
   have been deployed with the fake smtp server as described in
   etc/ode/mail-server.example
"""

from ode.testlib import ITest
import ode

class TestMail(ITest):

    def skipIfNot(self):
        self.skip_if("ode.mail.fake", lambda x: str(x).lower() != "true",
                     message="ode.mail.fake not configured")

        # If active, we make sure there is an admin
        # who will definitely have an email
        a = self.root.sf.getAdminService()
        r = a.getSecurityRoles()
        q = self.root.sf.getQueryService()
        with_email = q.findAllByQuery((
            "select e from Experimenter e "
            "join e.groupExperimenterMap m "
            "join m.parent g where g.id = :id "
            "and length(e.email) > 0"),
            ode.sys.ParametersI().addId(r.systemGroupId))

        if not with_email:
            self.new_user(system=True, email="random_user@localhost")

    def mailRequest(self, subject, body, everyone=False):
        req = ode.cmd.SendEmailRequest()
        req.subject = subject
        req.body = body
        req.everyone = everyone
        return req

    def assertMail(self, text):
        q = self.root.sf.getQueryService()
        assert q.findAllByQuery((
            "select a from MapAnnotation a where "
            "a.description like '%%%s%%'") % text, None,
            {"ode.group": "-1"})

    def testEveryone(self):
        self.skipIfNot()
        uuid = self.uuid()
        req = self.mailRequest(subject=uuid,
                               body="test",
                               everyone=True)
        try:
            self.root.submit(req)
        except ode.CmdError as ce:
            raise Exception(ce.err)

        self.assertMail(uuid)

    def testUserAdd(self):
        self.skipIfNot()
        uid = self.new_user().id.val
        self.assertMail("Experimenter:%s" % uid)

    def testComment(self):
        self.skipIfNot()

        group = self.new_group(perms="rwra--")  # TODO: fixture
        user = self.new_client(group=group)
        admin = user.sf.getAdminService()

        image = self.make_image(name="testOwnComments", client=user)
        ctx = admin.getEventContext()

        # Set own email
        exp = admin.getExperimenter(ctx.userId)
        exp.setEmail(ode.rtypes.rstring("tester@localhost"))
        admin.updateSelf(exp)

        commenter = self.new_client(group=group,
                                    email="commenter@localhost")
        comment = ode.model.CommentAnnotationI()
        link = self.link(image, comment, client=commenter)
        comment = link.child

        self.assertMail("CommentAnnotation:%s -" % comment.id.val)
