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

import unittest
import os

from ode.util.populate_roi import AbstractPlateAnalysisCtx
from ode.util.populate_roi import MIASPlateAnalysisCtx
from ode.util.populate_roi import FlexPlateAnalysisCtx
from ode.util.populate_roi import InCellPlateAnalysisCtx

from ode.rtypes import rstring, rint
from ode.model import OriginalFileI, ImageI, WellI, WellSampleI

class TestingServiceFactory(object):
    """
    Testing service factory implementation.
    """
    def getUpdateService(self):
        return None

    def getQueryService(self):
        return None

class FromFileOriginalFileProvider(object):
    """
    Provides a testing original file provider which provides file data
    directly from disk.
    """
    def __init__(self, service_factory):
        pass

    def get_original_file_data(self, original_file):
        """Returns a file handle to the path of the original file."""
        return open(original_file.path.val)


class MIASParseRoiTest(unittest.TestCase):

    LOG_FILE = "NEOlog2008-09-18-14h37m07s.txt"

    RESULT_FILE = "Well0001_mode1_z000_t000_detail_2008-09-18-10h48m54s.txt"

    ROOT = "/Users/bhojpur/testimages/siRNA_PRIM1_03102008/"\
        "001-365700055641/results/"

    def setUp(self):
        AbstractPlateAnalysisCtx.DEFAULT_ORIGINAL_FILE_PROVIDER = \
            FromFileOriginalFileProvider
        original_files = list()
        # Create our container images and an original file image map
        images = list()
        n_images = 0
        for row in range(16):
            for column in range(24):
                well = WellI(n_images, True)
                well.column = rint(column)
                well.row = rint(row)
                well_sample = WellSampleI(n_images, True)
                well_sample.well = well
                image = ImageI(n_images, True)
                image.addWellSample(well_sample)
                images.append(image)
        original_file_image_map = dict()
        # Our required original file format
        format = rstring('Companion/MIAS')
        # Create original file representing the log file
        o = OriginalFileI(1, True)
        o.name = rstring(self.LOG_FILE)
        o.path = rstring(os.path.join(self.ROOT, self.LOG_FILE))
        o.mimetype = format
        original_files.append(o)  # [1L] = o
        original_file_image_map[1] = images[0]
        # Create original file representing the result file
        o = OriginalFileI(2, True)
        o.name = rstring(self.RESULT_FILE)
        o.path = rstring(os.path.join(self.ROOT, self.RESULT_FILE))
        o.mimetype = format
        original_files.append(o)  # [2L] = o
        original_file_image_map[2] = images[0]
        sf = TestingServiceFactory()
        self.analysis_ctx = MIASPlateAnalysisCtx(
            images, original_files, original_file_image_map, 1, sf)

    def test_get_measurement_ctx(self):
        ctx = self.analysis_ctx.get_measurement_ctx(0)
        self.assertNotEqual(None, ctx)

    def test_get_columns(self):
        ctx = self.analysis_ctx.get_measurement_ctx(0)
        columns = ctx.parse()
        self.assertNotEqual(None, columns)
        self.assertEqual(9, len(columns))
        self.assertEqual('Image', columns[0].name)
        self.assertEqual('ROI', columns[1].name)
        self.assertEqual('Label', columns[2].name)
        self.assertEqual('Row', columns[3].name)
        self.assertEqual('Col', columns[4].name)
        self.assertEqual('Nucleus Area', columns[5].name)
        self.assertEqual('Cell Diam.', columns[6].name)
        self.assertEqual('Cell Type', columns[7].name)
        self.assertEqual('Mean Nucleus Intens.', columns[8].name)
        for column in columns:
            if column.name == "ROI":
                continue
            self.assertEqual(173, len(column.values))

class FlexParseRoiTest(unittest.TestCase):

    ROOT = "/Users/bhojpur/testimages/"

    RESULT_FILE = "An_02_Me01_12132846(2009-06-17_11-56-17).res"

    def setUp(self):
        AbstractPlateAnalysisCtx.DEFAULT_ORIGINAL_FILE_PROVIDER = \
            FromFileOriginalFileProvider
        original_files = list()
        # Create our container images and an original file image map
        images = list()
        n_images = 0
        for row in range(16):
            for column in range(24):
                well = WellI(n_images, True)
                well.column = rint(column)
                well.row = rint(row)
                well_sample = WellSampleI(n_images, True)
                well_sample.well = well
                image = ImageI(n_images, True)
                image.addWellSample(well_sample)
                images.append(image)
        original_file_image_map = dict()
        # Our required original file format
        format = rstring('Companion/Flex')
        # Create original file representing the result file
        o = OriginalFileI(1, True)
        o.name = rstring(self.RESULT_FILE)
        o.path = rstring(os.path.join(self.ROOT, self.RESULT_FILE))
        o.mimetype = format
        original_files.append(o)  # [1L] = o
        original_file_image_map[1] = images[0]
        sf = TestingServiceFactory()
        self.analysis_ctx = FlexPlateAnalysisCtx(
            images, original_files, original_file_image_map, 1, sf)

    def test_get_measurement_ctx(self):
        ctx = self.analysis_ctx.get_measurement_ctx(0)
        self.assertNotEqual(None, ctx)

    def test_get_columns(self):
        ctx = self.analysis_ctx.get_measurement_ctx(0)
        columns = ctx.parse()
        self.assertNotEqual(None, columns)
        self.assertEqual(50, len(columns))
        for column in columns:
            self.assertEqual(384, len(column.values))

class InCellParseRoiTest(unittest.TestCase):

    ROOT = "/Users/bhojpur/testimages"

    RESULT_FILE = "Mara_488 and hoechst_P-HisH3.xml"

    def setUp(self):
        AbstractPlateAnalysisCtx.DEFAULT_ORIGINAL_FILE_PROVIDER = \
            FromFileOriginalFileProvider
        original_files = list()
        # Create our container images and an original file image map
        images = list()
        n_images = 0
        for row in range(16):
            for column in range(24):
                well = WellI(n_images, True)
                well.column = rint(column)
                well.row = rint(row)
                well_sample = WellSampleI(n_images, True)
                well_sample.well = well
                image = ImageI(n_images, True)
                image.addWellSample(well_sample)
                images.append(image)
        original_file_image_map = dict()
        # Our required original file format
        format = rstring('Companion/InCell')
        # Create original file representing the result file
        o = OriginalFileI(1, True)
        o.name = rstring(self.RESULT_FILE)
        o.path = rstring(os.path.join(self.ROOT, self.RESULT_FILE))
        o.mimetype = format
        original_files.append(o)  # [1L] = o
        original_file_image_map[1] = image
        sf = TestingServiceFactory()
        self.analysis_ctx = InCellPlateAnalysisCtx(
            images, original_files, original_file_image_map, 1, sf)

    def test_get_measurement_ctx(self):
        ctx = self.analysis_ctx.get_measurement_ctx(0)
        self.assertNotEqual(None, ctx)

    def test_get_columns(self):
        ctx = self.analysis_ctx.get_measurement_ctx(0)
        columns = ctx.parse()
        self.assertNotEqual(None, columns)
        for column in columns:
            print('Column: %s' % column.name)
        self.assertEqual(33, len(columns))
        for column in columns:
            self.assertEqual(114149, len(column.values))

if __name__ == '__main__':
    unittest.main()
