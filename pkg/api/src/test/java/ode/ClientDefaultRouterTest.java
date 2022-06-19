package ode;

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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "unit", timeOut = 5000)
public class ClientDefaultRouterTest {

  @DataProvider
  public Object[][] clientHosturlParams() {
    return new Object[][]{
        {
          new String[]{"--ode.host=example.org"},
          "ODE.Glacier2/router:ssl -p 4064 -h example.org",
        },
        {
            new String[]{"--ode.host=example.org", "--ode.port=12345"},
            "ODE.Glacier2/router:ssl -p 12345 -h example.org",
        },

        {
            new String[]{"--ode.host=ssl://example.org"},
            "ODE.Glacier2/router:ssl -p 4064 -h example.org",
        },
        {
            new String[]{"--ode.host=ssl://example.org:12345"},
            "ODE.Glacier2/router:ssl -p 12345 -h example.org",
        },

        {
            new String[]{"--ode.host=tcp://example.org"},
            "ODE.Glacier2/router:tcp -p 4063 -h example.org",
        },
        {
            new String[]{"--ode.host=tcp://example.org:12345"},
            "ODE.Glacier2/router:tcp -p 12345 -h example.org",
        },

        {
            new String[]{"--ode.host=wss://example.org"},
            "ODE.Glacier2/router:wss -p 443 -h example.org",
        },
        {
            new String[]{"--ode.host=wss://example.org:12345"},
            "ODE.Glacier2/router:wss -p 12345 -h example.org",
        },
        {
            new String[]{"--ode.host=wss://example.org/ode"},
            "ODE.Glacier2/router:wss -p 443 -h example.org -r /ode",
        },
        {
            new String[]{"--ode.host=wss://example.org:12345/ode"},
            "ODE.Glacier2/router:wss -p 12345 -h example.org -r /ode",
        },

        {
            new String[]{"--ode.host=ws://example.org"},
            "ODE.Glacier2/router:ws -p 80 -h example.org",
        },
        {
            new String[]{"--ode.host=ws://example.org:12345"},
            "ODE.Glacier2/router:ws -p 12345 -h example.org",
        },
        {
            new String[]{"--ode.host=ws://example.org/ode"},
            "ODE.Glacier2/router:ws -p 80 -h example.org -r /ode",
        },
        {
            new String[]{"--ode.host=ws://example.org:12345/ode"},
            "ODE.Glacier2/router:ws -p 12345 -h example.org -r /ode",
        },

    };
  }

  @Test
  public void testHost() {
    client c = new client("example.org");
    Assert.assertEquals(c.getProperty("Ice.Default.Router"), "ODE.Glacier2/router:ssl -p 4064 -h example.org");
  }

  @Test
  public void testHostPort() {
    client c = new client("example.org", 12345);
    Assert.assertEquals(c.getProperty("Ice.Default.Router"), "ODE.Glacier2/router:ssl -p 12345 -h example.org");
  }

  @Test(dataProvider = "clientHosturlParams")
  public void testHosturl(String[] args, String expected) {
    client c = new client(args);
    Assert.assertEquals(c.getProperty("Ice.Default.Router"), expected);
  }
}