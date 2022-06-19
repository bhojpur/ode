package ode.services.server.impl;

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

import Ice.Current;
import ode.services.server.util.UnregisterServantMessage;
import ode.util.messages.InternalMessage;
import ode.RType;
import ode.ServerError;
import ode.api.StatefulServiceInterfacePrx;
import ode.api.StatefulServiceInterfacePrxHelper;
import ode.api._StatefulServiceInterfaceOperations;
import ode.api._StatefulServiceInterfaceTie;
import ode.grid.*;
import ode.model.OriginalFile;

import java.util.Map;

/**
 * Bhojpur ODE server specific wrapper for table proxies to facilitate the
 * cleanup of files on session exit.
 */
public class TableCloserI extends AbstractCloseableAmdServant
    implements _StatefulServiceInterfaceOperations, _TableOperations {

    /**
     * Proxy to the remote table which should only be used internally to this instance.
     */
    private final TablePrx table;

    /**
     * Proxy to this instance which will be passed to clients.
     */
    private final TablePrx self;

    private final Ice.Identity tableId;

    private final Ice.Identity statefulId;

    public TableCloserI(ServiceFactoryI sf, TablePrx tablePrx, Ice.Identity id)
            throws ode.ServerError {
        super(null, null);
        this.table = tablePrx;
        if (this.table == null) {
            throw new ode.InternalException(null, null, "null table as argument");
        }
        this.tableId = id;
        this.statefulId = new Ice.Identity(id.name + "-closer", id.category);
        this.self = TablePrxHelper.uncheckedCast(
                sf.registerServant(tableId, new _TableTie(this)));
        StatefulServiceInterfacePrx closer = StatefulServiceInterfacePrxHelper.uncheckedCast(
                sf.registerServant(statefulId, new _StatefulServiceInterfaceTie(this)));
        sf.allow(self);
        sf.allow(closer);
    }

    public TablePrx getProxy() {
        return self;
    }

    // CLOSEABLE

    @Override
    protected void preClose(Current current) throws Throwable {
        try {
            this.table.close();
        } catch (Ice.ObjectNotExistException onee) {
            // already closed, e.g. by table.delete()
        } finally {
            Ice.Current copy = new Ice.Current();
            copy.adapter = current.adapter;
            copy.con = current.con;
            copy.ctx = current.ctx;
            copy.encoding = current.encoding;
            copy.facet = current.facet;
            copy.id = new Ice.Identity(current.id.name + "-closer", current.id.category);
            copy.mode = current.mode;
            copy.operation = current.operation;
            copy.requestId = current.requestId;
            InternalMessage msg = new UnregisterServantMessage(this, copy, holder);
            ctx.publishMessage(msg);
        }
    }

    @Override
    protected void postClose(Current current) {

    }

    // DELEGATION

    private Map<String, String> check(Current __current) {
        if (__current != null) {
            return __current.ctx;
        }
        return null;
    }

    @Override
    public OriginalFile getOriginalFile(Current __current) throws ServerError {
        return table.getOriginalFile(check(__current));
    }

    @Override
    public Column[] getHeaders(Current __current) throws ServerError {
        return table.getHeaders(check(__current));
    }

    @Override
    public long getNumberOfRows(Current __current) throws ServerError {
        return table.getNumberOfRows(check(__current));
    }

    @Override
    public long[] getWhereList(String condition, Map<String, RType> variables, long start, long stop, long step, Current __current) throws ServerError {
        return table.getWhereList(condition, variables, start, stop, step, check(__current));
    }

    @Override
    public Data readCoordinates(long[] rowNumbers, Current __current) throws ServerError {
        return table.readCoordinates(rowNumbers, check(__current));
    }

    @Override
    public Data read(long[] colNumbers, long start, long stop, Current __current) throws ServerError {
        return table.read(colNumbers, start, stop, check(__current));
    }

    @Override
    public Data slice(long[] colNumbers, long[] rowNumbers, Current __current) throws ServerError {
        return table.slice(colNumbers, rowNumbers, check(__current));
    }

    @Override
    public void addData(Column[] cols, Current __current) throws ServerError {
        table.addData(cols, check(__current));
    }

    @Override
    public void update(Data modifiedData, Current __current) throws ServerError {
        table.update(modifiedData, check(__current));
    }

    @Override
    public Map<String, RType> getAllMetadata(Current __current) throws ServerError {
        return table.getAllMetadata(check(__current));
    }

    @Override
    public RType getMetadata(String key, Current __current) throws ServerError {
        return table.getMetadata(key, check(__current));
    }

    @Override
    public void setAllMetadata(Map<String, RType> dict, Current __current) throws ServerError {
        table.setAllMetadata(dict, check(__current));
    }

    @Override
    public void setMetadata(String key, RType value, Current __current) throws ServerError {
        table.setMetadata(key, value, check(__current));
    }

    @Override
    public void initialize(Column[] cols, Current __current) throws ServerError {
        table.initialize(cols, check(__current));
    }

    @Override
    public int addColumn(Column col, Current __current) throws ServerError {
        return table.addColumn(col, check(__current));
    }

    @Override
    public void delete(Current __current) throws ServerError {
        table.delete(check(__current));
    }

}