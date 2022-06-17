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

if 'Length' not in _M_ode.model.__dict__:
    _M_ode.model._t_Length = IcePy.declareClass('::ode::model::Length')
    _M_ode.model._t_LengthPrx = IcePy.declareProxy('::ode::model::Length')

if 'PlateAcquisition' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlateAcquisition = IcePy.declareClass('::ode::model::PlateAcquisition')
    _M_ode.model._t_PlateAcquisitionPrx = IcePy.declareProxy('::ode::model::PlateAcquisition')

if 'Well' not in _M_ode.model.__dict__:
    _M_ode.model._t_Well = IcePy.declareClass('::ode::model::Well')
    _M_ode.model._t_WellPrx = IcePy.declareProxy('::ode::model::Well')

if 'Image' not in _M_ode.model.__dict__:
    _M_ode.model._t_Image = IcePy.declareClass('::ode::model::Image')
    _M_ode.model._t_ImagePrx = IcePy.declareProxy('::ode::model::Image')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'WellSample' not in _M_ode.model.__dict__:
    _M_ode.model.WellSample = Ice.createTempClass()
    class WellSample(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _posX=None, _posY=None, _timepoint=None, _plateAcquisition=None, _well=None, _image=None):
            if Ice.getType(self) == _M_ode.model.WellSample:
                raise RuntimeError('ode.model.WellSample is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._posX = _posX
            self._posY = _posY
            self._timepoint = _timepoint
            self._plateAcquisition = _plateAcquisition
            self._well = _well
            self._image = _image

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::WellSample')

        def ice_id(self, current=None):
            return '::ode::model::WellSample'

        def ice_staticId():
            return '::ode::model::WellSample'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getPosX(self, current=None):
            pass

        def setPosX(self, thePosX, current=None):
            pass

        def getPosY(self, current=None):
            pass

        def setPosY(self, thePosY, current=None):
            pass

        def getTimepoint(self, current=None):
            pass

        def setTimepoint(self, theTimepoint, current=None):
            pass

        def getPlateAcquisition(self, current=None):
            pass

        def setPlateAcquisition(self, thePlateAcquisition, current=None):
            pass

        def getWell(self, current=None):
            pass

        def setWell(self, theWell, current=None):
            pass

        def getImage(self, current=None):
            pass

        def setImage(self, theImage, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_WellSample)

        __repr__ = __str__

    _M_ode.model.WellSamplePrx = Ice.createTempClass()
    class WellSamplePrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.WellSample._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.WellSample._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.WellSample._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.WellSample._op_setVersion.end(self, _r)

        def getPosX(self, _ctx=None):
            return _M_ode.model.WellSample._op_getPosX.invoke(self, ((), _ctx))

        def begin_getPosX(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_getPosX.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPosX(self, _r):
            return _M_ode.model.WellSample._op_getPosX.end(self, _r)

        def setPosX(self, thePosX, _ctx=None):
            return _M_ode.model.WellSample._op_setPosX.invoke(self, ((thePosX, ), _ctx))

        def begin_setPosX(self, thePosX, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_setPosX.begin(self, ((thePosX, ), _response, _ex, _sent, _ctx))

        def end_setPosX(self, _r):
            return _M_ode.model.WellSample._op_setPosX.end(self, _r)

        def getPosY(self, _ctx=None):
            return _M_ode.model.WellSample._op_getPosY.invoke(self, ((), _ctx))

        def begin_getPosY(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_getPosY.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPosY(self, _r):
            return _M_ode.model.WellSample._op_getPosY.end(self, _r)

        def setPosY(self, thePosY, _ctx=None):
            return _M_ode.model.WellSample._op_setPosY.invoke(self, ((thePosY, ), _ctx))

        def begin_setPosY(self, thePosY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_setPosY.begin(self, ((thePosY, ), _response, _ex, _sent, _ctx))

        def end_setPosY(self, _r):
            return _M_ode.model.WellSample._op_setPosY.end(self, _r)

        def getTimepoint(self, _ctx=None):
            return _M_ode.model.WellSample._op_getTimepoint.invoke(self, ((), _ctx))

        def begin_getTimepoint(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_getTimepoint.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTimepoint(self, _r):
            return _M_ode.model.WellSample._op_getTimepoint.end(self, _r)

        def setTimepoint(self, theTimepoint, _ctx=None):
            return _M_ode.model.WellSample._op_setTimepoint.invoke(self, ((theTimepoint, ), _ctx))

        def begin_setTimepoint(self, theTimepoint, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_setTimepoint.begin(self, ((theTimepoint, ), _response, _ex, _sent, _ctx))

        def end_setTimepoint(self, _r):
            return _M_ode.model.WellSample._op_setTimepoint.end(self, _r)

        def getPlateAcquisition(self, _ctx=None):
            return _M_ode.model.WellSample._op_getPlateAcquisition.invoke(self, ((), _ctx))

        def begin_getPlateAcquisition(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_getPlateAcquisition.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPlateAcquisition(self, _r):
            return _M_ode.model.WellSample._op_getPlateAcquisition.end(self, _r)

        def setPlateAcquisition(self, thePlateAcquisition, _ctx=None):
            return _M_ode.model.WellSample._op_setPlateAcquisition.invoke(self, ((thePlateAcquisition, ), _ctx))

        def begin_setPlateAcquisition(self, thePlateAcquisition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_setPlateAcquisition.begin(self, ((thePlateAcquisition, ), _response, _ex, _sent, _ctx))

        def end_setPlateAcquisition(self, _r):
            return _M_ode.model.WellSample._op_setPlateAcquisition.end(self, _r)

        def getWell(self, _ctx=None):
            return _M_ode.model.WellSample._op_getWell.invoke(self, ((), _ctx))

        def begin_getWell(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_getWell.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getWell(self, _r):
            return _M_ode.model.WellSample._op_getWell.end(self, _r)

        def setWell(self, theWell, _ctx=None):
            return _M_ode.model.WellSample._op_setWell.invoke(self, ((theWell, ), _ctx))

        def begin_setWell(self, theWell, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_setWell.begin(self, ((theWell, ), _response, _ex, _sent, _ctx))

        def end_setWell(self, _r):
            return _M_ode.model.WellSample._op_setWell.end(self, _r)

        def getImage(self, _ctx=None):
            return _M_ode.model.WellSample._op_getImage.invoke(self, ((), _ctx))

        def begin_getImage(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_getImage.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getImage(self, _r):
            return _M_ode.model.WellSample._op_getImage.end(self, _r)

        def setImage(self, theImage, _ctx=None):
            return _M_ode.model.WellSample._op_setImage.invoke(self, ((theImage, ), _ctx))

        def begin_setImage(self, theImage, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.WellSample._op_setImage.begin(self, ((theImage, ), _response, _ex, _sent, _ctx))

        def end_setImage(self, _r):
            return _M_ode.model.WellSample._op_setImage.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.WellSamplePrx.ice_checkedCast(proxy, '::ode::model::WellSample', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.WellSamplePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::WellSample'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_WellSamplePrx = IcePy.defineProxy('::ode::model::WellSample', WellSamplePrx)

    _M_ode.model._t_WellSample = IcePy.declareClass('::ode::model::WellSample')

    _M_ode.model._t_WellSample = IcePy.defineClass('::ode::model::WellSample', WellSample, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_posX', (), _M_ode.model._t_Length, False, 0),
        ('_posY', (), _M_ode.model._t_Length, False, 0),
        ('_timepoint', (), _M_ode._t_RTime, False, 0),
        ('_plateAcquisition', (), _M_ode.model._t_PlateAcquisition, False, 0),
        ('_well', (), _M_ode.model._t_Well, False, 0),
        ('_image', (), _M_ode.model._t_Image, False, 0)
    ))
    WellSample._ice_type = _M_ode.model._t_WellSample

    WellSample._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    WellSample._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    WellSample._op_getPosX = IcePy.Operation('getPosX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    WellSample._op_setPosX = IcePy.Operation('setPosX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    WellSample._op_getPosY = IcePy.Operation('getPosY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    WellSample._op_setPosY = IcePy.Operation('setPosY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    WellSample._op_getTimepoint = IcePy.Operation('getTimepoint', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    WellSample._op_setTimepoint = IcePy.Operation('setTimepoint', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    WellSample._op_getPlateAcquisition = IcePy.Operation('getPlateAcquisition', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlateAcquisition, False, 0), ())
    WellSample._op_setPlateAcquisition = IcePy.Operation('setPlateAcquisition', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlateAcquisition, False, 0),), (), None, ())
    WellSample._op_getWell = IcePy.Operation('getWell', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Well, False, 0), ())
    WellSample._op_setWell = IcePy.Operation('setWell', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Well, False, 0),), (), None, ())
    WellSample._op_getImage = IcePy.Operation('getImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Image, False, 0), ())
    WellSample._op_setImage = IcePy.Operation('setImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Image, False, 0),), (), None, ())

    _M_ode.model.WellSample = WellSample
    del WellSample

    _M_ode.model.WellSamplePrx = WellSamplePrx
    del WellSamplePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
