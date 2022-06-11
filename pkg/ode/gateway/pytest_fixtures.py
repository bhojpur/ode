#!/usr/bin/env python
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
   conftest.py - py.test fixtures for gatewaytest
"""

from ode.gateway.scripts.testdb_create import TestDBHelper, dbhelpers

import pytest

class GatewayWrapper (TestDBHelper):

    def __init__(self):
        super(GatewayWrapper, self).__init__()
        self.setUp(skipTestDB=False, skipTestImages=True)

    def createTestImg_generated(self):
        ds = self.getTestDataset()
        assert ds
        testimg = self.createTestImage(dataset=ds)
        return testimg

@pytest.fixture(scope='class')
def gatewaywrapper(request):
    """
    Returns a test helper gateway object.
    """
    g = GatewayWrapper()

    def fin():
        g.tearDown()
        dbhelpers.cleanup()
    request.addfinalizer(fin)
    return g

@pytest.fixture(scope='function')
def author_testimg_generated(request, gatewaywrapper):
    """
    logs in as Author and returns the test image, creating it first if needed.
    """
    gatewaywrapper.loginAsAuthor()
    rv = gatewaywrapper.createTestImg_generated()
    return rv

@pytest.fixture(scope='function')
def author_testimg_tiny(request, gatewaywrapper):
    """
    logs in as Author and returns the test image, creating it first if needed.
    """
    gatewaywrapper.loginAsAuthor()
    rv = gatewaywrapper.getTinyTestImage(autocreate=True)
    return rv

@pytest.fixture(scope='function')
def author_testimg_tiny2(request, gatewaywrapper):
    """
    logs in as Author and returns the test image, creating it first if needed.
    """
    gatewaywrapper.loginAsAuthor()
    rv = gatewaywrapper.getTinyTestImage2(autocreate=True)
    return rv

@pytest.fixture(scope='function')
def author_testimg(request, gatewaywrapper):
    """
    logs in as Author and returns the test image, creating it first if needed.
    """
    gatewaywrapper.loginAsAuthor()
    rv = gatewaywrapper.getTestImage(autocreate=True)
    return rv

@pytest.fixture(scope='function')
def author_testimg_bad(request, gatewaywrapper):
    """
    logs in as Author and returns the test image, creating it first if needed.
    """
    gatewaywrapper.loginAsAuthor()
    rv = gatewaywrapper.getBadTestImage(autocreate=True)
    return rv

@pytest.fixture(scope='function')
def author_testimg_big(request, gatewaywrapper):
    """
    logs in as Author and returns the test image, creating it first if needed.
    """
    gatewaywrapper.loginAsAuthor()
    rv = gatewaywrapper.getBigTestImage(autocreate=True)
    return rv

@pytest.fixture(scope='function')
def author_testimg_32float(request, gatewaywrapper):
    """
    logs in as Author and returns the float image, creating it first if needed.
    """
    gatewaywrapper.loginAsAuthor()
    rv = gatewaywrapper.get32FloatTestImage(autocreate=True)
    return rv