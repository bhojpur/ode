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
import ode_Collections_ice
import ode_System_ice
import ode_cmd_API_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.cmd
_M_ode.cmd = Ice.openModule('ode.cmd')

# Start of module ode
__name__ = 'ode'

# Start of module ode.cmd
__name__ = 'ode.cmd'

if 'ResetPasswordRequest' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.ResetPasswordRequest = Ice.createTempClass()
    class ResetPasswordRequest(_M_ode.cmd.Request):
        """
        Requests a reset password for the given user.
        The user must not be an administrator.
        examples:
        - ode.cmd.ResetPasswordRequest(odename, email)
        sends new password to the given user
        """
        def __init__(self, odename='', email=''):
            _M_ode.cmd.Request.__init__(self)
            self.odename = odename
            self.email = email

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Request', '::ode::cmd::ResetPasswordRequest')

        def ice_id(self, current=None):
            return '::ode::cmd::ResetPasswordRequest'

        def ice_staticId():
            return '::ode::cmd::ResetPasswordRequest'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_ResetPasswordRequest)

        __repr__ = __str__

    _M_ode.cmd.ResetPasswordRequestPrx = Ice.createTempClass()
    class ResetPasswordRequestPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.ResetPasswordRequestPrx.ice_checkedCast(proxy, '::ode::cmd::ResetPasswordRequest', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.ResetPasswordRequestPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::ResetPasswordRequest'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_ResetPasswordRequestPrx = IcePy.defineProxy('::ode::cmd::ResetPasswordRequest', ResetPasswordRequestPrx)

    _M_ode.cmd._t_ResetPasswordRequest = IcePy.defineClass('::ode::cmd::ResetPasswordRequest', ResetPasswordRequest, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('odename', (), IcePy._t_string, False, 0),
        ('email', (), IcePy._t_string, False, 0)
    ))
    ResetPasswordRequest._ice_type = _M_ode.cmd._t_ResetPasswordRequest

    _M_ode.cmd.ResetPasswordRequest = ResetPasswordRequest
    del ResetPasswordRequest

    _M_ode.cmd.ResetPasswordRequestPrx = ResetPasswordRequestPrx
    del ResetPasswordRequestPrx

if 'ResetPasswordResponse' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.ResetPasswordResponse = Ice.createTempClass()
    class ResetPasswordResponse(_M_ode.cmd.OK):
        """
        Successful response for ResetPasswordRequest.
        If no valid user with matching email is found,
        an ERR will be returned.
        """
        def __init__(self):
            _M_ode.cmd.OK.__init__(self)

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::OK', '::ode::cmd::ResetPasswordResponse', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::ResetPasswordResponse'

        def ice_staticId():
            return '::ode::cmd::ResetPasswordResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_ResetPasswordResponse)

        __repr__ = __str__

    _M_ode.cmd.ResetPasswordResponsePrx = Ice.createTempClass()
    class ResetPasswordResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.ResetPasswordResponsePrx.ice_checkedCast(proxy, '::ode::cmd::ResetPasswordResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.ResetPasswordResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::ResetPasswordResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_ResetPasswordResponsePrx = IcePy.defineProxy('::ode::cmd::ResetPasswordResponse', ResetPasswordResponsePrx)

    _M_ode.cmd._t_ResetPasswordResponse = IcePy.defineClass('::ode::cmd::ResetPasswordResponse', ResetPasswordResponse, -1, (), False, False, _M_ode.cmd._t_OK, (), ())
    ResetPasswordResponse._ice_type = _M_ode.cmd._t_ResetPasswordResponse

    _M_ode.cmd.ResetPasswordResponse = ResetPasswordResponse
    del ResetPasswordResponse

    _M_ode.cmd.ResetPasswordResponsePrx = ResetPasswordResponsePrx
    del ResetPasswordResponsePrx

if 'UpdateSessionTimeoutRequest' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.UpdateSessionTimeoutRequest = Ice.createTempClass()
    class UpdateSessionTimeoutRequest(_M_ode.cmd.Request):
        """
        Proposes a change to one or both of the timeToLive
        and timeToIdle properties of a live session. The session
        uuid cannot be null. If either other argument is null,
        it will be ignored. Otherwise, the long value will be
        interpreted as the the millisecond value which should
        be set. Non-administrators will not be able to reduce
        current values. No special response is returned, but
        an ode.cmd.OK counts as success.
        """
        def __init__(self, session='', timeToLive=None, timeToIdle=None):
            _M_ode.cmd.Request.__init__(self)
            self.session = session
            self.timeToLive = timeToLive
            self.timeToIdle = timeToIdle

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Request', '::ode::cmd::UpdateSessionTimeoutRequest')

        def ice_id(self, current=None):
            return '::ode::cmd::UpdateSessionTimeoutRequest'

        def ice_staticId():
            return '::ode::cmd::UpdateSessionTimeoutRequest'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_UpdateSessionTimeoutRequest)

        __repr__ = __str__

    _M_ode.cmd.UpdateSessionTimeoutRequestPrx = Ice.createTempClass()
    class UpdateSessionTimeoutRequestPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.UpdateSessionTimeoutRequestPrx.ice_checkedCast(proxy, '::ode::cmd::UpdateSessionTimeoutRequest', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.UpdateSessionTimeoutRequestPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::UpdateSessionTimeoutRequest'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_UpdateSessionTimeoutRequestPrx = IcePy.defineProxy('::ode::cmd::UpdateSessionTimeoutRequest', UpdateSessionTimeoutRequestPrx)

    _M_ode.cmd._t_UpdateSessionTimeoutRequest = IcePy.declareClass('::ode::cmd::UpdateSessionTimeoutRequest')

    _M_ode.cmd._t_UpdateSessionTimeoutRequest = IcePy.defineClass('::ode::cmd::UpdateSessionTimeoutRequest', UpdateSessionTimeoutRequest, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('session', (), IcePy._t_string, False, 0),
        ('timeToLive', (), _M_ode._t_RLong, False, 0),
        ('timeToIdle', (), _M_ode._t_RLong, False, 0)
    ))
    UpdateSessionTimeoutRequest._ice_type = _M_ode.cmd._t_UpdateSessionTimeoutRequest

    _M_ode.cmd.UpdateSessionTimeoutRequest = UpdateSessionTimeoutRequest
    del UpdateSessionTimeoutRequest

    _M_ode.cmd.UpdateSessionTimeoutRequestPrx = UpdateSessionTimeoutRequestPrx
    del UpdateSessionTimeoutRequestPrx

