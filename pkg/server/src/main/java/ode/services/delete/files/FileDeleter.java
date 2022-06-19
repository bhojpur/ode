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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ode.io.nio.AbstractFileSystemService;
import ode.system.OdeContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.SetMultimap;

/**
 * Helper class which sorts through a number of
 * file-based deletions before processing them.
 */
public class FileDeleter {

    enum Type {
        OriginalFile,
        Pixels,
        Thumbnail;
    }

    private static final Logger log = LoggerFactory.getLogger(FileDeleter.class);

    private OdeContext ctx;

    private final AbstractFileSystemService afs;

    private final SetMultimap<String, Long> deleteTargets;

    private OriginalFileDeletions originalFD;
 
    private ThumbnailFileDeletions thumbFD;

    private PixelsFileDeletions pixelsFD;

    private HashMap<String, long[]> undeletedFiles;

    private int filesFailed = 0;

    private long bytesFailed = 0;

	public FileDeleter(OdeContext ctx, AbstractFileSystemService afs, SetMultimap<String, Long> deleteTargets) {
        this.ctx = ctx;
        this.afs = afs;
        this.deleteTargets = deleteTargets;
    }

    public void run() {
        originalFD = new OriginalFileDeletions(afs, load(Type.OriginalFile), ctx);
        filesFailed += originalFD.deleteLocal();
        bytesFailed += originalFD.getBytesFailed();

        thumbFD = new ThumbnailFileDeletions(afs, load(Type.Thumbnail));
        filesFailed += thumbFD.deleteLocal();
        bytesFailed += thumbFD.getBytesFailed();

        pixelsFD = new PixelsFileDeletions(afs, load(Type.Pixels));
        filesFailed += pixelsFD.deleteLocal();
        bytesFailed += pixelsFD.getBytesFailed();

        undeletedFiles = new HashMap<String, long[]>();
        undeletedFiles.put(Type.OriginalFile.toString(), originalFD.getUndeletedFiles());
        undeletedFiles.put(Type.Thumbnail.toString(), thumbFD.getUndeletedFiles());
        undeletedFiles.put(Type.Pixels.toString(), pixelsFD.getUndeletedFiles());

        if (log.isDebugEnabled()) {
            for (final Map.Entry<String, long[]> undeletedFilesOneClass : undeletedFiles.entrySet()) {
                final String className = undeletedFilesOneClass.getKey();
                final long[] ids = undeletedFilesOneClass.getValue();
                if (ids.length > 0) {
                    log.debug("Failed to delete files : " + className + ":" + Arrays.toString(ids));
                }
            }
        }
    }

    /**
     * Lookup the ids which are scheduled for deletion.
     * @param fileType non-null
     * @return the IDs for that file type
     */
    protected Set<Long> load(Type fileType) {
        return deleteTargets.get(fileType.toString());
    }

    public HashMap<String, long[]> getUndeletedFiles() {
        return undeletedFiles;
    }

    public int getFailedFilesCount() {
        return filesFailed;
    }

    public String getWarning() {
        return String.format(
                "Warning: %s file(s) comprising %s bytes were not removed",
                filesFailed, bytesFailed);
    }
}
