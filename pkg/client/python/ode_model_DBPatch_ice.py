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

if 'DBPatch' not in _M_ode.model.__dict__:
    _M_ode.model.DBPatch = Ice.createTempClass()
    class DBPatch(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _currentVersion=None, _currentPatch=None, _previousVersion=None, _previousPatch=None, _finished=None, _message=None):
            if Ice.getType(self) == _M_ode.model.DBPatch:
                raise RuntimeError('ode.model.DBPatch is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._currentVersion = _currentVersion
            self._currentPatch = _currentPatch
            self._previousVersion = _previousVersion
            self._previousPatch = _previousPatch
            self._finished = _finished
            self._message = _message

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::DBPatch', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::DBPatch'

        def ice_staticId():
            return '::ode::model::DBPatch'
        ice_staticId = staticmethod(ice_staticId)

        def getCurrentVersion(self, current=None):
            pass

        def setCurrentVersion(self, theCurrentVersion, current=None):
            pass

        def getCurrentPatch(self, current=None):
            pass

        def setCurrentPatch(self, theCurrentPatch, current=None):
            pass

        def getPreviousVersion(self, current=None):
            pass

        def setPreviousVersion(self, thePreviousVersion, current=None):
            pass

        def getPreviousPatch(self, current=None):
            pass

        def setPreviousPatch(self, thePreviousPatch, current=None):
            pass

        def getFinished(self, current=None):
            pass

        def setFinished(self, theFinished, current=None):
            pass

        def getMessage(self, current=None):
            pass

        def setMessage(self, theMessage, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_DBPatch)

        __repr__ = __str__

    _M_ode.model.DBPatchPrx = Ice.createTempClass()
    class DBPatchPrx(_M_ode.model.IObjectPrx):

        def getCurrentVersion(self, _ctx=None):
            return _M_ode.model.DBPatch._op_getCurrentVersion.invoke(self, ((), _ctx))

        def begin_getCurrentVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_getCurrentVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCurrentVersion(self, _r):
            return _M_ode.model.DBPatch._op_getCurrentVersion.end(self, _r)

        def setCurrentVersion(self, theCurrentVersion, _ctx=None):
            return _M_ode.model.DBPatch._op_setCurrentVersion.invoke(self, ((theCurrentVersion, ), _ctx))

        def begin_setCurrentVersion(self, theCurrentVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_setCurrentVersion.begin(self, ((theCurrentVersion, ), _response, _ex, _sent, _ctx))

        def end_setCurrentVersion(self, _r):
            return _M_ode.model.DBPatch._op_setCurrentVersion.end(self, _r)

        def getCurrentPatch(self, _ctx=None):
            return _M_ode.model.DBPatch._op_getCurrentPatch.invoke(self, ((), _ctx))

        def begin_getCurrentPatch(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_getCurrentPatch.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCurrentPatch(self, _r):
            return _M_ode.model.DBPatch._op_getCurrentPatch.end(self, _r)

        def setCurrentPatch(self, theCurrentPatch, _ctx=None):
            return _M_ode.model.DBPatch._op_setCurrentPatch.invoke(self, ((theCurrentPatch, ), _ctx))

        def begin_setCurrentPatch(self, theCurrentPatch, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_setCurrentPatch.begin(self, ((theCurrentPatch, ), _response, _ex, _sent, _ctx))

        def end_setCurrentPatch(self, _r):
            return _M_ode.model.DBPatch._op_setCurrentPatch.end(self, _r)

        def getPreviousVersion(self, _ctx=None):
            return _M_ode.model.DBPatch._op_getPreviousVersion.invoke(self, ((), _ctx))

        def begin_getPreviousVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_getPreviousVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPreviousVersion(self, _r):
            return _M_ode.model.DBPatch._op_getPreviousVersion.end(self, _r)

        def setPreviousVersion(self, thePreviousVersion, _ctx=None):
            return _M_ode.model.DBPatch._op_setPreviousVersion.invoke(self, ((thePreviousVersion, ), _ctx))

        def begin_setPreviousVersion(self, thePreviousVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_setPreviousVersion.begin(self, ((thePreviousVersion, ), _response, _ex, _sent, _ctx))

        def end_setPreviousVersion(self, _r):
            return _M_ode.model.DBPatch._op_setPreviousVersion.end(self, _r)

        def getPreviousPatch(self, _ctx=None):
            return _M_ode.model.DBPatch._op_getPreviousPatch.invoke(self, ((), _ctx))

        def begin_getPreviousPatch(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_getPreviousPatch.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPreviousPatch(self, _r):
            return _M_ode.model.DBPatch._op_getPreviousPatch.end(self, _r)

        def setPreviousPatch(self, thePreviousPatch, _ctx=None):
            return _M_ode.model.DBPatch._op_setPreviousPatch.invoke(self, ((thePreviousPatch, ), _ctx))

        def begin_setPreviousPatch(self, thePreviousPatch, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_setPreviousPatch.begin(self, ((thePreviousPatch, ), _response, _ex, _sent, _ctx))

        def end_setPreviousPatch(self, _r):
            return _M_ode.model.DBPatch._op_setPreviousPatch.end(self, _r)

        def getFinished(self, _ctx=None):
            return _M_ode.model.DBPatch._op_getFinished.invoke(self, ((), _ctx))

        def begin_getFinished(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_getFinished.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFinished(self, _r):
            return _M_ode.model.DBPatch._op_getFinished.end(self, _r)

        def setFinished(self, theFinished, _ctx=None):
            return _M_ode.model.DBPatch._op_setFinished.invoke(self, ((theFinished, ), _ctx))

        def begin_setFinished(self, theFinished, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_setFinished.begin(self, ((theFinished, ), _response, _ex, _sent, _ctx))

        def end_setFinished(self, _r):
            return _M_ode.model.DBPatch._op_setFinished.end(self, _r)

        def getMessage(self, _ctx=None):
            return _M_ode.model.DBPatch._op_getMessage.invoke(self, ((), _ctx))

        def begin_getMessage(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_getMessage.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMessage(self, _r):
            return _M_ode.model.DBPatch._op_getMessage.end(self, _r)

        def setMessage(self, theMessage, _ctx=None):
            return _M_ode.model.DBPatch._op_setMessage.invoke(self, ((theMessage, ), _ctx))

        def begin_setMessage(self, theMessage, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.DBPatch._op_setMessage.begin(self, ((theMessage, ), _response, _ex, _sent, _ctx))

        def end_setMessage(self, _r):
            return _M_ode.model.DBPatch._op_setMessage.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.DBPatchPrx.ice_checkedCast(proxy, '::ode::model::DBPatch', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.DBPatchPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::DBPatch'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_DBPatchPrx = IcePy.defineProxy('::ode::model::DBPatch', DBPatchPrx)

    _M_ode.model._t_DBPatch = IcePy.declareClass('::ode::model::DBPatch')

    _M_ode.model._t_DBPatch = IcePy.defineClass('::ode::model::DBPatch', DBPatch, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_currentVersion', (), _M_ode._t_RString, False, 0),
        ('_currentPatch', (), _M_ode._t_RInt, False, 0),
        ('_previousVersion', (), _M_ode._t_RString, False, 0),
        ('_previousPatch', (), _M_ode._t_RInt, False, 0),
        ('_finished', (), _M_ode._t_RTime, False, 0),
        ('_message', (), _M_ode._t_RString, False, 0)
    ))
    DBPatch._ice_type = _M_ode.model._t_DBPatch

    DBPatch._op_getCurrentVersion = IcePy.Operation('getCurrentVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    DBPatch._op_setCurrentVersion = IcePy.Operation('setCurrentVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    DBPatch._op_getCurrentPatch = IcePy.Operation('getCurrentPatch', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    DBPatch._op_setCurrentPatch = IcePy.Operation('setCurrentPatch', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    DBPatch._op_getPreviousVersion = IcePy.Operation('getPreviousVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    DBPatch._op_setPreviousVersion = IcePy.Operation('setPreviousVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    DBPatch._op_getPreviousPatch = IcePy.Operation('getPreviousPatch', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    DBPatch._op_setPreviousPatch = IcePy.Operation('setPreviousPatch', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    DBPatch._op_getFinished = IcePy.Operation('getFinished', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    DBPatch._op_setFinished = IcePy.Operation('setFinished', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    DBPatch._op_getMessage = IcePy.Operation('getMessage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    DBPatch._op_setMessage = IcePy.Operation('setMessage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.DBPatch = DBPatch
    del DBPatch

    _M_ode.model.DBPatchPrx = DBPatchPrx
    del DBPatchPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
