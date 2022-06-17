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
import omero_model_IObject_ice
import omero_RTypes_ice
import omero_model_RTypes_ice
import omero_System_ice
import omero_Collections_ice

# Included module omero
_M_omero = Ice.openModule('omero')

# Included module omero.model
_M_omero.model = Ice.openModule('omero.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module omero.sys
_M_omero.sys = Ice.openModule('omero.sys')

# Included module omero.api
_M_omero.api = Ice.openModule('omero.api')

# Start of module omero
__name__ = 'omero'

# Start of module omero.model
__name__ = 'omero.model'

if 'Folder' not in _M_omero.model.__dict__:
    _M_omero.model._t_Folder = IcePy.declareClass('::omero::model::Folder')
    _M_omero.model._t_FolderPrx = IcePy.declareProxy('::omero::model::Folder')

if 'Roi' not in _M_omero.model.__dict__:
    _M_omero.model._t_Roi = IcePy.declareClass('::omero::model::Roi')
    _M_omero.model._t_RoiPrx = IcePy.declareProxy('::omero::model::Roi')

if 'Details' not in _M_omero.model.__dict__:
    _M_omero.model._t_Details = IcePy.declareClass('::omero::model::Details')
    _M_omero.model._t_DetailsPrx = IcePy.declareProxy('::omero::model::Details')

if 'FolderRoiLink' not in _M_omero.model.__dict__:
    _M_omero.model.FolderRoiLink = Ice.createTempClass()
    class FolderRoiLink(_M_omero.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _parent=None, _child=None):
            if Ice.getType(self) == _M_omero.model.FolderRoiLink:
                raise RuntimeError('omero.model.FolderRoiLink is an abstract class')
            _M_omero.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._parent = _parent
            self._child = _child

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::omero::model::FolderRoiLink', '::omero::model::IObject')

        def ice_id(self, current=None):
            return '::omero::model::FolderRoiLink'

        def ice_staticId():
            return '::omero::model::FolderRoiLink'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getParent(self, current=None):
            pass

        def setParent(self, theParent, current=None):
            pass

        def getChild(self, current=None):
            pass

        def setChild(self, theChild, current=None):
            pass

        def link(self, theParent, theChild, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_omero.model._t_FolderRoiLink)

        __repr__ = __str__

    _M_omero.model.FolderRoiLinkPrx = Ice.createTempClass()
    class FolderRoiLinkPrx(_M_omero.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_omero.model.FolderRoiLink._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_omero.model.FolderRoiLink._op_setVersion.end(self, _r)

        def getParent(self, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_getParent.invoke(self, ((), _ctx))

        def begin_getParent(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_getParent.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getParent(self, _r):
            return _M_omero.model.FolderRoiLink._op_getParent.end(self, _r)

        def setParent(self, theParent, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_setParent.invoke(self, ((theParent, ), _ctx))

        def begin_setParent(self, theParent, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_setParent.begin(self, ((theParent, ), _response, _ex, _sent, _ctx))

        def end_setParent(self, _r):
            return _M_omero.model.FolderRoiLink._op_setParent.end(self, _r)

        def getChild(self, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_getChild.invoke(self, ((), _ctx))

        def begin_getChild(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_getChild.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getChild(self, _r):
            return _M_omero.model.FolderRoiLink._op_getChild.end(self, _r)

        def setChild(self, theChild, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_setChild.invoke(self, ((theChild, ), _ctx))

        def begin_setChild(self, theChild, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_setChild.begin(self, ((theChild, ), _response, _ex, _sent, _ctx))

        def end_setChild(self, _r):
            return _M_omero.model.FolderRoiLink._op_setChild.end(self, _r)

        def link(self, theParent, theChild, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_link.invoke(self, ((theParent, theChild), _ctx))

        def begin_link(self, theParent, theChild, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.FolderRoiLink._op_link.begin(self, ((theParent, theChild), _response, _ex, _sent, _ctx))

        def end_link(self, _r):
            return _M_omero.model.FolderRoiLink._op_link.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_omero.model.FolderRoiLinkPrx.ice_checkedCast(proxy, '::omero::model::FolderRoiLink', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_omero.model.FolderRoiLinkPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::omero::model::FolderRoiLink'
        ice_staticId = staticmethod(ice_staticId)

    _M_omero.model._t_FolderRoiLinkPrx = IcePy.defineProxy('::omero::model::FolderRoiLink', FolderRoiLinkPrx)

    _M_omero.model._t_FolderRoiLink = IcePy.declareClass('::omero::model::FolderRoiLink')

    _M_omero.model._t_FolderRoiLink = IcePy.defineClass('::omero::model::FolderRoiLink', FolderRoiLink, -1, (), True, False, _M_omero.model._t_IObject, (), (
        ('_version', (), _M_omero._t_RInt, False, 0),
        ('_parent', (), _M_omero.model._t_Folder, False, 0),
        ('_child', (), _M_omero.model._t_Roi, False, 0)
    ))
    FolderRoiLink._ice_type = _M_omero.model._t_FolderRoiLink

    FolderRoiLink._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RInt, False, 0), ())
    FolderRoiLink._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RInt, False, 0),), (), None, ())
    FolderRoiLink._op_getParent = IcePy.Operation('getParent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_Folder, False, 0), ())
    FolderRoiLink._op_setParent = IcePy.Operation('setParent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), None, ())
    FolderRoiLink._op_getChild = IcePy.Operation('getChild', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_Roi, False, 0), ())
    FolderRoiLink._op_setChild = IcePy.Operation('setChild', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Roi, False, 0),), (), None, ())
    FolderRoiLink._op_link = IcePy.Operation('link', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0), ((), _M_omero.model._t_Roi, False, 0)), (), None, ())

    _M_omero.model.FolderRoiLink = FolderRoiLink
    del FolderRoiLink

    _M_omero.model.FolderRoiLinkPrx = FolderRoiLinkPrx
    del FolderRoiLinkPrx

# End of module omero.model

__name__ = 'omero'

# End of module omero
