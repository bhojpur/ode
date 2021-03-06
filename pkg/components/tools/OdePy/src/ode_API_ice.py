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
import ode_cmd_API_ice
import ode_ServerErrors_ice
import ode_ModelF_ice
import ode_ServicesF_ice
import ode_System_ice
import ode_Collections_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.cmd
_M_ode.cmd = Ice.openModule('ode.cmd')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'
_M_ode.api.__doc__ = """
The ode::api module defines all the central verbs for working with ODE.server.
 Arguments and return values consist of those
types defined in the other ice files available here. With no
further custom code, it is possible to interoperate with
ODE.server simply via the definitions here. Start with the
ServiceFactory definition at the end of this file.
 Note: Using these types is significantly easier in combination with
the JavaDocs of the ODE.server, specifically the ode.api
package. Where not further noted below, the follow mappings between
ode.api argument types and ode::api argument types hold: 
+-----------------------+------------------------+
|        ode.api        |      ode::api          |
+-----------------------+------------------------+
|java.lang.Class        |string                  |
+-----------------------+------------------------+
|java.util.Set          |java.util.List/vector   |
+-----------------------+------------------------+
|IPojo options (Map)    |ode::sys::ParamMap      |
+-----------------------+------------------------+
|If null needed         |ode::RType subclass     |
+-----------------------+------------------------+
|...                    |...                     |
+-----------------------+------------------------+
"""

