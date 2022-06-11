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
from . import views

urlpatterns = [
    # Designer App
    url(r"^designer/$", views.designer, name="odeforms_designer"),
    # API
    url(r"^$", lambda x: None, name="odeforms_base"),
    # List all forms
    url(r"^list_forms/$", views.list_forms, name="odeforms_list_forms"),
    # List the forms that are assigned to the user's active group that
    # apply to the object type
    url(
        r"^list_applicable_forms/(?P<obj_type>\w+)/$",
        views.list_applicable_forms,
        name="odeforms_list_applicable_forms",
    ),
    # Get a form (latest version)
    url(r"^get_form/(?P<form_id>[\w ]+)/$", views.get_form, name="odeforms_get_form"),
    # Get data for a form (latest version) for a certain object
    url(
        r"^get_form_data/"
        r"(?P<form_id>[\w ]+)/(?P<obj_type>\w+)/(?P<obj_id>[\w ]+)/$",
        views.get_form_data,
        name="odeforms_get_form_data",
    ),
    # Get assignments (restricted to those the user can unassign)
    url(
        r"get_form_assignments/$",
        views.get_form_assignments,
        name="odeforms_get_form_assignments",
    ),
    # Get the entire history of a form including all data and the forms used
    # to enter that data
    url(
        r"^get_form_data_history/"
        r"(?P<form_id>[\w ]+)/(?P<obj_type>\w+)/(?P<obj_id>[\w ]+)/$",
        views.get_form_data_history,
        name="odeforms_get_form_data_history",
    ),
    # Get groups that the user can manage
    url(
        r"^get_managed_groups/$",
        views.get_managed_groups,
        name="odeforms_get_managed_groups",
    ),
    # Lookup usernames by uid
    url(r"^get_users/$", views.get_users, name="odeforms_get_users"),
    # Check form id ownership
    url(
        r"^get_formid_editable/(?P<form_id>[\w ]+)/$",
        views.get_formid_editable,
        name="odeforms_get_formid_editable",
    ),
    # Save a form version (potentially a new form)
    url(r"^save_form/$", views.save_form, name="odeforms_save_form"),
    # Save data for a form
    url(
        r"^save_form_data/"
        r"(?P<form_id>[\w ]+)/(?P<obj_type>\w+)/(?P<obj_id>[\w ]+)/$",
        views.save_form_data,
        name="odeforms_save_form_data",
    ),
    # Save a form assignment
    url(
        r"^save_form_assignment/$",
        views.save_form_assignment,
        name="odeforms_save_form_assignment",
    ),
]