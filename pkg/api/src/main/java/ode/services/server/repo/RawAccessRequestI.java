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

import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

import Ice.Current;

import ode.io.nio.FileBuffer;

import ode.services.server.fire.Registry;
import ode.services.server.util.ChecksumAlgorithmMapper;

import ode.cmd.ERR;
import ode.cmd.HandleI.Cancel;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.Response;
import ode.cmd.Unknown;
import ode.constants.LogLevel;
import ode.grid.InternalRepositoryPrx;
import ode.grid.RawAccessRequest;
import ode.grid.RepositoryException;
import ode.model.ChecksumAlgorithm;

/**
 * Command request for accessing a repository directly.
 */
public class RawAccessRequestI extends RawAccessRequest implements IRequest {

    private static final long serialVersionUID = -303948503984L;

    private static Logger log = LoggerFactory.getLogger(RawAccessRequestI.class);
    private static Logger logCmd = LoggerFactory.getLogger(log.getName() + " log cmd");

    private static ImmutableMap<String, LogLevel> logLevels;

    private final Registry reg;

    protected Helper helper;

    protected InternalRepositoryPrx repo;

    static {
        final ImmutableMap.Builder<String, LogLevel> builder = ImmutableMap.builder();
        for (final LogLevel value : LogLevel.values()) {
            builder.put(value.name().toLowerCase(), value);
        }
        logLevels = builder.build();
    }

    public RawAccessRequestI(Registry reg) {
        this.reg = reg;
    }

    //
    // IRequest methods
    //

    public Map<String, String> getCallContext() {
        return null;
    }

    public void init(Helper helper) {
        this.helper = helper;

        if (!helper.getEventContext().isCurrentUserAdmin()) {
            throw helper.cancel(new ERR(), new ode.SecurityViolation(),
                    "not-admin");
        }

        log.debug("Looking repo " + repoUuid);
        try {
            String proposedName = "InternalRepository-" + repoUuid;
            InternalRepositoryPrx[] proxies = reg.lookupRepositories();
            for (InternalRepositoryPrx prx : proxies) {
                Ice.Identity id = prx.ice_getIdentity();
                if (proposedName.equals(id.name)) {
                    repo = prx;
                    log.debug("Found repo " + repoUuid);
                    break;
                }
            }
        }
        catch (Exception e) {
            throw helper.cancel(new ERR(), e, "registry-lookup", "repoUuid", repoUuid);
        }

        if (repo == null) {
            throw helper.cancel(new Unknown(), null, "unknown-repo", "repoUuid",
                    repoUuid);
        }
        this.helper.setSteps(1);

    }

    public Object step(int step) throws Cancel {
        helper.assertStep(step);
        return rawAccess();
    }

