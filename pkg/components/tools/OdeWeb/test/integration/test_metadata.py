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
Tests display of metadata in webclient
"""

import ode
import json
import pytest

from engine.testlib import IWebTest
from engine.testlib import get, _get_response

from django.core.urlresolvers import reverse
from ode.constants.namespaces import NSBULKANNOTATIONS
from ode.model.enums import UnitsLength
from ode_model_ImageI import ImageI

from ode.rtypes import rstring
from ode.rtypes import wrap

class TestCoreMetadata(IWebTest):
    """
    Tests display of core metatada
    """

    def test_pixel_size_units(self):
        # Create image
        iid = self.create_test_image(size_c=2, session=self.sf).id.val

        # show right panel for image
        request_url = reverse('load_metadata_details', args=['image', iid])
        data = {}
        rsp = get(self.django_client, request_url, data, status_code=200)
        html = rsp.content
        # Units are µm by default
        assert "Pixels Size (XYZ) (µm):" in html

        # Now save units as PIXELs and view again
        conn = ode.gateway.ServerGateway(client_obj=self.client)
        i = conn.getObject("Image", iid)
        u = ode.model.LengthI(1.2, UnitsLength.PIXEL)
        p = i.getPrimaryPixels()._obj
        p.setPhysicalSizeX(u)
        p.setPhysicalSizeY(u)
        conn.getUpdateService().saveObject(p)

        # Should now be showing pixels
        rsp = get(self.django_client, request_url, data, status_code=200)
        html = rsp.content
        assert "Pixels Size (XYZ):" in html
        assert "1.20 (pixel)" in html

    def test_none_pixel_size(self):
        """
        Tests display of core metatada still works even when image
        doesn't have pixels data
        """
        img = ImageI()
        img.setName(rstring("no_pixels"))
        img.setDescription(rstring("empty image without pixels data"))

        conn = ode.gateway.ServerGateway(client_obj=self.client)
        img = conn.getUpdateService().saveAndReturnObject(img)

        request_url = reverse('load_metadata_details',
                              args=['image', img.id.val])

        # Just check that the metadata panel is loaded
        rsp = get(self.django_client, request_url, status_code=200)
        assert "no_pixels" in rsp.content


class TestBulkAnnotations(IWebTest):
    """
    Tests retrieval of bulk annotations and related metadata
    """

    @pytest.mark.parametrize("bulkann", [True, False])
    def test_nsbulkannotations_file(self, bulkann):
        if bulkann:
            ns = NSBULKANNOTATIONS
        else:
            ns = 'other'
        # Create plate
        p = ode.model.PlateI()
        p.setName(wrap(self.uuid()))
        update = self.client.sf.getUpdateService()
        p = update.saveAndReturnObject(p)

        # Create a file annotation
        name = self.uuid()
        fa = self.make_file_annotation(name=name, namespace=ns)
        link = self.link(p, fa)

        # retrieve annotations
        request_url = reverse(
            "webgateway_annotations", args=["Plate", p.id.val])
        rsp = _get_response(
            self.django_client, request_url, {}, status_code=200)
        j = json.loads(rsp.content)

        if bulkann:
            assert len(j["data"]) == 1
            assert j["data"][0]["file"] == link.child.file.id.val
        else:
            assert len(j["data"]) == 0

    def test_nsbulkannotations_not_file(self):
        # Create plate
        p = ode.model.PlateI()
        p.setName(wrap(self.uuid()))
        update = self.client.sf.getUpdateService()
        p = update.saveAndReturnObject(p)

        # Create a non-file annotation
        tag = self.new_tag(ns=NSBULKANNOTATIONS)
        link = self.link(p, tag)
        assert link

        # retrieve annotations
        request_url = reverse(
            "webgateway_annotations", args=["Plate", p.id.val])
        rsp = _get_response(
            self.django_client, request_url, {}, status_code=200)
        j = json.loads(rsp.content)
        assert len(j["data"]) == 0
