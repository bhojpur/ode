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
#include <ode/cmd/Graphs.h>
#include <string>
#include <map>

using namespace std;
using namespace ode;
using namespace ode::api;
using namespace ode::cmd;
using namespace ode::cmd::graphs;
using namespace ode::callbacks;
using namespace ode::model;
using namespace ode::rtypes;
using namespace ode::sys;


TEST(ChgrpTest, testSimpleChgrp ) {
    Fixture f;
    ExperimenterGroupPtr g1 = f.newGroup();
    ExperimenterGroupPtr g2 = f.newGroup();
    ExperimenterPtr user = f.newUser(g1);
    f.addExperimenter(g2, user);

    f.login(user->getOdeName()->getValue(), user->getOdeName()->getValue());
    ServiceFactoryPrx sf = f.client->getSession();
    IAdminPrx admin = sf->getAdminService();
    ASSERT_EQ(g1->getId()->getValue(), admin->getEventContext()->groupId);

    IQueryPrx iquery = sf->getQueryService();
    IUpdatePrx iupdate = sf->getUpdateService();

    ImagePtr image = new ImageI();
    image->setName( rstring("testSimpleChgrp") );
    image = ImagePtr::dynamicCast( iupdate->saveAndReturnObject( image ) );

    ode::sys::LongList imageIds;
    StringLongListMap objects;
    ChildOptions options;
    Chgrp2Ptr chgrpCmd = new Chgrp2();
    imageIds.push_back( image->getId()->getValue() );
    objects["Image"] = imageIds;
    chgrpCmd->targetObjects = objects;
    chgrpCmd->groupId = g2->getId()->getValue();
    chgrpCmd->childOptions = options;

    HandlePrx handle = sf->submit( chgrpCmd );
    CmdCallbackIPtr cb = new CmdCallbackI(f.client, handle);
    ResponsePtr rsp = cb->loop(10, 500);
    ERRPtr err = ERRPtr::dynamicCast(rsp);
    if (err) {
        FAIL() << "ERR returned";
    }

}
