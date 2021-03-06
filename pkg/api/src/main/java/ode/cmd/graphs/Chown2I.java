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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import ode.system.EventContext;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import ode.api.IAdmin;
import ode.api.IQuery;
import ode.model.IAnnotationLink;
import ode.model.ILink;
import ode.model.IObject;
import ode.model.containers.DatasetImageLink;
import ode.model.containers.FolderImageLink;
import ode.model.containers.FolderRoiLink;
import ode.model.containers.ProjectDatasetLink;
import ode.model.enums.AdminPrivilege;
import ode.model.internal.Details;
import ode.model.meta.Experimenter;
import ode.model.screen.ScreenPlateLink;
import ode.parameters.Parameters;
import ode.security.ACLVoter;
import ode.security.basic.LightAdminPrivileges;
import ode.services.delete.Deletion;
import ode.services.graphs.GraphException;
import ode.services.graphs.GraphPathBean;
import ode.services.graphs.GraphPolicy;
import ode.services.graphs.GraphTraversal;
import ode.services.graphs.GroupPredicate;
import ode.services.graphs.PermissionsPredicate;
import ode.services.util.ReadOnlyStatus;
import ode.system.Login;
import ode.system.Roles;
import ode.ServerError;
import ode.cmd.Chown2;
import ode.cmd.Chown2Response;
import ode.cmd.HandleI.Cancel;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.Response;

/**
 * Request to give model objects to a different experimenter.
 */
