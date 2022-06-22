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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ode.RLong;
import ode.api.IPixelsPrx;
import ode.gateway.Gateway;
import ode.gateway.LoginCredentials;
import ode.gateway.SecurityContext;
import ode.gateway.exception.DSAccessException;
import ode.gateway.exception.DSOutOfServiceException;
import ode.gateway.facility.AdminFacility;
import ode.gateway.facility.BrowseFacility;
import ode.gateway.facility.DataManagerFacility;
import ode.gateway.facility.Facility;
import ode.gateway.facility.MetadataFacility;
import ode.gateway.facility.ROIFacility;
import ode.gateway.facility.RawDataFacility;
import ode.gateway.facility.ROIFacility;
import ode.gateway.facility.SearchFacility;
import ode.gateway.facility.TablesFacility;
import ode.gateway.facility.TransferFacility;
import ode.log.SimpleLogger;
import ode.model.IObject;
import ode.model.PixelsType;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ode.gateway.model.DatasetData;
import ode.gateway.model.ExperimenterData;
import ode.gateway.model.FolderData;
import ode.gateway.model.GroupData;
import ode.gateway.model.ImageData;
import ode.gateway.model.PlateData;
import ode.gateway.model.ProjectData;
import ode.gateway.model.ScreenData;

public class GatewayTest {

    /** Identifies the <code>user</code> group. */
    public String USER_GROUP = "user";

    Gateway gw = null;
    ExperimenterData root = null;
    SecurityContext rootCtx = null;

    AdminFacility adminFacility = null;
    BrowseFacility browseFacility = null;
    RawDataFacility rawdataFacility = null;
    SearchFacility searchFacility = null;
    TransferFacility transferFacility = null;
    DataManagerFacility datamanagerFacility = null;
    MetadataFacility metadataFacility = null;
    ROIFacility roiFacility = null;
    TablesFacility tablesFacility = null;

    @Test
    public void testConnected() throws DSOutOfServiceException {
        String version = gw.getServerVersion();
        Assert.assertTrue(version != null && version.trim().length() > 0);
    }
    
    /**
     * Initializes the Gateway.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @BeforeClass(alwaysRun = true)
    protected void setUp() throws Exception {

        ode.client client =  new ode.client();
        String pass = client.getProperty("ode.rootpass");
        String host = client.getProperty("ode.host");
        String port = client.getProperty("ode.port");
        
        LoginCredentials c = new LoginCredentials();
        c.getServer().setHostname(host);
        c.getServer().setPort(Integer.parseInt(port));
        c.getUser().setUsername("root");
        c.getUser().setPassword(pass);

        gw = new Gateway(new SimpleLogger());
        root = gw.connect(c);

        rootCtx = new SecurityContext(root.getDefaultGroup().getGroupId());
        rootCtx.setExperimenter(root);

        adminFacility = Facility.getFacility(AdminFacility.class, gw);
        browseFacility = Facility.getFacility(BrowseFacility.class, gw);
        rawdataFacility = Facility.getFacility(RawDataFacility.class, gw);
        searchFacility = Facility.getFacility(SearchFacility.class, gw);
        transferFacility = Facility.getFacility(TransferFacility.class, gw);
        datamanagerFacility = Facility.getFacility(DataManagerFacility.class,
                gw);
        metadataFacility = Facility.getFacility(MetadataFacility.class,
                gw);
        roiFacility = Facility.getFacility(ROIFacility.class,
                gw);
        tablesFacility = Facility.getFacility(TablesFacility.class, gw);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        if (gw != null)
            gw.disconnect();
    }

    GroupData createGroup() throws DSOutOfServiceException,
            DSAccessException {
        GroupData group = new GroupData();
        group.setName(UUID.randomUUID().toString());
        return adminFacility.createGroup(rootCtx, group, null,
                GroupData.PERMISSIONS_GROUP_READ_WRITE);
    }

    ExperimenterData createExperimenter(GroupData group)
            throws DSOutOfServiceException, DSAccessException {
        ExperimenterData exp = new ExperimenterData();
        exp.setFirstName("Test");
        exp.setLastName("User");
        List<GroupData> groups = new ArrayList<GroupData>();
        if (group != null)
            groups.add(group);
        return adminFacility.createExperimenter(rootCtx, exp, UUID.randomUUID()
                .toString(), "test", groups, false, true);
    }

    FolderData createFolder(SecurityContext ctx)
            throws DSOutOfServiceException, DSAccessException {
        FolderData folder = new FolderData();
        folder.setName(UUID.randomUUID().toString());
        return (FolderData) datamanagerFacility.saveAndReturnObject(ctx, folder);
    }
    
    ProjectData createProject(SecurityContext ctx)
            throws DSOutOfServiceException, DSAccessException {
        ProjectData proj = new ProjectData();
        proj.setName(UUID.randomUUID().toString());
        return (ProjectData) datamanagerFacility.saveAndReturnObject(ctx, proj);
    }

    ScreenData createScreen(SecurityContext ctx)
            throws DSOutOfServiceException, DSAccessException {
        ScreenData screen = new ScreenData();
        screen.setName(UUID.randomUUID().toString());
        return (ScreenData) datamanagerFacility
                .saveAndReturnObject(ctx, screen);
    }

    DatasetData createDataset(SecurityContext ctx, ProjectData proj)
            throws DSOutOfServiceException, DSAccessException {
        DatasetData ds = new DatasetData();
        ds.setName(UUID.randomUUID().toString());
        if (proj != null) {
            Set<ProjectData> projs = new HashSet<ProjectData>(1);
            projs.add(proj);
            ds.setProjects(projs);
        }
        return (DatasetData) datamanagerFacility.saveAndReturnObject(ctx, ds);
    }

    PlateData createPlate(SecurityContext ctx, ScreenData screen)
            throws DSOutOfServiceException, DSAccessException {
        PlateData plate = new PlateData();
        plate.setName(UUID.randomUUID().toString());
        if (screen != null) {
            Set<ScreenData> screens = new HashSet<ScreenData>(1);
            screens.add(screen);
            plate.setScreens(screens);
        }
        return (PlateData) datamanagerFacility.saveAndReturnObject(ctx, plate);
    }

    ImageData createImage(SecurityContext ctx, DatasetData ds)
            throws Exception {
        long imgId = createImage(ctx);
        List<Long> ids = new ArrayList<Long>(1);
        ids.add(imgId);
        ImageData img = browseFacility.getImages(ctx, ids).iterator().next();

        if (ds != null) {
            List<ImageData> l = new ArrayList<ImageData>(1);
            l.add(img);
            datamanagerFacility.addImagesToDataset(ctx, l, ds);

            ids.clear();
            ids.add(ds.getId());
            ds = browseFacility.getDatasets(ctx, ids).iterator().next();
        }
        
        return img;
    }

    private long createImage(SecurityContext ctx) throws Exception {
        String name = UUID.randomUUID().toString();
        IPixelsPrx svc = gw.getPixelsService(ctx);
        List<IObject> types = gw.getTypesService(ctx)
                .allEnumerations(PixelsType.class.getName());
        List<Integer> channels = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            channels.add(i);
        }
        RLong id = svc.createImage(10, 10, 10, 10, channels,
                (PixelsType) types.get(1), name, "");
        return id.getValue();
    }
}
