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
import ode_System_ice
import Ice_Current_ice

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

# Start of module ode.model
__name__ = 'ode.model'

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model.Details = Ice.createTempClass()
    class Details(Ice.Object):
        """
        Embedded component of every ode.server type. Since this is
        not an IObject subclass, no attempt is made to hide the state
        of this object, since it cannot be ""unloaded"".
        Members:
        owner -- 
        group -- 
        creationEvent -- 
        updateEvent -- 
        permissions -- 
        externalInfo -- 
        call -- Context which was active during the call which
        returned this object. This context is set as
        the last (optional) argument of any remote
        Ice invocation. This is used to change the
        user, group, share, etc. of the current session.
        event -- Context which would have been returned by a
        simultaneous call to {@code ode.api.IAdmin.getEventContext}
        while this object was being loaded.
        """
        def __init__(self, _owner=None, _group=None, _creationEvent=None, _updateEvent=None, _permissions=None, _externalInfo=None, _call=None, _event=None):
            if Ice.getType(self) == _M_ode.model.Details:
                raise RuntimeError('ode.model.Details is an abstract class')
            self._owner = _owner
            self._group = _group
            self._creationEvent = _creationEvent
            self._updateEvent = _updateEvent
            self._permissions = _permissions
            self._externalInfo = _externalInfo
            self._call = _call
            self._event = _event

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Details')

        def ice_id(self, current=None):
            return '::ode::model::Details'

        def ice_staticId():
            return '::ode::model::Details'
        ice_staticId = staticmethod(ice_staticId)

        def getOwner(self, current=None):
            pass

        def setOwner(self, theOwner, current=None):
            pass

        def getGroup(self, current=None):
            pass

        def setGroup(self, theGroup, current=None):
            pass

        def getCreationEvent(self, current=None):
            pass

        def setCreationEvent(self, theCreationEvent, current=None):
            pass

        def getUpdateEvent(self, current=None):
            pass

        def setUpdateEvent(self, theUpdateEvent, current=None):
            pass

        def getPermissions(self, current=None):
            pass

        def setPermissions(self, thePermissions, current=None):
            pass

        def getExternalInfo(self, current=None):
            pass

        def setExternalInfo(self, theExternalInfo, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Details)

        __repr__ = __str__

    _M_ode.model.DetailsPrx = Ice.createTempClass()
    class DetailsPrx(Ice.ObjectPrx):

        def getOwner(self, _ctx=None):
            return _M_ode.model.Details._op_getOwner.invoke(self, ((), _ctx))

        def begin_getOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_getOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getOwner(self, _r):
            return _M_ode.model.Details._op_getOwner.end(self, _r)

        def setOwner(self, theOwner, _ctx=None):
            return _M_ode.model.Details._op_setOwner.invoke(self, ((theOwner, ), _ctx))

        def begin_setOwner(self, theOwner, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_setOwner.begin(self, ((theOwner, ), _response, _ex, _sent, _ctx))

        def end_setOwner(self, _r):
            return _M_ode.model.Details._op_setOwner.end(self, _r)

        def getGroup(self, _ctx=None):
            return _M_ode.model.Details._op_getGroup.invoke(self, ((), _ctx))

        def begin_getGroup(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_getGroup.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getGroup(self, _r):
            return _M_ode.model.Details._op_getGroup.end(self, _r)

        def setGroup(self, theGroup, _ctx=None):
            return _M_ode.model.Details._op_setGroup.invoke(self, ((theGroup, ), _ctx))

        def begin_setGroup(self, theGroup, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_setGroup.begin(self, ((theGroup, ), _response, _ex, _sent, _ctx))

        def end_setGroup(self, _r):
            return _M_ode.model.Details._op_setGroup.end(self, _r)

        def getCreationEvent(self, _ctx=None):
            return _M_ode.model.Details._op_getCreationEvent.invoke(self, ((), _ctx))

        def begin_getCreationEvent(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_getCreationEvent.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCreationEvent(self, _r):
            return _M_ode.model.Details._op_getCreationEvent.end(self, _r)

        def setCreationEvent(self, theCreationEvent, _ctx=None):
            return _M_ode.model.Details._op_setCreationEvent.invoke(self, ((theCreationEvent, ), _ctx))

        def begin_setCreationEvent(self, theCreationEvent, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_setCreationEvent.begin(self, ((theCreationEvent, ), _response, _ex, _sent, _ctx))

        def end_setCreationEvent(self, _r):
            return _M_ode.model.Details._op_setCreationEvent.end(self, _r)

        def getUpdateEvent(self, _ctx=None):
            return _M_ode.model.Details._op_getUpdateEvent.invoke(self, ((), _ctx))

        def begin_getUpdateEvent(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_getUpdateEvent.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getUpdateEvent(self, _r):
            return _M_ode.model.Details._op_getUpdateEvent.end(self, _r)

        def setUpdateEvent(self, theUpdateEvent, _ctx=None):
            return _M_ode.model.Details._op_setUpdateEvent.invoke(self, ((theUpdateEvent, ), _ctx))

        def begin_setUpdateEvent(self, theUpdateEvent, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_setUpdateEvent.begin(self, ((theUpdateEvent, ), _response, _ex, _sent, _ctx))

        def end_setUpdateEvent(self, _r):
            return _M_ode.model.Details._op_setUpdateEvent.end(self, _r)

        def getPermissions(self, _ctx=None):
            return _M_ode.model.Details._op_getPermissions.invoke(self, ((), _ctx))

        def begin_getPermissions(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_getPermissions.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPermissions(self, _r):
            return _M_ode.model.Details._op_getPermissions.end(self, _r)

        def setPermissions(self, thePermissions, _ctx=None):
            return _M_ode.model.Details._op_setPermissions.invoke(self, ((thePermissions, ), _ctx))

        def begin_setPermissions(self, thePermissions, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_setPermissions.begin(self, ((thePermissions, ), _response, _ex, _sent, _ctx))

        def end_setPermissions(self, _r):
            return _M_ode.model.Details._op_setPermissions.end(self, _r)

        def getExternalInfo(self, _ctx=None):
            return _M_ode.model.Details._op_getExternalInfo.invoke(self, ((), _ctx))

        def begin_getExternalInfo(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_getExternalInfo.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getExternalInfo(self, _r):
            return _M_ode.model.Details._op_getExternalInfo.end(self, _r)

        def setExternalInfo(self, theExternalInfo, _ctx=None):
            return _M_ode.model.Details._op_setExternalInfo.invoke(self, ((theExternalInfo, ), _ctx))

        def begin_setExternalInfo(self, theExternalInfo, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Details._op_setExternalInfo.begin(self, ((theExternalInfo, ), _response, _ex, _sent, _ctx))

        def end_setExternalInfo(self, _r):
            return _M_ode.model.Details._op_setExternalInfo.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.DetailsPrx.ice_checkedCast(proxy, '::ode::model::Details', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.DetailsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Details'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_DetailsPrx = IcePy.defineProxy('::ode::model::Details', DetailsPrx)

    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')

    _M_ode.model._t_Details = IcePy.defineClass('::ode::model::Details', Details, -1, (), True, False, None, (), (
        ('_owner', (), _M_ode.model._t_Experimenter, False, 0),
        ('_group', (), _M_ode.model._t_ExperimenterGroup, False, 0),
        ('_creationEvent', (), _M_ode.model._t_Event, False, 0),
        ('_updateEvent', (), _M_ode.model._t_Event, False, 0),
        ('_permissions', (), _M_ode.model._t_Permissions, False, 0),
        ('_externalInfo', (), _M_ode.model._t_ExternalInfo, False, 0),
        ('_call', (), _M_Ice._t_Context, False, 0),
        ('_event', (), _M_ode.sys._t_EventContext, False, 0)
    ))
    Details._ice_type = _M_ode.model._t_Details

    Details._op_getOwner = IcePy.Operation('getOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Experimenter, False, 0), ())
    Details._op_setOwner = IcePy.Operation('setOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), None, ())
    Details._op_getGroup = IcePy.Operation('getGroup', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ExperimenterGroup, False, 0), ())
    Details._op_setGroup = IcePy.Operation('setGroup', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0),), (), None, ())
    Details._op_getCreationEvent = IcePy.Operation('getCreationEvent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Event, False, 0), ())
    Details._op_setCreationEvent = IcePy.Operation('setCreationEvent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Event, False, 0),), (), None, ())
    Details._op_getUpdateEvent = IcePy.Operation('getUpdateEvent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Event, False, 0), ())
    Details._op_setUpdateEvent = IcePy.Operation('setUpdateEvent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Event, False, 0),), (), None, ())
    Details._op_getPermissions = IcePy.Operation('getPermissions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Permissions, False, 0), ())
    Details._op_setPermissions = IcePy.Operation('setPermissions', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Permissions, False, 0),), (), None, ())
    Details._op_getExternalInfo = IcePy.Operation('getExternalInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ExternalInfo, False, 0), ())
    Details._op_setExternalInfo = IcePy.Operation('setExternalInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExternalInfo, False, 0),), (), None, ())

    _M_ode.model.Details = Details
    del Details

    _M_ode.model.DetailsPrx = DetailsPrx
    del DetailsPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
