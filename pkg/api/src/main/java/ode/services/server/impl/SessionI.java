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

import ode.api.local.LocalSession;
import ode.services.server.util.ServerExecutor;
import ode.RType;
import ode.ServerError;
import ode.api.AMD_ISession_closeSession;
import ode.api.AMD_ISession_createSession;
import ode.api.AMD_ISession_createSessionWithTimeout;
import ode.api.AMD_ISession_createSessionWithTimeouts;
import ode.api.AMD_ISession_createUserSession;
import ode.api.AMD_ISession_getInput;
import ode.api.AMD_ISession_getInputKeys;
import ode.api.AMD_ISession_getInputs;
import ode.api.AMD_ISession_getMyOpenAgentSessions;
import ode.api.AMD_ISession_getMyOpenClientSessions;
import ode.api.AMD_ISession_getMyOpenSessions;
import ode.api.AMD_ISession_getOutput;
import ode.api.AMD_ISession_getOutputKeys;
import ode.api.AMD_ISession_getOutputs;
import ode.api.AMD_ISession_getReferenceCount;
import ode.api.AMD_ISession_getSession;
import ode.api.AMD_ISession_setInput;
import ode.api.AMD_ISession_setOutput;
import ode.api._ISessionOperations;
import ode.model.Session;
import ode.sys.Principal;
import ode.util.IceMapper;
import ode.util.RTypeMapper;
import Ice.Current;

/**
 * Implementation of the ISession service.
 * 
 * @see ode.api.ISession
 */
public class SessionI extends AbstractAmdServant implements _ISessionOperations {

    public SessionI(LocalSession service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void closeSession_async(AMD_ISession_closeSession __cb,
            Session sess, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sess);

    }

    public void createSessionWithTimeout_async(
            AMD_ISession_createSessionWithTimeout __cb, Principal p,
            long ttlMs, Current __current) throws ServerError, Glacier2.CannotCreateSessionException {
        callInvokerOnRawArgs(__cb, __current, p, ttlMs);

    }

    public void createSessionWithTimeouts_async(
            AMD_ISession_createSessionWithTimeouts __cb, Principal p,
            long ttlMs, long ttiMs, Current __current) throws ServerError, Glacier2.CannotCreateSessionException {
        callInvokerOnRawArgs(__cb, __current, p, ttlMs, ttiMs);

    }

    public void createSession_async(AMD_ISession_createSession __cb,
            Principal p, String credentials, Current __current)
            throws ServerError, Glacier2.CannotCreateSessionException {
        callInvokerOnRawArgs(__cb, __current, p, credentials);
    }

    public void createUserSession_async(AMD_ISession_createUserSession __cb, long arg0,
            long arg1, String arg2, Ice.Current __current)
            throws ServerError, Glacier2.CannotCreateSessionException {
        callInvokerOnRawArgs(__cb, __current, arg0, arg1, arg2);
    }

    public void getInputKeys_async(AMD_ISession_getInputKeys __cb, String sess,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sess);

    }

    public void getInput_async(AMD_ISession_getInput __cb, String sess,
            String key, Current __current) throws ServerError {
        RTypeMapper mapper = new RTypeMapper(IceMapper.OBJECT_TO_RTYPE);
        callInvokerOnMappedArgs(mapper, __cb, __current, sess, key);

    }

    public void getOutputKeys_async(AMD_ISession_getOutputKeys __cb,
            String sess, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sess);

    }

    public void getOutput_async(AMD_ISession_getOutput __cb, String sess,
            String key, Current __current) throws ServerError {
        RTypeMapper mapper = new RTypeMapper(IceMapper.OBJECT_TO_RTYPE);
        callInvokerOnMappedArgs(mapper, __cb, __current, sess, key);
    }

    public void getSession_async(AMD_ISession_getSession __cb,
            String sessionUuid, Current __current) throws ServerError {
        boolean quietly = false;
        if (__current != null && __current.ctx != null) {
            quietly = Boolean.valueOf(__current.ctx.getOrDefault("quietly", "false"));
        }

        try {
            if (quietly) {
                __current.operation = "getSessionQuietly";
            }
            callInvokerOnRawArgs(__cb, __current, sessionUuid);
        } finally {
            if (quietly) {
                __current.operation = "getSession";
            }
        }

    }

    public void getReferenceCount_async(AMD_ISession_getReferenceCount __cb,
            String sessionUuid, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sessionUuid);

    }

    public void setInput_async(AMD_ISession_setInput __cb, String sess,
            String key, RType value, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sess, key, value);

    }

    public void setOutput_async(AMD_ISession_setOutput __cb, String sess,
            String key, RType value, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, sess, key, value);

    }

    public void getInputs_async(AMD_ISession_getInputs __cb, String sess,
            Current __current) throws ServerError {
        RTypeMapper mapper = new RTypeMapper(IceMapper.RTYPEDICT);
        callInvokerOnMappedArgs(mapper, __cb, __current, sess);
    }
    public void getOutputs_async(AMD_ISession_getOutputs __cb, String sess,
            Current __current) throws ServerError {
        RTypeMapper mapper = new RTypeMapper(IceMapper.RTYPEDICT);
        callInvokerOnMappedArgs(mapper, __cb, __current, sess);
    }

    public void getMyOpenAgentSessions_async(
            AMD_ISession_getMyOpenAgentSessions __cb, String agent,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, agent);
    }

    public void getMyOpenClientSessions_async(
            AMD_ISession_getMyOpenClientSessions __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void getMyOpenSessions_async(AMD_ISession_getMyOpenSessions __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }
}