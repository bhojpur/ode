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

import static ode.rtypes.rstring;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ode.formats.importer.ImportContainer;
import ode.services.server.repo.path.ClientFilePathTransformer;
import ode.services.server.repo.path.FilePathRestrictionInstance;
import ode.services.server.repo.path.FilePathRestrictions;
import ode.services.server.repo.path.FsFile;
import ode.services.server.repo.path.MakePathComponentSafe;
import ode.ServerError;
import ode.api.IQueryPrx;
import ode.api.ServiceFactoryPrx;
import ode.model.ChecksumAlgorithm;
import ode.model.IObject;
import ode.model.OriginalFile;
import ode.sys.ParametersI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link FileExclusion Voter} which checks the original filepath from the client
 * ({@link ode.model.FilesetEntry#getClientPath()}) for existence.
 *
 * This check is most useful for imports from large, well-structured data directories
 * where continuously re-trying a number of imports is needed. The checksum for the
 * target file are <em>not</em> checked, meaning that modifications to the file will
 * not trigger a re-import.
 */
public class ClientPathExclusion extends AbstractFileExclusion {

    private final static Logger log = LoggerFactory.getLogger(ClientPathExclusion.class);

    public Boolean suggestExclusion(ServiceFactoryPrx factory,
            ImportContainer container) throws ServerError {

        final IQueryPrx query = factory.getQueryService();
        final String clientPath = calculateClientPath(container);

        List<IObject> files = query.findAllByQuery(
                "select o from FilesetEntry fe join fe.originalFile o "
                + "where fe.clientPath = :clientPath",
                new ParametersI().add("clientPath", rstring(clientPath)).page(0, 100));

        if (files.size() > 0) {
            log.info("ClientPath match for filename: {}", clientPath);
            for (IObject obj : files) {
                OriginalFile ofile = (OriginalFile) obj;
                log.debug("Found original file: {}", ofile.getId().getValue());
            }
            return true;
       }
       return false; // Everything fine as far as we're concerned.
    }

    private String calculateClientPath(ImportContainer container) {

        final String fullpath = container.getFile().getAbsolutePath();
        
        // Copied from ImportLibrary.java
        // TODO: allow looser sanitization according to server configuration
        final FilePathRestrictions portableRequiredRules =
                FilePathRestrictionInstance.getFilePathRestrictions(FilePathRestrictionInstance.WINDOWS_REQUIRED,
                                                                    FilePathRestrictionInstance.UNIX_REQUIRED);
        final ClientFilePathTransformer sanitizer = new ClientFilePathTransformer(new MakePathComponentSafe(portableRequiredRules));

        try {
            final FsFile fsPath = sanitizer.getFsFileFromClientFile(new File(fullpath), Integer.MAX_VALUE);
            final String clientPath = fsPath.toString();
            return clientPath;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}