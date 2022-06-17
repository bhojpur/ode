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
import ode_model_Annotation_ice

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

if 'MapAnnotation' not in _M_ode.model.__dict__:
    _M_ode.model.MapAnnotation = Ice.createTempClass()
    class MapAnnotation(_M_ode.model.Annotation):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _ns=None, _name=None, _description=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _mapValue=None):
            if Ice.getType(self) == _M_ode.model.MapAnnotation:
                raise RuntimeError('ode.model.MapAnnotation is an abstract class')
            _M_ode.model.Annotation.__init__(self, _id, _details, _loaded, _version, _ns, _name, _description, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._mapValue = _mapValue

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Annotation', '::ode::model::IObject', '::ode::model::MapAnnotation')

        def ice_id(self, current=None):
            return '::ode::model::MapAnnotation'

        def ice_staticId():
            return '::ode::model::MapAnnotation'
        ice_staticId = staticmethod(ice_staticId)

        def getMapValueAsMap(self, current=None):
            pass

        def getMapValue(self, current=None):
            pass

        def setMapValue(self, theMapValue, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_MapAnnotation)

        __repr__ = __str__

    _M_ode.model.MapAnnotationPrx = Ice.createTempClass()
    class MapAnnotationPrx(_M_ode.model.AnnotationPrx):

        def getMapValueAsMap(self, _ctx=None):
            return _M_ode.model.MapAnnotation._op_getMapValueAsMap.invoke(self, ((), _ctx))

        def begin_getMapValueAsMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MapAnnotation._op_getMapValueAsMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMapValueAsMap(self, _r):
            return _M_ode.model.MapAnnotation._op_getMapValueAsMap.end(self, _r)

        def getMapValue(self, _ctx=None):
            return _M_ode.model.MapAnnotation._op_getMapValue.invoke(self, ((), _ctx))

        def begin_getMapValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MapAnnotation._op_getMapValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMapValue(self, _r):
            return _M_ode.model.MapAnnotation._op_getMapValue.end(self, _r)

        def setMapValue(self, theMapValue, _ctx=None):
            return _M_ode.model.MapAnnotation._op_setMapValue.invoke(self, ((theMapValue, ), _ctx))

        def begin_setMapValue(self, theMapValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.MapAnnotation._op_setMapValue.begin(self, ((theMapValue, ), _response, _ex, _sent, _ctx))

        def end_setMapValue(self, _r):
            return _M_ode.model.MapAnnotation._op_setMapValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.MapAnnotationPrx.ice_checkedCast(proxy, '::ode::model::MapAnnotation', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.MapAnnotationPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::MapAnnotation'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_MapAnnotationPrx = IcePy.defineProxy('::ode::model::MapAnnotation', MapAnnotationPrx)

    _M_ode.model._t_MapAnnotation = IcePy.declareClass('::ode::model::MapAnnotation')

    _M_ode.model._t_MapAnnotation = IcePy.defineClass('::ode::model::MapAnnotation', MapAnnotation, -1, (), True, False, _M_ode.model._t_Annotation, (), (('_mapValue', (), _M_ode.api._t_NamedValueList, False, 0),))
    MapAnnotation._ice_type = _M_ode.model._t_MapAnnotation

    MapAnnotation._op_getMapValueAsMap = IcePy.Operation('getMapValueAsMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_StringStringMap, False, 0), ())
    MapAnnotation._op_getMapValue = IcePy.Operation('getMapValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_NamedValueList, False, 0), ())
    MapAnnotation._op_setMapValue = IcePy.Operation('setMapValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_NamedValueList, False, 0),), (), None, ())

    _M_ode.model.MapAnnotation = MapAnnotation
    del MapAnnotation

    _M_ode.model.MapAnnotationPrx = MapAnnotationPrx
    del MapAnnotationPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
