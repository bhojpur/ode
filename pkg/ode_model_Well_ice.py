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

if 'WellReagentLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_WellReagentLink = IcePy.declareClass('::ode::model::WellReagentLink')
    _M_ode.model._t_WellReagentLinkPrx = IcePy.declareProxy('::ode::model::WellReagentLink')

if 'Reagent' not in _M_ode.model.__dict__:
    _M_ode.model._t_Reagent = IcePy.declareClass('::ode::model::Reagent')
    _M_ode.model._t_ReagentPrx = IcePy.declareProxy('::ode::model::Reagent')

if 'WellSample' not in _M_ode.model.__dict__:
    _M_ode.model._t_WellSample = IcePy.declareClass('::ode::model::WellSample')
    _M_ode.model._t_WellSamplePrx = IcePy.declareProxy('::ode::model::WellSample')

if 'Plate' not in _M_ode.model.__dict__:
    _M_ode.model._t_Plate = IcePy.declareClass('::ode::model::Plate')
    _M_ode.model._t_PlatePrx = IcePy.declareProxy('::ode::model::Plate')

if 'WellAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_WellAnnotationLink = IcePy.declareClass('::ode::model::WellAnnotationLink')
    _M_ode.model._t_WellAnnotationLinkPrx = IcePy.declareProxy('::ode::model::WellAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_WellReagentLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_WellReagentLinksSeq = IcePy.defineSequence('::ode::model::WellReagentLinksSeq', (), _M_ode.model._t_WellReagentLink)

if '_t_WellLinkedReagentSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_WellLinkedReagentSeq = IcePy.defineSequence('::ode::model::WellLinkedReagentSeq', (), _M_ode.model._t_Reagent)

if '_t_WellWellSamplesSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_WellWellSamplesSeq = IcePy.defineSequence('::ode::model::WellWellSamplesSeq', (), _M_ode.model._t_WellSample)

if '_t_WellAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_WellAnnotationLinksSeq = IcePy.defineSequence('::ode::model::WellAnnotationLinksSeq', (), _M_ode.model._t_WellAnnotationLink)

if '_t_WellLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_WellLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::WellLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Well' not in _M_ode.model.__dict__:
    _M_ode.model.Well = Ice.createTempClass()
    class Well(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _status=None, _column=None, _row=None, _red=None, _green=None, _blue=None, _alpha=None, _reagentLinksSeq=None, _reagentLinksLoaded=False, _reagentLinksCountPerOwner=None, _externalDescription=None, _externalIdentifier=None, _type=None, _wellSamplesSeq=None, _wellSamplesLoaded=False, _plate=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.Well:
                raise RuntimeError('ode.model.Well is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._status = _status
            self._column = _column
            self._row = _row
            self._red = _red
            self._green = _green
            self._blue = _blue
            self._alpha = _alpha
            self._reagentLinksSeq = _reagentLinksSeq
            self._reagentLinksLoaded = _reagentLinksLoaded
            self._reagentLinksCountPerOwner = _reagentLinksCountPerOwner
            self._externalDescription = _externalDescription
            self._externalIdentifier = _externalIdentifier
            self._type = _type
            self._wellSamplesSeq = _wellSamplesSeq
            self._wellSamplesLoaded = _wellSamplesLoaded
            self._plate = _plate
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Well')

        def ice_id(self, current=None):
            return '::ode::model::Well'

        def ice_staticId():
            return '::ode::model::Well'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getStatus(self, current=None):
            pass

        def setStatus(self, theStatus, current=None):
            pass

        def getColumn(self, current=None):
            pass

        def setColumn(self, theColumn, current=None):
            pass

        def getRow(self, current=None):
            pass

        def setRow(self, theRow, current=None):
            pass

        def getRed(self, current=None):
            pass

        def setRed(self, theRed, current=None):
            pass

        def getGreen(self, current=None):
            pass

        def setGreen(self, theGreen, current=None):
            pass

        def getBlue(self, current=None):
            pass

        def setBlue(self, theBlue, current=None):
            pass

        def getAlpha(self, current=None):
            pass

        def setAlpha(self, theAlpha, current=None):
            pass

        def unloadReagentLinks(self, current=None):
            pass

        def sizeOfReagentLinks(self, current=None):
            pass

        def copyReagentLinks(self, current=None):
            pass

        def addWellReagentLink(self, target, current=None):
            pass

        def addAllWellReagentLinkSet(self, targets, current=None):
            pass

        def removeWellReagentLink(self, theTarget, current=None):
            pass

        def removeAllWellReagentLinkSet(self, targets, current=None):
            pass

        def clearReagentLinks(self, current=None):
            pass

        def reloadReagentLinks(self, toCopy, current=None):
            pass

        def getReagentLinksCountPerOwner(self, current=None):
            pass

        def linkReagent(self, addition, current=None):
            pass

        def addWellReagentLinkToBoth(self, link, bothSides, current=None):
            pass

        def findWellReagentLink(self, removal, current=None):
            pass

        def unlinkReagent(self, removal, current=None):
            pass

        def removeWellReagentLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedReagentList(self, current=None):
            pass

        def getExternalDescription(self, current=None):
            pass

        def setExternalDescription(self, theExternalDescription, current=None):
            pass

        def getExternalIdentifier(self, current=None):
            pass

        def setExternalIdentifier(self, theExternalIdentifier, current=None):
            pass

        def getType(self, current=None):
            pass

        def setType(self, theType, current=None):
            pass

        def unloadWellSamples(self, current=None):
            pass

        def sizeOfWellSamples(self, current=None):
            pass

        def copyWellSamples(self, current=None):
            pass

        def addWellSample(self, target, current=None):
            pass

        def addAllWellSampleSet(self, targets, current=None):
            pass

        def removeWellSample(self, theTarget, current=None):
            pass

        def removeAllWellSampleSet(self, targets, current=None):
            pass

        def clearWellSamples(self, current=None):
            pass

        def reloadWellSamples(self, toCopy, current=None):
            pass

        def getWellSample(self, index, current=None):
            pass

        def setWellSample(self, index, theElement, current=None):
            pass

        def getPrimaryWellSample(self, current=None):
            pass

        def setPrimaryWellSample(self, theElement, current=None):
            pass

        def getPlate(self, current=None):
            pass

        def setPlate(self, thePlate, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addWellAnnotationLink(self, target, current=None):
            pass

        def addAllWellAnnotationLinkSet(self, targets, current=None):
            pass

        def removeWellAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllWellAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addWellAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findWellAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeWellAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Well)

        __repr__ = __str__

    _M_ode.model.WellPrx = Ice.createTempClass()
    class WellPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Well._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Well._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Well._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Well._op_setVersion.end(self, _r)

        def getStatus(self, _ctx=None):
            return _M_ode.model.Well._op_getStatus.invoke(self, ((), _ctx))

        def begin_getStatus(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getStatus.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStatus(self, _r):
            return _M_ode.model.Well._op_getStatus.end(self, _r)

        def setStatus(self, theStatus, _ctx=None):
            return _M_ode.model.Well._op_setStatus.invoke(self, ((theStatus, ), _ctx))

        def begin_setStatus(self, theStatus, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setStatus.begin(self, ((theStatus, ), _response, _ex, _sent, _ctx))

        def end_setStatus(self, _r):
            return _M_ode.model.Well._op_setStatus.end(self, _r)

        def getColumn(self, _ctx=None):
            return _M_ode.model.Well._op_getColumn.invoke(self, ((), _ctx))

        def begin_getColumn(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getColumn.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getColumn(self, _r):
            return _M_ode.model.Well._op_getColumn.end(self, _r)

        def setColumn(self, theColumn, _ctx=None):
            return _M_ode.model.Well._op_setColumn.invoke(self, ((theColumn, ), _ctx))

        def begin_setColumn(self, theColumn, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setColumn.begin(self, ((theColumn, ), _response, _ex, _sent, _ctx))

        def end_setColumn(self, _r):
            return _M_ode.model.Well._op_setColumn.end(self, _r)

        def getRow(self, _ctx=None):
            return _M_ode.model.Well._op_getRow.invoke(self, ((), _ctx))

        def begin_getRow(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getRow.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRow(self, _r):
            return _M_ode.model.Well._op_getRow.end(self, _r)

        def setRow(self, theRow, _ctx=None):
            return _M_ode.model.Well._op_setRow.invoke(self, ((theRow, ), _ctx))

        def begin_setRow(self, theRow, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setRow.begin(self, ((theRow, ), _response, _ex, _sent, _ctx))

        def end_setRow(self, _r):
            return _M_ode.model.Well._op_setRow.end(self, _r)

        def getRed(self, _ctx=None):
            return _M_ode.model.Well._op_getRed.invoke(self, ((), _ctx))

        def begin_getRed(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getRed.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRed(self, _r):
            return _M_ode.model.Well._op_getRed.end(self, _r)

        def setRed(self, theRed, _ctx=None):
            return _M_ode.model.Well._op_setRed.invoke(self, ((theRed, ), _ctx))

        def begin_setRed(self, theRed, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setRed.begin(self, ((theRed, ), _response, _ex, _sent, _ctx))

        def end_setRed(self, _r):
            return _M_ode.model.Well._op_setRed.end(self, _r)

        def getGreen(self, _ctx=None):
            return _M_ode.model.Well._op_getGreen.invoke(self, ((), _ctx))

        def begin_getGreen(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getGreen.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getGreen(self, _r):
            return _M_ode.model.Well._op_getGreen.end(self, _r)

        def setGreen(self, theGreen, _ctx=None):
            return _M_ode.model.Well._op_setGreen.invoke(self, ((theGreen, ), _ctx))

        def begin_setGreen(self, theGreen, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setGreen.begin(self, ((theGreen, ), _response, _ex, _sent, _ctx))

        def end_setGreen(self, _r):
            return _M_ode.model.Well._op_setGreen.end(self, _r)

        def getBlue(self, _ctx=None):
            return _M_ode.model.Well._op_getBlue.invoke(self, ((), _ctx))

        def begin_getBlue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getBlue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getBlue(self, _r):
            return _M_ode.model.Well._op_getBlue.end(self, _r)

        def setBlue(self, theBlue, _ctx=None):
            return _M_ode.model.Well._op_setBlue.invoke(self, ((theBlue, ), _ctx))

        def begin_setBlue(self, theBlue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setBlue.begin(self, ((theBlue, ), _response, _ex, _sent, _ctx))

        def end_setBlue(self, _r):
            return _M_ode.model.Well._op_setBlue.end(self, _r)

        def getAlpha(self, _ctx=None):
            return _M_ode.model.Well._op_getAlpha.invoke(self, ((), _ctx))

        def begin_getAlpha(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getAlpha.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAlpha(self, _r):
            return _M_ode.model.Well._op_getAlpha.end(self, _r)

        def setAlpha(self, theAlpha, _ctx=None):
            return _M_ode.model.Well._op_setAlpha.invoke(self, ((theAlpha, ), _ctx))

        def begin_setAlpha(self, theAlpha, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setAlpha.begin(self, ((theAlpha, ), _response, _ex, _sent, _ctx))

        def end_setAlpha(self, _r):
            return _M_ode.model.Well._op_setAlpha.end(self, _r)

        def unloadReagentLinks(self, _ctx=None):
            return _M_ode.model.Well._op_unloadReagentLinks.invoke(self, ((), _ctx))

        def begin_unloadReagentLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_unloadReagentLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadReagentLinks(self, _r):
            return _M_ode.model.Well._op_unloadReagentLinks.end(self, _r)

        def sizeOfReagentLinks(self, _ctx=None):
            return _M_ode.model.Well._op_sizeOfReagentLinks.invoke(self, ((), _ctx))

        def begin_sizeOfReagentLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_sizeOfReagentLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfReagentLinks(self, _r):
            return _M_ode.model.Well._op_sizeOfReagentLinks.end(self, _r)

        def copyReagentLinks(self, _ctx=None):
            return _M_ode.model.Well._op_copyReagentLinks.invoke(self, ((), _ctx))

        def begin_copyReagentLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_copyReagentLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyReagentLinks(self, _r):
            return _M_ode.model.Well._op_copyReagentLinks.end(self, _r)

        def addWellReagentLink(self, target, _ctx=None):
            return _M_ode.model.Well._op_addWellReagentLink.invoke(self, ((target, ), _ctx))

        def begin_addWellReagentLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_addWellReagentLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addWellReagentLink(self, _r):
            return _M_ode.model.Well._op_addWellReagentLink.end(self, _r)

        def addAllWellReagentLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Well._op_addAllWellReagentLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllWellReagentLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_addAllWellReagentLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllWellReagentLinkSet(self, _r):
            return _M_ode.model.Well._op_addAllWellReagentLinkSet.end(self, _r)

        def removeWellReagentLink(self, theTarget, _ctx=None):
            return _M_ode.model.Well._op_removeWellReagentLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeWellReagentLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_removeWellReagentLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeWellReagentLink(self, _r):
            return _M_ode.model.Well._op_removeWellReagentLink.end(self, _r)

        def removeAllWellReagentLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Well._op_removeAllWellReagentLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllWellReagentLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_removeAllWellReagentLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllWellReagentLinkSet(self, _r):
            return _M_ode.model.Well._op_removeAllWellReagentLinkSet.end(self, _r)

        def clearReagentLinks(self, _ctx=None):
            return _M_ode.model.Well._op_clearReagentLinks.invoke(self, ((), _ctx))

        def begin_clearReagentLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_clearReagentLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearReagentLinks(self, _r):
            return _M_ode.model.Well._op_clearReagentLinks.end(self, _r)

        def reloadReagentLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Well._op_reloadReagentLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadReagentLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_reloadReagentLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadReagentLinks(self, _r):
            return _M_ode.model.Well._op_reloadReagentLinks.end(self, _r)

        def getReagentLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Well._op_getReagentLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getReagentLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getReagentLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getReagentLinksCountPerOwner(self, _r):
            return _M_ode.model.Well._op_getReagentLinksCountPerOwner.end(self, _r)

        def linkReagent(self, addition, _ctx=None):
            return _M_ode.model.Well._op_linkReagent.invoke(self, ((addition, ), _ctx))

        def begin_linkReagent(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_linkReagent.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkReagent(self, _r):
            return _M_ode.model.Well._op_linkReagent.end(self, _r)

        def addWellReagentLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Well._op_addWellReagentLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addWellReagentLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_addWellReagentLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addWellReagentLinkToBoth(self, _r):
            return _M_ode.model.Well._op_addWellReagentLinkToBoth.end(self, _r)

        def findWellReagentLink(self, removal, _ctx=None):
            return _M_ode.model.Well._op_findWellReagentLink.invoke(self, ((removal, ), _ctx))

        def begin_findWellReagentLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_findWellReagentLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findWellReagentLink(self, _r):
            return _M_ode.model.Well._op_findWellReagentLink.end(self, _r)

        def unlinkReagent(self, removal, _ctx=None):
            return _M_ode.model.Well._op_unlinkReagent.invoke(self, ((removal, ), _ctx))

        def begin_unlinkReagent(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_unlinkReagent.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkReagent(self, _r):
            return _M_ode.model.Well._op_unlinkReagent.end(self, _r)

        def removeWellReagentLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Well._op_removeWellReagentLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeWellReagentLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_removeWellReagentLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeWellReagentLinkFromBoth(self, _r):
            return _M_ode.model.Well._op_removeWellReagentLinkFromBoth.end(self, _r)

        def linkedReagentList(self, _ctx=None):
            return _M_ode.model.Well._op_linkedReagentList.invoke(self, ((), _ctx))

        def begin_linkedReagentList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_linkedReagentList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedReagentList(self, _r):
            return _M_ode.model.Well._op_linkedReagentList.end(self, _r)

        def getExternalDescription(self, _ctx=None):
            return _M_ode.model.Well._op_getExternalDescription.invoke(self, ((), _ctx))

        def begin_getExternalDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getExternalDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getExternalDescription(self, _r):
            return _M_ode.model.Well._op_getExternalDescription.end(self, _r)

        def setExternalDescription(self, theExternalDescription, _ctx=None):
            return _M_ode.model.Well._op_setExternalDescription.invoke(self, ((theExternalDescription, ), _ctx))

        def begin_setExternalDescription(self, theExternalDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setExternalDescription.begin(self, ((theExternalDescription, ), _response, _ex, _sent, _ctx))

        def end_setExternalDescription(self, _r):
            return _M_ode.model.Well._op_setExternalDescription.end(self, _r)

        def getExternalIdentifier(self, _ctx=None):
            return _M_ode.model.Well._op_getExternalIdentifier.invoke(self, ((), _ctx))

        def begin_getExternalIdentifier(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getExternalIdentifier.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getExternalIdentifier(self, _r):
            return _M_ode.model.Well._op_getExternalIdentifier.end(self, _r)

        def setExternalIdentifier(self, theExternalIdentifier, _ctx=None):
            return _M_ode.model.Well._op_setExternalIdentifier.invoke(self, ((theExternalIdentifier, ), _ctx))

        def begin_setExternalIdentifier(self, theExternalIdentifier, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setExternalIdentifier.begin(self, ((theExternalIdentifier, ), _response, _ex, _sent, _ctx))

        def end_setExternalIdentifier(self, _r):
            return _M_ode.model.Well._op_setExternalIdentifier.end(self, _r)

        def getType(self, _ctx=None):
            return _M_ode.model.Well._op_getType.invoke(self, ((), _ctx))

        def begin_getType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getType(self, _r):
            return _M_ode.model.Well._op_getType.end(self, _r)

        def setType(self, theType, _ctx=None):
            return _M_ode.model.Well._op_setType.invoke(self, ((theType, ), _ctx))

        def begin_setType(self, theType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setType.begin(self, ((theType, ), _response, _ex, _sent, _ctx))

        def end_setType(self, _r):
            return _M_ode.model.Well._op_setType.end(self, _r)

        def unloadWellSamples(self, _ctx=None):
            return _M_ode.model.Well._op_unloadWellSamples.invoke(self, ((), _ctx))

        def begin_unloadWellSamples(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_unloadWellSamples.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadWellSamples(self, _r):
            return _M_ode.model.Well._op_unloadWellSamples.end(self, _r)

        def sizeOfWellSamples(self, _ctx=None):
            return _M_ode.model.Well._op_sizeOfWellSamples.invoke(self, ((), _ctx))

        def begin_sizeOfWellSamples(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_sizeOfWellSamples.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfWellSamples(self, _r):
            return _M_ode.model.Well._op_sizeOfWellSamples.end(self, _r)

        def copyWellSamples(self, _ctx=None):
            return _M_ode.model.Well._op_copyWellSamples.invoke(self, ((), _ctx))

        def begin_copyWellSamples(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_copyWellSamples.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyWellSamples(self, _r):
            return _M_ode.model.Well._op_copyWellSamples.end(self, _r)

        def addWellSample(self, target, _ctx=None):
            return _M_ode.model.Well._op_addWellSample.invoke(self, ((target, ), _ctx))

        def begin_addWellSample(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_addWellSample.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addWellSample(self, _r):
            return _M_ode.model.Well._op_addWellSample.end(self, _r)

        def addAllWellSampleSet(self, targets, _ctx=None):
            return _M_ode.model.Well._op_addAllWellSampleSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllWellSampleSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_addAllWellSampleSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllWellSampleSet(self, _r):
            return _M_ode.model.Well._op_addAllWellSampleSet.end(self, _r)

        def removeWellSample(self, theTarget, _ctx=None):
            return _M_ode.model.Well._op_removeWellSample.invoke(self, ((theTarget, ), _ctx))

        def begin_removeWellSample(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_removeWellSample.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeWellSample(self, _r):
            return _M_ode.model.Well._op_removeWellSample.end(self, _r)

        def removeAllWellSampleSet(self, targets, _ctx=None):
            return _M_ode.model.Well._op_removeAllWellSampleSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllWellSampleSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_removeAllWellSampleSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllWellSampleSet(self, _r):
            return _M_ode.model.Well._op_removeAllWellSampleSet.end(self, _r)

        def clearWellSamples(self, _ctx=None):
            return _M_ode.model.Well._op_clearWellSamples.invoke(self, ((), _ctx))

        def begin_clearWellSamples(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_clearWellSamples.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearWellSamples(self, _r):
            return _M_ode.model.Well._op_clearWellSamples.end(self, _r)

        def reloadWellSamples(self, toCopy, _ctx=None):
            return _M_ode.model.Well._op_reloadWellSamples.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadWellSamples(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_reloadWellSamples.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadWellSamples(self, _r):
            return _M_ode.model.Well._op_reloadWellSamples.end(self, _r)

        def getWellSample(self, index, _ctx=None):
            return _M_ode.model.Well._op_getWellSample.invoke(self, ((index, ), _ctx))

        def begin_getWellSample(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getWellSample.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getWellSample(self, _r):
            return _M_ode.model.Well._op_getWellSample.end(self, _r)

        def setWellSample(self, index, theElement, _ctx=None):
            return _M_ode.model.Well._op_setWellSample.invoke(self, ((index, theElement), _ctx))

        def begin_setWellSample(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setWellSample.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setWellSample(self, _r):
            return _M_ode.model.Well._op_setWellSample.end(self, _r)

        def getPrimaryWellSample(self, _ctx=None):
            return _M_ode.model.Well._op_getPrimaryWellSample.invoke(self, ((), _ctx))

        def begin_getPrimaryWellSample(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getPrimaryWellSample.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryWellSample(self, _r):
            return _M_ode.model.Well._op_getPrimaryWellSample.end(self, _r)

        def setPrimaryWellSample(self, theElement, _ctx=None):
            return _M_ode.model.Well._op_setPrimaryWellSample.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryWellSample(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setPrimaryWellSample.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryWellSample(self, _r):
            return _M_ode.model.Well._op_setPrimaryWellSample.end(self, _r)

        def getPlate(self, _ctx=None):
            return _M_ode.model.Well._op_getPlate.invoke(self, ((), _ctx))

        def begin_getPlate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getPlate.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPlate(self, _r):
            return _M_ode.model.Well._op_getPlate.end(self, _r)

        def setPlate(self, thePlate, _ctx=None):
            return _M_ode.model.Well._op_setPlate.invoke(self, ((thePlate, ), _ctx))

        def begin_setPlate(self, thePlate, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_setPlate.begin(self, ((thePlate, ), _response, _ex, _sent, _ctx))

        def end_setPlate(self, _r):
            return _M_ode.model.Well._op_setPlate.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Well._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Well._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Well._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Well._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Well._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Well._op_copyAnnotationLinks.end(self, _r)

        def addWellAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Well._op_addWellAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addWellAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_addWellAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addWellAnnotationLink(self, _r):
            return _M_ode.model.Well._op_addWellAnnotationLink.end(self, _r)

        def addAllWellAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Well._op_addAllWellAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllWellAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_addAllWellAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllWellAnnotationLinkSet(self, _r):
            return _M_ode.model.Well._op_addAllWellAnnotationLinkSet.end(self, _r)

        def removeWellAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Well._op_removeWellAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeWellAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_removeWellAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeWellAnnotationLink(self, _r):
            return _M_ode.model.Well._op_removeWellAnnotationLink.end(self, _r)

        def removeAllWellAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Well._op_removeAllWellAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllWellAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_removeAllWellAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllWellAnnotationLinkSet(self, _r):
            return _M_ode.model.Well._op_removeAllWellAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Well._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Well._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Well._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Well._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Well._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Well._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Well._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Well._op_linkAnnotation.end(self, _r)

        def addWellAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Well._op_addWellAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addWellAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_addWellAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addWellAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Well._op_addWellAnnotationLinkToBoth.end(self, _r)

        def findWellAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Well._op_findWellAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findWellAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_findWellAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findWellAnnotationLink(self, _r):
            return _M_ode.model.Well._op_findWellAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Well._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Well._op_unlinkAnnotation.end(self, _r)

        def removeWellAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Well._op_removeWellAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeWellAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_removeWellAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeWellAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Well._op_removeWellAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Well._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Well._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Well._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.WellPrx.ice_checkedCast(proxy, '::ode::model::Well', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.WellPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Well'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_WellPrx = IcePy.defineProxy('::ode::model::Well', WellPrx)

    _M_ode.model._t_Well = IcePy.declareClass('::ode::model::Well')

    _M_ode.model._t_Well = IcePy.defineClass('::ode::model::Well', Well, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_status', (), _M_ode._t_RString, False, 0),
        ('_column', (), _M_ode._t_RInt, False, 0),
        ('_row', (), _M_ode._t_RInt, False, 0),
        ('_red', (), _M_ode._t_RInt, False, 0),
        ('_green', (), _M_ode._t_RInt, False, 0),
        ('_blue', (), _M_ode._t_RInt, False, 0),
        ('_alpha', (), _M_ode._t_RInt, False, 0),
        ('_reagentLinksSeq', (), _M_ode.model._t_WellReagentLinksSeq, False, 0),
        ('_reagentLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_reagentLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_externalDescription', (), _M_ode._t_RString, False, 0),
        ('_externalIdentifier', (), _M_ode._t_RString, False, 0),
        ('_type', (), _M_ode._t_RString, False, 0),
        ('_wellSamplesSeq', (), _M_ode.model._t_WellWellSamplesSeq, False, 0),
        ('_wellSamplesLoaded', (), IcePy._t_bool, False, 0),
        ('_plate', (), _M_ode.model._t_Plate, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_WellAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    Well._ice_type = _M_ode.model._t_Well

    Well._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Well._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Well._op_getStatus = IcePy.Operation('getStatus', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Well._op_setStatus = IcePy.Operation('setStatus', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Well._op_getColumn = IcePy.Operation('getColumn', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Well._op_setColumn = IcePy.Operation('setColumn', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Well._op_getRow = IcePy.Operation('getRow', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Well._op_setRow = IcePy.Operation('setRow', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Well._op_getRed = IcePy.Operation('getRed', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Well._op_setRed = IcePy.Operation('setRed', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Well._op_getGreen = IcePy.Operation('getGreen', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Well._op_setGreen = IcePy.Operation('setGreen', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Well._op_getBlue = IcePy.Operation('getBlue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Well._op_setBlue = IcePy.Operation('setBlue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Well._op_getAlpha = IcePy.Operation('getAlpha', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Well._op_setAlpha = IcePy.Operation('setAlpha', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Well._op_unloadReagentLinks = IcePy.Operation('unloadReagentLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Well._op_sizeOfReagentLinks = IcePy.Operation('sizeOfReagentLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Well._op_copyReagentLinks = IcePy.Operation('copyReagentLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_WellReagentLinksSeq, False, 0), ())
    Well._op_addWellReagentLink = IcePy.Operation('addWellReagentLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellReagentLink, False, 0),), (), None, ())
    Well._op_addAllWellReagentLinkSet = IcePy.Operation('addAllWellReagentLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellReagentLinksSeq, False, 0),), (), None, ())
    Well._op_removeWellReagentLink = IcePy.Operation('removeWellReagentLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellReagentLink, False, 0),), (), None, ())
    Well._op_removeAllWellReagentLinkSet = IcePy.Operation('removeAllWellReagentLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellReagentLinksSeq, False, 0),), (), None, ())
    Well._op_clearReagentLinks = IcePy.Operation('clearReagentLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Well._op_reloadReagentLinks = IcePy.Operation('reloadReagentLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Well, False, 0),), (), None, ())
    Well._op_getReagentLinksCountPerOwner = IcePy.Operation('getReagentLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Well._op_linkReagent = IcePy.Operation('linkReagent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Reagent, False, 0),), (), ((), _M_ode.model._t_WellReagentLink, False, 0), ())
    Well._op_addWellReagentLinkToBoth = IcePy.Operation('addWellReagentLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellReagentLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Well._op_findWellReagentLink = IcePy.Operation('findWellReagentLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Reagent, False, 0),), (), ((), _M_ode.model._t_WellReagentLinksSeq, False, 0), ())
    Well._op_unlinkReagent = IcePy.Operation('unlinkReagent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Reagent, False, 0),), (), None, ())
    Well._op_removeWellReagentLinkFromBoth = IcePy.Operation('removeWellReagentLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellReagentLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Well._op_linkedReagentList = IcePy.Operation('linkedReagentList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_WellLinkedReagentSeq, False, 0), ())
    Well._op_getExternalDescription = IcePy.Operation('getExternalDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Well._op_setExternalDescription = IcePy.Operation('setExternalDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Well._op_getExternalIdentifier = IcePy.Operation('getExternalIdentifier', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Well._op_setExternalIdentifier = IcePy.Operation('setExternalIdentifier', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Well._op_getType = IcePy.Operation('getType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Well._op_setType = IcePy.Operation('setType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Well._op_unloadWellSamples = IcePy.Operation('unloadWellSamples', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Well._op_sizeOfWellSamples = IcePy.Operation('sizeOfWellSamples', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Well._op_copyWellSamples = IcePy.Operation('copyWellSamples', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_WellWellSamplesSeq, False, 0), ())
    Well._op_addWellSample = IcePy.Operation('addWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellSample, False, 0),), (), None, ())
    Well._op_addAllWellSampleSet = IcePy.Operation('addAllWellSampleSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellWellSamplesSeq, False, 0),), (), None, ())
    Well._op_removeWellSample = IcePy.Operation('removeWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellSample, False, 0),), (), None, ())
    Well._op_removeAllWellSampleSet = IcePy.Operation('removeAllWellSampleSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellWellSamplesSeq, False, 0),), (), None, ())
    Well._op_clearWellSamples = IcePy.Operation('clearWellSamples', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Well._op_reloadWellSamples = IcePy.Operation('reloadWellSamples', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Well, False, 0),), (), None, ())
    Well._op_getWellSample = IcePy.Operation('getWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_WellSample, False, 0), ())
    Well._op_setWellSample = IcePy.Operation('setWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_WellSample, False, 0)), (), ((), _M_ode.model._t_WellSample, False, 0), ())
    Well._op_getPrimaryWellSample = IcePy.Operation('getPrimaryWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_WellSample, False, 0), ())
    Well._op_setPrimaryWellSample = IcePy.Operation('setPrimaryWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellSample, False, 0),), (), ((), _M_ode.model._t_WellSample, False, 0), ())
    Well._op_getPlate = IcePy.Operation('getPlate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Plate, False, 0), ())
    Well._op_setPlate = IcePy.Operation('setPlate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Plate, False, 0),), (), None, ())
    Well._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Well._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Well._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_WellAnnotationLinksSeq, False, 0), ())
    Well._op_addWellAnnotationLink = IcePy.Operation('addWellAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellAnnotationLink, False, 0),), (), None, ())
    Well._op_addAllWellAnnotationLinkSet = IcePy.Operation('addAllWellAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellAnnotationLinksSeq, False, 0),), (), None, ())
    Well._op_removeWellAnnotationLink = IcePy.Operation('removeWellAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellAnnotationLink, False, 0),), (), None, ())
    Well._op_removeAllWellAnnotationLinkSet = IcePy.Operation('removeAllWellAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellAnnotationLinksSeq, False, 0),), (), None, ())
    Well._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Well._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Well, False, 0),), (), None, ())
    Well._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Well._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_WellAnnotationLink, False, 0), ())
    Well._op_addWellAnnotationLinkToBoth = IcePy.Operation('addWellAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Well._op_findWellAnnotationLink = IcePy.Operation('findWellAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_WellAnnotationLinksSeq, False, 0), ())
    Well._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Well._op_removeWellAnnotationLinkFromBoth = IcePy.Operation('removeWellAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Well._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_WellLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.Well = Well
    del Well

    _M_ode.model.WellPrx = WellPrx
    del WellPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
