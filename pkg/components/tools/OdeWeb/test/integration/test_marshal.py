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
Test json methods of webgateway
"""

from django.core.urlresolvers import reverse

from engine.testlib import IWebTest, get_json

class TestImgDetail(IWebTest):

    """
    Tests json for webgateway/imgData/
    """

    def test_image_detail(self):
        """
        Download of archived files for a non-SPW Image.
        """
        user_name = "%s %s" % (self.user.firstName.val, self.user.lastName.val)

        # Import image with metadata and get ImageID
        images = self.import_fake_file(
            client=self.client, pixelType="int16", sizeX=20, sizeY=20, sizeZ=5,
            sizeT=6)
        iid = images[0].id.val
        json_url = reverse('webgateway.views.imageData_json', args=[iid])
        data = {}
        img_data = get_json(self.django_client, json_url, data,
                            status_code=200)

        # Not a big image - tiles should be False with no other tiles metadata
        assert img_data['tiles'] is False
        assert 'levels' not in img_data
        assert 'zoomLevelScaling' not in img_data
        assert 'tile_size' not in img_data

        # Channels metadata
        assert len(img_data['channels']) == 1
        assert img_data['channels'][0] == {
            'color': "808080",
            'active': True,
            'window': {
                'max': 32767.0,
                'end': 12.0,
                'start': -32768.0,
                'min': -32768.0
            },
            'family': 'linear',
            'coefficient': 1.0,
            'reverseIntensity': False,
            'inverted': False,
            'emissionWave': None,
            'label': "0"  # to be reviewed when wavelength is supported
        }
        assert img_data['pixel_range'] == [-32768, 32767]
        assert img_data['rdefs'] == {
            'defaultT': 0,
            'model': "greyscale",
            'invertAxis': False,
            'projection': "normal",
            'defaultZ': 2
        }

        # Core image metadata
        assert img_data['size'] == {
            'width': 20,
            'c': 1,
            'z': 5,
            't': 6,
            'height': 20
        }
        assert img_data['meta']['pixelsType'] == "int16"
        assert img_data['meta']['projectName'] == "Multiple"
        assert img_data['meta']['imageId'] == iid
        assert img_data['meta']['imageAuthor'] == user_name
        assert img_data['meta']['datasetId'] is None
        assert img_data['meta']['projectDescription'] == ""
        assert img_data['meta']['datasetName'] == "Multiple"
        assert img_data['meta']['wellSampleId'] == ""
        assert img_data['meta']['projectId'] is None
        assert img_data['meta']['imageDescription'] == ""
        assert img_data['meta']['wellId'] == ""
        assert img_data['meta']['imageName'].endswith(".fake")
        assert img_data['meta']['datasetDescription'] == ""
        # Don't know exact timestamp of import
        assert 'imageTimestamp' in img_data['meta']

        # Permissions - User is owner. All perms are True
        assert img_data['perms'] == {
            'canAnnotate': True,
            'canEdit': True,
            'canDelete': True,
            'canLink': True
        }

        # Sizes for split channel image
        assert img_data['split_channel'] == {
            'c': {
                'width': 46,
                'gridy': 1,
                'border': 2,
                'gridx': 2,
                'height': 24
            },
            'g': {
                'width': 24,
                'gridy': 1,
                'border': 2,
                'gridx': 1,
                'height': 24
            }
        }
