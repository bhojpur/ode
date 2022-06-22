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

#include <ode/IceNoWarnPush.h>
#include <Ice/Initialize.h>
#include <ode/IceNoWarnPop.h>
#include <ode/client.h>
#include <ode/model/ExperimenterI.h>
#include <ode/model/GroupExperimenterMapI.h>
#include <ode/model/ExperimenterGroupI.h>
#include <ode/model/SessionI.h>
#include <ode/fixture.h>

using namespace ode::rtypes;

TEST(SessionsTest, RootCanCreateSessionForUser )
{
  Fixture f;
  ode::api::ServiceFactoryPrx sf = f.root->getSession();
  ode::api::ISessionPrx sess = sf->getSessionService();

  ode::model::ExperimenterGroupPtr group = f.newGroup("rwr---");
  ode::model::ExperimenterPtr e = f.newUser(group);

  ode::sys::PrincipalPtr p = new ode::sys::Principal();
  p->name = e->getOdeName()->getValue();
  p->group = e->getPrimaryGroupExperimenterMap()->getParent()->getName()->getValue();
  p->eventType = "Test";
  ode::model::SessionPtr session = sess->createSessionWithTimeout(p, 10000L);

  ode::client_ptr user = new ode::client(f.root->getPropertyMap());
  user->joinSession(session->getUuid()->getValue());
  ode::api::ServiceFactoryPrx sf2 = f.root->getSession();
  sf2->closeOnDestroy();
  sf2->getQueryService()->get("Experimenter",0L);
}

