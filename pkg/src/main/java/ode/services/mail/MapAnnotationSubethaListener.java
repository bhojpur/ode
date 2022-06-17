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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ode.model.annotations.MapAnnotation;
import ode.model.internal.NamedValue;
import ode.services.util.Executor;
import ode.system.Principal;
import ode.system.ServiceFactory;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.subethamail.smtp.TooMuchDataException;
import org.subethamail.smtp.helper.SimpleMessageListener;

/**
 * Basic {@link SimpleMessageListener} which stores all received emails as
 * {@link MapAnnotation} instances belong to root with the namespace
 * {@link #NAMESPACE}. The description of the annotation is the body of the
 * text and two fields are set with the keys {@link #FROM} and {@link #TO}.
 *
 * @since 5.1.0
 */

public class MapAnnotationSubethaListener implements SimpleMessageListener {

    public final static String NAMESPACE = "bhojpur.net/testing/smtp";

    public final static String FROM = "from";

    public final static String TO = "to";

    private final static Logger log = LoggerFactory.getLogger(MapAnnotationSubethaListener.class);

    private final Executor executor;

    private final Principal principal;

    public MapAnnotationSubethaListener(Executor executor, Principal principal) {
        this.executor = executor;
        this.principal = principal;
    }

    public MapAnnotationSubethaListener(Executor executor, String uuid) {
        this.executor = executor;
        this.principal = new Principal(uuid);
    }

    /**
     * Accepts all mails.
     */
    @Override
    public boolean accept(String arg0, String arg1) {
        return true;
    }

    /**
     * Stores each email as a map annotation with {@link #NAMESPACE} set.
     */
    @Override
    public void deliver(final String from, final String recipient, final InputStream stream)
            throws TooMuchDataException, IOException {
       executor.execute(principal, new Executor.SimpleWork(this, "deliver") {
        @Transactional(readOnly=false)
        @Override
        public Object doWork(Session session, ServiceFactory sf) {
            MapAnnotation ma = new MapAnnotation();
            ma.setNs(NAMESPACE);
            ma.setDescription(parseStream(stream));
            ma.setMapValue(new ArrayList<NamedValue>());
            ma.getMapValue().add(new NamedValue("from", from));
            ma.getMapValue().add(new NamedValue("to", recipient));
            ma = sf.getUpdateService().saveAndReturnObject(ma);
            log.info("Saved email: MapAnnotation:" + ma.getId());
            return ma;
        }});
    }

    protected String parseStream(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        
        String line = null;
        try {
                while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return sb.toString();
    }

}