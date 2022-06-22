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

#include <ode/model/PermissionsI.h>
#include <ode/fixture.h>
#include <ode/ClientErrors.h>

using namespace ode;
using namespace ode::model;

TEST( PermsTest, Perm1 )
{
  ode::model::PermissionsIPtr p = new ode::model::PermissionsI();

  // The default
  ASSERT_TRUE( p->isUserRead() );
  ASSERT_TRUE( p->isUserWrite() );
  ASSERT_TRUE( p->isGroupRead() );
  ASSERT_TRUE( p->isGroupWrite() );
  ASSERT_TRUE( p->isWorldRead() );
  ASSERT_TRUE( p->isWorldWrite() );

  // All off
  p->setPerm1( 0L );
  ASSERT_TRUE( ! p->isUserRead() );
  ASSERT_TRUE( ! p->isUserWrite() );
  ASSERT_TRUE( ! p->isGroupRead() );
  ASSERT_TRUE( ! p->isGroupWrite() );
  ASSERT_TRUE( ! p->isWorldRead() );
  ASSERT_TRUE( ! p->isWorldWrite() );

  // All on
  p->setPerm1( -1L );
  ASSERT_TRUE( p->isUserRead() );
  ASSERT_TRUE( p->isUserWrite() );
  ASSERT_TRUE( p->isGroupRead() );
  ASSERT_TRUE( p->isGroupWrite() );
  ASSERT_TRUE( p->isWorldRead() );
  ASSERT_TRUE( p->isWorldWrite() );

  // Various swaps
  p->setUserRead(false);
  ASSERT_TRUE( !p->isUserRead() );
  p->setGroupWrite(true);
  ASSERT_TRUE( p->isGroupWrite() );

  // Now reverse each of the above
  p->setUserRead(true);
  ASSERT_TRUE( p->isUserRead() );
  p->setGroupWrite(false);
  ASSERT_TRUE( !p->isGroupWrite() );

}

TEST( PermsTest, invalidCreate ) {
    bool exceptionThrown = false;
    try {
        PermissionsIPtr p = new PermissionsI("r");
    }
    catch (ClientError e) {
        exceptionThrown = true;
    }

    ASSERT_TRUE(exceptionThrown);
}

static void testStringPerms( const char* perms, bool allOn ) {
    PermissionsIPtr p = new PermissionsI(perms);
    ASSERT_EQ(p->isUserRead(), allOn);
    ASSERT_EQ(p->isUserWrite(), allOn);
    ASSERT_EQ(p->isGroupRead(), allOn);
    ASSERT_EQ(p->isGroupWrite(), allOn);
    ASSERT_EQ(p->isWorldRead(), allOn);
    ASSERT_EQ(p->isWorldWrite(), allOn);
}

TEST( PermsTest, createFromString ) {
    testStringPerms("rwrwrw", true);
    testStringPerms("RWRWRW", true);
    testStringPerms("------", false);
}
