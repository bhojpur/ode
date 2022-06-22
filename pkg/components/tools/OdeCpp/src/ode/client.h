// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

#ifndef ODE_CLIENT_H
#define ODE_CLIENT_H

#include <ode/IceNoWarnPush.h>
#include <ode/API.h>
#include <ode/Collections.h>
#include <ode/Constants.h>
#include <ode/ServerErrors.h>
#include <ode/ServicesF.h>
#include <ode/System.h>
#include <ode/model/OriginalFile.h>
#include <Ice/Ice.h>
#include <IceUtil/RecMutex.h>
#include <Glacier2/Glacier2.h>
#include <ode/IceNoWarnPop.h>

#include <ode/clientF.h>
#include <ode/ClientErrors.h>
#include <ode/ObjectFactoryRegistrar.h>
#include <string>
#include <iosfwd>

#ifndef ODE_CLIENT
#   ifdef ODE_CLIENT_EXPORTS
#       define ODE_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define ODE_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

namespace ode {

    /*
     * Simple type for use with the ClientCallback type below.
     */
    class ODE_CLIENT Callable {
    public:
        Callable() {};
        void operator()() {};
    };

    /*
     * Central client-side entry point and should be in sync with
     * OdeJavas and OdePy's ode.client classes.
     *
     * Typical usage:
     *   ode::client_ptr client = new ode::client();               // Uses ICE_CONFIG
     *   ode::client_ptr client = new ode::client(host);        // Defines "ode.host"
     *   ode::client_ptr client = new ode::client(host, port);  // Defines "ode.host" and "ode.port"
     *
     *   ode::client::~client() called on scope exit.
     *
     * For more information, see:
     *
     *    https://docs.bhojpur.net/latest/ode/developers/GettingStarted.html
     *
     */
    class ODE_CLIENT client : public IceUtil::Shared {

        // Preventing copy-construction and assigning by value.
    private:
        client& operator=(const client& rv);
        client(client&);
        void optionallySetProperty(const Ice::InitializationData& id,
                const std::string& key, const std::string& def="");

        // These are the central instances provided by this class.
    protected:

        /*
         * See isSecure
         */
        bool __insecure;

        /*
         * See setAgent(string)
        */
        std::string __agent;

        /*
        * Identifier for this client instance. Multiple client uuids may be
        * attached to a single session uuid.
        */
        std::string __uuid;

       /*
        * InitializationData from the last communicator used to create
        * ic if nulled after closeSession(). A pointer is used since
        * ID is a simple struct.
        */
        Ice::InitializationData __previous;

       /*
        * Ice.ObjectAdapter containing the ClientCallback for
        * this instance.
        */
        Ice::ObjectAdapterPtr __oa;

       /*
        * Single communicator for this ode::client. Nullness is used as
        * a test of what state the client is in, therefore all access is
        * synchronized by locking on mutex.
        */
        Ice::CommunicatorPtr __ic;

        /*
         * Single session for this ode::client. Nullness is used as a test
         * of what state the client is in, like ic, therefore all access is
         * synchronized by locking on mutex.
         */
        ode::api::ServiceFactoryPrx __sf;

        /*
         * Lock (mutex) for all access to ic and sf
         */
        IceUtil::RecMutex mutex;

        /*
         * Initializes the current client via an InitializationData instance.
         * This is called by all of the constructors, but may also be called
         * on createSession() if a previous call to closeSession() has nulled
         * the Ice::Communicator.
         */
        void init(const Ice::InitializationData& id);

    public:

        /*
         * Sets all the values in props as configuration properties.
         * Primarily for use with createClient
         */
        client(const std::map<std::string, std::string>& props, bool secure = true);

        /*
         * Creates an Ice::Communicator from command-line arguments. These
         * are parsed via Ice::Properties::parseIceCommandLineOptions(args) and
         * Ice::Properties::parseCommandLineOptions("ode", args)
         */
        client(int& argc, char* argv[],
               const Ice::InitializationData& id = Ice::InitializationData());

