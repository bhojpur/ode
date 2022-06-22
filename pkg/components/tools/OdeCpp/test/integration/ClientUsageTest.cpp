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
#include <ode/fixture.h>
#include <ode/client.h>
#include <algorithm>

using namespace ode::rtypes;

TEST(ClientUsageTest, testClientClosedAutomatically)
{
    ode::client_ptr client = new ode::client();
    client->createSession();
    client->getSession()->closeOnDestroy();
}

TEST(ClientUsageTest, testClientClosedManually )
{
    ode::client_ptr client = new ode::client();
    client->createSession();
    client->getSession()->closeOnDestroy();
    client->closeSession();
}

TEST(ClientUsageTest, testUseSharedMemory )
{
    ode::client_ptr client = new ode::client();
    client->createSession();

    ASSERT_EQ(0U, client->getInputKeys().size());
    client->setInput("a", rstring("b"));
    ASSERT_EQ(1U, client->getInputKeys().size());
    std::vector<std::string> keys = client->getInputKeys();
    std::vector<std::string>::iterator it = find(keys.begin(), keys.end(), "a");
    ASSERT_TRUE( it != keys.end() );
    ASSERT_EQ("b", ode::RStringPtr::dynamicCast(client->getInput("a"))->getValue());

    client->closeSession();
}

TEST(ClientUsageTest, testCreateInsecureClientTicket2099 )
{
    ode::client_ptr secure = new ode::client();
    ASSERT_TRUE(secure->isSecure());
    secure->createSession()->getAdminService()->getEventContext();
    ode::client_ptr insecure = secure->createClient(false);
    insecure->getSession()->getAdminService()->getEventContext();
    ASSERT_FALSE( insecure->isSecure());
}

TEST(ClientUsageTest, testGetStatefulServices )
{
    Fixture f;
    ode::api::ServiceFactoryPrx sf = f.root->getSession();
    sf->setSecurityContext(new ode::model::ExperimenterGroupI(0L, false));
    sf->createRenderingEngine();
    std::vector<ode::api::StatefulServiceInterfacePrx> srvs = f.root->getStatefulServices();
    ASSERT_EQ(1U, srvs.size());
    try {
        sf->setSecurityContext(new ode::model::ExperimenterGroupI(1L, false));
        FAIL() << "Should not be allowed";
    } catch (const ode::SecurityViolation& sv) {
        // good
    }
    srvs.at(0)->close();
    srvs = f.root->getStatefulServices();
    ASSERT_EQ(0U, srvs.size());
    sf->setSecurityContext(new ode::model::ExperimenterGroupI(1L, false));
}

TEST(ClientUsageTest, testKillSession)
{
    Fixture f;
    f.login();
    int count = f.client->killSession();
    ASSERT_EQ(count, 1);
}
