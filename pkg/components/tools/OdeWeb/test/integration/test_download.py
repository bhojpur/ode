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
Test download of data.
"""

from ode.model import PlateI, WellI, WellSampleI
from ode.rtypes import rstring

import pytest
from django.core.urlresolvers import reverse

from engine.testlib import IWebTest, get

class TestDownload(IWebTest):
    """
    Tests to check download is disabled where specified.
    """

    @pytest.fixture
    def image_well_plate(self):
        """
        Returns a new Bhojpur ODE Project, linked Dataset and linked Image populated
        by an L{test.integration.library.ITest} instance with required fields
        set.
        """
        plate = PlateI()
        plate.name = rstring(self.uuid())
        plate = self.update.saveAndReturnObject(plate)

        well = WellI()
        well.plate = plate
        well = self.update.saveAndReturnObject(well)

        image = self.new_image(name=self.uuid())

        ws = WellSampleI()
        ws.image = image
        ws.well = well
        well.addWellSample(ws)
        ws = self.update.saveAndReturnObject(ws)
        return plate, well, ws.image

    def test_spw_download(self, image_well_plate):
        """
        Download of an Image that is part of a plate should be disabled,
        and return a 404 response.
        """

        plate, well, image = image_well_plate
        # download archived files
        request_url = reverse('webgateway.views.archived_files')
        data = {
            "image": image.id.val
        }
        get(self.django_client, request_url, data, status_code=404)

    def test_orphaned_image_direct_download(self):
        """
        Download of archived files for a non-SPW orphaned Image.
        """

        images = self.import_fake_file()
        image = images[0]
        # download archived files
        request_url = reverse('webgateway.views.archived_files',
                              args=[image.id.val])
        get(self.django_client, request_url)

    def test_orphaned_image_download(self):
        """
        Download of archived files for a non-SPW orphaned Image.
        """

        images = self.import_fake_file()
        image = images[0]

        # download archived files
        request_url = reverse('webgateway.views.archived_files')
        data = {
            "image": image.id.val
        }
        get(self.django_client, request_url, data)

    def test_image_in_dataset_download(self):
        """
        Download of archived files for a non-SPW Image in Dataset.
        """

        images = self.import_fake_file()
        image = images[0]
        ds = self.make_dataset()
        self.link(ds, image)

        # download archived files
        request_url = reverse('webgateway.views.archived_files')
        data = {
            "image": image.id.val
        }
        get(self.django_client, request_url, data)

    def test_image_in_dataset_in_project_download(self):
        """
        Download of archived files for a non-SPW Image in Dataset in Project.
        """

        images = self.import_fake_file()
        image = images[0]
        ds = self.make_dataset()
        pr = self.make_project()

        self.link(pr, ds)
        self.link(ds, image)

        # download archived files
        request_url = reverse('webgateway.views.archived_files')
        data = {
            "image": image.id.val
        }
        get(self.django_client, request_url, data)

    def test_well_download(self, image_well_plate):
        """
        Download of archived files for a SPW Well.
        """

        plate, well, image = image_well_plate
        # download archived files
        request_url = reverse('webgateway.views.archived_files')
        data = {
            "well": well.id.val
        }
        get(self.django_client, request_url, data, status_code=404)

    def test_attachment_download(self):
        """
        Download of attachment.
        """

        images = self.import_fake_file()
        image = images[0]
        fa = self.make_file_annotation()
        self.link(image, fa)

        # download archived files
        request_url = reverse('download_annotation',
                              args=[fa.id.val])
        get(self.django_client, request_url)
