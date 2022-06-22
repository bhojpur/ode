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

#include <iosfwd>
#include <sstream>
#include <stdexcept>

#include <ode/IceNoWarnPush.h>
#include <ode/Constants.h>
#include <ode/api/IAdmin.h>
#include <ode/api/IConfig.h>
#include <ode/api/ISession.h>
#include <ode/model/IObject.h>
#include <ode/IceNoWarnPop.h>

#include <ode/client.h>
#include <ode/RTypesI.h>

#include <ode/model/SessionI.h>
#include <ode/util/uuid.h>
#include <ode/ObjectFactory.h>
#include <ode/model/ObjectFactory.h>

using namespace std;

namespace ode {

    void client::optionallySetProperty(const Ice::InitializationData& id,
            const std::string& key, const std::string& def) {
        std::string val(id.properties->getProperty(key));
        if (val.empty()) {
            id.properties->setProperty(key, def);
        } else {
            id.properties->setProperty(key, val);
        }
    }

    void client::init(const Ice::InitializationData& data) {

        // Not possible for id to be null since its a struct
        Ice::InitializationData id(data);

        if (!id.properties) {
            int argc = 0;
            char* argv[] = {0};
            id.properties = Ice::createProperties(argc, argv);
        }

        // Strictly necessary for this class to work
        optionallySetProperty(id, "Ice.ImplicitContext", "Shared");
#if ICE_INT_VERSION / 100 >= 306
        stringstream sstimeoutInt;
        sstimeoutInt << ode::constants::ACMCLIENTTIMEOUT;
        optionallySetProperty(id, "Ice.ACM.Client.Timeout", sstimeoutInt.str());
        stringstream ssheartbeatInt;
        ssheartbeatInt << ode::constants::ACMCLIENTHEARTBEAT;
        optionallySetProperty(id, "Ice.ACM.Client.Heartbeat", ssheartbeatInt.str());
#else
        optionallySetProperty(id, "Ice.ACM.Client", "0");
#endif
        optionallySetProperty(id, "Ice.CacheMessageBuffers", "0");
        optionallySetProperty(id, "Ice.RetryIntervals", "-1");
        optionallySetProperty(id, "Ice.Default.EndpointSelection", "Ordered");
        optionallySetProperty(id, "Ice.Default.PreferSecure", "1");
        optionallySetProperty(id, "Ice.Plugin.IceSSL" , "IceSSL:createIceSSL");
#if ICE_INT_VERSION / 100 >= 306
    #if defined (__APPLE__)
        optionallySetProperty(id, "IceSSL.Ciphers" , "NONE (DH_anon.*AES)");
    #else
        optionallySetProperty(id, "IceSSL.Ciphers" , "ADH");
    #endif
#else
        optionallySetProperty(id, "IceSSL.Ciphers" , "ADH");
#endif

        optionallySetProperty(id, "IceSSL.Protocols" , "tls1");
        optionallySetProperty(id, "IceSSL.VerifyPeer" , "0");

        // Set the default encoding if this is Ice 3.5 or later
        // and none is set.
        optionallySetProperty(id, "Ice.Default.EncodingVersion", "1.0");

        // C++ only
#if ICE_INT_VERSION / 100 <= 305
        std::string gcInterval = id.properties->getProperty("Ice.GC.Interval");
        if ( gcInterval.length() == 0 ) {
            stringstream ssgcInt;
            ssgcInt << ode::constants::GCINTERVAL;
            id.properties->setProperty("Ice.GC.Interval", ssgcInt.str());
        }
#endif
        

        // Setting block size
        std::string blockSize = id.properties->getProperty("ode.block_size");
        if ( blockSize.length() == 0 ) {
            stringstream ssmsgsize;
            ssmsgsize << ode::constants::DEFAULTBLOCKSIZE;
            id.properties->setProperty("ode.block_size", ssmsgsize.str());
        }

        // Setting MessageSizeMax
        std::string messageSize = id.properties->getProperty("Ice.MessageSizeMax");
        if ( messageSize.length() == 0 ) {
            stringstream ssmsgsize;
            ssmsgsize << ode::constants::MESSAGESIZEMAX;
            id.properties->setProperty("Ice.MessageSizeMax", ssmsgsize.str());
        }

        // Setting ConnectTimeout
        parseAndSetInt(id, "Ice.Override.ConnectTimeout",
            ode::constants::CONNECTTIMEOUT);

        // Endpoints set to tcp if not present
        optionallySetProperty(id, "ode.ClientCallback.Endpoints", "tcp");

        // Set large thread pool max values for all communicators
        std::string xs[] = {"Client", "Server"};
        for (int i = 0; i < 2; i++) {
            std::string key = "Ice.ThreadPool." + xs[i] + ".SizeMax";
            std::string sizemax = id.properties->getProperty(key);
            if (sizemax.length() == 0) {
                id.properties->setProperty(key.c_str(), "50");
            }
        }

        // ThreadPool to 5 if not present
        std::string threadpool = id.properties->getProperty("ode.ClientCallback.ThreadPool.Size");
        if ( threadpool.length() == 0 ) {
                stringstream ssTpInt;
                ssTpInt << ode::constants::CLIENTTHREADPOOLSIZE;
            id.properties->setProperty("ode.ClientCallback.ThreadPool.Size", ssTpInt.str());
        }

        // Port, setting to default if not present
        std::string port = parseAndSetInt(id, "ode.port",
                    ode::constants::GLACIER2PORT);

        // Default Router, set a default and then replace
        std::string router = id.properties->getProperty("Ice.Default.Router");
        if ( router.length() == 0 ) {
            router = ode::constants::DEFAULTROUTER;
        }
        std::string host = id.properties->getPropertyWithDefault("ode.host", "<\"ode.host\" not set >");

        size_t found;
        std::string h_("@ode.host@");
        std::string p_("@ode.port@");
        while (true) {
            found = router.rfind(h_);
            if (found != string::npos) {
                router.replace(found, h_.length(), host);
                continue;
            }
            break;

        }
        while (true) {
            found = router.rfind(p_);
            if (found != string::npos) {
                router.replace(found, p_.length(), port);
                continue;
            }
            break;
        }
        id.properties->setProperty("Ice.Default.Router", router);

        // Dump properties
        std::string dump = id.properties->getProperty("ode.dump");
        if ( dump.length() != 0 ) {
            std::map<string, string> pm = getPropertyMap(id.properties);
            std::map<string, string>::const_iterator beg = pm.begin();
            std::map<string, string>::const_iterator end = pm.end();
            while (beg != end) {
                std::cout << (*beg).first << "=" << (*beg).second << std::endl;
                beg++;
            }
        }

        // Synchronization
        IceUtil::RecMutex::Lock lock(mutex);

        if ( __ic ) {
            throw ClientError(__FILE__, __LINE__, "Client already initialized.");
        }

        try {
            __ic = Ice::initialize(id);
        } catch (const Ice::EndpointParseException& epe) {
            throw ClientError(__FILE__, __LINE__,
                    "No host specified. Use ode.client(HOSTNAME), ICE_CONFIG, or similar.");
        }


        if ( ! __ic ) {
            throw ClientError(__FILE__, __LINE__, "Improper initialization");
        }

        // Register Object Factory
        ode::registerObjectFactory(__ic, this);
        ode::model::registerObjectFactory(__ic);
        ode::rtypes::registerObjectFactory(__ic);

        // Define our unique identifier (used during close/detach)
        __uuid = util::generate_uuid();
        Ice::ImplicitContextPtr ctx = __ic->getImplicitContext();
        if (!ctx) {
            throw ode::ClientError(__FILE__,__LINE__,"Ice.ImplicitContext not set to Shared");
        }
        ctx->put(ode::constants::CLIENTUUID, __uuid);

        // ticket:2951 - sending user group
        std::string group = id.properties->getPropertyWithDefault("ode.group", "");
        if (group.length() > 0) {
            ctx->put("ode.group", group);
        }
    }

