#!/usr/bin/env python
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
/*
 *   $Id$
 */
"""

import Ice
import IceImport

IceImport.load("ode_model_Details_ice")
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"

class DetailsI(_ode_model.Details):

    def __init__(self, client=None):
        super(DetailsI, self).__init__()
        self.__client = client
        self.__session = None
        if client:
            self.__session = client.getSession(False)

    def getClient(self):
        return self.__client

    def getSession(self):
        return self.__session

    def getEventContext(self):
        return self._event

    def getCallContext(self):
        return self._call

    def getOwner(self):
        return self._owner

    def setOwner(self, value):
        self._owner = value
        pass

    def getGroup(self):
        return self._group

    def setGroup(self, value):
        self._group = value
        pass

    def getCreationEvent(self):
        return self._creationEvent

    def setCreationEvent(self, value):
        self._creationEvent = value
        pass

    def getUpdateEvent(self):
        return self._updateEvent

    def setUpdateEvent(self, value):
        self._updateEvent = value
        pass

    def getPermissions(self):
        return self._permissions

    def setPermissions(self, value):
        self._permissions = value
        pass

    def getExternalInfo(self):
        return self._externalInfo

    def setExternalInfo(self, value):
        self._externalInfo = value
        pass

    def ice_postUnmarshal(self):
        """
        Provides additional initialization once all data loaded
        Required due to __getattr__ implementation.
        """
        pass  # Currently unused

    def ice_preMarshal(self):
        """
        Provides additional validation before data is sent
        Required due to __getattr__ implementation.
        """
        pass  # Currently unused

    def __getattr__(self, attr):
        if attr == "owner":
            return self.getOwner()
        elif attr == "group":
            return self.getGroup()
        elif attr == "creationEvent":
            return self.getCreationEvent()
        elif attr == "updateEvent":
            return self.getUpdateEvent()
        elif attr == "permissions":
            return self.getPermissions()
        elif attr == "externalInfo":
            return self.getExternalInfo()
        else:
            raise AttributeError(attr)

    def __setattr__(self, attr, value):
        if attr.startswith("_"):
            self.__dict__[attr] = value
        else:
            try:
                object.__getattribute__(self, attr)
                object.__setattr__(self, attr, value)
            except AttributeError:
                if attr == "owner":
                    return self.setOwner(value)
                elif attr == "group":
                    return self.setGroup(value)
                elif attr == "creationEvent":
                    return self.setCreationEvent(value)
                elif attr == "updateEvent":
                    return self.setUpdateEvent(value)
                elif attr == "permissions":
                    return self.setPermissions(value)
                elif attr == "externalInfo":
                    return self.setExternalInfo(value)
                else:
                    raise

_ode_model.DetailsI = DetailsI