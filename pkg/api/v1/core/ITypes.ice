/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_CORE_ITYPES_ICE
#define ODE_CORE_ITYPES_ICE

#include <ode/ModelF.ice>
#include <ode/ServicesF.ice>
#include <ode/Collections.ice>

module ode {

    module core {

        /**
         * Access to reflective type information. Also provides simplified
         * access to special types like enumerations.
         */
        ["ami", "amd"] interface ITypes extends ServiceInterface
            {
                ode::model::IObject createEnumeration(ode::model::IObject newEnum) throws ServerError;

                /**
                 * Lookup an enumeration value. As with the get-methods of
                 * {@link ode.api.IQuery} queries returning no results
                 * will through an exception.
                 *
                 * @param type An enumeration class which should be searched.
                 * @param value The value for which an enumeration should be
                 *              found.
                 * @return A managed enumeration. Never null.
                 * @throws ApiUsageException
                 *         if {@link ode.model.IEnum} is not found.
                 */
                idempotent ode::model::IObject getEnumeration(string type, string value) throws ServerError;
                idempotent IObjectList allEnumerations(string type) throws ServerError;

                /**
                 * Updates enumeration value specified by object.
                 *
                 * @param oldEnum An enumeration object which should be
                 *                searched.
                 * @return A managed enumeration. Never null.
                 */
                ode::model::IObject updateEnumeration(ode::model::IObject oldEnum) throws ServerError;

                /**
                 * Updates enumeration value specified by object.
                 *
                 * @param oldEnums An enumeration collection of objects which
                 *                 should be searched.
                 */
                void updateEnumerations(IObjectList oldEnums) throws ServerError;

                /**
                 * Deletes enumeration value specified by object.
                 *
                 * @param oldEnum An enumeration object which should be
                 *                searched.
                 */
                void deleteEnumeration(ode::model::IObject oldEnum) throws ServerError;

                /**
                 * Gets all metadata classes which are IEnum type.
                 *
                 * @return set of classes that extend IEnum
                 * @throws RuntimeException if Class not found.
                 */
                idempotent StringSet getEnumerationTypes() throws ServerError;

                /**
                 * Returns a list of classes which implement
                 * {@link ode.model.IAnnotated}. These can
                 * be used in combination with {@link ode.api.Search}.
                 *
                 * @return a {@link java.util.Set} of
                 *         {@link ode.model.IAnnotated} implementations
                 */
                idempotent StringSet getAnnotationTypes() throws ServerError;

                /**
                 * Gets all metadata classes which are IEnum type with
                 * contained objects.
                 *
                 * @return map of classes that extend IEnum
                 * @throws RuntimeException if xml parsing failure.
                 */
                idempotent IObjectListMap getEnumerationsWithEntries() throws ServerError;

                /**
                 * Gets all original values.
                 *
                 * @return A list of managed enumerations.
                 * @throws RuntimeException if xml parsing failure.
                 */
                idempotent IObjectList getOriginalEnumerations() throws ServerError;
                void resetEnumerations(string enumClass) throws ServerError;
            };

    };
};

#endif