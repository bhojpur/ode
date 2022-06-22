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
Test of various things under ode.gateway
"""

import Ice
import pytest

from ode.gateway import ServerGateway, ImageWrapper
from ode.model import ImageI, PixelsI, ExperimenterI, EventI
from ode.rtypes import rstring, rtime, rlong, rint

class MockQueryService(object):

    def findByQuery(self, query, params, _ctx=None):
        experimenter = ExperimenterI()
        experimenter.firstName = rstring('first_name')
        experimenter.lastName = rstring('last_name')
        return experimenter

class MockConnection(object):

    SERVICE_OPTS = dict()

    def getQueryService(self):
        return MockQueryService()

    def getMaxPlaneSize(self):
        return (64, 64)


@pytest.fixture(scope='function')
def wrapped_image():
    image = ImageI()
    image.id = rlong(1)
    image.description = rstring('description')
    image.name = rstring('name')
    image.acquisitionDate = rtime(1000)  # In milliseconds
    image.details.owner = ExperimenterI(1, False)
    creation_event = EventI()
    creation_event.time = rtime(2000)  # In milliseconds
    image.details.creationEvent = creation_event
    return ImageWrapper(conn=MockConnection(), obj=image)


class TestServerGatewayUnicode(object):
    """
    Tests to ensure that unicode encoding of usernames and passwords are
    performed successfully.  `gateway.connect()` will not even attempt to
    perform a connection and just return `False` if the encoding fails.
    """

    def test_unicode_username(self):
        with pytest.raises(Ice.ConnectionRefusedException):
            gateway = ServerGateway(
                username=u'ążźćółę', passwd='secret',
                host='localhost', port=65535
            )
            gateway.connect()

    def test_unicode_password(self):
        with pytest.raises(Ice.ConnectionRefusedException):
            gateway = ServerGateway(
                username='user', passwd=u'ążźćółę',
                host='localhost', port=65535
            )
            gateway.connect()


class TestServerGatewayImageWrapper(object):
    """Tests for various methods associated with the `ImageWrapper`."""

    def assert_data(self, data):
        assert data['description'] == 'description'
        assert data['author'] == 'first_name last_name'
        assert data['date'] == 1.0  # In seconds
        assert data['type'] == 'Image'
        assert data['id'] == 1
        assert data['name'] == 'name'

    def test_simple_marshal(self, wrapped_image):
        self.assert_data(wrapped_image.simpleMarshal())

    def test_simple_marshal_tiled(self, wrapped_image):
        image = wrapped_image._obj
        pixels = PixelsI()
        pixels.sizeX = rint(65)
        pixels.sizeY = rint(65)
        image.addPixels(pixels)
        data = wrapped_image.simpleMarshal(xtra={'tiled': True})
        self.assert_data(data)
        assert data['tiled'] is True

    def test_simple_marshal_not_tiled(self, wrapped_image):
        data = wrapped_image.simpleMarshal(xtra={'tiled': True})
        self.assert_data(data)
        assert data['tiled'] is False
