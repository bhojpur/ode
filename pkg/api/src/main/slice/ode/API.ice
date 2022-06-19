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

#ifndef ODE_API_ICE
#define ODE_API_ICE

#include <ode/cmd/API.ice>
#include <ode/ServerErrors.ice>
#include <ode/ModelF.ice>
#include <ode/ServicesF.ice>
#include <ode/System.ice>
#include <ode/Collections.ice>
#include <Glacier2/Session.ice>
#include <Ice/BuiltinSequences.ice>
#include <Ice/Identity.ice>

module ode {

    /** The ode::api module defines all the central verbs for working with
     * the Bhojpur ODE server.
     *
     * <p> Arguments and return values consist of those
     * types defined in the other ice files available here. With no
     * further custom code, it is possible to interoperate with
     * ODE.server simply via the definitions here. Start with the
     * ServiceFactory definition at the end of this file.</p>
     *
     * <p> NOTE: Using these types is significantly easier in combination with
     * the JavaDocs of the Bhojpur ODE server, specifically the ode.api
     * package. Where not further noted below, the follow mappings between
     * ode.api argument types and ode::api argument types hold: </p>
     *
     * <pre>
     *     +-----------------------+------------------------+
     *     |        ode.api        |      ode::api          |
     *     +-----------------------+------------------------+
     *     |java.lang.Class        |string                  |
     *     +-----------------------+------------------------+
     *     |java.util.Set          |java.util.List/vector   |
     *     +-----------------------+------------------------+
     *     |IPojo options (Map)    |ode::sys::ParamMap      |
     *     +-----------------------+------------------------+
     *     |If null needed         |ode::RType subclass     |
     *     +-----------------------+------------------------+
     *     |...                    |...                     |
     *     +-----------------------+------------------------+
     * </pre>
     **/
    module api {

        /**
         * Primary callback interface for interaction between client and
         * server session (""ServiceFactory""). Where possible these methods
         * will be called one-way to prevent clients from hanging the server.
         **/

        ["ami"] interface ClientCallback
        {

            /**
             * Heartbeat-request made by the server to guarantee that the client
             * is alive. If the client is still active, then some method should
             * be made on the server to update the last idle time.
             **/
            void requestHeartbeat();

            /**
             * The session to which this ServiceFactory is connected has been
             * closed. Almost no further method calls (if any) are possible.
             * Create a new session via ode.client.createSession()
             **/
            void sessionClosed();

            /**
             * Message that the server will be shutting down in the
             * given number of milliseconds, after which all new and
             * running method invocations will receive a CancelledException.
             **/
            void shutdownIn(long milliseconds);

        };

        /**
         * Starting point for all Bhojupr ODE server interaction.
         *
         * <p> A ServiceFactory once acquired can be used to create any number
         * of service proxies to the server. Most services implement
         * {@link ServiceInterface} or its subinterface
         * {@link StatefulServiceInterface}. </p>
         **/
        interface ServiceFactory extends ode::cmd::Session
        {

            // Security context

            /**
             * Provides a list of all valid security contexts for this session.
             * Each of the returned {@link ode.model.IObject} instances can
             * be passed to {@code setSecurityContext}.
             **/
            IObjectList getSecurityContexts() throws ServerError;

            /**
             * Changes the security context for the current session.
             *
             * <p> A security context limits the set of objects which will
             * be returned by all queries and restricts what updates
             * can be made. </p>
             *
             * <p> Current valid values for security context:
             * <ul>
             *  <li>{@link ode.model.ExperimenterGroup} - logs into a
             * specific group</li>
             *  <li>{@link ode.model.Share} - uses IShare to activate a
             * share</li>
             * </ul> </p>
             *
             * <p> Passing an unloaded version of either object type will change
             * the way the current session operates. Note: only objects which
             * are returned by the {@code getSecurityContext} method are
             * considered valid. Any other instance will cause an exception to
             * be thrown. </p>
             *
             * <h4>Example usage in Python:<h4>
             * <pre>
             * sf = client.createSession()
             * objs = sf.getSecurityContexts()
             * old = sf.setSecurityContext(objs\[-1])
             * </pre>
             *
             **/
            ode::model::IObject setSecurityContext(ode::model::IObject obj) throws ServerError;

            /**
             * Re-validates the password for the current session. This prevents
             *
             * See methods that mention "HasPassword".
             **/
            void setSecurityPassword(string password) throws ServerError;

            // Central Bhojpur ODE server stateless services.

