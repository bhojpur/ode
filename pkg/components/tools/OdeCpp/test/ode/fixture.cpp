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

#include <algorithm>
#include <ode/fixture.h>
#include <ode/model/LengthI.h>
#include <ode/model/PixelsTypeI.h>
#include <ode/model/PhotometricInterpretationI.h>
#include <ode/model/AcquisitionModeI.h>
#include <ode/model/DimensionOrderI.h>
#include <ode/model/ChannelI.h>
#include <ode/model/LogicalChannelI.h>
#include <ode/model/StatsInfoI.h>
#include <ode/model/PlaneInfoI.h>
#include <ode/util/uuid.h>

using namespace ode::api;
using namespace ode::model;
using namespace ode::model::enums;
using namespace ode::rtypes;

ode::model::ImagePtr new_ImageI()
{
    ode::model::ImagePtr img = new ode::model::ImageI();
    return img;
}

Fixture::Fixture()
{
    client = new ode::client();
    std::string rootpass = client->getProperty("ode.rootpass");
    logout();
    login("root", rootpass);
    root = client;
    client = ode::client_ptr();
}

void Fixture::logout()
{
    root = NULL;
    client = NULL;
}


void Fixture::show_stackframe() {
#ifdef LINUX
  void *trace[16];
  char **messages = (char **)NULL;
  int i, trace_size = 0;

  trace_size = backtrace(trace, 16);
  messages = backtrace_symbols(trace, trace_size);
  printf("[bt] Execution path:\n");
  for (i=0; i<trace_size; ++i)
    printf("[bt] %s\n", messages[i]);
#endif
}

std::string Fixture::uuid()
{
  std::string s = ode::util::generate_uuid();
    std::replace(s.begin(), s.end(), '-', 'X');
    return s;
}

void Fixture::printUnexpected()
{
  /* Need printStackTrace.h for this
  char* buf = new char[1024];
  printStackTrace(buf, 1024);
  std::cout << "Trace:" << buf << std::endl;
  delete[] buf;
  */
}

void Fixture::login(const std::string& username, const std::string& password) {
    client = new ode::client();
    client->createSession(username, password);
    client->getSession()->closeOnDestroy();
}

void Fixture::login(const ode::model::ExperimenterPtr& user, const std::string& password) {
    this->login(user->getOdeName()->getValue(), password);
}

ode::model::ExperimenterPtr Fixture::newUser(const ode::model::ExperimenterGroupPtr& _g) {

    IAdminPrx admin = root->getSession()->getAdminService();
    ode::model::ExperimenterGroupPtr g(_g);
    ode::RStringPtr name = ode::rtypes::rstring(uuid());
    ode::RStringPtr groupName = name;
    long gid = -1;
    if (!g) {
        g = new ode::model::ExperimenterGroupI();
        g->setName( name );
        g->setLdap( rbool(false) );
        gid = admin->createGroup(g);
    } else {
        gid = g->getId()->getValue();
        groupName = admin->getGroup(gid)->getName();
    }
    ode::model::ExperimenterPtr e = new ode::model::ExperimenterI();
    e->setOdeName( name );
    e->setFirstName( name );
    e->setLastName( name );
    e->setLdap( rbool(false) );
    std::vector<ExperimenterGroupPtr> groups;
    ode::model::ExperimenterGroupPtr userGroup = admin->lookupGroup("user");
    groups.push_back(userGroup);
    long id = admin->createExperimenterWithPassword(e, name, g, groups);
    return admin->getExperimenter(id);
}

ode::model::ExperimenterGroupPtr Fixture::newGroup(const std::string& perms) {
    IAdminPrx admin = root->getSession()->getAdminService();
    std::string gname = uuid();
    ExperimenterGroupPtr group = new ExperimenterGroupI();
    group->setName( rstring(gname) );
    group->setLdap( rbool(false) );
    if (!perms.empty()) {
        group->getDetails()->setPermissions( new PermissionsI(perms) );
    }
    long gid = admin->createGroup(group);
    group = admin->getGroup(gid);
    return group;
}

void Fixture::addExperimenter(
        const ode::model::ExperimenterGroupPtr& group,
        const ode::model::ExperimenterPtr& user) {

        IAdminPrx admin = root->getSession()->getAdminService();
        std::vector<ExperimenterGroupPtr> groups;
        groups.push_back(group);
        admin->addGroups(user, groups);
}

ode::model::PixelsIPtr Fixture::pixels() {
    PixelsIPtr pix = new PixelsI();
    PixelsTypePtr pt = new PixelsTypeI();
    PhotometricInterpretationIPtr pi = new PhotometricInterpretationI();
    AcquisitionModeIPtr mode = new AcquisitionModeI();
    DimensionOrderIPtr d0 = new DimensionOrderI();
    ChannelIPtr c = new ChannelI();
    LogicalChannelIPtr lc = new LogicalChannelI();
    StatsInfoIPtr si = new StatsInfoI();
    PlaneInfoIPtr pl = new PlaneInfoI();

    mode->setValue( rstring("Wide-field") );
    pi->setValue( rstring("RGB") );
    pt->setValue( rstring("int8") );
    d0->setValue( rstring("XYZTC") );

    lc->setPhotometricInterpretation( pi );

    UnitsLength mm = ode::model::enums::MILLIMETER;
    LengthPtr mm1 = new LengthI();
    mm1->setUnit(mm);
    mm1->setValue(1.0);

    pix->setSizeX( rint(1) );
    pix->setSizeY( rint(1) );
    pix->setSizeZ( rint(1) );
    pix->setSizeT( rint(1) );
    pix->setSizeC( rint(1) );
    pix->setSha1 (rstring("09bc7b2dcc9a510f4ab3a40c47f7a4cb77954356") ); // for "pixels"
    pix->setPixelsType( pt );
    pix->setDimensionOrder( d0 );
    pix->setPhysicalSizeX(mm1);
    pix->setPhysicalSizeY(mm1);
    pix->setPhysicalSizeZ(mm1);

    pix->addChannel( c );
    c->setLogicalChannel( lc) ;
    return pix;
}
