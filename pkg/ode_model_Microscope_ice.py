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

if 'MicroscopeType' not in _M_ode.model.__dict__:
    _M_ode.model._t_MicroscopeType = IcePy.declareClass('::ode::model::MicroscopeType')
    _M_ode.model._t_MicroscopeTypePrx = IcePy.declareProxy('::ode::model::MicroscopeType')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'Microscope' not in _M_ode.model.__dict__:
    _M_ode.model.Microscope = Ice.createTempClass()
    class Microscope(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _manufacturer=None, _model=None, _lotNumber=None, _serialNumber=None, _type=None):
            if Ice.getType(self) == _M_ode.model.Microscope:
                raise RuntimeError('ode.model.Microscope is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._manufacturer = _manufacturer
            self._model = _model
            self._lotNumber = _lotNumber
            self._serialNumber = _serialNumber
            self._type = _type

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Microscope')

        def ice_id(self, current=None):
            return '::ode::model::Microscope'

        def ice_staticId():
            return '::ode::model::Microscope'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getManufacturer(self, current=None):
            pass

        def setManufacturer(self, theManufacturer, current=None):
            pass

        def getModel(self, current=None):
            pass

        def setModel(self, theModel, current=None):
            pass

        def getLotNumber(self, current=None):
            pass

        def setLotNumber(self, theLotNumber, current=None):
            pass

        def getSerialNumber(self, current=None):
            pass

        def setSerialNumber(self, theSerialNumber, current=None):
            pass

        def getType(self, current=None):
            pass

        def setType(self, theType, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Microscope)

        __repr__ = __str__

    _M_ode.model.MicroscopePrx = Ice.createTempClass()
    class MicroscopePrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Microscope._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Microscope._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Microscope._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Microscope._op_setVersion.end(self, _r)

        def getManufacturer(self, _ctx=None):
            return _M_ode.model.Microscope._op_getManufacturer.invoke(self, ((), _ctx))

        def begin_getManufacturer(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_getManufacturer.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getManufacturer(self, _r):
            return _M_ode.model.Microscope._op_getManufacturer.end(self, _r)

        def setManufacturer(self, theManufacturer, _ctx=None):
            return _M_ode.model.Microscope._op_setManufacturer.invoke(self, ((theManufacturer, ), _ctx))

        def begin_setManufacturer(self, theManufacturer, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_setManufacturer.begin(self, ((theManufacturer, ), _response, _ex, _sent, _ctx))

        def end_setManufacturer(self, _r):
            return _M_ode.model.Microscope._op_setManufacturer.end(self, _r)

        def getModel(self, _ctx=None):
            return _M_ode.model.Microscope._op_getModel.invoke(self, ((), _ctx))

        def begin_getModel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_getModel.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getModel(self, _r):
            return _M_ode.model.Microscope._op_getModel.end(self, _r)

        def setModel(self, theModel, _ctx=None):
            return _M_ode.model.Microscope._op_setModel.invoke(self, ((theModel, ), _ctx))

        def begin_setModel(self, theModel, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_setModel.begin(self, ((theModel, ), _response, _ex, _sent, _ctx))

        def end_setModel(self, _r):
            return _M_ode.model.Microscope._op_setModel.end(self, _r)

        def getLotNumber(self, _ctx=None):
            return _M_ode.model.Microscope._op_getLotNumber.invoke(self, ((), _ctx))

        def begin_getLotNumber(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_getLotNumber.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLotNumber(self, _r):
            return _M_ode.model.Microscope._op_getLotNumber.end(self, _r)

        def setLotNumber(self, theLotNumber, _ctx=None):
            return _M_ode.model.Microscope._op_setLotNumber.invoke(self, ((theLotNumber, ), _ctx))

        def begin_setLotNumber(self, theLotNumber, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_setLotNumber.begin(self, ((theLotNumber, ), _response, _ex, _sent, _ctx))

        def end_setLotNumber(self, _r):
            return _M_ode.model.Microscope._op_setLotNumber.end(self, _r)

        def getSerialNumber(self, _ctx=None):
            return _M_ode.model.Microscope._op_getSerialNumber.invoke(self, ((), _ctx))

        def begin_getSerialNumber(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_getSerialNumber.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSerialNumber(self, _r):
            return _M_ode.model.Microscope._op_getSerialNumber.end(self, _r)

        def setSerialNumber(self, theSerialNumber, _ctx=None):
            return _M_ode.model.Microscope._op_setSerialNumber.invoke(self, ((theSerialNumber, ), _ctx))

        def begin_setSerialNumber(self, theSerialNumber, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_setSerialNumber.begin(self, ((theSerialNumber, ), _response, _ex, _sent, _ctx))

        def end_setSerialNumber(self, _r):
            return _M_ode.model.Microscope._op_setSerialNumber.end(self, _r)

        def getType(self, _ctx=None):
            return _M_ode.model.Microscope._op_getType.invoke(self, ((), _ctx))

        def begin_getType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_getType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getType(self, _r):
            return _M_ode.model.Microscope._op_getType.end(self, _r)

        def setType(self, theType, _ctx=None):
            return _M_ode.model.Microscope._op_setType.invoke(self, ((theType, ), _ctx))

        def begin_setType(self, theType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Microscope._op_setType.begin(self, ((theType, ), _response, _ex, _sent, _ctx))

        def end_setType(self, _r):
            return _M_ode.model.Microscope._op_setType.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.MicroscopePrx.ice_checkedCast(proxy, '::ode::model::Microscope', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.MicroscopePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Microscope'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_MicroscopePrx = IcePy.defineProxy('::ode::model::Microscope', MicroscopePrx)

    _M_ode.model._t_Microscope = IcePy.declareClass('::ode::model::Microscope')

    _M_ode.model._t_Microscope = IcePy.defineClass('::ode::model::Microscope', Microscope, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_manufacturer', (), _M_ode._t_RString, False, 0),
        ('_model', (), _M_ode._t_RString, False, 0),
        ('_lotNumber', (), _M_ode._t_RString, False, 0),
        ('_serialNumber', (), _M_ode._t_RString, False, 0),
        ('_type', (), _M_ode.model._t_MicroscopeType, False, 0)
    ))
    Microscope._ice_type = _M_ode.model._t_Microscope

    Microscope._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Microscope._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Microscope._op_getManufacturer = IcePy.Operation('getManufacturer', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Microscope._op_setManufacturer = IcePy.Operation('setManufacturer', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Microscope._op_getModel = IcePy.Operation('getModel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Microscope._op_setModel = IcePy.Operation('setModel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Microscope._op_getLotNumber = IcePy.Operation('getLotNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Microscope._op_setLotNumber = IcePy.Operation('setLotNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Microscope._op_getSerialNumber = IcePy.Operation('getSerialNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Microscope._op_setSerialNumber = IcePy.Operation('setSerialNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Microscope._op_getType = IcePy.Operation('getType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_MicroscopeType, False, 0), ())
    Microscope._op_setType = IcePy.Operation('setType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_MicroscopeType, False, 0),), (), None, ())

    _M_ode.model.Microscope = Microscope
    del Microscope

    _M_ode.model.MicroscopePrx = MicroscopePrx
    del MicroscopePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
