package ode.util;

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

import java.io.Closeable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import ode.model.IObject;
import ode.model.internal.Permissions;

/**
 * various tools needed throughout Bhojpur ODE.
 * TODO Grinder issues should be moved to test component to reduce deps.
 */
public class Utils {

    private final static Logger log = Logger.getLogger(Utils.class.getName());

    protected final static String CGLIB_IDENTIFIER = "$$EnhancerByCGLIB$$";

    protected final static String JVST_IDENTIFIER = "_$$_jvst";

    /**
     * finds the "true" class identified by a given Class object. This is
     * necessary because of possibly proxied instances.
     * 
     * @param source
     *            Regular or CGLIB-based class.
     * @return the regular Java class.
     */
    public static <T extends IObject> Class<T> trueClass(Class<T> source) {
        String s = source.getName();
        if (s.contains(CGLIB_IDENTIFIER)) { // TODO any other test?
            try {
                return (Class<T>) Class.forName(s.substring(0, s
                        .indexOf(CGLIB_IDENTIFIER)));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException( /* TODO */
                "Classname contains " + CGLIB_IDENTIFIER
                        + " but base class cannout be found.");
            }
        } else if (s.contains(JVST_IDENTIFIER)) {
            try {
                return (Class<T>) Class.forName(s.substring(0, s
                        .indexOf(JVST_IDENTIFIER)));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException( /* TODO */
                "Classname contains " + JVST_IDENTIFIER
                        + " but base class cannout be found.");
            }
        }
        return source;
    }

    static String msg = "Failed to instantiate %s. This may be caused by an "
            + "abstract class not being properly \"join fetch\"'d. Please review "
            + "your query or contact your server administrator.";

    /**
     * instantiates an object using the trueClass.
     * 
     * @param source
     *            Regular or CGLIB-based class.
     * @return the regular Java instance.
     */
    public static <T extends IObject> T trueInstance(Class<T> source) {
        final Class<T> trueClass = trueClass(source);
        final T result;
        try {
            result = trueClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(String.format(msg, trueClass), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Not allowed to create class:"
                    + trueClass, e);
        }
        return result;
    }

    /**
     * primarily used in Grinder to discover what methods to call
     * 
     * @param clazz
     */
    public static <T> String[] getObjectVoidMethods(Class<T> clazz) {
        final Set<String> set = new HashSet<String>();

        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getReturnType().equals(Object.class)) {
                if (method.getParameterTypes().length == 0) {
                    set.add(method.getName());
                }
            }
        }

        return set.toArray(new String[set.size()]);
    }

    /**
     * Returns the internal representation of a {@link Permissions} object.
     * Should be used with caution!
     */
    public static Object internalForm(Permissions p) {
        P pp = new P(p);
        return Long.valueOf(pp.toLong());
    }

    /**
     * Returns a {@link Permissions} instance from its internal representation.
     * Should be used with caution!
     */
    public static Permissions toPermissions(Object o) {
        P pp = new P((Long) o);
        return new Permissions(pp);
    }

    private static class P extends Permissions {
        private static final long serialVersionUID = -18133057809465999L;

        protected P(Permissions p) {
            revokeAll(p);
            grantAll(p);
        }

        protected P(Long l) {
            this.setPerm1(l.longValue());
        }

        long toLong() {
            return super.getPerm1();
        }
    }

    /**
     * Returns a {@link String} which can be used to correlate log messages.
     */
    public static String getThreadIdentifier() {
        return new StringBuilder(32).append(Runtime.getRuntime().hashCode())
                .append("::").append(Thread.currentThread().getId()).toString();
    }

    // Helpers
    // =========================================================================

    public static void closeQuietly(Closeable is) {
        if (is == null) {
            log.fine("Closeable is null");
        } else {
            try {
                is.close();
            } catch (Exception e) {
                log.info("Exception on closing closeable " + is + ":" + e);
            }
        }
    }
}