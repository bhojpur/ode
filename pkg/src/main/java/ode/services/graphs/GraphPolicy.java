package ode.services.graphs;

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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import ode.model.IObject;
 
/**
 * A policy guides how to traverse the graph. This class' methods are expected to be fast and are not required to be thread-safe.
 */
public abstract class GraphPolicy {

    /**
     * The action to take on an object instance.
     */
    public static enum Action {
        /** do not include the object in the operation */
        EXCLUDE,
        /** delete the object */
        DELETE,
        /** include the object in the operation */
        INCLUDE,
        /** object is inappropriate for the operation and may be related to both included and excluded objects */
        OUTSIDE;
    }

    /**
     * If an object instance has any {@link Action#EXCLUDE}d <q>parents</q> that would prevent it from being <q>orphaned</q>.
     */
    public static enum Orphan {
        /** it is not known and it does not matter if the object is an orphan: effort should not yet be made to find out */
        IRRELEVANT,
        /** it is not known but it matters if the object is an orphan: effort should be made to find out */
        RELEVANT,
        /** the object is an orphan */
        IS_LAST,
        /** the object is not an orphan */
        IS_NOT_LAST;
    }

    /**
     * Abilities that the user may have to operate upon model objects.
     * Note that system users have all abilities.
     */
    public static enum Ability {
        /**
         * the user's ability to update the object, as judged by
         * {@link ode.security.ACLVoter#allowUpdate(IObject, ode.model.internal.Details)}
         */
        UPDATE,

        /**
         * the user's ability to delete the object, as judged by
         * {@link ode.security.ACLVoter#allowDelete(IObject, ode.model.internal.Details)}
         */
        DELETE,

        /**
         * the user's ability to change permissions on the object, as judged by
         * {@link ode.security.ACLVoter#allowChmod(IObject)}
         */
        CHMOD,

        /**
         * the user's ability to move the object, as judged by
         * {@link ode.model.internal.Permissions#isDisallowChgrp()}
         */
        CHGRP,

        /**
         * the user's ability to give the object, as judged by
         * {@link ode.model.internal.Permissions#isDisallowChown()}
         */
        CHOWN,

        /**
         * the user actually owns the object
         */
        OWN;
    }

    /**
     * A tuple noting the state of a mapped object instance in the current graph traversal.
     */
    public static abstract class Details {

        /** the unloaded instance */
        public final IObject subject;

        /**
         * the ID of the object's {@link ode.model.meta.Experimenter},
         * or {@code null} if the object does not have an owner 
         */
        public final Long ownerId;

        /**
         * the ID of the object's {@link ode.model.meta.ExperimenterGroup},
         * or {@code null} if the object does not have a group
         */
        public final Long groupId;

        /** the current permissions on the object */
        public final Set<Ability> permissions;

        /** the current plan for the object, may be mutated by {@link GraphPolicy#review(Map, Details, Map, Set, boolean)} */
        public Action action;

        /**
         * the current <q>orphan</q> state of the object, may be mutated by {@link GraphPolicy#review(Map, Details, Map, Set, boolean)};
         * applies only if {@link #action} is {@link Action#EXCLUDE}
         */
        public Orphan orphan;

        /** if the user's permissions for the object should be checked before the {@link GraphTraversal.Processor} acts upon it */
        public boolean isCheckPermissions;

        /**
         * Construct a note of an object and its details.
         * {@link #equals(Object)} and {@link #hashCode()} consider only the subject, not the action or orphan.
         * @param subject the object whose details these are
         * @param ownerId the ID of the object's owner
         * @param groupId the ID of the object's group
         * @param action the current plan for the object
         * @param orphan the current <q>orphan</q> state of the object
         * @param mayUpdate if the object may be updated
         * @param mayDelete if the object may be deleted
         * @param mayChmod if the object may have its permissions changed
         * @param mayChgrp if the object may be moved
         * @param mayChown if the object may be given
         * @param isOwner if the user owns the object
         * @param isCheckPermissions if the user is expected to have the permissions required to process the object
         */
        Details(IObject subject, Long ownerId, Long groupId, Action action, Orphan orphan,
                boolean mayUpdate, boolean mayDelete, boolean mayChmod, boolean mayChgrp, boolean mayChown,
                boolean isOwner, boolean isCheckPermissions) {
            this.subject = subject;
            this.ownerId = ownerId;
            this.groupId = groupId;
            this.action = action;
            this.orphan = orphan;

            permissions = EnumSet.noneOf(Ability.class);
            if (mayUpdate) {
                permissions.add(Ability.UPDATE);
            }
            if (mayDelete) {
                permissions.add(Ability.DELETE);
            }
            if (mayChmod) {
                permissions.add(Ability.CHMOD);
            }
            if (mayChgrp) {
                permissions.add(Ability.CHGRP);
            }
            if (mayChown) {
                permissions.add(Ability.CHOWN);
            }
            if (isOwner) {
                permissions.add(Ability.OWN);
            }

            this.isCheckPermissions = isCheckPermissions;
        }
    }

