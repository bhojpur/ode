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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.UUID;

import integration.AbstractServerTest;
import ode.SecurityViolation;
import ode.gateway.Gateway;
import ode.gateway.JoinSessionCredentials;
import ode.gateway.LoginCredentials;
import ode.gateway.SecurityContext;
import ode.gateway.exception.DSOutOfServiceException;
import ode.gateway.facility.AdminFacility;
import ode.gateway.facility.BrowseFacility;
import ode.gateway.facility.DataManagerFacility;
import ode.gateway.model.DatasetData;
import ode.gateway.model.ExperimenterData;
import ode.gateway.model.GroupData;
import ode.log.SimpleLogger;
import ode.model.LongAnnotation;
import ode.model.LongAnnotationI;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

/**
 * Tests the login options supported by gateway
 */
public class GatewayUsageTest extends AbstractServerTest
{

    @Test
    public void testLoginWithCredentials()
            throws DSOutOfServiceException {
        ode.client client =  new ode.client();
        String port = client.getProperty("ode.port");
        LoginCredentials c = new LoginCredentials();
        c.getServer().setHostname(client.getProperty("ode.host"));
        c.getServer().setPort(Integer.parseInt(port));
        c.getUser().setUsername("root");
        c.getUser().setPassword(client.getProperty("ode.rootpass"));
        try (Gateway gw = new Gateway(new SimpleLogger())) {
            ExperimenterData root = gw.connect(c);
            Assert.assertNotNull(root);
        } catch (Exception e) {
            Assert.fail("Gateway credentials login failed.", e);
        }
    }

    @Test
    public void testLoginWithArgs()
            throws DSOutOfServiceException {
        ode.client client =  new ode.client();
        String[] args = new String[4];
        args[0] = "--ode.host="+client.getProperty("ode.host");
        args[1] = "--ode.port="+client.getProperty("ode.port");
        args[2] = "--ode.user=root";
        args[3] = "--ode.pass="+client.getProperty("ode.rootpass");
        LoginCredentials c = new LoginCredentials(args);
        try (Gateway gw = new Gateway(new SimpleLogger())) {
            ExperimenterData root = gw.connect(c);
            Assert.assertNotNull(root);
        } catch (Exception e) {
            Assert.fail("Gateway args login failed.", e);
        }
    }
    
    @Test
    public void testLoginWithSessionID() throws DSOutOfServiceException {
        ode.client client = new ode.client();
        String[] args = new String[4];
        args[0] = "--ode.host=" + client.getProperty("ode.host");
        args[1] = "--ode.port=" + client.getProperty("ode.port");
        args[2] = "--ode.user=root";
        args[3] = "--ode.pass=" + client.getProperty("ode.rootpass");
        LoginCredentials c = new LoginCredentials(args);
        
        try (Gateway gw = new Gateway(new SimpleLogger())) {
            ExperimenterData root = gw.connect(c);
            String sessionId = gw.getSessionId(root);
            try (Gateway gw2 = new Gateway(new SimpleLogger())) {
                JoinSessionCredentials c2 = new JoinSessionCredentials(
                        sessionId, client.getProperty("ode.host"),
                        Integer.parseInt(client.getProperty("ode.port")));
                ExperimenterData root2 = gw2.connect(c2);
                Assert.assertNotNull(root2);
                Assert.assertEquals(gw2.getSessionId(root2), sessionId);
            } catch (Exception e) {
                Assert.fail("Gateway sessionId login failed.", e);
            }
        } catch (Exception e1) {
            Assert.fail("Gateway credentials login failed.", e1);
        }
    }

