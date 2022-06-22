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

import ode.ServerError;
import ode.api.IUpdatePrx;
import ode.gateway.SecurityContext;
import ode.gateway.exception.DSAccessException;
import ode.gateway.exception.DSOutOfServiceException;
import ode.gateway.model.SearchParameters;
import ode.gateway.model.SearchResultCollection;
import ode.gateway.model.SearchScope;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ode.gateway.model.DataObject;
import ode.gateway.model.DatasetData;
import ode.gateway.model.ExperimenterData;
import ode.gateway.model.GroupData;
import ode.gateway.model.ImageData;
import ode.gateway.model.PlateData;
import ode.gateway.model.ProjectData;
import ode.gateway.model.ScreenData;

public class SearchFacilityTest extends GatewayTest {

    private SecurityContext ctx;
    private GroupData group;
    private ExperimenterData user;

    private ProjectData proj;
    private DatasetData ds;
    private ScreenData screen;
    private PlateData plate;
    private ImageData img;

    @Override
    @BeforeClass(alwaysRun = true)
    protected void setUp() throws Exception {
        super.setUp();
        initData();
        indexData();
    }

    @Test
    public void testSearch() throws DSOutOfServiceException, DSAccessException {
        Set<SearchScope> scope = new HashSet<SearchScope>();
        scope.add(SearchScope.NAME);

        List<Class<? extends DataObject>> types = new ArrayList<Class<? extends DataObject>>();
        types.add(ProjectData.class);
        types.add(DatasetData.class);
        types.add(ScreenData.class);
        types.add(PlateData.class);
        types.add(ImageData.class);

        String query = proj.getName().substring(0, 6) + "*";
        SearchParameters param = new SearchParameters(scope, types, query);
        SearchResultCollection results = searchFacility.search(ctx, param);
        Assert.assertEquals(results.size(), 1);
        Assert.assertEquals(results.iterator().next().getType(),
                ProjectData.class);
        Assert.assertEquals(results.iterator().next().getObjectId(),
                proj.getId());

        query = ds.getName().substring(0, 6) + "*";
        param = new SearchParameters(scope, types, query);
        results = searchFacility.search(ctx, param);
        Assert.assertEquals(results.size(), 1);
        Assert.assertEquals(results.iterator().next().getType(),
                DatasetData.class);
        Assert.assertEquals(results.iterator().next().getObjectId(), ds.getId());

        query = screen.getName().substring(0, 6) + "*";
        param = new SearchParameters(scope, types, query);
        results = searchFacility.search(ctx, param);
        Assert.assertEquals(results.size(), 1);
        Assert.assertEquals(results.iterator().next().getType(),
                ScreenData.class);
        Assert.assertEquals(results.iterator().next().getObjectId(),
                screen.getId());

        query = plate.getName().substring(0, 6) + "*";
        param = new SearchParameters(scope, types, query);
        results = searchFacility.search(ctx, param);
        Assert.assertEquals(results.size(), 1);
        Assert.assertEquals(results.iterator().next().getType(),
                PlateData.class);
        Assert.assertEquals(results.iterator().next().getObjectId(),
                plate.getId());

        query = img.getName().substring(0, 6) + "*";
        param = new SearchParameters(scope, types, query);
        results = searchFacility.search(ctx, param);
        Assert.assertEquals(results.size(), 1);
        Assert.assertEquals(results.iterator().next().getType(),
                ImageData.class);
        Assert.assertEquals(results.iterator().next().getObjectId(),
                img.getId());

    }

    private void initData() throws Exception {
        this.group = createGroup();
        this.user = createExperimenter(group);

        ctx = new SecurityContext(group.getId());
        ctx.setExperimenter(user);
        ctx.sudo();

        this.proj = createProject(ctx);
        this.ds = createDataset(ctx, proj);
        this.screen = createScreen(ctx);
        this.plate = createPlate(ctx, screen);
        this.img = createImage(ctx, ds);
    }

    private void indexData() throws DSOutOfServiceException, ServerError {
        final IUpdatePrx iUpdate = gw.getUpdateService(rootCtx);
        for (final DataObject obj : new DataObject[] {proj, ds, screen, plate, img}) {
            iUpdate.indexObject(obj.asIObject());
        }
    }
}
