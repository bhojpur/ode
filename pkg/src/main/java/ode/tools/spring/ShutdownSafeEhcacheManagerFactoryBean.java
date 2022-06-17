package ode.tools.spring;

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
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;

/**
 * Workaround for the Spring/EHCache shutdown sequence. Catches the {@link IllegalStateException}
 * which gets thrown on {@link #destroy()} and simply logs a message.
 */
public class ShutdownSafeEhcacheManagerFactoryBean extends EhCacheManagerFactoryBean {

    private static final Logger log = LoggerFactory
            .getLogger(ShutdownSafeEhcacheManagerFactoryBean.class);

    @Override
    public void destroy() {
        try {
            super.destroy();
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("Shutdown in progress")) {
                // ignore. It's because we're closing the application context
                // during shutdown.
                if (log.isDebugEnabled()) {
                    log.debug("Ignoring \"Shutdown in progress\" error.");
                }
            } else {
                throw e;
            }
        }
    }
    
}