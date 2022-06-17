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

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'StatsInfo' not in _M_ode.model.__dict__:
    _M_ode.model.StatsInfo = Ice.createTempClass()
    class StatsInfo(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _globalMin=None, _globalMax=None):
            if Ice.getType(self) == _M_ode.model.StatsInfo:
                raise RuntimeError('ode.model.StatsInfo is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._globalMin = _globalMin
            self._globalMax = _globalMax

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::StatsInfo')

        def ice_id(self, current=None):
            return '::ode::model::StatsInfo'

        def ice_staticId():
            return '::ode::model::StatsInfo'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getGlobalMin(self, current=None):
            pass

        def setGlobalMin(self, theGlobalMin, current=None):
            pass

        def getGlobalMax(self, current=None):
            pass

        def setGlobalMax(self, theGlobalMax, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_StatsInfo)

        __repr__ = __str__

    _M_ode.model.StatsInfoPrx = Ice.createTempClass()
    class StatsInfoPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.StatsInfo._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StatsInfo._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.StatsInfo._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.StatsInfo._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StatsInfo._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.StatsInfo._op_setVersion.end(self, _r)

        def getGlobalMin(self, _ctx=None):
            return _M_ode.model.StatsInfo._op_getGlobalMin.invoke(self, ((), _ctx))

        def begin_getGlobalMin(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StatsInfo._op_getGlobalMin.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getGlobalMin(self, _r):
            return _M_ode.model.StatsInfo._op_getGlobalMin.end(self, _r)

        def setGlobalMin(self, theGlobalMin, _ctx=None):
            return _M_ode.model.StatsInfo._op_setGlobalMin.invoke(self, ((theGlobalMin, ), _ctx))

        def begin_setGlobalMin(self, theGlobalMin, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StatsInfo._op_setGlobalMin.begin(self, ((theGlobalMin, ), _response, _ex, _sent, _ctx))

        def end_setGlobalMin(self, _r):
            return _M_ode.model.StatsInfo._op_setGlobalMin.end(self, _r)

        def getGlobalMax(self, _ctx=None):
            return _M_ode.model.StatsInfo._op_getGlobalMax.invoke(self, ((), _ctx))

        def begin_getGlobalMax(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StatsInfo._op_getGlobalMax.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getGlobalMax(self, _r):
            return _M_ode.model.StatsInfo._op_getGlobalMax.end(self, _r)

        def setGlobalMax(self, theGlobalMax, _ctx=None):
            return _M_ode.model.StatsInfo._op_setGlobalMax.invoke(self, ((theGlobalMax, ), _ctx))

        def begin_setGlobalMax(self, theGlobalMax, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.StatsInfo._op_setGlobalMax.begin(self, ((theGlobalMax, ), _response, _ex, _sent, _ctx))

        def end_setGlobalMax(self, _r):
            return _M_ode.model.StatsInfo._op_setGlobalMax.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.StatsInfoPrx.ice_checkedCast(proxy, '::ode::model::StatsInfo', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.StatsInfoPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::StatsInfo'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_StatsInfoPrx = IcePy.defineProxy('::ode::model::StatsInfo', StatsInfoPrx)

    _M_ode.model._t_StatsInfo = IcePy.declareClass('::ode::model::StatsInfo')

    _M_ode.model._t_StatsInfo = IcePy.defineClass('::ode::model::StatsInfo', StatsInfo, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_globalMin', (), _M_ode._t_RDouble, False, 0),
        ('_globalMax', (), _M_ode._t_RDouble, False, 0)
    ))
    StatsInfo._ice_type = _M_ode.model._t_StatsInfo

    StatsInfo._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    StatsInfo._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    StatsInfo._op_getGlobalMin = IcePy.Operation('getGlobalMin', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    StatsInfo._op_setGlobalMin = IcePy.Operation('setGlobalMin', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    StatsInfo._op_getGlobalMax = IcePy.Operation('getGlobalMax', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    StatsInfo._op_setGlobalMax = IcePy.Operation('setGlobalMax', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())

    _M_ode.model.StatsInfo = StatsInfo
    del StatsInfo

    _M_ode.model.StatsInfoPrx = StatsInfoPrx
    del StatsInfoPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
