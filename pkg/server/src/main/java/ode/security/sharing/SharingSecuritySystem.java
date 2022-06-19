package ode.security.sharing;

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
import ode.model.internal.Permissions;
import ode.model.internal.Token;
import ode.model.meta.ExperimenterGroup;
import ode.security.ACLEventListener;
import ode.security.AdminAction;
import ode.security.SecureAction;
import ode.security.SecuritySystem;
import ode.security.basic.BasicSecuritySystem;
import ode.system.Principal;
import ode.system.Roles;

/**
 * central security interface. All queries and actions that deal with a secure
 * context should pass through an implementation of this interface.
 */
public class SharingSecuritySystem implements SecuritySystem {

    private final BasicSecuritySystem delegate;

    public SharingSecuritySystem(BasicSecuritySystem delegate) {
        this.delegate = delegate;
    }

    public Details checkManagedDetails(IObject object, Details trustedDetails)
            throws ApiUsageException, SecurityViolation {
        return null;
    }

    public void invalidateEventContext() {

    }

    public void disable(String... ids) {
        // TODO Auto-generated method stub

    }

    public <T extends IObject> T doAction(SecureAction action, T... objs) {
        // TODO Auto-generated method stub
        return null;
    }

    public void enable(String... ids) {
        // TODO Auto-generated method stub

    }

    public EventContext getEventContext() {
        // TODO Auto-generated method stub
        return null;
    }

    public EventContext getEventContext(boolean refresh) {
        // TODO Auto-generated method stub
        return null;
    }

    public Long getEffectiveUID() {
        return delegate.getEffectiveUID();
    }

    public Roles getSecurityRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasPrivilegedToken(IObject obj) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void checkRestriction(String name, IObject obj) {
        // TODO Auto-generated method stub
    }

    public boolean isDisabled(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isEmptyEventContext() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isReady() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSystemType(Class<? extends IObject> klass) {
        // TODO Auto-generated method stub
        return false;
    }

    public void loadEventContext(boolean isReadOnly) {
        // TODO Auto-generated method stub

    }

    public void login(Principal principal) {
        // TODO Auto-generated method stub

    }

    public int logout() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Details newTransientDetails(IObject object)
            throws ApiUsageException, SecurityViolation {
        // TODO Auto-generated method stub
        return null;
    }

    public void runAsAdmin(ExperimenterGroup group, AdminAction action) {

    }

    public void runAsAdmin(AdminAction action) {
        // TODO Auto-generated method stub

    }

    public boolean isGraphCritical(Details details) {
        // TODO Auto-generated method stub
        return false;
    }
}