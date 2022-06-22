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
import ode_ModelF_ice
import ode_ServicesF_ice
import ode_Collections_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'JobHandle' not in _M_ode.api.__dict__:
    _M_ode.api.JobHandle = Ice.createTempClass()
    class JobHandle(_M_ode.api.StatefulServiceInterface):
        """
        Allows submission of asynchronous jobs.
        NOTE: The calling order for the service is as follows:
        {@code submit} or {@code attach}
        any of the other methods
        {@code close}
        Calling {@code close} does not cancel or otherwise change
        the Job state. See {@code cancelJob}.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.JobHandle:
                raise RuntimeError('ode.api.JobHandle is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::JobHandle', '::ode::api::ServiceInterface', '::ode::api::StatefulServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::JobHandle'

        def ice_staticId():
            return '::ode::api::JobHandle'
        ice_staticId = staticmethod(ice_staticId)

        def submit_async(self, _cb, job, current=None):
            """
            Submits a ode.model.Job and returns its database
            id. The only fields directly on status which are editable
            are message, scheduledFor and
            status. The latter two must be sensible.
            Arguments:
            _cb -- The asynchronous callback object.
            job -- Not null
            current -- The Current object for the invocation.
            """
            pass

        def attach_async(self, _cb, jobId, current=None):
            """
            Returns the current ode.model.JobStatus for the
            Job id.
            Arguments:
            _cb -- The asynchronous callback object.
            jobId -- 
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if the ode.model.Job does not exist.
            """
            pass

        def getJob_async(self, _cb, current=None):
            """
            Returns the current ode.model.Job
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def jobStatus_async(self, _cb, current=None):
            """
            Returns the current ode.model.JobStatus. Will
            never return null.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def jobFinished_async(self, _cb, current=None):
            """
            Returns null if the ode.model.Job is
            not finished, otherwise the ode.RTime for when it
            completed.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def jobMessage_async(self, _cb, current=None):
            """
            Returns the current message for job. May be set during
            processing.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def jobRunning_async(self, _cb, current=None):
            """
            Returns true if the ode.model.Job is
            running, i.e. has an attached process.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def jobError_async(self, _cb, current=None):
            """
            Returns true if the ode.model.Job
            has thrown an error.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def cancelJob_async(self, _cb, current=None):
            """
            Marks a job for cancellation. Not every processor will
            check for the cancelled flag for a running job, but no
            non-running job will start if it has been cancelled.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setStatus_async(self, _cb, status, current=None):
            """
            Updates the ode.model.JobStatus for the current
            job. The previous status is returned as a string. If the
            status is {@code CANCELLED}, this method is equivalent to
            {@code cancelJob}.
            Arguments:
            _cb -- The asynchronous callback object.
            status -- 
            current -- The Current object for the invocation.
            """
            pass

        def setMessage_async(self, _cb, message, current=None):
            """
            Sets the job's message string, and returns the previous
            value.
            Arguments:
            _cb -- The asynchronous callback object.
            message -- 
            current -- The Current object for the invocation.
            """
            pass

        def setStatusAndMessage_async(self, _cb, status, message, current=None):
            """
            Like {@code setStatus} but also sets the message.
            Arguments:
            _cb -- The asynchronous callback object.
            status -- 
            message -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_JobHandle)

        __repr__ = __str__

    _M_ode.api.JobHandlePrx = Ice.createTempClass()
    class JobHandlePrx(_M_ode.api.StatefulServiceInterfacePrx):

        """
        Submits a ode.model.Job and returns its database
        id. The only fields directly on status which are editable
        are message, scheduledFor and
        status. The latter two must be sensible.
        Arguments:
        job -- Not null
        _ctx -- The request context for the invocation.
        """
        def submit(self, job, _ctx=None):
            return _M_ode.api.JobHandle._op_submit.invoke(self, ((job, ), _ctx))

        """
        Submits a ode.model.Job and returns its database
        id. The only fields directly on status which are editable
        are message, scheduledFor and
        status. The latter two must be sensible.
        Arguments:
        job -- Not null
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_submit(self, job, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_submit.begin(self, ((job, ), _response, _ex, _sent, _ctx))

        """
        Submits a ode.model.Job and returns its database
        id. The only fields directly on status which are editable
        are message, scheduledFor and
        status. The latter two must be sensible.
        Arguments:
        job -- Not null
        """
        def end_submit(self, _r):
            return _M_ode.api.JobHandle._op_submit.end(self, _r)

        """
        Returns the current ode.model.JobStatus for the
        Job id.
        Arguments:
        jobId -- 
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- if the ode.model.Job does not exist.
        """
        def attach(self, jobId, _ctx=None):
            return _M_ode.api.JobHandle._op_attach.invoke(self, ((jobId, ), _ctx))

        """
        Returns the current ode.model.JobStatus for the
        Job id.
        Arguments:
        jobId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_attach(self, jobId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_attach.begin(self, ((jobId, ), _response, _ex, _sent, _ctx))

        """
        Returns the current ode.model.JobStatus for the
        Job id.
        Arguments:
        jobId -- 
        Throws:
        ApiUsageException -- if the ode.model.Job does not exist.
        """
        def end_attach(self, _r):
            return _M_ode.api.JobHandle._op_attach.end(self, _r)

        """
        Returns the current ode.model.Job
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getJob(self, _ctx=None):
            return _M_ode.api.JobHandle._op_getJob.invoke(self, ((), _ctx))

        """
        Returns the current ode.model.Job
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getJob(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_getJob.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current ode.model.Job
        Arguments:
        """
        def end_getJob(self, _r):
            return _M_ode.api.JobHandle._op_getJob.end(self, _r)

        """
        Returns the current ode.model.JobStatus. Will
        never return null.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def jobStatus(self, _ctx=None):
            return _M_ode.api.JobHandle._op_jobStatus.invoke(self, ((), _ctx))

        """
        Returns the current ode.model.JobStatus. Will
        never return null.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_jobStatus(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_jobStatus.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current ode.model.JobStatus. Will
        never return null.
        Arguments:
        """
        def end_jobStatus(self, _r):
            return _M_ode.api.JobHandle._op_jobStatus.end(self, _r)

        """
        Returns null if the ode.model.Job is
        not finished, otherwise the ode.RTime for when it
        completed.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def jobFinished(self, _ctx=None):
            return _M_ode.api.JobHandle._op_jobFinished.invoke(self, ((), _ctx))

        """
        Returns null if the ode.model.Job is
        not finished, otherwise the ode.RTime for when it
        completed.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_jobFinished(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_jobFinished.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns null if the ode.model.Job is
        not finished, otherwise the ode.RTime for when it
        completed.
        Arguments:
        """
        def end_jobFinished(self, _r):
            return _M_ode.api.JobHandle._op_jobFinished.end(self, _r)

        """
        Returns the current message for job. May be set during
        processing.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def jobMessage(self, _ctx=None):
            return _M_ode.api.JobHandle._op_jobMessage.invoke(self, ((), _ctx))

        """
        Returns the current message for job. May be set during
        processing.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_jobMessage(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_jobMessage.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current message for job. May be set during
        processing.
        Arguments:
        """
        def end_jobMessage(self, _r):
            return _M_ode.api.JobHandle._op_jobMessage.end(self, _r)

        """
        Returns true if the ode.model.Job is
        running, i.e. has an attached process.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def jobRunning(self, _ctx=None):
            return _M_ode.api.JobHandle._op_jobRunning.invoke(self, ((), _ctx))

        """
        Returns true if the ode.model.Job is
        running, i.e. has an attached process.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_jobRunning(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_jobRunning.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns true if the ode.model.Job is
        running, i.e. has an attached process.
        Arguments:
        """
        def end_jobRunning(self, _r):
            return _M_ode.api.JobHandle._op_jobRunning.end(self, _r)

        """
        Returns true if the ode.model.Job
        has thrown an error.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def jobError(self, _ctx=None):
            return _M_ode.api.JobHandle._op_jobError.invoke(self, ((), _ctx))

        """
        Returns true if the ode.model.Job
        has thrown an error.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_jobError(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_jobError.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns true if the ode.model.Job
        has thrown an error.
        Arguments:
        """
        def end_jobError(self, _r):
            return _M_ode.api.JobHandle._op_jobError.end(self, _r)

        """
        Marks a job for cancellation. Not every processor will
        check for the cancelled flag for a running job, but no
        non-running job will start if it has been cancelled.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def cancelJob(self, _ctx=None):
            return _M_ode.api.JobHandle._op_cancelJob.invoke(self, ((), _ctx))

        """
        Marks a job for cancellation. Not every processor will
        check for the cancelled flag for a running job, but no
        non-running job will start if it has been cancelled.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_cancelJob(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_cancelJob.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Marks a job for cancellation. Not every processor will
        check for the cancelled flag for a running job, but no
        non-running job will start if it has been cancelled.
        Arguments:
        """
        def end_cancelJob(self, _r):
            return _M_ode.api.JobHandle._op_cancelJob.end(self, _r)

        """
        Updates the ode.model.JobStatus for the current
        job. The previous status is returned as a string. If the
        status is {@code CANCELLED}, this method is equivalent to
        {@code cancelJob}.
        Arguments:
        status -- 
        _ctx -- The request context for the invocation.
        """
        def setStatus(self, status, _ctx=None):
            return _M_ode.api.JobHandle._op_setStatus.invoke(self, ((status, ), _ctx))

        """
        Updates the ode.model.JobStatus for the current
        job. The previous status is returned as a string. If the
        status is {@code CANCELLED}, this method is equivalent to
        {@code cancelJob}.
        Arguments:
        status -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setStatus(self, status, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_setStatus.begin(self, ((status, ), _response, _ex, _sent, _ctx))

        """
        Updates the ode.model.JobStatus for the current
        job. The previous status is returned as a string. If the
        status is {@code CANCELLED}, this method is equivalent to
        {@code cancelJob}.
        Arguments:
        status -- 
        """
        def end_setStatus(self, _r):
            return _M_ode.api.JobHandle._op_setStatus.end(self, _r)

        """
        Sets the job's message string, and returns the previous
        value.
        Arguments:
        message -- 
        _ctx -- The request context for the invocation.
        Returns: the previous message value
        """
        def setMessage(self, message, _ctx=None):
            return _M_ode.api.JobHandle._op_setMessage.invoke(self, ((message, ), _ctx))

        """
        Sets the job's message string, and returns the previous
        value.
        Arguments:
        message -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setMessage(self, message, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_setMessage.begin(self, ((message, ), _response, _ex, _sent, _ctx))

        """
        Sets the job's message string, and returns the previous
        value.
        Arguments:
        message -- 
        Returns: the previous message value
        """
        def end_setMessage(self, _r):
            return _M_ode.api.JobHandle._op_setMessage.end(self, _r)

        """
        Like {@code setStatus} but also sets the message.
        Arguments:
        status -- 
        message -- 
        _ctx -- The request context for the invocation.
        """
        def setStatusAndMessage(self, status, message, _ctx=None):
            return _M_ode.api.JobHandle._op_setStatusAndMessage.invoke(self, ((status, message), _ctx))

        """
        Like {@code setStatus} but also sets the message.
        Arguments:
        status -- 
        message -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setStatusAndMessage(self, status, message, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.JobHandle._op_setStatusAndMessage.begin(self, ((status, message), _response, _ex, _sent, _ctx))

        """
        Like {@code setStatus} but also sets the message.
        Arguments:
        status -- 
        message -- 
        """
        def end_setStatusAndMessage(self, _r):
            return _M_ode.api.JobHandle._op_setStatusAndMessage.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.JobHandlePrx.ice_checkedCast(proxy, '::ode::api::JobHandle', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.JobHandlePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::JobHandle'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_JobHandlePrx = IcePy.defineProxy('::ode::api::JobHandle', JobHandlePrx)

    _M_ode.api._t_JobHandle = IcePy.defineClass('::ode::api::JobHandle', JobHandle, -1, (), True, False, None, (_M_ode.api._t_StatefulServiceInterface,), ())
    JobHandle._ice_type = _M_ode.api._t_JobHandle

    JobHandle._op_submit = IcePy.Operation('submit', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Job, False, 0),), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    JobHandle._op_attach = IcePy.Operation('attach', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.model._t_JobStatus, False, 0), (_M_ode._t_ServerError,))
    JobHandle._op_getJob = IcePy.Operation('getJob', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.model._t_Job, False, 0), (_M_ode._t_ServerError,))
    JobHandle._op_jobStatus = IcePy.Operation('jobStatus', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.model._t_JobStatus, False, 0), (_M_ode._t_ServerError,))
    JobHandle._op_jobFinished = IcePy.Operation('jobFinished', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode._t_RTime, False, 0), (_M_ode._t_ServerError,))
    JobHandle._op_jobMessage = IcePy.Operation('jobMessage', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    JobHandle._op_jobRunning = IcePy.Operation('jobRunning', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    JobHandle._op_jobError = IcePy.Operation('jobError', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    JobHandle._op_cancelJob = IcePy.Operation('cancelJob', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    JobHandle._op_setStatus = IcePy.Operation('setStatus', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    JobHandle._op_setMessage = IcePy.Operation('setMessage', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    JobHandle._op_setStatusAndMessage = IcePy.Operation('setStatusAndMessage', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode._t_RString, False, 0)), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.JobHandle = JobHandle
    del JobHandle

    _M_ode.api.JobHandlePrx = JobHandlePrx
    del JobHandlePrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
