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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import ode.util.checksum.ChecksumProvider;
import ode.ServerError;
import ode.api.RawFileStorePrx;
import ode.model.OriginalFile;

/**
 * Local-only file transfer mechanism which makes use of soft-linking.
 * This is only useful where the command "ln -s source target" will work.
 */
public abstract class AbstractExecFileTransfer extends AbstractFileTransfer {

    private static final String LINE = "\n---------------------------------------------------\n";

    private static final String SEPARATOR = System.getProperty("line.separator");

    /**
     * "Transfer" files by soft-linking them into place. This method is likely
     * re-usable for other general "linking" strategies by overriding
     * {@link #createProcessBuilder(File, File)} and the other protected methods here.
     */
    public String transfer(TransferState state) throws IOException, ServerError {
        RawFileStorePrx rawFileStore = start(state);
        try {
            final OriginalFile root = state.getRootFile();
            final OriginalFile ofile = state.getOriginalFile();
            final File location = getLocalLocation(root, ofile);
            final File file = state.getFile();
            final long length = state.getLength();
            final ChecksumProvider cp = state.getChecksumProvider();
            state.uploadStarted();
            checkLocation(location, rawFileStore); // closes rawFileStore
            state.closeUploader();
            exec(file, location);
            checkTarget(location, state);
            cp.putFile(file.getAbsolutePath());
            state.stop(length);
            state.uploadBytes(length);
            return finish(state, length);
        } finally {
            state.closeUploader();
        }
    }

    /**
     * Build a path of the form "root.path/root.name/file.path/file.name".
     *
     * @param root the root directory
     * @param ofile a path relative to the root
     * @return the assembled path with separators suitable for the local filesystem
     */
    protected File getLocalLocation(OriginalFile root, OriginalFile ofile) {
        StringBuilder sb = new StringBuilder();
        sb.append(root.getPath().getValue());
        sb.append(File.separatorChar);
        sb.append(root.getName().getValue());
        sb.append(File.separatorChar);
        sb.append(ofile.getPath().getValue());
        sb.append(File.separatorChar);
        sb.append(ofile.getName().getValue());
        return new File(sb.toString());
    }

    /**
     * Check that the target location: 1) doesn't exist and 2) is properly
     * written to by the server. If either condition fails, no linking takes
     * place.
     *
     * @param location the source file
     * @param rawFileStore the target on the server
     * @throws ServerError if the raw file store could not be used
     * @throws IOException for problems with the source file
     */
    protected void checkLocation(File location, RawFileStorePrx rawFileStore)
            throws ServerError, IOException {

        final String uuid = UUID.randomUUID().toString();

        // Safety measures
        if (location.exists()) {
            throw new RuntimeException(location + " exists!");
        }

        // First we guarantee that we have the right file
        // If so, we remove it
        try {
            rawFileStore.write(uuid.getBytes(), 0, uuid.getBytes().length);
        } finally {
            rawFileStore.close();
        }
        try {
            if (!location.exists()) {
                throw failLocationCheck(location, "does not exist");
            } else if (!location.canRead()) {
                throw failLocationCheck(location, "cannot be read");
            } else if (!uuid.equals(FileUtils.readFileToString(location))) {
                throw failLocationCheck(location, "does not match check text");
            }
        } finally {
            if (!location.canWrite()) {
                throw failLocationCheck(location, "cannot be modified locally");
            } else {
                boolean deleted = FileUtils.deleteQuietly(location);
                if (!deleted) {
                    throw failLocationCheck(location, "could not be cleaned up");
                }
            }
        }
    }

    protected RuntimeException failLocationCheck(File location, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE);
        sb.append(String.format("Check failed: %s %s!\n", location, msg));
        sb.append("You likely do not have access to the ManagedRepository ");
        sb.append("for in-place import.\n");
        sb.append("Aborting...");
        sb.append(LINE);
        throw new RuntimeException(sb.toString());
    }

    /**
     * Executes a local command and fails on non-0 return codes.
     *
     * @param file the source file
     * @param location the target on the server
     * @throws IOException for problems with the source file
     */
    protected void exec(File file, File location) throws IOException {
        ProcessBuilder pb = createProcessBuilder(file, location);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        Integer rcode = null;
        while (rcode == null) {
            try {
                rcode = process.waitFor();
                break;
            } catch (InterruptedException e) {
                continue;
            }
        }
        if (rcode == null || rcode.intValue() != 0) {
            StringWriter sw = new StringWriter();
            sw.append("transfer process returned: ");
            sw.append(Integer.toString(rcode));
            sw.append("\n");
            sw.append("command:");
            for (String arg : pb.command()) {
                sw.append(" ");
                sw.append(arg);
            }
            sw.append("\n");
            sw.append("output:");
            sw.append(LINE);
            String line = "";
            BufferedReader br = new BufferedReader(
                   new InputStreamReader(process.getInputStream()));
            while ( (line = br.readLine()) != null) {
               sw.append(line);
               sw.append(SEPARATOR);
            }
            sw.append(LINE);
            String msg = sw.toString();
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Check that the server can properly read the copied file.
     *
     * Like {@link #checkLocation(File, RawFileStorePrx)} but <em>after</em>
     * the invocation of {@link #exec(File, File)}, there is some chance, likely
     * due to file permissions, that the server will not be able to read the
     * transfered file. If so, raise an exception and leave the user to cleanup
     * and modifications.
     */
    protected void checkTarget(File location, TransferState state) throws ServerError {
        try {
            state.getUploader("r").size();
        } catch (Throwable t) {
            String message;
            if (t instanceof ServerError) {
                message = ((ServerError) t).message;
            } else {
                message = t.getMessage();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(t.getClass().getName());
            sb.append(" : ");
            sb.append(message);
            sb.append("\nThe server could not check the size of the file:\n");
            sb.append("-----------------------------------------------\n");
            sb.append(location);
            sb.append("\n-----------------------------------------------\n");
            sb.append("Most likely the server process has no read access\n");
            sb.append("and therefore in-place import cannot proceed. You\n");
            sb.append("should delete this file manually if you are sure\n");
            sb.append("that the original is safe.\n");
            throw new RuntimeException(sb.toString());
        }
    }

    /**
     * Creates a {@link ProcessBuilder} instance ready to have
     * {@link ProcessBuilder#start()} called on it. The only critical
     * piece of information should be the return code.
     *
     * @param file File to be copied.
     * @param location Location to copy to.
     * @return an instance ready for performing the transfer
     */
    protected abstract ProcessBuilder createProcessBuilder(File file, File location);

    protected void printLine() {
        log.error("*******************************************");
    }

}