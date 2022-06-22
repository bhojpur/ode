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
Integration test focused on the ode.api.IMetadata interface.
"""

from ode.testlib import ITest
import pytest
import ode

NAMES = ("TagAnnotation",
         "TagAnnotationI",
         "ode.model.TagAnnotation",
         "ode.model.TagAnnotationI",
         "ode.model.annotations.TagAnnotation")

class TestIMetadata(ITest):

    def setup_method(self, method):
        self.md = self.client.sf.getMetadataService()

    def testLoadAnnotations3671(self):
        """
        Support for less-strict class names
        """
        with pytest.raises(ode.ApiUsageException):
            self.md.loadAnnotations('Project', [0], ['X'], None, None)
        for name in NAMES:
            self.md.loadAnnotations('Project', [0], [name], None, None)
        self.md.loadAnnotations('Project', [0], NAMES, None, None)

    def testLoadAnnotationsUsedNotOwned3671(self):
        """
        Support for less-strict class names
        """
        with pytest.raises(ode.ApiUsageException):
            self.md.loadAnnotationsUsedNotOwned('X', 0, None)
        for name in NAMES:
            self.md.loadAnnotationsUsedNotOwned(name, 0, None)

    def testCountAnnotationsUsedNotOwned3671(self):
        """
        Support for less-strict class names
        """
        with pytest.raises(ode.ApiUsageException):
            self.md.countAnnotationsUsedNotOwned('X', 0, None)
        for name in NAMES:
            self.md.countAnnotationsUsedNotOwned(name, 0, None)

    def testCountSpecifiedAnnotations3671(self):
        """
        Support for less-strict class names
        """
        with pytest.raises(ode.ApiUsageException):
            self.md.countSpecifiedAnnotations('X', [], [], None)
        for name in NAMES:
            self.md.countSpecifiedAnnotations(name, [], [], None)

    def testLoadSpecifiedAnnotations3671(self):
        """
        Support for less-strict class names
        """
        with pytest.raises(ode.ApiUsageException):
            self.md.loadSpecifiedAnnotations('X', [], [], None)
        for name in NAMES:
            self.md.loadSpecifiedAnnotations(name, [], [], None)
