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

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Start of module ode
__name__ = 'ode'

if 'RObject' not in _M_ode.__dict__:
    _M_ode.RObject = Ice.createTempClass()
    class RObject(_M_ode.RType):
        """
        Wrapper for an ode.model.IObject instance.
        """
        def __init__(self, _val=None):
            if Ice.getType(self) == _M_ode.RObject:
                raise RuntimeError('ode.RObject is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RObject', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RObject'

        def ice_staticId():
            return '::ode::RObject'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RObject)

        __repr__ = __str__

    _M_ode.RObjectPrx = Ice.createTempClass()
    class RObjectPrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RObject._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RObject._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RObject._op_getValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RObjectPrx.ice_checkedCast(proxy, '::ode::RObject', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RObjectPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RObject'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RObjectPrx = IcePy.defineProxy('::ode::RObject', RObjectPrx)

    _M_ode._t_RObject = IcePy.declareClass('::ode::RObject')

    _M_ode._t_RObject = IcePy.defineClass('::ode::RObject', RObject, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), _M_ode.model._t_IObject, False, 0),))
    RObject._ice_type = _M_ode._t_RObject

    RObject._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_IObject, False, 0), ())

    _M_ode.RObject = RObject
    del RObject

    _M_ode.RObjectPrx = RObjectPrx
    del RObjectPrx

# End of module ode
