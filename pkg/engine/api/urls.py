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

"""Handles all 'api' urls."""

from django.conf.urls import url
from engine.api import views
from engine.webgateway.views import LoginView
from . import api_settings
import re

versions = "|".join([re.escape(v) for v in api_settings.API_VERSIONS])

api_versions = url(r"^$", views.api_versions, name="api_versions")

api_base = url(r"^v(?P<api_version>%s)/$" % versions, views.api_base, name="api_base")
"""
GET various urls listed below
"""

api_token = url(
    r"^v(?P<api_version>%s)/token/$" % versions, views.api_token, name="api_token"
)
"""
GET the CSRF token for this session. Needs to be included
in header with all POST, PUT & DELETE requests
"""

api_servers = url(
    r"^v(?P<api_version>%s)/servers/$" % versions, views.api_servers, name="api_servers"
)
"""
GET list of available Bhojpur ODE servers to login to.
"""

api_login = url(
    r"^v(?P<api_version>%s)/login/$" % versions, LoginView.as_view(), name="api_login"
)
"""
Login to Bhojpur ODE. POST with 'username', 'password' and 'server' index
"""

api_save = url(
    r"^v(?P<api_version>%s)/m/save/$" % versions,
    views.SaveView.as_view(),
    name="api_save",
)
"""
POST to create a new object or PUT to update existing object.
In both cases content body encodes json data.
"""

api_projects = url(
    r"^v(?P<api_version>%s)/m/projects/$" % versions,
    views.ProjectsView.as_view(),
    name="api_projects",
)
"""
GET all projects, using ode-marshal to generate json
"""

api_project = url(
    r"^v(?P<api_version>%s)/m/projects/(?P<object_id>[0-9]+)/$" % versions,
    views.ProjectView.as_view(),
    name="api_project",
)
"""
Project url to GET or DELETE a single Project
"""

api_datasets = url(
    r"^v(?P<api_version>%s)/m/datasets/$" % versions,
    views.DatasetsView.as_view(),
    name="api_datasets",
)
"""
GET all datasets, using ode-marshal to generate json
"""

api_project_datasets = url(
    r"^v(?P<api_version>%s)/m/projects/" "(?P<project_id>[0-9]+)/datasets/$" % versions,
    views.DatasetsView.as_view(),
    name="api_project_datasets",
)
"""
GET Datasets in Project, using ode-marshal to generate json
"""

api_dataset = url(
    r"^v(?P<api_version>%s)/m/datasets/(?P<object_id>[0-9]+)/$" % versions,
    views.DatasetView.as_view(),
    name="api_dataset",
)
"""
Dataset url to GET or DELETE a single Dataset
"""

api_images = url(
    r"^v(?P<api_version>%s)/m/images/$" % versions,
    views.ImagesView.as_view(),
    name="api_images",
)
"""
GET all images, using ode-marshal to generate json
"""

api_dataset_images = url(
    r"^v(?P<api_version>%s)/m/datasets/" "(?P<dataset_id>[0-9]+)/images/$" % versions,
    views.ImagesView.as_view(),
    name="api_dataset_images",
)
"""
GET Images in Dataset, using ode-marshal to generate json
"""

api_dataset_projects = url(
    r"^v(?P<api_version>%s)/m/datasets/" "(?P<dataset_id>[0-9]+)/projects/$" % versions,
    views.ProjectsView.as_view(),
    name="api_dataset_projects",
)
"""
GET Projects that contain a Dataset, using ode-marshal to generate json
"""

api_image = url(
    r"^v(?P<api_version>%s)/m/images/(?P<object_id>[0-9]+)/$" % versions,
    views.ImageView.as_view(),
    name="api_image",
)
"""
Image url to GET or DELETE a single Image
"""

api_image_datasets = url(
    r"^v(?P<api_version>%s)/m/images/" "(?P<image_id>[0-9]+)/datasets/$" % versions,
    views.DatasetsView.as_view(),
    name="api_image_datasets",
)
"""
GET Datasets that contain an Image, using ode-marshal to generate json
"""

api_screen = url(
    r"^v(?P<api_version>%s)/m/screens/(?P<object_id>[0-9]+)/$" % versions,
    views.ScreenView.as_view(),
    name="api_screen",
)
"""
Screen url to GET or DELETE a single Screen
"""

