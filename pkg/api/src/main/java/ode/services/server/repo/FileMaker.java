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

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Date;

import ode.conditions.InternalException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for creating and cleaning up a repository lock file. After
 * {@link #init(String) initialization}, a call to {@link #getLine()} will
 * either return the UUID of the repository or null. If null, then
 * {@link #writeLine(String)} can be used to assert that this repository is to
 * be named according to the string.
 */
public class FileMaker {

    private final static Logger log = LoggerFactory.getLogger(FileMaker.class);

    private final Object[] mutex = new Object[0];

    private final String repoDir;

    private/* final */String dbUuid;

    private boolean isReadOnlyRepo;

    private File repoUuidFile, dotLockFile;

    private RandomAccessFile repoUuidRaf, dotLockRaf;

    private FileLock lock;

    public FileMaker(String repoDir) {
        this.repoDir = repoDir;
    }

    public String getDir() {
        return this.repoDir;
    }

    public boolean needsInit() {
        synchronized (mutex) {
            return this.dbUuid == null;
        }
    }

    /**
     * @param dbUuid
     * @throws Exception
     */
    public void init(String dbUuid) throws Exception {
        init(dbUuid, false);
    }

    public void init(String dbUuid, boolean isReadOnlyRepo) throws Exception {
        synchronized (mutex) {

            if (this.dbUuid != null) {
                throw new InternalException("Already initialized");
            }

            this.dbUuid = dbUuid;
            this.isReadOnlyRepo = isReadOnlyRepo;
            File mountDir = new File(repoDir);
            File odeDir = new File(mountDir, ".ode");
            File repoCfg = new File(odeDir, "repository");
            File uuidDir = new File(repoCfg, dbUuid);
            if (!uuidDir.exists()) {
                uuidDir.mkdirs();
                log.info("Creating " + uuidDir);
            }

            repoUuidFile = new File(uuidDir, "repo_uuid");
            dotLockFile = new File(uuidDir, ".lock");

            if (isReadOnlyRepo) {
                try {
                    repoUuidRaf = new RandomAccessFile(repoUuidFile, "r");
                } catch (FileNotFoundException fnfe) {
                    repoUuidRaf = null;
                }
                dotLockRaf = null;
            } else {
                repoUuidRaf = new RandomAccessFile(repoUuidFile, "rw");
                dotLockRaf = new RandomAccessFile(dotLockFile, "rw");
            }
        }
    }

    public String getLine() throws Exception {
        synchronized (mutex) {

            if (dbUuid == null) {
                throw new InternalException("Not initialized");
            }

            if (repoUuidRaf == null) {
                return null;
            }

            if (!isReadOnlyRepo) {
                lock = dotLockRaf.getChannel().lock();
                dotLockRaf.seek(0);
                dotLockRaf.writeUTF(new Date().toString());
            }

            String line = null;
            try {
                repoUuidRaf.seek(0);
                line = repoUuidRaf.readUTF();
            } catch (EOFException eof) {
                // pass
            }
            return line;
        }
    }

    public void writeLine(String line) throws Exception {

        synchronized (mutex) {

            if (dbUuid == null) {
                throw new InternalException("Not initialized");
            }

            repoUuidRaf.seek(0);
            repoUuidRaf.writeUTF(line);
        }
    }

    public void close() {

        synchronized (mutex) {

            if (dbUuid == null) {
                return;
            }

            try {
                if (lock != null) {
                    lock.release();
                }
            } catch (IOException e) {
                log.warn("Failed to release lock");
            }

            try {
                if (repoUuidRaf != null) {
                    repoUuidRaf.close();
                }
            } catch (IOException e) {
                log.warn("Failed to close repo_uuid");
            }

            try {
                if (dotLockRaf != null) {
                    dotLockRaf.close();
                }
            } catch (IOException e) {
                log.warn("Failed to close .lock");
            }

            dbUuid = null;
            repoUuidFile = null;
            if (!(isReadOnlyRepo || dotLockFile.delete())) {
                log.warn("Failed to delete lock file: "
                        + dotLockFile.getAbsolutePath());
            }
            dotLockFile = null;
            repoUuidRaf = null;
            dotLockRaf = null;

        }
    }

}