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

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.base.Joiner;
import com.google.common.collect.ListMultimap;

import static ode.formats.importer.ImportEvent.*;
import ode.formats.importer.IObservable;
import ode.formats.importer.IObserver;
import ode.formats.importer.ImportEvent;

import ode.model.IObject;
import ode.model.Pixels;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic import process monitor that writes information to the log.
 */
public class LoggingImportMonitor implements IObserver
{
    private static Logger log = LoggerFactory.getLogger(LoggingImportMonitor.class);

    private final ImportSummary importSummary = new ImportSummary();

    private ImportOutput importOutput = ImportOutput.ids;

    /**
     * Set the current {@link ImportOutput} (defaulting to null if
     * a null value is passed).
     *
     * @param importOutput possibly null enumeration value.
     * @return previous value.
     */
    public ImportOutput setImportOutput(ImportOutput importOutput) {
        ImportOutput old = importOutput;
        if (importOutput == null) {
            this.importOutput = ImportOutput.ids;
        }
        this.importOutput = importOutput;
        return old;
    }

    public synchronized void update(IObservable importLibrary, ImportEvent event)
    {
        if (event instanceof IMPORT_DONE) {
            IMPORT_DONE ev = (IMPORT_DONE) event;
            log.info(event.toLog());

            // send the import results to stdout
            // to enable external tools integration
            switch (importOutput) {
                case yaml:
                    importSummary.outputYamlResults(ev);
                    break;
                case legacy:
                    importSummary.outputGreppableResults(ev);
                    break;
                default:
                    importSummary.outputImageOrPlateIds(ev);
            }
            importSummary.update(ev);
        } else if (event instanceof IMPORT_SUMMARY) {
            IMPORT_SUMMARY ev = (IMPORT_SUMMARY) event;
            importSummary.setTime(ev.importTime);
            importSummary.setErrors(ev.errorCount);
            importSummary.report();
        } else if (event instanceof FILESET_UPLOAD_PREPARATION) {
            log.info(event.toLog());
        } else if (event instanceof FILESET_UPLOAD_START) {
            log.info(event.toLog());
        } else if (event instanceof FILESET_UPLOAD_END) {
            log.info(event.toLog());
        } else if (event instanceof FILE_UPLOAD_STARTED) {
            FILE_UPLOAD_STARTED ev = (FILE_UPLOAD_STARTED) event;
            log.info(event.toLog() + ": " + ev.filename);
        } else if (event instanceof FILE_UPLOAD_COMPLETE) {
            FILE_UPLOAD_COMPLETE ev = (FILE_UPLOAD_COMPLETE) event;
            log.info(event.toLog() + ": " + ev.filename);
            importSummary.update(ev);
        } else if (event instanceof PROGRESS_EVENT) {
            log.info(event.toLog());
        } else if (log.isDebugEnabled()) {
            log.debug(event.toLog());
        }
    }

    /**
     * Placeholder used for printing a final import summary.
     */
    private class ImportSummary
    {
        private final static String PLATE_CLASS = "PlateI";

        private int createdFilesets;
        private int createdPlates;
        private int errors;
        private int importedImages;
        private int uploadedFiles;

        /** Time taken by import in milliseconds. **/
        private long time;

        /**
         * Updates the state of the object using information held by given even
         * type.
         *
         * @param event An import event.
         */
        public void update(IMPORT_DONE event) {
            importedImages += event.pixels.size();
            createdFilesets++;
            for (IObject object : event.objects) {
                if (PLATE_CLASS.equals(object.getClass().getSimpleName())) {
                    createdPlates++;
                }
            }
        }

        /**
         * Updates the state of the object using information held by given event
         * type.
         *
         * @param event An import event.
         */
        public void update(FILE_UPLOAD_COMPLETE event) {
            uploadedFiles++;
        }

        /**
         * Sets the import error count to the given number.
         *
         * @param errors Count.
         */
        public void setErrors(int errors) {
            this.errors = errors;
        }

        /**
         * Sets the time taken by import to given value.
         *
         * @param time The time in milliseconds.
         */
        public void setTime(long time) {
            this.time = time;
        }

