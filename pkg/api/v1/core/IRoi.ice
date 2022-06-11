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

#ifndef ODE_CORE_IROI_ICE
#define ODE_CORE_IROI_ICE

#include <ode/ServicesF.ice>
#include <ode/Collections.ice>

// Items for a separate service:
// -----------------------------
// Histograms
// Volumes, Velocities, Diffusions
["deprecate:IROI is deprecated."]
module ode {

    module core {

        /**
         * Specifies filters used when querying the ROIs.
         */
        class RoiOptions
            {
                StringSet          shapes;
                ode::RInt        limit;
                ode::RInt        offset;
                ode::RLong       userId;
                ode::RLong       groupId;
            };

        /**
         * Returned by most search methods. The RoiOptions is the options object passed
         * into a method, possibly modified by the server if some value was out of range.
         * The RoiList contains all the Rois which matched the given query.
         *
         * The individual shapes of the Rois which matched can be found in the indexes.
         * For example, all the shapes on z=1 can by found by:
         *
         *   ShapeList shapes = byZ.get(1);
         *
         * Shapes which are found on all z or t can be found with:
         *
         *   byZ.get(-1);
         *   byT.get(-1);
         *
         * respectively.
         *
         */
        class RoiResult
            {
                RoiOptions         opts;
                RoiList            rois;

                // Indexes

                IntShapeListMap    byZ;
                IntShapeListMap    byT;
            };

        /**
         *
         * Contains a discrete representation of the geometry of
         * an ode::model::Shape. The x and y array are of the
         * same size with each pair of entries representing a
         * single point in the 2D plane.
         *
         */
        class ShapePoints
            {
                IntegerArray x;
                IntegerArray y;
            };

        /**
         *
         * Contains arrays, one entry per channel, of the statistics
         * for a given shape. All arrays are the same size, except for
         * the channelIds array, which specifies the ids of the logical
         * channels which compose this Shape. If the user specified no
         * logical channels for the Shape, then all logical channels from
         * the Pixels will be in channelIds.
         */
        class ShapeStats
            {
                long         shapeId;
                LongArray    channelIds;
                LongArray    pointsCount;

                DoubleArray  min;
                DoubleArray  max;
                DoubleArray  sum;
                DoubleArray  mean;
                DoubleArray  stdDev;
           };

        sequence<ShapeStats> ShapeStatsList;

        dictionary<long, RoiResult> LongRoiResultMap;

        /**
         * Container for ShapeStats, one with the combined values,
         * and one per shape.
         */
        class RoiStats
            {
                long           roiId;
                long           imageId;
                long           pixelsId;
                ShapeStats     combined;
                ShapeStatsList perShape;
            };

        /**
         * Interface for working with regions of interest.
         */
        ["ami","amd"] interface IRoi extends ServiceInterface
            {

                /**
                 * Returns a RoiResult with a single Roi member.
                 * Shape linkages are properly created.
                 * All Shapes are loaded, as is the Pixels and Image object.
                 * TODO: Annotations?
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                RoiResult findByRoi(long roiId, RoiOptions opts) throws ode::ServerError;

                /**
                 * Returns all the Rois in an Image, indexed via Shape.
                 *
                 * Loads Rois as findByRoi.
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                RoiResult findByImage(long imageId, RoiOptions opts) throws ode::ServerError;

                /**
                 * Returns all the Rois on the given plane, indexed via Shape.
                 *
                 * Loads Rois as findByRoi.
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                RoiResult findByPlane(long imageId, int z, int t, RoiOptions opts) throws ode::ServerError;

                /**
                 * Calculate the points contained within a given shape
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                ShapePoints getPoints(long shapeId) throws ode::ServerError;

                /**
                 * Calculate stats for all the shapes within the given Roi.
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                RoiStats getRoiStats(long roiId) throws ode::ServerError;

                /**
                 * Calculate the stats for the points within the given Shape.
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                ShapeStats getShapeStats(long shapeId) throws ode::ServerError;

                /**
                 * Calculate the stats for the points within the given Shapes.
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                ShapeStatsList getShapeStatsList(LongList shapeIdList) throws ode::ServerError;

                /**
                 * Calculate the stats for the points within the given Shapes.
                 * Varies to the above in the following ways:
                 * - does not allow tiled images
                 * - shapes have to be all belonging to the same image
                 * - unattached z/t use the fallback parameters zForUnattached/tForUnattached
                 *   that is to say there is never more than 1 z/t combination queried
                 * - if channel list is given, only the channels in that list are iterated over
                 * - does not request data from reader on each iteration 
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                ShapeStatsList getShapeStatsRestricted(
                    LongList shapeIdList, int zForUnattached, int tForUnattached, IntegerArray channels) throws ode::ServerError;

                //
                // Measurement-based methods
                //

                /**
                 * Returns a list of {@link ode.model.FileAnnotation}
                 * instances with the namespace
                 * <i>bhojpur.net/measurements</i> which are attached
                 * to the {@link ode.model.Plate} containing the given image
                 * AND which are attached to at least one
                 * {@link ode.model.Roi}
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                AnnotationList getRoiMeasurements(long imageId, RoiOptions opts) throws ode::ServerError;

                /**
                 * Loads the ROIs which are linked to by the given
                 * {@link ode.model.FileAnnotation} id for the given image.
                 *
                 * @param annotationId if -1, logic is identical to findByImage(imageId, opts)
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                RoiResult getMeasuredRois(long imageId, long annotationId, RoiOptions opts) throws ode::ServerError;

                /**
                 * Returns a map from {@link ode.model.FileAnnotation} ids
                 * to {@link RoiResult} instances.
                 * Logic is identical to getMeasuredRois, but Roi data will not be duplicated. (i.e.
                 * the objects are referentially identical)
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                LongRoiResultMap getMeasuredRoisMap(long imageId, LongList annotationIds, RoiOptions opts) throws ode::ServerError;

                /**
                 * Returns the ODE.tables service via the
                 * {@link ode.model.FileAnnotation} id returned
                 * by {@code getImageMeasurements}.
                 */
                ["deprecate:IROI is deprecated."]
                idempotent
                ode::grid::Table* getTable(long annotationId) throws ode::ServerError;

                ["deprecate:IROI is deprecated."]
                void uploadMask(long roiId, int z, int t, Ice::ByteSeq bytes) throws ode::ServerError;

            };

    };

};

#endif