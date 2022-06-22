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

import static ode.rtypes.rstring;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import loci.formats.FormatReader;
import ode.system.EventContext;
import ode.api.IAdmin;
import ode.api.IUpdate;
import ode.conditions.ApiUsageException;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportContainer;
import ode.model.core.OriginalFile;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.security.SecuritySystem;
import ode.security.basic.BasicEventContext;
import ode.services.server.repo.path.ClientFilePathTransformer;
import ode.services.server.repo.path.FilePathRestrictionInstance;
import ode.services.server.repo.path.FsFile;
import ode.services.server.repo.path.MakeNextDirectory;
import ode.services.server.util.ChecksumAlgorithmMapper;
import ode.system.Roles;
import ode.system.ServiceFactory;
import ode.util.SqlAction;
import ode.util.checksum.ChecksumProvider;
import ode.util.checksum.ChecksumProviderFactory;
import ode.util.checksum.ChecksumProviderFactoryImpl;
import ode.util.checksum.ChecksumType;
import ode.ResourceError;
import ode.ServerError;
import ode.ValidationException;
import ode.grid.ImportLocation;
import ode.grid.ImportProcessPrx;
import ode.grid.ImportSettings;
import ode.grid._ManagedRepositoryOperations;
import ode.grid._ManagedRepositoryTie;
import ode.model.ChecksumAlgorithm;
import ode.model.Fileset;
import ode.model.FilesetEntry;
import ode.model.FilesetI;
import ode.model.FilesetJobLink;
import ode.model.IndexingJobI;
import ode.model.Job;
import ode.model.MetadataImportJob;
import ode.model.MetadataImportJobI;
import ode.model.NamedValue;
import ode.model.PixelDataJobI;
import ode.model.ThumbnailGenerationJob;
import ode.model.ThumbnailGenerationJobI;
import ode.model.UploadJob;
import ode.util.IceMapper;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import Ice.Current;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.google.common.math.IntMath;

/**
 * Extension of the PublicRepository API which only manages files
 * under ${ode.data.dir}/ManagedRepository.
 */
