package ode.services.util;

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

import loci.formats.IFormatReader;
import loci.formats.ImageReader;

import ode.model.enums.Format;
import ode.system.PreferenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hook run by the context to guarantee that various enumerations are up to
 * date. This is especially important for changes to bioformats which add
 * readers. Each reader is equivalent to a format ( + "Companion/<reader>").
 * Without such an extension, users are not able to import the latest and
 * greatest without a database upgrade.
 */
public class DBEnumCheck extends BaseDBCheck {

    public final static Logger log = LoggerFactory.getLogger(DBEnumCheck.class);

    private final EnsureEnum ensureEnum;

    public DBEnumCheck(Executor executor, PreferenceContext preferences, EnsureEnum ensureEnum, ReadOnlyStatus readOnly) {
        super(executor, preferences, readOnly);
        this.ensureEnum = ensureEnum;
    }

    @Override
    protected void doCheck() {
        final List<String> formatNames = new ArrayList<String>();
        for (final IFormatReader formatReader : new ImageReader().getReaders()) {
            String name = formatReader.getClass().getSimpleName();
            if (name.endsWith("Reader")) {
                name = name.substring(0, name.length() - 6);
                formatNames.add(name);
                if (formatReader.hasCompanionFiles()) {
                    formatNames.add("Companion/" + name);
                }
            }
        }
        ensureEnum.ensure(Format.class, formatNames);
    }

    @Override
    protected String getCheckDone() {
        return "done for Bio-Formats revision " + loci.formats.FormatTools.VCS_REVISION;
    }
}
