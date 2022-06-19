package ode.sys;

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

import static ode.rtypes.rbool;
import static ode.rtypes.rint;
import static ode.rtypes.rlist;
import static ode.rtypes.rlong;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ode.RInt;
import ode.RList;
import ode.RLong;
import ode.RTime;
import ode.RType;

/**
 * Helper subclass of {@link ode.sys.Parameters} for simplifying method
 * parameter creation.
 */
public class ParametersI extends ode.sys.Parameters {

    /**
     * Default constructor creates the {@link #map} instance to prevent later
     * {@link NullPointerException}s. To save memory, it is possible to pass
     * null to {@link ParametersI#ParametersI(Map)}.
     */
    public ParametersI() {
        this.map = new HashMap<String, RType>();
    }

    /**
     * Uses (and does not copy) the given {@code Map<String, RType>} as the
     * named parameter store in this instance. Be careful if either null is
     * passed or if this instance is being used in a multi-threaded environment.
     * No synchronization takes place.
     * @param map the named parameter store to use
     */
    public ParametersI(Map<String, RType> map) {
        this.map = map;
    }

    // ~ Parameters.theFilter.limit & offset
    // =========================================================================

    /**
     * Nulls both the {@link Filter#limit} and {@link Filter#offset} values.
     * @return this instance, for method chaining
     */
    public Parameters noPage() {
        if (this.theFilter != null) {
            this.theFilter.limit = null;
            this.theFilter.offset = null;
        }
        return this;
    }

    /**
     * Sets both the {@link Filter#limit} and {@link Filter#offset} values by
     * wrapping the arguments in {@link RInt} and passing the values to
     * {@link #page(RInt, RInt)}
     * @param offset the offset (to start from)
     * @param limit the limit (maximum to return)
     * @return this instance, for method chaining
     */
    public ParametersI page(int offset, int limit) {
        return this.page(rint(offset), rint(limit));
    }

    /**
     * Creates a {@link Filter} if necessary and sets both {@link Filter#limit}
     * and {@link Filter#offset}.
     * @param offset the offset (to start from)
     * @param limit the limit (maximum to return)
     * @return this instance, for method chaining
     */
    public ParametersI page(RInt offset, RInt limit) {
        if (this.theFilter == null) {
            this.theFilter = new Filter();
        }
        this.theFilter.limit = limit;
        this.theFilter.offset = offset;
        return this;
    }

    /**
     * Returns <code>true</code> if the filter contains a <code>limit</code>
     * <em>OR</em> a <code>offset</code>, <code>false</code> otherwise.
     * 
     * @return See above.
     */
    public boolean isPagination() {
        if (this.theFilter != null) {
            return (null != this.theFilter.limit)
                    || (null != this.theFilter.offset);
        }
        return false;
    }

    /**
     * Returns the value of the <code>offset</code> parameter.
     * 
     * @return See above.
     */
    public RInt getOffset() {
        if (this.theFilter != null) {
            return this.theFilter.offset;
        }
        return null;
    }

    /**
     * Returns the value of the <code>limit</code> parameter.
     * 
     * @return See above.
     */
    public RInt getLimit() {
        if (this.theFilter != null) {
            return this.theFilter.limit;
        }
        return null;
    }
    
    public ParametersI unique() {
        if (this.theFilter == null) {
            this.theFilter = new Filter();
        }
        this.theFilter.unique = rbool(true);
        return this;
    }
    
    public ParametersI noUnique() {
        if (this.theFilter == null) {
            this.theFilter = new Filter();
        }
        this.theFilter.unique = rbool(false);
        return this;
    }
    
    public ode.RBool getUnique() {
        if (this.theFilter != null) {
            return this.theFilter.unique;
        }
        return null;
    }

    // ~ Parameters.theFilter.ownerId & groupId
    // =========================================================================

    /**
     * Sets the value of the <code>experimenter</code> parameter.
     * 
     * @param i
     *            The Id of the experimenter.
     * @return Returns the current object.
     */
    public ParametersI exp(RLong i) {
        if (this.theFilter == null) {
            this.theFilter = new Filter();
        }
        this.theFilter.ownerId = i;
        return this;
    }

