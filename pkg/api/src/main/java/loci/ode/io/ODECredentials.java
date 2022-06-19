package loci.ode.io;

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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.StringTokenizer;

import loci.common.Constants;

/**
 * Stores credentials for logging into a ODE/ server.
 */
@Deprecated
public class ODECredentials {

  // -- Fields --

  public String server;
  public String port;
  public String username;
  public String password;
  public long imageID;
  public boolean isODE;

  // -- Constructor --

  public ODECredentials(String server, String username, String password) {
    this.server = server;
    this.username = username;
    this.password = password;
  }

  /**
   * Get credentials from a string. The following two formats are recognized:
   * {@code
   * ip.address?port=54321&username=login&password=secret&id=12345
   * }
   * or:
   * <pre>
   * server=ip.address
   * port=54321
   * user=login
   * password=secret
   * id=12345
   * </pre>
   * Strings are assumed to be encoded with the HTML form encoding scheme,
   * and will be decoded accordingly.
   */
  public ODECredentials(String s) {
    final String invalidMsg = "Invalid credentials string";
    if (s == null) {
      throw new IllegalArgumentException(invalidMsg);
    }

    String split = s.indexOf("\n") < 0 ? "?&" : "\n";
    StringTokenizer st = new StringTokenizer(s, split);
    while (st.hasMoreTokens()) {
      String token = st.nextToken();

      int equals = token.indexOf("=");
      String key = equals < 0 ? "server" : token.substring(0, equals);
      String value = token.substring(equals + 1);

      try {
        key = URLDecoder.decode(key, Constants.ENCODING).trim();
        value = URLDecoder.decode(value, Constants.ENCODING).trim();
      }
      catch (UnsupportedEncodingException exc) {
        throw new IllegalArgumentException(invalidMsg, exc);
      }

      if (key.equals("server")) server = value;
      else if (key.equals("username")) username = value;
      else if (key.equals("port")) port = value;
      else if (key.equals("password")) password = value;
      else if (key.equals("id")) {
        try {
          imageID = Long.parseLong(value);
        }
        catch (NumberFormatException exc) {
          throw new IllegalArgumentException(invalidMsg, exc);
        }
      }
    }
  }

}