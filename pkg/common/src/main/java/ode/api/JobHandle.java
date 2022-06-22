package ode.api;

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

import java.sql.Timestamp;

import ode.annotations.NotNull;
import ode.conditions.ApiUsageException;
import ode.model.jobs.Job;
import ode.model.jobs.JobStatus;

/**
 * Allows submission of asynchronous jobs.
 * <p>
 * NOTE: The calling order for the service is as follows:
 * <ol>
 * <li>submit({@link Job}) <em>or</em> attach(long)</li>
 * <li>any of the other methods</li>
 * <li>close()</li>
 * </ol>
 * </p>
 * Calling <code>close()</code> does not cancel or otherwise change the Job
 * state. See {@link #cancelJob()}.
 */
public interface JobHandle extends StatefulServiceInterface {

    public final static String SUBMITTED = "Submitted";
    public final static String RESUBMITTED = "Resubmitted";
    public final static String QUEUED = "Queued";
    public final static String REQUEUED = "Requeued";
    public final static String RUNNING = "Running";
    public final static String ERROR = "Error";
    public final static String WAITING = "Waiting";
    public final static String FINISHED = "Finished";
    public final static String CANCELLED = "Cancelled";

    /**
     * Submits a {@link Job} and returns its database id. The only fields
     * directly on status which are editable are <em>message</em>,
     * <em>scheduledFor</em> and <em>status</em>. The latter two must be
     * sensible.
     * 
     * @param job
     *            Not null
     * @return id
     */
    long submit(@NotNull
    Job job);

    /**
     * @return the current {@link JobStatus} for the {@link Job id}
     * @throws ApiUsageException
     *             if the {@link Job id} does not exist.
     */
    JobStatus attach(long jobId) throws ApiUsageException;

    /**
     * @return the current {@link Job}
     */
    Job getJob();

    /**
     * @return the current {@link JobStatus}. Will never return null.
     */
    JobStatus jobStatus();

    /**
     * @return null if the {@link Job} is not finished, otherwise the
     *         {@link Timestamp} for when it completed.
     */
    Timestamp jobFinished();

    /**
     * @return current message for job. May be set during processing.
     */
    String jobMessage();

    /**
     * Returns true if the {@link Job} is running, i.e. has an attached
     * {@link Process}.
     */
    boolean jobRunning();

    /**
     * Returns true if the {@link Job} has thrown an error.
     */
    boolean jobError();

    /**
     * Marks a job for cancellation. Not every processor will check for the
     * cancelled flag for a running job, but no non-running job will start if it
     * has been cancelled.
     */
    void cancelJob();

    /**
     * Updates the {@link JobStatus} for the current job. The previous status
     * is returned as a string. If the status is {@link #CANCELLED}, this
     * method is equivalent to {@link #cancelJob()}.
     */
    String setStatus(String status);

    /**
     * Like {@link #setStatus(String)} but also sets the message.
     */
    String setStatusAndMessage(@NotNull String status, String message);

    /**
     * Sets the job's message string, and returns the previous value.

     * @param message
     * @return the previous message value
     */
    String setMessage(String message);

}