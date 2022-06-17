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
import ode_model_IObject_ice
import ode_RTypes_ice
import ode_model_RTypes_ice
import ode_System_ice
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

# Start of module ode.model
__name__ = 'ode.model'

if 'Event' not in _M_ode.model.__dict__:
    _M_ode.model._t_Event = IcePy.declareClass('::ode::model::Event')
    _M_ode.model._t_EventPrx = IcePy.declareProxy('::ode::model::Event')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'EventLog' not in _M_ode.model.__dict__:
    _M_ode.model.EventLog = Ice.createTempClass()
    class EventLog(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _entityId=None, _entityType=None, _action=None, _event=None):
            if Ice.getType(self) == _M_ode.model.EventLog:
                raise RuntimeError('ode.model.EventLog is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._entityId = _entityId
            self._entityType = _entityType
            self._action = _action
            self._event = _event

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::EventLog', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::EventLog'

        def ice_staticId():
            return '::ode::model::EventLog'
        ice_staticId = staticmethod(ice_staticId)

        def getEntityId(self, current=None):
            pass

        def setEntityId(self, theEntityId, current=None):
            pass

        def getEntityType(self, current=None):
            pass

        def setEntityType(self, theEntityType, current=None):
            pass

        def getAction(self, current=None):
            pass

        def setAction(self, theAction, current=None):
            pass

        def getEvent(self, current=None):
            pass

        def setEvent(self, theEvent, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_EventLog)

        __repr__ = __str__

    _M_ode.model.EventLogPrx = Ice.createTempClass()
    class EventLogPrx(_M_ode.model.IObjectPrx):

        def getEntityId(self, _ctx=None):
            return _M_ode.model.EventLog._op_getEntityId.invoke(self, ((), _ctx))

        def begin_getEntityId(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.EventLog._op_getEntityId.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEntityId(self, _r):
            return _M_ode.model.EventLog._op_getEntityId.end(self, _r)

        def setEntityId(self, theEntityId, _ctx=None):
            return _M_ode.model.EventLog._op_setEntityId.invoke(self, ((theEntityId, ), _ctx))

        def begin_setEntityId(self, theEntityId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.EventLog._op_setEntityId.begin(self, ((theEntityId, ), _response, _ex, _sent, _ctx))

        def end_setEntityId(self, _r):
            return _M_ode.model.EventLog._op_setEntityId.end(self, _r)

        def getEntityType(self, _ctx=None):
            return _M_ode.model.EventLog._op_getEntityType.invoke(self, ((), _ctx))

        def begin_getEntityType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.EventLog._op_getEntityType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEntityType(self, _r):
            return _M_ode.model.EventLog._op_getEntityType.end(self, _r)

        def setEntityType(self, theEntityType, _ctx=None):
            return _M_ode.model.EventLog._op_setEntityType.invoke(self, ((theEntityType, ), _ctx))

        def begin_setEntityType(self, theEntityType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.EventLog._op_setEntityType.begin(self, ((theEntityType, ), _response, _ex, _sent, _ctx))

        def end_setEntityType(self, _r):
            return _M_ode.model.EventLog._op_setEntityType.end(self, _r)

        def getAction(self, _ctx=None):
            return _M_ode.model.EventLog._op_getAction.invoke(self, ((), _ctx))

        def begin_getAction(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.EventLog._op_getAction.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAction(self, _r):
            return _M_ode.model.EventLog._op_getAction.end(self, _r)

        def setAction(self, theAction, _ctx=None):
            return _M_ode.model.EventLog._op_setAction.invoke(self, ((theAction, ), _ctx))

        def begin_setAction(self, theAction, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.EventLog._op_setAction.begin(self, ((theAction, ), _response, _ex, _sent, _ctx))

        def end_setAction(self, _r):
            return _M_ode.model.EventLog._op_setAction.end(self, _r)

        def getEvent(self, _ctx=None):
            return _M_ode.model.EventLog._op_getEvent.invoke(self, ((), _ctx))

        def begin_getEvent(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.EventLog._op_getEvent.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEvent(self, _r):
            return _M_ode.model.EventLog._op_getEvent.end(self, _r)

        def setEvent(self, theEvent, _ctx=None):
            return _M_ode.model.EventLog._op_setEvent.invoke(self, ((theEvent, ), _ctx))

        def begin_setEvent(self, theEvent, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.EventLog._op_setEvent.begin(self, ((theEvent, ), _response, _ex, _sent, _ctx))

        def end_setEvent(self, _r):
            return _M_ode.model.EventLog._op_setEvent.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.EventLogPrx.ice_checkedCast(proxy, '::ode::model::EventLog', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.EventLogPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::EventLog'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_EventLogPrx = IcePy.defineProxy('::ode::model::EventLog', EventLogPrx)

    _M_ode.model._t_EventLog = IcePy.declareClass('::ode::model::EventLog')

    _M_ode.model._t_EventLog = IcePy.defineClass('::ode::model::EventLog', EventLog, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_entityId', (), _M_ode._t_RLong, False, 0),
        ('_entityType', (), _M_ode._t_RString, False, 0),
        ('_action', (), _M_ode._t_RString, False, 0),
        ('_event', (), _M_ode.model._t_Event, False, 0)
    ))
    EventLog._ice_type = _M_ode.model._t_EventLog

    EventLog._op_getEntityId = IcePy.Operation('getEntityId', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RLong, False, 0), ())
    EventLog._op_setEntityId = IcePy.Operation('setEntityId', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RLong, False, 0),), (), None, ())
    EventLog._op_getEntityType = IcePy.Operation('getEntityType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    EventLog._op_setEntityType = IcePy.Operation('setEntityType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    EventLog._op_getAction = IcePy.Operation('getAction', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    EventLog._op_setAction = IcePy.Operation('setAction', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    EventLog._op_getEvent = IcePy.Operation('getEvent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Event, False, 0), ())
    EventLog._op_setEvent = IcePy.Operation('setEvent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Event, False, 0),), (), None, ())

    _M_ode.model.EventLog = EventLog
    del EventLog

    _M_ode.model.EventLogPrx = EventLogPrx
    del EventLogPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
