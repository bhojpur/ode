package ode.annotations;

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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import ode.conditions.InternalException;

/**
 * Checks metadata constraints on API calls.
 */
public class AnnotationUtils {

    /**
     * finds all parameter {@link Annotation annotations} for the given class
     * including on all implemented interfaces.
     */
    @SuppressWarnings("unchecked")
    public static Object[] findMethodAnnotations(Class implClass, Method mthd)
            throws InternalException {

        Class[] interfaces = implClass.getInterfaces();
        Object[] annotations = new Object[interfaces.length + 1];

        for (int i = 0; i < interfaces.length; i++) {
            Method m = findMethod(interfaces[i], mthd);
            annotations[i] = m == null ? null : m.getDeclaredAnnotations();
        }
        annotations[interfaces.length] = mthd.getDeclaredAnnotations();

        return annotations;

    }

    /**
     * finds all parameter {@link Annotation annotations} for the given class
     * including on all implemented interfaces.
     */
    @SuppressWarnings("unchecked")
    public static Object[] findParameterAnnotations(Class implClass, Method mthd)
            throws InternalException {

        Class[] interfaces = implClass.getInterfaces();
        Object[] annotations = new Object[interfaces.length + 1];

        for (int i = 0; i < interfaces.length; i++) {
            Method m = findMethod(interfaces[i], mthd);
            annotations[i] = m == null ? null : m.getParameterAnnotations();
        }
        annotations[interfaces.length] = mthd.getParameterAnnotations();

        return annotations;

    }

    /**
     * finds methods on interfaces based on the {@link Class} and
     * {@link Method#getName() method name}.
     */
    @SuppressWarnings("unchecked")
    private static Method findMethod(Class implClass, Method mthd)
            throws InternalException {

        // Get the method.
        Method implMethod;
        try {
            implMethod = implClass.getMethod(mthd.getName(), mthd
                    .getParameterTypes());

        } catch (SecurityException e) {
            throw new InternalException(
                    "Not allowed to perform reflection for testing API.\n"
                            + String.format("Class:%s Method:%s", implClass
                                    .getName(), mthd));
        } catch (NoSuchMethodException e) {
            implMethod = null; // TODO No method == no violation.
        }
        return implMethod;
    }
}