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

if 'Length' not in _M_ode.model.__dict__:
    _M_ode.model._t_Length = IcePy.declareClass('::ode::model::Length')
    _M_ode.model._t_LengthPrx = IcePy.declareProxy('::ode::model::Length')

if 'ScreenPlateLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_ScreenPlateLink = IcePy.declareClass('::ode::model::ScreenPlateLink')
    _M_ode.model._t_ScreenPlateLinkPrx = IcePy.declareProxy('::ode::model::ScreenPlateLink')

if 'Screen' not in _M_ode.model.__dict__:
    _M_ode.model._t_Screen = IcePy.declareClass('::ode::model::Screen')
    _M_ode.model._t_ScreenPrx = IcePy.declareProxy('::ode::model::Screen')

if 'Well' not in _M_ode.model.__dict__:
    _M_ode.model._t_Well = IcePy.declareClass('::ode::model::Well')
    _M_ode.model._t_WellPrx = IcePy.declareProxy('::ode::model::Well')

if 'PlateAcquisition' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateAcquisition = IcePy.declareClass('::ode::model::PlateAcquisition')
    _M_ode.model._t_PlateAcquisitionPrx = IcePy.declareProxy('::ode::model::PlateAcquisition')

if 'PlateAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateAnnotationLink = IcePy.declareClass('::ode::model::PlateAnnotationLink')
    _M_ode.model._t_PlateAnnotationLinkPrx = IcePy.declareProxy('::ode::model::PlateAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_PlateScreenLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateScreenLinksSeq = IcePy.defineSequence('::ode::model::PlateScreenLinksSeq', (), _M_ode.model._t_ScreenPlateLink)

if '_t_PlateLinkedScreenSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateLinkedScreenSeq = IcePy.defineSequence('::ode::model::PlateLinkedScreenSeq', (), _M_ode.model._t_Screen)

if '_t_PlateWellsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateWellsSeq = IcePy.defineSequence('::ode::model::PlateWellsSeq', (), _M_ode.model._t_Well)

if '_t_PlatePlateAcquisitionsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlatePlateAcquisitionsSeq = IcePy.defineSequence('::ode::model::PlatePlateAcquisitionsSeq', (), _M_ode.model._t_PlateAcquisition)

if '_t_PlateAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateAnnotationLinksSeq = IcePy.defineSequence('::ode::model::PlateAnnotationLinksSeq', (), _M_ode.model._t_PlateAnnotationLink)

if '_t_PlateLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::PlateLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Plate' not in _M_ode.model.__dict__:
    _M_ode.model.Plate = Ice.createTempClass()
    class Plate(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _defaultSample=None, _columnNamingConvention=None, _rowNamingConvention=None, _wellOriginX=None, _wellOriginY=None, _rows=None, _columns=None, _status=None, _externalIdentifier=None, _screenLinksSeq=None, _screenLinksLoaded=False, _screenLinksCountPerOwner=None, _wellsSeq=None, _wellsLoaded=False, _plateAcquisitionsSeq=None, _plateAcquisitionsLoaded=False, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _name=None, _description=None):
            if Ice.getType(self) == _M_ode.model.Plate:
                raise RuntimeError('ode.model.Plate is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._defaultSample = _defaultSample
            self._columnNamingConvention = _columnNamingConvention
            self._rowNamingConvention = _rowNamingConvention
            self._wellOriginX = _wellOriginX
            self._wellOriginY = _wellOriginY
            self._rows = _rows
            self._columns = _columns
            self._status = _status
            self._externalIdentifier = _externalIdentifier
            self._screenLinksSeq = _screenLinksSeq
            self._screenLinksLoaded = _screenLinksLoaded
            self._screenLinksCountPerOwner = _screenLinksCountPerOwner
            self._wellsSeq = _wellsSeq
            self._wellsLoaded = _wellsLoaded
            self._plateAcquisitionsSeq = _plateAcquisitionsSeq
            self._plateAcquisitionsLoaded = _plateAcquisitionsLoaded
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._name = _name
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Plate')

        def ice_id(self, current=None):
            return '::ode::model::Plate'

        def ice_staticId():
            return '::ode::model::Plate'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getDefaultSample(self, current=None):
            pass

        def setDefaultSample(self, theDefaultSample, current=None):
            pass

        def getColumnNamingConvention(self, current=None):
            pass

        def setColumnNamingConvention(self, theColumnNamingConvention, current=None):
            pass

        def getRowNamingConvention(self, current=None):
            pass

        def setRowNamingConvention(self, theRowNamingConvention, current=None):
            pass

        def getWellOriginX(self, current=None):
            pass

        def setWellOriginX(self, theWellOriginX, current=None):
            pass

        def getWellOriginY(self, current=None):
            pass

        def setWellOriginY(self, theWellOriginY, current=None):
            pass

        def getRows(self, current=None):
            pass

        def setRows(self, theRows, current=None):
            pass

        def getColumns(self, current=None):
            pass

        def setColumns(self, theColumns, current=None):
            pass

        def getStatus(self, current=None):
            pass

        def setStatus(self, theStatus, current=None):
            pass

        def getExternalIdentifier(self, current=None):
            pass

        def setExternalIdentifier(self, theExternalIdentifier, current=None):
            pass

        def unloadScreenLinks(self, current=None):
            pass

        def sizeOfScreenLinks(self, current=None):
            pass

        def copyScreenLinks(self, current=None):
            pass

        def addScreenPlateLink(self, target, current=None):
            pass

        def addAllScreenPlateLinkSet(self, targets, current=None):
            pass

        def removeScreenPlateLink(self, theTarget, current=None):
            pass

        def removeAllScreenPlateLinkSet(self, targets, current=None):
            pass

        def clearScreenLinks(self, current=None):
            pass

        def reloadScreenLinks(self, toCopy, current=None):
            pass

        def getScreenLinksCountPerOwner(self, current=None):
            pass

        def linkScreen(self, addition, current=None):
            pass

        def addScreenPlateLinkToBoth(self, link, bothSides, current=None):
            pass

        def findScreenPlateLink(self, removal, current=None):
            pass

        def unlinkScreen(self, removal, current=None):
            pass

        def removeScreenPlateLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedScreenList(self, current=None):
            pass

        def unloadWells(self, current=None):
            pass

        def sizeOfWells(self, current=None):
            pass

        def copyWells(self, current=None):
            pass

        def addWell(self, target, current=None):
            pass

        def addAllWellSet(self, targets, current=None):
            pass

        def removeWell(self, theTarget, current=None):
            pass

        def removeAllWellSet(self, targets, current=None):
            pass

        def clearWells(self, current=None):
            pass

        def reloadWells(self, toCopy, current=None):
            pass

        def unloadPlateAcquisitions(self, current=None):
            pass

        def sizeOfPlateAcquisitions(self, current=None):
            pass

        def copyPlateAcquisitions(self, current=None):
            pass

        def addPlateAcquisition(self, target, current=None):
            pass

        def addAllPlateAcquisitionSet(self, targets, current=None):
            pass

        def removePlateAcquisition(self, theTarget, current=None):
            pass

        def removeAllPlateAcquisitionSet(self, targets, current=None):
            pass

        def clearPlateAcquisitions(self, current=None):
            pass

        def reloadPlateAcquisitions(self, toCopy, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addPlateAnnotationLink(self, target, current=None):
            pass

        def addAllPlateAnnotationLinkSet(self, targets, current=None):
            pass

        def removePlateAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllPlateAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addPlateAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findPlateAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removePlateAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def getName(self, current=None):
            pass

        def setName(self, theName, current=None):
            pass

        def getDescription(self, current=None):
            pass

        def setDescription(self, theDescription, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Plate)

        __repr__ = __str__

    _M_ode.model.PlatePrx = Ice.createTempClass()
    class PlatePrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Plate._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Plate._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Plate._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Plate._op_setVersion.end(self, _r)

        def getDefaultSample(self, _ctx=None):
            return _M_ode.model.Plate._op_getDefaultSample.invoke(self, ((), _ctx))

        def begin_getDefaultSample(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getDefaultSample.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDefaultSample(self, _r):
            return _M_ode.model.Plate._op_getDefaultSample.end(self, _r)

        def setDefaultSample(self, theDefaultSample, _ctx=None):
            return _M_ode.model.Plate._op_setDefaultSample.invoke(self, ((theDefaultSample, ), _ctx))

        def begin_setDefaultSample(self, theDefaultSample, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setDefaultSample.begin(self, ((theDefaultSample, ), _response, _ex, _sent, _ctx))

        def end_setDefaultSample(self, _r):
            return _M_ode.model.Plate._op_setDefaultSample.end(self, _r)

        def getColumnNamingConvention(self, _ctx=None):
            return _M_ode.model.Plate._op_getColumnNamingConvention.invoke(self, ((), _ctx))

        def begin_getColumnNamingConvention(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getColumnNamingConvention.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getColumnNamingConvention(self, _r):
            return _M_ode.model.Plate._op_getColumnNamingConvention.end(self, _r)

        def setColumnNamingConvention(self, theColumnNamingConvention, _ctx=None):
            return _M_ode.model.Plate._op_setColumnNamingConvention.invoke(self, ((theColumnNamingConvention, ), _ctx))

        def begin_setColumnNamingConvention(self, theColumnNamingConvention, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setColumnNamingConvention.begin(self, ((theColumnNamingConvention, ), _response, _ex, _sent, _ctx))

        def end_setColumnNamingConvention(self, _r):
            return _M_ode.model.Plate._op_setColumnNamingConvention.end(self, _r)

        def getRowNamingConvention(self, _ctx=None):
            return _M_ode.model.Plate._op_getRowNamingConvention.invoke(self, ((), _ctx))

        def begin_getRowNamingConvention(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getRowNamingConvention.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRowNamingConvention(self, _r):
            return _M_ode.model.Plate._op_getRowNamingConvention.end(self, _r)

        def setRowNamingConvention(self, theRowNamingConvention, _ctx=None):
            return _M_ode.model.Plate._op_setRowNamingConvention.invoke(self, ((theRowNamingConvention, ), _ctx))

        def begin_setRowNamingConvention(self, theRowNamingConvention, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setRowNamingConvention.begin(self, ((theRowNamingConvention, ), _response, _ex, _sent, _ctx))

        def end_setRowNamingConvention(self, _r):
            return _M_ode.model.Plate._op_setRowNamingConvention.end(self, _r)

        def getWellOriginX(self, _ctx=None):
            return _M_ode.model.Plate._op_getWellOriginX.invoke(self, ((), _ctx))

        def begin_getWellOriginX(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getWellOriginX.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getWellOriginX(self, _r):
            return _M_ode.model.Plate._op_getWellOriginX.end(self, _r)

        def setWellOriginX(self, theWellOriginX, _ctx=None):
            return _M_ode.model.Plate._op_setWellOriginX.invoke(self, ((theWellOriginX, ), _ctx))

        def begin_setWellOriginX(self, theWellOriginX, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setWellOriginX.begin(self, ((theWellOriginX, ), _response, _ex, _sent, _ctx))

        def end_setWellOriginX(self, _r):
            return _M_ode.model.Plate._op_setWellOriginX.end(self, _r)

        def getWellOriginY(self, _ctx=None):
            return _M_ode.model.Plate._op_getWellOriginY.invoke(self, ((), _ctx))

        def begin_getWellOriginY(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getWellOriginY.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getWellOriginY(self, _r):
            return _M_ode.model.Plate._op_getWellOriginY.end(self, _r)

        def setWellOriginY(self, theWellOriginY, _ctx=None):
            return _M_ode.model.Plate._op_setWellOriginY.invoke(self, ((theWellOriginY, ), _ctx))

        def begin_setWellOriginY(self, theWellOriginY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setWellOriginY.begin(self, ((theWellOriginY, ), _response, _ex, _sent, _ctx))

        def end_setWellOriginY(self, _r):
            return _M_ode.model.Plate._op_setWellOriginY.end(self, _r)

        def getRows(self, _ctx=None):
            return _M_ode.model.Plate._op_getRows.invoke(self, ((), _ctx))

        def begin_getRows(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getRows.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRows(self, _r):
            return _M_ode.model.Plate._op_getRows.end(self, _r)

        def setRows(self, theRows, _ctx=None):
            return _M_ode.model.Plate._op_setRows.invoke(self, ((theRows, ), _ctx))

        def begin_setRows(self, theRows, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setRows.begin(self, ((theRows, ), _response, _ex, _sent, _ctx))

        def end_setRows(self, _r):
            return _M_ode.model.Plate._op_setRows.end(self, _r)

        def getColumns(self, _ctx=None):
            return _M_ode.model.Plate._op_getColumns.invoke(self, ((), _ctx))

        def begin_getColumns(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getColumns.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getColumns(self, _r):
            return _M_ode.model.Plate._op_getColumns.end(self, _r)

        def setColumns(self, theColumns, _ctx=None):
            return _M_ode.model.Plate._op_setColumns.invoke(self, ((theColumns, ), _ctx))

        def begin_setColumns(self, theColumns, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setColumns.begin(self, ((theColumns, ), _response, _ex, _sent, _ctx))

        def end_setColumns(self, _r):
            return _M_ode.model.Plate._op_setColumns.end(self, _r)

        def getStatus(self, _ctx=None):
            return _M_ode.model.Plate._op_getStatus.invoke(self, ((), _ctx))

        def begin_getStatus(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getStatus.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStatus(self, _r):
            return _M_ode.model.Plate._op_getStatus.end(self, _r)

        def setStatus(self, theStatus, _ctx=None):
            return _M_ode.model.Plate._op_setStatus.invoke(self, ((theStatus, ), _ctx))

        def begin_setStatus(self, theStatus, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setStatus.begin(self, ((theStatus, ), _response, _ex, _sent, _ctx))

        def end_setStatus(self, _r):
            return _M_ode.model.Plate._op_setStatus.end(self, _r)

        def getExternalIdentifier(self, _ctx=None):
            return _M_ode.model.Plate._op_getExternalIdentifier.invoke(self, ((), _ctx))

        def begin_getExternalIdentifier(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getExternalIdentifier.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getExternalIdentifier(self, _r):
            return _M_ode.model.Plate._op_getExternalIdentifier.end(self, _r)

        def setExternalIdentifier(self, theExternalIdentifier, _ctx=None):
            return _M_ode.model.Plate._op_setExternalIdentifier.invoke(self, ((theExternalIdentifier, ), _ctx))

        def begin_setExternalIdentifier(self, theExternalIdentifier, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setExternalIdentifier.begin(self, ((theExternalIdentifier, ), _response, _ex, _sent, _ctx))

        def end_setExternalIdentifier(self, _r):
            return _M_ode.model.Plate._op_setExternalIdentifier.end(self, _r)

        def unloadScreenLinks(self, _ctx=None):
            return _M_ode.model.Plate._op_unloadScreenLinks.invoke(self, ((), _ctx))

        def begin_unloadScreenLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_unloadScreenLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadScreenLinks(self, _r):
            return _M_ode.model.Plate._op_unloadScreenLinks.end(self, _r)

        def sizeOfScreenLinks(self, _ctx=None):
            return _M_ode.model.Plate._op_sizeOfScreenLinks.invoke(self, ((), _ctx))

        def begin_sizeOfScreenLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_sizeOfScreenLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfScreenLinks(self, _r):
            return _M_ode.model.Plate._op_sizeOfScreenLinks.end(self, _r)

        def copyScreenLinks(self, _ctx=None):
            return _M_ode.model.Plate._op_copyScreenLinks.invoke(self, ((), _ctx))

        def begin_copyScreenLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_copyScreenLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyScreenLinks(self, _r):
            return _M_ode.model.Plate._op_copyScreenLinks.end(self, _r)

        def addScreenPlateLink(self, target, _ctx=None):
            return _M_ode.model.Plate._op_addScreenPlateLink.invoke(self, ((target, ), _ctx))

        def begin_addScreenPlateLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_addScreenPlateLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addScreenPlateLink(self, _r):
            return _M_ode.model.Plate._op_addScreenPlateLink.end(self, _r)

        def addAllScreenPlateLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Plate._op_addAllScreenPlateLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllScreenPlateLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_addAllScreenPlateLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllScreenPlateLinkSet(self, _r):
            return _M_ode.model.Plate._op_addAllScreenPlateLinkSet.end(self, _r)

        def removeScreenPlateLink(self, theTarget, _ctx=None):
            return _M_ode.model.Plate._op_removeScreenPlateLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeScreenPlateLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_removeScreenPlateLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeScreenPlateLink(self, _r):
            return _M_ode.model.Plate._op_removeScreenPlateLink.end(self, _r)

        def removeAllScreenPlateLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Plate._op_removeAllScreenPlateLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllScreenPlateLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_removeAllScreenPlateLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllScreenPlateLinkSet(self, _r):
            return _M_ode.model.Plate._op_removeAllScreenPlateLinkSet.end(self, _r)

        def clearScreenLinks(self, _ctx=None):
            return _M_ode.model.Plate._op_clearScreenLinks.invoke(self, ((), _ctx))

        def begin_clearScreenLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_clearScreenLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearScreenLinks(self, _r):
            return _M_ode.model.Plate._op_clearScreenLinks.end(self, _r)

        def reloadScreenLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Plate._op_reloadScreenLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadScreenLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_reloadScreenLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadScreenLinks(self, _r):
            return _M_ode.model.Plate._op_reloadScreenLinks.end(self, _r)

        def getScreenLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Plate._op_getScreenLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getScreenLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getScreenLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getScreenLinksCountPerOwner(self, _r):
            return _M_ode.model.Plate._op_getScreenLinksCountPerOwner.end(self, _r)

        def linkScreen(self, addition, _ctx=None):
            return _M_ode.model.Plate._op_linkScreen.invoke(self, ((addition, ), _ctx))

        def begin_linkScreen(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_linkScreen.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkScreen(self, _r):
            return _M_ode.model.Plate._op_linkScreen.end(self, _r)

        def addScreenPlateLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Plate._op_addScreenPlateLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addScreenPlateLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_addScreenPlateLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addScreenPlateLinkToBoth(self, _r):
            return _M_ode.model.Plate._op_addScreenPlateLinkToBoth.end(self, _r)

        def findScreenPlateLink(self, removal, _ctx=None):
            return _M_ode.model.Plate._op_findScreenPlateLink.invoke(self, ((removal, ), _ctx))

        def begin_findScreenPlateLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_findScreenPlateLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findScreenPlateLink(self, _r):
            return _M_ode.model.Plate._op_findScreenPlateLink.end(self, _r)

        def unlinkScreen(self, removal, _ctx=None):
            return _M_ode.model.Plate._op_unlinkScreen.invoke(self, ((removal, ), _ctx))

        def begin_unlinkScreen(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_unlinkScreen.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkScreen(self, _r):
            return _M_ode.model.Plate._op_unlinkScreen.end(self, _r)

        def removeScreenPlateLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Plate._op_removeScreenPlateLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeScreenPlateLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_removeScreenPlateLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeScreenPlateLinkFromBoth(self, _r):
            return _M_ode.model.Plate._op_removeScreenPlateLinkFromBoth.end(self, _r)

        def linkedScreenList(self, _ctx=None):
            return _M_ode.model.Plate._op_linkedScreenList.invoke(self, ((), _ctx))

        def begin_linkedScreenList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_linkedScreenList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedScreenList(self, _r):
            return _M_ode.model.Plate._op_linkedScreenList.end(self, _r)

        def unloadWells(self, _ctx=None):
            return _M_ode.model.Plate._op_unloadWells.invoke(self, ((), _ctx))

        def begin_unloadWells(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_unloadWells.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadWells(self, _r):
            return _M_ode.model.Plate._op_unloadWells.end(self, _r)

        def sizeOfWells(self, _ctx=None):
            return _M_ode.model.Plate._op_sizeOfWells.invoke(self, ((), _ctx))

        def begin_sizeOfWells(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_sizeOfWells.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfWells(self, _r):
            return _M_ode.model.Plate._op_sizeOfWells.end(self, _r)

        def copyWells(self, _ctx=None):
            return _M_ode.model.Plate._op_copyWells.invoke(self, ((), _ctx))

        def begin_copyWells(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_copyWells.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyWells(self, _r):
            return _M_ode.model.Plate._op_copyWells.end(self, _r)

        def addWell(self, target, _ctx=None):
            return _M_ode.model.Plate._op_addWell.invoke(self, ((target, ), _ctx))

        def begin_addWell(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_addWell.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addWell(self, _r):
            return _M_ode.model.Plate._op_addWell.end(self, _r)

        def addAllWellSet(self, targets, _ctx=None):
            return _M_ode.model.Plate._op_addAllWellSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllWellSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_addAllWellSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllWellSet(self, _r):
            return _M_ode.model.Plate._op_addAllWellSet.end(self, _r)

        def removeWell(self, theTarget, _ctx=None):
            return _M_ode.model.Plate._op_removeWell.invoke(self, ((theTarget, ), _ctx))

        def begin_removeWell(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_removeWell.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeWell(self, _r):
            return _M_ode.model.Plate._op_removeWell.end(self, _r)

        def removeAllWellSet(self, targets, _ctx=None):
            return _M_ode.model.Plate._op_removeAllWellSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllWellSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_removeAllWellSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllWellSet(self, _r):
            return _M_ode.model.Plate._op_removeAllWellSet.end(self, _r)

        def clearWells(self, _ctx=None):
            return _M_ode.model.Plate._op_clearWells.invoke(self, ((), _ctx))

        def begin_clearWells(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_clearWells.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearWells(self, _r):
            return _M_ode.model.Plate._op_clearWells.end(self, _r)

        def reloadWells(self, toCopy, _ctx=None):
            return _M_ode.model.Plate._op_reloadWells.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadWells(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_reloadWells.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadWells(self, _r):
            return _M_ode.model.Plate._op_reloadWells.end(self, _r)

        def unloadPlateAcquisitions(self, _ctx=None):
            return _M_ode.model.Plate._op_unloadPlateAcquisitions.invoke(self, ((), _ctx))

        def begin_unloadPlateAcquisitions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_unloadPlateAcquisitions.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadPlateAcquisitions(self, _r):
            return _M_ode.model.Plate._op_unloadPlateAcquisitions.end(self, _r)

        def sizeOfPlateAcquisitions(self, _ctx=None):
            return _M_ode.model.Plate._op_sizeOfPlateAcquisitions.invoke(self, ((), _ctx))

        def begin_sizeOfPlateAcquisitions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_sizeOfPlateAcquisitions.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfPlateAcquisitions(self, _r):
            return _M_ode.model.Plate._op_sizeOfPlateAcquisitions.end(self, _r)

        def copyPlateAcquisitions(self, _ctx=None):
            return _M_ode.model.Plate._op_copyPlateAcquisitions.invoke(self, ((), _ctx))

        def begin_copyPlateAcquisitions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_copyPlateAcquisitions.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyPlateAcquisitions(self, _r):
            return _M_ode.model.Plate._op_copyPlateAcquisitions.end(self, _r)

        def addPlateAcquisition(self, target, _ctx=None):
            return _M_ode.model.Plate._op_addPlateAcquisition.invoke(self, ((target, ), _ctx))

        def begin_addPlateAcquisition(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_addPlateAcquisition.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addPlateAcquisition(self, _r):
            return _M_ode.model.Plate._op_addPlateAcquisition.end(self, _r)

        def addAllPlateAcquisitionSet(self, targets, _ctx=None):
            return _M_ode.model.Plate._op_addAllPlateAcquisitionSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllPlateAcquisitionSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_addAllPlateAcquisitionSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllPlateAcquisitionSet(self, _r):
            return _M_ode.model.Plate._op_addAllPlateAcquisitionSet.end(self, _r)

        def removePlateAcquisition(self, theTarget, _ctx=None):
            return _M_ode.model.Plate._op_removePlateAcquisition.invoke(self, ((theTarget, ), _ctx))

        def begin_removePlateAcquisition(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_removePlateAcquisition.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removePlateAcquisition(self, _r):
            return _M_ode.model.Plate._op_removePlateAcquisition.end(self, _r)

        def removeAllPlateAcquisitionSet(self, targets, _ctx=None):
            return _M_ode.model.Plate._op_removeAllPlateAcquisitionSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllPlateAcquisitionSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_removeAllPlateAcquisitionSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllPlateAcquisitionSet(self, _r):
            return _M_ode.model.Plate._op_removeAllPlateAcquisitionSet.end(self, _r)

        def clearPlateAcquisitions(self, _ctx=None):
            return _M_ode.model.Plate._op_clearPlateAcquisitions.invoke(self, ((), _ctx))

        def begin_clearPlateAcquisitions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_clearPlateAcquisitions.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearPlateAcquisitions(self, _r):
            return _M_ode.model.Plate._op_clearPlateAcquisitions.end(self, _r)

        def reloadPlateAcquisitions(self, toCopy, _ctx=None):
            return _M_ode.model.Plate._op_reloadPlateAcquisitions.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadPlateAcquisitions(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_reloadPlateAcquisitions.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadPlateAcquisitions(self, _r):
            return _M_ode.model.Plate._op_reloadPlateAcquisitions.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Plate._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Plate._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Plate._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Plate._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Plate._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Plate._op_copyAnnotationLinks.end(self, _r)

        def addPlateAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Plate._op_addPlateAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addPlateAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_addPlateAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addPlateAnnotationLink(self, _r):
            return _M_ode.model.Plate._op_addPlateAnnotationLink.end(self, _r)

        def addAllPlateAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Plate._op_addAllPlateAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllPlateAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_addAllPlateAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllPlateAnnotationLinkSet(self, _r):
            return _M_ode.model.Plate._op_addAllPlateAnnotationLinkSet.end(self, _r)

        def removePlateAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Plate._op_removePlateAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removePlateAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_removePlateAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removePlateAnnotationLink(self, _r):
            return _M_ode.model.Plate._op_removePlateAnnotationLink.end(self, _r)

        def removeAllPlateAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Plate._op_removeAllPlateAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllPlateAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_removeAllPlateAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllPlateAnnotationLinkSet(self, _r):
            return _M_ode.model.Plate._op_removeAllPlateAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Plate._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Plate._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Plate._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Plate._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Plate._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Plate._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Plate._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Plate._op_linkAnnotation.end(self, _r)

        def addPlateAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Plate._op_addPlateAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addPlateAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_addPlateAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addPlateAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Plate._op_addPlateAnnotationLinkToBoth.end(self, _r)

        def findPlateAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Plate._op_findPlateAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findPlateAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_findPlateAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findPlateAnnotationLink(self, _r):
            return _M_ode.model.Plate._op_findPlateAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Plate._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Plate._op_unlinkAnnotation.end(self, _r)

        def removePlateAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Plate._op_removePlateAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removePlateAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_removePlateAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removePlateAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Plate._op_removePlateAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Plate._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Plate._op_linkedAnnotationList.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.Plate._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.Plate._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.Plate._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.Plate._op_setName.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_ode.model.Plate._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_ode.model.Plate._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_ode.model.Plate._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Plate._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_ode.model.Plate._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.PlatePrx.ice_checkedCast(proxy, '::ode::model::Plate', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.PlatePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Plate'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_PlatePrx = IcePy.defineProxy('::ode::model::Plate', PlatePrx)

    _M_ode.model._t_Plate = IcePy.declareClass('::ode::model::Plate')

    _M_ode.model._t_Plate = IcePy.defineClass('::ode::model::Plate', Plate, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_defaultSample', (), _M_ode._t_RInt, False, 0),
        ('_columnNamingConvention', (), _M_ode._t_RString, False, 0),
        ('_rowNamingConvention', (), _M_ode._t_RString, False, 0),
        ('_wellOriginX', (), _M_ode.model._t_Length, False, 0),
        ('_wellOriginY', (), _M_ode.model._t_Length, False, 0),
        ('_rows', (), _M_ode._t_RInt, False, 0),
        ('_columns', (), _M_ode._t_RInt, False, 0),
        ('_status', (), _M_ode._t_RString, False, 0),
        ('_externalIdentifier', (), _M_ode._t_RString, False, 0),
        ('_screenLinksSeq', (), _M_ode.model._t_PlateScreenLinksSeq, False, 0),
        ('_screenLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_screenLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_wellsSeq', (), _M_ode.model._t_PlateWellsSeq, False, 0),
        ('_wellsLoaded', (), IcePy._t_bool, False, 0),
        ('_plateAcquisitionsSeq', (), _M_ode.model._t_PlatePlateAcquisitionsSeq, False, 0),
        ('_plateAcquisitionsLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_PlateAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0),
        ('_description', (), _M_ode._t_RString, False, 0)
    ))
    Plate._ice_type = _M_ode.model._t_Plate

    Plate._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Plate._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Plate._op_getDefaultSample = IcePy.Operation('getDefaultSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Plate._op_setDefaultSample = IcePy.Operation('setDefaultSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Plate._op_getColumnNamingConvention = IcePy.Operation('getColumnNamingConvention', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Plate._op_setColumnNamingConvention = IcePy.Operation('setColumnNamingConvention', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Plate._op_getRowNamingConvention = IcePy.Operation('getRowNamingConvention', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Plate._op_setRowNamingConvention = IcePy.Operation('setRowNamingConvention', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Plate._op_getWellOriginX = IcePy.Operation('getWellOriginX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    Plate._op_setWellOriginX = IcePy.Operation('setWellOriginX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    Plate._op_getWellOriginY = IcePy.Operation('getWellOriginY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    Plate._op_setWellOriginY = IcePy.Operation('setWellOriginY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    Plate._op_getRows = IcePy.Operation('getRows', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Plate._op_setRows = IcePy.Operation('setRows', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Plate._op_getColumns = IcePy.Operation('getColumns', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Plate._op_setColumns = IcePy.Operation('setColumns', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Plate._op_getStatus = IcePy.Operation('getStatus', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Plate._op_setStatus = IcePy.Operation('setStatus', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Plate._op_getExternalIdentifier = IcePy.Operation('getExternalIdentifier', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Plate._op_setExternalIdentifier = IcePy.Operation('setExternalIdentifier', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Plate._op_unloadScreenLinks = IcePy.Operation('unloadScreenLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Plate._op_sizeOfScreenLinks = IcePy.Operation('sizeOfScreenLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Plate._op_copyScreenLinks = IcePy.Operation('copyScreenLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlateScreenLinksSeq, False, 0), ())
    Plate._op_addScreenPlateLink = IcePy.Operation('addScreenPlateLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenPlateLink, False, 0),), (), None, ())
    Plate._op_addAllScreenPlateLinkSet = IcePy.Operation('addAllScreenPlateLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateScreenLinksSeq, False, 0),), (), None, ())
    Plate._op_removeScreenPlateLink = IcePy.Operation('removeScreenPlateLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenPlateLink, False, 0),), (), None, ())
    Plate._op_removeAllScreenPlateLinkSet = IcePy.Operation('removeAllScreenPlateLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateScreenLinksSeq, False, 0),), (), None, ())
    Plate._op_clearScreenLinks = IcePy.Operation('clearScreenLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Plate._op_reloadScreenLinks = IcePy.Operation('reloadScreenLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Plate, False, 0),), (), None, ())
    Plate._op_getScreenLinksCountPerOwner = IcePy.Operation('getScreenLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Plate._op_linkScreen = IcePy.Operation('linkScreen', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Screen, False, 0),), (), ((), _M_ode.model._t_ScreenPlateLink, False, 0), ())
    Plate._op_addScreenPlateLinkToBoth = IcePy.Operation('addScreenPlateLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenPlateLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Plate._op_findScreenPlateLink = IcePy.Operation('findScreenPlateLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Screen, False, 0),), (), ((), _M_ode.model._t_PlateScreenLinksSeq, False, 0), ())
    Plate._op_unlinkScreen = IcePy.Operation('unlinkScreen', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Screen, False, 0),), (), None, ())
    Plate._op_removeScreenPlateLinkFromBoth = IcePy.Operation('removeScreenPlateLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenPlateLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Plate._op_linkedScreenList = IcePy.Operation('linkedScreenList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlateLinkedScreenSeq, False, 0), ())
    Plate._op_unloadWells = IcePy.Operation('unloadWells', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Plate._op_sizeOfWells = IcePy.Operation('sizeOfWells', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Plate._op_copyWells = IcePy.Operation('copyWells', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlateWellsSeq, False, 0), ())
    Plate._op_addWell = IcePy.Operation('addWell', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Well, False, 0),), (), None, ())
    Plate._op_addAllWellSet = IcePy.Operation('addAllWellSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateWellsSeq, False, 0),), (), None, ())
    Plate._op_removeWell = IcePy.Operation('removeWell', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Well, False, 0),), (), None, ())
    Plate._op_removeAllWellSet = IcePy.Operation('removeAllWellSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateWellsSeq, False, 0),), (), None, ())
    Plate._op_clearWells = IcePy.Operation('clearWells', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Plate._op_reloadWells = IcePy.Operation('reloadWells', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Plate, False, 0),), (), None, ())
    Plate._op_unloadPlateAcquisitions = IcePy.Operation('unloadPlateAcquisitions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Plate._op_sizeOfPlateAcquisitions = IcePy.Operation('sizeOfPlateAcquisitions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Plate._op_copyPlateAcquisitions = IcePy.Operation('copyPlateAcquisitions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlatePlateAcquisitionsSeq, False, 0), ())
    Plate._op_addPlateAcquisition = IcePy.Operation('addPlateAcquisition', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisition, False, 0),), (), None, ())
    Plate._op_addAllPlateAcquisitionSet = IcePy.Operation('addAllPlateAcquisitionSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlatePlateAcquisitionsSeq, False, 0),), (), None, ())
    Plate._op_removePlateAcquisition = IcePy.Operation('removePlateAcquisition', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisition, False, 0),), (), None, ())
    Plate._op_removeAllPlateAcquisitionSet = IcePy.Operation('removeAllPlateAcquisitionSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlatePlateAcquisitionsSeq, False, 0),), (), None, ())
    Plate._op_clearPlateAcquisitions = IcePy.Operation('clearPlateAcquisitions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Plate._op_reloadPlateAcquisitions = IcePy.Operation('reloadPlateAcquisitions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Plate, False, 0),), (), None, ())
    Plate._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Plate._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Plate._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlateAnnotationLinksSeq, False, 0), ())
    Plate._op_addPlateAnnotationLink = IcePy.Operation('addPlateAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAnnotationLink, False, 0),), (), None, ())
    Plate._op_addAllPlateAnnotationLinkSet = IcePy.Operation('addAllPlateAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAnnotationLinksSeq, False, 0),), (), None, ())
    Plate._op_removePlateAnnotationLink = IcePy.Operation('removePlateAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAnnotationLink, False, 0),), (), None, ())
    Plate._op_removeAllPlateAnnotationLinkSet = IcePy.Operation('removeAllPlateAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAnnotationLinksSeq, False, 0),), (), None, ())
    Plate._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Plate._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Plate, False, 0),), (), None, ())
    Plate._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Plate._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_PlateAnnotationLink, False, 0), ())
    Plate._op_addPlateAnnotationLinkToBoth = IcePy.Operation('addPlateAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Plate._op_findPlateAnnotationLink = IcePy.Operation('findPlateAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_PlateAnnotationLinksSeq, False, 0), ())
    Plate._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Plate._op_removePlateAnnotationLinkFromBoth = IcePy.Operation('removePlateAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Plate._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlateLinkedAnnotationSeq, False, 0), ())
    Plate._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Plate._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Plate._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Plate._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Plate = Plate
    del Plate

    _M_ode.model.PlatePrx = PlatePrx
    del PlatePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
