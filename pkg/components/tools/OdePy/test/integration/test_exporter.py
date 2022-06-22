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
   Tests for the stateful Exporter service.
"""

import ode
from ode.testlib import ITest
import pytest

class TestExporter(ITest):

    def bigimage(self):
        pix = self.create_pixels(x=4000, y=4000, z=1, t=1, c=1)
        rps = self.client.sf.createRawPixelsStore()
        try:
            rps.setPixelsId(pix.id.val, True)
            self.write(pix, rps)
            return pix
        finally:
            rps.close()

    def testBasic(self):
        """
        Runs a simple export through to completion
        as a smoke test.
        """
        session = self.client.getSession()
        image = self.create_test_image(100, 100, 1, 1, 1, session)
        exporter = self.client.sf.createExporter()
        exporter.addImage(image.id.val)
        length = exporter.generateTiff()
        offset = 0
        while True:
            rv = exporter.read(offset, 1000 * 1000)
            if not rv:
                break
            rv = rv[:min(1000 * 1000, length - offset)]
            offset += len(rv)

    def test6713(self):
        """
        Tests that a big image will not be exportable.
        """
        pix = self.bigimage()
        exporter = self.client.sf.createExporter()
        exporter.addImage(pix.getImage().id.val)
        with pytest.raises(ode.ApiUsageException):
            exporter.generateTiff()
