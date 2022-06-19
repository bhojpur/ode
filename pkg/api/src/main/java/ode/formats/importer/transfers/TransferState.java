package ode.formats.importer.transfers;

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
import java.util.HashMap;
import java.util.Map;

import ode.formats.importer.ImportEvent;
import ode.formats.importer.ImportLibrary;
import ode.formats.importer.util.TimeEstimator;
import ode.util.checksum.ChecksumProvider;
import ode.ServerError;
import ode.api.RawFileStorePrx;
import ode.grid.ImportProcessPrx;
import ode.model.OriginalFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Non-thread-safe argument holder for {@link FileTransfer} implementations.
 * A single instance will be created per invocation of
 * {@link FileTransfer#transfer(TransferState)}. Several instance methods are
 * provided for common reporting actions (See usage in existing
 * {@link FileTransfer} implementations.
 */
public class TransferState implements TimeEstimator {

    private static final Logger log = LoggerFactory.getLogger(TransferState.class);

    private final File file;

    private final long length;

    private final int index;

    private final int total;

    private final ImportProcessPrx proc;

    private final ImportLibrary library;

    private final TimeEstimator estimator;

    private final ChecksumProvider cp;

    private final byte[] buf;

    private OriginalFile ofile;

    private String checksum;

    /**
     * Cache of the latest return value from
     * {@link #getUploader(String)} which can be used to cleanup
     * server state.
     */
    private RawFileStorePrx prx;

    /**
     * State of the current file transfer.
     *
     * @param file Source file which is to be transferred.
     * @param index Which of the total files to upload this is.
     * @param total Total number of files to upload.
     * @param proc {@link ImportProcessPrx} which is being imported to.
     * @param library {@link ImportLibrary} to use for notifications.
     * @param estimator a time-to-completion estimator.
     * @param cp a checksum provider, for calculating file content checksums.
     * @param buf optional buffer. Need not be used or updated.
     * @throws IOException I/O exception
     * @throws ServerError server error
     */
    public TransferState(File file,
            int index, int total, // as index of
            ImportProcessPrx proc, // to
            ImportLibrary library,
            TimeEstimator estimator,
            ChecksumProvider cp,
            byte[] buf) throws IOException, ServerError {
                this.file = file;
                this.length = file.length();
                this.index = index;
                this.total = total;
                this.proc = proc;
                this.library = library;
                this.estimator = estimator;
                this.cp = cp;
                this.buf = buf;
            }

    /**
     * Calls {@link RawFileStorePrx#save()} and stores the resultant
     * {@link OriginalFile} for future inspection along with the <em>local</em>
     * checksum. (The remote checksum is available from the
     * {@link OriginalFile}.
     *
     * @throws ServerError server error
     */
    public void save() throws ServerError {
        // We don't need write access here, and considering that
        // a symlink or similar to a non-executable file may have
        // replaced the previous test file (see checkLocation), we
        // try to be as conservative as possible.
        RawFileStorePrx rawFileStore = getUploader("r");
        checksum = cp.checksumAsString();
        ofile = rawFileStore.save();
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s/%s id=%s",
                    ofile.getPath().getValue(),
                    ofile.getName().getValue(),
                    ofile.getId().getValue()));
            log.debug(String.format("checksums: client=%s,server=%s",
                    checksum, ofile.getHash().getValue()));
        }
    }

    //
    // ACCESSORS
    //

    /**
     * <em>(Not thread safe)</em> Get a moderately large buffer for use in
     * reading/writing data. To prevent the creation of many MB-sized byte
     * arrays, this value can be re-used but requires external synchronization.
     * @return the buffer
     */
    public byte[] getBuffer() {
        return this.buf;
    }

    /**
     * Get the digest string for the local file. This will only be available,
     * i.e. non-null, after {@link #save()} has been called.
     * @return the checksum
     */
    public String getChecksum() {
        return this.checksum;
    }

    /**
     * Get the {@link ChecksumProvider} passed to the constructor.
     * Since the {@link ChecksumProvider} has a number of different usage styles,
     * {@link TransferState} doesn't attempt to delegate but just returns the
     * instance.
     * @return the checksum provider used for calculating the checksum
     */
    public ChecksumProvider getChecksumProvider() {
        return this.cp;
    }

    /**
     * Return the target file passed to the constructor.
     * @return the source file
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Return the length of the {@link #getFile() target file}.
     * @return the length of the source file
     */
    public long getLength() {
        return this.length;
    }

    /**
     * Find original file as defined by the ID in the {@link RawFileStorePrx}
     * regardless of group.
     * @return the original file from the upload process
     * @throws ServerError server error
     */
    public OriginalFile getOriginalFile() throws ServerError {
        return library.loadOriginalFile(getUploader());
    }

    /**
     * Find original file represented by the managed repository that
     * import is taking place to.
     * @return the original file at the root of the repository targeted by the upload process
     * @throws ServerError server error
     */
    public OriginalFile getRootFile() throws ServerError {
        return library.lookupManagedRepository().root();
    }

    /**
     * @return the {@link RawFileStorePrx} instance for this index
     * @throws ServerError server error
     */
    public RawFileStorePrx getUploader() throws ServerError {
        return getUploader(null);
    }

    /**
     * Return the {@link RawFileStorePrx} instance for this index setting
     * the mode if not null. Valid values include "r" and "rw". If a non-null
     * uploader is available, it will be returned <em>instead</em>.
     *
     * <em>Every</em> instance which is returned from this method should
     * eventually have {@link RawFileStorePrx#close()} called on it.
     * {@link RawFileStorePrx#close()} can be used to facilitate this.
     * @param mode the mode as understood by
     * {@link ode.services.server.repo.PublicRepositoryI#file(String, String, Ice.Current)}
     * @return the {@link RawFileStorePrx} instance
     * @throws ServerError server error
     */
    public RawFileStorePrx getUploader(String mode) throws ServerError {
        if (prx != null) {
            return prx;
        } else if (mode != null) {
            Map<String, String> ctx = new HashMap<String, String>();
            ctx.put("ode.fs.mode", mode);
            prx = this.proc.getUploader(this.index, ctx);
        } else {
            prx = this.proc.getUploader(this.index);
        }
        return prx;
    }

    /**
     * Call {@link RawFileStorePrx#close()} on the cached uploader
     * instance if non-null and null the instance. If
     * {@link Ice.ObjectNotExistException} is thrown, the service is
     * assumed closed. All other exceptions will be printed at WARN.
     */
    public void closeUploader() {
        if (prx != null) {
            try {
                prx.close();
            } catch (Ice.ObjectNotExistException onee) {
                // no-op
            } catch (Exception e) {
                log.warn("Exception closing " + prx, e);
            } finally {
                prx = null;
            }
        }
    }

    //
    // NOTIFICATIONS AND LOGGING
    //

    /**
     * Raise the {@link ode.formats.importer.ImportEvent.FILE_UPLOAD_STARTED}
     * event to all observers.
     */
    public void uploadStarted() {
        library.notifyObservers(
                new ImportEvent.FILE_UPLOAD_STARTED(
                file.getAbsolutePath(), index, total,
                null, length, null));
    }

    /**
     * Raise the {@link ode.formats.importer.ImportEvent.FILE_UPLOAD_BYTES}
     * event to all observers.
     * @param offset how many bytes are uploaded
     */
    public void uploadBytes(long offset) {
        library.notifyObservers(
                new ImportEvent.FILE_UPLOAD_BYTES(
                file.getAbsolutePath(), index, total,
                offset, length, estimator.getUploadTimeLeft(), null));
    }

    /**
     * Raise the {@link ode.formats.importer.ImportEvent.FILE_UPLOAD_COMPLETE}
     * event to all observers.
     * @param offset how many bytes are uploaded
     */
    public void uploadComplete(long offset) {
        library.notifyObservers(new ImportEvent.FILE_UPLOAD_COMPLETE(
                file.getAbsolutePath(), index, total,
                offset, length, null));
    }

    //
    // ESTIMATOR DELEGATION
    //

    public void start() {
        this.estimator.start();
    }

    public void stop() {
        this.estimator.stop();
    }

    public void stop(long uploadedBytes) {
        this.estimator.stop(uploadedBytes);
    }

    public long getUploadTimeLeft() {
        return this.estimator.getUploadTimeLeft();
    }

}