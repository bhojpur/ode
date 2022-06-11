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

#ifndef ODE_CMD_API_ICE
#define ODE_CMD_API_ICE

#include <ode/RTypes.ice>
#include <ode/ServerErrors.ice>
#include <Glacier2/Session.ice>
#include <Ice/BuiltinSequences.ice>
#include <Ice/Identity.ice>

module ode {

    /**
     * Simplified API that is intended for passing
     **/
    module cmd {

        dictionary<string, string> StringMap;

        sequence<StringMap> StringMapList;

        enum State {
            ALL, ACTIVE, INACTIVE, SUCCESS, FAILURE, CANCELLED
        };

        ["java:type:java.util.ArrayList<ode.cmd.State>:java.util.List<ode.cmd.State>"]
        sequence<State> StateList;

        interface Handle; /* Forward */

        class Status {
            Handle* source;
            string category;
            string name;
            StateList flags;
            StringMap parameters;

            /** the latest step to be commenced, from 0 to steps-1 */
            int currentStep;
            /** the total number of steps */
            int steps;
            long startTime;
            Ice::LongSeq stepStartTimes;
            Ice::LongSeq stepStopTimes;
            long stopTime;

        };

        ["java:type:java.util.ArrayList<ode.cmd.Status>:java.util.List<ode.cmd.Status>"]
        sequence<Status> StatusList;

        class Request {
        };

        ["java:type:java.util.ArrayList<ode.cmd.Request>:java.util.List<ode.cmd.Request>"]
        sequence<Request> RequestList;

        class Response {
        };

        class OK extends Response {

        };

        class ERR extends Response {
            string category;
            string name;
            StringMap parameters;
        };

        class Unknown extends ERR {

        };

        ["java:type:java.util.ArrayList<ode.cmd.Response>:java.util.List<ode.cmd.Response>"]
        sequence<Response> ResponseList;

        interface CmdCallback {

            /**
             * Notifies clients that the given number of steps
             * from the total is complete. This method will not
             * necessarily be called for every step.
             */
             void step(int complete, int total);

            /**
             * Called when the command has completed in any fashion
             * including cancellation. The {@link Status#flags} list will
             * contain information about whether or not the process
             * was cancelled.
             */
             void finished(Response rsp, Status s);

        };

        ["ami"] interface Handle {

            /**
             * Add a callback for notifications.
             **/
            void addCallback(CmdCallback* cb);

            /**
             * Remove callback for notifications.
             **/
            void removeCallback(CmdCallback* cb);

            /**
             * Returns the request object that was used to
             * initialize this handle. Never null.
             **/
            Request getRequest();

            /**
             * Returns a response if this handle has finished
             * execution, otherwise returns null.
             **/
            Response getResponse();

            /**
             * Returns a status object for the current execution.
             *
             * This will likely be the same object that would be
             * returned as a component of the {@link Response} value.
             *
             * Never null.
             **/
            Status getStatus();

            /**
             * Attempts to cancel execution of this {@link Request}. Returns
             * true if cancellation was successful. Returns false if not,
             * in which case likely this request will run to completion.
             **/
            bool cancel() throws ode::LockTimeout;

            /**
             * Closes this handle. If the request is running, then a
             * cancellation will be attempted first. All uses of a handle
             * should be surrounded by a try/finally close block.
             **/
            void close();
        };

        ["java:type:java.util.ArrayList<ode.cmd.HandlePrx>:java.util.List<ode.cmd.HandlePrx>"]
        sequence<Handle*> HandleList;

        /**
         * Starting point for all command-based Bhojpur ODE interaction.
         **/
        interface Session extends Glacier2::Session {
            ["amd", "ami"] Handle* submit(Request req);
        };

    };
};

#endif