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

if 'ScreenPlateLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_ScreenPlateLink = IcePy.declareClass('::ode::model::ScreenPlateLink')
    _M_ode.model._t_ScreenPlateLinkPrx = IcePy.declareProxy('::ode::model::ScreenPlateLink')

if 'Plate' not in _M_ode.model.__dict__:
    _M_ode.model._t_Plate = IcePy.declareClass('::ode::model::Plate')
    _M_ode.model._t_PlatePrx = IcePy.declareProxy('::ode::model::Plate')

if 'Reagent' not in _M_ode.model.__dict__:
    _M_ode.model._t_Reagent = IcePy.declareClass('::ode::model::Reagent')
    _M_ode.model._t_ReagentPrx = IcePy.declareProxy('::ode::model::Reagent')

if 'ScreenAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_ScreenAnnotationLink = IcePy.declareClass('::ode::model::ScreenAnnotationLink')
    _M_ode.model._t_ScreenAnnotationLinkPrx = IcePy.declareProxy('::ode::model::ScreenAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_ScreenPlateLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ScreenPlateLinksSeq = IcePy.defineSequence('::ode::model::ScreenPlateLinksSeq', (), _M_ode.model._t_ScreenPlateLink)

if '_t_ScreenLinkedPlateSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ScreenLinkedPlateSeq = IcePy.defineSequence('::ode::model::ScreenLinkedPlateSeq', (), _M_ode.model._t_Plate)

if '_t_ScreenReagentsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ScreenReagentsSeq = IcePy.defineSequence('::ode::model::ScreenReagentsSeq', (), _M_ode.model._t_Reagent)

if '_t_ScreenAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ScreenAnnotationLinksSeq = IcePy.defineSequence('::ode::model::ScreenAnnotationLinksSeq', (), _M_ode.model._t_ScreenAnnotationLink)

if '_t_ScreenLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ScreenLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::ScreenLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Screen' not in _M_ode.model.__dict__:
    _M_ode.model.Screen = Ice.createTempClass()
    class Screen(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _type=None, _protocolIdentifier=None, _protocolDescription=None, _reagentSetIdentifier=None, _reagentSetDescription=None, _plateLinksSeq=None, _plateLinksLoaded=False, _plateLinksCountPerOwner=None, _reagentsSeq=None, _reagentsLoaded=False, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _name=None, _description=None):
            if Ice.getType(self) == _M_ode.model.Screen:
                raise RuntimeError('ode.model.Screen is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._type = _type
            self._protocolIdentifier = _protocolIdentifier
            self._protocolDescription = _protocolDescription
            self._reagentSetIdentifier = _reagentSetIdentifier
            self._reagentSetDescription = _reagentSetDescription
            self._plateLinksSeq = _plateLinksSeq
            self._plateLinksLoaded = _plateLinksLoaded
            self._plateLinksCountPerOwner = _plateLinksCountPerOwner
            self._reagentsSeq = _reagentsSeq
            self._reagentsLoaded = _reagentsLoaded
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._name = _name
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Screen')

        def ice_id(self, current=None):
            return '::ode::model::Screen'

        def ice_staticId():
            return '::ode::model::Screen'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getType(self, current=None):
            pass

        def setType(self, theType, current=None):
            pass

        def getProtocolIdentifier(self, current=None):
            pass

        def setProtocolIdentifier(self, theProtocolIdentifier, current=None):
            pass

        def getProtocolDescription(self, current=None):
            pass

        def setProtocolDescription(self, theProtocolDescription, current=None):
            pass

        def getReagentSetIdentifier(self, current=None):
            pass

        def setReagentSetIdentifier(self, theReagentSetIdentifier, current=None):
            pass

        def getReagentSetDescription(self, current=None):
            pass

        def setReagentSetDescription(self, theReagentSetDescription, current=None):
            pass

        def unloadPlateLinks(self, current=None):
            pass

        def sizeOfPlateLinks(self, current=None):
            pass

        def copyPlateLinks(self, current=None):
            pass

        def addScreenPlateLink(self, target, current=None):
            pass

        def addAllScreenPlateLinkSet(self, targets, current=None):
            pass

        def removeScreenPlateLink(self, theTarget, current=None):
            pass

        def removeAllScreenPlateLinkSet(self, targets, current=None):
            pass

        def clearPlateLinks(self, current=None):
            pass

        def reloadPlateLinks(self, toCopy, current=None):
            pass

        def getPlateLinksCountPerOwner(self, current=None):
            pass

        def linkPlate(self, addition, current=None):
            pass

        def addScreenPlateLinkToBoth(self, link, bothSides, current=None):
            pass

        def findScreenPlateLink(self, removal, current=None):
            pass

        def unlinkPlate(self, removal, current=None):
            pass

        def removeScreenPlateLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedPlateList(self, current=None):
            pass

        def unloadReagents(self, current=None):
            pass

        def sizeOfReagents(self, current=None):
            pass

        def copyReagents(self, current=None):
            pass

        def addReagent(self, target, current=None):
            pass

        def addAllReagentSet(self, targets, current=None):
            pass

        def removeReagent(self, theTarget, current=None):
            pass

        def removeAllReagentSet(self, targets, current=None):
            pass

        def clearReagents(self, current=None):
            pass

        def reloadReagents(self, toCopy, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addScreenAnnotationLink(self, target, current=None):
            pass

        def addAllScreenAnnotationLinkSet(self, targets, current=None):
            pass

        def removeScreenAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllScreenAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addScreenAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findScreenAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeScreenAnnotationLinkFromBoth(self, link, bothSides, current=None):
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
            return IcePy.stringify(self, _M_ode.model._t_Screen)

        __repr__ = __str__

    _M_ode.model.ScreenPrx = Ice.createTempClass()
    class ScreenPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Screen._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Screen._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Screen._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Screen._op_setVersion.end(self, _r)

        def getType(self, _ctx=None):
            return _M_ode.model.Screen._op_getType.invoke(self, ((), _ctx))

        def begin_getType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_getType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getType(self, _r):
            return _M_ode.model.Screen._op_getType.end(self, _r)

        def setType(self, theType, _ctx=None):
            return _M_ode.model.Screen._op_setType.invoke(self, ((theType, ), _ctx))

        def begin_setType(self, theType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_setType.begin(self, ((theType, ), _response, _ex, _sent, _ctx))

        def end_setType(self, _r):
            return _M_ode.model.Screen._op_setType.end(self, _r)

        def getProtocolIdentifier(self, _ctx=None):
            return _M_ode.model.Screen._op_getProtocolIdentifier.invoke(self, ((), _ctx))

        def begin_getProtocolIdentifier(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_getProtocolIdentifier.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getProtocolIdentifier(self, _r):
            return _M_ode.model.Screen._op_getProtocolIdentifier.end(self, _r)

        def setProtocolIdentifier(self, theProtocolIdentifier, _ctx=None):
            return _M_ode.model.Screen._op_setProtocolIdentifier.invoke(self, ((theProtocolIdentifier, ), _ctx))

        def begin_setProtocolIdentifier(self, theProtocolIdentifier, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_setProtocolIdentifier.begin(self, ((theProtocolIdentifier, ), _response, _ex, _sent, _ctx))

        def end_setProtocolIdentifier(self, _r):
            return _M_ode.model.Screen._op_setProtocolIdentifier.end(self, _r)

        def getProtocolDescription(self, _ctx=None):
            return _M_ode.model.Screen._op_getProtocolDescription.invoke(self, ((), _ctx))

        def begin_getProtocolDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_getProtocolDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getProtocolDescription(self, _r):
            return _M_ode.model.Screen._op_getProtocolDescription.end(self, _r)

        def setProtocolDescription(self, theProtocolDescription, _ctx=None):
            return _M_ode.model.Screen._op_setProtocolDescription.invoke(self, ((theProtocolDescription, ), _ctx))

        def begin_setProtocolDescription(self, theProtocolDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_setProtocolDescription.begin(self, ((theProtocolDescription, ), _response, _ex, _sent, _ctx))

        def end_setProtocolDescription(self, _r):
            return _M_ode.model.Screen._op_setProtocolDescription.end(self, _r)

        def getReagentSetIdentifier(self, _ctx=None):
            return _M_ode.model.Screen._op_getReagentSetIdentifier.invoke(self, ((), _ctx))

        def begin_getReagentSetIdentifier(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_getReagentSetIdentifier.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getReagentSetIdentifier(self, _r):
            return _M_ode.model.Screen._op_getReagentSetIdentifier.end(self, _r)

        def setReagentSetIdentifier(self, theReagentSetIdentifier, _ctx=None):
            return _M_ode.model.Screen._op_setReagentSetIdentifier.invoke(self, ((theReagentSetIdentifier, ), _ctx))

        def begin_setReagentSetIdentifier(self, theReagentSetIdentifier, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_setReagentSetIdentifier.begin(self, ((theReagentSetIdentifier, ), _response, _ex, _sent, _ctx))

        def end_setReagentSetIdentifier(self, _r):
            return _M_ode.model.Screen._op_setReagentSetIdentifier.end(self, _r)

        def getReagentSetDescription(self, _ctx=None):
            return _M_ode.model.Screen._op_getReagentSetDescription.invoke(self, ((), _ctx))

        def begin_getReagentSetDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_getReagentSetDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getReagentSetDescription(self, _r):
            return _M_ode.model.Screen._op_getReagentSetDescription.end(self, _r)

        def setReagentSetDescription(self, theReagentSetDescription, _ctx=None):
            return _M_ode.model.Screen._op_setReagentSetDescription.invoke(self, ((theReagentSetDescription, ), _ctx))

        def begin_setReagentSetDescription(self, theReagentSetDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_setReagentSetDescription.begin(self, ((theReagentSetDescription, ), _response, _ex, _sent, _ctx))

        def end_setReagentSetDescription(self, _r):
            return _M_ode.model.Screen._op_setReagentSetDescription.end(self, _r)

        def unloadPlateLinks(self, _ctx=None):
            return _M_ode.model.Screen._op_unloadPlateLinks.invoke(self, ((), _ctx))

        def begin_unloadPlateLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_unloadPlateLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadPlateLinks(self, _r):
            return _M_ode.model.Screen._op_unloadPlateLinks.end(self, _r)

        def sizeOfPlateLinks(self, _ctx=None):
            return _M_ode.model.Screen._op_sizeOfPlateLinks.invoke(self, ((), _ctx))

        def begin_sizeOfPlateLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_sizeOfPlateLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfPlateLinks(self, _r):
            return _M_ode.model.Screen._op_sizeOfPlateLinks.end(self, _r)

        def copyPlateLinks(self, _ctx=None):
            return _M_ode.model.Screen._op_copyPlateLinks.invoke(self, ((), _ctx))

        def begin_copyPlateLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_copyPlateLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyPlateLinks(self, _r):
            return _M_ode.model.Screen._op_copyPlateLinks.end(self, _r)

        def addScreenPlateLink(self, target, _ctx=None):
            return _M_ode.model.Screen._op_addScreenPlateLink.invoke(self, ((target, ), _ctx))

        def begin_addScreenPlateLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_addScreenPlateLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addScreenPlateLink(self, _r):
            return _M_ode.model.Screen._op_addScreenPlateLink.end(self, _r)

        def addAllScreenPlateLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Screen._op_addAllScreenPlateLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllScreenPlateLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_addAllScreenPlateLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllScreenPlateLinkSet(self, _r):
            return _M_ode.model.Screen._op_addAllScreenPlateLinkSet.end(self, _r)

        def removeScreenPlateLink(self, theTarget, _ctx=None):
            return _M_ode.model.Screen._op_removeScreenPlateLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeScreenPlateLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_removeScreenPlateLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeScreenPlateLink(self, _r):
            return _M_ode.model.Screen._op_removeScreenPlateLink.end(self, _r)

        def removeAllScreenPlateLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Screen._op_removeAllScreenPlateLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllScreenPlateLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_removeAllScreenPlateLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllScreenPlateLinkSet(self, _r):
            return _M_ode.model.Screen._op_removeAllScreenPlateLinkSet.end(self, _r)

        def clearPlateLinks(self, _ctx=None):
            return _M_ode.model.Screen._op_clearPlateLinks.invoke(self, ((), _ctx))

        def begin_clearPlateLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_clearPlateLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearPlateLinks(self, _r):
            return _M_ode.model.Screen._op_clearPlateLinks.end(self, _r)

        def reloadPlateLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Screen._op_reloadPlateLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadPlateLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_reloadPlateLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadPlateLinks(self, _r):
            return _M_ode.model.Screen._op_reloadPlateLinks.end(self, _r)

        def getPlateLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Screen._op_getPlateLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getPlateLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_getPlateLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPlateLinksCountPerOwner(self, _r):
            return _M_ode.model.Screen._op_getPlateLinksCountPerOwner.end(self, _r)

        def linkPlate(self, addition, _ctx=None):
            return _M_ode.model.Screen._op_linkPlate.invoke(self, ((addition, ), _ctx))

        def begin_linkPlate(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_linkPlate.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkPlate(self, _r):
            return _M_ode.model.Screen._op_linkPlate.end(self, _r)

        def addScreenPlateLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Screen._op_addScreenPlateLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addScreenPlateLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_addScreenPlateLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addScreenPlateLinkToBoth(self, _r):
            return _M_ode.model.Screen._op_addScreenPlateLinkToBoth.end(self, _r)

        def findScreenPlateLink(self, removal, _ctx=None):
            return _M_ode.model.Screen._op_findScreenPlateLink.invoke(self, ((removal, ), _ctx))

        def begin_findScreenPlateLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_findScreenPlateLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findScreenPlateLink(self, _r):
            return _M_ode.model.Screen._op_findScreenPlateLink.end(self, _r)

        def unlinkPlate(self, removal, _ctx=None):
            return _M_ode.model.Screen._op_unlinkPlate.invoke(self, ((removal, ), _ctx))

        def begin_unlinkPlate(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_unlinkPlate.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkPlate(self, _r):
            return _M_ode.model.Screen._op_unlinkPlate.end(self, _r)

        def removeScreenPlateLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Screen._op_removeScreenPlateLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeScreenPlateLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_removeScreenPlateLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeScreenPlateLinkFromBoth(self, _r):
            return _M_ode.model.Screen._op_removeScreenPlateLinkFromBoth.end(self, _r)

        def linkedPlateList(self, _ctx=None):
            return _M_ode.model.Screen._op_linkedPlateList.invoke(self, ((), _ctx))

        def begin_linkedPlateList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_linkedPlateList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedPlateList(self, _r):
            return _M_ode.model.Screen._op_linkedPlateList.end(self, _r)

        def unloadReagents(self, _ctx=None):
            return _M_ode.model.Screen._op_unloadReagents.invoke(self, ((), _ctx))

        def begin_unloadReagents(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_unloadReagents.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadReagents(self, _r):
            return _M_ode.model.Screen._op_unloadReagents.end(self, _r)

        def sizeOfReagents(self, _ctx=None):
            return _M_ode.model.Screen._op_sizeOfReagents.invoke(self, ((), _ctx))

        def begin_sizeOfReagents(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_sizeOfReagents.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfReagents(self, _r):
            return _M_ode.model.Screen._op_sizeOfReagents.end(self, _r)

        def copyReagents(self, _ctx=None):
            return _M_ode.model.Screen._op_copyReagents.invoke(self, ((), _ctx))

        def begin_copyReagents(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_copyReagents.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyReagents(self, _r):
            return _M_ode.model.Screen._op_copyReagents.end(self, _r)

        def addReagent(self, target, _ctx=None):
            return _M_ode.model.Screen._op_addReagent.invoke(self, ((target, ), _ctx))

        def begin_addReagent(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_addReagent.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addReagent(self, _r):
            return _M_ode.model.Screen._op_addReagent.end(self, _r)

        def addAllReagentSet(self, targets, _ctx=None):
            return _M_ode.model.Screen._op_addAllReagentSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllReagentSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_addAllReagentSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllReagentSet(self, _r):
            return _M_ode.model.Screen._op_addAllReagentSet.end(self, _r)

        def removeReagent(self, theTarget, _ctx=None):
            return _M_ode.model.Screen._op_removeReagent.invoke(self, ((theTarget, ), _ctx))

        def begin_removeReagent(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_removeReagent.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeReagent(self, _r):
            return _M_ode.model.Screen._op_removeReagent.end(self, _r)

        def removeAllReagentSet(self, targets, _ctx=None):
            return _M_ode.model.Screen._op_removeAllReagentSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllReagentSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_removeAllReagentSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllReagentSet(self, _r):
            return _M_ode.model.Screen._op_removeAllReagentSet.end(self, _r)

        def clearReagents(self, _ctx=None):
            return _M_ode.model.Screen._op_clearReagents.invoke(self, ((), _ctx))

        def begin_clearReagents(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_clearReagents.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearReagents(self, _r):
            return _M_ode.model.Screen._op_clearReagents.end(self, _r)

        def reloadReagents(self, toCopy, _ctx=None):
            return _M_ode.model.Screen._op_reloadReagents.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadReagents(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_reloadReagents.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadReagents(self, _r):
            return _M_ode.model.Screen._op_reloadReagents.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Screen._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Screen._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Screen._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Screen._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Screen._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Screen._op_copyAnnotationLinks.end(self, _r)

        def addScreenAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Screen._op_addScreenAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addScreenAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_addScreenAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addScreenAnnotationLink(self, _r):
            return _M_ode.model.Screen._op_addScreenAnnotationLink.end(self, _r)

        def addAllScreenAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Screen._op_addAllScreenAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllScreenAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_addAllScreenAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllScreenAnnotationLinkSet(self, _r):
            return _M_ode.model.Screen._op_addAllScreenAnnotationLinkSet.end(self, _r)

        def removeScreenAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Screen._op_removeScreenAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeScreenAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_removeScreenAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeScreenAnnotationLink(self, _r):
            return _M_ode.model.Screen._op_removeScreenAnnotationLink.end(self, _r)

        def removeAllScreenAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Screen._op_removeAllScreenAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllScreenAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_removeAllScreenAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllScreenAnnotationLinkSet(self, _r):
            return _M_ode.model.Screen._op_removeAllScreenAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Screen._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Screen._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Screen._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Screen._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Screen._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Screen._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Screen._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Screen._op_linkAnnotation.end(self, _r)

        def addScreenAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Screen._op_addScreenAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addScreenAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_addScreenAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addScreenAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Screen._op_addScreenAnnotationLinkToBoth.end(self, _r)

        def findScreenAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Screen._op_findScreenAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findScreenAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_findScreenAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findScreenAnnotationLink(self, _r):
            return _M_ode.model.Screen._op_findScreenAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Screen._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Screen._op_unlinkAnnotation.end(self, _r)

        def removeScreenAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Screen._op_removeScreenAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeScreenAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_removeScreenAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeScreenAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Screen._op_removeScreenAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Screen._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Screen._op_linkedAnnotationList.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.Screen._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.Screen._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.Screen._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.Screen._op_setName.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_ode.model.Screen._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_ode.model.Screen._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_ode.model.Screen._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Screen._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_ode.model.Screen._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ScreenPrx.ice_checkedCast(proxy, '::ode::model::Screen', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ScreenPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Screen'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ScreenPrx = IcePy.defineProxy('::ode::model::Screen', ScreenPrx)

    _M_ode.model._t_Screen = IcePy.declareClass('::ode::model::Screen')

    _M_ode.model._t_Screen = IcePy.defineClass('::ode::model::Screen', Screen, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_type', (), _M_ode._t_RString, False, 0),
        ('_protocolIdentifier', (), _M_ode._t_RString, False, 0),
        ('_protocolDescription', (), _M_ode._t_RString, False, 0),
        ('_reagentSetIdentifier', (), _M_ode._t_RString, False, 0),
        ('_reagentSetDescription', (), _M_ode._t_RString, False, 0),
        ('_plateLinksSeq', (), _M_ode.model._t_ScreenPlateLinksSeq, False, 0),
        ('_plateLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_plateLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_reagentsSeq', (), _M_ode.model._t_ScreenReagentsSeq, False, 0),
        ('_reagentsLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_ScreenAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0),
        ('_description', (), _M_ode._t_RString, False, 0)
    ))
    Screen._ice_type = _M_ode.model._t_Screen

    Screen._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Screen._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Screen._op_getType = IcePy.Operation('getType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Screen._op_setType = IcePy.Operation('setType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Screen._op_getProtocolIdentifier = IcePy.Operation('getProtocolIdentifier', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Screen._op_setProtocolIdentifier = IcePy.Operation('setProtocolIdentifier', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Screen._op_getProtocolDescription = IcePy.Operation('getProtocolDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Screen._op_setProtocolDescription = IcePy.Operation('setProtocolDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Screen._op_getReagentSetIdentifier = IcePy.Operation('getReagentSetIdentifier', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Screen._op_setReagentSetIdentifier = IcePy.Operation('setReagentSetIdentifier', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Screen._op_getReagentSetDescription = IcePy.Operation('getReagentSetDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Screen._op_setReagentSetDescription = IcePy.Operation('setReagentSetDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Screen._op_unloadPlateLinks = IcePy.Operation('unloadPlateLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Screen._op_sizeOfPlateLinks = IcePy.Operation('sizeOfPlateLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Screen._op_copyPlateLinks = IcePy.Operation('copyPlateLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ScreenPlateLinksSeq, False, 0), ())
    Screen._op_addScreenPlateLink = IcePy.Operation('addScreenPlateLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenPlateLink, False, 0),), (), None, ())
    Screen._op_addAllScreenPlateLinkSet = IcePy.Operation('addAllScreenPlateLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenPlateLinksSeq, False, 0),), (), None, ())
    Screen._op_removeScreenPlateLink = IcePy.Operation('removeScreenPlateLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenPlateLink, False, 0),), (), None, ())
    Screen._op_removeAllScreenPlateLinkSet = IcePy.Operation('removeAllScreenPlateLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenPlateLinksSeq, False, 0),), (), None, ())
    Screen._op_clearPlateLinks = IcePy.Operation('clearPlateLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Screen._op_reloadPlateLinks = IcePy.Operation('reloadPlateLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Screen, False, 0),), (), None, ())
    Screen._op_getPlateLinksCountPerOwner = IcePy.Operation('getPlateLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Screen._op_linkPlate = IcePy.Operation('linkPlate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Plate, False, 0),), (), ((), _M_ode.model._t_ScreenPlateLink, False, 0), ())
    Screen._op_addScreenPlateLinkToBoth = IcePy.Operation('addScreenPlateLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenPlateLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Screen._op_findScreenPlateLink = IcePy.Operation('findScreenPlateLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Plate, False, 0),), (), ((), _M_ode.model._t_ScreenPlateLinksSeq, False, 0), ())
    Screen._op_unlinkPlate = IcePy.Operation('unlinkPlate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Plate, False, 0),), (), None, ())
    Screen._op_removeScreenPlateLinkFromBoth = IcePy.Operation('removeScreenPlateLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenPlateLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Screen._op_linkedPlateList = IcePy.Operation('linkedPlateList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ScreenLinkedPlateSeq, False, 0), ())
    Screen._op_unloadReagents = IcePy.Operation('unloadReagents', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Screen._op_sizeOfReagents = IcePy.Operation('sizeOfReagents', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Screen._op_copyReagents = IcePy.Operation('copyReagents', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ScreenReagentsSeq, False, 0), ())
    Screen._op_addReagent = IcePy.Operation('addReagent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Reagent, False, 0),), (), None, ())
    Screen._op_addAllReagentSet = IcePy.Operation('addAllReagentSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenReagentsSeq, False, 0),), (), None, ())
    Screen._op_removeReagent = IcePy.Operation('removeReagent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Reagent, False, 0),), (), None, ())
    Screen._op_removeAllReagentSet = IcePy.Operation('removeAllReagentSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenReagentsSeq, False, 0),), (), None, ())
    Screen._op_clearReagents = IcePy.Operation('clearReagents', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Screen._op_reloadReagents = IcePy.Operation('reloadReagents', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Screen, False, 0),), (), None, ())
    Screen._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Screen._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Screen._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ScreenAnnotationLinksSeq, False, 0), ())
    Screen._op_addScreenAnnotationLink = IcePy.Operation('addScreenAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenAnnotationLink, False, 0),), (), None, ())
    Screen._op_addAllScreenAnnotationLinkSet = IcePy.Operation('addAllScreenAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenAnnotationLinksSeq, False, 0),), (), None, ())
    Screen._op_removeScreenAnnotationLink = IcePy.Operation('removeScreenAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenAnnotationLink, False, 0),), (), None, ())
    Screen._op_removeAllScreenAnnotationLinkSet = IcePy.Operation('removeAllScreenAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenAnnotationLinksSeq, False, 0),), (), None, ())
    Screen._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Screen._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Screen, False, 0),), (), None, ())
    Screen._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Screen._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ScreenAnnotationLink, False, 0), ())
    Screen._op_addScreenAnnotationLinkToBoth = IcePy.Operation('addScreenAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Screen._op_findScreenAnnotationLink = IcePy.Operation('findScreenAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ScreenAnnotationLinksSeq, False, 0), ())
    Screen._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Screen._op_removeScreenAnnotationLinkFromBoth = IcePy.Operation('removeScreenAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ScreenAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Screen._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ScreenLinkedAnnotationSeq, False, 0), ())
    Screen._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Screen._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Screen._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Screen._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Screen = Screen
    del Screen

    _M_ode.model.ScreenPrx = ScreenPrx
    del ScreenPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode