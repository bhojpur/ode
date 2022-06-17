package ode.services.messages;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ode.util.SqlAction.DeleteLog;
import ode.util.messages.InternalMessage;

public class DeleteLogMessage extends InternalMessage {

    private static final long serialVersionUID = 1946424150689223625L;

    private final long fileId;

    private final List<DeleteLog> successes = new ArrayList<DeleteLog>();

    private final Map<DeleteLog, Throwable> errors = new HashMap<DeleteLog, Throwable>();

    public DeleteLogMessage(Object source, long fileId) {
        super(source);
        this.fileId = fileId;
    }

    public long getFileId() {
        return fileId;
    }

    public int count() {
        return successes.size() + errors.size();
    }

    public void success(DeleteLog log) {
        successes.add(log);
    }

    public void error(DeleteLog log, Throwable t) {
        errors.put(log, t);
    }

    public boolean isError(DeleteLog log) {
        return errors.containsKey(log);
    }

}
