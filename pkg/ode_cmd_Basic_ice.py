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

# Start of module ode
__name__ = 'ode'

# Start of module ode.cmd
__name__ = 'ode.cmd'

if 'DoAll' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.DoAll = Ice.createTempClass()
    class DoAll(_M_ode.cmd.Request):
        def __init__(self, requests=None, contexts=None):
            _M_ode.cmd.Request.__init__(self)
            self.requests = requests
            self.contexts = contexts

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::DoAll', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::DoAll'

        def ice_staticId():
            return '::ode::cmd::DoAll'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_DoAll)

        __repr__ = __str__

    _M_ode.cmd.DoAllPrx = Ice.createTempClass()
    class DoAllPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.DoAllPrx.ice_checkedCast(proxy, '::ode::cmd::DoAll', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.DoAllPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::DoAll'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_DoAllPrx = IcePy.defineProxy('::ode::cmd::DoAll', DoAllPrx)

    _M_ode.cmd._t_DoAll = IcePy.declareClass('::ode::cmd::DoAll')

    _M_ode.cmd._t_DoAll = IcePy.defineClass('::ode::cmd::DoAll', DoAll, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('requests', (), _M_ode.cmd._t_RequestList, False, 0),
        ('contexts', (), _M_ode.cmd._t_StringMapList, False, 0)
    ))
    DoAll._ice_type = _M_ode.cmd._t_DoAll

    _M_ode.cmd.DoAll = DoAll
    del DoAll

    _M_ode.cmd.DoAllPrx = DoAllPrx
    del DoAllPrx

if 'DoAllRsp' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.DoAllRsp = Ice.createTempClass()
    class DoAllRsp(_M_ode.cmd.OK):
        def __init__(self, responses=None, status=None):
            _M_ode.cmd.OK.__init__(self)
            self.responses = responses
            self.status = status

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::DoAllRsp', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::DoAllRsp'

        def ice_staticId():
            return '::ode::cmd::DoAllRsp'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_DoAllRsp)

        __repr__ = __str__

    _M_ode.cmd.DoAllRspPrx = Ice.createTempClass()
    class DoAllRspPrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.DoAllRspPrx.ice_checkedCast(proxy, '::ode::cmd::DoAllRsp', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.DoAllRspPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::DoAllRsp'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_DoAllRspPrx = IcePy.defineProxy('::ode::cmd::DoAllRsp', DoAllRspPrx)

    _M_ode.cmd._t_DoAllRsp = IcePy.declareClass('::ode::cmd::DoAllRsp')

    _M_ode.cmd._t_DoAllRsp = IcePy.defineClass('::ode::cmd::DoAllRsp', DoAllRsp, -1, (), False, False, _M_ode.cmd._t_OK, (), (
        ('responses', (), _M_ode.cmd._t_ResponseList, False, 0),
        ('status', (), _M_ode.cmd._t_StatusList, False, 0)
    ))
    DoAllRsp._ice_type = _M_ode.cmd._t_DoAllRsp

    _M_ode.cmd.DoAllRsp = DoAllRsp
    del DoAllRsp

    _M_ode.cmd.DoAllRspPrx = DoAllRspPrx
    del DoAllRspPrx

if 'ListRequests' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.ListRequests = Ice.createTempClass()
    class ListRequests(_M_ode.cmd.Request):
        def __init__(self):
            _M_ode.cmd.Request.__init__(self)

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::ListRequests', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::ListRequests'

        def ice_staticId():
            return '::ode::cmd::ListRequests'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_ListRequests)

        __repr__ = __str__

    _M_ode.cmd.ListRequestsPrx = Ice.createTempClass()
    class ListRequestsPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.ListRequestsPrx.ice_checkedCast(proxy, '::ode::cmd::ListRequests', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.ListRequestsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::ListRequests'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_ListRequestsPrx = IcePy.defineProxy('::ode::cmd::ListRequests', ListRequestsPrx)

    _M_ode.cmd._t_ListRequests = IcePy.defineClass('::ode::cmd::ListRequests', ListRequests, -1, (), False, False, _M_ode.cmd._t_Request, (), ())
    ListRequests._ice_type = _M_ode.cmd._t_ListRequests

    _M_ode.cmd.ListRequests = ListRequests
    del ListRequests

    _M_ode.cmd.ListRequestsPrx = ListRequestsPrx
    del ListRequestsPrx

if 'ListRequestsRsp' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.ListRequestsRsp = Ice.createTempClass()
    class ListRequestsRsp(_M_ode.cmd.OK):
        def __init__(self, list=None):
            _M_ode.cmd.OK.__init__(self)
            self.list = list

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::ListRequestsRsp', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::ListRequestsRsp'

        def ice_staticId():
            return '::ode::cmd::ListRequestsRsp'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_ListRequestsRsp)

        __repr__ = __str__

    _M_ode.cmd.ListRequestsRspPrx = Ice.createTempClass()
    class ListRequestsRspPrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.ListRequestsRspPrx.ice_checkedCast(proxy, '::ode::cmd::ListRequestsRsp', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.ListRequestsRspPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::ListRequestsRsp'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_ListRequestsRspPrx = IcePy.defineProxy('::ode::cmd::ListRequestsRsp', ListRequestsRspPrx)

    _M_ode.cmd._t_ListRequestsRsp = IcePy.declareClass('::ode::cmd::ListRequestsRsp')

    _M_ode.cmd._t_ListRequestsRsp = IcePy.defineClass('::ode::cmd::ListRequestsRsp', ListRequestsRsp, -1, (), False, False, _M_ode.cmd._t_OK, (), (('list', (), _M_ode.cmd._t_RequestList, False, 0),))
    ListRequestsRsp._ice_type = _M_ode.cmd._t_ListRequestsRsp

    _M_ode.cmd.ListRequestsRsp = ListRequestsRsp
    del ListRequestsRsp

    _M_ode.cmd.ListRequestsRspPrx = ListRequestsRspPrx
    del ListRequestsRspPrx

if 'PopStatus' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.PopStatus = Ice.createTempClass()
    class PopStatus(_M_ode.cmd.Request):
        def __init__(self, limit=0, include=None, exclude=None):
            _M_ode.cmd.Request.__init__(self)
            self.limit = limit
            self.include = include
            self.exclude = exclude

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::PopStatus', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::PopStatus'

        def ice_staticId():
            return '::ode::cmd::PopStatus'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_PopStatus)

        __repr__ = __str__

    _M_ode.cmd.PopStatusPrx = Ice.createTempClass()
    class PopStatusPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.PopStatusPrx.ice_checkedCast(proxy, '::ode::cmd::PopStatus', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.PopStatusPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::PopStatus'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_PopStatusPrx = IcePy.defineProxy('::ode::cmd::PopStatus', PopStatusPrx)

    _M_ode.cmd._t_PopStatus = IcePy.defineClass('::ode::cmd::PopStatus', PopStatus, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('limit', (), IcePy._t_int, False, 0),
        ('include', (), _M_ode.cmd._t_StateList, False, 0),
        ('exclude', (), _M_ode.cmd._t_StateList, False, 0)
    ))
    PopStatus._ice_type = _M_ode.cmd._t_PopStatus

    _M_ode.cmd.PopStatus = PopStatus
    del PopStatus

    _M_ode.cmd.PopStatusPrx = PopStatusPrx
    del PopStatusPrx

