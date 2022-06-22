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

# Version comparison functionality

from __future__ import print_function
import re
import logging

# Regex copied from ode.api.IConfig.VERSION_REGEX
REGEX = re.compile(r"^.*?[-]?(\\d+[.]\\d+([.]\\d+)?)[-]?.*?$")
LOG = logging.getLogger("ode.version")

def needs_upgrade(client_version, server_version, verbose=False):
    """
    Tests whether the client version is behind the server version.
    For example::

        import ode
        from ode_version import ode_version as client_version

        client = ode.client()
        session = client.createSession()
        config = session.getConfigService()
        server_version = config.getVersion()

        upgrade = needs_upgrade(client_version, server_version)
        if upgrade:
           # Inform client

    Alternatively, from the command-line::

        ./versions.py --quiet 4.1.0 4.2.0-DEV || echo upgrade
    """
    try:
        client_cleaned = REGEX.match(client_version).group(1)
        client_split = client_cleaned.split(".")

        server_cleaned = REGEX.match(server_version).group(1)
        server_split = server_cleaned.split(".")

        rv = (client_split < server_split)
        if verbose:
            LOG.info("Client=%20s (%-5s)  v.  Server=%20s (%-5s) Upgrade? %s",
                     client_version, ".".join(client_split),
                     server_version, ".".join(server_split), rv)
        return rv

    except:
        LOG.warn("Bad versions: client=%s server=%s", client_version,
                 server_version, exc_info=1)
        return True

if __name__ == "__main__":

    import sys
    args = list(sys.argv[1:])

    if "--quiet" in args:
        args.remove("--quiet")
        logging.basicConfig(level=logging.WARN)
    else:
        logging.basicConfig(level=logging.DEBUG)

    if "--test" in args:
        print("="*10, "Test", "="*72)
        needs_upgrade("4.0", "4.1.1", True)
        needs_upgrade("4.1", "4.1.1", True)
        needs_upgrade("4.1.0", "4.1.1", True)
        needs_upgrade("4.1.0", "4.1.1-Dev", True)
        needs_upgrade("4.1.0-Dev", "4.1.1", True)
        needs_upgrade("4.1.1", "4.1.1", True)
        needs_upgrade("Beta-4.1", "4.1.1", True)
        needs_upgrade("Beta-4.1.0", "4.1.1", True)
        needs_upgrade("Beta-4.1.1", "4.1.1", True)
        needs_upgrade("4.1.1", "Beta-4.1.1", True)
        needs_upgrade("Beta-4.1.0", "Beta-4.1.1", True)
        needs_upgrade("4.1.1-Foo", "4.1.1", True)
        needs_upgrade("4.1.1-Foo", "4.1.1-Dev", True)
        needs_upgrade("4.1.1-Foo", "4.1.2-Dev", True)
        needs_upgrade("4.1.1-Foo", "4.2.0-Dev", True)
        needs_upgrade("4.1.1-Foo", "4.2", True)
        needs_upgrade("4.1.1-Foo", "5.0", True)
        needs_upgrade("v.4.1.1-Foo", "5.0", True)
        # Additions post git-describe
        needs_upgrade("v.4.1.1-Foo", "5.0", True)
        needs_upgrade("v4.1.1-Foo", "5.0", True)
        needs_upgrade("Beta-v4.1.1-Foo", "5.0", True)
        needs_upgrade("A1-4.1.1-Foo", "5.0", True)
        needs_upgrade("A1-v4.1.1-Foo", "5.0", True)
    else:
        try:
            rv = int(needs_upgrade(args[0], args[1], True))
        except:
            rv = 2
            print("""    %s [--quiet] client_version server_version
or: %s [--quiet] --test """ % (sys.argv[0], sys.argv[0]))
        sys.exit(rv)