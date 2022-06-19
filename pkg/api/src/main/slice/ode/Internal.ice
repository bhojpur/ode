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

#ifndef ODE_INTERNAL_ICE
#define ODE_INTERNAL_ICE

/*
 * These interfaces and classes are used internal to the Bhojpur ODE
 * server and are not needed by client developers.
 */
module ode {

    module grid {

        /**
         * Interface implemented by each server instance. Instances lookup one
         * another in the IceGrid registry.
         **/
        interface ClusterNode {

            /**
             * Each node acquires the uuids of all other active nodes on start
             * up. The uuid is an internal value and does not
             * correspond to a session.
             **/
             idempotent string getNodeUuid();

            /**
             * Let all cluster nodes know that the instance with this
             * uuid is going down.
             **/
            void down(string uuid);

        };

    };

};
#endif