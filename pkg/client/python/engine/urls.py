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

import logging
import pkgutil
from django.conf import settings
from django.apps import AppConfig
from django.conf.urls import url, include
from django.contrib.staticfiles.urls import staticfiles_urlpatterns
from django.shortcuts import redirect

from django.urls import reverse
from django.utils.functional import lazy
from django.views.generic import RedirectView
from django.views.decorators.cache import never_cache
from engine.webclient import views as webclient_views

logger = logging.getLogger(__name__)

# error handler
handler404 = "engine.feedback.views.handler404"
handler500 = "engine.feedback.views.handler500"

reverse_lazy = lazy(reverse, str)

def redirect_urlpatterns():
    """
    Helper function to return a URL pattern for index page http://host/.
    """
    if settings.INDEX_TEMPLATE is None:
        return [
            url(
                r"^$",
                never_cache(
                    RedirectView.as_view(url=reverse_lazy("webindex"), permanent=True)
                ),
                name="index",
            )
        ]
    else:
        return [
            url(
                r"^$",
                never_cache(
                    RedirectView.as_view(
                        url=reverse_lazy("webindex_custom"), permanent=True
                    )
                ),
                name="index",
            ),
        ]

# url patterns

urlpatterns = []

for app in settings.ADDITIONAL_APPS:
    if isinstance(app, AppConfig):
        app_config = app
    else:
        app_config = AppConfig.create(app)
    label = app_config.label

    # Depending on how we added the app to INSTALLED_APPS in settings.py,
    # include the urls the same way
    if "engine.%s" % app in settings.INSTALLED_APPS:
        urlmodule = "engine.%s.urls" % app
    else:
        urlmodule = "%s.urls" % app

    # Try to import module.urls.py if it exists (not for corsheaders etc)
    urls_found = pkgutil.find_loader(urlmodule)
    if urls_found is not None:
        try:
            __import__(urlmodule)
            if label == settings.ENGINE_ROOT_APPLICATION:
                regex = r"^"
            else:
                regex = "^%s/" % label
            urlpatterns.append(url(regex, include(urlmodule)))
        except ImportError:
            print(
                """Failed to import %s
Please check if the app is installed and the versions of the app and
ODE.web are compatible
            """
                % urlmodule
            )
            raise
    else:
        logger.debug("Module not found: %s" % urlmodule)

urlpatterns += [
    url(
        r"^favicon\.ico$",
        lambda request: redirect("%s%s" % (settings.STATIC_URL, settings.FAVICON_URL)),
    ),
    url(r"^webgateway/", include("engine.webgateway.urls")),
    url(r"^webadmin/", include("engine.webadmin.urls")),
    url(r"^webclient/", include("engine.webclient.urls")),
    url(r"^url/", include("engine.webredirect.urls")),
    url(r"^feedback/", include("engine.feedback.urls")),
    url(r"^api/", include("engine.api.urls")),
    url(r"^index/$", webclient_views.custom_index, name="webindex_custom"),
]

urlpatterns += redirect_urlpatterns()

if settings.DEBUG:
    urlpatterns += staticfiles_urlpatterns()