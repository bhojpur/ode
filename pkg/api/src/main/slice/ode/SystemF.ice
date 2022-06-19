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

#include <ode/RTypes.ice>
#include <Ice/BuiltinSequences.ice>

#ifndef ODE_SYSTEMF_ICE
#define ODE_SYSTEMF_ICE

/*
 * Defines various classes for more simplified imports.
 */
module ode {

    module sys {

        // START: TRANSFERRED FROM COLLECTIONS
        // Some collections were initially defined under ode::sys

        ["java:type:java.util.ArrayList<Long>:java.util.List<Long>"]
            sequence<long> LongList;

        ["java:type:java.util.ArrayList<Integer>:java.util.List<Integer>"]
            sequence<int> IntList;

        ["java:type:java.util.HashMap<Long,Long>:java.util.Map<Long,Long>"]
            dictionary<long, long> CountMap;

        /**
         * ParamMap replaces the ode.parameters.QueryParam
         * type, since the use of varargs is not possible.
         **/
        ["java:type:java.util.HashMap"]
            dictionary<string,ode::RType> ParamMap;

        /**
         * IdByteMap is used by the ThumbnailService for the multiple thumbnail
         * retrieval methods.
         **/
        ["java:type:java.util.HashMap"]
            dictionary<long,Ice::ByteSeq> IdByteMap;

        // END: TRANSFERRED FROM COLLECTIONS

        class EventContext;

    };

};

#endif