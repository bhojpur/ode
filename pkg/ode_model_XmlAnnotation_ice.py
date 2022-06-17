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

from sys import version_info as _version_info_
import Ice, IcePy
import ode_model_IObject_ice
import ode_RTypes_ice
import ode_model_RTypes_ice
import ode_System_ice
import ode_Collections_ice
import ode_model_TextAnnotation_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Start of module ode
__name__ = 'ode'

# Start of module ode.model
__name__ = 'ode.model'

if 'AnnotationAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_AnnotationAnnotationLink = IcePy.declareClass('::ode::model::AnnotationAnnotationLink')
    _M_ode.model._t_AnnotationAnnotationLinkPrx = IcePy.declareProxy('::ode::model::AnnotationAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'XmlAnnotation' not in _M_ode.model.__dict__:
    _M_ode.model.XmlAnnotation = Ice.createTempClass()
    class XmlAnnotation(_M_ode.model.TextAnnotation):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _ns=None, _name=None, _description=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _textValue=None):
            if Ice.getType(self) == _M_ode.model.XmlAnnotation:
                raise RuntimeError('ode.model.XmlAnnotation is an abstract class')
            _M_ode.model.TextAnnotation.__init__(self, _id, _details, _loaded, _version, _ns, _name, _description, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner, _textValue)

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Annotation', '::ode::model::IObject', '::ode::model::TextAnnotation', '::ode::model::XmlAnnotation')

        def ice_id(self, current=None):
            return '::ode::model::XmlAnnotation'

        def ice_staticId():
            return '::ode::model::XmlAnnotation'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_XmlAnnotation)

        __repr__ = __str__

    _M_ode.model.XmlAnnotationPrx = Ice.createTempClass()
    class XmlAnnotationPrx(_M_ode.model.TextAnnotationPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.XmlAnnotationPrx.ice_checkedCast(proxy, '::ode::model::XmlAnnotation', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.XmlAnnotationPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::XmlAnnotation'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_XmlAnnotationPrx = IcePy.defineProxy('::ode::model::XmlAnnotation', XmlAnnotationPrx)

    _M_ode.model._t_XmlAnnotation = IcePy.declareClass('::ode::model::XmlAnnotation')

    _M_ode.model._t_XmlAnnotation = IcePy.defineClass('::ode::model::XmlAnnotation', XmlAnnotation, -1, (), True, False, _M_ode.model._t_TextAnnotation, (), ())
    XmlAnnotation._ice_type = _M_ode.model._t_XmlAnnotation

    _M_ode.model.XmlAnnotation = XmlAnnotation
    del XmlAnnotation

    _M_ode.model.XmlAnnotationPrx = XmlAnnotationPrx
    del XmlAnnotationPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
