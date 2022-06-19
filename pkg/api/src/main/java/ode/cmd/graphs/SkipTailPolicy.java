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

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.model.IObject;
import ode.services.graphs.GraphException;
import ode.services.graphs.GraphPolicy;
import ode.services.graphs.GraphPolicyRulePredicate;

/**
 * Adjust graph traversal policy to avoid processing or acting on certain model object classes.
 */
public class SkipTailPolicy {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkipTailPolicy.class);

    /**
     * Adjust an existing graph traversal policy so that processing stops at certain model object classes.
     * @param graphPolicy the graph policy to adjust
     * @param isSkipClass if a given class should be not be reviewed or acted on
     * @return the adjusted graph policy
     */
    public static GraphPolicy getSkipTailPolicy(final GraphPolicy graphPolicy,
            final Predicate<Class<? extends IObject>> isSkipClass) {

        /* construct the function corresponding to the model graph descent truncation */

        return new GraphPolicy() {
            @Override
            public void registerPredicate(GraphPolicyRulePredicate predicate) {
                graphPolicy.registerPredicate(predicate);
            }

            @Override
            public GraphPolicy getCleanInstance() {
                throw new IllegalStateException("not expecting to provide a clean instance");
            }

            @Override
            public void setCondition(String name) {
                graphPolicy.setCondition(name);
            }

            @Override
            public boolean isCondition(String name) {
                return graphPolicy.isCondition(name);
            }

            @Override
            public void noteDetails(Session session, IObject object, String realClass, long id) {
                graphPolicy.noteDetails(session, object, realClass, id);
            }

            @Override
            public final Set<Details> review(Map<String, Set<Details>> linkedFrom, Details rootObject,
                    Map<String, Set<Details>> linkedTo, Set<String> notNullable, boolean isErrorRules) throws GraphException {
                if (isSkipClass.test(rootObject.subject.getClass())) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("halting review at " + rootObject);
                    }
                    /* request parameters specify to review no further */
                    return Collections.emptySet();
                } else {
                    /* do the review */
                    final Set<Details> changes = graphPolicy.review(linkedFrom, rootObject, linkedTo, notNullable, isErrorRules);
                    final Iterator<Details> changesIterator = changes.iterator();
                    while (changesIterator.hasNext()) {
                        final Details change = changesIterator.next();
                        if (change.action == Action.INCLUDE && isSkipClass.test(change.subject.getClass())) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("forestalling policy-based change " + change);
                            }
                            /* do not act on skipped classes */
                            changesIterator.remove();
                        }
                    }
                    return changes;
                }
            }
        };
    }
}