#!/usr/bin/env python
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

"""
Code-generated ode.model.Temperature implementation,
based on ode.model.PermissionsI
"""

from builtins import str
from past.builtins import basestring
import Ice
import IceImport

IceImport.load("ode_model_Temperature_ice")
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"

from ode_model_UnitBase import UnitBase
from ode.model.enums import UnitsTemperature

from ode.conversions import Add  # nopep8
from ode.conversions import Int  # nopep8
from ode.conversions import Mul  # nopep8
from ode.conversions import Pow  # nopep8
from ode.conversions import Rat  # nopep8
from ode.conversions import Sym  # nopep8

class TemperatureI(_ode_model.Temperature, UnitBase):

    UNIT_VALUES = sorted(UnitsTemperature._enumerators.values())
    CONVERSIONS = dict()
    for val in UNIT_VALUES:
        CONVERSIONS[val] = dict()
    CONVERSIONS[UnitsTemperature.CELSIUS][UnitsTemperature.FAHRENHEIT] = \
        Add(Mul(Rat(Int(9), Int(5)), Sym("c")), Int(32))  # nopep8
    CONVERSIONS[UnitsTemperature.CELSIUS][UnitsTemperature.KELVIN] = \
        Add(Sym("c"), Rat(Int(5463), Int(20)))  # nopep8
    CONVERSIONS[UnitsTemperature.CELSIUS][UnitsTemperature.RANKINE] = \
        Add(Mul(Rat(Int(9), Int(5)), Sym("c")), Rat(Int(49167), Int(100)))  # nopep8
    CONVERSIONS[UnitsTemperature.FAHRENHEIT][UnitsTemperature.CELSIUS] = \
        Add(Mul(Rat(Int(5), Int(9)), Sym("f")), Rat(Int(-160), Int(9)))  # nopep8
    CONVERSIONS[UnitsTemperature.FAHRENHEIT][UnitsTemperature.KELVIN] = \
        Add(Mul(Rat(Int(5), Int(9)), Sym("f")), Rat(Int(45967), Int(180)))  # nopep8
    CONVERSIONS[UnitsTemperature.FAHRENHEIT][UnitsTemperature.RANKINE] = \
        Add(Sym("f"), Rat(Int(45967), Int(100)))  # nopep8
    CONVERSIONS[UnitsTemperature.KELVIN][UnitsTemperature.CELSIUS] = \
        Add(Sym("k"), Rat(Int(-5463), Int(20)))  # nopep8
    CONVERSIONS[UnitsTemperature.KELVIN][UnitsTemperature.FAHRENHEIT] = \
        Add(Mul(Rat(Int(9), Int(5)), Sym("k")), Rat(Int(-45967), Int(100)))  # nopep8
    CONVERSIONS[UnitsTemperature.KELVIN][UnitsTemperature.RANKINE] = \
        Mul(Rat(Int(9), Int(5)), Sym("k"))  # nopep8
    CONVERSIONS[UnitsTemperature.RANKINE][UnitsTemperature.CELSIUS] = \
        Add(Mul(Rat(Int(5), Int(9)), Sym("r")), Rat(Int(-5463), Int(20)))  # nopep8
    CONVERSIONS[UnitsTemperature.RANKINE][UnitsTemperature.FAHRENHEIT] = \
        Add(Sym("r"), Rat(Int(-45967), Int(100)))  # nopep8
    CONVERSIONS[UnitsTemperature.RANKINE][UnitsTemperature.KELVIN] = \
        Mul(Rat(Int(5), Int(9)), Sym("r"))  # nopep8
    del val

    SYMBOLS = dict()
    SYMBOLS["CELSIUS"] = "°C"
    SYMBOLS["FAHRENHEIT"] = "°F"
    SYMBOLS["KELVIN"] = "K"
    SYMBOLS["RANKINE"] = "°R"

    def __init__(self, value=None, unit=None):
        _ode_model.Temperature.__init__(self)

        if unit is None:
            target = None
        elif isinstance(unit, UnitsTemperature):
            target = unit
        elif isinstance(unit, basestring):
            target = getattr(UnitsTemperature, unit)
        else:
            raise Exception("Unknown unit: %s (%s)" % (
                unit, type(unit)
            ))

        if isinstance(value, _ode_model.TemperatureI):
            # This is a copy-constructor call.

            source = value.getUnit()

            if target is None:
                raise Exception("Null target unit")
            if source is None:
                raise Exception("Null source unit")

            if target == source:
                self.setValue(value.getValue())
                self.setUnit(source)
            else:
                c = self.CONVERSIONS.get(source).get(target)
                if c is None:
                    t = (value.getValue(), source, target)
                    msg = "%s %s cannot be converted to %s" % t
                    raise Exception(msg)
                self.setValue(c(value.getValue()))
                self.setUnit(target)
        else:
            self.setValue(value)
            self.setUnit(target)

    def getUnit(self, current=None):
        return self._unit

    def getValue(self, current=None):
        return self._value

    def getSymbol(self, current=None):
        return self.SYMBOLS.get(str(self.getUnit()))

    @staticmethod
    def lookupSymbol(unit):
        return TemperatureI.SYMBOLS.get(str(unit))

    def setUnit(self, unit, current=None):
        self._unit = unit

    def setValue(self, value, current=None):
        self._value = value

    def __str__(self):
        return self._base_string(self.getValue(), self.getUnit())

_ode_model.TemperatureI = TemperatureI