        /**
         * Prints out the import summary to stderr.
         */
        public void report() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n==> Summary\n");
            sb.append(entityCountToString("file", uploadedFiles));
            sb.append(" uploaded, ");
            sb.append(entityCountToString("fileset", createdFilesets));
            if (createdPlates != 0) {
                sb.append(", ");
                sb.append(entityCountToString("plate", createdPlates));
            }
            sb.append(" created, ");
            sb.append(entityCountToString("image", importedImages));
            sb.append(" imported, ");
            sb.append(entityCountToString("error", errors));
            sb.append(String.format(" in %s\n",
                    DurationFormatUtils.formatDurationHMS(time)));
            System.err.print(sb.toString());
        }

        /**
         * Returns a string with a digit and singular/plural form of the
         * provided entity name (e.g. "3 apples", "1 car").
         *
         * @param name The name of the entity used in the output.
         * @param count The number of entity elements.
         * @return See above.
         */
        private String entityCountToString(String name, int count) {
            return String.format("%d %s%s", count, name, count != 1 ? "s" : "");
        }

        /**
         * Returns a ListMultimap containing the class names and IDs of
         * the imported objects
         *
         * @param ev the end of import event.
         * @return See above.
         */
        private ListMultimap<String, Long> getObjectIdMap(IMPORT_DONE ev) {
            ListMultimap<String, Long> collect = ArrayListMultimap.create();
            for (IObject object : ev.objects) {
                if (object != null && object.getId() != null) {
                    String kls = object.getClass().getSimpleName();
                    if (kls.endsWith("I")) {
                        kls = kls.substring(0,kls.length()-1);
                    }
                    collect.put(kls, object.getId().getValue());
                }
            }
            return collect;
        }

        /**
         * Displays a list of other imported objects IDs on standard error.
         *
         * @param fid  the Fileset ID.
         * @param collect  a map of classes and IDs.
         */
        void otherImportedObjects(long fid, ListMultimap<String, Long> collect,
                String exclude) {

            System.err.println("Other imported objects:");
            System.err.println("Fileset:" + fid);
            for (String kls : collect.keySet()) {
                if (exclude == null || !kls.equals(exclude)) {
                    List<Long> ids = collect.get(kls);
                    for (Long id : ids) {
                        System.err.println(String.format("%s:%d", kls, id));
                    }
                }
            }
        }

        /**
         * Displays a list of successfully imported Pixels IDs on standard
         * output.
         *
         * Note that this behavior is intended for other command line tools to
         * pipe/grep the import results, and should be kept as is.
         *
         * @param ev the end of import event.
         */
        void outputGreppableResults(IMPORT_DONE ev) {
            System.err.println("Imported pixels:");
            for (Pixels p : ev.pixels) {
                System.out.println(p.getId().getValue());
            }

            ListMultimap<String, Long> collect = getObjectIdMap(ev);
            otherImportedObjects(ev.fileset.getId().getValue(), collect, null);
        }

        /**
         * Displays a yaml description of the successfully imported Images
         * and other objects to standard output.
         *
         * Note that this behavior is intended for other command line tools to
         * pipe/grep the import results, and should be kept as is.
         *
         * @param ev the end of import event.
         */
        void outputYamlResults(IMPORT_DONE ev) {
            System.err.println("Imported objects:");
            System.out.println("---");
            System.out.println("- Fileset: " + ev.fileset.getId().getValue());
            ListMultimap<String, Long> collect = getObjectIdMap(ev);
            for (String kls : collect.keySet()) {
                List<Long> ids = collect.get(kls);
                System.out.println(String.format("  %s: [%s]", kls,
                    Joiner.on(",").join(ids)));
            }
        }

        /**
         * Displays a list of successfully imported Image or Plate IDs
         * on standard output using the Object:id format.
         *
         * Note that this behavior is intended for other command line tools to
         * pipe/grep the import results, and should be kept as is.
         *
         * @param ev the end of import event.
         */
        void outputImageOrPlateIds(IMPORT_DONE ev) {
            String kls;
            ListMultimap<String, Long> collect = getObjectIdMap(ev);
            if (collect.containsKey("Plate")) {
                kls = "Plate";
            } else {
                kls = "Image";
            }
            List<Long> ids = collect.get(kls);
            System.out.println(String.format("%s:%s", kls, Joiner.on(",").join(ids)));

            otherImportedObjects(ev.fileset.getId().getValue(), collect, kls);
        }
    }
}