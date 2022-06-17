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

if 'Power' not in _M_ode.model.__dict__:
    _M_ode.model._t_Power = IcePy.declareClass('::ode::model::Power')
    _M_ode.model._t_PowerPrx = IcePy.declareProxy('::ode::model::Power')

if 'Instrument' not in _M_ode.model.__dict__:
    _M_ode.model._t_Instrument = IcePy.declareClass('::ode::model::Instrument')
    _M_ode.model._t_InstrumentPrx = IcePy.declareProxy('::ode::model::Instrument')

if 'LightSourceAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightSourceAnnotationLink = IcePy.declareClass('::ode::model::LightSourceAnnotationLink')
    _M_ode.model._t_LightSourceAnnotationLinkPrx = IcePy.declareProxy('::ode::model::LightSourceAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_LightSourceAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightSourceAnnotationLinksSeq = IcePy.defineSequence('::ode::model::LightSourceAnnotationLinksSeq', (), _M_ode.model._t_LightSourceAnnotationLink)

if '_t_LightSourceLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightSourceLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::LightSourceLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'LightSource' not in _M_ode.model.__dict__:
    _M_ode.model.LightSource = Ice.createTempClass()
    class LightSource(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _manufacturer=None, _model=None, _power=None, _lotNumber=None, _serialNumber=None, _instrument=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.LightSource:
                raise RuntimeError('ode.model.LightSource is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._manufacturer = _manufacturer
            self._model = _model
            self._power = _power
            self._lotNumber = _lotNumber
            self._serialNumber = _serialNumber
            self._instrument = _instrument
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::LightSource')

        def ice_id(self, current=None):
            return '::ode::model::LightSource'

        def ice_staticId():
            return '::ode::model::LightSource'
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

        def getPower(self, current=None):
            pass

        def setPower(self, thePower, current=None):
            pass

        def getLotNumber(self, current=None):
            pass

        def setLotNumber(self, theLotNumber, current=None):
            pass

        def getSerialNumber(self, current=None):
            pass

        def setSerialNumber(self, theSerialNumber, current=None):
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

        def addLightSourceAnnotationLink(self, target, current=None):
            pass

        def addAllLightSourceAnnotationLinkSet(self, targets, current=None):
            pass

        def removeLightSourceAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllLightSourceAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addLightSourceAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findLightSourceAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeLightSourceAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_LightSource)

        __repr__ = __str__

    _M_ode.model.LightSourcePrx = Ice.createTempClass()
    class LightSourcePrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.LightSource._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.LightSource._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.LightSource._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.LightSource._op_setVersion.end(self, _r)

        def getManufacturer(self, _ctx=None):
            return _M_ode.model.LightSource._op_getManufacturer.invoke(self, ((), _ctx))

        def begin_getManufacturer(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_getManufacturer.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getManufacturer(self, _r):
            return _M_ode.model.LightSource._op_getManufacturer.end(self, _r)

        def setManufacturer(self, theManufacturer, _ctx=None):
            return _M_ode.model.LightSource._op_setManufacturer.invoke(self, ((theManufacturer, ), _ctx))

        def begin_setManufacturer(self, theManufacturer, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_setManufacturer.begin(self, ((theManufacturer, ), _response, _ex, _sent, _ctx))

        def end_setManufacturer(self, _r):
            return _M_ode.model.LightSource._op_setManufacturer.end(self, _r)

        def getModel(self, _ctx=None):
            return _M_ode.model.LightSource._op_getModel.invoke(self, ((), _ctx))

        def begin_getModel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_getModel.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getModel(self, _r):
            return _M_ode.model.LightSource._op_getModel.end(self, _r)

        def setModel(self, theModel, _ctx=None):
            return _M_ode.model.LightSource._op_setModel.invoke(self, ((theModel, ), _ctx))

        def begin_setModel(self, theModel, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_setModel.begin(self, ((theModel, ), _response, _ex, _sent, _ctx))

        def end_setModel(self, _r):
            return _M_ode.model.LightSource._op_setModel.end(self, _r)

        def getPower(self, _ctx=None):
            return _M_ode.model.LightSource._op_getPower.invoke(self, ((), _ctx))

        def begin_getPower(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_getPower.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPower(self, _r):
            return _M_ode.model.LightSource._op_getPower.end(self, _r)

        def setPower(self, thePower, _ctx=None):
            return _M_ode.model.LightSource._op_setPower.invoke(self, ((thePower, ), _ctx))

        def begin_setPower(self, thePower, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_setPower.begin(self, ((thePower, ), _response, _ex, _sent, _ctx))

        def end_setPower(self, _r):
            return _M_ode.model.LightSource._op_setPower.end(self, _r)

        def getLotNumber(self, _ctx=None):
            return _M_ode.model.LightSource._op_getLotNumber.invoke(self, ((), _ctx))

        def begin_getLotNumber(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_getLotNumber.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLotNumber(self, _r):
            return _M_ode.model.LightSource._op_getLotNumber.end(self, _r)

        def setLotNumber(self, theLotNumber, _ctx=None):
            return _M_ode.model.LightSource._op_setLotNumber.invoke(self, ((theLotNumber, ), _ctx))

        def begin_setLotNumber(self, theLotNumber, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_setLotNumber.begin(self, ((theLotNumber, ), _response, _ex, _sent, _ctx))

        def end_setLotNumber(self, _r):
            return _M_ode.model.LightSource._op_setLotNumber.end(self, _r)

        def getSerialNumber(self, _ctx=None):
            return _M_ode.model.LightSource._op_getSerialNumber.invoke(self, ((), _ctx))

        def begin_getSerialNumber(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_getSerialNumber.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSerialNumber(self, _r):
            return _M_ode.model.LightSource._op_getSerialNumber.end(self, _r)

        def setSerialNumber(self, theSerialNumber, _ctx=None):
            return _M_ode.model.LightSource._op_setSerialNumber.invoke(self, ((theSerialNumber, ), _ctx))

        def begin_setSerialNumber(self, theSerialNumber, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_setSerialNumber.begin(self, ((theSerialNumber, ), _response, _ex, _sent, _ctx))

        def end_setSerialNumber(self, _r):
            return _M_ode.model.LightSource._op_setSerialNumber.end(self, _r)

        def getInstrument(self, _ctx=None):
            return _M_ode.model.LightSource._op_getInstrument.invoke(self, ((), _ctx))

        def begin_getInstrument(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_getInstrument.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getInstrument(self, _r):
            return _M_ode.model.LightSource._op_getInstrument.end(self, _r)

        def setInstrument(self, theInstrument, _ctx=None):
            return _M_ode.model.LightSource._op_setInstrument.invoke(self, ((theInstrument, ), _ctx))

        def begin_setInstrument(self, theInstrument, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_setInstrument.begin(self, ((theInstrument, ), _response, _ex, _sent, _ctx))

        def end_setInstrument(self, _r):
            return _M_ode.model.LightSource._op_setInstrument.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.LightSource._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.LightSource._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.LightSource._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.LightSource._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.LightSource._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.LightSource._op_copyAnnotationLinks.end(self, _r)

        def addLightSourceAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.LightSource._op_addLightSourceAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addLightSourceAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_addLightSourceAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addLightSourceAnnotationLink(self, _r):
            return _M_ode.model.LightSource._op_addLightSourceAnnotationLink.end(self, _r)

        def addAllLightSourceAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.LightSource._op_addAllLightSourceAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllLightSourceAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_addAllLightSourceAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllLightSourceAnnotationLinkSet(self, _r):
            return _M_ode.model.LightSource._op_addAllLightSourceAnnotationLinkSet.end(self, _r)

        def removeLightSourceAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.LightSource._op_removeLightSourceAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeLightSourceAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_removeLightSourceAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeLightSourceAnnotationLink(self, _r):
            return _M_ode.model.LightSource._op_removeLightSourceAnnotationLink.end(self, _r)

        def removeAllLightSourceAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.LightSource._op_removeAllLightSourceAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllLightSourceAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_removeAllLightSourceAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllLightSourceAnnotationLinkSet(self, _r):
            return _M_ode.model.LightSource._op_removeAllLightSourceAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.LightSource._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.LightSource._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.LightSource._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.LightSource._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.LightSource._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.LightSource._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.LightSource._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.LightSource._op_linkAnnotation.end(self, _r)

        def addLightSourceAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.LightSource._op_addLightSourceAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addLightSourceAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_addLightSourceAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addLightSourceAnnotationLinkToBoth(self, _r):
            return _M_ode.model.LightSource._op_addLightSourceAnnotationLinkToBoth.end(self, _r)

        def findLightSourceAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.LightSource._op_findLightSourceAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findLightSourceAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_findLightSourceAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findLightSourceAnnotationLink(self, _r):
            return _M_ode.model.LightSource._op_findLightSourceAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.LightSource._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.LightSource._op_unlinkAnnotation.end(self, _r)

        def removeLightSourceAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.LightSource._op_removeLightSourceAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeLightSourceAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_removeLightSourceAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeLightSourceAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.LightSource._op_removeLightSourceAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.LightSource._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightSource._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.LightSource._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.LightSourcePrx.ice_checkedCast(proxy, '::ode::model::LightSource', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.LightSourcePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::LightSource'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_LightSourcePrx = IcePy.defineProxy('::ode::model::LightSource', LightSourcePrx)

    _M_ode.model._t_LightSource = IcePy.declareClass('::ode::model::LightSource')

    _M_ode.model._t_LightSource = IcePy.defineClass('::ode::model::LightSource', LightSource, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_manufacturer', (), _M_ode._t_RString, False, 0),
        ('_model', (), _M_ode._t_RString, False, 0),
        ('_power', (), _M_ode.model._t_Power, False, 0),
        ('_lotNumber', (), _M_ode._t_RString, False, 0),
        ('_serialNumber', (), _M_ode._t_RString, False, 0),
        ('_instrument', (), _M_ode.model._t_Instrument, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_LightSourceAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    LightSource._ice_type = _M_ode.model._t_LightSource

    LightSource._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    LightSource._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    LightSource._op_getManufacturer = IcePy.Operation('getManufacturer', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    LightSource._op_setManufacturer = IcePy.Operation('setManufacturer', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    LightSource._op_getModel = IcePy.Operation('getModel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    LightSource._op_setModel = IcePy.Operation('setModel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    LightSource._op_getPower = IcePy.Operation('getPower', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Power, False, 0), ())
    LightSource._op_setPower = IcePy.Operation('setPower', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Power, False, 0),), (), None, ())
    LightSource._op_getLotNumber = IcePy.Operation('getLotNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    LightSource._op_setLotNumber = IcePy.Operation('setLotNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    LightSource._op_getSerialNumber = IcePy.Operation('getSerialNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    LightSource._op_setSerialNumber = IcePy.Operation('setSerialNumber', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    LightSource._op_getInstrument = IcePy.Operation('getInstrument', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Instrument, False, 0), ())
    LightSource._op_setInstrument = IcePy.Operation('setInstrument', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Instrument, False, 0),), (), None, ())
    LightSource._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    LightSource._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    LightSource._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightSourceAnnotationLinksSeq, False, 0), ())
    LightSource._op_addLightSourceAnnotationLink = IcePy.Operation('addLightSourceAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSourceAnnotationLink, False, 0),), (), None, ())
    LightSource._op_addAllLightSourceAnnotationLinkSet = IcePy.Operation('addAllLightSourceAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSourceAnnotationLinksSeq, False, 0),), (), None, ())
    LightSource._op_removeLightSourceAnnotationLink = IcePy.Operation('removeLightSourceAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSourceAnnotationLink, False, 0),), (), None, ())
    LightSource._op_removeAllLightSourceAnnotationLinkSet = IcePy.Operation('removeAllLightSourceAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSourceAnnotationLinksSeq, False, 0),), (), None, ())
    LightSource._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    LightSource._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSource, False, 0),), (), None, ())
    LightSource._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    LightSource._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_LightSourceAnnotationLink, False, 0), ())
    LightSource._op_addLightSourceAnnotationLinkToBoth = IcePy.Operation('addLightSourceAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSourceAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    LightSource._op_findLightSourceAnnotationLink = IcePy.Operation('findLightSourceAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_LightSourceAnnotationLinksSeq, False, 0), ())
    LightSource._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    LightSource._op_removeLightSourceAnnotationLinkFromBoth = IcePy.Operation('removeLightSourceAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightSourceAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    LightSource._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightSourceLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.LightSource = LightSource
    del LightSource

    _M_ode.model.LightSourcePrx = LightSourcePrx
    del LightSourcePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
