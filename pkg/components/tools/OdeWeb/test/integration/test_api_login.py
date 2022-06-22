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
Tests logging in with webgateway json api
"""

import pytest
from engine.testlib import IWebTest, get_json, _response, post
from django.core.urlresolvers import reverse, NoReverseMatch
from engine.api import api_settings
from django.test import Client
from ode_marshal import ODE_SCHEMA_URL
import json

class TestLogin(IWebTest):
    """
    Tests login workflow: getting url, csfv tokens etc.
    """

    def test_versions(self):
        """
        Start at the base url, get versions
        """
        django_client = self.django_root_client
        request_url = reverse('api_versions')
        rsp = get_json(django_client, request_url)
        versions = rsp['data']
        assert len(versions) == len(api_settings.API_VERSIONS)
        for v in versions:
            assert v['version'] in api_settings.API_VERSIONS

    def test_base_url(self):
        """
        Tests that the base url for a given version provides other urls
        """
        django_client = self.django_root_client
        # test the most recent version
        version = api_settings.API_VERSIONS[-1]
        request_url = reverse('api_base', kwargs={'api_version': version})
        rsp = get_json(django_client, request_url)
        assert 'url:servers' in rsp
        assert 'url:login' in rsp
        assert 'url:projects' in rsp
        assert 'url:datasets' in rsp
        assert 'url:images' in rsp
        assert 'url:screens' in rsp
        assert 'url:plates' in rsp
        assert 'url:save' in rsp
        assert rsp['url:schema'] == ODE_SCHEMA_URL

    def test_base_url_versions_404(self):
        """
        Tests that the base url gives 404 for invalid versions
        """
        version = api_settings.API_VERSIONS[-1] + "1"
        with pytest.raises(NoReverseMatch):
            reverse('api_base', kwargs={'api_version': version})

    def test_login_get(self):
        """
        Tests that we get a suitable message if we try to GET login_url
        """
        django_client = self.django_root_client
        version = api_settings.API_VERSIONS[-1]
        request_url = reverse('api_login', kwargs={'api_version': version})
        rsp = get_json(django_client, request_url, status_code=405)
        assert (rsp['message'] ==
                "POST only with username, password, server and csrftoken")

    def test_login_csrf(self):
        """
        Tests that we can only login with CSRF
        """
        django_client = self.django_root_client
        # test the most recent version
        version = api_settings.API_VERSIONS[-1]
        request_url = reverse('api_login', kwargs={'api_version': version})
        # POST without adding CSRF token
        rsp = _response(django_client, request_url, method='post',
                        status_code=403)
        rsp = json.loads(rsp.content)
        assert (rsp['message'] ==
                ("CSRF Error. You need to include valid CSRF tokens for any"
                 " POST, PUT, PATCH or DELETE operations."
                 " You have to include CSRF token in the POST data or"
                 " add the token to the HTTP header."))

    @pytest.mark.parametrize("credentials", [
        [{'username': 'guest', 'password': 'fake', 'server': 1},
            "Username: Guest account is not supported."],
        [{'username': 'nobody', 'password': '', 'server': 1},
            "Password: This field is required."],
        [{'password': 'fake'},
            # No username OR server. Test concatenation of 2 errors
            ("Username: This field is required. "
             "Server: This field is required.")],
        [{'username': 'nobody', 'password': 'fake', 'server': 1},
            ("Connection not available, "
             "please check your user name and password.")]
        ])
    def test_login_errors(self, credentials):
        """
        Tests that we get expected form validation errors if try to login
        without required fields, as 'guest' or with invalid username/password.
        """
        django_client = self.django_root_client
        # test the most recent version
        version = api_settings.API_VERSIONS[-1]
        request_url = reverse('api_login', kwargs={'api_version': version})
        data = credentials[0]
        message = credentials[1]
        rsp = post(django_client, request_url, data, status_code=403)
        rsp = json.loads(rsp.content)
        assert rsp['message'] == message

    def test_login_example(self):
        """
        Example of successful login as user would do for real,
        starting at base url and getting all other urls and info from there.
        """
        # Create test user and get username & password for use below
        user = self.new_user()
        username = password = user.getOdeName().val

        # Django client, not logged in yet
        django_client = Client()
        # Start at the /api/ url to list versions...
        request_url = reverse('api_versions')
        rsp = get_json(django_client, request_url)
        # Pick the last version
        version = rsp['data'][-1]
        base_url = version['url:base']
        # Base url will give a bunch of other urls
        base_rsp = get_json(django_client, base_url)
        login_url = base_rsp['url:login']
        servers_url = base_rsp['url:servers']
        login_url = base_rsp['url:login']
        token_url = base_rsp['url:token']
        # See what servers we can log in to
        servers_rsp = get_json(django_client, servers_url)
        server_id = servers_rsp['data'][0]['id']
        # Need a CSRF token
        token_rsp = get_json(django_client, token_url)
        token = token_rsp['data']
        # Can also get this from our session cookies
        csrf_token = django_client.cookies['csrftoken'].value
        assert token == csrf_token
        # Now we have all info we need for login.
        # Set the header, so we don't need to do this for every POST/PUT/DELETE
        # OR we could add it to each POST as 'csrfmiddlewaretoken'
        django_client = Client(HTTP_X_CSRFTOKEN=token)
        data = {
            'username': username,
            'password': password,
            'server': server_id,
            # 'csrfmiddlewaretoken': token,
        }
        login_rsp = django_client.post(login_url, data)
        login_json = json.loads(login_rsp.content)
        assert login_json['success']
        event_context = login_json['eventContext']
        # eventContext gives a bunch of info
        member_of_groups = event_context['memberOfGroups']
        current_group = event_context['groupId']
        user_id = event_context['userId']
        assert len(member_of_groups) == 2      # includes 'user' group
        assert current_group in member_of_groups
        assert user_id > 0