if 'CurrentSessionsRequest' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.CurrentSessionsRequest = Ice.createTempClass()
    class CurrentSessionsRequest(_M_ode.cmd.Request):
        """
        Argument-less request that will produce a
        CurrentSessionsResponse if no ode.cmd.ERR occurs.
        """
        def __init__(self):
            _M_ode.cmd.Request.__init__(self)

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::CurrentSessionsRequest', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::CurrentSessionsRequest'

        def ice_staticId():
            return '::ode::cmd::CurrentSessionsRequest'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_CurrentSessionsRequest)

        __repr__ = __str__

    _M_ode.cmd.CurrentSessionsRequestPrx = Ice.createTempClass()
    class CurrentSessionsRequestPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.CurrentSessionsRequestPrx.ice_checkedCast(proxy, '::ode::cmd::CurrentSessionsRequest', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.CurrentSessionsRequestPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::CurrentSessionsRequest'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_CurrentSessionsRequestPrx = IcePy.defineProxy('::ode::cmd::CurrentSessionsRequest', CurrentSessionsRequestPrx)

    _M_ode.cmd._t_CurrentSessionsRequest = IcePy.defineClass('::ode::cmd::CurrentSessionsRequest', CurrentSessionsRequest, -1, (), False, False, _M_ode.cmd._t_Request, (), ())
    CurrentSessionsRequest._ice_type = _M_ode.cmd._t_CurrentSessionsRequest

    _M_ode.cmd.CurrentSessionsRequest = CurrentSessionsRequest
    del CurrentSessionsRequest

    _M_ode.cmd.CurrentSessionsRequestPrx = CurrentSessionsRequestPrx
    del CurrentSessionsRequestPrx

if 'CurrentSessionsResponse' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.CurrentSessionsResponse = Ice.createTempClass()
    class CurrentSessionsResponse(_M_ode.cmd.OK):
        """
        Return value from ode.cmd.CurrentSessionsRequest
        consisting of two ordered lists of matching length. The sessions
        field contains a list of the ode ode.model.Session
        objects that are currently active *after* all timeouts have been
        applied.
        This is the value that would be returned by
        {@code ode.api.ISession.getSession} when joined to that session.
        Similarly, the contexts field contains the value that would be
        returned by a call to {@code ode.api.IAdmin.getEventContext}.
        For non-administrators, most values for all sessions other than
        those belonging to that user will be null.
        Members:
        sessions -- ode.model.Session objects loaded from
        the database.
        contexts -- ode.sys.EventContext objects stored in
        memory by the server.
        data -- Other session state which may vary based on
        usage. This may include "hitCount", "lastAccess",
        and similar metrics.
        """
        def __init__(self, sessions=None, contexts=None, data=None):
            _M_ode.cmd.OK.__init__(self)
            self.sessions = sessions
            self.contexts = contexts
            self.data = data

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::CurrentSessionsResponse', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::CurrentSessionsResponse'

        def ice_staticId():
            return '::ode::cmd::CurrentSessionsResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_CurrentSessionsResponse)

        __repr__ = __str__

    _M_ode.cmd.CurrentSessionsResponsePrx = Ice.createTempClass()
    class CurrentSessionsResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.CurrentSessionsResponsePrx.ice_checkedCast(proxy, '::ode::cmd::CurrentSessionsResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.CurrentSessionsResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::CurrentSessionsResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_CurrentSessionsResponsePrx = IcePy.defineProxy('::ode::cmd::CurrentSessionsResponse', CurrentSessionsResponsePrx)

    _M_ode.cmd._t_CurrentSessionsResponse = IcePy.declareClass('::ode::cmd::CurrentSessionsResponse')

    _M_ode.cmd._t_CurrentSessionsResponse = IcePy.defineClass('::ode::cmd::CurrentSessionsResponse', CurrentSessionsResponse, -1, (), False, False, _M_ode.cmd._t_OK, (), (
        ('sessions', (), _M_ode.api._t_SessionList, False, 0),
        ('contexts', (), _M_ode.api._t_EventContextList, False, 0),
        ('data', (), _M_ode.api._t_RTypeDictArray, False, 0)
    ))
    CurrentSessionsResponse._ice_type = _M_ode.cmd._t_CurrentSessionsResponse

    _M_ode.cmd.CurrentSessionsResponse = CurrentSessionsResponse
    del CurrentSessionsResponse

    _M_ode.cmd.CurrentSessionsResponsePrx = CurrentSessionsResponsePrx
    del CurrentSessionsResponsePrx

# End of module ode.cmd

__name__ = 'ode'

# End of module ode
