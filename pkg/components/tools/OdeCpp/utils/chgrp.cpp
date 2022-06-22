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

#include <ode/all.h>
#include <ode/callbacks.h>
#include <ode/client.h>
#include <ode/cmd/Graphs.h>
#include <ode/model/ImageI.h>
#include <IceUtil/CtrlCHandler.h>
#include <IceUtil/Config.h>
#include <IceUtil/Handle.h>
#include <functional>
#include <iostream>
#include <map>
#include <sstream>
#include <string>
#include <stdexcept>

using namespace std;
using namespace ode;
using namespace ode::api;
using namespace ode::cmd;
using namespace ode::cmd::graphs;
using namespace ode::callbacks;
using namespace ode::model;
using namespace ode::rtypes;
using namespace ode::sys;


template <typename K, typename V>
V GetWithDef(const std::map <K,V> & m, const K & key, const V & defval, const Ice::LoggerPtr&
#ifdef DEBUG
        logger = Ice::LoggerPtr()
#endif
) {
    typename std::map<K,V>::const_iterator it = m.find( key );
    if ( it == m.end() ) {
#ifdef DEBUG
        if (logger) {
            stringstream ss;
            ss << "Lookup key: ";
            ss << key;
            ss << " Return default: ";
            ss << defval;
            logger->print(ss.str());
        }
#endif
        return defval;
    }
    else {
#ifdef DEBUG
        if (logger) {
            stringstream ss;
            ss << "Lookup key: ";
            ss << key;
            ss << " Found: ";
            ss << it->second;
            logger->print(ss.str());
        }
#endif
        return it->second;
    }
}

// http://www.parashift.com/c++-faq-lite/misc-technical-issues.html#faq-39.2
class BadConversion : public std::runtime_error {
public:
BadConversion(std::string const& s)
    : std::runtime_error(s)
    { }
};

template<typename T>
inline void convert(std::string const& s, T& x,
                    bool failIfLeftoverChars = true)
{
    std::istringstream i(s);
    char c;
    if (!(i >> x) || (failIfLeftoverChars && i.get(c)))
        throw BadConversion(s);
}


template<typename T>
inline T convertTo(std::string const& s,
                bool failIfLeftoverChars = true)
{
    T x;
    convert(s, x, failIfLeftoverChars);
    return x;
}

// http://www.codeguru.com/forum/showthread.php?t=231054
template <class T>
void from_string(T& t,
                 const std::string& s,
                 std::ios_base& (*f)(std::ios_base&))
{
    std::istringstream iss(s);
    if (!(iss >> f >> t).fail()) {
        throw BadConversion(s);
    }
    char c;
    if (iss.get(c)) {
        throw BadConversion(s);
    }
}

// http://stackoverflow.com/questions/1484140/how-do-you-get-an-unsigned-long-out-of-a-string
template <class T>
T strToNum(const std::string &inputString,
           std::ios_base &(*f)(std::ios_base&) = std::dec)
{
    T t;
    std::istringstream stringStream(inputString);

    // was: if ((stringStream >> f >> t).fail())
    if ((stringStream >> f >> t).fail())
    {
        throw BadConversion("(Fail) " + inputString);
    }
    else if (!(stringStream >> std::ws).eof())
    {
        throw BadConversion("(Incomplete) " + inputString);
    }
    return t;
}

class ChgrpCall;

typedef IceUtil::Handle<ChgrpCall> ChgrpCallPtr;

class ChgrpCall : public IceUtil::Shared {
private:
    // Preventing copy-construction and assigning by value.
    ChgrpCall& operator=(const ChgrpCall& rv);
    ChgrpCall(ChgrpCall&);
public:
    client_ptr c;
    int status;
    long id;
    long grp;
    long wait;
    Chgrp2Ptr req;
    ResponsePtr rsp;
    HandlePrx handle;
    CmdCallbackIPtr cb;

    ChgrpCall(const client_ptr& c) : c(c), status(0), id(-1), grp(-1), wait(-1) {

        map<string, string> m = c->getPropertyMap();
        Ice::LoggerPtr log = c->getCommunicator()->getLogger();
        string typestr = GetWithDef(m, string("ode.in.type"), string(""), log);
        string idstr = GetWithDef(m, string("ode.in.id"), string(""), log);
        string grpstr = GetWithDef(m, string("ode.in.grp"), string(""), log);
        string waitstr = GetWithDef(m, string("ode.in.wait"), string("-1"), log);

        id = strToNum<long>(idstr);
        grp = strToNum<long>(grpstr);
        wait = strToNum<long>(waitstr);

        ode::sys::LongList objectIds;
        StringLongListMap objects;
        ChildOptions options;
        req = new Chgrp2();
        objectIds.push_back(id);
        objects[typestr] = objectIds;
        req->targetObjects = objects;
        req->groupId = grp;
        req->childOptions = options;
    }

    void ctrlc(int /*sig*/) {
        cout << "Attempting cancel..." << endl;
        wait = 0;
        if (handle->cancel()) {
            cout << "Cancelled..." << endl;
        } else {
            cout << "Failed to cancel" << endl;
            status = 1;
        }
    }

    void run() {
        ServiceFactoryPrx sf = c->createSession();
        handle = sf->submit( req );
        cb = new CmdCallbackI(c, handle);

        if (wait == 0) {
            cout << "Exiting immediately..." << endl;
        } else if (wait < 0) {
            bool blocked = true;
            while (blocked) {
                blocked = cb->block(500);
            }
        } else {
            int loops = wait * 2;
            cout << "Waiting " << loops << " loops of 500 ms" << endl;
            rsp = cb->loop(loops, 500);
        }

        OKPtr ok = OKPtr::dynamicCast(rsp);
        ERRPtr err = ERRPtr::dynamicCast(rsp);
        if (err) {
            cout << "ERR returned" << endl;
            cout << err->category << endl;
            cout << err->name << endl;
            status = 3;
        } else if (ok) {
            cout << "OK returned" << endl;
        }
    }
};

static ChgrpCallPtr call;

static void
onCtrlC(int sig)
{
    if (call) {
        call->ctrlc(sig);
    }
}

int main(int argc, char** argv) {
    //
    // From Ice/Application.cpp:
    //   The ctrlCHandler must be created before starting any thread, in particular
    //   before initializing the communicator.
    //
    IceUtil::CtrlCHandler ctrlCHandler(onCtrlC);
    client_ptr c = new client(argc, argv); // Uses --Ice.Config=
    try {
        call = new ChgrpCall(c);
        call->run();
        return call->status;
    } catch (const BadConversion& bc) {
        cerr << "BadConversion: " << bc.what() << endl;
        return -1;
    }
}
