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
import ode_System_ice
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

if 'IRepositoryInfo' not in _M_ode.api.__dict__:
    _M_ode.api.IRepositoryInfo = Ice.createTempClass()
    class IRepositoryInfo(_M_ode.api.ServiceInterface):
        """
        Provides methods for obtaining information for server repository
        disk space allocation. Could be used generically to obtain usage
        information for any mount point, however, this interface is
        prepared for the API to provide methods to obtain usage info for
        the server filesystem containing the image uploads. For the ode
        server base this is /ODE. For this implementation it could be
        anything e.g. /Data1.
        Methods that fail or cannot execute on the server will throw an
        InternalException. This would not be normal and would indicate some
        server or disk failure.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IRepositoryInfo:
                raise RuntimeError('ode.api.IRepositoryInfo is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IRepositoryInfo', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IRepositoryInfo'

        def ice_staticId():
            return '::ode::api::IRepositoryInfo'
        ice_staticId = staticmethod(ice_staticId)

        def getUsedSpaceInKilobytes_async(self, _cb, current=None):
            """
            Returns the total space in bytes for this file system
            including nested subdirectories.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            ResourceError -- If there is a problem retrieving disk space used.
            """
            pass

        def getFreeSpaceInKilobytes_async(self, _cb, current=None):
            """
            Returns the free or available space on this file system
            including nested subdirectories.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            ResourceError -- If there is a problem retrieving disk space free.
            """
            pass

        def getUsageFraction_async(self, _cb, current=None):
            """
            Returns a double of the used space divided by the free
            space.
            This method will be called by a client to watch the
            repository filesystem so that it doesn't exceed 95% full.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            ResourceError -- If there is a problem calculating the usage fraction.
            """
            pass

        def sanityCheckRepository_async(self, _cb, current=None):
            """
            Checks that image data repository has not exceeded 95% disk
            space use level.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            InternalException -- If there is a critical failure while sanity checking the repository.
            ResourceError -- If the repository usage has exceeded 95%.
            """
            pass

        def removeUnusedFiles_async(self, _cb, current=None):
            """
            Removes all files from the server that do not have an
            OriginalFile complement in the database, all the Pixels
            that do not have a complement in the database and all the
            Thumbnail's that do not have a complement in the database.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            ResourceError -- If deletion fails.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IRepositoryInfo)

        __repr__ = __str__

    _M_ode.api.IRepositoryInfoPrx = Ice.createTempClass()
    class IRepositoryInfoPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Returns the total space in bytes for this file system
        including nested subdirectories.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: Total space used on this file system.
        Throws:
        ResourceError -- If there is a problem retrieving disk space used.
        """
        def getUsedSpaceInKilobytes(self, _ctx=None):
            return _M_ode.api.IRepositoryInfo._op_getUsedSpaceInKilobytes.invoke(self, ((), _ctx))

        """
        Returns the total space in bytes for this file system
        including nested subdirectories.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getUsedSpaceInKilobytes(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRepositoryInfo._op_getUsedSpaceInKilobytes.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the total space in bytes for this file system
        including nested subdirectories.
        Arguments:
        Returns: Total space used on this file system.
        Throws:
        ResourceError -- If there is a problem retrieving disk space used.
        """
        def end_getUsedSpaceInKilobytes(self, _r):
            return _M_ode.api.IRepositoryInfo._op_getUsedSpaceInKilobytes.end(self, _r)

        """
        Returns the free or available space on this file system
        including nested subdirectories.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: Free space on this file system in KB.
        Throws:
        ResourceError -- If there is a problem retrieving disk space free.
        """
        def getFreeSpaceInKilobytes(self, _ctx=None):
            return _M_ode.api.IRepositoryInfo._op_getFreeSpaceInKilobytes.invoke(self, ((), _ctx))

        """
        Returns the free or available space on this file system
        including nested subdirectories.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getFreeSpaceInKilobytes(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRepositoryInfo._op_getFreeSpaceInKilobytes.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the free or available space on this file system
        including nested subdirectories.
        Arguments:
        Returns: Free space on this file system in KB.
        Throws:
        ResourceError -- If there is a problem retrieving disk space free.
        """
        def end_getFreeSpaceInKilobytes(self, _r):
            return _M_ode.api.IRepositoryInfo._op_getFreeSpaceInKilobytes.end(self, _r)

        """
        Returns a double of the used space divided by the free
        space.
        This method will be called by a client to watch the
        repository filesystem so that it doesn't exceed 95% full.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: Fraction of used/free.
        Throws:
        ResourceError -- If there is a problem calculating the usage fraction.
        """
        def getUsageFraction(self, _ctx=None):
            return _M_ode.api.IRepositoryInfo._op_getUsageFraction.invoke(self, ((), _ctx))

        """
        Returns a double of the used space divided by the free
        space.
        This method will be called by a client to watch the
        repository filesystem so that it doesn't exceed 95% full.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getUsageFraction(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRepositoryInfo._op_getUsageFraction.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns a double of the used space divided by the free
        space.
        This method will be called by a client to watch the
        repository filesystem so that it doesn't exceed 95% full.
        Arguments:
        Returns: Fraction of used/free.
        Throws:
        ResourceError -- If there is a problem calculating the usage fraction.
        """
        def end_getUsageFraction(self, _r):
            return _M_ode.api.IRepositoryInfo._op_getUsageFraction.end(self, _r)

        """
        Checks that image data repository has not exceeded 95% disk
        space use level.
        Arguments:
        _ctx -- The request context for the invocation.
        Throws:
        InternalException -- If there is a critical failure while sanity checking the repository.
        ResourceError -- If the repository usage has exceeded 95%.
        """
        def sanityCheckRepository(self, _ctx=None):
            return _M_ode.api.IRepositoryInfo._op_sanityCheckRepository.invoke(self, ((), _ctx))

        """
        Checks that image data repository has not exceeded 95% disk
        space use level.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_sanityCheckRepository(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRepositoryInfo._op_sanityCheckRepository.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Checks that image data repository has not exceeded 95% disk
        space use level.
        Arguments:
        Throws:
        InternalException -- If there is a critical failure while sanity checking the repository.
        ResourceError -- If the repository usage has exceeded 95%.
        """
        def end_sanityCheckRepository(self, _r):
            return _M_ode.api.IRepositoryInfo._op_sanityCheckRepository.end(self, _r)

        """
        Removes all files from the server that do not have an
        OriginalFile complement in the database, all the Pixels
        that do not have a complement in the database and all the
        Thumbnail's that do not have a complement in the database.
        Arguments:
        _ctx -- The request context for the invocation.
        Throws:
        ResourceError -- If deletion fails.
        """
        def removeUnusedFiles(self, _ctx=None):
            return _M_ode.api.IRepositoryInfo._op_removeUnusedFiles.invoke(self, ((), _ctx))

        """
        Removes all files from the server that do not have an
        OriginalFile complement in the database, all the Pixels
        that do not have a complement in the database and all the
        Thumbnail's that do not have a complement in the database.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeUnusedFiles(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRepositoryInfo._op_removeUnusedFiles.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Removes all files from the server that do not have an
        OriginalFile complement in the database, all the Pixels
        that do not have a complement in the database and all the
        Thumbnail's that do not have a complement in the database.
        Arguments:
        Throws:
        ResourceError -- If deletion fails.
        """
        def end_removeUnusedFiles(self, _r):
            return _M_ode.api.IRepositoryInfo._op_removeUnusedFiles.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IRepositoryInfoPrx.ice_checkedCast(proxy, '::ode::api::IRepositoryInfo', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IRepositoryInfoPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IRepositoryInfo'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IRepositoryInfoPrx = IcePy.defineProxy('::ode::api::IRepositoryInfo', IRepositoryInfoPrx)

    _M_ode.api._t_IRepositoryInfo = IcePy.defineClass('::ode::api::IRepositoryInfo', IRepositoryInfo, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IRepositoryInfo._ice_type = _M_ode.api._t_IRepositoryInfo

    IRepositoryInfo._op_getUsedSpaceInKilobytes = IcePy.Operation('getUsedSpaceInKilobytes', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IRepositoryInfo._op_getFreeSpaceInKilobytes = IcePy.Operation('getFreeSpaceInKilobytes', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IRepositoryInfo._op_getUsageFraction = IcePy.Operation('getUsageFraction', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_double, False, 0), (_M_ode._t_ServerError,))
    IRepositoryInfo._op_sanityCheckRepository = IcePy.Operation('sanityCheckRepository', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    IRepositoryInfo._op_removeUnusedFiles = IcePy.Operation('removeUnusedFiles', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))

    _M_ode.api.IRepositoryInfo = IRepositoryInfo
    del IRepositoryInfo

    _M_ode.api.IRepositoryInfoPrx = IRepositoryInfoPrx
    del IRepositoryInfoPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
