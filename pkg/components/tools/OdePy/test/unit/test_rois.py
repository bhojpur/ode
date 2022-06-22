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
Simple tests of various ROI utilities
"""

from ode.util.ROI_utils import pointsStringToXYlist, xyListToBbox
from ode.util.roi_handling_utils import points_string_to_xy_list

class TestRoiUtils(object):

    def test_old_format(self):
        xy_list = pointsStringToXYlist((
            "points[1,2, 3,4, 5,6] "
        ))
        assert xy_list == [(1, 2), (3, 4), (5, 6)]

    def test_new_format(self):
        xy_list = pointsStringToXYlist((
            "1,2 3,4 5,6"
        ))
        assert xy_list == [(1, 2), (3, 4), (5, 6)]

    def test_bbox(self):
        xy_list = points_string_to_xy_list((
            "points[1,2, 3,4, 5,6] "
        ))
        bbox = xyListToBbox(xy_list)
        assert bbox == (1, 2, 4, 4)

    def test_old_format_new_method(self):
        xy_list = points_string_to_xy_list((
            "points[1,2, 3,4, 5,6] "
        ))
        assert xy_list == [(1, 2), (3, 4), (5, 6)]

    def test_new_format_new_method(self):
        xy_list = points_string_to_xy_list((
            "1,2 3,4 5,6"
        ))
        assert xy_list == [(1, 2), (3, 4), (5, 6)]
