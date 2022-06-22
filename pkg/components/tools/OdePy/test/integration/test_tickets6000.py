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
Integration tests for tickets between 5000 and 5999
"""

import pytest
from ode.testlib import ITest

from ode.rtypes import rstring

class TestTickets6000(ITest):

    @pytest.mark.broken(ticket="11539")
    def test5684(self):
        """
        Similar to integration.tickets4000.TestTickets4000.test3138
        but here we check that using a valid session UUID does *not*
        cause a wait time.

        Note: this issue only appeared initially while running with
        LDAP enabled.
        """
        client, user = self.new_client_and_user()
        uuid = client.getSessionId()
        name = user.odeName.val

        admin = self.root.sf.getAdminService()
        admin.changeUserPassword(name, rstring("GOOD"))

        # First real password attempt is fast
        self.login_attempt(name, 0.15, "GOOD", less=True)

        # First attempt with UUID is fast
        self.login_attempt(uuid, 0.15, pw=uuid, less=True)

        # Second attempt with UUID should still be fast
        self.login_attempt(uuid, 0.15, pw=uuid, less=True)

        print(client.sf)
        print(uuid)
