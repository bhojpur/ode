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
import ode_model_Shape_ice

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

if 'Roi' not in _M_ode.model.__dict__:
    _M_ode.model._t_Roi = IcePy.declareClass('::ode::model::Roi')
    _M_ode.model._t_RoiPrx = IcePy.declareProxy('::ode::model::Roi')

if 'AffineTransform' not in _M_ode.model.__dict__:
    _M_ode.model._t_AffineTransform = IcePy.declareClass('::ode::model::AffineTransform')
    _M_ode.model._t_AffineTransformPrx = IcePy.declareProxy('::ode::model::AffineTransform')

if 'Length' not in _M_ode.model.__dict__:
    _M_ode.model._t_Length = IcePy.declareClass('::ode::model::Length')
    _M_ode.model._t_LengthPrx = IcePy.declareProxy('::ode::model::Length')

if 'ShapeAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_ShapeAnnotationLink = IcePy.declareClass('::ode::model::ShapeAnnotationLink')
    _M_ode.model._t_ShapeAnnotationLinkPrx = IcePy.declareProxy('::ode::model::ShapeAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'Polyline' not in _M_ode.model.__dict__:
    _M_ode.model.Polyline = Ice.createTempClass()
    class Polyline(_M_ode.model.Shape):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _theZ=None, _theT=None, _theC=None, _roi=None, _locked=None, _transform=None, _fillColor=None, _fillRule=None, _strokeColor=None, _strokeDashArray=None, _strokeWidth=None, _fontFamily=None, _fontSize=None, _fontStyle=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _points=None, _markerStart=None, _markerEnd=None, _textValue=None):
            if Ice.getType(self) == _M_ode.model.Polyline:
                raise RuntimeError('ode.model.Polyline is an abstract class')
            _M_ode.model.Shape.__init__(self, _id, _details, _loaded, _version, _theZ, _theT, _theC, _roi, _locked, _transform, _fillColor, _fillRule, _strokeColor, _strokeDashArray, _strokeWidth, _fontFamily, _fontSize, _fontStyle, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._points = _points
            self._markerStart = _markerStart
            self._markerEnd = _markerEnd
            self._textValue = _textValue

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Polyline', '::ode::model::Shape')

        def ice_id(self, current=None):
            return '::ode::model::Polyline'

        def ice_staticId():
            return '::ode::model::Polyline'
        ice_staticId = staticmethod(ice_staticId)

        def getPoints(self, current=None):
            pass

        def setPoints(self, thePoints, current=None):
            pass

        def getMarkerStart(self, current=None):
            pass

        def setMarkerStart(self, theMarkerStart, current=None):
            pass

        def getMarkerEnd(self, current=None):
            pass

        def setMarkerEnd(self, theMarkerEnd, current=None):
            pass

        def getTextValue(self, current=None):
            pass

        def setTextValue(self, theTextValue, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Polyline)

        __repr__ = __str__

    _M_ode.model.PolylinePrx = Ice.createTempClass()
    class PolylinePrx(_M_ode.model.ShapePrx):

        def getPoints(self, _ctx=None):
            return _M_ode.model.Polyline._op_getPoints.invoke(self, ((), _ctx))

        def begin_getPoints(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Polyline._op_getPoints.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPoints(self, _r):
            return _M_ode.model.Polyline._op_getPoints.end(self, _r)

        def setPoints(self, thePoints, _ctx=None):
            return _M_ode.model.Polyline._op_setPoints.invoke(self, ((thePoints, ), _ctx))

        def begin_setPoints(self, thePoints, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Polyline._op_setPoints.begin(self, ((thePoints, ), _response, _ex, _sent, _ctx))

        def end_setPoints(self, _r):
            return _M_ode.model.Polyline._op_setPoints.end(self, _r)

        def getMarkerStart(self, _ctx=None):
            return _M_ode.model.Polyline._op_getMarkerStart.invoke(self, ((), _ctx))

        def begin_getMarkerStart(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Polyline._op_getMarkerStart.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMarkerStart(self, _r):
            return _M_ode.model.Polyline._op_getMarkerStart.end(self, _r)

        def setMarkerStart(self, theMarkerStart, _ctx=None):
            return _M_ode.model.Polyline._op_setMarkerStart.invoke(self, ((theMarkerStart, ), _ctx))

        def begin_setMarkerStart(self, theMarkerStart, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Polyline._op_setMarkerStart.begin(self, ((theMarkerStart, ), _response, _ex, _sent, _ctx))

        def end_setMarkerStart(self, _r):
            return _M_ode.model.Polyline._op_setMarkerStart.end(self, _r)

        def getMarkerEnd(self, _ctx=None):
            return _M_ode.model.Polyline._op_getMarkerEnd.invoke(self, ((), _ctx))

        def begin_getMarkerEnd(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Polyline._op_getMarkerEnd.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMarkerEnd(self, _r):
            return _M_ode.model.Polyline._op_getMarkerEnd.end(self, _r)

        def setMarkerEnd(self, theMarkerEnd, _ctx=None):
            return _M_ode.model.Polyline._op_setMarkerEnd.invoke(self, ((theMarkerEnd, ), _ctx))

        def begin_setMarkerEnd(self, theMarkerEnd, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Polyline._op_setMarkerEnd.begin(self, ((theMarkerEnd, ), _response, _ex, _sent, _ctx))

        def end_setMarkerEnd(self, _r):
            return _M_ode.model.Polyline._op_setMarkerEnd.end(self, _r)

        def getTextValue(self, _ctx=None):
            return _M_ode.model.Polyline._op_getTextValue.invoke(self, ((), _ctx))

        def begin_getTextValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Polyline._op_getTextValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTextValue(self, _r):
            return _M_ode.model.Polyline._op_getTextValue.end(self, _r)

        def setTextValue(self, theTextValue, _ctx=None):
            return _M_ode.model.Polyline._op_setTextValue.invoke(self, ((theTextValue, ), _ctx))

        def begin_setTextValue(self, theTextValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Polyline._op_setTextValue.begin(self, ((theTextValue, ), _response, _ex, _sent, _ctx))

        def end_setTextValue(self, _r):
            return _M_ode.model.Polyline._op_setTextValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.PolylinePrx.ice_checkedCast(proxy, '::ode::model::Polyline', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.PolylinePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Polyline'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_PolylinePrx = IcePy.defineProxy('::ode::model::Polyline', PolylinePrx)

    _M_ode.model._t_Polyline = IcePy.declareClass('::ode::model::Polyline')

    _M_ode.model._t_Polyline = IcePy.defineClass('::ode::model::Polyline', Polyline, -1, (), True, False, _M_ode.model._t_Shape, (), (
        ('_points', (), _M_ode._t_RString, False, 0),
        ('_markerStart', (), _M_ode._t_RString, False, 0),
        ('_markerEnd', (), _M_ode._t_RString, False, 0),
        ('_textValue', (), _M_ode._t_RString, False, 0)
    ))
    Polyline._ice_type = _M_ode.model._t_Polyline

    Polyline._op_getPoints = IcePy.Operation('getPoints', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Polyline._op_setPoints = IcePy.Operation('setPoints', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Polyline._op_getMarkerStart = IcePy.Operation('getMarkerStart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Polyline._op_setMarkerStart = IcePy.Operation('setMarkerStart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Polyline._op_getMarkerEnd = IcePy.Operation('getMarkerEnd', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Polyline._op_setMarkerEnd = IcePy.Operation('setMarkerEnd', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Polyline._op_getTextValue = IcePy.Operation('getTextValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Polyline._op_setTextValue = IcePy.Operation('setTextValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Polyline = Polyline
    del Polyline

    _M_ode.model.PolylinePrx = PolylinePrx
    del PolylinePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
