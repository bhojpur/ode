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
import ode_model_IObject_ice
import ode_RTypes_ice
import ode_model_RTypes_ice
import ode_System_ice
import ode_Collections_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Start of module ode
__name__ = 'ode'

# Start of module ode.model
__name__ = 'ode.model'

if 'Pixels' not in _M_ode.model.__dict__:
    _M_ode.model._t_Pixels = IcePy.declareClass('::ode::model::Pixels')
    _M_ode.model._t_PixelsPrx = IcePy.declareProxy('::ode::model::Pixels')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'Thumbnail' not in _M_ode.model.__dict__:
    _M_ode.model.Thumbnail = Ice.createTempClass()
    class Thumbnail(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _pixels=None, _mimeType=None, _sizeX=None, _sizeY=None, _ref=None):
            if Ice.getType(self) == _M_ode.model.Thumbnail:
                raise RuntimeError('ode.model.Thumbnail is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._pixels = _pixels
            self._mimeType = _mimeType
            self._sizeX = _sizeX
            self._sizeY = _sizeY
            self._ref = _ref

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Thumbnail')

        def ice_id(self, current=None):
            return '::ode::model::Thumbnail'

        def ice_staticId():
            return '::ode::model::Thumbnail'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getPixels(self, current=None):
            pass

        def setPixels(self, thePixels, current=None):
            pass

        def getMimeType(self, current=None):
            pass

        def setMimeType(self, theMimeType, current=None):
            pass

        def getSizeX(self, current=None):
            pass

        def setSizeX(self, theSizeX, current=None):
            pass

        def getSizeY(self, current=None):
            pass

        def setSizeY(self, theSizeY, current=None):
            pass

        def getRef(self, current=None):
            pass

        def setRef(self, theRef, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Thumbnail)

        __repr__ = __str__

    _M_ode.model.ThumbnailPrx = Ice.createTempClass()
    class ThumbnailPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Thumbnail._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Thumbnail._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Thumbnail._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Thumbnail._op_setVersion.end(self, _r)

        def getPixels(self, _ctx=None):
            return _M_ode.model.Thumbnail._op_getPixels.invoke(self, ((), _ctx))

        def begin_getPixels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_getPixels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPixels(self, _r):
            return _M_ode.model.Thumbnail._op_getPixels.end(self, _r)

        def setPixels(self, thePixels, _ctx=None):
            return _M_ode.model.Thumbnail._op_setPixels.invoke(self, ((thePixels, ), _ctx))

        def begin_setPixels(self, thePixels, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_setPixels.begin(self, ((thePixels, ), _response, _ex, _sent, _ctx))

        def end_setPixels(self, _r):
            return _M_ode.model.Thumbnail._op_setPixels.end(self, _r)

        def getMimeType(self, _ctx=None):
            return _M_ode.model.Thumbnail._op_getMimeType.invoke(self, ((), _ctx))

        def begin_getMimeType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_getMimeType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMimeType(self, _r):
            return _M_ode.model.Thumbnail._op_getMimeType.end(self, _r)

        def setMimeType(self, theMimeType, _ctx=None):
            return _M_ode.model.Thumbnail._op_setMimeType.invoke(self, ((theMimeType, ), _ctx))

        def begin_setMimeType(self, theMimeType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_setMimeType.begin(self, ((theMimeType, ), _response, _ex, _sent, _ctx))

        def end_setMimeType(self, _r):
            return _M_ode.model.Thumbnail._op_setMimeType.end(self, _r)

        def getSizeX(self, _ctx=None):
            return _M_ode.model.Thumbnail._op_getSizeX.invoke(self, ((), _ctx))

        def begin_getSizeX(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_getSizeX.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSizeX(self, _r):
            return _M_ode.model.Thumbnail._op_getSizeX.end(self, _r)

        def setSizeX(self, theSizeX, _ctx=None):
            return _M_ode.model.Thumbnail._op_setSizeX.invoke(self, ((theSizeX, ), _ctx))

        def begin_setSizeX(self, theSizeX, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_setSizeX.begin(self, ((theSizeX, ), _response, _ex, _sent, _ctx))

        def end_setSizeX(self, _r):
            return _M_ode.model.Thumbnail._op_setSizeX.end(self, _r)

        def getSizeY(self, _ctx=None):
            return _M_ode.model.Thumbnail._op_getSizeY.invoke(self, ((), _ctx))

        def begin_getSizeY(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_getSizeY.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSizeY(self, _r):
            return _M_ode.model.Thumbnail._op_getSizeY.end(self, _r)

        def setSizeY(self, theSizeY, _ctx=None):
            return _M_ode.model.Thumbnail._op_setSizeY.invoke(self, ((theSizeY, ), _ctx))

        def begin_setSizeY(self, theSizeY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_setSizeY.begin(self, ((theSizeY, ), _response, _ex, _sent, _ctx))

        def end_setSizeY(self, _r):
            return _M_ode.model.Thumbnail._op_setSizeY.end(self, _r)

        def getRef(self, _ctx=None):
            return _M_ode.model.Thumbnail._op_getRef.invoke(self, ((), _ctx))

        def begin_getRef(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_getRef.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRef(self, _r):
            return _M_ode.model.Thumbnail._op_getRef.end(self, _r)

        def setRef(self, theRef, _ctx=None):
            return _M_ode.model.Thumbnail._op_setRef.invoke(self, ((theRef, ), _ctx))

        def begin_setRef(self, theRef, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Thumbnail._op_setRef.begin(self, ((theRef, ), _response, _ex, _sent, _ctx))

        def end_setRef(self, _r):
            return _M_ode.model.Thumbnail._op_setRef.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ThumbnailPrx.ice_checkedCast(proxy, '::ode::model::Thumbnail', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ThumbnailPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Thumbnail'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ThumbnailPrx = IcePy.defineProxy('::ode::model::Thumbnail', ThumbnailPrx)

    _M_ode.model._t_Thumbnail = IcePy.declareClass('::ode::model::Thumbnail')

    _M_ode.model._t_Thumbnail = IcePy.defineClass('::ode::model::Thumbnail', Thumbnail, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_pixels', (), _M_ode.model._t_Pixels, False, 0),
        ('_mimeType', (), _M_ode._t_RString, False, 0),
        ('_sizeX', (), _M_ode._t_RInt, False, 0),
        ('_sizeY', (), _M_ode._t_RInt, False, 0),
        ('_ref', (), _M_ode._t_RString, False, 0)
    ))
    Thumbnail._ice_type = _M_ode.model._t_Thumbnail

    Thumbnail._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Thumbnail._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Thumbnail._op_getPixels = IcePy.Operation('getPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Pixels, False, 0), ())
    Thumbnail._op_setPixels = IcePy.Operation('setPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())
    Thumbnail._op_getMimeType = IcePy.Operation('getMimeType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Thumbnail._op_setMimeType = IcePy.Operation('setMimeType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Thumbnail._op_getSizeX = IcePy.Operation('getSizeX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Thumbnail._op_setSizeX = IcePy.Operation('setSizeX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Thumbnail._op_getSizeY = IcePy.Operation('getSizeY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Thumbnail._op_setSizeY = IcePy.Operation('setSizeY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Thumbnail._op_getRef = IcePy.Operation('getRef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Thumbnail._op_setRef = IcePy.Operation('setRef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Thumbnail = Thumbnail
    del Thumbnail

    _M_ode.model.ThumbnailPrx = ThumbnailPrx
    del ThumbnailPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
