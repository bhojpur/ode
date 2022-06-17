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

import ode.model.IEnum;
import ode.model.IGlobal;
import ode.model.IObject;
import ode.model.internal.Details;
import ode.model.meta.DBPatch;
import ode.model.meta.Event;
import ode.model.meta.EventLog;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.model.meta.GroupExperimenterMap;
import ode.model.meta.Node;
import ode.model.meta.ShareMember;
import ode.system.Roles;

/**
 * Defines what {@link IObject} classes are considered "system" types. System
 * types have special meaning with regard to ACL. They cannot be created except
 * by an administrator, primarily.
 */
public class SystemTypes {

    private final Roles roles;

    public SystemTypes() {
        this(new Roles());
    }

    public SystemTypes(Roles roles) {
        this.roles = roles;
    }

    /**
     * classes which cannot be created by regular users.

     */
    public boolean isSystemType(Class<?> klass) {

        if (klass == null) {
            return false;
        }

        if (ode.model.meta.Session.class.isAssignableFrom(klass)) {
            return true;
        } else if (ShareMember.class.isAssignableFrom(klass)) {
            return true;
        } else if (Node.class.isAssignableFrom(klass)) {
            return true;
        } else if (Experimenter.class.isAssignableFrom(klass)) {
            return true;
        } else if (ExperimenterGroup.class.isAssignableFrom(klass)) {
            return true;
        } else if (GroupExperimenterMap.class.isAssignableFrom(klass)) {
            return true;
        } else if (Event.class.isAssignableFrom(klass)) {
            return true;
        } else if (EventLog.class.isAssignableFrom(klass)) {
            return true;
        } else if (IEnum.class.isAssignableFrom(klass)) {
            return true;
        } else if (DBPatch.class.isAssignableFrom(klass)) {
            return true;
        } else if (IGlobal.class.isAssignableFrom(klass)) {
            return true;
        }

        return false;

    }

    // Make "system" group contents system types.
    public boolean isInSystemGroup(Long groupId) {
        Long systemGroupId = Long.valueOf(roles.getSystemGroupId());
        if (systemGroupId.equals(groupId)) {
            return true;
        }
        return false;
    }

    public boolean isInSystemGroup(Details d) {
        if (d == null || d.getGroup() == null) {
            return false;
        }
        Long groupId = d.getGroup().getId();
        return isInSystemGroup(groupId);
    }

    public boolean isInUserGroup(Long groupId) {
        Long userGroupId = Long.valueOf(roles.getUserGroupId());
        if (userGroupId.equals(groupId)) {
            return true;
        }
        return false;
    }

    public boolean isInUserGroup(Details d) {
        if (d == null || d.getGroup() == null) {
            return false;
        }
        Long groupId = d.getGroup().getId();
        return isInUserGroup(groupId);
    }
}