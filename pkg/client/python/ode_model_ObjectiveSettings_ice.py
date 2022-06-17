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

if 'Medium' not in _M_ode.model.__dict__:
    _M_ode.model._t_Medium = IcePy.declareClass('::ode::model::Medium')
    _M_ode.model._t_MediumPrx = IcePy.declareProxy('::ode::model::Medium')

if 'Objective' not in _M_ode.model.__dict__:
    _M_ode.model._t_Objective = IcePy.declareClass('::ode::model::Objective')
    _M_ode.model._t_ObjectivePrx = IcePy.declareProxy('::ode::model::Objective')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'ObjectiveSettings' not in _M_ode.model.__dict__:
    _M_ode.model.ObjectiveSettings = Ice.createTempClass()
    class ObjectiveSettings(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _correctionCollar=None, _medium=None, _refractiveIndex=None, _objective=None):
            if Ice.getType(self) == _M_ode.model.ObjectiveSettings:
                raise RuntimeError('ode.model.ObjectiveSettings is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._correctionCollar = _correctionCollar
            self._medium = _medium
            self._refractiveIndex = _refractiveIndex
            self._objective = _objective

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::ObjectiveSettings')

        def ice_id(self, current=None):
            return '::ode::model::ObjectiveSettings'

        def ice_staticId():
            return '::ode::model::ObjectiveSettings'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getCorrectionCollar(self, current=None):
            pass

        def setCorrectionCollar(self, theCorrectionCollar, current=None):
            pass

        def getMedium(self, current=None):
            pass

        def setMedium(self, theMedium, current=None):
            pass

        def getRefractiveIndex(self, current=None):
            pass

        def setRefractiveIndex(self, theRefractiveIndex, current=None):
            pass

        def getObjective(self, current=None):
            pass

        def setObjective(self, theObjective, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_ObjectiveSettings)

        __repr__ = __str__

    _M_ode.model.ObjectiveSettingsPrx = Ice.createTempClass()
    class ObjectiveSettingsPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.ObjectiveSettings._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.ObjectiveSettings._op_setVersion.end(self, _r)

        def getCorrectionCollar(self, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_getCorrectionCollar.invoke(self, ((), _ctx))

        def begin_getCorrectionCollar(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_getCorrectionCollar.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCorrectionCollar(self, _r):
            return _M_ode.model.ObjectiveSettings._op_getCorrectionCollar.end(self, _r)

        def setCorrectionCollar(self, theCorrectionCollar, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_setCorrectionCollar.invoke(self, ((theCorrectionCollar, ), _ctx))

        def begin_setCorrectionCollar(self, theCorrectionCollar, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_setCorrectionCollar.begin(self, ((theCorrectionCollar, ), _response, _ex, _sent, _ctx))

        def end_setCorrectionCollar(self, _r):
            return _M_ode.model.ObjectiveSettings._op_setCorrectionCollar.end(self, _r)

        def getMedium(self, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_getMedium.invoke(self, ((), _ctx))

        def begin_getMedium(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_getMedium.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMedium(self, _r):
            return _M_ode.model.ObjectiveSettings._op_getMedium.end(self, _r)

        def setMedium(self, theMedium, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_setMedium.invoke(self, ((theMedium, ), _ctx))

        def begin_setMedium(self, theMedium, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_setMedium.begin(self, ((theMedium, ), _response, _ex, _sent, _ctx))

        def end_setMedium(self, _r):
            return _M_ode.model.ObjectiveSettings._op_setMedium.end(self, _r)

        def getRefractiveIndex(self, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_getRefractiveIndex.invoke(self, ((), _ctx))

        def begin_getRefractiveIndex(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_getRefractiveIndex.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRefractiveIndex(self, _r):
            return _M_ode.model.ObjectiveSettings._op_getRefractiveIndex.end(self, _r)

        def setRefractiveIndex(self, theRefractiveIndex, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_setRefractiveIndex.invoke(self, ((theRefractiveIndex, ), _ctx))

        def begin_setRefractiveIndex(self, theRefractiveIndex, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_setRefractiveIndex.begin(self, ((theRefractiveIndex, ), _response, _ex, _sent, _ctx))

        def end_setRefractiveIndex(self, _r):
            return _M_ode.model.ObjectiveSettings._op_setRefractiveIndex.end(self, _r)

        def getObjective(self, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_getObjective.invoke(self, ((), _ctx))

        def begin_getObjective(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_getObjective.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getObjective(self, _r):
            return _M_ode.model.ObjectiveSettings._op_getObjective.end(self, _r)

        def setObjective(self, theObjective, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_setObjective.invoke(self, ((theObjective, ), _ctx))

        def begin_setObjective(self, theObjective, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ObjectiveSettings._op_setObjective.begin(self, ((theObjective, ), _response, _ex, _sent, _ctx))

        def end_setObjective(self, _r):
            return _M_ode.model.ObjectiveSettings._op_setObjective.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ObjectiveSettingsPrx.ice_checkedCast(proxy, '::ode::model::ObjectiveSettings', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ObjectiveSettingsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ObjectiveSettings'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ObjectiveSettingsPrx = IcePy.defineProxy('::ode::model::ObjectiveSettings', ObjectiveSettingsPrx)

    _M_ode.model._t_ObjectiveSettings = IcePy.declareClass('::ode::model::ObjectiveSettings')

    _M_ode.model._t_ObjectiveSettings = IcePy.defineClass('::ode::model::ObjectiveSettings', ObjectiveSettings, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_correctionCollar', (), _M_ode._t_RDouble, False, 0),
        ('_medium', (), _M_ode.model._t_Medium, False, 0),
        ('_refractiveIndex', (), _M_ode._t_RDouble, False, 0),
        ('_objective', (), _M_ode.model._t_Objective, False, 0)
    ))
    ObjectiveSettings._ice_type = _M_ode.model._t_ObjectiveSettings

    ObjectiveSettings._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ObjectiveSettings._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ObjectiveSettings._op_getCorrectionCollar = IcePy.Operation('getCorrectionCollar', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    ObjectiveSettings._op_setCorrectionCollar = IcePy.Operation('setCorrectionCollar', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    ObjectiveSettings._op_getMedium = IcePy.Operation('getMedium', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Medium, False, 0), ())
    ObjectiveSettings._op_setMedium = IcePy.Operation('setMedium', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Medium, False, 0),), (), None, ())
    ObjectiveSettings._op_getRefractiveIndex = IcePy.Operation('getRefractiveIndex', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    ObjectiveSettings._op_setRefractiveIndex = IcePy.Operation('setRefractiveIndex', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    ObjectiveSettings._op_getObjective = IcePy.Operation('getObjective', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Objective, False, 0), ())
    ObjectiveSettings._op_setObjective = IcePy.Operation('setObjective', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Objective, False, 0),), (), None, ())

    _M_ode.model.ObjectiveSettings = ObjectiveSettings
    del ObjectiveSettings

    _M_ode.model.ObjectiveSettingsPrx = ObjectiveSettingsPrx
    del ObjectiveSettingsPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
