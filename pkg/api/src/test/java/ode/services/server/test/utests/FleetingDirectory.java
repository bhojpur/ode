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
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Creates a nested directory as with {@link File#mkdirs} while noting
 * state that allows the created directories to later be deleted.
 */
public class FleetingDirectory {
    /* the directory deletion that should follow */
    private final Deque<File> created = new ArrayDeque<File>();

    /**
     * Ensure that the given directory exists, 
     * by creating it and its parents if necessary.
     * @param directory the directory that is to exist
     */
    public FleetingDirectory(File directory) {
        directory = directory.getAbsoluteFile();
        final Deque<File> toCreate = new ArrayDeque<File>();

        /* find which directories need to be created */
        while (!directory.exists()) {
            toCreate.push(directory);
            directory = directory.getParentFile();
        }

        /* create the directories, noting that they must later be deleted */
        while (!toCreate.isEmpty()) {
            final File nextToCreate = toCreate.pop();
            nextToCreate.mkdir();
            created.push(nextToCreate);
        }
    }

    /**
     * Delete the directories that were created in constructing this instance.
     */
    public void deleteCreated() {
        while (!created.isEmpty())
            created.pop().delete();
    }
}