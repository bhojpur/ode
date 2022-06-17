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

if '_t_ShapeAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ShapeAnnotationLinksSeq = IcePy.defineSequence('::ode::model::ShapeAnnotationLinksSeq', (), _M_ode.model._t_ShapeAnnotationLink)

if '_t_ShapeLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ShapeLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::ShapeLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Shape' not in _M_ode.model.__dict__:
    _M_ode.model.Shape = Ice.createTempClass()
    class Shape(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _theZ=None, _theT=None, _theC=None, _roi=None, _locked=None, _transform=None, _fillColor=None, _fillRule=None, _strokeColor=None, _strokeDashArray=None, _strokeWidth=None, _fontFamily=None, _fontSize=None, _fontStyle=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.Shape:
                raise RuntimeError('ode.model.Shape is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._theZ = _theZ
            self._theT = _theT
            self._theC = _theC
            self._roi = _roi
            self._locked = _locked
            self._transform = _transform
            self._fillColor = _fillColor
            self._fillRule = _fillRule
            self._strokeColor = _strokeColor
            self._strokeDashArray = _strokeDashArray
            self._strokeWidth = _strokeWidth
            self._fontFamily = _fontFamily
            self._fontSize = _fontSize
            self._fontStyle = _fontStyle
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Shape')

        def ice_id(self, current=None):
            return '::ode::model::Shape'

        def ice_staticId():
            return '::ode::model::Shape'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getTheZ(self, current=None):
            pass

        def setTheZ(self, theTheZ, current=None):
            pass

        def getTheT(self, current=None):
            pass

        def setTheT(self, theTheT, current=None):
            pass

        def getTheC(self, current=None):
            pass

        def setTheC(self, theTheC, current=None):
            pass

        def getRoi(self, current=None):
            pass

        def setRoi(self, theRoi, current=None):
            pass

        def getLocked(self, current=None):
            pass

        def setLocked(self, theLocked, current=None):
            pass

        def getTransform(self, current=None):
            pass

        def setTransform(self, theTransform, current=None):
            pass

        def getFillColor(self, current=None):
            pass

        def setFillColor(self, theFillColor, current=None):
            pass

        def getFillRule(self, current=None):
            pass

        def setFillRule(self, theFillRule, current=None):
            pass

        def getStrokeColor(self, current=None):
            pass

        def setStrokeColor(self, theStrokeColor, current=None):
            pass

        def getStrokeDashArray(self, current=None):
            pass

        def setStrokeDashArray(self, theStrokeDashArray, current=None):
            pass

        def getStrokeWidth(self, current=None):
            pass

        def setStrokeWidth(self, theStrokeWidth, current=None):
            pass

        def getFontFamily(self, current=None):
            pass

        def setFontFamily(self, theFontFamily, current=None):
            pass

        def getFontSize(self, current=None):
            pass

        def setFontSize(self, theFontSize, current=None):
            pass

        def getFontStyle(self, current=None):
            pass

        def setFontStyle(self, theFontStyle, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addShapeAnnotationLink(self, target, current=None):
            pass

        def addAllShapeAnnotationLinkSet(self, targets, current=None):
            pass

        def removeShapeAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllShapeAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addShapeAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findShapeAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeShapeAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Shape)

        __repr__ = __str__

    _M_ode.model.ShapePrx = Ice.createTempClass()
    class ShapePrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Shape._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Shape._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Shape._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Shape._op_setVersion.end(self, _r)

        def getTheZ(self, _ctx=None):
            return _M_ode.model.Shape._op_getTheZ.invoke(self, ((), _ctx))

        def begin_getTheZ(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getTheZ.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTheZ(self, _r):
            return _M_ode.model.Shape._op_getTheZ.end(self, _r)

        def setTheZ(self, theTheZ, _ctx=None):
            return _M_ode.model.Shape._op_setTheZ.invoke(self, ((theTheZ, ), _ctx))

        def begin_setTheZ(self, theTheZ, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setTheZ.begin(self, ((theTheZ, ), _response, _ex, _sent, _ctx))

        def end_setTheZ(self, _r):
            return _M_ode.model.Shape._op_setTheZ.end(self, _r)

        def getTheT(self, _ctx=None):
            return _M_ode.model.Shape._op_getTheT.invoke(self, ((), _ctx))

        def begin_getTheT(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getTheT.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTheT(self, _r):
            return _M_ode.model.Shape._op_getTheT.end(self, _r)

        def setTheT(self, theTheT, _ctx=None):
            return _M_ode.model.Shape._op_setTheT.invoke(self, ((theTheT, ), _ctx))

        def begin_setTheT(self, theTheT, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setTheT.begin(self, ((theTheT, ), _response, _ex, _sent, _ctx))

        def end_setTheT(self, _r):
            return _M_ode.model.Shape._op_setTheT.end(self, _r)

        def getTheC(self, _ctx=None):
            return _M_ode.model.Shape._op_getTheC.invoke(self, ((), _ctx))

        def begin_getTheC(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getTheC.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTheC(self, _r):
            return _M_ode.model.Shape._op_getTheC.end(self, _r)

        def setTheC(self, theTheC, _ctx=None):
            return _M_ode.model.Shape._op_setTheC.invoke(self, ((theTheC, ), _ctx))

        def begin_setTheC(self, theTheC, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setTheC.begin(self, ((theTheC, ), _response, _ex, _sent, _ctx))

        def end_setTheC(self, _r):
            return _M_ode.model.Shape._op_setTheC.end(self, _r)

        def getRoi(self, _ctx=None):
            return _M_ode.model.Shape._op_getRoi.invoke(self, ((), _ctx))

        def begin_getRoi(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getRoi.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRoi(self, _r):
            return _M_ode.model.Shape._op_getRoi.end(self, _r)

        def setRoi(self, theRoi, _ctx=None):
            return _M_ode.model.Shape._op_setRoi.invoke(self, ((theRoi, ), _ctx))

        def begin_setRoi(self, theRoi, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setRoi.begin(self, ((theRoi, ), _response, _ex, _sent, _ctx))

        def end_setRoi(self, _r):
            return _M_ode.model.Shape._op_setRoi.end(self, _r)

        def getLocked(self, _ctx=None):
            return _M_ode.model.Shape._op_getLocked.invoke(self, ((), _ctx))

        def begin_getLocked(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getLocked.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLocked(self, _r):
            return _M_ode.model.Shape._op_getLocked.end(self, _r)

        def setLocked(self, theLocked, _ctx=None):
            return _M_ode.model.Shape._op_setLocked.invoke(self, ((theLocked, ), _ctx))

        def begin_setLocked(self, theLocked, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setLocked.begin(self, ((theLocked, ), _response, _ex, _sent, _ctx))

        def end_setLocked(self, _r):
            return _M_ode.model.Shape._op_setLocked.end(self, _r)

        def getTransform(self, _ctx=None):
            return _M_ode.model.Shape._op_getTransform.invoke(self, ((), _ctx))

        def begin_getTransform(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getTransform.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTransform(self, _r):
            return _M_ode.model.Shape._op_getTransform.end(self, _r)

        def setTransform(self, theTransform, _ctx=None):
            return _M_ode.model.Shape._op_setTransform.invoke(self, ((theTransform, ), _ctx))

        def begin_setTransform(self, theTransform, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setTransform.begin(self, ((theTransform, ), _response, _ex, _sent, _ctx))

        def end_setTransform(self, _r):
            return _M_ode.model.Shape._op_setTransform.end(self, _r)

        def getFillColor(self, _ctx=None):
            return _M_ode.model.Shape._op_getFillColor.invoke(self, ((), _ctx))

        def begin_getFillColor(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getFillColor.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFillColor(self, _r):
            return _M_ode.model.Shape._op_getFillColor.end(self, _r)

        def setFillColor(self, theFillColor, _ctx=None):
            return _M_ode.model.Shape._op_setFillColor.invoke(self, ((theFillColor, ), _ctx))

        def begin_setFillColor(self, theFillColor, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setFillColor.begin(self, ((theFillColor, ), _response, _ex, _sent, _ctx))

        def end_setFillColor(self, _r):
            return _M_ode.model.Shape._op_setFillColor.end(self, _r)

        def getFillRule(self, _ctx=None):
            return _M_ode.model.Shape._op_getFillRule.invoke(self, ((), _ctx))

        def begin_getFillRule(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getFillRule.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFillRule(self, _r):
            return _M_ode.model.Shape._op_getFillRule.end(self, _r)

        def setFillRule(self, theFillRule, _ctx=None):
            return _M_ode.model.Shape._op_setFillRule.invoke(self, ((theFillRule, ), _ctx))

        def begin_setFillRule(self, theFillRule, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setFillRule.begin(self, ((theFillRule, ), _response, _ex, _sent, _ctx))

        def end_setFillRule(self, _r):
            return _M_ode.model.Shape._op_setFillRule.end(self, _r)

        def getStrokeColor(self, _ctx=None):
            return _M_ode.model.Shape._op_getStrokeColor.invoke(self, ((), _ctx))

        def begin_getStrokeColor(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getStrokeColor.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStrokeColor(self, _r):
            return _M_ode.model.Shape._op_getStrokeColor.end(self, _r)

        def setStrokeColor(self, theStrokeColor, _ctx=None):
            return _M_ode.model.Shape._op_setStrokeColor.invoke(self, ((theStrokeColor, ), _ctx))

        def begin_setStrokeColor(self, theStrokeColor, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setStrokeColor.begin(self, ((theStrokeColor, ), _response, _ex, _sent, _ctx))

        def end_setStrokeColor(self, _r):
            return _M_ode.model.Shape._op_setStrokeColor.end(self, _r)

        def getStrokeDashArray(self, _ctx=None):
            return _M_ode.model.Shape._op_getStrokeDashArray.invoke(self, ((), _ctx))

        def begin_getStrokeDashArray(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getStrokeDashArray.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStrokeDashArray(self, _r):
            return _M_ode.model.Shape._op_getStrokeDashArray.end(self, _r)

        def setStrokeDashArray(self, theStrokeDashArray, _ctx=None):
            return _M_ode.model.Shape._op_setStrokeDashArray.invoke(self, ((theStrokeDashArray, ), _ctx))

        def begin_setStrokeDashArray(self, theStrokeDashArray, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setStrokeDashArray.begin(self, ((theStrokeDashArray, ), _response, _ex, _sent, _ctx))

        def end_setStrokeDashArray(self, _r):
            return _M_ode.model.Shape._op_setStrokeDashArray.end(self, _r)

        def getStrokeWidth(self, _ctx=None):
            return _M_ode.model.Shape._op_getStrokeWidth.invoke(self, ((), _ctx))

        def begin_getStrokeWidth(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getStrokeWidth.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStrokeWidth(self, _r):
            return _M_ode.model.Shape._op_getStrokeWidth.end(self, _r)

        def setStrokeWidth(self, theStrokeWidth, _ctx=None):
            return _M_ode.model.Shape._op_setStrokeWidth.invoke(self, ((theStrokeWidth, ), _ctx))

        def begin_setStrokeWidth(self, theStrokeWidth, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setStrokeWidth.begin(self, ((theStrokeWidth, ), _response, _ex, _sent, _ctx))

        def end_setStrokeWidth(self, _r):
            return _M_ode.model.Shape._op_setStrokeWidth.end(self, _r)

        def getFontFamily(self, _ctx=None):
            return _M_ode.model.Shape._op_getFontFamily.invoke(self, ((), _ctx))

        def begin_getFontFamily(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getFontFamily.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFontFamily(self, _r):
            return _M_ode.model.Shape._op_getFontFamily.end(self, _r)

        def setFontFamily(self, theFontFamily, _ctx=None):
            return _M_ode.model.Shape._op_setFontFamily.invoke(self, ((theFontFamily, ), _ctx))

        def begin_setFontFamily(self, theFontFamily, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setFontFamily.begin(self, ((theFontFamily, ), _response, _ex, _sent, _ctx))

        def end_setFontFamily(self, _r):
            return _M_ode.model.Shape._op_setFontFamily.end(self, _r)

        def getFontSize(self, _ctx=None):
            return _M_ode.model.Shape._op_getFontSize.invoke(self, ((), _ctx))

        def begin_getFontSize(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getFontSize.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFontSize(self, _r):
            return _M_ode.model.Shape._op_getFontSize.end(self, _r)

        def setFontSize(self, theFontSize, _ctx=None):
            return _M_ode.model.Shape._op_setFontSize.invoke(self, ((theFontSize, ), _ctx))

        def begin_setFontSize(self, theFontSize, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setFontSize.begin(self, ((theFontSize, ), _response, _ex, _sent, _ctx))

        def end_setFontSize(self, _r):
            return _M_ode.model.Shape._op_setFontSize.end(self, _r)

        def getFontStyle(self, _ctx=None):
            return _M_ode.model.Shape._op_getFontStyle.invoke(self, ((), _ctx))

        def begin_getFontStyle(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getFontStyle.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFontStyle(self, _r):
            return _M_ode.model.Shape._op_getFontStyle.end(self, _r)

        def setFontStyle(self, theFontStyle, _ctx=None):
            return _M_ode.model.Shape._op_setFontStyle.invoke(self, ((theFontStyle, ), _ctx))

        def begin_setFontStyle(self, theFontStyle, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_setFontStyle.begin(self, ((theFontStyle, ), _response, _ex, _sent, _ctx))

        def end_setFontStyle(self, _r):
            return _M_ode.model.Shape._op_setFontStyle.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Shape._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Shape._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Shape._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Shape._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Shape._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Shape._op_copyAnnotationLinks.end(self, _r)

        def addShapeAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Shape._op_addShapeAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addShapeAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_addShapeAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addShapeAnnotationLink(self, _r):
            return _M_ode.model.Shape._op_addShapeAnnotationLink.end(self, _r)

        def addAllShapeAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Shape._op_addAllShapeAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllShapeAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_addAllShapeAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllShapeAnnotationLinkSet(self, _r):
            return _M_ode.model.Shape._op_addAllShapeAnnotationLinkSet.end(self, _r)

        def removeShapeAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Shape._op_removeShapeAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeShapeAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_removeShapeAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeShapeAnnotationLink(self, _r):
            return _M_ode.model.Shape._op_removeShapeAnnotationLink.end(self, _r)

        def removeAllShapeAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Shape._op_removeAllShapeAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllShapeAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_removeAllShapeAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllShapeAnnotationLinkSet(self, _r):
            return _M_ode.model.Shape._op_removeAllShapeAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Shape._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Shape._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Shape._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Shape._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Shape._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Shape._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Shape._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Shape._op_linkAnnotation.end(self, _r)

        def addShapeAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Shape._op_addShapeAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addShapeAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_addShapeAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addShapeAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Shape._op_addShapeAnnotationLinkToBoth.end(self, _r)

        def findShapeAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Shape._op_findShapeAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findShapeAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_findShapeAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findShapeAnnotationLink(self, _r):
            return _M_ode.model.Shape._op_findShapeAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Shape._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Shape._op_unlinkAnnotation.end(self, _r)

        def removeShapeAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Shape._op_removeShapeAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeShapeAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_removeShapeAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeShapeAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Shape._op_removeShapeAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Shape._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Shape._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Shape._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ShapePrx.ice_checkedCast(proxy, '::ode::model::Shape', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ShapePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Shape'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ShapePrx = IcePy.defineProxy('::ode::model::Shape', ShapePrx)

    _M_ode.model._t_Shape = IcePy.declareClass('::ode::model::Shape')

    _M_ode.model._t_Shape = IcePy.defineClass('::ode::model::Shape', Shape, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_theZ', (), _M_ode._t_RInt, False, 0),
        ('_theT', (), _M_ode._t_RInt, False, 0),
        ('_theC', (), _M_ode._t_RInt, False, 0),
        ('_roi', (), _M_ode.model._t_Roi, False, 0),
        ('_locked', (), _M_ode._t_RBool, False, 0),
        ('_transform', (), _M_ode.model._t_AffineTransform, False, 0),
        ('_fillColor', (), _M_ode._t_RInt, False, 0),
        ('_fillRule', (), _M_ode._t_RString, False, 0),
        ('_strokeColor', (), _M_ode._t_RInt, False, 0),
        ('_strokeDashArray', (), _M_ode._t_RString, False, 0),
        ('_strokeWidth', (), _M_ode.model._t_Length, False, 0),
        ('_fontFamily', (), _M_ode._t_RString, False, 0),
        ('_fontSize', (), _M_ode.model._t_Length, False, 0),
        ('_fontStyle', (), _M_ode._t_RString, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_ShapeAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    Shape._ice_type = _M_ode.model._t_Shape

    Shape._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Shape._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Shape._op_getTheZ = IcePy.Operation('getTheZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Shape._op_setTheZ = IcePy.Operation('setTheZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Shape._op_getTheT = IcePy.Operation('getTheT', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Shape._op_setTheT = IcePy.Operation('setTheT', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Shape._op_getTheC = IcePy.Operation('getTheC', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Shape._op_setTheC = IcePy.Operation('setTheC', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Shape._op_getRoi = IcePy.Operation('getRoi', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Roi, False, 0), ())
    Shape._op_setRoi = IcePy.Operation('setRoi', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Roi, False, 0),), (), None, ())
    Shape._op_getLocked = IcePy.Operation('getLocked', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    Shape._op_setLocked = IcePy.Operation('setLocked', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    Shape._op_getTransform = IcePy.Operation('getTransform', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_AffineTransform, False, 0), ())
    Shape._op_setTransform = IcePy.Operation('setTransform', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_AffineTransform, False, 0),), (), None, ())
    Shape._op_getFillColor = IcePy.Operation('getFillColor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Shape._op_setFillColor = IcePy.Operation('setFillColor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Shape._op_getFillRule = IcePy.Operation('getFillRule', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Shape._op_setFillRule = IcePy.Operation('setFillRule', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Shape._op_getStrokeColor = IcePy.Operation('getStrokeColor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Shape._op_setStrokeColor = IcePy.Operation('setStrokeColor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Shape._op_getStrokeDashArray = IcePy.Operation('getStrokeDashArray', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Shape._op_setStrokeDashArray = IcePy.Operation('setStrokeDashArray', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Shape._op_getStrokeWidth = IcePy.Operation('getStrokeWidth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    Shape._op_setStrokeWidth = IcePy.Operation('setStrokeWidth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    Shape._op_getFontFamily = IcePy.Operation('getFontFamily', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Shape._op_setFontFamily = IcePy.Operation('setFontFamily', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Shape._op_getFontSize = IcePy.Operation('getFontSize', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    Shape._op_setFontSize = IcePy.Operation('setFontSize', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    Shape._op_getFontStyle = IcePy.Operation('getFontStyle', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Shape._op_setFontStyle = IcePy.Operation('setFontStyle', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Shape._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Shape._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Shape._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ShapeAnnotationLinksSeq, False, 0), ())
    Shape._op_addShapeAnnotationLink = IcePy.Operation('addShapeAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ShapeAnnotationLink, False, 0),), (), None, ())
    Shape._op_addAllShapeAnnotationLinkSet = IcePy.Operation('addAllShapeAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ShapeAnnotationLinksSeq, False, 0),), (), None, ())
    Shape._op_removeShapeAnnotationLink = IcePy.Operation('removeShapeAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ShapeAnnotationLink, False, 0),), (), None, ())
    Shape._op_removeAllShapeAnnotationLinkSet = IcePy.Operation('removeAllShapeAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ShapeAnnotationLinksSeq, False, 0),), (), None, ())
    Shape._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Shape._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Shape, False, 0),), (), None, ())
    Shape._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Shape._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ShapeAnnotationLink, False, 0), ())
    Shape._op_addShapeAnnotationLinkToBoth = IcePy.Operation('addShapeAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ShapeAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Shape._op_findShapeAnnotationLink = IcePy.Operation('findShapeAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ShapeAnnotationLinksSeq, False, 0), ())
    Shape._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Shape._op_removeShapeAnnotationLinkFromBoth = IcePy.Operation('removeShapeAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ShapeAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Shape._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ShapeLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.Shape = Shape
    del Shape

    _M_ode.model.ShapePrx = ShapePrx
    del ShapePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
