package ode.services.server.test;

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

import static ode.rtypes.rstring;

import java.util.List;

import ode.model.annotations.ProjectAnnotationLink;
import ode.parameters.Filter;
import ode.parameters.Parameters;
import ode.ApiUsageException;
import ode.api.AMD_IUpdate_deleteObject;
import ode.model.Annotation;
import ode.model.CommentAnnotationI;
import ode.model.Dataset;
import ode.model.DatasetI;
import ode.model.IObject;
import ode.model.ImageI;
import ode.model.Project;
import ode.model.ProjectI;
import ode.sys.ParametersI;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = "integration")
public class UpdateITest extends AbstractServantTest {

    @Override
    @BeforeMethod
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test(groups = "ticket:1183")
    public void testProjectWithAnnotationCausesError() throws Exception {
        Project p = new ProjectI();
        p.setName(rstring("ticket:1183"));
        p.linkAnnotation(new CommentAnnotationI());
        p = (Project) assertSaveAndReturn(p);
    }

    public void testQueryWithSelect() throws Exception {
        Project p = new ProjectI();
        p.setName(rstring(""));
        Dataset d = new DatasetI();
        d.setName(rstring(""));
        p.linkDataset(d);
        assertSaveAndReturn(p);
        List<IObject> objects = assertFindByQuery(
                "from Project p join fetch p.datasetLinks pdl "
                        + "join fetch pdl.child ", null);
        for (IObject object : objects) {
            assertTrue(object instanceof Project);
        }
    }
    
    @Test(expectedExceptions = ApiUsageException.class)
    public void testNPEOnMissingQuotes() throws Exception {
        Project p = new ProjectI();
        p.setName(rstring(""));
        assertSaveAndReturn(p);
        List<IObject> objects = assertFindByQuery(
                "from Project p where p.name = foo ", null);
        for (IObject object : objects) {
            assertTrue(object instanceof Project);
        }
    }

    @Test(groups = "ticket:1193")
    public void testNullDetails() throws Exception {
        Project prj = new ProjectI();
        prj.setName(rstring("1193"));
        Annotation a = new CommentAnnotationI();
        prj.linkAnnotation(a);
        assertSaveAndReturn(prj);

        String q = "select pal from ProjectAnnotationLink pal join fetch pal.child";
        Parameters param = new Parameters(new Filter().page(0, 2));
        List<ProjectAnnotationLink> pals = user.managedSf.getQueryService()
                .findAllByQuery(q, param);
        assertNotNull(pals.get(0).getDetails());

        ode.sys.Parameters p = new ParametersI().page(0, 2);
        List<IObject> objects = assertFindByQuery(q, p);
        assertNotNull(objects.get(0).getDetails());
    }

    @Test(groups = "ticket:587", expectedExceptions = ApiUsageException.class)
    public void testDeleteImageNotPixels() throws Exception {
        final long iid = makeImage();
        final RV rv = new RV();
        user.update.deleteObject_async(new AMD_IUpdate_deleteObject() {
            public void ice_response() {
                rv.rv = Boolean.TRUE;
            }

            public void ice_exception(Exception ex) {
                rv.ex = ex;
            }
        }, new ImageI(iid, false), current("deleteObject"));
        assertNull(rv.rv);
        assertNotNull(rv.ex);
        throw rv.ex;
    }

}