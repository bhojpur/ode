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
import ode_System_ice
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

if 'IQuery' not in _M_ode.api.__dict__:
    _M_ode.api.IQuery = Ice.createTempClass()
    class IQuery(_M_ode.api.ServiceInterface):
        """
        Provides methods for directly querying object graphs. As far as is
        possible, IQuery should be considered the lowest level DB-access
        (SELECT) interface.
        Unlike the ode.api.IUpdate interface, using other methods
        will most likely not leave the database in an inconsistent state,
        but may provide stale data in some situations.
        By convention, all methods that begin with get will
        never return a null or empty java.util.Collection, but
        instead will throw a ode.ValidationException.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IQuery:
                raise RuntimeError('ode.api.IQuery is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IQuery', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IQuery'

        def ice_staticId():
            return '::ode::api::IQuery'
        ice_staticId = staticmethod(ice_staticId)

        def get_async(self, _cb, klass, id, current=None):
            """
            Looks up an entity by class and id. If no such object
            exists, an exception will be thrown.
            Arguments:
            _cb -- The asynchronous callback object.
            klass -- the type of the entity. Not null.
            id -- the entity's id
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the id doesn't exist.
            """
            pass

        def find_async(self, _cb, klass, id, current=None):
            """
            Looks up an entity by class and id. If no such objects
            exists, return a null.
            Arguments:
            _cb -- The asynchronous callback object.
            klass -- klass the type of the entity. Not null.
            id -- the entity's id
            current -- The Current object for the invocation.
            """
            pass

        def findAll_async(self, _cb, klass, filter, current=None):
            """
            Looks up all entities that belong to this class and match
            filter.
            Arguments:
            _cb -- The asynchronous callback object.
            klass -- entity type to be searched. Not null.
            filter -- filters the result set. Can be null.
            current -- The Current object for the invocation.
            """
            pass

        def findByExample_async(self, _cb, example, current=None):
            """
            Searches based on provided example entity. The example
            entity should uniquely specify the entity or an
            exception will be thrown.
            Note: findByExample does not operate on the id
            field. For that, use {@code find}, {@code get},
            {@code findByQuery}, or {@code findAllByQuery}.
            Arguments:
            _cb -- The asynchronous callback object.
            example -- Non-null example object.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if more than one result is return.
            """
            pass

        def findAllByExample_async(self, _cb, example, filter, current=None):
            """
            Searches based on provided example entity. The returned
            entities will be limited by the ode.sys.Filter
            object.
            Note: findAllbyExample does not operate on the
            id field.
            For that, use {@code find}, {@code get},
            {@code findByQuery}, or {@code findAllByQuery}.
            Arguments:
            _cb -- The asynchronous callback object.
            example -- Non-null example object.
            filter -- filters the result set. Can be null.
            current -- The Current object for the invocation.
            """
            pass

        def findByString_async(self, _cb, klass, field, value, current=None):
            """
            Searches a given field matching against a String. Method
            does not allow for case sensitive or insensitive
            searching since this is essentially a lookup. The existence
            of more than one result will result in an exception.
            Arguments:
            _cb -- The asynchronous callback object.
            klass -- type of entity to be searched
            field -- the name of the field, either as simple string or as public static final from the entity class, e.g. {@code ode.model.Project.NAME}
            value -- String used for search.
            current -- The Current object for the invocation.
            Throws:
            ode.conditions.ApiUsageException -- if more than one result.
            """
            pass

        def findAllByString_async(self, _cb, klass, field, value, caseSensitive, filter, current=None):
            """
            Searches a given field matching against a String. Method
            allows for case sensitive or insensitive searching using
            the (I)LIKE comparators. Result set will be reduced by the
            ode.sys.Filter instance.
            Arguments:
            _cb -- The asynchronous callback object.
            klass -- type of entity to be searched. Not null.
            field -- the name of the field, either as simple string or as public static final from the entity class, e.g. {@code ode.model.Project.NAME}. Not null.
            value -- String used for search. Not null.
            caseSensitive -- whether to use LIKE or ILIKE
            filter -- filters the result set. Can be null.
            current -- The Current object for the invocation.
            """
            pass

        def findByQuery_async(self, _cb, query, params, current=None):
            """
            Executes the stored query with the given name. If a query
            with the name cannot be found, an exception will be thrown.
            The queryName parameter can be an actual query String if the
            StringQuerySource is configured on the server and the user
            running the query has proper permissions.
            Arguments:
            _cb -- The asynchronous callback object.
            query -- Query to execute
            params -- 
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- 
            """
            pass

        def findAllByQuery_async(self, _cb, query, params, current=None):
            """
            Executes the stored query with the given name. If a query
            with the name cannot be found, an exception will be thrown.
            The queryName parameter can be an actual query String if the
            StringQuerySource is configured on the server and the user
            running the query has proper permissions.
            Queries can only return lists of
            ode.model.IObject instances. This means
            all must be of the form:
            select this from SomeModelClass this ...
            though the alias this is unimportant. Do not try to
            return multiple classes in one call like:
            select this, that from SomeClass this, SomeOtherClass that ...
            nor to project values out of an object:
            select this.name from SomeClass this ...
            If a page is desired, add it to the query parameters.
            Arguments:
            _cb -- The asynchronous callback object.
            query -- Query to execute. Not null.
            params -- 
            current -- The Current object for the invocation.
            """
            pass

        def findAllByFullText_async(self, _cb, klass, query, params, current=None):
            """
            Executes a full text search based on Lucene. Each term in
            the query can also be prefixed by the name of the field to
            which is should be restricted.
            Examples:
            owner:root AND annotation:someTag
            file:xml AND name:*hoechst*
            For more information, see
            Query Parser Syntax
            The return values are first filtered by the security system.
            Arguments:
            _cb -- The asynchronous callback object.
            klass -- A non-null class specification of which type should be searched.
            query -- A non-null query string. An empty string will return no results.
            params -- Currently the parameters themselves are unused. But the ode.sys.Parameters#theFilter can be used to limit the number of results returned (ode.sys.Filter#limit) or the user for who the results will be found (ode.sys.Filter#ownerId).
            current -- The Current object for the invocation.
            """
            pass

        def projection_async(self, _cb, query, params, current=None):
            """
            Return a sequence of ode.RType sequences.
            Each element of the outer sequence is one row in the return
            value.
            Each element of the inner sequence is one column specified
            in the HQL.
            ode.model.IObject instances are returned wrapped
            in an ode.RObject instance. Primitives are
            mapped to the expected ode.RType subclass. Types
            without an ode.RType mapper if returned will
            throw an exception if present in the select except where a
            manual conversion is present on the server. This includes:
            ode.model.Permissions instances are
            serialized to an ode.RMap containing the
            keys: perms, canAnnotate, canEdit, canLink, canDelete,
            canChgrp, canChown
            The quantity types like ode.model.Length are
            serialized to an ode.RMap containing the
            keys: value, unit, symbol
            As with SQL, if an aggregation statement is used, a group
            by clause must be added.
            Examples:
            select i.name, i.description from Image i where i.name like '%.dv'
            select tag.textValue, tagset.textValue from TagAnnotation tag join tag.annotationLinks l join l.child tagset
            select p.pixelsType.value, count(p.id) from Pixel p group by p.pixelsType.value
            Arguments:
            _cb -- The asynchronous callback object.
            query -- 
            params -- 
            current -- The Current object for the invocation.
            """
            pass

        def refresh_async(self, _cb, iObject, current=None):
            """
            Refreshes an entire ode.model.IObject graph,
            recursive loading all data for the managed instances in the
            graph from the database. If any non-managed entities are
            detected (e.g. without ids), an
            ode.ApiUsageException will be thrown.
            Arguments:
            _cb -- The asynchronous callback object.
            iObject -- Non-null managed ode.model.IObject graph which should have all values re-assigned from the database
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if any non-managed entities are found.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IQuery)

        __repr__ = __str__

    _M_ode.api.IQueryPrx = Ice.createTempClass()
    class IQueryPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Looks up an entity by class and id. If no such object
        exists, an exception will be thrown.
        Arguments:
        klass -- the type of the entity. Not null.
        id -- the entity's id
        _ctx -- The request context for the invocation.
        Returns: an initialized entity
        Throws:
        ValidationException -- if the id doesn't exist.
        """
        def get(self, klass, id, _ctx=None):
            return _M_ode.api.IQuery._op_get.invoke(self, ((klass, id), _ctx))

        """
        Looks up an entity by class and id. If no such object
        exists, an exception will be thrown.
        Arguments:
        klass -- the type of the entity. Not null.
        id -- the entity's id
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_get(self, klass, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_get.begin(self, ((klass, id), _response, _ex, _sent, _ctx))

        """
        Looks up an entity by class and id. If no such object
        exists, an exception will be thrown.
        Arguments:
        klass -- the type of the entity. Not null.
        id -- the entity's id
        Returns: an initialized entity
        Throws:
        ValidationException -- if the id doesn't exist.
        """
        def end_get(self, _r):
            return _M_ode.api.IQuery._op_get.end(self, _r)

        """
        Looks up an entity by class and id. If no such objects
        exists, return a null.
        Arguments:
        klass -- klass the type of the entity. Not null.
        id -- the entity's id
        _ctx -- The request context for the invocation.
        Returns: an initialized entity or null if id doesn't exist.
        """
        def find(self, klass, id, _ctx=None):
            return _M_ode.api.IQuery._op_find.invoke(self, ((klass, id), _ctx))

        """
        Looks up an entity by class and id. If no such objects
        exists, return a null.
        Arguments:
        klass -- klass the type of the entity. Not null.
        id -- the entity's id
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_find(self, klass, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_find.begin(self, ((klass, id), _response, _ex, _sent, _ctx))

        """
        Looks up an entity by class and id. If no such objects
        exists, return a null.
        Arguments:
        klass -- klass the type of the entity. Not null.
        id -- the entity's id
        Returns: an initialized entity or null if id doesn't exist.
        """
        def end_find(self, _r):
            return _M_ode.api.IQuery._op_find.end(self, _r)

        """
        Looks up all entities that belong to this class and match
        filter.
        Arguments:
        klass -- entity type to be searched. Not null.
        filter -- filters the result set. Can be null.
        _ctx -- The request context for the invocation.
        Returns: a collection if initialized entities or an empty List if none exist.
        """
        def findAll(self, klass, filter, _ctx=None):
            return _M_ode.api.IQuery._op_findAll.invoke(self, ((klass, filter), _ctx))

        """
        Looks up all entities that belong to this class and match
        filter.
        Arguments:
        klass -- entity type to be searched. Not null.
        filter -- filters the result set. Can be null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findAll(self, klass, filter, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_findAll.begin(self, ((klass, filter), _response, _ex, _sent, _ctx))

        """
        Looks up all entities that belong to this class and match
        filter.
        Arguments:
        klass -- entity type to be searched. Not null.
        filter -- filters the result set. Can be null.
        Returns: a collection if initialized entities or an empty List if none exist.
        """
        def end_findAll(self, _r):
            return _M_ode.api.IQuery._op_findAll.end(self, _r)

        """
        Searches based on provided example entity. The example
        entity should uniquely specify the entity or an
        exception will be thrown.
        Note: findByExample does not operate on the id
        field. For that, use {@code find}, {@code get},
        {@code findByQuery}, or {@code findAllByQuery}.
        Arguments:
        example -- Non-null example object.
        _ctx -- The request context for the invocation.
        Returns: Possibly null IObject result.
        Throws:
        ApiUsageException -- if more than one result is return.
        """
        def findByExample(self, example, _ctx=None):
            return _M_ode.api.IQuery._op_findByExample.invoke(self, ((example, ), _ctx))

        """
        Searches based on provided example entity. The example
        entity should uniquely specify the entity or an
        exception will be thrown.
        Note: findByExample does not operate on the id
        field. For that, use {@code find}, {@code get},
        {@code findByQuery}, or {@code findAllByQuery}.
        Arguments:
        example -- Non-null example object.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findByExample(self, example, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_findByExample.begin(self, ((example, ), _response, _ex, _sent, _ctx))

        """
        Searches based on provided example entity. The example
        entity should uniquely specify the entity or an
        exception will be thrown.
        Note: findByExample does not operate on the id
        field. For that, use {@code find}, {@code get},
        {@code findByQuery}, or {@code findAllByQuery}.
        Arguments:
        example -- Non-null example object.
        Returns: Possibly null IObject result.
        Throws:
        ApiUsageException -- if more than one result is return.
        """
        def end_findByExample(self, _r):
            return _M_ode.api.IQuery._op_findByExample.end(self, _r)

        """
        Searches based on provided example entity. The returned
        entities will be limited by the ode.sys.Filter
        object.
        Note: findAllbyExample does not operate on the
        id field.
        For that, use {@code find}, {@code get},
        {@code findByQuery}, or {@code findAllByQuery}.
        Arguments:
        example -- Non-null example object.
        filter -- filters the result set. Can be null.
        _ctx -- The request context for the invocation.
        Returns: Possibly empty List of IObject results.
        """
        def findAllByExample(self, example, filter, _ctx=None):
            return _M_ode.api.IQuery._op_findAllByExample.invoke(self, ((example, filter), _ctx))

        """
        Searches based on provided example entity. The returned
        entities will be limited by the ode.sys.Filter
        object.
        Note: findAllbyExample does not operate on the
        id field.
        For that, use {@code find}, {@code get},
        {@code findByQuery}, or {@code findAllByQuery}.
        Arguments:
        example -- Non-null example object.
        filter -- filters the result set. Can be null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findAllByExample(self, example, filter, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_findAllByExample.begin(self, ((example, filter), _response, _ex, _sent, _ctx))

        """
        Searches based on provided example entity. The returned
        entities will be limited by the ode.sys.Filter
        object.
        Note: findAllbyExample does not operate on the
        id field.
        For that, use {@code find}, {@code get},
        {@code findByQuery}, or {@code findAllByQuery}.
        Arguments:
        example -- Non-null example object.
        filter -- filters the result set. Can be null.
        Returns: Possibly empty List of IObject results.
        """
        def end_findAllByExample(self, _r):
            return _M_ode.api.IQuery._op_findAllByExample.end(self, _r)

        """
        Searches a given field matching against a String. Method
        does not allow for case sensitive or insensitive
        searching since this is essentially a lookup. The existence
        of more than one result will result in an exception.
        Arguments:
        klass -- type of entity to be searched
        field -- the name of the field, either as simple string or as public static final from the entity class, e.g. {@code ode.model.Project.NAME}
        value -- String used for search.
        _ctx -- The request context for the invocation.
        Returns: found entity or possibly null.
        Throws:
        ode.conditions.ApiUsageException -- if more than one result.
        """
        def findByString(self, klass, field, value, _ctx=None):
            return _M_ode.api.IQuery._op_findByString.invoke(self, ((klass, field, value), _ctx))

        """
        Searches a given field matching against a String. Method
        does not allow for case sensitive or insensitive
        searching since this is essentially a lookup. The existence
        of more than one result will result in an exception.
        Arguments:
        klass -- type of entity to be searched
        field -- the name of the field, either as simple string or as public static final from the entity class, e.g. {@code ode.model.Project.NAME}
        value -- String used for search.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findByString(self, klass, field, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_findByString.begin(self, ((klass, field, value), _response, _ex, _sent, _ctx))

        """
        Searches a given field matching against a String. Method
        does not allow for case sensitive or insensitive
        searching since this is essentially a lookup. The existence
        of more than one result will result in an exception.
        Arguments:
        klass -- type of entity to be searched
        field -- the name of the field, either as simple string or as public static final from the entity class, e.g. {@code ode.model.Project.NAME}
        value -- String used for search.
        Returns: found entity or possibly null.
        Throws:
        ode.conditions.ApiUsageException -- if more than one result.
        """
        def end_findByString(self, _r):
            return _M_ode.api.IQuery._op_findByString.end(self, _r)

        """
        Searches a given field matching against a String. Method
        allows for case sensitive or insensitive searching using
        the (I)LIKE comparators. Result set will be reduced by the
        ode.sys.Filter instance.
        Arguments:
        klass -- type of entity to be searched. Not null.
        field -- the name of the field, either as simple string or as public static final from the entity class, e.g. {@code ode.model.Project.NAME}. Not null.
        value -- String used for search. Not null.
        caseSensitive -- whether to use LIKE or ILIKE
        filter -- filters the result set. Can be null.
        _ctx -- The request context for the invocation.
        Returns: A list (possibly empty) with the results.
        """
        def findAllByString(self, klass, field, value, caseSensitive, filter, _ctx=None):
            return _M_ode.api.IQuery._op_findAllByString.invoke(self, ((klass, field, value, caseSensitive, filter), _ctx))

        """
        Searches a given field matching against a String. Method
        allows for case sensitive or insensitive searching using
        the (I)LIKE comparators. Result set will be reduced by the
        ode.sys.Filter instance.
        Arguments:
        klass -- type of entity to be searched. Not null.
        field -- the name of the field, either as simple string or as public static final from the entity class, e.g. {@code ode.model.Project.NAME}. Not null.
        value -- String used for search. Not null.
        caseSensitive -- whether to use LIKE or ILIKE
        filter -- filters the result set. Can be null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findAllByString(self, klass, field, value, caseSensitive, filter, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_findAllByString.begin(self, ((klass, field, value, caseSensitive, filter), _response, _ex, _sent, _ctx))

        """
        Searches a given field matching against a String. Method
        allows for case sensitive or insensitive searching using
        the (I)LIKE comparators. Result set will be reduced by the
        ode.sys.Filter instance.
        Arguments:
        klass -- type of entity to be searched. Not null.
        field -- the name of the field, either as simple string or as public static final from the entity class, e.g. {@code ode.model.Project.NAME}. Not null.
        value -- String used for search. Not null.
        caseSensitive -- whether to use LIKE or ILIKE
        filter -- filters the result set. Can be null.
        Returns: A list (possibly empty) with the results.
        """
        def end_findAllByString(self, _r):
            return _M_ode.api.IQuery._op_findAllByString.end(self, _r)

        """
        Executes the stored query with the given name. If a query
        with the name cannot be found, an exception will be thrown.
        The queryName parameter can be an actual query String if the
        StringQuerySource is configured on the server and the user
        running the query has proper permissions.
        Arguments:
        query -- Query to execute
        params -- 
        _ctx -- The request context for the invocation.
        Returns: Possibly null IObject result.
        Throws:
        ValidationException -- 
        """
        def findByQuery(self, query, params, _ctx=None):
            return _M_ode.api.IQuery._op_findByQuery.invoke(self, ((query, params), _ctx))

        """
        Executes the stored query with the given name. If a query
        with the name cannot be found, an exception will be thrown.
        The queryName parameter can be an actual query String if the
        StringQuerySource is configured on the server and the user
        running the query has proper permissions.
        Arguments:
        query -- Query to execute
        params -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findByQuery(self, query, params, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_findByQuery.begin(self, ((query, params), _response, _ex, _sent, _ctx))

        """
        Executes the stored query with the given name. If a query
        with the name cannot be found, an exception will be thrown.
        The queryName parameter can be an actual query String if the
        StringQuerySource is configured on the server and the user
        running the query has proper permissions.
        Arguments:
        query -- Query to execute
        params -- 
        Returns: Possibly null IObject result.
        Throws:
        ValidationException -- 
        """
        def end_findByQuery(self, _r):
            return _M_ode.api.IQuery._op_findByQuery.end(self, _r)

        """
        Executes the stored query with the given name. If a query
        with the name cannot be found, an exception will be thrown.
        The queryName parameter can be an actual query String if the
        StringQuerySource is configured on the server and the user
        running the query has proper permissions.
        Queries can only return lists of
        ode.model.IObject instances. This means
        all must be of the form:
        select this from SomeModelClass this ...
        though the alias this is unimportant. Do not try to
        return multiple classes in one call like:
        select this, that from SomeClass this, SomeOtherClass that ...
        nor to project values out of an object:
        select this.name from SomeClass this ...
        If a page is desired, add it to the query parameters.
        Arguments:
        query -- Query to execute. Not null.
        params -- 
        _ctx -- The request context for the invocation.
        Returns: Possibly empty List of IObject results.
        """
        def findAllByQuery(self, query, params, _ctx=None):
            return _M_ode.api.IQuery._op_findAllByQuery.invoke(self, ((query, params), _ctx))

        """
        Executes the stored query with the given name. If a query
        with the name cannot be found, an exception will be thrown.
        The queryName parameter can be an actual query String if the
        StringQuerySource is configured on the server and the user
        running the query has proper permissions.
        Queries can only return lists of
        ode.model.IObject instances. This means
        all must be of the form:
        select this from SomeModelClass this ...
        though the alias this is unimportant. Do not try to
        return multiple classes in one call like:
        select this, that from SomeClass this, SomeOtherClass that ...
        nor to project values out of an object:
        select this.name from SomeClass this ...
        If a page is desired, add it to the query parameters.
        Arguments:
        query -- Query to execute. Not null.
        params -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findAllByQuery(self, query, params, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_findAllByQuery.begin(self, ((query, params), _response, _ex, _sent, _ctx))

        """
        Executes the stored query with the given name. If a query
        with the name cannot be found, an exception will be thrown.
        The queryName parameter can be an actual query String if the
        StringQuerySource is configured on the server and the user
        running the query has proper permissions.
        Queries can only return lists of
        ode.model.IObject instances. This means
        all must be of the form:
        select this from SomeModelClass this ...
        though the alias this is unimportant. Do not try to
        return multiple classes in one call like:
        select this, that from SomeClass this, SomeOtherClass that ...
        nor to project values out of an object:
        select this.name from SomeClass this ...
        If a page is desired, add it to the query parameters.
        Arguments:
        query -- Query to execute. Not null.
        params -- 
        Returns: Possibly empty List of IObject results.
        """
        def end_findAllByQuery(self, _r):
            return _M_ode.api.IQuery._op_findAllByQuery.end(self, _r)

        """
        Executes a full text search based on Lucene. Each term in
        the query can also be prefixed by the name of the field to
        which is should be restricted.
        Examples:
        owner:root AND annotation:someTag
        file:xml AND name:*hoechst*
        For more information, see
        Query Parser Syntax
        The return values are first filtered by the security system.
        Arguments:
        klass -- A non-null class specification of which type should be searched.
        query -- A non-null query string. An empty string will return no results.
        params -- Currently the parameters themselves are unused. But the ode.sys.Parameters#theFilter can be used to limit the number of results returned (ode.sys.Filter#limit) or the user for who the results will be found (ode.sys.Filter#ownerId).
        _ctx -- The request context for the invocation.
        Returns: A list of loaded ode.model.IObject instances. Never null.
        """
        def findAllByFullText(self, klass, query, params, _ctx=None):
            return _M_ode.api.IQuery._op_findAllByFullText.invoke(self, ((klass, query, params), _ctx))

        """
        Executes a full text search based on Lucene. Each term in
        the query can also be prefixed by the name of the field to
        which is should be restricted.
        Examples:
        owner:root AND annotation:someTag
        file:xml AND name:*hoechst*
        For more information, see
        Query Parser Syntax
        The return values are first filtered by the security system.
        Arguments:
        klass -- A non-null class specification of which type should be searched.
        query -- A non-null query string. An empty string will return no results.
        params -- Currently the parameters themselves are unused. But the ode.sys.Parameters#theFilter can be used to limit the number of results returned (ode.sys.Filter#limit) or the user for who the results will be found (ode.sys.Filter#ownerId).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findAllByFullText(self, klass, query, params, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_findAllByFullText.begin(self, ((klass, query, params), _response, _ex, _sent, _ctx))

        """
        Executes a full text search based on Lucene. Each term in
        the query can also be prefixed by the name of the field to
        which is should be restricted.
        Examples:
        owner:root AND annotation:someTag
        file:xml AND name:*hoechst*
        For more information, see
        Query Parser Syntax
        The return values are first filtered by the security system.
        Arguments:
        klass -- A non-null class specification of which type should be searched.
        query -- A non-null query string. An empty string will return no results.
        params -- Currently the parameters themselves are unused. But the ode.sys.Parameters#theFilter can be used to limit the number of results returned (ode.sys.Filter#limit) or the user for who the results will be found (ode.sys.Filter#ownerId).
        Returns: A list of loaded ode.model.IObject instances. Never null.
        """
        def end_findAllByFullText(self, _r):
            return _M_ode.api.IQuery._op_findAllByFullText.end(self, _r)

        """
        Return a sequence of ode.RType sequences.
        Each element of the outer sequence is one row in the return
        value.
        Each element of the inner sequence is one column specified
        in the HQL.
        ode.model.IObject instances are returned wrapped
        in an ode.RObject instance. Primitives are
        mapped to the expected ode.RType subclass. Types
        without an ode.RType mapper if returned will
        throw an exception if present in the select except where a
        manual conversion is present on the server. This includes:
        ode.model.Permissions instances are
        serialized to an ode.RMap containing the
        keys: perms, canAnnotate, canEdit, canLink, canDelete,
        canChgrp, canChown
        The quantity types like ode.model.Length are
        serialized to an ode.RMap containing the
        keys: value, unit, symbol
        As with SQL, if an aggregation statement is used, a group
        by clause must be added.
        Examples:
        select i.name, i.description from Image i where i.name like '%.dv'
        select tag.textValue, tagset.textValue from TagAnnotation tag join tag.annotationLinks l join l.child tagset
        select p.pixelsType.value, count(p.id) from Pixel p group by p.pixelsType.value
        Arguments:
        query -- 
        params -- 
        _ctx -- The request context for the invocation.
        """
        def projection(self, query, params, _ctx=None):
            return _M_ode.api.IQuery._op_projection.invoke(self, ((query, params), _ctx))

        """
        Return a sequence of ode.RType sequences.
        Each element of the outer sequence is one row in the return
        value.
        Each element of the inner sequence is one column specified
        in the HQL.
        ode.model.IObject instances are returned wrapped
        in an ode.RObject instance. Primitives are
        mapped to the expected ode.RType subclass. Types
        without an ode.RType mapper if returned will
        throw an exception if present in the select except where a
        manual conversion is present on the server. This includes:
        ode.model.Permissions instances are
        serialized to an ode.RMap containing the
        keys: perms, canAnnotate, canEdit, canLink, canDelete,
        canChgrp, canChown
        The quantity types like ode.model.Length are
        serialized to an ode.RMap containing the
        keys: value, unit, symbol
        As with SQL, if an aggregation statement is used, a group
        by clause must be added.
        Examples:
        select i.name, i.description from Image i where i.name like '%.dv'
        select tag.textValue, tagset.textValue from TagAnnotation tag join tag.annotationLinks l join l.child tagset
        select p.pixelsType.value, count(p.id) from Pixel p group by p.pixelsType.value
        Arguments:
        query -- 
        params -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_projection(self, query, params, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_projection.begin(self, ((query, params), _response, _ex, _sent, _ctx))

        """
        Return a sequence of ode.RType sequences.
        Each element of the outer sequence is one row in the return
        value.
        Each element of the inner sequence is one column specified
        in the HQL.
        ode.model.IObject instances are returned wrapped
        in an ode.RObject instance. Primitives are
        mapped to the expected ode.RType subclass. Types
        without an ode.RType mapper if returned will
        throw an exception if present in the select except where a
        manual conversion is present on the server. This includes:
        ode.model.Permissions instances are
        serialized to an ode.RMap containing the
        keys: perms, canAnnotate, canEdit, canLink, canDelete,
        canChgrp, canChown
        The quantity types like ode.model.Length are
        serialized to an ode.RMap containing the
        keys: value, unit, symbol
        As with SQL, if an aggregation statement is used, a group
        by clause must be added.
        Examples:
        select i.name, i.description from Image i where i.name like '%.dv'
        select tag.textValue, tagset.textValue from TagAnnotation tag join tag.annotationLinks l join l.child tagset
        select p.pixelsType.value, count(p.id) from Pixel p group by p.pixelsType.value
        Arguments:
        query -- 
        params -- 
        """
        def end_projection(self, _r):
            return _M_ode.api.IQuery._op_projection.end(self, _r)

        """
        Refreshes an entire ode.model.IObject graph,
        recursive loading all data for the managed instances in the
        graph from the database. If any non-managed entities are
        detected (e.g. without ids), an
        ode.ApiUsageException will be thrown.
        Arguments:
        iObject -- Non-null managed ode.model.IObject graph which should have all values re-assigned from the database
        _ctx -- The request context for the invocation.
        Returns: a similar ode.model.IObject graph (with possible additions and deletions) which is in-sync with the database.
        Throws:
        ApiUsageException -- if any non-managed entities are found.
        """
        def refresh(self, iObject, _ctx=None):
            return _M_ode.api.IQuery._op_refresh.invoke(self, ((iObject, ), _ctx))

        """
        Refreshes an entire ode.model.IObject graph,
        recursive loading all data for the managed instances in the
        graph from the database. If any non-managed entities are
        detected (e.g. without ids), an
        ode.ApiUsageException will be thrown.
        Arguments:
        iObject -- Non-null managed ode.model.IObject graph which should have all values re-assigned from the database
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_refresh(self, iObject, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IQuery._op_refresh.begin(self, ((iObject, ), _response, _ex, _sent, _ctx))

        """
        Refreshes an entire ode.model.IObject graph,
        recursive loading all data for the managed instances in the
        graph from the database. If any non-managed entities are
        detected (e.g. without ids), an
        ode.ApiUsageException will be thrown.
        Arguments:
        iObject -- Non-null managed ode.model.IObject graph which should have all values re-assigned from the database
        Returns: a similar ode.model.IObject graph (with possible additions and deletions) which is in-sync with the database.
        Throws:
        ApiUsageException -- if any non-managed entities are found.
        """
        def end_refresh(self, _r):
            return _M_ode.api.IQuery._op_refresh.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IQueryPrx.ice_checkedCast(proxy, '::ode::api::IQuery', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IQueryPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IQuery'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IQueryPrx = IcePy.defineProxy('::ode::api::IQuery', IQueryPrx)

    _M_ode.api._t_IQuery = IcePy.defineClass('::ode::api::IQuery', IQuery, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IQuery._ice_type = _M_ode.api._t_IQuery

    IQuery._op_get = IcePy.Operation('get', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_find = IcePy.Operation('find', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_findAll = IcePy.Operation('findAll', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_Filter, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_findByExample = IcePy.Operation('findByExample', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_findAllByExample = IcePy.Operation('findAllByExample', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_IObject, False, 0), ((), _M_ode.sys._t_Filter, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_findByString = IcePy.Operation('findByString', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_findAllByString = IcePy.Operation('findAllByString', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_bool, False, 0), ((), _M_ode.sys._t_Filter, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_findByQuery = IcePy.Operation('findByQuery', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_findAllByQuery = IcePy.Operation('findAllByQuery', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_findAllByFullText = IcePy.Operation('findAllByFullText', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_projection = IcePy.Operation('projection', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode._t_RTypeSeqSeq, False, 0), (_M_ode._t_ServerError,))
    IQuery._op_refresh = IcePy.Operation('refresh', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.IQuery = IQuery
    del IQuery

    _M_ode.api.IQueryPrx = IQueryPrx
    del IQueryPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
