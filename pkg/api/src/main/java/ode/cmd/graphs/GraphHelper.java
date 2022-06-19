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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;

import ode.conditions.InternalException;
import ode.model.IObject;
import ode.model.enums.AdminPrivilege;
import ode.security.ACLVoter;
import ode.services.graphs.GraphPathBean;
import ode.services.graphs.GraphPolicy;
import ode.services.graphs.GraphTraversal;
import ode.services.messages.EventLogMessage;
import ode.cmd.ERR;
import ode.cmd.Helper;

/**
 * Factors common code out of {@link ode.cmd.GraphQuery} implementations for reuse.
 */
public class GraphHelper {

    private final Helper helper;
    private final GraphPathBean graphPathBean;

    /**
     * Construct a helper for a graph request instance.
     * @param helper the general request helper for the graph request instance
     * @param graphPathBean the graph path bean
     */
    public GraphHelper(Helper helper, GraphPathBean graphPathBean) {
        this.helper = helper;
        this.graphPathBean = graphPathBean;
    }

    /**
     * Check if the current user is an administrator.
     * @param requiredPrivilege the privilege that the administrator must have if they are a light administrator
     * @return if the current user is an administrator
     */
    public boolean checkIsAdministrator(AdminPrivilege requiredPrivilege) {
        return helper.getEventContext().getCurrentAdminPrivileges().contains(requiredPrivilege);
    }

    /**
     * Given class names provided by the user, find the corresponding set of actual classes.
     * @param classNames names of model object classes
     * @return the named classes
     */
    public Set<Class<? extends IObject>> getClassesFromNames(Collection<String> classNames) {
        if (CollectionUtils.isEmpty(classNames)) {
            return Collections.emptySet();
        }
        final Set<Class<? extends IObject>> classes = new HashSet<Class<? extends IObject>>();
        for (String className : classNames) {
            final int lastDot = className.lastIndexOf('.');
            if (lastDot > 0) {
                className = className.substring(lastDot + 1);
            }
            final Class<? extends IObject> actualClass = graphPathBean.getClassForSimpleName(className);
            if (actualClass == null) {
                throw new IllegalArgumentException("unknown model object class named: " + className);
            }
            classes.add(actualClass);
        }
        return classes;
    }

    /**
     * Construct a graph traversal manager for a {@link ode.cmd.GraphQuery} request.
     * @param childOptions the child options set on the request
     * @param requiredPermissions the abilities that the user must have to operate upon an object for it to be included
     * @param graphPolicy the graph policy for the request
     * @param graphPolicyAdjusters the adjusters to be applied to the graph policy
     * @param aclVoter ACL voter for permissions checking
     * @param graphPathBean the graph path bean
     * @param unnullable properties that, while nullable, may not be nulled by a graph traversal operation
     * @param processor how to operate on the resulting target object graph
     * @param dryRun if the request should skip the actual model object updates
     * @return the new graph traversal manager
     */
    public GraphTraversal prepareGraphTraversal(List<ChildOption> childOptions, Set<GraphPolicy.Ability> requiredPermissions,
            GraphPolicy graphPolicy, Iterable<Function<GraphPolicy, GraphPolicy>> graphPolicyAdjusters,
            ACLVoter aclVoter, GraphPathBean graphPathBean, SetMultimap<String, String> unnullable,
            GraphTraversal.Processor processor, boolean dryRun) {

        if (childOptions != null) {
            final List<ChildOptionI> childOptionsI = ChildOptionI.castChildOptions(childOptions);
            for (final ChildOptionI childOption : childOptionsI) {
                childOption.init();
            }
            graphPolicy = ChildOptionsPolicy.getChildOptionsPolicy(graphPolicy, childOptionsI, requiredPermissions);
        }

        for (final Function<GraphPolicy, GraphPolicy> adjuster : graphPolicyAdjusters) {
            graphPolicy = adjuster.apply(graphPolicy);
        }

        if (dryRun) {
            processor = GraphUtil.disableProcessor(processor);
        }

        return new GraphTraversal(helper.getSession(), helper.getEventContext(), aclVoter, graphPathBean, unnullable, graphPolicy,
                processor);
    }

