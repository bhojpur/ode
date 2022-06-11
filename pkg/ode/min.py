#!/usr/bin/env python
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


# This file is an import-only file providing a mechanism for other files to
# import a range of modules in a controlled way. It could be made to pass
# flake8 but given its simplicity it is being marked as noqa for now.
#
# flake8: noqa

import core
import IceImport
if core.__import_style__ is None:
    core.__import_style__ = "min"

# Internal types
IceImport.load("ode_model_NamedValue_ice")

# New Command API
IceImport.load("ode_cmd_Admin_ice")
IceImport.load("ode_cmd_API_ice")
IceImport.load("ode_cmd_Basic_ice")
IceImport.load("ode_cmd_FS_ice")
IceImport.load("ode_cmd_Graphs_ice")
IceImport.load("ode_cmd_Mail_ice")

# Previous ServiceFactory API
IceImport.load("ode_API_ice")
IceImport.load("ode_ServicesF_ice")
IceImport.load("ode_Constants_ice")

IceImport.load("Glacier2_Router_ice")

import core.rtypes