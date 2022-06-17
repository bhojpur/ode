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

if 'Illumination' not in _M_ode.model.__dict__:
    _M_ode.model._t_Illumination = IcePy.declareClass('::ode::model::Illumination')
    _M_ode.model._t_IlluminationPrx = IcePy.declareProxy('::ode::model::Illumination')

if 'ContrastMethod' not in _M_ode.model.__dict__:
    _M_ode.model._t_ContrastMethod = IcePy.declareClass('::ode::model::ContrastMethod')
    _M_ode.model._t_ContrastMethodPrx = IcePy.declareProxy('::ode::model::ContrastMethod')

if 'OTF' not in _M_ode.model.__dict__:
    _M_ode.model._t_OTF = IcePy.declareClass('::ode::model::OTF')
    _M_ode.model._t_OTFPrx = IcePy.declareProxy('::ode::model::OTF')

if 'DetectorSettings' not in _M_ode.model.__dict__:
    _M_ode.model._t_DetectorSettings = IcePy.declareClass('::ode::model::DetectorSettings')
    _M_ode.model._t_DetectorSettingsPrx = IcePy.declareProxy('::ode::model::DetectorSettings')

if 'LightSettings' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightSettings = IcePy.declareClass('::ode::model::LightSettings')
    _M_ode.model._t_LightSettingsPrx = IcePy.declareProxy('::ode::model::LightSettings')

if 'FilterSet' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilterSet = IcePy.declareClass('::ode::model::FilterSet')
    _M_ode.model._t_FilterSetPrx = IcePy.declareProxy('::ode::model::FilterSet')

if 'PhotometricInterpretation' not in _M_ode.model.__dict__:
    _M_ode.model._t_PhotometricInterpretation = IcePy.declareClass('::ode::model::PhotometricInterpretation')
    _M_ode.model._t_PhotometricInterpretationPrx = IcePy.declareProxy('::ode::model::PhotometricInterpretation')

if 'AcquisitionMode' not in _M_ode.model.__dict__:
    _M_ode.model._t_AcquisitionMode = IcePy.declareClass('::ode::model::AcquisitionMode')
    _M_ode.model._t_AcquisitionModePrx = IcePy.declareProxy('::ode::model::AcquisitionMode')

if 'Channel' not in _M_ode.model.__dict__:
    _M_ode.model._t_Channel = IcePy.declareClass('::ode::model::Channel')
    _M_ode.model._t_ChannelPrx = IcePy.declareProxy('::ode::model::Channel')

if 'LightPath' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightPath = IcePy.declareClass('::ode::model::LightPath')
    _M_ode.model._t_LightPathPrx = IcePy.declareProxy('::ode::model::LightPath')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_LogicalChannelChannelsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_LogicalChannelChannelsSeq = IcePy.defineSequence('::ode::model::LogicalChannelChannelsSeq', (), _M_ode.model._t_Channel)

