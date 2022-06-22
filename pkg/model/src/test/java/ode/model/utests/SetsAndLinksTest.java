package ode.model.utests;

import java.util.List;

import ode.conditions.ApiUsageException;
import ode.model.containers.Dataset;
import ode.model.containers.Project;
import ode.model.containers.ProjectDatasetLink;
import ode.model.core.Channel;
import ode.model.core.Image;
import ode.model.core.OriginalFile;
import ode.model.core.Pixels;
import ode.model.core.PlaneInfo;
import ode.model.display.Thumbnail;
import ode.model.jobs.ImportJob;
import ode.model.jobs.JobOriginalFileLink;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.model.meta.GroupExperimenterMap;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SetsAndLinksTest {

    Project p;

    Dataset d;

    Image i;

    Pixels pix;

    @BeforeMethod
    protected void setUp() throws Exception {
        p = new Project();
        d = new Dataset();
        i = new Image();
        pix = new Pixels();
    }

    @Test
    public void test_linking() throws Exception {
        p.linkDataset(d);

        Assert.assertEquals(p.linkedDatasetList().size(), 1);
        Assert.assertTrue(p.linkedDatasetIterator().next().equals(d));

    }

    @Test
    public void test_unlinking() throws Exception {
        p.linkDataset(d);
        p.unlinkDataset(d);
        Assert.assertEquals(p.linkedDatasetList().size(), 0);

        p.linkDataset(d);
        p.clearDatasetLinks();
        Assert.assertEquals(p.linkedDatasetList().size(), 0);

    }

    @Test
    public void test_retrieving() throws Exception {
        p.linkDataset(d);
        List<Object> l = p.eachLinkedDataset(null);
        Assert.assertEquals(l.size(), 1);
        Assert.assertTrue(l.get(0).equals(d));
    }

    @Test
    public void test_adding_a_placeholder() throws Exception {
        Project p = new Project();
        Dataset d = new Dataset(new Long(1L), false);

        p.linkDataset(d);
    }

    @Test(groups = "ticket:60")
    public void test_cantLinkNullSet() throws Exception {
        p.putAt(Project.DATASETLINKS, null); // This is a workaround.
        try {
            p.linkDataset(d);
            Assert.fail("Should not be allowed.");
        } catch (ApiUsageException api) {
            // ok.
        }

    }

    @Test(groups = "ticket:60")
    public void test_butWeStillWantToUseUnloadeds() throws Exception {
        d.unload();
        p.linkDataset(d);
    }

    @Test(groups = "ticket:60")
    public void test_andTheReverseToo() throws Exception {
        d.putAt(Dataset.PROJECTLINKS, null); // This is a workaround.
        try {
            p.linkDataset(d);
            Assert.fail("Should not be allowed.");
        } catch (ApiUsageException api) {
            // ok.
        }
    }

    // Default Experimenter Group
    @Test
    public void test_one_way_to_default_link() throws Exception {
        Experimenter experimenter = new Experimenter();
        ExperimenterGroup defaultGroup = new ExperimenterGroup();
        ExperimenterGroup defaultGroup2 = new ExperimenterGroup();

        experimenter.linkExperimenterGroup(defaultGroup);
        testIsDefault(experimenter, defaultGroup);

        GroupExperimenterMap map = experimenter
                .linkExperimenterGroup(defaultGroup2);
        experimenter.setPrimaryGroupExperimenterMap(map);
        testIsDefault(experimenter, defaultGroup2);
    }

    @Test(groups = { "broken", "ticket:346" })
    public void testAddingFillsContainer() throws Exception {
        Pixels p = new Pixels();
        Thumbnail tb = new Thumbnail();
        tb.setPixels(p);
        Assert.assertTrue(p.iterateThumbnails().hasNext());
    }

    @Test(groups = { "broken", "ticket:346" })
    public void testLinkingFillsContainer() throws Exception {
        Project p = new Project();
        Dataset d = new Dataset();
        ProjectDatasetLink link = new ProjectDatasetLink();
        link.link(p, d);
        Assert.assertNotNull(link.parent());
        Assert.assertNotNull(link.child());
        Assert.assertEquals(link.parent().sizeOfDatasetLinks(), 1);
        Assert.assertEquals(link.child().sizeOfProjectLinks(), 1);
    }

    @Test(groups = "jobs")
    public void testUnidirectionalLinks() throws Exception {
        ImportJob job = new ImportJob();
        OriginalFile file = new OriginalFile();
        job.linkOriginalFile(file);
        Assert.assertEquals(job.sizeOfOriginalFileLinks(), 1);
        job.unlinkOriginalFile(file);
        Assert.assertEquals(job.sizeOfOriginalFileLinks(), 0);
        JobOriginalFileLink link = new JobOriginalFileLink();
        link.link(job, file);
        job.addJobOriginalFileLink(link, true);
        Assert.assertEquals(job.sizeOfOriginalFileLinks(), 1);
        job.clearOriginalFileLinks();
        Assert.assertEquals(job.sizeOfOriginalFileLinks(), 0);
    }

    @Test
    public void testOrderedRelationshipsCanHaveUnloadedAdd() {
        Pixels p = new Pixels();
        Channel c = new Channel(1L, false);
        p.addChannel(c);

        PlaneInfo pi = new PlaneInfo(1L, false);
        try {
            p.addPlaneInfo(pi);
            Assert.fail("This should not be accepted.");
        } catch (IllegalStateException ise) {
            // good.
        }
    }
    
    // This is now allowed? (ticket:2067)
    @Test(groups={"broken"}, expectedExceptions = NullPointerException.class)
    public void testNullArentAddableToOrderedCollections() {
        Pixels p = new Pixels();
        p.addChannel(null);
        Assert.assertEquals(p.sizeOfChannels(), 1);
    }

    // ~ Private helpers
    // ===========================================================================
    private void testIsDefault(Experimenter user, ExperimenterGroup group) {
        ExperimenterGroup t = user.getPrimaryGroupExperimenterMap().parent();
        Assert.assertEquals(group, t);
    }

}