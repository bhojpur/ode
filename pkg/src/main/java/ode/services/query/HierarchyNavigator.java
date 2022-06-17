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

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import ode.api.IQuery;
import ode.parameters.Parameters;

/**
 * Query the database for relationships between model objects.
 */
public class HierarchyNavigator {
    /* This class and {@link HierarchyNavigatorWrap} are designed to make it easy to adjust the Java types
     * via which the model object hierarchy is navigated, and to make the HQL queries efficient
     * (batching, caching), at the small expense of constructing instances of simple Java objects.
     * The methods are not public to avoid polluting users of subclasses of {@link HierarchyNavigatorWrap}.
     */

    /** HQL queries to map from ID of first target type to that of the second */
    private static final ImmutableMap<Map.Entry<String, String>, String> hqlFromTo;

    static {
        /* note that there is not yet any treatment of PlateAcquisition or WellSample */
        final Builder<Map.Entry<String, String>, String> builder = ImmutableMap.builder();
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Folder", "Folder"),
                /* cannot distinguish ascent from descent, guess the latter */
                "SELECT parentFolder.id, id FROM Folder WHERE parentFolder.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Folder", "Image"),
                "SELECT parent.id, child.id FROM FolderImageLink WHERE parent.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Project", "Dataset"),
                "SELECT parent.id, child.id FROM ProjectDatasetLink WHERE parent.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Dataset", "Image"),
                "SELECT parent.id, child.id FROM DatasetImageLink WHERE parent.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Screen", "Plate"),
                "SELECT parent.id, child.id FROM ScreenPlateLink WHERE parent.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Plate", "Well"),
                "SELECT plate.id, id FROM Well WHERE plate.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Well", "Image"),
                "SELECT well.id, image.id FROM WellSample WHERE well.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Fileset", "Image"),
                "SELECT fileset.id, id FROM Image WHERE fileset.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Image", "Fileset"),
                "SELECT id, fileset.id FROM Image WHERE fileset.id IS NOT NULL AND id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Image", "Well"),
                "SELECT image.id, well.id FROM WellSample WHERE image.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Well", "Plate"),
                "SELECT id, plate.id FROM Well WHERE id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Plate", "Screen"),
                "SELECT child.id, parent.id FROM ScreenPlateLink WHERE child.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Image", "Dataset"),
                "SELECT child.id, parent.id FROM DatasetImageLink WHERE child.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Dataset", "Project"),
                "SELECT child.id, parent.id FROM ProjectDatasetLink WHERE child.id IN (:" + Parameters.IDS + ")");
        builder.put(new AbstractMap.SimpleImmutableEntry<>("Image", "Folder"),
                "SELECT child.id, parent.id FROM FolderImageLink WHERE child.id IN (:" + Parameters.IDS + ")");
        hqlFromTo = builder.build();
    }

    /** available query service */
    protected final IQuery iQuery;

    /** cache of query results */
    private final ModelObjectCache cache = new ModelObjectCache();

    /**
     * Construct a new hierarchy navigator.
     * @param iQuery the query service
     */
    protected HierarchyNavigator(IQuery iQuery) {
        this.iQuery = iQuery;
    }

    /**
     * Perform the database query to discover the IDs of the related objects.
     * @param toType the type of the objects to which the query object may be related, not <code>null</code>
     * @param fromType the query object's type, not <code>null</code>
     * @param fromIds the query objects' database IDs, none <code>null</code>
     * @return pairs of database IDs: of the query object, and an object to which it relates
     */
    private List<Object[]> doQuery(String toType, String fromType, Collection<Long> fromIds) {
        final String queryString = hqlFromTo.get(new AbstractMap.SimpleImmutableEntry<>(fromType, toType));
        if (queryString == null) {
            throw new IllegalArgumentException("not implemented for " + fromType + " to " + toType);
        }
        return this.iQuery.projection(queryString, new Parameters().addIds(fromIds));
    }

    /**
     * Batch bulk database queries to prime the cache for {@link #doLookup(String, String, Long)}.
     * It is not necessary to call this method, but it is advised if many lookups are anticipated.
     * @param toType the type of the objects to which the query objects may be related, not <code>null</code>
     * @param fromType the query object's type, not <code>null</code>
     * @param fromIds the query objects' database IDs, none <code>null</code>
     */
    protected void prepareLookups(String toType, String fromType, Collection<Long> fromIds) {
        /* note which query object IDs have not already had results cached */
        final Set<Long> fromIdsToQuery = new HashSet<Long>(fromIds);
        for (final long fromId : fromIds) {
            if (cache.getFromCache(fromType, fromId, toType) != null) {
                fromIdsToQuery.remove(fromId);
            }
        }
        if (fromIdsToQuery.isEmpty()) {
            /* ... all of them are already cached */
            return;
        }
        /* collate the results from multiple batches */
        final SetMultimap<Long, Long> fromIdsToIds = HashMultimap.create();
        for (final List<Long> fromIdsToQueryBatch : Iterables.partition(fromIdsToQuery, 256)) {
            for (final Object[] queryResult : doQuery(toType, fromType, fromIdsToQueryBatch)) {
                fromIdsToIds.put((Long) queryResult[0], (Long) queryResult[1]);
            }
        }
        /* cache the results by query object */
        for (final Entry<Long, Collection<Long>> fromIdToIds : fromIdsToIds.asMap().entrySet()) {
            cache.putIntoCache(fromType, fromIdToIds.getKey(), toType, ImmutableSet.copyOf(fromIdToIds.getValue()));
        }
        /* note empty results so that the database is not again queried */
        for (final Long fromId : Sets.difference(fromIdsToQuery, fromIdsToIds.keySet())) {
            cache.putIntoCache(fromType, fromId, toType, ImmutableSet.<Long>of());
        }
    }

    /**
     * Look up which objects of a given type relate to the given query object.
     * Caches results, and one may bulk-cache results in advance using {@link #prepareLookups(String, String, Collection)}.
     * @param toType the type of the objects to which the query object may be related, not <code>null</code>
     * @param fromType the query object's type, not <code>null</code>
     * @param fromId the query object's database ID, not <code>null</code>
     * @return the related objects' database IDs, never <code>null</code>
     */
    protected ImmutableSet<Long> doLookup(String toType, String fromType, Long fromId) {
        final ImmutableSet<Long> result = cache.getFromCache(fromType, fromId, toType);
        if (result == null) {
            /* cache miss, so query the single object */
            final ImmutableSet.Builder<Long> toIdsBuilder = ImmutableSet.builder();
            for (final Object[] queryResult : doQuery(toType, fromType, Collections.singleton(fromId))) {
                toIdsBuilder.add((Long) queryResult[1]);
            }
            final ImmutableSet<Long> toIds = toIdsBuilder.build();
            cache.putIntoCache(fromType, fromId, toType, toIds);
            return toIds;
        } else {
            /* cache hit */
            return result;
        }
    }
}
