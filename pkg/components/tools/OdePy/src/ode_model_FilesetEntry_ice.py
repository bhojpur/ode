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

if 'Fileset' not in _M_ode.model.__dict__:
    _M_ode.model._t_Fileset = IcePy.declareClass('::ode::model::Fileset')
    _M_ode.model._t_FilesetPrx = IcePy.declareProxy('::ode::model::Fileset')

if 'OriginalFile' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFile = IcePy.declareClass('::ode::model::OriginalFile')
    _M_ode.model._t_OriginalFilePrx = IcePy.declareProxy('::ode::model::OriginalFile')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'FilesetEntry' not in _M_ode.model.__dict__:
    _M_ode.model.FilesetEntry = Ice.createTempClass()
    class FilesetEntry(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _fileset=None, _originalFile=None, _clientPath=None):
            if Ice.getType(self) == _M_ode.model.FilesetEntry:
                raise RuntimeError('ode.model.FilesetEntry is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._fileset = _fileset
            self._originalFile = _originalFile
            self._clientPath = _clientPath

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::FilesetEntry', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::FilesetEntry'

        def ice_staticId():
            return '::ode::model::FilesetEntry'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getFileset(self, current=None):
            pass

        def setFileset(self, theFileset, current=None):
            pass

        def getOriginalFile(self, current=None):
            pass

        def setOriginalFile(self, theOriginalFile, current=None):
            pass

        def getClientPath(self, current=None):
            pass

        def setClientPath(self, theClientPath, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_FilesetEntry)

        __repr__ = __str__

    _M_ode.model.FilesetEntryPrx = Ice.createTempClass()
    class FilesetEntryPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.FilesetEntry._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.FilesetEntry._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.FilesetEntry._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.FilesetEntry._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.FilesetEntry._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.FilesetEntry._op_setVersion.end(self, _r)

        def getFileset(self, _ctx=None):
            return _M_ode.model.FilesetEntry._op_getFileset.invoke(self, ((), _ctx))

        def begin_getFileset(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.FilesetEntry._op_getFileset.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFileset(self, _r):
            return _M_ode.model.FilesetEntry._op_getFileset.end(self, _r)

        def setFileset(self, theFileset, _ctx=None):
            return _M_ode.model.FilesetEntry._op_setFileset.invoke(self, ((theFileset, ), _ctx))

        def begin_setFileset(self, theFileset, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.FilesetEntry._op_setFileset.begin(self, ((theFileset, ), _response, _ex, _sent, _ctx))

        def end_setFileset(self, _r):
            return _M_ode.model.FilesetEntry._op_setFileset.end(self, _r)

        def getOriginalFile(self, _ctx=None):
            return _M_ode.model.FilesetEntry._op_getOriginalFile.invoke(self, ((), _ctx))

        def begin_getOriginalFile(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.FilesetEntry._op_getOriginalFile.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getOriginalFile(self, _r):
            return _M_ode.model.FilesetEntry._op_getOriginalFile.end(self, _r)

        def setOriginalFile(self, theOriginalFile, _ctx=None):
            return _M_ode.model.FilesetEntry._op_setOriginalFile.invoke(self, ((theOriginalFile, ), _ctx))

        def begin_setOriginalFile(self, theOriginalFile, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.FilesetEntry._op_setOriginalFile.begin(self, ((theOriginalFile, ), _response, _ex, _sent, _ctx))

        def end_setOriginalFile(self, _r):
            return _M_ode.model.FilesetEntry._op_setOriginalFile.end(self, _r)

        def getClientPath(self, _ctx=None):
            return _M_ode.model.FilesetEntry._op_getClientPath.invoke(self, ((), _ctx))

        def begin_getClientPath(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.FilesetEntry._op_getClientPath.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getClientPath(self, _r):
            return _M_ode.model.FilesetEntry._op_getClientPath.end(self, _r)

        def setClientPath(self, theClientPath, _ctx=None):
            return _M_ode.model.FilesetEntry._op_setClientPath.invoke(self, ((theClientPath, ), _ctx))

        def begin_setClientPath(self, theClientPath, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.FilesetEntry._op_setClientPath.begin(self, ((theClientPath, ), _response, _ex, _sent, _ctx))

        def end_setClientPath(self, _r):
            return _M_ode.model.FilesetEntry._op_setClientPath.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.FilesetEntryPrx.ice_checkedCast(proxy, '::ode::model::FilesetEntry', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.FilesetEntryPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::FilesetEntry'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_FilesetEntryPrx = IcePy.defineProxy('::ode::model::FilesetEntry', FilesetEntryPrx)

    _M_ode.model._t_FilesetEntry = IcePy.declareClass('::ode::model::FilesetEntry')

    _M_ode.model._t_FilesetEntry = IcePy.defineClass('::ode::model::FilesetEntry', FilesetEntry, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_fileset', (), _M_ode.model._t_Fileset, False, 0),
        ('_originalFile', (), _M_ode.model._t_OriginalFile, False, 0),
        ('_clientPath', (), _M_ode._t_RString, False, 0)
    ))
    FilesetEntry._ice_type = _M_ode.model._t_FilesetEntry

    FilesetEntry._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    FilesetEntry._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    FilesetEntry._op_getFileset = IcePy.Operation('getFileset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Fileset, False, 0), ())
    FilesetEntry._op_setFileset = IcePy.Operation('setFileset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Fileset, False, 0),), (), None, ())
    FilesetEntry._op_getOriginalFile = IcePy.Operation('getOriginalFile', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OriginalFile, False, 0), ())
    FilesetEntry._op_setOriginalFile = IcePy.Operation('setOriginalFile', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), None, ())
    FilesetEntry._op_getClientPath = IcePy.Operation('getClientPath', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    FilesetEntry._op_setClientPath = IcePy.Operation('setClientPath', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.FilesetEntry = FilesetEntry
    del FilesetEntry

    _M_ode.model.FilesetEntryPrx = FilesetEntryPrx
    del FilesetEntryPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
