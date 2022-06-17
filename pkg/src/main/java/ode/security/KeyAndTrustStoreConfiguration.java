package ode.security;

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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Calls {@link System#setProperty(String, String)} for the "javax.net.ssl"
 * properties needed to configure a trust store and a keystore for the Java
 * process.
 */
public class KeyAndTrustStoreConfiguration implements InitializingBean {

    private final static Logger log = LoggerFactory.getLogger("ode.security");

    private static final String JAVAX_NET_SSL_KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword";
    private static final String JAVAX_NET_SSL_KEY_STORE = "javax.net.ssl.keyStore";
    private static final String JAVAX_NET_SSL_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";
    private static final String JAVAX_NET_SSL_TRUST_STORE = "javax.net.ssl.trustStore";
    private String keyStore = null;
    private String keyStorePassword = null;
    private String trustStore = null;
    private String trustStorePassword = null;

    public void afterPropertiesSet() throws Exception {

        String oldTrustStore = System
                .getProperty(JAVAX_NET_SSL_TRUST_STORE, "");
        String oldKeyStore = System.getProperty(JAVAX_NET_SSL_KEY_STORE, "");

        if (oldTrustStore != null) {
            if (oldTrustStore.equals(trustStore)) {
                log.debug("Found duplicate trust store: " + oldTrustStore);
            } else if (oldTrustStore.length() > 0) {
                log.warn("Overwriting existing trust store: " + oldTrustStore);
            }
        }
        if (StringUtils.isEmpty(trustStore)) {
            log.warn("trustStore property is empty, not setting");
        } else {
            System.setProperty(JAVAX_NET_SSL_TRUST_STORE, trustStore);
        }
        if (StringUtils.isEmpty(trustStorePassword)) {
            log.warn("trustStorePassword property is empty, not setting");
        } else {
            System.setProperty(JAVAX_NET_SSL_TRUST_STORE_PASSWORD,
                               trustStorePassword);
        }

        if (oldKeyStore != null) {
            if (oldKeyStore.equals(keyStore)) {
                log.debug("Found duplicate trust store: " + oldKeyStore);
            } else if (oldKeyStore.length() > 0) {
                log.warn("Overwriting existing key store: " + oldKeyStore);
            }
        }
        System.setProperty(JAVAX_NET_SSL_KEY_STORE, keyStore);
        System.setProperty(JAVAX_NET_SSL_KEY_STORE_PASSWORD, keyStorePassword);
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public void setTrustStorePassword(String password) {
        this.trustStorePassword = password;
    }

    public void setKeyStorePassword(String password) {
        this.keyStorePassword = password;
    }

}