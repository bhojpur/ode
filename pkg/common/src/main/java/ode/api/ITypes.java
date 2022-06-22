package ode.api;

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

import java.util.List;
import java.util.Map;

import ode.annotations.NotNull;
import ode.annotations.Validate;
import ode.conditions.ApiUsageException;
import ode.model.IAnnotated;
import ode.model.IEnum;

/**
 * Access to reflective type information. Also provides simplified access to
 * special types like enumerations.
 */

public interface ITypes extends ServiceInterface {

    /**
     * Returns a list of classes which implement {@link IAnnotated}. These can
     * be used in combination with {@link ode.api.Search}.
     * 
     * @return a {@link List} of {@link IAnnotated} implementations
     */
    List<Class<IAnnotated>> getAnnotationTypes();

    <T extends IEnum> T createEnumeration(T newEnum);

    <T extends IEnum> List<T> allEnumerations(Class<T> k);

    /**
     * lookup an enumeration value. As with the get-methods of {@link IQuery}
     * queries returning no results will through an exception.
     * 
     * @param <T>
     *            The type of the enumeration. Must extend {@link IEnum}
     * @param k
     *            An enumeration class which should be searched.
     * @param string
     *            The value for which an enumeration should be found.
     * @return A managed enumeration. Never null.
     * @throws ApiUsageException
     *             if {@link IEnum} is not found.
     */
    <T extends IEnum> T getEnumeration(Class<T> k, String string);

    /**
     * updates enumeration value specified by object
     * 
     * @param <T>
     *            The type of the enumeration. Must extend {@link IEnum}
     * @param oEnum
     *            An enumeration object which should be searched.
     * @return A managed enumeration. Never null.
     */
    <T extends IEnum> T updateEnumeration(@NotNull
    T oEnum);

    /**
     * updates enumeration value specified by object
     * 
     * @param <T>
     *            The type of the enumeration. Must extend {@link IEnum}
     * @param listEnum
     *            An enumeration collection of objects which should be searched.
     */
    <T extends IEnum> void updateEnumerations(@NotNull
    @Validate(IEnum.class)
    List<T> listEnum);

    /**
     * deletes enumeration value specified by object
     * 
     * @param <T>
     *            The type of the enumeration. Must extend {@link IEnum}
     * @param oEnum
     *            An enumeration object which should be searched.
     */
    <T extends IEnum> void deleteEnumeration(@NotNull
    T oEnum);

    /**
     * Gets all original values.
     * 
     * @param <T>
     *            The type of the enumeration. Must extend {@link IEnum}
     * @return A list of managed enumerations.
     * @throws RuntimeException
     *             if xml parsing failure.
     */
    <T extends IEnum> List<T> getOriginalEnumerations();

    <T extends IEnum> void resetEnumerations(Class<T> klass);

    /**
     * Gets all metadata classes which are IEnum type.
     * 
     * @param <T>
     *            The type of the enumeration. Must extend {@link IEnum}
     * @return list of Class of T extends IEnum
     * @throws RuntimeException
     *             if Class not found.
     */
    <T extends IEnum> List<Class<T>> getEnumerationTypes();

    /**
     * Gets all metadata classes which are IEnum type with contained objects.
     * 
     * @param <T>
     *            The type of the enumeration. Must extend {@link IEnum}
     * @return list of Class of T extends IEnum
     * @throws RuntimeException
     *             if xml parsing failure.
     */
    <T extends IEnum> Map<Class<T>, List<T>> getEnumerationsWithEntries();

}