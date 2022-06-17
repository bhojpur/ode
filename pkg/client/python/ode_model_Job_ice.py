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

if 'JobStatus' not in _M_ode.model.__dict__:
    _M_ode.model._t_JobStatus = IcePy.declareClass('::ode::model::JobStatus')
    _M_ode.model._t_JobStatusPrx = IcePy.declareProxy('::ode::model::JobStatus')

if 'JobOriginalFileLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_JobOriginalFileLink = IcePy.declareClass('::ode::model::JobOriginalFileLink')
    _M_ode.model._t_JobOriginalFileLinkPrx = IcePy.declareProxy('::ode::model::JobOriginalFileLink')

if 'OriginalFile' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFile = IcePy.declareClass('::ode::model::OriginalFile')
    _M_ode.model._t_OriginalFilePrx = IcePy.declareProxy('::ode::model::OriginalFile')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_JobOriginalFileLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_JobOriginalFileLinksSeq = IcePy.defineSequence('::ode::model::JobOriginalFileLinksSeq', (), _M_ode.model._t_JobOriginalFileLink)

if '_t_JobLinkedOriginalFileSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_JobLinkedOriginalFileSeq = IcePy.defineSequence('::ode::model::JobLinkedOriginalFileSeq', (), _M_ode.model._t_OriginalFile)

