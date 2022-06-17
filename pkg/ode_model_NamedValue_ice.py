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

# Start of module ode
_M_ode = Ice.openModule('ode')
__name__ = 'ode'

# Start of module ode.model
_M_ode.model = Ice.openModule('ode.model')
__name__ = 'ode.model'

if 'NamedValue' not in _M_ode.model.__dict__:
    _M_ode.model.NamedValue = Ice.createTempClass()
    class NamedValue(Ice.Object):
        """
        Simple Pair-like container which is
        used in a sequence to support ordered maps.
        """
        def __init__(self, name='', value=''):
            self.name = name
            self.value = value

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::NamedValue')

        def ice_id(self, current=None):
            return '::ode::model::NamedValue'

        def ice_staticId():
            return '::ode::model::NamedValue'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_NamedValue)

        __repr__ = __str__

    _M_ode.model.NamedValuePrx = Ice.createTempClass()
    class NamedValuePrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.NamedValuePrx.ice_checkedCast(proxy, '::ode::model::NamedValue', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.NamedValuePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::NamedValue'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_NamedValuePrx = IcePy.defineProxy('::ode::model::NamedValue', NamedValuePrx)

    _M_ode.model._t_NamedValue = IcePy.defineClass('::ode::model::NamedValue', NamedValue, -1, (), False, False, None, (), (
        ('name', (), IcePy._t_string, False, 0),
        ('value', (), IcePy._t_string, False, 0)
    ))
    NamedValue._ice_type = _M_ode.model._t_NamedValue

    _M_ode.model.NamedValue = NamedValue
    del NamedValue

    _M_ode.model.NamedValuePrx = NamedValuePrx
    del NamedValuePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
