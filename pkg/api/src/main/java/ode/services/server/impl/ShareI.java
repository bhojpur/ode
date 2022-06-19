package ode.services.server.impl;

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

import ode.api.IShare;
import ode.services.server.util.ServerExecutor;
import ode.RTime;
import ode.ServerError;
import ode.ValidationException;
import ode.api.AMD_IShare_activate;
import ode.api.AMD_IShare_addComment;
import ode.api.AMD_IShare_addGuest;
import ode.api.AMD_IShare_addGuests;
import ode.api.AMD_IShare_addObject;
import ode.api.AMD_IShare_addObjects;
import ode.api.AMD_IShare_addReply;
import ode.api.AMD_IShare_addUser;
import ode.api.AMD_IShare_addUsers;
import ode.api.AMD_IShare_closeShare;
import ode.api.AMD_IShare_createShare;
import ode.api.AMD_IShare_deactivate;
import ode.api.AMD_IShare_deleteComment;
import ode.api.AMD_IShare_getActiveConnections;
import ode.api.AMD_IShare_getAllGuests;
import ode.api.AMD_IShare_getAllMembers;
import ode.api.AMD_IShare_getAllUsers;
import ode.api.AMD_IShare_getCommentCount;
import ode.api.AMD_IShare_getComments;
import ode.api.AMD_IShare_getContentMap;
import ode.api.AMD_IShare_getContentSize;
import ode.api.AMD_IShare_getContentSubList;
import ode.api.AMD_IShare_getContents;
import ode.api.AMD_IShare_getEvents;
import ode.api.AMD_IShare_getMemberCount;
import ode.api.AMD_IShare_getMemberShares;
import ode.api.AMD_IShare_getMemberSharesFor;
import ode.api.AMD_IShare_getOwnShares;
import ode.api.AMD_IShare_getPastConnections;
import ode.api.AMD_IShare_getShare;
import ode.api.AMD_IShare_getSharesOwnedBy;
import ode.api.AMD_IShare_invalidateConnection;
import ode.api.AMD_IShare_removeGuest;
import ode.api.AMD_IShare_removeGuests;
import ode.api.AMD_IShare_removeObject;
import ode.api.AMD_IShare_removeObjects;
import ode.api.AMD_IShare_removeUser;
import ode.api.AMD_IShare_removeUsers;
import ode.api.AMD_IShare_setActive;
import ode.api.AMD_IShare_setDescription;
import ode.api.AMD_IShare_setExpiration;
import ode.api.AMD_IShare_notifyMembersOfShare;
import ode.api._IShareOperations;
import ode.model.Annotation;
import ode.model.Experimenter;
import ode.model.IObject;
import ode.model.TextAnnotation;
import Ice.Current;

/**
 * Implementation of the IShare service.
 * @see ode.api.IShare
 */
public class ShareI extends AbstractAmdServant implements _IShareOperations {

    public ShareI(IShare service, ServerExecutor be) {
        super(service, be);
    }

    // Interface methods
    // =========================================================================

