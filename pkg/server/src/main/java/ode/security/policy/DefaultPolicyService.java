package ode.security.policy;

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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ode.model.IObject;
import ode.tools.spring.OnContextRefreshedEventListener;
import ode.util.Utils;

import org.springframework.context.event.ContextRefreshedEvent;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * {@link PolicyService} which is configured with all {@link Policy} instances
 * which are discovered in the Spring context and only focuses on a small
 * subset of {@link IObject} types as specified by {@link #filterObject(IObject)}.
 */
public class DefaultPolicyService
    extends OnContextRefreshedEventListener
    implements PolicyService {

    private final Set<Class<IObject>> types = new HashSet<Class<IObject>>();

    private final ListMultimap<String, Policy> policies = ArrayListMultimap.create();

    /**
     * Loads all {@link Policy} instances from the context,
     * and uses them to initialize this {@link PolicyService}.
     */
    @Override
    public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
        for (Policy policy : event.getApplicationContext()
                .getBeansOfType(Policy.class).values()) {
            policies.put(policy.getName(), policy);
            types.addAll(policy.getTypes());
        }
    }

    //
    // INTERFACE METHODS
    //

    @Override
    public boolean isRestricted(final String name, final IObject obj) {

        if (name == null) {
            return false;
        }

        for (Policy check : policies.get(name))
            if (check.isRestricted(obj)) {
                return true;
        }
        return false;
    }

    @Override
    public void checkRestriction(final String name, final IObject obj) {
        for (Policy check : policies.get(name)) {
            check.checkRestriction(obj);
        }
    }

    @Override
    public Set<String> listAllRestrictions() {
        return policies.keySet();
    }

    @Override
    public Set<String> listActiveRestrictions(IObject obj) {

        if (filterObject(obj)) {
            return Collections.emptySet();
        }

        Set<String> rv = new HashSet<String>();
        for (Map.Entry<String, Policy> entry : policies.entries()) {
            if (entry.getValue().isRestricted(obj)) {
                rv.add(entry.getKey());
            }
        }
        return rv;
    }

    /**
     * Limit the objects to which {@link Policy} instances are applied. This
     * reduces the overhead of creating a {@link HashSet} for every object in
     * a returned graph.
     *
     * @param obj e.g. the argument to {@link #listActiveRestrictions(IObject)}.
     * @return true if the given object should <em>not</em> be restricted.
     */
    protected boolean filterObject(IObject obj) {
        if (obj == null) {
            return true;
        }
        return !types.contains(
                Utils.trueClass(obj.getClass())); // Fix javassist
    }

}