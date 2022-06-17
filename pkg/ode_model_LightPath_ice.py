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

if 'LightPathExcitationFilterLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightPathExcitationFilterLink = IcePy.declareClass('::ode::model::LightPathExcitationFilterLink')
    _M_ode.model._t_LightPathExcitationFilterLinkPrx = IcePy.declareProxy('::ode::model::LightPathExcitationFilterLink')

if 'Filter' not in _M_ode.model.__dict__:
    _M_ode.model._t_Filter = IcePy.declareClass('::ode::model::Filter')
    _M_ode.model._t_FilterPrx = IcePy.declareProxy('::ode::model::Filter')

if 'Dichroic' not in _M_ode.model.__dict__:
    _M_ode.model._t_Dichroic = IcePy.declareClass('::ode::model::Dichroic')
    _M_ode.model._t_DichroicPrx = IcePy.declareProxy('::ode::model::Dichroic')

if 'LightPathEmissionFilterLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightPathEmissionFilterLink = IcePy.declareClass('::ode::model::LightPathEmissionFilterLink')
    _M_ode.model._t_LightPathEmissionFilterLinkPrx = IcePy.declareProxy('::ode::model::LightPathEmissionFilterLink')

if 'LightPathAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightPathAnnotationLink = IcePy.declareClass('::ode::model::LightPathAnnotationLink')
    _M_ode.model._t_LightPathAnnotationLinkPrx = IcePy.declareProxy('::ode::model::LightPathAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_LightPathExcitationFilterLinkSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightPathExcitationFilterLinkSeq = IcePy.defineSequence('::ode::model::LightPathExcitationFilterLinkSeq', (), _M_ode.model._t_LightPathExcitationFilterLink)

if '_t_LightPathLinkedExcitationFilterSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightPathLinkedExcitationFilterSeq = IcePy.defineSequence('::ode::model::LightPathLinkedExcitationFilterSeq', (), _M_ode.model._t_Filter)

if '_t_LightPathEmissionFilterLinkSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightPathEmissionFilterLinkSeq = IcePy.defineSequence('::ode::model::LightPathEmissionFilterLinkSeq', (), _M_ode.model._t_LightPathEmissionFilterLink)

if '_t_LightPathLinkedEmissionFilterSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightPathLinkedEmissionFilterSeq = IcePy.defineSequence('::ode::model::LightPathLinkedEmissionFilterSeq', (), _M_ode.model._t_Filter)

if '_t_LightPathAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightPathAnnotationLinksSeq = IcePy.defineSequence('::ode::model::LightPathAnnotationLinksSeq', (), _M_ode.model._t_LightPathAnnotationLink)

if '_t_LightPathLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightPathLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::LightPathLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'LightPath' not in _M_ode.model.__dict__:
    _M_ode.model.LightPath = Ice.createTempClass()
    class LightPath(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _excitationFilterLinkSeq=None, _excitationFilterLinkLoaded=False, _excitationFilterLinkCountPerOwner=None, _dichroic=None, _emissionFilterLinkSeq=None, _emissionFilterLinkLoaded=False, _emissionFilterLinkCountPerOwner=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.LightPath:
                raise RuntimeError('ode.model.LightPath is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._excitationFilterLinkSeq = _excitationFilterLinkSeq
            self._excitationFilterLinkLoaded = _excitationFilterLinkLoaded
            self._excitationFilterLinkCountPerOwner = _excitationFilterLinkCountPerOwner
            self._dichroic = _dichroic
            self._emissionFilterLinkSeq = _emissionFilterLinkSeq
            self._emissionFilterLinkLoaded = _emissionFilterLinkLoaded
            self._emissionFilterLinkCountPerOwner = _emissionFilterLinkCountPerOwner
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::LightPath')

        def ice_id(self, current=None):
            return '::ode::model::LightPath'

        def ice_staticId():
            return '::ode::model::LightPath'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def unloadExcitationFilterLink(self, current=None):
            pass

        def sizeOfExcitationFilterLink(self, current=None):
            pass

        def copyExcitationFilterLink(self, current=None):
            pass

        def addLightPathExcitationFilterLink(self, target, current=None):
            pass

        def addAllLightPathExcitationFilterLinkSet(self, targets, current=None):
            pass

        def removeLightPathExcitationFilterLink(self, theTarget, current=None):
            pass

        def removeAllLightPathExcitationFilterLinkSet(self, targets, current=None):
            pass

        def clearExcitationFilterLink(self, current=None):
            pass

        def reloadExcitationFilterLink(self, toCopy, current=None):
            pass

        def getLightPathExcitationFilterLink(self, index, current=None):
            pass

        def setLightPathExcitationFilterLink(self, index, theElement, current=None):
            pass

        def getPrimaryLightPathExcitationFilterLink(self, current=None):
            pass

        def setPrimaryLightPathExcitationFilterLink(self, theElement, current=None):
            pass

        def getExcitationFilterLinkCountPerOwner(self, current=None):
            pass

        def linkExcitationFilter(self, addition, current=None):
            pass

        def addLightPathExcitationFilterLinkToBoth(self, link, bothSides, current=None):
            pass

        def findLightPathExcitationFilterLink(self, removal, current=None):
            pass

        def unlinkExcitationFilter(self, removal, current=None):
            pass

        def removeLightPathExcitationFilterLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedExcitationFilterList(self, current=None):
            pass

        def getDichroic(self, current=None):
            pass

        def setDichroic(self, theDichroic, current=None):
            pass

        def unloadEmissionFilterLink(self, current=None):
            pass

        def sizeOfEmissionFilterLink(self, current=None):
            pass

        def copyEmissionFilterLink(self, current=None):
            pass

        def addLightPathEmissionFilterLink(self, target, current=None):
            pass

        def addAllLightPathEmissionFilterLinkSet(self, targets, current=None):
            pass

        def removeLightPathEmissionFilterLink(self, theTarget, current=None):
            pass

        def removeAllLightPathEmissionFilterLinkSet(self, targets, current=None):
            pass

        def clearEmissionFilterLink(self, current=None):
            pass

        def reloadEmissionFilterLink(self, toCopy, current=None):
            pass

        def getEmissionFilterLinkCountPerOwner(self, current=None):
            pass

        def linkEmissionFilter(self, addition, current=None):
            pass

        def addLightPathEmissionFilterLinkToBoth(self, link, bothSides, current=None):
            pass

        def findLightPathEmissionFilterLink(self, removal, current=None):
            pass

        def unlinkEmissionFilter(self, removal, current=None):
            pass

        def removeLightPathEmissionFilterLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedEmissionFilterList(self, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addLightPathAnnotationLink(self, target, current=None):
            pass

        def addAllLightPathAnnotationLinkSet(self, targets, current=None):
            pass

        def removeLightPathAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllLightPathAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addLightPathAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findLightPathAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeLightPathAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_LightPath)

        __repr__ = __str__

    _M_ode.model.LightPathPrx = Ice.createTempClass()
    class LightPathPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.LightPath._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.LightPath._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.LightPath._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.LightPath._op_setVersion.end(self, _r)

        def unloadExcitationFilterLink(self, _ctx=None):
            return _M_ode.model.LightPath._op_unloadExcitationFilterLink.invoke(self, ((), _ctx))

        def begin_unloadExcitationFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_unloadExcitationFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_unloadExcitationFilterLink.end(self, _r)

        def sizeOfExcitationFilterLink(self, _ctx=None):
            return _M_ode.model.LightPath._op_sizeOfExcitationFilterLink.invoke(self, ((), _ctx))

        def begin_sizeOfExcitationFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_sizeOfExcitationFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_sizeOfExcitationFilterLink.end(self, _r)

        def copyExcitationFilterLink(self, _ctx=None):
            return _M_ode.model.LightPath._op_copyExcitationFilterLink.invoke(self, ((), _ctx))

        def begin_copyExcitationFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_copyExcitationFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_copyExcitationFilterLink.end(self, _r)

        def addLightPathExcitationFilterLink(self, target, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathExcitationFilterLink.invoke(self, ((target, ), _ctx))

        def begin_addLightPathExcitationFilterLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathExcitationFilterLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addLightPathExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_addLightPathExcitationFilterLink.end(self, _r)

        def addAllLightPathExcitationFilterLinkSet(self, targets, _ctx=None):
            return _M_ode.model.LightPath._op_addAllLightPathExcitationFilterLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllLightPathExcitationFilterLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_addAllLightPathExcitationFilterLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllLightPathExcitationFilterLinkSet(self, _r):
            return _M_ode.model.LightPath._op_addAllLightPathExcitationFilterLinkSet.end(self, _r)

        def removeLightPathExcitationFilterLink(self, theTarget, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathExcitationFilterLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeLightPathExcitationFilterLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathExcitationFilterLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeLightPathExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_removeLightPathExcitationFilterLink.end(self, _r)

        def removeAllLightPathExcitationFilterLinkSet(self, targets, _ctx=None):
            return _M_ode.model.LightPath._op_removeAllLightPathExcitationFilterLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllLightPathExcitationFilterLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_removeAllLightPathExcitationFilterLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllLightPathExcitationFilterLinkSet(self, _r):
            return _M_ode.model.LightPath._op_removeAllLightPathExcitationFilterLinkSet.end(self, _r)

        def clearExcitationFilterLink(self, _ctx=None):
            return _M_ode.model.LightPath._op_clearExcitationFilterLink.invoke(self, ((), _ctx))

        def begin_clearExcitationFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_clearExcitationFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_clearExcitationFilterLink.end(self, _r)

        def reloadExcitationFilterLink(self, toCopy, _ctx=None):
            return _M_ode.model.LightPath._op_reloadExcitationFilterLink.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadExcitationFilterLink(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_reloadExcitationFilterLink.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_reloadExcitationFilterLink.end(self, _r)

        def getLightPathExcitationFilterLink(self, index, _ctx=None):
            return _M_ode.model.LightPath._op_getLightPathExcitationFilterLink.invoke(self, ((index, ), _ctx))

        def begin_getLightPathExcitationFilterLink(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_getLightPathExcitationFilterLink.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getLightPathExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_getLightPathExcitationFilterLink.end(self, _r)

        def setLightPathExcitationFilterLink(self, index, theElement, _ctx=None):
            return _M_ode.model.LightPath._op_setLightPathExcitationFilterLink.invoke(self, ((index, theElement), _ctx))

        def begin_setLightPathExcitationFilterLink(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_setLightPathExcitationFilterLink.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setLightPathExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_setLightPathExcitationFilterLink.end(self, _r)

        def getPrimaryLightPathExcitationFilterLink(self, _ctx=None):
            return _M_ode.model.LightPath._op_getPrimaryLightPathExcitationFilterLink.invoke(self, ((), _ctx))

        def begin_getPrimaryLightPathExcitationFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_getPrimaryLightPathExcitationFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryLightPathExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_getPrimaryLightPathExcitationFilterLink.end(self, _r)

        def setPrimaryLightPathExcitationFilterLink(self, theElement, _ctx=None):
            return _M_ode.model.LightPath._op_setPrimaryLightPathExcitationFilterLink.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryLightPathExcitationFilterLink(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_setPrimaryLightPathExcitationFilterLink.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryLightPathExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_setPrimaryLightPathExcitationFilterLink.end(self, _r)

        def getExcitationFilterLinkCountPerOwner(self, _ctx=None):
            return _M_ode.model.LightPath._op_getExcitationFilterLinkCountPerOwner.invoke(self, ((), _ctx))

        def begin_getExcitationFilterLinkCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_getExcitationFilterLinkCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getExcitationFilterLinkCountPerOwner(self, _r):
            return _M_ode.model.LightPath._op_getExcitationFilterLinkCountPerOwner.end(self, _r)

        def linkExcitationFilter(self, addition, _ctx=None):
            return _M_ode.model.LightPath._op_linkExcitationFilter.invoke(self, ((addition, ), _ctx))

        def begin_linkExcitationFilter(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_linkExcitationFilter.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkExcitationFilter(self, _r):
            return _M_ode.model.LightPath._op_linkExcitationFilter.end(self, _r)

        def addLightPathExcitationFilterLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathExcitationFilterLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addLightPathExcitationFilterLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathExcitationFilterLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addLightPathExcitationFilterLinkToBoth(self, _r):
            return _M_ode.model.LightPath._op_addLightPathExcitationFilterLinkToBoth.end(self, _r)

        def findLightPathExcitationFilterLink(self, removal, _ctx=None):
            return _M_ode.model.LightPath._op_findLightPathExcitationFilterLink.invoke(self, ((removal, ), _ctx))

        def begin_findLightPathExcitationFilterLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_findLightPathExcitationFilterLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findLightPathExcitationFilterLink(self, _r):
            return _M_ode.model.LightPath._op_findLightPathExcitationFilterLink.end(self, _r)

        def unlinkExcitationFilter(self, removal, _ctx=None):
            return _M_ode.model.LightPath._op_unlinkExcitationFilter.invoke(self, ((removal, ), _ctx))

        def begin_unlinkExcitationFilter(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_unlinkExcitationFilter.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkExcitationFilter(self, _r):
            return _M_ode.model.LightPath._op_unlinkExcitationFilter.end(self, _r)

        def removeLightPathExcitationFilterLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathExcitationFilterLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeLightPathExcitationFilterLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathExcitationFilterLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeLightPathExcitationFilterLinkFromBoth(self, _r):
            return _M_ode.model.LightPath._op_removeLightPathExcitationFilterLinkFromBoth.end(self, _r)

        def linkedExcitationFilterList(self, _ctx=None):
            return _M_ode.model.LightPath._op_linkedExcitationFilterList.invoke(self, ((), _ctx))

        def begin_linkedExcitationFilterList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_linkedExcitationFilterList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedExcitationFilterList(self, _r):
            return _M_ode.model.LightPath._op_linkedExcitationFilterList.end(self, _r)

        def getDichroic(self, _ctx=None):
            return _M_ode.model.LightPath._op_getDichroic.invoke(self, ((), _ctx))

        def begin_getDichroic(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_getDichroic.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDichroic(self, _r):
            return _M_ode.model.LightPath._op_getDichroic.end(self, _r)

        def setDichroic(self, theDichroic, _ctx=None):
            return _M_ode.model.LightPath._op_setDichroic.invoke(self, ((theDichroic, ), _ctx))

        def begin_setDichroic(self, theDichroic, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_setDichroic.begin(self, ((theDichroic, ), _response, _ex, _sent, _ctx))

        def end_setDichroic(self, _r):
            return _M_ode.model.LightPath._op_setDichroic.end(self, _r)

        def unloadEmissionFilterLink(self, _ctx=None):
            return _M_ode.model.LightPath._op_unloadEmissionFilterLink.invoke(self, ((), _ctx))

        def begin_unloadEmissionFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_unloadEmissionFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadEmissionFilterLink(self, _r):
            return _M_ode.model.LightPath._op_unloadEmissionFilterLink.end(self, _r)

        def sizeOfEmissionFilterLink(self, _ctx=None):
            return _M_ode.model.LightPath._op_sizeOfEmissionFilterLink.invoke(self, ((), _ctx))

        def begin_sizeOfEmissionFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_sizeOfEmissionFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfEmissionFilterLink(self, _r):
            return _M_ode.model.LightPath._op_sizeOfEmissionFilterLink.end(self, _r)

        def copyEmissionFilterLink(self, _ctx=None):
            return _M_ode.model.LightPath._op_copyEmissionFilterLink.invoke(self, ((), _ctx))

        def begin_copyEmissionFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_copyEmissionFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyEmissionFilterLink(self, _r):
            return _M_ode.model.LightPath._op_copyEmissionFilterLink.end(self, _r)

        def addLightPathEmissionFilterLink(self, target, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathEmissionFilterLink.invoke(self, ((target, ), _ctx))

        def begin_addLightPathEmissionFilterLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathEmissionFilterLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addLightPathEmissionFilterLink(self, _r):
            return _M_ode.model.LightPath._op_addLightPathEmissionFilterLink.end(self, _r)

        def addAllLightPathEmissionFilterLinkSet(self, targets, _ctx=None):
            return _M_ode.model.LightPath._op_addAllLightPathEmissionFilterLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllLightPathEmissionFilterLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_addAllLightPathEmissionFilterLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllLightPathEmissionFilterLinkSet(self, _r):
            return _M_ode.model.LightPath._op_addAllLightPathEmissionFilterLinkSet.end(self, _r)

        def removeLightPathEmissionFilterLink(self, theTarget, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathEmissionFilterLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeLightPathEmissionFilterLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathEmissionFilterLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeLightPathEmissionFilterLink(self, _r):
            return _M_ode.model.LightPath._op_removeLightPathEmissionFilterLink.end(self, _r)

        def removeAllLightPathEmissionFilterLinkSet(self, targets, _ctx=None):
            return _M_ode.model.LightPath._op_removeAllLightPathEmissionFilterLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllLightPathEmissionFilterLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_removeAllLightPathEmissionFilterLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllLightPathEmissionFilterLinkSet(self, _r):
            return _M_ode.model.LightPath._op_removeAllLightPathEmissionFilterLinkSet.end(self, _r)

        def clearEmissionFilterLink(self, _ctx=None):
            return _M_ode.model.LightPath._op_clearEmissionFilterLink.invoke(self, ((), _ctx))

        def begin_clearEmissionFilterLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_clearEmissionFilterLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearEmissionFilterLink(self, _r):
            return _M_ode.model.LightPath._op_clearEmissionFilterLink.end(self, _r)

        def reloadEmissionFilterLink(self, toCopy, _ctx=None):
            return _M_ode.model.LightPath._op_reloadEmissionFilterLink.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadEmissionFilterLink(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_reloadEmissionFilterLink.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadEmissionFilterLink(self, _r):
            return _M_ode.model.LightPath._op_reloadEmissionFilterLink.end(self, _r)

        def getEmissionFilterLinkCountPerOwner(self, _ctx=None):
            return _M_ode.model.LightPath._op_getEmissionFilterLinkCountPerOwner.invoke(self, ((), _ctx))

        def begin_getEmissionFilterLinkCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_getEmissionFilterLinkCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEmissionFilterLinkCountPerOwner(self, _r):
            return _M_ode.model.LightPath._op_getEmissionFilterLinkCountPerOwner.end(self, _r)

        def linkEmissionFilter(self, addition, _ctx=None):
            return _M_ode.model.LightPath._op_linkEmissionFilter.invoke(self, ((addition, ), _ctx))

        def begin_linkEmissionFilter(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_linkEmissionFilter.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkEmissionFilter(self, _r):
            return _M_ode.model.LightPath._op_linkEmissionFilter.end(self, _r)

        def addLightPathEmissionFilterLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathEmissionFilterLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addLightPathEmissionFilterLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathEmissionFilterLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addLightPathEmissionFilterLinkToBoth(self, _r):
            return _M_ode.model.LightPath._op_addLightPathEmissionFilterLinkToBoth.end(self, _r)

        def findLightPathEmissionFilterLink(self, removal, _ctx=None):
            return _M_ode.model.LightPath._op_findLightPathEmissionFilterLink.invoke(self, ((removal, ), _ctx))

        def begin_findLightPathEmissionFilterLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_findLightPathEmissionFilterLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findLightPathEmissionFilterLink(self, _r):
            return _M_ode.model.LightPath._op_findLightPathEmissionFilterLink.end(self, _r)

        def unlinkEmissionFilter(self, removal, _ctx=None):
            return _M_ode.model.LightPath._op_unlinkEmissionFilter.invoke(self, ((removal, ), _ctx))

        def begin_unlinkEmissionFilter(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_unlinkEmissionFilter.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkEmissionFilter(self, _r):
            return _M_ode.model.LightPath._op_unlinkEmissionFilter.end(self, _r)

        def removeLightPathEmissionFilterLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathEmissionFilterLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeLightPathEmissionFilterLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathEmissionFilterLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeLightPathEmissionFilterLinkFromBoth(self, _r):
            return _M_ode.model.LightPath._op_removeLightPathEmissionFilterLinkFromBoth.end(self, _r)

        def linkedEmissionFilterList(self, _ctx=None):
            return _M_ode.model.LightPath._op_linkedEmissionFilterList.invoke(self, ((), _ctx))

        def begin_linkedEmissionFilterList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_linkedEmissionFilterList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedEmissionFilterList(self, _r):
            return _M_ode.model.LightPath._op_linkedEmissionFilterList.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.LightPath._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.LightPath._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.LightPath._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.LightPath._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.LightPath._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.LightPath._op_copyAnnotationLinks.end(self, _r)

        def addLightPathAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addLightPathAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addLightPathAnnotationLink(self, _r):
            return _M_ode.model.LightPath._op_addLightPathAnnotationLink.end(self, _r)

        def addAllLightPathAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.LightPath._op_addAllLightPathAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllLightPathAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_addAllLightPathAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllLightPathAnnotationLinkSet(self, _r):
            return _M_ode.model.LightPath._op_addAllLightPathAnnotationLinkSet.end(self, _r)

        def removeLightPathAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeLightPathAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeLightPathAnnotationLink(self, _r):
            return _M_ode.model.LightPath._op_removeLightPathAnnotationLink.end(self, _r)

        def removeAllLightPathAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.LightPath._op_removeAllLightPathAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllLightPathAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_removeAllLightPathAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllLightPathAnnotationLinkSet(self, _r):
            return _M_ode.model.LightPath._op_removeAllLightPathAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.LightPath._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.LightPath._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.LightPath._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.LightPath._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.LightPath._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.LightPath._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.LightPath._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.LightPath._op_linkAnnotation.end(self, _r)

        def addLightPathAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addLightPathAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_addLightPathAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addLightPathAnnotationLinkToBoth(self, _r):
            return _M_ode.model.LightPath._op_addLightPathAnnotationLinkToBoth.end(self, _r)

        def findLightPathAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.LightPath._op_findLightPathAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findLightPathAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_findLightPathAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findLightPathAnnotationLink(self, _r):
            return _M_ode.model.LightPath._op_findLightPathAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.LightPath._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.LightPath._op_unlinkAnnotation.end(self, _r)

        def removeLightPathAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeLightPathAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_removeLightPathAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeLightPathAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.LightPath._op_removeLightPathAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.LightPath._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.LightPath._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.LightPath._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.LightPathPrx.ice_checkedCast(proxy, '::ode::model::LightPath', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.LightPathPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::LightPath'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_LightPathPrx = IcePy.defineProxy('::ode::model::LightPath', LightPathPrx)

    _M_ode.model._t_LightPath = IcePy.declareClass('::ode::model::LightPath')

    _M_ode.model._t_LightPath = IcePy.defineClass('::ode::model::LightPath', LightPath, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_excitationFilterLinkSeq', (), _M_ode.model._t_LightPathExcitationFilterLinkSeq, False, 0),
        ('_excitationFilterLinkLoaded', (), IcePy._t_bool, False, 0),
        ('_excitationFilterLinkCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_dichroic', (), _M_ode.model._t_Dichroic, False, 0),
        ('_emissionFilterLinkSeq', (), _M_ode.model._t_LightPathEmissionFilterLinkSeq, False, 0),
        ('_emissionFilterLinkLoaded', (), IcePy._t_bool, False, 0),
        ('_emissionFilterLinkCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_LightPathAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    LightPath._ice_type = _M_ode.model._t_LightPath

    LightPath._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    LightPath._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    LightPath._op_unloadExcitationFilterLink = IcePy.Operation('unloadExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    LightPath._op_sizeOfExcitationFilterLink = IcePy.Operation('sizeOfExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    LightPath._op_copyExcitationFilterLink = IcePy.Operation('copyExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightPathExcitationFilterLinkSeq, False, 0), ())
    LightPath._op_addLightPathExcitationFilterLink = IcePy.Operation('addLightPathExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0),), (), None, ())
    LightPath._op_addAllLightPathExcitationFilterLinkSet = IcePy.Operation('addAllLightPathExcitationFilterLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathExcitationFilterLinkSeq, False, 0),), (), None, ())
    LightPath._op_removeLightPathExcitationFilterLink = IcePy.Operation('removeLightPathExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0),), (), None, ())
    LightPath._op_removeAllLightPathExcitationFilterLinkSet = IcePy.Operation('removeAllLightPathExcitationFilterLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathExcitationFilterLinkSeq, False, 0),), (), None, ())
    LightPath._op_clearExcitationFilterLink = IcePy.Operation('clearExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    LightPath._op_reloadExcitationFilterLink = IcePy.Operation('reloadExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPath, False, 0),), (), None, ())
    LightPath._op_getLightPathExcitationFilterLink = IcePy.Operation('getLightPathExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0), ())
    LightPath._op_setLightPathExcitationFilterLink = IcePy.Operation('setLightPathExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0)), (), ((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0), ())
    LightPath._op_getPrimaryLightPathExcitationFilterLink = IcePy.Operation('getPrimaryLightPathExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0), ())
    LightPath._op_setPrimaryLightPathExcitationFilterLink = IcePy.Operation('setPrimaryLightPathExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0),), (), ((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0), ())
    LightPath._op_getExcitationFilterLinkCountPerOwner = IcePy.Operation('getExcitationFilterLinkCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    LightPath._op_linkExcitationFilter = IcePy.Operation('linkExcitationFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Filter, False, 0),), (), ((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0), ())
    LightPath._op_addLightPathExcitationFilterLinkToBoth = IcePy.Operation('addLightPathExcitationFilterLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    LightPath._op_findLightPathExcitationFilterLink = IcePy.Operation('findLightPathExcitationFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Filter, False, 0),), (), ((), _M_ode.model._t_LightPathExcitationFilterLinkSeq, False, 0), ())
    LightPath._op_unlinkExcitationFilter = IcePy.Operation('unlinkExcitationFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Filter, False, 0),), (), None, ())
    LightPath._op_removeLightPathExcitationFilterLinkFromBoth = IcePy.Operation('removeLightPathExcitationFilterLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathExcitationFilterLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    LightPath._op_linkedExcitationFilterList = IcePy.Operation('linkedExcitationFilterList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightPathLinkedExcitationFilterSeq, False, 0), ())
    LightPath._op_getDichroic = IcePy.Operation('getDichroic', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Dichroic, False, 0), ())
    LightPath._op_setDichroic = IcePy.Operation('setDichroic', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Dichroic, False, 0),), (), None, ())
    LightPath._op_unloadEmissionFilterLink = IcePy.Operation('unloadEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    LightPath._op_sizeOfEmissionFilterLink = IcePy.Operation('sizeOfEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    LightPath._op_copyEmissionFilterLink = IcePy.Operation('copyEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightPathEmissionFilterLinkSeq, False, 0), ())
    LightPath._op_addLightPathEmissionFilterLink = IcePy.Operation('addLightPathEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathEmissionFilterLink, False, 0),), (), None, ())
    LightPath._op_addAllLightPathEmissionFilterLinkSet = IcePy.Operation('addAllLightPathEmissionFilterLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathEmissionFilterLinkSeq, False, 0),), (), None, ())
    LightPath._op_removeLightPathEmissionFilterLink = IcePy.Operation('removeLightPathEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathEmissionFilterLink, False, 0),), (), None, ())
    LightPath._op_removeAllLightPathEmissionFilterLinkSet = IcePy.Operation('removeAllLightPathEmissionFilterLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathEmissionFilterLinkSeq, False, 0),), (), None, ())
    LightPath._op_clearEmissionFilterLink = IcePy.Operation('clearEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    LightPath._op_reloadEmissionFilterLink = IcePy.Operation('reloadEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPath, False, 0),), (), None, ())
    LightPath._op_getEmissionFilterLinkCountPerOwner = IcePy.Operation('getEmissionFilterLinkCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    LightPath._op_linkEmissionFilter = IcePy.Operation('linkEmissionFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Filter, False, 0),), (), ((), _M_ode.model._t_LightPathEmissionFilterLink, False, 0), ())
    LightPath._op_addLightPathEmissionFilterLinkToBoth = IcePy.Operation('addLightPathEmissionFilterLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathEmissionFilterLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    LightPath._op_findLightPathEmissionFilterLink = IcePy.Operation('findLightPathEmissionFilterLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Filter, False, 0),), (), ((), _M_ode.model._t_LightPathEmissionFilterLinkSeq, False, 0), ())
    LightPath._op_unlinkEmissionFilter = IcePy.Operation('unlinkEmissionFilter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Filter, False, 0),), (), None, ())
    LightPath._op_removeLightPathEmissionFilterLinkFromBoth = IcePy.Operation('removeLightPathEmissionFilterLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathEmissionFilterLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    LightPath._op_linkedEmissionFilterList = IcePy.Operation('linkedEmissionFilterList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightPathLinkedEmissionFilterSeq, False, 0), ())
    LightPath._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    LightPath._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    LightPath._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightPathAnnotationLinksSeq, False, 0), ())
    LightPath._op_addLightPathAnnotationLink = IcePy.Operation('addLightPathAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathAnnotationLink, False, 0),), (), None, ())
    LightPath._op_addAllLightPathAnnotationLinkSet = IcePy.Operation('addAllLightPathAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathAnnotationLinksSeq, False, 0),), (), None, ())
    LightPath._op_removeLightPathAnnotationLink = IcePy.Operation('removeLightPathAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathAnnotationLink, False, 0),), (), None, ())
    LightPath._op_removeAllLightPathAnnotationLinkSet = IcePy.Operation('removeAllLightPathAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathAnnotationLinksSeq, False, 0),), (), None, ())
    LightPath._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    LightPath._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPath, False, 0),), (), None, ())
    LightPath._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    LightPath._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_LightPathAnnotationLink, False, 0), ())
    LightPath._op_addLightPathAnnotationLinkToBoth = IcePy.Operation('addLightPathAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    LightPath._op_findLightPathAnnotationLink = IcePy.Operation('findLightPathAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_LightPathAnnotationLinksSeq, False, 0), ())
    LightPath._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    LightPath._op_removeLightPathAnnotationLinkFromBoth = IcePy.Operation('removeLightPathAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LightPathAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    LightPath._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LightPathLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.LightPath = LightPath
    del LightPath

    _M_ode.model.LightPathPrx = LightPathPrx
    del LightPathPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
