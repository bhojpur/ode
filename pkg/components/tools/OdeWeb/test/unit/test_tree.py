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
Simple unit tests for the "tree" module.
"""

import pytest

from ode.rtypes import rlong, rstring, rtime
from engine.webclient.tree import _marshal_plate_acquisition, \
    _marshal_dataset, _marshal_plate, parse_permissions_css

class MockConnection(object):

    def getUserId(self):
        return 1

    def isAdmin(self):
        return False

@pytest.fixture(scope='module')
def mock_conn():
    return MockConnection()


@pytest.fixture(scope='module')
def owner_permissions():
    return {
        'canEdit': True,
        'canAnnotate': True,
        'canLink': True,
        'canDelete': True,
        'canChgrp': True,
    }

@pytest.fixture(scope='module')
def start_time():
    # 2018-05-08 10:37:02 UTC; server timestamps contain ms
    return rtime(1399545422 * 1000)


@pytest.fixture(scope='module')
def end_time():
    # 2018-05-08 10:38:30 UTC; server timestamps contain ms
    return rtime(1399545510 * 1000)

class TestTree(object):
    """
    Tests to ensure that ODE.web "tree" infrastructure is working
    correctly.  Order and type of columns in row is:
      * id (rlong)
      * name (rstring)
      * details.owner.id (rlong)
      * details.permissions (dict)
      * startTime (rtime)
      * endTime (rtime)
    """

    def test_marshal_plate_acquisition_no_name_no_start_no_end(
            self, mock_conn, owner_permissions):
        row = [
            rlong(1),
            None,
            rlong(1),
            owner_permissions,
            None,
            None
        ]
        expected = {
            'id': 1,
            'ownerId': 1,
            'name': 'Run 1',
            'permsCss':
                'canEdit canAnnotate canLink canDelete canChgrp isOwned'
        }

        marshaled = _marshal_plate_acquisition(mock_conn, row)
        assert marshaled == expected

    def test_marshal_plate_acquisition_name_no_start_no_end(
            self, mock_conn, owner_permissions):
        row = [
            rlong(1),
            rstring('name'),
            rlong(1),
            owner_permissions,
            None,
            None
        ]
        expected = {
            'id': 1,
            'ownerId': 1,
            'name': 'name',
            'permsCss':
                'canEdit canAnnotate canLink canDelete canChgrp isOwned'
        }

        marshaled = _marshal_plate_acquisition(mock_conn, row)
        assert marshaled == expected

    def test_marshal_plate_acquisition_no_name_start_end(
            self, mock_conn, owner_permissions, start_time, end_time):
        row = [
            rlong(1),
            None,
            rlong(1),
            owner_permissions,
            start_time,
            end_time
        ]
        expected = {
            'id': 1,
            'ownerId': 1,
            'name': '2018-05-08 10:37:02 - 2018-05-08 10:38:30',
            'permsCss':
                'canEdit canAnnotate canLink canDelete canChgrp isOwned'
        }

        marshaled = _marshal_plate_acquisition(mock_conn, row)
        assert marshaled == expected

    def test_marshal_plate_acquisition_not_owner(
            self, mock_conn, owner_permissions):
        row = [
            rlong(1),
            None,
            rlong(2),
            owner_permissions,
            None,
            None
        ]
        expected = {
            'id': 1,
            'ownerId': 2,
            'name': 'Run 1',
            'permsCss': 'canEdit canAnnotate canLink canDelete canChgrp'
        }

        marshaled = _marshal_plate_acquisition(mock_conn, row)
        assert marshaled == expected

    def test_parse_permissions_css(
            self, mock_conn):
        restrictions = ('canEdit', 'canAnnotate', 'canLink', 'canDelete',
                        'canChgrp')
        # Iterate through every combination of the restrictions' flags,
        # checking each with and without expected canChgrp
        for i in range(2**len(restrictions)):
            expected = []
            permissions_dict = {'perm': '------'}
            for j in range(len(restrictions)):
                if i & 2**j != 0:
                    expected.append(restrictions[j])
                    permissions_dict[restrictions[j]] = True
                else:
                    permissions_dict[restrictions[j]] = False
            expected.sort()
            owner_id = mock_conn.getUserId()
            # Test with different owner_ids, which means canChgrp is False
            received = parse_permissions_css(permissions_dict,
                                             owner_id+1,
                                             mock_conn)
            received = filter(None, received.split(' '))
            received.sort()
            assert expected == received
            # Test with matching owner_ids, which means
            # isOwned and canChgrp is True
            expected.append('isOwned')
            expected.sort()
            received = parse_permissions_css(permissions_dict,
                                             owner_id,
                                             mock_conn)
            received = filter(None, received.split(' '))
            received.sort()
            assert expected == received

    def test_marshal_dataset(self, mock_conn, owner_permissions):
        row = [
            rlong(1),
            rstring('name'),
            rlong(1),
            owner_permissions,
            rlong(1)
        ]
        expected = {
            'id': 1,
            'ownerId': 1,
            'name': 'name',
            'permsCss':
                'canEdit canAnnotate canLink canDelete canChgrp isOwned',
            'childCount': 1
        }

        marshaled = _marshal_dataset(mock_conn, row)
        assert marshaled == expected

    def test_marshal_dataset_not_owner(self, mock_conn, owner_permissions):
        row = [
            rlong(1),
            rstring('name'),
            rlong(2),
            owner_permissions,
            rlong(1)
        ]
        expected = {
            'id': 1,
            'ownerId': 2,
            'name': 'name',
            'permsCss': 'canEdit canAnnotate canLink canDelete canChgrp',
            'childCount': 1
        }

        marshaled = _marshal_dataset(mock_conn, row)
        assert marshaled == expected

    def test_marshal_plate(self, mock_conn, owner_permissions):
        row = [
            rlong(1),
            rstring('name'),
            rlong(1),
            owner_permissions,
            2
        ]
        expected = {
            'id': 1,
            'ownerId': 1,
            'name': 'name',
            'permsCss':
                'canEdit canAnnotate canLink canDelete canChgrp isOwned',
                'childCount': 2
        }

        marshaled = _marshal_plate(mock_conn, row)
        assert marshaled == expected

    def test_marshal_plate_not_owner(self, mock_conn, owner_permissions):
        row = [
            rlong(1),
            rstring('name'),
            rlong(2),
            owner_permissions,
            2
        ]
        expected = {
            'id': 1,
            'ownerId': 2,
            'name': 'name',
            'permsCss': 'canEdit canAnnotate canLink canDelete canChgrp',
            'childCount': 2
        }

        marshaled = _marshal_plate(mock_conn, row)
        print(marshaled)
        print(expected)
        assert marshaled == expected

    # Add a lot of tests
