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

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

import ode.model.IObject;
import ode.model.internal.Permissions;
import ode.security.ACLVoter;
import ode.security.basic.LightAdminPrivileges;
import ode.services.delete.Deletion;
import ode.services.graphs.GraphException;
import ode.services.graphs.GraphPathBean;
import ode.services.graphs.GraphPolicy;
import ode.services.graphs.GraphPolicyRule;
import ode.system.Roles;
import ode.cmd.GraphModify2;
import ode.cmd.GraphQuery;
import ode.cmd.Request;
import ode.cmd.SkipHead;

/**
 * Create request objects that are executed using the {@link ode.services.graphs.GraphPathBean}.
 */
public class GraphRequestFactory implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphRequestFactory.class);

    private final ACLVoter aclVoter;
    private final Roles securityRoles;
    private final GraphPathBean graphPathBean;
    private final LightAdminPrivileges adminPrivileges;
    private final Deletion deletionInstance;
    private final ImmutableSetMultimap<Class<? extends Request>, Class<? extends IObject>> allTargets;
    private final ImmutableMap<Class<? extends Request>, GraphPolicy> graphPolicies;
    private final ImmutableSetMultimap<String, String> unnullable;
    private final ImmutableSet<String> defaultExcludeNs;

    private ApplicationContext applicationContext = null;

    /**
     * Construct a new graph request factory.
     * @param aclVoter ACL voter for permissions checking
     * @param securityRoles the security roles
     * @param graphPathBean the graph path bean
     * @param adminPrivileges the light administrator privileges helper
     * @param deletionInstance a deletion instance for deleting files
     * @param allTargets legal target object classes for all request classes that use the graph path bean
     * @param allRules rules for all request classes that use the graph path bean
     * @param unnullable properties that, while nullable, may not be nulled by a graph traversal operation
     * @param defaultExcludeNs the default value for an unset excludeNs field
     * @throws GraphException if the graph path rules could not be parsed
     */
    public GraphRequestFactory(ACLVoter aclVoter, Roles securityRoles, GraphPathBean graphPathBean,
            LightAdminPrivileges adminPrivileges, Deletion deletionInstance, Map<Class<? extends Request>, List<String>> allTargets,
            Map<Class<? extends Request>, List<GraphPolicyRule>> allRules, List<String> unnullable, Set<String> defaultExcludeNs)
                    throws GraphException {
        this.aclVoter = aclVoter;
        this.securityRoles = securityRoles;
        this.graphPathBean = graphPathBean;
        this.adminPrivileges = adminPrivileges;
        this.deletionInstance = deletionInstance;

        final ImmutableSetMultimap.Builder<Class<? extends Request>, Class<? extends IObject>> allTargetsBuilder =
                ImmutableSetMultimap.builder();
        for (final Map.Entry<Class<? extends Request>, List<String>> rules : allTargets.entrySet()) {
            final Class<? extends Request> requestClass = rules.getKey();
            for (final String targetClassName : rules.getValue()) {
                allTargetsBuilder.put(requestClass, graphPathBean.getClassForSimpleName(targetClassName));
            }
        }
        this.allTargets = allTargetsBuilder.build();

        aclVoter.setPermittedClasses(ImmutableMap.of(
                Permissions.CHGRPRESTRICTION,
                (Set<Class<? extends IObject>>) this.allTargets.get(Chgrp2I.class),
                Permissions.CHOWNRESTRICTION,
                (Set<Class<? extends IObject>>) this.allTargets.get(Chown2I.class)));

        final ImmutableMap.Builder<Class<? extends Request>, GraphPolicy> graphPoliciesBuilder = ImmutableMap.builder();
        for (final Map.Entry<Class<? extends Request>, List<GraphPolicyRule>> rules : allRules.entrySet()) {
            graphPoliciesBuilder.put(rules.getKey(), GraphPolicyRule.parseRules(graphPathBean, rules.getValue()));
        }
        this.graphPolicies = graphPoliciesBuilder.build();

        final ImmutableSetMultimap.Builder<String, String> unnullableBuilder = ImmutableSetMultimap.builder();
        for (final String classProperty : unnullable) {
            final int period = classProperty.indexOf('.');
            final String classNameSimple = classProperty.substring(0, period);
            final String property = classProperty.substring(period + 1);
            final String classNameFull = graphPathBean.getClassForSimpleName(classNameSimple).getName();
            unnullableBuilder.put(classNameFull, property);
        }
        this.unnullable = unnullableBuilder.build();

        this.defaultExcludeNs = ImmutableSet.copyOf(defaultExcludeNs);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * @return the graph path bean used by this instance
     */
    public GraphPathBean getGraphPathBean() {
        return graphPathBean;
    }

    /**
     * Get the legal target object classes for the given request.
     * @param requestClass a request class
     * @return the legal target object classes for that type of request
     */
    public <R extends GraphQuery> Set<Class<? extends IObject>> getLegalTargets(Class<R> requestClass) {
        final Set<Class<? extends IObject>> targetClasses = allTargets.get(requestClass);
        if (targetClasses.isEmpty()) {
            throw new IllegalArgumentException("no legal target classes defined for request class " + requestClass);
        }
        return targetClasses;
    }

    /**
     * Construct a request.
     * @param requestClass a request class
     * @return a new instance of that class
     */
    public <R extends GraphQuery> R getRequest(Class<R> requestClass) {
        final R request;
        try {
            if (SkipHead.class.isAssignableFrom(requestClass)) {
                final Constructor<R> constructor = requestClass.getConstructor(GraphPathBean.class, GraphRequestFactory.class);
                request = constructor.newInstance(graphPathBean, this);
            } else {
                final Set<Class<? extends IObject>> targetClasses = getLegalTargets(requestClass);
                GraphPolicy graphPolicy = graphPolicies.get(requestClass);
                if (graphPolicy == null) {
                    throw new IllegalArgumentException("no graph traversal policy rules defined for request class " + requestClass);
                } else {
                    graphPolicy = graphPolicy.getCleanInstance();
                }
                if (GraphModify2.class.isAssignableFrom(requestClass)) {
                    final Constructor<R> constructor = requestClass.getConstructor(ACLVoter.class, Roles.class,
                            GraphPathBean.class, LightAdminPrivileges.class, Deletion.class, Set.class, GraphPolicy.class,
                            SetMultimap.class, ApplicationContext.class);
                    request = constructor.newInstance(aclVoter, securityRoles, graphPathBean, adminPrivileges, deletionInstance,
                            targetClasses, graphPolicy, unnullable, applicationContext);
                } else {
                    final Constructor<R> constructor = requestClass.getConstructor(ACLVoter.class, Roles.class,
                            GraphPathBean.class, LightAdminPrivileges.class, Set.class, GraphPolicy.class);
                    request = constructor.newInstance(aclVoter, securityRoles, graphPathBean, adminPrivileges,
                            targetClasses, graphPolicy);
                }
            }
        } catch (IllegalArgumentException | ReflectiveOperationException | SecurityException e) {
            throw new IllegalArgumentException("cannot instantiate " + requestClass, e);
        }
        return request;
    }

    /**
     * @return an uninitialized child option instance
     */
    public ChildOption createChildOption() {
        return new ChildOptionI(graphPathBean, defaultExcludeNs);
    }
}