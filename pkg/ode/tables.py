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

# Bhojpur ODE Tables Interface

from __future__ import division
from builtins import str
from builtins import range
from future.utils import native_str
from past.utils import old_div
import Ice
import time
import traceback

import ode  # Do we need both??
import ode.clients
import ode.callbacks
from extras.path import path

# For ease of use
from ode import LockTimeout
from ode.rtypes import rstring
from ode.rtypes import unwrap
from ode.util.decorators import remoted, perf

sys = __import__("sys")  # Python sys
tables = __import__("tables")  # Pytables

VERSION = '2'
RETRIES = 20

def slen(rv):
    """
    Returns the length of the argument or None
    if the argument is None
    """
    if rv is None:
        return None
    return len(rv)

class TableI(ode.grid.Table, ode.util.SimpleServant):

    """
    Spreadsheet implementation based on pytables.
    """

    def __init__(self, ctx, file_obj, file_path, factory, storage_factory, read_only=False, uuid="unknown",
                 call_context=None, adapter=None):
        self.id = Ice.Identity()
        self.id.name = uuid
        self.uuid = uuid
        self.file_obj = file_obj
        self.factory = factory
        self.storage = storage_factory.getOrCreate(file_path, self, read_only)
        self.call_context = call_context
        self.adapter = adapter
        self.can_write = factory.getAdminService().canUpdate(
            file_obj, call_context)
        ode.util.SimpleServant.__init__(self, ctx)

        self.stamp = time.time()
        self._closed = False

        if (not self.file_obj.isLoaded() or
                self.file_obj.getDetails() is None or
                self.file_obj.details.group is None):
            self.file_obj = self.ctx.getSession().getQueryService().get(
                'ode.model.OriginalFileI', unwrap(file_obj.id),
                {"ode.group": "-1"})

    def assert_write(self):
        """
        Checks that the current user can write to the given object
        at the database level. If not, no FS level writes are permitted
        either.

        ticket:2910
        """
        if not self.can_write:
            raise ode.SecurityViolation(
                "Current user cannot write to file %s" % self.file_obj.id.val)

    def check(self):
        """
        Called periodically to check the resource is alive. Returns
        False if this resource can be cleaned up. (Resources API)
        """
        self.logger.debug("Checking %s" % self)
        if self._closed:
            return False

        idname = 'UNKNOWN'
        try:
            # Quietly loading the session will mean that the last access
            # time will not be incremented so that dangling files can be
            # cleaned up. Note: this is different that the strategy of a
            # script which *wants* to keep its session alive.
            idname = self.factory.ice_getIdentity().name
            clientSession = self.ctx.getSession().getSessionService() \
                .getSession(idname, {"quietly": "true"})
            if clientSession.getClosed():
                self.logger.debug("Client session closed: %s" % idname)
                return False
            return True
        except Exception:
            self.logger.debug("Client session not found: %s" % idname)
            return False

    def cleanup(self):
        """
        Decrements the counter on the held storage to allow it to
        be cleaned up. Returns the current file-size.
        """
        if self.storage:
            try:
                self.storage.decr(self)
                return self.storage.size()
            finally:
                self.storage = None

    def __str__(self):
        return "Table-%s" % self.uuid

    @remoted
    @perf
    def close(self, current=None):
        try:
            self.__close_file()
        finally:
            if self.adapter is not None:
                self.adapter.remove(self.id)
            self.adapter = None

    def __close_file(self):
        if self._closed:
            self.logger.warn(
                "File object %d already closed",
                unwrap(self.file_obj.id) if self.file_obj else None)
            return

        modified = self.storage.modified()

        try:
            size = self.cleanup()
            self.logger.info("Closed %s", self)
        except:
            self.logger.warn("Closed %s with errors", self)

        self._closed = True

        fid = unwrap(self.file_obj.id)

        if self.file_obj is not None and self.can_write and modified:
            gid = unwrap(self.file_obj.details.group.id)
            client_uuid = self.factory.ice_getIdentity().category[8:]
            ctx = {
                "ode.group": native_str(gid),
                ode.constants.CLIENTUUID: client_uuid}
            try:
                # Size to reset the server object to (must be checked after
                # the underlying HDF file has been closed)
                rfs = self.factory.createRawFileStore(ctx)
                try:
                    rfs.setFileId(fid, ctx)
                    if size:
                        rfs.truncate(size, ctx)     # May do nothing
                        rfs.write([], size, 0, ctx)  # Force an update
                    else:
                        rfs.write([], 0, 0, ctx)    # No-op
                    file_obj = rfs.save(ctx)
                finally:
                    rfs.close(ctx)
                self.logger.info(
                    "Updated file object %s to hash=%s (%s bytes)",
                    fid, unwrap(file_obj.hash), unwrap(file_obj.size))
            except:
                self.logger.warn("Failed to update file object %s",
                                 fid, exc_info=1)
        else:
            self.logger.info("File object %s not updated", fid)

    # TABLES READ API ============================

    @remoted
    @perf
    def getOriginalFile(self, current=None):
        msg = "unknown"
        if self.file_obj:
            if self.file_obj.id:
                msg = self.file_obj.id.val
        self.logger.info("%s.getOriginalFile() => id=%s", self, msg)
        return self.file_obj

    @remoted
    @perf
    def getHeaders(self, current=None):
        rv = self.storage.cols(None, current)
        self.logger.info("%s.getHeaders() => size=%s", self, slen(rv))
        return rv

    @remoted
    @perf
    def getNumberOfRows(self, current=None):
        rv = self.storage.rows()
        self.logger.info("%s.getNumberOfRows() => %s", self, rv)
        return int(rv)

    @remoted
    @perf
    def getWhereList(self, condition, variables,
                     start, stop, step, current=None):
        variables = unwrap(variables)
        if stop == 0:
            stop = None
        if step == 0:
            step = None
        rv = self.storage.getWhereList(
            self.stamp, condition, variables, None, start, stop, step)
        self.logger.info("%s.getWhereList(%s, %s, %s, %s, %s) => size=%s",
                         self, condition, variables,
                         start, stop, step, slen(rv))
        return rv

    @remoted
    @perf
    def readCoordinates(self, rowNumbers, current=None):
        self.logger.info("%s.readCoordinates(size=%s)", self, slen(rowNumbers))
        try:
            return self.storage.readCoordinates(self.stamp, rowNumbers,
                                                current)
        except tables.HDF5ExtError as err:
            aue = ode.ApiUsageException()
            aue.message = "Error reading coordinates. Most likely out of range"
            aue.serverStackTrace = "".join(traceback.format_exc())
            aue.serverExceptionClass = str(err.__class__.__name__)
            raise aue

    @remoted
    @perf
    def read(self, colNumbers, start, stop, current=None):
        self.logger.info("%s.read(%s, %s, %s)", self, colNumbers, start, stop)
        if start == 0 and stop == 0:
            stop = None
        try:
            return self.storage.read(self.stamp, colNumbers,
                                     start, stop, current)
        except tables.HDF5ExtError as err:
            aue = ode.ApiUsageException()
            aue.message = "Error reading coordinates. Most likely out of range"
            aue.serverStackTrace = "".join(traceback.format_exc())
            aue.serverExceptionClass = str(err.__class__.__name__)
            raise aue

    @remoted
    @perf
    def slice(self, colNumbers, rowNumbers, current=None):
        self.logger.info(
            "%s.slice(size=%s, size=%s)", self,
            slen(colNumbers), slen(rowNumbers))
        return self.storage.slice(self.stamp, colNumbers, rowNumbers, current)

    # TABLES WRITE API ===========================

    @remoted
    @perf
    def initialize(self, cols, current=None):
        self.assert_write()
        self.storage.initialize(cols)
        if cols:
            self.logger.info("Initialized %s with %s col(s)", self, slen(cols))

    @remoted
    @perf
    def addColumn(self, col, current=None):
        self.assert_write()
        raise ode.ApiUsageException(None, None, "NYI")

    @remoted
    @perf
    def addData(self, cols, current=None):
        self.assert_write()
        self.storage.append(cols)
        if cols and cols[0] and cols[0].getsize():
            self.logger.info(
                "Added %s row(s) of data to %s", cols[0].getsize(), self)

    @remoted
    @perf
    def update(self, data, current=None):
        self.assert_write()
        if data:
            self.storage.update(self.stamp, data)
            self.logger.info(
                "Updated %s row(s) of data to %s", slen(data.rowNumbers), self)

    @remoted
    @perf
    def delete(self, current=None):
        self.assert_write()
        self.close()
        dc = ode.cmd.Delete2(
            targetObjects={"OriginalFile": [self.file_obj.id.val]}
        )
        handle = self.factory.submit(dc)
        # Copied from clients.py since none is available
        try:
            callback = ode.callbacks.CmdCallbackI(
                current.adapter, handle, "Fake")
        except:
            # Since the callback won't escape this method,
            # close the handle if requested.
            handle.close()
            raise

        try:
            try:
                callback.loop(20, 500)
            except LockTimeout:
                raise ode.InternalException(None, None, "delete timed-out")

            rsp = callback.getResponse()
            if isinstance(rsp, ode.cmd.ERR):
                raise ode.InternalException(None, None, str(rsp))
        finally:
            callback.close(True)

    # TABLES METADATA API ===========================

    @remoted
    @perf
    def getMetadata(self, key, current=None):
        rv = self.storage.get_meta_map()
        rv = rv.get(key)
        self.logger.info("%s.getMetadata() => %s", self, unwrap(rv))
        return rv

    @remoted
    @perf
    def getAllMetadata(self, current=None):
        rv = self.storage.get_meta_map()
        self.logger.info("%s.getMetadata() => size=%s", self, slen(rv))
        return rv

    @remoted
    @perf
    def setMetadata(self, key, value, current=None):
        self.assert_write()
        self.storage.add_meta_map({key: value})
        self.logger.info("%s.setMetadata() => %s=%s", self, key, unwrap(value))

    @remoted
    @perf
    def setAllMetadata(self, value, current=None):
        self.assert_write()
        self.storage.add_meta_map(value, replace=True)
        self.logger.info("%s.setMetadata() => number=%s", self, slen(value))

    # Column methods missing

