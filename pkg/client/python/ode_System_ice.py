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
import ode_SystemF_ice
import ode_Collections_ice

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

# Start of module ode
__name__ = 'ode'

# Start of module ode.sys
__name__ = 'ode.sys'

if 'EventContext' not in _M_ode.sys.__dict__:
    _M_ode.sys.EventContext = Ice.createTempClass()
    class EventContext(Ice.Object):
        """
        Maps the ode.system.EventContext interface. Represents the
        information known by the server security system about the
        current user login.
        """
        def __init__(self, shareId=0, sessionId=0, sessionUuid='', userId=0, userName='', sudoerId=None, sudoerName=None, groupId=0, groupName='', isAdmin=False, adminPrivileges=None, eventId=0, eventType='', memberOfGroups=None, leaderOfGroups=None, groupPermissions=None):
            self.shareId = shareId
            self.sessionId = sessionId
            self.sessionUuid = sessionUuid
            self.userId = userId
            self.userName = userName
            self.sudoerId = sudoerId
            self.sudoerName = sudoerName
            self.groupId = groupId
            self.groupName = groupName
            self.isAdmin = isAdmin
            self.adminPrivileges = adminPrivileges
            self.eventId = eventId
            self.eventType = eventType
            self.memberOfGroups = memberOfGroups
            self.leaderOfGroups = leaderOfGroups
            self.groupPermissions = groupPermissions

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::sys::EventContext')

        def ice_id(self, current=None):
            return '::ode::sys::EventContext'

        def ice_staticId():
            return '::ode::sys::EventContext'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.sys._t_EventContext)

        __repr__ = __str__

    _M_ode.sys.EventContextPrx = Ice.createTempClass()
    class EventContextPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.sys.EventContextPrx.ice_checkedCast(proxy, '::ode::sys::EventContext', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.sys.EventContextPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::sys::EventContext'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.sys._t_EventContextPrx = IcePy.defineProxy('::ode::sys::EventContext', EventContextPrx)

    _M_ode.sys._t_EventContext = IcePy.declareClass('::ode::sys::EventContext')

    _M_ode.sys._t_EventContext = IcePy.defineClass('::ode::sys::EventContext', EventContext, -1, (), False, False, None, (), (
        ('shareId', (), IcePy._t_long, False, 0),
        ('sessionId', (), IcePy._t_long, False, 0),
        ('sessionUuid', (), IcePy._t_string, False, 0),
        ('userId', (), IcePy._t_long, False, 0),
        ('userName', (), IcePy._t_string, False, 0),
        ('sudoerId', (), _M_ode._t_RLong, False, 0),
        ('sudoerName', (), _M_ode._t_RString, False, 0),
        ('groupId', (), IcePy._t_long, False, 0),
        ('groupName', (), IcePy._t_string, False, 0),
        ('isAdmin', (), IcePy._t_bool, False, 0),
        ('adminPrivileges', (), _M_ode.api._t_StringSet, False, 0),
        ('eventId', (), IcePy._t_long, False, 0),
        ('eventType', (), IcePy._t_string, False, 0),
        ('memberOfGroups', (), _M_ode.sys._t_LongList, False, 0),
        ('leaderOfGroups', (), _M_ode.sys._t_LongList, False, 0),
        ('groupPermissions', (), _M_ode.model._t_Permissions, False, 0)
    ))
    EventContext._ice_type = _M_ode.sys._t_EventContext

    _M_ode.sys.EventContext = EventContext
    del EventContext

    _M_ode.sys.EventContextPrx = EventContextPrx
    del EventContextPrx

if 'Filter' not in _M_ode.sys.__dict__:
    _M_ode.sys.Filter = Ice.createTempClass()
    class Filter(Ice.Object):
        """
        Provides common filters which MAY be applied to a
        query. Check the documentation for the particular
        method for more information on how these values will
        be interpreted as well as default values if they
        are missing. In general they are intended to mean:
        unique        := similar to SQL's ""DISTINCT"" keyword
        ownerId       := (some) objects queried should belong
        to this user
        groupId       := (some) objects queried should belong
        to this group
        preferOwner   := true implies if if ownerId and groupId
        are both defined, use only ownerId
        offset/limit  := represent a page which should be loaded
        Note: servers may choose to impose a
        maximum limit.
        start/endTime := (some) objects queried should have been
        created and/or modified within time span.
        """
        def __init__(self, unique=None, ownerId=None, groupId=None, offset=None, limit=None, startTime=None, endTime=None):
            self.unique = unique
            self.ownerId = ownerId
            self.groupId = groupId
            self.offset = offset
            self.limit = limit
            self.startTime = startTime
            self.endTime = endTime

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::sys::Filter')

        def ice_id(self, current=None):
            return '::ode::sys::Filter'

        def ice_staticId():
            return '::ode::sys::Filter'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.sys._t_Filter)

        __repr__ = __str__

    _M_ode.sys.FilterPrx = Ice.createTempClass()
    class FilterPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.sys.FilterPrx.ice_checkedCast(proxy, '::ode::sys::Filter', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.sys.FilterPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::sys::Filter'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.sys._t_FilterPrx = IcePy.defineProxy('::ode::sys::Filter', FilterPrx)

    _M_ode.sys._t_Filter = IcePy.declareClass('::ode::sys::Filter')

    _M_ode.sys._t_Filter = IcePy.defineClass('::ode::sys::Filter', Filter, -1, (), False, False, None, (), (
        ('unique', (), _M_ode._t_RBool, False, 0),
        ('ownerId', (), _M_ode._t_RLong, False, 0),
        ('groupId', (), _M_ode._t_RLong, False, 0),
        ('offset', (), _M_ode._t_RInt, False, 0),
        ('limit', (), _M_ode._t_RInt, False, 0),
        ('startTime', (), _M_ode._t_RTime, False, 0),
        ('endTime', (), _M_ode._t_RTime, False, 0)
    ))
    Filter._ice_type = _M_ode.sys._t_Filter

    _M_ode.sys.Filter = Filter
    del Filter

    _M_ode.sys.FilterPrx = FilterPrx
    del FilterPrx

if 'Options' not in _M_ode.sys.__dict__:
    _M_ode.sys.Options = Ice.createTempClass()
    class Options(Ice.Object):
        """
        Similar to Filter, provides common options which MAY be
        applied on a given method. Check each interface's
        documentation for more details.
        leaves        := whether or not graph leaves (usually images)
        should be loaded
        orphan        := whether or not orphaned objects (e.g. datasets
        not in a project) should be loaded
        acquisition...:= whether or not acquisitionData (objectives, etc.)
        should be loaded
        cacheable      := whether or not the query is cacheable by Hibernate
        (use with caution: caching may be counterproductive)
        """
        def __init__(self, leaves=None, orphan=None, acquisitionData=None, cacheable=None):
            self.leaves = leaves
            self.orphan = orphan
            self.acquisitionData = acquisitionData
            self.cacheable = cacheable

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::sys::Options')

        def ice_id(self, current=None):
            return '::ode::sys::Options'

        def ice_staticId():
            return '::ode::sys::Options'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.sys._t_Options)

        __repr__ = __str__

    _M_ode.sys.OptionsPrx = Ice.createTempClass()
    class OptionsPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.sys.OptionsPrx.ice_checkedCast(proxy, '::ode::sys::Options', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.sys.OptionsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::sys::Options'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.sys._t_OptionsPrx = IcePy.defineProxy('::ode::sys::Options', OptionsPrx)

    _M_ode.sys._t_Options = IcePy.declareClass('::ode::sys::Options')

    _M_ode.sys._t_Options = IcePy.defineClass('::ode::sys::Options', Options, -1, (), False, False, None, (), (
        ('leaves', (), _M_ode._t_RBool, False, 0),
        ('orphan', (), _M_ode._t_RBool, False, 0),
        ('acquisitionData', (), _M_ode._t_RBool, False, 0),
        ('cacheable', (), _M_ode._t_RBool, False, 0)
    ))
    Options._ice_type = _M_ode.sys._t_Options

    _M_ode.sys.Options = Options
    del Options

    _M_ode.sys.OptionsPrx = OptionsPrx
    del OptionsPrx

if 'Parameters' not in _M_ode.sys.__dict__:
    _M_ode.sys.Parameters = Ice.createTempClass()
    class Parameters(Ice.Object):
        """
        Holder for all the parameters which can be taken to a query.
        """
        def __init__(self, map=None, theFilter=None, theOptions=None):
            self.map = map
            self.theFilter = theFilter
            self.theOptions = theOptions

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::sys::Parameters')

        def ice_id(self, current=None):
            return '::ode::sys::Parameters'

        def ice_staticId():
            return '::ode::sys::Parameters'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.sys._t_Parameters)

        __repr__ = __str__

    _M_ode.sys.ParametersPrx = Ice.createTempClass()
    class ParametersPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.sys.ParametersPrx.ice_checkedCast(proxy, '::ode::sys::Parameters', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.sys.ParametersPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::sys::Parameters'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.sys._t_ParametersPrx = IcePy.defineProxy('::ode::sys::Parameters', ParametersPrx)

    _M_ode.sys._t_Parameters = IcePy.declareClass('::ode::sys::Parameters')

    _M_ode.sys._t_Parameters = IcePy.defineClass('::ode::sys::Parameters', Parameters, -1, (), False, False, None, (), (
        ('map', (), _M_ode.sys._t_ParamMap, False, 0),
        ('theFilter', (), _M_ode.sys._t_Filter, False, 0),
        ('theOptions', (), _M_ode.sys._t_Options, False, 0)
    ))
    Parameters._ice_type = _M_ode.sys._t_Parameters

    _M_ode.sys.Parameters = Parameters
    del Parameters

    _M_ode.sys.ParametersPrx = ParametersPrx
    del ParametersPrx

if 'Principal' not in _M_ode.sys.__dict__:
    _M_ode.sys.Principal = Ice.createTempClass()
    class Principal(Ice.Object):
        """
        Principal used for login, etc.
        """
        def __init__(self, name='', group='', eventType='', umask=None):
            self.name = name
            self.group = group
            self.eventType = eventType
            self.umask = umask

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::sys::Principal')

        def ice_id(self, current=None):
            return '::ode::sys::Principal'

        def ice_staticId():
            return '::ode::sys::Principal'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.sys._t_Principal)

        __repr__ = __str__

    _M_ode.sys.PrincipalPrx = Ice.createTempClass()
    class PrincipalPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.sys.PrincipalPrx.ice_checkedCast(proxy, '::ode::sys::Principal', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.sys.PrincipalPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::sys::Principal'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.sys._t_PrincipalPrx = IcePy.defineProxy('::ode::sys::Principal', PrincipalPrx)

    _M_ode.sys._t_Principal = IcePy.declareClass('::ode::sys::Principal')

    _M_ode.sys._t_Principal = IcePy.defineClass('::ode::sys::Principal', Principal, -1, (), False, False, None, (), (
        ('name', (), IcePy._t_string, False, 0),
        ('group', (), IcePy._t_string, False, 0),
        ('eventType', (), IcePy._t_string, False, 0),
        ('umask', (), _M_ode.model._t_Permissions, False, 0)
    ))
    Principal._ice_type = _M_ode.sys._t_Principal

    _M_ode.sys.Principal = Principal
    del Principal

    _M_ode.sys.PrincipalPrx = PrincipalPrx
    del PrincipalPrx

if 'Roles' not in _M_ode.sys.__dict__:
    _M_ode.sys.Roles = Ice.createTempClass()
    class Roles(Ice.Object):
        """
        Server-constants used for determining particular groups and
        users.
        """
        def __init__(self, rootId=0, rootName='', systemGroupId=0, systemGroupName='', userGroupId=0, userGroupName='', guestId=0, guestName='', guestGroupId=0, guestGroupName=''):
            self.rootId = rootId
            self.rootName = rootName
            self.systemGroupId = systemGroupId
            self.systemGroupName = systemGroupName
            self.userGroupId = userGroupId
            self.userGroupName = userGroupName
            self.guestId = guestId
            self.guestName = guestName
            self.guestGroupId = guestGroupId
            self.guestGroupName = guestGroupName

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::sys::Roles')

        def ice_id(self, current=None):
            return '::ode::sys::Roles'

        def ice_staticId():
            return '::ode::sys::Roles'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.sys._t_Roles)

        __repr__ = __str__

    _M_ode.sys.RolesPrx = Ice.createTempClass()
    class RolesPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.sys.RolesPrx.ice_checkedCast(proxy, '::ode::sys::Roles', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.sys.RolesPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::sys::Roles'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.sys._t_RolesPrx = IcePy.defineProxy('::ode::sys::Roles', RolesPrx)

    _M_ode.sys._t_Roles = IcePy.defineClass('::ode::sys::Roles', Roles, -1, (), False, False, None, (), (
        ('rootId', (), IcePy._t_long, False, 0),
        ('rootName', (), IcePy._t_string, False, 0),
        ('systemGroupId', (), IcePy._t_long, False, 0),
        ('systemGroupName', (), IcePy._t_string, False, 0),
        ('userGroupId', (), IcePy._t_long, False, 0),
        ('userGroupName', (), IcePy._t_string, False, 0),
        ('guestId', (), IcePy._t_long, False, 0),
        ('guestName', (), IcePy._t_string, False, 0),
        ('guestGroupId', (), IcePy._t_long, False, 0),
        ('guestGroupName', (), IcePy._t_string, False, 0)
    ))
    Roles._ice_type = _M_ode.sys._t_Roles

    _M_ode.sys.Roles = Roles
    del Roles

    _M_ode.sys.RolesPrx = RolesPrx
    del RolesPrx

# End of module ode.sys

__name__ = 'ode'

# End of module ode
