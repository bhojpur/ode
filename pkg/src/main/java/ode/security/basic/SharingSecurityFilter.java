package ode.security.basic;

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
import java.util.Map;

import ode.system.EventContext;
import ode.model.core.Image;
import ode.model.internal.Details;
import ode.services.sharing.ShareStore;
import ode.services.sharing.data.ShareData;
import ode.system.Roles;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Filter;
import org.hibernate.Session;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * The sharing security filter provides database-level access protection for share contexts.
 * @see ode.security.sharing.SharingACLVoter
 */
public class SharingSecurityFilter extends AbstractSecurityFilter {

    private static final ImmutableMap<String, String> PARAMETER_TYPES =
            ImmutableMap.of("is_admin", "int",
                            "is_share", "int",
                            "images", "long");

    private ShareStore shares;

    /**
     * Construct a new sharing security filter.
     * @param roles the users and groups that are special to Bhojpur ODE
     * @param shares the shares
     */
    public SharingSecurityFilter(Roles roles, ShareStore shares) {
        super(roles);
        this.shares = shares;
    }

    @Override
    public Map<String, String> getParameterTypes() {
        return PARAMETER_TYPES;
    }

    @Override
    public String getDefaultCondition() {
        /* provided instead by annotations */
        return null;
    }

    @Override
    public boolean passesFilter(Session session, Details details, EventContext ec) {
        final Long shareId = ec.getCurrentShareId();
        if (shareId == null) {
            return true;
        }
        final ShareData share = shares.get(shareId);
        return ec.isCurrentUserAdmin() || share != null && share.enabled;
    }

    @Override
    public void enable(Session session, EventContext ec) {
        List<Long> imageIds = null;
        final Long shareId = ec.getCurrentShareId();
        if (shareId != null) {
            final ShareData shareData = shares.get(shareId);
            if (shareData != null && shareData.enabled) {
                imageIds = shareData.objectMap.get(Image.class.getName());
            }
        }
        if (CollectionUtils.isEmpty(imageIds)) {
            imageIds = ImmutableList.of(-1L);
        }
        final int isAdmin01 = ec.isCurrentUserAdmin() ? 1 : 0;
        final int isShare01 = isShare(ec) ? 1 : 0;

        final Filter filter = session.enableFilter(getName());
        filter.setParameter("is_admin", isAdmin01);
        filter.setParameter("is_share", isShare01);
        filter.setParameterList("images", imageIds);
        enableBaseFilters(session, isAdmin01, ec.getCurrentUserId());
    }
}