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

if 'Plate' not in _M_ode.model.__dict__:
    _M_ode.model._t_Plate = IcePy.declareClass('::ode::model::Plate')
    _M_ode.model._t_PlatePrx = IcePy.declareProxy('::ode::model::Plate')

if 'WellSample' not in _M_ode.model.__dict__:
    _M_ode.model._t_WellSample = IcePy.declareClass('::ode::model::WellSample')
    _M_ode.model._t_WellSamplePrx = IcePy.declareProxy('::ode::model::WellSample')

if 'PlateAcquisitionAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateAcquisitionAnnotationLink = IcePy.declareClass('::ode::model::PlateAcquisitionAnnotationLink')
    _M_ode.model._t_PlateAcquisitionAnnotationLinkPrx = IcePy.declareProxy('::ode::model::PlateAcquisitionAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_PlateAcquisitionWellSampleSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateAcquisitionWellSampleSeq = IcePy.defineSequence('::ode::model::PlateAcquisitionWellSampleSeq', (), _M_ode.model._t_WellSample)

if '_t_PlateAcquisitionAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateAcquisitionAnnotationLinksSeq = IcePy.defineSequence('::ode::model::PlateAcquisitionAnnotationLinksSeq', (), _M_ode.model._t_PlateAcquisitionAnnotationLink)

if '_t_PlateAcquisitionLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateAcquisitionLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::PlateAcquisitionLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'PlateAcquisition' not in _M_ode.model.__dict__:
    _M_ode.model.PlateAcquisition = Ice.createTempClass()
    class PlateAcquisition(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _name=None, _startTime=None, _endTime=None, _maximumFieldCount=None, _plate=None, _wellSampleSeq=None, _wellSampleLoaded=False, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _description=None):
            if Ice.getType(self) == _M_ode.model.PlateAcquisition:
                raise RuntimeError('ode.model.PlateAcquisition is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._name = _name
            self._startTime = _startTime
            self._endTime = _endTime
            self._maximumFieldCount = _maximumFieldCount
            self._plate = _plate
            self._wellSampleSeq = _wellSampleSeq
            self._wellSampleLoaded = _wellSampleLoaded
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::PlateAcquisition')

        def ice_id(self, current=None):
            return '::ode::model::PlateAcquisition'

        def ice_staticId():
            return '::ode::model::PlateAcquisition'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getName(self, current=None):
            pass

        def setName(self, theName, current=None):
            pass

        def getStartTime(self, current=None):
            pass

        def setStartTime(self, theStartTime, current=None):
            pass

        def getEndTime(self, current=None):
            pass

        def setEndTime(self, theEndTime, current=None):
            pass

        def getMaximumFieldCount(self, current=None):
            pass

        def setMaximumFieldCount(self, theMaximumFieldCount, current=None):
            pass

        def getPlate(self, current=None):
            pass

        def setPlate(self, thePlate, current=None):
            pass

        def unloadWellSample(self, current=None):
            pass

        def sizeOfWellSample(self, current=None):
            pass

        def copyWellSample(self, current=None):
            pass

        def addWellSample(self, target, current=None):
            pass

        def addAllWellSampleSet(self, targets, current=None):
            pass

        def removeWellSample(self, theTarget, current=None):
            pass

        def removeAllWellSampleSet(self, targets, current=None):
            pass

        def clearWellSample(self, current=None):
            pass

        def reloadWellSample(self, toCopy, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addPlateAcquisitionAnnotationLink(self, target, current=None):
            pass

        def addAllPlateAcquisitionAnnotationLinkSet(self, targets, current=None):
            pass

        def removePlateAcquisitionAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllPlateAcquisitionAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addPlateAcquisitionAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findPlateAcquisitionAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removePlateAcquisitionAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def getDescription(self, current=None):
            pass

        def setDescription(self, theDescription, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_PlateAcquisition)

        __repr__ = __str__

    _M_ode.model.PlateAcquisitionPrx = Ice.createTempClass()
    class PlateAcquisitionPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.PlateAcquisition._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.PlateAcquisition._op_setVersion.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.PlateAcquisition._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.PlateAcquisition._op_setName.end(self, _r)

        def getStartTime(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getStartTime.invoke(self, ((), _ctx))

        def begin_getStartTime(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getStartTime.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStartTime(self, _r):
            return _M_ode.model.PlateAcquisition._op_getStartTime.end(self, _r)

        def setStartTime(self, theStartTime, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setStartTime.invoke(self, ((theStartTime, ), _ctx))

        def begin_setStartTime(self, theStartTime, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setStartTime.begin(self, ((theStartTime, ), _response, _ex, _sent, _ctx))

        def end_setStartTime(self, _r):
            return _M_ode.model.PlateAcquisition._op_setStartTime.end(self, _r)

        def getEndTime(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getEndTime.invoke(self, ((), _ctx))

        def begin_getEndTime(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getEndTime.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEndTime(self, _r):
            return _M_ode.model.PlateAcquisition._op_getEndTime.end(self, _r)

        def setEndTime(self, theEndTime, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setEndTime.invoke(self, ((theEndTime, ), _ctx))

        def begin_setEndTime(self, theEndTime, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setEndTime.begin(self, ((theEndTime, ), _response, _ex, _sent, _ctx))

        def end_setEndTime(self, _r):
            return _M_ode.model.PlateAcquisition._op_setEndTime.end(self, _r)

        def getMaximumFieldCount(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getMaximumFieldCount.invoke(self, ((), _ctx))

        def begin_getMaximumFieldCount(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getMaximumFieldCount.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMaximumFieldCount(self, _r):
            return _M_ode.model.PlateAcquisition._op_getMaximumFieldCount.end(self, _r)

        def setMaximumFieldCount(self, theMaximumFieldCount, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setMaximumFieldCount.invoke(self, ((theMaximumFieldCount, ), _ctx))

        def begin_setMaximumFieldCount(self, theMaximumFieldCount, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setMaximumFieldCount.begin(self, ((theMaximumFieldCount, ), _response, _ex, _sent, _ctx))

        def end_setMaximumFieldCount(self, _r):
            return _M_ode.model.PlateAcquisition._op_setMaximumFieldCount.end(self, _r)

        def getPlate(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getPlate.invoke(self, ((), _ctx))

        def begin_getPlate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getPlate.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPlate(self, _r):
            return _M_ode.model.PlateAcquisition._op_getPlate.end(self, _r)

        def setPlate(self, thePlate, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setPlate.invoke(self, ((thePlate, ), _ctx))

        def begin_setPlate(self, thePlate, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setPlate.begin(self, ((thePlate, ), _response, _ex, _sent, _ctx))

        def end_setPlate(self, _r):
            return _M_ode.model.PlateAcquisition._op_setPlate.end(self, _r)

        def unloadWellSample(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_unloadWellSample.invoke(self, ((), _ctx))

        def begin_unloadWellSample(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_unloadWellSample.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadWellSample(self, _r):
            return _M_ode.model.PlateAcquisition._op_unloadWellSample.end(self, _r)

        def sizeOfWellSample(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_sizeOfWellSample.invoke(self, ((), _ctx))

        def begin_sizeOfWellSample(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_sizeOfWellSample.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfWellSample(self, _r):
            return _M_ode.model.PlateAcquisition._op_sizeOfWellSample.end(self, _r)

        def copyWellSample(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_copyWellSample.invoke(self, ((), _ctx))

        def begin_copyWellSample(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_copyWellSample.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyWellSample(self, _r):
            return _M_ode.model.PlateAcquisition._op_copyWellSample.end(self, _r)

        def addWellSample(self, target, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_addWellSample.invoke(self, ((target, ), _ctx))

        def begin_addWellSample(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_addWellSample.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addWellSample(self, _r):
            return _M_ode.model.PlateAcquisition._op_addWellSample.end(self, _r)

        def addAllWellSampleSet(self, targets, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_addAllWellSampleSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllWellSampleSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_addAllWellSampleSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllWellSampleSet(self, _r):
            return _M_ode.model.PlateAcquisition._op_addAllWellSampleSet.end(self, _r)

        def removeWellSample(self, theTarget, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_removeWellSample.invoke(self, ((theTarget, ), _ctx))

        def begin_removeWellSample(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_removeWellSample.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeWellSample(self, _r):
            return _M_ode.model.PlateAcquisition._op_removeWellSample.end(self, _r)

        def removeAllWellSampleSet(self, targets, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_removeAllWellSampleSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllWellSampleSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_removeAllWellSampleSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllWellSampleSet(self, _r):
            return _M_ode.model.PlateAcquisition._op_removeAllWellSampleSet.end(self, _r)

        def clearWellSample(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_clearWellSample.invoke(self, ((), _ctx))

        def begin_clearWellSample(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_clearWellSample.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearWellSample(self, _r):
            return _M_ode.model.PlateAcquisition._op_clearWellSample.end(self, _r)

        def reloadWellSample(self, toCopy, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_reloadWellSample.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadWellSample(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_reloadWellSample.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadWellSample(self, _r):
            return _M_ode.model.PlateAcquisition._op_reloadWellSample.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.PlateAcquisition._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.PlateAcquisition._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.PlateAcquisition._op_copyAnnotationLinks.end(self, _r)

        def addPlateAcquisitionAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_addPlateAcquisitionAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addPlateAcquisitionAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_addPlateAcquisitionAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addPlateAcquisitionAnnotationLink(self, _r):
            return _M_ode.model.PlateAcquisition._op_addPlateAcquisitionAnnotationLink.end(self, _r)

        def addAllPlateAcquisitionAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_addAllPlateAcquisitionAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllPlateAcquisitionAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_addAllPlateAcquisitionAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllPlateAcquisitionAnnotationLinkSet(self, _r):
            return _M_ode.model.PlateAcquisition._op_addAllPlateAcquisitionAnnotationLinkSet.end(self, _r)

        def removePlateAcquisitionAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_removePlateAcquisitionAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removePlateAcquisitionAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_removePlateAcquisitionAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removePlateAcquisitionAnnotationLink(self, _r):
            return _M_ode.model.PlateAcquisition._op_removePlateAcquisitionAnnotationLink.end(self, _r)

        def removeAllPlateAcquisitionAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_removeAllPlateAcquisitionAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllPlateAcquisitionAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_removeAllPlateAcquisitionAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllPlateAcquisitionAnnotationLinkSet(self, _r):
            return _M_ode.model.PlateAcquisition._op_removeAllPlateAcquisitionAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.PlateAcquisition._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.PlateAcquisition._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.PlateAcquisition._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.PlateAcquisition._op_linkAnnotation.end(self, _r)

        def addPlateAcquisitionAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_addPlateAcquisitionAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addPlateAcquisitionAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_addPlateAcquisitionAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addPlateAcquisitionAnnotationLinkToBoth(self, _r):
            return _M_ode.model.PlateAcquisition._op_addPlateAcquisitionAnnotationLinkToBoth.end(self, _r)

        def findPlateAcquisitionAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_findPlateAcquisitionAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findPlateAcquisitionAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_findPlateAcquisitionAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findPlateAcquisitionAnnotationLink(self, _r):
            return _M_ode.model.PlateAcquisition._op_findPlateAcquisitionAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.PlateAcquisition._op_unlinkAnnotation.end(self, _r)

        def removePlateAcquisitionAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_removePlateAcquisitionAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removePlateAcquisitionAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_removePlateAcquisitionAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removePlateAcquisitionAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.PlateAcquisition._op_removePlateAcquisitionAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.PlateAcquisition._op_linkedAnnotationList.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_ode.model.PlateAcquisition._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlateAcquisition._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_ode.model.PlateAcquisition._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.PlateAcquisitionPrx.ice_checkedCast(proxy, '::ode::model::PlateAcquisition', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.PlateAcquisitionPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::PlateAcquisition'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_PlateAcquisitionPrx = IcePy.defineProxy('::ode::model::PlateAcquisition', PlateAcquisitionPrx)

    _M_ode.model._t_PlateAcquisition = IcePy.declareClass('::ode::model::PlateAcquisition')

    _M_ode.model._t_PlateAcquisition = IcePy.defineClass('::ode::model::PlateAcquisition', PlateAcquisition, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0),
        ('_startTime', (), _M_ode._t_RTime, False, 0),
        ('_endTime', (), _M_ode._t_RTime, False, 0),
        ('_maximumFieldCount', (), _M_ode._t_RInt, False, 0),
        ('_plate', (), _M_ode.model._t_Plate, False, 0),
        ('_wellSampleSeq', (), _M_ode.model._t_PlateAcquisitionWellSampleSeq, False, 0),
        ('_wellSampleLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_PlateAcquisitionAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_description', (), _M_ode._t_RString, False, 0)
    ))
    PlateAcquisition._ice_type = _M_ode.model._t_PlateAcquisition

    PlateAcquisition._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    PlateAcquisition._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    PlateAcquisition._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    PlateAcquisition._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    PlateAcquisition._op_getStartTime = IcePy.Operation('getStartTime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    PlateAcquisition._op_setStartTime = IcePy.Operation('setStartTime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    PlateAcquisition._op_getEndTime = IcePy.Operation('getEndTime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    PlateAcquisition._op_setEndTime = IcePy.Operation('setEndTime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    PlateAcquisition._op_getMaximumFieldCount = IcePy.Operation('getMaximumFieldCount', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    PlateAcquisition._op_setMaximumFieldCount = IcePy.Operation('setMaximumFieldCount', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    PlateAcquisition._op_getPlate = IcePy.Operation('getPlate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Plate, False, 0), ())
    PlateAcquisition._op_setPlate = IcePy.Operation('setPlate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Plate, False, 0),), (), None, ())
    PlateAcquisition._op_unloadWellSample = IcePy.Operation('unloadWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    PlateAcquisition._op_sizeOfWellSample = IcePy.Operation('sizeOfWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    PlateAcquisition._op_copyWellSample = IcePy.Operation('copyWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlateAcquisitionWellSampleSeq, False, 0), ())
    PlateAcquisition._op_addWellSample = IcePy.Operation('addWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellSample, False, 0),), (), None, ())
    PlateAcquisition._op_addAllWellSampleSet = IcePy.Operation('addAllWellSampleSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisitionWellSampleSeq, False, 0),), (), None, ())
    PlateAcquisition._op_removeWellSample = IcePy.Operation('removeWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellSample, False, 0),), (), None, ())
    PlateAcquisition._op_removeAllWellSampleSet = IcePy.Operation('removeAllWellSampleSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisitionWellSampleSeq, False, 0),), (), None, ())
    PlateAcquisition._op_clearWellSample = IcePy.Operation('clearWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    PlateAcquisition._op_reloadWellSample = IcePy.Operation('reloadWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisition, False, 0),), (), None, ())
    PlateAcquisition._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    PlateAcquisition._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    PlateAcquisition._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlateAcquisitionAnnotationLinksSeq, False, 0), ())
    PlateAcquisition._op_addPlateAcquisitionAnnotationLink = IcePy.Operation('addPlateAcquisitionAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisitionAnnotationLink, False, 0),), (), None, ())
    PlateAcquisition._op_addAllPlateAcquisitionAnnotationLinkSet = IcePy.Operation('addAllPlateAcquisitionAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisitionAnnotationLinksSeq, False, 0),), (), None, ())
    PlateAcquisition._op_removePlateAcquisitionAnnotationLink = IcePy.Operation('removePlateAcquisitionAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisitionAnnotationLink, False, 0),), (), None, ())
    PlateAcquisition._op_removeAllPlateAcquisitionAnnotationLinkSet = IcePy.Operation('removeAllPlateAcquisitionAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisitionAnnotationLinksSeq, False, 0),), (), None, ())
    PlateAcquisition._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    PlateAcquisition._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisition, False, 0),), (), None, ())
    PlateAcquisition._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    PlateAcquisition._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_PlateAcquisitionAnnotationLink, False, 0), ())
    PlateAcquisition._op_addPlateAcquisitionAnnotationLinkToBoth = IcePy.Operation('addPlateAcquisitionAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisitionAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    PlateAcquisition._op_findPlateAcquisitionAnnotationLink = IcePy.Operation('findPlateAcquisitionAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_PlateAcquisitionAnnotationLinksSeq, False, 0), ())
    PlateAcquisition._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    PlateAcquisition._op_removePlateAcquisitionAnnotationLinkFromBoth = IcePy.Operation('removePlateAcquisitionAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisitionAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    PlateAcquisition._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlateAcquisitionLinkedAnnotationSeq, False, 0), ())
    PlateAcquisition._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    PlateAcquisition._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.PlateAcquisition = PlateAcquisition
    del PlateAcquisition

    _M_ode.model.PlateAcquisitionPrx = PlateAcquisitionPrx
    del PlateAcquisitionPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
