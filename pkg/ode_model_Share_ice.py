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
import ode_model_Session_ice

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

if 'ExperimenterGroup' not in _M_ode.model.__dict__:
    _M_ode.model._t_ExperimenterGroup = IcePy.declareClass('::ode::model::ExperimenterGroup')
    _M_ode.model._t_ExperimenterGroupPrx = IcePy.declareProxy('::ode::model::ExperimenterGroup')

if 'Node' not in _M_ode.model.__dict__:
    _M_ode.model._t_Node = IcePy.declareClass('::ode::model::Node')
    _M_ode.model._t_NodePrx = IcePy.declareProxy('::ode::model::Node')

if 'Experimenter' not in _M_ode.model.__dict__:
    _M_ode.model._t_Experimenter = IcePy.declareClass('::ode::model::Experimenter')
    _M_ode.model._t_ExperimenterPrx = IcePy.declareProxy('::ode::model::Experimenter')

if 'Event' not in _M_ode.model.__dict__:
    _M_ode.model._t_Event = IcePy.declareClass('::ode::model::Event')
    _M_ode.model._t_EventPrx = IcePy.declareProxy('::ode::model::Event')

if 'SessionAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_SessionAnnotationLink = IcePy.declareClass('::ode::model::SessionAnnotationLink')
    _M_ode.model._t_SessionAnnotationLinkPrx = IcePy.declareProxy('::ode::model::SessionAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'Share' not in _M_ode.model.__dict__:
    _M_ode.model.Share = Ice.createTempClass()
    class Share(_M_ode.model.Session):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _node=None, _uuid=None, _owner=None, _sudoer=None, _timeToIdle=None, _timeToLive=None, _started=None, _closed=None, _message=None, _defaultEventType=None, _userAgent=None, _userIP=None, _eventsSeq=None, _eventsLoaded=False, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _group=None, _itemCount=None, _active=None, _data=None):
            if Ice.getType(self) == _M_ode.model.Share:
                raise RuntimeError('ode.model.Share is an abstract class')
            _M_ode.model.Session.__init__(self, _id, _details, _loaded, _version, _node, _uuid, _owner, _sudoer, _timeToIdle, _timeToLive, _started, _closed, _message, _defaultEventType, _userAgent, _userIP, _eventsSeq, _eventsLoaded, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._group = _group
            self._itemCount = _itemCount
            self._active = _active
            self._data = _data

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Session', '::ode::model::Share')

        def ice_id(self, current=None):
            return '::ode::model::Share'

        def ice_staticId():
            return '::ode::model::Share'
        ice_staticId = staticmethod(ice_staticId)

        def getGroup(self, current=None):
            pass

        def setGroup(self, theGroup, current=None):
            pass

        def getItemCount(self, current=None):
            pass

        def setItemCount(self, theItemCount, current=None):
            pass

        def getActive(self, current=None):
            pass

        def setActive(self, theActive, current=None):
            pass

        def getData(self, current=None):
            pass

        def setData(self, theData, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Share)

        __repr__ = __str__

    _M_ode.model.SharePrx = Ice.createTempClass()
    class SharePrx(_M_ode.model.SessionPrx):

        def getGroup(self, _ctx=None):
            return _M_ode.model.Share._op_getGroup.invoke(self, ((), _ctx))

        def begin_getGroup(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Share._op_getGroup.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getGroup(self, _r):
            return _M_ode.model.Share._op_getGroup.end(self, _r)

        def setGroup(self, theGroup, _ctx=None):
            return _M_ode.model.Share._op_setGroup.invoke(self, ((theGroup, ), _ctx))

        def begin_setGroup(self, theGroup, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Share._op_setGroup.begin(self, ((theGroup, ), _response, _ex, _sent, _ctx))

        def end_setGroup(self, _r):
            return _M_ode.model.Share._op_setGroup.end(self, _r)

        def getItemCount(self, _ctx=None):
            return _M_ode.model.Share._op_getItemCount.invoke(self, ((), _ctx))

        def begin_getItemCount(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Share._op_getItemCount.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getItemCount(self, _r):
            return _M_ode.model.Share._op_getItemCount.end(self, _r)

        def setItemCount(self, theItemCount, _ctx=None):
            return _M_ode.model.Share._op_setItemCount.invoke(self, ((theItemCount, ), _ctx))

        def begin_setItemCount(self, theItemCount, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Share._op_setItemCount.begin(self, ((theItemCount, ), _response, _ex, _sent, _ctx))

        def end_setItemCount(self, _r):
            return _M_ode.model.Share._op_setItemCount.end(self, _r)

        def getActive(self, _ctx=None):
            return _M_ode.model.Share._op_getActive.invoke(self, ((), _ctx))

        def begin_getActive(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Share._op_getActive.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getActive(self, _r):
            return _M_ode.model.Share._op_getActive.end(self, _r)

        def setActive(self, theActive, _ctx=None):
            return _M_ode.model.Share._op_setActive.invoke(self, ((theActive, ), _ctx))

        def begin_setActive(self, theActive, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Share._op_setActive.begin(self, ((theActive, ), _response, _ex, _sent, _ctx))

        def end_setActive(self, _r):
            return _M_ode.model.Share._op_setActive.end(self, _r)

        def getData(self, _ctx=None):
            return _M_ode.model.Share._op_getData.invoke(self, ((), _ctx))

        def begin_getData(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Share._op_getData.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getData(self, _r):
            return _M_ode.model.Share._op_getData.end(self, _r)

        def setData(self, theData, _ctx=None):
            return _M_ode.model.Share._op_setData.invoke(self, ((theData, ), _ctx))

        def begin_setData(self, theData, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Share._op_setData.begin(self, ((theData, ), _response, _ex, _sent, _ctx))

        def end_setData(self, _r):
            return _M_ode.model.Share._op_setData.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.SharePrx.ice_checkedCast(proxy, '::ode::model::Share', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.SharePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Share'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_SharePrx = IcePy.defineProxy('::ode::model::Share', SharePrx)

    _M_ode.model._t_Share = IcePy.declareClass('::ode::model::Share')

    _M_ode.model._t_Share = IcePy.defineClass('::ode::model::Share', Share, -1, (), True, False, _M_ode.model._t_Session, (), (
        ('_group', (), _M_ode.model._t_ExperimenterGroup, False, 0),
        ('_itemCount', (), _M_ode._t_RLong, False, 0),
        ('_active', (), _M_ode._t_RBool, False, 0),
        ('_data', (), _M_Ice._t_ByteSeq, False, 0)
    ))
    Share._ice_type = _M_ode.model._t_Share

    Share._op_getGroup = IcePy.Operation('getGroup', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ExperimenterGroup, False, 0), ())
    Share._op_setGroup = IcePy.Operation('setGroup', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0),), (), None, ())
    Share._op_getItemCount = IcePy.Operation('getItemCount', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RLong, False, 0), ())
    Share._op_setItemCount = IcePy.Operation('setItemCount', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RLong, False, 0),), (), None, ())
    Share._op_getActive = IcePy.Operation('getActive', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RBool, False, 0), ())
    Share._op_setActive = IcePy.Operation('setActive', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RBool, False, 0),), (), None, ())
    Share._op_getData = IcePy.Operation('getData', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_Ice._t_ByteSeq, False, 0), ())
    Share._op_setData = IcePy.Operation('setData', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_Ice._t_ByteSeq, False, 0),), (), None, ())

    _M_ode.model.Share = Share
    del Share

    _M_ode.model.SharePrx = SharePrx
    del SharePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
