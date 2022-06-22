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
import ode_model_Job_ice

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

if 'ParseJob' not in _M_ode.model.__dict__:
    _M_ode.model.ParseJob = Ice.createTempClass()
    class ParseJob(_M_ode.model.Job):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _username=None, _groupname=None, _type=None, _message=None, _status=None, _submitted=None, _scheduledFor=None, _started=None, _finished=None, _originalFileLinksSeq=None, _originalFileLinksLoaded=False, _originalFileLinksCountPerOwner=None, _params=None):
            if Ice.getType(self) == _M_ode.model.ParseJob:
                raise RuntimeError('ode.model.ParseJob is an abstract class')
            _M_ode.model.Job.__init__(self, _id, _details, _loaded, _version, _username, _groupname, _type, _message, _status, _submitted, _scheduledFor, _started, _finished, _originalFileLinksSeq, _originalFileLinksLoaded, _originalFileLinksCountPerOwner)
            self._params = _params

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Job', '::ode::model::ParseJob')

        def ice_id(self, current=None):
            return '::ode::model::ParseJob'

        def ice_staticId():
            return '::ode::model::ParseJob'
        ice_staticId = staticmethod(ice_staticId)

        def getParams(self, current=None):
            pass

        def setParams(self, theParams, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_ParseJob)

        __repr__ = __str__

    _M_ode.model.ParseJobPrx = Ice.createTempClass()
    class ParseJobPrx(_M_ode.model.JobPrx):

        def getParams(self, _ctx=None):
            return _M_ode.model.ParseJob._op_getParams.invoke(self, ((), _ctx))

        def begin_getParams(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ParseJob._op_getParams.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getParams(self, _r):
            return _M_ode.model.ParseJob._op_getParams.end(self, _r)

        def setParams(self, theParams, _ctx=None):
            return _M_ode.model.ParseJob._op_setParams.invoke(self, ((theParams, ), _ctx))

        def begin_setParams(self, theParams, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ParseJob._op_setParams.begin(self, ((theParams, ), _response, _ex, _sent, _ctx))

        def end_setParams(self, _r):
            return _M_ode.model.ParseJob._op_setParams.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ParseJobPrx.ice_checkedCast(proxy, '::ode::model::ParseJob', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ParseJobPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ParseJob'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ParseJobPrx = IcePy.defineProxy('::ode::model::ParseJob', ParseJobPrx)

    _M_ode.model._t_ParseJob = IcePy.declareClass('::ode::model::ParseJob')

    _M_ode.model._t_ParseJob = IcePy.defineClass('::ode::model::ParseJob', ParseJob, -1, (), True, False, _M_ode.model._t_Job, (), (('_params', (), _M_Ice._t_ByteSeq, False, 0),))
    ParseJob._ice_type = _M_ode.model._t_ParseJob

    ParseJob._op_getParams = IcePy.Operation('getParams', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_Ice._t_ByteSeq, False, 0), ())
    ParseJob._op_setParams = IcePy.Operation('setParams', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_Ice._t_ByteSeq, False, 0),), (), None, ())

    _M_ode.model.ParseJob = ParseJob
    del ParseJob

    _M_ode.model.ParseJobPrx = ParseJobPrx
    del ParseJobPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