api_screens = url(
    r"^v(?P<api_version>%s)/m/screens/$" % versions,
    views.ScreensView.as_view(),
    name="api_screens",
)
"""
GET all screens, using ode-marshal to generate json
"""

api_plates = url(
    r"^v(?P<api_version>%s)/m/plates/$" % versions,
    views.PlatesView.as_view(),
    name="api_plates",
)
"""
GET all plates, using ode-marshal to generate json
"""

api_screen_plates = url(
    r"^v(?P<api_version>%s)/m/screens/" "(?P<screen_id>[0-9]+)/plates/$" % versions,
    views.PlatesView.as_view(),
    name="api_screen_plates",
)
"""
GET Plates in Screen, using ode-marshal to generate json
"""

api_well_plates = url(
    r"^v(?P<api_version>%s)/m/wells/" "(?P<well_id>[0-9]+)/plates/$" % versions,
    views.PlatesView.as_view(),
    name="api_well_plates",
)
"""
GET Plates that contain a Well, using ode-marshal to generate json
"""

api_plate = url(
    r"^v(?P<api_version>%s)/m/plates/(?P<object_id>[0-9]+)/$" % versions,
    views.PlateView.as_view(),
    name="api_plate",
)
"""
Plate url to GET or DELETE a single Plate
"""

api_wells = url(
    r"^v(?P<api_version>%s)/m/wells/$" % versions,
    views.WellsView.as_view(),
    name="api_wells",
)
"""
GET all wells, using ode-marshal to generate json
"""

api_plate_plateacquisitions = url(
    r"^v(?P<api_version>%s)/m/plates/"
    "(?P<plate_id>[0-9]+)/plateacquisitions/$" % versions,
    views.PlateAcquisitionsView.as_view(),
    name="api_plate_plateacquisitions",
)
"""
GET PlateAcquisitions in Plate, using ode-marshal to generate json
"""

api_plateacquisition = url(
    r"^v(?P<api_version>%s)/m/plateacquisitions/" "(?P<object_id>[0-9]+)/$" % versions,
    views.PlateAcquisitionView.as_view(),
    name="api_plateacquisition",
)
"""
Well url to GET or DELETE a single Well
"""

api_plateacquisition_wellsampleindex_wells = url(
    r"^v(?P<api_version>%s)/m/plateacquisitions/"
    "(?P<plateacquisition_id>[0-9]+)/wellsampleindex/"
    "(?P<index>[0-9]+)/wells/$" % versions,
    views.WellsView.as_view(),
    name="api_plateacquisition_wellsampleindex_wells",
)
"""
GET Wells from a single Index in PlateAcquisition
"""

api_plate_wellsampleindex_wells = url(
    r"^v(?P<api_version>%s)/m/plates/"
    "(?P<plate_id>[0-9]+)/wellsampleindex/"
    "(?P<index>[0-9]+)/wells/$" % versions,
    views.WellsView.as_view(),
    name="api_plate_wellsampleindex_wells",
)
"""
GET Wells from a single Index in Plate
"""

api_plate_wells = url(
    r"^v(?P<api_version>%s)/m/plates/" "(?P<plate_id>[0-9]+)/wells/$" % versions,
    views.WellsView.as_view(),
    name="api_plate_wells",
)
"""
GET Wells in Plate, using ode-marshal to generate json
"""

api_plateacquisition_wells = url(
    r"^v(?P<api_version>%s)/m/plateacquisitions/"
    "(?P<plateacquisition_id>[0-9]+)/wells/$" % versions,
    views.WellsView.as_view(),
    name="api_plateacquisition_wells",
)
"""
GET Wells in Plate, using ode-marshal to generate json
"""

api_well = url(
    r"^v(?P<api_version>%s)/m/wells/(?P<object_id>[0-9]+)/$" % versions,
    views.WellView.as_view(),
    name="api_well",
)
"""
Well url to GET or DELETE a single Well
"""

api_plate_screens = url(
    r"^v(?P<api_version>%s)/m/plates/" "(?P<plate_id>[0-9]+)/screens/$" % versions,
    views.ScreensView.as_view(),
    name="api_plate_screens",
)
"""
GET Screens that contain a Plate, using ode-marshal to generate json
"""

