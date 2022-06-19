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

#ifndef ODE_SHAREDRESOURCES_ICE
#define ODE_SHAREDRESOURCES_ICE

#include <ode/Repositories.ice>
#include <ode/Scripts.ice>
#include <ode/Tables.ice>

module ode {

    module grid {

        /**
         * Resource manager provided by each Server session for acquiring
         * shared resources in the OdeGrid. Unlike the other services
         * provided by ServiceFactory instances, it is not guaranteed
         * that a service instance returned from this interface will be
         * returned if that resource happens to be busy. In that case,
         * a null will be returned.
         **/
        interface SharedResources {

            /**
             * Waits up to seconds to acquire a slot in a processor
             * which can handle the given job.
             **/
            ode::grid::InteractiveProcessor*
                acquireProcessor(ode::model::Job job, int seconds)
                throws ServerError;

            /**
             * Registers a {@link ode.grid.Processor} for Storm notifications
             * so that other sessions can query whether or not a given
             * processor would accept a given task.
             **/
            void
                addProcessor(ode::grid::Processor* proc)
                throws ServerError;

            /**
             * Unregisters a {@link ode.grid.Processor} from Storm
             * notifications. If the processor was not already registered via
             * {@code addProcessor} this is a no-op.
             **/
            void
                removeProcessor(ode::grid::Processor* proc)
                throws ServerError;

            /**
             * Returns a map between Repository descriptions (ode::model::OriginalFile
             * instances) and RepositoryPrx instances (possibly null).
             **/
            idempotent
            ode::grid::RepositoryMap
                repositories()
                throws ServerError;

            /**
             * Returns the single (possibly mirrored) script repository which makes
             * all official scripts available.
             **/
            idempotent
            ode::grid::Repository*
                getScriptRepository()
                throws ServerError;

            /**
             * Returns true if a {@code Tables} service is active in the grid.
             * If this value is false, then all calls to {@code #ewTable}
             * or {@code openTable} will either fail or return null (possibly
             * blocking while waiting for a service to startup)
             **/
            idempotent
             bool
                areTablesEnabled()
                throws ServerError;

            /**
             * Creates a new Format(""ODE.tables"") file at the given path
             * on the given repository. The returned Table proxy follows
             * the same semantics as the openTable method.
             */
            ode::grid::Table*
                newTable(long repoId, string path)
                throws ServerError;

            /**
             * Returns a Table instance or null. Table instances are not
             * exclusively owned by the client and may throw an OptimisticLockException
             * if background modifications take place.
             *
             * The file instance must be managed (i.e. have a non-null id) and
             * be of the format ""ODE.tables"". Use newTable() to create
             * a new instance.
             */
            idempotent
            ode::grid::Table*
                openTable(ode::model::OriginalFile file)
                throws ServerError;

        };

    };

};

#endif