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

if 'Pixels' not in _M_ode.model.__dict__:
    _M_ode.model._t_Pixels = IcePy.declareClass('::ode::model::Pixels')
    _M_ode.model._t_PixelsPrx = IcePy.declareProxy('::ode::model::Pixels')

if 'RenderingModel' not in _M_ode.model.__dict__:
    _M_ode.model._t_RenderingModel = IcePy.declareClass('::ode::model::RenderingModel')
    _M_ode.model._t_RenderingModelPrx = IcePy.declareProxy('::ode::model::RenderingModel')

if 'ChannelBinding' not in _M_ode.model.__dict__:
    _M_ode.model._t_ChannelBinding = IcePy.declareClass('::ode::model::ChannelBinding')
    _M_ode.model._t_ChannelBindingPrx = IcePy.declareProxy('::ode::model::ChannelBinding')

if 'QuantumDef' not in _M_ode.model.__dict__:
    _M_ode.model._t_QuantumDef = IcePy.declareClass('::ode::model::QuantumDef')
    _M_ode.model._t_QuantumDefPrx = IcePy.declareProxy('::ode::model::QuantumDef')

if 'ProjectionDef' not in _M_ode.model.__dict__:
    _M_ode.model._t_ProjectionDef = IcePy.declareClass('::ode::model::ProjectionDef')
    _M_ode.model._t_ProjectionDefPrx = IcePy.declareProxy('::ode::model::ProjectionDef')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_RenderingDefWaveRenderingSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_RenderingDefWaveRenderingSeq = IcePy.defineSequence('::ode::model::RenderingDefWaveRenderingSeq', (), _M_ode.model._t_ChannelBinding)

if '_t_RenderingDefProjectionsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_RenderingDefProjectionsSeq = IcePy.defineSequence('::ode::model::RenderingDefProjectionsSeq', (), _M_ode.model._t_ProjectionDef)

