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

if 'ChannelBinding' not in _M_ode.model.__dict__:
    _M_ode.model._t_ChannelBinding = IcePy.declareClass('::ode::model::ChannelBinding')
    _M_ode.model._t_ChannelBindingPrx = IcePy.declareProxy('::ode::model::ChannelBinding')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'CodomainMapContext' not in _M_ode.model.__dict__:
    _M_ode.model.CodomainMapContext = Ice.createTempClass()
    class CodomainMapContext(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _channelBinding=None):
            if Ice.getType(self) == _M_ode.model.CodomainMapContext:
                raise RuntimeError('ode.model.CodomainMapContext is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._channelBinding = _channelBinding

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::CodomainMapContext', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::CodomainMapContext'

        def ice_staticId():
            return '::ode::model::CodomainMapContext'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getChannelBinding(self, current=None):
            pass

        def setChannelBinding(self, theChannelBinding, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_CodomainMapContext)

        __repr__ = __str__

    _M_ode.model.CodomainMapContextPrx = Ice.createTempClass()
    class CodomainMapContextPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.CodomainMapContext._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.CodomainMapContext._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.CodomainMapContext._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.CodomainMapContext._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.CodomainMapContext._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.CodomainMapContext._op_setVersion.end(self, _r)

        def getChannelBinding(self, _ctx=None):
            return _M_ode.model.CodomainMapContext._op_getChannelBinding.invoke(self, ((), _ctx))

        def begin_getChannelBinding(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.CodomainMapContext._op_getChannelBinding.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getChannelBinding(self, _r):
            return _M_ode.model.CodomainMapContext._op_getChannelBinding.end(self, _r)

        def setChannelBinding(self, theChannelBinding, _ctx=None):
            return _M_ode.model.CodomainMapContext._op_setChannelBinding.invoke(self, ((theChannelBinding, ), _ctx))

        def begin_setChannelBinding(self, theChannelBinding, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.CodomainMapContext._op_setChannelBinding.begin(self, ((theChannelBinding, ), _response, _ex, _sent, _ctx))

        def end_setChannelBinding(self, _r):
            return _M_ode.model.CodomainMapContext._op_setChannelBinding.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.CodomainMapContextPrx.ice_checkedCast(proxy, '::ode::model::CodomainMapContext', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.CodomainMapContextPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::CodomainMapContext'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_CodomainMapContextPrx = IcePy.defineProxy('::ode::model::CodomainMapContext', CodomainMapContextPrx)

    _M_ode.model._t_CodomainMapContext = IcePy.declareClass('::ode::model::CodomainMapContext')

    _M_ode.model._t_CodomainMapContext = IcePy.defineClass('::ode::model::CodomainMapContext', CodomainMapContext, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_channelBinding', (), _M_ode.model._t_ChannelBinding, False, 0)
    ))
    CodomainMapContext._ice_type = _M_ode.model._t_CodomainMapContext

    CodomainMapContext._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    CodomainMapContext._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    CodomainMapContext._op_getChannelBinding = IcePy.Operation('getChannelBinding', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ChannelBinding, False, 0), ())
    CodomainMapContext._op_setChannelBinding = IcePy.Operation('setChannelBinding', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelBinding, False, 0),), (), None, ())

    _M_ode.model.CodomainMapContext = CodomainMapContext
    del CodomainMapContext

    _M_ode.model.CodomainMapContextPrx = CodomainMapContextPrx
    del CodomainMapContextPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
