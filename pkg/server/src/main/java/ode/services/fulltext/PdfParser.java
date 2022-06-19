package ode.services.fulltext;

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

import java.io.File;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;

import ode.services.messages.ParserOpenFileMessage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link FileParser} for "application/pdf" files using <a
 * href="http://pdfbox.org/">PDFBox</a>.
 */
public class PdfParser extends FileParser {

    private final static Logger log = LoggerFactory.getLogger(PdfParser.class);

    @Override
    public Iterable<Reader> doParse(File file) throws Exception {

        final PdfThread pdfThread = new PdfThread(file);
        this.context.publishEvent(new ParserOpenFileMessage(this,
                pdfThread) {
            @Override
            public void close() {
                try {
                    pdfThread.close();
                } catch (Exception e) {
                    log.warn("Error closing PdfThread " + pdfThread, e);
                }
            }

        });

        pdfThread.start();
        return wrap(pdfThread.getReader());
    }
}

class PdfThread extends Thread {

    private final static Logger log = LoggerFactory.getLogger(PdfThread.class);

    final File file;
    final PipedWriter writer;
    final PipedReader reader;
    PDDocument document = null;

    PdfThread(File file) throws IOException {
        this.file = file;
        reader = new PipedReader();
        writer = new PipedWriter(reader);
    }

    Reader getReader() {
        return reader;
    }

    @Override
    public void run() {

        try {
            document = PDDocument.load(file);
        } catch (IOException e) {
            log.warn("Could not load Pdf " + file, e);
            try {
                writer.close();
            } catch (IOException ioe) {
                // What can we do?
            }
        }

        try {
            if (document != null && !document.isEncrypted()) {
                try {
                    PDFTextStripper stripper = null;
                    stripper = new PDFTextStripper();
                    stripper.writeText(document, writer);
                } finally {
                    close();
                }
            }
        } catch (IOException e) {
            log.warn("Error reading pdf file", e);
        }
    }

    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception e) {
                log.warn("Error closing writer", e);
            }
        }
        if (document != null) {
            try {
                document.close();
            } catch (Exception e) {
                log.warn("Error closing PDF document", e);
            }
        }

    }

}
