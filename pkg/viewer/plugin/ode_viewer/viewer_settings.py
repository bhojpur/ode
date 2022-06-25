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

"""Settings for the Bhojpur ODE.viewer app."""

import sys
from engine.settings import process_custom_settings, report_settings

# load settings
VIEWER_SETTINGS_MAPPING = {

    "ode.web.viewer.max_projection_bytes":
        ["MAX_PROJECTION_BYTES",
         -1,
         int,
         ("Maximum bytes of raw pixel data allowed for Z-projection. "
          "Above this threshold, Z-projection is disabled. "
          "If unset, the server setting of "
          "ode.pixeldata.max_projection_bytes will be used or "
          "the lower value if both are set.")],

    "ode.web.viewer.roi_page_size":
        ["ROI_PAGE_SIZE",
         500,
         int,
         "Page size for ROI pagination."],
}

process_custom_settings(sys.modules[__name__], 'VIEWER_SETTINGS_MAPPING')
report_settings(sys.modules[__name__])