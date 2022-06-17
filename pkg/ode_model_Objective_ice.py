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

if 'Immersion' not in _M_ode.model.__dict__:
    _M_ode.model._t_Immersion = IcePy.declareClass('::ode::model::Immersion')
    _M_ode.model._t_ImmersionPrx = IcePy.declareProxy('::ode::model::Immersion')

if 'Correction' not in _M_ode.model.__dict__:
    _M_ode.model._t_Correction = IcePy.declareClass('::ode::model::Correction')
    _M_ode.model._t_CorrectionPrx = IcePy.declareProxy('::ode::model::Correction')

if 'Length' not in _M_ode.model.__dict__:
    _M_ode.model._t_Length = IcePy.declareClass('::ode::model::Length')
    _M_ode.model._t_LengthPrx = IcePy.declareProxy('::ode::model::Length')

if 'Instrument' not in _M_ode.model.__dict__:
    _M_ode.model._t_Instrument = IcePy.declareClass('::ode::model::Instrument')
    _M_ode.model._t_InstrumentPrx = IcePy.declareProxy('::ode::model::Instrument')

if 'ObjectiveAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_ObjectiveAnnotationLink = IcePy.declareClass('::ode::model::ObjectiveAnnotationLink')
    _M_ode.model._t_ObjectiveAnnotationLinkPrx = IcePy.declareProxy('::ode::model::ObjectiveAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_ObjectiveAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ObjectiveAnnotationLinksSeq = IcePy.defineSequence('::ode::model::ObjectiveAnnotationLinksSeq', (), _M_ode.model._t_ObjectiveAnnotationLink)

if '_t_ObjectiveLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ObjectiveLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::ObjectiveLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Objective' not in _M_ode.model.__dict__:
    _M_ode.model.Objective = Ice.createTempClass()
    class Objective(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _manufacturer=None, _model=None, _lotNumber=None, _serialNumber=None, _nominalMagnification=None, _calibratedMagnification=None, _lensNA=None, _immersion=None, _correction=None, _workingDistance=None, _iris=None, _instrument=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.Objective:
                raise RuntimeError('ode.model.Objective is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._manufacturer = _manufacturer
            self._model = _model
            self._lotNumber = _lotNumber
            self._serialNumber = _serialNumber
            self._nominalMagnification = _nominalMagnification
            self._calibratedMagnification = _calibratedMagnification
            self._lensNA = _lensNA
            self._immersion = _immersion
            self._correction = _correction
            self._workingDistance = _workingDistance
            self._iris = _iris
            self._instrument = _instrument
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Objective')

        def ice_id(self, current=None):
            return '::ode::model::Objective'

        def ice_staticId():
            return '::ode::model::Objective'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getManufacturer(self, current=None):
            pass

        def setManufacturer(self, theManufacturer, current=None):
            pass

        def getModel(self, current=None):
            pass

        def setModel(self, theModel, current=None):
            pass

        def getLotNumber(self, current=None):
            pass

        def setLotNumber(self, theLotNumber, current=None):
            pass

        def getSerialNumber(self, current=None):
            pass

        def setSerialNumber(self, theSerialNumber, current=None):
            pass

        def getNominalMagnification(self, current=None):
            pass

        def setNominalMagnification(self, theNominalMagnification, current=None):
            pass

        def getCalibratedMagnification(self, current=None):
            pass

        def setCalibratedMagnification(self, theCalibratedMagnification, current=None):
            pass

        def getLensNA(self, current=None):
            pass

        def setLensNA(self, theLensNA, current=None):
            pass

        def getImmersion(self, current=None):
            pass

        def setImmersion(self, theImmersion, current=None):
            pass

        def getCorrection(self, current=None):
            pass

        def setCorrection(self, theCorrection, current=None):
            pass

        def getWorkingDistance(self, current=None):
            pass

        def setWorkingDistance(self, theWorkingDistance, current=None):
            pass

        def getIris(self, current=None):
            pass

        def setIris(self, theIris, current=None):
            pass

        def getInstrument(self, current=None):
            pass

        def setInstrument(self, theInstrument, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addObjectiveAnnotationLink(self, target, current=None):
            pass

        def addAllObjectiveAnnotationLinkSet(self, targets, current=None):
            pass

        def removeObjectiveAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllObjectiveAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addObjectiveAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findObjectiveAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeObjectiveAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Objective)

        __repr__ = __str__

    _M_ode.model.ObjectivePrx = Ice.createTempClass()
    class ObjectivePrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Objective._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Objective._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Objective._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Objective._op_setVersion.end(self, _r)

        def getManufacturer(self, _ctx=None):
            return _M_ode.model.Objective._op_getManufacturer.invoke(self, ((), _ctx))

        def begin_getManufacturer(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getManufacturer.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getManufacturer(self, _r):
            return _M_ode.model.Objective._op_getManufacturer.end(self, _r)

        def setManufacturer(self, theManufacturer, _ctx=None):
            return _M_ode.model.Objective._op_setManufacturer.invoke(self, ((theManufacturer, ), _ctx))

        def begin_setManufacturer(self, theManufacturer, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setManufacturer.begin(self, ((theManufacturer, ), _response, _ex, _sent, _ctx))

        def end_setManufacturer(self, _r):
            return _M_ode.model.Objective._op_setManufacturer.end(self, _r)

        def getModel(self, _ctx=None):
            return _M_ode.model.Objective._op_getModel.invoke(self, ((), _ctx))

        def begin_getModel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getModel.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getModel(self, _r):
            return _M_ode.model.Objective._op_getModel.end(self, _r)

        def setModel(self, theModel, _ctx=None):
            return _M_ode.model.Objective._op_setModel.invoke(self, ((theModel, ), _ctx))

        def begin_setModel(self, theModel, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setModel.begin(self, ((theModel, ), _response, _ex, _sent, _ctx))

        def end_setModel(self, _r):
            return _M_ode.model.Objective._op_setModel.end(self, _r)

        def getLotNumber(self, _ctx=None):
            return _M_ode.model.Objective._op_getLotNumber.invoke(self, ((), _ctx))

        def begin_getLotNumber(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getLotNumber.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLotNumber(self, _r):
            return _M_ode.model.Objective._op_getLotNumber.end(self, _r)

        def setLotNumber(self, theLotNumber, _ctx=None):
            return _M_ode.model.Objective._op_setLotNumber.invoke(self, ((theLotNumber, ), _ctx))

        def begin_setLotNumber(self, theLotNumber, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setLotNumber.begin(self, ((theLotNumber, ), _response, _ex, _sent, _ctx))

        def end_setLotNumber(self, _r):
            return _M_ode.model.Objective._op_setLotNumber.end(self, _r)

        def getSerialNumber(self, _ctx=None):
            return _M_ode.model.Objective._op_getSerialNumber.invoke(self, ((), _ctx))

        def begin_getSerialNumber(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getSerialNumber.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSerialNumber(self, _r):
            return _M_ode.model.Objective._op_getSerialNumber.end(self, _r)

        def setSerialNumber(self, theSerialNumber, _ctx=None):
            return _M_ode.model.Objective._op_setSerialNumber.invoke(self, ((theSerialNumber, ), _ctx))

        def begin_setSerialNumber(self, theSerialNumber, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setSerialNumber.begin(self, ((theSerialNumber, ), _response, _ex, _sent, _ctx))

        def end_setSerialNumber(self, _r):
            return _M_ode.model.Objective._op_setSerialNumber.end(self, _r)

        def getNominalMagnification(self, _ctx=None):
            return _M_ode.model.Objective._op_getNominalMagnification.invoke(self, ((), _ctx))

        def begin_getNominalMagnification(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getNominalMagnification.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getNominalMagnification(self, _r):
            return _M_ode.model.Objective._op_getNominalMagnification.end(self, _r)

        def setNominalMagnification(self, theNominalMagnification, _ctx=None):
            return _M_ode.model.Objective._op_setNominalMagnification.invoke(self, ((theNominalMagnification, ), _ctx))

        def begin_setNominalMagnification(self, theNominalMagnification, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setNominalMagnification.begin(self, ((theNominalMagnification, ), _response, _ex, _sent, _ctx))

        def end_setNominalMagnification(self, _r):
            return _M_ode.model.Objective._op_setNominalMagnification.end(self, _r)

        def getCalibratedMagnification(self, _ctx=None):
            return _M_ode.model.Objective._op_getCalibratedMagnification.invoke(self, ((), _ctx))

        def begin_getCalibratedMagnification(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getCalibratedMagnification.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCalibratedMagnification(self, _r):
            return _M_ode.model.Objective._op_getCalibratedMagnification.end(self, _r)

        def setCalibratedMagnification(self, theCalibratedMagnification, _ctx=None):
            return _M_ode.model.Objective._op_setCalibratedMagnification.invoke(self, ((theCalibratedMagnification, ), _ctx))

        def begin_setCalibratedMagnification(self, theCalibratedMagnification, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setCalibratedMagnification.begin(self, ((theCalibratedMagnification, ), _response, _ex, _sent, _ctx))

        def end_setCalibratedMagnification(self, _r):
            return _M_ode.model.Objective._op_setCalibratedMagnification.end(self, _r)

        def getLensNA(self, _ctx=None):
            return _M_ode.model.Objective._op_getLensNA.invoke(self, ((), _ctx))

        def begin_getLensNA(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getLensNA.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLensNA(self, _r):
            return _M_ode.model.Objective._op_getLensNA.end(self, _r)

        def setLensNA(self, theLensNA, _ctx=None):
            return _M_ode.model.Objective._op_setLensNA.invoke(self, ((theLensNA, ), _ctx))

        def begin_setLensNA(self, theLensNA, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setLensNA.begin(self, ((theLensNA, ), _response, _ex, _sent, _ctx))

        def end_setLensNA(self, _r):
            return _M_ode.model.Objective._op_setLensNA.end(self, _r)

        def getImmersion(self, _ctx=None):
            return _M_ode.model.Objective._op_getImmersion.invoke(self, ((), _ctx))

        def begin_getImmersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getImmersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getImmersion(self, _r):
            return _M_ode.model.Objective._op_getImmersion.end(self, _r)

        def setImmersion(self, theImmersion, _ctx=None):
            return _M_ode.model.Objective._op_setImmersion.invoke(self, ((theImmersion, ), _ctx))

        def begin_setImmersion(self, theImmersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setImmersion.begin(self, ((theImmersion, ), _response, _ex, _sent, _ctx))

        def end_setImmersion(self, _r):
            return _M_ode.model.Objective._op_setImmersion.end(self, _r)

        def getCorrection(self, _ctx=None):
            return _M_ode.model.Objective._op_getCorrection.invoke(self, ((), _ctx))

        def begin_getCorrection(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getCorrection.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCorrection(self, _r):
            return _M_ode.model.Objective._op_getCorrection.end(self, _r)

        def setCorrection(self, theCorrection, _ctx=None):
            return _M_ode.model.Objective._op_setCorrection.invoke(self, ((theCorrection, ), _ctx))

        def begin_setCorrection(self, theCorrection, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setCorrection.begin(self, ((theCorrection, ), _response, _ex, _sent, _ctx))

        def end_setCorrection(self, _r):
            return _M_ode.model.Objective._op_setCorrection.end(self, _r)

        def getWorkingDistance(self, _ctx=None):
            return _M_ode.model.Objective._op_getWorkingDistance.invoke(self, ((), _ctx))

        def begin_getWorkingDistance(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getWorkingDistance.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getWorkingDistance(self, _r):
            return _M_ode.model.Objective._op_getWorkingDistance.end(self, _r)

        def setWorkingDistance(self, theWorkingDistance, _ctx=None):
            return _M_ode.model.Objective._op_setWorkingDistance.invoke(self, ((theWorkingDistance, ), _ctx))

        def begin_setWorkingDistance(self, theWorkingDistance, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setWorkingDistance.begin(self, ((theWorkingDistance, ), _response, _ex, _sent, _ctx))

        def end_setWorkingDistance(self, _r):
            return _M_ode.model.Objective._op_setWorkingDistance.end(self, _r)

        def getIris(self, _ctx=None):
            return _M_ode.model.Objective._op_getIris.invoke(self, ((), _ctx))

        def begin_getIris(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getIris.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getIris(self, _r):
            return _M_ode.model.Objective._op_getIris.end(self, _r)

        def setIris(self, theIris, _ctx=None):
            return _M_ode.model.Objective._op_setIris.invoke(self, ((theIris, ), _ctx))

        def begin_setIris(self, theIris, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setIris.begin(self, ((theIris, ), _response, _ex, _sent, _ctx))

        def end_setIris(self, _r):
            return _M_ode.model.Objective._op_setIris.end(self, _r)

        def getInstrument(self, _ctx=None):
            return _M_ode.model.Objective._op_getInstrument.invoke(self, ((), _ctx))

        def begin_getInstrument(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getInstrument.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getInstrument(self, _r):
            return _M_ode.model.Objective._op_getInstrument.end(self, _r)

        def setInstrument(self, theInstrument, _ctx=None):
            return _M_ode.model.Objective._op_setInstrument.invoke(self, ((theInstrument, ), _ctx))

        def begin_setInstrument(self, theInstrument, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_setInstrument.begin(self, ((theInstrument, ), _response, _ex, _sent, _ctx))

        def end_setInstrument(self, _r):
            return _M_ode.model.Objective._op_setInstrument.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Objective._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Objective._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Objective._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Objective._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Objective._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Objective._op_copyAnnotationLinks.end(self, _r)

        def addObjectiveAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Objective._op_addObjectiveAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addObjectiveAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_addObjectiveAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addObjectiveAnnotationLink(self, _r):
            return _M_ode.model.Objective._op_addObjectiveAnnotationLink.end(self, _r)

        def addAllObjectiveAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Objective._op_addAllObjectiveAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllObjectiveAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_addAllObjectiveAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllObjectiveAnnotationLinkSet(self, _r):
            return _M_ode.model.Objective._op_addAllObjectiveAnnotationLinkSet.end(self, _r)

        def removeObjectiveAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Objective._op_removeObjectiveAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeObjectiveAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_removeObjectiveAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeObjectiveAnnotationLink(self, _r):
            return _M_ode.model.Objective._op_removeObjectiveAnnotationLink.end(self, _r)

        def removeAllObjectiveAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Objective._op_removeAllObjectiveAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllObjectiveAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_removeAllObjectiveAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllObjectiveAnnotationLinkSet(self, _r):
            return _M_ode.model.Objective._op_removeAllObjectiveAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Objective._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Objective._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Objective._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Objective._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Objective._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Objective._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Objective._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Objective._op_linkAnnotation.end(self, _r)

        def addObjectiveAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Objective._op_addObjectiveAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addObjectiveAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_addObjectiveAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addObjectiveAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Objective._op_addObjectiveAnnotationLinkToBoth.end(self, _r)

        def findObjectiveAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Objective._op_findObjectiveAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findObjectiveAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_findObjectiveAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findObjectiveAnnotationLink(self, _r):
            return _M_ode.model.Objective._op_findObjectiveAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Objective._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Objective._op_unlinkAnnotation.end(self, _r)

        def removeObjectiveAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Objective._op_removeObjectiveAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeObjectiveAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_removeObjectiveAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeObjectiveAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Objective._op_removeObjectiveAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Objective._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Objective._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Objective._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ObjectivePrx.ice_checkedCast(proxy, '::ode::model::Objective', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ObjectivePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Objective'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ObjectivePrx = IcePy.defineProxy('::ode::model::Objective', ObjectivePrx)

    _M_ode.model._t_Objective = IcePy.declareClass('::ode::model::Objective')

    _M_ode.model._t_Objective = IcePy.defineClass('::ode::model::Objective', Objective, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_manufacturer', (), _M_ode._t_RString, False, 0),
        ('_model', (), _M_ode._t_RString, False, 0),
        ('_lotNumber', (), _M_ode._t_RString, False, 0),
        ('_serialNumber', (), _M_ode._t_RString, False, 0),
        ('_nominalMagnification', (), _M_ode._t_RDouble, False, 0),
        ('_calibratedMagnification', (), _M_ode._t_RDouble, False, 0),
        ('_lensNA', (), _M_ode._t_RDouble, False, 0),
        ('_immersion', (), _M_ode.model._t_Immersion, False, 0),
        ('_correction', (), _M_ode.model._t_Correction, False, 0),
        ('_workingDistance', (), _M_ode.model._t_Length, False, 0),
        ('_iris', (), _M_ode._t_RBool, False, 0),
        ('_instrument', (), _M_ode.model._t_Instrument, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_ObjectiveAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    Objective._ice_type = _M_ode.model._t_Objective

    Objective._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Objective._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Objective._op_getManufacturer = IcePy.Operation('getManufacturer', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Objective._op_setManufacturer = IcePy.Operation('setManufacturer', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Objective._op_getModel = IcePy.Operation('getModel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Objective._op_setModel = IcePy.Operation('setModel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Objective._op_getLotNumber = IcePy.Operation('getLotNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Objective._op_setLotNumber = IcePy.Operation('setLotNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Objective._op_getSerialNumber = IcePy.Operation('getSerialNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Objective._op_setSerialNumber = IcePy.Operation('setSerialNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Objective._op_getNominalMagnification = IcePy.Operation('getNominalMagnification', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Objective._op_setNominalMagnification = IcePy.Operation('setNominalMagnification', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Objective._op_getCalibratedMagnification = IcePy.Operation('getCalibratedMagnification', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Objective._op_setCalibratedMagnification = IcePy.Operation('setCalibratedMagnification', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Objective._op_getLensNA = IcePy.Operation('getLensNA', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    Objective._op_setLensNA = IcePy.Operation('setLensNA', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    Objective._op_getImmersion = IcePy.Operation('getImmersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Immersion, False, 0), ())
    Objective._op_setImmersion = IcePy.Operation('setImmersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Immersion, False, 0),), (), None, ())
    Objective._op_getCorrection = IcePy.Operation('getCorrection', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Correction, False, 0), ())
    Objective._op_setCorrection = IcePy.Operation('setCorrection', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Correction, False, 0),), (), None, ())
    Objective._op_getWorkingDistance = IcePy.Operation('getWorkingDistance', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    Objective._op_setWorkingDistance = IcePy.Operation('setWorkingDistance', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    Objective._op_getIris = IcePy.Operation('getIris', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    Objective._op_setIris = IcePy.Operation('setIris', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    Objective._op_getInstrument = IcePy.Operation('getInstrument', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Instrument, False, 0), ())
    Objective._op_setInstrument = IcePy.Operation('setInstrument', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Instrument, False, 0),), (), None, ())
    Objective._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Objective._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Objective._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ObjectiveAnnotationLinksSeq, False, 0), ())
    Objective._op_addObjectiveAnnotationLink = IcePy.Operation('addObjectiveAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ObjectiveAnnotationLink, False, 0),), (), None, ())
    Objective._op_addAllObjectiveAnnotationLinkSet = IcePy.Operation('addAllObjectiveAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ObjectiveAnnotationLinksSeq, False, 0),), (), None, ())
    Objective._op_removeObjectiveAnnotationLink = IcePy.Operation('removeObjectiveAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ObjectiveAnnotationLink, False, 0),), (), None, ())
    Objective._op_removeAllObjectiveAnnotationLinkSet = IcePy.Operation('removeAllObjectiveAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ObjectiveAnnotationLinksSeq, False, 0),), (), None, ())
    Objective._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Objective._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Objective, False, 0),), (), None, ())
    Objective._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Objective._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ObjectiveAnnotationLink, False, 0), ())
    Objective._op_addObjectiveAnnotationLinkToBoth = IcePy.Operation('addObjectiveAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ObjectiveAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Objective._op_findObjectiveAnnotationLink = IcePy.Operation('findObjectiveAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ObjectiveAnnotationLinksSeq, False, 0), ())
    Objective._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Objective._op_removeObjectiveAnnotationLinkFromBoth = IcePy.Operation('removeObjectiveAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ObjectiveAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Objective._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ObjectiveLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.Objective = Objective
    del Objective

    _M_ode.model.ObjectivePrx = ObjectivePrx
    del ObjectivePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
