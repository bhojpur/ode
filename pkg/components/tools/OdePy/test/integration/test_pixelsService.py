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
   Tests for the Pixels service.
"""

import ode
import ode.gateway
from ode.testlib import ITest
from helpers import createImageWithPixels

class TestPixelsService(ITest):

    def test9655(self):
        # Create an image without statsinfo objects and attempt
        # to retrieve it from the Rendering service.

        # Get the pixels
        image_id = createImageWithPixels(self.client, self.uuid())
        gateway = ode.gateway.ServerGateway(client_obj=self.client)
        image = gateway.getObject("Image", image_id)
        pixels_id = image.getPrimaryPixels().id

        # Save the pixels
        rps = self.client.sf.createRawPixelsStore()
        rps.setPixelsId(pixels_id, False)
        rps.setPlane([0], 0, 0, 0)
        rps.save()
        rps.close()

        # Now use the RE to load
        re = self.client.sf.createRenderingEngine()
        re.lookupPixels(pixels_id)
        re.resetDefaultSettings(save=True)
        re.lookupPixels(pixels_id)
        re.lookupRenderingDef(pixels_id)
        re.getPixels()
