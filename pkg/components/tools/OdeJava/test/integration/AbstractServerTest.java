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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.IObservable;
import ode.formats.importer.IObserver;
import ode.formats.importer.ImportCandidates;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportContainer;
import ode.formats.importer.ImportEvent;
import ode.formats.importer.ImportLibrary;
import ode.formats.importer.ODEWrapper;
import ode.formats.importer.util.ErrorHandler;
import ode.io.nio.SimpleBackOff;
import ode.services.server.repo.path.FsFile;
import ode.services.scripts.ScriptRepoHelper;
import ode.system.Login;
import ode.ApiUsageException;
import ode.RLong;
import ode.RType;
import ode.SecurityViolation;
import ode.ServerError;
import ode.rtypes;
import ode.api.IAdminPrx;
import ode.api.IPixelsPrx;
import ode.api.IQueryPrx;
import ode.api.IUpdatePrx;
import ode.api.ServiceFactoryPrx;
import ode.cmd.CmdCallbackI;
import ode.cmd.Delete2;
import ode.cmd.Delete2Response;
import ode.cmd.DoAll;
import ode.cmd.DoAllRsp;
import ode.cmd.ERR;
import ode.cmd.HandlePrx;
import ode.cmd.OK;
import ode.cmd.Request;
import ode.cmd.Response;
import ode.cmd.State;
import ode.cmd.Status;
import ode.grid.RawAccessRequest;
import ode.grid.RepositoryMap;
import ode.grid.RepositoryPrx;
import ode.model.Annotation;
import ode.model.BooleanAnnotation;
import ode.model.BooleanAnnotationI;
import ode.model.ChannelBinding;
import ode.model.CommentAnnotation;
import ode.model.CommentAnnotationI;
import ode.model.Dataset;
import ode.model.DatasetAnnotationLink;
import ode.model.DatasetAnnotationLinkI;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.Detector;
import ode.model.DetectorAnnotationLink;
import ode.model.DetectorAnnotationLinkI;
import ode.model.Experiment;
import ode.model.Experimenter;
import ode.model.ExperimenterGroup;
import ode.model.ExperimenterGroupI;
import ode.model.ExperimenterI;
import ode.model.FileAnnotation;
import ode.model.FileAnnotationI;
import ode.model.Fileset;
import ode.model.FilesetI;
import ode.model.Folder;
import ode.model.IObject;
import ode.model.Image;
import ode.model.ImageAnnotationLink;
import ode.model.ImageAnnotationLinkI;
import ode.model.Instrument;
import ode.model.InstrumentAnnotationLink;
import ode.model.InstrumentAnnotationLinkI;
import ode.model.LightSource;
import ode.model.LightSourceAnnotationLink;
import ode.model.LightSourceAnnotationLinkI;
import ode.model.LongAnnotation;
import ode.model.LongAnnotationI;
import ode.model.MapAnnotation;
import ode.model.MapAnnotationI;
import ode.model.NamedValue;
import ode.model.OriginalFile;
import ode.model.OriginalFileAnnotationLink;
import ode.model.OriginalFileAnnotationLinkI;
import ode.model.Permissions;
import ode.model.PermissionsI;
import ode.model.Pixels;
import ode.model.Plate;
import ode.model.PlateAcquisition;
import ode.model.PlateAcquisitionAnnotationLink;
import ode.model.PlateAcquisitionAnnotationLinkI;
import ode.model.PlateAnnotationLink;
import ode.model.PlateAnnotationLinkI;
import ode.model.Project;
import ode.model.ProjectAnnotationLink;
import ode.model.ProjectAnnotationLinkI;
import ode.model.ProjectDatasetLink;
import ode.model.ProjectDatasetLinkI;
import ode.model.QuantumDef;
import ode.model.RenderingDef;
import ode.model.Screen;
import ode.model.ScreenAnnotationLink;
import ode.model.ScreenAnnotationLinkI;
import ode.model.TagAnnotation;
import ode.model.TagAnnotationI;
import ode.model.TermAnnotation;
import ode.model.TermAnnotationI;
import ode.model.Well;
import ode.model.WellAnnotationLink;
import ode.model.WellAnnotationLinkI;
import ode.model.WellSample;
import ode.sys.EventContext;
import ode.sys.Parameters;
import ode.sys.ParametersI;
import ode.sys.Roles;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

/**
 * Base test for integration tests.
 */
public class AbstractServerTest extends AbstractTest {

    /** Performs the move as data owner. */
    public static final int MEMBER = 100;

    /** Performs the move as group owner. */
    public static final int GROUP_OWNER = 101;

    /** Performs the move as group owner. */
    public static final int ADMIN = 102;

    /** Scaling factor used for CmdCallbackI loop timings. */
    protected long scalingFactor;

    /** All groups context to use in cases where errors due to group restriction are to be avoided. */
    protected static final ImmutableMap<String, String> ALL_GROUPS_CONTEXT = ImmutableMap.of(Login.ODE_GROUP, "-1");

    /** The client object, this is the entry point to the Server. */
    protected ode.client client;

    /** A root-client object. */
    protected ode.client root;

    /** Helper reference to the <code>Service factory</code>. */
    protected ServiceFactoryPrx factory;

    /** Helper reference to the <code>Service factory</code>. */
    protected ServiceFactoryPrx factoryEncrypted;

    /** Helper reference to the <code>IQuery</code> service. */
    protected IQueryPrx iQuery;

    /** Helper reference to the <code>IUpdate</code> service. */
    protected IUpdatePrx iUpdate;

    /** Helper reference to the <code>IAdmin</code> service. */
    protected IAdminPrx iAdmin;

    /** Helper reference to the <code>IPixels</code> service. */
    protected IPixelsPrx iPix;

    /** Helper reference to the server's critical roles. */
    protected Roles roles;

    /** Reference to the importer store. */
    private ODEMetadataStoreClient importer;

    /** Helper class creating mock object. */
    protected ModelMockFactory mmFactory;

    /** the managed repository directory for the user from test class setup **/
    private String userFsDir = null;

    /**
     * {@link ode.client} instances which are created via the newUser*
     * methods. These will be forcefully closed at the end of the test.
     * "new ode.client(...)" should be strictly avoided except for in the
     * method {@link #newOdeClient()}.
     *
     * @see #newUserAndGroup(Permissions)
     * @see #newUserAndGroup(String)
     * @see #newUserInGroup()
     * @see #newUserInGroup(EventContext)
     * @see #newUserInGroup(ExperimenterGroup)
     */
    private final Set<ode.client> clients = new HashSet<ode.client>();

    /* a simple valid Python script */
    private String pythonScript = null;

    protected AbstractServerTest() {
        final ode.system.Roles defaultRoles = new ode.system.Roles();
        roles = new Roles(
                defaultRoles.getRootId(), defaultRoles.getRootName(),
                defaultRoles.getSystemGroupId(), defaultRoles.getSystemGroupName(),
                defaultRoles.getUserGroupId(), defaultRoles.getUserGroupName(),
                defaultRoles.getGuestId(), defaultRoles.getGuestName(),
                defaultRoles.getGuestGroupId(), defaultRoles.getGuestGroupName());
    }

    /**
     * Sole location where {@link ode.client#client()} or any other
     * {@link ode.client} constructor should be called.
     */
    protected ode.client newOdeClient() {
        ode.client client = new ode.client(); // OK
        clients.add(client);
        return client;
    }

    /**
     * Creates a client for the root user.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected ode.client newRootOdeClient() throws Exception {
        ode.client client = newOdeClient();
        client.createSession("root", rootpass);
        return client;
    }

    /**
     * Initializes the various services.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Override
    @BeforeClass(alwaysRun = true)
    protected void setUp() throws Exception {
        // administrator client
        ode.client tmp = newOdeClient();
        rootpass = tmp.getProperty("ode.rootpass");
        root = newRootOdeClient();
        tmp.__del__();

        scalingFactor = 500;
        final String timeoutString = System.getProperty("ode.test.timeout");
        if (StringUtils.isNotBlank(timeoutString)) {
            try {
                scalingFactor = Long.valueOf(timeoutString);
            } catch (NumberFormatException e) {
                log.warn("Problem setting 'ode.test.timeout' to: {}. " +
                         "Defaulting to {}.", timeoutString, scalingFactor);
            }
        }
        final EventContext ctx = newUserAndGroup("rw----");
        this.userFsDir = ctx.userName + "_" + ctx.userId + FsFile.separatorChar;
        SimpleBackOff backOff = new SimpleBackOff();
        long newScalingFactor = (long) backOff.getScalingFactor()
                * backOff.getCount();
        if (newScalingFactor > scalingFactor) {
            scalingFactor = newScalingFactor;
        }
    }

    /**
     * Closes the session.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Override
    @AfterClass
    public void tearDown() throws Exception {
        clean();
        for (ode.client c : clients) {
            if (c != null) {
                c.__del__();
            }
        }
    }

    /**
     * Ask the server to log the given message.
     * @param message a message
     */
    @SuppressWarnings("deprecation")
    private void logOnServer(String... message) {
        final RawAccessRequest rar = new RawAccessRequest();
        rar.command = "log";
        rar.path = "info";
        rar.repoUuid = ScriptRepoHelper.SCRIPT_REPO;
        rar.args = Arrays.asList(message);
        try {
            doChange(root, root.getSession(), rar, true);
        } catch (Throwable t) {
            /* We tried but no sense causing further clutter around root cause. */
        }
    }

