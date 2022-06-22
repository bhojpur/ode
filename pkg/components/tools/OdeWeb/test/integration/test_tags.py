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
Tests creation, linking, editing and deletion of Tags
"""

import ode
import ode.clients
from ode.rtypes import rstring
from engine.testlib import IWebTest
from engine.testlib import post, get_json

import pytest
import json
from django.core.urlresolvers import reverse
from time import sleep

class TestTags(IWebTest):
    """
    Tests creation, linking, editing and deletion of Tags
    """

    def new_tag(self):
        """
        Returns a new Tag objects
        """
        tag = ode.model.TagAnnotationI()
        tag.textValue = rstring(self.uuid())
        tag.ns = rstring("pytest")
        return self.sf.getUpdateService().saveAndReturnObject(tag)

    def test_create_tag_and_tagset(self):
        """
        Creates a Tagset then a Tag within the Tagset
        """

        tsValue = 'testTagset'
        request_url = reverse("manage_action_containers",
                              args=["addnewcontainer"])
        data = {
            'folder_type': 'tagset',
            'name': tsValue
        }
        response = post(self.django_client, request_url, data)
        tagsetId = json.loads(response.content).get("id")

        # check creation
        tagset = self.query.get('TagAnnotationI', tagsetId)
        assert tagset is not None
        assert tagset.ns.val == ode.constants.metadata.NSINSIGHTTAGSET
        assert tagset.textValue.val == tsValue

        # Add tag to the tagset
        request_url = reverse("manage_action_containers",
                              args=["addnewcontainer", "tagset", tagsetId])
        data = {
            'folder_type': 'tag',
            'name': 'tagInTagset'
        }
        response2 = post(self.django_client, request_url, data)
        tagId = json.loads(response2.content).get("id")

        # Check that tag is listed under tagset...
        request_url = reverse("api_tags_and_tagged")
        data = {'id': tagsetId}
        data = get_json(self.django_client, request_url, data)
        assert len(data['tags']) == 1
        assert data['tags'][0]['id'] == tagId

    @pytest.mark.parametrize("dtype", ["tagset", "tag"])
    def test_edit_tag_and_tagset(self, dtype):
        """
        Creates Tag/Tagset and tests editing of name and description
        """

        request_url = reverse("manage_action_containers",
                              args=["addnewcontainer"])
        data = {
            'folder_type': dtype,
            'name': 'beforeEdit'
        }
        response = post(self.django_client, request_url, data)
        tagId = json.loads(response.content).get("id")

        # Edit name
        request_url = reverse("manage_action_containers",
                              args=["savename", dtype, tagId])
        data = {
            'name': 'afterEdit'
        }
        response = post(self.django_client, request_url, data)

        # Edit description
        request_url = reverse("manage_action_containers",
                              args=["savedescription", dtype, tagId])
        data = {
            'description': 'New description after editing'
        }
        response = post(self.django_client, request_url, data)

        # check edited name and description
        tagset = self.query.get('TagAnnotationI', tagId)
        assert tagset is not None
        if dtype == "tagset":
            assert tagset.ns.val == ode.constants.metadata.NSINSIGHTTAGSET
        assert tagset.textValue.val == 'afterEdit'
        assert tagset.description.val == 'New description after editing'

    def test_add_edit_and_remove_tag(self):

        # Add tag
        img = self.make_image()
        tag = self.new_tag()
        request_url = reverse('annotate_tags')
        data = {
            'image': img.id.val,
            'filter_mode': 'any',
            'filter_owner_mode': 'all',
            'index': 0,
            'newtags-0-description': '',
            'newtags-0-tag': 'foobar',
            'newtags-0-tagset': '',
            'newtags-INITIAL_FORMS': 0,
            'newtags-MAX_NUM_FORMS': 1000,
            'newtags-TOTAL_FORMS': 1,
            'tags': tag.id.val
        }
        rsp = post(self.django_client, request_url, data)
        rspJson = json.loads(rsp.content)
        assert len(rspJson['new']) == 1
        newTagId = rspJson['new'][0]
        assert rspJson['added'] == [tag.id.val]
        # Check that image is tagged with both tags
        request_url = reverse("api_annotations")
        data = {'image': img.id.val, 'type': 'tag'}
        data = get_json(self.django_client, request_url, data)
        assert len(data['annotations']) == 2

        # Remove tag
        request_url = reverse("manage_action_containers",
                              args=["remove", "tag", tag.id.val])
        data = {
            'index': 0,
            'parent': "image-%i" % img.id.val
        }
        post(self.django_client, request_url, data)
        # Check that tag is removed
        request_url = reverse("api_annotations")
        data = {'image': img.id.val, 'type': 'tag'}
        data = get_json(self.django_client, request_url, data)
        assert len(data['annotations']) == 1

        # Delete other tag
        request_url = reverse("manage_action_containers",
                              args=["delete", "tag", newTagId])
        post(self.django_client, request_url, {})
        # Check that tag is deleted from image
        request_url = reverse("api_annotations")
        data = {'image': img.id.val, 'type': 'tag'}
        completed = False
        # Async delete - Keep checking until done
        for t in range(20):
            rsp = get_json(self.django_client, request_url, data)
            if len(rsp['annotations']) == 0:
                completed = True
                break
            sleep(0.1)
        assert completed

    def test_add_remove_tags(self):
        # Test performance with lots of tags.
        img_count = 200
        tag_count = 10
        iids = [self.make_image().id.val for i in range(img_count)]
        tagIds = [str(self.new_tag().id.val) for i in range(tag_count)]
        tagIds = ",".join(tagIds)
        request_url = reverse('annotate_tags')
        data = {
            'image': iids,
            'filter_mode': 'any',
            'filter_owner_mode': 'all',
            'index': 0,
            'newtags-INITIAL_FORMS': 0,
            'newtags-MAX_NUM_FORMS': 1000,
            'newtags-TOTAL_FORMS': 0,
            'tags': tagIds
        }
        rsp = post(self.django_client, request_url, data)
        rspJson = json.loads(rsp.content)
        assert len(rspJson['added']) == tag_count
        # Check that tags are added to all images
        anns_url = reverse("api_annotations")
        query_string = '&'.join(['image=%s' % i for i in iids])
        query_string += '&type=tag'
        query_string += '&page=0'  # disable pagination
        rsp = self.django_client.get('%s?%s' % (anns_url, query_string))
        rspJson = json.loads(rsp.content)
        assert len(rspJson['annotations']) == img_count * tag_count

        # Remove tags
        data = {
            'image': iids,
            'filter_mode': 'any',
            'filter_owner_mode': 'all',
            'index': 0,
            'newtags-INITIAL_FORMS': 0,
            'newtags-MAX_NUM_FORMS': 1000,
            'newtags-TOTAL_FORMS': 0,
            'tags': ''
        }
        post(self.django_client, request_url, data)
        # Check tags removed
        rsp = self.django_client.get('%s?%s' % (anns_url, query_string))
        rspJson = json.loads(rsp.content)
        assert len(rspJson['annotations']) == 0
