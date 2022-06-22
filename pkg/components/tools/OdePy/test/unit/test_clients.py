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
Module documentation
"""

import pytest
import Ice
import logging
import threading
import ode.clients as base

class MockCommunicator(object):

    def __init__(self):
        self.props = Ice.createProperties()
        self._impl = object()

    def getLogger(self):
        return logging.getLogger("MockCommunicator")

    def getProperties(self):
        return self.props

    def getDefaultRouter(self):
        return object()

    def destroy(self):
        pass


class MockRouter(object):

    def destroySession(self):
        pass


class MockClient(base.BaseClient):

    def __init__(self):
        """
        Replicating a good deal of the __init__ setup, since the method
        is too complicated at the moment to just invoke.
        """
        self._BaseClient__agent = "t_clients"  #: See setAgent
        self._BaseClient__insecure = False
        self._BaseClient__previous = None
        self._BaseClient__ic = MockCommunicator()
        self._BaseClient__oa = None
        self._BaseClient__sf = None
        self._BaseClient__uuid = None
        self._BaseClient__resources = None
        self._BaseClient__lock = threading.RLock()

        # Logging
        self._BaseClient__logger = logging.getLogger("ode.client")
        logging.basicConfig()  # Does nothing if already configured

    def createSession(self):
        """bit of a cop out"""
        self.startKeepAlive()

    def getRouter(self, ic):
        return MockRouter()

    def assertNoResources(self):
        assert self._BaseClient__resources is None

    def assertResources(self):
        assert self._BaseClient__resources is not None

    def setSession(self):
        self._BaseClient__sf = object()


class TestKeepAlive(object):
    """
    Test that keep alives and resources only exist
    at the proper times (i.e. when a session is active)

    See #7747
    """

    def setup_method(self, method):
        self.mc = MockClient()

    def teardown_method(self, method):
        self.mc.__del__()

    def testStartsWithout(self):
        self.mc.assertNoResources()

    def testNoneOnJustEnable(self):
        self.mc.enableKeepAlive(60)
        self.mc.assertNoResources()

    def testOneAfterCreateSession(self):
        self.mc.enableKeepAlive(60)
        self.mc.assertNoResources()
        self.mc.createSession()
        self.mc.assertResources()

    def testOneOnEnableWithSession(self):
        self.mc.setSession()
        self.mc.enableKeepAlive(60)
        self.mc.assertResources()

    def testOneStartedManually(self):
        self.mc.setSession()
        self.mc.enableKeepAlive(60)
        self.mc.assertResources()

    def testClosedOnCloseSession(self):
        self.mc.setSession()
        self.mc.enableKeepAlive(60)
        self.mc.assertResources()
        self.mc.closeSession()
        self.mc.assertNoResources()

    def testClosedOnNegativeKeepAlive(self):
        self.mc.enableKeepAlive(60)
        self.mc.startKeepAlive()
        self.mc.assertResources()
        self.mc.enableKeepAlive(-1)
        self.mc.assertNoResources()


class TestHostUrlParsing(object):
    """
    Test that protocol://host/url parsing works
    """

    def setup_method(self, method):
        self.mc = MockClient()

    def teardown_method(self, method):
        self.mc.__del__()

    def _get_hosturl(self, values):
        return {
            'protocol': values[0],
            'server': values[1],
            'port': values[2],
            'path': values[3],
        }

    @pytest.mark.parametrize('host,port,pmap,expected', [
        ('test-host', None, {}, None),
        ('test-host:12345', None, {}, None),
        ('wss://test', None, {}, ('wss', 'test', 443, None)),
        ('wss://test:12345', None, {}, ('wss', 'test', 12345, None)),
        ('wss://test/sub/path', None, {}, ('wss', 'test', 443, '/sub/path')),
        ('ws://test', None, {}, ('ws', 'test', 80, None)),
        ('tcp://test', None, {}, ('tcp', 'test', 4063, None)),
        ('ssl://test', None, {}, ('ssl', 'test', 4064, None)),
        (None, None, {'ode.host': 'wss://test'},
            ('wss', 'test', 443, None)),
        (None, None, {'ode.host': 'wss://test:12345'},
            ('wss', 'test', 12345, None)),
        (None, None, {'ode.host': 'wss://test', 'ode.port': '12345'},
            ('wss', 'test', 12345, None)),
    ])
    def test_check_for_hosturl(self, host, port, pmap, expected):
        hosturl = self.mc._check_for_hosturl(host, port, pmap)
        if expected:
            expected_hosturl = self._get_hosturl(expected)
        else:
            expected_hosturl = {}
        assert expected_hosturl == hosturl

    @pytest.mark.parametrize('values,expected', [
        (('ssl', 'test', 12345, ''),
         '--Ice.Default.Router=ODE.Glacier2/router:ssl -p 12345 -h test'),
        (('wss', 'test', 12345, '/sub/path'),
         '--Ice.Default.Router=ODE.Glacier2/router:wss -p 12345 -h test '
         '-r /sub/path'),
    ])
    def test_get_endpoint_from_hosturl(self, values, expected):
        hosturl = self._get_hosturl(values)
        assert expected == self.mc._get_endpoint_from_hosturl(hosturl)
