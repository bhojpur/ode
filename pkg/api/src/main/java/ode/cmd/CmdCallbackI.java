package ode.cmd;

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

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import ode.ServerError;
import Ice.Current;

/**
 *
 * Callback servant used to wait until a HandlePrx would
 * return non-null on getReponse. The server will notify
 * of completion to prevent constantly polling on
 * getResponse. Subclasses can override methods for handling
 * based on the completion status.
 *
 * Example usage:
 * <pre>
 *      cb = new CmdCallbackI(client, handle);
 *      response = null;
 *      while (response == null) {
 *          response = cb.block(500);
 *      }
 *
 *      // or
 *
 *      response = cb.loop(5, 500);
 * </pre>
 *
 * Subclasses which depend on the proper ordering of either initialization
 * or calls to {@link #onFinished(Response, Status, Current)} should make
 * use of the {@link #initializationDone()} and {@link #waitOnInitialization()},
 * or the {@link #onFinishedDone()} and {@link #waitOnFinishedDone()} methods.
 *
 * @see #initializationDone()
 * @see #onFinishedDone()
 */
public class CmdCallbackI extends _CmdCallbackDisp {

    private static class State {
        final Response rsp;
        final Status status;

        State(Response rsp, Status status) {
                this.rsp = rsp;
                this.status = status;
        }
    }

    private static final long serialVersionUID = 1L;

    private final Ice.ObjectAdapter adapter;

    private final Ice.Identity id;

    /**
     * Latch which is released once {@link #finished(Response, Status, Current)} is
     * called. Other methods will block on this value.
     */
    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * @see #initializationDone()
     * @see #waitOnInitialization()
     */
    private final CountDownLatch isInitialized = new CountDownLatch(1);

    /**
     * @see #onFinishedDone()
     * @see #waitOnFinishedDone()
     */
    private final CountDownLatch isOnFinishedDone = new CountDownLatch(1);

    private final AtomicReference<State> state = new AtomicReference<State>(
            new State(null, null));

    /**
     * Proxy passed to this instance on creation. Can be used by subclasses
     * freely. The object will not be nulled, but may be closed server-side.
     */
    protected final HandlePrx handle;

    public CmdCallbackI(ode.client client, HandlePrx handle)
    throws ServerError {
        this(client.getAdapter(), client.getCategory(), handle);
    }

    public CmdCallbackI(CmdCallbackI ccb) throws ServerError {
        this(ccb.adapter, ccb.id.category, ccb.handle);
    }
    
    public CmdCallbackI(Ice.ObjectAdapter adapter, String category,
            HandlePrx handle)
        throws ServerError {

        this.adapter = adapter;
        this.handle = handle;
        this.id = new Ice.Identity();
        this.id.name = UUID.randomUUID().toString();
        this.id.category = category;
        Ice.ObjectPrx prx = adapter.add(this, id);
        CmdCallbackPrx cb = CmdCallbackPrxHelper.uncheckedCast(prx);
        handle.addCallback(cb);
        initialPoll();
    }

    //
    // Subclass initialization
    //

    /**
     * Called at the end of construction to check a race condition.
     *
     * If {@link HandlePrx} finishes its execution before the
     * {@link CmdCallbackPrx} has been sent set via addCallback,
     * then there's a chance that this implementation will never
     * receive a call to finished, leading to perceived hangs.
     *
     * By default, this method starts a background thread and
     * calls {@link #poll()}. An {@link Ice.ObjectNotExistException}
     * implies that another caller has already closed the
     * {@link HandlePrx}.
     */
    protected void initialPoll() {
        // Now check just in case the process exited VERY quickly
        new Thread() {
            public void run() {
                try {
                    poll();
                } catch (Exception e) {
                    // don't throw any exceptions, e. g. if the handle
                    // has already been closed
                    onFinished(null, null, null);
                }
            }
        }.start();
    }

    /**
     * Subclasses which must perform their own initialization before
     * {@link #onFinished(Response, Status, Current)} is called should
     * call {@link #initializationDone()} once that setup is complete.
     */
    protected void initializationDone() {
        isInitialized.countDown();
    }

    /**
     * Subclasses which must perform their own initialization before
     * {@link #onFinished(Response, Status, Current)} is called should
     * call {@link #waitOnInitialization()} before accessing any initialized
     * state.
     */
    protected void waitOnInitialization() {
        try {
            isInitialized.await();
        } catch (InterruptedException ie) {
            // pass
        }
    }

    protected void onFinishedDone() {
        isOnFinishedDone.countDown();
    }

    protected void waitOnFinishedDone() {
        try {
            isOnFinishedDone.await();
        } catch (InterruptedException ie) {
            // pass
        }
    }

