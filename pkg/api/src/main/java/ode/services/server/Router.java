package ode.services.server;

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper around a call to 'glacier2router' to permit Java control over the
 * process.
 */
public class Router {

    private final static String LOCALHOST = "127.0.0.1";

    private final static Logger log = LoggerFactory.getLogger("ODE.router");

    Process p = null;

    private final Map<String, String> map = new HashMap<String, String>();
    {
        map.put("Glacier2.InstanceName", "ODE.Glacier2");
        setClientEndpoints(LOCALHOST, 4064);
        setSessionManager(LOCALHOST, 9999);
        setPermissionsVerifier(LOCALHOST, 9999);
        setTimeout(600);
    }

    public void allowAdministration() {
        map.put("Glacier2.Admin.Endpoints", "tcp -p 4064 -h 127.0.0.1");
    }

    public void setClientEndpoints(String host, int port) {
        map.put("Glacier2.Client.Endpoints", "tcp -p " + port + " -h " + host);
    }

    public void setServerEndpoints(String host, int port) {
        map.put("Glacier2.Server.Endpoints", "tcp -h " + host + " -p " + port);
    }

    public void setSessionManager(String host, int port) {
        map.put("Glacier2.SessionManager", "ServerManager:tcp -h " + host
                + " -p " + port);
    }

    public void setPermissionsVerifier(String host, int port) {
        map.put("Glacier2.PermissionsVerifier", "ServerVerifier:tcp -h " + host
                + " -p " + port);
    }

    public void setTimeout(int timeout) {
        map.put("Glacier2.SessionTimeout", "" + timeout);
    }

    public int start() {
        List<String> list = new ArrayList<String>();
        list.add(getBashPath());
        list.add("--daemon");
        for (String string : map.keySet()) {
            list.add("--" + string + "=" + map.get(string));
        }
        log.info(list.toString()); // slf4j migration: toString()
        ProcessBuilder pb = new ProcessBuilder(list.toArray(new String[list
                .size()]));
        try {
            p = pb.start();
            p.waitFor();
            return p.exitValue();
        } catch (Exception e) {
            log.info("Failed to start", e);
            return Integer.MIN_VALUE;
        }
    }

    String getBashPath() {
        ProcessBuilder pb = new ProcessBuilder("bash", "-l", "-c",
                "which glacier2router");
        String path;
        try {
            Process p = pb.start();
            StringBuilder sb = new StringBuilder();
            InputStream is = p.getInputStream();
            int c;
            while ((c = is.read()) != -1) {
                sb.append((char) c);
            }
            path = sb.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return path;
    }

}