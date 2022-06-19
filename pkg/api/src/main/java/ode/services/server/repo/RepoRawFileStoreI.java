package ode.services.server.repo;

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

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Current;

import ode.api.RawFileStore;
import ode.services.server.impl.RawFileStoreI;
import ode.services.server.util.ServerExecutor;

import ode.ServerError;
import ode.api.AMD_RawFileStore_read;
import ode.api.AMD_RawFileStore_setFileId;
import ode.api.AMD_RawFileStore_write;

/**
 * An implementation of the RepoRawFileStore interface
 */
public class RepoRawFileStoreI extends RawFileStoreI {

    private final static Logger log = LoggerFactory.getLogger(RepoRawFileStoreI.class);

    /**
     * Interface which can be implemented in order to inject logic into various
     * {@link RepoRawFileStoreI} methods. These are only called if the callback
     * instance is set via {@link RepoRawFileStoreI#setCallback(Callback)}.
     *
     */
    interface Callback {

        /**
         * Called if and only if the write method is successful.
         */
        void onWrite(byte[] buf, long position, long length);

        /**
         * Called in a finally block after
         * {@link RepoRawFileStoreI#postClose(Current)} completes.
         */
        void onPreClose();

        /**
         * Called in a finally block after
         * {@link RepoRawFileStoreI#postClose(Current)} completes.
         */
        void onPostClose();
    }

    /**
     * Implementation of {@link Callback} which does nothing for each of the
     * methods. Each method can be easily overridden in order to provide a
     * subset of the callback methods.
     */
    static class NoOpCallback implements Callback {

        @Override
        public void onWrite(byte[] buf, long position, long length) {
             // no-op
        }

        @Override
        public void onPreClose() {
            // no-op
        }

        @Override
        public void onPostClose() {
            // no-op
        }

    }

    private AtomicReference<Callback> cb = new AtomicReference<Callback>();

    public RepoRawFileStoreI(ServerExecutor be, RawFileStore service, Ice.Current curr) {
        super(service, be);
    }

    public void setCallback(Callback cb) {
        this.cb.set(cb);
    }

    @Override
    public void setFileId_async(AMD_RawFileStore_setFileId __cb, long fileId,
            Current __current) throws ServerError {
        ode.ApiUsageException aue = new ode.ApiUsageException();
        aue.message = "Cannot reset id to " + fileId;
        __cb.ice_exception(aue);
    }

    @Override
    public void read_async(AMD_RawFileStore_read __cb, long position,
            int length, Current __current) throws ServerError {

        if (length > 64 * 1000 * 1000) {
            __cb.ice_exception(new ode.ApiUsageException(null, null,
                    "Too big: " + length));
            return; // EARLY EXIT!
        }
        super.read_async(__cb, position, length, __current);
    }

    @Override
    public void write_async(AMD_RawFileStore_write __cb, byte[] buf,
            long position, int length, Current __current) throws ServerError {
        super.write_async(__cb, buf, position, length, __current);
        Callback cb = this.cb.get();
        if (cb != null) {
            cb.onWrite(buf, position, length);
        }
    }

    @Override
    protected void preClose(Current current) throws Throwable {
        try {
            super.preClose(current);
        } finally {
            Callback cb = this.cb.get();
            if (cb != null) {
                cb.onPreClose();
            }
        }
    }

    @Override
    protected void postClose(Ice.Current c) {
        try {
            super.postClose(c);
        } finally {
            Callback cb = this.cb.get();
            if (cb != null) {
                cb.onPostClose();
            }
        }
    }
}