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

import base64
import json
from engine.testlib import IWebTest
from engine.testlib import get

from cStringIO import StringIO
import pytest
from django.core.urlresolvers import reverse
try:
    from PIL import Image
except:
    import Image

class TestThumbnails(IWebTest):
    """Tests loading of thumbnails."""

    @pytest.mark.parametrize("size", [None, 100])
    def test_default_thumb_size(self, size):
        """
        Test that the default size of thumbnails is correct.

        Default size is 96.
        """
        # Create a square image
        iId = self.create_test_image(size_x=125, size_y=125,
                                     session=self.sf).getId().getValue()
        args = [iId]
        if size is not None:
            args.append(size)
        request_url = reverse('webgateway.views.render_thumbnail', args=args)
        rsp = get(self.django_client, request_url)

        thumb = Image.open(StringIO(rsp.content))
        # Should be 96 on both sides
        if size is None:
            assert thumb.size == (96, 96)
        else:
            assert thumb.size == (size, size)

    @pytest.mark.parametrize("size", [None, 100])
    def test_base64_thumb(self, size):
        """
        Test base64 encoded retrival of single thumbnail
        """
        # Create a square image
        iid = self.create_test_image(size_x=256, size_y=256,
                                     session=self.sf).id.val
        args = [iid]
        if size is not None:
            args.append(size)
        request_url = reverse('webgateway.views.render_thumbnail', args=args)
        rsp = get(self.django_client, request_url)
        thumb = json.dumps(
            "data:image/jpeg;base64,%s" % base64.b64encode(rsp.content))

        request_url = reverse('webgateway.views.get_thumbnail_json',
                              args=args)
        b64rsp = get(self.django_client, request_url).content
        assert thumb == b64rsp

    def test_base64_thumb_set(self):
        """
        Test base64 encoded retrival of thumbnails in a batch
        """
        # Create a square image
        images = []
        for i in range(2, 5):
            iid = self.create_test_image(size_x=64*i, size_y=64*i,
                                         session=self.sf).id.val
            images.append(iid)

        expected_thumbs = {}
        for i in images:
            request_url = reverse('webgateway.views.render_thumbnail',
                                  args=[i])
            rsp = get(self.django_client, request_url)

            expected_thumbs[i] = \
                "data:image/jpeg;base64,%s" % base64.b64encode(rsp.content)

        iids = {'id': images}
        request_url = reverse('webgateway.views.get_thumbnails_json')
        b64rsp = get(self.django_client, request_url, iids).content

        assert cmp(json.loads(b64rsp),
                   json.loads(json.dumps(expected_thumbs))) == 0
        assert json.dumps(expected_thumbs) == b64rsp