if 'PopStatusRsp' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.PopStatusRsp = Ice.createTempClass()
    class PopStatusRsp(_M_ode.cmd.OK):
        def __init__(self, list=None):
            _M_ode.cmd.OK.__init__(self)
            self.list = list

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::OK', '::ode::cmd::PopStatusRsp', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::PopStatusRsp'

        def ice_staticId():
            return '::ode::cmd::PopStatusRsp'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_PopStatusRsp)

        __repr__ = __str__

    _M_ode.cmd.PopStatusRspPrx = Ice.createTempClass()
    class PopStatusRspPrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.PopStatusRspPrx.ice_checkedCast(proxy, '::ode::cmd::PopStatusRsp', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.PopStatusRspPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::PopStatusRsp'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_PopStatusRspPrx = IcePy.defineProxy('::ode::cmd::PopStatusRsp', PopStatusRspPrx)

    _M_ode.cmd._t_PopStatusRsp = IcePy.declareClass('::ode::cmd::PopStatusRsp')

    _M_ode.cmd._t_PopStatusRsp = IcePy.defineClass('::ode::cmd::PopStatusRsp', PopStatusRsp, -1, (), False, False, _M_ode.cmd._t_OK, (), (('list', (), _M_ode.cmd._t_StatusList, False, 0),))
    PopStatusRsp._ice_type = _M_ode.cmd._t_PopStatusRsp

    _M_ode.cmd.PopStatusRsp = PopStatusRsp
    del PopStatusRsp

    _M_ode.cmd.PopStatusRspPrx = PopStatusRspPrx
    del PopStatusRspPrx

if 'FindHandles' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.FindHandles = Ice.createTempClass()
    class FindHandles(_M_ode.cmd.Request):
        def __init__(self, limit=0, include=None, exclude=None):
            _M_ode.cmd.Request.__init__(self)
            self.limit = limit
            self.include = include
            self.exclude = exclude

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::FindHandles', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::FindHandles'

        def ice_staticId():
            return '::ode::cmd::FindHandles'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_FindHandles)

        __repr__ = __str__

    _M_ode.cmd.FindHandlesPrx = Ice.createTempClass()
    class FindHandlesPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.FindHandlesPrx.ice_checkedCast(proxy, '::ode::cmd::FindHandles', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.FindHandlesPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::FindHandles'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_FindHandlesPrx = IcePy.defineProxy('::ode::cmd::FindHandles', FindHandlesPrx)

    _M_ode.cmd._t_FindHandles = IcePy.defineClass('::ode::cmd::FindHandles', FindHandles, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('limit', (), IcePy._t_int, False, 0),
        ('include', (), _M_ode.cmd._t_StateList, False, 0),
        ('exclude', (), _M_ode.cmd._t_StateList, False, 0)
    ))
    FindHandles._ice_type = _M_ode.cmd._t_FindHandles

    _M_ode.cmd.FindHandles = FindHandles
    del FindHandles

    _M_ode.cmd.FindHandlesPrx = FindHandlesPrx
    del FindHandlesPrx

