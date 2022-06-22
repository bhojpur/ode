package integration;

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

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ode.RBool;
import ode.RMap;
import ode.RType;
import ode.ServerError;
import ode.gateway.util.Requests;
import ode.model.BooleanAnnotation;
import ode.model.BooleanAnnotationI;
import ode.model.CommentAnnotationI;
import ode.model.DetailsI;
import ode.model.Experimenter;
import ode.model.ExperimenterGroup;
import ode.model.IObject;
import ode.model.Namespace;
import ode.model.Permissions;
import ode.model.PermissionsI;
import ode.model.Session;
import ode.sys.EventContext;
import ode.sys.Parameters;
import ode.sys.ParametersI;

/**
 * Tests for the updated group permissions
 */
public class PermissionsTest extends AbstractServerTest {

    // chmod
    // ==============================================

    /*
     * See #8277 permissions returned from the server should now be immutable.
     */
    @Test
    public void testImmutablePermissions() throws Exception {

        // Test on the raw object
        PermissionsI p = new ode.model.PermissionsI();
        p.ice_postUnmarshal();
        try {
            p.setPerm1(1);
            Assert.fail("throw!");
        } catch (ode.ClientError err) {
            // good
        }

        // and on one returned from the server
        CommentAnnotationI c = new ode.model.CommentAnnotationI();
        c = (CommentAnnotationI) this.iUpdate.saveAndReturnObject(c);
        p = (PermissionsI) c.getDetails().getPermissions();
        try {
            p.setPerm1(1);
        } catch (ode.ClientError err) {
            // good
        }
    }

    @Test
    public void testDisallow() {
        PermissionsI p = new ode.model.PermissionsI();
        Assert.assertTrue(p.canAnnotate());
        Assert.assertTrue(p.canEdit());
    }

    @Test
    public void testClientSet() throws Exception {
        CommentAnnotationI c = new ode.model.CommentAnnotationI();
        c = (CommentAnnotationI) this.iUpdate.saveAndReturnObject(c);
        DetailsI d = (DetailsI) c.getDetails();
        Assert.assertNotNull(d.getClient());
        Assert.assertNotNull(d.getSession());
        Assert.assertNotNull(d.getCallContext());
        Assert.assertNotNull(d.getEventContext());
    }

    /**
     * Test that {@link ode.api.IQueryPrx#get(String, long)} returns object permissions reporting that the <tt>root</tt> user
     * <q>can</q> do everything.
     * @throws Exception unexpected
     */
    @Test
    public void testRootCanPermissionsByGet() throws Exception {
        final EventContext normalUser = newUserAndGroup("rwr---");
        final long projectId = iUpdate.saveAndReturnObject(mmFactory.simpleProject()).getId().getValue();
        logRootIntoGroup(normalUser.groupId);
        final Permissions projectPerms = iQuery.get("Project", projectId).getDetails().getPermissions();
        Assert.assertTrue(projectPerms.canEdit());
        Assert.assertTrue(projectPerms.canAnnotate());
        Assert.assertTrue(projectPerms.canLink());
        Assert.assertTrue(projectPerms.canDelete());
        Assert.assertTrue(projectPerms.canChgrp());
        Assert.assertTrue(projectPerms.canChown());
    }

    /**
     * Test that {@link ode.api.IQueryPrx#projection(String, ode.sys.Parameters)} returns object permissions reporting that the
     * <tt>root</tt> user <q>can</q> do everything.
     * @throws Exception unexpected
     */
    @Test
    public void testRootCanPermissionsByProjection() throws Exception {
        final EventContext normalUser = newUserAndGroup("rwr---");
        final long projectId = iUpdate.saveAndReturnObject(mmFactory.simpleProject()).getId().getValue();
        logRootIntoGroup(normalUser.groupId);
        final Map<String, RType> queriedMap = ((RMap) iQuery.projection(
                "SELECT new map(project AS project_details_permissions) FROM Project AS project WHERE project.id = :id",
                new ParametersI().addId(projectId)).get(0).get(0)).getValue();
        final Map<String, RType> projectPermsMap = ((RMap) queriedMap.get("project_details_permissions")).getValue();
        Assert.assertTrue(((RBool) projectPermsMap.get("canEdit")).getValue());
        Assert.assertTrue(((RBool) projectPermsMap.get("canAnnotate")).getValue());
        Assert.assertTrue(((RBool) projectPermsMap.get("canLink")).getValue());
        Assert.assertTrue(((RBool) projectPermsMap.get("canDelete")).getValue());
        Assert.assertTrue(((RBool) projectPermsMap.get("canChgrp")).getValue());
        Assert.assertTrue(((RBool) projectPermsMap.get("canChown")).getValue());
    }

    /**
     * Test that an instance of the given class can be annotated only if the instance's {@link Permissions#canAnnotate()} is true.
     * @param annotateeClass the class of which to try annotating an instance
     * @throws Exception unexpected
     */
    @Test(dataProvider = "annotation classes")
    public void testCanAnnotateConsistency(Class<? extends IObject> annotateeClass)
            throws Exception {
        final Parameters params = new ParametersI().page(0, 1);
        final List<IObject> toAnnotates = iQuery.findAllByQuery("FROM " + annotateeClass.getSimpleName(), params);
        if (CollectionUtils.isEmpty(toAnnotates)) {
            throw new SkipException("nothing to annotate");
        }
        final IObject toAnnotate = toAnnotates.get(0);
        final boolean isExpectSuccess = toAnnotate.getDetails().getPermissions().canAnnotate();
        final BooleanAnnotation annotation = new BooleanAnnotationI();
        annotation.setBoolValue(ode.rtypes.rbool(false));
        IObject link = mmFactory.createAnnotationLink(toAnnotate.proxy(), annotation);
        try {
            link = iUpdate.saveAndReturnObject(link);
            Assert.assertTrue(isExpectSuccess);
        } catch (ServerError se) {
            Assert.assertFalse(isExpectSuccess);
        } finally {
            if (link.getId() != null) {
                doChange(Requests.delete().target(link).build());
            }
        }
    }

    /**
     * @return test cases for {@link #testCanAnnotateConsistency(Class)}
     */
    @DataProvider(name = "annotation classes") Object[][] provideSystemClasses() {
        return new Object[][] {
            new Object[] {Experimenter.class},
            new Object[] {ExperimenterGroup.class},
            new Object[] {Namespace.class},
            new Object[] {Session.class}};
    }
}
