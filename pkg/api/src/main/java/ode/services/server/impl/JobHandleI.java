package ode.services.server.impl;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import ode.api.JobHandle;
import ode.services.server.util.ServerExecutor;
import ode.RString;
import ode.ServerError;
import ode.api.AMD_JobHandle_attach;
import ode.api.AMD_JobHandle_cancelJob;
import ode.api.AMD_JobHandle_getJob;
import ode.api.AMD_JobHandle_jobError;
import ode.api.AMD_JobHandle_jobFinished;
import ode.api.AMD_JobHandle_jobMessage;
import ode.api.AMD_JobHandle_jobRunning;
import ode.api.AMD_JobHandle_jobStatus;
import ode.api.AMD_JobHandle_setStatus;
import ode.api.AMD_JobHandle_setStatusAndMessage;
import ode.api.AMD_JobHandle_submit;
import ode.api._JobHandleOperations;
import ode.model.Job;
import Ice.Current;

/**
 * Implementation of the JobHandle service.
 * 
 * @see ode.api.JobHandle
 */
public class JobHandleI extends AbstractCloseableAmdServant implements
        _JobHandleOperations {

    public JobHandleI(JobHandle service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void attach_async(AMD_JobHandle_attach __cb, long jobId,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, jobId);

    }

    public void cancelJob_async(AMD_JobHandle_cancelJob __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void getJob_async(AMD_JobHandle_getJob __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void jobError_async(AMD_JobHandle_jobError __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void jobFinished_async(AMD_JobHandle_jobFinished __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void jobMessage_async(AMD_JobHandle_jobMessage __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void jobRunning_async(AMD_JobHandle_jobRunning __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void jobStatus_async(AMD_JobHandle_jobStatus __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void submit_async(AMD_JobHandle_submit __cb, Job j, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, j);

    }

    public void setStatus_async(AMD_JobHandle_setStatus __cb, String status,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, status);
    }

    public void setStatusAndMessage_async(AMD_JobHandle_setStatusAndMessage __cb, String status,
            RString msg, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, status, msg);
    }

    public void setMessage_async(ode.api.AMD_JobHandle_setMessage __cb, String message,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, message);
    };

    @Override
    protected void preClose(Current current) throws Throwable {
        // no-op
    }

    @Override
    protected void postClose(Current current) {
        // no-op
    }
}