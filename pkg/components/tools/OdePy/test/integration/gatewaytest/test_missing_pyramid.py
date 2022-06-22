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
   gateway tests - Testing various methods on a Big image when
   renderingEngine.load() etc throws MissingPyramidException
"""

import ode
import pytest

class TestPyramid (object):

    @pytest.fixture(autouse=True)
    def setUp(self, author_testimg_generated):
        self.image = author_testimg_generated

    def testThrowException(self):
        """ test that image._prepareRE() throws MissingPyramidException """
        self.image._conn.createRenderingEngine = lambda: MockRenderingEngine()

        try:
            self.image._prepareRE()
            assert False, "_prepareRE should have thrown an exception"
        except ode.ConcurrencyException as ce:
            print('Handling MissingPyramidException with backoff: %s secs' \
                % (ce.backOff/1000))

    def testPrepareRenderingEngine(self):
        """
        We need image._prepareRenderingEngine() to raise
        MissingPyramidException
        """
        self.image._conn.createRenderingEngine = lambda: MockRenderingEngine()

        try:
            self.image._prepareRenderingEngine()
            assert False, \
                "_prepareRenderingEngine() should have thrown an exception"
        except ode.ConcurrencyException as ce:
            print('Handling MissingPyramidException with backoff: %s secs' \
                % (ce.backOff/1000))

    def testGetChannels(self):
        """ Missing Pyramid shouldn't stop us from getting Channel Info """
        self.image._conn.createRenderingEngine = lambda: MockRenderingEngine()

        channels = self.image.getChannels()
        for c in channels:
            print(c.getLabel())

    def testGetChannelsNoRe(self):
        """ With noRE, getChannels() shouldn't need rendering Engine """
        self.image._conn.createRenderingEngine = lambda: None

        channels = self.image.getChannels(noRE=True)
        assert len(channels) > 0
        for c in channels:
            print(c.getLabel())

    def testGetRdefId(self):
        """ getRenderingDefId() silently returns None with Missing Pyramid """
        self.image._conn.createRenderingEngine = lambda: MockRenderingEngine()

        assert self.image.getRenderingDefId() is None


class MockRenderingEngine(object):
    """ Should throw on re.load() """

    def lookupPixels(self, id, ctx=None):
        pass

    def lookupRenderingDef(self, id, ctx=None):
        pass

    def loadRenderingDef(self, id, ctx=None):
        pass

    def resetDefaultSettings(self, save=True, ctx=None):
        pass

    def getRenderingDefId(self, ctx=None):
        return 1

    def load(self, ctx=None):
        e = ode.ConcurrencyException("MOCK MissingPyramidException")
        # 3 hours
        e.backOff = (3 * 60 * 60 * 1000) + (20 * 60 * 1000) + (45 * 1000)
        raise e
