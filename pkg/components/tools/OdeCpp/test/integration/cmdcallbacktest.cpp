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

#include <ode/fixture.h>
#include <ode/callbacks.h>
#include <ode/all.h>
#include <string>
#include <map>
#include <IceUtil/RecMutex.h>
#include <IceUtil/Config.h>
#include <Ice/Handle.h>
#include <ode/util/concurrency.h>

using namespace std;
using namespace ode;
using namespace ode::api;
using namespace ode::cmd;
using namespace ode::callbacks;
using namespace ode::model;
using namespace ode::rtypes;
using namespace ode::sys;

class TestCB;

typedef CallbackWrapper<TestCB> TestCBPtr;

class TestCB: virtual public CmdCallbackI {
private:
    // Preventing copy-construction and assigning by value.
    TestCB& operator=(const TestCB& rv);
    TestCB(TestCB&);
public:
    int steps;
    int finished;

    TestCB(const ode::client_ptr client, const HandlePrx& handle) :
        CmdCallbackI(client, handle), steps(0), finished(0) {
        }

    ~TestCB(){}

    // Expose protected event member.
    ode::util::concurrency::Event&
    getEvent() {
      return event;
    }

    virtual void step(int /*complete*/, int /*total*/, const Ice::Current&) {
        IceUtil::RecMutex::Lock lock(mutex);
        steps++;
    }

    virtual void onFinished(const ResponsePtr&,
            const StatusPtr&, const Ice::Current&) {
        IceUtil::RecMutex::Lock lock(mutex);
        finished++;
        event.set();
    }

    void assertSteps() {
        IceUtil::RecMutex::Lock lock(mutex);

        // Not guranteed to get called for all steps, as the callback can
        // get added on the server after the operation has already started
        // if there is network latency
        ASSERT_GE(steps, 1);
    }

    void assertFinished(bool testSteps = true) {
        try {
            IceUtil::RecMutex::Lock lock(mutex);
            if (0 == finished) {
                // If things work as expected, then
                // the callback is added to the server
                // in time for onFinished to be called.
                // If that has failed, then we try again
                // since there seems to be some race
                // condition in C++.
                poll();
            }
            ASSERT_GT(finished, 0);
            ASSERT_FALSE(isCancelled());
            ASSERT_FALSE(isFailure());
            ResponsePtr rsp = getResponse();
            if (!rsp) {
                FAIL() << "null response";
            }
            ERRPtr err = ERRPtr::dynamicCast(rsp);
            if (err) {
                ostringstream ss;
                ode::cmd::StringMap::iterator it;
                for (it=err->parameters.begin(); it != err->parameters.end(); it++ ) {
                    ss << (*it).first << " => " << (*it).second << endl;
                }
                FAIL()
                << "ERR!"
                << "cat:" << err->category << "\n"
                << "name:" << err->name << "\n"
                << "params:" << ss.str() << "\n";
            }

            if (testSteps) {
                assertSteps();
            }
        } catch (const ode::ValidationException& ve) {
            FAIL() << "validation exception:" << ve.message;
        }
    }

    void assertCancelled() {
        try {
            IceUtil::RecMutex::Lock lock(mutex);
            if (0 == finished) {
                // If things work as expected, then
                // the callback is added to the server
                // in time for onFinished to be called.
                // If that has failed, then we try again
                // since there seems to be some race
                // condition in C++.
                poll();
            }
            ASSERT_GT(finished, 0);
            ASSERT_TRUE(isCancelled());
        } catch (const ode::ValidationException& ve) {
            FAIL() << "validation exception:" << ve.message;
        }
    }
};

class CBFixture : virtual public Fixture {
public:

    TestCBPtr run(const RequestPtr& req, int addCbDelay = 0) {
        ExperimenterGroupPtr group = newGroup("rwr---");
        ExperimenterPtr user = newUser(group);
        login(user->getOdeName()->getValue(), user->getOdeName()->getValue());
        HandlePrx handle = client->getSession()->submit(req);
        TestCBPtr rv = new TestCB(client, handle);

        if (addCbDelay > 0) {
            ode::util::concurrency::Event event;
            event.wait(IceUtil::Time::milliSeconds(addCbDelay));
        }

        return rv;
    }

    // Timing
    // =========================================================================

    TestCBPtr timing(int millis, int steps, int addCbDelay = 0) {
        ode::cmd::TimingPtr t = new Timing();
        t->millisPerStep = millis;
        t->steps = steps;
        return run(t, addCbDelay);
    }

    // DoAll
    // =========================================================================

    TestCBPtr doAllOfNothing() {
        return run(new ode::cmd::DoAll());
    }

    TestCBPtr doAllTiming(int count) {
        ode::cmd::RequestList timings;
        for (int i = 0; i < count; i++) {
            ode::cmd::TimingPtr t = new ode::cmd::Timing();
            t->steps = 3;
            t->millisPerStep = 50;
            timings.push_back(t);
        }
        ode::cmd::DoAllPtr all = new ode::cmd::DoAll();
        all->requests = timings;
        return run(all);
    }

};

TEST(CmdCallbackTest, testTimingFinishesOnLatch) {
    CBFixture f;
    TestCBPtr cb = f.timing(25, 4 * 10); // Runs 1 second
    cb->getEvent().wait(IceUtil::Time::milliSeconds(1500));
    cb->assertFinished();
}

TEST(CmdCallbackTest, testTimingFinishesOnBlock) {
    CBFixture f;
    TestCBPtr cb = f.timing(25, 4 * 10); // Runs 1 second
    cb->block(1500);
    cb->assertFinished();
}

TEST(CmdCallbackTest, testTimingFinishesOnLoop) {
    CBFixture f;
    TestCBPtr cb = f.timing(25, 4 * 10); // Runs 1 second
    cb->loop(3, 500);
    cb->assertFinished();
}

TEST(CmdCallbackTest, testDoNothingFinishesOnLatch) {
    CBFixture f;
    TestCBPtr cb = f.doAllOfNothing();
    cb->getEvent().wait(IceUtil::Time::milliSeconds(5000));
    cb->assertCancelled();
}

TEST(CmdCallbackTest, testDoNothingFinishesOnLoop) {
    CBFixture f;
    TestCBPtr cb = f.doAllOfNothing();
    cb->loop(5, 1000);
    cb->assertCancelled();
}

TEST(CmdCallbackTest, testDoAllTimingFinishesOnLoop) {
    try {
        CBFixture f;
        TestCBPtr cb = f.doAllTiming(5);
        cb->loop(5, 1000);
        cb->assertFinished();
        // For some reason the number of steps is varying between 10 and 15
    } catch (const Ice::ConnectionLostException& cle) {
        FAIL() << "connection lost: " << cle << endl;
    }
}

TEST(CmdCallbackTest, testAddAfterFinish) {
    CBFixture f;
    TestCBPtr cb = f.timing(25, 4 * 10, 1200); // Runs 1 second
    cb->getEvent().wait(IceUtil::Time::milliSeconds(1500));
    cb->assertFinished(false);
}
