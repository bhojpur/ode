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
import omero_model_IObject_ice
import omero_RTypes_ice
import omero_model_RTypes_ice
import omero_System_ice
import omero_Collections_ice

# Included module omero
_M_omero = Ice.openModule('omero')

# Included module omero.model
_M_omero.model = Ice.openModule('omero.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module omero.sys
_M_omero.sys = Ice.openModule('omero.sys')

# Included module omero.api
_M_omero.api = Ice.openModule('omero.api')

# Start of module omero
__name__ = 'omero'

# Start of module omero.model
__name__ = 'omero.model'

if 'Microscope' not in _M_omero.model.__dict__:
    _M_omero.model._t_Microscope = IcePy.declareClass('::omero::model::Microscope')
    _M_omero.model._t_MicroscopePrx = IcePy.declareProxy('::omero::model::Microscope')

if 'Detector' not in _M_omero.model.__dict__:
    _M_omero.model._t_Detector = IcePy.declareClass('::omero::model::Detector')
    _M_omero.model._t_DetectorPrx = IcePy.declareProxy('::omero::model::Detector')

if 'Objective' not in _M_omero.model.__dict__:
    _M_omero.model._t_Objective = IcePy.declareClass('::omero::model::Objective')
    _M_omero.model._t_ObjectivePrx = IcePy.declareProxy('::omero::model::Objective')

if 'LightSource' not in _M_omero.model.__dict__:
    _M_omero.model._t_LightSource = IcePy.declareClass('::omero::model::LightSource')
    _M_omero.model._t_LightSourcePrx = IcePy.declareProxy('::omero::model::LightSource')

if 'Filter' not in _M_omero.model.__dict__:
    _M_omero.model._t_Filter = IcePy.declareClass('::omero::model::Filter')
    _M_omero.model._t_FilterPrx = IcePy.declareProxy('::omero::model::Filter')

if 'Dichroic' not in _M_omero.model.__dict__:
    _M_omero.model._t_Dichroic = IcePy.declareClass('::omero::model::Dichroic')
    _M_omero.model._t_DichroicPrx = IcePy.declareProxy('::omero::model::Dichroic')

if 'FilterSet' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterSet = IcePy.declareClass('::omero::model::FilterSet')
    _M_omero.model._t_FilterSetPrx = IcePy.declareProxy('::omero::model::FilterSet')

if 'OTF' not in _M_omero.model.__dict__:
    _M_omero.model._t_OTF = IcePy.declareClass('::omero::model::OTF')
    _M_omero.model._t_OTFPrx = IcePy.declareProxy('::omero::model::OTF')

if 'InstrumentAnnotationLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_InstrumentAnnotationLink = IcePy.declareClass('::omero::model::InstrumentAnnotationLink')
    _M_omero.model._t_InstrumentAnnotationLinkPrx = IcePy.declareProxy('::omero::model::InstrumentAnnotationLink')

if 'Annotation' not in _M_omero.model.__dict__:
    _M_omero.model._t_Annotation = IcePy.declareClass('::omero::model::Annotation')
    _M_omero.model._t_AnnotationPrx = IcePy.declareProxy('::omero::model::Annotation')

if 'Details' not in _M_omero.model.__dict__:
    _M_omero.model._t_Details = IcePy.declareClass('::omero::model::Details')
    _M_omero.model._t_DetailsPrx = IcePy.declareProxy('::omero::model::Details')

if '_t_InstrumentDetectorSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_InstrumentDetectorSeq = IcePy.defineSequence('::omero::model::InstrumentDetectorSeq', (), _M_omero.model._t_Detector)

if '_t_InstrumentObjectiveSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_InstrumentObjectiveSeq = IcePy.defineSequence('::omero::model::InstrumentObjectiveSeq', (), _M_omero.model._t_Objective)

if '_t_InstrumentLightSourceSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_InstrumentLightSourceSeq = IcePy.defineSequence('::omero::model::InstrumentLightSourceSeq', (), _M_omero.model._t_LightSource)

if '_t_InstrumentFilterSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_InstrumentFilterSeq = IcePy.defineSequence('::omero::model::InstrumentFilterSeq', (), _M_omero.model._t_Filter)

if '_t_InstrumentDichroicSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_InstrumentDichroicSeq = IcePy.defineSequence('::omero::model::InstrumentDichroicSeq', (), _M_omero.model._t_Dichroic)

if '_t_InstrumentFilterSetSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_InstrumentFilterSetSeq = IcePy.defineSequence('::omero::model::InstrumentFilterSetSeq', (), _M_omero.model._t_FilterSet)

if '_t_InstrumentOtfSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_InstrumentOtfSeq = IcePy.defineSequence('::omero::model::InstrumentOtfSeq', (), _M_omero.model._t_OTF)

if '_t_InstrumentAnnotationLinksSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_InstrumentAnnotationLinksSeq = IcePy.defineSequence('::omero::model::InstrumentAnnotationLinksSeq', (), _M_omero.model._t_InstrumentAnnotationLink)

if '_t_InstrumentLinkedAnnotationSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_InstrumentLinkedAnnotationSeq = IcePy.defineSequence('::omero::model::InstrumentLinkedAnnotationSeq', (), _M_omero.model._t_Annotation)

if 'Instrument' not in _M_omero.model.__dict__:
    _M_omero.model.Instrument = Ice.createTempClass()
    class Instrument(_M_omero.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _microscope=None, _detectorSeq=None, _detectorLoaded=False, _objectiveSeq=None, _objectiveLoaded=False, _lightSourceSeq=None, _lightSourceLoaded=False, _filterSeq=None, _filterLoaded=False, _dichroicSeq=None, _dichroicLoaded=False, _filterSetSeq=None, _filterSetLoaded=False, _otfSeq=None, _otfLoaded=False, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_omero.model.Instrument:
                raise RuntimeError('omero.model.Instrument is an abstract class')
            _M_omero.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._microscope = _microscope
            self._detectorSeq = _detectorSeq
            self._detectorLoaded = _detectorLoaded
            self._objectiveSeq = _objectiveSeq
            self._objectiveLoaded = _objectiveLoaded
            self._lightSourceSeq = _lightSourceSeq
            self._lightSourceLoaded = _lightSourceLoaded
            self._filterSeq = _filterSeq
            self._filterLoaded = _filterLoaded
            self._dichroicSeq = _dichroicSeq
            self._dichroicLoaded = _dichroicLoaded
            self._filterSetSeq = _filterSetSeq
            self._filterSetLoaded = _filterSetLoaded
            self._otfSeq = _otfSeq
            self._otfLoaded = _otfLoaded
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::omero::model::IObject', '::omero::model::Instrument')

        def ice_id(self, current=None):
            return '::omero::model::Instrument'

        def ice_staticId():
            return '::omero::model::Instrument'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getMicroscope(self, current=None):
            pass

        def setMicroscope(self, theMicroscope, current=None):
            pass

        def unloadDetector(self, current=None):
            pass

        def sizeOfDetector(self, current=None):
            pass

        def copyDetector(self, current=None):
            pass

        def addDetector(self, target, current=None):
            pass

        def addAllDetectorSet(self, targets, current=None):
            pass

        def removeDetector(self, theTarget, current=None):
            pass

        def removeAllDetectorSet(self, targets, current=None):
            pass

        def clearDetector(self, current=None):
            pass

        def reloadDetector(self, toCopy, current=None):
            pass

        def unloadObjective(self, current=None):
            pass

        def sizeOfObjective(self, current=None):
            pass

        def copyObjective(self, current=None):
            pass

        def addObjective(self, target, current=None):
            pass

        def addAllObjectiveSet(self, targets, current=None):
            pass

        def removeObjective(self, theTarget, current=None):
            pass

        def removeAllObjectiveSet(self, targets, current=None):
            pass

        def clearObjective(self, current=None):
            pass

        def reloadObjective(self, toCopy, current=None):
            pass

        def unloadLightSource(self, current=None):
            pass

        def sizeOfLightSource(self, current=None):
            pass

        def copyLightSource(self, current=None):
            pass

        def addLightSource(self, target, current=None):
            pass

        def addAllLightSourceSet(self, targets, current=None):
            pass

        def removeLightSource(self, theTarget, current=None):
            pass

        def removeAllLightSourceSet(self, targets, current=None):
            pass

        def clearLightSource(self, current=None):
            pass

        def reloadLightSource(self, toCopy, current=None):
            pass

        def unloadFilter(self, current=None):
            pass

        def sizeOfFilter(self, current=None):
            pass

        def copyFilter(self, current=None):
            pass

        def addFilter(self, target, current=None):
            pass

        def addAllFilterSet(self, targets, current=None):
            pass

        def removeFilter(self, theTarget, current=None):
            pass

        def removeAllFilterSet(self, targets, current=None):
            pass

        def clearFilter(self, current=None):
            pass

        def reloadFilter(self, toCopy, current=None):
            pass

        def unloadDichroic(self, current=None):
            pass

        def sizeOfDichroic(self, current=None):
            pass

        def copyDichroic(self, current=None):
            pass

        def addDichroic(self, target, current=None):
            pass

        def addAllDichroicSet(self, targets, current=None):
            pass

        def removeDichroic(self, theTarget, current=None):
            pass

        def removeAllDichroicSet(self, targets, current=None):
            pass

        def clearDichroic(self, current=None):
            pass

        def reloadDichroic(self, toCopy, current=None):
            pass

        def unloadFilterSet(self, current=None):
            pass

        def sizeOfFilterSet(self, current=None):
            pass

        def copyFilterSet(self, current=None):
            pass

        def addFilterSet(self, target, current=None):
            pass

        def addAllFilterSetSet(self, targets, current=None):
            pass

        def removeFilterSet(self, theTarget, current=None):
            pass

        def removeAllFilterSetSet(self, targets, current=None):
            pass

        def clearFilterSet(self, current=None):
            pass

        def reloadFilterSet(self, toCopy, current=None):
            pass

        def unloadOtf(self, current=None):
            pass

        def sizeOfOtf(self, current=None):
            pass

        def copyOtf(self, current=None):
            pass

        def addOTF(self, target, current=None):
            pass

        def addAllOTFSet(self, targets, current=None):
            pass

        def removeOTF(self, theTarget, current=None):
            pass

        def removeAllOTFSet(self, targets, current=None):
            pass

        def clearOtf(self, current=None):
            pass

        def reloadOtf(self, toCopy, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addInstrumentAnnotationLink(self, target, current=None):
            pass

        def addAllInstrumentAnnotationLinkSet(self, targets, current=None):
            pass

        def removeInstrumentAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllInstrumentAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addInstrumentAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findInstrumentAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeInstrumentAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_omero.model._t_Instrument)

        __repr__ = __str__

    _M_omero.model.InstrumentPrx = Ice.createTempClass()
    class InstrumentPrx(_M_omero.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_omero.model.Instrument._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_omero.model.Instrument._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_omero.model.Instrument._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_omero.model.Instrument._op_setVersion.end(self, _r)

        def getMicroscope(self, _ctx=None):
            return _M_omero.model.Instrument._op_getMicroscope.invoke(self, ((), _ctx))

        def begin_getMicroscope(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_getMicroscope.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMicroscope(self, _r):
            return _M_omero.model.Instrument._op_getMicroscope.end(self, _r)

        def setMicroscope(self, theMicroscope, _ctx=None):
            return _M_omero.model.Instrument._op_setMicroscope.invoke(self, ((theMicroscope, ), _ctx))

        def begin_setMicroscope(self, theMicroscope, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_setMicroscope.begin(self, ((theMicroscope, ), _response, _ex, _sent, _ctx))

        def end_setMicroscope(self, _r):
            return _M_omero.model.Instrument._op_setMicroscope.end(self, _r)

        def unloadDetector(self, _ctx=None):
            return _M_omero.model.Instrument._op_unloadDetector.invoke(self, ((), _ctx))

        def begin_unloadDetector(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_unloadDetector.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadDetector(self, _r):
            return _M_omero.model.Instrument._op_unloadDetector.end(self, _r)

        def sizeOfDetector(self, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfDetector.invoke(self, ((), _ctx))

        def begin_sizeOfDetector(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfDetector.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfDetector(self, _r):
            return _M_omero.model.Instrument._op_sizeOfDetector.end(self, _r)

        def copyDetector(self, _ctx=None):
            return _M_omero.model.Instrument._op_copyDetector.invoke(self, ((), _ctx))

        def begin_copyDetector(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_copyDetector.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyDetector(self, _r):
            return _M_omero.model.Instrument._op_copyDetector.end(self, _r)

        def addDetector(self, target, _ctx=None):
            return _M_omero.model.Instrument._op_addDetector.invoke(self, ((target, ), _ctx))

        def begin_addDetector(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addDetector.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addDetector(self, _r):
            return _M_omero.model.Instrument._op_addDetector.end(self, _r)

        def addAllDetectorSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_addAllDetectorSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllDetectorSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addAllDetectorSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllDetectorSet(self, _r):
            return _M_omero.model.Instrument._op_addAllDetectorSet.end(self, _r)

        def removeDetector(self, theTarget, _ctx=None):
            return _M_omero.model.Instrument._op_removeDetector.invoke(self, ((theTarget, ), _ctx))

        def begin_removeDetector(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeDetector.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeDetector(self, _r):
            return _M_omero.model.Instrument._op_removeDetector.end(self, _r)

        def removeAllDetectorSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllDetectorSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllDetectorSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllDetectorSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllDetectorSet(self, _r):
            return _M_omero.model.Instrument._op_removeAllDetectorSet.end(self, _r)

        def clearDetector(self, _ctx=None):
            return _M_omero.model.Instrument._op_clearDetector.invoke(self, ((), _ctx))

        def begin_clearDetector(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_clearDetector.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearDetector(self, _r):
            return _M_omero.model.Instrument._op_clearDetector.end(self, _r)

        def reloadDetector(self, toCopy, _ctx=None):
            return _M_omero.model.Instrument._op_reloadDetector.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadDetector(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_reloadDetector.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadDetector(self, _r):
            return _M_omero.model.Instrument._op_reloadDetector.end(self, _r)

        def unloadObjective(self, _ctx=None):
            return _M_omero.model.Instrument._op_unloadObjective.invoke(self, ((), _ctx))

        def begin_unloadObjective(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_unloadObjective.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadObjective(self, _r):
            return _M_omero.model.Instrument._op_unloadObjective.end(self, _r)

        def sizeOfObjective(self, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfObjective.invoke(self, ((), _ctx))

        def begin_sizeOfObjective(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfObjective.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfObjective(self, _r):
            return _M_omero.model.Instrument._op_sizeOfObjective.end(self, _r)

        def copyObjective(self, _ctx=None):
            return _M_omero.model.Instrument._op_copyObjective.invoke(self, ((), _ctx))

        def begin_copyObjective(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_copyObjective.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyObjective(self, _r):
            return _M_omero.model.Instrument._op_copyObjective.end(self, _r)

        def addObjective(self, target, _ctx=None):
            return _M_omero.model.Instrument._op_addObjective.invoke(self, ((target, ), _ctx))

        def begin_addObjective(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addObjective.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addObjective(self, _r):
            return _M_omero.model.Instrument._op_addObjective.end(self, _r)

        def addAllObjectiveSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_addAllObjectiveSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllObjectiveSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addAllObjectiveSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllObjectiveSet(self, _r):
            return _M_omero.model.Instrument._op_addAllObjectiveSet.end(self, _r)

        def removeObjective(self, theTarget, _ctx=None):
            return _M_omero.model.Instrument._op_removeObjective.invoke(self, ((theTarget, ), _ctx))

        def begin_removeObjective(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeObjective.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeObjective(self, _r):
            return _M_omero.model.Instrument._op_removeObjective.end(self, _r)

        def removeAllObjectiveSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllObjectiveSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllObjectiveSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllObjectiveSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllObjectiveSet(self, _r):
            return _M_omero.model.Instrument._op_removeAllObjectiveSet.end(self, _r)

        def clearObjective(self, _ctx=None):
            return _M_omero.model.Instrument._op_clearObjective.invoke(self, ((), _ctx))

        def begin_clearObjective(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_clearObjective.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearObjective(self, _r):
            return _M_omero.model.Instrument._op_clearObjective.end(self, _r)

        def reloadObjective(self, toCopy, _ctx=None):
            return _M_omero.model.Instrument._op_reloadObjective.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadObjective(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_reloadObjective.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadObjective(self, _r):
            return _M_omero.model.Instrument._op_reloadObjective.end(self, _r)

        def unloadLightSource(self, _ctx=None):
            return _M_omero.model.Instrument._op_unloadLightSource.invoke(self, ((), _ctx))

        def begin_unloadLightSource(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_unloadLightSource.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadLightSource(self, _r):
            return _M_omero.model.Instrument._op_unloadLightSource.end(self, _r)

        def sizeOfLightSource(self, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfLightSource.invoke(self, ((), _ctx))

        def begin_sizeOfLightSource(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfLightSource.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfLightSource(self, _r):
            return _M_omero.model.Instrument._op_sizeOfLightSource.end(self, _r)

        def copyLightSource(self, _ctx=None):
            return _M_omero.model.Instrument._op_copyLightSource.invoke(self, ((), _ctx))

        def begin_copyLightSource(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_copyLightSource.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyLightSource(self, _r):
            return _M_omero.model.Instrument._op_copyLightSource.end(self, _r)

        def addLightSource(self, target, _ctx=None):
            return _M_omero.model.Instrument._op_addLightSource.invoke(self, ((target, ), _ctx))

        def begin_addLightSource(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addLightSource.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addLightSource(self, _r):
            return _M_omero.model.Instrument._op_addLightSource.end(self, _r)

        def addAllLightSourceSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_addAllLightSourceSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllLightSourceSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addAllLightSourceSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllLightSourceSet(self, _r):
            return _M_omero.model.Instrument._op_addAllLightSourceSet.end(self, _r)

        def removeLightSource(self, theTarget, _ctx=None):
            return _M_omero.model.Instrument._op_removeLightSource.invoke(self, ((theTarget, ), _ctx))

        def begin_removeLightSource(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeLightSource.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeLightSource(self, _r):
            return _M_omero.model.Instrument._op_removeLightSource.end(self, _r)

        def removeAllLightSourceSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllLightSourceSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllLightSourceSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllLightSourceSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllLightSourceSet(self, _r):
            return _M_omero.model.Instrument._op_removeAllLightSourceSet.end(self, _r)

        def clearLightSource(self, _ctx=None):
            return _M_omero.model.Instrument._op_clearLightSource.invoke(self, ((), _ctx))

        def begin_clearLightSource(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_clearLightSource.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearLightSource(self, _r):
            return _M_omero.model.Instrument._op_clearLightSource.end(self, _r)

        def reloadLightSource(self, toCopy, _ctx=None):
            return _M_omero.model.Instrument._op_reloadLightSource.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadLightSource(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_reloadLightSource.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadLightSource(self, _r):
            return _M_omero.model.Instrument._op_reloadLightSource.end(self, _r)

        def unloadFilter(self, _ctx=None):
            return _M_omero.model.Instrument._op_unloadFilter.invoke(self, ((), _ctx))

        def begin_unloadFilter(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_unloadFilter.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadFilter(self, _r):
            return _M_omero.model.Instrument._op_unloadFilter.end(self, _r)

        def sizeOfFilter(self, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfFilter.invoke(self, ((), _ctx))

        def begin_sizeOfFilter(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfFilter.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfFilter(self, _r):
            return _M_omero.model.Instrument._op_sizeOfFilter.end(self, _r)

        def copyFilter(self, _ctx=None):
            return _M_omero.model.Instrument._op_copyFilter.invoke(self, ((), _ctx))

        def begin_copyFilter(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_copyFilter.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyFilter(self, _r):
            return _M_omero.model.Instrument._op_copyFilter.end(self, _r)

        def addFilter(self, target, _ctx=None):
            return _M_omero.model.Instrument._op_addFilter.invoke(self, ((target, ), _ctx))

        def begin_addFilter(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addFilter.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFilter(self, _r):
            return _M_omero.model.Instrument._op_addFilter.end(self, _r)

        def addAllFilterSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_addAllFilterSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFilterSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addAllFilterSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFilterSet(self, _r):
            return _M_omero.model.Instrument._op_addAllFilterSet.end(self, _r)

        def removeFilter(self, theTarget, _ctx=None):
            return _M_omero.model.Instrument._op_removeFilter.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFilter(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeFilter.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFilter(self, _r):
            return _M_omero.model.Instrument._op_removeFilter.end(self, _r)

        def removeAllFilterSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllFilterSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFilterSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllFilterSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFilterSet(self, _r):
            return _M_omero.model.Instrument._op_removeAllFilterSet.end(self, _r)

        def clearFilter(self, _ctx=None):
            return _M_omero.model.Instrument._op_clearFilter.invoke(self, ((), _ctx))

        def begin_clearFilter(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_clearFilter.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearFilter(self, _r):
            return _M_omero.model.Instrument._op_clearFilter.end(self, _r)

        def reloadFilter(self, toCopy, _ctx=None):
            return _M_omero.model.Instrument._op_reloadFilter.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadFilter(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_reloadFilter.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadFilter(self, _r):
            return _M_omero.model.Instrument._op_reloadFilter.end(self, _r)

        def unloadDichroic(self, _ctx=None):
            return _M_omero.model.Instrument._op_unloadDichroic.invoke(self, ((), _ctx))

        def begin_unloadDichroic(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_unloadDichroic.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadDichroic(self, _r):
            return _M_omero.model.Instrument._op_unloadDichroic.end(self, _r)

        def sizeOfDichroic(self, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfDichroic.invoke(self, ((), _ctx))

        def begin_sizeOfDichroic(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfDichroic.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfDichroic(self, _r):
            return _M_omero.model.Instrument._op_sizeOfDichroic.end(self, _r)

        def copyDichroic(self, _ctx=None):
            return _M_omero.model.Instrument._op_copyDichroic.invoke(self, ((), _ctx))

        def begin_copyDichroic(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_copyDichroic.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyDichroic(self, _r):
            return _M_omero.model.Instrument._op_copyDichroic.end(self, _r)

        def addDichroic(self, target, _ctx=None):
            return _M_omero.model.Instrument._op_addDichroic.invoke(self, ((target, ), _ctx))

        def begin_addDichroic(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addDichroic.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addDichroic(self, _r):
            return _M_omero.model.Instrument._op_addDichroic.end(self, _r)

        def addAllDichroicSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_addAllDichroicSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllDichroicSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addAllDichroicSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllDichroicSet(self, _r):
            return _M_omero.model.Instrument._op_addAllDichroicSet.end(self, _r)

        def removeDichroic(self, theTarget, _ctx=None):
            return _M_omero.model.Instrument._op_removeDichroic.invoke(self, ((theTarget, ), _ctx))

        def begin_removeDichroic(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeDichroic.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeDichroic(self, _r):
            return _M_omero.model.Instrument._op_removeDichroic.end(self, _r)

        def removeAllDichroicSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllDichroicSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllDichroicSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllDichroicSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllDichroicSet(self, _r):
            return _M_omero.model.Instrument._op_removeAllDichroicSet.end(self, _r)

        def clearDichroic(self, _ctx=None):
            return _M_omero.model.Instrument._op_clearDichroic.invoke(self, ((), _ctx))

        def begin_clearDichroic(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_clearDichroic.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearDichroic(self, _r):
            return _M_omero.model.Instrument._op_clearDichroic.end(self, _r)

        def reloadDichroic(self, toCopy, _ctx=None):
            return _M_omero.model.Instrument._op_reloadDichroic.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadDichroic(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_reloadDichroic.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadDichroic(self, _r):
            return _M_omero.model.Instrument._op_reloadDichroic.end(self, _r)

        def unloadFilterSet(self, _ctx=None):
            return _M_omero.model.Instrument._op_unloadFilterSet.invoke(self, ((), _ctx))

        def begin_unloadFilterSet(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_unloadFilterSet.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadFilterSet(self, _r):
            return _M_omero.model.Instrument._op_unloadFilterSet.end(self, _r)

        def sizeOfFilterSet(self, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfFilterSet.invoke(self, ((), _ctx))

        def begin_sizeOfFilterSet(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfFilterSet.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfFilterSet(self, _r):
            return _M_omero.model.Instrument._op_sizeOfFilterSet.end(self, _r)

        def copyFilterSet(self, _ctx=None):
            return _M_omero.model.Instrument._op_copyFilterSet.invoke(self, ((), _ctx))

        def begin_copyFilterSet(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_copyFilterSet.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyFilterSet(self, _r):
            return _M_omero.model.Instrument._op_copyFilterSet.end(self, _r)

        def addFilterSet(self, target, _ctx=None):
            return _M_omero.model.Instrument._op_addFilterSet.invoke(self, ((target, ), _ctx))

        def begin_addFilterSet(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addFilterSet.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFilterSet(self, _r):
            return _M_omero.model.Instrument._op_addFilterSet.end(self, _r)

        def addAllFilterSetSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_addAllFilterSetSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFilterSetSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addAllFilterSetSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFilterSetSet(self, _r):
            return _M_omero.model.Instrument._op_addAllFilterSetSet.end(self, _r)

        def removeFilterSet(self, theTarget, _ctx=None):
            return _M_omero.model.Instrument._op_removeFilterSet.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFilterSet(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeFilterSet.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFilterSet(self, _r):
            return _M_omero.model.Instrument._op_removeFilterSet.end(self, _r)

        def removeAllFilterSetSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllFilterSetSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFilterSetSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllFilterSetSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFilterSetSet(self, _r):
            return _M_omero.model.Instrument._op_removeAllFilterSetSet.end(self, _r)

        def clearFilterSet(self, _ctx=None):
            return _M_omero.model.Instrument._op_clearFilterSet.invoke(self, ((), _ctx))

        def begin_clearFilterSet(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_clearFilterSet.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearFilterSet(self, _r):
            return _M_omero.model.Instrument._op_clearFilterSet.end(self, _r)

        def reloadFilterSet(self, toCopy, _ctx=None):
            return _M_omero.model.Instrument._op_reloadFilterSet.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadFilterSet(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_reloadFilterSet.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadFilterSet(self, _r):
            return _M_omero.model.Instrument._op_reloadFilterSet.end(self, _r)

        def unloadOtf(self, _ctx=None):
            return _M_omero.model.Instrument._op_unloadOtf.invoke(self, ((), _ctx))

        def begin_unloadOtf(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_unloadOtf.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadOtf(self, _r):
            return _M_omero.model.Instrument._op_unloadOtf.end(self, _r)

        def sizeOfOtf(self, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfOtf.invoke(self, ((), _ctx))

        def begin_sizeOfOtf(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfOtf.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfOtf(self, _r):
            return _M_omero.model.Instrument._op_sizeOfOtf.end(self, _r)

        def copyOtf(self, _ctx=None):
            return _M_omero.model.Instrument._op_copyOtf.invoke(self, ((), _ctx))

        def begin_copyOtf(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_copyOtf.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyOtf(self, _r):
            return _M_omero.model.Instrument._op_copyOtf.end(self, _r)

        def addOTF(self, target, _ctx=None):
            return _M_omero.model.Instrument._op_addOTF.invoke(self, ((target, ), _ctx))

        def begin_addOTF(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addOTF.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addOTF(self, _r):
            return _M_omero.model.Instrument._op_addOTF.end(self, _r)

        def addAllOTFSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_addAllOTFSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllOTFSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addAllOTFSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllOTFSet(self, _r):
            return _M_omero.model.Instrument._op_addAllOTFSet.end(self, _r)

        def removeOTF(self, theTarget, _ctx=None):
            return _M_omero.model.Instrument._op_removeOTF.invoke(self, ((theTarget, ), _ctx))

        def begin_removeOTF(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeOTF.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeOTF(self, _r):
            return _M_omero.model.Instrument._op_removeOTF.end(self, _r)

        def removeAllOTFSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllOTFSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllOTFSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllOTFSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllOTFSet(self, _r):
            return _M_omero.model.Instrument._op_removeAllOTFSet.end(self, _r)

        def clearOtf(self, _ctx=None):
            return _M_omero.model.Instrument._op_clearOtf.invoke(self, ((), _ctx))

        def begin_clearOtf(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_clearOtf.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearOtf(self, _r):
            return _M_omero.model.Instrument._op_clearOtf.end(self, _r)

        def reloadOtf(self, toCopy, _ctx=None):
            return _M_omero.model.Instrument._op_reloadOtf.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadOtf(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_reloadOtf.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadOtf(self, _r):
            return _M_omero.model.Instrument._op_reloadOtf.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Instrument._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_omero.model.Instrument._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_omero.model.Instrument._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Instrument._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_omero.model.Instrument._op_copyAnnotationLinks.end(self, _r)

        def addInstrumentAnnotationLink(self, target, _ctx=None):
            return _M_omero.model.Instrument._op_addInstrumentAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addInstrumentAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addInstrumentAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addInstrumentAnnotationLink(self, _r):
            return _M_omero.model.Instrument._op_addInstrumentAnnotationLink.end(self, _r)

        def addAllInstrumentAnnotationLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_addAllInstrumentAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllInstrumentAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addAllInstrumentAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllInstrumentAnnotationLinkSet(self, _r):
            return _M_omero.model.Instrument._op_addAllInstrumentAnnotationLinkSet.end(self, _r)

        def removeInstrumentAnnotationLink(self, theTarget, _ctx=None):
            return _M_omero.model.Instrument._op_removeInstrumentAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeInstrumentAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeInstrumentAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeInstrumentAnnotationLink(self, _r):
            return _M_omero.model.Instrument._op_removeInstrumentAnnotationLink.end(self, _r)

        def removeAllInstrumentAnnotationLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllInstrumentAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllInstrumentAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeAllInstrumentAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllInstrumentAnnotationLinkSet(self, _r):
            return _M_omero.model.Instrument._op_removeAllInstrumentAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Instrument._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_omero.model.Instrument._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_omero.model.Instrument._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_omero.model.Instrument._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_omero.model.Instrument._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_omero.model.Instrument._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_omero.model.Instrument._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_omero.model.Instrument._op_linkAnnotation.end(self, _r)

        def addInstrumentAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Instrument._op_addInstrumentAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addInstrumentAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_addInstrumentAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addInstrumentAnnotationLinkToBoth(self, _r):
            return _M_omero.model.Instrument._op_addInstrumentAnnotationLinkToBoth.end(self, _r)

        def findInstrumentAnnotationLink(self, removal, _ctx=None):
            return _M_omero.model.Instrument._op_findInstrumentAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findInstrumentAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_findInstrumentAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findInstrumentAnnotationLink(self, _r):
            return _M_omero.model.Instrument._op_findInstrumentAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_omero.model.Instrument._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_omero.model.Instrument._op_unlinkAnnotation.end(self, _r)

        def removeInstrumentAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Instrument._op_removeInstrumentAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeInstrumentAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_removeInstrumentAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeInstrumentAnnotationLinkFromBoth(self, _r):
            return _M_omero.model.Instrument._op_removeInstrumentAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_omero.model.Instrument._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Instrument._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_omero.model.Instrument._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_omero.model.InstrumentPrx.ice_checkedCast(proxy, '::omero::model::Instrument', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_omero.model.InstrumentPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::omero::model::Instrument'
        ice_staticId = staticmethod(ice_staticId)

    _M_omero.model._t_InstrumentPrx = IcePy.defineProxy('::omero::model::Instrument', InstrumentPrx)

    _M_omero.model._t_Instrument = IcePy.declareClass('::omero::model::Instrument')

    _M_omero.model._t_Instrument = IcePy.defineClass('::omero::model::Instrument', Instrument, -1, (), True, False, _M_omero.model._t_IObject, (), (
        ('_version', (), _M_omero._t_RInt, False, 0),
        ('_microscope', (), _M_omero.model._t_Microscope, False, 0),
        ('_detectorSeq', (), _M_omero.model._t_InstrumentDetectorSeq, False, 0),
        ('_detectorLoaded', (), IcePy._t_bool, False, 0),
        ('_objectiveSeq', (), _M_omero.model._t_InstrumentObjectiveSeq, False, 0),
        ('_objectiveLoaded', (), IcePy._t_bool, False, 0),
        ('_lightSourceSeq', (), _M_omero.model._t_InstrumentLightSourceSeq, False, 0),
        ('_lightSourceLoaded', (), IcePy._t_bool, False, 0),
        ('_filterSeq', (), _M_omero.model._t_InstrumentFilterSeq, False, 0),
        ('_filterLoaded', (), IcePy._t_bool, False, 0),
        ('_dichroicSeq', (), _M_omero.model._t_InstrumentDichroicSeq, False, 0),
        ('_dichroicLoaded', (), IcePy._t_bool, False, 0),
        ('_filterSetSeq', (), _M_omero.model._t_InstrumentFilterSetSeq, False, 0),
        ('_filterSetLoaded', (), IcePy._t_bool, False, 0),
        ('_otfSeq', (), _M_omero.model._t_InstrumentOtfSeq, False, 0),
        ('_otfLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksSeq', (), _M_omero.model._t_InstrumentAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_omero.sys._t_CountMap, False, 0)
    ))
    Instrument._ice_type = _M_omero.model._t_Instrument

    Instrument._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RInt, False, 0), ())
    Instrument._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RInt, False, 0),), (), None, ())
    Instrument._op_getMicroscope = IcePy.Operation('getMicroscope', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_Microscope, False, 0), ())
    Instrument._op_setMicroscope = IcePy.Operation('setMicroscope', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Microscope, False, 0),), (), None, ())
    Instrument._op_unloadDetector = IcePy.Operation('unloadDetector', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_sizeOfDetector = IcePy.Operation('sizeOfDetector', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Instrument._op_copyDetector = IcePy.Operation('copyDetector', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_InstrumentDetectorSeq, False, 0), ())
    Instrument._op_addDetector = IcePy.Operation('addDetector', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Detector, False, 0),), (), None, ())
    Instrument._op_addAllDetectorSet = IcePy.Operation('addAllDetectorSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentDetectorSeq, False, 0),), (), None, ())
    Instrument._op_removeDetector = IcePy.Operation('removeDetector', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Detector, False, 0),), (), None, ())
    Instrument._op_removeAllDetectorSet = IcePy.Operation('removeAllDetectorSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentDetectorSeq, False, 0),), (), None, ())
    Instrument._op_clearDetector = IcePy.Operation('clearDetector', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_reloadDetector = IcePy.Operation('reloadDetector', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Instrument, False, 0),), (), None, ())
    Instrument._op_unloadObjective = IcePy.Operation('unloadObjective', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_sizeOfObjective = IcePy.Operation('sizeOfObjective', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Instrument._op_copyObjective = IcePy.Operation('copyObjective', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_InstrumentObjectiveSeq, False, 0), ())
    Instrument._op_addObjective = IcePy.Operation('addObjective', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Objective, False, 0),), (), None, ())
    Instrument._op_addAllObjectiveSet = IcePy.Operation('addAllObjectiveSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentObjectiveSeq, False, 0),), (), None, ())
    Instrument._op_removeObjective = IcePy.Operation('removeObjective', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Objective, False, 0),), (), None, ())
    Instrument._op_removeAllObjectiveSet = IcePy.Operation('removeAllObjectiveSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentObjectiveSeq, False, 0),), (), None, ())
    Instrument._op_clearObjective = IcePy.Operation('clearObjective', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_reloadObjective = IcePy.Operation('reloadObjective', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Instrument, False, 0),), (), None, ())
    Instrument._op_unloadLightSource = IcePy.Operation('unloadLightSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_sizeOfLightSource = IcePy.Operation('sizeOfLightSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Instrument._op_copyLightSource = IcePy.Operation('copyLightSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_InstrumentLightSourceSeq, False, 0), ())
    Instrument._op_addLightSource = IcePy.Operation('addLightSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_LightSource, False, 0),), (), None, ())
    Instrument._op_addAllLightSourceSet = IcePy.Operation('addAllLightSourceSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentLightSourceSeq, False, 0),), (), None, ())
    Instrument._op_removeLightSource = IcePy.Operation('removeLightSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_LightSource, False, 0),), (), None, ())
    Instrument._op_removeAllLightSourceSet = IcePy.Operation('removeAllLightSourceSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentLightSourceSeq, False, 0),), (), None, ())
    Instrument._op_clearLightSource = IcePy.Operation('clearLightSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_reloadLightSource = IcePy.Operation('reloadLightSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Instrument, False, 0),), (), None, ())
    Instrument._op_unloadFilter = IcePy.Operation('unloadFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_sizeOfFilter = IcePy.Operation('sizeOfFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Instrument._op_copyFilter = IcePy.Operation('copyFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_InstrumentFilterSeq, False, 0), ())
    Instrument._op_addFilter = IcePy.Operation('addFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Filter, False, 0),), (), None, ())
    Instrument._op_addAllFilterSet = IcePy.Operation('addAllFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentFilterSeq, False, 0),), (), None, ())
    Instrument._op_removeFilter = IcePy.Operation('removeFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Filter, False, 0),), (), None, ())
    Instrument._op_removeAllFilterSet = IcePy.Operation('removeAllFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentFilterSeq, False, 0),), (), None, ())
    Instrument._op_clearFilter = IcePy.Operation('clearFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_reloadFilter = IcePy.Operation('reloadFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Instrument, False, 0),), (), None, ())
    Instrument._op_unloadDichroic = IcePy.Operation('unloadDichroic', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_sizeOfDichroic = IcePy.Operation('sizeOfDichroic', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Instrument._op_copyDichroic = IcePy.Operation('copyDichroic', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_InstrumentDichroicSeq, False, 0), ())
    Instrument._op_addDichroic = IcePy.Operation('addDichroic', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Dichroic, False, 0),), (), None, ())
    Instrument._op_addAllDichroicSet = IcePy.Operation('addAllDichroicSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentDichroicSeq, False, 0),), (), None, ())
    Instrument._op_removeDichroic = IcePy.Operation('removeDichroic', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Dichroic, False, 0),), (), None, ())
    Instrument._op_removeAllDichroicSet = IcePy.Operation('removeAllDichroicSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentDichroicSeq, False, 0),), (), None, ())
    Instrument._op_clearDichroic = IcePy.Operation('clearDichroic', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_reloadDichroic = IcePy.Operation('reloadDichroic', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Instrument, False, 0),), (), None, ())
    Instrument._op_unloadFilterSet = IcePy.Operation('unloadFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_sizeOfFilterSet = IcePy.Operation('sizeOfFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Instrument._op_copyFilterSet = IcePy.Operation('copyFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_InstrumentFilterSetSeq, False, 0), ())
    Instrument._op_addFilterSet = IcePy.Operation('addFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSet, False, 0),), (), None, ())
    Instrument._op_addAllFilterSetSet = IcePy.Operation('addAllFilterSetSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentFilterSetSeq, False, 0),), (), None, ())
    Instrument._op_removeFilterSet = IcePy.Operation('removeFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSet, False, 0),), (), None, ())
    Instrument._op_removeAllFilterSetSet = IcePy.Operation('removeAllFilterSetSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentFilterSetSeq, False, 0),), (), None, ())
    Instrument._op_clearFilterSet = IcePy.Operation('clearFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_reloadFilterSet = IcePy.Operation('reloadFilterSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Instrument, False, 0),), (), None, ())
    Instrument._op_unloadOtf = IcePy.Operation('unloadOtf', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_sizeOfOtf = IcePy.Operation('sizeOfOtf', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Instrument._op_copyOtf = IcePy.Operation('copyOtf', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_InstrumentOtfSeq, False, 0), ())
    Instrument._op_addOTF = IcePy.Operation('addOTF', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_OTF, False, 0),), (), None, ())
    Instrument._op_addAllOTFSet = IcePy.Operation('addAllOTFSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentOtfSeq, False, 0),), (), None, ())
    Instrument._op_removeOTF = IcePy.Operation('removeOTF', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_OTF, False, 0),), (), None, ())
    Instrument._op_removeAllOTFSet = IcePy.Operation('removeAllOTFSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentOtfSeq, False, 0),), (), None, ())
    Instrument._op_clearOtf = IcePy.Operation('clearOtf', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_reloadOtf = IcePy.Operation('reloadOtf', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Instrument, False, 0),), (), None, ())
    Instrument._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Instrument._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_InstrumentAnnotationLinksSeq, False, 0), ())
    Instrument._op_addInstrumentAnnotationLink = IcePy.Operation('addInstrumentAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentAnnotationLink, False, 0),), (), None, ())
    Instrument._op_addAllInstrumentAnnotationLinkSet = IcePy.Operation('addAllInstrumentAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentAnnotationLinksSeq, False, 0),), (), None, ())
    Instrument._op_removeInstrumentAnnotationLink = IcePy.Operation('removeInstrumentAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentAnnotationLink, False, 0),), (), None, ())
    Instrument._op_removeAllInstrumentAnnotationLinkSet = IcePy.Operation('removeAllInstrumentAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentAnnotationLinksSeq, False, 0),), (), None, ())
    Instrument._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Instrument._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Instrument, False, 0),), (), None, ())
    Instrument._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.sys._t_CountMap, False, 0), ())
    Instrument._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), ((), _M_omero.model._t_InstrumentAnnotationLink, False, 0), ())
    Instrument._op_addInstrumentAnnotationLinkToBoth = IcePy.Operation('addInstrumentAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Instrument._op_findInstrumentAnnotationLink = IcePy.Operation('findInstrumentAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), ((), _M_omero.model._t_InstrumentAnnotationLinksSeq, False, 0), ())
    Instrument._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), None, ())
    Instrument._op_removeInstrumentAnnotationLinkFromBoth = IcePy.Operation('removeInstrumentAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_InstrumentAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Instrument._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_InstrumentLinkedAnnotationSeq, False, 0), ())

    _M_omero.model.Instrument = Instrument
    del Instrument

    _M_omero.model.InstrumentPrx = InstrumentPrx
    del InstrumentPrx

# End of module omero.model

__name__ = 'omero'

# End of module omero
