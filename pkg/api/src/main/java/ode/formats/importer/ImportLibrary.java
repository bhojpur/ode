package ode.formats.importer;

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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import loci.common.Location;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.exclusions.FileExclusion;
import ode.formats.importer.targets.ImportTarget;
import ode.formats.importer.transfers.FileTransfer;
import ode.formats.importer.transfers.TransferState;
import ode.formats.importer.transfers.UploadFileTransfer;
import ode.formats.importer.util.ClientKeepAlive;
import ode.formats.importer.util.ErrorHandler;
import ode.formats.importer.util.ProportionalTimeEstimatorImpl;
import ode.formats.importer.util.TimeEstimator;
import ode.services.server.repo.path.ClientFilePathTransformer;
import ode.services.server.repo.path.FilePathRestrictionInstance;
import ode.services.server.repo.path.FilePathRestrictions;
import ode.services.server.repo.path.MakePathComponentSafe;
import ode.services.server.util.ChecksumAlgorithmMapper;
import ode.util.checksum.ChecksumProvider;
import ode.util.checksum.ChecksumProviderFactory;
import ode.util.checksum.ChecksumProviderFactoryImpl;
import ode.util.checksum.ChecksumType;
import ode.ChecksumValidationException;
import ode.ServerError;
import ode.api.IMetadataPrx;
import ode.api.RawFileStorePrx;
import ode.api.ServiceFactoryPrx;
import ode.cmd.CmdCallbackI;
import ode.cmd.ERR;
import ode.cmd.HandlePrx;
import ode.cmd.Response;
import ode.cmd.Status;
import ode.grid.ImportProcessPrx;
import ode.grid.ImportRequest;
import ode.grid.ImportResponse;
import ode.grid.ImportSettings;
import ode.grid.ManagedRepositoryPrx;
import ode.grid.ManagedRepositoryPrxHelper;
import ode.grid.RepositoryMap;
import ode.grid.RepositoryPrx;
import ode.model.Annotation;
import ode.model.ChecksumAlgorithm;
import ode.model.Dataset;
import ode.model.Fileset;
import ode.model.FilesetI;
import ode.model.IObject;
import ode.model.OriginalFile;
import ode.model.Pixels;
import ode.model.Screen;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import Ice.Current;

/**
 * support class for the proper usage of {@link ODEMetadataStoreClient} and
 * {@link FormatReader} instances. This library was factored out of
 * ImportHandler to support ImportFixture
 *
 * @see FormatReader
 * @see ODEMetadataStoreClient
 * @see ImportFixture
 * @see IObservable
 * @see IObserver
 */
public class ImportLibrary implements IObservable
{
    private static Logger log = LoggerFactory.getLogger(ImportLibrary.class);

    /* checksum provider factory for verifying file integrity in upload */
    private static final ChecksumProviderFactory checksumProviderFactory = new ChecksumProviderFactoryImpl();

    /* the checksum algorithms available from the checksum provider factory */
    private static final ImmutableList<ChecksumAlgorithm> availableChecksumAlgorithms;

    private final ArrayList<IObserver> observers = new ArrayList<IObserver>();

    private final ODEMetadataStoreClient store;

    private final ManagedRepositoryPrx repo;

    private final ServiceFactoryPrx sf;

    /**
     * Method used for transferring files to the server.
     */
    private final FileTransfer transfer;

    /**
     * Voters which can choose to skip a given import.
     */
    private final List<FileExclusion> exclusions = new ArrayList<FileExclusion>();

    /**
     * Minutes to wait for an import to take place. If 0 is set, then no waiting
     * will take place and an empty list of objects will be returned. If negative,
     * then the process will loop indefinitely (default). Otherwise, the given
     * number of minutes will be waited until throwing a {@link LockTimeout}.
     */
    private final int minutesToWait;

    /**
     * Adapter for use with any callbacks created by the library.
     */
    private final Ice.ObjectAdapter oa;

    /**
     * Router category which allows callbacks to be accessed behind a firewall.
     */
    private final String category;

    /**
     * {@link ExecutorService} used to process fileset imports.
     */
    private ExecutorService filesetThreadPool;

    /**
     * {@link ExecutorService} used to upload individual files.
     */
    private ExecutorService uploadThreadPool;

    /**
     * Flag stating that this instance has received an {@link ImportEvent.LOGGED_OUT} event.
     */
    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private ConcurrentLinkedQueue<ImportCallback> callbacks = new ConcurrentLinkedQueue();

