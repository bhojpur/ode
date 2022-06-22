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

if 'SendEmailRequest' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.SendEmailRequest = Ice.createTempClass()
    class SendEmailRequest(_M_ode.cmd.Request):
        """
        Requests an email to be send to all users of the Bhojpur ODE
        determines inactive users, an active members of given groups
        and/or specific users.
        examples:
        - ode.cmd.SendEmailRequest(subject, body, everyone=True)
        sends message to everyone who has email set
        and is an active user
        - ode.cmd.SendEmailRequest(subject, body, everyone=True, inactive=True)
        sends message to everyone who has email set,
        even inactive users
        - ode.cmd.SendEmailRequest(subject, body, groupIds=\[...],
        userIds=\[...] )
        sends email to active members of given groups and selected users
        - extra=\[...] allows to set extra email address if not in DB
        """
        def __init__(self, subject='', body='', html=False, userIds=None, groupIds=None, extra=None, inactive=False, everyone=False):
            _M_ode.cmd.Request.__init__(self)
            self.subject = subject
            self.body = body
            self.html = html
            self.userIds = userIds
            self.groupIds = groupIds
            self.extra = extra
            self.inactive = inactive
            self.everyone = everyone

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Request', '::ode::cmd::SendEmailRequest')

        def ice_id(self, current=None):
            return '::ode::cmd::SendEmailRequest'

        def ice_staticId():
            return '::ode::cmd::SendEmailRequest'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_SendEmailRequest)

        __repr__ = __str__

    _M_ode.cmd.SendEmailRequestPrx = Ice.createTempClass()
    class SendEmailRequestPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.SendEmailRequestPrx.ice_checkedCast(proxy, '::ode::cmd::SendEmailRequest', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.SendEmailRequestPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::SendEmailRequest'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_SendEmailRequestPrx = IcePy.defineProxy('::ode::cmd::SendEmailRequest', SendEmailRequestPrx)

    _M_ode.cmd._t_SendEmailRequest = IcePy.defineClass('::ode::cmd::SendEmailRequest', SendEmailRequest, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('subject', (), IcePy._t_string, False, 0),
        ('body', (), IcePy._t_string, False, 0),
        ('html', (), IcePy._t_bool, False, 0),
        ('userIds', (), _M_ode.sys._t_LongList, False, 0),
        ('groupIds', (), _M_ode.sys._t_LongList, False, 0),
        ('extra', (), _M_ode.api._t_StringSet, False, 0),
        ('inactive', (), IcePy._t_bool, False, 0),
        ('everyone', (), IcePy._t_bool, False, 0)
    ))
    SendEmailRequest._ice_type = _M_ode.cmd._t_SendEmailRequest

    _M_ode.cmd.SendEmailRequest = SendEmailRequest
    del SendEmailRequest

    _M_ode.cmd.SendEmailRequestPrx = SendEmailRequestPrx
    del SendEmailRequestPrx

if 'SendEmailResponse' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.SendEmailResponse = Ice.createTempClass()
    class SendEmailResponse(_M_ode.cmd.Response):
        """
        Successful response for {@code SendEmailRequest}. Contains
        a list of invalid users that has no email address set.
        If no recipients or invalid users found, an {@code ERR} will be
        returned.
        - invalidusers is a list of userIds that email didn't pass criteria
        such as was empty or less then 5 characters
        - invalidemails is a list of email addresses that send email failed
        - total is a total number of email in the pull to be sent.
        - success is a number of emails that were sent successfully.
        """
        def __init__(self, total=0, success=0, invalidusers=None, invalidemails=None):
            _M_ode.cmd.Response.__init__(self)
            self.total = total
            self.success = success
            self.invalidusers = invalidusers
            self.invalidemails = invalidemails

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Response', '::ode::cmd::SendEmailResponse')

        def ice_id(self, current=None):
            return '::ode::cmd::SendEmailResponse'

        def ice_staticId():
            return '::ode::cmd::SendEmailResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_SendEmailResponse)

        __repr__ = __str__

    _M_ode.cmd.SendEmailResponsePrx = Ice.createTempClass()
    class SendEmailResponsePrx(_M_ode.cmd.ResponsePrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.SendEmailResponsePrx.ice_checkedCast(proxy, '::ode::cmd::SendEmailResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.SendEmailResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::SendEmailResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_SendEmailResponsePrx = IcePy.defineProxy('::ode::cmd::SendEmailResponse', SendEmailResponsePrx)

    _M_ode.cmd._t_SendEmailResponse = IcePy.defineClass('::ode::cmd::SendEmailResponse', SendEmailResponse, -1, (), False, False, _M_ode.cmd._t_Response, (), (
        ('total', (), IcePy._t_long, False, 0),
        ('success', (), IcePy._t_long, False, 0),
        ('invalidusers', (), _M_ode.api._t_LongList, False, 0),
        ('invalidemails', (), _M_ode.api._t_StringSet, False, 0)
    ))
    SendEmailResponse._ice_type = _M_ode.cmd._t_SendEmailResponse

    _M_ode.cmd.SendEmailResponse = SendEmailResponse
    del SendEmailResponse

    _M_ode.cmd.SendEmailResponsePrx = SendEmailResponsePrx
    del SendEmailResponsePrx

# End of module ode.cmd

__name__ = 'ode'

# End of module ode
