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
Simple integration tests to ensure that the CSRF middleware is enabled and
working correctly.
"""

import ode
import ode.clients
from ode.rtypes import rstring
from engine.testlib import IWebTest
from engine.testlib import post, get, _response, csrf_response

from django.test import Client
from django.core.urlresolvers import reverse

try:
    from PIL import Image, ImageDraw  # see ticket:2597
except ImportError:  # see ticket:2597
    import Image
    import ImageDraw

from random import randint as rint
import tempfile


class TestCsrf(IWebTest):
    """
    Tests to ensure that Django CSRF middleware for ODE.web is enabled and
    working correctly.
    """

    def image_with_channels(self):
        """
        Returns a new foundational Image with Channel objects attached for
        view method testing.
        """
        pixels = self.create_pixels(client=self.client)
        for the_c in range(pixels.getSizeC().val):
            channel = ode.model.ChannelI()
            channel.logicalChannel = ode.model.LogicalChannelI()
            pixels.addChannel(channel)
        image = pixels.getImage()
        return self.sf.getUpdateService().saveAndReturnObject(image)

    def new_tag(self):
        """
        Returns a new Tag objects
        """
        tag = ode.model.TagAnnotationI()
        tag.textValue = rstring(self.uuid())
        tag.ns = rstring("pytest")
        return self.sf.getUpdateService().saveAndReturnObject(tag)

    # Client
    def test_csrf_middleware_enabled(self):
        """
        If the CSRF middleware is enabled login attempts that do not include
        the CSRF token should fail with an HTTP 403 (forbidden) status code.
        """
        # https://docs.djangoproject.com/en/dev/ref/contrib/csrf/#testing
        django_client = Client(enforce_csrf_checks=True)

        data = {
            'server': 1,
            'username': self.client.getProperty('ode.user'),
            'password': self.client.getProperty('ode.pass')
        }
        login_url = reverse('weblogin')
        _response(django_client, login_url, 'post', data=data, status_code=403)

        logout_url = reverse('weblogout')
        _response(django_client, logout_url, 'post', status_code=403)

    def test_forgot_password(self):

        request_url = reverse('waforgottenpassword')
        data = {
            'username': "odename",
            'email': "email"
        }
        post(self.django_client, request_url, data)

    def test_move_data(self):

        group_id = self.new_group(experimenters=[self.user]).id.val

        request_url = reverse('chgrp')
        data = {
            'image': self.image_with_channels().id.val,
            'group_id': group_id
        }

        post(self.django_root_client, request_url, data)

    def test_add_and_remove_comment(self):

        request_url = reverse('annotate_comment')
        data = {
            'comment': 'foobar',
            'image': self.image_with_channels().id.val
        }
        post(self.django_client, request_url, data)

        # Remove comment, see remove tag,
        # http://localhost/webclient/action/remove/[comment|tag|file]/ID/

    def test_attach_file(self):

        # Due to EOF both posts must be test separately
        # Bad post
        img = self.image_with_channels()
        try:
            temp = tempfile.NamedTemporaryFile(suffix='.csrf')
            temp.write("Testing without csrf token")
            temp.seek(0)

            request_url = reverse('annotate_file')
            data = {
                'image': img.id.val,
                'index': 0,
                'annotation_file': temp
            }
            post(self.django_client, request_url, data)
        finally:
            temp.close()

        # Good post
        try:
            temp = tempfile.NamedTemporaryFile(suffix='.csrf')
            temp.write("Testing csrf token")
            temp.seek(0)

            request_url = reverse('annotate_file')
            data = {
                'image': img.id.val,
                'index': 0,
                'annotation_file': temp
            }
            post(self.django_client, request_url, data)
        finally:
            temp.close()

        # link existing annotation is handled by the same request.

        # Remove file, see remove tag,
        # http://localhost/webclient/action/remove/[comment|tag|file]/ID/

    def test_edit_channel_names(self):

        """
        CSRF protection does not check `GET` requests so we need to be sure
        that this request results in an HTTP 405 (method not allowed) status
        code.
        """
        img = self.image_with_channels()
        data = {'channel0': 'foobar'}
        request_url = reverse(
            'edit_channel_names', args=[img.id.val]
        )
        get(self.django_client, request_url, data, status_code=405, csrf=True)
        post(self.django_client, request_url, data)

    def test_copy_past_rendering_settings(self):

        """
        CSRF protection does not check `GET` requests so we need to be sure
        that this request results in an HTTP 405 (method not allowed) status
        code.
        """

        img = self.create_test_image(session=self.sf)

        # put image id into session
        session = self.django_client.session
        session['fromid'] = img.id.val
        session.save()

        request_url = reverse('webgateway.views.copy_image_rdef_json')
        data = {
            'toids': img.id.val
        }

        post(self.django_client, request_url, data)
        get(self.django_client, request_url, data, status_code=405, csrf=True)

    def test_reset_rendering_settings(self):

        """
        CSRF protection does not check `GET` requests so we need to be sure
        that this request results in an HTTP 405 (method not allowed) status
        code.
        """

        img = self.create_test_image(session=self.sf)

        # Reset through webclient as it is calling directly
        # webgateway.reset_image_rdef_json
        request_url = reverse('reset_rdef_json')
        data = {
            'toids': img.id.val,
            'to_type': 'image'
        }

        post(self.django_client, request_url, data)
        get(self.django_client, request_url, data, status_code=405)
        get(self.django_client, request_url, data, status_code=405, csrf=True)

    def test_apply_owners_rendering_settings(self):

        img = self.create_test_image(session=self.sf)

        request_url = reverse('reset_owners_rdef_json')
        data = {
            'toids': img.id.val,
            'to_type': 'image'
        }

        post(self.django_client, request_url, data)

    def test_ode_tiff_script(self):

        """
        CSRF protection does not check `GET` requests so we need to be sure
        that this request results in an HTTP 405 (method not allowed) status
        code.
        """

        img = self.create_test_image(session=self.sf)

        request_url = reverse('ode_tiff_script', args=[img.id.val])

        post(self.django_client, request_url, {})
        get(self.django_client, request_url, status_code=405)

    def test_script(self):

        img = self.create_test_image(session=self.sf)

        script_path = "ode/export_scripts/Batch_Image_Export.py"
        script = self.sf.getScriptService().getScriptID(script_path)

        request_url = reverse('script_run', args=[script])
        data = {
            "Data_Type": "Image",
            "IDs": img.id.val,
            "Choose_T_Section": "Default-T (last-viewed)",
            "Choose_Z_Section": "Default-Z (last-viewed)",
            "Export_Individual_Channels": "on",
            "Export_Merged_Image": "on",
            "Folder_Name": "Batch_Image_Export",
            "Format": "JPEG",
            "Zoom": "100%"
        }
        post(self.django_client, request_url, data)

    # ADMIN
    def test_myaccount(self):

        request_url = reverse('wamyaccount', args=["save"])
        data = {
            "odename": self.user.odeName.val,
            "first_name": self.user.odeName.val,
            "last_name": self.user.lastName.val,
            "institution": "foo bar",
            "default_group": self.group.id.val
        }
        post(self.django_client, request_url, data, status_code=302)

    def test_avatar(self):

        user_id = self.user.id.val

        # Due to EOF both posts must be test separately
        # Bad post
        try:
            temp = tempfile.NamedTemporaryFile(suffix='.png')

            img = Image.new("RGB", (200, 200), "#FFFFFF")
            draw = ImageDraw.Draw(img)

            r, g, b = rint(0, 255), rint(0, 255), rint(0, 255)
            for i in range(200):
                draw.line((i, 0, i, 200), fill=(int(r), int(g), int(b)))
            img.save(temp, "PNG")
            temp.seek(0)

            request_url = reverse('wamanageavatar', args=[user_id, "upload"])
            data = {
                'filename': 'avatar.png',
                "photo": temp
            }
            csrf_response(self.django_client, request_url, 'post', data,
                          status_code=302, test_csrf_required=False)
        finally:
            temp.close()

        # Good post
        try:
            temp = tempfile.NamedTemporaryFile(suffix='.png')

            img = Image.new("RGB", (200, 200), "#FFFFFF")
            draw = ImageDraw.Draw(img)

            r, g, b = rint(0, 255), rint(0, 255), rint(0, 255)
            for i in range(200):
                draw.line((i, 0, i, 200), fill=(int(r), int(g), int(b)))
            img.save(temp, "PNG")
            temp.seek(0)

            request_url = reverse('wamanageavatar', args=[user_id, "upload"])
            data = {
                'filename': 'avatar.png',
                "photo": temp
            }
            csrf_response(self.django_client, request_url, 'post', data,
                          status_code=302, test_csrf_required=False)
        finally:
            temp.close()

        # Crop avatar
        request_url = reverse('wamanageavatar', args=[user_id, "crop"])
        data = {
            'x1': 50,
            'x2': 150,
            'y1': 50,
            'y2': 150
        }
        csrf_response(self.django_client, request_url, 'post', data,
                      status_code=302, test_csrf_required=False)

    def test_create_group(self):
        uuid = self.uuid()
        request_url = reverse('wamanagegroupid', args=["create"])
        data = {
            "name": uuid,
            "description": uuid,
            "permissions": 0
        }
        post(self.django_root_client, request_url, data, status_code=302)

    def test_create_user(self):
        uuid = self.uuid()
        groupid = self.new_group().id.val
        request_url = reverse('wamanageexperimenterid', args=["create"])
        data = {
            "odename": uuid,
            "first_name": uuid,
            "last_name": uuid,
            "active": "on",
            "default_group": groupid,
            "other_groups": groupid,
            "password": uuid,
            "confirmation": uuid,
            "role": "user",
        }
        post(self.django_root_client, request_url, data, status_code=302)

    def test_edit_group(self):
        group = self.new_group(perms="rw----")
        request_url = reverse('wamanagegroupid', args=["save", group.id.val])
        data = {
            "name": group.name.val,
            "description": "description",
            "permissions": 0
        }
        post(self.django_root_client, request_url, data, status_code=302)

    def test_edit_user(self):
        user = self.new_user()
        request_url = reverse('wamanageexperimenterid',
                              args=["save", user.id.val])
        data = {
            "odename": user.odeName.val,
            "first_name": user.firstName.val,
            "last_name": user.lastName.val,
            "default_group": user.copyGroupExperimenterMap()[0].parent.id.val,
            "other_groups": user.copyGroupExperimenterMap()[0].parent.id.val,
        }
        post(self.django_root_client, request_url, data, status_code=302)

    def test_edit_group_by_owner(self):

        self.add_groups(experimenter=self.user, groups=[self.group],
                        owner=True)

        request_url = reverse('wamanagegroupownerid',
                              args=["save", self.group.id.val])
        data = {
            "members": self.user.id.val,
            "owners": self.user.id.val,
            "permissions": 0
        }
        post(self.django_client, request_url, data, status_code=302)

    def test_change_password(self):
        user = self.new_user()
        request_url = reverse('wamanagechangepasswordid', args=[user.id.val])
        data = {
            "old_password": self.root.ic.getProperties().getProperty(
                'ode.rootpass'),
            "password": "new",
            "confirmation": "new"
        }
        post(self.django_root_client, request_url, data)

    def test_su(self):

        """
        CSRF protection does not check `GET` requests so we need to be sure
        that this request results in an HTTP 405 (method not allowed) status
        code.
        """

        user = self.new_user()

        request_url = reverse('webgateway_su', args=[user.odeName.val])

        get(self.django_root_client, request_url, {}, csrf=True)
        post(self.django_root_client, request_url)
