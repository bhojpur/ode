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

import static ode.formats.importer.ImportEvent.*;
import ode.formats.importer.IObservable;
import ode.formats.importer.IObserver;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportContainer;
import ode.formats.importer.ImportEvent;
import ode.formats.importer.ImportLibrary;
import ode.formats.importer.ImportCandidates.SCANNING;
import ode.formats.importer.util.ErrorContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link IObserver} based on the gui ErrorHandler code which collects all
 * exceptions during
 * {@link ImportLibrary#importCandidates(ode.formats.importer.ImportConfig, ode.formats.importer.ImportCandidates)}
 * and after the import is finished offers to submit them via the feedback
 * system.
 */
public class ErrorHandler extends ode.formats.importer.util.ErrorHandler {

    private final static Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    public ErrorHandler(ImportConfig config) {
        super(config);
    }

    public void onUpdate(IObservable importLibrary, ImportEvent event) {

        if (event instanceof IMPORT_DONE) {
            if (errors.size() == 0) {
                log.debug("Number of errors: " + errors.size());
            } else {
                log.info("Number of errors: " + errors.size());
            }
        }

        else if (event instanceof SCANNING) {
            log.debug(event.toLog());
        }

        else if (event instanceof ImportEvent.DEBUG_SEND) {
            boolean plate = false;
            for (ErrorContainer error : errors) {
                error.setEmail(config.email.get());
                error.setComment("Sent from CLI");
                if (!plate) {
                    ImportContainer ic = icMap.get(
                            error.getSelectedFile().getAbsolutePath());
                    if (ic != null) {
                        Boolean b = ic.getIsSPW();
                        plate = (b != null && b.booleanValue());
                    }
                }
            }
            if (errors.size() > 0) {
                // Note: it wasn't the intent to have these variables set
                // here. This requires that subclasses know to call
                // super.onUpdate(). To prevent that, one could make this method
                // final and have an onOnUpdate, etc.
                sendFiles = ((ImportEvent.DEBUG_SEND) event).sendFiles;
                sendLogs = ((ImportEvent.DEBUG_SEND) event).sendLogs;
                if (plate) {
                    log.info("To submit HCS data, please e-mail us.");
                    sendFiles = false;
                }
                log.info("Sending error report "+ "(" + errors.size() + ")...");
                sendErrors();
                if (sendFiles) {
                    if (sendLogs)
                        log.info("Sent files and log file.");
                    else log.info("Sent files.");
                } else {
                    if (sendLogs) {
                        log.info("Sent log file.");
                    }
                }
            }

        }

    }

}