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
import ode_model_LightSource_ice

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

if 'LaserType' not in _M_ode.model.__dict__:
    _M_ode.model._t_LaserType = IcePy.declareClass('::ode::model::LaserType')
    _M_ode.model._t_LaserTypePrx = IcePy.declareProxy('::ode::model::LaserType')

if 'LaserMedium' not in _M_ode.model.__dict__:
    _M_ode.model._t_LaserMedium = IcePy.declareClass('::ode::model::LaserMedium')
    _M_ode.model._t_LaserMediumPrx = IcePy.declareProxy('::ode::model::LaserMedium')

if 'Pulse' not in _M_ode.model.__dict__:
    _M_ode.model._t_Pulse = IcePy.declareClass('::ode::model::Pulse')
    _M_ode.model._t_PulsePrx = IcePy.declareProxy('::ode::model::Pulse')

if 'Length' not in _M_ode.model.__dict__:
    _M_ode.model._t_Length = IcePy.declareClass('::ode::model::Length')
    _M_ode.model._t_LengthPrx = IcePy.declareProxy('::ode::model::Length')

if 'LightSource' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightSource = IcePy.declareClass('::ode::model::LightSource')
    _M_ode.model._t_LightSourcePrx = IcePy.declareProxy('::ode::model::LightSource')

if 'Frequency' not in _M_ode.model.__dict__:
    _M_ode.model._t_Frequency = IcePy.declareClass('::ode::model::Frequency')
    _M_ode.model._t_FrequencyPrx = IcePy.declareProxy('::ode::model::Frequency')

if 'Power' not in _M_ode.model.__dict__:
    _M_ode.model._t_Power = IcePy.declareClass('::ode::model::Power')
    _M_ode.model._t_PowerPrx = IcePy.declareProxy('::ode::model::Power')

if 'Instrument' not in _M_ode.model.__dict__:
    _M_ode.model._t_Instrument = IcePy.declareClass('::ode::model::Instrument')
    _M_ode.model._t_InstrumentPrx = IcePy.declareProxy('::ode::model::Instrument')

