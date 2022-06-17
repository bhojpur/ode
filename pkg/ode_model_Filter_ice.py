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

if 'FilterType' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterType = IcePy.declareClass('::omero::model::FilterType')
    _M_omero.model._t_FilterTypePrx = IcePy.declareProxy('::omero::model::FilterType')

if 'TransmittanceRange' not in _M_omero.model.__dict__:
    _M_omero.model._t_TransmittanceRange = IcePy.declareClass('::omero::model::TransmittanceRange')
    _M_omero.model._t_TransmittanceRangePrx = IcePy.declareProxy('::omero::model::TransmittanceRange')

if 'Instrument' not in _M_omero.model.__dict__:
    _M_omero.model._t_Instrument = IcePy.declareClass('::omero::model::Instrument')
    _M_omero.model._t_InstrumentPrx = IcePy.declareProxy('::omero::model::Instrument')

if 'FilterSetExcitationFilterLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterSetExcitationFilterLink = IcePy.declareClass('::omero::model::FilterSetExcitationFilterLink')
    _M_omero.model._t_FilterSetExcitationFilterLinkPrx = IcePy.declareProxy('::omero::model::FilterSetExcitationFilterLink')

if 'FilterSet' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterSet = IcePy.declareClass('::omero::model::FilterSet')
    _M_omero.model._t_FilterSetPrx = IcePy.declareProxy('::omero::model::FilterSet')

if 'FilterSetEmissionFilterLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterSetEmissionFilterLink = IcePy.declareClass('::omero::model::FilterSetEmissionFilterLink')
    _M_omero.model._t_FilterSetEmissionFilterLinkPrx = IcePy.declareProxy('::omero::model::FilterSetEmissionFilterLink')

if 'FilterAnnotationLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterAnnotationLink = IcePy.declareClass('::omero::model::FilterAnnotationLink')
    _M_omero.model._t_FilterAnnotationLinkPrx = IcePy.declareProxy('::omero::model::FilterAnnotationLink')

if 'Annotation' not in _M_omero.model.__dict__:
    _M_omero.model._t_Annotation = IcePy.declareClass('::omero::model::Annotation')
    _M_omero.model._t_AnnotationPrx = IcePy.declareProxy('::omero::model::Annotation')

if 'Details' not in _M_omero.model.__dict__:
    _M_omero.model._t_Details = IcePy.declareClass('::omero::model::Details')
    _M_omero.model._t_DetailsPrx = IcePy.declareProxy('::omero::model::Details')

if '_t_FilterExcitationFilterLinkSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterExcitationFilterLinkSeq = IcePy.defineSequence('::omero::model::FilterExcitationFilterLinkSeq', (), _M_omero.model._t_FilterSetExcitationFilterLink)

if '_t_FilterLinkedExcitationFilterSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterLinkedExcitationFilterSeq = IcePy.defineSequence('::omero::model::FilterLinkedExcitationFilterSeq', (), _M_omero.model._t_FilterSet)

if '_t_FilterEmissionFilterLinkSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterEmissionFilterLinkSeq = IcePy.defineSequence('::omero::model::FilterEmissionFilterLinkSeq', (), _M_omero.model._t_FilterSetEmissionFilterLink)

if '_t_FilterLinkedEmissionFilterSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterLinkedEmissionFilterSeq = IcePy.defineSequence('::omero::model::FilterLinkedEmissionFilterSeq', (), _M_omero.model._t_FilterSet)

if '_t_FilterAnnotationLinksSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterAnnotationLinksSeq = IcePy.defineSequence('::omero::model::FilterAnnotationLinksSeq', (), _M_omero.model._t_FilterAnnotationLink)

if '_t_FilterLinkedAnnotationSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilterLinkedAnnotationSeq = IcePy.defineSequence('::omero::model::FilterLinkedAnnotationSeq', (), _M_omero.model._t_Annotation)

