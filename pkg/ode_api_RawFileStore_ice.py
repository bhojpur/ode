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

from sys import version_info as _version_info_
import Ice, IcePy
import ode_ModelF_ice
import ode_ServicesF_ice
import ode_Collections_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'RawFileStore' not in _M_ode.api.__dict__:
    _M_ode.api.RawFileStore = Ice.createTempClass()
    class RawFileStore(_M_ode.api.StatefulServiceInterface):
        """
        Raw file gateway which provides access to the ode file repository.
        Note: methods on this service are protected by a DOWNLOAD
        restriction.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.RawFileStore:
                raise RuntimeError('ode.api.RawFileStore is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::RawFileStore', '::ode::api::ServiceInterface', '::ode::api::StatefulServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::RawFileStore'

        def ice_staticId():
            return '::ode::api::RawFileStore'
        ice_staticId = staticmethod(ice_staticId)

        def setFileId_async(self, _cb, fileId, current=None):
            """
            This method manages the state of the service. This method
            will throw a ode.SecurityViolation if for the
            current user context either the file is not readable or a
            {@code ode.constants.permissions.BINARYACCESS} restriction is
            in place.
            Arguments:
            _cb -- The asynchronous callback object.
            fileId -- 
            current -- The Current object for the invocation.
            """
            pass

        def getFileId_async(self, _cb, current=None):
            """
            Returns the current file id or null if none has been set.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def read_async(self, _cb, position, length, current=None):
            """
            Reads length bytes of data at the
            position from the raw file into an array of
            bytes.
            Arguments:
            _cb -- The asynchronous callback object.
            position -- 
            length -- 
            current -- The Current object for the invocation.
            """
            pass

        def size_async(self, _cb, current=None):
            """
            Returns the size of the file on disk (not as stored in the
            database since that value will only be updated on
            {@code save}. If the file has not yet been written to, and
            therefore does not exist, a ode.ResourceError
            will be thrown.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def truncate_async(self, _cb, length, current=None):
            """
            Limits the size of a file to the given length. If the file
            is already shorter than length, no action is taken in which
            case false is returned.
            Arguments:
            _cb -- The asynchronous callback object.
            length -- 
            current -- The Current object for the invocation.
            """
            pass

        def write_async(self, _cb, buf, position, length, current=None):
            """
            Writes length bytes of data from the specified
            buf byte array starting at at
            position to the raw file.
            Arguments:
            _cb -- The asynchronous callback object.
            buf -- 
            position -- 
            length -- 
            current -- The Current object for the invocation.
            """
            pass

        def exists_async(self, _cb, current=None):
            """
            Checks to see if a raw file exists with the file ID that
            the service was initialized with.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            ResourceError -- if there is a problem accessing the file due to permissions errors within the repository or any other I/O error.
            """
            pass

        def save_async(self, _cb, current=None):
            """
            Saves the ode.model.OriginalFile associated with
            the service if it has been modified. The returned valued
            should replace all instances of the
            ode.model.OriginalFile in the client.
            If save has not been called, ode.api.RawFileStore
            instances will save the ode.model.OriginalFile
            object associated with it on {@code close}.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_RawFileStore)

        __repr__ = __str__

    _M_ode.api.RawFileStorePrx = Ice.createTempClass()
    class RawFileStorePrx(_M_ode.api.StatefulServiceInterfacePrx):

        """
        This method manages the state of the service. This method
        will throw a ode.SecurityViolation if for the
        current user context either the file is not readable or a
        {@code ode.constants.permissions.BINARYACCESS} restriction is
        in place.
        Arguments:
        fileId -- 
        _ctx -- The request context for the invocation.
        """
        def setFileId(self, fileId, _ctx=None):
            return _M_ode.api.RawFileStore._op_setFileId.invoke(self, ((fileId, ), _ctx))

        """
        This method manages the state of the service. This method
        will throw a ode.SecurityViolation if for the
        current user context either the file is not readable or a
        {@code ode.constants.permissions.BINARYACCESS} restriction is
        in place.
        Arguments:
        fileId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setFileId(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawFileStore._op_setFileId.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        This method manages the state of the service. This method
        will throw a ode.SecurityViolation if for the
        current user context either the file is not readable or a
        {@code ode.constants.permissions.BINARYACCESS} restriction is
        in place.
        Arguments:
        fileId -- 
        """
        def end_setFileId(self, _r):
            return _M_ode.api.RawFileStore._op_setFileId.end(self, _r)

        """
        Returns the current file id or null if none has been set.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getFileId(self, _ctx=None):
            return _M_ode.api.RawFileStore._op_getFileId.invoke(self, ((), _ctx))

        """
        Returns the current file id or null if none has been set.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getFileId(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawFileStore._op_getFileId.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current file id or null if none has been set.
        Arguments:
        """
        def end_getFileId(self, _r):
            return _M_ode.api.RawFileStore._op_getFileId.end(self, _r)

        """
        Reads length bytes of data at the
        position from the raw file into an array of
        bytes.
        Arguments:
        position -- 
        length -- 
        _ctx -- The request context for the invocation.
        """
        def read(self, position, length, _ctx=None):
            return _M_ode.api.RawFileStore._op_read.invoke(self, ((position, length), _ctx))

        """
        Reads length bytes of data at the
        position from the raw file into an array of
        bytes.
        Arguments:
        position -- 
        length -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_read(self, position, length, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawFileStore._op_read.begin(self, ((position, length), _response, _ex, _sent, _ctx))

        """
        Reads length bytes of data at the
        position from the raw file into an array of
        bytes.
        Arguments:
        position -- 
        length -- 
        """
        def end_read(self, _r):
            return _M_ode.api.RawFileStore._op_read.end(self, _r)

        """
        Returns the size of the file on disk (not as stored in the
        database since that value will only be updated on
        {@code save}. If the file has not yet been written to, and
        therefore does not exist, a ode.ResourceError
        will be thrown.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def size(self, _ctx=None):
            return _M_ode.api.RawFileStore._op_size.invoke(self, ((), _ctx))

        """
        Returns the size of the file on disk (not as stored in the
        database since that value will only be updated on
        {@code save}. If the file has not yet been written to, and
        therefore does not exist, a ode.ResourceError
        will be thrown.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_size(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawFileStore._op_size.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the size of the file on disk (not as stored in the
        database since that value will only be updated on
        {@code save}. If the file has not yet been written to, and
        therefore does not exist, a ode.ResourceError
        will be thrown.
        Arguments:
        """
        def end_size(self, _r):
            return _M_ode.api.RawFileStore._op_size.end(self, _r)

        """
        Limits the size of a file to the given length. If the file
        is already shorter than length, no action is taken in which
        case false is returned.
        Arguments:
        length -- 
        _ctx -- The request context for the invocation.
        """
        def truncate(self, length, _ctx=None):
            return _M_ode.api.RawFileStore._op_truncate.invoke(self, ((length, ), _ctx))

        """
        Limits the size of a file to the given length. If the file
        is already shorter than length, no action is taken in which
        case false is returned.
        Arguments:
        length -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_truncate(self, length, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawFileStore._op_truncate.begin(self, ((length, ), _response, _ex, _sent, _ctx))

        """
        Limits the size of a file to the given length. If the file
        is already shorter than length, no action is taken in which
        case false is returned.
        Arguments:
        length -- 
        """
        def end_truncate(self, _r):
            return _M_ode.api.RawFileStore._op_truncate.end(self, _r)

        """
        Writes length bytes of data from the specified
        buf byte array starting at at
        position to the raw file.
        Arguments:
        buf -- 
        position -- 
        length -- 
        _ctx -- The request context for the invocation.
        """
        def write(self, buf, position, length, _ctx=None):
            return _M_ode.api.RawFileStore._op_write.invoke(self, ((buf, position, length), _ctx))

        """
        Writes length bytes of data from the specified
        buf byte array starting at at
        position to the raw file.
        Arguments:
        buf -- 
        position -- 
        length -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_write(self, buf, position, length, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawFileStore._op_write.begin(self, ((buf, position, length), _response, _ex, _sent, _ctx))

        """
        Writes length bytes of data from the specified
        buf byte array starting at at
        position to the raw file.
        Arguments:
        buf -- 
        position -- 
        length -- 
        """
        def end_write(self, _r):
            return _M_ode.api.RawFileStore._op_write.end(self, _r)

        """
        Checks to see if a raw file exists with the file ID that
        the service was initialized with.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: true if there is an accessible file within the original file repository with the correct ID. Otherwise false.
        Throws:
        ResourceError -- if there is a problem accessing the file due to permissions errors within the repository or any other I/O error.
        """
        def exists(self, _ctx=None):
            return _M_ode.api.RawFileStore._op_exists.invoke(self, ((), _ctx))

        """
        Checks to see if a raw file exists with the file ID that
        the service was initialized with.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_exists(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawFileStore._op_exists.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Checks to see if a raw file exists with the file ID that
        the service was initialized with.
        Arguments:
        Returns: true if there is an accessible file within the original file repository with the correct ID. Otherwise false.
        Throws:
        ResourceError -- if there is a problem accessing the file due to permissions errors within the repository or any other I/O error.
        """
        def end_exists(self, _r):
            return _M_ode.api.RawFileStore._op_exists.end(self, _r)

        """
        Saves the ode.model.OriginalFile associated with
        the service if it has been modified. The returned valued
        should replace all instances of the
        ode.model.OriginalFile in the client.
        If save has not been called, ode.api.RawFileStore
        instances will save the ode.model.OriginalFile
        object associated with it on {@code close}.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def save(self, _ctx=None):
            return _M_ode.api.RawFileStore._op_save.invoke(self, ((), _ctx))

        """
        Saves the ode.model.OriginalFile associated with
        the service if it has been modified. The returned valued
        should replace all instances of the
        ode.model.OriginalFile in the client.
        If save has not been called, ode.api.RawFileStore
        instances will save the ode.model.OriginalFile
        object associated with it on {@code close}.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_save(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawFileStore._op_save.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Saves the ode.model.OriginalFile associated with
        the service if it has been modified. The returned valued
        should replace all instances of the
        ode.model.OriginalFile in the client.
        If save has not been called, ode.api.RawFileStore
        instances will save the ode.model.OriginalFile
        object associated with it on {@code close}.
        Arguments:
        """
        def end_save(self, _r):
            return _M_ode.api.RawFileStore._op_save.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.RawFileStorePrx.ice_checkedCast(proxy, '::ode::api::RawFileStore', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.RawFileStorePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::RawFileStore'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_RawFileStorePrx = IcePy.defineProxy('::ode::api::RawFileStore', RawFileStorePrx)

    _M_ode.api._t_RawFileStore = IcePy.defineClass('::ode::api::RawFileStore', RawFileStore, -1, (), True, False, None, (_M_ode.api._t_StatefulServiceInterface,), ())
    RawFileStore._ice_type = _M_ode.api._t_RawFileStore

    RawFileStore._op_setFileId = IcePy.Operation('setFileId', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    RawFileStore._op_getFileId = IcePy.Operation('getFileId', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode._t_RLong, False, 0), (_M_ode._t_ServerError,))
    RawFileStore._op_read = IcePy.Operation('read', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawFileStore._op_size = IcePy.Operation('size', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RawFileStore._op_truncate = IcePy.Operation('truncate', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    RawFileStore._op_write = IcePy.Operation('write', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_Ice._t_ByteSeq, False, 0), ((), IcePy._t_long, False, 0), ((), IcePy._t_int, False, 0)), (), None, (_M_ode._t_ServerError,))
    RawFileStore._op_exists = IcePy.Operation('exists', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    RawFileStore._op_save = IcePy.Operation('save', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.model._t_OriginalFile, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.RawFileStore = RawFileStore
    del RawFileStore

    _M_ode.api.RawFileStorePrx = RawFileStorePrx
    del RawFileStorePrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
