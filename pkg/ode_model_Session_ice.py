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

if 'Node' not in _M_ode.model.__dict__:
    _M_ode.model._t_Node = IcePy.declareClass('::ode::model::Node')
    _M_ode.model._t_NodePrx = IcePy.declareProxy('::ode::model::Node')

if 'Experimenter' not in _M_ode.model.__dict__:
    _M_ode.model._t_Experimenter = IcePy.declareClass('::ode::model::Experimenter')
    _M_ode.model._t_ExperimenterPrx = IcePy.declareProxy('::ode::model::Experimenter')

if 'Event' not in _M_ode.model.__dict__:
    _M_ode.model._t_Event = IcePy.declareClass('::ode::model::Event')
    _M_ode.model._t_EventPrx = IcePy.declareProxy('::ode::model::Event')

if 'SessionAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_SessionAnnotationLink = IcePy.declareClass('::ode::model::SessionAnnotationLink')
    _M_ode.model._t_SessionAnnotationLinkPrx = IcePy.declareProxy('::ode::model::SessionAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_SessionEventsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_SessionEventsSeq = IcePy.defineSequence('::ode::model::SessionEventsSeq', (), _M_ode.model._t_Event)

if '_t_SessionAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_SessionAnnotationLinksSeq = IcePy.defineSequence('::ode::model::SessionAnnotationLinksSeq', (), _M_ode.model._t_SessionAnnotationLink)

if '_t_SessionLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_SessionLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::SessionLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Session' not in _M_ode.model.__dict__:
    _M_ode.model.Session = Ice.createTempClass()
    class Session(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _node=None, _uuid=None, _owner=None, _sudoer=None, _timeToIdle=None, _timeToLive=None, _started=None, _closed=None, _message=None, _defaultEventType=None, _userAgent=None, _userIP=None, _eventsSeq=None, _eventsLoaded=False, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.Session:
                raise RuntimeError('ode.model.Session is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._node = _node
            self._uuid = _uuid
            self._owner = _owner
            self._sudoer = _sudoer
            self._timeToIdle = _timeToIdle
            self._timeToLive = _timeToLive
            self._started = _started
            self._closed = _closed
            self._message = _message
            self._defaultEventType = _defaultEventType
            self._userAgent = _userAgent
            self._userIP = _userIP
            self._eventsSeq = _eventsSeq
            self._eventsLoaded = _eventsLoaded
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Session')

        def ice_id(self, current=None):
            return '::ode::model::Session'

        def ice_staticId():
            return '::ode::model::Session'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getNode(self, current=None):
            pass

        def setNode(self, theNode, current=None):
            pass

        def getUuid(self, current=None):
            pass

        def setUuid(self, theUuid, current=None):
            pass

        def getOwner(self, current=None):
            pass

        def setOwner(self, theOwner, current=None):
            pass

        def getSudoer(self, current=None):
            pass

        def setSudoer(self, theSudoer, current=None):
            pass

        def getTimeToIdle(self, current=None):
            pass

        def setTimeToIdle(self, theTimeToIdle, current=None):
            pass

        def getTimeToLive(self, current=None):
            pass

        def setTimeToLive(self, theTimeToLive, current=None):
            pass

        def getStarted(self, current=None):
            pass

        def setStarted(self, theStarted, current=None):
            pass

        def getClosed(self, current=None):
            pass

        def setClosed(self, theClosed, current=None):
            pass

        def getMessage(self, current=None):
            pass

        def setMessage(self, theMessage, current=None):
            pass

        def getDefaultEventType(self, current=None):
            pass

        def setDefaultEventType(self, theDefaultEventType, current=None):
            pass

        def getUserAgent(self, current=None):
            pass

        def setUserAgent(self, theUserAgent, current=None):
            pass

        def getUserIP(self, current=None):
            pass

        def setUserIP(self, theUserIP, current=None):
            pass

        def unloadEvents(self, current=None):
            pass

        def sizeOfEvents(self, current=None):
            pass

        def copyEvents(self, current=None):
            pass

        def addEvent(self, target, current=None):
            pass

        def addAllEventSet(self, targets, current=None):
            pass

        def removeEvent(self, theTarget, current=None):
            pass

        def removeAllEventSet(self, targets, current=None):
            pass

        def clearEvents(self, current=None):
            pass

        def reloadEvents(self, toCopy, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addSessionAnnotationLink(self, target, current=None):
            pass

        def addAllSessionAnnotationLinkSet(self, targets, current=None):
            pass

        def removeSessionAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllSessionAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addSessionAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findSessionAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeSessionAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Session)

        __repr__ = __str__

    _M_ode.model.SessionPrx = Ice.createTempClass()
    class SessionPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Session._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Session._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Session._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Session._op_setVersion.end(self, _r)

        def getNode(self, _ctx=None):
            return _M_ode.model.Session._op_getNode.invoke(self, ((), _ctx))

        def begin_getNode(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getNode.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getNode(self, _r):
            return _M_ode.model.Session._op_getNode.end(self, _r)

        def setNode(self, theNode, _ctx=None):
            return _M_ode.model.Session._op_setNode.invoke(self, ((theNode, ), _ctx))

        def begin_setNode(self, theNode, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setNode.begin(self, ((theNode, ), _response, _ex, _sent, _ctx))

        def end_setNode(self, _r):
            return _M_ode.model.Session._op_setNode.end(self, _r)

        def getUuid(self, _ctx=None):
            return _M_ode.model.Session._op_getUuid.invoke(self, ((), _ctx))

        def begin_getUuid(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getUuid.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getUuid(self, _r):
            return _M_ode.model.Session._op_getUuid.end(self, _r)

        def setUuid(self, theUuid, _ctx=None):
            return _M_ode.model.Session._op_setUuid.invoke(self, ((theUuid, ), _ctx))

        def begin_setUuid(self, theUuid, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setUuid.begin(self, ((theUuid, ), _response, _ex, _sent, _ctx))

        def end_setUuid(self, _r):
            return _M_ode.model.Session._op_setUuid.end(self, _r)

        def getOwner(self, _ctx=None):
            return _M_ode.model.Session._op_getOwner.invoke(self, ((), _ctx))

        def begin_getOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getOwner(self, _r):
            return _M_ode.model.Session._op_getOwner.end(self, _r)

        def setOwner(self, theOwner, _ctx=None):
            return _M_ode.model.Session._op_setOwner.invoke(self, ((theOwner, ), _ctx))

        def begin_setOwner(self, theOwner, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setOwner.begin(self, ((theOwner, ), _response, _ex, _sent, _ctx))

        def end_setOwner(self, _r):
            return _M_ode.model.Session._op_setOwner.end(self, _r)

        def getSudoer(self, _ctx=None):
            return _M_ode.model.Session._op_getSudoer.invoke(self, ((), _ctx))

        def begin_getSudoer(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getSudoer.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSudoer(self, _r):
            return _M_ode.model.Session._op_getSudoer.end(self, _r)

        def setSudoer(self, theSudoer, _ctx=None):
            return _M_ode.model.Session._op_setSudoer.invoke(self, ((theSudoer, ), _ctx))

        def begin_setSudoer(self, theSudoer, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setSudoer.begin(self, ((theSudoer, ), _response, _ex, _sent, _ctx))

        def end_setSudoer(self, _r):
            return _M_ode.model.Session._op_setSudoer.end(self, _r)

        def getTimeToIdle(self, _ctx=None):
            return _M_ode.model.Session._op_getTimeToIdle.invoke(self, ((), _ctx))

        def begin_getTimeToIdle(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getTimeToIdle.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTimeToIdle(self, _r):
            return _M_ode.model.Session._op_getTimeToIdle.end(self, _r)

        def setTimeToIdle(self, theTimeToIdle, _ctx=None):
            return _M_ode.model.Session._op_setTimeToIdle.invoke(self, ((theTimeToIdle, ), _ctx))

        def begin_setTimeToIdle(self, theTimeToIdle, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setTimeToIdle.begin(self, ((theTimeToIdle, ), _response, _ex, _sent, _ctx))

        def end_setTimeToIdle(self, _r):
            return _M_ode.model.Session._op_setTimeToIdle.end(self, _r)

        def getTimeToLive(self, _ctx=None):
            return _M_ode.model.Session._op_getTimeToLive.invoke(self, ((), _ctx))

        def begin_getTimeToLive(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getTimeToLive.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTimeToLive(self, _r):
            return _M_ode.model.Session._op_getTimeToLive.end(self, _r)

        def setTimeToLive(self, theTimeToLive, _ctx=None):
            return _M_ode.model.Session._op_setTimeToLive.invoke(self, ((theTimeToLive, ), _ctx))

        def begin_setTimeToLive(self, theTimeToLive, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setTimeToLive.begin(self, ((theTimeToLive, ), _response, _ex, _sent, _ctx))

        def end_setTimeToLive(self, _r):
            return _M_ode.model.Session._op_setTimeToLive.end(self, _r)

        def getStarted(self, _ctx=None):
            return _M_ode.model.Session._op_getStarted.invoke(self, ((), _ctx))

        def begin_getStarted(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getStarted.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStarted(self, _r):
            return _M_ode.model.Session._op_getStarted.end(self, _r)

        def setStarted(self, theStarted, _ctx=None):
            return _M_ode.model.Session._op_setStarted.invoke(self, ((theStarted, ), _ctx))

        def begin_setStarted(self, theStarted, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setStarted.begin(self, ((theStarted, ), _response, _ex, _sent, _ctx))

        def end_setStarted(self, _r):
            return _M_ode.model.Session._op_setStarted.end(self, _r)

        def getClosed(self, _ctx=None):
            return _M_ode.model.Session._op_getClosed.invoke(self, ((), _ctx))

        def begin_getClosed(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getClosed.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getClosed(self, _r):
            return _M_ode.model.Session._op_getClosed.end(self, _r)

        def setClosed(self, theClosed, _ctx=None):
            return _M_ode.model.Session._op_setClosed.invoke(self, ((theClosed, ), _ctx))

        def begin_setClosed(self, theClosed, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setClosed.begin(self, ((theClosed, ), _response, _ex, _sent, _ctx))

        def end_setClosed(self, _r):
            return _M_ode.model.Session._op_setClosed.end(self, _r)

        def getMessage(self, _ctx=None):
            return _M_ode.model.Session._op_getMessage.invoke(self, ((), _ctx))

        def begin_getMessage(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getMessage.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMessage(self, _r):
            return _M_ode.model.Session._op_getMessage.end(self, _r)

        def setMessage(self, theMessage, _ctx=None):
            return _M_ode.model.Session._op_setMessage.invoke(self, ((theMessage, ), _ctx))

        def begin_setMessage(self, theMessage, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setMessage.begin(self, ((theMessage, ), _response, _ex, _sent, _ctx))

        def end_setMessage(self, _r):
            return _M_ode.model.Session._op_setMessage.end(self, _r)

        def getDefaultEventType(self, _ctx=None):
            return _M_ode.model.Session._op_getDefaultEventType.invoke(self, ((), _ctx))

        def begin_getDefaultEventType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getDefaultEventType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDefaultEventType(self, _r):
            return _M_ode.model.Session._op_getDefaultEventType.end(self, _r)

        def setDefaultEventType(self, theDefaultEventType, _ctx=None):
            return _M_ode.model.Session._op_setDefaultEventType.invoke(self, ((theDefaultEventType, ), _ctx))

        def begin_setDefaultEventType(self, theDefaultEventType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setDefaultEventType.begin(self, ((theDefaultEventType, ), _response, _ex, _sent, _ctx))

        def end_setDefaultEventType(self, _r):
            return _M_ode.model.Session._op_setDefaultEventType.end(self, _r)

        def getUserAgent(self, _ctx=None):
            return _M_ode.model.Session._op_getUserAgent.invoke(self, ((), _ctx))

        def begin_getUserAgent(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getUserAgent.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getUserAgent(self, _r):
            return _M_ode.model.Session._op_getUserAgent.end(self, _r)

        def setUserAgent(self, theUserAgent, _ctx=None):
            return _M_ode.model.Session._op_setUserAgent.invoke(self, ((theUserAgent, ), _ctx))

        def begin_setUserAgent(self, theUserAgent, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setUserAgent.begin(self, ((theUserAgent, ), _response, _ex, _sent, _ctx))

        def end_setUserAgent(self, _r):
            return _M_ode.model.Session._op_setUserAgent.end(self, _r)

        def getUserIP(self, _ctx=None):
            return _M_ode.model.Session._op_getUserIP.invoke(self, ((), _ctx))

        def begin_getUserIP(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getUserIP.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getUserIP(self, _r):
            return _M_ode.model.Session._op_getUserIP.end(self, _r)

        def setUserIP(self, theUserIP, _ctx=None):
            return _M_ode.model.Session._op_setUserIP.invoke(self, ((theUserIP, ), _ctx))

        def begin_setUserIP(self, theUserIP, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_setUserIP.begin(self, ((theUserIP, ), _response, _ex, _sent, _ctx))

        def end_setUserIP(self, _r):
            return _M_ode.model.Session._op_setUserIP.end(self, _r)

        def unloadEvents(self, _ctx=None):
            return _M_ode.model.Session._op_unloadEvents.invoke(self, ((), _ctx))

        def begin_unloadEvents(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_unloadEvents.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadEvents(self, _r):
            return _M_ode.model.Session._op_unloadEvents.end(self, _r)

        def sizeOfEvents(self, _ctx=None):
            return _M_ode.model.Session._op_sizeOfEvents.invoke(self, ((), _ctx))

        def begin_sizeOfEvents(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_sizeOfEvents.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfEvents(self, _r):
            return _M_ode.model.Session._op_sizeOfEvents.end(self, _r)

        def copyEvents(self, _ctx=None):
            return _M_ode.model.Session._op_copyEvents.invoke(self, ((), _ctx))

        def begin_copyEvents(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_copyEvents.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyEvents(self, _r):
            return _M_ode.model.Session._op_copyEvents.end(self, _r)

        def addEvent(self, target, _ctx=None):
            return _M_ode.model.Session._op_addEvent.invoke(self, ((target, ), _ctx))

        def begin_addEvent(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_addEvent.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addEvent(self, _r):
            return _M_ode.model.Session._op_addEvent.end(self, _r)

        def addAllEventSet(self, targets, _ctx=None):
            return _M_ode.model.Session._op_addAllEventSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllEventSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_addAllEventSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllEventSet(self, _r):
            return _M_ode.model.Session._op_addAllEventSet.end(self, _r)

        def removeEvent(self, theTarget, _ctx=None):
            return _M_ode.model.Session._op_removeEvent.invoke(self, ((theTarget, ), _ctx))

        def begin_removeEvent(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_removeEvent.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeEvent(self, _r):
            return _M_ode.model.Session._op_removeEvent.end(self, _r)

        def removeAllEventSet(self, targets, _ctx=None):
            return _M_ode.model.Session._op_removeAllEventSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllEventSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_removeAllEventSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllEventSet(self, _r):
            return _M_ode.model.Session._op_removeAllEventSet.end(self, _r)

        def clearEvents(self, _ctx=None):
            return _M_ode.model.Session._op_clearEvents.invoke(self, ((), _ctx))

        def begin_clearEvents(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_clearEvents.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearEvents(self, _r):
            return _M_ode.model.Session._op_clearEvents.end(self, _r)

        def reloadEvents(self, toCopy, _ctx=None):
            return _M_ode.model.Session._op_reloadEvents.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadEvents(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_reloadEvents.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadEvents(self, _r):
            return _M_ode.model.Session._op_reloadEvents.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Session._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Session._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Session._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Session._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Session._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Session._op_copyAnnotationLinks.end(self, _r)

        def addSessionAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Session._op_addSessionAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addSessionAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_addSessionAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addSessionAnnotationLink(self, _r):
            return _M_ode.model.Session._op_addSessionAnnotationLink.end(self, _r)

        def addAllSessionAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Session._op_addAllSessionAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllSessionAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_addAllSessionAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllSessionAnnotationLinkSet(self, _r):
            return _M_ode.model.Session._op_addAllSessionAnnotationLinkSet.end(self, _r)

        def removeSessionAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Session._op_removeSessionAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeSessionAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_removeSessionAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeSessionAnnotationLink(self, _r):
            return _M_ode.model.Session._op_removeSessionAnnotationLink.end(self, _r)

        def removeAllSessionAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Session._op_removeAllSessionAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllSessionAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_removeAllSessionAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllSessionAnnotationLinkSet(self, _r):
            return _M_ode.model.Session._op_removeAllSessionAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Session._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Session._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Session._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Session._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Session._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Session._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Session._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Session._op_linkAnnotation.end(self, _r)

        def addSessionAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Session._op_addSessionAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addSessionAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_addSessionAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addSessionAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Session._op_addSessionAnnotationLinkToBoth.end(self, _r)

        def findSessionAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Session._op_findSessionAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findSessionAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_findSessionAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findSessionAnnotationLink(self, _r):
            return _M_ode.model.Session._op_findSessionAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Session._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Session._op_unlinkAnnotation.end(self, _r)

        def removeSessionAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Session._op_removeSessionAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeSessionAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_removeSessionAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeSessionAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Session._op_removeSessionAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Session._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Session._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Session._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.SessionPrx.ice_checkedCast(proxy, '::ode::model::Session', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.SessionPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Session'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_SessionPrx = IcePy.defineProxy('::ode::model::Session', SessionPrx)

    _M_ode.model._t_Session = IcePy.declareClass('::ode::model::Session')

    _M_ode.model._t_Session = IcePy.defineClass('::ode::model::Session', Session, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_node', (), _M_ode.model._t_Node, False, 0),
        ('_uuid', (), _M_ode._t_RString, False, 0),
        ('_owner', (), _M_ode.model._t_Experimenter, False, 0),
        ('_sudoer', (), _M_ode.model._t_Experimenter, False, 0),
        ('_timeToIdle', (), _M_ode._t_RLong, False, 0),
        ('_timeToLive', (), _M_ode._t_RLong, False, 0),
        ('_started', (), _M_ode._t_RTime, False, 0),
        ('_closed', (), _M_ode._t_RTime, False, 0),
        ('_message', (), _M_ode._t_RString, False, 0),
        ('_defaultEventType', (), _M_ode._t_RString, False, 0),
        ('_userAgent', (), _M_ode._t_RString, False, 0),
        ('_userIP', (), _M_ode._t_RString, False, 0),
        ('_eventsSeq', (), _M_ode.model._t_SessionEventsSeq, False, 0),
        ('_eventsLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_SessionAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    Session._ice_type = _M_ode.model._t_Session

    Session._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Session._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Session._op_getNode = IcePy.Operation('getNode', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Node, False, 0), ())
    Session._op_setNode = IcePy.Operation('setNode', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Node, False, 0),), (), None, ())
    Session._op_getUuid = IcePy.Operation('getUuid', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Session._op_setUuid = IcePy.Operation('setUuid', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Session._op_getOwner = IcePy.Operation('getOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Experimenter, False, 0), ())
    Session._op_setOwner = IcePy.Operation('setOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), None, ())
    Session._op_getSudoer = IcePy.Operation('getSudoer', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Experimenter, False, 0), ())
    Session._op_setSudoer = IcePy.Operation('setSudoer', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), None, ())
    Session._op_getTimeToIdle = IcePy.Operation('getTimeToIdle', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RLong, False, 0), ())
    Session._op_setTimeToIdle = IcePy.Operation('setTimeToIdle', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RLong, False, 0),), (), None, ())
    Session._op_getTimeToLive = IcePy.Operation('getTimeToLive', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RLong, False, 0), ())
    Session._op_setTimeToLive = IcePy.Operation('setTimeToLive', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RLong, False, 0),), (), None, ())
    Session._op_getStarted = IcePy.Operation('getStarted', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    Session._op_setStarted = IcePy.Operation('setStarted', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    Session._op_getClosed = IcePy.Operation('getClosed', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    Session._op_setClosed = IcePy.Operation('setClosed', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    Session._op_getMessage = IcePy.Operation('getMessage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Session._op_setMessage = IcePy.Operation('setMessage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Session._op_getDefaultEventType = IcePy.Operation('getDefaultEventType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Session._op_setDefaultEventType = IcePy.Operation('setDefaultEventType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Session._op_getUserAgent = IcePy.Operation('getUserAgent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Session._op_setUserAgent = IcePy.Operation('setUserAgent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Session._op_getUserIP = IcePy.Operation('getUserIP', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Session._op_setUserIP = IcePy.Operation('setUserIP', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Session._op_unloadEvents = IcePy.Operation('unloadEvents', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Session._op_sizeOfEvents = IcePy.Operation('sizeOfEvents', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Session._op_copyEvents = IcePy.Operation('copyEvents', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_SessionEventsSeq, False, 0), ())
    Session._op_addEvent = IcePy.Operation('addEvent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Event, False, 0),), (), None, ())
    Session._op_addAllEventSet = IcePy.Operation('addAllEventSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_SessionEventsSeq, False, 0),), (), None, ())
    Session._op_removeEvent = IcePy.Operation('removeEvent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Event, False, 0),), (), None, ())
    Session._op_removeAllEventSet = IcePy.Operation('removeAllEventSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_SessionEventsSeq, False, 0),), (), None, ())
    Session._op_clearEvents = IcePy.Operation('clearEvents', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Session._op_reloadEvents = IcePy.Operation('reloadEvents', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Session, False, 0),), (), None, ())
    Session._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Session._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Session._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_SessionAnnotationLinksSeq, False, 0), ())
    Session._op_addSessionAnnotationLink = IcePy.Operation('addSessionAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_SessionAnnotationLink, False, 0),), (), None, ())
    Session._op_addAllSessionAnnotationLinkSet = IcePy.Operation('addAllSessionAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_SessionAnnotationLinksSeq, False, 0),), (), None, ())
    Session._op_removeSessionAnnotationLink = IcePy.Operation('removeSessionAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_SessionAnnotationLink, False, 0),), (), None, ())
    Session._op_removeAllSessionAnnotationLinkSet = IcePy.Operation('removeAllSessionAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_SessionAnnotationLinksSeq, False, 0),), (), None, ())
    Session._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Session._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Session, False, 0),), (), None, ())
    Session._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Session._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_SessionAnnotationLink, False, 0), ())
    Session._op_addSessionAnnotationLinkToBoth = IcePy.Operation('addSessionAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_SessionAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Session._op_findSessionAnnotationLink = IcePy.Operation('findSessionAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_SessionAnnotationLinksSeq, False, 0), ())
    Session._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Session._op_removeSessionAnnotationLinkFromBoth = IcePy.Operation('removeSessionAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_SessionAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Session._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_SessionLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.Session = Session
    del Session

    _M_ode.model.SessionPrx = SessionPrx
    del SessionPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
