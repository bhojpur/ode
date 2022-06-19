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

#ifndef ODE_CONSTANTS_ICE
#define ODE_CONSTANTS_ICE

module ode {

  /**
   * Most client-intended constants are provided in this module.
   **/
  module constants {

    /**
     * Key in the ImplicitContext which must be filled
     * by all ode.client implementations. Primarily
     * used by the session manager to count references
     * to sessions.
     **/
    const string CLIENTUUID = "ode.client.uuid";

    /**
     * Key in the ImplicitContext which must be filled
     * by all ode.client implementations. Primarily
     * used by backend services to lookup the proper
     * sessions for clients.
     **/
    const string SESSIONUUID = "ode.session.uuid";

    /**
     * Default Ice.GC.Interval for OdeCpp (60 seconds)
     **/
    const int GCINTERVAL = 60;

    /**
     * Default Glacier2 port. Used to define '@ode.port@' if not set.
     **/
    const int GLACIER2PORT = 4064;

    /**
     * Default Ice.MessageSizeMax (250mb). Not strictly necessary, but helps to
     * curb memory issues. Must be set before communicator initialization.
     *
     * Temporarily set to 250MB
     **/
    const int MESSAGESIZEMAX = 250000;

    /**
     * Determines the batch size for sending
     * objects to the server. Too many can
     * result in MessageSizeMax errors.
     **/
    const int DEFAULTBATCHSIZE = 2000;

    /**
     * Default size for byte arrays during upload and download
     * of binary data.
     **/
    const int DEFAULTBLOCKSIZE = 5000000;

    /**
     * Default ode.ClientCallback.ThreadPool.Size (5).
     * Must be set before communicator initialization.
     **/
    const int CLIENTTHREADPOOLSIZE = 5;

    /**
     * Default Ice.Override.ConnectTimeout (5000). Also not strictly necessary,
     * but prevents clients being blocked by failed servers. -1 disables.
     **/
     const int CONNECTTIMEOUT = 5000;

    /**
     * Default connection string for connecting to Glacier2
     * (Ice.Default.Router). The '@ode.port@' and '@ode.host@' values will
     * be replaced by the properties with those names from the context.
     **/
    const string DEFAULTROUTER = "ODE.Glacier2/router:ssl -p @ode.port@ -h @ode.host@";

    /**
     * Introduce in Ice 3.6. Default Ice.ACM.Client.Heartbeat (2).
     * Send a heartbeat at regular intervals when the connection is idle.
     * To have any effect, ACMCLIENTTIMEOUT must be set to a non-zero value.
     **/
    const int ACMCLIENTHEARTBEAT = 2;

    /**
     * Introduce in Ice 3.6. Default Ice.ACM.Client.Timeout (60000).
     * Timeout in seconds for Active Connection Management.
     **/
    const int ACMCLIENTTIMEOUT = 60000;

    /**
     * Server-side names used for each of the services
     * defined in API.ice. These names can be used in
     * the {@code ServiceFactory.getByName} and {@code createByName}
     * methods.
     **/
    const string ADMINSERVICE     = "ode.api.IAdmin";
    const string ANALYSISSERVICE  = "ode.api.IAnalysis";
    const string CONFIGSERVICE    = "ode.api.IConfig";
    const string CONTAINERSERVICE = "ode.api.IContainer";
    const string EXPORTERSERVICE  = "ode.api.Exporter";
    const string LDAPSERVICE      = "ode.api.ILdap";
    const string PIXELSSERVICE    = "ode.api.IPixels";
    const string PROJECTIONSERVICE= "ode.api.IProjection";
    const string QUERYSERVICE     = "ode.api.IQuery";
    const string SESSIONSERVICE   = "ode.api.ISession";
    const string SHARESERVICE     = "ode.api.IShare";
    const string TIMELINESERVICE  = "ode.api.ITimeline";
    const string TYPESSERVICE     = "ode.api.ITypes";
    const string UPDATESERVICE    = "ode.api.IUpdate";
    const string JOBHANDLE        = "ode.api.JobHandle";
    const string RAWFILESTORE     = "ode.api.RawFileStore";
    const string RAWPIXELSSTORE   = "ode.api.RawPixelsStore";
    const string RENDERINGENGINE  = "ode.api.RenderingEngine";
    const string ROISERVICE       = "ode.api.IRoi";
    const string SCRIPTSERVICE    = "ode.api.IScript";
    const string SEARCH           = "ode.api.Search";
    const string THUMBNAILSTORE   = "ode.api.ThumbnailStore";
    const string REPOSITORYINFO   = "ode.api.IRepositoryInfo";
    const string RENDERINGSETTINGS= "ode.api.IRenderingSettings";
    const string METADATASERVICE  = "ode.api.IMetadata";
    const string SHAREDRESOURCES  = "ode.grid.SharedResources";

