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

import ode.conditions.SecurityViolation;

import ode.ServerError;
import ode.api.ServiceFactoryPrx;
import ode.model.Dataset;
import ode.model.DatasetI;
import ode.model.PermissionsI;
import ode.model.Project;
import ode.model.ProjectI;
import ode.model.ProjectDatasetLink;
import ode.model.ProjectDatasetLinkI;
import ode.sys.ParametersI;
import ode.model.Pixels;
import ode.model.Thumbnail;
import ode.model.Instrument;
import ode.model.Image;

import static ode.rtypes.rstring;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled=false, groups = { "broken", "ticket:200", "security", "integration" })
public class ReadSecurityTest extends AbstractPermissionsTest {
    /**
     * due to permission restrictions, certain user combinations will not be
     * able to even create the needed test instances. this provides a check for
     * that.
     */
    boolean canCreate;

    // ~ single
    // =========================================================================

    @Override
    public void testSingleProject_U() throws Exception {
        ownsfA = u;
        ownerA = user;
        groupA = user_other_group;

        // RW_xx_xx : should not be readable by anyone but user.
        permsA = new PermissionsI(RW_xx_xx.toString());
        canCreate = true;
        single(u, true);
        single(o, false);
        single(w, false);
        single(p, true);
        single(r, true);

        // RW_RW_xx : now let's up the readability
        permsA = new PermissionsI(RW_RW_xx.toString());
        canCreate = true;
        single(u, true);
        single(o, true);
        single(w, false);
        single(p, true);
        single(r, true);

        // RW_RW_RW : now let's up the readability one more time
        permsA = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        single(u, true);
        single(o, true);
        single(w, true);
        single(p, true);
        single(r, true);

        // xx_xx_xx : and if we make it invisible
        permsA = new PermissionsI(xx_xx_xx.toString());
        canCreate = true;
        single(u, false);
        single(o, false);
        single(w, false);
        single(p, true);
        single(r, true);
    }

    // don't need to test for OTHER because OTHER and USER are symmetric.

    @Override
    public void testSingleProject_W() throws Exception {
        ownsfA = w;
        ownerA = world;
        groupA = common_group;

        // RW_xx_xx : should not be readable by anyone but world.
        permsA = new PermissionsI(RW_xx_xx.toString());
        canCreate = true;
        single(u, false);
        single(o, false);
        single(w, true);
        single(p, false);
        single(r, true);

        // RW_RW_xx : now let's up the readability
        permsA = new PermissionsI(RW_RW_xx.toString());
        canCreate = true;
        single(u, true);
        single(o, true);
        single(w, true);
        single(p, true);
        single(r, true);

        // RW_RW_RW : now let's up the readability one more time
        permsA = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        single(u, true);
        single(o, true);
        single(w, true);
        single(p, true);
        single(r, true);

        // xx_xx_xx : and if we make it invisible
        permsA = new PermissionsI(xx_xx_xx.toString());
        canCreate = true;
        single(u, false);
        single(o, false);
        single(w, false);
        single(p, false);
        single(r, true);

    }

    // don't need to test PI because acts just like a group member

    @Override
    public void testSingleProject_R() throws Exception {
        ownsfA = r;
        ownerA = root;
        groupA = system_group;

        // RW_xx_xx : should not be readable by anyone but world.
        permsA = new PermissionsI(RW_xx_xx.toString());
        canCreate = true;
        single(u, false);
        single(o, false);
        single(w, false);
        single(p, false);
        single(r, true);

        // RW_RW_xx : now let's up the readability
        permsA = new PermissionsI(RW_RW_xx.toString());
        canCreate = true;
        single(u, false);
        single(o, false);
        single(w, false);
        single(p, false);
        single(r, true);

        // RW_RW_RW : now let's up the readability one more time
        permsA = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        single(u, true);
        single(o, true);
        single(w, true);
        single(p, true);
        single(r, true);

        // xx_xx_xx : and if we make it invisible
        permsA = new PermissionsI(xx_xx_xx.toString());
        canCreate = true;
        single(u, false);
        single(o, false);
        single(w, false);
        single(p, false);
        single(r, true);

    }

