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
import ode_Collections_ice
import ode_cmd_API_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.cmd
_M_ode.cmd = Ice.openModule('ode.cmd')

# Start of module ode
__name__ = 'ode'

# Start of module ode.cmd
__name__ = 'ode.cmd'

if 'FindPyramids' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.FindPyramids = Ice.createTempClass()
    class FindPyramids(_M_ode.cmd.Request):
        """
        Requests all pyramids files. A FindPyramidsResponse
        will be returned under normal conditions, otherwise a ERR
        will be returned.
        Members:
        littleEndian -- Retrieves the pyramids with little endian true or false.
        If unset, both will be retrieved.
        importedAfter -- Retrieves the pyramids created after a specified time if set.
        checkEmptyFile -- Retrieves the pyramids of length 0 if true
        limit -- The maximum number of files to find. No limit will be applied
        if set to 0 or to a negative value.
        """
        def __init__(self, littleEndian=None, importedAfter=None, checkEmptyFile=False, limit=0):
            _M_ode.cmd.Request.__init__(self)
            self.littleEndian = littleEndian
            self.importedAfter = importedAfter
            self.checkEmptyFile = checkEmptyFile
            self.limit = limit

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::FindPyramids', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::FindPyramids'

        def ice_staticId():
            return '::ode::cmd::FindPyramids'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_FindPyramids)

        __repr__ = __str__

    _M_ode.cmd.FindPyramidsPrx = Ice.createTempClass()
    class FindPyramidsPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.FindPyramidsPrx.ice_checkedCast(proxy, '::ode::cmd::FindPyramids', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.FindPyramidsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::FindPyramids'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_FindPyramidsPrx = IcePy.defineProxy('::ode::cmd::FindPyramids', FindPyramidsPrx)

    _M_ode.cmd._t_FindPyramids = IcePy.declareClass('::ode::cmd::FindPyramids')

    _M_ode.cmd._t_FindPyramids = IcePy.defineClass('::ode::cmd::FindPyramids', FindPyramids, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('littleEndian', (), _M_ode._t_RBool, False, 0),
        ('importedAfter', (), _M_ode._t_RTime, False, 0),
        ('checkEmptyFile', (), IcePy._t_bool, False, 0),
        ('limit', (), IcePy._t_long, False, 0)
    ))
    FindPyramids._ice_type = _M_ode.cmd._t_FindPyramids

    _M_ode.cmd.FindPyramids = FindPyramids
    del FindPyramids

    _M_ode.cmd.FindPyramidsPrx = FindPyramidsPrx
    del FindPyramidsPrx

