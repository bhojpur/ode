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
import ode_model_TypeAnnotation_ice

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

if 'OriginalFile' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFile = IcePy.declareClass('::ode::model::OriginalFile')
    _M_ode.model._t_OriginalFilePrx = IcePy.declareProxy('::ode::model::OriginalFile')

if 'AnnotationAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_AnnotationAnnotationLink = IcePy.declareClass('::ode::model::AnnotationAnnotationLink')
    _M_ode.model._t_AnnotationAnnotationLinkPrx = IcePy.declareProxy('::ode::model::AnnotationAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'FileAnnotation' not in _M_ode.model.__dict__:
    _M_ode.model.FileAnnotation = Ice.createTempClass()
    class FileAnnotation(_M_ode.model.TypeAnnotation):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _ns=None, _name=None, _description=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _file=None):
            if Ice.getType(self) == _M_ode.model.FileAnnotation:
                raise RuntimeError('ode.model.FileAnnotation is an abstract class')
            _M_ode.model.TypeAnnotation.__init__(self, _id, _details, _loaded, _version, _ns, _name, _description, _annotationLinksSeq, _annotationLinksLoaded, _annotationLinksCountPerOwner)
            self._file = _file

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Annotation', '::ode::model::FileAnnotation', '::ode::model::IObject', '::ode::model::TypeAnnotation')

        def ice_id(self, current=None):
            return '::ode::model::FileAnnotation'

        def ice_staticId():
            return '::ode::model::FileAnnotation'
        ice_staticId = staticmethod(ice_staticId)

        def getFile(self, current=None):
            pass

        def setFile(self, theFile, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_FileAnnotation)

        __repr__ = __str__

    _M_ode.model.FileAnnotationPrx = Ice.createTempClass()
    class FileAnnotationPrx(_M_ode.model.TypeAnnotationPrx):

        def getFile(self, _ctx=None):
            return _M_ode.model.FileAnnotation._op_getFile.invoke(self, ((), _ctx))

        def begin_getFile(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.FileAnnotation._op_getFile.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFile(self, _r):
            return _M_ode.model.FileAnnotation._op_getFile.end(self, _r)

        def setFile(self, theFile, _ctx=None):
            return _M_ode.model.FileAnnotation._op_setFile.invoke(self, ((theFile, ), _ctx))

        def begin_setFile(self, theFile, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.FileAnnotation._op_setFile.begin(self, ((theFile, ), _response, _ex, _sent, _ctx))

        def end_setFile(self, _r):
            return _M_ode.model.FileAnnotation._op_setFile.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.FileAnnotationPrx.ice_checkedCast(proxy, '::ode::model::FileAnnotation', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.FileAnnotationPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::FileAnnotation'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_FileAnnotationPrx = IcePy.defineProxy('::ode::model::FileAnnotation', FileAnnotationPrx)

    _M_ode.model._t_FileAnnotation = IcePy.declareClass('::ode::model::FileAnnotation')

    _M_ode.model._t_FileAnnotation = IcePy.defineClass('::ode::model::FileAnnotation', FileAnnotation, -1, (), True, False, _M_ode.model._t_TypeAnnotation, (), (('_file', (), _M_ode.model._t_OriginalFile, False, 0),))
    FileAnnotation._ice_type = _M_ode.model._t_FileAnnotation

    FileAnnotation._op_getFile = IcePy.Operation('getFile', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OriginalFile, False, 0), ())
    FileAnnotation._op_setFile = IcePy.Operation('setFile', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), None, ())

    _M_ode.model.FileAnnotation = FileAnnotation
    del FileAnnotation

    _M_ode.model.FileAnnotationPrx = FileAnnotationPrx
    del FileAnnotationPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
