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
#include <ode/model/AcquisitionModeI.h>
#include <ode/model/ChannelI.h>
#include <ode/model/DimensionOrderI.h>
#include <ode/model/ImageI.h>
#include <ode/model/LogicalChannelI.h>
#include <ode/model/PhotometricInterpretationI.h>
#include <ode/model/PixelsI.h>
#include <ode/model/PixelsTypeI.h>
#include <ode/model/PlaneInfoI.h>
#include <ode/model/StatsInfoI.h>

using namespace ode::api;
using namespace ode::model;
using namespace ode::rtypes;

TEST(Beta3Test, SavingPixels )
{
    Fixture f;

    f.login();
    ServiceFactoryPrx sf = f.client->getSession();

    PixelsIPtr pix = f.pixels();

    // At this point trying to save throws a ValidationException
    try {
        sf->getUpdateService()->saveObject(pix);
        FAIL() << "Should fail";
    } catch (const ode::ValidationException& ve) {
        // ok
    }

    ImagePtr i = new_ImageI();
    i->addPixels( pix );
    i->setName( rstring("test1") );

    try {
        sf->getUpdateService()->saveObject(i);
    } catch (const ode::ValidationException& ve) {
        // ok
        FAIL() << ve.serverStackTrace;
    }

}
