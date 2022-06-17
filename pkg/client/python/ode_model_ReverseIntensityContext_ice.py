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
import ode_model_CodomainMapContext_ice

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

if 'ChannelBinding' not in _M_ode.model.__dict__:
    _M_ode.model._t_ChannelBinding = IcePy.declareClass('::ode::model::ChannelBinding')
    _M_ode.model._t_ChannelBindingPrx = IcePy.declareProxy('::ode::model::ChannelBinding')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'ReverseIntensityContext' not in _M_ode.model.__dict__:
    _M_ode.model.ReverseIntensityContext = Ice.createTempClass()
    class ReverseIntensityContext(_M_ode.model.CodomainMapContext):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _channelBinding=None, _reverse=None):
            if Ice.getType(self) == _M_ode.model.ReverseIntensityContext:
                raise RuntimeError('ode.model.ReverseIntensityContext is an abstract class')
            _M_ode.model.CodomainMapContext.__init__(self, _id, _details, _loaded, _version, _channelBinding)
            self._reverse = _reverse

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::CodomainMapContext', '::ode::model::IObject', '::ode::model::ReverseIntensityContext')

        def ice_id(self, current=None):
            return '::ode::model::ReverseIntensityContext'

        def ice_staticId():
            return '::ode::model::ReverseIntensityContext'
        ice_staticId = staticmethod(ice_staticId)

        def getReverse(self, current=None):
            pass

        def setReverse(self, theReverse, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_ReverseIntensityContext)

        __repr__ = __str__

    _M_ode.model.ReverseIntensityContextPrx = Ice.createTempClass()
    class ReverseIntensityContextPrx(_M_ode.model.CodomainMapContextPrx):

        def getReverse(self, _ctx=None):
            return _M_ode.model.ReverseIntensityContext._op_getReverse.invoke(self, ((), _ctx))

        def begin_getReverse(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ReverseIntensityContext._op_getReverse.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getReverse(self, _r):
            return _M_ode.model.ReverseIntensityContext._op_getReverse.end(self, _r)

        def setReverse(self, theReverse, _ctx=None):
            return _M_ode.model.ReverseIntensityContext._op_setReverse.invoke(self, ((theReverse, ), _ctx))

        def begin_setReverse(self, theReverse, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ReverseIntensityContext._op_setReverse.begin(self, ((theReverse, ), _response, _ex, _sent, _ctx))

        def end_setReverse(self, _r):
            return _M_ode.model.ReverseIntensityContext._op_setReverse.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ReverseIntensityContextPrx.ice_checkedCast(proxy, '::ode::model::ReverseIntensityContext', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ReverseIntensityContextPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ReverseIntensityContext'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ReverseIntensityContextPrx = IcePy.defineProxy('::ode::model::ReverseIntensityContext', ReverseIntensityContextPrx)

    _M_ode.model._t_ReverseIntensityContext = IcePy.declareClass('::ode::model::ReverseIntensityContext')

    _M_ode.model._t_ReverseIntensityContext = IcePy.defineClass('::ode::model::ReverseIntensityContext', ReverseIntensityContext, -1, (), True, False, _M_ode.model._t_CodomainMapContext, (), (('_reverse', (), _M_ode._t_RBool, False, 0),))
    ReverseIntensityContext._ice_type = _M_ode.model._t_ReverseIntensityContext

    ReverseIntensityContext._op_getReverse = IcePy.Operation('getReverse', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    ReverseIntensityContext._op_setReverse = IcePy.Operation('setReverse', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())

    _M_ode.model.ReverseIntensityContext = ReverseIntensityContext
    del ReverseIntensityContext

    _M_ode.model.ReverseIntensityContextPrx = ReverseIntensityContextPrx
    del ReverseIntensityContextPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
