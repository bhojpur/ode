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

# Start of module ode
_M_ode = Ice.openModule('ode')
__name__ = 'ode'

# Start of module ode.constants
_M_ode.constants = Ice.openModule('ode.constants')
__name__ = 'ode.constants'
_M_ode.constants.__doc__ = """
Most client-intended constants are provided in this module.
"""

_M_ode.constants.CLIENTUUID = "ode.client.uuid"

_M_ode.constants.SESSIONUUID = "ode.session.uuid"

_M_ode.constants.GCINTERVAL = 60

_M_ode.constants.GLACIER2PORT = 4064

_M_ode.constants.MESSAGESIZEMAX = 250000

_M_ode.constants.DEFAULTBATCHSIZE = 2000

_M_ode.constants.DEFAULTBLOCKSIZE = 5000000

_M_ode.constants.CLIENTTHREADPOOLSIZE = 5

_M_ode.constants.CONNECTTIMEOUT = 5000

_M_ode.constants.DEFAULTROUTER = "ode.Glacier2/router:ssl -p \100ode.port\100 -h \100ode.host\100" if _version_info_[0] < 3 else "ode.Glacier2/router:ssl -p \U00000040ode.port\U00000040 -h \U00000040ode.host\U00000040"

_M_ode.constants.ACMCLIENTHEARTBEAT = 2

_M_ode.constants.ACMCLIENTTIMEOUT = 60000

_M_ode.constants.ADMINSERVICE = "ode.api.IAdmin"

_M_ode.constants.ANALYSISSERVICE = "ode.api.IAnalysis"

_M_ode.constants.CONFIGSERVICE = "ode.api.IConfig"

_M_ode.constants.CONTAINERSERVICE = "ode.api.IContainer"

_M_ode.constants.EXPORTERSERVICE = "ode.api.Exporter"

_M_ode.constants.LDAPSERVICE = "ode.api.ILdap"

_M_ode.constants.PIXELSSERVICE = "ode.api.IPixels"

_M_ode.constants.PROJECTIONSERVICE = "ode.api.IProjection"

_M_ode.constants.QUERYSERVICE = "ode.api.IQuery"

_M_ode.constants.SESSIONSERVICE = "ode.api.ISession"

_M_ode.constants.SHARESERVICE = "ode.api.IShare"

_M_ode.constants.TIMELINESERVICE = "ode.api.ITimeline"

_M_ode.constants.TYPESSERVICE = "ode.api.ITypes"

_M_ode.constants.UPDATESERVICE = "ode.api.IUpdate"

_M_ode.constants.JOBHANDLE = "ode.api.JobHandle"

_M_ode.constants.RAWFILESTORE = "ode.api.RawFileStore"

_M_ode.constants.RAWPIXELSSTORE = "ode.api.RawPixelsStore"

_M_ode.constants.RENDERINGENGINE = "ode.api.RenderingEngine"

_M_ode.constants.ROISERVICE = "ode.api.IRoi"

_M_ode.constants.SCRIPTSERVICE = "ode.api.IScript"

_M_ode.constants.SEARCH = "ode.api.Search"

_M_ode.constants.THUMBNAILSTORE = "ode.api.ThumbnailStore"

_M_ode.constants.REPOSITORYINFO = "ode.api.IRepositoryInfo"

_M_ode.constants.RENDERINGSETTINGS = "ode.api.IRenderingSettings"

_M_ode.constants.METADATASERVICE = "ode.api.IMetadata"

_M_ode.constants.SHAREDRESOURCES = "ode.grid.SharedResources"

_M_ode.constants.USERNAME = "ode.user"

_M_ode.constants.PASSWORD = "ode.pass"

_M_ode.constants.GROUP = "ode.group"

_M_ode.constants.EVENT = "ode.event"

_M_ode.constants.AGENT = "ode.agent"

_M_ode.constants.IP = "ode.ip"

# Start of module ode.constants.cluster
_M_ode.constants.cluster = Ice.openModule('ode.constants.cluster')
__name__ = 'ode.constants.cluster'

_M_ode.constants.cluster.REDIRECT = "ode.cluster.redirect"

# End of module ode.constants.cluster

__name__ = 'ode.constants'

# Start of module ode.constants.annotation
_M_ode.constants.annotation = Ice.openModule('ode.constants.annotation')
__name__ = 'ode.constants.annotation'
_M_ode.constants.annotation.__doc__ = """
General constants used for annotations.
"""

# Start of module ode.constants.annotation.file
_M_ode.constants.annotation.file = Ice.openModule('ode.constants.annotation.file')
__name__ = 'ode.constants.annotation.file'
_M_ode.constants.annotation.file.__doc__ = """
Constants used for file annotations.
"""

_M_ode.constants.annotation.file.ORIGINALMETADATAPREFIX = "/bhojpur.net/ode/image_files/"

_M_ode.constants.annotation.file.ORIGINALMETADATA = "original_metadata.txt"

# End of module ode.constants.annotation.file

__name__ = 'ode.constants.annotation'

