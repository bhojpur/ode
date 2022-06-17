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

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'ExternalInfo' not in _M_ode.model.__dict__:
    _M_ode.model.ExternalInfo = Ice.createTempClass()
    class ExternalInfo(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _entityId=None, _entityType=None, _lsid=None, _uuid=None):
            if Ice.getType(self) == _M_ode.model.ExternalInfo:
                raise RuntimeError('ode.model.ExternalInfo is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._entityId = _entityId
            self._entityType = _entityType
            self._lsid = _lsid
            self._uuid = _uuid

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::ExternalInfo', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::ExternalInfo'

        def ice_staticId():
            return '::ode::model::ExternalInfo'
        ice_staticId = staticmethod(ice_staticId)

        def getEntityId(self, current=None):
            pass

        def setEntityId(self, theEntityId, current=None):
            pass

        def getEntityType(self, current=None):
            pass

        def setEntityType(self, theEntityType, current=None):
            pass

        def getLsid(self, current=None):
            pass

        def setLsid(self, theLsid, current=None):
            pass

        def getUuid(self, current=None):
            pass

        def setUuid(self, theUuid, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_ExternalInfo)

        __repr__ = __str__

    _M_ode.model.ExternalInfoPrx = Ice.createTempClass()
    class ExternalInfoPrx(_M_ode.model.IObjectPrx):

        def getEntityId(self, _ctx=None):
            return _M_ode.model.ExternalInfo._op_getEntityId.invoke(self, ((), _ctx))

        def begin_getEntityId(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ExternalInfo._op_getEntityId.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEntityId(self, _r):
            return _M_ode.model.ExternalInfo._op_getEntityId.end(self, _r)

        def setEntityId(self, theEntityId, _ctx=None):
            return _M_ode.model.ExternalInfo._op_setEntityId.invoke(self, ((theEntityId, ), _ctx))

        def begin_setEntityId(self, theEntityId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ExternalInfo._op_setEntityId.begin(self, ((theEntityId, ), _response, _ex, _sent, _ctx))

        def end_setEntityId(self, _r):
            return _M_ode.model.ExternalInfo._op_setEntityId.end(self, _r)

        def getEntityType(self, _ctx=None):
            return _M_ode.model.ExternalInfo._op_getEntityType.invoke(self, ((), _ctx))

        def begin_getEntityType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ExternalInfo._op_getEntityType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEntityType(self, _r):
            return _M_ode.model.ExternalInfo._op_getEntityType.end(self, _r)

        def setEntityType(self, theEntityType, _ctx=None):
            return _M_ode.model.ExternalInfo._op_setEntityType.invoke(self, ((theEntityType, ), _ctx))

        def begin_setEntityType(self, theEntityType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ExternalInfo._op_setEntityType.begin(self, ((theEntityType, ), _response, _ex, _sent, _ctx))

        def end_setEntityType(self, _r):
            return _M_ode.model.ExternalInfo._op_setEntityType.end(self, _r)

        def getLsid(self, _ctx=None):
            return _M_ode.model.ExternalInfo._op_getLsid.invoke(self, ((), _ctx))

        def begin_getLsid(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ExternalInfo._op_getLsid.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLsid(self, _r):
            return _M_ode.model.ExternalInfo._op_getLsid.end(self, _r)

        def setLsid(self, theLsid, _ctx=None):
            return _M_ode.model.ExternalInfo._op_setLsid.invoke(self, ((theLsid, ), _ctx))

        def begin_setLsid(self, theLsid, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ExternalInfo._op_setLsid.begin(self, ((theLsid, ), _response, _ex, _sent, _ctx))

        def end_setLsid(self, _r):
            return _M_ode.model.ExternalInfo._op_setLsid.end(self, _r)

        def getUuid(self, _ctx=None):
            return _M_ode.model.ExternalInfo._op_getUuid.invoke(self, ((), _ctx))

        def begin_getUuid(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ExternalInfo._op_getUuid.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getUuid(self, _r):
            return _M_ode.model.ExternalInfo._op_getUuid.end(self, _r)

        def setUuid(self, theUuid, _ctx=None):
            return _M_ode.model.ExternalInfo._op_setUuid.invoke(self, ((theUuid, ), _ctx))

        def begin_setUuid(self, theUuid, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ExternalInfo._op_setUuid.begin(self, ((theUuid, ), _response, _ex, _sent, _ctx))

        def end_setUuid(self, _r):
            return _M_ode.model.ExternalInfo._op_setUuid.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ExternalInfoPrx.ice_checkedCast(proxy, '::ode::model::ExternalInfo', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ExternalInfoPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ExternalInfo'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ExternalInfoPrx = IcePy.defineProxy('::ode::model::ExternalInfo', ExternalInfoPrx)

    _M_ode.model._t_ExternalInfo = IcePy.declareClass('::ode::model::ExternalInfo')

    _M_ode.model._t_ExternalInfo = IcePy.defineClass('::ode::model::ExternalInfo', ExternalInfo, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_entityId', (), _M_ode._t_RLong, False, 0),
        ('_entityType', (), _M_ode._t_RString, False, 0),
        ('_lsid', (), _M_ode._t_RString, False, 0),
        ('_uuid', (), _M_ode._t_RString, False, 0)
    ))
    ExternalInfo._ice_type = _M_ode.model._t_ExternalInfo

    ExternalInfo._op_getEntityId = IcePy.Operation('getEntityId', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RLong, False, 0), ())
    ExternalInfo._op_setEntityId = IcePy.Operation('setEntityId', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RLong, False, 0),), (), None, ())
    ExternalInfo._op_getEntityType = IcePy.Operation('getEntityType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    ExternalInfo._op_setEntityType = IcePy.Operation('setEntityType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    ExternalInfo._op_getLsid = IcePy.Operation('getLsid', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    ExternalInfo._op_setLsid = IcePy.Operation('setLsid', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    ExternalInfo._op_getUuid = IcePy.Operation('getUuid', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    ExternalInfo._op_setUuid = IcePy.Operation('setUuid', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.ExternalInfo = ExternalInfo
    del ExternalInfo

    _M_ode.model.ExternalInfoPrx = ExternalInfoPrx
    del ExternalInfoPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
