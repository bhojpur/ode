#!/usr/bin/env python
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

# Bhojpur ODE HdfStorage Interface

from builtins import str
from builtins import zip
from builtins import range
from builtins import object
from future.utils import native, bytes_to_native_str, isbytes
from past.builtins import basestring
import time
import numpy
import logging
import threading
import traceback

from os import W_OK

import core  # Do we need both??
import core.clients
import core.callbacks

# For ease of use
from core.columns import columns2definition
from core.rtypes import rfloat, rlong, rstring, unwrap
from core.util.decorators import locked
from extras.path import path
from extras import portalocker
from functools import wraps

sys = __import__("sys")  # Python sys
tables = __import__("tables")  # Pytables

try:
    # long only exists on Python 2
    # Recent versions of PyTables may have treated Python 2 int and long
    # identically anyway so treat the same
    TABLES_METADATA_INT_TYPES = (int, numpy.int64, long)
except NameError:
    TABLES_METADATA_INT_TYPES = (int, numpy.int64)

VERSION = '2'

def internal_attr(s):
    """
    Checks whether this attribute name is reserved for internal use
    """
    return s.startswith('__')

def stamped(func, update=False):
    """
    Decorator which takes the first argument after "self" and compares
    that to the last modification time. If the stamp is older, then the
    method call will throw an core.OptimisticLockException. Otherwise,
    execution will complete normally. If update is True, then the
    last modification time will be updated after the method call if it
    is successful.

    Note: stamped implies locked

    """
    def check_and_update_stamp(*args, **kwargs):
        self = args[0]
        stamp = args[1]
        if stamp < self._stamp:
            raise core.OptimisticLockException(
                None, None, "Resource modified by another thread")

        try:
            return func(*args, **kwargs)
        finally:
            if update:
                self._stamp = time.time()
    check_and_update_stamp = wraps(func)(check_and_update_stamp)
    return locked(check_and_update_stamp)

def modifies(func):
    """
    Decorator which always calls flush() on the first argument after the
    method call
    """
    def flush_after(*args, **kwargs):
        self = args[0]
        try:
            return func(*args, **kwargs)
        finally:
            self.flush()
    return wraps(func)(flush_after)

class HdfList(object):

    """
    Since two calls to tables.open_file() return non-equal files
    with equal fileno's, portalocker cannot be used to prevent
    the creation of two HdfStorage instances from the same
    Python process.

    This also holds a global lock for all HDF5 calls since libhdf5 is usually
    compiled without --enable-threadsafe
    """

    def __init__(self):
        self.logger = logging.getLogger("core.tables.HdfList")
        self._lock = threading.RLock()
        self.__filenos = {}
        self.__paths = {}

    @locked
    def addOrThrow(self, hdfpath, hdfstorage, read_only=False):

        if hdfpath in self.__paths:
            raise core.LockTimeout(
                None, None, "Path already in HdfList: %s" % hdfpath)

        parent = path(hdfpath).parent
        if not parent.exists():
            raise core.ApiUsageException(
                None, None, "Parent directory does not exist: %s" % parent)

        mode = read_only and "r" or "a"
        hdffile = hdfstorage.openfile(mode)
        fileno = hdffile.fileno()

        if not read_only:
            try:
                portalocker.lockno(
                    fileno, portalocker.LOCK_NB | portalocker.LOCK_EX)
            except portalocker.LockException:
                hdffile.close()
                raise core.LockTimeout(
                    None, None,
                    "Cannot acquire exclusive lock on: %s" % hdfpath, 0)
            except:
                hdffile.close()
                raise

        if fileno in list(self.__filenos.keys()):
            hdffile.close()
            raise core.LockTimeout(
                None, None, "File already opened by process: %s" % hdfpath, 0)
        else:
            self.__filenos[fileno] = hdfstorage
            self.__paths[hdfpath] = hdfstorage

        return hdffile

    @locked
    def getOrCreate(self, hdfpath, table, read_only=False):
        storage = None
        try:
            storage = self.__paths[hdfpath]
        except KeyError:
            # Adds itself to the global list
            storage = HdfStorage(hdfpath, self._lock, read_only=read_only)
        storage.incr(table)
        return storage

    @locked
    def remove(self, hdfpath, hdffile):
        del self.__filenos[hdffile.fileno()]
        del self.__paths[hdfpath]

# Global object for maintaining files
HDFLIST = HdfList()

