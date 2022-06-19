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

import java.util.Map;
import java.util.Set;

import ode.api.IShare;
import ode.conditions.SecurityViolation;
import ode.model.IObject;
import ode.model.internal.Details;
import ode.model.internal.Permissions;
import ode.security.ACLVoter;
import ode.security.SystemTypes;
import ode.security.basic.CurrentDetails;
import ode.security.basic.TokenHolder;
import ode.services.sharing.ShareStore;
import ode.services.sharing.data.ShareData;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class SharingACLVoter implements ACLVoter {

    private final static Logger log = LoggerFactory.getLogger(SharingACLVoter.class);

    private final SystemTypes sysTypes;

    private final ShareStore store;

    private final CurrentDetails cd;

    private final TokenHolder tokenHolder;

    public SharingACLVoter(CurrentDetails cd, SystemTypes sysTypes,
            ShareStore store, TokenHolder tokenHolder) {
        this.tokenHolder = tokenHolder;
        this.sysTypes = sysTypes;
        this.store = store;
        this.cd = cd;
    }

    // ~ Interface methods
    // =========================================================================

    /**
     * 
     */
    public boolean allowChmod(IObject iObject) {
        return false;
    }

    /**
     * 
     */
    public boolean allowLoad(Session session, Class<? extends IObject> klass, Details d, long id) {
        Assert.notNull(klass);
        // Assert.notNull(d);
        if (d == null ||
                sysTypes.isSystemType(klass) ||
                sysTypes.isInSystemGroup(d)) {
            return true;
        }
        long sessionID = cd.getCurrentEventContext().getCurrentShareId();
        ShareData data = store.get(sessionID);
        if (data.enabled) {
            return store.contains(sessionID, klass, id);
        }
        return false;
    }

    public void throwLoadViolation(IObject iObject) throws SecurityViolation {
        Assert.notNull(iObject);
        throw new SecurityViolation(iObject + " not contained in share");
    }

    public boolean allowCreation(IObject iObject) {
        if (tokenHolder.hasPrivilegedToken(iObject)) {
            return true;
        }
        return false;
    }

    public void throwCreationViolation(IObject iObject)
            throws SecurityViolation {
        throwDisabled("Creation");
    }

    public boolean allowAnnotate(IObject iObject, Details trustedDetails) {
        return false;
    }

    public boolean allowUpdate(IObject iObject, Details trustedDetails) {
        return false;
    }

    public void throwUpdateViolation(IObject iObject) throws SecurityViolation {
        throwDisabled("Update");
    }

    public boolean allowDelete(IObject iObject, Details trustedDetails) {
        return false;
    }

    public void throwDeleteViolation(IObject iObject) throws SecurityViolation {
        throwDisabled("Delete");
    }

    @Override
    public Set<String> restrictions(IObject object) {
        return null;
    }

    @Override
    public void setPermittedClasses(Map<Integer, Set<Class<? extends IObject>>> objectClassesPermitted) {
    }

    @Override
    public void postProcess(IObject object) {
        if (object != null && object.isLoaded()) {
            Details d = object.getDetails();
            Permissions p = d.getPermissions();
            Permissions copy = new Permissions(p);
            copy.copyRestrictions(0, null);
            d.setPermissions(copy);
        }
    }

    // Helpers
    // =========================================================================
    protected void throwDisabled(String action) {
        throw new SecurityViolation(action + " is not allowed while in share.");
    }
}