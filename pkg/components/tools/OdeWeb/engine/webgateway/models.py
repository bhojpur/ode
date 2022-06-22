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

# webgateway/model.py - django application model description
#
# from django.db import models
#
# import ode
#
# class StoredConnection(models.Model):
#     # To make things play good with MSIE, the url size limit is < 2083
#     base_path = models.CharField(max_length=20)
#     config_file = models.CharField(max_length=200, blank=True)
#     username = models.CharField(max_length=200, blank=True)
#     password = models.CharField(max_length=20, blank=True)
#     failcount = models.PositiveIntegerField(default=0)
#     enabled = models.BooleanField(default=True)
#     admin_group = models.CharField(max_length=80, blank=True)
#     annotations = models.TextField(blank=True)
#     site_message = models.TextField(blank=True)
#
# def getOdeGateway (self, trysuper=True):
#     rv = engine.client_wrapper(
#         self.username, self.password, self,
#         group=trysuper and str(self.admin_group) or None,try_super=trysuper,
#         extra_config=self.config_file)
#    rv.conn = self
#    return rv
#
#  def getProperty (self, key):
#    for e in [x.split(':') for x in self.annotations.split('\n')]:
#      if e[0].strip() == key:
#        if len(e) < 2:
#          return True
#        return (':'.join(e[1:])).strip()
#    return None
#
#  def getMessage (self):
#    return self.site_message