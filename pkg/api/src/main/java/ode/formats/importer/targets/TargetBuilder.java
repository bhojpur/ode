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

import ode.model.IObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TargetBuilder {

    private final static Logger log = LoggerFactory.getLogger(TargetBuilder.class);

    private String target;

    private Class<? extends ImportTarget> type;

    @SuppressWarnings("unchecked")
    public TargetBuilder parse(String string) {
        if (target != null) {
            throw new IllegalStateException(String.format(
                    "Only one target supported: old=%s new=%s",
                    this.target, string));
        }
        target = string;
        int firstColon = string.indexOf(":");
        String prefix;
        if (firstColon >= 0) {
            prefix = string.substring(0, firstColon);
            Class<?> klass = tryClass(prefix);
            if (klass != null) {
                if (ImportTarget.class.isAssignableFrom(klass)) {
                    type = (Class<ImportTarget>) klass;
                    return this;
                } else if (IObject.class.isAssignableFrom(klass)) {
                    type = (Class<? extends ImportTarget>) ModelImportTarget.class;
                    return this;
                }
            }
            klass = tryClass("ode.model." + prefix);
            if (klass != null) {
                if (IObject.class.isAssignableFrom(klass)) {
                    type = (Class<? extends ImportTarget>) ModelImportTarget.class;
                    return this;
                }
            }
        } else {
            throw new IllegalStateException(String.format(
                    "Target string not valid: %s", string));
        }

        // Handles everything else.
        if (!prefix.equals("regex") && !prefix.equals("")) {
            throw new IllegalStateException(String.format(
                    "Target string not valid: %s", string));
        }
        type = TemplateImportTarget.class;
        return this;
    }

   /**
    */
    public ImportTarget build() {
        ImportTarget inst = null;
        try {
            inst = type.newInstance();
        } catch (Exception e) {
            log.warn("Failed to instantiate for: {}", target, e);
            throw new RuntimeException("Could not create ImportTarget: "
                + target, e);
        }
        inst.init(target);
        return inst;
    }

    protected Class<?> tryClass(String string) {
        try {
            return Class.forName(string);
        } catch (ClassNotFoundException e) {
            log.debug(e.getMessage());
            return null;
        }
    }

}