# End of module ode.constants.annotation

__name__ = 'ode.constants'

# Start of module ode.constants.data
_M_ode.constants.data = Ice.openModule('ode.constants.data')
__name__ = 'ode.constants.data'
_M_ode.constants.data.__doc__ = """
Constants used for field defaults and similar
in the ode.model classes.
"""

_M_ode.constants.data.NONAMESET = "NO_NAME_SET"

# End of module ode.constants.data

__name__ = 'ode.constants'

if 'LogLevel' not in _M_ode.constants.__dict__:
    _M_ode.constants.LogLevel = Ice.createTempClass()
    class LogLevel(Ice.EnumBase):
        """
        Log levels used by {@code RawAccessRequest}'s {@code log} command for {@code path}.
        """

        def __init__(self, _n, _v):
            Ice.EnumBase.__init__(self, _n, _v)

        def valueOf(self, _n):
            if _n in self._enumerators:
                return self._enumerators[_n]
            return None
        valueOf = classmethod(valueOf)

    LogLevel.Trace = LogLevel("Trace", 0)
    LogLevel.Debug = LogLevel("Debug", 1)
    LogLevel.Info = LogLevel("Info", 2)
    LogLevel.Warn = LogLevel("Warn", 3)
    LogLevel.Error = LogLevel("Error", 4)
    LogLevel._enumerators = { 0:LogLevel.Trace, 1:LogLevel.Debug, 2:LogLevel.Info, 3:LogLevel.Warn, 4:LogLevel.Error }

    _M_ode.constants._t_LogLevel = IcePy.defineEnum('::ode::constants::LogLevel', LogLevel, (), LogLevel._enumerators)

    _M_ode.constants.LogLevel = LogLevel
    del LogLevel

# Start of module ode.constants.metadata
_M_ode.constants.metadata = Ice.openModule('ode.constants.metadata')
__name__ = 'ode.constants.metadata'
_M_ode.constants.metadata.__doc__ = """
Namespaces for the ode.api.IMetadata interface.
"""

_M_ode.constants.metadata.NSINSIGHTTAGSET = "bhojpur.net/ode/insight/tagset"

_M_ode.constants.metadata.NSINSIGHTRATING = "bhojpur.net/ode/insight/rating"

_M_ode.constants.metadata.NSMOVIE = "bhojpur.net/ode/movie"

_M_ode.constants.metadata.NSCLIENTMAPANNOTATION = "bhojpur.net/ode/client/mapAnnotation"

# End of module ode.constants.metadata

__name__ = 'ode.constants'

# Start of module ode.constants.namespaces
_M_ode.constants.namespaces = Ice.openModule('ode.constants.namespaces')
__name__ = 'ode.constants.namespaces'
_M_ode.constants.namespaces.__doc__ = """
General namespaces for StructuredAnnotations
"""

_M_ode.constants.namespaces.NSFSRENAME = "bhojpur.net/ode/fs/rename"

_M_ode.constants.namespaces.NSMEASUREMENT = "bhojpur.net/ode/measurement"

_M_ode.constants.namespaces.NSAUTOCLOSE = "bhojpur.net/ode/import/autoClose"

_M_ode.constants.namespaces.NSTARGETTEMPLATE = "bhojpur.net/ode/import/targetTemplate"

_M_ode.constants.namespaces.NSCOMPANIONFILE = "bhojpur.net/ode/import/companionFile"

_M_ode.constants.namespaces.NSLOGFILE = "bhojpur.net/ode/import/logFile"

_M_ode.constants.namespaces.NSFILETRANSFER = "bhojpur.net/ode/import/fileTransfer"

_M_ode.constants.namespaces.NSEXPERIMENTERPHOTO = "bhojpur.net/ode/experimenter/photo"

_M_ode.constants.namespaces.NSBULKANNOTATIONS = "bhojpur.net/ode/bulk_annotations"

_M_ode.constants.namespaces.NSODETIFF = "bhojpur.net/ode/ode_tiff"

_M_ode.constants.namespaces.NSCREATED = "bhojpur.net/ode/scripts/results/created"

_M_ode.constants.namespaces.NSDOWNLOAD = "bhojpur.net/ode/scripts/results/download"

_M_ode.constants.namespaces.NSVIEW = "bhojpur.net/ode/scripts/results/view"

_M_ode.constants.namespaces.NSDYNAMIC = "bhojpur.net/ode/scripts/job/dynamic"

_M_ode.constants.namespaces.NSMODULO = "bhojpur.net/ode/dimension/modulo"

# End of module ode.constants.namespaces

__name__ = 'ode.constants'

# Start of module ode.constants.analysis
_M_ode.constants.analysis = Ice.openModule('ode.constants.analysis')
__name__ = 'ode.constants.analysis'
_M_ode.constants.analysis.__doc__ = """
Namespaces for analysis.
"""

# Start of module ode.constants.analysis.flim
_M_ode.constants.analysis.flim = Ice.openModule('ode.constants.analysis.flim')
__name__ = 'ode.constants.analysis.flim'
_M_ode.constants.analysis.flim.__doc__ = """
namespaces related to the FLIM analysis.
"""