api_rois = url(
    r"^v(?P<api_version>%s)/m/rois/$" % versions,
    views.RoisView.as_view(),
    name="api_rois",
)
"""
GET all rois, using ode-marshal to generate json
"""

api_roi = url(
    r"^v(?P<api_version>%s)/m/rois/(?P<object_id>[0-9]+)/$" % versions,
    views.RoiView.as_view(),
    name="api_roi",
)
"""
ROI url to GET or DELETE a single ROI
"""

api_image_rois = url(
    r"^v(?P<api_version>%s)/m/images/(?P<image_id>[0-9]+)/rois/$" % versions,
    views.RoisView.as_view(),
    name="api_image_rois",
)
"""
GET ROIs that belong to an Image, using ode-marshal to generate json
"""

api_shapes = url(
    r"^v(?P<api_version>%s)/m/shapes/$" % versions,
    views.ShapesView.as_view(),
    name="api_shapes",
)
"""
GET all Shapes, using ode-marshal to generate json
"""

api_shape = url(
    r"^v(?P<api_version>%s)/m/shapes/(?P<object_id>[0-9]+)/$" % versions,
    views.ShapeView.as_view(),
    name="api_shape",
)
"""
Shape url to GET or DELETE a single Shape
"""

api_experimenters = url(
    r"^v(?P<api_version>%s)/m/experimenters/$" % versions,
    views.ExperimentersView.as_view(),
    name="api_experimenters",
)
"""
GET Experimenters, using ode-marshal to generate json
"""

api_experimenter = url(
    r"^v(?P<api_version>%s)/m/experimenters/" "(?P<object_id>[0-9]+)/$" % versions,
    views.ExperimenterView.as_view(),
    name="api_experimenter",
)
"""
GET Experimenter, using ode-marshal to generate json
"""

api_group_experimenters = url(
    r"^v(?P<api_version>%s)/m/experimentergroups/(?P<group_id>[0-9]+)"
    "/experimenters/$" % versions,
    views.ExperimentersView.as_view(),
    name="api_experimentergroup_experimenters",
)
"""
GET Experimenters in a Group, using ode-marshal to generate json
"""

api_groups = url(
    r"^v(?P<api_version>%s)/m/experimentergroups/$" % versions,
    views.ExperimenterGroupsView.as_view(),
    name="api_experimentergroups",
)
"""
GET ExperimenterGroups, using ode-marshal to generate json
"""

api_group = url(
    r"^v(?P<api_version>%s)/m/experimentergroups/" "(?P<object_id>[0-9]+)/$" % versions,
    views.ExperimenterGroupView.as_view(),
    name="api_experimentergroup",
)
"""
GET ExperimenterGroup, using ode-marshal to generate json
"""

api_experimenter_groups = url(
    r"^v(?P<api_version>%s)/m/experimenters/(?P<experimenter_id>[0-9]+)"
    "/experimentergroups/$" % versions,
    views.ExperimenterGroupsView.as_view(),
    name="api_experimenter_experimentergroups",
)
"""
GET Groups that an Experimenter is in, using ode-marshal to generate json
"""

urlpatterns = [
    api_versions,
    api_base,
    api_token,
    api_servers,
    api_login,
    api_save,
    api_projects,
    api_project,
    api_datasets,
    api_project_datasets,
    api_dataset,
    api_images,
    api_dataset_images,
    api_dataset_projects,
    api_image,
    api_image_datasets,
    api_screen,
    api_screens,
    api_plates,
    api_screen_plates,
    api_well_plates,
    api_plate,
    api_wells,
    api_plate_plateacquisitions,
    api_plateacquisition,
    api_plateacquisition_wellsampleindex_wells,
    api_plate_wellsampleindex_wells,
    api_plate_wells,
    api_plateacquisition_wells,
    api_well,
    api_plate_screens,
    api_rois,
    api_roi,
    api_image_rois,
    api_shapes,
    api_shape,
    api_experimenters,
    api_experimenter,
    api_group_experimenters,
    api_groups,
    api_group,
    api_experimenter_groups,
]