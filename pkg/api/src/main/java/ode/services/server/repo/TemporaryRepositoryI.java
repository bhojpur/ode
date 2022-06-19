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
 * Simple repository service to make the ${java.io.tmpdir} available at runtime.
 * This is primarily for testing (see server-config.xml to disable) of the
 * repository infrastructure, and will lead to a number of repository objects
 * being created in the database.
 */
public class TemporaryRepositoryI extends AbstractRepositoryI {

    private final static Logger log = LoggerFactory
            .getLogger(TemporaryRepositoryI.class);

    public TemporaryRepositoryI(Ice.ObjectAdapter oa, Registry reg,
            Executor ex, Principal p, PublicRepositoryI servant) {
        super(oa, reg, ex, p, System.getProperty("java.io.tmpdir"), new ReadOnlyStatus(false, false), servant);
    }

    //TODO CACHING
    public String getFilePath(final OriginalFile file, Current __current)
            throws ServerError {

        String repo = getFileRepo(file);

        if (repo == null || !repo.equals(getRepoUuid())) {
            String msg = String.format("%s (in %s) "
                    + "does not belong to this repository: %s", file.getId()
                    .getValue(), repo, getRepoUuid());

            throw new ode.ValidationException(null, null, msg);
        }

        return file.getPath().getValue();

    }
}