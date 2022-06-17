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

if __name__ == "__main__":

    import sys
    import Ice
    import ode
    import ode.clients
    import ode.tables
    from ode.util import Dependency

    # Logging hack
    ode.tables.TablesI.__module__ = "ode.tables"
    ode.tables.TableI.__module__ = "ode.tables"

    class TablesDependency(Dependency):

        def __init__(self):
            Dependency.__init__(self, "tables")

        def get_version(self, target):
            self.target = target
            ver = "%s, hdf=%s" % (target.__version__, self.optional("hdf5", 1))
            return ver

        def optional(self, key, idx):
            try:
                x = self.target.whichLibVersion(key)
                if x is not None:
                    return x[idx]
                else:
                    return "unknown"
            except:
                return "error"

    app = ode.util.Server(
        ode.tables.TablesI, "TablesAdapter", Ice.Identity("Tables", ""),
        dependencies=(Dependency("numpy"), TablesDependency()))

    sys.exit(app.main(sys.argv))