    @Override
    public void finish() throws Cancel {
        // no-op
    }

    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);
        helper.setResponseIfNull(((Response) object));
    }

    public Response getResponse() {
        return helper.getResponse();
    }

    public Response rawAccess() {
        try {
            log.debug("Calling raw access for command " + command);
            return repo.rawAccess(this);
        }
        catch (Throwable t) {
            throw helper.cancel(new ERR(), t, "raw-access",
                    "command", command);
        } finally {
            log.debug("Done calling raw access for command " + command);
        }
    }

    /**
     * Method called locally to the repository during {@link #rawAccess()}. Only
     * the remoteable fields should be accessed during this method call since this
     * will be a copy of the object originally called.
     *
     * @param abstractRepositoryI
     * @param servant
     * @param __current
     */
    public void local(AbstractRepositoryI abstractRepositoryI,
            PublicRepositoryI servant, Current __current) throws Exception {

        if ("touch".equals(command)) {
            for (String arg : args) {
                final CheckedPath checked = servant.checkPath(parse(arg), null, __current);
                if (!checked.exists()) {
                    final CheckedPath parent = checked.parent();
                    if (!(parent.isDirectory() || checked.parent().mkdirs())) {
                        throw new RepositoryException(null, null, "cannot create directory: " + parent);
                    }
                    final FileBuffer buffer = checked.getFileBuffer("rw");
                    buffer.write(ByteBuffer.allocate(0));
                    buffer.close();
                } else if (!checked.markModified()) {
                    throw new RepositoryException(null, null, "cannot touch file: " + checked);
                }
            }
        } else if ("exists".equals(command)) {
            final String arg = args.get(0);
            final CheckedPath checked = servant.checkPath(parse(arg), null, __current);
            if (!checked.exists()) {
                    throw new RepositoryException(null, null, "file does not exist: " + checked);
            }
        } else if ("mkdir".equals(command)) {
            boolean parents = false;
            for (String arg: args) {
                if ("-p".equals(arg)) {
                    parents = true;
                    continue;
                }
                final CheckedPath checked = servant.checkPath(parse(arg), null, __current);
                if (parents) {
                    checked.mkdirs();
                } else {
                    checked.mkdir();
                }
            }
        } else if ("rm".equals(command)) {
            if (args.size() == 1) {
                final CheckedPath checked = servant.checkPath(parse(args.get(0)), null, __current);
                if (!checked.delete()) {
                    throw new ode.grid.FileDeleteException(null, null,
                            "Delete file failed: " + args.get(0));
                }
            } else {
                throw new ode.ApiUsageException(null, null,
                        "Command: " + command + " takes just one argument");
            }
        } else if ("mv".equals(command)) {
            if (args.size() == 2) {
                final CheckedPath source = servant.checkPath(parse(args.get(0)), null, __current);
                final CheckedPath target = servant.checkPath(parse(args.get(1)), null, __current);
                boolean success = false;
                if (target.exists() && target.isDirectory()) {
                    try {
                        source.moveToDir(target, false);
                        success = true;
                    } catch (java.io.IOException ex) {
                        success = false;
                        log.warn("IOException on moveToDir: {}->{}",
                                source, target, ex);
                    }
                } else {
                    success = source.renameTo(target);
                }
                if (!success) {
                    throw new ode.ResourceError(null, null,
                        String.format("'mv %s %s' failed", source, target));
                }
            } else {
                throw new ode.ApiUsageException(null, null,
                        "Command: " + command + " takes two arguments");
            }
        } else if ("checksum".equals(command)) {
            if (args.size() == 3) {
                final String checksumType = args.get(0);
                final ChecksumAlgorithm algo = ChecksumAlgorithmMapper.getChecksumAlgorithm(checksumType);
                final String expectedHash = args.get(1);
                final CheckedPath checked = servant.checkPath(parse(args.get(2)), algo, __current);
                final String currentHash = checked.hash();
                if (!currentHash.equals(expectedHash)) {
                    // TODO: ADD ANNOTATION TO DATABASE HERE!
                    throw new ode.ResourceError(null, null, String.format(
                            "Checksum mismatch (%s): expected=%s found=%s",
                            checksumType, expectedHash, currentHash));
                }
            } else {
                throw new ode.ApiUsageException(null, null,
                        "'checksum' requires HASHER HASH FILEPATH, not: " + args.toString());
            }
        } else if ("read-only".equals(command)) {
            if (args.size() == 1) {
                final CheckedPath checked = servant.checkPath(parse(args.get(0)), null, __current);
                if (!checked.setReadOnly()) {
                    throw new ode.ResourceError(null, null,
                            "setReadOnly failed: " + args.get(0));
                }
            } else {
                throw new ode.ApiUsageException(null, null,
                        "Command: " + command + " takes just one argument");
            }
        } else if ("log".equals(command)) {
            final LogLevel logLevel = path == null ? null : logLevels.get(path.toLowerCase());
            if (logLevel == null) {
                throw new ode.ApiUsageException(null, null,
                        "Command: " + command + " requires path value of: " + Joiner.on(", ").join(logLevels.keySet()));
            }
            for (final String arg : args) {
                switch (logLevel) {
                case Trace:
                    logCmd.trace(arg);
                    break;
                case Debug:
                    logCmd.debug(arg);
                    break;
                case Info:
                    logCmd.info(arg);
                    break;
                case Warn:
                    logCmd.warn(arg);
                    break;
                case Error:
                    logCmd.error(arg);
                    break;
                }
            }
        } else {
            throw new ode.ApiUsageException(null, null,
                    "Unknown command: " + command);
        }
    }

    /**
     * Prepend a "./" if necessary to make the path relative.
     */
    //TODO: this could likely be removed once the prefix is always stripped.
    private String parse(String arg) {
        if (arg.startsWith("/")) {
            arg = "./" + arg;
        }
        return arg;
    }
}