    // --------------------------------------------------------------------

    client::client(int& argc, char* argv[],
        const Ice::InitializationData& data) : __insecure(false), __agent("ODE.cpp") {

        Ice::InitializationData id(data);

        if ( ! id.properties ) {
            id.properties = Ice::createProperties(argc, argv);
        }

        Ice::StringSeq args = Ice::argsToStringSeq(argc, argv);
        id.properties->parseIceCommandLineOptions(args);
        id.properties->parseCommandLineOptions("ode", args);
        init(id);
    }


    // --------------------------------------------------------------------


    client::client(const Ice::InitializationData& id) : __insecure(false), __agent("ODE.cpp") {
        init(id);
    }


    // --------------------------------------------------------------------

    client::client(const std::string& host, int port) : __insecure(false), __agent("ODE.cpp") {
        int argc = 0;
        char* argv[] = {0};
        stringstream ss;
        ss << port;
        Ice::InitializationData id;
        id.properties = Ice::createProperties(argc, argv);
        id.properties->setProperty("ode.host", host);
        id.properties->setProperty("ode.port", ss.str());
        init(id);
    }

    // --------------------------------------------------------------------

    client::client(const std::map<std::string, std::string>& props, bool secure) : __agent("ODE.cpp") {

        __insecure = !secure;

        int argc = 0;
        char* argv[] = {0};
        Ice::InitializationData id;
        id.properties = Ice::createProperties(argc, argv);

        std::map<string, string>::const_iterator beg = props.begin();
        std::map<string, string>::const_iterator end = props.end();
        while (beg != end) {
            id.properties->setProperty((*beg).first, (*beg).second);
            beg++;
        }
        init(id);
    }

