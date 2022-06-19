package ode.formats.importer.exclusions;

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

import ode.formats.importer.transfers.AbstractFileTransfer.Transfers;
import ode.formats.importer.transfers.FileTransfer;
import ode.services.server.util.ChecksumAlgorithmMapper;
import ode.util.checksum.ChecksumProvider;
import ode.util.checksum.ChecksumProviderFactory;
import ode.util.checksum.ChecksumProviderFactoryImpl;
import ode.model.ChecksumAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base {@link FileExclusion} implementation primarily providing a factory
 * for {@link FileExclusion} implementations via {@link #createExclusion(String)}.
 */
public abstract class AbstractFileExclusion implements FileExclusion {

    /**
     * Enum of well-known {@link FileExclusion} names.
     */
    public enum Exclusion {
        filename(FilenameExclusion.class),
        clientpath(ClientPathExclusion.class);
        Class<?> kls;
        Exclusion(Class<?> kls) {
            this.kls = kls;
        }
    }

    /**
     * Factory method for instantiating {@link FileTransfer} objects from
     * a string. Supported values can be found in the {@link Transfers} enum.
     * Otherwise, a FQN for a class on the classpath should be passed in.
     * @param arg non-null
     */
    public static FileExclusion createExclusion(String arg) {
        Logger tmp = LoggerFactory.getLogger(AbstractFileExclusion.class);
        tmp.debug("Loading file exclusion class {}", arg);
        try {
            try {
                return (FileExclusion) Exclusion.valueOf(arg).kls.newInstance();
            } catch (Exception e) {
                // Assume not in the enum
            }
            Class<?> c = Class.forName(arg);
            return (FileExclusion) c.newInstance();
        } catch (Exception e) {
            tmp.error("Failed to load file exclusion class " + arg);
            throw new RuntimeException(e);
        }
    }

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected String checksum(String filename, ChecksumAlgorithm checksumAlgorithm) {
        final ChecksumProviderFactory checksumProviderFactory = new ChecksumProviderFactoryImpl();
        final ChecksumProvider cp = checksumProviderFactory.getProvider(
                ChecksumAlgorithmMapper.getChecksumType(checksumAlgorithm));
        cp.putFile(filename);
        return cp.checksumAsString();
    }

}