    /**
     * Removes the <code>experimenter</code> parameter from the map.
     * 
     * @return Returns the current object.
     */
    public ParametersI allExps() {
        if (this.theFilter != null) {
            this.theFilter.ownerId = null;
        }
        return this;
    }

    /**
     * Returns <code>true</code> if the filter contains and <code>ownerId</code>
     * parameter, <code>false</code> otherwise.
     * 
     * @return See above.
     */
    public boolean isExperimenter() {
        if (this.theFilter != null) {
            return null != this.theFilter.ownerId;
        }
        return false;
    }

    /**
     * Returns the value of the <code>experimenter</code> parameter.
     * 
     * @return See above.
     */
    public RLong getExperimenter() {
        if (this.theFilter != null) {
            return this.theFilter.ownerId;
        }
        return null;
    }

    /**
     * Sets the value of the <code>group</code> parameter.
     * 
     * @param i
     *            The value to set.
     * @return See above.
     */
    public ParametersI grp(RLong i) {
        if (this.theFilter == null) {
            this.theFilter = new Filter();
        }
        this.theFilter.groupId = i;
        return this;
    }

    /**
     * Removes the <code>group</code> parameter from the map.
     * 
     * @return Returns the current object.
     */
    public ParametersI allGrps() {
        if (this.theFilter != null) {
            this.theFilter.groupId = null;
        }
        return this;
    }

    /**
     * Returns <code>true</code> if the filter contains an <code>groupId</code>,
     * <code>false</code> otherwise.
     * 
     * @return See above.
     */
    public boolean isGroup() {
        if (this.theFilter != null) {
            return null != this.theFilter.groupId;
        }
        return false;
    }

    /**
     * Returns the value of the <code>group</code> parameter.
     * 
     * @return See above.
     */
    public RLong getGroup() {
        if (this.theFilter != null) {
            return this.theFilter.groupId;
        }
        return null;
    }

    // ~ Parameters.theFilter.startTime, endTime
    // =========================================================================

    /**
     * Sets the value of the <code>start time</code> parameter.
     * 
     * @param startTime
     *            The time to set.
     * @return Returns the current object.
     */
    public ParametersI startTime(RTime startTime) {
        if (this.theFilter == null) {
            this.theFilter = new Filter();
        }
        this.theFilter.startTime = startTime;
        return this;
    }

    /**
     * Sets the value of the <code>end time</code> parameter.
     * 
     * @param endTime
     *            The time to set.
     * @return Returns the current object.
     */
    public ParametersI endTime(RTime endTime) {
        if (this.theFilter == null) {
            this.theFilter = new Filter();
        }
        this.theFilter.endTime = endTime;
        return this;
    }

    /**
     * Removes the time parameters from the map.
     * 
     * @return Returns the current object.
     */
    public ParametersI allTimes() {
        if (this.theFilter != null) {
            this.theFilter.startTime = null;
            this.theFilter.endTime = null;
        }
        return this;
    }

    /**
     * Returns <code>true</code> if the map contains the <code>start time</code>
     * parameter, <code>false</code> otherwise.
     * 
     * @return See above.
     */
    public boolean isStartTime() {
        if (this.theFilter != null) {
            return null != this.theFilter.startTime;
        }
        return false;
    }

    /**
     * Returns <code>true</code> if the map contains the <code>end time</code>
     * parameter, <code>false</code> otherwise.
     * 
     * @return See above.
     */
    public boolean isEndTime() {
        if (this.theFilter != null) {
            return null != this.theFilter.endTime;
        }
        return false;
    }

    /**
     * Returns the value of the <code>start time</code> parameter.
     * 
     * @return See above.
     */
    public RTime getStartTime() {
        if (this.theFilter != null) {
            return this.theFilter.startTime;
        }
        return null;
    }

    /**
     * Returns the value of the <code>end time</code> parameter.
     * 
     * @return See above.
     */
    public RTime getEndTime() {
        if (this.theFilter != null) {
            return this.theFilter.endTime;
        }
        return null;
    }

