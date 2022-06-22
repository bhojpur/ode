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

"""Tests querying Images with web json api."""

from engine.testlib import IWebTest, get_json
from django.core.urlresolvers import reverse
from engine.api import api_settings
import pytest
from test_api_projects import cmp_name_insensitive, get_update_service, \
    get_connection, marshal_objects
from ode.model import DatasetI, ImageI
from ode.rtypes import rstring
import json

def get_query_service(user):
    """Get the query_service for the given user's client."""
    return user[0].getSession().getQueryService()

def assert_objects(conn, json_objects, ode_ids_objects, dtype="Project",
                   group='-1', extra=None, opts=None):
    """
    Load objects from Bhojpur ODE, via conn.getObjects().

    marshal with ode_marshal and compare with json_objects.
    ode_ids_objects can be IDs or list of ode.model objects.

    @param: extra       List of dicts containing expected extra json data
                        e.g. {'ode:childCount': 1}
    """
    pids = []
    for p in ode_ids_objects:
        try:
            pids.append(long(p))
        except TypeError:
            pids.append(p.id.val)
    conn.SERVICE_OPTS.setOdeGroup(group)
    objs = conn.getObjects(dtype, pids, respect_order=True, opts=opts)
    objs = [p._obj for p in objs]
    expected = marshal_objects(objs)
    assert len(json_objects) == len(expected)
    for i, o1, o2 in zip(range(len(expected)), json_objects, expected):
        if extra is not None and i < len(extra):
            o2.update(extra[i])
        # dumping to json and loading (same as test data) means that
        # unicode has been handled in same way, e.g. Pixel size symbols.
        o2 = json.loads(json.dumps(o2))
        # remove any urls from json (tested elsewhere)
        for key in o1.keys():
            if key.startswith('url:'):
                del(o1[key])
        assert o1 == o2

class TestImages(IWebTest):
    """Tests querying & editing Images."""

    @pytest.fixture()
    def user1(self):
        """Return a new user in a read-annotate group."""
        group = self.new_group(perms='rwra--')
        return self.new_client_and_user(group=group)

    @pytest.fixture()
    def dataset_images(self, user1):
        """Return Dataset with Images and an orphaned Image."""
        dataset = DatasetI()
        dataset.name = rstring('Dataset')

        # Create 5 Images in Dataset
        for i in range(5):
            img = self.create_test_image(size_x=125, size_y=125,
                                         session=user1[0].getSession(),
                                         name="Image%s" % i)
            img = ImageI(img.id.val, False)
            dataset.linkImage(img)

        # Create a single orphaned Image
        image = self.create_test_image(size_x=125, size_y=125,
                                       session=user1[0].getSession())

        dataset = get_update_service(user1).saveAndReturnObject(dataset)
        return dataset, image

    def test_dataset_images(self, user1, dataset_images):
        """Test listing of Images in a Dataset."""
        conn = get_connection(user1)
        group_id = conn.getEventContext().groupId
        user_name = conn.getUser().getName()
        django_client = self.new_django_client(user_name, user_name)
        version = api_settings.API_VERSIONS[-1]

        dataset = dataset_images[0]
        images = dataset.linkedImageList()
        orphaned = dataset_images[1]

        images_url = reverse('api_images', kwargs={'api_version': version})
        datasets_url = reverse('api_datasets', kwargs={'api_version': version})

        # List ALL Images
        rsp = get_json(django_client, images_url, {'group': group_id})
        assert len(rsp['data']) == 6
        assert rsp['meta'] == {'totalCount': 6,
                               'limit': api_settings.API_LIMIT,
                               'maxLimit': api_settings.API_MAX_LIMIT,
                               'offset': 0}

        # Filter Images by Orphaned
        payload = {'orphaned': 'true', 'group': group_id}
        rsp = get_json(django_client, images_url, payload)
        assert_objects(conn, rsp['data'], [orphaned], dtype='Image',
                       group=group_id, opts={'load_pixels': True})
        assert rsp['meta'] == {'totalCount': 1,
                               'limit': api_settings.API_LIMIT,
                               'maxLimit': api_settings.API_MAX_LIMIT,
                               'offset': 0}

        # Filter Images by Dataset
        images.sort(cmp_name_insensitive)
        payload = {'dataset': dataset.id.val}
        rsp = get_json(django_client, images_url, payload)
        # Manual check that Pixels & Type are loaded but Channels are not
        assert 'Type' in rsp['data'][0]['Pixels']
        assert 'Channels' not in rsp['data'][0]['Pixels']
        assert_objects(conn, rsp['data'], images, dtype='Image',
                       opts={'load_pixels': True})
        assert rsp['meta'] == {'totalCount': 5,
                               'limit': api_settings.API_LIMIT,
                               'maxLimit': api_settings.API_MAX_LIMIT,
                               'offset': 0}

        # Pagination, listing images via /datasets/:id/images/
        limit = 3
        dataset_images_url = datasets_url + "%s/images/" % dataset.id.val
        payload = {'dataset': dataset.id.val, 'limit': limit}
        rsp = get_json(django_client, dataset_images_url, payload)
        assert_objects(conn, rsp['data'], images[0:limit], dtype='Image',
                       opts={'load_pixels': True})
        assert rsp['meta'] == {'totalCount': 5,
                               'limit': limit,
                               'maxLimit': api_settings.API_MAX_LIMIT,
                               'offset': 0}
        payload['offset'] = limit   # page 2
        rsp = get_json(django_client, images_url, payload)
        assert_objects(conn, rsp['data'], images[limit:limit * 2],
                       dtype='Image', opts={'load_pixels': True})
        assert rsp['meta'] == {'totalCount': 5,
                               'limit': limit,
                               'maxLimit': api_settings.API_MAX_LIMIT,
                               'offset': limit}

        # Show ONLY the orphaned image (channels are loaded by default)
        img_url = images_url + '%s/' % orphaned.id.val
        rsp = get_json(django_client, img_url)
        # Manual check that Channels are loaded
        img_json = rsp['data']
        assert len(img_json['Pixels']['Channels']) == 1
        assert_objects(conn, [img_json], [orphaned], dtype='Image',
                       opts={'load_channels': True})
