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

#ifndef ODE_CMD_BASIC_ICE
#define ODE_CMD_BASIC_ICE

#include <ode/cmd/API.ice>

module ode {

    module cmd {

        class DoAll extends Request {

            RequestList requests;

            /**
             * List of call context objects which should get applied to each Request.
             * The list need only be as large as necessary to apply to a given request.
             * Null and empty {@code StringMap} instances will be ignored.
             **/
            StringMapList contexts;
        };

        class DoAllRsp extends OK {
            ResponseList responses;
            StatusList status;
        };

        class ListRequests extends Request {

        };

        class ListRequestsRsp extends OK {
            RequestList list;
        };

        class PopStatus extends Request {
            int limit;
            StateList include;
            StateList exclude;
        };

        class PopStatusRsp extends OK {
            StatusList list;
        };

        class FindHandles extends Request {
            int limit;
            StateList include;
            StateList exclude;
        };

        class FindHandlesRsp extends OK {
            HandleList handles;
        };

        /**
         * Diagnostic command which can be used to see the overhead
         * of callbacks. The number of steps and the simulated workload
         * can be specified.
         */
        class Timing extends Request {

            /**
             * Number of steps that will be run by this command. Value is
             * limited by the overall invocation time (5 minutes) as well as
             * total number of calls (e.g. 100000)
             */
            int steps;

            /**
             * Number of millis to wait. This value simulates activity on the server.
             * Value is limited by the overall invocation time (5 minutes).
             */
            int millisPerStep;
        };
    };
};

#endif