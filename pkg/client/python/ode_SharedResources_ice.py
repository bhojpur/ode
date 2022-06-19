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
import ode_Repositories_ice
import ode_Scripts_ice
import ode_Tables_ice

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

# Included module ode.cmd
_M_ode.cmd = Ice.openModule('ode.cmd')

# Start of module ode
__name__ = 'ode'

# Start of module ode.grid
__name__ = 'ode.grid'

if 'SharedResources' not in _M_ode.grid.__dict__:
    _M_ode.grid.SharedResources = Ice.createTempClass()
    class SharedResources(Ice.Object):
        """
        Resource manager provided by each Server session for acquiring
        shared resources in the OdeGrid. Unlike the other services
        provided by ServiceFactory instances, it is not guaranteed
        that a service instance returned from this interface will be
        returned if that resource happens to be busy. In that case,
        a null will be returned.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.SharedResources:
                raise RuntimeError('ode.grid.SharedResources is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::SharedResources')

        def ice_id(self, current=None):
            return '::ode::grid::SharedResources'

        def ice_staticId():
            return '::ode::grid::SharedResources'
        ice_staticId = staticmethod(ice_staticId)

        def acquireProcessor(self, job, seconds, current=None):
            """
            Waits up to seconds to acquire a slot in a processor
            which can handle the given job.
            Arguments:
            job -- 
            seconds -- 
            current -- The Current object for the invocation.
            """
            pass

        def addProcessor(self, proc, current=None):
            """
            Registers a ode.grid.Processor for Storm notifications
            so that other sessions can query whether or not a given
            processor would accept a given task.
            Arguments:
            proc -- 
            current -- The Current object for the invocation.
            """
            pass

        def removeProcessor(self, proc, current=None):
            """
            Unregisters a ode.grid.Processor from Storm
            notifications. If the processor was not already registered via
            {@code addProcessor} this is a no-op.
            Arguments:
            proc -- 
            current -- The Current object for the invocation.
            """
            pass

        def repositories(self, current=None):
            """
            Returns a map between Repository descriptions (ode::model::OriginalFile
            instances) and RepositoryPrx instances (possibly null).
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def getScriptRepository(self, current=None):
            """
            Returns the single (possibly mirrored) script repository which makes
            all official scripts available.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def areTablesEnabled(self, current=None):
            """
            Returns true if a {@code Tables} service is active in the grid.
            If this value is false, then all calls to {@code #ewTable}
            or {@code openTable} will either fail or return null (possibly
            blocking while waiting for a service to startup)
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def newTable(self, repoId, path, current=None):
            """
            Creates a new Format(""ODE.tables"") file at the given path
            on the given repository. The returned Table proxy follows
            the same semantics as the openTable method.
            Arguments:
            repoId -- 
            path -- 
            current -- The Current object for the invocation.
            """
            pass

        def openTable(self, file, current=None):
            """
            Returns a Table instance or null. Table instances are not
            exclusively owned by the client and may throw an OptimisticLockException
            if background modifications take place.
            The file instance must be managed (i.e. have a non-null id) and
            be of the format ""ODE.tables"". Use newTable() to create
            a new instance.
            Arguments:
            file -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_SharedResources)

        __repr__ = __str__

    _M_ode.grid.SharedResourcesPrx = Ice.createTempClass()
    class SharedResourcesPrx(Ice.ObjectPrx):

        """
        Waits up to seconds to acquire a slot in a processor
        which can handle the given job.
        Arguments:
        job -- 
        seconds -- 
        _ctx -- The request context for the invocation.
        """
        def acquireProcessor(self, job, seconds, _ctx=None):
            return _M_ode.grid.SharedResources._op_acquireProcessor.invoke(self, ((job, seconds), _ctx))

        """
        Waits up to seconds to acquire a slot in a processor
        which can handle the given job.
        Arguments:
        job -- 
        seconds -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_acquireProcessor(self, job, seconds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.SharedResources._op_acquireProcessor.begin(self, ((job, seconds), _response, _ex, _sent, _ctx))

        """
        Waits up to seconds to acquire a slot in a processor
        which can handle the given job.
        Arguments:
        job -- 
        seconds -- 
        """
        def end_acquireProcessor(self, _r):
            return _M_ode.grid.SharedResources._op_acquireProcessor.end(self, _r)

        """
        Registers a ode.grid.Processor for Storm notifications
        so that other sessions can query whether or not a given
        processor would accept a given task.
        Arguments:
        proc -- 
        _ctx -- The request context for the invocation.
        """
        def addProcessor(self, proc, _ctx=None):
            return _M_ode.grid.SharedResources._op_addProcessor.invoke(self, ((proc, ), _ctx))

        """
        Registers a ode.grid.Processor for Storm notifications
        so that other sessions can query whether or not a given
        processor would accept a given task.
        Arguments:
        proc -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addProcessor(self, proc, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.SharedResources._op_addProcessor.begin(self, ((proc, ), _response, _ex, _sent, _ctx))

        """
        Registers a ode.grid.Processor for Storm notifications
        so that other sessions can query whether or not a given
        processor would accept a given task.
        Arguments:
        proc -- 
        """
        def end_addProcessor(self, _r):
            return _M_ode.grid.SharedResources._op_addProcessor.end(self, _r)

        """
        Unregisters a ode.grid.Processor from Storm
        notifications. If the processor was not already registered via
        {@code addProcessor} this is a no-op.
        Arguments:
        proc -- 
        _ctx -- The request context for the invocation.
        """
        def removeProcessor(self, proc, _ctx=None):
            return _M_ode.grid.SharedResources._op_removeProcessor.invoke(self, ((proc, ), _ctx))

        """
        Unregisters a ode.grid.Processor from Storm
        notifications. If the processor was not already registered via
        {@code addProcessor} this is a no-op.
        Arguments:
        proc -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeProcessor(self, proc, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.SharedResources._op_removeProcessor.begin(self, ((proc, ), _response, _ex, _sent, _ctx))

        """
        Unregisters a ode.grid.Processor from Storm
        notifications. If the processor was not already registered via
        {@code addProcessor} this is a no-op.
        Arguments:
        proc -- 
        """
        def end_removeProcessor(self, _r):
            return _M_ode.grid.SharedResources._op_removeProcessor.end(self, _r)

        """
        Returns a map between Repository descriptions (ode::model::OriginalFile
        instances) and RepositoryPrx instances (possibly null).
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def repositories(self, _ctx=None):
            return _M_ode.grid.SharedResources._op_repositories.invoke(self, ((), _ctx))

        """
        Returns a map between Repository descriptions (ode::model::OriginalFile
        instances) and RepositoryPrx instances (possibly null).
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_repositories(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.SharedResources._op_repositories.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns a map between Repository descriptions (ode::model::OriginalFile
        instances) and RepositoryPrx instances (possibly null).
        Arguments:
        """
        def end_repositories(self, _r):
            return _M_ode.grid.SharedResources._op_repositories.end(self, _r)

        """
        Returns the single (possibly mirrored) script repository which makes
        all official scripts available.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getScriptRepository(self, _ctx=None):
            return _M_ode.grid.SharedResources._op_getScriptRepository.invoke(self, ((), _ctx))

        """
        Returns the single (possibly mirrored) script repository which makes
        all official scripts available.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getScriptRepository(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.SharedResources._op_getScriptRepository.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the single (possibly mirrored) script repository which makes
        all official scripts available.
        Arguments:
        """
        def end_getScriptRepository(self, _r):
            return _M_ode.grid.SharedResources._op_getScriptRepository.end(self, _r)

        """
        Returns true if a {@code Tables} service is active in the grid.
        If this value is false, then all calls to {@code #ewTable}
        or {@code openTable} will either fail or return null (possibly
        blocking while waiting for a service to startup)
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def areTablesEnabled(self, _ctx=None):
            return _M_ode.grid.SharedResources._op_areTablesEnabled.invoke(self, ((), _ctx))

        """
        Returns true if a {@code Tables} service is active in the grid.
        If this value is false, then all calls to {@code #ewTable}
        or {@code openTable} will either fail or return null (possibly
        blocking while waiting for a service to startup)
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_areTablesEnabled(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.SharedResources._op_areTablesEnabled.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns true if a {@code Tables} service is active in the grid.
        If this value is false, then all calls to {@code #ewTable}
        or {@code openTable} will either fail or return null (possibly
        blocking while waiting for a service to startup)
        Arguments:
        """
        def end_areTablesEnabled(self, _r):
            return _M_ode.grid.SharedResources._op_areTablesEnabled.end(self, _r)

        """
        Creates a new Format(""ODE.tables"") file at the given path
        on the given repository. The returned Table proxy follows
        the same semantics as the openTable method.
        Arguments:
        repoId -- 
        path -- 
        _ctx -- The request context for the invocation.
        """
        def newTable(self, repoId, path, _ctx=None):
            return _M_ode.grid.SharedResources._op_newTable.invoke(self, ((repoId, path), _ctx))

        """
        Creates a new Format(""ODE.tables"") file at the given path
        on the given repository. The returned Table proxy follows
        the same semantics as the openTable method.
        Arguments:
        repoId -- 
        path -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_newTable(self, repoId, path, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.SharedResources._op_newTable.begin(self, ((repoId, path), _response, _ex, _sent, _ctx))

        """
        Creates a new Format(""ODE.tables"") file at the given path
        on the given repository. The returned Table proxy follows
        the same semantics as the openTable method.
        Arguments:
        repoId -- 
        path -- 
        """
        def end_newTable(self, _r):
            return _M_ode.grid.SharedResources._op_newTable.end(self, _r)

        """
        Returns a Table instance or null. Table instances are not
        exclusively owned by the client and may throw an OptimisticLockException
        if background modifications take place.
        The file instance must be managed (i.e. have a non-null id) and
        be of the format ""ODE.tables"". Use newTable() to create
        a new instance.
        Arguments:
        file -- 
        _ctx -- The request context for the invocation.
        """
        def openTable(self, file, _ctx=None):
            return _M_ode.grid.SharedResources._op_openTable.invoke(self, ((file, ), _ctx))

        """
        Returns a Table instance or null. Table instances are not
        exclusively owned by the client and may throw an OptimisticLockException
        if background modifications take place.
        The file instance must be managed (i.e. have a non-null id) and
        be of the format ""ODE.tables"". Use newTable() to create
        a new instance.
        Arguments:
        file -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_openTable(self, file, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.SharedResources._op_openTable.begin(self, ((file, ), _response, _ex, _sent, _ctx))

        """
        Returns a Table instance or null. Table instances are not
        exclusively owned by the client and may throw an OptimisticLockException
        if background modifications take place.
        The file instance must be managed (i.e. have a non-null id) and
        be of the format ""ODE.tables"". Use newTable() to create
        a new instance.
        Arguments:
        file -- 
        """
        def end_openTable(self, _r):
            return _M_ode.grid.SharedResources._op_openTable.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.SharedResourcesPrx.ice_checkedCast(proxy, '::ode::grid::SharedResources', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.SharedResourcesPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::SharedResources'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_SharedResourcesPrx = IcePy.defineProxy('::ode::grid::SharedResources', SharedResourcesPrx)

    _M_ode.grid._t_SharedResources = IcePy.defineClass('::ode::grid::SharedResources', SharedResources, -1, (), True, False, None, (), ())
    SharedResources._ice_type = _M_ode.grid._t_SharedResources

    SharedResources._op_acquireProcessor = IcePy.Operation('acquireProcessor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Job, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_ode.grid._t_InteractiveProcessorPrx, False, 0), (_M_ode._t_ServerError,))
    SharedResources._op_addProcessor = IcePy.Operation('addProcessor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.grid._t_ProcessorPrx, False, 0),), (), None, (_M_ode._t_ServerError,))
    SharedResources._op_removeProcessor = IcePy.Operation('removeProcessor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.grid._t_ProcessorPrx, False, 0),), (), None, (_M_ode._t_ServerError,))
    SharedResources._op_repositories = IcePy.Operation('repositories', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), _M_ode.grid._t_RepositoryMap, False, 0), (_M_ode._t_ServerError,))
    SharedResources._op_getScriptRepository = IcePy.Operation('getScriptRepository', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), _M_ode.grid._t_RepositoryPrx, False, 0), (_M_ode._t_ServerError,))
    SharedResources._op_areTablesEnabled = IcePy.Operation('areTablesEnabled', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    SharedResources._op_newTable = IcePy.Operation('newTable', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.grid._t_TablePrx, False, 0), (_M_ode._t_ServerError,))
    SharedResources._op_openTable = IcePy.Operation('openTable', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), ((), _M_ode.grid._t_TablePrx, False, 0), (_M_ode._t_ServerError,))

    _M_ode.grid.SharedResources = SharedResources
    del SharedResources

    _M_ode.grid.SharedResourcesPrx = SharedResourcesPrx
    del SharedResourcesPrx

# End of module ode.grid

__name__ = 'ode'

# End of module ode
