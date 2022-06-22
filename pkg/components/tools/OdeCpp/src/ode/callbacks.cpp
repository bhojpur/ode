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

#include <ode/callbacks.h>
#include <ode/util/uuid.h>

#include <ode/IceNoWarnPush.h>
#include <ode/ClientErrors.h>
#include <ode/RTypesI.h>
#include <IceUtil/Time.h>
#include <ode/IceNoWarnPop.h>

using namespace std;
using namespace IceUtil;
using namespace ode;
using namespace ode::grid;
using namespace ode::rtypes;

::Ice::Object* IceInternal::upCast(::ode::callbacks::ProcessCallbackI* p) { return p; }
::Ice::Object* IceInternal::upCast(::ode::callbacks::CmdCallbackI* p) { return p; }

namespace
{
    const std::string FINISHED("FINISHED");
    const std::string CANCELLED("CANCELLED");
    const std::string KILLED("KILLED");
}

namespace ode {

    namespace callbacks {


        //
        // ProcessCallback
        //

        ProcessCallbackI::ProcessCallbackI(
                const Ice::ObjectAdapterPtr& adapter,
                const ProcessPrx& process,
                bool poll) :
            ProcessCallback(),
            adapter(adapter),
            id(Ice::Identity()),
            poll(poll),
            result(std::string()),
            process(process) {

            this->id.category = "ProcessCallback";
            this->id.name = util::generate_uuid();
            Ice::ObjectPrx prx = adapter->add(this, this->id);
            ode::grid::ProcessCallbackPrx pcb = ProcessCallbackPrx::uncheckedCast(prx);
            process->registerCallback(pcb);

        }

        void ProcessCallbackI::close() {
            if (adapter)
                adapter->remove(id);
        }

        std::string ProcessCallbackI::block(long ms) {
            if (poll) {
                try {
                    RIntPtr rc = process->poll();
                    if (rc) {
                        processFinished(rc->getValue());
                    }
                } catch (const Ice::Exception& ex) {
                    cerr << "Error calling poll: " << ex << endl;
                }
            }
            event.wait(Time::milliSeconds(ms));
            return result; // Possibly empty
        }

        void ProcessCallbackI::processCancelled(bool /*success*/, const Ice::Current&) {
            result = CANCELLED;
            event.set();
        }

        void ProcessCallbackI::processFinished(int /*returncode*/, const Ice::Current&) {
            result = FINISHED;
            event.set();
        }

        void ProcessCallbackI::processKilled(bool /*success*/, const Ice::Current&) {
            result = KILLED;
            event.set();
        }

        //
        // CmdCallback
        //

        CmdCallbackI::CmdCallbackI(
            const Ice::ObjectAdapterPtr& adapter, const ode::cmd::HandlePrx handle, std::string category, bool closeHandle) :
            adapter(adapter),
            handle(handle),
            closeHandle(closeHandle){
                doinit(category);
        }

        CmdCallbackI::CmdCallbackI(
            const ode::client_ptr& client, const ode::cmd::HandlePrx handle, bool closeHandle) :
            adapter(client->getObjectAdapter()),
            handle(handle),
            closeHandle(closeHandle){
                doinit(client->getCategory());
        }

        void CmdCallbackI::doinit(std::string category) {
            this->id = Ice::Identity();
            this->id.name = util::generate_uuid();
            this->id.category = category;
            Ice::ObjectPrx prx = adapter->add(this, id);
            ode::cmd::CmdCallbackPrx cb = ode::cmd::CmdCallbackPrx::uncheckedCast(prx);
            handle->addCallback(cb);
            initialPoll();
        }

        void CmdCallbackI::initialPoll() {
            // Now check just in case the process exited VERY quickly
            pollThread = new PollThread(this);
            pollThread->start();
        }

        void CmdCallbackI::close() {
            if (adapter) {
                adapter->remove(id); // OK ADAPTER USAGE
            }
            if (closeHandle && handle) {
                handle->close();
            }
        }

        ode::cmd::ResponsePtr CmdCallbackI::getResponse() {
            IceUtil::RecMutex::Lock lock(mutex);
            return state.first;
        }

        ode::cmd::StatusPtr CmdCallbackI::getStatus() {
            IceUtil::RecMutex::Lock lock(mutex);
            return state.second;
        }

        ode::cmd::StatusPtr CmdCallbackI::getStatusOrThrow() {
            IceUtil::RecMutex::Lock lock(mutex);
            ode::cmd::StatusPtr s = state.second;
            if (!s) {
                throw ClientError(__FILE__, __LINE__, "Status not present!");
            }
            return s;
        }

        int CmdCallbackI::count(ode::cmd::StateList list, ode::cmd::State s) {
            int c = 0;
            ode::cmd::StateList::iterator it;
            for (it=list.begin(); it != list.end(); it++) {
                if (*it == s) {
                    c++;
                }
            }
            return c;
        }

        bool CmdCallbackI::isCancelled() {
            ode::cmd::StatusPtr s = getStatusOrThrow();
            return count(s->flags, ode::cmd::CANCELLED) > 0;

        }

        bool CmdCallbackI::isFailure() {
            ode::cmd::StatusPtr s = getStatusOrThrow();
            return count(s->flags, ode::cmd::FAILURE) > 0;

        }

        ode::cmd::ResponsePtr CmdCallbackI::loop(int loops, long ms) {
            int count = 0;
            bool found = false;
            while (count < loops) {
                count++;
                found = block(ms);
                if (found) {
                    break;
                }
            }

            if (found) {
                return getResponse();
            } else {
                int waited = (ms/1000.0) * loops;
                stringstream ss;
                ss << "Cmd unfinished after " << waited << "seconds.";
                throw LockTimeout("", "", ss.str(), 5000L, waited);
            }
        }

        bool CmdCallbackI::block(long ms) {
            return event.wait(Time::milliSeconds(ms));
        }

        void CmdCallbackI::poll() {
            IceUtil::RecMutex::Lock lock(mutex);

            ode::cmd::ResponsePtr rsp = handle->getResponse();
            if (rsp) {
                ode::cmd::StatusPtr s = handle->getStatus();
                finished(rsp, s, Ice::Current()); // Only time that current should be null.
            }

            pollThread = NULL;
        }

        void CmdCallbackI::step(int /*complete*/, int /*total*/, const Ice::Current&) {
            // no-op
        }

        void CmdCallbackI::finished(const ode::cmd::ResponsePtr& rsp,
                const ode::cmd::StatusPtr& status, const Ice::Current& current) {
            std::pair<ode::cmd::ResponsePtr, ode::cmd::StatusPtr> s(rsp, status);
            this->state = s;
            event.set();
            onFinished(rsp, status, current);
        }

        void CmdCallbackI::onFinished(const ode::cmd::ResponsePtr&,
                const ode::cmd::StatusPtr&, const Ice::Current&) {
            // no-op
        }
    }

}
