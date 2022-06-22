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
Tests adding & removing annotations
"""

import ode
import ode.clients
from time import sleep
import json

from engine.testlib import IWebTest
from engine.testlib import get, post, get_json

from django.core.urlresolvers import reverse

class TestMapAnnotations(IWebTest):

    def annotate_dataset(self, django_client, keyValues,
                         ds_id=None, ns=None, ann_id=None):
        """
        Adds a new Map Annotation to Dataset or edits existing annotation

        @param keyValues - List of [key: value] pairs.
        """
        request_url = reverse('annotate_map')
        data = {
            'mapAnnotation': json.dumps(keyValues),
        }
        if ns is not None:
            data['ns'] = ns
        if ds_id is not None:
            data['dataset'] = ds_id
        if ann_id is not None:
            data['annId'] = ann_id
        post(django_client, request_url, data)

    def test_annotate_map(self):
        """Test we can create and retrieve map annotations, filter by ns."""

        # Create User in a Read-Annotate group
        client1, user1 = self.new_client_and_user(perms='rwrw--')
        odeName = client1.sf.getAdminService().getEventContext().userName
        django_client = self.new_django_client(odeName, odeName)

        # User1 creates Dataset
        ds = self.make_dataset("user1_Dataset", client=client1)

        # Add 2 map annotations, one with ns specfied (default is 'client' ns)
        ns = 'test.annotate.map.ns'
        map_data_ns = [['testKey', 'someValue'], ['ns', ns]]
        self.annotate_dataset(django_client, map_data_ns, ds.id.val, ns)
        client_map_data = [['expect', 'client'], ['ns', 'to be used']]
        self.annotate_dataset(django_client, client_map_data, ds.id.val)

        # check maps got added
        request_url = reverse('api_annotations')
        data = {
            "dataset": ds.id.val,
            "type": "map",
        }
        # get both map annotations
        rsp = get_json(django_client, request_url, data)
        assert len(rsp['annotations']) == 2

        # now filter by custom ns
        data['ns'] = ns
        rsp = get_json(django_client, request_url, data)
        assert len(rsp['annotations']) == 1
        # check essential values
        ann = rsp['annotations'][0]
        ann_id = ann['id']
        assert ann["values"] == map_data_ns
        assert ann["ns"] == ns
        assert ann["link"]["parent"]["id"] == ds.id.val

        # update map annotation
        new_data = [['new', 'data']]
        self.annotate_dataset(django_client, new_data, None, None, ann_id)
        rsp = get_json(django_client, request_url, data)
        assert rsp['annotations'][0]['values'] == new_data

        # delete map annotation (set data as empty list)
        self.annotate_dataset(django_client, [], None, None, ann_id)

        # only one left
        client_ns = ode.constants.metadata.NSCLIENTMAPANNOTATION
        del data['ns']
        rsp = get_json(django_client, request_url, data)
        assert len(rsp['annotations']) == 1
        ann = rsp['annotations'][0]
        assert ann["values"] == client_map_data
        assert ann["ns"] == client_ns


class TestTagging(IWebTest):
    """
    Tests adding and removing Tags with annotate_tags()
    """

    def annotate_dataset(self, django_client, dsId, tagIds):
        """
        Returns userA's Tag linked to userB's dataset
        by userA and userB
        """
        # 'newtags-0-description': '',
        # 'newtags-0-tag': 'foobar',
        # 'newtags-0-tagset': '',
        request_url = reverse('annotate_tags')
        data = {
            'dataset': dsId,
            'filter_mode': 'any',
            'filter_owner_mode': 'all',
            'index': 0,
            'newtags-INITIAL_FORMS': 0,
            'newtags-MAX_NUM_FORMS': 1000,
            'newtags-TOTAL_FORMS': 0,
            'tags': ",".join([str(i) for i in tagIds])
        }
        post(django_client, request_url, data)

    def test_annotate_tag(self):

        # Create User in a Read-Annotate group
        client1, user1 = self.new_client_and_user(perms='rwrw--')
        # conn = ode.gateway.ServerGateway(client_obj=client1)
        odeName = client1.sf.getAdminService().getEventContext().userName
        django_client1 = self.new_django_client(odeName, odeName)

        # User1 creates Tag and Dataset
        ds = self.make_dataset("user1_Dataset", client=client1)
        tag = self.make_tag("test_annotate_tag", client=client1)

        # User2...
        groupId = client1.sf.getAdminService().getEventContext().groupId
        client2, user2 = self.new_client_and_user(
            group=ode.model.ExperimenterGroupI(groupId, False))
        # ...creates Tag
        tag2 = self.make_tag("user2_tag", client=client2)

        # User1 adds 2 tags to Dataset
        self.annotate_dataset(django_client1, ds.id.val,
                              [tag.id.val, tag2.id.val])

        # check tags got added
        request_url = reverse('api_annotations')
        data = {
            "dataset": ds.id.val
        }
        rsp = get_json(django_client1, request_url, data)

        tagIds = [t['id'] for t in rsp['annotations']]
        assert tag.id.val in tagIds
        assert tag2.id.val in tagIds

        # We can remove tags by not including them
        # E.g. move from Right to Left column in the UI
        self.annotate_dataset(django_client1, ds.id.val, [tag2.id.val])

        # Since tag link deletion is async, we need to wait to be sure that
        # tag is removed.
        sleep(1)
        rsp = get_json(django_client1, request_url, data)
        tagIds = [t['id'] for t in rsp['annotations']]
        assert tag.id.val not in tagIds
        assert tag2.id.val in tagIds


class TestFileAnnotations(IWebTest):
    """
    Tests listing file annotations
    """

    def test_add_fileannotations_form(self):

        # Create User in a Read-Annotate group
        client, user = self.new_client_and_user(perms='rwrw--')
        # conn = ode.gateway.ServerGateway(client_obj=client)
        odeName = client.sf.getAdminService().getEventContext().userName
        django_client1 = self.new_django_client(odeName, odeName)

        # User creates Dataset
        ds = self.make_dataset("user1_Dataset", client=client)

        # Create File and FileAnnotation
        update = client.sf.getUpdateService()
        f = ode.model.OriginalFileI()
        f.name = ode.rtypes.rstring("")
        f.path = ode.rtypes.rstring("")
        f = update.saveAndReturnObject(f)
        fa = ode.model.FileAnnotationI()
        fa.setFile(f)
        fa = update.saveAndReturnObject(fa)

        # get form for annotating Dataset
        request_url = reverse('annotate_file')
        data = {
            "dataset": ds.id.val
        }
        rsp = get(django_client1, request_url, data)
        html = rsp.content

        expected_name = "No name. ID %s" % fa.id.val
        assert expected_name in html
