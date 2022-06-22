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
Tests creation, linking, editing & deletion of containers
"""

import ode
import ode.clients
from ode.rtypes import rtime
from engine.api import api_settings
from engine.testlib import IWebTest
from engine.testlib import get_json, post, post_json, delete_json

import json

from django.core.urlresolvers import reverse

class TestContainers(IWebTest):
    """
    Tests creation, linking, editing & deletion of containers
    """

    def blank_image(self):
        """
        Returns a new foundational Image with Channel objects attached for
        view method testing.
        """
        print(dir(self))

        pixels = self.create_pixels(client=self.client)
        for the_c in range(pixels.getSizeC().val):
            channel = ode.model.ChannelI()
            channel.logicalChannel = ode.model.LogicalChannelI()
            pixels.addChannel(channel)
        image = pixels.getImage()
        return self.sf.getUpdateService().saveAndReturnObject(image)

    def test_add_and_rename_container(self):

        # Add project
        request_url = reverse("manage_action_containers",
                              args=["addnewcontainer"])
        data = {
            'folder_type': 'project',
            'name': 'foobar'
        }
        response = post(self.django_client, request_url, data)
        pid = json.loads(response.content).get("id")

        # Add dataset to the project
        request_url = reverse("manage_action_containers",
                              args=["addnewcontainer", "project", pid])
        data = {
            'folder_type': 'dataset',
            'name': 'foobar'
        }
        post(self.django_client, request_url, data)

        # Rename project
        request_url = reverse("manage_action_containers",
                              args=["savename", "project", pid])
        data = {
            'name': 'anotherfoobar'
        }
        post(self.django_client, request_url, data)

        # Change project description
        request_url = reverse("manage_action_containers",
                              args=["savedescription", "project", pid])
        data = {
            'description': 'anotherfoobar'
        }
        post(self.django_client, request_url, data)

    def test_add_owned_container(self):
        """Test root user creating a Dataset owned by another user."""
        request_url = reverse("manage_action_containers",
                              args=["addnewcontainer"])

        # Create user in 2 groups
        group1 = self.new_group()
        group2 = self.new_group()
        user = self.new_user(group=group1)
        self.add_groups(user, [group2])

        # Container will get added to active_group
        session = self.django_root_client.session
        session['active_group'] = group2.id.val
        session.save()
        data = {
            'folder_type': 'dataset',
            'name': 'ownedby',
            'owner': str(user.id.val)
        }
        response = post(self.django_root_client, request_url, data)
        did = json.loads(response.content).get("id")

        # Check that Dataset was created & has correct group and owner
        request_url = reverse("manage_action_containers",
                              args=["addnewcontainer"])
        version = api_settings.API_VERSIONS[-1]
        request_url = reverse('api_dataset', kwargs={'api_version': version,
                                                     'object_id': did})
        rsp_json = get_json(self.django_root_client, request_url, {})
        dataset = rsp_json['data']
        assert dataset['@id'] == did
        assert dataset['ode:details']['owner']['@id'] == user.id.val
        assert dataset['ode:details']['group']['@id'] == group2.id.val

    def test_paste_move_remove_deletamany_image(self):

        # Add dataset
        request_url = reverse("manage_action_containers",
                              args=["addnewcontainer"])
        data = {
            'folder_type': 'dataset',
            'name': 'foobar'
        }
        response = post(self.django_client, request_url, data)
        did = json.loads(response.content).get("id")

        img = self.make_image()
        print(img)

        # Link image to Dataset
        request_url = reverse("api_links")
        data = {
            'dataset': {did: {'image': [img.id.val]}}
        }

        post_json(self.django_client, request_url, data)

        # Unlink image from Dataset
        request_url = reverse("api_links")
        data = {
            'dataset': {did: {'image': [img.id.val]}}
        }
        response = delete_json(self.django_client, request_url, data)
        # Response will contain remaining links from image (see test_links.py)
        assert response == {"success": True}

    def test_create_share(self):

        img = self.make_image()
        request_url = reverse("manage_action_containers",
                              args=["add", "share"])
        data = {
            'enable': 'on',
            'image': img.id.val,
            'members': self.user.id.val,
            'message': 'foobar'
        }

        post(self.django_client, request_url, data)

    def test_edit_share(self):

        # create images
        images = [self.create_test_image(session=self.sf),
                  self.create_test_image(session=self.sf)]

        sid = self.sf.getShareService().createShare(
            "foobar", rtime(None), images, [self.user], [], True)

        request_url = reverse("manage_action_containers",
                              args=["save", "share", sid])

        data = {
            'enable': 'on',
            'image': [i.id.val for i in images],
            'members': self.user.id.val,
            'message': 'another foobar'
        }
        post(self.django_client, request_url, data)

        # remove image from share
        request_url = reverse("manage_action_containers",
                              args=["removefromshare", "share", sid])
        data = {
            'source': images[1].id.val,
        }
        post(self.django_client, request_url, data)
