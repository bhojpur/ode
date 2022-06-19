package ode.formats.utests;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ode.formats.importer.IObservable;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ImportEvent;
import ode.formats.importer.util.ErrorContainer;
import ode.formats.importer.util.ErrorHandler;
import ode.formats.importer.util.HtmlMessengerException;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Various configuration workflows
 */
@Test
public class ErrorHandlerTest {

    class MyErrorHandler extends ErrorHandler {

        private final boolean _sendFiles;

        private final boolean _sendLog;

        private Integer posts = null;

        private Integer uploads = null;

        public MyErrorHandler(ImportConfig config, boolean sendFiles,
                boolean sendLog) {
            super(config);
            this._sendFiles = sendFiles;
            this._sendLog = sendLog;
        }

        @Override
        public void executePost(String sendUrl, Map<String, String> postList)
                throws HtmlMessengerException {

            List<String> selectedFiles = new ArrayList<String>();
            for (int i = 0; i < postList.size(); i++) {
                String part = postList.get(i);
                if (part.equals("selected_file")) {
                    selectedFiles.add(""+part);
                }
            }
            posts = selectedFiles.size();
        }

        @Override
        public void uploadFile(ErrorContainer errorContainer) {
            uploads = errorContainer.getFiles().length;
        }

        @Override
        protected void onUpdate(IObservable observable, ImportEvent event) {
            if (event instanceof ImportEvent.DEBUG_SEND) {
                ImportEvent.DEBUG_SEND send = (ImportEvent.DEBUG_SEND) event;
                Assert.assertEquals(send.sendFiles, _sendFiles);
                Assert.assertEquals(send.sendLogs, _sendLog);

                // Copied from cli ErrorHandler.onUpdate.
                sendFiles = send.sendFiles;
                sendLogs = send.sendLogs;

                sendErrors();
            } else if (event instanceof ErrorHandler.UNKNOWN_FORMAT){
                // ignore this one.
            } else {
                Assert.fail("Bad event: " + event);
            }
        }

    }

    protected ImportConfig cfg(boolean sendFiles, boolean sendLog) {
        final ImportConfig config = new ImportConfig();
        config.sendLogFile.set(sendLog);
        config.sendFiles.set(sendFiles);
        return config;
    }

    protected ImportEvent err() {
        return new ErrorHandler.UNKNOWN_FORMAT("/tmp/",
                new RuntimeException("test"), null);
    }

    @Test
    public void testLogsAndFiles() {
        ImportConfig cfg = cfg(true, true);
        MyErrorHandler handler = new MyErrorHandler(cfg, true, true);
        handler.update(null, err());
        handler.update(null, new ImportEvent.DEBUG_SEND(true, true));
        Assert.assertEquals(handler.uploads, new Integer(2));
        Assert.assertEquals(handler.posts, new Integer(1));
    }

    @Test
    public void testLogsNotFiles() {
        ImportConfig cfg = cfg(false, true);
        MyErrorHandler handler = new MyErrorHandler(cfg, false, true);
        handler.update(null, err());
        handler.update(null, new ImportEvent.DEBUG_SEND(false, true));
        Assert.assertEquals(handler.uploads, new Integer(1));
        Assert.assertEquals(handler.posts, new Integer(0));
    }

    @Test
    public void testFilesNotLogs() {
        ImportConfig cfg = cfg(true, false);
        MyErrorHandler handler = new MyErrorHandler(cfg, true, false);
        handler.update(null, err());
        handler.update(null, new ImportEvent.DEBUG_SEND(true, false));
        Assert.assertEquals(handler.uploads, new Integer(1));
        Assert.assertEquals(handler.posts, new Integer(1));
    }

    @Test
    public void testNeitherFilesNorLogs() {
        ImportConfig cfg = cfg(false, false);
        MyErrorHandler handler = new MyErrorHandler(cfg, false, false);
        handler.update(null, err());
        handler.update(null, new ImportEvent.DEBUG_SEND(false, false));
        Assert.assertNull(handler.uploads);
        Assert.assertEquals(handler.posts, new Integer(0));
    }

}