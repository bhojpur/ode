package ode.services.graphs;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.hibernate.Session;

import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

/**
 * Bhojpur ODE model objects sometimes must be processed in a specific order.
 * This class groups the utility methods for performing such ordering.
 * @deprecated no longer used internally, replaced by {@code GraphTraversal.orderFileDeletion()}
 */
@Deprecated
public class ModelObjectSequencer {
    /**
     * Sort a list of original file IDs such that files precede containing directories.
     * @param session the Hibernate session
     * @param unorderedIds the IDs of original files
     * @return a batching of the given IDs such that a batch containing a file precedes a batch containing a containing directory
     */
    public static Collection<Collection<Long>> sortOriginalFileIds(Session session, Collection<Long> unorderedIds) {
        if (unorderedIds.size() < 2) {
            /* no need to rearrange anything, as there are not multiple original files */
            return Collections.<Collection<Long>>singleton(new ArrayList<Long>(unorderedIds));
        }
        final String hql = "SELECT id, length(path) FROM OriginalFile WHERE id IN (:ids)";
        final SortedMap<Integer, List<Long>> filesByPathLength = new TreeMap<Integer, List<Long>>(Ordering.natural().reverse());
        for (final Collection<Long> idBatch : Iterables.partition(unorderedIds, 256)) {
            for (final Object[] result : (List<Object[]>) session.createQuery(hql).setParameterList("ids", idBatch).list()) {
                final Long id = (Long) result[0];
                final Integer length = (Integer) result[1];
                List<Long> idList = filesByPathLength.get(length);
                if (idList == null) {
                    idList = new ArrayList<Long>();
                    filesByPathLength.put(length, idList);
                }
                idList.add(id);
            }
        }
        final Collection<Collection<Long>> orderedIds = new ArrayList<Collection<Long>>();
        for (final List<Long> ids : filesByPathLength.values()) {
            orderedIds.add(ids);
        }
        return orderedIds;
    }
}
