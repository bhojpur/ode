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

if 'Session' not in _M_ode.model.__dict__:
    _M_ode.model._t_Session = IcePy.declareClass('::ode::model::Session')
    _M_ode.model._t_SessionPrx = IcePy.declareProxy('::ode::model::Session')

if 'NodeAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_NodeAnnotationLink = IcePy.declareClass('::ode::model::NodeAnnotationLink')
    _M_ode.model._t_NodeAnnotationLinkPrx = IcePy.declareProxy('::ode::model::NodeAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_NodeSessionsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_NodeSessionsSeq = IcePy.defineSequence('::ode::model::NodeSessionsSeq', (), _M_ode.model._t_Session)

if '_t_NodeAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_NodeAnnotationLinksSeq = IcePy.defineSequence('::ode::model::NodeAnnotationLinksSeq', (), _M_ode.model._t_NodeAnnotationLink)

if '_t_NodeLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_NodeLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::NodeLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Node' not in _M_ode.model.__dict__:
    _M_ode.model.Node = Ice.createTempClass()
    class Node(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _sessionsSeq=None, _sessionsLoaded=False, _uuid=None, _conn=None, _up=None, _down=None, _scale=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.Node:
                raise RuntimeError('ode.model.Node is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._sessionsSeq = _sessionsSeq
            self._sessionsLoaded = _sessionsLoaded
            self._uuid = _uuid
            self._conn = _conn
            self._up = _up
            self._down = _down
            self._scale = _scale
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Node')

        def ice_id(self, current=None):
            return '::ode::model::Node'

        def ice_staticId():
            return '::ode::model::Node'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def unloadSessions(self, current=None):
            pass

        def sizeOfSessions(self, current=None):
            pass

        def copySessions(self, current=None):
            pass

        def addSession(self, target, current=None):
            pass

        def addAllSessionSet(self, targets, current=None):
            pass

        def removeSession(self, theTarget, current=None):
            pass

        def removeAllSessionSet(self, targets, current=None):
            pass

        def clearSessions(self, current=None):
            pass

        def reloadSessions(self, toCopy, current=None):
            pass

        def getUuid(self, current=None):
            pass

        def setUuid(self, theUuid, current=None):
            pass

        def getConn(self, current=None):
            pass

        def setConn(self, theConn, current=None):
            pass

        def getUp(self, current=None):
            pass

        def setUp(self, theUp, current=None):
            pass

        def getDown(self, current=None):
            pass

        def setDown(self, theDown, current=None):
            pass

        def getScale(self, current=None):
            pass

        def setScale(self, theScale, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addNodeAnnotationLink(self, target, current=None):
            pass

        def addAllNodeAnnotationLinkSet(self, targets, current=None):
            pass

        def removeNodeAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllNodeAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addNodeAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findNodeAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeNodeAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Node)

        __repr__ = __str__

    _M_ode.model.NodePrx = Ice.createTempClass()
    class NodePrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Node._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Node._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Node._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Node._op_setVersion.end(self, _r)

        def unloadSessions(self, _ctx=None):
            return _M_ode.model.Node._op_unloadSessions.invoke(self, ((), _ctx))

        def begin_unloadSessions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_unloadSessions.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadSessions(self, _r):
            return _M_ode.model.Node._op_unloadSessions.end(self, _r)

        def sizeOfSessions(self, _ctx=None):
            return _M_ode.model.Node._op_sizeOfSessions.invoke(self, ((), _ctx))

        def begin_sizeOfSessions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_sizeOfSessions.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfSessions(self, _r):
            return _M_ode.model.Node._op_sizeOfSessions.end(self, _r)

        def copySessions(self, _ctx=None):
            return _M_ode.model.Node._op_copySessions.invoke(self, ((), _ctx))

        def begin_copySessions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_copySessions.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copySessions(self, _r):
            return _M_ode.model.Node._op_copySessions.end(self, _r)

        def addSession(self, target, _ctx=None):
            return _M_ode.model.Node._op_addSession.invoke(self, ((target, ), _ctx))

        def begin_addSession(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_addSession.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addSession(self, _r):
            return _M_ode.model.Node._op_addSession.end(self, _r)

        def addAllSessionSet(self, targets, _ctx=None):
            return _M_ode.model.Node._op_addAllSessionSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllSessionSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_addAllSessionSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllSessionSet(self, _r):
            return _M_ode.model.Node._op_addAllSessionSet.end(self, _r)

        def removeSession(self, theTarget, _ctx=None):
            return _M_ode.model.Node._op_removeSession.invoke(self, ((theTarget, ), _ctx))

        def begin_removeSession(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_removeSession.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeSession(self, _r):
            return _M_ode.model.Node._op_removeSession.end(self, _r)

        def removeAllSessionSet(self, targets, _ctx=None):
            return _M_ode.model.Node._op_removeAllSessionSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllSessionSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_removeAllSessionSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllSessionSet(self, _r):
            return _M_ode.model.Node._op_removeAllSessionSet.end(self, _r)

        def clearSessions(self, _ctx=None):
            return _M_ode.model.Node._op_clearSessions.invoke(self, ((), _ctx))

        def begin_clearSessions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_clearSessions.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearSessions(self, _r):
            return _M_ode.model.Node._op_clearSessions.end(self, _r)

        def reloadSessions(self, toCopy, _ctx=None):
            return _M_ode.model.Node._op_reloadSessions.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadSessions(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_reloadSessions.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadSessions(self, _r):
            return _M_ode.model.Node._op_reloadSessions.end(self, _r)

        def getUuid(self, _ctx=None):
            return _M_ode.model.Node._op_getUuid.invoke(self, ((), _ctx))

        def begin_getUuid(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_getUuid.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getUuid(self, _r):
            return _M_ode.model.Node._op_getUuid.end(self, _r)

        def setUuid(self, theUuid, _ctx=None):
            return _M_ode.model.Node._op_setUuid.invoke(self, ((theUuid, ), _ctx))

        def begin_setUuid(self, theUuid, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_setUuid.begin(self, ((theUuid, ), _response, _ex, _sent, _ctx))

        def end_setUuid(self, _r):
            return _M_ode.model.Node._op_setUuid.end(self, _r)

        def getConn(self, _ctx=None):
            return _M_ode.model.Node._op_getConn.invoke(self, ((), _ctx))

        def begin_getConn(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_getConn.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getConn(self, _r):
            return _M_ode.model.Node._op_getConn.end(self, _r)

        def setConn(self, theConn, _ctx=None):
            return _M_ode.model.Node._op_setConn.invoke(self, ((theConn, ), _ctx))

        def begin_setConn(self, theConn, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_setConn.begin(self, ((theConn, ), _response, _ex, _sent, _ctx))

        def end_setConn(self, _r):
            return _M_ode.model.Node._op_setConn.end(self, _r)

        def getUp(self, _ctx=None):
            return _M_ode.model.Node._op_getUp.invoke(self, ((), _ctx))

        def begin_getUp(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_getUp.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getUp(self, _r):
            return _M_ode.model.Node._op_getUp.end(self, _r)

        def setUp(self, theUp, _ctx=None):
            return _M_ode.model.Node._op_setUp.invoke(self, ((theUp, ), _ctx))

        def begin_setUp(self, theUp, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_setUp.begin(self, ((theUp, ), _response, _ex, _sent, _ctx))

        def end_setUp(self, _r):
            return _M_ode.model.Node._op_setUp.end(self, _r)

        def getDown(self, _ctx=None):
            return _M_ode.model.Node._op_getDown.invoke(self, ((), _ctx))

        def begin_getDown(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_getDown.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDown(self, _r):
            return _M_ode.model.Node._op_getDown.end(self, _r)

        def setDown(self, theDown, _ctx=None):
            return _M_ode.model.Node._op_setDown.invoke(self, ((theDown, ), _ctx))

        def begin_setDown(self, theDown, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_setDown.begin(self, ((theDown, ), _response, _ex, _sent, _ctx))

        def end_setDown(self, _r):
            return _M_ode.model.Node._op_setDown.end(self, _r)

        def getScale(self, _ctx=None):
            return _M_ode.model.Node._op_getScale.invoke(self, ((), _ctx))

        def begin_getScale(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_getScale.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getScale(self, _r):
            return _M_ode.model.Node._op_getScale.end(self, _r)

        def setScale(self, theScale, _ctx=None):
            return _M_ode.model.Node._op_setScale.invoke(self, ((theScale, ), _ctx))

        def begin_setScale(self, theScale, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_setScale.begin(self, ((theScale, ), _response, _ex, _sent, _ctx))

        def end_setScale(self, _r):
            return _M_ode.model.Node._op_setScale.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Node._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Node._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Node._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Node._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Node._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Node._op_copyAnnotationLinks.end(self, _r)

        def addNodeAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Node._op_addNodeAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addNodeAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_addNodeAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addNodeAnnotationLink(self, _r):
            return _M_ode.model.Node._op_addNodeAnnotationLink.end(self, _r)

        def addAllNodeAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Node._op_addAllNodeAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllNodeAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_addAllNodeAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllNodeAnnotationLinkSet(self, _r):
            return _M_ode.model.Node._op_addAllNodeAnnotationLinkSet.end(self, _r)

        def removeNodeAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Node._op_removeNodeAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeNodeAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_removeNodeAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeNodeAnnotationLink(self, _r):
            return _M_ode.model.Node._op_removeNodeAnnotationLink.end(self, _r)

        def removeAllNodeAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Node._op_removeAllNodeAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllNodeAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_removeAllNodeAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllNodeAnnotationLinkSet(self, _r):
            return _M_ode.model.Node._op_removeAllNodeAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Node._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Node._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Node._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Node._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Node._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Node._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Node._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Node._op_linkAnnotation.end(self, _r)

        def addNodeAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Node._op_addNodeAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addNodeAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_addNodeAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addNodeAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Node._op_addNodeAnnotationLinkToBoth.end(self, _r)

        def findNodeAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Node._op_findNodeAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findNodeAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_findNodeAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findNodeAnnotationLink(self, _r):
            return _M_ode.model.Node._op_findNodeAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Node._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Node._op_unlinkAnnotation.end(self, _r)

        def removeNodeAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Node._op_removeNodeAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeNodeAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_removeNodeAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeNodeAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Node._op_removeNodeAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Node._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Node._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Node._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.NodePrx.ice_checkedCast(proxy, '::ode::model::Node', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.NodePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Node'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_NodePrx = IcePy.defineProxy('::ode::model::Node', NodePrx)

    _M_ode.model._t_Node = IcePy.declareClass('::ode::model::Node')

    _M_ode.model._t_Node = IcePy.defineClass('::ode::model::Node', Node, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_sessionsSeq', (), _M_ode.model._t_NodeSessionsSeq, False, 0),
        ('_sessionsLoaded', (), IcePy._t_bool, False, 0),
        ('_uuid', (), _M_ode._t_RString, False, 0),
        ('_conn', (), _M_ode._t_RString, False, 0),
        ('_up', (), _M_ode._t_RTime, False, 0),
        ('_down', (), _M_ode._t_RTime, False, 0),
        ('_scale', (), _M_ode._t_RInt, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_NodeAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    Node._ice_type = _M_ode.model._t_Node

    Node._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Node._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Node._op_unloadSessions = IcePy.Operation('unloadSessions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Node._op_sizeOfSessions = IcePy.Operation('sizeOfSessions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Node._op_copySessions = IcePy.Operation('copySessions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_NodeSessionsSeq, False, 0), ())
    Node._op_addSession = IcePy.Operation('addSession', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Session, False, 0),), (), None, ())
    Node._op_addAllSessionSet = IcePy.Operation('addAllSessionSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NodeSessionsSeq, False, 0),), (), None, ())
    Node._op_removeSession = IcePy.Operation('removeSession', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Session, False, 0),), (), None, ())
    Node._op_removeAllSessionSet = IcePy.Operation('removeAllSessionSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NodeSessionsSeq, False, 0),), (), None, ())
    Node._op_clearSessions = IcePy.Operation('clearSessions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Node._op_reloadSessions = IcePy.Operation('reloadSessions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Node, False, 0),), (), None, ())
    Node._op_getUuid = IcePy.Operation('getUuid', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Node._op_setUuid = IcePy.Operation('setUuid', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Node._op_getConn = IcePy.Operation('getConn', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Node._op_setConn = IcePy.Operation('setConn', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Node._op_getUp = IcePy.Operation('getUp', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    Node._op_setUp = IcePy.Operation('setUp', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    Node._op_getDown = IcePy.Operation('getDown', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    Node._op_setDown = IcePy.Operation('setDown', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    Node._op_getScale = IcePy.Operation('getScale', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Node._op_setScale = IcePy.Operation('setScale', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Node._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Node._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Node._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_NodeAnnotationLinksSeq, False, 0), ())
    Node._op_addNodeAnnotationLink = IcePy.Operation('addNodeAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NodeAnnotationLink, False, 0),), (), None, ())
    Node._op_addAllNodeAnnotationLinkSet = IcePy.Operation('addAllNodeAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NodeAnnotationLinksSeq, False, 0),), (), None, ())
    Node._op_removeNodeAnnotationLink = IcePy.Operation('removeNodeAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NodeAnnotationLink, False, 0),), (), None, ())
    Node._op_removeAllNodeAnnotationLinkSet = IcePy.Operation('removeAllNodeAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NodeAnnotationLinksSeq, False, 0),), (), None, ())
    Node._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Node._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Node, False, 0),), (), None, ())
    Node._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Node._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_NodeAnnotationLink, False, 0), ())
    Node._op_addNodeAnnotationLinkToBoth = IcePy.Operation('addNodeAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NodeAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Node._op_findNodeAnnotationLink = IcePy.Operation('findNodeAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_NodeAnnotationLinksSeq, False, 0), ())
    Node._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Node._op_removeNodeAnnotationLinkFromBoth = IcePy.Operation('removeNodeAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_NodeAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Node._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_NodeLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.Node = Node
    del Node

    _M_ode.model.NodePrx = NodePrx
    del NodePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