    public void activate_async(AMD_IShare_activate __cb, long shareId,
            Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId);

    }

    public void deactivate_async(AMD_IShare_deactivate __cb,
            Current __current) {
        callInvokerOnRawArgs(__cb, __current);

    }

    public void addComment_async(AMD_IShare_addComment __cb, long shareId,
            String comment, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, comment);

    }

    public void addGuest_async(AMD_IShare_addGuest __cb, long shareId,
            String emailAddress, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, emailAddress);

    }

    public void addObject_async(AMD_IShare_addObject __cb, long shareId,
            IObject iobject, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, iobject);

    }

    public void addObjects_async(AMD_IShare_addObjects __cb, long shareId,
            List<IObject> iobjects, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, iobjects);

    }

    public void addReply_async(AMD_IShare_addReply __cb, long shareId,
            String comment, TextAnnotation replyTo, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, comment, replyTo);

    }

    public void addUser_async(AMD_IShare_addUser __cb, long shareId,
            Experimenter exp, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, exp);

    }

    public void addUsers_async(AMD_IShare_addUsers __cb, long shareId,
            List<Experimenter> exps, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, exps);

    }

    public void closeShare_async(AMD_IShare_closeShare __cb, long shareId,
            Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId);

    }

    public void deleteComment_async(AMD_IShare_deleteComment __cb,
            Annotation comment, Current __current) {
        callInvokerOnRawArgs(__cb, __current, comment);

    }

    public void getAllGuests_async(AMD_IShare_getAllGuests __cb, long shareId,
            Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId);

    }

    public void getAllMembers_async(AMD_IShare_getAllMembers __cb,
            long shareId, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId);

    }

    public void getAllUsers_async(AMD_IShare_getAllUsers __cb, long shareId,
            Current __current) throws ValidationException {
        callInvokerOnRawArgs(__cb, __current, shareId);

    }

    public void getCommentCount_async(AMD_IShare_getCommentCount __cb,
            List<Long> shareIds, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, shareIds);

    }
    
    public void getComments_async(AMD_IShare_getComments __cb, long shareId,
            Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId);

    }

    public void getContentMap_async(AMD_IShare_getContentMap __cb,
            long shareId, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId);

    }

    public void getContentSize_async(AMD_IShare_getContentSize __cb,
            long shareId, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId);

    }

    public void getContentSubList_async(AMD_IShare_getContentSubList __cb,
            long shareId, int start, int finish, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, start, finish);

    }

    public void getContents_async(AMD_IShare_getContents __cb, long shareId,
            Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId);

    }
    
    public void getMemberCount_async(AMD_IShare_getMemberCount __cb,
            List<Long> shareIds, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, shareIds);

    }

    public void getMemberSharesFor_async(AMD_IShare_getMemberSharesFor __cb,
            Experimenter user, boolean active, Current __current) {
        callInvokerOnRawArgs(__cb, __current, user, active);

    }

    public void getMemberShares_async(AMD_IShare_getMemberShares __cb,
            boolean active, Current __current) {
        callInvokerOnRawArgs(__cb, __current, active);

    }

    public void getOwnShares_async(AMD_IShare_getOwnShares __cb,
            boolean active, Current __current) {
        callInvokerOnRawArgs(__cb, __current, active);

    }

    public void getShare_async(AMD_IShare_getShare __cb, long shareId,
            Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId);

    }

    public void getSharesOwnedBy_async(AMD_IShare_getSharesOwnedBy __cb,
            Experimenter user, boolean active, Current __current) {
        callInvokerOnRawArgs(__cb, __current, user, active);

    }

    public void removeGuest_async(AMD_IShare_removeGuest __cb, long shareId,
            String emailAddress, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, emailAddress);

    }

    public void removeObject_async(AMD_IShare_removeObject __cb, long shareId,
            IObject iobject, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, iobject);

    }

    public void removeObjects_async(AMD_IShare_removeObjects __cb,
            long shareId, List<IObject> iobjects, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, iobjects);

    }

    public void removeUser_async(AMD_IShare_removeUser __cb, long shareId,
            Experimenter exp, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, exp);

    }

    public void removeUsers_async(AMD_IShare_removeUsers __cb, long shareId,
            List<Experimenter> exps, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, exps);

    }

    public void setActive_async(AMD_IShare_setActive __cb, long shareId,
            boolean active, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, active);

    }

    public void setDescription_async(AMD_IShare_setDescription __cb,
            long shareId, String description, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, description);

    }

    public void setExpiration_async(AMD_IShare_setExpiration __cb,
            long shareId, RTime expiration, Current __current) {
        callInvokerOnRawArgs(__cb, __current, shareId, expiration);

    }

    public void addGuests_async(AMD_IShare_addGuests __cb, long shareId,
            List<String> emailAddresses, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, shareId, emailAddresses);

    }

    public void createShare_async(AMD_IShare_createShare __cb,
            String description, RTime expiration, List<IObject> items,
            List<Experimenter> exps, List<String> guests, boolean enabled,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, description, expiration, items,
                exps, guests, enabled);
    }

    public void getActiveConnections_async(
            AMD_IShare_getActiveConnections __cb, long shareId,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, shareId);
    }

    public void getEvents_async(AMD_IShare_getEvents __cb, long shareId,
            Experimenter exp, RTime from, RTime to, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, shareId, exp, from, to);
    }

    public void getPastConnections_async(AMD_IShare_getPastConnections __cb,
            long shareId, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, shareId);
    }

    public void invalidateConnection_async(
            AMD_IShare_invalidateConnection __cb, long shareId,
            Experimenter exp, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, shareId, exp);
    }

    public void notifyMembersOfShare_async(
            AMD_IShare_notifyMembersOfShare __cb, long shareId, String subject,
            String message, boolean html, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, shareId, subject, message, html);
    }

    public void removeGuests_async(AMD_IShare_removeGuests __cb, long shareId,
            List<String> emailAddresses, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, shareId, emailAddresses);
    }


}