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

"""
Code-generated ode.model.${name} implementation,
based on ode.model.PermissionsI
"""

import Ice
import IceImport

IceImport.load("ode_model_${name}_ice")

_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"

from ode_model_UnitBase import UnitBase
from ode.model.enums import Units${name}

from ode.conversions import Add  # nopep8
from ode.conversions import Int  # nopep8
from ode.conversions import Mul  # nopep8
from ode.conversions import Pow  # nopep8
from ode.conversions import Rat  # nopep8
from ode.conversions import Sym  # nopep8

class ${name}I(_ode_model.${name}, UnitBase):

    UNIT_VALUES = sorted(Units${name}._enumerators.values())
    CONVERSIONS = dict()
    for val in UNIT_VALUES:
        CONVERSIONS[val] = dict()
{% for cfrom in sorted(equations) %}\
{% for cto, equation in sorted(equations.get(cfrom, {}).items()) %}\
{% if cfrom != cto %}\
    CONVERSIONS[Units${name}.${cfrom}][Units${name}.${cto}] = \\
        ${equation}  # nopep8
{% end %}\
{% end %}\
{% end %}\
    del val

    SYMBOLS = dict()
{% for x in sorted(items) %}\
    SYMBOLS["${x.CODE}"] = "${x.SYMBOL}"
{% end %}\

    def __init__(self, value=None, unit=None):
        _ode_model.${name}.__init__(self)

        if unit is None:
            target = None
        elif isinstance(unit, Units${name}):
            target = unit
        elif isinstance(unit, (str, unicode)):
            target = getattr(Units${name}, unit)
        else:
            raise Exception("Unknown unit: %s (%s)" % (
                unit, type(unit)
            ))

        if isinstance(value, _ode_model.${name}I):
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
        return ${name}I.SYMBOLS.get(str(unit))

    def setUnit(self, unit, current=None):
        self._unit = unit

    def setValue(self, value, current=None):
        self._value = value

    def __str__(self):
        return self._base_string(self.getValue(), self.getUnit())

_ode_model.${name}I = ${name}I\