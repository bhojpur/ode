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

"""Test Permissions functionality of the ServerGateway wrapper."""

from ode.testlib import ITest
from ode.gateway import ServerGateway

class TestPrivileges(ITest):
    """Test getting and updating Privileges."""

    def test_update_admin_privileges(self):
        """Test getting and updating Privileges."""
        conn = ServerGateway(client_obj=self.root)
        privileges = ['Chown', 'ModifyGroup', 'ModifyUser',
                      'ModifyGroupMembership']
        exp = self.new_user(privileges=privileges)
        # Check before update
        assert set(privileges) == set(conn.getAdminPrivileges(exp.id.val))

        # Update and check again...
        conn.updateAdminPrivileges(exp.id.val, add=['Sudo', 'ModifyUser'],
                                   remove=['Chown', 'ModifyGroupMembership'])

        expected = ['Sudo', 'ModifyGroup', 'ModifyUser']
        assert set(expected) == set(conn.getAdminPrivileges(exp.id.val))

    def test_full_admin_privileges(self):
        """Test full admin privileges check."""
        # root user
        conn = ServerGateway(client_obj=self.root)
        assert conn.isFullAdmin()

        # New user with All privileges
        allPrivs = []
        for p in conn.getEnumerationEntries('AdminPrivilege'):
            allPrivs.append(p.getValue())
        client1 = self.new_client(privileges=allPrivs)
        conn1 = ServerGateway(client_obj=client1)
        assert conn1.isFullAdmin()

        # New user with Restricted privileges
        client2 = self.new_client(privileges=['Sudo', 'ReadSession'])
        conn2 = ServerGateway(client_obj=client2)
        assert not conn2.isFullAdmin()
