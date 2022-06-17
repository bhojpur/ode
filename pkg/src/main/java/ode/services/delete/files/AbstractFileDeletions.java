package ode.services.delete.files;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ode.io.nio.AbstractFileSystemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for managing the removal of files from disk.
 *
 * @since 5.1.0-m3
 */
public abstract class AbstractFileDeletions {

    private static final Logger log = LoggerFactory.getLogger(AbstractFileDeletions.class);
    
    protected final Set<Long> deletedIds;
    
    protected final AbstractFileSystemService afs;

    private Map<File, Long> localFiles = new HashMap<File, Long>();

    private Map<File, Long> failedFiles = new HashMap<File, Long>();

    private long bytesFailed = 0;

    public AbstractFileDeletions(AbstractFileSystemService afs, Set<Long> deletedIds) {
        this.afs = afs;
    	this.deletedIds = deletedIds;
    }

    public void fail(File file, Long id, Long size) {
       failedFiles.put(file, id);
       if (size != null) {
           bytesFailed += size.longValue();
       }
    }

    /**
     * Called during the creation of instances if a particular file should
     * be handled by {@link #deleteLocal()}.
     * @param fileId
     * @param file
     */
    public void addLocalFile(File file, long fileId) {
        localFiles.put(file, fileId);
    }

    /**
     * Helper to delete and log. These files have not been handled elsewhere,
     * for example because they don't live in a repository.
     */
    public int deleteLocal() {
        for (Map.Entry<File, Long> entry: localFiles.entrySet()) {
            File file = entry.getKey();
            Long id = entry.getValue();
            if (file.exists()) {
                if (file.delete()) {
                    log.debug("DELETED: " + file.getAbsolutePath());
                } else {
                    log.debug("Failed to delete " + file.getAbsolutePath());
                    fail(file, id, file.length());
                }
            } else {
                log.debug("File " + file.getAbsolutePath() + " does not exist.");
            }
        }
        return failedFiles.size();
    }

    public long getBytesFailed() {
        return bytesFailed;
    }

    public long[] getUndeletedFiles() {
        List<Long> copy = new ArrayList<Long>(failedFiles.values());
        long[] ids = new long[copy.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = copy.get(i);
        }
        return ids;
    }
}
