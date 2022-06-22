package ode.testing;

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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ode.model.IObject;

import org.testng.annotations.DataProvider;

/**
 * Utility class for building return values for {@link DataProvider} methods. Instances of this builder can still be used even after
 * {@link DataProviderBuilder#build()} has been called.
 */
public class DataProviderBuilder {

    private List<List<Object>> args = Collections.singletonList(Collections.emptyList());

    /**
     * Provide test methods with an additional argument that iterates through every value of an {@link java.lang.Enum}.
     * @param enumClass the enumeration class whose values are to be iterated through
     * @return this builder with the additional argument noted
     */
    public DataProviderBuilder add(Class<? extends Enum<?>> enumClass) {
        final Enum<?>[] enums = enumClass.getEnumConstants();
        final List<List<Object>> newArgs = new ArrayList<>(args.size() * enums.length);
        for (final List<Object> arg : args) {
            for (final Enum<?> e : enums) {
                final List<Object> newArg = new ArrayList<>(arg.size() + 1);
                newArg.addAll(arg);
                newArg.add(e);
                newArgs.add(newArg);
            }
        }
        args = newArgs;
        return this;
    }

    /**
     * Provide test methods with an additional argument that iterates through <em>an instance of</em> every given kind of model
     * object.
     * @param objectClasses the kinds of model object that are to be iterated through
     * @return this builder with the additional argument noted
     * @throws ReflectiveOperationException if the model object could not be instantiated
     */
    public DataProviderBuilder add(Collection<Class<? extends IObject>> objectClasses)
            throws ReflectiveOperationException {
        final List<IObject> objects = new ArrayList<>(objectClasses.size());
        for (final Class<? extends IObject> objectClass : objectClasses) {
            objects.add(objectClass.newInstance());
        }
        final List<List<Object>> newArgs = new ArrayList<>(args.size() * objects.size());
        for (final List<Object> arg : args) {
            for (final IObject o : objects) {
                final List<Object> newArg = new ArrayList<>(arg.size() + 1);
                newArg.addAll(arg);
                newArg.add(o);
                newArgs.add(newArg);
            }
        }
        args = newArgs;
        return this;
    }

    /**
     * Provide test methods with an additional argument that iterates through Boolean values.
     * @param isNullable if {@code null} should be included along with {@code true} and {@code false}
     * @return this builder with the additional argument noted
     */
    public DataProviderBuilder addBoolean(boolean isNullable) {
        final List<List<Object>> newArgs = new ArrayList<>(args.size() * (isNullable ? 3 : 2));
        for (final List<Object> arg : args) {
            List<Object> newArg;
            newArg = new ArrayList<>(arg.size() + 1);
            newArg.addAll(arg);
            newArg.add(Boolean.FALSE);
            newArgs.add(newArg);
            newArg = new ArrayList<>(arg.size() + 1);
            newArg.addAll(arg);
            newArg.add(Boolean.TRUE);
            newArgs.add(newArg);
            if (isNullable) {
                newArg = new ArrayList<>(arg.size() + 1);
                newArg.addAll(arg);
                newArg.add(null);
                newArgs.add(newArg);
            }
        }
        args = newArgs;
        return this;
    }

    /**
     * @return the return value of a {@link DataProvider} that provides the arguments noted by this builder
     */
    public Object[][] build() {
        final Object[][] argsArray = new Object[args.size()][];
        int index = 0;
        for (final List<Object> arg : args) {
            argsArray[index++] = arg.toArray();
        }
        return argsArray;
    }
}