if 'Filter' not in _M_omero.model.__dict__:
    _M_omero.model.Filter = Ice.createTempClass()
    class Filter(_M_omero.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _manufacturer=None, _model=None, _lotNumber=None, _serialNumber=None, _filterWheel=None, _type=None, _transmittanceRange=None, _instrument=None, _excitationFilterLinkSeq=None, _excitationFilterLinkLoaded=False, _excitationFilterLinkCountPerOwner=None, _emissionFilterLinkSeq=None, _emissionFilterLinkLoaded=False, _emissionFilterLinkCountPerOwner=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_omero.model.Filter:
                raise RuntimeError('omero.model.Filter is an abstract class')
            _M_omero.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._manufacturer = _manufacturer
            self._model = _model
            self._lotNumber = _lotNumber
            self._serialNumber = _serialNumber
            self._filterWheel = _filterWheel
            self._type = _type
            self._transmittanceRange = _transmittanceRange
            self._instrument = _instrument
            self._excitationFilterLinkSeq = _excitationFilterLinkSeq
            self._excitationFilterLinkLoaded = _excitationFilterLinkLoaded
            self._excitationFilterLinkCountPerOwner = _excitationFilterLinkCountPerOwner
            self._emissionFilterLinkSeq = _emissionFilterLinkSeq
            self._emissionFilterLinkLoaded = _emissionFilterLinkLoaded
            self._emissionFilterLinkCountPerOwner = _emissionFilterLinkCountPerOwner
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::omero::model::Filter', '::omero::model::IObject')

        def ice_id(self, current=None):
            return '::omero::model::Filter'

        def ice_staticId():
            return '::omero::model::Filter'
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

        def getFilterWheel(self, current=None):
            pass

        def setFilterWheel(self, theFilterWheel, current=None):
            pass

        def getType(self, current=None):
            pass

        def setType(self, theType, current=None):
            pass

        def getTransmittanceRange(self, current=None):
            pass

        def setTransmittanceRange(self, theTransmittanceRange, current=None):
            pass

        def getInstrument(self, current=None):
            pass

        def setInstrument(self, theInstrument, current=None):
            pass

        def unloadExcitationFilterLink(self, current=None):
            pass

        def sizeOfExcitationFilterLink(self, current=None):
            pass

        def copyExcitationFilterLink(self, current=None):
            pass

        def addFilterSetExcitationFilterLink(self, target, current=None):
            pass

        def addAllFilterSetExcitationFilterLinkSet(self, targets, current=None):
            pass

        def removeFilterSetExcitationFilterLink(self, theTarget, current=None):
            pass

        def removeAllFilterSetExcitationFilterLinkSet(self, targets, current=None):
            pass

        def clearExcitationFilterLink(self, current=None):
            pass

        def reloadExcitationFilterLink(self, toCopy, current=None):
            pass

        def getExcitationFilterLinkCountPerOwner(self, current=None):
            pass

        def linkExcitationFilter(self, addition, current=None):
            pass

        def addFilterSetExcitationFilterLinkToBoth(self, link, bothSides, current=None):
            pass

        def findFilterSetExcitationFilterLink(self, removal, current=None):
            pass

        def unlinkExcitationFilter(self, removal, current=None):
            pass

        def removeFilterSetExcitationFilterLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedExcitationFilterList(self, current=None):
            pass

        def unloadEmissionFilterLink(self, current=None):
            pass

        def sizeOfEmissionFilterLink(self, current=None):
            pass

        def copyEmissionFilterLink(self, current=None):
            pass

        def addFilterSetEmissionFilterLink(self, target, current=None):
            pass

        def addAllFilterSetEmissionFilterLinkSet(self, targets, current=None):
            pass

        def removeFilterSetEmissionFilterLink(self, theTarget, current=None):
            pass

        def removeAllFilterSetEmissionFilterLinkSet(self, targets, current=None):
            pass

        def clearEmissionFilterLink(self, current=None):
            pass

        def reloadEmissionFilterLink(self, toCopy, current=None):
            pass

        def getEmissionFilterLinkCountPerOwner(self, current=None):
            pass

        def linkEmissionFilter(self, addition, current=None):
            pass

        def addFilterSetEmissionFilterLinkToBoth(self, link, bothSides, current=None):
            pass

        def findFilterSetEmissionFilterLink(self, removal, current=None):
            pass

        def unlinkEmissionFilter(self, removal, current=None):
            pass

        def removeFilterSetEmissionFilterLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedEmissionFilterList(self, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addFilterAnnotationLink(self, target, current=None):
            pass

        def addAllFilterAnnotationLinkSet(self, targets, current=None):
            pass

        def removeFilterAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllFilterAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addFilterAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findFilterAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeFilterAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_omero.model._t_Filter)

        __repr__ = __str__

    _M_omero.model.FilterPrx = Ice.createTempClass()
    class FilterPrx(_M_omero.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_omero.model.Filter._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_omero.model.Filter._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_omero.model.Filter._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_omero.model.Filter._op_setVersion.end(self, _r)

        def getManufacturer(self, _ctx=None):
            return _M_omero.model.Filter._op_getManufacturer.invoke(self, ((), _ctx))

        def begin_getManufacturer(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getManufacturer.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getManufacturer(self, _r):
            return _M_omero.model.Filter._op_getManufacturer.end(self, _r)

        def setManufacturer(self, theManufacturer, _ctx=None):
            return _M_omero.model.Filter._op_setManufacturer.invoke(self, ((theManufacturer, ), _ctx))

        def begin_setManufacturer(self, theManufacturer, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_setManufacturer.begin(self, ((theManufacturer, ), _response, _ex, _sent, _ctx))

        def end_setManufacturer(self, _r):
            return _M_omero.model.Filter._op_setManufacturer.end(self, _r)

        def getModel(self, _ctx=None):
            return _M_omero.model.Filter._op_getModel.invoke(self, ((), _ctx))

        def begin_getModel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getModel.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getModel(self, _r):
            return _M_omero.model.Filter._op_getModel.end(self, _r)

        def setModel(self, theModel, _ctx=None):
            return _M_omero.model.Filter._op_setModel.invoke(self, ((theModel, ), _ctx))

        def begin_setModel(self, theModel, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_setModel.begin(self, ((theModel, ), _response, _ex, _sent, _ctx))

        def end_setModel(self, _r):
            return _M_omero.model.Filter._op_setModel.end(self, _r)

        def getLotNumber(self, _ctx=None):
            return _M_omero.model.Filter._op_getLotNumber.invoke(self, ((), _ctx))

        def begin_getLotNumber(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getLotNumber.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLotNumber(self, _r):
            return _M_omero.model.Filter._op_getLotNumber.end(self, _r)

        def setLotNumber(self, theLotNumber, _ctx=None):
            return _M_omero.model.Filter._op_setLotNumber.invoke(self, ((theLotNumber, ), _ctx))

        def begin_setLotNumber(self, theLotNumber, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_setLotNumber.begin(self, ((theLotNumber, ), _response, _ex, _sent, _ctx))

        def end_setLotNumber(self, _r):
            return _M_omero.model.Filter._op_setLotNumber.end(self, _r)

        def getSerialNumber(self, _ctx=None):
            return _M_omero.model.Filter._op_getSerialNumber.invoke(self, ((), _ctx))

        def begin_getSerialNumber(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getSerialNumber.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSerialNumber(self, _r):
            return _M_omero.model.Filter._op_getSerialNumber.end(self, _r)

        def setSerialNumber(self, theSerialNumber, _ctx=None):
            return _M_omero.model.Filter._op_setSerialNumber.invoke(self, ((theSerialNumber, ), _ctx))

        def begin_setSerialNumber(self, theSerialNumber, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_setSerialNumber.begin(self, ((theSerialNumber, ), _response, _ex, _sent, _ctx))

        def end_setSerialNumber(self, _r):
            return _M_omero.model.Filter._op_setSerialNumber.end(self, _r)

        def getFilterWheel(self, _ctx=None):
            return _M_omero.model.Filter._op_getFilterWheel.invoke(self, ((), _ctx))

        def begin_getFilterWheel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getFilterWheel.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFilterWheel(self, _r):
            return _M_omero.model.Filter._op_getFilterWheel.end(self, _r)

        def setFilterWheel(self, theFilterWheel, _ctx=None):
            return _M_omero.model.Filter._op_setFilterWheel.invoke(self, ((theFilterWheel, ), _ctx))

        def begin_setFilterWheel(self, theFilterWheel, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_setFilterWheel.begin(self, ((theFilterWheel, ), _response, _ex, _sent, _ctx))

        def end_setFilterWheel(self, _r):
            return _M_omero.model.Filter._op_setFilterWheel.end(self, _r)

        def getType(self, _ctx=None):
            return _M_omero.model.Filter._op_getType.invoke(self, ((), _ctx))

        def begin_getType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getType(self, _r):
            return _M_omero.model.Filter._op_getType.end(self, _r)

        def setType(self, theType, _ctx=None):
            return _M_omero.model.Filter._op_setType.invoke(self, ((theType, ), _ctx))

        def begin_setType(self, theType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_setType.begin(self, ((theType, ), _response, _ex, _sent, _ctx))

        def end_setType(self, _r):
            return _M_omero.model.Filter._op_setType.end(self, _r)

        def getTransmittanceRange(self, _ctx=None):
            return _M_omero.model.Filter._op_getTransmittanceRange.invoke(self, ((), _ctx))

        def begin_getTransmittanceRange(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getTransmittanceRange.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTransmittanceRange(self, _r):
            return _M_omero.model.Filter._op_getTransmittanceRange.end(self, _r)

        def setTransmittanceRange(self, theTransmittanceRange, _ctx=None):
            return _M_omero.model.Filter._op_setTransmittanceRange.invoke(self, ((theTransmittanceRange, ), _ctx))

        def begin_setTransmittanceRange(self, theTransmittanceRange, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_setTransmittanceRange.begin(self, ((theTransmittanceRange, ), _response, _ex, _sent, _ctx))

        def end_setTransmittanceRange(self, _r):
            return _M_omero.model.Filter._op_setTransmittanceRange.end(self, _r)

        def getInstrument(self, _ctx=None):
            return _M_omero.model.Filter._op_getInstrument.invoke(self, ((), _ctx))

        def begin_getInstrument(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getInstrument.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getInstrument(self, _r):
            return _M_omero.model.Filter._op_getInstrument.end(self, _r)

        def setInstrument(self, theInstrument, _ctx=None):
            return _M_omero.model.Filter._op_setInstrument.invoke(self, ((theInstrument, ), _ctx))

        def begin_setInstrument(self, theInstrument, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_setInstrument.begin(self, ((theInstrument, ), _response, _ex, _sent, _ctx))

        def end_setInstrument(self, _r):
            return _M_omero.model.Filter._op_setInstrument.end(self, _r)

        def unloadExcitationFilterLink(self, _ctx=None):
            return _M_omero.model.Filter._op_unloadExcitationFilterLink.invoke(self, ((), _ctx))

        def begin_unloadExcitationFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_unloadExcitationFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadExcitationFilterLink(self, _r):
            return _M_omero.model.Filter._op_unloadExcitationFilterLink.end(self, _r)

        def sizeOfExcitationFilterLink(self, _ctx=None):
            return _M_omero.model.Filter._op_sizeOfExcitationFilterLink.invoke(self, ((), _ctx))

        def begin_sizeOfExcitationFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_sizeOfExcitationFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfExcitationFilterLink(self, _r):
            return _M_omero.model.Filter._op_sizeOfExcitationFilterLink.end(self, _r)

        def copyExcitationFilterLink(self, _ctx=None):
            return _M_omero.model.Filter._op_copyExcitationFilterLink.invoke(self, ((), _ctx))

        def begin_copyExcitationFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_copyExcitationFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyExcitationFilterLink(self, _r):
            return _M_omero.model.Filter._op_copyExcitationFilterLink.end(self, _r)

        def addFilterSetExcitationFilterLink(self, target, _ctx=None):
            return _M_omero.model.Filter._op_addFilterSetExcitationFilterLink.invoke(self, ((target, ), _ctx))

        def begin_addFilterSetExcitationFilterLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_addFilterSetExcitationFilterLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFilterSetExcitationFilterLink(self, _r):
            return _M_omero.model.Filter._op_addFilterSetExcitationFilterLink.end(self, _r)

        def addAllFilterSetExcitationFilterLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Filter._op_addAllFilterSetExcitationFilterLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFilterSetExcitationFilterLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_addAllFilterSetExcitationFilterLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFilterSetExcitationFilterLinkSet(self, _r):
            return _M_omero.model.Filter._op_addAllFilterSetExcitationFilterLinkSet.end(self, _r)

        def removeFilterSetExcitationFilterLink(self, theTarget, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterSetExcitationFilterLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFilterSetExcitationFilterLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterSetExcitationFilterLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFilterSetExcitationFilterLink(self, _r):
            return _M_omero.model.Filter._op_removeFilterSetExcitationFilterLink.end(self, _r)

        def removeAllFilterSetExcitationFilterLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Filter._op_removeAllFilterSetExcitationFilterLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFilterSetExcitationFilterLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_removeAllFilterSetExcitationFilterLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFilterSetExcitationFilterLinkSet(self, _r):
            return _M_omero.model.Filter._op_removeAllFilterSetExcitationFilterLinkSet.end(self, _r)

        def clearExcitationFilterLink(self, _ctx=None):
            return _M_omero.model.Filter._op_clearExcitationFilterLink.invoke(self, ((), _ctx))

        def begin_clearExcitationFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_clearExcitationFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearExcitationFilterLink(self, _r):
            return _M_omero.model.Filter._op_clearExcitationFilterLink.end(self, _r)

        def reloadExcitationFilterLink(self, toCopy, _ctx=None):
            return _M_omero.model.Filter._op_reloadExcitationFilterLink.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadExcitationFilterLink(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_reloadExcitationFilterLink.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadExcitationFilterLink(self, _r):
            return _M_omero.model.Filter._op_reloadExcitationFilterLink.end(self, _r)

        def getExcitationFilterLinkCountPerOwner(self, _ctx=None):
            return _M_omero.model.Filter._op_getExcitationFilterLinkCountPerOwner.invoke(self, ((), _ctx))

        def begin_getExcitationFilterLinkCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getExcitationFilterLinkCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getExcitationFilterLinkCountPerOwner(self, _r):
            return _M_omero.model.Filter._op_getExcitationFilterLinkCountPerOwner.end(self, _r)

        def linkExcitationFilter(self, addition, _ctx=None):
            return _M_omero.model.Filter._op_linkExcitationFilter.invoke(self, ((addition, ), _ctx))

        def begin_linkExcitationFilter(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_linkExcitationFilter.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkExcitationFilter(self, _r):
            return _M_omero.model.Filter._op_linkExcitationFilter.end(self, _r)

        def addFilterSetExcitationFilterLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Filter._op_addFilterSetExcitationFilterLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addFilterSetExcitationFilterLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_addFilterSetExcitationFilterLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addFilterSetExcitationFilterLinkToBoth(self, _r):
            return _M_omero.model.Filter._op_addFilterSetExcitationFilterLinkToBoth.end(self, _r)

        def findFilterSetExcitationFilterLink(self, removal, _ctx=None):
            return _M_omero.model.Filter._op_findFilterSetExcitationFilterLink.invoke(self, ((removal, ), _ctx))

        def begin_findFilterSetExcitationFilterLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_findFilterSetExcitationFilterLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findFilterSetExcitationFilterLink(self, _r):
            return _M_omero.model.Filter._op_findFilterSetExcitationFilterLink.end(self, _r)

        def unlinkExcitationFilter(self, removal, _ctx=None):
            return _M_omero.model.Filter._op_unlinkExcitationFilter.invoke(self, ((removal, ), _ctx))

        def begin_unlinkExcitationFilter(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_unlinkExcitationFilter.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkExcitationFilter(self, _r):
            return _M_omero.model.Filter._op_unlinkExcitationFilter.end(self, _r)

        def removeFilterSetExcitationFilterLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterSetExcitationFilterLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeFilterSetExcitationFilterLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterSetExcitationFilterLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeFilterSetExcitationFilterLinkFromBoth(self, _r):
            return _M_omero.model.Filter._op_removeFilterSetExcitationFilterLinkFromBoth.end(self, _r)

        def linkedExcitationFilterList(self, _ctx=None):
            return _M_omero.model.Filter._op_linkedExcitationFilterList.invoke(self, ((), _ctx))

        def begin_linkedExcitationFilterList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_linkedExcitationFilterList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedExcitationFilterList(self, _r):
            return _M_omero.model.Filter._op_linkedExcitationFilterList.end(self, _r)

        def unloadEmissionFilterLink(self, _ctx=None):
            return _M_omero.model.Filter._op_unloadEmissionFilterLink.invoke(self, ((), _ctx))

        def begin_unloadEmissionFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_unloadEmissionFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadEmissionFilterLink(self, _r):
            return _M_omero.model.Filter._op_unloadEmissionFilterLink.end(self, _r)

        def sizeOfEmissionFilterLink(self, _ctx=None):
            return _M_omero.model.Filter._op_sizeOfEmissionFilterLink.invoke(self, ((), _ctx))

        def begin_sizeOfEmissionFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_sizeOfEmissionFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfEmissionFilterLink(self, _r):
            return _M_omero.model.Filter._op_sizeOfEmissionFilterLink.end(self, _r)

        def copyEmissionFilterLink(self, _ctx=None):
            return _M_omero.model.Filter._op_copyEmissionFilterLink.invoke(self, ((), _ctx))

        def begin_copyEmissionFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_copyEmissionFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyEmissionFilterLink(self, _r):
            return _M_omero.model.Filter._op_copyEmissionFilterLink.end(self, _r)

        def addFilterSetEmissionFilterLink(self, target, _ctx=None):
            return _M_omero.model.Filter._op_addFilterSetEmissionFilterLink.invoke(self, ((target, ), _ctx))

        def begin_addFilterSetEmissionFilterLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_addFilterSetEmissionFilterLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFilterSetEmissionFilterLink(self, _r):
            return _M_omero.model.Filter._op_addFilterSetEmissionFilterLink.end(self, _r)

        def addAllFilterSetEmissionFilterLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Filter._op_addAllFilterSetEmissionFilterLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFilterSetEmissionFilterLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_addAllFilterSetEmissionFilterLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFilterSetEmissionFilterLinkSet(self, _r):
            return _M_omero.model.Filter._op_addAllFilterSetEmissionFilterLinkSet.end(self, _r)

        def removeFilterSetEmissionFilterLink(self, theTarget, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterSetEmissionFilterLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFilterSetEmissionFilterLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterSetEmissionFilterLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFilterSetEmissionFilterLink(self, _r):
            return _M_omero.model.Filter._op_removeFilterSetEmissionFilterLink.end(self, _r)

        def removeAllFilterSetEmissionFilterLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Filter._op_removeAllFilterSetEmissionFilterLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFilterSetEmissionFilterLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_removeAllFilterSetEmissionFilterLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFilterSetEmissionFilterLinkSet(self, _r):
            return _M_omero.model.Filter._op_removeAllFilterSetEmissionFilterLinkSet.end(self, _r)

        def clearEmissionFilterLink(self, _ctx=None):
            return _M_omero.model.Filter._op_clearEmissionFilterLink.invoke(self, ((), _ctx))

        def begin_clearEmissionFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_clearEmissionFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearEmissionFilterLink(self, _r):
            return _M_omero.model.Filter._op_clearEmissionFilterLink.end(self, _r)

        def reloadEmissionFilterLink(self, toCopy, _ctx=None):
            return _M_omero.model.Filter._op_reloadEmissionFilterLink.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadEmissionFilterLink(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_reloadEmissionFilterLink.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadEmissionFilterLink(self, _r):
            return _M_omero.model.Filter._op_reloadEmissionFilterLink.end(self, _r)

        def getEmissionFilterLinkCountPerOwner(self, _ctx=None):
            return _M_omero.model.Filter._op_getEmissionFilterLinkCountPerOwner.invoke(self, ((), _ctx))

        def begin_getEmissionFilterLinkCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getEmissionFilterLinkCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEmissionFilterLinkCountPerOwner(self, _r):
            return _M_omero.model.Filter._op_getEmissionFilterLinkCountPerOwner.end(self, _r)

        def linkEmissionFilter(self, addition, _ctx=None):
            return _M_omero.model.Filter._op_linkEmissionFilter.invoke(self, ((addition, ), _ctx))

        def begin_linkEmissionFilter(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_linkEmissionFilter.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkEmissionFilter(self, _r):
            return _M_omero.model.Filter._op_linkEmissionFilter.end(self, _r)

        def addFilterSetEmissionFilterLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Filter._op_addFilterSetEmissionFilterLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addFilterSetEmissionFilterLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_addFilterSetEmissionFilterLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addFilterSetEmissionFilterLinkToBoth(self, _r):
            return _M_omero.model.Filter._op_addFilterSetEmissionFilterLinkToBoth.end(self, _r)

        def findFilterSetEmissionFilterLink(self, removal, _ctx=None):
            return _M_omero.model.Filter._op_findFilterSetEmissionFilterLink.invoke(self, ((removal, ), _ctx))

        def begin_findFilterSetEmissionFilterLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_findFilterSetEmissionFilterLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findFilterSetEmissionFilterLink(self, _r):
            return _M_omero.model.Filter._op_findFilterSetEmissionFilterLink.end(self, _r)

        def unlinkEmissionFilter(self, removal, _ctx=None):
            return _M_omero.model.Filter._op_unlinkEmissionFilter.invoke(self, ((removal, ), _ctx))

        def begin_unlinkEmissionFilter(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_unlinkEmissionFilter.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkEmissionFilter(self, _r):
            return _M_omero.model.Filter._op_unlinkEmissionFilter.end(self, _r)

        def removeFilterSetEmissionFilterLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterSetEmissionFilterLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeFilterSetEmissionFilterLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterSetEmissionFilterLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeFilterSetEmissionFilterLinkFromBoth(self, _r):
            return _M_omero.model.Filter._op_removeFilterSetEmissionFilterLinkFromBoth.end(self, _r)

        def linkedEmissionFilterList(self, _ctx=None):
            return _M_omero.model.Filter._op_linkedEmissionFilterList.invoke(self, ((), _ctx))

        def begin_linkedEmissionFilterList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_linkedEmissionFilterList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedEmissionFilterList(self, _r):
            return _M_omero.model.Filter._op_linkedEmissionFilterList.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Filter._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_omero.model.Filter._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Filter._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_omero.model.Filter._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Filter._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_omero.model.Filter._op_copyAnnotationLinks.end(self, _r)

        def addFilterAnnotationLink(self, target, _ctx=None):
            return _M_omero.model.Filter._op_addFilterAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addFilterAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_addFilterAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFilterAnnotationLink(self, _r):
            return _M_omero.model.Filter._op_addFilterAnnotationLink.end(self, _r)

        def addAllFilterAnnotationLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Filter._op_addAllFilterAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFilterAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_addAllFilterAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFilterAnnotationLinkSet(self, _r):
            return _M_omero.model.Filter._op_addAllFilterAnnotationLinkSet.end(self, _r)

        def removeFilterAnnotationLink(self, theTarget, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFilterAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFilterAnnotationLink(self, _r):
            return _M_omero.model.Filter._op_removeFilterAnnotationLink.end(self, _r)

        def removeAllFilterAnnotationLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Filter._op_removeAllFilterAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFilterAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_removeAllFilterAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFilterAnnotationLinkSet(self, _r):
            return _M_omero.model.Filter._op_removeAllFilterAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Filter._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_omero.model.Filter._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_omero.model.Filter._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_omero.model.Filter._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_omero.model.Filter._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_omero.model.Filter._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_omero.model.Filter._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_omero.model.Filter._op_linkAnnotation.end(self, _r)

        def addFilterAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Filter._op_addFilterAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addFilterAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_addFilterAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addFilterAnnotationLinkToBoth(self, _r):
            return _M_omero.model.Filter._op_addFilterAnnotationLinkToBoth.end(self, _r)

        def findFilterAnnotationLink(self, removal, _ctx=None):
            return _M_omero.model.Filter._op_findFilterAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findFilterAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_findFilterAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findFilterAnnotationLink(self, _r):
            return _M_omero.model.Filter._op_findFilterAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_omero.model.Filter._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_omero.model.Filter._op_unlinkAnnotation.end(self, _r)

        def removeFilterAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeFilterAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_removeFilterAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeFilterAnnotationLinkFromBoth(self, _r):
            return _M_omero.model.Filter._op_removeFilterAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_omero.model.Filter._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filter._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_omero.model.Filter._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_omero.model.FilterPrx.ice_checkedCast(proxy, '::omero::model::Filter', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_omero.model.FilterPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::omero::model::Filter'
        ice_staticId = staticmethod(ice_staticId)

    _M_omero.model._t_FilterPrx = IcePy.defineProxy('::omero::model::Filter', FilterPrx)

    _M_omero.model._t_Filter = IcePy.declareClass('::omero::model::Filter')

    _M_omero.model._t_Filter = IcePy.defineClass('::omero::model::Filter', Filter, -1, (), True, False, _M_omero.model._t_IObject, (), (
        ('_version', (), _M_omero._t_RInt, False, 0),
        ('_manufacturer', (), _M_omero._t_RString, False, 0),
        ('_model', (), _M_omero._t_RString, False, 0),
        ('_lotNumber', (), _M_omero._t_RString, False, 0),
        ('_serialNumber', (), _M_omero._t_RString, False, 0),
        ('_filterWheel', (), _M_omero._t_RString, False, 0),
        ('_type', (), _M_omero.model._t_FilterType, False, 0),
        ('_transmittanceRange', (), _M_omero.model._t_TransmittanceRange, False, 0),
        ('_instrument', (), _M_omero.model._t_Instrument, False, 0),
        ('_excitationFilterLinkSeq', (), _M_omero.model._t_FilterExcitationFilterLinkSeq, False, 0),
        ('_excitationFilterLinkLoaded', (), IcePy._t_bool, False, 0),
        ('_excitationFilterLinkCountPerOwner', (), _M_omero.sys._t_CountMap, False, 0),
        ('_emissionFilterLinkSeq', (), _M_omero.model._t_FilterEmissionFilterLinkSeq, False, 0),
        ('_emissionFilterLinkLoaded', (), IcePy._t_bool, False, 0),
        ('_emissionFilterLinkCountPerOwner', (), _M_omero.sys._t_CountMap, False, 0),
        ('_annotationLinksSeq', (), _M_omero.model._t_FilterAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_omero.sys._t_CountMap, False, 0)
    ))
    Filter._ice_type = _M_omero.model._t_Filter

    Filter._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RInt, False, 0), ())
    Filter._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RInt, False, 0),), (), None, ())
    Filter._op_getManufacturer = IcePy.Operation('getManufacturer', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RString, False, 0), ())
    Filter._op_setManufacturer = IcePy.Operation('setManufacturer', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RString, False, 0),), (), None, ())
    Filter._op_getModel = IcePy.Operation('getModel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RString, False, 0), ())
    Filter._op_setModel = IcePy.Operation('setModel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RString, False, 0),), (), None, ())
    Filter._op_getLotNumber = IcePy.Operation('getLotNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RString, False, 0), ())
    Filter._op_setLotNumber = IcePy.Operation('setLotNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RString, False, 0),), (), None, ())
    Filter._op_getSerialNumber = IcePy.Operation('getSerialNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RString, False, 0), ())
    Filter._op_setSerialNumber = IcePy.Operation('setSerialNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RString, False, 0),), (), None, ())
    Filter._op_getFilterWheel = IcePy.Operation('getFilterWheel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RString, False, 0), ())
    Filter._op_setFilterWheel = IcePy.Operation('setFilterWheel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RString, False, 0),), (), None, ())
    Filter._op_getType = IcePy.Operation('getType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FilterType, False, 0), ())
    Filter._op_setType = IcePy.Operation('setType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterType, False, 0),), (), None, ())
    Filter._op_getTransmittanceRange = IcePy.Operation('getTransmittanceRange', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_TransmittanceRange, False, 0), ())
    Filter._op_setTransmittanceRange = IcePy.Operation('setTransmittanceRange', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_TransmittanceRange, False, 0),), (), None, ())
    Filter._op_getInstrument = IcePy.Operation('getInstrument', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_Instrument, False, 0), ())
    Filter._op_setInstrument = IcePy.Operation('setInstrument', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Instrument, False, 0),), (), None, ())
    Filter._op_unloadExcitationFilterLink = IcePy.Operation('unloadExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Filter._op_sizeOfExcitationFilterLink = IcePy.Operation('sizeOfExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Filter._op_copyExcitationFilterLink = IcePy.Operation('copyExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FilterExcitationFilterLinkSeq, False, 0), ())
    Filter._op_addFilterSetExcitationFilterLink = IcePy.Operation('addFilterSetExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSetExcitationFilterLink, False, 0),), (), None, ())
    Filter._op_addAllFilterSetExcitationFilterLinkSet = IcePy.Operation('addAllFilterSetExcitationFilterLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterExcitationFilterLinkSeq, False, 0),), (), None, ())
    Filter._op_removeFilterSetExcitationFilterLink = IcePy.Operation('removeFilterSetExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSetExcitationFilterLink, False, 0),), (), None, ())
    Filter._op_removeAllFilterSetExcitationFilterLinkSet = IcePy.Operation('removeAllFilterSetExcitationFilterLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterExcitationFilterLinkSeq, False, 0),), (), None, ())
    Filter._op_clearExcitationFilterLink = IcePy.Operation('clearExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Filter._op_reloadExcitationFilterLink = IcePy.Operation('reloadExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Filter, False, 0),), (), None, ())
    Filter._op_getExcitationFilterLinkCountPerOwner = IcePy.Operation('getExcitationFilterLinkCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.sys._t_CountMap, False, 0), ())
    Filter._op_linkExcitationFilter = IcePy.Operation('linkExcitationFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSet, False, 0),), (), ((), _M_omero.model._t_FilterSetExcitationFilterLink, False, 0), ())
    Filter._op_addFilterSetExcitationFilterLinkToBoth = IcePy.Operation('addFilterSetExcitationFilterLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSetExcitationFilterLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Filter._op_findFilterSetExcitationFilterLink = IcePy.Operation('findFilterSetExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSet, False, 0),), (), ((), _M_omero.model._t_FilterExcitationFilterLinkSeq, False, 0), ())
    Filter._op_unlinkExcitationFilter = IcePy.Operation('unlinkExcitationFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSet, False, 0),), (), None, ())
    Filter._op_removeFilterSetExcitationFilterLinkFromBoth = IcePy.Operation('removeFilterSetExcitationFilterLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSetExcitationFilterLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Filter._op_linkedExcitationFilterList = IcePy.Operation('linkedExcitationFilterList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FilterLinkedExcitationFilterSeq, False, 0), ())
    Filter._op_unloadEmissionFilterLink = IcePy.Operation('unloadEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Filter._op_sizeOfEmissionFilterLink = IcePy.Operation('sizeOfEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Filter._op_copyEmissionFilterLink = IcePy.Operation('copyEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FilterEmissionFilterLinkSeq, False, 0), ())
    Filter._op_addFilterSetEmissionFilterLink = IcePy.Operation('addFilterSetEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSetEmissionFilterLink, False, 0),), (), None, ())
    Filter._op_addAllFilterSetEmissionFilterLinkSet = IcePy.Operation('addAllFilterSetEmissionFilterLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterEmissionFilterLinkSeq, False, 0),), (), None, ())
    Filter._op_removeFilterSetEmissionFilterLink = IcePy.Operation('removeFilterSetEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSetEmissionFilterLink, False, 0),), (), None, ())
    Filter._op_removeAllFilterSetEmissionFilterLinkSet = IcePy.Operation('removeAllFilterSetEmissionFilterLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterEmissionFilterLinkSeq, False, 0),), (), None, ())
    Filter._op_clearEmissionFilterLink = IcePy.Operation('clearEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Filter._op_reloadEmissionFilterLink = IcePy.Operation('reloadEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Filter, False, 0),), (), None, ())
    Filter._op_getEmissionFilterLinkCountPerOwner = IcePy.Operation('getEmissionFilterLinkCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.sys._t_CountMap, False, 0), ())
    Filter._op_linkEmissionFilter = IcePy.Operation('linkEmissionFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSet, False, 0),), (), ((), _M_omero.model._t_FilterSetEmissionFilterLink, False, 0), ())
    Filter._op_addFilterSetEmissionFilterLinkToBoth = IcePy.Operation('addFilterSetEmissionFilterLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSetEmissionFilterLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Filter._op_findFilterSetEmissionFilterLink = IcePy.Operation('findFilterSetEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSet, False, 0),), (), ((), _M_omero.model._t_FilterEmissionFilterLinkSeq, False, 0), ())
    Filter._op_unlinkEmissionFilter = IcePy.Operation('unlinkEmissionFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSet, False, 0),), (), None, ())
    Filter._op_removeFilterSetEmissionFilterLinkFromBoth = IcePy.Operation('removeFilterSetEmissionFilterLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterSetEmissionFilterLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Filter._op_linkedEmissionFilterList = IcePy.Operation('linkedEmissionFilterList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FilterLinkedEmissionFilterSeq, False, 0), ())
    Filter._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Filter._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Filter._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FilterAnnotationLinksSeq, False, 0), ())
    Filter._op_addFilterAnnotationLink = IcePy.Operation('addFilterAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterAnnotationLink, False, 0),), (), None, ())
    Filter._op_addAllFilterAnnotationLinkSet = IcePy.Operation('addAllFilterAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterAnnotationLinksSeq, False, 0),), (), None, ())
    Filter._op_removeFilterAnnotationLink = IcePy.Operation('removeFilterAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterAnnotationLink, False, 0),), (), None, ())
    Filter._op_removeAllFilterAnnotationLinkSet = IcePy.Operation('removeAllFilterAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterAnnotationLinksSeq, False, 0),), (), None, ())
    Filter._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Filter._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Filter, False, 0),), (), None, ())
    Filter._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.sys._t_CountMap, False, 0), ())
    Filter._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), ((), _M_omero.model._t_FilterAnnotationLink, False, 0), ())
    Filter._op_addFilterAnnotationLinkToBoth = IcePy.Operation('addFilterAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Filter._op_findFilterAnnotationLink = IcePy.Operation('findFilterAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), ((), _M_omero.model._t_FilterAnnotationLinksSeq, False, 0), ())
    Filter._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), None, ())
    Filter._op_removeFilterAnnotationLinkFromBoth = IcePy.Operation('removeFilterAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilterAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Filter._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FilterLinkedAnnotationSeq, False, 0), ())

    _M_omero.model.Filter = Filter
    del Filter

    _M_omero.model.FilterPrx = FilterPrx
    del FilterPrx

# End of module omero.model

__name__ = 'omero'

# End of module omero