if 'FindHandlesRsp' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.FindHandlesRsp = Ice.createTempClass()
    class FindHandlesRsp(_M_ode.cmd.OK):
        def __init__(self, handles=None):
            _M_ode.cmd.OK.__init__(self)
            self.handles = handles

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::FindHandlesRsp', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::FindHandlesRsp'

        def ice_staticId():
            return '::ode::cmd::FindHandlesRsp'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_FindHandlesRsp)

        __repr__ = __str__

    _M_ode.cmd.FindHandlesRspPrx = Ice.createTempClass()
    class FindHandlesRspPrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.FindHandlesRspPrx.ice_checkedCast(proxy, '::ode::cmd::FindHandlesRsp', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.FindHandlesRspPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::FindHandlesRsp'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_FindHandlesRspPrx = IcePy.defineProxy('::ode::cmd::FindHandlesRsp', FindHandlesRspPrx)

    _M_ode.cmd._t_FindHandlesRsp = IcePy.defineClass('::ode::cmd::FindHandlesRsp', FindHandlesRsp, -1, (), False, False, _M_ode.cmd._t_OK, (), (('handles', (), _M_ode.cmd._t_HandleList, False, 0),))
    FindHandlesRsp._ice_type = _M_ode.cmd._t_FindHandlesRsp

    _M_ode.cmd.FindHandlesRsp = FindHandlesRsp
    del FindHandlesRsp

    _M_ode.cmd.FindHandlesRspPrx = FindHandlesRspPrx
    del FindHandlesRspPrx

if 'Timing' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.Timing = Ice.createTempClass()
    class Timing(_M_ode.cmd.Request):
        """
        Diagnostic command which can be used to see the overhead
        of callbacks. The number of steps and the simulated workload
        can be specified.
        Members:
        steps -- Number of steps that will be run by this command. Value is
        limited by the overall invocation time (5 minutes) as well as
        total number of calls (e.g. 100000)
        millisPerStep -- Number of millis to wait. This value simulates activity on the server.
        Value is limited by the overall invocation time (5 minutes).
        """
        def __init__(self, steps=0, millisPerStep=0):
            _M_ode.cmd.Request.__init__(self)
            self.steps = steps
            self.millisPerStep = millisPerStep

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Request', '::ode::cmd::Timing')

        def ice_id(self, current=None):
            return '::ode::cmd::Timing'

        def ice_staticId():
            return '::ode::cmd::Timing'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_Timing)

        __repr__ = __str__

    _M_ode.cmd.TimingPrx = Ice.createTempClass()
    class TimingPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.TimingPrx.ice_checkedCast(proxy, '::ode::cmd::Timing', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.TimingPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::Timing'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_TimingPrx = IcePy.defineProxy('::ode::cmd::Timing', TimingPrx)

    _M_ode.cmd._t_Timing = IcePy.defineClass('::ode::cmd::Timing', Timing, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('steps', (), IcePy._t_int, False, 0),
        ('millisPerStep', (), IcePy._t_int, False, 0)
    ))
    Timing._ice_type = _M_ode.cmd._t_Timing

    _M_ode.cmd.Timing = Timing
    del Timing

    _M_ode.cmd.TimingPrx = TimingPrx
    del TimingPrx

# End of module ode.cmd

__name__ = 'ode'

# End of module ode
