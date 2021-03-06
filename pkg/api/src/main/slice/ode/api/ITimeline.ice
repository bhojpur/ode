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

#ifndef ODE_API_ITIMELINE_ICE
#define ODE_API_ITIMELINE_ICE

#include <ode/ModelF.ice>
#include <ode/ServicesF.ice>
#include <ode/System.ice>
#include <ode/Collections.ice>

module ode {

    module api {

        /**
         * Service for the querying of Bhojpur ODE metadata based on creation and
         * modification time. Currently supported types for querying include:
         * <ul>
         *   <li>{@link ode.model.Annotation}</li>
         *   <li>{@link ode.model.Dataset}</li>
         *   <li>{@link ode.model.Image}</li>
         *   <li>{@link ode.model.Project}</li>
         *   <li>{@link ode.model.RenderingDef}</li>
         * </ul>
         *
         * <h4>Return maps</h4>
         *
         * The map return values will be indexed by the short type name above:
         * <i>Project</i>, <i>Image</i>, ... All keys which are passed in the
         * StringSet argument will be included in the returned map, even if
         * they have no values. A default value of 0 or the empty list \[]
         * will be used.
         * The only exception to this rule is that the null or empty StringSet
         * implies all valid keys.
         *
         * <h4>Parameters</h4>
         *
         * All methods take a ode::sys::Parameters object and will apply the filter
         * object for paging through the data in order to prevent loading too
         * many objects. If the parameters argument is null or no paging is activated,
         * then the default will be: OFFSET=0, LIMIT=50. Filter.ownerId and
         * Filter.groupId will also be AND'ed to the query if either value is present.
         * If both are null, then the current user id will be used. To retrieve for
         * all users, use <code>ownerId == rlong(-1)</code> and
         * <code>groupId == null</code>.
         *
         * <h4>Merging</h4>
         *
         * The methods which take a StringSet and a Parameters object, also
         * have a <i>bool merge</i> argument. This argument defines whether or
         * not the LIMIT applies to each object independently
         * (<code>\["a","b"] @ 100 == 200</code>) or merges the lists together
         * chronologically (<code>\["a","b"] @ 100 merged == 100</code>).
         *
         * <h4>Time used</h4>
         *
         * Except for Image, IObject.details.updateEvent is used in all cases
         * for determining most recent events. Images are compared via
         * Image.acquisitionDate which is unlike the other properties is
         * modifiable by the user.
         *
         * A typical invocation might look like (in Python):
         * <pre>
         * {@code
         *     timeline = sf.getTimelineService()
         *     params = ParametersI().page(0,100)
         *     types = \["Project","Dataset"])
         *     map = timeline.getByPeriod(types, params, False)
         * }
         * </pre>
         * At this point, map will not contain more than 200 objects.
         *
         * This service is defined only in Server and so no javadoc is available
         * in the ode.api package.
         *
         * TODO: binning, stateful caching, ...
         */
        ["ami", "amd"] interface ITimeline extends ServiceInterface {

            /**
             * Return the last LIMIT annotation __Links__ whose parent (IAnnotated)
             * matches one of the parentTypes, whose child (Annotation) matches one
             * of the childTypes (limit of one for the moment), and who namespace
             * matches via ILIKE.
             *
             * The parents and children will be unloaded. The link will have
             * its creation/update events loaded.
             *
             * If Parameters.theFilter.ownerId or groupId are set, they will be
             * AND'ed to the query to filter results.
             *
             * Merges by default based on parentType.
             */
            idempotent
            IObjectList
                getMostRecentAnnotationLinks(StringSet parentTypes, StringSet childTypes,
                                             StringSet namespaces, ode::sys::Parameters p)
                throws ServerError;

            /**
             * Return the last LIMIT comment annotation links attached to a share by
             * __others__.
             *
             * Note: Currently the storage of these objects is not optimal
             * and so this method may change.
             */
            idempotent
            IObjectList
                getMostRecentShareCommentLinks(ode::sys::Parameters p)
                throws ServerError;

            /**
             * Returns the last LIMIT objects of TYPES as ordered by
             * creation/modification times in the Event table.
             */
            idempotent
            IObjectListMap
                getMostRecentObjects(StringSet types, ode::sys::Parameters p, bool merge)
                throws ServerError;

            /**
             * Returns the given LIMIT objects of TYPES as ordered by
             * creation/modification times in the Event table, but
             * within the given time window.
             */
            idempotent
            IObjectListMap
                getByPeriod(StringSet types, ode::RTime start, ode::RTime end, ode::sys::Parameters p,  bool merge)
                throws ServerError;

            /**
             * Queries the same information as getByPeriod, but only returns the counts
             * for the given objects.
             */
            idempotent
            StringLongMap
                countByPeriod(StringSet types, ode::RTime start, ode::RTime end, ode::sys::Parameters p)
                throws ServerError;

            /**
             * Returns the EventLog table objects which are queried to produce the counts above.
             * Note the concept of <i>period inclusion</i> mentioned above.
             *
             * WORKAROUND WARNING: this method returns non-managed EventLogs (i.e.
             * eventLog.getId() == null) for <i>Image acquisitions</i>.
             */
            idempotent
            EventLogList
                getEventLogsByPeriod(ode::RTime start, ode::RTime end, ode::sys::Parameters p)
                throws ServerError;

        };

    };
};

#endif