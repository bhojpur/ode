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

if 'DoubleAnnotation' not in _M_ode.model.__dict__:
    _M_ode.model.DoubleAnnotation = Ice.createTempClass()
    class DoubleAnnotation(_M_ode.model.NumericAnnotation):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _ns=None, _name=None, _description=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _doubleValue=None):
            if Ice.getType(self) == _M_ode.model.DoubleAnnotation:
                raise RuntimeError('ode.model.DoubleAnnotation is an abstract class')
            _M_ode.model.NumericAnnotation.__init__(self, _id, _details, _loaded, _version, _ns, _name, _description, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._doubleValue = _doubleValue

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Annotation', '::ode::model::BasicAnnotation', '::ode::model::DoubleAnnotation', '::ode::model::IObject', '::ode::model::NumericAnnotation')

        def ice_id(self, current=None):
            return '::ode::model::DoubleAnnotation'

        def ice_staticId():
            return '::ode::model::DoubleAnnotation'
        ice_staticId = staticmethod(ice_staticId)

        def getDoubleValue(self, current=None):
            pass

        def setDoubleValue(self, theDoubleValue, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_DoubleAnnotation)

        __repr__ = __str__

    _M_ode.model.DoubleAnnotationPrx = Ice.createTempClass()
    class DoubleAnnotationPrx(_M_ode.model.NumericAnnotationPrx):

        def getDoubleValue(self, _ctx=None):
            return _M_ode.model.DoubleAnnotation._op_getDoubleValue.invoke(self, ((), _ctx))

        def begin_getDoubleValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DoubleAnnotation._op_getDoubleValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDoubleValue(self, _r):
            return _M_ode.model.DoubleAnnotation._op_getDoubleValue.end(self, _r)

        def setDoubleValue(self, theDoubleValue, _ctx=None):
            return _M_ode.model.DoubleAnnotation._op_setDoubleValue.invoke(self, ((theDoubleValue, ), _ctx))

        def begin_setDoubleValue(self, theDoubleValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DoubleAnnotation._op_setDoubleValue.begin(self, ((theDoubleValue, ), _response, _ex, _sent, _ctx))

        def end_setDoubleValue(self, _r):
            return _M_ode.model.DoubleAnnotation._op_setDoubleValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.DoubleAnnotationPrx.ice_checkedCast(proxy, '::ode::model::DoubleAnnotation', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.DoubleAnnotationPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::DoubleAnnotation'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_DoubleAnnotationPrx = IcePy.defineProxy('::ode::model::DoubleAnnotation', DoubleAnnotationPrx)

    _M_ode.model._t_DoubleAnnotation = IcePy.declareClass('::ode::model::DoubleAnnotation')

    _M_ode.model._t_DoubleAnnotation = IcePy.defineClass('::ode::model::DoubleAnnotation', DoubleAnnotation, -1, (), True, False, _M_ode.model._t_NumericAnnotation, (), (('_doubleValue', (), _M_ode._t_RDouble, False, 0),))
    DoubleAnnotation._ice_type = _M_ode.model._t_DoubleAnnotation

    DoubleAnnotation._op_getDoubleValue = IcePy.Operation('getDoubleValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    DoubleAnnotation._op_setDoubleValue = IcePy.Operation('setDoubleValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())

    _M_ode.model.DoubleAnnotation = DoubleAnnotation
    del DoubleAnnotation

    _M_ode.model.DoubleAnnotationPrx = DoubleAnnotationPrx
    del DoubleAnnotationPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
