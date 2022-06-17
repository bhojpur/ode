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

"""
Decorators for use with the webclient application.
"""

import logging

import engine.decorators
from ode import constants

from django.http import HttpResponse
from django.conf import settings
from django.urls import reverse
from django.urls import NoReverseMatch

from engine.webclient.forms import GlobalSearchForm
from engine.utils import reverse_with_params

logger = logging.getLogger(__name__)

class login_required(engine.decorators.login_required):
    """
    webclient specific extension of the Bhojpur ODE web login_required() decorator.
    """

    def __init__(
        self,
        ignore_login_fail=False,
        setGroupContext=False,
        login_redirect=None,
        **kwargs
    ):
        """
        Initialises the decorator.
        """
        super(login_required, self).__init__(**kwargs)
        self.ignore_login_fail = ignore_login_fail
        self.setGroupContext = setGroupContext
        self.login_redirect = login_redirect

    def on_logged_in(self, request, conn):
        """Called whenever the users is successfully logged in."""
        super(login_required, self).on_logged_in(request, conn)
        self.prepare_session(request)
        if self.setGroupContext:
            if request.session.get("active_group"):
                conn.SERVICE_OPTS.setOdeGroup(request.session.get("active_group"))
            else:
                conn.SERVICE_OPTS.setOdeGroup(conn.getEventContext().groupId)

    def on_not_logged_in(self, request, url, error=None):
        """
        This can be used to fail silently (not return 403, 500 etc. E.g.
        keepalive ping)
        """
        if self.ignore_login_fail:
            return HttpResponse("Connection Failed")
        if self.login_redirect is not None:
            try:
                url = reverse(self.login_redirect)
            except Exception:
                pass
        return super(login_required, self).on_not_logged_in(request, url, error)

    def prepare_session(self, request):
        """Prepares various session variables."""
        changes = False
        if request.session.get("callback") is None:
            request.session["callback"] = dict()
            changes = True
        if request.session.get("shares") is None:
            request.session["shares"] = dict()
            changes = True
        if changes:
            request.session.modified = True


class render_response(engine.decorators.render_response):
    """
    Subclass for adding additional data to the 'context' dict passed to
    templates
    """

    def prepare_context(self, request, context, *args, **kwargs):
        """
        This allows templates to access the current eventContext and user from
        the L{ode.gateway.OdeGateway}.
        E.g. <h1>{{ ode.user.getFullName }}</h1>
        If these are not required by the template, then they will not need to
        be loaded by the ODE Gateway.
        The results are cached by ODE Gateway, so repeated calls have no
        additional cost.
        We also process some values from settings and add these to the
        context.
        """

        super(render_response, self).prepare_context(request, context, *args, **kwargs)

        # we expect @login_required to pass us 'conn', but just in case...
        if "conn" not in kwargs:
            return
        conn = kwargs["conn"]

        # ode constants
        context["ode"] = {
            "constants": {
                "NSCOMPANIONFILE": constants.namespaces.NSCOMPANIONFILE,
                "ORIGINALMETADATA": constants.annotation.file.ORIGINALMETADATA,
                "NSCLIENTMAPANNOTATION": constants.metadata.NSCLIENTMAPANNOTATION,
            }
        }

        context.setdefault("ode", {})  # don't overwrite existing Bhojpur ODE
        public_user = engine.decorators.is_public_user(request)
        if public_user is not None:
            context["ode"]["is_public_user"] = public_user
        context["ode"]["is_admin"] = conn.getEventContext().isAdmin
        context["ode"]["user"] = conn.getUser
        # The currently logged-in user ID
        context["ode"]["user_id"] = conn.getUserId()
        # group_id never set and never used
        context["ode"]["group_id"] = request.session.get("group_id", None)
        context["ode"]["active_group"] = request.session.get(
            "active_group", conn.getEventContext().groupId
        )
        context["ode"]["active_user"] = request.session.get("user_id", conn.getUserId())
        context["global_search_form"] = GlobalSearchForm()
        context["ode"]["can_create"] = request.session.get("can_create", True)
        # UI server preferences
        if request.session.get("server_settings"):
            context["ode"]["email"] = request.session.get("server_settings").get(
                "email", False
            )
            if request.session.get("server_settings").get("ui"):
                # don't overwrite existing ui
                context.setdefault("ui", {"tree": {}})
                context["ui"]["orphans"] = (
                    request.session.get("server_settings")
                    .get("ui", {})
                    .get("tree", {})
                    .get("orphans")
                )
                context["ui"]["dropdown_menu"] = (
                    request.session.get("server_settings")
                    .get("ui", {})
                    .get("menu", {})
                    .get("dropdown")
                )
                context["ui"]["tree"]["type_order"] = (
                    request.session.get("server_settings")
                    .get("ui", {})
                    .get("tree", {})
                    .get("type_order")
                )

        self.load_settings(request, context, conn)

    def load_settings(self, request, context, conn):

        # Process various settings and add to the template context dict
        ping_interval = settings.PING_INTERVAL
        if ping_interval > 0:
            context["ping_interval"] = ping_interval

        top_links = settings.TOP_LINKS
        links = []
        for tl in top_links:
            if len(tl) < 2:
                continue
            link = {}
            link["label"] = tl[0]
            link_id = tl[1]
            try:
                # test if complex dictionary view with args and query_string
                link["link"] = reverse_with_params(**link_id)
            except TypeError:
                # assume is only view name
                try:
                    link["link"] = reverse(link_id)
                except NoReverseMatch:
                    # assume we've been passed a url
                    link["link"] = link_id
            # simply add optional attrs dict
            if len(tl) > 2:
                link["attrs"] = tl[2]
            links.append(link)
        context["ode"]["top_links"] = links

        if settings.TOP_LOGO:
            context["ode"]["logo_src"] = settings.TOP_LOGO
        if settings.TOP_LOGO_LINK:
            context["ode"]["logo_href"] = settings.TOP_LOGO_LINK

        metadata_panes = settings.METADATA_PANES
        context["ode"]["metadata_panes"] = metadata_panes

        right_plugins = settings.RIGHT_PLUGINS
        r_plugins = []
        for rt in right_plugins:
            label = rt[0]
            include = rt[1]
            plugin_id = rt[2]
            r_plugins.append(
                {"label": label, "include": include, "plugin_id": plugin_id}
            )
        context["ode"]["right_plugins"] = r_plugins

        center_plugins = settings.CENTER_PLUGINS
        c_plugins = []
        for cp in center_plugins:
            label = cp[0]
            include = cp[1]
            plugin_id = cp[2]
            c_plugins.append(
                {"label": label, "include": include, "plugin_id": plugin_id}
            )
        context["ode"]["center_plugins"] = c_plugins

        context["ode"]["user_dropdown"] = settings.USER_DROPDOWN
        context["ode"]["login_view"] = settings.LOGIN_VIEW