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

from sympy import Eq
from sympy import solve
from sympy import Symbol
from sympy import srepr

from sympy.core.expr import Expr
from sympy.core.numbers import Integer
from sympy.core.numbers import Pi
from sympy.core.numbers import Rational
from sympy.core.symbol import Symbol

from sympy.physics.unitsystems import prefixes as sp
from sympy.printing.repr import ReprPrinter

from collections import defaultdict
from pprint import pprint

def STR_SORT(lhs, rhs):
    return cmp(str(lhs), str(rhs))

class Printer(ReprPrinter):

    MAX_INT = 2**31 -1

    def pow10(self, p):
        s = str(p)
        for x in range(60, 3, -1):
            if s.endswith("0" * x):
                front = s[0:-x]
                if front == "1":
                    front = None
                return front, x
        return (None, None)

    def _print_Integer(self, expr):
        f, p = self.pow10(expr.p)
        if p:
            p = "Pow(10, %s)" % p
            if not f:
                return p
            f = self._print_Integer(Integer(int(f)))
            return "Mul(%s, %s)" % (f, p)
        if expr.p > self.MAX_INT:
            return 'Int("%s")' % expr.p
        else:
            return 'Int(%i)' % expr.p

    def _print_Rational(self, expr):
        p = self._print_Integer(Integer(expr.p))
        q = self._print_Integer(Integer(expr.q))
        return 'Rat(%s, %s)' % (p, q)

    def _print_Symbol(self, expr):
        return '%s("%s")' % (
            expr.__class__.__name__[0:3], expr.name)

def for_each():
    for name, prefix in sorted(sp.PREFIXES.items()):
        yield name, prefix.name, prefix.factor

def add_si(key, value, units, equations):
    for viz, sym, factor in for_each():
        x = "%s%s" % (sym, key)
        s = Symbol(x)
        units[s] = sym.upper() + value
        eq = Eq(key, factor * s)
        equations.append(eq)

def solve_for(equations, source):
    try:
        x = solve(
            equations,
            exclude=[source],
            map=True,
            rational=True)
        if isinstance(x, list):
            return x[0]
        else:
            return x
    except KeyboardInterrupt:
        import sys
        sys.exit(1)

def print_table(units, equations):
    sz = len(units.keys())
    fmt = "%16s\t" * (sz+1)
    print(fmt % tuple(["."] + units.keys()))
    for source in units.keys():
        data = [source]
        x = solve_for(equations, source)
        for target in units.keys():
            if source == target:
                data.append(" ")
            else:
                data.append(x[target])
        data = tuple(data)
        print(fmt % data)

def gen_conv(units, equations):
    for source in sorted(units.keys(), STR_SORT):
        if units[source] in ("REFERENCEFRAME", "PIXEL"):
            # HACK: to do this properly it's
            # likely necessary to check the
            # resulting equations for the
            # source variable.
            continue
        x = solve_for(equations, source)
        for target in sorted(units.keys(), STR_SORT):
            if source == target:
                continue
            elif target not in x:
                continue
            eqn = x[target]
            to_check = [eqn.args]
            for chk in to_check:
                for arg in chk:
                    # Assume top-level is Add or Mul
                    if isinstance(arg, (Integer, Pi, Rational)):
                        continue
                    elif isinstance(arg, Symbol):
                        if source == arg:
                            continue
                    elif isinstance(arg, Expr):
                        to_check.append(arg.args)
                        continue
                    raise Exception(
                        "%s:%s -> %s Found %s (%s)\n%s" % (
                            source, target, eqn, arg, type(arg),
                            "\n".join([str(x) for x in x.items()])
                        )
                    )
            yield units[source], units[target], eqn

def module_conversions(module):
    for x in gen_conv(module.units, module.equations):
        yield x

def print_conversions(*modules):
    for mod in modules:
        for x in module_conversions(mod):
            print ('("' + mod.NAME + '", "%s", "%s"): "%s"' % (
                x[0], x[1], srepr(x[2])))

def print_python(*modules):
    printer = Printer()
    mods = defaultdict(lambda: defaultdict(dict))
    for mod in modules:
        for x in module_conversions(mod):
            s = printer.doprint(x[2])
            mods[mod.NAME][x[0]][x[1]] = s
    mods = mods.items()
    mods = [(k, dict(v)) for k, v in mods]
    mods = dict(mods)

    print("EQUATIONS = %s" % mods)