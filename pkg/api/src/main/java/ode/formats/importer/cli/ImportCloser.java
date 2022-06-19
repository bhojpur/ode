package ode.formats.importer.cli;

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

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loci.formats.in.DynamicMetadataOptions;
import loci.formats.in.MetadataLevel;
import loci.formats.meta.MetadataStore;
import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportCandidates;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportContainer;
import ode.formats.importer.ImportEvent;
import ode.formats.importer.ImportLibrary;
import ode.formats.importer.ODEWrapper;
import ode.formats.importer.exclusions.AbstractFileExclusion;
import ode.formats.importer.exclusions.FileExclusion;
import ode.formats.importer.transfers.AbstractFileTransfer;
import ode.formats.importer.transfers.CleanupFailure;
import ode.formats.importer.transfers.FileTransfer;
import ode.formats.importer.transfers.UploadFileTransfer;
import ode.api.ServiceFactoryPrx;
import ode.api.ServiceInterfacePrx;
import ode.cmd.HandlePrx;
import ode.cmd.Response;
import ode.grid.ImportProcessPrx;
import ode.grid.ImportProcessPrxHelper;
import ode.model.Annotation;
import ode.model.CommentAnnotationI;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stateful class for use by {@link CommandLineImporter} to interact
 * with server-side processes which are running.
 */
class ImportCloser {

    private final static Logger log = LoggerFactory.getLogger(ImportCloser.class);

    List<ImportProcessPrx> imports;
    int closed = 0;
    int errors = 0;
    int processed = 0;

    ImportCloser(ODEMetadataStoreClient client) throws Exception {
        this.imports = getImports(client);
    }

    void closeCompleted() {
        for (ImportProcessPrx imPrx : imports) {
            try {
                processed++;
                String logName = imPrx.toString().split("\\s")[0];
                HandlePrx handle = imPrx.getHandle();
                if (handle != null) {
                    Response rsp = handle.getResponse();
                    if (rsp != null) {
                        log.info("Done: {}", logName);
                        imPrx.close();
                        closed++;
                        continue;
                    }
                }
                log.info("Running: {}", logName);
            } catch (Exception e) {
                errors++;
                log.warn("Failure accessing service", e);
            }
        }
    }

    int getClosed() {
        return closed;
    }

    int getErrors() {
        return errors;
    }

    int getProcessed() {
        return processed;
    }

    private static List<ImportProcessPrx> getImports(ODEMetadataStoreClient client) throws Exception {
        final List<ImportProcessPrx> rv = new ArrayList<ImportProcessPrx>();
        final ServiceFactoryPrx sf = client.getServiceFactory();
        final List<String> active = sf.activeServices();
        for (String service : active) {
            try {
                final ServiceInterfacePrx prx = sf.getByName(service);
                final ImportProcessPrx imPrx = ImportProcessPrxHelper.checkedCast(prx);
                if (imPrx != null) {
                    try {
                        imPrx.ice_ping();
                        rv.add(imPrx);
                    } catch (Ice.ObjectNotExistException onee) {
                        // ignore
                    }
                }
            } catch (Exception e) {
                log.warn("Failure accessing active service", e);
            }
        }
        return rv;
    }
}