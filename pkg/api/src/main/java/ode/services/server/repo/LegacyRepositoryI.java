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

import ode.io.nio.OriginalFilesService;
import ode.services.server.fire.Registry;
import ode.services.util.Executor;
import ode.services.util.ReadOnlyStatus;
import ode.system.Principal;
import ode.ServerError;
import ode.model.OriginalFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Current;

/**
 * Repository which makes the ${ode.data.dir} directory available
 * via the standard Repository API. Moving forward, this repository should
 * be phased out.
 */
public class LegacyRepositoryI extends AbstractRepositoryI {

    private final static Logger log = LoggerFactory.getLogger(LegacyRepositoryI.class);

    private final OriginalFilesService fs;

    @Deprecated
    public LegacyRepositoryI(Ice.ObjectAdapter oa, Registry reg, Executor ex,
            Principal p, String repoDir, PublicRepositoryI servant) {
        this(oa, reg, ex, p, new FileMaker(repoDir), new ReadOnlyStatus(false, false), servant);
        log.info("assuming read-write repository");
    }

    public LegacyRepositoryI(Ice.ObjectAdapter oa, Registry reg, Executor ex,
            Principal p, String repoDir, ReadOnlyStatus readOnly, PublicRepositoryI servant) {
        this(oa, reg, ex, p, new FileMaker(repoDir), readOnly, servant);
    }

    @Deprecated
    public LegacyRepositoryI(Ice.ObjectAdapter oa, Registry reg, Executor ex,
            Principal p, FileMaker fileMaker, PublicRepositoryI servant) {
        this(oa, reg, ex, p, fileMaker, new ReadOnlyStatus(false, false), servant);
        log.info("assuming read-write repository");
    }

    public LegacyRepositoryI(Ice.ObjectAdapter oa, Registry reg, Executor ex,
            Principal p, FileMaker fileMaker, ReadOnlyStatus readOnly, PublicRepositoryI servant) {
        super(oa, reg, ex, p, fileMaker, readOnly, servant);
        this.fs = new OriginalFilesService(fileMaker.getDir(), readOnly.isReadOnlyRepo());
    }

    /**
     * TODO CACHING
     */
    public String getFilePath(final OriginalFile file, Current __current)
            throws ServerError {

        String repo = getFileRepo(file);

        if (repo != null) {
            throw new ode.ValidationException(null, null,
                    "Does not belong to this repository");
        }

        return fs.getFilesPath(file.getId().getValue());

    }

}