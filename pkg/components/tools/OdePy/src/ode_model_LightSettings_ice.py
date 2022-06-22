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

if 'LightSource' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightSource = IcePy.declareClass('::ode::model::LightSource')
    _M_ode.model._t_LightSourcePrx = IcePy.declareProxy('::ode::model::LightSource')

if 'MicrobeamManipulation' not in _M_ode.model.__dict__:
    _M_ode.model._t_MicrobeamManipulation = IcePy.declareClass('::ode::model::MicrobeamManipulation')
    _M_ode.model._t_MicrobeamManipulationPrx = IcePy.declareProxy('::ode::model::MicrobeamManipulation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'LightSettings' not in _M_ode.model.__dict__:
    _M_ode.model.LightSettings = Ice.createTempClass()
    class LightSettings(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _attenuation=None, _wavelength=None, _lightSource=None, _microbeamManipulation=None):
            if Ice.getType(self) == _M_ode.model.LightSettings:
                raise RuntimeError('ode.model.LightSettings is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._attenuation = _attenuation
            self._wavelength = _wavelength
            self._lightSource = _lightSource
            self._microbeamManipulation = _microbeamManipulation

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::LightSettings')

        def ice_id(self, current=None):
            return '::ode::model::LightSettings'

        def ice_staticId():
            return '::ode::model::LightSettings'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getAttenuation(self, current=None):
            pass

        def setAttenuation(self, theAttenuation, current=None):
            pass

        def getWavelength(self, current=None):
            pass

        def setWavelength(self, theWavelength, current=None):
            pass

        def getLightSource(self, current=None):
            pass

        def setLightSource(self, theLightSource, current=None):
            pass

        def getMicrobeamManipulation(self, current=None):
            pass

        def setMicrobeamManipulation(self, theMicrobeamManipulation, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_LightSettings)

        __repr__ = __str__

    _M_ode.model.LightSettingsPrx = Ice.createTempClass()
    class LightSettingsPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.LightSettings._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSettings._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.LightSettings._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.LightSettings._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSettings._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.LightSettings._op_setVersion.end(self, _r)

        def getAttenuation(self, _ctx=None):
            return _M_ode.model.LightSettings._op_getAttenuation.invoke(self, ((), _ctx))

        def begin_getAttenuation(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSettings._op_getAttenuation.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAttenuation(self, _r):
            return _M_ode.model.LightSettings._op_getAttenuation.end(self, _r)

        def setAttenuation(self, theAttenuation, _ctx=None):
            return _M_ode.model.LightSettings._op_setAttenuation.invoke(self, ((theAttenuation, ), _ctx))

        def begin_setAttenuation(self, theAttenuation, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSettings._op_setAttenuation.begin(self, ((theAttenuation, ), _response, _ex, _sent, _ctx))

        def end_setAttenuation(self, _r):
            return _M_ode.model.LightSettings._op_setAttenuation.end(self, _r)

        def getWavelength(self, _ctx=None):
            return _M_ode.model.LightSettings._op_getWavelength.invoke(self, ((), _ctx))

        def begin_getWavelength(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSettings._op_getWavelength.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getWavelength(self, _r):
            return _M_ode.model.LightSettings._op_getWavelength.end(self, _r)

        def setWavelength(self, theWavelength, _ctx=None):
            return _M_ode.model.LightSettings._op_setWavelength.invoke(self, ((theWavelength, ), _ctx))

        def begin_setWavelength(self, theWavelength, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSettings._op_setWavelength.begin(self, ((theWavelength, ), _response, _ex, _sent, _ctx))

        def end_setWavelength(self, _r):
            return _M_ode.model.LightSettings._op_setWavelength.end(self, _r)

        def getLightSource(self, _ctx=None):
            return _M_ode.model.LightSettings._op_getLightSource.invoke(self, ((), _ctx))

        def begin_getLightSource(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSettings._op_getLightSource.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLightSource(self, _r):
            return _M_ode.model.LightSettings._op_getLightSource.end(self, _r)

        def setLightSource(self, theLightSource, _ctx=None):
            return _M_ode.model.LightSettings._op_setLightSource.invoke(self, ((theLightSource, ), _ctx))

        def begin_setLightSource(self, theLightSource, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSettings._op_setLightSource.begin(self, ((theLightSource, ), _response, _ex, _sent, _ctx))

        def end_setLightSource(self, _r):
            return _M_ode.model.LightSettings._op_setLightSource.end(self, _r)

        def getMicrobeamManipulation(self, _ctx=None):
            return _M_ode.model.LightSettings._op_getMicrobeamManipulation.invoke(self, ((), _ctx))

        def begin_getMicrobeamManipulation(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSettings._op_getMicrobeamManipulation.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMicrobeamManipulation(self, _r):
            return _M_ode.model.LightSettings._op_getMicrobeamManipulation.end(self, _r)

        def setMicrobeamManipulation(self, theMicrobeamManipulation, _ctx=None):
            return _M_ode.model.LightSettings._op_setMicrobeamManipulation.invoke(self, ((theMicrobeamManipulation, ), _ctx))

        def begin_setMicrobeamManipulation(self, theMicrobeamManipulation, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSettings._op_setMicrobeamManipulation.begin(self, ((theMicrobeamManipulation, ), _response, _ex, _sent, _ctx))

        def end_setMicrobeamManipulation(self, _r):
            return _M_ode.model.LightSettings._op_setMicrobeamManipulation.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.LightSettingsPrx.ice_checkedCast(proxy, '::ode::model::LightSettings', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.LightSettingsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::LightSettings'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_LightSettingsPrx = IcePy.defineProxy('::ode::model::LightSettings', LightSettingsPrx)

    _M_ode.model._t_LightSettings = IcePy.declareClass('::ode::model::LightSettings')

    _M_ode.model._t_LightSettings = IcePy.defineClass('::ode::model::LightSettings', LightSettings, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_attenuation', (), _M_ode._t_RDouble, False, 0),
        ('_wavelength', (), _M_ode.model._t_Length, False, 0),
        ('_lightSource', (), _M_ode.model._t_LightSource, False, 0),
        ('_microbeamManipulation', (), _M_ode.model._t_MicrobeamManipulation, False, 0)
    ))
    LightSettings._ice_type = _M_ode.model._t_LightSettings

    LightSettings._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    LightSettings._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    LightSettings._op_getAttenuation = IcePy.Operation('getAttenuation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    LightSettings._op_setAttenuation = IcePy.Operation('setAttenuation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    LightSettings._op_getWavelength = IcePy.Operation('getWavelength', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    LightSettings._op_setWavelength = IcePy.Operation('setWavelength', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    LightSettings._op_getLightSource = IcePy.Operation('getLightSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightSource, False, 0), ())
    LightSettings._op_setLightSource = IcePy.Operation('setLightSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSource, False, 0),), (), None, ())
    LightSettings._op_getMicrobeamManipulation = IcePy.Operation('getMicrobeamManipulation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_MicrobeamManipulation, False, 0), ())
    LightSettings._op_setMicrobeamManipulation = IcePy.Operation('setMicrobeamManipulation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_MicrobeamManipulation, False, 0),), (), None, ())

    _M_ode.model.LightSettings = LightSettings
    del LightSettings

    _M_ode.model.LightSettingsPrx = LightSettingsPrx
    del LightSettingsPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
