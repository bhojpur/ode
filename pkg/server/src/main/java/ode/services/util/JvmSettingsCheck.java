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

import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.nio.charset.Charset;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Joiner;

/**
 * Hook run by the context which prints out JVM-related
 * information, primarily Java version, max heap size
 * and available processors.
 */
public class JvmSettingsCheck {

    public final static Log log = LogFactory.getLog(JvmSettingsCheck.class);

    /**
     * TotalPhysicalMemorySize value from the OperatingSystem JMX bean
     * at startup.
     */
    public final static long TOTAL_PHYSICAL_MEMORY;

    /**
     * FreePhysicalMemorySize value from the OperatingSystem JMX bean
     * at startup.
     */
    public final static long INITIAL_FREE_PHYSICAL_MEMORY;

    static {
        TOTAL_PHYSICAL_MEMORY = _get("TotalPhysicalMemorySize");
        INITIAL_FREE_PHYSICAL_MEMORY = _get("FreePhysicalMemorySize");
    }

    public JvmSettingsCheck() {
        final String fmt = "%s = %6s";
        final Runtime rt = Runtime.getRuntime();
        final int mb = 1024 * 1024;

        StringBuilder version = new StringBuilder();
        for (String key : new String[]{
                "java.version", "os.name", "os.arch", "os.version"}) {
            if (version.length() != 0) {
                version.append("; ");
            }
            version.append(System.getProperty(key));
        }

        Locale locale = Locale.getDefault();
        log.info("Language,Country,Charset,Timezone: " + Joiner.on(',').join(Arrays.asList(
                locale.getLanguage(), locale.getCountry(), Charset.defaultCharset(), TimeZone.getDefault().getID())));

        log.info("Java version: " + version);
        log.info(String.format(fmt, "Max Memory (MB):  ", (rt.maxMemory() / mb)));
        log.info(String.format(fmt, "OS Memory (MB):   ", (getPhysicalMemory() / mb)));
        log.info(String.format(fmt, "Processors:       ", rt.availableProcessors()));
    }

    public static long getPhysicalMemory() {
        return TOTAL_PHYSICAL_MEMORY;
    }

    private static long _get(String name) {
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            Object attribute = mBeanServer.getAttribute(
                new ObjectName("java.lang","type","OperatingSystem"), name);
            return Long.valueOf(attribute.toString());
        } catch (Exception e) {
            log.debug("Failed to get: " + name, e);
            return -1;
        }
    }

    public static void main(String[] args) {
        if (args.length >= 1 && "--psutil".equals(args[0])) {
            System.out.println("Free:" + INITIAL_FREE_PHYSICAL_MEMORY);
            System.out.println("Total:" + TOTAL_PHYSICAL_MEMORY);
            return; // EARLY EXIT
        }
        new JvmSettingsCheck();
    }

}
