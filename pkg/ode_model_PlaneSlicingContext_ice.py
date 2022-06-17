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

if 'PlaneSlicingContext' not in _M_ode.model.__dict__:
    _M_ode.model.PlaneSlicingContext = Ice.createTempClass()
    class PlaneSlicingContext(_M_ode.model.CodomainMapContext):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _channelBinding=None, _upperLimit=None, _lowerLimit=None, _planeSelected=None, _planePrevious=None, _constant=None):
            if Ice.getType(self) == _M_ode.model.PlaneSlicingContext:
                raise RuntimeError('ode.model.PlaneSlicingContext is an abstract class')
            _M_ode.model.CodomainMapContext.__init__(self, _id, _details, _loaded, _version, _channelBinding)
            self._upperLimit = _upperLimit
            self._lowerLimit = _lowerLimit
            self._planeSelected = _planeSelected
            self._planePrevious = _planePrevious
            self._constant = _constant

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::CodomainMapContext', '::ode::model::IObject', '::ode::model::PlaneSlicingContext')

        def ice_id(self, current=None):
            return '::ode::model::PlaneSlicingContext'

        def ice_staticId():
            return '::ode::model::PlaneSlicingContext'
        ice_staticId = staticmethod(ice_staticId)

        def getUpperLimit(self, current=None):
            pass

        def setUpperLimit(self, theUpperLimit, current=None):
            pass

        def getLowerLimit(self, current=None):
            pass

        def setLowerLimit(self, theLowerLimit, current=None):
            pass

        def getPlaneSelected(self, current=None):
            pass

        def setPlaneSelected(self, thePlaneSelected, current=None):
            pass

        def getPlanePrevious(self, current=None):
            pass

        def setPlanePrevious(self, thePlanePrevious, current=None):
            pass

        def getConstant(self, current=None):
            pass

        def setConstant(self, theConstant, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_PlaneSlicingContext)

        __repr__ = __str__

    _M_ode.model.PlaneSlicingContextPrx = Ice.createTempClass()
    class PlaneSlicingContextPrx(_M_ode.model.CodomainMapContextPrx):

        def getUpperLimit(self, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_getUpperLimit.invoke(self, ((), _ctx))

        def begin_getUpperLimit(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_getUpperLimit.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getUpperLimit(self, _r):
            return _M_ode.model.PlaneSlicingContext._op_getUpperLimit.end(self, _r)

        def setUpperLimit(self, theUpperLimit, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_setUpperLimit.invoke(self, ((theUpperLimit, ), _ctx))

        def begin_setUpperLimit(self, theUpperLimit, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_setUpperLimit.begin(self, ((theUpperLimit, ), _response, _ex, _sent, _ctx))

        def end_setUpperLimit(self, _r):
            return _M_ode.model.PlaneSlicingContext._op_setUpperLimit.end(self, _r)

        def getLowerLimit(self, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_getLowerLimit.invoke(self, ((), _ctx))

        def begin_getLowerLimit(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_getLowerLimit.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLowerLimit(self, _r):
            return _M_ode.model.PlaneSlicingContext._op_getLowerLimit.end(self, _r)

        def setLowerLimit(self, theLowerLimit, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_setLowerLimit.invoke(self, ((theLowerLimit, ), _ctx))

        def begin_setLowerLimit(self, theLowerLimit, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_setLowerLimit.begin(self, ((theLowerLimit, ), _response, _ex, _sent, _ctx))

        def end_setLowerLimit(self, _r):
            return _M_ode.model.PlaneSlicingContext._op_setLowerLimit.end(self, _r)

        def getPlaneSelected(self, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_getPlaneSelected.invoke(self, ((), _ctx))

        def begin_getPlaneSelected(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_getPlaneSelected.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPlaneSelected(self, _r):
            return _M_ode.model.PlaneSlicingContext._op_getPlaneSelected.end(self, _r)

        def setPlaneSelected(self, thePlaneSelected, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_setPlaneSelected.invoke(self, ((thePlaneSelected, ), _ctx))

        def begin_setPlaneSelected(self, thePlaneSelected, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_setPlaneSelected.begin(self, ((thePlaneSelected, ), _response, _ex, _sent, _ctx))

        def end_setPlaneSelected(self, _r):
            return _M_ode.model.PlaneSlicingContext._op_setPlaneSelected.end(self, _r)

        def getPlanePrevious(self, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_getPlanePrevious.invoke(self, ((), _ctx))

        def begin_getPlanePrevious(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_getPlanePrevious.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPlanePrevious(self, _r):
            return _M_ode.model.PlaneSlicingContext._op_getPlanePrevious.end(self, _r)

        def setPlanePrevious(self, thePlanePrevious, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_setPlanePrevious.invoke(self, ((thePlanePrevious, ), _ctx))

        def begin_setPlanePrevious(self, thePlanePrevious, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_setPlanePrevious.begin(self, ((thePlanePrevious, ), _response, _ex, _sent, _ctx))

        def end_setPlanePrevious(self, _r):
            return _M_ode.model.PlaneSlicingContext._op_setPlanePrevious.end(self, _r)

        def getConstant(self, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_getConstant.invoke(self, ((), _ctx))

        def begin_getConstant(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_getConstant.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getConstant(self, _r):
            return _M_ode.model.PlaneSlicingContext._op_getConstant.end(self, _r)

        def setConstant(self, theConstant, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_setConstant.invoke(self, ((theConstant, ), _ctx))

        def begin_setConstant(self, theConstant, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContext._op_setConstant.begin(self, ((theConstant, ), _response, _ex, _sent, _ctx))

        def end_setConstant(self, _r):
            return _M_ode.model.PlaneSlicingContext._op_setConstant.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.PlaneSlicingContextPrx.ice_checkedCast(proxy, '::ode::model::PlaneSlicingContext', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.PlaneSlicingContextPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::PlaneSlicingContext'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_PlaneSlicingContextPrx = IcePy.defineProxy('::ode::model::PlaneSlicingContext', PlaneSlicingContextPrx)

    _M_ode.model._t_PlaneSlicingContext = IcePy.declareClass('::ode::model::PlaneSlicingContext')

    _M_ode.model._t_PlaneSlicingContext = IcePy.defineClass('::ode::model::PlaneSlicingContext', PlaneSlicingContext, -1, (), True, False, _M_ode.model._t_CodomainMapContext, (), (
        ('_upperLimit', (), _M_ode._t_RInt, False, 0),
        ('_lowerLimit', (), _M_ode._t_RInt, False, 0),
        ('_planeSelected', (), _M_ode._t_RInt, False, 0),
        ('_planePrevious', (), _M_ode._t_RInt, False, 0),
        ('_constant', (), _M_ode._t_RBool, False, 0)
    ))
    PlaneSlicingContext._ice_type = _M_ode.model._t_PlaneSlicingContext

    PlaneSlicingContext._op_getUpperLimit = IcePy.Operation('getUpperLimit', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    PlaneSlicingContext._op_setUpperLimit = IcePy.Operation('setUpperLimit', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    PlaneSlicingContext._op_getLowerLimit = IcePy.Operation('getLowerLimit', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    PlaneSlicingContext._op_setLowerLimit = IcePy.Operation('setLowerLimit', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    PlaneSlicingContext._op_getPlaneSelected = IcePy.Operation('getPlaneSelected', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    PlaneSlicingContext._op_setPlaneSelected = IcePy.Operation('setPlaneSelected', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    PlaneSlicingContext._op_getPlanePrevious = IcePy.Operation('getPlanePrevious', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    PlaneSlicingContext._op_setPlanePrevious = IcePy.Operation('setPlanePrevious', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    PlaneSlicingContext._op_getConstant = IcePy.Operation('getConstant', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    PlaneSlicingContext._op_setConstant = IcePy.Operation('setConstant', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())

    _M_ode.model.PlaneSlicingContext = PlaneSlicingContext
    del PlaneSlicingContext

    _M_ode.model.PlaneSlicingContextPrx = PlaneSlicingContextPrx
    del PlaneSlicingContextPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
