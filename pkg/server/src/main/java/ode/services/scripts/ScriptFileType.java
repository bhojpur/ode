package ode.services.scripts;

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

import ode.model.core.OriginalFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;

/**
 * Definition of the script types that will be accepted by the server.
 * Every instance of {@link ScriptFileType} defined in the Spring
 * context will be registered with the {@link ScriptRepoHelper}.
 */
public class ScriptFileType {

    /**
     * Pattern that will be used by the default
     * {@link #getFileFilter()} implementation. Glob style such
     * as "*.py" is acceptable.
     */
    private final String pattern;

    /**
     * Mimetype that's required in the OriginalFile table
     * for the script to match this file type.
     */
    private final String mimetype;

    /**
     * Non-null but possibly empty launcher string. If empty, the
     * default behavior is chosen by the launching process itself.
     * For example, processor.py would chose "sys.executable". Values
     * are configured in Spring (e.g. "${ode.launcher.python}") and
     * can be modified by users via `bin/ode config set`.
     */
    private final String launcher;

    /**
     * String representing the class of the launcher that is to be
     * used. This will be loaded by the backend and given the same
     * arguments as would be given to the default processor (e.g.
     * "ode.processor.ProcessI").
     */
    private final String process;

    public ScriptFileType(String pattern, String mimetype) {
        this(pattern, mimetype, "", "");
    }

    public ScriptFileType(String pattern, String mimetype,
            String launcher, String process) {
        this.pattern = pattern;
        this.mimetype = mimetype;
        this.launcher = launcher;
        this.process = process;
    }

    /**
     * Returns <code>true</code> if the script type cannot be executed,
     * <code>false</code> otherwise.
     * @return See above.
     */
    public boolean isInert()
    {
        return StringUtils.isBlank(launcher) && StringUtils.isBlank(process);
    }

    /**
     * Returns the mimetype that must be set on an original file instance
     * in order to be considered of this type. Used in conjunction with
     * {@link #getFileFilter()}.
     */
    public String getMimetype() {
        return this.mimetype;
    }

    /**
     * A file-pattern (most likely of the form "*.EXT") which will be used
     * to determine if a file is of this type. Used in conjunction with
     * {@link #getMimetype()}.
     */
    public IOFileFilter getFileFilter() {
        return new WildcardFileFilter(pattern);
    }

    /**
     *  Return the name of the launcher ("./run.exe") that is used for
     *  scripts of this type. This is a fairly easy way to modify what
     *  is called by the backend.
     */
    public String getLauncher() {
        return launcher;
    }

    /**
     * Return the import name of the process class which will be used
     * to invoke scripts of this type. This permits developers to inject
     * completely different process handling.
     */
    public String getProcess() {
        return process;
    }

    /**
     * Sets the mimetype on the given {@link OriginalFile}
     * if the name field matches the {@link #pattern wildcard pattern}
     * for this instance.
     */
    public boolean setMimetype(OriginalFile ofile) {
        if (FilenameUtils.wildcardMatch(ofile.getName(), pattern)) {
            ofile.setMimetype(mimetype);
            return true;
        }
        return false;
    }
}