_M_ode.constants.analysis.flim.NSFLIM = "bhojpur.net/ode/analysis/flim"

_M_ode.constants.analysis.flim.KEYWORDFLIMCELL = "Cell"

_M_ode.constants.analysis.flim.KEYWORDFLIMBACKGROUND = "Background"

# End of module ode.constants.analysis.flim

__name__ = 'ode.constants.analysis'

# End of module ode.constants.analysis

__name__ = 'ode.constants'

# Start of module ode.constants.jobs
_M_ode.constants.jobs = Ice.openModule('ode.constants.jobs')
__name__ = 'ode.constants.jobs'

_M_ode.constants.jobs.SUBMITTED = "Submitted"

_M_ode.constants.jobs.RESUBMITTED = "Resubmitted"

_M_ode.constants.jobs.QUEUED = "Queued"

_M_ode.constants.jobs.REQUEUED = "Requeued"

_M_ode.constants.jobs.RUNNING = "Running"

_M_ode.constants.jobs.ERRORX = "Error"

_M_ode.constants.jobs.WAITING = "Waiting"

_M_ode.constants.jobs.FINISHED = "Finished"

_M_ode.constants.jobs.CANCELLED = "Cancelled"

# End of module ode.constants.jobs

__name__ = 'ode.constants'

# Start of module ode.constants.permissions
_M_ode.constants.permissions = Ice.openModule('ode.constants.permissions')
__name__ = 'ode.constants.permissions'

_M_ode.constants.permissions.LINKRESTRICTION = 0

_M_ode.constants.permissions.EDITRESTRICTION = 1

_M_ode.constants.permissions.DELETERESTRICTION = 2

_M_ode.constants.permissions.ANNOTATERESTRICTION = 3

_M_ode.constants.permissions.CHGRPRESTRICTION = 4

_M_ode.constants.permissions.CHOWNRESTRICTION = 5

_M_ode.constants.permissions.BINARYACCESS = "RESTRICT-BINARY-ACCESS"

# End of module ode.constants.permissions

__name__ = 'ode.constants'

# Start of module ode.constants.projection
_M_ode.constants.projection = Ice.openModule('ode.constants.projection')
__name__ = 'ode.constants.projection'

_M_ode.constants.projection.MAXIMUMINTENSITYMETHODOLOGY = "MAXIMUM_INTENSITY_PROJECTION"

_M_ode.constants.projection.MEANINTENSITYMETHODOLOGY = "MEAN_INTENSITY_PROJECTION"

_M_ode.constants.projection.SUMINTENSITYMETHODOLOGY = "SUM_INTENSITY_PROJECTION"

if 'ProjectionType' not in _M_ode.constants.projection.__dict__:
    _M_ode.constants.projection.ProjectionType = Ice.createTempClass()
    class ProjectionType(Ice.EnumBase):
        """
        Used by the IProjection methods to declare which projection to perform.
        """

        def __init__(self, _n, _v):
            Ice.EnumBase.__init__(self, _n, _v)

        def valueOf(self, _n):
            if _n in self._enumerators:
                return self._enumerators[_n]
            return None
        valueOf = classmethod(valueOf)

    ProjectionType.MAXIMUMINTENSITY = ProjectionType("MAXIMUMINTENSITY", 0)
    ProjectionType.MEANINTENSITY = ProjectionType("MEANINTENSITY", 1)
    ProjectionType.SUMINTENSITY = ProjectionType("SUMINTENSITY", 2)
    ProjectionType._enumerators = { 0:ProjectionType.MAXIMUMINTENSITY, 1:ProjectionType.MEANINTENSITY, 2:ProjectionType.SUMINTENSITY }

    _M_ode.constants.projection._t_ProjectionType = IcePy.defineEnum('::ode::constants::projection::ProjectionType', ProjectionType, (), ProjectionType._enumerators)

    _M_ode.constants.projection.ProjectionType = ProjectionType
    del ProjectionType

# End of module ode.constants.projection

__name__ = 'ode.constants'

# Start of module ode.constants.topics
_M_ode.constants.topics = Ice.openModule('ode.constants.topics')
__name__ = 'ode.constants.topics'

_M_ode.constants.topics.PROCESSORACCEPTS = "/internal/ProcessorAccept"

_M_ode.constants.topics.HEARTBEAT = "/public/HeartBeat"

# End of module ode.constants.topics

__name__ = 'ode.constants'

# Start of module ode.constants.categories
_M_ode.constants.categories = Ice.openModule('ode.constants.categories')
__name__ = 'ode.constants.categories'

_M_ode.constants.categories.PROCESSORCALLBACK = "ProcessorCallback"

_M_ode.constants.categories.PROCESSCALLBACK = "ProcessCallback"

# End of module ode.constants.categories

__name__ = 'ode.constants'

# End of module ode.constants

__name__ = 'ode'

# End of module ode
