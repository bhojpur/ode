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
import ode_model_Units_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.model.enums
_M_ode.model.enums = Ice.openModule('ode.model.enums')

# Start of module ode
__name__ = 'ode'

# Start of module ode.model
__name__ = 'ode.model'

if 'Power' not in _M_ode.model.__dict__:
    _M_ode.model.Power = Ice.createTempClass()
    class Power(Ice.Object):
        """
        Unit of Power which is used through the model. This is not
        an ode.model.IObject implementation and as such does
        not have an ID value. Instead, the entire object is embedded
        into the containing class, so that the value and unit rows
        can be found on the table itself (e.g. lightSource.power
        and lightSource.powerUnit).
        Members:
        value -- PositiveFloat value
        unit -- 
        """
        def __init__(self, _value=0.0, _unit=_M_ode.model.enums.UnitsPower.YOTTAWATT):
            if Ice.getType(self) == _M_ode.model.Power:
                raise RuntimeError('ode.model.Power is an abstract class')
            self._value = _value
            self._unit = _unit

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Power')

        def ice_id(self, current=None):
            return '::ode::model::Power'

        def ice_staticId():
            return '::ode::model::Power'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            """
            Actual value for this unit-based field. The interpretation of
            the value is only possible along with the
            ode.model.enums.UnitsPower enum.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def setValue(self, value, current=None):
            pass

        def getUnit(self, current=None):
            """
            ode.model.enums.UnitsPower instance which is an
            ode.model.IObject
            meaning that its ID is sufficient for identifying equality.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def setUnit(self, unit, current=None):
            pass

        def getSymbol(self, current=None):
            """
            Returns the possibly unicode representation of the ""unit""
            value for display.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def copy(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Power)

        __repr__ = __str__

    _M_ode.model.PowerPrx = Ice.createTempClass()
    class PowerPrx(Ice.ObjectPrx):

        """
        Actual value for this unit-based field. The interpretation of
        the value is only possible along with the
        ode.model.enums.UnitsPower enum.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getValue(self, _ctx=None):
            return _M_ode.model.Power._op_getValue.invoke(self, ((), _ctx))

        """
        Actual value for this unit-based field. The interpretation of
        the value is only possible along with the
        ode.model.enums.UnitsPower enum.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Power._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Actual value for this unit-based field. The interpretation of
        the value is only possible along with the
        ode.model.enums.UnitsPower enum.
        Arguments:
        """
        def end_getValue(self, _r):
            return _M_ode.model.Power._op_getValue.end(self, _r)

        def setValue(self, value, _ctx=None):
            return _M_ode.model.Power._op_setValue.invoke(self, ((value, ), _ctx))

        def begin_setValue(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Power._op_setValue.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        def end_setValue(self, _r):
            return _M_ode.model.Power._op_setValue.end(self, _r)

        """
        ode.model.enums.UnitsPower instance which is an
        ode.model.IObject
        meaning that its ID is sufficient for identifying equality.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getUnit(self, _ctx=None):
            return _M_ode.model.Power._op_getUnit.invoke(self, ((), _ctx))

        """
        ode.model.enums.UnitsPower instance which is an
        ode.model.IObject
        meaning that its ID is sufficient for identifying equality.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getUnit(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Power._op_getUnit.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        ode.model.enums.UnitsPower instance which is an
        ode.model.IObject
        meaning that its ID is sufficient for identifying equality.
        Arguments:
        """
        def end_getUnit(self, _r):
            return _M_ode.model.Power._op_getUnit.end(self, _r)

        def setUnit(self, unit, _ctx=None):
            return _M_ode.model.Power._op_setUnit.invoke(self, ((unit, ), _ctx))

        def begin_setUnit(self, unit, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Power._op_setUnit.begin(self, ((unit, ), _response, _ex, _sent, _ctx))

        def end_setUnit(self, _r):
            return _M_ode.model.Power._op_setUnit.end(self, _r)

        """
        Returns the possibly unicode representation of the ""unit""
        value for display.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getSymbol(self, _ctx=None):
            return _M_ode.model.Power._op_getSymbol.invoke(self, ((), _ctx))

        """
        Returns the possibly unicode representation of the ""unit""
        value for display.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getSymbol(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Power._op_getSymbol.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the possibly unicode representation of the ""unit""
        value for display.
        Arguments:
        """
        def end_getSymbol(self, _r):
            return _M_ode.model.Power._op_getSymbol.end(self, _r)

        def copy(self, _ctx=None):
            return _M_ode.model.Power._op_copy.invoke(self, ((), _ctx))

        def begin_copy(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Power._op_copy.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copy(self, _r):
            return _M_ode.model.Power._op_copy.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.PowerPrx.ice_checkedCast(proxy, '::ode::model::Power', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.PowerPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Power'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_PowerPrx = IcePy.defineProxy('::ode::model::Power', PowerPrx)

    _M_ode.model._t_Power = IcePy.defineClass('::ode::model::Power', Power, -1, (), True, False, None, (), (
        ('_value', (), IcePy._t_double, False, 0),
        ('_unit', (), _M_ode.model.enums._t_UnitsPower, False, 0)
    ))
    Power._ice_type = _M_ode.model._t_Power

    Power._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_double, False, 0), ())
    Power._op_setValue = IcePy.Operation('setValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_double, False, 0),), (), None, ())
    Power._op_getUnit = IcePy.Operation('getUnit', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model.enums._t_UnitsPower, False, 0), ())
    Power._op_setUnit = IcePy.Operation('setUnit', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model.enums._t_UnitsPower, False, 0),), (), None, ())
    Power._op_getSymbol = IcePy.Operation('getSymbol', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_string, False, 0), ())
    Power._op_copy = IcePy.Operation('copy', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Power, False, 0), ())

    _M_ode.model.Power = Power
    del Power

    _M_ode.model.PowerPrx = PowerPrx
    del PowerPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