    // User context for logging in
    const string USERNAME = "ode.user";
    const string PASSWORD = "ode.pass";
    const string GROUP    = "ode.group";
    const string EVENT    = "ode.event";
    const string AGENT    = "ode.agent";
    const string IP       = "ode.ip";

    module cluster {
        // config string used by the ConfigRedirector
        const string REDIRECT = "ode.cluster.redirect";
    };

    /**
     * General constants used for annotations.
     **/
    module annotation {

        /** Constants used for file annotations. **/
        module file {
            const string ORIGINALMETADATAPREFIX = "/bhojpur.net/ode/image_files/";
            const string ORIGINALMETADATA = "original_metadata.txt";
        };
    };

    /**
     * Constants used for field defaults and similar
     * in the {@link ode.model} classes.
     **/
    module data {

        /**
         * Set as Image.name when no name is provided by the user.
         **/
        const string NONAMESET = "NO_NAME_SET";

    };

    /**
     * Log levels used by {@code RawAccessRequest}'s {@code log} command for {@code path}.
     **/
    enum LogLevel { Trace, Debug, Info, Warn, Error };

    /**
     * Namespaces for the {@link ode.api.IMetadata} interface.
     **/
    module metadata {
        const string NSINSIGHTTAGSET = "bhojpur.net/ode/insight/tagset";
        const string NSINSIGHTRATING = "bhojpur.net/ode/insight/rating";
        const string NSMOVIE = "bhojpur.net/ode/movie";
        const string NSCLIENTMAPANNOTATION = "bhojpur.net/ode/client/mapAnnotation";
    };

    /**
     * General namespaces for <a href="https://docs.bhojpur.net/latest/ode/developers/scripts/developers/Modules/StructuredAnnotations.html">StructuredAnnotations</a>
     **/
    module namespaces {
        const string NSFSRENAME = "bhojpur.net/ode/fs/rename";
        const string NSMEASUREMENT = "bhojpur.net/ode/measurement";
        const string NSAUTOCLOSE = "bhojpur.net/ode/import/autoClose";
        const string NSTARGETTEMPLATE = "bhojpur.net/ode/import/targetTemplate";
        const string NSCOMPANIONFILE = "bhojpur.net/ode/import/companionFile";
        const string NSLOGFILE = "bhojpur.net/ode/import/logFile";
        const string NSFILETRANSFER = "bhojpur.net/ode/import/fileTransfer";
        const string NSEXPERIMENTERPHOTO = "bhojpur.net/ode/experimenter/photo";
        const string NSBULKANNOTATIONS = "bhojpur.net/ode/bulk_annotations";
        const string NSODETIFF = "bhojpur.net/ode/ode_tiff";

        //
        // ode.grid.Param.namespaces in Scripts.ice
        //
        const string NSCREATED = "bhojpur.net/ode/scripts/results/created";
        const string NSDOWNLOAD = "bhojpur.net/ode/scripts/results/download";
        const string NSVIEW = "bhojpur.net/ode/scripts/results/view";

        //
        // ode.grid.JobParam.namespaces in Scripts.ice
        //
        const string NSDYNAMIC = "bhojpur.net/ode/scripts/job/dynamic";

