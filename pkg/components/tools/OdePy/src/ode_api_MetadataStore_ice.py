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
import Ice_BuiltinSequences_ice
import ode_ModelF_ice
import ode_ServicesF_ice
import ode_Scripts_ice
import ode_Repositories_ice

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Included module ode.cmd
_M_ode.cmd = Ice.openModule('ode.cmd')

# Start of module ode
__name__ = 'ode'

# Start of module ode.constants
_M_ode.constants = Ice.openModule('ode.constants')
__name__ = 'ode.constants'

_M_ode.constants.METADATASTORE = "ode.api.MetadataStore"

# End of module ode.constants

__name__ = 'ode'

# Start of module ode.metadatastore
_M_ode.metadatastore = Ice.openModule('ode.metadatastore')
__name__ = 'ode.metadatastore'
_M_ode.metadatastore.__doc__ = """
Types used during import.
"""

if 'IObjectContainer' not in _M_ode.metadatastore.__dict__:
    _M_ode.metadatastore.IObjectContainer = Ice.createTempClass()
    class IObjectContainer(Ice.Object):
        """
        Container-class used by the import mechanism. Passed to
        ode.api.MetadataStore
        """
        def __init__(self, LSID='', indexes=None, sourceObject=None):
            self.LSID = LSID
            self.indexes = indexes
            self.sourceObject = sourceObject

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::metadatastore::IObjectContainer')

        def ice_id(self, current=None):
            return '::ode::metadatastore::IObjectContainer'

        def ice_staticId():
            return '::ode::metadatastore::IObjectContainer'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.metadatastore._t_IObjectContainer)

        __repr__ = __str__

    _M_ode.metadatastore.IObjectContainerPrx = Ice.createTempClass()
    class IObjectContainerPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.metadatastore.IObjectContainerPrx.ice_checkedCast(proxy, '::ode::metadatastore::IObjectContainer', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.metadatastore.IObjectContainerPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::metadatastore::IObjectContainer'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.metadatastore._t_IObjectContainerPrx = IcePy.defineProxy('::ode::metadatastore::IObjectContainer', IObjectContainerPrx)

    _M_ode.metadatastore._t_IObjectContainer = IcePy.declareClass('::ode::metadatastore::IObjectContainer')

    _M_ode.metadatastore._t_IObjectContainer = IcePy.defineClass('::ode::metadatastore::IObjectContainer', IObjectContainer, -1, (), False, False, None, (), (
        ('LSID', (), IcePy._t_string, False, 0),
        ('indexes', (), _M_ode.api._t_StringIntMap, False, 0),
        ('sourceObject', (), _M_ode.model._t_IObject, False, 0)
    ))
    IObjectContainer._ice_type = _M_ode.metadatastore._t_IObjectContainer

    _M_ode.metadatastore.IObjectContainer = IObjectContainer
    del IObjectContainer

    _M_ode.metadatastore.IObjectContainerPrx = IObjectContainerPrx
    del IObjectContainerPrx

if '_t_IObjectContainerArray' not in _M_ode.metadatastore.__dict__:
    _M_ode.metadatastore._t_IObjectContainerArray = IcePy.defineSequence('::ode::metadatastore::IObjectContainerArray', (), _M_ode.metadatastore._t_IObjectContainer)

# End of module ode.metadatastore

__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'MetadataStore' not in _M_ode.api.__dict__:
    _M_ode.api.MetadataStore = Ice.createTempClass()
    class MetadataStore(_M_ode.api.StatefulServiceInterface):
        """
        Server-side interface for import.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.MetadataStore:
                raise RuntimeError('ode.api.MetadataStore is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::MetadataStore', '::ode::api::ServiceInterface', '::ode::api::StatefulServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::MetadataStore'

        def ice_staticId():
            return '::ode::api::MetadataStore'
        ice_staticId = staticmethod(ice_staticId)

        def createRoot_async(self, _cb, current=None):
            pass

        def updateObjects_async(self, _cb, objects, current=None):
            pass

        def updateReferences_async(self, _cb, references, current=None):
            pass

        def saveToDB_async(self, _cb, activity, current=None):
            pass

        def populateMinMax_async(self, _cb, imageChannelGlobalMinMax, current=None):
            pass

        def setPixelsFile_async(self, _cb, pixelsId, file, repo, current=None):
            pass

        def postProcess_async(self, _cb, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_MetadataStore)

        __repr__ = __str__

    _M_ode.api.MetadataStorePrx = Ice.createTempClass()
    class MetadataStorePrx(_M_ode.api.StatefulServiceInterfacePrx):

        def createRoot(self, _ctx=None):
            return _M_ode.api.MetadataStore._op_createRoot.invoke(self, ((), _ctx))

        def begin_createRoot(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.MetadataStore._op_createRoot.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_createRoot(self, _r):
            return _M_ode.api.MetadataStore._op_createRoot.end(self, _r)

        def updateObjects(self, objects, _ctx=None):
            return _M_ode.api.MetadataStore._op_updateObjects.invoke(self, ((objects, ), _ctx))

        def begin_updateObjects(self, objects, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.MetadataStore._op_updateObjects.begin(self, ((objects, ), _response, _ex, _sent, _ctx))

        def end_updateObjects(self, _r):
            return _M_ode.api.MetadataStore._op_updateObjects.end(self, _r)

        def updateReferences(self, references, _ctx=None):
            return _M_ode.api.MetadataStore._op_updateReferences.invoke(self, ((references, ), _ctx))

        def begin_updateReferences(self, references, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.MetadataStore._op_updateReferences.begin(self, ((references, ), _response, _ex, _sent, _ctx))

        def end_updateReferences(self, _r):
            return _M_ode.api.MetadataStore._op_updateReferences.end(self, _r)

        def saveToDB(self, activity, _ctx=None):
            return _M_ode.api.MetadataStore._op_saveToDB.invoke(self, ((activity, ), _ctx))

        def begin_saveToDB(self, activity, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.MetadataStore._op_saveToDB.begin(self, ((activity, ), _response, _ex, _sent, _ctx))

        def end_saveToDB(self, _r):
            return _M_ode.api.MetadataStore._op_saveToDB.end(self, _r)

        def populateMinMax(self, imageChannelGlobalMinMax, _ctx=None):
            return _M_ode.api.MetadataStore._op_populateMinMax.invoke(self, ((imageChannelGlobalMinMax, ), _ctx))

        def begin_populateMinMax(self, imageChannelGlobalMinMax, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.MetadataStore._op_populateMinMax.begin(self, ((imageChannelGlobalMinMax, ), _response, _ex, _sent, _ctx))

        def end_populateMinMax(self, _r):
            return _M_ode.api.MetadataStore._op_populateMinMax.end(self, _r)

        def setPixelsFile(self, pixelsId, file, repo, _ctx=None):
            return _M_ode.api.MetadataStore._op_setPixelsFile.invoke(self, ((pixelsId, file, repo), _ctx))

        def begin_setPixelsFile(self, pixelsId, file, repo, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.MetadataStore._op_setPixelsFile.begin(self, ((pixelsId, file, repo), _response, _ex, _sent, _ctx))

        def end_setPixelsFile(self, _r):
            return _M_ode.api.MetadataStore._op_setPixelsFile.end(self, _r)

        def postProcess(self, _ctx=None):
            return _M_ode.api.MetadataStore._op_postProcess.invoke(self, ((), _ctx))

        def begin_postProcess(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.MetadataStore._op_postProcess.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_postProcess(self, _r):
            return _M_ode.api.MetadataStore._op_postProcess.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.MetadataStorePrx.ice_checkedCast(proxy, '::ode::api::MetadataStore', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.MetadataStorePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::MetadataStore'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_MetadataStorePrx = IcePy.defineProxy('::ode::api::MetadataStore', MetadataStorePrx)

    _M_ode.api._t_MetadataStore = IcePy.defineClass('::ode::api::MetadataStore', MetadataStore, -1, (), True, False, None, (_M_ode.api._t_StatefulServiceInterface,), ())
    MetadataStore._ice_type = _M_ode.api._t_MetadataStore

    MetadataStore._op_createRoot = IcePy.Operation('createRoot', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    MetadataStore._op_updateObjects = IcePy.Operation('updateObjects', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.metadatastore._t_IObjectContainerArray, False, 0),), (), None, (_M_ode._t_ServerError,))
    MetadataStore._op_updateReferences = IcePy.Operation('updateReferences', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_StringStringArrayMap, False, 0),), (), None, (_M_ode._t_ServerError,))
    MetadataStore._op_saveToDB = IcePy.Operation('saveToDB', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_FilesetJobLink, False, 0),), (), ((), _M_ode.api._t_IObjectListMap, False, 0), (_M_ode._t_ServerError,))
    MetadataStore._op_populateMinMax = IcePy.Operation('populateMinMax', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_DoubleArrayArrayArray, False, 0),), (), None, (_M_ode._t_ServerError,))
    MetadataStore._op_setPixelsFile = IcePy.Operation('setPixelsFile', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    MetadataStore._op_postProcess = IcePy.Operation('postProcess', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), ((), _M_ode.grid._t_InteractiveProcessorList, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.MetadataStore = MetadataStore
    del MetadataStore

    _M_ode.api.MetadataStorePrx = MetadataStorePrx
    del MetadataStorePrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
