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

# Start of module ode
_M_ode = Ice.openModule('ode')
__name__ = 'ode'

# Start of module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')
__name__ = 'ode.grid'

if 'ClusterNode' not in _M_ode.grid.__dict__:
    _M_ode.grid.ClusterNode = Ice.createTempClass()
    class ClusterNode(Ice.Object):
        """
        Interface implemented by each server instance. Instances lookup one
        another in the IceGrid registry.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.ClusterNode:
                raise RuntimeError('ode.grid.ClusterNode is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::ClusterNode')

        def ice_id(self, current=None):
            return '::ode::grid::ClusterNode'

        def ice_staticId():
            return '::ode::grid::ClusterNode'
        ice_staticId = staticmethod(ice_staticId)

        def getNodeUuid(self, current=None):
            """
            Each node acquires the uuids of all other active nodes on start
            up. The uuid is an internal value and does not
            correspond to a session.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def down(self, uuid, current=None):
            """
            Let all cluster nodes know that the instance with this
            uuid is going down.
            Arguments:
            uuid -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ClusterNode)

        __repr__ = __str__

    _M_ode.grid.ClusterNodePrx = Ice.createTempClass()
    class ClusterNodePrx(Ice.ObjectPrx):

        """
        Each node acquires the uuids of all other active nodes on start
        up. The uuid is an internal value and does not
        correspond to a session.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getNodeUuid(self, _ctx=None):
            return _M_ode.grid.ClusterNode._op_getNodeUuid.invoke(self, ((), _ctx))

        """
        Each node acquires the uuids of all other active nodes on start
        up. The uuid is an internal value and does not
        correspond to a session.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getNodeUuid(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ClusterNode._op_getNodeUuid.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Each node acquires the uuids of all other active nodes on start
        up. The uuid is an internal value and does not
        correspond to a session.
        Arguments:
        """
        def end_getNodeUuid(self, _r):
            return _M_ode.grid.ClusterNode._op_getNodeUuid.end(self, _r)

        """
        Let all cluster nodes know that the instance with this
        uuid is going down.
        Arguments:
        uuid -- 
        _ctx -- The request context for the invocation.
        """
        def down(self, uuid, _ctx=None):
            return _M_ode.grid.ClusterNode._op_down.invoke(self, ((uuid, ), _ctx))

        """
        Let all cluster nodes know that the instance with this
        uuid is going down.
        Arguments:
        uuid -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_down(self, uuid, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ClusterNode._op_down.begin(self, ((uuid, ), _response, _ex, _sent, _ctx))

        """
        Let all cluster nodes know that the instance with this
        uuid is going down.
        Arguments:
        uuid -- 
        """
        def end_down(self, _r):
            return _M_ode.grid.ClusterNode._op_down.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ClusterNodePrx.ice_checkedCast(proxy, '::ode::grid::ClusterNode', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ClusterNodePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ClusterNode'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ClusterNodePrx = IcePy.defineProxy('::ode::grid::ClusterNode', ClusterNodePrx)

    _M_ode.grid._t_ClusterNode = IcePy.defineClass('::ode::grid::ClusterNode', ClusterNode, -1, (), True, False, None, (), ())
    ClusterNode._ice_type = _M_ode.grid._t_ClusterNode

    ClusterNode._op_getNodeUuid = IcePy.Operation('getNodeUuid', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), IcePy._t_string, False, 0), ())
    ClusterNode._op_down = IcePy.Operation('down', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), None, ())

    _M_ode.grid.ClusterNode = ClusterNode
    del ClusterNode

    _M_ode.grid.ClusterNodePrx = ClusterNodePrx
    del ClusterNodePrx

# End of module ode.grid

__name__ = 'ode'

# End of module ode
