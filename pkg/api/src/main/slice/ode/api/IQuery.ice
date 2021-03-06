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

#ifndef ODE_API_IQUERY_ICE
#define ODE_API_IQUERY_ICE

#include <ode/ModelF.ice>
#include <ode/ServicesF.ice>
#include <ode/System.ice>
#include <ode/Collections.ice>

module ode {

    module api {

        /**
         * Provides methods for directly querying object graphs. As far as is
         * possible, IQuery should be considered the lowest level DB-access
         * (SELECT) interface.
         * Unlike the {@link ode.api.IUpdate} interface, using other methods
         * will most likely not leave the database in an inconsistent state,
         * but may provide stale data in some situations.
         *
         * By convention, all methods that begin with <code>get</code> will
         * never return a null or empty {@link java.util.Collection}, but
         * instead will throw a {@link ode.ValidationException}.
         *
         */
        ["ami", "amd"] interface IQuery extends ServiceInterface
            {

                /**
                 * Looks up an entity by class and id. If no such object
                 * exists, an exception will be thrown.
                 *
                 * @param klass the type of the entity. Not null.
                 * @param id the entity's id
                 * @return an initialized entity
                 * @throws ValidationException if the id doesn't exist.
                 */
                idempotent ode::model::IObject get(string klass, long id) throws ServerError;

                /**
                 * Looks up an entity by class and id. If no such objects
                 * exists, return a <code>null</code>.
                 *
                 * @param klass klass the type of the entity. Not null.
                 * @param id the entity's id
                 * @return an initialized entity or null if id doesn't exist.
                 */
                idempotent ode::model::IObject find(string klass, long id) throws ServerError;

                /**
                 * Looks up all entities that belong to this class and match
                 * filter.
                 *
                 * @param klass entity type to be searched. Not null.
                 * @param filter filters the result set. Can be null.
                 * @return a collection if initialized entities or an empty
                 *         List if none exist.
                 */
                idempotent IObjectList findAll(string klass, ode::sys::Filter filter) throws ServerError;

                /**
                 * Searches based on provided example entity. The example
                 * entity should <em>uniquely</em> specify the entity or an
                 * exception will be thrown.
                 *
                 * Note: findByExample does not operate on the <code>id</code>
                 * field. For that, use {@code find}, {@code get},
                 * {@code findByQuery}, or {@code findAllByQuery}.
                 *
                 * @param example Non-null example object.
                 * @return Possibly null IObject result.
                 * @throws ApiUsageException if more than one result is return.
                 */
                idempotent ode::model::IObject findByExample(ode::model::IObject example) throws ServerError;

                /**
                 * Searches based on provided example entity. The returned
                 * entities will be limited by the {@link ode.sys.Filter}
                 * object.
                 *
                 * Note: findAllbyExample does not operate on the
                 * <code>id</code> field.
                 * For that, use {@code find}, {@code get},
                 * {@code findByQuery}, or {@code findAllByQuery}.
                 *
                 * @param example Non-null example object.
                 * @param filter filters the result set. Can be null.
                 * @return Possibly empty List of IObject results.
                 */
                idempotent IObjectList findAllByExample(ode::model::IObject example, ode::sys::Filter filter) throws ServerError;

                /**
                 * Searches a given field matching against a String. Method
                 * does <em>not</em> allow for case sensitive or insensitive
                 * searching since this is essentially a lookup. The existence
                 * of more than one result will result in an exception.
                 *
                 * @param klass type of entity to be searched
                 * @param field the name of the field, either as simple string
                 *              or as public static final from the entity
                 *              class, e.g. {@code ode.model.Project.NAME}
                 * @param value String used for search.
                 * @return found entity or possibly null.
                 * @throws ode.conditions.ApiUsageException
                 *             if more than one result.
                 */
                idempotent ode::model::IObject findByString(string klass, string field, string value) throws ServerError;

                /**
                 * Searches a given field matching against a String. Method
                 * allows for case sensitive or insensitive searching using
                 * the (I)LIKE comparators. Result set will be reduced by the
                 * {@link ode.sys.Filter} instance.
                 *
                 * @param klass type of entity to be searched. Not null.
                 * @param field the name of the field, either as simple string
                 *              or as public static final from the entity
                 *              class, e.g. {@code ode.model.Project.NAME}.
                 *              Not null.
                 * @param value String used for search. Not null.
                 * @param caseSensitive whether to use LIKE or ILIKE
                 * @param filter filters the result set. Can be null.
                 * @return A list (possibly empty) with the results.
                 */
                idempotent IObjectList findAllByString(string klass, string field, string value, bool caseSensitive, ode::sys::Filter filter) throws ServerError;

                /**
                 * Executes the stored query with the given name. If a query
                 * with the name cannot be found, an exception will be thrown.
                 *
                 * The queryName parameter can be an actual query String if the
                 * StringQuerySource is configured on the server and the user
                 * running the query has proper permissions.
                 *
                 * @param query Query to execute
                 * @param params
                 * @return Possibly null IObject result.
                 * @throws ValidationException
                 */
                idempotent ode::model::IObject findByQuery(string query, ode::sys::Parameters params) throws ServerError;

                /**
                 * Executes the stored query with the given name. If a query
                 * with the name cannot be found, an exception will be thrown.
                 *
                 * The queryName parameter can be an actual query String if the
                 * StringQuerySource is configured on the server and the user
                 * running the query has proper permissions.
                 *
                 * Queries can only return lists of
                 * {@link ode.model.IObject} instances. This means
                 * all must be of the form:
                 *
                 * <pre>
                 * select this from SomeModelClass this ...
                 * </pre>
                 *
                 * though the alias <i>this</i> is unimportant. Do not try to
                 * return multiple classes in one call like:
                 *
                 * <pre>
                 * select this, that from SomeClass this, SomeOtherClass that ...
                 * </pre>
                 *
                 * nor to project values out of an object:
                 *
                 * <pre>
                 * select this.name from SomeClass this ...
                 * </pre>
                 *
                 * If a page is desired, add it to the query parameters.
                 *
                 * @param query Query to execute. Not null.
                 * @param params
                 * @return Possibly empty List of IObject results.
                 */
                idempotent IObjectList findAllByQuery(string query, ode::sys::Parameters params) throws ServerError;

                /**
                 * Executes a full text search based on Lucene. Each term in
                 * the query can also be prefixed by the name of the field to
                 * which is should be restricted.
                 *
                 * Examples:
                 * <ul>
                 * <li>owner:root AND annotation:someTag</li>
                 * <li>file:xml AND name:*hoechst*</li>
                 * </ul>
                 *
                 * For more information, see
                 * <a href="http://lucene.apache.org/java/docs/queryparsersyntax.html">Query Parser Syntax</a>
                 *
                 * The return values are first filtered by the security system.
                 *
                 * @param klass A non-null class specification of which type
                 *             should be searched.
                 * @param query A non-null query string. An empty string will
                 *              return no results.
                 * @param params
                 *            Currently the parameters themselves are unused.
                 *            But the {@link ode.sys.Parameters#theFilter}
                 *            can be used to limit the number of results
                 *            returned ({@link ode.sys.Filter#limit}) or the
                 *            user for who the results will be found
                 *            ({@link ode.sys.Filter#ownerId}).
                 * @return A list of loaded {@link ode.model.IObject}
                 *         instances. Never null.
                 **/
                idempotent IObjectList findAllByFullText(string klass, string query, ode::sys::Parameters params) throws ServerError;

                /**
                 * Return a sequence of {@link ode.RType} sequences.
                 *
                 * <p>
                 * Each element of the outer sequence is one row in the return
                 * value.
                 * Each element of the inner sequence is one column specified
                 * in the HQL.
                 * </p>
                 *
                 * <p>
                 * {@link ode.model.IObject} instances are returned wrapped
                 * in an {@link ode.RObject} instance. Primitives are
                 * mapped to the expected {@link ode.RType} subclass. Types
                 * without an {@link ode.RType} mapper if returned will
                 * throw an exception if present in the select except where a
                 * manual conversion is present on the server. This includes:
                 * </p>
                 *
                 * <ul>
                 * <li>
                 *     {@link ode.model.Permissions} instances are
                 *     serialized to an {@link ode.RMap} containing the
                 *     keys: perms, canAnnotate, canEdit, canLink, canDelete,
                 *     canChgrp, canChown
                 * </li>
                 * <li>
                 *     The quantity types like {@link ode.model.Length} are
                 *     serialized to an {@link ode.RMap} containing the
                 *     keys: value, unit, symbol
                 * </li>
                 * </ul>
                 *
                 * <p>
                 * As with SQL, if an aggregation statement is used, a group
                 * by clause must be added.
                 * </p>
                 *
                 * <p>
                 * Examples:
                 * <pre>
                 *   select i.name, i.description from Image i where i.name like '%.dv'
                 *
                 *   select tag.textValue, tagset.textValue from TagAnnotation tag join tag.annotationLinks l join l.child tagset
                 *
                 *   select p.pixelsType.value, count(p.id) from Pixel p group by p.pixelsType.value
                 * </pre>
                 * </p>
                 *
                 */
                idempotent RTypeSeqSeq projection(string query, ode::sys::Parameters params) throws ServerError;

                /**
                 * Refreshes an entire {@link ode.model.IObject} graph,
                 * recursive loading all data for the managed instances in the
                 * graph from the database. If any non-managed entities are
                 * detected (e.g. without ids), an
                 * {@link ode.ApiUsageException} will be thrown.
                 *
                 * @param iObject Non-null managed {@link ode.model.IObject}
                 *                graph which should have all values
                 *                re-assigned from the database
                 * @return a similar {@link ode.model.IObject} graph (with
                 *         possible additions and deletions) which is in-sync
                 *         with the database.
                 * @throws ApiUsageException if any non-managed entities are
                 *         found.
                 */
                idempotent ode::model::IObject refresh(ode::model::IObject iObject) throws ServerError;
            };

    };
};

#endif