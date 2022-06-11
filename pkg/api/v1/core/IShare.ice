/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_CORE_ISHARE_ICE
#define ODE_CORE_ISHARE_ICE

#include <ode/ModelF.ice>
#include <ode/ServicesF.ice>
#include <ode/Collections.ice>
["deprecate:IShare is deprecated."]
module ode {

    module core {

        /**
         * Provides method for sharing - collaboration process for images,
         * datasets, projects.
         **/
        ["ami", "amd"] interface IShare extends ServiceInterface
            {
                /**
                 * Turns on the access control lists attached to the given
                 * share for the current session. Warning: this will slow down
                 * the execution of the current session for all database
                 * reads. Writing to the database will not be allowed. If
                 * share does not exist or is not accessible (non-members) or
                 * is disabled, then an {@link ode.ValidationException} is thrown.
                 */
                idempotent void activate(long shareId) throws ServerError;

                /**
                 * Turns off the access control lists with the current share.
                 */
                idempotent void deactivate() throws ServerError;

                /**
                 * Gets a share as a {@link ode.model.Session} with all
                 * related: {@link ode.model.Annotation} comments,
                 * {@link ode.model.Experimenter} members, fully loaded.
                 * Unlike the other methods on this interface, if the
                 * sessionId is unknown, does not throw a
                 * {@link ode.ValidationException}.
                 *
                 * @return a {@link ode.model.Session} with id and
                 *         {@link ode.model.Details} set or null.
                 *         The owner in the Details object is the true owner,
                 *         and the group in the Details has all member users
                 *         linked. {@link ode.model.Annotation} instances
                 *         of the share are linked to the
                 *         {@link ode.model.Session}. Missing is a list of
                 *         share guests.
                 */
                idempotent ode::model::Share getShare(long shareId) throws ServerError;

                /**
                 * Returns a map from share id to the count of total members
                 * (including the owner). This is represented by
                 * {@link ode.model.ShareMember} links.
                 *
                 * @param shareIds Not null.
                 * @return Map with all ids present.
                 * @throws ValidationException if a given share does not exist
                 */
                idempotent ode::sys::CountMap getMemberCount(ode::sys::LongList shareIds) throws ServerError;

                /**
                 * Gets all owned shares for the current
                 * {@link ode.model.Experimenter}.
                 *
                 * @param active
                 *            if true, then only shares which can be used for
                 *            login will be returned. All <i>draft</i> shares
                 *            (see {@code createShare}) and closed shares (see
                 *            {@code closeShare}) will be filtered.
                 * @return set of shares. Never null. May be empty.
                 */
                idempotent SessionList getOwnShares(bool active) throws ServerError;

                /**
                 * Gets all shares where current
                 * {@link ode.model.Experimenter} is a member.
                 *
                 * @param active
                 *            if true, then only shares which can be used for
                 *            login will be returned. All <i>draft</i> shares
                 *            (see {@code createShare}) and closed shares (see
                 *            {@code closeShare}) will be filtered.
                 * @return set of shares. Never null. May be empty.
                 */
                idempotent SessionList getMemberShares(bool active) throws ServerError;

                /**
                 * Gets all shares owned by the given
                 * {@link ode.model.Experimenter}.
                 *
                 * @param user
                 *            the experimenter
                 * @param active
                 *            if true, then only shares which can be used for
                 *            login will be returned. All <i>draft</i> shares
                 *            (see {@code createShare}) and closed shares (see
                 *            {@code closeShare}) will be filtered.
                 * @return set of shares. Never null. May be empty.
                 */
                idempotent SessionList getSharesOwnedBy(ode::model::Experimenter user, bool active) throws ServerError;

                /**
                 * Gets all shares where given
                 * {@link ode.model.Experimenter} is a member.
                 *
                 * @param user
                 *            the experimenter
                 * @param active
                 *            if true, then only shares which can be used for
                 *            login will be returned. All <i>draft</i> shares
                 *            (see {@code createShare}) and closed shares (see
                 *            {@code closeShare}) will be filtered.
                 * @return set of shares. Never null. May be empty.
                 */
                idempotent SessionList getMemberSharesFor(ode::model::Experimenter user, bool active) throws ServerError;

                /**
                 * Looks up all {@link ode.model.IObject} items belong to the
                 * {@link ode.model.Session} share.
                 *
                 * @return list of objects. Not null. Probably not empty.
                 */
                idempotent IObjectList getContents(long shareId) throws ServerError;

                /**
                 * Returns a range of items from the share.
                 *
                 * @see #getContents
                 */
                idempotent IObjectList getContentSubList(long shareId, int start, int finish) throws ServerError;

                /**
                 * Returns the number of items in the share.
                 */
                idempotent int getContentSize(long shareId) throws ServerError;

                /**
                 * Returns the contents of the share keyed by type.
                 */
                idempotent IdListMap getContentMap(long shareId) throws ServerError;

                /**
                 * Creates {@link ode.model.Session} share with all related:
                 * {@link ode.model.IObject} itmes,
                 * {@link ode.model.Experimenter} members, and guests.
                 *
                 * @param enabled
                 *            if true, then the share is immediately available
                 *            for use. If false, then the share is in draft
                 *            state. All methods on this interface will work
                 *            for shares <em>except</em> {@code activate}.
                 *            Similarly, the share password cannot be used by
                 *            guests to login.
                 */
                long createShare(string description,
                                 ode::RTime expiration,
                                 IObjectList items,
                                 ExperimenterList exps,
                                 StringSet guests,
                                 bool enabled) throws ServerError;
                idempotent void setDescription(long shareId, string description) throws ServerError;
                idempotent void setExpiration(long shareId, ode::RTime expiration) throws ServerError;
                idempotent void setActive(long shareId, bool active) throws ServerError;

                /**
                 * Closes {@link ode.model.Session} share. No further logins
                 * will be possible and all getters (e.g.
                 * {@code getMemberShares}, {@code getOwnShares}, ...) will
                 * filter these results if {@code onlyActive} is true.
                 */
                void closeShare(long shareId) throws ServerError;

                /**
                 * Adds new {@link ode.model.IObject} items to
                 * {@link ode.model.Session} share. Conceptually calls
                 * {@code addObjects} for every argument passed, but the
                 * graphs will be merged.
                 */
                void addObjects(long shareId, IObjectList iobjects) throws ServerError;

                /**
                 * Adds new {@link ode.model.IObject} item to
                 * {@link ode.model.Session} share. The entire object graph
                 * with the exception of all Details will be loaded into the
                 * share. If you would like to load a single object, then pass
                 * an unloaded reference.
                 */
                void addObject(long shareId, ode::model::IObject iobject) throws ServerError;

                /**
                 * Remove existing items from the share.
                 */
                void removeObjects(long shareId, IObjectList iobjects) throws ServerError;

                /**
                 * Removes existing {@link ode.model.IObject} object from the
                 * {@link ode.model.Session} share.
                 */
                void removeObject(long shareId, ode::model::IObject iobject) throws ServerError;

                /**
                 * Returns a map from share id to comment count.
                 *
                 * @param shareIds Not null.
                 * @return Map with all ids present and 0 if no count exists.
                 * @throws ValidationException if a given share does not exist
                 */
                idempotent ode::sys::CountMap getCommentCount(ode::sys::LongList shareIds) throws ServerError;

                /**
                 * Looks up all {@link ode.model.Annotation} comments which
                 * belong to the {@link ode.model.Session} share.
                 *
                 * @return list of Annotation
                 */
                idempotent AnnotationList getComments(long shareId) throws ServerError;

                /**
                 * Creates {@link ode.model.TextAnnotation} comment for
                 * {@link ode.model.Session} share.
                 */
                ode::model::TextAnnotation addComment(long shareId, string comment) throws ServerError;

                /**
                 * Creates {@link ode.model.TextAnnotation} comment which
                 * replies to an existing comment.
                 *
                 * @return the new {@link ode.model.TextAnnotation}
                 */
                ode::model::TextAnnotation addReply(long shareId,
                                                      string comment,
                                                      ode::model::TextAnnotation replyTo) throws ServerError;

                /**
                 * Deletes {@link ode.model.Annotation} comment from the
                 * database.
                 */
                void deleteComment(ode::model::Annotation comment) throws ServerError;

                /**
                 * Get all {@link ode.model.Experimenter} users who are a
                 * member of the share.
                 */
                idempotent ExperimenterList getAllMembers(long shareId) throws ServerError;

                /**
                 * Get the email addresses for all share guests.
                 */
                idempotent StringSet getAllGuests(long shareId) throws ServerError;

                /**
                 * Get a single set containing the
                 * {@code ode.model.Experimenter.getOdeName} login names
                 * of the users as well email addresses for guests.
                 *
                 * @param shareId
                 * @return a {@link java.util.Set} containing the login of all
                 *         users
                 * @throws ValidationException
                 *         if there is a conflict between email addresses and
                 *         user names.
                 */
                idempotent StringSet getAllUsers(long shareId) throws ValidationException, ServerError;

                /**
                 * Adds {@link ode.model.Experimenter} experimenters to
                 * {@link ode.model.Session} share.
                 */
                void addUsers(long shareId, ExperimenterList exps) throws ServerError;

                /**
                 * Adds guest email addresses to the share.
                 */
                void addGuests(long shareId, StringSet emailAddresses) throws ServerError;

                /**
                 * Removes {@link ode.model.Experimenter} experimenters from
                 * {@link ode.model.Session} share.
                 */
                void removeUsers(long shareId, ExperimenterList exps) throws ServerError;

                /**
                 * Removes guest email addresses from the share.
                 */
                void removeGuests(long shareId, StringSet emailAddresses) throws ServerError;

                /**
                 * Adds {@link ode.model.Experimenter} experimenter to
                 * {@link ode.model.Session} share.
                 */
                void addUser(long shareId, ode::model::Experimenter exp) throws ServerError;

                /**
                 * Adds guest email address to the share.
                 */
                void addGuest(long shareId, string emailAddress) throws ServerError;

                /**
                 * Removes {@link ode.model.Experimenter} experimenter from
                 * {@link ode.model.Session} share.
                 */
                void removeUser(long shareId, ode::model::Experimenter exp) throws ServerError;

                /**
                 * Removes guest email address from share.
                 */
                void removeGuest(long shareId, string emailAddress) throws ServerError;

                // Under construction
                /**
                 * Gets actual active connections to
                 * {@link ode.model.Session} share.
                 *
                 * @return map of experimenter and IP address
                 */
                idempotent UserMap getActiveConnections(long shareId) throws ServerError;

                /**
                 * Gets previous connections to
                 * {@link ode.model.Session} share.
                 *
                 * @return map of experimenter and IP address
                 */
                idempotent UserMap getPastConnections(long shareId) throws ServerError;

                /**
                 * Makes the connection invalid for
                 * {@link ode.model.Session} share for specified user.
                 */
                void invalidateConnection(long shareId, ode::model::Experimenter exp) throws ServerError;

                /**
                 * Gets events for {@link ode.model.Session} share per
                 * {@link ode.model.Experimenter} experimenter for period of
                 * time.
                 * @return List of events
                 */
                idempotent IObjectList getEvents(long shareId, ode::model::Experimenter exp, ode::RTime from, ode::RTime to) throws ServerError;

                /**
                 * Notifies via email selected members of share.
                 */
                void notifyMembersOfShare(long shareId, string subject, string message, bool html) throws ServerError;
            };

    };
};

#endif