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

"""Tests rendering of thumbnails."""

from engine.testlib import IWebTest
from engine.testlib import get_json

import pytest
from django.core.urlresolvers import reverse


class TestHistogram(IWebTest):
    """Tests loading of histogram json data."""

    @pytest.mark.parametrize("bins", [None, 10])
    def test_histogram_bin_count(self, bins):
        """
        Test that we get histogram json of the expected size.

        Default size is 256 bins.
        """
        size_x = 125
        size_y = 125
        img_id = self.create_test_image(size_x=size_x, size_y=size_y,
                                        session=self.sf).id.val
        the_c = 0
        args = [img_id, the_c]
        payload = {}
        if bins is not None:
            payload['bins'] = bins
        request_url = reverse('histogram_json', args=args)
        json = get_json(self.django_client, request_url, payload)
        data = json['data']
        # Sum of all pixel counts should equal number of pixels in image
        assert sum(data) == size_x * size_y
        # Number of bins should equal the 'bins' parameter (256 by default)
        if bins is None:
            assert len(data) == 256
        else:
            assert len(data) == bins
