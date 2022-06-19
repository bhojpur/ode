package ode.services.roi;

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

import java.io.File;

import ode.services.scripts.ScriptFinder;
import ode.services.util.Executor;
import ode.system.Principal;
import ode.system.Roles;

/**
 * Start-up task which guarantees that lib/python/populateroi.py is added as a
 * script to the server. Then, users like MetadataStoreI who would like to run
 * populateroi.py scripts, can use {@link #createJob(ServiceFactory)}
 */
public class PopulateRoiJob extends ScriptFinder {

    private static File production() {
        File cwd = new File(".");
        File lib = new File(cwd, "lib");
        File scripts = new File(lib, "scripts");
        File ode = new File(scripts, "ode");
        File import_scripts = new File(ode, "import_scripts");
        File Populate_ROI = new File(import_scripts, "Populate_ROI.py");
        return Populate_ROI;
    }

    public PopulateRoiJob(Roles roles, String uuid, Executor executor) {
        super(roles, uuid, executor, production());
    }

    public PopulateRoiJob(Roles roles, String uuid, Executor executor, File source) {
        super(roles, new Principal(uuid, "system", "Internal"), executor, source);
    }

    public PopulateRoiJob(Roles roles, Principal principal, Executor executor, File source) {
        super(roles, principal, executor, source);
    }

    @Override
    public String getName() {
        return "Populate_ROI.py";
    }
}