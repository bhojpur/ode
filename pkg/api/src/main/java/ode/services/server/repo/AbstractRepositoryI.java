package ode.services.server.repo;

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
import java.nio.channels.OverlappingFileLockException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Transactional;

import Ice.Current;
import Ice.ObjectAdapter;
import ode.services.server.fire.Registry;
import ode.services.messages.DeleteLogMessage;
import ode.services.messages.DeleteLogsMessage;
import ode.services.util.Executor;
import ode.services.util.ReadOnlyStatus;
import ode.system.Principal;
import ode.system.ServiceFactory;
import ode.util.SqlAction;
import ode.util.SqlAction.DeleteLog;
import ode.util.messages.InternalMessage;
import ode.ServerError;
import ode.api.RawFileStorePrx;
import ode.api.RawPixelsStorePrx;
import ode.api.RenderingEnginePrx;
import ode.api.ThumbnailStorePrx;
import ode.cmd.Response;
import ode.constants.SESSIONUUID;
import ode.grid.InternalRepositoryPrx;
import ode.grid.RawAccessRequest;
import ode.grid.RepositoryPrx;
import ode.grid.RepositoryPrxHelper;
import ode.grid._InternalRepositoryDisp;
import ode.model.OriginalFile;
import ode.model.OriginalFileI;
import ode.util.IceMapper;

/**
 * Base repository class responsible for properly handling directory
 * {@link #takeover() takeover} and other lifecycle tasks. Individual instances
 * will be responsible for providing the other service instances which are
 * returned from this service.
 */