public class Chown2I extends Chown2 implements IRequest, ReadOnlyStatus.IsAware, WrappableRequest<Chown2> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Chown2I.class);

    private static final ImmutableMap<String, String> ALL_GROUPS_CONTEXT = ImmutableMap.of(Login.ODE_GROUP, "-1");

    private static final Set<GraphPolicy.Ability> REQUIRED_ABILITIES = ImmutableSet.of(GraphPolicy.Ability.DELETE);

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
    private Set<Long> acceptableGroupsFrom;
    private Set<Long> acceptableGroupsTo;

    private GraphTraversal.PlanExecutor unlinker;
    private GraphTraversal.PlanExecutor processor;

    private int targetObjectCount = 0;
    private int deletedObjectCount = 0;
    private int givenObjectCount = 0;

    /**
     * Construct a new <q>chown</q> request; called from {@link GraphRequestFactory#getRequest(Class)}.
     * @param aclVoter ACL voter for permissions checking
     * @param securityRoles the security roles
     * @param graphPathBean the graph path bean to use
     * @param adminPrivileges the light administrator privileges helper
     * @param deletionInstance a deletion instance for deleting files
     * @param targetClasses legal target object classes for chown
     * @param graphPolicy the graph policy to apply for chown
     * @param unnullable properties that, while nullable, may not be nulled by a graph traversal operation
     * @param applicationContext the Bhojpur ODE application context from Spring
     */
    public Chown2I(ACLVoter aclVoter, Roles securityRoles, GraphPathBean graphPathBean, LightAdminPrivileges adminPrivileges,
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
            arguments.addParameter("userId", userId);
            arguments.addParameter("targetObjects", targetObjects);
            arguments.addParameter("targetUsers", targetUsers);
            arguments.addParameter("childOptions", childOptions);
            arguments.addParameter("dryRun", dryRun);
            LOGGER.debug("request: " + arguments);
        }

        this.helper = helper;
        helper.setSteps(dryRun ? 5 : 7);
        this.graphHelper = new GraphHelper(helper, graphPathBean);

        /* if the current user is not an administrator then find of which groups the target user is a member */
        final EventContext eventContext = helper.getEventContext();
        final boolean isChownPrivilege = graphHelper.checkIsAdministrator(adminPrivileges.getPrivilege(AdminPrivilege.VALUE_CHOWN));
        if (isChownPrivilege) {
            acceptableGroupsFrom = null;
            acceptableGroupsTo = null;
        } else {
            final IAdmin iAdmin = helper.getServiceFactory().getAdminService();
            acceptableGroupsFrom = ImmutableSet.copyOf(eventContext.getLeaderOfGroupsList());
            acceptableGroupsTo = ImmutableSet.copyOf(iAdmin.getMemberOfGroupIds(new Experimenter(userId, false)));
            if (acceptableGroupsFrom.isEmpty()) {
                throw new RuntimeException(new GraphException("not an owner of any group"));
            }
            if (targetUsers != null) {
                for (final Long targetUserId : targetUsers) {
                    final Set<Long> groupsForTargetUserData = new HashSet<Long>(acceptableGroupsFrom);
                    final Experimenter targetUser = new Experimenter(targetUserId, false);
                    groupsForTargetUserData.retainAll(iAdmin.getMemberOfGroupIds(targetUser));
                    if (groupsForTargetUserData.isEmpty()) {
                        final String message = "not an owner of any group of " +
                                Experimenter.class.getName() + "[" + targetUserId + "]";
                        throw new RuntimeException(new GraphException(message));
                    }
                }
            }
        }

        final Set<GraphPolicy.Ability> requiredAbilities;
        if (isChownPrivilege) {
            requiredAbilities = Collections.<GraphPolicy.Ability>emptySet();
        } else {
            requiredAbilities = REQUIRED_ABILITIES;
        }

        graphPolicy.registerPredicate(new GroupPredicate(securityRoles));
        graphPolicy.registerPredicate(new PermissionsPredicate());

        internalProcessor = new InternalProcessor(requiredAbilities);

        graphTraversal = graphHelper.prepareGraphTraversal(childOptions, REQUIRED_ABILITIES, graphPolicy, graphPolicyAdjusters,
                aclVoter, graphPathBean, unnullable, internalProcessor, dryRun);

        if (isChownPrivilege) {
            graphTraversal.setOwnsAll();
        }

        graphPolicyAdjusters = null;
    }

    /**
     * Get a map of which classes implement which interfaces.
     * Map keys include the interfaces implemented indirectly as implemented interfaces extend others.
     * Map values are limited to model classes that have no subclasses and that are legal targets for this request.
     * @return the map of interfaces to classes
     * @throws ServerError if the graph path bean reports an unknown class
     */
    private SetMultimap<Class<?>, Class<? extends IObject>> getImplementorMap() throws ServerError {
        /* find the classes that have no subclasses */
        final Set<Class<? extends IObject>> leafClasses = new HashSet<Class<? extends IObject>>();
        for (final String className : graphPathBean.getAllClasses()) {
            if (graphPathBean.getSubclassesOf(className).isEmpty()) {
                try {
                    leafClasses.add(Class.forName(className).asSubclass(IObject.class));
                } catch (ClassNotFoundException cnfe) {
                    throw new ServerError(null, null, "graph path bean reports unknown class " + className, cnfe);
                }
            }
        }
        /* retain only the classes that are legal targets */
        final Iterator<Class<? extends IObject>> leafClassIterator = leafClasses.iterator();
        while (leafClassIterator.hasNext()) {
            final Class<? extends IObject> leafClass = leafClassIterator.next();
            boolean isLegal = false;
            for (final Class<? extends IObject> targetClass : targetClasses) {
                if (targetClass.isAssignableFrom(leafClass)) {
                    isLegal = true;
                    break;
                }
            }
            if (!isLegal) {
                leafClassIterator.remove();
            }
        }
        /* index the classes by which interfaces they implement, whether directly or indirectly */
        final SetMultimap<Class<?>, Class<? extends IObject>> implementors = HashMultimap.create();
        for (final Class<? extends IObject> leafClass : leafClasses) {
            final Set<Class<?>> interfaces = new HashSet<Class<?>>(Arrays.asList(leafClass.getInterfaces()));
            while (!interfaces.isEmpty()) {
                final Iterator<Class<?>> interfaceIterator = interfaces.iterator();
                final Class<?> intrface = interfaceIterator.next();
                interfaceIterator.remove();
                interfaces.addAll(Arrays.asList(intrface.getInterfaces()));
                implementors.put(intrface, leafClass);
            }
        }
        return implementors;
    }

    /**
     * Add to {@link ode.cmd.GraphQuery#targetObjects} all model objects owned by target users that this current user can target.
     * @throws ServerError if the graph path bean reports an unknown class
     */
    private void targetAllUsersObjects() throws ServerError {
        final SetMultimap<Class<?>, Class<? extends IObject>> implementorMap = getImplementorMap();
        final IQuery iQuery = helper.getServiceFactory().getQueryService();
        Parameters params = new Parameters().addList("owners", targetUsers);
        if (acceptableGroupsFrom != null) {
            params = params.addSet("groups", acceptableGroupsFrom);
        }
        final Set<Class<? extends IObject>> classesToQuery = new HashSet<Class<? extends IObject>>();
        for (final Class<? extends IObject> targetClass : targetClasses) {
            final Set<Class<? extends IObject>> implementors = implementorMap.get(targetClass);
            if (implementors.isEmpty()) {
                classesToQuery.add(targetClass);
            } else {
                classesToQuery.addAll(implementors);
            }
        }
        for (final Class<? extends IObject> queryClass : classesToQuery) {
            final String queryClassName = queryClass.getName();
            final String idProperty = graphPathBean.getIdentifierProperty(queryClassName);
            String hql = "SELECT " + idProperty + " FROM " + queryClassName + " WHERE details.owner.id IN (:owners)";
            if (acceptableGroupsFrom != null) {
                hql += " AND details.group.id IN (:groups)";
            }
            List<Long> idList = targetObjects.get(queryClassName);
            if (idList == null) {
                idList = new ArrayList<Long>();
                targetObjects.put(queryClassName, idList);
            }
            for (final Object[] id : iQuery.projection(hql, params)) {
                idList.add((Long) id[0]);
            }
        }
    }

    @Override
    public Object step(int step) throws Cancel {
        helper.assertStep(step);
        try {
            switch (step) {
            case 0:
                if (CollectionUtils.isNotEmpty(targetUsers)) {
                    targetAllUsersObjects();
                }
                return null;
            case 1:
                final SetMultimap<String, Long> targetMultimap = graphHelper.getTargetMultimap(targetClasses, targetObjects);
                targetObjectCount += targetMultimap.size();
                return graphTraversal.planOperation(targetMultimap, true, true);
            case 2:
                graphTraversal.assertNoPolicyViolations();
                return null;
            case 3:
                processor = graphTraversal.processTargets();
                return null;
            case 4:
                unlinker = graphTraversal.unlinkTargets(false);
                graphTraversal = null;
                return null;
            case 5:
                unlinker.execute();
                return null;
            case 6:
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
        if (step == 1) {
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
            final Map<String, List<Long>> givenObjects = GraphUtil.copyMultimapForResponse(result.getKey());
            final Map<String, List<Long>> deletedObjects = GraphUtil.copyMultimapForResponse(result.getValue());
            givenObjectCount += result.getKey().size();
            deletedObjectCount += result.getValue().size();
            final Chown2Response response = new Chown2Response(givenObjects, deletedObjects);
            helper.setResponseIfNull(response);
            helper.info("in " + (dryRun ? "mock " : "") + "chown to " + userId + " of " + targetObjectCount +
                    ", gave " + givenObjectCount + " and deleted " + deletedObjectCount + " in total");

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
    public void copyFieldsTo(Chown2 request) {
        GraphUtil.copyFields(this, request);
        request.userId = userId;
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
        return 1;
    }

    @Override
    public GraphPolicy.Action getActionForStarting() {
        return GraphPolicy.Action.INCLUDE;
    }

    @Override
    public Map<String, List<Long>> getStartFrom(Response response) {
        return ((Chown2Response) response).includedObjects;
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return dryRun;
    }

    /**
     * Notes links that are to be given. Intended for use in {@link HashSet}s.
     */
    private final class LinkDetails {
        private final Class<? extends ILink> linkType;
        private final long parentId, childId;

        /**
         * Construct a new note of link details.
         * @param linkType the type of link
         * @param parentId the ID of the link's parent <q>from</q> object
         * @param childId the ID of the link's child <q>to</q> object
         */
        public LinkDetails(Class<? extends ILink> linkType, long parentId, long childId) {
            this.linkType = linkType;
            this.parentId = parentId;
            this.childId = childId;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            } else if (other instanceof LinkDetails) {
                final LinkDetails otherLink = (LinkDetails) other;
                return this.linkType == otherLink.linkType &&
                        this.parentId == otherLink.parentId &&
                        this.childId == otherLink.childId;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new Object[] {LinkDetails.class, linkType, parentId, childId});
        }
    }

    /**
     * A <q>chown</q> processor that updates model objects' user.
     */
    private final class InternalProcessor extends BaseGraphTraversalProcessor {

        private final Logger LOGGER = LoggerFactory.getLogger(InternalProcessor.class);

        @SuppressWarnings("unchecked")
        private final ImmutableSet<Class<? extends ILink>> UNIQUENESS_RISK_LINK_TYPES = ImmutableSet.of(
                IAnnotationLink.class, ScreenPlateLink.class, ProjectDatasetLink.class, DatasetImageLink.class,
                FolderImageLink.class, FolderRoiLink.class);

        private final Long userFromId = helper.getEventContext().getCurrentUserId();
        private final Experimenter userTo = new Experimenter(userId, false);

        private final Set<GraphPolicy.Ability> requiredAbilities;

        private final Set<LinkDetails> linksToChown = new HashSet<LinkDetails>();

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
            final String update = "UPDATE " + className + " SET details.owner = :user WHERE id IN (:ids)";
            final int count =
                    session.createQuery(update).setParameter("user", userTo).setParameterList("ids", ids).executeUpdate();
            graphHelper.publishEventLog(applicationContext, "UPDATE", className, ids);
            if (count != ids.size()) {
                LOGGER.warn("not all the objects of type " + className + " could be processed");
            }
        }

        @Override
        public Set<GraphPolicy.Ability> getRequiredPermissions() {
            return requiredAbilities;
        }

        /**
         * Determine if the given link type may be duplicated among different users.
         * @param linkType the type of link
         * @return if multiple identical links may exist among different users
         */
        private boolean isDuplicationRisk(Class<? extends ILink> linkType) {
            for (final Class<? extends ILink> riskType : UNIQUENESS_RISK_LINK_TYPES) {
                if (riskType.isAssignableFrom(linkType)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void assertMayProcess(String className, long objectId, Details details) throws GraphException {
            if (details.getOwner().getId() == userId) {
                /* no-op */
                return;
            }
            /* final Long objectOwnerId = details.getOwner().getId();
               also allow userFromId.equals(objectOwnerId) for users to chown their own data */
            final Long objectGroupId = details.getGroup().getId();
            if (!(acceptableGroupsFrom == null || acceptableGroupsFrom.contains(objectGroupId))) {
                throw new GraphException("user " + userFromId + " is not an owner of group " + objectGroupId);
            }
            if (!(acceptableGroupsTo == null || acceptableGroupsTo.contains(objectGroupId))) {
                throw new GraphException("user " + userId + " is not a member of group " + objectGroupId);
            }
            final Class<?> actualClass;
            try {
                actualClass = Class.forName(className);
            } catch (ClassNotFoundException cnfe) {
                LOGGER.error("could not look up model class " + className, cnfe);
                return;
            }
            if (ILink.class.isAssignableFrom(actualClass)) {
                final Class<? extends ILink> linkType = actualClass.asSubclass(ILink.class);
                if (isDuplicationRisk(linkType)) {
                    final Object[] parentChildIds = (Object[]) session.createQuery(
                            "SELECT parent.id, child.id FROM " + className + " WHERE id = :id")
                            .setParameter("id", objectId)
                            .uniqueResult();
                    final Long parentId = (Long) parentChildIds[0];
                    final Long childId = (Long) parentChildIds[1];
                    final Long count = (Long) session.createQuery(
                            "SELECT COUNT(*) FROM " + className +
                            " WHERE parent.id = :parent AND child.id = :child AND details.owner.id = :owner")
                            .setParameter("parent", parentId).setParameter("child", childId).setParameter("owner", userId)
                            .uniqueResult();
                    if (count > 0 || !linksToChown.add(new LinkDetails(linkType, parentId, childId))) {
                        throw new GraphException("would have user " + userId + " owning multiple identical links");
                    }
                }
            }
        }
    }
}