package ode.formats.importer.targets;

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

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportContainer;
import ode.api.IUpdatePrx;
import ode.constants.namespaces.NSTARGETTEMPLATE;
import ode.model.CommentAnnotation;
import ode.model.CommentAnnotationI;
import ode.model.IObject;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateImportTarget implements ImportTarget {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private String discriminator;

    private String template;

    private String target;

    @Override
    public void init(String target) {
        // Builder is responsible for only passing valid files.
        this.target = target;
        String[] tokens = this.target.split(":",3);
        if (tokens.length == 2) {
            this.template = tokens[1];
            this.discriminator = "+name";
        } else {
            this.template = tokens[2];
            this.discriminator = tokens[1];
        }
    }

    public String getTemplate() {
        return this.template;
    }

    public String getDiscriminator() {
        return this.discriminator;
    }

    @Override
    public IObject load(ODEMetadataStoreClient client, ImportContainer ic) throws Exception {
        // Now we create an annotation for delaying parsing of the template
        // until we can receive server-side pre-processed paths.
        IUpdatePrx update = client.getServiceFactory().getUpdateService();
        CommentAnnotation ca = new CommentAnnotationI();
        ca.setNs(ode.rtypes.rstring(NSTARGETTEMPLATE.value));
        ca.setTextValue(ode.rtypes.rstring(this.target));

        // Here we save the unix-styled path to the directory that the target
        // file was stored in. Server-side, further directories should be
        // stripped from this based on the pattern and *that* will be
        // run against the regex "template".
        File dir = ic.getFile().getParentFile();
        String desc = FilenameUtils.separatorsToUnix(dir.toString());
        ca.setDescription(ode.rtypes.rstring(desc));
        ca = (CommentAnnotation) update.saveAndReturnObject(ca);
        log.debug("Created annotation {}", ca.getId().getValue());
        return ca;
    }

}