    /**
     * Log on the server that the given test method will start.
     * @param testMethod a test method
     */
    @BeforeMethod
    public void logTestStart(Method testMethod) {
        logOnServer(String.format("%s.%s - test starts",
                testMethod.getDeclaringClass().getSimpleName(), testMethod.getName()));
    }

    /**
     * Log on the server that the given test method has finished.
     * @param testMethod a test method
     */
    @AfterMethod
    public void logTestEnd(Method testMethod) {
        logOnServer(String.format("%s.%s - test ends",
                testMethod.getDeclaringClass().getSimpleName(), testMethod.getName()));
    }

    /**
     * Creates the import if not already initialized and returns it.
     */
    protected ODEMetadataStoreClient createImporter() throws Exception
    {

        if (importer == null) {
            try {
                importer = new ODEMetadataStoreClient();
                importer.initialize(factory);
            } catch (Exception e) {
                if (importer != null) {
                    try {
                        importer.closeServices();
                    } catch (Exception ex) {
                        //the initial error will be thrown
                    }
                    importer = null;
                }
                throw e;
            }
        }
        return importer;
    }

    /**
     * An enumeration of properties for which IObjects can be examined.
     * Used in {@link AbstractServerTest.verifyObjectProperty}.
     */
    private enum DetailsProperty {
        GROUP("group"),
        OWNER("owner");

        private final String name;

