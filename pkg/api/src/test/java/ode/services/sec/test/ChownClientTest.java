package ode.services.sec.test;

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

import org.testng.annotations.*;

import ode.conditions.SecurityViolation;

@Test(enabled=false, groups = { "broken", "client", "integration", "security", "ticket:52", "chown" })
public class ChownClientTest extends AbstractChangeDetailClientTest {

    // design parameters:
    // 1. new or existing object (belonging to root, user, or other)
    // 2. as user or root
    // 3. changing to root, user, or other

    // ~ AS USER TO ROOT
    // =========================================================================

    @Test(enabled=false, expectedExceptions = SecurityViolation.class)
    public void test_NewImageAsUserChownToROOT() throws Exception {
        createAsUserToOwner(asUser, toRoot);
    }

    @Test(enabled=false, expectedExceptions = SecurityViolation.class)
    public void test_UserImageAsUserChownToROOT() throws Exception {
        updateAsUserToOwner(managedImage(asUser), asUser, toRoot);
    }

    @Test(enabled=false, expectedExceptions = SecurityViolation.class)
    public void test_OtherImageAsUserChownToROOT() throws Exception {
        updateAsUserToOwner(managedImage(asOther), asUser, toRoot);
    }

    @Test(enabled=false)
    // This should be ok since it already belongs to root.
    public void test_RootImageAsUserChownToROOT() throws Exception {
        updateAsUserToOwner(managedImage(asRoot), asUser, toRoot);
    }

    // ~ AS USER TO USER
    // =========================================================================
    @Test(enabled=false)
    public void test_NewImageAsUserChownToUSER() throws Exception {
        createAsUserToOwner(asUser, toUser);
    }

    @Test(enabled=false)
    public void test_UserImageAsUserChownToUSER() throws Exception {
        updateAsUserToOwner(managedImage(asUser), asUser, toUser);
    }

    @Test(enabled=false, expectedExceptions = SecurityViolation.class)
    public void test_OtherImageAsUserChownToUSER() throws Exception {
        updateAsUserToOwner(managedImage(asOther), asUser, toUser);
    }

    @Test(enabled=false, expectedExceptions = SecurityViolation.class)
    public void test_RootImageAsUserChownToUSER() throws Exception {
        updateAsUserToOwner(managedImage(asRoot), asUser, toUser);
    }

    // ~ AS USER TO OTHER
    // =========================================================================
    @Test(enabled=false, expectedExceptions = SecurityViolation.class)
    public void test_NewImageAsUserChownToOTHER() throws Exception {
        createAsUserToOwner(asUser, toOther);
    }

    @Test(enabled=false, expectedExceptions = SecurityViolation.class)
    public void test_UserImageAsUserChownToOTHER() throws Exception {
        updateAsUserToOwner(managedImage(asUser), asUser, toOther);
    }

    @Test(enabled=false)
    public void test_OtherImageAsUserChownToOTHER() throws Exception {
        updateAsUserToOwner(managedImage(asOther), asUser, toOther);
    }

    @Test(enabled=false, expectedExceptions = SecurityViolation.class)
    public void test_RootImageAsUserChownToOTHER() throws Exception {
        updateAsUserToOwner(managedImage(asRoot), asUser, toOther);
    }

    // ~ AS ROOT TO USER
    // =========================================================================
    @Test(enabled=false)
    public void test_NewImageAsRootChownToUSER() throws Exception {
        createAsUserToOwner(asRoot, toUser);
    }

    @Test(enabled=false)
    public void test_UserImageAsRootChownToUSER() throws Exception {
        updateAsUserToOwner(managedImage(asUser), asRoot, toUser);
    }

    @Test(enabled=false)
    public void test_OtherImageAsRootChownToUSER() throws Exception {
        updateAsUserToOwner(managedImage(asOther), asRoot, toUser);
    }

    @Test(enabled=false)
    public void test_RootImageAsRootChownToUSER() throws Exception {
        updateAsUserToOwner(managedImage(asRoot), asRoot, toUser);
    }

    // ~ AS ROOT TO OTHER
    // =========================================================================
    @Test(enabled=false)
    public void test_NewImageAsRootChownToOTHER() throws Exception {
        createAsUserToOwner(asRoot, toOther);
    }

    @Test(enabled=false)
    public void test_UserImageAsRootChownToOTHER() throws Exception {
        updateAsUserToOwner(managedImage(asUser), asRoot, toOther);
    }

    @Test(enabled=false)
    public void test_OtherImageAsRootChownToOTHER() throws Exception {
        updateAsUserToOwner(managedImage(asOther), asRoot, toOther);
    }

    @Test(enabled=false)
    public void test_RootImageAsRootChownToOTHER() throws Exception {
        updateAsUserToOwner(managedImage(asRoot), asRoot, toOther);
    }

    // ~ AS ROOT TO ROOT
    // =========================================================================

    @Test(enabled=false)
    public void test_NewImageAsRootChownToROOT() throws Exception {
        createAsUserToOwner(asRoot, toRoot);
    }

    @Test(enabled=false)
    public void test_UserImageAsRootChownToROOT() throws Exception {
        updateAsUserToOwner(managedImage(asUser), asRoot, toRoot);
    }

    @Test(enabled=false)
    public void test_OtherImageAsRootChownToROOT() throws Exception {
        updateAsUserToOwner(managedImage(asOther), asRoot, toRoot);
    }

    @Test(enabled=false)
    public void test_RootImageAsRootChownToROOT() throws Exception {
        updateAsUserToOwner(managedImage(asRoot), asRoot, toRoot);
    }

}