class TablesI(ode.grid.Tables, ode.util.Servant):

    """
    Implementation of the ode.grid.Tables API. Provides
    spreadsheet like functionality across the ODE.grid.
    This servant serves as a session-less, user-less
    resource for obtaining ode.grid.Table proxies.

    The first major step in initialization is getting
    a session. This will block until the Bhojpur ODe server
    is reachable.
    """

    def __init__(
            self, ctx,
            table_cast=ode.grid.TablePrx.uncheckedCast,
            internal_repo_cast=ode.grid.InternalRepositoryPrx.checkedCast,
            storage_factory=None,
            retries=None):

        ode.util.Servant.__init__(self, ctx, needs_session=True)

        # Storing these methods, mainly to allow overriding via
        # test methods. Static methods are evil.
        self._table_cast = table_cast
        self._internal_repo_cast = internal_repo_cast

        self.__stores = []

        if storage_factory is None:
            from ode.hdfstorageV2 import HDFLIST
            self._storage_factory = HDFLIST
        else:
            self._storage_factory = storage_factory
        self.logger.info("Using storage factory: %s.%s",
                         str(self._storage_factory.__module__),
                         self._storage_factory.__class__.__name__)

        self.repo_cfg = None
        self.repo_mgr = None
        self.repo_obj = None
        self.repo_svc = None
        self.repo_uuid = None

        try:
            config_service = ctx.getSession().getConfigService()
            prefix = "ode.cluster.read_only.runtime"
            self.read_only = "true" in [config_service.getConfigValue(
                "{}.{}".format(prefix, suffix)) for suffix in ["db", "repo"]]
        except:
            self.read_only = False

        if self.read_only:
            self.logger.info("Starting in read-only mode.")

        if retries is None:
            retries = RETRIES

        wait = float(self.communicator.getProperties().getPropertyWithDefault(
            "ode.repo.wait", "1"))
        per_loop = old_div(wait, retries)

        exc = None
        for x in range(retries):
            try:
                self._get_dir()
                self._get_uuid()
                self._get_repo()
            except Exception as e:
                self.logger.warn("Failed to find repo_svc: %s" % e)
                exc = e

            if self.repo_svc:
                break
            else:
                msg = "waiting %ss (%s of %s)" % (per_loop, x+1, retries)
                self.logger.debug(msg)
                self.stop_event.wait(per_loop)

        if exc:
            raise exc

    def _get_dir(self):
        """
        Second step in initialization is to find the .ode/repository
        directory. If this is not created, then a required server has
        not started, and so this instance will not start.
        """
        self.repo_dir = self.communicator.getProperties().getProperty(
            "ode.repo.dir")

        if not self.repo_dir:
            # Implies this is the legacy directory. Obtain from server
            self.repo_dir = self.ctx.getSession(
                ).getConfigService().getConfigValue("ode.data.dir")

        self.repo_cfg = path(self.repo_dir) / ".ode" / "repository"
        if not self.repo_cfg.exists():
            msg = "No repository found: %s" % self.repo_cfg
            raise ode.ResourceError(None, None, msg)

    def _get_uuid(self):
        """
        Third step in initialization is to find the database uuid
        for this grid instance. Multiple ODE.grids could be watching
        the same directory.
        """
        cfg = self.ctx.getSession().getConfigService()
        self.db_uuid = cfg.getDatabaseUuid()
        self.instance = old_div(self.repo_cfg, self.db_uuid)

    def _get_repo(self):
        """
        Fourth step in initialization is to find the repository object
        for the UUID found in .ode/repository/<db_uuid>, and then
        create a proxy for the InternalRepository attached to that.
        """

        uuidfile = old_div(self.instance, "repo_uuid")
        if not uuidfile.exists():
            msg = "%s doesn't exist" % uuidfile
            raise IOError(msg)

        # Get and parse the uuid from the RandomAccessFile format from
        # FileMaker
        self.repo_uuid = uuidfile.lines()[0].strip()
        if len(self.repo_uuid) != 38:
            raise ode.ResourceError(
                "Poorly formed UUID: %s" % self.repo_uuid)
        self.repo_uuid = self.repo_uuid[2:]

        # Using the repo_uuid, find our OriginalFile object
        self.repo_obj = self.ctx.getSession().getQueryService().findByQuery(
            "select f from OriginalFile f where hash = :uuid",
            ode.sys.ParametersI().add("uuid", rstring(self.repo_uuid)))
        self.repo_mgr = self.communicator.stringToProxy(
            "InternalRepository-%s" % self.repo_uuid)
        self.repo_mgr = self._internal_repo_cast(self.repo_mgr)
        self.repo_svc = self.repo_mgr.getProxy()

    @remoted
    def getRepository(self, current=None):
        """
        Returns the Repository object for this Tables server.
        """
        return self.repo_svc

    @remoted
    @perf
    def getTable(self, file_obj, factory, current=None):
        """
        Create and/or register a table servant.
        """

        # Will throw an exception if not allowed.
        file_id = None
        if file_obj is not None and file_obj.id is not None:
            file_id = file_obj.id.val
        self.logger.info("getTable: %s %s", file_id, current.ctx)

        file_path = self.repo_mgr.getFilePath(file_obj)
        p = path(file_path).dirname()
        if not p.exists():
            p.makedirs()

        table = TableI(self.ctx, file_obj,file_path,
                       factory,
                       self._storage_factory,
                       read_only=self.read_only,
                       uuid=Ice.generateUUID(),
                       call_context=current.ctx,
                       adapter=current.adapter)
        self.resources.add(table)
        prx = current.adapter.add(table, table.id)
        return self._table_cast(prx)