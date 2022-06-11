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

#ifndef ODE_CORE_METADATASTORE_ICE
#define ODE_CORE_METADATASTORE_ICE

#include <Ice/BuiltinSequences.ice>
#include <ode/ModelF.ice>
#include <ode/ServicesF.ice>
#include <ode/Scripts.ice>
#include <ode/Repositories.ice>

module ode {

    module constants {

        const string METADATASTORE = "ode.core.MetadataStore";

    };

    /**
     * Types used during import.
     */
    module metadatastore {

        /**
         * Container-class used by the import mechanism. Passed to
         * {@link ode.api.MetadataStore}
         */
        class IObjectContainer {
            string LSID;
            ode::api::StringIntMap indexes;
            ode::model::IObject sourceObject;
        };

        sequence<IObjectContainer> IObjectContainerArray;

    };

    module core {

        /**
         * Server-side interface for import.
         */
        ["ami","amd"] interface MetadataStore extends StatefulServiceInterface
            {
                void createRoot() throws ServerError;
                void updateObjects(ode::metadatastore::IObjectContainerArray objects) throws ServerError;
                void updateReferences(ode::api::StringStringArrayMap references) throws ServerError;
                IObjectListMap saveToDB(ode::model::FilesetJobLink activity) throws ServerError;
                void populateMinMax(DoubleArrayArrayArray imageChannelGlobalMinMax) throws ServerError;
                idempotent void setPixelsFile(long pixelsId, string file, string repo) throws ServerError;
                ode::grid::InteractiveProcessorList postProcess() throws ServerError;
            };
    };

};
#endif