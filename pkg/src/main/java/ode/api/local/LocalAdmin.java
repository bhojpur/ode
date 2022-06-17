package ode.api.local;

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

import java.util.List;
import java.util.Map;

import ode.model.IObject;
import ode.model.internal.Details;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.system.EventContext;

/**
 * Provides local (internal) extensions for administration
 */
public interface LocalAdmin extends ode.api.IAdmin {

    /**
     * returns a possibly uninitialized proxy for the given
     * {@link Experimenter#getOdeName() user name}. Use of the
     * {@link Experimenter} instance will initialize its values.
     * @param odeName the name of a user
     * @return the user (may be uninitialized)
     */
    Experimenter userProxy(String odeName);

    /**
     * returns a possibly uninitialized proxy for the given
     * {@link Experimenter#getId() user id}. Use of the {@link Experimenter}
     * instance will initialize its values.
     * @param userId the ID of a user
     * @return the user (may be uninitialized)
     */
    Experimenter userProxy(Long userId);

    /**
     * returns a possibly uninitialized proxy for the given
     * {@link ExperimenterGroup#getId() group id}. Use of the
     * {@link Experimenter} instance will initialize its values.
     * @param groupId the ID of a group
     * @return the group (may be uninitialized)
     */
    ExperimenterGroup groupProxy(Long groupId);

    /**
     * returns a possibly uninitialized proxy for the given
     * {@link ExperimenterGroup#getName() group name}. Use of the
     * {@link Experimenter} instance will initialize its values.
     * @param groupName the name of a group
     * @return the group (may be uninitialized)
     */
    ExperimenterGroup groupProxy(String groupName);

    /**
     * Finds the group names for all groups for which the given {@link Experimenter} is
     * a member.
     * 
     * @param e
     *            Non-null, managed (i.e. with id) {@link Experimenter}
     * @return the groups of which the user is a member
     * @see ExperimenterGroup#getDetails()
     * @see Details#getOwner()
     */
    List<String> getUserRoles(Experimenter e);
    
    /**
     * Checks password for given user. ReadOnly determines if a actions can be
     * taken to create the given user, for example in the case of LDAP.
     * @param user the name of a user
     * @param password the user's password
     * @param readOnly if the password check should be transactionally read-only
     * @return if the user's password is correct
     */
    boolean checkPassword(String user, String password, boolean readOnly);

    //TODO The following method will eventually return a list of ids
    /**
     * Returns a map from {@link Class} (as string) to a count for all entities
     * which point to the given {@link IObject}. The String "*" is mapped to
     * the sum of all the locks.
     *
     * @param klass the name of a model class
     * @param id the ID of an instance of {@code klass}
     * @param groupId the ID of a group to omit from the results, may be {@code null}
     * @return the classes and counts of the objects that point to the given object
     */
    Map<String, Long> getLockingIds(Class<IObject> klass, long id, Long groupId);

    /**
     * Like {@link #getEventContext()} but will not reload the context.
     * This also has the result that values from the current call context
     * will be applied as simply the session context.
     * @return the current event context
     */
    EventContext getEventContextQuiet();

    /**
     * Companion to {@link ode.api.IAdmin#canUpdate(IObject)} but not yet remotely
     * accessible.
     * @param obj Not null.
     * @return if the object can be annotated
     */
    boolean canAnnotate(IObject obj);

    /**
     * Unconditionally move an object into the user group (usually id=1).
     * Here, it will be readable from any group context.
     */
    void internalMoveToCommonSpace(IObject obj);
}