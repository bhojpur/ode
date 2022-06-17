#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

from sys import version_info as _version_info_
import Ice, IcePy
import ode_ServicesF_ice
import ode_System_ice
import ode_Collections_ice
import ode_model_AdminPrivilege_ice

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Included module ode.model.enums
_M_ode.model.enums = Ice.openModule('ode.model.enums')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if '_t_AdminPrivilegeList' not in _M_ode.api.__dict__:
    _M_ode.api._t_AdminPrivilegeList = IcePy.defineSequence('::ode::api::AdminPrivilegeList', (), _M_ode.model._t_AdminPrivilege)

if 'IAdmin' not in _M_ode.api.__dict__:
    _M_ode.api.IAdmin = Ice.createTempClass()
    class IAdmin(_M_ode.api.ServiceInterface):
        """
        Administration interface providing access to admin-only
        functionality as well as JMX-based server access and selected user
        functions. Most methods require membership in privileged
        ode.model.ExperimenterGroup groups.
        Methods which return ode.model.Experimenter or
        ode.model.ExperimenterGroup instances fetch and load all
        related instances of ode.model.ExperimenterGroup or
        ode.model.Experimenter, respectively.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IAdmin:
                raise RuntimeError('ode.api.IAdmin is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IAdmin', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IAdmin'

        def ice_staticId():
            return '::ode::api::IAdmin'
        ice_staticId = staticmethod(ice_staticId)

        def canUpdate_async(self, _cb, obj, current=None):
            """
            Returns true if the currently logged in user can modify the
            given ode.model.IObject. This uses the same logic
            that would be applied during a Hibernate flush to the
            database.
            Arguments:
            _cb -- The asynchronous callback object.
            obj -- 
            current -- The Current object for the invocation.
            """
            pass

        def getExperimenter_async(self, _cb, id, current=None):
            """
            Fetches an ode.model.Experimenter and all related
            ode.model.ExperimenterGroup.
            Arguments:
            _cb -- The asynchronous callback object.
            id -- id of the Experimenter
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if id does not exist.
            """
            pass

        def lookupExperimenter_async(self, _cb, name, current=None):
            """
            Looks up an ode.model.Experimenter and all related
            ode.model.ExperimenterGroup by name.
            Arguments:
            _cb -- The asynchronous callback object.
            name -- Name of the Experimenter
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if odeName does not exist.
            """
            pass

        def lookupExperimenters_async(self, _cb, current=None):
            """
            Looks up all ode.model.Experimenter experimenters
            present and all related
            ode.model.ExperimenterGroup groups.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getGroup_async(self, _cb, id, current=None):
            """
            Fetches an ode.model.ExperimenterGroup and all
            contained ode.model.Experimenter users.
            Arguments:
            _cb -- The asynchronous callback object.
            id -- id of the ExperimenterGroup
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if id does not exist.
            """
            pass

        def lookupGroup_async(self, _cb, name, current=None):
            """
            Looks up an ode.model.ExperimenterGroup and all
            contained ode.model.Experimenter users by name.
            Arguments:
            _cb -- The asynchronous callback object.
            name -- Name of the ExperimenterGroup
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if groupName does not exist.
            """
            pass

        def lookupGroups_async(self, _cb, current=None):
            """
            Looks up all ode.model.ExperimenterGroup groups
            present and all related
            ode.model.Experimenter experimenters. The
            experimenter's groups are also loaded.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def containedExperimenters_async(self, _cb, groupId, current=None):
            """
            Fetches all ode.model.Experimenter users
            contained in this group. The returned users will have all
            fields filled in and all collections unloaded.
            Arguments:
            _cb -- The asynchronous callback object.
            groupId -- id of the ExperimenterGroup
            current -- The Current object for the invocation.
            """
            pass

        def containedGroups_async(self, _cb, experimenterId, current=None):
            """
            Fetches all ode.model.ExperimenterGroup groups of
            which the given user is a member. The returned groups will
            have all fields filled in and all collections unloaded.
            Arguments:
            _cb -- The asynchronous callback object.
            experimenterId -- id of the Experimenter. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def getDefaultGroup_async(self, _cb, experimenterId, current=None):
            """
            Retrieves the default ode.model.ExperimenterGroup
            group for the given user id.
            Arguments:
            _cb -- The asynchronous callback object.
            experimenterId -- of the Experimenter. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def lookupLdapAuthExperimenter_async(self, _cb, id, current=None):
            """
            Looks up ode.model.Experimenter experimenters who
            use LDAP authentication  (has set dn on password table).
            Arguments:
            _cb -- The asynchronous callback object.
            id -- id of the Experimenter. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def lookupLdapAuthExperimenters_async(self, _cb, current=None):
            """
            Looks up all ids of ode.model.Experimenter
            experimenters who use LDAP authentication (has set dn on
            password table).
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getMemberOfGroupIds_async(self, _cb, exp, current=None):
            """
            Finds the ids for all groups for which the given
            ode.model.Experimenter is a member.
            Arguments:
            _cb -- The asynchronous callback object.
            exp -- Non-null, managed (i.e. with id)
            current -- The Current object for the invocation.
            """
            pass

        def getLeaderOfGroupIds_async(self, _cb, exp, current=None):
            """
            Finds the ids for all groups for which the given
            ode.model.Experimenter is owner/leader.
            Arguments:
            _cb -- The asynchronous callback object.
            exp -- Non-null, managed (i.e. with id)
            current -- The Current object for the invocation.
            """
            pass

        def getCurrentAdminPrivileges_async(self, _cb, current=None):
            """
            Gets the light administrator privileges for the current user.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getAdminPrivileges_async(self, _cb, user, current=None):
            """
            Gets the light administrator privileges for the given user.
            Arguments:
            _cb -- The asynchronous callback object.
            user -- the user whose privileges are being queried
            current -- The Current object for the invocation.
            """
            pass

        def getAdminsWithPrivileges_async(self, _cb, privileges, current=None):
            """
            Gets the administrators who have all the given privileges.
            Consistent with the results from "getAdminPrivileges".
            Arguments:
            _cb -- The asynchronous callback object.
            privileges -- the required privileges
            current -- The Current object for the invocation.
            """
            pass

        def updateSelf_async(self, _cb, experimenter, current=None):
            """
            Allows a user to update his/her own information. This is
            limited to the fields on Experimenter, all other fields
            (groups, etc.) are ignored. The experimenter argument need
            not have the proper id nor the proper odeName (which is
            immutable). To change the users default group (which is the
            only other customizable option), use
            {@code setDefaultGroup}
            Arguments:
            _cb -- The asynchronous callback object.
            experimenter -- 
            current -- The Current object for the invocation.
            """
            pass

        def uploadMyUserPhoto_async(self, _cb, filename, format, data, current=None):
            """
            Uploads a photo for the user which will be displayed on
            his/her profile.
            This photo will be saved as an
            ode.model.OriginalFile object with the given
            format, and attached to the user's
            ode.model.Experimenter object via an
            ode.model.FileAnnotation with
            the namespace:
            bhojpur.net/ode/experimenter/photo
            (NSEXPERIMENTERPHOTO).
            If such an ode.model.OriginalFile instance
            already exists, it will be overwritten. If more than one
            photo is present, the oldest version will be modified (i.e.
            the highest updateEvent id).
            this photo will be placed in the user group and
            therefore will be visible to everyone on the system.
            Arguments:
            _cb -- The asynchronous callback object.
            filename -- Not null. String name which will be used.
            format -- Not null. Format.value string. 'image/jpeg' and 'image/png' are common values.
            data -- Not null. Data from the image. This will be written to disk.
            current -- The Current object for the invocation.
            """
            pass

        def getMyUserPhotos_async(self, _cb, current=None):
            """
            Retrieves the ode.model.OriginalFile object
            attached to this user as specified by
            {@code uploadMyUserPhoto}.
            The return value is order by the most recently modified
            file first.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def updateExperimenter_async(self, _cb, experimenter, current=None):
            """
            Updates an experimenter if admin or owner of group. Only
            string fields on the object are taken into account.
            The root and guest experimenters may not be renamed.
            Before a SecurityViolation would be thrown, however, this
            method will pass to {@code #updateSelf} if the
            current user matches the given experimenter.
            Arguments:
            _cb -- The asynchronous callback object.
            experimenter -- the Experimenter to update.
            current -- The Current object for the invocation.
            """
            pass

        def updateExperimenterWithPassword_async(self, _cb, experimenter, password, current=None):
            """
            Updates an experimenter if admin or owner of group.
            Only string fields on the object are taken into account.
            The root and guest experimenters may not be renamed.
            Arguments:
            _cb -- The asynchronous callback object.
            experimenter -- the Experimenter to update.
            password -- Not-null. Must pass validation in the security sub-system.
            current -- The Current object for the invocation.
            """
            pass

        def updateGroup_async(self, _cb, group, current=None):
            """
            Updates an experimenter group if admin or owner of group.
            Only string fields on the object are taken into account.
            The root, system and guest groups may not be renamed,
            nor may the user's current group.
            Arguments:
            _cb -- The asynchronous callback object.
            group -- the ExperimenterGroup to update.
            current -- The Current object for the invocation.
            """
            pass

        def createUser_async(self, _cb, experimenter, group, current=None):
            """
            Creates and returns a new user. This user will be created
            with the default group specified.
            Arguments:
            _cb -- The asynchronous callback object.
            experimenter -- a new ode.model.Experimenter instance
            group -- group name of the default group for this user
            current -- The Current object for the invocation.
            """
            pass

        def createSystemUser_async(self, _cb, experimenter, current=None):
            """
            Creates and returns a new system user. This user will be
            created with the System (administration) group as
            default and will also be in the user group.
            Arguments:
            _cb -- The asynchronous callback object.
            experimenter -- a new ode.model.Experimenter instance
            current -- The Current object for the invocation.
            """
            pass

        def createRestrictedSystemUser_async(self, _cb, experimenter, privileges, current=None):
            """
            Creates and returns a new system user. This user will be
            created with the System (administration) group as
            default and will also be in the user group. Their
            light administrator privileges will be set as given.
            Arguments:
            _cb -- The asynchronous callback object.
            experimenter -- a new ode.model.Experimenter instance
            privileges -- the privileges to set for the user
            current -- The Current object for the invocation.
            """
            pass

        def createRestrictedSystemUserWithPassword_async(self, _cb, experimenter, privileges, password, current=None):
            """
            Creates and returns a new system user. This user will be
            created with the System (administration) group as
            default and will also be in the user group. Their
            light administrator privileges and password will be set
            as given.
            Arguments:
            _cb -- The asynchronous callback object.
            experimenter -- a new ode.model.Experimenter instance
            privileges -- the privileges to set for the user
            password -- Not-null. Must pass validation in the security sub-system.
            current -- The Current object for the invocation.
            """
            pass

        def createExperimenter_async(self, _cb, user, defaultGroup, groups, current=None):
            """
            Creates and returns a new user in the given groups.
            Arguments:
            _cb -- The asynchronous callback object.
            user -- A new ode.model.Experimenter instance. Not null.
            defaultGroup -- Instance of ode.model.ExperimenterGroup. Not null.
            groups -- Array of ode.model.ExperimenterGroup instances. Can be null.
            current -- The Current object for the invocation.
            """
            pass

        def createExperimenterWithPassword_async(self, _cb, user, password, defaultGroup, groups, current=None):
            """
            Creates and returns a new user in the given groups with
            password.
            Arguments:
            _cb -- The asynchronous callback object.
            user -- A new ode.model.Experimenter instance. Not null.
            password -- Not-null. Must pass validation in the security sub-system.
            defaultGroup -- Instance of ode.model.ExperimenterGroup. Not null.
            groups -- Array of ode.model.ExperimenterGroup instances. Can be null.
            current -- The Current object for the invocation.
            Throws:
            SecurityViolation -- if the new password is too weak.
            """
            pass

        def createGroup_async(self, _cb, group, current=None):
            """
            Creates and returns a new group. The
            {@code ode.model.Details.setPermissions} method should be
            called on the instance which is passed. The given
            ode.model.Permissions will become the default for
            all objects created while logged into this group, possibly
            modified by the user's umask settings.
            If no permissions is set, the default will be
            {@code ode.model.Permissions.USER_PRIVATE},
            i.e. a group in which no user can see the other group
            member's data.
            Arguments:
            _cb -- The asynchronous callback object.
            group -- a new ode.model.ExperimenterGroup instance. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def addGroups_async(self, _cb, user, groups, current=None):
            """
            Adds a user to the given groups.
            Arguments:
            _cb -- The asynchronous callback object.
            user -- A currently managed entity. Not null.
            groups -- Groups to which the user will be added. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def removeGroups_async(self, _cb, user, groups, current=None):
            """
            Removes an experimenter from the given groups.
            The root experimenter is required to be in both the
            user and system groups.
            An experimenter may not remove themselves from the user
            or system group.
            An experimenter may not be a member of only the user
            group, some other group is also required as the default
            group.
            An experimenter must remain a member of some group.
            Arguments:
            _cb -- The asynchronous callback object.
            user -- A currently managed entity. Not null.
            groups -- Groups from which the user will be removed. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def setDefaultGroup_async(self, _cb, user, group, current=None):
            """
            Sets the default group for a given user.
            Arguments:
            _cb -- The asynchronous callback object.
            user -- A currently managed ode.model.Experimenter. Not null.
            group -- The group which should be set as default group for this user. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def setGroupOwner_async(self, _cb, group, owner, current=None):
            """
            Adds the user to the owner list for this group.
            Since Beta 4.2
            multiple users can be the owner of a group.
            Arguments:
            _cb -- The asynchronous callback object.
            group -- A currently managed ode.model.ExperimenterGroup. Not null.
            owner -- A currently managed ode.model.Experimenter. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def unsetGroupOwner_async(self, _cb, group, owner, current=None):
            """
            Removes the user from the owner list for this group.
            Since Beta 4.2
            multiple users can be the owner of a group.
            Arguments:
            _cb -- The asynchronous callback object.
            group -- A currently managed ode.model.ExperimenterGroup. Not null.
            owner -- A currently managed ode.model.Experimenter. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def addGroupOwners_async(self, _cb, group, owners, current=None):
            """
            Adds the given users to the owner list for this group.
            Arguments:
            _cb -- The asynchronous callback object.
            group -- A currently managed ode.model.ExperimenterGroup. Not null.
            owners -- A set of currently managed ode.model.Experimenters. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def removeGroupOwners_async(self, _cb, group, owners, current=None):
            """
            removes the given users from the owner list for this group.
            Arguments:
            _cb -- The asynchronous callback object.
            group -- A currently managed ode.model.ExperimenterGroup. Not
            owners -- A set of currently managed ode.model.Experimenters. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def deleteExperimenter_async(self, _cb, user, current=None):
            """
            Removes a user by removing the password information for
            that user as well as all
            ode.model.GroupExperimenterMap instances.
            Arguments:
            _cb -- The asynchronous callback object.
            user -- Experimenter to be deleted. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def deleteGroup_async(self, _cb, group, current=None):
            """
            Removes a group by first removing all users in the group,
            and then deleting the actual
            ode.model.ExperimenterGroup instance.
            Arguments:
            _cb -- The asynchronous callback object.
            group -- ode.model.ExperimenterGroup to be deleted. Not null.
            current -- The Current object for the invocation.
            """
            pass

        def changeOwner_async(self, _cb, obj, odeName, current=None):
            pass

        def changeGroup_async(self, _cb, obj, odeName, current=None):
            pass

        def changePermissions_async(self, _cb, obj, perms, current=None):
            pass

        def moveToCommonSpace_async(self, _cb, objects, current=None):
            """
            Moves the given objects into the user group to make
            them visible and linkable from all security contexts.
            Arguments:
            _cb -- The asynchronous callback object.
            objects -- 
            current -- The Current object for the invocation.
            """
            pass

        def setAdminPrivileges_async(self, _cb, user, privileges, current=None):
            """
            Sets the set of light administrator privileges for the given user.
            Arguments:
            _cb -- The asynchronous callback object.
            user -- the user whose privileges are to be set
            privileges -- the privileges to set for the user
            current -- The Current object for the invocation.
            """
            pass

        def changePassword_async(self, _cb, newPassword, current=None):
            """
            Changes the password for the current user.
            Warning:This method requires the user to be
            authenticated with a password and not with a one-time
            session id. To avoid this problem, use
            {@code changePasswordWithOldPassword}.
            Arguments:
            _cb -- The asynchronous callback object.
            newPassword -- Possibly null to allow logging in with no password.
            current -- The Current object for the invocation.
            Throws:
            SecurityViolation -- if the user is not authenticated with a password.
            """
            pass

        def changePasswordWithOldPassword_async(self, _cb, oldPassword, newPassword, current=None):
            """
            Changes the password for the current user by passing the
            old password.
            Arguments:
            _cb -- The asynchronous callback object.
            oldPassword -- Not-null. Must pass validation in the security sub-system.
            newPassword -- Possibly null to allow logging in with no password.
            current -- The Current object for the invocation.
            Throws:
            SecurityViolation -- if the oldPassword is incorrect.
            """
            pass

        def changeUserPassword_async(self, _cb, odeName, newPassword, current=None):
            """
            Changes the password for the a given user.
            Arguments:
            _cb -- The asynchronous callback object.
            odeName -- 
            newPassword -- Not-null. Might must pass validation in the security sub-system.
            current -- The Current object for the invocation.
            Throws:
            SecurityViolation -- if the new password is too weak.
            """
            pass

        def synchronizeLoginCache_async(self, _cb, current=None):
            """
            Uses JMX to refresh the login cache if supported.
            Some backends may not provide refreshing. This may be
            called internally during some other administrative tasks.
            The exact implementation of this depends on the application
            server and the authentication/authorization backend.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def changeExpiredCredentials_async(self, _cb, name, oldCred, newCred, current=None):
            """
            Used after an ode.ExpiredCredentialException
            instance is thrown.
            Arguments:
            _cb -- The asynchronous callback object.
            name -- 
            oldCred -- 
            newCred -- 
            current -- The Current object for the invocation.
            """
            pass

        def reportForgottenPassword_async(self, _cb, name, email, current=None):
            pass

        def getSecurityRoles_async(self, _cb, current=None):
            """
            Returns the active ode.sys.Roles in use by the
            server.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getEventContext_async(self, _cb, current=None):
            """
            Returns an implementation of ode.sys.EventContext
            loaded with the security for the current user and thread.
            If called remotely, not all values of
            ode.sys.EventContext will be sensible.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IAdmin)

        __repr__ = __str__

    _M_ode.api.IAdminPrx = Ice.createTempClass()
    class IAdminPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Returns true if the currently logged in user can modify the
        given ode.model.IObject. This uses the same logic
        that would be applied during a Hibernate flush to the
        database.
        Arguments:
        obj -- 
        _ctx -- The request context for the invocation.
        """
        def canUpdate(self, obj, _ctx=None):
            return _M_ode.api.IAdmin._op_canUpdate.invoke(self, ((obj, ), _ctx))

        """
        Returns true if the currently logged in user can modify the
        given ode.model.IObject. This uses the same logic
        that would be applied during a Hibernate flush to the
        database.
        Arguments:
        obj -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_canUpdate(self, obj, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_canUpdate.begin(self, ((obj, ), _response, _ex, _sent, _ctx))

        """
        Returns true if the currently logged in user can modify the
        given ode.model.IObject. This uses the same logic
        that would be applied during a Hibernate flush to the
        database.
        Arguments:
        obj -- 
        """
        def end_canUpdate(self, _r):
            return _M_ode.api.IAdmin._op_canUpdate.end(self, _r)

        """
        Fetches an ode.model.Experimenter and all related
        ode.model.ExperimenterGroup.
        Arguments:
        id -- id of the Experimenter
        _ctx -- The request context for the invocation.
        Returns: an Experimenter. Never null.
        Throws:
        ApiUsageException -- if id does not exist.
        """
        def getExperimenter(self, id, _ctx=None):
            return _M_ode.api.IAdmin._op_getExperimenter.invoke(self, ((id, ), _ctx))

        """
        Fetches an ode.model.Experimenter and all related
        ode.model.ExperimenterGroup.
        Arguments:
        id -- id of the Experimenter
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getExperimenter(self, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getExperimenter.begin(self, ((id, ), _response, _ex, _sent, _ctx))

        """
        Fetches an ode.model.Experimenter and all related
        ode.model.ExperimenterGroup.
        Arguments:
        id -- id of the Experimenter
        Returns: an Experimenter. Never null.
        Throws:
        ApiUsageException -- if id does not exist.
        """
        def end_getExperimenter(self, _r):
            return _M_ode.api.IAdmin._op_getExperimenter.end(self, _r)

        """
        Looks up an ode.model.Experimenter and all related
        ode.model.ExperimenterGroup by name.
        Arguments:
        name -- Name of the Experimenter
        _ctx -- The request context for the invocation.
        Returns: an Experimenter. Never null.
        Throws:
        ApiUsageException -- if odeName does not exist.
        """
        def lookupExperimenter(self, name, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupExperimenter.invoke(self, ((name, ), _ctx))

        """
        Looks up an ode.model.Experimenter and all related
        ode.model.ExperimenterGroup by name.
        Arguments:
        name -- Name of the Experimenter
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_lookupExperimenter(self, name, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupExperimenter.begin(self, ((name, ), _response, _ex, _sent, _ctx))

        """
        Looks up an ode.model.Experimenter and all related
        ode.model.ExperimenterGroup by name.
        Arguments:
        name -- Name of the Experimenter
        Returns: an Experimenter. Never null.
        Throws:
        ApiUsageException -- if odeName does not exist.
        """
        def end_lookupExperimenter(self, _r):
            return _M_ode.api.IAdmin._op_lookupExperimenter.end(self, _r)

        """
        Looks up all ode.model.Experimenter experimenters
        present and all related
        ode.model.ExperimenterGroup groups.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: all Experimenters. Never null.
        """
        def lookupExperimenters(self, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupExperimenters.invoke(self, ((), _ctx))

        """
        Looks up all ode.model.Experimenter experimenters
        present and all related
        ode.model.ExperimenterGroup groups.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_lookupExperimenters(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupExperimenters.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Looks up all ode.model.Experimenter experimenters
        present and all related
        ode.model.ExperimenterGroup groups.
        Arguments:
        Returns: all Experimenters. Never null.
        """
        def end_lookupExperimenters(self, _r):
            return _M_ode.api.IAdmin._op_lookupExperimenters.end(self, _r)

        """
        Fetches an ode.model.ExperimenterGroup and all
        contained ode.model.Experimenter users.
        Arguments:
        id -- id of the ExperimenterGroup
        _ctx -- The request context for the invocation.
        Returns: an ExperimenterGroup. Never null.
        Throws:
        ApiUsageException -- if id does not exist.
        """
        def getGroup(self, id, _ctx=None):
            return _M_ode.api.IAdmin._op_getGroup.invoke(self, ((id, ), _ctx))

        """
        Fetches an ode.model.ExperimenterGroup and all
        contained ode.model.Experimenter users.
        Arguments:
        id -- id of the ExperimenterGroup
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getGroup(self, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getGroup.begin(self, ((id, ), _response, _ex, _sent, _ctx))

        """
        Fetches an ode.model.ExperimenterGroup and all
        contained ode.model.Experimenter users.
        Arguments:
        id -- id of the ExperimenterGroup
        Returns: an ExperimenterGroup. Never null.
        Throws:
        ApiUsageException -- if id does not exist.
        """
        def end_getGroup(self, _r):
            return _M_ode.api.IAdmin._op_getGroup.end(self, _r)

        """
        Looks up an ode.model.ExperimenterGroup and all
        contained ode.model.Experimenter users by name.
        Arguments:
        name -- Name of the ExperimenterGroup
        _ctx -- The request context for the invocation.
        Returns: an ExperimenterGroup. Never null.
        Throws:
        ApiUsageException -- if groupName does not exist.
        """
        def lookupGroup(self, name, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupGroup.invoke(self, ((name, ), _ctx))

        """
        Looks up an ode.model.ExperimenterGroup and all
        contained ode.model.Experimenter users by name.
        Arguments:
        name -- Name of the ExperimenterGroup
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_lookupGroup(self, name, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupGroup.begin(self, ((name, ), _response, _ex, _sent, _ctx))

        """
        Looks up an ode.model.ExperimenterGroup and all
        contained ode.model.Experimenter users by name.
        Arguments:
        name -- Name of the ExperimenterGroup
        Returns: an ExperimenterGroup. Never null.
        Throws:
        ApiUsageException -- if groupName does not exist.
        """
        def end_lookupGroup(self, _r):
            return _M_ode.api.IAdmin._op_lookupGroup.end(self, _r)

        """
        Looks up all ode.model.ExperimenterGroup groups
        present and all related
        ode.model.Experimenter experimenters. The
        experimenter's groups are also loaded.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: all Groups. Never null.
        """
        def lookupGroups(self, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupGroups.invoke(self, ((), _ctx))

        """
        Looks up all ode.model.ExperimenterGroup groups
        present and all related
        ode.model.Experimenter experimenters. The
        experimenter's groups are also loaded.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_lookupGroups(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupGroups.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Looks up all ode.model.ExperimenterGroup groups
        present and all related
        ode.model.Experimenter experimenters. The
        experimenter's groups are also loaded.
        Arguments:
        Returns: all Groups. Never null.
        """
        def end_lookupGroups(self, _r):
            return _M_ode.api.IAdmin._op_lookupGroups.end(self, _r)

        """
        Fetches all ode.model.Experimenter users
        contained in this group. The returned users will have all
        fields filled in and all collections unloaded.
        Arguments:
        groupId -- id of the ExperimenterGroup
        _ctx -- The request context for the invocation.
        Returns: non-null array of all ode.model.Experimenter users in this group.
        """
        def containedExperimenters(self, groupId, _ctx=None):
            return _M_ode.api.IAdmin._op_containedExperimenters.invoke(self, ((groupId, ), _ctx))

        """
        Fetches all ode.model.Experimenter users
        contained in this group. The returned users will have all
        fields filled in and all collections unloaded.
        Arguments:
        groupId -- id of the ExperimenterGroup
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_containedExperimenters(self, groupId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_containedExperimenters.begin(self, ((groupId, ), _response, _ex, _sent, _ctx))

        """
        Fetches all ode.model.Experimenter users
        contained in this group. The returned users will have all
        fields filled in and all collections unloaded.
        Arguments:
        groupId -- id of the ExperimenterGroup
        Returns: non-null array of all ode.model.Experimenter users in this group.
        """
        def end_containedExperimenters(self, _r):
            return _M_ode.api.IAdmin._op_containedExperimenters.end(self, _r)

        """
        Fetches all ode.model.ExperimenterGroup groups of
        which the given user is a member. The returned groups will
        have all fields filled in and all collections unloaded.
        Arguments:
        experimenterId -- id of the Experimenter. Not null.
        _ctx -- The request context for the invocation.
        Returns: non-null array of all ode.model.ExperimenterGroup groups for this user.
        """
        def containedGroups(self, experimenterId, _ctx=None):
            return _M_ode.api.IAdmin._op_containedGroups.invoke(self, ((experimenterId, ), _ctx))

        """
        Fetches all ode.model.ExperimenterGroup groups of
        which the given user is a member. The returned groups will
        have all fields filled in and all collections unloaded.
        Arguments:
        experimenterId -- id of the Experimenter. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_containedGroups(self, experimenterId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_containedGroups.begin(self, ((experimenterId, ), _response, _ex, _sent, _ctx))

        """
        Fetches all ode.model.ExperimenterGroup groups of
        which the given user is a member. The returned groups will
        have all fields filled in and all collections unloaded.
        Arguments:
        experimenterId -- id of the Experimenter. Not null.
        Returns: non-null array of all ode.model.ExperimenterGroup groups for this user.
        """
        def end_containedGroups(self, _r):
            return _M_ode.api.IAdmin._op_containedGroups.end(self, _r)

        """
        Retrieves the default ode.model.ExperimenterGroup
        group for the given user id.
        Arguments:
        experimenterId -- of the Experimenter. Not null.
        _ctx -- The request context for the invocation.
        Returns: non-null ode.model.ExperimenterGroup. If no default group is found, an exception will be thrown.
        """
        def getDefaultGroup(self, experimenterId, _ctx=None):
            return _M_ode.api.IAdmin._op_getDefaultGroup.invoke(self, ((experimenterId, ), _ctx))

        """
        Retrieves the default ode.model.ExperimenterGroup
        group for the given user id.
        Arguments:
        experimenterId -- of the Experimenter. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getDefaultGroup(self, experimenterId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getDefaultGroup.begin(self, ((experimenterId, ), _response, _ex, _sent, _ctx))

        """
        Retrieves the default ode.model.ExperimenterGroup
        group for the given user id.
        Arguments:
        experimenterId -- of the Experimenter. Not null.
        Returns: non-null ode.model.ExperimenterGroup. If no default group is found, an exception will be thrown.
        """
        def end_getDefaultGroup(self, _r):
            return _M_ode.api.IAdmin._op_getDefaultGroup.end(self, _r)

        """
        Looks up ode.model.Experimenter experimenters who
        use LDAP authentication  (has set dn on password table).
        Arguments:
        id -- id of the Experimenter. Not null.
        _ctx -- The request context for the invocation.
        Returns: Experimenter. Never null.
        """
        def lookupLdapAuthExperimenter(self, id, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupLdapAuthExperimenter.invoke(self, ((id, ), _ctx))

        """
        Looks up ode.model.Experimenter experimenters who
        use LDAP authentication  (has set dn on password table).
        Arguments:
        id -- id of the Experimenter. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_lookupLdapAuthExperimenter(self, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupLdapAuthExperimenter.begin(self, ((id, ), _response, _ex, _sent, _ctx))

        """
        Looks up ode.model.Experimenter experimenters who
        use LDAP authentication  (has set dn on password table).
        Arguments:
        id -- id of the Experimenter. Not null.
        Returns: Experimenter. Never null.
        """
        def end_lookupLdapAuthExperimenter(self, _r):
            return _M_ode.api.IAdmin._op_lookupLdapAuthExperimenter.end(self, _r)

        """
        Looks up all ids of ode.model.Experimenter
        experimenters who use LDAP authentication (has set dn on
        password table).
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: list of experimenters. Never null.
        """
        def lookupLdapAuthExperimenters(self, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupLdapAuthExperimenters.invoke(self, ((), _ctx))

        """
        Looks up all ids of ode.model.Experimenter
        experimenters who use LDAP authentication (has set dn on
        password table).
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_lookupLdapAuthExperimenters(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_lookupLdapAuthExperimenters.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Looks up all ids of ode.model.Experimenter
        experimenters who use LDAP authentication (has set dn on
        password table).
        Arguments:
        Returns: list of experimenters. Never null.
        """
        def end_lookupLdapAuthExperimenters(self, _r):
            return _M_ode.api.IAdmin._op_lookupLdapAuthExperimenters.end(self, _r)

        """
        Finds the ids for all groups for which the given
        ode.model.Experimenter is a member.
        Arguments:
        exp -- Non-null, managed (i.e. with id)
        _ctx -- The request context for the invocation.
        """
        def getMemberOfGroupIds(self, exp, _ctx=None):
            return _M_ode.api.IAdmin._op_getMemberOfGroupIds.invoke(self, ((exp, ), _ctx))

        """
        Finds the ids for all groups for which the given
        ode.model.Experimenter is a member.
        Arguments:
        exp -- Non-null, managed (i.e. with id)
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMemberOfGroupIds(self, exp, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getMemberOfGroupIds.begin(self, ((exp, ), _response, _ex, _sent, _ctx))

        """
        Finds the ids for all groups for which the given
        ode.model.Experimenter is a member.
        Arguments:
        exp -- Non-null, managed (i.e. with id)
        """
        def end_getMemberOfGroupIds(self, _r):
            return _M_ode.api.IAdmin._op_getMemberOfGroupIds.end(self, _r)

        """
        Finds the ids for all groups for which the given
        ode.model.Experimenter is owner/leader.
        Arguments:
        exp -- Non-null, managed (i.e. with id)
        _ctx -- The request context for the invocation.
        """
        def getLeaderOfGroupIds(self, exp, _ctx=None):
            return _M_ode.api.IAdmin._op_getLeaderOfGroupIds.invoke(self, ((exp, ), _ctx))

        """
        Finds the ids for all groups for which the given
        ode.model.Experimenter is owner/leader.
        Arguments:
        exp -- Non-null, managed (i.e. with id)
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getLeaderOfGroupIds(self, exp, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getLeaderOfGroupIds.begin(self, ((exp, ), _response, _ex, _sent, _ctx))

        """
        Finds the ids for all groups for which the given
        ode.model.Experimenter is owner/leader.
        Arguments:
        exp -- Non-null, managed (i.e. with id)
        """
        def end_getLeaderOfGroupIds(self, _r):
            return _M_ode.api.IAdmin._op_getLeaderOfGroupIds.end(self, _r)

        """
        Gets the light administrator privileges for the current user.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: the current user's light administrator privileges
        """
        def getCurrentAdminPrivileges(self, _ctx=None):
            return _M_ode.api.IAdmin._op_getCurrentAdminPrivileges.invoke(self, ((), _ctx))

        """
        Gets the light administrator privileges for the current user.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getCurrentAdminPrivileges(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getCurrentAdminPrivileges.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Gets the light administrator privileges for the current user.
        Arguments:
        Returns: the current user's light administrator privileges
        """
        def end_getCurrentAdminPrivileges(self, _r):
            return _M_ode.api.IAdmin._op_getCurrentAdminPrivileges.end(self, _r)

        """
        Gets the light administrator privileges for the given user.
        Arguments:
        user -- the user whose privileges are being queried
        _ctx -- The request context for the invocation.
        Returns: the user's light administrator privileges
        """
        def getAdminPrivileges(self, user, _ctx=None):
            return _M_ode.api.IAdmin._op_getAdminPrivileges.invoke(self, ((user, ), _ctx))

        """
        Gets the light administrator privileges for the given user.
        Arguments:
        user -- the user whose privileges are being queried
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getAdminPrivileges(self, user, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getAdminPrivileges.begin(self, ((user, ), _response, _ex, _sent, _ctx))

        """
        Gets the light administrator privileges for the given user.
        Arguments:
        user -- the user whose privileges are being queried
        Returns: the user's light administrator privileges
        """
        def end_getAdminPrivileges(self, _r):
            return _M_ode.api.IAdmin._op_getAdminPrivileges.end(self, _r)

        """
        Gets the administrators who have all the given privileges.
        Consistent with the results from "getAdminPrivileges".
        Arguments:
        privileges -- the required privileges
        _ctx -- The request context for the invocation.
        Returns: the light administrators who have those privileges
        """
        def getAdminsWithPrivileges(self, privileges, _ctx=None):
            return _M_ode.api.IAdmin._op_getAdminsWithPrivileges.invoke(self, ((privileges, ), _ctx))

        """
        Gets the administrators who have all the given privileges.
        Consistent with the results from "getAdminPrivileges".
        Arguments:
        privileges -- the required privileges
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getAdminsWithPrivileges(self, privileges, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getAdminsWithPrivileges.begin(self, ((privileges, ), _response, _ex, _sent, _ctx))

        """
        Gets the administrators who have all the given privileges.
        Consistent with the results from "getAdminPrivileges".
        Arguments:
        privileges -- the required privileges
        Returns: the light administrators who have those privileges
        """
        def end_getAdminsWithPrivileges(self, _r):
            return _M_ode.api.IAdmin._op_getAdminsWithPrivileges.end(self, _r)

        """
        Allows a user to update his/her own information. This is
        limited to the fields on Experimenter, all other fields
        (groups, etc.) are ignored. The experimenter argument need
        not have the proper id nor the proper odeName (which is
        immutable). To change the users default group (which is the
        only other customizable option), use
        {@code setDefaultGroup}
        Arguments:
        experimenter -- 
        _ctx -- The request context for the invocation.
        """
        def updateSelf(self, experimenter, _ctx=None):
            return _M_ode.api.IAdmin._op_updateSelf.invoke(self, ((experimenter, ), _ctx))

        """
        Allows a user to update his/her own information. This is
        limited to the fields on Experimenter, all other fields
        (groups, etc.) are ignored. The experimenter argument need
        not have the proper id nor the proper odeName (which is
        immutable). To change the users default group (which is the
        only other customizable option), use
        {@code setDefaultGroup}
        Arguments:
        experimenter -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_updateSelf(self, experimenter, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_updateSelf.begin(self, ((experimenter, ), _response, _ex, _sent, _ctx))

        """
        Allows a user to update his/her own information. This is
        limited to the fields on Experimenter, all other fields
        (groups, etc.) are ignored. The experimenter argument need
        not have the proper id nor the proper odeName (which is
        immutable). To change the users default group (which is the
        only other customizable option), use
        {@code setDefaultGroup}
        Arguments:
        experimenter -- 
        """
        def end_updateSelf(self, _r):
            return _M_ode.api.IAdmin._op_updateSelf.end(self, _r)

        """
        Uploads a photo for the user which will be displayed on
        his/her profile.
        This photo will be saved as an
        ode.model.OriginalFile object with the given
        format, and attached to the user's
        ode.model.Experimenter object via an
        ode.model.FileAnnotation with
        the namespace:
        bhojpur.net/ode/experimenter/photo
        (NSEXPERIMENTERPHOTO).
        If such an ode.model.OriginalFile instance
        already exists, it will be overwritten. If more than one
        photo is present, the oldest version will be modified (i.e.
        the highest updateEvent id).
        this photo will be placed in the user group and
        therefore will be visible to everyone on the system.
        Arguments:
        filename -- Not null. String name which will be used.
        format -- Not null. Format.value string. 'image/jpeg' and 'image/png' are common values.
        data -- Not null. Data from the image. This will be written to disk.
        _ctx -- The request context for the invocation.
        Returns: the id of the overwritten or newly created user photo OriginalFile object.
        """
        def uploadMyUserPhoto(self, filename, format, data, _ctx=None):
            return _M_ode.api.IAdmin._op_uploadMyUserPhoto.invoke(self, ((filename, format, data), _ctx))

        """
        Uploads a photo for the user which will be displayed on
        his/her profile.
        This photo will be saved as an
        ode.model.OriginalFile object with the given
        format, and attached to the user's
        ode.model.Experimenter object via an
        ode.model.FileAnnotation with
        the namespace:
        bhojpur.net/ode/experimenter/photo
        (NSEXPERIMENTERPHOTO).
        If such an ode.model.OriginalFile instance
        already exists, it will be overwritten. If more than one
        photo is present, the oldest version will be modified (i.e.
        the highest updateEvent id).
        this photo will be placed in the user group and
        therefore will be visible to everyone on the system.
        Arguments:
        filename -- Not null. String name which will be used.
        format -- Not null. Format.value string. 'image/jpeg' and 'image/png' are common values.
        data -- Not null. Data from the image. This will be written to disk.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_uploadMyUserPhoto(self, filename, format, data, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_uploadMyUserPhoto.begin(self, ((filename, format, data), _response, _ex, _sent, _ctx))

        """
        Uploads a photo for the user which will be displayed on
        his/her profile.
        This photo will be saved as an
        ode.model.OriginalFile object with the given
        format, and attached to the user's
        ode.model.Experimenter object via an
        ode.model.FileAnnotation with
        the namespace:
        bhojpur.net/ode/experimenter/photo
        (NSEXPERIMENTERPHOTO).
        If such an ode.model.OriginalFile instance
        already exists, it will be overwritten. If more than one
        photo is present, the oldest version will be modified (i.e.
        the highest updateEvent id).
        this photo will be placed in the user group and
        therefore will be visible to everyone on the system.
        Arguments:
        filename -- Not null. String name which will be used.
        format -- Not null. Format.value string. 'image/jpeg' and 'image/png' are common values.
        data -- Not null. Data from the image. This will be written to disk.
        Returns: the id of the overwritten or newly created user photo OriginalFile object.
        """
        def end_uploadMyUserPhoto(self, _r):
            return _M_ode.api.IAdmin._op_uploadMyUserPhoto.end(self, _r)

        """
        Retrieves the ode.model.OriginalFile object
        attached to this user as specified by
        {@code uploadMyUserPhoto}.
        The return value is order by the most recently modified
        file first.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: file objects. Possibly empty.
        """
        def getMyUserPhotos(self, _ctx=None):
            return _M_ode.api.IAdmin._op_getMyUserPhotos.invoke(self, ((), _ctx))

        """
        Retrieves the ode.model.OriginalFile object
        attached to this user as specified by
        {@code uploadMyUserPhoto}.
        The return value is order by the most recently modified
        file first.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMyUserPhotos(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getMyUserPhotos.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves the ode.model.OriginalFile object
        attached to this user as specified by
        {@code uploadMyUserPhoto}.
        The return value is order by the most recently modified
        file first.
        Arguments:
        Returns: file objects. Possibly empty.
        """
        def end_getMyUserPhotos(self, _r):
            return _M_ode.api.IAdmin._op_getMyUserPhotos.end(self, _r)

        """
        Updates an experimenter if admin or owner of group. Only
        string fields on the object are taken into account.
        The root and guest experimenters may not be renamed.
        Before a SecurityViolation would be thrown, however, this
        method will pass to {@code #updateSelf} if the
        current user matches the given experimenter.
        Arguments:
        experimenter -- the Experimenter to update.
        _ctx -- The request context for the invocation.
        """
        def updateExperimenter(self, experimenter, _ctx=None):
            return _M_ode.api.IAdmin._op_updateExperimenter.invoke(self, ((experimenter, ), _ctx))

        """
        Updates an experimenter if admin or owner of group. Only
        string fields on the object are taken into account.
        The root and guest experimenters may not be renamed.
        Before a SecurityViolation would be thrown, however, this
        method will pass to {@code #updateSelf} if the
        current user matches the given experimenter.
        Arguments:
        experimenter -- the Experimenter to update.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_updateExperimenter(self, experimenter, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_updateExperimenter.begin(self, ((experimenter, ), _response, _ex, _sent, _ctx))

        """
        Updates an experimenter if admin or owner of group. Only
        string fields on the object are taken into account.
        The root and guest experimenters may not be renamed.
        Before a SecurityViolation would be thrown, however, this
        method will pass to {@code #updateSelf} if the
        current user matches the given experimenter.
        Arguments:
        experimenter -- the Experimenter to update.
        """
        def end_updateExperimenter(self, _r):
            return _M_ode.api.IAdmin._op_updateExperimenter.end(self, _r)

        """
        Updates an experimenter if admin or owner of group.
        Only string fields on the object are taken into account.
        The root and guest experimenters may not be renamed.
        Arguments:
        experimenter -- the Experimenter to update.
        password -- Not-null. Must pass validation in the security sub-system.
        _ctx -- The request context for the invocation.
        """
        def updateExperimenterWithPassword(self, experimenter, password, _ctx=None):
            return _M_ode.api.IAdmin._op_updateExperimenterWithPassword.invoke(self, ((experimenter, password), _ctx))

        """
        Updates an experimenter if admin or owner of group.
        Only string fields on the object are taken into account.
        The root and guest experimenters may not be renamed.
        Arguments:
        experimenter -- the Experimenter to update.
        password -- Not-null. Must pass validation in the security sub-system.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_updateExperimenterWithPassword(self, experimenter, password, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_updateExperimenterWithPassword.begin(self, ((experimenter, password), _response, _ex, _sent, _ctx))

        """
        Updates an experimenter if admin or owner of group.
        Only string fields on the object are taken into account.
        The root and guest experimenters may not be renamed.
        Arguments:
        experimenter -- the Experimenter to update.
        password -- Not-null. Must pass validation in the security sub-system.
        """
        def end_updateExperimenterWithPassword(self, _r):
            return _M_ode.api.IAdmin._op_updateExperimenterWithPassword.end(self, _r)

        """
        Updates an experimenter group if admin or owner of group.
        Only string fields on the object are taken into account.
        The root, system and guest groups may not be renamed,
        nor may the user's current group.
        Arguments:
        group -- the ExperimenterGroup to update.
        _ctx -- The request context for the invocation.
        """
        def updateGroup(self, group, _ctx=None):
            return _M_ode.api.IAdmin._op_updateGroup.invoke(self, ((group, ), _ctx))

        """
        Updates an experimenter group if admin or owner of group.
        Only string fields on the object are taken into account.
        The root, system and guest groups may not be renamed,
        nor may the user's current group.
        Arguments:
        group -- the ExperimenterGroup to update.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_updateGroup(self, group, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_updateGroup.begin(self, ((group, ), _response, _ex, _sent, _ctx))

        """
        Updates an experimenter group if admin or owner of group.
        Only string fields on the object are taken into account.
        The root, system and guest groups may not be renamed,
        nor may the user's current group.
        Arguments:
        group -- the ExperimenterGroup to update.
        """
        def end_updateGroup(self, _r):
            return _M_ode.api.IAdmin._op_updateGroup.end(self, _r)

        """
        Creates and returns a new user. This user will be created
        with the default group specified.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        group -- group name of the default group for this user
        _ctx -- The request context for the invocation.
        Returns: id of the newly created ode.model.Experimenter
        """
        def createUser(self, experimenter, group, _ctx=None):
            return _M_ode.api.IAdmin._op_createUser.invoke(self, ((experimenter, group), _ctx))

        """
        Creates and returns a new user. This user will be created
        with the default group specified.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        group -- group name of the default group for this user
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createUser(self, experimenter, group, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_createUser.begin(self, ((experimenter, group), _response, _ex, _sent, _ctx))

        """
        Creates and returns a new user. This user will be created
        with the default group specified.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        group -- group name of the default group for this user
        Returns: id of the newly created ode.model.Experimenter
        """
        def end_createUser(self, _r):
            return _M_ode.api.IAdmin._op_createUser.end(self, _r)

        """
        Creates and returns a new system user. This user will be
        created with the System (administration) group as
        default and will also be in the user group.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        _ctx -- The request context for the invocation.
        Returns: id of the newly created ode.model.Experimenter
        """
        def createSystemUser(self, experimenter, _ctx=None):
            return _M_ode.api.IAdmin._op_createSystemUser.invoke(self, ((experimenter, ), _ctx))

        """
        Creates and returns a new system user. This user will be
        created with the System (administration) group as
        default and will also be in the user group.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createSystemUser(self, experimenter, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_createSystemUser.begin(self, ((experimenter, ), _response, _ex, _sent, _ctx))

        """
        Creates and returns a new system user. This user will be
        created with the System (administration) group as
        default and will also be in the user group.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        Returns: id of the newly created ode.model.Experimenter
        """
        def end_createSystemUser(self, _r):
            return _M_ode.api.IAdmin._op_createSystemUser.end(self, _r)

        """
        Creates and returns a new system user. This user will be
        created with the System (administration) group as
        default and will also be in the user group. Their
        light administrator privileges will be set as given.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        privileges -- the privileges to set for the user
        _ctx -- The request context for the invocation.
        Returns: id of the newly created ode.model.Experimenter
        """
        def createRestrictedSystemUser(self, experimenter, privileges, _ctx=None):
            return _M_ode.api.IAdmin._op_createRestrictedSystemUser.invoke(self, ((experimenter, privileges), _ctx))

        """
        Creates and returns a new system user. This user will be
        created with the System (administration) group as
        default and will also be in the user group. Their
        light administrator privileges will be set as given.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        privileges -- the privileges to set for the user
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createRestrictedSystemUser(self, experimenter, privileges, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_createRestrictedSystemUser.begin(self, ((experimenter, privileges), _response, _ex, _sent, _ctx))

        """
        Creates and returns a new system user. This user will be
        created with the System (administration) group as
        default and will also be in the user group. Their
        light administrator privileges will be set as given.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        privileges -- the privileges to set for the user
        Returns: id of the newly created ode.model.Experimenter
        """
        def end_createRestrictedSystemUser(self, _r):
            return _M_ode.api.IAdmin._op_createRestrictedSystemUser.end(self, _r)

        """
        Creates and returns a new system user. This user will be
        created with the System (administration) group as
        default and will also be in the user group. Their
        light administrator privileges and password will be set
        as given.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        privileges -- the privileges to set for the user
        password -- Not-null. Must pass validation in the security sub-system.
        _ctx -- The request context for the invocation.
        Returns: id of the newly created ode.model.Experimenter
        """
        def createRestrictedSystemUserWithPassword(self, experimenter, privileges, password, _ctx=None):
            return _M_ode.api.IAdmin._op_createRestrictedSystemUserWithPassword.invoke(self, ((experimenter, privileges, password), _ctx))

        """
        Creates and returns a new system user. This user will be
        created with the System (administration) group as
        default and will also be in the user group. Their
        light administrator privileges and password will be set
        as given.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        privileges -- the privileges to set for the user
        password -- Not-null. Must pass validation in the security sub-system.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createRestrictedSystemUserWithPassword(self, experimenter, privileges, password, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_createRestrictedSystemUserWithPassword.begin(self, ((experimenter, privileges, password), _response, _ex, _sent, _ctx))

        """
        Creates and returns a new system user. This user will be
        created with the System (administration) group as
        default and will also be in the user group. Their
        light administrator privileges and password will be set
        as given.
        Arguments:
        experimenter -- a new ode.model.Experimenter instance
        privileges -- the privileges to set for the user
        password -- Not-null. Must pass validation in the security sub-system.
        Returns: id of the newly created ode.model.Experimenter
        """
        def end_createRestrictedSystemUserWithPassword(self, _r):
            return _M_ode.api.IAdmin._op_createRestrictedSystemUserWithPassword.end(self, _r)

        """
        Creates and returns a new user in the given groups.
        Arguments:
        user -- A new ode.model.Experimenter instance. Not null.
        defaultGroup -- Instance of ode.model.ExperimenterGroup. Not null.
        groups -- Array of ode.model.ExperimenterGroup instances. Can be null.
        _ctx -- The request context for the invocation.
        Returns: id of the newly created ode.model.Experimenter Not null.
        """
        def createExperimenter(self, user, defaultGroup, groups, _ctx=None):
            return _M_ode.api.IAdmin._op_createExperimenter.invoke(self, ((user, defaultGroup, groups), _ctx))

        """
        Creates and returns a new user in the given groups.
        Arguments:
        user -- A new ode.model.Experimenter instance. Not null.
        defaultGroup -- Instance of ode.model.ExperimenterGroup. Not null.
        groups -- Array of ode.model.ExperimenterGroup instances. Can be null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createExperimenter(self, user, defaultGroup, groups, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_createExperimenter.begin(self, ((user, defaultGroup, groups), _response, _ex, _sent, _ctx))

        """
        Creates and returns a new user in the given groups.
        Arguments:
        user -- A new ode.model.Experimenter instance. Not null.
        defaultGroup -- Instance of ode.model.ExperimenterGroup. Not null.
        groups -- Array of ode.model.ExperimenterGroup instances. Can be null.
        Returns: id of the newly created ode.model.Experimenter Not null.
        """
        def end_createExperimenter(self, _r):
            return _M_ode.api.IAdmin._op_createExperimenter.end(self, _r)

        """
        Creates and returns a new user in the given groups with
        password.
        Arguments:
        user -- A new ode.model.Experimenter instance. Not null.
        password -- Not-null. Must pass validation in the security sub-system.
        defaultGroup -- Instance of ode.model.ExperimenterGroup. Not null.
        groups -- Array of ode.model.ExperimenterGroup instances. Can be null.
        _ctx -- The request context for the invocation.
        Returns: id of the newly created ode.model.Experimenter Not null.
        Throws:
        SecurityViolation -- if the new password is too weak.
        """
        def createExperimenterWithPassword(self, user, password, defaultGroup, groups, _ctx=None):
            return _M_ode.api.IAdmin._op_createExperimenterWithPassword.invoke(self, ((user, password, defaultGroup, groups), _ctx))

        """
        Creates and returns a new user in the given groups with
        password.
        Arguments:
        user -- A new ode.model.Experimenter instance. Not null.
        password -- Not-null. Must pass validation in the security sub-system.
        defaultGroup -- Instance of ode.model.ExperimenterGroup. Not null.
        groups -- Array of ode.model.ExperimenterGroup instances. Can be null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createExperimenterWithPassword(self, user, password, defaultGroup, groups, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_createExperimenterWithPassword.begin(self, ((user, password, defaultGroup, groups), _response, _ex, _sent, _ctx))

        """
        Creates and returns a new user in the given groups with
        password.
        Arguments:
        user -- A new ode.model.Experimenter instance. Not null.
        password -- Not-null. Must pass validation in the security sub-system.
        defaultGroup -- Instance of ode.model.ExperimenterGroup. Not null.
        groups -- Array of ode.model.ExperimenterGroup instances. Can be null.
        Returns: id of the newly created ode.model.Experimenter Not null.
        Throws:
        SecurityViolation -- if the new password is too weak.
        """
        def end_createExperimenterWithPassword(self, _r):
            return _M_ode.api.IAdmin._op_createExperimenterWithPassword.end(self, _r)

        """
        Creates and returns a new group. The
        {@code ode.model.Details.setPermissions} method should be
        called on the instance which is passed. The given
        ode.model.Permissions will become the default for
        all objects created while logged into this group, possibly
        modified by the user's umask settings.
        If no permissions is set, the default will be
        {@code ode.model.Permissions.USER_PRIVATE},
        i.e. a group in which no user can see the other group
        member's data.
        Arguments:
        group -- a new ode.model.ExperimenterGroup instance. Not null.
        _ctx -- The request context for the invocation.
        Returns: id of the newly created ode.model.ExperimenterGroup
        """
        def createGroup(self, group, _ctx=None):
            return _M_ode.api.IAdmin._op_createGroup.invoke(self, ((group, ), _ctx))

        """
        Creates and returns a new group. The
        {@code ode.model.Details.setPermissions} method should be
        called on the instance which is passed. The given
        ode.model.Permissions will become the default for
        all objects created while logged into this group, possibly
        modified by the user's umask settings.
        If no permissions is set, the default will be
        {@code ode.model.Permissions.USER_PRIVATE},
        i.e. a group in which no user can see the other group
        member's data.
        Arguments:
        group -- a new ode.model.ExperimenterGroup instance. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createGroup(self, group, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_createGroup.begin(self, ((group, ), _response, _ex, _sent, _ctx))

        """
        Creates and returns a new group. The
        {@code ode.model.Details.setPermissions} method should be
        called on the instance which is passed. The given
        ode.model.Permissions will become the default for
        all objects created while logged into this group, possibly
        modified by the user's umask settings.
        If no permissions is set, the default will be
        {@code ode.model.Permissions.USER_PRIVATE},
        i.e. a group in which no user can see the other group
        member's data.
        Arguments:
        group -- a new ode.model.ExperimenterGroup instance. Not null.
        Returns: id of the newly created ode.model.ExperimenterGroup
        """
        def end_createGroup(self, _r):
            return _M_ode.api.IAdmin._op_createGroup.end(self, _r)

        """
        Adds a user to the given groups.
        Arguments:
        user -- A currently managed entity. Not null.
        groups -- Groups to which the user will be added. Not null.
        _ctx -- The request context for the invocation.
        """
        def addGroups(self, user, groups, _ctx=None):
            return _M_ode.api.IAdmin._op_addGroups.invoke(self, ((user, groups), _ctx))

        """
        Adds a user to the given groups.
        Arguments:
        user -- A currently managed entity. Not null.
        groups -- Groups to which the user will be added. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addGroups(self, user, groups, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_addGroups.begin(self, ((user, groups), _response, _ex, _sent, _ctx))

        """
        Adds a user to the given groups.
        Arguments:
        user -- A currently managed entity. Not null.
        groups -- Groups to which the user will be added. Not null.
        """
        def end_addGroups(self, _r):
            return _M_ode.api.IAdmin._op_addGroups.end(self, _r)

        """
        Removes an experimenter from the given groups.
        The root experimenter is required to be in both the
        user and system groups.
        An experimenter may not remove themselves from the user
        or system group.
        An experimenter may not be a member of only the user
        group, some other group is also required as the default
        group.
        An experimenter must remain a member of some group.
        Arguments:
        user -- A currently managed entity. Not null.
        groups -- Groups from which the user will be removed. Not null.
        _ctx -- The request context for the invocation.
        """
        def removeGroups(self, user, groups, _ctx=None):
            return _M_ode.api.IAdmin._op_removeGroups.invoke(self, ((user, groups), _ctx))

        """
        Removes an experimenter from the given groups.
        The root experimenter is required to be in both the
        user and system groups.
        An experimenter may not remove themselves from the user
        or system group.
        An experimenter may not be a member of only the user
        group, some other group is also required as the default
        group.
        An experimenter must remain a member of some group.
        Arguments:
        user -- A currently managed entity. Not null.
        groups -- Groups from which the user will be removed. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeGroups(self, user, groups, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_removeGroups.begin(self, ((user, groups), _response, _ex, _sent, _ctx))

        """
        Removes an experimenter from the given groups.
        The root experimenter is required to be in both the
        user and system groups.
        An experimenter may not remove themselves from the user
        or system group.
        An experimenter may not be a member of only the user
        group, some other group is also required as the default
        group.
        An experimenter must remain a member of some group.
        Arguments:
        user -- A currently managed entity. Not null.
        groups -- Groups from which the user will be removed. Not null.
        """
        def end_removeGroups(self, _r):
            return _M_ode.api.IAdmin._op_removeGroups.end(self, _r)

        """
        Sets the default group for a given user.
        Arguments:
        user -- A currently managed ode.model.Experimenter. Not null.
        group -- The group which should be set as default group for this user. Not null.
        _ctx -- The request context for the invocation.
        """
        def setDefaultGroup(self, user, group, _ctx=None):
            return _M_ode.api.IAdmin._op_setDefaultGroup.invoke(self, ((user, group), _ctx))

        """
        Sets the default group for a given user.
        Arguments:
        user -- A currently managed ode.model.Experimenter. Not null.
        group -- The group which should be set as default group for this user. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setDefaultGroup(self, user, group, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_setDefaultGroup.begin(self, ((user, group), _response, _ex, _sent, _ctx))

        """
        Sets the default group for a given user.
        Arguments:
        user -- A currently managed ode.model.Experimenter. Not null.
        group -- The group which should be set as default group for this user. Not null.
        """
        def end_setDefaultGroup(self, _r):
            return _M_ode.api.IAdmin._op_setDefaultGroup.end(self, _r)

        """
        Adds the user to the owner list for this group.
        Since Beta 4.2
        multiple users can be the owner of a group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not null.
        owner -- A currently managed ode.model.Experimenter. Not null.
        _ctx -- The request context for the invocation.
        """
        def setGroupOwner(self, group, owner, _ctx=None):
            return _M_ode.api.IAdmin._op_setGroupOwner.invoke(self, ((group, owner), _ctx))

        """
        Adds the user to the owner list for this group.
        Since Beta 4.2
        multiple users can be the owner of a group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not null.
        owner -- A currently managed ode.model.Experimenter. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setGroupOwner(self, group, owner, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_setGroupOwner.begin(self, ((group, owner), _response, _ex, _sent, _ctx))

        """
        Adds the user to the owner list for this group.
        Since Beta 4.2
        multiple users can be the owner of a group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not null.
        owner -- A currently managed ode.model.Experimenter. Not null.
        """
        def end_setGroupOwner(self, _r):
            return _M_ode.api.IAdmin._op_setGroupOwner.end(self, _r)

        """
        Removes the user from the owner list for this group.
        Since Beta 4.2
        multiple users can be the owner of a group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not null.
        owner -- A currently managed ode.model.Experimenter. Not null.
        _ctx -- The request context for the invocation.
        """
        def unsetGroupOwner(self, group, owner, _ctx=None):
            return _M_ode.api.IAdmin._op_unsetGroupOwner.invoke(self, ((group, owner), _ctx))

        """
        Removes the user from the owner list for this group.
        Since Beta 4.2
        multiple users can be the owner of a group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not null.
        owner -- A currently managed ode.model.Experimenter. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_unsetGroupOwner(self, group, owner, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_unsetGroupOwner.begin(self, ((group, owner), _response, _ex, _sent, _ctx))

        """
        Removes the user from the owner list for this group.
        Since Beta 4.2
        multiple users can be the owner of a group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not null.
        owner -- A currently managed ode.model.Experimenter. Not null.
        """
        def end_unsetGroupOwner(self, _r):
            return _M_ode.api.IAdmin._op_unsetGroupOwner.end(self, _r)

        """
        Adds the given users to the owner list for this group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not null.
        owners -- A set of currently managed ode.model.Experimenters. Not null.
        _ctx -- The request context for the invocation.
        """
        def addGroupOwners(self, group, owners, _ctx=None):
            return _M_ode.api.IAdmin._op_addGroupOwners.invoke(self, ((group, owners), _ctx))

        """
        Adds the given users to the owner list for this group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not null.
        owners -- A set of currently managed ode.model.Experimenters. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addGroupOwners(self, group, owners, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_addGroupOwners.begin(self, ((group, owners), _response, _ex, _sent, _ctx))

        """
        Adds the given users to the owner list for this group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not null.
        owners -- A set of currently managed ode.model.Experimenters. Not null.
        """
        def end_addGroupOwners(self, _r):
            return _M_ode.api.IAdmin._op_addGroupOwners.end(self, _r)

        """
        removes the given users from the owner list for this group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not
        owners -- A set of currently managed ode.model.Experimenters. Not null.
        _ctx -- The request context for the invocation.
        """
        def removeGroupOwners(self, group, owners, _ctx=None):
            return _M_ode.api.IAdmin._op_removeGroupOwners.invoke(self, ((group, owners), _ctx))

        """
        removes the given users from the owner list for this group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not
        owners -- A set of currently managed ode.model.Experimenters. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeGroupOwners(self, group, owners, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_removeGroupOwners.begin(self, ((group, owners), _response, _ex, _sent, _ctx))

        """
        removes the given users from the owner list for this group.
        Arguments:
        group -- A currently managed ode.model.ExperimenterGroup. Not
        owners -- A set of currently managed ode.model.Experimenters. Not null.
        """
        def end_removeGroupOwners(self, _r):
            return _M_ode.api.IAdmin._op_removeGroupOwners.end(self, _r)

        """
        Removes a user by removing the password information for
        that user as well as all
        ode.model.GroupExperimenterMap instances.
        Arguments:
        user -- Experimenter to be deleted. Not null.
        _ctx -- The request context for the invocation.
        """
        def deleteExperimenter(self, user, _ctx=None):
            return _M_ode.api.IAdmin._op_deleteExperimenter.invoke(self, ((user, ), _ctx))

        """
        Removes a user by removing the password information for
        that user as well as all
        ode.model.GroupExperimenterMap instances.
        Arguments:
        user -- Experimenter to be deleted. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_deleteExperimenter(self, user, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_deleteExperimenter.begin(self, ((user, ), _response, _ex, _sent, _ctx))

        """
        Removes a user by removing the password information for
        that user as well as all
        ode.model.GroupExperimenterMap instances.
        Arguments:
        user -- Experimenter to be deleted. Not null.
        """
        def end_deleteExperimenter(self, _r):
            return _M_ode.api.IAdmin._op_deleteExperimenter.end(self, _r)

        """
        Removes a group by first removing all users in the group,
        and then deleting the actual
        ode.model.ExperimenterGroup instance.
        Arguments:
        group -- ode.model.ExperimenterGroup to be deleted. Not null.
        _ctx -- The request context for the invocation.
        """
        def deleteGroup(self, group, _ctx=None):
            return _M_ode.api.IAdmin._op_deleteGroup.invoke(self, ((group, ), _ctx))

        """
        Removes a group by first removing all users in the group,
        and then deleting the actual
        ode.model.ExperimenterGroup instance.
        Arguments:
        group -- ode.model.ExperimenterGroup to be deleted. Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_deleteGroup(self, group, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_deleteGroup.begin(self, ((group, ), _response, _ex, _sent, _ctx))

        """
        Removes a group by first removing all users in the group,
        and then deleting the actual
        ode.model.ExperimenterGroup instance.
        Arguments:
        group -- ode.model.ExperimenterGroup to be deleted. Not null.
        """
        def end_deleteGroup(self, _r):
            return _M_ode.api.IAdmin._op_deleteGroup.end(self, _r)

        def changeOwner(self, obj, odeName, _ctx=None):
            return _M_ode.api.IAdmin._op_changeOwner.invoke(self, ((obj, odeName), _ctx))

        def begin_changeOwner(self, obj, odeName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_changeOwner.begin(self, ((obj, odeName), _response, _ex, _sent, _ctx))

        def end_changeOwner(self, _r):
            return _M_ode.api.IAdmin._op_changeOwner.end(self, _r)

        def changeGroup(self, obj, odeName, _ctx=None):
            return _M_ode.api.IAdmin._op_changeGroup.invoke(self, ((obj, odeName), _ctx))

        def begin_changeGroup(self, obj, odeName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_changeGroup.begin(self, ((obj, odeName), _response, _ex, _sent, _ctx))

        def end_changeGroup(self, _r):
            return _M_ode.api.IAdmin._op_changeGroup.end(self, _r)

        def changePermissions(self, obj, perms, _ctx=None):
            return _M_ode.api.IAdmin._op_changePermissions.invoke(self, ((obj, perms), _ctx))

        def begin_changePermissions(self, obj, perms, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_changePermissions.begin(self, ((obj, perms), _response, _ex, _sent, _ctx))

        def end_changePermissions(self, _r):
            return _M_ode.api.IAdmin._op_changePermissions.end(self, _r)

        """
        Moves the given objects into the user group to make
        them visible and linkable from all security contexts.
        Arguments:
        objects -- 
        _ctx -- The request context for the invocation.
        """
        def moveToCommonSpace(self, objects, _ctx=None):
            return _M_ode.api.IAdmin._op_moveToCommonSpace.invoke(self, ((objects, ), _ctx))

        """
        Moves the given objects into the user group to make
        them visible and linkable from all security contexts.
        Arguments:
        objects -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_moveToCommonSpace(self, objects, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_moveToCommonSpace.begin(self, ((objects, ), _response, _ex, _sent, _ctx))

        """
        Moves the given objects into the user group to make
        them visible and linkable from all security contexts.
        Arguments:
        objects -- 
        """
        def end_moveToCommonSpace(self, _r):
            return _M_ode.api.IAdmin._op_moveToCommonSpace.end(self, _r)

        """
        Sets the set of light administrator privileges for the given user.
        Arguments:
        user -- the user whose privileges are to be set
        privileges -- the privileges to set for the user
        _ctx -- The request context for the invocation.
        """
        def setAdminPrivileges(self, user, privileges, _ctx=None):
            return _M_ode.api.IAdmin._op_setAdminPrivileges.invoke(self, ((user, privileges), _ctx))

        """
        Sets the set of light administrator privileges for the given user.
        Arguments:
        user -- the user whose privileges are to be set
        privileges -- the privileges to set for the user
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setAdminPrivileges(self, user, privileges, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_setAdminPrivileges.begin(self, ((user, privileges), _response, _ex, _sent, _ctx))

        """
        Sets the set of light administrator privileges for the given user.
        Arguments:
        user -- the user whose privileges are to be set
        privileges -- the privileges to set for the user
        """
        def end_setAdminPrivileges(self, _r):
            return _M_ode.api.IAdmin._op_setAdminPrivileges.end(self, _r)

        """
        Changes the password for the current user.
        Warning:This method requires the user to be
        authenticated with a password and not with a one-time
        session id. To avoid this problem, use
        {@code changePasswordWithOldPassword}.
        Arguments:
        newPassword -- Possibly null to allow logging in with no password.
        _ctx -- The request context for the invocation.
        Throws:
        SecurityViolation -- if the user is not authenticated with a password.
        """
        def changePassword(self, newPassword, _ctx=None):
            return _M_ode.api.IAdmin._op_changePassword.invoke(self, ((newPassword, ), _ctx))

        """
        Changes the password for the current user.
        Warning:This method requires the user to be
        authenticated with a password and not with a one-time
        session id. To avoid this problem, use
        {@code changePasswordWithOldPassword}.
        Arguments:
        newPassword -- Possibly null to allow logging in with no password.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_changePassword(self, newPassword, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_changePassword.begin(self, ((newPassword, ), _response, _ex, _sent, _ctx))

        """
        Changes the password for the current user.
        Warning:This method requires the user to be
        authenticated with a password and not with a one-time
        session id. To avoid this problem, use
        {@code changePasswordWithOldPassword}.
        Arguments:
        newPassword -- Possibly null to allow logging in with no password.
        Throws:
        SecurityViolation -- if the user is not authenticated with a password.
        """
        def end_changePassword(self, _r):
            return _M_ode.api.IAdmin._op_changePassword.end(self, _r)

        """
        Changes the password for the current user by passing the
        old password.
        Arguments:
        oldPassword -- Not-null. Must pass validation in the security sub-system.
        newPassword -- Possibly null to allow logging in with no password.
        _ctx -- The request context for the invocation.
        Throws:
        SecurityViolation -- if the oldPassword is incorrect.
        """
        def changePasswordWithOldPassword(self, oldPassword, newPassword, _ctx=None):
            return _M_ode.api.IAdmin._op_changePasswordWithOldPassword.invoke(self, ((oldPassword, newPassword), _ctx))

        """
        Changes the password for the current user by passing the
        old password.
        Arguments:
        oldPassword -- Not-null. Must pass validation in the security sub-system.
        newPassword -- Possibly null to allow logging in with no password.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_changePasswordWithOldPassword(self, oldPassword, newPassword, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_changePasswordWithOldPassword.begin(self, ((oldPassword, newPassword), _response, _ex, _sent, _ctx))

        """
        Changes the password for the current user by passing the
        old password.
        Arguments:
        oldPassword -- Not-null. Must pass validation in the security sub-system.
        newPassword -- Possibly null to allow logging in with no password.
        Throws:
        SecurityViolation -- if the oldPassword is incorrect.
        """
        def end_changePasswordWithOldPassword(self, _r):
            return _M_ode.api.IAdmin._op_changePasswordWithOldPassword.end(self, _r)

        """
        Changes the password for the a given user.
        Arguments:
        odeName -- 
        newPassword -- Not-null. Might must pass validation in the security sub-system.
        _ctx -- The request context for the invocation.
        Throws:
        SecurityViolation -- if the new password is too weak.
        """
        def changeUserPassword(self, odeName, newPassword, _ctx=None):
            return _M_ode.api.IAdmin._op_changeUserPassword.invoke(self, ((odeName, newPassword), _ctx))

        """
        Changes the password for the a given user.
        Arguments:
        odeName -- 
        newPassword -- Not-null. Might must pass validation in the security sub-system.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_changeUserPassword(self, odeName, newPassword, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_changeUserPassword.begin(self, ((odeName, newPassword), _response, _ex, _sent, _ctx))

        """
        Changes the password for the a given user.
        Arguments:
        odeName -- 
        newPassword -- Not-null. Might must pass validation in the security sub-system.
        Throws:
        SecurityViolation -- if the new password is too weak.
        """
        def end_changeUserPassword(self, _r):
            return _M_ode.api.IAdmin._op_changeUserPassword.end(self, _r)

        """
        Uses JMX to refresh the login cache if supported.
        Some backends may not provide refreshing. This may be
        called internally during some other administrative tasks.
        The exact implementation of this depends on the application
        server and the authentication/authorization backend.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def synchronizeLoginCache(self, _ctx=None):
            return _M_ode.api.IAdmin._op_synchronizeLoginCache.invoke(self, ((), _ctx))

        """
        Uses JMX to refresh the login cache if supported.
        Some backends may not provide refreshing. This may be
        called internally during some other administrative tasks.
        The exact implementation of this depends on the application
        server and the authentication/authorization backend.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_synchronizeLoginCache(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_synchronizeLoginCache.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Uses JMX to refresh the login cache if supported.
        Some backends may not provide refreshing. This may be
        called internally during some other administrative tasks.
        The exact implementation of this depends on the application
        server and the authentication/authorization backend.
        Arguments:
        """
        def end_synchronizeLoginCache(self, _r):
            return _M_ode.api.IAdmin._op_synchronizeLoginCache.end(self, _r)

        """
        Used after an ode.ExpiredCredentialException
        instance is thrown.
        Arguments:
        name -- 
        oldCred -- 
        newCred -- 
        _ctx -- The request context for the invocation.
        """
        def changeExpiredCredentials(self, name, oldCred, newCred, _ctx=None):
            return _M_ode.api.IAdmin._op_changeExpiredCredentials.invoke(self, ((name, oldCred, newCred), _ctx))

        """
        Used after an ode.ExpiredCredentialException
        instance is thrown.
        Arguments:
        name -- 
        oldCred -- 
        newCred -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_changeExpiredCredentials(self, name, oldCred, newCred, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_changeExpiredCredentials.begin(self, ((name, oldCred, newCred), _response, _ex, _sent, _ctx))

        """
        Used after an ode.ExpiredCredentialException
        instance is thrown.
        Arguments:
        name -- 
        oldCred -- 
        newCred -- 
        """
        def end_changeExpiredCredentials(self, _r):
            return _M_ode.api.IAdmin._op_changeExpiredCredentials.end(self, _r)

        def reportForgottenPassword(self, name, email, _ctx=None):
            return _M_ode.api.IAdmin._op_reportForgottenPassword.invoke(self, ((name, email), _ctx))

        def begin_reportForgottenPassword(self, name, email, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_reportForgottenPassword.begin(self, ((name, email), _response, _ex, _sent, _ctx))

        def end_reportForgottenPassword(self, _r):
            return _M_ode.api.IAdmin._op_reportForgottenPassword.end(self, _r)

        """
        Returns the active ode.sys.Roles in use by the
        server.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: Non-null, immutable ode.sys.Roles instance.
        """
        def getSecurityRoles(self, _ctx=None):
            return _M_ode.api.IAdmin._op_getSecurityRoles.invoke(self, ((), _ctx))

        """
        Returns the active ode.sys.Roles in use by the
        server.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getSecurityRoles(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getSecurityRoles.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the active ode.sys.Roles in use by the
        server.
        Arguments:
        Returns: Non-null, immutable ode.sys.Roles instance.
        """
        def end_getSecurityRoles(self, _r):
            return _M_ode.api.IAdmin._op_getSecurityRoles.end(self, _r)

        """
        Returns an implementation of ode.sys.EventContext
        loaded with the security for the current user and thread.
        If called remotely, not all values of
        ode.sys.EventContext will be sensible.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: Non-null, immutable ode.sys.EventContext instance
        """
        def getEventContext(self, _ctx=None):
            return _M_ode.api.IAdmin._op_getEventContext.invoke(self, ((), _ctx))

        """
        Returns an implementation of ode.sys.EventContext
        loaded with the security for the current user and thread.
        If called remotely, not all values of
        ode.sys.EventContext will be sensible.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getEventContext(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IAdmin._op_getEventContext.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns an implementation of ode.sys.EventContext
        loaded with the security for the current user and thread.
        If called remotely, not all values of
        ode.sys.EventContext will be sensible.
        Arguments:
        Returns: Non-null, immutable ode.sys.EventContext instance
        """
        def end_getEventContext(self, _r):
            return _M_ode.api.IAdmin._op_getEventContext.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IAdminPrx.ice_checkedCast(proxy, '::ode::api::IAdmin', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IAdminPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IAdmin'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IAdminPrx = IcePy.defineProxy('::ode::api::IAdmin', IAdminPrx)

    _M_ode.api._t_IAdmin = IcePy.defineClass('::ode::api::IAdmin', IAdmin, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IAdmin._ice_type = _M_ode.api._t_IAdmin

    IAdmin._op_canUpdate = IcePy.Operation('canUpdate', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_IObject, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_getExperimenter = IcePy.Operation('getExperimenter', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.model._t_Experimenter, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_lookupExperimenter = IcePy.Operation('lookupExperimenter', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.model._t_Experimenter, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_lookupExperimenters = IcePy.Operation('lookupExperimenters', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_ExperimenterList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_getGroup = IcePy.Operation('getGroup', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.model._t_ExperimenterGroup, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_lookupGroup = IcePy.Operation('lookupGroup', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.model._t_ExperimenterGroup, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_lookupGroups = IcePy.Operation('lookupGroups', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_ExperimenterGroupList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_containedExperimenters = IcePy.Operation('containedExperimenters', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_ExperimenterList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_containedGroups = IcePy.Operation('containedGroups', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_ExperimenterGroupList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_getDefaultGroup = IcePy.Operation('getDefaultGroup', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.model._t_ExperimenterGroup, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_lookupLdapAuthExperimenter = IcePy.Operation('lookupLdapAuthExperimenter', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_lookupLdapAuthExperimenters = IcePy.Operation('lookupLdapAuthExperimenters', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode._t_RList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_getMemberOfGroupIds = IcePy.Operation('getMemberOfGroupIds', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), ((), _M_ode.api._t_LongList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_getLeaderOfGroupIds = IcePy.Operation('getLeaderOfGroupIds', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), ((), _M_ode.api._t_LongList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_getCurrentAdminPrivileges = IcePy.Operation('getCurrentAdminPrivileges', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_AdminPrivilegeList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_getAdminPrivileges = IcePy.Operation('getAdminPrivileges', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), ((), _M_ode.api._t_AdminPrivilegeList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_getAdminsWithPrivileges = IcePy.Operation('getAdminsWithPrivileges', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.api._t_AdminPrivilegeList, False, 0),), (), ((), _M_ode.api._t_ExperimenterList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_updateSelf = IcePy.Operation('updateSelf', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_uploadMyUserPhoto = IcePy.Operation('uploadMyUserPhoto', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), _M_Ice._t_ByteSeq, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_getMyUserPhotos = IcePy.Operation('getMyUserPhotos', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_OriginalFileList, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_updateExperimenter = IcePy.Operation('updateExperimenter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_updateExperimenterWithPassword = IcePy.Operation('updateExperimenterWithPassword', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode._t_RString, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_updateGroup = IcePy.Operation('updateGroup', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0),), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_createUser = IcePy.Operation('createUser', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), IcePy._t_string, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_createSystemUser = IcePy.Operation('createSystemUser', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_createRestrictedSystemUser = IcePy.Operation('createRestrictedSystemUser', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode.api._t_AdminPrivilegeList, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_createRestrictedSystemUserWithPassword = IcePy.Operation('createRestrictedSystemUserWithPassword', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode.api._t_AdminPrivilegeList, False, 0), ((), _M_ode._t_RString, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_createExperimenter = IcePy.Operation('createExperimenter', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode.model._t_ExperimenterGroup, False, 0), ((), _M_ode.api._t_ExperimenterGroupList, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_createExperimenterWithPassword = IcePy.Operation('createExperimenterWithPassword', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode._t_RString, False, 0), ((), _M_ode.model._t_ExperimenterGroup, False, 0), ((), _M_ode.api._t_ExperimenterGroupList, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_createGroup = IcePy.Operation('createGroup', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0),), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_addGroups = IcePy.Operation('addGroups', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode.api._t_ExperimenterGroupList, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_removeGroups = IcePy.Operation('removeGroups', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode.api._t_ExperimenterGroupList, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_setDefaultGroup = IcePy.Operation('setDefaultGroup', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode.model._t_ExperimenterGroup, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_setGroupOwner = IcePy.Operation('setGroupOwner', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0), ((), _M_ode.model._t_Experimenter, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_unsetGroupOwner = IcePy.Operation('unsetGroupOwner', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0), ((), _M_ode.model._t_Experimenter, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_addGroupOwners = IcePy.Operation('addGroupOwners', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0), ((), _M_ode.api._t_ExperimenterList, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_removeGroupOwners = IcePy.Operation('removeGroupOwners', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0), ((), _M_ode.api._t_ExperimenterList, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_deleteExperimenter = IcePy.Operation('deleteExperimenter', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0),), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_deleteGroup = IcePy.Operation('deleteGroup', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_ExperimenterGroup, False, 0),), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_changeOwner = IcePy.Operation('changeOwner', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_IObject, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_changeOwner.deprecate("changeOwner() is deprecated. use ode::cmd::Chown2() instead.")
    IAdmin._op_changeGroup = IcePy.Operation('changeGroup', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_IObject, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_changeGroup.deprecate("changeGroup() is deprecated. use ode::cmd::Chgrp2() instead.")
    IAdmin._op_changePermissions = IcePy.Operation('changePermissions', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_IObject, False, 0), ((), _M_ode.model._t_Permissions, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_changePermissions.deprecate("changePermissions() is deprecated. use ode::cmd::Chmod2() instead.")
    IAdmin._op_moveToCommonSpace = IcePy.Operation('moveToCommonSpace', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0),), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_setAdminPrivileges = IcePy.Operation('setAdminPrivileges', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode.api._t_AdminPrivilegeList, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_changePassword = IcePy.Operation('changePassword', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RString, False, 0),), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_changePasswordWithOldPassword = IcePy.Operation('changePasswordWithOldPassword', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RString, False, 0), ((), _M_ode._t_RString, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_changeUserPassword = IcePy.Operation('changeUserPassword', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode._t_RString, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_synchronizeLoginCache = IcePy.Operation('synchronizeLoginCache', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_changeExpiredCredentials = IcePy.Operation('changeExpiredCredentials', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_reportForgottenPassword = IcePy.Operation('reportForgottenPassword', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    IAdmin._op_reportForgottenPassword.deprecate("reportForgottenPassword() is deprecated. use ode::cmd::ResetPasswordRequest() instead.")
    IAdmin._op_getSecurityRoles = IcePy.Operation('getSecurityRoles', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.sys._t_Roles, False, 0), (_M_ode._t_ServerError,))
    IAdmin._op_getEventContext = IcePy.Operation('getEventContext', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.sys._t_EventContext, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.IAdmin = IAdmin
    del IAdmin

    _M_ode.api.IAdminPrx = IAdminPrx
    del IAdminPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
