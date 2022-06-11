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

#ifndef ODE_CMD_FS_ICE
#define ODE_CMD_FS_ICE

#include <ode/Collections.ice>
#include <ode/cmd/API.ice>

module ode {

    module cmd {

        /**
         * Requests all pyramids files. A {@link FindPyramidsResponse}
         * will be returned under normal conditions, otherwise a {@link ERR}
         * will be returned.
         */
        class FindPyramids extends Request {

            /**
             * Retrieves the pyramids with little endian true or false.
             * If unset, both will be retrieved.
             */
            ode::RBool littleEndian;
            /**
             * Retrieves the pyramids created after a specified time if set.
             */
            ode::RTime importedAfter;
            /**
             * Retrieves the pyramids of length 0 if true
             */
            bool checkEmptyFile;
            /**
             * The maximum number of files to find. No limit will be applied
             * if set to 0 or to a negative value.
             **/
             long limit;
        };

        /**
         * Returns the image Ids corresponding to the pyramid files.
         * A {@link FindPyramidsResponse}
         * will be returned under normal conditions, otherwise a {@link ERR}
         * will be returned.
         **/
        class FindPyramidsResponse extends OK {

            /**
             * The image IDs corresponding to the pyramid
             **/
            ode::api::LongList pyramidFiles;
        };

        /**
         * Requests the file metadata to be loaded for a given
         * image. This should handle both the pre-FS metadata
         * in file annotations as well as loading the metadata
         * directly from the FS files. A {@link OriginalMetadataResponse}
         * will be returned under normal conditions, otherwise a {@link ERR}
         * will be returned.
         **/
        class OriginalMetadataRequest extends Request {
            long imageId;
        };

        /**
         * Successful response for {@link OriginalMetadataRequest}. Contains
         * both the global and the series metadata as maps. Only one
         * of {@code filesetId} or {@code filesetAnnotationId} will be set.
         * Pre-FS images will have {@code filesetAnnotationId} set; otherwise
         * {@code filesetId} will be set.
         **/
        class OriginalMetadataResponse extends OK {

            /**
             * Set to the id of the {@link ode.model.Fileset} that this
             * {@link ode.model.Image} contained in if one exists.
             **/
            ode::RLong filesetId;

            /**
             * Set to the id of the {@link ode.model.FileAnnotation}
             * linked to this {@link ode.model.Image} if one exists.
             **/
            ode::RLong fileAnnotationId;

            /**
             * Metadata which applies to the entire
             * {@link ode.model.Fileset}
             **/
            ode::RTypeDict globalMetadata;

            /**
             * Metadata specific to the series id of this
             * {@link ode.model.Image}.
             * In the {@link ode.model.Fileset} that this
             * {@link ode.model.Image} is contained in, there may be a large
             * number of other images, but the series metadata applies only to
             * this specific one.
             **/
            ode::RTypeDict seriesMetadata;
        };

        /**
         * Request to determine the original files associated with the given
         * image. The image must have an associated Pixels object. Different
         * response objects are returned depending on if the image is FS or
         * pre-FS.
         **/
        class UsedFilesRequest extends Request {
            /**
             * an image ID
             **/
            long imageId;
        };

        /**
         * The used files associated with a pre-FS image.
         **/
        class UsedFilesResponsePreFs extends OK {
            /**
             * The original file IDs of any archived files associated with
             * the image.
             **/
            ode::api::LongList archivedFiles;

            /**
             * The original file IDs of any companion files associated with
             * the image.
             **/
            ode::api::LongList companionFiles;

            /**
             * The original file IDs of any original metadata files associated
             * with the image.
             **/
            ode::api::LongList originalMetadataFiles;
        };

        /**
         * The used files associated with an FS image.
         **/
        class UsedFilesResponse extends OK {
            /**
             * The original file IDs of any binary files associated with the
             * image's particular series.
             **/
            ode::api::LongList binaryFilesThisSeries;

            /**
             * The original file IDs of any binary files associated with the
             * image's fileset but not with its particular series.
             **/
            ode::api::LongList binaryFilesOtherSeries;

            /**
             * The original file IDs of any companion files associated with the
             * image's particular series.
             **/
            ode::api::LongList companionFilesThisSeries;

            /**
             * The original file IDs of any companion files associated with the
             * image's fileset but not with its particular series.
             **/
            ode::api::LongList companionFilesOtherSeries;
        };

        /**
         * Queries and modifies the various binary artifacts
         * which may be linked to an {@link ode.model.Image}.
         *
         * This can be useful, e.g., after converting pre-ODE-5
         * archived original files into {@link ode.model.Fileset}.
         *
         * The command works in several stages:
         *
         *   1. loads an {@link ode.model.Image} by id, failing if none
         *      present.
         *   2. renames Pixels file to '*_bak'
         *   3. deletes existing Pyramidfiles if present;
         *
         * This command can be run multiple times with different settings
         * to iteratively test if the migration is working.
         **/
        class ManageImageBinaries extends Request {

            long imageId;
            bool togglePixels;
            bool deletePyramid;
            bool deleteThumbnails;

        };

        /**
         * {@link Response} from a {@link ManageImageBinaries} {@link Request}.
         * If no action is requested, then the fields of this
         * instance can be examined to see what would be done
         * if requested.
         */
        class ManageImageBinariesResponse extends Response {

            ode::RLong filesetId;
            ode::api::LongList archivedFiles;
            bool pixelsPresent;
            bool pyramidPresent;
            long archivedSize;
            long pixelSize;
            long pyramidSize;
            long thumbnailSize;

        };

        /**
         * Request to determine the disk usage of the given objects
         * and their contents. File-system paths used by multiple objects
         * are de-duplicated in the total count. Specifying a class is
         * equivalent to specifying all its instances as objects.
         *
         * Permissible classes include:
         *   ExperimenterGroup, Experimenter, Project, Dataset,
         *   Folder, Screen, Plate, Well, WellSample,
         *   Image, Pixels, Annotation, Job, Fileset, OriginalFile.
         **/
        ["deprecate:use ode::cmd::DiskUsage2 instead"]
        class DiskUsage extends Request {
            ode::api::StringSet classes;
            ode::api::StringLongListMap objects;
        };

        /**
         * Disk usage report: bytes used and non-empty file counts on the
         * repository file-system for specific objects. The counts from the
         * maps may sum to more than the total if different types of object
         * refer to the same file. Common referrers include:
         *   Annotation for file annotations
         *   FilesetEntry for ODE image files (ODE.fs)
         *   Job for import logs
         *   Pixels for pyramids and ODE images and archived files
         *   Thumbnail for the image thumbnails
         * The above map values are broken down by owner-group keys.
         **/
        ["deprecate:use ode::cmd::DiskUsage2Response instead"]
        class DiskUsageResponse extends Response {
            ode::api::LongPairToStringIntMap fileCountByReferer;
            ode::api::LongPairToStringLongMap bytesUsedByReferer;
            ode::api::LongPairIntMap totalFileCount;
            ode::api::LongPairLongMap totalBytesUsed;
        };

    };
};

#endif