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
import omero_model_LightSource_ice

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

if 'FilamentType' not in _M_omero.model.__dict__:
    _M_omero.model._t_FilamentType = IcePy.declareClass('::omero::model::FilamentType')
    _M_omero.model._t_FilamentTypePrx = IcePy.declareProxy('::omero::model::FilamentType')

if 'Power' not in _M_omero.model.__dict__:
    _M_omero.model._t_Power = IcePy.declareClass('::omero::model::Power')
    _M_omero.model._t_PowerPrx = IcePy.declareProxy('::omero::model::Power')

if 'Instrument' not in _M_omero.model.__dict__:
    _M_omero.model._t_Instrument = IcePy.declareClass('::omero::model::Instrument')
    _M_omero.model._t_InstrumentPrx = IcePy.declareProxy('::omero::model::Instrument')

if 'LightSourceAnnotationLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_LightSourceAnnotationLink = IcePy.declareClass('::omero::model::LightSourceAnnotationLink')
    _M_omero.model._t_LightSourceAnnotationLinkPrx = IcePy.declareProxy('::omero::model::LightSourceAnnotationLink')

if 'Annotation' not in _M_omero.model.__dict__:
    _M_omero.model._t_Annotation = IcePy.declareClass('::omero::model::Annotation')
    _M_omero.model._t_AnnotationPrx = IcePy.declareProxy('::omero::model::Annotation')

if 'Details' not in _M_omero.model.__dict__:
    _M_omero.model._t_Details = IcePy.declareClass('::omero::model::Details')
    _M_omero.model._t_DetailsPrx = IcePy.declareProxy('::omero::model::Details')

if 'Filament' not in _M_omero.model.__dict__:
    _M_omero.model.Filament = Ice.createTempClass()
    class Filament(_M_omero.model.LightSource):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _manufacturer=None, _model=None, _power=None, _lotNumber=None, _serialNumber=None, _instrument=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _type=None):
            if Ice.getType(self) == _M_omero.model.Filament:
                raise RuntimeError('omero.model.Filament is an abstract class')
            _M_omero.model.LightSource.__init__(self, _id, _details, _loaded, _version, _manufacturer, _model, _power, _lotNumber, _serialNumber, _instrument, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._type = _type

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::omero::model::Filament', '::omero::model::IObject', '::omero::model::LightSource')

        def ice_id(self, current=None):
            return '::omero::model::Filament'

        def ice_staticId():
            return '::omero::model::Filament'
        ice_staticId = staticmethod(ice_staticId)

        def getType(self, current=None):
            pass

        def setType(self, theType, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_omero.model._t_Filament)

        __repr__ = __str__

    _M_omero.model.FilamentPrx = Ice.createTempClass()
    class FilamentPrx(_M_omero.model.LightSourcePrx):

        def getType(self, _ctx=None):
            return _M_omero.model.Filament._op_getType.invoke(self, ((), _ctx))

        def begin_getType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filament._op_getType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getType(self, _r):
            return _M_omero.model.Filament._op_getType.end(self, _r)

        def setType(self, theType, _ctx=None):
            return _M_omero.model.Filament._op_setType.invoke(self, ((theType, ), _ctx))

        def begin_setType(self, theType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Filament._op_setType.begin(self, ((theType, ), _response, _ex, _sent, _ctx))

        def end_setType(self, _r):
            return _M_omero.model.Filament._op_setType.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_omero.model.FilamentPrx.ice_checkedCast(proxy, '::omero::model::Filament', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_omero.model.FilamentPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::omero::model::Filament'
        ice_staticId = staticmethod(ice_staticId)

    _M_omero.model._t_FilamentPrx = IcePy.defineProxy('::omero::model::Filament', FilamentPrx)

    _M_omero.model._t_Filament = IcePy.declareClass('::omero::model::Filament')

    _M_omero.model._t_Filament = IcePy.defineClass('::omero::model::Filament', Filament, -1, (), True, False, _M_omero.model._t_LightSource, (), (('_type', (), _M_omero.model._t_FilamentType, False, 0),))
    Filament._ice_type = _M_omero.model._t_Filament

    Filament._op_getType = IcePy.Operation('getType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FilamentType, False, 0), ())
    Filament._op_setType = IcePy.Operation('setType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FilamentType, False, 0),), (), None, ())

    _M_omero.model.Filament = Filament
    del Filament

    _M_omero.model.FilamentPrx = FilamentPrx
    del FilamentPrx

# End of module omero.model

__name__ = 'omero'

# End of module omero