    /**
     * performs various types of Bhojpur ODE lookups for a particular user proxied by
     * a {@link ServiceFactory}. If ok is true, then the lookups should
     * succeed.
     * @throws ServerError 
     */
    protected void single(ServiceFactoryPrx sf, boolean ok) throws ServerError {
        createProject(ownsfA, permsA, groupA);
        verifyDetails(prj, ownerA, groupA, permsA);

        Project t;
        try {
            t = (Project) sf.getQueryService().find("Project", prj.getId().getValue());
            Assert.assertNotNull(t);
        } catch (SecurityViolation e) {
            if (ok) {
                throw e;
            }
        }

        String q = "select p from Project p where p.id = :id";
        ParametersI param = new ParametersI().addId(prj.getId());

        t = (Project) sf.getQueryService().findByQuery(q, param);
        if (ok) {
            Assert.assertNotNull(t);
        } else {
            Assert.assertNull(t);
        }

    }

    // ~ bidirectional one-to-many
    // =========================================================================

    @Override
    public void test_U_Pixels_And_U_Thumbnails() throws Exception {
        ownsfA = ownsfB = u;
        ownerA = ownerB = user;
        groupA = groupB = user_other_group;

        // RW_RW_RW / RW_RW_RW
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, true, true);
        oneToMany(w, true, true);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_RW_RW / RW_RW_xx : now lets lower visibility
        // thumbnail readable by other but not by world
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_xx.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, true, true);
        oneToMany(w, true, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_RW_RW / RW_xx_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_xx_xx.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, true, false);
        oneToMany(w, true, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_RW_RW / xx_xx_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(xx_xx_xx.toString());
        canCreate = true;
        oneToMany(u, true, false);
        oneToMany(o, true, false);
        oneToMany(w, true, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_RW_xx / RW_RW_xx
        permsA = new PermissionsI(RW_RW_xx.toString());
        permsB = new PermissionsI(RW_RW_xx.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, true, true);
        oneToMany(w, false, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_xx_xx / RW_xx_xx
        permsA = new PermissionsI(RW_xx_xx.toString());
        permsB = new PermissionsI(RW_xx_xx.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, false, false);
        oneToMany(w, false, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // xx_xx_xx / xx_xx_xx
        permsA = new PermissionsI(xx_xx_xx.toString());
        permsB = new PermissionsI(xx_xx_xx.toString());
        canCreate = false;
        oneToMany(u, false, false);
        oneToMany(o, false, false);
        oneToMany(w, false, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

    }

    @Override
    public void test_O_Pixels_And_U_Thumbnails() throws Exception {
        ownsfA = o;
        ownerA = other;
        groupA = user_other_group;

        ownsfB = u;
        ownerB = user;
        groupB = user_other_group;

        // RW_RW_RW / RW_RW_RW
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, true, true);
        oneToMany(w, true, true);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_RW_RW / RW_RW_xx : now lets lower visibility
        // thumbnail readable by other but not by world
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_xx.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, true, true);
        oneToMany(w, true, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_RW_RW / RW_xx_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_xx_xx.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, true, false);
        oneToMany(w, true, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_RW_RW / xx_xx_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(xx_xx_xx.toString());
        canCreate = true;
        oneToMany(u, true, false);
        oneToMany(o, true, false);
        oneToMany(w, true, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_xx_xx / RW_xx_xx
        permsA = new PermissionsI(RW_xx_xx.toString());
        permsB = new PermissionsI(RW_xx_xx.toString());
        canCreate = false;
        oneToMany(u, false, false);
        oneToMany(o, true, false);
        oneToMany(w, false, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // xx_xx_xx / xx_xx_xx
        permsA = new PermissionsI(xx_xx_xx.toString());
        permsB = new PermissionsI(xx_xx_xx.toString());
        canCreate = false;
        oneToMany(u, false, false);
        oneToMany(o, false, false);
        oneToMany(w, false, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

    }

    @Override
    public void test_U_Pixels_And_O_Thumbnails() throws Exception {
        ownsfA = u;
        ownerA = user;
        groupA = user_other_group;

        ownsfB = o;
        ownerB = other;
        groupB = user_other_group;

        // RW_RW_RW / RW_RW_RW
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, true, true);
        oneToMany(w, true, true);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_RW_RW / RW_RW_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_xx.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, true, true);
        oneToMany(w, true, false);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // xx_xx_xx / RW_RW_RW
        permsA = new PermissionsI(xx_xx_xx.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = false;
        oneToMany(u, false, true);
        oneToMany(o, false, true);
        oneToMany(w, false, true);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

    }

    @Override
    public void test_U_Pixels_And_R_Thumbnails() throws Exception {
        ownsfA = u;
        ownerA = user;
        groupA = user_other_group;

        ownsfB = r;
        ownerB = root;
        groupB = system_group;

        // RW_RW_RW / RW_RW_RW
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        oneToMany(u, true, true);
        oneToMany(o, true, true);
        oneToMany(w, true, true);
        oneToMany(p, true, true);
        oneToMany(r, true, true);

        // RW_RW_RW / RW_RW_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_xx.toString());
        canCreate = true;
        oneToMany(u, true, false);
        oneToMany(o, true, false);
        oneToMany(w, true, false);
        oneToMany(p, true, false);
        oneToMany(r, true, true);

        // RW_RW_RW / RW_xx_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_xx_xx.toString());
        canCreate = true;
        oneToMany(u, true, false);
        oneToMany(o, true, false);
        oneToMany(w, true, false);
        oneToMany(p, true, false);
        oneToMany(r, true, true);

        // RW_xx_xx / RW_xx_xx
        permsA = new PermissionsI(RW_xx_xx.toString());
        permsB = new PermissionsI(RW_xx_xx.toString());
        canCreate = true;
        oneToMany(u, true, false);
        oneToMany(o, false, false);
        oneToMany(w, false, false);
        oneToMany(p, true, false);
        oneToMany(r, true, true);

        // xx_xx_xx / xx_xx_xx
        permsA = new PermissionsI(xx_xx_xx.toString());
        permsB = new PermissionsI(xx_xx_xx.toString());
        canCreate = true;
        oneToMany(u, false, false);
        oneToMany(o, false, false);
        oneToMany(w, false, false);
        oneToMany(p, true, false);
        oneToMany(r, true, true);

    }

    /**
     * performs various types of Bhojpur ODE lookups for a particular user proxied by
     * a {@link ServiceFactory}. If pix_ok is true, then the lookups should
     * succeed for the top-level pixel, and if tb_ok is true, then that pixel
     * should contain a single thumbnail.
     * @throws ServerError 
     */
    protected void oneToMany(ServiceFactoryPrx sf, boolean pix_ok, boolean tb_ok) throws Exception {
        createPixels(ownsfA, groupA, permsA);
        verifyDetails(pix, ownerA, groupA, permsA);

        try {
            createThumbnail(ownsfB, groupB, permsB, pix);
            verifyDetails(tb, ownerB, groupB, permsB);
            if (!canCreate) {
                Assert.fail("secvio!");
            }

            pix = tb.getPixels();

            String outerJoin = "select p from Pixels p left outer join fetch p.thumbnails where p.id = :id";
            String innerJoin = "select p from Pixels p join fetch p.thumbnails where p.id = :id";
            ParametersI params = new ParametersI().addId(pix.getId());

            Pixels test = (Pixels) sf.getQueryService().findByQuery(outerJoin, params);
            if (pix_ok) {
                Assert.assertNotNull(test);
                if (tb_ok) {
                    Assert.assertTrue(test.sizeOfThumbnails() > 0);
                } else {
                    Assert.assertTrue(test.sizeOfThumbnails() == 0); // TODO should
                    // it be null?
                }

            } else {
                Assert.assertNull(test);
            }

            outerJoin = "select t from Thumbnail t left outer join fetch t.pixels where t.id = :id";
            innerJoin = "select t from Thumbnail t join fetch t.pixels where t.id = :id";
            params = new ParametersI().addId(tb.getId());

            Thumbnail test2 = (Thumbnail) sf.getQueryService().findByQuery(outerJoin, params);
            if (tb_ok) {
                Assert.assertNotNull(test2);
                if (pix_ok) {
                    Assert.assertNotNull(test2.getPixels());
                } else {
                    Assert.fail("should not be possible (null)");
                }

            } else {
                Assert.assertNull(test2);
            }

        } catch (SecurityViolation sv) {
            if (canCreate) {
                throw sv;
            }
        }
    }

    // ~ many-to-one
    // =========================================================================
    public void test_U_Thumbnails_And_U_Pixels() throws Exception {
        ownsfA = ownsfB = u;
        ownerA = ownerB = user;
        groupA = groupB = user_other_group;

        // RW_RW_RW / RW_RW_RW : readable by all
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        manyToOne(u, true, true);
        manyToOne(o, true, true);
        manyToOne(w, true, true);
        manyToOne(p, true, true);
        manyToOne(r, true, true);

        // RW_RW_xx / RW_RW_RW
        permsA = new PermissionsI(RW_RW_xx.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        manyToOne(u, true, true);
        manyToOne(o, true, true);
        manyToOne(w, false, false);
        manyToOne(p, true, true);
        manyToOne(r, true, true);

        // RW_xx_xx / RW_RW_RW
        permsA = new PermissionsI(RW_xx_xx.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        manyToOne(u, true, true);
        manyToOne(o, false, true);
        manyToOne(w, false, false);
        manyToOne(p, true, true);
        manyToOne(r, true, true);

        // RW_xx_xx / RW_xx_xx
        permsA = new PermissionsI(RW_xx_xx.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        manyToOne(u, true, true);
        manyToOne(o, false, true);
        manyToOne(w, false, false);
        manyToOne(p, true, true);
        manyToOne(r, true, true);

        // RW_RW_RW / xx_xx_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(xx_xx_xx.toString());
        canCreate = false;
        manyToOne(u, true, false);
        manyToOne(o, true, false);
        manyToOne(w, true, false);
        manyToOne(p, true, true);
        manyToOne(r, true, true);

    }

    public void test_O_Thumbnails_And_U_Pixels() throws Exception {
        ownsfA = o;
        ownerA = other;
        groupA = user_other_group;

        ownsfB = u;
        ownerB = user;
        groupB = user_other_group;

        // RW_RW_RW / RW_RW_RW
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        manyToOne(u, true, true);
        manyToOne(o, true, true);
        manyToOne(w, true, true);
        manyToOne(p, true, true);
        manyToOne(r, true, true);

        // RW_RW_xx / RW_RW_RW
        permsA = new PermissionsI(RW_RW_xx.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        manyToOne(u, true, true);
        manyToOne(o, true, true);
        manyToOne(w, false, false);
        manyToOne(p, true, true);
        manyToOne(r, true, true);

        // RW_RW_RW / xx_xx_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(xx_xx_xx.toString());
        canCreate = false;
        manyToOne(u, true, false);
        manyToOne(o, true, false);
        manyToOne(w, true, false);
        manyToOne(p, true, true);
        manyToOne(r, true, true);

    }

    /**
     * performs various types of Bhojpur ODE lookups for a particular user proxied by
     * a {@link ServiceFactory}. If pix_ok is true, then the lookups should
     * succeed for the top-level pixel, and if tb_ok is true, then that pixel
     * should contain a single thumbnail.
     * @throws ServerError 
     */
    protected void manyToOne(ServiceFactoryPrx sf, boolean tb_ok, boolean pix_ok) throws Exception {
        createPixels(ownsfB, groupB, permsB);
        verifyDetails(pix, ownerB, groupB, permsB);

        try {

            createThumbnail(ownsfA, groupA, permsA, pix);
            verifyDetails(tb, ownerA, groupA, permsA);
            if (!canCreate) {
                Assert.fail("secvio!");
            }

            pix = tb.getPixels();

            String outerJoin = "select t from Thumbnail t left outer join fetch t.pixels where t.id = :id";
            String innerJoin = "select t from Thumbnail t join fetch t.pixels where t.id = :id";
            ParametersI params = new ParametersI().addId(tb.getId());

            try {
                Thumbnail test = (Thumbnail) sf.getQueryService().findByQuery(outerJoin, params);
                if (tb_ok) {
                    Assert.assertNotNull(test);
                    if (pix_ok) {
                        Assert.assertNotNull(test.getPixels());
                    } else {
                        Assert.assertNull(test.getPixels()); // TODO should it be
                        // null?
                    }

                } else {
                    Assert.assertNull(test);
                }
            } catch (SecurityViolation sv) {
                if (tb_ok && pix_ok) {
                    throw sv;
                }
            }

        } catch (SecurityViolation sv) {
            if (canCreate) {
                throw sv;
            }
        }
    }

    // ~ unidirectional many-to-one
    // =========================================================================
    @Override
    public void test_U_Instrument_And_U_Microscope() throws Exception {
        ownsfA = ownsfB = u;
        ownerA = ownerB = user;
        groupA = groupB = user_other_group;

        // RW_RW_RW / RW_RW_RW
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        uniManyOne(u, true, true);

    }

    protected void uniManyOne(ServiceFactoryPrx sf, boolean instr_ok,
            boolean micro_ok) throws ServerError {

        createMicroscope(ownsfB, groupB, permsB);
        verifyDetails(micro, ownerB, groupB, permsB);

        try {
            createInstrument(ownsfA, groupA, permsA, micro);
            verifyDetails(instr, ownerA, groupA, permsA);
            if (!canCreate) {
                Assert.fail("secvio!");
            }
        } catch (SecurityViolation sv) {
            if (canCreate) {
                throw sv;
            }
        }

        String outerJoin = "select i from Instrument i left outer join fetch i.microscope where i.id = :id";
        String innerJoin = "select p from Instrument i join fetch i.microscope where i.id = :id";
        ParametersI params = new ParametersI().addId(instr.getId());

        Instrument test = (Instrument) sf.getQueryService().findByQuery(outerJoin, params);
        if (instr_ok) {
            Assert.assertNotNull(test);
            if (micro_ok) {
                Assert.assertNotNull(test.getMicroscope());
            } else {
                Assert.fail("should not be possibe (null)");
            }
        } else {
            Assert.assertNull(test);
        }
    }

    // ~ many-to-many
    // =========================================================================

    @Override
    public void test_U_Projects_U_Datasets_U_Link() throws Exception {
        ownsfA = ownsfB = ownsfC = u;
        ownerA = ownerB = ownerC = user;
        groupA = groupB = groupC = user_other_group;

        // RW_RW_RW / RW_RW_RW / RW_RW_RW
        permsA = permsB = permsC = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        manyToMany(u, true, true);
        manyToMany(o, true, true);
        manyToMany(w, true, true);
        manyToMany(p, true, true);
        manyToMany(r, true, true);

        // RW_RW_RW / RW_RW_xx / RW_RW_RW
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_xx.toString());
        permsC = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        manyToMany(u, true, true);
        manyToMany(o, true, true);
        manyToMany(w, true, false);
        manyToMany(p, true, true);
        manyToMany(r, true, true);

        // RW_RW_RW / RW_xx_xx / RW_RW_RW
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_xx_xx.toString());
        permsC = new PermissionsI(RW_RW_RW.toString());
        canCreate = true;
        manyToMany(u, true, true);
        manyToMany(o, true, false);
        manyToMany(w, true, false);
        manyToMany(p, true, true);
        manyToMany(r, true, true);

    }

    /**
     * performs various types of Bhojpur ODE lookups for a particular user proxied by
     * a {@link ServiceFactory}. If prj_ok is true, then the lookups should
     * succeed for the top-level project, and if ds_ok is true, then that
     * project should contain a single linked dataset.
     * @throws ServerError 
     */
    protected void manyToMany(ServiceFactoryPrx sf, boolean prj_ok, boolean ds_ok) throws ServerError {

        prj = new ProjectI();
        prj.setName(rstring("links"));
        prj.getDetails().setPermissions(permsA);
        prj.getDetails().setGroup(groupA);

        Dataset ds = new DatasetI();
        ds.setName(rstring("links"));
        ds.getDetails().setPermissions(permsB);
        ds.getDetails().setGroup(groupB);

        prj = (Project) ownsfA.getUpdateService().saveAndReturnObject(prj);
        ds = (Dataset) ownsfB.getUpdateService().saveAndReturnObject(ds);
        link = new ProjectDatasetLinkI();
        link.link(prj, ds);
        link.getDetails().setPermissions(permsC);
        link.getDetails().setGroup(groupC);
        link = (ProjectDatasetLink) ownsfC.getUpdateService().saveAndReturnObject(link);

        // RW_RW_RW / RW_RW_RW
        verifyDetails(prj, ownerA, groupA, permsA);
        verifyDetails(ds, ownerB, groupB, permsB);
        verifyDetails(link, ownerC, groupC, permsC);

        String outerJoin = "select p from Project p "
                + " left outer join fetch p.datasetLinks pdl "
                + " left outer join fetch pdl.child ds " + " where p.id = :id";
        ParametersI params = new ParametersI().addId(prj.getId());

        try {
            Project test = (Project) sf.getQueryService().findByQuery(outerJoin, params);
            if (prj_ok) {
                Assert.assertNotNull(test);
                if (ds_ok) {
                    Assert.assertNotNull(test.linkedDatasetList().size() == 1);
                } else {
                    Assert.assertTrue(test.linkedDatasetList().size() == 0); // TODO
                    // should
                    // it be
                    // null?
                }

            } else {
                Assert.assertNull(test);
            }
        } catch (SecurityViolation sv) {
            if (prj_ok && ds_ok) {
                throw sv;
            }
        }

    }

    // ~ Special: "tag" (e.g. Image/Pixels)
    // =========================================================================

    @Override
    @Test(enabled=false)
    public void test_U_Image_U_Pixels() throws Exception {
        ownsfA = ownsfB = u;
        ownerA = ownerB = user;
        groupA = groupB = user_other_group;

        // RW_RW_RW / RW_RW_RW
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_RW.toString());
        imagePixels(u, true, true);
        imagePixels(o, true, true);
        imagePixels(w, true, true);
        imagePixels(p, true, true);
        imagePixels(r, true, true);

        // RW_RW_RW / RW_RW_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_RW_xx.toString());
        imagePixels(u, true, true);
        imagePixels(o, true, true);
        imagePixels(w, true, false);
        imagePixels(p, true, true);
        imagePixels(r, true, true);

        // RW_RW_RW / RW_xx_xx
        permsA = new PermissionsI(RW_RW_RW.toString());
        permsB = new PermissionsI(RW_xx_xx.toString());
        imagePixels(u, true, true);
        imagePixels(o, true, false);
        imagePixels(w, true, false);
        imagePixels(p, true, true);
        imagePixels(r, true, true);

    }

    protected void imagePixels(ServiceFactoryPrx sf, boolean img_ok, boolean pix_ok) throws Exception {
        createPixels(ownsfB, groupB, permsB);
        createImage(ownsfA, groupA, permsA, pix);

        // RW_RW_RW / RW_RW_RW
        verifyDetails(img, ownerA, groupA, permsA);
        verifyDetails(pix, ownerB, groupB, permsB);

        String outerJoin = "select i from Image i "
                + "left outer join fetch i.pixels " + "where i.id = :id";
        ParametersI params = new ParametersI().addId(img.getId());

        Image test = (Image) sf.getQueryService().findByQuery(outerJoin, params);
        if (img_ok) {
            Assert.assertNotNull(test);
            if (pix_ok) {
                Assert.assertNotNull(test.getPrimaryPixels());
                Assert.assertTrue(test.sizeOfPixels() > 0);
            } else {
                Assert.assertTrue(test.sizeOfPixels() == 0); // TODO should it be
                // null?
            }

        } else {
            Assert.assertNull(test);
        }

    }

}