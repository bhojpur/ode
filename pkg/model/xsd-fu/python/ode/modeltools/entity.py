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

from ode.modeltools import config
from ode.modeltools import language
from ode.modeltools.exceptions import ModelProcessingError

class ODEModelEntity(object):
    """
    An abstract root class for properties and model objects containing
    common type resolution and text processing functionality.
    """

    def resolveLangTypeFromSimpleType(self, simpleTypeName):
        getSimpleType = self.model.getTopLevelSimpleType
        while True:
            simpleType = getSimpleType(simpleTypeName)
            if simpleType is None:
                logging.debug("No simpleType found with name: %s"
                              % simpleTypeName)
                # Handle cases where the simple type is prefixed by
                # a namespace definition. (e.g. ODE:LSID).
                namespaceless = simpleTypeName.split(':')[-1]
                if namespaceless != simpleTypeName:
                    simpleTypeName = namespaceless
                    continue
                break
            logging.debug("%s simpleType dump: %s"
                          % (self, simpleType.__dict__))
            # It's possible the simpleType is a union of other
            # simpleTypes so we need to handle that. We assume
            # that all the unioned simpleTypes are of the same
            # base type (ex. "xsd:string" or "xsd:float").
            if simpleType.unionOf:
                union = getSimpleType(simpleType.unionOf[0])
                if self.model.opts.lang.hasType(union.getBase()):
                    return self.model.opts.lang.type(union.getBase())
                else:
                    simpleTypeName = union.getBase()
            if self.model.opts.lang.hasType(simpleType.getBase()):
                return self.model.opts.lang.type(simpleType.getBase())
            else:
                simpleTypeName = simpleType.getBase()
            # TODO: The above logic looks wrong.  simpleTypeName is
            # asigned but not used and then nothing is returned.

    def lowerCasePrefix(self, v):
        if v is None:
            raise ModelProcessingError(
                'Cannot lower case %s on %s' % (v, self.name))
        match = config.PREFIX_CASE_REGEX.match(v)
        if match is None:
            raise ModelProcessingError(
                'No prefix match for %s on %s' % (v, self.name))
        prefix, = [_f for _f in match.groups() if _f]
        return prefix.lower() + v[len(prefix):]

    def _get_argumentName(self):
        argumentName = config.REF_REGEX.sub('', self.name)
        argumentName = self.lowerCasePrefix(argumentName)
        return argumentName
    argumentName = property(
        _get_argumentName,
        doc="""The property's argument name (camelCase).""")

    def _get_methodName(self):
        try:
            name = config.BACK_REFERENCE_NAME_OVERRIDE[self.key]
            return name[0].upper() + name[1:]
        except (KeyError, AttributeError):
            pass
        return config.BACKREF_REGEX.sub(
            '', config.REF_REGEX.sub('', self.name))
    methodName = property(
        _get_methodName,
        doc="""The property's method name.""")

    def _get_isGlobal(self):
        isGlobal = self._isGlobal
        try:
            if self.isBackReference:
                ref = self.model.getObjectByName(
                    config.BACKREF_REGEX.sub('', self.type))
                if ref.name == self.name:
                    return isGlobal
                return isGlobal or ref.isGlobal
        except AttributeError:
            pass
        if self.isReference:
            ref = self.model.getObjectByName(
                config.REF_REGEX.sub('', self.type))
            if ref.name == self.name:
                return isGlobal
            isGlobal = isGlobal or ref.isGlobal
        return isGlobal
    isGlobal = property(
        _get_isGlobal,
        doc="""Whether or not the model object is a Bhojpur ODE system type.""")

    def _get_isManyToMany(self):
        try:
            if self.isBackReference:
                reference_to = self.model.getObjectByName(self.type)
                for prop in reference_to.properties.values():
                    if prop.type == self.parent.name + 'Ref':
                        return prop.isManyToMany
        except AttributeError:
            pass
        return self.manyToMany
    isManyToMany = property(
        _get_isManyToMany,
        doc="""Whether or not the entity is a many-to-many reference.""")

    def _get_isSettings(self):
        return self.name.endswith('Settings')
    isSettings = property(
        _get_isSettings,
        doc="""Whether or not the entity is a Settings reference.""")