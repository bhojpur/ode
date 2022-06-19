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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ode.ServerError;
import ode.api.RawFileStorePrx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base {@link FileTransfer} implementation primarily providing the
 * {@link #start(TransferState)} and {@link #finish(TransferState, long)}
 * methods. Also used as the factory for {@link FileTransfer} implementations
 * via {@link #createTransfer(String)}.
 */
public abstract class AbstractFileTransfer implements FileTransfer {

    /**
     * Enum of well-known {@link FileTransfer} names.
     * Note: these values are also in use in the fs.py
     * CLI plugin.
     */
    public enum Transfers {
        ln(HardlinkFileTransfer.class),
        ln_rm(MoveFileTransfer.class),
        ln_s(SymlinkFileTransfer.class),
        cp(CopyFileTransfer.class),
        cp_rm(CopyMoveFileTransfer.class),
        upload(UploadFileTransfer.class),
        upload_rm(UploadRmFileTransfer.class);
        Class<?> kls;
        Transfers(Class<?> kls) {
            this.kls = kls;
        }
    }

    /**
     * Factory method for instantiating {@link FileTransfer} objects from
     * a string. Supported values can be found in the {@link Transfers} enum.
     * Otherwise, a FQN for a class on the classpath should be passed in.
     * @param arg a type of {@link FileTransfer} instance as named among {@link Transfers}
     * @return the new {@link FileTransfer} instance of the requested type
     */
    public static FileTransfer createTransfer(String arg) {
        Logger tmp = LoggerFactory.getLogger(AbstractFileTransfer.class);
        tmp.debug("Loading file transfer class {}", arg);
        try {
            try {
                return (FileTransfer) Transfers.valueOf(arg).kls.newInstance();
            } catch (Exception e) {
                // Assume not in the enum
            }
            Class<?> c = Class.forName(arg);
            return (FileTransfer) c.newInstance();
        } catch (Exception e) {
            tmp.error("Failed to load file transfer class " + arg);
            throw new RuntimeException(e);
        }
    }

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Minimal start method which logs the file, calls
     * {@link TransferState#start()}, and loads the {@link RawFileStorePrx}
     * which any implementation will need.
     *
     * @param state the transfer state
     * @return a raw file store proxy for the upload
     * @throws ServerError if the uploader could not be obtained
     */
    protected RawFileStorePrx start(TransferState state) throws ServerError {
        log.info("Transferring {}...", state.getFile());
        state.start();
        return state.getUploader();
    }

    /**
     * Save the current state to disk and finish all timing and logging.
     *
     * @param state non-null
     * @param offset total length transferred.
     * @return client-side digest string.
     * @throws ServerError if the upload could not be completed and checksummed
     */
    protected String finish(TransferState state, long offset) throws ServerError {
        state.start();
        state.save();
        state.stop();
        state.uploadComplete(offset);
        return state.getChecksum();
    }

    /**
     * Utility method for closing resources.
     *
     * @param rawFileStore possibly null
     * @param stream possibly null
     * @throws ServerError presently not at all as errors are simply logged, but possibly in the future
     */
    protected void cleanupUpload(RawFileStorePrx rawFileStore,
            FileInputStream stream) throws ServerError {
        try {
            if (rawFileStore != null) {
                try {
                    rawFileStore.close();
                } catch (Exception e) {
                    log.debug("error in closing raw file store", e);
                }
            }
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error("I/O in error closing stream", e);
                }
            }
        }

    }

    /**
     * Uses os.name to determine whether or not this JVM is running
     * under Windows. This is mostly used for determining which executables
     * to run.
     */
    protected boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    protected void printLine() {
        log.error("*******************************************");
    }

    /**
     * Method used by subclasses during {@link FileTransfer#afterTransfer(int, List)}
     * if they would like to remove all the files transferred in the set.
     */
    protected void deleteTransferredFiles(int errors, List<String> srcFiles)
        throws CleanupFailure {

        if (errors > 0) {
            printLine();
            log.error("{} error(s) found.", errors);
            log.error("{} cleanup not performed!", getClass().getSimpleName());
            log.error("The following files will *not* be deleted:");
            for (String srcFile : srcFiles) {
                log.error("\t{}", srcFile);
            }
            printLine();
            return;
        }

        List<File> failedFiles = new ArrayList<File>();
        for (String path : srcFiles) {
            File srcFile = new File(path);
            try {
                log.info("Deleting source file {}...", srcFile);
                if (!srcFile.delete()) {
                    throw new RuntimeException("Failed to delete.");
                }
            } catch (Exception e) {
                log.error("Failed to remove source file {}", srcFile);
                failedFiles.add(srcFile);
            }
        }

        if (!failedFiles.isEmpty()) {
            printLine();
            log.error("Cleanup failed!");
            log.error("{} files could not be removed and will need to " +
                "be handled manually", failedFiles.size());
            for (File failedFile : failedFiles) {
                log.error("\t{}", failedFile.getAbsolutePath());
            }
            printLine();
            throw new CleanupFailure(failedFiles);
        }
    }

}