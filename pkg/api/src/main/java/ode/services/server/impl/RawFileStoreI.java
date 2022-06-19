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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import ode.api.RawFileStore;
import ode.services.server.util.ServerExecutor;
import ode.services.server.util.ServiceFactoryAware;
import ode.services.util.Executor;
import ode.util.SqlAction;
import ode.RLong;
import ode.ServerError;
import ode.api.AMD_RawFileStore_exists;
import ode.api.AMD_RawFileStore_getFileId;
import ode.api.AMD_RawFileStore_read;
import ode.api.AMD_RawFileStore_save;
import ode.api.AMD_RawFileStore_setFileId;
import ode.api.AMD_RawFileStore_size;
import ode.api.AMD_RawFileStore_truncate;
import ode.api.AMD_RawFileStore_write;
import ode.api.RawFileStorePrx;
import ode.api._RawFileStoreOperations;
import ode.api._RawFileStoreTie;
import ode.constants.CLIENTUUID;
import ode.grid.RepositoryPrx;
import ode.grid.RepositoryPrxHelper;
import ode.model.OriginalFile;
import ode.util.IceMapper;
import ode.util.TieAware;

import org.springframework.transaction.annotation.Transactional;

import Ice.Current;
import Ice.TieBase;

/**
 * Implementation of the RawFileStore service.
 * @see ode.api.RawFileStore
 */
