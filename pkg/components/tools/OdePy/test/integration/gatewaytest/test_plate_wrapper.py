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
   gateway tests - Testing the gateway plate wrapper

   pytest fixtures used as defined in conftest.py:
   - gatewaywrapper
"""

import pytest

from ode.model import PlateI, WellI, WellSampleI, ImageI
from ode.rtypes import rstring, rint, rtime
from uuid import uuid4

def uuid():
    return str(uuid4())

@pytest.fixture()
def plate(request, gatewaywrapper):
    """Creates a Plate."""
    gatewaywrapper.loginAsAuthor()
    gw = gatewaywrapper.gateway
    update_service = gw.getUpdateService()
    plate = PlateI()
    plate.name = rstring(uuid())
    for well_index in range(3):
        well = WellI()
        well.row = rint(well_index**2)
        well.column = rint(well_index**3)
        for well_sample_index in range(2):
            well_sample = WellSampleI()
            image = ImageI()
            image.name = rstring('%s_%d' % (uuid(), well_sample_index))
            image.acquisitionDate = rtime(0)
            well_sample.image = image
            well.addWellSample(well_sample)
        plate.addWell(well)
    plate_id, = update_service.saveAndReturnIds([plate])
    return gw.getObject('Plate', plate_id)

class TestPlateWrapper(object):

    def testGetGridSize(self, gatewaywrapper, plate):
        assert plate.getGridSize() == {'rows': 5, 'columns': 9}
