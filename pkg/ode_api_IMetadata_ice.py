#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

from sys import version_info as _version_info_
import Ice, IcePy
import ode_ServicesF_ice
import ode_System_ice
import ode_Collections_ice

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'IMetadata' not in _M_ode.api.__dict__:
    _M_ode.api.IMetadata = Ice.createTempClass()
    class IMetadata(_M_ode.api.ServiceInterface):
        """
        Provides method to interact with acquisition metadata and
        annotations.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IMetadata:
                raise RuntimeError('ode.api.IMetadata is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IMetadata', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IMetadata'

        def ice_staticId():
            return '::ode::api::IMetadata'
        ice_staticId = staticmethod(ice_staticId)

        def loadChannelAcquisitionData_async(self, _cb, ids, current=None):
            """
            Loads the logical channels and the acquisition metadata
            related to them.
            Arguments:
            _cb -- The asynchronous callback object.
            ids -- The collection of logical channel's ids. Mustn't be null.
            current -- The Current object for the invocation.
            """
            pass

        def loadAnnotations_async(self, _cb, rootType, rootIds, annotationTypes, annotatorIds, options, current=None):
            """
            Loads all the annotations of given types, that have been
            attached to the specified rootNodes for the
            specified annotatorIds.
            If no types specified, all annotations will be loaded.
            This method looks for the annotations that have been
            attached to each of the specified objects. It then maps
            each rootId onto the set of annotations
            that were found for that node. If no annotations were found
            for that node, then the entry will be null.
            Otherwise it will be a Map containing
            ode.model.Annotation objects.
            Arguments:
            _cb -- The asynchronous callback object.
            rootType -- The type of the nodes the annotations are linked to. Mustn't be null.
            rootIds -- Ids of the objects of type rootType. Mustn't be null.
            annotationTypes -- The types of annotation to retrieve. If null all annotations will be loaded. String of the type ode.model.annotations.*.
            annotatorIds -- Ids of the users for whom annotations should be retrieved. If null, all annotations returned.
            options -- 
            current -- The Current object for the invocation.
            """
            pass

        def loadSpecifiedAnnotations_async(self, _cb, annotationType, include, exclude, options, current=None):
            """
            Loads all the annotations of a given type.
            It is possible to filter the annotations by including or
            excluding name spaces set on the annotations.
            Arguments:
            _cb -- The asynchronous callback object.
            annotationType -- The type of annotations to load.
            include -- Include the annotations with the specified name spaces.
            exclude -- Exclude the annotations with the specified name spaces.
            options -- The POJO options.
            current -- The Current object for the invocation.
            """
            pass

        def loadTagContent_async(self, _cb, ids, options, current=None):
            """
            Loads the TagSet if the id is specified otherwise loads
            all the TagSet.
            Arguments:
            _cb -- The asynchronous callback object.
            ids -- The id of the tag to load or -1.
            options -- 
            current -- The Current object for the invocation.
            """
            pass

        def loadTagSets_async(self, _cb, options, current=None):
            """
            Loads all the TagSets. Returns a collection of
            AnnotationAnnotatioLink objects and, if the
            orphan parameters is true, the
            TagAnnotation object.
            Note that the difference between a TagSet and a Tag is made
            using the NS_INSIGHT_TAG_SET namespace.
            Arguments:
            _cb -- The asynchronous callback object.
            options -- The POJO options.
            current -- The Current object for the invocation.
            """
            pass

        def getTaggedObjectsCount_async(self, _cb, ids, options, current=None):
            """
            Returns a map whose key is a tag id and the value the
            number of Projects, Datasets, and Images linked to that tag.
            Arguments:
            _cb -- The asynchronous callback object.
            ids -- The collection of ids.
            options -- The POJO options.
            current -- The Current object for the invocation.
            """
            pass

        def countSpecifiedAnnotations_async(self, _cb, annotationType, include, exclude, options, current=None):
            """
            Counts the number of annotation of a given type.
            Arguments:
            _cb -- The asynchronous callback object.
            annotationType -- The type of annotations to load.
            include -- The collection of name space, one of the constants defined by this class.
            exclude -- The collection of name space, one of the constants defined by this class.
            options -- The POJO options.
            current -- The Current object for the invocation.
            """
            pass

        def loadAnnotation_async(self, _cb, annotationIds, current=None):
            """
            Loads the specified annotations.
            Arguments:
            _cb -- The asynchronous callback object.
            annotationIds -- The collection of annotation ids.
            current -- The Current object for the invocation.
            """
            pass

        def loadInstrument_async(self, _cb, id, current=None):
            """
            Loads the instrument and its components i.e. detectors,
            objectives, etc.
            Arguments:
            _cb -- The asynchronous callback object.
            id -- The id of the instrument to load.
            current -- The Current object for the invocation.
            """
            pass

        def loadAnnotationsUsedNotOwned_async(self, _cb, annotationType, userID, current=None):
            """
            Loads the annotations of a given type used by the specified
            user but not owned by the user.
            Arguments:
            _cb -- The asynchronous callback object.
            annotationType -- The type of annotations to load.
            userID -- The identifier of the user.
            current -- The Current object for the invocation.
            """
            pass

        def countAnnotationsUsedNotOwned_async(self, _cb, annotationType, userID, current=None):
            """
            Counts the number of annotation of a given type used by the
            specified user but not owned by the user.
            Arguments:
            _cb -- The asynchronous callback object.
            annotationType -- The type of annotations to load.
            userID -- The identifier of the user.
            current -- The Current object for the invocation.
            """
            pass

        def loadSpecifiedAnnotationsLinkedTo_async(self, _cb, annotationType, include, exclude, rootNodeType, rootNodeIds, options, current=None):
            """
            Loads the annotations of a given type linked to the
            specified objects. It is possible to filter the annotations
            by including or excluding name spaces set on the
            annotations.
            This method looks for the annotations that have been
            attached to each of the specified objects. It then maps
            each rootNodeId onto the set of annotations
            that were found for that node. If no annotations were found
            for that node, the map will not contain an entry for that
            node. Otherwise it will be a Set containing
            ode.model.Annotation objects.
            The rootNodeType supported are:
            Project, Dataset, Image, Pixels, Screen, Plate,
            PlateAcquisition, Well, Fileset.
            Arguments:
            _cb -- The asynchronous callback object.
            annotationType -- The type of annotations to load.
            include -- Include the annotations with the specified name spaces.
            exclude -- Exclude the annotations with the specified name spaces.
            rootNodeType -- The type of objects the annotations are linked to.
            rootNodeIds -- The identifiers of the objects.
            options -- The POJO options.
            current -- The Current object for the invocation.
            """
            pass

        def loadLogFiles_async(self, _cb, rootType, ids, current=None):
            """
            Finds the original file IDs for the import logs
            corresponding to the given Image or Fileset IDs.
            Arguments:
            _cb -- The asynchronous callback object.
            rootType -- the root node type, may be ode.model.Image or ode.model.Fileset
            ids -- the IDs of the entities for which the import log original file IDs are required
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IMetadata)

        __repr__ = __str__

    _M_ode.api.IMetadataPrx = Ice.createTempClass()
    class IMetadataPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Loads the logical channels and the acquisition metadata
        related to them.
        Arguments:
        ids -- The collection of logical channel's ids. Mustn't be null.
        _ctx -- The request context for the invocation.
        Returns: The collection of loaded logical channels.
        """
        def loadChannelAcquisitionData(self, ids, _ctx=None):
            return _M_ode.api.IMetadata._op_loadChannelAcquisitionData.invoke(self, ((ids, ), _ctx))

        """
        Loads the logical channels and the acquisition metadata
        related to them.
        Arguments:
        ids -- The collection of logical channel's ids. Mustn't be null.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadChannelAcquisitionData(self, ids, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_loadChannelAcquisitionData.begin(self, ((ids, ), _response, _ex, _sent, _ctx))

        """
        Loads the logical channels and the acquisition metadata
        related to them.
        Arguments:
        ids -- The collection of logical channel's ids. Mustn't be null.
        Returns: The collection of loaded logical channels.
        """
        def end_loadChannelAcquisitionData(self, _r):
            return _M_ode.api.IMetadata._op_loadChannelAcquisitionData.end(self, _r)

        """
        Loads all the annotations of given types, that have been
        attached to the specified rootNodes for the
        specified annotatorIds.
        If no types specified, all annotations will be loaded.
        This method looks for the annotations that have been
        attached to each of the specified objects. It then maps
        each rootId onto the set of annotations
        that were found for that node. If no annotations were found
        for that node, then the entry will be null.
        Otherwise it will be a Map containing
        ode.model.Annotation objects.
        Arguments:
        rootType -- The type of the nodes the annotations are linked to. Mustn't be null.
        rootIds -- Ids of the objects of type rootType. Mustn't be null.
        annotationTypes -- The types of annotation to retrieve. If null all annotations will be loaded. String of the type ode.model.annotations.*.
        annotatorIds -- Ids of the users for whom annotations should be retrieved. If null, all annotations returned.
        options -- 
        _ctx -- The request context for the invocation.
        Returns: A map whose key is rootId and value the Map of all annotations for that node or null.
        """
        def loadAnnotations(self, rootType, rootIds, annotationTypes, annotatorIds, options, _ctx=None):
            return _M_ode.api.IMetadata._op_loadAnnotations.invoke(self, ((rootType, rootIds, annotationTypes, annotatorIds, options), _ctx))

        """
        Loads all the annotations of given types, that have been
        attached to the specified rootNodes for the
        specified annotatorIds.
        If no types specified, all annotations will be loaded.
        This method looks for the annotations that have been
        attached to each of the specified objects. It then maps
        each rootId onto the set of annotations
        that were found for that node. If no annotations were found
        for that node, then the entry will be null.
        Otherwise it will be a Map containing
        ode.model.Annotation objects.
        Arguments:
        rootType -- The type of the nodes the annotations are linked to. Mustn't be null.
        rootIds -- Ids of the objects of type rootType. Mustn't be null.
        annotationTypes -- The types of annotation to retrieve. If null all annotations will be loaded. String of the type ode.model.annotations.*.
        annotatorIds -- Ids of the users for whom annotations should be retrieved. If null, all annotations returned.
        options -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadAnnotations(self, rootType, rootIds, annotationTypes, annotatorIds, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_loadAnnotations.begin(self, ((rootType, rootIds, annotationTypes, annotatorIds, options), _response, _ex, _sent, _ctx))

        """
        Loads all the annotations of given types, that have been
        attached to the specified rootNodes for the
        specified annotatorIds.
        If no types specified, all annotations will be loaded.
        This method looks for the annotations that have been
        attached to each of the specified objects. It then maps
        each rootId onto the set of annotations
        that were found for that node. If no annotations were found
        for that node, then the entry will be null.
        Otherwise it will be a Map containing
        ode.model.Annotation objects.
        Arguments:
        rootType -- The type of the nodes the annotations are linked to. Mustn't be null.
        rootIds -- Ids of the objects of type rootType. Mustn't be null.
        annotationTypes -- The types of annotation to retrieve. If null all annotations will be loaded. String of the type ode.model.annotations.*.
        annotatorIds -- Ids of the users for whom annotations should be retrieved. If null, all annotations returned.
        options -- 
        Returns: A map whose key is rootId and value the Map of all annotations for that node or null.
        """
        def end_loadAnnotations(self, _r):
            return _M_ode.api.IMetadata._op_loadAnnotations.end(self, _r)

        """
        Loads all the annotations of a given type.
        It is possible to filter the annotations by including or
        excluding name spaces set on the annotations.
        Arguments:
        annotationType -- The type of annotations to load.
        include -- Include the annotations with the specified name spaces.
        exclude -- Exclude the annotations with the specified name spaces.
        options -- The POJO options.
        _ctx -- The request context for the invocation.
        Returns: A collection of found annotations.
        """
        def loadSpecifiedAnnotations(self, annotationType, include, exclude, options, _ctx=None):
            return _M_ode.api.IMetadata._op_loadSpecifiedAnnotations.invoke(self, ((annotationType, include, exclude, options), _ctx))

        """
        Loads all the annotations of a given type.
        It is possible to filter the annotations by including or
        excluding name spaces set on the annotations.
        Arguments:
        annotationType -- The type of annotations to load.
        include -- Include the annotations with the specified name spaces.
        exclude -- Exclude the annotations with the specified name spaces.
        options -- The POJO options.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadSpecifiedAnnotations(self, annotationType, include, exclude, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_loadSpecifiedAnnotations.begin(self, ((annotationType, include, exclude, options), _response, _ex, _sent, _ctx))

        """
        Loads all the annotations of a given type.
        It is possible to filter the annotations by including or
        excluding name spaces set on the annotations.
        Arguments:
        annotationType -- The type of annotations to load.
        include -- Include the annotations with the specified name spaces.
        exclude -- Exclude the annotations with the specified name spaces.
        options -- The POJO options.
        Returns: A collection of found annotations.
        """
        def end_loadSpecifiedAnnotations(self, _r):
            return _M_ode.api.IMetadata._op_loadSpecifiedAnnotations.end(self, _r)

        """
        Loads the TagSet if the id is specified otherwise loads
        all the TagSet.
        Arguments:
        ids -- The id of the tag to load or -1.
        options -- 
        _ctx -- The request context for the invocation.
        Returns: Map whose key is a Tag/TagSet and the value either a Map or a list of related DataObject.
        """
        def loadTagContent(self, ids, options, _ctx=None):
            return _M_ode.api.IMetadata._op_loadTagContent.invoke(self, ((ids, options), _ctx))

        """
        Loads the TagSet if the id is specified otherwise loads
        all the TagSet.
        Arguments:
        ids -- The id of the tag to load or -1.
        options -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadTagContent(self, ids, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_loadTagContent.begin(self, ((ids, options), _response, _ex, _sent, _ctx))

        """
        Loads the TagSet if the id is specified otherwise loads
        all the TagSet.
        Arguments:
        ids -- The id of the tag to load or -1.
        options -- 
        Returns: Map whose key is a Tag/TagSet and the value either a Map or a list of related DataObject.
        """
        def end_loadTagContent(self, _r):
            return _M_ode.api.IMetadata._op_loadTagContent.end(self, _r)

        """
        Loads all the TagSets. Returns a collection of
        AnnotationAnnotatioLink objects and, if the
        orphan parameters is true, the
        TagAnnotation object.
        Note that the difference between a TagSet and a Tag is made
        using the NS_INSIGHT_TAG_SET namespace.
        Arguments:
        options -- The POJO options.
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def loadTagSets(self, options, _ctx=None):
            return _M_ode.api.IMetadata._op_loadTagSets.invoke(self, ((options, ), _ctx))

        """
        Loads all the TagSets. Returns a collection of
        AnnotationAnnotatioLink objects and, if the
        orphan parameters is true, the
        TagAnnotation object.
        Note that the difference between a TagSet and a Tag is made
        using the NS_INSIGHT_TAG_SET namespace.
        Arguments:
        options -- The POJO options.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadTagSets(self, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_loadTagSets.begin(self, ((options, ), _response, _ex, _sent, _ctx))

        """
        Loads all the TagSets. Returns a collection of
        AnnotationAnnotatioLink objects and, if the
        orphan parameters is true, the
        TagAnnotation object.
        Note that the difference between a TagSet and a Tag is made
        using the NS_INSIGHT_TAG_SET namespace.
        Arguments:
        options -- The POJO options.
        Returns: See above.
        """
        def end_loadTagSets(self, _r):
            return _M_ode.api.IMetadata._op_loadTagSets.end(self, _r)

        """
        Returns a map whose key is a tag id and the value the
        number of Projects, Datasets, and Images linked to that tag.
        Arguments:
        ids -- The collection of ids.
        options -- The POJO options.
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def getTaggedObjectsCount(self, ids, options, _ctx=None):
            return _M_ode.api.IMetadata._op_getTaggedObjectsCount.invoke(self, ((ids, options), _ctx))

        """
        Returns a map whose key is a tag id and the value the
        number of Projects, Datasets, and Images linked to that tag.
        Arguments:
        ids -- The collection of ids.
        options -- The POJO options.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getTaggedObjectsCount(self, ids, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_getTaggedObjectsCount.begin(self, ((ids, options), _response, _ex, _sent, _ctx))

        """
        Returns a map whose key is a tag id and the value the
        number of Projects, Datasets, and Images linked to that tag.
        Arguments:
        ids -- The collection of ids.
        options -- The POJO options.
        Returns: See above.
        """
        def end_getTaggedObjectsCount(self, _r):
            return _M_ode.api.IMetadata._op_getTaggedObjectsCount.end(self, _r)

        """
        Counts the number of annotation of a given type.
        Arguments:
        annotationType -- The type of annotations to load.
        include -- The collection of name space, one of the constants defined by this class.
        exclude -- The collection of name space, one of the constants defined by this class.
        options -- The POJO options.
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def countSpecifiedAnnotations(self, annotationType, include, exclude, options, _ctx=None):
            return _M_ode.api.IMetadata._op_countSpecifiedAnnotations.invoke(self, ((annotationType, include, exclude, options), _ctx))

        """
        Counts the number of annotation of a given type.
        Arguments:
        annotationType -- The type of annotations to load.
        include -- The collection of name space, one of the constants defined by this class.
        exclude -- The collection of name space, one of the constants defined by this class.
        options -- The POJO options.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_countSpecifiedAnnotations(self, annotationType, include, exclude, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_countSpecifiedAnnotations.begin(self, ((annotationType, include, exclude, options), _response, _ex, _sent, _ctx))

        """
        Counts the number of annotation of a given type.
        Arguments:
        annotationType -- The type of annotations to load.
        include -- The collection of name space, one of the constants defined by this class.
        exclude -- The collection of name space, one of the constants defined by this class.
        options -- The POJO options.
        Returns: See above.
        """
        def end_countSpecifiedAnnotations(self, _r):
            return _M_ode.api.IMetadata._op_countSpecifiedAnnotations.end(self, _r)

        """
        Loads the specified annotations.
        Arguments:
        annotationIds -- The collection of annotation ids.
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def loadAnnotation(self, annotationIds, _ctx=None):
            return _M_ode.api.IMetadata._op_loadAnnotation.invoke(self, ((annotationIds, ), _ctx))

        """
        Loads the specified annotations.
        Arguments:
        annotationIds -- The collection of annotation ids.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadAnnotation(self, annotationIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_loadAnnotation.begin(self, ((annotationIds, ), _response, _ex, _sent, _ctx))

        """
        Loads the specified annotations.
        Arguments:
        annotationIds -- The collection of annotation ids.
        Returns: See above.
        """
        def end_loadAnnotation(self, _r):
            return _M_ode.api.IMetadata._op_loadAnnotation.end(self, _r)

        """
        Loads the instrument and its components i.e. detectors,
        objectives, etc.
        Arguments:
        id -- The id of the instrument to load.
        _ctx -- The request context for the invocation.
        Returns: See above
        """
        def loadInstrument(self, id, _ctx=None):
            return _M_ode.api.IMetadata._op_loadInstrument.invoke(self, ((id, ), _ctx))

        """
        Loads the instrument and its components i.e. detectors,
        objectives, etc.
        Arguments:
        id -- The id of the instrument to load.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadInstrument(self, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_loadInstrument.begin(self, ((id, ), _response, _ex, _sent, _ctx))

        """
        Loads the instrument and its components i.e. detectors,
        objectives, etc.
        Arguments:
        id -- The id of the instrument to load.
        Returns: See above
        """
        def end_loadInstrument(self, _r):
            return _M_ode.api.IMetadata._op_loadInstrument.end(self, _r)

        """
        Loads the annotations of a given type used by the specified
        user but not owned by the user.
        Arguments:
        annotationType -- The type of annotations to load.
        userID -- The identifier of the user.
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def loadAnnotationsUsedNotOwned(self, annotationType, userID, _ctx=None):
            return _M_ode.api.IMetadata._op_loadAnnotationsUsedNotOwned.invoke(self, ((annotationType, userID), _ctx))

        """
        Loads the annotations of a given type used by the specified
        user but not owned by the user.
        Arguments:
        annotationType -- The type of annotations to load.
        userID -- The identifier of the user.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadAnnotationsUsedNotOwned(self, annotationType, userID, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_loadAnnotationsUsedNotOwned.begin(self, ((annotationType, userID), _response, _ex, _sent, _ctx))

        """
        Loads the annotations of a given type used by the specified
        user but not owned by the user.
        Arguments:
        annotationType -- The type of annotations to load.
        userID -- The identifier of the user.
        Returns: See above.
        """
        def end_loadAnnotationsUsedNotOwned(self, _r):
            return _M_ode.api.IMetadata._op_loadAnnotationsUsedNotOwned.end(self, _r)

        """
        Counts the number of annotation of a given type used by the
        specified user but not owned by the user.
        Arguments:
        annotationType -- The type of annotations to load.
        userID -- The identifier of the user.
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def countAnnotationsUsedNotOwned(self, annotationType, userID, _ctx=None):
            return _M_ode.api.IMetadata._op_countAnnotationsUsedNotOwned.invoke(self, ((annotationType, userID), _ctx))

        """
        Counts the number of annotation of a given type used by the
        specified user but not owned by the user.
        Arguments:
        annotationType -- The type of annotations to load.
        userID -- The identifier of the user.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_countAnnotationsUsedNotOwned(self, annotationType, userID, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_countAnnotationsUsedNotOwned.begin(self, ((annotationType, userID), _response, _ex, _sent, _ctx))

        """
        Counts the number of annotation of a given type used by the
        specified user but not owned by the user.
        Arguments:
        annotationType -- The type of annotations to load.
        userID -- The identifier of the user.
        Returns: See above.
        """
        def end_countAnnotationsUsedNotOwned(self, _r):
            return _M_ode.api.IMetadata._op_countAnnotationsUsedNotOwned.end(self, _r)

        """
        Loads the annotations of a given type linked to the
        specified objects. It is possible to filter the annotations
        by including or excluding name spaces set on the
        annotations.
        This method looks for the annotations that have been
        attached to each of the specified objects. It then maps
        each rootNodeId onto the set of annotations
        that were found for that node. If no annotations were found
        for that node, the map will not contain an entry for that
        node. Otherwise it will be a Set containing
        ode.model.Annotation objects.
        The rootNodeType supported are:
        Project, Dataset, Image, Pixels, Screen, Plate,
        PlateAcquisition, Well, Fileset.
        Arguments:
        annotationType -- The type of annotations to load.
        include -- Include the annotations with the specified name spaces.
        exclude -- Exclude the annotations with the specified name spaces.
        rootNodeType -- The type of objects the annotations are linked to.
        rootNodeIds -- The identifiers of the objects.
        options -- The POJO options.
        _ctx -- The request context for the invocation.
        Returns: A collection of found annotations.
        """
        def loadSpecifiedAnnotationsLinkedTo(self, annotationType, include, exclude, rootNodeType, rootNodeIds, options, _ctx=None):
            return _M_ode.api.IMetadata._op_loadSpecifiedAnnotationsLinkedTo.invoke(self, ((annotationType, include, exclude, rootNodeType, rootNodeIds, options), _ctx))

        """
        Loads the annotations of a given type linked to the
        specified objects. It is possible to filter the annotations
        by including or excluding name spaces set on the
        annotations.
        This method looks for the annotations that have been
        attached to each of the specified objects. It then maps
        each rootNodeId onto the set of annotations
        that were found for that node. If no annotations were found
        for that node, the map will not contain an entry for that
        node. Otherwise it will be a Set containing
        ode.model.Annotation objects.
        The rootNodeType supported are:
        Project, Dataset, Image, Pixels, Screen, Plate,
        PlateAcquisition, Well, Fileset.
        Arguments:
        annotationType -- The type of annotations to load.
        include -- Include the annotations with the specified name spaces.
        exclude -- Exclude the annotations with the specified name spaces.
        rootNodeType -- The type of objects the annotations are linked to.
        rootNodeIds -- The identifiers of the objects.
        options -- The POJO options.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadSpecifiedAnnotationsLinkedTo(self, annotationType, include, exclude, rootNodeType, rootNodeIds, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_loadSpecifiedAnnotationsLinkedTo.begin(self, ((annotationType, include, exclude, rootNodeType, rootNodeIds, options), _response, _ex, _sent, _ctx))

        """
        Loads the annotations of a given type linked to the
        specified objects. It is possible to filter the annotations
        by including or excluding name spaces set on the
        annotations.
        This method looks for the annotations that have been
        attached to each of the specified objects. It then maps
        each rootNodeId onto the set of annotations
        that were found for that node. If no annotations were found
        for that node, the map will not contain an entry for that
        node. Otherwise it will be a Set containing
        ode.model.Annotation objects.
        The rootNodeType supported are:
        Project, Dataset, Image, Pixels, Screen, Plate,
        PlateAcquisition, Well, Fileset.
        Arguments:
        annotationType -- The type of annotations to load.
        include -- Include the annotations with the specified name spaces.
        exclude -- Exclude the annotations with the specified name spaces.
        rootNodeType -- The type of objects the annotations are linked to.
        rootNodeIds -- The identifiers of the objects.
        options -- The POJO options.
        Returns: A collection of found annotations.
        """
        def end_loadSpecifiedAnnotationsLinkedTo(self, _r):
            return _M_ode.api.IMetadata._op_loadSpecifiedAnnotationsLinkedTo.end(self, _r)

        """
        Finds the original file IDs for the import logs
        corresponding to the given Image or Fileset IDs.
        Arguments:
        rootType -- the root node type, may be ode.model.Image or ode.model.Fileset
        ids -- the IDs of the entities for which the import log original file IDs are required
        _ctx -- The request context for the invocation.
        Returns: the original file IDs of the import logs
        """
        def loadLogFiles(self, rootType, ids, _ctx=None):
            return _M_ode.api.IMetadata._op_loadLogFiles.invoke(self, ((rootType, ids), _ctx))

        """
        Finds the original file IDs for the import logs
        corresponding to the given Image or Fileset IDs.
        Arguments:
        rootType -- the root node type, may be ode.model.Image or ode.model.Fileset
        ids -- the IDs of the entities for which the import log original file IDs are required
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadLogFiles(self, rootType, ids, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IMetadata._op_loadLogFiles.begin(self, ((rootType, ids), _response, _ex, _sent, _ctx))

        """
        Finds the original file IDs for the import logs
        corresponding to the given Image or Fileset IDs.
        Arguments:
        rootType -- the root node type, may be ode.model.Image or ode.model.Fileset
        ids -- the IDs of the entities for which the import log original file IDs are required
        Returns: the original file IDs of the import logs
        """
        def end_loadLogFiles(self, _r):
            return _M_ode.api.IMetadata._op_loadLogFiles.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IMetadataPrx.ice_checkedCast(proxy, '::ode::api::IMetadata', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IMetadataPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IMetadata'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IMetadataPrx = IcePy.defineProxy('::ode::api::IMetadata', IMetadataPrx)

    _M_ode.api._t_IMetadata = IcePy.defineClass('::ode::api::IMetadata', IMetadata, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IMetadata._ice_type = _M_ode.api._t_IMetadata

    IMetadata._op_loadChannelAcquisitionData = IcePy.Operation('loadChannelAcquisitionData', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_LongList, False, 0),), (), ((), _M_ode.api._t_LogicalChannelList, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_loadAnnotations = IcePy.Operation('loadAnnotations', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0), ((), _M_ode.api._t_StringSet, False, 0), ((), _M_ode.sys._t_LongList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_LongIObjectListMap, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_loadSpecifiedAnnotations = IcePy.Operation('loadSpecifiedAnnotations', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.api._t_StringSet, False, 0), ((), _M_ode.api._t_StringSet, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_AnnotationList, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_loadTagContent = IcePy.Operation('loadTagContent', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_LongList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_LongIObjectListMap, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_loadTagSets = IcePy.Operation('loadTagSets', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_Parameters, False, 0),), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_getTaggedObjectsCount = IcePy.Operation('getTaggedObjectsCount', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_LongList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.sys._t_CountMap, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_countSpecifiedAnnotations = IcePy.Operation('countSpecifiedAnnotations', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.api._t_StringSet, False, 0), ((), _M_ode.api._t_StringSet, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode._t_RLong, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_loadAnnotation = IcePy.Operation('loadAnnotation', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_LongList, False, 0),), (), ((), _M_ode.api._t_AnnotationList, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_loadInstrument = IcePy.Operation('loadInstrument', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.model._t_Instrument, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_loadAnnotationsUsedNotOwned = IcePy.Operation('loadAnnotationsUsedNotOwned', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_countAnnotationsUsedNotOwned = IcePy.Operation('countAnnotationsUsedNotOwned', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode._t_RLong, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_loadSpecifiedAnnotationsLinkedTo = IcePy.Operation('loadSpecifiedAnnotationsLinkedTo', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.api._t_StringSet, False, 0), ((), _M_ode.api._t_StringSet, False, 0), ((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_LongAnnotationListMap, False, 0), (_M_ode._t_ServerError,))
    IMetadata._op_loadLogFiles = IcePy.Operation('loadLogFiles', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0)), (), ((), _M_ode.api._t_LongIObjectListMap, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.IMetadata = IMetadata
    del IMetadata

    _M_ode.api.IMetadataPrx = IMetadataPrx
    del IMetadataPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
