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

#ifndef ODE_API_IMETADATA_ICE
#define ODE_API_IMETADATA_ICE

#include <ode/ServicesF.ice>
#include <ode/System.ice>
#include <ode/Collections.ice>

module ode {

    module api {
        /**
         * Provides method to interact with acquisition metadata and
         * annotations.
         */
        ["ami", "amd"] interface IMetadata extends ServiceInterface
            {
                /**
                 * Loads the logical channels and the acquisition metadata
                 * related to them.
                 *
                 * @param ids The collection of logical channel's ids.
                 * 		      Mustn't be <code>null</code>.
                 * @return The collection of loaded logical channels.
                 */
                idempotent LogicalChannelList loadChannelAcquisitionData(ode::sys::LongList ids) throws ServerError;

                /**
                 * Loads all the annotations of given types, that have been
                 * attached to the specified <code>rootNodes</code> for the
                 * specified <code>annotatorIds</code>.
                 * If no types specified, all annotations will be loaded.
                 * This method looks for the annotations that have been
                 * attached to each of the specified objects. It then maps
                 * each <code>rootId</code> onto the set of annotations
                 * that were found for that node. If no annotations were found
                 * for that node, then the entry will be <code>null</code>.
                 * Otherwise it will be a <code>Map</code> containing
                 * {@link ode.model.Annotation} objects.
                 *
                 * @param rootType
                 *      The type of the nodes the annotations are linked to.
                 *      Mustn't be <code>null</code>.
                 * @param rootIds
                 *      Ids of the objects of type <code>rootType</code>.
                 * 		Mustn't be <code>null</code>.
                 * @param annotationTypes
                 *      The types of annotation to retrieve. If
                 *      <code>null</code> all annotations will be loaded.
                 *      String of the type
                 *      <code>ode.model.annotations.*</code>.
                 * @param annotatorIds
                 *      Ids of the users for whom annotations should be
                 *      retrieved. If <code>null</code>, all annotations
                 *      returned.
                 * @param options
                 * @return A map whose key is rootId and value the
                 *         <code>Map</code> of all annotations for that node
                 *         or <code>null</code>.
                 */
                idempotent LongIObjectListMap loadAnnotations(string rootType, ode::sys::LongList rootIds,
                                                         ode::api::StringSet annotationTypes, ode::sys::LongList annotatorIds,
                                                         ode::sys::Parameters options) throws ServerError;

                /**
                 * Loads all the annotations of a given type.
                 * It is possible to filter the annotations by including or
                 * excluding name spaces set on the annotations.
                 *
                 * @param annotationType The type of annotations to load.
                 * @param include
                 *      Include the annotations with the specified name spaces.
                 * @param exclude
                 *      Exclude the annotations with the specified name spaces.
                 * @param options   The POJO options.
                 * @return          A collection of found annotations.
                 */
                idempotent AnnotationList loadSpecifiedAnnotations(string annotationType,
                                                                   ode::api::StringSet include,
                                                                   ode::api::StringSet exclude,
                                                                   ode::sys::Parameters options) throws ServerError;
                //idempotent ode::metadata::TagSetContainerList loadTagSets(long id, bool withObjects, ode::sys::Parameters options) throws ServerError;
                //idempotent ode::metadata::TagContainerList loadTags(long id, bool withObjects, ode::sys::Parameters options) throws ServerError;

                /**
                 * Loads the TagSet if the id is specified otherwise loads
                 * all the TagSet.
                 *
                 * @param ids The id of the tag to load or <code>-1</code>.
                 * @return Map whose key is a <code>Tag/TagSet</code> and the
                 *         value either a Map or a list of related
                 *         <code>DataObject</code>.
                 */
                idempotent LongIObjectListMap loadTagContent(ode::sys::LongList ids, ode::sys::Parameters options) throws ServerError;

                /**
                 * Loads all the TagSets. Returns a collection of
                 * <code>AnnotationAnnotatioLink</code> objects and, if the
                 * <code>orphan</code> parameters is <code>true</code>, the
                 * <code>TagAnnotation</code> object.
                 * Note that the difference between a TagSet and a Tag is made
                 * using the NS_INSIGHT_TAG_SET namespace.
                 *
                 * @param options The POJO options.
                 * @return See above.
                 */
                idempotent IObjectList loadTagSets(ode::sys::Parameters options) throws ServerError;

                /**
                 * Returns a map whose key is a tag id and the value the
                 * number of Projects, Datasets, and Images linked to that tag.
                 *
                 * @param ids The collection of ids.
                 * @param options The POJO options.
                 * @return See above.
                 */
                idempotent ode::sys::CountMap getTaggedObjectsCount(ode::sys::LongList ids, ode::sys::Parameters options) throws ServerError;

                /**
                 * Counts the number of annotation of a given type.
                 *
                 * @param annotationType The type of annotations to load.
                 * @param include   The collection of name space, one of the
                 *                  constants defined by this class.
                 * @param exclude   The collection of name space, one of the
                 *                  constants defined by this class.
                 * @param options	The POJO options.
                 * @return See above.
                 */
                ode::RLong countSpecifiedAnnotations(string annotationType,
                                                       ode::api::StringSet include,
                                                       ode::api::StringSet exclude,
                                                       ode::sys::Parameters options) throws ServerError;

                /**
                 * Loads the specified annotations.
                 *
                 * @param annotationIds The collection of annotation ids.
                 * @return              See above.
                 */
                idempotent AnnotationList loadAnnotation(ode::sys::LongList annotationIds) throws ServerError;

                /**
                 * Loads the instrument and its components i.e. detectors,
                 * objectives, etc.
                 *
                 * @param id    The id of the instrument to load.
                 * @return      See above
                 */
                idempotent ode::model::Instrument loadInstrument(long id) throws ServerError;

                /**
                 * Loads the annotations of a given type used by the specified
                 * user but not owned by the user.
                 *
                 * @param annotationType    The type of annotations to load.
                 * @param userID            The identifier of the user.
                 * @return                  See above.
                 */
                idempotent IObjectList loadAnnotationsUsedNotOwned(string annotationType, long userID) throws ServerError;

                /**
                 * Counts the number of annotation of a given type used by the
                 * specified user but not owned by the user.
                 *
                 * @param annotationType    The type of annotations to load.
                 * @param userID            The identifier of the user.
                 * @return                  See above.
                 */
                ode::RLong countAnnotationsUsedNotOwned(string annotationType, long userID) throws ServerError;

                /**
                 * Loads the annotations of a given type linked to the
                 * specified objects. It is possible to filter the annotations
                 * by including or excluding name spaces set on the
                 * annotations.
                 *
                 * This method looks for the annotations that have been
                 * attached to each of the specified objects. It then maps
                 * each <code>rootNodeId</code> onto the set of annotations
                 * that were found for that node. If no annotations were found
                 * for that node, the map will not contain an entry for that
                 * node. Otherwise it will be a <code>Set</code> containing
                 * {@link ode.model.Annotation} objects.
                 * The <code>rootNodeType</code> supported are:
                 * Project, Dataset, Image, Pixels, Screen, Plate,
                 * PlateAcquisition, Well, Fileset.
                 *
                 * @param annotationType  The type of annotations to load.
                 * @param include
                 *      Include the annotations with the specified name spaces.
                 * @param exclude
                 *      Exclude the annotations with the specified name spaces.
                 * @param rootNodeType
                 *      The type of objects the annotations are linked to.
                 * @param rootNodeIds   The identifiers of the objects.
                 * @param options       The POJO options.
                 * @return              A collection of found annotations.
                 */
                idempotent LongAnnotationListMap loadSpecifiedAnnotationsLinkedTo(string annotationType,
                                                                   ode::api::StringSet include,
                                                                   ode::api::StringSet exclude,
                                                                   string rootNodeType,
                                                                   ode::sys::LongList rootNodeIds,
                                                                   ode::sys::Parameters options) throws ServerError;

                /**
                 * Finds the original file IDs for the import logs
                 * corresponding to the given Image or Fileset IDs.
                 *
                 * @param rootType
                 *       the root node type, may be {@link ode.model.Image}
                 *       or {@link ode.model.Fileset}
                 * @param ids
                 *       the IDs of the entities for which the import log
                 *       original file IDs are required
                 * @return the original file IDs of the import logs
                 **/
                idempotent LongIObjectListMap loadLogFiles(string rootType, ode::sys::LongList ids) throws ServerError;
            };

    };
};

#endif