if 'LightSourceAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightSourceAnnotationLink = IcePy.declareClass('::ode::model::LightSourceAnnotationLink')
    _M_ode.model._t_LightSourceAnnotationLinkPrx = IcePy.declareProxy('::ode::model::LightSourceAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'Laser' not in _M_ode.model.__dict__:
    _M_ode.model.Laser = Ice.createTempClass()
    class Laser(_M_ode.model.LightSource):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _manufacturer=None, _model=None, _power=None, _lotNumber=None, _serialNumber=None, _instrument=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _type=None, _laserMedium=None, _frequencyMultiplication=None, _tuneable=None, _pulse=None, _wavelength=None, _pockelCell=None, _pump=None, _repetitionRate=None):
            if Ice.getType(self) == _M_ode.model.Laser:
                raise RuntimeError('ode.model.Laser is an abstract class')
            _M_ode.model.LightSource.__init__(self, _id, _details, _loaded, _version, _manufacturer, _model, _power, _lotNumber, _serialNumber, _instrument, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._type = _type
            self._laserMedium = _laserMedium
            self._frequencyMultiplication = _frequencyMultiplication
            self._tuneable = _tuneable
            self._pulse = _pulse
            self._wavelength = _wavelength
            self._pockelCell = _pockelCell
            self._pump = _pump
            self._repetitionRate = _repetitionRate

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Laser', '::ode::model::LightSource')

        def ice_id(self, current=None):
            return '::ode::model::Laser'

        def ice_staticId():
            return '::ode::model::Laser'
        ice_staticId = staticmethod(ice_staticId)

        def getType(self, current=None):
            pass

        def setType(self, theType, current=None):
            pass

        def getLaserMedium(self, current=None):
            pass

        def setLaserMedium(self, theLaserMedium, current=None):
            pass

        def getFrequencyMultiplication(self, current=None):
            pass

        def setFrequencyMultiplication(self, theFrequencyMultiplication, current=None):
            pass

        def getTuneable(self, current=None):
            pass

        def setTuneable(self, theTuneable, current=None):
            pass

        def getPulse(self, current=None):
            pass

        def setPulse(self, thePulse, current=None):
            pass

        def getWavelength(self, current=None):
            pass

        def setWavelength(self, theWavelength, current=None):
            pass

        def getPockelCell(self, current=None):
            pass

        def setPockelCell(self, thePockelCell, current=None):
            pass

        def getPump(self, current=None):
            pass

        def setPump(self, thePump, current=None):
            pass

        def getRepetitionRate(self, current=None):
            pass

        def setRepetitionRate(self, theRepetitionRate, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Laser)

        __repr__ = __str__

    _M_ode.model.LaserPrx = Ice.createTempClass()
    class LaserPrx(_M_ode.model.LightSourcePrx):

        def getType(self, _ctx=None):
            return _M_ode.model.Laser._op_getType.invoke(self, ((), _ctx))

        def begin_getType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_getType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getType(self, _r):
            return _M_ode.model.Laser._op_getType.end(self, _r)

        def setType(self, theType, _ctx=None):
            return _M_ode.model.Laser._op_setType.invoke(self, ((theType, ), _ctx))

        def begin_setType(self, theType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_setType.begin(self, ((theType, ), _response, _ex, _sent, _ctx))

        def end_setType(self, _r):
            return _M_ode.model.Laser._op_setType.end(self, _r)

        def getLaserMedium(self, _ctx=None):
            return _M_ode.model.Laser._op_getLaserMedium.invoke(self, ((), _ctx))

        def begin_getLaserMedium(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_getLaserMedium.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLaserMedium(self, _r):
            return _M_ode.model.Laser._op_getLaserMedium.end(self, _r)

        def setLaserMedium(self, theLaserMedium, _ctx=None):
            return _M_ode.model.Laser._op_setLaserMedium.invoke(self, ((theLaserMedium, ), _ctx))

        def begin_setLaserMedium(self, theLaserMedium, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_setLaserMedium.begin(self, ((theLaserMedium, ), _response, _ex, _sent, _ctx))

        def end_setLaserMedium(self, _r):
            return _M_ode.model.Laser._op_setLaserMedium.end(self, _r)

        def getFrequencyMultiplication(self, _ctx=None):
            return _M_ode.model.Laser._op_getFrequencyMultiplication.invoke(self, ((), _ctx))

        def begin_getFrequencyMultiplication(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_getFrequencyMultiplication.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFrequencyMultiplication(self, _r):
            return _M_ode.model.Laser._op_getFrequencyMultiplication.end(self, _r)

        def setFrequencyMultiplication(self, theFrequencyMultiplication, _ctx=None):
            return _M_ode.model.Laser._op_setFrequencyMultiplication.invoke(self, ((theFrequencyMultiplication, ), _ctx))

        def begin_setFrequencyMultiplication(self, theFrequencyMultiplication, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_setFrequencyMultiplication.begin(self, ((theFrequencyMultiplication, ), _response, _ex, _sent, _ctx))

        def end_setFrequencyMultiplication(self, _r):
            return _M_ode.model.Laser._op_setFrequencyMultiplication.end(self, _r)

        def getTuneable(self, _ctx=None):
            return _M_ode.model.Laser._op_getTuneable.invoke(self, ((), _ctx))

        def begin_getTuneable(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_getTuneable.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTuneable(self, _r):
            return _M_ode.model.Laser._op_getTuneable.end(self, _r)

        def setTuneable(self, theTuneable, _ctx=None):
            return _M_ode.model.Laser._op_setTuneable.invoke(self, ((theTuneable, ), _ctx))

        def begin_setTuneable(self, theTuneable, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_setTuneable.begin(self, ((theTuneable, ), _response, _ex, _sent, _ctx))

        def end_setTuneable(self, _r):
            return _M_ode.model.Laser._op_setTuneable.end(self, _r)

        def getPulse(self, _ctx=None):
            return _M_ode.model.Laser._op_getPulse.invoke(self, ((), _ctx))

        def begin_getPulse(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_getPulse.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPulse(self, _r):
            return _M_ode.model.Laser._op_getPulse.end(self, _r)

        def setPulse(self, thePulse, _ctx=None):
            return _M_ode.model.Laser._op_setPulse.invoke(self, ((thePulse, ), _ctx))

        def begin_setPulse(self, thePulse, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_setPulse.begin(self, ((thePulse, ), _response, _ex, _sent, _ctx))

        def end_setPulse(self, _r):
            return _M_ode.model.Laser._op_setPulse.end(self, _r)

        def getWavelength(self, _ctx=None):
            return _M_ode.model.Laser._op_getWavelength.invoke(self, ((), _ctx))

        def begin_getWavelength(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_getWavelength.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getWavelength(self, _r):
            return _M_ode.model.Laser._op_getWavelength.end(self, _r)

        def setWavelength(self, theWavelength, _ctx=None):
            return _M_ode.model.Laser._op_setWavelength.invoke(self, ((theWavelength, ), _ctx))

        def begin_setWavelength(self, theWavelength, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_setWavelength.begin(self, ((theWavelength, ), _response, _ex, _sent, _ctx))

        def end_setWavelength(self, _r):
            return _M_ode.model.Laser._op_setWavelength.end(self, _r)

        def getPockelCell(self, _ctx=None):
            return _M_ode.model.Laser._op_getPockelCell.invoke(self, ((), _ctx))

        def begin_getPockelCell(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_getPockelCell.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPockelCell(self, _r):
            return _M_ode.model.Laser._op_getPockelCell.end(self, _r)

        def setPockelCell(self, thePockelCell, _ctx=None):
            return _M_ode.model.Laser._op_setPockelCell.invoke(self, ((thePockelCell, ), _ctx))

        def begin_setPockelCell(self, thePockelCell, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_setPockelCell.begin(self, ((thePockelCell, ), _response, _ex, _sent, _ctx))

        def end_setPockelCell(self, _r):
            return _M_ode.model.Laser._op_setPockelCell.end(self, _r)

        def getPump(self, _ctx=None):
            return _M_ode.model.Laser._op_getPump.invoke(self, ((), _ctx))

        def begin_getPump(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_getPump.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPump(self, _r):
            return _M_ode.model.Laser._op_getPump.end(self, _r)

        def setPump(self, thePump, _ctx=None):
            return _M_ode.model.Laser._op_setPump.invoke(self, ((thePump, ), _ctx))

        def begin_setPump(self, thePump, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_setPump.begin(self, ((thePump, ), _response, _ex, _sent, _ctx))

        def end_setPump(self, _r):
            return _M_ode.model.Laser._op_setPump.end(self, _r)

        def getRepetitionRate(self, _ctx=None):
            return _M_ode.model.Laser._op_getRepetitionRate.invoke(self, ((), _ctx))

        def begin_getRepetitionRate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_getRepetitionRate.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRepetitionRate(self, _r):
            return _M_ode.model.Laser._op_getRepetitionRate.end(self, _r)

        def setRepetitionRate(self, theRepetitionRate, _ctx=None):
            return _M_ode.model.Laser._op_setRepetitionRate.invoke(self, ((theRepetitionRate, ), _ctx))

        def begin_setRepetitionRate(self, theRepetitionRate, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Laser._op_setRepetitionRate.begin(self, ((theRepetitionRate, ), _response, _ex, _sent, _ctx))

        def end_setRepetitionRate(self, _r):
            return _M_ode.model.Laser._op_setRepetitionRate.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.LaserPrx.ice_checkedCast(proxy, '::ode::model::Laser', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.LaserPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Laser'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_LaserPrx = IcePy.defineProxy('::ode::model::Laser', LaserPrx)

    _M_ode.model._t_Laser = IcePy.declareClass('::ode::model::Laser')

    _M_ode.model._t_Laser = IcePy.defineClass('::ode::model::Laser', Laser, -1, (), True, False, _M_ode.model._t_LightSource, (), (
        ('_type', (), _M_ode.model._t_LaserType, False, 0),
        ('_laserMedium', (), _M_ode.model._t_LaserMedium, False, 0),
        ('_frequencyMultiplication', (), _M_ode._t_RInt, False, 0),
        ('_tuneable', (), _M_ode._t_RBool, False, 0),
        ('_pulse', (), _M_ode.model._t_Pulse, False, 0),
        ('_wavelength', (), _M_ode.model._t_Length, False, 0),
        ('_pockelCell', (), _M_ode._t_RBool, False, 0),
        ('_pump', (), _M_ode.model._t_LightSource, False, 0),
        ('_repetitionRate', (), _M_ode.model._t_Frequency, False, 0)
    ))
    Laser._ice_type = _M_ode.model._t_Laser

    Laser._op_getType = IcePy.Operation('getType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LaserType, False, 0), ())
    Laser._op_setType = IcePy.Operation('setType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LaserType, False, 0),), (), None, ())
    Laser._op_getLaserMedium = IcePy.Operation('getLaserMedium', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LaserMedium, False, 0), ())
    Laser._op_setLaserMedium = IcePy.Operation('setLaserMedium', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LaserMedium, False, 0),), (), None, ())
    Laser._op_getFrequencyMultiplication = IcePy.Operation('getFrequencyMultiplication', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Laser._op_setFrequencyMultiplication = IcePy.Operation('setFrequencyMultiplication', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Laser._op_getTuneable = IcePy.Operation('getTuneable', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    Laser._op_setTuneable = IcePy.Operation('setTuneable', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    Laser._op_getPulse = IcePy.Operation('getPulse', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Pulse, False, 0), ())
    Laser._op_setPulse = IcePy.Operation('setPulse', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pulse, False, 0),), (), None, ())
    Laser._op_getWavelength = IcePy.Operation('getWavelength', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    Laser._op_setWavelength = IcePy.Operation('setWavelength', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    Laser._op_getPockelCell = IcePy.Operation('getPockelCell', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    Laser._op_setPockelCell = IcePy.Operation('setPockelCell', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    Laser._op_getPump = IcePy.Operation('getPump', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightSource, False, 0), ())
    Laser._op_setPump = IcePy.Operation('setPump', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSource, False, 0),), (), None, ())
    Laser._op_getRepetitionRate = IcePy.Operation('getRepetitionRate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Frequency, False, 0), ())
    Laser._op_setRepetitionRate = IcePy.Operation('setRepetitionRate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Frequency, False, 0),), (), None, ())

    _M_ode.model.Laser = Laser
    del Laser

    _M_ode.model.LaserPrx = LaserPrx
    del LaserPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
