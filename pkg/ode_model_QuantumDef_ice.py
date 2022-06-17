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

if 'QuantumDef' not in _M_ode.model.__dict__:
    _M_ode.model.QuantumDef = Ice.createTempClass()
    class QuantumDef(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _cdStart=None, _cdEnd=None, _bitResolution=None):
            if Ice.getType(self) == _M_ode.model.QuantumDef:
                raise RuntimeError('ode.model.QuantumDef is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._cdStart = _cdStart
            self._cdEnd = _cdEnd
            self._bitResolution = _bitResolution

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::QuantumDef')

        def ice_id(self, current=None):
            return '::ode::model::QuantumDef'

        def ice_staticId():
            return '::ode::model::QuantumDef'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getCdStart(self, current=None):
            pass

        def setCdStart(self, theCdStart, current=None):
            pass

        def getCdEnd(self, current=None):
            pass

        def setCdEnd(self, theCdEnd, current=None):
            pass

        def getBitResolution(self, current=None):
            pass

        def setBitResolution(self, theBitResolution, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_QuantumDef)

        __repr__ = __str__

    _M_ode.model.QuantumDefPrx = Ice.createTempClass()
    class QuantumDefPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.QuantumDef._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.QuantumDef._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.QuantumDef._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.QuantumDef._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.QuantumDef._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.QuantumDef._op_setVersion.end(self, _r)

        def getCdStart(self, _ctx=None):
            return _M_ode.model.QuantumDef._op_getCdStart.invoke(self, ((), _ctx))

        def begin_getCdStart(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.QuantumDef._op_getCdStart.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCdStart(self, _r):
            return _M_ode.model.QuantumDef._op_getCdStart.end(self, _r)

        def setCdStart(self, theCdStart, _ctx=None):
            return _M_ode.model.QuantumDef._op_setCdStart.invoke(self, ((theCdStart, ), _ctx))

        def begin_setCdStart(self, theCdStart, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.QuantumDef._op_setCdStart.begin(self, ((theCdStart, ), _response, _ex, _sent, _ctx))

        def end_setCdStart(self, _r):
            return _M_ode.model.QuantumDef._op_setCdStart.end(self, _r)

        def getCdEnd(self, _ctx=None):
            return _M_ode.model.QuantumDef._op_getCdEnd.invoke(self, ((), _ctx))

        def begin_getCdEnd(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.QuantumDef._op_getCdEnd.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCdEnd(self, _r):
            return _M_ode.model.QuantumDef._op_getCdEnd.end(self, _r)

        def setCdEnd(self, theCdEnd, _ctx=None):
            return _M_ode.model.QuantumDef._op_setCdEnd.invoke(self, ((theCdEnd, ), _ctx))

        def begin_setCdEnd(self, theCdEnd, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.QuantumDef._op_setCdEnd.begin(self, ((theCdEnd, ), _response, _ex, _sent, _ctx))

        def end_setCdEnd(self, _r):
            return _M_ode.model.QuantumDef._op_setCdEnd.end(self, _r)

        def getBitResolution(self, _ctx=None):
            return _M_ode.model.QuantumDef._op_getBitResolution.invoke(self, ((), _ctx))

        def begin_getBitResolution(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.QuantumDef._op_getBitResolution.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getBitResolution(self, _r):
            return _M_ode.model.QuantumDef._op_getBitResolution.end(self, _r)

        def setBitResolution(self, theBitResolution, _ctx=None):
            return _M_ode.model.QuantumDef._op_setBitResolution.invoke(self, ((theBitResolution, ), _ctx))

        def begin_setBitResolution(self, theBitResolution, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.QuantumDef._op_setBitResolution.begin(self, ((theBitResolution, ), _response, _ex, _sent, _ctx))

        def end_setBitResolution(self, _r):
            return _M_ode.model.QuantumDef._op_setBitResolution.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.QuantumDefPrx.ice_checkedCast(proxy, '::ode::model::QuantumDef', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.QuantumDefPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::QuantumDef'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_QuantumDefPrx = IcePy.defineProxy('::ode::model::QuantumDef', QuantumDefPrx)

    _M_ode.model._t_QuantumDef = IcePy.declareClass('::ode::model::QuantumDef')

    _M_ode.model._t_QuantumDef = IcePy.defineClass('::ode::model::QuantumDef', QuantumDef, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_cdStart', (), _M_ode._t_RInt, False, 0),
        ('_cdEnd', (), _M_ode._t_RInt, False, 0),
        ('_bitResolution', (), _M_ode._t_RInt, False, 0)
    ))
    QuantumDef._ice_type = _M_ode.model._t_QuantumDef

    QuantumDef._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    QuantumDef._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    QuantumDef._op_getCdStart = IcePy.Operation('getCdStart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    QuantumDef._op_setCdStart = IcePy.Operation('setCdStart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    QuantumDef._op_getCdEnd = IcePy.Operation('getCdEnd', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    QuantumDef._op_setCdEnd = IcePy.Operation('setCdEnd', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    QuantumDef._op_getBitResolution = IcePy.Operation('getBitResolution', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    QuantumDef._op_setBitResolution = IcePy.Operation('setBitResolution', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())

    _M_ode.model.QuantumDef = QuantumDef
    del QuantumDef

    _M_ode.model.QuantumDefPrx = QuantumDefPrx
    del QuantumDefPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
