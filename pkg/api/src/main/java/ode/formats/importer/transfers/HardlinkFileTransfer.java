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
import java.util.ArrayList;
import java.util.List;

/**
 * Local-only file transfer mechanism which makes use of hard-linking.
 *
 * This is only useful where the commands "ln source target" (Unix) or
 * "mklink /H target source" (Windows) will work.
 */
public class HardlinkFileTransfer extends AbstractExecFileTransfer {

    /**
     * Executes "ln file location" (Unix) or "mklink /H location file" (Windows)
     * and fails on non-0 return codes.
     *
     * @param file File to be copied.
     * @param location Location to copy to.
     * @throws IOException
     *
     */
    // TODO Java7: replace ln once Java6 is dropped
    protected ProcessBuilder createProcessBuilder(File file, File location) {
        ProcessBuilder pb = new ProcessBuilder();
        List<String> args = new ArrayList<String>();
        if (isWindows()) {
            args.add("cmd");
            args.add("/c");
            args.add("mklink");
            args.add("/H");
            args.add(location.getAbsolutePath());
            args.add(file.getAbsolutePath());
        } else {
            args.add("ln");
            args.add(file.getAbsolutePath());
            args.add(location.getAbsolutePath());
        }
        pb.command(args);
        return pb;
    }

    /**
     * No cleanup action is taken.
     */
    public void afterTransfer(int errors, List<String> srcFiles) throws CleanupFailure {
        // no-op
    }
}