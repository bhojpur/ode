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
import ode_ModelF_ice
import ode_ServicesF_ice
import ode_Collections_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'IShare' not in _M_ode.api.__dict__:
    _M_ode.api.IShare = Ice.createTempClass()
    class IShare(_M_ode.api.ServiceInterface):
        """
        Provides method for sharing - collaboration process for images,
        datasets, projects.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IShare:
                raise RuntimeError('ode.api.IShare is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IShare', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IShare'

        def ice_staticId():
            return '::ode::api::IShare'
        ice_staticId = staticmethod(ice_staticId)

        def activate_async(self, _cb, shareId, current=None):
            """
            Turns on the access control lists attached to the given
            share for the current session. Warning: this will slow down
            the execution of the current session for all database
            reads. Writing to the database will not be allowed. If
            share does not exist or is not accessible (non-members) or
            is disabled, then an ode.ValidationException is thrown.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def deactivate_async(self, _cb, current=None):
            """
            Turns off the access control lists with the current share.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getShare_async(self, _cb, shareId, current=None):
            """
            Gets a share as a ode.model.Session with all
            related: ode.model.Annotation comments,
            ode.model.Experimenter members, fully loaded.
            Unlike the other methods on this interface, if the
            sessionId is unknown, does not throw a
            ode.ValidationException.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def getMemberCount_async(self, _cb, shareIds, current=None):
            """
            Returns a map from share id to the count of total members
            (including the owner). This is represented by
            ode.model.ShareMember links.
            Arguments:
            _cb -- The asynchronous callback object.
            shareIds -- Not null.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if a given share does not exist
            """
            pass

        def getOwnShares_async(self, _cb, active, current=None):
            """
            Gets all owned shares for the current
            ode.model.Experimenter.
            Arguments:
            _cb -- The asynchronous callback object.
            active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
            current -- The Current object for the invocation.
            """
            pass

        def getMemberShares_async(self, _cb, active, current=None):
            """
            Gets all shares where current
            ode.model.Experimenter is a member.
            Arguments:
            _cb -- The asynchronous callback object.
            active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
            current -- The Current object for the invocation.
            """
            pass

        def getSharesOwnedBy_async(self, _cb, user, active, current=None):
            """
            Gets all shares owned by the given
            ode.model.Experimenter.
            Arguments:
            _cb -- The asynchronous callback object.
            user -- the experimenter
            active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
            current -- The Current object for the invocation.
            """
            pass

        def getMemberSharesFor_async(self, _cb, user, active, current=None):
            """
            Gets all shares where given
            ode.model.Experimenter is a member.
            Arguments:
            _cb -- The asynchronous callback object.
            user -- the experimenter
            active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
            current -- The Current object for the invocation.
            """
            pass

        def getContents_async(self, _cb, shareId, current=None):
            """
            Looks up all ode.model.IObject items belong to the
            ode.model.Session share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def getContentSubList_async(self, _cb, shareId, start, finish, current=None):
            """
            Returns a range of items from the share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            start -- 
            finish -- 
            current -- The Current object for the invocation.
            """
            pass

        def getContentSize_async(self, _cb, shareId, current=None):
            """
            Returns the number of items in the share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def getContentMap_async(self, _cb, shareId, current=None):
            """
            Returns the contents of the share keyed by type.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def createShare_async(self, _cb, description, expiration, items, exps, guests, enabled, current=None):
            """
            Creates ode.model.Session share with all related:
            ode.model.IObject itmes,
            ode.model.Experimenter members, and guests.
            Arguments:
            _cb -- The asynchronous callback object.
            description -- 
            expiration -- 
            items -- 
            exps -- 
            guests -- 
            enabled -- if true, then the share is immediately available for use. If false, then the share is in draft state. All methods on this interface will work for shares except {@code activate}. Similarly, the share password cannot be used by guests to login.
            current -- The Current object for the invocation.
            """
            pass

        def setDescription_async(self, _cb, shareId, description, current=None):
            pass

        def setExpiration_async(self, _cb, shareId, expiration, current=None):
            pass

        def setActive_async(self, _cb, shareId, active, current=None):
            pass

        def closeShare_async(self, _cb, shareId, current=None):
            """
            Closes ode.model.Session share. No further logins
            will be possible and all getters (e.g.
            {@code getMemberShares}, {@code getOwnShares}, ...) will
            filter these results if {@code onlyActive} is true.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def addObjects_async(self, _cb, shareId, iobjects, current=None):
            """
            Adds new ode.model.IObject items to
            ode.model.Session share. Conceptually calls
            {@code addObjects} for every argument passed, but the
            graphs will be merged.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            iobjects -- 
            current -- The Current object for the invocation.
            """
            pass

        def addObject_async(self, _cb, shareId, iobject, current=None):
            """
            Adds new ode.model.IObject item to
            ode.model.Session share. The entire object graph
            with the exception of all Details will be loaded into the
            share. If you would like to load a single object, then pass
            an unloaded reference.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            iobject -- 
            current -- The Current object for the invocation.
            """
            pass

        def removeObjects_async(self, _cb, shareId, iobjects, current=None):
            """
            Remove existing items from the share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            iobjects -- 
            current -- The Current object for the invocation.
            """
            pass

        def removeObject_async(self, _cb, shareId, iobject, current=None):
            """
            Removes existing ode.model.IObject object from the
            ode.model.Session share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            iobject -- 
            current -- The Current object for the invocation.
            """
            pass

        def getCommentCount_async(self, _cb, shareIds, current=None):
            """
            Returns a map from share id to comment count.
            Arguments:
            _cb -- The asynchronous callback object.
            shareIds -- Not null.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if a given share does not exist
            """
            pass

        def getComments_async(self, _cb, shareId, current=None):
            """
            Looks up all ode.model.Annotation comments which
            belong to the ode.model.Session share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def addComment_async(self, _cb, shareId, comment, current=None):
            """
            Creates ode.model.TextAnnotation comment for
            ode.model.Session share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            comment -- 
            current -- The Current object for the invocation.
            """
            pass

        def addReply_async(self, _cb, shareId, comment, replyTo, current=None):
            """
            Creates ode.model.TextAnnotation comment which
            replies to an existing comment.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            comment -- 
            replyTo -- 
            current -- The Current object for the invocation.
            """
            pass

        def deleteComment_async(self, _cb, comment, current=None):
            """
            Deletes ode.model.Annotation comment from the
            database.
            Arguments:
            _cb -- The asynchronous callback object.
            comment -- 
            current -- The Current object for the invocation.
            """
            pass

        def getAllMembers_async(self, _cb, shareId, current=None):
            """
            Get all ode.model.Experimenter users who are a
            member of the share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def getAllGuests_async(self, _cb, shareId, current=None):
            """
            Get the email addresses for all share guests.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def getAllUsers_async(self, _cb, shareId, current=None):
            """
            Get a single set containing the
            {@code ode.model.Experimenter.getOdeName} login names
            of the users as well email addresses for guests.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if there is a conflict between email addresses and user names.
            """
            pass

        def addUsers_async(self, _cb, shareId, exps, current=None):
            """
            Adds ode.model.Experimenter experimenters to
            ode.model.Session share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            exps -- 
            current -- The Current object for the invocation.
            """
            pass

        def addGuests_async(self, _cb, shareId, emailAddresses, current=None):
            """
            Adds guest email addresses to the share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            emailAddresses -- 
            current -- The Current object for the invocation.
            """
            pass

        def removeUsers_async(self, _cb, shareId, exps, current=None):
            """
            Removes ode.model.Experimenter experimenters from
            ode.model.Session share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            exps -- 
            current -- The Current object for the invocation.
            """
            pass

        def removeGuests_async(self, _cb, shareId, emailAddresses, current=None):
            """
            Removes guest email addresses from the share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            emailAddresses -- 
            current -- The Current object for the invocation.
            """
            pass

        def addUser_async(self, _cb, shareId, exp, current=None):
            """
            Adds ode.model.Experimenter experimenter to
            ode.model.Session share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            exp -- 
            current -- The Current object for the invocation.
            """
            pass

        def addGuest_async(self, _cb, shareId, emailAddress, current=None):
            """
            Adds guest email address to the share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            emailAddress -- 
            current -- The Current object for the invocation.
            """
            pass

        def removeUser_async(self, _cb, shareId, exp, current=None):
            """
            Removes ode.model.Experimenter experimenter from
            ode.model.Session share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            exp -- 
            current -- The Current object for the invocation.
            """
            pass

        def removeGuest_async(self, _cb, shareId, emailAddress, current=None):
            """
            Removes guest email address from share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            emailAddress -- 
            current -- The Current object for the invocation.
            """
            pass

        def getActiveConnections_async(self, _cb, shareId, current=None):
            """
            Gets actual active connections to
            ode.model.Session share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def getPastConnections_async(self, _cb, shareId, current=None):
            """
            Gets previous connections to
            ode.model.Session share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            current -- The Current object for the invocation.
            """
            pass

        def invalidateConnection_async(self, _cb, shareId, exp, current=None):
            """
            Makes the connection invalid for
            ode.model.Session share for specified user.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            exp -- 
            current -- The Current object for the invocation.
            """
            pass

        def getEvents_async(self, _cb, shareId, exp, _from, to, current=None):
            """
            Gets events for ode.model.Session share per
            ode.model.Experimenter experimenter for period of
            time.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            exp -- 
            _from -- 
            to -- 
            current -- The Current object for the invocation.
            """
            pass

        def notifyMembersOfShare_async(self, _cb, shareId, subject, message, html, current=None):
            """
            Notifies via email selected members of share.
            Arguments:
            _cb -- The asynchronous callback object.
            shareId -- 
            subject -- 
            message -- 
            html -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IShare)

        __repr__ = __str__

    _M_ode.api.ISharePrx = Ice.createTempClass()
    class ISharePrx(_M_ode.api.ServiceInterfacePrx):

        """
        Turns on the access control lists attached to the given
        share for the current session. Warning: this will slow down
        the execution of the current session for all database
        reads. Writing to the database will not be allowed. If
        share does not exist or is not accessible (non-members) or
        is disabled, then an ode.ValidationException is thrown.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        """
        def activate(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_activate.invoke(self, ((shareId, ), _ctx))

        """
        Turns on the access control lists attached to the given
        share for the current session. Warning: this will slow down
        the execution of the current session for all database
        reads. Writing to the database will not be allowed. If
        share does not exist or is not accessible (non-members) or
        is disabled, then an ode.ValidationException is thrown.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_activate(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_activate.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Turns on the access control lists attached to the given
        share for the current session. Warning: this will slow down
        the execution of the current session for all database
        reads. Writing to the database will not be allowed. If
        share does not exist or is not accessible (non-members) or
        is disabled, then an ode.ValidationException is thrown.
        Arguments:
        shareId -- 
        """
        def end_activate(self, _r):
            return _M_ode.api.IShare._op_activate.end(self, _r)

        """
        Turns off the access control lists with the current share.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def deactivate(self, _ctx=None):
            return _M_ode.api.IShare._op_deactivate.invoke(self, ((), _ctx))

        """
        Turns off the access control lists with the current share.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_deactivate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_deactivate.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Turns off the access control lists with the current share.
        Arguments:
        """
        def end_deactivate(self, _r):
            return _M_ode.api.IShare._op_deactivate.end(self, _r)

        """
        Gets a share as a ode.model.Session with all
        related: ode.model.Annotation comments,
        ode.model.Experimenter members, fully loaded.
        Unlike the other methods on this interface, if the
        sessionId is unknown, does not throw a
        ode.ValidationException.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        Returns: a ode.model.Session with id and ode.model.Details set or null. The owner in the Details object is the true owner, and the group in the Details has all member users linked. ode.model.Annotation instances of the share are linked to the ode.model.Session. Missing is a list of share guests.
        """
        def getShare(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_getShare.invoke(self, ((shareId, ), _ctx))

        """
        Gets a share as a ode.model.Session with all
        related: ode.model.Annotation comments,
        ode.model.Experimenter members, fully loaded.
        Unlike the other methods on this interface, if the
        sessionId is unknown, does not throw a
        ode.ValidationException.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getShare(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getShare.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Gets a share as a ode.model.Session with all
        related: ode.model.Annotation comments,
        ode.model.Experimenter members, fully loaded.
        Unlike the other methods on this interface, if the
        sessionId is unknown, does not throw a
        ode.ValidationException.
        Arguments:
        shareId -- 
        Returns: a ode.model.Session with id and ode.model.Details set or null. The owner in the Details object is the true owner, and the group in the Details has all member users linked. ode.model.Annotation instances of the share are linked to the ode.model.Session. Missing is a list of share guests.
        """
        def end_getShare(self, _r):
            return _M_ode.api.IShare._op_getShare.end(self, _r)

        """
        Returns a map from share id to the count of total members
        (including the owner). This is represented by
        ode.model.ShareMember links.
        Arguments:
        shareIds -- Not null.
        _ctx -- The request context for the invocation.
        Returns: Map with all ids present.
        Throws:
        ValidationException -- if a given share does not exist
        """
        def getMemberCount(self, shareIds, _ctx=None):
            return _M_ode.api.IShare._op_getMemberCount.invoke(self, ((shareIds, ), _ctx))

        """
        Returns a map from share id to the count of total members
        (including the owner). This is represented by
        ode.model.ShareMember links.
        Arguments:
        shareIds -- Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMemberCount(self, shareIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getMemberCount.begin(self, ((shareIds, ), _response, _ex, _sent, _ctx))

        """
        Returns a map from share id to the count of total members
        (including the owner). This is represented by
        ode.model.ShareMember links.
        Arguments:
        shareIds -- Not null.
        Returns: Map with all ids present.
        Throws:
        ValidationException -- if a given share does not exist
        """
        def end_getMemberCount(self, _r):
            return _M_ode.api.IShare._op_getMemberCount.end(self, _r)

        """
        Gets all owned shares for the current
        ode.model.Experimenter.
        Arguments:
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        _ctx -- The request context for the invocation.
        Returns: set of shares. Never null. May be empty.
        """
        def getOwnShares(self, active, _ctx=None):
            return _M_ode.api.IShare._op_getOwnShares.invoke(self, ((active, ), _ctx))

        """
        Gets all owned shares for the current
        ode.model.Experimenter.
        Arguments:
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getOwnShares(self, active, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getOwnShares.begin(self, ((active, ), _response, _ex, _sent, _ctx))

        """
        Gets all owned shares for the current
        ode.model.Experimenter.
        Arguments:
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        Returns: set of shares. Never null. May be empty.
        """
        def end_getOwnShares(self, _r):
            return _M_ode.api.IShare._op_getOwnShares.end(self, _r)

        """
        Gets all shares where current
        ode.model.Experimenter is a member.
        Arguments:
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        _ctx -- The request context for the invocation.
        Returns: set of shares. Never null. May be empty.
        """
        def getMemberShares(self, active, _ctx=None):
            return _M_ode.api.IShare._op_getMemberShares.invoke(self, ((active, ), _ctx))

        """
        Gets all shares where current
        ode.model.Experimenter is a member.
        Arguments:
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMemberShares(self, active, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getMemberShares.begin(self, ((active, ), _response, _ex, _sent, _ctx))

        """
        Gets all shares where current
        ode.model.Experimenter is a member.
        Arguments:
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        Returns: set of shares. Never null. May be empty.
        """
        def end_getMemberShares(self, _r):
            return _M_ode.api.IShare._op_getMemberShares.end(self, _r)

        """
        Gets all shares owned by the given
        ode.model.Experimenter.
        Arguments:
        user -- the experimenter
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        _ctx -- The request context for the invocation.
        Returns: set of shares. Never null. May be empty.
        """
        def getSharesOwnedBy(self, user, active, _ctx=None):
            return _M_ode.api.IShare._op_getSharesOwnedBy.invoke(self, ((user, active), _ctx))

        """
        Gets all shares owned by the given
        ode.model.Experimenter.
        Arguments:
        user -- the experimenter
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getSharesOwnedBy(self, user, active, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getSharesOwnedBy.begin(self, ((user, active), _response, _ex, _sent, _ctx))

        """
        Gets all shares owned by the given
        ode.model.Experimenter.
        Arguments:
        user -- the experimenter
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        Returns: set of shares. Never null. May be empty.
        """
        def end_getSharesOwnedBy(self, _r):
            return _M_ode.api.IShare._op_getSharesOwnedBy.end(self, _r)

        """
        Gets all shares where given
        ode.model.Experimenter is a member.
        Arguments:
        user -- the experimenter
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        _ctx -- The request context for the invocation.
        Returns: set of shares. Never null. May be empty.
        """
        def getMemberSharesFor(self, user, active, _ctx=None):
            return _M_ode.api.IShare._op_getMemberSharesFor.invoke(self, ((user, active), _ctx))

        """
        Gets all shares where given
        ode.model.Experimenter is a member.
        Arguments:
        user -- the experimenter
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMemberSharesFor(self, user, active, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getMemberSharesFor.begin(self, ((user, active), _response, _ex, _sent, _ctx))

        """
        Gets all shares where given
        ode.model.Experimenter is a member.
        Arguments:
        user -- the experimenter
        active -- if true, then only shares which can be used for login will be returned. All draft shares (see {@code createShare}) and closed shares (see {@code closeShare}) will be filtered.
        Returns: set of shares. Never null. May be empty.
        """
        def end_getMemberSharesFor(self, _r):
            return _M_ode.api.IShare._op_getMemberSharesFor.end(self, _r)

        """
        Looks up all ode.model.IObject items belong to the
        ode.model.Session share.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        Returns: list of objects. Not null. Probably not empty.
        """
        def getContents(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_getContents.invoke(self, ((shareId, ), _ctx))

        """
        Looks up all ode.model.IObject items belong to the
        ode.model.Session share.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getContents(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getContents.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Looks up all ode.model.IObject items belong to the
        ode.model.Session share.
        Arguments:
        shareId -- 
        Returns: list of objects. Not null. Probably not empty.
        """
        def end_getContents(self, _r):
            return _M_ode.api.IShare._op_getContents.end(self, _r)

        """
        Returns a range of items from the share.
        Arguments:
        shareId -- 
        start -- 
        finish -- 
        _ctx -- The request context for the invocation.
        """
        def getContentSubList(self, shareId, start, finish, _ctx=None):
            return _M_ode.api.IShare._op_getContentSubList.invoke(self, ((shareId, start, finish), _ctx))

        """
        Returns a range of items from the share.
        Arguments:
        shareId -- 
        start -- 
        finish -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getContentSubList(self, shareId, start, finish, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getContentSubList.begin(self, ((shareId, start, finish), _response, _ex, _sent, _ctx))

        """
        Returns a range of items from the share.
        Arguments:
        shareId -- 
        start -- 
        finish -- 
        """
        def end_getContentSubList(self, _r):
            return _M_ode.api.IShare._op_getContentSubList.end(self, _r)

        """
        Returns the number of items in the share.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        """
        def getContentSize(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_getContentSize.invoke(self, ((shareId, ), _ctx))

        """
        Returns the number of items in the share.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getContentSize(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getContentSize.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Returns the number of items in the share.
        Arguments:
        shareId -- 
        """
        def end_getContentSize(self, _r):
            return _M_ode.api.IShare._op_getContentSize.end(self, _r)

        """
        Returns the contents of the share keyed by type.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        """
        def getContentMap(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_getContentMap.invoke(self, ((shareId, ), _ctx))

        """
        Returns the contents of the share keyed by type.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getContentMap(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getContentMap.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Returns the contents of the share keyed by type.
        Arguments:
        shareId -- 
        """
        def end_getContentMap(self, _r):
            return _M_ode.api.IShare._op_getContentMap.end(self, _r)

        """
        Creates ode.model.Session share with all related:
        ode.model.IObject itmes,
        ode.model.Experimenter members, and guests.
        Arguments:
        description -- 
        expiration -- 
        items -- 
        exps -- 
        guests -- 
        enabled -- if true, then the share is immediately available for use. If false, then the share is in draft state. All methods on this interface will work for shares except {@code activate}. Similarly, the share password cannot be used by guests to login.
        _ctx -- The request context for the invocation.
        """
        def createShare(self, description, expiration, items, exps, guests, enabled, _ctx=None):
            return _M_ode.api.IShare._op_createShare.invoke(self, ((description, expiration, items, exps, guests, enabled), _ctx))

        """
        Creates ode.model.Session share with all related:
        ode.model.IObject itmes,
        ode.model.Experimenter members, and guests.
        Arguments:
        description -- 
        expiration -- 
        items -- 
        exps -- 
        guests -- 
        enabled -- if true, then the share is immediately available for use. If false, then the share is in draft state. All methods on this interface will work for shares except {@code activate}. Similarly, the share password cannot be used by guests to login.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createShare(self, description, expiration, items, exps, guests, enabled, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_createShare.begin(self, ((description, expiration, items, exps, guests, enabled), _response, _ex, _sent, _ctx))

        """
        Creates ode.model.Session share with all related:
        ode.model.IObject itmes,
        ode.model.Experimenter members, and guests.
        Arguments:
        description -- 
        expiration -- 
        items -- 
        exps -- 
        guests -- 
        enabled -- if true, then the share is immediately available for use. If false, then the share is in draft state. All methods on this interface will work for shares except {@code activate}. Similarly, the share password cannot be used by guests to login.
        """
        def end_createShare(self, _r):
            return _M_ode.api.IShare._op_createShare.end(self, _r)

        def setDescription(self, shareId, description, _ctx=None):
            return _M_ode.api.IShare._op_setDescription.invoke(self, ((shareId, description), _ctx))

        def begin_setDescription(self, shareId, description, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_setDescription.begin(self, ((shareId, description), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_ode.api.IShare._op_setDescription.end(self, _r)

        def setExpiration(self, shareId, expiration, _ctx=None):
            return _M_ode.api.IShare._op_setExpiration.invoke(self, ((shareId, expiration), _ctx))

        def begin_setExpiration(self, shareId, expiration, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_setExpiration.begin(self, ((shareId, expiration), _response, _ex, _sent, _ctx))

        def end_setExpiration(self, _r):
            return _M_ode.api.IShare._op_setExpiration.end(self, _r)

        def setActive(self, shareId, active, _ctx=None):
            return _M_ode.api.IShare._op_setActive.invoke(self, ((shareId, active), _ctx))

        def begin_setActive(self, shareId, active, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_setActive.begin(self, ((shareId, active), _response, _ex, _sent, _ctx))

        def end_setActive(self, _r):
            return _M_ode.api.IShare._op_setActive.end(self, _r)

        """
        Closes ode.model.Session share. No further logins
        will be possible and all getters (e.g.
        {@code getMemberShares}, {@code getOwnShares}, ...) will
        filter these results if {@code onlyActive} is true.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        """
        def closeShare(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_closeShare.invoke(self, ((shareId, ), _ctx))

        """
        Closes ode.model.Session share. No further logins
        will be possible and all getters (e.g.
        {@code getMemberShares}, {@code getOwnShares}, ...) will
        filter these results if {@code onlyActive} is true.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_closeShare(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_closeShare.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Closes ode.model.Session share. No further logins
        will be possible and all getters (e.g.
        {@code getMemberShares}, {@code getOwnShares}, ...) will
        filter these results if {@code onlyActive} is true.
        Arguments:
        shareId -- 
        """
        def end_closeShare(self, _r):
            return _M_ode.api.IShare._op_closeShare.end(self, _r)

        """
        Adds new ode.model.IObject items to
        ode.model.Session share. Conceptually calls
        {@code addObjects} for every argument passed, but the
        graphs will be merged.
        Arguments:
        shareId -- 
        iobjects -- 
        _ctx -- The request context for the invocation.
        """
        def addObjects(self, shareId, iobjects, _ctx=None):
            return _M_ode.api.IShare._op_addObjects.invoke(self, ((shareId, iobjects), _ctx))

        """
        Adds new ode.model.IObject items to
        ode.model.Session share. Conceptually calls
        {@code addObjects} for every argument passed, but the
        graphs will be merged.
        Arguments:
        shareId -- 
        iobjects -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addObjects(self, shareId, iobjects, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_addObjects.begin(self, ((shareId, iobjects), _response, _ex, _sent, _ctx))

        """
        Adds new ode.model.IObject items to
        ode.model.Session share. Conceptually calls
        {@code addObjects} for every argument passed, but the
        graphs will be merged.
        Arguments:
        shareId -- 
        iobjects -- 
        """
        def end_addObjects(self, _r):
            return _M_ode.api.IShare._op_addObjects.end(self, _r)

        """
        Adds new ode.model.IObject item to
        ode.model.Session share. The entire object graph
        with the exception of all Details will be loaded into the
        share. If you would like to load a single object, then pass
        an unloaded reference.
        Arguments:
        shareId -- 
        iobject -- 
        _ctx -- The request context for the invocation.
        """
        def addObject(self, shareId, iobject, _ctx=None):
            return _M_ode.api.IShare._op_addObject.invoke(self, ((shareId, iobject), _ctx))

        """
        Adds new ode.model.IObject item to
        ode.model.Session share. The entire object graph
        with the exception of all Details will be loaded into the
        share. If you would like to load a single object, then pass
        an unloaded reference.
        Arguments:
        shareId -- 
        iobject -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addObject(self, shareId, iobject, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_addObject.begin(self, ((shareId, iobject), _response, _ex, _sent, _ctx))

        """
        Adds new ode.model.IObject item to
        ode.model.Session share. The entire object graph
        with the exception of all Details will be loaded into the
        share. If you would like to load a single object, then pass
        an unloaded reference.
        Arguments:
        shareId -- 
        iobject -- 
        """
        def end_addObject(self, _r):
            return _M_ode.api.IShare._op_addObject.end(self, _r)

        """
        Remove existing items from the share.
        Arguments:
        shareId -- 
        iobjects -- 
        _ctx -- The request context for the invocation.
        """
        def removeObjects(self, shareId, iobjects, _ctx=None):
            return _M_ode.api.IShare._op_removeObjects.invoke(self, ((shareId, iobjects), _ctx))

        """
        Remove existing items from the share.
        Arguments:
        shareId -- 
        iobjects -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeObjects(self, shareId, iobjects, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_removeObjects.begin(self, ((shareId, iobjects), _response, _ex, _sent, _ctx))

        """
        Remove existing items from the share.
        Arguments:
        shareId -- 
        iobjects -- 
        """
        def end_removeObjects(self, _r):
            return _M_ode.api.IShare._op_removeObjects.end(self, _r)

        """
        Removes existing ode.model.IObject object from the
        ode.model.Session share.
        Arguments:
        shareId -- 
        iobject -- 
        _ctx -- The request context for the invocation.
        """
        def removeObject(self, shareId, iobject, _ctx=None):
            return _M_ode.api.IShare._op_removeObject.invoke(self, ((shareId, iobject), _ctx))

        """
        Removes existing ode.model.IObject object from the
        ode.model.Session share.
        Arguments:
        shareId -- 
        iobject -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeObject(self, shareId, iobject, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_removeObject.begin(self, ((shareId, iobject), _response, _ex, _sent, _ctx))

        """
        Removes existing ode.model.IObject object from the
        ode.model.Session share.
        Arguments:
        shareId -- 
        iobject -- 
        """
        def end_removeObject(self, _r):
            return _M_ode.api.IShare._op_removeObject.end(self, _r)

        """
        Returns a map from share id to comment count.
        Arguments:
        shareIds -- Not null.
        _ctx -- The request context for the invocation.
        Returns: Map with all ids present and 0 if no count exists.
        Throws:
        ValidationException -- if a given share does not exist
        """
        def getCommentCount(self, shareIds, _ctx=None):
            return _M_ode.api.IShare._op_getCommentCount.invoke(self, ((shareIds, ), _ctx))

        """
        Returns a map from share id to comment count.
        Arguments:
        shareIds -- Not null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getCommentCount(self, shareIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getCommentCount.begin(self, ((shareIds, ), _response, _ex, _sent, _ctx))

        """
        Returns a map from share id to comment count.
        Arguments:
        shareIds -- Not null.
        Returns: Map with all ids present and 0 if no count exists.
        Throws:
        ValidationException -- if a given share does not exist
        """
        def end_getCommentCount(self, _r):
            return _M_ode.api.IShare._op_getCommentCount.end(self, _r)

        """
        Looks up all ode.model.Annotation comments which
        belong to the ode.model.Session share.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        Returns: list of Annotation
        """
        def getComments(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_getComments.invoke(self, ((shareId, ), _ctx))

        """
        Looks up all ode.model.Annotation comments which
        belong to the ode.model.Session share.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getComments(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getComments.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Looks up all ode.model.Annotation comments which
        belong to the ode.model.Session share.
        Arguments:
        shareId -- 
        Returns: list of Annotation
        """
        def end_getComments(self, _r):
            return _M_ode.api.IShare._op_getComments.end(self, _r)

        """
        Creates ode.model.TextAnnotation comment for
        ode.model.Session share.
        Arguments:
        shareId -- 
        comment -- 
        _ctx -- The request context for the invocation.
        """
        def addComment(self, shareId, comment, _ctx=None):
            return _M_ode.api.IShare._op_addComment.invoke(self, ((shareId, comment), _ctx))

        """
        Creates ode.model.TextAnnotation comment for
        ode.model.Session share.
        Arguments:
        shareId -- 
        comment -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addComment(self, shareId, comment, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_addComment.begin(self, ((shareId, comment), _response, _ex, _sent, _ctx))

        """
        Creates ode.model.TextAnnotation comment for
        ode.model.Session share.
        Arguments:
        shareId -- 
        comment -- 
        """
        def end_addComment(self, _r):
            return _M_ode.api.IShare._op_addComment.end(self, _r)

        """
        Creates ode.model.TextAnnotation comment which
        replies to an existing comment.
        Arguments:
        shareId -- 
        comment -- 
        replyTo -- 
        _ctx -- The request context for the invocation.
        Returns: the new ode.model.TextAnnotation
        """
        def addReply(self, shareId, comment, replyTo, _ctx=None):
            return _M_ode.api.IShare._op_addReply.invoke(self, ((shareId, comment, replyTo), _ctx))

        """
        Creates ode.model.TextAnnotation comment which
        replies to an existing comment.
        Arguments:
        shareId -- 
        comment -- 
        replyTo -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addReply(self, shareId, comment, replyTo, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_addReply.begin(self, ((shareId, comment, replyTo), _response, _ex, _sent, _ctx))

        """
        Creates ode.model.TextAnnotation comment which
        replies to an existing comment.
        Arguments:
        shareId -- 
        comment -- 
        replyTo -- 
        Returns: the new ode.model.TextAnnotation
        """
        def end_addReply(self, _r):
            return _M_ode.api.IShare._op_addReply.end(self, _r)

        """
        Deletes ode.model.Annotation comment from the
        database.
        Arguments:
        comment -- 
        _ctx -- The request context for the invocation.
        """
        def deleteComment(self, comment, _ctx=None):
            return _M_ode.api.IShare._op_deleteComment.invoke(self, ((comment, ), _ctx))

        """
        Deletes ode.model.Annotation comment from the
        database.
        Arguments:
        comment -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_deleteComment(self, comment, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_deleteComment.begin(self, ((comment, ), _response, _ex, _sent, _ctx))

        """
        Deletes ode.model.Annotation comment from the
        database.
        Arguments:
        comment -- 
        """
        def end_deleteComment(self, _r):
            return _M_ode.api.IShare._op_deleteComment.end(self, _r)

        """
        Get all ode.model.Experimenter users who are a
        member of the share.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        """
        def getAllMembers(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_getAllMembers.invoke(self, ((shareId, ), _ctx))

        """
        Get all ode.model.Experimenter users who are a
        member of the share.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getAllMembers(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getAllMembers.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Get all ode.model.Experimenter users who are a
        member of the share.
        Arguments:
        shareId -- 
        """
        def end_getAllMembers(self, _r):
            return _M_ode.api.IShare._op_getAllMembers.end(self, _r)

        """
        Get the email addresses for all share guests.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        """
        def getAllGuests(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_getAllGuests.invoke(self, ((shareId, ), _ctx))

        """
        Get the email addresses for all share guests.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getAllGuests(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getAllGuests.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Get the email addresses for all share guests.
        Arguments:
        shareId -- 
        """
        def end_getAllGuests(self, _r):
            return _M_ode.api.IShare._op_getAllGuests.end(self, _r)

        """
        Get a single set containing the
        {@code ode.model.Experimenter.getOdeName} login names
        of the users as well email addresses for guests.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        Returns: a java.util.Set containing the login of all users
        Throws:
        ValidationException -- if there is a conflict between email addresses and user names.
        """
        def getAllUsers(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_getAllUsers.invoke(self, ((shareId, ), _ctx))

        """
        Get a single set containing the
        {@code ode.model.Experimenter.getOdeName} login names
        of the users as well email addresses for guests.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getAllUsers(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getAllUsers.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Get a single set containing the
        {@code ode.model.Experimenter.getOdeName} login names
        of the users as well email addresses for guests.
        Arguments:
        shareId -- 
        Returns: a java.util.Set containing the login of all users
        Throws:
        ValidationException -- if there is a conflict between email addresses and user names.
        """
        def end_getAllUsers(self, _r):
            return _M_ode.api.IShare._op_getAllUsers.end(self, _r)

        """
        Adds ode.model.Experimenter experimenters to
        ode.model.Session share.
        Arguments:
        shareId -- 
        exps -- 
        _ctx -- The request context for the invocation.
        """
        def addUsers(self, shareId, exps, _ctx=None):
            return _M_ode.api.IShare._op_addUsers.invoke(self, ((shareId, exps), _ctx))

        """
        Adds ode.model.Experimenter experimenters to
        ode.model.Session share.
        Arguments:
        shareId -- 
        exps -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addUsers(self, shareId, exps, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_addUsers.begin(self, ((shareId, exps), _response, _ex, _sent, _ctx))

        """
        Adds ode.model.Experimenter experimenters to
        ode.model.Session share.
        Arguments:
        shareId -- 
        exps -- 
        """
        def end_addUsers(self, _r):
            return _M_ode.api.IShare._op_addUsers.end(self, _r)

        """
        Adds guest email addresses to the share.
        Arguments:
        shareId -- 
        emailAddresses -- 
        _ctx -- The request context for the invocation.
        """
        def addGuests(self, shareId, emailAddresses, _ctx=None):
            return _M_ode.api.IShare._op_addGuests.invoke(self, ((shareId, emailAddresses), _ctx))

        """
        Adds guest email addresses to the share.
        Arguments:
        shareId -- 
        emailAddresses -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addGuests(self, shareId, emailAddresses, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_addGuests.begin(self, ((shareId, emailAddresses), _response, _ex, _sent, _ctx))

        """
        Adds guest email addresses to the share.
        Arguments:
        shareId -- 
        emailAddresses -- 
        """
        def end_addGuests(self, _r):
            return _M_ode.api.IShare._op_addGuests.end(self, _r)

        """
        Removes ode.model.Experimenter experimenters from
        ode.model.Session share.
        Arguments:
        shareId -- 
        exps -- 
        _ctx -- The request context for the invocation.
        """
        def removeUsers(self, shareId, exps, _ctx=None):
            return _M_ode.api.IShare._op_removeUsers.invoke(self, ((shareId, exps), _ctx))

        """
        Removes ode.model.Experimenter experimenters from
        ode.model.Session share.
        Arguments:
        shareId -- 
        exps -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeUsers(self, shareId, exps, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_removeUsers.begin(self, ((shareId, exps), _response, _ex, _sent, _ctx))

        """
        Removes ode.model.Experimenter experimenters from
        ode.model.Session share.
        Arguments:
        shareId -- 
        exps -- 
        """
        def end_removeUsers(self, _r):
            return _M_ode.api.IShare._op_removeUsers.end(self, _r)

        """
        Removes guest email addresses from the share.
        Arguments:
        shareId -- 
        emailAddresses -- 
        _ctx -- The request context for the invocation.
        """
        def removeGuests(self, shareId, emailAddresses, _ctx=None):
            return _M_ode.api.IShare._op_removeGuests.invoke(self, ((shareId, emailAddresses), _ctx))

        """
        Removes guest email addresses from the share.
        Arguments:
        shareId -- 
        emailAddresses -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeGuests(self, shareId, emailAddresses, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_removeGuests.begin(self, ((shareId, emailAddresses), _response, _ex, _sent, _ctx))

        """
        Removes guest email addresses from the share.
        Arguments:
        shareId -- 
        emailAddresses -- 
        """
        def end_removeGuests(self, _r):
            return _M_ode.api.IShare._op_removeGuests.end(self, _r)

        """
        Adds ode.model.Experimenter experimenter to
        ode.model.Session share.
        Arguments:
        shareId -- 
        exp -- 
        _ctx -- The request context for the invocation.
        """
        def addUser(self, shareId, exp, _ctx=None):
            return _M_ode.api.IShare._op_addUser.invoke(self, ((shareId, exp), _ctx))

        """
        Adds ode.model.Experimenter experimenter to
        ode.model.Session share.
        Arguments:
        shareId -- 
        exp -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addUser(self, shareId, exp, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_addUser.begin(self, ((shareId, exp), _response, _ex, _sent, _ctx))

        """
        Adds ode.model.Experimenter experimenter to
        ode.model.Session share.
        Arguments:
        shareId -- 
        exp -- 
        """
        def end_addUser(self, _r):
            return _M_ode.api.IShare._op_addUser.end(self, _r)

        """
        Adds guest email address to the share.
        Arguments:
        shareId -- 
        emailAddress -- 
        _ctx -- The request context for the invocation.
        """
        def addGuest(self, shareId, emailAddress, _ctx=None):
            return _M_ode.api.IShare._op_addGuest.invoke(self, ((shareId, emailAddress), _ctx))

        """
        Adds guest email address to the share.
        Arguments:
        shareId -- 
        emailAddress -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addGuest(self, shareId, emailAddress, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_addGuest.begin(self, ((shareId, emailAddress), _response, _ex, _sent, _ctx))

        """
        Adds guest email address to the share.
        Arguments:
        shareId -- 
        emailAddress -- 
        """
        def end_addGuest(self, _r):
            return _M_ode.api.IShare._op_addGuest.end(self, _r)

        """
        Removes ode.model.Experimenter experimenter from
        ode.model.Session share.
        Arguments:
        shareId -- 
        exp -- 
        _ctx -- The request context for the invocation.
        """
        def removeUser(self, shareId, exp, _ctx=None):
            return _M_ode.api.IShare._op_removeUser.invoke(self, ((shareId, exp), _ctx))

        """
        Removes ode.model.Experimenter experimenter from
        ode.model.Session share.
        Arguments:
        shareId -- 
        exp -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeUser(self, shareId, exp, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_removeUser.begin(self, ((shareId, exp), _response, _ex, _sent, _ctx))

        """
        Removes ode.model.Experimenter experimenter from
        ode.model.Session share.
        Arguments:
        shareId -- 
        exp -- 
        """
        def end_removeUser(self, _r):
            return _M_ode.api.IShare._op_removeUser.end(self, _r)

        """
        Removes guest email address from share.
        Arguments:
        shareId -- 
        emailAddress -- 
        _ctx -- The request context for the invocation.
        """
        def removeGuest(self, shareId, emailAddress, _ctx=None):
            return _M_ode.api.IShare._op_removeGuest.invoke(self, ((shareId, emailAddress), _ctx))

        """
        Removes guest email address from share.
        Arguments:
        shareId -- 
        emailAddress -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeGuest(self, shareId, emailAddress, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_removeGuest.begin(self, ((shareId, emailAddress), _response, _ex, _sent, _ctx))

        """
        Removes guest email address from share.
        Arguments:
        shareId -- 
        emailAddress -- 
        """
        def end_removeGuest(self, _r):
            return _M_ode.api.IShare._op_removeGuest.end(self, _r)

        """
        Gets actual active connections to
        ode.model.Session share.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        Returns: map of experimenter and IP address
        """
        def getActiveConnections(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_getActiveConnections.invoke(self, ((shareId, ), _ctx))

        """
        Gets actual active connections to
        ode.model.Session share.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getActiveConnections(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getActiveConnections.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Gets actual active connections to
        ode.model.Session share.
        Arguments:
        shareId -- 
        Returns: map of experimenter and IP address
        """
        def end_getActiveConnections(self, _r):
            return _M_ode.api.IShare._op_getActiveConnections.end(self, _r)

        """
        Gets previous connections to
        ode.model.Session share.
        Arguments:
        shareId -- 
        _ctx -- The request context for the invocation.
        Returns: map of experimenter and IP address
        """
        def getPastConnections(self, shareId, _ctx=None):
            return _M_ode.api.IShare._op_getPastConnections.invoke(self, ((shareId, ), _ctx))

        """
        Gets previous connections to
        ode.model.Session share.
        Arguments:
        shareId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPastConnections(self, shareId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getPastConnections.begin(self, ((shareId, ), _response, _ex, _sent, _ctx))

        """
        Gets previous connections to
        ode.model.Session share.
        Arguments:
        shareId -- 
        Returns: map of experimenter and IP address
        """
        def end_getPastConnections(self, _r):
            return _M_ode.api.IShare._op_getPastConnections.end(self, _r)

        """
        Makes the connection invalid for
        ode.model.Session share for specified user.
        Arguments:
        shareId -- 
        exp -- 
        _ctx -- The request context for the invocation.
        """
        def invalidateConnection(self, shareId, exp, _ctx=None):
            return _M_ode.api.IShare._op_invalidateConnection.invoke(self, ((shareId, exp), _ctx))

        """
        Makes the connection invalid for
        ode.model.Session share for specified user.
        Arguments:
        shareId -- 
        exp -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_invalidateConnection(self, shareId, exp, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_invalidateConnection.begin(self, ((shareId, exp), _response, _ex, _sent, _ctx))

        """
        Makes the connection invalid for
        ode.model.Session share for specified user.
        Arguments:
        shareId -- 
        exp -- 
        """
        def end_invalidateConnection(self, _r):
            return _M_ode.api.IShare._op_invalidateConnection.end(self, _r)

        """
        Gets events for ode.model.Session share per
        ode.model.Experimenter experimenter for period of
        time.
        Arguments:
        shareId -- 
        exp -- 
        _from -- 
        to -- 
        _ctx -- The request context for the invocation.
        Returns: List of events
        """
        def getEvents(self, shareId, exp, _from, to, _ctx=None):
            return _M_ode.api.IShare._op_getEvents.invoke(self, ((shareId, exp, _from, to), _ctx))

        """
        Gets events for ode.model.Session share per
        ode.model.Experimenter experimenter for period of
        time.
        Arguments:
        shareId -- 
        exp -- 
        _from -- 
        to -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getEvents(self, shareId, exp, _from, to, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_getEvents.begin(self, ((shareId, exp, _from, to), _response, _ex, _sent, _ctx))

        """
        Gets events for ode.model.Session share per
        ode.model.Experimenter experimenter for period of
        time.
        Arguments:
        shareId -- 
        exp -- 
        _from -- 
        to -- 
        Returns: List of events
        """
        def end_getEvents(self, _r):
            return _M_ode.api.IShare._op_getEvents.end(self, _r)

        """
        Notifies via email selected members of share.
        Arguments:
        shareId -- 
        subject -- 
        message -- 
        html -- 
        _ctx -- The request context for the invocation.
        """
        def notifyMembersOfShare(self, shareId, subject, message, html, _ctx=None):
            return _M_ode.api.IShare._op_notifyMembersOfShare.invoke(self, ((shareId, subject, message, html), _ctx))

        """
        Notifies via email selected members of share.
        Arguments:
        shareId -- 
        subject -- 
        message -- 
        html -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_notifyMembersOfShare(self, shareId, subject, message, html, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IShare._op_notifyMembersOfShare.begin(self, ((shareId, subject, message, html), _response, _ex, _sent, _ctx))

        """
        Notifies via email selected members of share.
        Arguments:
        shareId -- 
        subject -- 
        message -- 
        html -- 
        """
        def end_notifyMembersOfShare(self, _r):
            return _M_ode.api.IShare._op_notifyMembersOfShare.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ISharePrx.ice_checkedCast(proxy, '::ode::api::IShare', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ISharePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IShare'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ISharePrx = IcePy.defineProxy('::ode::api::IShare', ISharePrx)

    _M_ode.api._t_IShare = IcePy.defineClass('::ode::api::IShare', IShare, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IShare._ice_type = _M_ode.api._t_IShare

    IShare._op_activate = IcePy.Operation('activate', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    IShare._op_deactivate = IcePy.Operation('deactivate', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    IShare._op_getShare = IcePy.Operation('getShare', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.model._t_Share, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getMemberCount = IcePy.Operation('getMemberCount', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_LongList, False, 0),), (), ((), _M_ode.sys._t_CountMap, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getOwnShares = IcePy.Operation('getOwnShares', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_bool, False, 0),), (), ((), _M_ode.api._t_SessionList, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getMemberShares = IcePy.Operation('getMemberShares', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_bool, False, 0),), (), ((), _M_ode.api._t_SessionList, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getSharesOwnedBy = IcePy.Operation('getSharesOwnedBy', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), IcePy._t_bool, False, 0)), (), ((), _M_ode.api._t_SessionList, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getMemberSharesFor = IcePy.Operation('getMemberSharesFor', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), IcePy._t_bool, False, 0)), (), ((), _M_ode.api._t_SessionList, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getContents = IcePy.Operation('getContents', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getContentSubList = IcePy.Operation('getContentSubList', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getContentSize = IcePy.Operation('getContentSize', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getContentMap = IcePy.Operation('getContentMap', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_IdListMap, False, 0), (_M_ode._t_ServerError,))
    IShare._op_createShare = IcePy.Operation('createShare', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode._t_RTime, False, 0), ((), _M_ode.api._t_IObjectList, False, 0), ((), _M_ode.api._t_ExperimenterList, False, 0), ((), _M_ode.api._t_StringSet, False, 0), ((), IcePy._t_bool, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IShare._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_setExpiration = IcePy.Operation('setExpiration', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode._t_RTime, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_setActive = IcePy.Operation('setActive', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_bool, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_closeShare = IcePy.Operation('closeShare', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    IShare._op_addObjects = IcePy.Operation('addObjects', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.api._t_IObjectList, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_addObject = IcePy.Operation('addObject', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.model._t_IObject, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_removeObjects = IcePy.Operation('removeObjects', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.api._t_IObjectList, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_removeObject = IcePy.Operation('removeObject', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.model._t_IObject, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_getCommentCount = IcePy.Operation('getCommentCount', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_LongList, False, 0),), (), ((), _M_ode.sys._t_CountMap, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getComments = IcePy.Operation('getComments', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_AnnotationList, False, 0), (_M_ode._t_ServerError,))
    IShare._op_addComment = IcePy.Operation('addComment', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.model._t_TextAnnotation, False, 0), (_M_ode._t_ServerError,))
    IShare._op_addReply = IcePy.Operation('addReply', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_string, False, 0), ((), _M_ode.model._t_TextAnnotation, False, 0)), (), ((), _M_ode.model._t_TextAnnotation, False, 0), (_M_ode._t_ServerError,))
    IShare._op_deleteComment = IcePy.Operation('deleteComment', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, (_M_ode._t_ServerError,))
    IShare._op_getAllMembers = IcePy.Operation('getAllMembers', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_ExperimenterList, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getAllGuests = IcePy.Operation('getAllGuests', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_StringSet, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getAllUsers = IcePy.Operation('getAllUsers', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_StringSet, False, 0), (_M_ode._t_ValidationException, _M_ode._t_ServerError))
    IShare._op_addUsers = IcePy.Operation('addUsers', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.api._t_ExperimenterList, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_addGuests = IcePy.Operation('addGuests', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.api._t_StringSet, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_removeUsers = IcePy.Operation('removeUsers', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.api._t_ExperimenterList, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_removeGuests = IcePy.Operation('removeGuests', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.api._t_StringSet, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_addUser = IcePy.Operation('addUser', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.model._t_Experimenter, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_addGuest = IcePy.Operation('addGuest', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_removeUser = IcePy.Operation('removeUser', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.model._t_Experimenter, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_removeGuest = IcePy.Operation('removeGuest', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_getActiveConnections = IcePy.Operation('getActiveConnections', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_UserMap, False, 0), (_M_ode._t_ServerError,))
    IShare._op_getPastConnections = IcePy.Operation('getPastConnections', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_UserMap, False, 0), (_M_ode._t_ServerError,))
    IShare._op_invalidateConnection = IcePy.Operation('invalidateConnection', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.model._t_Experimenter, False, 0)), (), None, (_M_ode._t_ServerError,))
    IShare._op_getEvents = IcePy.Operation('getEvents', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode._t_RTime, False, 0), ((), _M_ode._t_RTime, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IShare._op_notifyMembersOfShare = IcePy.Operation('notifyMembersOfShare', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_bool, False, 0)), (), None, (_M_ode._t_ServerError,))

    _M_ode.api.IShare = IShare
    del IShare

    _M_ode.api.ISharePrx = ISharePrx
    del ISharePrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
