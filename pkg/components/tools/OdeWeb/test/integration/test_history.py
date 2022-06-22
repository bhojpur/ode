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

"""Tests display of data in History page."""

from engine.testlib import IWebTest
from engine.testlib import get, post
from datetime import datetime
from django.core.urlresolvers import reverse

class TestHistory(IWebTest):
    """Tests display of data in History page."""

    def test_history(self):
        """Test /webclient/history/ page."""
        request_url = reverse("load_template", args=["history"])
        response = get(self.django_client, request_url)
        assert "history_calendar" in response.content

    def test_calendar_default(self):
        """Test display of new Project in today's history page."""
        calendar_url = reverse("load_calendar")
        response = get(self.django_client, calendar_url)
        # Calendar is initially empty (no 'Project' icon)
        assert "folder16.png" not in response.content

        # Add Project
        request_url = reverse("manage_action_containers",
                              args=["addnewcontainer"])
        data = {
            'folder_type': 'project',
            'name': 'foobar'
        }
        response = post(self.django_client, request_url, data)

        # Default calendar loads calendar for current month
        response = get(self.django_client, calendar_url)
        # Now contains icon for Project
        assert "folder16.png" in response.content

    def test_calendar_month(self):
        """Test loading of calendar, specifying this month."""
        now = datetime.now()
        calendar_url = reverse("load_calendar", args=[now.year, now.month])
        print('calendar_url', calendar_url)
        response = get(self.django_client, calendar_url)
        # Calendar is initially empty (no 'Dataset' icon)
        assert "folder_image16.png" not in response.content

        # Add Dataset
        request_url = reverse("manage_action_containers",
                              args=["addnewcontainer"])
        data = {
            'folder_type': 'dataset',
            'name': 'foobar'
        }
        response = post(self.django_client, request_url, data)

        # Now contains icon for Dataset
        response = get(self.django_client, calendar_url)
        assert "folder_image16.png" in response.content
