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

if 'FilamentType' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilamentType = IcePy.declareClass('::ode::model::FilamentType')
    _M_ode.model._t_FilamentTypePrx = IcePy.declareProxy('::ode::model::FilamentType')

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

if 'Filament' not in _M_ode.model.__dict__:
    _M_ode.model.Filament = Ice.createTempClass()
    class Filament(_M_ode.model.LightSource):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _manufacturer=None, _model=None, _power=None, _lotNumber=None, _serialNumber=None, _instrument=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _type=None):
            if Ice.getType(self) == _M_ode.model.Filament:
                raise RuntimeError('ode.model.Filament is an abstract class')
            _M_ode.model.LightSource.__init__(self, _id, _details, _loaded, _version, _manufacturer, _model, _power, _lotNumber, _serialNumber, _instrument, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._type = _type

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Filament', '::ode::model::IObject', '::ode::model::LightSource')

        def ice_id(self, current=None):
            return '::ode::model::Filament'

        def ice_staticId():
            return '::ode::model::Filament'
        ice_staticId = staticmethod(ice_staticId)

        def getType(self, current=None):
            pass

        def setType(self, theType, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Filament)

        __repr__ = __str__

    _M_ode.model.FilamentPrx = Ice.createTempClass()
    class FilamentPrx(_M_ode.model.LightSourcePrx):

        def getType(self, _ctx=None):
            return _M_ode.model.Filament._op_getType.invoke(self, ((), _ctx))

        def begin_getType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Filament._op_getType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getType(self, _r):
            return _M_ode.model.Filament._op_getType.end(self, _r)

        def setType(self, theType, _ctx=None):
            return _M_ode.model.Filament._op_setType.invoke(self, ((theType, ), _ctx))

        def begin_setType(self, theType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Filament._op_setType.begin(self, ((theType, ), _response, _ex, _sent, _ctx))

        def end_setType(self, _r):
            return _M_ode.model.Filament._op_setType.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.FilamentPrx.ice_checkedCast(proxy, '::ode::model::Filament', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.FilamentPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Filament'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_FilamentPrx = IcePy.defineProxy('::ode::model::Filament', FilamentPrx)

    _M_ode.model._t_Filament = IcePy.declareClass('::ode::model::Filament')

    _M_ode.model._t_Filament = IcePy.defineClass('::ode::model::Filament', Filament, -1, (), True, False, _M_ode.model._t_LightSource, (), (('_type', (), _M_ode.model._t_FilamentType, False, 0),))
    Filament._ice_type = _M_ode.model._t_Filament

    Filament._op_getType = IcePy.Operation('getType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_FilamentType, False, 0), ())
    Filament._op_setType = IcePy.Operation('setType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilamentType, False, 0),), (), None, ())

    _M_ode.model.Filament = Filament
    del Filament

    _M_ode.model.FilamentPrx = FilamentPrx
    del FilamentPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
