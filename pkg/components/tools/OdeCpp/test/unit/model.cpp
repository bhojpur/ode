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
#include <ode/model/ProjectI.h>
#include <ode/model/ProjectDatasetLinkI.h>
#include <ode/model/DatasetI.h>
#include <ode/model/DatasetImageLinkI.h>
#include <ode/model/ImageI.h>
#include <ode/model/PixelsI.h>
#include <ode/model/ChannelI.h>
#include <ode/model/LengthI.h>
#include <ode/model/TagAnnotationI.h>
#include <ode/model/GroupExperimenterMapI.h>

using namespace ode::rtypes;
using namespace ode::model;
using namespace ode;
using namespace std;

TEST(ModelTest, DetailsPtrIsNull )
{
    DetailsIPtr p;
    ASSERT_FALSE(p);
}

TEST(ModelTest, Virtual )
{
  ImagePtr img = new ImageI();
  ImageIPtr imgI = new ImageI();
  img->unload();
  imgI->unload();
}

TEST(ModelTest, Toggle )
{
  PixelsIPtr pix = new PixelsI();
  ASSERT_TRUE( pix->sizeOfSettings() >= 0 );
  pix->unloadCollections();
  ASSERT_TRUE( pix->sizeOfSettings() < 0 );
}

TEST(ModelTest, SimpleCtor )
{
  ImageIPtr img = new ImageI();
  ASSERT_TRUE( img->isLoaded() );
  ASSERT_TRUE( img->sizeOfPixels() >= 0 );
}

TEST(ModelTest, UnloadedCtor )
{
  ImageIPtr img = new ImageI(rlong(1),false);
  ASSERT_TRUE( !(img->isLoaded()) );
  ASSERT_THROW( img->sizeOfDatasetLinks(), ode::UnloadedEntityException );
}

TEST(ModelTest, UnloadCheckPtr )
{
  ImageIPtr img = new ImageI();
  ASSERT_TRUE( img->isLoaded() );
  // operator bool() is overloaded
  ASSERT_TRUE( img->getDetails() ); // details are auto instantiated
  ASSERT_TRUE( ! img->getName() ); // no other single-valued field is
  img->unload();
  ASSERT_TRUE( !img->isLoaded() );
  ASSERT_THROW( img->getDetails(), ode::UnloadedEntityException );
}

TEST(ModelTest, UnloadField )
{
  ImageIPtr img = new ImageI();
  ASSERT_TRUE( img->getDetails() );
  img->unloadDetails();
  ASSERT_TRUE( ! img->getDetails() );
}

TEST(ModelTest, Sequences )
{
  ImageIPtr img = new ImageI();
  ASSERT_EQ(0, img->sizeOfAnnotationLinks());
  img->unloadAnnotationLinks();
  img->unload();
  ASSERT_THROW( img->sizeOfAnnotationLinks(), ode::UnloadedEntityException );
}

TEST(ModelTest, Accessors )
{
  RStringPtr name = rstring("name");
  ImageIPtr img = new ImageI();
  ASSERT_TRUE( !img->getName() );
  img->setName( name );
  ASSERT_TRUE( img->getName() );
  RStringPtr str = img->getName();
  ASSERT_EQ("name", str->getValue());
  ASSERT_TRUE(str == name );

  img->setName(rstring("name2"));
  ASSERT_TRUE( img->getName() );
  ASSERT_EQ("name2",  img->getName()->getValue());

  img->unload();
  ASSERT_THROW( img->getName(), ode::UnloadedEntityException );
}

TEST(ModelTest, UnloadedAccessThrows )
{
  ImageIPtr unloaded = new ImageI(rlong(1),false);
  ASSERT_THROW( unloaded->getName(), ode::UnloadedEntityException );
}

TEST(ModelTest, Iterators )
{

  DatasetIPtr d = new DatasetI();
  ImageIPtr image = new ImageI();
  image->linkDataset(d);
  ImageDatasetLinksSeq::iterator it= image->beginDatasetLinks();
  int count = 0;
  for (;it != image->endDatasetLinks(); ++it) {
    count++;
  }
  ASSERT_EQ(1, count);
}

TEST(ModelTest, ClearSet )
{
  ImageIPtr img = new ImageI();
  ASSERT_TRUE( img->sizeOfPixels() >= 0 );
  img->addPixels( new PixelsI() );
  ASSERT_EQ(1, img->sizeOfPixels());
  img->clearPixels();
  ASSERT_TRUE( img->sizeOfPixels() >= 0 );
  ASSERT_EQ(0, img->sizeOfPixels());
}

TEST(ModelTest, UnloadSet )
{
  ImageIPtr img = new ImageI();
  ASSERT_TRUE( img->sizeOfPixels() >= 0 );
  img->addPixels( new PixelsI() );
  ASSERT_EQ(1, img->sizeOfPixels());
  img->unloadPixels();
  ASSERT_TRUE( img->sizeOfPixels() < 0 );
}

TEST(ModelTest, RemoveFromSet )
{
  PixelsIPtr pix = new PixelsI();
  ImageIPtr img = new ImageI();
  ASSERT_TRUE( img->sizeOfPixels() >= 0 );

  img->addPixels( pix );
  ASSERT_EQ(1, img->sizeOfPixels());

  img->removePixels( pix );
  ASSERT_EQ(0, img->sizeOfPixels());
}

