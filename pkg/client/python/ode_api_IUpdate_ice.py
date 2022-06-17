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
import ode_ServicesF_ice
import ode_Collections_ice

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

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'IUpdate' not in _M_ode.api.__dict__:
    _M_ode.api.IUpdate = Ice.createTempClass()
    class IUpdate(_M_ode.api.ServiceInterface):
        """
        Provides methods for directly updating object graphs. IUpdate is
        the lowest level (level-1) interface which may make changes
        (INSERT, UPDATE, DELETE) to the database. All other methods of
        changing the database may leave it in an inconsistent state.
        All the save* methods act recursively on the entire object graph,
        replacing placeholders and details where necessary, and then
        merging the final graph.
        This means that the objects that are passed into IUpdate.save*
        methods are copied over to new instances which are then returned.
        The original objects should be discarded.
        {@code saveAndReturnIds} behaves slightly differently in that
        it does not handle object modifications. The graph of
        objects passed in can consist ONLY if either newly created
        objects without ids or of unloaded objects with ids. Note:
        The ids of the saved values may not be in order. This is caused by
        persistence-by-transitivity. Hibernate may detect an item later in
        the array if they are interconnected and therefore choose to save
        it first.
        All methods throw ode.ValidationException if the input
        objects do not pass validation, and
        ode.OptimisticLockException if the version of a given has
        already been incremented.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IUpdate:
                raise RuntimeError('ode.api.IUpdate is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IUpdate', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IUpdate'

        def ice_staticId():
            return '::ode::api::IUpdate'
        ice_staticId = staticmethod(ice_staticId)

        def saveObject_async(self, _cb, obj, current=None):
            pass

        def saveCollection_async(self, _cb, objs, current=None):
            pass

        def saveAndReturnObject_async(self, _cb, obj, current=None):
            pass

        def saveArray_async(self, _cb, graph, current=None):
            pass

        def saveAndReturnArray_async(self, _cb, graph, current=None):
            pass

        def saveAndReturnIds_async(self, _cb, graph, current=None):
            pass

        def deleteObject_async(self, _cb, row, current=None):
            pass

        def indexObject_async(self, _cb, row, current=None):
            """
            Initiates full-text indexing for the given object. This may
            have to wait
            for the current {@code FullTextThread} to finish.
            Can only be executed by an admin. Other users must wait for
            the background Thread to complete.
            Arguments:
            _cb -- The asynchronous callback object.
            row -- a persistent ode.model.IObject to be deleted
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the object does not exist or is nul
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IUpdate)

        __repr__ = __str__

    _M_ode.api.IUpdatePrx = Ice.createTempClass()
    class IUpdatePrx(_M_ode.api.ServiceInterfacePrx):

        def saveObject(self, obj, _ctx=None):
            return _M_ode.api.IUpdate._op_saveObject.invoke(self, ((obj, ), _ctx))

        def begin_saveObject(self, obj, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IUpdate._op_saveObject.begin(self, ((obj, ), _response, _ex, _sent, _ctx))

        def end_saveObject(self, _r):
            return _M_ode.api.IUpdate._op_saveObject.end(self, _r)

        def saveCollection(self, objs, _ctx=None):
            return _M_ode.api.IUpdate._op_saveCollection.invoke(self, ((objs, ), _ctx))

        def begin_saveCollection(self, objs, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IUpdate._op_saveCollection.begin(self, ((objs, ), _response, _ex, _sent, _ctx))

        def end_saveCollection(self, _r):
            return _M_ode.api.IUpdate._op_saveCollection.end(self, _r)

        def saveAndReturnObject(self, obj, _ctx=None):
            return _M_ode.api.IUpdate._op_saveAndReturnObject.invoke(self, ((obj, ), _ctx))

        def begin_saveAndReturnObject(self, obj, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IUpdate._op_saveAndReturnObject.begin(self, ((obj, ), _response, _ex, _sent, _ctx))

        def end_saveAndReturnObject(self, _r):
            return _M_ode.api.IUpdate._op_saveAndReturnObject.end(self, _r)

        def saveArray(self, graph, _ctx=None):
            return _M_ode.api.IUpdate._op_saveArray.invoke(self, ((graph, ), _ctx))

        def begin_saveArray(self, graph, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IUpdate._op_saveArray.begin(self, ((graph, ), _response, _ex, _sent, _ctx))

        def end_saveArray(self, _r):
            return _M_ode.api.IUpdate._op_saveArray.end(self, _r)

        def saveAndReturnArray(self, graph, _ctx=None):
            return _M_ode.api.IUpdate._op_saveAndReturnArray.invoke(self, ((graph, ), _ctx))

        def begin_saveAndReturnArray(self, graph, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IUpdate._op_saveAndReturnArray.begin(self, ((graph, ), _response, _ex, _sent, _ctx))

        def end_saveAndReturnArray(self, _r):
            return _M_ode.api.IUpdate._op_saveAndReturnArray.end(self, _r)

        def saveAndReturnIds(self, graph, _ctx=None):
            return _M_ode.api.IUpdate._op_saveAndReturnIds.invoke(self, ((graph, ), _ctx))

        def begin_saveAndReturnIds(self, graph, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IUpdate._op_saveAndReturnIds.begin(self, ((graph, ), _response, _ex, _sent, _ctx))

        def end_saveAndReturnIds(self, _r):
            return _M_ode.api.IUpdate._op_saveAndReturnIds.end(self, _r)

        def deleteObject(self, row, _ctx=None):
            return _M_ode.api.IUpdate._op_deleteObject.invoke(self, ((row, ), _ctx))

        def begin_deleteObject(self, row, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IUpdate._op_deleteObject.begin(self, ((row, ), _response, _ex, _sent, _ctx))

        def end_deleteObject(self, _r):
            return _M_ode.api.IUpdate._op_deleteObject.end(self, _r)

        """
        Initiates full-text indexing for the given object. This may
        have to wait
        for the current {@code FullTextThread} to finish.
        Can only be executed by an admin. Other users must wait for
        the background Thread to complete.
        Arguments:
        row -- a persistent ode.model.IObject to be deleted
        _ctx -- The request context for the invocation.
        Throws:
        ValidationException -- if the object does not exist or is nul
        """
        def indexObject(self, row, _ctx=None):
            return _M_ode.api.IUpdate._op_indexObject.invoke(self, ((row, ), _ctx))

        """
        Initiates full-text indexing for the given object. This may
        have to wait
        for the current {@code FullTextThread} to finish.
        Can only be executed by an admin. Other users must wait for
        the background Thread to complete.
        Arguments:
        row -- a persistent ode.model.IObject to be deleted
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_indexObject(self, row, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IUpdate._op_indexObject.begin(self, ((row, ), _response, _ex, _sent, _ctx))

        """
        Initiates full-text indexing for the given object. This may
        have to wait
        for the current {@code FullTextThread} to finish.
        Can only be executed by an admin. Other users must wait for
        the background Thread to complete.
        Arguments:
        row -- a persistent ode.model.IObject to be deleted
        Throws:
        ValidationException -- if the object does not exist or is nul
        """
        def end_indexObject(self, _r):
            return _M_ode.api.IUpdate._op_indexObject.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IUpdatePrx.ice_checkedCast(proxy, '::ode::api::IUpdate', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IUpdatePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IUpdate'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IUpdatePrx = IcePy.defineProxy('::ode::api::IUpdate', IUpdatePrx)

    _M_ode.api._t_IUpdate = IcePy.defineClass('::ode::api::IUpdate', IUpdate, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IUpdate._ice_type = _M_ode.api._t_IUpdate

    IUpdate._op_saveObject = IcePy.Operation('saveObject', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), None, (_M_ode._t_ServerError,))
    IUpdate._op_saveCollection = IcePy.Operation('saveCollection', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0),), (), None, (_M_ode._t_ServerError,))
    IUpdate._op_saveAndReturnObject = IcePy.Operation('saveAndReturnObject', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    IUpdate._op_saveArray = IcePy.Operation('saveArray', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0),), (), None, (_M_ode._t_ServerError,))
    IUpdate._op_saveAndReturnArray = IcePy.Operation('saveAndReturnArray', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0),), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IUpdate._op_saveAndReturnIds = IcePy.Operation('saveAndReturnIds', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0),), (), ((), _M_ode.sys._t_LongList, False, 0), (_M_ode._t_ServerError,))
    IUpdate._op_deleteObject = IcePy.Operation('deleteObject', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), None, (_M_ode._t_ServerError,))
    IUpdate._op_deleteObject.deprecate("use ode::cmd::Delete2 instead")
    IUpdate._op_indexObject = IcePy.Operation('indexObject', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), None, (_M_ode._t_ServerError,))

    _M_ode.api.IUpdate = IUpdate
    del IUpdate

    _M_ode.api.IUpdatePrx = IUpdatePrx
    del IUpdatePrx

if 'Save' not in _M_ode.api.__dict__:
    _M_ode.api.Save = Ice.createTempClass()
    class Save(_M_ode.cmd.Request):
        def __init__(self, obj=None):
            _M_ode.cmd.Request.__init__(self)
            self.obj = obj

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::Save', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::api::Save'

        def ice_staticId():
            return '::ode::api::Save'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_Save)

        __repr__ = __str__

    _M_ode.api.SavePrx = Ice.createTempClass()
    class SavePrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.SavePrx.ice_checkedCast(proxy, '::ode::api::Save', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.SavePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::Save'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_SavePrx = IcePy.defineProxy('::ode::api::Save', SavePrx)

    _M_ode.api._t_Save = IcePy.declareClass('::ode::api::Save')

    _M_ode.api._t_Save = IcePy.defineClass('::ode::api::Save', Save, -1, (), False, False, _M_ode.cmd._t_Request, (), (('obj', (), _M_ode.model._t_IObject, False, 0),))
    Save._ice_type = _M_ode.api._t_Save

    _M_ode.api.Save = Save
    del Save

    _M_ode.api.SavePrx = SavePrx
    del SavePrx

if 'SaveRsp' not in _M_ode.api.__dict__:
    _M_ode.api.SaveRsp = Ice.createTempClass()
    class SaveRsp(_M_ode.cmd.Response):
        def __init__(self, obj=None):
            _M_ode.cmd.Response.__init__(self)
            self.obj = obj

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::SaveRsp', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::api::SaveRsp'

        def ice_staticId():
            return '::ode::api::SaveRsp'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_SaveRsp)

        __repr__ = __str__

    _M_ode.api.SaveRspPrx = Ice.createTempClass()
    class SaveRspPrx(_M_ode.cmd.ResponsePrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.SaveRspPrx.ice_checkedCast(proxy, '::ode::api::SaveRsp', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.SaveRspPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::SaveRsp'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_SaveRspPrx = IcePy.defineProxy('::ode::api::SaveRsp', SaveRspPrx)

    _M_ode.api._t_SaveRsp = IcePy.declareClass('::ode::api::SaveRsp')

    _M_ode.api._t_SaveRsp = IcePy.defineClass('::ode::api::SaveRsp', SaveRsp, -1, (), False, False, _M_ode.cmd._t_Response, (), (('obj', (), _M_ode.model._t_IObject, False, 0),))
    SaveRsp._ice_type = _M_ode.api._t_SaveRsp

    _M_ode.api.SaveRsp = SaveRsp
    del SaveRsp

    _M_ode.api.SaveRspPrx = SaveRspPrx
    del SaveRspPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
