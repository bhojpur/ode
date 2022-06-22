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
   Test of the Tables facility independent of Ice.
"""

import pytest
import Ice
import ode
import ode.tables
import uuid
import logging
from library import TestCase

from ode.columns import LongColumnI, DoubleColumnI, ObjectFactories
from path import path

logging.basicConfig(level=logging.DEBUG)

# Don't retry since we expect errors
ode.tables.RETRIES = 1

class communicator_provider(object):
    def __init__(self, ic=None):
        self.ic = ic

    def __call__(self, *args):
        return self.ic


class mock_communicator(object):
    def __init__(self):
        self.delegate = Ice.initialize()
        for of in ObjectFactories.values():
            of.register(self.delegate)  # Columns

    # Delegated
    def getProperties(self):
        return self.delegate.getProperties()

    def findObjectFactory(self, s):
        return self.delegate.findObjectFactory(s)

    # Overridden
    def stringToProxy(self, arg):
        return arg


class mock_current(object):
    def __init__(self, communicator):
        self.adapter = mock_adapter(communicator)
        self.ctx = {}


class mock_adapter(object):
    def __init__(self, communicator):
        self.ic = communicator

    def addWithUUID(self, arg):
        return arg

    def add(self, arg, id):
        return arg

    def getCommunicator(self):
        return self.ic


class mocked_internal_service_factory(object):
    def __init__(self, sf=None):
        if sf is None:
            sf = mocked_service_factory()
        self.sf = sf

    def __call__(self, *args, **kwargs):
        if not self.sf:
            raise Exception("Mock error connecting to server")
        return self.sf


class mocked_service_factory(object):
    def __init__(self):
        self.db_uuid = str(uuid.uuid4())
        self.return_values = []

    def keepAlive(self, *args):
        pass

    def getAdminService(self):
        return mocked_admin_service(True)

    def getConfigService(self):
        return mocked_config_service(self.db_uuid, self.return_values)

    def getQueryService(self):
        return mocked_query_service(self.return_values)

    def destroy(self):
        pass


class mocked_admin_service(object):
    def __init__(self, can_update):
        self.can_update = can_update

    def canUpdate(self, file_obj, call_context=None):
        return self.can_update


class mocked_config_service(object):
    def __init__(self, db_uuid, return_values):
        self.db_uuid = db_uuid
        self.return_values = return_values

    def getDatabaseUuid(self):
        return self.db_uuid

    def getConfigValue(self, str):
        # Not testing read-only yet.
        # Server sets these runtime properties at startup.
        if str.startswith("ode.cluster.read_only.runtime."):
            return "false"
        rv = self.return_values.pop(0)
        if isinstance(rv, ode.ServerError):
            raise rv
        else:
            return rv


class mocked_query_service(object):
    def __init__(self, return_values):
        self.return_values = return_values

    def findByQuery(self, *args):
        rv = self.return_values.pop(0)
        if isinstance(rv, ode.ServerError):
            raise rv
        else:
            return rv

    def get(self, *args):
        rv = self.return_values.pop(0)
        if isinstance(rv, ode.ServerError):
            raise rv
        else:
            return rv


class mock_internal_repo(object):
    def __init__(self, dir):
        self.path = dir / "mock.h5"

    def __call__(self, *args):
        return self

    def getProxy(self):
        return self

    def getFilePath(self, *args):
        return self.path


class mock_table(object):
    def __call__(self, *args):
        self.table = args[0]
        return self


class mock_storage(object):
    def __init__(self):
        self.up = False
        self.down = False

    def incr(self, *args):
        self.up = True

    def decr(self, *args):
        self.down = True

    def size(self):
        return 0


class TestTables(TestCase):

    def setup_method(self, method):
        TestCase.setup_method(self, method)

        # Session
        self.sf_provider = mocked_internal_service_factory()
        ode.util.internal_service_factory = self.sf_provider
        self.sf = self.sf_provider()

        # Context
        serverid = "mock_table"
        self.communicator = mock_communicator()
        self.communicator_provider = communicator_provider(self.communicator)
        self.stop_event = ode.util.concurrency.get_event()
        self.ctx = ode.util.ServerContext(serverid, self.communicator,
                                            self.stop_event)

        self.current = mock_current(self.communicator)
        self.__tables = []

    def teardown_method(self, method):
        """
        To prevent cleanup from taking place, we hold on to all the tables
        until the end.
        This is caused by the reuse of TableI instances after the Tables go
        out of scope.
        """
        for t in self.__tables:
            t.__del__()

    def tablesI(self, internal_repo=None):
        if internal_repo is None:
            internal_repo = mock_internal_repo(self.tmp)
        t = ode.tables.TablesI(self.ctx, mock_table(), internal_repo)
        self.__tables.append(t)
        return t

    def repouuid(self):
        """
        Returns a string similar to that written by
        RandomAccessFile.writeUTF() in Java
        """
        return "XX%s" % uuid.uuid4()

    def repodir(self, make=True):
        self.tmp = path(self.tmpdir())
        self.communicator.getProperties().setProperty("ode.repo.dir",
                                                      str(self.tmp))
        repo = self.tmp / ".ode" / "repository"
        if make:
            repo.makedirs()
        return str(repo)

    def repofile(self, db_uuid, repo_uuid=None):
        if repo_uuid is None:
            repo_uuid = self.repouuid()
        f = self.repodir()
        f = path(f) / db_uuid
        f.makedirs()
        f = f / "repo_uuid"
        f.write_lines([repo_uuid])

    # Note: some of the following method were added as __init__ called
    # first _get_dir() and then _get_uuid(), so the naming is off

    def testTablesIGetDirNoRepoSet(self):
        self.sf.return_values.append(self.tmpdir())
        pytest.raises(ode.ResourceError, ode.tables.TablesI, self.ctx)

    def testTablesIGetDirNoRepoCreated(self):
        self.repodir(False)
        pytest.raises(ode.ResourceError, ode.tables.TablesI, self.ctx)

    def testTablesIGetDirGetsRepoThenNoSF(self):
        self.repodir()
        ode.util.internal_service_factory = \
            mocked_internal_service_factory(None)
        pytest.raises(Exception, ode.tables.TablesI, self.ctx)

    def testTablesIGetDirGetsRepoGetsSFCantFindRepoFile(self):
        self.repodir()
        pytest.raises(IOError, ode.tables.TablesI, self.ctx)

    def testTablesIGetDirGetsRepoGetsSFCantFindRepoObject(self):
        self.repofile(self.sf.db_uuid)
        self.sf.return_values.append(
            ode.ApiUsageException(None, None, "Can't Find"))
        pytest.raises(ode.ApiUsageException, ode.tables.TablesI, self.ctx)

    def testTablesIGetDirGetsRepoGetsSFGetsRepo(self):
        self.repofile(self.sf.db_uuid)
        self.sf.return_values.append(ode.model.OriginalFileI(1, False))
        self.tablesI()

    def testTables(self, newfile=True):
        if newfile:
            self.repofile(self.sf.db_uuid)
        f = ode.model.OriginalFileI(1, True)
        f.details.group = ode.model.ExperimenterGroupI(1, False)
        self.sf.return_values.append(f)
        tables = self.tablesI()
        table = tables.getTable(f, self.sf, self.current)
        assert table
        assert table.table
        assert table.table.storage
        return table

    def testTableOriginalFileLoaded(self):
        f1 = ode.model.OriginalFileI(1, False)
        f2 = ode.model.OriginalFileI(1, True)
        f2.details.group = ode.model.ExperimenterGroupI(1, False)
        self.sf.return_values.append(f2)
        storage = mock_storage()
        self.ctx.newSession()
        table = ode.tables.TableI(self.ctx, f1, self.sf, storage)
        assert table.file_obj.details.group

    def testTableIncrDecr(self):
        f = ode.model.OriginalFileI(1, True)
        f.details.group = ode.model.ExperimenterGroupI(1, False)
        storage = mock_storage()
        table = ode.tables.TableI(self.ctx, f, self.sf, storage)
        assert storage.up
        table.cleanup()
        assert storage.down

    def testTablePreInitialized(self):
        f = ode.model.OriginalFileI(1, True)
        f.details.group = ode.model.ExperimenterGroupI(1, False)
        mocktable = self.testTables()
        table1 = mocktable.table
        storage = table1.storage
        storage.initialize([LongColumnI("a", None, [])])
        table2 = ode.tables.TableI(self.ctx, f, self.sf, storage)
        table2.cleanup()
        table1.cleanup()

    def testTableModifications(self):
        mocktable = self.testTables()
        table = mocktable.table
        storage = table.storage
        storage.initialize([LongColumnI("a", None, [])])
        assert storage.uptodate(table.stamp)
        storage._stamp += 1  # Not really allowed
        assert not storage.uptodate(table.stamp)
        table.cleanup()

    def testTableAddData(self, newfile=True, cleanup=True):
        mocktable = self.testTables(newfile)
        table = mocktable.table
        storage = table.storage
        assert storage

        table.initialize([LongColumnI("a", None, []),
                          DoubleColumnI("b", None, [])])
        template = table.getHeaders(self.current)
        template[0].values = [1] * 5
        template[1].values = [2.0] * 5
        table.addData(template)
        if cleanup:
            table.cleanup()
        return table

    def testTableSearch(self):
        table = self.testTableAddData(True, False)
        rv = list(table.getWhereList('(a==1)', None, None, None, None, None))
        assert range(5) == rv
        data = table.readCoordinates(rv, self.current)
        assert 2 == len(data.columns)
        for i in range(5):
            assert 1 == data.columns[0].values[i]
            assert 2.0 == data.columns[1].values[i]
        table.cleanup()

    def testErrorInStorage(self):
        self.repofile(self.sf.db_uuid)
        of = ode.model.OriginalFileI(1, False)
        self.sf.return_values.append(of)

        internal_repo = mock_internal_repo(self.tmp)
        f = open(internal_repo.path, "w")
        f.write("this file is not HDF")
        f.close()

        tables = self.tablesI(internal_repo)
        pytest.raises(ode.ValidationException, tables.getTable, of, self.sf,
                      self.current)

    def testErrorInGet(self):
        self.repofile(self.sf.db_uuid)
        f = ode.model.OriginalFileI(1, True)
        f.details.group = ode.model.ExperimenterGroupI(1, False)
        self.sf.return_values.append(f)

        tables = self.tablesI()
        table = tables.getTable(f, self.sf, self.current).table  # From mock
        cols = [ode.columns.LongColumnI('name', 'desc', None)]
        table.initialize(cols)
        cols[0].values = [1, 2, 3, 4]
        table.addData(cols)
        table.getWhereList('(name==1)', None, 0, 0, 0, self.current)
