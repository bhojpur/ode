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
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.google.common.collect.ImmutableList;

import ode.services.server.repo.path.FsFile;

import ode.RString;
import ode.SecurityViolation;
import ode.ServerError;
import ode.grid.ImportLocation;
import ode.model.Dataset;
import ode.model.IObject;
import ode.model.Image;
import ode.model.OriginalFile;
import ode.model.Permissions;
import ode.model.PermissionsI;
import ode.model.Session;
import ode.sys.EventContext;
import ode.sys.ParametersI;
import ode.sys.Principal;
import ode.util.TempFileManager;

/**
 * Helper methods class for supporting of "new role" workflow
 * and security tests.
 */
public class RolesTests extends AbstractServerImportTest {

    private static final TempFileManager TEMPORARY_FILE_MANAGER = new TempFileManager(
            "test-" + LightAdminRolesTest.class.getSimpleName());

    protected File fakeImageFile = null;

    /**
     * Create a fake image file for use in import tests.
     * @throws IOException unexpected
     */
    @BeforeClass
    public void createFakeImageFile() throws IOException {
        final File temporaryDirectory = TEMPORARY_FILE_MANAGER.createPath("images", null, true);
        fakeImageFile = new File(temporaryDirectory, "image.fake");
        fakeImageFile.createNewFile();
    }

    /**
     * Clear the instance fields that were set before running this class' tests.
     */
    @AfterClass
    public void teardown() {
        fakeImageFile = null;
    }

    /* These permissions do not permit anything.*/
    @SuppressWarnings("serial")
    private static final Permissions NO_PERMISSIONS = new PermissionsI("------") {
        @Override
        public boolean isDisallow(int restriction, Ice.Current c) {
            return true;
        }
    };

    /**
     * Get the current permissions for the given object.
     * @param object a model object previously retrieved from the server
     * @return the permissions for the object in the current context
     * @throws ServerError if the query caused a server error
     * (except for security violations, returns NO_PERMISSIONS)
     */
    protected Permissions getCurrentPermissions(IObject object) throws ServerError {
        final String objectClass = object.getClass().getSuperclass().getSimpleName();
        final long objectId = object.getId().getValue();
        try {
            final IObject objectRetrieved;
            if (objectClass.endsWith("Link")) {
                objectRetrieved = iQuery.findByQuery("FROM " + objectClass + " link JOIN FETCH link.child WHERE link.id = :id",
                        new ParametersI().addId(objectId), ALL_GROUPS_CONTEXT);
            } else {
                objectRetrieved = iQuery.get(objectClass, objectId, ALL_GROUPS_CONTEXT);
            }
            if (objectRetrieved == null) {
                return NO_PERMISSIONS;
            }
            return objectRetrieved.getDetails().getPermissions();
        } catch (SecurityViolation sv) {
            return NO_PERMISSIONS;
        }
    }

    /**
     * Import an image with original file into a given dataset.
     * @param dataset dataset to which to import the image if not null
     * @return the original file and the imported image
     * @throws Exception if the import fails
     */
    protected List<IObject> importImageWithOriginalFile(Dataset dataset) throws Exception {
        final String odeGroup = client.getImplicitContext().get(ode.constants.GROUP.value);
        final long currentGroupId = StringUtils.isBlank(odeGroup) ? iAdmin.getEventContext().groupId : Long.parseLong(odeGroup);
        final ImportLocation importLocation = importFileset(Collections.singletonList(fakeImageFile.getPath()), 1, dataset);
        final RString imagePath = ode.rtypes.rstring(importLocation.sharedPath + FsFile.separatorChar);
        final RString imageName = ode.rtypes.rstring(fakeImageFile.getName());
        String hql = "FROM OriginalFile o WHERE o.path = :path AND o.name = :name";
        final ParametersI params = new ParametersI().add("path", imagePath).add("name", imageName);
        if (currentGroupId >= 0) {
            hql += " AND o.details.group.id = :group_id";
            params.addLong("group_id", currentGroupId);
        }
        final OriginalFile remoteFile = (OriginalFile) iQuery.findByQuery(hql, params);
        final Image image = (Image) iQuery.findByQuery(
                "FROM Image WHERE fileset IN "
                + "(SELECT fileset FROM FilesetEntry WHERE originalFile.id = :id)",
                new ParametersI().addId(remoteFile.getId()));
        return ImmutableList.of(remoteFile, image);
    }

    /**
     * Sudo to the given user.
     * @param targetName the name of a user
     * @return context for a session owned by the given user
     * @throws Exception if the sudo could not be performed
     */
    protected EventContext sudo(String targetName) throws Exception {
        final Principal principal = new Principal();
        principal.name = targetName;
        final Session session = factory.getSessionService().createSessionWithTimeout(principal, 100 * 1000);
        final ode.client client = newOdeClient();
        final String sessionUUID = session.getUuid().getValue();
        client.createSession(sessionUUID, sessionUUID);
        return init(client);
    }
}
