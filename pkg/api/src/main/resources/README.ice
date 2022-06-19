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

#ifndef  README_ICE
#define  README_ICE

// The *.ice files that are included here are ZeroC slice definitions
// which are used to generate much of the source code (headers, C/C++,
// and Java classes) which compose Bhojpur ODE system.
//
// The documentation which follows is used by the release-slice2html
// target to generate HTML under dist/docs/api/slice2html. The latest
// copy of which is available here:
//
// http://downloads.bhojpur.net/latest/ode/api/slice2html/

/**

Main module.

<h1>Bhojpur ODE - Optical Data Engine</h1>

<p>
In the [pkg/api/src/main/slice] folder, the ode/ ice files are the central
definitions of the Bhojpur ODE implementation. They provide an Ice facade
over the existing Bhojpur ODE server architecture. Because of the increased
overhead of creating implementations in multiple languages, the mappings are
simplified and work mostly as simple data transfer objects. For more on the
individual classes, see the slice definition files.
</p>

<h2>ode/model *.ice</h2>

<p>
The slice definitions under ode/model were themselves code-generated and are
largely undocumented. Instead, this README helps one to understand just what
those files mean.
</p>

<p>
Before each class definition, an Ice sequence is created of the form
"FieldNameSeq". The annotation which precedes the sequence definition
["java:type:java.util.ArrayList"] changes the default mapping from a
native Java array, to a List with generics. In C/C++, the sequence will
become a typedef for vector<type>.
</p>

<p>
The class itself will either subclass ode::model::IObject or another subclass
of ode::model::IObject (defined in ODE/IObject.ice). Otherwise, only fields
are defined which will be made into public fields in all the language bindings.
Note the "sequenceNameLoaded" fields which are used strictly to allow for
marking the collection fields as "null" since Ice automatically converts nulls
to empty collections.
</p>

<p>
It should be noted that the classes generated from this definition will be
abstract because of the "unload()" method in the IObject superclass. When a
method is defined on a class in slice, the resulting object is abstract, and a
concrete implementation must be provided. This is done via the types ending in
"I" (for implementation) in the ode::model package. An ObjectFactory is required
to tell an Ice communicator how to map type names to concrete implementations.
</p>

<h2>RTypes</h2>

<p>
RType-sub["protected"] classes permit both the passing of null values to the
Bhojpur ODE server, since the Ice protocol maps null values to default (the empty
string, 0.0, etc.), and a simple implementation of an "Any" value.
</p>

<p>
Usage (C/C++):
</p>

<pre>

    ode::RBoolPtr b1 = new ode::RBool(true);
    ode::RBoolPtr b2 = someObjPtr->getBool();
    if (b2 && b2.val) { ... };
    // the first test, checks if the pointer is null
</pre>

<p>
 Usage (Java):
</p>

<pre>
    ode.RBool b1 = new ode.RBool(true);
    ode.RBool b2 = someObj.getBool();
    if (b2!=null && b2.val) { ... };
    // no operator overloading; check for null directly.
</pre>

**/

module ode {

/**

Interfaces and types running the backend facilities.

**/

module grid {

}; /* End grid */

/**
Code-generated types based on the <a href="http://ode.bhojpur.net">Bhojpur ODE specification</a>.
**/

module model {};

/**

Various core types.

**/

module sys {};

}; /* End Bhojpur ODE */

#endif