    @Test
    public void testPreserveSession() throws DSOutOfServiceException {
        ode.client client = new ode.client();
        String[] args = new String[4];
        args[0] = "--ode.host=" + client.getProperty("ode.host");
        args[1] = "--ode.port=" + client.getProperty("ode.port");
        args[2] = "--ode.user=root";
        args[3] = "--ode.pass=" + client.getProperty("ode.rootpass");
        LoginCredentials c = new LoginCredentials(args);

        String sessionId = "";
        // create a session
        try (Gateway gw = new Gateway(new SimpleLogger())) {
            ExperimenterData user = gw.connect(c);
            SecurityContext ctx = new SecurityContext(user.getGroupId());
            gw.closeSessionOnExit(ctx, false); // do not close the session when disconnecting
            sessionId = gw.getSessionId(user);
            Assert.assertNotNull(sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // join session
        try (Gateway gw = new Gateway(new SimpleLogger())) {
            JoinSessionCredentials c2= new JoinSessionCredentials(
                    sessionId, client.getProperty("ode.host"),
                    Integer.parseInt(client.getProperty("ode.port")));
            gw.connect(c2);
            Assert.assertTrue(gw.isConnected());
            // JoinSessionCredentials by default should not close the server session
        } catch (Exception e) {
            e.printStackTrace();
        }

        // make sure the session is still active
        try (Gateway gw = new Gateway(new SimpleLogger())) {
            JoinSessionCredentials c2= new JoinSessionCredentials(
                    sessionId, client.getProperty("ode.host"),
                    Integer.parseInt(client.getProperty("ode.port")));
            ExperimenterData user = gw.connect(c2);
            Assert.assertTrue(gw.isConnected());
            SecurityContext ctx = new SecurityContext(user.getGroupId());
            gw.closeSessionOnExit(ctx, true); // force the session to be closed
        } catch (Exception e) {
            e.printStackTrace();
        }

        // make sure the session was closed
        try (Gateway gw = new Gateway(new SimpleLogger())) {
            JoinSessionCredentials c2= new JoinSessionCredentials(
                    sessionId, client.getProperty("ode.host"),
                    Integer.parseInt(client.getProperty("ode.port")));
            gw.connect(c2);
            Assert.fail("The session "+sessionId+" should have been closed.");
        } catch (Exception e) {
            // expected to fail
        }
    }
    
    Boolean sessionActive = null;

    @Test
    public void testAutoClose() throws DSOutOfServiceException {
        ode.client client = new ode.client();
        String[] args = new String[4];
        args[0] = "--ode.host=" + client.getProperty("ode.host");
        args[1] = "--ode.port=" + client.getProperty("ode.port");
        args[2] = "--ode.user=root";
        args[3] = "--ode.pass=" + client.getProperty("ode.rootpass");
        LoginCredentials c = new LoginCredentials(args);

        try (Gateway gw = new Gateway(new SimpleLogger())) {
            // do nothing; checks that auto close of non-connected Gateway
            // doesn't throw any exceptions.
        } catch (Exception e1) {
            Assert.fail("Gateway autoclose threw exception.", e1);
        }

        try (Gateway gw = new Gateway(new SimpleLogger())) {
            gw.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (Gateway.PROP_SESSION_CREATED.matches(evt.getPropertyName()))
                        sessionActive = Boolean.TRUE;
                    if (Gateway.PROP_SESSION_CLOSED.matches(evt.getPropertyName()))
                        sessionActive = Boolean.FALSE;
                }
            });
            Assert.assertNull(sessionActive);
            gw.connect(c);
            Assert.assertTrue(sessionActive);
        } catch (Exception e1) {
            Assert.fail("Gateway login failed.", e1);
        }
        Assert.assertFalse(sessionActive);
    }
    
    @Test
    public void testFailedLogin() throws DSOutOfServiceException {
        ode.client client = new ode.client();
        String port = client.getProperty("ode.port");

        LoginCredentials c = new LoginCredentials();
        c.getServer().setHostname(client.getProperty("ode.host"));
        c.getServer().setPort(Integer.parseInt(port));
        c.getUser().setUsername("root");
        c.getUser().setPassword("wrongPassword");

        try (Gateway gw = new Gateway(new SimpleLogger())) {
            gw.connect(c);
            Assert.fail("Connection should have failed");
        } catch (Exception e) {
            // Something about false "credentials"
            Assert.assertTrue(e.getMessage().contains("credentials"));
        }

        c.getServer().setHostname("UnknownHost");
        
        try (Gateway gw = new Gateway(new SimpleLogger())) {
            gw.connect(c);
            Assert.fail("Connection should have failed");
        } catch (Exception e) {
            // Something about "resolve" hostname failed
            Assert.assertTrue(e.getMessage().contains("resolve"));
        }
    }

    @Test
    public void testSwitchGroup() throws DSOutOfServiceException {
        ode.client client = new ode.client();
        String[] args = new String[4];
        args[0] = "--ode.host=" + client.getProperty("ode.host");
        args[1] = "--ode.port=" + client.getProperty("ode.port");
        args[2] = "--ode.user=root";
        args[3] = "--ode.pass=" + client.getProperty("ode.rootpass");
        LoginCredentials c = new LoginCredentials(args);

        long groupId = -1;

        // Create a new group
        try (Gateway gw = new Gateway(new SimpleLogger())) {
            ExperimenterData root = gw.connect(c);
            SecurityContext rootCtx = new SecurityContext(root.getGroupId());
            AdminFacility af = gw.getFacility(AdminFacility.class);
            GroupData g = new GroupData();
            g.setName(UUID.randomUUID().toString().substring(0, 8));
            g = af.createGroup(rootCtx, g, root,
                    GroupData.PERMISSIONS_GROUP_READ);
            groupId = g.getId();
            Assert.assertTrue(groupId > 0, "Create group failed");
        } catch (Exception e1) {
            Assert.fail("Create group failed.", e1);
        }

        // do something within the group context
        try (Gateway gw = new Gateway(new SimpleLogger())) {
            ExperimenterData root = gw.connect(c);

            // do something with root context...
            SecurityContext rootCtx = new SecurityContext(root.getGroupId());
            gw.getFacility(BrowseFacility.class).getDatasets(rootCtx);

            // then switch group
            SecurityContext groupCtx = new SecurityContext(groupId);
            DataManagerFacility df = gw.getFacility(DataManagerFacility.class);
            DatasetData ds = new DatasetData();
            ds.setName(UUID.randomUUID().toString().substring(0, 8));
            ds = df.createDataset(groupCtx, ds, null);
            Assert.assertTrue(ds.getId() >= 0,
                    "Dataset in new group was not created");
            Assert.assertEquals(ds.getGroupId(), groupId,
                    "Dataset does not belong to new group");
        } catch (Exception e1) {
            Assert.fail("Create dataset in new group failed.", e1);
        }
    }

    /**
     * Test that queries respond as expected to group context.
     * @throws Exception unexpected
     */
    @Test
    public void testGroupContextForService() throws Exception {
        /* as a normal user create an annotation */
        LongAnnotation annotation = new LongAnnotationI();
        annotation.setLongValue(ode.rtypes.rlong(1));
        annotation.setNs(ode.rtypes.rstring("test/" + getClass().getSimpleName() + "/" + UUID.randomUUID()));
        final long annotationId = iUpdate.saveAndReturnObject(annotation).getId().getValue();
        final long annotationGroupId = iAdmin.getEventContext().groupId;

        /* now do the rest of the test as the root user via the gateway */
        final LoginCredentials credentials = new LoginCredentials(
                roles.rootName, client.getProperty("ode.rootpass"),
                client.getProperty("ode.host"), Integer.valueOf(client.getProperty("ode.port")));
        try (final Gateway gateway = new Gateway(new SimpleLogger())) {
            gateway.connect(credentials);

            /* set query service to system group's context */
            iQuery = gateway.getQueryService(new SecurityContext(roles.systemGroupId));
            try {
                annotation = (LongAnnotation) iQuery.find(LongAnnotation.class.getSimpleName(), annotationId);
                Assert.fail("should not see annotation from wrong group");
            } catch (SecurityViolation sv) {
                /* expected */
            }

            /* do query in annotation group's context */
            iQuery = gateway.getQueryService(new SecurityContext(roles.systemGroupId));
            annotation = (LongAnnotation) iQuery.find(LongAnnotation.class.getSimpleName(), annotationId,
                    ImmutableMap.of("ode.group", Long.toString(annotationGroupId)));
            Assert.assertNotNull(annotation, "should see annotation from query in its group");

            /* set query service to annotation group's context */
            iQuery = gateway.getQueryService(new SecurityContext(annotationGroupId));
            annotation = (LongAnnotation) iQuery.find(LongAnnotation.class.getSimpleName(), annotationId);
            Assert.assertNotNull(annotation, "should see annotation from query service in its group");

            /* do query in all-groups context */
            iQuery = gateway.getQueryService(new SecurityContext(roles.systemGroupId));
            annotation = (LongAnnotation) iQuery.find(LongAnnotation.class.getSimpleName(), annotationId,
                    ALL_GROUPS_CONTEXT);
            Assert.assertNotNull(annotation, "should see annotation from query in all-groups context");

            try {
                /* set query service to all-groups context */
                iQuery = gateway.getQueryService(new SecurityContext(-1));
                Assert.fail("should not be possible without an equivalent of SERVICE_OPTS");
            } catch (DSOutOfServiceException dsoose) {
                Assert.assertEquals(dsoose.getCause().getClass(), IllegalArgumentException.class);
            }
        }
    }
}
