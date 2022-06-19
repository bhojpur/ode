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

import ode.system.EventContext;
import ode.conditions.ApiUsageException;
import ode.conditions.SecurityViolation;
import ode.model.IObject;
import ode.model.internal.Details;
import ode.model.meta.ExperimenterGroup;
import ode.security.basic.BasicSecuritySystem;
import ode.security.sharing.SharingSecuritySystem;
import ode.system.Principal;
import ode.system.Roles;

/**
 * Security dispatcher holding each currently active {@link SecuritySystem}
 * instance and allowing dispatching between them.
 */

public class SecuritySystemHolder implements SecuritySystem {

    final protected BasicSecuritySystem basic;

    final protected SharingSecuritySystem sharing;

    protected ThreadLocal<SecuritySystem> current = new ThreadLocal<SecuritySystem>() {
        @Override
        protected SecuritySystem initialValue() {
            return basic;
        }
    };

    public SecuritySystemHolder(ode.security.basic.BasicSecuritySystem basic,
            SharingSecuritySystem sharing) {
        this.basic = basic;
        this.sharing = sharing;
    }

    public SecuritySystem choose() {
        Long shareId = this.basic.getEventContext().getCurrentShareId();
        if (shareId == null) {
            return basic;
        } else {
            return sharing;
        }
    }

    // Delegation
    // =========================================================================

    public Details checkManagedDetails(IObject object, Details trustedDetails)
            throws ApiUsageException, SecurityViolation {
        return choose().checkManagedDetails(object, trustedDetails);
    }

    public void invalidateEventContext() {
        choose().invalidateEventContext();
    }

    public void disable(String... ids) {
        choose().disable(ids);
    }

    public <T extends IObject> T doAction(SecureAction action, T... objs) {
        return choose().doAction(action, objs);
    }

    public void enable(String... ids) {
        choose().enable(ids);
    }

    public EventContext getEventContext() {
        return choose().getEventContext();
    }

    public EventContext getEventContext(boolean refresh) {
        return choose().getEventContext(refresh);
    }

    public Long getEffectiveUID() {
        return choose().getEffectiveUID();
    }

    public Roles getSecurityRoles() {
        return choose().getSecurityRoles();
    }

    public boolean hasPrivilegedToken(IObject obj) {
        return choose().hasPrivilegedToken(obj);
    }

    @Override
    public void checkRestriction(String name, IObject obj) {
        choose().checkRestriction(name, obj);
    }

    public boolean isDisabled(String id) {
        return choose().isDisabled(id);
    }

    public boolean isReady() {
        return choose().isReady();
    }

    public boolean isSystemType(Class<? extends IObject> klass) {
        return choose().isSystemType(klass);
    }

    public void loadEventContext(boolean isReadOnly) {
        choose().loadEventContext(isReadOnly);
    }

    public void login(Principal principal) {
        choose().login(principal);
    }

    public int logout() {
        return choose().logout();
    }

    public Details newTransientDetails(IObject object)
            throws ApiUsageException, SecurityViolation {
        return choose().newTransientDetails(object);
    }

    public void runAsAdmin(AdminAction action) {
        choose().runAsAdmin(action);
    }

    public void runAsAdmin(ExperimenterGroup group, AdminAction action) {
        choose().runAsAdmin(group, action);
    }

    public boolean isGraphCritical(Details details) {
        return choose().isGraphCritical(details);
    }

}