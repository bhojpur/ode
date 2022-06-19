/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_API_EXPORTER_ICE
#define ODE_API_EXPORTER_ICE

#include <ode/ServerErrors.ice>
#include <ode/ServicesF.ice>

module ode {

    module api {

        /**
         * Stateful service for generating ODE-XML or ODE-TIFF from data stored
         * in Bhojpur ODE. Intended usage:
         * <pre>
         * {@code

         *   ExporterPrx e = sf.createExporter();
         *
         *   // Exporter is currently in the <i>configuration</i> state
         *   // Objects can be added by id which should be present
         *   // in the output.
         *
         *   e.addImage(1);
         *
         *   // As soon as a generate method is called, the objects
         *   // added to the Exporter are converted to the specified
         *   // format. The length of the file produced is returned.
         *   // No more objects can be added to the Exporter, nor can
         *   // another generate method be called.
         *
         *   long length = e.generateTiff();
         *
         *   // As soon as the server-side file is generated, read()
         *   // can be called to get file segments. To create another
         *   // file, create a second Exporter. Be sure to close all
         *   // Exporter instances.
         *
         *   long read = 0
         *   byte\[] buf;
         *   while (true) {
         *      buf = e.read(read, 1000000);
         *      // Store to file locally here
         *      if (buf.length < 1000000) {
         *          break;
         *       }
         *       read += buf.length;
         *   }
         *   e.close();
         * }
         * </pre>
         **/
        ["ami", "amd"] interface Exporter extends StatefulServiceInterface {

            // Config ================================================

            /**
             * Adds a single image with basic metadata to the Exporter for inclusion
             * on the next call to getBytes().
             **/
            void addImage(long id) throws ServerError;

            // Output ================================================

            /**
             * Generates an ODE-XML file. The return value is the length
             * of the file produced.
             **/
            long generateXml() throws ServerError;

            /**
             * Generates an ODE-TIFF file. The return value is the length
             * of the file produced. This method ends configuration.
             **/
            long generateTiff() throws ServerError;

            /**
             * Returns <code>length</code> bytes from the output file. The
             * file can be safely read until reset() is called.
             **/
            idempotent Ice::ByteSeq read(long position, int length) throws ServerError;

            // StatefulService: be sure to call close()!

        };
    };
};

#endif