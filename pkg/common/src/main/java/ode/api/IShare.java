package ode.api;

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

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ode.annotations.NotNull;
import ode.annotations.Validate;
import ode.conditions.ValidationException;
import ode.model.IObject;
import ode.model.annotations.Annotation;
import ode.model.annotations.CommentAnnotation;
import ode.model.internal.Details;
import ode.model.meta.Event;
import ode.model.meta.Experimenter;
import ode.model.meta.Session;
import ode.model.meta.Share;

/**
 * Provides method for sharing - collaboration process for images, datasets,
 * projects.
 */
@Deprecated
public interface IShare extends ServiceInterface {

    /**
     * Turns on the access control lists attached to the given share for the
     * current session. Warning: this will slow down the execution of the
     * current session for all database reads. Writing to the database will not
     * be allowed. If share does not exist or is not accessible (non-members) or
     * is disabled, then an {@link ValidationException} is thrown.
     * 
     * @param shareId
     */
    void activate(long shareId);

    /**
     * Turns off the access control lists with the current share.
     */
    void deactivate();

    // ~ Getting shares and objects (READ)
    // =========================================================================

    /**
     * Returns a map from share id to comment count.  
     * @param shareIds Not null.
     * @return Map with all ids present and 0 if no count exists. 
     * @throws ValidationException if a given share does not exist
     */
    Map<Long, Long> getCommentCount(@NotNull @Validate(Long.class) Set<Long> shareIds);


    /**
     * Gets all owned shares for the current {@link Experimenter}
     * 
     * @param onlyActive
     *            if true, then only shares which can be used for login will be
     *            returned. All "draft" shares (see
     *            {@link #createShare(String, Timestamp, List, List, List, boolean)}
     *            and {@link #closeShare(long) closed shares} will be filtered.
     * @return set of shares. Never null. May be empty.
     */
    Set<Session> getOwnShares(boolean onlyActive);

    /**
     * Gets all shares where current {@link Experimenter} is a member.
     * 
     * @param onlyActive
     *            if true, then only shares which can be used for login will be
     *            returned. All "draft" shares (see
     *            {@link #createShare(String, Timestamp, List, List, List, boolean)}
     *            and {@link #closeShare(long) closed shares} will be filtered.
     * @return set of shares. Never null. May be empty.
     */
    Set<Session> getMemberShares(boolean onlyActive);

    /**
     * Gets all shares owned by the given {@link Experimenter}.
     * 
     * @param onlyActive
     *            if true, then only shares which can be used for login will be
     *            returned. All "draft" shares (see
     *            {@link #createShare(String, Timestamp, List, List, List, boolean)}
     *            and {@link #closeShare(long) closed shares} will be filtered.
     * @return set of shares. Never null. May be empty.
     */
    Set<Session> getSharesOwnedBy(@NotNull Experimenter user, boolean onlyActive);

    /**
     * Gets all shares where given {@link Experimenter} is a member.
     * 
     * @param onlyActive
     *            if true, then only shares which can be used for login will be
     *            returned. All "draft" shares (see
     *            {@link #createShare(String, Timestamp, List, List, List, boolean)}
     *            and {@link #closeShare(long) closed shares} will be filtered.
     * @return set of shares. Never null. May be empty.
     */
    Set<Session> getMemberSharesFor(@NotNull Experimenter user,
            boolean onlyActive);

    /**
     * Gets a share as a {@link Session} with all related:
     * {@link ode.model.annotations.Annotation comments},
     * {@link ode.model.meta.Experimenter members}, fully loaded. Unlike the
     * other methods on this interface, if the sessionId is unknown, does not
     * throw a {@link ValidationException}.
     * 
     * @param sessionId
     * @return a {@link Session} with id and {@link Details} set or null.
     *         The owner in
     *         the Details object is the true owner, and the group in the
     *         Details has all member users linked. {@link Annotation} instances
     *         of the share are linked to the {@link Session}. Missing is a list
     *         of share guests.
     */
    Share getShare(long sessionId);

