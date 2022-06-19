/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_API_SEARCH_ICE
#define ODE_API_SEARCH_ICE

#include <ode/ServicesF.ice>
#include <ode/System.ice>
#include <ode/Collections.ice>

module ode {

    module api {

        /**
         * Central search interface, allowing Web2.0 style queries. Each
         * {@link ode.api.Search} instance keeps up with several queries and
         * lazily-loads the results as {@code hasNext}, {@code next} and
         * {@code results} are called. These queries are created by the
         * <i>by*</i> methods.
         *
         * Each instance also has a number of settings which can all be
         * changed from their defaults via accessors, e.g.
         * {@code setBatchSize} or {@code setCaseSensitive}.
         *
         * The only methods which are required for the proper functioning of a
         * {@link Search} instance are:
         * <ul>
         * <li>{@code onlyType}, {@code onlyTypes} OR
         * {@code allTypes}</li>
         * <li>Any <i>by*</i> method to create a query</li>
         * </ul>
         * Use of the {@code allTypes} method is discouraged, since it is
         * possibly very resource intensive, which is why any attempt to
         * receive results without specifically setting types or allowing all
         * is prohibited.
         *
         * @see ode.api.IQuery
         */
        ["ami", "amd"] interface Search extends StatefulServiceInterface
            {

                // Non-query state ~~~~~~~~~~~~~~~~~~~~~~

                /**
                 * Returns the number of active queries. This means that
                 * {@code activeQueries} gives the minimum number of
                 * remaining calls to {@code results} when batches are not
                 * merged.
                 *
                 * @return number of active queries
                 */
                idempotent int activeQueries() throws ServerError;

                /**
                 * Sets the maximum number of results that will be returned by
                 * one call to {@code results}. If batches are not merged,
                 * then results may often be less than the batch size. If
                 * batches are merged, then only the last call to
                 * {@code results} can be less than batch size.
                 *
                 * Note: some query types may not support batching at the
                 * query level, and all instances must then be loaded into
                 * memory simultaneously.
                 *
                 * @param size maximum number of results per call to
                 *             {@code results}
                 */
                idempotent void setBatchSize(int size) throws ServerError;

                /**
                 * Returns the current batch size. If {@code setBatchSize}
                 * has not been called, the default value will be in effect.
                 *
                 * @return maximum number of results per call to {@code results}
                 */
                idempotent int getBatchSize() throws ServerError;

                /**
                 * Set whether or not results from two separate queries can be
                 * returned in the same call to {@code results}.
                 */
                idempotent void setMergedBatches(bool merge) throws ServerError;

                /**
                 * Returns the current merged-batches setting. If
                 * {@code setMergedBatches} has not been called, the
                 * default value will be in effect.
                 */
                idempotent bool isMergedBatches() throws ServerError;

                /**
                 * Sets the case sensitivity on all queries where
                 * case-sensitivity is supported.
                 */
                ["deprecate:use setCaseSensitive instead"]
                idempotent void setCaseSentivice(bool caseSensitive) throws ServerError;

                /**
                 * Sets the case sensitivity on all queries where
                 * case-sensitivity is supported.
                 */
                idempotent void setCaseSensitive(bool caseSensitive) throws ServerError;

                /**
                 * Returns the current case sensitivity setting. If
                 * {@code setCaseSensitive} has not been called, the
                 * default value will be in effect.
                 */
                idempotent bool isCaseSensitive() throws ServerError;

                /**
                 * Determines if Lucene queries should not hit the database.
                 * Instead all values which are stored in the index will be
                 * loaded into the object, which includes the id. However, the
                 * entity will not be marked unloaded and therefore it is
                 * especially important to not allow a projection-instance to
                 * be saved back to the server. This can result in DATA LOSS.
                 */
                idempotent void setUseProjections(bool useProjections) throws ServerError;

                /**
                 * Returns the current use-projection setting. If true, the
                 * client must be careful with all results that are returned.
                 * See {@code setUseProjections} for more. If
                 * {@code setUseProjections} has not been called, the
                 * default value will be in effect.
                 */
                idempotent bool isUseProjections() throws ServerError;

                /**
                 * Determines if all results should be returned as unloaded
                 * objects. This is particularly useful for creating lists for
                 * further querying via {@link ode.api.IQuery}. This value
                 * overrides the {@code setUseProjections} setting.
                 */
                idempotent void setReturnUnloaded(bool returnUnloaded) throws ServerError;

                /**
                 * Returns the current return-unloaded setting. If true, all
                 * returned entities will be unloaded. If
                 * {@code setReturnUnloaded} has not been called, the
                 * default value will be in effect.
                 */
                idempotent bool isReturnUnloaded() throws ServerError;

                /**
                 * Permits full-text queries with a leading query if true.
                 *
                 * @see #isAllowLeadingWildcard
                 * @see #byFullText
                 * @see #bySomeMustNone
                 */
                idempotent void setAllowLeadingWildcard(bool allowLeadingWildcard) throws ServerError;

                /**
                 * Returns the current leading-wildcard setting. If false,
                 * {@code byFullText} and {@code bySomeMustNone} will throw
                 * an {@link ode.ApiUsageException}, since leading-wildcard
                 * searches are quite slow. Use
                 * {@code setAllowLeadingWildcard} in order to permit this
                 * usage.
                 */
                idempotent bool isAllowLeadingWildcard() throws ServerError;


                // Filters ~~~~~~~~~~~~~~~~~~~~~~

                /**
                 * Restricts the search to a single type. All return values
                 * will match this type.
                 */
                void onlyType(string klass) throws ServerError;

                /**
                 * Restricts searches to a set of types. The entities returned
                 * are guaranteed to be one of these types.
                 */
                void onlyTypes(StringSet classes) throws ServerError;

                /**
                 * Permits all types to be returned. For some types of
                 * queries, this carries a performance penalty as every
                 * database table must be hit.
                 */
                void allTypes() throws ServerError;

                /**
                 * Restricts the set of ids which will be checked.
                 * This is useful for testing one of the given restrictions on
                 * a reduced set of objects.
                 *
                 * @param ids Can be null, in which case the previous
                 *            restriction is removed.
                 */
                void onlyIds(ode::sys::LongList ids) throws ServerError;

                /**
                 * Uses the {@link ode.model.Details#getOwner()} and
                 * {@link ode.model.Details#getGroup()} information to
                 * restrict the entities which will be returned. If both are
                 * non-null, the two restrictions are joined by an AND.
                 *
                 * @param d Can be null, in which case the previous
                 *          restriction is removed.
                 */
                void onlyOwnedBy(ode::model::Details d) throws ServerError;

                /**
                 * Uses the {@link ode.model.Details#getOwner()} and
                 * {@link ode.model.Details#getGroup()} information to
                 * restrict the entities which will be returned. If both are
                 * non-null, the two restrictions are joined by an AND.
                 *
                 * @param d Can be null, in which case the previous
                 *          restriction is removed.
                 */
                void notOwnedBy(ode::model::Details d) throws ServerError;

                /**
                 * Restricts the time between which an entity may have been
                 * created.
                 *
                 * @param start Can be null, i.e. interval open to negative
                 *              infinity.
                 * @param stop Can be null, i.e. interval opens to positive
                 *             infinity.
                 */
                void onlyCreatedBetween(ode::RTime start, ode::RTime  stop) throws ServerError;

                /**
                 * Restricts the time between which an entity may have last
                 * been modified.
                 *
                 * @param start Can be null, i.e. interval open to negative
                 *              infinity.
                 * @param stop Can be null, i.e. interval open to positive
                 *             infinity.
                 */
                void onlyModifiedBetween(ode::RTime start, ode::RTime stop) throws ServerError;

                /**
                 * Restricts entities by the time in which any annotation
                 * (which matches the other filters) was added them. This
                 * matches the {@link ode.model.Details#getCreationEvent()}
                 * creation event of the {@link ode.model.Annotation}.
                 *
                 * @param start Can be null, i.e. interval open to negative
                 *              infinity.
                 * @param stop Can be null, i.e. interval open to positive
                 *             infinity.
                 */
                void onlyAnnotatedBetween(ode::RTime start, ode::RTime stop) throws ServerError;

                /**
                 * Restricts entities by who has annotated them with an
                 * {@link ode.model.Annotation} matching the other filters.
                 * As {@code onlyOwnedBy}, the
                 * {@link ode.model.Details#getOwner()} and
                 * {@link ode.model.Details#getGroup()} information is
                 * combined with an AND condition.
                 *
                 * @param d Can be null, in which case any previous
                 *          restriction is removed.
                 */
                void onlyAnnotatedBy(ode::model::Details d) throws ServerError;

                /**
                 * Restricts entities by who has not annotated them with an
                 * {@link ode.model.Annotation} matching the other filters.
                 * As {@code notOwnedBy}, the
                 * {@link ode.model.Details#getOwner()} and
                 * {@link ode.model.Details#getGroup()} information is
                 * combined with an AND condition.
                 *
                 * @param d Can be null, in which case any previous
                 *          restriction is removed.
                 */
                void notAnnotatedBy(ode::model::Details d) throws ServerError;

                /**
                 * Restricts entities to having an
                 * {@link ode.model.Annotation} of all the given types. This
                 * is useful in combination with the other onlyAnnotated*
                 * methods to say, e.g., only annotated with a file by user X.
                 * By default, this value is <code>null</code> and imposes no
                 * restriction. Passing an empty array implies an object that
                 * is not annotated at all.
                 *
                 *
                 * Note: If the semantics were OR, then a client would have to
                 * query each class individually, and compare all the various
                 * values, checking which ids are where. However, since this
                 * method defaults to AND, multiple calls (optionally with
                 * {@code isMergedBatches} and {@code isReturnUnloaded})
                 * and combine the results. Duplicate ids are still possible
                 * so a set of some form should be used to collect the results.
                 *
                 * @param classes Can be empty, in which case restriction is
                 *                removed.
                 */
                void onlyAnnotatedWith(StringSet classes) throws ServerError;


                // Fetches, order, counts, etc. ~~~~~~~~~~~~~~~~~~~~~~

                /**
                 * A path from the target entity which will be added to the
                 * current stack of order statements applied to the query.
                 *
                 * @param path Non-null.
                 * @see #unordered
                 */
                void addOrderByAsc(string path) throws ServerError;

                /**
                 * A path from the target entity which will be added to the
                 * current stack of order statements applied to the query.
                 *
                 * @param path Non-null.
                 * @see #unordered
                 */
                void addOrderByDesc(string path) throws ServerError;

                /**
                 * Removes the current stack of order statements.
                 *
                 * @see #addOrderByAsc
                 * @see #addOrderByDesc
                 */
                void unordered() throws ServerError;

                /**
                 * Queries the database for all {@link ode.model.Annotation}
                 * annotations of the given types for all returned instances.
                 *
                 * @param classes Can be empty, which removes previous fetch
                 *                setting.
                 */
                void fetchAnnotations(StringSet classes) throws ServerError;

                /**
                 * Adds a fetch clause for loading non-annotation fields of
                 * returned entities. Each fetch is a hibernate clause in dot
                 * notation.
                 *
                 * @param fetches Can be empty, which removes previous fetch
                 *                setting.
                 */
                void fetchAlso(StringSet fetches) throws ServerError;

                // Reset ~~~~~~~~~~~~~~~~~~~~~~~~~

                /**
                 * Resets all settings (non-query state) to the original
                 * default values, as if the instance had just be created.
                 */
                void resetDefaults() throws ServerError;

                // Query state  ~~~~~~~~~~~~~~~~~~~~~~~~~

                /**
                 * Returns transient (without ID)
                 * {@link ode.model.TagAnnotation} instances which
                 * represent all the
                 * {@link ode.model.TagAnnotation} tags in the given group.
                 * The entities are transient and without ownership since
                 * multiple users can own the same tag. This method will
                 * override settings for types.
                 *
                 * @param group Can be null or empty to return all tags.
                 */
                void byGroupForTags(string group) throws ServerError;

                /**
                 * Creates a query which will return transient (without ID)
                 * {@link ode.model.TagAnnotation} instances which represent
                 * all the {@link ode.model.TagAnnotation} tag groups which
                 * the given tag belongs to. The entities are transient and
                 * without ownership since multiple users can own the same tag
                 * group. This method will override settings for types.
                 *
                 * @param tag Can be null or empty to return all groups.
                 */
                void byTagForGroups(string tag) throws ServerError;

                /**
                 * Passes the query as is to the Lucene backend.
                 *
                 * @param query May not be null or of zero length.
                 */
                void byFullText(string query) throws ServerError;

                /**
                 * Builds a Lucene query and passes it to the Lucene backend.
                 *
                 * @param fields   The fields (comma separated) to search in
                 *                 (name, description, ...)
                 * @param from     The date range from, in the form YYYYMMDD
                 *                 (may be null)
                 * @param to       The date range to (inclusive), in the form
                 *                 YYYYMMDD (may be null)
                 * @param dateType {@code DATE_TYPE_ACQUISITION} or
                 *                 {@code DATE_TYPE_IMPORT}
                 * @param query May not be null or of zero length.
                 */
                void byLuceneQueryBuilder(string fields, string from, string to, string dateType, string query) throws ServerError;

                /**
                 * Returns transient (without ID)
                 * {@link ode.model.TextAnnotation} instances which represent
                 * terms which are similar to the given terms. For example, if
                 * the argument is <i>cell</i>, one return value might have as
                 * its textValue: <i>cellular</i> while another has
                 * <i>cellularize</i>.
                 *
                 * No filtering or fetching is performed.
                 *
                 * @param terms Cannot be empty.
                 */
                void bySimilarTerms(StringSet terms) throws ServerError;

                /**
                 * Delegates to {@code ode.api.IQuery.findAllByQuery} method
                 * to take advantage of the {@code and}, {@code or}, and
                 * {@code not} methods, or queue-semantics.
                 *
                 * @param query Not null.
                 * @param params May be null. Defaults are then in effect.
                 */
                void byHqlQuery(string query, ode::sys::Parameters params) throws ServerError;

                /**
                 * Builds a Lucene query and passes it to {@code byFullText}.
                 *
                 * @param some Some (at least one) of these terms must be
                 *             present in the document. May be null.
                 * @param must All of these terms must be present in the
                 *             document. May be null.
                 * @param none None of these terms may be present in the
                 *             document. May be null.
                 */
                void bySomeMustNone(StringSet some, StringSet must, StringSet none) throws ServerError;

                /**
                 * Finds entities annotated with an
                 * {@link ode.model.Annotation} similar to the example. This
                 * does not use Hibernate's
                 * {@code ode.api.IQuery.findByExample} Query-By-Example}
                 * mechanism, since that cannot handle joins. The fields which
                 * are used are:
                 * <ul>
                 * <li>the main content of the annotation : String,
                 * {@link ode.model.OriginalFile#getId()}, etc.</li>
                 * </ul>
                 *
                 * If the main content is <code>null</code> it is assumed to
                 * be a wildcard searched, and only the type of the annotation
                 * is searched. Currently, ListAnnotations are not supported.
                 *
                 *
                 * @param examples Not empty.
                 */
                void byAnnotatedWith(AnnotationList examples) throws ServerError;

                /**
                 * Removes all active queries (leaving {@code resetDefaults}
                 * settings alone), such that {@code activeQueries} will
                 * return 0.
                 */
                void clearQueries() throws ServerError;

                /**
                 * Applies the next by* method to the previous by* method, so
                 * that a call {@code hasNext}, {@code next}, or
                 * {@code results} sees only the intersection of the two
                 * calls.
                 *
                 * For example,
                 *
                 * <pre>
                 * {@code
                 * service.onlyType(Image.class);
                 * service.byFullText(&quot;foo&quot;);
                 * service.intersection();
                 * service.byAnnotatedWith(TagAnnotation.class);
                 * }
                 * </pre>
                 *
                 * will return only the Images with TagAnnotations.
                 *
                 * <p>
                 * Calling this method overrides a previous setting of
                 * {@code or} or {@code not}. If there is no active queries
                 * (i.e. {@code activeQueries > 0}), then an
                 * {@link ode.ApiUsageException} will be thrown.</p>
                 */
                void and() throws ServerError;

                /**
                 * Applies the next by* method to the previous by* method, so
                 * that a call {@code hasNext}, {@code next} or
                 * {@code results} sees only the union of the two calls.
                 *
                 * For example,
                 *
                 * <pre>
                 * {@code
                 * service.onlyType(Image.class);
                 * service.byFullText(&quot;foo&quot;);
                 * service.or();
                 * service.onlyType(Dataset.class);
                 * service.byFullText(&quot;foo&quot;);
                 * }
                 * </pre>
                 *
                 * will return both Images and Datasets together.
                 *
                 * Calling this method overrides a previous setting of
                 * {@code and} or {@code not}. If there is no active queries
                 * (i.e. {@code activeQueries > 0}), then an
                 * {@link ode.ApiUsageException} will be thrown.
                 */
                void or() throws ServerError;

                /**
                 * Applies the next by* method to the previous by* method, so
                 * that a call {@code hasNext}, {@code next}, or
                 * {@code results} sees only the intersection of the two
                 * calls.
                 * 
                 * For example,
                 * 
                 * <pre>
                 * {@code
                 * service.onlyType(Image.class);
                 * service.byFullText(&quot;foo&quot;);
                 * service.complement();
                 * service.byAnnotatedWith(TagAnnotation.class);
                 * }
                 * </pre>
                 * 
                 * will return all the Images <em>not</em> annotated with
                 * TagAnnotation. <p>
                 * Calling this method overrides a previous setting of
                 * {@code or} or {@code and}. If there is no active queries
                 * (i.e. {@code activeQueries > 0}), then an
                 * {@link ode.ApiUsageException} will be thrown.
                 * </p>
                 */
                void not() throws ServerError;

                // Retrieval  ~~~~~~~~~~~~~~~~~~~~~~~~~

                /**
                 * Returns <code>true</code> if another call to
                 * {@code next} is valid. A call to {@code next} may throw
                 * an exception for another reason, however.
                 */
                idempotent bool hasNext() throws ServerError;

                /**
                 * Returns the next entity from the current query. If the
                 * previous call returned the last entity from a given query,
                 * the first entity from the next query will be returned and
                 * {@code activeQueries} decremented.
                 * Since this method only returns the entity itself, a single
                 * call to {@code currentMetadata} may follow this call to
                 * gather the extra metadata which is returned in the map via
                 * {@code results}.
                 * 
                 * @throws ApiUsageException if {code hasNext} returns false.
                 */
                ode::model::IObject next() throws ServerError;

                /**
                 * Returns up to {@code getBatchSize} batch size number of
                 * results along with the related query metadata. If
                 * {@code isMergedBatches} batches are merged then the
                 * results from multiple queries may be returned together.
                 * 
                 * @throws ApiUsageException if {@code hasNext} returns false.
                 */
                IObjectList results() throws ServerError;

                // Currently unused
                /**
                 * Provides access to the extra query information (for example
                 * Lucene score and boost values) for a single call to
                 * {@code next}. This method may only be called once for any
                 * given call to {@code next}.
                 */
                idempotent SearchMetadata currentMetadata() throws ServerError;

                /**
                 * Provides access to the extra query information (for example
                 * Lucene score and boost values) for a single call to
                 * {@code results}. This method may only be called once for
                 * any given call to {@code results}.
                 */
                idempotent SearchMetadataList currentMetadataList() throws ServerError;

                // Unused; Part of Java Iterator interface
                /**
                 * Unsupported operation.
                 */
                void remove() throws ServerError;
            };

    };
};

#endif