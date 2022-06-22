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
import ode_model_LightSource_ice

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

if 'Power' not in _M_ode.model.__dict__:
    _M_ode.model._t_Power = IcePy.declareClass('::ode::model::Power')
    _M_ode.model._t_PowerPrx = IcePy.declareProxy('::ode::model::Power')

if 'Instrument' not in _M_ode.model.__dict__:
    _M_ode.model._t_Instrument = IcePy.declareClass('::ode::model::Instrument')
    _M_ode.model._t_InstrumentPrx = IcePy.declareProxy('::ode::model::Instrument')

if 'LightSourceAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_LightSourceAnnotationLink = IcePy.declareClass('::ode::model::LightSourceAnnotationLink')
    _M_ode.model._t_LightSourceAnnotationLinkPrx = IcePy.declareProxy('::ode::model::LightSourceAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'GenericExcitationSource' not in _M_ode.model.__dict__:
    _M_ode.model.GenericExcitationSource = Ice.createTempClass()
    class GenericExcitationSource(_M_ode.model.LightSource):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _manufacturer=None, _model=None, _power=None, _lotNumber=None, _serialNumber=None, _instrument=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _map=None):
            if Ice.getType(self) == _M_ode.model.GenericExcitationSource:
                raise RuntimeError('ode.model.GenericExcitationSource is an abstract class')
            _M_ode.model.LightSource.__init__(self, _id, _details, _loaded, _version, _manufacturer, _model, _power, _lotNumber, _serialNumber, _instrument, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._map = _map

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::GenericExcitationSource', '::ode::model::IObject', '::ode::model::LightSource')

        def ice_id(self, current=None):
            return '::ode::model::GenericExcitationSource'

        def ice_staticId():
            return '::ode::model::GenericExcitationSource'
        ice_staticId = staticmethod(ice_staticId)

        def getMapAsMap(self, current=None):
            pass

        def getMap(self, current=None):
            pass

        def setMap(self, theMap, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_GenericExcitationSource)

        __repr__ = __str__

    _M_ode.model.GenericExcitationSourcePrx = Ice.createTempClass()
    class GenericExcitationSourcePrx(_M_ode.model.LightSourcePrx):

        def getMapAsMap(self, _ctx=None):
            return _M_ode.model.GenericExcitationSource._op_getMapAsMap.invoke(self, ((), _ctx))

        def begin_getMapAsMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.GenericExcitationSource._op_getMapAsMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMapAsMap(self, _r):
            return _M_ode.model.GenericExcitationSource._op_getMapAsMap.end(self, _r)

        def getMap(self, _ctx=None):
            return _M_ode.model.GenericExcitationSource._op_getMap.invoke(self, ((), _ctx))

        def begin_getMap(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.GenericExcitationSource._op_getMap.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMap(self, _r):
            return _M_ode.model.GenericExcitationSource._op_getMap.end(self, _r)

        def setMap(self, theMap, _ctx=None):
            return _M_ode.model.GenericExcitationSource._op_setMap.invoke(self, ((theMap, ), _ctx))

        def begin_setMap(self, theMap, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.GenericExcitationSource._op_setMap.begin(self, ((theMap, ), _response, _ex, _sent, _ctx))

        def end_setMap(self, _r):
            return _M_ode.model.GenericExcitationSource._op_setMap.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.GenericExcitationSourcePrx.ice_checkedCast(proxy, '::ode::model::GenericExcitationSource', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.GenericExcitationSourcePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::GenericExcitationSource'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_GenericExcitationSourcePrx = IcePy.defineProxy('::ode::model::GenericExcitationSource', GenericExcitationSourcePrx)

    _M_ode.model._t_GenericExcitationSource = IcePy.declareClass('::ode::model::GenericExcitationSource')

    _M_ode.model._t_GenericExcitationSource = IcePy.defineClass('::ode::model::GenericExcitationSource', GenericExcitationSource, -1, (), True, False, _M_ode.model._t_LightSource, (), (('_map', (), _M_ode.api._t_NamedValueList, False, 0),))
    GenericExcitationSource._ice_type = _M_ode.model._t_GenericExcitationSource

    GenericExcitationSource._op_getMapAsMap = IcePy.Operation('getMapAsMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_StringStringMap, False, 0), ())
    GenericExcitationSource._op_getMap = IcePy.Operation('getMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_NamedValueList, False, 0), ())
    GenericExcitationSource._op_setMap = IcePy.Operation('setMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_NamedValueList, False, 0),), (), None, ())

    _M_ode.model.GenericExcitationSource = GenericExcitationSource
    del GenericExcitationSource

    _M_ode.model.GenericExcitationSourcePrx = GenericExcitationSourcePrx
    del GenericExcitationSourcePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
