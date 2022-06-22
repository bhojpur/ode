package ode.system;

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

import java.util.Properties;

import ode.conditions.ApiUsageException;

/**
 * Provides simplified handling of server properties when creating a
 * {@link ode.system.ServiceFactory}. For more complicated uses,
 * {@link java.util.Properties} can also be used. In which case, the constant
 * {@link java.lang.String strings} provided in this class can be used as the
 * keys to the {@link java.util.Properties properties instance} passed to
 * {@link ode.system.ServiceFactory#ServiceFactory(Properties)}.
 * 
 * @see ode.system.ServiceFactory
 */
public class Server {

    /**
     * Java property name for use in configuration of the client connection.
     */
    public final static String ODE_HOST = "server.host";

    /**
     * Java property name for use in configuration of the client connection.
     */
    public final static String ODE_PORT = "server.port";

    public final static int DEFAULT_PORT = 1099;

    private String _server, _port;

    // Need at least user and password
    private Server() {
    }

    /**
     * standard constructor which users {@link #DEFAULT_PORT}.
     * 
     * @param serverHost
     *            Not null.
     */
    public Server(String serverHost) {
        if (serverHost == null) {
            throw new ApiUsageException("serverHost argument "
                    + "to Server constructor cannot be null");
        }
        _server = serverHost;
        _port = Integer.toString(DEFAULT_PORT);
    }

    /**
     * extended constructor. As with {@link #Server(String)}, serverHost may
     * not be null.
     * 
     * @param serverHost
     *            Not null.
     * @param port
     */
    public Server(String serverHost, int port) {
        this(serverHost);
        if (port < 0) {
            throw new ApiUsageException("serverPort may not be null.");
        }
        _port = Integer.toString(port);
    }

    // ~ Views
    // =========================================================================

    /**
     * produces a copy of the internal fields as a {@link java.util.Properties}
     * instance. Only those keys are present for which a field is non-null.
     * 
     * @return Properties. Not null.
     */
    public Properties asProperties() {
        Properties p = new Properties();
        p.setProperty(ODE_HOST, _server);
        p.setProperty(ODE_PORT, _port);
        return p;
    }

    /**
     * simple getter for the server host passed into the constructor
     * 
     * @return host name Not null.
     */
    public String getHost() {
        return _server;
    }

    /**
     * simple getter for the port passed into the constructor or the default
     * port if none.
     */
    public int getPort() {
        return Integer.valueOf(_port);
    }

}