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
   gateway tests - Testing the gateway image wrapper

   pytest fixtures used as defined in conftest.py:
   - gatewaywrapper
"""

import pytest
from ode.testlib import ITest

from ode.model import ImageI, ChannelI, LogicalChannelI, LengthI
from ode.rtypes import rstring, rtime
from datetime import datetime

@pytest.fixture(scope='module')
def itest(request):
    """
    Returns a new L{library.ITest} instance. With attached
    finalizer so that pytest will clean it up.
    """
    class ImageWrapperITest(ITest):
        """
        This class emulates py.test scoping semantics when the xunit style
        is in use.
        """
        pass
    ImageWrapperITest.setup_class()

    def finalizer():
        ImageWrapperITest.teardown_class()
    request.addfinalizer(finalizer)
    return ImageWrapperITest()


@pytest.fixture()
def image(request, gatewaywrapper):
    """Creates an Image."""
    gatewaywrapper.loginAsAuthor()
    gw = gatewaywrapper.gateway
    update_service = gw.getUpdateService()
    image = ImageI()
    image.name = rstring('an image')
    # 2015-04-21 01:15:00
    image.acquisitionDate = rtime(1429578900000)
    image_id, = update_service.saveAndReturnIds([image])
    return gw.getObject('Image', image_id)

@pytest.fixture()
def image_no_acquisition_date(request, gatewaywrapper):
    """Creates an Image."""
    gatewaywrapper.loginAsAuthor()
    gw = gatewaywrapper.gateway
    update_service = gw.getUpdateService()
    image = ImageI()
    image.name = rstring('an image')
    image.acquisitionDate = rtime(0)
    image_id, = update_service.saveAndReturnIds([image])
    return gw.getObject('Image', image_id)

@pytest.fixture()
def image_channel_factory(itest, gatewaywrapper):

    def make_image_channels(channels):
        gatewaywrapper.loginAsAuthor()
        gw = gatewaywrapper.gateway
        update_service = gw.getUpdateService()
        pixels = itest.create_pixels(client=gw.c)
        for channel in channels:
            pixels.addChannel(channel)
        pixels = update_service.saveAndReturnObject(pixels)
        return gw.getObject('Image', pixels.image.id)
    return make_image_channels

@pytest.fixture()
def labeled_channel(image_channel_factory):
    channels = list()
    for index in range(2):
        channel = ChannelI()
        lchannel = LogicalChannelI()
        lchannel.name = rstring('a channel %d' % index)
        channel.logicalChannel = lchannel
        channels.append(channel)
    return image_channel_factory(channels)

@pytest.fixture()
def emissionWave_channel(image_channel_factory):
    channels = list()
    emission_waves = (LengthI(123.0, 'NANOMETER'), LengthI(456.0, 'NANOMETER'))
    for emission_wave in emission_waves:
        channel = ChannelI()
        lchannel = LogicalChannelI()
        lchannel.emissionWave = emission_wave
        channel.logicalChannel = lchannel
        channels.append(channel)
    return image_channel_factory(channels)

@pytest.fixture()
def unlabeled_channel(image_channel_factory):
    channels = list()
    for index in range(2):
        channel = ChannelI()
        lchannel = LogicalChannelI()
        channel.logicalChannel = lchannel
        channels.append(channel)
    return image_channel_factory(channels)

class TestImageWrapper(object):

    def testGetDate(self, gatewaywrapper, image):
        date = image.getDate()
        assert date == datetime.fromtimestamp(1429578900)

    def testGetDateNoAcquisitionDate(
            self, gatewaywrapper, image_no_acquisition_date):
        date = image_no_acquisition_date.getDate()
        creation_event_date = image_no_acquisition_date.creationEventDate()
        assert date == creation_event_date

    def testSimpleMarshal(self, gatewaywrapper, image):
        marshalled = image.simpleMarshal()
        assert marshalled == {
            'description': '',
            'author': 'Author ',
            'date': 1429578900.0,
            'type': 'Image',
            'id': image.getId(),
            'name': 'an image'
        }

    def testChannelLabel(self, labeled_channel):
        labels_a = labeled_channel.getChannelLabels()
        labels_b = [v.getLabel() for v in labeled_channel.getChannels()]
        assert labels_a == labels_b == ['a channel 0', 'a channel 1']

    def testChannelEmissionWaveLabel(self, emissionWave_channel):
        labels_a = emissionWave_channel.getChannelLabels()
        labels_b = [v.getLabel() for v in emissionWave_channel.getChannels()]
        assert labels_a == labels_b == ['123', '456']

    def testChannelNoLabel(self, unlabeled_channel):
        labels_a = unlabeled_channel.getChannelLabels()
        labels_b = [v.getLabel() for v in unlabeled_channel.getChannels()]
        assert labels_a == labels_b == ['0', '1']