    static {
        final Set<ChecksumType> availableTypes = checksumProviderFactory.getAvailableTypes();
        final ImmutableList.Builder<ChecksumAlgorithm> builder = ImmutableList.builder();
        for (final ChecksumAlgorithm checksumAlgorithm : ChecksumAlgorithmMapper.getAllChecksumAlgorithms()) {
            final ChecksumType checksumType =  ChecksumAlgorithmMapper.getChecksumType(checksumAlgorithm);
            if (availableTypes.contains(checksumType)) {
                builder.add(checksumAlgorithm);
            }
        }
        availableChecksumAlgorithms = builder.build();
    }

    /**
     * The default implementation of {@link FileTransfer} performs a
     * no-op and therefore need not have
     * {@link FileTransfer#afterTransfer(int, List)} as with the
     * {@link #ImportLibrary(ODEMetadataStoreClient, ODEWrapper, FileTransfer)}
     * constructor.
     *
     * @param client client-side {@link loci.formats.meta.MetadataStore}, not null
     * @param reader a Bio-Formats reader (ignored), not null
     */
    public ImportLibrary(ODEMetadataStoreClient client, ODEWrapper reader)
    {
        this(client, reader, new UploadFileTransfer());
    }

    /**
     * The library will not close the client instance. The reader will be closed
     * between calls to import.
     *
     * <em>Note:</em> the responsibility of closing
     * {@link FileTransfer#afterTransfer(int, List)} falls to invokers of this
     * method.
     *
     * @param client client-side {@link loci.formats.meta.MetadataStore}, not null
     * @param reader a Bio-Formats reader (ignored), not null
     * @param transfer how files are to be transferred to the server
     */
    public ImportLibrary(ODEMetadataStoreClient client, ODEWrapper reader,
            FileTransfer transfer)
    {
        this(client, reader, transfer, -1);
    }

    public ImportLibrary(ODEMetadataStoreClient client, ODEWrapper reader,
            FileTransfer transfer, int minutesToWait)
    {
        this(client, reader, transfer, null, -1);
    }

    public ImportLibrary(ODEMetadataStoreClient client, ODEWrapper reader,
            FileTransfer transfer, List<FileExclusion> exclusions, int minutesToWait)
    {
        if (client == null || reader == null)
        {
            throw new NullPointerException(
            "All arguments to ImportLibrary() must be non-null.");
        }

        this.store = client;
        this.transfer = transfer;
        if (exclusions != null) {
            this.exclusions.addAll(exclusions);
        }
        this.minutesToWait = minutesToWait;
        repo = lookupManagedRepository();
        // Adapter which should be used for callbacks. This is more
        // complicated than it needs to be at the moment. We're only sure that
        // the OMSClient has a ServiceFactory (and not necessarily a client)
        // so we have to inspect various fields to get the adapter.
        sf = store.getServiceFactory();
        oa = sf.ice_getConnection().getAdapter();
        final Ice.Communicator ic = oa.getCommunicator();
        category = ode.client.getRouter(ic).getCategoryForClient();

        // Propagate a logout from the OMSC to this instance's executors
        ClientKeepAlive cka = store.getKeepAlive();
        if (cka != null) {
            cka.addObserver((observable, event) -> {
                if (event instanceof ImportEvent.LOGGED_OUT) {
                    log.debug("Initializing log out");
                    shutdown.set(true);
                    for (ImportCallback cb : callbacks) {
                        log.debug("Shutting down callback " + cb);
                        cb.shutdown();
                    }
                    if (filesetThreadPool != null) {
                        log.debug("Shutting down fileset thread pool.");
                        filesetThreadPool.shutdown();
                    }
                    if (uploadThreadPool != null) {
                        log.debug("Shutting down upload thread pool.");
                        uploadThreadPool.shutdown();
                    }
                }
            });
        }
    }

    //
    // Observable methods
    //

    public boolean addObserver(IObserver object)
    {
        return observers.add(object);
    }

    public boolean deleteObserver(IObserver object)
    {
        return observers.remove(object);

    }

    /* (non-Javadoc)
     * @see ode.formats.importer.IObservable#notifyObservers(ode.formats.importer.ImportEvent)
     */
    public void notifyObservers(ImportEvent event)
    {
        for (IObserver observer : observers) {
            observer.update(this, event);
        }
    }


