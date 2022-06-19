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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ode.system.EventContext;
import ode.api.IQuery;
import ode.api.IUpdate;
import ode.api.JobHandle;
import ode.api.RawFileStore;
import ode.api.local.LocalAdmin;
import ode.conditions.InternalException;
import ode.io.nio.FileBuffer;
import ode.model.IObject;
import ode.model.fs.FilesetJobLink;
import ode.model.internal.Permissions.Right;
import ode.model.internal.Permissions.Role;
import ode.model.meta.Experimenter;
import ode.parameters.Parameters;
import ode.security.basic.OdeInterceptor;
import ode.services.RawFileBean;
import ode.services.server.repo.path.FsFile;
import ode.services.util.Executor;
import ode.system.Principal;
import ode.system.Roles;
import ode.system.ServiceFactory;
import ode.util.SqlAction;
import ode.util.SqlAction.DeleteLog;
import ode.RMap;
import ode.RType;
import ode.SecurityViolation;
import ode.ServerError;
import ode.ValidationException;
import ode.model.ChecksumAlgorithm;
import ode.model.Fileset;
import ode.model.Job;
import ode.model.OriginalFile;
import ode.model.OriginalFileI;
import ode.util.IceMapper;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.transaction.annotation.Transactional;

import Ice.Current;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;

/**
 * DAO class for encapsulating operations related to resource access inside the
 * repository. Methods return types already mapped from Ice. DAO is also used
 * in the form of a mock in unit tests.
 */
public class RepositoryDaoImpl implements RepositoryDao {

    private static class Rethrow extends InternalException {
        private final Throwable t;
        Rethrow(Throwable t) {
            super("rethrow!");
            this.t = t;
        }
    }

    private static abstract class StatefulWork<T> extends Executor.SimpleWork<T>
        implements Executor.StatefulWork {

        private final RawFileBean bean;
        StatefulWork(RawFileBean bean,
                Object self, String method, Object...args) {
            super(self, method, args);
            this.bean = bean;
        }

        public Object getThis() {
            return bean;
        }
    }

    /** Query to load the original file.*/
    private static final String LOAD_ORIGINAL_FILE =
    "select f from OriginalFile as f left outer join fetch f.hasher where ";

    /* query to load a user's institution */
    private static final String LOAD_USER_INSTITUTION =
            "SELECT institution FROM " + Experimenter.class.getName() + " WHERE id = :id";

    /* for large database queries */
    private static final int BATCH_SIZE = 1024;

    private final static Logger log = LoggerFactory.getLogger(RepositoryDaoImpl.class);

    private final IceMapper mapper = new IceMapper();

    protected final Principal principal;
    protected final Roles roles;
    protected final Executor executor;
    protected final Executor statefulExecutor;
    protected final OdeInterceptor interceptor;
    protected final String fileRepoSecretKey;

    /**
     * Primary constructor which takes all final fields.
     */
    public RepositoryDaoImpl(Principal principal, Roles roles,
            Executor executor, Executor statefulExecutor, OdeInterceptor interceptor, String fileRepoSecretKey) {
        this.principal = principal;
        this.roles = roles;
        this.executor = executor;
        this.statefulExecutor = statefulExecutor;
        this.interceptor = interceptor;
        this.fileRepoSecretKey = fileRepoSecretKey;
    }

    /**
     * Previous constructor which should no longer be used. Primarily for
     * simplicity of testing.
     */
    public RepositoryDaoImpl(Principal principal, Executor executor) {
        this(principal, new Roles(), executor,
                executor.getContext().getBean("statefulExecutor", Executor.class),
                executor.getContext().getBean("odeInterceptor", OdeInterceptor.class),
                UUID.randomUUID().toString());
    }

