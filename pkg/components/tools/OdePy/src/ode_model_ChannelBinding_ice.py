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

if 'RenderingDef' not in _M_ode.model.__dict__:
    _M_ode.model._t_RenderingDef = IcePy.declareClass('::ode::model::RenderingDef')
    _M_ode.model._t_RenderingDefPrx = IcePy.declareProxy('::ode::model::RenderingDef')

if 'Family' not in _M_ode.model.__dict__:
    _M_ode.model._t_Family = IcePy.declareClass('::ode::model::Family')
    _M_ode.model._t_FamilyPrx = IcePy.declareProxy('::ode::model::Family')

if 'CodomainMapContext' not in _M_ode.model.__dict__:
    _M_ode.model._t_CodomainMapContext = IcePy.declareClass('::ode::model::CodomainMapContext')
    _M_ode.model._t_CodomainMapContextPrx = IcePy.declareProxy('::ode::model::CodomainMapContext')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_ChannelBindingSpatialDomainEnhancementSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ChannelBindingSpatialDomainEnhancementSeq = IcePy.defineSequence('::ode::model::ChannelBindingSpatialDomainEnhancementSeq', (), _M_ode.model._t_CodomainMapContext)

if 'ChannelBinding' not in _M_ode.model.__dict__:
    _M_ode.model.ChannelBinding = Ice.createTempClass()
    class ChannelBinding(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _renderingDef=None, _family=None, _coefficient=None, _inputStart=None, _inputEnd=None, _active=None, _noiseReduction=None, _red=None, _green=None, _blue=None, _alpha=None, _lookupTable=None, _spatialDomainEnhancementSeq=None, _spatialDomainEnhancementLoaded=False):
            if Ice.getType(self) == _M_ode.model.ChannelBinding:
                raise RuntimeError('ode.model.ChannelBinding is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._renderingDef = _renderingDef
            self._family = _family
            self._coefficient = _coefficient
            self._inputStart = _inputStart
            self._inputEnd = _inputEnd
            self._active = _active
            self._noiseReduction = _noiseReduction
            self._red = _red
            self._green = _green
            self._blue = _blue
            self._alpha = _alpha
            self._lookupTable = _lookupTable
            self._spatialDomainEnhancementSeq = _spatialDomainEnhancementSeq
            self._spatialDomainEnhancementLoaded = _spatialDomainEnhancementLoaded

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::ChannelBinding', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::ChannelBinding'

        def ice_staticId():
            return '::ode::model::ChannelBinding'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getRenderingDef(self, current=None):
            pass

        def setRenderingDef(self, theRenderingDef, current=None):
            pass

        def getFamily(self, current=None):
            pass

        def setFamily(self, theFamily, current=None):
            pass

        def getCoefficient(self, current=None):
            pass

        def setCoefficient(self, theCoefficient, current=None):
            pass

        def getInputStart(self, current=None):
            pass

        def setInputStart(self, theInputStart, current=None):
            pass

        def getInputEnd(self, current=None):
            pass

        def setInputEnd(self, theInputEnd, current=None):
            pass

        def getActive(self, current=None):
            pass

        def setActive(self, theActive, current=None):
            pass

        def getNoiseReduction(self, current=None):
            pass

        def setNoiseReduction(self, theNoiseReduction, current=None):
            pass

        def getRed(self, current=None):
            pass

        def setRed(self, theRed, current=None):
            pass

        def getGreen(self, current=None):
            pass

        def setGreen(self, theGreen, current=None):
            pass

        def getBlue(self, current=None):
            pass

        def setBlue(self, theBlue, current=None):
            pass

        def getAlpha(self, current=None):
            pass

        def setAlpha(self, theAlpha, current=None):
            pass

        def getLookupTable(self, current=None):
            pass

        def setLookupTable(self, theLookupTable, current=None):
            pass

        def unloadSpatialDomainEnhancement(self, current=None):
            pass

        def sizeOfSpatialDomainEnhancement(self, current=None):
            pass

        def copySpatialDomainEnhancement(self, current=None):
            pass

        def addCodomainMapContext(self, target, current=None):
            pass

        def addAllCodomainMapContextSet(self, targets, current=None):
            pass

        def removeCodomainMapContext(self, theTarget, current=None):
            pass

        def removeAllCodomainMapContextSet(self, targets, current=None):
            pass

        def clearSpatialDomainEnhancement(self, current=None):
            pass

        def reloadSpatialDomainEnhancement(self, toCopy, current=None):
            pass

        def getCodomainMapContext(self, index, current=None):
            pass

        def setCodomainMapContext(self, index, theElement, current=None):
            pass

        def getPrimaryCodomainMapContext(self, current=None):
            pass

        def setPrimaryCodomainMapContext(self, theElement, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_ChannelBinding)

        __repr__ = __str__

    _M_ode.model.ChannelBindingPrx = Ice.createTempClass()
    class ChannelBindingPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.ChannelBinding._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.ChannelBinding._op_setVersion.end(self, _r)

        def getRenderingDef(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getRenderingDef.invoke(self, ((), _ctx))

        def begin_getRenderingDef(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getRenderingDef.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRenderingDef(self, _r):
            return _M_ode.model.ChannelBinding._op_getRenderingDef.end(self, _r)

        def setRenderingDef(self, theRenderingDef, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setRenderingDef.invoke(self, ((theRenderingDef, ), _ctx))

        def begin_setRenderingDef(self, theRenderingDef, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setRenderingDef.begin(self, ((theRenderingDef, ), _response, _ex, _sent, _ctx))

        def end_setRenderingDef(self, _r):
            return _M_ode.model.ChannelBinding._op_setRenderingDef.end(self, _r)

        def getFamily(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getFamily.invoke(self, ((), _ctx))

        def begin_getFamily(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getFamily.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFamily(self, _r):
            return _M_ode.model.ChannelBinding._op_getFamily.end(self, _r)

        def setFamily(self, theFamily, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setFamily.invoke(self, ((theFamily, ), _ctx))

        def begin_setFamily(self, theFamily, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setFamily.begin(self, ((theFamily, ), _response, _ex, _sent, _ctx))

        def end_setFamily(self, _r):
            return _M_ode.model.ChannelBinding._op_setFamily.end(self, _r)

        def getCoefficient(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getCoefficient.invoke(self, ((), _ctx))

        def begin_getCoefficient(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getCoefficient.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCoefficient(self, _r):
            return _M_ode.model.ChannelBinding._op_getCoefficient.end(self, _r)

        def setCoefficient(self, theCoefficient, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setCoefficient.invoke(self, ((theCoefficient, ), _ctx))

        def begin_setCoefficient(self, theCoefficient, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setCoefficient.begin(self, ((theCoefficient, ), _response, _ex, _sent, _ctx))

        def end_setCoefficient(self, _r):
            return _M_ode.model.ChannelBinding._op_setCoefficient.end(self, _r)

        def getInputStart(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getInputStart.invoke(self, ((), _ctx))

        def begin_getInputStart(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getInputStart.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getInputStart(self, _r):
            return _M_ode.model.ChannelBinding._op_getInputStart.end(self, _r)

        def setInputStart(self, theInputStart, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setInputStart.invoke(self, ((theInputStart, ), _ctx))

        def begin_setInputStart(self, theInputStart, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setInputStart.begin(self, ((theInputStart, ), _response, _ex, _sent, _ctx))

        def end_setInputStart(self, _r):
            return _M_ode.model.ChannelBinding._op_setInputStart.end(self, _r)

        def getInputEnd(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getInputEnd.invoke(self, ((), _ctx))

        def begin_getInputEnd(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getInputEnd.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getInputEnd(self, _r):
            return _M_ode.model.ChannelBinding._op_getInputEnd.end(self, _r)

        def setInputEnd(self, theInputEnd, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setInputEnd.invoke(self, ((theInputEnd, ), _ctx))

        def begin_setInputEnd(self, theInputEnd, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setInputEnd.begin(self, ((theInputEnd, ), _response, _ex, _sent, _ctx))

        def end_setInputEnd(self, _r):
            return _M_ode.model.ChannelBinding._op_setInputEnd.end(self, _r)

        def getActive(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getActive.invoke(self, ((), _ctx))

        def begin_getActive(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getActive.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getActive(self, _r):
            return _M_ode.model.ChannelBinding._op_getActive.end(self, _r)

        def setActive(self, theActive, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setActive.invoke(self, ((theActive, ), _ctx))

        def begin_setActive(self, theActive, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setActive.begin(self, ((theActive, ), _response, _ex, _sent, _ctx))

        def end_setActive(self, _r):
            return _M_ode.model.ChannelBinding._op_setActive.end(self, _r)

        def getNoiseReduction(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getNoiseReduction.invoke(self, ((), _ctx))

        def begin_getNoiseReduction(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getNoiseReduction.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getNoiseReduction(self, _r):
            return _M_ode.model.ChannelBinding._op_getNoiseReduction.end(self, _r)

        def setNoiseReduction(self, theNoiseReduction, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setNoiseReduction.invoke(self, ((theNoiseReduction, ), _ctx))

        def begin_setNoiseReduction(self, theNoiseReduction, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setNoiseReduction.begin(self, ((theNoiseReduction, ), _response, _ex, _sent, _ctx))

        def end_setNoiseReduction(self, _r):
            return _M_ode.model.ChannelBinding._op_setNoiseReduction.end(self, _r)

        def getRed(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getRed.invoke(self, ((), _ctx))

        def begin_getRed(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getRed.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRed(self, _r):
            return _M_ode.model.ChannelBinding._op_getRed.end(self, _r)

        def setRed(self, theRed, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setRed.invoke(self, ((theRed, ), _ctx))

        def begin_setRed(self, theRed, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setRed.begin(self, ((theRed, ), _response, _ex, _sent, _ctx))

        def end_setRed(self, _r):
            return _M_ode.model.ChannelBinding._op_setRed.end(self, _r)

        def getGreen(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getGreen.invoke(self, ((), _ctx))

        def begin_getGreen(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getGreen.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getGreen(self, _r):
            return _M_ode.model.ChannelBinding._op_getGreen.end(self, _r)

        def setGreen(self, theGreen, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setGreen.invoke(self, ((theGreen, ), _ctx))

        def begin_setGreen(self, theGreen, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setGreen.begin(self, ((theGreen, ), _response, _ex, _sent, _ctx))

        def end_setGreen(self, _r):
            return _M_ode.model.ChannelBinding._op_setGreen.end(self, _r)

        def getBlue(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getBlue.invoke(self, ((), _ctx))

        def begin_getBlue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getBlue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getBlue(self, _r):
            return _M_ode.model.ChannelBinding._op_getBlue.end(self, _r)

        def setBlue(self, theBlue, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setBlue.invoke(self, ((theBlue, ), _ctx))

        def begin_setBlue(self, theBlue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setBlue.begin(self, ((theBlue, ), _response, _ex, _sent, _ctx))

        def end_setBlue(self, _r):
            return _M_ode.model.ChannelBinding._op_setBlue.end(self, _r)

        def getAlpha(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getAlpha.invoke(self, ((), _ctx))

        def begin_getAlpha(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getAlpha.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAlpha(self, _r):
            return _M_ode.model.ChannelBinding._op_getAlpha.end(self, _r)

        def setAlpha(self, theAlpha, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setAlpha.invoke(self, ((theAlpha, ), _ctx))

        def begin_setAlpha(self, theAlpha, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setAlpha.begin(self, ((theAlpha, ), _response, _ex, _sent, _ctx))

        def end_setAlpha(self, _r):
            return _M_ode.model.ChannelBinding._op_setAlpha.end(self, _r)

        def getLookupTable(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getLookupTable.invoke(self, ((), _ctx))

        def begin_getLookupTable(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getLookupTable.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLookupTable(self, _r):
            return _M_ode.model.ChannelBinding._op_getLookupTable.end(self, _r)

        def setLookupTable(self, theLookupTable, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setLookupTable.invoke(self, ((theLookupTable, ), _ctx))

        def begin_setLookupTable(self, theLookupTable, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setLookupTable.begin(self, ((theLookupTable, ), _response, _ex, _sent, _ctx))

        def end_setLookupTable(self, _r):
            return _M_ode.model.ChannelBinding._op_setLookupTable.end(self, _r)

        def unloadSpatialDomainEnhancement(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_unloadSpatialDomainEnhancement.invoke(self, ((), _ctx))

        def begin_unloadSpatialDomainEnhancement(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_unloadSpatialDomainEnhancement.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadSpatialDomainEnhancement(self, _r):
            return _M_ode.model.ChannelBinding._op_unloadSpatialDomainEnhancement.end(self, _r)

        def sizeOfSpatialDomainEnhancement(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_sizeOfSpatialDomainEnhancement.invoke(self, ((), _ctx))

        def begin_sizeOfSpatialDomainEnhancement(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_sizeOfSpatialDomainEnhancement.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfSpatialDomainEnhancement(self, _r):
            return _M_ode.model.ChannelBinding._op_sizeOfSpatialDomainEnhancement.end(self, _r)

        def copySpatialDomainEnhancement(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_copySpatialDomainEnhancement.invoke(self, ((), _ctx))

        def begin_copySpatialDomainEnhancement(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_copySpatialDomainEnhancement.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copySpatialDomainEnhancement(self, _r):
            return _M_ode.model.ChannelBinding._op_copySpatialDomainEnhancement.end(self, _r)

        def addCodomainMapContext(self, target, _ctx=None):
            return _M_ode.model.ChannelBinding._op_addCodomainMapContext.invoke(self, ((target, ), _ctx))

        def begin_addCodomainMapContext(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_addCodomainMapContext.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addCodomainMapContext(self, _r):
            return _M_ode.model.ChannelBinding._op_addCodomainMapContext.end(self, _r)

        def addAllCodomainMapContextSet(self, targets, _ctx=None):
            return _M_ode.model.ChannelBinding._op_addAllCodomainMapContextSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllCodomainMapContextSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_addAllCodomainMapContextSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllCodomainMapContextSet(self, _r):
            return _M_ode.model.ChannelBinding._op_addAllCodomainMapContextSet.end(self, _r)

        def removeCodomainMapContext(self, theTarget, _ctx=None):
            return _M_ode.model.ChannelBinding._op_removeCodomainMapContext.invoke(self, ((theTarget, ), _ctx))

        def begin_removeCodomainMapContext(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_removeCodomainMapContext.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeCodomainMapContext(self, _r):
            return _M_ode.model.ChannelBinding._op_removeCodomainMapContext.end(self, _r)

        def removeAllCodomainMapContextSet(self, targets, _ctx=None):
            return _M_ode.model.ChannelBinding._op_removeAllCodomainMapContextSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllCodomainMapContextSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_removeAllCodomainMapContextSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllCodomainMapContextSet(self, _r):
            return _M_ode.model.ChannelBinding._op_removeAllCodomainMapContextSet.end(self, _r)

        def clearSpatialDomainEnhancement(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_clearSpatialDomainEnhancement.invoke(self, ((), _ctx))

        def begin_clearSpatialDomainEnhancement(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_clearSpatialDomainEnhancement.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearSpatialDomainEnhancement(self, _r):
            return _M_ode.model.ChannelBinding._op_clearSpatialDomainEnhancement.end(self, _r)

        def reloadSpatialDomainEnhancement(self, toCopy, _ctx=None):
            return _M_ode.model.ChannelBinding._op_reloadSpatialDomainEnhancement.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadSpatialDomainEnhancement(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_reloadSpatialDomainEnhancement.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadSpatialDomainEnhancement(self, _r):
            return _M_ode.model.ChannelBinding._op_reloadSpatialDomainEnhancement.end(self, _r)

        def getCodomainMapContext(self, index, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getCodomainMapContext.invoke(self, ((index, ), _ctx))

        def begin_getCodomainMapContext(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getCodomainMapContext.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getCodomainMapContext(self, _r):
            return _M_ode.model.ChannelBinding._op_getCodomainMapContext.end(self, _r)

        def setCodomainMapContext(self, index, theElement, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setCodomainMapContext.invoke(self, ((index, theElement), _ctx))

        def begin_setCodomainMapContext(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setCodomainMapContext.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setCodomainMapContext(self, _r):
            return _M_ode.model.ChannelBinding._op_setCodomainMapContext.end(self, _r)

        def getPrimaryCodomainMapContext(self, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getPrimaryCodomainMapContext.invoke(self, ((), _ctx))

        def begin_getPrimaryCodomainMapContext(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_getPrimaryCodomainMapContext.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryCodomainMapContext(self, _r):
            return _M_ode.model.ChannelBinding._op_getPrimaryCodomainMapContext.end(self, _r)

        def setPrimaryCodomainMapContext(self, theElement, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setPrimaryCodomainMapContext.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryCodomainMapContext(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ChannelBinding._op_setPrimaryCodomainMapContext.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryCodomainMapContext(self, _r):
            return _M_ode.model.ChannelBinding._op_setPrimaryCodomainMapContext.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ChannelBindingPrx.ice_checkedCast(proxy, '::ode::model::ChannelBinding', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ChannelBindingPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ChannelBinding'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ChannelBindingPrx = IcePy.defineProxy('::ode::model::ChannelBinding', ChannelBindingPrx)

    _M_ode.model._t_ChannelBinding = IcePy.declareClass('::ode::model::ChannelBinding')

    _M_ode.model._t_ChannelBinding = IcePy.defineClass('::ode::model::ChannelBinding', ChannelBinding, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_renderingDef', (), _M_ode.model._t_RenderingDef, False, 0),
        ('_family', (), _M_ode.model._t_Family, False, 0),
        ('_coefficient', (), _M_ode._t_RDouble, False, 0),
        ('_inputStart', (), _M_ode._t_RDouble, False, 0),
        ('_inputEnd', (), _M_ode._t_RDouble, False, 0),
        ('_active', (), _M_ode._t_RBool, False, 0),
        ('_noiseReduction', (), _M_ode._t_RBool, False, 0),
        ('_red', (), _M_ode._t_RInt, False, 0),
        ('_green', (), _M_ode._t_RInt, False, 0),
        ('_blue', (), _M_ode._t_RInt, False, 0),
        ('_alpha', (), _M_ode._t_RInt, False, 0),
        ('_lookupTable', (), _M_ode._t_RString, False, 0),
        ('_spatialDomainEnhancementSeq', (), _M_ode.model._t_ChannelBindingSpatialDomainEnhancementSeq, False, 0),
        ('_spatialDomainEnhancementLoaded', (), IcePy._t_bool, False, 0)
    ))
    ChannelBinding._ice_type = _M_ode.model._t_ChannelBinding

    ChannelBinding._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ChannelBinding._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ChannelBinding._op_getRenderingDef = IcePy.Operation('getRenderingDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_RenderingDef, False, 0), ())
    ChannelBinding._op_setRenderingDef = IcePy.Operation('setRenderingDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingDef, False, 0),), (), None, ())
    ChannelBinding._op_getFamily = IcePy.Operation('getFamily', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Family, False, 0), ())
    ChannelBinding._op_setFamily = IcePy.Operation('setFamily', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Family, False, 0),), (), None, ())
    ChannelBinding._op_getCoefficient = IcePy.Operation('getCoefficient', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    ChannelBinding._op_setCoefficient = IcePy.Operation('setCoefficient', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    ChannelBinding._op_getInputStart = IcePy.Operation('getInputStart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    ChannelBinding._op_setInputStart = IcePy.Operation('setInputStart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    ChannelBinding._op_getInputEnd = IcePy.Operation('getInputEnd', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    ChannelBinding._op_setInputEnd = IcePy.Operation('setInputEnd', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    ChannelBinding._op_getActive = IcePy.Operation('getActive', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    ChannelBinding._op_setActive = IcePy.Operation('setActive', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    ChannelBinding._op_getNoiseReduction = IcePy.Operation('getNoiseReduction', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    ChannelBinding._op_setNoiseReduction = IcePy.Operation('setNoiseReduction', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    ChannelBinding._op_getRed = IcePy.Operation('getRed', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ChannelBinding._op_setRed = IcePy.Operation('setRed', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ChannelBinding._op_getGreen = IcePy.Operation('getGreen', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ChannelBinding._op_setGreen = IcePy.Operation('setGreen', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ChannelBinding._op_getBlue = IcePy.Operation('getBlue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ChannelBinding._op_setBlue = IcePy.Operation('setBlue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ChannelBinding._op_getAlpha = IcePy.Operation('getAlpha', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ChannelBinding._op_setAlpha = IcePy.Operation('setAlpha', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ChannelBinding._op_getLookupTable = IcePy.Operation('getLookupTable', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    ChannelBinding._op_setLookupTable = IcePy.Operation('setLookupTable', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    ChannelBinding._op_unloadSpatialDomainEnhancement = IcePy.Operation('unloadSpatialDomainEnhancement', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    ChannelBinding._op_sizeOfSpatialDomainEnhancement = IcePy.Operation('sizeOfSpatialDomainEnhancement', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    ChannelBinding._op_copySpatialDomainEnhancement = IcePy.Operation('copySpatialDomainEnhancement', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ChannelBindingSpatialDomainEnhancementSeq, False, 0), ())
    ChannelBinding._op_addCodomainMapContext = IcePy.Operation('addCodomainMapContext', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_CodomainMapContext, False, 0),), (), None, ())
    ChannelBinding._op_addAllCodomainMapContextSet = IcePy.Operation('addAllCodomainMapContextSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelBindingSpatialDomainEnhancementSeq, False, 0),), (), None, ())
    ChannelBinding._op_removeCodomainMapContext = IcePy.Operation('removeCodomainMapContext', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_CodomainMapContext, False, 0),), (), None, ())
    ChannelBinding._op_removeAllCodomainMapContextSet = IcePy.Operation('removeAllCodomainMapContextSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelBindingSpatialDomainEnhancementSeq, False, 0),), (), None, ())
    ChannelBinding._op_clearSpatialDomainEnhancement = IcePy.Operation('clearSpatialDomainEnhancement', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    ChannelBinding._op_reloadSpatialDomainEnhancement = IcePy.Operation('reloadSpatialDomainEnhancement', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelBinding, False, 0),), (), None, ())
    ChannelBinding._op_getCodomainMapContext = IcePy.Operation('getCodomainMapContext', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_CodomainMapContext, False, 0), ())
    ChannelBinding._op_setCodomainMapContext = IcePy.Operation('setCodomainMapContext', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_CodomainMapContext, False, 0)), (), ((), _M_ode.model._t_CodomainMapContext, False, 0), ())
    ChannelBinding._op_getPrimaryCodomainMapContext = IcePy.Operation('getPrimaryCodomainMapContext', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_CodomainMapContext, False, 0), ())
    ChannelBinding._op_setPrimaryCodomainMapContext = IcePy.Operation('setPrimaryCodomainMapContext', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_CodomainMapContext, False, 0),), (), ((), _M_ode.model._t_CodomainMapContext, False, 0), ())

    _M_ode.model.ChannelBinding = ChannelBinding
    del ChannelBinding

    _M_ode.model.ChannelBindingPrx = ChannelBindingPrx
    del ChannelBindingPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
