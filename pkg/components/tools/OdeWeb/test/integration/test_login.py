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
Tests webclient login
"""

from django.conf import settings
from django.conf.urls import url
from django.utils.importlib import import_module
from django.test.utils import override_settings

from engine.webclient.views import WebclientLoginView

from engine.testlib import IWebTest, post, get
from django.core.urlresolvers import reverse
from django.test import Client
from random import random
import pytest

tag_url = reverse('load_template', kwargs={'menu': 'usertags'})

class CustomWebclientLoginView(WebclientLoginView):
    pass

urlpatterns = import_module(settings.ROOT_URLCONF).urlpatterns
urlpatterns += [
    url(r'^test_login/$',
        CustomWebclientLoginView.as_view(), name="test_weblogin"),
]


class TestLogin(IWebTest):
    """
    Tests login
    """

    @pytest.mark.parametrize("credentials", [
        [{'username': 'guest', 'password': 'fake', 'server': 1},
            "Guest account is not supported."],
        [{'username': 'nobody', 'password': '', 'server': 1},
            "This field is required."],
        [{'password': 'fake'},
            "This field is required."],
        [{'username': 'g', 'password': str(random()), 'server': 1},
            "please check your user name and password."]
        ])
    def test_login_errors(self, credentials):
        """
        Tests handling of various login errors.
        E.g. missing fields, invalid credentials and guest login
        """
        django_client = self.django_root_client
        request_url = reverse('weblogin')
        data = credentials[0]
        data['server'] = 1
        message = credentials[1]
        rsp = post(django_client, request_url, data, status_code=200)
        assert message in rsp.content

    def test_get_login_page(self):
        """
        Simply test if a GET of the login url returns login page
        """
        django_client = Client()
        request_url = reverse('weblogin')
        rsp = get(django_client, request_url, {}, status_code=200)
        assert 'ODE.web - Login' in rsp.content

    @pytest.mark.parametrize("redirect", ['', tag_url])
    def test_login_redirect(self, redirect):
        """
        Test that a successful login redirects to /webclient/
        or to specified url
        """
        django_client = self.django_root_client
        # redirect = reverse('load_template', kwargs={'menu': 'usertags'})
        request_url = "%s?url=%s" % (reverse('weblogin'), redirect)
        data = {'username': self.ctx.userName,
                'password': self.ctx.userName,
                'server': 1}
        rsp = post(django_client, request_url, data, status_code=302)
        if len(redirect) == 0:
            redirect = reverse('webindex')
        assert rsp['Location'].endswith(redirect)

    @override_settings(ROOT_URLCONF=__name__, LOGIN_VIEW='test_weblogin')
    def test_login_view(self):
        """
        Test that a successful logout redirects to custom login view
        """
        django_client = self.django_root_client
        request_url = reverse('test_weblogin')
        data = {
            'server': 1,
            'username': self.ctx.userName,
            'password': self.ctx.userName,
        }
        rsp = post(django_client, request_url, data, status_code=302)
        request_url = reverse('weblogout')
        rsp = post(django_client, request_url, {}, status_code=302)
        assert rsp['Location'].endswith(reverse('test_weblogin'))
        assert not rsp['Location'].endswith(reverse('weblogin'))
