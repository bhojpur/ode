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

import java.util.Map;
import java.util.Set;

import ode.api.IShare;
import ode.conditions.SecurityViolation;
import ode.model.IObject;
import ode.model.internal.Details;
import ode.security.basic.BasicACLVoter;
import ode.security.basic.CurrentDetails;
import ode.security.sharing.SharingACLVoter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;

public class CompositeACLVoter implements ACLVoter {

    private final static Logger log = LoggerFactory.getLogger(CompositeACLVoter.class);

    private final CurrentDetails cd;

    private final BasicACLVoter basic;

    private final SharingACLVoter sharing;

    public CompositeACLVoter(CurrentDetails cd, BasicACLVoter basic,
            SharingACLVoter sharing) {
        this.basic = basic;
        this.sharing = sharing;
        this.cd = cd;
    }

    public ACLVoter choose() {
        Long shareId = cd.getCurrentEventContext().getCurrentShareId();
        if (shareId == null || shareId.longValue() < 0) {
            return basic;
        } else {
            return sharing;
        }
    }

    // Delegation
    // =========================================================================

    public boolean allowChmod(IObject object) {
        return choose().allowChmod(object);
    }

    public boolean allowCreation(IObject object) {
        return choose().allowCreation(object);
    }

    public boolean allowDelete(IObject object, Details trustedDetails) {
        return choose().allowDelete(object, trustedDetails);
    }

    public boolean allowLoad(Session session,Class<? extends IObject> klass,
            Details trustedDetails, long id) {
        return choose().allowLoad(session, klass, trustedDetails, id);
    }

    public boolean allowAnnotate(IObject object, Details trustedDetails) {
        return choose().allowAnnotate(object, trustedDetails);
    }

    public boolean allowUpdate(IObject object, Details trustedDetails) {
        return choose().allowUpdate(object, trustedDetails);
    }

    public void throwCreationViolation(IObject object) throws SecurityViolation {
        choose().throwCreationViolation(object);
    }

    public void throwDeleteViolation(IObject object) throws SecurityViolation {
        choose().throwDeleteViolation(object);
    }

    public void throwLoadViolation(IObject object) throws SecurityViolation {
        choose().throwLoadViolation(object);
    }

    public void throwUpdateViolation(IObject object) throws SecurityViolation {
        choose().throwUpdateViolation(object);
    }

    @Override
    public Set<String> restrictions(IObject object) {
        return choose().restrictions(object);
    }

    @Override
    public void setPermittedClasses(Map<Integer, Set<Class<? extends IObject>>> objectClassesPermitted) {
        basic.setPermittedClasses(objectClassesPermitted);
    }

    public void postProcess(IObject object) {
        choose().postProcess(object);
    }
}