if 'Job' not in _M_ode.model.__dict__:
    _M_ode.model.Job = Ice.createTempClass()
    class Job(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _username=None, _groupname=None, _type=None, _message=None, _status=None, _submitted=None, _scheduledFor=None, _started=None, _finished=None, _originalFileLinksSeq=None, _originalFileLinksLoaded=False, _originalFileLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.Job:
                raise RuntimeError('ode.model.Job is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._username = _username
            self._groupname = _groupname
            self._type = _type
            self._message = _message
            self._status = _status
            self._submitted = _submitted
            self._scheduledFor = _scheduledFor
            self._started = _started
            self._finished = _finished
            self._originalFileLinksSeq = _originalFileLinksSeq
            self._originalFileLinksLoaded = _originalFileLinksLoaded
            self._originalFileLinksCountPerOwner = _originalFileLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Job')

        def ice_id(self, current=None):
            return '::ode::model::Job'

        def ice_staticId():
            return '::ode::model::Job'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getUsername(self, current=None):
            pass

        def setUsername(self, theUsername, current=None):
            pass

        def getGroupname(self, current=None):
            pass

        def setGroupname(self, theGroupname, current=None):
            pass

        def getType(self, current=None):
            pass

        def setType(self, theType, current=None):
            pass

        def getMessage(self, current=None):
            pass

        def setMessage(self, theMessage, current=None):
            pass

        def getStatus(self, current=None):
            pass

        def setStatus(self, theStatus, current=None):
            pass

        def getSubmitted(self, current=None):
            pass

        def setSubmitted(self, theSubmitted, current=None):
            pass

        def getScheduledFor(self, current=None):
            pass

        def setScheduledFor(self, theScheduledFor, current=None):
            pass

        def getStarted(self, current=None):
            pass

        def setStarted(self, theStarted, current=None):
            pass

        def getFinished(self, current=None):
            pass

        def setFinished(self, theFinished, current=None):
            pass

        def unloadOriginalFileLinks(self, current=None):
            pass

        def sizeOfOriginalFileLinks(self, current=None):
            pass

        def copyOriginalFileLinks(self, current=None):
            pass

        def addJobOriginalFileLink(self, target, current=None):
            pass

        def addAllJobOriginalFileLinkSet(self, targets, current=None):
            pass

        def removeJobOriginalFileLink(self, theTarget, current=None):
            pass

        def removeAllJobOriginalFileLinkSet(self, targets, current=None):
            pass

        def clearOriginalFileLinks(self, current=None):
            pass

        def reloadOriginalFileLinks(self, toCopy, current=None):
            pass

        def getOriginalFileLinksCountPerOwner(self, current=None):
            pass

        def linkOriginalFile(self, addition, current=None):
            pass

        def addJobOriginalFileLinkToBoth(self, link, bothSides, current=None):
            pass

        def findJobOriginalFileLink(self, removal, current=None):
            pass

        def unlinkOriginalFile(self, removal, current=None):
            pass

        def removeJobOriginalFileLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedOriginalFileList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Job)

        __repr__ = __str__

    _M_ode.model.JobPrx = Ice.createTempClass()
    class JobPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Job._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Job._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Job._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Job._op_setVersion.end(self, _r)

        def getUsername(self, _ctx=None):
            return _M_ode.model.Job._op_getUsername.invoke(self, ((), _ctx))

        def begin_getUsername(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getUsername.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getUsername(self, _r):
            return _M_ode.model.Job._op_getUsername.end(self, _r)

        def setUsername(self, theUsername, _ctx=None):
            return _M_ode.model.Job._op_setUsername.invoke(self, ((theUsername, ), _ctx))

        def begin_setUsername(self, theUsername, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_setUsername.begin(self, ((theUsername, ), _response, _ex, _sent, _ctx))

        def end_setUsername(self, _r):
            return _M_ode.model.Job._op_setUsername.end(self, _r)

        def getGroupname(self, _ctx=None):
            return _M_ode.model.Job._op_getGroupname.invoke(self, ((), _ctx))

        def begin_getGroupname(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getGroupname.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getGroupname(self, _r):
            return _M_ode.model.Job._op_getGroupname.end(self, _r)

        def setGroupname(self, theGroupname, _ctx=None):
            return _M_ode.model.Job._op_setGroupname.invoke(self, ((theGroupname, ), _ctx))

        def begin_setGroupname(self, theGroupname, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_setGroupname.begin(self, ((theGroupname, ), _response, _ex, _sent, _ctx))

        def end_setGroupname(self, _r):
            return _M_ode.model.Job._op_setGroupname.end(self, _r)

        def getType(self, _ctx=None):
            return _M_ode.model.Job._op_getType.invoke(self, ((), _ctx))

        def begin_getType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getType(self, _r):
            return _M_ode.model.Job._op_getType.end(self, _r)

        def setType(self, theType, _ctx=None):
            return _M_ode.model.Job._op_setType.invoke(self, ((theType, ), _ctx))

        def begin_setType(self, theType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_setType.begin(self, ((theType, ), _response, _ex, _sent, _ctx))

        def end_setType(self, _r):
            return _M_ode.model.Job._op_setType.end(self, _r)

        def getMessage(self, _ctx=None):
            return _M_ode.model.Job._op_getMessage.invoke(self, ((), _ctx))

        def begin_getMessage(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getMessage.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMessage(self, _r):
            return _M_ode.model.Job._op_getMessage.end(self, _r)

        def setMessage(self, theMessage, _ctx=None):
            return _M_ode.model.Job._op_setMessage.invoke(self, ((theMessage, ), _ctx))

        def begin_setMessage(self, theMessage, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_setMessage.begin(self, ((theMessage, ), _response, _ex, _sent, _ctx))

        def end_setMessage(self, _r):
            return _M_ode.model.Job._op_setMessage.end(self, _r)

        def getStatus(self, _ctx=None):
            return _M_ode.model.Job._op_getStatus.invoke(self, ((), _ctx))

        def begin_getStatus(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getStatus.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStatus(self, _r):
            return _M_ode.model.Job._op_getStatus.end(self, _r)

        def setStatus(self, theStatus, _ctx=None):
            return _M_ode.model.Job._op_setStatus.invoke(self, ((theStatus, ), _ctx))

        def begin_setStatus(self, theStatus, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_setStatus.begin(self, ((theStatus, ), _response, _ex, _sent, _ctx))

        def end_setStatus(self, _r):
            return _M_ode.model.Job._op_setStatus.end(self, _r)

        def getSubmitted(self, _ctx=None):
            return _M_ode.model.Job._op_getSubmitted.invoke(self, ((), _ctx))

        def begin_getSubmitted(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getSubmitted.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSubmitted(self, _r):
            return _M_ode.model.Job._op_getSubmitted.end(self, _r)

        def setSubmitted(self, theSubmitted, _ctx=None):
            return _M_ode.model.Job._op_setSubmitted.invoke(self, ((theSubmitted, ), _ctx))

        def begin_setSubmitted(self, theSubmitted, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_setSubmitted.begin(self, ((theSubmitted, ), _response, _ex, _sent, _ctx))

        def end_setSubmitted(self, _r):
            return _M_ode.model.Job._op_setSubmitted.end(self, _r)

        def getScheduledFor(self, _ctx=None):
            return _M_ode.model.Job._op_getScheduledFor.invoke(self, ((), _ctx))

        def begin_getScheduledFor(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getScheduledFor.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getScheduledFor(self, _r):
            return _M_ode.model.Job._op_getScheduledFor.end(self, _r)

        def setScheduledFor(self, theScheduledFor, _ctx=None):
            return _M_ode.model.Job._op_setScheduledFor.invoke(self, ((theScheduledFor, ), _ctx))

        def begin_setScheduledFor(self, theScheduledFor, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_setScheduledFor.begin(self, ((theScheduledFor, ), _response, _ex, _sent, _ctx))

        def end_setScheduledFor(self, _r):
            return _M_ode.model.Job._op_setScheduledFor.end(self, _r)

        def getStarted(self, _ctx=None):
            return _M_ode.model.Job._op_getStarted.invoke(self, ((), _ctx))

        def begin_getStarted(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getStarted.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStarted(self, _r):
            return _M_ode.model.Job._op_getStarted.end(self, _r)

        def setStarted(self, theStarted, _ctx=None):
            return _M_ode.model.Job._op_setStarted.invoke(self, ((theStarted, ), _ctx))

        def begin_setStarted(self, theStarted, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_setStarted.begin(self, ((theStarted, ), _response, _ex, _sent, _ctx))

        def end_setStarted(self, _r):
            return _M_ode.model.Job._op_setStarted.end(self, _r)

        def getFinished(self, _ctx=None):
            return _M_ode.model.Job._op_getFinished.invoke(self, ((), _ctx))

        def begin_getFinished(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getFinished.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFinished(self, _r):
            return _M_ode.model.Job._op_getFinished.end(self, _r)

        def setFinished(self, theFinished, _ctx=None):
            return _M_ode.model.Job._op_setFinished.invoke(self, ((theFinished, ), _ctx))

        def begin_setFinished(self, theFinished, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_setFinished.begin(self, ((theFinished, ), _response, _ex, _sent, _ctx))

        def end_setFinished(self, _r):
            return _M_ode.model.Job._op_setFinished.end(self, _r)

        def unloadOriginalFileLinks(self, _ctx=None):
            return _M_ode.model.Job._op_unloadOriginalFileLinks.invoke(self, ((), _ctx))

        def begin_unloadOriginalFileLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_unloadOriginalFileLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadOriginalFileLinks(self, _r):
            return _M_ode.model.Job._op_unloadOriginalFileLinks.end(self, _r)

        def sizeOfOriginalFileLinks(self, _ctx=None):
            return _M_ode.model.Job._op_sizeOfOriginalFileLinks.invoke(self, ((), _ctx))

        def begin_sizeOfOriginalFileLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_sizeOfOriginalFileLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfOriginalFileLinks(self, _r):
            return _M_ode.model.Job._op_sizeOfOriginalFileLinks.end(self, _r)

        def copyOriginalFileLinks(self, _ctx=None):
            return _M_ode.model.Job._op_copyOriginalFileLinks.invoke(self, ((), _ctx))

        def begin_copyOriginalFileLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_copyOriginalFileLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyOriginalFileLinks(self, _r):
            return _M_ode.model.Job._op_copyOriginalFileLinks.end(self, _r)

        def addJobOriginalFileLink(self, target, _ctx=None):
            return _M_ode.model.Job._op_addJobOriginalFileLink.invoke(self, ((target, ), _ctx))

        def begin_addJobOriginalFileLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_addJobOriginalFileLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addJobOriginalFileLink(self, _r):
            return _M_ode.model.Job._op_addJobOriginalFileLink.end(self, _r)

        def addAllJobOriginalFileLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Job._op_addAllJobOriginalFileLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllJobOriginalFileLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_addAllJobOriginalFileLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllJobOriginalFileLinkSet(self, _r):
            return _M_ode.model.Job._op_addAllJobOriginalFileLinkSet.end(self, _r)

        def removeJobOriginalFileLink(self, theTarget, _ctx=None):
            return _M_ode.model.Job._op_removeJobOriginalFileLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeJobOriginalFileLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_removeJobOriginalFileLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeJobOriginalFileLink(self, _r):
            return _M_ode.model.Job._op_removeJobOriginalFileLink.end(self, _r)

        def removeAllJobOriginalFileLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Job._op_removeAllJobOriginalFileLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllJobOriginalFileLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_removeAllJobOriginalFileLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllJobOriginalFileLinkSet(self, _r):
            return _M_ode.model.Job._op_removeAllJobOriginalFileLinkSet.end(self, _r)

        def clearOriginalFileLinks(self, _ctx=None):
            return _M_ode.model.Job._op_clearOriginalFileLinks.invoke(self, ((), _ctx))

        def begin_clearOriginalFileLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_clearOriginalFileLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearOriginalFileLinks(self, _r):
            return _M_ode.model.Job._op_clearOriginalFileLinks.end(self, _r)

        def reloadOriginalFileLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Job._op_reloadOriginalFileLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadOriginalFileLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_reloadOriginalFileLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadOriginalFileLinks(self, _r):
            return _M_ode.model.Job._op_reloadOriginalFileLinks.end(self, _r)

        def getOriginalFileLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Job._op_getOriginalFileLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getOriginalFileLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_getOriginalFileLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getOriginalFileLinksCountPerOwner(self, _r):
            return _M_ode.model.Job._op_getOriginalFileLinksCountPerOwner.end(self, _r)

        def linkOriginalFile(self, addition, _ctx=None):
            return _M_ode.model.Job._op_linkOriginalFile.invoke(self, ((addition, ), _ctx))

        def begin_linkOriginalFile(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_linkOriginalFile.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkOriginalFile(self, _r):
            return _M_ode.model.Job._op_linkOriginalFile.end(self, _r)

        def addJobOriginalFileLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Job._op_addJobOriginalFileLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addJobOriginalFileLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_addJobOriginalFileLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addJobOriginalFileLinkToBoth(self, _r):
            return _M_ode.model.Job._op_addJobOriginalFileLinkToBoth.end(self, _r)

        def findJobOriginalFileLink(self, removal, _ctx=None):
            return _M_ode.model.Job._op_findJobOriginalFileLink.invoke(self, ((removal, ), _ctx))

        def begin_findJobOriginalFileLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_findJobOriginalFileLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findJobOriginalFileLink(self, _r):
            return _M_ode.model.Job._op_findJobOriginalFileLink.end(self, _r)

        def unlinkOriginalFile(self, removal, _ctx=None):
            return _M_ode.model.Job._op_unlinkOriginalFile.invoke(self, ((removal, ), _ctx))

        def begin_unlinkOriginalFile(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_unlinkOriginalFile.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkOriginalFile(self, _r):
            return _M_ode.model.Job._op_unlinkOriginalFile.end(self, _r)

        def removeJobOriginalFileLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Job._op_removeJobOriginalFileLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeJobOriginalFileLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_removeJobOriginalFileLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeJobOriginalFileLinkFromBoth(self, _r):
            return _M_ode.model.Job._op_removeJobOriginalFileLinkFromBoth.end(self, _r)

        def linkedOriginalFileList(self, _ctx=None):
            return _M_ode.model.Job._op_linkedOriginalFileList.invoke(self, ((), _ctx))

        def begin_linkedOriginalFileList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Job._op_linkedOriginalFileList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedOriginalFileList(self, _r):
            return _M_ode.model.Job._op_linkedOriginalFileList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.JobPrx.ice_checkedCast(proxy, '::ode::model::Job', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.JobPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Job'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_JobPrx = IcePy.defineProxy('::ode::model::Job', JobPrx)

    _M_ode.model._t_Job = IcePy.declareClass('::ode::model::Job')

    _M_ode.model._t_Job = IcePy.defineClass('::ode::model::Job', Job, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_username', (), _M_ode._t_RString, False, 0),
        ('_groupname', (), _M_ode._t_RString, False, 0),
        ('_type', (), _M_ode._t_RString, False, 0),
        ('_message', (), _M_ode._t_RString, False, 0),
        ('_status', (), _M_ode.model._t_JobStatus, False, 0),
        ('_submitted', (), _M_ode._t_RTime, False, 0),
        ('_scheduledFor', (), _M_ode._t_RTime, False, 0),
        ('_started', (), _M_ode._t_RTime, False, 0),
        ('_finished', (), _M_ode._t_RTime, False, 0),
        ('_originalFileLinksSeq', (), _M_ode.model._t_JobOriginalFileLinksSeq, False, 0),
        ('_originalFileLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_originalFileLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    Job._ice_type = _M_ode.model._t_Job

    Job._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Job._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Job._op_getUsername = IcePy.Operation('getUsername', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Job._op_setUsername = IcePy.Operation('setUsername', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Job._op_getGroupname = IcePy.Operation('getGroupname', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Job._op_setGroupname = IcePy.Operation('setGroupname', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Job._op_getType = IcePy.Operation('getType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Job._op_setType = IcePy.Operation('setType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Job._op_getMessage = IcePy.Operation('getMessage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Job._op_setMessage = IcePy.Operation('setMessage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Job._op_getStatus = IcePy.Operation('getStatus', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_JobStatus, False, 0), ())
    Job._op_setStatus = IcePy.Operation('setStatus', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_JobStatus, False, 0),), (), None, ())
    Job._op_getSubmitted = IcePy.Operation('getSubmitted', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    Job._op_setSubmitted = IcePy.Operation('setSubmitted', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    Job._op_getScheduledFor = IcePy.Operation('getScheduledFor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    Job._op_setScheduledFor = IcePy.Operation('setScheduledFor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    Job._op_getStarted = IcePy.Operation('getStarted', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    Job._op_setStarted = IcePy.Operation('setStarted', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    Job._op_getFinished = IcePy.Operation('getFinished', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    Job._op_setFinished = IcePy.Operation('setFinished', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    Job._op_unloadOriginalFileLinks = IcePy.Operation('unloadOriginalFileLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Job._op_sizeOfOriginalFileLinks = IcePy.Operation('sizeOfOriginalFileLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Job._op_copyOriginalFileLinks = IcePy.Operation('copyOriginalFileLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_JobOriginalFileLinksSeq, False, 0), ())
    Job._op_addJobOriginalFileLink = IcePy.Operation('addJobOriginalFileLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_JobOriginalFileLink, False, 0),), (), None, ())
    Job._op_addAllJobOriginalFileLinkSet = IcePy.Operation('addAllJobOriginalFileLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_JobOriginalFileLinksSeq, False, 0),), (), None, ())
    Job._op_removeJobOriginalFileLink = IcePy.Operation('removeJobOriginalFileLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_JobOriginalFileLink, False, 0),), (), None, ())
    Job._op_removeAllJobOriginalFileLinkSet = IcePy.Operation('removeAllJobOriginalFileLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_JobOriginalFileLinksSeq, False, 0),), (), None, ())
    Job._op_clearOriginalFileLinks = IcePy.Operation('clearOriginalFileLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Job._op_reloadOriginalFileLinks = IcePy.Operation('reloadOriginalFileLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Job, False, 0),), (), None, ())
    Job._op_getOriginalFileLinksCountPerOwner = IcePy.Operation('getOriginalFileLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Job._op_linkOriginalFile = IcePy.Operation('linkOriginalFile', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), ((), _M_ode.model._t_JobOriginalFileLink, False, 0), ())
    Job._op_addJobOriginalFileLinkToBoth = IcePy.Operation('addJobOriginalFileLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_JobOriginalFileLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Job._op_findJobOriginalFileLink = IcePy.Operation('findJobOriginalFileLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), ((), _M_ode.model._t_JobOriginalFileLinksSeq, False, 0), ())
    Job._op_unlinkOriginalFile = IcePy.Operation('unlinkOriginalFile', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), None, ())
    Job._op_removeJobOriginalFileLinkFromBoth = IcePy.Operation('removeJobOriginalFileLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_JobOriginalFileLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Job._op_linkedOriginalFileList = IcePy.Operation('linkedOriginalFileList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_JobLinkedOriginalFileSeq, False, 0), ())

    _M_ode.model.Job = Job
    del Job

    _M_ode.model.JobPrx = JobPrx
    del JobPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