class HdfStorage(object):

    """
    Provides HDF-storage for measurement results. At most a single
    instance will be available for any given physical HDF5 file.
    """

    def __init__(self, file_path, hdf5lock, read_only=False):
        """
        file_path should be the path to a file in a valid directory where
        this HDF instance can be stored (Not None or Empty). Once this
        method is finished, self.__hdf_file is guaranteed to be a PyTables HDF
        file, but not necessarily initialized.
        """

        if file_path is None or str(file_path) == "":
            raise core.ValidationException(None, None, "Invalid file_path")

        self.logger = logging.getLogger("core.tables.HdfStorage")

        self.__hdf_path = path(file_path)
        # Locking first as described at:
        # http://www.pytables.org/trac/ticket/185
        self.__hdf_file = HDFLIST.addOrThrow(file_path, self, read_only)
        self.__tables = []

        self._lock = hdf5lock
        self._stamp = time.time()

        # These are what we'd like to have
        self.__mea = None
        self.__ode = None

        try:
            self.__ode = self.__hdf_file.root.ODE
            self.__mea = self.__ode.Measurements
            self.__types = self.__ode.ColumnTypes[:]
            self.__descriptions = self.__ode.ColumnDescriptions[:]
            self.__initialized = True
        except tables.NoSuchNodeError:
            self.__initialized = False

        self._modified = False

    #
    # Non-locked methods
    #

    def size(self):
        return self.__hdf_path.size

    def openfile(self, mode, policy='default'):
        tables.file._FILE_OPEN_POLICY = policy
        try:
            if self.__hdf_path.exists():
                if self.__hdf_path.size == 0:
                    mode = "w"
                elif mode != "r" and not self.__hdf_path.access(W_OK):
                    self.logger.info(
                        "%s not writable (mode=%s). Opening read-only" % (
                            self.__hdf_path, mode))
                    mode = "r"

            return tables.open_file(native(str(self.__hdf_path)), mode=mode,
                                    title="ODE HDF Measurement Storage",
                                    rootUEP="/")
        except (tables.HDF5ExtError, IOError) as e:
            msg = "HDFStorage initialized with bad path: %s: %s" % (
                self.__hdf_path, e)
            self.logger.error(msg)
            raise core.ValidationException(None, None, msg)
        except (ValueError) as e:

            # trap PyTables >= 3.1 FILE_OPEN_POLICY exception
            # to provide an updated message
            if 'FILE_OPEN_POLICY' in str(e):
                e = ValueError(
                    "PyTables [{version}] no longer supports opening multiple "
                    "files\n"
                    "even in read-only mode on this HDF5 version "
                    "[{hdf_version}]. You can accept this\n"
                    "and not open the same file multiple times at once,\n"
                    "upgrade the HDF5 version, or downgrade to PyTables 3.0.0 "
                    "which allows\n"
                    "files to be opened multiple times at once\n"
                    .format(version=tables.__version__,
                            hdf_version=tables.get_hdf5_version()))

            raise e

    def modified(self):
        return self._modified

    def __initcheck(self):
        if not self.__initialized:
            raise core.ApiUsageException(None, None, "Not yet initialized")

    def __width(self):
        return len(self.__types)

    def __length(self):
        return self.__mea.nrows

    def __sizecheck(self, colNumbers, rowNumbers):
        if colNumbers is not None:
            if len(colNumbers) > 0:
                maxcol = max(colNumbers)
                totcol = self.__width()
                if maxcol >= totcol:
                    raise core.ApiUsageException(
                        None, None, "Column overflow: %s >= %s"
                        % (maxcol, totcol))
            else:
                raise core.ApiUsageException(
                    None, None, "Columns not specified: %s" % colNumbers)

        if rowNumbers is not None:
            if len(rowNumbers) > 0:
                maxrow = max(rowNumbers)
                totrow = self.__length()
                if maxrow >= totrow:
                    raise core.ApiUsageException(
                        None, None, "Row overflow: %s >= %s"
                        % (maxrow, totrow))
            else:
                raise core.ApiUsageException(
                    None, None, "Rows not specified: %s" % rowNumbers)

    def __getversion(self):
        """
        In ODE.tables v2 the version attribute name was changed to __version
        """
        self.__initcheck()
        k = '__version'
        try:
            v = self.__mea.attrs[k]
            if isinstance(v, basestring):
                return v
        except KeyError:
            k = 'version'
            v = self.__mea.attrs[k]
            if isbytes(v):
                v = bytes_to_native_str(v)
            if v == 'v1':
                return '1'

        msg = "Invalid version attribute (%s=%s) in path: %s" % (
            k, v, self.__hdf_path)
        self.logger.error(msg)
        raise core.ValidationException(None, None, msg)

    #
    # Locked methods
    #

    @locked
    def flush(self):
        """
        Flush writes to the underlying table, mark this object as modified
        """
        self._modified = True
        if self.__mea:
            self.__mea.flush()
        self.logger.debug("Modified flag set")

    @locked
    @modifies
    def initialize(self, cols, metadata=None):
        """

        """
        if metadata is None:
            metadata = {}

        if self.__initialized:
            raise core.ValidationException(None, None, "Already initialized.")

        if not cols:
            raise core.ApiUsageException(None, None, "No columns provided")

        for c in cols:
            if not c.name:
                raise core.ApiUsageException(
                    None, None, "Column unnamed: %s" % c)
            if internal_attr(c.name):
                raise core.ApiUsageException(
                    None, None, "Reserved column name: %s" % c.name)

        self.__definition = columns2definition(cols)
        self.__ode = self.__hdf_file.create_group("/", "ODE")
        self.__mea = self.__hdf_file.create_table(
            self.__ode, "Measurements", self.__definition)

        self.__types = [x.ice_staticId() for x in cols]
        self.__descriptions = [
            (x.description is not None) and x.description or "" for x in cols]
        self.__hdf_file.create_array(self.__ode, "ColumnTypes", self.__types)
        self.__hdf_file.create_array(
            self.__ode, "ColumnDescriptions", self.__descriptions)

        md = {}
        if metadata:
            md = metadata.copy()
        md['__version'] = VERSION
        md['__initialized'] = time.time()
        self.add_meta_map(md, replace=True, init=True)

        self.__hdf_file.flush()
        self.__initialized = True

    @locked
    def incr(self, table):
        sz = len(self.__tables)
        self.logger.info("Size: %s - Attaching %s to %s" %
                         (sz, table, self.__hdf_path))
        if table in self.__tables:
            self.logger.warn("Already added")
            raise core.ApiUsageException(None, None, "Already added")
        self.__tables.append(table)
        return sz + 1

    @locked
    def decr(self, table):
        sz = len(self.__tables)
        self.logger.info(
            "Size: %s - Detaching %s from %s", sz, table, self.__hdf_path)
        if not (table in self.__tables):
            self.logger.warn("Unknown table")
            raise core.ApiUsageException(None, None, "Unknown table")
        self.__tables.remove(table)
        if sz <= 1:
            self.cleanup()
        return sz - 1

    @locked
    def uptodate(self, stamp):
        return self._stamp <= stamp

    @locked
    def rows(self):
        self.__initcheck()
        return self.__mea.nrows

    @locked
    def cols(self, size, current):
        self.__initcheck()
        ic = current.adapter.getCommunicator()
        types = self.__types
        names = self.__mea.colnames
        descs = self.__descriptions
        cols = []
        for i in range(len(types)):
            t = types[i]
            if isbytes(t):
                t = bytes_to_native_str(t)
            n = names[i]
            d = descs[i]
            if isbytes(d):
                d = bytes_to_native_str(d)
            try:
                col = ic.findObjectFactory(t).create(t)
                col.name = n
                col.description = d
                col.setsize(size)
                col.settable(self.__mea)
                cols.append(col)
            except:
                msg = traceback.format_exc()
                raise core.ValidationException(
                    None, msg, "BAD COLUMN TYPE: %s for %s" % (t, n))
        return cols

    @locked
    def get_meta_map(self):
        self.__initcheck()
        metadata = {}
        attr = self.__mea.attrs
        keys = list(self.__mea.attrs._v_attrnamesuser)
        for key in keys:
            val = attr[key]
            if isinstance(val, float):
                val = rfloat(val)
            elif isinstance(val, TABLES_METADATA_INT_TYPES):
                val = rlong(val)
            elif isinstance(val, basestring):
                if isbytes(val):
                    val = bytes_to_native_str(val)
                val = rstring(val)
            else:
                raise core.ValidationException("BAD TYPE: %s" % type(val))
            metadata[key] = val
        return metadata

    @locked
    @modifies
    def add_meta_map(self, m, replace=False, init=False):
        if not init:
            if int(self.__getversion()) < 2:
                # Metadata methods were generally broken for v1 tables so
                # the introduction of internal metadata attributes is unlikely
                # to affect anyone.
                msg = 'Tables metadata is only supported for ODE.tables >= 2'
                self.logger.error(msg)
                raise core.ApiUsageException(None, None, msg)

            self.__initcheck()
            for k, v in m.items():
                if internal_attr(k):
                    raise core.ApiUsageException(
                        None, None, "Reserved attribute name: %s" % k)
                if not isinstance(v, (
                        core.RString, core.RLong, core.RInt, core.RFloat)):
                    raise core.ValidationException(
                        "Unsupported type: %s" % type(v))

        attr = self.__mea.attrs
        if replace:
            for f in list(attr._v_attrnamesuser):
                if init or not internal_attr(f):
                    del attr[f]
        if not m:
            return

        for k, v in m.items():
            # This uses the default pytables type conversion, which may
            # convert it to a numpy type or keep it as a native Python type
            attr[k] = unwrap(v)

    @locked
    @modifies
    def append(self, cols):
        self.__initcheck()
        # Optimize!
        arrays = []
        dtypes = []
        sz = None
        for col in cols:
            if sz is None:
                sz = col.getsize()
            else:
                if sz != col.getsize():
                    raise core.ValidationException(
                        "Columns are of differing length")
            arrays.extend(col.arrays())
            dtypes.extend(col.dtypes())
            col.append(self.__mea)  # Potential corruption !!!

        # Convert column-wise data to row-wise records
        records = numpy.array(list(zip(*arrays)), dtype=dtypes)

        self.__mea.append(records)

    #
    # Stamped methods
    #

    @stamped
    @modifies
    def update(self, stamp, data):
        self.__initcheck()
        if data:
            for i, rn in enumerate(data.rowNumbers):
                for col in data.columns:
                    getattr(self.__mea.cols, col.name)[rn] = col.values[i]

    @stamped
    def getWhereList(self, stamp, condition, variables, unused,
                     start, stop, step):
        self.__initcheck()
        try:
            condvars = variables
            if variables:
                for key, value in condvars.items():
                    if isinstance(value, str):
                        condvars[key] = getattr(self.__mea.cols, value)
            return self.__mea.get_where_list(condition, condvars, None,
                                             start, stop, step).tolist()
        except (NameError, SyntaxError, TypeError, ValueError) as err:
            aue = core.ApiUsageException()
            aue.message = "Bad condition: %s, %s" % (condition, variables)
            aue.serverStackTrace = "".join(traceback.format_exc())
            aue.serverExceptionClass = str(err.__class__.__name__)
            raise aue

    def _as_data(self, cols, rowNumbers):
        """
        Constructs a core.grid.Data object for returning to the client.
        """
        data = core.grid.Data()
        data.columns = cols
        data.rowNumbers = rowNumbers
        # Convert to millis since epoch
        data.lastModification = int(self._stamp * 1000)
        return data

    @stamped
    def readCoordinates(self, stamp, rowNumbers, current):
        self.__initcheck()
        self.__sizecheck(None, rowNumbers)
        cols = self.cols(None, current)
        for col in cols:
            col.readCoordinates(self.__mea, rowNumbers)
        return self._as_data(cols, rowNumbers)

    @stamped
    def read(self, stamp, colNumbers, start, stop, current):
        self.__initcheck()
        self.__sizecheck(colNumbers, None)
        all_cols = self.cols(None, current)
        cols = [all_cols[i] for i in colNumbers]

        for col in cols:
            col.read(self.__mea, start, stop)
        if start is not None and stop is not None:
            rowNumbers = list(range(start, stop))
        elif start is not None and stop is None:
            rowNumbers =  list(range(start, self.__length()))
        elif start is None and stop is None:
            rowNumbers = list(range(self.__length()))

        return self._as_data(cols, rowNumbers)

    @stamped
    def slice(self, stamp, colNumbers, rowNumbers, current):
        self.__initcheck()

        if colNumbers is None or len(colNumbers) == 0:
            colNumbers = list(range(self.__width()))
        if rowNumbers is None or len(rowNumbers) == 0:
            rowNumbers = list(range(self.__length()))

        self.__sizecheck(colNumbers, rowNumbers)
        cols = self.cols(None, current)
        rv = []
        for i in colNumbers:
            col = cols[i]
            col.readCoordinates(self.__mea, rowNumbers)
            rv.append(col)
        return self._as_data(rv, rowNumbers)

    #
    # Lifecycle methods
    #

    def check(self):
        return True

    @locked
    def cleanup(self):
        self.logger.info("Cleaning storage: %s", self.__hdf_path)
        if self.__mea:
            self.__mea = None
        if self.__ode:
            self.__ode = None
        if self.__hdf_file:
            HDFLIST.remove(self.__hdf_path, self.__hdf_file)
        hdffile = self.__hdf_file
        self.__hdf_file = None
        hdffile.close()  # Resources freed

# End class HdfStorage