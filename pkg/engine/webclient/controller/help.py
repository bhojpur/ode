#!/usr/bin/env python
# -*- coding: utf-8 -*-

from engine.webclient.controller import BaseController

class BaseHelp(BaseController):
    def __init__(self, conn, **kw):
        BaseController.__init__(self, conn)