if 'ClientCallback' not in _M_ode.api.__dict__:
    _M_ode.api.ClientCallback = Ice.createTempClass()
    class ClientCallback(Ice.Object):
        """
        Primary callback interface for interaction between client and
        server session (""ServiceFactory""). Where possible these methods
        will be called one-way to prevent clients from hanging the server.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.ClientCallback:
                raise RuntimeError('ode.api.ClientCallback is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ClientCallback')

        def ice_id(self, current=None):
            return '::ode::api::ClientCallback'

        def ice_staticId():
            return '::ode::api::ClientCallback'
        ice_staticId = staticmethod(ice_staticId)

        def requestHeartbeat(self, current=None):
            """
            Heartbeat-request made by the server to guarantee that the client
            is alive. If the client is still active, then some method should
            be made on the server to update the last idle time.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def sessionClosed(self, current=None):
            """
            The session to which this ServiceFactory is connected has been
            closed. Almost no further method calls (if any) are possible.
            Create a new session via ode.client.createSession()
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def shutdownIn(self, milliseconds, current=None):
            """
            Message that the server will be shutting down in the
            given number of milliseconds, after which all new and
            running method invocations will receive a CancelledException.
            Arguments:
            milliseconds -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_ClientCallback)

        __repr__ = __str__

    _M_ode.api.ClientCallbackPrx = Ice.createTempClass()
    class ClientCallbackPrx(Ice.ObjectPrx):

        """
        Heartbeat-request made by the server to guarantee that the client
        is alive. If the client is still active, then some method should
        be made on the server to update the last idle time.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def requestHeartbeat(self, _ctx=None):
            return _M_ode.api.ClientCallback._op_requestHeartbeat.invoke(self, ((), _ctx))

        """
        Heartbeat-request made by the server to guarantee that the client
        is alive. If the client is still active, then some method should
        be made on the server to update the last idle time.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_requestHeartbeat(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ClientCallback._op_requestHeartbeat.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Heartbeat-request made by the server to guarantee that the client
        is alive. If the client is still active, then some method should
        be made on the server to update the last idle time.
        Arguments:
        """
        def end_requestHeartbeat(self, _r):
            return _M_ode.api.ClientCallback._op_requestHeartbeat.end(self, _r)

        """
        The session to which this ServiceFactory is connected has been
        closed. Almost no further method calls (if any) are possible.
        Create a new session via ode.client.createSession()
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def sessionClosed(self, _ctx=None):
            return _M_ode.api.ClientCallback._op_sessionClosed.invoke(self, ((), _ctx))

        """
        The session to which this ServiceFactory is connected has been
        closed. Almost no further method calls (if any) are possible.
        Create a new session via ode.client.createSession()
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_sessionClosed(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ClientCallback._op_sessionClosed.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        The session to which this ServiceFactory is connected has been
        closed. Almost no further method calls (if any) are possible.
        Create a new session via ode.client.createSession()
        Arguments:
        """
        def end_sessionClosed(self, _r):
            return _M_ode.api.ClientCallback._op_sessionClosed.end(self, _r)

        """
        Message that the server will be shutting down in the
        given number of milliseconds, after which all new and
        running method invocations will receive a CancelledException.
        Arguments:
        milliseconds -- 
        _ctx -- The request context for the invocation.
        """
        def shutdownIn(self, milliseconds, _ctx=None):
            return _M_ode.api.ClientCallback._op_shutdownIn.invoke(self, ((milliseconds, ), _ctx))

        """
        Message that the server will be shutting down in the
        given number of milliseconds, after which all new and
        running method invocations will receive a CancelledException.
        Arguments:
        milliseconds -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_shutdownIn(self, milliseconds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ClientCallback._op_shutdownIn.begin(self, ((milliseconds, ), _response, _ex, _sent, _ctx))

        """
        Message that the server will be shutting down in the
        given number of milliseconds, after which all new and
        running method invocations will receive a CancelledException.
        Arguments:
        milliseconds -- 
        """
        def end_shutdownIn(self, _r):
            return _M_ode.api.ClientCallback._op_shutdownIn.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ClientCallbackPrx.ice_checkedCast(proxy, '::ode::api::ClientCallback', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ClientCallbackPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::ClientCallback'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ClientCallbackPrx = IcePy.defineProxy('::ode::api::ClientCallback', ClientCallbackPrx)

    _M_ode.api._t_ClientCallback = IcePy.defineClass('::ode::api::ClientCallback', ClientCallback, -1, (), True, False, None, (), ())
    ClientCallback._ice_type = _M_ode.api._t_ClientCallback

    ClientCallback._op_requestHeartbeat = IcePy.Operation('requestHeartbeat', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    ClientCallback._op_sessionClosed = IcePy.Operation('sessionClosed', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    ClientCallback._op_shutdownIn = IcePy.Operation('shutdownIn', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_long, False, 0),), (), None, ())

    _M_ode.api.ClientCallback = ClientCallback
    del ClientCallback

    _M_ode.api.ClientCallbackPrx = ClientCallbackPrx
    del ClientCallbackPrx

if 'ServiceFactory' not in _M_ode.api.__dict__:
    _M_ode.api.ServiceFactory = Ice.createTempClass()
    class ServiceFactory(_M_ode.cmd.Session):
        """
        Starting point for all ODE.server interaction.
         A ServiceFactory once acquired can be used to create any number
        of service proxies to the server. Most services implement
        ServiceInterface or its subinterface
        StatefulServiceInterface.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.ServiceFactory:
                raise RuntimeError('ode.api.ServiceFactory is an abstract class')

        def ice_ids(self, current=None):
            return ('::Glacier2::Session', '::Ice::Object', '::ode::api::ServiceFactory', '::ode::cmd::Session')

        def ice_id(self, current=None):
            return '::ode::api::ServiceFactory'

        def ice_staticId():
            return '::ode::api::ServiceFactory'
        ice_staticId = staticmethod(ice_staticId)

        def getSecurityContexts(self, current=None):
            """
            Provides a list of all valid security contexts for this session.
            Each of the returned ode.model.IObject instances can
            be passed to {@code setSecurityContext}.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def setSecurityContext(self, obj, current=None):
            """
            Changes the security context for the current session.
             A security context limits the set of objects which will
            be returned by all queries and restricts what updates
            can be made. 
             Current valid values for security context:
            ode.model.ExperimenterGroup - logs into a
            specific group
            ode.model.Share - uses IShare to activate a
            share
             Passing an unloaded version of either object type will change
            the way the current session operates. Note: only objects which
            are returned by the {@code getSecurityContext} method are
            considered valid. Any other instance will cause an exception to
            be thrown. 
            Example usage in Python:
            sf = client.createSession()
            objs = sf.getSecurityContexts()
            old = sf.setSecurityContext(objs\[-1])
            Arguments:
            obj -- 
            current -- The Current object for the invocation.
            """
            pass

        def setSecurityPassword(self, password, current=None):
            """
            Re-validates the password for the current session. This prevents
            See methods that mention "HasPassword".
            Arguments:
            password -- 
            current -- The Current object for the invocation.
            """
            pass

        def getAdminService(self, current=None):
            pass

        def getConfigService(self, current=None):
            pass

        def getContainerService(self, current=None):
            pass

        def getLdapService(self, current=None):
            pass

        def getPixelsService(self, current=None):
            pass

        def getProjectionService(self, current=None):
            pass

        def getQueryService(self, current=None):
            pass

        def getRenderingSettingsService(self, current=None):
            pass

        def getRepositoryInfoService(self, current=None):
            pass

        def getRoiService(self, current=None):
            pass

        def getScriptService(self, current=None):
            pass

        def getSessionService(self, current=None):
            pass

        def getShareService(self, current=None):
            pass

        def getTimelineService(self, current=None):
            pass

        def getTypesService(self, current=None):
            pass

        def getUpdateService(self, current=None):
            pass

        def getMetadataService(self, current=None):
            pass

        def createExporter(self, current=None):
            pass

        def createJobHandle(self, current=None):
            pass

        def createRawFileStore(self, current=None):
            pass

        def createRawPixelsStore(self, current=None):
            pass

        def createRenderingEngine(self, current=None):
            pass

        def createSearchService(self, current=None):
            pass

        def createThumbnailStore(self, current=None):
            pass

        def sharedResources(self, current=None):
            """
            Returns a reference to a back-end manager. The
            ode.grid.SharedResources service provides look ups
            for various facilities offered by ODE:
            ODE.scripts
            ODE.tables
            These facilities may or may not be available on first request.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def getByName(self, name, current=None):
            """
            Allows looking up any stateless service by name.
            See Constants.ice for examples of services.
            If a service has been added by third-parties,
            getByName can be used even though no concrete
            method is available.
            Arguments:
            name -- 
            current -- The Current object for the invocation.
            """
            pass

        def createByName(self, name, current=None):
            """
            Allows looking up any stateful service by name.
            See Constants.ice for examples of services.
            If a service has been added by third-parties,
            createByName can be used even though no concrete
            method is available.
            Arguments:
            name -- 
            current -- The Current object for the invocation.
            """
            pass

        def subscribe(self, topicName, prx, current=None):
            """
            Subscribe to a given topic. The topic must exist and the user must
            have sufficient permissions for that topic. Further the proxy object
            must match the required type for the topic as encoded in the topic
            name.
            Arguments:
            topicName -- 
            prx -- 
            current -- The Current object for the invocation.
            """
            pass

        def setCallback(self, callback, current=None):
            """
            Sets the single callback used by the ServiceFactory
            to communicate with the client application. A default
            callback is set by the ode::client object on
            session creation which should suffice for most usage.
            See the client object's documentation in each language
            mapping for ways to use the callback.
            Arguments:
            callback -- 
            current -- The Current object for the invocation.
            """
            pass

        def closeOnDestroy(self, current=None):
            """
            Marks the session for closure rather than detachment, which will
            be triggered by the destruction of the Glacier2 connection via
            router.destroySession()
            Closing the session rather the detaching is more secure, since all
            resources are removed from the server and can safely be set once
            it is clear that a client is finished with those resources.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def detachOnDestroy(self, current=None):
            """
            Marks the session for detachment rather than closure, which will
            be triggered by the destruction of the Glacier2 connection via
            router.destroySession()
            This is the default and allows a lost session to be reconnected,
            at a slight security cost since the session will persist longer
            and can be used by others if the UUID is intercepted.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def activeServices(self, current=None):
            """
            Returns a list of string ids for currently active services. This will
            _not_ keep services alive, and in fact checks for all expired services
            and removes them.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def keepAllAlive(self, proxies, current=None):
            """
            Requests that the given services be marked as alive. It is
            possible that one of the services has already timed out, in which
            case the returned long value will be non-zero.
            Specifically, the bit representing the 0-based index will be 1:
            {@code
            if (retval & 1&lt;&lt;idx == 1&lt;&lt;idx) { // not alive }
            }
            Except for fatal server or session errors, this method should never
            throw an exception.
            Arguments:
            proxies -- 
            current -- The Current object for the invocation.
            """
            pass

        def keepAlive(self, proxy, current=None):
            """
            Returns true if the given service is alive.
            Except for fatal server or session errors, this method should never
            throw an exception.
            Arguments:
            proxy -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_ServiceFactory)

        __repr__ = __str__

    _M_ode.api.ServiceFactoryPrx = Ice.createTempClass()
    class ServiceFactoryPrx(_M_ode.cmd.SessionPrx):

        """
        Provides a list of all valid security contexts for this session.
        Each of the returned ode.model.IObject instances can
        be passed to {@code setSecurityContext}.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getSecurityContexts(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getSecurityContexts.invoke(self, ((), _ctx))

        """
        Provides a list of all valid security contexts for this session.
        Each of the returned ode.model.IObject instances can
        be passed to {@code setSecurityContext}.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getSecurityContexts(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getSecurityContexts.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Provides a list of all valid security contexts for this session.
        Each of the returned ode.model.IObject instances can
        be passed to {@code setSecurityContext}.
        Arguments:
        """
        def end_getSecurityContexts(self, _r):
            return _M_ode.api.ServiceFactory._op_getSecurityContexts.end(self, _r)

        """
        Changes the security context for the current session.
         A security context limits the set of objects which will
        be returned by all queries and restricts what updates
        can be made. 
         Current valid values for security context:
        ode.model.ExperimenterGroup - logs into a
        specific group
        ode.model.Share - uses IShare to activate a
        share
         Passing an unloaded version of either object type will change
        the way the current session operates. Note: only objects which
        are returned by the {@code getSecurityContext} method are
        considered valid. Any other instance will cause an exception to
        be thrown. 
        Example usage in Python:
        sf = client.createSession()
        objs = sf.getSecurityContexts()
        old = sf.setSecurityContext(objs\[-1])
        Arguments:
        obj -- 
        _ctx -- The request context for the invocation.
        """
        def setSecurityContext(self, obj, _ctx=None):
            return _M_ode.api.ServiceFactory._op_setSecurityContext.invoke(self, ((obj, ), _ctx))

        """
        Changes the security context for the current session.
         A security context limits the set of objects which will
        be returned by all queries and restricts what updates
        can be made. 
         Current valid values for security context:
        ode.model.ExperimenterGroup - logs into a
        specific group
        ode.model.Share - uses IShare to activate a
        share
         Passing an unloaded version of either object type will change
        the way the current session operates. Note: only objects which
        are returned by the {@code getSecurityContext} method are
        considered valid. Any other instance will cause an exception to
        be thrown. 
        Example usage in Python:
        sf = client.createSession()
        objs = sf.getSecurityContexts()
        old = sf.setSecurityContext(objs\[-1])
        Arguments:
        obj -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setSecurityContext(self, obj, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_setSecurityContext.begin(self, ((obj, ), _response, _ex, _sent, _ctx))

        """
        Changes the security context for the current session.
         A security context limits the set of objects which will
        be returned by all queries and restricts what updates
        can be made. 
         Current valid values for security context:
        ode.model.ExperimenterGroup - logs into a
        specific group
        ode.model.Share - uses IShare to activate a
        share
         Passing an unloaded version of either object type will change
        the way the current session operates. Note: only objects which
        are returned by the {@code getSecurityContext} method are
        considered valid. Any other instance will cause an exception to
        be thrown. 
        Example usage in Python:
        sf = client.createSession()
        objs = sf.getSecurityContexts()
        old = sf.setSecurityContext(objs\[-1])
        Arguments:
        obj -- 
        """
        def end_setSecurityContext(self, _r):
            return _M_ode.api.ServiceFactory._op_setSecurityContext.end(self, _r)

        """
        Re-validates the password for the current session. This prevents
        See methods that mention "HasPassword".
        Arguments:
        password -- 
        _ctx -- The request context for the invocation.
        """
        def setSecurityPassword(self, password, _ctx=None):
            return _M_ode.api.ServiceFactory._op_setSecurityPassword.invoke(self, ((password, ), _ctx))

        """
        Re-validates the password for the current session. This prevents
        See methods that mention "HasPassword".
        Arguments:
        password -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setSecurityPassword(self, password, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_setSecurityPassword.begin(self, ((password, ), _response, _ex, _sent, _ctx))

        """
        Re-validates the password for the current session. This prevents
        See methods that mention "HasPassword".
        Arguments:
        password -- 
        """
        def end_setSecurityPassword(self, _r):
            return _M_ode.api.ServiceFactory._op_setSecurityPassword.end(self, _r)

        def getAdminService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getAdminService.invoke(self, ((), _ctx))

        def begin_getAdminService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getAdminService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAdminService(self, _r):
            return _M_ode.api.ServiceFactory._op_getAdminService.end(self, _r)

        def getConfigService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getConfigService.invoke(self, ((), _ctx))

        def begin_getConfigService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getConfigService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getConfigService(self, _r):
            return _M_ode.api.ServiceFactory._op_getConfigService.end(self, _r)

        def getContainerService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getContainerService.invoke(self, ((), _ctx))

        def begin_getContainerService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getContainerService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getContainerService(self, _r):
            return _M_ode.api.ServiceFactory._op_getContainerService.end(self, _r)

        def getLdapService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getLdapService.invoke(self, ((), _ctx))

        def begin_getLdapService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getLdapService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLdapService(self, _r):
            return _M_ode.api.ServiceFactory._op_getLdapService.end(self, _r)

        def getPixelsService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getPixelsService.invoke(self, ((), _ctx))

        def begin_getPixelsService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getPixelsService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPixelsService(self, _r):
            return _M_ode.api.ServiceFactory._op_getPixelsService.end(self, _r)

        def getProjectionService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getProjectionService.invoke(self, ((), _ctx))

        def begin_getProjectionService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getProjectionService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getProjectionService(self, _r):
            return _M_ode.api.ServiceFactory._op_getProjectionService.end(self, _r)

        def getQueryService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getQueryService.invoke(self, ((), _ctx))

        def begin_getQueryService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getQueryService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getQueryService(self, _r):
            return _M_ode.api.ServiceFactory._op_getQueryService.end(self, _r)

        def getRenderingSettingsService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getRenderingSettingsService.invoke(self, ((), _ctx))

        def begin_getRenderingSettingsService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getRenderingSettingsService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRenderingSettingsService(self, _r):
            return _M_ode.api.ServiceFactory._op_getRenderingSettingsService.end(self, _r)

        def getRepositoryInfoService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getRepositoryInfoService.invoke(self, ((), _ctx))

        def begin_getRepositoryInfoService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getRepositoryInfoService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRepositoryInfoService(self, _r):
            return _M_ode.api.ServiceFactory._op_getRepositoryInfoService.end(self, _r)

        def getRoiService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getRoiService.invoke(self, ((), _ctx))

        def begin_getRoiService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getRoiService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRoiService(self, _r):
            return _M_ode.api.ServiceFactory._op_getRoiService.end(self, _r)

        def getScriptService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getScriptService.invoke(self, ((), _ctx))

        def begin_getScriptService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getScriptService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getScriptService(self, _r):
            return _M_ode.api.ServiceFactory._op_getScriptService.end(self, _r)

        def getSessionService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getSessionService.invoke(self, ((), _ctx))

        def begin_getSessionService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getSessionService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSessionService(self, _r):
            return _M_ode.api.ServiceFactory._op_getSessionService.end(self, _r)

        def getShareService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getShareService.invoke(self, ((), _ctx))

        def begin_getShareService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getShareService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getShareService(self, _r):
            return _M_ode.api.ServiceFactory._op_getShareService.end(self, _r)

        def getTimelineService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getTimelineService.invoke(self, ((), _ctx))

        def begin_getTimelineService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getTimelineService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTimelineService(self, _r):
            return _M_ode.api.ServiceFactory._op_getTimelineService.end(self, _r)

        def getTypesService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getTypesService.invoke(self, ((), _ctx))

        def begin_getTypesService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getTypesService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTypesService(self, _r):
            return _M_ode.api.ServiceFactory._op_getTypesService.end(self, _r)

        def getUpdateService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getUpdateService.invoke(self, ((), _ctx))

        def begin_getUpdateService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getUpdateService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getUpdateService(self, _r):
            return _M_ode.api.ServiceFactory._op_getUpdateService.end(self, _r)

        def getMetadataService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getMetadataService.invoke(self, ((), _ctx))

        def begin_getMetadataService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getMetadataService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMetadataService(self, _r):
            return _M_ode.api.ServiceFactory._op_getMetadataService.end(self, _r)

        def createExporter(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createExporter.invoke(self, ((), _ctx))

        def begin_createExporter(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createExporter.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_createExporter(self, _r):
            return _M_ode.api.ServiceFactory._op_createExporter.end(self, _r)

        def createJobHandle(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createJobHandle.invoke(self, ((), _ctx))

        def begin_createJobHandle(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createJobHandle.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_createJobHandle(self, _r):
            return _M_ode.api.ServiceFactory._op_createJobHandle.end(self, _r)

        def createRawFileStore(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createRawFileStore.invoke(self, ((), _ctx))

        def begin_createRawFileStore(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createRawFileStore.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_createRawFileStore(self, _r):
            return _M_ode.api.ServiceFactory._op_createRawFileStore.end(self, _r)

        def createRawPixelsStore(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createRawPixelsStore.invoke(self, ((), _ctx))

        def begin_createRawPixelsStore(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createRawPixelsStore.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_createRawPixelsStore(self, _r):
            return _M_ode.api.ServiceFactory._op_createRawPixelsStore.end(self, _r)

        def createRenderingEngine(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createRenderingEngine.invoke(self, ((), _ctx))

        def begin_createRenderingEngine(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createRenderingEngine.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_createRenderingEngine(self, _r):
            return _M_ode.api.ServiceFactory._op_createRenderingEngine.end(self, _r)

        def createSearchService(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createSearchService.invoke(self, ((), _ctx))

        def begin_createSearchService(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createSearchService.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_createSearchService(self, _r):
            return _M_ode.api.ServiceFactory._op_createSearchService.end(self, _r)

        def createThumbnailStore(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createThumbnailStore.invoke(self, ((), _ctx))

        def begin_createThumbnailStore(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createThumbnailStore.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_createThumbnailStore(self, _r):
            return _M_ode.api.ServiceFactory._op_createThumbnailStore.end(self, _r)

        """
        Returns a reference to a back-end manager. The
        ode.grid.SharedResources service provides look ups
        for various facilities offered by ODE:
        ODE.scripts
        ODE.tables
        These facilities may or may not be available on first request.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def sharedResources(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_sharedResources.invoke(self, ((), _ctx))

        """
        Returns a reference to a back-end manager. The
        ode.grid.SharedResources service provides look ups
        for various facilities offered by ODE:
        ODE.scripts
        ODE.tables
        These facilities may or may not be available on first request.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_sharedResources(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_sharedResources.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns a reference to a back-end manager. The
        ode.grid.SharedResources service provides look ups
        for various facilities offered by ODE:
        ODE.scripts
        ODE.tables
        These facilities may or may not be available on first request.
        Arguments:
        """
        def end_sharedResources(self, _r):
            return _M_ode.api.ServiceFactory._op_sharedResources.end(self, _r)

        """
        Allows looking up any stateless service by name.
        See Constants.ice for examples of services.
        If a service has been added by third-parties,
        getByName can be used even though no concrete
        method is available.
        Arguments:
        name -- 
        _ctx -- The request context for the invocation.
        """
        def getByName(self, name, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getByName.invoke(self, ((name, ), _ctx))

        """
        Allows looking up any stateless service by name.
        See Constants.ice for examples of services.
        If a service has been added by third-parties,
        getByName can be used even though no concrete
        method is available.
        Arguments:
        name -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getByName(self, name, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_getByName.begin(self, ((name, ), _response, _ex, _sent, _ctx))

        """
        Allows looking up any stateless service by name.
        See Constants.ice for examples of services.
        If a service has been added by third-parties,
        getByName can be used even though no concrete
        method is available.
        Arguments:
        name -- 
        """
        def end_getByName(self, _r):
            return _M_ode.api.ServiceFactory._op_getByName.end(self, _r)

        """
        Allows looking up any stateful service by name.
        See Constants.ice for examples of services.
        If a service has been added by third-parties,
        createByName can be used even though no concrete
        method is available.
        Arguments:
        name -- 
        _ctx -- The request context for the invocation.
        """
        def createByName(self, name, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createByName.invoke(self, ((name, ), _ctx))

        """
        Allows looking up any stateful service by name.
        See Constants.ice for examples of services.
        If a service has been added by third-parties,
        createByName can be used even though no concrete
        method is available.
        Arguments:
        name -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createByName(self, name, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_createByName.begin(self, ((name, ), _response, _ex, _sent, _ctx))

        """
        Allows looking up any stateful service by name.
        See Constants.ice for examples of services.
        If a service has been added by third-parties,
        createByName can be used even though no concrete
        method is available.
        Arguments:
        name -- 
        """
        def end_createByName(self, _r):
            return _M_ode.api.ServiceFactory._op_createByName.end(self, _r)

        """
        Subscribe to a given topic. The topic must exist and the user must
        have sufficient permissions for that topic. Further the proxy object
        must match the required type for the topic as encoded in the topic
        name.
        Arguments:
        topicName -- 
        prx -- 
        _ctx -- The request context for the invocation.
        """
        def subscribe(self, topicName, prx, _ctx=None):
            return _M_ode.api.ServiceFactory._op_subscribe.invoke(self, ((topicName, prx), _ctx))

        """
        Subscribe to a given topic. The topic must exist and the user must
        have sufficient permissions for that topic. Further the proxy object
        must match the required type for the topic as encoded in the topic
        name.
        Arguments:
        topicName -- 
        prx -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_subscribe(self, topicName, prx, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_subscribe.begin(self, ((topicName, prx), _response, _ex, _sent, _ctx))

        """
        Subscribe to a given topic. The topic must exist and the user must
        have sufficient permissions for that topic. Further the proxy object
        must match the required type for the topic as encoded in the topic
        name.
        Arguments:
        topicName -- 
        prx -- 
        """
        def end_subscribe(self, _r):
            return _M_ode.api.ServiceFactory._op_subscribe.end(self, _r)

        """
        Sets the single callback used by the ServiceFactory
        to communicate with the client application. A default
        callback is set by the ode::client object on
        session creation which should suffice for most usage.
        See the client object's documentation in each language
        mapping for ways to use the callback.
        Arguments:
        callback -- 
        _ctx -- The request context for the invocation.
        """
        def setCallback(self, callback, _ctx=None):
            return _M_ode.api.ServiceFactory._op_setCallback.invoke(self, ((callback, ), _ctx))

        """
        Sets the single callback used by the ServiceFactory
        to communicate with the client application. A default
        callback is set by the ode::client object on
        session creation which should suffice for most usage.
        See the client object's documentation in each language
        mapping for ways to use the callback.
        Arguments:
        callback -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setCallback(self, callback, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_setCallback.begin(self, ((callback, ), _response, _ex, _sent, _ctx))

        """
        Sets the single callback used by the ServiceFactory
        to communicate with the client application. A default
        callback is set by the ode::client object on
        session creation which should suffice for most usage.
        See the client object's documentation in each language
        mapping for ways to use the callback.
        Arguments:
        callback -- 
        """
        def end_setCallback(self, _r):
            return _M_ode.api.ServiceFactory._op_setCallback.end(self, _r)

        """
        Marks the session for closure rather than detachment, which will
        be triggered by the destruction of the Glacier2 connection via
        router.destroySession()
        Closing the session rather the detaching is more secure, since all
        resources are removed from the server and can safely be set once
        it is clear that a client is finished with those resources.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def closeOnDestroy(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_closeOnDestroy.invoke(self, ((), _ctx))

        """
        Marks the session for closure rather than detachment, which will
        be triggered by the destruction of the Glacier2 connection via
        router.destroySession()
        Closing the session rather the detaching is more secure, since all
        resources are removed from the server and can safely be set once
        it is clear that a client is finished with those resources.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_closeOnDestroy(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_closeOnDestroy.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Marks the session for closure rather than detachment, which will
        be triggered by the destruction of the Glacier2 connection via
        router.destroySession()
        Closing the session rather the detaching is more secure, since all
        resources are removed from the server and can safely be set once
        it is clear that a client is finished with those resources.
        Arguments:
        """
        def end_closeOnDestroy(self, _r):
            return _M_ode.api.ServiceFactory._op_closeOnDestroy.end(self, _r)

        """
        Marks the session for detachment rather than closure, which will
        be triggered by the destruction of the Glacier2 connection via
        router.destroySession()
        This is the default and allows a lost session to be reconnected,
        at a slight security cost since the session will persist longer
        and can be used by others if the UUID is intercepted.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def detachOnDestroy(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_detachOnDestroy.invoke(self, ((), _ctx))

        """
        Marks the session for detachment rather than closure, which will
        be triggered by the destruction of the Glacier2 connection via
        router.destroySession()
        This is the default and allows a lost session to be reconnected,
        at a slight security cost since the session will persist longer
        and can be used by others if the UUID is intercepted.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_detachOnDestroy(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_detachOnDestroy.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Marks the session for detachment rather than closure, which will
        be triggered by the destruction of the Glacier2 connection via
        router.destroySession()
        This is the default and allows a lost session to be reconnected,
        at a slight security cost since the session will persist longer
        and can be used by others if the UUID is intercepted.
        Arguments:
        """
        def end_detachOnDestroy(self, _r):
            return _M_ode.api.ServiceFactory._op_detachOnDestroy.end(self, _r)

        """
        Returns a list of string ids for currently active services. This will
        _not_ keep services alive, and in fact checks for all expired services
        and removes them.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def activeServices(self, _ctx=None):
            return _M_ode.api.ServiceFactory._op_activeServices.invoke(self, ((), _ctx))

        """
        Returns a list of string ids for currently active services. This will
        _not_ keep services alive, and in fact checks for all expired services
        and removes them.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_activeServices(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_activeServices.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns a list of string ids for currently active services. This will
        _not_ keep services alive, and in fact checks for all expired services
        and removes them.
        Arguments:
        """
        def end_activeServices(self, _r):
            return _M_ode.api.ServiceFactory._op_activeServices.end(self, _r)

        """
        Requests that the given services be marked as alive. It is
        possible that one of the services has already timed out, in which
        case the returned long value will be non-zero.
        Specifically, the bit representing the 0-based index will be 1:
        {@code
        if (retval & 1&lt;&lt;idx == 1&lt;&lt;idx) { // not alive }
        }
        Except for fatal server or session errors, this method should never
        throw an exception.
        Arguments:
        proxies -- 
        _ctx -- The request context for the invocation.
        """
        def keepAllAlive(self, proxies, _ctx=None):
            return _M_ode.api.ServiceFactory._op_keepAllAlive.invoke(self, ((proxies, ), _ctx))

        """
        Requests that the given services be marked as alive. It is
        possible that one of the services has already timed out, in which
        case the returned long value will be non-zero.
        Specifically, the bit representing the 0-based index will be 1:
        {@code
        if (retval & 1&lt;&lt;idx == 1&lt;&lt;idx) { // not alive }
        }
        Except for fatal server or session errors, this method should never
        throw an exception.
        Arguments:
        proxies -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_keepAllAlive(self, proxies, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_keepAllAlive.begin(self, ((proxies, ), _response, _ex, _sent, _ctx))

        """
        Requests that the given services be marked as alive. It is
        possible that one of the services has already timed out, in which
        case the returned long value will be non-zero.
        Specifically, the bit representing the 0-based index will be 1:
        {@code
        if (retval & 1&lt;&lt;idx == 1&lt;&lt;idx) { // not alive }
        }
        Except for fatal server or session errors, this method should never
        throw an exception.
        Arguments:
        proxies -- 
        """
        def end_keepAllAlive(self, _r):
            return _M_ode.api.ServiceFactory._op_keepAllAlive.end(self, _r)

        """
        Returns true if the given service is alive.
        Except for fatal server or session errors, this method should never
        throw an exception.
        Arguments:
        proxy -- 
        _ctx -- The request context for the invocation.
        """
        def keepAlive(self, proxy, _ctx=None):
            return _M_ode.api.ServiceFactory._op_keepAlive.invoke(self, ((proxy, ), _ctx))

        """
        Returns true if the given service is alive.
        Except for fatal server or session errors, this method should never
        throw an exception.
        Arguments:
        proxy -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_keepAlive(self, proxy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ServiceFactory._op_keepAlive.begin(self, ((proxy, ), _response, _ex, _sent, _ctx))

        """
        Returns true if the given service is alive.
        Except for fatal server or session errors, this method should never
        throw an exception.
        Arguments:
        proxy -- 
        """
        def end_keepAlive(self, _r):
            return _M_ode.api.ServiceFactory._op_keepAlive.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ServiceFactoryPrx.ice_checkedCast(proxy, '::ode::api::ServiceFactory', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ServiceFactoryPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::ServiceFactory'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ServiceFactoryPrx = IcePy.defineProxy('::ode::api::ServiceFactory', ServiceFactoryPrx)

    _M_ode.api._t_ServiceFactory = IcePy.defineClass('::ode::api::ServiceFactory', ServiceFactory, -1, (), True, False, None, (_M_ode.cmd._t_Session,), ())
    ServiceFactory._ice_type = _M_ode.api._t_ServiceFactory

    ServiceFactory._op_getSecurityContexts = IcePy.Operation('getSecurityContexts', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_setSecurityContext = IcePy.Operation('setSecurityContext', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_setSecurityPassword = IcePy.Operation('setSecurityPassword', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_ServerError,))
    ServiceFactory._op_getAdminService = IcePy.Operation('getAdminService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IAdminPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getConfigService = IcePy.Operation('getConfigService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IConfigPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getContainerService = IcePy.Operation('getContainerService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IContainerPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getLdapService = IcePy.Operation('getLdapService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_ILdapPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getPixelsService = IcePy.Operation('getPixelsService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IPixelsPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getProjectionService = IcePy.Operation('getProjectionService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IProjectionPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getQueryService = IcePy.Operation('getQueryService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IQueryPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getRenderingSettingsService = IcePy.Operation('getRenderingSettingsService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IRenderingSettingsPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getRepositoryInfoService = IcePy.Operation('getRepositoryInfoService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IRepositoryInfoPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getRoiService = IcePy.Operation('getRoiService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IRoiPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getScriptService = IcePy.Operation('getScriptService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IScriptPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getSessionService = IcePy.Operation('getSessionService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_ISessionPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getShareService = IcePy.Operation('getShareService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_ISharePrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getTimelineService = IcePy.Operation('getTimelineService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_ITimelinePrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getTypesService = IcePy.Operation('getTypesService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_ITypesPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getUpdateService = IcePy.Operation('getUpdateService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IUpdatePrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getMetadataService = IcePy.Operation('getMetadataService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_IMetadataPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_createExporter = IcePy.Operation('createExporter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_ExporterPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_createJobHandle = IcePy.Operation('createJobHandle', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_JobHandlePrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_createRawFileStore = IcePy.Operation('createRawFileStore', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_RawFileStorePrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_createRawPixelsStore = IcePy.Operation('createRawPixelsStore', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_RawPixelsStorePrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_createRenderingEngine = IcePy.Operation('createRenderingEngine', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_RenderingEnginePrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_createSearchService = IcePy.Operation('createSearchService', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_SearchPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_createThumbnailStore = IcePy.Operation('createThumbnailStore', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_ThumbnailStorePrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_sharedResources = IcePy.Operation('sharedResources', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.grid._t_SharedResourcesPrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_getByName = IcePy.Operation('getByName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_ServiceInterfacePrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_createByName = IcePy.Operation('createByName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_StatefulServiceInterfacePrx, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_subscribe = IcePy.Operation('subscribe', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_ObjectPrx, False, 0)), (), None, (_M_ode._t_ServerError,))
    ServiceFactory._op_setCallback = IcePy.Operation('setCallback', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_ClientCallbackPrx, False, 0),), (), None, (_M_ode._t_ServerError,))
    ServiceFactory._op_closeOnDestroy = IcePy.Operation('closeOnDestroy', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, (_M_ode._t_ServerError,))
    ServiceFactory._op_detachOnDestroy = IcePy.Operation('detachOnDestroy', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, (_M_ode._t_ServerError,))
    ServiceFactory._op_activeServices = IcePy.Operation('activeServices', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_StringSet, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_keepAllAlive = IcePy.Operation('keepAllAlive', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_ServiceList, False, 0),), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    ServiceFactory._op_keepAlive = IcePy.Operation('keepAlive', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_ServiceInterfacePrx, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.ServiceFactory = ServiceFactory
    del ServiceFactory

    _M_ode.api.ServiceFactoryPrx = ServiceFactoryPrx
    del ServiceFactoryPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
