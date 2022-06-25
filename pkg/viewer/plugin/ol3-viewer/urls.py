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

from django.conf.urls import url

from ode_viewer import views

urlpatterns = [
    # general entry point for Bhojpur ODE viewer app
    url(r'^$', views.index, name='ode_viewer_index'),
    url(r'^persist_rois/?$', views.persist_rois,
        name='ode_viewer_persist_rois'),
    url(r'^image_data/(?P<image_id>[0-9]+)/$', views.image_data,
        name='ode_viewer_image_data'),
    url(r'^image_data/(?P<image_id>[0-9]+)/delta_t/$', views.delta_t_data,
        name='ode_viewer_image_data_deltat'),
    # load image_data for image linked to an ROI or Shape
    url(r'^(?P<obj_type>(roi|shape))/(?P<obj_id>[0-9]+)/image_data/$',
        views.roi_image_data, name='ode_viewer_roi_image_data'),
    url(r'^save_projection/?$', views.save_projection,
        name='ode_viewer_save_projection'),
    url(r'^well_images/?$', views.well_images,
        name='ode_viewer_well_images'),
    url(r'^get_intensity/?$', views.get_intensity,
        name='ode_viewer_get_intensity'),
    url(r'^shape_stats/?$', views.shape_stats,
        name='ode_viewer_shape_stats'),
    # optional z or t range e.g. iid/0-10/2-5/
    url(r'^rois_by_plane/(?P<image_id>[0-9]+)/'
        r'(?P<the_z>[0-9]+)(?:-(?P<z_end>[0-9]+))?/'
        r'(?P<the_t>[0-9:]+)(?:-(?P<t_end>[0-9]+))?/$',
        views.rois_by_plane, name='ode_viewer_rois_by_plane'),
    url(r'^plane_shape_counts/(?P<image_id>[0-9]+)/$',
        views.plane_shape_counts, name='ode_viewer_plane_shape_counts'),
    # Find the index of an ROI within all ROIs for the Image (for pagination)
    url(r'^(?P<obj_type>(roi|shape))/(?P<obj_id>[0-9]+)/page_data/$',
        views.roi_page_data, name='ode_viewer_roi_page_data'),
]