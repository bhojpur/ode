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
Integration test focused on the ode.api.IUpdate interface.
"""

from ode.testlib import ITest
import ode
import pytest

class TestIUpdate(ITest):
    """
    Basic test of all IUpdate functionality
    """

    def tags(self, count=3):
        return [ode.model.TagAnnotationI() for x in range(count)]

    def testSaveArray(self):
        """
        See ticket:6870
        """
        tags = self.tags()
        self.update.saveArray(tags)

    def testSaveCollection(self):
        """
        See ticket:6870
        """
        tags = self.tags()
        self.update.saveCollection(tags)

    def testExternalInfoOnCreation(self):
        ds = self.mkdataset(True)
        ds = self.update.saveAndReturnObject(ds)
        assert ds.details.externalInfo
        self.assert_type(ds, "test")

    def testExternalInfoAfterCreationTransient(self):
        ds = self.mkdataset(False)
        ds = self.update.saveAndReturnObject(ds)
        assert not ds.details.externalInfo
        ds.details.externalInfo = self.mkinfo("late")
        ds = self.update.saveAndReturnObject(ds)
        assert ds.details.externalInfo
        self.assert_type(ds, "late")

    def testExternalInfoAfterCreationManaged(self):
        ds = self.mkdataset(False)
        ds = self.update.saveAndReturnObject(ds)
        assert not ds.details.externalInfo
        info = self.mkinfo("late")
        info = self.update.saveAndReturnObject(info)
        ds.details.externalInfo = info
        ds = self.update.saveAndReturnObject(ds)
        assert ds.details.externalInfo
        self.assert_type(ds, "late")

    def testExternalInfoNewInstance(self):
        ds = self.mkdataset(True)
        ds = self.update.saveAndReturnObject(ds)
        info = self.mkinfo(type="updated")
        ds.details.externalInfo = info
        ds = self.update.saveAndReturnObject(ds)
        self.assert_type(ds, "updated")

    def testExternalInfoNullInstance(self):
        ds = self.mkdataset(True)
        ds = self.update.saveAndReturnObject(ds)
        ds.details.externalInfo = None
        ds = self.update.saveAndReturnObject(ds)
        self.assert_type(ds, None)

    def testExternalInfoUpdateInstance(self):
        ds = self.mkdataset(True)
        ds = self.update.saveAndReturnObject(ds)
        self.assert_type(ds, "test")
        info = ds.details.externalInfo
        info.entityType = ode.rtypes.rstring("updated")
        ds = self.update.saveAndReturnObject(ds)
        # This is still disallowed since the ExternalInfo
        # object itself is immutable (it has no update_id
        # column).
        with pytest.raises(Exception):
            self.assert_type(ds, "updated")

    def testCannotSaveDeleted(self):
        ds = self.mkdataset(True)
        ds = self.update.saveAndReturnObject(ds)
        self.delete([ds])
        ds.name = ode.rtypes.rstring("now is deleted")
        with pytest.raises(ode.ValidationException):
            self.update.saveObject(ds)

    # Helpers

    def reload(self, ds):
        return self.query.findByQuery((
            "select ds from Dataset ds "
            "left outer join fetch ds.details.externalInfo "
            "where ds.id = :id"), ode.sys.ParametersI().addId(ds.id))

    def mkdataset(self, info):
        ds = ode.model.DatasetI()
        ds.name = ode.rtypes.rstring("testExternalInfo")
        if info:
            ds.details.externalInfo = self.mkinfo()
        return ds

    def mkinfo(self, type="test"):
        info = ode.model.ExternalInfoI()
        info.entityType = ode.rtypes.rstring(type)
        info.entityId = ode.rtypes.rlong(1)
        return info

    def assert_type(self, ds, value):
        if value is None:
            assert ds.details.externalInfo is None
        else:
            assert ds.details.externalInfo.entityType.val == value
        ds = self.reload(ds)
        if value is None:
            assert ds.details.externalInfo is None
        else:
            assert ds.details.externalInfo.entityType.val == value
        return ds

    def incr(self, ds):
        if ds.version is None:
            ds.version = ode.rtypes.rint(0)
        else:
            i = ds.version.val
            ds.version = ode.rtypes.rint(i+1)
