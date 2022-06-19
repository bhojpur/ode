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

import java.util.List;

import ode.formats.importer.ImportContainer;
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
 * {@link FileExclusion Voter} which checks the filename (not full path) of a
 * given {@link java.io.File} along with the checksum to detect duplicates. If
 * either the checksum or the checksum algorithm are null, then no detection
 * will be attempted, i.e. the voter will return a null to abstain.
 */
public class FilenameExclusion extends AbstractFileExclusion {

    private final static Logger log = LoggerFactory.getLogger(FilenameExclusion.class);

    public Boolean suggestExclusion(ServiceFactoryPrx factory,
            ImportContainer container) throws ServerError {
        IQueryPrx query = factory.getQueryService();
        String fullpath = container.getFile().getAbsolutePath();
        String filename = container.getFile().getName();
        List<IObject> files = query.findAllByQuery(
                "select o from OriginalFile o "
                + "join fetch o.hasher "
                + "where o.name = :name",
                new ParametersI().add("name", rstring(filename)));
       for (IObject obj : files) {
           OriginalFile ofile = (OriginalFile) obj;
           log.debug("Found original file: {}", ofile.getId().getValue());
           ChecksumAlgorithm algo = ofile.getHasher();
           String chksm = ofile.getHash() == null ? null : ofile.getHash().getValue();
           if (algo == null) {
               log.debug("No hasher: no vote");
               return null;
           } else if (chksm == null) {
               log.debug("No hash: no vote");
               return null;
           } else {
               String checksum = checksum(fullpath, algo);
               if (checksum == null) {
                   log.debug("Null checksum: no vote");
               } else {
                   if (checksum.equals(chksm)) {
                       log.info("Checksum match for filename: {}", filename);
                       return true;
                   }
               }
           }
       }
       return false; // Everything fine as far as we're concerned.
    }

}