    /**
     * The predicates that have been registered with {@link GraphPolicy#registerPredicate(GraphPolicyRulePredicate)}.
     */
    protected final Map<String, GraphPolicyRulePredicate> predicates = new HashMap<String, GraphPolicyRulePredicate>();

    /**
     * Create a clone of this graph policy that has fresh state.
     * @return an instance ready to begin a new graph traversal
     */
    public abstract GraphPolicy getCleanInstance();

    /**
     * Use the given predicate in executing this graph policy.
     * @param predicate a graph policy predicate
     */
    public void registerPredicate(GraphPolicyRulePredicate predicate) {
        predicates.put(predicate.getName(), predicate);
    }

    /**
     * Set a named condition,
     * @param name the name of the condition
     */
    public abstract void setCondition(String name);

    /**
     * Check if a condition has been set.
     * @param name the name of the condition
     * @return if the condition is set
     */
    public abstract boolean isCondition(String name);

    /**
     * Any model object about which policy may be asked is first passed to {@link #noteDetails(Session, IObject, String, long)}
     * before {@link GraphPolicy#review(Map, Details, Map, Set, boolean)}. Each object is passed only once.
     * Subclasses overriding this method probably ought also override {@link #getCleanInstance()}.
     * @param session the Hibernate session, for obtaining more information about the object
     * @param object an unloaded model object about which policy may be asked
     * @param realClass the real class name of the object
     * @param id the ID of the object
     */
    public void noteDetails(Session session, IObject object, String realClass, long id) {
        for (final GraphPolicyRulePredicate predicate : predicates.values()) {
            predicate.noteDetails(session, object, realClass, id);
        }
    }

    /**
     * Utility method to return all the objects for review as a single set of objects.
     * @param linkedFrom details of the objects linking to the root object
     * @param rootObject details of the root objects
     * @param linkedTo details of the objects linked by the root object
     * @return details of all the objects passed as arguments
     */
    public static Set<Details> allObjects(Collection<Set<Details>> linkedFrom, Details rootObject,
            Collection<Set<Details>> linkedTo) {
        final Set<Details> allTerms = new HashSet<Details>();
        allTerms.add(rootObject);
        for (final Set<Details> terms : linkedFrom) {
            allTerms.addAll(terms);
        }
        for (final Set<Details> terms : linkedTo) {
            allTerms.addAll(terms);
        }
        return allTerms;
    }

    /**
     * The action to take about the link between the mapped objects.
     * An {@link Action#EXCLUDE}d object, once changed from that, may not change back to {@link Action#EXCLUDE}.
     * An {@link Action#OUTSIDE} object, once changed to that, may not change back from {@link Action#OUTSIDE}.
     * {@link Orphan} values matter only for {@link Action#EXCLUDE}d objects.
     * Given {@link Orphan#RELEVANT} if {@link Orphan#IS_LAST} or {@link Orphan#IS_NOT_LAST} can be returned,
     * or could be if after {@link Orphan#RELEVANT} is returned then resolved for the other object,
     * then appropriate values should be returned accordingly.
     * If {@link Orphan#RELEVANT} is returned for an object then this method may be called again with
     * {@link Orphan#IS_LAST} or {@link Orphan#IS_NOT_LAST}.
     * Class properties' <code>String</code> representation is <code>package.DeclaringClass.propertyName</code>.
     * @param linkedFrom map from class property to objects for which the property links to the root object
     * @param rootObject the object at the center of this review
     * @param linkedTo map from class property to objects to which the property links from the root object
     * @param notNullable which properties from the linkedFrom and linkedTo map keys are not nullable
     * @param isErrorRules if final checks should be performed instead of normal rule matching
     * @return changes to make, included unchanged details typically cause review as root object
     * @throws GraphException if there was a problem in applying the policy
     */
    public abstract Set<Details> review(Map<String, Set<Details>> linkedFrom, Details rootObject,
            Map<String, Set<Details>> linkedTo, Set<String> notNullable, boolean isErrorRules) throws GraphException;
}
