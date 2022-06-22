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

from engine.webadmin import views
from engine.webclient.views import WebclientLoginView

# url patterns
urlpatterns = [
    url(r"^$", views.index, name="waindex"),
    url(r"^login/$", WebclientLoginView.as_view(), name="walogin"),
    url(r"^logout/$", views.logout, name="walogout"),
    url(r"^forgottenpassword/$", views.forgotten_password, name="waforgottenpassword"),
    url(r"^experimenters/$", views.experimenters, name="waexperimenters"),
    url(
        r"^experimenter/(?P<action>[a-z]+)/(?:(?P<eid>[0-9]+)/)?$",
        views.manage_experimenter,
        name="wamanageexperimenterid",
    ),
    url(
        r"^change_password/(?P<eid>[0-9]+)/$",
        views.manage_password,
        name="wamanagechangepasswordid",
    ),
    url(r"^groups/$", views.groups, name="wagroups"),
    url(
        r"^group/(?P<action>(new|create|edit|save))/" "(?:(?P<gid>[0-9]+)/)?$",
        views.manage_group,
        name="wamanagegroupid",
    ),
    url(
        r"^group_owner/(?P<action>(edit|save))/(?P<gid>[0-9]+)/$",
        views.manage_group_owner,
        name="wamanagegroupownerid",
    ),
    url(r"^myaccount/(?:(?P<action>[a-z]+)/)?$", views.my_account, name="wamyaccount"),
    url(r"^stats/$", views.stats, name="wastats"),
    url(
        r"^drivespace_json/groups/$",
        views.drivespace_json,
        {"query": "groups"},
        name="waloaddrivespace_groups",
    ),
    url(
        r"^drivespace_json/users/$",
        views.drivespace_json,
        {"query": "users"},
        name="waloaddrivespace_users",
    ),
    url(
        r"^drivespace_json/group/(?P<groupId>[0-9]+)/$",
        views.drivespace_json,
        name="waloaddrivespace_group",
    ),
    url(
        r"^drivespace_json/user/(?P<userId>[0-9]+)/$",
        views.drivespace_json,
        name="waloaddrivespace_user",
    ),
    # NB: eid is not used and is optional in URL.
    # Left in place to avoid breaking change.
    url(
        r"^change_avatar/(?:(?P<eid>[0-9]+)/)?(?:(?P<action>[a-z]+)/)?$",
        views.manage_avatar,
        name="wamanageavatar",
    ),
    url(r"^myphoto/$", views.myphoto, name="wamyphoto"),
    url(r"^email/$", views.email, name="waemail"),
]