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

if 'ElectricPotential' not in _M_ode.model.__dict__:
    _M_ode.model._t_ElectricPotential = IcePy.declareClass('::ode::model::ElectricPotential')
    _M_ode.model._t_ElectricPotentialPrx = IcePy.declareProxy('::ode::model::ElectricPotential')

if 'Frequency' not in _M_ode.model.__dict__:
    _M_ode.model._t_Frequency = IcePy.declareClass('::ode::model::Frequency')
    _M_ode.model._t_FrequencyPrx = IcePy.declareProxy('::ode::model::Frequency')

if 'Binning' not in _M_ode.model.__dict__:
    _M_ode.model._t_Binning = IcePy.declareClass('::ode::model::Binning')
    _M_ode.model._t_BinningPrx = IcePy.declareProxy('::ode::model::Binning')

if 'Detector' not in _M_ode.model.__dict__:
    _M_ode.model._t_Detector = IcePy.declareClass('::ode::model::Detector')
    _M_ode.model._t_DetectorPrx = IcePy.declareProxy('::ode::model::Detector')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'DetectorSettings' not in _M_ode.model.__dict__:
    _M_ode.model.DetectorSettings = Ice.createTempClass()
    class DetectorSettings(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _voltage=None, _gain=None, _offsetValue=None, _readOutRate=None, _binning=None, _integration=None, _zoom=None, _detector=None):
            if Ice.getType(self) == _M_ode.model.DetectorSettings:
                raise RuntimeError('ode.model.DetectorSettings is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._voltage = _voltage
            self._gain = _gain
            self._offsetValue = _offsetValue
            self._readOutRate = _readOutRate
            self._binning = _binning
            self._integration = _integration
            self._zoom = _zoom
            self._detector = _detector

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::DetectorSettings', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::DetectorSettings'

        def ice_staticId():
            return '::ode::model::DetectorSettings'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getVoltage(self, current=None):
            pass

        def setVoltage(self, theVoltage, current=None):
            pass

        def getGain(self, current=None):
            pass

        def setGain(self, theGain, current=None):
            pass

        def getOffsetValue(self, current=None):
            pass

        def setOffsetValue(self, theOffsetValue, current=None):
            pass

        def getReadOutRate(self, current=None):
            pass

        def setReadOutRate(self, theReadOutRate, current=None):
            pass

        def getBinning(self, current=None):
            pass

        def setBinning(self, theBinning, current=None):
            pass

        def getIntegration(self, current=None):
            pass

        def setIntegration(self, theIntegration, current=None):
            pass

        def getZoom(self, current=None):
            pass

        def setZoom(self, theZoom, current=None):
            pass

        def getDetector(self, current=None):
            pass

        def setDetector(self, theDetector, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_DetectorSettings)

        __repr__ = __str__

    _M_ode.model.DetectorSettingsPrx = Ice.createTempClass()
    class DetectorSettingsPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.DetectorSettings._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.DetectorSettings._op_setVersion.end(self, _r)

        def getVoltage(self, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getVoltage.invoke(self, ((), _ctx))

        def begin_getVoltage(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getVoltage.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVoltage(self, _r):
            return _M_ode.model.DetectorSettings._op_getVoltage.end(self, _r)

        def setVoltage(self, theVoltage, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setVoltage.invoke(self, ((theVoltage, ), _ctx))

        def begin_setVoltage(self, theVoltage, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setVoltage.begin(self, ((theVoltage, ), _response, _ex, _sent, _ctx))

        def end_setVoltage(self, _r):
            return _M_ode.model.DetectorSettings._op_setVoltage.end(self, _r)

        def getGain(self, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getGain.invoke(self, ((), _ctx))

        def begin_getGain(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getGain.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getGain(self, _r):
            return _M_ode.model.DetectorSettings._op_getGain.end(self, _r)

        def setGain(self, theGain, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setGain.invoke(self, ((theGain, ), _ctx))

        def begin_setGain(self, theGain, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setGain.begin(self, ((theGain, ), _response, _ex, _sent, _ctx))

        def end_setGain(self, _r):
            return _M_ode.model.DetectorSettings._op_setGain.end(self, _r)

        def getOffsetValue(self, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getOffsetValue.invoke(self, ((), _ctx))

        def begin_getOffsetValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getOffsetValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getOffsetValue(self, _r):
            return _M_ode.model.DetectorSettings._op_getOffsetValue.end(self, _r)

        def setOffsetValue(self, theOffsetValue, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setOffsetValue.invoke(self, ((theOffsetValue, ), _ctx))

        def begin_setOffsetValue(self, theOffsetValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setOffsetValue.begin(self, ((theOffsetValue, ), _response, _ex, _sent, _ctx))

        def end_setOffsetValue(self, _r):
            return _M_ode.model.DetectorSettings._op_setOffsetValue.end(self, _r)

        def getReadOutRate(self, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getReadOutRate.invoke(self, ((), _ctx))

        def begin_getReadOutRate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getReadOutRate.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getReadOutRate(self, _r):
            return _M_ode.model.DetectorSettings._op_getReadOutRate.end(self, _r)

        def setReadOutRate(self, theReadOutRate, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setReadOutRate.invoke(self, ((theReadOutRate, ), _ctx))

        def begin_setReadOutRate(self, theReadOutRate, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setReadOutRate.begin(self, ((theReadOutRate, ), _response, _ex, _sent, _ctx))

        def end_setReadOutRate(self, _r):
            return _M_ode.model.DetectorSettings._op_setReadOutRate.end(self, _r)

        def getBinning(self, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getBinning.invoke(self, ((), _ctx))

        def begin_getBinning(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getBinning.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getBinning(self, _r):
            return _M_ode.model.DetectorSettings._op_getBinning.end(self, _r)

        def setBinning(self, theBinning, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setBinning.invoke(self, ((theBinning, ), _ctx))

        def begin_setBinning(self, theBinning, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setBinning.begin(self, ((theBinning, ), _response, _ex, _sent, _ctx))

        def end_setBinning(self, _r):
            return _M_ode.model.DetectorSettings._op_setBinning.end(self, _r)

        def getIntegration(self, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getIntegration.invoke(self, ((), _ctx))

        def begin_getIntegration(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getIntegration.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getIntegration(self, _r):
            return _M_ode.model.DetectorSettings._op_getIntegration.end(self, _r)

        def setIntegration(self, theIntegration, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setIntegration.invoke(self, ((theIntegration, ), _ctx))

        def begin_setIntegration(self, theIntegration, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setIntegration.begin(self, ((theIntegration, ), _response, _ex, _sent, _ctx))

        def end_setIntegration(self, _r):
            return _M_ode.model.DetectorSettings._op_setIntegration.end(self, _r)

        def getZoom(self, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getZoom.invoke(self, ((), _ctx))

        def begin_getZoom(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getZoom.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getZoom(self, _r):
            return _M_ode.model.DetectorSettings._op_getZoom.end(self, _r)

        def setZoom(self, theZoom, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setZoom.invoke(self, ((theZoom, ), _ctx))

        def begin_setZoom(self, theZoom, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setZoom.begin(self, ((theZoom, ), _response, _ex, _sent, _ctx))

        def end_setZoom(self, _r):
            return _M_ode.model.DetectorSettings._op_setZoom.end(self, _r)

        def getDetector(self, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getDetector.invoke(self, ((), _ctx))

        def begin_getDetector(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_getDetector.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDetector(self, _r):
            return _M_ode.model.DetectorSettings._op_getDetector.end(self, _r)

        def setDetector(self, theDetector, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setDetector.invoke(self, ((theDetector, ), _ctx))

        def begin_setDetector(self, theDetector, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DetectorSettings._op_setDetector.begin(self, ((theDetector, ), _response, _ex, _sent, _ctx))

        def end_setDetector(self, _r):
            return _M_ode.model.DetectorSettings._op_setDetector.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.DetectorSettingsPrx.ice_checkedCast(proxy, '::ode::model::DetectorSettings', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.DetectorSettingsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::DetectorSettings'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_DetectorSettingsPrx = IcePy.defineProxy('::ode::model::DetectorSettings', DetectorSettingsPrx)

    _M_ode.model._t_DetectorSettings = IcePy.declareClass('::ode::model::DetectorSettings')

    _M_ode.model._t_DetectorSettings = IcePy.defineClass('::ode::model::DetectorSettings', DetectorSettings, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_voltage', (), _M_ode.model._t_ElectricPotential, False, 0),
        ('_gain', (), _M_ode._t_RDouble, False, 0),
        ('_offsetValue', (), _M_ode._t_RDouble, False, 0),
        ('_readOutRate', (), _M_ode.model._t_Frequency, False, 0),
        ('_binning', (), _M_ode.model._t_Binning, False, 0),
        ('_integration', (), _M_ode._t_RInt, False, 0),
        ('_zoom', (), _M_ode._t_RDouble, False, 0),
        ('_detector', (), _M_ode.model._t_Detector, False, 0)
    ))
    DetectorSettings._ice_type = _M_ode.model._t_DetectorSettings

    DetectorSettings._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    DetectorSettings._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    DetectorSettings._op_getVoltage = IcePy.Operation('getVoltage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ElectricPotential, False, 0), ())
    DetectorSettings._op_setVoltage = IcePy.Operation('setVoltage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ElectricPotential, False, 0),), (), None, ())
    DetectorSettings._op_getGain = IcePy.Operation('getGain', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    DetectorSettings._op_setGain = IcePy.Operation('setGain', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    DetectorSettings._op_getOffsetValue = IcePy.Operation('getOffsetValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    DetectorSettings._op_setOffsetValue = IcePy.Operation('setOffsetValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    DetectorSettings._op_getReadOutRate = IcePy.Operation('getReadOutRate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Frequency, False, 0), ())
    DetectorSettings._op_setReadOutRate = IcePy.Operation('setReadOutRate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Frequency, False, 0),), (), None, ())
    DetectorSettings._op_getBinning = IcePy.Operation('getBinning', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Binning, False, 0), ())
    DetectorSettings._op_setBinning = IcePy.Operation('setBinning', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Binning, False, 0),), (), None, ())
    DetectorSettings._op_getIntegration = IcePy.Operation('getIntegration', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    DetectorSettings._op_setIntegration = IcePy.Operation('setIntegration', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    DetectorSettings._op_getZoom = IcePy.Operation('getZoom', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    DetectorSettings._op_setZoom = IcePy.Operation('setZoom', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    DetectorSettings._op_getDetector = IcePy.Operation('getDetector', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Detector, False, 0), ())
    DetectorSettings._op_setDetector = IcePy.Operation('setDetector', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Detector, False, 0),), (), None, ())

    _M_ode.model.DetectorSettings = DetectorSettings
    del DetectorSettings

    _M_ode.model.DetectorSettingsPrx = DetectorSettingsPrx
    del DetectorSettingsPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