    // --------------------------------------------------------------------

    void client::setAgent(const std::string& agent) {
        __agent = agent;
    }

    // --------------------------------------------------------------------

    bool client::isSecure() {
        return !__insecure;
    }

    // --------------------------------------------------------------------

    client_ptr client::createClient(bool secure) {

        std::map<string, string> props = getPropertyMap();
        if (!secure) {
            string insecure = getSession()->getConfigService()->getConfigValue(
                    "ode.router.insecure");
            if (insecure.length() != 0) {
                props["Ice.Default.Router"] = insecure;
            } else {
                getCommunicator()->getLogger()->warning("Could not retrieve \"ode.router.insecure\"");
            }
        }

        std::string agent(__agent);
        agent.append(";secure=");
        agent.append(secure ? "true" : "false");
        client_ptr nClient = new ode::client(props, secure);
        nClient->setAgent(agent);
        nClient->joinSession(getSessionId());
        return nClient;
    }

    // --------------------------------------------------------------------

    client::~client() {
        __del__();
    }

    void client::__del__() {
        try {
            closeSession();
        } catch (const std::exception& ex) {
            std::cout << ex.what() << std::endl;
        }
    }

    // Acessors
    // ===================================================================


    Ice::CommunicatorPtr client::getCommunicator() const {
        IceUtil::RecMutex::Lock lock(mutex);
        if ( ! __ic ) {
            throw ClientError(__FILE__, __LINE__, "No Ice.Communicator active; call createSession()");
        }
        return __ic;
    }

    Ice::ObjectAdapterPtr client::getObjectAdapter() const {
        IceUtil::RecMutex::Lock lock(mutex);
        if ( ! __oa ) {
            throw ClientError(__FILE__, __LINE__, "No Ice.ObjectAdapter active; call createSession()");
        }
        return __oa;
    }

    // --------------------------------------------------------------------


    std::string client::getSessionId() const {
        return getSession()->ice_getIdentity().name;
    }

    std::string client::getCategory() const {
        return getRouter(getCommunicator())->getCategoryForClient();
    }

    // --------------------------------------------------------------------
    ode::api::ServiceFactoryPrx client::getSession() const {
        IceUtil::RecMutex::Lock lock(mutex);
        if ( ! __sf ) {
            throw ClientError(__FILE__, __LINE__, "Call createSession() to login");
        }
        return __sf;
    }


    // --------------------------------------------------------------------


    Ice::ImplicitContextPtr client::getImplicitContext() const {
        return getCommunicator()->getImplicitContext();
    }


    // --------------------------------------------------------------------


    Ice::PropertiesPtr client::getProperties() const {
        return getCommunicator()->getProperties();
    }

    // --------------------------------------------------------------------

