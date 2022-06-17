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

if 'NamespaceAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_NamespaceAnnotationLink = IcePy.declareClass('::ode::model::NamespaceAnnotationLink')
    _M_ode.model._t_NamespaceAnnotationLinkPrx = IcePy.declareProxy('::ode::model::NamespaceAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_NamespaceAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_NamespaceAnnotationLinksSeq = IcePy.defineSequence('::ode::model::NamespaceAnnotationLinksSeq', (), _M_ode.model._t_NamespaceAnnotationLink)

if '_t_NamespaceLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_NamespaceLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::NamespaceLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Namespace' not in _M_ode.model.__dict__:
    _M_ode.model.Namespace = Ice.createTempClass()
    class Namespace(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _keywords=None, _multivalued=None, _display=None, _displayName=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _name=None, _description=None):
            if Ice.getType(self) == _M_ode.model.Namespace:
                raise RuntimeError('ode.model.Namespace is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._keywords = _keywords
            self._multivalued = _multivalued
            self._display = _display
            self._displayName = _displayName
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._name = _name
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Namespace')

        def ice_id(self, current=None):
            return '::ode::model::Namespace'

        def ice_staticId():
            return '::ode::model::Namespace'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getKeywords(self, current=None):
            pass

        def setKeywords(self, theKeywords, current=None):
            pass

        def getMultivalued(self, current=None):
            pass

        def setMultivalued(self, theMultivalued, current=None):
            pass

        def getDisplay(self, current=None):
            pass

        def setDisplay(self, theDisplay, current=None):
            pass

        def getDisplayName(self, current=None):
            pass

        def setDisplayName(self, theDisplayName, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addNamespaceAnnotationLink(self, target, current=None):
            pass

        def addAllNamespaceAnnotationLinkSet(self, targets, current=None):
            pass

        def removeNamespaceAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllNamespaceAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addNamespaceAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findNamespaceAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeNamespaceAnnotationLinkFromBoth(self, link, bothSides, current=None):
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
            return IcePy.stringify(self, _M_ode.model._t_Namespace)

        __repr__ = __str__

    _M_ode.model.NamespacePrx = Ice.createTempClass()
    class NamespacePrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Namespace._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Namespace._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Namespace._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Namespace._op_setVersion.end(self, _r)

        def getKeywords(self, _ctx=None):
            return _M_ode.model.Namespace._op_getKeywords.invoke(self, ((), _ctx))

        def begin_getKeywords(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_getKeywords.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getKeywords(self, _r):
            return _M_ode.model.Namespace._op_getKeywords.end(self, _r)

        def setKeywords(self, theKeywords, _ctx=None):
            return _M_ode.model.Namespace._op_setKeywords.invoke(self, ((theKeywords, ), _ctx))

        def begin_setKeywords(self, theKeywords, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_setKeywords.begin(self, ((theKeywords, ), _response, _ex, _sent, _ctx))

        def end_setKeywords(self, _r):
            return _M_ode.model.Namespace._op_setKeywords.end(self, _r)

        def getMultivalued(self, _ctx=None):
            return _M_ode.model.Namespace._op_getMultivalued.invoke(self, ((), _ctx))

        def begin_getMultivalued(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_getMultivalued.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMultivalued(self, _r):
            return _M_ode.model.Namespace._op_getMultivalued.end(self, _r)

        def setMultivalued(self, theMultivalued, _ctx=None):
            return _M_ode.model.Namespace._op_setMultivalued.invoke(self, ((theMultivalued, ), _ctx))

        def begin_setMultivalued(self, theMultivalued, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_setMultivalued.begin(self, ((theMultivalued, ), _response, _ex, _sent, _ctx))

        def end_setMultivalued(self, _r):
            return _M_ode.model.Namespace._op_setMultivalued.end(self, _r)

        def getDisplay(self, _ctx=None):
            return _M_ode.model.Namespace._op_getDisplay.invoke(self, ((), _ctx))

        def begin_getDisplay(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_getDisplay.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDisplay(self, _r):
            return _M_ode.model.Namespace._op_getDisplay.end(self, _r)

        def setDisplay(self, theDisplay, _ctx=None):
            return _M_ode.model.Namespace._op_setDisplay.invoke(self, ((theDisplay, ), _ctx))

        def begin_setDisplay(self, theDisplay, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_setDisplay.begin(self, ((theDisplay, ), _response, _ex, _sent, _ctx))

        def end_setDisplay(self, _r):
            return _M_ode.model.Namespace._op_setDisplay.end(self, _r)

        def getDisplayName(self, _ctx=None):
            return _M_ode.model.Namespace._op_getDisplayName.invoke(self, ((), _ctx))

        def begin_getDisplayName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_getDisplayName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDisplayName(self, _r):
            return _M_ode.model.Namespace._op_getDisplayName.end(self, _r)

        def setDisplayName(self, theDisplayName, _ctx=None):
            return _M_ode.model.Namespace._op_setDisplayName.invoke(self, ((theDisplayName, ), _ctx))

        def begin_setDisplayName(self, theDisplayName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_setDisplayName.begin(self, ((theDisplayName, ), _response, _ex, _sent, _ctx))

        def end_setDisplayName(self, _r):
            return _M_ode.model.Namespace._op_setDisplayName.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Namespace._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Namespace._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Namespace._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Namespace._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Namespace._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Namespace._op_copyAnnotationLinks.end(self, _r)

        def addNamespaceAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Namespace._op_addNamespaceAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addNamespaceAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_addNamespaceAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addNamespaceAnnotationLink(self, _r):
            return _M_ode.model.Namespace._op_addNamespaceAnnotationLink.end(self, _r)

        def addAllNamespaceAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Namespace._op_addAllNamespaceAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllNamespaceAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_addAllNamespaceAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllNamespaceAnnotationLinkSet(self, _r):
            return _M_ode.model.Namespace._op_addAllNamespaceAnnotationLinkSet.end(self, _r)

        def removeNamespaceAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Namespace._op_removeNamespaceAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeNamespaceAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_removeNamespaceAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeNamespaceAnnotationLink(self, _r):
            return _M_ode.model.Namespace._op_removeNamespaceAnnotationLink.end(self, _r)

        def removeAllNamespaceAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Namespace._op_removeAllNamespaceAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllNamespaceAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_removeAllNamespaceAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllNamespaceAnnotationLinkSet(self, _r):
            return _M_ode.model.Namespace._op_removeAllNamespaceAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Namespace._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Namespace._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Namespace._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Namespace._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Namespace._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Namespace._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Namespace._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Namespace._op_linkAnnotation.end(self, _r)

        def addNamespaceAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Namespace._op_addNamespaceAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addNamespaceAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_addNamespaceAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addNamespaceAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Namespace._op_addNamespaceAnnotationLinkToBoth.end(self, _r)

        def findNamespaceAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Namespace._op_findNamespaceAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findNamespaceAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_findNamespaceAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findNamespaceAnnotationLink(self, _r):
            return _M_ode.model.Namespace._op_findNamespaceAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Namespace._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Namespace._op_unlinkAnnotation.end(self, _r)

        def removeNamespaceAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Namespace._op_removeNamespaceAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeNamespaceAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_removeNamespaceAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeNamespaceAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Namespace._op_removeNamespaceAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Namespace._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Namespace._op_linkedAnnotationList.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.Namespace._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.Namespace._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.Namespace._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.Namespace._op_setName.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_ode.model.Namespace._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_ode.model.Namespace._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_ode.model.Namespace._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Namespace._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_ode.model.Namespace._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.NamespacePrx.ice_checkedCast(proxy, '::ode::model::Namespace', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.NamespacePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Namespace'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_NamespacePrx = IcePy.defineProxy('::ode::model::Namespace', NamespacePrx)

    _M_ode.model._t_Namespace = IcePy.declareClass('::ode::model::Namespace')

    _M_ode.model._t_Namespace = IcePy.defineClass('::ode::model::Namespace', Namespace, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_keywords', (), _M_ode.api._t_StringArray, False, 0),
        ('_multivalued', (), _M_ode._t_RBool, False, 0),
        ('_display', (), _M_ode._t_RBool, False, 0),
        ('_displayName', (), _M_ode._t_RString, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_NamespaceAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0),
        ('_description', (), _M_ode._t_RString, False, 0)
    ))
    Namespace._ice_type = _M_ode.model._t_Namespace

    Namespace._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Namespace._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Namespace._op_getKeywords = IcePy.Operation('getKeywords', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_StringArray, False, 0), ())
    Namespace._op_setKeywords = IcePy.Operation('setKeywords', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_StringArray, False, 0),), (), None, ())
    Namespace._op_getMultivalued = IcePy.Operation('getMultivalued', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    Namespace._op_setMultivalued = IcePy.Operation('setMultivalued', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    Namespace._op_getDisplay = IcePy.Operation('getDisplay', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    Namespace._op_setDisplay = IcePy.Operation('setDisplay', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    Namespace._op_getDisplayName = IcePy.Operation('getDisplayName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Namespace._op_setDisplayName = IcePy.Operation('setDisplayName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Namespace._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Namespace._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Namespace._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_NamespaceAnnotationLinksSeq, False, 0), ())
    Namespace._op_addNamespaceAnnotationLink = IcePy.Operation('addNamespaceAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NamespaceAnnotationLink, False, 0),), (), None, ())
    Namespace._op_addAllNamespaceAnnotationLinkSet = IcePy.Operation('addAllNamespaceAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NamespaceAnnotationLinksSeq, False, 0),), (), None, ())
    Namespace._op_removeNamespaceAnnotationLink = IcePy.Operation('removeNamespaceAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NamespaceAnnotationLink, False, 0),), (), None, ())
    Namespace._op_removeAllNamespaceAnnotationLinkSet = IcePy.Operation('removeAllNamespaceAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NamespaceAnnotationLinksSeq, False, 0),), (), None, ())
    Namespace._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Namespace._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Namespace, False, 0),), (), None, ())
    Namespace._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Namespace._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_NamespaceAnnotationLink, False, 0), ())
    Namespace._op_addNamespaceAnnotationLinkToBoth = IcePy.Operation('addNamespaceAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NamespaceAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Namespace._op_findNamespaceAnnotationLink = IcePy.Operation('findNamespaceAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_NamespaceAnnotationLinksSeq, False, 0), ())
    Namespace._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Namespace._op_removeNamespaceAnnotationLinkFromBoth = IcePy.Operation('removeNamespaceAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NamespaceAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Namespace._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_NamespaceLinkedAnnotationSeq, False, 0), ())
    Namespace._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Namespace._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Namespace._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Namespace._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Namespace = Namespace
    del Namespace

    _M_ode.model.NamespacePrx = NamespacePrx
    del NamespacePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
