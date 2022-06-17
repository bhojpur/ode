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
import java.util.List;
import java.util.Set;

import ode.io.nio.AbstractFileSystemService;
import ode.services.messages.DeleteLogMessage;
import ode.services.messages.DeleteLogsMessage;
import ode.system.OdeContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deletes all repository files immediately. Other files which are considered
 * "local" are handled later by a call to @{link {@link #deleteLocal()}.
 */
public class OriginalFileDeletions extends AbstractFileDeletions {

    private static final Logger log = LoggerFactory.getLogger(OriginalFileDeletions.class);

    public OriginalFileDeletions(AbstractFileSystemService afs, Set<Long> deletedIds, OdeContext ctx) {
        super(afs, deletedIds);

        // First we give the repositories a chance to delete
        // FS-based files.
        List<Long> orderedIds = new ArrayList<Long>(deletedIds);
        DeleteLogsMessage dlms = new DeleteLogsMessage(this, orderedIds);
        try {
            ctx.publishMessage(dlms);
            // We don't expect the message to throw. Instead we have to
            // evaluate its return value to see what failed.
            for (DeleteLogMessage dlm : dlms.getMessages()) {
                // If no logs were found via the publish
                // message, we have to assume that the files are local.
                // This may just log that the file doesn't exist.
                if (dlm.count() == 0) {
                    String filePath = afs.getFilesPath(dlm.getFileId());
                    addLocalFile(new File(filePath), dlm.getFileId());
                }
            }
        }
        catch (Throwable e) {
            // If an exception *is* thrown, we assume everything went awry.
            log.warn("Error on DeleteLogMessage", e);
            for (Long id : orderedIds) {
                // No way to calculate size!
                String filePath = afs.getFilesPath(id);
                File file = new File(filePath);
                fail(file, id, null);
            }
        }

    }
}
