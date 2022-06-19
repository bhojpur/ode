package ode.services.mail;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

/**
 * Simple server bean which can be started on Spring context creation
 * and shutdown when the application is editing. The
 * {@link MessageHandlerFactory} implementation which is passed to the server
 * is critical in deciding what will happen with the email.
 */

public class SubethaSMTP {

    public final static int DEFAULT_PORT = 2525;

    private final static Logger log = LoggerFactory.getLogger(SubethaSMTP.class);

    private final SMTPServer smtpServer;

    public SubethaSMTP(MessageHandlerFactory factory) {
        this(factory, DEFAULT_PORT);
    }
    
    public SubethaSMTP(MessageHandlerFactory factory, int port) {
        this.smtpServer = new SMTPServer(factory);
        smtpServer.setPort(port);
    }

    public void start() {
        smtpServer.start();
        log.info("SMTP started");
    }
    
    public void stop() {
        smtpServer.stop();
        log.info("SMTP stopped");
    }
}