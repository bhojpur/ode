package ode.security;

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

// Java imports

// Third-party libraries

// Application-internal dependencies
import ode.system.EventContext;
import ode.conditions.ApiUsageException;
import ode.conditions.SecurityViolation;
import ode.model.IObject;
import ode.model.internal.Details;
import ode.model.internal.Permissions;
import ode.model.internal.Token;
import ode.model.meta.ExperimenterGroup;
import ode.security.policy.Policy;
import ode.system.Principal;
import ode.system.Roles;

/**
 * central security interface. All queries and actions that deal with a secure
 * context should pass through an implementation of this interface.
 */
public interface SecuritySystem {

    // ~ Login/logout
    // =========================================================================

    /**
     * stores this {@link Principal} instance in the current thread context for
     * authenticating and authorizing all actions. This method does <em>not</em>
     * make any queries and is only a conduit for login information from the
     * outermost levels. Session bean implementations and other in-JVM clients
     * can fill the {@link Principal}. Note, however, a call must first be made
     * to {@link #loadEventContext(boolean)} for some calls to be made to the
     * {@link SecuritySystem}. In general, this means that execution must pass
     * through the {@link ode.security.basic.EventHandler}
     *
     * @param principal the new current principal
     */
    void login(Principal principal);

    /**
     * clears the top {@link Principal} instance from the current thread
     * context.
     * 
     * @return the number of remaining instances.
     */
    int logout();

    /**
     * Calls {@link #getEventContext(boolean)} with a false as "refresh".
     * This is the previous, safer logic of the method since consumers
     * are not expecting a long method run.
     *
     * @return the event context
     */
    EventContext getEventContext();

    /**
     * Returns UID based on whether a share is active, etc. This is the UID
     * value that should be used for writing data.
     *
     * The return value <em>may be</em> null if the user is currently querying
     * across multiple contents. In this case another method for
     * choosing the UID must be chosen, for example by taking the UID of
     * another element under consideration.
     *
     * For example,
     * <pre>
     * Annotation toSave = ...;
     * if (toSave.getDetails().getOwner() == null) // No owner need to find one.
     * {
     *     Long uid = sec.getEffectiveUID();
     *     if (uid != null)
     *     {
     *         toSave.getDetails().setOwner(new Experimenter(uid, false));
     *     }
     *     else
     *     {
     *         toSave.getDetails().setOwner(
     *            image.getDetails().getOwner()); // may be null.
     *     }
     * }
     * image.linkAnnotation(toSave);
     * etc.
     * </pre>
     * 
     * @return the effective user ID
     */
    Long getEffectiveUID();

    /**
     * If refresh is false, returns the current {@link EventContext} stored
     * in the session. Otherwise, reloads the context to have the most
     * up-to-date information.
     *
     * @param refresh if the event context should first be reloaded
     * @return the event context
     */
    EventContext getEventContext(boolean refresh);

    /**
     * Prepares the current {@link EventContext} instance with the current
     * {@link Principal}. An exception is thrown if there is none.
     * 
     * @param isReadOnly
     */
    void loadEventContext(boolean isReadOnly);

    /**
     * Clears the content of the {@link EventContext}so that the
     * {@link SecuritySystem} will no longer return true for {@link #isReady()}.
     * The {@link Principal} set during {@link #login(Principal)} is retained.
     */
    void invalidateEventContext();

    // ~ Checks
    // =========================================================================
    /**
     * checks if this {@link SecuritySystem} instance is in a valid state. This
     * includes that a user is properly logged in and that a connection is
     * available to all necessary resources, e.g. database handle and mapping
     * session.
     * 
     * Not all methods require that the instance is ready.
     * 
     * @return true if all methods on this interface are ready to be called.
     */
    boolean isReady();

    /**
     * checks if instances of the given type are "System-Types". Security logic
     * for all system types is significantly different. In general, system types
     * cannot be created, updated, or deleted by regular users, and are visible
     * to all users.
     * 
     * @param klass
     *            A class which extends from {@link IObject}
     * @return true if instances of the class argument can be considered system
     *         types.
     */
    boolean isSystemType(Class<? extends IObject> klass);

    /**
     * checks that the {@link IObject} argument has been granted a {@link Token}
     * by the {@link SecuritySystem}.
     */
    boolean hasPrivilegedToken(IObject obj);

    /**
     * Checks whether or not a {@link Policy} instance of matching
     * name has been registered, considers itself active, <em>and</em>
     * considers the passed context object to be restricted.
     * 
     * @param name A non-null unique name for a class of policies.
     * @param obj An instance which is to be checked against matching policies.
     * @throws {@link SecurityViolation} if the given {@link Policy} is
     *      considered to be restricted.
     */
    void checkRestriction(String name, IObject obj) throws SecurityViolation;

    // ~ Subsystem disabling
    // =========================================================================

    /**
     * disables components of the backend for the current Thread. Further checks
     * to {@link #isDisabled(String)} will return false. It is the
     * responsibility of various security system components to then throw
     * exceptions.
     * 
     * @param ids
     *            Non-null, non-empty array of String ids to disable.
     */
    void disable(String... ids);

