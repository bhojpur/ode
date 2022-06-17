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

# Start of module omero.model.enums
_M_omero.model.enums = Ice.openModule('omero.model.enums')
__name__ = 'omero.model.enums'

_M_omero.model.enums.FilamentTypeIncandescent = "Incandescent"

_M_omero.model.enums.FilamentTypeHalogen = "Halogen"

_M_omero.model.enums.FilamentTypeOther = "Other"

_M_omero.model.enums.FilamentTypeUnknown = "Unknown"

# End of module omero.model.enums

__name__ = 'omero.model'

if 'Details' not in _M_omero.model.__dict__:
    _M_omero.model._t_Details = IcePy.declareClass('::omero::model::Details')
    _M_omero.model._t_DetailsPrx = IcePy.declareProxy('::omero::model::Details')

if 'FilamentType' not in _M_omero.model.__dict__:
    _M_omero.model.FilamentType = Ice.createTempClass()
    class FilamentType(_M_omero.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _value=None):
            if Ice.getType(self) == _M_omero.model.FilamentType:
                raise RuntimeError('omero.model.FilamentType is an abstract class')
            _M_omero.model.IObject.__init__(self, _id, _details, _loaded)
            self._value = _value

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::omero::model::FilamentType', '::omero::model::IObject')

        def ice_id(self, current=None):
            return '::omero::model::FilamentType'

        def ice_staticId():
            return '::omero::model::FilamentType'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def setValue(self, theValue, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_omero.model._t_FilamentType)

        __repr__ = __str__

    _M_omero.model.FilamentTypePrx = Ice.createTempClass()
    class FilamentTypePrx(_M_omero.model.IObjectPrx):

        def getValue(self, _ctx=None):
            return _M_omero.model.FilamentType._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.FilamentType._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_omero.model.FilamentType._op_getValue.end(self, _r)

        def setValue(self, theValue, _ctx=None):
            return _M_omero.model.FilamentType._op_setValue.invoke(self, ((theValue, ), _ctx))

        def begin_setValue(self, theValue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.FilamentType._op_setValue.begin(self, ((theValue, ), _response, _ex, _sent, _ctx))

        def end_setValue(self, _r):
            return _M_omero.model.FilamentType._op_setValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_omero.model.FilamentTypePrx.ice_checkedCast(proxy, '::omero::model::FilamentType', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_omero.model.FilamentTypePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::omero::model::FilamentType'
        ice_staticId = staticmethod(ice_staticId)

    _M_omero.model._t_FilamentTypePrx = IcePy.defineProxy('::omero::model::FilamentType', FilamentTypePrx)

    _M_omero.model._t_FilamentType = IcePy.declareClass('::omero::model::FilamentType')

    _M_omero.model._t_FilamentType = IcePy.defineClass('::omero::model::FilamentType', FilamentType, -1, (), True, False, _M_omero.model._t_IObject, (), (('_value', (), _M_omero._t_RString, False, 0),))
    FilamentType._ice_type = _M_omero.model._t_FilamentType

    FilamentType._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RString, False, 0), ())
    FilamentType._op_setValue = IcePy.Operation('setValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RString, False, 0),), (), None, ())

    _M_omero.model.FilamentType = FilamentType
    del FilamentType

    _M_omero.model.FilamentTypePrx = FilamentTypePrx
    del FilamentTypePrx

# End of module omero.model

__name__ = 'omero'

# End of module omero