        /*
         * Default constructor which can only parse the ICE_CONFIG environment
         * variable in a manner similar to that of the --Ice.Config file.
         */
        client(const Ice::InitializationData& id = Ice::InitializationData());

        /*
         * Creates an Ice::Communicator pointing at the given server and port,
         * which defaults to ode::constants::GLACIER2PORT if none is given.
         */
        client(const std::string& host, int port = ode::constants::GLACIER2PORT);

        /*
         * Sets the ode.model.Session#getUserAgent() string for
         * this client. Every session creation will be passed this argument. Finding
         * open sesssions with the same agent can be done via
         * ode.api.ISessionPrx#getMyOpenAgentSessions(String).
         */
        void setAgent(const std::string& agent);

        /*
        * Specifies whether or not this client was created via a call to
        * createClient(bool) with a boolean of false. If insecure, then
        * all remote calls will use the insecure connection defined by the server.
        */
        bool isSecure();

       /*
        * Creates a possibly insecure ode::client instance and calls
        * joinSession(string) using the current getSessionId()
        * session id}. If secure is false, then first the "ode.router.insecure"
        * configuration property is retrieved from the server and used as the value
        * of "Ice.Default.Router" for the new client. Any exception thrown during
        * creation is passed on to the caller.
        *
        * Note: detachOnDestroy has NOT been called on the session in the returned client.
        * Clients are responsible for doing this immediately if such desired.
        */
        client_ptr createClient(bool secure);

        /*
         * Calls closeSession() and ignores all exceptions.
         */
        ~client();

        /*
         * Implements the ~client logic as in OdePy and OdeJava
         */
         void __del__();

        // Accessors
        // ==============================================================

        /*
         * Returns the Ice::Communicator for this instance or throws
         * an exception if null.
         */
        Ice::CommunicatorPtr getCommunicator() const;

        /*
         * Returns the Ice::ObjectAdapter for this instance or throws
         * an exception if null.
         */
        Ice::ObjectAdapterPtr getObjectAdapter() const;

        /*
         * Returns the current active session or throws an exception if none
         * has been created via createSession() since the last closeSession()
         */
        ode::api::ServiceFactoryPrx getSession() const;

        /*
         * Returns the UUID for the current session without making a remote call.
         * Uses getSession() internally and will throw an exception if
         * no session is active.
         */
         std::string getSessionId() const;

        /**
         * Returns the category which should be used for all callbacks
         * passed to the server.
         */
        std::string getCategory() const;

        /*
         * Returns the Ice::ImplicitContext which defiens what properties
         * will be sent on every method invocation.
         */
        Ice::ImplicitContextPtr getImplicitContext() const;

        /*
         * Returns the active Ice.Properties for this instance.
         */
        Ice::PropertiesPtr getProperties() const;

        /*
         * Returns the property value for this key or the empty string if none.
         */
        std::string getProperty(const std::string& key) const;

        /*
         * Returns all properties which are prefixed with "ode." or "Ice."
         * If no properties is passed, uses the Properties from {@link #getProperties()}.
         */
        std::map<std::string, std::string> getPropertyMap(const Ice::PropertiesPtr& props = Ice::PropertiesPtr()) const;

        /*
         * Returns the user-configured "ode.block_size" property or
         * ode::constants::DEFAULTBLOCKSIZE if none is set.
         */
        int getDefaultBlockSize() const;

        // Session management
        // ================================================================

        /*
         * Uses the given uuid as name and password to rejoin a running session.
         */
        ode::api::ServiceFactoryPrx joinSession(const std::string& sessionUuid);

        /*
         * Creates a session. Calling this method while a session is
         * active will throw an exception. It should only be used again,
         * after a session timeout exception, or similar.
         */
        ode::api::ServiceFactoryPrx createSession(const std::string& username = std::string(), const std::string& password = std::string());

        /*
         * Acquires the Ice::Communicator::getDefaultRouter() and throws an exception
         * if it is not of type Glacier2::RouterPrx. Also sets the ImplicitContext
         * on the router proxy.
         */
        Glacier2::RouterPrx const getRouter(const Ice::CommunicatorPtr& comm) const;