if 'RenderingDef' not in _M_ode.model.__dict__:
    _M_ode.model.RenderingDef = Ice.createTempClass()
    class RenderingDef(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _pixels=None, _defaultZ=None, _defaultT=None, _model=None, _waveRenderingSeq=None, _waveRenderingLoaded=False, _name=None, _compression=None, _quantization=None, _projectionsSeq=None, _projectionsLoaded=False):
            if Ice.getType(self) == _M_ode.model.RenderingDef:
                raise RuntimeError('ode.model.RenderingDef is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._pixels = _pixels
            self._defaultZ = _defaultZ
            self._defaultT = _defaultT
            self._model = _model
            self._waveRenderingSeq = _waveRenderingSeq
            self._waveRenderingLoaded = _waveRenderingLoaded
            self._name = _name
            self._compression = _compression
            self._quantization = _quantization
            self._projectionsSeq = _projectionsSeq
            self._projectionsLoaded = _projectionsLoaded

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::RenderingDef')

        def ice_id(self, current=None):
            return '::ode::model::RenderingDef'

        def ice_staticId():
            return '::ode::model::RenderingDef'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getPixels(self, current=None):
            pass

        def setPixels(self, thePixels, current=None):
            pass

        def getDefaultZ(self, current=None):
            pass

        def setDefaultZ(self, theDefaultZ, current=None):
            pass

        def getDefaultT(self, current=None):
            pass

        def setDefaultT(self, theDefaultT, current=None):
            pass

        def getModel(self, current=None):
            pass

        def setModel(self, theModel, current=None):
            pass

        def unloadWaveRendering(self, current=None):
            pass

        def sizeOfWaveRendering(self, current=None):
            pass

        def copyWaveRendering(self, current=None):
            pass

        def addChannelBinding(self, target, current=None):
            pass

        def addAllChannelBindingSet(self, targets, current=None):
            pass

        def removeChannelBinding(self, theTarget, current=None):
            pass

        def removeAllChannelBindingSet(self, targets, current=None):
            pass

        def clearWaveRendering(self, current=None):
            pass

        def reloadWaveRendering(self, toCopy, current=None):
            pass

        def getChannelBinding(self, index, current=None):
            pass

        def setChannelBinding(self, index, theElement, current=None):
            pass

        def getPrimaryChannelBinding(self, current=None):
            pass

        def setPrimaryChannelBinding(self, theElement, current=None):
            pass

        def getName(self, current=None):
            pass

        def setName(self, theName, current=None):
            pass

        def getCompression(self, current=None):
            pass

        def setCompression(self, theCompression, current=None):
            pass

        def getQuantization(self, current=None):
            pass

        def setQuantization(self, theQuantization, current=None):
            pass

        def unloadProjections(self, current=None):
            pass

        def sizeOfProjections(self, current=None):
            pass

        def copyProjections(self, current=None):
            pass

        def addProjectionDef(self, target, current=None):
            pass

        def addAllProjectionDefSet(self, targets, current=None):
            pass

        def removeProjectionDef(self, theTarget, current=None):
            pass

        def removeAllProjectionDefSet(self, targets, current=None):
            pass

        def clearProjections(self, current=None):
            pass

        def reloadProjections(self, toCopy, current=None):
            pass

        def getProjectionDef(self, index, current=None):
            pass

        def setProjectionDef(self, index, theElement, current=None):
            pass

        def getPrimaryProjectionDef(self, current=None):
            pass

        def setPrimaryProjectionDef(self, theElement, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_RenderingDef)

        __repr__ = __str__

    _M_ode.model.RenderingDefPrx = Ice.createTempClass()
    class RenderingDefPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.RenderingDef._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.RenderingDef._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.RenderingDef._op_setVersion.end(self, _r)

        def getPixels(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_getPixels.invoke(self, ((), _ctx))

        def begin_getPixels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getPixels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPixels(self, _r):
            return _M_ode.model.RenderingDef._op_getPixels.end(self, _r)

        def setPixels(self, thePixels, _ctx=None):
            return _M_ode.model.RenderingDef._op_setPixels.invoke(self, ((thePixels, ), _ctx))

        def begin_setPixels(self, thePixels, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setPixels.begin(self, ((thePixels, ), _response, _ex, _sent, _ctx))

        def end_setPixels(self, _r):
            return _M_ode.model.RenderingDef._op_setPixels.end(self, _r)

        def getDefaultZ(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_getDefaultZ.invoke(self, ((), _ctx))

        def begin_getDefaultZ(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getDefaultZ.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDefaultZ(self, _r):
            return _M_ode.model.RenderingDef._op_getDefaultZ.end(self, _r)

        def setDefaultZ(self, theDefaultZ, _ctx=None):
            return _M_ode.model.RenderingDef._op_setDefaultZ.invoke(self, ((theDefaultZ, ), _ctx))

        def begin_setDefaultZ(self, theDefaultZ, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setDefaultZ.begin(self, ((theDefaultZ, ), _response, _ex, _sent, _ctx))

        def end_setDefaultZ(self, _r):
            return _M_ode.model.RenderingDef._op_setDefaultZ.end(self, _r)

        def getDefaultT(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_getDefaultT.invoke(self, ((), _ctx))

        def begin_getDefaultT(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getDefaultT.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDefaultT(self, _r):
            return _M_ode.model.RenderingDef._op_getDefaultT.end(self, _r)

        def setDefaultT(self, theDefaultT, _ctx=None):
            return _M_ode.model.RenderingDef._op_setDefaultT.invoke(self, ((theDefaultT, ), _ctx))

        def begin_setDefaultT(self, theDefaultT, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setDefaultT.begin(self, ((theDefaultT, ), _response, _ex, _sent, _ctx))

        def end_setDefaultT(self, _r):
            return _M_ode.model.RenderingDef._op_setDefaultT.end(self, _r)

        def getModel(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_getModel.invoke(self, ((), _ctx))

        def begin_getModel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getModel.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getModel(self, _r):
            return _M_ode.model.RenderingDef._op_getModel.end(self, _r)

        def setModel(self, theModel, _ctx=None):
            return _M_ode.model.RenderingDef._op_setModel.invoke(self, ((theModel, ), _ctx))

        def begin_setModel(self, theModel, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setModel.begin(self, ((theModel, ), _response, _ex, _sent, _ctx))

        def end_setModel(self, _r):
            return _M_ode.model.RenderingDef._op_setModel.end(self, _r)

        def unloadWaveRendering(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_unloadWaveRendering.invoke(self, ((), _ctx))

        def begin_unloadWaveRendering(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_unloadWaveRendering.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadWaveRendering(self, _r):
            return _M_ode.model.RenderingDef._op_unloadWaveRendering.end(self, _r)

        def sizeOfWaveRendering(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_sizeOfWaveRendering.invoke(self, ((), _ctx))

        def begin_sizeOfWaveRendering(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_sizeOfWaveRendering.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfWaveRendering(self, _r):
            return _M_ode.model.RenderingDef._op_sizeOfWaveRendering.end(self, _r)

        def copyWaveRendering(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_copyWaveRendering.invoke(self, ((), _ctx))

        def begin_copyWaveRendering(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_copyWaveRendering.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyWaveRendering(self, _r):
            return _M_ode.model.RenderingDef._op_copyWaveRendering.end(self, _r)

        def addChannelBinding(self, target, _ctx=None):
            return _M_ode.model.RenderingDef._op_addChannelBinding.invoke(self, ((target, ), _ctx))

        def begin_addChannelBinding(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_addChannelBinding.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addChannelBinding(self, _r):
            return _M_ode.model.RenderingDef._op_addChannelBinding.end(self, _r)

        def addAllChannelBindingSet(self, targets, _ctx=None):
            return _M_ode.model.RenderingDef._op_addAllChannelBindingSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllChannelBindingSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_addAllChannelBindingSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllChannelBindingSet(self, _r):
            return _M_ode.model.RenderingDef._op_addAllChannelBindingSet.end(self, _r)

        def removeChannelBinding(self, theTarget, _ctx=None):
            return _M_ode.model.RenderingDef._op_removeChannelBinding.invoke(self, ((theTarget, ), _ctx))

        def begin_removeChannelBinding(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_removeChannelBinding.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeChannelBinding(self, _r):
            return _M_ode.model.RenderingDef._op_removeChannelBinding.end(self, _r)

        def removeAllChannelBindingSet(self, targets, _ctx=None):
            return _M_ode.model.RenderingDef._op_removeAllChannelBindingSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllChannelBindingSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_removeAllChannelBindingSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllChannelBindingSet(self, _r):
            return _M_ode.model.RenderingDef._op_removeAllChannelBindingSet.end(self, _r)

        def clearWaveRendering(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_clearWaveRendering.invoke(self, ((), _ctx))

        def begin_clearWaveRendering(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_clearWaveRendering.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearWaveRendering(self, _r):
            return _M_ode.model.RenderingDef._op_clearWaveRendering.end(self, _r)

        def reloadWaveRendering(self, toCopy, _ctx=None):
            return _M_ode.model.RenderingDef._op_reloadWaveRendering.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadWaveRendering(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_reloadWaveRendering.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadWaveRendering(self, _r):
            return _M_ode.model.RenderingDef._op_reloadWaveRendering.end(self, _r)

        def getChannelBinding(self, index, _ctx=None):
            return _M_ode.model.RenderingDef._op_getChannelBinding.invoke(self, ((index, ), _ctx))

        def begin_getChannelBinding(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getChannelBinding.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getChannelBinding(self, _r):
            return _M_ode.model.RenderingDef._op_getChannelBinding.end(self, _r)

        def setChannelBinding(self, index, theElement, _ctx=None):
            return _M_ode.model.RenderingDef._op_setChannelBinding.invoke(self, ((index, theElement), _ctx))

        def begin_setChannelBinding(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setChannelBinding.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setChannelBinding(self, _r):
            return _M_ode.model.RenderingDef._op_setChannelBinding.end(self, _r)

        def getPrimaryChannelBinding(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_getPrimaryChannelBinding.invoke(self, ((), _ctx))

        def begin_getPrimaryChannelBinding(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getPrimaryChannelBinding.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryChannelBinding(self, _r):
            return _M_ode.model.RenderingDef._op_getPrimaryChannelBinding.end(self, _r)

        def setPrimaryChannelBinding(self, theElement, _ctx=None):
            return _M_ode.model.RenderingDef._op_setPrimaryChannelBinding.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryChannelBinding(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setPrimaryChannelBinding.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryChannelBinding(self, _r):
            return _M_ode.model.RenderingDef._op_setPrimaryChannelBinding.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.RenderingDef._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.RenderingDef._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.RenderingDef._op_setName.end(self, _r)

        def getCompression(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_getCompression.invoke(self, ((), _ctx))

        def begin_getCompression(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getCompression.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCompression(self, _r):
            return _M_ode.model.RenderingDef._op_getCompression.end(self, _r)

        def setCompression(self, theCompression, _ctx=None):
            return _M_ode.model.RenderingDef._op_setCompression.invoke(self, ((theCompression, ), _ctx))

        def begin_setCompression(self, theCompression, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setCompression.begin(self, ((theCompression, ), _response, _ex, _sent, _ctx))

        def end_setCompression(self, _r):
            return _M_ode.model.RenderingDef._op_setCompression.end(self, _r)

        def getQuantization(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_getQuantization.invoke(self, ((), _ctx))

        def begin_getQuantization(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getQuantization.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getQuantization(self, _r):
            return _M_ode.model.RenderingDef._op_getQuantization.end(self, _r)

        def setQuantization(self, theQuantization, _ctx=None):
            return _M_ode.model.RenderingDef._op_setQuantization.invoke(self, ((theQuantization, ), _ctx))

        def begin_setQuantization(self, theQuantization, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setQuantization.begin(self, ((theQuantization, ), _response, _ex, _sent, _ctx))

        def end_setQuantization(self, _r):
            return _M_ode.model.RenderingDef._op_setQuantization.end(self, _r)

        def unloadProjections(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_unloadProjections.invoke(self, ((), _ctx))

        def begin_unloadProjections(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_unloadProjections.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadProjections(self, _r):
            return _M_ode.model.RenderingDef._op_unloadProjections.end(self, _r)

        def sizeOfProjections(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_sizeOfProjections.invoke(self, ((), _ctx))

        def begin_sizeOfProjections(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_sizeOfProjections.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfProjections(self, _r):
            return _M_ode.model.RenderingDef._op_sizeOfProjections.end(self, _r)

        def copyProjections(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_copyProjections.invoke(self, ((), _ctx))

        def begin_copyProjections(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_copyProjections.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyProjections(self, _r):
            return _M_ode.model.RenderingDef._op_copyProjections.end(self, _r)

        def addProjectionDef(self, target, _ctx=None):
            return _M_ode.model.RenderingDef._op_addProjectionDef.invoke(self, ((target, ), _ctx))

        def begin_addProjectionDef(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_addProjectionDef.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addProjectionDef(self, _r):
            return _M_ode.model.RenderingDef._op_addProjectionDef.end(self, _r)

        def addAllProjectionDefSet(self, targets, _ctx=None):
            return _M_ode.model.RenderingDef._op_addAllProjectionDefSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllProjectionDefSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_addAllProjectionDefSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllProjectionDefSet(self, _r):
            return _M_ode.model.RenderingDef._op_addAllProjectionDefSet.end(self, _r)

        def removeProjectionDef(self, theTarget, _ctx=None):
            return _M_ode.model.RenderingDef._op_removeProjectionDef.invoke(self, ((theTarget, ), _ctx))

        def begin_removeProjectionDef(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_removeProjectionDef.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeProjectionDef(self, _r):
            return _M_ode.model.RenderingDef._op_removeProjectionDef.end(self, _r)

        def removeAllProjectionDefSet(self, targets, _ctx=None):
            return _M_ode.model.RenderingDef._op_removeAllProjectionDefSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllProjectionDefSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_removeAllProjectionDefSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllProjectionDefSet(self, _r):
            return _M_ode.model.RenderingDef._op_removeAllProjectionDefSet.end(self, _r)

        def clearProjections(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_clearProjections.invoke(self, ((), _ctx))

        def begin_clearProjections(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_clearProjections.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearProjections(self, _r):
            return _M_ode.model.RenderingDef._op_clearProjections.end(self, _r)

        def reloadProjections(self, toCopy, _ctx=None):
            return _M_ode.model.RenderingDef._op_reloadProjections.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadProjections(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_reloadProjections.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadProjections(self, _r):
            return _M_ode.model.RenderingDef._op_reloadProjections.end(self, _r)

        def getProjectionDef(self, index, _ctx=None):
            return _M_ode.model.RenderingDef._op_getProjectionDef.invoke(self, ((index, ), _ctx))

        def begin_getProjectionDef(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getProjectionDef.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getProjectionDef(self, _r):
            return _M_ode.model.RenderingDef._op_getProjectionDef.end(self, _r)

        def setProjectionDef(self, index, theElement, _ctx=None):
            return _M_ode.model.RenderingDef._op_setProjectionDef.invoke(self, ((index, theElement), _ctx))

        def begin_setProjectionDef(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setProjectionDef.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setProjectionDef(self, _r):
            return _M_ode.model.RenderingDef._op_setProjectionDef.end(self, _r)

        def getPrimaryProjectionDef(self, _ctx=None):
            return _M_ode.model.RenderingDef._op_getPrimaryProjectionDef.invoke(self, ((), _ctx))

        def begin_getPrimaryProjectionDef(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_getPrimaryProjectionDef.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryProjectionDef(self, _r):
            return _M_ode.model.RenderingDef._op_getPrimaryProjectionDef.end(self, _r)

        def setPrimaryProjectionDef(self, theElement, _ctx=None):
            return _M_ode.model.RenderingDef._op_setPrimaryProjectionDef.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryProjectionDef(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.RenderingDef._op_setPrimaryProjectionDef.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryProjectionDef(self, _r):
            return _M_ode.model.RenderingDef._op_setPrimaryProjectionDef.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.RenderingDefPrx.ice_checkedCast(proxy, '::ode::model::RenderingDef', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.RenderingDefPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::RenderingDef'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_RenderingDefPrx = IcePy.defineProxy('::ode::model::RenderingDef', RenderingDefPrx)

    _M_ode.model._t_RenderingDef = IcePy.declareClass('::ode::model::RenderingDef')

    _M_ode.model._t_RenderingDef = IcePy.defineClass('::ode::model::RenderingDef', RenderingDef, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_pixels', (), _M_ode.model._t_Pixels, False, 0),
        ('_defaultZ', (), _M_ode._t_RInt, False, 0),
        ('_defaultT', (), _M_ode._t_RInt, False, 0),
        ('_model', (), _M_ode.model._t_RenderingModel, False, 0),
        ('_waveRenderingSeq', (), _M_ode.model._t_RenderingDefWaveRenderingSeq, False, 0),
        ('_waveRenderingLoaded', (), IcePy._t_bool, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0),
        ('_compression', (), _M_ode._t_RDouble, False, 0),
        ('_quantization', (), _M_ode.model._t_QuantumDef, False, 0),
        ('_projectionsSeq', (), _M_ode.model._t_RenderingDefProjectionsSeq, False, 0),
        ('_projectionsLoaded', (), IcePy._t_bool, False, 0)
    ))
    RenderingDef._ice_type = _M_ode.model._t_RenderingDef

    RenderingDef._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    RenderingDef._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    RenderingDef._op_getPixels = IcePy.Operation('getPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Pixels, False, 0), ())
    RenderingDef._op_setPixels = IcePy.Operation('setPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())
    RenderingDef._op_getDefaultZ = IcePy.Operation('getDefaultZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    RenderingDef._op_setDefaultZ = IcePy.Operation('setDefaultZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    RenderingDef._op_getDefaultT = IcePy.Operation('getDefaultT', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    RenderingDef._op_setDefaultT = IcePy.Operation('setDefaultT', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    RenderingDef._op_getModel = IcePy.Operation('getModel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_RenderingModel, False, 0), ())
    RenderingDef._op_setModel = IcePy.Operation('setModel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingModel, False, 0),), (), None, ())
    RenderingDef._op_unloadWaveRendering = IcePy.Operation('unloadWaveRendering', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    RenderingDef._op_sizeOfWaveRendering = IcePy.Operation('sizeOfWaveRendering', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    RenderingDef._op_copyWaveRendering = IcePy.Operation('copyWaveRendering', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_RenderingDefWaveRenderingSeq, False, 0), ())
    RenderingDef._op_addChannelBinding = IcePy.Operation('addChannelBinding', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelBinding, False, 0),), (), None, ())
    RenderingDef._op_addAllChannelBindingSet = IcePy.Operation('addAllChannelBindingSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingDefWaveRenderingSeq, False, 0),), (), None, ())
    RenderingDef._op_removeChannelBinding = IcePy.Operation('removeChannelBinding', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelBinding, False, 0),), (), None, ())
    RenderingDef._op_removeAllChannelBindingSet = IcePy.Operation('removeAllChannelBindingSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingDefWaveRenderingSeq, False, 0),), (), None, ())
    RenderingDef._op_clearWaveRendering = IcePy.Operation('clearWaveRendering', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    RenderingDef._op_reloadWaveRendering = IcePy.Operation('reloadWaveRendering', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingDef, False, 0),), (), None, ())
    RenderingDef._op_getChannelBinding = IcePy.Operation('getChannelBinding', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_ChannelBinding, False, 0), ())
    RenderingDef._op_setChannelBinding = IcePy.Operation('setChannelBinding', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_ChannelBinding, False, 0)), (), ((), _M_ode.model._t_ChannelBinding, False, 0), ())
    RenderingDef._op_getPrimaryChannelBinding = IcePy.Operation('getPrimaryChannelBinding', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ChannelBinding, False, 0), ())
    RenderingDef._op_setPrimaryChannelBinding = IcePy.Operation('setPrimaryChannelBinding', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelBinding, False, 0),), (), ((), _M_ode.model._t_ChannelBinding, False, 0), ())
    RenderingDef._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    RenderingDef._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    RenderingDef._op_getCompression = IcePy.Operation('getCompression', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    RenderingDef._op_setCompression = IcePy.Operation('setCompression', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    RenderingDef._op_getQuantization = IcePy.Operation('getQuantization', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_QuantumDef, False, 0), ())
    RenderingDef._op_setQuantization = IcePy.Operation('setQuantization', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_QuantumDef, False, 0),), (), None, ())
    RenderingDef._op_unloadProjections = IcePy.Operation('unloadProjections', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    RenderingDef._op_sizeOfProjections = IcePy.Operation('sizeOfProjections', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    RenderingDef._op_copyProjections = IcePy.Operation('copyProjections', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_RenderingDefProjectionsSeq, False, 0), ())
    RenderingDef._op_addProjectionDef = IcePy.Operation('addProjectionDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectionDef, False, 0),), (), None, ())
    RenderingDef._op_addAllProjectionDefSet = IcePy.Operation('addAllProjectionDefSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingDefProjectionsSeq, False, 0),), (), None, ())
    RenderingDef._op_removeProjectionDef = IcePy.Operation('removeProjectionDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectionDef, False, 0),), (), None, ())
    RenderingDef._op_removeAllProjectionDefSet = IcePy.Operation('removeAllProjectionDefSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingDefProjectionsSeq, False, 0),), (), None, ())
    RenderingDef._op_clearProjections = IcePy.Operation('clearProjections', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    RenderingDef._op_reloadProjections = IcePy.Operation('reloadProjections', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingDef, False, 0),), (), None, ())
    RenderingDef._op_getProjectionDef = IcePy.Operation('getProjectionDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_ProjectionDef, False, 0), ())
    RenderingDef._op_setProjectionDef = IcePy.Operation('setProjectionDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_ProjectionDef, False, 0)), (), ((), _M_ode.model._t_ProjectionDef, False, 0), ())
    RenderingDef._op_getPrimaryProjectionDef = IcePy.Operation('getPrimaryProjectionDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ProjectionDef, False, 0), ())
    RenderingDef._op_setPrimaryProjectionDef = IcePy.Operation('setPrimaryProjectionDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectionDef, False, 0),), (), ((), _M_ode.model._t_ProjectionDef, False, 0), ())

    _M_ode.model.RenderingDef = RenderingDef
    del RenderingDef

    _M_ode.model.RenderingDefPrx = RenderingDefPrx
    del RenderingDefPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