    // ~ Parameters.theOption.leaves, orphan, acquisitionData, cacheable
    // =========================================================================

    /**
     * Sets the <code>leaf</code> parameter to <code>true</code>.
     * 
     * @return Returns the current object.
     */
    public ParametersI leaves() {
        if (this.theOptions == null) {
            this.theOptions = new Options();
        }
        this.theOptions.leaves = rbool(true);
        return this;
    }

    /**
     * Sets the <code>leaf</code> parameter to <code>false</code>.
     * 
     * @return Returns the current object.
     */
    public ParametersI noLeaves() {
        if (this.theOptions == null) {
            this.theOptions = new Options();
        }
        this.theOptions.leaves = rbool(false);
        return this;
    }

    public ode.RBool getLeaves() {
        if (this.theOptions != null) {
            return this.theOptions.leaves;
        }
        return null;
    }

    /**
     * Sets the <code>orphan</code> parameter to <code>true</code>.
     * 
     * @return Returns the current object.
     */
    public ParametersI orphan() {
        if (this.theOptions == null) {
            this.theOptions = new Options();
        }
        this.theOptions.orphan = rbool(true);
        return this;
    }

    /**
     * Sets the <code>orphan</code> parameter to <code>false</code>.
     * 
     * @return Returns the current object.
     */
    public ParametersI noOrphan() {
        if (this.theOptions == null) {
            this.theOptions = new Options();
        }
        this.theOptions.orphan = rbool(false);
        return this;
    }

    public ode.RBool getOrphan() {
        if (this.theOptions != null) {
            return this.theOptions.orphan;
        }
        return null;
    }

    /**
     * Sets the <code>acquisition data</code> parameter to <code>true</code>.
     * 
     * @return Returns the current object.
     */
    public ParametersI acquisitionData() {
        if (this.theOptions == null) {
            this.theOptions = new Options();
        }
        this.theOptions.acquisitionData = rbool(true);
        return this;
    }

    /**
     * Sets the <code>acquisition data</code> parameter to <code>false</code>.
     * 
     * @return Returns the current object.
     */
    public ParametersI noAcquisitionData() {
        if (this.theOptions == null) {
            this.theOptions = new Options();
        }
        this.theOptions.acquisitionData = rbool(false);
        return this;
    }

    public ode.RBool getAcquisitionData() {
        if (this.theOptions != null) {
            return this.theOptions.acquisitionData;
        }
        return null;
    }

    /**
     * Set queries to be cacheable. Use with caution.
     * @return this instance, for method chaining
     * @deprecated experimental: may be wholly removed in next major version
     */
    public ParametersI cache() {
        if (theOptions == null) {
            theOptions = new Options();
        }
        theOptions.cacheable = rbool(true);
        return this;
    }

    /**
     * Set queries to not be cacheable. This is the default.
     * @return this instance, for method chaining
     */
    public ParametersI noCache() {
        if (theOptions == null) {
            theOptions = new Options();
        }
        theOptions.cacheable = rbool(false);
        return this;
    }

    /**
     * @return if queries are cacheable
     */
    public ode.RBool getCache() {
        return theOptions == null ? null : theOptions.cacheable;
    }

    // ~ Parameters.map
    // =========================================================================

    public ParametersI add(String name, RType r) {
        this.map.put(name, r);
        return this;
    }

    public ParametersI addId(long id) {
        add(ode.parameters.Parameters.ID, rlong(id));
        return this;
    }

    public ParametersI addId(RLong id) {
        add(ode.parameters.Parameters.ID, id);
        return this;
    }

    public ParametersI addIds(Collection<Long> longs) {
        addLongs(ode.parameters.Parameters.IDS, longs);
        return this;
    }

    public ParametersI addLong(String name, long l) {
        add(name, rlong(l));
        return this;
    }

    public ParametersI addLong(String name, RLong l) {
        add(name, l);
        return this;
    }

    public ParametersI addLongs(String name, Collection<Long> longs) {
        RList rlongs = rlist();
        for (Long l : longs) {
            rlongs.add(rlong(l));
        }
        this.map.put(name, rlongs);
        return this;
    }
}