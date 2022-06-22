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

if 'IntegrityCheckJob' not in _M_ode.model.__dict__:
    _M_ode.model.IntegrityCheckJob = Ice.createTempClass()
    class IntegrityCheckJob(_M_ode.model.Job):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _username=None, _groupname=None, _type=None, _message=None, _status=None, _submitted=None, _scheduledFor=None, _started=None, _finished=None, _originalFileLinksSeq=None, _originalFileLinksLoaded=False, _originalFileLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.IntegrityCheckJob:
                raise RuntimeError('ode.model.IntegrityCheckJob is an abstract class')
            _M_ode.model.Job.__init__(self, _id, _details, _loaded, _version, _username, _groupname, _type, _message, _status, _submitted, _scheduledFor, _started, _finished, _originalFileLinksSeq, _originalFileLinksLoaded, _originalFileLinksCountPerOwner)

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::IntegrityCheckJob', '::ode::model::Job')

        def ice_id(self, current=None):
            return '::ode::model::IntegrityCheckJob'

        def ice_staticId():
            return '::ode::model::IntegrityCheckJob'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_IntegrityCheckJob)

        __repr__ = __str__

    _M_ode.model.IntegrityCheckJobPrx = Ice.createTempClass()
    class IntegrityCheckJobPrx(_M_ode.model.JobPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.IntegrityCheckJobPrx.ice_checkedCast(proxy, '::ode::model::IntegrityCheckJob', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.IntegrityCheckJobPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::IntegrityCheckJob'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_IntegrityCheckJobPrx = IcePy.defineProxy('::ode::model::IntegrityCheckJob', IntegrityCheckJobPrx)

    _M_ode.model._t_IntegrityCheckJob = IcePy.declareClass('::ode::model::IntegrityCheckJob')

    _M_ode.model._t_IntegrityCheckJob = IcePy.defineClass('::ode::model::IntegrityCheckJob', IntegrityCheckJob, -1, (), True, False, _M_ode.model._t_Job, (), ())
    IntegrityCheckJob._ice_type = _M_ode.model._t_IntegrityCheckJob

    _M_ode.model.IntegrityCheckJob = IntegrityCheckJob
    del IntegrityCheckJob

    _M_ode.model.IntegrityCheckJobPrx = IntegrityCheckJobPrx
    del IntegrityCheckJobPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
