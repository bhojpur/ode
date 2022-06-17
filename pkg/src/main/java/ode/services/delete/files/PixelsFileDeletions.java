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
import java.io.FileFilter;
import java.util.Set;

import ode.io.bioformats.BfPyramidPixelBuffer;
import ode.io.nio.AbstractFileSystemService;
import ode.io.nio.PixelsService;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registers the given file paths as well as other Pixel-related
 * files like Pyramids for later deletion via {@link #deleteLocal()}.
 */
public class PixelsFileDeletions extends AbstractFileDeletions {

    private static final Logger log = LoggerFactory.getLogger(PixelsFileDeletions.class);

    public PixelsFileDeletions(AbstractFileSystemService afs, Set<Long> deletedIds) {
        super(afs, deletedIds);
        for (Long id : deletedIds) {
            final String filePath = afs.getPixelsPath(id);
            final File file = new File(filePath);
            final File pyrFile = new File(filePath + PixelsService.PYRAMID_SUFFIX);
            final File dir = file.getParentFile();
            final File lockFile = new File(dir, "." + id + PixelsService.PYRAMID_SUFFIX
                + BfPyramidPixelBuffer.PYR_LOCK_EXT);

            // Remove the Pyramid file itself
            addLocalFile(file, id);
            // Try to remove a _pyramid file if it exists
            addLocalFile(pyrFile, id);
            // Now any lock file
            addLocalFile(lockFile, id);

            // Now any tmp files
            FileFilter tmpFileFilter = new WildcardFileFilter("."
                    + id + PixelsService.PYRAMID_SUFFIX + "*.tmp");
            File[] tmpFiles = dir.listFiles(tmpFileFilter);
            if(tmpFiles != null) {
                for (int i = 0; i < tmpFiles.length; i++) {
                    addLocalFile(tmpFiles[i], id);
                }
            }
        }
    }
}
