package integration.gateway;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import ode.gateway.SecurityContext;
import ode.gateway.exception.DSAccessException;
import ode.gateway.exception.DSOutOfServiceException;
import ode.gateway.facility.AdminFacility;

import org.testng.Assert;
import org.testng.annotations.Test;

import ode.gateway.model.ExperimenterData;
import ode.gateway.model.GroupData;
import ode.model.enums.AdminPrivilegeChgrp;
import ode.model.enums.AdminPrivilegeChown;


/**
 * Tests for the {@link AdminFacility} methods.
 */
public class AdminFacilityTest extends GatewayTest {

    GroupData group;
    ExperimenterData exp;
    
    /**
     * Test creation of groups
     * @throws DSOutOfServiceException If an error occurred
     * @throws DSAccessException If an error occurred
     */
    @Test
    public void testCreateGroup() throws DSOutOfServiceException, DSAccessException {
        group = new GroupData();
        group.setName(UUID.randomUUID().toString());
        group = adminFacility.createGroup(rootCtx, group, null, GroupData.PERMISSIONS_GROUP_READ_WRITE);
        Assert.assertTrue(group.getId()>-1);
    }
    
    /**
     * Test creation of users
     * @throws DSOutOfServiceException If an error occurred
     * @throws DSAccessException If an error occurred
     */
    @Test(dependsOnMethods = {"testCreateGroup"})
    public void testCreateExperimenter() throws DSOutOfServiceException, DSAccessException {
        exp = new ExperimenterData();
        exp.setFirstName("Test");
        exp.setLastName("User");
        List<GroupData> groups = new ArrayList<GroupData>();
        groups.add(group);
        // create a 'light admin' user without any admin privileges
        exp = adminFacility
                .createExperimenter(rootCtx, exp, UUID.randomUUID().toString(),
                        "test", groups, true, true, Collections.<String>emptyList());
        Assert.assertTrue(exp.getId()>-1);
        Assert.assertTrue(adminFacility.getAdminPrivileges(rootCtx, exp).isEmpty());
    }
    
    /**
     * Test lookup of users
     * @throws DSOutOfServiceException If an error occurred
     * @throws DSAccessException If an error occurred
     */
    @Test(dependsOnMethods = {"testCreateExperimenter"})
    public void testLookupExperimenter() throws DSOutOfServiceException, DSAccessException {
        ExperimenterData e = adminFacility.lookupExperimenter(rootCtx, exp.getUserName());
        Assert.assertEquals(exp.getId(), e.getId());
    }
    
    /**
     * Test lookup of groups
     * @throws DSOutOfServiceException If an error occurred
     * @throws DSAccessException If an error occurred
     */
    @Test(dependsOnMethods = {"testCreateGroup"})
    public void testLookupGroup() throws DSOutOfServiceException, DSAccessException {
        GroupData g = adminFacility.lookupGroup(rootCtx, group.getName());
        Assert.assertEquals(group.getId(), g.getId());
    }
    
    /**
     * Test getting and setting admin privileges
     * @throws DSOutOfServiceException If an error occurred
     * @throws DSAccessException If an error occurred
     */
    @Test(dependsOnMethods = { "testCreateExperimenter" })
    public void testAdminPrivileges() throws DSOutOfServiceException,
            DSAccessException {
        SecurityContext userCtx = new SecurityContext(exp.getGroupId());
        Collection<String> privs = adminFacility.getAdminPrivileges(userCtx, exp);
        Assert.assertTrue(privs.isEmpty());
        
        adminFacility.setAdminPrivileges(userCtx, exp,
                Collections.singletonList(AdminPrivilegeChgrp.value));
        privs = adminFacility.getAdminPrivileges(userCtx, exp);
        Assert.assertEquals(privs.size(), 1);
        Assert.assertEquals(privs.iterator().next(), AdminPrivilegeChgrp.value);
        
        adminFacility.addAdminPrivileges(userCtx, exp,
                Collections.singletonList(AdminPrivilegeChown.value));
        privs = adminFacility.getAdminPrivileges(userCtx, exp);
        Assert.assertEquals(privs.size(), 2);
        Assert.assertTrue(privs.contains(AdminPrivilegeChgrp.value));
        Assert.assertTrue(privs.contains(AdminPrivilegeChown.value));
        
        adminFacility.removeAdminPrivileges(userCtx, exp,
                Collections.singletonList(AdminPrivilegeChown.value));
        privs = adminFacility.getAdminPrivileges(userCtx, exp);
        Assert.assertEquals(privs.size(), 1);
        Assert.assertTrue(privs.contains(AdminPrivilegeChgrp.value));
        
        Collection<String> allPrivs = adminFacility.getAvailableAdminPrivileges(userCtx);
        adminFacility.addAdminPrivileges(userCtx, exp, allPrivs);
        Assert.assertTrue(adminFacility.isFullAdmin(userCtx, exp));
        
        adminFacility.removeAdminPrivileges(userCtx, exp,
                Collections.singletonList(AdminPrivilegeChown.value));
        Assert.assertFalse(adminFacility.isFullAdmin(userCtx, exp));
    }
}
