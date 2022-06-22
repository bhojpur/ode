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

#include <ode/model/DetailsI.h>

::Ice::Object* IceInternal::upCast(::ode::model::DetailsI* p) { return p; }

namespace ode {

    namespace model {

        DetailsI::DetailsI(const ode::client* client):
          Details(),
          client(client) {
            if (client) {
                session = client->getSession();
            }
        }

        DetailsI::~DetailsI() {}

        const ode::client* DetailsI::getClient() const {
            return client;
        }

        const ode::api::ServiceFactoryPrx DetailsI::getSession() const {
              return session;
        }

        /*const*/ ode::sys::EventContextPtr DetailsI::getEventContext() const {
            return event;
        }

        /*const*/ std::map<std::string, std::string> DetailsI::getCallContext() const {
            return call;
        }

        ode::model::ExperimenterPtr DetailsI::getOwner(const Ice::Current& /*current */) {
            return owner ;
        }

        void DetailsI::setOwner(const ode::model::ExperimenterPtr& _owner, const Ice::Current& /*current */) {
              owner = _owner ;
        }

        ode::model::ExperimenterGroupPtr DetailsI::getGroup(const Ice::Current& /*current */) {
              return group ;
        }

        void DetailsI::setGroup(const ode::model::ExperimenterGroupPtr& _group, const Ice::Current& /*current */) {
              group = _group ;
        }

        ode::model::EventPtr DetailsI::getCreationEvent(const Ice::Current& /*current */) {
              return creationEvent ;
        }

        void DetailsI::setCreationEvent(const ode::model::EventPtr& _creationEvent, const Ice::Current& /*current */) {
              creationEvent = _creationEvent ;
        }

        ode::model::EventPtr DetailsI::getUpdateEvent(const Ice::Current& /*current */) {
              return updateEvent ;
        }

        void DetailsI::setUpdateEvent(const ode::model::EventPtr& _updateEvent, const Ice::Current& /*current */) {
              updateEvent = _updateEvent ;
        }

        ode::model::PermissionsPtr DetailsI::getPermissions(const Ice::Current& /*current */) {
              return permissions ;
        }

        void DetailsI::setPermissions(const ode::model::PermissionsPtr& _permissions, const Ice::Current& /*current */) {
              permissions = _permissions ;
        }

        ode::model::ExternalInfoPtr DetailsI::getExternalInfo(const Ice::Current& /*current */) {
              return externalInfo ;
        }

        void DetailsI::setExternalInfo(const ode::model::ExternalInfoPtr& _externalInfo, const Ice::Current& /*current */) {
              externalInfo = _externalInfo ;
        }

    }
}
