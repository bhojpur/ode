package ode.services;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.api.IQuery;
import ode.model.core.Pixels;
import ode.parameters.Parameters;
import ode.services.messages.ContextMessage;
import ode.system.OdeContext;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * Perform an operation on {@link Pixels} in contexts corresponding to the {@link Pixels}' group.
 */
abstract class PerGroupActor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerGroupActor.class);

    private final OdeContext applicationContext;
    private final IQuery queryService;
    private final Long currentGroupId;

    /**
     * Create a new per-group actor.
     * @param applicationContext the Bhojpur ODE application context
     * @param queryService the query service for retrieving the pixels' groups
     * @param currentGroupId the current group ID, may be {@code null}
     */
    PerGroupActor(OdeContext applicationContext, IQuery queryService, Long currentGroupId) {
        this.applicationContext = applicationContext;
        this.queryService = queryService;
        this.currentGroupId = currentGroupId;
    }

    /**
     * Act on the {@link Pixels} by setting the group context and calling {@link #actOnOneGroup(Set)} as necessary.
     * @param pixelsIds the IDs of the {@link Pixels} on which to act
     */
    void actOnByGroup(Collection<Long> pixelsIds) {
        if (pixelsIds.isEmpty()) {
            return;
        }
        final SetMultimap<Long, Long> pixelsByGroup = HashMultimap.create();
        for (final Object[] resultRow : queryService.projection("SELECT id, details.group.id FROM Pixels WHERE id IN (:ids)",
                new Parameters().addIds(pixelsIds))) {
            final Long pixelsId = (Long) resultRow[0];
            final Long groupId  = (Long) resultRow[1];
            pixelsByGroup.put(groupId, pixelsId);
        }
        for (final Map.Entry<Long, Collection<Long>> pixelsOneGroup : pixelsByGroup.asMap().entrySet()) {
            final Long groupId = pixelsOneGroup.getKey();
            if (groupId.equals(currentGroupId)) {
                actOnOneGroup((Set<Long>) pixelsOneGroup.getValue());
            } else {
                final Map<String, String> groupContext = new HashMap<>();
                groupContext.put("ode.group", Long.toString(groupId));
                try {
                    try {
                        applicationContext.publishMessage(new ContextMessage.Push(this, groupContext));
                    } catch (Throwable t) {
                        LOGGER.error("could not publish context change push", t);
                    }
                    actOnOneGroup((Set<Long>) pixelsOneGroup.getValue());
                } finally {
                    try {
                        applicationContext.publishMessage(new ContextMessage.Pop(this, groupContext));
                    } catch (Throwable t) {
                        LOGGER.error("could not publish context change pop", t);
                    }
                }
            }
        }
    }

    /**
     * Act on the {@link Pixels}. Called within a context corresponding to the {@link Pixels}' group.
     * @param pixelsIds the IDs of the {@link Pixels} on which to act
     */
    abstract protected void actOnOneGroup(Set<Long> pixelsIds);
}