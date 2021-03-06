package ode.services.server.fire;

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

import java.lang.reflect.Method;

import ode.ApiUsageException;
import ode.InternalException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import IceStorm.AlreadySubscribed;
import IceStorm.BadQoS;
import IceStorm.NoSuchTopic;

/**
 * Local dispatcher to {@link IceStorm.TopicManager}
 */
public interface TopicManager extends ApplicationListener {

    /**
     * Enforces <em>no</em> security constraints. For the moment, that is the
     * responsibility of application code. WILL CHANGE
     */
    public void register(String topicName, Ice.ObjectPrx prx, boolean strict)
    throws ode.ServerError;

    public void unregister(String topicName, Ice.ObjectPrx prx)
    throws ode.ServerError;

    public final static class TopicMessage extends ApplicationEvent {

        private final String topic;
        private final Ice.ObjectPrxHelperBase base;
        private final String method;
        private final Object[] args;

        public TopicMessage(Object source, String topic,
                Ice.ObjectPrxHelperBase base, String method, Object... args) {
            super(source);
            this.topic = topic;
            this.base = base;
            this.method = method;
            this.args = args;
        }
    }

    public final static class Impl implements TopicManager {

        private final static Logger log = LoggerFactory.getLogger(Impl.class);

        private final Ice.Communicator communicator;

        public Impl(Ice.Communicator communicator) {
            this.communicator = communicator;
        }

        public void onApplicationEvent(ApplicationEvent event) {
            if (event instanceof TopicMessage) {

                TopicMessage msg = (TopicMessage) event;
                try {

                    IceStorm.TopicManagerPrx topicManager = managerOrNull();
                    if (topicManager == null) {
                        log.warn("No topic manager");
                        return; // EARLY EXIT
                    }

                    Ice.ObjectPrx obj = publisherOrNull(msg.topic);
                    msg.base.__copyFrom(obj);
                    Method m = null;
                    for (Method check : msg.base.getClass().getMethods()) {
                        if (check.getName().equals(msg.method)) {
                            if (check.getParameterTypes().length == msg.args.length) {
                                if (m != null) {
                                    String err = String
                                            .format(
                                                    "More than one method named "
                                                            + "\"%s\" with %s arguments",
                                                    msg.method, msg.args);
                                    log.error(err);
                                } else {
                                    m = check;
                                }
                            }
                        }
                    }
                    if (m == null) {
                        log.error(String.format("No method named \"%s\" "
                                + "with %s arguments", msg.method, msg.args));
                    } else {
                        m.invoke(msg.base, msg.args);
                    }
                } catch (Ice.NoEndpointException nee) {
                    // Most likely caused during testing.
                    log.debug("Ice.NoEndpointException");
                } catch (Exception e) {
                    log.error("Error publishing to topic:" + msg.topic, e);
                }
            }
        }

        public void register(String topicName, Ice.ObjectPrx prx, boolean strict)
                throws ode.ServerError {
            String id = prx.ice_id();
            id = id.replaceFirst("::", "");
            id = id.replace("::", ".");
            id = id + "PrxHelper";
            Class<?> pubClass = null;
            try {
                pubClass = Class.forName(id);
            } catch (ClassNotFoundException e) {
                throw new ApiUsageException(null, null,
                        "Unknown type for proxy: " + prx.ice_id());
            }
            IceStorm.TopicPrx topic = topicOrNull(topicName);

            while (topic != null) { // See IceStorm Clients under HA
                // IceStorm
                try {
                    topic.subscribeAndGetPublisher(null, prx);
                } catch (Ice.UnknownException ue) {
                    log.warn("Unknown exception on subscribeAndGetPublisher");
                    continue;
                } catch (AlreadySubscribed e) {
                    if (strict) {
                        throw new ApiUsageException(null, null,
                                "Proxy already subscribed: " + prx);
                    }
                } catch (BadQoS e) {
                    throw new InternalException(null, null,
                            "BadQos in TopicManager.subscribe");
                } catch (Ice.UserException ue) {
                    // Actually IceStorm.InvalidSubscriber, for Ice 3.4/3.5 compatibility
                    log.warn("Invalid subscriber on subscribeAndGetPublisher");
                    continue;
                }
                break;
            }
        }

        public void unregister(String topicName, Ice.ObjectPrx prx)
            throws ode.ServerError {

            try {
                IceStorm.TopicPrx topic = topicOrNull(topicName);
                if (topic != null) {
                    topic.unsubscribe(prx);
                }
            } catch (Exception e) {
                log.warn(String.format("Error unregistering: %s from %s",
                        prx, topicName));
            }

        }

        // Helpers
        // =========================================================================

        protected IceStorm.TopicManagerPrx managerOrNull() {

            Ice.ObjectPrx objectPrx = communicator
                    .stringToProxy("IceGrid/Query");
            Ice.ObjectPrx[] candidates = null;

            try {
                IceGrid.QueryPrx query = IceGrid.QueryPrxHelper
                        .checkedCast(objectPrx);
                candidates = query
                        .findAllObjectsByType("::IceStorm::TopicManager");
            } catch (Ice.CommunicatorDestroyedException cde) {
                // Nothing we can do. Return null;
                return null;
            } catch (Ice.NoEndpointException nee) {
                // Most likely caused during testing.
                log.debug("Ice.NoEndpointException");
            } catch (Exception e) {
                log.warn("Error querying for topic manager", e);
            }

            IceStorm.TopicManagerPrx tm = null;

            if (candidates == null || candidates.length == 0) {
                log.warn("Found no topic manager");
            } else if (candidates.length > 1) {
                log.warn("Found wrong number of topic managers: "
                        + candidates.length);
            } else {
                try {
                    tm = IceStorm.TopicManagerPrxHelper
                            .checkedCast(candidates[0]);
                } catch (Exception e) {
                    log.warn("Could not cast to TopicManager", e);
                }
            }
            return tm;
        }

        protected IceStorm.TopicPrx topicOrNull(String name) {
            IceStorm.TopicManagerPrx topicManager = managerOrNull();
            IceStorm.TopicPrx topic = null;
            if (topicManager != null) {
                try {
                    topic = topicManager.create(name);
                } catch (IceStorm.TopicExists ex2) {
                    try {
                        topic = topicManager.retrieve(name);
                    } catch (NoSuchTopic e) {
                        throw new RuntimeException(
                                "Race condition retriving topic: " + name);
                    }
                }
            }
            return topic;
        }

        protected Ice.ObjectPrx publisherOrNull(String name) {
            IceStorm.TopicPrx topic = topicOrNull(name);
            Ice.ObjectPrx pub = null;
            if (topic != null) {
                pub = topic.getPublisher().ice_oneway();
            }
            return pub;
        }
    }

}