public class RawFileStoreI extends AbstractCloseableAmdServant implements
_RawFileStoreOperations, ServiceFactoryAware, TieAware {

    private ServiceFactoryI sf;

    private _RawFileStoreTie tie;

    public RawFileStoreI(RawFileStore service, ServerExecutor be) {
        super(service, be);
    }

    public void setServiceFactory(ServiceFactoryI sf) throws ServerError {
        this.sf = sf;
        setHolder(sf.holder);
    }

    public ServiceFactoryI getServiceFactory() throws ServerError {
        return this.sf;
    }

    public void setTie(TieBase tie) throws ServerError {
        if (!(tie instanceof _RawFileStoreTie)) {
            throw new RuntimeException("Bad tie: " + tie);
        }
        this.tie = (_RawFileStoreTie) tie;
    }

    // Interface methods
    // =========================================================================

    public void exists_async(AMD_RawFileStore_exists __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void read_async(AMD_RawFileStore_read __cb, long position,
            int length, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, position, length);

    }

    public void getFileId_async(AMD_RawFileStore_getFileId __cb,
            Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void setFileId_async(AMD_RawFileStore_setFileId __cb, long fileId,
            Current __current) throws ServerError {

        try {
            if (__redirect(fileId, tie, __current)) {
                __cb.ice_response();
            } else {
                callInvokerOnRawArgs(__cb, __current, fileId);
            }
        } catch (ode.conditions.SecurityViolation e) {
            final ode.SecurityViolation sv = new ode.SecurityViolation();
            IceMapper.fillServerError(sv, e);
            __cb.ice_exception(sv);
        } catch (Exception e) {
            __cb.ice_exception((Exception) e);
        } catch (Throwable e) {
            final ode.InternalException ie = new ode.InternalException();
            IceMapper.fillServerError(ie, e);
            __cb.ice_exception(ie);
        }
    }

    public void size_async(AMD_RawFileStore_size __cb, Current __current)
    throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }

    public void truncate_async(AMD_RawFileStore_truncate __cb, long length,
        Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, length);
    }


    public void write_async(AMD_RawFileStore_write __cb, byte[] buf,
            long position, int length, Current __current) throws ServerError {
        callInvokerOnRawArgs(__cb, __current, buf, position, length);
    }

    public void save_async(AMD_RawFileStore_save __cb, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current);
    }


    //
    // Close logic
    //

    @Override
    protected void preClose(Current current) throws Throwable {
        // no-op
    }

    @Override
    protected void postClose(Current current) {
        // no-op
    }

    //
    // Redirect
    //

    public boolean __redirect(final long fileId, final _RawFileStoreTie rfsTie,
            final Ice.Current current) throws ServerError {

        final String repo = (String) sf.executor
                .executeSql(new Executor.SimpleSqlWork(this,
                        "__redirect", fileId) {
                    @Transactional(readOnly = true)
                    public Object doWork(SqlAction sql) {
                        return sql.fileRepo(fileId);
                    }
                });

        // This implementation is intended only for local use (i.e.
        // ${ode.data.dir})
        if (repo == null) {
            return false;
        }

        // WORKAROUND: store the current session in the context.
        // If this isn't available, it won't be possible for this instance
        // to be registered with
        Map<String, String> adjustedCtx = new HashMap<String, String>(current.ctx);
        adjustedCtx.put(ode.constants.SESSIONUUID.value, current.id.category);
        adjustedCtx.put(ode.constants.CLIENTUUID.value, current.ctx.get(CLIENTUUID.value));

        final Ice.ObjectPrx prx = sf.getAdapter().createProxy(
                Ice.Util.stringToIdentity("PublicRepository-" + repo));
        final RepositoryPrx repoPrx = RepositoryPrxHelper.checkedCast(prx);
        final RawFileStorePrx rfsPrx = repoPrx.fileById(fileId, adjustedCtx);
        OpsDelegate ops = new OpsDelegate(be, rfsTie, this, rfsPrx);
        ops.setApplicationContext(ctx);
        ops.setHolder(holder);
        tie.ice_delegate(ops);
        return true;
    }

    private static class OpsDelegate extends AbstractCloseableAmdServant implements
            _RawFileStoreOperations {

        final private _RawFileStoreTie tie;

        final private RawFileStoreI impl;

        final private RawFileStorePrx prx;

        OpsDelegate(ServerExecutor be, _RawFileStoreTie tie, RawFileStoreI impl, RawFileStorePrx prx) {
            super(null, be);
            this.tie = tie;
            this.impl = impl;
            this.prx = prx;
        }

        public void exists_async(AMD_RawFileStore_exists __cb, Current __current)
                throws ServerError {
            safeRunnableCall(__current, __cb, false, new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return prx.exists();
                }
            });
        }

        public void getFileId_async(AMD_RawFileStore_getFileId __cb, Current __current)
                throws ServerError {
            safeRunnableCall(__current, __cb, false, new Callable<RLong>() {
                public RLong call() throws Exception {
                    return prx.getFileId();
                }
            });
        }

        public void read_async(AMD_RawFileStore_read __cb, final long position,
                final int length, Current __current) throws ServerError {
            safeRunnableCall(__current, __cb, false, new Callable<byte[]>() {
                public byte[] call() throws Exception {
                    return prx.read(position, length);
                }
            });
        }

        public void size_async(AMD_RawFileStore_size __cb, Current __current)
                throws ServerError {
            safeRunnableCall(__current, __cb, false, new Callable<Long>() {
                public Long call() throws Exception {
                    return prx.size();
                }
            });
        }

        public void truncate_async(AMD_RawFileStore_truncate __cb, final long length,
                Current __current) throws ServerError {
            safeRunnableCall(__current, __cb, false, new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return prx.truncate(length);
                }
            });
        }

        public void save_async(AMD_RawFileStore_save __cb, Current __current)
                throws ServerError {
            safeRunnableCall(__current, __cb, false,
                    new Callable<OriginalFile>() {
                        public OriginalFile call() throws Exception {
                            return prx.save();
                        }
                    });
        }

        /**
         * Resets the original implementation, allowing for the creation
         * of another delegate.
         */
        public void setFileId_async(AMD_RawFileStore_setFileId __cb,
                long fileId, Current __current) throws ServerError {

            tie.ice_delegate(impl); // Remove self. Frees for GC.
            tie.setFileId_async(__cb, fileId, __current);

        }

        public void write_async(AMD_RawFileStore_write __cb, final byte[] buf,
                final long position, final int length, Current __current)
                throws ServerError {
            safeRunnableCall(__current, __cb, true, new Callable<Object>() {
                public Object call() throws Exception {
                    prx.write(buf, position, length);
                    return null;
                }
            });
        }

        @Override
        protected void preClose(Current current) throws Throwable {
            prx.close(current.ctx);
        }

        @Override
        protected void postClose(Current current) {
            // no-op
        }

    }
}