        /*
         * Calculates the local sha1 for a file.
         */
        std::string sha1(const std::string& file);

        /*
         * Utility method to upload a file. The original file can be a null pointer, and the
         * block size can be 0 or negative to use defaults. The string must point to a valid
         * file.
         */
        void upload(const std::string& file,
                    const ode::model::OriginalFilePtr& ofile,
                    int blockSize);

        /**
         * Returns all active StatefulServiceInterface proxies. This can
         * be used to call close before calling setSecurityContext.
         */
        std::vector<ode::api::StatefulServiceInterfacePrx> getStatefulServices();

        /*
         * Closes the Router connection created by createSession(). Due to a bug in Ice,
         * only one connection is allowed per communicator, so we also destroy the
         * communicator.
         *
         * http://www.zeroc.com/forums/help-center/2370-ice_ping-error-right-after-createsession-succeed.html
         */
        void closeSession();

        /*
         * Calls ISession.closeSession(ode.model.Session) until
         * the returned reference count is greater than zero. The
         * number of invocations is returned. If ISession.closeSession()
         * cannot be called, -1 is returned.
         */
        int killSession();

        // Environment methods. Allows to store and retrieve
        // ==================================================================


        /**
         * Sets an item in the "input" shared (session) memory under the given name.
         */
        ode::RTypePtr getInput(const std::string& key);

        /**
         * Sets an item in the "output" shared (session) memory under the given name.
         */
        ode::RTypePtr getOutput(const std::string& key);

        /*
         * Sets an item in the "input" shared (session) memory under the given name.
         */
        void setInput(const std::string& key, const ode::RTypePtr& value);

        /*
         * Sets an item in the "output" shared (session) memory under the given name.
         */
        void setOutput(const std::string& key, const ode::RTypePtr& value);

        /*
         * Returns a list of keys for all items in the "input" shared (session) memory
         */
        std::vector<std::string> getInputKeys();

        /*
         * Returns a list of keys for all items in the "output" shared (session) memory
         */
        std::vector<std::string> getOutputKeys();

    protected:
        const std::string sess();
        ode::api::ISessionPrx env();
        std::string parseAndSetInt(const Ice::InitializationData& id,
                                   const std::string& key, int port);

        // Callback methods
        // ==================================================================

        /*
         * Implementation of ClientCallback which will be added to
         * any Session which this instance creates
         */
    protected:
        CallbackIPtr _getCb();
    public:
        void onHeartbeat(Callable callable);
        void onSessionClosed(Callable callable);
        void onShutdown(Callable callable);
    };

    /*
     * Default implementation of the ode::api::ClientCallback servant.
     * Provides basic operator() implementations for each of the methods
     * that the server may call on it.
     */
    class ODE_CLIENT CallbackI : virtual public ode::api::ClientCallback {

        /*
         * ode::client needs access to the Callable fields on the callback.
         */
        friend class client;

        // Preventing copy-construction and assigning by value.
    private:
        CallbackI& operator=(const CallbackI& rv);
        CallbackI(CallbackI&);
        void execute(Callable callable, const std::string& action);
        // State
        Ice::CommunicatorPtr ic;
        Ice::ObjectAdapterPtr oa;
        Callable onHeartbeat;
        Callable onSessionClosed;
        Callable onShutdown;

    public:
        CallbackI(const Ice::CommunicatorPtr& ic, const Ice::ObjectAdapterPtr& oa);
        virtual void requestHeartbeat(const Ice::Current& current = Ice::Current());
        virtual void sessionClosed(const Ice::Current& current = Ice::Current());
        virtual void shutdownIn(Ice::Long milliSeconds, const Ice::Current& current = Ice::Current());
    };

    // Callable implementations
    // ==================================================================

    /*
     * Callable implementation which does nothing.
     */
    class ODE_CLIENT NoOpCallable : public Callable {
    public:
    NoOpCallable() : Callable() {}
        void operator()(){}
    };
}

/*
 * operator<< for printing all IObject classes
 */
std::ostream& operator<<(std::ostream& os, const ode::model::IObjectPtr ptr);

#endif // ODE_CLIENT_H
