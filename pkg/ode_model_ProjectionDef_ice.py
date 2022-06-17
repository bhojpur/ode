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

if 'RenderingDef' not in _M_ode.model.__dict__:
    _M_ode.model._t_RenderingDef = IcePy.declareClass('::ode::model::RenderingDef')
    _M_ode.model._t_RenderingDefPrx = IcePy.declareProxy('::ode::model::RenderingDef')

if 'ProjectionAxis' not in _M_ode.model.__dict__:
    _M_ode.model._t_ProjectionAxis = IcePy.declareClass('::ode::model::ProjectionAxis')
    _M_ode.model._t_ProjectionAxisPrx = IcePy.declareProxy('::ode::model::ProjectionAxis')

if 'ProjectionType' not in _M_ode.model.__dict__:
    _M_ode.model._t_ProjectionType = IcePy.declareClass('::ode::model::ProjectionType')
    _M_ode.model._t_ProjectionTypePrx = IcePy.declareProxy('::ode::model::ProjectionType')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'ProjectionDef' not in _M_ode.model.__dict__:
    _M_ode.model.ProjectionDef = Ice.createTempClass()
    class ProjectionDef(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _renderingDef=None, _axis=None, _type=None, _active=None, _startPlane=None, _endPlane=None, _stepping=None):
            if Ice.getType(self) == _M_ode.model.ProjectionDef:
                raise RuntimeError('ode.model.ProjectionDef is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._renderingDef = _renderingDef
            self._axis = _axis
            self._type = _type
            self._active = _active
            self._startPlane = _startPlane
            self._endPlane = _endPlane
            self._stepping = _stepping

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::ProjectionDef')

        def ice_id(self, current=None):
            return '::ode::model::ProjectionDef'

        def ice_staticId():
            return '::ode::model::ProjectionDef'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getRenderingDef(self, current=None):
            pass

        def setRenderingDef(self, theRenderingDef, current=None):
            pass

        def getAxis(self, current=None):
            pass

        def setAxis(self, theAxis, current=None):
            pass

        def getType(self, current=None):
            pass

        def setType(self, theType, current=None):
            pass

        def getActive(self, current=None):
            pass

        def setActive(self, theActive, current=None):
            pass

        def getStartPlane(self, current=None):
            pass

        def setStartPlane(self, theStartPlane, current=None):
            pass

        def getEndPlane(self, current=None):
            pass

        def setEndPlane(self, theEndPlane, current=None):
            pass

        def getStepping(self, current=None):
            pass

        def setStepping(self, theStepping, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_ProjectionDef)

        __repr__ = __str__

    _M_ode.model.ProjectionDefPrx = Ice.createTempClass()
    class ProjectionDefPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.ProjectionDef._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.ProjectionDef._op_setVersion.end(self, _r)

        def getRenderingDef(self, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getRenderingDef.invoke(self, ((), _ctx))

        def begin_getRenderingDef(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getRenderingDef.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRenderingDef(self, _r):
            return _M_ode.model.ProjectionDef._op_getRenderingDef.end(self, _r)

        def setRenderingDef(self, theRenderingDef, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setRenderingDef.invoke(self, ((theRenderingDef, ), _ctx))

        def begin_setRenderingDef(self, theRenderingDef, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setRenderingDef.begin(self, ((theRenderingDef, ), _response, _ex, _sent, _ctx))

        def end_setRenderingDef(self, _r):
            return _M_ode.model.ProjectionDef._op_setRenderingDef.end(self, _r)

        def getAxis(self, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getAxis.invoke(self, ((), _ctx))

        def begin_getAxis(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getAxis.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAxis(self, _r):
            return _M_ode.model.ProjectionDef._op_getAxis.end(self, _r)

        def setAxis(self, theAxis, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setAxis.invoke(self, ((theAxis, ), _ctx))

        def begin_setAxis(self, theAxis, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setAxis.begin(self, ((theAxis, ), _response, _ex, _sent, _ctx))

        def end_setAxis(self, _r):
            return _M_ode.model.ProjectionDef._op_setAxis.end(self, _r)

        def getType(self, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getType.invoke(self, ((), _ctx))

        def begin_getType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getType(self, _r):
            return _M_ode.model.ProjectionDef._op_getType.end(self, _r)

        def setType(self, theType, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setType.invoke(self, ((theType, ), _ctx))

        def begin_setType(self, theType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setType.begin(self, ((theType, ), _response, _ex, _sent, _ctx))

        def end_setType(self, _r):
            return _M_ode.model.ProjectionDef._op_setType.end(self, _r)

        def getActive(self, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getActive.invoke(self, ((), _ctx))

        def begin_getActive(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getActive.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getActive(self, _r):
            return _M_ode.model.ProjectionDef._op_getActive.end(self, _r)

        def setActive(self, theActive, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setActive.invoke(self, ((theActive, ), _ctx))

        def begin_setActive(self, theActive, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setActive.begin(self, ((theActive, ), _response, _ex, _sent, _ctx))

        def end_setActive(self, _r):
            return _M_ode.model.ProjectionDef._op_setActive.end(self, _r)

        def getStartPlane(self, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getStartPlane.invoke(self, ((), _ctx))

        def begin_getStartPlane(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getStartPlane.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStartPlane(self, _r):
            return _M_ode.model.ProjectionDef._op_getStartPlane.end(self, _r)

        def setStartPlane(self, theStartPlane, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setStartPlane.invoke(self, ((theStartPlane, ), _ctx))

        def begin_setStartPlane(self, theStartPlane, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setStartPlane.begin(self, ((theStartPlane, ), _response, _ex, _sent, _ctx))

        def end_setStartPlane(self, _r):
            return _M_ode.model.ProjectionDef._op_setStartPlane.end(self, _r)

        def getEndPlane(self, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getEndPlane.invoke(self, ((), _ctx))

        def begin_getEndPlane(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getEndPlane.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getEndPlane(self, _r):
            return _M_ode.model.ProjectionDef._op_getEndPlane.end(self, _r)

        def setEndPlane(self, theEndPlane, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setEndPlane.invoke(self, ((theEndPlane, ), _ctx))

        def begin_setEndPlane(self, theEndPlane, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setEndPlane.begin(self, ((theEndPlane, ), _response, _ex, _sent, _ctx))

        def end_setEndPlane(self, _r):
            return _M_ode.model.ProjectionDef._op_setEndPlane.end(self, _r)

        def getStepping(self, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getStepping.invoke(self, ((), _ctx))

        def begin_getStepping(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_getStepping.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStepping(self, _r):
            return _M_ode.model.ProjectionDef._op_getStepping.end(self, _r)

        def setStepping(self, theStepping, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setStepping.invoke(self, ((theStepping, ), _ctx))

        def begin_setStepping(self, theStepping, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.ProjectionDef._op_setStepping.begin(self, ((theStepping, ), _response, _ex, _sent, _ctx))

        def end_setStepping(self, _r):
            return _M_ode.model.ProjectionDef._op_setStepping.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ProjectionDefPrx.ice_checkedCast(proxy, '::ode::model::ProjectionDef', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ProjectionDefPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::ProjectionDef'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ProjectionDefPrx = IcePy.defineProxy('::ode::model::ProjectionDef', ProjectionDefPrx)

    _M_ode.model._t_ProjectionDef = IcePy.declareClass('::ode::model::ProjectionDef')

    _M_ode.model._t_ProjectionDef = IcePy.defineClass('::ode::model::ProjectionDef', ProjectionDef, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_renderingDef', (), _M_ode.model._t_RenderingDef, False, 0),
        ('_axis', (), _M_ode.model._t_ProjectionAxis, False, 0),
        ('_type', (), _M_ode.model._t_ProjectionType, False, 0),
        ('_active', (), _M_ode._t_RBool, False, 0),
        ('_startPlane', (), _M_ode._t_RInt, False, 0),
        ('_endPlane', (), _M_ode._t_RInt, False, 0),
        ('_stepping', (), _M_ode._t_RInt, False, 0)
    ))
    ProjectionDef._ice_type = _M_ode.model._t_ProjectionDef

    ProjectionDef._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ProjectionDef._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ProjectionDef._op_getRenderingDef = IcePy.Operation('getRenderingDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_RenderingDef, False, 0), ())
    ProjectionDef._op_setRenderingDef = IcePy.Operation('setRenderingDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingDef, False, 0),), (), None, ())
    ProjectionDef._op_getAxis = IcePy.Operation('getAxis', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ProjectionAxis, False, 0), ())
    ProjectionDef._op_setAxis = IcePy.Operation('setAxis', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectionAxis, False, 0),), (), None, ())
    ProjectionDef._op_getType = IcePy.Operation('getType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ProjectionType, False, 0), ())
    ProjectionDef._op_setType = IcePy.Operation('setType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectionType, False, 0),), (), None, ())
    ProjectionDef._op_getActive = IcePy.Operation('getActive', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    ProjectionDef._op_setActive = IcePy.Operation('setActive', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    ProjectionDef._op_getStartPlane = IcePy.Operation('getStartPlane', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ProjectionDef._op_setStartPlane = IcePy.Operation('setStartPlane', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ProjectionDef._op_getEndPlane = IcePy.Operation('getEndPlane', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ProjectionDef._op_setEndPlane = IcePy.Operation('setEndPlane', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    ProjectionDef._op_getStepping = IcePy.Operation('getStepping', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    ProjectionDef._op_setStepping = IcePy.Operation('setStepping', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())

    _M_ode.model.ProjectionDef = ProjectionDef
    del ProjectionDef

    _M_ode.model.ProjectionDefPrx = ProjectionDefPrx
    del ProjectionDefPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
