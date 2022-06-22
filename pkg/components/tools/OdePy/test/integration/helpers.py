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
   Helper method for integration tests
"""

import ode.util.script_utils as scriptUtil
from numpy import arange, uint8

def createTestImage(session, imageName="imageName"):

    plane2D = arange(256, dtype=uint8).reshape(16, 16)
    image = scriptUtil.createNewImage(session, [plane2D], imageName,
                                      "description", dataset=None)

    return image.getId().getValue()


def createImageWithPixels(client, name, sizes={}):
        """
        Create a new image with pixels
        """
        pixelsService = client.sf.getPixelsService()
        queryService = client.sf.getQueryService()

        pixelsType = queryService.findByQuery(
            "from PixelsType as p where p.value='int8'", None)
        assert pixelsType is not None

        sizeX = "x" in sizes and sizes["x"] or 1
        sizeY = "y" in sizes and sizes["y"] or 1
        sizeZ = "z" in sizes and sizes["z"] or 1
        sizeT = "t" in sizes and sizes["t"] or 1
        sizeC = "c" in sizes and sizes["c"] or 1
        channelList = range(1, sizeC+1)
        id = pixelsService.createImage(
            sizeX, sizeY, sizeZ, sizeT, channelList, pixelsType,
            name, description=None)
        return id
