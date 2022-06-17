package ode.logic;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.model.display.ChannelBinding;
import ode.services.scripts.RepoFile;
import ode.services.scripts.ScriptFileType;
import ode.services.scripts.ScriptRepoHelper;
import odeis.providers.re.lut.LutProvider;
import odeis.providers.re.lut.LutReader;
import odeis.providers.re.lut.LutReaderFactory;

/**
 * Lookup table provider implementation.
 */
public class LutProviderImpl implements LutProvider {

    /** The logger for this class. */
    private static Logger log =
            LoggerFactory.getLogger(LutProviderImpl.class);

    /**
     * Available readers, keyed off name.  Should be an unmodifiable map.
     */
    protected final Map<String, LutReader> lutReaders =
            new HashMap<String, LutReader>();

    @SuppressWarnings("unchecked")
    public LutProviderImpl(
            ScriptRepoHelper scriptRepoHelper, ScriptFileType lutType) {
        File root = new File(scriptRepoHelper.getScriptDir());
        Iterator<File> scripts = FileUtils.iterateFiles(
                root, lutType.getFileFilter(), TrueFileFilter.TRUE);
        while (scripts.hasNext()) {
            RepoFile script = new RepoFile(root, scripts.next());
            String basename = script.basename();
            try {
                lutReaders.put(
                        basename, LutReaderFactory.read(script.file()));
                log.debug("Successfully added LUT '{}'", basename);
            } catch (Exception e) {
                log.warn("Cannot read lookup table: '{}'",
                        script.fullname(), e);
            }
        }
        log.info("Successfully added {} LUTs", lutReaders.size());
    }

    /* (non-Javadoc)
     * @see odeis.providers.re.lut.LutProvider#getLutReaders(ode.model.display.ChannelBinding[])
     */
    public List<LutReader> getLutReaders(ChannelBinding[] channelBindings) {
        log.debug("Looking up LUT readers for {} channels from {} LUTs",
                channelBindings.length, lutReaders.size());
        List<LutReader> toReturn = new ArrayList<LutReader>();
        for (ChannelBinding cb : channelBindings) {
            if (cb.getActive()) {
                toReturn.add(lutReaders.get(cb.getLookupTable()));
            }
        }
        return toReturn;
    }

}