    std::map<string, string> client::getPropertyMap(const Ice::PropertiesPtr& properties) const {

        Ice::PropertiesPtr props;
        if (!properties) {
            props = getProperties();
        } else {
            props = properties;
        }

        std::map<string, string> pm;
        Ice::PropertyDict odeProperties = props->getPropertiesForPrefix("ode");
        Ice::PropertyDict::const_iterator beg = odeProperties.begin();
        Ice::PropertyDict::const_iterator end = odeProperties.end();
        while (beg != end) {
            pm[(*beg).first] = (*beg).second;
            beg++;
        }
        Ice::PropertyDict iceProperties = props->getPropertiesForPrefix("Ice");
        beg = iceProperties.begin();
        end = iceProperties.end();
        while (beg != end) {
            pm[(*beg).first] = (*beg).second;
            beg++;
        }
        return pm;
    }


    // --------------------------------------------------------------------


    std::string client::getProperty(const std::string& key) const {
        return getCommunicator()->getProperties()->getProperty(key);
    }

    // --------------------------------------------------------------------


    int client::getDefaultBlockSize() const {
        try {
            std::stringstream ss(getProperty("ode.block_size"));
            int num;
            ss >> num;
            return num;
        } catch (...) {
            return ode::constants::DEFAULTBLOCKSIZE;
        }
    }


    // Session management
    // ====================================================================


    ode::api::ServiceFactoryPrx client::joinSession(const std::string& _sessionUuid) {
        return createSession(_sessionUuid, _sessionUuid);
    }


    // --------------------------------------------------------------------


    ode::api::ServiceFactoryPrx client::createSession(const std::string& _username, const std::string& _password) {

        IceUtil::RecMutex::Lock lock(mutex);

        // Checking state

        if ( __sf ) {
            throw ClientError(__FILE__, __LINE__,
                    "Session already active. Create a new ode.client or closeSession()");
        }

        if ( ! __ic ) {
            if ( ! __previous.properties ) {
            throw ClientError(__FILE__, __LINE__,
                        "No previous data to recreate communicator");
            }
            init(__previous);
            __previous = Ice::InitializationData();
        }

        // Check the required properties

        std::string username, password;
        if ( _username.empty() ) {
            username = getProperty(ode::constants::USERNAME);
            if ( username.empty() ) {
                throw ode::ClientError(__FILE__,__LINE__,"No username provided");
            }
        } else {
            username = _username;
        }

        if (_password.empty()) {
            password = getProperty(ode::constants::PASSWORD);
        } else {
            password = _password;
        }

        // Acquire router and get the proxy
        Glacier2::SessionPrx prx;
        int retries = 0;
        while (retries < 3) {
            std::string reason;
            if (retries > 0) {
                stringstream msg;
                msg << reason << " - createSession retry: " << retries;
                __ic->getLogger()->warning(msg.str());
            }
            try {
                std::map<string, string> ctx = getImplicitContext()->getContext();
                ctx[ode::constants::AGENT] = __agent;
                prx = getRouter(__ic)->createSession(username, password);

                // Register the default client callback.
                Ice::Identity id = Ice::Identity();
                id.name = __uuid;
                id.category = getRouter(__ic)->getCategoryForClient();

                __oa = __ic->createObjectAdapterWithRouter("ode.ClientCallback", getRouter(__ic));
                __oa->activate();
                __oa->add(new CallbackI(__ic, __oa), id);

                break;
            } catch (const ode::WrappedCreateSessionException& wrapped) {
                if (!wrapped.concurrency) {
                    throw wrapped; // We only retry concurrency issues.
                }
                stringstream msg;
                msg << wrapped.type << ":" << wrapped.reason;
                reason = msg.str();
                retries++;
            } catch (Ice::ConnectTimeoutException cte) {
                stringstream msg;
                msg << "Ice.ConnectTimeoutException:" << cte;
                reason = msg.str();
                retries++;
            }
        }

        if ( ! prx ) {
            throw ode::ClientError(__FILE__,__LINE__,"Obtained null object proxy");
        }

        // Check type
        __sf = ode::api::ServiceFactoryPrx::uncheckedCast(prx);
        if ( ! __sf ) {
            throw ode::ClientError(__FILE__,__LINE__,"Obtained object proxy is not a ServiceFactory.");
        }

        // Set the client callback on the session
        // and pass it to icestorm
        try {
            Ice::Identity id = __ic->stringToIdentity("ClientCallback/" + __uuid);
            Ice::ObjectPrx raw = __oa->createProxy(id);
            __sf->setCallback(ode::api::ClientCallbackPrx::uncheckedCast(raw));
            //__sf->subscribe("/public/HeartBeat", raw);
        } catch (...) {
            __del__();
            throw;
        }


        // Set the session uuid in the implicit context
        getImplicitContext()->put(ode::constants::SESSIONUUID, getSessionId());

        return __sf;
    }


