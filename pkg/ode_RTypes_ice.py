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

# Start of module ode
_M_ode = Ice.openModule('ode')
__name__ = 'ode'

if 'RType' not in _M_ode.__dict__:
    _M_ode.RType = Ice.createTempClass()
    class RType(Ice.Object):
        """
        Simple base ""protected"" class. Essentially abstract.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.RType:
                raise RuntimeError('ode.RType is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RType'

        def ice_staticId():
            return '::ode::RType'
        ice_staticId = staticmethod(ice_staticId)

        def compare(self, rhs, current=None):
            """
            Equals-like functionality for all RTypes. A return value
            of 0 means they are equivalent and were almost certainly
            created by the same constructor call, e.g.
            rbool(true).compare(rbool(true)) == 0
            This method was originally added (Oct 2008) to force the
            base RType class to be abstract in all languages.
            Arguments:
            rhs -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RType)

        __repr__ = __str__

    _M_ode.RTypePrx = Ice.createTempClass()
    class RTypePrx(Ice.ObjectPrx):

        """
        Equals-like functionality for all RTypes. A return value
        of 0 means they are equivalent and were almost certainly
        created by the same constructor call, e.g.
        rbool(true).compare(rbool(true)) == 0
        This method was originally added (Oct 2008) to force the
        base RType class to be abstract in all languages.
        Arguments:
        rhs -- 
        _ctx -- The request context for the invocation.
        """
        def compare(self, rhs, _ctx=None):
            return _M_ode.RType._op_compare.invoke(self, ((rhs, ), _ctx))

        """
        Equals-like functionality for all RTypes. A return value
        of 0 means they are equivalent and were almost certainly
        created by the same constructor call, e.g.
        rbool(true).compare(rbool(true)) == 0
        This method was originally added (Oct 2008) to force the
        base RType class to be abstract in all languages.
        Arguments:
        rhs -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_compare(self, rhs, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RType._op_compare.begin(self, ((rhs, ), _response, _ex, _sent, _ctx))

        """
        Equals-like functionality for all RTypes. A return value
        of 0 means they are equivalent and were almost certainly
        created by the same constructor call, e.g.
        rbool(true).compare(rbool(true)) == 0
        This method was originally added (Oct 2008) to force the
        base RType class to be abstract in all languages.
        Arguments:
        rhs -- 
        """
        def end_compare(self, _r):
            return _M_ode.RType._op_compare.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RTypePrx.ice_checkedCast(proxy, '::ode::RType', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RTypePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RType'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RTypePrx = IcePy.defineProxy('::ode::RType', RTypePrx)

    _M_ode._t_RType = IcePy.defineClass('::ode::RType', RType, -1, (), True, False, None, (), ())
    RType._ice_type = _M_ode._t_RType

    RType._op_compare = IcePy.Operation('compare', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RType, False, 0),), (), ((), IcePy._t_int, False, 0), ())

    _M_ode.RType = RType
    del RType

    _M_ode.RTypePrx = RTypePrx
    del RTypePrx

if 'RBool' not in _M_ode.__dict__:
    _M_ode.RBool = Ice.createTempClass()
    class RBool(_M_ode.RType):
        """
        Boolean wrapper.
        """
        def __init__(self, _val=False):
            if Ice.getType(self) == _M_ode.RBool:
                raise RuntimeError('ode.RBool is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RBool', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RBool'

        def ice_staticId():
            return '::ode::RBool'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RBool)

        __repr__ = __str__

    _M_ode.RBoolPrx = Ice.createTempClass()
    class RBoolPrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RBool._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RBool._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RBool._op_getValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RBoolPrx.ice_checkedCast(proxy, '::ode::RBool', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RBoolPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RBool'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RBoolPrx = IcePy.defineProxy('::ode::RBool', RBoolPrx)

    _M_ode._t_RBool = IcePy.defineClass('::ode::RBool', RBool, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), IcePy._t_bool, False, 0),))
    RBool._ice_type = _M_ode._t_RBool

    RBool._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())

    _M_ode.RBool = RBool
    del RBool

    _M_ode.RBoolPrx = RBoolPrx
    del RBoolPrx

if 'RDouble' not in _M_ode.__dict__:
    _M_ode.RDouble = Ice.createTempClass()
    class RDouble(_M_ode.RType):
        """
        Double wrapper.
        """
        def __init__(self, _val=0.0):
            if Ice.getType(self) == _M_ode.RDouble:
                raise RuntimeError('ode.RDouble is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RDouble', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RDouble'

        def ice_staticId():
            return '::ode::RDouble'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RDouble)

        __repr__ = __str__

    _M_ode.RDoublePrx = Ice.createTempClass()
    class RDoublePrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RDouble._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RDouble._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RDouble._op_getValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RDoublePrx.ice_checkedCast(proxy, '::ode::RDouble', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RDoublePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RDouble'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RDoublePrx = IcePy.defineProxy('::ode::RDouble', RDoublePrx)

    _M_ode._t_RDouble = IcePy.defineClass('::ode::RDouble', RDouble, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), IcePy._t_double, False, 0),))
    RDouble._ice_type = _M_ode._t_RDouble

    RDouble._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_double, False, 0), ())

    _M_ode.RDouble = RDouble
    del RDouble

    _M_ode.RDoublePrx = RDoublePrx
    del RDoublePrx

if 'RFloat' not in _M_ode.__dict__:
    _M_ode.RFloat = Ice.createTempClass()
    class RFloat(_M_ode.RType):
        """
        Float wrapper.
        """
        def __init__(self, _val=0.0):
            if Ice.getType(self) == _M_ode.RFloat:
                raise RuntimeError('ode.RFloat is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RFloat', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RFloat'

        def ice_staticId():
            return '::ode::RFloat'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RFloat)

        __repr__ = __str__

    _M_ode.RFloatPrx = Ice.createTempClass()
    class RFloatPrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RFloat._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RFloat._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RFloat._op_getValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RFloatPrx.ice_checkedCast(proxy, '::ode::RFloat', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RFloatPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RFloat'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RFloatPrx = IcePy.defineProxy('::ode::RFloat', RFloatPrx)

    _M_ode._t_RFloat = IcePy.defineClass('::ode::RFloat', RFloat, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), IcePy._t_float, False, 0),))
    RFloat._ice_type = _M_ode._t_RFloat

    RFloat._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_float, False, 0), ())

    _M_ode.RFloat = RFloat
    del RFloat

    _M_ode.RFloatPrx = RFloatPrx
    del RFloatPrx

if 'RInt' not in _M_ode.__dict__:
    _M_ode.RInt = Ice.createTempClass()
    class RInt(_M_ode.RType):
        """
        Integer wrapper.
        """
        def __init__(self, _val=0):
            if Ice.getType(self) == _M_ode.RInt:
                raise RuntimeError('ode.RInt is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RInt', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RInt'

        def ice_staticId():
            return '::ode::RInt'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RInt)

        __repr__ = __str__

    _M_ode.RIntPrx = Ice.createTempClass()
    class RIntPrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RInt._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RInt._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RInt._op_getValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RIntPrx.ice_checkedCast(proxy, '::ode::RInt', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RIntPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RInt'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RIntPrx = IcePy.defineProxy('::ode::RInt', RIntPrx)

    _M_ode._t_RInt = IcePy.defineClass('::ode::RInt', RInt, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), IcePy._t_int, False, 0),))
    RInt._ice_type = _M_ode._t_RInt

    RInt._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())

    _M_ode.RInt = RInt
    del RInt

    _M_ode.RIntPrx = RIntPrx
    del RIntPrx

if 'RLong' not in _M_ode.__dict__:
    _M_ode.RLong = Ice.createTempClass()
    class RLong(_M_ode.RType):
        """
        Long Wrapper.
        """
        def __init__(self, _val=0):
            if Ice.getType(self) == _M_ode.RLong:
                raise RuntimeError('ode.RLong is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RLong', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RLong'

        def ice_staticId():
            return '::ode::RLong'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RLong)

        __repr__ = __str__

    _M_ode.RLongPrx = Ice.createTempClass()
    class RLongPrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RLong._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RLong._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RLong._op_getValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RLongPrx.ice_checkedCast(proxy, '::ode::RLong', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RLongPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RLong'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RLongPrx = IcePy.defineProxy('::ode::RLong', RLongPrx)

    _M_ode._t_RLong = IcePy.defineClass('::ode::RLong', RLong, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), IcePy._t_long, False, 0),))
    RLong._ice_type = _M_ode._t_RLong

    RLong._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_long, False, 0), ())

    _M_ode.RLong = RLong
    del RLong

    _M_ode.RLongPrx = RLongPrx
    del RLongPrx

if 'RString' not in _M_ode.__dict__:
    _M_ode.RString = Ice.createTempClass()
    class RString(_M_ode.RType):
        """
        String wrapper.
        """
        def __init__(self, _val=''):
            if Ice.getType(self) == _M_ode.RString:
                raise RuntimeError('ode.RString is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RString', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RString'

        def ice_staticId():
            return '::ode::RString'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RString)

        __repr__ = __str__

    _M_ode.RStringPrx = Ice.createTempClass()
    class RStringPrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RString._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RString._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RString._op_getValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RStringPrx.ice_checkedCast(proxy, '::ode::RString', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RStringPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RString'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RStringPrx = IcePy.defineProxy('::ode::RString', RStringPrx)

    _M_ode._t_RString = IcePy.defineClass('::ode::RString', RString, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), IcePy._t_string, False, 0),))
    RString._ice_type = _M_ode._t_RString

    RString._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_string, False, 0), ())

    _M_ode.RString = RString
    del RString

    _M_ode.RStringPrx = RStringPrx
    del RStringPrx

if 'RClass' not in _M_ode.__dict__:
    _M_ode.RClass = Ice.createTempClass()
    class RClass(_M_ode.RString):
        """
        Extends RString and simply provides runtime
        information to the server that this string
        is intended as a ""protected"" class parameter. Used especially
        by {@code ode.system.ParamMap} (ode/System.ice)
        Usage:
        {@code
        ode::RClass c = ...; // from service
        if (!c.null && c.val.equals("Image")) { ... }
        }
        """
        def __init__(self, _val=''):
            if Ice.getType(self) == _M_ode.RClass:
                raise RuntimeError('ode.RClass is an abstract class')
            _M_ode.RString.__init__(self, _val)

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RClass', '::ode::RString', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RClass'

        def ice_staticId():
            return '::ode::RClass'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RClass)

        __repr__ = __str__

    _M_ode.RClassPrx = Ice.createTempClass()
    class RClassPrx(_M_ode.RStringPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RClassPrx.ice_checkedCast(proxy, '::ode::RClass', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RClassPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RClass'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RClassPrx = IcePy.defineProxy('::ode::RClass', RClassPrx)

    _M_ode._t_RClass = IcePy.defineClass('::ode::RClass', RClass, -1, (), True, False, _M_ode._t_RString, (), ())
    RClass._ice_type = _M_ode._t_RClass

    _M_ode.RClass = RClass
    del RClass

    _M_ode.RClassPrx = RClassPrx
    del RClassPrx

if 'RTime' not in _M_ode.__dict__:
    _M_ode.RTime = Ice.createTempClass()
    class RTime(_M_ode.RType):
        """
        A simple Time implementation. The long value is the number
        of milliseconds since the epoch (January 1, 1970).
        """
        def __init__(self, _val=0):
            if Ice.getType(self) == _M_ode.RTime:
                raise RuntimeError('ode.RTime is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RTime', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RTime'

        def ice_staticId():
            return '::ode::RTime'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RTime)

        __repr__ = __str__

    _M_ode.RTimePrx = Ice.createTempClass()
    class RTimePrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RTime._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RTime._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RTime._op_getValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RTimePrx.ice_checkedCast(proxy, '::ode::RTime', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RTimePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RTime'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RTimePrx = IcePy.defineProxy('::ode::RTime', RTimePrx)

    _M_ode._t_RTime = IcePy.defineClass('::ode::RTime', RTime, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), IcePy._t_long, False, 0),))
    RTime._ice_type = _M_ode._t_RTime

    RTime._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_long, False, 0), ())

    _M_ode.RTime = RTime
    del RTime

    _M_ode.RTimePrx = RTimePrx
    del RTimePrx

if '_t_RTypeSeq' not in _M_ode.__dict__:
    _M_ode._t_RTypeSeq = IcePy.defineSequence('::ode::RTypeSeq', (), _M_ode._t_RType)

if '_t_RTypeSeqSeq' not in _M_ode.__dict__:
    _M_ode._t_RTypeSeqSeq = IcePy.defineSequence('::ode::RTypeSeqSeq', (), _M_ode._t_RTypeSeq)

if 'RCollection' not in _M_ode.__dict__:
    _M_ode.RCollection = Ice.createTempClass()
    class RCollection(_M_ode.RType):
        """
        The collection ""protected"" classes permit the passing of sequences of all
        other RTypes (including other collections) and it is itself nullable. The
        allows for similar arguments to collections in languages with a unified
        inheritance hierarchy (e.g., Java in which all ""protected"" classes extend
        from java.lang.Object).
        Unlike the other rtypes which are used internally within the
        {@code ode.model} classes, these types are mutable since they solely
        pass through the
        This flexible mechanism is not used in all API calls because
        the flexibility brings a performance penalty.
        """
        def __init__(self, _val=None):
            if Ice.getType(self) == _M_ode.RCollection:
                raise RuntimeError('ode.RCollection is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RCollection', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RCollection'

        def ice_staticId():
            return '::ode::RCollection'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def size(self, current=None):
            pass

        def get(self, index, current=None):
            pass

        def add(self, value, current=None):
            pass

        def addAll(self, value, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RCollection)

        __repr__ = __str__

    _M_ode.RCollectionPrx = Ice.createTempClass()
    class RCollectionPrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RCollection._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RCollection._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RCollection._op_getValue.end(self, _r)

        def size(self, _ctx=None):
            return _M_ode.RCollection._op_size.invoke(self, ((), _ctx))

        def begin_size(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RCollection._op_size.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_size(self, _r):
            return _M_ode.RCollection._op_size.end(self, _r)

        def get(self, index, _ctx=None):
            return _M_ode.RCollection._op_get.invoke(self, ((index, ), _ctx))

        def begin_get(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RCollection._op_get.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_get(self, _r):
            return _M_ode.RCollection._op_get.end(self, _r)

        def add(self, value, _ctx=None):
            return _M_ode.RCollection._op_add.invoke(self, ((value, ), _ctx))

        def begin_add(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RCollection._op_add.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        def end_add(self, _r):
            return _M_ode.RCollection._op_add.end(self, _r)

        def addAll(self, value, _ctx=None):
            return _M_ode.RCollection._op_addAll.invoke(self, ((value, ), _ctx))

        def begin_addAll(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RCollection._op_addAll.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        def end_addAll(self, _r):
            return _M_ode.RCollection._op_addAll.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RCollectionPrx.ice_checkedCast(proxy, '::ode::RCollection', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RCollectionPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RCollection'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RCollectionPrx = IcePy.defineProxy('::ode::RCollection', RCollectionPrx)

    _M_ode._t_RCollection = IcePy.declareClass('::ode::RCollection')

    _M_ode._t_RCollection = IcePy.defineClass('::ode::RCollection', RCollection, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), _M_ode._t_RTypeSeq, False, 0),))
    RCollection._ice_type = _M_ode._t_RCollection

    RCollection._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTypeSeq, False, 0), ())
    RCollection._op_size = IcePy.Operation('size', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    RCollection._op_get = IcePy.Operation('get', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode._t_RType, False, 0), ())
    RCollection._op_add = IcePy.Operation('add', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RType, False, 0),), (), None, ())
    RCollection._op_addAll = IcePy.Operation('addAll', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTypeSeq, False, 0),), (), None, ())

    _M_ode.RCollection = RCollection
    del RCollection

    _M_ode.RCollectionPrx = RCollectionPrx
    del RCollectionPrx

if 'RArray' not in _M_ode.__dict__:
    _M_ode.RArray = Ice.createTempClass()
    class RArray(_M_ode.RCollection):
        """
        {@code RCollection} mapped to an array on the server of a type given
        by a random member of the RTypeSeq. Only pass consistent arrays!
        homogeneous lists.
        """
        def __init__(self, _val=None):
            if Ice.getType(self) == _M_ode.RArray:
                raise RuntimeError('ode.RArray is an abstract class')
            _M_ode.RCollection.__init__(self, _val)

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RArray', '::ode::RCollection', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RArray'

        def ice_staticId():
            return '::ode::RArray'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RArray)

        __repr__ = __str__

    _M_ode.RArrayPrx = Ice.createTempClass()
    class RArrayPrx(_M_ode.RCollectionPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RArrayPrx.ice_checkedCast(proxy, '::ode::RArray', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RArrayPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RArray'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RArrayPrx = IcePy.defineProxy('::ode::RArray', RArrayPrx)

    _M_ode._t_RArray = IcePy.declareClass('::ode::RArray')

    _M_ode._t_RArray = IcePy.defineClass('::ode::RArray', RArray, -1, (), True, False, _M_ode._t_RCollection, (), ())
    RArray._ice_type = _M_ode._t_RArray

    _M_ode.RArray = RArray
    del RArray

    _M_ode.RArrayPrx = RArrayPrx
    del RArrayPrx

if 'RList' not in _M_ode.__dict__:
    _M_ode.RList = Ice.createTempClass()
    class RList(_M_ode.RCollection):
        """
        {@code RCollection} mapped to a java.util.List on the server
        """
        def __init__(self, _val=None):
            if Ice.getType(self) == _M_ode.RList:
                raise RuntimeError('ode.RList is an abstract class')
            _M_ode.RCollection.__init__(self, _val)

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RCollection', '::ode::RList', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RList'

        def ice_staticId():
            return '::ode::RList'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RList)

        __repr__ = __str__

    _M_ode.RListPrx = Ice.createTempClass()
    class RListPrx(_M_ode.RCollectionPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RListPrx.ice_checkedCast(proxy, '::ode::RList', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RListPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RList'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RListPrx = IcePy.defineProxy('::ode::RList', RListPrx)

    _M_ode._t_RList = IcePy.declareClass('::ode::RList')

    _M_ode._t_RList = IcePy.defineClass('::ode::RList', RList, -1, (), True, False, _M_ode._t_RCollection, (), ())
    RList._ice_type = _M_ode._t_RList

    _M_ode.RList = RList
    del RList

    _M_ode.RListPrx = RListPrx
    del RListPrx

if 'RSet' not in _M_ode.__dict__:
    _M_ode.RSet = Ice.createTempClass()
    class RSet(_M_ode.RCollection):
        """
        {@code RCollection} mapped to a java.util.HashSet on the server
        """
        def __init__(self, _val=None):
            if Ice.getType(self) == _M_ode.RSet:
                raise RuntimeError('ode.RSet is an abstract class')
            _M_ode.RCollection.__init__(self, _val)

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RCollection', '::ode::RSet', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RSet'

        def ice_staticId():
            return '::ode::RSet'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RSet)

        __repr__ = __str__

    _M_ode.RSetPrx = Ice.createTempClass()
    class RSetPrx(_M_ode.RCollectionPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RSetPrx.ice_checkedCast(proxy, '::ode::RSet', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RSetPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RSet'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RSetPrx = IcePy.defineProxy('::ode::RSet', RSetPrx)

    _M_ode._t_RSet = IcePy.declareClass('::ode::RSet')

    _M_ode._t_RSet = IcePy.defineClass('::ode::RSet', RSet, -1, (), True, False, _M_ode._t_RCollection, (), ())
    RSet._ice_type = _M_ode._t_RSet

    _M_ode.RSet = RSet
    del RSet

    _M_ode.RSetPrx = RSetPrx
    del RSetPrx

if '_t_RTypeDict' not in _M_ode.__dict__:
    _M_ode._t_RTypeDict = IcePy.defineDictionary('::ode::RTypeDict', (), IcePy._t_string, _M_ode._t_RType)

if 'RMap' not in _M_ode.__dict__:
    _M_ode.RMap = Ice.createTempClass()
    class RMap(_M_ode.RType):
        """
        Similar to {@code RCollection}, the {@code RMap} class permits the passing
        of a possible null {@code RTypeDict} where any other {@code RType} is
        expected.
        """
        def __init__(self, _val=None):
            if Ice.getType(self) == _M_ode.RMap:
                raise RuntimeError('ode.RMap is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RMap', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RMap'

        def ice_staticId():
            return '::ode::RMap'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def size(self, current=None):
            pass

        def get(self, key, current=None):
            pass

        def put(self, key, value, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RMap)

        __repr__ = __str__

    _M_ode.RMapPrx = Ice.createTempClass()
    class RMapPrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RMap._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RMap._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RMap._op_getValue.end(self, _r)

        def size(self, _ctx=None):
            return _M_ode.RMap._op_size.invoke(self, ((), _ctx))

        def begin_size(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RMap._op_size.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_size(self, _r):
            return _M_ode.RMap._op_size.end(self, _r)

        def get(self, key, _ctx=None):
            return _M_ode.RMap._op_get.invoke(self, ((key, ), _ctx))

        def begin_get(self, key, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RMap._op_get.begin(self, ((key, ), _response, _ex, _sent, _ctx))

        def end_get(self, _r):
            return _M_ode.RMap._op_get.end(self, _r)

        def put(self, key, value, _ctx=None):
            return _M_ode.RMap._op_put.invoke(self, ((key, value), _ctx))

        def begin_put(self, key, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RMap._op_put.begin(self, ((key, value), _response, _ex, _sent, _ctx))

        def end_put(self, _r):
            return _M_ode.RMap._op_put.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RMapPrx.ice_checkedCast(proxy, '::ode::RMap', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RMapPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RMap'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RMapPrx = IcePy.defineProxy('::ode::RMap', RMapPrx)

    _M_ode._t_RMap = IcePy.declareClass('::ode::RMap')

    _M_ode._t_RMap = IcePy.defineClass('::ode::RMap', RMap, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), _M_ode._t_RTypeDict, False, 0),))
    RMap._ice_type = _M_ode._t_RMap

    RMap._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTypeDict, False, 0), ())
    RMap._op_size = IcePy.Operation('size', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    RMap._op_get = IcePy.Operation('get', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode._t_RType, False, 0), ())
    RMap._op_put = IcePy.Operation('put', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode._t_RType, False, 0)), (), None, ())

    _M_ode.RMap = RMap
    del RMap

    _M_ode.RMapPrx = RMapPrx
    del RMapPrx

# End of module ode
