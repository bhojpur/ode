package ode.parameters;

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

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * parameter to generally reduce the size of a query result set.
 */
public class Filter implements Serializable {

    /**
     * flag determining if a {@code ode.services.query.Query} will attempt to
     * return a single value <em>if supported</em>.
     */
    private boolean unique = false;

    private long id_owner = -1, id_group = -1;

    public Integer limit;
    
    public Integer offset;

    public Timestamp startTime, endTime;

    public Filter page(Integer offset, Integer limit) {
        this.offset = offset;
        this.limit = limit;
        return this;
    }
    
    // ~ Flags
    // =========================================================================
    /**
     * state that this Filter should only return a single value if possible. By
     * default, a Filter will make no assumptions regarding the uniquesness of a
     * query.
     */
    public Filter unique() {
        unique = true;
        return this;
    }

    /**
     * check uniqueness for this query. Participating queries will attempt to
     * call <code>uniqueResult</code> rather than <code>list</code>. This may
     * throw a {@link ode.conditions.ValidationException} on execution.
     */
    public boolean isUnique() {
        return unique;
    }

    // ~ Owner
    // =========================================================================
    public Filter owner(long ownerId) {
        id_owner = ownerId;
        return this;
    }

    public long owner() {
        return id_owner;
    }

    public Filter group(long groupId) {
        id_group = groupId;
        return this;
    }

    public long group() {
        return id_group;
    }

    // ~ Serialization
    // =========================================================================
    private static final long serialVersionUID = 60649802598825408L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("F(");
        if (offset != null) {
            sb.append("o");
            sb.append(offset);
        }
        if (limit != null) {
            sb.append("l");
            sb.append(limit);
        }
        if (id_owner >= 0) {
            sb.append("u");
            sb.append(id_owner);
        }
        if (id_group >= 0) {
            sb.append("g");
            sb.append(id_group);
        }
        if (unique) {
            sb.append("U");
        }
        sb.append(")");
        return sb.toString();
    }
}