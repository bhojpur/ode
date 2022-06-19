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
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import ode.model.IObject;
import ode.services.graphs.GraphException;
import ode.services.graphs.GraphPathBean;
import ode.services.graphs.GraphPolicy;
import ode.services.graphs.GraphPolicyRulePredicate;
import ode.cmd.SkipHead;

/**
 * Adjust graph traversal policy to prevent descent into inclusions beyond certain types.
 */
public class SkipHeadPolicy {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkipHeadPolicy.class);

    /**
     * Adjust an existing graph traversal policy so that orphaned model objects will always or never be included,
     * according to their type.
     * @param graphPolicy the graph policy to adjust
     * @param graphPathBean the graph path bean, for converting class names to the actual classes
     * @param startFrom the model object types to from which to start inclusion, may not be empty or {@code null}
     * @param startAction the action associated with nodes qualifying as start objects
     * @param permissionsOverrides where to note for which {@code startFrom} objects permissions are not to be checked
     * @return the adjusted graph policy
     * @throws GraphException if no start classes are named
     */
    public static GraphPolicy getSkipHeadPolicySkip(final GraphPolicy graphPolicy, final GraphPathBean graphPathBean,
            Collection<String> startFrom, final GraphPolicy.Action startAction,
            final SetMultimap<String, Long> permissionsOverrides) throws GraphException {
        if (CollectionUtils.isEmpty(startFrom)) {
            throw new GraphException(SkipHead.class.getSimpleName() + " requires the start classes to be named");
        }

        /* convert the class names to actual classes */

        final Function<String, Class<? extends IObject>> getClassFromName = new Function<String, Class<? extends IObject>>() {
            @Override
            public Class<? extends IObject> apply(String className) {
                final int lastDot = className.lastIndexOf('.');
                if (lastDot > 0) {
                    className = className.substring(lastDot + 1);
                }
                return graphPathBean.getClassForSimpleName(className);
            }
        };

        final ImmutableSet<Class <? extends IObject>> startFromClasses =
                ImmutableSet.copyOf(startFrom.stream().map(getClassFromName).collect(Collectors.toSet()));

        final Predicate<IObject> isStartFrom = new Predicate<IObject>() {
            private final Predicate<Class<? extends IObject>> tester = GraphUtil.getPredicateFromClasses(startFromClasses);

            @Override
            public boolean test(IObject subject) {
                return tester.test(subject.getClass());
            }
        };

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
                if (rootObject.action == startAction && isStartFrom.test(rootObject.subject)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("deferring review of " + rootObject);
                    }
                    /* note which permissions overrides to start from */
                    final String className = rootObject.subject.getClass().getName();
                    final Long id = rootObject.subject.getId();
                    if (rootObject.isCheckPermissions) {
                        permissionsOverrides.remove(className, id);
                    } else {
                        permissionsOverrides.put(className, id);
                    }
                    /* skip the review, start from this object in a later request */
                    return Collections.emptySet();
                } else {
                    /* do the review */
                    return graphPolicy.review(linkedFrom, rootObject, linkedTo, notNullable, isErrorRules);
                }
            }
        };
    }

    /**
     * Adjust an existing graph traversal policy so that for specific model objects permissions are not checked.
     * @param graphPolicy the graph policy to adjust
     * @param permissionsOverrides for which model objects permissions are not to be checked
     * @return the adjusted graph policy
     */
    public static GraphPolicy getSkipHeadPolicyPerform(final GraphPolicy graphPolicy,
            final SetMultimap<String, Long> permissionsOverrides) {
        return new BaseGraphPolicyAdjuster(graphPolicy) {
            @Override
            protected boolean isAdjustedBeforeReview(Details object) {
                if (object.isCheckPermissions &&
                        permissionsOverrides.containsEntry(object.subject.getClass().getName(), object.subject.getId())) {
                    object.isCheckPermissions = false;
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("preserving previous setting, making " + object);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        };
    }
}