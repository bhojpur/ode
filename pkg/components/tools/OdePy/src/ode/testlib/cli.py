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

from builtins import object
import pytest

import ode
from ode.cli import CLI
from ode.plugins.sessions import SessionsControl
from ode.rtypes import rstring

from ode.testlib import ITest
from mox3 import mox

class AbstractCLITest(ITest):

    @classmethod
    def setup_class(cls):
        super(AbstractCLITest, cls).setup_class()
        cls.cli = CLI()
        cls.cli.register("sessions", SessionsControl, "TEST")

    def setup_mock(self):
        self.mox = mox.Mox()

    def teardown_mock(self):
        self.mox.UnsetStubs()
        self.mox.VerifyAll()

class CLITest(AbstractCLITest):

    def setup_method(self, method):
        self.args = self.login_args()

    def create_object(self, object_type, name=""):
        # create object
        if object_type == 'Dataset':
            new_object = ode.model.DatasetI()
        elif object_type == 'Project':
            new_object = ode.model.ProjectI()
        elif object_type == 'Plate':
            new_object = ode.model.PlateI()
        elif object_type == 'Screen':
            new_object = ode.model.ScreenI()
        elif object_type == 'Image':
            new_object = self.new_image()
        new_object.name = rstring(name)
        new_object = self.update.saveAndReturnObject(new_object)

        # check object has been created
        found_object = self.query.get(object_type, new_object.id.val)
        assert found_object.id.val == new_object.id.val

        return new_object.id.val

    @pytest.fixture()
    def simpleHierarchy(self):
        proj = self.make_project()
        dset = self.make_dataset()
        img = self.update.saveAndReturnObject(self.new_image())
        self.link(proj, dset)
        self.link(dset, img)
        return proj, dset, img

class RootCLITest(AbstractCLITest):

    def setup_method(self, method):
        self.args = self.root_login_args()

class ArgumentFixture(object):

    """
    Used to test the user/group argument
    """

    def __init__(self, prefix, attr):
        self.prefix = prefix
        self.attr = attr

    def get_arguments(self, obj):
        args = []
        if self.prefix:
            args += [self.prefix]
        if self.attr:
            args += ["%s" % getattr(obj, self.attr).val]
        return args

    def __repr__(self):
        if self.prefix:
            return "%s" % self.prefix
        else:
            return "%s" % self.attr

UserIdNameFixtures = (
    ArgumentFixture('--id', 'id'),
    ArgumentFixture('--name', 'odeName'),
    )

UserFixtures = (
    ArgumentFixture(None, 'id'),
    ArgumentFixture(None, 'odeName'),
    ArgumentFixture('--user-id', 'id'),
    ArgumentFixture('--user-name', 'odeName'),
    )

GroupIdNameFixtures = (
    ArgumentFixture('--id', 'id'),
    ArgumentFixture('--name', 'name'),
    )

GroupFixtures = (
    ArgumentFixture(None, 'id'),
    ArgumentFixture(None, 'name'),
    ArgumentFixture('--group-id', 'id'),
    ArgumentFixture('--group-name', 'name'),
    )

def get_user_ids(out, sort_key=None):
    columns = {'login': 1, 'first-name': 2, 'last-name': 3, 'email': 4}
    lines = out.split('\n')
    ids = []
    last_value = None
    for line in lines[2:]:
        elements = line.split('|')
        if len(elements) < 8:
            continue

        ids.append(int(elements[0].strip()))
        if sort_key:
            if sort_key == 'id':
                new_value = ids[-1]
            else:
                new_value = elements[columns[sort_key]].strip()
            if last_value is not None:
                assert new_value >= last_value
            last_value = new_value
    return ids

def get_group_ids(out, sort_key=None):
    lines = out.split('\n')
    ids = []
    last_value = None
    for line in lines[2:]:
        elements = line.split('|')
        if len(elements) < 4:
            continue

        ids.append(int(elements[0].strip()))
        if sort_key:
            if sort_key == 'id':
                new_value = ids[-1]
            else:
                new_value = elements[1].strip()
            if last_value is not None:
                assert new_value >= last_value
            last_value = new_value
    return ids