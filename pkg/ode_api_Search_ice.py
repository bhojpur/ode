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
import ode_ServicesF_ice
import ode_System_ice
import ode_Collections_ice

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

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

if 'Search' not in _M_ode.api.__dict__:
    _M_ode.api.Search = Ice.createTempClass()
    class Search(_M_ode.api.StatefulServiceInterface):
        """
        Central search interface, allowing Web2.0 style queries. Each
        ode.api.Search instance keeps up with several queries and
        lazily-loads the results as {@code hasNext}, {@code next} and
        {@code results} are called. These queries are created by the
        by* methods.
        Each instance also has a number of settings which can all be
        changed from their defaults via accessors, e.g.
        {@code setBatchSize} or {@code setCaseSensitive}.
        The only methods which are required for the proper functioning of a
        Search instance are:
        {@code onlyType}, {@code onlyTypes} OR
        {@code allTypes}
        Any by* method to create a query
        Use of the {@code allTypes} method is discouraged, since it is
        possibly very resource intensive, which is why any attempt to
        receive results without specifically setting types or allowing all
        is prohibited.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.Search:
                raise RuntimeError('ode.api.Search is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::Search', '::ode::api::ServiceInterface', '::ode::api::StatefulServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::Search'

        def ice_staticId():
            return '::ode::api::Search'
        ice_staticId = staticmethod(ice_staticId)

        def activeQueries_async(self, _cb, current=None):
            """
            Returns the number of active queries. This means that
            {@code activeQueries} gives the minimum number of
            remaining calls to {@code results} when batches are not
            merged.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setBatchSize_async(self, _cb, size, current=None):
            """
            Sets the maximum number of results that will be returned by
            one call to {@code results}. If batches are not merged,
            then results may often be less than the batch size. If
            batches are merged, then only the last call to
            {@code results} can be less than batch size.
            Note: some query types may not support batching at the
            query level, and all instances must then be loaded into
            memory simultaneously.
            Arguments:
            _cb -- The asynchronous callback object.
            size -- maximum number of results per call to {@code results}
            current -- The Current object for the invocation.
            """
            pass

        def getBatchSize_async(self, _cb, current=None):
            """
            Returns the current batch size. If {@code setBatchSize}
            has not been called, the default value will be in effect.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setMergedBatches_async(self, _cb, merge, current=None):
            """
            Set whether or not results from two separate queries can be
            returned in the same call to {@code results}.
            Arguments:
            _cb -- The asynchronous callback object.
            merge -- 
            current -- The Current object for the invocation.
            """
            pass

        def isMergedBatches_async(self, _cb, current=None):
            """
            Returns the current merged-batches setting. If
            {@code setMergedBatches} has not been called, the
            default value will be in effect.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setCaseSentivice_async(self, _cb, caseSensitive, current=None):
            """
            Sets the case sensitivity on all queries where
            case-sensitivity is supported.
            Arguments:
            _cb -- The asynchronous callback object.
            caseSensitive -- 
            current -- The Current object for the invocation.
            """
            pass

        def setCaseSensitive_async(self, _cb, caseSensitive, current=None):
            """
            Sets the case sensitivity on all queries where
            case-sensitivity is supported.
            Arguments:
            _cb -- The asynchronous callback object.
            caseSensitive -- 
            current -- The Current object for the invocation.
            """
            pass

        def isCaseSensitive_async(self, _cb, current=None):
            """
            Returns the current case sensitivity setting. If
            {@code setCaseSensitive} has not been called, the
            default value will be in effect.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setUseProjections_async(self, _cb, useProjections, current=None):
            """
            Determines if Lucene queries should not hit the database.
            Instead all values which are stored in the index will be
            loaded into the object, which includes the id. However, the
            entity will not be marked unloaded and therefore it is
            especially important to not allow a projection-instance to
            be saved back to the server. This can result in DATA LOSS.
            Arguments:
            _cb -- The asynchronous callback object.
            useProjections -- 
            current -- The Current object for the invocation.
            """
            pass

        def isUseProjections_async(self, _cb, current=None):
            """
            Returns the current use-projection setting. If true, the
            client must be careful with all results that are returned.
            See {@code setUseProjections} for more. If
            {@code setUseProjections} has not been called, the
            default value will be in effect.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setReturnUnloaded_async(self, _cb, returnUnloaded, current=None):
            """
            Determines if all results should be returned as unloaded
            objects. This is particularly useful for creating lists for
            further querying via ode.api.IQuery. This value
            overrides the {@code setUseProjections} setting.
            Arguments:
            _cb -- The asynchronous callback object.
            returnUnloaded -- 
            current -- The Current object for the invocation.
            """
            pass

        def isReturnUnloaded_async(self, _cb, current=None):
            """
            Returns the current return-unloaded setting. If true, all
            returned entities will be unloaded. If
            {@code setReturnUnloaded} has not been called, the
            default value will be in effect.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setAllowLeadingWildcard_async(self, _cb, allowLeadingWildcard, current=None):
            """
            Permits full-text queries with a leading query if true.
            Arguments:
            _cb -- The asynchronous callback object.
            allowLeadingWildcard -- 
            current -- The Current object for the invocation.
            """
            pass

        def isAllowLeadingWildcard_async(self, _cb, current=None):
            """
            Returns the current leading-wildcard setting. If false,
            {@code byFullText} and {@code bySomeMustNone} will throw
            an ode.ApiUsageException, since leading-wildcard
            searches are quite slow. Use
            {@code setAllowLeadingWildcard} in order to permit this
            usage.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def onlyType_async(self, _cb, klass, current=None):
            """
            Restricts the search to a single type. All return values
            will match this type.
            Arguments:
            _cb -- The asynchronous callback object.
            klass -- 
            current -- The Current object for the invocation.
            """
            pass

        def onlyTypes_async(self, _cb, classes, current=None):
            """
            Restricts searches to a set of types. The entities returned
            are guaranteed to be one of these types.
            Arguments:
            _cb -- The asynchronous callback object.
            classes -- 
            current -- The Current object for the invocation.
            """
            pass

        def allTypes_async(self, _cb, current=None):
            """
            Permits all types to be returned. For some types of
            queries, this carries a performance penalty as every
            database table must be hit.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def onlyIds_async(self, _cb, ids, current=None):
            """
            Restricts the set of ids which will be checked.
            This is useful for testing one of the given restrictions on
            a reduced set of objects.
            Arguments:
            _cb -- The asynchronous callback object.
            ids -- Can be null, in which case the previous restriction is removed.
            current -- The Current object for the invocation.
            """
            pass

        def onlyOwnedBy_async(self, _cb, d, current=None):
            """
            Uses the ode.model.Details#getOwner() and
            ode.model.Details#getGroup() information to
            restrict the entities which will be returned. If both are
            non-null, the two restrictions are joined by an AND.
            Arguments:
            _cb -- The asynchronous callback object.
            d -- Can be null, in which case the previous restriction is removed.
            current -- The Current object for the invocation.
            """
            pass

        def notOwnedBy_async(self, _cb, d, current=None):
            """
            Uses the ode.model.Details#getOwner() and
            ode.model.Details#getGroup() information to
            restrict the entities which will be returned. If both are
            non-null, the two restrictions are joined by an AND.
            Arguments:
            _cb -- The asynchronous callback object.
            d -- Can be null, in which case the previous restriction is removed.
            current -- The Current object for the invocation.
            """
            pass

        def onlyCreatedBetween_async(self, _cb, start, stop, current=None):
            """
            Restricts the time between which an entity may have been
            created.
            Arguments:
            _cb -- The asynchronous callback object.
            start -- Can be null, i.e. interval open to negative infinity.
            stop -- Can be null, i.e. interval opens to positive infinity.
            current -- The Current object for the invocation.
            """
            pass

        def onlyModifiedBetween_async(self, _cb, start, stop, current=None):
            """
            Restricts the time between which an entity may have last
            been modified.
            Arguments:
            _cb -- The asynchronous callback object.
            start -- Can be null, i.e. interval open to negative infinity.
            stop -- Can be null, i.e. interval open to positive infinity.
            current -- The Current object for the invocation.
            """
            pass

        def onlyAnnotatedBetween_async(self, _cb, start, stop, current=None):
            """
            Restricts entities by the time in which any annotation
            (which matches the other filters) was added them. This
            matches the ode.model.Details#getCreationEvent()
            creation event of the ode.model.Annotation.
            Arguments:
            _cb -- The asynchronous callback object.
            start -- Can be null, i.e. interval open to negative infinity.
            stop -- Can be null, i.e. interval open to positive infinity.
            current -- The Current object for the invocation.
            """
            pass

        def onlyAnnotatedBy_async(self, _cb, d, current=None):
            """
            Restricts entities by who has annotated them with an
            ode.model.Annotation matching the other filters.
            As {@code onlyOwnedBy}, the
            ode.model.Details#getOwner() and
            ode.model.Details#getGroup() information is
            combined with an AND condition.
            Arguments:
            _cb -- The asynchronous callback object.
            d -- Can be null, in which case any previous restriction is removed.
            current -- The Current object for the invocation.
            """
            pass

        def notAnnotatedBy_async(self, _cb, d, current=None):
            """
            Restricts entities by who has not annotated them with an
            ode.model.Annotation matching the other filters.
            As {@code notOwnedBy}, the
            ode.model.Details#getOwner() and
            ode.model.Details#getGroup() information is
            combined with an AND condition.
            Arguments:
            _cb -- The asynchronous callback object.
            d -- Can be null, in which case any previous restriction is removed.
            current -- The Current object for the invocation.
            """
            pass

        def onlyAnnotatedWith_async(self, _cb, classes, current=None):
            """
            Restricts entities to having an
            ode.model.Annotation of all the given types. This
            is useful in combination with the other onlyAnnotated*
            methods to say, e.g., only annotated with a file by user X.
            By default, this value is null and imposes no
            restriction. Passing an empty array implies an object that
            is not annotated at all.
            Note: If the semantics were OR, then a client would have to
            query each class individually, and compare all the various
            values, checking which ids are where. However, since this
            method defaults to AND, multiple calls (optionally with
            {@code isMergedBatches} and {@code isReturnUnloaded})
            and combine the results. Duplicate ids are still possible
            so a set of some form should be used to collect the results.
            Arguments:
            _cb -- The asynchronous callback object.
            classes -- Can be empty, in which case restriction is removed.
            current -- The Current object for the invocation.
            """
            pass

        def addOrderByAsc_async(self, _cb, path, current=None):
            """
            A path from the target entity which will be added to the
            current stack of order statements applied to the query.
            Arguments:
            _cb -- The asynchronous callback object.
            path -- Non-null.
            current -- The Current object for the invocation.
            """
            pass

        def addOrderByDesc_async(self, _cb, path, current=None):
            """
            A path from the target entity which will be added to the
            current stack of order statements applied to the query.
            Arguments:
            _cb -- The asynchronous callback object.
            path -- Non-null.
            current -- The Current object for the invocation.
            """
            pass

        def unordered_async(self, _cb, current=None):
            """
            Removes the current stack of order statements.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def fetchAnnotations_async(self, _cb, classes, current=None):
            """
            Queries the database for all ode.model.Annotation
            annotations of the given types for all returned instances.
            Arguments:
            _cb -- The asynchronous callback object.
            classes -- Can be empty, which removes previous fetch setting.
            current -- The Current object for the invocation.
            """
            pass

        def fetchAlso_async(self, _cb, fetches, current=None):
            """
            Adds a fetch clause for loading non-annotation fields of
            returned entities. Each fetch is a hibernate clause in dot
            notation.
            Arguments:
            _cb -- The asynchronous callback object.
            fetches -- Can be empty, which removes previous fetch setting.
            current -- The Current object for the invocation.
            """
            pass

        def resetDefaults_async(self, _cb, current=None):
            """
            Resets all settings (non-query state) to the original
            default values, as if the instance had just be created.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def byGroupForTags_async(self, _cb, group, current=None):
            """
            Returns transient (without ID)
            ode.model.TagAnnotation instances which
            represent all the
            ode.model.TagAnnotation tags in the given group.
            The entities are transient and without ownership since
            multiple users can own the same tag. This method will
            override settings for types.
            Arguments:
            _cb -- The asynchronous callback object.
            group -- Can be null or empty to return all tags.
            current -- The Current object for the invocation.
            """
            pass

        def byTagForGroups_async(self, _cb, tag, current=None):
            """
            Creates a query which will return transient (without ID)
            ode.model.TagAnnotation instances which represent
            all the ode.model.TagAnnotation tag groups which
            the given tag belongs to. The entities are transient and
            without ownership since multiple users can own the same tag
            group. This method will override settings for types.
            Arguments:
            _cb -- The asynchronous callback object.
            tag -- Can be null or empty to return all groups.
            current -- The Current object for the invocation.
            """
            pass

        def byFullText_async(self, _cb, query, current=None):
            """
            Passes the query as is to the Lucene backend.
            Arguments:
            _cb -- The asynchronous callback object.
            query -- May not be null or of zero length.
            current -- The Current object for the invocation.
            """
            pass

        def byLuceneQueryBuilder_async(self, _cb, fields, _from, to, dateType, query, current=None):
            """
            Builds a Lucene query and passes it to the Lucene backend.
            Arguments:
            _cb -- The asynchronous callback object.
            fields -- The fields (comma separated) to search in (name, description, ...)
            _from -- The date range from, in the form YYYYMMDD (may be null)
            to -- The date range to (inclusive), in the form YYYYMMDD (may be null)
            dateType -- {@code DATE_TYPE_ACQUISITION} or {@code DATE_TYPE_IMPORT}
            query -- May not be null or of zero length.
            current -- The Current object for the invocation.
            """
            pass

        def bySimilarTerms_async(self, _cb, terms, current=None):
            """
            Returns transient (without ID)
            ode.model.TextAnnotation instances which represent
            terms which are similar to the given terms. For example, if
            the argument is cell, one return value might have as
            its textValue: cellular while another has
            cellularize.
            No filtering or fetching is performed.
            Arguments:
            _cb -- The asynchronous callback object.
            terms -- Cannot be empty.
            current -- The Current object for the invocation.
            """
            pass

        def byHqlQuery_async(self, _cb, query, params, current=None):
            """
            Delegates to {@code ode.api.IQuery.findAllByQuery} method
            to take advantage of the {@code and}, {@code or}, and
            {@code not} methods, or queue-semantics.
            Arguments:
            _cb -- The asynchronous callback object.
            query -- Not null.
            params -- May be null. Defaults are then in effect.
            current -- The Current object for the invocation.
            """
            pass

        def bySomeMustNone_async(self, _cb, some, must, none, current=None):
            """
            Builds a Lucene query and passes it to {@code byFullText}.
            Arguments:
            _cb -- The asynchronous callback object.
            some -- Some (at least one) of these terms must be present in the document. May be null.
            must -- All of these terms must be present in the document. May be null.
            none -- None of these terms may be present in the document. May be null.
            current -- The Current object for the invocation.
            """
            pass

        def byAnnotatedWith_async(self, _cb, examples, current=None):
            """
            Finds entities annotated with an
            ode.model.Annotation similar to the example. This
            does not use Hibernate's
            {@code ode.api.IQuery.findByExample} Query-By-Example}
            mechanism, since that cannot handle joins. The fields which
            are used are:
            the main content of the annotation : String,
            ode.model.OriginalFile#getId(), etc.
            If the main content is null it is assumed to
            be a wildcard searched, and only the type of the annotation
            is searched. Currently, ListAnnotations are not supported.
            Arguments:
            _cb -- The asynchronous callback object.
            examples -- Not empty.
            current -- The Current object for the invocation.
            """
            pass

        def clearQueries_async(self, _cb, current=None):
            """
            Removes all active queries (leaving {@code resetDefaults}
            settings alone), such that {@code activeQueries} will
            return 0.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def and_async(self, _cb, current=None):
            """
            Applies the next by* method to the previous by* method, so
            that a call {@code hasNext}, {@code next}, or
            {@code results} sees only the intersection of the two
            calls.
            For example,
            {@code
            service.onlyType(Image.class);
            service.byFullText(&quot;foo&quot;);
            service.intersection();
            service.byAnnotatedWith(TagAnnotation.class);
            }
            will return only the Images with TagAnnotations.
            Calling this method overrides a previous setting of
            {@code or} or {@code not}. If there is no active queries
            (i.e. {@code activeQueries > 0}), then an
            ode.ApiUsageException will be thrown.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def or_async(self, _cb, current=None):
            """
            Applies the next by* method to the previous by* method, so
            that a call {@code hasNext}, {@code next} or
            {@code results} sees only the union of the two calls.
            For example,
            {@code
            service.onlyType(Image.class);
            service.byFullText(&quot;foo&quot;);
            service.or();
            service.onlyType(Dataset.class);
            service.byFullText(&quot;foo&quot;);
            }
            will return both Images and Datasets together.
            Calling this method overrides a previous setting of
            {@code and} or {@code not}. If there is no active queries
            (i.e. {@code activeQueries > 0}), then an
            ode.ApiUsageException will be thrown.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def not_async(self, _cb, current=None):
            """
            Applies the next by* method to the previous by* method, so
            that a call {@code hasNext}, {@code next}, or
            {@code results} sees only the intersection of the two
            calls.
            For example,
            {@code
            service.onlyType(Image.class);
            service.byFullText(&quot;foo&quot;);
            service.complement();
            service.byAnnotatedWith(TagAnnotation.class);
            }
            will return all the Images not annotated with
            TagAnnotation. 
            Calling this method overrides a previous setting of
            {@code or} or {@code and}. If there is no active queries
            (i.e. {@code activeQueries > 0}), then an
            ode.ApiUsageException will be thrown.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def hasNext_async(self, _cb, current=None):
            """
            Returns true if another call to
            {@code next} is valid. A call to {@code next} may throw
            an exception for another reason, however.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def next_async(self, _cb, current=None):
            """
            Returns the next entity from the current query. If the
            previous call returned the last entity from a given query,
            the first entity from the next query will be returned and
            {@code activeQueries} decremented.
            Since this method only returns the entity itself, a single
            call to {@code currentMetadata} may follow this call to
            gather the extra metadata which is returned in the map via
            {@code results}.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if {code hasNext} returns false.
            """
            pass

        def results_async(self, _cb, current=None):
            """
            Returns up to {@code getBatchSize} batch size number of
            results along with the related query metadata. If
            {@code isMergedBatches} batches are merged then the
            results from multiple queries may be returned together.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if {@code hasNext} returns false.
            """
            pass

        def currentMetadata_async(self, _cb, current=None):
            """
            Provides access to the extra query information (for example
            Lucene score and boost values) for a single call to
            {@code next}. This method may only be called once for any
            given call to {@code next}.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def currentMetadataList_async(self, _cb, current=None):
            """
            Provides access to the extra query information (for example
            Lucene score and boost values) for a single call to
            {@code results}. This method may only be called once for
            any given call to {@code results}.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def remove_async(self, _cb, current=None):
            """
            Unsupported operation.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_Search)

        __repr__ = __str__

    _M_ode.api.SearchPrx = Ice.createTempClass()
    class SearchPrx(_M_ode.api.StatefulServiceInterfacePrx):

        """
        Returns the number of active queries. This means that
        {@code activeQueries} gives the minimum number of
        remaining calls to {@code results} when batches are not
        merged.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: number of active queries
        """
        def activeQueries(self, _ctx=None):
            return _M_ode.api.Search._op_activeQueries.invoke(self, ((), _ctx))

        """
        Returns the number of active queries. This means that
        {@code activeQueries} gives the minimum number of
        remaining calls to {@code results} when batches are not
        merged.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_activeQueries(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_activeQueries.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the number of active queries. This means that
        {@code activeQueries} gives the minimum number of
        remaining calls to {@code results} when batches are not
        merged.
        Arguments:
        Returns: number of active queries
        """
        def end_activeQueries(self, _r):
            return _M_ode.api.Search._op_activeQueries.end(self, _r)

        """
        Sets the maximum number of results that will be returned by
        one call to {@code results}. If batches are not merged,
        then results may often be less than the batch size. If
        batches are merged, then only the last call to
        {@code results} can be less than batch size.
        Note: some query types may not support batching at the
        query level, and all instances must then be loaded into
        memory simultaneously.
        Arguments:
        size -- maximum number of results per call to {@code results}
        _ctx -- The request context for the invocation.
        """
        def setBatchSize(self, size, _ctx=None):
            return _M_ode.api.Search._op_setBatchSize.invoke(self, ((size, ), _ctx))

        """
        Sets the maximum number of results that will be returned by
        one call to {@code results}. If batches are not merged,
        then results may often be less than the batch size. If
        batches are merged, then only the last call to
        {@code results} can be less than batch size.
        Note: some query types may not support batching at the
        query level, and all instances must then be loaded into
        memory simultaneously.
        Arguments:
        size -- maximum number of results per call to {@code results}
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setBatchSize(self, size, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_setBatchSize.begin(self, ((size, ), _response, _ex, _sent, _ctx))

        """
        Sets the maximum number of results that will be returned by
        one call to {@code results}. If batches are not merged,
        then results may often be less than the batch size. If
        batches are merged, then only the last call to
        {@code results} can be less than batch size.
        Note: some query types may not support batching at the
        query level, and all instances must then be loaded into
        memory simultaneously.
        Arguments:
        size -- maximum number of results per call to {@code results}
        """
        def end_setBatchSize(self, _r):
            return _M_ode.api.Search._op_setBatchSize.end(self, _r)

        """
        Returns the current batch size. If {@code setBatchSize}
        has not been called, the default value will be in effect.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: maximum number of results per call to {@code results}
        """
        def getBatchSize(self, _ctx=None):
            return _M_ode.api.Search._op_getBatchSize.invoke(self, ((), _ctx))

        """
        Returns the current batch size. If {@code setBatchSize}
        has not been called, the default value will be in effect.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getBatchSize(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_getBatchSize.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current batch size. If {@code setBatchSize}
        has not been called, the default value will be in effect.
        Arguments:
        Returns: maximum number of results per call to {@code results}
        """
        def end_getBatchSize(self, _r):
            return _M_ode.api.Search._op_getBatchSize.end(self, _r)

        """
        Set whether or not results from two separate queries can be
        returned in the same call to {@code results}.
        Arguments:
        merge -- 
        _ctx -- The request context for the invocation.
        """
        def setMergedBatches(self, merge, _ctx=None):
            return _M_ode.api.Search._op_setMergedBatches.invoke(self, ((merge, ), _ctx))

        """
        Set whether or not results from two separate queries can be
        returned in the same call to {@code results}.
        Arguments:
        merge -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setMergedBatches(self, merge, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_setMergedBatches.begin(self, ((merge, ), _response, _ex, _sent, _ctx))

        """
        Set whether or not results from two separate queries can be
        returned in the same call to {@code results}.
        Arguments:
        merge -- 
        """
        def end_setMergedBatches(self, _r):
            return _M_ode.api.Search._op_setMergedBatches.end(self, _r)

        """
        Returns the current merged-batches setting. If
        {@code setMergedBatches} has not been called, the
        default value will be in effect.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def isMergedBatches(self, _ctx=None):
            return _M_ode.api.Search._op_isMergedBatches.invoke(self, ((), _ctx))

        """
        Returns the current merged-batches setting. If
        {@code setMergedBatches} has not been called, the
        default value will be in effect.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isMergedBatches(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_isMergedBatches.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current merged-batches setting. If
        {@code setMergedBatches} has not been called, the
        default value will be in effect.
        Arguments:
        """
        def end_isMergedBatches(self, _r):
            return _M_ode.api.Search._op_isMergedBatches.end(self, _r)

        """
        Sets the case sensitivity on all queries where
        case-sensitivity is supported.
        Arguments:
        caseSensitive -- 
        _ctx -- The request context for the invocation.
        """
        def setCaseSentivice(self, caseSensitive, _ctx=None):
            return _M_ode.api.Search._op_setCaseSentivice.invoke(self, ((caseSensitive, ), _ctx))

        """
        Sets the case sensitivity on all queries where
        case-sensitivity is supported.
        Arguments:
        caseSensitive -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setCaseSentivice(self, caseSensitive, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_setCaseSentivice.begin(self, ((caseSensitive, ), _response, _ex, _sent, _ctx))

        """
        Sets the case sensitivity on all queries where
        case-sensitivity is supported.
        Arguments:
        caseSensitive -- 
        """
        def end_setCaseSentivice(self, _r):
            return _M_ode.api.Search._op_setCaseSentivice.end(self, _r)

        """
        Sets the case sensitivity on all queries where
        case-sensitivity is supported.
        Arguments:
        caseSensitive -- 
        _ctx -- The request context for the invocation.
        """
        def setCaseSensitive(self, caseSensitive, _ctx=None):
            return _M_ode.api.Search._op_setCaseSensitive.invoke(self, ((caseSensitive, ), _ctx))

        """
        Sets the case sensitivity on all queries where
        case-sensitivity is supported.
        Arguments:
        caseSensitive -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setCaseSensitive(self, caseSensitive, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_setCaseSensitive.begin(self, ((caseSensitive, ), _response, _ex, _sent, _ctx))

        """
        Sets the case sensitivity on all queries where
        case-sensitivity is supported.
        Arguments:
        caseSensitive -- 
        """
        def end_setCaseSensitive(self, _r):
            return _M_ode.api.Search._op_setCaseSensitive.end(self, _r)

        """
        Returns the current case sensitivity setting. If
        {@code setCaseSensitive} has not been called, the
        default value will be in effect.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def isCaseSensitive(self, _ctx=None):
            return _M_ode.api.Search._op_isCaseSensitive.invoke(self, ((), _ctx))

        """
        Returns the current case sensitivity setting. If
        {@code setCaseSensitive} has not been called, the
        default value will be in effect.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isCaseSensitive(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_isCaseSensitive.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current case sensitivity setting. If
        {@code setCaseSensitive} has not been called, the
        default value will be in effect.
        Arguments:
        """
        def end_isCaseSensitive(self, _r):
            return _M_ode.api.Search._op_isCaseSensitive.end(self, _r)

        """
        Determines if Lucene queries should not hit the database.
        Instead all values which are stored in the index will be
        loaded into the object, which includes the id. However, the
        entity will not be marked unloaded and therefore it is
        especially important to not allow a projection-instance to
        be saved back to the server. This can result in DATA LOSS.
        Arguments:
        useProjections -- 
        _ctx -- The request context for the invocation.
        """
        def setUseProjections(self, useProjections, _ctx=None):
            return _M_ode.api.Search._op_setUseProjections.invoke(self, ((useProjections, ), _ctx))

        """
        Determines if Lucene queries should not hit the database.
        Instead all values which are stored in the index will be
        loaded into the object, which includes the id. However, the
        entity will not be marked unloaded and therefore it is
        especially important to not allow a projection-instance to
        be saved back to the server. This can result in DATA LOSS.
        Arguments:
        useProjections -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setUseProjections(self, useProjections, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_setUseProjections.begin(self, ((useProjections, ), _response, _ex, _sent, _ctx))

        """
        Determines if Lucene queries should not hit the database.
        Instead all values which are stored in the index will be
        loaded into the object, which includes the id. However, the
        entity will not be marked unloaded and therefore it is
        especially important to not allow a projection-instance to
        be saved back to the server. This can result in DATA LOSS.
        Arguments:
        useProjections -- 
        """
        def end_setUseProjections(self, _r):
            return _M_ode.api.Search._op_setUseProjections.end(self, _r)

        """
        Returns the current use-projection setting. If true, the
        client must be careful with all results that are returned.
        See {@code setUseProjections} for more. If
        {@code setUseProjections} has not been called, the
        default value will be in effect.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def isUseProjections(self, _ctx=None):
            return _M_ode.api.Search._op_isUseProjections.invoke(self, ((), _ctx))

        """
        Returns the current use-projection setting. If true, the
        client must be careful with all results that are returned.
        See {@code setUseProjections} for more. If
        {@code setUseProjections} has not been called, the
        default value will be in effect.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isUseProjections(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_isUseProjections.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current use-projection setting. If true, the
        client must be careful with all results that are returned.
        See {@code setUseProjections} for more. If
        {@code setUseProjections} has not been called, the
        default value will be in effect.
        Arguments:
        """
        def end_isUseProjections(self, _r):
            return _M_ode.api.Search._op_isUseProjections.end(self, _r)

        """
        Determines if all results should be returned as unloaded
        objects. This is particularly useful for creating lists for
        further querying via ode.api.IQuery. This value
        overrides the {@code setUseProjections} setting.
        Arguments:
        returnUnloaded -- 
        _ctx -- The request context for the invocation.
        """
        def setReturnUnloaded(self, returnUnloaded, _ctx=None):
            return _M_ode.api.Search._op_setReturnUnloaded.invoke(self, ((returnUnloaded, ), _ctx))

        """
        Determines if all results should be returned as unloaded
        objects. This is particularly useful for creating lists for
        further querying via ode.api.IQuery. This value
        overrides the {@code setUseProjections} setting.
        Arguments:
        returnUnloaded -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setReturnUnloaded(self, returnUnloaded, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_setReturnUnloaded.begin(self, ((returnUnloaded, ), _response, _ex, _sent, _ctx))

        """
        Determines if all results should be returned as unloaded
        objects. This is particularly useful for creating lists for
        further querying via ode.api.IQuery. This value
        overrides the {@code setUseProjections} setting.
        Arguments:
        returnUnloaded -- 
        """
        def end_setReturnUnloaded(self, _r):
            return _M_ode.api.Search._op_setReturnUnloaded.end(self, _r)

        """
        Returns the current return-unloaded setting. If true, all
        returned entities will be unloaded. If
        {@code setReturnUnloaded} has not been called, the
        default value will be in effect.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def isReturnUnloaded(self, _ctx=None):
            return _M_ode.api.Search._op_isReturnUnloaded.invoke(self, ((), _ctx))

        """
        Returns the current return-unloaded setting. If true, all
        returned entities will be unloaded. If
        {@code setReturnUnloaded} has not been called, the
        default value will be in effect.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isReturnUnloaded(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_isReturnUnloaded.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current return-unloaded setting. If true, all
        returned entities will be unloaded. If
        {@code setReturnUnloaded} has not been called, the
        default value will be in effect.
        Arguments:
        """
        def end_isReturnUnloaded(self, _r):
            return _M_ode.api.Search._op_isReturnUnloaded.end(self, _r)

        """
        Permits full-text queries with a leading query if true.
        Arguments:
        allowLeadingWildcard -- 
        _ctx -- The request context for the invocation.
        """
        def setAllowLeadingWildcard(self, allowLeadingWildcard, _ctx=None):
            return _M_ode.api.Search._op_setAllowLeadingWildcard.invoke(self, ((allowLeadingWildcard, ), _ctx))

        """
        Permits full-text queries with a leading query if true.
        Arguments:
        allowLeadingWildcard -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setAllowLeadingWildcard(self, allowLeadingWildcard, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_setAllowLeadingWildcard.begin(self, ((allowLeadingWildcard, ), _response, _ex, _sent, _ctx))

        """
        Permits full-text queries with a leading query if true.
        Arguments:
        allowLeadingWildcard -- 
        """
        def end_setAllowLeadingWildcard(self, _r):
            return _M_ode.api.Search._op_setAllowLeadingWildcard.end(self, _r)

        """
        Returns the current leading-wildcard setting. If false,
        {@code byFullText} and {@code bySomeMustNone} will throw
        an ode.ApiUsageException, since leading-wildcard
        searches are quite slow. Use
        {@code setAllowLeadingWildcard} in order to permit this
        usage.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def isAllowLeadingWildcard(self, _ctx=None):
            return _M_ode.api.Search._op_isAllowLeadingWildcard.invoke(self, ((), _ctx))

        """
        Returns the current leading-wildcard setting. If false,
        {@code byFullText} and {@code bySomeMustNone} will throw
        an ode.ApiUsageException, since leading-wildcard
        searches are quite slow. Use
        {@code setAllowLeadingWildcard} in order to permit this
        usage.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isAllowLeadingWildcard(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_isAllowLeadingWildcard.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current leading-wildcard setting. If false,
        {@code byFullText} and {@code bySomeMustNone} will throw
        an ode.ApiUsageException, since leading-wildcard
        searches are quite slow. Use
        {@code setAllowLeadingWildcard} in order to permit this
        usage.
        Arguments:
        """
        def end_isAllowLeadingWildcard(self, _r):
            return _M_ode.api.Search._op_isAllowLeadingWildcard.end(self, _r)

        """
        Restricts the search to a single type. All return values
        will match this type.
        Arguments:
        klass -- 
        _ctx -- The request context for the invocation.
        """
        def onlyType(self, klass, _ctx=None):
            return _M_ode.api.Search._op_onlyType.invoke(self, ((klass, ), _ctx))

        """
        Restricts the search to a single type. All return values
        will match this type.
        Arguments:
        klass -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_onlyType(self, klass, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_onlyType.begin(self, ((klass, ), _response, _ex, _sent, _ctx))

        """
        Restricts the search to a single type. All return values
        will match this type.
        Arguments:
        klass -- 
        """
        def end_onlyType(self, _r):
            return _M_ode.api.Search._op_onlyType.end(self, _r)

        """
        Restricts searches to a set of types. The entities returned
        are guaranteed to be one of these types.
        Arguments:
        classes -- 
        _ctx -- The request context for the invocation.
        """
        def onlyTypes(self, classes, _ctx=None):
            return _M_ode.api.Search._op_onlyTypes.invoke(self, ((classes, ), _ctx))

        """
        Restricts searches to a set of types. The entities returned
        are guaranteed to be one of these types.
        Arguments:
        classes -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_onlyTypes(self, classes, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_onlyTypes.begin(self, ((classes, ), _response, _ex, _sent, _ctx))

        """
        Restricts searches to a set of types. The entities returned
        are guaranteed to be one of these types.
        Arguments:
        classes -- 
        """
        def end_onlyTypes(self, _r):
            return _M_ode.api.Search._op_onlyTypes.end(self, _r)

        """
        Permits all types to be returned. For some types of
        queries, this carries a performance penalty as every
        database table must be hit.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def allTypes(self, _ctx=None):
            return _M_ode.api.Search._op_allTypes.invoke(self, ((), _ctx))

        """
        Permits all types to be returned. For some types of
        queries, this carries a performance penalty as every
        database table must be hit.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_allTypes(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_allTypes.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Permits all types to be returned. For some types of
        queries, this carries a performance penalty as every
        database table must be hit.
        Arguments:
        """
        def end_allTypes(self, _r):
            return _M_ode.api.Search._op_allTypes.end(self, _r)

        """
        Restricts the set of ids which will be checked.
        This is useful for testing one of the given restrictions on
        a reduced set of objects.
        Arguments:
        ids -- Can be null, in which case the previous restriction is removed.
        _ctx -- The request context for the invocation.
        """
        def onlyIds(self, ids, _ctx=None):
            return _M_ode.api.Search._op_onlyIds.invoke(self, ((ids, ), _ctx))

        """
        Restricts the set of ids which will be checked.
        This is useful for testing one of the given restrictions on
        a reduced set of objects.
        Arguments:
        ids -- Can be null, in which case the previous restriction is removed.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_onlyIds(self, ids, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_onlyIds.begin(self, ((ids, ), _response, _ex, _sent, _ctx))

        """
        Restricts the set of ids which will be checked.
        This is useful for testing one of the given restrictions on
        a reduced set of objects.
        Arguments:
        ids -- Can be null, in which case the previous restriction is removed.
        """
        def end_onlyIds(self, _r):
            return _M_ode.api.Search._op_onlyIds.end(self, _r)

        """
        Uses the ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information to
        restrict the entities which will be returned. If both are
        non-null, the two restrictions are joined by an AND.
        Arguments:
        d -- Can be null, in which case the previous restriction is removed.
        _ctx -- The request context for the invocation.
        """
        def onlyOwnedBy(self, d, _ctx=None):
            return _M_ode.api.Search._op_onlyOwnedBy.invoke(self, ((d, ), _ctx))

        """
        Uses the ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information to
        restrict the entities which will be returned. If both are
        non-null, the two restrictions are joined by an AND.
        Arguments:
        d -- Can be null, in which case the previous restriction is removed.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_onlyOwnedBy(self, d, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_onlyOwnedBy.begin(self, ((d, ), _response, _ex, _sent, _ctx))

        """
        Uses the ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information to
        restrict the entities which will be returned. If both are
        non-null, the two restrictions are joined by an AND.
        Arguments:
        d -- Can be null, in which case the previous restriction is removed.
        """
        def end_onlyOwnedBy(self, _r):
            return _M_ode.api.Search._op_onlyOwnedBy.end(self, _r)

        """
        Uses the ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information to
        restrict the entities which will be returned. If both are
        non-null, the two restrictions are joined by an AND.
        Arguments:
        d -- Can be null, in which case the previous restriction is removed.
        _ctx -- The request context for the invocation.
        """
        def notOwnedBy(self, d, _ctx=None):
            return _M_ode.api.Search._op_notOwnedBy.invoke(self, ((d, ), _ctx))

        """
        Uses the ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information to
        restrict the entities which will be returned. If both are
        non-null, the two restrictions are joined by an AND.
        Arguments:
        d -- Can be null, in which case the previous restriction is removed.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_notOwnedBy(self, d, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_notOwnedBy.begin(self, ((d, ), _response, _ex, _sent, _ctx))

        """
        Uses the ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information to
        restrict the entities which will be returned. If both are
        non-null, the two restrictions are joined by an AND.
        Arguments:
        d -- Can be null, in which case the previous restriction is removed.
        """
        def end_notOwnedBy(self, _r):
            return _M_ode.api.Search._op_notOwnedBy.end(self, _r)

        """
        Restricts the time between which an entity may have been
        created.
        Arguments:
        start -- Can be null, i.e. interval open to negative infinity.
        stop -- Can be null, i.e. interval opens to positive infinity.
        _ctx -- The request context for the invocation.
        """
        def onlyCreatedBetween(self, start, stop, _ctx=None):
            return _M_ode.api.Search._op_onlyCreatedBetween.invoke(self, ((start, stop), _ctx))

        """
        Restricts the time between which an entity may have been
        created.
        Arguments:
        start -- Can be null, i.e. interval open to negative infinity.
        stop -- Can be null, i.e. interval opens to positive infinity.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_onlyCreatedBetween(self, start, stop, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_onlyCreatedBetween.begin(self, ((start, stop), _response, _ex, _sent, _ctx))

        """
        Restricts the time between which an entity may have been
        created.
        Arguments:
        start -- Can be null, i.e. interval open to negative infinity.
        stop -- Can be null, i.e. interval opens to positive infinity.
        """
        def end_onlyCreatedBetween(self, _r):
            return _M_ode.api.Search._op_onlyCreatedBetween.end(self, _r)

        """
        Restricts the time between which an entity may have last
        been modified.
        Arguments:
        start -- Can be null, i.e. interval open to negative infinity.
        stop -- Can be null, i.e. interval open to positive infinity.
        _ctx -- The request context for the invocation.
        """
        def onlyModifiedBetween(self, start, stop, _ctx=None):
            return _M_ode.api.Search._op_onlyModifiedBetween.invoke(self, ((start, stop), _ctx))

        """
        Restricts the time between which an entity may have last
        been modified.
        Arguments:
        start -- Can be null, i.e. interval open to negative infinity.
        stop -- Can be null, i.e. interval open to positive infinity.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_onlyModifiedBetween(self, start, stop, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_onlyModifiedBetween.begin(self, ((start, stop), _response, _ex, _sent, _ctx))

        """
        Restricts the time between which an entity may have last
        been modified.
        Arguments:
        start -- Can be null, i.e. interval open to negative infinity.
        stop -- Can be null, i.e. interval open to positive infinity.
        """
        def end_onlyModifiedBetween(self, _r):
            return _M_ode.api.Search._op_onlyModifiedBetween.end(self, _r)

        """
        Restricts entities by the time in which any annotation
        (which matches the other filters) was added them. This
        matches the ode.model.Details#getCreationEvent()
        creation event of the ode.model.Annotation.
        Arguments:
        start -- Can be null, i.e. interval open to negative infinity.
        stop -- Can be null, i.e. interval open to positive infinity.
        _ctx -- The request context for the invocation.
        """
        def onlyAnnotatedBetween(self, start, stop, _ctx=None):
            return _M_ode.api.Search._op_onlyAnnotatedBetween.invoke(self, ((start, stop), _ctx))

        """
        Restricts entities by the time in which any annotation
        (which matches the other filters) was added them. This
        matches the ode.model.Details#getCreationEvent()
        creation event of the ode.model.Annotation.
        Arguments:
        start -- Can be null, i.e. interval open to negative infinity.
        stop -- Can be null, i.e. interval open to positive infinity.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_onlyAnnotatedBetween(self, start, stop, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_onlyAnnotatedBetween.begin(self, ((start, stop), _response, _ex, _sent, _ctx))

        """
        Restricts entities by the time in which any annotation
        (which matches the other filters) was added them. This
        matches the ode.model.Details#getCreationEvent()
        creation event of the ode.model.Annotation.
        Arguments:
        start -- Can be null, i.e. interval open to negative infinity.
        stop -- Can be null, i.e. interval open to positive infinity.
        """
        def end_onlyAnnotatedBetween(self, _r):
            return _M_ode.api.Search._op_onlyAnnotatedBetween.end(self, _r)

        """
        Restricts entities by who has annotated them with an
        ode.model.Annotation matching the other filters.
        As {@code onlyOwnedBy}, the
        ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information is
        combined with an AND condition.
        Arguments:
        d -- Can be null, in which case any previous restriction is removed.
        _ctx -- The request context for the invocation.
        """
        def onlyAnnotatedBy(self, d, _ctx=None):
            return _M_ode.api.Search._op_onlyAnnotatedBy.invoke(self, ((d, ), _ctx))

        """
        Restricts entities by who has annotated them with an
        ode.model.Annotation matching the other filters.
        As {@code onlyOwnedBy}, the
        ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information is
        combined with an AND condition.
        Arguments:
        d -- Can be null, in which case any previous restriction is removed.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_onlyAnnotatedBy(self, d, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_onlyAnnotatedBy.begin(self, ((d, ), _response, _ex, _sent, _ctx))

        """
        Restricts entities by who has annotated them with an
        ode.model.Annotation matching the other filters.
        As {@code onlyOwnedBy}, the
        ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information is
        combined with an AND condition.
        Arguments:
        d -- Can be null, in which case any previous restriction is removed.
        """
        def end_onlyAnnotatedBy(self, _r):
            return _M_ode.api.Search._op_onlyAnnotatedBy.end(self, _r)

        """
        Restricts entities by who has not annotated them with an
        ode.model.Annotation matching the other filters.
        As {@code notOwnedBy}, the
        ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information is
        combined with an AND condition.
        Arguments:
        d -- Can be null, in which case any previous restriction is removed.
        _ctx -- The request context for the invocation.
        """
        def notAnnotatedBy(self, d, _ctx=None):
            return _M_ode.api.Search._op_notAnnotatedBy.invoke(self, ((d, ), _ctx))

        """
        Restricts entities by who has not annotated them with an
        ode.model.Annotation matching the other filters.
        As {@code notOwnedBy}, the
        ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information is
        combined with an AND condition.
        Arguments:
        d -- Can be null, in which case any previous restriction is removed.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_notAnnotatedBy(self, d, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_notAnnotatedBy.begin(self, ((d, ), _response, _ex, _sent, _ctx))

        """
        Restricts entities by who has not annotated them with an
        ode.model.Annotation matching the other filters.
        As {@code notOwnedBy}, the
        ode.model.Details#getOwner() and
        ode.model.Details#getGroup() information is
        combined with an AND condition.
        Arguments:
        d -- Can be null, in which case any previous restriction is removed.
        """
        def end_notAnnotatedBy(self, _r):
            return _M_ode.api.Search._op_notAnnotatedBy.end(self, _r)

        """
        Restricts entities to having an
        ode.model.Annotation of all the given types. This
        is useful in combination with the other onlyAnnotated*
        methods to say, e.g., only annotated with a file by user X.
        By default, this value is null and imposes no
        restriction. Passing an empty array implies an object that
        is not annotated at all.
        Note: If the semantics were OR, then a client would have to
        query each class individually, and compare all the various
        values, checking which ids are where. However, since this
        method defaults to AND, multiple calls (optionally with
        {@code isMergedBatches} and {@code isReturnUnloaded})
        and combine the results. Duplicate ids are still possible
        so a set of some form should be used to collect the results.
        Arguments:
        classes -- Can be empty, in which case restriction is removed.
        _ctx -- The request context for the invocation.
        """
        def onlyAnnotatedWith(self, classes, _ctx=None):
            return _M_ode.api.Search._op_onlyAnnotatedWith.invoke(self, ((classes, ), _ctx))

        """
        Restricts entities to having an
        ode.model.Annotation of all the given types. This
        is useful in combination with the other onlyAnnotated*
        methods to say, e.g., only annotated with a file by user X.
        By default, this value is null and imposes no
        restriction. Passing an empty array implies an object that
        is not annotated at all.
        Note: If the semantics were OR, then a client would have to
        query each class individually, and compare all the various
        values, checking which ids are where. However, since this
        method defaults to AND, multiple calls (optionally with
        {@code isMergedBatches} and {@code isReturnUnloaded})
        and combine the results. Duplicate ids are still possible
        so a set of some form should be used to collect the results.
        Arguments:
        classes -- Can be empty, in which case restriction is removed.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_onlyAnnotatedWith(self, classes, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_onlyAnnotatedWith.begin(self, ((classes, ), _response, _ex, _sent, _ctx))

        """
        Restricts entities to having an
        ode.model.Annotation of all the given types. This
        is useful in combination with the other onlyAnnotated*
        methods to say, e.g., only annotated with a file by user X.
        By default, this value is null and imposes no
        restriction. Passing an empty array implies an object that
        is not annotated at all.
        Note: If the semantics were OR, then a client would have to
        query each class individually, and compare all the various
        values, checking which ids are where. However, since this
        method defaults to AND, multiple calls (optionally with
        {@code isMergedBatches} and {@code isReturnUnloaded})
        and combine the results. Duplicate ids are still possible
        so a set of some form should be used to collect the results.
        Arguments:
        classes -- Can be empty, in which case restriction is removed.
        """
        def end_onlyAnnotatedWith(self, _r):
            return _M_ode.api.Search._op_onlyAnnotatedWith.end(self, _r)

        """
        A path from the target entity which will be added to the
        current stack of order statements applied to the query.
        Arguments:
        path -- Non-null.
        _ctx -- The request context for the invocation.
        """
        def addOrderByAsc(self, path, _ctx=None):
            return _M_ode.api.Search._op_addOrderByAsc.invoke(self, ((path, ), _ctx))

        """
        A path from the target entity which will be added to the
        current stack of order statements applied to the query.
        Arguments:
        path -- Non-null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addOrderByAsc(self, path, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_addOrderByAsc.begin(self, ((path, ), _response, _ex, _sent, _ctx))

        """
        A path from the target entity which will be added to the
        current stack of order statements applied to the query.
        Arguments:
        path -- Non-null.
        """
        def end_addOrderByAsc(self, _r):
            return _M_ode.api.Search._op_addOrderByAsc.end(self, _r)

        """
        A path from the target entity which will be added to the
        current stack of order statements applied to the query.
        Arguments:
        path -- Non-null.
        _ctx -- The request context for the invocation.
        """
        def addOrderByDesc(self, path, _ctx=None):
            return _M_ode.api.Search._op_addOrderByDesc.invoke(self, ((path, ), _ctx))

        """
        A path from the target entity which will be added to the
        current stack of order statements applied to the query.
        Arguments:
        path -- Non-null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addOrderByDesc(self, path, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_addOrderByDesc.begin(self, ((path, ), _response, _ex, _sent, _ctx))

        """
        A path from the target entity which will be added to the
        current stack of order statements applied to the query.
        Arguments:
        path -- Non-null.
        """
        def end_addOrderByDesc(self, _r):
            return _M_ode.api.Search._op_addOrderByDesc.end(self, _r)

        """
        Removes the current stack of order statements.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def unordered(self, _ctx=None):
            return _M_ode.api.Search._op_unordered.invoke(self, ((), _ctx))

        """
        Removes the current stack of order statements.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_unordered(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_unordered.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Removes the current stack of order statements.
        Arguments:
        """
        def end_unordered(self, _r):
            return _M_ode.api.Search._op_unordered.end(self, _r)

        """
        Queries the database for all ode.model.Annotation
        annotations of the given types for all returned instances.
        Arguments:
        classes -- Can be empty, which removes previous fetch setting.
        _ctx -- The request context for the invocation.
        """
        def fetchAnnotations(self, classes, _ctx=None):
            return _M_ode.api.Search._op_fetchAnnotations.invoke(self, ((classes, ), _ctx))

        """
        Queries the database for all ode.model.Annotation
        annotations of the given types for all returned instances.
        Arguments:
        classes -- Can be empty, which removes previous fetch setting.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_fetchAnnotations(self, classes, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_fetchAnnotations.begin(self, ((classes, ), _response, _ex, _sent, _ctx))

        """
        Queries the database for all ode.model.Annotation
        annotations of the given types for all returned instances.
        Arguments:
        classes -- Can be empty, which removes previous fetch setting.
        """
        def end_fetchAnnotations(self, _r):
            return _M_ode.api.Search._op_fetchAnnotations.end(self, _r)

        """
        Adds a fetch clause for loading non-annotation fields of
        returned entities. Each fetch is a hibernate clause in dot
        notation.
        Arguments:
        fetches -- Can be empty, which removes previous fetch setting.
        _ctx -- The request context for the invocation.
        """
        def fetchAlso(self, fetches, _ctx=None):
            return _M_ode.api.Search._op_fetchAlso.invoke(self, ((fetches, ), _ctx))

        """
        Adds a fetch clause for loading non-annotation fields of
        returned entities. Each fetch is a hibernate clause in dot
        notation.
        Arguments:
        fetches -- Can be empty, which removes previous fetch setting.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_fetchAlso(self, fetches, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_fetchAlso.begin(self, ((fetches, ), _response, _ex, _sent, _ctx))

        """
        Adds a fetch clause for loading non-annotation fields of
        returned entities. Each fetch is a hibernate clause in dot
        notation.
        Arguments:
        fetches -- Can be empty, which removes previous fetch setting.
        """
        def end_fetchAlso(self, _r):
            return _M_ode.api.Search._op_fetchAlso.end(self, _r)

        """
        Resets all settings (non-query state) to the original
        default values, as if the instance had just be created.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def resetDefaults(self, _ctx=None):
            return _M_ode.api.Search._op_resetDefaults.invoke(self, ((), _ctx))

        """
        Resets all settings (non-query state) to the original
        default values, as if the instance had just be created.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetDefaults(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_resetDefaults.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Resets all settings (non-query state) to the original
        default values, as if the instance had just be created.
        Arguments:
        """
        def end_resetDefaults(self, _r):
            return _M_ode.api.Search._op_resetDefaults.end(self, _r)

        """
        Returns transient (without ID)
        ode.model.TagAnnotation instances which
        represent all the
        ode.model.TagAnnotation tags in the given group.
        The entities are transient and without ownership since
        multiple users can own the same tag. This method will
        override settings for types.
        Arguments:
        group -- Can be null or empty to return all tags.
        _ctx -- The request context for the invocation.
        """
        def byGroupForTags(self, group, _ctx=None):
            return _M_ode.api.Search._op_byGroupForTags.invoke(self, ((group, ), _ctx))

        """
        Returns transient (without ID)
        ode.model.TagAnnotation instances which
        represent all the
        ode.model.TagAnnotation tags in the given group.
        The entities are transient and without ownership since
        multiple users can own the same tag. This method will
        override settings for types.
        Arguments:
        group -- Can be null or empty to return all tags.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_byGroupForTags(self, group, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_byGroupForTags.begin(self, ((group, ), _response, _ex, _sent, _ctx))

        """
        Returns transient (without ID)
        ode.model.TagAnnotation instances which
        represent all the
        ode.model.TagAnnotation tags in the given group.
        The entities are transient and without ownership since
        multiple users can own the same tag. This method will
        override settings for types.
        Arguments:
        group -- Can be null or empty to return all tags.
        """
        def end_byGroupForTags(self, _r):
            return _M_ode.api.Search._op_byGroupForTags.end(self, _r)

        """
        Creates a query which will return transient (without ID)
        ode.model.TagAnnotation instances which represent
        all the ode.model.TagAnnotation tag groups which
        the given tag belongs to. The entities are transient and
        without ownership since multiple users can own the same tag
        group. This method will override settings for types.
        Arguments:
        tag -- Can be null or empty to return all groups.
        _ctx -- The request context for the invocation.
        """
        def byTagForGroups(self, tag, _ctx=None):
            return _M_ode.api.Search._op_byTagForGroups.invoke(self, ((tag, ), _ctx))

        """
        Creates a query which will return transient (without ID)
        ode.model.TagAnnotation instances which represent
        all the ode.model.TagAnnotation tag groups which
        the given tag belongs to. The entities are transient and
        without ownership since multiple users can own the same tag
        group. This method will override settings for types.
        Arguments:
        tag -- Can be null or empty to return all groups.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_byTagForGroups(self, tag, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_byTagForGroups.begin(self, ((tag, ), _response, _ex, _sent, _ctx))

        """
        Creates a query which will return transient (without ID)
        ode.model.TagAnnotation instances which represent
        all the ode.model.TagAnnotation tag groups which
        the given tag belongs to. The entities are transient and
        without ownership since multiple users can own the same tag
        group. This method will override settings for types.
        Arguments:
        tag -- Can be null or empty to return all groups.
        """
        def end_byTagForGroups(self, _r):
            return _M_ode.api.Search._op_byTagForGroups.end(self, _r)

        """
        Passes the query as is to the Lucene backend.
        Arguments:
        query -- May not be null or of zero length.
        _ctx -- The request context for the invocation.
        """
        def byFullText(self, query, _ctx=None):
            return _M_ode.api.Search._op_byFullText.invoke(self, ((query, ), _ctx))

        """
        Passes the query as is to the Lucene backend.
        Arguments:
        query -- May not be null or of zero length.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_byFullText(self, query, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_byFullText.begin(self, ((query, ), _response, _ex, _sent, _ctx))

        """
        Passes the query as is to the Lucene backend.
        Arguments:
        query -- May not be null or of zero length.
        """
        def end_byFullText(self, _r):
            return _M_ode.api.Search._op_byFullText.end(self, _r)

        """
        Builds a Lucene query and passes it to the Lucene backend.
        Arguments:
        fields -- The fields (comma separated) to search in (name, description, ...)
        _from -- The date range from, in the form YYYYMMDD (may be null)
        to -- The date range to (inclusive), in the form YYYYMMDD (may be null)
        dateType -- {@code DATE_TYPE_ACQUISITION} or {@code DATE_TYPE_IMPORT}
        query -- May not be null or of zero length.
        _ctx -- The request context for the invocation.
        """
        def byLuceneQueryBuilder(self, fields, _from, to, dateType, query, _ctx=None):
            return _M_ode.api.Search._op_byLuceneQueryBuilder.invoke(self, ((fields, _from, to, dateType, query), _ctx))

        """
        Builds a Lucene query and passes it to the Lucene backend.
        Arguments:
        fields -- The fields (comma separated) to search in (name, description, ...)
        _from -- The date range from, in the form YYYYMMDD (may be null)
        to -- The date range to (inclusive), in the form YYYYMMDD (may be null)
        dateType -- {@code DATE_TYPE_ACQUISITION} or {@code DATE_TYPE_IMPORT}
        query -- May not be null or of zero length.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_byLuceneQueryBuilder(self, fields, _from, to, dateType, query, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_byLuceneQueryBuilder.begin(self, ((fields, _from, to, dateType, query), _response, _ex, _sent, _ctx))

        """
        Builds a Lucene query and passes it to the Lucene backend.
        Arguments:
        fields -- The fields (comma separated) to search in (name, description, ...)
        _from -- The date range from, in the form YYYYMMDD (may be null)
        to -- The date range to (inclusive), in the form YYYYMMDD (may be null)
        dateType -- {@code DATE_TYPE_ACQUISITION} or {@code DATE_TYPE_IMPORT}
        query -- May not be null or of zero length.
        """
        def end_byLuceneQueryBuilder(self, _r):
            return _M_ode.api.Search._op_byLuceneQueryBuilder.end(self, _r)

        """
        Returns transient (without ID)
        ode.model.TextAnnotation instances which represent
        terms which are similar to the given terms. For example, if
        the argument is cell, one return value might have as
        its textValue: cellular while another has
        cellularize.
        No filtering or fetching is performed.
        Arguments:
        terms -- Cannot be empty.
        _ctx -- The request context for the invocation.
        """
        def bySimilarTerms(self, terms, _ctx=None):
            return _M_ode.api.Search._op_bySimilarTerms.invoke(self, ((terms, ), _ctx))

        """
        Returns transient (without ID)
        ode.model.TextAnnotation instances which represent
        terms which are similar to the given terms. For example, if
        the argument is cell, one return value might have as
        its textValue: cellular while another has
        cellularize.
        No filtering or fetching is performed.
        Arguments:
        terms -- Cannot be empty.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_bySimilarTerms(self, terms, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_bySimilarTerms.begin(self, ((terms, ), _response, _ex, _sent, _ctx))

        """
        Returns transient (without ID)
        ode.model.TextAnnotation instances which represent
        terms which are similar to the given terms. For example, if
        the argument is cell, one return value might have as
        its textValue: cellular while another has
        cellularize.
        No filtering or fetching is performed.
        Arguments:
        terms -- Cannot be empty.
        """
        def end_bySimilarTerms(self, _r):
            return _M_ode.api.Search._op_bySimilarTerms.end(self, _r)

        """
        Delegates to {@code ode.api.IQuery.findAllByQuery} method
        to take advantage of the {@code and}, {@code or}, and
        {@code not} methods, or queue-semantics.
        Arguments:
        query -- Not null.
        params -- May be null. Defaults are then in effect.
        _ctx -- The request context for the invocation.
        """
        def byHqlQuery(self, query, params, _ctx=None):
            return _M_ode.api.Search._op_byHqlQuery.invoke(self, ((query, params), _ctx))

        """
        Delegates to {@code ode.api.IQuery.findAllByQuery} method
        to take advantage of the {@code and}, {@code or}, and
        {@code not} methods, or queue-semantics.
        Arguments:
        query -- Not null.
        params -- May be null. Defaults are then in effect.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_byHqlQuery(self, query, params, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_byHqlQuery.begin(self, ((query, params), _response, _ex, _sent, _ctx))

        """
        Delegates to {@code ode.api.IQuery.findAllByQuery} method
        to take advantage of the {@code and}, {@code or}, and
        {@code not} methods, or queue-semantics.
        Arguments:
        query -- Not null.
        params -- May be null. Defaults are then in effect.
        """
        def end_byHqlQuery(self, _r):
            return _M_ode.api.Search._op_byHqlQuery.end(self, _r)

        """
        Builds a Lucene query and passes it to {@code byFullText}.
        Arguments:
        some -- Some (at least one) of these terms must be present in the document. May be null.
        must -- All of these terms must be present in the document. May be null.
        none -- None of these terms may be present in the document. May be null.
        _ctx -- The request context for the invocation.
        """
        def bySomeMustNone(self, some, must, none, _ctx=None):
            return _M_ode.api.Search._op_bySomeMustNone.invoke(self, ((some, must, none), _ctx))

        """
        Builds a Lucene query and passes it to {@code byFullText}.
        Arguments:
        some -- Some (at least one) of these terms must be present in the document. May be null.
        must -- All of these terms must be present in the document. May be null.
        none -- None of these terms may be present in the document. May be null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_bySomeMustNone(self, some, must, none, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_bySomeMustNone.begin(self, ((some, must, none), _response, _ex, _sent, _ctx))

        """
        Builds a Lucene query and passes it to {@code byFullText}.
        Arguments:
        some -- Some (at least one) of these terms must be present in the document. May be null.
        must -- All of these terms must be present in the document. May be null.
        none -- None of these terms may be present in the document. May be null.
        """
        def end_bySomeMustNone(self, _r):
            return _M_ode.api.Search._op_bySomeMustNone.end(self, _r)

        """
        Finds entities annotated with an
        ode.model.Annotation similar to the example. This
        does not use Hibernate's
        {@code ode.api.IQuery.findByExample} Query-By-Example}
        mechanism, since that cannot handle joins. The fields which
        are used are:
        the main content of the annotation : String,
        ode.model.OriginalFile#getId(), etc.
        If the main content is null it is assumed to
        be a wildcard searched, and only the type of the annotation
        is searched. Currently, ListAnnotations are not supported.
        Arguments:
        examples -- Not empty.
        _ctx -- The request context for the invocation.
        """
        def byAnnotatedWith(self, examples, _ctx=None):
            return _M_ode.api.Search._op_byAnnotatedWith.invoke(self, ((examples, ), _ctx))

        """
        Finds entities annotated with an
        ode.model.Annotation similar to the example. This
        does not use Hibernate's
        {@code ode.api.IQuery.findByExample} Query-By-Example}
        mechanism, since that cannot handle joins. The fields which
        are used are:
        the main content of the annotation : String,
        ode.model.OriginalFile#getId(), etc.
        If the main content is null it is assumed to
        be a wildcard searched, and only the type of the annotation
        is searched. Currently, ListAnnotations are not supported.
        Arguments:
        examples -- Not empty.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_byAnnotatedWith(self, examples, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_byAnnotatedWith.begin(self, ((examples, ), _response, _ex, _sent, _ctx))

        """
        Finds entities annotated with an
        ode.model.Annotation similar to the example. This
        does not use Hibernate's
        {@code ode.api.IQuery.findByExample} Query-By-Example}
        mechanism, since that cannot handle joins. The fields which
        are used are:
        the main content of the annotation : String,
        ode.model.OriginalFile#getId(), etc.
        If the main content is null it is assumed to
        be a wildcard searched, and only the type of the annotation
        is searched. Currently, ListAnnotations are not supported.
        Arguments:
        examples -- Not empty.
        """
        def end_byAnnotatedWith(self, _r):
            return _M_ode.api.Search._op_byAnnotatedWith.end(self, _r)

        """
        Removes all active queries (leaving {@code resetDefaults}
        settings alone), such that {@code activeQueries} will
        return 0.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def clearQueries(self, _ctx=None):
            return _M_ode.api.Search._op_clearQueries.invoke(self, ((), _ctx))

        """
        Removes all active queries (leaving {@code resetDefaults}
        settings alone), such that {@code activeQueries} will
        return 0.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_clearQueries(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_clearQueries.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Removes all active queries (leaving {@code resetDefaults}
        settings alone), such that {@code activeQueries} will
        return 0.
        Arguments:
        """
        def end_clearQueries(self, _r):
            return _M_ode.api.Search._op_clearQueries.end(self, _r)

        """
        Applies the next by* method to the previous by* method, so
        that a call {@code hasNext}, {@code next}, or
        {@code results} sees only the intersection of the two
        calls.
        For example,
        {@code
        service.onlyType(Image.class);
        service.byFullText(&quot;foo&quot;);
        service.intersection();
        service.byAnnotatedWith(TagAnnotation.class);
        }
        will return only the Images with TagAnnotations.
        Calling this method overrides a previous setting of
        {@code or} or {@code not}. If there is no active queries
        (i.e. {@code activeQueries > 0}), then an
        ode.ApiUsageException will be thrown.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def _and(self, _ctx=None):
            return _M_ode.api.Search._op_and.invoke(self, ((), _ctx))

        """
        Applies the next by* method to the previous by* method, so
        that a call {@code hasNext}, {@code next}, or
        {@code results} sees only the intersection of the two
        calls.
        For example,
        {@code
        service.onlyType(Image.class);
        service.byFullText(&quot;foo&quot;);
        service.intersection();
        service.byAnnotatedWith(TagAnnotation.class);
        }
        will return only the Images with TagAnnotations.
        Calling this method overrides a previous setting of
        {@code or} or {@code not}. If there is no active queries
        (i.e. {@code activeQueries > 0}), then an
        ode.ApiUsageException will be thrown.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_and(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_and.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Applies the next by* method to the previous by* method, so
        that a call {@code hasNext}, {@code next}, or
        {@code results} sees only the intersection of the two
        calls.
        For example,
        {@code
        service.onlyType(Image.class);
        service.byFullText(&quot;foo&quot;);
        service.intersection();
        service.byAnnotatedWith(TagAnnotation.class);
        }
        will return only the Images with TagAnnotations.
        Calling this method overrides a previous setting of
        {@code or} or {@code not}. If there is no active queries
        (i.e. {@code activeQueries > 0}), then an
        ode.ApiUsageException will be thrown.
        Arguments:
        """
        def end_and(self, _r):
            return _M_ode.api.Search._op_and.end(self, _r)

        """
        Applies the next by* method to the previous by* method, so
        that a call {@code hasNext}, {@code next} or
        {@code results} sees only the union of the two calls.
        For example,
        {@code
        service.onlyType(Image.class);
        service.byFullText(&quot;foo&quot;);
        service.or();
        service.onlyType(Dataset.class);
        service.byFullText(&quot;foo&quot;);
        }
        will return both Images and Datasets together.
        Calling this method overrides a previous setting of
        {@code and} or {@code not}. If there is no active queries
        (i.e. {@code activeQueries > 0}), then an
        ode.ApiUsageException will be thrown.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def _or(self, _ctx=None):
            return _M_ode.api.Search._op_or.invoke(self, ((), _ctx))

        """
        Applies the next by* method to the previous by* method, so
        that a call {@code hasNext}, {@code next} or
        {@code results} sees only the union of the two calls.
        For example,
        {@code
        service.onlyType(Image.class);
        service.byFullText(&quot;foo&quot;);
        service.or();
        service.onlyType(Dataset.class);
        service.byFullText(&quot;foo&quot;);
        }
        will return both Images and Datasets together.
        Calling this method overrides a previous setting of
        {@code and} or {@code not}. If there is no active queries
        (i.e. {@code activeQueries > 0}), then an
        ode.ApiUsageException will be thrown.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_or(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_or.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Applies the next by* method to the previous by* method, so
        that a call {@code hasNext}, {@code next} or
        {@code results} sees only the union of the two calls.
        For example,
        {@code
        service.onlyType(Image.class);
        service.byFullText(&quot;foo&quot;);
        service.or();
        service.onlyType(Dataset.class);
        service.byFullText(&quot;foo&quot;);
        }
        will return both Images and Datasets together.
        Calling this method overrides a previous setting of
        {@code and} or {@code not}. If there is no active queries
        (i.e. {@code activeQueries > 0}), then an
        ode.ApiUsageException will be thrown.
        Arguments:
        """
        def end_or(self, _r):
            return _M_ode.api.Search._op_or.end(self, _r)

        """
        Applies the next by* method to the previous by* method, so
        that a call {@code hasNext}, {@code next}, or
        {@code results} sees only the intersection of the two
        calls.
        For example,
        {@code
        service.onlyType(Image.class);
        service.byFullText(&quot;foo&quot;);
        service.complement();
        service.byAnnotatedWith(TagAnnotation.class);
        }
        will return all the Images not annotated with
        TagAnnotation. 
        Calling this method overrides a previous setting of
        {@code or} or {@code and}. If there is no active queries
        (i.e. {@code activeQueries > 0}), then an
        ode.ApiUsageException will be thrown.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def _not(self, _ctx=None):
            return _M_ode.api.Search._op_not.invoke(self, ((), _ctx))

        """
        Applies the next by* method to the previous by* method, so
        that a call {@code hasNext}, {@code next}, or
        {@code results} sees only the intersection of the two
        calls.
        For example,
        {@code
        service.onlyType(Image.class);
        service.byFullText(&quot;foo&quot;);
        service.complement();
        service.byAnnotatedWith(TagAnnotation.class);
        }
        will return all the Images not annotated with
        TagAnnotation. 
        Calling this method overrides a previous setting of
        {@code or} or {@code and}. If there is no active queries
        (i.e. {@code activeQueries > 0}), then an
        ode.ApiUsageException will be thrown.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_not(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_not.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Applies the next by* method to the previous by* method, so
        that a call {@code hasNext}, {@code next}, or
        {@code results} sees only the intersection of the two
        calls.
        For example,
        {@code
        service.onlyType(Image.class);
        service.byFullText(&quot;foo&quot;);
        service.complement();
        service.byAnnotatedWith(TagAnnotation.class);
        }
        will return all the Images not annotated with
        TagAnnotation. 
        Calling this method overrides a previous setting of
        {@code or} or {@code and}. If there is no active queries
        (i.e. {@code activeQueries > 0}), then an
        ode.ApiUsageException will be thrown.
        Arguments:
        """
        def end_not(self, _r):
            return _M_ode.api.Search._op_not.end(self, _r)

        """
        Returns true if another call to
        {@code next} is valid. A call to {@code next} may throw
        an exception for another reason, however.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def hasNext(self, _ctx=None):
            return _M_ode.api.Search._op_hasNext.invoke(self, ((), _ctx))

        """
        Returns true if another call to
        {@code next} is valid. A call to {@code next} may throw
        an exception for another reason, however.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_hasNext(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_hasNext.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns true if another call to
        {@code next} is valid. A call to {@code next} may throw
        an exception for another reason, however.
        Arguments:
        """
        def end_hasNext(self, _r):
            return _M_ode.api.Search._op_hasNext.end(self, _r)

        """
        Returns the next entity from the current query. If the
        previous call returned the last entity from a given query,
        the first entity from the next query will be returned and
        {@code activeQueries} decremented.
        Since this method only returns the entity itself, a single
        call to {@code currentMetadata} may follow this call to
        gather the extra metadata which is returned in the map via
        {@code results}.
        Arguments:
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- if {code hasNext} returns false.
        """
        def next(self, _ctx=None):
            return _M_ode.api.Search._op_next.invoke(self, ((), _ctx))

        """
        Returns the next entity from the current query. If the
        previous call returned the last entity from a given query,
        the first entity from the next query will be returned and
        {@code activeQueries} decremented.
        Since this method only returns the entity itself, a single
        call to {@code currentMetadata} may follow this call to
        gather the extra metadata which is returned in the map via
        {@code results}.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_next(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_next.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the next entity from the current query. If the
        previous call returned the last entity from a given query,
        the first entity from the next query will be returned and
        {@code activeQueries} decremented.
        Since this method only returns the entity itself, a single
        call to {@code currentMetadata} may follow this call to
        gather the extra metadata which is returned in the map via
        {@code results}.
        Arguments:
        Throws:
        ApiUsageException -- if {code hasNext} returns false.
        """
        def end_next(self, _r):
            return _M_ode.api.Search._op_next.end(self, _r)

        """
        Returns up to {@code getBatchSize} batch size number of
        results along with the related query metadata. If
        {@code isMergedBatches} batches are merged then the
        results from multiple queries may be returned together.
        Arguments:
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- if {@code hasNext} returns false.
        """
        def results(self, _ctx=None):
            return _M_ode.api.Search._op_results.invoke(self, ((), _ctx))

        """
        Returns up to {@code getBatchSize} batch size number of
        results along with the related query metadata. If
        {@code isMergedBatches} batches are merged then the
        results from multiple queries may be returned together.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_results(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_results.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns up to {@code getBatchSize} batch size number of
        results along with the related query metadata. If
        {@code isMergedBatches} batches are merged then the
        results from multiple queries may be returned together.
        Arguments:
        Throws:
        ApiUsageException -- if {@code hasNext} returns false.
        """
        def end_results(self, _r):
            return _M_ode.api.Search._op_results.end(self, _r)

        """
        Provides access to the extra query information (for example
        Lucene score and boost values) for a single call to
        {@code next}. This method may only be called once for any
        given call to {@code next}.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def currentMetadata(self, _ctx=None):
            return _M_ode.api.Search._op_currentMetadata.invoke(self, ((), _ctx))

        """
        Provides access to the extra query information (for example
        Lucene score and boost values) for a single call to
        {@code next}. This method may only be called once for any
        given call to {@code next}.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_currentMetadata(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_currentMetadata.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Provides access to the extra query information (for example
        Lucene score and boost values) for a single call to
        {@code next}. This method may only be called once for any
        given call to {@code next}.
        Arguments:
        """
        def end_currentMetadata(self, _r):
            return _M_ode.api.Search._op_currentMetadata.end(self, _r)

        """
        Provides access to the extra query information (for example
        Lucene score and boost values) for a single call to
        {@code results}. This method may only be called once for
        any given call to {@code results}.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def currentMetadataList(self, _ctx=None):
            return _M_ode.api.Search._op_currentMetadataList.invoke(self, ((), _ctx))

        """
        Provides access to the extra query information (for example
        Lucene score and boost values) for a single call to
        {@code results}. This method may only be called once for
        any given call to {@code results}.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_currentMetadataList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_currentMetadataList.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Provides access to the extra query information (for example
        Lucene score and boost values) for a single call to
        {@code results}. This method may only be called once for
        any given call to {@code results}.
        Arguments:
        """
        def end_currentMetadataList(self, _r):
            return _M_ode.api.Search._op_currentMetadataList.end(self, _r)

        """
        Unsupported operation.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def remove(self, _ctx=None):
            return _M_ode.api.Search._op_remove.invoke(self, ((), _ctx))

        """
        Unsupported operation.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_remove(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.Search._op_remove.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Unsupported operation.
        Arguments:
        """
        def end_remove(self, _r):
            return _M_ode.api.Search._op_remove.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.SearchPrx.ice_checkedCast(proxy, '::ode::api::Search', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.SearchPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::Search'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_SearchPrx = IcePy.defineProxy('::ode::api::Search', SearchPrx)

    _M_ode.api._t_Search = IcePy.defineClass('::ode::api::Search', Search, -1, (), True, False, None, (_M_ode.api._t_StatefulServiceInterface,), ())
    Search._ice_type = _M_ode.api._t_Search

    Search._op_activeQueries = IcePy.Operation('activeQueries', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    Search._op_setBatchSize = IcePy.Operation('setBatchSize', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_getBatchSize = IcePy.Operation('getBatchSize', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    Search._op_setMergedBatches = IcePy.Operation('setMergedBatches', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_bool, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_isMergedBatches = IcePy.Operation('isMergedBatches', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    Search._op_setCaseSentivice = IcePy.Operation('setCaseSentivice', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_bool, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_setCaseSentivice.deprecate("use setCaseSensitive instead")
    Search._op_setCaseSensitive = IcePy.Operation('setCaseSensitive', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_bool, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_isCaseSensitive = IcePy.Operation('isCaseSensitive', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    Search._op_setUseProjections = IcePy.Operation('setUseProjections', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_bool, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_isUseProjections = IcePy.Operation('isUseProjections', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    Search._op_setReturnUnloaded = IcePy.Operation('setReturnUnloaded', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_bool, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_isReturnUnloaded = IcePy.Operation('isReturnUnloaded', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    Search._op_setAllowLeadingWildcard = IcePy.Operation('setAllowLeadingWildcard', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_bool, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_isAllowLeadingWildcard = IcePy.Operation('isAllowLeadingWildcard', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    Search._op_onlyType = IcePy.Operation('onlyType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_onlyTypes = IcePy.Operation('onlyTypes', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_StringSet, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_allTypes = IcePy.Operation('allTypes', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    Search._op_onlyIds = IcePy.Operation('onlyIds', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.sys._t_LongList, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_onlyOwnedBy = IcePy.Operation('onlyOwnedBy', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Details, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_notOwnedBy = IcePy.Operation('notOwnedBy', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Details, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_onlyCreatedBetween = IcePy.Operation('onlyCreatedBetween', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode._t_RTime, False, 0), ((), _M_ode._t_RTime, False, 0)), (), None, (_M_ode._t_ServerError,))
    Search._op_onlyModifiedBetween = IcePy.Operation('onlyModifiedBetween', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode._t_RTime, False, 0), ((), _M_ode._t_RTime, False, 0)), (), None, (_M_ode._t_ServerError,))
    Search._op_onlyAnnotatedBetween = IcePy.Operation('onlyAnnotatedBetween', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode._t_RTime, False, 0), ((), _M_ode._t_RTime, False, 0)), (), None, (_M_ode._t_ServerError,))
    Search._op_onlyAnnotatedBy = IcePy.Operation('onlyAnnotatedBy', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Details, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_notAnnotatedBy = IcePy.Operation('notAnnotatedBy', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Details, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_onlyAnnotatedWith = IcePy.Operation('onlyAnnotatedWith', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_StringSet, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_addOrderByAsc = IcePy.Operation('addOrderByAsc', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_addOrderByDesc = IcePy.Operation('addOrderByDesc', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_unordered = IcePy.Operation('unordered', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    Search._op_fetchAnnotations = IcePy.Operation('fetchAnnotations', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_StringSet, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_fetchAlso = IcePy.Operation('fetchAlso', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_StringSet, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_resetDefaults = IcePy.Operation('resetDefaults', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    Search._op_byGroupForTags = IcePy.Operation('byGroupForTags', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_byTagForGroups = IcePy.Operation('byTagForGroups', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_byFullText = IcePy.Operation('byFullText', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_byLuceneQueryBuilder = IcePy.Operation('byLuceneQueryBuilder', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    Search._op_bySimilarTerms = IcePy.Operation('bySimilarTerms', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_StringSet, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_byHqlQuery = IcePy.Operation('byHqlQuery', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), None, (_M_ode._t_ServerError,))
    Search._op_bySomeMustNone = IcePy.Operation('bySomeMustNone', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_StringSet, False, 0), ((), _M_ode.api._t_StringSet, False, 0), ((), _M_ode.api._t_StringSet, False, 0)), (), None, (_M_ode._t_ServerError,))
    Search._op_byAnnotatedWith = IcePy.Operation('byAnnotatedWith', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_AnnotationList, False, 0),), (), None, (_M_ode._t_ServerError,))
    Search._op_clearQueries = IcePy.Operation('clearQueries', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    Search._op_and = IcePy.Operation('and', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    Search._op_or = IcePy.Operation('or', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    Search._op_not = IcePy.Operation('not', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    Search._op_hasNext = IcePy.Operation('hasNext', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    Search._op_next = IcePy.Operation('next', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    Search._op_results = IcePy.Operation('results', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    Search._op_currentMetadata = IcePy.Operation('currentMetadata', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_SearchMetadata, False, 0), (_M_ode._t_ServerError,))
    Search._op_currentMetadataList = IcePy.Operation('currentMetadataList', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_SearchMetadataList, False, 0), (_M_ode._t_ServerError,))
    Search._op_remove = IcePy.Operation('remove', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))

    _M_ode.api.Search = Search
    del Search

    _M_ode.api.SearchPrx = SearchPrx
    del SearchPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
