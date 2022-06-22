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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ode.RLong;
import ode.ServerError;
import ode.api.IAdminPrx;
import ode.api.IQueryPrx;
import ode.api.IUpdatePrx;
import ode.api.ServiceFactoryPrx;
import ode.model.CommentAnnotation;
import ode.model.CommentAnnotationI;
import ode.model.Dataset;
import ode.model.DatasetAnnotationLink;
import ode.model.DatasetAnnotationLinkI;
import ode.model.DatasetI;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.Experimenter;
import ode.model.ExperimenterGroup;
import ode.model.ExperimenterGroupI;
import ode.model.ExperimenterI;
import ode.model.IObject;
import ode.model.Image;
import ode.model.ImageAnnotationLink;
import ode.model.ImageAnnotationLinkI;
import ode.model.ImageI;
import ode.model.Project;
import ode.model.ProjectDatasetLink;
import ode.model.ProjectDatasetLinkI;
import ode.model.ProjectI;
import ode.sys.EventContext;
import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;

/**
 * setUp and tearDown must be called properly to make these work. Copied from
 * testing/src/ode/testing/CreatePojosFixture for ticket1106
 */
public class CreatePojosFixture2 {

    private static final String TESTER_PASS = "ode";

    private static final String USER_GROUP = "user";

    /**
     * creates a new fixture logged in as a newly created user. requires an
     * admin service factory in order to create user and should NOT be used from
     * the server side.
     *
     * @throws ServerError
     * @throws PermissionDeniedException
     * @throws CannotCreateSessionException
     */
    public static CreatePojosFixture2 withNewUser(ode.client root)
            throws ServerError, CannotCreateSessionException,
            PermissionDeniedException {
        return withNewUser(root, (String) null);
    }

    public static CreatePojosFixture2 withNewUser(ode.client root,
            String groupName) throws ServerError, CannotCreateSessionException,
            PermissionDeniedException {
        CreatePojosFixture2 fixture = new CreatePojosFixture2();

        ServiceFactoryPrx sf = root.getSession();
        IAdminPrx rootAdmin = sf.getAdminService();

        String G_NAME;
        if (groupName == null) {
            G_NAME = UUID.randomUUID().toString();
            fixture.g = new ExperimenterGroupI();
            fixture.g.setName(ode.rtypes.rstring(G_NAME));
            fixture.g.setLdap(ode.rtypes.rbool(false));
            fixture.g = new ExperimenterGroupI(
                    rootAdmin.createGroup(fixture.g), false);
        } else {
            G_NAME = groupName;
            fixture.g = rootAdmin.lookupGroup(groupName);
        }

        fixture.TESTER = "TESTER-" + UUID.randomUUID().toString();
        fixture.e = new ExperimenterI();
        fixture.e.setOdeName(ode.rtypes.rstring(fixture.TESTER));
        fixture.e.setFirstName(ode.rtypes.rstring("Mr."));
        fixture.e.setLastName(ode.rtypes.rstring("Allen"));
        fixture.e.setLdap(ode.rtypes.rbool(false));

        ExperimenterGroup userGroup = rootAdmin.lookupGroup(USER_GROUP);
        List<ExperimenterGroup> groups = new ArrayList<ExperimenterGroup>();
        groups.add(fixture.g);
        fixture.e = new ExperimenterI(
                rootAdmin.createExperimenterWithPassword(fixture.e,
                        ode.rtypes.rstring(TESTER_PASS), userGroup, groups),
                false);

        java.util.Map<String, String> m = new HashMap<String, String>();
        m.put("ode.user", fixture.TESTER);
        m.put("ode.pass", TESTER_PASS);
        m.put("ode.group", G_NAME);
        m.put("Ice.Default.Router", root.getProperty("Ice.Default.Router"));
        ode.client client = new ode.client(m);
        ServiceFactoryPrx factory = client.createSession();
        fixture.setServices(factory);

        fixture.init = true;

        return fixture;
    }

    private CreatePojosFixture2() {
    }

    /**
     * requires an admin service factory in order to create user.
     *
     * @throws ServerError
     */
    public CreatePojosFixture2(ServiceFactoryPrx factory) throws ServerError {
        setServices(factory);
        EventContext ec = iAdmin.getEventContext();
        e = new ExperimenterI(ec.userId, false);
        g = new ExperimenterGroupI(ec.groupId, false);
        TESTER = ec.userName;
        init = true;
    }

    private void setServices(ServiceFactoryPrx factory) throws ServerError {
        iAdmin = factory.getAdminService();
        iQuery = factory.getQueryService();
        iUpdate = factory.getUpdateService();
    }

    public IAdminPrx iAdmin;

    public IQueryPrx iQuery;

    public IUpdatePrx iUpdate;