    //
    // Local invocations
    //

    /**
     * Returns possibly null Response value. If null, then neither has
     * the remote server nor the local poll method called finish with
     * non-null values.
     * @return the response, may be {@code null}
     */
    public Response getResponse() {
        return state.get().rsp;
    }

    /**
     * Returns possibly null Status value. If null, then neither has
     * the remote server nor the local poll method called finish with
     * non-null values.
     * @return the status, may be {@code null}
     */
    public Status getStatus() {
        return state.get().status;
    }

    protected Status getStatusOrThrow() {
        Status s = getStatus();
        if (s == null) {
            throw new ode.ClientError("Status not present!");
        }
        return s;
    }

    /**
     * Returns whether Status::CANCELLED is contained in
     * the flags variable of the Status instance. If no
     * Status is available, a ClientError will be thrown.
     * @return if {@link ode.cmd.State#CANCELLED} has been flagged
     */
    public boolean isCancelled() {
        Status s = getStatusOrThrow();
        return s.flags.contains(ode.cmd.State.CANCELLED);
    }

    /**
     * Returns whether Status::FAILURE is contained in
     * the flags variable of the Status instance. If no
     * Status is available, a ClientError will be thrown.
     * @return if {@link ode.cmd.State#FAILURE} has been flagged
     */
    public boolean isFailure() {
        Status s = getStatusOrThrow();
        return s.flags.contains(ode.cmd.State.FAILURE);
    }

    /**
     * Calls block(long) "loops" number of times with the "ms"
     * argument. This means the total wait time for the action to occur
     * is: loops X ms. Sensible values might be 10 loops for 500 ms, or
     * 5 seconds.
     *
     * @param loops Number of times to call block(long)
     * @param ms Number of milliseconds to pass to block(long
     * @return the response
     * @throws InterruptedException if the thread was interrupted
     * @throws ode.LockTimeout if block(long) does not return
     * a non-null value after loops calls.
     */
    public Response loop(int loops, long ms) throws InterruptedException,
        ode.LockTimeout {

        int count = 0;
        boolean found = false;
        while (count < loops) {
            count++;
            found = block(ms);
            if (found) {
                break;
            }
        }

        if (found) {
            return getResponse();
        } else {
            double waited = (ms/1000.0) * loops;
            throw new ode.LockTimeout(null, null,
                String.format("Command unfinished after %s seconds",
                    waited), 10000, (int) waited);
        }
    }

    /**
     * Blocks for the given number of milliseconds unless
     * {@link #finished(Response, Status, Current)} has been called in which case
     * it returns immediately with true. If false is returned, then the timeout
     * was reached.
     *
     * @param ms Milliseconds which this method should block for.
     * @return if the the thread finished before the timeout was reached
     * @throws InterruptedException if the thread was interrupted
     */
    public boolean block(long ms) throws InterruptedException {
        return latch.await(ms, TimeUnit.MILLISECONDS);
    }

    //
    // Remote invocations
    //

    /**
     * Calls {@link HandlePrx#getResponse} in order to check for a non-null
     * value. If so, {@link Handle#getStatus} is also called, and the two
     * non-null values are passed to
     * {@link #finished(Response, Status, Current)}. This should typically
     * not be used. Instead, favor the use of block and loop.
     *
     */
    public void poll() {
        Response rsp = handle.getResponse();
        if (rsp != null) {
            Status s = handle.getStatus();
            finished(rsp, s, null); // Only time that current should be null.
        }
    }

    /**
     * Called periodically by the server to signal that processing is
     * moving forward. Default implementation does nothing.
     */
    public void step(int complete, int total, Current __current) {
        // no-op
    }

    /**
     * Called when the command has completed.
     */
    public final void finished(Response rsp, Status status, Current __current) {
        state.set(new State(rsp, status));
        latch.countDown();
        onFinished(rsp, status, __current);
    }

    /**
     * Method intended to be overridden by subclasses. Default logic does
     * nothing.
     * @param rsp the response
     * @param status the status
     * @param __current regarding the current method invocation
     */
    public void onFinished(Response rsp, Status status, Current __current) {
        // no-op
    }

    /**
     * First removes self from the adapter so as to no longer receive
     * notifications, and the calls close on the remote handle if requested.
     * @param closeHandle if the handle should be closed
     */
    public void close(boolean closeHandle) {
        try {
            adapter.remove(id); // OK ADAPTER USAGE
            if (closeHandle) {
                handle.close();
            }
        } catch (Ice.ObjectAdapterDeactivatedException oade) {
            // Already shutdown.
        }
    }
}