
package ode.services.server.test.utests;

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

import ode.services.server.repo.path.FilePathRestrictionInstance;
import ode.services.server.repo.path.FilePathRestrictions;

/**
 * Utility functions for file path transformer testing.
 */
public class FilePathTransformerTestBase {
    protected final FilePathRestrictions conservativeRules =
            FilePathRestrictionInstance.getFilePathRestrictions(FilePathRestrictionInstance.values());

    /**
     * Get the absolute path of the root directory above the current directory.
     * Assumes that the current directory is named <q><code>.</code></q>.
     * @return the root directory
     */
    private static File getRootDir() {
        File parent = new File(".").getAbsoluteFile();
        File dir = null;
        while (parent != null) {
            dir = parent;
            parent = dir.getParentFile();
        }
        return dir;
    }
    
    /**
     * Converts the path components to a corresponding absolute File.
     * @param components path components
     * @return the corresponding File
     */
    protected File componentsToFile(String... components) {
        File file = getRootDir();
        for (final String component : components)
            file = new File(file, component);
        return file;
    }
}