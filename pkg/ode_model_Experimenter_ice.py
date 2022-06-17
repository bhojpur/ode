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

if 'GroupExperimenterMap' not in _M_ode.model.__dict__:
    _M_ode.model._t_GroupExperimenterMap = IcePy.declareClass('::ode::model::GroupExperimenterMap')
    _M_ode.model._t_GroupExperimenterMapPrx = IcePy.declareProxy('::ode::model::GroupExperimenterMap')

if 'ExperimenterGroup' not in _M_ode.model.__dict__:
    _M_ode.model._t_ExperimenterGroup = IcePy.declareClass('::ode::model::ExperimenterGroup')
    _M_ode.model._t_ExperimenterGroupPrx = IcePy.declareProxy('::ode::model::ExperimenterGroup')

if 'ExperimenterAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_ExperimenterAnnotationLink = IcePy.declareClass('::ode::model::ExperimenterAnnotationLink')
    _M_ode.model._t_ExperimenterAnnotationLinkPrx = IcePy.declareProxy('::ode::model::ExperimenterAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_ExperimenterGroupExperimenterMapSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ExperimenterGroupExperimenterMapSeq = IcePy.defineSequence('::ode::model::ExperimenterGroupExperimenterMapSeq', (), _M_ode.model._t_GroupExperimenterMap)

if '_t_ExperimenterLinkedExperimenterGroupSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ExperimenterLinkedExperimenterGroupSeq = IcePy.defineSequence('::ode::model::ExperimenterLinkedExperimenterGroupSeq', (), _M_ode.model._t_ExperimenterGroup)

if '_t_ExperimenterAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ExperimenterAnnotationLinksSeq = IcePy.defineSequence('::ode::model::ExperimenterAnnotationLinksSeq', (), _M_ode.model._t_ExperimenterAnnotationLink)

if '_t_ExperimenterLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ExperimenterLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::ExperimenterLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Experimenter' not in _M_ode.model.__dict__:
    _M_ode.model.Experimenter = Ice.createTempClass()
    class Experimenter(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _groupExperimenterMapSeq=None, _groupExperimenterMapLoaded=False, _odeName=None, _firstName=None, _middleName=None, _lastName=None, _institution=None, _ldap=None, _email=None, _config=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.Experimenter:
                raise RuntimeError('ode.model.Experimenter is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._groupExperimenterMapSeq = _groupExperimenterMapSeq
            self._groupExperimenterMapLoaded = _groupExperimenterMapLoaded
            self._odeName = _odeName
            self._firstName = _firstName
            self._middleName = _middleName
            self._lastName = _lastName
            self._institution = _institution
            self._ldap = _ldap
            self._email = _email
            self._config = _config
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Experimenter', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::Experimenter'

        def ice_staticId():
            return '::ode::model::Experimenter'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def unloadGroupExperimenterMap(self, current=None):
            pass

        def sizeOfGroupExperimenterMap(self, current=None):
            pass

        def copyGroupExperimenterMap(self, current=None):
            pass

        def addGroupExperimenterMap(self, target, current=None):
            pass

        def addAllGroupExperimenterMapSet(self, targets, current=None):
            pass

        def removeGroupExperimenterMap(self, theTarget, current=None):
            pass

        def removeAllGroupExperimenterMapSet(self, targets, current=None):
            pass

        def clearGroupExperimenterMap(self, current=None):
            pass

        def reloadGroupExperimenterMap(self, toCopy, current=None):
            pass

        def getGroupExperimenterMap(self, index, current=None):
            pass

        def setGroupExperimenterMap(self, index, theElement, current=None):
            pass

        def getPrimaryGroupExperimenterMap(self, current=None):
            pass

        def setPrimaryGroupExperimenterMap(self, theElement, current=None):
            pass

        def linkExperimenterGroup(self, addition, current=None):
            pass

        def addGroupExperimenterMapToBoth(self, link, bothSides, current=None):
            pass

        def findGroupExperimenterMap(self, removal, current=None):
            pass

        def unlinkExperimenterGroup(self, removal, current=None):
            pass

        def removeGroupExperimenterMapFromBoth(self, link, bothSides, current=None):
            pass

        def linkedExperimenterGroupList(self, current=None):
            pass

        def getOdeName(self, current=None):
            pass

        def setOdeName(self, theOdeName, current=None):
            pass

        def getFirstName(self, current=None):
            pass

        def setFirstName(self, theFirstName, current=None):
            pass

        def getMiddleName(self, current=None):
            pass

        def setMiddleName(self, theMiddleName, current=None):
            pass

        def getLastName(self, current=None):
            pass

        def setLastName(self, theLastName, current=None):
            pass

        def getInstitution(self, current=None):
            pass

        def setInstitution(self, theInstitution, current=None):
            pass

        def getLdap(self, current=None):
            pass

        def setLdap(self, theLdap, current=None):
            pass

        def getEmail(self, current=None):
            pass

        def setEmail(self, theEmail, current=None):
            pass

        def getConfigAsMap(self, current=None):
            pass

        def getConfig(self, current=None):
            pass

        def setConfig(self, theConfig, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addExperimenterAnnotationLink(self, target, current=None):
            pass

        def addAllExperimenterAnnotationLinkSet(self, targets, current=None):
            pass

        def removeExperimenterAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllExperimenterAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addExperimenterAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findExperimenterAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeExperimenterAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Experimenter)

        __repr__ = __str__

    _M_ode.model.ExperimenterPrx = Ice.createTempClass()
    class ExperimenterPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Experimenter._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Experimenter._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Experimenter._op_setVersion.end(self, _r)

        def unloadGroupExperimenterMap(self, _ctx=None):
            return _M_ode.model.Experimenter._op_unloadGroupExperimenterMap.invoke(self, ((), _ctx))

        def begin_unloadGroupExperimenterMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_unloadGroupExperimenterMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_unloadGroupExperimenterMap.end(self, _r)

        def sizeOfGroupExperimenterMap(self, _ctx=None):
            return _M_ode.model.Experimenter._op_sizeOfGroupExperimenterMap.invoke(self, ((), _ctx))

        def begin_sizeOfGroupExperimenterMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_sizeOfGroupExperimenterMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_sizeOfGroupExperimenterMap.end(self, _r)

        def copyGroupExperimenterMap(self, _ctx=None):
            return _M_ode.model.Experimenter._op_copyGroupExperimenterMap.invoke(self, ((), _ctx))

        def begin_copyGroupExperimenterMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_copyGroupExperimenterMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_copyGroupExperimenterMap.end(self, _r)

        def addGroupExperimenterMap(self, target, _ctx=None):
            return _M_ode.model.Experimenter._op_addGroupExperimenterMap.invoke(self, ((target, ), _ctx))

        def begin_addGroupExperimenterMap(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_addGroupExperimenterMap.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_addGroupExperimenterMap.end(self, _r)

        def addAllGroupExperimenterMapSet(self, targets, _ctx=None):
            return _M_ode.model.Experimenter._op_addAllGroupExperimenterMapSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllGroupExperimenterMapSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_addAllGroupExperimenterMapSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllGroupExperimenterMapSet(self, _r):
            return _M_ode.model.Experimenter._op_addAllGroupExperimenterMapSet.end(self, _r)

        def removeGroupExperimenterMap(self, theTarget, _ctx=None):
            return _M_ode.model.Experimenter._op_removeGroupExperimenterMap.invoke(self, ((theTarget, ), _ctx))

        def begin_removeGroupExperimenterMap(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_removeGroupExperimenterMap.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_removeGroupExperimenterMap.end(self, _r)

        def removeAllGroupExperimenterMapSet(self, targets, _ctx=None):
            return _M_ode.model.Experimenter._op_removeAllGroupExperimenterMapSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllGroupExperimenterMapSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_removeAllGroupExperimenterMapSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllGroupExperimenterMapSet(self, _r):
            return _M_ode.model.Experimenter._op_removeAllGroupExperimenterMapSet.end(self, _r)

        def clearGroupExperimenterMap(self, _ctx=None):
            return _M_ode.model.Experimenter._op_clearGroupExperimenterMap.invoke(self, ((), _ctx))

        def begin_clearGroupExperimenterMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_clearGroupExperimenterMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_clearGroupExperimenterMap.end(self, _r)

        def reloadGroupExperimenterMap(self, toCopy, _ctx=None):
            return _M_ode.model.Experimenter._op_reloadGroupExperimenterMap.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadGroupExperimenterMap(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_reloadGroupExperimenterMap.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_reloadGroupExperimenterMap.end(self, _r)

        def getGroupExperimenterMap(self, index, _ctx=None):
            return _M_ode.model.Experimenter._op_getGroupExperimenterMap.invoke(self, ((index, ), _ctx))

        def begin_getGroupExperimenterMap(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getGroupExperimenterMap.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_getGroupExperimenterMap.end(self, _r)

        def setGroupExperimenterMap(self, index, theElement, _ctx=None):
            return _M_ode.model.Experimenter._op_setGroupExperimenterMap.invoke(self, ((index, theElement), _ctx))

        def begin_setGroupExperimenterMap(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setGroupExperimenterMap.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_setGroupExperimenterMap.end(self, _r)

        def getPrimaryGroupExperimenterMap(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getPrimaryGroupExperimenterMap.invoke(self, ((), _ctx))

        def begin_getPrimaryGroupExperimenterMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getPrimaryGroupExperimenterMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_getPrimaryGroupExperimenterMap.end(self, _r)

        def setPrimaryGroupExperimenterMap(self, theElement, _ctx=None):
            return _M_ode.model.Experimenter._op_setPrimaryGroupExperimenterMap.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryGroupExperimenterMap(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setPrimaryGroupExperimenterMap.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_setPrimaryGroupExperimenterMap.end(self, _r)

        def linkExperimenterGroup(self, addition, _ctx=None):
            return _M_ode.model.Experimenter._op_linkExperimenterGroup.invoke(self, ((addition, ), _ctx))

        def begin_linkExperimenterGroup(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_linkExperimenterGroup.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkExperimenterGroup(self, _r):
            return _M_ode.model.Experimenter._op_linkExperimenterGroup.end(self, _r)

        def addGroupExperimenterMapToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Experimenter._op_addGroupExperimenterMapToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addGroupExperimenterMapToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_addGroupExperimenterMapToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addGroupExperimenterMapToBoth(self, _r):
            return _M_ode.model.Experimenter._op_addGroupExperimenterMapToBoth.end(self, _r)

        def findGroupExperimenterMap(self, removal, _ctx=None):
            return _M_ode.model.Experimenter._op_findGroupExperimenterMap.invoke(self, ((removal, ), _ctx))

        def begin_findGroupExperimenterMap(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_findGroupExperimenterMap.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findGroupExperimenterMap(self, _r):
            return _M_ode.model.Experimenter._op_findGroupExperimenterMap.end(self, _r)

        def unlinkExperimenterGroup(self, removal, _ctx=None):
            return _M_ode.model.Experimenter._op_unlinkExperimenterGroup.invoke(self, ((removal, ), _ctx))

        def begin_unlinkExperimenterGroup(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_unlinkExperimenterGroup.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkExperimenterGroup(self, _r):
            return _M_ode.model.Experimenter._op_unlinkExperimenterGroup.end(self, _r)

        def removeGroupExperimenterMapFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Experimenter._op_removeGroupExperimenterMapFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeGroupExperimenterMapFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_removeGroupExperimenterMapFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeGroupExperimenterMapFromBoth(self, _r):
            return _M_ode.model.Experimenter._op_removeGroupExperimenterMapFromBoth.end(self, _r)

        def linkedExperimenterGroupList(self, _ctx=None):
            return _M_ode.model.Experimenter._op_linkedExperimenterGroupList.invoke(self, ((), _ctx))

        def begin_linkedExperimenterGroupList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_linkedExperimenterGroupList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedExperimenterGroupList(self, _r):
            return _M_ode.model.Experimenter._op_linkedExperimenterGroupList.end(self, _r)

        def getOdeName(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getOdeName.invoke(self, ((), _ctx))

        def begin_getOdeName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getOdeName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getOdeName(self, _r):
            return _M_ode.model.Experimenter._op_getOdeName.end(self, _r)

        def setOdeName(self, theOdeName, _ctx=None):
            return _M_ode.model.Experimenter._op_setOdeName.invoke(self, ((theOdeName, ), _ctx))

        def begin_setOdeName(self, theOdeName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setOdeName.begin(self, ((theOdeName, ), _response, _ex, _sent, _ctx))

        def end_setOdeName(self, _r):
            return _M_ode.model.Experimenter._op_setOdeName.end(self, _r)

        def getFirstName(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getFirstName.invoke(self, ((), _ctx))

        def begin_getFirstName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getFirstName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFirstName(self, _r):
            return _M_ode.model.Experimenter._op_getFirstName.end(self, _r)

        def setFirstName(self, theFirstName, _ctx=None):
            return _M_ode.model.Experimenter._op_setFirstName.invoke(self, ((theFirstName, ), _ctx))

        def begin_setFirstName(self, theFirstName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setFirstName.begin(self, ((theFirstName, ), _response, _ex, _sent, _ctx))

        def end_setFirstName(self, _r):
            return _M_ode.model.Experimenter._op_setFirstName.end(self, _r)

        def getMiddleName(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getMiddleName.invoke(self, ((), _ctx))

        def begin_getMiddleName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getMiddleName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMiddleName(self, _r):
            return _M_ode.model.Experimenter._op_getMiddleName.end(self, _r)

        def setMiddleName(self, theMiddleName, _ctx=None):
            return _M_ode.model.Experimenter._op_setMiddleName.invoke(self, ((theMiddleName, ), _ctx))

        def begin_setMiddleName(self, theMiddleName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setMiddleName.begin(self, ((theMiddleName, ), _response, _ex, _sent, _ctx))

        def end_setMiddleName(self, _r):
            return _M_ode.model.Experimenter._op_setMiddleName.end(self, _r)

        def getLastName(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getLastName.invoke(self, ((), _ctx))

        def begin_getLastName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getLastName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLastName(self, _r):
            return _M_ode.model.Experimenter._op_getLastName.end(self, _r)

        def setLastName(self, theLastName, _ctx=None):
            return _M_ode.model.Experimenter._op_setLastName.invoke(self, ((theLastName, ), _ctx))

        def begin_setLastName(self, theLastName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setLastName.begin(self, ((theLastName, ), _response, _ex, _sent, _ctx))

        def end_setLastName(self, _r):
            return _M_ode.model.Experimenter._op_setLastName.end(self, _r)

        def getInstitution(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getInstitution.invoke(self, ((), _ctx))

        def begin_getInstitution(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getInstitution.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getInstitution(self, _r):
            return _M_ode.model.Experimenter._op_getInstitution.end(self, _r)

        def setInstitution(self, theInstitution, _ctx=None):
            return _M_ode.model.Experimenter._op_setInstitution.invoke(self, ((theInstitution, ), _ctx))

        def begin_setInstitution(self, theInstitution, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setInstitution.begin(self, ((theInstitution, ), _response, _ex, _sent, _ctx))

        def end_setInstitution(self, _r):
            return _M_ode.model.Experimenter._op_setInstitution.end(self, _r)

        def getLdap(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getLdap.invoke(self, ((), _ctx))

        def begin_getLdap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getLdap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLdap(self, _r):
            return _M_ode.model.Experimenter._op_getLdap.end(self, _r)

        def setLdap(self, theLdap, _ctx=None):
            return _M_ode.model.Experimenter._op_setLdap.invoke(self, ((theLdap, ), _ctx))

        def begin_setLdap(self, theLdap, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setLdap.begin(self, ((theLdap, ), _response, _ex, _sent, _ctx))

        def end_setLdap(self, _r):
            return _M_ode.model.Experimenter._op_setLdap.end(self, _r)

        def getEmail(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getEmail.invoke(self, ((), _ctx))

        def begin_getEmail(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getEmail.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEmail(self, _r):
            return _M_ode.model.Experimenter._op_getEmail.end(self, _r)

        def setEmail(self, theEmail, _ctx=None):
            return _M_ode.model.Experimenter._op_setEmail.invoke(self, ((theEmail, ), _ctx))

        def begin_setEmail(self, theEmail, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setEmail.begin(self, ((theEmail, ), _response, _ex, _sent, _ctx))

        def end_setEmail(self, _r):
            return _M_ode.model.Experimenter._op_setEmail.end(self, _r)

        def getConfigAsMap(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getConfigAsMap.invoke(self, ((), _ctx))

        def begin_getConfigAsMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getConfigAsMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getConfigAsMap(self, _r):
            return _M_ode.model.Experimenter._op_getConfigAsMap.end(self, _r)

        def getConfig(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getConfig.invoke(self, ((), _ctx))

        def begin_getConfig(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getConfig.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getConfig(self, _r):
            return _M_ode.model.Experimenter._op_getConfig.end(self, _r)

        def setConfig(self, theConfig, _ctx=None):
            return _M_ode.model.Experimenter._op_setConfig.invoke(self, ((theConfig, ), _ctx))

        def begin_setConfig(self, theConfig, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_setConfig.begin(self, ((theConfig, ), _response, _ex, _sent, _ctx))

        def end_setConfig(self, _r):
            return _M_ode.model.Experimenter._op_setConfig.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Experimenter._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Experimenter._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Experimenter._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Experimenter._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Experimenter._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Experimenter._op_copyAnnotationLinks.end(self, _r)

        def addExperimenterAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Experimenter._op_addExperimenterAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addExperimenterAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_addExperimenterAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addExperimenterAnnotationLink(self, _r):
            return _M_ode.model.Experimenter._op_addExperimenterAnnotationLink.end(self, _r)

        def addAllExperimenterAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Experimenter._op_addAllExperimenterAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllExperimenterAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_addAllExperimenterAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllExperimenterAnnotationLinkSet(self, _r):
            return _M_ode.model.Experimenter._op_addAllExperimenterAnnotationLinkSet.end(self, _r)

        def removeExperimenterAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Experimenter._op_removeExperimenterAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeExperimenterAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_removeExperimenterAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeExperimenterAnnotationLink(self, _r):
            return _M_ode.model.Experimenter._op_removeExperimenterAnnotationLink.end(self, _r)

        def removeAllExperimenterAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Experimenter._op_removeAllExperimenterAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllExperimenterAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_removeAllExperimenterAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllExperimenterAnnotationLinkSet(self, _r):
            return _M_ode.model.Experimenter._op_removeAllExperimenterAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Experimenter._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Experimenter._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Experimenter._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Experimenter._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Experimenter._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Experimenter._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Experimenter._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Experimenter._op_linkAnnotation.end(self, _r)

        def addExperimenterAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Experimenter._op_addExperimenterAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addExperimenterAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_addExperimenterAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addExperimenterAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Experimenter._op_addExperimenterAnnotationLinkToBoth.end(self, _r)

        def findExperimenterAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Experimenter._op_findExperimenterAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findExperimenterAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_findExperimenterAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findExperimenterAnnotationLink(self, _r):
            return _M_ode.model.Experimenter._op_findExperimenterAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Experimenter._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Experimenter._op_unlinkAnnotation.end(self, _r)

        def removeExperimenterAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Experimenter._op_removeExperimenterAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeExperimenterAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_removeExperimenterAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeExperimenterAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Experimenter._op_removeExperimenterAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Experimenter._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Experimenter._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Experimenter._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ExperimenterPrx.ice_checkedCast(proxy, '::ode::model::Experimenter', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ExperimenterPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Experimenter'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ExperimenterPrx = IcePy.defineProxy('::ode::model::Experimenter', ExperimenterPrx)

    _M_ode.model._t_Experimenter = IcePy.declareClass('::ode::model::Experimenter')

    _M_ode.model._t_Experimenter = IcePy.defineClass('::ode::model::Experimenter', Experimenter, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_groupExperimenterMapSeq', (), _M_ode.model._t_ExperimenterGroupExperimenterMapSeq, False, 0),
        ('_groupExperimenterMapLoaded', (), IcePy._t_bool, False, 0),
        ('_odeName', (), _M_ode._t_RString, False, 0),
        ('_firstName', (), _M_ode._t_RString, False, 0),
        ('_middleName', (), _M_ode._t_RString, False, 0),
        ('_lastName', (), _M_ode._t_RString, False, 0),
        ('_institution', (), _M_ode._t_RString, False, 0),
        ('_ldap', (), _M_ode._t_RBool, False, 0),
        ('_email', (), _M_ode._t_RString, False, 0),
        ('_config', (), _M_ode.api._t_NamedValueList, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_ExperimenterAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    Experimenter._ice_type = _M_ode.model._t_Experimenter

    Experimenter._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Experimenter._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Experimenter._op_unloadGroupExperimenterMap = IcePy.Operation('unloadGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Experimenter._op_sizeOfGroupExperimenterMap = IcePy.Operation('sizeOfGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Experimenter._op_copyGroupExperimenterMap = IcePy.Operation('copyGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ExperimenterGroupExperimenterMapSeq, False, 0), ())
    Experimenter._op_addGroupExperimenterMap = IcePy.Operation('addGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_GroupExperimenterMap, False, 0),), (), None, ())
    Experimenter._op_addAllGroupExperimenterMapSet = IcePy.Operation('addAllGroupExperimenterMapSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterGroupExperimenterMapSeq, False, 0),), (), None, ())
    Experimenter._op_removeGroupExperimenterMap = IcePy.Operation('removeGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_GroupExperimenterMap, False, 0),), (), None, ())
    Experimenter._op_removeAllGroupExperimenterMapSet = IcePy.Operation('removeAllGroupExperimenterMapSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterGroupExperimenterMapSeq, False, 0),), (), None, ())
    Experimenter._op_clearGroupExperimenterMap = IcePy.Operation('clearGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Experimenter._op_reloadGroupExperimenterMap = IcePy.Operation('reloadGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), None, ())
    Experimenter._op_getGroupExperimenterMap = IcePy.Operation('getGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_GroupExperimenterMap, False, 0), ())
    Experimenter._op_setGroupExperimenterMap = IcePy.Operation('setGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_GroupExperimenterMap, False, 0)), (), ((), _M_ode.model._t_GroupExperimenterMap, False, 0), ())
    Experimenter._op_getPrimaryGroupExperimenterMap = IcePy.Operation('getPrimaryGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_GroupExperimenterMap, False, 0), ())
    Experimenter._op_setPrimaryGroupExperimenterMap = IcePy.Operation('setPrimaryGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_GroupExperimenterMap, False, 0),), (), ((), _M_ode.model._t_GroupExperimenterMap, False, 0), ())
    Experimenter._op_linkExperimenterGroup = IcePy.Operation('linkExperimenterGroup', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0),), (), ((), _M_ode.model._t_GroupExperimenterMap, False, 0), ())
    Experimenter._op_addGroupExperimenterMapToBoth = IcePy.Operation('addGroupExperimenterMapToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_GroupExperimenterMap, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Experimenter._op_findGroupExperimenterMap = IcePy.Operation('findGroupExperimenterMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0),), (), ((), _M_ode.model._t_ExperimenterGroupExperimenterMapSeq, False, 0), ())
    Experimenter._op_unlinkExperimenterGroup = IcePy.Operation('unlinkExperimenterGroup', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0),), (), None, ())
    Experimenter._op_removeGroupExperimenterMapFromBoth = IcePy.Operation('removeGroupExperimenterMapFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_GroupExperimenterMap, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Experimenter._op_linkedExperimenterGroupList = IcePy.Operation('linkedExperimenterGroupList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ExperimenterLinkedExperimenterGroupSeq, False, 0), ())
    Experimenter._op_getOdeName = IcePy.Operation('getOdeName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Experimenter._op_setOdeName = IcePy.Operation('setOdeName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Experimenter._op_getFirstName = IcePy.Operation('getFirstName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Experimenter._op_setFirstName = IcePy.Operation('setFirstName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Experimenter._op_getMiddleName = IcePy.Operation('getMiddleName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Experimenter._op_setMiddleName = IcePy.Operation('setMiddleName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Experimenter._op_getLastName = IcePy.Operation('getLastName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Experimenter._op_setLastName = IcePy.Operation('setLastName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Experimenter._op_getInstitution = IcePy.Operation('getInstitution', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Experimenter._op_setInstitution = IcePy.Operation('setInstitution', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Experimenter._op_getLdap = IcePy.Operation('getLdap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    Experimenter._op_setLdap = IcePy.Operation('setLdap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    Experimenter._op_getEmail = IcePy.Operation('getEmail', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Experimenter._op_setEmail = IcePy.Operation('setEmail', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Experimenter._op_getConfigAsMap = IcePy.Operation('getConfigAsMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_StringStringMap, False, 0), ())
    Experimenter._op_getConfig = IcePy.Operation('getConfig', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_NamedValueList, False, 0), ())
    Experimenter._op_setConfig = IcePy.Operation('setConfig', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_NamedValueList, False, 0),), (), None, ())
    Experimenter._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Experimenter._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Experimenter._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ExperimenterAnnotationLinksSeq, False, 0), ())
    Experimenter._op_addExperimenterAnnotationLink = IcePy.Operation('addExperimenterAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterAnnotationLink, False, 0),), (), None, ())
    Experimenter._op_addAllExperimenterAnnotationLinkSet = IcePy.Operation('addAllExperimenterAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterAnnotationLinksSeq, False, 0),), (), None, ())
    Experimenter._op_removeExperimenterAnnotationLink = IcePy.Operation('removeExperimenterAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterAnnotationLink, False, 0),), (), None, ())
    Experimenter._op_removeAllExperimenterAnnotationLinkSet = IcePy.Operation('removeAllExperimenterAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterAnnotationLinksSeq, False, 0),), (), None, ())
    Experimenter._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Experimenter._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), None, ())
    Experimenter._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Experimenter._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ExperimenterAnnotationLink, False, 0), ())
    Experimenter._op_addExperimenterAnnotationLinkToBoth = IcePy.Operation('addExperimenterAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Experimenter._op_findExperimenterAnnotationLink = IcePy.Operation('findExperimenterAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ExperimenterAnnotationLinksSeq, False, 0), ())
    Experimenter._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Experimenter._op_removeExperimenterAnnotationLinkFromBoth = IcePy.Operation('removeExperimenterAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Experimenter._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ExperimenterLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.Experimenter = Experimenter
    del Experimenter

    _M_ode.model.ExperimenterPrx = ExperimenterPrx
    del ExperimenterPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