if 'FindPyramidsResponse' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.FindPyramidsResponse = Ice.createTempClass()
    class FindPyramidsResponse(_M_ode.cmd.OK):
        """
        Returns the image Ids corresponding to the pyramid files.
        A FindPyramidsResponse
        will be returned under normal conditions, otherwise a ERR
        will be returned.
        Members:
        pyramidFiles -- The image IDs corresponding to the pyramid
        """
        def __init__(self, pyramidFiles=None):
            _M_ode.cmd.OK.__init__(self)
            self.pyramidFiles = pyramidFiles

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::FindPyramidsResponse', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::FindPyramidsResponse'

        def ice_staticId():
            return '::ode::cmd::FindPyramidsResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_FindPyramidsResponse)

        __repr__ = __str__

    _M_ode.cmd.FindPyramidsResponsePrx = Ice.createTempClass()
    class FindPyramidsResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.FindPyramidsResponsePrx.ice_checkedCast(proxy, '::ode::cmd::FindPyramidsResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.FindPyramidsResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::FindPyramidsResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_FindPyramidsResponsePrx = IcePy.defineProxy('::ode::cmd::FindPyramidsResponse', FindPyramidsResponsePrx)

    _M_ode.cmd._t_FindPyramidsResponse = IcePy.defineClass('::ode::cmd::FindPyramidsResponse', FindPyramidsResponse, -1, (), False, False, _M_ode.cmd._t_OK, (), (('pyramidFiles', (), _M_ode.api._t_LongList, False, 0),))
    FindPyramidsResponse._ice_type = _M_ode.cmd._t_FindPyramidsResponse

    _M_ode.cmd.FindPyramidsResponse = FindPyramidsResponse
    del FindPyramidsResponse

    _M_ode.cmd.FindPyramidsResponsePrx = FindPyramidsResponsePrx
    del FindPyramidsResponsePrx

if 'OriginalMetadataRequest' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.OriginalMetadataRequest = Ice.createTempClass()
    class OriginalMetadataRequest(_M_ode.cmd.Request):
        """
        Requests the file metadata to be loaded for a given
        image. This should handle both the pre-FS metadata
        in file annotations as well as loading the metadata
        directly from the FS files. A OriginalMetadataResponse
        will be returned under normal conditions, otherwise a ERR
        will be returned.
        """
        def __init__(self, imageId=0):
            _M_ode.cmd.Request.__init__(self)
            self.imageId = imageId

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::OriginalMetadataRequest', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::OriginalMetadataRequest'

        def ice_staticId():
            return '::ode::cmd::OriginalMetadataRequest'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_OriginalMetadataRequest)

        __repr__ = __str__

    _M_ode.cmd.OriginalMetadataRequestPrx = Ice.createTempClass()
    class OriginalMetadataRequestPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.OriginalMetadataRequestPrx.ice_checkedCast(proxy, '::ode::cmd::OriginalMetadataRequest', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.OriginalMetadataRequestPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::OriginalMetadataRequest'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_OriginalMetadataRequestPrx = IcePy.defineProxy('::ode::cmd::OriginalMetadataRequest', OriginalMetadataRequestPrx)

    _M_ode.cmd._t_OriginalMetadataRequest = IcePy.defineClass('::ode::cmd::OriginalMetadataRequest', OriginalMetadataRequest, -1, (), False, False, _M_ode.cmd._t_Request, (), (('imageId', (), IcePy._t_long, False, 0),))
    OriginalMetadataRequest._ice_type = _M_ode.cmd._t_OriginalMetadataRequest

    _M_ode.cmd.OriginalMetadataRequest = OriginalMetadataRequest
    del OriginalMetadataRequest

    _M_ode.cmd.OriginalMetadataRequestPrx = OriginalMetadataRequestPrx
    del OriginalMetadataRequestPrx

if 'OriginalMetadataResponse' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.OriginalMetadataResponse = Ice.createTempClass()
    class OriginalMetadataResponse(_M_ode.cmd.OK):
        """
        Successful response for OriginalMetadataRequest. Contains
        both the global and the series metadata as maps. Only one
        of {@code filesetId} or {@code filesetAnnotationId} will be set.
        Pre-FS images will have {@code filesetAnnotationId} set; otherwise
        {@code filesetId} will be set.
        Members:
        filesetId -- Set to the id of the ode.model.Fileset that this
        ode.model.Image contained in if one exists.
        fileAnnotationId -- Set to the id of the ode.model.FileAnnotation
        linked to this ode.model.Image if one exists.
        globalMetadata -- Metadata which applies to the entire
        ode.model.Fileset
        seriesMetadata -- Metadata specific to the series id of this
        ode.model.Image.
        In the ode.model.Fileset that this
        ode.model.Image is contained in, there may be a large
        number of other images, but the series metadata applies only to
        this specific one.
        """
        def __init__(self, filesetId=None, fileAnnotationId=None, globalMetadata=None, seriesMetadata=None):
            _M_ode.cmd.OK.__init__(self)
            self.filesetId = filesetId
            self.fileAnnotationId = fileAnnotationId
            self.globalMetadata = globalMetadata
            self.seriesMetadata = seriesMetadata

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::OK', '::ode::cmd::OriginalMetadataResponse', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::OriginalMetadataResponse'

        def ice_staticId():
            return '::ode::cmd::OriginalMetadataResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_OriginalMetadataResponse)

        __repr__ = __str__

    _M_ode.cmd.OriginalMetadataResponsePrx = Ice.createTempClass()
    class OriginalMetadataResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.OriginalMetadataResponsePrx.ice_checkedCast(proxy, '::ode::cmd::OriginalMetadataResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.OriginalMetadataResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::OriginalMetadataResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_OriginalMetadataResponsePrx = IcePy.defineProxy('::ode::cmd::OriginalMetadataResponse', OriginalMetadataResponsePrx)

    _M_ode.cmd._t_OriginalMetadataResponse = IcePy.declareClass('::ode::cmd::OriginalMetadataResponse')

    _M_ode.cmd._t_OriginalMetadataResponse = IcePy.defineClass('::ode::cmd::OriginalMetadataResponse', OriginalMetadataResponse, -1, (), False, False, _M_ode.cmd._t_OK, (), (
        ('filesetId', (), _M_ode._t_RLong, False, 0),
        ('fileAnnotationId', (), _M_ode._t_RLong, False, 0),
        ('globalMetadata', (), _M_ode._t_RTypeDict, False, 0),
        ('seriesMetadata', (), _M_ode._t_RTypeDict, False, 0)
    ))
    OriginalMetadataResponse._ice_type = _M_ode.cmd._t_OriginalMetadataResponse

    _M_ode.cmd.OriginalMetadataResponse = OriginalMetadataResponse
    del OriginalMetadataResponse

    _M_ode.cmd.OriginalMetadataResponsePrx = OriginalMetadataResponsePrx
    del OriginalMetadataResponsePrx

if 'UsedFilesRequest' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.UsedFilesRequest = Ice.createTempClass()
    class UsedFilesRequest(_M_ode.cmd.Request):
        """
        Request to determine the original files associated with the given
        image. The image must have an associated Pixels object. Different
        response objects are returned depending on if the image is FS or
        pre-FS.
        Members:
        imageId -- an image ID
        """
        def __init__(self, imageId=0):
            _M_ode.cmd.Request.__init__(self)
            self.imageId = imageId

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Request', '::ode::cmd::UsedFilesRequest')

        def ice_id(self, current=None):
            return '::ode::cmd::UsedFilesRequest'

        def ice_staticId():
            return '::ode::cmd::UsedFilesRequest'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_UsedFilesRequest)

        __repr__ = __str__

    _M_ode.cmd.UsedFilesRequestPrx = Ice.createTempClass()
    class UsedFilesRequestPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.UsedFilesRequestPrx.ice_checkedCast(proxy, '::ode::cmd::UsedFilesRequest', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.UsedFilesRequestPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::UsedFilesRequest'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_UsedFilesRequestPrx = IcePy.defineProxy('::ode::cmd::UsedFilesRequest', UsedFilesRequestPrx)

    _M_ode.cmd._t_UsedFilesRequest = IcePy.defineClass('::ode::cmd::UsedFilesRequest', UsedFilesRequest, -1, (), False, False, _M_ode.cmd._t_Request, (), (('imageId', (), IcePy._t_long, False, 0),))
    UsedFilesRequest._ice_type = _M_ode.cmd._t_UsedFilesRequest

    _M_ode.cmd.UsedFilesRequest = UsedFilesRequest
    del UsedFilesRequest

    _M_ode.cmd.UsedFilesRequestPrx = UsedFilesRequestPrx
    del UsedFilesRequestPrx

if 'UsedFilesResponsePreFs' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.UsedFilesResponsePreFs = Ice.createTempClass()
    class UsedFilesResponsePreFs(_M_ode.cmd.OK):
        """
        The used files associated with a pre-FS image.
        Members:
        archivedFiles -- The original file IDs of any archived files associated with
        the image.
        companionFiles -- The original file IDs of any companion files associated with
        the image.
        originalMetadataFiles -- The original file IDs of any original metadata files associated
        with the image.
        """
        def __init__(self, archivedFiles=None, companionFiles=None, originalMetadataFiles=None):
            _M_ode.cmd.OK.__init__(self)
            self.archivedFiles = archivedFiles
            self.companionFiles = companionFiles
            self.originalMetadataFiles = originalMetadataFiles

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::OK', '::ode::cmd::Response', '::ode::cmd::UsedFilesResponsePreFs')

        def ice_id(self, current=None):
            return '::ode::cmd::UsedFilesResponsePreFs'

        def ice_staticId():
            return '::ode::cmd::UsedFilesResponsePreFs'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_UsedFilesResponsePreFs)

        __repr__ = __str__

    _M_ode.cmd.UsedFilesResponsePreFsPrx = Ice.createTempClass()
    class UsedFilesResponsePreFsPrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.UsedFilesResponsePreFsPrx.ice_checkedCast(proxy, '::ode::cmd::UsedFilesResponsePreFs', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.UsedFilesResponsePreFsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::UsedFilesResponsePreFs'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_UsedFilesResponsePreFsPrx = IcePy.defineProxy('::ode::cmd::UsedFilesResponsePreFs', UsedFilesResponsePreFsPrx)

    _M_ode.cmd._t_UsedFilesResponsePreFs = IcePy.defineClass('::ode::cmd::UsedFilesResponsePreFs', UsedFilesResponsePreFs, -1, (), False, False, _M_ode.cmd._t_OK, (), (
        ('archivedFiles', (), _M_ode.api._t_LongList, False, 0),
        ('companionFiles', (), _M_ode.api._t_LongList, False, 0),
        ('originalMetadataFiles', (), _M_ode.api._t_LongList, False, 0)
    ))
    UsedFilesResponsePreFs._ice_type = _M_ode.cmd._t_UsedFilesResponsePreFs

    _M_ode.cmd.UsedFilesResponsePreFs = UsedFilesResponsePreFs
    del UsedFilesResponsePreFs

    _M_ode.cmd.UsedFilesResponsePreFsPrx = UsedFilesResponsePreFsPrx
    del UsedFilesResponsePreFsPrx

if 'UsedFilesResponse' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.UsedFilesResponse = Ice.createTempClass()
    class UsedFilesResponse(_M_ode.cmd.OK):
        """
        The used files associated with an FS image.
        Members:
        binaryFilesThisSeries -- The original file IDs of any binary files associated with the
        image's particular series.
        binaryFilesOtherSeries -- The original file IDs of any binary files associated with the
        image's fileset but not with its particular series.
        companionFilesThisSeries -- The original file IDs of any companion files associated with the
        image's particular series.
        companionFilesOtherSeries -- The original file IDs of any companion files associated with the
        image's fileset but not with its particular series.
        """
        def __init__(self, binaryFilesThisSeries=None, binaryFilesOtherSeries=None, companionFilesThisSeries=None, companionFilesOtherSeries=None):
            _M_ode.cmd.OK.__init__(self)
            self.binaryFilesThisSeries = binaryFilesThisSeries
            self.binaryFilesOtherSeries = binaryFilesOtherSeries
            self.companionFilesThisSeries = companionFilesThisSeries
            self.companionFilesOtherSeries = companionFilesOtherSeries

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::OK', '::ode::cmd::Response', '::ode::cmd::UsedFilesResponse')

        def ice_id(self, current=None):
            return '::ode::cmd::UsedFilesResponse'

        def ice_staticId():
            return '::ode::cmd::UsedFilesResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_UsedFilesResponse)

        __repr__ = __str__

    _M_ode.cmd.UsedFilesResponsePrx = Ice.createTempClass()
    class UsedFilesResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.UsedFilesResponsePrx.ice_checkedCast(proxy, '::ode::cmd::UsedFilesResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.UsedFilesResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::UsedFilesResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_UsedFilesResponsePrx = IcePy.defineProxy('::ode::cmd::UsedFilesResponse', UsedFilesResponsePrx)

    _M_ode.cmd._t_UsedFilesResponse = IcePy.defineClass('::ode::cmd::UsedFilesResponse', UsedFilesResponse, -1, (), False, False, _M_ode.cmd._t_OK, (), (
        ('binaryFilesThisSeries', (), _M_ode.api._t_LongList, False, 0),
        ('binaryFilesOtherSeries', (), _M_ode.api._t_LongList, False, 0),
        ('companionFilesThisSeries', (), _M_ode.api._t_LongList, False, 0),
        ('companionFilesOtherSeries', (), _M_ode.api._t_LongList, False, 0)
    ))
    UsedFilesResponse._ice_type = _M_ode.cmd._t_UsedFilesResponse

    _M_ode.cmd.UsedFilesResponse = UsedFilesResponse
    del UsedFilesResponse

    _M_ode.cmd.UsedFilesResponsePrx = UsedFilesResponsePrx
    del UsedFilesResponsePrx

if 'ManageImageBinaries' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.ManageImageBinaries = Ice.createTempClass()
    class ManageImageBinaries(_M_ode.cmd.Request):
        """
        Queries and modifies the various binary artifacts
        which may be linked to an ode.model.Image.
        This can be useful, e.g., after converting pre-ode-5
        archived original files into ode.model.Fileset.
        The command works in several stages:
        1. loads an ode.model.Image by id, failing if none
        present.
        2. renames Pixels file to '*_bak'
        3. deletes existing Pyramidfiles if present;
        This command can be run multiple times with different settings
        to iteratively test if the migration is working.
        """
        def __init__(self, imageId=0, togglePixels=False, deletePyramid=False, deleteThumbnails=False):
            _M_ode.cmd.Request.__init__(self)
            self.imageId = imageId
            self.togglePixels = togglePixels
            self.deletePyramid = deletePyramid
            self.deleteThumbnails = deleteThumbnails

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::ManageImageBinaries', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::ManageImageBinaries'

        def ice_staticId():
            return '::ode::cmd::ManageImageBinaries'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_ManageImageBinaries)

        __repr__ = __str__

    _M_ode.cmd.ManageImageBinariesPrx = Ice.createTempClass()
    class ManageImageBinariesPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.ManageImageBinariesPrx.ice_checkedCast(proxy, '::ode::cmd::ManageImageBinaries', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.ManageImageBinariesPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::ManageImageBinaries'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_ManageImageBinariesPrx = IcePy.defineProxy('::ode::cmd::ManageImageBinaries', ManageImageBinariesPrx)

    _M_ode.cmd._t_ManageImageBinaries = IcePy.defineClass('::ode::cmd::ManageImageBinaries', ManageImageBinaries, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('imageId', (), IcePy._t_long, False, 0),
        ('togglePixels', (), IcePy._t_bool, False, 0),
        ('deletePyramid', (), IcePy._t_bool, False, 0),
        ('deleteThumbnails', (), IcePy._t_bool, False, 0)
    ))
    ManageImageBinaries._ice_type = _M_ode.cmd._t_ManageImageBinaries

    _M_ode.cmd.ManageImageBinaries = ManageImageBinaries
    del ManageImageBinaries

    _M_ode.cmd.ManageImageBinariesPrx = ManageImageBinariesPrx
    del ManageImageBinariesPrx

if 'ManageImageBinariesResponse' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.ManageImageBinariesResponse = Ice.createTempClass()
    class ManageImageBinariesResponse(_M_ode.cmd.Response):
        """
        Response from a ManageImageBinaries Request.
        If no action is requested, then the fields of this
        instance can be examined to see what would be done
        if requested.
        """
        def __init__(self, filesetId=None, archivedFiles=None, pixelsPresent=False, pyramidPresent=False, archivedSize=0, pixelSize=0, pyramidSize=0, thumbnailSize=0):
            _M_ode.cmd.Response.__init__(self)
            self.filesetId = filesetId
            self.archivedFiles = archivedFiles
            self.pixelsPresent = pixelsPresent
            self.pyramidPresent = pyramidPresent
            self.archivedSize = archivedSize
            self.pixelSize = pixelSize
            self.pyramidSize = pyramidSize
            self.thumbnailSize = thumbnailSize

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::ManageImageBinariesResponse', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::ManageImageBinariesResponse'

        def ice_staticId():
            return '::ode::cmd::ManageImageBinariesResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_ManageImageBinariesResponse)

        __repr__ = __str__

    _M_ode.cmd.ManageImageBinariesResponsePrx = Ice.createTempClass()
    class ManageImageBinariesResponsePrx(_M_ode.cmd.ResponsePrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.ManageImageBinariesResponsePrx.ice_checkedCast(proxy, '::ode::cmd::ManageImageBinariesResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.ManageImageBinariesResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::ManageImageBinariesResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_ManageImageBinariesResponsePrx = IcePy.defineProxy('::ode::cmd::ManageImageBinariesResponse', ManageImageBinariesResponsePrx)

    _M_ode.cmd._t_ManageImageBinariesResponse = IcePy.declareClass('::ode::cmd::ManageImageBinariesResponse')

    _M_ode.cmd._t_ManageImageBinariesResponse = IcePy.defineClass('::ode::cmd::ManageImageBinariesResponse', ManageImageBinariesResponse, -1, (), False, False, _M_ode.cmd._t_Response, (), (
        ('filesetId', (), _M_ode._t_RLong, False, 0),
        ('archivedFiles', (), _M_ode.api._t_LongList, False, 0),
        ('pixelsPresent', (), IcePy._t_bool, False, 0),
        ('pyramidPresent', (), IcePy._t_bool, False, 0),
        ('archivedSize', (), IcePy._t_long, False, 0),
        ('pixelSize', (), IcePy._t_long, False, 0),
        ('pyramidSize', (), IcePy._t_long, False, 0),
        ('thumbnailSize', (), IcePy._t_long, False, 0)
    ))
    ManageImageBinariesResponse._ice_type = _M_ode.cmd._t_ManageImageBinariesResponse

    _M_ode.cmd.ManageImageBinariesResponse = ManageImageBinariesResponse
    del ManageImageBinariesResponse

    _M_ode.cmd.ManageImageBinariesResponsePrx = ManageImageBinariesResponsePrx
    del ManageImageBinariesResponsePrx

if 'DiskUsage' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.DiskUsage = Ice.createTempClass()
    class DiskUsage(_M_ode.cmd.Request):
        """
        Request to determine the disk usage of the given objects
        and their contents. File-system paths used by multiple objects
        are de-duplicated in the total count. Specifying a class is
        equivalent to specifying all its instances as objects.
        Permissible classes include:
        ExperimenterGroup, Experimenter, Project, Dataset,
        Folder, Screen, Plate, Well, WellSample,
        Image, Pixels, Annotation, Job, Fileset, OriginalFile.
        """
        def __init__(self, classes=None, objects=None):
            _M_ode.cmd.Request.__init__(self)
            self.classes = classes
            self.objects = objects

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::DiskUsage', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::DiskUsage'

        def ice_staticId():
            return '::ode::cmd::DiskUsage'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_DiskUsage)

        __repr__ = __str__

    _M_ode.cmd.DiskUsagePrx = Ice.createTempClass()
    class DiskUsagePrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.DiskUsagePrx.ice_checkedCast(proxy, '::ode::cmd::DiskUsage', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.DiskUsagePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::DiskUsage'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_DiskUsagePrx = IcePy.defineProxy('::ode::cmd::DiskUsage', DiskUsagePrx)

    _M_ode.cmd._t_DiskUsage = IcePy.defineClass('::ode::cmd::DiskUsage', DiskUsage, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('classes', (), _M_ode.api._t_StringSet, False, 0),
        ('objects', (), _M_ode.api._t_StringLongListMap, False, 0)
    ))
    DiskUsage._ice_type = _M_ode.cmd._t_DiskUsage

    _M_ode.cmd.DiskUsage = DiskUsage
    del DiskUsage

    _M_ode.cmd.DiskUsagePrx = DiskUsagePrx
    del DiskUsagePrx

if 'DiskUsageResponse' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.DiskUsageResponse = Ice.createTempClass()
    class DiskUsageResponse(_M_ode.cmd.Response):
        """
        Disk usage report: bytes used and non-empty file counts on the
        repository file-system for specific objects. The counts from the
        maps may sum to more than the total if different types of object
        refer to the same file. Common referrers include:
        Annotation for file annotations
        FilesetEntry for ode 5 image files (ode.fs)
        Job for import logs
        Pixels for pyramids and ODE 4 images and archived files
        Thumbnail for the image thumbnails
        The above map values are broken down by owner-group keys.
        """
        def __init__(self, fileCountByReferer=None, bytesUsedByReferer=None, totalFileCount=None, totalBytesUsed=None):
            _M_ode.cmd.Response.__init__(self)
            self.fileCountByReferer = fileCountByReferer
            self.bytesUsedByReferer = bytesUsedByReferer
            self.totalFileCount = totalFileCount
            self.totalBytesUsed = totalBytesUsed

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::DiskUsageResponse', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::DiskUsageResponse'

        def ice_staticId():
            return '::ode::cmd::DiskUsageResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_DiskUsageResponse)

        __repr__ = __str__

    _M_ode.cmd.DiskUsageResponsePrx = Ice.createTempClass()
    class DiskUsageResponsePrx(_M_ode.cmd.ResponsePrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.DiskUsageResponsePrx.ice_checkedCast(proxy, '::ode::cmd::DiskUsageResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.DiskUsageResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::DiskUsageResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_DiskUsageResponsePrx = IcePy.defineProxy('::ode::cmd::DiskUsageResponse', DiskUsageResponsePrx)

    _M_ode.cmd._t_DiskUsageResponse = IcePy.defineClass('::ode::cmd::DiskUsageResponse', DiskUsageResponse, -1, (), False, False, _M_ode.cmd._t_Response, (), (
        ('fileCountByReferer', (), _M_ode.api._t_LongPairToStringIntMap, False, 0),
        ('bytesUsedByReferer', (), _M_ode.api._t_LongPairToStringLongMap, False, 0),
        ('totalFileCount', (), _M_ode.api._t_LongPairIntMap, False, 0),
        ('totalBytesUsed', (), _M_ode.api._t_LongPairLongMap, False, 0)
    ))
    DiskUsageResponse._ice_type = _M_ode.cmd._t_DiskUsageResponse

    _M_ode.cmd.DiskUsageResponse = DiskUsageResponse
    del DiskUsageResponse

    _M_ode.cmd.DiskUsageResponsePrx = DiskUsageResponsePrx
    del DiskUsageResponsePrx

# End of module ode.cmd

__name__ = 'ode'

# End of module ode
