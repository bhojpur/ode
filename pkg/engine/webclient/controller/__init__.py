#!/usr/bin/env python
# -*- coding: utf-8 -*-
#

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