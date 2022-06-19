package ode.services.eventlogs;

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

import ode.api.ITypes;
import ode.conditions.InternalException;
import ode.model.meta.EventLog;
import ode.util.SqlAction;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * {@link EventLogLoader} implementation which keeps tracks of the last
 * {@link EventLog} instance, and always provides the next unindexed instance.
 * Resetting that saved value would restart indexing.
 */
public abstract class PersistentEventLogLoader extends EventLogLoader {

    /**
     * Key used to look configuration value; 'name'
     */
    protected String key;

    protected ITypes types;

    protected SqlAction sql;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public void setTypes(ITypes types) {
        this.types = types;
    }

    public void setSqlAction(SqlAction sql) {
        this.sql = sql;
    }

    @Override
    protected EventLog query() {

        long current_id = getCurrentId();

        EventLog el = nextEventLog(current_id);
        if (el != null) {
            setCurrentId(el.getId());
        }
        return el;

    }

    /**
     * Called when the configuration database does not contain a valid
     * current_id.
     */
    public abstract void initialize();

    /**
     * Get current {@link EventLog} id. If the lookup throws an exception,
     * either the configuration has been deleted or renamed, in which we need to
     * reinitialize, or the table is missing and something is wrong.
     */
    public long getCurrentId() {
        long current_id;
        try {
            current_id = sql.selectCurrentEventLog(key);
        } catch (EmptyResultDataAccessException erdae) {
            // This event log loader has never been run. Initialize
            current_id = -1;
            setCurrentId(-1);
            initialize();
        } catch (DataAccessException dae) {
            // Most likely there's no configuration table.
            throw new InternalException(
                    "The configuration table seems to be missing \n"
                            + "from your database. Please check your server installation instructions \n"
                            + "for possible reasons.");
        }
        return current_id;
    }

    public void setCurrentId(long id) {
        sql.setCurrentEventLog(id, key);
    }

    public void deleteCurrentId() {
        sql.delCurrentEventLog(key);
    }

    @Override
    public long more() {
        long diff = lastEventLog().getEntityId() - getCurrentId();
        return diff < 0 ? 0 : diff;
    }

}
