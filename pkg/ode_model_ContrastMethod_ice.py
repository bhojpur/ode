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

_M_ode.model.enums.ContrastMethodBrightfield = "Brightfield"

_M_ode.model.enums.ContrastMethodPhase = "Phase"

_M_ode.model.enums.ContrastMethodDIC = "DIC"

_M_ode.model.enums.ContrastMethodHoffmanModulation = "HoffmanModulation"

_M_ode.model.enums.ContrastMethodObliqueIllumination = "ObliqueIllumination"

_M_ode.model.enums.ContrastMethodPolarizedLight = "PolarizedLight"

_M_ode.model.enums.ContrastMethodDarkfield = "Darkfield"

_M_ode.model.enums.ContrastMethodFluorescence = "Fluorescence"

_M_ode.model.enums.ContrastMethodOther = "Other"

_M_ode.model.enums.ContrastMethodUnknown = "Unknown"

# End of module ode.model.enums

__name__ = 'ode.model'

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'ContrastMethod' not in _M_ode.model.__dict__:
    _M_ode.model.ContrastMethod = Ice.createTempClass()
    class ContrastMethod(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _value=None):
            if Ice.getType(self) == _M_ode.model.ContrastMethod:
                raise RuntimeError('ode.model.ContrastMethod is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._value = _value

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::ContrastMethod', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::ContrastMethod'

        def ice_staticId():
            return '::ode::model::ContrastMethod'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def setValue(self, theValue, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_ContrastMethod)

        __repr__ = __str__

    _M_ode.model.ContrastMethodPrx = Ice.createTempClass()
    class ContrastMethodPrx(_M_ode.model.IObjectPrx):

        def getValue(self, _ctx=None):
            return _M_ode.model.ContrastMethod._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ContrastMethod._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.model.ContrastMethod._op_getValue.end(self, _r)

        def setValue(self, theValue, _ctx=None):
            return _M_ode.model.ContrastMethod._op_setValue.invoke(self, ((theValue, ), _ctx))

        def begin_setValue(self, theValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ContrastMethod._op_setValue.begin(self, ((theValue, ), _response, _ex, _sent, _ctx))

        def end_setValue(self, _r):
            return _M_ode.model.ContrastMethod._op_setValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ContrastMethodPrx.ice_checkedCast(proxy, '::ode::model::ContrastMethod', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ContrastMethodPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ContrastMethod'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ContrastMethodPrx = IcePy.defineProxy('::ode::model::ContrastMethod', ContrastMethodPrx)

    _M_ode.model._t_ContrastMethod = IcePy.declareClass('::ode::model::ContrastMethod')

    _M_ode.model._t_ContrastMethod = IcePy.defineClass('::ode::model::ContrastMethod', ContrastMethod, -1, (), True, False, _M_ode.model._t_IObject, (), (('_value', (), _M_ode._t_RString, False, 0),))
    ContrastMethod._ice_type = _M_ode.model._t_ContrastMethod

    ContrastMethod._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    ContrastMethod._op_setValue = IcePy.Operation('setValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.ContrastMethod = ContrastMethod
    del ContrastMethod

    _M_ode.model.ContrastMethodPrx = ContrastMethodPrx
    del ContrastMethodPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