    /**
     * Converts the Ice {@code StringSet} to a set.
     * @param legalClasses legal target object classes
     * @param targetClasses the actual target object classes
     * @return a set of the legal model objects to process
     * @throws ode.conditions.InternalException if any of the target classes are illegal
     */
    public Set<String> getTargetSet(Set<Class<? extends IObject>> legalClasses, Collection<String> targetClasses) {
        final Set<String> targetSet = new HashSet<String>();
        for (String targetClassName : targetClasses) {
            /* determine actual class from given target class name */
            final int lastDot = targetClassName.lastIndexOf('.');
            if (lastDot > 0) {
                targetClassName = targetClassName.substring(lastDot + 1);
            }
            final Class<? extends IObject> targetObjectClass = graphPathBean.getClassForSimpleName(targetClassName);
            /* check that it is legal to target the given class */
            final Iterator<Class<? extends IObject>> legalClassesIterator = legalClasses.iterator();
            do {
                if (!legalClassesIterator.hasNext()) {
                    final Exception e = new IllegalArgumentException("cannot target " + targetClassName);
                    throw helper.cancel(new ERR(), e, "bad-target");
                }
            } while (!legalClassesIterator.next().isAssignableFrom(targetObjectClass));
            /* note IDs to target for the class */
            targetSet.add(targetObjectClass.getName());
        }
        return targetSet;
    }

    /**
     * Converts the Ice {@code StringLongListMap} to a multimap.
     * @param legalClasses legal target object classes
     * @param targetObjects the model objects to process
     * @return a multimap of the legal model objects to process
     * @throws ode.conditions.InternalException if any of the target object classes are illegal
     */
    public SetMultimap<String, Long> getTargetMultimap(Set<Class<? extends IObject>> legalClasses,
            Map<String, java.util.List<Long>> targetObjects) {
        /* if targetObjects were an IObjectList then this would need IceMapper.reverse */
        final SetMultimap<String, Long> targetMultimap = HashMultimap.create();
        for (final Map.Entry<String, List<Long>> oneClassToTarget : targetObjects.entrySet()) {
            /* determine actual class from given target object class name */
            String targetObjectClassName = oneClassToTarget.getKey();
            final int lastDot = targetObjectClassName.lastIndexOf('.');
            if (lastDot > 0) {
                targetObjectClassName = targetObjectClassName.substring(lastDot + 1);
            }
            final Class<? extends IObject> targetObjectClass = graphPathBean.getClassForSimpleName(targetObjectClassName);
            /* check that it is legal to target the given class */
            final Iterator<Class<? extends IObject>> legalClassesIterator = legalClasses.iterator();
            do {
                if (!legalClassesIterator.hasNext()) {
                    final Exception e = new IllegalArgumentException("cannot target " + targetObjectClassName);
                    throw helper.cancel(new ERR(), e, "bad-target");
                }
            } while (!legalClassesIterator.next().isAssignableFrom(targetObjectClass));
            /* note IDs to target for the class */
            final Collection<Long> ids = oneClassToTarget.getValue();
            targetMultimap.putAll(targetObjectClass.getName(), ids);
        }
        return targetMultimap;
    }

    /**
     * Get the simple names of the top-level superclasses of the given classes.
     * @param modelClasses some model classes
     * @return the simple names of their top-level classes
     */
    public Set<String> getTopLevelNames(Iterable<Class<? extends IObject>> modelClasses) {
        final Set<String> classNames = new HashSet<String>();
        for (Class<?> modelClass : modelClasses) {
            while (true) {
                final Class<?> superclass = modelClass.getSuperclass();
                if (superclass == Object.class) {
                    break;
                } else {
                    modelClass = superclass;
                }
            }
            classNames.add(modelClass.getSimpleName());
        }
        return classNames;
    }

    /**
     * Publish database changes to the event log.
     * @param context the context for publishing the application event
     * @param action the name of the change action
     * @param className the class of objects that were changed
     * @param ids the IDs of the objects that were changed
     */
    public void publishEventLog(ApplicationContext context, String action, String className, Collection<Long> ids) {
        final Class<? extends IObject> actualClass;
        try {
            actualClass = Class.forName(className).asSubclass(IObject.class);
        } catch (ClassNotFoundException cnfe) {
            throw new InternalException("reference to unknown model class " + className + ": " + cnfe);
        }
        context.publishEvent(new EventLogMessage(this, action, actualClass, ImmutableList.copyOf(ids)));
    }
}