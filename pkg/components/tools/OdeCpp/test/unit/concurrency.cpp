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

#include <list>

#include <ode/IceNoWarnPush.h>
#include <IceUtil/Thread.h>
#include <IceUtil/Config.h>
#include <IceUtil/Handle.h>
#include <ode/IceNoWarnPop.h>

#include <ode/util/concurrency.h>
#include <ode/fixture.h>

using namespace ode::util::concurrency;
using namespace ode;
using namespace std;

class BaseThread;

typedef IceUtil::Handle<BaseThread> BaseThreadPtr;

class BaseThread : public IceUtil::Thread {
public:
    bool passed;
    IceUtil::Time ms(long ms) {
        return IceUtil::Time::milliSeconds(ms);
    };
};

Event event;

class ReaderThread : public BaseThread {
    virtual void run() {
        if (event.wait(ms(1000))) {
            passed = true;
        }
    }
};

class WriterThread : public BaseThread {
    virtual void run() {
        event.set();
        passed = true;
    }
};

TEST(ConcurrencyTest, testEvent )
{
        list<BaseThreadPtr> rs;
        list<IceUtil::ThreadControl> rcs;
        for (int i = 0; i < 10; i++) {
            BaseThreadPtr r = new ReaderThread();
            rs.push_back(r);
            rcs.push_back(r->start());
        }

        BaseThreadPtr w = new WriterThread();
        IceUtil::ThreadControl wc = w->start();
        wc.join();
        ASSERT_TRUE( (*w).passed );

        for (int i = 0; i < 10; i++) {
            BaseThreadPtr r = rs.front();
            IceUtil::ThreadControl tc = rcs.front();
            rs.pop_front();
            rcs.pop_front();
            tc.join();
            ASSERT_TRUE( (*r).passed );
        }
}

TEST(ConcurrencyTest, testEventFlag)
{
    Event event;
    ASSERT_FALSE(event.isSet());

    event.set();
    ASSERT_TRUE(event.isSet());

    event.clear();
    ASSERT_FALSE(event.isSet());

    event.set();
    ASSERT_TRUE(event.wait(IceUtil::Time::milliSeconds(100)));
}
