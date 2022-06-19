package ode.services.server.util;

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

/**
 * Test under which platform Bhojpur ODE is currently running.
 * Exactly one of the public methods returns <code>true</code>. Each executes quickly.
 * Useful with {@literal @}Assumption annotations to limit unit tests to specific test platforms.
 */

public class CurrentPlatform {
    /**
     * A enumeration of operating systems under which Bhojpur ODE may run.
     */
    private enum OperatingSystem {
        WINDOWS("Microsoft Windows"),
        LINUX("Linux"),
        MAC("Apple Mac OS X");

        private final String name;

        OperatingSystem(String name) {
            this.name = name;
        }
    
        @Override
        public String toString() {
            return this.name;
        }
    }

    /* the current platform's operating system */
    private static final OperatingSystem os;

    static {
        final Logger logger = LoggerFactory.getLogger(CurrentPlatform.class);

        final String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows "))
            os = OperatingSystem.WINDOWS;
        else if (osName.equals("Linux"))
            os = OperatingSystem.LINUX;
        else if (osName.equals("Mac OS X"))
            os = OperatingSystem.MAC;
        else {
            os = null;
            logger.warn("failed to recognize current operating system");
        }
        if (os != null && logger.isDebugEnabled()) {
            logger.debug("recognized current operating system as being " + os);
        }
    }

    /**
     * @return if this platform is Microsoft Windows
     */
    public static boolean isWindows() {
        return os == OperatingSystem.WINDOWS;
    }

    /**
     * @return if this platform is Linux
     */
    public static boolean isLinux() {
        return os == OperatingSystem.LINUX;
    }

    /**
     * @return if this platform is Mac OS X
     */
    public static boolean isMacOSX() {
        return os == OperatingSystem.MAC;
    }

    /**
     * @return if this platform is unidentified
     */
    public static boolean isUnknown() {
        return os == null;
    }
}