    // --------------------------------------------------------------------


    Glacier2::RouterPrx const client::getRouter(const Ice::CommunicatorPtr& comm) const {

        Ice::RouterPrx prx = comm->getDefaultRouter();
        if ( ! prx ) {
            throw ode::ClientError(__FILE__,__LINE__,"No default router found.");
        }

        Glacier2::RouterPrx router = Glacier2::RouterPrx::checkedCast(prx);
        if ( ! router ) {
            throw ClientError(__FILE__, __LINE__, "Error obtaining Glacier2 router");
        }

        // For whatever reason, we have to set the context
        // on the router context here as well.
        router = Glacier2::RouterPrx::uncheckedCast(router->ice_context(comm->getImplicitContext()->getContext()));

        return router;
    }


    // --------------------------------------------------------------------

    std::vector<ode::api::StatefulServiceInterfacePrx> client::getStatefulServices() {
            vector<ode::api::StatefulServiceInterfacePrx> rv;
            ode::api::ServiceFactoryPrx sf = getSession();
            vector<string> services = sf->activeServices();
            vector<string>::const_iterator srv = services.begin();
            vector<string>::const_iterator end = services.end();
            while (srv != end) {
                try {
                    ode::api::ServiceInterfacePrx prx = sf->getByName(*srv);
                    ode::api::StatefulServiceInterfacePrx sPrx =
                        ode::api::StatefulServiceInterfacePrx::checkedCast(prx);
                    if (sPrx) {
                        rv.push_back(sPrx);
                    }
                } catch (...) {
                    getCommunicator()->getLogger()->warning(
                            "Error looking up proxy: " + *srv);
                }
                srv++;
            }

            return rv;
        }
    // --------------------------------------------------------------------

    void client::closeSession() {

        IceUtil::RecMutex::Lock lock(mutex);

        __sf = NULL;

        Ice::ObjectAdapterPtr oldOa = __oa;
        __oa = Ice::ObjectAdapterPtr();

        Ice::CommunicatorPtr oldIc = __ic;
        __ic = Ice::CommunicatorPtr();

        // Only possible if improperly configured
        if (! oldIc) {
            return; // EARLY EXIT!
        }

        if (oldOa) {
            try {
                oldOa->deactivate();
            } catch (const std::exception& ex) {
                stringstream msg;
                msg << "While deactivating adapter : " << ex.what() << std::endl;
                oldIc->getLogger()->warning(msg.str());
            }
        }

        __previous = Ice::InitializationData();
        __previous.properties = oldIc->getProperties()->clone();

        try {
            getRouter(oldIc)->destroySession();
        } catch (const Glacier2::SessionNotExistException& snee) {
            // ok. We don't want it to exist
            oldIc->destroy();
        } catch (const Ice::ConnectionLostException& cle) {
            // ok. Exception will always be thrown.
            oldIc->destroy();
        } catch (const Ice::ConnectionRefusedException& cre) {
            // ok. Server probably went down
            oldIc->destroy();
        } catch (const Ice::ConnectTimeoutException& cre) {
            // ok. Server probably went down
            oldIc->destroy();
        } catch (const ode::ClientError& ce) {
            // This is called by getRouter() if a router is not configured.
            // If there isn't one, then we can't be connected. That's alright.
            // Most likely called during ~client
            oldIc->destroy();
        } catch (...) {
            oldIc->destroy();
            throw;
        }

    }

    int client::killSession() {

        ode::api::ServiceFactoryPrx sf = getSession();
        Ice::LoggerPtr __logger = getCommunicator()->getLogger();

        ode::model::SessionPtr s = new ode::model::SessionI();
        s->setUuid(ode::rtypes::rstring(getSessionId()));

        ode::api::ISessionPrx prx;
        try {
            prx = sf->getSessionService();
        } catch (...) {
            __logger->warning("Cannot get session service for killSession. Using closeSession");
            closeSession();
            return -1;
        }

        int count = 0;
        try {
            int r = 1;
            while (r > 0) {
                count++;
                r = prx->closeSession(s);
            }
        } catch (ode::RemovedSessionException rse) {
            // ignore
        } catch (...) {
            __logger->warning("Unknown exception while closing all references");
        }

        // Now the server-side session is dead, call closeSession()
        closeSession();
        return count;
    }

