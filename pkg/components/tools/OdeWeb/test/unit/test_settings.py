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
Simple integration tests to ensure that the settings are working correctly.
"""

from connector import Server

# Test model
class TestServerModel (object):

    def test_constructor(self):
        Server.reset()
        # Create object with alias
        Server(host=u'example.com', port=4064, server=u'ome')

        # Create object without alias
        Server(host=u'example2.com', port=4065)

        # without any params
        try:
            Server()
        except TypeError as te:
            assert str(te) == '__new__() takes at least 3 arguments (1 given)'

    def test_get_and_find(self):
        Server.reset()

        SERVER_LIST = [[u'example1.com', 4064, u'ode1'],
                       [u'example2.com', 4064, u'ode2'],
                       [u'example3.com', 4064],
                       [u'example4.com', 4064]]
        for s in SERVER_LIST:
            server = (len(s) > 2) and s[2] or None
            Server(host=s[0], port=s[1], server=server)

        s1 = Server.get(1)
        assert s1.host == u'example1.com'
        assert s1.port == 4064
        assert s1.server == u'ode1'

        s2 = Server.find('example2.com')[0]
        assert s2.host == u'example2.com'
        assert s2.port == 4064
        assert s2.server == u'ode2'

    def test_load_server_list(self):
        Server.reset()

        SERVER_LIST = [[u'example1.com', 4064, u'ode1'],
                       [u'example2.com', 4064, u'ode2'],
                       [u'example3.com', 4064],
                       [u'example4.com', 4064]]
        for s in SERVER_LIST:
            server = (len(s) > 2) and s[2] or None
            Server(host=s[0], port=s[1], server=server)
        Server.freeze()

        try:
            Server(host=u'example5.com', port=4064)
        except TypeError as te:
            assert str(te) == 'No more instances allowed'

        Server(host=u'example1.com', port=4064)
