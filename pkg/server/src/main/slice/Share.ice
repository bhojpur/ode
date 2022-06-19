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

#ifndef __ODE_SERVICES_SHARING_SHARE_ICE
#define __ODE_SERVICES_SHARING_SHARE_ICE

module  ode { module services { module sharing { module data {

    class Obj
    {
        string type;
        long id;
    };

    ["java:type:java.util.ArrayList<ode.services.sharing.data.Obj>:java.util.List<ode.services.sharing.data.Obj>"]
    sequence<Obj> ObjSeq;

    ["java:type:java.util.ArrayList<Long>:java.util.List<Long>"]
    sequence<long> LongSeq;

    ["java:type:java.util.ArrayList<String>:java.util.List<String>"]
    sequence<string> StringSeq;

    dictionary<string, LongSeq> IdMap;

    /*
     * Full definition of a "share".
     */
    class ShareData
    {
        long id;
        long owner;
        LongSeq members;
        StringSeq guests;
        IdMap objectMap;
        ObjSeq objectList;

        bool enabled;
        long optlock;
    };

    /*
     * View on the ShareData, per object. Essentially an ACL list.
     */
    class ShareItem
    {
        long share;
        string type;
        long id;
        LongSeq members;
        StringSeq guests;
    };

}; }; }; };

#endif