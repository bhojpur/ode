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

if 'Ellipse' not in _M_ode.model.__dict__:
    _M_ode.model.Ellipse = Ice.createTempClass()
    class Ellipse(_M_ode.model.Shape):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _theZ=None, _theT=None, _theC=None, _roi=None, _locked=None, _transform=None, _fillColor=None, _fillRule=None, _strokeColor=None, _strokeDashArray=None, _strokeWidth=None, _fontFamily=None, _fontSize=None, _fontStyle=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _x=None, _y=None, _radiusX=None, _radiusY=None, _textValue=None):
            if Ice.getType(self) == _M_ode.model.Ellipse:
                raise RuntimeError('ode.model.Ellipse is an abstract class')
            _M_ode.model.Shape.__init__(self, _id, _details, _loaded, _version, _theZ, _theT, _theC, _roi, _locked, _transform, _fillColor, _fillRule, _strokeColor, _strokeDashArray, _strokeWidth, _fontFamily, _fontSize, _fontStyle, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._x = _x
            self._y = _y
            self._radiusX = _radiusX
            self._radiusY = _radiusY
            self._textValue = _textValue

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Ellipse', '::ode::model::IObject', '::ode::model::Shape')

        def ice_id(self, current=None):
            return '::ode::model::Ellipse'

        def ice_staticId():
            return '::ode::model::Ellipse'
        ice_staticId = staticmethod(ice_staticId)

        def getX(self, current=None):
            pass

        def setX(self, theX, current=None):
            pass

        def getY(self, current=None):
            pass

        def setY(self, theY, current=None):
            pass

        def getRadiusX(self, current=None):
            pass

        def setRadiusX(self, theRadiusX, current=None):
            pass

        def getRadiusY(self, current=None):
            pass

        def setRadiusY(self, theRadiusY, current=None):
            pass

        def getTextValue(self, current=None):
            pass

        def setTextValue(self, theTextValue, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Ellipse)

        __repr__ = __str__

    _M_ode.model.EllipsePrx = Ice.createTempClass()
    class EllipsePrx(_M_ode.model.ShapePrx):

        def getX(self, _ctx=None):
            return _M_ode.model.Ellipse._op_getX.invoke(self, ((), _ctx))

        def begin_getX(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Ellipse._op_getX.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getX(self, _r):
            return _M_ode.model.Ellipse._op_getX.end(self, _r)

        def setX(self, theX, _ctx=None):
            return _M_ode.model.Ellipse._op_setX.invoke(self, ((theX, ), _ctx))

        def begin_setX(self, theX, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Ellipse._op_setX.begin(self, ((theX, ), _response, _ex, _sent, _ctx))

        def end_setX(self, _r):
            return _M_ode.model.Ellipse._op_setX.end(self, _r)

        def getY(self, _ctx=None):
            return _M_ode.model.Ellipse._op_getY.invoke(self, ((), _ctx))

        def begin_getY(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Ellipse._op_getY.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getY(self, _r):
            return _M_ode.model.Ellipse._op_getY.end(self, _r)

        def setY(self, theY, _ctx=None):
            return _M_ode.model.Ellipse._op_setY.invoke(self, ((theY, ), _ctx))

        def begin_setY(self, theY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Ellipse._op_setY.begin(self, ((theY, ), _response, _ex, _sent, _ctx))

        def end_setY(self, _r):
            return _M_ode.model.Ellipse._op_setY.end(self, _r)

        def getRadiusX(self, _ctx=None):
            return _M_ode.model.Ellipse._op_getRadiusX.invoke(self, ((), _ctx))

        def begin_getRadiusX(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Ellipse._op_getRadiusX.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRadiusX(self, _r):
            return _M_ode.model.Ellipse._op_getRadiusX.end(self, _r)

        def setRadiusX(self, theRadiusX, _ctx=None):
            return _M_ode.model.Ellipse._op_setRadiusX.invoke(self, ((theRadiusX, ), _ctx))

        def begin_setRadiusX(self, theRadiusX, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Ellipse._op_setRadiusX.begin(self, ((theRadiusX, ), _response, _ex, _sent, _ctx))

        def end_setRadiusX(self, _r):
            return _M_ode.model.Ellipse._op_setRadiusX.end(self, _r)

        def getRadiusY(self, _ctx=None):
            return _M_ode.model.Ellipse._op_getRadiusY.invoke(self, ((), _ctx))

        def begin_getRadiusY(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Ellipse._op_getRadiusY.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRadiusY(self, _r):
            return _M_ode.model.Ellipse._op_getRadiusY.end(self, _r)

        def setRadiusY(self, theRadiusY, _ctx=None):
            return _M_ode.model.Ellipse._op_setRadiusY.invoke(self, ((theRadiusY, ), _ctx))

        def begin_setRadiusY(self, theRadiusY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Ellipse._op_setRadiusY.begin(self, ((theRadiusY, ), _response, _ex, _sent, _ctx))

        def end_setRadiusY(self, _r):
            return _M_ode.model.Ellipse._op_setRadiusY.end(self, _r)

        def getTextValue(self, _ctx=None):
            return _M_ode.model.Ellipse._op_getTextValue.invoke(self, ((), _ctx))

        def begin_getTextValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Ellipse._op_getTextValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTextValue(self, _r):
            return _M_ode.model.Ellipse._op_getTextValue.end(self, _r)

        def setTextValue(self, theTextValue, _ctx=None):
            return _M_ode.model.Ellipse._op_setTextValue.invoke(self, ((theTextValue, ), _ctx))

        def begin_setTextValue(self, theTextValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Ellipse._op_setTextValue.begin(self, ((theTextValue, ), _response, _ex, _sent, _ctx))

        def end_setTextValue(self, _r):
            return _M_ode.model.Ellipse._op_setTextValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.EllipsePrx.ice_checkedCast(proxy, '::ode::model::Ellipse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.EllipsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Ellipse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_EllipsePrx = IcePy.defineProxy('::ode::model::Ellipse', EllipsePrx)

    _M_ode.model._t_Ellipse = IcePy.declareClass('::ode::model::Ellipse')

    _M_ode.model._t_Ellipse = IcePy.defineClass('::ode::model::Ellipse', Ellipse, -1, (), True, False, _M_ode.model._t_Shape, (), (
        ('_x', (), _M_ode._t_RDouble, False, 0),
        ('_y', (), _M_ode._t_RDouble, False, 0),
        ('_radiusX', (), _M_ode._t_RDouble, False, 0),
        ('_radiusY', (), _M_ode._t_RDouble, False, 0),
        ('_textValue', (), _M_ode._t_RString, False, 0)
    ))
    Ellipse._ice_type = _M_ode.model._t_Ellipse

    Ellipse._op_getX = IcePy.Operation('getX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Ellipse._op_setX = IcePy.Operation('setX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Ellipse._op_getY = IcePy.Operation('getY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Ellipse._op_setY = IcePy.Operation('setY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Ellipse._op_getRadiusX = IcePy.Operation('getRadiusX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Ellipse._op_setRadiusX = IcePy.Operation('setRadiusX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Ellipse._op_getRadiusY = IcePy.Operation('getRadiusY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Ellipse._op_setRadiusY = IcePy.Operation('setRadiusY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Ellipse._op_getTextValue = IcePy.Operation('getTextValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Ellipse._op_setTextValue = IcePy.Operation('setTextValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Ellipse = Ellipse
    del Ellipse

    _M_ode.model.EllipsePrx = EllipsePrx
    del EllipsePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
