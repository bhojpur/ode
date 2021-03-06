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

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'AffineTransform' not in _M_ode.model.__dict__:
    _M_ode.model.AffineTransform = Ice.createTempClass()
    class AffineTransform(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _a00=None, _a10=None, _a01=None, _a11=None, _a02=None, _a12=None):
            if Ice.getType(self) == _M_ode.model.AffineTransform:
                raise RuntimeError('ode.model.AffineTransform is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._a00 = _a00
            self._a10 = _a10
            self._a01 = _a01
            self._a11 = _a11
            self._a02 = _a02
            self._a12 = _a12

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::AffineTransform', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::AffineTransform'

        def ice_staticId():
            return '::ode::model::AffineTransform'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getA00(self, current=None):
            pass

        def setA00(self, theA00, current=None):
            pass

        def getA10(self, current=None):
            pass

        def setA10(self, theA10, current=None):
            pass

        def getA01(self, current=None):
            pass

        def setA01(self, theA01, current=None):
            pass

        def getA11(self, current=None):
            pass

        def setA11(self, theA11, current=None):
            pass

        def getA02(self, current=None):
            pass

        def setA02(self, theA02, current=None):
            pass

        def getA12(self, current=None):
            pass

        def setA12(self, theA12, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_AffineTransform)

        __repr__ = __str__

    _M_ode.model.AffineTransformPrx = Ice.createTempClass()
    class AffineTransformPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.AffineTransform._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.AffineTransform._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.AffineTransform._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.AffineTransform._op_setVersion.end(self, _r)

        def getA00(self, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA00.invoke(self, ((), _ctx))

        def begin_getA00(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA00.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getA00(self, _r):
            return _M_ode.model.AffineTransform._op_getA00.end(self, _r)

        def setA00(self, theA00, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA00.invoke(self, ((theA00, ), _ctx))

        def begin_setA00(self, theA00, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA00.begin(self, ((theA00, ), _response, _ex, _sent, _ctx))

        def end_setA00(self, _r):
            return _M_ode.model.AffineTransform._op_setA00.end(self, _r)

        def getA10(self, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA10.invoke(self, ((), _ctx))

        def begin_getA10(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA10.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getA10(self, _r):
            return _M_ode.model.AffineTransform._op_getA10.end(self, _r)

        def setA10(self, theA10, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA10.invoke(self, ((theA10, ), _ctx))

        def begin_setA10(self, theA10, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA10.begin(self, ((theA10, ), _response, _ex, _sent, _ctx))

        def end_setA10(self, _r):
            return _M_ode.model.AffineTransform._op_setA10.end(self, _r)

        def getA01(self, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA01.invoke(self, ((), _ctx))

        def begin_getA01(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA01.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getA01(self, _r):
            return _M_ode.model.AffineTransform._op_getA01.end(self, _r)

        def setA01(self, theA01, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA01.invoke(self, ((theA01, ), _ctx))

        def begin_setA01(self, theA01, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA01.begin(self, ((theA01, ), _response, _ex, _sent, _ctx))

        def end_setA01(self, _r):
            return _M_ode.model.AffineTransform._op_setA01.end(self, _r)

        def getA11(self, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA11.invoke(self, ((), _ctx))

        def begin_getA11(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA11.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getA11(self, _r):
            return _M_ode.model.AffineTransform._op_getA11.end(self, _r)

        def setA11(self, theA11, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA11.invoke(self, ((theA11, ), _ctx))

        def begin_setA11(self, theA11, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA11.begin(self, ((theA11, ), _response, _ex, _sent, _ctx))

        def end_setA11(self, _r):
            return _M_ode.model.AffineTransform._op_setA11.end(self, _r)

        def getA02(self, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA02.invoke(self, ((), _ctx))

        def begin_getA02(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA02.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getA02(self, _r):
            return _M_ode.model.AffineTransform._op_getA02.end(self, _r)

        def setA02(self, theA02, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA02.invoke(self, ((theA02, ), _ctx))

        def begin_setA02(self, theA02, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA02.begin(self, ((theA02, ), _response, _ex, _sent, _ctx))

        def end_setA02(self, _r):
            return _M_ode.model.AffineTransform._op_setA02.end(self, _r)

        def getA12(self, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA12.invoke(self, ((), _ctx))

        def begin_getA12(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_getA12.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getA12(self, _r):
            return _M_ode.model.AffineTransform._op_getA12.end(self, _r)

        def setA12(self, theA12, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA12.invoke(self, ((theA12, ), _ctx))

        def begin_setA12(self, theA12, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.AffineTransform._op_setA12.begin(self, ((theA12, ), _response, _ex, _sent, _ctx))

        def end_setA12(self, _r):
            return _M_ode.model.AffineTransform._op_setA12.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.AffineTransformPrx.ice_checkedCast(proxy, '::ode::model::AffineTransform', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.AffineTransformPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::AffineTransform'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_AffineTransformPrx = IcePy.defineProxy('::ode::model::AffineTransform', AffineTransformPrx)

    _M_ode.model._t_AffineTransform = IcePy.declareClass('::ode::model::AffineTransform')

    _M_ode.model._t_AffineTransform = IcePy.defineClass('::ode::model::AffineTransform', AffineTransform, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_a00', (), _M_ode._t_RDouble, False, 0),
        ('_a10', (), _M_ode._t_RDouble, False, 0),
        ('_a01', (), _M_ode._t_RDouble, False, 0),
        ('_a11', (), _M_ode._t_RDouble, False, 0),
        ('_a02', (), _M_ode._t_RDouble, False, 0),
        ('_a12', (), _M_ode._t_RDouble, False, 0)
    ))
    AffineTransform._ice_type = _M_ode.model._t_AffineTransform

    AffineTransform._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    AffineTransform._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    AffineTransform._op_getA00 = IcePy.Operation('getA00', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    AffineTransform._op_setA00 = IcePy.Operation('setA00', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    AffineTransform._op_getA10 = IcePy.Operation('getA10', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    AffineTransform._op_setA10 = IcePy.Operation('setA10', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    AffineTransform._op_getA01 = IcePy.Operation('getA01', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    AffineTransform._op_setA01 = IcePy.Operation('setA01', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    AffineTransform._op_getA11 = IcePy.Operation('getA11', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    AffineTransform._op_setA11 = IcePy.Operation('setA11', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    AffineTransform._op_getA02 = IcePy.Operation('getA02', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    AffineTransform._op_setA02 = IcePy.Operation('setA02', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())
    AffineTransform._op_getA12 = IcePy.Operation('getA12', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RDouble, False, 0), ())
    AffineTransform._op_setA12 = IcePy.Operation('setA12', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RDouble, False, 0),), (), None, ())

    _M_ode.model.AffineTransform = AffineTransform
    del AffineTransform

    _M_ode.model.AffineTransformPrx = AffineTransformPrx
    del AffineTransformPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
