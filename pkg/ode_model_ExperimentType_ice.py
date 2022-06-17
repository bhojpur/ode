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

# Start of module ode.model.enums
_M_ode.model.enums = Ice.openModule('ode.model.enums')
__name__ = 'ode.model.enums'

_M_ode.model.enums.ExperimentTypeFP = "FP"

_M_ode.model.enums.ExperimentTypeFRET = "FRET"

_M_ode.model.enums.ExperimentTypeTimeLapse = "TimeLapse"

_M_ode.model.enums.ExperimentTypeFourDPlus = "FourDPlus"

_M_ode.model.enums.ExperimentTypeScreen = "Screen"

_M_ode.model.enums.ExperimentTypeImmunocytochemistry = "Immunocytochemistry"

_M_ode.model.enums.ExperimentTypeImmunofluorescence = "Immunofluorescence"

_M_ode.model.enums.ExperimentTypeFISH = "FISH"

_M_ode.model.enums.ExperimentTypeElectrophysiology = "Electrophysiology"

_M_ode.model.enums.ExperimentTypeIonImaging = "IonImaging"

_M_ode.model.enums.ExperimentTypeColocalization = "Colocalization"

_M_ode.model.enums.ExperimentTypePGIDocumentation = "PGIDocumentation"

_M_ode.model.enums.ExperimentTypeFluorescenceLifetime = "FluorescenceLifetime"

_M_ode.model.enums.ExperimentTypeSpectralImaging = "SpectralImaging"

_M_ode.model.enums.ExperimentTypePhotobleaching = "Photobleaching"

_M_ode.model.enums.ExperimentTypeOther = "Other"

_M_ode.model.enums.ExperimentTypeUnknown = "Unknown"

# End of module ode.model.enums

__name__ = 'ode.model'

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'ExperimentType' not in _M_ode.model.__dict__:
    _M_ode.model.ExperimentType = Ice.createTempClass()
    class ExperimentType(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _value=None):
            if Ice.getType(self) == _M_ode.model.ExperimentType:
                raise RuntimeError('ode.model.ExperimentType is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._value = _value

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::ExperimentType', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::ExperimentType'

        def ice_staticId():
            return '::ode::model::ExperimentType'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def setValue(self, theValue, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_ExperimentType)

        __repr__ = __str__

    _M_ode.model.ExperimentTypePrx = Ice.createTempClass()
    class ExperimentTypePrx(_M_ode.model.IObjectPrx):

        def getValue(self, _ctx=None):
            return _M_ode.model.ExperimentType._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ExperimentType._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.model.ExperimentType._op_getValue.end(self, _r)

        def setValue(self, theValue, _ctx=None):
            return _M_ode.model.ExperimentType._op_setValue.invoke(self, ((theValue, ), _ctx))

        def begin_setValue(self, theValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ExperimentType._op_setValue.begin(self, ((theValue, ), _response, _ex, _sent, _ctx))

        def end_setValue(self, _r):
            return _M_ode.model.ExperimentType._op_setValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ExperimentTypePrx.ice_checkedCast(proxy, '::ode::model::ExperimentType', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ExperimentTypePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ExperimentType'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ExperimentTypePrx = IcePy.defineProxy('::ode::model::ExperimentType', ExperimentTypePrx)

    _M_ode.model._t_ExperimentType = IcePy.declareClass('::ode::model::ExperimentType')

    _M_ode.model._t_ExperimentType = IcePy.defineClass('::ode::model::ExperimentType', ExperimentType, -1, (), True, False, _M_ode.model._t_IObject, (), (('_value', (), _M_ode._t_RString, False, 0),))
    ExperimentType._ice_type = _M_ode.model._t_ExperimentType

    ExperimentType._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    ExperimentType._op_setValue = IcePy.Operation('setValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.ExperimentType = ExperimentType
    del ExperimentType

    _M_ode.model.ExperimentTypePrx = ExperimentTypePrx
    del ExperimentTypePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