            IAdmin*          getAdminService() throws ServerError;
            IConfig*         getConfigService() throws ServerError;
            IContainer*      getContainerService() throws ServerError;
            ILdap*           getLdapService() throws ServerError;
            IPixels*         getPixelsService() throws ServerError;
            IProjection*     getProjectionService() throws ServerError;
            IQuery*          getQueryService() throws ServerError;
            IRenderingSettings* getRenderingSettingsService() throws ServerError;
            IRepositoryInfo* getRepositoryInfoService() throws ServerError;
            IRoi*            getRoiService() throws ServerError;
            IScript*         getScriptService() throws ServerError;
            ISession*        getSessionService() throws ServerError;
            IShare*          getShareService() throws ServerError;
            ITimeline*       getTimelineService() throws ServerError;
            ITypes*          getTypesService() throws ServerError;
            IUpdate*         getUpdateService() throws ServerError;
            IMetadata*       getMetadataService() throws ServerError;

            // Central Bhojpur ODE server stateful services.

            Exporter*        createExporter() throws ServerError;
            JobHandle*       createJobHandle() throws ServerError;
            RawFileStore*    createRawFileStore() throws ServerError;
            RawPixelsStore*  createRawPixelsStore() throws ServerError;
            RenderingEngine* createRenderingEngine() throws ServerError;
            Search*          createSearchService() throws ServerError;
            ThumbnailStore*  createThumbnailStore() throws ServerError;

            // Shared resources -----------------------------------------------

            /**
             * Returns a reference to a back-end manager. The
             * {@link ode.grid.SharedResources} service provides lookups
             * for various facilities offered by Bhojpur ODE:
             * <ul>
             *   <li><a href="https://docs.bhojpur.net/latest/ode/developers/scripts/">ODE.scripts</a>
             *   <li><a href="https://docs.bhojpur.net/latest/ode/developers/Tables.html">ODE.tables</a>
             * </ul>
             * These facilities may or may not be available on first request.
             *
             * @see ode.grid.SharedResources
             **/
            ode::grid::SharedResources* sharedResources() throws ServerError;

            // General methods ------------------------------------------------

            /**
             * Allows looking up any stateless service by name.
             *
             * See Constants.ice for examples of services.
             * If a service has been added by third-parties,
             * getByName can be used even though no concrete
             * method is available.
             **/
            ServiceInterface* getByName(string name) throws ServerError;

            /**
             * Allows looking up any stateful service by name.
             *
             * See Constants.ice for examples of services.
             * If a service has been added by third-parties,
             * createByName can be used even though no concrete
             * method is available.
             **/
            StatefulServiceInterface* createByName(string name) throws ServerError;

            /**
             * Subscribe to a given topic. The topic must exist and the user must
             * have sufficient permissions for that topic. Further the proxy object
             * must match the required type for the topic as encoded in the topic
             * name.
             **/
            void subscribe(string topicName, Object* prx) throws ServerError;

            /**
             * Sets the single callback used by the ServiceFactory
             * to communicate with the client application. A default
             * callback is set by the ode::client object on
             * session creation which should suffice for most usage.
             *
             * See the client object's documentation in each language
             * mapping for ways to use the callback.
             **/
            void setCallback(ClientCallback* callback) throws ServerError;

            /**
             * Marks the session for closure rather than detachment, which will
             * be triggered by the destruction of the Glacier2 connection via
             * router.destroySession()
             *
             * Closing the session rather the detaching is more secure, since all
             * resources are removed from the server and can safely be set once
             * it is clear that a client is finished with those resources.
             **/
            void closeOnDestroy() throws ServerError;

            /**
             * Marks the session for detachment rather than closure, which will
             * be triggered by the destruction of the Glacier2 connection via
             * router.destroySession()
             *
             * This is the default and allows a lost session to be reconnected,
             * at a slight security cost since the session will persist longer
             * and can be used by others if the UUID is intercepted.
             **/
            void detachOnDestroy() throws ServerError;

            // Session management

            /**
             * Returns a list of string ids for currently active services. This will
             * _not_ keep services alive, and in fact checks for all expired services
             * and removes them.
             **/
            StringSet activeServices() throws ServerError;

            /**
             * Requests that the given services be marked as alive. It is
             * possible that one of the services has already timed out, in which
             * case the returned long value will be non-zero.
             *
             * Specifically, the bit representing the 0-based index will be 1:
             * {@code
             *        if (retval & 1&lt;&lt;idx == 1&lt;&lt;idx) { // not alive }
             * }
             * Except for fatal server or session errors, this method should never
             * throw an exception.
             **/
            long keepAllAlive(ServiceList proxies) throws ServerError;

            /**
             * Returns true if the given service is alive.
             *
             * Except for fatal server or session errors, this method should never
             * throw an exception.
             **/
            bool keepAlive(ServiceInterface* proxy) throws ServerError;

        };

    };
};

#endif