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

from django.conf import settings

PAGE = settings.PAGE

class BaseController(object):

    conn = None

    def __init__(self, conn, **kw):
        self.conn = conn

    def getShareId(self):
        return self.conn.getShareId()

    ###########################################################
    # Paging

    def doPaging(self, page, page_size, total_size, limit=PAGE):
        total = list()
        t = (total_size // limit) + (total_size % limit > 0 and 1 or 0)
        if total_size > (limit * 10):
            if page > 10:
                total.append(-1)
            for i in range(
                (1, page - 9)[page - 9 >= 1], (t + 1, page + 10)[page + 9 < t]
            ):
                total.append(i)
            if page < t - 9:
                total.append(-1)

        elif total_size > limit and total_size <= (limit * 10):
            for i in range(1, t + 1):
                total.append(i)
        else:
            total.append(1)
        next = None
        if page_size == limit and (page * limit) < total_size:
            next = page + 1
        prev = None
        if page > 1:
            prev = page - 1
        if len(total) > 1:
            return {"page": page, "total": total, "next": next, "prev": prev}
        return None