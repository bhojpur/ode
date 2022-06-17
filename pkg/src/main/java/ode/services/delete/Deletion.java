package ode.services.delete;

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

import ode.io.nio.AbstractFileSystemService;
import ode.services.delete.files.FileDeleter;
import ode.system.OdeContext;
import ode.tools.hibernate.ExtendedMetadata;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.SetMultimap;

/**
 * Maintain state about a delete itself. That makes a central class for
 * providing reusable delete logic. (Note: much of this code has been
 * refactored out of DeleteHandleI for reuse by DeleteI etc.)
 * @see ode.api.IDelete
 */
public class Deletion {

    /**
     * Inner class which can be used to generate a Deletion. The use of 
     * {@link OdeContext} makes creating this object from the command-line
     * somewhat complicated, but with a Deletion.Builder inside of the Spring
     * configuration it should be possible to use:
     * <pre>
     * Deletion d = ctx.getBean("Deletion", Deletion.class);
     * </pre>
     * anywhere that a new deletion is needed.
     */
    public static class Builder extends AbstractFactoryBean<Deletion>
        implements ApplicationContextAware {

        protected OdeContext ctx;

        protected AbstractFileSystemService afs;

        protected ExtendedMetadata em;

        public Builder(AbstractFileSystemService afs, ExtendedMetadata em) {
            this.afs = afs;
            this.em = em;
        }

        @Override
        public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
            this.ctx = (OdeContext) ctx;
        }

        @Override
        protected Deletion createInstance()
            throws Exception {
            return new Deletion(afs, ctx);
        }

        @Override
        public Class<? extends Deletion> getObjectType() {
            return Deletion.class;
        }

    }

    private static final Logger log = LoggerFactory.getLogger(Deletion.class);

    //
    // Ctor/injection state
    //

    private final OdeContext ctx;

    private final AbstractFileSystemService afs;

    public Deletion(AbstractFileSystemService afs, OdeContext ctx) {

        this.afs = afs;
        this.ctx = ctx;

    }

    /**
     * For each Report use the map of tables to deleted ids to remove the files
     * under Files, Pixels and Thumbnails if the ids no longer exist in the db.
     * Create a map of failed ids (not yet passed back to client).
      */
    public void deleteFiles(SetMultimap<String, Long> deleteTargets) {
        final StopWatch sw = new Slf4JStopWatch();
        try {
            final FileDeleter files = new FileDeleter(ctx, afs, deleteTargets);
            files.run();
            if (files.getFailedFilesCount() > 0) {
                log.warn(files.getWarning());
            }
        } finally {
            sw.stop("ode.delete.binary");
        }
    }
}
