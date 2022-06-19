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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import ode.model.IObject;
import ode.security.ACLVoter;
import ode.security.basic.LightAdminPrivileges;
import ode.services.graphs.GraphException;
import ode.services.graphs.GraphPathBean;
import ode.services.graphs.GraphPolicy;
import ode.services.graphs.GraphTraversal;
import ode.services.util.ReadOnlyStatus;
import ode.system.Login;
import ode.system.Roles;
import ode.cmd.FindChildren;
import ode.cmd.FoundChildren;
import ode.cmd.HandleI.Cancel;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.Response;

/**
 * Request to identify children or contents of model objects, whether direct or indirect.
 */
public class FindChildrenI extends FindChildren implements IRequest, ReadOnlyStatus.IsAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindChildrenI.class);

    private static final ImmutableMap<String, String> ALL_GROUPS_CONTEXT = ImmutableMap.of(Login.ODE_GROUP, "-1");

    private static final Set<GraphPolicy.Ability> REQUIRED_ABILITIES = ImmutableSet.of();

    private final ACLVoter aclVoter;
    private final GraphPathBean graphPathBean;
    private final Set<Class<? extends IObject>> targetClasses;
    private final GraphPolicy graphPolicy;

    private final Set<Class<? extends IObject>> classesToFind = new HashSet<Class<? extends IObject>>();

    private Helper helper;
    private GraphHelper graphHelper;
    private GraphTraversal graphTraversal;

    private int targetObjectCount = 0;
    private int foundObjectCount = 0;

    /**
     * Construct a new <q>find-children</q> request; called from {@link GraphRequestFactory#getRequest(Class)}.
     * @param aclVoter ACL voter for permissions checking
     * @param securityRoles the security roles
     * @param graphPathBean the graph path bean to use
     * @param adminPrivileges the light administrator privileges helper
     * @param targetClasses legal target object classes for the search
     * @param graphPolicy the graph policy to apply for the search
     */
    public FindChildrenI(ACLVoter aclVoter, Roles securityRoles, GraphPathBean graphPathBean, LightAdminPrivileges adminPrivileges,
            Set<Class<? extends IObject>> targetClasses, GraphPolicy graphPolicy) {
        this.aclVoter = aclVoter;
        this.graphPathBean = graphPathBean;
        this.targetClasses = targetClasses;
        this.graphPolicy = graphPolicy;
    }

    @Override
    public Map<String, String> getCallContext() {
       return new HashMap<String, String>(ALL_GROUPS_CONTEXT);
    }

    @Override
    public void init(Helper helper) {
        if (LOGGER.isDebugEnabled()) {
            final GraphUtil.ParameterReporter arguments = new GraphUtil.ParameterReporter();
            arguments.addParameter("targetObjects", targetObjects);
            arguments.addParameter("typesOfChildren", typesOfChildren);
            arguments.addParameter("stopBefore", stopBefore);
            LOGGER.debug("request: " + arguments);
        }

        this.helper = helper;
        helper.setSteps(1);
        this.graphHelper = new GraphHelper(helper, graphPathBean);

        if (CollectionUtils.isEmpty(typesOfChildren)) {
            final Exception e = new IllegalArgumentException("no types of children specified to find");
            throw helper.cancel(new ERR(), e, "bad-options");
        }

        classesToFind.addAll(graphHelper.getClassesFromNames(typesOfChildren));

        final Set<String> targetClassNames = graphHelper.getTopLevelNames(graphHelper.getClassesFromNames(targetObjects.keySet()));
        final Set<String> childTypeNames = graphHelper.getTopLevelNames(classesToFind);
        final Set<String> currentStopBefore = graphHelper.getTopLevelNames(graphHelper.getClassesFromNames(stopBefore));
        final Set<String> suggestedStopBefore;
        try {
            suggestedStopBefore = StopBeforeHelper.get().getStopBeforeChildren(targetClassNames, childTypeNames);
        } catch (IllegalArgumentException e) {
            throw helper.cancel(new ERR(), e, "bad-options");
        }
        final Set<String> extraStopBefore = Sets.difference(suggestedStopBefore, currentStopBefore);
        stopBefore.addAll(extraStopBefore);
        if (!extraStopBefore.isEmpty() && LOGGER.isDebugEnabled()) {
            LOGGER.debug("to stopBefore added: " + Joiner.on(',').join(Ordering.natural().sortedCopy(extraStopBefore)));
        }

        final Iterable<Function<GraphPolicy, GraphPolicy>> graphPolicyAdjusters;
        if (CollectionUtils.isEmpty(stopBefore)) {
            graphPolicyAdjusters = Collections.emptySet();
        } else {
            final Set<Class<? extends IObject>> typesToStopBefore = graphHelper.getClassesFromNames(stopBefore);
            final Function<GraphPolicy, GraphPolicy> graphPolicyAdjuster = new Function<GraphPolicy, GraphPolicy>() {
                @Override
                public GraphPolicy apply(GraphPolicy graphPolicy) {
                    return SkipTailPolicy.getSkipTailPolicy(graphPolicy, GraphUtil.getPredicateFromClasses(typesToStopBefore));
                }
            };
            graphPolicyAdjusters = Collections.singleton(graphPolicyAdjuster);
        }

        graphTraversal = graphHelper.prepareGraphTraversal(null, REQUIRED_ABILITIES, graphPolicy, graphPolicyAdjusters,
                aclVoter, graphPathBean, null, new NullGraphTraversalProcessor(REQUIRED_ABILITIES), false);
    }

    @Override
    public Object step(int step) throws Cancel {
        helper.assertStep(step);
        try {
            switch (step) {
            case 0:
                final SetMultimap<String, Long> targetMultimap = graphHelper.getTargetMultimap(targetClasses, targetObjects);
                targetObjectCount += targetMultimap.size();
                final Entry<SetMultimap<String, Long>, SetMultimap<String, Long>> plan =
                        graphTraversal.planOperation(targetMultimap, true, true);
                if (!plan.getValue().isEmpty()) {
                    final Exception e = new IllegalStateException("querying the model graph does not delete any objects");
                    helper.cancel(new ERR(), e, "graph-fail");
                }
                return plan.getKey();
            default:
                final Exception e = new IllegalArgumentException("model object graph operation has no step " + step);
                throw helper.cancel(new ERR(), e, "bad-step");
            }
        } catch (Cancel c) {
            throw c;
        } catch (GraphException ge) {
            final ode.cmd.GraphException graphERR = new ode.cmd.GraphException();
            graphERR.message = ge.message;
            throw helper.cancel(graphERR, ge, "graph-fail");
        } catch (Throwable t) {
            throw helper.cancel(new ERR(), t, "graph-fail");
        }
    }

    @Override
    public void finish() {
    }

    @Override
    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);
        if (step == 0) {
            SetMultimap<String, Long> result = (SetMultimap<String, Long>) object;
            result = Multimaps.filterKeys(result, new Predicate<String>() {
                @Override
                public boolean apply(String foundClassName) {
                    final Class<? extends IObject> foundClass;
                    try {
                        foundClass = Class.forName(foundClassName).asSubclass(IObject.class);
                    } catch (ClassCastException | ClassNotFoundException e) {
                        throw helper.cancel(new ERR(), e, "graph-fail");
                    }
                    for (final Class<? extends IObject> classToFind : classesToFind) {
                        if (classToFind.isAssignableFrom(foundClass)) {
                            return true;
                        }
                    }
                    return false;
                }});
            final Map<String, List<Long>> foundObjects = GraphUtil.copyMultimapForResponse(result);
            foundObjectCount += result.size();
            final FoundChildren response = new FoundChildren(foundObjects);
            helper.setResponseIfNull(response);
            helper.info("in finding children of " + targetObjectCount +
                    ", found " + foundObjectCount + " in total");

            if (LOGGER.isDebugEnabled()) {
                final GraphUtil.ParameterReporter arguments = new GraphUtil.ParameterReporter();
                arguments.addParameter("children", response.children);
                LOGGER.debug("response: " + arguments);
            }
        }
    }

    @Override
    public Response getResponse() {
        return helper.getResponse();
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return true;
    }
}