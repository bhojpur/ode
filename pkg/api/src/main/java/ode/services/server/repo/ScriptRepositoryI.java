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

import java.util.Set;

import ode.services.server.fire.Registry;
import ode.services.scripts.ScriptRepoHelper;
import ode.services.util.Executor;
import ode.services.util.ReadOnlyStatus;
import ode.system.Principal;
import ode.ServerError;
import ode.model.OriginalFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Current;
import Ice.ObjectAdapter;

/**
 * Repository which makes the included script files available to users.
 */
public class ScriptRepositoryI extends AbstractRepositoryI {

    private final static Logger log = LoggerFactory.getLogger(ScriptRepositoryI.class);

    private final ScriptRepoHelper helper;

    @Deprecated
    public ScriptRepositoryI(ObjectAdapter oa, Registry reg, Executor ex,
            Principal p, ScriptRepoHelper helper, PublicRepositoryI servant, Set<String> scriptRepoUuids) {
        this(oa, reg, ex, p, helper, new ReadOnlyStatus(false, false), servant, scriptRepoUuids);
        log.info("assuming read-write repository");
    }

    public ScriptRepositoryI(ObjectAdapter oa, Registry reg, Executor ex, Principal p,
           ScriptRepoHelper helper, ReadOnlyStatus readOnly, PublicRepositoryI servant, Set<String> scriptRepoUuids) {
        super(oa, reg, ex, p, helper.getScriptDir(), readOnly, servant);
        this.helper = helper;
        scriptRepoUuids.add(helper.getUuid());
    }

    @Override
    public String generateRepoUuid() {
        return this.helper.getUuid();
    }

    /**
     */
    public String getFilePath(final OriginalFile file, Current __current)
            throws ServerError {

        String repo = getFileRepo(file);
        String uuid = getRepoUuid();

        if (repo == null || !repo.equals(uuid)) {
            throw new ode.ValidationException(null, null,repo
                    + " does not belong to this repository: " + uuid);
        }

        return file.getPath() == null ? null : file.getPath().getValue();

    }

}