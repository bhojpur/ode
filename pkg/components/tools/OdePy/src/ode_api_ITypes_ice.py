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
import ode_ModelF_ice
import ode_ServicesF_ice
import ode_Collections_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'ITypes' not in _M_ode.api.__dict__:
    _M_ode.api.ITypes = Ice.createTempClass()
    class ITypes(_M_ode.api.ServiceInterface):
        """
        Access to reflective type information. Also provides simplified
        access to special types like enumerations.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.ITypes:
                raise RuntimeError('ode.api.ITypes is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ITypes', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::ITypes'

        def ice_staticId():
            return '::ode::api::ITypes'
        ice_staticId = staticmethod(ice_staticId)

        def createEnumeration_async(self, _cb, newEnum, current=None):
            pass

        def getEnumeration_async(self, _cb, type, value, current=None):
            """
            Lookup an enumeration value. As with the get-methods of
            ode.api.IQuery queries returning no results
            will through an exception.
            Arguments:
            _cb -- The asynchronous callback object.
            type -- An enumeration class which should be searched.
            value -- The value for which an enumeration should be found.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if ode.model.IEnum is not found.
            """
            pass

        def allEnumerations_async(self, _cb, type, current=None):
            pass

        def updateEnumeration_async(self, _cb, oldEnum, current=None):
            """
            Updates enumeration value specified by object.
            Arguments:
            _cb -- The asynchronous callback object.
            oldEnum -- An enumeration object which should be searched.
            current -- The Current object for the invocation.
            """
            pass

        def updateEnumerations_async(self, _cb, oldEnums, current=None):
            """
            Updates enumeration value specified by object.
            Arguments:
            _cb -- The asynchronous callback object.
            oldEnums -- An enumeration collection of objects which should be searched.
            current -- The Current object for the invocation.
            """
            pass

        def deleteEnumeration_async(self, _cb, oldEnum, current=None):
            """
            Deletes enumeration value specified by object.
            Arguments:
            _cb -- The asynchronous callback object.
            oldEnum -- An enumeration object which should be searched.
            current -- The Current object for the invocation.
            """
            pass

        def getEnumerationTypes_async(self, _cb, current=None):
            """
            Gets all metadata classes which are IEnum type.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            RuntimeException -- if Class not found.
            """
            pass

        def getAnnotationTypes_async(self, _cb, current=None):
            """
            Returns a list of classes which implement
            ode.model.IAnnotated. These can
            be used in combination with ode.api.Search.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getEnumerationsWithEntries_async(self, _cb, current=None):
            """
            Gets all metadata classes which are IEnum type with
            contained objects.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            RuntimeException -- if xml parsing failure.
            """
            pass

        def getOriginalEnumerations_async(self, _cb, current=None):
            """
            Gets all original values.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            RuntimeException -- if xml parsing failure.
            """
            pass

        def resetEnumerations_async(self, _cb, enumClass, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_ITypes)

        __repr__ = __str__

    _M_ode.api.ITypesPrx = Ice.createTempClass()
    class ITypesPrx(_M_ode.api.ServiceInterfacePrx):

        def createEnumeration(self, newEnum, _ctx=None):
            return _M_ode.api.ITypes._op_createEnumeration.invoke(self, ((newEnum, ), _ctx))

        def begin_createEnumeration(self, newEnum, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_createEnumeration.begin(self, ((newEnum, ), _response, _ex, _sent, _ctx))

        def end_createEnumeration(self, _r):
            return _M_ode.api.ITypes._op_createEnumeration.end(self, _r)

        """
        Lookup an enumeration value. As with the get-methods of
        ode.api.IQuery queries returning no results
        will through an exception.
        Arguments:
        type -- An enumeration class which should be searched.
        value -- The value for which an enumeration should be found.
        _ctx -- The request context for the invocation.
        Returns: A managed enumeration. Never null.
        Throws:
        ApiUsageException -- if ode.model.IEnum is not found.
        """
        def getEnumeration(self, type, value, _ctx=None):
            return _M_ode.api.ITypes._op_getEnumeration.invoke(self, ((type, value), _ctx))

        """
        Lookup an enumeration value. As with the get-methods of
        ode.api.IQuery queries returning no results
        will through an exception.
        Arguments:
        type -- An enumeration class which should be searched.
        value -- The value for which an enumeration should be found.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getEnumeration(self, type, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_getEnumeration.begin(self, ((type, value), _response, _ex, _sent, _ctx))

        """
        Lookup an enumeration value. As with the get-methods of
        ode.api.IQuery queries returning no results
        will through an exception.
        Arguments:
        type -- An enumeration class which should be searched.
        value -- The value for which an enumeration should be found.
        Returns: A managed enumeration. Never null.
        Throws:
        ApiUsageException -- if ode.model.IEnum is not found.
        """
        def end_getEnumeration(self, _r):
            return _M_ode.api.ITypes._op_getEnumeration.end(self, _r)

        def allEnumerations(self, type, _ctx=None):
            return _M_ode.api.ITypes._op_allEnumerations.invoke(self, ((type, ), _ctx))

        def begin_allEnumerations(self, type, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_allEnumerations.begin(self, ((type, ), _response, _ex, _sent, _ctx))

        def end_allEnumerations(self, _r):
            return _M_ode.api.ITypes._op_allEnumerations.end(self, _r)

        """
        Updates enumeration value specified by object.
        Arguments:
        oldEnum -- An enumeration object which should be searched.
        _ctx -- The request context for the invocation.
        Returns: A managed enumeration. Never null.
        """
        def updateEnumeration(self, oldEnum, _ctx=None):
            return _M_ode.api.ITypes._op_updateEnumeration.invoke(self, ((oldEnum, ), _ctx))

        """
        Updates enumeration value specified by object.
        Arguments:
        oldEnum -- An enumeration object which should be searched.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_updateEnumeration(self, oldEnum, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_updateEnumeration.begin(self, ((oldEnum, ), _response, _ex, _sent, _ctx))

        """
        Updates enumeration value specified by object.
        Arguments:
        oldEnum -- An enumeration object which should be searched.
        Returns: A managed enumeration. Never null.
        """
        def end_updateEnumeration(self, _r):
            return _M_ode.api.ITypes._op_updateEnumeration.end(self, _r)

        """
        Updates enumeration value specified by object.
        Arguments:
        oldEnums -- An enumeration collection of objects which should be searched.
        _ctx -- The request context for the invocation.
        """
        def updateEnumerations(self, oldEnums, _ctx=None):
            return _M_ode.api.ITypes._op_updateEnumerations.invoke(self, ((oldEnums, ), _ctx))

        """
        Updates enumeration value specified by object.
        Arguments:
        oldEnums -- An enumeration collection of objects which should be searched.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_updateEnumerations(self, oldEnums, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_updateEnumerations.begin(self, ((oldEnums, ), _response, _ex, _sent, _ctx))

        """
        Updates enumeration value specified by object.
        Arguments:
        oldEnums -- An enumeration collection of objects which should be searched.
        """
        def end_updateEnumerations(self, _r):
            return _M_ode.api.ITypes._op_updateEnumerations.end(self, _r)

        """
        Deletes enumeration value specified by object.
        Arguments:
        oldEnum -- An enumeration object which should be searched.
        _ctx -- The request context for the invocation.
        """
        def deleteEnumeration(self, oldEnum, _ctx=None):
            return _M_ode.api.ITypes._op_deleteEnumeration.invoke(self, ((oldEnum, ), _ctx))

        """
        Deletes enumeration value specified by object.
        Arguments:
        oldEnum -- An enumeration object which should be searched.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_deleteEnumeration(self, oldEnum, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_deleteEnumeration.begin(self, ((oldEnum, ), _response, _ex, _sent, _ctx))

        """
        Deletes enumeration value specified by object.
        Arguments:
        oldEnum -- An enumeration object which should be searched.
        """
        def end_deleteEnumeration(self, _r):
            return _M_ode.api.ITypes._op_deleteEnumeration.end(self, _r)

        """
        Gets all metadata classes which are IEnum type.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: set of classes that extend IEnum
        Throws:
        RuntimeException -- if Class not found.
        """
        def getEnumerationTypes(self, _ctx=None):
            return _M_ode.api.ITypes._op_getEnumerationTypes.invoke(self, ((), _ctx))

        """
        Gets all metadata classes which are IEnum type.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getEnumerationTypes(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_getEnumerationTypes.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Gets all metadata classes which are IEnum type.
        Arguments:
        Returns: set of classes that extend IEnum
        Throws:
        RuntimeException -- if Class not found.
        """
        def end_getEnumerationTypes(self, _r):
            return _M_ode.api.ITypes._op_getEnumerationTypes.end(self, _r)

        """
        Returns a list of classes which implement
        ode.model.IAnnotated. These can
        be used in combination with ode.api.Search.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: a java.util.Set of ode.model.IAnnotated implementations
        """
        def getAnnotationTypes(self, _ctx=None):
            return _M_ode.api.ITypes._op_getAnnotationTypes.invoke(self, ((), _ctx))

        """
        Returns a list of classes which implement
        ode.model.IAnnotated. These can
        be used in combination with ode.api.Search.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getAnnotationTypes(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_getAnnotationTypes.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns a list of classes which implement
        ode.model.IAnnotated. These can
        be used in combination with ode.api.Search.
        Arguments:
        Returns: a java.util.Set of ode.model.IAnnotated implementations
        """
        def end_getAnnotationTypes(self, _r):
            return _M_ode.api.ITypes._op_getAnnotationTypes.end(self, _r)

        """
        Gets all metadata classes which are IEnum type with
        contained objects.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: map of classes that extend IEnum
        Throws:
        RuntimeException -- if xml parsing failure.
        """
        def getEnumerationsWithEntries(self, _ctx=None):
            return _M_ode.api.ITypes._op_getEnumerationsWithEntries.invoke(self, ((), _ctx))

        """
        Gets all metadata classes which are IEnum type with
        contained objects.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getEnumerationsWithEntries(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_getEnumerationsWithEntries.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Gets all metadata classes which are IEnum type with
        contained objects.
        Arguments:
        Returns: map of classes that extend IEnum
        Throws:
        RuntimeException -- if xml parsing failure.
        """
        def end_getEnumerationsWithEntries(self, _r):
            return _M_ode.api.ITypes._op_getEnumerationsWithEntries.end(self, _r)

        """
        Gets all original values.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: A list of managed enumerations.
        Throws:
        RuntimeException -- if xml parsing failure.
        """
        def getOriginalEnumerations(self, _ctx=None):
            return _M_ode.api.ITypes._op_getOriginalEnumerations.invoke(self, ((), _ctx))

        """
        Gets all original values.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getOriginalEnumerations(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_getOriginalEnumerations.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Gets all original values.
        Arguments:
        Returns: A list of managed enumerations.
        Throws:
        RuntimeException -- if xml parsing failure.
        """
        def end_getOriginalEnumerations(self, _r):
            return _M_ode.api.ITypes._op_getOriginalEnumerations.end(self, _r)

        def resetEnumerations(self, enumClass, _ctx=None):
            return _M_ode.api.ITypes._op_resetEnumerations.invoke(self, ((enumClass, ), _ctx))

        def begin_resetEnumerations(self, enumClass, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ITypes._op_resetEnumerations.begin(self, ((enumClass, ), _response, _ex, _sent, _ctx))

        def end_resetEnumerations(self, _r):
            return _M_ode.api.ITypes._op_resetEnumerations.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ITypesPrx.ice_checkedCast(proxy, '::ode::api::ITypes', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ITypesPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::ITypes'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ITypesPrx = IcePy.defineProxy('::ode::api::ITypes', ITypesPrx)

    _M_ode.api._t_ITypes = IcePy.defineClass('::ode::api::ITypes', ITypes, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    ITypes._ice_type = _M_ode.api._t_ITypes

    ITypes._op_createEnumeration = IcePy.Operation('createEnumeration', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    ITypes._op_getEnumeration = IcePy.Operation('getEnumeration', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    ITypes._op_allEnumerations = IcePy.Operation('allEnumerations', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    ITypes._op_updateEnumeration = IcePy.Operation('updateEnumeration', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    ITypes._op_updateEnumerations = IcePy.Operation('updateEnumerations', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0),), (), None, (_M_ode._t_ServerError,))
    ITypes._op_deleteEnumeration = IcePy.Operation('deleteEnumeration', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), None, (_M_ode._t_ServerError,))
    ITypes._op_getEnumerationTypes = IcePy.Operation('getEnumerationTypes', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_StringSet, False, 0), (_M_ode._t_ServerError,))
    ITypes._op_getAnnotationTypes = IcePy.Operation('getAnnotationTypes', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_StringSet, False, 0), (_M_ode._t_ServerError,))
    ITypes._op_getEnumerationsWithEntries = IcePy.Operation('getEnumerationsWithEntries', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_IObjectListMap, False, 0), (_M_ode._t_ServerError,))
    ITypes._op_getOriginalEnumerations = IcePy.Operation('getOriginalEnumerations', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    ITypes._op_resetEnumerations = IcePy.Operation('resetEnumerations', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_ServerError,))

    _M_ode.api.ITypes = ITypes
    del ITypes

    _M_ode.api.ITypesPrx = ITypesPrx
    del ITypesPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
