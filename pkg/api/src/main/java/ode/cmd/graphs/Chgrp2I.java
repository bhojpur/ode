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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import ode.system.EventContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import ode.model.IObject;
import ode.model.enums.AdminPrivilege;
import ode.model.internal.Permissions;
import ode.model.meta.ExperimenterGroup;
import ode.security.ACLVoter;
import ode.security.basic.LightAdminPrivileges;
import ode.services.delete.Deletion;
import ode.services.graphs.GraphException;
import ode.services.graphs.GraphPathBean;
import ode.services.graphs.GraphPolicy;
import ode.services.graphs.GraphTraversal;
import ode.services.graphs.GroupPredicate;
import ode.services.util.ReadOnlyStatus;

import ode.system.Login;
import ode.system.Roles;
import ode.cmd.Chgrp2;
import ode.cmd.Chgrp2Response;
import ode.cmd.HandleI.Cancel;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.Response;

/**
 * Request to move model objects to a different experiment group.
 */
public class Chgrp2I extends Chgrp2 implements IRequest, ReadOnlyStatus.IsAware, WrappableRequest<Chgrp2> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Chgrp2I.class);

    private static final ImmutableMap<String, String> ALL_GROUPS_CONTEXT = ImmutableMap.of(Login.ODE_GROUP, "-1");

    private static final Set<GraphPolicy.Ability> REQUIRED_ABILITIES = ImmutableSet.of(GraphPolicy.Ability.OWN);

    private final ACLVoter aclVoter;
    private final Roles securityRoles;
    private final GraphPathBean graphPathBean;
    private final LightAdminPrivileges adminPrivileges;
    private final Deletion deletionInstance;
    private final Set<Class<? extends IObject>> targetClasses;
    private GraphPolicy graphPolicy;  /* not final because of adjustGraphPolicy */
    private final SetMultimap<String, String> unnullable;
    private final ApplicationContext applicationContext;

    private List<Function<GraphPolicy, GraphPolicy>> graphPolicyAdjusters = new ArrayList<Function<GraphPolicy, GraphPolicy>>();
    private Helper helper;
    private GraphHelper graphHelper;
    private GraphTraversal graphTraversal;
    private InternalProcessor internalProcessor;

    private GraphTraversal.PlanExecutor unlinker;
    private GraphTraversal.PlanExecutor processor;

    private int targetObjectCount = 0;
    private int deletedObjectCount = 0;
    private int movedObjectCount = 0;

    /**
     * Construct a new <q>chgrp</q> request; called from {@link GraphRequestFactory#getRequest(Class)}.
     * @param aclVoter ACL voter for permissions checking
     * @param securityRoles the security roles
     * @param graphPathBean the graph path bean to use
     * @param adminPrivileges the light administrator privileges helper
     * @param deletionInstance a deletion instance for deleting files
     * @param targetClasses legal target object classes for chgrp
     * @param graphPolicy the graph policy to apply for chgrp
     * @param unnullable properties that, while nullable, may not be nulled by a graph traversal operation
     * @param applicationContext the Bhojpur ODE application context from Spring
     */
    public Chgrp2I(ACLVoter aclVoter, Roles securityRoles, GraphPathBean graphPathBean, LightAdminPrivileges adminPrivileges,
            Deletion deletionInstance, Set<Class<? extends IObject>> targetClasses, GraphPolicy graphPolicy,
            SetMultimap<String, String> unnullable, ApplicationContext applicationContext) {
        this.aclVoter = aclVoter;
        this.securityRoles = securityRoles;
        this.graphPathBean = graphPathBean;
        this.adminPrivileges = adminPrivileges;
        this.deletionInstance = deletionInstance;
        this.targetClasses = targetClasses;
        this.graphPolicy = graphPolicy;
        this.unnullable = unnullable;
        this.applicationContext = applicationContext;
    }

    @Override
    public Map<String, String> getCallContext() {
        return new HashMap<String, String>(ALL_GROUPS_CONTEXT);
    }

    @Override
    public void init(Helper helper) {
        if (LOGGER.isDebugEnabled()) {
            final GraphUtil.ParameterReporter arguments = new GraphUtil.ParameterReporter();
            arguments.addParameter("groupId", groupId);
            arguments.addParameter("targetObjects", targetObjects);
            arguments.addParameter("childOptions", childOptions);
            arguments.addParameter("dryRun", dryRun);
            LOGGER.debug("request: " + arguments);
        }

        this.helper = helper;
        helper.setSteps(dryRun ? 4 : 6);
        this.graphHelper = new GraphHelper(helper, graphPathBean);

        /* check that the user is a member of the destination group */
        final EventContext eventContext = helper.getEventContext();
        final boolean isChgrpPrivilege = graphHelper.checkIsAdministrator(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_CHGRP));
        if (!(isChgrpPrivilege || eventContext.getMemberOfGroupsList().contains(groupId))) {
            final Exception e = new IllegalArgumentException("not a member of the chgrp destination group");
            throw helper.cancel(new ERR(), e, "not-in-group");
        }

        final ExperimenterGroup destinationGroup = (ExperimenterGroup) helper.getSession().get(ExperimenterGroup.class, groupId);
        final Permissions destinationGroupPermissions = destinationGroup.getDetails().getPermissions();
        final boolean isToGroupReadable = destinationGroupPermissions.isGranted(Permissions.Role.GROUP, Permissions.Right.READ);

        if (!isToGroupReadable) {
            graphPolicy.setCondition("to_private");
        }

        final Set<GraphPolicy.Ability> requiredAbilities;
        if (isChgrpPrivilege) {
            requiredAbilities = Collections.<GraphPolicy.Ability>emptySet();
        } else {
            requiredAbilities = REQUIRED_ABILITIES;
        }

        graphPolicy.registerPredicate(new GroupPredicate(securityRoles));

        internalProcessor = new InternalProcessor(requiredAbilities);

        graphTraversal = graphHelper.prepareGraphTraversal(childOptions, requiredAbilities, graphPolicy, graphPolicyAdjusters,
                aclVoter, graphPathBean, unnullable, internalProcessor, dryRun);

        if (isChgrpPrivilege) {
            graphTraversal.setOwnsAll();
        }

        graphPolicyAdjusters = null;
    }

    @Override
    public Object step(int step) throws Cancel {
        helper.assertStep(step);
        try {
            switch (step) {
            case 0:
                final SetMultimap<String, Long> targetMultimap = graphHelper.getTargetMultimap(targetClasses, targetObjects);
                targetObjectCount += targetMultimap.size();
                return graphTraversal.planOperation(targetMultimap, true, true);
            case 1:
                graphTraversal.assertNoPolicyViolations();
                return null;
            case 2:
                processor = graphTraversal.processTargets();
                return null;
            case 3:
                unlinker = graphTraversal.unlinkTargets(true);
                graphTraversal = null;
                return null;
            case 4:
                unlinker.execute();
                return null;
            case 5:
                processor.execute();
                return null;
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
            /* if the results object were in terms of IObjectList then this would need IceMapper.map */
            final Entry<SetMultimap<String, Long>, SetMultimap<String, Long>> result =
                    (Entry<SetMultimap<String, Long>, SetMultimap<String, Long>>) object;
            if (!dryRun) {
                try {
                    internalProcessor.deleteFiles(deletionInstance);
                } catch (Exception e) {
                    helper.cancel(new ERR(), e, "file-delete-fail");
                }
            }
            final Map<String, List<Long>> movedObjects = GraphUtil.copyMultimapForResponse(result.getKey());
            final Map<String, List<Long>> deletedObjects = GraphUtil.copyMultimapForResponse(result.getValue());
            movedObjectCount += result.getKey().size();
            deletedObjectCount += result.getValue().size();
            final Chgrp2Response response = new Chgrp2Response(movedObjects, deletedObjects);
            helper.setResponseIfNull(response);
            helper.info("in " + (dryRun ? "mock " : "") + "chgrp to " + groupId + " of " + targetObjectCount +
                    ", moved " + movedObjectCount + " and deleted " + deletedObjectCount + " in total");

            if (LOGGER.isDebugEnabled()) {
                final GraphUtil.ParameterReporter arguments = new GraphUtil.ParameterReporter();
                arguments.addParameter("includedObjects", response.includedObjects);
                arguments.addParameter("deletedObjects", response.deletedObjects);
                LOGGER.debug("response: " + arguments);
            }
        }
    }

    @Override
    public Response getResponse() {
        return helper.getResponse();
    }

    @Override
    public void copyFieldsTo(Chgrp2 request) {
        GraphUtil.copyFields(this, request);
        request.groupId = groupId;
    }

    @Override
    public void adjustGraphPolicy(Function<GraphPolicy, GraphPolicy> adjuster) {
        if (graphPolicyAdjusters == null) {
            throw new IllegalStateException("request is already initialized");
        } else {
            graphPolicyAdjusters.add(adjuster);
        }
    }

    @Override
    public int getStepProvidingCompleteResponse() {
        return 0;
    }

    @Override
    public GraphPolicy.Action getActionForStarting() {
        return GraphPolicy.Action.INCLUDE;
    }

    @Override
    public Map<String, List<Long>> getStartFrom(Response response) {
        return ((Chgrp2Response) response).includedObjects;
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return dryRun;
    }

    /**
     * A <q>chgrp</q> processor that updates model objects' group.
     */
    private final class InternalProcessor extends BaseGraphTraversalProcessor {

        private final Logger LOGGER = LoggerFactory.getLogger(InternalProcessor.class);

        private final ExperimenterGroup group = new ExperimenterGroup(groupId, false);

        private final Set<GraphPolicy.Ability> requiredAbilities;

        public InternalProcessor(Set<GraphPolicy.Ability> requiredAbilities) {
            super(helper.getSession());
            this.requiredAbilities = requiredAbilities;
        }

        @Override
        public void deleteInstances(String className, Collection<Long> ids) throws GraphException {
            super.deleteInstances(className, ids);
            graphHelper.publishEventLog(applicationContext, "DELETE", className, ids);
        }

        @Override
        public void processInstances(String className, Collection<Long> ids) throws GraphException {
            final String update = "UPDATE " + className + " SET details.group = :group WHERE id IN (:ids)";
            final int count =
                    session.createQuery(update).setParameter("group", group).setParameterList("ids", ids).executeUpdate();
            graphHelper.publishEventLog(applicationContext, "UPDATE", className, ids);
            if (count != ids.size()) {
                LOGGER.warn("not all the objects of type " + className + " could be processed");
            }
        }

        @Override
        public Set<GraphPolicy.Ability> getRequiredPermissions() {
            return requiredAbilities;
        }
    }
}