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
#include <ode/model/ImageI.h>
#include <ode/model/TagAnnotationI.h>

using namespace std;
using namespace ode::api;
using namespace ode::model;
using namespace ode::rtypes;
using namespace ode::sys;

TEST(CountsTest, Counts )
{
    try {
        Fixture f;
        f.login();

        ServiceFactoryPrx sf = f.client->getSession();
        IAdminPrx admin = sf->getAdminService();
        IQueryPrx query = sf->getQueryService();
        IUpdatePrx update = sf->getUpdateService();

        long usr = admin->getEventContext()->userId;

        ImagePtr img = new_ImageI();
        img->setName( rstring("name") );
        TagAnnotationIPtr tag = new TagAnnotationI();
        img->linkAnnotation( tag );
        img = ImageIPtr::dynamicCast( update->saveAndReturnObject( img ) );

        stringstream q;
        q << "select img from Image img ";
        q << "join fetch img.annotationLinksCountPerOwner ";
        q << "where img.id = ";
        q << img->getId()->getValue();
        img = ImageIPtr::dynamicCast( query->findByQuery(q.str(), 0) );

        ASSERT_TRUE( img->getAnnotationLinksCountPerOwner()[usr] > 0 );

    } catch (const ode::ApiUsageException& aue) {
        cout << aue.message <<endl;
        FAIL() << "api usage exception thrown";
    } catch (const Ice::UnknownException& ue) {
        cout << ue << endl;
        FAIL() << "unknown exception thrown";
    }
}
