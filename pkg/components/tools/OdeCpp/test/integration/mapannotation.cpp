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
#include <ode/model/ExperimenterGroupI.h>
#include <ode/sys/ParametersI.h>
#include <ode/util/uuid.h>

using namespace std;
using namespace ode::api;
using namespace ode::model;
using namespace ode::sys;
using namespace ode::rtypes;
using namespace ode::util;

static void assertFooBar(const ExperimenterGroupPtr& group)
{
    NamedValuePtr nv = group->getConfig()[0];
    ASSERT_EQ("foo", nv->name);
    ASSERT_EQ("bar", nv->value);
}

TEST(MapAnnotationTest, mapStringField)
{

    Fixture f;
    f.login();

    ServiceFactoryPrx sf = f.root->getSession();
    IQueryPrx q = sf->getQueryService();
    IUpdatePrx u = sf->getUpdateService();

    string uuid = generate_uuid();
    ExperimenterGroupPtr group = new ExperimenterGroupI();
    ParametersIPtr params = new ParametersI();
    NamedValueList map;
    NamedValuePtr nv = new NamedValue();
    nv->name = "foo";
    nv->value = "bar";
    group->setName(rstring(uuid));
    group->setLdap(rbool(true));
    map.push_back(nv);
    // Setting must happen after map updated, since a copy is made
    group->setConfig(map);
    assertFooBar(group);

    group = ExperimenterGroupPtr::dynamicCast(u->saveAndReturnObject(group));
    params->addId(group->getId()->getValue());
    group = ExperimenterGroupPtr::dynamicCast(q->findByQuery(
            "select g from ExperimenterGroup g left outer join fetch g.config where g.id = :id", params));
    assertFooBar(group);
}

TEST(MapAnnotationTest, asMapMethod)
{
    ExperimenterGroupPtr group = new ExperimenterGroupI();
    NamedValueList nvl;
    NamedValuePtr nv = new NamedValue();
    nv->name = "foo";
    nv->value = "bar";
    nvl.push_back(nv);
    group->setConfig(nvl);
    ode::api::StringStringMap asMap = group->getConfigAsMap();
    ASSERT_EQ("bar", asMap["foo"]);
}
