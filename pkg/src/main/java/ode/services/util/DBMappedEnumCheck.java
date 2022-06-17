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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;

import ode.model.IEnum;
import ode.model.IGlobal;
import ode.system.PreferenceContext;

/**
 * When a new version of the Bhojpur ODE server starts up check that the database
 * includes an enumeration instance for every value mapped for code generation.
 */
public class DBMappedEnumCheck extends BaseDBCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBMappedEnumCheck.class);

    private static final Pattern ENUMERATION_PATTERN = Pattern.compile("(ode\\.model\\.enums\\.[A-Za-z\\.]+)\\.\\d+\\:(\\S*)");

    private EnsureEnum ensureEnum;

    protected DBMappedEnumCheck(Executor executor, PreferenceContext preferences, EnsureEnum ensureEnum, ReadOnlyStatus readOnly) {
        super(executor, preferences, readOnly);
        this.ensureEnum = ensureEnum;
    }

    @Override
    protected void doCheck() {
        final SetMultimap<String, String> enums = getEnums();
        if (enums != null) {
            ensureEnums(enums);
        }
    }

    /**
     * @return the enumeration classes and values from the generated {@code enums.properties} file,
     * or {@code null} if it the file not be read
     */
    private SetMultimap<String, String> getEnums() {
        JarFile jarFile = null;
        InputStream enums = null;
        final SetMultimap<String, String> enumValues = LinkedHashMultimap.create();
        try {
            try {
                final URL file = ResourceUtils.getURL("classpath:enums.properties");
                jarFile = new JarFile(ResourceUtils.extractJarFileURL(file).getPath());
                enums = jarFile.getInputStream(jarFile.getJarEntry("enums.properties"));
            } catch (IOException ioe) {
                LOGGER.warn("could not locate mapped enumerations", ioe);
                return null;
            }
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(enums))) {
                String line;
                while ((line = in.readLine()) != null) {
                    final Matcher matcher = ENUMERATION_PATTERN.matcher(line);
                    if (matcher.matches()) {
                        enumValues.put(matcher.group(1), matcher.group(2));
                    }
                }
            } catch (IOException ioe) {
                LOGGER.warn("could not read mapped enumerations", ioe);
                return null;
            }
        } finally {
            try {
                if (jarFile != null) {
                    jarFile.close();
                }
                if (enums != null) {
                    enums.close();
                }
            } catch (IOException ioe) {
                LOGGER.warn("failed to close input handle", ioe);
            }
        }
        return enumValues;
    }

    /**
     * @param className the name of an enumeration class
     * @return the corresponding class
     * @throws ClassNotFoundException if the class could not be found
     */
    @SuppressWarnings("unchecked")
    private static <C extends IEnum & IGlobal> Class<C> getEnumClassForName(String className) throws ClassNotFoundException {
        return (Class<C>) Class.forName(className);
    }

    /**
     * Ensure that the given enumeration values exist as instances in the database.
     * @param enumValues the enumeration classes and their values
     */
    private void ensureEnums(SetMultimap<String, String> enumValues) {
        for (final Map.Entry<String, Collection<String>> valuesForClass : enumValues.asMap().entrySet()) {
            try {
                ensureEnum.ensure(getEnumClassForName(valuesForClass.getKey()), valuesForClass.getValue());
            } catch (ClassNotFoundException cnfe) {
                LOGGER.warn("cannot find class", cnfe);
                continue;
            }
        }
    }

    @Override
    protected String getCheckDone() {
        return "done for Bhojpur ODE version " + getOdeVersion();
    }
}
