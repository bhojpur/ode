package ode.services.query;

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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import ode.api.IQuery;

/**
 * Convenience class for creating versions of {@link HierarchyNavigator} with different model object representations.
 */
public abstract class HierarchyNavigatorWrap<T, E> extends HierarchyNavigator {
    public HierarchyNavigatorWrap(IQuery iQuery) {
        super(iQuery);
    }

    /**
     * Convert the given object type to the type strings expected by {@link HierarchyNavigator}.
     * @param type an object type
     * @return the corresponding type string
     */
    protected abstract String typeToString(T type);

    /**
     * Convert the given type string to the object type.
     * @param typeName a type string
     * @return the corresponding object type
     */
    protected abstract T stringToType(String typeName);

    /**
     * Convert a model object to the type string and database ID expected by {@link HierarchyNavigator}.
     * @param entity a model object
     * @return the corresponding type string and database ID
     */
    protected abstract Map.Entry<String, Long> entityToStringLong(E entity);

    /**
     * Convert the given type string and database ID to the model object.
     * @param typeName a type string
     * @param id a database ID
     * @return the corresponding model object
     */
    protected abstract E stringLongToEntity(String typeName, long id);

    /**
     * Batch bulk database queries to prime the cache for {@link #doLookup(Object, Object)}.
     * It is not necessary to call this method, but it is advised if many lookups are anticipated.
     * Wraps {@link HierarchyNavigator#prepareLookups(String, String, Collection)}.
     * @param toType the type of the objects to which the query objects may be related, not <code>null</code>
     * @param from the query objects, none <code>null</code>, may be of differing types
     */

    public void prepareLookups(T toType, Collection<E> from) {
        final String toTypeAsString = typeToString(toType);
        final SetMultimap<String, Long> fromIdsByType = HashMultimap.create();
        for (final E entity : from) {
            final Map.Entry<String, Long> fromAsStringLong = entityToStringLong(entity);
            fromIdsByType.put(fromAsStringLong.getKey(), fromAsStringLong.getValue());
        }
        for (final String fromTypeAsString : fromIdsByType.keySet()) {
            final Set<Long> fromIdsAsLongs = fromIdsByType.get(fromTypeAsString);
            prepareLookups(toTypeAsString, fromTypeAsString, fromIdsAsLongs);
        }
    }

    /**
     * Look up which objects of a given type relate to the given query object.
     * Caches results, and one may bulk-cache results in advance using {@link #prepareLookups(Object, Collection)}.
     * Wraps {@link HierarchyNavigator#doLookup(String, String, Long)}.
     * @param toType the type of the objects to which the query object may be related, not <code>null</code>
     * @param from the query object, not <code>null</code>
     * @return the related objects, never <code>null</code>
     */
    public ImmutableSet<E> doLookup(T toType, E from) {
        final String toTypeAsString = typeToString(toType);
        final Map.Entry<String, Long> fromAsStringLong = entityToStringLong(from);
        final ImmutableSet.Builder<E> to = ImmutableSet.builder();
        for (final Long toIdAsLong : doLookup(toTypeAsString, fromAsStringLong.getKey(), fromAsStringLong.getValue())) {
            to.add(stringLongToEntity(toTypeAsString, toIdAsLong));
        }
        return to.build();
    }
}
