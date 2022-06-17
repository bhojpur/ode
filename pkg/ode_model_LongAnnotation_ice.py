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
import ode_model_NumericAnnotation_ice

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

if 'AnnotationAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_AnnotationAnnotationLink = IcePy.declareClass('::ode::model::AnnotationAnnotationLink')
    _M_ode.model._t_AnnotationAnnotationLinkPrx = IcePy.declareProxy('::ode::model::AnnotationAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'LongAnnotation' not in _M_ode.model.__dict__:
    _M_ode.model.LongAnnotation = Ice.createTempClass()
    class LongAnnotation(_M_ode.model.NumericAnnotation):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _ns=None, _name=None, _description=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _longValue=None):
            if Ice.getType(self) == _M_ode.model.LongAnnotation:
                raise RuntimeError('ode.model.LongAnnotation is an abstract class')
            _M_ode.model.NumericAnnotation.__init__(self, _id, _details, _loaded, _version, _ns, _name, _description, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._longValue = _longValue

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Annotation', '::ode::model::BasicAnnotation', '::ode::model::IObject', '::ode::model::LongAnnotation', '::ode::model::NumericAnnotation')

        def ice_id(self, current=None):
            return '::ode::model::LongAnnotation'

        def ice_staticId():
            return '::ode::model::LongAnnotation'
        ice_staticId = staticmethod(ice_staticId)

        def getLongValue(self, current=None):
            pass

        def setLongValue(self, theLongValue, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_LongAnnotation)

        __repr__ = __str__

    _M_ode.model.LongAnnotationPrx = Ice.createTempClass()
    class LongAnnotationPrx(_M_ode.model.NumericAnnotationPrx):

        def getLongValue(self, _ctx=None):
            return _M_ode.model.LongAnnotation._op_getLongValue.invoke(self, ((), _ctx))

        def begin_getLongValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LongAnnotation._op_getLongValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLongValue(self, _r):
            return _M_ode.model.LongAnnotation._op_getLongValue.end(self, _r)

        def setLongValue(self, theLongValue, _ctx=None):
            return _M_ode.model.LongAnnotation._op_setLongValue.invoke(self, ((theLongValue, ), _ctx))

        def begin_setLongValue(self, theLongValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LongAnnotation._op_setLongValue.begin(self, ((theLongValue, ), _response, _ex, _sent, _ctx))

        def end_setLongValue(self, _r):
            return _M_ode.model.LongAnnotation._op_setLongValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.LongAnnotationPrx.ice_checkedCast(proxy, '::ode::model::LongAnnotation', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.LongAnnotationPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::LongAnnotation'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_LongAnnotationPrx = IcePy.defineProxy('::ode::model::LongAnnotation', LongAnnotationPrx)

    _M_ode.model._t_LongAnnotation = IcePy.declareClass('::ode::model::LongAnnotation')

    _M_ode.model._t_LongAnnotation = IcePy.defineClass('::ode::model::LongAnnotation', LongAnnotation, -1, (), True, False, _M_ode.model._t_NumericAnnotation, (), (('_longValue', (), _M_ode._t_RLong, False, 0),))
    LongAnnotation._ice_type = _M_ode.model._t_LongAnnotation

    LongAnnotation._op_getLongValue = IcePy.Operation('getLongValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RLong, False, 0), ())
    LongAnnotation._op_setLongValue = IcePy.Operation('setLongValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RLong, False, 0),), (), None, ())

    _M_ode.model.LongAnnotation = LongAnnotation
    del LongAnnotation

    _M_ode.model.LongAnnotationPrx = LongAnnotationPrx
    del LongAnnotationPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
