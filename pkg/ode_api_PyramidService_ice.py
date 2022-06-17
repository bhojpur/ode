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
import ode_ServicesF_ice

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

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

if 'ResolutionDescription' not in _M_ode.api.__dict__:
    _M_ode.api.ResolutionDescription = Ice.createTempClass()
    class ResolutionDescription(Ice.Object):
        def __init__(self, sizeX=0, sizeY=0):
            self.sizeX = sizeX
            self.sizeY = sizeY

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ResolutionDescription')

        def ice_id(self, current=None):
            return '::ode::api::ResolutionDescription'

        def ice_staticId():
            return '::ode::api::ResolutionDescription'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_ResolutionDescription)

        __repr__ = __str__

    _M_ode.api.ResolutionDescriptionPrx = Ice.createTempClass()
    class ResolutionDescriptionPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ResolutionDescriptionPrx.ice_checkedCast(proxy, '::ode::api::ResolutionDescription', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ResolutionDescriptionPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::ResolutionDescription'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ResolutionDescriptionPrx = IcePy.defineProxy('::ode::api::ResolutionDescription', ResolutionDescriptionPrx)

    _M_ode.api._t_ResolutionDescription = IcePy.defineClass('::ode::api::ResolutionDescription', ResolutionDescription, -1, (), False, False, None, (), (
        ('sizeX', (), IcePy._t_int, False, 0),
        ('sizeY', (), IcePy._t_int, False, 0)
    ))
    ResolutionDescription._ice_type = _M_ode.api._t_ResolutionDescription

    _M_ode.api.ResolutionDescription = ResolutionDescription
    del ResolutionDescription

    _M_ode.api.ResolutionDescriptionPrx = ResolutionDescriptionPrx
    del ResolutionDescriptionPrx

if '_t_ResolutionDescriptions' not in _M_ode.api.__dict__:
    _M_ode.api._t_ResolutionDescriptions = IcePy.defineSequence('::ode::api::ResolutionDescriptions', (), _M_ode.api._t_ResolutionDescription)

if 'PyramidService' not in _M_ode.api.__dict__:
    _M_ode.api.PyramidService = Ice.createTempClass()
    class PyramidService(_M_ode.api.StatefulServiceInterface):
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.PyramidService:
                raise RuntimeError('ode.api.PyramidService is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::PyramidService', '::ode::api::ServiceInterface', '::ode::api::StatefulServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::PyramidService'

        def ice_staticId():
            return '::ode::api::PyramidService'
        ice_staticId = staticmethod(ice_staticId)

        def requiresPixelsPyramid_async(self, _cb, current=None):
            """
            Whether or not this raw pixels store requires a backing
            pixels pyramid to provide sub-resolutions of the data.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getResolutionLevels_async(self, _cb, current=None):
            """
            Retrieves the number of resolution levels that the backing
            pixels pyramid contains.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getResolutionDescriptions_async(self, _cb, current=None):
            """
            Retrieves a more complete definition of the resolution
            level in question. The size of this array will be of
            length {@code getResolutionLevels}.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getResolutionLevel_async(self, _cb, current=None):
            """
            Retrieves the active resolution level.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setResolutionLevel_async(self, _cb, resolutionLevel, current=None):
            """
            Sets the active resolution level.
            Arguments:
            _cb -- The asynchronous callback object.
            resolutionLevel -- The resolution level to be used by the pixel buffer.
            current -- The Current object for the invocation.
            """
            pass

        def getTileSize_async(self, _cb, current=None):
            """
            Retrieves the tile size for the pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_PyramidService)

        __repr__ = __str__

    _M_ode.api.PyramidServicePrx = Ice.createTempClass()
    class PyramidServicePrx(_M_ode.api.StatefulServiceInterfacePrx):

        """
        Whether or not this raw pixels store requires a backing
        pixels pyramid to provide sub-resolutions of the data.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: true if the pixels store requires a pixels pyramid and false otherwise.
        """
        def requiresPixelsPyramid(self, _ctx=None):
            return _M_ode.api.PyramidService._op_requiresPixelsPyramid.invoke(self, ((), _ctx))

        """
        Whether or not this raw pixels store requires a backing
        pixels pyramid to provide sub-resolutions of the data.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_requiresPixelsPyramid(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.PyramidService._op_requiresPixelsPyramid.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Whether or not this raw pixels store requires a backing
        pixels pyramid to provide sub-resolutions of the data.
        Arguments:
        Returns: true if the pixels store requires a pixels pyramid and false otherwise.
        """
        def end_requiresPixelsPyramid(self, _r):
            return _M_ode.api.PyramidService._op_requiresPixelsPyramid.end(self, _r)

        """
        Retrieves the number of resolution levels that the backing
        pixels pyramid contains.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: The number of resolution levels. This value does not necessarily indicate either the presence or absence of a pixels pyramid.
        """
        def getResolutionLevels(self, _ctx=None):
            return _M_ode.api.PyramidService._op_getResolutionLevels.invoke(self, ((), _ctx))

        """
        Retrieves the number of resolution levels that the backing
        pixels pyramid contains.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getResolutionLevels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.PyramidService._op_getResolutionLevels.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves the number of resolution levels that the backing
        pixels pyramid contains.
        Arguments:
        Returns: The number of resolution levels. This value does not necessarily indicate either the presence or absence of a pixels pyramid.
        """
        def end_getResolutionLevels(self, _r):
            return _M_ode.api.PyramidService._op_getResolutionLevels.end(self, _r)

        """
        Retrieves a more complete definition of the resolution
        level in question. The size of this array will be of
        length {@code getResolutionLevels}.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getResolutionDescriptions(self, _ctx=None):
            return _M_ode.api.PyramidService._op_getResolutionDescriptions.invoke(self, ((), _ctx))

        """
        Retrieves a more complete definition of the resolution
        level in question. The size of this array will be of
        length {@code getResolutionLevels}.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getResolutionDescriptions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.PyramidService._op_getResolutionDescriptions.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves a more complete definition of the resolution
        level in question. The size of this array will be of
        length {@code getResolutionLevels}.
        Arguments:
        """
        def end_getResolutionDescriptions(self, _r):
            return _M_ode.api.PyramidService._op_getResolutionDescriptions.end(self, _r)

        """
        Retrieves the active resolution level.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: The active resolution level.
        """
        def getResolutionLevel(self, _ctx=None):
            return _M_ode.api.PyramidService._op_getResolutionLevel.invoke(self, ((), _ctx))

        """
        Retrieves the active resolution level.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getResolutionLevel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.PyramidService._op_getResolutionLevel.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves the active resolution level.
        Arguments:
        Returns: The active resolution level.
        """
        def end_getResolutionLevel(self, _r):
            return _M_ode.api.PyramidService._op_getResolutionLevel.end(self, _r)

        """
        Sets the active resolution level.
        Arguments:
        resolutionLevel -- The resolution level to be used by the pixel buffer.
        _ctx -- The request context for the invocation.
        """
        def setResolutionLevel(self, resolutionLevel, _ctx=None):
            return _M_ode.api.PyramidService._op_setResolutionLevel.invoke(self, ((resolutionLevel, ), _ctx))

        """
        Sets the active resolution level.
        Arguments:
        resolutionLevel -- The resolution level to be used by the pixel buffer.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setResolutionLevel(self, resolutionLevel, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.PyramidService._op_setResolutionLevel.begin(self, ((resolutionLevel, ), _response, _ex, _sent, _ctx))

        """
        Sets the active resolution level.
        Arguments:
        resolutionLevel -- The resolution level to be used by the pixel buffer.
        """
        def end_setResolutionLevel(self, _r):
            return _M_ode.api.PyramidService._op_setResolutionLevel.end(self, _r)

        """
        Retrieves the tile size for the pixel store.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: An array of length = 2 where the first value of the array is the tile width and the second value is the tile height.
        """
        def getTileSize(self, _ctx=None):
            return _M_ode.api.PyramidService._op_getTileSize.invoke(self, ((), _ctx))

        """
        Retrieves the tile size for the pixel store.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getTileSize(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.PyramidService._op_getTileSize.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves the tile size for the pixel store.
        Arguments:
        Returns: An array of length = 2 where the first value of the array is the tile width and the second value is the tile height.
        """
        def end_getTileSize(self, _r):
            return _M_ode.api.PyramidService._op_getTileSize.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.PyramidServicePrx.ice_checkedCast(proxy, '::ode::api::PyramidService', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.PyramidServicePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::PyramidService'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_PyramidServicePrx = IcePy.defineProxy('::ode::api::PyramidService', PyramidServicePrx)

    _M_ode.api._t_PyramidService = IcePy.defineClass('::ode::api::PyramidService', PyramidService, -1, (), True, False, None, (_M_ode.api._t_StatefulServiceInterface,), ())
    PyramidService._ice_type = _M_ode.api._t_PyramidService

    PyramidService._op_requiresPixelsPyramid = IcePy.Operation('requiresPixelsPyramid', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    PyramidService._op_getResolutionLevels = IcePy.Operation('getResolutionLevels', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    PyramidService._op_getResolutionDescriptions = IcePy.Operation('getResolutionDescriptions', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_ResolutionDescriptions, False, 0), (_M_ode._t_ServerError,))
    PyramidService._op_getResolutionLevel = IcePy.Operation('getResolutionLevel', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    PyramidService._op_setResolutionLevel = IcePy.Operation('setResolutionLevel', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), None, (_M_ode._t_ServerError,))
    PyramidService._op_getTileSize = IcePy.Operation('getTileSize', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_Ice._t_IntSeq, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.PyramidService = PyramidService
    del PyramidService

    _M_ode.api.PyramidServicePrx = PyramidServicePrx
    del PyramidServicePrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
