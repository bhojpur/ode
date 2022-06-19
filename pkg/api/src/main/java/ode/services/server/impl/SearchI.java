package ode.services.server.impl;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import java.util.List;

import ode.api.Search;
import ode.services.server.util.ServerExecutor;
import ode.RTime;
import ode.ServerError;
import ode.api.AMD_Search_activeQueries;
import ode.api.AMD_Search_addOrderByAsc;
import ode.api.AMD_Search_addOrderByDesc;
import ode.api.AMD_Search_allTypes;
import ode.api.AMD_Search_and;
import ode.api.AMD_Search_byAnnotatedWith;
import ode.api.AMD_Search_byFullText;
import ode.api.AMD_Search_byLuceneQueryBuilder;
import ode.api.AMD_Search_byGroupForTags;
import ode.api.AMD_Search_byHqlQuery;
import ode.api.AMD_Search_bySimilarTerms;
import ode.api.AMD_Search_bySomeMustNone;
import ode.api.AMD_Search_byTagForGroups;
import ode.api.AMD_Search_clearQueries;
import ode.api.AMD_Search_currentMetadata;
import ode.api.AMD_Search_currentMetadataList;
import ode.api.AMD_Search_fetchAlso;
import ode.api.AMD_Search_fetchAnnotations;
import ode.api.AMD_Search_getBatchSize;
import ode.api.AMD_Search_hasNext;
import ode.api.AMD_Search_isAllowLeadingWildcard;
import ode.api.AMD_Search_isCaseSensitive;
import ode.api.AMD_Search_isMergedBatches;
import ode.api.AMD_Search_isReturnUnloaded;
import ode.api.AMD_Search_isUseProjections;
import ode.api.AMD_Search_next;
import ode.api.AMD_Search_not;
import ode.api.AMD_Search_notAnnotatedBy;
import ode.api.AMD_Search_notOwnedBy;
import ode.api.AMD_Search_onlyAnnotatedBetween;
import ode.api.AMD_Search_onlyAnnotatedBy;
import ode.api.AMD_Search_onlyAnnotatedWith;
import ode.api.AMD_Search_onlyCreatedBetween;
import ode.api.AMD_Search_onlyIds;
import ode.api.AMD_Search_onlyModifiedBetween;
import ode.api.AMD_Search_onlyOwnedBy;
import ode.api.AMD_Search_onlyType;
import ode.api.AMD_Search_onlyTypes;
import ode.api.AMD_Search_or;
import ode.api.AMD_Search_remove;
import ode.api.AMD_Search_resetDefaults;
import ode.api.AMD_Search_results;
import ode.api.AMD_Search_setAllowLeadingWildcard;
import ode.api.AMD_Search_setBatchSize;
import ode.api.AMD_Search_setCaseSensitive;
import ode.api.AMD_Search_setCaseSentivice;
import ode.api.AMD_Search_setMergedBatches;
import ode.api.AMD_Search_setReturnUnloaded;
import ode.api.AMD_Search_setUseProjections;
import ode.api.AMD_Search_unordered;
import ode.api._SearchOperations;
import ode.model.Annotation;
import ode.model.Details;
import ode.sys.Parameters;
import ode.util.IceMapper;
import Ice.Current;

/**
 * Implementation of the Search service.
 * @see ode.api.Search
 */
public class SearchI extends AbstractCloseableAmdServant implements _SearchOperations {

    public SearchI(Search service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void activeQueries_async(AMD_Search_activeQueries __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void addOrderByAsc_async(AMD_Search_addOrderByAsc __cb, String path,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, path);

    }

    public void addOrderByDesc_async(AMD_Search_addOrderByDesc __cb,
            String path, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, path);

    }

    public void allTypes_async(AMD_Search_allTypes __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void and_async(AMD_Search_and __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void byAnnotatedWith_async(AMD_Search_byAnnotatedWith __cb,
            List<Annotation> examples, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, examples);

    }

    public void byFullText_async(AMD_Search_byFullText __cb, String query,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, query);

    }
    