public abstract class AbstractRepositoryI extends _InternalRepositoryDisp
    implements ApplicationListener<InternalMessage> {

    private final static Logger log = LoggerFactory.getLogger(AbstractRepositoryI.class);

    private final Ice.ObjectAdapter oa;

    private final Registry reg;

    private final Executor ex;

    private final Principal p;

    private final FileMaker fileMaker;

    private final ReadOnlyStatus readOnly;

    private final PublicRepositoryI servant;

    private OriginalFile description;

    private RepositoryPrx proxy;

    private String repoUuid;

    private volatile AtomicReference<State> state = new AtomicReference<State>();

    private enum State {
        ACTIVE, EAGER, WAITING, CLOSED;
    }

    @Deprecated
    public AbstractRepositoryI(Ice.ObjectAdapter oa, Registry reg, Executor ex,
            Principal p, String repoDir, PublicRepositoryI servant) {
        this(oa, reg, ex, p, repoDir, new ReadOnlyStatus(false, false), servant);
        log.info("assuming read-write repository");
    }

    @Deprecated
    public AbstractRepositoryI(Ice.ObjectAdapter oa, Registry reg, Executor ex,
            Principal p, FileMaker fileMaker, PublicRepositoryI servant) {
        this(oa, reg, ex, p, fileMaker, new ReadOnlyStatus(false, false), servant);
        log.info("assuming read-write repository");
    }

    public AbstractRepositoryI(Ice.ObjectAdapter oa, Registry reg, Executor ex,
            Principal p, String repoDir, ReadOnlyStatus readOnly, PublicRepositoryI servant) {
        this(oa, reg, ex, p, new FileMaker(repoDir), readOnly, servant);
    }

    public AbstractRepositoryI(Ice.ObjectAdapter oa, Registry reg, Executor ex,
            Principal p, FileMaker fileMaker, ReadOnlyStatus readOnly, PublicRepositoryI servant) {
        this.state.set(State.EAGER);
        this.p = p;
        this.oa = oa;
        this.ex = ex;
        this.reg = reg;
        this.fileMaker = fileMaker;
        this.readOnly = readOnly;
        this.servant = servant;
        log.info("Initializing repository in " + fileMaker.getDir());
    }

    /**
     * Called when this repository is creating a new {@link OriginalFile}
     * repository object.
     */
    public String generateRepoUuid() {
        return UUID.randomUUID().toString();
    }

    public void onApplicationEvent(InternalMessage im) {
        if (im instanceof DeleteLogMessage) {
            handleDLMs(Arrays.asList((DeleteLogMessage) im));
        } else if (im instanceof DeleteLogsMessage) {
            handleDLMs(((DeleteLogsMessage) im).getMessages());
        }
    }

    private void handleDLMs(List<DeleteLogMessage> dlms) {

        final Ice.Current rootCurrent = new Ice.Current();
        rootCurrent.ctx = new HashMap<String, String>();
        rootCurrent.ctx.put(SESSIONUUID.value, p.toString());
        final RepositoryDao dao = servant.repositoryDao;
        final List<DeleteLog> templates = new ArrayList<DeleteLog>();
        for (DeleteLogMessage dlm : dlms) {
            final DeleteLog template = new DeleteLog();
            template.repo = repoUuid; // Ourselves!
            template.fileId = dlm.getFileId();
            templates.add(template);
        }

        // Length matches that of dlms
        final List<List<DeleteLog>> logs = dao.findRepoDeleteLogs(templates,
                rootCurrent);

        final Map<DeleteLog, Integer> successes =
                new HashMap<DeleteLog, Integer>();

        for (int i = 0; i < dlms.size(); i++) {
            final DeleteLogMessage dlm = dlms.get(i);
            final List<DeleteLog> dls = logs.get(i);

            for (DeleteLog dl : dls) {
                // Copied from RawAccessRequestI.local
                String filename = dl.path + "/" + dl.name;
                if (filename.startsWith("/")) {
                    filename = "." + filename;
                }
                try {
                    final CheckedPath checked = servant.checkPath(filename, null, null /* i.e. as admin*/);
                    if (checked.delete()) {
                        log.debug("DELETED: {}", checked);
                    } else {
                        Throwable t = new ode.grid.FileDeleteException(
                                null, null, "Delete file failed: " + filename);
                        dlm.error(dl, t);
                    }
                } catch (Throwable t) {
                    log.warn("Failed to delete log " + dl, t);
                    dlm.error(dl, t);
                }
                if (!dlm.isError(dl)) {
                    successes.put(dl, i);
                }
            }
        }

        // Only remove the logs if req.local was successful
        List<DeleteLog> copies = new ArrayList<DeleteLog>(successes.keySet());
        List<Integer> counts = dao.deleteRepoDeleteLogs(copies, rootCurrent);
        for (int i = 0; i < copies.size(); i++) {
            DeleteLog copy = copies.get(i);
            Integer index = successes.get(copy);
            DeleteLogMessage dlm = dlms.get(index);
            int expected = logs.get(index).size();
            int actual = counts.get(i);
            if (actual != expected) {
                log.warn(String.format(
                    "Failed to remove all delete log entries: %s instead of %s",
                    actual, expected));
            }
            dlm.success(copy);
        }
    }

    /**
     * Method called in a background thread which may end up waiting
     * indefinitely on the repository lock file
     * ("${ode.data.dir}/.ode/repository/${ode.db.uuid}/repo_uuid").
     */
    public boolean takeover() {

        if (!state.compareAndSet(State.EAGER, State.WAITING)) {
            log.debug("Skipping takeover: EAGER / WAITING");
            return false;
        }

        // All code paths after this point should guarantee that they set
        // the state to the proper code, since now no other thread can get
        // into this method.

        Object rv = null;
        try {
            final GetOrCreateRepo gorc;
            if (readOnly.isReadOnlyDb()) {
                gorc = new GetOrCreateRepo(this, "takeover (ro)") {
                    @Override
                    @Transactional(readOnly = true)
                    public Object doWork(Session session, ServiceFactory sf) {
                        return innerWork(sf);
                    }
                };
            } else {
                gorc = new GetOrCreateRepo(this, "takeover (rw)") {
                    @Override
                    @Transactional(readOnly = false)
                    public Object doWork(Session session, ServiceFactory sf) {
                        return innerWork(sf);
                    }
                };
            }
            rv = ex.execute(p, gorc);
            if (rv instanceof ode.model.core.OriginalFile) {

                ode.model.core.OriginalFile r = (ode.model.core.OriginalFile) rv;
                description = getDescription(r.getId());
                proxy = gorc.publicPrx;

                // Success
                if (!state.compareAndSet(State.WAITING, State.ACTIVE)) {
                    // But this may have been set to CLOSED
                    log.debug("Could not set state to ACTIVE");
                }
                return true;

            } else if (rv instanceof Exception) {
                log.error("Failed during repository takeover", (Exception) rv);
            } else {
                log.error("Unknown issue with repository takeover:" + rv);
            }
        } catch (Exception e) {
            log.error("Unexpected error in called executor on takeover", e);
        }

        state.compareAndSet(State.WAITING, State.EAGER);
        return false;

    }

    public void close() {
        state.set(State.CLOSED);
        log.info("Releasing " + fileMaker.getDir());
        fileMaker.close();
    }

    public final String getRepoUuid() {
        return repoUuid;
    }

    public final Ice.Communicator getCommunicator() {
        return oa.getCommunicator();
    }

    public final ObjectAdapter getObjectAdapter() {
        return oa;
    }

    public final OriginalFile getDescription(Current __current) {
        return description;
    }

    public final RepositoryPrx getProxy(Current __current) {
        return proxy;
    }

    public Response rawAccess(RawAccessRequest req, Current __current) throws ServerError {
        if (!(req instanceof RawAccessRequestI)) {
            return new ode.cmd.ERR();
        }
        try {
            ((RawAccessRequestI) req).local(this, servant, __current);
            return new ode.cmd.OK();
        } catch (Throwable t) {
            throw new IceMapper().handleServerError(t, servant.context);
        }
    }

    public abstract String getFilePath(final OriginalFile file,
            Current __current) throws ServerError;

    // UNIMPLEMENTED
    // =========================================================================

    public RawFileStorePrx createRawFileStore(OriginalFile file,
            Current __current) {
        return null;
    }

    public RawPixelsStorePrx createRawPixelsStore(OriginalFile file,
            Current __current) {
        // TODO Auto-generated method stub
        return null;
    }

    public RenderingEnginePrx createRenderingEngine(OriginalFile file,
            Current __current) {
        // TODO Auto-generated method stub
        return null;
    }

    public ThumbnailStorePrx createThumbnailStore(OriginalFile file,
            Current __current) {
        // TODO Auto-generated method stub
        return null;
    }

    // Helpers
    // =========================================================================

    /**
     * Action class for either looking up the repository for this instance, or
     * if it doesn't exist, creating it. This is the bulk of the logic for the
     * {@link AbstractRepositoryI#takeover()} method, but doesn't deal with the
     * atomic locking of {@link AbstractRepositoryI#state} nor error handling.
     * Instead it simple returns an {@link Exception} ("failure") or null
     * ("success").
     */
    private abstract class GetOrCreateRepo extends Executor.SimpleWork {

        private ServiceFactory sf;

        RepositoryPrx publicPrx;

        private GetOrCreateRepo(Object object, String method) {
            super(object, method);
        }

        protected Object innerWork(ServiceFactory sf) {
            try {
                this.sf = sf;
                final String line = handleFileMaker();
                final ode.model.core.OriginalFile r = handleRepository(line);
                handleServants(r);
                return r;
            } catch (Exception e) {
                fileMaker.close();  // If anything goes awry, we release for others!
                return e;
            }
        }

        private String handleFileMaker() throws Exception {
            if (fileMaker.needsInit()) {
                fileMaker.init(sf.getConfigService().getDatabaseUuid(), readOnly.isReadOnlyRepo());
            }

            String line = null;
            try {
                line = fileMaker.getLine();
            } catch (OverlappingFileLockException ofle) {
                InternalRepositoryPrx[] repos = reg.lookupRepositories();
                InternalRepositoryPrx prx = null;
                if (repos != null) {
                    for (int i = 0; i < repos.length; i++) {
                        if (repos[i] != null) {
                            if (repos[i].toString().contains(repoUuid)) {
                                prx = repos[i];
                            }
                        }
                    }
                }
                if (prx == null) {
                    fileMaker.close();
                    FileMaker newFileMaker = new FileMaker(new File(
                            fileMaker.getDir()).getAbsolutePath());
                    fileMaker.init(sf.getConfigService().getDatabaseUuid(), readOnly.isReadOnlyRepo());
                    line = newFileMaker.getLine();
                }
            }
            return line;
        }

        private ode.model.core.OriginalFile handleRepository(String line) throws Exception {
            if (line == null) {
                repoUuid = generateRepoUuid();
            } else {
                repoUuid = line;
            }

            ode.model.core.OriginalFile r = sf.getQueryService()
                    .findByString(ode.model.core.OriginalFile.class, "hash", repoUuid);

            if (!(readOnly.isReadOnlyDb() || readOnly.isReadOnlyRepo())) {
                r = handleRepoChanges(r, line);
            }

            if (r == null) {
                throw new NullPointerException("No repository to open!");
            }

            log.info(String.format("Opened repository %s (uuid=%s)",
                    r.getName(), repoUuid));
            return r;
        }

        private ode.model.core.OriginalFile handleRepoChanges(ode.model.core.OriginalFile r, String line) throws Exception {
            final String path = FilenameUtils.normalize(
                    new File(fileMaker.getDir()).getAbsolutePath());
            final String pathName = FilenameUtils.getName(path);
            final String pathDir = FilenameUtils.getFullPath(path);
            if (r == null) {
                if (line != null) {
                    log.warn("Couldn't find repository object: " + line);
                }

                r = new ode.model.core.OriginalFile();
                r.setHash(repoUuid);
                r.setName(pathName);
                r.setPath(pathDir);
                Timestamp t = new Timestamp(System.currentTimeMillis());
                r.setAtime(t);
                r.setMtime(t);
                r.setCtime(t);
                r.setMimetype("Repository"); //
                r.setSize(0L);
                r = sf.getUpdateService().saveAndReturnObject(r);
                fileMaker.writeLine(repoUuid);
                log.info(String.format(
                        "Registered new repository %s (uuid=%s)",
                        r.getName(), repoUuid));
            } else if (!r.getPath().equals(pathDir) ||
                       !r.getName().equals(pathName)) {
                final String oldPath = r.getPath();
                final String oldName = r.getName();
                r.setPath(pathDir);
                r.setName(pathName);
                r = sf.getUpdateService().saveAndReturnObject(r);
                log.warn("Data directory moved: {}{} updated to {}{}",
                        oldPath, oldName, pathDir, pathName);
            }

            // only adds if necessary
            sf.getAdminService().moveToCommonSpace(r);

            return r;
        }

        private void handleServants(ode.model.core.OriginalFile r) throws Exception {
            //
            // Servants
            //

            servant.initialize(fileMaker, r.getId(), repoUuid);

            LinkedList<Ice.ObjectPrx> objs = new LinkedList<Ice.ObjectPrx>();
            objs.add(addOrReplace("InternalRepository-", AbstractRepositoryI.this));
            objs.add(addOrReplace("PublicRepository-", servant.tie()));
            publicPrx = RepositoryPrxHelper.uncheckedCast(objs.getLast());

            //
            // Activation & Registration
            //
            oa.activate(); // Must happen before the registry tries to connect

            for (Ice.ObjectPrx prx : objs) {
                reg.addObject(prx);
            }

            log.info("Repository now active");
        }

        private Ice.ObjectPrx addOrReplace(String prefix, Ice.Object obj) {
            Ice.Identity id = Ice.Util.stringToIdentity(prefix + repoUuid);
            Object old = oa.find(id);
            if (old != null) {
                oa.remove(id);
                log.warn(String.format("Found %s; removing: %s", id, old));
            }
            oa.add(obj, id);
            return oa.createDirectProxy(id);
        }
    }

    protected OriginalFileI getDescription(final long id) throws ServerError {
        ode.model.core.OriginalFile file = (ode.model.core.OriginalFile) ex
                .execute(p,
                        new Executor.SimpleWork(this, "getDescription", id) {
                            @Transactional(readOnly = true)
                            public Object doWork(Session session,
                                    ServiceFactory sf) {
                                return sf.getQueryService().findByQuery(
                                        "select o from OriginalFile o "
                                                + "where o.id = " + id, null);
                            }
                        });
        OriginalFileI rv = (OriginalFileI) new IceMapper().map(file);
        return rv;

    }

    @SuppressWarnings("unchecked")
    protected String getFileRepo(final OriginalFile file) throws ServerError {

        if (file == null || file.getId() == null) {
            throw new ode.ValidationException(null, null, "Unmanaged file");
        }

        Map<String, Object> map = (Map<String, Object>) ex
                .executeSql(new Executor.SimpleSqlWork(this,
                        "getFileRepo") {
                    @Transactional(readOnly = true)
                    public Object doWork(SqlAction sql) {
                        return sql.repoFile(file.getId().getValue());
                    }
                });

        if (map.size() == 0) {
            throw new ode.ValidationException(null, null, "Unknown file: "
                    + file.getId().getValue());
        }

        return (String) map.get("repo");

    }

}