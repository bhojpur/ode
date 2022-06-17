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
import ode_ModelF_ice
import ode_ServicesF_ice
import ode_System_ice
import ode_Collections_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

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

if 'IContainer' not in _M_ode.api.__dict__:
    _M_ode.api.IContainer = Ice.createTempClass()
    class IContainer(_M_ode.api.ServiceInterface):
        """
        Provides methods for dealing with the core Pojos of Bhojpur ODE.
        Included are:
        Projects, Datasets, Images.
        Read API
        The names of the methods correlate to how the function operates:
        load: start at container objects and work down toward the
        leaves, returning hierarchy (Project-&gt;Dataset-&gt;Image
        find: start at leaf objects and work up to containers,
        returning hierarchy
        get: retrieves only leaves in the hierarchy (currently
        only Images)
        Options Mechanism
        The options are used to add some constraints to the generic method
        e.g. load hierarchy trees images for a given user. This
        mechanism should give us enough flexibility to extend the API if
        necessary, e.g. in some cases we might want to retrieve the images
        with or without annotations
        Most methods take such an options map which is built
        on the client-side using the ode.sys.Parameters class. The
        currently supported options are:
        annotator(Integer): If key exists but value null,
        annotations are retrieved for all objects in the hierarchy where
        they exist; if a valid experimenterID, annotations are only
        retrieved for that user. May not be used
        be all methods. Default: all annotations
        leaves(Boolean): if FALSE omits images from the returned
        hierarchy. May not be used by all methods. Default: true
        experimenter(Integer): enables filtering on a
        per-experimenter basis. This option has a method-specific (and
        possibly context-specific) meaning. Please see the individual
        methods.
        group(Integer): enables filtering on a per-group basis.
        The experimenter value is ignored if present and instead a
        similar filtering is done using all experimenters in the
        given group.
        Write API
        As outlined in TODO, the semantics of the Bhojpur ODE write API are based
        on three rules:
        IObject-valued fields for which isLoaded() returns
        false are assumed filtered
        Collection-valued fields that are null are assumed filtered
        Collection-valued fields for which
        getDetails().isFiltered(String collectionName) returns
        true are assumed filtered. TODO: should we accept isFiltered for
        all fields?
        In each of these cases, the server will reload that given field
        before attempting to save the graph.
        For all write calls, the options map (see below) must contain the
        userId and the userGroupId for the newly created objects. TODO
        umask.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IContainer:
                raise RuntimeError('ode.api.IContainer is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IContainer', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IContainer'

        def ice_staticId():
            return '::ode::api::IContainer'
        ice_staticId = staticmethod(ice_staticId)

        def loadContainerHierarchy_async(self, _cb, rootType, rootIds, options, current=None):
            """
            Retrieves hierarchy trees rooted by a given node (unless
            orphan is specified -- See below)
            This method also retrieves the Experimenters linked to the
            objects in the tree. Similarly, all Images will be linked
            to their Pixel objects if included.
            Note that objects are never duplicated. For example, if an
            Experimenter owns all the objects in the retrieved tree,
            then those objects will be linked to the same
            instance of ode.model.Experimenter. Or if an
            Image is contained in more than one Dataset in the
            retrieved tree, then all enclosing
            ode.model.Dataset objects will point
            to the same ode.model.Image object. And
            so on.
            Arguments:
            _cb -- The asynchronous callback object.
            rootType -- The type of the root node. Can be ode.model.Project, ode.model.Dataset, ode.model.Screen or ode.model.Plate. Cannot be null.
            rootIds -- The ids of the root nodes. Can be null if an Experimenter is specified in options, otherwise an Exception is thrown to prevent all images in the entire database from being downloaded.
            options -- Parameters as above. annotator, leaves, orphan, acquisition data used. acquisition data is only relevant for images and taken into account if the images are loaded. If rootNodeIds==null, experimenter|group must be set and filtering will be applied at the Class-level; e.g. to retrieve a user's Projects, or user's Datasets. If rootNodeIds!=null, the result will be filtered by the experimenter|group at the Image and intermediate levels if available. Due to the amount of data potentially linked a Screen/Plate, the leaves option is not taken into account when the root node is a ode.model.Screen. orphan implies that objects which are not contained in an object of rootNodeType should also be returned.
            current -- The Current object for the invocation.
            """
            pass

        def findContainerHierarchies_async(self, _cb, rootType, imageIds, options, current=None):
            """
            Retrieves hierarchy trees in various hierarchies that
            contain the specified Images.
            This method will look for all the containers containing the
            specified Images and then for all containers containing
            those containers and on up the container hierarchy.
            This method returns a Set with all root nodes
            that were
            found. Every root node is linked to the found objects and
            so on until the leaf nodes, which are
            ode.model.Image objects. Note that the type of any
            root node in the returned set can be the given
            rootNodeType, any of its containees or an
            ode.model.Image.
            For example, say that you pass in the ids of six Images:
            i1, i2, i3, i4, i5, i6.
            If the P/D/I hierarchy in the DB looks like this:
            |                  __p1__                     
            |                 /      \                    
            |               _d1_    _d2_      d3          
            |              /    \  /    \     |           
            |             i1     i2     i3    i4    i5  i6
            Then the returned set will contain
            p1, d3, i5, i6. All objects will be properly
            linked up.
            Finally, this method will only retrieve the nodes
            that are connected in a tree to the specified leaf image
            nodes. Back to the previous example, if d1
            contained image img500, then the returned
            object would not contain img500. In a
            similar way, if p1 contained
            ds300 and this dataset weren't linked to any of
            the i1, i2, i3, i4, i5, i6  images, then
            ds300 would not be part of the returned
            tree rooted by p1.
            Arguments:
            _cb -- The asynchronous callback object.
            rootType -- top-most type which will be searched for Can be ode.model.Project. Not null.
            imageIds -- Contains the ids of the Images that sit at the bottom of the trees. Not null.
            options -- Parameters as above. annotator used. experimenter|group may be applied at the top-level only or at each level in the hierarchy, but will not apply to the leaf (Image) level.
            current -- The Current object for the invocation.
            """
            pass

        def getImages_async(self, _cb, rootType, rootIds, options, current=None):
            """
            Retrieve a user's (or all users') images within any given
            container. For example, all images in project, applying
            temporal filtering or pagination.
            Arguments:
            _cb -- The asynchronous callback object.
            rootType -- A Class which will have its hierarchy searched for Images. Not null.
            rootIds -- A set of ids of type rootNodeType Not null.
            options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level. OPTIONS: - startTime and/or endTime should be Timestamp.valueOf("YYYY-MM-DD hh:mm:ss.ms"); limit and offset are applied at the Image-level. That is, calling with Dataset.class, limit == 10 and offset == 0 will first perform one query to get an effective set of rootNodeIds, then getImages will be called with an effective rootNodeType of Image.class and the new ids. acquisition data is only relevant for images.
            current -- The Current object for the invocation.
            """
            pass

        def getUserImages_async(self, _cb, options, current=None):
            """
            Retrieves a user's images.
            Arguments:
            _cb -- The asynchronous callback object.
            options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level and must be present.
            current -- The Current object for the invocation.
            """
            pass

        def getImagesByOptions_async(self, _cb, options, current=None):
            """
            Retrieves images by options.
            Arguments:
            _cb -- The asynchronous callback object.
            options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level and must be present. OPTIONS: - startTime and/or endTime should be Timestamp.valueOf("YYYY-MM-DD hh:mm:ss.ms"). acquisition data is only relevant for images.
            current -- The Current object for the invocation.
            """
            pass

        def getImagesBySplitFilesets_async(self, _cb, included, options, current=None):
            """
            Given a list of IDs of certain entity types, calculates
            which filesets are split such that a non-empty proper
            subset of their images are referenced, directly or
            indirectly, as being included. The return value lists both
            the fileset IDs and the image IDs in ascending order,
            the image ID lists separated by if they were included.
            return type may be changed.
            Arguments:
            _cb -- The asynchronous callback object.
            included -- the entities included
            options -- parameters, presently ignored
            current -- The Current object for the invocation.
            """
            pass

        def getCollectionCount_async(self, _cb, type, property, ids, options, current=None):
            """
            Counts the number of members in a collection for a given
            object. For example, if you wanted to retrieve the number
            of Images contained in a Dataset you would pass TODO.
            Arguments:
            _cb -- The asynchronous callback object.
            type -- The fully-qualified classname of the object to be tested
            property -- Name of the property on that class, omitting getters and setters.
            ids -- Set of Longs, the ids of the objects to test
            options -- Parameters. Unused.
            current -- The Current object for the invocation.
            """
            pass

        def retrieveCollection_async(self, _cb, obj, collectionName, options, current=None):
            """
            Retrieves a collection with all members initialized
            (loaded). This is useful when a collection has been
            nulled in a previous query.
            Arguments:
            _cb -- The asynchronous callback object.
            obj -- Can be unloaded.
            collectionName -- public static final String from the IObject.class
            options -- Parameters. Unused.
            current -- The Current object for the invocation.
            """
            pass

        def createDataObject_async(self, _cb, obj, options, current=None):
            """
            Creates the specified data object.
            A placeholder parent object is created if the data object
            is to be put in a collection.
            For example, if the object is a Dataset, we
            first create a Project as parent then we set
            the Dataset parent as follows: 
            //pseudo-code TODO
            Project p = new Project(id,false);
            dataset.addProject(p);
            then for each parent relationship a DataObject
            ode.model.ILink is created.
            Arguments:
            _cb -- The asynchronous callback object.
            obj -- IObject. Supported: Project, Dataset, Annotation, Group, Experimenter. Not null.
            options -- Parameters as above.
            current -- The Current object for the invocation.
            """
            pass

        def createDataObjects_async(self, _cb, dataObjects, options, current=None):
            """
            Convenience method to save network calls. Loops over the
            array of IObjects calling {@code createDataObject}.
            Arguments:
            _cb -- The asynchronous callback object.
            dataObjects -- Array of Bhojpur ODE IObjects
            options -- Parameters as above.
            current -- The Current object for the invocation.
            """
            pass

        def unlink_async(self, _cb, links, options, current=None):
            """
            Removes links between OdeDataObjects e.g Project-Dataset,
            Dataset-Image
            Note that the objects themselves aren't deleted, only the
            Link objects.
            Arguments:
            _cb -- The asynchronous callback object.
            links -- Not null.
            options -- Parameters as above.
            current -- The Current object for the invocation.
            """
            pass

        def link_async(self, _cb, links, options, current=None):
            """
            Convenience method for creating links. Functionality also
            available from {@code createDataObject}
            Arguments:
            _cb -- The asynchronous callback object.
            links -- Array of links to be created.
            options -- Parameters as above.
            current -- The Current object for the invocation.
            """
            pass

        def updateDataObject_async(self, _cb, obj, options, current=None):
            """
            Updates a data object.
            To link or unlink objects to the specified object, we
            should call the methods link or unlink. TODO Or do we use
            for example dataset.setProjects(set of projects) to add.
            Link has to be set as follows dataset&rarr;project and
            project&rarr;dataset.
            Alternatively, you can make sure that the collection is
            exactly how it should be in the database. If you
            can't guarantee this, it's best to send all your
            collections back as null
            Arguments:
            _cb -- The asynchronous callback object.
            obj -- Pojos-based IObject. Supported: Project, Dataset, Annotation, Group, Experimenter.
            options -- Parameters as above.
            current -- The Current object for the invocation.
            """
            pass

        def updateDataObjects_async(self, _cb, objs, options, current=None):
            """
            Convenience method to save network calls. Loops over the
            array of IObjects calling {@code updateDataObject}.
            Arguments:
            _cb -- The asynchronous callback object.
            objs -- 
            options -- Parameters as above.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IContainer)

        __repr__ = __str__

    _M_ode.api.IContainerPrx = Ice.createTempClass()
    class IContainerPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Retrieves hierarchy trees rooted by a given node (unless
        orphan is specified -- See below)
        This method also retrieves the Experimenters linked to the
        objects in the tree. Similarly, all Images will be linked
        to their Pixel objects if included.
        Note that objects are never duplicated. For example, if an
        Experimenter owns all the objects in the retrieved tree,
        then those objects will be linked to the same
        instance of ode.model.Experimenter. Or if an
        Image is contained in more than one Dataset in the
        retrieved tree, then all enclosing
        ode.model.Dataset objects will point
        to the same ode.model.Image object. And
        so on.
        Arguments:
        rootType -- The type of the root node. Can be ode.model.Project, ode.model.Dataset, ode.model.Screen or ode.model.Plate. Cannot be null.
        rootIds -- The ids of the root nodes. Can be null if an Experimenter is specified in options, otherwise an Exception is thrown to prevent all images in the entire database from being downloaded.
        options -- Parameters as above. annotator, leaves, orphan, acquisition data used. acquisition data is only relevant for images and taken into account if the images are loaded. If rootNodeIds==null, experimenter|group must be set and filtering will be applied at the Class-level; e.g. to retrieve a user's Projects, or user's Datasets. If rootNodeIds!=null, the result will be filtered by the experimenter|group at the Image and intermediate levels if available. Due to the amount of data potentially linked a Screen/Plate, the leaves option is not taken into account when the root node is a ode.model.Screen. orphan implies that objects which are not contained in an object of rootNodeType should also be returned.
        _ctx -- The request context for the invocation.
        Returns: a set of hierarchy trees. The requested node as root and all of its descendants. The type of the returned value will be rootNodeType, unless orphan is specified in which case objects of type rootNodeType and below may be returned.
        """
        def loadContainerHierarchy(self, rootType, rootIds, options, _ctx=None):
            return _M_ode.api.IContainer._op_loadContainerHierarchy.invoke(self, ((rootType, rootIds, options), _ctx))

        """
        Retrieves hierarchy trees rooted by a given node (unless
        orphan is specified -- See below)
        This method also retrieves the Experimenters linked to the
        objects in the tree. Similarly, all Images will be linked
        to their Pixel objects if included.
        Note that objects are never duplicated. For example, if an
        Experimenter owns all the objects in the retrieved tree,
        then those objects will be linked to the same
        instance of ode.model.Experimenter. Or if an
        Image is contained in more than one Dataset in the
        retrieved tree, then all enclosing
        ode.model.Dataset objects will point
        to the same ode.model.Image object. And
        so on.
        Arguments:
        rootType -- The type of the root node. Can be ode.model.Project, ode.model.Dataset, ode.model.Screen or ode.model.Plate. Cannot be null.
        rootIds -- The ids of the root nodes. Can be null if an Experimenter is specified in options, otherwise an Exception is thrown to prevent all images in the entire database from being downloaded.
        options -- Parameters as above. annotator, leaves, orphan, acquisition data used. acquisition data is only relevant for images and taken into account if the images are loaded. If rootNodeIds==null, experimenter|group must be set and filtering will be applied at the Class-level; e.g. to retrieve a user's Projects, or user's Datasets. If rootNodeIds!=null, the result will be filtered by the experimenter|group at the Image and intermediate levels if available. Due to the amount of data potentially linked a Screen/Plate, the leaves option is not taken into account when the root node is a ode.model.Screen. orphan implies that objects which are not contained in an object of rootNodeType should also be returned.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadContainerHierarchy(self, rootType, rootIds, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_loadContainerHierarchy.begin(self, ((rootType, rootIds, options), _response, _ex, _sent, _ctx))

        """
        Retrieves hierarchy trees rooted by a given node (unless
        orphan is specified -- See below)
        This method also retrieves the Experimenters linked to the
        objects in the tree. Similarly, all Images will be linked
        to their Pixel objects if included.
        Note that objects are never duplicated. For example, if an
        Experimenter owns all the objects in the retrieved tree,
        then those objects will be linked to the same
        instance of ode.model.Experimenter. Or if an
        Image is contained in more than one Dataset in the
        retrieved tree, then all enclosing
        ode.model.Dataset objects will point
        to the same ode.model.Image object. And
        so on.
        Arguments:
        rootType -- The type of the root node. Can be ode.model.Project, ode.model.Dataset, ode.model.Screen or ode.model.Plate. Cannot be null.
        rootIds -- The ids of the root nodes. Can be null if an Experimenter is specified in options, otherwise an Exception is thrown to prevent all images in the entire database from being downloaded.
        options -- Parameters as above. annotator, leaves, orphan, acquisition data used. acquisition data is only relevant for images and taken into account if the images are loaded. If rootNodeIds==null, experimenter|group must be set and filtering will be applied at the Class-level; e.g. to retrieve a user's Projects, or user's Datasets. If rootNodeIds!=null, the result will be filtered by the experimenter|group at the Image and intermediate levels if available. Due to the amount of data potentially linked a Screen/Plate, the leaves option is not taken into account when the root node is a ode.model.Screen. orphan implies that objects which are not contained in an object of rootNodeType should also be returned.
        Returns: a set of hierarchy trees. The requested node as root and all of its descendants. The type of the returned value will be rootNodeType, unless orphan is specified in which case objects of type rootNodeType and below may be returned.
        """
        def end_loadContainerHierarchy(self, _r):
            return _M_ode.api.IContainer._op_loadContainerHierarchy.end(self, _r)

        """
        Retrieves hierarchy trees in various hierarchies that
        contain the specified Images.
        This method will look for all the containers containing the
        specified Images and then for all containers containing
        those containers and on up the container hierarchy.
        This method returns a Set with all root nodes
        that were
        found. Every root node is linked to the found objects and
        so on until the leaf nodes, which are
        ode.model.Image objects. Note that the type of any
        root node in the returned set can be the given
        rootNodeType, any of its containees or an
        ode.model.Image.
        For example, say that you pass in the ids of six Images:
        i1, i2, i3, i4, i5, i6.
        If the P/D/I hierarchy in the DB looks like this:
        |                  __p1__                     
        |                 /      \                    
        |               _d1_    _d2_      d3          
        |              /    \  /    \     |           
        |             i1     i2     i3    i4    i5  i6
        Then the returned set will contain
        p1, d3, i5, i6. All objects will be properly
        linked up.
        Finally, this method will only retrieve the nodes
        that are connected in a tree to the specified leaf image
        nodes. Back to the previous example, if d1
        contained image img500, then the returned
        object would not contain img500. In a
        similar way, if p1 contained
        ds300 and this dataset weren't linked to any of
        the i1, i2, i3, i4, i5, i6  images, then
        ds300 would not be part of the returned
        tree rooted by p1.
        Arguments:
        rootType -- top-most type which will be searched for Can be ode.model.Project. Not null.
        imageIds -- Contains the ids of the Images that sit at the bottom of the trees. Not null.
        options -- Parameters as above. annotator used. experimenter|group may be applied at the top-level only or at each level in the hierarchy, but will not apply to the leaf (Image) level.
        _ctx -- The request context for the invocation.
        Returns: A Set with all root nodes that were found.
        """
        def findContainerHierarchies(self, rootType, imageIds, options, _ctx=None):
            return _M_ode.api.IContainer._op_findContainerHierarchies.invoke(self, ((rootType, imageIds, options), _ctx))

        """
        Retrieves hierarchy trees in various hierarchies that
        contain the specified Images.
        This method will look for all the containers containing the
        specified Images and then for all containers containing
        those containers and on up the container hierarchy.
        This method returns a Set with all root nodes
        that were
        found. Every root node is linked to the found objects and
        so on until the leaf nodes, which are
        ode.model.Image objects. Note that the type of any
        root node in the returned set can be the given
        rootNodeType, any of its containees or an
        ode.model.Image.
        For example, say that you pass in the ids of six Images:
        i1, i2, i3, i4, i5, i6.
        If the P/D/I hierarchy in the DB looks like this:
        |                  __p1__                     
        |                 /      \                    
        |               _d1_    _d2_      d3          
        |              /    \  /    \     |           
        |             i1     i2     i3    i4    i5  i6
        Then the returned set will contain
        p1, d3, i5, i6. All objects will be properly
        linked up.
        Finally, this method will only retrieve the nodes
        that are connected in a tree to the specified leaf image
        nodes. Back to the previous example, if d1
        contained image img500, then the returned
        object would not contain img500. In a
        similar way, if p1 contained
        ds300 and this dataset weren't linked to any of
        the i1, i2, i3, i4, i5, i6  images, then
        ds300 would not be part of the returned
        tree rooted by p1.
        Arguments:
        rootType -- top-most type which will be searched for Can be ode.model.Project. Not null.
        imageIds -- Contains the ids of the Images that sit at the bottom of the trees. Not null.
        options -- Parameters as above. annotator used. experimenter|group may be applied at the top-level only or at each level in the hierarchy, but will not apply to the leaf (Image) level.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findContainerHierarchies(self, rootType, imageIds, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_findContainerHierarchies.begin(self, ((rootType, imageIds, options), _response, _ex, _sent, _ctx))

        """
        Retrieves hierarchy trees in various hierarchies that
        contain the specified Images.
        This method will look for all the containers containing the
        specified Images and then for all containers containing
        those containers and on up the container hierarchy.
        This method returns a Set with all root nodes
        that were
        found. Every root node is linked to the found objects and
        so on until the leaf nodes, which are
        ode.model.Image objects. Note that the type of any
        root node in the returned set can be the given
        rootNodeType, any of its containees or an
        ode.model.Image.
        For example, say that you pass in the ids of six Images:
        i1, i2, i3, i4, i5, i6.
        If the P/D/I hierarchy in the DB looks like this:
        |                  __p1__                     
        |                 /      \                    
        |               _d1_    _d2_      d3          
        |              /    \  /    \     |           
        |             i1     i2     i3    i4    i5  i6
        Then the returned set will contain
        p1, d3, i5, i6. All objects will be properly
        linked up.
        Finally, this method will only retrieve the nodes
        that are connected in a tree to the specified leaf image
        nodes. Back to the previous example, if d1
        contained image img500, then the returned
        object would not contain img500. In a
        similar way, if p1 contained
        ds300 and this dataset weren't linked to any of
        the i1, i2, i3, i4, i5, i6  images, then
        ds300 would not be part of the returned
        tree rooted by p1.
        Arguments:
        rootType -- top-most type which will be searched for Can be ode.model.Project. Not null.
        imageIds -- Contains the ids of the Images that sit at the bottom of the trees. Not null.
        options -- Parameters as above. annotator used. experimenter|group may be applied at the top-level only or at each level in the hierarchy, but will not apply to the leaf (Image) level.
        Returns: A Set with all root nodes that were found.
        """
        def end_findContainerHierarchies(self, _r):
            return _M_ode.api.IContainer._op_findContainerHierarchies.end(self, _r)

        """
        Retrieve a user's (or all users') images within any given
        container. For example, all images in project, applying
        temporal filtering or pagination.
        Arguments:
        rootType -- A Class which will have its hierarchy searched for Images. Not null.
        rootIds -- A set of ids of type rootNodeType Not null.
        options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level. OPTIONS: - startTime and/or endTime should be Timestamp.valueOf("YYYY-MM-DD hh:mm:ss.ms"); limit and offset are applied at the Image-level. That is, calling with Dataset.class, limit == 10 and offset == 0 will first perform one query to get an effective set of rootNodeIds, then getImages will be called with an effective rootNodeType of Image.class and the new ids. acquisition data is only relevant for images.
        _ctx -- The request context for the invocation.
        Returns: A set of images.
        """
        def getImages(self, rootType, rootIds, options, _ctx=None):
            return _M_ode.api.IContainer._op_getImages.invoke(self, ((rootType, rootIds, options), _ctx))

        """
        Retrieve a user's (or all users') images within any given
        container. For example, all images in project, applying
        temporal filtering or pagination.
        Arguments:
        rootType -- A Class which will have its hierarchy searched for Images. Not null.
        rootIds -- A set of ids of type rootNodeType Not null.
        options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level. OPTIONS: - startTime and/or endTime should be Timestamp.valueOf("YYYY-MM-DD hh:mm:ss.ms"); limit and offset are applied at the Image-level. That is, calling with Dataset.class, limit == 10 and offset == 0 will first perform one query to get an effective set of rootNodeIds, then getImages will be called with an effective rootNodeType of Image.class and the new ids. acquisition data is only relevant for images.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getImages(self, rootType, rootIds, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_getImages.begin(self, ((rootType, rootIds, options), _response, _ex, _sent, _ctx))

        """
        Retrieve a user's (or all users') images within any given
        container. For example, all images in project, applying
        temporal filtering or pagination.
        Arguments:
        rootType -- A Class which will have its hierarchy searched for Images. Not null.
        rootIds -- A set of ids of type rootNodeType Not null.
        options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level. OPTIONS: - startTime and/or endTime should be Timestamp.valueOf("YYYY-MM-DD hh:mm:ss.ms"); limit and offset are applied at the Image-level. That is, calling with Dataset.class, limit == 10 and offset == 0 will first perform one query to get an effective set of rootNodeIds, then getImages will be called with an effective rootNodeType of Image.class and the new ids. acquisition data is only relevant for images.
        Returns: A set of images.
        """
        def end_getImages(self, _r):
            return _M_ode.api.IContainer._op_getImages.end(self, _r)

        """
        Retrieves a user's images.
        Arguments:
        options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level and must be present.
        _ctx -- The request context for the invocation.
        Returns: A set of images.
        """
        def getUserImages(self, options, _ctx=None):
            return _M_ode.api.IContainer._op_getUserImages.invoke(self, ((options, ), _ctx))

        """
        Retrieves a user's images.
        Arguments:
        options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level and must be present.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getUserImages(self, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_getUserImages.begin(self, ((options, ), _response, _ex, _sent, _ctx))

        """
        Retrieves a user's images.
        Arguments:
        options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level and must be present.
        Returns: A set of images.
        """
        def end_getUserImages(self, _r):
            return _M_ode.api.IContainer._op_getUserImages.end(self, _r)

        """
        Retrieves images by options.
        Arguments:
        options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level and must be present. OPTIONS: - startTime and/or endTime should be Timestamp.valueOf("YYYY-MM-DD hh:mm:ss.ms"). acquisition data is only relevant for images.
        _ctx -- The request context for the invocation.
        Returns: A set of images.
        """
        def getImagesByOptions(self, options, _ctx=None):
            return _M_ode.api.IContainer._op_getImagesByOptions.invoke(self, ((options, ), _ctx))

        """
        Retrieves images by options.
        Arguments:
        options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level and must be present. OPTIONS: - startTime and/or endTime should be Timestamp.valueOf("YYYY-MM-DD hh:mm:ss.ms"). acquisition data is only relevant for images.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getImagesByOptions(self, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_getImagesByOptions.begin(self, ((options, ), _response, _ex, _sent, _ctx))

        """
        Retrieves images by options.
        Arguments:
        options -- Parameters as above. No notion of leaves. experimenter|group apply at the Image level and must be present. OPTIONS: - startTime and/or endTime should be Timestamp.valueOf("YYYY-MM-DD hh:mm:ss.ms"). acquisition data is only relevant for images.
        Returns: A set of images.
        """
        def end_getImagesByOptions(self, _r):
            return _M_ode.api.IContainer._op_getImagesByOptions.end(self, _r)

        """
        Given a list of IDs of certain entity types, calculates
        which filesets are split such that a non-empty proper
        subset of their images are referenced, directly or
        indirectly, as being included. The return value lists both
        the fileset IDs and the image IDs in ascending order,
        the image ID lists separated by if they were included.
        Arguments:
        included -- the entities included
        options -- parameters, presently ignored
        _ctx -- The request context for the invocation.
        Returns: the partially included filesets
        """
        def getImagesBySplitFilesets(self, included, options, _ctx=None):
            return _M_ode.api.IContainer._op_getImagesBySplitFilesets.invoke(self, ((included, options), _ctx))

        """
        Given a list of IDs of certain entity types, calculates
        which filesets are split such that a non-empty proper
        subset of their images are referenced, directly or
        indirectly, as being included. The return value lists both
        the fileset IDs and the image IDs in ascending order,
        the image ID lists separated by if they were included.
        Arguments:
        included -- the entities included
        options -- parameters, presently ignored
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getImagesBySplitFilesets(self, included, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_getImagesBySplitFilesets.begin(self, ((included, options), _response, _ex, _sent, _ctx))

        """
        Given a list of IDs of certain entity types, calculates
        which filesets are split such that a non-empty proper
        subset of their images are referenced, directly or
        indirectly, as being included. The return value lists both
        the fileset IDs and the image IDs in ascending order,
        the image ID lists separated by if they were included.
        Arguments:
        included -- the entities included
        options -- parameters, presently ignored
        Returns: the partially included filesets
        """
        def end_getImagesBySplitFilesets(self, _r):
            return _M_ode.api.IContainer._op_getImagesBySplitFilesets.end(self, _r)

        """
        Counts the number of members in a collection for a given
        object. For example, if you wanted to retrieve the number
        of Images contained in a Dataset you would pass TODO.
        Arguments:
        type -- The fully-qualified classname of the object to be tested
        property -- Name of the property on that class, omitting getters and setters.
        ids -- Set of Longs, the ids of the objects to test
        options -- Parameters. Unused.
        _ctx -- The request context for the invocation.
        Returns: A map from id integer to count integer
        """
        def getCollectionCount(self, type, property, ids, options, _ctx=None):
            return _M_ode.api.IContainer._op_getCollectionCount.invoke(self, ((type, property, ids, options), _ctx))

        """
        Counts the number of members in a collection for a given
        object. For example, if you wanted to retrieve the number
        of Images contained in a Dataset you would pass TODO.
        Arguments:
        type -- The fully-qualified classname of the object to be tested
        property -- Name of the property on that class, omitting getters and setters.
        ids -- Set of Longs, the ids of the objects to test
        options -- Parameters. Unused.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getCollectionCount(self, type, property, ids, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_getCollectionCount.begin(self, ((type, property, ids, options), _response, _ex, _sent, _ctx))

        """
        Counts the number of members in a collection for a given
        object. For example, if you wanted to retrieve the number
        of Images contained in a Dataset you would pass TODO.
        Arguments:
        type -- The fully-qualified classname of the object to be tested
        property -- Name of the property on that class, omitting getters and setters.
        ids -- Set of Longs, the ids of the objects to test
        options -- Parameters. Unused.
        Returns: A map from id integer to count integer
        """
        def end_getCollectionCount(self, _r):
            return _M_ode.api.IContainer._op_getCollectionCount.end(self, _r)

        """
        Retrieves a collection with all members initialized
        (loaded). This is useful when a collection has been
        nulled in a previous query.
        Arguments:
        obj -- Can be unloaded.
        collectionName -- public static final String from the IObject.class
        options -- Parameters. Unused.
        _ctx -- The request context for the invocation.
        Returns: An initialized collection.
        """
        def retrieveCollection(self, obj, collectionName, options, _ctx=None):
            return _M_ode.api.IContainer._op_retrieveCollection.invoke(self, ((obj, collectionName, options), _ctx))

        """
        Retrieves a collection with all members initialized
        (loaded). This is useful when a collection has been
        nulled in a previous query.
        Arguments:
        obj -- Can be unloaded.
        collectionName -- public static final String from the IObject.class
        options -- Parameters. Unused.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_retrieveCollection(self, obj, collectionName, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_retrieveCollection.begin(self, ((obj, collectionName, options), _response, _ex, _sent, _ctx))

        """
        Retrieves a collection with all members initialized
        (loaded). This is useful when a collection has been
        nulled in a previous query.
        Arguments:
        obj -- Can be unloaded.
        collectionName -- public static final String from the IObject.class
        options -- Parameters. Unused.
        Returns: An initialized collection.
        """
        def end_retrieveCollection(self, _r):
            return _M_ode.api.IContainer._op_retrieveCollection.end(self, _r)

        """
        Creates the specified data object.
        A placeholder parent object is created if the data object
        is to be put in a collection.
        For example, if the object is a Dataset, we
        first create a Project as parent then we set
        the Dataset parent as follows: 
        //pseudo-code TODO
        Project p = new Project(id,false);
        dataset.addProject(p);
        then for each parent relationship a DataObject
        ode.model.ILink is created.
        Arguments:
        obj -- IObject. Supported: Project, Dataset, Annotation, Group, Experimenter. Not null.
        options -- Parameters as above.
        _ctx -- The request context for the invocation.
        Returns: the created object
        """
        def createDataObject(self, obj, options, _ctx=None):
            return _M_ode.api.IContainer._op_createDataObject.invoke(self, ((obj, options), _ctx))

        """
        Creates the specified data object.
        A placeholder parent object is created if the data object
        is to be put in a collection.
        For example, if the object is a Dataset, we
        first create a Project as parent then we set
        the Dataset parent as follows: 
        //pseudo-code TODO
        Project p = new Project(id,false);
        dataset.addProject(p);
        then for each parent relationship a DataObject
        ode.model.ILink is created.
        Arguments:
        obj -- IObject. Supported: Project, Dataset, Annotation, Group, Experimenter. Not null.
        options -- Parameters as above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createDataObject(self, obj, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_createDataObject.begin(self, ((obj, options), _response, _ex, _sent, _ctx))

        """
        Creates the specified data object.
        A placeholder parent object is created if the data object
        is to be put in a collection.
        For example, if the object is a Dataset, we
        first create a Project as parent then we set
        the Dataset parent as follows: 
        //pseudo-code TODO
        Project p = new Project(id,false);
        dataset.addProject(p);
        then for each parent relationship a DataObject
        ode.model.ILink is created.
        Arguments:
        obj -- IObject. Supported: Project, Dataset, Annotation, Group, Experimenter. Not null.
        options -- Parameters as above.
        Returns: the created object
        """
        def end_createDataObject(self, _r):
            return _M_ode.api.IContainer._op_createDataObject.end(self, _r)

        """
        Convenience method to save network calls. Loops over the
        array of IObjects calling {@code createDataObject}.
        Arguments:
        dataObjects -- Array of Bhojpur ODE IObjects
        options -- Parameters as above.
        _ctx -- The request context for the invocation.
        """
        def createDataObjects(self, dataObjects, options, _ctx=None):
            return _M_ode.api.IContainer._op_createDataObjects.invoke(self, ((dataObjects, options), _ctx))

        """
        Convenience method to save network calls. Loops over the
        array of IObjects calling {@code createDataObject}.
        Arguments:
        dataObjects -- Array of Bhojpur ODE IObjects
        options -- Parameters as above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createDataObjects(self, dataObjects, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_createDataObjects.begin(self, ((dataObjects, options), _response, _ex, _sent, _ctx))

        """
        Convenience method to save network calls. Loops over the
        array of IObjects calling {@code createDataObject}.
        Arguments:
        dataObjects -- Array of Bhojpur ODE IObjects
        options -- Parameters as above.
        """
        def end_createDataObjects(self, _r):
            return _M_ode.api.IContainer._op_createDataObjects.end(self, _r)

        """
        Removes links between OdeDataObjects e.g Project-Dataset,
        Dataset-Image
        Note that the objects themselves aren't deleted, only the
        Link objects.
        Arguments:
        links -- Not null.
        options -- Parameters as above.
        _ctx -- The request context for the invocation.
        """
        def unlink(self, links, options, _ctx=None):
            return _M_ode.api.IContainer._op_unlink.invoke(self, ((links, options), _ctx))

        """
        Removes links between OdeDataObjects e.g Project-Dataset,
        Dataset-Image
        Note that the objects themselves aren't deleted, only the
        Link objects.
        Arguments:
        links -- Not null.
        options -- Parameters as above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_unlink(self, links, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_unlink.begin(self, ((links, options), _response, _ex, _sent, _ctx))

        """
        Removes links between OdeDataObjects e.g Project-Dataset,
        Dataset-Image
        Note that the objects themselves aren't deleted, only the
        Link objects.
        Arguments:
        links -- Not null.
        options -- Parameters as above.
        """
        def end_unlink(self, _r):
            return _M_ode.api.IContainer._op_unlink.end(self, _r)

        """
        Convenience method for creating links. Functionality also
        available from {@code createDataObject}
        Arguments:
        links -- Array of links to be created.
        options -- Parameters as above.
        _ctx -- The request context for the invocation.
        Returns: the created links
        """
        def link(self, links, options, _ctx=None):
            return _M_ode.api.IContainer._op_link.invoke(self, ((links, options), _ctx))

        """
        Convenience method for creating links. Functionality also
        available from {@code createDataObject}
        Arguments:
        links -- Array of links to be created.
        options -- Parameters as above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_link(self, links, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_link.begin(self, ((links, options), _response, _ex, _sent, _ctx))

        """
        Convenience method for creating links. Functionality also
        available from {@code createDataObject}
        Arguments:
        links -- Array of links to be created.
        options -- Parameters as above.
        Returns: the created links
        """
        def end_link(self, _r):
            return _M_ode.api.IContainer._op_link.end(self, _r)

        """
        Updates a data object.
        To link or unlink objects to the specified object, we
        should call the methods link or unlink. TODO Or do we use
        for example dataset.setProjects(set of projects) to add.
        Link has to be set as follows dataset&rarr;project and
        project&rarr;dataset.
        Alternatively, you can make sure that the collection is
        exactly how it should be in the database. If you
        can't guarantee this, it's best to send all your
        collections back as null
        Arguments:
        obj -- Pojos-based IObject. Supported: Project, Dataset, Annotation, Group, Experimenter.
        options -- Parameters as above.
        _ctx -- The request context for the invocation.
        Returns: created data object
        """
        def updateDataObject(self, obj, options, _ctx=None):
            return _M_ode.api.IContainer._op_updateDataObject.invoke(self, ((obj, options), _ctx))

        """
        Updates a data object.
        To link or unlink objects to the specified object, we
        should call the methods link or unlink. TODO Or do we use
        for example dataset.setProjects(set of projects) to add.
        Link has to be set as follows dataset&rarr;project and
        project&rarr;dataset.
        Alternatively, you can make sure that the collection is
        exactly how it should be in the database. If you
        can't guarantee this, it's best to send all your
        collections back as null
        Arguments:
        obj -- Pojos-based IObject. Supported: Project, Dataset, Annotation, Group, Experimenter.
        options -- Parameters as above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_updateDataObject(self, obj, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_updateDataObject.begin(self, ((obj, options), _response, _ex, _sent, _ctx))

        """
        Updates a data object.
        To link or unlink objects to the specified object, we
        should call the methods link or unlink. TODO Or do we use
        for example dataset.setProjects(set of projects) to add.
        Link has to be set as follows dataset&rarr;project and
        project&rarr;dataset.
        Alternatively, you can make sure that the collection is
        exactly how it should be in the database. If you
        can't guarantee this, it's best to send all your
        collections back as null
        Arguments:
        obj -- Pojos-based IObject. Supported: Project, Dataset, Annotation, Group, Experimenter.
        options -- Parameters as above.
        Returns: created data object
        """
        def end_updateDataObject(self, _r):
            return _M_ode.api.IContainer._op_updateDataObject.end(self, _r)

        """
        Convenience method to save network calls. Loops over the
        array of IObjects calling {@code updateDataObject}.
        Arguments:
        objs -- 
        options -- Parameters as above.
        _ctx -- The request context for the invocation.
        Returns: created data objects.
        """
        def updateDataObjects(self, objs, options, _ctx=None):
            return _M_ode.api.IContainer._op_updateDataObjects.invoke(self, ((objs, options), _ctx))

        """
        Convenience method to save network calls. Loops over the
        array of IObjects calling {@code updateDataObject}.
        Arguments:
        objs -- 
        options -- Parameters as above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_updateDataObjects(self, objs, options, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IContainer._op_updateDataObjects.begin(self, ((objs, options), _response, _ex, _sent, _ctx))

        """
        Convenience method to save network calls. Loops over the
        array of IObjects calling {@code updateDataObject}.
        Arguments:
        objs -- 
        options -- Parameters as above.
        Returns: created data objects.
        """
        def end_updateDataObjects(self, _r):
            return _M_ode.api.IContainer._op_updateDataObjects.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IContainerPrx.ice_checkedCast(proxy, '::ode::api::IContainer', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IContainerPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IContainer'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IContainerPrx = IcePy.defineProxy('::ode::api::IContainer', IContainerPrx)

    _M_ode.api._t_IContainer = IcePy.defineClass('::ode::api::IContainer', IContainer, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IContainer._ice_type = _M_ode.api._t_IContainer

    IContainer._op_loadContainerHierarchy = IcePy.Operation('loadContainerHierarchy', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_findContainerHierarchies = IcePy.Operation('findContainerHierarchies', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_getImages = IcePy.Operation('getImages', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_ImageList, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_getUserImages = IcePy.Operation('getUserImages', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_Parameters, False, 0),), (), ((), _M_ode.api._t_ImageList, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_getImagesByOptions = IcePy.Operation('getImagesByOptions', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_Parameters, False, 0),), (), ((), _M_ode.api._t_ImageList, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_getImagesBySplitFilesets = IcePy.Operation('getImagesBySplitFilesets', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.api._t_StringLongListMap, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_IdBooleanLongListMapMap, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_getCollectionCount = IcePy.Operation('getCollectionCount', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.sys._t_CountMap, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_retrieveCollection = IcePy.Operation('retrieveCollection', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_IObject, False, 0), ((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_createDataObject = IcePy.Operation('createDataObject', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_IObject, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_createDataObjects = IcePy.Operation('createDataObjects', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_unlink = IcePy.Operation('unlink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), None, (_M_ode._t_ServerError,))
    IContainer._op_link = IcePy.Operation('link', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_updateDataObject = IcePy.Operation('updateDataObject', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_IObject, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    IContainer._op_updateDataObjects = IcePy.Operation('updateDataObjects', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0), ((), _M_ode.sys._t_Parameters, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.IContainer = IContainer
    del IContainer

    _M_ode.api.IContainerPrx = IContainerPrx
    del IContainerPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