        //
        // modulo namespaces for <a href="https://docs.bhojpur.net/latest/ode-model/developers/6d-7d-and-8d-storage.html">6d-7d-and-8d-storage</a>
        //
        const string NSMODULO = "bhojpur.net/ode/dimension/modulo";
    };

    /**
     * Namespaces for analysis.
     **/
    module analysis {
        /** namespaces related to the FLIM analysis. **/
        ["deprecate: It will be removed"]
        module flim {
            const string NSFLIM = "bhojpur.net/ode/analysis/flim";

           //keywords associated to the namespace.
           const string KEYWORDFLIMCELL = "Cell";
           const string KEYWORDFLIMBACKGROUND = "Background";
       };
    };

    module jobs {

      /**
       * Used by JobHandle as the status of jobs
       **/
      const string SUBMITTED = "Submitted";
      const string RESUBMITTED = "Resubmitted";
      const string QUEUED = "Queued";
      const string REQUEUED = "Requeued";
      const string RUNNING = "Running";
      const string ERRORX = "Error"; // Can't be 'ERROR' or C++ won't compile
      const string WAITING = "Waiting";
      const string FINISHED = "Finished";
      const string CANCELLED = "Cancelled";

    };

    module permissions {

      /**
       * Index into the {@code ode.model.Permissions.restrictions}
       * {@code ode.api.BoolArray} field to test whether or not
       * the link restriction has been applied to the current object.
       **/
      const int LINKRESTRICTION = 0;

      /**
       * Index into the {@code ode.model.Permissions.restrictions}
       * {@code ode.api.BoolArray} field to test whether or not
       * the edit restriction has been applied to the current object.
       **/
      const int EDITRESTRICTION = 1;

      /**
       * Index into the {@code ode.model.Permissions.restrictions}
       * {@code ode.api.BoolArray} field to test whether or not
       * the delete restriction has been applied to the current object.
       **/
      const int DELETERESTRICTION = 2;

      /**
       * Index into the {@code ode.model.Permissions.restrictions}
       * {@code ode.api.BoolArray} field to test whether or not
       * the annotate restriction has been applied to the current object.
       **/
      const int ANNOTATERESTRICTION = 3;

      /**
       * Index into the {@code ode.model.Permissions.restrictions}
       * {@code ode.api.BoolArray} field to test whether or not
       * the chgrp restriction has been applied to the current object.
       **/
      const int CHGRPRESTRICTION = 4;

      /**
       * Index into the {@code ode.model.Permissions.restrictions}
       * {@code ode.api.BoolArray} field to test whether or not
       * the chown restriction has been applied to the current object.
       **/
      const int CHOWNRESTRICTION = 5;

      /**
       * Extended restriction name which may be applied to images and other
       * downloadable materials. This string can also be found in the
       * ode.security.policy.BinaryAccessPolicy class.
       **/
      const string BINARYACCESS = "RESTRICT-BINARY-ACCESS";
    };

    module projection {

      /**
       * Methodology strings
       **/
      const string MAXIMUMINTENSITYMETHODOLOGY = "MAXIMUM_INTENSITY_PROJECTION";
      const string MEANINTENSITYMETHODOLOGY = "MEAN_INTENSITY_PROJECTION";
      const string SUMINTENSITYMETHODOLOGY = "SUM_INTENSITY_PROJECTION";

      /**
       * Used by the IProjection methods to declare which projection to perform.
       **/
      enum ProjectionType {
        MAXIMUMINTENSITY,
        MEANINTENSITY,
        SUMINTENSITY
      };
    };

    module topics {
        const string PROCESSORACCEPTS = "/internal/ProcessorAccept";
        const string HEARTBEAT = "/public/HeartBeat";
    };

    module categories {
        const string PROCESSORCALLBACK = "ProcessorCallback";
        const string PROCESSCALLBACK = "ProcessCallback";
    };
  };
};

#endif