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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.collect.ImmutableSet;

/**
 * Simple cache of lookups of which objects of a given type relate to query objects.
 */
class ModelObjectCache {
    private static class Lookup {
        final String fromType;
        final long fromId;
        final String toType;

        /**
         * A lookup of which objects of a given type relate to a query object.
         * It is assumed that there are not enormously many different object types.
         * @param fromType the query object's type, not <code>null</code>
         * @param fromId the query object's database ID
         * @param toType the type of the objects to which the query object may be related, not <code>null</code>
         */
        private Lookup(String fromType, long fromId, String toType) {
            if (fromType == null || toType == null) {
                throw new IllegalArgumentException(new NullPointerException());
            }
            this.fromType = fromType.intern();
            this.fromId = fromId;
            this.toType = toType.intern();
        }

        /**
         * {@inheritDoc}
         * Different instances are equal if their fields are equal.
         */
        @Override
        public boolean equals(Object object) {
            if (object instanceof Lookup) {
                final Lookup lookup = (Lookup) object;
                /* note that the constructor intern()'d the type values */
                return this.fromType == lookup.fromType &&
                       this.fromId   == lookup.fromId &&
                       this.toType   == lookup.toType;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                .append(this.fromType)
                .append(this.fromId)
                .append(this.toType)
                .toHashCode();
        }
    }

    private final Map<Lookup, ImmutableSet<Long>> lookupCache = new HashMap<Lookup, ImmutableSet<Long>>();

    /**
     * Retrieve related objects from the cache.
     * @param fromType the query object's type, not <code>null</code>
     * @param fromId the query object's database ID
     * @param toType the type of the objects to which the query object may be related, not <code>null</code>
     * @return the related objects, or <code>null</code> for a cache miss
     */
    ImmutableSet<Long> getFromCache(String fromType, Long fromId, String toType) {
        return lookupCache.get(new Lookup(fromType, fromId, toType));
    }

    /**
     * Insert related objects into the cache.
     * Wholly replaces any previous value.
     * @param fromType the query object's type, not <code>null</code>
     * @param fromId the query object's database ID
     * @param toType the type of the objects to which the query object may be related, not <code>null</code>
     * @param toIds the related objects
     */
    void putIntoCache(String fromType, Long fromId, String toType, ImmutableSet<Long> toIds) {
        lookupCache.put(new Lookup(fromType, fromId, toType), toIds);
    }
}
