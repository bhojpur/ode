/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_API_JOBHANDLE_ICE
#define ODE_API_JOBHANDLE_ICE

#include <ode/ModelF.ice>
#include <ode/ServicesF.ice>
#include <ode/Collections.ice>

module ode {

    module api {

        /**
         * Allows submission of asynchronous jobs.
         * <p>
         * NOTE: The calling order for the service is as follows:
         * <ol>
         * <li>{@code submit} <em>or</em> {@code attach}</li>
         * <li>any of the other methods</li>
         * <li>{@code close}</li>
         * </ol>
         * </p>
         * Calling {@code close} does not cancel or otherwise change
         * the Job state. See {@code cancelJob}.
         */
        ["ami", "amd"] interface JobHandle extends StatefulServiceInterface
            {
                /**
                 * Submits a {@link ode.model.Job} and returns its database
                 * id. The only fields directly on status which are editable
                 * are <em>message</em>, <em>scheduledFor</em> and
                 * <em>status</em>. The latter two must be sensible.
                 *
                 * @param job Not null
                 */
                long submit(ode::model::Job job) throws ServerError;

                /**
                 * Returns the current {@link ode.model.JobStatus} for the
                 * Job id.
                 *
                 * @throws ApiUsageException
                 *             if the {@link ode.model.Job} does not exist.
                 */
                ode::model::JobStatus attach(long jobId) throws ServerError;

                /**
                 * Returns the current {@link ode.model.Job}
                 */
                idempotent ode::model::Job getJob()  throws ServerError;

                /**
                 * Returns the current {@link ode.model.JobStatus}. Will
                 * never return null.
                 */
                idempotent ode::model::JobStatus jobStatus()  throws ServerError;

                /**
                 * Returns <code>null</code> if the {@link ode.model.Job} is
                 * not finished, otherwise the {@link ode.RTime} for when it
                 * completed.
                 */
                idempotent ode::RTime jobFinished()  throws ServerError;

                /**
                 * Returns the current message for job. May be set during
                 * processing.
                 */
                idempotent string jobMessage()  throws ServerError;

                /**
                 * Returns <code>true</code> if the {@link ode.model.Job} is
                 * running, i.e. has an attached process.
                 */
                idempotent bool jobRunning()  throws ServerError;

                /**
                 * Returns <code>true</code> if the {@link ode.model.Job}
                 * has thrown an error.
                 */
                idempotent bool jobError()  throws ServerError;

                /**
                 * Marks a job for cancellation. Not every processor will
                 * check for the cancelled flag for a running job, but no
                 * non-running job will start if it has been cancelled.
                 */
                void cancelJob()  throws ServerError;

                /**
                 * Updates the {@link ode.model.JobStatus} for the current
                 * job. The previous status is returned as a string. If the
                 * status is {@code CANCELLED}, this method is equivalent to
                 * {@code cancelJob}.
                 */
                idempotent string setStatus(string status) throws ServerError;

                /**
                 * Sets the job's message string, and returns the previous
                 * value.
                 *
                 * @return the previous message value
                 */
                idempotent string setMessage(string message) throws ServerError;

                /**
                 * Like {@code setStatus} but also sets the message.
                 */
                idempotent string setStatusAndMessage(string status, ode::RString message) throws ServerError;
            };
    };
};

#endif