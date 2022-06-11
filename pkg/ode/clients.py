#!/usr/bin/env python
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

from __future__ import print_function

from builtins import zip
from builtins import range
from builtins import object
from future.utils import bytes_to_native_str
from future.utils import native_str

__save__ = __name__
__name__ = 'ode'
try:
    api = __import__('ode.api')
    model = __import__('ode.model')
    util = __import__('ode.util')
    sys = __import__('ode.sys')
    import ode.all
finally:
    __name__ = __save__
    del __save__

sys = __import__("sys")

import threading
import logging
import IceImport
import Ice
import re
import ssl
import uuid

from past.builtins import basestring

IceImport.load("Glacier2_Router_ice")
import Glacier2

if sys.version_info >= (3, 0, 0):
    # Keep str behavior on Python 2
    from builtins import str

class BaseClient(object):
    """
    Central client-side Bhojpur ODE entry point, and should be in sync with
    OdeJava's ode.client and OdeCpp's ode::client.

    Typical usage includes::

        # Uses --Ice.Config argument or ICE_CONFIG variable
        client = ode.client()
        # Defines "ode.host"
        client = ode.client(host = host)
        # Defines "ode.host" and "ode.port"
        client = ode.client(host = host, port = port)
    """

    def __init__(self, args=None, id=None, host=None, port=None, pmap=None):
        """
        Constructor which takes one sys.argv-style list, one initialization
        data, one host string, one port integer, and one properties map, in
        that order. *However*, to simplify use, we reassign values based on
        their type with a warning printed. A cleaner approach is to use named
        parameters.
        ::
            c1 = ode.client(None, None, "host", myPort)   # Correct
            c2 = ode.client(host = "host", port = myPort) # Correct
            # Works with warning
            c3 = ode.client("host", myPort)

        Both "Ice" and "ode" prefixed properties will be parsed.

        Defines the state variables::
            __previous : InitializationData from any previous communicator, if
                         any. Used to re-initialization the client
                         post-closeSession()

            __ic       : communicator. Nullness => init() needed on
                         createSession()

            __sf       : current session. Nullness => createSession() needed.

            __resources: if non-null, hs access to this client instance and
                         will periodically call sf.keepAlive(None) in order to
                         keep any session alive. This can be enabled either
                         via the ode.keep_alive configuration property, or
                         by calling the enableKeepAlive() method.
                         Once enabled, the period cannot be adjusted during a
                         single session.

        Modifying these variables outside of the accessors can lead to
        undefined behavior.

        Equivalent to all OdeJava and OdeCpp constructors
        """
        args, id, host, port, pmap = self._repair(args, id, host, port, pmap)

        # Setting all protected values to prevent AttributeError
        self.__agent = "ODE.py"  #: See setAgent
        self.__ip = None  #: See setIP
        self.__insecure = False
        self.__previous = None
        self.__ic = None
        self.__oa = None
        self.__cb = None
        self.__sf = None
        self.__uuid = None
        self.__resources = None
        self.__lock = threading.RLock()

        # Logging
        self.__logger = logging.getLogger("ode.client")
        logging.basicConfig()  # Does nothing if already configured

        # Reassigning based on argument type

        args, id, host, port, pmap = self._repair(args, id, host, port, pmap)
        if not args:
            args = []

        # hosturl overrides all other args
        hosturl = self._check_for_hosturl(host, port, pmap)
        if hosturl:
            # ode.clients does a lot of mysterious magic autodetection.
            # If host is set it overrides the endpoint so instead set host to
            # None and store the host in a separate property ode.url.host
            host = None
            port = hosturl['port']
            args.append(self._get_endpoint_from_hosturl(hosturl))

        # Copying args since we don't really want them edited
        if args:
            # See ticket:5516 To prevent issues on systems where the base
            # class of path.path is unicode, we will encode all unicode
            # strings here.
            args = [arg.encode("utf-8") if isinstance(arg, str)
                    else arg for arg in args]

        args = [bytes_to_native_str(x) for x in args]
        # Under Python 2 this can still leave us with 'unicode'
        args = [native_str(x) for x in args]

        # Equiv to multiple constructors. #######################
        if id is None:
            id = Ice.InitializationData()

        if id.properties is None:
            id.properties = Ice.createProperties(args)

        id.properties.parseCommandLineOptions("ode", args)
        if host:
            id.properties.setProperty("ode.host", str(host))
        if hosturl:
            id.properties.setProperty("ode.url.host", hosturl['server'])
        if not port:
            port = id.properties.getPropertyWithDefault(
                "ode.port", str(ode.constants.GLACIER2PORT))
        id.properties.setProperty("ode.port", str(port))
        if pmap:
            for k, v in list(pmap.items()):
                id.properties.setProperty(str(k), str(v))

        self._initData(id)

    def _repair(self, args, id, host, port, pmap):
        """
        Takes the 5 arguments passed to the __init__ method
        and attempts to re-order them based on their types.
        This allows for simplified usage without parameter
        names.
        """
        types = [list, Ice.InitializationData, basestring, int, dict]
        original = [args, id, host, port, pmap]
        repaired = [None, None, None, None, None]

        # Check all to see if valid
        valid = True
        for i in range(0, len(types)):
            if None != original[i] and not isinstance(original[i], types[i]):
                valid = False
                break
        if valid:
            return original

        # Now try to find corrections.
        for i in range(0, len(types)):
            found = None
            for j in range(0, len(types)):
                if isinstance(original[j], types[i]):
                    if not found:
                        found = original[j]
                    else:
                        raise ode.ClientError(
                            "Found two arguments of same type: " +
                            str(types[i]))
            if found:
                repaired[i] = found
        return repaired

    def _initData(self, id):
        """
        Initializes the current client via an Ice.InitializationData
        instance. This is called by all of the constructors, but may
        also be called on createSession(name, pass) if a previous
        call to closeSession() has nulled the Ice.Communicator.
        """

        if not id:
            raise ode.ClientError("No initialization data provided.")

        # Strictly necessary for this class to work
        self._optSetProp(id, "Ice.ImplicitContext", "Shared")

        self._optSetProp(id, "Ice.ACM.Client.Timeout",
                         str(ode.constants.ACMCLIENTTIMEOUT))
        self._optSetProp(id, "Ice.ACM.Client.Heartbeat",
                         str(ode.constants.ACMCLIENTHEARTBEAT))

        self._optSetProp(id, "Ice.CacheMessageBuffers", "0")
        self._optSetProp(id, "Ice.RetryIntervals", "-1")
        self._optSetProp(id, "Ice.Default.EndpointSelection", "Ordered")
        self._optSetProp(id, "Ice.Default.PreferSecure", "1")
        self._optSetProp(id, "Ice.Plugin.IceSSL", "IceSSL:createIceSSL")

        prop = "IceSSL.Ciphers"
        try:
            if sys.platform == "darwin":
                self._optSetProp(id, prop, "(AES_256) (DH_anon.*AES)")
            elif ssl.OPENSSL_VERSION_INFO >= (1, 1):
                self._optSetProp(id, prop, "HIGH:ADH:@SECLEVEL=0")
            else:
                self._optSetProp(id, prop, "HIGH:ADH")
        except Exception:
            # OPENSSL_VERSION_INFO not available for 2.6, fall back to default
            self._optSetProp(id, prop, "HIGH:ADH")

        self._optSetProp(id, "IceSSL.VerifyDepthMax", "6")
        self._optSetProp(id, "IceSSL.VerifyPeer", "0")
        self._optSetProp(id, "IceSSL.Protocols", "tls1_0,tls1_1,tls1_2")

        # Setting block size
        self._optSetProp(
            id, "ode.block_size", str(ode.constants.DEFAULTBLOCKSIZE))

        # Setting MessageSizeMax
        self._optSetProp(
            id, "Ice.MessageSizeMax", str(ode.constants.MESSAGESIZEMAX))

        # Setting ConnectTimeout
        self.parseAndSetInt(id, "Ice.Override.ConnectTimeout",
                            ode.constants.CONNECTTIMEOUT)

        # Set large thread pool max values for all communicators
        for x in ("Client", "Server"):
            sizemax = id.properties.getProperty(
                "Ice.ThreadPool.%s.SizeMax" % x)
            if not sizemax or len(sizemax) == 0:
                id.properties.setProperty(
                    "Ice.ThreadPool.%s.SizeMax" % x, "50")

        # Port, setting to default if not present
        port = self.parseAndSetInt(id, "ode.port",
                                   ode.constants.GLACIER2PORT)

        # Default Router, set a default and then replace
        router = id.properties.getProperty("Ice.Default.Router")
        if not router or len(router) == 0:
            router = str(ode.constants.DEFAULTROUTER)
        host = id.properties.getPropertyWithDefault(
            "ode.host", """<"ode.host" not set>""")
        router = router.replace("@ode.port@", str(port))
        router = router.replace("@ode.host@", str(host))
        id.properties.setProperty("Ice.Default.Router", router)

        # Dump properties
        dump = id.properties.getProperty("ode.dump")
        if len(dump) > 0:
            m = self.getPropertyMap(id.properties)
            keys = list(m.keys())
            keys.sort()
            for key in keys:
                print("%s=%s" % (key, m[key]))

        self.__lock.acquire()
        try:
            if self.__ic:
                raise ode.ClientError("Client already initialized")

            try:
                self.__ic = Ice.initialize(id)
            except Ice.EndpointParseException:
                msg = "No host specified. "
                msg += "Use ode.client(HOSTNAME), ICE_CONFIG, or similar."
                raise ode.ClientError(msg)

            if not self.__ic:
                raise ode.ClientError("Improper initialization")

            # Register Object Factory
            try:
                import ObjectFactoryRegistrar as ofr
            except ImportError:
                from . import ObjectFactoryRegistrar as ofr
            ofr.registerObjectFactory(self.__ic, self)

            for of in list(ode.rtypes.ObjectFactories.values()):
                of.register(self.__ic)

            # Define our unique identifier (used during close/detach)
            self.__uuid = str(uuid.uuid4())
            ctx = self.__ic.getImplicitContext()
            if not ctx:
                raise ode.ClientError(
                    "Ice.ImplicitContext not set to Shared")
            ctx.put(ode.constants.CLIENTUUID, self.__uuid)

            # ticket:2951 - sending user group
            group = id.properties.getPropertyWithDefault("ode.group", "")
            if group:
                ctx.put("ode.group", group)

        finally:
            self.__lock.release()

    def _check_for_hosturl(self, host, port, pmap):
        """
        Checks whether the host is a URL, returns a dict of parameters if so
        """
        # ode/util/sessions.py may initialise this class with a property-map
        if not host and pmap and 'ode.host' in pmap:
            host = pmap['ode.host']
        if not port and pmap and 'ode.port' in pmap:
            port = pmap['ode.port']
        if not host:
            return {}

        hostmatch = re.match(
            r'(?P<protocol>\w+)://'
            r'(?P<server>[^:/]+)'
            r'(:(?P<port>\d+))?'
            r'(?P<path>/.*)?$',
            host)

        if hostmatch:
            hosturl = hostmatch.groupdict()
            if not hosturl['port']:
                hosturl['port'] = port
            if not hosturl['port']:
                default_ports = {
                    'ws': 80,
                    'wss': 443,
                    'tcp': 4063,
                    'ssl': 4064,
                }
                try:
                    hosturl['port'] = default_ports[hosturl['protocol']]
                except KeyError:
                    raise ode.ClientError(
                        "Port required for protocol: " + hosturl['protocol'])
            hosturl['port'] = int(hosturl['port'])
        else:
            hosturl = {}
        return hosturl

    def _get_endpoint_from_hosturl(self, hosturl):
        """
        Gets Ice.Default.Router from a dictionary of hosturl parameters
        """
        ice_router = (
            '--Ice.Default.Router=ODE.Glacier2/router:{protocol} '
            '-p {port} '
            '-h {server}'.format(**hosturl))
        if hosturl['path']:
            ice_router += ' -r {path}'.format(**hosturl)
        return ice_router

    def setAgent(self, agent):
        """
        Sets the ode.model.Session#getUserAgent() string for
        this client. Every session creation will be passed this argument.
        Finding open sessions with the same agent can be done via
        ode.api.ISessionPrx#getMyOpenAgentSessions(String).
        """
        self.__agent = agent

    def setIP(self, ip):
        """
        Sets the ode.model.Session#getUserIP() string for
        this client. Every session creation will be passed this argument.
        Finding open sessions with the same IP can be done via
        ode.api.ISessionPrx#getMyOpenIPSessions(ip).
        """
        self.__ip = ip

    def isSecure(self):
        """
        Specifies whether or not this client was created via a call to
        createClient with a boolean of False. If insecure, then all
        remote calls will use the insecure connection defined by the server.
        """
        return not self.__insecure

    def createClient(self, secure):
        """
        Creates a possibly insecure ode.client instance and calls
        joinSession using the current getSessionId value. If secure is False,
        then first the "ode.router.insecure" configuration property is
        retrieved from the server and used as the value of
        "Ice.Default.Router" for the new client. Any exception thrown during
        creation is passed on to the caller.

        Note: detachOnDestroy has NOT been called on the session in the
        returned client.
        Clients are responsible for doing this immediately if such desired.
        """
        props = self.getPropertyMap()
        if not secure:
            insecure = self.getSession().getConfigService().getConfigValue(
                "ode.router.insecure")
            if insecure is not None and insecure != "":
                # insecure still has @ode.host@, so we need to substitute it
                router = self.getRouter(self.getCommunicator())
                if router is not None:
                    for endpoint in router.ice_getEndpoints():
                        host = endpoint.getInfo().host
                    if host != "":
                        insecure = insecure.replace("@ode.host@", str(host))
                props["Ice.Default.Router"] = insecure
            else:
                self.__logger.warn(
                    "Could not retrieve \"ode.router.insecure\"")

        nClient = ode.client(props)
        nClient.__insecure = not secure
        nClient.setAgent("%s;secure=%s" % (self.__agent, secure))
        nClient.joinSession(self.getSessionId())
        return nClient

    def __del__(self):
        """
        Calls closeSession() and ignores any exceptions.

        Equivalent to close() in OdeJava or ode::client::~client()
        """
        try:
            self.closeSession()
        except Exception as e:
            # It is perfectly normal for the session to have been closed
            # before garbage collection
            # though for some reason I can't match this exception with the
            # Glacier2.SessionNotExistException class.
            # Using str matching instead.
            if 'Glacier2.SessionNotExistException' not in str(e.__class__):
                self.__logger.warning(
                    "..Ignoring error in client.__del__:" + str(e.__class__))

    def getCommunicator(self):
        """
        Returns the Ice.Communicator for this instance or throws
        an exception if None.
        """
        self.__lock.acquire()
        try:
            if not self.__ic:
                raise ode.ClientError(
                    "No Ice.Communicator active; call createSession() "
                    "or create a new client instance")
            return self.__ic
        finally:
            self.__lock.release()

    def getAdapter(self):
        """
        Returns the Ice.ObjectAdapter for this instance or throws
        an exception if None.
        """
        self.__lock.acquire()
        try:
            if not self.__oa:
                raise ode.ClientError(
                    "No Ice.ObjectAdapter active; call createSession() "
                    "or create a new client instance")
            return self.__oa
        finally:
            self.__lock.release()

    def getSession(self, blocking=True):
        """
        Returns the current active session or throws an exception if none has
        been created since the last closeSession()

        If blocking is False, then self.__lock is not acquired and the value
        of self.__sf is simply returned. Clients must properly handle the
        situation where this value is None.
        """
        if not blocking:
            return self.__sf

        self.__lock.acquire(blocking)
        try:
            sf = self.__sf
            if not sf:
                raise ode.ClientError("No session available")
            return sf
        finally:
            self.__lock.release()

    def getSessionId(self):
        """
        Returns the UUID for the current session without making a remote call.
        Uses getSession() internally and will throw an exception if no session
        is active.
        """
        return self.getSession().ice_getIdentity().name

    def getCategory(self):
        """
        Returns the category which should be used for all callbacks
        passed to the server.
        """
        return self.getRouter(self.__ic).getCategoryForClient()

    def getImplicitContext(self):
        """
        Returns the Ice.ImplicitContext which defines what properties
        will be sent on every method invocation.
        """
        return self.getCommunicator().getImplicitContext()

    def getContext(self, group=None):
        """
        Returns a copy of the implicit context's context, i.e.
        dict(getImplicitContext().getContext()) for use as the
        last argument to any remote method.
        """
        ctx = self.getImplicitContext().getContext()
        ctx = dict(ctx)
        if group is not None:
            ctx["ode.group"] = str(group)
        return ctx

    def getProperties(self):
        """
        Returns the active properties for this instance
        """
        self.__lock.acquire()
        try:
            return self.__ic.getProperties()
        finally:
            self.__lock.release()

    def getProperty(self, key):
        """
        Returns the property for the given key or "" if none present
        """
        return self.getProperties().getProperty(key)

    def getPropertyMap(self, properties=None):
        """
        Returns all properties which are prefixed with "ode." or "Ice."
        """
        if properties is None:
            properties = self.getProperties()

        rv = dict()
        for prefix in ["ode", "Ice"]:
            for k, v in list(properties.getPropertiesForPrefix(prefix).items()):
                rv[k] = v
        return rv

    def getDefaultBlockSize(self):
        """
        Returns the user-configured "ode.block_size" property or
        ode.constants.DEFAULTBLOCKSIZE if none is set.
        """
        try:
            return int(self.getProperty("ode.block_size"))
        except:
            return ode.constants.DEFAULTBLOCKSIZE

    def joinSession(self, session):
        """
        Uses the given session uuid as name
        and password to rejoin a running session
        """
        return self.createSession(session, session)

    def createSession(self, username=None, password=None):
        """
        Performs the actual logic of logging in, which is done via the
        getRouter(). Disallows an extant ServiceFactoryPrx, and
        tries to re-create a null Ice.Communicator. A null or empty
        username will throw an exception, but an empty password is allowed.
        """
        import ode

        self.__lock.acquire()
        try:

            # Checking state

            if self.__sf:
                raise ode.ClientError(
                    "Session already active. "
                    "Create a new ode.client or closeSession()")

            if not self.__ic:
                if not self.__previous:
                    raise ode.ClientError(
                        "No previous data to recreate communicator.")
                self._initData(self.__previous)
                self.__previous = None

            # Check the required properties

            if not username:
                username = self.getProperty("ode.user")
            elif isinstance(username, ode.RString):
                username = username.val

            if not username or len(username) == 0:
                raise ode.ClientError("No username specified")

            if not password:
                password = self.getProperty("ode.pass")
            elif isinstance(password, ode.RString):
                password = password.val

            if not password:
                raise ode.ClientError("No password specified")

            # Acquire router and get the proxy
            prx = None
            retries = 0
            while retries < 3:
                reason = None
                if retries > 0:
                    self.__logger.warning(
                        "%s - createSession retry: %s" % (reason, retries))
                try:
                    ctx = self.getContext()
                    ctx[ode.constants.AGENT] = self.__agent
                    if self.__ip is not None:
                        ctx[ode.constants.IP] = self.__ip
                    rtr = self.getRouter(self.__ic)
                    prx = rtr.createSession(username, password, ctx)

                    # Create the adapter
                    self.__oa = self.__ic.createObjectAdapterWithRouter(
                        "ode.ClientCallback", rtr)
                    self.__oa.activate()

                    id = Ice.Identity()
                    id.name = self.__uuid
                    id.category = rtr.getCategoryForClient()

                    self.__cb = BaseClient.CallbackI(self.__ic, self.__oa, id)
                    self.__oa.add(self.__cb, id)

                    break
                except ode.WrappedCreateSessionException as wrapped:
                    if not wrapped.concurrency:
                        raise wrapped  # We only retry concurrency issues.
                    reason = "%s:%s" % (wrapped.type, wrapped.reason)
                    retries = retries + 1
                except Ice.ConnectTimeoutException as cte:
                    reason = "Ice.ConnectTimeoutException:%s" % str(cte)
                    retries = retries + 1

            if not prx:
                raise ode.ClientError("Obtained null object prox")

            # Check type
            self.__sf = ode.api.ServiceFactoryPrx.uncheckedCast(prx)
            if not self.__sf:
                raise ode.ClientError(
                    "Obtained object proxy is not a ServiceFactory")

            # Configure keep alive
            self.startKeepAlive()

            # Set the client callback on the session
            # and pass it to icestorm
            try:

                raw = self.__oa.createProxy(self.__cb.id)
                self.__sf.setCallback(
                    ode.api.ClientCallbackPrx.uncheckedCast(raw))
                # self.__sf.subscribe("/public/HeartBeat", raw)
            except:
                self.__del__()
                raise

            # Set the session uuid in the implicit context
            self.getImplicitContext().put(
                ode.constants.SESSIONUUID, self.getSessionId())

            return self.__sf
        finally:
            self.__lock.release()

    def enableKeepAlive(self, seconds):
        """
        Resets the "ode.keep_alive" property on the current
        Ice.Communicator which is used on initialization to determine
        the time-period between Resource checks. The __resources
        instance will be created as soon as an active session is
        detected.
        """

        self.__lock.acquire()
        try:
            # A communicator must be configured!
            ic = self.getCommunicator()
            # Setting this here guarantees that after closeSession()
            # the next createSession() will use the new value despite
            # what was in the configuration file
            ic.getProperties().setProperty("ode.keep_alive", str(seconds))

            # If there's not a session, there should be no
            # __resources but just in case since startKeepAlive
            # could have been called manually.
            if seconds <= 0:
                self.stopKeepAlive()
            else:
                try:
                    # If there's a session, then go ahead and
                    # start the keep alive.
                    self.getSession()
                    self.startKeepAlive()
                except ode.ClientError:
                    pass
        finally:
            self.__lock.release()

    def startKeepAlive(self):
        """
        Start a new __resources instance, stopping any that current exists
        IF ode.keep_alive is greater than 1.
        """
        self.__lock.acquire()
        try:
            ic = self.getCommunicator()
            props = ic.getProperties()
            seconds = -1
            try:
                seconds = props.getPropertyWithDefault(
                    "ode.keep_alive", "-1")
                seconds = int(seconds)
            except ValueError:
                pass

            # Any existing resource should be shutdown.
            if self.__resources is not None:
                self.stopKeepAlive()

            # If seconds is more than 0, a new one should be started.
            if seconds > 0:
                self.__resources = ode.util.Resources(seconds)

                class Entry(object):
                    def __init__(self, c):
                        self.c = c

                    def cleanup(self):
                        pass

                    def check(self):
                        sf = self.c._BaseClient__sf
                        ic = self.c._BaseClient__ic
                        if sf is not None:
                            try:
                                sf.keepAlive(None)
                            except Exception:
                                if ic is not None:
                                    ic.getLogger().warning(
                                        "Proxy keep alive failed.")
                                return False
                        return True
                self.__resources.add(Entry(self))
        finally:
            self.__lock.release()

    def stopKeepAlive(self):
        self.__lock.acquire()
        try:
            if self.__resources is not None:
                try:
                    self.__resources.cleanup()
                finally:
                    self.__resources = None

        finally:
            self.__lock.release()

    def getManagedRepository(self, description=False):
        repos = self.getSession().sharedResources().repositories()
        repoMap = list(zip(repos.proxies, repos.descriptions))
        prx = None
        for (prx, desc) in repoMap:
            if not prx:
                continue
            prx = ode.grid.ManagedRepositoryPrx.checkedCast(prx)
            if prx:
                break
        if description:
            return(prx, desc)
        else:
            return prx

    def getRouter(self, comm):
        """
        Acquires the default router, and throws an exception
        if it is not of type Glacier2.Router. Also sets the
        Ice.ImplicitContext on the router proxy.
        """
        prx = comm.getDefaultRouter()
        if not prx:
            raise ode.ClientError("No default router found.")
        router = Glacier2.RouterPrx.uncheckedCast(prx)
        if not router:
            raise ode.ClientError("Error obtaining Glacier2 router")

        # For whatever reason, we have to set the context
        # on the router context here as well
        router = router.ice_context(comm.getImplicitContext().getContext())
        return router

    def sha1(self, filename):
        """
        Calculates the local sha1 for a file.
        """
        try:
            from hashlib import sha1 as sha_new
        except ImportError:
            from sha import new as sha_new
        digest = sha_new()
        file = open(filename, 'rb')
        try:
            while True:
                block = file.read(1024)
                if not block:
                    break
                digest.update(block)
        finally:
            file.close()
        return digest.hexdigest()

    def upload(self, filename, name=None, path=None, type=None, ofile=None,
               block_size=1024):
        """
        Utility method to upload a file to the server.
        """
        if not self.__sf:
            raise ode.ClientError("No session. Use createSession first.")

        import os
        import types
        if not filename or not isinstance(filename, basestring):
            raise ode.ClientError("Non-null filename must be provided")

        if not os.path.exists(filename):
            raise ode.ClientError("File does not exist: " + filename)

        from extras.path import path as __path__
        filepath = __path__(filename)
        file = open(filename, 'rb')
        try:

            size = os.path.getsize(file.name)
            if block_size > size:
                block_size = size

            if not ofile:
                ofile = ode.model.OriginalFileI()

            ofile.hash = ode.rtypes.rstring(self.sha1(file.name))
            ofile.hasher = ode.model.ChecksumAlgorithmI()
            ofile.hasher.value = ode.rtypes.rstring("SHA1-160")

            abspath = filepath.normpath().abspath()
            if not ofile.name:
                if name:
                    ofile.name = ode.rtypes.rstring(name)
                else:
                    ofile.name = ode.rtypes.rstring(str(abspath.basename()))

            if not ofile.path:
                ofile.path = ode.rtypes.rstring(
                    str(abspath.dirname())+os.path.sep)

            if not ofile.mimetype:
                if type:
                    # ofile.mimetype = 'application/octet-stream' by default
                    ofile.mimetype = ode.rtypes.rstring(type)

            # Disabled with group permissions #1434
            # if permissions:
            #    ofile.details.permissions = permissions

            up = self.__sf.getUpdateService()
            ofile = up.saveAndReturnObject(ofile)

            prx = self.__sf.createRawFileStore()
            try:
                prx.setFileId(ofile.id.val)
                prx.truncate(size)  # ticket:2337
                self.write_stream(file, prx, block_size)
            finally:
                prx.close()
        finally:
            file.close()

        return ofile

    def write_stream(self, file, prx, block_size=1024*1024):
        offset = 0
        while True:
            block = file.read(block_size)
            if not block:
                break
            prx.write(block, offset, len(block))
            offset += len(block)

    def download(self, ofile, filename=None, block_size=1024*1024,
                 filehandle=None):
        if not self.__sf:
            raise ode.ClientError("No session. Use createSession first.")

        # Search for objects in all groups. See #12146
        ctx = self.getContext(group=-1)
        prx = self.__sf.createRawFileStore()

        try:
            if not ofile or not ofile.id:
                raise ode.ClientError("No file to download")
            ofile = self.__sf.getQueryService().get(
                "OriginalFile", ofile.id.val, ctx)

            prx.setFileId(ofile.id.val, ctx)
            size = None
            if prx.size() is None:
                name = ode.rtypes.unwrap(ofile.name)
                mimetype = ode.rtypes.unwrap(ofile.mimetype)
                raise ode.ClientError(
                    ("invalid size for OriginalFile '%s' "
                     "(mimetype:%s)") % (name, mimetype))
            else:
                size = prx.size()

            if block_size > size:
                block_size = size


            if filehandle is None:
                if filename is None:
                    raise ode.ClientError(
                        "no filename or filehandle specified")
                filehandle = open(filename, 'wb')
            else:
                if filename:
                    raise ode.ClientError(
                        "filename and filehandle specified.")

            offset = 0
            try:
                while (offset+block_size) < size:
                    data = prx.read(offset, block_size)
                    try:
                        filehandle.write(data)
                    except TypeError:
                        # for Python 3.5
                        filehandle.write(bytes_to_native_str(data))
                    offset += block_size
                data = prx.read(offset, size - offset)
                try:
                    filehandle.write(data)
                except TypeError:
                    # for Python 3.5
                    filehandle.write(bytes_to_native_str(data))
            finally:
                if filename:
                    filehandle.close()
        finally:
            prx.close()

    def submit(self, req, loops=10, ms=500,
               failonerror=True, ctx=None, failontimeout=True):
        handle = self.getSession().submit(req, ctx)
        return self.waitOnCmd(
            handle, loops=loops, ms=ms,
            failonerror=failonerror,
            failontimeout=failontimeout,
            closehandle=True)

    def waitOnCmd(self, handle, loops=10, ms=500,
                  failonerror=True,
                  failontimeout=False,
                  closehandle=False):

        from ode import LockTimeout

        try:
            callback = ode.callbacks.CmdCallbackI(self, handle)
        except:
            # Since the callback won't escape this method,
            # close the handle if requested.
            if closehandle and handle:
                handle.close()
            raise

        try:
            callback.loop(loops, ms)  # Throw LockTimeout
        except LockTimeout:
            if failontimeout:
                callback.close(closehandle)
                raise
            else:
                return callback

        rsp = callback.getResponse()
        if isinstance(rsp, ode.cmd.ERR):
            if failonerror:
                callback.close(closehandle)
                raise ode.CmdError(rsp)
        return callback

    def getStatefulServices(self):
        """
        Returns all active StatefulServiceInterface proxies. This can
        be used to call close before calling setSecurityContext.
        """
        rv = []
        sf = self.sf
        services = sf.activeServices()
        for srv in services:
            try:
                prx = sf.getByName(srv)
                prx = ode.api.StatefulServiceInterfacePrx.checkedCast(prx)
                if prx is not None:
                    rv.append(prx)
            except:
                self.__logger.warn(
                    "Error looking up proxy: %s" % srv, exc_info=1)
        return rv

    def closeSession(self):
        """
        Closes the Router connection created by createSession(). Due to a bug
        in Ice, only one connection is allowed per communicator, so we also
        destroy the communicator.
        """

        self.__lock.acquire()
        try:

            try:
                self.stopKeepAlive()
            except Exception as e:
                self.__logger.warning(
                    "While cleaning up resources: " + str(e))

            self.__sf = None

            oldOa = self.__oa
            self.__oa = None

            oldIc = self.__ic
            self.__ic = None

            # Only possible if improperly configured.
            if not oldIc:
                return

            if oldOa:
                try:
                    oldOa.deactivate()
                except Exception as e:
                    self.__logger.warning(
                        "While deactivating adapter: " + str(e.message))

            self.__previous = Ice.InitializationData()
            self.__previous.properties = oldIc.getProperties().clone()

            try:
                try:
                    self.getRouter(oldIc).destroySession()
                except Glacier2.SessionNotExistException:
                    # ok. We don't want it to exist
                    pass
                except Ice.ConnectionLostException:
                    # ok. Exception will always be thrown
                    pass
                except Ice.ConnectionRefusedException:
                    # ok. Server probably went down
                    pass
                except Ice.ConnectTimeoutException:
                    # ok. Server probably went down
                    pass
                # Possible other items to handle/ignore:
                # * Ice.DNSException
            finally:
                oldIc.destroy()

        finally:
            self.__lock.release()

    def killSession(self):
        """
        Calls ISession.closeSession(ode.model.Session) until
        the returned reference count is greater than zero. The
        number of invocations is returned. If ISession.closeSession()
        cannot be called, -1 is returned.
        """

        try:
            self.sf.getSessionService()
        except:
            self.__logger.warning(
                "Cannot get session service for killSession. "
                "Using closeSession")
            self.closeSession()
            return -1

        count = 0
        try:
            count = self.destroySession(self.getSessionId())
        except:
            self.__logger.warning(
                "Unknown exception while closing all references",
                exc_info=True)

        # Now the server-side session is dead, call closeSession()
        self.closeSession()
        return count

    def destroySession(self, session_uuid):
        """
        Takes the UUID for a session and iterates over calls to
        ISession.closeSession until the reference count hits 0.
        Returns the number of calls to closeSession executed
        before hitting 0. Raises any non-RemovedSessionExceptions.
        """
        svc = self.sf.getSessionService()
        s = ode.model.SessionI()
        s.uuid = ode.rtypes.rstring(session_uuid)
        count = 0
        try:
            r = 1
            while r > 0:
                count += 1
                r = svc.closeSession(s)
        except ode.RemovedSessionException:
            pass

        return count

    # Environment Methods
    # ===========================================================

    def _env(self, _unwrap, method, *args):
        """ Helper method to access session environment"""
        session = self.getSession()
        if not session:
            raise ode.ClientError("No session active")
        u = self.getSessionId()
        s = session.getSessionService()
        m = getattr(s, method)
        rv = m(*(u,)+args)
        if callable(_unwrap):
            rv = _unwrap(rv)  # Passed in function
        elif _unwrap:
            rv = ode.rtypes.unwrap(rv)  # Default method
        return rv

    def getInput(self, key, unwrap=False):
        """
        Retrieves an item from the "input" shared (session) memory.
        """
        return self._env(unwrap, "getInput", key)

    def getOutput(self, key, unwrap=False):
        """
        Retrieves an item from the "output" shared (session) memory.
        """
        return self._env(unwrap, "getOutput", key)

    def setInput(self, key, value):
        """
        Sets an item in the "input" shared (session) memory under the given
        name.
        """
        self._env(False, "setInput", key, value)

    def setOutput(self, key, value):
        """
        Sets an item in the "output" shared (session) memory under the given
        name.
        """
        self._env(False, "setOutput", key, value)

    def getInputKeys(self):
        """
        Returns a list of keys for all items in the "input" shared (session)
        memory
        """
        return self._env(False, "getInputKeys")

    def getOutputKeys(self):
        """
        Returns a list of keys for all items in the "output" shared (session)
        memory
        """
        return self._env(False, "getOutputKeys")

    def getInputs(self, unwrap=False):
        """
        Returns all items in the "input" shared (session) memory
        """
        return self._env(unwrap, "getInputs")

    def getOutputs(self, unwrap=False):
        """
        Returns all items in the "output" shared (session) memory
        """
        return self._env(unwrap, "getOutputKeys")

    #
    # Misc.
    #

    def _optSetProp(self, id, key, default=""):
        val = id.properties.getProperty(key)
        if not val:
            val = default
        id.properties.setProperty(key, val)

    def parseAndSetInt(self, data, key, newValue):
        currentValue = data.properties.getProperty(key)
        if not currentValue or len(currentValue) == 0:
            newStr = str(newValue)
            data.properties.setProperty(key, newStr)
            currentValue = newStr
        return currentValue

    def __getattr__(self, name):
        """
        Compatibility layer, which allows calls to getCommunicator() and
        getSession() to be called via self.ic and self.sf
        """
        if name == "ic":
            return self.getCommunicator()
        elif name == "sf":
            return self.getSession()
        elif name == "adapter":
            return self.getAdapter()
        else:
            raise AttributeError("Unknown property: " + name)

    #
    # Callback
    #
    def onHeartbeat(self, myCallable):
        self.__cb.onHeartbeat = myCallable

    def onSessionClosed(self, myCallable):
        self.__cb.onSessionClosed = myCallable

    def onShutdownIn(self, myCallable):
        self.__cb.onShutdownIn = myCallable

    class CallbackI(ode.api.ClientCallback):
        """
        Implemention of ClientCallback which will be added to
        any Session which this instance creates. Note: this client
        should avoid all interaction with the {@link client#lock} since it
        can lead to deadlocks during shutdown. See: ticket:1210
        """

        #
        # Default callbacks
        #
        def _noop(self):
            pass

        def _closeSession(self):
            try:
                self.oa.deactivate()
            except Exception as e:
                sys.err.write("On session closed: " + str(e))

        def __init__(self, ic, oa, id):
            self.ic = ic
            self.oa = oa
            self.id = id
            self.onHeartbeat = self._noop
            self.onShutdownIn = self._noop
            self.onSessionClosed = self._noop

        def execute(self, myCallable, action):
            try:
                myCallable()
                # self.ic.getLogger().trace("ClientCallback", action + " run")
            except:
                try:
                    self.ic.getLogger().error("Error performing %s" % action)
                except:
                    print("Error performing %s" % action)

        def requestHeartbeat(self, current=None):
            self.execute(self.onHeartbeat, "heartbeat")

        def shutdownIn(self, milliseconds, current=None):
            self.execute(self.onShutdownIn, "shutdown")

        def sessionClosed(self, current=None):
            self.execute(self.onSessionClosed, "sessionClosed")