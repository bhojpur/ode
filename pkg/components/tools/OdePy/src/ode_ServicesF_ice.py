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
import ode_ServerErrors_ice
import ode_System_ice

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'ServiceInterface' not in _M_ode.api.__dict__:
    _M_ode.api.ServiceInterface = Ice.createTempClass()
    class ServiceInterface(Ice.Object):
        """
        Service marker similar to ode.api.ServiceInterface. Any object which
        IS-A ServiceInterface but IS-NOT-A StatefulServiceInterface (below)
        is by definition a ""stateless service""
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.ServiceInterface:
                raise RuntimeError('ode.api.ServiceInterface is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::ServiceInterface'

        def ice_staticId():
            return '::ode::api::ServiceInterface'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_ServiceInterface)

        __repr__ = __str__

    _M_ode.api.ServiceInterfacePrx = Ice.createTempClass()
    class ServiceInterfacePrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ServiceInterfacePrx.ice_checkedCast(proxy, '::ode::api::ServiceInterface', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ServiceInterfacePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::ServiceInterface'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ServiceInterfacePrx = IcePy.defineProxy('::ode::api::ServiceInterface', ServiceInterfacePrx)

    _M_ode.api._t_ServiceInterface = IcePy.defineClass('::ode::api::ServiceInterface', ServiceInterface, -1, (), True, False, None, (), ())
    ServiceInterface._ice_type = _M_ode.api._t_ServiceInterface

    _M_ode.api.ServiceInterface = ServiceInterface
    del ServiceInterface

    _M_ode.api.ServiceInterfacePrx = ServiceInterfacePrx
    del ServiceInterfacePrx

if '_t_ServiceList' not in _M_ode.api.__dict__:
    _M_ode.api._t_ServiceList = IcePy.defineSequence('::ode::api::ServiceList', (), _M_ode.api._t_ServiceInterfacePrx)

if 'StatefulServiceInterface' not in _M_ode.api.__dict__:
    _M_ode.api.StatefulServiceInterface = Ice.createTempClass()
    class StatefulServiceInterface(_M_ode.api.ServiceInterface):
        """
        Service marker for stateful services which permits the closing
        of a particular service before the destruction of the session.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.StatefulServiceInterface:
                raise RuntimeError('ode.api.StatefulServiceInterface is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ServiceInterface', '::ode::api::StatefulServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::StatefulServiceInterface'

        def ice_staticId():
            return '::ode::api::StatefulServiceInterface'
        ice_staticId = staticmethod(ice_staticId)

        def passivate_async(self, _cb, current=None):
            """
            Causes the ODE server to store the service implementation to disk
            to free memory. This is typically done automatically by the server
            when a pre-defined memory limit is reached, but can be used by the
            client if it clear that a stateful service will not be used for some
            time.
            Activation will happen automatically whether passivation was done
            manually or automatically.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def activate_async(self, _cb, current=None):
            """
            Load a service implementation from disk if it was previously
            passivated. It is unnecessary to call this method since activation
            happens automatically, but calling this may prevent a short
            lapse when the service is first accessed after passivation.
            It is safe to call this method at any time, even when the service
            is not passivated.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def close_async(self, _cb, current=None):
            """
            Frees all resources -- passivated or active -- for the given
            stateful service and removes its name from the object adapter.
            Any further method calls will fail with a Ice::NoSuchObjectException.
            Note: with JavaEE, the close method was called publicly,
            and internally this called destroy(). As of the OdeServer
            migration, this functionality has been combined.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getCurrentEventContext_async(self, _cb, current=None):
            """
            To free clients from tracking the mapping from session to stateful
            service, each stateful service can returns its own context information.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_StatefulServiceInterface)

        __repr__ = __str__

    _M_ode.api.StatefulServiceInterfacePrx = Ice.createTempClass()
    class StatefulServiceInterfacePrx(_M_ode.api.ServiceInterfacePrx):

        """
        Causes the ODE server to store the service implementation to disk
        to free memory. This is typically done automatically by the server
        when a pre-defined memory limit is reached, but can be used by the
        client if it clear that a stateful service will not be used for some
        time.
        Activation will happen automatically whether passivation was done
        manually or automatically.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def passivate(self, _ctx=None):
            return _M_ode.api.StatefulServiceInterface._op_passivate.invoke(self, ((), _ctx))

        """
        Causes the ODE server to store the service implementation to disk
        to free memory. This is typically done automatically by the server
        when a pre-defined memory limit is reached, but can be used by the
        client if it clear that a stateful service will not be used for some
        time.
        Activation will happen automatically whether passivation was done
        manually or automatically.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_passivate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.StatefulServiceInterface._op_passivate.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Causes the ODE server to store the service implementation to disk
        to free memory. This is typically done automatically by the server
        when a pre-defined memory limit is reached, but can be used by the
        client if it clear that a stateful service will not be used for some
        time.
        Activation will happen automatically whether passivation was done
        manually or automatically.
        Arguments:
        """
        def end_passivate(self, _r):
            return _M_ode.api.StatefulServiceInterface._op_passivate.end(self, _r)

        """
        Load a service implementation from disk if it was previously
        passivated. It is unnecessary to call this method since activation
        happens automatically, but calling this may prevent a short
        lapse when the service is first accessed after passivation.
        It is safe to call this method at any time, even when the service
        is not passivated.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def activate(self, _ctx=None):
            return _M_ode.api.StatefulServiceInterface._op_activate.invoke(self, ((), _ctx))

        """
        Load a service implementation from disk if it was previously
        passivated. It is unnecessary to call this method since activation
        happens automatically, but calling this may prevent a short
        lapse when the service is first accessed after passivation.
        It is safe to call this method at any time, even when the service
        is not passivated.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_activate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.StatefulServiceInterface._op_activate.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Load a service implementation from disk if it was previously
        passivated. It is unnecessary to call this method since activation
        happens automatically, but calling this may prevent a short
        lapse when the service is first accessed after passivation.
        It is safe to call this method at any time, even when the service
        is not passivated.
        Arguments:
        """
        def end_activate(self, _r):
            return _M_ode.api.StatefulServiceInterface._op_activate.end(self, _r)

        """
        Frees all resources -- passivated or active -- for the given
        stateful service and removes its name from the object adapter.
        Any further method calls will fail with a Ice::NoSuchObjectException.
        Note: with JavaEE, the close method was called publicly,
        and internally this called destroy(). As of the OdeServer
        migration, this functionality has been combined.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def close(self, _ctx=None):
            return _M_ode.api.StatefulServiceInterface._op_close.invoke(self, ((), _ctx))

        """
        Frees all resources -- passivated or active -- for the given
        stateful service and removes its name from the object adapter.
        Any further method calls will fail with a Ice::NoSuchObjectException.
        Note: with JavaEE, the close method was called publicly,
        and internally this called destroy(). As of the OdeServer
        migration, this functionality has been combined.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_close(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.StatefulServiceInterface._op_close.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Frees all resources -- passivated or active -- for the given
        stateful service and removes its name from the object adapter.
        Any further method calls will fail with a Ice::NoSuchObjectException.
        Note: with JavaEE, the close method was called publicly,
        and internally this called destroy(). As of the OdeServer
        migration, this functionality has been combined.
        Arguments:
        """
        def end_close(self, _r):
            return _M_ode.api.StatefulServiceInterface._op_close.end(self, _r)

        """
        To free clients from tracking the mapping from session to stateful
        service, each stateful service can returns its own context information.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getCurrentEventContext(self, _ctx=None):
            return _M_ode.api.StatefulServiceInterface._op_getCurrentEventContext.invoke(self, ((), _ctx))

        """
        To free clients from tracking the mapping from session to stateful
        service, each stateful service can returns its own context information.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getCurrentEventContext(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.StatefulServiceInterface._op_getCurrentEventContext.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        To free clients from tracking the mapping from session to stateful
        service, each stateful service can returns its own context information.
        Arguments:
        """
        def end_getCurrentEventContext(self, _r):
            return _M_ode.api.StatefulServiceInterface._op_getCurrentEventContext.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.StatefulServiceInterfacePrx.ice_checkedCast(proxy, '::ode::api::StatefulServiceInterface', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.StatefulServiceInterfacePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::StatefulServiceInterface'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_StatefulServiceInterfacePrx = IcePy.defineProxy('::ode::api::StatefulServiceInterface', StatefulServiceInterfacePrx)

    _M_ode.api._t_StatefulServiceInterface = IcePy.defineClass('::ode::api::StatefulServiceInterface', StatefulServiceInterface, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    StatefulServiceInterface._ice_type = _M_ode.api._t_StatefulServiceInterface

    StatefulServiceInterface._op_passivate = IcePy.Operation('passivate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    StatefulServiceInterface._op_activate = IcePy.Operation('activate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    StatefulServiceInterface._op_close = IcePy.Operation('close', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    StatefulServiceInterface._op_getCurrentEventContext = IcePy.Operation('getCurrentEventContext', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.sys._t_EventContext, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.StatefulServiceInterface = StatefulServiceInterface
    del StatefulServiceInterface

    _M_ode.api.StatefulServiceInterfacePrx = StatefulServiceInterfacePrx
    del StatefulServiceInterfacePrx

if 'IAdmin' not in _M_ode.api.__dict__:
    _M_ode.api._t_IAdmin = IcePy.declareClass('::ode::api::IAdmin')
    _M_ode.api._t_IAdminPrx = IcePy.declareProxy('::ode::api::IAdmin')

if 'IConfig' not in _M_ode.api.__dict__:
    _M_ode.api._t_IConfig = IcePy.declareClass('::ode::api::IConfig')
    _M_ode.api._t_IConfigPrx = IcePy.declareProxy('::ode::api::IConfig')

if 'IContainer' not in _M_ode.api.__dict__:
    _M_ode.api._t_IContainer = IcePy.declareClass('::ode::api::IContainer')
    _M_ode.api._t_IContainerPrx = IcePy.declareProxy('::ode::api::IContainer')

if 'ILdap' not in _M_ode.api.__dict__:
    _M_ode.api._t_ILdap = IcePy.declareClass('::ode::api::ILdap')
    _M_ode.api._t_ILdapPrx = IcePy.declareProxy('::ode::api::ILdap')

if 'IMetadata' not in _M_ode.api.__dict__:
    _M_ode.api._t_IMetadata = IcePy.declareClass('::ode::api::IMetadata')
    _M_ode.api._t_IMetadataPrx = IcePy.declareProxy('::ode::api::IMetadata')

if 'IPixels' not in _M_ode.api.__dict__:
    _M_ode.api._t_IPixels = IcePy.declareClass('::ode::api::IPixels')
    _M_ode.api._t_IPixelsPrx = IcePy.declareProxy('::ode::api::IPixels')

if 'IProjection' not in _M_ode.api.__dict__:
    _M_ode.api._t_IProjection = IcePy.declareClass('::ode::api::IProjection')
    _M_ode.api._t_IProjectionPrx = IcePy.declareProxy('::ode::api::IProjection')

if 'IQuery' not in _M_ode.api.__dict__:
    _M_ode.api._t_IQuery = IcePy.declareClass('::ode::api::IQuery')
    _M_ode.api._t_IQueryPrx = IcePy.declareProxy('::ode::api::IQuery')

if 'IRoi' not in _M_ode.api.__dict__:
    _M_ode.api._t_IRoi = IcePy.declareClass('::ode::api::IRoi')
    _M_ode.api._t_IRoiPrx = IcePy.declareProxy('::ode::api::IRoi')

if 'IScript' not in _M_ode.api.__dict__:
    _M_ode.api._t_IScript = IcePy.declareClass('::ode::api::IScript')
    _M_ode.api._t_IScriptPrx = IcePy.declareProxy('::ode::api::IScript')

if 'ISession' not in _M_ode.api.__dict__:
    _M_ode.api._t_ISession = IcePy.declareClass('::ode::api::ISession')
    _M_ode.api._t_ISessionPrx = IcePy.declareProxy('::ode::api::ISession')

if 'IShare' not in _M_ode.api.__dict__:
    _M_ode.api._t_IShare = IcePy.declareClass('::ode::api::IShare')
    _M_ode.api._t_ISharePrx = IcePy.declareProxy('::ode::api::IShare')

if 'ITypes' not in _M_ode.api.__dict__:
    _M_ode.api._t_ITypes = IcePy.declareClass('::ode::api::ITypes')
    _M_ode.api._t_ITypesPrx = IcePy.declareProxy('::ode::api::ITypes')

if 'IUpdate' not in _M_ode.api.__dict__:
    _M_ode.api._t_IUpdate = IcePy.declareClass('::ode::api::IUpdate')
    _M_ode.api._t_IUpdatePrx = IcePy.declareProxy('::ode::api::IUpdate')

if 'IRenderingSettings' not in _M_ode.api.__dict__:
    _M_ode.api._t_IRenderingSettings = IcePy.declareClass('::ode::api::IRenderingSettings')
    _M_ode.api._t_IRenderingSettingsPrx = IcePy.declareProxy('::ode::api::IRenderingSettings')

if 'IRepositoryInfo' not in _M_ode.api.__dict__:
    _M_ode.api._t_IRepositoryInfo = IcePy.declareClass('::ode::api::IRepositoryInfo')
    _M_ode.api._t_IRepositoryInfoPrx = IcePy.declareProxy('::ode::api::IRepositoryInfo')

if 'ITimeline' not in _M_ode.api.__dict__:
    _M_ode.api._t_ITimeline = IcePy.declareClass('::ode::api::ITimeline')
    _M_ode.api._t_ITimelinePrx = IcePy.declareProxy('::ode::api::ITimeline')

if 'Exporter' not in _M_ode.api.__dict__:
    _M_ode.api._t_Exporter = IcePy.declareClass('::ode::api::Exporter')
    _M_ode.api._t_ExporterPrx = IcePy.declareProxy('::ode::api::Exporter')

if 'JobHandle' not in _M_ode.api.__dict__:
    _M_ode.api._t_JobHandle = IcePy.declareClass('::ode::api::JobHandle')
    _M_ode.api._t_JobHandlePrx = IcePy.declareProxy('::ode::api::JobHandle')

if 'RawFileStore' not in _M_ode.api.__dict__:
    _M_ode.api._t_RawFileStore = IcePy.declareClass('::ode::api::RawFileStore')
    _M_ode.api._t_RawFileStorePrx = IcePy.declareProxy('::ode::api::RawFileStore')

if 'RawPixelsStore' not in _M_ode.api.__dict__:
    _M_ode.api._t_RawPixelsStore = IcePy.declareClass('::ode::api::RawPixelsStore')
    _M_ode.api._t_RawPixelsStorePrx = IcePy.declareProxy('::ode::api::RawPixelsStore')

if 'RenderingEngine' not in _M_ode.api.__dict__:
    _M_ode.api._t_RenderingEngine = IcePy.declareClass('::ode::api::RenderingEngine')
    _M_ode.api._t_RenderingEnginePrx = IcePy.declareProxy('::ode::api::RenderingEngine')

if 'Search' not in _M_ode.api.__dict__:
    _M_ode.api._t_Search = IcePy.declareClass('::ode::api::Search')
    _M_ode.api._t_SearchPrx = IcePy.declareProxy('::ode::api::Search')

if 'ThumbnailStore' not in _M_ode.api.__dict__:
    _M_ode.api._t_ThumbnailStore = IcePy.declareClass('::ode::api::ThumbnailStore')
    _M_ode.api._t_ThumbnailStorePrx = IcePy.declareProxy('::ode::api::ThumbnailStore')

# End of module ode.api

__name__ = 'ode'

# Start of module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')
__name__ = 'ode.grid'

if 'ManagedRepository' not in _M_ode.grid.__dict__:
    _M_ode.grid._t_ManagedRepository = IcePy.declareClass('::ode::grid::ManagedRepository')
    _M_ode.grid._t_ManagedRepositoryPrx = IcePy.declareProxy('::ode::grid::ManagedRepository')

if 'ScriptProcessor' not in _M_ode.grid.__dict__:
    _M_ode.grid._t_ScriptProcessor = IcePy.declareClass('::ode::grid::ScriptProcessor')
    _M_ode.grid._t_ScriptProcessorPrx = IcePy.declareProxy('::ode::grid::ScriptProcessor')

if 'SharedResources' not in _M_ode.grid.__dict__:
    _M_ode.grid._t_SharedResources = IcePy.declareClass('::ode::grid::SharedResources')
    _M_ode.grid._t_SharedResourcesPrx = IcePy.declareProxy('::ode::grid::SharedResources')

if 'Table' not in _M_ode.grid.__dict__:
    _M_ode.grid._t_Table = IcePy.declareClass('::ode::grid::Table')
    _M_ode.grid._t_TablePrx = IcePy.declareProxy('::ode::grid::Table')

# End of module ode.grid

__name__ = 'ode'

# End of module ode
