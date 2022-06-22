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

#ifndef ODE_MODEL_DETAILSI_H
#define ODE_MODEL_DETAILSI_H

#include <ode/IceNoWarnPush.h>
#include <ode/model/Details.h>
#include <Ice/Ice.h>
#include <IceUtil/Config.h>
#include <Ice/Handle.h>
#include <ode/IceNoWarnPop.h>

#include <ode/client.h>
#include <ode/model/ExperimenterI.h>
#include <ode/model/ExperimenterGroupI.h>
#include <ode/model/EventI.h>
#include <ode/model/ExternalInfoI.h>
#include <ode/model/PermissionsI.h>

#include <iostream>
#include <string>
#include <vector>

#ifndef ODE_CLIENT
#   ifdef ODE_CLIENT_EXPORTS
#       define ODE_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define ODE_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

namespace ode {
    namespace model {
	class DetailsI;
    }
}

namespace IceInternal {
  ODE_CLIENT ::Ice::Object* upCast(::ode::model::DetailsI*);
}

namespace ode {

    namespace model {

	/*
	 * Simple implementation of the Details.ice
	 * type embedded in every ODE.server type.
	 */

	typedef IceInternal::Handle<DetailsI> DetailsIPtr;

	class ODE_CLIENT DetailsI : virtual public Details {

	protected:
	    virtual ~DetailsI(); // protected as outlined in Ice docs.

            // This must be stored as a raw pointer to prevent circular ref with client
            const ode::client* client;
            /*const*/ ode::api::ServiceFactoryPrx session;
	public:

          DetailsI(const ode::client* client = NULL);

          const ode::client* getClient() const;

          const ode::api::ServiceFactoryPrx getSession() const;

          /*const*/ ode::sys::EventContextPtr getEventContext() const;

          /*const*/ std::map<std::string, std::string> getCallContext() const;

          virtual ode::model::ExperimenterPtr getOwner(const Ice::Current& current = Ice::Current());

	  virtual void setOwner(const ode::model::ExperimenterPtr& _owner, const Ice::Current& current = Ice::Current());

	  virtual ode::model::ExperimenterGroupPtr getGroup(const Ice::Current& current = Ice::Current());

	  virtual void setGroup(const ode::model::ExperimenterGroupPtr& _group, const Ice::Current& current = Ice::Current());

	  virtual ode::model::EventPtr getCreationEvent(const Ice::Current& current = Ice::Current());

	  virtual void setCreationEvent(const ode::model::EventPtr& _creationEvent, const Ice::Current& current = Ice::Current());

	  virtual ode::model::EventPtr getUpdateEvent(const Ice::Current& current = Ice::Current());

	  virtual void setUpdateEvent(const ode::model::EventPtr& _updateEvent, const Ice::Current& current = Ice::Current());

	  virtual ode::model::PermissionsPtr getPermissions(const Ice::Current& current = Ice::Current());

	  virtual void setPermissions(const ode::model::PermissionsPtr& _permissions, const Ice::Current& current = Ice::Current());

	  virtual ode::model::ExternalInfoPtr getExternalInfo(const Ice::Current& current = Ice::Current());

	  virtual void setExternalInfo(const ode::model::ExternalInfoPtr& _externalInfo, const Ice::Current& current = Ice::Current());

	};

  }
}
#endif // ODE_MODEL_DETAILSI_H
