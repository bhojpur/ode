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

import ode.system.EventContext;
import org.hibernate.Session;

import ode.model.internal.Details;
import ode.security.basic.AllGroupsSecurityFilter;
import ode.security.basic.CurrentDetails;
import ode.security.basic.OneGroupSecurityFilter;
import ode.security.basic.SharingSecurityFilter;

/**
 * Security dispatcher holding each currently active {@link SecurityFilter}
 * instance and allowing dispatching between them.
 */
public class SecurityFilterHolder implements SecurityFilter {

    final protected AllGroupsSecurityFilter allgroups;

    final protected OneGroupSecurityFilter onegroup;

    final protected SharingSecurityFilter share;

    final protected CurrentDetails cd;

    protected ThreadLocal<SecurityFilter> current = new ThreadLocal<SecurityFilter>() {
        @Override
        protected SecurityFilter initialValue() {
            return onegroup;
        }
    };

    public SecurityFilterHolder(CurrentDetails cd,
            OneGroupSecurityFilter onegroup,
            AllGroupsSecurityFilter allgroups,
            SharingSecurityFilter share) {
        this.cd = cd;
        this.onegroup = onegroup;
        this.allgroups = allgroups;
        this.share = share;
    }

    public SecurityFilter choose() {
        final EventContext ec = cd.getCurrentEventContext();
        final Long groupId = ec.getCurrentGroupId();
        final Long shareId = ec.getCurrentShareId();
        if (shareId != null && shareId >= 0) {
            return share;
        } else if (groupId < 0) {
            return allgroups;
        } else {
            return onegroup;
        }
    }

    // Delegation
    // =========================================================================

    public String getName() {
        return choose().getName();
    }

    public String getDefaultCondition() {
        return choose().getDefaultCondition();
    }

    public Map<String, String> getParameterTypes() {
        return choose().getParameterTypes();
    }

    public void enable(Session sess, EventContext ec) {
        choose().enable(sess, ec);
    }

    public void disable(Session sess) {
        choose().disable(sess);
    }

    public boolean passesFilter(Session s, Details d, EventContext c) {
        return choose().passesFilter(s, d, c);
    }

}