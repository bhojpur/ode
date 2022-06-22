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
Uses the ode.util.populate_roi functionality to parse all the measurement
files attached to a plate, and generate server-side rois.

params:
    Plate_ID: id of the plate which should be parsed.
"""

import ode.scripts as scripts
from ode.util.populate_roi import PlateAnalysisCtxFactory

client = scripts.client(
    'Populate_ROI.py',
    scripts.Long(
        "Plate_ID", optional=False,
        description="ID of a valid plate with attached results files"),
    version="1.0.0",
    contact="product@bhojpur-consulting.com",
    description="""Generates regions of interest from the measurement files \
associated with a plate

This script is executed by the server on initial import, and should typically\
not need to be run by users.""")

factory = PlateAnalysisCtxFactory(client.getSession())
analysis_ctx = factory.get_analysis_ctx(client.getInput("Plate_ID").val)
n_measurements = analysis_ctx.get_measurement_count()

for i in range(n_measurements):
    measurement_ctx = analysis_ctx.get_measurement_ctx(i)
    measurement_ctx.parse_and_populate()