        DetailsProperty(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * Add the given annotation to the given image.
     * @param image an image
     * @param annotation an annotation
     * @return the new loaded link from the image to the annotation
     * @throws ServerError an error possibly occurring during saving of the link
     */
    protected ImageAnnotationLink linkParentToChild(Image image, Annotation annotation) throws ServerError {
        if (image.isLoaded()) {
            image = (Image) image.proxy();
        }
        if (annotation.isLoaded() && annotation.getId() != null) {
            annotation = (Annotation) annotation.proxy();
        }

        final ImageAnnotationLink link = new ImageAnnotationLinkI();
        link.setParent(image);
        link.setChild(annotation);
        return (ImageAnnotationLink) iUpdate.saveAndReturnObject(link);
    }

    /**
     * Create a link between a Project and a Dataset.
     * @param project a Bhojpur ODE Project
     * @param dataset a Bhojpur ODE Dataset
     * @return the created link
     * @throws ServerError an error possibly occurring during saving of the link
     */
    protected ProjectDatasetLink linkParentToChild(Project project, Dataset dataset) throws ServerError {
        if (project.isLoaded() && project.getId() != null) {
            project = (Project) project.proxy();
        }
        if (dataset.isLoaded() && dataset.getId() != null) {
            dataset = (Dataset) dataset.proxy();
        }

        final ProjectDatasetLink link = new ProjectDatasetLinkI();
        link.setParent(project);
        link.setChild(dataset);
        return (ProjectDatasetLink) iUpdate.saveAndReturnObject(link);
    }

    /**
     * Create a link between a Dataset and an Image.
     * @param dataset a Bhojpur ODE Dataset
     * @param image a Bhojpur ODE Image
     * @return the created link
     * @throws ServerError an error possibly occurring during saving of the link
     */
    protected DatasetImageLink linkParentToChild(Dataset dataset, Image image) throws ServerError {
        if (dataset.isLoaded() && dataset.getId() != null) {
            dataset = (Dataset) dataset.proxy();
        }
        if (image.isLoaded() && image.getId() != null) {
            image = (Image) image.proxy();
        }

        final DatasetImageLink link = new DatasetImageLinkI();
        link.setParent(dataset);
        link.setChild(image);
        return (DatasetImageLink) iUpdate.saveAndReturnObject(link);
    }

    /**
     * Assert that the given object is in the given group.
     * @param object a model object
     * @param expectedGroup an experimenter group
     * @throws ServerError unexpected
     */
    protected void assertInGroup(IObject object, ExperimenterGroup expectedGroup) throws ServerError {
        assertInGroup(Collections.singleton(object), expectedGroup);
    }

    /**
     * Assert that the given objects are in the given group.
     * @param objects some model objects
     * @param expectedGroup an experimenter group
     * @throws ServerError unexpected
     */
    protected void assertInGroup(Collection<? extends IObject> objects, ExperimenterGroup expectedGroup) throws ServerError {
        assertInGroup(objects, expectedGroup.getId().getValue());
    }

    /**
     * Assert that the given object is in the given group.
     * @param object a model object
     * @param expectedGroupId a group Id
     * @throws ServerError unexpected
     */
    protected void assertInGroup(IObject object, long expectedGroupId) throws ServerError {
        assertInGroup(Collections.singleton(object), expectedGroupId);
    }

    /**
     * Assert that the given objects are in the given group.
     * @param objects some model objects
     * @param expectedGroupId a group Id
     * @throws ServerError unexpected
     */
    protected void assertInGroup(Collection<? extends IObject> objects, long expectedGroupId) throws ServerError {
        verifyObjectProperty(objects, expectedGroupId, DetailsProperty.GROUP);
    }

    /**
     * Assert that certain objects either belong to a certain group
     * or have a certain owner.
     * @param testedObjects some model objects to test for properties
     * @param id expected id of the property object (of GROUP or OWNER)
     * @param property property to examine the testedObjects for (can be GROUP or OWNER)
     * @throws ServerError if query fails
     */
    protected void verifyObjectProperty(Collection<? extends IObject> testedObjects, long id, DetailsProperty property)
            throws ServerError {
        if (testedObjects.isEmpty()) {
            throw new IllegalArgumentException("must assert about some objects");
        }
        for (final IObject testedObject : testedObjects) {
            final String testedObjectName = testedObject.getClass().getName() + '[' + testedObject.getId().getValue() + ']';
            final String query = "SELECT details." + property + ".id FROM " +
                    testedObject.getClass().getSuperclass().getSimpleName() + " WHERE id = :id";
            final Parameters params = new ParametersI().addId(testedObject.getId());
            final List<List<RType>> results = root.getSession().getQueryService().projection(query, params, ALL_GROUPS_CONTEXT);
            final long actualId = ((RLong) results.get(0).get(0)).getValue();
            Assert.assertEquals(actualId, id, testedObjectName);
        }
    }

    /**
     * Assert that the given object is owned by the given owner.
     * @param object a model object
     * @param expectedOwner a user's event context
     * @throws ServerError unexpected
     */
    protected void assertOwnedBy(IObject object, EventContext expectedOwner) throws ServerError {
        assertOwnedBy(Collections.singleton(object), expectedOwner);
    }

    /**
     * Assert that the given objects are owned by the given owner.
     * @param objects some model objects
     * @param expectedOwner a user's event context
     * @throws ServerError unexpected
     */
    protected void assertOwnedBy(Collection<? extends IObject> objects, EventContext expectedOwner) throws ServerError {
        verifyObjectProperty(objects, expectedOwner.userId, DetailsProperty.OWNER);
    }

    /**
     * Creates a new group and experimenter and returns the event context.
     *
     * @param perms
     *            The permissions level.
     * @param owner
     *            Pass <code>true</code> to indicate that the new user is an
     *            owner of the group, <code>false</code> otherwise.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected EventContext newUserAndGroup(String perms, boolean owner)
            throws Exception {
        return newUserAndGroup(new PermissionsI(perms), owner);
    }

    /**
     * Creates a new group and experimenter and returns the event context.
     *
     * @param perms
     *            The permissions level.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected EventContext newUserAndGroup(String perms) throws Exception {
        return newUserAndGroup(new PermissionsI(perms), false);
    }

    /**
     * Creates a new group and experimenter and returns the event context.
     *
     * @param perms
     *            The permissions level.
     * @param owner
     *            Pass <code>true</code> to indicate that the new user is an
     *            owner of the group, <code>false</code> otherwise.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected EventContext newUserAndGroup(Permissions perms, boolean owner)
            throws Exception {
        IAdminPrx rootAdmin = root.getSession().getAdminService();
        String uuid = UUID.randomUUID().toString();
        ExperimenterGroup g = new ExperimenterGroupI();
        g.setName(ode.rtypes.rstring(uuid));
        g.setLdap(ode.rtypes.rbool(false));
        g.getDetails().setPermissions(perms);
        g = new ExperimenterGroupI(rootAdmin.createGroup(g), false);
        return newUserInGroup(g, owner);
    }

    /**
     * Creates a new group and experimenter and returns the event context.
     *
     * @param perms
     *            The permissions level.
     * @param experimenterId
     *            The identifier of the experimenter. @ * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected ExperimenterGroup newGroupAddUser(Permissions perms,
            long experimenterId) throws Exception {
        return newGroupAddUser(perms, Arrays.asList(experimenterId), false);
    }

    /**
     * Creates a new group and experimenter and returns the event context.
     *
     * @param perms
     *            The permissions level.
     * @param experimenterId
     *            The identifier of the experimenter.
     * @param owner
     *            Pass <code>true</code> to indicate that the new user is an
     *            owner of the group, <code>false</code> otherwise.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected ExperimenterGroup newGroupAddUser(Permissions perms,
            long experimenterId, boolean owner) throws Exception {
        return newGroupAddUser(perms, Arrays.asList(experimenterId), owner);
    }

    /**
     * Creates a new group and experimenter and returns the event context.
     *
     * @param perms
     *            The permissions level.
     * @param experimenterIds
     *            The identifier of the experimenters.
     * @param owner
     *            Pass <code>true</code> to indicate that the new user is an
     *            owner of the group, <code>false</code> otherwise.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected ExperimenterGroup newGroupAddUser(Permissions perms,
            List<Long> experimenterIds, boolean owner) throws Exception {
        IAdminPrx rootAdmin = root.getSession().getAdminService();
        String uuid = UUID.randomUUID().toString();
        ExperimenterGroup g = new ExperimenterGroupI();
        g.setName(ode.rtypes.rstring(uuid));
        g.setLdap(ode.rtypes.rbool(false));
        g.getDetails().setPermissions(perms);
        g = new ExperimenterGroupI(rootAdmin.createGroup(g), false);
        return addUsers(g, experimenterIds, owner);
    }

    protected ExperimenterGroup addUsers(ExperimenterGroup g,
            List<Long> experimenterIds, boolean owner) throws Exception {
        IAdminPrx rootAdmin = root.getSession().getAdminService();
        g = rootAdmin.getGroup(g.getId().getValue());
        Iterator<Long> i = experimenterIds.iterator();
        List<Experimenter> l = new ArrayList<Experimenter>();
        while (i.hasNext()) {
            Experimenter e = rootAdmin.getExperimenter(i.next());
            rootAdmin.addGroups(e, Arrays.asList(g));
            l.add(e);
        }
        if (owner && l.size() > 0) {
            rootAdmin.addGroupOwners(g, l);
        }
        return g;
    }

    /**
     * Creates a new group and experimenter and returns the event context.
     *
     * @param perms
     *            The permissions level.
     * @param experimenterId
     *            The identifier of the experimenters.
     * @param owner
     *            Pass <code>true</code> to indicate that the new user is an
     *            owner of the group, <code>false</code> otherwise.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected ExperimenterGroup newGroupAddUser(String perms,
            List<Long> experimenterIds, boolean owner) throws Exception {
        return newGroupAddUser(new PermissionsI(perms), experimenterIds, owner);
    }

    /**
     * Creates a new group and experimenter and returns the event context.
     *
     * @param perms
     *            The permissions level.
     * @param experimenterId
     *            The identifier of the experimenters.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected ExperimenterGroup newGroupAddUser(String perms,
            List<Long> experimenterIds) throws Exception {
        return newGroupAddUser(new PermissionsI(perms), experimenterIds, false);
    }

    /**
     * Creates a new group and experimenter and returns the event context.
     *
     * @param perms
     *            The permissions level.
     * @param experimenterId
     *            The identifier of the experimenter.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected ExperimenterGroup newGroupAddUser(String perms,
            long experimenterId) throws Exception {
        return newGroupAddUser(new PermissionsI(perms), experimenterId);
    }

    /**
     * Creates a new group and experimenter and returns the event context.
     *
     * @param perms
     *            The permissions level.
     * @param experimenterId
     *            The identifier of the experimenter.
     * @param owner
     *            Pass <code>true</code> to indicate that the new user is an
     *            owner of the group, <code>false</code> otherwise.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected ExperimenterGroup newGroupAddUser(String perms,
            long experimenterId, boolean owner) throws Exception {
        return newGroupAddUser(new PermissionsI(perms), experimenterId, owner);
    }

    /**
     * Creates a new user in the current group.
     *
     * @return
     */
    protected EventContext newUserInGroup() throws Exception {
        EventContext ec = client.getSession().getAdminService()
                .getEventContext();
        return newUserInGroup(ec);
    }

    /**
     * Takes the {@link EventContext} from another user and creates a new user
     * in the same group as that user is currently logged in to.
     *
     * @param previousUser
     *            The context of the previous user.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected EventContext newUserInGroup(EventContext previousUser)
            throws Exception {
        return newUserInGroup(previousUser, false);
    }

    /**
     * Takes the {@link EventContext} from another user and creates a new user
     * in the same group as that user is currently logged in to.
     *
     * @param previousUser
     *            The context of the previous user.
     * @param owner
     *            Pass <code>true</code> to indicate that the new user is an
     *            owner of the group, <code>false</code> otherwise.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected EventContext newUserInGroup(EventContext previousUser,
            boolean owner) throws Exception {
        ExperimenterGroup eg = new ExperimenterGroupI(previousUser.groupId,
                false);
        return newUserInGroup(eg, owner);
    }

    /**
     * Creates a new user in the specified group.
     *
     * @param group
     *            The group to add the user to.
     * @param owner
     *            Pass <code>true</code> to indicate that the new user is an
     *            owner of the group, <code>false</code> otherwise.
     * @return The context.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected EventContext newUserInGroup(ExperimenterGroup group, boolean owner)
            throws Exception {
        IAdminPrx rootAdmin = root.getSession().getAdminService();
        group = rootAdmin.getGroup(group.getId().getValue());

        String uuid = UUID.randomUUID().toString();
        Experimenter e = new ExperimenterI();
        e.setOdeName(ode.rtypes.rstring(uuid));
        e.setFirstName(ode.rtypes.rstring("integration"));
        e.setLastName(ode.rtypes.rstring("tester"));
        e.setLdap(ode.rtypes.rbool(false));
        long id = newUserInGroupWithPassword(e, group, uuid);
        e = rootAdmin.getExperimenter(id);
        rootAdmin.addGroups(e, Arrays.asList(group));
        if (owner) {
            rootAdmin.addGroupOwners(group, Arrays.asList(e));
        }
        ode.client client = newOdeClient();
        client.createSession(uuid, uuid);
        return init(client);
    }

    /**
     * Creates the specified user in the specified groups. Also adds the user
     * to the default user group. Requires a password.
     *
     * @param experimenter The pre-populated Experimenter object.
     * @param groups The target groups.
     * @param password The user password.
     * @return long The created user ID.
     */
    protected long newUserInGroupWithPassword(Experimenter experimenter,
            List<ExperimenterGroup> groups, String password) throws Exception {
        IAdminPrx rootAdmin = root.getSession().getAdminService();
        ExperimenterGroup userGroup = rootAdmin.lookupGroup(roles.userGroupName);
        return rootAdmin.createExperimenterWithPassword(experimenter,
                ode.rtypes.rstring(password), userGroup, groups);
    }

    /**
     * Creates the specified user in the specified group. Also adds the user
     * to the default user group. Requires a password.
     *
     * @param experimenter The pre-populated Experimenter object.
     * @param group The target group.
     * @param password The user password.
     * @return long The created user ID.
     */
    protected long newUserInGroupWithPassword(Experimenter experimenter,
            ExperimenterGroup group, String password) throws Exception {
        return newUserInGroupWithPassword(experimenter,
                Lists.newArrayList(group), password);
    }

    /**
     * Create a fileset with a template prefix appropriate for the user created
     * by {@link #setUp()}. Does not access the Bhojpur ODE API or persist the new
     * fileset.
     *
     * @return a new fileset
     */
    protected Fileset newFileset() {
        final Fileset fileset = new FilesetI();
        fileset.setTemplatePrefix(ode.rtypes.rstring(this.userFsDir
                + System.currentTimeMillis() + FsFile.separatorChar));
        return fileset;
    }

    /**
     * Logs in the user.
     *
     * @param g
     *            The group to log into.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected EventContext loginUser(ExperimenterGroup g) throws Exception {
        EventContext ec = iAdmin.getEventContext();
        ode.client client = newOdeClient();
        client.createSession(ec.userName, ec.userName);
        client.getSession().setSecurityContext(
                new ExperimenterGroupI(g.getId(), false));
        return init(client);
    }

    /**
     * Logs in the user.
     *
     * @param ownerEc
     *            The context of the user.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected void loginUser(EventContext ownerEc) throws Exception {
        loginUser(ownerEc.userName);
    }

    /**
     * Logs in the user.
     *
     * @param ownerName
     *            The Bhojpur ODE name of the user.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected void loginUser(String ownerName) throws Exception {
        ode.client client = newOdeClient();
        client.createSession(ownerName, ownerName);
        init(client);
    }

    /**
     * Logs in the user.
     *
     * @param ownerEc
     *            The context of the user.
     * @param g
     *            The group to log into.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected void loginUser(EventContext ownerEc, ExperimenterGroup g) throws Exception {
        final ode.client client = newOdeClient();
        client.createSession(ownerEc.userName, ownerEc.userName);
        client.getSession().setSecurityContext(
                new ExperimenterGroupI(g.getId(), false));
        init(client);
    }

    /**
     * Creates a new {@link ode.client} for root based on the current group.
     */
    protected void logRootIntoGroup() throws Exception {
        EventContext ec = iAdmin.getEventContext();
        logRootIntoGroup(ec);
    }

    /**
     * Creates a new {@link ode.client} for root based on the
     * {@link EventContext}
     */
    protected void logRootIntoGroup(EventContext ec) throws Exception {
        logRootIntoGroup(ec.groupId);
    }

    /**
     * Creates a new {@link ode.client} for root based on the group
     * identifier.
     */
    protected void logRootIntoGroup(long groupId) throws Exception {
        ode.client rootClient = newRootOdeClient();
        rootClient.getSession().setSecurityContext(
                new ExperimenterGroupI(groupId, false));
        init(rootClient);
    }

    /**
     * Makes the current user an owner of the current group.
     */
    protected void makeGroupOwner() throws Exception {
        EventContext ec = client.getSession().getAdminService()
                .getEventContext();
        IAdminPrx rootAdmin = root.getSession().getAdminService();
        rootAdmin.setGroupOwner(new ExperimenterGroupI(ec.groupId, false),
                new ExperimenterI(ec.userId, false));

        disconnect();
        init(ec); // Create new session with the added privileges
    }

    /**
     * Saves the current client before calling {@link #clean()} and returns it
     * to the user.
     */
    protected ode.client disconnect() throws Exception {
        ode.client oldClient = client;
        clean();
        client = null;
        return oldClient;
    }

    /**
     * If {@link #client} is non-null, destroys the client and nulls all fields
     * which were set on creation.
     */
    protected void clean() throws Exception {
        if (importer != null) {
            importer.closeServices();
            importer = null;
        }

        if (client != null) {
            client.__del__();
        }
        client = null;
    }

    /**
     */
    protected EventContext init(EventContext ec) throws Exception {
        ode.client c = newOdeClient();
        factoryEncrypted = c.createSession(ec.userName, ec.userName);
        return init(c);
    }

    /**
     * Resets the client and return the event context.
     *
     * @param client
     *            The client to handle.
     * @return The event context to handle.
     * @throws Exception
     */
    protected EventContext init(ode.client client) throws Exception {

        clean();

        this.client = client;
        factory = client.getSession();
        EventContext ctx = null;
        try {
            iQuery = factory.getQueryService();
            iUpdate = factory.getUpdateService();
            iAdmin = factory.getAdminService();
            iPix = factory.getPixelsService();
            roles = iAdmin.getSecurityRoles();
            mmFactory = new ModelMockFactory(root.getSession().getTypesService());
            ctx = iAdmin.getEventContext();
        } catch (SecurityViolation sv) {
            mmFactory = null;
            iAdmin = null;
            iQuery = null;
            iUpdate = null;
            iPix = null;
        }

        return ctx;
    }

    /**
     * Compares the passed rendering definitions.
     *
     * @param def1
     *            The first rendering definition to handle.
     * @param def2
     *            The second rendering definition to handle.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected void compareRenderingDef(RenderingDef def1, RenderingDef def2)
            throws Exception {
        Assert.assertNotNull(def1);
        Assert.assertNotNull(def2);
        Assert.assertEquals(def1.getDefaultZ().getValue(),
                def2.getDefaultZ().getValue());
        Assert.assertEquals(def1.getDefaultT().getValue(),
                def2.getDefaultT().getValue());
        Assert.assertEquals(def1.getModel().getValue().getValue(),
                def2.getModel().getValue().getValue());
        QuantumDef q1 = def1.getQuantization();
        QuantumDef q2 = def2.getQuantization();
        Assert.assertNotNull(q1);
        Assert.assertNotNull(q2);
        Assert.assertEquals(q1.getBitResolution().getValue(),
                q2.getBitResolution().getValue());
        Assert.assertEquals(q1.getCdStart().getValue(), q2.getCdStart().getValue());
        Assert.assertEquals(q1.getCdEnd().getValue(), q2.getCdEnd().getValue());
        List<ChannelBinding> channels1 = def1.copyWaveRendering();
        List<ChannelBinding> channels2 = def2.copyWaveRendering();
        Assert.assertNotNull(channels1);
        Assert.assertNotNull(channels2);
        Assert.assertEquals(channels1.size(), channels2.size());
        Iterator<ChannelBinding> i = channels1.iterator();
        ChannelBinding c1, c2;
        int index = 0;
        while (i.hasNext()) {
            c1 = i.next();
            c2 = channels2.get(index);
            Assert.assertEquals(c1.getAlpha().getValue(), c2.getAlpha().getValue());
            Assert.assertEquals(c1.getRed().getValue(), c2.getRed().getValue());
            Assert.assertEquals(c1.getGreen().getValue(), c2.getGreen().getValue());
            Assert.assertEquals(c1.getBlue().getValue(), c2.getBlue().getValue());
            Assert.assertEquals(c1.getCoefficient().getValue(), c2.getCoefficient()
                    .getValue());
            Assert.assertEquals(c1.getFamily().getValue().getValue(),
                    c2.getFamily().getValue().getValue());
            Assert.assertEquals(c1.getInputStart().getValue(), c2.getInputStart()
                    .getValue());
            Assert.assertEquals(c1.getInputEnd().getValue(), c2.getInputEnd()
                    .getValue());
            Boolean b1 = Boolean.valueOf(c1.getActive().getValue());
            Boolean b2 = Boolean.valueOf(c2.getActive().getValue());
            Assert.assertEquals(b1, b2);
            b1 = Boolean.valueOf(c1.getNoiseReduction().getValue());
            b2 = Boolean.valueOf(c2.getNoiseReduction().getValue());
            Assert.assertEquals(b1, b2);
            //Check lut
            if (c1.getLookupTable() != null && c2.getLookupTable() != null) {
                Assert.assertEquals(c1.getLookupTable().getValue(),
                        c2.getLookupTable().getValue());
            }
        }
    }

    /**
     * Helper method to load the wells.
     *
     * @param plateID
     *            The identifier of the plate.
     * @param pixels
     *            Pass <code>true</code> to load the pixels, <code>false</code>
     *            otherwise.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @SuppressWarnings("unchecked")
    protected List<Well> loadWells(long plateID, boolean pixels)
            throws Exception {
        StringBuilder sb = new StringBuilder();
        ParametersI param = new ParametersI();
        param.addLong("plateID", plateID);
        sb.append("select well from Well as well ");
        sb.append("left outer join fetch well.plate as pt ");
        sb.append("left outer join fetch well.wellSamples as ws ");
        sb.append("left outer join fetch ws.plateAcquisition as pa ");
        sb.append("left outer join fetch ws.image as img ");
        if (pixels) {
            sb.append("left outer join fetch img.pixels as pix ");
            sb.append("left outer join fetch pix.pixelsType as pixType ");
        }
        sb.append("where pt.id = :plateID");
        return (List<Well>) (List<?>) iQuery.findAllByQuery(sb.toString(),
                param);
    }

    /**
     * Helper method to load a well sample with its well and plate intact (and
     * possibly a screen if one exists) for the given pixels.
     *
     * @param p
     * @return
     * @throws ServerError
     */
    protected WellSample getWellSample(Pixels p) throws ServerError {
        long id = p.getImage().getId().getValue();
        String sql = "select ws from WellSample as ws ";
        sql += "join fetch ws.well as w ";
        sql += "left outer join fetch ws.plateAcquisition as pa ";
        sql += "join fetch w.plate as p ";
        sql += "left outer join fetch p.screenLinks sl ";
        sql += "left outer join fetch sl.parent s ";
        sql += "where ws.image.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> results = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(1, results.size());
        WellSample ws = (WellSample) results.get(0);
        Assert.assertNotNull(ws);
        return ws;
    }

    /**
     * Helper method to load the Experiment which is is associated with the
     * pixels argument via Image.
     *
     * @param p
     * @return
     * @throws ServerError
     */
    protected Experiment getExperiment(Pixels p) throws ServerError {
        long id = p.getImage().getId().getValue();
        String sql = "select e from Image i ";
        sql += "join i.experiment e ";
        sql += "where i.id = :id";
        ParametersI param = new ParametersI();
        param.addId(id);
        List<IObject> results = iQuery.findAllByQuery(sql, param);
        Assert.assertEquals(1, results.size());
        Experiment e = (Experiment) results.get(0);
        Assert.assertNotNull(e);
        return e;
    }

    /**
     * @return a repository rooted at a directory named <q>ManagedRepository</q>
     * @throws ServerError if the repository map could not be retrieved
     */
    protected RepositoryPrx getManagedRepository() throws ServerError {
        final RepositoryMap repos = factory.sharedResources().repositories();
        int index = repos.descriptions.size();
        while (--index >= 0) {
            if ("ManagedRepository".equals(repos.descriptions.get(index).getName().getValue())) {
                return repos.proxies.get(index);
            }
        }
        throw new RuntimeException("no managed repository");
    }

    /**
     * Provides a simple Python script with valid syntax.
     * @return the content of an uploadable Python script
     * @throws IOException if the simple script cannot be read
     */
    protected String getPythonScript() throws IOException {
        if (pythonScript == null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("import ode.scripts as s");
            buffer.append("\n");
            buffer.append("import uuid");
            buffer.append("\n");
            buffer.append("\n");
            buffer.append("uuid = str(uuid.uuid4())");
            buffer.append("\n");
            buffer.append("print(\"I am the script named %s.\" % uuid)");
            buffer.append("\n");
            buffer.append("client = s.client(uuid, \"simple script\")");
            buffer.append("\n");
            pythonScript = buffer.toString();
        }
        return pythonScript;
    }

    /**
     * Makes sure that the passed object exists.
     *
     * @param obj
     *            The object to handle.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected void assertExists(IObject obj) throws Exception {
        IObject copy = iQuery.find(obj.getClass().getSimpleName(), obj.getId()
                .getValue());
        Assert.assertNotNull(copy,
                String.format("%s:%s", obj.getClass().getName(), obj.getId()
                        .getValue()) + " is missing!");
    }

    protected void assertAllExist(IObject... obj) throws Exception {
        for (IObject iObject : obj) {
            assertExists(iObject);
        }
    }

    protected void assertAllExist(Iterable<? extends IObject> obj) throws Exception {
        for (IObject iObject : obj) {
            assertExists(iObject);
        }
    }

    protected void assertExists(String className, Long id) throws ServerError {
        assertAllExist(className, Collections.singletonList(id));
    }

    protected void assertAllExist(String className, Collection<Long> ids) throws ServerError {
        final String hql = "SELECT COUNT(*) FROM " + className + " WHERE id IN (:ids)";
        final List<List<RType>> results = iQuery.projection(hql, new ParametersI().addIds(ids));
        final long count = ((RLong) results.get(0).get(0)).getValue();
        Assert.assertEquals(count, ids.size());
    }

    /**
     * Makes sure that the passed object does not exist.
     *
     * @param obj
     *            The object to handle.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected void assertDoesNotExist(IObject obj) throws Exception {
        IObject copy = iQuery.find(obj.getClass().getSimpleName(), obj.getId()
                .getValue());
        Assert.assertNull(copy,
                String.format("%s:%s", obj.getClass().getName(), obj.getId()
                        .getValue()) + " still exists!");
    }

    protected void assertNoneExist(IObject... obj) throws Exception {
        for (IObject iObject : obj) {
            assertDoesNotExist(iObject);
        }
    }

    protected void assertNoneExist(Iterable<? extends IObject> obj) throws Exception {
        for (IObject iObject : obj) {
            assertDoesNotExist(iObject);
        }
    }

    protected void assertDoesNotExist(String className, Long id) throws ServerError {
        assertNoneExist(className, Collections.singletonList(id));
    }

    protected void assertNoneExist(String className, Collection<Long> ids) throws ServerError {
        final String hql = "SELECT COUNT(*) FROM " + className + " WHERE id IN (:ids)";
        final List<List<RType>> results = iQuery.projection(hql, new ParametersI().addIds(ids));
        final long count = ((RLong) results.get(0).get(0)).getValue();
        Assert.assertEquals(count, 0);
    }

    /**
     * Imports the specified ODE-XML file and returns the pixels set if
     * successfully imported.
     *
     * @param file
     *            The file to import.
     * @param format
     *            The format of the file to import.
     * @param target
     *            The container where to import the image.
     * @return The collection of imported pixels set.
     * @throws Throwable
     *             Thrown if an error occurred while encoding the image.
     */
    protected List<Pixels> importFile(File file, String format, IObject target)
            throws Throwable {
        return importFile(importer, new ImportConfig(), file, format, target);
    }

    /**
     * Imports the specified ODE-XML file and returns the pixels set if
     * successfully imported.
     *
     * @param config
     *            The import configuration.
     * @param file
     *            The file to import.
     * @param format
     *            The format of the file to import.
     * @return The collection of imported pixels set.
     * @throws Throwable
     *             Thrown if an error occurred while encoding the image.
     */
    protected List<Pixels> importFile(ImportConfig config, File file, String format)
            throws Throwable {
        return importFile(importer, config, file, format);
    }

    /**
     * Imports the specified ODE-XML file and returns the pixels set if
     * successfully imported.
     *
     * @param file
     *            The file to import.
     * @param format
     *            The format of the file to import.
     * @return The collection of imported pixels set.
     * @throws Exception
     *             Thrown if an error occurred while encoding the image.
     */
    protected List<Pixels> importFile(File file, String format)
            throws Throwable {
        return importFile(new ImportConfig(), file, format);
    }

    /**
     * Imports the specified ODE-XML file and returns the pixels set if
     * successfully imported.
     *
     * @param importer
     *            The metadataStore to use.
     * @param file
     *            The file to import.
     * @param format
     *            The format of the file to import.
     * @return The collection of imported pixels set.
     * @throws Throwable
     *             Thrown if an error occurred while encoding the image.
     */
    protected List<Pixels> importFile(ODEMetadataStoreClient importer,
                                      File file, String format) throws Throwable {
        return importFile(importer, new ImportConfig(), file, format, null);
    }

    /**
     * Imports the specified ODE-XML file and returns the pixels set if
     * successfully imported.
     *
     * @param importer
     *            The metadataStore to use.
     * @param config
     *            The import configuration.
     * @param file
     *            The file to import.
     * @param format
     *            The format of the file to import.
     * @return The collection of imported pixels set.
     * @throws Throwable
     *             Thrown if an error occurred while encoding the image.
     */
    protected List<Pixels> importFile(ODEMetadataStoreClient importer,
                                      ImportConfig config, File file, String format) throws Throwable {
        return importFile(importer, config, file, format, null);
    }

    /**
     * Imports the specified ODE-XML file and returns the pixels set if
     * successfully imported.
     *
     * @param importer
     *            The metadataStore to use.
     * @param config
     *            The import configuration.
     * @param file
     *            The file to import.
     * @param format
     *            The format of the file to import.
     * @return The collection of imported pixels set.
     * @throws Throwable
     *             Thrown if an error occurred while encoding the image.
     */
    protected List<Pixels> importFile(ODEMetadataStoreClient importer,
                                      ImportConfig config, File file,
                                      String format, IObject target) throws Throwable {
        if (importer == null) {
            importer = createImporter();
        }
        String[] paths = new String[1];
        paths[0] = file.getAbsolutePath();
        ODEWrapper reader = new ODEWrapper(config);
        IObserver o = new IObserver() {
            public void update(IObservable importLibrary, ImportEvent event) {
                if (event instanceof ErrorHandler.EXCEPTION_EVENT) {
                    Exception ex = ((ErrorHandler.EXCEPTION_EVENT) event).exception;
                    if (ex instanceof RuntimeException) {
                        throw (RuntimeException) ex;
                    } else {
                        throw new RuntimeException(ex);
                    }
                }
            }
        };
        ImportCandidates candidates = new ImportCandidates(reader, paths, o);

        ImportLibrary library = new ImportLibrary(importer, reader);
        library.addObserver(o);

        ImportContainer ic = candidates.getContainers().get(0);
        // new ImportContainer(
        // file, null, target, false, null, null, null, null);
        ic.setUserSpecifiedName(format);
        ic.setTarget(target);
        // ic = library.uploadFilesToRepository(ic);
        List<Pixels> pixels = library.importImage(ic, 0, 0, 1);
        Assert.assertNotNull(pixels);
        Assert.assertTrue(CollectionUtils.isNotEmpty(pixels));
        return pixels;
    }

    /**
     * Basic asynchronous delete command. Used in order to reduce the number of
     * places that we do the same thing in case the API changes.
     *
     * @param dc
     *            The command to handle.
     * @throws ApiUsageException
     * @throws ServerError
     * @throws InterruptedException
     */
    protected String delete(ode.client c, Delete2... dc)
            throws ApiUsageException, ServerError, InterruptedException {
        return delete(true, c, dc);
    }

    /**
     * Basic asynchronous delete command. Used in order to reduce the number of
     * places that we do the same thing in case the API changes.
     *
     * @param passes
     *            Pass <code>true</code> to indicate that no error found in
     *            report, <code>false</code> otherwise.
     * @param dc
     *            The command to handle.
     * @param strict
     *            whether or not the method should succeed.
     * @throws ApiUsageException
     * @throws ServerError
     * @throws InterruptedException
     */
    protected String delete(boolean passes, ode.client c, Delete2... dc)
            throws ApiUsageException, ServerError, InterruptedException {

        callback(passes, c, dc);
        return "ok";
    }

    /**
     * Asynchronous command for a single delete, this means a single report is
     * returned for testing.
     *
     * @param dc
     *            The SINGLE command to handle.
     * @throws ApiUsageException
     * @throws ServerError
     * @throws InterruptedException
     */
    protected Delete2Response singleDeleteWithReport(ode.client c, Delete2 dc)
            throws ApiUsageException, ServerError, InterruptedException {
        return deleteWithReports(c, dc)[0];
    }

    /**
     * Asynchronous command for delete, report array is returned.
     *
     * @param dc
     *            The command to handle.
     * @throws ApiUsageException
     * @throws ServerError
     * @throws InterruptedException
     */
    private Delete2Response[] deleteWithReports(ode.client c, Delete2... dc)
            throws ApiUsageException, ServerError, InterruptedException {
        CmdCallbackI cb = callback(true, c, dc);
        // If the above passes, then we know it's not an ERR
        DoAllRsp all = (DoAllRsp) cb.getResponse();
        Delete2Response[] reports = new Delete2Response[all.responses.size()];
        for (int i = 0; i < reports.length; i++) {
            reports[i] = (Delete2Response) all.responses.get(i);
        }
        return reports;
    }

    /**
     * Create a single image with binary.
     *
     * After recent changes on the server to check for existing binary data for
     * pixels, many resetDefaults methods tested below began returning null
     * since {@link ode.LockTimeout} exceptions were being thrown server-side.
     * By using ode.client.forEachTile, we can set the necessary data easily.
     *
     * @see ticket:5755
     */
    protected Image createBinaryImage() throws Exception {
        Image image = mmFactory.createImage();
        image = (Image) iUpdate.saveAndReturnObject(image);
        return createBinaryImage(image);
    }

    /**
     * Create a single image with binary.
     *
     * After recent changes on the server to check for existing binary data for
     * pixels, many resetDefaults methods tested below began returning null
     * since {@link ode.LockTimeout} exceptions were being thrown server-side.
     * By using ode.client.forEachTile, we can set the necessary data easily.
     *
     * @param sizeX The number of pixels along the X-axis.
     * @param sizeY The number of pixels along the Y-axis.
     * @param sizeZ The number of z-sections.
     * @param sizeT The number of timepoints.
     * @param sizeC The number of channels.
     * @see ticket:5755
     */
    protected Image createBinaryImage(int sizeX, int sizeY, int sizeZ,
            int sizeT, int sizeC) throws Exception {
        Image image = mmFactory.createImage(sizeX, sizeY, sizeZ, sizeT,
                sizeC, ModelMockFactory.UINT16);
        image = (Image) iUpdate.saveAndReturnObject(image);
        return createBinaryImage(image);
    }

    /**
     * Create the binary data for the given image.
     */
    protected Image createBinaryImage(Image image) throws Exception {
        Pixels pixels = image.getPrimaryPixels();
        // Image
        List<Long> ids = new ArrayList<Long>();
        ids.add(image.getId().getValue());
        // method already tested

        // first write to the image
        ode.util.RPSTileLoop loop = new ode.util.RPSTileLoop(
                client.getSession(), pixels);
        loop.forEachTile(256, 256, new ode.util.TileLoopIteration() {
            public void run(ode.util.TileData data, int z, int c, int t,
                    int x, int y, int tileWidth, int tileHeight, int tileCount) {
                data.setTile(new byte[tileWidth * tileHeight * 8], z, c, t, x,
                        y, tileWidth, tileHeight);
            }
        });
        // This block will change the updateEvent on the pixels
        // therefore we're going to reload the pixels.

        image.setPixels(0, loop.getPixels());
        return image;
    }

    /**
     * Creates various sharable annotations i.e. TagAnnotation, TermAnnotation,
     * FileAnnotation
     *
     * @param parent1
     *            The object to link the annotation to.
     * @param parent2
     *            The object to link the annotation to if not null.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected List<Long> createSharableAnnotation(IObject parent1,
            IObject parent2) throws Exception {
        // Copying to a proxy to prevent issues with parent.annotationLinks
        // becoming stale on multiple copies.
        parent1 = parent1.proxy();
        if (parent2 != null) {
            parent2 = parent2.proxy();
        }

        // creation already tested in UpdateServiceTest
        List<Long> ids = new ArrayList<Long>();
        TagAnnotation c = new TagAnnotationI();
        c.setTextValue(ode.rtypes.rstring("tag"));
        c = (TagAnnotation) iUpdate.saveAndReturnObject(c);
        ids.add(c.getId().getValue());

        TermAnnotation t = new TermAnnotationI();
        t.setTermValue(ode.rtypes.rstring("term"));
        t = (TermAnnotation) iUpdate.saveAndReturnObject(t);
        ids.add(t.getId().getValue());

        OriginalFile of = (OriginalFile) iUpdate.saveAndReturnObject(mmFactory
                .createOriginalFile());
        Assert.assertNotNull(of);
        FileAnnotation f = new FileAnnotationI();
        f.setFile(of);
        f = (FileAnnotation) iUpdate.saveAndReturnObject(f);
        ids.add(f.getId().getValue());

		MapAnnotation ma = new MapAnnotationI();
		List<NamedValue> values = new ArrayList<NamedValue>();
		for (int i = 0; i < 3; i++)
			values.add(new NamedValue("name " + i, "value " + i));
		ma.setMapValue(values);
		ma = (MapAnnotation) iUpdate.saveAndReturnObject(ma);
		ids.add(ma.getId().getValue());
        
        List<IObject> links = new ArrayList<IObject>();
        if (parent1 instanceof Image) {
            ImageAnnotationLink link = new ImageAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((Image) parent1);
            links.add(link);
            link = new ImageAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((Image) parent1);
            links.add(link);
            link = new ImageAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((Image) parent1);
            links.add(link);
            link = new ImageAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((Image) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((Image) parent2);
                links.add(link);
                link = new ImageAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((Image) parent2);
                links.add(link);
                link = new ImageAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((Image) parent2);
                links.add(link);
                link = new ImageAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((Image) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof Project) {
            ProjectAnnotationLink link = new ProjectAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((Project) parent1);
            links.add(link);
            link = new ProjectAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((Project) parent1);
            links.add(link);
            link = new ProjectAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((Project) parent1);
            links.add(link);
            link = new ProjectAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((Project) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((Project) parent2);
                links.add(link);
                link = new ProjectAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((Project) parent2);
                links.add(link);
                link = new ProjectAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((Project) parent2);
                links.add(link);
                link = new ProjectAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((Project) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof Dataset) {
            DatasetAnnotationLink link = new DatasetAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((Dataset) parent1);
            links.add(link);
            link = new DatasetAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((Dataset) parent1);
            links.add(link);
            link = new DatasetAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((Dataset) parent1);
            links.add(link);
            link = new DatasetAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((Dataset) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((Dataset) parent2);
                links.add(link);
                link = new DatasetAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((Dataset) parent2);
                links.add(link);
                link = new DatasetAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((Dataset) parent2);
                links.add(link);
                link = new DatasetAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((Dataset) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof Plate) {
            PlateAnnotationLink link = new PlateAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((Plate) parent1);
            links.add(link);
            link = new PlateAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((Plate) parent1);
            links.add(link);
            link = new PlateAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((Plate) parent1);
            links.add(link);
            link = new PlateAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((Plate) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((Plate) parent2);
                links.add(link);
                link = new PlateAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((Plate) parent2);
                links.add(link);
                link = new PlateAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((Plate) parent2);
                links.add(link);
                link = new PlateAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((Plate) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof Screen) {
            ScreenAnnotationLink link = new ScreenAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((Screen) parent1);
            links.add(link);
            link = new ScreenAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((Screen) parent1);
            links.add(link);
            link = new ScreenAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((Screen) parent1);
            links.add(link);
            link = new ScreenAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((Screen) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((Screen) parent2);
                links.add(link);
                link = new ScreenAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((Screen) parent2);
                links.add(link);
                link = new ScreenAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((Screen) parent2);
                links.add(link);
                link = new ScreenAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((Screen) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof Well) {
            WellAnnotationLink link = new WellAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((Well) parent1);
            links.add(link);
            link = new WellAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((Well) parent1);
            links.add(link);
            link = new WellAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((Well) parent1);
            links.add(link);
            link = new WellAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((Well) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((Well) parent2);
                links.add(link);
                link = new WellAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((Well) parent2);
                links.add(link);
                link = new WellAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((Well) parent2);
                links.add(link);
                link = new WellAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((Well) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof PlateAcquisition) {
            PlateAcquisitionAnnotationLink link = new PlateAcquisitionAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((PlateAcquisition) parent1);
            links.add(link);
            link = new PlateAcquisitionAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((PlateAcquisition) parent1);
            links.add(link);
            link = new PlateAcquisitionAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((PlateAcquisition) parent1);
            links.add(link);
            link = new PlateAcquisitionAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((PlateAcquisition) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((PlateAcquisition) parent2);
                links.add(link);
                link = new PlateAcquisitionAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((PlateAcquisition) parent2);
                links.add(link);
                link = new PlateAcquisitionAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((PlateAcquisition) parent2);
                links.add(link);
                link = new PlateAcquisitionAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((PlateAcquisition) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof Detector) {
            DetectorAnnotationLink link = new DetectorAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((Detector) parent1);
            links.add(link);
            link = new DetectorAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((Detector) parent1);
            links.add(link);
            link = new DetectorAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((Detector) parent1);
            links.add(link);
            link = new DetectorAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((Detector) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((Detector) parent2);
                links.add(link);
                link = new DetectorAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((Detector) parent2);
                links.add(link);
                link = new DetectorAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((Detector) parent2);
                links.add(link);
                link = new DetectorAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((Detector) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof Detector) {
            DetectorAnnotationLink link = new DetectorAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((Detector) parent1);
            links.add(link);
            link = new DetectorAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((Detector) parent1);
            links.add(link);
            link = new DetectorAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((Detector) parent1);
            links.add(link);
            link = new DetectorAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((Detector) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((Detector) parent2);
                links.add(link);
                link = new DetectorAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((Detector) parent2);
                links.add(link);
                link = new DetectorAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((Detector) parent2);
                links.add(link);
                link = new DetectorAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((Detector) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof LightSource) {
            LightSourceAnnotationLink link = new LightSourceAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((LightSource) parent1);
            links.add(link);
            link = new LightSourceAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((LightSource) parent1);
            links.add(link);
            link = new LightSourceAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((LightSource) parent1);
            links.add(link);
            link = new LightSourceAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((LightSource) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((LightSource) parent2);
                links.add(link);
                link = new LightSourceAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((LightSource) parent2);
                links.add(link);
                link = new LightSourceAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((LightSource) parent2);
                links.add(link);
                link = new LightSourceAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((LightSource) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof Instrument) {
            InstrumentAnnotationLink link = new InstrumentAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((Instrument) parent1);
            links.add(link);
            link = new InstrumentAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((Instrument) parent1);
            links.add(link);
            link = new InstrumentAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((Instrument) parent1);
            links.add(link);
            link = new InstrumentAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((Instrument) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((Instrument) parent2);
                links.add(link);
                link = new InstrumentAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((Instrument) parent2);
                links.add(link);
                link = new InstrumentAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((Instrument) parent2);
                links.add(link);
                link = new InstrumentAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((Instrument) parent2);
                links.add(link);
            }
        } else if (parent1 instanceof OriginalFile) {
            OriginalFileAnnotationLink link = new OriginalFileAnnotationLinkI();
            link.setChild(new TagAnnotationI(c.getId().getValue(), false));
            link.setParent((OriginalFile) parent1);
            links.add(link);
            link = new OriginalFileAnnotationLinkI();
            link.setChild(new TermAnnotationI(t.getId().getValue(), false));
            link.setParent((OriginalFile) parent1);
            links.add(link);
            link = new OriginalFileAnnotationLinkI();
            link.setChild(new FileAnnotationI(f.getId().getValue(), false));
            link.setParent((OriginalFile) parent1);
            links.add(link);
            link = new OriginalFileAnnotationLinkI();
            link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
            link.setParent((OriginalFile) parent1);
            links.add(link);
            if (parent2 != null) {
                link.setChild(new TagAnnotationI(c.getId().getValue(), false));
                link.setParent((OriginalFile) parent2);
                links.add(link);
                link = new OriginalFileAnnotationLinkI();
                link.setChild(new TermAnnotationI(t.getId().getValue(), false));
                link.setParent((OriginalFile) parent2);
                links.add(link);
                link = new OriginalFileAnnotationLinkI();
                link.setChild(new FileAnnotationI(f.getId().getValue(), false));
                link.setParent((OriginalFile) parent2);
                links.add(link);
                link = new OriginalFileAnnotationLinkI();
                link.setChild(new MapAnnotationI(ma.getId().getValue(), false));
                link.setParent((OriginalFile) parent2);
                links.add(link);
            }
        } else {
            throw new UnsupportedOperationException("Unknown parent type: " + parent1);
        }
        if (links.size() > 0)
            iUpdate.saveAndReturnArray(links);
        return ids;
    }

    /**
     * Creates various non sharable annotations.
     *
     * @param parent
     *            The object to link the annotation to.
     * @param ns
     *            The name space or <code>null</code>.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected List<Long> createNonSharableAnnotation(IObject parent, String ns)
            throws Exception {
        // Copying to a proxy to prevent issues with parent.annotationLinks
        // becoming stale on multiple copies.
        parent = parent.proxy();

        // creation already tested in UpdateServiceTest
        List<Long> ids = new ArrayList<Long>();
        CommentAnnotation c = new CommentAnnotationI();
        c.setTextValue(ode.rtypes.rstring("comment"));
        if (ns != null)
            c.setNs(ode.rtypes.rstring(ns));

        c = (CommentAnnotation) iUpdate.saveAndReturnObject(c);

        LongAnnotation l = new LongAnnotationI();
        l.setLongValue(ode.rtypes.rlong(1L));
        if (ns != null)
            l.setNs(ode.rtypes.rstring(ns));

        l = (LongAnnotation) iUpdate.saveAndReturnObject(l);

        BooleanAnnotation b = new BooleanAnnotationI();
        b.setBoolValue(ode.rtypes.rbool(true));
        if (ns != null)
            b.setNs(ode.rtypes.rstring(ns));

        b = (BooleanAnnotation) iUpdate.saveAndReturnObject(b);
        
        ids.add(c.getId().getValue());
        ids.add(l.getId().getValue());
        ids.add(b.getId().getValue());
        
        List<IObject> links = new ArrayList<IObject>();
        if (parent instanceof Image) {
            ImageAnnotationLink link = new ImageAnnotationLinkI();
            link.setChild(c);
            link.setParent((Image) parent);
            links.add(link);
            link = new ImageAnnotationLinkI();
            link.setChild(l);
            link.setParent((Image) parent);
            links.add(link);
            link = new ImageAnnotationLinkI();
            link.setChild(b);
            link.setParent((Image) parent);
            links.add(link);
        } else if (parent instanceof Project) {
            ProjectAnnotationLink link = new ProjectAnnotationLinkI();
            link.setChild(c);
            link.setParent((Project) parent);
            links.add(link);
            link = new ProjectAnnotationLinkI();
            link.setChild(l);
            link.setParent((Project) parent);
            links.add(link);
            link = new ProjectAnnotationLinkI();
            link.setChild(b);
            link.setParent((Project) parent);
            links.add(link);
        } else if (parent instanceof Dataset) {
            DatasetAnnotationLink link = new DatasetAnnotationLinkI();
            link.setChild(c);
            link.setParent((Dataset) parent);
            links.add(link);
            link = new DatasetAnnotationLinkI();
            link.setChild(l);
            link.setParent((Dataset) parent);
            links.add(link);
            link = new DatasetAnnotationLinkI();
            link.setChild(b);
            link.setParent((Dataset) parent);
            links.add(link);
        } else if (parent instanceof Plate) {
            PlateAnnotationLink link = new PlateAnnotationLinkI();
            link.setChild(c);
            link.setParent((Plate) parent);
            links.add(link);
            link = new PlateAnnotationLinkI();
            link.setChild(l);
            link.setParent((Plate) parent);
            links.add(link);
            link = new PlateAnnotationLinkI();
            link.setChild(b);
            link.setParent((Plate) parent);
            links.add(link);
        } else if (parent instanceof Screen) {
            ScreenAnnotationLink link = new ScreenAnnotationLinkI();
            link.setChild(c);
            link.setParent((Screen) parent);
            links.add(link);
            link = new ScreenAnnotationLinkI();
            link.setChild(l);
            link.setParent((Screen) parent);
            links.add(link);
            link = new ScreenAnnotationLinkI();
            link.setChild(b);
            link.setParent((Screen) parent);
            links.add(link);
        } else if (parent instanceof Well) {
            WellAnnotationLink link = new WellAnnotationLinkI();
            link.setChild(c);
            link.setParent((Well) parent);
            links.add(link);
            link = new WellAnnotationLinkI();
            link.setChild(l);
            link.setParent((Well) parent);
            links.add(link);
            link = new WellAnnotationLinkI();
            link.setChild(b);
            link.setParent((Well) parent);
            links.add(link);
        } else if (parent instanceof PlateAcquisition) {
            PlateAcquisitionAnnotationLink link = new PlateAcquisitionAnnotationLinkI();
            link.setChild(c);
            link.setParent((PlateAcquisition) parent);
            links.add(link);
            link = new PlateAcquisitionAnnotationLinkI();
            link.setChild(l);
            link.setParent((PlateAcquisition) parent);
            links.add(link);
            link = new PlateAcquisitionAnnotationLinkI();
            link.setChild(b);
            link.setParent((PlateAcquisition) parent);
            links.add(link);
        } else if (parent instanceof Detector) {
            DetectorAnnotationLink link = new DetectorAnnotationLinkI();
            link.setChild(c);
            link.setParent((Detector) parent);
            links.add(link);
            link = new DetectorAnnotationLinkI();
            link.setChild(l);
            link.setParent((Detector) parent);
            links.add(link);
            link = new DetectorAnnotationLinkI();
            link.setChild(b);
            link.setParent((Detector) parent);
            links.add(link);
        } else if (parent instanceof Instrument) {
            InstrumentAnnotationLink link = new InstrumentAnnotationLinkI();
            link.setChild(c);
            link.setParent((Instrument) parent);
            links.add(link);
            link = new InstrumentAnnotationLinkI();
            link.setChild(l);
            link.setParent((Instrument) parent);
            links.add(link);
            link = new InstrumentAnnotationLinkI();
            link.setChild(b);
            link.setParent((Instrument) parent);
            links.add(link);
        } else if (parent instanceof LightSource) {
            LightSourceAnnotationLink link = new LightSourceAnnotationLinkI();
            link.setChild(c);
            link.setParent((LightSource) parent);
            links.add(link);
            link = new LightSourceAnnotationLinkI();
            link.setChild(l);
            link.setParent((LightSource) parent);
            links.add(link);
            link = new LightSourceAnnotationLinkI();
            link.setChild(b);
            link.setParent((LightSource) parent);
            links.add(link);
        } else {
            throw new UnsupportedOperationException("Unknown parent type: " + parent);
        }
        if (links.size() > 0)
            iUpdate.saveAndReturnArray(links);
        return ids;
    }

    /**
     * Create a new unpersisted experimenter with the given field values.
     * @param odeName a Bhojpur ODE name
     * @param firstName a first name
     * @param lastName a last time
     * @return the new experimenter
     */
    protected Experimenter createExperimenterI(String odeName, String firstName, String lastName) {
        final Experimenter experimenter = new ExperimenterI();
        experimenter.setOdeName(rtypes.rstring(odeName));
        experimenter.setFirstName(rtypes.rstring(firstName));
        experimenter.setLastName(rtypes.rstring(lastName));
        experimenter.setLdap(rtypes.rbool(false));
        return experimenter;
    }

    /**
     * Refresh a folder.
     * @param folder the folder to refresh
     * @return the same folder refreshed with its child folder and image link collections loaded
     * @throws ServerError unexpected
     */
    protected Folder returnFolder(Folder folder) throws ServerError {
        final String query =
                "FROM Folder AS f LEFT OUTER JOIN FETCH f.childFolders LEFT OUTER JOIN FETCH f.imageLinks WHERE f.id = :id";
        final Parameters params = new ParametersI().addId(folder.getId().getValue());
        return (Folder) iQuery.findByQuery(query, params);
    }

    /**
     * Save and refresh a folder.
     * @param folder the folder to save and refresh
     * @return the same folder refreshed with its child folder and image link collections loaded
     * @throws ServerError unexpected
     */
    protected Folder saveAndReturnFolder(Folder folder) throws ServerError {
        folder = (Folder) iUpdate.saveAndReturnObject(folder);
        return returnFolder(folder);
    }

    /**
     * Modifies the graph.
     *
     * @param change
     *            The object hosting information about data to modify.
     * @return See above.
     * @throws Exception
     */
    protected Response doChange(Request change) throws Exception {
        return doChange(client, factory, change, true);
    }

    /**
     * Modifies the graph.
     *
     * @param change
     *            The object hosting information about data to modify.
     * @return See above.
     * @throws Exception
     */
    protected Response doChange(Request change, long groupID) throws Exception {
        return doChange(client, factory, change, true, groupID, null);
    }

    protected Response doChange(ode.client c, ServiceFactoryPrx f,
            Request change, boolean pass) throws Exception {
        return doChange(c, f, change, pass, null, null);
    }

    protected Response doAllChanges(ode.client c, ServiceFactoryPrx f,
            boolean pass, Request... changes) throws Exception {
        DoAll all = new DoAll();
        all.requests = new ArrayList<Request>();
        all.requests.addAll(Arrays.asList(changes));
        return doChange(c, f, all, pass);
    }

    /**
     *
     * @param c
     * @param f
     * @param change
     * @param pass
     * @return
     * @throws Exception
     */
    protected Response doChange(ode.client c, ServiceFactoryPrx f,
            Request change, boolean pass, Long groupID, Integer scalingFactorAdjustment) throws Exception {
        final Map<String, String> callContext = new HashMap<String, String>();
        if (groupID != null) {
            callContext.put("ode.group", "" + groupID);
        }
        final HandlePrx prx = f.submit(change, callContext);
        // assertFalse(prx.getStatus().flags.contains(State.FAILURE));
        CmdCallbackI cb = new CmdCallbackI(c, prx);
        long useScalingFactor = scalingFactor;
        if (scalingFactorAdjustment != null) {
            useScalingFactor *= scalingFactorAdjustment;
        }
        cb.loop(20, useScalingFactor);
        return assertCmd(cb, pass);
    }

    protected CmdCallbackI callback(boolean passes, ode.client c,
            ode.cmd.Request... reqs) throws ApiUsageException, ServerError,
            InterruptedException {
        DoAll all = new DoAll();
        all.requests = new ArrayList<ode.cmd.Request>();
        for (ode.cmd.Request req : reqs) {
            all.requests.add(req);
        }
        HandlePrx handle = c.getSession().submit(all);
        CmdCallbackI cb = new CmdCallbackI(c, handle);
        cb.loop(10 * reqs.length, scalingFactor); // throws on timeout
        assertCmd(cb, passes);
        return cb;
    }

    protected Response assertCmd(CmdCallbackI cb, boolean pass) {
        Status status = cb.getStatus();
        Response rsp = cb.getResponse();
        Assert.assertNotNull(rsp);
        if (pass) {
            if (rsp instanceof ERR) {
                ERR err = (ERR) rsp;
                String name = err.getClass().getSimpleName();
                Assert.fail(String.format(
                        "Found %s when pass==true: %s (%s) params=%s", name,
                        err.category, err.name, err.parameters));
            }
            Assert.assertFalse(status.flags.contains(State.FAILURE));
        } else {
            if (rsp instanceof OK) {
                OK ok = (OK) rsp;
                Assert.fail(String.format("Found OK when pass==false: %s", ok));
            }
            Assert.assertTrue(status.flags.contains(State.FAILURE));
        }
        return rsp;
    }

    /**
     * Creates a new group with the specified permissions and sets the role of
     * the user.
     *
     * @param permissions
     *            The permissions of the group.
     * @param userRole
     *            The role of the user e.g. group owner.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    protected void login(String permissions, int userRole) throws Exception {
        newUserAndGroup(permissions);
        switch (userRole) {
            case GROUP_OWNER:
                makeGroupOwner();
                break;
            case ADMIN:
                logRootIntoGroup();
        }
    }

    /**
     * Convenient helper function for providing Boolean arguments to TestNG tests.
     * @param argCount how many arguments the test takes
     * @return every combination of argument values
     * @see ode.testing.DataProviderBuilder#addBoolean(boolean)
     */
    private static Boolean[][] provideEveryBooleanCombination(int argCount) {
        return Lists.cartesianProduct(Collections.nCopies(argCount, ImmutableList.of(false, true)))
                .stream().map(args -> args.stream().toArray(Boolean[]::new)).toArray(Boolean[][]::new);
    }

    /**
     * @return all four combinations of Boolean argument values
     */
    @DataProvider(name = "test cases using two Boolean arguments")
    public Object[][] provideTwoBooleanArguments() {
        return provideEveryBooleanCombination(2);
    }

    /**
     * @return all eight combinations of Boolean argument values
     */
    @DataProvider(name = "test cases using three Boolean arguments")
    public Object[][] provideThreeBooleanArguments() {
        return provideEveryBooleanCombination(3);
    }

    /**
     * @return all sixteen combinations of Boolean argument values
     */
    @DataProvider(name = "test cases using four Boolean arguments")
    public Object[][] provideFourBooleanArguments() {
        return provideEveryBooleanCombination(4);
    }

    /**
     * Override the Ice implicit call context with a group ID.
     * Removes the override upon closing.
     */
    protected class ImplicitGroupContext implements AutoCloseable {
        /**
         * Set the implicit group context to the given group.
         * @param groupId a group ID
         */
        ImplicitGroupContext(long groupId) {
            if (client.getImplicitContext().containsKey(Login.ODE_GROUP)) {
                throw new IllegalStateException("group context already set");
            }
            client.getImplicitContext().put(Login.ODE_GROUP, Long.toString(groupId));
        }

        /**
         * Set the implicit group context.
         * @param groupId a group ID
         */
        ImplicitGroupContext(RLong groupId) {
            this(groupId.getValue());
        }

        @Override
        public void close() {
            if (!client.getImplicitContext().containsKey(Login.ODE_GROUP)) {
                throw new IllegalStateException("group context no longer set");
            }
            client.getImplicitContext().remove(Login.ODE_GROUP);
        }
    }

    /**
     * Override the Ice implicit call context with all-groups.
     * Removes the override upon closing.
     */
    protected class ImplicitAllGroupsContext extends ImplicitGroupContext {
        /**
         * Set the implicit group context to all-groups.
         * @param groupId a group ID
         */
        ImplicitAllGroupsContext() {
            super(-1);
        }
    }

    /**
     * Convenient method which adds a user to the current group and logs the
     * user.
     * Depending on the specified parameters, the user will become a group owner
     * or an admin.
     * @param user1
     * @param isAdmin
     * @param isGroupOwner
     * @return
     * @throws Exception
     */
    protected EventContext addUserAndLogin(EventContext user1, boolean isAdmin, boolean isGroupOwner) throws Exception {
        // Login as new user
        EventContext newUser = addUser(user1, isAdmin, isGroupOwner);
        loginUser(newUser);
        return newUser;
    }

    /**
     * Convenient method which adds a user to the current group.
     * Depending on the specified parameters, the user will become a group owner
     * or an admin.
     * @param user1
     * @param isAdmin
     * @param isGroupOwner
     * @return
     * @throws Exception
     */
    protected EventContext addUser(EventContext user1, boolean isAdmin, boolean isGroupOwner) throws Exception {
        // Create new user in group and login as that user
        EventContext newUser = newUserInGroup(user1, isGroupOwner);
        if (isAdmin) {
            // If user is an admin, add them to the system group
            ExperimenterGroup systemGroup = new ExperimenterGroupI(iAdmin.getSecurityRoles().systemGroupId, false);
            addUsers(systemGroup, Collections.singletonList(newUser.userId), false);
        }
        return newUser;
    }
}