    // ~ Actions
    // =========================================================================

    /**
     * Primary user method for importing a number of import candidates.
     * @param config The configuration information.
     * @param candidates Hosts information about the files to import.
     * @return if the import did not exit because of an error
     */
    public boolean importCandidates(final ImportConfig config, ImportCandidates candidates)
    {
        List<ImportContainer> containers = candidates.getContainers();
        if (containers != null) {
            final int count = containers.size();
            filesetThreadPool = Executors.newFixedThreadPool(Math.min(count, config.parallelFileset.get()));
            uploadThreadPool  = Executors.newFixedThreadPool(config.parallelUpload.get());
            try {
                final List<Callable<Boolean>> threads = new ArrayList<>(count);
                for (int index = 0; index < count; index++) {
                    final ImportContainer ic = containers.get(index);
                    final ImportTarget target = config.getTarget();
                    if (config.checksumAlgorithm.get() != null) {
                        ic.setChecksumAlgorithm(config.checksumAlgorithm.get());
                    }
                    final ExecutorService uploadThreadPoolFinal = uploadThreadPool;
                    final int indexFinal = index;
                    threads.add(new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            try {
                                if (target != null) {
                                    try {
                                        IObject obj = target.load(store, ic);
                                        if (!(obj instanceof Annotation)) {
                                            Class <? extends IObject> targetClass = obj.getClass();
                                            while (targetClass.getSuperclass() != IObject.class) {
                                                targetClass = targetClass.getSuperclass().asSubclass(IObject.class);
                                            }
                                            log.info("Import target specifies container: {}:{}",
                                                    targetClass.getSimpleName(), obj.getId().getValue());
                                            ic.setTarget(obj);
                                        } else {
                                            // This is likely a "post-processing" annotation
                                            // so that we don't have to resolve the target
                                            // until later.
                                            ic.getCustomAnnotationList().add((Annotation) obj);
                                        }
                                    } catch (Exception e) {
                                        log.error("Could not load target: {}", target);
                                        throw new RuntimeException("Failed to load target", e);
                                    }
                                }
                                importImage(ic, uploadThreadPoolFinal, indexFinal);
                                return true;
                            } catch (Throwable t) {
                                String message = "Error on import";
                                if (t instanceof ServerError) {
                                    final ServerError se = (ServerError) t;
                                    if (StringUtils.isNotBlank(se.message)) {
                                        message += ": " + se.message;
                                    }
                                }
                                log.error(message, t);
                                if (!config.contOnError.get()) {
                                    log.info("Exiting on error");
                                    return false;
                                } else {
                                    log.info("Continuing after error");
                                    return true;
                                }
                            }
                        }
                    });
                }
                final ExecutorCompletionService<Boolean> threadQueue = new ExecutorCompletionService<>(filesetThreadPool);
                final List<Future<Boolean>> outcomes = new ArrayList<>(count);
                for (final Callable<Boolean> thread : threads) {
                    outcomes.add(threadQueue.submit(thread));
                }
                try {
                    for (int index = 0; index < count; index++) {
                        if (!threadQueue.take().get()) {
                            return false;
                        }
                    }
                } catch (InterruptedException ie) {
                    log.error("import interrupted", ie);
                    return false;
                } catch (ExecutionException ee) {
                    log.error("exception should be handled in other thread", ee);
                    return false;
                }
            } finally {
                if (filesetThreadPool != null) {
                    filesetThreadPool.shutdownNow();
                }
                if (uploadThreadPool != null) {
                    uploadThreadPool.shutdownNow();
                }
            }
        }
        return true;
    }

    /**
     * Delete files from the managed repository.
     * @param container The current import container containing usedFiles to be
     * deleted.
     * @return List of files that could not be deleted.
     * @throws ServerError if file deletion failed
     */
    public List<String> deleteFilesFromRepository(ImportContainer container)
            throws ServerError
    {
        checkManagedRepo();
        // FIXME List<String> undeleted = repo.deleteFiles(container.getUsedFiles());
        // return undeleted;
        return null;
    }

    /**
     * Provide initial configuration to the server in order to create the
     * {@link ImportProcessPrx} which will manage state server-side.
     * @param container the import container
     * @return the new import process from the server
     * @throws ServerError if the import process could not be created
     * @throws IOException if the used files' absolute path could not be found
     */
    public ImportProcessPrx createImport(final ImportContainer container)
        throws ServerError, IOException {
        checkManagedRepo();
        String[] usedFiles = container.getUsedFiles();
        File target = container.getFile();
        if (log.isDebugEnabled()) {
            log.debug("Main file: " + target.getAbsolutePath());
            log.debug("Used files before:");
            for (String f : usedFiles) {
                log.debug(f);
            }
        }

        notifyObservers(new ImportEvent.FILESET_UPLOAD_PREPARATION(
                null, 0, usedFiles.length, null, null, null));

        // Copied to ClientPathExclusion
        // TODO: allow looser sanitization according to server configuration
        final FilePathRestrictions portableRequiredRules =
                FilePathRestrictionInstance.getFilePathRestrictions(FilePathRestrictionInstance.WINDOWS_REQUIRED,
                                                                    FilePathRestrictionInstance.UNIX_REQUIRED);
        final ClientFilePathTransformer sanitizer = new ClientFilePathTransformer(new MakePathComponentSafe(portableRequiredRules));

        final ImportSettings settings = new ImportSettings();
        final Fileset fs = new FilesetI();
        container.fillData(settings, fs, sanitizer, transfer);

        String caStr = container.getChecksumAlgorithm();
        if (caStr != null) {
            settings.checksumAlgorithm = ChecksumAlgorithmMapper.getChecksumAlgorithm(caStr);
        } else {
            // check if the container object has ChecksumAlgorithm
            // present and pass it into the settings object
            settings.checksumAlgorithm = repo.suggestChecksumAlgorithm(availableChecksumAlgorithms);
            if (settings.checksumAlgorithm == null) {
                throw new RuntimeException("no supported checksum algorithm negotiated with server");
            }
        }
        return repo.importFileset(fs, settings);
    }

    /**
     * Upload files to the managed repository.
     *
     * This is done by first passing in the possibly absolute local file paths.
     * A common selection of those are chosen and passed back to the client.
     *
     * As each file is written to the server, a message digest is kept updated
     * of the bytes that are being written. These are then returned to the
     * caller so they can be checked against the values found on the server.
     * @param srcFiles the files to upload
     * @param proc the server import process to use for the upload
     * @return A list of the client-side (i.e. local) hashes for each file.
     */
    public List<String> uploadFilesToRepository(
            final String[] srcFiles, final ImportProcessPrx proc)
    {
        final byte[] buf = new byte[store.getDefaultBlockSize()];
        final int fileTotal = srcFiles.length;
        final List<String> checksums = new ArrayList<String>(fileTotal);
        // TODO Fix with proper code instead of 10000L
        final TimeEstimator estimator = new ProportionalTimeEstimatorImpl(10000L);

        log.debug("Used files created:");
        for (int i = 0; i < fileTotal; i++) {
            try {
                checksums.add(uploadFile(proc, srcFiles, i,
                        checksumProviderFactory, estimator, buf));
            } catch (ServerError e) {
                log.error("Server error uploading file.", e);
                break;
            } catch (IOException e) {
                log.error("I/O error uploading file.", e);
                break;
            }
        }
        return checksums;
    }

    public String uploadFile(final ImportProcessPrx proc,
            final String[] srcFiles, int index, TimeEstimator estimator)
                    throws ServerError, IOException
    {
        final byte[] buf = new byte[store.getDefaultBlockSize()];
        return uploadFile(proc, srcFiles, index, checksumProviderFactory,
                estimator, buf);
    }

    public String uploadFile(final ImportProcessPrx proc,
            final String[] srcFiles, final int index,
            final ChecksumProviderFactory cpf, TimeEstimator estimator,
            final byte[] buf)
            throws ServerError, IOException {

        final ChecksumProvider cp = cpf.getProvider(
                ChecksumAlgorithmMapper.getChecksumType(
                        proc.getImportSettings().checksumAlgorithm));

        final File file = new File(Location.getMappedId(srcFiles[index]));

        try {
            return transfer.transfer(new TransferState(
                    file, index, srcFiles.length,
                    proc, this, estimator, cp, buf));
        }
        catch (Exception e) {
            // Required to bump the error count
            notifyObservers(new ErrorHandler.FILE_EXCEPTION(
                    file.getAbsolutePath(), e, srcFiles, "unknown"));
            // The state that we're entering, i.e. exiting upload via error
            notifyObservers(new ImportEvent.FILE_UPLOAD_ERROR(
                    file.getAbsolutePath(), index, srcFiles.length,
                    null, null, e));
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof ServerError) {
                throw (ServerError) e;
            } else if (e instanceof IOException) {
                throw (IOException) e;
            } else {
                String msg = "Unexpected exception thrown!";
                log.error(msg, e);
                throw new RuntimeException(msg, e);
            }
        }

    }

    /**
     * Image import with only one file upload thread.
     * @see #importImage(ImportContainer, ExecutorService, int)
     * @deprecated now used by tests only
     */
    @SuppressWarnings("javadoc")
    @Deprecated
    public List<Pixels> importImage(final ImportContainer container, int index, int numDone, int total) throws Throwable {
        if (filesetThreadPool == null) {
            filesetThreadPool = Executors.newSingleThreadExecutor();
        }
        try {
            return importImage(container, filesetThreadPool, index);
        } finally {
            filesetThreadPool.shutdown();
        }
    }

    /**
     * Perform an image import uploading files if necessary.
     * @param container The import container which houses all the configuration
     * values and target for the import.
     * @param threadPool The pool of threads to use in file upload.
     * @param index Index of the import in a set. <code>0</code> is safe if
     * this is a singular import.
     * @return List of Pixels that have been imported.
     * @throws FormatException If there is a Bio-Formats image file format
     * error during import.
     * @throws IOException If there is an I/O error.
     * @throws ServerError If there is an error communicating with the Bhojpur ODE
     * server we're importing into.
     * @throws Throwable If there is some other kind of error during import.
     */
    public List<Pixels> importImage(final ImportContainer container, ExecutorService threadPool, int index)
            throws FormatException, IOException, Throwable
    {
        HandlePrx handle;
        for (FileExclusion exclusion : exclusions) {
            Boolean veto = exclusion.suggestExclusion(store.getServiceFactory(),
                    container);
            if (Boolean.TRUE.equals(veto)) {
                notifyObservers(new ImportEvent.FILESET_EXCLUSION(
                container.getFile().getAbsolutePath(), 0,
                container.getUsedFiles().length));
                return Collections.emptyList();
            }
        }
        final IObject target = container.getTarget();
        if (target != null && !target.getDetails().getPermissions().canLink()) {
            /* stop this import before file upload commences */
            final String message = "Cannot link to target";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        final ImportProcessPrx proc = createImport(container);
        final String[] srcFiles = container.getUsedFiles();
        final ThreadLocal<byte[]> buf = new ThreadLocal<byte[]>() {
            @Override
            protected byte[] initialValue() {
                return new byte[store.getDefaultBlockSize()];
            }
        };
        final TimeEstimator estimator = new ProportionalTimeEstimatorImpl(
                container.getUsedFilesTotalSize());
        Map<Integer, String> failingChecksums = new HashMap<Integer, String>();

        notifyObservers(new ImportEvent.FILESET_UPLOAD_START(
                null, index, srcFiles.length, null, null, null));

        final List<Callable<Map.Entry<Integer, String>>> threads = new ArrayList<>(srcFiles.length);
        for (int i = 0; i < srcFiles.length; i++) {
            final int fileIndex = i;
            threads.add(new Callable<Map.Entry<Integer, String>>() {
                @Override
                public Map.Entry<Integer, String> call() throws Exception {
                    final String checksum = uploadFile(proc, srcFiles, fileIndex, checksumProviderFactory, estimator, buf.get());
                    return new AbstractMap.SimpleImmutableEntry<>(fileIndex, checksum);
                }});
        }
        final ExecutorCompletionService<Map.Entry<Integer, String>> threadQueue = new ExecutorCompletionService<>(threadPool);
        final List<Future<Map.Entry<Integer, String>>> outcomes = new ArrayList<>(srcFiles.length);
        for (final Callable<Map.Entry<Integer, String>> thread : threads) {
            outcomes.add(threadQueue.submit(thread));
        }
        final String[] checksumArray = new String[srcFiles.length];
        for (index = 0; index < srcFiles.length; index++) {
            try {
                final Map.Entry<Integer, String> outcome = threadQueue.take().get();
                checksumArray[outcode.getKey()] = outcode.getValue();
            } catch (InterruptedException | ExecutionException e) {
                for (final Future<Map.Entry<Integer, String>> outcome : outcomes) {
                    outcode.cancel(true);
                }
                throw e instanceof ExecutionException ? e.getCause() : e;
            }
        }
        final List<String> checksums = Arrays.asList(checksumArray);
        try {
            handle = proc.verifyUpload(checksums);
        } catch (ChecksumValidationException cve) {
            failingChecksums = cve.failingChecksums;
            throw cve;
        } finally {

            try {
                proc.close();
            } catch (Exception e) {
                log.warn("Exception while closing proc", e);
            }


            notifyObservers(new ImportEvent.FILESET_UPLOAD_END(
                    null, index, srcFiles.length, null, null, srcFiles,
                    checksums, failingChecksums, null));
        }

        // At this point the import is running, check handle for number of
        // steps.
        ImportCallback cb = null;
        try {
            cb = createCallback(proc, handle, container);

            if (minutesToWait == 0) {
                log.info("Disconnecting from import process...");
                cb.close(false);
                cb = null;
                handle = null;
                return Collections.emptyList(); // EARLY EXIT
            }

            if (minutesToWait < 0) {
                while (!shutdown.get()) {
                    if (cb.block(5000)) {
                        break;
                    }
                }
            } else {
                cb.loop(minutesToWait * 30, 2000);
            }

            final ImportResponse rsp = cb.getImportResponse();
            if (rsp == null) {
                throw new Exception("Import failure");
            }
            return rsp.pixels;
        } finally {
            if (cb != null) {
                cb.close(true); // Allow cb to close handle
            } else if (handle != null) {
                handle.close();
            }
        }
    }

    public ImportCallback createCallback(ImportProcessPrx proc,
        HandlePrx handle, ImportContainer container) throws ServerError {
        return new ImportCallback(proc, handle, container);
    }

    @SuppressWarnings("serial")
    public class ImportCallback extends CmdCallbackI {

        final ImportContainer container;

        final Long logFileId;

        /**
         * If null, then {@link #onFinished(Response, Status, Current)} has
         * not yet been called with a non-error response. Field is volatile
         * because a separate {@link Thread} will fill in the value.
         */
        volatile ImportResponse importResponse = null;

        public ImportCallback(ImportProcessPrx proc, HandlePrx handle,
                ImportContainer container) throws ServerError {
                super(oa, category, handle);
                this.container = container;
                this.logFileId = loadLogFile();
                initializationDone();
                notifyObservers(new ImportEvent.IMPORT_STARTED(
                        0, this.container,
                        null, null, 0, null, 0, 0, logFileId));
                callbacks.add(this);
        }

        protected Long loadLogFile() throws ServerError {
            final ImportRequest req = (ImportRequest) handle.getRequest();
            final Long fsId = req.activity.getParent().getId().getValue();
            final IMetadataPrx metadataService = sf.getMetadataService();
            final List<Long> rootIds = Collections.singletonList(fsId);
            try {
                final Map<Long, List<IObject>> logMap = metadataService.loadLogFiles(Fileset.class.getName(), rootIds);
                final List<IObject> logs = logMap.get(fsId);
                if (CollectionUtils.isNotEmpty(logs)) {
                    for (final IObject log : logs) {
                        if (log instanceof OriginalFile) {
                            final Long ofId = log.getId().getValue();
                            if (ofId != null) {
                                return ofId;
                            }
                        }
                    }
                }
            } catch (ServerError e) {
                log.debug("failed to load log file", e);
            }
            return null;
        }

        @Override
        public void step(int step, int total, Ice.Current current) {
            if (step == 1) {
                notifyObservers(new ImportEvent.METADATA_IMPORTED(
                        0, container,
                        null, null, 0, null, step, total, logFileId));
            } else if (step == 2) {
                notifyObservers(new ImportEvent.PIXELDATA_PROCESSED(
                        0, container,
                        null, null, 0, null, step, total, logFileId));
            } else if (step == 3) {
                notifyObservers(new ImportEvent.THUMBNAILS_GENERATED(
                        0, container,
                        null, null, 0, null, step, total, logFileId));
            } else if (step == 4) {
                notifyObservers(new ImportEvent.METADATA_PROCESSED(
                        0, container,
                        null, null, 0, null, step, total, logFileId));
            } else if (step == 5) {
                notifyObservers(new ImportEvent.OBJECTS_RETURNED(
                        0, container,
                        null, null, 0, null, step, total, logFileId));
            }
        }

        /**
         * Overridden to handle the end of the process.
         * @see CmdCallbackI#onFinished(Response, Status, Current)
         */
        @Override
        public void onFinished(Response rsp, Status status, Current c)
        {
            waitOnInitialization(); // Need non-null container
            ImportResponse rv = null;
            final ImportRequest req = (ImportRequest) handle.getRequest();
            final Fileset fs = req.activity.getParent();
            if (rsp instanceof ERR) {
                final ERR err = (ERR) rsp;
                final RuntimeException rt = new RuntimeException(
                        String.format(
                        "Failure response on import!\n" +
                        "Category: %s\n" +
                        "Name: %s\n" +
                        "Parameters: %s\n", err.category, err.name,
                        err.parameters));
                notifyObservers(new ErrorHandler.INTERNAL_EXCEPTION(
                        container.getFile().getAbsolutePath(), rt,
                        container.getUsedFiles(), container.getReader()));
            } else if (rsp instanceof ImportResponse) {
                rv = (ImportResponse) rsp;
                if (this.importResponse == null)
                {
                    // Only respond once.
                    notifyObservers(new ImportEvent.IMPORT_DONE(
                        0, container,
                        null, null, 0, null, rv.pixels, fs, rv.objects));
                }
                this.importResponse = rv;
            } else {
                final RuntimeException rt
                    = new RuntimeException("Unknown response: " + rsp);
                notifyObservers(new ErrorHandler.INTERNAL_EXCEPTION(
                        container.getFile().getAbsolutePath(), rt,
                        container.getUsedFiles(), container.getReader()));
            }
            onFinishedDone();
        }

        /**
         * Assumes that users have already waited on proper
         * completion, i.e. that {@link #onFinished(Response, Status, Current)}
         * has been called.
         *
         * @return may be null.
         */
        public ImportResponse getImportResponse()
        {
            waitOnFinishedDone();
            return importResponse;
        }

        /**
         * Trigger all latches to prevent blocking the shutdown of the executors. This may need to be
         * moved into the superclass.
         */
        void shutdown() {
            this.initializationDone();
            this.onFinishedDone();
        }
    }

    // ~ Helpers
    // =========================================================================

    /**
     * Retrieves the first managed repository from the list of current active
     * repositories.
     * @return Active proxy for the legacy repository.
     */
    public ManagedRepositoryPrx lookupManagedRepository()
    {
        try
        {
            ManagedRepositoryPrx rv = null;
            ServiceFactoryPrx sf = store.getServiceFactory();
            RepositoryMap map = sf.sharedResources().repositories();
            for (int i = 0; i < map.proxies.size(); i++)
            {
                RepositoryPrx proxy = map.proxies.get(i);
                if (proxy != null) {
                    rv = ManagedRepositoryPrxHelper.checkedCast(proxy);
                    if (rv != null) {
                        return rv;
                    }
                }
            }
            return null;
        }
        catch (ServerError e)
        {
            throw new RuntimeException(e);
        }
    }

    private void checkManagedRepo() {
        if (repo == null) {
            throw new RuntimeException(
                    "Cannot exclusively use the managed repository.\n\n" +
                    "Likely no ManagedRepositoryPrx is being returned from the server.\n" +
                    "This could point to a recent server crash. Ask your server administrator\n" +
                    "to check for stale .lock files under the Bhojpur ODE data directory. This\n" +
                    "is particularly likely on a server using NFS.\n");
        }
    }

    public void clear()
    {
        try {
            store.setGroup(null);
            store.setCurrentLogFile(null, null);
            store.createRoot();
        } catch (Throwable t) {
            log.error("failed to clear metadata store", t);
        }
    }

    /**
     * Use {@link RawFileStorePrx#getFileId()} in order to load the
     * {@link OriginalFile} that the service argument is acting on.
     *
     * @param uploader not null
     * @return the original file
     * @throws ServerError if the file could not be identified and loaded
     */
    public OriginalFile loadOriginalFile(RawFileStorePrx uploader)
            throws ServerError {
        ode.RLong rid = uploader.getFileId();
        long id = rid.getValue();
        Map<String, String> ctx = new HashMap<String, String>();
        ctx.put("ode.group", "-1");
        return (OriginalFile)
                sf.getQueryService().get("OriginalFile", id, ctx);
    }
}