package ode.cmd.graphs;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

import ode.model.core.OriginalFile;
import ode.model.core.Pixels;
import ode.model.display.Thumbnail;
import ode.model.internal.Details;
import ode.services.delete.Deletion;
import ode.services.graphs.GraphException;
import ode.services.graphs.GraphTraversal;

/**
 * Useful methods for {@link ode.services.graphs.GraphTraversal.Processor} instances to share.
 */
public abstract class BaseGraphTraversalProcessor implements GraphTraversal.Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseGraphTraversalProcessor.class);

    /* compare with ode.services.delete.files.FileDeleter.Type */
    private static final Collection<String> DELETION_CLASS_NAMES = ImmutableSet.of(
            OriginalFile.class.getName(), Pixels.class.getName(), Thumbnail.class.getName());

    protected final Session session;

    private final List<Map.Entry<String, Collection<Long>>> deletionLog = new ArrayList<>();

    public BaseGraphTraversalProcessor(Session session) {
        this.session = session;
    }

    @Override
    public void nullProperties(String className, String propertyName,
            Collection<Long> ids) {
        final String update = "UPDATE " + className + " SET " + propertyName + " = NULL WHERE id IN (:ids)";
        session.createQuery(update).setParameterList("ids", ids).executeUpdate();
    }

    @Override
    public void deleteInstances(String className, Collection<Long> ids) throws GraphException {
        final String update = "DELETE FROM " + className + " WHERE id IN (:ids)";
        final int count = session.createQuery(update).setParameterList("ids", ids).executeUpdate();
        if (count != ids.size()) {
            LOGGER.warn("not all the objects of type " + className + " could be deleted");
        }
        if (DELETION_CLASS_NAMES.contains(className)) {
            deletionLog.add(new AbstractMap.SimpleImmutableEntry<>(className.substring(className.lastIndexOf('.') + 1), ids));
        }
    }

    /**
     * Delete data from the filesystem in the order in which the related batches were passed to
     * {@link #deleteInstances(String, Collection)}.
     * @param deletionInstance a deletion instance for deleting files
     */
    public void deleteFiles(Deletion deletionInstance) {
        for (final Map.Entry<String, Collection<Long>> deletionBatch : deletionLog) {
            deletionInstance.deleteFiles(
                    ImmutableSetMultimap.<String, Long>builder()
                    .putAll(deletionBatch.getKey(), deletionBatch.getValue())
                    .build());
        }
        deletionLog.clear();
    }

    @Override
    public void assertMayProcess(String className, long id, Details details) throws GraphException {
        /* no check */
    }
}