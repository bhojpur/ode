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
#include <ode/model/TagAnnotationI.h>
#include <ode/model/ImageAnnotationLinkI.h>
#include <ode/model/ImageI.h>
#include <ode/model/FormatI.h>
#include <ode/model/OriginalFileI.h>
#include <ode/model/FileAnnotationI.h>
#include <ode/util/uuid.h>

#include <stdio.h>
#include <fstream>
#include <iostream>

//http://msdn.microsoft.com/en-us/library/t8ex5e91(VS.80).aspx
#ifdef _WIN32
#include <stdio.h>
#include <io.h>
#endif

using namespace std;
using namespace ode::api;
using namespace ode::model;
using namespace ode::sys;
using namespace ode::rtypes;
using namespace ode::util;

TEST(AnnotationTest, tagAnnotation )
{
    try {

        Fixture f;
        f.login();

        ServiceFactoryPrx sf = f.client->getSession();
        IQueryPrx q = sf->getQueryService();
        IUpdatePrx u = sf->getUpdateService();

        TagAnnotationIPtr tag = new TagAnnotationI();
        tag->setTextValue(rstring("my-first-tag"));

        string uuid = generate_uuid();
        ImageIPtr i = ImageIPtr::dynamicCast(new_ImageI());
        i->setName(rstring(uuid));
        i->linkAnnotation(tag);
        u->saveObject(i);

        i = ImageIPtr::dynamicCast(
                q->findByQuery(
                        "select i from Image i "
                        "join fetch i.annotationLinks l "
                        "join fetch l.child where i.name = '" + uuid +"'", 0));
        ImageAnnotationLinkIPtr link = ImageAnnotationLinkIPtr::dynamicCast(i->beginAnnotationLinks()[0]);
        AnnotationPtr a = link->getChild();
        tag = TagAnnotationIPtr::dynamicCast(a);
        ASSERT_EQ( "my-first-tag", tag->getTextValue()->getValue() );

    } catch (ode::ApiUsageException& aue) {
        cout << aue.message <<endl;
        throw;
    }
}

TEST(AnnotationTest, fileAnnotation )
{
    try {

        Fixture f;
        f.login();

        ServiceFactoryPrx sf = f.client->getSession();
        IQueryPrx q = sf->getQueryService();
        IUpdatePrx u = sf->getUpdateService();

        // Create temp file
        char pointer[]="tmpXXXXXX";
#ifdef _WIN32
        int err;
        err = _mktemp_s(pointer, 10); // Length plus one for null
        ASSERT_FALSE( err );
#else
        int fd = mkstemp(pointer);
        close(fd);
#endif

        string unique_content = generate_uuid();
        {
            ofstream out(pointer);
            out << "<xml>" << endl;
            out << "  " << unique_content << endl;
            out << "</xml>" << endl;
        }


        long size;
        Ice::ByteSeq buf;
        ifstream in(pointer, ios::binary);
        if (!in.good() || in.eof() || !in.is_open()) {
            size = 0;
        } else {
            ifstream::pos_type beg = in.tellg();
            in.seekg(0, ios_base::end);
            ifstream::pos_type end = in.tellg();
            size = static_cast<long>(end - beg);

            in.seekg(0, ios_base::beg);
            istream_iterator<Ice::Byte> b(in), e;
            vector<Ice::Byte> v (b, e);
            buf = v;
        }

        // Create file object
        OriginalFilePtr file = new OriginalFileI();
        file->setMimetype(rstring("text/xml"));
        file->setName(rstring("my-file.xml"));
        file->setPath(rstring("/tmp"));
        file->setHash(rstring("foo"));
        file->setSize(rlong(size));
        file = OriginalFilePtr::dynamicCast(u->saveAndReturnObject(file));

        // Upload file
        RawFileStorePrx rfs = sf->createRawFileStore();
        rfs->setFileId(file->getId()->getValue());
        rfs->write(buf, 0, buf.size());
        file = rfs->save(); // Updates the event to prevent OptimisticLockExceptions
        rfs->close();

        FileAnnotationPtr attachment = new FileAnnotationI();
        attachment->setFile(file);

        string uuid = generate_uuid();
        ImageIPtr i = ImageIPtr::dynamicCast(new_ImageI());
        i->setName(rstring(uuid));
        i->linkAnnotation(attachment);
        u->saveObject(i);

        i = ImageIPtr::dynamicCast(
                q->findByQuery(
                        "select i from Image i "
                        "join fetch i.annotationLinks l "
                        "join fetch l.child where i.name = '" + uuid +"'", 0));
        ImageAnnotationLinkPtr link = ImageAnnotationLinkPtr::dynamicCast(i->beginAnnotationLinks()[0]);
        AnnotationPtr a = link->getChild();
        attachment = FileAnnotationPtr::dynamicCast(a);

    } catch (ode::OptimisticLockException& ole) {
        FAIL() << ole.message;
    } catch (ode::ApiUsageException& aue) {
        cout << aue.message << endl;
        cout << aue.serverStackTrace << endl;
        FAIL() << "api usage exception";
    }
}