    // File handling
    // ====================================================================


    std::string client::sha1(const std::string&) {
        throw ClientError(__FILE__, __LINE__, "Not yet implemented");
    }


    // --------------------------------------------------------------------


    void client::upload(const std::string&,
                    const ode::model::OriginalFilePtr&,
                    int /*blockSize*/) {
        throw ClientError(__FILE__, __LINE__, "Not yet implemented");
    }


    // Environment methods
    // ====================================================================


    ode::RTypePtr client::getInput(const string& key) {
        return env()->getInput(getSessionId(), key);
    }
    ode::RTypePtr client::getOutput(const string& key) {
        return env()->getOutput(getSessionId(), key);
    }
    void client::setInput(const string& key, const ode::RTypePtr& value) {
        env()->setInput(getSessionId(), key, value);
    }
    void client::setOutput(const string& key, const ode::RTypePtr& value) {
        env()->setOutput(getSessionId(), key, value);
    }
    vector<string> client::getInputKeys() {
        return env()->getInputKeys(getSessionId());
    }
    vector<string> client::getOutputKeys() {
        return env()->getOutputKeys(getSessionId());
    }
    ode::api::ISessionPrx client::env() {
        return __sf->getSessionService();
    }

    std::string client::parseAndSetInt(const Ice::InitializationData& data,
                    const std::string& key, int newValue) {
        std::string currentValue = data.properties->getProperty(key);
        if (currentValue.empty()) {
            stringstream t;
            t << newValue;
            std::string newStr = t.str();
            data.properties->setProperty(key, newStr);
            currentValue = newStr;
        }
        return currentValue;
    }

    // Callback methods
    // ====================================================================

    CallbackIPtr client::_getCb() {
        Ice::ObjectPtr obj = __oa->find(__ic->stringToIdentity("ClientCallback" + __uuid));
        CallbackIPtr cb = CallbackIPtr::dynamicCast(obj);
        if (!cb) {
            throw ClientError(__FILE__,__LINE__,"Cannot find CallbackI in ObjectAdapter");
        }
        return cb;
    }

    void client::onHeartbeat(Callable callable) {
        _getCb()->onHeartbeat = callable;
    }

    void client::onSessionClosed(Callable callable) {
        _getCb()->onSessionClosed = callable;
    }

    void client::onShutdown(Callable callable) {
        _getCb()->onShutdown = callable;
    }

    CallbackI::CallbackI(const Ice::CommunicatorPtr& _ic, const Ice::ObjectAdapterPtr& _oa) {
        ic = _ic;
        oa = _oa;
        onHeartbeat = NoOpCallable();
        onSessionClosed = NoOpCallable();
        onShutdown = NoOpCallable();
    }

    void CallbackI::requestHeartbeat(const Ice::Current&) {
        execute(onHeartbeat, "heartbeat");
    }

    void CallbackI::sessionClosed(const Ice::Current&) {
        execute(onSessionClosed, "sessionClosed");
    }

    void CallbackI::shutdownIn(Ice::Long /*milliseconds*/, const Ice::Current&) {
        execute(onShutdown, "shutdown");
    }

    void CallbackI::execute(Callable callable, const string& action) {
        try {
            callable();
            // ic->getLogger()->trace("ClientCallback", action + " run");
        } catch (const std::exception& ex) {
            try {
                ic->getLogger()->error("Error performing " + action+": "+ex.what());
            } catch (const std::exception& ex2) {
                std::cerr << "Error performing " << action << ": " << ex.what() << std::endl;
                std::cerr << "(Stderr due to: " << ex2.what() << std::endl;
            }
        }
    }

}

ostream& operator<<(ostream& os, const ode::model::IObjectPtr ptr) {
  if (!ptr) {
    os << "null";
  } else {
    os << ptr->ice_staticId() << ":";
    if (!ptr->getId()) {
      os << "null_id";
    } else {
        ode::RLongPtr id = ptr->getId();
        if (id) {
            os << "null";
        } else {
            os << id->getValue();
        }
    }
  }
  return os;
}
