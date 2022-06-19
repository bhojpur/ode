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

import java.util.Collection;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.model.IObject;
import ode.services.graphs.GraphPolicy;

/**
 * Adjust graph traversal policy to ignore objects according to their type.
 * @deprecated experimental: may be wholly removed in next major version
 */
@Deprecated
public class IgnoreTypePolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgnoreTypePolicy.class);

    /**
     * Adjust an existing graph traversal policy so that objects of certain types may be ignored.
     * @param graphPolicyToAdjust the graph policy to adjust
     * @param typesToIgnore the types to ignore as defined by {@link ode.cmd.Delete2#typesToIgnore}
     * @return the adjusted graph policy
     */
    public static GraphPolicy getIgnoreTypePolicy(GraphPolicy graphPolicyToAdjust,
            final Collection<Class<? extends IObject>> typesToIgnore) {
        if (CollectionUtils.isEmpty(typesToIgnore)) {
            return graphPolicyToAdjust;
        }

        final Predicate<Class<? extends IObject>> isTypeToIgnore = new Predicate<Class<? extends IObject>>() {
            @Override
            public boolean test(Class<? extends IObject> objectClass) {
                for (final Class<? extends IObject> typeToIgnore : typesToIgnore) {
                    if (typeToIgnore.isAssignableFrom(objectClass)) {
                        return true;
                    }
                }
                return false;
            }
        };

        return new BaseGraphPolicyAdjuster(graphPolicyToAdjust) {
            @Override
            protected boolean isAdjustedBeforeReview(Details object) {
                if (object.action == GraphPolicy.Action.EXCLUDE && isTypeToIgnore.test(object.subject.getClass())) {
                    object.action = GraphPolicy.Action.OUTSIDE;
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("ignoring all objects of its type, so making " + object);
                    }
                    return true;
                }
                return false;
            }
        };
    }
}