    /**
     * Loads
     * @return See above.
     */
    protected RawFileBean unwrapRawFileBean(RawFileStore proxy) {
        try {
            return (RawFileBean) ((Advised) proxy).getTargetSource().getTarget();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected SecurityViolation wrapSecurityViolation(
            ode.conditions.SecurityViolation sv) throws SecurityViolation {
        SecurityViolation copy = new SecurityViolation();
        IceMapper.fillServerError(copy, sv);
        throw copy;
    }

    public RawFileStore getRawFileStore(final long fileId, final CheckedPath checked,
            String mode, Ice.Current current) throws SecurityViolation {

        final RawFileStore proxy = executor.getContext()
                .getBean("managed-ode.api.RawFileStore", RawFileStore.class);
        final RawFileBean bean = unwrapRawFileBean(proxy);
        final FileBuffer buffer = checked.getFileBuffer(mode);
        try {
            Map<String, String> fileContext = fileContext(fileId, current);
            statefulExecutor.execute(fileContext, currentUser(current),
                new StatefulWork<Void>(bean, this,
                    "setFileIdWithBuffer", fileId, checked, mode) {
                    @Transactional(readOnly = true)
                    public Void doWork(Session session, ServiceFactory sf) {
                        bean.setFileIdWithBuffer(fileId, buffer);
                        return null;
                    }
                });
        } catch (ode.conditions.SecurityViolation sv) {
            throw wrapSecurityViolation(sv);
        }
        return proxy;
    }

    public OriginalFile findRepoFile(final String uuid, final CheckedPath checked,
            final String mimetype, Ice.Current current)
            throws ode.ServerError {

        try {
            ode.model.core.OriginalFile ofile =  executor
                    .execute(current.ctx, currentUser(current),
                            new Executor.SimpleWork<ode.model.core.OriginalFile>(this, "findRepoFile", uuid, checked, mimetype) {
                        @Transactional(readOnly = true)
                        public ode.model.core.OriginalFile doWork(Session session, ServiceFactory sf) {
                        return findRepoFile(sf, getSqlAction(), uuid, checked, mimetype);
                    }
                });
            return (OriginalFile) new IceMapper().map(ofile);
        } catch (ode.conditions.SecurityViolation sv) {
            throw wrapSecurityViolation(sv);
        }

    }

    public ode.model.core.OriginalFile findRepoFile(ServiceFactory sf,
            SqlAction sql, final String uuid, final CheckedPath checked,
            final String mimetype) {

        Long id = sql.findRepoFile(uuid,
                checked.getRelativePath(), checked.getName(),
                mimetype);
        if (id == null) {
            return null;
        } else {
            return sf.getQueryService().get(
                    ode.model.core.OriginalFile.class, id);
        }
    }

    public RMap treeList(final String repoUuid, final CheckedPath checked,
            Current current) throws ServerError {

        final RMap map = ode.rtypes.rmap();
        executor.execute(current.ctx, currentUser(current),
                new Executor.SimpleWork<Void>(this,
                "treeList", repoUuid, checked) {

            @Transactional(readOnly = true)
            public Void doWork(Session session, ServiceFactory sf) {
                _treeList(map, repoUuid, checked, sf, getSqlAction());
                return null;
            }
        });

        return map;
    }
    /**
     * Recursive descent for {@link PublicDirectoryI#treeList(String, Current)}.
     * This should only really be called on directories, but if it's accidentally
     * called on a non-directory, then we will treat it specially.
     *
     * @param rv The {@link RMap} which should be filled for a given level.
     * @param path
     * @param __current
     * @throws ServerError
     */
    private void _treeList(RMap rv, String repoUuid, CheckedPath checked,
            ServiceFactory sf, SqlAction sql) {

        final ode.model.core.OriginalFile file
            = findRepoFile(sf, sql, repoUuid, checked, null);

        if (file == null) {
            if (rv.getValue().size() == 0) {
                // This is likely the top-level search, and therefore
                // we can just exit.
                log.debug("No file found in _treeList: " + checked);
            } else {
                // In this case, we've been given data that's now
                // missing from the DB in the same transaction.
                // Shouldn't happen.
                log.warn("No file found in _treeList: " + checked);
            }
            return; // EARLY EXIT.
        }

        final String name = file.getName();
        final String mime = file.getMimetype();
        final Long size = file.getSize();
        final Long id = file.getId();

        final RMap subRv = ode.rtypes.rmap();
        final Map<String, RType> subVal = subRv.getValue();
        rv.put(name, subRv);
        subVal.put("id", ode.rtypes.rlong(id));
        subVal.put("mimetype", ode.rtypes.rstring(mime));
        if (size != null) {
            subVal.put("size", ode.rtypes.rlong(size));
        }

        if (file.getMimetype() != null && // FIXME: should be set!
                PublicRepositoryI.DIRECTORY_MIMETYPE.equals(file.getMimetype())) {

            // Now we recurse
            List<ode.model.core.OriginalFile> subFiles
                = getOriginalFiles(sf, sql, repoUuid, checked);
            final RMap subFilesRv = ode.rtypes.rmap();

            for (ode.model.core.OriginalFile subFile : subFiles) {
                CheckedPath child = null;
                try {
                    child = checked.child(subFile.getName());
                    if (child == null) {
                        // This should never happen.
                        throw new ode.ValidationException(null, null, "null child!");
                    }
                } catch (ode.ValidationException ve) {
                    // This can only really happen if the database has very odd
                    // information stored. Issuing a warning and then throwing
                    // an exception.
                    log.warn(String.format("Validation exception on %s.child(%s)",
                            checked, subFile.getName()), ve);
                    throw new ode.conditions.ValidationException(ve.getMessage());
                }

                _treeList(subFilesRv, repoUuid, child, sf, sql);
            }
            subVal.put("files", subFilesRv);
        }
    }

    public void createOrFixUserDir(final String repoUuid,
            final List<CheckedPath> checkedPaths,
            final Session s, final ServiceFactory sf, final SqlAction sql)
                    throws ServerError {

        final StopWatch outer = new Slf4JStopWatch();
        try {

            for (CheckedPath checked : checkedPaths) {

                CheckedPath parent;
                try {
                    parent = checked.parent();
                } catch (ValidationException ve) {
                    throw new RuntimeException(ve);
                }

                StopWatch sw = new Slf4JStopWatch();
                // Look for the dir in all groups (
                Long id = sql.findRepoFile(repoUuid, checked.getRelativePath(),
                        checked.getName());
                sw.stop("ode.repo.file.find");

                ode.model.core.OriginalFile f = null;
                if (id == null) {
                    // Doesn't exist. Create directory
                    sw = new Slf4JStopWatch();
                    // TODO: this whole method now looks quite similar to _internalRegister
                    f = _internalRegister(repoUuid,
                            Arrays.asList(checked), Arrays.asList(parent),
                            null, PublicRepositoryI.DIRECTORY_MIMETYPE,
                            sf, sql, s).get(0);
                    sw.stop("ode.repo.file.register");
                } else {
                    // Make sure the file is in the user group
                    try {
                        sw = new Slf4JStopWatch();
                        // Now that within one tx, likely cached.
                        f = sf.getQueryService().get(
                                ode.model.core.OriginalFile.class, id);
                        if (f != null) {
                            long groupId = f.getDetails().getGroup().getId();
                            if (roles.getUserGroupId() == groupId) {
                                // Null f, since it doesn't need to be reset.
                                f = null;
                            }
                        }
                        sw.stop("ode.repo.file.check_group");
                    }
                    catch (ode.conditions.SecurityViolation e) {
                        // If we aren't allowed to read the file, then likely
                        // it isn't in the user group so we will move it there.
                        f = new ode.model.core.OriginalFile(id, false);
                    }
                }

                if (f != null) {
                    sw = new Slf4JStopWatch();
                    ((LocalAdmin) sf.getAdminService())
                        .internalMoveToCommonSpace(f);
                    sw.stop("ode.repo.file.move_to_common");
                }

            }
        } catch (ode.conditions.SecurityViolation sv) {
            throw wrapSecurityViolation(sv);
        } finally {
            outer.stop("ode.repo.user_dir");
        }
    }

    public boolean canUpdate(final ode.model.IObject obj, Ice.Current current) {
        return executor.execute(current.ctx, currentUser(current),
                        new Executor.SimpleWork<Boolean>(this, "canUpdate") {
                    @Transactional(readOnly = true)
                    public Boolean doWork(Session session, ServiceFactory sf) {
                        try {
                            ode.model.IObject iobj = (ode.model.IObject)
                                new IceMapper().reverse(obj);
                            return sf.getAdminService().canUpdate(iobj);
                        } catch (Exception e) {
                            return false;
                        }
                    }
                });
    }

    public List<Long> filterFilesByRepository(final String repo, List<Long> ids, Ice.Current current) {
        final List<Long> inRepo = new ArrayList<Long>();
        for (final List<Long> idsBatch : Iterables.partition(ids, BATCH_SIZE)) {
            inRepo.addAll( executor
                    .execute(current.ctx, currentUser(current),
                            new Executor.SimpleWork<Collection<Long>>(this, "filterFilesByRepository") {
                        @Override
                        @Transactional(readOnly = true)
                        public List<Long> doWork(Session session, ServiceFactory sf) {
                            return getSqlAction().filterFileIdsByRepo(repo, idsBatch);
                        }
                    }));
        }
        return inRepo;
    }

    public OriginalFile getOriginalFile(final long repoId,
            final Ice.Current current) throws SecurityViolation {

         try {
             ode.model.core.OriginalFile oFile = executor
                 .execute(current.ctx, currentUser(current),
                         new Executor.SimpleWork<ode.model.core.OriginalFile>(this, "getOriginalFile", repoId) {
                     @Transactional(readOnly = true)
                     public ode.model.core.OriginalFile doWork(Session session, ServiceFactory sf) {
                        return sf.getQueryService().findByQuery(
                        LOAD_ORIGINAL_FILE+" f.id = :id",
                        new Parameters().addId(repoId));
                     }
                 });
             return (OriginalFileI) new IceMapper().map(oFile);
         } catch (ode.conditions.SecurityViolation sv) {
             throw wrapSecurityViolation(sv);
         }
     }

    @SuppressWarnings("unchecked")
    public List<OriginalFile> getOriginalFiles(final String repoUuid, final CheckedPath checked,
            final Ice.Current current) throws SecurityViolation {

         try {
             List<ode.model.core.OriginalFile> oFiles = (List<ode.model.core.OriginalFile>) executor
                .execute(current.ctx, currentUser(current),
                        new Executor.SimpleWork(this,
                        "getOriginalFiles", repoUuid, checked) {
                    @Transactional(readOnly = true)
                     public List<ode.model.core.OriginalFile> doWork(Session session, ServiceFactory sf) {
                         return getOriginalFiles(sf, getSqlAction(), repoUuid, checked);
                     }

                 });
             return (List<OriginalFile>) new IceMapper().map(oFiles);
         } catch (ode.conditions.SecurityViolation sv) {
             throw wrapSecurityViolation(sv);
         }
    }

    protected List<ode.model.core.OriginalFile> getOriginalFiles(
            ServiceFactory sf, SqlAction sql,
            final String repoUuid,
            final CheckedPath checked) {

            final IQuery q = sf.getQueryService();
            final Long id;

            if (checked.isRoot) {
                id = q.findByString(ode.model.core.OriginalFile.class,
                        "hash", repoUuid).getId();

                if (id == null) {
                    throw new ode.conditions.SecurityViolation(
                            "No repository with UUID: " + repoUuid);
                }
            } else {
                id = sql.findRepoFile(repoUuid,
                        checked.getRelativePath(), checked.getName());

                if (id == null) {
                    throw new ode.conditions.SecurityViolation(
                            "No such parent dir: " + checked);
                }
            }

            // Load parent directory to possibly cause
            // a read sec-vio.
            //
            q.get(ode.model.core.OriginalFile.class, id);

            List<Long> ids = sql.findRepoFiles(repoUuid,
                    checked.getDirname());

            if (CollectionUtils.isEmpty(ids)) {
                return Collections.emptyList();
            }
            final List<ode.model.core.OriginalFile> files = new ArrayList<>(ids.size());
            final String filesById = LOAD_ORIGINAL_FILE + "f.id in (:ids)";
            for (final List<Long> idsBatch : Iterables.partition(ids, BATCH_SIZE)) {
                final Parameters params = new Parameters().addIds(idsBatch);
                for (final IObject file : q.findAllByQuery(filesById, params)) {
                    files.add((ode.model.core.OriginalFile) file);
                }
            }
            return files;

    }

    public Fileset saveFileset(final String repoUuid, final Fileset _fs,
            final ChecksumAlgorithm checksumAlgorithm, final List<CheckedPath> paths,
            final Ice.Current current) throws ServerError {

        final IceMapper mapper = new IceMapper();
        final List<CheckedPath> parents = new ArrayList<CheckedPath>();
        for (CheckedPath path : paths) {
            parents.add(path.parent());
        }

        final ode.model.fs.Fileset fs = (ode.model.fs.Fileset) mapper.reverse(_fs);

        final StopWatch outer = new Slf4JStopWatch();
        try {
            return (Fileset) mapper.map(
                    executor.execute(current.ctx, currentUser(current),
                            new Executor.SimpleWork<ode.model.fs.Fileset>(
                    this, "saveFileset", repoUuid, fs, paths) {
                @Transactional(readOnly = false)
                public ode.model.fs.Fileset doWork(Session session, ServiceFactory sf) {
                    // Pre-save all the jobs.
                    for (int i = 0; i < fs.sizeOfJobLinks(); i++) {
                        FilesetJobLink link = fs.getFilesetJobLink(i);
                        JobHandle jh = sf.createJobHandle();
                        try {
                            jh.submit(link.child());
                            link.setChild(jh.getJob());
                        } finally {
                            jh.close();
                        }
                    }

                    StopWatch sw = new Slf4JStopWatch();
                    final int size = paths.size();
                    List<ode.model.core.OriginalFile> ofs =
                                _internalRegister(repoUuid, paths, parents,
                                        checksumAlgorithm, null,
                                        sf, getSqlAction(), session);
                    sw.stop("ode.repo.save_fileset.register");

                    sw = new Slf4JStopWatch();
                    for (int i = 0; i < size; i++) {
                        CheckedPath checked = paths.get(i);
                        ode.model.core.OriginalFile of = ofs.get(i);
                        fs.getFilesetEntry(i).setOriginalFile(of);
                    }
                    sw.stop("ode.repo.save_fileset.update_fileset_entries");

                    sw = new Slf4JStopWatch();
                    try {
                        return sf.getUpdateService().saveAndReturnObject(fs);
                    } finally {
                        sw.stop("ode.repo.save_fileset.save");
                    }

                }}));
        } catch (Exception e) {
            throw (ServerError) mapper.handleException(e, executor.getContext());
        } finally {
            outer.stop("ode.repo.save_fileset");
        }
    }

    @SuppressWarnings("unchecked")
    public List<Fileset> loadFilesets(final List<Long> ids,
            final Ice.Current current) throws ServerError {

        if (ids == null || ids.size() == 0) {
            return new ArrayList<Fileset>(); // EARLY EXIT
        }

        final IceMapper mapper = new IceMapper();

        try {
            return (List<Fileset>) mapper.map((List<ode.model.fs.Fileset>)
                    executor.execute(current.ctx, currentUser(current),
                            new Executor.SimpleWork(
                    this, "loadFilesets", ids) {
                @Transactional(readOnly = true)
                public Object doWork(Session session, ServiceFactory sf) {
                    return sf.getQueryService().findAllByQuery(
                            "select fs from Fileset fs where fs.id in (:ids)",
                            new Parameters().addIds(ids));
                }}));
        } catch (Exception e) {
            throw (ServerError) mapper.handleException(e, executor.getContext());
        }
    }

    public OriginalFile register(final String repoUuid, final CheckedPath checked,
            final String mimetype, final Ice.Current current) throws ServerError {

        if (checked.isRoot) {
            throw new ode.SecurityViolation(null, null,
                    "Can't re-register the repository");
        }

        final CheckedPath parent = checked.parent();
        final IceMapper mapper = new IceMapper();

        try {
            final ode.model.core.OriginalFile of = executor.execute(current.ctx, currentUser(current),
                            new Executor.SimpleWork<ode.model.core.OriginalFile>(
                    this, "register", repoUuid, checked, mimetype) {
                @Transactional(readOnly = false)
                public ode.model.core.OriginalFile doWork(Session session, ServiceFactory sf) {
                    return _internalRegister(repoUuid,
                            Arrays.asList(checked), Arrays.asList(parent),
                            null, mimetype, sf, getSqlAction(), session).get(0);
                }
            });
            final OriginalFile rv = (OriginalFile) mapper.map(of);
            final String name = rv.getName().getValue();
            if (name.startsWith(fileRepoSecretKey)) {
                rv.setName(ode.rtypes.rstring(name.substring(fileRepoSecretKey.length())));
            }
            return rv;
        } catch (Exception e) {
            throw (ServerError) mapper.handleException(e, executor.getContext());
        }
    }

    /**
     * Returns original file object is "live" within the Hibernate session.
     *
     * @param repoUuid
     * @param checked
     * @param mimetype
     * @param sf
     * @param sql
     * @param session
     * @return See above.
     * @throws ServerError
     */
    public ode.model.core.OriginalFile register(final String repoUuid, final CheckedPath checked,
            final String mimetype, final ServiceFactory sf, final SqlAction sql, Session session)
                    throws ServerError {

        if (checked.isRoot) {
            throw new ode.SecurityViolation(null, null,
                    "Can't re-register the repository");
        }

        final CheckedPath parent = checked.parent();

        return _internalRegister(repoUuid,
                Arrays.asList(checked), Arrays.asList(parent),
                null, mimetype, sf, sql, session).get(0);

    }

    public Job saveJob(final Job job, final Ice.Current current)
            throws ServerError {

        if (job == null) {
            throw new ode.ValidationException(null, null,
                    "Job is null!");
        }

        final IceMapper mapper = new IceMapper();

        try {
            final ode.model.jobs.Job in = (ode.model.jobs.Job) mapper.reverse(job);
            final ode.model.jobs.Job out = executor.execute(current.ctx, currentUser(current),
                            new Executor.SimpleWork<ode.model.jobs.Job>(
                    this, "saveJob", in) {
                @Transactional(readOnly = false)
                public ode.model.jobs.Job doWork(Session session, ServiceFactory sf) {
                    JobHandle jh = sf.createJobHandle();
                    jh.submit(in);
                    return jh.getJob();
                }
            });

            return (Job) mapper.map(out);
        } catch (Exception e) {
            throw (ServerError) mapper.handleException(e, executor.getContext());
        }
    }


    public void updateJob(final Job job, final String message, final String status,
            final Ice.Current current) throws ServerError {

        if (job == null || job.getId() == null) {
            throw new ode.ValidationException(null, null,
                    "Job is null!");
        }

        final IceMapper mapper = new IceMapper();

        try {
            final ode.model.jobs.Job in = (ode.model.jobs.Job) mapper.reverse(job);
            executor.execute(current.ctx, currentUser(current),
                            new Executor.SimpleWork<Void>(
                    this, "updateJob", in, message, status) {
                @Transactional(readOnly = false)
                public Void doWork(Session session, ServiceFactory sf) {
                    JobHandle jh = sf.createJobHandle();
                    jh.attach(in.getId());
                    jh.setStatusAndMessage(status, message);
                    return null;
                }
            });

        } catch (Exception e) {
            throw (ServerError) mapper.handleException(e, executor.getContext());
        }
    }

    /*
     * See api.
     */
    public void makeDirs(final PublicRepositoryI repo,
            final List<CheckedPath> dirs,
            final boolean parents,
            final Ice.Current __current) throws ServerError {
        try {
            /* first check for sudo to find real user's event context */
            final EventContext effectiveEventContext;
            final String realSessionUuid = __current.ctx.get(PublicRepositoryI.SUDO_REAL_SESSIONUUID);
            if (realSessionUuid != null) {
                final String realGroupName = __current.ctx.get(PublicRepositoryI.SUDO_REAL_GROUP_NAME);
                final Principal realPrincipal = new Principal(realSessionUuid, realGroupName, null);
                final Map<String, String> realCtx = new HashMap<String, String>(__current.ctx);
                realCtx.put(ode.constants.SESSIONUUID.value, realSessionUuid);
                if (realGroupName == null) {
                    realCtx.remove(ode.constants.GROUP.value);
                } else {
                    realCtx.put(ode.constants.GROUP.value, realGroupName);
                }
                effectiveEventContext = executor.execute(realCtx, realPrincipal,
                        new Executor.SimpleWork<EventContext>(this, "makeDirs", dirs) {
                    @Transactional(readOnly = true)
                    public EventContext doWork(Session session, ServiceFactory sf) {
                        return ((LocalAdmin) sf.getAdminService()).getEventContextQuiet();
                    }
                });
            } else {
                effectiveEventContext = null;
            }
            /* now actually make the directories */
            executor.execute(__current.ctx, currentUser(__current),
                new Executor.SimpleWork<Void>(this, "makeDirs", dirs) {
            @Transactional(readOnly = false)
            public Void doWork(Session session, ServiceFactory sf) {
                final EventContext eventContext;
                if (effectiveEventContext == null) {
                    eventContext =
                        ((LocalAdmin) sf.getAdminService()).getEventContextQuiet();
                } else {
                    eventContext = effectiveEventContext;  /* sudo */
                }
                for (CheckedPath checked : dirs) {
                    try {
                        repo.makeDir(checked, parents,
                            session, sf, getSqlAction(), eventContext);
                    } catch (ServerError se) {
                        throw new Rethrow(se);
                    }
                }
                return null;
            }
        });
        } catch (Rethrow rt) {
            throw (ServerError) rt.t;
        } catch (Exception e) {
            throw (ServerError) mapper.handleException(e, executor.getContext());
        }
    }

    /**
     * Internal file registration which must happen within a single tx. All
     * files in the same directory are loaded in one block.
     *
     * @param repoUuid
     * @param checked
     * @param checksumAlgorithm 
     * @param mimetype
     * @param parent
     * @param sf non-null
     * @param sql non-null
     * @param session
     * @return
     */
    private List<ode.model.core.OriginalFile> _internalRegister(final String repoUuid,
            final List<CheckedPath> checked, final List<CheckedPath> parents,
            ChecksumAlgorithm checksumAlgorithm, final String mimetype,
            ServiceFactory sf, SqlAction sql, Session session) {

        final List<ode.model.core.OriginalFile> toReturn = new ArrayList<ode.model.core.OriginalFile>();
        final ListMultimap<CheckedPath, CheckedPath> levels = ArrayListMultimap.create();
        for (int i = 0; i < checked.size(); i++) {
            levels.put(parents.get(i), checked.get(i));
        }

        for (CheckedPath parent : levels.keySet()) {
            List<CheckedPath> level = levels.get(parent);
            List<String> basenames = new ArrayList<String>(checked.size());
            for (CheckedPath path: level) {
                basenames.add(path.getName());
            }

            StopWatch sw = new Slf4JStopWatch();
            Map<String, Long> fileIds = sql.findRepoFiles(repoUuid,
                level.get(0).getRelativePath(), /* all the same */
                basenames, null /*mimetypes*/);
            sw.stop("ode.repo.internal_register.find_repo_files");

            List<Long> toLoad = new ArrayList<Long>();
            List<CheckedPath> toCreate = new ArrayList<CheckedPath>();
            for (int i = 0; i < level.size(); i++) {
                CheckedPath path = level.get(i);
                Long fileId = fileIds.get(path.getName());
                if (fileId == null) {
                    toCreate.add(path);
                } else {
                    toLoad.add(fileId);
                }
            }
            
            if (toCreate.size() > 0) {
                canWriteParentDirectory(sf, sql,
                    repoUuid, parent);
                List<ode.model.core.OriginalFile> created = createOriginalFile(sf, sql,
                    repoUuid, toCreate, checksumAlgorithm, mimetype, session);
                toReturn.addAll(created);
            }

            sw = new Slf4JStopWatch();
            if (toLoad.size() > 0) {
                List<ode.model.core.OriginalFile> loaded = 
                    sf.getQueryService().findAllByQuery("select o from OriginalFile o " +
                        "where o.id in (:ids)", new Parameters().addIds(toLoad));
                toReturn.addAll(loaded);
            }
            sw.stop("ode.repo.internal_register.load");
        }
        return toReturn;
    }

    public FsFile getFile(final long id, final Ice.Current current,
            final String repoUuid) {
        return executor.execute(current.ctx, currentUser(current),
                new Executor.SimpleWork<FsFile>(this, "getFile", id) {
            @Transactional(readOnly = true)
            public FsFile doWork(Session session, ServiceFactory sf) {
                    String path = getSqlAction().findRepoFilePath(
                            repoUuid, id);
                    if (path == null) {
                        return null;
                    }
                    return new FsFile(path);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<DeleteLog> findRepoDeleteLogs(final DeleteLog template, Current current) {
        return executor.execute(current.ctx, currentUser(current),
                new Executor.SimpleWork<List<DeleteLog>>(this, "findRepoDeleteLogs", template) {
            @Transactional(readOnly = true)
            public List<DeleteLog> doWork(Session session, ServiceFactory sf) {
                return getSqlAction().findRepoDeleteLogs(template);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<List<DeleteLog>> findRepoDeleteLogs(final List<DeleteLog> templates, Current current) {
        return executor.execute(current.ctx, currentUser(current),
                new Executor.SimpleWork<List<List<DeleteLog>>>(this, "findRepoDeleteLogs", templates) {
            @Transactional(readOnly = true)
            public List<List<DeleteLog>> doWork(Session session, ServiceFactory sf) {
                List<List<DeleteLog>> rv = new ArrayList<List<DeleteLog>>();
                for (DeleteLog template : templates) {
                    rv.add(getSqlAction().findRepoDeleteLogs(template));
                }
                return rv;
            }
        });
    }

    public int deleteRepoDeleteLogs(final DeleteLog template, Current current) {
        return executor.execute(current.ctx, currentUser(current),
                new Executor.SimpleWork<Integer>(this, "deleteRepoDeleteLogs", template) {
            @Transactional(readOnly = false)
            public Integer doWork(Session session, ServiceFactory sf) {
                return getSqlAction().deleteRepoDeleteLogs(template);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<Integer> deleteRepoDeleteLogs(final List<DeleteLog> templates, Current current) {
        return executor.execute(current.ctx, currentUser(current),
                new Executor.SimpleWork<List<Integer>>(this, "deleteRepoDeleteLogs", templates) {
            @Transactional(readOnly = false)
            public List<Integer> doWork(Session session, ServiceFactory sf) {
                List<Integer> rv = new ArrayList<Integer>();
                for (DeleteLog template : templates) {
                    Integer i = getSqlAction().deleteRepoDeleteLogs(template);
                    rv.add(i);
                }
                return rv;
            }
        });
    }

    public ode.sys.EventContext getEventContext(Ice.Current curr) {
        final Principal currentUser = new Principal(curr.ctx.get(ode.constants.SESSIONUUID.value));
        final EventContext ec = executor.execute(curr.ctx, currentUser,
                new Executor.SimpleWork<EventContext>(this, "getEventContext") {
            @Transactional(readOnly = true)
            public EventContext doWork(Session session, ServiceFactory sf) {
                return ((LocalAdmin) sf.getAdminService()).getEventContextQuiet();
            }
        });
        return IceMapper.convert(ec);
    }

    public String getUserInstitution(final long userId, Ice.Current current) {
        return executor.execute(current.ctx, currentUser(current),
                new Executor.SimpleWork<String>(this, "getUserInstitution") {
            @Transactional(readOnly = true)
            public String doWork(Session session, ServiceFactory sf) {
                return getUserInstitution(userId, sf);
            }
        });
    }

    public String getUserInstitution(long userId, ServiceFactory sf) {
        final Parameters parameters = new Parameters().addId(userId);
        final List<Object[]> results = sf.getQueryService().projection(LOAD_USER_INSTITUTION, parameters);
        if (results instanceof List && results.get(0) instanceof Object[]) {
            final Object[] firstResult = (Object[]) results.get(0);
            if (firstResult.length > 0 && firstResult[0] instanceof String) {
                return (String) firstResult[0];
            }
        }
        return null;
    }

    //
    // HELPERS
    //

    /**
     * Primary location for creating original files from a {@link CheckedPath}
     * instance. This will use access to the actual {@link java.io.File}
     * object in order to calculate size, timestamps, etc.
     *
     * @param sf
     * @param sql
     * @param repoUuid
     * @param checked
     * @param checksumAlgorithm 
     * @param mimetype
     * @param session
     * @return See above.
     */
    protected List<ode.model.core.OriginalFile> createOriginalFile(
            ServiceFactory sf, SqlAction sql, String repoUuid,
            List<CheckedPath> checked, ChecksumAlgorithm checksumAlgorithm, String mimetype, Session session) {

        ode.model.enums.ChecksumAlgorithm ca = null;
        if (checksumAlgorithm != null) {
             ca = new ode.model.enums.ChecksumAlgorithm(checksumAlgorithm.getValue().getValue());
        }
        List<ode.model.core.OriginalFile> rv = new ArrayList<ode.model.core.OriginalFile>();
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        for (CheckedPath path : checked) {
            ode.model.core.OriginalFile ofile = path.asOriginalFile(mimetype);
            rv.add(ofile);
            ofile.setCtime(now);
            ofile.setHasher(ca);
            ofile.setName(fileRepoSecretKey + ofile.getName());  // prefix removed by database trigger
            ofile.setRepo(repoUuid);
        }

        StopWatch sw = new Slf4JStopWatch();
        final IUpdate iUpdate = sf.getUpdateService();
        IObject[] saved = iUpdate.saveAndReturnArray(rv.toArray(new IObject[rv.size()]));
        sw.stop("ode.repo.create_original_file.save");
        final List<Long> ids = new ArrayList<Long>(saved.length);
        sw = new Slf4JStopWatch();
        for (int i = 0; i < saved.length; i++) {
            final CheckedPath path = checked.get(i);
            final ode.model.core.OriginalFile ofile = (ode.model.core.OriginalFile) saved[i];
            rv.set(i, ofile);
            ids.add(ofile.getId());
            if (PublicRepositoryI.DIRECTORY_MIMETYPE.equals(ofile.getMimetype())) {
                internalMkdir(path);
            }
        }
        sw.stop("ode.repo.create_original_file.internal_mkdir");
        return rv;
    }

    /**
     * This method should only be used by the register public method in order to
     * guarantee that the DB is kept in sync with the file system.
     * @param file the path to ensure exists as a directory
     * @throws ode.conditions.ResourceError
     */
    protected void internalMkdir(CheckedPath file) {
        if (file.exists()) {
            if (file.isRoot || file.isDirectory()) {
                return;
            } else {
                throw new ode.conditions.ResourceError("Cannot mkdir " + file + 
                        " because it is already a file");
            }
        }
        try {
            if (!file.mkdirs()) {
                throw new ode.conditions.ResourceError("Cannot mkdir " + file);
            }
        } catch (Exception e) {
            log.error(e.toString()); // slf4j migration: toString()
            throw new ode.conditions.ResourceError("Cannot mkdir " + file + ":" + e.getMessage());
        }
    }

    /**
     * Throw a {@link ode.conditions.SecurityViolation} if the current
     * context cannot write to the parent directory.
     *
     * @param sf
     * @param sql
     * @param repoUuid
     * @param parent
     */
    protected void canWriteParentDirectory(ServiceFactory sf, SqlAction sql,
            final String repoUuid, final CheckedPath parent) {

        if (parent.isRoot) {
            // Allow whatever (for the moment) at the top-level
            return; // EARLY EXIT!
        }

        // Now we check whether or not the current user has
        // write permissions for the *parent* directory.
        final Long parentId = sql.findRepoFile(repoUuid,
                parent.getRelativePath(), parent.getName());

        if (parentId == null) {
            throw new ode.conditions.SecurityViolation(
                    "Cannot find parent directory: " + parent);
        }

        final ode.model.core.OriginalFile parentObject
            = new ode.model.core.OriginalFile(parentId, false);

        long parentObjectOwnerId = -1;
        long parentObjectGroupId = -1;
        try {
            final String query = "SELECT details.owner.id, details.group.id FROM OriginalFile WHERE id = :id";
            final Parameters parameters = new Parameters().addId(parentId);
            final Object[] results = sf.getQueryService().projection(query, parameters).get(0);
            parentObjectOwnerId = (Long) results[0];
            parentObjectGroupId = (Long) results[1];
        } catch (Exception e) {
            log.warn("failed to retrieve owner and group details for original file #" + parentId, e);
        }

        if (parentObjectOwnerId != roles.getRootId() || parentObjectGroupId != roles.getUserGroupId()) {
            final LocalAdmin admin = (LocalAdmin) sf.getAdminService();
            final EventContext ec = admin.getEventContext();
            if (!(admin.canAnnotate(parentObject) ||
                    parentObjectGroupId == roles.getUserGroupId() &&
                    ec.getCurrentGroupPermissions().isGranted(
                            parentObjectOwnerId == ec.getCurrentUserId() ? Role.USER : Role.GROUP, Right.ANNOTATE))) {
                throw new ode.conditions.SecurityViolation(
                        "No annotate access for parent directory: "
                                + parentId);
            }
        }
    }

    protected Principal currentUser(Current __current) {
        final Map<String, String> ctx = __current.ctx;
        final String session = ctx.get(ode.constants.SESSIONUUID.value);
        final String group = ctx.get(ode.constants.GROUP.value);
        return new Principal(session, group, null);
    }

    /**
     * Create a String-String map which can be used as the context for a call
     * to Executor.execute based on the group of the file object.
     * @return See above.
     * @throws SecurityViolation if the file can't be read.
     */
    protected Map<String, String> fileContext(long fileId, Ice.Current current)
        throws ode.SecurityViolation {

        // TODO: we should perhaps pass "-1" here regardless of what group is
        // passed by the client, but that violates the current working of the
        // API, so using the standard behavior at the moment.
        final OriginalFile file = getOriginalFile(fileId, current);
        return groupContext(file.getDetails().getGroup().getId().getValue(),
                current);
    }

    /**
     * Creates a copy of the {@link Ice.Current#ctx} map and if groupId is
     * not null, sets the "ode.group" key to be a string version of the
     * id.
     *
     * @param groupId
     * @param current
     * @return See above.
     */
    protected Map<String, String> groupContext(Long groupId, Ice.Current current) {
        final Map<String, String> context = new HashMap<String, String>();
        if (current.ctx != null) {
            context.putAll(current.ctx);
        }
        if (groupId != null) {
            context.put("ode.group", Long.toString(groupId));
        }
        return context;
    }

    @Override
    public ode.model.enums.ChecksumAlgorithm getChecksumAlgorithm(final String name, Ice.Current current) {
        return executor.execute(current.ctx, currentUser(current),
                new Executor.LoggedWork<ode.model.enums.ChecksumAlgorithm>() {

            @Override
            public String description() {
                return "get a checksum algorithm by name " + name;
            }

            @Override
            @Transactional(readOnly = true)
            public ode.model.enums.ChecksumAlgorithm doWork(Session session, ServiceFactory sf) {
                final String query = "FROM ChecksumAlgorithm WHERE value = :name";
                final Parameters params = new Parameters().addString("name", name);
                final List<Object[]> results = sf.getQueryService().projection(query, params);
                return (ode.model.enums.ChecksumAlgorithm) results.get(0)[0];
            }
        });
    }

    @Override
    public ode.model.core.OriginalFile getOriginalFileWithHasher(final long id, Ice.Current current) {
        return executor.execute(current.ctx, currentUser(current),
                new Executor.LoggedWork<ode.model.core.OriginalFile>() {

            @Override
            public String description() {
                return "get an original file #" + id + ", with hasher joined";
            }

            @Override
            @Transactional(readOnly = true)
            public ode.model.core.OriginalFile doWork(Session session, ServiceFactory sf) {
                final String query = "FROM OriginalFile o LEFT OUTER JOIN FETCH o.hasher WHERE o.id = :id";
                final Parameters params = new Parameters().addId(id);
                final List<Object[]> results = sf.getQueryService().projection(query, params);
                return (ode.model.core.OriginalFile) results.get(0)[0];
            }
        });
    }

    @Override
    public void saveObject(final IObject object, Ice.Current current) {
        executor.execute(current.ctx, currentUser(current),
                new Executor.LoggedWork<Void>() {

            @Override
            public String description() {
                return "save the model object " + object;
            }

            @Override
            @Transactional(readOnly = false)
            public Void doWork(Session session, ServiceFactory sf) {
                sf.getUpdateService().saveObject(object);
                return null;
            }
        });
    }

}