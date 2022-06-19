package ode.services.scheduler;

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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

/**
 * An internal implementation of {@link ThreadPoolExecutor} that additionally
 * handles the submission of {@link #background(Callable)} tasks at a lower
 * priority and with more limited slots.
 */
public class ThreadPool extends ThreadPoolExecutor {

    private final static Logger log = LoggerFactory.getLogger(ThreadPool.class);

    /**
     * {@link Semaphore} protecting the <i>submission</i> of background tasks.
     * The slot will not be made available though until <i>completion</i>, i.e.
     * during {@link #afterExecute(Runnable, Throwable)}.
     */
    private final Semaphore maxBackground;

    /**
     * Milliseconds to wait until calls to {@link #background(Callable)} will
     * timeout.
     */
    private final long backgroundTimeout;

    public ThreadPool() {
        // Values from Executors.newCachedThreadPool
        super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());
        this.maxBackground = new Semaphore(10);
        this.backgroundTimeout = 3600*1000;

    }

    public ThreadPool(int minThreads, int maxThreads, long msTimeout,
            int backgroundThreads, long backgroundTimeout) {
        super(minThreads, maxThreads, msTimeout, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        this.maxBackground = new Semaphore(backgroundThreads);
        this.backgroundTimeout = backgroundTimeout;
        log.info("ThreadPool: normal=(#{}, {}ms), background=(#{}, {}ms)",
                maxThreads, msTimeout, backgroundThreads, backgroundTimeout);
    }

    /**
     *  Returns this. Previously {@link ThreadPool} was not itself an
     *  {@link ExecutorService} and returned a delegate instead, most typically
     *  in a Spring configuration.
     */
    @Deprecated
    public ExecutorService getExecutor() {
        return this;
    }

    /**
     * Schedule a task in one of the limited background slots. If <i>scheduling</i>
     * takes more than hour then the submission will be rejected. Otherwise,
     * the task will run in the same thread pool as both USER and BACKGROUND
     * tasks.
     * @param callable
     * @return a future for this task
     */
    public <T> Future<T> background(Callable<T> callable){
        StopWatch sw = new Slf4JStopWatch();
        try {
            if (!maxBackground.tryAcquire(
                    1, backgroundTimeout, TimeUnit.MILLISECONDS)) {
                String msg = String.format(
                    "Failed to execute %s after %sms", callable, backgroundTimeout);
                log.warn(msg);
                throw new RejectedExecutionException(msg);
            }
            return this.submit((Callable<T>) new BackgroundCallable(callable));
        } catch (InterruptedException e) {
            String msg = String.format(
                "Interrupted while waiting to execute %s", callable);
            log.warn(msg);
            throw new RejectedExecutionException(msg);
        } finally {
            sw.stop("ode.background.submit");
        }
    }

    /**
     * Overrides {@link ThreadPoolExecutor} to return our own instance of
     * {@link FutureTask} so that we have a marker for when the background
     * activity is completed in {@link #afterExecute(Runnable, Throwable)}.
     *
     * Overriding this method is somewhat non-standard.
     */
    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof BackgroundCallable) {
            BackgroundCallable<?> background = (BackgroundCallable<?>) callable;
            return new BackgroundFutureTask(background.delegate);
        }
        return super.newTaskFor(callable);
    }

    /**
     * Standard {@link ThreadPoolExecutor} extension point which checks for the
     * BackgroundFutureTask marker and releases a slot in the {@link #maxBackground}
     * {@link Semaphore}.
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            if (r instanceof BackgroundFutureTask) {
                maxBackground.release();
                ((BackgroundFutureTask) r).sw.stop("ode.background.task");
            }
        } finally {
            super.afterExecute(r, t);
        }
    }

    /**
     * Marker class which will be detected in {@link ThreadPool#newTaskFor(Callable)}
     * so that the {@link Callable} can be unwrapped and packed in a
     * {@link BackgroundFutureTask}.
     */
    private static class BackgroundCallable<T extends Callable<T>>
        implements Callable<T> {

        Callable<T> delegate;

        BackgroundCallable(Callable<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public T call() throws Exception {
            return this.delegate.call();
        }

    }

    /**
     * Another marker class which is needed to wrap the Callable since
     * {@link ThreadPoolExecutor} <i>always</i> calls
     * {@link ThreadPool#newTaskFor(Callable)} before execution.
     */
    private static class BackgroundFutureTask<T extends FutureTask<T>>
        extends FutureTask<T> {

        StopWatch sw = new Slf4JStopWatch();

        public BackgroundFutureTask(Callable<T> callable) {
            super(callable);
        }

    }
}
