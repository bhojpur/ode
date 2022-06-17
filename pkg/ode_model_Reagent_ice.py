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

if 'Screen' not in _M_ode.model.__dict__:
    _M_ode.model._t_Screen = IcePy.declareClass('::ode::model::Screen')
    _M_ode.model._t_ScreenPrx = IcePy.declareProxy('::ode::model::Screen')

if 'WellReagentLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_WellReagentLink = IcePy.declareClass('::ode::model::WellReagentLink')
    _M_ode.model._t_WellReagentLinkPrx = IcePy.declareProxy('::ode::model::WellReagentLink')

if 'Well' not in _M_ode.model.__dict__:
    _M_ode.model._t_Well = IcePy.declareClass('::ode::model::Well')
    _M_ode.model._t_WellPrx = IcePy.declareProxy('::ode::model::Well')

if 'ReagentAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_ReagentAnnotationLink = IcePy.declareClass('::ode::model::ReagentAnnotationLink')
    _M_ode.model._t_ReagentAnnotationLinkPrx = IcePy.declareProxy('::ode::model::ReagentAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_ReagentWellLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ReagentWellLinksSeq = IcePy.defineSequence('::ode::model::ReagentWellLinksSeq', (), _M_ode.model._t_WellReagentLink)

if '_t_ReagentLinkedWellSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ReagentLinkedWellSeq = IcePy.defineSequence('::ode::model::ReagentLinkedWellSeq', (), _M_ode.model._t_Well)

if '_t_ReagentAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ReagentAnnotationLinksSeq = IcePy.defineSequence('::ode::model::ReagentAnnotationLinksSeq', (), _M_ode.model._t_ReagentAnnotationLink)

if '_t_ReagentLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ReagentLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::ReagentLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Reagent' not in _M_ode.model.__dict__:
    _M_ode.model.Reagent = Ice.createTempClass()
    class Reagent(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _name=None, _reagentIdentifier=None, _screen=None, _wellLinksSeq=None, _wellLinksLoaded=False, _wellLinksCountPerOwner=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _description=None):
            if Ice.getType(self) == _M_ode.model.Reagent:
                raise RuntimeError('ode.model.Reagent is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._name = _name
            self._reagentIdentifier = _reagentIdentifier
            self._screen = _screen
            self._wellLinksSeq = _wellLinksSeq
            self._wellLinksLoaded = _wellLinksLoaded
            self._wellLinksCountPerOwner = _wellLinksCountPerOwner
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Reagent')

        def ice_id(self, current=None):
            return '::ode::model::Reagent'

        def ice_staticId():
            return '::ode::model::Reagent'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getName(self, current=None):
            pass

        def setName(self, theName, current=None):
            pass

        def getReagentIdentifier(self, current=None):
            pass

        def setReagentIdentifier(self, theReagentIdentifier, current=None):
            pass

        def getScreen(self, current=None):
            pass

        def setScreen(self, theScreen, current=None):
            pass

        def unloadWellLinks(self, current=None):
            pass

        def sizeOfWellLinks(self, current=None):
            pass

        def copyWellLinks(self, current=None):
            pass

        def addWellReagentLink(self, target, current=None):
            pass

        def addAllWellReagentLinkSet(self, targets, current=None):
            pass

        def removeWellReagentLink(self, theTarget, current=None):
            pass

        def removeAllWellReagentLinkSet(self, targets, current=None):
            pass

        def clearWellLinks(self, current=None):
            pass

        def reloadWellLinks(self, toCopy, current=None):
            pass

        def getWellLinksCountPerOwner(self, current=None):
            pass

        def linkWell(self, addition, current=None):
            pass

        def addWellReagentLinkToBoth(self, link, bothSides, current=None):
            pass

        def findWellReagentLink(self, removal, current=None):
            pass

        def unlinkWell(self, removal, current=None):
            pass

        def removeWellReagentLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedWellList(self, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addReagentAnnotationLink(self, target, current=None):
            pass

        def addAllReagentAnnotationLinkSet(self, targets, current=None):
            pass

        def removeReagentAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllReagentAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addReagentAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findReagentAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeReagentAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def getDescription(self, current=None):
            pass

        def setDescription(self, theDescription, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Reagent)

        __repr__ = __str__

    _M_ode.model.ReagentPrx = Ice.createTempClass()
    class ReagentPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Reagent._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Reagent._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Reagent._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Reagent._op_setVersion.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.Reagent._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.Reagent._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.Reagent._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.Reagent._op_setName.end(self, _r)

        def getReagentIdentifier(self, _ctx=None):
            return _M_ode.model.Reagent._op_getReagentIdentifier.invoke(self, ((), _ctx))

        def begin_getReagentIdentifier(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_getReagentIdentifier.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getReagentIdentifier(self, _r):
            return _M_ode.model.Reagent._op_getReagentIdentifier.end(self, _r)

        def setReagentIdentifier(self, theReagentIdentifier, _ctx=None):
            return _M_ode.model.Reagent._op_setReagentIdentifier.invoke(self, ((theReagentIdentifier, ), _ctx))

        def begin_setReagentIdentifier(self, theReagentIdentifier, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_setReagentIdentifier.begin(self, ((theReagentIdentifier, ), _response, _ex, _sent, _ctx))

        def end_setReagentIdentifier(self, _r):
            return _M_ode.model.Reagent._op_setReagentIdentifier.end(self, _r)

        def getScreen(self, _ctx=None):
            return _M_ode.model.Reagent._op_getScreen.invoke(self, ((), _ctx))

        def begin_getScreen(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_getScreen.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getScreen(self, _r):
            return _M_ode.model.Reagent._op_getScreen.end(self, _r)

        def setScreen(self, theScreen, _ctx=None):
            return _M_ode.model.Reagent._op_setScreen.invoke(self, ((theScreen, ), _ctx))

        def begin_setScreen(self, theScreen, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_setScreen.begin(self, ((theScreen, ), _response, _ex, _sent, _ctx))

        def end_setScreen(self, _r):
            return _M_ode.model.Reagent._op_setScreen.end(self, _r)

        def unloadWellLinks(self, _ctx=None):
            return _M_ode.model.Reagent._op_unloadWellLinks.invoke(self, ((), _ctx))

        def begin_unloadWellLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_unloadWellLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadWellLinks(self, _r):
            return _M_ode.model.Reagent._op_unloadWellLinks.end(self, _r)

        def sizeOfWellLinks(self, _ctx=None):
            return _M_ode.model.Reagent._op_sizeOfWellLinks.invoke(self, ((), _ctx))

        def begin_sizeOfWellLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_sizeOfWellLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfWellLinks(self, _r):
            return _M_ode.model.Reagent._op_sizeOfWellLinks.end(self, _r)

        def copyWellLinks(self, _ctx=None):
            return _M_ode.model.Reagent._op_copyWellLinks.invoke(self, ((), _ctx))

        def begin_copyWellLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_copyWellLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyWellLinks(self, _r):
            return _M_ode.model.Reagent._op_copyWellLinks.end(self, _r)

        def addWellReagentLink(self, target, _ctx=None):
            return _M_ode.model.Reagent._op_addWellReagentLink.invoke(self, ((target, ), _ctx))

        def begin_addWellReagentLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_addWellReagentLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addWellReagentLink(self, _r):
            return _M_ode.model.Reagent._op_addWellReagentLink.end(self, _r)

        def addAllWellReagentLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Reagent._op_addAllWellReagentLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllWellReagentLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_addAllWellReagentLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllWellReagentLinkSet(self, _r):
            return _M_ode.model.Reagent._op_addAllWellReagentLinkSet.end(self, _r)

        def removeWellReagentLink(self, theTarget, _ctx=None):
            return _M_ode.model.Reagent._op_removeWellReagentLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeWellReagentLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_removeWellReagentLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeWellReagentLink(self, _r):
            return _M_ode.model.Reagent._op_removeWellReagentLink.end(self, _r)

        def removeAllWellReagentLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Reagent._op_removeAllWellReagentLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllWellReagentLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_removeAllWellReagentLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllWellReagentLinkSet(self, _r):
            return _M_ode.model.Reagent._op_removeAllWellReagentLinkSet.end(self, _r)

        def clearWellLinks(self, _ctx=None):
            return _M_ode.model.Reagent._op_clearWellLinks.invoke(self, ((), _ctx))

        def begin_clearWellLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_clearWellLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearWellLinks(self, _r):
            return _M_ode.model.Reagent._op_clearWellLinks.end(self, _r)

        def reloadWellLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Reagent._op_reloadWellLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadWellLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_reloadWellLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadWellLinks(self, _r):
            return _M_ode.model.Reagent._op_reloadWellLinks.end(self, _r)

        def getWellLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Reagent._op_getWellLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getWellLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_getWellLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getWellLinksCountPerOwner(self, _r):
            return _M_ode.model.Reagent._op_getWellLinksCountPerOwner.end(self, _r)

        def linkWell(self, addition, _ctx=None):
            return _M_ode.model.Reagent._op_linkWell.invoke(self, ((addition, ), _ctx))

        def begin_linkWell(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_linkWell.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkWell(self, _r):
            return _M_ode.model.Reagent._op_linkWell.end(self, _r)

        def addWellReagentLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Reagent._op_addWellReagentLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addWellReagentLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_addWellReagentLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addWellReagentLinkToBoth(self, _r):
            return _M_ode.model.Reagent._op_addWellReagentLinkToBoth.end(self, _r)

        def findWellReagentLink(self, removal, _ctx=None):
            return _M_ode.model.Reagent._op_findWellReagentLink.invoke(self, ((removal, ), _ctx))

        def begin_findWellReagentLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_findWellReagentLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findWellReagentLink(self, _r):
            return _M_ode.model.Reagent._op_findWellReagentLink.end(self, _r)

        def unlinkWell(self, removal, _ctx=None):
            return _M_ode.model.Reagent._op_unlinkWell.invoke(self, ((removal, ), _ctx))

        def begin_unlinkWell(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_unlinkWell.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkWell(self, _r):
            return _M_ode.model.Reagent._op_unlinkWell.end(self, _r)

        def removeWellReagentLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Reagent._op_removeWellReagentLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeWellReagentLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_removeWellReagentLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeWellReagentLinkFromBoth(self, _r):
            return _M_ode.model.Reagent._op_removeWellReagentLinkFromBoth.end(self, _r)

        def linkedWellList(self, _ctx=None):
            return _M_ode.model.Reagent._op_linkedWellList.invoke(self, ((), _ctx))

        def begin_linkedWellList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_linkedWellList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedWellList(self, _r):
            return _M_ode.model.Reagent._op_linkedWellList.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Reagent._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Reagent._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Reagent._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Reagent._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Reagent._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Reagent._op_copyAnnotationLinks.end(self, _r)

        def addReagentAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Reagent._op_addReagentAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addReagentAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_addReagentAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addReagentAnnotationLink(self, _r):
            return _M_ode.model.Reagent._op_addReagentAnnotationLink.end(self, _r)

        def addAllReagentAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Reagent._op_addAllReagentAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllReagentAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_addAllReagentAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllReagentAnnotationLinkSet(self, _r):
            return _M_ode.model.Reagent._op_addAllReagentAnnotationLinkSet.end(self, _r)

        def removeReagentAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Reagent._op_removeReagentAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeReagentAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_removeReagentAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeReagentAnnotationLink(self, _r):
            return _M_ode.model.Reagent._op_removeReagentAnnotationLink.end(self, _r)

        def removeAllReagentAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Reagent._op_removeAllReagentAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllReagentAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_removeAllReagentAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllReagentAnnotationLinkSet(self, _r):
            return _M_ode.model.Reagent._op_removeAllReagentAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Reagent._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Reagent._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Reagent._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Reagent._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Reagent._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Reagent._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Reagent._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Reagent._op_linkAnnotation.end(self, _r)

        def addReagentAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Reagent._op_addReagentAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addReagentAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_addReagentAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addReagentAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Reagent._op_addReagentAnnotationLinkToBoth.end(self, _r)

        def findReagentAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Reagent._op_findReagentAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findReagentAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_findReagentAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findReagentAnnotationLink(self, _r):
            return _M_ode.model.Reagent._op_findReagentAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Reagent._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Reagent._op_unlinkAnnotation.end(self, _r)

        def removeReagentAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Reagent._op_removeReagentAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeReagentAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_removeReagentAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeReagentAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Reagent._op_removeReagentAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Reagent._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Reagent._op_linkedAnnotationList.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_ode.model.Reagent._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_ode.model.Reagent._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_ode.model.Reagent._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Reagent._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_ode.model.Reagent._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ReagentPrx.ice_checkedCast(proxy, '::ode::model::Reagent', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ReagentPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Reagent'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ReagentPrx = IcePy.defineProxy('::ode::model::Reagent', ReagentPrx)

    _M_ode.model._t_Reagent = IcePy.declareClass('::ode::model::Reagent')

    _M_ode.model._t_Reagent = IcePy.defineClass('::ode::model::Reagent', Reagent, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0),
        ('_reagentIdentifier', (), _M_ode._t_RString, False, 0),
        ('_screen', (), _M_ode.model._t_Screen, False, 0),
        ('_wellLinksSeq', (), _M_ode.model._t_ReagentWellLinksSeq, False, 0),
        ('_wellLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_wellLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_ReagentAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_description', (), _M_ode._t_RString, False, 0)
    ))
    Reagent._ice_type = _M_ode.model._t_Reagent

    Reagent._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Reagent._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Reagent._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Reagent._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Reagent._op_getReagentIdentifier = IcePy.Operation('getReagentIdentifier', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Reagent._op_setReagentIdentifier = IcePy.Operation('setReagentIdentifier', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Reagent._op_getScreen = IcePy.Operation('getScreen', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Screen, False, 0), ())
    Reagent._op_setScreen = IcePy.Operation('setScreen', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Screen, False, 0),), (), None, ())
    Reagent._op_unloadWellLinks = IcePy.Operation('unloadWellLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Reagent._op_sizeOfWellLinks = IcePy.Operation('sizeOfWellLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Reagent._op_copyWellLinks = IcePy.Operation('copyWellLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ReagentWellLinksSeq, False, 0), ())
    Reagent._op_addWellReagentLink = IcePy.Operation('addWellReagentLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellReagentLink, False, 0),), (), None, ())
    Reagent._op_addAllWellReagentLinkSet = IcePy.Operation('addAllWellReagentLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ReagentWellLinksSeq, False, 0),), (), None, ())
    Reagent._op_removeWellReagentLink = IcePy.Operation('removeWellReagentLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellReagentLink, False, 0),), (), None, ())
    Reagent._op_removeAllWellReagentLinkSet = IcePy.Operation('removeAllWellReagentLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ReagentWellLinksSeq, False, 0),), (), None, ())
    Reagent._op_clearWellLinks = IcePy.Operation('clearWellLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Reagent._op_reloadWellLinks = IcePy.Operation('reloadWellLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Reagent, False, 0),), (), None, ())
    Reagent._op_getWellLinksCountPerOwner = IcePy.Operation('getWellLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Reagent._op_linkWell = IcePy.Operation('linkWell', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Well, False, 0),), (), ((), _M_ode.model._t_WellReagentLink, False, 0), ())
    Reagent._op_addWellReagentLinkToBoth = IcePy.Operation('addWellReagentLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellReagentLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Reagent._op_findWellReagentLink = IcePy.Operation('findWellReagentLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Well, False, 0),), (), ((), _M_ode.model._t_ReagentWellLinksSeq, False, 0), ())
    Reagent._op_unlinkWell = IcePy.Operation('unlinkWell', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Well, False, 0),), (), None, ())
    Reagent._op_removeWellReagentLinkFromBoth = IcePy.Operation('removeWellReagentLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_WellReagentLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Reagent._op_linkedWellList = IcePy.Operation('linkedWellList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ReagentLinkedWellSeq, False, 0), ())
    Reagent._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Reagent._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Reagent._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ReagentAnnotationLinksSeq, False, 0), ())
    Reagent._op_addReagentAnnotationLink = IcePy.Operation('addReagentAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ReagentAnnotationLink, False, 0),), (), None, ())
    Reagent._op_addAllReagentAnnotationLinkSet = IcePy.Operation('addAllReagentAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ReagentAnnotationLinksSeq, False, 0),), (), None, ())
    Reagent._op_removeReagentAnnotationLink = IcePy.Operation('removeReagentAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ReagentAnnotationLink, False, 0),), (), None, ())
    Reagent._op_removeAllReagentAnnotationLinkSet = IcePy.Operation('removeAllReagentAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ReagentAnnotationLinksSeq, False, 0),), (), None, ())
    Reagent._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Reagent._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Reagent, False, 0),), (), None, ())
    Reagent._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Reagent._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ReagentAnnotationLink, False, 0), ())
    Reagent._op_addReagentAnnotationLinkToBoth = IcePy.Operation('addReagentAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ReagentAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Reagent._op_findReagentAnnotationLink = IcePy.Operation('findReagentAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ReagentAnnotationLinksSeq, False, 0), ())
    Reagent._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Reagent._op_removeReagentAnnotationLinkFromBoth = IcePy.Operation('removeReagentAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ReagentAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Reagent._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ReagentLinkedAnnotationSeq, False, 0), ())
    Reagent._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Reagent._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Reagent = Reagent
    del Reagent

    _M_ode.model.ReagentPrx = ReagentPrx
    del ReagentPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
