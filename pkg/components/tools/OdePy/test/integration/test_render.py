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
   Integration test for rendering engine, particularly
   rendering a 'region' of big images.
"""

import ode
import logging
from ode.testlib import ITest
import io

try:
    from PIL import Image  #
except:  # pragma: nocover
    try:
        import Image  #
    except:
        logging.error('No Pillow installed')

from numpy import asarray, array_equal

class TestRendering(ITest):

    def test_render_region(self):
        """
        Test attempts to compare a full image plane, cropped to a region, with
        a region retrieved from rendering engine.

        Uses PIL to convert compressed strings into 2D numpy arrays for
        cropping and comparison.
        """

        session = self.root.sf

        size_x = 4
        size_y = 3
        size_z = 1
        size_c = 1
        size_t = 1
        image = self.create_test_image(size_x, size_y, size_z, size_c, size_t)
        pixels_id = image.getPrimaryPixels().getId().getValue()

        rendering_engine = session.createRenderingEngine()
        rendering_engine.lookupPixels(pixels_id)
        if not rendering_engine.lookupRenderingDef(pixels_id):
            rendering_engine.resetDefaultSettings(save=True)
        rendering_engine.lookupRenderingDef(pixels_id)
        rendering_engine.load()

        # turn all channels on
        for i in range(size_c):
            rendering_engine.setActive(i, True)

        region_def = ode.romio.RegionDef()
        x = 0
        y = 0
        width = 2
        height = 2
        x2 = x + width
        y2 = y + height

        region_def.x = x
        region_def.y = y
        region_def.width = width
        region_def.height = height

        plane_def = ode.romio.PlaneDef()
        plane_def.z = long(0)
        plane_def.t = long(0)

        # First, get the full rendered plane...
        img = rendering_engine.renderCompressed(plane_def)  # compressed String
        full_image = Image.open(io.BytesIO(img))  # convert to numpy arr
        # 3D array, since each pixel is [r,g,b]
        img_array = asarray(full_image)

        # get the cropped image
        cropped = img_array[y:y2, x:x2, :]      # ... so we can crop to region

        # now get the region
        plane_def.region = region_def
        img = rendering_engine.renderCompressed(plane_def)
        region_image = Image.open(io.BytesIO(img))
        region_array = asarray(region_image)

        # compare the values of the arrays
        assert array_equal(cropped, region_array)
