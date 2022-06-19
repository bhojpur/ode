package ode.formats.importer.transfers;

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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import ode.util.checksum.ChecksumProvider;
import ode.ServerError;
import ode.api.RawFileStorePrx;

import org.apache.commons.lang.ArrayUtils;

/**
 * Traditional file transfer mechanism which uploads
 * files using the API. This is done by reading from
 * {@link TransferState#getFile()} into {@link TransferState#getBuffer()}
 * and then {@link RawFileStorePrx#write(byte[], long, int) writing} to the
 * server. <em>Not thread safe</em>
 */
public class UploadFileTransfer extends AbstractFileTransfer {

    public String transfer(TransferState state) throws IOException, ServerError {

        final RawFileStorePrx rawFileStore = start(state);
        final File file = state.getFile();
        final byte[] buf = state.getBuffer();
        final ChecksumProvider cp = state.getChecksumProvider();
        
        FileInputStream stream = null;

        try {
            stream = new FileInputStream(file);
            int rlen = 0;
            long offset = 0;

            state.uploadStarted();
      
            // "touch" the file otherwise zero-length files
            rawFileStore.write(ArrayUtils.EMPTY_BYTE_ARRAY, offset, 0);
            state.stop();
            state.uploadBytes(offset);
    
            while (true) {
                state.start();
                rlen = stream.read(buf);
                if (rlen == -1) {
                    break;
                }
                cp.putBytes(buf, 0, rlen);
                final byte[] bufferToWrite;
                if (rlen < buf.length) {
                    bufferToWrite = new byte[rlen];
                    System.arraycopy(buf, 0, bufferToWrite, 0, rlen);
                } else {
                    bufferToWrite = buf;
                }
                rawFileStore.write(bufferToWrite, offset, rlen);
                offset += rlen;
                state.stop(rlen);
                state.uploadBytes(offset);
            }

            return finish(state, offset);
        } finally {
            cleanupUpload(rawFileStore, stream);
        }
    }

    /**
     * Since the {@link RawFileStorePrx} instances are cleaned up after each
     * transfer, there's no need to cleanup per {@link File}.
     */
    public void afterTransfer(int errors, List<String> srcFiles) throws CleanupFailure {
        // no-op
    }
}