if 'LogicalChannel' not in _M_ode.model.__dict__:
    _M_ode.model.LogicalChannel = Ice.createTempClass()
    class LogicalChannel(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _name=None, _pinHoleSize=None, _illumination=None, _contrastMethod=None, _excitationWave=None, _emissionWave=None, _fluor=None, _ndFilter=None, _otf=None, _detectorSettings=None, _lightSourceSettings=None, _filterSet=None, _samplesPerPixel=None, _photometricInterpretation=None, _mode=None, _pockelCellSetting=None, _channelsSeq=None, _channelsLoaded=False, _lightPath=None):
            if Ice.getType(self) == _M_ode.model.LogicalChannel:
                raise RuntimeError('ode.model.LogicalChannel is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._name = _name
            self._pinHoleSize = _pinHoleSize
            self._illumination = _illumination
            self._contrastMethod = _contrastMethod
            self._excitationWave = _excitationWave
            self._emissionWave = _emissionWave
            self._fluor = _fluor
            self._ndFilter = _ndFilter
            self._otf = _otf
            self._detectorSettings = _detectorSettings
            self._lightSourceSettings = _lightSourceSettings
            self._filterSet = _filterSet
            self._samplesPerPixel = _samplesPerPixel
            self._photometricInterpretation = _photometricInterpretation
            self._mode = _mode
            self._pockelCellSetting = _pockelCellSetting
            self._channelsSeq = _channelsSeq
            self._channelsLoaded = _channelsLoaded
            self._lightPath = _lightPath

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::LogicalChannel')

        def ice_id(self, current=None):
            return '::ode::model::LogicalChannel'

        def ice_staticId():
            return '::ode::model::LogicalChannel'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getName(self, current=None):
            pass

        def setName(self, theName, current=None):
            pass

        def getPinHoleSize(self, current=None):
            pass

        def setPinHoleSize(self, thePinHoleSize, current=None):
            pass

        def getIllumination(self, current=None):
            pass

        def setIllumination(self, theIllumination, current=None):
            pass

        def getContrastMethod(self, current=None):
            pass

        def setContrastMethod(self, theContrastMethod, current=None):
            pass

        def getExcitationWave(self, current=None):
            pass

        def setExcitationWave(self, theExcitationWave, current=None):
            pass

        def getEmissionWave(self, current=None):
            pass

        def setEmissionWave(self, theEmissionWave, current=None):
            pass

        def getFluor(self, current=None):
            pass

        def setFluor(self, theFluor, current=None):
            pass

        def getNdFilter(self, current=None):
            pass

        def setNdFilter(self, theNdFilter, current=None):
            pass

        def getOtf(self, current=None):
            pass

        def setOtf(self, theOtf, current=None):
            pass

        def getDetectorSettings(self, current=None):
            pass

        def setDetectorSettings(self, theDetectorSettings, current=None):
            pass

        def getLightSourceSettings(self, current=None):
            pass

        def setLightSourceSettings(self, theLightSourceSettings, current=None):
            pass

        def getFilterSet(self, current=None):
            pass

        def setFilterSet(self, theFilterSet, current=None):
            pass

        def getSamplesPerPixel(self, current=None):
            pass

        def setSamplesPerPixel(self, theSamplesPerPixel, current=None):
            pass

        def getPhotometricInterpretation(self, current=None):
            pass

        def setPhotometricInterpretation(self, thePhotometricInterpretation, current=None):
            pass

        def getMode(self, current=None):
            pass

        def setMode(self, theMode, current=None):
            pass

        def getPockelCellSetting(self, current=None):
            pass

        def setPockelCellSetting(self, thePockelCellSetting, current=None):
            pass

        def unloadChannels(self, current=None):
            pass

        def sizeOfChannels(self, current=None):
            pass

        def copyChannels(self, current=None):
            pass

        def addChannel(self, target, current=None):
            pass

        def addAllChannelSet(self, targets, current=None):
            pass

        def removeChannel(self, theTarget, current=None):
            pass

        def removeAllChannelSet(self, targets, current=None):
            pass

        def clearChannels(self, current=None):
            pass

        def reloadChannels(self, toCopy, current=None):
            pass

        def getLightPath(self, current=None):
            pass

        def setLightPath(self, theLightPath, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_LogicalChannel)

        __repr__ = __str__

    _M_ode.model.LogicalChannelPrx = Ice.createTempClass()
    class LogicalChannelPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.LogicalChannel._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.LogicalChannel._op_setVersion.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.LogicalChannel._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.LogicalChannel._op_setName.end(self, _r)

        def getPinHoleSize(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getPinHoleSize.invoke(self, ((), _ctx))

        def begin_getPinHoleSize(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getPinHoleSize.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPinHoleSize(self, _r):
            return _M_ode.model.LogicalChannel._op_getPinHoleSize.end(self, _r)

        def setPinHoleSize(self, thePinHoleSize, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setPinHoleSize.invoke(self, ((thePinHoleSize, ), _ctx))

        def begin_setPinHoleSize(self, thePinHoleSize, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setPinHoleSize.begin(self, ((thePinHoleSize, ), _response, _ex, _sent, _ctx))

        def end_setPinHoleSize(self, _r):
            return _M_ode.model.LogicalChannel._op_setPinHoleSize.end(self, _r)

        def getIllumination(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getIllumination.invoke(self, ((), _ctx))

        def begin_getIllumination(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getIllumination.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getIllumination(self, _r):
            return _M_ode.model.LogicalChannel._op_getIllumination.end(self, _r)

        def setIllumination(self, theIllumination, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setIllumination.invoke(self, ((theIllumination, ), _ctx))

        def begin_setIllumination(self, theIllumination, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setIllumination.begin(self, ((theIllumination, ), _response, _ex, _sent, _ctx))

        def end_setIllumination(self, _r):
            return _M_ode.model.LogicalChannel._op_setIllumination.end(self, _r)

        def getContrastMethod(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getContrastMethod.invoke(self, ((), _ctx))

        def begin_getContrastMethod(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getContrastMethod.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getContrastMethod(self, _r):
            return _M_ode.model.LogicalChannel._op_getContrastMethod.end(self, _r)

        def setContrastMethod(self, theContrastMethod, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setContrastMethod.invoke(self, ((theContrastMethod, ), _ctx))

        def begin_setContrastMethod(self, theContrastMethod, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setContrastMethod.begin(self, ((theContrastMethod, ), _response, _ex, _sent, _ctx))

        def end_setContrastMethod(self, _r):
            return _M_ode.model.LogicalChannel._op_setContrastMethod.end(self, _r)

        def getExcitationWave(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getExcitationWave.invoke(self, ((), _ctx))

        def begin_getExcitationWave(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getExcitationWave.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getExcitationWave(self, _r):
            return _M_ode.model.LogicalChannel._op_getExcitationWave.end(self, _r)

        def setExcitationWave(self, theExcitationWave, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setExcitationWave.invoke(self, ((theExcitationWave, ), _ctx))

        def begin_setExcitationWave(self, theExcitationWave, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setExcitationWave.begin(self, ((theExcitationWave, ), _response, _ex, _sent, _ctx))

        def end_setExcitationWave(self, _r):
            return _M_ode.model.LogicalChannel._op_setExcitationWave.end(self, _r)

        def getEmissionWave(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getEmissionWave.invoke(self, ((), _ctx))

        def begin_getEmissionWave(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getEmissionWave.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEmissionWave(self, _r):
            return _M_ode.model.LogicalChannel._op_getEmissionWave.end(self, _r)

        def setEmissionWave(self, theEmissionWave, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setEmissionWave.invoke(self, ((theEmissionWave, ), _ctx))

        def begin_setEmissionWave(self, theEmissionWave, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setEmissionWave.begin(self, ((theEmissionWave, ), _response, _ex, _sent, _ctx))

        def end_setEmissionWave(self, _r):
            return _M_ode.model.LogicalChannel._op_setEmissionWave.end(self, _r)

        def getFluor(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getFluor.invoke(self, ((), _ctx))

        def begin_getFluor(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getFluor.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFluor(self, _r):
            return _M_ode.model.LogicalChannel._op_getFluor.end(self, _r)

        def setFluor(self, theFluor, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setFluor.invoke(self, ((theFluor, ), _ctx))

        def begin_setFluor(self, theFluor, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setFluor.begin(self, ((theFluor, ), _response, _ex, _sent, _ctx))

        def end_setFluor(self, _r):
            return _M_ode.model.LogicalChannel._op_setFluor.end(self, _r)

        def getNdFilter(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getNdFilter.invoke(self, ((), _ctx))

        def begin_getNdFilter(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getNdFilter.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getNdFilter(self, _r):
            return _M_ode.model.LogicalChannel._op_getNdFilter.end(self, _r)

        def setNdFilter(self, theNdFilter, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setNdFilter.invoke(self, ((theNdFilter, ), _ctx))

        def begin_setNdFilter(self, theNdFilter, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setNdFilter.begin(self, ((theNdFilter, ), _response, _ex, _sent, _ctx))

        def end_setNdFilter(self, _r):
            return _M_ode.model.LogicalChannel._op_setNdFilter.end(self, _r)

        def getOtf(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getOtf.invoke(self, ((), _ctx))

        def begin_getOtf(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getOtf.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getOtf(self, _r):
            return _M_ode.model.LogicalChannel._op_getOtf.end(self, _r)

        def setOtf(self, theOtf, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setOtf.invoke(self, ((theOtf, ), _ctx))

        def begin_setOtf(self, theOtf, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setOtf.begin(self, ((theOtf, ), _response, _ex, _sent, _ctx))

        def end_setOtf(self, _r):
            return _M_ode.model.LogicalChannel._op_setOtf.end(self, _r)

        def getDetectorSettings(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getDetectorSettings.invoke(self, ((), _ctx))

        def begin_getDetectorSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getDetectorSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDetectorSettings(self, _r):
            return _M_ode.model.LogicalChannel._op_getDetectorSettings.end(self, _r)

        def setDetectorSettings(self, theDetectorSettings, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setDetectorSettings.invoke(self, ((theDetectorSettings, ), _ctx))

        def begin_setDetectorSettings(self, theDetectorSettings, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setDetectorSettings.begin(self, ((theDetectorSettings, ), _response, _ex, _sent, _ctx))

        def end_setDetectorSettings(self, _r):
            return _M_ode.model.LogicalChannel._op_setDetectorSettings.end(self, _r)

        def getLightSourceSettings(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getLightSourceSettings.invoke(self, ((), _ctx))

        def begin_getLightSourceSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getLightSourceSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLightSourceSettings(self, _r):
            return _M_ode.model.LogicalChannel._op_getLightSourceSettings.end(self, _r)

        def setLightSourceSettings(self, theLightSourceSettings, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setLightSourceSettings.invoke(self, ((theLightSourceSettings, ), _ctx))

        def begin_setLightSourceSettings(self, theLightSourceSettings, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setLightSourceSettings.begin(self, ((theLightSourceSettings, ), _response, _ex, _sent, _ctx))

        def end_setLightSourceSettings(self, _r):
            return _M_ode.model.LogicalChannel._op_setLightSourceSettings.end(self, _r)

        def getFilterSet(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getFilterSet.invoke(self, ((), _ctx))

        def begin_getFilterSet(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getFilterSet.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFilterSet(self, _r):
            return _M_ode.model.LogicalChannel._op_getFilterSet.end(self, _r)

        def setFilterSet(self, theFilterSet, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setFilterSet.invoke(self, ((theFilterSet, ), _ctx))

        def begin_setFilterSet(self, theFilterSet, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setFilterSet.begin(self, ((theFilterSet, ), _response, _ex, _sent, _ctx))

        def end_setFilterSet(self, _r):
            return _M_ode.model.LogicalChannel._op_setFilterSet.end(self, _r)

        def getSamplesPerPixel(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getSamplesPerPixel.invoke(self, ((), _ctx))

        def begin_getSamplesPerPixel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getSamplesPerPixel.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSamplesPerPixel(self, _r):
            return _M_ode.model.LogicalChannel._op_getSamplesPerPixel.end(self, _r)

        def setSamplesPerPixel(self, theSamplesPerPixel, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setSamplesPerPixel.invoke(self, ((theSamplesPerPixel, ), _ctx))

        def begin_setSamplesPerPixel(self, theSamplesPerPixel, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setSamplesPerPixel.begin(self, ((theSamplesPerPixel, ), _response, _ex, _sent, _ctx))

        def end_setSamplesPerPixel(self, _r):
            return _M_ode.model.LogicalChannel._op_setSamplesPerPixel.end(self, _r)

        def getPhotometricInterpretation(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getPhotometricInterpretation.invoke(self, ((), _ctx))

        def begin_getPhotometricInterpretation(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getPhotometricInterpretation.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPhotometricInterpretation(self, _r):
            return _M_ode.model.LogicalChannel._op_getPhotometricInterpretation.end(self, _r)

        def setPhotometricInterpretation(self, thePhotometricInterpretation, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setPhotometricInterpretation.invoke(self, ((thePhotometricInterpretation, ), _ctx))

        def begin_setPhotometricInterpretation(self, thePhotometricInterpretation, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setPhotometricInterpretation.begin(self, ((thePhotometricInterpretation, ), _response, _ex, _sent, _ctx))

        def end_setPhotometricInterpretation(self, _r):
            return _M_ode.model.LogicalChannel._op_setPhotometricInterpretation.end(self, _r)

        def getMode(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getMode.invoke(self, ((), _ctx))

        def begin_getMode(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getMode.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMode(self, _r):
            return _M_ode.model.LogicalChannel._op_getMode.end(self, _r)

        def setMode(self, theMode, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setMode.invoke(self, ((theMode, ), _ctx))

        def begin_setMode(self, theMode, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setMode.begin(self, ((theMode, ), _response, _ex, _sent, _ctx))

        def end_setMode(self, _r):
            return _M_ode.model.LogicalChannel._op_setMode.end(self, _r)

        def getPockelCellSetting(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getPockelCellSetting.invoke(self, ((), _ctx))

        def begin_getPockelCellSetting(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getPockelCellSetting.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPockelCellSetting(self, _r):
            return _M_ode.model.LogicalChannel._op_getPockelCellSetting.end(self, _r)

        def setPockelCellSetting(self, thePockelCellSetting, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setPockelCellSetting.invoke(self, ((thePockelCellSetting, ), _ctx))

        def begin_setPockelCellSetting(self, thePockelCellSetting, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setPockelCellSetting.begin(self, ((thePockelCellSetting, ), _response, _ex, _sent, _ctx))

        def end_setPockelCellSetting(self, _r):
            return _M_ode.model.LogicalChannel._op_setPockelCellSetting.end(self, _r)

        def unloadChannels(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_unloadChannels.invoke(self, ((), _ctx))

        def begin_unloadChannels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_unloadChannels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadChannels(self, _r):
            return _M_ode.model.LogicalChannel._op_unloadChannels.end(self, _r)

        def sizeOfChannels(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_sizeOfChannels.invoke(self, ((), _ctx))

        def begin_sizeOfChannels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_sizeOfChannels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfChannels(self, _r):
            return _M_ode.model.LogicalChannel._op_sizeOfChannels.end(self, _r)

        def copyChannels(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_copyChannels.invoke(self, ((), _ctx))

        def begin_copyChannels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_copyChannels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyChannels(self, _r):
            return _M_ode.model.LogicalChannel._op_copyChannels.end(self, _r)

        def addChannel(self, target, _ctx=None):
            return _M_ode.model.LogicalChannel._op_addChannel.invoke(self, ((target, ), _ctx))

        def begin_addChannel(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_addChannel.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addChannel(self, _r):
            return _M_ode.model.LogicalChannel._op_addChannel.end(self, _r)

        def addAllChannelSet(self, targets, _ctx=None):
            return _M_ode.model.LogicalChannel._op_addAllChannelSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllChannelSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_addAllChannelSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllChannelSet(self, _r):
            return _M_ode.model.LogicalChannel._op_addAllChannelSet.end(self, _r)

        def removeChannel(self, theTarget, _ctx=None):
            return _M_ode.model.LogicalChannel._op_removeChannel.invoke(self, ((theTarget, ), _ctx))

        def begin_removeChannel(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_removeChannel.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeChannel(self, _r):
            return _M_ode.model.LogicalChannel._op_removeChannel.end(self, _r)

        def removeAllChannelSet(self, targets, _ctx=None):
            return _M_ode.model.LogicalChannel._op_removeAllChannelSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllChannelSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_removeAllChannelSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllChannelSet(self, _r):
            return _M_ode.model.LogicalChannel._op_removeAllChannelSet.end(self, _r)

        def clearChannels(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_clearChannels.invoke(self, ((), _ctx))

        def begin_clearChannels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_clearChannels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearChannels(self, _r):
            return _M_ode.model.LogicalChannel._op_clearChannels.end(self, _r)

        def reloadChannels(self, toCopy, _ctx=None):
            return _M_ode.model.LogicalChannel._op_reloadChannels.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadChannels(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_reloadChannels.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadChannels(self, _r):
            return _M_ode.model.LogicalChannel._op_reloadChannels.end(self, _r)

        def getLightPath(self, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getLightPath.invoke(self, ((), _ctx))

        def begin_getLightPath(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_getLightPath.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLightPath(self, _r):
            return _M_ode.model.LogicalChannel._op_getLightPath.end(self, _r)

        def setLightPath(self, theLightPath, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setLightPath.invoke(self, ((theLightPath, ), _ctx))

        def begin_setLightPath(self, theLightPath, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LogicalChannel._op_setLightPath.begin(self, ((theLightPath, ), _response, _ex, _sent, _ctx))

        def end_setLightPath(self, _r):
            return _M_ode.model.LogicalChannel._op_setLightPath.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.LogicalChannelPrx.ice_checkedCast(proxy, '::ode::model::LogicalChannel', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.LogicalChannelPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::LogicalChannel'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_LogicalChannelPrx = IcePy.defineProxy('::ode::model::LogicalChannel', LogicalChannelPrx)

    _M_ode.model._t_LogicalChannel = IcePy.declareClass('::ode::model::LogicalChannel')

    _M_ode.model._t_LogicalChannel = IcePy.defineClass('::ode::model::LogicalChannel', LogicalChannel, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0),
        ('_pinHoleSize', (), _M_ode.model._t_Length, False, 0),
        ('_illumination', (), _M_ode.model._t_Illumination, False, 0),
        ('_contrastMethod', (), _M_ode.model._t_ContrastMethod, False, 0),
        ('_excitationWave', (), _M_ode.model._t_Length, False, 0),
        ('_emissionWave', (), _M_ode.model._t_Length, False, 0),
        ('_fluor', (), _M_ode._t_RString, False, 0),
        ('_ndFilter', (), _M_ode._t_RDouble, False, 0),
        ('_otf', (), _M_ode.model._t_OTF, False, 0),
        ('_detectorSettings', (), _M_ode.model._t_DetectorSettings, False, 0),
        ('_lightSourceSettings', (), _M_ode.model._t_LightSettings, False, 0),
        ('_filterSet', (), _M_ode.model._t_FilterSet, False, 0),
        ('_samplesPerPixel', (), _M_ode._t_RInt, False, 0),
        ('_photometricInterpretation', (), _M_ode.model._t_PhotometricInterpretation, False, 0),
        ('_mode', (), _M_ode.model._t_AcquisitionMode, False, 0),
        ('_pockelCellSetting', (), _M_ode._t_RInt, False, 0),
        ('_channelsSeq', (), _M_ode.model._t_LogicalChannelChannelsSeq, False, 0),
        ('_channelsLoaded', (), IcePy._t_bool, False, 0),
        ('_lightPath', (), _M_ode.model._t_LightPath, False, 0)
    ))
    LogicalChannel._ice_type = _M_ode.model._t_LogicalChannel

    LogicalChannel._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    LogicalChannel._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    LogicalChannel._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    LogicalChannel._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    LogicalChannel._op_getPinHoleSize = IcePy.Operation('getPinHoleSize', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    LogicalChannel._op_setPinHoleSize = IcePy.Operation('setPinHoleSize', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    LogicalChannel._op_getIllumination = IcePy.Operation('getIllumination', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Illumination, False, 0), ())
    LogicalChannel._op_setIllumination = IcePy.Operation('setIllumination', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Illumination, False, 0),), (), None, ())
    LogicalChannel._op_getContrastMethod = IcePy.Operation('getContrastMethod', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ContrastMethod, False, 0), ())
    LogicalChannel._op_setContrastMethod = IcePy.Operation('setContrastMethod', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ContrastMethod, False, 0),), (), None, ())
    LogicalChannel._op_getExcitationWave = IcePy.Operation('getExcitationWave', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    LogicalChannel._op_setExcitationWave = IcePy.Operation('setExcitationWave', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    LogicalChannel._op_getEmissionWave = IcePy.Operation('getEmissionWave', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    LogicalChannel._op_setEmissionWave = IcePy.Operation('setEmissionWave', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    LogicalChannel._op_getFluor = IcePy.Operation('getFluor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    LogicalChannel._op_setFluor = IcePy.Operation('setFluor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    LogicalChannel._op_getNdFilter = IcePy.Operation('getNdFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    LogicalChannel._op_setNdFilter = IcePy.Operation('setNdFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    LogicalChannel._op_getOtf = IcePy.Operation('getOtf', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OTF, False, 0), ())
    LogicalChannel._op_setOtf = IcePy.Operation('setOtf', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OTF, False, 0),), (), None, ())
    LogicalChannel._op_getDetectorSettings = IcePy.Operation('getDetectorSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_DetectorSettings, False, 0), ())
    LogicalChannel._op_setDetectorSettings = IcePy.Operation('setDetectorSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DetectorSettings, False, 0),), (), None, ())
    LogicalChannel._op_getLightSourceSettings = IcePy.Operation('getLightSourceSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightSettings, False, 0), ())
    LogicalChannel._op_setLightSourceSettings = IcePy.Operation('setLightSourceSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSettings, False, 0),), (), None, ())
    LogicalChannel._op_getFilterSet = IcePy.Operation('getFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_FilterSet, False, 0), ())
    LogicalChannel._op_setFilterSet = IcePy.Operation('setFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilterSet, False, 0),), (), None, ())
    LogicalChannel._op_getSamplesPerPixel = IcePy.Operation('getSamplesPerPixel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    LogicalChannel._op_setSamplesPerPixel = IcePy.Operation('setSamplesPerPixel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    LogicalChannel._op_getPhotometricInterpretation = IcePy.Operation('getPhotometricInterpretation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PhotometricInterpretation, False, 0), ())
    LogicalChannel._op_setPhotometricInterpretation = IcePy.Operation('setPhotometricInterpretation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PhotometricInterpretation, False, 0),), (), None, ())
    LogicalChannel._op_getMode = IcePy.Operation('getMode', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_AcquisitionMode, False, 0), ())
    LogicalChannel._op_setMode = IcePy.Operation('setMode', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_AcquisitionMode, False, 0),), (), None, ())
    LogicalChannel._op_getPockelCellSetting = IcePy.Operation('getPockelCellSetting', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    LogicalChannel._op_setPockelCellSetting = IcePy.Operation('setPockelCellSetting', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    LogicalChannel._op_unloadChannels = IcePy.Operation('unloadChannels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    LogicalChannel._op_sizeOfChannels = IcePy.Operation('sizeOfChannels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    LogicalChannel._op_copyChannels = IcePy.Operation('copyChannels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LogicalChannelChannelsSeq, False, 0), ())
    LogicalChannel._op_addChannel = IcePy.Operation('addChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Channel, False, 0),), (), None, ())
    LogicalChannel._op_addAllChannelSet = IcePy.Operation('addAllChannelSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LogicalChannelChannelsSeq, False, 0),), (), None, ())
    LogicalChannel._op_removeChannel = IcePy.Operation('removeChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Channel, False, 0),), (), None, ())
    LogicalChannel._op_removeAllChannelSet = IcePy.Operation('removeAllChannelSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LogicalChannelChannelsSeq, False, 0),), (), None, ())
    LogicalChannel._op_clearChannels = IcePy.Operation('clearChannels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    LogicalChannel._op_reloadChannels = IcePy.Operation('reloadChannels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LogicalChannel, False, 0),), (), None, ())
    LogicalChannel._op_getLightPath = IcePy.Operation('getLightPath', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightPath, False, 0), ())
    LogicalChannel._op_setLightPath = IcePy.Operation('setLightPath', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPath, False, 0),), (), None, ())

    _M_ode.model.LogicalChannel = LogicalChannel
    del LogicalChannel

    _M_ode.model.LogicalChannelPrx = LogicalChannelPrx
    del LogicalChannelPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
