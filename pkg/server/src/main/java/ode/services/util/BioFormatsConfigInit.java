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

import loci.formats.FormatTools;
import ode.api.IConfig;

/**
 * Propagate the server's Bio-Formats version metadata into corresponding keys
 * in the configuration service.
 */
public class BioFormatsConfigInit {

    private static final String KEY_PREFIX = "ode.bioformats.";

    /**
     * Set Bio-Formats verison metadata in the current configuration.
     * @param iConfig the configuration service
     */
    public BioFormatsConfigInit(IConfig iConfig) {
        iConfig.setConfigValue(KEY_PREFIX + "version", FormatTools.VERSION);
        iConfig.setConfigValue(
                KEY_PREFIX + "vcs_revision", FormatTools.VCS_REVISION);
        iConfig.setConfigValue(KEY_PREFIX + "date", FormatTools.DATE);
    }
}
