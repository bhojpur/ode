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

if 'Length' not in _M_ode.model.__dict__:
    _M_ode.model._t_Length = IcePy.declareClass('::ode::model::Length')
    _M_ode.model._t_LengthPrx = IcePy.declareProxy('::ode::model::Length')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'StageLabel' not in _M_ode.model.__dict__:
    _M_ode.model.StageLabel = Ice.createTempClass()
    class StageLabel(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _positionX=None, _positionY=None, _positionZ=None, _name=None):
            if Ice.getType(self) == _M_ode.model.StageLabel:
                raise RuntimeError('ode.model.StageLabel is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._positionX = _positionX
            self._positionY = _positionY
            self._positionZ = _positionZ
            self._name = _name

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::StageLabel')

        def ice_id(self, current=None):
            return '::ode::model::StageLabel'

        def ice_staticId():
            return '::ode::model::StageLabel'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getPositionX(self, current=None):
            pass

        def setPositionX(self, thePositionX, current=None):
            pass

        def getPositionY(self, current=None):
            pass

        def setPositionY(self, thePositionY, current=None):
            pass

        def getPositionZ(self, current=None):
            pass

        def setPositionZ(self, thePositionZ, current=None):
            pass

        def getName(self, current=None):
            pass

        def setName(self, theName, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_StageLabel)

        __repr__ = __str__

    _M_ode.model.StageLabelPrx = Ice.createTempClass()
    class StageLabelPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.StageLabel._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StageLabel._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.StageLabel._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.StageLabel._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StageLabel._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.StageLabel._op_setVersion.end(self, _r)

        def getPositionX(self, _ctx=None):
            return _M_ode.model.StageLabel._op_getPositionX.invoke(self, ((), _ctx))

        def begin_getPositionX(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StageLabel._op_getPositionX.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPositionX(self, _r):
            return _M_ode.model.StageLabel._op_getPositionX.end(self, _r)

        def setPositionX(self, thePositionX, _ctx=None):
            return _M_ode.model.StageLabel._op_setPositionX.invoke(self, ((thePositionX, ), _ctx))

        def begin_setPositionX(self, thePositionX, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StageLabel._op_setPositionX.begin(self, ((thePositionX, ), _response, _ex, _sent, _ctx))

        def end_setPositionX(self, _r):
            return _M_ode.model.StageLabel._op_setPositionX.end(self, _r)

        def getPositionY(self, _ctx=None):
            return _M_ode.model.StageLabel._op_getPositionY.invoke(self, ((), _ctx))

        def begin_getPositionY(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StageLabel._op_getPositionY.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPositionY(self, _r):
            return _M_ode.model.StageLabel._op_getPositionY.end(self, _r)

        def setPositionY(self, thePositionY, _ctx=None):
            return _M_ode.model.StageLabel._op_setPositionY.invoke(self, ((thePositionY, ), _ctx))

        def begin_setPositionY(self, thePositionY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StageLabel._op_setPositionY.begin(self, ((thePositionY, ), _response, _ex, _sent, _ctx))

        def end_setPositionY(self, _r):
            return _M_ode.model.StageLabel._op_setPositionY.end(self, _r)

        def getPositionZ(self, _ctx=None):
            return _M_ode.model.StageLabel._op_getPositionZ.invoke(self, ((), _ctx))

        def begin_getPositionZ(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StageLabel._op_getPositionZ.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPositionZ(self, _r):
            return _M_ode.model.StageLabel._op_getPositionZ.end(self, _r)

        def setPositionZ(self, thePositionZ, _ctx=None):
            return _M_ode.model.StageLabel._op_setPositionZ.invoke(self, ((thePositionZ, ), _ctx))

        def begin_setPositionZ(self, thePositionZ, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StageLabel._op_setPositionZ.begin(self, ((thePositionZ, ), _response, _ex, _sent, _ctx))

        def end_setPositionZ(self, _r):
            return _M_ode.model.StageLabel._op_setPositionZ.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.StageLabel._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StageLabel._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.StageLabel._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.StageLabel._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StageLabel._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.StageLabel._op_setName.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.StageLabelPrx.ice_checkedCast(proxy, '::ode::model::StageLabel', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.StageLabelPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::StageLabel'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_StageLabelPrx = IcePy.defineProxy('::ode::model::StageLabel', StageLabelPrx)

    _M_ode.model._t_StageLabel = IcePy.declareClass('::ode::model::StageLabel')

    _M_ode.model._t_StageLabel = IcePy.defineClass('::ode::model::StageLabel', StageLabel, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_positionX', (), _M_ode.model._t_Length, False, 0),
        ('_positionY', (), _M_ode.model._t_Length, False, 0),
        ('_positionZ', (), _M_ode.model._t_Length, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0)
    ))
    StageLabel._ice_type = _M_ode.model._t_StageLabel

    StageLabel._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    StageLabel._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    StageLabel._op_getPositionX = IcePy.Operation('getPositionX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    StageLabel._op_setPositionX = IcePy.Operation('setPositionX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    StageLabel._op_getPositionY = IcePy.Operation('getPositionY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    StageLabel._op_setPositionY = IcePy.Operation('setPositionY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    StageLabel._op_getPositionZ = IcePy.Operation('getPositionZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    StageLabel._op_setPositionZ = IcePy.Operation('setPositionZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    StageLabel._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    StageLabel._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.StageLabel = StageLabel
    del StageLabel

    _M_ode.model.StageLabelPrx = StageLabelPrx
    del StageLabelPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
