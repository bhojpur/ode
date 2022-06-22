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

# This file is an import-only file providing a mechanism for other files to
# import a range of modules in a controlled way. It could be made to pass
# flake8 but given its simplicity it is being marked as noqa for now.

# flake8: noqa

import Ice
import IceImport
import ode

if ode.__import_style__ is None:
    ode.__import_style__ = "all"
    import ode.min
    import ode.callbacks
    import ode.ObjectFactoryRegistrar
    IceImport.load("ode_FS_ice")
    IceImport.load("ode_System_ice")
    IceImport.load("ode_Collections_ice")
    IceImport.load("ode_Repositories_ice")
    IceImport.load("ode_SharedResources_ice")
    IceImport.load("ode_Scripts_ice")
    IceImport.load("ode_Tables_ice")
    IceImport.load("ode_api_IAdmin_ice")
    IceImport.load("ode_api_IConfig_ice")
    IceImport.load("ode_api_IContainer_ice")
    IceImport.load("ode_api_ILdap_ice")
    IceImport.load("ode_api_IMetadata_ice")
    IceImport.load("ode_api_IPixels_ice")
    IceImport.load("ode_api_IProjection_ice")
    IceImport.load("ode_api_IQuery_ice")
    IceImport.load("ode_api_IRenderingSettings_ice")
    IceImport.load("ode_api_IRepositoryInfo_ice")
    IceImport.load("ode_api_IRoi_ice")
    IceImport.load("ode_api_IScript_ice")
    IceImport.load("ode_api_ISession_ice")
    IceImport.load("ode_api_IShare_ice")
    IceImport.load("ode_api_ITimeline_ice")
    IceImport.load("ode_api_ITypes_ice")
    IceImport.load("ode_api_IUpdate_ice")
    IceImport.load("ode_api_Exporter_ice")
    IceImport.load("ode_api_JobHandle_ice")
    IceImport.load("ode_api_MetadataStore_ice")
    IceImport.load("ode_api_RawFileStore_ice")
    IceImport.load("ode_api_RawPixelsStore_ice")
    IceImport.load("ode_api_RenderingEngine_ice")
    IceImport.load("ode_api_Search_ice")
    IceImport.load("ode_api_ThumbnailStore_ice")
    IceImport.load("ode_cmd_Admin_ice")
    IceImport.load("ode_cmd_API_ice")
    IceImport.load("ode_cmd_Basic_ice")
    IceImport.load("ode_cmd_FS_ice")
    IceImport.load("ode_cmd_Graphs_ice")
    IceImport.load("ode_cmd_Mail_ice")
    IceImport.load("ode_model_Units_ice")

    import ode_sys_ParametersI
    import ode_model_PermissionsI