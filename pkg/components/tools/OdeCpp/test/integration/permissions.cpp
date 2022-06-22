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
#include <ode/model/CommentAnnotationI.h>
#include <string>
#include <map>

using namespace std;
using namespace ode;
using namespace ode::api;
using namespace ode::cmd;
using namespace ode::callbacks;
using namespace ode::model;
using namespace ode::rtypes;
using namespace ode::sys;

TEST( PermissionsTest, testImmutablePermissions ) {

    PermissionsIPtr p = new PermissionsI();
    p->setPerm1(1L);
    p->setWorldRead(true);

    p = new PermissionsI();
    p->ice_postUnmarshal();

    ASSERT_THROW(p->setPerm1(1L), ode::ClientError);
    ASSERT_THROW(p->setWorldRead(true), ode::ClientError);

    Fixture f;
    f.login();
    ServiceFactoryPrx sf = f.client->getSession();
    IUpdatePrx iupdate = sf->getUpdateService();

    CommentAnnotationPtr c = new CommentAnnotationI();
    c = CommentAnnotationPtr::dynamicCast( iupdate->saveAndReturnObject( c ) );
    p = PermissionsIPtr::dynamicCast( c->getDetails()->getPermissions() );

    ASSERT_THROW(p->setPerm1(1L), ode::ClientError);
    ASSERT_THROW(p->setWorldRead(true), ode::ClientError);

}

TEST( PermissionsTest, testDisallow ) {

    PermissionsIPtr p = new PermissionsI();
    ASSERT_TRUE(p->canAnnotate());
    ASSERT_TRUE(p->canEdit());

}

TEST( PermissionsTest, testClientSet ) {

    std::map<std::string, std::string> myctx;
    myctx["test"] = "value";

    Fixture f;
    f.login();
    ServiceFactoryPrx sf = f.client->getSession();
    IUpdatePrx iupdate = sf->getUpdateService();

    CommentAnnotationPtr c = new CommentAnnotationI();
    c = CommentAnnotationPtr::dynamicCast( iupdate->saveAndReturnObject( c, myctx ) );

    DetailsPtr d = c->getDetails();
    ASSERT_TRUE(d);

    DetailsIPtr di = DetailsIPtr::dynamicCast(d);
    ASSERT_TRUE(di->getClient());
    ASSERT_TRUE(di->getSession());
    ASSERT_TRUE(di->getEventContext());
    ASSERT_EQ("value", di->getCallContext()["test"]);
}

static void assertPerms(const string& msg, const client_ptr& client,
        const IObjectPtr& obj, bool ann, bool edit) {

    IQueryPrx query = client->getSession()->getQueryService();
    IObjectPtr copy = query->get("CommentAnnotation", obj->getId()->getValue());
    PermissionsPtr p = copy->getDetails()->getPermissions();
    ASSERT_EQ(ann, p->canAnnotate()) << msg;
    ASSERT_EQ(edit, p->canEdit()) << msg;
}

TEST( PermissionsTest, testAdjustPermissions ) {
    try {
        Fixture f;
        ExperimenterGroupPtr group = f.newGroup("rwr---");
        ExperimenterPtr user1 = f.newUser(group);
        ExperimenterPtr user2 = f.newUser(group);
        f.login(user1, user1->getOdeName()->getValue());
        IQueryPrx query = f.client->getSession()->getQueryService();
        IUpdatePrx update = f.client->getSession()->getUpdateService();

        CommentAnnotationPtr c = new CommentAnnotationI();
        c = CommentAnnotationPtr::dynamicCast( update->saveAndReturnObject(c) );

        assertPerms("creator can ann/edit", f.client, c, true, true);
        f.login(user2, user2->getOdeName()->getValue());
        assertPerms("group member can't ann/edit", f.client, c, false, false);

        // Search all groups for the annotation
        std::stringstream groupId;
        groupId << group->getId()->getValue();
        std::string gid = groupId.str();
        f.root->getImplicitContext()->put("ode.group", gid);
        assertPerms("root can ann/edit", f.root, c, true, true);
    } catch (const ode::ValidationException& ve) {
        FAIL() << "validation exception:" + ve.message;
    }

}
