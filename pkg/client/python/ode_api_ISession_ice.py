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
import ode_System_ice
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

if 'ISession' not in _M_ode.api.__dict__:
    _M_ode.api.ISession = Ice.createTempClass()
    class ISession(_M_ode.api.ServiceInterface):
        """
        ode.model.Session creation service for ODE. Access to
        all other services is dependent upon a properly created and still
        active ode.model.Session.
        The session uuid ({@code ode.model.Session.getUuid}) can be
        considered a capability token, or temporary single use password.
        Simply by possessing it the client has access to all information
        available to the ode.model.Session.
        Note: Both the RMI ode.system.ServiceFactory as well
        as the Ice ode.api.ServiceFactory use
        ode.api.ISession to acquire a
        ode.model.Session. In the Ice case, Glacier2
        contacts ode.api.ISession itself and returns a
        ServiceFactory remote proxy. From both ServiceFactory
        instances, it is possible but not necessary to access
        ode.api.ISession.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.ISession:
                raise RuntimeError('ode.api.ISession is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ISession', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::ISession'

        def ice_staticId():
            return '::ode::api::ISession'
        ice_staticId = staticmethod(ice_staticId)

        def createSession_async(self, _cb, p, credentials, current=None):
            """
            Creates a new session and returns it to the user.
            Arguments:
            _cb -- The asynchronous callback object.
            p -- 
            credentials -- 
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if principal is null
            SecurityViolation -- if the password check fails
            """
            pass

        def createUserSession_async(self, _cb, timeToLiveMilliseconds, timeToIdleMilliseconds, defaultGroup, current=None):
            """
            Allows a user to open up another session for him/herself with the given
            defaults without needing to re-enter password.
            Arguments:
            _cb -- The asynchronous callback object.
            timeToLiveMilliseconds -- 
            timeToIdleMilliseconds -- 
            defaultGroup -- 
            current -- The Current object for the invocation.
            """
            pass

        def createSessionWithTimeout_async(self, _cb, principal, timeToLiveMilliseconds, current=None):
            """
            Allows an admin to create a ode.model.meta.Session for the give
            ode.sys.Principal.
            Arguments:
            _cb -- The asynchronous callback object.
            principal -- Non-null ode.sys.Principal with the target user's name
            timeToLiveMilliseconds -- The time that this ode.model.meta.Session has until destruction. This is useful to override the server default so that an initial delay before the user is given the token will not be construed as idle time. A value less than 1 will cause the default max timeToLive to be used; but timeToIdle will be disabled.
            current -- The Current object for the invocation.
            """
            pass

        def createSessionWithTimeouts_async(self, _cb, principal, timeToLiveMilliseconds, timeToIdleMilliseconds, current=None):
            """
            Allows an admin to create a ode.model.meta.Session for
            the given ode.sys.Principal.
            Arguments:
            _cb -- The asynchronous callback object.
            principal -- Non-null ode.sys.Principal with the target user's name
            timeToLiveMilliseconds -- The time that this ode.model.meta.Session has until destruction. Setting the value to 0 will prevent destruction unless the session remains idle.
            timeToIdleMilliseconds -- The time that this ode.model.meta.Session can remain idle before being destroyed. Setting the value to 0 will prevent idleness based destruction.
            current -- The Current object for the invocation.
            """
            pass

        def getSession_async(self, _cb, sessionUuid, current=None):
            """
            Retrieves the session associated with this uuid, updating
            the last access time as well. Throws a
            ode.RemovedSessionException if not present, or
            a ode.SessionTimeoutException if expired.
            This method can be used as a ode.model.meta.Session ping.
            Arguments:
            _cb -- The asynchronous callback object.
            sessionUuid -- 
            current -- The Current object for the invocation.
            """
            pass

        def getReferenceCount_async(self, _cb, sessionUuid, current=None):
            """
            Retrieves the current reference count for the given uuid.
            Has the same semantics as {@code getSession}.
            Arguments:
            _cb -- The asynchronous callback object.
            sessionUuid -- 
            current -- The Current object for the invocation.
            """
            pass

        def closeSession_async(self, _cb, sess, current=None):
            """
            Closes session and releases all resources. It is preferred
            that all clients call this method as soon as possible to
            free memory, but it is possible to not call close, and
            rejoin a session later.
            The current reference count for the session is returned. If
            the session does not exist, -1. If this call caused the
            death of the session, then -2.
            Arguments:
            _cb -- The asynchronous callback object.
            sess -- 
            current -- The Current object for the invocation.
            """
            pass

        def getMyOpenSessions_async(self, _cb, current=None):
            """
            Returns a list of open sessions for the current user. The
            list is ordered by session creation time, so that the last
            item was created last.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getMyOpenAgentSessions_async(self, _cb, agent, current=None):
            """
            Like {@code getMyOpenSessions} but returns only those
            sessions with the given agent string.
            Arguments:
            _cb -- The asynchronous callback object.
            agent -- 
            current -- The Current object for the invocation.
            """
            pass

        def getMyOpenClientSessions_async(self, _cb, current=None):
            """
            Like {@code getMyOpenSessions} but returns only those
            sessions started by official Bhojpur ODE clients.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getInput_async(self, _cb, sess, key, current=None):
            """
            Retrieves an entry from the given
            ode.model.Session input environment.
            Arguments:
            _cb -- The asynchronous callback object.
            sess -- 
            key -- 
            current -- The Current object for the invocation.
            """
            pass

        def getOutput_async(self, _cb, sess, key, current=None):
            """
            Retrieves an entry from the ode.model.Session
            output environment.
            Arguments:
            _cb -- The asynchronous callback object.
            sess -- 
            key -- 
            current -- The Current object for the invocation.
            """
            pass

        def setInput_async(self, _cb, sess, key, value, current=None):
            """
            Places an entry in the given ode.model.Session
            input environment.
            If the value is null, the key will be removed.
            Arguments:
            _cb -- The asynchronous callback object.
            sess -- 
            key -- 
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def setOutput_async(self, _cb, sess, key, value, current=None):
            """
            Places an entry in the given ode.model.Session
            output environment. If the value is null, the key will be
            removed.
            Arguments:
            _cb -- The asynchronous callback object.
            sess -- 
            key -- 
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def getInputKeys_async(self, _cb, sess, current=None):
            """
            Retrieves all keys in the ode.model.Session input
            environment.
            Arguments:
            _cb -- The asynchronous callback object.
            sess -- 
            current -- The Current object for the invocation.
            """
            pass

        def getOutputKeys_async(self, _cb, sess, current=None):
            """
            Retrieves all keys in the ode.model.Session
            output environment.
            Arguments:
            _cb -- The asynchronous callback object.
            sess -- 
            current -- The Current object for the invocation.
            """
            pass

        def getInputs_async(self, _cb, sess, current=None):
            """
            Retrieves all inputs from the given
            ode.model.Session input environment.
            Arguments:
            _cb -- The asynchronous callback object.
            sess -- 
            current -- The Current object for the invocation.
            """
            pass

        def getOutputs_async(self, _cb, sess, current=None):
            """
            Retrieves all outputs from the given
            ode.model.Session input environment.
            Arguments:
            _cb -- The asynchronous callback object.
            sess -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_ISession)

        __repr__ = __str__

    _M_ode.api.ISessionPrx = Ice.createTempClass()
    class ISessionPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Creates a new session and returns it to the user.
        Arguments:
        p -- 
        credentials -- 
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- if principal is null
        SecurityViolation -- if the password check fails
        """
        def createSession(self, p, credentials, _ctx=None):
            return _M_ode.api.ISession._op_createSession.invoke(self, ((p, credentials), _ctx))

        """
        Creates a new session and returns it to the user.
        Arguments:
        p -- 
        credentials -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createSession(self, p, credentials, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_createSession.begin(self, ((p, credentials), _response, _ex, _sent, _ctx))

        """
        Creates a new session and returns it to the user.
        Arguments:
        p -- 
        credentials -- 
        Throws:
        ApiUsageException -- if principal is null
        SecurityViolation -- if the password check fails
        """
        def end_createSession(self, _r):
            return _M_ode.api.ISession._op_createSession.end(self, _r)

        """
        Allows a user to open up another session for him/herself with the given
        defaults without needing to re-enter password.
        Arguments:
        timeToLiveMilliseconds -- 
        timeToIdleMilliseconds -- 
        defaultGroup -- 
        _ctx -- The request context for the invocation.
        """
        def createUserSession(self, timeToLiveMilliseconds, timeToIdleMilliseconds, defaultGroup, _ctx=None):
            return _M_ode.api.ISession._op_createUserSession.invoke(self, ((timeToLiveMilliseconds, timeToIdleMilliseconds, defaultGroup), _ctx))

        """
        Allows a user to open up another session for him/herself with the given
        defaults without needing to re-enter password.
        Arguments:
        timeToLiveMilliseconds -- 
        timeToIdleMilliseconds -- 
        defaultGroup -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createUserSession(self, timeToLiveMilliseconds, timeToIdleMilliseconds, defaultGroup, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_createUserSession.begin(self, ((timeToLiveMilliseconds, timeToIdleMilliseconds, defaultGroup), _response, _ex, _sent, _ctx))

        """
        Allows a user to open up another session for him/herself with the given
        defaults without needing to re-enter password.
        Arguments:
        timeToLiveMilliseconds -- 
        timeToIdleMilliseconds -- 
        defaultGroup -- 
        """
        def end_createUserSession(self, _r):
            return _M_ode.api.ISession._op_createUserSession.end(self, _r)

        """
        Allows an admin to create a ode.model.meta.Session for the give
        ode.sys.Principal.
        Arguments:
        principal -- Non-null ode.sys.Principal with the target user's name
        timeToLiveMilliseconds -- The time that this ode.model.meta.Session has until destruction. This is useful to override the server default so that an initial delay before the user is given the token will not be construed as idle time. A value less than 1 will cause the default max timeToLive to be used; but timeToIdle will be disabled.
        _ctx -- The request context for the invocation.
        """
        def createSessionWithTimeout(self, principal, timeToLiveMilliseconds, _ctx=None):
            return _M_ode.api.ISession._op_createSessionWithTimeout.invoke(self, ((principal, timeToLiveMilliseconds), _ctx))

        """
        Allows an admin to create a ode.model.meta.Session for the give
        ode.sys.Principal.
        Arguments:
        principal -- Non-null ode.sys.Principal with the target user's name
        timeToLiveMilliseconds -- The time that this ode.model.meta.Session has until destruction. This is useful to override the server default so that an initial delay before the user is given the token will not be construed as idle time. A value less than 1 will cause the default max timeToLive to be used; but timeToIdle will be disabled.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createSessionWithTimeout(self, principal, timeToLiveMilliseconds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_createSessionWithTimeout.begin(self, ((principal, timeToLiveMilliseconds), _response, _ex, _sent, _ctx))

        """
        Allows an admin to create a ode.model.meta.Session for the give
        ode.sys.Principal.
        Arguments:
        principal -- Non-null ode.sys.Principal with the target user's name
        timeToLiveMilliseconds -- The time that this ode.model.meta.Session has until destruction. This is useful to override the server default so that an initial delay before the user is given the token will not be construed as idle time. A value less than 1 will cause the default max timeToLive to be used; but timeToIdle will be disabled.
        """
        def end_createSessionWithTimeout(self, _r):
            return _M_ode.api.ISession._op_createSessionWithTimeout.end(self, _r)

        """
        Allows an admin to create a ode.model.meta.Session for
        the given ode.sys.Principal.
        Arguments:
        principal -- Non-null ode.sys.Principal with the target user's name
        timeToLiveMilliseconds -- The time that this ode.model.meta.Session has until destruction. Setting the value to 0 will prevent destruction unless the session remains idle.
        timeToIdleMilliseconds -- The time that this ode.model.meta.Session can remain idle before being destroyed. Setting the value to 0 will prevent idleness based destruction.
        _ctx -- The request context for the invocation.
        """
        def createSessionWithTimeouts(self, principal, timeToLiveMilliseconds, timeToIdleMilliseconds, _ctx=None):
            return _M_ode.api.ISession._op_createSessionWithTimeouts.invoke(self, ((principal, timeToLiveMilliseconds, timeToIdleMilliseconds), _ctx))

        """
        Allows an admin to create a ode.model.meta.Session for
        the given ode.sys.Principal.
        Arguments:
        principal -- Non-null ode.sys.Principal with the target user's name
        timeToLiveMilliseconds -- The time that this ode.model.meta.Session has until destruction. Setting the value to 0 will prevent destruction unless the session remains idle.
        timeToIdleMilliseconds -- The time that this ode.model.meta.Session can remain idle before being destroyed. Setting the value to 0 will prevent idleness based destruction.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createSessionWithTimeouts(self, principal, timeToLiveMilliseconds, timeToIdleMilliseconds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_createSessionWithTimeouts.begin(self, ((principal, timeToLiveMilliseconds, timeToIdleMilliseconds), _response, _ex, _sent, _ctx))

        """
        Allows an admin to create a ode.model.meta.Session for
        the given ode.sys.Principal.
        Arguments:
        principal -- Non-null ode.sys.Principal with the target user's name
        timeToLiveMilliseconds -- The time that this ode.model.meta.Session has until destruction. Setting the value to 0 will prevent destruction unless the session remains idle.
        timeToIdleMilliseconds -- The time that this ode.model.meta.Session can remain idle before being destroyed. Setting the value to 0 will prevent idleness based destruction.
        """
        def end_createSessionWithTimeouts(self, _r):
            return _M_ode.api.ISession._op_createSessionWithTimeouts.end(self, _r)

        """
        Retrieves the session associated with this uuid, updating
        the last access time as well. Throws a
        ode.RemovedSessionException if not present, or
        a ode.SessionTimeoutException if expired.
        This method can be used as a ode.model.meta.Session ping.
        Arguments:
        sessionUuid -- 
        _ctx -- The request context for the invocation.
        """
        def getSession(self, sessionUuid, _ctx=None):
            return _M_ode.api.ISession._op_getSession.invoke(self, ((sessionUuid, ), _ctx))

        """
        Retrieves the session associated with this uuid, updating
        the last access time as well. Throws a
        ode.RemovedSessionException if not present, or
        a ode.SessionTimeoutException if expired.
        This method can be used as a ode.model.meta.Session ping.
        Arguments:
        sessionUuid -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getSession(self, sessionUuid, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getSession.begin(self, ((sessionUuid, ), _response, _ex, _sent, _ctx))

        """
        Retrieves the session associated with this uuid, updating
        the last access time as well. Throws a
        ode.RemovedSessionException if not present, or
        a ode.SessionTimeoutException if expired.
        This method can be used as a ode.model.meta.Session ping.
        Arguments:
        sessionUuid -- 
        """
        def end_getSession(self, _r):
            return _M_ode.api.ISession._op_getSession.end(self, _r)

        """
        Retrieves the current reference count for the given uuid.
        Has the same semantics as {@code getSession}.
        Arguments:
        sessionUuid -- 
        _ctx -- The request context for the invocation.
        """
        def getReferenceCount(self, sessionUuid, _ctx=None):
            return _M_ode.api.ISession._op_getReferenceCount.invoke(self, ((sessionUuid, ), _ctx))

        """
        Retrieves the current reference count for the given uuid.
        Has the same semantics as {@code getSession}.
        Arguments:
        sessionUuid -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getReferenceCount(self, sessionUuid, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getReferenceCount.begin(self, ((sessionUuid, ), _response, _ex, _sent, _ctx))

        """
        Retrieves the current reference count for the given uuid.
        Has the same semantics as {@code getSession}.
        Arguments:
        sessionUuid -- 
        """
        def end_getReferenceCount(self, _r):
            return _M_ode.api.ISession._op_getReferenceCount.end(self, _r)

        """
        Closes session and releases all resources. It is preferred
        that all clients call this method as soon as possible to
        free memory, but it is possible to not call close, and
        rejoin a session later.
        The current reference count for the session is returned. If
        the session does not exist, -1. If this call caused the
        death of the session, then -2.
        Arguments:
        sess -- 
        _ctx -- The request context for the invocation.
        """
        def closeSession(self, sess, _ctx=None):
            return _M_ode.api.ISession._op_closeSession.invoke(self, ((sess, ), _ctx))

        """
        Closes session and releases all resources. It is preferred
        that all clients call this method as soon as possible to
        free memory, but it is possible to not call close, and
        rejoin a session later.
        The current reference count for the session is returned. If
        the session does not exist, -1. If this call caused the
        death of the session, then -2.
        Arguments:
        sess -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_closeSession(self, sess, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_closeSession.begin(self, ((sess, ), _response, _ex, _sent, _ctx))

        """
        Closes session and releases all resources. It is preferred
        that all clients call this method as soon as possible to
        free memory, but it is possible to not call close, and
        rejoin a session later.
        The current reference count for the session is returned. If
        the session does not exist, -1. If this call caused the
        death of the session, then -2.
        Arguments:
        sess -- 
        """
        def end_closeSession(self, _r):
            return _M_ode.api.ISession._op_closeSession.end(self, _r)

        """
        Returns a list of open sessions for the current user. The
        list is ordered by session creation time, so that the last
        item was created last.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getMyOpenSessions(self, _ctx=None):
            return _M_ode.api.ISession._op_getMyOpenSessions.invoke(self, ((), _ctx))

        """
        Returns a list of open sessions for the current user. The
        list is ordered by session creation time, so that the last
        item was created last.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMyOpenSessions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getMyOpenSessions.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns a list of open sessions for the current user. The
        list is ordered by session creation time, so that the last
        item was created last.
        Arguments:
        """
        def end_getMyOpenSessions(self, _r):
            return _M_ode.api.ISession._op_getMyOpenSessions.end(self, _r)

        """
        Like {@code getMyOpenSessions} but returns only those
        sessions with the given agent string.
        Arguments:
        agent -- 
        _ctx -- The request context for the invocation.
        """
        def getMyOpenAgentSessions(self, agent, _ctx=None):
            return _M_ode.api.ISession._op_getMyOpenAgentSessions.invoke(self, ((agent, ), _ctx))

        """
        Like {@code getMyOpenSessions} but returns only those
        sessions with the given agent string.
        Arguments:
        agent -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMyOpenAgentSessions(self, agent, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getMyOpenAgentSessions.begin(self, ((agent, ), _response, _ex, _sent, _ctx))

        """
        Like {@code getMyOpenSessions} but returns only those
        sessions with the given agent string.
        Arguments:
        agent -- 
        """
        def end_getMyOpenAgentSessions(self, _r):
            return _M_ode.api.ISession._op_getMyOpenAgentSessions.end(self, _r)

        """
        Like {@code getMyOpenSessions} but returns only those
        sessions started by official ODE clients.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getMyOpenClientSessions(self, _ctx=None):
            return _M_ode.api.ISession._op_getMyOpenClientSessions.invoke(self, ((), _ctx))

        """
        Like {@code getMyOpenSessions} but returns only those
        sessions started by official ODE clients.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMyOpenClientSessions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getMyOpenClientSessions.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Like {@code getMyOpenSessions} but returns only those
        sessions started by official ODE clients.
        Arguments:
        """
        def end_getMyOpenClientSessions(self, _r):
            return _M_ode.api.ISession._op_getMyOpenClientSessions.end(self, _r)

        """
        Retrieves an entry from the given
        ode.model.Session input environment.
        Arguments:
        sess -- 
        key -- 
        _ctx -- The request context for the invocation.
        """
        def getInput(self, sess, key, _ctx=None):
            return _M_ode.api.ISession._op_getInput.invoke(self, ((sess, key), _ctx))

        """
        Retrieves an entry from the given
        ode.model.Session input environment.
        Arguments:
        sess -- 
        key -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getInput(self, sess, key, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getInput.begin(self, ((sess, key), _response, _ex, _sent, _ctx))

        """
        Retrieves an entry from the given
        ode.model.Session input environment.
        Arguments:
        sess -- 
        key -- 
        """
        def end_getInput(self, _r):
            return _M_ode.api.ISession._op_getInput.end(self, _r)

        """
        Retrieves an entry from the ode.model.Session
        output environment.
        Arguments:
        sess -- 
        key -- 
        _ctx -- The request context for the invocation.
        """
        def getOutput(self, sess, key, _ctx=None):
            return _M_ode.api.ISession._op_getOutput.invoke(self, ((sess, key), _ctx))

        """
        Retrieves an entry from the ode.model.Session
        output environment.
        Arguments:
        sess -- 
        key -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getOutput(self, sess, key, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getOutput.begin(self, ((sess, key), _response, _ex, _sent, _ctx))

        """
        Retrieves an entry from the ode.model.Session
        output environment.
        Arguments:
        sess -- 
        key -- 
        """
        def end_getOutput(self, _r):
            return _M_ode.api.ISession._op_getOutput.end(self, _r)

        """
        Places an entry in the given ode.model.Session
        input environment.
        If the value is null, the key will be removed.
        Arguments:
        sess -- 
        key -- 
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setInput(self, sess, key, value, _ctx=None):
            return _M_ode.api.ISession._op_setInput.invoke(self, ((sess, key, value), _ctx))

        """
        Places an entry in the given ode.model.Session
        input environment.
        If the value is null, the key will be removed.
        Arguments:
        sess -- 
        key -- 
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setInput(self, sess, key, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_setInput.begin(self, ((sess, key, value), _response, _ex, _sent, _ctx))

        """
        Places an entry in the given ode.model.Session
        input environment.
        If the value is null, the key will be removed.
        Arguments:
        sess -- 
        key -- 
        value -- 
        """
        def end_setInput(self, _r):
            return _M_ode.api.ISession._op_setInput.end(self, _r)

        """
        Places an entry in the given ode.model.Session
        output environment. If the value is null, the key will be
        removed.
        Arguments:
        sess -- 
        key -- 
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setOutput(self, sess, key, value, _ctx=None):
            return _M_ode.api.ISession._op_setOutput.invoke(self, ((sess, key, value), _ctx))

        """
        Places an entry in the given ode.model.Session
        output environment. If the value is null, the key will be
        removed.
        Arguments:
        sess -- 
        key -- 
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setOutput(self, sess, key, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_setOutput.begin(self, ((sess, key, value), _response, _ex, _sent, _ctx))

        """
        Places an entry in the given ode.model.Session
        output environment. If the value is null, the key will be
        removed.
        Arguments:
        sess -- 
        key -- 
        value -- 
        """
        def end_setOutput(self, _r):
            return _M_ode.api.ISession._op_setOutput.end(self, _r)

        """
        Retrieves all keys in the ode.model.Session input
        environment.
        Arguments:
        sess -- 
        _ctx -- The request context for the invocation.
        Returns: a java.util.Set of keys
        """
        def getInputKeys(self, sess, _ctx=None):
            return _M_ode.api.ISession._op_getInputKeys.invoke(self, ((sess, ), _ctx))

        """
        Retrieves all keys in the ode.model.Session input
        environment.
        Arguments:
        sess -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getInputKeys(self, sess, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getInputKeys.begin(self, ((sess, ), _response, _ex, _sent, _ctx))

        """
        Retrieves all keys in the ode.model.Session input
        environment.
        Arguments:
        sess -- 
        Returns: a java.util.Set of keys
        """
        def end_getInputKeys(self, _r):
            return _M_ode.api.ISession._op_getInputKeys.end(self, _r)

        """
        Retrieves all keys in the ode.model.Session
        output environment.
        Arguments:
        sess -- 
        _ctx -- The request context for the invocation.
        """
        def getOutputKeys(self, sess, _ctx=None):
            return _M_ode.api.ISession._op_getOutputKeys.invoke(self, ((sess, ), _ctx))

        """
        Retrieves all keys in the ode.model.Session
        output environment.
        Arguments:
        sess -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getOutputKeys(self, sess, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getOutputKeys.begin(self, ((sess, ), _response, _ex, _sent, _ctx))

        """
        Retrieves all keys in the ode.model.Session
        output environment.
        Arguments:
        sess -- 
        """
        def end_getOutputKeys(self, _r):
            return _M_ode.api.ISession._op_getOutputKeys.end(self, _r)

        """
        Retrieves all inputs from the given
        ode.model.Session input environment.
        Arguments:
        sess -- 
        _ctx -- The request context for the invocation.
        """
        def getInputs(self, sess, _ctx=None):
            return _M_ode.api.ISession._op_getInputs.invoke(self, ((sess, ), _ctx))

        """
        Retrieves all inputs from the given
        ode.model.Session input environment.
        Arguments:
        sess -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getInputs(self, sess, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getInputs.begin(self, ((sess, ), _response, _ex, _sent, _ctx))

        """
        Retrieves all inputs from the given
        ode.model.Session input environment.
        Arguments:
        sess -- 
        """
        def end_getInputs(self, _r):
            return _M_ode.api.ISession._op_getInputs.end(self, _r)

        """
        Retrieves all outputs from the given
        ode.model.Session input environment.
        Arguments:
        sess -- 
        _ctx -- The request context for the invocation.
        """
        def getOutputs(self, sess, _ctx=None):
            return _M_ode.api.ISession._op_getOutputs.invoke(self, ((sess, ), _ctx))

        """
        Retrieves all outputs from the given
        ode.model.Session input environment.
        Arguments:
        sess -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getOutputs(self, sess, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ISession._op_getOutputs.begin(self, ((sess, ), _response, _ex, _sent, _ctx))

        """
        Retrieves all outputs from the given
        ode.model.Session input environment.
        Arguments:
        sess -- 
        """
        def end_getOutputs(self, _r):
            return _M_ode.api.ISession._op_getOutputs.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ISessionPrx.ice_checkedCast(proxy, '::ode::api::ISession', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ISessionPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::ISession'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ISessionPrx = IcePy.defineProxy('::ode::api::ISession', ISessionPrx)

    _M_ode.api._t_ISession = IcePy.defineClass('::ode::api::ISession', ISession, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    ISession._ice_type = _M_ode.api._t_ISession

    ISession._op_createSession = IcePy.Operation('createSession', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.sys._t_Principal, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.model._t_Session, False, 0), (_M_ode._t_ServerError, _M_Glacier2._t_CannotCreateSessionException))
    ISession._op_createUserSession = IcePy.Operation('createUserSession', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.model._t_Session, False, 0), (_M_ode._t_ServerError, _M_Glacier2._t_CannotCreateSessionException))
    ISession._op_createSessionWithTimeout = IcePy.Operation('createSessionWithTimeout', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.sys._t_Principal, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.model._t_Session, False, 0), (_M_ode._t_ServerError, _M_Glacier2._t_CannotCreateSessionException))
    ISession._op_createSessionWithTimeouts = IcePy.Operation('createSessionWithTimeouts', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.sys._t_Principal, False, 0), ((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.model._t_Session, False, 0), (_M_ode._t_ServerError, _M_Glacier2._t_CannotCreateSessionException))
    ISession._op_getSession = IcePy.Operation('getSession', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.model._t_Session, False, 0), (_M_ode._t_ServerError,))
    ISession._op_getReferenceCount = IcePy.Operation('getReferenceCount', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    ISession._op_closeSession = IcePy.Operation('closeSession', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Session, False, 0),), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    ISession._op_getMyOpenSessions = IcePy.Operation('getMyOpenSessions', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_SessionList, False, 0), (_M_ode._t_ServerError,))
    ISession._op_getMyOpenAgentSessions = IcePy.Operation('getMyOpenAgentSessions', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_SessionList, False, 0), (_M_ode._t_ServerError,))
    ISession._op_getMyOpenClientSessions = IcePy.Operation('getMyOpenClientSessions', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_SessionList, False, 0), (_M_ode._t_ServerError,))
    ISession._op_getInput = IcePy.Operation('getInput', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode._t_RType, False, 0), (_M_ode._t_ServerError,))
    ISession._op_getOutput = IcePy.Operation('getOutput', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode._t_RType, False, 0), (_M_ode._t_ServerError,))
    ISession._op_setInput = IcePy.Operation('setInput', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), _M_ode._t_RType, False, 0)), (), None, (_M_ode._t_ServerError,))
    ISession._op_setOutput = IcePy.Operation('setOutput', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), _M_ode._t_RType, False, 0)), (), None, (_M_ode._t_ServerError,))
    ISession._op_getInputKeys = IcePy.Operation('getInputKeys', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_StringSet, False, 0), (_M_ode._t_ServerError,))
    ISession._op_getOutputKeys = IcePy.Operation('getOutputKeys', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_StringSet, False, 0), (_M_ode._t_ServerError,))
    ISession._op_getInputs = IcePy.Operation('getInputs', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode._t_RTypeDict, False, 0), (_M_ode._t_ServerError,))
    ISession._op_getOutputs = IcePy.Operation('getOutputs', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode._t_RTypeDict, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.ISession = ISession
    del ISession

    _M_ode.api.ISessionPrx = ISessionPrx
    del ISessionPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