    /**
     * enables components of the backend for the current Thread. Further checks
     * to {@link #isDisabled(String)} will return true.
     * 
     * @param ids
     *            possibly null array of String ids. A null array specifies that
     *            all subsystems are to be enabled. Otherwise, only those
     *            subsystems specified by the ids.
     */
    void enable(String... ids);

    /**
     * checks if the listed id is disabled for the current Thread.
     * 
     * @param id
     *            non-null String representing a backend subsystem.
     * @return true if the backend subsystem has been previously disabled by
     *         calls to {@link #disable(String[])}
     */
    boolean isDisabled(String id);

    // ~ Details checking (prime responsibility)
    // =========================================================================

    /**
     * Determines if the current security context has the possibility of
     * corrupting consistent graphs. Consistent graphs are enforced by the
     * security context to make sure that all READ actions work smoothly. If an
     * administrator or PI is logged into a private group, or otherwise may
     * create an object linked to an object with lower READ rights, then
     * corruption could occur.
     *
     * Starting with 4.4.2, a trusted details object should be passed in order
     * to handle the situation where the current group id is -1. Possibles
     * cases that can occur:
     *
     * <pre>
     *  The current group is non-negative, then use the previous logic;
     *  else the current group is negative,
     *     and the object is in a non-"user" group: USE THAT GROUP;
     *     else the object is in the "user" group: UNCLEAR
     *     (for the modent we're throwing an exception)
     * </pre>
     *
     * If no {@link Details} instance is passed or a {@link Details} without
     * a {@link ExperimenterGroup} value, then throw as well.
     * @param details the details
     * @return if the graph is critical
     */
    boolean isGraphCritical(Details details);

    /**
     * creates a new secure {@link IObject#getDetails() details} for transient
     * entities. Non-privileged users can only edit the
     * {@link Details#getPermissions() Permissions} field. Privileged users can
     * use the {@link Details} object as a single-step <code>chmod</code> and
     * <code>chgrp</code>.
     * 
     * {@link #newTransientDetails(IObject) newTransientDetails} always returns
     * a non-null Details that is not equivalent (==) to the Details argument.
     * 
     * This method can be used from anywhere in the codebase to obtain a valid
     * {@link Details}, but passing in an {@link IObject} instance with a null
     * {@link Details}. However, if the {@link Details} is non-null, there is
     * the possibility that this method will throw an exception.
     * 
     * @throws ApiUsageException
     *             if {@link SecuritySystem} is not {@link #isReady() ready}
     * @throws SecurityViolation
     *             if {@link Details} instance contains illegal values.
     */
    Details newTransientDetails(IObject iObject) throws ApiUsageException,
            SecurityViolation;

    /**
     * checks that a non-privileged user has not attempted to edit the entity's
     * {@link IObject#getDetails() security details}. Privileged users can set
     * fields on {@link Details} as a single-step <code>chmod</code> and
     * <code>chgrp</code>.
     * 
     * {@link #checkManagedDetails(IObject, Details) managedDetails} may create
     * a new Details instance and return that if needed. If the returned Details
     * is not equivalent (==) to the argument Details, then values have been
     * changed.
     * 
     * @param iObject
     *            non-null {@link IObject} instance. {@link Details} for that
     *            instance can be null.
     * @param trustedDetails
     *            possibly null {@link Details} instance. These {@link Details}
     *            are trusted in the sense that they have already once passed
     *            through the {@link SecuritySystem}.
     * @throws ApiUsageException
     *             if {@link SecuritySystem} is not {@link #isReady() ready}
     * @throws SecurityViolation
     *             if {@link Details} instance contains illegal values.
     */
    Details checkManagedDetails(IObject iObject, Details trustedDetails)
            throws ApiUsageException, SecurityViolation;

    // ~ Actions
    // =========================================================================
    /**
     * Allows actions to be performed with the
     * {@link EventContext#isCurrentUserAdmin()} flag enabled but
     * <em>without</em> changing the value of
     * {@link EventContext#getCurrentUserId()}, so that ownerships are properly
     * handled. The merging of detached entity graphs should be disabled for the
     * extent of the execution.
     * 
     * Due to the addition of the group permission system, we also permit
     * setting the group on the call so that the administrator can work within
     * all groups. A value of null will not change the current group.
     *
     * Note: the {@link ode.api.IUpdate} save methods should not be used, since
     * they also accept detached entities, which could pose security risks.
     * Instead load an entity from the database via {@link ode.api.IQuery},
     * make changes, and save the changes with {@link ode.api.IUpdate}.
     *
     * @param group the group to run the action as
     * @param action the action to run
     */
    void runAsAdmin(ExperimenterGroup group, AdminAction action);

    /**
     * Calls {@link #runAsAdmin(ExperimenterGroup, AdminAction)} with a
     * null group.
     *
     * @param action the action to run
     */
    void runAsAdmin(AdminAction action);

    <T extends IObject> T doAction(SecureAction action, T... objs);

    // TODO do these need checks to isReady()?

    // ~ Configured Elements
    // =========================================================================
    Roles getSecurityRoles();

}