    protected boolean init = false;

    protected List<IObject> toAdd = new ArrayList<IObject>(),
            needId = new ArrayList<IObject>();

    public void createAllPojos() throws Exception {
        init();
        projects();
        datasets();
        pdlinks();
        images();
        dilinks();
        annotations();
    }

    public void init() {
        if (!init) {
            // moved to ctor
        }
    }

    public void pdi() throws Exception {
        projects();
        datasets();
        images();
        pdlinks();
        dilinks();
    }

    public void projects() throws Exception {
        init();
        pr9090 = project(null, "root project without links");
        pr9091 = project(null, "root project with own annotations");
        pr9092 = project(null, "root project with foreign annotations");
        pu9990 = project(e, "user project without links");
        pu9991 = project(e, "user project with own annotations");
        pu9992 = project(e, "user project with foreign annotations");
        saveAndClear();
    }

    public void datasets() throws Exception {
        init();
        dr7070 = dataset(null, "root dataset without links");
        dr7071 = dataset(null, "root dataset with own annotations");
        dr7072 = dataset(null, "root dataset with foreign annotations");
        du7770 = dataset(e, "user dataset without links");
        du7771 = dataset(e, "user dataset with own annotations");
        du7772 = dataset(e, "user dataset with foreign annotations");
        saveAndClear();
    }

    // TODO we aren't passing in Experimenter here
    public void pdlinks() throws Exception {
        init();
        pdlink(pr9091, dr7071);
        pdlink(pr9092, dr7071);
        pdlink(pr9091, dr7072);
        pdlink(pr9092, dr7072);

        pdlink(pu9991, du7771);
        pdlink(pu9992, du7771);
        pdlink(pu9991, du7772);
        pdlink(pu9992, du7772);
        saveAndClear();
    }

    public void images() throws Exception {
        init();
        ir5050 = image(null, "");
        ir5051 = image(null, "");
        ir5052 = image(null, "");
        iu5550 = image(e, "");
        iu5551 = image(e, "");
        iu5552 = image(e, "");
        // cgcpaths
        iu5580 = image(e, "");
        iu5581 = image(e, "");
        iu5582 = image(e, "");
        iu5583 = image(e, "");
        iu5584 = image(e, "");
        iu5585 = image(e, "");
        iu5586 = image(e, "");
        iu5587 = image(e, "");
        iu5588 = image(e, "");
        saveAndClear();
    }

    public void dilinks() throws Exception {
        init();
        dilink(null, dr7071, ir5051);
        dilink(null, dr7071, ir5052);
        dilink(e, dr7072, ir5051);
        dilink(e, dr7072, ir5052);

        dilink(null, du7771, iu5551);
        dilink(null, du7771, iu5552);
        dilink(e, du7772, iu5551);
        dilink(e, du7772, iu5552);
        saveAndClear();
    }

    public void annotations() throws Exception {
        init();
        datasetann(null, dr7071, "roots annotation");
        datasetann(e, dr7072, "users annotation");
        datasetann(null, du7771, "roots annotation");
        datasetann(e, du7772, "users annotation");

        imageann(null, ir5051, "roots annotation");
        imageann(e, ir5052, "users annotation");
        imageann(null, iu5551, "roots annotation");
        imageann(e, iu5552, "users annotation");
        saveAndClear();
    }

    // ~ Helpers
    // =========================================================================

    protected <T extends IObject> T push(T obj) throws Exception {
        toAdd.add(obj);
        T copy = (T) obj.getClass().newInstance();
        copy.unload();
        needId.add(copy);
        return copy;
    }

    protected void saveAndClear() throws ServerError {
        List<IObject> retVal = iUpdate.saveAndReturnArray(toAdd);
        IObject[] unloaded = needId.toArray(new IObject[needId.size()]);
        for (int i = 0; i < retVal.size(); i++) {
            unloaded[i].setId(retVal.get(i).getId());
        }
        toAdd.clear();
        needId.clear();
    }

    protected Project project(Experimenter owner, String name) throws Exception {
        Project p = new ProjectI();
        p.getDetails().setOwner(owner);
        p.setName(ode.rtypes.rstring(name));
        p = push(p);
        return p;
    }

    protected Dataset dataset(Experimenter owner, String name) throws Exception {
        Dataset d = new DatasetI();
        d.getDetails().setOwner(owner);
        d.setName(ode.rtypes.rstring(name));
        d = push(d);
        return d;
    }

    protected ProjectDatasetLink pdlink(Project prj, Dataset ds)
            throws Exception {
        ProjectDatasetLink link = new ProjectDatasetLinkI();
        link.link(prj, ds);
        link = push(link);
        return link;
    }

