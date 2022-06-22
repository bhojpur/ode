package ode.system;

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
import java.util.Set;

import ode.model.enums.AdminPrivilege;
import ode.model.internal.Permissions;

/**
 * manages authenticated principals and other context for a given event. Just as
 * all API method calls take place in a transaction and a session (in that
 * order), they also take place within an Event.
 * 
 * @see ode.model.meta.Experimenter
 * @see ode.model.meta.ExperimenterGroup
 */
public interface EventContext {

    Long getCurrentShareId();

    Long getCurrentSessionId();

    String getCurrentSessionUuid();

    Long getCurrentUserId();

    String getCurrentUserName();

    Long getCurrentSudoerId();

    String getCurrentSudoerName();

    Long getCurrentGroupId();

    String getCurrentGroupName();

    boolean isCurrentUserAdmin();

    Set<AdminPrivilege> getCurrentAdminPrivileges();

    boolean isReadOnly();

    Long getCurrentEventId();

    String getCurrentEventType();

    List<Long> getMemberOfGroupsList();

    List<Long> getLeaderOfGroupsList();

    Permissions getCurrentGroupPermissions();

}