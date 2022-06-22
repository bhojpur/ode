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
#include <ode/API.h>
#include <ode/Collections.h>

using namespace std;
using namespace ode;
using namespace ode::api;
using namespace ode::model;

TEST(AdminTest, getGroup) {
    Fixture f;
    f.login();
    
    ServiceFactoryPrx sf = f.client->getSession();
    IAdminPrx admin = sf->getAdminService();
    
    ExperimenterGroupList groups = admin->lookupGroups();
    ExperimenterGroupPtr g = admin->getGroup(groups[0]->getId()->getValue());
    
    ASSERT_EQ(g->getId()->getValue(), groups[0]->getId()->getValue());
    ASSERT_GT(g->sizeOfGroupExperimenterMap(), 0);
}

TEST(AdminTest, setGroup) {
    Fixture f;
    f.login();
    
    ServiceFactoryPrx sf = f.client->getSession();
    IAdminPrx admin = sf->getAdminService();
    
    Ice::Long uid = admin->getEventContext()->userId;
    
    // Add user to new group to test setting default
    ExperimenterPtr user = admin->getExperimenter(uid);
    ExperimenterGroupPtr group = f.newGroup();
    f.addExperimenter(group, user);

    admin->setDefaultGroup(user, group);
    ExperimenterGroupPtr defGroup = admin->getDefaultGroup(uid);
    ASSERT_EQ(defGroup->getId()->getValue(), group->getId()->getValue());
}