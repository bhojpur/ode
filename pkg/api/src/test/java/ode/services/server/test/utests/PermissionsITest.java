package ode.services.server.test.utests;

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

import static ode.model.internal.Permissions.Role.*;
import static ode.model.internal.Permissions.Right.*;
import static ode.model.internal.Permissions.Flag.*;

import ode.model.internal.Permissions;
import ode.util.Utils;
import ode.model.PermissionsI;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = "ticket:685")
public class PermissionsITest  {

    Permissions p;
    PermissionsI pI;
    
    @BeforeMethod
    public void configure() throws Exception {
        p = new Permissions();
        pI = new PermissionsI();
        pI.setPerm1( (Long) Utils.internalForm(p) );
        verify(p,pI);
    }

    public void testPermissionsDefault() throws Exception {
        pI = new PermissionsI();
        Assert.assertFalse(pI.isUserRead());
        Assert.assertFalse(pI.isUserAnnotate());
        Assert.assertFalse(pI.isUserWrite());
        Assert.assertFalse(pI.isGroupRead());
        Assert.assertFalse(pI.isGroupAnnotate());
        Assert.assertFalse(pI.isGroupWrite());
        Assert.assertFalse(pI.isWorldRead());
        Assert.assertFalse(pI.isWorldAnnotate());
        Assert.assertFalse(pI.isWorldWrite());
    }
    
    public void testPermissionsUserRead() throws Exception {
        p.grant(USER, READ);
        pI.setUserRead(true);
        verify(p,pI);
        p.revoke(USER, READ);
        pI.setUserRead(false);
        verify(p,pI);
    }

    public void testPermissionsUserAnnotate() throws Exception {
        p.grant(USER, ANNOTATE);
        pI.setUserAnnotate(true);
        verify(p,pI);
        p.revoke(USER, ANNOTATE);
        pI.setUserAnnotate(false);
        verify(p,pI);
    }

    public void testPermissionsUserWrite() throws Exception {
        p.grant(USER, WRITE);
        pI.setUserWrite(true);
        verify(p,pI);
        p.revoke(USER, WRITE);
        pI.setUserWrite(false);
        verify(p,pI);
    }

    public void testPermissionsGroupRead() throws Exception {
        p.grant(GROUP, READ);
        pI.setGroupRead(true);
        verify(p,pI);
        p.revoke(GROUP, READ);
        pI.setGroupRead(false);
        verify(p,pI);
    }
    
    public void testPermissionsGroupAnnotate() throws Exception {
        p.grant(GROUP, ANNOTATE);
        pI.setGroupAnnotate(true);
        verify(p,pI);
        p.revoke(GROUP, ANNOTATE);
        pI.setGroupAnnotate(false);
        verify(p,pI);
    }

    public void testPermissionsGroupWrite() throws Exception {
        p.grant(GROUP, WRITE);
        pI.setGroupWrite(true);
        verify(p,pI);
        p.revoke(GROUP, WRITE);
        pI.setGroupWrite(false);
        verify(p,pI);
    }
    
    public void testPermissionsWorldRead() throws Exception {
        p.grant(WORLD, READ);
        pI.setWorldRead(true);
        verify(p,pI);
        p.revoke(WORLD, READ);
        pI.setWorldRead(false);
        verify(p,pI);
    }

    public void testPermissionsWorldAnnotate() throws Exception {
        p.grant(WORLD, ANNOTATE);
        pI.setWorldAnnotate(true);
        verify(p,pI);
        p.revoke(WORLD, ANNOTATE);
        pI.setWorldAnnotate(false);
        verify(p,pI);
    }

    public void testPermissionsWorldWrite() throws Exception {
        p.grant(WORLD, WRITE);
        pI.setWorldWrite(true);
        verify(p,pI);
        p.revoke(WORLD, WRITE);
        pI.setWorldWrite(false);
        verify(p,pI);
    }

    void verify(Permissions p, PermissionsI pI) {
        Assert.assertEquals(p.isGranted(USER, READ), pI.isUserRead());
        Assert.assertEquals(p.isGranted(USER, ANNOTATE), pI.isUserAnnotate());
        Assert.assertEquals(p.isGranted(USER, WRITE), pI.isUserWrite());
        Assert.assertEquals(p.isGranted(GROUP, READ), pI.isGroupRead());
        Assert.assertEquals(p.isGranted(GROUP, ANNOTATE), pI.isGroupAnnotate());
        Assert.assertEquals(p.isGranted(GROUP, WRITE), pI.isGroupWrite());
        Assert.assertEquals(p.isGranted(WORLD, READ), pI.isWorldRead());
        Assert.assertEquals(p.isGranted(WORLD, ANNOTATE), pI.isWorldAnnotate());
        Assert.assertEquals(p.isGranted(WORLD, WRITE), pI.isWorldWrite());
    }

}