public class ManagedRepositoryI extends PublicRepositoryI
    implements _ManagedRepositoryOperations {

    private final static Logger log = LoggerFactory.getLogger(ManagedRepositoryI.class);

    private final static int parentDirsToRetain = 3;

    /* This class is used in the server-side creation of import containers.
     * The suggestImportPaths method sanitizes the paths in due course.
     * From the server side, we cannot imitate ImportLibrary.createImport
     * in applying client-side specifics to clean up the path. */
    private static final ClientFilePathTransformer nopClientTransformer = new ClientFilePathTransformer(Function.identity());

    /* used for generating %monthname% for path templates */
    private static final DateFormatSymbols DATE_FORMAT = new DateFormatSymbols();

    /* referenced by only the bare-bones ManagedRepositoryI constructor used in testing */
    private static final String ALL_CHECKSUM_ALGORITHMS =
            Joiner.on(',').join(ChecksumAlgorithmMapper.getAllChecksumAlgorithms()
                    .stream().map(ChecksumAlgorithmMapper.CHECKSUM_ALGORITHM_NAMER).collect(Collectors.toList()));

    /* template paths: matches any special expansion term */
    private static final Pattern TEMPLATE_TERM = Pattern.compile("%([a-zA-Z]+)(:([^%/]+))?%");

    /* template paths: the root and user portions separately, never null */
    /*private final*/ protected FsFile templateRoot;  /* exposed for unit testing only */
    private final FsFile templateUser;

    private final ProcessContainer processes;

    private final SecuritySystem securitySystem;

    private final String rootSessionUuid;

    private final long userGroupId;

    private final Set<String> managedRepoUuids;

    private final ThreadLocal<WeakReference<Consumer<CheckedPath>>> fileCreationListeners = new ThreadLocal<>();

    /**
     * Creates a {@link ProcessContainer} internally that will not be managed
     * by background threads. Used primarily during testing.
     * @param template
     * @param dao
     */
    public ManagedRepositoryI(String template, RepositoryDao dao) throws Exception {
        this(template, dao, new ProcessContainer(), null, new ChecksumProviderFactoryImpl(),
                ALL_CHECKSUM_ALGORITHMS, FilePathRestrictionInstance.UNIX_REQUIRED.name, null, new Roles(), new HashSet<String>());
    }

    public ManagedRepositoryI(String template, RepositoryDao dao,
            ProcessContainer processes,
            SecuritySystem securitySystem,
            ChecksumProviderFactory checksumProviderFactory,
            String checksumAlgorithmSupported,
            String pathRules,
            String rootSessionUuid,
            Roles roles, Set<String> managedRepoUuids) throws ServerError {
        super(dao, checksumProviderFactory, checksumAlgorithmSupported, pathRules);
        this.managedRepoUuids = managedRepoUuids;

        int splitPoint = template.lastIndexOf("//");
        if (splitPoint < 0) {
            /* without "//" the whole path is user-owned */
            splitPoint = 0;
        }

        this.templateRoot = new FsFile(template.substring(0, splitPoint));
        this.templateUser = new FsFile(template.substring(splitPoint));

        if (FsFile.emptyPath.equals(templateUser)) {
            throw new ode.ApiUsageException(null, null,
                    "no user-owned directories in managed repository template path");
        }

        this.processes = processes;
        this.securitySystem = securitySystem;
        this.rootSessionUuid = rootSessionUuid;
        this.userGroupId = roles.getUserGroupId();
        log.info("Repository template: " + template);
    }

    @Override
    public Ice.Object tie() {
        return new _ManagedRepositoryTie(this);
    }

    @Override
    public void initialize(FileMaker fileMaker, Long id, String repoUuid) throws ValidationException {
        super.initialize(fileMaker, id, repoUuid);
        managedRepoUuids.add(repoUuid);
    }

    /**
     * Set the file creation listener <em>for this thread</em> that is notified of new files being created.
     * Maintains only a <em>weak</em> reference to the given listener.
     * Applies only for uses of {@link #makeCheckedDirs(LinkedList, boolean, Session, ServiceFactory, SqlAction, EventContext)}
     * by this thread.
     * @param fileCreationListener a file creation listener
     */
    public void setFileCreationListener(Consumer<CheckedPath> fileCreationListener) {
        fileCreationListeners.set(new WeakReference<>(fileCreationListener));
    }

    /**
     * Clear the file creation listener <em>for this thread</em>.
     * @see #setFileCreationListener(Consumer)
     */
    public void clearFileCreationListener() {
        fileCreationListeners.remove();
    }

    //
    // INTERFACE METHODS
    //

    /**
     * Return a template based directory path. The path will be created
     * by calling {@link #makeDir(String, boolean, Ice.Current)}.
     */
    public ImportProcessPrx uploadFileset(Fileset fs, ImportSettings settings,
            Ice.Current __current) throws ode.ServerError {

        ImportLocation location = internalImport(fs, settings, __current);
        return createUploadProcess(fs, location, settings, __current, true);

    }

    /**
     * Return a template based directory path. The path will be created
     * by calling {@link #makeDir(String, boolean, Ice.Current)}.
     */
    public ImportProcessPrx importFileset(Fileset fs, ImportSettings settings,
            Ice.Current __current) throws ode.ServerError {

        ImportLocation location = internalImport(fs, settings, __current);
        return createImportProcess(fs, location, settings, __current);

    }

    private ImportLocation internalImport(Fileset fs, ImportSettings settings,
            Ice.Current __current) throws ode.ServerError {

        if (fs == null || fs.sizeOfUsedFiles() < 1) {
            throw new ode.ApiUsageException(null, null, "No paths provided");
        }

        if (settings == null) {
            settings = new ImportSettings(); // All defaults.
        }
        if (settings.checksumAlgorithm == null) {
            throw new ode.ApiUsageException(null, null, "must specify checksum algorithm");
        }

        final List<FsFile> paths = new ArrayList<FsFile>();
        for (FilesetEntry entry : fs.copyUsedFiles()) {
            paths.add(new FsFile(entry.getClientPath().getValue()));
        }

        // at this point, the template path should not yet exist on the filesystem
        final List<FsFile> sortedPaths = Ordering.usingToString().immutableSortedCopy(paths);
        final FsFile relPath = createTemplatePath(sortedPaths, __current);
        fs.setTemplatePrefix(rstring(relPath.toString() + FsFile.separatorChar));

        final Class<? extends FormatReader> readerClass = getReaderClass(fs, __current);

        // The next part of the string which is chosen by the user:
        // /home/shashi.rai/myStuff
        FsFile basePath = commonRoot(paths);

        // If any two files clash in that chosen basePath directory, then
        // we want to suggest a similar alternative.
        return suggestImportPaths(relPath, basePath, paths, readerClass,
                settings.checksumAlgorithm, __current);

    }

    public ImportProcessPrx importPaths(List<String> paths,
            Ice.Current __current) throws ServerError {

        if (paths == null || paths.size() < 1) {
            throw new ode.ApiUsageException(null, null, "No paths provided");
        }

        final ImportContainer container = new ImportContainer(
                null /*file*/, null /*target*/, null /*userPixels*/,
                "Unknown" /*reader*/, paths.toArray(new String[0]),
                false /*spw*/);

        final ImportSettings settings = new ImportSettings();
        final Fileset fs = new FilesetI();
        try {
            container.fillData(new ImportConfig(), settings, fs, nopClientTransformer);
            settings.checksumAlgorithm = this.checksumAlgorithms.get(0);
        } catch (IOException e) {
            throw new IceMapper().handleServerError(e, context);
        }

        return importFileset(fs, settings, __current);
    }

    public List<ImportProcessPrx> listImports(Ice.Current __current) throws ode.ServerError {

        final List<Long> filesetIds = new ArrayList<Long>();
        final List<ImportProcessPrx> proxies = new ArrayList<ImportProcessPrx>();
        final ode.sys.EventContext ec = repositoryDao.getEventContext(__current);
        final List<ProcessContainer.Process> ps
            = processes.listProcesses(ec.memberOfGroups);

        for (final ProcessContainer.Process p : ps) {
            filesetIds.add(p.getFileset().getId().getValue());
        }

        final List<Fileset> filesets
            = repositoryDao.loadFilesets(filesetIds, __current);

        for (Fileset fs : filesets) {
            if (!fs.getDetails().getPermissions().canEdit()) {
                filesetIds.remove(fs.getId().getValue());
            }
        }

        for (final ProcessContainer.Process p : ps) {
            if (filesetIds.contains(p.getFileset().getId().getValue())) {
                proxies.add(p.getProxy());
            }
        }

        return proxies;
    }

    public List<ChecksumAlgorithm> listChecksumAlgorithms(Current __current) {
        return this.checksumAlgorithms;
    }

    public ChecksumAlgorithm suggestChecksumAlgorithm(List<ChecksumAlgorithm> supported, Current __current) {
        final Set<String> supportedNames =
                supported.stream().map(ChecksumAlgorithmMapper.CHECKSUM_ALGORITHM_NAMER).collect(Collectors.toSet());
        for (final ChecksumAlgorithm configured : listChecksumAlgorithms(__current)) {
            if (supportedNames.contains(ChecksumAlgorithmMapper.CHECKSUM_ALGORITHM_NAMER.apply(configured))) {
                return configured;
            }
        }
        return null;
    }

    public List<Long> verifyChecksums(List<Long> ids, Current __current) throws ServerError {
        /* set up an invocation context in which the group is set to -1, for "all groups" */
        final Current allGroupsCurrent = makeAdjustedCurrent(__current);
        allGroupsCurrent.ctx = new HashMap<String, String>(__current.ctx);
        allGroupsCurrent.ctx.put(ode.constants.GROUP.value, "-1");

        /* verify the checksum of the specified files that are in this repository */
        final List<Long> mismatchFiles = new ArrayList<Long>();
        for (final long id : repositoryDao.filterFilesByRepository(getRepoUuid(), ids, allGroupsCurrent)) {
            /* get one of the files */
            final OriginalFile file = repositoryDao.getOriginalFileWithHasher(id, allGroupsCurrent);
            final FsFile fsPath = new FsFile(file.getPath() + file.getName());
            final String osPath = serverPaths.getServerFileFromFsFile(fsPath).getAbsolutePath();

            /* check the file's checksum */
            final ode.model.enums.ChecksumAlgorithm hasher = file.getHasher();
            final String hash = file.getHash();
            if (hasher != null && hash != null) {
                /* has a valid checksum, so check it */
                final ChecksumProvider fromProvider =
                        checksumProviderFactory.getProvider(ChecksumAlgorithmMapper.getChecksumType(hasher));
                fromProvider.putFile(osPath);
                if (!fromProvider.checksumAsString().equalsIgnoreCase(hash)) {
                    mismatchFiles.add(id);
                }
            }
        }
        return mismatchFiles;
    }

    public List<Long> setChecksumAlgorithm(ChecksumAlgorithm toHasherWrapped, List<Long> ids, Current __current)
            throws ServerError {
        /* set up an invocation context in which the group may be adjusted freely */
        final Current adjustedGroupCurrent = makeAdjustedCurrent(__current);
        adjustedGroupCurrent.ctx = new HashMap<String, String>(__current.ctx);
        adjustedGroupCurrent.ctx.put(ode.constants.GROUP.value, "-1");

        /* get the hasher to which to set the files */
        final String toHasherName = toHasherWrapped.getValue().getValue();
        final ode.model.enums.ChecksumAlgorithm toHasher = repositoryDao.getChecksumAlgorithm(toHasherName, adjustedGroupCurrent);
        final ChecksumType toType = ChecksumAlgorithmMapper.getChecksumType(toHasher);

        /* set the specified files that are in this repository */
        final List<Long> adjustedFiles = new ArrayList<Long>();
        for (final long id : repositoryDao.filterFilesByRepository(getRepoUuid(), ids, adjustedGroupCurrent)) {
            /* get one of the files */
            final OriginalFile file = repositoryDao.getOriginalFileWithHasher(id, adjustedGroupCurrent);
            final FsFile fsPath = new FsFile(file.getPath() + file.getName());
            final String osPath = serverPaths.getServerFileFromFsFile(fsPath).getAbsolutePath();

            /* check the file's existing hasher */
            final ode.model.enums.ChecksumAlgorithm fromHasher = file.getHasher();
            final String fromHash = file.getHash();
            ChecksumProvider fromProvider = null;
            if (fromHasher != null && fromHash != null) {
                /* already has a valid hash */
                if (toHasherName.equals(fromHasher.getValue())) {
                    /* already hashed in the specified manner */
                    continue;
                } else {
                    /* hashed with a different hasher */
                    fromProvider = checksumProviderFactory.getProvider(ChecksumAlgorithmMapper.getChecksumType(fromHasher));
                }
            }
            /* find the new hash */
            final ChecksumProvider toProvider = checksumProviderFactory.getProvider(toType);
            toProvider.putFile(osPath);
            final String toHash = toProvider.checksumAsString();
            if (fromProvider != null) {
                /* check old hash after new one is calculated */
                fromProvider.putFile(osPath);
                if (!fromProvider.checksumAsString().equals(fromHash)) {
                    throw new ServerError(null, null, "hash mismatch on file ID " + id);
                }
            }
            /* update the file's checksum */
            file.setHasher(toHasher);
            file.setHash(toHash);
            final String fileGroup = Long.toString(file.getDetails().getGroup().getId());
            adjustedGroupCurrent.ctx.put(ode.constants.GROUP.value, fileGroup);
            repositoryDao.saveObject(file, adjustedGroupCurrent);
            adjustedGroupCurrent.ctx.put(ode.constants.GROUP.value, "-1");
            adjustedFiles.add(id);
        }
        return adjustedFiles;
    }

    //
    // HELPERS
    //

    /**
     * Creating the process will register itself in an appropriate
     * container (i.e. a SessionI or similar) for the current
     * user and therefore this instance no longer needs to worry
     * about the maintenance of the object.
     */
    protected ImportProcessPrx createImportProcess(Fileset fs,
            ImportLocation location, ImportSettings settings, Current __current)
                throws ServerError {

        StopWatch sw = new Slf4JStopWatch();

        // Initialization version info
        final ImportConfig config = new ImportConfig();
        final List<NamedValue> serverVersionInfo = new ArrayList<NamedValue>();
        config.fillVersionInfo(serverVersionInfo);

        // Create and validate jobs
        if (fs.sizeOfJobLinks() != 1) {
            throw new ode.ValidationException(null, null,
                    "Found more than one job link. "+
                    "Link only updateJob on creation!");
        }
        final FilesetJobLink jobLink = fs.getFilesetJobLink(0);
        final Job job = jobLink.getChild();
        if (job == null) {
            throw new ode.ValidationException(null, null,
                    "Found null-UploadJob on creation");
        }
        if (!(job instanceof UploadJob)) {
            throw new ode.ValidationException(null, null,
                    "Found non-UploadJob on creation: "+
                    job.getClass().getName());
        }

        MetadataImportJob metadata = new MetadataImportJobI();
        metadata.setVersionInfo(serverVersionInfo);
        fs.linkJob(metadata);

        fs.linkJob(new PixelDataJobI());

        if (settings.doThumbnails != null && settings.doThumbnails.getValue()) {
            ThumbnailGenerationJob thumbnail = new ThumbnailGenerationJobI();
            fs.linkJob(thumbnail);
        }

        fs.linkJob(new IndexingJobI());

        if (location instanceof ManagedImportLocationI) {
            CheckedPath logFile  = ((ManagedImportLocationI) location).getLogFile();
            OriginalFile of = logFile.asOriginalFile(IMPORT_LOG_MIMETYPE);
            of = persistLogFile(of, __current);
            job.linkOriginalFile((ode.model.OriginalFile) new IceMapper().map(of));
            try {
                MDC.put("fileset", logFile.getFullFsPath());
                sw.stop("ode.import.process.init");
            } finally {
                MDC.clear();
            }
        }

        return createUploadProcess(fs, location, settings, __current, false);
    }

    /**
     * Creating the process will register itself in an appropriate
     * container (i.e. a SessionI or similar) for the current
     * user and therefore this instance no longer needs to worry
     * about the maintenance of the object.
     */
    protected ImportProcessPrx createUploadProcess(Fileset fs,
            ImportLocation location, ImportSettings settings,
            Current __current, boolean uploadOnly) throws ServerError {

        StopWatch sw = new Slf4JStopWatch();
        MDC.put("fileset", ((ManagedImportLocationI) location).getLogFile().getFullFsPath());
        try {
            // Create CheckedPath objects for use by saveFileset
            final int size = fs.sizeOfUsedFiles();
            final List<CheckedPath> checked = new ArrayList<CheckedPath>();
            for (int i = 0; i < size; i++) {
                final String path = location.sharedPath + FsFile.separatorChar + location.usedFiles.get(i);
                checked.add(checkPath(path, settings.checksumAlgorithm, __current));
            }

            final Fileset managedFs = repositoryDao.saveFileset(getRepoUuid(), fs, settings.checksumAlgorithm, checked, __current);
            // Since the fileset saved validly, we create a session for the user
            // and return the process.

            final ManagedImportProcessI proc = new ManagedImportProcessI(this,
                    managedFs, location, settings, __current, rootSessionUuid);
            processes.addProcess(proc);
            return proc.getProxy();
        } finally {
            sw.stop("ode.import.process.create");
            MDC.clear();
        }
    }

    /**
     * Get the suggested BioFormats Reader for the given Fileset.
     * @param fs a fileset
     * @param __current the current ICE context
     * @return a reader class, or null if none could be found
     */
    protected Class<? extends FormatReader> getReaderClass(Fileset fs, Current __current) {
        for (final Job job : fs.linkedJobList()) {
            if (job instanceof UploadJob) {
                final List<NamedValue> versionInfo = ((UploadJob) job).getVersionInfo(__current);

                if (versionInfo == null) {
                	continue;
                }
                
                String readerName = null;
                for (NamedValue nv : versionInfo) {
                	if (nv != null &&
                			ImportConfig.VersionInfo.BIO_FORMATS_READER.key.equals(
                					nv.name)) {
                		readerName = nv.value;
                		break;
                	}
                }
                if (readerName == null) {
                	continue;
                }
                Class<?> potentialReaderClass;
                try {
                    potentialReaderClass = Class.forName(readerName);
                } catch (NullPointerException npe) {
                    log.debug("No info provided for reader class");
                    continue;
                } catch (Exception e) {
                    log.warn("Error getting reader class", e);
                    continue;
                }
                if (FormatReader.class.isAssignableFrom(potentialReaderClass)) {
                    return (Class<? extends FormatReader>) potentialReaderClass;
                }
            }
        }
        return null;
    }

    /**
     * From a list of paths, calculate the common root path that all share. In
     * the worst case, that may be "/". May not include the last element, the filename.
     * @param paths some paths
     * @return the paths' common root
     */
    protected FsFile commonRoot(List<FsFile> paths) {
        final List<String> commonRoot = new ArrayList<String>();
        int index = 0;
        boolean isCommon = false;
        while (true) {
            String component = null;
            for (final FsFile path : paths) {
                final List<String> components = path.getComponents();
                if (components.size() <= index + 1)  // prohibit very last component
                    isCommon = false;  // not long enough
                else if (component == null) {
                    component = components.get(index);
                    isCommon = true;  // first path
                } else  // subsequent long-enough path
                    isCommon = component.equals(components.get(index));
                if (!isCommon)
                    break;
            }
            if (isCommon)
                commonRoot.add(paths.get(0).getComponents().get(index++));
            else
                break;
        }
        return new FsFile(commonRoot);
    }

    /**
     * Manages the expansion of template paths. Expected to be superseded by a more general approach.
     */
    private class TemplateDirectoryCreator {
        private final Calendar now = Calendar.getInstance();
        private final EventContext intCtx;
        private final ode.sys.EventContext extCtx;
        private final Object consistentData;
        private final boolean createDirectories;
        private final Ice.Current current;
        private final ServiceFactory sf;
        private final Session session;
        private final SqlAction sql;
        private final Deque<String> remaining;
        private final List<String> done;

        /**
         * Prepare to expand a template path.
         * @param base the pre-existing parent directories in the repository
         * @param todo the template path to expand
         * @param ctx the context to apply in expanding the template path
         * @param consistentData the data from which to calculate a consistent hash
         * @param createDirectories if this instance should create the template path on the file-system
         * @param current the method invocation context in which to perform queries and create directories
         * {@code null} to omit actual directory creation
         */
        TemplateDirectoryCreator(FsFile base, FsFile todo, final ode.sys.EventContext ctx, final Object consistentData,
                                 boolean createDirectories, Current current) {
            this.intCtx = null;
            this.extCtx = ctx;
            this.consistentData = consistentData;
            this.createDirectories = createDirectories;
            this.current = current;
            this.sf = null;
            this.session = null;
            this.sql = null;
            this.remaining = new ArrayDeque<String>(todo.getComponents());
            this.done = new ArrayList<String>(base.getComponents());
        }

        /**
         * Prepare to expand a template path.
         * @param base the pre-existing parent directories in the repository
         * @param todo the template path to expand
         * @param ctx the context to apply in expanding the template path
         * @param consistentData the data from which to calculate a consistent hash
         * @param createDirectories if this instance should create the template path on the file-system
         * (must be {@code false} for this constructor)
         * @param sf the service factory which to perform queries
         * {@code null} to omit actual directory creation
         */
        TemplateDirectoryCreator(FsFile base, FsFile todo, final ode.sys.EventContext ctx, final Object consistentData,
                                 boolean createDirectories, ServiceFactory sf) {
            if (createDirectories) {
                throw new ApiUsageException("may not create directories with only a service factory");
            }
            this.intCtx = null;
            this.extCtx = ctx;
            this.consistentData = consistentData;
            this.createDirectories = createDirectories;
            this.current = null;
            this.sf = sf;
            this.session = null;
            this.sql = null;
            this.remaining = new ArrayDeque<String>(todo.getComponents());
            this.done = new ArrayList<String>(base.getComponents());
        }

        TemplateDirectoryCreator(FsFile base, FsFile todo, final EventContext ctx, final Object consistentData,
                boolean createDirectories, ServiceFactory sf, Session session, SqlAction sql) {
            this.intCtx = ctx;
            this.extCtx = IceMapper.convert(ctx);
            this.consistentData = consistentData;
            this.createDirectories = createDirectories;
            this.current = null;
            this.sf = sf;
            this.session = session;
            this.sql = sql;
            this.remaining = new ArrayDeque<String>(todo.getComponents());
            this.done = new ArrayList<String>(base.getComponents());
        }

        /**
         * Concatenate the given path on to the end of the directories already expanded, and return that full repository path.
         * @param path a list of subdirectories
         * @return the full path
         */
        private String getFullPathWith(List<String> path) {
            final StringBuffer sb = new StringBuffer();
            for (final String component : Iterables.concat(done, path)) {
                sb.append(FsFile.separatorChar);
                sb.append(component);
            }
            return sb.toString();
        }

        /**
         * Create a directory in the managed repository.
         * @param path the path of the directory to create
         * @param parents if the parents of the directory should also be created if necessary, like {@code mkdir -p}
         * @throws ServerError if the directory creation failed
         */
        private void makeDir(String path, boolean parents) throws ServerError {
            if (current == null) {
                ManagedRepositoryI.this.makeDir(path, parents, session, sf, sql, intCtx);
            } else {
                ManagedRepositoryI.this.makeDir(path, parents, current);
            }
        }

        /**
         * Expand {@code %user%} to the user's name.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandUser(String prefix, String suffix) {
            return prefix + serverPaths.getPathSanitizer().apply(extCtx.userName) + suffix;
        }

        /**
         * Expand {@code %userid%} to the user's ID.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandUserId(String prefix, String suffix) {
            return prefix + extCtx.userId + suffix;
        }

        /**
         * Expand {@code %group%} to the group's name.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandGroup(String prefix, String suffix) {
            return prefix + serverPaths.getPathSanitizer().apply(extCtx.groupName) + suffix;
        }

        /**
         * Expand {@code %groupid%} to the group's ID.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandGroupId(String prefix, String suffix) {
            return prefix + extCtx.groupId + suffix;
        }

        /**
         * Expand {@code %year%} to the current year.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandYear(String prefix, String suffix) {
            return prefix + now.get(Calendar.YEAR) + suffix;
        }

        /**
         * Expand {@code %month%} to the current month number.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandMonth(String prefix, String suffix) {
            return prefix + String.format("%02d", now.get(Calendar.MONTH) + 1) + suffix;
        }

        /**
         * Expand {@code %monthname%} to the current month name.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandMonthname(String prefix, String suffix) {
            return prefix + DATE_FORMAT.getMonths()[now.get(Calendar.MONTH)] + suffix;
        }

        /**
         * Expand {@code %day%} to the current day number in the month.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandDay(String prefix, String suffix) {
            return prefix + String.format("%02d", now.get(Calendar.DAY_OF_MONTH)) + suffix;
        }

        /**
         * Expand {@code %time%} to the current hour, minute, second and millisecond.
         * The directory is actually created.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @throws ServerError if the directory could not be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public void expandAndCreateTime(final String prefix, final String suffix) throws ServerError {
            final MakeNextDirectory directoryMaker = new MakeNextDirectory() {

                @Override
                public List<String> getPathFor(long index) {
                    final int hour = now.get(Calendar.HOUR_OF_DAY);
                    final int minute = now.get(Calendar.MINUTE);
                    final int second = now.get(Calendar.SECOND);
                    final long millisecond = now.get(Calendar.MILLISECOND) + index;
                    final String time = String.format("%s%02d-%02d-%02d.%03d%s", prefix, hour, minute, second, millisecond, suffix);
                    return Collections.singletonList(time);
                }

                @Override
                public boolean isAcceptable(List<String> path) throws ServerError {
                    return !checkPath(getFullPathWith(path), null).exists();
                }

                @Override
                public void usePath(List<String> path) throws ServerError {
                    makeDir(getFullPathWith(path), false);
                }
            };

            done.addAll(directoryMaker.useFirstAcceptable());
        }

        /**
         * Expand {@code %session%} to the session's UUID.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandSession(String prefix, String suffix) {
            return prefix + extCtx.sessionUuid + suffix;
        }

        /**
         * Expand {@code %sessionid%} to the session's ID.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandSessionId(String prefix, String suffix) {
            return prefix + extCtx.sessionId + suffix;
        }

        /**
         * Expand {@code %perms%} to the group's permissions.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandPerms(String prefix, String suffix) {
            return prefix + extCtx.groupPermissions + suffix;
        }

        /**
         * Expand {@code %institution%} to the user's institution, omitting this component if they do not have one.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandInstitution(String prefix, String suffix) {
            final String institution;
            if (current == null) {
                institution = repositoryDao.getUserInstitution(extCtx.userId, sf);
            } else {
                institution = repositoryDao.getUserInstitution(extCtx.userId, current);
            }
            if (StringUtils.isBlank(institution)) {
                return null;
            } else {
                return prefix + serverPaths.getPathSanitizer().apply(institution) + suffix;
            }
        }

        /**
         * Expand {@code %institution%} to the user's institution, using a default if they do not have one.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @param defaultForNone the string to use as the institution of users who do not have one set for them
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandInstitution(String prefix, String suffix, String defaultForNone) {
            String institution;
            if (current == null) {
                institution = repositoryDao.getUserInstitution(extCtx.userId, sf);
            } else {
                institution = repositoryDao.getUserInstitution(extCtx.userId, current);
            }
            if (StringUtils.isBlank(institution)) {
                institution = defaultForNone;
            }
            return prefix + serverPaths.getPathSanitizer().apply(institution) + suffix;
        }

        /**
         * Expand {@code %hash%} to a consistent hash of eight hexadecimal digits.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         * @throws ServerError if the expansion term was improperly specified
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandHash(String prefix, String suffix) throws ServerError {
            return expandHash(prefix, suffix, "8");
        }

        /**
         * Expand {@code %hash%} to a consistent hash of the given number of hexadecimal digits.
         * Further comma-separated digits use more of the hash in subdirectories.
         * @param prefix path component text preceding the expansion term in the first directory, may be empty
         * @param suffix path component text following the expansion term in the first directory, may be empty
         * @param parameters a comma-separated list of how many hexadecimal digits of the hash to use in each directory
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         * @throws ServerError if the expansion term was improperly specified
         */
        // @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandHash(String prefix, String suffix, String parameters) throws ServerError {
            if (consistentData == null) {
                throw new ServerError(null, null, "%hash% is prohibited in this part of the repository template path");
            }
            /* simple zero-padding regardless of the hash code's sign */
            final String hash = Long.toHexString(0x200000000l + consistentData.hashCode()).substring(1).toUpperCase();
            final Deque<String> components = new ArrayDeque<String>();
            int currentPosition = 0;
            for (final String digitCount : Splitter.on(',').split(parameters)) {
                final int length = Integer.parseInt(digitCount);
                if (length < 1 || length + currentPosition > hash.length()) {
                    throw new ServerError(null, null,
                            "invalid parameters \"" + parameters + "\" for %hash% in the repository template path");
                }
                components.push(prefix + hash.substring(currentPosition, currentPosition + length) + suffix);
                currentPosition += length;
                /* apply prefix and suffix to first directory only */
                prefix = "";
                suffix = "";
            }
            while (!components.isEmpty()) {
                remaining.push(components.pop());
            }
            return null;
        }

        /**
         * Expand {@code %increment%} to a uniquely named directory, counting by natural numbers.
         * The directory is actually created.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @throws ServerError if the directory could not be created or the expansion term was improperly specified
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public void expandAndCreateIncrement(String prefix, String suffix) throws ServerError {
            expandAndCreateIncrement(prefix, suffix, "0");
        }

        /**
         * Expand {@code %increment%} to a uniquely named directory, counting by natural numbers.
         * The directory is actually created.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @param paddingString the minimum number of digits for the natural number, achieved by zero-padding if necessary
         * @throws ServerError if the directory could not be created or the expansion term was improperly specified
         */
        // @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public void expandAndCreateIncrement(final String prefix, final String suffix, String paddingString) throws ServerError {
            final int padding = Integer.parseInt(paddingString);

            final MakeNextDirectory directoryMaker = new MakeNextDirectory() {

                @Override
                public List<String> getPathFor(long index) {
                    return Collections.singletonList(prefix + Strings.padStart(Long.toString(index + 1), padding, '0') + suffix);
                }

                @Override
                public boolean isAcceptable(List<String> path) throws ServerError {
                    return !checkPath(getFullPathWith(path), null).exists();
                }

                @Override
                public void usePath(List<String> path) throws ServerError {
                    makeDir(getFullPathWith(path), false);
                }
            };

            done.addAll(directoryMaker.useFirstAcceptable());
        }

        /**
         * Get the extra directories that correspond to the given natural number.
         * @param prefix path component text preceding the expansion term in the first directory, may be empty
         * @param suffix path component text following the expansion term in the first directory, may be empty
         * @param digits the power of ten that is the directory entry limit, e.g., {@code "3"} for one thousand
         * @param count the natural number identifying the set of extra directories
         * @return the extra directories
         */
        private List<String> getExtraSubdirectories(String prefix, String suffix, int digits, long count) {
            final List<String> subdirectories = new ArrayList<String>();
            StringBuffer paddedCount = new StringBuffer();
            paddedCount.append(count);
            /* make padded.length() a multiple of digits by zero padding */
            while (paddedCount.length() % digits != 0) {
                paddedCount.insert(0, '0');
            }
            /* and work through the digits-length groups */
            for (int c = 0, l = paddedCount.length(); c < l; c += digits) {
                subdirectories.add(prefix + paddedCount.substring(c, c + digits) + suffix);
                /* apply prefix and suffix to first directory only */
                prefix = "";
                suffix = "";
            }
            return subdirectories;
        }

        /**
         * Count the entries in the given directory.
         * @param path the repository path for the directory
         * @return the number of entries in the directory, or {@code 0} if the path does not exist but its parent is a directory,
         * or a very large number if the path cannot be created as a directory
         */
        private int directoryContentsCount(String path) {
            final File directory = serverPaths.getServerFileFromFsFile(new FsFile(path));
            if (directory.exists()) {
                if (directory.isDirectory()) {
                    return directory.list().length;
                }
            } else {
                final File parent = directory.getParentFile();
                if (parent != null && parent.exists() && parent.isDirectory()) {
                    return 0;
                }
            }
            return Integer.MAX_VALUE;
        }

        /**
         * Expand {@code %subdirs%} to none or more directories such that the final one contains no more than one thousand entries.
         * These extra directories are added at the point in the path where the component mentioning {@code %subdirs%} occurs, when
         * the preceding directory has become sufficiently full. The directories are actually created.
         * @param prefix path component text preceding the expansion term in the first directory, may be empty
         * @param suffix path component text following the expansion term in the first directory, may be empty
         * @throws ServerError if the directory could not be created or the expansion term was improperly specified
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public void expandAndCreateSubdirs(String prefix, String suffix) throws ServerError {
            expandAndCreateSubdirs(prefix, suffix, "3");
        }

        /**
         * Expand {@code %subdirs%} to none or more directories such that the final one contains no more than a certain number of
         * entries. These extra directories are added at the point in the path where the component mentioning {@code %subdirs%}
         * occurs, when the preceding directory has become sufficiently full. The directories are actually created.
         * @param prefix path component text preceding the expansion term in the first directory, may be empty
         * @param suffix path component text following the expansion term in the first directory, may be empty
         * @param digitsString the power of ten that is the directory entry limit, e.g., {@code "3"} for one thousand
         * @throws ServerError if the directory could not be created or the expansion term was improperly specified
         */
        // @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public void expandAndCreateSubdirs(final String prefix, final String suffix, String digitsString) throws ServerError {
            final int digits = Integer.parseInt(digitsString);
            if (digits < 1) {
                throw new ServerError(null, null,
                        "invalid parameter \"" + digitsString + "\" for %subdirs% in the repository template path");
            }
            final int limit = IntMath.checkedPow(10, digits);
            if (directoryContentsCount(getFullPathWith(Collections.<String>emptyList())) < limit) {
                /* do not yet need to break out into subdirectories */
                return;
            }

            final MakeNextDirectory directoryMaker = new MakeNextDirectory() {

                @Override
                public List<String> getPathFor(long index) {
                    return getExtraSubdirectories(prefix, suffix, digits, index);
                }

                @Override
                public boolean isAcceptable(List<String> path) throws ServerError {
                    return directoryContentsCount(getFullPathWith(path)) < limit;
                }

                @Override
                public void usePath(List<String> path) throws ServerError {
                    makeDir(getFullPathWith(path), true);
                }
            };

            done.addAll(directoryMaker.useFirstAcceptable());
        }

        /**
         * Expand {@code %thread%} to the name of the current thread.
         * @param prefix path component text preceding the expansion term, may be empty
         * @param suffix path component text following the expansion term, may be empty
         * @return entire replaced path component, may be unchanged to be revisited,
         * or {@code null} if it has been wholly processed; otherwise it will be created
         */
        @SuppressWarnings("unused")  /* used by create() via Method.invoke */
        public String expandThread(String prefix, String suffix) {
            return prefix + serverPaths.getPathSanitizer().apply(Thread.currentThread().getName()) + suffix;
        }

        /**
         * Expand and create the template path.
         * @return the path
         * @throws ServerError if the path could not be expanded and created
         */
        FsFile create() throws ServerError {
            while (!remaining.isEmpty()) {
                /* work on next directory component */
                String pattern = remaining.pop();
                Matcher matcher = TEMPLATE_TERM.matcher(pattern);
                boolean isMatcherPristine = true;
                while (pattern != null) {
                    if (matcher.find()) {
                        isMatcherPristine = false;
                    } else {
                        /* no terms still to review in this component */
                        if (isMatcherPristine) {
                            /* and none to revisit, this component is done */
                            done.add(pattern);
                            break;
                        } else {
                            /* no expansions occurred, else the matcher would have been reset */
                            throw new ServerError(null, null,
                                    "cannot expand template repository path component \"" + pattern + '"');
                        }
                    }

                    /* examine the term to expand */
                    final String prefix = pattern.substring(0, matcher.start());
                    final String suffix = pattern.substring(matcher.end());
                    final String term = matcher.group(1);
                    String parameters = matcher.group(3);
                    Method expander;

                    /* try to expand the term */
                    final String oldPattern = pattern;
                    final boolean isTryCreateDirectory = createDirectories &&
                            !(TEMPLATE_TERM.matcher(prefix).find() || TEMPLATE_TERM.matcher(suffix).find());
                    while (true) {
                        try {
                            if (parameters == null) {
                                /* without parameters */
                                try {
                                    /* expand term only */
                                    final String methodName = "expand" + StringUtils.capitalize(term);
                                    expander = getClass().getMethod(methodName, String.class, String.class);
                                    pattern = (String) expander.invoke(this, prefix, suffix);
                                } catch (NoSuchMethodException e1) {
                                    if (isTryCreateDirectory) {
                                        try {
                                            /* expand term and create directory */
                                            final String methodName = "expandAndCreate" + StringUtils.capitalize(term);
                                            expander = getClass().getMethod(methodName, String.class, String.class);
                                            expander.invoke(this, prefix, suffix);
                                            pattern = null;
                                        } catch (NoSuchMethodException e2) {
                                            /* move on */
                                        }
                                    }
                                }
                                /* tried without parameters, so move on */
                                break;
                            } else {
                                /* with parameters */
                                try {
                                    /* expand term only */
                                    final String methodName = "expand" + StringUtils.capitalize(term);
                                    expander = getClass().getMethod(methodName, String.class, String.class, String.class);
                                    pattern = (String) expander.invoke(this, prefix, suffix, parameters);
                                    break;
                                } catch (NoSuchMethodException e1) {
                                    if (isTryCreateDirectory) {
                                        try {
                                            /* expand term and create directory */
                                            final String methodName = "expandAndCreate" + StringUtils.capitalize(term);
                                            expander = getClass().getMethod(methodName, String.class, String.class, String.class);
                                            expander.invoke(this, prefix, suffix, parameters);
                                            pattern = null;
                                            break;
                                        } catch (NoSuchMethodException e2) {
                                            /* try without parameters */
                                        }
                                    }
                                }
                                /* failed with parameters, so try without */
                                log.warn("ignoring parameters \"" + parameters + "\" on \"" + matcher.group(0) +
                                        "\" in repository template path");
                                parameters = null;
                            }
                        } catch (InvocationTargetException e) {
                            /* try to unwrap underlying exception from expansion method invocation */
                            final Throwable cause = e.getCause();
                            if (cause instanceof ServerError) {
                                throw (ServerError) cause;
                            } else if (cause instanceof RuntimeException) {
                                throw (RuntimeException) cause;
                            } else {
                                final String message = "unexpected exception in expanding \"" + pattern + '"';
                                throw new ServerError(null, null, message, cause);
                            }
                        } catch (ReflectiveOperationException e) {
                            final String message = "unexpected exception in expanding \"" + pattern + '"';
                            throw new ServerError(null, null, message, e);
                        }
                    }
                    if (!(pattern == null || oldPattern.equals(pattern))) {
                        /* successful expansion, so match against the new form of this component */
                        matcher = TEMPLATE_TERM.matcher(pattern);
                        isMatcherPristine = true;
                    }
                }
                if (pattern != null && createDirectories) {
                    /* expansion occurred but directory was not yet created */
                    makeDir(new FsFile(done).toString(), !remaining.isEmpty());
                }
            }
            /* all components now processed */
            return new FsFile(done);
        }
    }

    /**
     * Expand the root-owned segment of the template path.
     * @param ctx the event context to apply in expanding terms
     * @param current the method invocation context
     * @return the expanded template path
     * @throws ServerError if the path could not be expanded
     */
    private FsFile expandTemplateRootOwnedPath(ode.sys.EventContext ctx, Current current) throws ServerError {
        return new TemplateDirectoryCreator(FsFile.emptyPath, templateRoot, ctx, null, false, current).create();
    }

    /**
     * Expand the root-owned segment of the template path.
     * @param ctx the event context to apply in expanding terms
     * @param sf the service factory
     * @param session the Hibernate session
     * @param sql the JDBC convenience wrapper
     * @return the expanded template path
     * @throws ServerError if the path could not be expanded
     */
    private FsFile expandTemplateRootOwnedPath(EventContext ctx, ServiceFactory sf, Session session, SqlAction sql)
            throws ServerError {
        return new TemplateDirectoryCreator(FsFile.emptyPath, templateRoot, ctx, null, false, sf, session, sql).create();
    }

    /**
     * Expand the root-owned segment of the template path.
     * @param ctx the event context to apply in expanding terms
     * @param sf the service factory
     * @return the expanded template path
     * @throws ServerError if the path could not be expanded
     */
    /* exposed for unit testing only */
    /*private*/ protected FsFile expandTemplateRootOwnedPath(ode.sys.EventContext ctx, ServiceFactory sf) throws ServerError {
        return new TemplateDirectoryCreator(FsFile.emptyPath, templateRoot, ctx, null, false, sf).create();
    }

    /**
     * Expand and create the user-owned segment of the template path.
     * @param ctx the event context to apply in expanding terms
     * @param rootBase the expanded root-owned segment of the template path
     * @param consistentData the object to hash in expanding {@code %hash%}
     * @param current the method invocation context in which to perform queries and create directories
     * @return the expanded template path
     * @throws ServerError if the path could not be expanded and created
     */
    private FsFile expandAndCreateTemplateUserOwnedPath(ode.sys.EventContext ctx, FsFile rootBase, Object consistentData,
            Current current) throws ServerError {
        return new TemplateDirectoryCreator(rootBase, templateUser, ctx, consistentData, true, current).create();
    }

    /**
     * Expand the root-owned segment of the template path.
     * @param ctx the event context to apply in expanding terms
     * @param rootBase the expanded root-owned segment of the template path
     * @param consistentData the object to hash in expanding {@code %hash%}
     * @param sf the service factory
     * @param session the Hibernate session
     * @param sql the JDBC convenience wrapper
     * @return the expanded template path
     * @throws ServerError if the path could not be expanded
     */
    private FsFile expandAndCreateTemplateUserOwnedPath(EventContext ctx, FsFile rootBase, Object consistentData, ServiceFactory sf,
            Session session, SqlAction sql) throws ServerError {
        return new TemplateDirectoryCreator(rootBase, templateUser, ctx, consistentData, true, sf, session, sql).create();
    }

    /**
     * Expand the template path and create its directories with the correct ownership.
     * @param consistentData the object to hash in expanding {@code %hash%}
     * @param __current the method invocation context
     * @return the expanded template path
     * @throws ServerError if the new path could not be created
     */
    protected FsFile createTemplatePath(Object consistentData, Ice.Current __current) throws ServerError {
        final ode.sys.EventContext ctx = repositoryDao.getEventContext(__current);

        final FsFile rootOwnedExpanded;
        if (FsFile.emptyPath.equals(templateRoot)) {
            rootOwnedExpanded = FsFile.emptyPath;
        } else {
            /* there are some root-owned directories first */
            rootOwnedExpanded = expandTemplateRootOwnedPath(ctx, __current);
            final Current rootCurr = sudo(__current, rootSessionUuid);
            rootCurr.ctx.put(ode.constants.GROUP.value, Long.toString(userGroupId));
            makeDir(rootOwnedExpanded.toString(), true, rootCurr);
        }

        /* now create the user-owned directories */
        final FsFile wholeExpanded = expandAndCreateTemplateUserOwnedPath(ctx, rootOwnedExpanded, consistentData, __current);
        if (wholeExpanded.equals(rootOwnedExpanded)) {
            throw new ode.ApiUsageException(null, null,
                    "no user-owned directories in expanded form of managed repository template path");
        }
        return wholeExpanded;
}

    /**
     * Expand the template path and create its directories with the correct ownership.
     * Performs the work of {@link #createTemplatePath(Object, Current)} except from within an existing transaction.
     * @param consistentData the object to hash in expanding {@code %hash%}
     * @param ctx the event context to apply in expanding terms
     * @param sf the service factory
     * @param session the Hibernate session
     * @param sql the JDBC convenience wrapper
     * @return the expanded template path
     * @throws ServerError if the new path could not be created
     */
    public FsFile createTemplatePath(Object consistentData, EventContext ctx, ServiceFactory sf, Session session, SqlAction sql)
            throws ServerError {
        final FsFile rootOwnedExpanded;
        if (FsFile.emptyPath.equals(templateRoot)) {
            rootOwnedExpanded = FsFile.emptyPath;
        } else {
            /* there are some root-owned directories first */
            rootOwnedExpanded = expandTemplateRootOwnedPath(ctx, sf, session, sql);
            final CheckedPath checked = checkPath(rootOwnedExpanded.toString(), null);
            final Roles roles = securitySystem.getSecurityRoles();
            final Experimenter rootOwner = (Experimenter) session.get(Experimenter.class, roles.getRootId());
            final ExperimenterGroup userGroup = (ExperimenterGroup) session.get(ExperimenterGroup.class, userGroupId);
            final EventContext ec = securitySystem.getEventContext();
            if (!(ec instanceof BasicEventContext)) {
                throw new ServerError(null, null, "inappropriate security context for creating repository template path: " + ec);
            }
            final BasicEventContext bec = (BasicEventContext) ec;
            final Experimenter currentUser = bec.getOwner();
            final Experimenter currentSudoer = bec.getSudoer();
            final ExperimenterGroup currentGroup = bec.getGroup();
            try {
                session.flush();
                bec.setOwner(rootOwner);
                bec.setSudoer(currentUser);
                bec.setGroup(userGroup, userGroup.getDetails().getPermissions());
                makeDir(checked, true, session, sf, sql, ctx);
                session.flush();
            } finally {
                bec.setOwner(currentUser);
                bec.setSudoer(currentSudoer);
                bec.setGroup(currentGroup, currentGroup.getDetails().getPermissions());
            }
        }

        /* now create the user-owned directories */
        final FsFile wholeExpanded = expandAndCreateTemplateUserOwnedPath(ctx, rootOwnedExpanded, consistentData, sf, session, sql);
        if (wholeExpanded.equals(rootOwnedExpanded)) {
            throw new ode.ApiUsageException(null, null,
                    "no user-owned directories in expanded form of managed repository template path");
        }
        return wholeExpanded;
}

    /** Return value for {@link #trimPaths}. */
    private static class Paths {
        final FsFile basePath;
        final List<FsFile> fullPaths;

        Paths(FsFile basePath, List<FsFile> fullPaths) {
            this.basePath = basePath;
            this.fullPaths = fullPaths;
        }
    }

    /**
     * Trim off the start of long client-side paths.
     * @param basePath the common root
     * @param fullPaths the full paths from the common root down to the filename
     * @param readerClass BioFormats reader for data, may be null
     * @return possibly trimmed common root and full paths
     */
    protected Paths trimPaths(FsFile basePath, List<FsFile> fullPaths,
            Class<? extends FormatReader> readerClass) {
        // find how many common parent directories to retain according to BioFormats
        Integer commonParentDirsToRetain = null;
        final String[] localStylePaths = new String[fullPaths.size()];
        int index = 0;
        for (final FsFile fsFile : fullPaths)
            localStylePaths[index++] = serverPaths.getServerFileFromFsFile(fsFile).getAbsolutePath();
        try {
            commonParentDirsToRetain = readerClass.newInstance().getRequiredDirectories(localStylePaths);
        } catch (Exception e) { }

        final List<String> basePathComponents = basePath.getComponents();
        final int baseDirsToTrim;
        if (commonParentDirsToRetain == null) {
            // no help from BioFormats

            // find the length of the shortest path, including file name
            int smallestPathLength;
            if (fullPaths.isEmpty())
                smallestPathLength = 1; /* imaginary file name */
            else {
                smallestPathLength = Integer.MAX_VALUE;
                for (final FsFile path : fullPaths) {
                    final int pathLength = path.getComponents().size();
                    if (smallestPathLength > pathLength)
                        smallestPathLength = pathLength;
                }
            }

            // plan to trim to try to retain a certain number of parent directories
            baseDirsToTrim = smallestPathLength - parentDirsToRetain - (1 /* file name */);
        }
        else
            // plan to trim the common root according to BioFormats' suggestion
            baseDirsToTrim = basePathComponents.size() - commonParentDirsToRetain;
        if (baseDirsToTrim < 0)
            return new Paths(basePath, fullPaths);
        // actually do the trimming
        basePath = new FsFile(basePathComponents.subList(baseDirsToTrim, basePathComponents.size()));
        final List<FsFile> trimmedPaths = new ArrayList<FsFile>(fullPaths.size());
        for (final FsFile path : fullPaths) {
            final List<String> pathComponents = path.getComponents();
            trimmedPaths.add(new FsFile(pathComponents.subList(baseDirsToTrim, pathComponents.size())));
        }
        return new Paths(basePath, trimmedPaths);
    }

    /**
     * Take a relative path that the user would like to see in his or her
     * upload area, and provide an import location instance whose paths
     * correspond to existing directories corresponding to the sanitized
     * file paths.
     * @param relPath Path parsed from the template
     * @param basePath Common base of all the listed paths ("/my/path")
     * @param reader BioFormats reader for data, may be null
     * @param checksumAlgorithm the checksum algorithm to use in verifying the integrity of uploaded files
     * @return {@link ImportLocation} instance
     */
    protected ImportLocation suggestImportPaths(FsFile relPath, FsFile basePath, List<FsFile> paths,
            Class<? extends FormatReader> reader, ChecksumAlgorithm checksumAlgorithm, Ice.Current __current)
                    throws ode.ServerError {
        final Paths trimmedPaths = trimPaths(basePath, paths, reader);
        basePath = trimmedPaths.basePath;
        paths = trimmedPaths.fullPaths;

        // sanitize paths (should already be sanitary; could introduce conflicts)
        final Function<String, String> sanitizer = serverPaths.getPathSanitizer();
        relPath = relPath.transform(sanitizer);
        basePath = basePath.transform(sanitizer);
        int index = paths.size();
        while (--index >= 0) {
            paths.set(index, paths.get(index).transform(sanitizer));
        }

        // Static elements which will be re-used throughout
        final ManagedImportLocationI data = new ManagedImportLocationI(); // Return value
        data.logFile = checkPath(relPath.toString()+".log", checksumAlgorithm, __current);

        // try actually making directories
        final FsFile newBase = FsFile.concatenate(relPath, basePath);
        data.sharedPath = newBase.toString();
        data.usedFiles = new ArrayList<String>(paths.size());
        data.checkedPaths = new ArrayList<CheckedPath>(paths.size());
        for (final FsFile path : paths) {
            final String relativeToEnd = path.getPathFrom(basePath).toString();
            data.usedFiles.add(relativeToEnd);
            final String fullRepoPath = data.sharedPath + FsFile.separatorChar + relativeToEnd;
            data.checkedPaths.add(new CheckedPath(this.serverPaths, fullRepoPath,
                    this.checksumProviderFactory, checksumAlgorithm));
        }

        // Assuming we reach here, then we need to make
        // sure that the directory exists since the call
        // to saveFileset() requires the parent dirs to
        // exist.
        List<CheckedPath> dirs = new ArrayList<CheckedPath>();
        Set<String> seen = new HashSet<String>();
        dirs.add(checkPath(data.sharedPath, checksumAlgorithm, __current));
        for (CheckedPath checked : data.checkedPaths) {
            if (!seen.contains(checked.getRelativePath())) {
                dirs.add(checked.parent());
                seen.add(checked.getRelativePath());
            }
        }
        repositoryDao.makeDirs(this, dirs, true, __current);

        return data;
    }

    /**
     * @param x a collection of items, not {@code null}
     * @param y a collection of items, not {@code null}
     * @return if the collections have the same items in the same order, or if one is a prefix of the other
     */
    private static boolean isConsistentPrefixes(Iterable<?> x, Iterable<?> y) {
        final Iterator<?> xIterator = x.iterator();
        final Iterator<?> yIterator = y.iterator();
        while (xIterator.hasNext() && yIterator.hasNext()) {
            if (!xIterator.next().equals(yIterator.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks for the top-level user directory restriction before calling
     * {@link PublicRepositoryI#makeCheckedDirs(LinkedList, boolean, Session, ServiceFactory, SqlAction, EventContext)}
     */
    @Override
    protected void makeCheckedDirs(final LinkedList<CheckedPath> paths,
            boolean parents, Session s, ServiceFactory sf, SqlAction sql,
            EventContext effectiveEventContext) throws ServerError {
        final IAdmin adminService = sf.getAdminService();
        final ode.sys.EventContext ec = IceMapper.convert(effectiveEventContext);
        final FsFile rootOwnedPath = expandTemplateRootOwnedPath(ec, sf);
        final List<CheckedPath> pathsToFix = new ArrayList<CheckedPath>();
        List<CheckedPath> pathsForRoot;

        /* if running as root then the paths must be root-owned */
        final EventContext currentEventContext = adminService.getEventContext();
        final long rootId = adminService.getSecurityRoles().getRootId();
        if (currentEventContext.getCurrentUserId() == rootId) {
            pathsForRoot = ImmutableList.copyOf(paths);
        } else {
            pathsForRoot = ImmutableList.of();
        }

        for (int i = 0; i < paths.size(); i++) {

            CheckedPath checked = paths.get(i);
            if (checked.isRoot) {
                // This shouldn't happen but just in case.
                throw new ResourceError(null, null, "Cannot re-create root!");
            }

            if (isConsistentPrefixes(rootOwnedPath.getComponents(), checked.fsFile.getComponents())) {
                /* the path is consistent with the root-owned template path directories for import */
            } else if (currentEventContext.isCurrentUserAdmin()) {
                /* the path violates the root-owned template path but the current user is an administrator */
                pathsForRoot = ImmutableList.copyOf(paths);
            } else {
                throw new ode.ValidationException(null, null,
                        "cannot create directory \"" + checked.fsFile
                        + "\" with template path's root-owned \"" + rootOwnedPath + "\"");
            }

            pathsToFix.add(checked);
        }

        final WeakReference<Consumer<CheckedPath>> maybeFileCreationListener = fileCreationListeners.get();
        final Consumer<CheckedPath> fileCreationListener;
        if (maybeFileCreationListener != null) {
            fileCreationListener = maybeFileCreationListener.get();
        } else {
            fileCreationListener = null;
        }
        super.makeCheckedDirs(paths, parents, s, sf, sql, null, fileCreationListener);

        /* ensure that root segment of the template path is wholly root-owned */
        if (!pathsForRoot.isEmpty()) {
            final Experimenter rootUser = sf.getQueryService().find(Experimenter.class, rootId);
            final IUpdate updateService = sf.getUpdateService();
            for (final CheckedPath pathForRoot : pathsForRoot) {
                final OriginalFile directory = repositoryDao.findRepoFile(sf, sql, getRepoUuid(), pathForRoot, null);
                if (directory.getDetails().getOwner().getId() != rootId) {
                    directory.getDetails().setOwner(rootUser);
                    updateService.saveObject(directory);
                }
            }
        }

        // Now that we know that these are the right directories for
        // the current user, we make sure that the directories are in
        // the user group.
        repositoryDao.createOrFixUserDir(getRepoUuid(), pathsToFix, s, sf, sql);
    }

    @Override
    protected final CheckedPath checkPath(String path, ChecksumAlgorithm checksumAlgorithm, final Ice.Current curr)
            throws ValidationException {
        /* This a final method so it cannot be subclassed to use Ice.Current. The superclass does not use the invocation context. */
        return super.checkPath(path, checksumAlgorithm, null);
    }

    /**
     * @see PublicRepositoryI#checkPath(String, ChecksumAlgorithm, Current)
     */
    protected final CheckedPath checkPath(String path, ChecksumAlgorithm checksumAlgorithm) throws ValidationException {
        return checkPath(path, checksumAlgorithm, null);
    }

    /**
     * @see PublicRepositoryI#makeDir(CheckedPath, boolean, Session, ServiceFactory, SqlAction, EventContext)
     */
    public void makeDir(String path, boolean parents, Session session, ServiceFactory sf, SqlAction sql,
            ode.system.EventContext effectiveEventContext) throws ServerError {
        final CheckedPath checked = checkPath(path, null);
        makeDir(checked, parents, session, sf, sql, effectiveEventContext);
    }
}