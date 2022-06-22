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

if 'Temperature' not in _M_ode.model.__dict__:
    _M_ode.model._t_Temperature = IcePy.declareClass('::ode::model::Temperature')
    _M_ode.model._t_TemperaturePrx = IcePy.declareProxy('::ode::model::Temperature')

if 'Pressure' not in _M_ode.model.__dict__:
    _M_ode.model._t_Pressure = IcePy.declareClass('::ode::model::Pressure')
    _M_ode.model._t_PressurePrx = IcePy.declareProxy('::ode::model::Pressure')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'ImagingEnvironment' not in _M_ode.model.__dict__:
    _M_ode.model.ImagingEnvironment = Ice.createTempClass()
    class ImagingEnvironment(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _temperature=None, _airPressure=None, _humidity=None, _co2percent=None, _map=None):
            if Ice.getType(self) == _M_ode.model.ImagingEnvironment:
                raise RuntimeError('ode.model.ImagingEnvironment is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._temperature = _temperature
            self._airPressure = _airPressure
            self._humidity = _humidity
            self._co2percent = _co2percent
            self._map = _map

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::ImagingEnvironment')

        def ice_id(self, current=None):
            return '::ode::model::ImagingEnvironment'

        def ice_staticId():
            return '::ode::model::ImagingEnvironment'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getTemperature(self, current=None):
            pass

        def setTemperature(self, theTemperature, current=None):
            pass

        def getAirPressure(self, current=None):
            pass

        def setAirPressure(self, theAirPressure, current=None):
            pass

        def getHumidity(self, current=None):
            pass

        def setHumidity(self, theHumidity, current=None):
            pass

        def getCo2percent(self, current=None):
            pass

        def setCo2percent(self, theCo2percent, current=None):
            pass

        def getMapAsMap(self, current=None):
            pass

        def getMap(self, current=None):
            pass

        def setMap(self, theMap, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_ImagingEnvironment)

        __repr__ = __str__

    _M_ode.model.ImagingEnvironmentPrx = Ice.createTempClass()
    class ImagingEnvironmentPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.ImagingEnvironment._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.ImagingEnvironment._op_setVersion.end(self, _r)

        def getTemperature(self, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getTemperature.invoke(self, ((), _ctx))

        def begin_getTemperature(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getTemperature.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTemperature(self, _r):
            return _M_ode.model.ImagingEnvironment._op_getTemperature.end(self, _r)

        def setTemperature(self, theTemperature, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setTemperature.invoke(self, ((theTemperature, ), _ctx))

        def begin_setTemperature(self, theTemperature, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setTemperature.begin(self, ((theTemperature, ), _response, _ex, _sent, _ctx))

        def end_setTemperature(self, _r):
            return _M_ode.model.ImagingEnvironment._op_setTemperature.end(self, _r)

        def getAirPressure(self, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getAirPressure.invoke(self, ((), _ctx))

        def begin_getAirPressure(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getAirPressure.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAirPressure(self, _r):
            return _M_ode.model.ImagingEnvironment._op_getAirPressure.end(self, _r)

        def setAirPressure(self, theAirPressure, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setAirPressure.invoke(self, ((theAirPressure, ), _ctx))

        def begin_setAirPressure(self, theAirPressure, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setAirPressure.begin(self, ((theAirPressure, ), _response, _ex, _sent, _ctx))

        def end_setAirPressure(self, _r):
            return _M_ode.model.ImagingEnvironment._op_setAirPressure.end(self, _r)

        def getHumidity(self, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getHumidity.invoke(self, ((), _ctx))

        def begin_getHumidity(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getHumidity.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getHumidity(self, _r):
            return _M_ode.model.ImagingEnvironment._op_getHumidity.end(self, _r)

        def setHumidity(self, theHumidity, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setHumidity.invoke(self, ((theHumidity, ), _ctx))

        def begin_setHumidity(self, theHumidity, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setHumidity.begin(self, ((theHumidity, ), _response, _ex, _sent, _ctx))

        def end_setHumidity(self, _r):
            return _M_ode.model.ImagingEnvironment._op_setHumidity.end(self, _r)

        def getCo2percent(self, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getCo2percent.invoke(self, ((), _ctx))

        def begin_getCo2percent(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getCo2percent.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCo2percent(self, _r):
            return _M_ode.model.ImagingEnvironment._op_getCo2percent.end(self, _r)

        def setCo2percent(self, theCo2percent, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setCo2percent.invoke(self, ((theCo2percent, ), _ctx))

        def begin_setCo2percent(self, theCo2percent, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setCo2percent.begin(self, ((theCo2percent, ), _response, _ex, _sent, _ctx))

        def end_setCo2percent(self, _r):
            return _M_ode.model.ImagingEnvironment._op_setCo2percent.end(self, _r)

        def getMapAsMap(self, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getMapAsMap.invoke(self, ((), _ctx))

        def begin_getMapAsMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getMapAsMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMapAsMap(self, _r):
            return _M_ode.model.ImagingEnvironment._op_getMapAsMap.end(self, _r)

        def getMap(self, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getMap.invoke(self, ((), _ctx))

        def begin_getMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_getMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMap(self, _r):
            return _M_ode.model.ImagingEnvironment._op_getMap.end(self, _r)

        def setMap(self, theMap, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setMap.invoke(self, ((theMap, ), _ctx))

        def begin_setMap(self, theMap, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ImagingEnvironment._op_setMap.begin(self, ((theMap, ), _response, _ex, _sent, _ctx))

        def end_setMap(self, _r):
            return _M_ode.model.ImagingEnvironment._op_setMap.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ImagingEnvironmentPrx.ice_checkedCast(proxy, '::ode::model::ImagingEnvironment', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ImagingEnvironmentPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ImagingEnvironment'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ImagingEnvironmentPrx = IcePy.defineProxy('::ode::model::ImagingEnvironment', ImagingEnvironmentPrx)

    _M_ode.model._t_ImagingEnvironment = IcePy.declareClass('::ode::model::ImagingEnvironment')

    _M_ode.model._t_ImagingEnvironment = IcePy.defineClass('::ode::model::ImagingEnvironment', ImagingEnvironment, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_temperature', (), _M_ode.model._t_Temperature, False, 0),
        ('_airPressure', (), _M_ode.model._t_Pressure, False, 0),
        ('_humidity', (), _M_ode._t_RDouble, False, 0),
        ('_co2percent', (), _M_ode._t_RDouble, False, 0),
        ('_map', (), _M_ode.api._t_NamedValueList, False, 0)
    ))
    ImagingEnvironment._ice_type = _M_ode.model._t_ImagingEnvironment

    ImagingEnvironment._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ImagingEnvironment._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ImagingEnvironment._op_getTemperature = IcePy.Operation('getTemperature', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Temperature, False, 0), ())
    ImagingEnvironment._op_setTemperature = IcePy.Operation('setTemperature', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Temperature, False, 0),), (), None, ())
    ImagingEnvironment._op_getAirPressure = IcePy.Operation('getAirPressure', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Pressure, False, 0), ())
    ImagingEnvironment._op_setAirPressure = IcePy.Operation('setAirPressure', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pressure, False, 0),), (), None, ())
    ImagingEnvironment._op_getHumidity = IcePy.Operation('getHumidity', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    ImagingEnvironment._op_setHumidity = IcePy.Operation('setHumidity', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    ImagingEnvironment._op_getCo2percent = IcePy.Operation('getCo2percent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    ImagingEnvironment._op_setCo2percent = IcePy.Operation('setCo2percent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    ImagingEnvironment._op_getMapAsMap = IcePy.Operation('getMapAsMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_StringStringMap, False, 0), ())
    ImagingEnvironment._op_getMap = IcePy.Operation('getMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_NamedValueList, False, 0), ())
    ImagingEnvironment._op_setMap = IcePy.Operation('setMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_NamedValueList, False, 0),), (), None, ())

    _M_ode.model.ImagingEnvironment = ImagingEnvironment
    del ImagingEnvironment

    _M_ode.model.ImagingEnvironmentPrx = ImagingEnvironmentPrx
    del ImagingEnvironmentPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
