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

if 'ContrastStretchingContext' not in _M_ode.model.__dict__:
    _M_ode.model.ContrastStretchingContext = Ice.createTempClass()
    class ContrastStretchingContext(_M_ode.model.CodomainMapContext):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _channelBinding=None, _xstart=None, _ystart=None, _xend=None, _yend=None):
            if Ice.getType(self) == _M_ode.model.ContrastStretchingContext:
                raise RuntimeError('ode.model.ContrastStretchingContext is an abstract class')
            _M_ode.model.CodomainMapContext.__init__(self, _id, _details, _loaded, _version, _channelBinding)
            self._xstart = _xstart
            self._ystart = _ystart
            self._xend = _xend
            self._yend = _yend

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::CodomainMapContext', '::ode::model::ContrastStretchingContext', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::ContrastStretchingContext'

        def ice_staticId():
            return '::ode::model::ContrastStretchingContext'
        ice_staticId = staticmethod(ice_staticId)

        def getXstart(self, current=None):
            pass

        def setXstart(self, theXstart, current=None):
            pass

        def getYstart(self, current=None):
            pass

        def setYstart(self, theYstart, current=None):
            pass

        def getXend(self, current=None):
            pass

        def setXend(self, theXend, current=None):
            pass

        def getYend(self, current=None):
            pass

        def setYend(self, theYend, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_ContrastStretchingContext)

        __repr__ = __str__

    _M_ode.model.ContrastStretchingContextPrx = Ice.createTempClass()
    class ContrastStretchingContextPrx(_M_ode.model.CodomainMapContextPrx):

        def getXstart(self, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_getXstart.invoke(self, ((), _ctx))

        def begin_getXstart(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_getXstart.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getXstart(self, _r):
            return _M_ode.model.ContrastStretchingContext._op_getXstart.end(self, _r)

        def setXstart(self, theXstart, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_setXstart.invoke(self, ((theXstart, ), _ctx))

        def begin_setXstart(self, theXstart, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_setXstart.begin(self, ((theXstart, ), _response, _ex, _sent, _ctx))

        def end_setXstart(self, _r):
            return _M_ode.model.ContrastStretchingContext._op_setXstart.end(self, _r)

        def getYstart(self, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_getYstart.invoke(self, ((), _ctx))

        def begin_getYstart(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_getYstart.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getYstart(self, _r):
            return _M_ode.model.ContrastStretchingContext._op_getYstart.end(self, _r)

        def setYstart(self, theYstart, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_setYstart.invoke(self, ((theYstart, ), _ctx))

        def begin_setYstart(self, theYstart, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_setYstart.begin(self, ((theYstart, ), _response, _ex, _sent, _ctx))

        def end_setYstart(self, _r):
            return _M_ode.model.ContrastStretchingContext._op_setYstart.end(self, _r)

        def getXend(self, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_getXend.invoke(self, ((), _ctx))

        def begin_getXend(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_getXend.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getXend(self, _r):
            return _M_ode.model.ContrastStretchingContext._op_getXend.end(self, _r)

        def setXend(self, theXend, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_setXend.invoke(self, ((theXend, ), _ctx))

        def begin_setXend(self, theXend, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_setXend.begin(self, ((theXend, ), _response, _ex, _sent, _ctx))

        def end_setXend(self, _r):
            return _M_ode.model.ContrastStretchingContext._op_setXend.end(self, _r)

        def getYend(self, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_getYend.invoke(self, ((), _ctx))

        def begin_getYend(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_getYend.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getYend(self, _r):
            return _M_ode.model.ContrastStretchingContext._op_getYend.end(self, _r)

        def setYend(self, theYend, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_setYend.invoke(self, ((theYend, ), _ctx))

        def begin_setYend(self, theYend, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ContrastStretchingContext._op_setYend.begin(self, ((theYend, ), _response, _ex, _sent, _ctx))

        def end_setYend(self, _r):
            return _M_ode.model.ContrastStretchingContext._op_setYend.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ContrastStretchingContextPrx.ice_checkedCast(proxy, '::ode::model::ContrastStretchingContext', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ContrastStretchingContextPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ContrastStretchingContext'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ContrastStretchingContextPrx = IcePy.defineProxy('::ode::model::ContrastStretchingContext', ContrastStretchingContextPrx)

    _M_ode.model._t_ContrastStretchingContext = IcePy.declareClass('::ode::model::ContrastStretchingContext')

    _M_ode.model._t_ContrastStretchingContext = IcePy.defineClass('::ode::model::ContrastStretchingContext', ContrastStretchingContext, -1, (), True, False, _M_ode.model._t_CodomainMapContext, (), (
        ('_xstart', (), _M_ode._t_RInt, False, 0),
        ('_ystart', (), _M_ode._t_RInt, False, 0),
        ('_xend', (), _M_ode._t_RInt, False, 0),
        ('_yend', (), _M_ode._t_RInt, False, 0)
    ))
    ContrastStretchingContext._ice_type = _M_ode.model._t_ContrastStretchingContext

    ContrastStretchingContext._op_getXstart = IcePy.Operation('getXstart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ContrastStretchingContext._op_setXstart = IcePy.Operation('setXstart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ContrastStretchingContext._op_getYstart = IcePy.Operation('getYstart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ContrastStretchingContext._op_setYstart = IcePy.Operation('setYstart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ContrastStretchingContext._op_getXend = IcePy.Operation('getXend', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ContrastStretchingContext._op_setXend = IcePy.Operation('setXend', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ContrastStretchingContext._op_getYend = IcePy.Operation('getYend', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ContrastStretchingContext._op_setYend = IcePy.Operation('setYend', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())

    _M_ode.model.ContrastStretchingContext = ContrastStretchingContext
    del ContrastStretchingContext

    _M_ode.model.ContrastStretchingContextPrx = ContrastStretchingContextPrx
    del ContrastStretchingContextPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