TEST(ModelTest, LinkGroupAndUser )
{

  ExperimenterIPtr user = new ExperimenterI();
  ExperimenterGroupIPtr group = new ExperimenterGroupI();
  GroupExperimenterMapIPtr map = new GroupExperimenterMapI();

  map->setId( rlong(1) );
  map->link(group,user);
  user->addGroupExperimenterMapToBoth( map, false );
  group->addGroupExperimenterMapToBoth( map, false );

  typedef ExperimenterGroupExperimenterMapSeq::iterator egm_it;
  egm_it beg = user->beginGroupExperimenterMap();
  egm_it end = user->endGroupExperimenterMap();
  int count = 0 ;
  for( ; beg != end; beg++ ) {
    ++count;
  }
  ASSERT_EQ(1, count);

}

TEST(ModelTest, LinkViaMap )
{
  ExperimenterIPtr user = new ExperimenterI();
  user->setFirstName(rstring("test"));
  user->setLastName(rstring("user"));
  user->setOdeName(rstring("UUID"));

  // possibly setOdeName() and setOdeName(string) ??
  // and then don't need ode/types.h

  ExperimenterGroupIPtr group = new ExperimenterGroupI();
  // TODOuser->linkExperimenterGroup(group);
  GroupExperimenterMapIPtr map = new GroupExperimenterMapI();
  map->setParent( group );
  map->setChild( user );
}

TEST(ModelTest, LinkingAndUnlinking )
{

  DatasetImageLinkIPtr dil;

  DatasetIPtr d = new DatasetI();
  ImageIPtr   i = new ImageI();

  d->linkImage(i);
  ASSERT_EQ(1, d->sizeOfImageLinks());
  d->unlinkImage(i);
  ASSERT_EQ(0, d->sizeOfImageLinks());

  d = new DatasetI();
  i = new ImageI();
  d->linkImage(i);
  ASSERT_EQ(1, i->sizeOfDatasetLinks());
  i->unlinkDataset(d);
  ASSERT_EQ(0, d->sizeOfImageLinks());

  d = new DatasetI();
  i = new ImageI();
  dil = new DatasetImageLinkI();
  dil->link(d,i);
  d->addDatasetImageLinkToBoth(dil, false);
  ASSERT_EQ(1, d->sizeOfImageLinks());
  ASSERT_EQ(0, i->sizeOfDatasetLinks());

}

TEST(ModelTest, UnloadedEntityTermination ) {


  ProjectDatasetLinkIPtr pDL = new ProjectDatasetLinkI();
  ProjectIPtr p = new ProjectI();
  DatasetIPtr d = new DatasetI();
  //pDL->link(p,d);
  d->unload();
  ode::model::IObjectPtr             theChild =
  ode::model::IObjectPtr::dynamicCast(pDL->getChild());
  //cout << "theChild is: " << theChild << "... pDS" << endl;
  ode::model::DatasetIPtr            pDS =
  ode::model::DatasetIPtr::dynamicCast(theChild);

}

TEST(ModelTest, PrimaryPixels ) {


    ImageIPtr i = new ImageI();

    ASSERT_TRUE( i->isPixelsLoaded() );
    ASSERT_EQ( 0, i->sizeOfPixels() );
    bool called = false;
    ImagePixelsSeq::iterator beg = i->beginPixels();
    ImagePixelsSeq::iterator end = i->endPixels();
    while (beg != end) {
        called = true;
        beg++;
    }
    ASSERT_FALSE( called );


    PixelsIPtr p = new PixelsI();
    i->addPixels( p );

    ASSERT_TRUE( i->isPixelsLoaded() );
    ASSERT_EQ( 1, i->sizeOfPixels() );
    ASSERT_TRUE( p == i->beginPixels()[0] );
    beg = i->beginPixels();
    end = i->endPixels();
    while (beg != end) {
        called = true;
        beg++;
    }
    ASSERT_EQ( true, called );


    i->unloadPixels();

    ASSERT_FALSE( i->isPixelsLoaded() );
    ASSERT_EQ( -1, i->sizeOfPixels() );
    try {
        i->beginPixels();
        FAIL() << "Should have thrown an exception ";
    } catch (const std::exception& ex) {
        // ok
    }

}

TEST(ModelTest, OrderedCollectionsTicket2547 ) {
    PixelsPtr pixels = new PixelsI();
    ChannelPtr channel0 = new ChannelI();
    ChannelPtr channel1 = new ChannelI();
    ChannelPtr channel2 = new ChannelI();
    pixels->addChannel(channel0);
    ASSERT_EQ(1, pixels->sizeOfChannels());
    ChannelPtr old = pixels->setChannel(0, channel1);
    ASSERT_TRUE(old == channel0);
    ASSERT_EQ(1, pixels->sizeOfChannels());
}

TEST(ModelTest, LengthGetSymbol) {
    LengthPtr l = new LengthI();
    l->setValue(1.0);
    l->setUnit(ode::model::enums::MICROMETER);
    ASSERT_EQ("??m", l->getSymbol());
}

TEST(ModelTest, LengthLookupSymbol) {
    ASSERT_EQ("??m", LengthI::lookupSymbol(
            ode::model::enums::MICROMETER));
}
