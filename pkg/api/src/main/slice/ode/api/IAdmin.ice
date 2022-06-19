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

#ifndef ODE_API_IADMIN_ICE
#define ODE_API_IADMIN_ICE

#include <ode/ServicesF.ice>
#include <ode/System.ice>
#include <ode/Collections.ice>
#include <ode/model/AdminPrivilege.ice>

module ode {

    module api {

        /* would normally be among Collections' object lists but that does not have ODE-only enumerations in scope */
        ["java:type:java.util.ArrayList<ode.model.AdminPrivilege>:java.util.List<ode.model.AdminPrivilege>"]
        sequence<ode::model::AdminPrivilege> AdminPrivilegeList;

        /**
         * Administration interface providing access to admin-only
         * functionality as well as JMX-based server access and selected user
         * functions. Most methods require membership in privileged
         * {@link ode.model.ExperimenterGroup groups}.
         *
         * Methods which return {@link ode.model.Experimenter} or
         * {@link ode.model.ExperimenterGroup} instances fetch and load all
         * related instances of {@link ode.model.ExperimenterGroup} or
         * {@link ode.model.Experimenter}, respectively.
         *
         **/
        ["ami", "amd"] interface IAdmin extends ServiceInterface
            {

                // Getters
                /**
                 * Returns true if the currently logged in user can modify the
                 * given {@link ode.model.IObject}. This uses the same logic
                 * that would be applied during a Hibernate flush to the
                 * database.
                 */
                idempotent bool canUpdate(ode::model::IObject obj) throws ServerError;

                /**
                 * Fetches an {@link ode.model.Experimenter} and all related
                 * {@link ode.model.ExperimenterGroup}.
                 *
                 * @param id id of the Experimenter
                 * @return an Experimenter. Never null.
                 * @throws ApiUsageException if id does not exist.
                 */
                idempotent ode::model::Experimenter getExperimenter(long id) throws ServerError;

                /**
                 * Looks up an {@link ode.model.Experimenter} and all related
                 * {@link ode.model.ExperimenterGroup} by name.
                 *
                 * @param name Name of the Experimenter
                 * @return an Experimenter. Never null.
                 * @throws ApiUsageException if odeName does not exist.
                 */
                idempotent ode::model::Experimenter lookupExperimenter(string name) throws ServerError;

                /**
                 * Looks up all {@link ode.model.Experimenter} experimenters
                 * present and all related
                 * {@link ode.model.ExperimenterGroup} groups.
                 *
                 * @return all Experimenters. Never null.
                 */
                idempotent ExperimenterList lookupExperimenters() throws ServerError;
                /**
                 * Fetches an {@link ode.model.ExperimenterGroup} and all
                 * contained {@link ode.model.Experimenter} users.
                 *
                 * @param id id of the ExperimenterGroup
                 * @return an ExperimenterGroup. Never null.
                 * @throws ApiUsageException if id does not exist.
                 */
                idempotent ode::model::ExperimenterGroup getGroup(long id) throws ServerError;

                /**
                 * Looks up an {@link ode.model.ExperimenterGroup} and all
                 * contained {@link ode.model.Experimenter} users by name.
                 *
                 * @param name Name of the ExperimenterGroup
                 * @return an ExperimenterGroup. Never null.
                 * @throws ApiUsageException if groupName does not exist.
                 */
                idempotent ode::model::ExperimenterGroup lookupGroup(string name) throws ServerError ;

                /**
                 * Looks up all {@link ode.model.ExperimenterGroup} groups
                 * present and all related
                 * {@link ode.model.Experimenter} experimenters. The
                 * experimenter's groups are also loaded.
                 *
                 * @return all Groups. Never null.
                 */
                idempotent ExperimenterGroupList lookupGroups() throws ServerError;

                /**
                 * Fetches all {@link ode.model.Experimenter} users
                 * contained in this group. The returned users will have all
                 * fields filled in and all collections unloaded.
                 *
                 * @param groupId id of the ExperimenterGroup
                 * @return non-null array of all
                 * {@link ode.model.Experimenter} users in this group.
                 */
                idempotent ExperimenterList containedExperimenters(long groupId) throws ServerError;

                /**
                 * Fetches all {@link ode.model.ExperimenterGroup} groups of
                 * which the given user is a member. The returned groups will
                 * have all fields filled in and all collections unloaded.
                 *
                 * @param experimenterId id of the Experimenter. Not null.
                 * @return non-null array of all
                 * {@link ode.model.ExperimenterGroup} groups for this user.
                 */
                idempotent ExperimenterGroupList containedGroups(long experimenterId) throws ServerError;

                /**
                 * Retrieves the default {@link ode.model.ExperimenterGroup}
                 * group for the given user id.
                 *
                 * @param experimenterId of the Experimenter. Not null.
                 * @return non-null {@link ode.model.ExperimenterGroup}. If
                 *         no default group is found, an exception will be
                 *         thrown.
                 */
                idempotent ode::model::ExperimenterGroup getDefaultGroup(long experimenterId) throws ServerError;

                /**
                 * Looks up {@link ode.model.Experimenter} experimenters who
                 * use LDAP authentication  (has set dn on password table).
                 *
                 * @param id id of the Experimenter. Not null.
                 * @return Experimenter. Never null.
                 */
                idempotent string lookupLdapAuthExperimenter(long id) throws ServerError;

                /**
                 * Looks up all ids of {@link ode.model.Experimenter}
                 * experimenters who use LDAP authentication (has set dn on
                 * password table).
                 *
                 * @return list of experimenters. Never null.
                 */
                idempotent RList lookupLdapAuthExperimenters() throws ServerError;

                /**
                 * Finds the ids for all groups for which the given
                 * {@link ode.model.Experimenter} is a member.
                 *
                 * @param exp Non-null, managed (i.e. with id)
                 * @see ode.model.Details#getOwner
                 */
                idempotent LongList getMemberOfGroupIds(ode::model::Experimenter exp) throws ServerError;

                /**
                 * Finds the ids for all groups for which the given
                 * {@link ode.model.Experimenter} is owner/leader.
                 *
                 * @param exp Non-null, managed (i.e. with id)
                 * @see ode.model.Details#getOwner
                 */
                idempotent LongList getLeaderOfGroupIds(ode::model::Experimenter exp) throws ServerError;

                /**
                 * Gets the light administrator privileges for the current user.
                 *
                 * @return the current user's light administrator privileges
                 */
                idempotent AdminPrivilegeList getCurrentAdminPrivileges() throws ServerError;

                /**
                 * Gets the light administrator privileges for the given user.
                 *
                 * @param user the user whose privileges are being queried
                 * @return the user's light administrator privileges
                 */
                idempotent AdminPrivilegeList getAdminPrivileges(ode::model::Experimenter user) throws ServerError;

                /**
                 * Gets the administrators who have all the given privileges.
                 * Consistent with the results from "getAdminPrivileges".
                 *
                 * @param privileges the required privileges
                 * @return the light administrators who have those privileges
                 */
                idempotent ExperimenterList getAdminsWithPrivileges(AdminPrivilegeList privileges) throws ServerError;

                // Mutators

                /**
                 * Allows a user to update his/her own information. This is
                 * limited to the fields on Experimenter, all other fields
                 * (groups, etc.) are ignored. The experimenter argument need
                 * not have the proper id nor the proper odeName (which is
                 * immutable). To change the users default group (which is the
                 * only other customizable option), use
                 * {@code setDefaultGroup}
                 *
                 * @see #setDefaultGroup
                 */
                void updateSelf(ode::model::Experimenter experimenter) throws ServerError;

                /**
                 * Uploads a photo for the user which will be displayed on
                 * his/her profile.
                 * This photo will be saved as an
                 * {@link ode.model.OriginalFile} object with the given
                 * format, and attached to the user's
                 * {@link ode.model.Experimenter} object via an
                 * {@link ode.model.FileAnnotation} with
                 * the namespace:
                 *  <i>bhojpur.net/ode/experimenter/photo</i>
                 * (NSEXPERIMENTERPHOTO).
                 * If such an {@link ode.model.OriginalFile} instance
                 * already exists, it will be overwritten. If more than one
                 * photo is present, the oldest version will be modified (i.e.
                 * the highest updateEvent id).
                 *
                 * This photo will be placed in the <i>user</i> group and
                 * therefore will be visible to everyone on the system.
                 *
                 * @param filename Not null. String name which will be used.
                 * @param format Not null. Format.value string. 'image/jpeg'
                 *        and 'image/png' are common values.
                 * @param data Not null. Data from the image. This will be
                 *        written to disk.
                 * @return the id of the overwritten or newly created user
                 *         photo OriginalFile object.
                 */
                long uploadMyUserPhoto(string filename, string format, Ice::ByteSeq data) throws ServerError;

                /**
                 * Retrieves the {@link ode.model.OriginalFile} object
                 * attached to this user as specified by
                 * {@code uploadMyUserPhoto}.
                 * The return value is order by the most recently modified
                 * file first.
                 *
                 * @return file objects. Possibly empty.
                 */
                idempotent OriginalFileList getMyUserPhotos() throws ServerError;

                /**
                 * Updates an experimenter if admin or owner of group. Only
                 * string fields on the object are taken into account.
                 * The root and guest experimenters may not be renamed.
                 *
                 * Before a SecurityViolation would be thrown, however, this
                 * method will pass to {@code #updateSelf} <em>if</em> the
                 * current user matches the given experimenter.
                 *
                 * @param experimenter the Experimenter to update.
                 */
                void updateExperimenter(ode::model::Experimenter experimenter) throws ServerError;

                /**
                 * Updates an experimenter if admin or owner of group.
                 * Only string fields on the object are taken into account.
                 * The root and guest experimenters may not be renamed.
                 *
                 * @param experimenter the Experimenter to update.
                 * @param password Not-null. Must pass validation in the
                 *        security sub-system.
                 */
                void updateExperimenterWithPassword(ode::model::Experimenter experimenter,
                                                    ode::RString password) throws ServerError;

                /**
                 * Updates an experimenter group if admin or owner of group.
                 * Only string fields on the object are taken into account.
                 * The root, system and guest groups may not be renamed,
                 * nor may the user's current group.
                 *
                 * @param group the ExperimenterGroup to update.
                 */
                void updateGroup(ode::model::ExperimenterGroup group) throws ServerError;

                /**
                 * Creates and returns a new user. This user will be created
                 * with the default group specified.
                 *
                 * @param experimenter a new {@link ode.model.Experimenter}
                 *        instance
                 * @param group group name of the default group for this user
                 * @return id of the newly created
                 * {@link ode.model.Experimenter}
                 */
                long createUser(ode::model::Experimenter experimenter, string group) throws ServerError;

                /**
                 * Creates and returns a new system user. This user will be
                 * created with the <i>System</i> (administration) group as
                 * default and will also be in the <i>user</i> group.
                 *
                 * @param experimenter a new {@link ode.model.Experimenter}
                 *        instance
                 * @return id of the newly created
                 *         {@link ode.model.Experimenter}
                 */
                long createSystemUser(ode::model::Experimenter experimenter) throws ServerError;

                /**
                 * Creates and returns a new system user. This user will be
                 * created with the <i>System</i> (administration) group as
                 * default and will also be in the <i>user</i> group. Their
                 * light administrator privileges will be set as given.
                 *
                 * @param experimenter a new {@link ode.model.Experimenter}
                 *        instance
                 * @param privileges the privileges to set for the user
                 * @return id of the newly created
                 *         {@link ode.model.Experimenter}
                 */
                long createRestrictedSystemUser(ode::model::Experimenter experimenter, AdminPrivilegeList privileges) throws ServerError;

                /**
                 * Creates and returns a new system user. This user will be
                 * created with the <i>System</i> (administration) group as
                 * default and will also be in the <i>user</i> group. Their
                 * light administrator privileges and password will be set
                 * as given.
                 *
                 * @param experimenter a new {@link ode.model.Experimenter}
                 *        instance
                 * @param privileges the privileges to set for the user
                 * @param password Not-null. Must pass validation in the
                 *        security sub-system.
                 * @return id of the newly created
                 *         {@link ode.model.Experimenter}
                 */
                long createRestrictedSystemUserWithPassword(ode::model::Experimenter experimenter, AdminPrivilegeList privileges, ode::RString password) throws ServerError;

                /**
                 * Creates and returns a new user in the given groups.
                 *
                 * @param user A new {@link ode.model.Experimenter}
                 *        instance. Not null.
                 * @param defaultGroup Instance of
                 *        {@link ode.model.ExperimenterGroup}. Not null.
                 * @param groups Array of
                 *        {@link ode.model.ExperimenterGroup} instances. Can
                 *        be null.
                 * @return id of the newly created
                 *         {@link ode.model.Experimenter} Not null.
                 */
                long createExperimenter(ode::model::Experimenter user,
                                        ode::model::ExperimenterGroup defaultGroup, ExperimenterGroupList groups) throws ServerError;

                /**
                 * Creates and returns a new user in the given groups with
                 * password.
                 *
                 * @param user A new {@link ode.model.Experimenter}
                 *        instance. Not null.
                 * @param password Not-null. Must pass validation in the
                 *        security sub-system.
                 * @param defaultGroup Instance of
                 *        {@link ode.model.ExperimenterGroup}. Not null.
                 * @param groups Array of
                 *        {@link ode.model.ExperimenterGroup} instances. Can
                 *        be null.
                 * @return id of the newly created
                 *         {@link ode.model.Experimenter} Not null.
                 * @throws SecurityViolation if the new password is too weak.
                 */
                long createExperimenterWithPassword(ode::model::Experimenter user, ode::RString password,
                                                    ode::model::ExperimenterGroup defaultGroup, ExperimenterGroupList groups) throws ServerError;

                /**
                 * Creates and returns a new group. The
                 * {@code ode.model.Details.setPermissions} method should be
                 * called on the instance which is passed. The given
                 * {@link ode.model.Permissions} will become the default for
                 * all objects created while logged into this group, possibly
                 * modified by the user's umask settings.
                 * If no permissions is set, the default will be
                 * {@code ode.model.Permissions.USER_PRIVATE},
                 * i.e. a group in which no user can see the other group
                 * member's data.
                 *
                 * @param group  a new
                 * {@link ode.model.ExperimenterGroup} instance. Not null.
                 * @return id of the newly created {@link ode.model.ExperimenterGroup}
                 */
                long createGroup(ode::model::ExperimenterGroup group) throws ServerError;

                /**
                 * Adds a user to the given groups.
                 *
                 * @param user A currently managed entity. Not null.
                 * @param groups Groups to which the user will be added. Not
                 *        null.
                 */
                idempotent void addGroups(ode::model::Experimenter user, ExperimenterGroupList groups) throws ServerError;

                /**
                 * Removes an experimenter from the given groups.
                 * <ul>
                 * <li>The root experimenter is required to be in both the
                 * user and system groups.</li>
                 * <li>An experimenter may not remove themselves from the user
                 * or system group.</li>
                 * <li>An experimenter may not be a member of only the user
                 * group, some other group is also required as the default
                 * group.</li>
                 * <li>An experimenter must remain a member of some group.</li>
                 * </ul>
                 *
                 * @param user A currently managed entity. Not null.
                 * @param groups Groups from which the user will be removed.
                 *        Not null.
                 */
                idempotent void removeGroups(ode::model::Experimenter user, ExperimenterGroupList groups) throws ServerError;

                /**
                 * Sets the default group for a given user.
                 *
                 * @param user A currently managed
                 *        {@link ode.model.Experimenter}. Not null.
                 * @param group The group which should be set as default group
                 *        for this user. Not null.
                 */
                idempotent void setDefaultGroup(ode::model::Experimenter user, ode::model::ExperimenterGroup group) throws ServerError;

                /**
                 * Adds the user to the owner list for this group.
                 *
                 * Multiple users can be the <i>owner</i> of a group.
                 *
                 * @param group A currently managed
                 *        {@link ode.model.ExperimenterGroup}. Not null.
                 * @param owner A currently managed
                 *        {@link ode.model.Experimenter}. Not null.
                 */
                idempotent void setGroupOwner(ode::model::ExperimenterGroup group, ode::model::Experimenter owner) throws ServerError;

                /**
                 * Removes the user from the owner list for this group.
                 *
                 * Multiple users can be the <i>owner</i> of a group.
                 *
                 * @param group A currently managed
                 *        {@link ode.model.ExperimenterGroup}. Not null.
                 * @param owner A currently managed
                 *        {@link ode.model.Experimenter}. Not null.
                 */
                idempotent void unsetGroupOwner(ode::model::ExperimenterGroup group, ode::model::Experimenter owner) throws ServerError;

                /**
                 * Adds the given users to the owner list for this group.
                 *
                 * @param group A currently managed
                 *        {@link ode.model.ExperimenterGroup}. Not null.
                 * @param owners A set of currently managed
                 *        {@link ode.model.Experimenter}s. Not null.
                 */
                idempotent void addGroupOwners(ode::model::ExperimenterGroup group, ExperimenterList owners) throws ServerError;

                /**
                 * removes the given users from the owner list for this group.
                 *
                 * @param group A currently managed
                 *        {@link ode.model.ExperimenterGroup}. Not
                 * @param owners A set of currently managed
                 *        {@link ode.model.Experimenter}s. Not null.
                 */
                idempotent void removeGroupOwners(ode::model::ExperimenterGroup group, ExperimenterList owners) throws ServerError;

                /**
                 * Removes a user by removing the password information for
                 * that user as well as all
                 * {@link ode.model.GroupExperimenterMap} instances.
                 *
                 * @param user Experimenter to be deleted. Not null.
                 */
                idempotent void deleteExperimenter(ode::model::Experimenter user) throws ServerError;

                /**
                 * Removes a group by first removing all users in the group,
                 * and then deleting the actual
                 * {@link ode.model.ExperimenterGroup} instance.
                 *
                 * @param group {@link ode.model.ExperimenterGroup} to be
                 *        deleted. Not null.
                 */
                idempotent void deleteGroup(ode::model::ExperimenterGroup group) throws ServerError;

                ["deprecate:changeOwner() is deprecated. use ode::cmd::Chown2() instead."]
                idempotent void changeOwner(ode::model::IObject obj, string odeName) throws ServerError;

                ["deprecate:changeGroup() is deprecated. use ode::cmd::Chgrp2() instead."]
                idempotent void changeGroup(ode::model::IObject obj, string odeName) throws ServerError;

                ["deprecate:changePermissions() is deprecated. use ode::cmd::Chmod2() instead."]
                idempotent void changePermissions(ode::model::IObject obj, ode::model::Permissions perms) throws ServerError;

                /**
                 * Moves the given objects into the <i>user</i> group to make
                 * them visible and linkable from all security contexts.
                 *
                 * @param objects
                 */
                idempotent void moveToCommonSpace(IObjectList objects) throws ServerError;

                /**
                 * Sets the set of light administrator privileges for the given user.
                 *
                 * @param user the user whose privileges are to be set
                 * @param privileges the privileges to set for the user
                 */
                idempotent void setAdminPrivileges(ode::model::Experimenter user, AdminPrivilegeList privileges) throws ServerError;

                // UAuth

                /**
                 * Changes the password for the current user.
                 * <p>
                 * <em>Warning:</em>This method requires the user to be
                 * authenticated with a password and not with a one-time
                 * session id. To avoid this problem, use
                 * {@code changePasswordWithOldPassword}.
                 * </p>
                 *
                 * @param newPassword Possibly null to allow logging in with
                 *        no password.
                 * @throws SecurityViolation if the user is not authenticated
                 *         with a password.
                 */
                idempotent void changePassword(ode::RString newPassword) throws ServerError;

                /**
                 * Changes the password for the current user by passing the
                 * old password.
                 *
                 * @param oldPassword Not-null. Must pass validation in the
                 *                    security sub-system.
                 * @param newPassword Possibly null to allow logging in with
                 *                    no password.
                 * @throws SecurityViolation if the oldPassword is incorrect.
                 **/
                idempotent void changePasswordWithOldPassword(ode::RString oldPassword, ode::RString newPassword) throws ServerError;

                /**
                 * Changes the password for the a given user.
                 *
                 * @param newPassword Not-null. Might must pass validation in
                 *        the security sub-system.
                 * @throws SecurityViolation if the new password is too weak.
                 */
                idempotent void changeUserPassword(string odeName, ode::RString newPassword) throws ServerError;

                /**
                 * Uses JMX to refresh the login cache <em>if supported</em>.
                 * Some backends may not provide refreshing. This may be
                 * called internally during some other administrative tasks.
                 * The exact implementation of this depends on the application
                 * server and the authentication/authorization backend.
                 */
                idempotent void synchronizeLoginCache() throws ServerError;

                /**
                 * Used after an {@link ode.ExpiredCredentialException}
                 * instance is thrown.
                 */
                void changeExpiredCredentials(string name, string oldCred, string newCred) throws ServerError;

                ["deprecate:reportForgottenPassword() is deprecated. use ode::cmd::ResetPasswordRequest() instead."]
                void reportForgottenPassword(string name, string email) throws ServerError;

                // Security Context
                /**
                 * Returns the active {@link ode.sys.Roles} in use by the
                 * server.
                 *
                 * @return Non-null, immutable {@link ode.sys.Roles}
                 *         instance.
                 */
                idempotent ode::sys::Roles getSecurityRoles() throws ServerError;

                /**
                 * Returns an implementation of {@link ode.sys.EventContext}
                 * loaded with the security for the current user and thread.
                 * If called remotely, not all values of
                 * {@link ode.sys.EventContext} will be sensible.
                 *
                 * @return Non-null, immutable {@link ode.sys.EventContext}
                 *         instance
                 */
                idempotent ode::sys::EventContext getEventContext() throws ServerError;
            };

    };
};

#endif