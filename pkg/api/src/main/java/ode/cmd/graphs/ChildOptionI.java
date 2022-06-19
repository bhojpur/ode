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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.ImmutableSet;

import ode.model.IObject;
import ode.services.graphs.GraphPathBean;

/**
 * Child options adjust how child objects are treated according to their type and, if annotations, namespace,
 * overriding the default graph traversal policy for orphans.
 */
public class ChildOptionI extends ChildOption {

    private final GraphPathBean graphPathBean;
    private final ImmutableSet<String> defaultExcludeNs;

    private Function<Class<? extends IObject>, Boolean> isIncludeType = null;
    private Predicate<String> isTargetNamespace = null;

    /**
     * Construct a new child option instance.
     * @param graphPathBean the graph path bean
     * @param defaultExcludeNs annotation namespaces to exclude by default
     */
    public ChildOptionI(GraphPathBean graphPathBean, ImmutableSet<String> defaultExcludeNs) {
        this.graphPathBean = graphPathBean;
        this.defaultExcludeNs = defaultExcludeNs;
    }

    /**
     * Construct a new child option instance identical to that given. If the original was initialized, this one is too.
     * @param original a child option instance
     */
    public ChildOptionI(ChildOptionI original) {
        graphPathBean = original.graphPathBean;
        defaultExcludeNs = original.defaultExcludeNs;

        includeType = original.includeType == null ? null : new ArrayList<String>(original.includeType);
        excludeType = original.excludeType == null ? null : new ArrayList<String>(original.excludeType);
        includeNs = original.includeNs == null ? null : new ArrayList<String>(original.includeNs);
        excludeNs = original.excludeNs == null ? null : new ArrayList<String>(original.excludeNs);
        isIncludeType = original.isIncludeType;
        isTargetNamespace = original.isTargetNamespace;
    }

    /**
     * Initialize this child option instance.
     * An option takes effect according to the {@link ChildOption} field values set when this method was last called.
     */
    public void init() {
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

        /* construct the function corresponding to the type-based inclusion requirements */

        final ImmutableSet<Class<? extends IObject>> typeInclusions;
        final ImmutableSet<Class<? extends IObject>> typeExclusions;

        if (CollectionUtils.isEmpty(includeType)) {
            typeInclusions = ImmutableSet.of();
        } else {
            typeInclusions = ImmutableSet.copyOf(includeType.stream().map(getClassFromName).collect(Collectors.toSet()));
        }
        if (CollectionUtils.isEmpty(excludeType)) {
            typeExclusions = ImmutableSet.of();
        } else {
            typeExclusions = ImmutableSet.copyOf(excludeType.stream().map(getClassFromName).collect(Collectors.toSet()));
        }

        if (typeInclusions.isEmpty() && typeExclusions.isEmpty()) {
            throw new IllegalArgumentException("child option must include or exclude some type");
        }

        isIncludeType = new Function<Class<? extends IObject>, Boolean>() {
            @Override
            public Boolean apply(Class<? extends IObject> objectClass) {
                for (final Class<? extends IObject> typeInclusion : typeInclusions) {
                    if (typeInclusion.isAssignableFrom(objectClass)) {
                        return Boolean.TRUE;
                    }
                }
                for (final Class<? extends IObject> typeExclusion : typeExclusions) {
                    if (typeExclusion.isAssignableFrom(objectClass)) {
                        return Boolean.FALSE;
                    }
                }
                return null;
            }
        };

        /* if no annotation namespaces are set, then apply the defaults */

        if (CollectionUtils.isEmpty(includeNs) && CollectionUtils.isEmpty(excludeNs)) {
            excludeNs = new ArrayList<String>(defaultExcludeNs);
        }

        /* construct the predicate corresponding to the namespace restriction */

        if (CollectionUtils.isEmpty(includeNs)) {
            if (CollectionUtils.isEmpty(excludeNs)) {
                /* there is no adjustment to make, not even for any default namespaces */
                isTargetNamespace = x -> true;
            } else {
                final ImmutableSet<String> nsExclusions = ImmutableSet.copyOf(excludeNs);
                isTargetNamespace = new Predicate<String>() {
                    @Override
                    public boolean test(String namespace) {
                        return !nsExclusions.contains(namespace);
                    }
                };
            }
        } else {
            if (CollectionUtils.isEmpty(excludeNs)) {
                final ImmutableSet<String> nsInclusions = ImmutableSet.copyOf(includeNs);
                isTargetNamespace = new Predicate<String>() {
                    @Override
                    public boolean test(String namespace) {
                        return nsInclusions.contains(namespace);
                    }
                };
            } else {
                throw new IllegalArgumentException("child option may not both include and exclude namespace");
            }
        }
    }

    

    /**
     * Test if this child option adjusts graph traversal policy for the given child object class.
     * Requires {@link #init()} to have been called previously.
     * @param objectClass a child object class
     * @return {@code true} if such children should be included in the operation,
     * {@code false} if such children should not be included in the operation, or
     * {@code null} if this child option does not affect the treatment of such children
     */
    public Boolean isIncludeType(Class<? extends IObject> objectClass) {
        return isIncludeType.apply(objectClass);
    }

    /**
     * Test if this child option adjusts graph traversal policy for child objects that are annotations in the given namespace.
     * Requires {@link #init()} to have been called previously.
     * @param namespace an annotation namespace
     * @return if child objects that are annotations in this namespace are affected by this child option
     */
    public boolean isTargetNamespace(String namespace) {
        return isTargetNamespace.test(namespace);
    }

    /**
     * Cast {@code ChildOption[]} to {@code ChildOptionI[]}.
     * @param childOptions an array of {@code ChildOption} which may all be casted to {@code ChildOptionI}, may be {@code null}
     * @return an array of {@code ChildOptionI}, may be {@code null}
     */
    public static List<ChildOptionI> castChildOptions(Collection<ChildOption> childOptions) {
        if (childOptions == null) {
            return null;
        } else {
            final List<ChildOptionI> childOptionsI = new ArrayList<ChildOptionI>(childOptions.size());
            for (final ChildOption childOption : childOptions) {
                childOptionsI.add((ChildOptionI) childOption);
            }

            return childOptionsI;
        }
    }
}