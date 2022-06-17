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

if 'Project' not in _M_ode.model.__dict__:
    _M_ode.model._t_Project = IcePy.declareClass('::ode::model::Project')
    _M_ode.model._t_ProjectPrx = IcePy.declareProxy('::ode::model::Project')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'ProjectAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model.ProjectAnnotationLink = Ice.createTempClass()
    class ProjectAnnotationLink(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _parent=None, _child=None):
            if Ice.getType(self) == _M_ode.model.ProjectAnnotationLink:
                raise RuntimeError('ode.model.ProjectAnnotationLink is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._parent = _parent
            self._child = _child

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::ProjectAnnotationLink')

        def ice_id(self, current=None):
            return '::ode::model::ProjectAnnotationLink'

        def ice_staticId():
            return '::ode::model::ProjectAnnotationLink'
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
            return IcePy.stringify(self, _M_ode.model._t_ProjectAnnotationLink)

        __repr__ = __str__

    _M_ode.model.ProjectAnnotationLinkPrx = Ice.createTempClass()
    class ProjectAnnotationLinkPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.ProjectAnnotationLink._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.ProjectAnnotationLink._op_setVersion.end(self, _r)

        def getParent(self, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_getParent.invoke(self, ((), _ctx))

        def begin_getParent(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_getParent.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getParent(self, _r):
            return _M_ode.model.ProjectAnnotationLink._op_getParent.end(self, _r)

        def setParent(self, theParent, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_setParent.invoke(self, ((theParent, ), _ctx))

        def begin_setParent(self, theParent, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_setParent.begin(self, ((theParent, ), _response, _ex, _sent, _ctx))

        def end_setParent(self, _r):
            return _M_ode.model.ProjectAnnotationLink._op_setParent.end(self, _r)

        def getChild(self, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_getChild.invoke(self, ((), _ctx))

        def begin_getChild(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_getChild.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getChild(self, _r):
            return _M_ode.model.ProjectAnnotationLink._op_getChild.end(self, _r)

        def setChild(self, theChild, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_setChild.invoke(self, ((theChild, ), _ctx))

        def begin_setChild(self, theChild, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_setChild.begin(self, ((theChild, ), _response, _ex, _sent, _ctx))

        def end_setChild(self, _r):
            return _M_ode.model.ProjectAnnotationLink._op_setChild.end(self, _r)

        def link(self, theParent, theChild, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_link.invoke(self, ((theParent, theChild), _ctx))

        def begin_link(self, theParent, theChild, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectAnnotationLink._op_link.begin(self, ((theParent, theChild), _response, _ex, _sent, _ctx))

        def end_link(self, _r):
            return _M_ode.model.ProjectAnnotationLink._op_link.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ProjectAnnotationLinkPrx.ice_checkedCast(proxy, '::ode::model::ProjectAnnotationLink', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ProjectAnnotationLinkPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ProjectAnnotationLink'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ProjectAnnotationLinkPrx = IcePy.defineProxy('::ode::model::ProjectAnnotationLink', ProjectAnnotationLinkPrx)

    _M_ode.model._t_ProjectAnnotationLink = IcePy.declareClass('::ode::model::ProjectAnnotationLink')

    _M_ode.model._t_ProjectAnnotationLink = IcePy.defineClass('::ode::model::ProjectAnnotationLink', ProjectAnnotationLink, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_parent', (), _M_ode.model._t_Project, False, 0),
        ('_child', (), _M_ode.model._t_Annotation, False, 0)
    ))
    ProjectAnnotationLink._ice_type = _M_ode.model._t_ProjectAnnotationLink

    ProjectAnnotationLink._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ProjectAnnotationLink._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ProjectAnnotationLink._op_getParent = IcePy.Operation('getParent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Project, False, 0), ())
    ProjectAnnotationLink._op_setParent = IcePy.Operation('setParent', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Project, False, 0),), (), None, ())
    ProjectAnnotationLink._op_getChild = IcePy.Operation('getChild', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Annotation, False, 0), ())
    ProjectAnnotationLink._op_setChild = IcePy.Operation('setChild', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    ProjectAnnotationLink._op_link = IcePy.Operation('link', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Project, False, 0), ((), _M_ode.model._t_Annotation, False, 0)), (), None, ())

    _M_ode.model.ProjectAnnotationLink = ProjectAnnotationLink
    del ProjectAnnotationLink

    _M_ode.model.ProjectAnnotationLinkPrx = ProjectAnnotationLinkPrx
    del ProjectAnnotationLinkPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