    /**
     * Looks up all {@link ode.model.IObject items} belong to the
     * {@link ode.model.meta.Session share}.
     * 
     * @param shareId
     * @return list of objects. Not null. Probably not empty.
     */
    <T extends IObject> List<T> getContents(long shareId);

    /**
     * Returns a range of items from the share.
     * 
     * @see #getContents(long)
     */
    <T extends IObject> List<T> getContentSubList(long shareId, int start,
            int finish);

    /**
     * Returns the number of items in the share.
     */
    int getContentSize(long shareId);

    /**
     * Returns the contents of the share keyed by type.
     */
    <T extends IObject> Map<Class<T>, List<Long>> getContentMap(long shareId);

    // ~ Creating share (WRITE)
    // =========================================================================

    /**
     * Creates {@link ode.model.meta.Session share} with all related:
     * {@link ode.model.IObject items}, {@link ode.model.meta.Experimenter
     * members}, and guests.
     * 
     * @param description
     * @param expiration
     * @param exps
     * @param guests
     * @param enabled
     *            if true, then the share is immediately available for use. If
     *            false, then the share is in draft state. All methods on this
     *            interface will work for shares <em>except</em>
     *            {@link #activate(long)}. Similarly, the share password cannot
     *            be used by guests to login.
     */
    <T extends IObject> long createShare(@NotNull String description,
            Timestamp expiration, @Validate(IObject.class) List<T> items,
            @Validate(Experimenter.class) List<Experimenter> exps,
            @Validate(String.class) List<String> guests, boolean enabled);

    void setDescription(long shareId, String description);

    void setExpiration(long shareId, Timestamp expiration);

    void setActive(long shareId, boolean active);

    /**
     * Closes {@link ode.model.meta.Session share}. No further logins will be
     * possible and all getters (e.g. {@link #getMemberShares(boolean)},
     * {@link #getOwnShares(boolean)}, ...) will filter these results if
     * {@code onlyActive} is true.
     * 
     * @param shareId
     */
    void closeShare(long shareId);

    // ~ Getting items
    // =========================================================================

    /**
     * Adds new {@link ode.model.IObject items} to
     * {@link ode.model.meta.Session share}. Conceptually calls
     * {@link #addObjects(long, IObject...)} for every argument passed, but the
     * graphs will be merged.
     * 
     * @param shareId
     * @param objects
     */
    <T extends IObject> void addObjects(long shareId, @NotNull T... objects);

    /**
     * Adds new {@link ode.model.IObject item} to {@link ode.model.meta.Session
     * share}. The entire object graph with the exception of all Details will
     * be loaded into the share. If you would like to load a single object,
     * then pass an unloaded reference.
     * 
     * @param shareId
     * @param object
     */
    <T extends IObject> void addObject(long shareId, @NotNull T object);

    /**
     * Remove existing items from the share.
     * 
     * @param shareId
     * @param objects
     */
    <T extends IObject> void removeObjects(long shareId, @NotNull T... objects);

    /**
     * Removes existing {@link ode.model.IObject object} from the
     * {@link ode.model.meta.Session share}.
     * 
     * @param shareId
     * @param object
     */
    <T extends IObject> void removeObject(long shareId, @NotNull T object);

    // ~ Getting comments
    // =========================================================================

    /**
     * Looks up all {@link ode.model.annotations.Annotation comments} which
     * belong to the {@link ode.model.meta.Session share}.
     * 
     * @param shareId
     * @return list of Annotation
     */
    List<Annotation> getComments(long shareId);

    /**
     * Returns a map from share id to the count of total members (including the
     * owner). This is represented by {@link ode.model.meta.ShareMember} links.
     *
     * @param shareIds Not null.
     * @return Map with all ids present.
     * @throws ValidationException if a given share does not exist
     */
    Map<Long, Long> getMemberCount(@NotNull @Validate(Long.class) Set<Long> shareIds);

    /**
     * Creates {@link ode.model.annotations.TextAnnotation comment} for
     * {@link ode.model.meta.Session share}.
     * 
     * @param shareId
     * @param comment
     */
    CommentAnnotation addComment(long shareId, @NotNull String comment);

