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

if 'Line' not in _M_ode.model.__dict__:
    _M_ode.model.Line = Ice.createTempClass()
    class Line(_M_ode.model.Shape):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _theZ=None, _theT=None, _theC=None, _roi=None, _locked=None, _transform=None, _fillColor=None, _fillRule=None, _strokeColor=None, _strokeDashArray=None, _strokeWidth=None, _fontFamily=None, _fontSize=None, _fontStyle=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _x1=None, _y1=None, _x2=None, _y2=None, _markerStart=None, _markerEnd=None, _textValue=None):
            if Ice.getType(self) == _M_ode.model.Line:
                raise RuntimeError('ode.model.Line is an abstract class')
            _M_ode.model.Shape.__init__(self, _id, _details, _loaded, _version, _theZ, _theT, _theC, _roi, _locked, _transform, _fillColor, _fillRule, _strokeColor, _strokeDashArray, _strokeWidth, _fontFamily, _fontSize, _fontStyle, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._x1 = _x1
            self._y1 = _y1
            self._x2 = _x2
            self._y2 = _y2
            self._markerStart = _markerStart
            self._markerEnd = _markerEnd
            self._textValue = _textValue

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Line', '::ode::model::Shape')

        def ice_id(self, current=None):
            return '::ode::model::Line'

        def ice_staticId():
            return '::ode::model::Line'
        ice_staticId = staticmethod(ice_staticId)

        def getX1(self, current=None):
            pass

        def setX1(self, theX1, current=None):
            pass

        def getY1(self, current=None):
            pass

        def setY1(self, theY1, current=None):
            pass

        def getX2(self, current=None):
            pass

        def setX2(self, theX2, current=None):
            pass

        def getY2(self, current=None):
            pass

        def setY2(self, theY2, current=None):
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
            return IcePy.stringify(self, _M_ode.model._t_Line)

        __repr__ = __str__

    _M_ode.model.LinePrx = Ice.createTempClass()
    class LinePrx(_M_ode.model.ShapePrx):

        def getX1(self, _ctx=None):
            return _M_ode.model.Line._op_getX1.invoke(self, ((), _ctx))

        def begin_getX1(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_getX1.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getX1(self, _r):
            return _M_ode.model.Line._op_getX1.end(self, _r)

        def setX1(self, theX1, _ctx=None):
            return _M_ode.model.Line._op_setX1.invoke(self, ((theX1, ), _ctx))

        def begin_setX1(self, theX1, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_setX1.begin(self, ((theX1, ), _response, _ex, _sent, _ctx))

        def end_setX1(self, _r):
            return _M_ode.model.Line._op_setX1.end(self, _r)

        def getY1(self, _ctx=None):
            return _M_ode.model.Line._op_getY1.invoke(self, ((), _ctx))

        def begin_getY1(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_getY1.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getY1(self, _r):
            return _M_ode.model.Line._op_getY1.end(self, _r)

        def setY1(self, theY1, _ctx=None):
            return _M_ode.model.Line._op_setY1.invoke(self, ((theY1, ), _ctx))

        def begin_setY1(self, theY1, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_setY1.begin(self, ((theY1, ), _response, _ex, _sent, _ctx))

        def end_setY1(self, _r):
            return _M_ode.model.Line._op_setY1.end(self, _r)

        def getX2(self, _ctx=None):
            return _M_ode.model.Line._op_getX2.invoke(self, ((), _ctx))

        def begin_getX2(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_getX2.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getX2(self, _r):
            return _M_ode.model.Line._op_getX2.end(self, _r)

        def setX2(self, theX2, _ctx=None):
            return _M_ode.model.Line._op_setX2.invoke(self, ((theX2, ), _ctx))

        def begin_setX2(self, theX2, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_setX2.begin(self, ((theX2, ), _response, _ex, _sent, _ctx))

        def end_setX2(self, _r):
            return _M_ode.model.Line._op_setX2.end(self, _r)

        def getY2(self, _ctx=None):
            return _M_ode.model.Line._op_getY2.invoke(self, ((), _ctx))

        def begin_getY2(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_getY2.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getY2(self, _r):
            return _M_ode.model.Line._op_getY2.end(self, _r)

        def setY2(self, theY2, _ctx=None):
            return _M_ode.model.Line._op_setY2.invoke(self, ((theY2, ), _ctx))

        def begin_setY2(self, theY2, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_setY2.begin(self, ((theY2, ), _response, _ex, _sent, _ctx))

        def end_setY2(self, _r):
            return _M_ode.model.Line._op_setY2.end(self, _r)

        def getMarkerStart(self, _ctx=None):
            return _M_ode.model.Line._op_getMarkerStart.invoke(self, ((), _ctx))

        def begin_getMarkerStart(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_getMarkerStart.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMarkerStart(self, _r):
            return _M_ode.model.Line._op_getMarkerStart.end(self, _r)

        def setMarkerStart(self, theMarkerStart, _ctx=None):
            return _M_ode.model.Line._op_setMarkerStart.invoke(self, ((theMarkerStart, ), _ctx))

        def begin_setMarkerStart(self, theMarkerStart, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_setMarkerStart.begin(self, ((theMarkerStart, ), _response, _ex, _sent, _ctx))

        def end_setMarkerStart(self, _r):
            return _M_ode.model.Line._op_setMarkerStart.end(self, _r)

        def getMarkerEnd(self, _ctx=None):
            return _M_ode.model.Line._op_getMarkerEnd.invoke(self, ((), _ctx))

        def begin_getMarkerEnd(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_getMarkerEnd.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMarkerEnd(self, _r):
            return _M_ode.model.Line._op_getMarkerEnd.end(self, _r)

        def setMarkerEnd(self, theMarkerEnd, _ctx=None):
            return _M_ode.model.Line._op_setMarkerEnd.invoke(self, ((theMarkerEnd, ), _ctx))

        def begin_setMarkerEnd(self, theMarkerEnd, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_setMarkerEnd.begin(self, ((theMarkerEnd, ), _response, _ex, _sent, _ctx))

        def end_setMarkerEnd(self, _r):
            return _M_ode.model.Line._op_setMarkerEnd.end(self, _r)

        def getTextValue(self, _ctx=None):
            return _M_ode.model.Line._op_getTextValue.invoke(self, ((), _ctx))

        def begin_getTextValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_getTextValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTextValue(self, _r):
            return _M_ode.model.Line._op_getTextValue.end(self, _r)

        def setTextValue(self, theTextValue, _ctx=None):
            return _M_ode.model.Line._op_setTextValue.invoke(self, ((theTextValue, ), _ctx))

        def begin_setTextValue(self, theTextValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Line._op_setTextValue.begin(self, ((theTextValue, ), _response, _ex, _sent, _ctx))

        def end_setTextValue(self, _r):
            return _M_ode.model.Line._op_setTextValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.LinePrx.ice_checkedCast(proxy, '::ode::model::Line', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.LinePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Line'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_LinePrx = IcePy.defineProxy('::ode::model::Line', LinePrx)

    _M_ode.model._t_Line = IcePy.declareClass('::ode::model::Line')

    _M_ode.model._t_Line = IcePy.defineClass('::ode::model::Line', Line, -1, (), True, False, _M_ode.model._t_Shape, (), (
        ('_x1', (), _M_ode._t_RDouble, False, 0),
        ('_y1', (), _M_ode._t_RDouble, False, 0),
        ('_x2', (), _M_ode._t_RDouble, False, 0),
        ('_y2', (), _M_ode._t_RDouble, False, 0),
        ('_markerStart', (), _M_ode._t_RString, False, 0),
        ('_markerEnd', (), _M_ode._t_RString, False, 0),
        ('_textValue', (), _M_ode._t_RString, False, 0)
    ))
    Line._ice_type = _M_ode.model._t_Line

    Line._op_getX1 = IcePy.Operation('getX1', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Line._op_setX1 = IcePy.Operation('setX1', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Line._op_getY1 = IcePy.Operation('getY1', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Line._op_setY1 = IcePy.Operation('setY1', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Line._op_getX2 = IcePy.Operation('getX2', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Line._op_setX2 = IcePy.Operation('setX2', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Line._op_getY2 = IcePy.Operation('getY2', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Line._op_setY2 = IcePy.Operation('setY2', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Line._op_getMarkerStart = IcePy.Operation('getMarkerStart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Line._op_setMarkerStart = IcePy.Operation('setMarkerStart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Line._op_getMarkerEnd = IcePy.Operation('getMarkerEnd', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Line._op_setMarkerEnd = IcePy.Operation('setMarkerEnd', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Line._op_getTextValue = IcePy.Operation('getTextValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Line._op_setTextValue = IcePy.Operation('setTextValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Line = Line
    del Line

    _M_ode.model.LinePrx = LinePrx
    del LinePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
