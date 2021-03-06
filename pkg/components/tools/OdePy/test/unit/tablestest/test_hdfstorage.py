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
   Test of the HDF storage for the Tables API.
"""

import time
import pytest
import ode.columns
import logging
import tables
import threading
import Ice

from extras.mox import Mox
from ode.rtypes import rint, rstring

from library import TestCase
from path import path

if hasattr(tables, "open_file"):
    import ode.hdfstorageV2 as storage_module
else:
    import ode.hdfstorageV1 as storage_module

HdfList = storage_module.HdfList
HdfStorage = storage_module.HdfStorage

logging.basicConfig(level=logging.CRITICAL)

class MockAdapter(object):
    def __init__(self, ic):
        self.ic = ic

    def getCommunicator(self):
        return self.ic

class TestHdfStorage(TestCase):

    def setup_method(self, method):
        TestCase.setup_method(self, method)
        self.ic = Ice.initialize()
        self.current = Ice.Current()
        self.current.adapter = MockAdapter(self.ic)
        self.lock = threading.RLock()

        for of in ode.columns.ObjectFactories.values():
            of.register(self.ic)

    def cols(self):
        a = ode.columns.LongColumnI('a', 'first', None)
        b = ode.columns.LongColumnI('b', 'first', None)
        c = ode.columns.LongColumnI('c', 'first', None)
        return [a, b, c]

    def init(self, hdf, meta=False):
        if meta:
            m = {"analysisA": 1, "analysisB": "param", "analysisC": 4.1}
        else:
            m = None
        hdf.initialize(self.cols(), m)

    def append(self, hdf, map):
        cols = self.cols()
        for col in cols:
            try:
                col.values = [map[col.name]]
            except KeyError:
                col.values = []
        hdf.append(cols)

    def hdfpath(self):
        tmpdir = self.tmpdir()
        return path(tmpdir) / "test.h5"

    def testInvalidFile(self):
        pytest.raises(
            ode.ApiUsageException, HdfStorage, None, None)
        pytest.raises(
            ode.ApiUsageException, HdfStorage, '', self.lock)
        bad = path(self.tmpdir()) / "doesntexist" / "test.h5"
        pytest.raises(
            ode.ApiUsageException, HdfStorage, bad, self.lock)

    def testValidFile(self):
        hdf = HdfStorage(self.hdfpath(), self.lock)
        hdf.cleanup()

    def testLocking(self):
        tmp = str(self.hdfpath())
        hdf1 = HdfStorage(tmp, self.lock)
        with pytest.raises(ode.LockTimeout) as exc_info:
            HdfStorage(tmp, self.lock)
        assert exc_info.value.message.startswith('Path already in HdfList: ')
        hdf1.cleanup()
        hdf3 = HdfStorage(tmp, self.lock)
        hdf3.cleanup()

    def testSimpleCreation(self):
        hdf = HdfStorage(self.hdfpath(), self.lock)
        self.init(hdf, False)
        hdf.cleanup()

    def testCreationWithMetadata(self):
        hdf = HdfStorage(self.hdfpath(), self.lock)
        self.init(hdf, True)
        hdf.cleanup()

    def testAddSingleRow(self):
        hdf = HdfStorage(self.hdfpath(), self.lock)
        self.init(hdf, True)
        self.append(hdf, {"a": 1, "b": 2, "c": 3})
        hdf.cleanup()

    def testModifyRow(self):
        hdf = HdfStorage(self.hdfpath(), self.lock)
        self.init(hdf, True)
        self.append(hdf, {"a": 1, "b": 2, "c": 3})
        self.append(hdf, {"a": 5, "b": 6, "c": 7})
        data = hdf.readCoordinates(hdf._stamp, [0, 1], self.current)
        data.columns[0].values[0] = 100
        data.columns[0].values[1] = 200
        data.columns[1].values[0] = 300
        data.columns[1].values[1] = 400
        hdf.update(hdf._stamp, data)
        hdf.readCoordinates(hdf._stamp, [0, 1], self.current)
        hdf.cleanup()

    def testReadTicket1951(self):
        hdf = HdfStorage(self.hdfpath(), self.lock)
        self.init(hdf, True)
        self.append(hdf, {"a": 1, "b": 2, "c": 3})
        hdf.readCoordinates(hdf._stamp, [0], self.current)
        hdf.read(hdf._stamp, [0, 1, 2], 0, 1, self.current)
        hdf.cleanup()

    def testSorting(self):  # Probably shouldn't work
        hdf = HdfStorage(self.hdfpath(), self.lock)
        self.init(hdf, True)
        self.append(hdf, {"a": 0, "b": 2, "c": 3})
        self.append(hdf, {"a": 4, "b": 4, "c": 4})
        self.append(hdf, {"a": 0, "b": 1, "c": 0})
        self.append(hdf, {"a": 0, "b": 0, "c": 0})
        self.append(hdf, {"a": 0, "b": 4, "c": 0})
        self.append(hdf, {"a": 0, "b": 0, "c": 0})
        hdf.getWhereList(time.time(), '(a==0)', None, 'b', None, None, None)
        # Doesn't work yet.
        hdf.cleanup()

    def testInitializeInvalidColoumnNames(self):
        hdf = HdfStorage(self.hdfpath(), self.lock)

        with pytest.raises(ode.ApiUsageException) as exc:
            hdf.initialize([ode.columns.LongColumnI('')], None)
        assert exc.value.message.startswith('Column unnamed:')

        with pytest.raises(ode.ApiUsageException) as exc:
            hdf.initialize([ode.columns.LongColumnI('__a')], None)
        assert exc.value.message == 'Reserved column name: __a'

        hdf.initialize([ode.columns.LongColumnI('a')], None)
        hdf.cleanup()

    def testInitializationOnInitializedFileFails(self):
        p = self.hdfpath()
        hdf = HdfStorage(p, self.lock)
        self.init(hdf, True)
        hdf.cleanup()
        hdf = HdfStorage(p, self.lock)
        try:
            self.init(hdf, True)
            assert False
        except ode.ApiUsageException:
            pass
        hdf.cleanup()

    """
    Hard fails disabled. See #2067
    def testAddColumn(self):
        assert False, "NYI"

    def testMergeFiles(self):
        assert False, "NYI"

    def testVersion(self):
        assert False, "NYI"
    """

    def testHandlesExistingDirectory(self):
        t = path(self.tmpdir())
        h = t / "test.h5"
        assert t.exists()
        hdf = HdfStorage(h, self.lock)
        hdf.cleanup()

    def testGetSetMetaMap(self):
        hdf = HdfStorage(self.hdfpath(), self.lock)
        self.init(hdf, False)

        hdf.add_meta_map({'a': rint(1)})
        m1 = hdf.get_meta_map()
        assert len(m1) == 3
        assert m1['__initialized'].val > 0
        assert m1['__version'] == rstring('2')
        assert m1['a'] == rint(1)

        with pytest.raises(ode.ApiUsageException) as exc:
            hdf.add_meta_map({'b': rint(1), '__c': rint(2)})
        assert exc.value.message == 'Reserved attribute name: __c'
        assert hdf.get_meta_map() == m1

        with pytest.raises(ode.ValidationException) as exc:
            hdf.add_meta_map({'d': rint(None)})
        assert exc.value.serverStackTrace.startswith('Unsupported type:')
        assert hdf.get_meta_map() == m1

        hdf.add_meta_map({}, replace=True)
        m2 = hdf.get_meta_map()
        assert len(m2) == 2
        assert m2 == {
            '__initialized': m1['__initialized'], '__version': rstring('2')}

        hdf.add_meta_map({'__test': 1}, replace=True, init=True)
        m3 = hdf.get_meta_map()
        assert m3 == {'__test': rint(1)}

        hdf.cleanup()

    def testStringCol(self):
        hdf = HdfStorage(self.hdfpath(), self.lock)
        cols = [ode.columns.StringColumnI("name", "description", 16, None)]
        hdf.initialize(cols)
        cols[0].settable(hdf._HdfStorage__mea)  # Needed for size
        cols[0].values = ["foo"]
        hdf.append(cols)
        rows = hdf.getWhereList(time.time(), '(name=="foo")', None, 'b', None,
                                None, None)
        assert 1 == len(rows)
        assert 16 == hdf.readCoordinates(time.time(), [0],
                                         self.current).columns[0].size
        # Doesn't work yet.
        hdf.cleanup()

    #
    # ROIs
    #
    @pytest.mark.broken
    def testMaskColumn(self):
        hdf = HdfStorage(self.hdfpath(), self.lock)
        mask = ode.columns.MaskColumnI('mask', 'desc', None)
        hdf.initialize([mask], None)
        mask.imageId = [1, 2]
        mask.theZ = [2, 2]
        mask.theT = [3, 3]
        mask.x = [4, 4]
        mask.y = [5, 5]
        mask.w = [6, 6]
        mask.h = [7, 7]
        mask.bytes = [[0], [0, 1, 2, 3, 4]]
        hdf.append([mask])
        data = hdf.readCoordinates(hdf._stamp, [0, 1], self.current)
        test = data.columns[0]
        assert 1 == test.imageId[0]
        assert 2 == test.theZ[0]
        assert 3 == test.theT[0]
        assert 4 == test.x[0]
        assert 5 == test.y[0]
        assert 6 == test.w[0]
        assert 7 == test.h[0]
        assert [0] == test.bytes[0]

        assert 2 == test.imageId[1]
        assert 2 == test.theZ[1]
        assert 3 == test.theT[1]
        assert 4 == test.x[1]
        assert 5 == test.y[1]
        assert 6 == test.w[1]
        assert 7 == test.h[1]
        assert [0 == 1, 2, 3, 4], test.bytes[1]
        hdf.cleanup()


class TestHdfList(TestCase):

    def setup_method(self, method):
        TestCase.setup_method(self, method)
        self.mox = Mox()

    def hdfpath(self):
        tmpdir = self.tmpdir()
        return path(tmpdir) / "test.h5"

    def testLocking(self, monkeypatch):
        lock1 = threading.RLock()
        hdflist2 = HdfList()
        lock2 = threading.RLock()
        tmp = str(self.hdfpath())

        # Using HDFLIST
        hdf1 = HdfStorage(tmp, lock1)

        # There are multiple guards against opening the same HDF5 file

        # PyTables includes a check
        monkeypatch.setattr(storage_module, 'HDFLIST', hdflist2)
        with pytest.raises(ValueError) as exc_info:
            HdfStorage(tmp, lock2)

        assert exc_info.value.message.startswith(
            "The file '%s' is already opened. " % tmp)
        monkeypatch.undo()

        # HdfList uses portalocker, test by mocking tables.open_file
        if hasattr(tables, "open_file"):
            self.mox.StubOutWithMock(tables, 'open_file')
            tables.file._FILE_OPEN_POLICY = 'default'
            tables.open_file(tmp, mode='w',
                             title='ODE HDF Measurement Storage',
                             rootUEP='/').AndReturn(open(tmp))

            self.mox.ReplayAll()
        else:
            self.mox.StubOutWithMock(tables, 'openFile')
            tables.openFile(tmp, mode='w',
                            title='ODE HDF Measurement Storage',
                            rootUEP='/').AndReturn(open(tmp))

        monkeypatch.setattr(storage_module, 'HDFLIST', hdflist2)
        with pytest.raises(ode.LockTimeout) as exc_info:
            HdfStorage(tmp, lock2)
        print(exc_info.value)
        assert (exc_info.value.message ==
                'Cannot acquire exclusive lock on: %s' % tmp)

        monkeypatch.undo()

        hdf1.cleanup()

        self.mox.UnsetStubs()
        self.mox.VerifyAll()