    /**
     * Creates {@link CommentAnnotation comment} which replies to an existing
     * comment.
     * 
     * @param shareId
     * @param comment
     * @param replyTo
     * @return the new {@link CommentAnnotation}
     */
    CommentAnnotation addReply(long shareId, @NotNull String comment,
            @NotNull CommentAnnotation replyTo);

    /**
     * Deletes {@link ode.model.annotations.Annotation comment} from the
     * database.
     * 
     * @param comment
     */
    void deleteComment(@NotNull Annotation comment);

    // ~ Member administration
    // =========================================================================

    /**
     * Get all {@link Experimenter users} who are a member of the share.
     */
    Set<Experimenter> getAllMembers(long shareId);

    /**
     * Get the email addresses for all share guests.
     */
    Set<String> getAllGuests(long shareId);

    /**
     * Get a single set containing the
     * {@link Experimenter#getOdeName() login names}
     * of the users as well email addresses for guests.
     * 
     * @param shareId
     * @return a {@link Set} containing the login of all users
     * @throws ValidationException
     *             if there is a conflict between email addresses and user
     *             names.
     */
    Set<String> getAllUsers(long shareId) throws ValidationException;

    /**
     * Adds {@link ode.model.meta.Experimenter experimenters} to
     * {@link ode.model.meta.Session share}
     * 
     * @param shareId
     * @param exps
     */
    void addUsers(long shareId, Experimenter... exps);

    /**
     * Adds guest email addresses to the share.
     * 
     * @param shareId
     * @param emailAddresses
     */
    void addGuests(long shareId, String... emailAddresses);

    /**
     * Removes {@link ode.model.meta.Experimenter experimenters} from
     * {@link ode.model.meta.Session share}
     * 
     * @param shareId
     * @param exps
     */
    void removeUsers(long shareId,
            @Validate(Experimenter.class) List<Experimenter> exps);

    /**
     * Removes guest email addresses from the share.
     * 
     * @param shareId
     * @param emailAddresses
     */
    void removeGuests(long shareId, String... emailAddresses);

    /**
     * Adds {@link ode.model.meta.Experimenter experimenter} to
     * {@link ode.model.meta.Session share}
     * 
     * @param shareId
     * @param exp
     */
    void addUser(long shareId, Experimenter exp);

    /**
     * Add guest email address to the share.
     * 
     * @param shareId
     * @param emailAddress
     */
    void addGuest(long shareId, String emailAddress);

    /**
     * Removes {@link ode.model.meta.Experimenter experimenter} from
     * {@link ode.model.meta.Session share}
     * 
     * @param shareId
     * @param exp
     */
    void removeUser(long shareId, Experimenter exp);

    /**
     * Removes guest email address from share.
     * 
     * @param shareId
     * @param emailAddress
     */
    void removeGuest(long shareId, String emailAddress);

    // ~ Event administration
    // =========================================================================

    /**
     * Gets actual active connections to {@link ode.model.meta.Session share}.
     * 
     * @param shareId
     * @return map of experimenter and IP address
     */
    Map<String, Experimenter> getActiveConnections(@NotNull long shareId);

    /**
     * Gets previous connections to {@link ode.model.meta.Session share}.
     * 
     * @param shareId
     * @return map of experimenter and IP address
     */
    Map<String, Experimenter> getPastConnections(@NotNull long shareId);

    /**
     * Makes the connection invalid for {@link ode.model.meta.Session share} for
     * specifiec user.
     * 
     * @param shareId
     * @param exp
     *            - connection
     */
    void invalidateConnection(@NotNull long shareId, Experimenter exp);

    /**
     * Gets events for {@link ode.model.meta.Session share} per
     * {@link ode.model.meta.Experimenter experimenter} for period of time.
     * 
     * @param shareId
     * @param experimenter
     * @param from
     *            - time
     * @param to
     *            - time
     * @return List of events
     */
    List<Event> getEvents(@NotNull long shareId, Experimenter experimenter,
            Timestamp from, Timestamp to);

    /**
     * Notify via email selected members of share.
     * 
     * @param shareId
     * @param subject
     * @param message
     * @param html
     */
    void notifyMembersOfShare(@NotNull long shareId, @NotNull String subject,
            @NotNull String message, boolean html);

}