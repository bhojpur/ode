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

if 'MicrobeamManipulationType' not in _M_ode.model.__dict__:
    _M_ode.model._t_MicrobeamManipulationType = IcePy.declareClass('::ode::model::MicrobeamManipulationType')
    _M_ode.model._t_MicrobeamManipulationTypePrx = IcePy.declareProxy('::ode::model::MicrobeamManipulationType')

if 'LightSettings' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightSettings = IcePy.declareClass('::ode::model::LightSettings')
    _M_ode.model._t_LightSettingsPrx = IcePy.declareProxy('::ode::model::LightSettings')

if 'Experiment' not in _M_ode.model.__dict__:
    _M_ode.model._t_Experiment = IcePy.declareClass('::ode::model::Experiment')
    _M_ode.model._t_ExperimentPrx = IcePy.declareProxy('::ode::model::Experiment')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_MicrobeamManipulationLightSourceSettingsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_MicrobeamManipulationLightSourceSettingsSeq = IcePy.defineSequence('::ode::model::MicrobeamManipulationLightSourceSettingsSeq', (), _M_ode.model._t_LightSettings)

if 'MicrobeamManipulation' not in _M_ode.model.__dict__:
    _M_ode.model.MicrobeamManipulation = Ice.createTempClass()
    class MicrobeamManipulation(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _type=None, _lightSourceSettingsSeq=None, _lightSourceSettingsLoaded=False, _experiment=None, _description=None):
            if Ice.getType(self) == _M_ode.model.MicrobeamManipulation:
                raise RuntimeError('ode.model.MicrobeamManipulation is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._type = _type
            self._lightSourceSettingsSeq = _lightSourceSettingsSeq
            self._lightSourceSettingsLoaded = _lightSourceSettingsLoaded
            self._experiment = _experiment
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::MicrobeamManipulation')

        def ice_id(self, current=None):
            return '::ode::model::MicrobeamManipulation'

        def ice_staticId():
            return '::ode::model::MicrobeamManipulation'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getType(self, current=None):
            pass

        def setType(self, theType, current=None):
            pass

        def unloadLightSourceSettings(self, current=None):
            pass

        def sizeOfLightSourceSettings(self, current=None):
            pass

        def copyLightSourceSettings(self, current=None):
            pass

        def addLightSettings(self, target, current=None):
            pass

        def addAllLightSettingsSet(self, targets, current=None):
            pass

        def removeLightSettings(self, theTarget, current=None):
            pass

        def removeAllLightSettingsSet(self, targets, current=None):
            pass

        def clearLightSourceSettings(self, current=None):
            pass

        def reloadLightSourceSettings(self, toCopy, current=None):
            pass

        def getExperiment(self, current=None):
            pass

        def setExperiment(self, theExperiment, current=None):
            pass

        def getDescription(self, current=None):
            pass

        def setDescription(self, theDescription, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_MicrobeamManipulation)

        __repr__ = __str__

    _M_ode.model.MicrobeamManipulationPrx = Ice.createTempClass()
    class MicrobeamManipulationPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_setVersion.end(self, _r)

        def getType(self, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_getType.invoke(self, ((), _ctx))

        def begin_getType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_getType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getType(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_getType.end(self, _r)

        def setType(self, theType, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_setType.invoke(self, ((theType, ), _ctx))

        def begin_setType(self, theType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_setType.begin(self, ((theType, ), _response, _ex, _sent, _ctx))

        def end_setType(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_setType.end(self, _r)

        def unloadLightSourceSettings(self, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_unloadLightSourceSettings.invoke(self, ((), _ctx))

        def begin_unloadLightSourceSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_unloadLightSourceSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadLightSourceSettings(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_unloadLightSourceSettings.end(self, _r)

        def sizeOfLightSourceSettings(self, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_sizeOfLightSourceSettings.invoke(self, ((), _ctx))

        def begin_sizeOfLightSourceSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_sizeOfLightSourceSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfLightSourceSettings(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_sizeOfLightSourceSettings.end(self, _r)

        def copyLightSourceSettings(self, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_copyLightSourceSettings.invoke(self, ((), _ctx))

        def begin_copyLightSourceSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_copyLightSourceSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyLightSourceSettings(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_copyLightSourceSettings.end(self, _r)

        def addLightSettings(self, target, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_addLightSettings.invoke(self, ((target, ), _ctx))

        def begin_addLightSettings(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_addLightSettings.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addLightSettings(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_addLightSettings.end(self, _r)

        def addAllLightSettingsSet(self, targets, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_addAllLightSettingsSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllLightSettingsSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_addAllLightSettingsSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllLightSettingsSet(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_addAllLightSettingsSet.end(self, _r)

        def removeLightSettings(self, theTarget, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_removeLightSettings.invoke(self, ((theTarget, ), _ctx))

        def begin_removeLightSettings(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_removeLightSettings.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeLightSettings(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_removeLightSettings.end(self, _r)

        def removeAllLightSettingsSet(self, targets, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_removeAllLightSettingsSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllLightSettingsSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_removeAllLightSettingsSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllLightSettingsSet(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_removeAllLightSettingsSet.end(self, _r)

        def clearLightSourceSettings(self, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_clearLightSourceSettings.invoke(self, ((), _ctx))

        def begin_clearLightSourceSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_clearLightSourceSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearLightSourceSettings(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_clearLightSourceSettings.end(self, _r)

        def reloadLightSourceSettings(self, toCopy, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_reloadLightSourceSettings.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadLightSourceSettings(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_reloadLightSourceSettings.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadLightSourceSettings(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_reloadLightSourceSettings.end(self, _r)

        def getExperiment(self, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_getExperiment.invoke(self, ((), _ctx))

        def begin_getExperiment(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_getExperiment.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getExperiment(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_getExperiment.end(self, _r)

        def setExperiment(self, theExperiment, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_setExperiment.invoke(self, ((theExperiment, ), _ctx))

        def begin_setExperiment(self, theExperiment, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_setExperiment.begin(self, ((theExperiment, ), _response, _ex, _sent, _ctx))

        def end_setExperiment(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_setExperiment.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulation._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_ode.model.MicrobeamManipulation._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.MicrobeamManipulationPrx.ice_checkedCast(proxy, '::ode::model::MicrobeamManipulation', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.MicrobeamManipulationPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::MicrobeamManipulation'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_MicrobeamManipulationPrx = IcePy.defineProxy('::ode::model::MicrobeamManipulation', MicrobeamManipulationPrx)

    _M_ode.model._t_MicrobeamManipulation = IcePy.declareClass('::ode::model::MicrobeamManipulation')

    _M_ode.model._t_MicrobeamManipulation = IcePy.defineClass('::ode::model::MicrobeamManipulation', MicrobeamManipulation, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_type', (), _M_ode.model._t_MicrobeamManipulationType, False, 0),
        ('_lightSourceSettingsSeq', (), _M_ode.model._t_MicrobeamManipulationLightSourceSettingsSeq, False, 0),
        ('_lightSourceSettingsLoaded', (), IcePy._t_bool, False, 0),
        ('_experiment', (), _M_ode.model._t_Experiment, False, 0),
        ('_description', (), _M_ode._t_RString, False, 0)
    ))
    MicrobeamManipulation._ice_type = _M_ode.model._t_MicrobeamManipulation

    MicrobeamManipulation._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    MicrobeamManipulation._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    MicrobeamManipulation._op_getType = IcePy.Operation('getType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_MicrobeamManipulationType, False, 0), ())
    MicrobeamManipulation._op_setType = IcePy.Operation('setType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_MicrobeamManipulationType, False, 0),), (), None, ())
    MicrobeamManipulation._op_unloadLightSourceSettings = IcePy.Operation('unloadLightSourceSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    MicrobeamManipulation._op_sizeOfLightSourceSettings = IcePy.Operation('sizeOfLightSourceSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    MicrobeamManipulation._op_copyLightSourceSettings = IcePy.Operation('copyLightSourceSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_MicrobeamManipulationLightSourceSettingsSeq, False, 0), ())
    MicrobeamManipulation._op_addLightSettings = IcePy.Operation('addLightSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSettings, False, 0),), (), None, ())
    MicrobeamManipulation._op_addAllLightSettingsSet = IcePy.Operation('addAllLightSettingsSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_MicrobeamManipulationLightSourceSettingsSeq, False, 0),), (), None, ())
    MicrobeamManipulation._op_removeLightSettings = IcePy.Operation('removeLightSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSettings, False, 0),), (), None, ())
    MicrobeamManipulation._op_removeAllLightSettingsSet = IcePy.Operation('removeAllLightSettingsSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_MicrobeamManipulationLightSourceSettingsSeq, False, 0),), (), None, ())
    MicrobeamManipulation._op_clearLightSourceSettings = IcePy.Operation('clearLightSourceSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    MicrobeamManipulation._op_reloadLightSourceSettings = IcePy.Operation('reloadLightSourceSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_MicrobeamManipulation, False, 0),), (), None, ())
    MicrobeamManipulation._op_getExperiment = IcePy.Operation('getExperiment', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Experiment, False, 0), ())
    MicrobeamManipulation._op_setExperiment = IcePy.Operation('setExperiment', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Experiment, False, 0),), (), None, ())
    MicrobeamManipulation._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    MicrobeamManipulation._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.MicrobeamManipulation = MicrobeamManipulation
    del MicrobeamManipulation

    _M_ode.model.MicrobeamManipulationPrx = MicrobeamManipulationPrx
    del MicrobeamManipulationPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