    protected Image image(Experimenter e, String name) throws Exception {
        Image i = new ImageI();
        i.getDetails().setOwner(e);
        i.setName(ode.rtypes.rstring(name));
        i = push(i);
        return i;
    }

    protected DatasetImageLink dilink(Experimenter user, Dataset ds, Image i)
            throws Exception {
        DatasetImageLink link = new DatasetImageLinkI();
        link.link(ds, i);
        link.getDetails().setOwner(user);
        link = push(link);
        return link;
    }

    protected DatasetAnnotationLink datasetann(Experimenter user, Dataset d,
            String name) throws Exception {
        CommentAnnotation dann = new CommentAnnotationI();
        dann.setNs(ode.rtypes.rstring(name));
        dann.getDetails().setOwner(user);
        DatasetAnnotationLink link = new DatasetAnnotationLinkI();
        link.link((Dataset) d.proxy(), dann);
        link = push(link);
        return link;
    }

    protected ImageAnnotationLink imageann(Experimenter user, Image i,
            String name) throws Exception {
        CommentAnnotation iann = new CommentAnnotationI();
        iann.setNs(ode.rtypes.rstring(name));
        iann.getDetails().setOwner(user);
        ImageAnnotationLink link = new ImageAnnotationLinkI();
        link.link((Image) i.proxy(), iann);
        link = push(link);
        return link;
    }

    // static class Data {
    public String TESTER;

    public Experimenter e;

    public ExperimenterGroup g;

    public Project pr9090, pr9091, pr9092, pu9990, pu9991, pu9992;

    public Dataset dr7070, dr7071, dr7072, du7770, du7771, du7772;

    public Image ir5050, ir5051, ir5052, iu5550, iu5551, iu5552, iu5580,
            iu5581, iu5582, iu5583, iu5584, iu5585, iu5586, iu5587, iu5588;

    // }

    public List<RLong> asIdList(IObject... iobjs) {
        List<RLong> list = new ArrayList<RLong>();
        for (IObject i : iobjs) {
            list.add(i.getId());
        }
        return list;
    }

    /**
     * Returns the collections of project's identifiers.
     *
     * @return See above.
     */
    public List<Long> getProjectIds() {
        List<Long> ids = new ArrayList<Long>();
        if (pr9090 != null)
            ids.add(pr9090.getId().getValue());
        if (pr9091 != null)
            ids.add(pr9091.getId().getValue());
        if (pr9092 != null)
            ids.add(pr9092.getId().getValue());
        if (pu9990 != null)
            ids.add(pu9990.getId().getValue());
        if (pu9991 != null)
            ids.add(pu9991.getId().getValue());
        if (pu9992 != null)
            ids.add(pu9992.getId().getValue());
        return ids;
    }

    /**
     * Returns the collections of project's identifiers.
     *
     * @return See above.
     */
    public List<Long> getImageIds() {
        List<Long> ids = new ArrayList<Long>();
        if (ir5050 != null)
            ids.add(ir5050.getId().getValue());
        if (ir5051 != null)
            ids.add(ir5051.getId().getValue());
        if (ir5052 != null)
            ids.add(ir5052.getId().getValue());
        if (iu5550 != null)
            ids.add(iu5550.getId().getValue());
        if (iu5551 != null)
            ids.add(iu5551.getId().getValue());
        if (iu5552 != null)
            ids.add(iu5552.getId().getValue());
        if (iu5580 != null)
            ids.add(iu5580.getId().getValue());
        if (iu5581 != null)
            ids.add(iu5581.getId().getValue());
        if (iu5582 != null)
            ids.add(iu5582.getId().getValue());
        if (iu5583 != null)
            ids.add(iu5583.getId().getValue());
        if (iu5584 != null)
            ids.add(iu5584.getId().getValue());
        if (iu5585 != null)
            ids.add(iu5585.getId().getValue());
        if (iu5586 != null)
            ids.add(iu5586.getId().getValue());
        if (iu5587 != null)
            ids.add(iu5587.getId().getValue());
        if (iu5588 != null)
            ids.add(iu5588.getId().getValue());
        return ids;
    }

    /**
     * Returns the collections of project's identifiers.
     *
     * @return See above.
     */
    public List<Long> getDatasetIds() {
        List<Long> ids = new ArrayList<Long>();
        if (dr7070 != null)
            ids.add(dr7070.getId().getValue());
        if (dr7071 != null)
            ids.add(dr7071.getId().getValue());
        if (dr7072 != null)
            ids.add(dr7072.getId().getValue());
        if (du7770 != null)
            ids.add(du7770.getId().getValue());
        if (du7771 != null)
            ids.add(du7771.getId().getValue());
        if (du7772 != null)
            ids.add(du7772.getId().getValue());
        return ids;
    }

}