    public void byLuceneQueryBuilder_async(AMD_Search_byLuceneQueryBuilder __cb, String fields, String from,
            String to, String dateType, String query, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, fields, from,
                to, dateType, query);

    }

    public void byGroupForTags_async(AMD_Search_byGroupForTags __cb,
            String group, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, group);

    }

    public void byHqlQuery_async(AMD_Search_byHqlQuery __cb, String query,
            Parameters params, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, query, params);

    }

    public void bySomeMustNone_async(AMD_Search_bySomeMustNone __cb,
            List<String> some, List<String> must, List<String> none,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, some, must, none);

    }

    public void byTagForGroups_async(AMD_Search_byTagForGroups __cb,
            String tag, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, tag);

    }

    public void clearQueries_async(AMD_Search_clearQueries __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void currentMetadataList_async(AMD_Search_currentMetadataList __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void currentMetadata_async(AMD_Search_currentMetadata __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void fetchAlso_async(AMD_Search_fetchAlso __cb,
            List<String> fetches, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, fetches);

    }

    public void fetchAnnotations_async(AMD_Search_fetchAnnotations __cb,
            List<String> classes, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, classes);

    }

    public void getBatchSize_async(AMD_Search_getBatchSize __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void hasNext_async(AMD_Search_hasNext __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void isAllowLeadingWildcard_async(
            AMD_Search_isAllowLeadingWildcard __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void isCaseSensitive_async(AMD_Search_isCaseSensitive __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void isMergedBatches_async(AMD_Search_isMergedBatches __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void isReturnUnloaded_async(AMD_Search_isReturnUnloaded __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void isUseProjections_async(AMD_Search_isUseProjections __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void next_async(AMD_Search_next __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void notAnnotatedBy_async(AMD_Search_notAnnotatedBy __cb, Details d,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, d);

    }

    public void notOwnedBy_async(AMD_Search_notOwnedBy __cb, Details d,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, d);

    }

    public void not_async(AMD_Search_not __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void onlyAnnotatedBetween_async(
            AMD_Search_onlyAnnotatedBetween __cb, RTime start, RTime stop,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, start, stop);

    }

    public void onlyAnnotatedBy_async(AMD_Search_onlyAnnotatedBy __cb,
            Details d, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, d);

    }

    public void onlyAnnotatedWith_async(AMD_Search_onlyAnnotatedWith __cb,
            List<String> classes, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, classes);

    }

    public void onlyCreatedBetween_async(AMD_Search_onlyCreatedBetween __cb,
            RTime start, RTime stop, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, start, stop);

    }

    public void onlyIds_async(AMD_Search_onlyIds __cb, List<Long> ids,
            Current __current) throws ServerError {

        IceMapper mapper = new IceMapper(IceMapper.VOID);
        Long[] array = new Long[0];
        if (ids != null) {
            array = ids.toArray(array);
        }
        callInvokerOnMappedArgs(mapper, __cb, __current, (Object) array);

    }

    public void onlyModifiedBetween_async(AMD_Search_onlyModifiedBetween __cb,
            RTime start, RTime stop, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, start, stop);

    }

    public void onlyOwnedBy_async(AMD_Search_onlyOwnedBy __cb, Details d,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, d);

    }

    public void onlyType_async(AMD_Search_onlyType __cb, String klass,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, klass);

    }

    public void onlyTypes_async(AMD_Search_onlyTypes __cb,
            List<String> classes, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, classes);

    }

    public void or_async(AMD_Search_or __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void remove_async(AMD_Search_remove __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void resetDefaults_async(AMD_Search_resetDefaults __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void results_async(AMD_Search_results __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void setAllowLeadingWildcard_async(
            AMD_Search_setAllowLeadingWildcard __cb,
            boolean allowLeadingWildcard, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, allowLeadingWildcard);

    }

    public void setBatchSize_async(AMD_Search_setBatchSize __cb, int size,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, size);

    }

    public void setCaseSentivice_async(AMD_Search_setCaseSentivice __cb,
            boolean caseSensitive, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, caseSensitive);

    }

    public void setCaseSensitive_async(AMD_Search_setCaseSensitive __cb,
            boolean caseSensitive, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, caseSensitive);

    }

    public void setMergedBatches_async(AMD_Search_setMergedBatches __cb,
            boolean merge, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, merge);

    }

    public void setReturnUnloaded_async(AMD_Search_setReturnUnloaded __cb,
            boolean returnUnloaded, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, returnUnloaded);

    }

    public void setUseProjections_async(AMD_Search_setUseProjections __cb,
            boolean useProjections, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, useProjections);

    }

    public void unordered_async(AMD_Search_unordered __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void bySimilarTerms_async(AMD_Search_bySimilarTerms __cb,
            List<String> terms, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, terms);
    }

    //
    // Close logic
    //

    @Override
    protected void preClose(Current current) throws Throwable {
        // no-op
    }

    @Override
    protected void postClose(Current current) {
        // no-op
    }

}