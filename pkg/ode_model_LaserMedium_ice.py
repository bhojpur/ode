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

_M_ode.model.enums.LaserMediumRhodamine6G = "Rhodamine6G"

_M_ode.model.enums.LaserMediumCoumarinC30 = "CoumarinC30"

_M_ode.model.enums.LaserMediumArFl = "ArFl"

_M_ode.model.enums.LaserMediumArCl = "ArCl"

_M_ode.model.enums.LaserMediumKrFl = "KrFl"

_M_ode.model.enums.LaserMediumKrCl = "KrCl"

_M_ode.model.enums.LaserMediumXeFl = "XeFl"

_M_ode.model.enums.LaserMediumXeCl = "XeCl"

_M_ode.model.enums.LaserMediumXeBr = "XeBr"

_M_ode.model.enums.LaserMediumGaAs = "GaAs"

_M_ode.model.enums.LaserMediumGaAlAs = "GaAlAs"

_M_ode.model.enums.LaserMediumEMinus = "EMinus"

_M_ode.model.enums.LaserMediumCu = "Cu"

_M_ode.model.enums.LaserMediumAg = "Ag"

_M_ode.model.enums.LaserMediumN = "N"

_M_ode.model.enums.LaserMediumAr = "Ar"

_M_ode.model.enums.LaserMediumKr = "Kr"

_M_ode.model.enums.LaserMediumXe = "Xe"

_M_ode.model.enums.LaserMediumHeNe = "HeNe"

_M_ode.model.enums.LaserMediumHeCd = "HeCd"

_M_ode.model.enums.LaserMediumCO = "CO"

_M_ode.model.enums.LaserMediumCO2 = "CO2"

_M_ode.model.enums.LaserMediumH2O = "H2O"

_M_ode.model.enums.LaserMediumHFl = "HFl"

_M_ode.model.enums.LaserMediumNdGlass = "NdGlass"

_M_ode.model.enums.LaserMediumNdYAG = "NdYAG"

_M_ode.model.enums.LaserMediumErGlass = "ErGlass"

_M_ode.model.enums.LaserMediumErYAG = "ErYAG"

_M_ode.model.enums.LaserMediumHoYLF = "HoYLF"

_M_ode.model.enums.LaserMediumHoYAG = "HoYAG"

_M_ode.model.enums.LaserMediumRuby = "Ruby"

_M_ode.model.enums.LaserMediumTiSapphire = "TiSapphire"

_M_ode.model.enums.LaserMediumAlexandrite = "Alexandrite"

_M_ode.model.enums.LaserMediumOther = "Other"

_M_ode.model.enums.LaserMediumUnknown = "Unknown"

# End of module ode.model.enums

__name__ = 'ode.model'

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'LaserMedium' not in _M_ode.model.__dict__:
    _M_ode.model.LaserMedium = Ice.createTempClass()
    class LaserMedium(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _value=None):
            if Ice.getType(self) == _M_ode.model.LaserMedium:
                raise RuntimeError('ode.model.LaserMedium is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._value = _value

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::LaserMedium')

        def ice_id(self, current=None):
            return '::ode::model::LaserMedium'

        def ice_staticId():
            return '::ode::model::LaserMedium'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def setValue(self, theValue, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_LaserMedium)

        __repr__ = __str__

    _M_ode.model.LaserMediumPrx = Ice.createTempClass()
    class LaserMediumPrx(_M_ode.model.IObjectPrx):

        def getValue(self, _ctx=None):
            return _M_ode.model.LaserMedium._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LaserMedium._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.model.LaserMedium._op_getValue.end(self, _r)

        def setValue(self, theValue, _ctx=None):
            return _M_ode.model.LaserMedium._op_setValue.invoke(self, ((theValue, ), _ctx))

        def begin_setValue(self, theValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LaserMedium._op_setValue.begin(self, ((theValue, ), _response, _ex, _sent, _ctx))

        def end_setValue(self, _r):
            return _M_ode.model.LaserMedium._op_setValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.LaserMediumPrx.ice_checkedCast(proxy, '::ode::model::LaserMedium', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.LaserMediumPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::LaserMedium'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_LaserMediumPrx = IcePy.defineProxy('::ode::model::LaserMedium', LaserMediumPrx)

    _M_ode.model._t_LaserMedium = IcePy.declareClass('::ode::model::LaserMedium')

    _M_ode.model._t_LaserMedium = IcePy.defineClass('::ode::model::LaserMedium', LaserMedium, -1, (), True, False, _M_ode.model._t_IObject, (), (('_value', (), _M_ode._t_RString, False, 0),))
    LaserMedium._ice_type = _M_ode.model._t_LaserMedium

    LaserMedium._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    LaserMedium._op_setValue = IcePy.Operation('setValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.LaserMedium = LaserMedium
    del LaserMedium

    _M_ode.model.LaserMediumPrx = LaserMediumPrx
    del LaserMediumPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
